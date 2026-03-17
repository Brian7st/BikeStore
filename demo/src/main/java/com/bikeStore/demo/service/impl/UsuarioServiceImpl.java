package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Rol;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import com.bikeStore.demo.mapper.UsuarioMapper;
import com.bikeStore.demo.repository.RolRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import com.bikeStore.demo.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;


    @Override
    @Transactional
    public UsuarioDtoResponse registrarUsuario(UsuarioDtoRequest dto) {


        Rol rol = rolRepository.findByNombre("EMPLEADO")
                .orElseThrow(() -> new RuntimeException("Rol EMPLEADO no encontrado"));

        Usuario nuevoUsuario = usuarioMapper.toEntity(dto);
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.password()));

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        return usuarioMapper.toResponseDto(guardado);
    }

    //Listar todos
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDtoResponse> listarTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper :: toResponseDto)
                .toList();
    }
    // Buscar por ID
    @Override
    @Transactional(readOnly = true)
    public UsuarioDtoResponse buscarPorId(UUID id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponseDto(usuario);
    }

    // Actualizar
    @Override
    @Transactional
    public UsuarioDtoResponse actualizar(UUID id, UsuarioDtoRequest dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        existente.setUserName(dto.usuario());
        existente.setDocument(dto.document());
        existente.setTelefono(dto.telefono());

        if (dto.password() != null && !dto.password().isBlank()) {
            existente.setPassword(passwordEncoder.encode(dto.password()));
        }
        return usuarioMapper.toResponseDto(usuarioRepository.save(existente));
    }

    // Eliminar
    @Override
    @Transactional
    public void eliminar(UUID id){
        if (!usuarioRepository.existsById(id)){
            throw new RuntimeException("usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    @Transactional
    public UsuarioDtoResponse cambiarEstado(UUID id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(activo);
        return usuarioMapper.toResponseDto(usuarioRepository.save(usuario));
    }
}
