package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;   
import javax.swing.JFrame;

import Model.Parametro;
import controller.ControllerProducto;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JCheckBoxMenuItem;

public class InterfazProductos extends JFrame {

    private JPanel panelGrid;
    private JPanel panelTop;

    private ArrayList<Parametro> comboMarca;
    private ArrayList<Parametro> comboSexo;
    private ArrayList<Parametro> comboCategoria;
    private PanelAgregarProducto panelAgregarProducto;

    public InterfazProductos() {
        setTitle("Inventario");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar las listas   
        comboMarca = new ArrayList<>();
        comboSexo = new ArrayList<>();
        comboCategoria = new ArrayList<>();
        panelAgregarProducto = new PanelAgregarProducto();
        
        /* ---------- Panel superior (filtros + botón) ---------- */
        panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(Color.DARK_GRAY);

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

    public void cargarParametros(List<Parametro> marcas, List<Parametro> sexos, List<Parametro> categorias) {

        this.comboMarca.clear();
        this.comboMarca.addAll(marcas);
        this.comboSexo.clear();
        this.comboSexo.addAll(sexos);
        this.comboCategoria.clear();
        this.comboCategoria.addAll(categorias);

        panelTop.removeAll();

        String[] marcasArr = this.comboMarca.stream().map(Parametro::darNombre).toArray(String[]::new);
        String[] sexosArr = this.comboSexo.stream().map(Parametro::darNombre).toArray(String[]::new);
        String[] categoriasArr = this.comboCategoria.stream().map(Parametro::darNombre).toArray(String[]::new);

        panelTop.add(crearFiltroChecklist("Sexo", sexosArr));
        panelTop.add(crearFiltroChecklist("Marca", marcasArr));
        panelTop.add(crearFiltroChecklist("Categoría", categoriasArr));

        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> panelAgregarProducto.setVisible(true));
        panelTop.add(btnAgregar);

        panelTop.revalidate();
        panelTop.repaint();
    }

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
//        try {
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (Exception ignored) {}
        
        // Crear las vistas
        InterfazProductos interfazProductos = new InterfazProductos();
        PanelAgregarProducto panelAgregarProducto = new PanelAgregarProducto();

        // Crear el controlador con ambas vistas
        // El controlador se inicializa y carga los parámetros automáticamente
        ControllerProducto controller = new ControllerProducto(interfazProductos, panelAgregarProducto);

        controller.cargarParametros();
    }
}