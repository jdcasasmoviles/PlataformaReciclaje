package com.plataforma.reciclaje.view.login;
import com.plataforma.reciclaje.controller.UsuarioController;
import com.plataforma.reciclaje.view.menu.Formulario;
import com.plataforma.reciclaje.view.registrar.Registrar;
import javax.swing.*;
import java.awt.*;

public class LoginUI extends javax.swing.JFrame {
    UsuarioController usuarioController = new UsuarioController();
    public LoginUI() {
        initComponents();
        setLocationRelativeTo(null);
        // Fade In al iniciar
        new Thread(() -> {
            try {
                for (float i = 0; i <= 1.0; i += 0.05) {
                    this.setOpacity(i);
                    Thread.sleep(40);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        mainPanel = new JPanel();
        cardPanel = new JPanel();
        topPanel = new GradientPanel();
        lblTitulo = new JLabel();
        lblUser = new JLabel();
        lblPass = new JLabel();
        txtUser = new JTextField();
        txtPass = new JPasswordField();
        btnLogin = new ModernButton();
        btnExit = new ModernButton();
        btnRegistrate = new ModernButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setOpacity(0.0F);

        mainPanel.setBackground(new java.awt.Color(240, 240, 240));
        mainPanel.setLayout(new GridBagLayout());

        cardPanel.setBackground(new Color(173, 216, 230, 128));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardPanel.setLayout(new BorderLayout(0, 20));

        // Encabezado
        topPanel.setPreferredSize(new java.awt.Dimension(400, 80));
        lblTitulo.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setText("Iniciar Sesión");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(lblTitulo, BorderLayout.CENTER);
        cardPanel.add(topPanel, BorderLayout.NORTH);

        // Cuerpo
        JPanel bodyPanel = new JPanel(new GridLayout(6, 1, 5, 10));
        bodyPanel.setOpaque(false);

        lblUser.setText("Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bodyPanel.add(lblUser);

        txtUser.setFont(new Font("Segoe UI", 0, 14));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtUser.setBackground(new Color(255, 255, 255));
        txtUser.setToolTipText("Ingrese su usuario");
        bodyPanel.add(txtUser);

        lblPass.setText("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bodyPanel.add(lblPass);

        txtPass.setFont(new Font("Segoe UI", 0, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPass.setBackground(new Color(255, 255, 255)); 
        txtPass.setToolTipText("Ingrese su contraseña");
        bodyPanel.add(txtPass);

        cardPanel.add(bodyPanel, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        btnLogin.setText("Ingresar");
        btnLogin.setBackground(new Color(40, 167, 69)); // Verde
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(evt -> btnLoginActionPerformed(evt));
        buttonPanel.add(btnLogin);

        btnExit.setText("Salir");
        btnExit.setBackground(new Color(220, 53, 69)); // Rojo
        btnExit.setForeground(Color.WHITE);
        btnExit.addActionListener(evt -> System.exit(0));
        buttonPanel.add(btnExit);

        btnRegistrate.setText("Registrate");
        btnRegistrate.setBackground(new Color(40, 167, 69)); // Verde
        btnRegistrate.setForeground(Color.WHITE);
        btnRegistrate.addActionListener(evt -> btnRegistrateActionPerformed(evt));
        buttonPanel.add(btnRegistrate);

        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(cardPanel, new GridBagConstraints());
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        pack();
    }

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String user = txtUser.getText();
        String password = new String(txtPass.getPassword());
        if (user.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.");
                return;
        }
        if (usuarioController.loginUsuario(user,password)) {
          dispose();
          Formulario formulario=new Formulario(usuarioController.getCurrentUser());
         formulario.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Usuario o contraseña incorrectos");
        }
    }

    private void btnRegistrateActionPerformed(java.awt.event.ActionEvent evt) {
       Registrar registrar=new Registrar(usuarioController);
        registrar.setVisible(true);
    }

    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new LoginUI().setVisible(true));
    }

    // Variables
    private JPanel mainPanel;
    private JPanel cardPanel;
    private GradientPanel topPanel;
    private JLabel lblTitulo, lblUser, lblPass;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private ModernButton btnLogin, btnExit,btnRegistrate;
}