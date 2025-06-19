package Model;

public class Tema {
	/** Identificador único del tema */
	private int id;
	
	 /** Nombre del tema */
	private String nombre;
	
	/**
     * Crea un tema con todos sus atributos inicializados.
     * 
     * @param id Identificador único del tema
     * @param nombre Nombre del tema
     */
	public Tema(int id, String nombre) {
		this.id = id;
		this.nombre = nombre; 
	}
	
	/**
     * Retorna el identificador del tema.
     * @return id del tema
     */
	public int getId() {
		return id;
	}
	
    /**
     * Establece el identificador del tema.
     * @param id Nuevo id del tema
     */
	public void setId(int id) {
		this.id = id;
	}
	

    /**
     * Retorna el nombre del tema.
     * @return nombre del tema
     */
	public String getNombre() {
		return nombre;
	}
	
	/**
    * Establece el nombre del tema.
    * @param nombre Nuevo nombre del tema
    */
	public void setNombre(String nombre) {
	    this.nombre = nombre;
	}
}
