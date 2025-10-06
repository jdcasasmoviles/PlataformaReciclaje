package com.plataforma.reciclaje.repository.entity;

public class Usuario {
    public static final String TABLE_NAME = "TABLA_USUARIO";

    public static final String COL_ID_USUARIO = "COL_ID_USUARIO";
    public static final String COL_NOMBRE = "COL_NOMBRE";
    public static final String COL_PASSWORD = "COL_PASSWORD";
    public static final String COL_EMAIL = "COL_EMAIL";
    public static final String COL_POINT = "COL_POINT";
    
    public static final String CREATE_TABLE_USUARIO = "" +
            "CREATE TABLE IF NOT EXISTS "+ Usuario.TABLE_NAME  + "(" +
            Usuario.COL_ID_USUARIO + " TEXT  PRIMARY KEY," +
            Usuario.COL_NOMBRE + " TEXT  ," +
            Usuario.COL_PASSWORD + " TEXT  ," +
            Usuario.COL_EMAIL + " TEXT  ," +
            Usuario.COL_POINT + " TEXT" + ");";
}
