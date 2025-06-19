package controller;

import java.util.List;

import DAO.ParametroDAO;
import Model.Parametro;
import View.PanelAgregarProducto;

public class ControllerProducto {
		
	 private ParametroDAO parametroDAO;
	    private PanelAgregarProducto panel;

	    public ControllerProducto(PanelAgregarProducto panel) {
	        this.panel = panel;
	        parametroDAO = new ParametroDAO();
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
}
