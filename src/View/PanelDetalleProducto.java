package View;

import Model.Producto;
import javax.swing.*;
import java.awt.*;

public class PanelDetalleProducto extends JDialog {

    public PanelDetalleProducto(JFrame owner, Producto producto) {
        super(owner, "Detalle del Producto", true);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Imagen
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        ImageIcon icon = null;
        String imagePath = producto.darImagenPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            icon = new ImageIcon(imagePath);
            // Escalar la imagen para que se vea bien en el detalle
            Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }
        add(new JLabel(icon), gbc);
        
        // Resetear constraints
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Añadir campos de detalle
        addDetalleCampo(gbc, "ID:", String.valueOf(producto.darIdProducto()), 1);
        addDetalleCampo(gbc, "Nombre:", producto.darNombreProducto(), 2);
        addDetalleCampo(gbc, "Precio:", "$" + String.format("%.2f", producto.darPrecio()), 3);
        addDetalleCampo(gbc, "Cantidad:", String.valueOf(producto.darCantidad()), 4);
        addDetalleCampo(gbc, "Estado:", producto.darStatus(), 5);
        addDetalleCampo(gbc, "ID Marca:", String.valueOf(producto.darIdMarca()), 6);
        addDetalleCampo(gbc, "ID Categoría:", String.valueOf(producto.darIdCategoria()), 7);
        addDetalleCampo(gbc, "ID Sexo:", String.valueOf(producto.darIdSexo()), 8);
        
        // Botón para cerrar
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> setVisible(false));
        add(btnCerrar, gbc);

        pack(); // Ajustar el tamaño del diálogo al contenido
        setLocationRelativeTo(owner); // Centrar relativo a la ventana principal
    }

    private void addDetalleCampo(GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        JTextField txtValue = new JTextField(value, 20);
        txtValue.setEditable(false); // Campo de solo lectura
        txtValue.setBorder(null); // Sin borde para apariencia limpia
        txtValue.setBackground(getBackground()); // Mismo fondo que el diálogo
        add(txtValue, gbc);
    }
} 