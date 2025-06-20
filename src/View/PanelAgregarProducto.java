package View;

import javax.swing.*;
import java.awt.*;
import Model.Parametro;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;
import java.awt.event.ActionListener;

/**
 * Vista Swing que contiene un formulario para capturar los datos de un {@code Producto}.
 * <p>
 * Presenta campos para nombre, precio, cantidad, parámetros dinámicos (marca, sexo,
 * categoría) y la ruta de la imagen. Expone getters para que el controlador
 * recupere los valores y métodos para registrar listeners sobre los botones de
 * acción.
 * </p>
 *
 * @author Mario
 * @since 1.0
 */
public class PanelAgregarProducto extends JDialog {

    private JTextField txtNombre, txtPrecio, txtCantidad, txtImagen;
    private JComboBox<Parametro> comboMarca, comboSexo, comboCategoria;
    private JButton btnGuardar, btnCancelar, btnSeleccionarImagen;

    /**
     * Construye la interfaz y posiciona todos los componentes utilizando
     * {@link GridBagLayout}. No recibe parámetros porque la vista no depende de
     * otros componentes para su creación.
     */
    public PanelAgregarProducto(JFrame owner) {
        super(owner, "Agregar Producto", true); // Título y modalidad
        
        // Configuración de la interfaz con GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Nombre
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        add(txtNombre, gbc);
        row++;
        gbc.weightx = 0;

        // Precio
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrecio = new JTextField(15);
        add(txtPrecio, gbc);
        row++;
        gbc.weightx = 0;

        // Cantidad
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCantidad = new JTextField(15);
        add(txtCantidad, gbc);
        row++;
        gbc.weightx = 0;

        // Marca
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboMarca = new JComboBox<>();
        add(comboMarca, gbc);
        row++;
        gbc.weightx = 0;

        // Sexo
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboSexo = new JComboBox<>();
        add(comboSexo, gbc);
        row++;
        gbc.weightx = 0;

        // Categoría
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>();
        add(comboCategoria, gbc);
        row++;

        // Imagen
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtImagen = new JTextField(15);
        add(txtImagen, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        btnSeleccionarImagen = new JButton("Seleccionar...");
        add(btnSeleccionarImagen, gbc);

        // Acción para abrir el selector de archivos
        btnSeleccionarImagen.addActionListener(e -> {
            // Directorio inicial dentro del proyecto (p.ej. carpeta "imagenes")
            File initialDir = new File(System.getProperty("user.dir"), "imagenes");
            if (!initialDir.exists()) {
                initialDir.mkdirs();
            }
            JFileChooser chooser = new JFileChooser(initialDir);
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "gif"));
            int result = chooser.showOpenDialog(PanelAgregarProducto.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                // Guardamos la ruta relativa al proyecto para facilitar su persistencia
                File selected = chooser.getSelectedFile();
                String projectPath = System.getProperty("user.dir");
                String relativePath = selected.getAbsolutePath().replace(projectPath + File.separator, "");
                txtImagen.setText(relativePath);
            }
        });
        row++;
        gbc.weightx = 0;

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        add(btnGuardar, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0;
        add(btnCancelar, gbc);
        
        // Acción para el botón Cancelar
        btnCancelar.addActionListener(e -> setVisible(false));

        pack(); // Ajusta el tamaño de la ventana al contenido
        setLocationRelativeTo(owner); // Centra el diálogo respecto a la ventana principal
    }

    // Método para cargar marcas, sexos y categorías en los JComboBox
    /**
     * Carga las listas de parámetros en sus respectivos {@link JComboBox}.
     *
     * @param marcas     lista de parámetros de tema «marca».
     * @param sexos      lista de parámetros de tema «sexo».
     * @param categorias lista de parámetros de tema «categoria».
     */
    public void cargarParametros(List<Parametro> marcas, List<Parametro> sexos, List<Parametro> categorias) {
        // Limpiar los JComboBox antes de agregar nuevos elementos
        comboMarca.removeAllItems();
        comboSexo.removeAllItems();
        comboCategoria.removeAllItems();

        // Agregar los parámetros a los JComboBox
        for (Parametro p : marcas) {
            comboMarca.addItem(p);
        }

        for (Parametro p : sexos) {
            comboSexo.addItem(p);
        }

        for (Parametro p : categorias) {
            comboCategoria.addItem(p);
        }
    }

    // Métodos para obtener los valores introducidos por el usuario
    /**
     * @return el texto introducido en el campo nombre.
     */
    public String getNombre() {
        return txtNombre.getText();
    }
    
    /**
     * @return precio introducido; si el texto no es numérico devuelve {@code 0}.
     */
    public double getPrecio() {
        try {
            return Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * @return texto de la ruta de imagen ingresada o seleccionada.
     */
    public String getImagenPath() {
        return txtImagen.getText();
    }

    /**
     * @return cantidad introducida; si el texto no es numérico devuelve {@code 0}.
     */
    public int getCantidad() {
        try {
            return Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * @return parámetro seleccionado en el combo de marca o {@code null}.
     */
    public Parametro getMarcaSeleccionada() {
        return (Parametro) comboMarca.getSelectedItem();
    }

    /**
     * @return parámetro seleccionado en el combo de sexo o {@code null}.
     */
    public Parametro getSexoSeleccionado() {
        return (Parametro) comboSexo.getSelectedItem();
    }

    /**
     * @return parámetro seleccionado en el combo de categoría o {@code null}.
     */
    public Parametro getCategoriaSeleccionada() {
        return (Parametro) comboCategoria.getSelectedItem();
    }

    /**
     * Permite al controlador registrar un ActionListener en el botón Guardar.
     * @param listener ActionListener a registrar
     */
    /**
     * Registra un {@link ActionListener} para el botón Guardar.
     *
     * @param listener acción a ejecutar cuando el usuario pulse Guardar.
     */
    public void addGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }

    /**
     * Registra un {@link ActionListener} para el botón seleccionar imagen.
     *
     * @param listener acción a ejecutar cuando el usuario pulse Seleccionar…
     */
    public void addSeleccionarImagenListener(ActionListener listener) {
        btnSeleccionarImagen.addActionListener(listener);
    }
    
    /**
     * Restaura todos los campos a su estado inicial (vacíos y sin selección).
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