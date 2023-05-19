package objetos_dao;

public class ManagerDAOjdbc{

	private static DeportistaDAOjdbc deportistaDAO = null;
	private static PaisDAOjdbc paisDAO = null;
	private static DisciplinaDAOjdbc disciplinaDAO = null;
	
	public static DeportistaDAOjdbc getDeportistaDAO() {
		
		if(deportistaDAO == null) 
			deportistaDAO = new DeportistaDAOjdbc();
		
		
		return deportistaDAO;
	}
	
	public static PaisDAOjdbc getPaisDAO() {
		
		if(paisDAO == null)
			paisDAO = new PaisDAOjdbc();
		
		return paisDAO;
	}
	
	public static DisciplinaDAOjdbc getDisciplinaDAO() {
		
		if(disciplinaDAO == null)
			disciplinaDAO = new DisciplinaDAOjdbc();
		
		return disciplinaDAO;
			
	}

}
