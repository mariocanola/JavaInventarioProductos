package controller;

import java.util.List;

import DAO.productoDAO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import Model.Parametro;
import Model.Producto;
import View.PanelAgregarProducto;

/**
 * Controlador que coordina la interacción entre la vista {@link View.PanelAgregarProducto}
 * y la capa de persistencia {@link DAO.productoDAO}.
 * <p>
 * – Carga los parámetros dinámicos (marca, sexo, categoría) y los envía a la vista.<br>
 * – Valida los datos ingresados por el usuario.<br>
 * – Copia la imagen seleccionada a la carpeta {@code imagenes/} del proyecto.<br>
 * – Construye la instancia de {@link Model.Producto} y la persiste.
 * </p>
 *
 */
public class ControllerProducto {
		
	 private productoDAO productoDAO;
	    private PanelAgregarProducto panel;

	    /**
     * Crea un controlador para la vista indicada y registra los listeners
     * necesarios.
     *
     * @param panel instancia de la vista a controlar.
     */
    public ControllerProducto(PanelAgregarProducto panel) {
	        this.panel = panel;
	        productoDAO = new productoDAO();
        // Cargar parámetros al iniciar
        cargarParametros();
        // Registrar acción del botón guardar
        this.panel.addGuardarListener(e -> agregarProducto());
	    }

	    // Método para cargar los parámetros dinámicamente desde la base de datos
	    /**
     * Consulta los parámetros dinámicos en BD y se los pasa a la vista para
     * poblar los {@code JComboBox}.
     */
    public void cargarParametros() {
	        // Obtener las listas de marcas, sexos y categorías
	        List<Parametro> marcas = productoDAO.obtenerParametrosPorTema("marca");
	        List<Parametro> sexos = productoDAO.obtenerParametrosPorTema("sexo");
	        List<Parametro> categorias = productoDAO.obtenerParametrosPorTema("categoria");

	        // Pasar los datos a la vista para que los cargue en los JComboBox
	        panel.cargarParametros(marcas, sexos, categorias);
	    }
	    
	    /**
     * Recupera los datos del formulario, los valida, gestiona la copia de la
     * imagen y, si todo es correcto, persiste el nuevo producto.
     */
    public void agregarProducto() {
        // Validaciones básicas
        String nombre = panel.getNombre().trim();
        double precio = panel.getPrecio();
        int cantidad = panel.getCantidad();
        Parametro idMarca = panel.getMarcaSeleccionada();
        Parametro idSexo = panel.getSexoSeleccionado();
        Parametro idCategoria = panel.getCategoriaSeleccionada();
        String rutaImagenIngresada = panel.getImagenPath().trim();

        if (nombre.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (precio <= 0) {
            javax.swing.JOptionPane.showMessageDialog(null, "El precio debe ser mayor que cero", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cantidad < 0) {
            javax.swing.JOptionPane.showMessageDialog(null, "La cantidad no puede ser negativa", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idMarca == null || idSexo == null || idCategoria == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Debe seleccionar marca, sexo y categoría", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
	        // (los valores ya se obtuvieron en la sección de validación)
        // Nota: evitamos volver a obtenerlos

	        // Procesar imagen: copiar al directorio "imagenes" si viene de fuera
        String imagenPathRel = null;
        if (!rutaImagenIngresada.isEmpty()) {
            File projectDir = new File(System.getProperty("user.dir"));
            File imagesDir = new File(projectDir, "imagenes");
            if (!imagesDir.exists()) imagesDir.mkdirs();

            File origen = new File(rutaImagenIngresada);
            // si ya es relativo al proyecto
            if (!origen.isAbsolute()) {
                origen = new File(projectDir, rutaImagenIngresada);
            }
            if (origen.exists()) {
                File destino = new File(imagesDir, origen.getName());
                try {
                    if (!destino.equals(origen)) {
                        Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    imagenPathRel = "imagenes/" + destino.getName();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(null, "No se pudo copiar la imagen: " + ex.getMessage());
                }
            }
        }

        // Crear el objeto Producto con ruta de imagen
        Producto producto = new Producto(0, nombre, cantidad, precio, "Disponible", idMarca.darId(), idCategoria.darId(), idSexo.darId(), imagenPathRel);
	
	        // Llamar al DAO para agregar el producto
	        boolean exito = productoDAO.agregarProducto(producto);

	        if (exito) {
	            System.out.println("Producto agregado exitosamente");
	        } else {
	            System.out.println("Error al agregar el producto");
	        }
	    }
}
