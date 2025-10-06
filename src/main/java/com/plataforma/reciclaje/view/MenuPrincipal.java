package com.plataforma.reciclaje.view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import com.plataforma.reciclaje.view.HamburgerToggle;

public class MenuPrincipal extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<TableModel> sorter;
    private JComboBox<String> yearCombo;
    private int hoveredRow = -1; // hover row index
    private Map<Integer, Float> hoverAlphas = new HashMap<>(); // row -> alpha (0..1)
    private Map<Integer, Float> flashAlphas = new HashMap<>(); // for add/delete flash effect
    private SideMenuPanel sideMenu;
    private FadePanel mainFade;
    private UnderlineLabel sectionTitle;
    private boolean darkMode = false;

    public MenuPrincipal() {
        super("Global School - Administrador (Animado)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Look & Feel moderno (Nimbus si está disponible)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // ---------- Barra superior con gradiente y controles ----------
        GradientPanel topBar = new GradientPanel(new Color(2, 119, 255), new Color(0, 95, 225));
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setLayout(new BorderLayout());

        // left: hamburger toggle and title area
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        leftTop.setOpaque(false);
        HamburgerToggle ham = new HamburgerToggle();
        ham.addActionListener(e -> sideMenu.toggle());
        leftTop.add(ham);

        JLabel userLabel = new JLabel("👩‍🎓 Edith Weed Davis");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        leftTop.add(userLabel);
        topBar.add(leftTop, BorderLayout.WEST);

        // center: animated header
        sectionTitle = new UnderlineLabel("Administrar el año académico");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(Color.WHITE);
        sectionTitle.setOpaque(false);
        // animate title slide+fade on start
        sectionTitle.slideIn();

        JPanel centerTop = new JPanel(new GridBagLayout());
        centerTop.setOpaque(false);
        centerTop.add(sectionTitle);
        topBar.add(centerTop, BorderLayout.CENTER);

        // right: filters + dark mode + close
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        topRight.setOpaque(false);
        yearCombo = new JComboBox<>(new String[]{
                "January 2020 - December 2020",
                "January 2021 - December 2021",
                "January 2022 - December 2022",
                "January 2023 - December 2023"
        });
        yearCombo.setPreferredSize(new Dimension(260, 36));
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        RippleButton updateBtn = new RippleButton("🔎 Actualizar", new Color(0, 150, 136));
        updateBtn.setPreferredSize(new Dimension(130, 36));
        updateBtn.setToolTipText("Aplica filtro por año");
        updateBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Filtrando por: " + yearCombo.getSelectedItem(), "Actualizar", JOptionPane.INFORMATION_MESSAGE));

        RippleButton darkBtn = new RippleButton("🌙", new Color(90,90,90));
        darkBtn.setToolTipText("Toggle Dark Mode");
        darkBtn.addActionListener(e -> toggleDarkMode());

        RippleButton closeBtn = new RippleButton("✕", new Color(220, 53, 69));
        closeBtn.addActionListener(e -> System.exit(0));

        topRight.add(yearCombo);
        topRight.add(updateBtn);
        topRight.add(darkBtn);
        topRight.add(closeBtn);
        topBar.add(topRight, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ---------- Menú lateral con slide-in y botones ----------
        sideMenu = new SideMenuPanel();
        add(sideMenu, BorderLayout.WEST);
        sideMenu.slideInInitial();

        // ---------- Panel principal (FadePanel para animación) ----------
        mainFade = new FadePanel();
        mainFade.setLayout(new BorderLayout());
        mainFade.setBackground(Color.WHITE);
        mainFade.setBorder(new EmptyBorder(12,12,12,12));
        mainFade.setAlpha(0f); // inicia transparente
        add(mainFade, BorderLayout.CENTER);

        // fade-in content
        javax.swing.Timer fadeTimer = new javax.swing.Timer(30, null);
        fadeTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float a = mainFade.getAlpha() + 0.06f;
                if (a >= 1f) {
                    a = 1f;
                    fadeTimer.stop();
                }
                mainFade.setAlpha(a);
                mainFade.repaint();
            }
        });
        fadeTimer.start();

        // Header del panel: quick links (with animated underline)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // sectionTitle panel on top of main too, but we already used topBar. Keep quick links here
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        linksPanel.setOpaque(false);
        String[] links = {
                "Año académico", "Rol de usuario", "Permiso de función",
                "Administrar usuario", "Restablecer la contraseña",
                "Credencial de usuario", "Restablecer correo"
        };
        for (String l : links) {
            QuickLinkLabel lbl = new QuickLinkLabel(l);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(0, 120, 255));
            lbl.setToolTipText("Ir a: " + l);
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(MenuPrincipal.this,
                            "Ir a: " + l, "Enlace rápido", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            linksPanel.add(lbl);
        }
        header.add(linksPanel, BorderLayout.CENTER);
        mainFade.add(header, BorderLayout.NORTH);

        // Panel de herramientas (exportar, buscar, show rows) con shadow card
        ShadowPanel toolsPanel = new ShadowPanel();
        toolsPanel.setLayout(new BorderLayout());
        toolsPanel.setOpaque(false);
        toolsPanel.setPreferredSize(new Dimension(0, 62));
        toolsPanel.setBackground(Color.WHITE);

        JPanel leftTools = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        leftTools.setOpaque(false);
        RippleButton copyBtn = new RippleButton("📋 Copy", new Color(66,133,244));
        RippleButton csvBtn = new RippleButton("🗂 CSV", new Color(23,162,184));
        RippleButton excelBtn = new RippleButton("📊 Excel", new Color(56,142,60));
        RippleButton pdfBtn = new RippleButton("📄 PDF", new Color(211,47,47));
        copyBtn.setToolTipText("Copiar tabla al portapapeles");
        csvBtn.setToolTipText("Exportar tabla a CSV");
        excelBtn.setToolTipText("Exportar a Excel (librería externa)");
        pdfBtn.setToolTipText("Exportar a PDF (librería externa)");
        copyBtn.addActionListener(e -> copyTableToClipboard());
        csvBtn.addActionListener(e -> exportTableCSV());
        excelBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Exportar Excel requiere librería externa."));
        pdfBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Exportar PDF requiere librería externa."));
        leftTools.add(copyBtn);
        leftTools.add(csvBtn);
        leftTools.add(excelBtn);
        leftTools.add(pdfBtn);
        toolsPanel.add(leftTools, BorderLayout.WEST);

        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        rightTools.setOpaque(false);
        JTextField searchField = new JTextField(22);
        searchField.setPreferredSize(new Dimension(220, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setToolTipText("Buscar en tabla (Año académico / Nota)");
        rightTools.add(new JLabel("Search:"));
        rightTools.add(searchField);
        toolsPanel.add(rightTools, BorderLayout.EAST);

        mainFade.add(toolsPanel, BorderLayout.CENTER);

        // ---------- Tabla con botones de acción ----------
        String[] columns = {"#", "Año académico", "¿Está corriendo?", "Nota", "Acción"};
        Object[][] data = {
                {1, "January 2020 - December 2020", "No", "Lorem Ipsum is simply dummy"},
                {2, "January 2021 - December 2021", "No", "Lorem Ipsum is simply dummy text"},
                {3, "January 2022 - December 2022", "No", "Lorem Ipsum has been"},
                {4, "January 2023 - December 2023", "Sí", "Año Académico 2023. Prueba"}
        };
        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna de acciones es editable
                return column == 4;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                // hover alpha animation
                float ha = hoverAlphas.getOrDefault(row, 0f);
                if (!isRowSelected(row)) {
                    if (ha > 0f) {
                        Color base = new Color(220, 240, 255);
                        comp.setBackground(interpolateColor(row%2==0? new Color(249,251,255):Color.WHITE, base, ha));
                    } else {
                        comp.setBackground(row % 2 == 0 ? new Color(249,251,255) : Color.WHITE);
                    }
                } else {
                    comp.setBackground(new Color(200,225,255));
                }
                // flash highlight (add/delete)
                float fa = flashAlphas.getOrDefault(row, 0f);
                if (fa > 0f && !isRowSelected(row)) {
                    Color flash = new Color(255, 249, 196, Math.round(255*fa));
                    comp.setBackground(blend(comp.getBackground(), flash));
                }
                if (column == 4) comp.setBackground(new Color(0,0,0,0));
                return comp;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        table.setRowHeight(48);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230,230,230));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245,245,245));
        table.getTableHeader().setReorderingAllowed(false);
        // rounded cell renderer can be achieved by overriding prepareRenderer (done)

        // hover animation management
        javax.swing.Timer hoverTimer = new javax.swing.Timer(30, evt -> {
            boolean repaintNeeded = false;
            for (Map.Entry<Integer, Float> e : new ArrayList<>(hoverAlphas.entrySet())) {
                int r = e.getKey();
                float v = e.getValue();
                if (r == hoveredRow) {
                    v = Math.min(1f, v + 0.12f);
                } else {
                    v = Math.max(0f, v - 0.12f);
                }
                if (Math.abs(v - e.getValue()) > 0.001) {
                    hoverAlphas.put(r, v);
                    repaintNeeded = true;
                } else {
                    if (v <= 0f) hoverAlphas.remove(r);
                }
            }
            if (hoveredRow != -1 && !hoverAlphas.containsKey(hoveredRow)) {
                hoverAlphas.put(hoveredRow, 0f);
                repaintNeeded = true;
            }
            // flashAlphas fade out
            for (Map.Entry<Integer, Float> e : new ArrayList<>(flashAlphas.entrySet())) {
                float v = e.getValue();
                v = Math.max(0f, v - 0.06f);
                if (v <= 0f) flashAlphas.remove(e.getKey());
                else flashAlphas.put(e.getKey(), v);
                repaintNeeded = true;
            }
            if (repaintNeeded) table.repaint();
        });
        hoverTimer.start();

        // mouse motion to update hoveredRow
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r != hoveredRow) {
                    hoveredRow = r;
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
            }
        });

        // Column action
        TableColumn actionColumn = table.getColumnModel().getColumn(4);
        actionColumn.setCellRenderer(new ActionCellRenderer());
        actionColumn.setCellEditor(new ActionCellEditor());

        // sorter + búsqueda
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    String expr = "(?i).*" + Pattern.quote(text) + ".*";
                    sorter.setRowFilter(RowFilter.regexFilter(expr, 1, 3));
                }
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        // add/delete animations: apply flash to row index after operation
        // we'll provide helper methods addAnimatedRow/removeAnimatedRow

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        scroll.getViewport().setBackground(new Color(250,250,250));
        mainFade.add(scroll, BorderLayout.SOUTH);

        // Keyboard shortcuts
        setupKeyBindings(searchField);

    }

    // ---------------- Helper: keyboard shortcuts ----------------
    private void setupKeyBindings(JTextField searchField) {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        // Ctrl+F -> focus search
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "focusSearch");
        am.put("focusSearch", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                searchField.requestFocusInWindow();
                searchField.selectAll();
            }
        });
        // Ctrl+E -> edit selected
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "editRow");
        am.put("editRow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r != -1) {
                    int modelRow = table.convertRowIndexToModel(r);
                    new AnimatedDialog(MenuPrincipal.this, "Editar registro", modelRow).showDialog();
                }
            }
        });
        // Delete -> delete selected
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
        am.put("deleteRow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r != -1) {
                    int modelRow = table.convertRowIndexToModel(r);
                    removeAnimatedRow(modelRow);
                }
            }
        });
        // Ctrl+S -> export CSV
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "exportCSV");
        am.put("exportCSV", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                exportTableCSV();
            }
        });
    }

    // ---------------- Add and remove row with animation ----------------
    private void addAnimatedRow(Object[] rowData) {
        model.addRow(rowData);
        int modelRow = model.getRowCount()-1;
        flashAlphas.put(modelRow, 1f); // start flash
        table.repaint();
    }

    private void removeAnimatedRow(int modelRow) {
        // animate flash fade-out then remove
        flashAlphas.put(modelRow, 1f);
        javax.swing.Timer t = new javax.swing.Timer(60, null);
        final int steps[] = {6};
        t.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                float v = flashAlphas.getOrDefault(modelRow, 0f);
                v = v - 0.16f;
                if (v <= 0f) {
                    t.stop();
                    // remove row
                    if (modelRow >= 0 && modelRow < model.getRowCount()) model.removeRow(modelRow);
                    // shrink flash map keys: reindex flashAlphas if needed (simple: clear)
                    flashAlphas.clear();
                } else {
                    flashAlphas.put(modelRow, v);
                }
                table.repaint();
            }
        });
        t.start();
    }

    // ---------------- Clipboard / CSV export ----------------
    private void copyTableToClipboard() {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < table.getColumnCount(); c++) {
            sb.append(table.getColumnName(c)).append("\t");
        }
        sb.append("\n");
        for (int r = 0; r < table.getRowCount(); r++) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                Object val = table.getValueAt(r, c);
                sb.append(val == null ? "" : val.toString()).append("\t");
            }
            sb.append("\n");
        }
        StringSelection sel = new StringSelection(sb.toString());
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(sel, null);
        JOptionPane.showMessageDialog(this, "Tabla copiada al portapapeles.");
    }

    private void exportTableCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar CSV");
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                for (int c = 0; c < table.getColumnCount(); c++) {
                    w.write("\"" + table.getColumnName(c) + "\"");
                    if (c < table.getColumnCount() - 1) w.write(",");
                }
                w.write("\n");
                for (int r = 0; r < table.getRowCount(); r++) {
                    for (int c = 0; c < table.getColumnCount(); c++) {
                        Object val = table.getValueAt(r, c);
                        w.write("\"" + (val == null ? "" : val.toString().replace("\"", "\"\"")) + "\"");
                        if (c < table.getColumnCount() - 1) w.write(",");
                    }
                    w.write("\n");
                }
                JOptionPane.showMessageDialog(this, "CSV guardado: " + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------------- Renderers / Editors ----------------
    class ActionCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
            p.setOpaque(false);

            RippleButton edit = new RippleButton("✏️ Editar", new Color(66, 133, 244));
            edit.setPreferredSize(new Dimension(84, 34));

            RippleButton del = new RippleButton("🗑️ Borrar", new Color(220, 53, 69));
            del.setPreferredSize(new Dimension(78, 34));

            int modelRow = table.convertRowIndexToModel(row);
            Object runningVal = model.getValueAt(modelRow, 2);
            boolean running = runningVal != null && runningVal.toString().equalsIgnoreCase("Sí");

            RippleButton act;
            if (running) {
                act = new RippleButton("✔️ Activo", new Color(0, 123, 255));
            } else {
                act = new RippleButton("▶️ Activar", new Color(23, 162, 184));
            }
            act.setPreferredSize(new Dimension(92, 34));

            p.add(edit);
            p.add(del);
            p.add(act);
            return p;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        private final RippleButton edit = new RippleButton("✏️ Editar", new Color(66, 133, 244));
        private final RippleButton del = new RippleButton("🗑️ Borrar", new Color(220, 53, 69));
        private final RippleButton act = new RippleButton("▶️ Activar", new Color(23, 162, 184));
        private int editingRow = -1;

        public ActionCellEditor() {
            panel.setOpaque(false);
            edit.setPreferredSize(new Dimension(84, 34));
            del.setPreferredSize(new Dimension(78, 34));
            act.setPreferredSize(new Dimension(92, 34));

            edit.addActionListener(e -> {
                System.out.println("1");
                 if (editingRow >= 0) openEditDialog(editingRow);
                fireEditingStopped();
                 System.out.println("4");
            });
            del.addActionListener(e -> {
                if (editingRow >= 0) confirmAndDelete(editingRow);
                fireEditingStopped();
            });
            act.addActionListener(e -> {
                    if (editingRow >= 0) toggleActivate(editingRow);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();
            editingRow = table.convertRowIndexToModel(row);
            Object runningVal = model.getValueAt(editingRow, 2);
            boolean running = runningVal != null && runningVal.toString().equalsIgnoreCase("Sí");
            if (running) {
                act.setText("✔️ Activo");
                act.setBackground(new Color(0, 123, 255));
            } else {
                act.setText("▶️ Activar");
                act.setBackground(new Color(23, 162, 184));
            }
            panel.add(edit);
            panel.add(del);
            panel.add(act);
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return null; }

        private void toggleActivate(int modelRow) {
            Object runningVal = model.getValueAt(modelRow, 2);
            boolean running = runningVal != null && runningVal.toString().equalsIgnoreCase("Sí");
            if (running) {
                model.setValueAt("No", modelRow, 2);
            } else {
                model.setValueAt("Sí", modelRow, 2);
            }
            flashAlphas.put(modelRow, 1f);
            table.repaint();
        }

        
        private void openEditDialog(int modelRow) {
            JTextField yearField = new JTextField((String) model.getValueAt(modelRow, 1));
            JTextArea noteArea = new JTextArea((String) model.getValueAt(modelRow, 3));
            noteArea.setRows(4);
            JScrollPane scroll = new JScrollPane(noteArea);

            JPanel p = new JPanel(new BorderLayout(6,6));
            p.add(new JLabel("Año académico:"), BorderLayout.NORTH);
            p.add(yearField, BorderLayout.CENTER);
            p.add(new JLabel("Nota (opcional):"), BorderLayout.SOUTH);

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.add(scroll, BorderLayout.CENTER);

            JPanel dialogPanel = new JPanel(new BorderLayout(8,8));
            dialogPanel.add(p, BorderLayout.NORTH);
            dialogPanel.add(bottom, BorderLayout.CENTER);

            int res = JOptionPane.showConfirmDialog(MenuPrincipal.this, dialogPanel,
                    "Editar registro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                model.setValueAt(yearField.getText(), modelRow, 1);
                model.setValueAt(noteArea.getText(), modelRow, 3);
                JOptionPane.showMessageDialog(MenuPrincipal.this, "Registro actualizado.");
            }
        }

        private void confirmAndDelete(int modelRow) {
               int resp = JOptionPane.showConfirmDialog(MenuPrincipal.this,
                    "¿Seguro que desea eliminar el registro seleccionado?",
                    "Confirmar borrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                model.removeRow(modelRow);
            }
        }
    }

    // ---------------- Utility UI classes ----------------

    // Gradient panel
    static class GradientPanel extends JPanel {
        private final Color start, end;
        GradientPanel(Color start, Color end) { this.start = start; this.end = end; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0,0,start, w, h, end);
            g2.setPaint(gp);
            g2.fillRect(0,0,w,h);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Fade panel (alpha composite)
    static class FadePanel extends JPanel {
        private float alpha = 1f;
        public FadePanel() { super(); setOpaque(false); }
        public void setAlpha(float a) { alpha = Math.max(0f, Math.min(1f, a)); }
        public float getAlpha() { return alpha; }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // Shadow panel to simulate elevation
    static class ShadowPanel extends JPanel {
        public ShadowPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            int arc = 12;
            int shadowSize = 8;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // shadow
            g2.setColor(new Color(0,0,0,24));
            g2.fillRoundRect(shadowSize, shadowSize, getWidth()-shadowSize*2, getHeight()-shadowSize*2, arc, arc);
            // panel background
            g2.setColor(getBackground() == null ? Color.WHITE : getBackground());
            g2.fillRoundRect(0, 0, getWidth()-shadowSize, getHeight()-shadowSize, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Rounded ripple button
    static class RippleButton extends JButton {
        private final java.util.List<Ripple> ripples = new ArrayList<>();
        private final Color baseColor;
        private javax.swing.Timer rippleTimer;

        RippleButton(String text, Color bg) {
            super(text);
            this.baseColor = bg;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(8,12,8,12));

            // hover smooth via timer (interpolate)
            final Color hoverTarget = bg.darker();
            final float[] progress = {0f};
            javax.swing.Timer hoverFade = new javax.swing.Timer(20, e -> {
                progress[0] = Math.max(0f, Math.min(1f, progress[0]));
                repaint();
            });
            hoverFade.start();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    progress[0] = 1f; // simple instantaneous; could animate more smoothly
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    progress[0] = 0f;
                    repaint();
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    // create ripple
                    ripples.add(new Ripple(e.getX(), e.getY()));
                    if (rippleTimer == null) {
                        rippleTimer = new javax.swing.Timer(16, ev -> {
                            Iterator<Ripple> it = ripples.iterator();
                            boolean repaintNeeded = false;
                            while (it.hasNext()) {
                                Ripple r = it.next();
                                r.update();
                                if (r.finished()) it.remove();
                                repaintNeeded = true;
                            }
                            if (repaintNeeded) repaint();
                            if (ripples.isEmpty()) {
                                rippleTimer.stop();
                                rippleTimer = null;
                            }
                        });
                        rippleTimer.start();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // background rounded
            g2.setColor(baseColor);
            g2.fillRoundRect(0, 0, w, h, 14, 14);
            // text
            FontMetrics fm = g2.getFontMetrics(getFont());
            String txt = getText();
            int tx = (w - fm.stringWidth(txt)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(txt, tx, ty);

            // draw ripples
            for (Ripple r : ripples) {
                r.paint(g2);
            }
            g2.dispose();
        }

        static class Ripple {
            int x, y;
            float radius = 0f;
            float alpha = 0.6f;
            Ripple(int x, int y) { this.x = x; this.y = y; }
            void update() { radius += 8f; alpha -= 0.04f; }
            boolean finished() { return alpha <= 0f; }
            void paint(Graphics2D g2) {
                g2.setColor(new Color(255,255,255, Math.round(255*alpha)));
                g2.fillOval(Math.round(x - radius), Math.round(y - radius), Math.round(radius*2), Math.round(radius*2));
            }
        }
    }

    // Rounded button used previously (kept for compatibility)
    static class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setForeground(Color.WHITE);
            setOpaque(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(6,12,6,12));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { setOpaque(true); }
                @Override
                public void mouseExited(MouseEvent e) { setOpaque(false); }
            });
            setBackground(new Color(66,133,244));
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, 14, 14);
            FontMetrics fm = g2.getFontMetrics(getFont());
            String txt = getText();
            int tx = (w - fm.stringWidth(txt)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(txt, tx, ty);
            g2.dispose();
        }
    }

    // Side menu with slide in/out and hamburger control
    class SideMenuPanel extends JPanel {
        private JPanel content;
        private int visibleWidth = 220;
        private int currentX = -220;
        private javax.swing.Timer slideTimer;
        private boolean open = true;

        SideMenuPanel() {
            setLayout(null);
            setPreferredSize(new Dimension(visibleWidth, 0));
            content = new GradientPanel(new Color(21,63,113), new Color(39,88,147));
            content.setLayout(null);
            content.setBounds(currentX, 0, visibleWidth, getHeight());
            add(content);

            // build items inside content
            JPanel items = new JPanel();
            items.setOpaque(false);
            items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
            items.setBounds(10, 10, visibleWidth - 20, 600);
            items.setBorder(new EmptyBorder(8,8,8,8));

            String[] menuItems = {
                    "Tablero", "Ajuste", "Tema", "Idioma",
                    "Administrador", "Año académico", "Rol del usuario (ACL)",
                    "Permiso de función (ACL)", "Administrar usuario",
                    "Restablecer contraseña", "Credencial de usuario",
                    "Restablecer correo", "Registro de actividades",
                    "Realimentación", "Apoyo"
            };

            for (String t : menuItems) {
                JButton b = new JButton(t);
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
            }
            content.add(items);

            // layout listener to reposition content width/height
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    content.setBounds(currentX, 0, visibleWidth, getHeight());
                    items.setBounds(10, 10, visibleWidth - 20, getHeight()-20);
                }
            });
        }

        // initial slide-in from left on start
        void slideInInitial() {
            currentX = -visibleWidth;
            if (slideTimer != null && slideTimer.isRunning()) slideTimer.stop();
            slideTimer = new javax.swing.Timer(12, null);
            slideTimer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentX += 12;
                    if (currentX >= 0) {
                        currentX = 0;
                        slideTimer.stop();
                        open = true;
                    }
                    content.setBounds(currentX, 0, visibleWidth, getHeight());
                    repaint();
                }
            });
            slideTimer.start();
        }

        void toggle() {
            if (slideTimer != null && slideTimer.isRunning()) return;
            final int dir = open ? -1 : 1; // close -> move negative, open -> positive
            slideTimer = new javax.swing.Timer(12, null);
            slideTimer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentX += dir * 18;
                    if (currentX <= -visibleWidth) {
                        currentX = -visibleWidth;
                        slideTimer.stop();
                        open = false;
                    } else if (currentX >= 0) {
                        currentX = 0;
                        slideTimer.stop();
                        open = true;
                    }
                    content.setBounds(currentX, 0, visibleWidth, getHeight());
                    repaint();
                }
            });
            slideTimer.start();
        }
    }

    // QuickLink label with underline animation
    static class QuickLinkLabel extends JLabel {
        private float progress = 0f;
        private javax.swing.Timer anim;
        QuickLinkLabel(String text) {
            super("<html><u>" + text + "</u></html>");
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    start(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    start(false);
                }
            });
        }
        private void start(boolean forward) {
            if (anim != null && anim.isRunning()) anim.stop();
            anim = new javax.swing.Timer(15, null);
            anim.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (forward) progress = Math.min(1f, progress + 0.08f);
                    else progress = Math.max(0f, progress - 0.08f);
                    repaint();
                    if (progress == 0f || progress == 1f) anim.stop();
                }
            });
            anim.start();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // draw underline proportional to progress
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0,120,255));
            int w = getWidth();
            int h = getHeight();
            int y = h - 6;
            int lineW = (int)(w * progress);
            g2.fillRoundRect((w-lineW)/2, y, lineW, 3, 6, 6);
            g2.dispose();
        }
    }

    // UnderlineLabel with slideIn animation (for header)
    static class UnderlineLabel extends JLabel {
        private float alpha = 0f;
        private int offsetX = -40;
        UnderlineLabel(String text) {
            super(text);
            setOpaque(false);
        }
        void slideIn() {
            javax.swing.Timer t = new javax.swing.Timer(20, null);
            t.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    alpha = Math.min(1f, alpha + 0.06f);
                    offsetX = Math.min(0, offsetX + 4);
                    repaint();
                    if (alpha >= 1f && offsetX >= 0) ((javax.swing.Timer)e.getSource()).stop();
                }
            });
            t.start();
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.translate(offsetX, 0);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // Animated dialog (scale + fade)
    static class AnimatedDialog extends JDialog {
        private float alpha = 0f;
        private float scale = 0.8f;
        AnimatedDialog(Window owner, String title, int modelRow) {
            super(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
            setUndecorated(true);
            JPanel p = new JPanel(new BorderLayout(8,8));
            p.setBorder(new EmptyBorder(12,12,12,12));
            p.setBackground(Color.WHITE);
            p.add(new JLabel("Editar fila ID: " + modelRow), BorderLayout.NORTH);
            JTextField tf = new JTextField();
            p.add(tf, BorderLayout.CENTER);
            setContentPane(p);
            pack();
            setLocationRelativeTo(owner);
        }
        void showDialog() {
            setOpacity(0f);
            setVisible(true);
            // animate scale+fade (simple)
            javax.swing.Timer t = new javax.swing.Timer(20, null);
            t.addActionListener(new ActionListener() {
                int steps = 10;
                public void actionPerformed(ActionEvent e) {
                    alpha = Math.min(1f, alpha + 0.12f);
                    scale = Math.min(1f, scale + 0.02f);
                    setOpacity(alpha);
                    setSize((int)(getWidth()*scale), (int)(getHeight()*scale));
                    if (alpha >= 1f) ((javax.swing.Timer)e.getSource()).stop();
                }
            });
            t.start();
        }
    }

    // ---------------- Small color helpers ----------------
    private static Color interpolateColor(Color c1, Color c2, float t) {
        t = Math.max(0f, Math.min(1f, t));
        int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * t);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t);
        int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t);
        return new Color(r,g,b);
    }
    private static Color blend(Color base, Color overlay) {
        int r = (base.getRed() + overlay.getRed())/2;
        int g = (base.getGreen() + overlay.getGreen())/2;
        int b = (base.getBlue() + overlay.getBlue())/2;
        return new Color(r,g,b);
    }

    // ---------------- Toggle dark mode ----------------
    private void toggleDarkMode() {
        darkMode = !darkMode;
        Color bg = darkMode ? new Color(34,34,34) : Color.WHITE;
        Color fg = darkMode ? Color.WHITE : Color.BLACK;
        mainFade.setBackground(bg);
        table.setBackground(darkMode? new Color(48,48,48): Color.WHITE);
        table.setForeground(fg);
        table.getTableHeader().setBackground(darkMode? new Color(30,30,30): new Color(245,245,245));
        table.getTableHeader().setForeground(darkMode? Color.WHITE: Color.BLACK);
        repaint();
    }

    // ---------- main ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal  f = new MenuPrincipal();
            f.setVisible(true);
        });
    }
}