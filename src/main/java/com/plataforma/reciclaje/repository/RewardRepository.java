package com.plataforma.reciclaje.repository;

import com.plataforma.reciclaje.model.Reward;
import java.util.List;

public interface RewardRepository {
    List<Reward> findAll();
    void save(Reward r);
    Reward findById(String id);
}
