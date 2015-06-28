package rolecarws.data.connection;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Configuracion
{
	  Properties properties = null;
	  
	    /** fichero de configuracion de la base de datos */
	    public final static String CONFIG_FILE_NAME = "jdbc.properties";
	    
	    public static Logger informe = Logger.getLogger(Configuracion.class.getName());
	 
	    private Configuracion()
	    {
	        this.properties = new Properties();
	        try
	        {
	            properties.load(Configuracion.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
	            //properties.load(new FileInputStream(CONFIG_FILE_NAME));//UTILIZAR ESTE SI EL FICHERO PROPERTIES ESTA FUERA DE LA APLICACION
	        }
	        catch (IOException ex)
	        {
	        	informe.error("Error: " + ex.getMessage().toString());
	            ex.printStackTrace();
	        }
	        catch (Exception ex)
	        {
	            informe.error("Error" + ex.getMessage().toString());
	        }
	    }//Configuration
	 
	    /**
	     * Implementando Singleton
	     *
	     * @return
	     */
	    public static Configuracion getInstance()
	    {
	        return ConfigurationHolder.INSTANCE;
	    }
	 
	    private static class ConfigurationHolder
	    {
	 
	        private static final Configuracion INSTANCE = new Configuracion();
	    }
	 
	    /**
	     * Retorna la propiedad de configuración solicitada
	     *
	     * @param key
	     * @return
	     */
	    public String getProperty(String key)
	    {
	        return this.properties.getProperty(key);
	    }//getProperty
}
