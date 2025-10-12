package com.plataforma.reciclaje.controller;

import com.plataforma.reciclaje.model.Reward;
import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.service.RewardService;
import java.util.List;


public class PremioController {
    RewardService rewardService = new RewardService();
    
    public PremioController(){
        /*
        rewardService.save(new Reward("Reclama una galleta en el kiosko", 50));
        rewardService.save(new Reward("Bono fotocopias 10 créditos", 100));
        rewardService.save(new Reward("Descuento Cafetería 10%", 200));
        rewardService.save(new Reward("Sorteo: Mochila ecológica", 300));
        rewardService.save(new Reward("Kit Universitario", 400));
        rewardService.save(new Reward("Un llavero destapador", 500));
        rewardService.save(new Reward("Descuento Cafetería 30%", 1000));
        rewardService.save(new Reward("Un polo autonoma", 2000));*/
        
    }
    
    public boolean redeem(User currentUser,String rewardId) {
        return rewardService.redeemReward(currentUser, rewardId);
    }
        
    public List<Reward> listRewards() { return rewardService.listRewards(); }
}
