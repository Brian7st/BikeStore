package com.bikeStore.demo.service;

import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import com.bikeStore.demo.mapper.UsuarioMapper;
import com.bikeStore.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    // Crear usuario
    public UsuarioDtoResponse crear(UsuarioDtoRequest dto){
        Usuario usuario = usuarioMapper.toEntity(dto);
        Usuario guardado = usuarioRepository.save(usuario);
        return  usuarioMapper.toResponseDto(guardado);
    }

    //Listar todos
    public List<UsuarioDtoResponse> listarTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper :: toResponseDto)
                .toList();
    }
    // Buscar por ID
    public UsuarioDtoResponse buscarPorId(Integer id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponseDto(usuario);
    }

    // Actualizar
    public UsuarioDtoResponse actualizar (Integer id, UsuarioDtoRequest dto){
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        existente.setUsuario(dto.usuario());
        existente.setPassoword(dto.password());
        existente.setDocument(dto.document());
        existente.setTelefono(dto.telefono());
        return usuarioMapper.toResponseDto(usuarioRepository.save(existente));
    }

    // Eliminar
    public void eliminar(Integer id){
        if (!usuarioRepository.existsById(id)){
            throw new RuntimeException("usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
