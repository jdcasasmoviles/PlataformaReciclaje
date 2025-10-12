package com.plataforma.reciclaje;
import com.plataforma.reciclaje.controller.PremioController;
import com.plataforma.reciclaje.controller.RegistroReciclajeController;
import com.plataforma.reciclaje.controller.UsuarioController;
import com.plataforma.reciclaje.model.MaterialType;
import com.plataforma.reciclaje.model.RecyclingRecord;
import com.plataforma.reciclaje.model.Reward;
import com.plataforma.reciclaje.model.User;
import java.util.List;
import java.util.Scanner;


public class ConsoleView {
    private final Scanner sc = new Scanner(System.in);
    RegistroReciclajeController registroReciclajeController=new RegistroReciclajeController();
    PremioController premioController = new PremioController();
    UsuarioController usuarioController = new UsuarioController();
    private User currentUser;

    public ConsoleView() {
    }

    public void start() {
        System.out.println("=== Plataforma de Reciclaje Inteligente ===");
        loginFlow();
        mainLoop();
    }

    private void loginFlow() {
        /*System.out.print("Ingrese su codigo universitario: ");
        String id = sc.nextLine().trim();
        System.out.print("Ingrese su nombre: ");
        String name = sc.nextLine().trim();
        System.out.print("Ingrese su email: ");
        String email = sc.nextLine().trim();
        usuarioController.loginOrRegister(id, name, email);
        */
        usuarioController.registerUsuario("COD00002", "Kimberly Quispe", "kquispe@autonoma.com.pe");
        currentUser = usuarioController.getCurrentUser();
        System.out.println("Bienvenido, " + usuarioController.getCurrentUser().getName());
    }

    private void mainLoop() {
        boolean run = true;
        while (run) {
            System.out.println("\n--- Menú ---");
            System.out.println("1) Registrar reciclaje");
            System.out.println("2) Consultar puntos / historial");
            System.out.println("3) Canjear incentivo");
            System.out.println("4) Reportes (admin)");
            System.out.println("5) Salir");
            System.out.print("Opción: ");
            String opt = sc.nextLine();
            switch (opt) {
                case "1": doRegister(); break;
                case "2": doHistory(); break;
                case "3": doRedeem(); break;
                case "4": doReports(); break;
                case "5": run = false; break;
                default: System.out.println("Opción inválida");
            }
        }
        System.out.println("Gracias por participar. ¡Recicla!");
    }

    private void doRegister() {
        System.out.println("Tipos de material:");
        for (MaterialType mt : MaterialType.values()) {
            System.out.println("- " + mt.name());
        }
        System.out.print("Tipo: ");
        String t = sc.nextLine().trim().toUpperCase();
        MaterialType mt;
        try {
            mt = MaterialType.valueOf(t);
        } catch (Exception e) { System.out.println("Tipo inválido"); return; }
        System.out.print("Cantidad (unidades o kg): ");
        double qty;
        try { qty = Double.parseDouble(sc.nextLine()); } catch (Exception e) { System.out.println("Cantidad inválida"); return; }
        RecyclingRecord rec = registroReciclajeController.registerRecycling(currentUser,mt, qty);
        System.out.println("Registro guardado: " + rec);
        System.out.println("Puntos actuales: " + usuarioController.getCurrentUser().getPoints());
    }

    private void doHistory() {
        User u = usuarioController.getCurrentUser();
        System.out.println("Usuario: " + u);
        List<RecyclingRecord> h = registroReciclajeController.getHistory(currentUser);
        if (h.isEmpty()) System.out.println("No hay registros aún.");
        else h.forEach(System.out::println);
    }

    private void doRedeem() {
        List<Reward> rewards = premioController.listRewards();
        System.out.println("Incentivos disponibles:");
        for (Reward r : rewards) System.out.println(r.getId() + " - " + r);
        System.out.print("Ingrese ID incentivo para canjear: ");
        String rid = sc.nextLine().trim();
        boolean ok = premioController.redeem(currentUser,rid);
        if (ok) System.out.println("Canje exitoso. Puntos restantes: " + usuarioController.getCurrentUser().getPoints());
        else System.out.println("No se pudo canjear (puntos insuficientes o ID invalido).");
    }

    private void doReports() {
        System.out.println("=== Reportes ===");
        System.out.println("Usuarios:");
        for (User u : usuarioController.listUsers()) System.out.println(" - " + u);
        System.out.println("Registros totales:");
        for (RecyclingRecord r : registroReciclajeController.listAllRecords()) System.out.println(" - " + r);
    }
}