package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;

import Model.Producto;
import Model.Parametro;
import controller.ControllerProducto;

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

    private List<Producto> ultimaListaProductos = new ArrayList<>();
    private List<String> marcasSeleccionadas = new ArrayList<>();
    private List<String> sexosSeleccionados = new ArrayList<>();
    private List<String> categoriasSeleccionadas = new ArrayList<>();

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
        panelGrid = new JPanel(new GridBagLayout());
        panelGrid.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(panelGrid,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(Color.BLACK);
        add(scroll, BorderLayout.CENTER);

        scroll.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                mostrarProductos(ultimaListaProductos); // Guarda la última lista mostrada en un atributo
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void mostrarProductos(List<Producto> productos) {
        ultimaListaProductos = productos;
        panelGrid.removeAll();

        // Calcula el ancho de una tarjeta (debe coincidir con el preferredSize de PanelCardProducto)
        int cardWidth = 180 + 15; // 180 de la tarjeta + 15 de margen
        int panelWidth = panelGrid.getParent().getWidth(); // El ancho del viewport del JScrollPane

        // Si el panel aún no está visible, usa un valor por defecto
        if (panelWidth == 0) panelWidth = 900;

        int cardsPerRow = Math.max(1, panelWidth / cardWidth);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            PanelCardProducto card = new PanelCardProducto(producto, p -> {
                if (controller != null) controller.mostrarDetalle(p);
            });

            gbc.gridx = i % cardsPerRow;
            gbc.gridy = i / cardsPerRow;
            panelGrid.add(card.getPanel(), gbc);
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
        
        // Crear botones de filtro con listeners
        JButton btnFiltroSexo = crearFiltroChecklist("Sexo", sexosArr);
        JButton btnFiltroMarca = crearFiltroChecklist("Marca", marcasArr);
        JButton btnFiltroCategoria = crearFiltroChecklist("Categoría", categoriasArr);
        
        // Crear menús emergentes
        JPopupMenu menuSexo = crearMenuFiltro(sexosArr, "Sexo");
        JPopupMenu menuMarca = crearMenuFiltro(marcasArr, "Marca");
        JPopupMenu menuCategoria = crearMenuFiltro(categoriasArr, "Categoría");
        
        // Configurar los listeners para los botones de filtro
        btnFiltroSexo.addActionListener(e -> 
            menuSexo.show(btnFiltroSexo, 0, btnFiltroSexo.getHeight())
        );
        
        btnFiltroMarca.addActionListener(e -> 
            menuMarca.show(btnFiltroMarca, 0, btnFiltroMarca.getHeight())
        );
        
        btnFiltroCategoria.addActionListener(e -> 
            menuCategoria.show(btnFiltroCategoria, 0, btnFiltroCategoria.getHeight())
        );
        
        panelTop.add(btnFiltroSexo);
        panelTop.add(btnFiltroMarca);
        panelTop.add(btnFiltroCategoria);
        
        // Botón para limpiar filtros
        JButton btnLimpiarFiltros = new JButton("Limpiar Filtros");
        btnLimpiarFiltros.addActionListener(e -> {
            marcasSeleccionadas.clear();
            sexosSeleccionados.clear();
            categoriasSeleccionadas.clear();
            if (controller != null) {
                controller.cargarProductos();
            }
        });
        panelTop.add(btnLimpiarFiltros);
   
        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> panelAgregarProducto.setVisible(true));
        panelTop.add(btnAgregar);

        panelTop.revalidate();
        panelTop.repaint();
    }

    private JPopupMenu crearMenuFiltro(String[] opciones, String tipo) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Agregar las opciones de filtro
        if (opciones != null && opciones.length > 0) {
            for (String opcion : opciones) {
                JCheckBoxMenuItem item = new JCheckBoxMenuItem(opcion);
                item.setOpaque(true);
                item.setBackground(new Color(60, 60, 60));
                item.setForeground(Color.WHITE);
                
                // Marcar como seleccionado si está en la lista de seleccionados
                boolean estaSeleccionado = false;
                switch(tipo) {
                    case "Sexo": 
                        estaSeleccionado = sexosSeleccionados.contains(opcion);
                        break;
                    case "Marca": 
                        estaSeleccionado = marcasSeleccionadas.contains(opcion);
                        break;
                    case "Categoría": 
                        estaSeleccionado = categoriasSeleccionadas.contains(opcion);
                        break;
                }
                item.setSelected(estaSeleccionado);
                
                // Configurar acción para seleccionar/deseleccionar filtro
                final String opcionFinal = opcion;
                item.addActionListener(e -> {
                    // Actualizar la lista de seleccionados según el estado del checkbox
                    List<String> listaSeleccionados = null;
                    switch(tipo) {
                        case "Sexo": 
                            listaSeleccionados = sexosSeleccionados;
                            break;
                        case "Marca": 
                            listaSeleccionados = marcasSeleccionadas;
                            break;
                        case "Categoría": 
                            listaSeleccionados = categoriasSeleccionadas;
                            break;
                    }
                    
                    if (item.isSelected()) {
                        if (!listaSeleccionados.contains(opcionFinal)) {
                            listaSeleccionados.add(opcionFinal);
                        }
                    } else {
                        listaSeleccionados.remove(opcionFinal);
                    }
                    aplicarFiltros();
                });
                
                menu.add(item);
            }
        } else {
            JMenuItem item = new JMenuItem("No hay opciones");
            item.setEnabled(false);
            menu.add(item);
        }
        
        return menu;
    }
    
    private JButton crearFiltroChecklist(String titulo, String[] opciones) {
        JButton btn = new JButton(titulo + " \u25BE"); // flecha hacia abajo
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public void setController(ControllerProducto controller) {
        this.controller = controller;
    }

    /**
     * Aplica los filtros seleccionados a la lista de productos.
     * Filtra por marca, sexo y categoría según lo seleccionado por el usuario.
     */
    private void aplicarFiltros() {
        if (controller == null) return;
        
        // Si no hay filtros seleccionados, cargar todos los productos
        if (marcasSeleccionadas.isEmpty() && sexosSeleccionados.isEmpty() && categoriasSeleccionadas.isEmpty()) {
            controller.cargarProductos();
            return;
        }
        
        List<Producto> todosProductos = obtenerTodosLosProductos();
        List<Producto> productosFiltrados = new ArrayList<>();
        
        for (Producto producto : todosProductos) {
            boolean cumpleFiltros = true;
            
            // Obtener los nombres de los parámetros del producto
            String nombreMarca = "";
            for (Parametro marca : comboMarca) {
                if (marca.darId() == producto.darIdMarca()) {
                    nombreMarca = marca.darNombre();
                    break;
                }
            }
            
            String nombreSexo = "";
            for (Parametro sexo : comboSexo) {
                if (sexo.darId() == producto.darIdSexo()) {
                    nombreSexo = sexo.darNombre();
                    break;
                }
            }
            
            String nombreCategoria = "";
            for (Parametro categoria : comboCategoria) {
                if (categoria.darId() == producto.darIdCategoria()) {
                    nombreCategoria = categoria.darNombre();
                    break;
                }
            }
            
            // Filtrar por marcas si hay seleccionadas
            if (cumpleFiltros && !marcasSeleccionadas.isEmpty()) {
                cumpleFiltros = marcasSeleccionadas.contains(nombreMarca);
            }
            
            // Filtrar por sexos si hay seleccionados
            if (cumpleFiltros && !sexosSeleccionados.isEmpty()) {
                cumpleFiltros = sexosSeleccionados.contains(nombreSexo);
            }
            
            // Filtrar por categorías si hay seleccionadas
            if (cumpleFiltros && !categoriasSeleccionadas.isEmpty()) {
                cumpleFiltros = categoriasSeleccionadas.contains(nombreCategoria);
            }
            
            if (cumpleFiltros) {
                productosFiltrados.add(producto);
            }
        }
        
        // Mostrar los productos filtrados
        mostrarProductos(productosFiltrados);
    }
    
    /**
     * Obtiene todos los productos del controlador.
     * @return Lista de todos los productos.
     */
    private List<Producto> obtenerTodosLosProductos() {
        if (controller != null) {
            return controller.obtenerTodosLosProductos();
        }
        return new ArrayList<>();
    }
    
    public void agregarProductoAlInicio(Producto producto) {
        PanelCardProducto card = new PanelCardProducto(producto, p -> {
            if (controller != null) {
                controller.mostrarDetalle(p);
            }
        });
        panelGrid.add(card.getPanel(), 0); // Lo agrega en la primera posición
        panelGrid.revalidate();
        panelGrid.repaint();
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