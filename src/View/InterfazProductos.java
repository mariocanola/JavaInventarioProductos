package View;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;   
import javax.swing.JFrame;

import Model.Producto;
import Model.Parametro;
import controller.ControllerProducto;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.BorderFactory;
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

    String[] sexosArr;
    String[] marcasArr;
    String[] categoriasArr;

    private ControllerProducto controller;

    public InterfazProductos() {
        setTitle("Inventario");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar las listas   
        comboMarca = new ArrayList<>();
        comboSexo = new ArrayList<>();
        comboCategoria = new ArrayList<>();
        // Crear el diálogo una sola vez
        panelAgregarProducto = new PanelAgregarProducto(this);

        /* ---------- Panel superior (filtros + botón) ---------- */
        panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(Color.DARK_GRAY);

        // Espacio horizontal entre componentes
        panelTop.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(panelTop, BorderLayout.NORTH);

        /* ---------- Panel central (grid de productos) ---------- */
        panelGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelGrid.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(panelGrid,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(Color.BLACK);
        add(scroll, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void mostrarProductos(List<Producto> productos) {
        panelGrid.removeAll();

        for (Producto producto : productos) {
            // La acción de clic llama al controlador para mostrar el detalle
            PanelCardProducto card = new PanelCardProducto(producto, p -> {
                if (controller != null) {
                    controller.mostrarDetalle(p);
                }
            });
            panelGrid.add(card.getPanel());
        }

        panelGrid.revalidate();
        panelGrid.repaint();
    }

    public void cargarParametros(List<Parametro> marcas, List<Parametro> sexos, List<Parametro> categorias) {

        this.comboMarca.clear();
        this.comboMarca.addAll(marcas);
        this.comboSexo.clear();
        this.comboSexo.addAll(sexos);
        this.comboCategoria.clear();
        this.comboCategoria.addAll(categorias);

        this.panelTop.removeAll();

        this.sexosArr = this.comboSexo.stream().map(Parametro::darNombre).toArray(String[]::new);
        this.marcasArr = this.comboMarca.stream().map(Parametro::darNombre).toArray(String[]::new);
        this.categoriasArr = this.comboCategoria.stream().map(Parametro::darNombre).toArray(String[]::new);
        
        // Add the filter checklists with the loaded data
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

    public void setController(ControllerProducto controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        
        // Crear la vista principal
        InterfazProductos interfazProductos = new InterfazProductos();

        // Crear el controlador con la vista principal
        // El panelAgregarProducto ya está inicializado en el constructor de InterfazProductos
        ControllerProducto controller = new ControllerProducto(interfazProductos, interfazProductos.panelAgregarProducto);

        interfazProductos.setController(controller);
        controller.cargarParametros();
        controller.cargarProductos();
    }
}