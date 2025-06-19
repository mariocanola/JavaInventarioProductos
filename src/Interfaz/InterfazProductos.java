package Interfaz;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;   
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JScrollPane;
import Interfaz.PanelCardProducto;


public class InterfazProductos extends JFrame {

    private JPanel panelGrid;

    public InterfazProductos() {
        setTitle("Inventario");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ---------- Panel superior (filtros + botón) ---------- */
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(Color.DARK_GRAY);

        // Filtros desplegables (checklist)
        String[] opcionesSexo = {"Hombre", "Mujer", "Unisex"};
        String[] opcionesMarca = {"Marca A", "Marca B", "Marca C"};
        String[] opcionesCategoria = {"Zapatos", "Camisas", "Pantalones"};

        panelTop.add(crearFiltroChecklist("Sexo", opcionesSexo));
        panelTop.add(crearFiltroChecklist("Marca", opcionesMarca));
        panelTop.add(crearFiltroChecklist("Categoría", opcionesCategoria));

        JButton btnAgregar = new JButton("agregar producto");
        panelTop.add(btnAgregar);
        // Espacio horizontal entre componentes
        panelTop.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(panelTop, BorderLayout.NORTH);

        /* ---------- Panel central (grid de productos) ---------- */
        panelGrid = new JPanel(new GridLayout(0, 4, 15, 15));  // 4 columnas, filas automáticas
        panelGrid.setBackground(Color.BLACK);

        for (int i = 0; i < 50; i++) {
            panelGrid.add(new PanelCardProducto(
                    new ImageIcon("ruta/a/placeholder.png"),
                    "Producto " + i, 
                    "$" + (10*i), 
                    "Disponible").getPanel());
        }

        JScrollPane scroll = new JScrollPane(panelGrid,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(Color.BLACK);
        add(scroll, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* ---------- Utilidades ---------- */
    private JButton crearFiltroChecklist(String titulo, String[] opciones) {
        JButton btn = new JButton(titulo + " \u25BE"); // flecha hacia abajo
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);

        // Popup con checklist
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        for (String op : opciones) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(op);
            item.setOpaque(true);
            item.setBackground(new Color(60, 60, 60));
            item.setForeground(Color.WHITE);
            menu.add(item);
        }

        btn.addActionListener(e -> menu.show(btn, 0, btn.getHeight()));
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(InterfazProductos::new);
    }
}
