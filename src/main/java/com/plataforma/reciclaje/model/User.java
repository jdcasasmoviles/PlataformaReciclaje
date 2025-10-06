package com.plataforma.reciclaje.model;
import java.util.Objects;

public class User {
    private String id; // e.g., codigo universitario
    private String name;
    private String email;
    private int points;
    //private final List<RecyclingRecord> history = new ArrayList<>();
    public User(){
    }

    public User(String id, String name, String email,int points) {
        this.id = Objects.requireNonNull(id);
        this.name = name;
        this.email = email;
        this.points = points;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getPoints() { return points; }

    public void addPoints(int p) { this.setPoints(this.getPoints() + p); }
    
    public boolean consumePoints(int p) {
        if (p <= getPoints()) { setPoints(getPoints() - p); return true; }
        return false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s (%s) - pts=%d", getName(), getId(), getPoints());
    }
}