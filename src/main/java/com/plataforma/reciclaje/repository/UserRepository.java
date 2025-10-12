package com.plataforma.reciclaje.repository;
import com.plataforma.reciclaje.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    void update(User usuario);
    User findUsuario(String usuario,String password);
}