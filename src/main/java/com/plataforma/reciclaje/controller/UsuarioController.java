package com.plataforma.reciclaje.controller;

import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.service.UsuarioService;
import java.util.List;

public class  UsuarioController{
    private User currentUser;
    private UsuarioService usuarioService = new UsuarioService();
    
    public UsuarioController(){
    usuarioService.registerOrGetUser("COD00001", "Celeste Ramos", "celeste@autonoma.com.pe");
    usuarioService.registerOrGetUser("COD00002", "Kimberly Quispe", "kquispe@autonoma.com.pe");
    usuarioService.registerOrGetUser("COD00003", "Sergio Soto", "ser.soto@autonoma.com.pe");
    }
    
    public List<User> listUsers() { return usuarioService.listUsers(); }
    public User getCurrentUser() { return currentUser; }
        // Auth / Register
    public boolean loginOrRegister(String id, String name, String email) {
        currentUser = usuarioService.registerOrGetUser(id, name, email);
        return currentUser != null;
    }
}
