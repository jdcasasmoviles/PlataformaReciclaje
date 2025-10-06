package com.plataforma.reciclaje.view.login;


import java.awt.*;
import javax.swing.*;

public class GradientPanel extends JPanel {
    private float hue = 0;

    public GradientPanel() {
        Timer timer = new Timer(50, e -> {
            hue += 0.01;
            if (hue > 1) hue = 0;
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, new Color(13, 110, 253), getWidth(), getHeight(), new Color(13, 110, 253));        
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}