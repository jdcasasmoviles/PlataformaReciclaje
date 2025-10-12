package com.plataforma.reciclaje.service;

import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.repository.impl.UserRepositoryImpl;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
     private final UserRepositoryImpl userRepo = new UserRepositoryImpl();
     
     public UsuarioService(){}
     public List<User> findTopUsers() { return userRepo.findTopUsers(); }
     
    // Registro (registro si no existe)
    public User registerUsuario(String id, String name, String email) {
        Optional<User> op = userRepo.findById(id);
        if (op.isPresent()) {
            System.out.println("Ya registrado usuario "+id);
            return null;
        }
        User u = new User(id, name, email,0);
        userRepo.save(u);
        return u;
    }
    
        // Registro / login básico (registro si no existe)
    public User loginUsuario(String usuario, String password) {
        User usuarioLogin = userRepo.findUsuario(usuario, password);
        return usuarioLogin;
    }
    
}
