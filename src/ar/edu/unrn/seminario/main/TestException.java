package ar.edu.unrn.seminario.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import ar.edu.unrn.seminario.gui.Setup;

public class TestException {
	
	public static void main(String args[]) {
		
		try {
			//Persona persona = new Persona("", null, 12345678);
			
			//DatabaseConfig config = DatabaseConfig.getInstance("/Users/ddira/OneDrive/Desktop/config.xml");
			
			Properties defecto = new Properties();
			File archivoPropiedadesDefecto = new File("db.properties"),
					directorioSalida = new File(System.getProperty("user.dir") + "/config/"),
					archivoPropiedadesSalida = new File(System.getProperty("user.dir") + "/config/db.properties");
			
			FileInputStream entradaPropiedades = new FileInputStream(archivoPropiedadesDefecto);
			
			// Leo propiedades por defecto
			defecto.load(entradaPropiedades);
			
			// El directorio de salida no existe
			if(!directorioSalida.canWrite()) {
				directorioSalida.mkdir();
			}
			
			// El archivo de propiedades no existe
			if(!archivoPropiedadesSalida.canWrite()) {
				archivoPropiedadesSalida.createNewFile();
			}
			
			FileOutputStream salidaPropiedades = new FileOutputStream(archivoPropiedadesSalida);
			
			defecto.store(salidaPropiedades, "");
			
			
			
			//TODO Cargar properties
			
			// String puertoViejo = (String) defecto.setProperty("port", "3307");
			// Retorna el valor viejo
			
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		
	}

}
