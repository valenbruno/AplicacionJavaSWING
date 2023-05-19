package datos_tokio2021;

public class Deportista{
	private int id;
	private String apellidos;
	private String nombres;
	private String email;
	private String telefono;
	private int idPais;
	private Disciplina disciplina;
	
	public Deportista(String apellidos, String nombres, String email, String telefono, int idPais) {
		this.id = -1;
		this.apellidos = apellidos;
		this.nombres = nombres;
		this.email = email;
		this.telefono = telefono;
		this.idPais = idPais;                                               
	}   
	
	public Deportista(int id, String apellidos, String nombres, String email, String telefono, int idPais) {
		this.id = id;
		this.apellidos= apellidos;
		this.nombres = nombres;
		this.email = email;
		this.telefono = telefono;
		this.idPais = idPais;                                               
	}                                                       

	public int getId() {                                                   
		return id;                                                          
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getIdPais() {
		return idPais;
	}

	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	@Override
	public String toString() {
		return "Deportista [id=" + id + ", apellidos=" + apellidos + ", nombres=" + nombres + ", email=" + email
				+ ", telefono=" + telefono + ", idPais=" + idPais + ", disciplina=" + disciplina + "]";
	}
	
	
	
	
	
}
