package com.plataforma.reciclaje;

import com.plataforma.reciclaje.repository.config.DBHelper;
import com.plataforma.reciclaje.view.SwingView;
import javax.swing.*;
public class Principal {
    public static void main(String[] args) {
        // Usar el look&feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            DBHelper.createDatabase();
            var view = new SwingView();
            view.setVisible(true);
        });
    }
}