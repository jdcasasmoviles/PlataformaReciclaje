package com.plataforma.reciclaje.model;

import java.time.LocalDateTime;

public class RecyclingRecord {
    
    private Long id; // uuid u otro
    private String userId;// usuario id
    private MaterialType material;
    private double quantity; // cantifaf
    private int points;
    private LocalDateTime fechaRegistro;
    
    public RecyclingRecord(){
    }

    public RecyclingRecord(String userId, MaterialType material, double quantity, int points) {
        this.userId = userId;
        this.material = material;
        this.quantity = quantity;
        this.points = points;
    }

    public String getUserId() { return userId; }
    public MaterialType getMaterial() { return material; }
    public double getQuantity() { return quantity; }
    public int getPoints() { return points; }
        public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setMaterial(MaterialType material) {
        this.material = material;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
        @Override
    public String toString() {
        return String.format("id=%d id usuario=%s Material=%s  cantidad=%.2f puntos=%d", getId(), getUserId(), getMaterial(),getQuantity(),getPoints());
    }
}