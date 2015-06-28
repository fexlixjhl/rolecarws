package rolecarws.main;

import org.apache.log4j.Logger;
public class Rolecarws
{
	public static Logger informe = Logger.getLogger(Rolecarws.class.getName());
	public static void main(String[] args)
    {
		try
		{
//		  while(true)
//		  {
			  Thread hilonotificacion = new Thread(new Rolecarwsrunnable());
			  hilonotificacion.start();
//			  Thread.sleep(10000);
			  hilonotificacion.interrupt();
			  System.gc();
//		  }
		}
        catch(Exception ex)
        {
        	informe.error("Error en Inicialización: " + ex.getMessage().toString());
        }
    }
}