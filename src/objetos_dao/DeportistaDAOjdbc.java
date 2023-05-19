package objetos_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connector.MyConnector;
import datos_tokio2021.Deportista;
import datos_tokio2021.Disciplina;
import excepciones.DAOException;
import excepciones.StatementException;
import interfaces_dao.DeportistaDAO;

public class DeportistaDAOjdbc implements DeportistaDAO{
	
	private Connection miConexion;
	
	private final String query_UPDATE = "UPDATE deportista SET apellidos=?, nombres=?, email=?, telefono=?, id_pais=? WHERE id=?";
	private final String query_UPDATE_inDisciplina = "UPDATE deportista_en_disciplina SET id_disciplina=? WHERE id_deportista=?";
	private final String query_DELETE_withID = "DELETE FROM deportista WHERE id=?";
	//private final String query_DELETE = "DELETE * FROM deportista WHERE apellidos=? AND nombres=? AND email=?";
	private final String query_DELETE_fromDisciplina = "DELETE FROM `deportista_en_disciplina` WHERE id_deportista=?";
	private final String query_INSERT_deportista = "INSERT INTO `deportista` (apellidos,nombres,email,telefono,id_pais) VALUES (?,?,?,?,?)";
	private final String query_INSERT_deportista_disciplina = "INSERT INTO `deportista_en_disciplina` (id_deportista, id_disciplina) VALUES (?,?)";
	private final String query_SELECT = "SELECT d FROM deportista WHERE d.id=?";
	private final String query_SELECT_ALL = "SELECT * FROM deportista";
	
	public DeportistaDAOjdbc() {
		miConexion = MyConnector.getConnection();
	}
	
	@Override
	public List<Deportista> cargar() throws DAOException, StatementException{
		Statement st = null;
		
		List<Deportista> deportistas = null;
		
		try {		

			st = miConexion.createStatement();
			
			deportistas = new ArrayList<Deportista>();
			
			ResultSet result = st.executeQuery(query_SELECT_ALL);
			
			while(result.next()) {
				deportistas.add(convertirResultSet(result));
			}
			
		}
		catch(SQLException e) {
			throw new DAOException("Error SQL", e);
		}
		finally {
			closeStatement(st);
		}
		
		
		return deportistas;
	}

	@Override
	public void eliminar(Deportista d) throws DAOException, StatementException {
	
		eliminar(d.getId());
		
	}
	
	public void eliminar(int id) throws DAOException, StatementException {
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		try {		
			
			MyConnector.getConnection().setAutoCommit(false);

			ps = miConexion.prepareStatement(query_DELETE_fromDisciplina);

			ps.clearParameters();
			ps.setInt(1, id);
			int t1 = ps.executeUpdate();

			ps2 = miConexion.prepareStatement(query_DELETE_withID);
				
			ps2.clearParameters();
			ps2.setInt(1, id);
			
			int t2 = ps2.executeUpdate();
			
			if(t1 > 0 && t2 > 0){
				MyConnector.getConnection().commit();	
			}
			else {
				MyConnector.getConnection().rollback();		
				System.err.println("Los cambios no se han guardado.");
			}
				

			MyConnector.getConnection().setAutoCommit(true);
			
		}
		catch(SQLException e) {
			throw new DAOException("Ha habido un error SQL", e);
		}
		finally {
			closePreparedStatement(ps);
			closePreparedStatement(ps2);
		}
		
	}

	@Override
	public Deportista encontrar(int id) throws DAOException, StatementException {
		
		Deportista d = null;
		PreparedStatement ps = null;
		
		try{
			 ps = miConexion.prepareStatement(query_SELECT);
			
			 ps.clearParameters();
			 ps.setInt(1, id);
			 			 
			 ResultSet rs = ps.executeQuery();
			 if (rs.next() == true) {
				 d = convertirResultSet(rs);
			 }
			 else {
				 throw new DAOException("Error al encontrar al deportista");
			 }
			
			 rs.close();
		} 
		catch (java.sql.SQLException e) {
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closePreparedStatement(ps);
		}
		
		return d;
	}

