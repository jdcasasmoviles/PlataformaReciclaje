package com.plataforma.reciclaje.service;

import com.plataforma.reciclaje.model.Reward;
import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.repository.impl.RewardRepositoryImpl;
import com.plataforma.reciclaje.repository.impl.UserRepositoryImpl;
import java.util.List;

public class RewardService {
    
    private final UserRepositoryImpl userRepo = new UserRepositoryImpl();
    private final RewardRepositoryImpl rewardRepo = new RewardRepositoryImpl();
    public RewardService(){}
   
    public boolean redeemReward(String userId, String rewardId) {
        var uOpt = userRepo.findById(userId);
        var rew = rewardRepo.findById(rewardId);
        if (uOpt.isEmpty() || rew == null) return false;
        User u = uOpt.get();
        if (u.consumePoints(rew.getCostPoints())) {
            userRepo.update(u);
            return true;
        }
        return false;
    }

   public List<Reward> listRewards() { return rewardRepo.findAll(); }

    public void save(Reward reward) {
        rewardRepo.save(reward);
    }
}
