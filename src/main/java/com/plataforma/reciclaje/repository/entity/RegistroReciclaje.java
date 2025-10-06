package com.plataforma.reciclaje.repository.entity;

public class RegistroReciclaje {
    public static final String TABLE_NAME = "TABLA_RECICLAJE";

    public static final String COL_ID = "COL_ID"; 
    public static final String COL_USUARIO_ID = "COL_USUARIO_ID"; 
    public static final String COL_MATERIAL = "COL_MATERIAL"; 
    public static final String COL_CANTIDAD = "COL_CANTIDAD"; 
    public static final String COL_PUNTOS = "COL_PUNTOS"; 
     public static final String COL_FECHA_REGISTRO = "COL_FECHA_REGISTRO"; 
    public static final String CREATE_TABLE_PREMIO = "" +
            "CREATE TABLE IF NOT EXISTS "+ RegistroReciclaje.TABLE_NAME  + "(" +
            RegistroReciclaje.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            RegistroReciclaje.COL_USUARIO_ID + " TEXT  ," +
             RegistroReciclaje.COL_MATERIAL + " TEXT  ," +
             RegistroReciclaje.COL_CANTIDAD + " TEXT  ," +
              RegistroReciclaje.COL_PUNTOS + " TEXT  ," +
            RegistroReciclaje.COL_FECHA_REGISTRO + " TEXT" + ");";
}
