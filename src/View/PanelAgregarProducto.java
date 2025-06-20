package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import Model.Parametro;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;

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

    public PanelAgregarProducto(JFrame owner) {
        super(owner, "Agregar Producto", true);
        initUI();
    }

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
    
    private JTextField createStyledTextField(JTextField field) {
        field.setFont(FIELD_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
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

    public String getNombre() { return txtNombre.getText(); }
    
    public double getPrecio() {
        try { return Double.parseDouble(txtPrecio.getText()); } 
        catch (NumberFormatException e) { return 0; }
    }

    public String getImagenPath() { return txtImagen.getText(); }

    public int getCantidad() {
        try { return Integer.parseInt(txtCantidad.getText()); } 
        catch (NumberFormatException e) { return 0; }
    }

    public Parametro getMarcaSeleccionada() { return (Parametro) comboMarca.getSelectedItem(); }
    public Parametro getSexoSeleccionado() { return (Parametro) comboSexo.getSelectedItem(); }
    public Parametro getCategoriaSeleccionada() { return (Parametro) comboCategoria.getSelectedItem(); }

    public void addGuardarListener(ActionListener listener) { btnGuardar.addActionListener(listener); }
    public void addSeleccionarImagenListener(ActionListener listener) { btnSeleccionarImagen.addActionListener(listener); }
    
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