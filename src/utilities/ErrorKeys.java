package utilities;

import java.util.HashMap;
import java.util.Map;

// Clase publica que guarda String constantes de los posibles errores en textFields
public class ErrorKeys {	
	public final static String TEXTO_NOMBRE_SOLO_LETRAS_ERROR = "El nombre solo debe contener letras"; // ID = 1
	public final static String TEXTO_APELLIDO_SOLO_LETRAS_ERROR = "El apellido solo debe contener letras"; // ID = 2
	public final static String TEXTO_EMAIL_ARROBA_ERROR = "El e-mail debe contener el '@'"; // ID = 3
	public final static String TEXTO_TELEFONO_SOLO_NUMEROS_ERROR = "El telefono solo debe contener numeros"; // ID = 4
	public final static String TEXTO_VACIO_ERROR = "Todos los campos obligatorios";	 // ID = 5
	public final static String TEXTO_PAIS_REPETIDO_ERROR = "Pais ya registrado en la BD"; // ID = 6
	public final static String TEXTO_PAIS_IGUAL_ERROR = "El nombre del pais es igual al anterior"; // ID = 7
	
	public static Map<Integer, String> mapKeys;
	static {
	    mapKeys = new HashMap<>();
	    mapKeys.put(1, TEXTO_NOMBRE_SOLO_LETRAS_ERROR);
	    mapKeys.put(2, TEXTO_APELLIDO_SOLO_LETRAS_ERROR);	  
	    mapKeys.put(3, TEXTO_EMAIL_ARROBA_ERROR);	  
	    mapKeys.put(4, TEXTO_TELEFONO_SOLO_NUMEROS_ERROR);
	    mapKeys.put(5, TEXTO_VACIO_ERROR);
	    mapKeys.put(6, TEXTO_PAIS_REPETIDO_ERROR);
	    mapKeys.put(7, TEXTO_PAIS_IGUAL_ERROR);
	}
}