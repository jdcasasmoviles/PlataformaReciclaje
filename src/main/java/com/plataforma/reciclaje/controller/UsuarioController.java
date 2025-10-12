package com.plataforma.reciclaje.controller;

import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.repository.config.DBHelper;
import com.plataforma.reciclaje.service.UsuarioService;
import java.util.List;

public class  UsuarioController{
    private User currentUser;
    private UsuarioService usuarioService = new UsuarioService();
    
    public UsuarioController(){
    DBHelper.createDatabase();
    /*
    usuarioService.registerUsuario("COD00001", "Celeste Ramos", "celeste@autonoma.com.pe");
    usuarioService.registerUsuario("COD00002", "Kimberly Quispe", "kquispe@autonoma.com.pe");
    usuarioService.registerUsuario("COD00003", "Sergio Soto", "ser.soto@autonoma.com.pe");*/
    }
    
    public List<User> listUsers() { return usuarioService.listUsers(); }
    public User getCurrentUser() { return currentUser; }
        //Register
    public boolean registerUsuario(String id, String name, String email) {
        currentUser = usuarioService.registerUsuario(id, name, email);
        return currentUser != null;
    }
    
    //AUth
    public boolean loginUsuario(String usuario, String password) {
        currentUser = usuarioService.loginUsuario(usuario, password);
        return currentUser != null;
    }
}
