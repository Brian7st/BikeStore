package com.bikeStore.demo;

import com.bikeStore.demo.Entity.Rol;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.repository.RolRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // Crear roles si no existen
        if (rolRepository.findByNombre("ADMIN").isEmpty()) {
            rolRepository.save(new Rol("ADMIN", "Administrador", true, null));
        }
        if (rolRepository.findByNombre("EMPLEADO").isEmpty()) {
            rolRepository.save(new Rol("EMPLEADO", "Empleado", true, null));
        }

        // Crear admin inicial si no existe
        if (usuarioRepository.findByUserName("admin").isEmpty()) {
            Rol rolAdmin = rolRepository.findByNombre("ADMIN").get();
            Usuario admin = new Usuario();
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setDocument("00000000");
            admin.setActivo(true);
            admin.setRol(rolAdmin);
            usuarioRepository.save(admin);
        }
    }
}