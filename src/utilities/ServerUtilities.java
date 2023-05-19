package utilities;


import java.util.ArrayList;

import datos_tokio2021.Deportista;
import datos_tokio2021.Pais;
import excepciones.DAOException;
import excepciones.StatementException;
import objetos_dao.ManagerDAOjdbc;
import ui.PaisFrame;

public class ServerUtilities {
	
	private final static int numeroDatos = 7; // Numero de Datos por Deportista en la BD
	
	public static String[][] arrayDeportistas(){
			
		Deportista[] arrayDepor = null;
		try {
			arrayDepor = ManagerDAOjdbc.getDeportistaDAO().cargarEnArray();
		}
		catch(StatementException e1) {
			e1.printStackTrace();
		}
		catch(DAOException e2) {
			e2.printStackTrace();
		}

		
		int numeroDeportistas = arrayDepor.length; // Se setea desde la BD

		String[][] rawData = new String[numeroDeportistas][numeroDatos];
		for (int i = 0; i < numeroDeportistas; i++) {
			for (int j = 0; j < numeroDatos; j++) {
				switch (j) {
				case 0:
					rawData[i][j] = String.valueOf(arrayDepor[i].getId());
					break;
				case 1:
					rawData[i][j] = arrayDepor[i].getApellidos();
					break;
				case 2:
					rawData[i][j] = arrayDepor[i].getNombres();
					break;
				case 3:
					rawData[i][j] = arrayDepor[i].getEmail();
					break;
				case 4:
					rawData[i][j] = arrayDepor[i].getTelefono();
					break;
				case 5:
					rawData[i][j] = arrayDepor[i].getDisciplina().getNombre();
					break;
				case 6:
					Pais p = null;
					try {
						p = ManagerDAOjdbc.getPaisDAO().encontrar(arrayDepor[i].getIdPais());
					}
					catch(StatementException e1) {
						e1.printStackTrace();
					}
					catch(DAOException e2) {
						e2.printStackTrace();
					}
					rawData[i][j] = p.getNombre();
					break;
				}
			}
		}	

		return rawData;
	}
	
	public static String[][] convertirAFilas(String[][] rawData){
		
		final int numeroColumnas = 5;
		int numeroDeportistas = rawData.length;
		
		String[][] filas = new String[numeroDeportistas][numeroColumnas];

		for (int i = 0; i < numeroDeportistas; i++) {
			for (int j = 0; j < numeroColumnas; j++) {
				switch (j) {
				case 0:
					filas[i][j] = rawData[i][1] + " " + rawData[i][2];
					break;
				case 1:
					filas[i][j] = rawData[i][4];
					break;
				case 2:
					filas[i][j] = rawData[i][5];
					break;
				default:
					filas[i][j] = null;
					break;
				}
			}
		}
		
		return filas;
	}
	
	public static String[][] agregarFila(String[][] rawData, Deportista deportista) throws DAOException{
		String[][] newRawData = new String[rawData.length + 1][numeroDatos];
		
		for(int i = 0; i < rawData.length; i++) 
			for(int j = 0; j < numeroDatos; j++) 
				newRawData[i][j] = rawData[i][j];
			
		
		newRawData[newRawData.length - 1][0] = String.valueOf(deportista.getId());
		newRawData[newRawData.length - 1][1] = deportista.getApellidos();
		newRawData[newRawData.length - 1][2] = deportista.getNombres();
		newRawData[newRawData.length - 1][3] = deportista.getEmail();
		newRawData[newRawData.length - 1][4] = deportista.getTelefono();
		newRawData[newRawData.length - 1][5] = deportista.getDisciplina().getNombre();
		
		Pais p = null;
		try {
			p = ManagerDAOjdbc.getPaisDAO().encontrar(deportista.getIdPais());
		}
		catch(StatementException e1) {
			e1.printStackTrace();
		}
		catch(DAOException e2) {
			e2.printStackTrace();
		}
		
		newRawData[newRawData.length - 1][6] = p.getNombre();
		
		return newRawData;
	}
	
