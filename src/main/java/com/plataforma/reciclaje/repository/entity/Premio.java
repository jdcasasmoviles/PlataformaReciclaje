package com.plataforma.reciclaje.repository.entity;

public class Premio {
    public static final String TABLE_NAME = "TABLA_PREMIO";

    public static final String COL_ID = "COL_ID";
    public static final String COL_NAME = "COL_NAME";
    public static final String COL_COST_POINT = "COL_COST_POINT";
    
    public static final String CREATE_TABLE_PREMIO = "" +
            "CREATE TABLE IF NOT EXISTS "+ Premio.TABLE_NAME  + "(" +
            Premio.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            Premio.COL_NAME + " TEXT  ," +
            Premio.COL_COST_POINT + " TEXT" + ");";
}
