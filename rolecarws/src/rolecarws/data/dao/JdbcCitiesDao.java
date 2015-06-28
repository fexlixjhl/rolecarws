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
import rolecarws.maestros.dao.City;
import rolecarws.maestros.dao.Country;
import rolecarws.maestros.dao.SchemaSing;

public class JdbcCitiesDao
{
	private static Log logger = LogFactory.getLog(JdbcCitiesDao.class);
	private static String nombresch = Atributos.ESQUEMA;
	private static String nombretabla = Atributos.TABLACITIES.concat(SchemaSing.getInstance().getOffLine());
	private static Hashtable<String,City> htcities = new Hashtable<String,City>();
	private static Connection conn = ConexionBDatos.getConnection();
	
	public static List<City> recogeCiudades(List<Country> pCountries) throws Exception
	{
		String request = "";
		List<City> aCities = new ArrayList<City>();
		//Vector<City> vcities = new Vector<City>();
		try
		{
			if (conn==null)
        	{
				conn = ConexionBDatos.getConnection();
        	}
			for (Country country : pCountries) {
				request = getRequestCities(country);
				if (request!=null && !request.equalsIgnoreCase(""))
				{
					NodeList listaciudades = getResponseCities(request);
					if (listaciudades!=null && listaciudades.getLength()>0)
					{
						insertaCiudades(listaciudades, country);
						//Para las pruebas
						String condicion = " order by codcity";
						aCities = getCities(condicion, country, aCities);
					}
				}
			}
//			for (int i=0;i<pCountries.size();i++)
//			{
//				Country country = pCountries.elementAt(i);
//				if (country.getCodcountry().equalsIgnoreCase("ES"))
//				{
//					request = getRequestCities(country);
//					if (request!=null && !request.equalsIgnoreCase(""))
//					{
//						NodeList listaciudades = getResponseCities(request);
//						if (listaciudades!=null && listaciudades.getLength()>0)
//						{
//							insertaCiudades(listaciudades, country);
//							//Para las pruebas
//							String condicion = " order by codcity";
//							vcities = getCities(condicion,country,vcities);
//						}
//					}
////				}
//			}
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return aCities;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return aCities;}
		return aCities;
	}
	
