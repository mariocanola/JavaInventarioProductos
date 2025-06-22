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
        // Ordenar productos por ID de forma descendente
        productos.sort(Comparator.comparingInt(Producto::darIdProducto).reversed());
        panelPrincipalInterfazProductos.mostrarProductos(productos);
    }
    
    /**
     * Elimina un producto de la base de datos y actualiza la vista.
     *
     * @param idProducto ID del producto a eliminar
     * @return true si el producto fue eliminado exitosamente, false en caso contrario
     */
    public boolean eliminarProducto(int idProducto) {
        boolean exito = productoDAO.eliminarProducto(idProducto);
        if (exito) {
            // Actualizar la vista después de eliminar
            cargarProductos();
        }
        return exito;
    }
    
    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param producto el producto con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarProducto(Producto producto) {
        boolean exito = productoDAO.actualizarProducto(producto);
        if (exito) {
            // Actualizar la vista después de actualizar
            cargarProductos();
        }
        return exito;
    }
    
    /**
     * Obtiene todos los productos disponibles en el sistema.
     * @return Lista de todos los productos.
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoDAO.obtenerTodos();
    }
    
    /**
     * Carga los productos filtrados por ID de marca
     * @param idMarca ID de la marca por la que filtrar
     */
    public void cargarProductosPorMarca(int idMarca) {
        List<Producto> productos = productoDAO.obtenerProductosPorMarca(idMarca);
        // Ordenar por ID descendente (el más reciente primero)
        productos.sort(Comparator.comparingInt(Producto::darIdProducto).reversed());
        panelPrincipalInterfazProductos.mostrarProductos(productos);
    }
    
    /**
     * Muestra el diálogo de detalle para un producto
     * @param producto El producto a mostrar en el diálogo de detalle
     */
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
            sexo != null ? sexo.darNombre() : "N/A",
            this
        );
        detalleDialog.setVisible(true);
    }

	    /**
     * Recupera los datos del formulario, los valida, gestiona la copia de la
     * imagen y, si todo es correcto, persiste el nuevo producto.
     */
    public void agregarProducto() {
        try {
            // Obtener y validar los datos del formulario
            String nombre = panelAgregarProducto.getNombre(); // Ya valida que no esté vacío
            double precio = panelAgregarProducto.getPrecio(); // Ya valida que sea > 0
            int cantidad = panelAgregarProducto.getCantidad(); // Ya valida que sea >= 0
            Parametro marca = panelAgregarProducto.getMarcaSeleccionada(); // Ya valida que no sea null
            Parametro sexo = panelAgregarProducto.getSexoSeleccionado(); // Ya valida que no sea null
            Parametro categoria = panelAgregarProducto.getCategoriaSeleccionada(); // Ya valida que no sea null
            String rutaImagenIngresada = panelAgregarProducto.getImagenPath().trim();

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
            Producto producto = new Producto(0, nombre, cantidad, precio, "Disponible", 
                marca.darId(), categoria.darId(), sexo.darId(), imagenPathRel);
        
            // Llamar al DAO para agregar el producto
            boolean exito = productoDAO.agregarProducto(producto);

            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(null, "Producto agregado exitosamente", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                panelAgregarProducto.setVisible(false);
                panelAgregarProducto.limpiarCampos();

                // Agregar el producto recién creado al principio de la interfaz
                panelPrincipalInterfazProductos.agregarProductoAlInicio(producto);
                
                // Recargar la lista de productos
                cargarProductos();
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Error al agregar el producto", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(panelAgregarProducto, 
                "Error en formato numérico: " + e.getMessage(), 
                "Error de validación", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            javax.swing.JOptionPane.showMessageDialog(panelAgregarProducto, 
                e.getMessage(), 
                "Error de validación", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(panelAgregarProducto, 
                "Error inesperado: " + e.getMessage(), 
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
