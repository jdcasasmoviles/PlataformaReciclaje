package com.plataforma.reciclaje.repository;
import com.plataforma.reciclaje.model.RecyclingRecord;
import java.util.List;

public interface RecyclingRepository {
    Long save(RecyclingRecord r);
    List<RecyclingRecord> findTotalReciclado(String usuarioId);
    List<RecyclingRecord> findByUserId(String userId) ;
    List<RecyclingRecord> findAllTotalReciclado();
}