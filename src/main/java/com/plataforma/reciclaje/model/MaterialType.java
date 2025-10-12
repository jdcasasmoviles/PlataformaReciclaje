package com.plataforma.reciclaje.model;

import java.util.concurrent.ThreadLocalRandom;

public enum MaterialType {
    PLASTICO, PAPEL, VIDRIO, METAL, OTRO;

    // ejemplo de puntos por unidad (puedes adaptar: por unidad o por kg)
public int pointsPerUnit() {
    switch (this) {
        case PLASTICO:
            return 5;
        case PAPEL:
            return 10;
        case VIDRIO:
            return 8;
        case METAL:
            return 12;
        case OTRO:
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