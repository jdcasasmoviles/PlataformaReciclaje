package com.plataforma.reciclaje.repository.impl;
import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.repository.UserRepository;
import com.plataforma.reciclaje.repository.config.DBHelper;
import com.plataforma.reciclaje.repository.entity.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepositoryImpl implements UserRepository {
    
        @Override
    public User findUsuario(String usuario, String password) { 
    User usuarioLogin = null;
    Connection  conexion=DBHelper.openDatabase(); 
    try{
         String sqlConsulta="SELECT "+Usuario.COL_ID_USUARIO+","
                 +Usuario.COL_NOMBRE+","
                  +Usuario.COL_EMAIL+","
                 +Usuario.COL_POINT
                 +" FROM "+Usuario.TABLE_NAME+" WHERE "+
                 Usuario.COL_ID_USUARIO+" = '"+usuario+"' AND "+
                  Usuario.COL_PASSWORD+" = '"+password+"'  ";
         System.out.println("UserRepositoryImpl findUsuario "+sqlConsulta);
         PreparedStatement  preparedStatement=conexion.prepareStatement(sqlConsulta);
        try (ResultSet resulset = preparedStatement.executeQuery()) {
            while(resulset.next()){
                System.out.println("UserRepositoryImpl findUsuario resulset "+resulset);
                usuarioLogin = new User(
                        resulset.getString(1),
                        resulset.getString(2),
                        resulset.getString(3),
                       Integer.parseInt( resulset.getString(4))
                ); 
                 return usuarioLogin;
            }
        }
        conexion.close();
     }catch (SQLException e){
        try {
            System.out.println( "Exception UserRepositoryImpl findUsuario  "+ e);
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    return usuarioLogin;
    }

    @Override
    public Optional<User> findById(String id) { 
    User usuario = null;
    Connection  conexion=DBHelper.openDatabase(); 
    try{
         String sqlConsulta="SELECT "+Usuario.COL_ID_USUARIO+","
                 +Usuario.COL_NOMBRE+","
                  +Usuario.COL_EMAIL+","
                 +Usuario.COL_POINT
                 +" FROM "+Usuario.TABLE_NAME+" WHERE "+
                 Usuario.COL_ID_USUARIO+" = '"+id+"'";
         System.out.println("UserRepositoryImpl findById "+sqlConsulta);
         PreparedStatement  preparedStatement=conexion.prepareStatement(sqlConsulta);
        try (ResultSet resulset = preparedStatement.executeQuery()) {
            while(resulset.next()){
                System.out.println("UserRepositoryImpl resulset "+resulset);
                usuario = new User(
                        resulset.getString(1),
                        resulset.getString(2),
                        resulset.getString(3),
                       Integer.parseInt( resulset.getString(4))
                ); 
                 return Optional.ofNullable(usuario);
            }
        }
        conexion.close();
     }catch (SQLException e){
        try {
            System.out.println( "Exception UserRepositoryImpl findById "+ e);
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    return Optional.ofNullable(usuario);
    }

    @Override
    public void save(User user) { 
        Connection conexion = DBHelper.openDatabase();
        try {
            PreparedStatement preparedStatement;
            String sql = "insert into "+Usuario.TABLE_NAME+" ("
                    + Usuario.COL_ID_USUARIO + ","
                    + Usuario.COL_NOMBRE + ","
                    + Usuario.COL_PASSWORD + ","
                    + Usuario.COL_EMAIL + ","
                    + Usuario.COL_POINT+ ") values(?,?,?,?,?)";
            System.out.println("UserRepository save sql " + sql);
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
             preparedStatement.setString(3, user.getId());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, String.valueOf(user.getPoints()));
            preparedStatement.executeUpdate();
        }catch (Exception ex) {
           System.out.println("Exception UserRepository save " + ex);
         } finally {
            try {       
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }

    @Override
    public List<User> findAll() { 
        List<User> lista = new ArrayList();
        lista.clear();
        Connection conexion = DBHelper.openDatabase();
        try {
              String sqlConsulta="SELECT "+Usuario.COL_ID_USUARIO+","
                 +Usuario.COL_NOMBRE+","
                  +Usuario.COL_EMAIL+","
                 +Usuario.COL_POINT
                 +" FROM "+Usuario.TABLE_NAME+" ";
            System.out.println("UserRepositoryImpl findAll "+sqlConsulta);
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlConsulta);
            try ( ResultSet resulset = preparedStatement.executeQuery()) {
                while (resulset.next()) {
                    User usuario  = new User();
                    usuario.setId(resulset.getString(1));
                    usuario.setName(resulset.getString(2));
                    usuario.setEmail(resulset.getString(3));
                    usuario.setPoints(Integer.parseInt(resulset.getString(4)));
                    lista.add(usuario);
                }
            }
            conexion.close();
        } catch (SQLException e) {
            try {
                System.out.println("Exception UserRepositoryImpl findAll " + e);
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(RecyclingRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    @Override
    public void update(User usuario) {
         try {
            PreparedStatement preparedStatement;
            try ( Connection conexion = DBHelper.openDatabase()) {
                String sql = "UPDATE " + Usuario.TABLE_NAME
                        + " SET " + Usuario.COL_POINT + " = " + usuario.getPoints()
                        + " WHERE " + Usuario.COL_ID_USUARIO + " = '" + usuario.getId() +"'";
                System.out.println("UserRepositoryImpl update "+sql);
                preparedStatement = conexion.prepareStatement(sql);
                preparedStatement.execute();
                conexion.close();
            }
        } catch (Exception e) {
            System.out.println("Exception UserRepositoryImpl update  " + e);
        }
    }
}