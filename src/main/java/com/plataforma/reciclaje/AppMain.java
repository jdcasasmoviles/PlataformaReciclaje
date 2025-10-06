package com.plataforma.reciclaje;
import com.plataforma.reciclaje.repository.config.DBHelper;
import com.plataforma.reciclaje.view.ConsoleView;

public class AppMain {
    public static void main(String[] args) {
        DBHelper.createDatabase();
        // Vista (consola)
        var view = new ConsoleView();
        // arrancar UI de consola
        view.start();
    }
}