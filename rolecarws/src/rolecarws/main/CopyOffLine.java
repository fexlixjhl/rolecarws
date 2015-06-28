package rolecarws.main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rolecarws.data.connection.ConexionBDatos;
import rolecarws.data.constantes.Atributos;
import rolecarws.data.dao.JdbcCountriesDao;
import rolecarws.maestros.dao.Country;
import rolecarws.maestros.dao.SchemaSing;

public class CopyOffLine {
	private static Log logger = LogFactory.getLog(JdbcCountriesDao.class);
	private static String nombresch = Atributos.ESQUEMA;
	private static String nombretabla = Atributos.TABLACOUNTRIES.concat(SchemaSing.getInstance().getOffLine());
	private static Hashtable<String, Country> htcountries = new Hashtable<String, Country>();
	private static Hashtable<String, String> existCountries = new Hashtable<String, String>();
	private static Connection conn = ConexionBDatos.getConnection();
	
	public static void runCopy(){
		boolean bReturn = false;
		try {
			CallableStatement copyOffLine = conn.prepareCall("{ call Copy_OffLine}");
			bReturn = copyOffLine.execute();
			//System.out.println("Retorna:: " + bReturn);
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				logger.error(ex.getMessage());
			}
			logger.error(e.getMessage());
		}
		finally {
			// cerrar la conexion
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
