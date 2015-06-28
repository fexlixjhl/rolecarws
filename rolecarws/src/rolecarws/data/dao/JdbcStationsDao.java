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
import rolecarws.maestros.dao.Station;
import rolecarws.maestros.dao.SchemaSing;

public class JdbcStationsDao
{
	private static Log logger = LogFactory.getLog(JdbcStationsDao.class);
	private static String nombresch = Atributos.ESQUEMA;
	private static String nombretabla = Atributos.TABLASTATIONS.concat(SchemaSing.getInstance().getOffLine());
	private static Hashtable<String,Station> htstations = new Hashtable<String,Station>();
	private static Connection conn = ConexionBDatos.getConnection();
	
	/**
	 * Recorro las ciudades para obtener las estaciones
	 * @param vcities
	 * @return
	 * @throws Exception
	 */
	public static List<Station> recogeEstaciones(List<City> pCities) throws Exception
	{
		String request = "";
		List<Station> aStations = new ArrayList<Station>();
		//Vector<Station> vstations = new Vector<Station>();
		int idprovincia = 0;
		try
		{
			if (conn==null)
        	{
				conn = ConexionBDatos.getConnection();
        	}
			for (City city : pCities) {
				request = getRequestStations(city);
				idprovincia = idprovincia + 1;
				if (request!=null && !request.equalsIgnoreCase(""))
				{
					NodeList listastations = getResponseStations(request);
					if (listastations!=null && listastations.getLength()>0)
					{
						insertaEstaciones(listastations, city, idprovincia);
						String condicion = " order by descr";
						aStations = getStations(condicion, city, aStations);
					}
				}
			} 
//			for (int i=0;i<vcities.size();i++)
//			{
//				City city = vcities.elementAt(i);
//				if (city.getCodcity().equalsIgnoreCase("CORDOBA"))
//				{
//					request = getRequestStations(city);
//					idprovincia = i + 1;
//					if (request!=null && !request.equalsIgnoreCase(""))
//					{
//						NodeList listastations = getResponseStations(request);
//						if (listastations!=null && listastations.getLength()>0)
//						{
//							insertaEstaciones(listastations, city, idprovincia);
//							String condicion = " order by descr";
//							vstations = getStations(condicion,city,vstations);
//						}
//					}
//				}
//			}
		}
		catch (Exception e)
		{
			if (conn!=null)
        	{
        		conn.close();conn=null;
        	}
			return aStations;
		}
		finally
        {
        	if (conn!=null)
        	{
        		conn.close();conn=null;
        	}
        }
		return aStations;
	}
	
