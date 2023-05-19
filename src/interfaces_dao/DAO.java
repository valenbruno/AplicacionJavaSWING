package interfaces_dao;

import java.util.List;

import excepciones.DAOException;
import excepciones.StatementException;

public interface DAO<T> {
	public List<T> cargar() throws DAOException, StatementException;
	public void eliminar(T d) throws DAOException, StatementException;
	public T encontrar(int id) throws DAOException, StatementException;
	public void guardar(T d) throws DAOException, StatementException;
	public void actualizar(T d) throws DAOException, StatementException;
}
