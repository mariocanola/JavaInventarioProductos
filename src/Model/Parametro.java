package Model;

/**
 * Representa un parámetro dentro del sistema.
 * 
 * Cada parámetro tiene un identificador y un nombre. 
 * 
 * Esta clase forma parte del modelo de datos.
 * 
 * @author Mario alexander Cañola
 */
public class Parametro {
    
    /** Identificador único del parámetro */
    private int id;

    /** Nombre del parámetro */
    private String nombre;
    
    /**
     * Crea un parámetro con todos sus atributos inicializados.
     * 
     * @param id Identificador único del parámetro
     * @param nombre Nombre del parámetro
     */
    public Parametro(int id, String nombre) {
        this.id = id;
        this.nombre = nombre; 
    }

    /**
     * Retorna el identificador del parámetro.
     * @return id del parámetro
     */
    public int darId() {
        return id;
    }

    /**
     * Establece el identificador del parámetro.
     * @param id Nuevo id del parámetro
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna el nombre del parámetro.
     * @return nombre del parámetro
     */
    public String darNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del parámetro.
     * @param nombre Nuevo nombre del parámetro
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Devuelve el nombre para mostrarlo automáticamente en los JComboBox.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
