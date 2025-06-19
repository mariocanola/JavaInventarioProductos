package Interfaz;

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

public class PanelCardProducto {
    private JPanel panel;

    public PanelCardProducto(ImageIcon img, String nombre,
    String precio, String estado) {
        panel = crearTarjetaProducto(img, nombre, precio, estado);
    }

    public JPanel getPanel() {
        return panel;
    }

    private JPanel crearTarjetaProducto(ImageIcon img, String nombre,
    String precio, String estado) {

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(180, 220));
        card.setBackground(new Color(40, 40, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

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
