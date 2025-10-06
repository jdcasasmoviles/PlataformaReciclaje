package com.plataforma.reciclaje.model;

public class Reward {
    private String id;
    private String name;
    private int costPoints;
    
    public Reward(){
    }
    
    public Reward(String id, String name, int costPoints) {
        this.id = id;
        this.name = name;
        this.costPoints = costPoints;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCostPoints() { return costPoints; }

    @Override
    public String toString() {
        return String.format("%s (%d pts)", getName(), getCostPoints());
    }
      
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCostPoints(int costPoints) {
        this.costPoints = costPoints;
    }
}