	/**
	 * CBC Construyo el request para enviar al WS
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public static String getRequestStations(City city) throws SQLException
	{
		String request = "";
		try
		{
			request += Atributos.CABECERAXML;
			request +=		"<message>"
								+ "<serviceRequest serviceCode=\"getStations\">"
								+ Atributos.LANGUAGE_ES
								+ "<serviceParameters>"
								+ "<station countryCode =\""+city.getCountry().getCodcountry()
								+ "\" cityName=\""+city.getCodcity()
								+ "\" language=\"ES\"/>"
								+ "</serviceParameters>"
								+ "</serviceRequest>"
							+ "</message>";
    		request += "&callerCode=".concat(Atributos.USER).concat("&password=").concat(Atributos.PASS);
		}
		catch(Exception e){logger.error(e.getMessage());conn.close();return request;}
		return request;
	}
	
	/**
     * Obtengo la lista de nodos de estaciones
     * @param texto
     * @throws Exception
     */
	public static NodeList getResponseStations(String texto)
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
    		if (!responseStr.contains("stationList"))
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
    			listaciudades = doc.getElementsByTagName("station");
    		}
    		
       }
       catch (Exception e)
       {
    	   return listaciudades;
       }
       return listaciudades;
    }
    
    /**
     * Inserto la lista de estaciones en base de datos
     * @param liststations
     * @param city
     * @param numprovincia
     * @throws Exception
     */
    public static boolean insertaEstaciones(NodeList liststations, City city, int idprovincia)
    {
    	boolean result = false;
    	try
    	{
    		for (int i = 0; i < liststations.getLength() ; i ++) 
			{
				Element el = (Element) liststations.item(i);
				Station station = new Station();
				
				String codstation = el.getAttribute("stationCode").trim();
				station.setCodstation(codstation);
				String stationName = el.getAttribute("stationName").trim();
				station.setDescr(stationName);
				station.setCity(city);
				station.setIdprovincia(idprovincia);
				
				//Relleno una hash con los códigos de stacion para saber cuáles tengo que borrar
				htstations.put(codstation, station);
				String condicion = "";
				Station st = existeStation(station, condicion);
				if (st!=null)
				{
					//Actualizo sólo la provincia de la ciudad cuando ya existe
					result = updateProvinciaByCity(city,st.getIdprovincia(),Atributos.TABLACITIES);
				}
				else
				{
					result = insertStation(station);
					//Actualizo la provincia de la ciudad que no existe
					result = updateProvinciaByCity(city,idprovincia,Atributos.TABLACITIES);
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
     * Inserto la estación en la base de datos
     * @param station
     * @throws SQLException 
     * @throws Exception
     */
    public static boolean insertStation(Station station) throws Exception
    {
    	boolean result = false;
    	try
    	{
    		String sql = "insert into ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " (codstation,descr,codcity,idprovincia)";
			sql += " values (?,?,?,?)";
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, station.getCodstation());
            pStm.setString(2, station.getDescr());
            pStm.setString(3, station.getCity().getCodcity());
            pStm.setInt(4, station.getIdprovincia());
            
            int numfilas = pStm.executeUpdate();
            if (numfilas>0) 
            {
            	result = true;
            }
		}
        catch(Exception e)
        {	
        	logger.error(e.getMessage());
        	conn.close();
        	return result;
        }
    	return result;
    }
    
    /**
     * Obtengo la lista de estaciones en base de datos
     * @param condicion
     * @param aStations2 
     * @throws SQLException 
     * @throws Exception
     */
    public static List<Station> getStations(String condicion, City city, List<Station> aStations) throws Exception
    {
    	//List<Station> aStations = new ArrayList<Station>();
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codcity = ?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, city.getCodcity());
            ResultSet rs = pStm.executeQuery();
            while(rs!=null && rs.next())
            {
            	String codstation = rs.getString("codstation");
            	Station station = new Station();
            	station.setIdstation(rs.getInt("idstation"));
            	station.setCodstation(codstation);
            	station.setDescr(rs.getString("descr"));
            	station.setIdprovincia(rs.getInt("idprovincia"));
            	station.setCity(city);
            	//Control hash borrado de tablas
            	if (!htstations.isEmpty() || htstations != null){
	            	if (htstations.get(codstation)!=null)
	            	{
	            		aStations.add(station);
	            	}
	            	//Si no existe, no lo añado al vector de ciudades y lo borro de base de datos
	            	else
	            	{
	            		String cond = "";
	            		deleteStation(station,cond);
	            	}
	            }
            	else{
            		logger.error("Hash Stations vacia, no se han refrescado las tablas. Evita borrado.");
            	}
            }
		}
        catch(Exception e){logger.error(e.getMessage());conn.close();return aStations;}
    	return aStations;
    }
    
    /**
     * Compruebo si existe la estación en base de datos
     * @param station
     * @param condicion
     * @throws SQLException 
     * @throws Exception
     */
    public static Station existeStation(Station station, String condicion) throws Exception
    {
    	Station stationnew = null;
    	try
    	{
    		String sql = "select * from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codstation = ?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, station.getCodstation());
            ResultSet rs = pStm.executeQuery();
            if(rs!=null && rs.next())
            {
            	stationnew = new Station();
            	//TODO la siguiente linea ????????????????? la elimino
            	//stationnew.setIdprovincia(rs.getInt(1));
            	String codstation = rs.getString("codstation");
            	stationnew.setIdstation(rs.getInt("idstation"));
            	stationnew.setCodstation(codstation);
            	stationnew.setDescr(rs.getString("descr"));
            	stationnew.setIdprovincia(rs.getInt("idprovincia"));
            	String codcity = rs.getString("codcity");
            	//Obtengo la ciudad en base al código de ciudad
            	City city = JdbcCitiesDao.getCityByCodcity(codcity);
            	stationnew.setCity(city);
            }
		}
        catch(Exception e){logger.error(e.getMessage());conn.close();return stationnew;}
    	return stationnew;
    }
    
    /**
     * Borro una estación
     * @param station
     * @param condicion
     * @throws SQLException 
     * @throws Exception
     */
    public static boolean deleteStation(Station station, String condicion) throws Exception
    {
    	boolean result = false;
    	try
    	{
    		String sql = "delete from ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " where codstation = ?";
    		sql += condicion;
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setString(1, station.getCodstation());
            int numfilas = pStm.executeUpdate();
        	if (numfilas>0)
        	{
        		result = true;
        	}
		}
        catch(Exception e){logger.error(e.getMessage());conn.close();return result;}
    	return result;
    }
    
    /**
     * Actualizo la provincia para tenerla en cuenta de cara a las estaciones
     * @param city
     * @throws SQLException 
     * @throws Exception
     */
    public static boolean updateProvinciaByCity(City city, int idprovincia, String nombretabla) throws Exception
    {
    	boolean result = false;
    	try
    	{
    		String sql = "update ".concat(nombresch).concat(".").concat(nombretabla);
    		sql += " set idprovincia = ? where idcity = ?";
            PreparedStatement pStm= conn.prepareStatement(sql);
            pStm.setInt(1, idprovincia);
            pStm.setInt(2, city.getIdcity());
            
            int numfilas = pStm.executeUpdate();
            if (numfilas>0) 
            {
            	result = true;
            }
		}
        catch(Exception e){logger.error(e.getMessage());conn.close();return result;}
    	return result;
    }
}