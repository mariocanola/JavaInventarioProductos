package View;

import Model.Producto;
import controller.ControllerProducto;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PanelDetalleProducto extends JDialog {

    @SuppressWarnings("unused")
    private final ControllerProducto controller;
    private final Producto producto;

    // Colores personalizados
    private static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    private static final Color SECONDARY_COLOR = new Color(92, 184, 92);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public PanelDetalleProducto(JFrame owner, Producto producto, String nombreMarca, String nombreCategoria, String nombreSexo, ControllerProducto controller) {
        super(owner, "Detalle del Producto", true);
        this.controller = controller;
        this.producto = producto;
        
        // Configuración principal del diálogo
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Panel de encabezado
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("DETALLES DEL PRODUCTO");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(titleLabel);
        
        // Panel principal para el contenido
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel para la información del producto
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Agregar campos de información
        addStyledDetailField(infoPanel, gbc, "Nombre:", this.producto.darNombreProducto(), 0);
        addStyledDetailField(infoPanel, gbc, "Precio:", "$" + String.format("%.2f", this.producto.darPrecio()), 1);
        addStyledDetailField(infoPanel, gbc, "Cantidad:", String.valueOf(this.producto.darCantidad()), 2);
        addStyledDetailField(infoPanel, gbc, "Estado:", this.producto.darStatus(), 3);
        addStyledDetailField(infoPanel, gbc, "Marca:", nombreMarca, 4);
        addStyledDetailField(infoPanel, gbc, "Categoría:", nombreCategoria, 5);
        addStyledDetailField(infoPanel, gbc, "Sexo:", nombreSexo, 6);
        
        // Panel para la imagen
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(BACKGROUND_COLOR);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        ImageIcon icon = null;
        String imagePath = this.producto.darImagenPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            icon = new ImageIcon(imagePath);
            // Escalar la imagen manteniendo la relación de aspecto
            Image img = icon.getImage();
            int maxSize = 250;
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();
            if (width > height) {
                height = height * maxSize / width;
                width = maxSize;
            } else {
                width = width * maxSize / height;
                height = maxSize;
            }
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        } else {
            // Si no hay imagen, mostrar un placeholder
            icon = new ImageIcon(getClass().getResource("/icons/no-image.png"));
            if (icon.getIconWidth() < 0) { // Si no se encuentra el recurso
                icon = new ImageIcon(createPlaceholderImage(200, 200, "Sin imagen"));
            }
        }
        
        JLabel lblImagen = new JLabel(icon, JLabel.CENTER);
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        imagePanel.add(lblImagen, BorderLayout.CENTER);
        
        // Agregar paneles al contenido
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.add(infoPanel);
        mainContent.add(imagePanel);
        
        contentPanel.add(mainContent, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        // Botón de Actualizar
        JButton btnActualizar = new JButton("Actualizar");
        styleButton(btnActualizar, WARNING_COLOR);
        btnActualizar.addActionListener(e -> {
            // Lógica para actualizar el producto
            JOptionPane.showMessageDialog(this, "Funcionalidad de actualización en desarrollo", "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Botón de Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        styleButton(btnEliminar, DANGER_COLOR);
        btnEliminar.addActionListener(e -> {
            // Lógica para eliminar el producto
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea eliminar este producto?", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION && controller != null) {
                boolean eliminado = controller.eliminarProducto(this.producto.darIdProducto());
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cerrar la ventana de detalles
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Botón de Cerrar
        JButton btnCerrar = new JButton("Cerrar");
        styleButton(btnCerrar, SECONDARY_COLOR);
        btnCerrar.addActionListener(e -> dispose());
        
        // Agregar botones al panel
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnCerrar);
        
        // Agregar todos los paneles al diálogo
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
        setResizable(true);
        setMinimumSize(new Dimension(800, 500));
    }

    private void addStyledDetailField(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        // Etiqueta
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(Color.DARK_GRAY);
        panel.add(lbl, gbc);
        
        // Valor
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField txtValue = new JTextField(value);
        txtValue.setEditable(false);
        txtValue.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtValue.setBackground(Color.WHITE);
        txtValue.setFont(VALUE_FONT);
        txtValue.setForeground(Color.BLACK);
        txtValue.setOpaque(true);
        panel.add(txtValue, gbc);
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
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
}