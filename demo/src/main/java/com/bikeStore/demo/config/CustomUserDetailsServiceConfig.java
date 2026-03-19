package com.bikeStore.demo.config;

import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsServiceConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = usuarioRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
            
            return User.builder()
                    .username(usuario.getUserName())
                    .password(usuario.getPassword())
                    .roles(usuario.getRol().getNombre())
                    .disabled(!usuario.isActivo())
                    .build();
        };
    }
}