	/**
	 * CBC Construyo el request para enviar al WS
	 * @return
	 */
	public static String getRequestCities(Country country)
	{
		String request = "";
		try
		{
			request += Atributos.CABECERAXML;
			request +=		"<message>"
								+ "<serviceRequest serviceCode=\"getCities\">"
//								+ Atributos.LANGUAGE_ES
								+ "<serviceParameters>"
								+ "<brand code =\"EP\"/>"
								+ "<country countryCode =\""+country.getCodcountry()+"\"/>"
								+ "</serviceParameters>"
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
	public static NodeList getResponseCities(String texto)
    {
    	String responseStr = "";
    	NodeList listaciudades = null;
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
    		
    		if (!responseStr.contains("cityList"))
    		{
    			logger.error("No se ha podido obtener la información, error en el formato del xml.");
    		}
    		else
    		{
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    			DocumentBuilder db = dbf.newDocumentBuilder();
    			InputSource is = new InputSource();
    			is.setCharacterStream(new StringReader(responseStr));
    			Document doc = db.parse(is);
    			listaciudades = doc.getElementsByTagName("city");	
    		}
       }
       catch (Exception e)
       {
    	   return listaciudades;
       }
       return listaciudades;
    }
    
    /**
     * Inserto la lista de ciudades en base de datos
     * @param listaciudades
     * @param country
     * @throws Exception
     */
    public static boolean insertaCiudades(NodeList listaciudades, Country country)
    {
    	boolean result = false;
    	try
    	{
    		for (int i = 0; i < listaciudades.getLength() ; i ++) 
			{
				Element el = (Element) listaciudades.item(i);
				City city = new City();
				String codcity = el.getAttribute("cityCode").trim();
				city.setCodcity(codcity);
				city.setDescr(el.getAttribute("cityDescription").trim());
				city.setCountry(country);
				
				//Para las pruebas
//				if (city.getCodcity().equalsIgnoreCase("ALCOBENDAS"))
//				{
//					System.out.println("En pruebas, estoy en alcobendas.");
//				}
				
				//Relleno una hash con los códigos de pais para saber cuáles tengo que borrar
				htcities.put(codcity, city);
				String condicion = "";
				if (!existeCity(city,condicion))
				{
					
					result = insertCity(city);
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
     * Inserto la ciudad en la base de datos
     * @param city
     * @throws SQLException 
     * @throws Exception
     */
    public static boolean insertCity(City city) throws SQLException
    {
    	boolean result = false;
    	try
    	{
    		
    		String sql = "insert into ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " (codcity,descr,codcountry)";
			sql += " values (?,?,?)";
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, city.getCodcity());
            pStm.setString(2, city.getDescr());
            pStm.setString(3, city.getCountry().getCodcountry());
            
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
     * Obtengo la lista de ciudades en base de datos
     * @param condicion
     * @param aCities 
     * @throws SQLException 
     * @throws Exception
     */
    public static List<City> getCities(String condicion, Country country, List<City> aCities) throws SQLException
    {
    	//List<City> aCities = new ArrayList<City>();
    	
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcountry = '" + country.getCodcountry()+"'";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            ResultSet rs = pStm.executeQuery();
            while(rs!=null && rs.next())
            {
            	String codcity = rs.getString("codcity");
            	City city = new City();
            	city.setCodcity(codcity);
            	city.setIdcity(rs.getInt("idcity"));
            	city.setDescr(rs.getString("descr"));
            	city.setCountry(country);
            	//Control vaciado tablas
            	if (!htcities.isEmpty()){
	            	if (htcities.get(codcity)!=null)
	            	{
	            		aCities.add(city);
	            	}
	            	//Si no existe, no lo añado al vector de ciudades y lo borro de base de datos
	            	else
	            	{
	            		String cond = "";
	            		deleteCity(city,cond);
	            	}
            	}
            	else{
            		logger.error("Hash Cities vacia, no se han refrescado las tablas. Evita borrado.");
            	}
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return aCities;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return aCities;}
    	return aCities;
    }
    
    /**
     * Obtengo la ciudad en base a una condición
     * @throws SQLException 
     * @throws Exception
     */
	public static City getCity(String condicion) throws SQLException
    {
    	City city = null;
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	city = new City();
            	city.setIdcity(rs.getInt("idcity"));
            	city.setCodcity(rs.getString("codcity"));
            	city.setDescr(rs.getString("descr"));
            	String condcountry = " where codcountry = '" + rs.getString("codcountry")+"'";
            	Country country = JdbcCountriesDao.getCountry(condcountry,conn);
            	city.setCountry(country);
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return city;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return city;}
    	return city;
    }
	
	/**
     * Obtengo la ciudad en base al código de ciudad
     * @throws SQLException 
     * @throws Exception
     */
	public static City getCityByCodcity(String codcity) throws SQLException
    {
    	City city = null;
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcity = ?";
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, codcity);
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	city = new City();
            	city.setIdcity(rs.getInt("idcity"));
            	city.setCodcity(rs.getString("codcity"));
            	city.setDescr(rs.getString("descr"));
            	String condcountry = " where codcountry = '" + rs.getString("codcountry")+"'";
            	Country country = JdbcCountriesDao.getCountry(condcountry,conn);
            	city.setCountry(country);
            }
		}
		catch(SQLException e){logger.error(e.getMessage());conn.close();return city;}
        catch(Exception e){logger.error(e.getMessage());conn.close();return city;}
    	return city;
    }
    
    /**
     * Compruebo si existe la ciudad en base de datos
     * @param city
     * @param condicion
     * @throws SQLException 
     * @throws Exception
     */
    public static boolean existeCity(City city, String condicion) throws SQLException
    {
    	boolean result = false;
    	try
    	{
    		String sql = "select count(*) as numfilas from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcity = ? and codcountry=?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, city.getCodcity());
            pStm.setString(2, city.getCountry().getCodcountry());
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
     * Borro una ciudad
     * @param city
     * @param condicion
     * @throws SQLException 
     * @throws Exception
     */
    public static boolean deleteCity(City city, String condicion) throws SQLException
    {
    	boolean result = false;
    	try
    	{
    		String sql = "delete from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcity = ?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, city.getCodcity());
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