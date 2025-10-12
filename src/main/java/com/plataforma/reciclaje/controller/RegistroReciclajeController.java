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
          generaRegistroreciclaje("COD00001","COD00001");
          generaRegistroreciclaje("COD00002","COD00002");
          generaRegistroreciclaje("COD00003","COD00003");
          generaRegistroreciclaje("COD00004","COD00004");
          generaRegistroreciclaje("COD00005","COD00005");
        }*/
    }

    public RecyclingRecord registerRecycling(User usuario,MaterialType material, double qty) {
        return recyclingService.registerRecycling(usuario, material, qty);
    }

    public List<RecyclingRecord> getHistory(User usuario) {
        return recyclingService.getUserHistory(usuario.getId());
    }
        
    public List<RecyclingRecord> findTotalReciclado(User usuario) { 
        return recyclingService.findTotalReciclado(usuario.getId()); 
    }

    private void generaRegistroreciclaje(String user,String password) {
        UsuarioController usuarioController = new UsuarioController();
        usuarioController.loginUsuario(user,password);
                
         MaterialType material = MaterialType.randomMaterial();
        int cantidad=(int)(Math.random() * (17)) + 4;
        recyclingService.registerRecycling(usuarioController.getCurrentUser(), material, cantidad);
        //recyclingService.save(new RecyclingRecord(idUsuario, material, cantidad, puntos));
    }

    public Iterable<RecyclingRecord> findAllTotalReciclado() {
  return recyclingService.findAllTotalReciclado();
    }
}
