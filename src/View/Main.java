package View;

import javax.swing.JFrame;
import controller.ControllerProducto;

/**
 * Punto de entrada de la aplicación de inventario.
 * <p>
 * Construye la vista {@link PanelAgregarProducto}, la enlaza con su
 * {@link controller.ControllerProducto} y muestra la ventana principal.
 * </p>
 */
public class Main {

    /**
     * Método principal. Inicializa MVC y lanza la interfaz.
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Crear la vista donde se agrega el producto
        PanelAgregarProducto panel = new PanelAgregarProducto();

        // Crear el controlador y pasarle la vista
        ControllerProducto controller = new ControllerProducto(panel);

        controller.cargarParametros();

        JFrame frame = new JFrame("Agregar Producto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.add(panel);
        frame.setVisible(true);
    }
}
