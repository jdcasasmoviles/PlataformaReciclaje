package com.plataforma.reciclaje.controller;

import com.plataforma.reciclaje.model.Reward;
import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.service.RewardService;
import java.util.List;


public class PremioController {
    RewardService rewardService = new RewardService();
    
    public PremioController(){
        rewardService.save(new Reward("Descuento Cafetería 10%", 100));
        rewardService.save(new Reward("Bono fotocopias 500 créditos", 200));
        rewardService.save(new Reward("Sorteo: Mochila ecológica", 300));
    }
    
    public boolean redeem(User currentUser,String rewardId) {
        return rewardService.redeemReward(currentUser.getId(), rewardId);
    }
        
    public List<Reward> listRewards() { return rewardService.listRewards(); }
}
