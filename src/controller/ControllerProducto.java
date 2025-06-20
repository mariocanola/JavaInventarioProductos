package controller;

import java.util.List;
import java.util.Comparator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import Model.Parametro;
import Model.Producto;
import View.InterfazProductos;
import View.PanelAgregarProducto;
import View.PanelDetalleProducto;
import DAO.ProductoDAO;

/**
 * Controlador que coordina la interacción entre la vista {@link View.InterfazProductos}
 * y la capa de persistencia {@link DAO.ProductoDAO}.
 * <p>
 * – Carga los parámetros dinámicos (marca, sexo, categoría) y los envía a la vista.<br>
 * – Valida los datos ingresados por el usuario.<br>
 * – Copia la imagen seleccionada a la carpeta {@code imagenes/} del proyecto.<br>
 * – Construye la instancia de {@link Model.Producto} y la persiste.
 * </p>
 *
 */
public class ControllerProducto {
		
	private ProductoDAO productoDAO;
	private InterfazProductos panelPrincipalInterfazProductos;
    private PanelAgregarProducto panelAgregarProducto;

	    /**
     * Crea un controlador para la vista indicada y registra los listeners
     * necesarios.
     *
     * @param interfazProductos instancia de la vista a controlar.
     */
    public ControllerProducto(InterfazProductos interfazProductos, PanelAgregarProducto panelAgregarProducto) {
            this.panelPrincipalInterfazProductos = interfazProductos;
            this.panelAgregarProducto = panelAgregarProducto;
            // 1. Inicializar el DAO
            this.productoDAO = new ProductoDAO();
            // 2. Cargar parámetros (ahora el DAO ya no es null)
            cargarParametros();
            // 3. Cargar productos
            cargarProductos();
            // 4. Registrar acción del botón guardar
            this.panelAgregarProducto.addGuardarListener(e -> agregarProducto());
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
	        panelAgregarProducto.cargarParametros(marcas, sexos, categorias);
            panelPrincipalInterfazProductos.cargarParametros(marcas, sexos, categorias);
	    }
	    
	    /**
     * Consulta los productos en BD y se los pasa a la vista para que los muestre.
     */
    public void cargarProductos() {
        List<Producto> productos = productoDAO.obtenerTodos();
        // Ordenar por ID descendente (el más reciente primero)
        productos.sort(Comparator.comparingInt(Producto::darIdProducto).reversed());
        panelPrincipalInterfazProductos.mostrarProductos(productos);
    }

    public void mostrarDetalle(Producto producto) {
        // Usar el DAO para obtener los nombres de los parámetros
        Parametro marca = productoDAO.obtenerParametroPorId(producto.darIdMarca());
        Parametro categoria = productoDAO.obtenerParametroPorId(producto.darIdCategoria());
        Parametro sexo = productoDAO.obtenerParametroPorId(producto.darIdSexo());

        // Pasar los nombres al diálogo de detalle
        PanelDetalleProducto detalleDialog = new PanelDetalleProducto(
            panelPrincipalInterfazProductos, 
            producto,
            marca != null ? marca.darNombre() : "N/A",
            categoria != null ? categoria.darNombre() : "N/A",
            sexo != null ? sexo.darNombre() : "N/A"
        );
        detalleDialog.setVisible(true);
    }

	    /**
     * Recupera los datos del formulario, los valida, gestiona la copia de la
     * imagen y, si todo es correcto, persiste el nuevo producto.
     */
    public void agregarProducto() {
        // Validaciones básicas
        String nombre = panelAgregarProducto.getNombre().trim();
        double precio = panelAgregarProducto.getPrecio();
        int cantidad = panelAgregarProducto.getCantidad();
        Parametro idMarca = panelAgregarProducto.getMarcaSeleccionada();
        Parametro idSexo = panelAgregarProducto.getSexoSeleccionado();
        Parametro idCategoria = panelAgregarProducto.getCategoriaSeleccionada();
        String rutaImagenIngresada = panelAgregarProducto.getImagenPath().trim();

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
	            javax.swing.JOptionPane.showMessageDialog(null, "Producto agregado exitosamente", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
	            panelAgregarProducto.setVisible(false);
	            panelAgregarProducto.limpiarCampos();

	            // Agregar el producto recién creado al principio de la interfaz
	            panelPrincipalInterfazProductos.agregarProductoAlInicio(producto);
	        } else {
	            javax.swing.JOptionPane.showMessageDialog(null, "Error al agregar el producto", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
	        }
	    }
}