	@Override
	public void guardar(Deportista d) throws DAOException, StatementException {
		PreparedStatement psDeportista = null;
		PreparedStatement psDeportistaDisciplina = null;
		
		try {
			MyConnector.getConnection().setAutoCommit(false);
			
			psDeportista = miConexion.prepareStatement(query_INSERT_deportista, Statement.RETURN_GENERATED_KEYS);
			psDeportista.clearParameters();
			psDeportista.setString(1,d.getApellidos());
			psDeportista.setString(2,d.getNombres());
			psDeportista.setString(3,d.getEmail());
			psDeportista.setString(4,d.getTelefono());
			psDeportista.setInt(5,d.getIdPais());
	
			int cargaDeportista = psDeportista.executeUpdate();	
			
			ResultSet rs = psDeportista.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				d.setId(id);
			}
			else {
				// Agregar throw
				throw new DAOException("Error encontrando el nuevo ID del deportista.");
			}
	
			if(cargaDeportista > 0) {
					
				psDeportistaDisciplina = miConexion.prepareStatement(query_INSERT_deportista_disciplina);
				psDeportistaDisciplina.setInt(1, d.getId());
				psDeportistaDisciplina.setInt(2, d.getDisciplina().getId());
				int cargaEnDisciplina = psDeportistaDisciplina.executeUpdate();
					
			
				
				if(cargaEnDisciplina == 0) {
						MyConnector.getConnection().rollback();
						throw new DAOException("Fallo la carga en la tabla de disciplinas");
				}
				else
						MyConnector.getConnection().commit();
				
			}
			
			else {
				MyConnector.getConnection().rollback();
				throw new DAOException("Alguna de las operaciones de guardado.");
			}
			
			rs.close();
			
			
			MyConnector.getConnection().setAutoCommit(true);
		}
		catch(SQLException e) {
			throw new DAOException("Error de SQL: ", e);
		}
		finally {
			closePreparedStatement(psDeportista);
			closePreparedStatement(psDeportistaDisciplina);
		}
	}

	@Override
	public void actualizar(Deportista d) throws DAOException, StatementException {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		try {			
			ps = miConexion.prepareStatement(query_UPDATE);
			ps.clearParameters();
			ps.setString(1, d.getApellidos());
			ps.setString(2,d.getNombres());
			ps.setString(3, d.getEmail());
			ps.setString(4, d.getTelefono());
			ps.setInt(5, d.getIdPais());
			ps.setInt(6, d.getId());
			
			ps.executeUpdate();
			
			ps2 = miConexion.prepareStatement(query_UPDATE_inDisciplina);
			ps2.clearParameters();
			ps2.setInt(1, d.getDisciplina().getId());
			ps2.setInt(2, d.getId());
			
			ps2.executeUpdate();
		}
		catch(SQLException e) {
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closePreparedStatement(ps);
			closePreparedStatement(ps2);
		}
	}
	
	private Disciplina encontrarDisciplina(int idDeportista) throws DAOException, StatementException {
		Disciplina dis = null;
		Statement ps = null;
		try {
			 ps = miConexion.createStatement();
			 ResultSet rs2 = ps.executeQuery("SELECT id_disciplina FROM deportista_en_disciplina WHERE id_deportista=" + idDeportista);
			 
			 if(rs2.next()) {
				 dis = ManagerDAOjdbc.getDisciplinaDAO().encontrar(rs2.getInt(1));
			 }
			 else
				 return dis;
			 
			 rs2.close();
		}
		catch(SQLException e){
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closeStatement(ps);
		}
		
		return dis;
	}
	
	private Deportista convertirResultSet(ResultSet rs) throws DAOException, StatementException {
		 
		Deportista d;
		
		 try{
			 d = new Deportista(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			 d.setId(rs.getInt(1));
			 
			 Disciplina dis = encontrarDisciplina(d.getId());
			 d.setDisciplina(dis);
		 }
		 catch(SQLException e) {
			 throw new DAOException("Error de SQL", e);
			 
		 }
		 
		 return d;
	}
	
	public Deportista[] cargarEnArray() throws DAOException, StatementException {
		ArrayList<Deportista> deportistas = (ArrayList<Deportista>) this.cargar();
		
		Deportista[] array = new Deportista[deportistas.size()];
		for(int i = 0; i < deportistas.size(); i++) {
			array[i] = deportistas.get(i);
		}
		
		return array;
	}
	
	private void closeStatement(Statement st) throws StatementException {
		if(st != null) {
			try {
				st.close();
			}
			catch(SQLException e) {
				throw new StatementException("Error de SQL", e);
			}
		}
	}
	private void closePreparedStatement(PreparedStatement ps) throws StatementException {
		if(ps != null) {
			try {
				ps.close();
			}
			catch(SQLException e) {
				throw new StatementException("Error al cerrar el Statement o PreparedStatement", e);
			}
		}
	}
	
	
}
