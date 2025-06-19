package controller;

import java.util.List;

import DAO.ParametroDAO;
import DAO.productoDAO;
import Model.Parametro;
import Model.Producto;
import View.PanelAgregarProducto;

public class ControllerProducto {
		
	 private ParametroDAO parametroDAO;
	    private PanelAgregarProducto panel;

	    public ControllerProducto(PanelAgregarProducto panel) {
	        this.panel = panel;
	        parametroDAO = new ParametroDAO();
        // Cargar parámetros al iniciar
        cargarParametros();
        // Registrar acción del botón guardar
        this.panel.addGuardarListener(e -> agregarProducto());
	    }

	    // Método para cargar los parámetros dinámicamente desde la base de datos
	    public void cargarParametros() {
	        // Obtener las listas de marcas, sexos y categorías
	        List<Parametro> marcas = parametroDAO.obtenerParametrosPorTema("marca");
	        List<Parametro> sexos = parametroDAO.obtenerParametrosPorTema("sexo");
	        List<Parametro> categorias = parametroDAO.obtenerParametrosPorTema("categoria");

	        // Pasar los datos a la vista para que los cargue en los JComboBox
	        panel.cargarParametros(marcas, sexos, categorias);
	    }
	    
	    public void agregarProducto() {
	        // Obtener los valores de la vista
	        String nombre = panel.getNombre();
	        double precio = panel.getPrecio();
	        int cantidad = panel.getCantidad();
	        Parametro idMarca = panel.getMarcaSeleccionada();
	        Parametro idSexo = panel.getSexoSeleccionado();
	        Parametro idCategoria = panel.getCategoriaSeleccionada();

	        // Crear un objeto Producto con todos los IDs necesarios (id = 0 porque lo asigna la BD)
	        Producto producto = new Producto(0, nombre, cantidad, precio, "Disponible", 
	                                         idMarca.darId(), idCategoria.darId(), idSexo.darId());
	
	        // Llamar al DAO para agregar el producto
	        productoDAO productoDAO = new productoDAO();
	        boolean exito = productoDAO.agregarProducto(producto);

	        if (exito) {
	            System.out.println("Producto agregado exitosamente");
	        } else {
	            System.out.println("Error al agregar el producto");
	        }
	    }
}
