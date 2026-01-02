package proyecto.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import proyecto.repository.UsuarioRepository;

@Primary
@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public JpaUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = usuarioRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email no encontrado: " + username));

        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getNombre()))
                .toList();

        return User.withUsername(u.getEmail())
                .password(u.getHashPassword())
                .authorities(authorities)
                .accountLocked(u.isCuentaBloqueada())
                .disabled(!u.isHabilitado())
                .build();
    }
}

