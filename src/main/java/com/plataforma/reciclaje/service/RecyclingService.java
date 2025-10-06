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
    public RecyclingRecord registerRecycling(String userId, MaterialType material, double qty) {
        // regla de puntos: pts = floor(pointsPerUnit * qty)
        int ptsPerUnit = material.pointsPerUnit();
        int pts = (int) Math.round(ptsPerUnit * qty);
        RecyclingRecord rec = new RecyclingRecord(userId, material, qty, pts);
        Long idRecyclingRecord=recRepo.save(rec);
        rec.setId(idRecyclingRecord);
        // actualizar usuario
        userRepo.findById(userId).ifPresent(u -> {
            u.addPoints(pts);
            //u.addRecord(rec);
            userRepo.update(u);
        });
        return rec;
    }

   /*public boolean canRedeem(String userId, String rewardId) {
        var uOpt = userRepo.findById(userId);
        if (uOpt.isEmpty()) return false;
        var rew = rewardRepo.findById(rewardId);
        if (rew == null) return false;
        return uOpt.get().getPoints() >= rew.getCostPoints();
    }*/

    public List<RecyclingRecord> getUserHistory(String userId) {
        return recRepo.findByUserId(userId);
    }

    public List<RecyclingRecord> allRecords() { return recRepo.findAll(); }

    public void save(RecyclingRecord recyclingRecord) {
      recRepo.save(recyclingRecord);
    }
   
}