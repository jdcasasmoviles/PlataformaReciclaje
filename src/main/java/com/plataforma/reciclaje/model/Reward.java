package com.plataforma.reciclaje.model;

public class Reward {
    private Long id;
    private String name;
    private int costPoints;
    
    public Reward(){
    }
    
    public Reward(String name, int costPoints) {
        this.name = name;
        this.costPoints = costPoints;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getCostPoints() { return costPoints; }

    @Override
    public String toString() {
        return String.format("%s (%d pts)", getName(), getCostPoints());
    }
      
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCostPoints(int costPoints) {
        this.costPoints = costPoints;
    }
}