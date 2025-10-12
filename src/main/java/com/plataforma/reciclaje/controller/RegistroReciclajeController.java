package com.plataforma.reciclaje.controller;
import com.plataforma.reciclaje.model.MaterialType;
import com.plataforma.reciclaje.model.RecyclingRecord;
import com.plataforma.reciclaje.model.User;
import com.plataforma.reciclaje.service.RecyclingService;
import java.util.List;

public class RegistroReciclajeController {
    RecyclingService recyclingService = new RecyclingService();
    
    public RegistroReciclajeController(){  
        /*
        for(int i=0;i<6;i++){
          generaRegistroreciclaje("COD00001");
          generaRegistroreciclaje("COD00002");
          generaRegistroreciclaje("COD00003");
        }*/
    }

    public RecyclingRecord registerRecycling(User usuario,MaterialType material, double qty) {
        return recyclingService.registerRecycling(usuario, material, qty);
    }

    public List<RecyclingRecord> getHistory(User usuario) {
        return recyclingService.getUserHistory(usuario.getId());
    }
        
    public List<RecyclingRecord> listAllRecords() { return recyclingService.allRecords(); }

    private void generaRegistroreciclaje(String idUsuario) {
         MaterialType material = MaterialType.randomMaterial();
        int cantidad=(int)(Math.random() * (17)) + 4;
        int puntosPesoUnitario = material.pointsPerUnit();
        int puntos = (int) Math.round(puntosPesoUnitario * cantidad);
        recyclingService.save(new RecyclingRecord(idUsuario, material, cantidad, puntos));
    }
}
