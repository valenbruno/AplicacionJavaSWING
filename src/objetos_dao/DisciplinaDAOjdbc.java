package objetos_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connector.MyConnector;
import datos_tokio2021.Disciplina;
import excepciones.DAOException;
import excepciones.StatementException;
import interfaces_dao.DisciplinaDAO;

public class DisciplinaDAOjdbc implements DisciplinaDAO{
	
	private Connection miConexion;
		
	private final String query_UPDATE = "UPDATE disciplina SET nombre=? WHERE id=?";
	private final String query_DELETE_withID = "DELETE * FROM disciplina WHERE id=?";
	//private final String query_DELETE = "DELETE * FROM disciplina WHERE nombre=?";
	private final String query_INSERT = "INSERT INTO `disciplina` (nombre) VALUES (?)";
	private final String query_SELECT = "SELECT * FROM disciplina WHERE id=?";
	private final String query_SELECT_withName = "SELECT * FROM disciplina WHERE nombre=?";
	private final String query_SELECT_ALL = "SELECT * FROM disciplina";
	
	public DisciplinaDAOjdbc() {
		miConexion = MyConnector.getConnection();
	}

	@Override
	public List<Disciplina> cargar() throws DAOException, StatementException {
		Statement st = null;
		
		List<Disciplina> disciplinas = null;
		
		try {		

			st = miConexion.createStatement();
			
			disciplinas = new ArrayList<Disciplina>();
			
			ResultSet result = st.executeQuery(query_SELECT_ALL);
			
			while(result.next()) {
				disciplinas.add(convertirResultSet(result));
			}

		}
		catch(SQLException e) {
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closeStatement(st);
		}
		
		
		return disciplinas;
	}

	@Override
	public void eliminar(Disciplina d) throws DAOException, StatementException {
		PreparedStatement ps = null;
		
		try {		
			ps = miConexion.prepareStatement(query_DELETE_withID);
				
			ps.clearParameters();
			ps.setInt(1, d.getId());
		
			
			ps.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closePreparedStatement(ps);
		}

	}

	@Override
	public Disciplina encontrar(int id) throws DAOException, StatementException {
		Disciplina d = null;
		PreparedStatement ps = null;
		
		try{
			 ps = miConexion.prepareStatement(query_SELECT);
			 
			 ps.clearParameters();
			 ps.setInt(1, id);
			 
			 ResultSet rs = ps.executeQuery();
			 if (rs.next() == true) {
				 d = convertirResultSet(rs);
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
	
	public Disciplina encontrar(String nombre) throws DAOException, StatementException {
		Disciplina d = null;
		PreparedStatement ps = null;
		
		try{
			 ps = miConexion.prepareStatement(query_SELECT_withName);
			 
			 ps.clearParameters();
			 ps.setString(1, nombre);
			 
			 ResultSet rs = ps.executeQuery();
			 if (rs.next() == true) {
				 d = convertirResultSet(rs);
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
	public void guardar(Disciplina d) throws DAOException, StatementException {
		PreparedStatement ps = null;
		
		try {
			ps = miConexion.prepareStatement(query_INSERT, Statement.RETURN_GENERATED_KEYS);
			ps.clearParameters();
			ps.setString(1,d.getNombre());
	
			ps.executeUpdate();	
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				int id = rs.getInt(1);
				d.setId(id);
			}
			else {
				// Agregar throw
			}
			
			
			rs.close();
		}
		catch(SQLException e) {
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closePreparedStatement(ps);
		}

		
	}

	@Override
	public void actualizar(Disciplina d) throws DAOException, StatementException {
		PreparedStatement ps = null;
		
		try {			
			ps = miConexion.prepareStatement(query_UPDATE);
			ps.clearParameters();
			ps.setString(1, d.getNombre());
			ps.setInt(2, d.getId());
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DAOException("Error de SQL", e);
		}
		finally {
			closePreparedStatement(ps);
		}
		
	}

	private Disciplina convertirResultSet(ResultSet rs) throws DAOException {
		 
		Disciplina d = null;
		
		 try{
			 d = new Disciplina(rs.getString(2));
			 d.setId(rs.getInt(1));
		 }
		 catch(SQLException e) {
			 throw new DAOException("Error de SQL", e);
		 }
		 
		 return d;
	}
	
	public String[] cargarEnArray() throws DAOException, StatementException {
		ArrayList<Disciplina> discplinas = (ArrayList<Disciplina>) this.cargar();
		
		String[] array = new String[discplinas.size()];
		for(int i = 0; i < discplinas.size(); i++) {
			array[i] = discplinas.get(i).getNombre();
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
