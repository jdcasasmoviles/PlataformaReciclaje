package com.plataforma.reciclaje.view;

import com.plataforma.reciclaje.controller.PremioController;
import com.plataforma.reciclaje.controller.RegistroReciclajeController;
import com.plataforma.reciclaje.controller.UsuarioController;
import com.plataforma.reciclaje.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SwingView extends JFrame {
    RegistroReciclajeController registroReciclajeController=new RegistroReciclajeController();
    PremioController premioController = new PremioController();
    UsuarioController usuarioController = new UsuarioController();
    private User currentUser;

    // Componentes
    private JTextField txtId, txtName, txtEmail;
    private JLabel lblWelcome, lblPoints;
    private JTable tblHistory, tblRewards;
    private DefaultTableModel modelHistory, modelRewards;

    public SwingView() {
        super("Plataforma de Reciclaje Inteligente");
        initUI();
    }

    private void initUI() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Login", createLoginPanel());
        tabs.add("Reciclaje", createRecyclingPanel());
        tabs.add("Historial", createHistoryPanel());
        tabs.add("Incentivos", createRewardsPanel());
        tabs.add("Reportes", createReportsPanel());

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    // ----------------- Panel Login -----------------
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtId = new JTextField();
        txtName = new JTextField();
        txtEmail = new JTextField();
        JButton btnLogin = new JButton("Ingresar");

        btnLogin.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            /*
            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                return;
            }*/
            usuarioController.loginOrRegister("COD00002", "Kimberly Quispe", "kquispe@autonoma.com.pe");
            currentUser = usuarioController.getCurrentUser();
            lblWelcome.setText("Bienvenido: " + currentUser.getName());
            lblPoints.setText("Puntos: " + currentUser.getPoints());
        });

        panel.add(new JLabel("Código:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtName);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel(""));
        panel.add(btnLogin);

        lblWelcome = new JLabel("No autenticado");
        lblPoints = new JLabel("Puntos: 0");
        panel.add(lblWelcome);
        panel.add(lblPoints);

        return panel;
    }

    // ----------------- Panel Reciclaje -----------------
    private JPanel createRecyclingPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JComboBox<MaterialType> cmbMaterial = new JComboBox<>(MaterialType.values());
        JTextField txtQty = new JTextField(10);
        JButton btnAdd = new JButton("Registrar reciclaje");

        btnAdd.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Inicie sesión primero.");
                return;
            }
            try {
                double qty = Double.parseDouble(txtQty.getText());
                
                RecyclingRecord rec = registroReciclajeController.registerRecycling(currentUser,(MaterialType) cmbMaterial.getSelectedItem(), qty);
                JOptionPane.showMessageDialog(this, "Registro guardado: " + rec);
                lblPoints.setText("Puntos: " + currentUser.getPoints());
                refreshHistory();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        });

        panel.add(new JLabel("Material:"));
        panel.add(cmbMaterial);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtQty);
        panel.add(btnAdd);

        return panel;
    }

    // ----------------- Panel Historial -----------------
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        modelHistory = new DefaultTableModel(new String[]{"Fecha", "Material", "Cantidad", "Puntos"}, 0);
        tblHistory = new JTable(modelHistory);
        panel.add(new JScrollPane(tblHistory), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Actualizar");
        btnRefresh.addActionListener(e -> refreshHistory());
        panel.add(btnRefresh, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshHistory() {
        modelHistory.setRowCount(0);
        if (currentUser == null) return;
        List<RecyclingRecord> history = registroReciclajeController.getHistory(currentUser);
        for (RecyclingRecord r : history) {
            modelHistory.addRow(new Object[]{r.getFechaRegistro(), r.getMaterial(), r.getQuantity(), r.getPoints()});
        }
    }

    // ----------------- Panel Incentivos -----------------
    private JPanel createRewardsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblPuntos = new JLabel("Puntos: 0");
        panel.add(lblPuntos,BorderLayout.SOUTH);

        modelRewards = new DefaultTableModel(new String[]{"ID", "Nombre", "Costo"}, 0);
        tblRewards = new JTable(modelRewards);
        panel.add(new JScrollPane(tblRewards), BorderLayout.CENTER);

        JButton btnRedeem = new JButton("Canjear incentivo");
        btnRedeem.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Inicie sesión primero.");
                return;
            }
            int row = tblRewards.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un incentivo.");
                return;
            }
            String rewardId = (String) modelRewards.getValueAt(row, 0);
            boolean ok = premioController.redeem(currentUser,rewardId);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Canje exitoso.");
                lblPuntos.setText("Puntos: " + currentUser.getPoints());
            } else {
                JOptionPane.showMessageDialog(this, "No tiene puntos suficientes.");
            }
        });

        panel.add(btnRedeem, BorderLayout.SOUTH);

        // cargar incentivos
        refreshRewards();

        return panel;
    }

    private void refreshRewards() {
        modelRewards.setRowCount(0);
        List<Reward> rewards = premioController.listRewards();
        for (Reward r : rewards) {
            modelRewards.addRow(new Object[]{r.getId(), r.getName(), r.getCostPoints()});
        }
    }

    // ----------------- Panel Reportes -----------------
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea txtReports = new JTextArea();
        txtReports.setEditable(false);

        JButton btnGenerate = new JButton("Generar Reportes");
        btnGenerate.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Usuarios ===\n");
            for (User u : usuarioController.listUsers()) {
                sb.append(u).append("\n");
            }
            sb.append("\n=== Registros ===\n");
            for (RecyclingRecord r : registroReciclajeController.listAllRecords()) {
                sb.append(r).append("\n");
            }
            txtReports.setText(sb.toString());
        });

        panel.add(new JScrollPane(txtReports), BorderLayout.CENTER);
        panel.add(btnGenerate, BorderLayout.SOUTH);

        return panel;
    }
}