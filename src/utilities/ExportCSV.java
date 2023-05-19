package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;

public class ExportCSV {
	
	private final static String SEPARADOR = ";";
	
	public static boolean exportar(JTable table){
		
		boolean exportadoCorrectamente = true;
		
		BufferedWriter out = null;
		try {
			// [Direc] guarda el directorio interno al Proyecto "Exports" como destino
			String direc = System.getProperty("user.dir");					
			direc = direc + "\\Entregable_3\\Exports\\outfile.csv";

			out = new BufferedWriter(new FileWriter(direc));
			String str = "Nombre y Apellido" + SEPARADOR + "Pais" + SEPARADOR + "Disciplina\r\n";

			// Genera el String con los datos de la tabla
			for (int i = 0; i < table.getRowCount(); i++) {
				for (int j = 0; j < table.getColumnCount() - 2; j++) {
					if (j != 0)
						str = str + SEPARADOR + table.getValueAt(i, j);
					else
						str = str + table.getValueAt(i, j);
				}
				str = str + "\r\n";
			}
			out.write(str);
			
		} 
		catch (IOException e) {
			
			System.err.println("¡Error en la exportacion!");
			exportadoCorrectamente = false;
			e.printStackTrace();
			
		}
		finally {
			try {
				if(out != null)
					out.close();	
			}
			catch(IOException e){
				System.err.println("¡Error en la exportacion!");
				exportadoCorrectamente = false;
				e.printStackTrace();
			}			
		}
		return exportadoCorrectamente;
	}
}
