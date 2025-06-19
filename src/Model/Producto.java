package Model;

/**
 * Representa un producto dentro del sistema de inventario.
 * 
 * Cada producto tiene un identificador, nombre, cantidad disponible,
 * precio, y pertenece a una categoría específica.
 * 
 * Esta clase forma parte del modelo de datos.
 * 
 * @author Mario alexander Cañola
 */
public class Producto {
    
    /** Identificador único del producto */
    private int id;

    /** Nombre del producto */
    private String nombre;

    /** Cantidad disponible en inventario */
    private int cantidad;

    /** Precio del producto */
    private double precio;

    /** Estado del producto (activo, inactivo, etc.) */
    private String status;

    /** Identificador de la relación con tema_parametro */
    private int idTemaParametro;

    /**
     * Crea un producto con todos sus atributos inicializados.
     * 
     * @param id Identificador único del producto
     * @param nombre Nombre del producto
     * @param cantidad Cantidad disponible en inventario
     * @param precio Precio del producto
     * @param categoria Categoría asociada al producto
     */
    public Producto(int id, String nombre, int cantidad, double precio, String status, int idTemaParametro) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.status = status;
        this.idTemaParametro = idTemaParametro;
    }

    /**
     * Retorna el identificador del producto.
     * @return id del producto
     */
    public int darIdProducto() {
        return id;
    }

    /**
     * Establece el identificador del producto.
     * @param id Nuevo id del producto
     */
    public void setIdProducto(int id) {
        this.id = id;
    }

    /**
     * Retorna el nombre del producto.
     * @return nombre del producto
     */
    public String darNombreProducto() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre Nuevo nombre del producto
     */
    public void setNombreProducto(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna la cantidad disponible del producto.
     * @return cantidad en inventario
     */
    public int darCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad disponible del producto.
     * @param cantidad Nueva cantidad en inventario
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Retorna el precio del producto.
     * @return precio
     */
    public double darPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     * @param precio Nuevo precio
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Retorna el estado del producto.
     * @return estado del producto
     */
    public String darStatus() {
        return status;
    }

    /**
     * Establece el estado del producto.
     * @param status Nuevo estado
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retorna el identificador de la relación con tema_parametro.
     * @return id del tema_parametro
     */
    public int darIdTemaParametro() {
        return idTemaParametro;
    }

    /**
     * Establece el identificador de la relación con tema_parametro.
     * @param idTemaParametro Nuevo id del tema_parametro
     */
    public void setIdTemaParametro(int idTemaParametro) {
        this.idTemaParametro = idTemaParametro;
    }
}
