package View;

import Model.Producto;
import javax.swing.*;
import java.awt.*;

public class PanelDetalleProducto extends JDialog {

    public PanelDetalleProducto(JFrame owner, Producto producto, String nombreMarca, String nombreCategoria, String nombreSexo) {
        super(owner, "Detalle del Producto", true);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Un poco más de espacio

        // --- Información a la Izquierda (Columnas 0 y 1) ---
        gbc.anchor = GridBagConstraints.WEST;
        addDetalleCampo(gbc, "Nombre:", producto.darNombreProducto(), 1);
        addDetalleCampo(gbc, "Precio:", "$" + String.format("%.2f", producto.darPrecio()), 2);
        addDetalleCampo(gbc, "Cantidad:", String.valueOf(producto.darCantidad()), 3);
        addDetalleCampo(gbc, "Estado:", producto.darStatus(), 4);
        addDetalleCampo(gbc, "Marca:", nombreMarca, 5);
        addDetalleCampo(gbc, "Categoría:", nombreCategoria, 6);
        addDetalleCampo(gbc, "Sexo:", nombreSexo, 7);
        
        // --- Imagen a la Derecha (Columna 2) ---
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 8; // Ocupa 8 filas, alineada con los detalles
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // Permite que la celda crezca si se redimensiona
        gbc.weighty = 1.0;

        ImageIcon icon = null;
        String imagePath = producto.darImagenPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            icon = new ImageIcon(imagePath);
            // Escalar la imagen para que se vea bien en el detalle
            Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }
        JLabel lblImagen = new JLabel(icon);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImagen, gbc);
        
        // Botón para cerrar, debajo de la información
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Ocupa las dos columnas de la info
        gbc.gridheight = 1; // Reseteamos el height
        gbc.fill = GridBagConstraints.NONE;
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