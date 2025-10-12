package com.plataforma.reciclaje.repository.impl;
import com.plataforma.reciclaje.model.Reward;
import com.plataforma.reciclaje.repository.RewardRepository;
import com.plataforma.reciclaje.repository.config.DBHelper;
import com.plataforma.reciclaje.repository.entity.Premio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RewardRepositoryImpl implements RewardRepository {

    public RewardRepositoryImpl() {
    }

    @Override
    public List<Reward> findAll() { 
        List<Reward> lista = new ArrayList();
        lista.clear();
        Connection conexion = DBHelper.openDatabase();
        try {
            String sqlConsulta = "SELECT "+ Premio.COL_ID+","
                    + Premio.COL_NAME+","
                    + Premio.COL_COST_POINT+""
                    + " FROM " + Premio.TABLE_NAME+"";
             System.out.println("RewardRepositoryImpl  findAll "+sqlConsulta);
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlConsulta);
            try ( ResultSet resulset = preparedStatement.executeQuery()) {
                while (resulset.next()) {
                    Reward  reward = new Reward();
                    reward.setId(resulset.getLong(1));
                    reward.setName(resulset.getString(2));
                    reward.setCostPoints(Integer.parseInt(resulset.getString(3)));
                    lista.add(reward);
                }
            }
            conexion.close();
        } catch (SQLException e) {
            try {
                System.out.println("Exception RewardRepositoryImpl findAll " + e);
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(RecyclingRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    @Override
    public void save(Reward reward) {
                Random random = new Random();
        Connection conexion = DBHelper.openDatabase();
        try {
            PreparedStatement preparedStatement;
            String sql = "insert into "+Premio.TABLE_NAME+" ("
                    + Premio.COL_NAME + ","
                    + Premio.COL_COST_POINT+ ") values(?,?)";                       
            System.out.println("RewardRepositoryImpl save sql " + sql);
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, reward.getName());
             preparedStatement.setString(2, String.valueOf(reward.getCostPoints()));
            preparedStatement.executeUpdate();
        }catch (Exception ex) {
           System.out.println("Exception RewardRepositoryImpl save " + ex);
         } finally {
            try {       
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }

    @Override
    public Reward findById(String rewardId) { 
        Reward premio = new Reward();
        Connection conexion = DBHelper.openDatabase();
        try {
            String sqlConsulta = "SELECT "+ Premio.COL_ID+","
                    + Premio.COL_NAME+","
                    + Premio.COL_COST_POINT+""
                    + " FROM " + Premio.TABLE_NAME+""
                    + " WHERE "+Premio.COL_ID+" = "+rewardId+" ";
             System.out.println("RewardRepositoryImpl   findById  "+sqlConsulta);
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlConsulta);
            try ( ResultSet resulset = preparedStatement.executeQuery()) {
                while (resulset.next()) {
                    premio.setId(resulset.getLong(1));
                    premio.setName(resulset.getString(2));
                    premio.setCostPoints(Integer.parseInt(resulset.getString(3)));
                     return premio;
                }
            }
            conexion.close();
        } catch (SQLException e) {
            try {
                System.out.println("Exception RewardRepositoryImpl  findById  " + e);
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(RecyclingRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return premio;
    }
}