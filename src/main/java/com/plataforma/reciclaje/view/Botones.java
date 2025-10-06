/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plataforma.reciclaje.view;
import com.plataforma.reciclaje.controller.RegistroReciclajeController;
import com.plataforma.reciclaje.controller.PremioController;
import com.plataforma.reciclaje.model.MaterialType;
import com.plataforma.reciclaje.model.Reward;
import com.plataforma.reciclaje.model.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Botones {
    
    public static void botonRegistrar(RegistroReciclajeController controller,User usuario,JFrame menuPrincipal,JPanel items){
    JButton registrarBtn = new JButton("Registrar Reciclaje");
    registrarBtn=configuracionBoton(registrarBtn,items);
    registrarBtn.addActionListener(e -> {
    // ejemplo: abrir un diálogo con selección de material
    String[] options = {"PLASTIC", "PAPER", "GLASS", "METAL", "OTHER"};
    String material = (String) JOptionPane.showInputDialog(menuPrincipal,
            "Seleccione el material:", "Registrar Reciclaje",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (material != null) {
        controller.registerRecycling(usuario,MaterialType.valueOf(material), 1);
        JOptionPane.showMessageDialog(menuPrincipal, "¡Reciclaje registrado!");
    }
});
    }

      public static void botonHistorial(RegistroReciclajeController controller,User usuario,JFrame menuPrincipal,JPanel items){
      JButton historyBtn = new JButton("Ver Historial");
       historyBtn=configuracionBoton(historyBtn,items);
historyBtn.addActionListener(e -> {
    var records = controller.getHistory(usuario);
    StringBuilder sb = new StringBuilder("Historial:\n");
    records.forEach(r -> sb.append(r.toString()).append("\n"));
    JOptionPane.showMessageDialog(menuPrincipal, sb.toString());
});
      }
      
      public static void botonCanjearIncentivos(PremioController controller,User usuario,JFrame menuPrincipal,JPanel items){
            JButton redeemBtn = new JButton("Canjear Incentivo");
             redeemBtn=configuracionBoton(redeemBtn,items);
redeemBtn.addActionListener(e -> {
    var rewards = controller.listRewards();
    Reward selected = (Reward) JOptionPane.showInputDialog(menuPrincipal,
            "Seleccione un incentivo:", "Canjear",
            JOptionPane.QUESTION_MESSAGE, null,
            rewards.toArray(), rewards.get(0));
    if (selected != null) {
        controller.redeem(usuario,selected.getName());
        JOptionPane.showMessageDialog(menuPrincipal, "Incentivo canjeado: " + selected.getName());
    }
});
      }
      
        public static void botonReportes(JFrame menuPrincipal,JPanel items){
            JButton reportesBtn = new JButton("Reportes");
    reportesBtn=configuracionBoton(reportesBtn,items);
    reportesBtn.addActionListener(e -> {
    // logica por agregar
});
        }
        
           public static void botonCerrarSesion(JFrame menuPrincipal,JPanel items){
                       JButton cerrarSesionBtn = new JButton("Cerrar Sesion");
    cerrarSesionBtn=configuracionBoton(cerrarSesionBtn,items);
    cerrarSesionBtn.addActionListener(e -> {
    // logica por agregar
});
           }
    private static JButton configuracionBoton(JButton b,JPanel items) {
                b.setAlignmentX(Component.LEFT_ALIGNMENT);
                b.setMaximumSize(new Dimension(200, 42));
                b.setPreferredSize(new Dimension(200, 42));
                b.setBackground(new Color(245,245,245));
                b.setFocusPainted(false);
                b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
                b.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        b.setBackground(new Color(235, 235, 235));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        b.setBackground(new Color(245,245,245));
                    }
                });
                items.add(Box.createVerticalStrut(8));
                items.add(b);
                return b;
    }

}
