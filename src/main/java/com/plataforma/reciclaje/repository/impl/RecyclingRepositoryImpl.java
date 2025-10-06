package com.plataforma.reciclaje.repository.impl;
import com.plataforma.reciclaje.model.MaterialType;
import com.plataforma.reciclaje.model.RecyclingRecord;
import com.plataforma.reciclaje.repository.RecyclingRepository;
import com.plataforma.reciclaje.repository.config.DBHelper;
import com.plataforma.reciclaje.repository.entity.RegistroReciclaje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecyclingRepositoryImpl implements RecyclingRepository {
    private final List<RecyclingRecord> store = new ArrayList<>();

    @Override
    public Long  save(RecyclingRecord recyclingRecord) {
         long generatedId = -1; // valor por defecto si algo falla       
        Connection conexion = DBHelper.openDatabase();
        try {
            PreparedStatement preparedStatement;
            String sql = "insert into "+RegistroReciclaje.TABLE_NAME+" ("
                    + RegistroReciclaje.COL_USUARIO_ID + ","
                    + RegistroReciclaje.COL_MATERIAL + ","
                    + RegistroReciclaje.COL_CANTIDAD + ","
                     + RegistroReciclaje.COL_PUNTOS + ","
                    + RegistroReciclaje.COL_FECHA_REGISTRO+ ") values(?,?,?,?,?)";
            System.out.println("RecyclingRepositoryImpl save sql " + sql);
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, recyclingRecord.getUserId());
            preparedStatement.setString(2, recyclingRecord.getMaterial().name());
            preparedStatement.setString(3, String.valueOf(recyclingRecord.getQuantity()));
            preparedStatement.setString(4, String.valueOf(recyclingRecord.getPoints()));
            preparedStatement.setString(5,String.valueOf(LocalDateTime.now()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
            //Recuperar el ID generado
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                     if (rs.next()) {
                    return rs.getLong(1);
                      }
                }
            }
        }catch (Exception ex) {
           System.out.println("Exception RecyclingRepositoryImpl save " + ex);
         } finally {
            try {       
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        return generatedId;
    }

    @Override
    public List<RecyclingRecord> findAll() { 
        List<RecyclingRecord> lista = new ArrayList();
        lista.clear();
        Connection conexion = DBHelper.openDatabase();
        try {
            String sqlConsulta = "SELECT "+ RegistroReciclaje.COL_ID+","
                    + RegistroReciclaje.COL_USUARIO_ID+","
                    + RegistroReciclaje.COL_MATERIAL+","
                    + RegistroReciclaje.COL_CANTIDAD+","
                    + RegistroReciclaje.COL_PUNTOS+","
                    + RegistroReciclaje.COL_FECHA_REGISTRO
                    + " FROM " + RegistroReciclaje.TABLE_NAME;
            System.out.println("RecyclingRepositoryImpl findAll  "+sqlConsulta);
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlConsulta);
            try ( ResultSet resulset = preparedStatement.executeQuery()) {
                while (resulset.next()) {
                    RecyclingRecord recyclingRecord = new RecyclingRecord();
                    recyclingRecord.setId(resulset.getLong(1));
                    recyclingRecord.setUserId(resulset.getString(2));
                    recyclingRecord.setMaterial(MaterialType.valueOf(resulset.getString(3).toUpperCase()));
                    recyclingRecord.setQuantity(Double.parseDouble(resulset.getString(4)));
                    recyclingRecord.setPoints(Integer.parseInt(resulset.getString(5)));
                    recyclingRecord.setFechaRegistro(LocalDateTime.parse(resulset.getString(6)));
                    lista.add(recyclingRecord);
                }
            }
            conexion.close();
        } catch (SQLException e) {
            try {
                System.out.println("Exception RecyclingRepositoryImpl findAll " + e);
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(RecyclingRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    @Override
    public List<RecyclingRecord> findByUserId(String userId)  {
        List<RecyclingRecord> lista = new ArrayList();
        lista.clear();
        Connection conexion = DBHelper.openDatabase();
        try {
            String sqlConsulta = "SELECT "+ RegistroReciclaje.COL_ID+","
                    + RegistroReciclaje.COL_USUARIO_ID+","
                    + RegistroReciclaje.COL_MATERIAL+","
                    + RegistroReciclaje.COL_CANTIDAD+","
                    + RegistroReciclaje.COL_PUNTOS+","
                    + RegistroReciclaje.COL_FECHA_REGISTRO
                    + " FROM " + RegistroReciclaje.TABLE_NAME
                    +" WHERE "+RegistroReciclaje.COL_USUARIO_ID+"= '"+userId+"'";
            System.out.println("RecyclingRepositoryImpl findByUserId "+sqlConsulta);
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlConsulta);
            try ( ResultSet resulset = preparedStatement.executeQuery()) {
                while (resulset.next()) {
                    RecyclingRecord recyclingRecord = new RecyclingRecord();
                    recyclingRecord.setId(resulset.getLong(1));
                    recyclingRecord.setUserId(resulset.getString(2));
                    recyclingRecord.setMaterial(MaterialType.valueOf(resulset.getString(3).toUpperCase()));
                    recyclingRecord.setQuantity(Double.parseDouble(resulset.getString(4)));
                    recyclingRecord.setPoints(Integer.parseInt(resulset.getString(5)));
                    recyclingRecord.setFechaRegistro(LocalDateTime.parse(resulset.getString(6)));
                    lista.add(recyclingRecord);
                }
            }
            conexion.close();
        } catch (SQLException e) {
            try {
                System.out.println("Exception RecyclingRepositoryImpl findByUserId " + e);
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(RecyclingRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
}