package com.plataforma.reciclaje;
import com.plataforma.reciclaje.repository.config.DBHelper;

public class AppMain {
    public static void main(String[] args) {
        DBHelper.createDatabase();
        // Vista (consola)
        var view = new ConsoleView();
        // arrancar UI de consola
        view.start();
    }
}