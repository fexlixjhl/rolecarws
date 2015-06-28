/**
 * 
 */
package rolecarws.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rolecarws.data.connection.ConexionBDatos;
import rolecarws.data.constantes.Atributos;
import rolecarws.maestros.dao.SchemaSing;

/**
 * @author FJHL
 *
 */
public class JdbcSchema {
	private static final String SELECT_SCHEMA = "select id, schema_on, schema_off from ";
	private static Log logger = LogFactory.getLog(JdbcSchema.class);
	private static String nombresch = Atributos.ESQUEMA;
	private static String nombretabla = Atributos.TABLASCHEMA;
	private static Connection conn = ConexionBDatos.getConnection();
	
	/**
	 * Get switch schema 
	 * @return Schema
	 */
	public static void getSchemas() {
		SchemaSing schema = SchemaSing.getInstance();
		try
    	{
    		String sql = SELECT_SCHEMA.concat(nombresch).concat(".").concat(nombretabla);
    		
            PreparedStatement pStm = conn.prepareStatement(sql);
            
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	if (rs.getInt(1) == 1){
            		schema.setOnLine(rs.getString("schema_on"));
            		schema.setOffLine(rs.getString("schema_off"));
            	}
            		
            }
		}
		catch(SQLException e){
			logger.error(e.getMessage());
			try {
				conn.close();
			} catch (SQLException eClose) {
				logger.error(eClose.getMessage());
			}
		}
        catch(Exception e){
        	logger.error(e.getMessage());
        	try {
				conn.close();
			} catch (SQLException eClose) {
				logger.error(eClose.getMessage());
			}
        }
    }
	
	
	

}
