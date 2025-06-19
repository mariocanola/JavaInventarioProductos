package View;

import javax.swing.*;
import java.awt.*;
import Model.Parametro;
import java.util.List;
import java.awt.event.ActionListener;

public class PanelAgregarProducto extends JPanel {

    private JTextField txtNombre, txtPrecio, txtCantidad;
    private JComboBox<Parametro> comboMarca, comboSexo, comboCategoria;
    private JButton btnGuardar, btnCancelar;

    public PanelAgregarProducto() {
        // Configuración de la interfaz
        setLayout(new GridLayout(6, 2, 10, 10));

        // Crear los campos de texto
        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        add(txtPrecio);

        add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        add(new JLabel("Marca:"));
        comboMarca = new JComboBox<>();
        add(comboMarca);

        add(new JLabel("Sexo:"));
        comboSexo = new JComboBox<>();
        add(comboSexo);

        add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>();
        add(comboCategoria);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        add(btnGuardar);
        add(btnCancelar);
    }

    // Método para cargar marcas, sexos y categorías en los JComboBox
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
    public String getNombre() {
        return txtNombre.getText();
    }
    
    public double getPrecio() {
        try {
            return Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getCantidad() {
        try {
            return Integer.parseInt(txtCantidad.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Parametro getMarcaSeleccionada() {
        return (Parametro) comboMarca.getSelectedItem();
    }

    public Parametro getSexoSeleccionado() {
        return (Parametro) comboSexo.getSelectedItem();
    }

    public Parametro getCategoriaSeleccionada() {
        return (Parametro) comboCategoria.getSelectedItem();
    }

    /**
     * Permite al controlador registrar un ActionListener en el botón Guardar.
     * @param listener ActionListener a registrar
     */
    public void addGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }
    
    /** Limpia los campos del formulario. */
    public void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        comboMarca.setSelectedIndex(-1);
        comboSexo.setSelectedIndex(-1);
        comboCategoria.setSelectedIndex(-1);
    }
}