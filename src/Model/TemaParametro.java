package Model;

public class TemaParametro {
	
	/** Identificador único de la relación */
	private int id;
	
	/** Tema asociado en la relación */
    private Tema tema;
    
    /** Parámetro asociado en la relación */
    private Parametro parametro;
    
    /**
     * Crea una relación entre un tema y un parámetro.
     * 
     * @param id Identificador único de la relación
     * @param tema Tema asociado
     * @param parametro Parámetro asociado
     */
    public TemaParametro(int id, Tema tema, Parametro parametro) {
        this.id = id;
        this.tema = tema;
        this.parametro = parametro;
    }
    
    /**
     * Retorna el identificador de la relación.
     * @return id de la relación
     */
    public int getId() {
        return id;
    }
    
    /**
     * Establece el identificador de la relación.
     * @param id Nuevo id de la relación
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Retorna el tema asociado a la relación.
     * @return Tema asociado
     */
    public Tema getTema() {
        return tema;
    }
    
    /**
     * Establece el tema asociado a la relación.
     * @param tema Nuevo tema
     */
    public void setTema(Tema tema) {
    	this.tema = tema;
    }
    
    /**
     * Retorna el parámetro asociado a la relación.
     * @return Parámetro asociado
     */
    public Parametro getParametro() {
        return parametro;
    }
    
    /**
     * Establece el parámetro asociado a la relación.
     * @param parametro Nuevo parámetro
     */
    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }
}
