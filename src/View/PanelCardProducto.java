package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Model.Producto;
import java.util.function.Consumer;

public class PanelCardProducto {
    private JPanel panel;

    public PanelCardProducto(Producto producto, Consumer<Producto> onCardClick) {
        panel = crearTarjetaProducto(producto, onCardClick);
    }

    public JPanel getPanel() {
        return panel;
    }

    private JPanel crearTarjetaProducto(Producto producto, Consumer<Producto> onCardClick) {
        ImageIcon img = null;
        String imagePath = producto.darImagenPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            img = new ImageIcon(imagePath);
        } else {
            img = new ImageIcon("ruta/a/placeholder.png");
        }
        
        String nombre = producto.darNombreProducto();
        String precio = "$" + String.format("%.2f", producto.darPrecio());
        String estado = producto.darStatus();

        JPanel card = new JPanel() {
            @Override
            public void updateUI() {
                super.updateUI();
                // Set background after UI is initialized
                setBackground(new Color(40, 40, 40));
            }
        };
        card.setPreferredSize(new Dimension(180, 220));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setOpaque(true);

        JLabel lblImg = new JLabel();
        if (img != null && img.getIconWidth() > 0) {
            java.awt.Image scaled = img.getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaled));
        }
        lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblImg);

        Font fuente = new Font("SansSerif", Font.PLAIN, 13);

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(fuente);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblNombre);

        JLabel lblPrecio = new JLabel(precio);
        lblPrecio.setForeground(new Color(144, 238, 144));
        lblPrecio.setFont(fuente);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblPrecio);

        JLabel lblEstado = new JLabel(estado);
        lblEstado.setForeground(Color.GRAY);
        lblEstado.setFont(fuente);
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblEstado);

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
                card.setBorder(BorderFactory.createLineBorder(new Color(0, 172, 237), 2));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                card.setCursor(Cursor.getDefaultCursor());
            }
        });

        return card;
    }
}
