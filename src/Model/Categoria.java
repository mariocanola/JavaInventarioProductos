package Model;

public class Categoria {
	private int id;
	private String nombre;
	
	public Categoria(){
		
	}
	
	public Categoria (int id, String nombre) {
		this.id = id;
		this.nombre = nombre; 
	}
	
	public int darId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String darNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
	    this.nombre = nombre;
	}
}
