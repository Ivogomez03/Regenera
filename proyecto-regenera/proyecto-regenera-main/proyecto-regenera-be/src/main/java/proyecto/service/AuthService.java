package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.dto.*;
import proyecto.model.RolModel;
import proyecto.model.TokenModel;
import proyecto.model.UsuarioModel;
import proyecto.repository.RolRepository;
import proyecto.repository.TokenRepository;
import proyecto.repository.UsuarioRepository;
import proyecto.request_response.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.auth.auto-verify:true}")
    private boolean autoVerify;

    private final UsuarioRepository usuarioRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final RolRepository rolRepo;
    private final UserDetailsService uds;
    private final JwtTokenService jwt;
    private final MailService mailService;
    private final AuthenticationConfiguration authConfig;

    @Transactional
    public void register(RegistroRequest req) {

        if (!req.getPassword().equals(req.getRepeatPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (usuarioRepo.existsByNombreUsuario(req.getNombreUsuario())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso");
        }

        if (usuarioRepo.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        UsuarioModel u = new UsuarioModel();
        u.setNombreUsuario(req.getNombreUsuario());
        u.setEmail(req.getEmail());
        u.setHashPassword(encoder.encode(req.getPassword()));
        u.setHabilitado(autoVerify);  ///CAMBIAR A FALSE
        u.setRoles(defaultUserRoles());
        usuarioRepo.save(u);

        TokenModel t = new TokenModel();
        t.setIdToken(UUID.randomUUID());
        t.setUsuario(u);
        t.setToken(UUID.randomUUID().toString());
        t.setTipo("VERIFY");
        t.setEmitidoEn(OffsetDateTime.now());
        t.setExpiraEn(OffsetDateTime.now().plusHours(24));
        t.setVenceEn(t.getExpiraEn());
        t.setUsado(false);
        tokenRepo.save(t);

        //ELIMINAR!!!
        if (autoVerify) {
            verify(t.getToken());
        }
        //else {
        //    mailService.enviarVerificacion(u.getEmail(), t.getToken());
        //}
    }

    private Set<RolModel> defaultUserRoles() {
        RolModel userRole = rolRepo.findById(2L)
                .orElseGet(() -> rolRepo.findByNombre("ROLE_USER")
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Rol ROLE_USER no está configurado")));

        return new HashSet<>(List.of(userRole));
    }


    public AuthRequest login(LoginRequest req) {

        try {
            String email = resolveEmail(req);
            AuthenticationManager authManager = authConfig.getAuthenticationManager();
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, req.getPassword())
            );
            UserDetails ud = uds.loadUserByUsername(email);
            String token = jwt.generate(ud);
            return new AuthRequest(token, "Bearer", jwt.getExpMin() * 60);
        } catch (DisabledException e) {
            throw new IllegalArgumentException("La cuenta no está verificada.", e);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Credenciales inválidas.", e);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo autenticar.", e);
        }
    }

    @Transactional
    public void verify(String tokenStr){

        TokenModel t = tokenRepo.findByTokenAndTipoAndUsadoFalse(tokenStr, "VERIFY")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        if (t.getExpiraEn().isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }

        UsuarioModel u = t.getUsuario();
        u.setHabilitado(true);
        usuarioRepo.save(u);

        t.setUsado(true);
        tokenRepo.save(t);
    }

    @Transactional
    public void forgot(ForgotRequest req){

        UsuarioModel u = usuarioRepo.findByEmail(req.getEmail())
                .orElse(null);

        if (u == null) {
            return;
        }

        TokenModel t = new TokenModel();
        t.setIdToken(UUID.randomUUID());
        t.setUsuario(u);
        t.setToken(UUID.randomUUID().toString());
        t.setTipo("RESET");
        t.setEmitidoEn(OffsetDateTime.now());
        t.setExpiraEn(OffsetDateTime.now().plusHours(2));
        t.setUsado(false);
        tokenRepo.save(t);

        mailService.enviarReset(u.getEmail(), t.getToken());
    }

    @Transactional
    public void reset(ResetRequest req){

        TokenModel t = tokenRepo.findByTokenAndTipoAndUsadoFalse(req.getToken(), "RESET")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        if (t.getExpiraEn().isBefore(OffsetDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }
        UsuarioModel u = t.getUsuario();
        u.setHashPassword(encoder.encode(req.getNewPassword()));
        usuarioRepo.save(u);

        t.setUsado(true);
        tokenRepo.save(t);
    }


    public CurrentUserResponse currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay sesión activa");
        }

        List<String> authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new CurrentUserResponse(auth.getName(), authorities);
    }

    private String resolveEmail(LoginRequest req) {
        String email = req.getEmail();
        if (email != null) {
            return email;
        }

        throw new IllegalArgumentException("El email es obligatorio para iniciar sesión.");
    }
}