	public static String[][] eliminarFila(String depor[][], int fila) {

		String[][] aux = new String[depor.length - 1][depor[0].length];
		int p = 0;
		for (int i = 0; i < depor.length; ++i) {
			if (i == fila)
				continue;
			int q = 0;
			for (int j = 0; j < depor[0].length; ++j) {
				aux[p][q] = depor[i][j];
				++q;
			}
			++p;
		}
		return aux;
	}


	// Comprueba que se hallan ingresado correctamente los datos en los textFiels
	// del Deportista
	/*
	 * Retorna numero entero de error: 
	 * -0: No hubo ningun error 
	 * -1: Error en el Nombre
	 * -2: Error en el Apellido 
	 * -3: Error en el E-mail 
	 * -4: Error en el Telefono	 
	 */
	public static int checkNuevoDeportista(String nombre, String apellido, String email, String telefono) {
		if (!nombre.matches("^[ A-Za-z]+$"))
			return 1;
		else if (!apellido.matches("^[ A-Za-z]+$"))
			return 2;
		else if (email.indexOf('@') == -1)
			return 3;
		else if (!telefono.matches("[0-9]+"))
			return 4;
		else
			return 0;
	}

	// Comprueba que se hallan ingresado correctamente los datos en el textFiel del
	// Pais
	/*
	 * Retorna numero entero de error: -0: No hubo ningun error. 
	 * -1: Error en el Nombre del Pais
	 * -6: Error Pais ya registrado
	 * -7: Error nombre pais igual al anterior
	 */
	public static int checkNuevoPais(String nombre) throws DAOException, StatementException {
		if (!nombre.matches("^[ A-Za-z ñ Ñ]+$"))
			return 1;
		else {
			ArrayList<Pais> paisesEnBD = (ArrayList<Pais>) ManagerDAOjdbc.getPaisDAO().cargar();
			boolean repetido = false;
			
			for(int i = 0; i < paisesEnBD.size() && !repetido; i++) {	
				
				Pais p = paisesEnBD.get(i);
				repetido = nombre.toLowerCase().equals(p.getNombre().toLowerCase());
				
			}
			
			if(repetido) {
				if(!nombre.equals(PaisFrame.paisAEditar))
					return 6;
				else
					return 7;
			}
			else
				return 0;
		}

		
	}
	
	public static String[][] convertirListaPais() throws DAOException, StatementException {
		
		ArrayList<Pais> paises = (ArrayList<Pais>) ManagerDAOjdbc.getPaisDAO().cargar();
		
		String[][] arrayPaises = new String[paises.size()][4];
		
		Pais p;
		for(int i = 0; i < paises.size(); i++) {
			p = paises.get(i);
			arrayPaises[i][0] = String.valueOf(p.getId());
			arrayPaises[i][1] = p.getNombre();
			arrayPaises[i][2] = null;
			arrayPaises[i][3] = null;
		}
		
		return arrayPaises;
	}
	
	public static String[][] agregarFilaPais(String[][] rawData, Pais p){
		String[][] newRawData = new String[rawData.length + 1][4];
		
		for(int i = 0; i < rawData.length; i++) 
			for(int j = 0; j < 4; j++) 
				newRawData[i][j] = rawData[i][j];
			
		
		newRawData[newRawData.length - 1][0] = String.valueOf(p.getId());
		newRawData[newRawData.length - 1][1] = p.getNombre();
		newRawData[newRawData.length - 1][2] = null;
		newRawData[newRawData.length - 1][3] = null;
		
		newRawData = Utilities.orderByName(newRawData);
		
		return newRawData;
	}

	public static boolean validarPaisEnUso(Pais p) throws DAOException, StatementException {
		
		ArrayList<Deportista> deportistas = (ArrayList<Deportista>) ManagerDAOjdbc.getDeportistaDAO().cargar();
		
		boolean enUso = false;
		
		for(int i = 0; i < deportistas.size() && !enUso; i++) {
			Deportista d = deportistas.get(i);
			enUso = (d.getIdPais() == p.getId());
		}
		
		return enUso;
		
	}
}