package rolecarws.data.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import rolecarws.data.connection.ConexionBDatos;
import rolecarws.data.constantes.Atributos;
import rolecarws.maestros.dao.Country;
import rolecarws.maestros.dao.SchemaSing;

public class JdbcCountriesDao
{
	private static Log logger = LogFactory.getLog(JdbcCountriesDao.class);
	private static String nombresch = Atributos.ESQUEMA;
	private static String nombretabla = Atributos.TABLACOUNTRIES.concat(SchemaSing.getInstance().getOffLine());
	private static Hashtable<String, Country> htcountries = new Hashtable<String, Country>();
	private static Hashtable<String, String> existCountries = new Hashtable<String, String>();
	private static Connection conn = ConexionBDatos.getConnection();
	
	public static List<Country> recogePaises() throws Exception
	{
		getCountriesHash("");
		String request = "";
		List<Country> aCountries = new ArrayList<Country>();
		//Vector<Country> vcountries = new Vector<Country>();
		try
		{
			if (conn==null)
        	{
				conn = ConexionBDatos.getConnection();
        	}
			request = getRequestCountries();
			if (request!=null && !request.equalsIgnoreCase(""))
			{
				NodeList listapaises = getResponseCountries(request);
				if (listapaises!=null && listapaises.getLength()>0)
				{
					insertaPaises(listapaises);
					//Para las pruebas
//					String condicion = " where codcountry='ES' order by codcountry";
					
					String condicion = " order by codcountry";
					aCountries = getCountries(condicion);
				}
			}
		}
		catch (Exception e)
		{
			return aCountries;
		}
		finally
        {
        	if (conn!=null)
        	{
        		conn.close();conn=null;
        	}
        }
		return aCountries;
	}
	
	/**
	 * CBC Construyo el request para enviar al WS
	 * @return
	 */
	public static String getRequestCountries()
	{
		String request = "";
		try
		{
			request += Atributos.CABECERAXML;
			request +=		"<message>"
								+ "<serviceRequest serviceCode=\"getCountries\">"
								+ Atributos.LANGUAGE_ES
								+ "<serviceParameters><brand code =\"EP\"/></serviceParameters>"
								+ "</serviceRequest>"
							+ "</message>";
    		request += "&callerCode=".concat(Atributos.USER).concat("&password=").concat(Atributos.PASS);
		}
		catch (Exception e)
		{
			return request;
		}
		return request;
	}
	
	/**
     * Obtengo la lista de nodos de paises
     * @param texto
     * @throws Exception
     */
	public static NodeList getResponseCountries(String texto)
    {
    	String responseStr = "";
    	NodeList listapaises = null;
    	try
    	{
    		URL obj = new URL(Atributos.PUSH_URL_AUTH);
    		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
           
    		//add request header
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;CHARSET=UTF-8");
    		con.setRequestProperty("Accept-Charset", "UTF-8"); 

    		//Send post request
    		con.setDoOutput(true);
    		con.setDoInput(true);
    		
    		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    		wr.writeBytes(texto);
    		wr.flush();
    		wr.close();
           
    		//int responseCode = con.getResponseCode();
    		//System.out.println("Response Code : " + responseCode);

    		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
    		String inputLine;
    		StringBuilder response = new StringBuilder();
   
    		while ((inputLine = in.readLine()) != null)
    		{
    			inputLine = java.net.URLDecoder.decode(inputLine, "UTF-8");
    			response.append(inputLine);
    		}
    		in.close();
    		responseStr = response.toString();

    		if (!responseStr.contains("countryList"))
    		{
    			logger.error("No se ha podido obtener la información, error en el formato del xml.");
    		}
    		else
    		{
    			logger.info("Obtengo el listado de países.");
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    			DocumentBuilder db = dbf.newDocumentBuilder();
    			InputSource is = new InputSource();
    			is.setCharacterStream(new StringReader(responseStr));
    			Document doc = db.parse(is);
    			listapaises = doc.getElementsByTagName("country");	
    		}
       }
       catch (Exception e)
       {
    	   return listapaises;
       }
       return listapaises;
    }
    
