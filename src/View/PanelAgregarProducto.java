package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import Model.Parametro;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;

/**
 * Diálogo para agregar un nuevo producto al inventario.
 * <p>
 * Este diálogo proporciona un formulario con campos para ingresar la información
 * básica de un producto, incluyendo nombre, precio, cantidad, marca, categoría,
 * sexo y una imagen opcional.
 * </p>
 */
public class PanelAgregarProducto extends JDialog {
    // Colores personalizados
    private static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    private static final Color SECONDARY_COLOR = new Color(92, 184, 92);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(206, 212, 218);

    
    // Fuentes
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Componentes de la interfaz
    private JTextField txtNombre, txtPrecio, txtCantidad, txtImagen;
    private JComboBox<Parametro> comboMarca, comboSexo, comboCategoria;
    private JButton btnGuardar, btnCancelar, btnSeleccionarImagen;

    /**
     * Crea una nueva instancia del diálogo para agregar producto.
     *
     * @param owner El frame padre del diálogo
     */
    public PanelAgregarProducto(JFrame owner) {
        super(owner, "Agregar Producto", true);
        initUI();
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     * Configura el diseño, crea y posiciona todos los componentes visuales
     * necesarios para el formulario de agregar producto.
     */
    private void initUI() {
        // Configuración principal del diálogo
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Panel del título
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("NUEVO PRODUCTO");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel);
        
        // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Nombre
        addFormField(formPanel, gbc, "Nombre:", createStyledTextField(txtNombre = new JTextField(20)), row++);
        
        // Precio
        addFormField(formPanel, gbc, "Precio ($):", createStyledTextField(txtPrecio = new JTextField(20)), row++);
        
        // Cantidad
        addFormField(formPanel, gbc, "Cantidad:", createStyledTextField(txtCantidad = new JTextField(20)), row++);
        
        // Marca
        addFormField(formPanel, gbc, "Marca:", createStyledComboBox(comboMarca = new JComboBox<Parametro>()), row++);
        
        // Sexo
        addFormField(formPanel, gbc, "Sexo:", createStyledComboBox(comboSexo = new JComboBox<Parametro>()), row++);
        
        // Categoría
        addFormField(formPanel, gbc, "Categoría:", createStyledComboBox(comboCategoria = new JComboBox<Parametro>()), row++);
        
        // Imagen
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lblImagen = new JLabel("Imagen:");
        lblImagen.setFont(LABEL_FONT);
        formPanel.add(lblImagen, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtImagen = new JTextField();
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 5)
        ));
        txtImagen.setBorder(null);
        txtImagen.setFont(FIELD_FONT);
        txtImagen.setBackground(Color.WHITE);
        imagePanel.add(txtImagen, BorderLayout.CENTER);
        formPanel.add(imagePanel, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0;
        btnSeleccionarImagen = createButton("Examinar...", SECONDARY_COLOR);
        formPanel.add(btnSeleccionarImagen, gbc);
        
        // Configurar el selector de archivos
        btnSeleccionarImagen.addActionListener(e -> {
            File initialDir = new File(System.getProperty("user.dir"), "imagenes");
            if (!initialDir.exists()) {
                initialDir.mkdirs();
            }
            JFileChooser chooser = new JFileChooser(initialDir);
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "gif"));
            int result = chooser.showOpenDialog(PanelAgregarProducto.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                String projectPath = System.getProperty("user.dir");
                String relativePath = selected.getAbsolutePath().replace(projectPath + File.separator, "");
                txtImagen.setText(relativePath);
            }
        });
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnCancelar = createButton("Cancelar", new Color(108, 117, 125));
        btnGuardar = createButton("Guardar", PRIMARY_COLOR);
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        
        // Acción para el botón Cancelar
        btnCancelar.addActionListener(e -> dispose());
        
        // Agregar componentes al diálogo
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Configuración final del diálogo
        setMinimumSize(new Dimension(500, 600));
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
    }
    
    /**
     * Agrega un campo de formulario con su etiqueta correspondiente al panel especificado.
     *
     * @param panel Panel al que se agregará el campo
     * @param gbc Restricciones de diseño para posicionar el campo
     * @param label Texto de la etiqueta del campo
     * @param field Componente del campo de entrada
     * @param row Fila en la que se ubicará el campo
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, Component field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        
        // Restablecer gridwidth
        gbc.gridwidth = 1;
    }
    
    /**
     * Aplica un estilo consistente a un campo de texto.
     *
     * @param field Campo de texto al que se aplicará el estilo
     * @return El campo de texto con el estilo aplicado
     */
    private JTextField createStyledTextField(JTextField field) {
        field.setFont(FIELD_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    /**
     * Aplica un estilo consistente a un JComboBox.
     *
     * @param combo ComboBox al que se aplicará el estilo
     * @return El ComboBox con el estilo aplicado
     */
    private JComboBox<Parametro> createStyledComboBox(JComboBox<Parametro> combo) {
        combo.setFont(FIELD_FONT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    setText(value.toString());
                }
                return this;
            }
        });
        return combo;
    }
    
    /**
     * Crea un botón con un estilo consistente.
     *
     * @param text Texto que mostrará el botón
     * @param bgColor Color de fondo del botón
     * @return El botón creado con el estilo aplicado
     */
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    // Métodos existentes (sin cambios)
    public void cargarParametros(List<Parametro> marcas, List<Parametro> sexos, List<Parametro> categorias) {
        comboMarca.removeAllItems();
        comboSexo.removeAllItems();
        comboCategoria.removeAllItems();
    
        marcas.forEach(comboMarca::addItem);
        sexos.forEach(comboSexo::addItem);
        categorias.forEach(comboCategoria::addItem);
    }

    /**
     * Obtiene el nombre ingresado en el campo correspondiente.
     * Valida que no esté vacío y que solo contenga letras, números y espacios.
     * @return el nombre del producto
     * @throws IllegalArgumentException si el campo está vacío o contiene caracteres no permitidos
     */
    public String getNombre() { 
        String nombre = txtNombre.getText().trim();
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El campo 'Nombre' es obligatorio");
        }
        // Validar que solo contenga letras, números y espacios
        if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras, números y espacios");
        }
        return nombre;
    }
    
    /**
     * Obtiene el precio ingresado en el campo correspondiente.
     * Valida que sea un número decimal mayor que cero.
     * @return el precio del producto
     * @throws NumberFormatException si el valor no es un número decimal válido o es menor o igual a cero
     */
    public double getPrecio() throws NumberFormatException {
        String precioText = txtPrecio.getText().trim().replace(",", ".");
        try {
            double precio = Double.parseDouble(precioText);
            if (precio <= 0) {
                throw new NumberFormatException("El precio debe ser un número mayor que cero");
            }
            return precio;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("El precio debe ser un número decimal");
        }
    }

    /**
     * Obtiene la ruta de la imagen ingresada en el campo correspondiente.
     * @return la ruta de la imagen
     */
    public String getImagenPath() { return txtImagen.getText(); }

    /**
     * Obtiene la cantidad ingresada en el campo correspondiente.
     * Valida que sea un número entero no negativo.
     * @return la cantidad del producto
     * @throws NumberFormatException si el valor no es un entero válido o es negativo
     */
    public int getCantidad() throws NumberFormatException {
        String cantidadText = txtCantidad.getText().trim();
        try {
            // Verificar si contiene punto o coma (números decimales)
            if (cantidadText.contains(".") || cantidadText.contains(",")) {
                throw new NumberFormatException("La cantidad debe ser un número entero (sin decimales)");
            }
            int cantidad = Integer.parseInt(cantidadText);
            if (cantidad < 0) {
                throw new NumberFormatException("La cantidad no puede ser negativa");
            }
            return cantidad;
        } catch (NumberFormatException e) {
            if (e.getMessage().startsWith("La cantidad")) {
                throw e; // Ya tiene un mensaje personalizado
            }
            throw new NumberFormatException("La cantidad debe ser un número entero (ejemplo: 5)");
        }
    }

    /**
     * Obtiene la marca seleccionada en el combo box.
     * @return la marca seleccionada
     * @throws IllegalArgumentException si no se ha seleccionado ninguna marca
     */
    public Parametro getMarcaSeleccionada() { 
        Parametro marca = (Parametro) comboMarca.getSelectedItem();
        if (marca == null) {
            throw new IllegalArgumentException("Por favor seleccione una marca");
        }
        return marca;
    }
    /**
     * Obtiene el sexo seleccionado en el combo box.
     * @return el sexo seleccionado
     * @throws IllegalArgumentException si no se ha seleccionado ningún género
     */
    public Parametro getSexoSeleccionado() { 
        Parametro sexo = (Parametro) comboSexo.getSelectedItem();
        if (sexo == null) {
            throw new IllegalArgumentException("Por favor seleccione un género");
        }
        return sexo;
    }
    /**
     * Obtiene la categoría seleccionada en el combo box.
     * @return la categoría seleccionada
     * @throws IllegalArgumentException si no se ha seleccionado ninguna categoría
     */
    public Parametro getCategoriaSeleccionada() { 
        Parametro categoria = (Parametro) comboCategoria.getSelectedItem();
        if (categoria == null) {
            throw new IllegalArgumentException("Por favor seleccione una categoría");
        }
        return categoria;
    }

    /**
     * Agrega un ActionListener al botón Guardar.
     * @param listener el ActionListener a agregar
     */
    public void addGuardarListener(ActionListener listener) { btnGuardar.addActionListener(listener); }
    /**
     * Agrega un ActionListener al botón Seleccionar Imagen.
     * @param listener el ActionListener a agregar
     */
    public void addSeleccionarImagenListener(ActionListener listener) { btnSeleccionarImagen.addActionListener(listener); }
    
    /**
     * Limpia todos los campos del formulario y restablece las selecciones.
     */
    public void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtImagen.setText("");
        comboMarca.setSelectedIndex(-1);
        comboSexo.setSelectedIndex(-1);
        comboCategoria.setSelectedIndex(-1);
    }
}