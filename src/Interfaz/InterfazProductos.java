package Interfaz;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;   
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JScrollPane;


public class InterfazProductos extends JFrame {
    private JPanel panelGrid;

    public InterfazProductos() {
        setTitle("Inventario");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ---------- Panel superior (filtros + botón) ---------- */
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(Color.DARK_GRAY);

        // Filtros desplegables (checklist)
        String[] opcionesSexo = {"Hombre", "Mujer", "Unisex"};
        String[] opcionesMarca = {"Marca A", "Marca B", "Marca C"};
        String[] opcionesCategoria = {"Zapatos", "Camisas", "Pantalones"};

        panelTop.add(crearFiltroChecklist("Sexo", opcionesSexo));
        panelTop.add(crearFiltroChecklist("Marca", opcionesMarca));
        panelTop.add(crearFiltroChecklist("Categoría", opcionesCategoria));

        JButton btnAgregar = new JButton("agregar producto");
        panelTop.add(btnAgregar);
        // Espacio horizontal entre componentes
        panelTop.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(panelTop, BorderLayout.NORTH);

        /* ---------- Panel central (grid de productos) ---------- */
        panelGrid = new JPanel(new GridLayout(0, 4, 15, 15));  // 4 columnas, filas automáticas
        panelGrid.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(panelGrid,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(Color.BLACK);
        add(scroll, BorderLayout.CENTER);

        /* Ejemplo: añadir 8 productos de prueba */
        for (int i = 0; i < 50; i++) {
            panelGrid.add(crearTarjetaProducto(
                    new ImageIcon("ruta/a/placeholder.png"),
                    "Producto " + i, 
                    "$" + (10*i), 
                    "Disponible"));
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* ---------- Utilidades ---------- */
    private JButton crearFiltroChecklist(String titulo, String[] opciones) {
        JButton btn = new JButton(titulo + " \u25BE"); // flecha hacia abajo
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);

        // Popup con checklist
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        for (String op : opciones) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(op);
            item.setOpaque(true);
            item.setBackground(new Color(60, 60, 60));
            item.setForeground(Color.WHITE);
            menu.add(item);
        }

        btn.addActionListener(e -> menu.show(btn, 0, btn.getHeight()));
        return btn;
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(InterfazProductos::new);
    }
}
