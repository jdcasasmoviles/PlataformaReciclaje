package com.plataforma.reciclaje.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;

public class HamburgerToggle extends JButton {
    private boolean open = true;

    public HamburgerToggle() {
        setText("≡"); // símbolo de menú
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("Segoe UI", Font.BOLD, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(new Color(33, 33, 33));

        // Animación simple de cambio de símbolo
        addActionListener(e -> toggle());
    }

    public void toggle() {
        open = !open;
        setText(open ? "≡" : "×"); // Cambia entre ≡ y ×
    }

    public boolean isOpen() {
        return open;
    }
}