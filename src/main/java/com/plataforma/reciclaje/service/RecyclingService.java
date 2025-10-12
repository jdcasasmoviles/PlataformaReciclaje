package com.plataforma.reciclaje.service;
import com.plataforma.reciclaje.model.*;
import com.plataforma.reciclaje.repository.impl.RecyclingRepositoryImpl;
import com.plataforma.reciclaje.repository.impl.RewardRepositoryImpl;
import com.plataforma.reciclaje.repository.impl.UserRepositoryImpl;
import java.util.List;

public class RecyclingService {
    private final UserRepositoryImpl userRepo = new UserRepositoryImpl();
    private final RecyclingRepositoryImpl recRepo = new RecyclingRepositoryImpl();
    private final RewardRepositoryImpl rewardRepo = new RewardRepositoryImpl();

    public RecyclingService() {
    }

    // Registrar reciclaje: calcula puntos y guarda
    public RecyclingRecord registerRecycling(User usuario, MaterialType material, double qty) {
        int ptsPerUnit = material.pointsPerUnit();
        int pts = (int) Math.round(ptsPerUnit * qty);
        RecyclingRecord rec = new RecyclingRecord(usuario.getId(), material, qty, pts);
        Long idRecyclingRecord=recRepo.save(rec);
        rec.setId(idRecyclingRecord);
        // actualizar usuario
        usuario.addPoints(pts);
        userRepo.update(usuario);
        return rec;
    }

    public List<RecyclingRecord> getUserHistory(String userId) {
        return recRepo.findByUserId(userId);
    }

    public List<RecyclingRecord> findTotalReciclado(String userId) { 
        return recRepo.findTotalReciclado(userId); 
    }

    public void save(RecyclingRecord recyclingRecord) {
      recRepo.save(recyclingRecord);
    }

    public List<RecyclingRecord> findAllTotalReciclado() {
   return recRepo.findAllTotalReciclado(); 
    }
   
}