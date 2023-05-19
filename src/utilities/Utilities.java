package utilities;

public class Utilities {
	
	public static String toUpperCaseEachWord(String cadena) {
		
		char[] caracteres = cadena.toLowerCase().toCharArray();
		caracteres[0] = Character.toUpperCase(caracteres[0]);
		
		for (int i = 0; i < cadena.length()- 2; i++) 
		    if (caracteres[i] == ' ')
		      caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
		
		return new String(caracteres);
	}	
	
	public static String[][] orderByName(String[][] matriz){
		
		for( int i=0; i < matriz.length; i++){
			for( int j=0;j< matriz[i].length; j++){
				for(int x=0; x < matriz.length; x++){
					if(matriz[i][1].compareTo(matriz[x][1]) < 0){
						String[] t = matriz[i];
						matriz[i] = matriz[x];
						matriz[x] = t;
					}
					
				}
			} 
		}	
		
		
		return matriz;
		
	}
}
