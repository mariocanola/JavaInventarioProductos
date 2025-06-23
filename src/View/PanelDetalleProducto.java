package View;

import Model.Producto;
import Model.Parametro;
import controller.ControllerProducto;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Diálogo para visualizar y editar los detalles de un producto existente.
 * <p>
 * Este diálogo muestra la información detallada de un producto y permite su edición
 * o eliminación. Incluye campos para modificar el nombre, precio, cantidad, marca,
 * categoría y sexo del producto, así como una vista previa de su imagen.
 * </p>
 */
public class PanelDetalleProducto extends JDialog {
    private final ControllerProducto controller;
    private final Producto producto;
    private JPanel infoPanel;
    private JButton btnEditar, btnActualizar, btnCancelar, btnEliminar, btnCerrar;
    private JTextField txtNombre, txtPrecio, txtCantidad;
    private JComboBox<Parametro> cmbMarca, cmbCategoria, cmbSexo;
    private List<Parametro> marcas, categorias, sexos;
    
    private static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    private static final Color SECONDARY_COLOR = new Color(92, 184, 92);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 12);
    
    /**
     * Crea una nueva instancia del diálogo de detalles del producto.
     *
     * @param owner El frame padre del diálogo
     * @param producto El producto cuyos detalles se mostrarán
     * @param nombreMarca Nombre de la marca del producto
     * @param nombreCategoria Nombre de la categoría del producto
     * @param nombreSexo Nombre del sexo al que está dirigido el producto
     * @param controller Controlador que gestiona las operaciones del producto
     */
    public PanelDetalleProducto(JFrame owner, Producto producto, String nombreMarca, 
            String nombreCategoria, String nombreSexo, ControllerProducto controller) {
        super(owner, "Detalle del Producto", true);
        this.controller = controller;
        this.producto = producto;
        
        // Cargar parámetros
        cargarParametros();
        
        // Configuración de la ventana
        configurarVentana();
        
        // Crear componentes
        JPanel headerPanel = crearHeaderPanel();
        infoPanel = crearInfoPanel(nombreMarca, nombreCategoria, nombreSexo);
        JPanel imagePanel = crearImagePanel();
        JPanel buttonPanel = crearButtonPanel();
        
        // Agregar componentes a la ventana
        add(headerPanel, BorderLayout.NORTH);
        add(crearMainContentPanel(infoPanel, imagePanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
    }
    
    /**
     * Configura las propiedades básicas de la ventana del diálogo.
     * Establece el diseño, el comportamiento de cierre y el color de fondo.
     */
    private void configurarVentana() {
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }
    
    /**
     * Crea el panel de encabezado del diálogo.
     *
     * @return Panel de encabezado con el título
     */
    private JPanel crearHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Detalles del Producto");
        lblTitulo.setFont(TITLE_FONT);
        lblTitulo.setForeground(Color.WHITE);
        panel.add(lblTitulo);
        
        return panel;
    }
    
    /**
     * Crea el panel que contiene los campos de información del producto.
     *
     * @param nombreMarca Nombre de la marca del producto
     * @param nombreCategoria Nombre de la categoría del producto
     * @param nombreSexo Nombre del sexo al que está dirigido el producto
     * @return Panel con los campos de información del producto
     */
    private JPanel crearInfoPanel(String nombreMarca, String nombreCategoria, String nombreSexo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Campos de texto
        txtNombre = new JTextField(producto.darNombreProducto());
        txtPrecio = new JTextField(String.format("%.2f", producto.darPrecio()));
        txtCantidad = new JTextField(String.valueOf(producto.darCantidad()));
        
        // Combo boxes
        cmbMarca = new JComboBox<>(marcas.toArray(new Parametro[0]));
        cmbCategoria = new JComboBox<>(categorias.toArray(new Parametro[0]));
        cmbSexo = new JComboBox<>(sexos.toArray(new Parametro[0]));
        
        // Seleccionar valores actuales en los combos
        seleccionarValorCombo(cmbMarca, producto.darIdMarca());
        seleccionarValorCombo(cmbCategoria, producto.darIdCategoria());
        seleccionarValorCombo(cmbSexo, producto.darIdSexo());
        
        // Hacer los campos no editables inicialmente
        setCamposEditables(false);
        
        // Agregar campos al panel
        addLabeledField(panel, gbc, "Nombre:", txtNombre, 0);
        addLabeledField(panel, gbc, "Precio:", txtPrecio, 1);
        addLabeledField(panel, gbc, "Cantidad:", txtCantidad, 2);
        addLabeledField(panel, gbc, "Marca:", cmbMarca, 3);
        addLabeledField(panel, gbc, "Categoría:", cmbCategoria, 4);
        addLabeledField(panel, gbc, "Sexo:", cmbSexo, 5);
        
        return panel;
    }
    
    /**
     * Crea el panel que contiene los botones de acción.
     * Incluye botones para editar, actualizar, cancelar, eliminar y cerrar.
     *
     * @return Panel con los botones de acción
     */
    private JPanel crearButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Botón de Editar
        btnEditar = new JButton("Editar");
        styleButton(btnEditar, WARNING_COLOR);
        btnEditar.addActionListener(e -> habilitarEdicion());
        
        // Botón de Actualizar
        btnActualizar = new JButton("Actualizar");
        styleButton(btnActualizar, PRIMARY_COLOR);
        btnActualizar.setVisible(false);
        btnActualizar.addActionListener(e -> actualizarProducto());
        
        // Botón de Cancelar
        btnCancelar = new JButton("Cancelar");
        styleButton(btnCancelar, DANGER_COLOR);
        btnCancelar.setVisible(false);
        btnCancelar.addActionListener(e -> cancelarEdicion());
        
        // Botón de Eliminar
        btnEliminar = new JButton("Eliminar");
        styleButton(btnEliminar, DANGER_COLOR);
        btnEliminar.addActionListener(e -> confirmarEliminacion());
        
        // Botón de Cerrar
        btnCerrar = new JButton("Cerrar");
        styleButton(btnCerrar, SECONDARY_COLOR);
        btnCerrar.addActionListener(e -> dispose());
        
        // Agregar botones al panel
        panel.add(btnEditar);
        panel.add(btnActualizar);
        panel.add(btnCancelar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    /**
     * Crea el panel principal que contiene tanto la información del producto como su imagen.
     *
     * @param infoPanel Panel con la información del producto
     * @param imagePanel Panel con la imagen del producto
     * @return Panel principal que contiene ambos paneles
     */
    private JPanel crearMainContentPanel(JPanel infoPanel, JPanel imagePanel) {
        JPanel mainPanel = new JPanel(new BorderLayout(30, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel infoWrapper = new JPanel(new BorderLayout());
        infoWrapper.setBackground(BACKGROUND_COLOR);
        infoWrapper.add(infoPanel, BorderLayout.CENTER);
        
        mainPanel.add(infoWrapper, BorderLayout.CENTER);
        mainPanel.add(imagePanel, BorderLayout.EAST);
        return mainPanel;
    }
    
    @SuppressWarnings("unchecked")
    /**
     * Carga los parámetros necesarios (marcas, categorías, sexos) desde el controlador.
     * Utiliza reflexión para acceder a los métodos del DAO a través del controlador.
     */
    private void cargarParametros() {
        // Inicializar listas vacías
        marcas = new ArrayList<>();
        categorias = new ArrayList<>();
        sexos = new ArrayList<>();
        
        if (controller != null) {
            try {
                java.lang.reflect.Field field = controller.getClass().getDeclaredField("productoDAO");
                field.setAccessible(true);
                Object productoDAO = field.get(controller);
                
                java.lang.reflect.Method metodoMarca = productoDAO.getClass().getMethod("obtenerParametrosPorTema", String.class);
                marcas = (List<Parametro>) metodoMarca.invoke(productoDAO, "marca");
                
                java.lang.reflect.Method metodoCategoria = productoDAO.getClass().getMethod("obtenerParametrosPorTema", String.class);
                categorias = (List<Parametro>) metodoCategoria.invoke(productoDAO, "categoria");
                
                java.lang.reflect.Method methodSexo = productoDAO.getClass().getMethod("obtenerParametrosPorTema", String.class);
                sexos = (List<Parametro>) methodSexo.invoke(productoDAO, "sexo");
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar los parámetros: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        // Label
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(new Color(60, 60, 60));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lbl, gbc);
        
        // Field
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(200, 28));
            ((JTextField)field).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(200, 32));
            field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
            ));
            field.setOpaque(true);
            field.setBackground(Color.WHITE);
        }
        panel.add(field, gbc);
    }
    
    private void seleccionarValorCombo(JComboBox<Parametro> combo, int id) {
        if (combo == null) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            Parametro param = combo.getItemAt(i);
            if (param != null && param.darId() == id) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void setCamposEditables(boolean editable) {
        if (txtNombre != null) txtNombre.setEditable(editable);
        if (txtPrecio != null) txtPrecio.setEditable(editable);
        if (txtCantidad != null) txtCantidad.setEditable(editable);
        if (cmbMarca != null) cmbMarca.setEnabled(editable);
        if (cmbCategoria != null) cmbCategoria.setEnabled(editable);
        if (cmbSexo != null) cmbSexo.setEnabled(editable);
        
        Color bgColor = editable ? new Color(255, 255, 200) : Color.WHITE;
        if (txtNombre != null) txtNombre.setBackground(bgColor);
        if (txtPrecio != null) txtPrecio.setBackground(bgColor);
        if (txtCantidad != null) txtCantidad.setBackground(bgColor);
    }
    
    private void habilitarEdicion() {
        setCamposEditables(true);
        if (btnEditar != null) btnEditar.setVisible(false);
        if (btnActualizar != null) btnActualizar.setVisible(true);
        if (btnCancelar != null) btnCancelar.setVisible(true);
        if (btnEliminar != null) btnEliminar.setVisible(false);
        if (btnCerrar != null) btnCerrar.setVisible(false);
    }
    
    private void cancelarEdicion() {
        // Restaurar valores originales
        if (txtNombre != null) txtNombre.setText(producto.darNombreProducto());
        if (txtPrecio != null) txtPrecio.setText(String.format("%.2f", producto.darPrecio()));
        if (txtCantidad != null) txtCantidad.setText(String.valueOf(producto.darCantidad()));
        if (cmbMarca != null) seleccionarValorCombo(cmbMarca, producto.darIdMarca());
        if (cmbCategoria != null) seleccionarValorCombo(cmbCategoria, producto.darIdCategoria());
        if (cmbSexo != null) seleccionarValorCombo(cmbSexo, producto.darIdSexo());
        
        // Restaurar estado de la interfaz
        setCamposEditables(false);
        if (btnEditar != null) btnEditar.setVisible(true);
        if (btnActualizar != null) btnActualizar.setVisible(false);
        if (btnCancelar != null) btnCancelar.setVisible(false);
        if (btnEliminar != null) {
            btnEliminar.setEnabled(true);
            btnEliminar.setVisible(true);
        }
        if (btnCerrar != null) {
            btnCerrar.setEnabled(true);
            btnCerrar.setVisible(true);
        }
    }
    
    private void actualizarProducto() {
        // Validar campos
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double precio;
        try {
            // Reemplazar comas por puntos para el análisis
            String precioStr = txtPrecio.getText().trim().replace(",", ".");
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número mayor a cero", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero no negativo", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Parametro marcaSeleccionada = (Parametro) cmbMarca.getSelectedItem();
        Parametro categoriaSeleccionada = (Parametro) cmbCategoria.getSelectedItem();
        Parametro sexoSeleccionado = (Parametro) cmbSexo.getSelectedItem();
        
        if (marcaSeleccionada == null || categoriaSeleccionada == null || sexoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar marca, categoría y sexo", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Actualizar el producto
        producto.setNombreProducto(nombre);
        producto.setPrecio(precio);
        producto.setCantidad(cantidad);
        producto.setStatus(cantidad > 0 ? "Disponible" : "Agotado");
        producto.setIdMarca(marcaSeleccionada.darId());
        producto.setIdCategoria(categoriaSeleccionada.darId());
        producto.setIdSexo(sexoSeleccionado.darId());
        
        // Llamar al controlador para guardar los cambios
        boolean exito = controller.actualizarProducto(producto);
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cancelarEdicion(); // Restaurar estado de la interfaz
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void confirmarEliminacion() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea eliminar este producto?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = controller.eliminarProducto(producto.darIdProducto());
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JPanel crearImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        ImageIcon icon = null;
        String imagePath = this.producto.darImagenPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                // Intentar cargar la imagen desde la ruta relativa o absoluta
                if (!new File(imagePath).isAbsolute()) {
                    // Si es una ruta relativa, intentar cargarla desde el directorio del proyecto
                    String projectPath = System.getProperty("user.dir");
                    File imageFile = new File(projectPath, imagePath);
                    if (imageFile.exists()) {
                        icon = new ImageIcon(imageFile.getAbsolutePath());
                    }
                } else {
                    icon = new ImageIcon(imagePath);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
            }
        }
        
        // Si no se pudo cargar la imagen, mostrar un placeholder
        if (icon == null || icon.getIconWidth() < 0) {
            icon = new ImageIcon(createPlaceholderImage(200, 200, "Sin imagen"));
        } else {
            // Escalar la imagen manteniendo la relación de aspecto
            Image img = icon.getImage();
            int maxSize = 250;
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();
            if (width > maxSize || height > maxSize) {
                double ratio = (double) width / height;
                if (width > height) {
                    width = maxSize;
                    height = (int) (width / ratio);
                } else {
                    height = maxSize;
                    width = (int) (height * ratio);
                }
                Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);
            }
        }
        
        JLabel lblImagen = new JLabel(icon, JLabel.CENTER);
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(lblImagen, BorderLayout.CENTER);
        
        return panel;
    }
    
    private Image createPlaceholderImage(int width, int height, String text) {
        // Crear una imagen de marcador de posición
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Rellenar con color de fondo
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        
        // Dibujar borde
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, width-1, height-1);
        
        // Dibujar texto
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return image;
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }
}
