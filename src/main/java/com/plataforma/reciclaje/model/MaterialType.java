package com.plataforma.reciclaje.model;

import java.util.concurrent.ThreadLocalRandom;

public enum MaterialType {
    PLASTIC, PAPER, GLASS, METAL, OTHER;

    // ejemplo de puntos por unidad (puedes adaptar: por unidad o por kg)
public int pointsPerUnit() {
    switch (this) {
        case PLASTIC:
            return 5;
        case PAPER:
            return 10;
        case GLASS:
            return 8;
        case METAL:
            return 12;
        case OTHER:
        default:
            return 2;
    }
}

 public static MaterialType randomMaterial() {
        MaterialType[] values = MaterialType.values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}