package datos_tokio2021;

public class Pais {
	private int id;
	private String nombre;
	
	public Pais(String nombre) {
		this.id = -1;
		this.nombre = nombre;
	}
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	@Override
	public String toString() {
		return "\tID: " + id + " - " + nombre;
	}
	
	
	
}
