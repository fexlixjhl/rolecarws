package rolecarws.data.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;


public class ConexionBDatos
{	
    private static final String url = Configuracion.getInstance().getProperty("url");
	private static final String dbUsername = Configuracion.getInstance().getProperty("dbUsername");
	private static final String dbPassword = Configuracion.getInstance().getProperty("dbPassword");
	public static Logger informe = Logger.getLogger(ConexionBDatos.class.getName());
	
	public static Connection getConnection() 
	{
	    try
	    {
	    	informe.info("Conexión en producción");
	    	Connection conn = DriverManager.getConnection(url,dbUsername,dbPassword);
	    	informe.info("Después de conexión en producción");
	    	return conn;
		}
	    catch (SQLException e)
	    {
			informe.error("Error en producción: " + e.getMessage().toString());
			e.printStackTrace();
			return null;
		}
	}
	
	public static void limpieza(ResultSet rs,Statement stmt,Connection con)
	{
		try
		{
			if (rs != null) rs.close();
		}
		catch (Exception e)
		{
			informe.error("Error: " + e.getMessage().toString());
		}
		try
		{
			if (stmt != null)
				stmt.close();
		}
		catch (Exception e)
		{
			informe.error("Error: " + e.getMessage().toString());
		}
		try
		{
			if (con != null)
			{
				con.setAutoCommit(true);
				con.close();
			}
		}
		catch (Exception e)
		{
			informe.error("Error: " + e.getMessage().toString());
		}
	}
}