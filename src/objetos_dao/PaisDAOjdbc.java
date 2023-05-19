package objetos_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connector.MyConnector;
import datos_tokio2021.Pais;
import excepciones.DAOException;
import excepciones.StatementException;
import interfaces_dao.PaisDAO;

public class PaisDAOjdbc implements PaisDAO {
	
	private Connection miConexion;
	
	private final String query_UPDATE = "UPDATE pais SET nombre=? WHERE id=?";
	private final String query_DELETE_withID = "DELETE FROM pais WHERE id=?";
	//private final String query_DELETE = "DELETE * FROM pais WHERE nombre=?";
	private final String query_INSERT = "INSERT INTO `pais` (nombre) VALUES (?)";
	private final String query_SELECT = "SELECT * FROM pais WHERE id=?";
	private final String query_SELECT_withName = "SELECT * FROM pais WHERE nombre=?";
	private final String query_SELECT_ALL = "SELECT * FROM pais ORDER BY nombre ASC";
	
	public PaisDAOjdbc() {
		miConexion = MyConnector.getConnection();
	}

	@Override
	public List<Pais> cargar() throws DAOException, StatementException {
		Statement st = null;
		
		List<Pais> paises = null;
		
		try {		

			st = miConexion.createStatement();
			
			paises = new ArrayList<Pais>();
			
			ResultSet result = st.executeQuery(query_SELECT_ALL);
			
			while(result.next()) {
				paises.add(convertirResultSet(result));
			}
		}
		catch(SQLException e) {
			throw new DAOException("Error SQL", e);
		}
		finally {
			closeStatement(st);
		}
		
		
		return paises;
	}

	@Override
	public void eliminar(Pais d) throws DAOException, StatementException {

		PreparedStatement ps = null;
		
		try {		
			
			ps = miConexion.prepareStatement(query_DELETE_withID);
				
			ps.clearParameters();
			ps.setInt(1, d.getId());
			
			
			ps.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DAOException("No se pudo conectar a la base de datos." + e.getMessage() + e.getStackTrace());
		}
		finally {
			closePreparedStatement(ps);
		}

	}

	@Override
	public Pais encontrar(int id) throws DAOException, StatementException {
		Pais d = null;
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
			throw new DAOException("Error de SQL: " + e.getMessage());
		}
		finally {
			closePreparedStatement(ps);
		}
		
		return d;
	}
	
	public Pais encontrar(String nombre) throws DAOException, StatementException {
		Pais d = null;
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
	public void guardar(Pais d) throws DAOException, StatementException {
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
	public void actualizar(Pais d) throws DAOException, StatementException {
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

	private Pais convertirResultSet(ResultSet rs) throws DAOException {
		 
		Pais d = null;
		
		 try{
			 d = new Pais(rs.getString(2));
			 d.setId(rs.getInt(1));
		 }
		 catch(SQLException e) {
			 throw new DAOException("Error de SQL", e);
		 }
		 
		 return d;
	}
	
	public String[] cargarEnArray() throws DAOException, StatementException {
		ArrayList<Pais> paises = (ArrayList<Pais>) this.cargar();
		
		String[] array = new String[paises.size()];
		for(int i = 0; i < paises.size(); i++) {
			array[i] = paises.get(i).getNombre();
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
