package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import Model.Producto;
import java.util.function.Consumer;

public class PanelCardProducto {
    // Colores personalizados
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color TITLE_COLOR = new Color(51, 51, 51);
    private static final Color PRICE_COLOR = new Color(40, 167, 69);
    private static final Color STATUS_ACTIVE = new Color(40, 167, 69);
    private static final Color STATUS_INACTIVE = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);
    private static final Color HOVER_BORDER_COLOR = new Color(13, 110, 253);
    private static final Color HOVER_BG_COLOR = new Color(248, 249, 250);
    
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font STATUS_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    private final JPanel panel;

    public PanelCardProducto(Producto producto, Consumer<Producto> onCardClick) {
        this.panel = crearTarjetaProducto(producto, onCardClick);
    }

    public JPanel getPanel() {
        return panel;
    }

    private JPanel crearTarjetaProducto(Producto producto, Consumer<Producto> onCardClick) {
        // Configuración de la tarjeta
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(true);

        // Panel para la imagen
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Cargar y escalar la imagen
        ImageIcon img = null;
        String imagePath = producto.darImagenPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            img = new ImageIcon(imagePath);
        }
        
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(JLabel.CENTER);
        if (img != null && img.getIconWidth() > 0) {
            Image scaled = img.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaled));
        } else {
            // Mostrar un placeholder si no hay imagen
            lblImg.setIcon(createPlaceholderIcon(150, 150, "Sin imagen"));
        }
        imagePanel.add(lblImg, BorderLayout.CENTER);
        card.add(imagePanel);

        // Nombre del producto
        JLabel lblNombre = new JLabel(producto.darNombreProducto());
        lblNombre.setFont(TITLE_FONT);
        lblNombre.setForeground(TITLE_COLOR);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        card.add(lblNombre);

        // Precio
        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", producto.darPrecio()));
        lblPrecio.setFont(PRICE_FONT);
        lblPrecio.setForeground(PRICE_COLOR);
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPrecio.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(lblPrecio);

        // Estado
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statusPanel.setOpaque(false);
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblStatusDot = new JLabel("•");
        lblStatusDot.setFont(new Font("Arial", Font.BOLD, 20));
        lblStatusDot.setForeground(
            "Activo".equalsIgnoreCase(producto.darStatus()) ? STATUS_ACTIVE : STATUS_INACTIVE
        );
        
        JLabel lblStatus = new JLabel(producto.darStatus());
        lblStatus.setFont(STATUS_FONT);
        lblStatus.setForeground(STATUS_INACTIVE);
        
        statusPanel.add(lblStatusDot);
        statusPanel.add(lblStatus);
        card.add(statusPanel);

        // Efecto hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onCardClick != null) {
                    onCardClick.accept(producto);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(HOVER_BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                card.setBackground(HOVER_BG_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                card.setBackground(CARD_BG);
            }
        });

        return card;
    }
    
    private ImageIcon createPlaceholderIcon(int width, int height, String text) {
        // Crear una imagen de marcador de posición
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Configuración de renderizado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fondo
        g2d.setColor(new Color(248, 249, 250));
        g2d.fillRect(0, 0, width, height);
        
        // Borde
        g2d.setColor(new Color(222, 226, 230));
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRect(1, 1, width-3, height-3);
        
        // Icono de cámara
        int iconSize = Math.min(width, height) / 3;
        int iconX = (width - iconSize) / 2;
        int iconY = (height - iconSize) / 2 - 10;
        
        g2d.setColor(new Color(173, 181, 189));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(iconX, iconY, iconSize, iconSize);
        g2d.fillOval(iconX + iconSize/4, iconY + iconSize/4, 2, 2);
        g2d.drawLine(iconX + iconSize/2, iconY + iconSize/2, 
                     iconX + iconSize, iconY + iconSize);
        
        // Texto
        g2d.setColor(new Color(134, 142, 150));
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = (width - textWidth) / 2;
        int textY = iconY + iconSize + 20;
        g2d.drawString(text, textX, textY);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
}