    /**
     * Inserto la lista de paises en base de datos
     * @param listpaises
     * @throws Exception
     */
	public static boolean insertaPaises(NodeList listapaises)
    {
    	boolean result = false;
    	try
    	{
    		for (int i = 0; i < listapaises.getLength() ; i ++) 
			{
				Element el = (Element) listapaises.item(i);
				Country country = new Country();
				String codcountry = el.getAttribute("countryCode").trim();
				country.setCodcountry(codcountry);
				country.setDescr(el.getAttribute("countryDescription").trim());
				//Relleno una hash con los códigos de pais para saber cuáles tengo que borrar
				htcountries.put(codcountry, country);
				String condicion = "";
				if (!existeCountry(country,condicion))
				{
					
					result = insertCountry(country);
				}
			}
    	}
    	catch (Exception e)
    	{
    		return result;
    	}
    	return result;
    }
    
    /**
     * Inserto el país en la base de datos
     * @param country
     * @throws SQLException 
     * @throws Exception
     */
	public static boolean insertCountry(Country country) throws SQLException
    {
    	
    	boolean result = false;
    	try
    	{
    		String sql = "insert into ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " (codcountry,descr)";
			sql += " values (?,?)";
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, country.getCodcountry());
            pStm.setString(2, country.getDescr());
            
            int numfilas = pStm.executeUpdate();
            if (numfilas>0) 
            {
            	result = true;
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return result;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return result;}
	
    	return result;
    }
    
    /**
     * Obtengo la lista de paises en base de datos
     * @throws SQLException 
     * @throws Exception
     */
	public static List<Country> getCountries(String condicion) throws SQLException
    {
    	
    	List<Country> aCountries = new ArrayList<Country>();
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            ResultSet rs = pStm.executeQuery();
            while(rs!=null && rs.next())
            {
            	String codcountry = rs.getString("codcountry");
            	Country country = new Country();
            	country.setIdcountry(rs.getInt("idcountry"));
            	country.setCodcountry(codcountry);
            	country.setDescr(rs.getString("descr"));
            	//Control borrado accidental de tablas
            	if (!htcountries.isEmpty()){
            		if (htcountries.get(codcountry)!=null)
            		{
            			aCountries.add(country);
            		}
            		//Si no existe, no lo añado al vector de paises y lo borro de base de datos
            		else
            		{
            			String cond = "";
            			deleteCountry(country,cond);
            		}
            	}
            	else{
            		logger.error("Hash Countries vacia, no se han refrescado las tablas. Evita borrado.");
            	}
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return aCountries;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return aCountries;}
    	return aCountries;
    }
    
    /**
     * Obtengo el país en base a una condición
     * @throws SQLException 
     * @throws Exception
     */
	public static Country getCountry(String condicion, Connection conn) throws SQLException
    {
    	Country country = null;
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	country = new Country();
            	country.setIdcountry(rs.getInt("idcountry"));
            	String codcountry = rs.getString("codcountry");
            	country.setCodcountry(codcountry);
            	country.setDescr(rs.getString("descr"));
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return country;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return country;}
    	return country;
    }
    
    /**
     * Obtengo la lista de paises en base de datos
     * @param country
     * @throws SQLException 
     * @throws Exception
     */
	public static boolean existeCountry(Country country, String condicion) throws SQLException
    {
    	boolean result = false;
    	try
    	{
    		String sql = "select count(*) as numfilas from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcountry = ?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, country.getCodcountry());
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	int numfilas = rs.getInt("numfilas");
            	if (numfilas>0)
            	{
            		result = true;
            	}
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return result;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return result;}
    	return result;
    }
	
	   /**
     * Obtengo la lista completa de paises en base de datos para la hash
     * 
     * @throws SQLException 
     * @throws Exception
     */
	public static boolean getCountriesHash(String pCondicion) throws SQLException
    {
    	boolean result = false;
    	try
    	{
    		String sql = "select codcountry, idcountry from ".concat(nombresch).concat(".").concat(nombretabla);
    		if (!"".equals(pCondicion.trim())){
    			sql.concat(pCondicion);
    		}
    		
            PreparedStatement pStm= conn.prepareStatement(sql);
            
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	existCountries.put(rs.getString("codcountry"), rs.getString("idcountry"));
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return result;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return result;}
    	return result;
    }
    
    /**
     * Borro un país
     * @param country
     * @throws SQLException 
     * @throws Exception
     */
	public static boolean deleteCountry(Country country, String condicion) throws SQLException
    {
    	
    	boolean result = false;
    	try
    	{
    		String sql = "delete from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcountry = ?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, country.getCodcountry());
            int numfilas = pStm.executeUpdate();
        	if (numfilas>0)
        	{
        		result = true;
        	}
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return result;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return result;}
    	return result;
    }
}