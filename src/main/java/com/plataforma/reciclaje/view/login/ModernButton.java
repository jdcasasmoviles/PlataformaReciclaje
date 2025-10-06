package com.plataforma.reciclaje.view.login;

import java.awt.*;
import javax.swing.*;

public class ModernButton extends JButton {
    private Color hoverColor = new Color(100, 149, 237);
    private Color normalColor = new Color(70, 130, 180);
    private Color pressedColor = new Color(25, 25, 112);

    public ModernButton() {
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Hover efecto
        addChangeListener(e -> {
            if (getModel().isPressed()) {
                setBackground(pressedColor);
            } else if (getModel().isRollover()) {
                setBackground(hoverColor);
            } else {
                setBackground(normalColor);
            }
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
        g2.dispose();
    }
}