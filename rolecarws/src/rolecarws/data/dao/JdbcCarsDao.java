package rolecarws.data.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Collections;
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

import rolecarws.data.constantes.Atributos;
import rolecarws.maestros.dao.Car;
import rolecarws.maestros.dao.Insurance;
import rolecarws.maestros.dao.Quote;
import rolecarws.maestros.dao.Reservation;
import rolecarws.maestros.dao.Station;
import rolecarws.maestros.dao.Tax;
import rolecarws.utils.CarComparator;


public class JdbcCarsDao
{
	private static Log logger = LogFactory.getLog(JdbcCarsDao.class);
	private static String carTypeG = "";
	
	/**
	 * Obtengo todos los vehículos en base a los parámetros seleccionados
	 * fecha de recogida, hora de recogida, lugar de recogida
	 * fecha de devolución, hora de devolución, lugar de devolución
	 * @param vcars
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Vector<Car> recogeVehiculos(String checkinstationid, String checkindate, String checkintime, String checkoutstationid, String checkoutdate, String checkouttime, String carType, int orden) throws Exception
	{
		String request = "";
		Vector<Car> vcars = new Vector<Car>();
		try
		{
			Reservation reservation = new Reservation();
			reservation.setCheckinstationId(checkinstationid);
			reservation.setCheckindate(checkindate);
			reservation.setCheckintime(checkintime);
			reservation.setCheckoutstationId(checkoutstationid);
			reservation.setCheckoutdate(checkoutdate);
			reservation.setCheckouttime(checkouttime);
			carTypeG =carType;
			request = getRequestCars(reservation, carType);
			if (request!=null && !request.equalsIgnoreCase(""))
			{
				vcars = getResponseCars(request,reservation);
			}

			Collections.sort(vcars,new CarComparator(orden));
		}
		catch (Exception e)
		{
			return vcars;
		}
		return vcars;
	}
	
	/**
	 * CBC Construyo el request de vehículos para enviar al WS
	 * 
	 * @return
	 */
	public static String getRequestCars(Reservation reservation, String carType)
	{
		String request = "";
		try
		{
			request += Atributos.CABECERAXML;
			//Se recibe el tipo de coche
			if (carType!=null && !carType.equalsIgnoreCase(""))
			{
				request += "<message>"
							+ "<serviceRequest serviceCode=\"getCarCategories\">"
							+ Atributos.LANGUAGE_ES
								+ "<serviceParameters>"
									+ "<reservation>"
										+ "<checkout stationID=\""+reservation.getCheckoutstationId()+"\" date=\""+reservation.getCheckoutdate()+"\" time=\""+reservation.getCheckouttime()+"\" language=\"ES\"/>"
										+ "<checkin stationID=\""+reservation.getCheckinstationId()+"\" date=\""+reservation.getCheckindate()+"\" time=\""+reservation.getCheckintime()+"\" language=\"ES\"/>"
									+ "</reservation>"
								+ "</serviceParameters>"
							+ "</serviceRequest>"
						+ "</message>";
			}
			//No se recibe tipo de coche
			else
			{
				request += "<message>"
							+ "<serviceRequest serviceCode=\"getCarCategories\">"
								+ "<serviceParameters>"
									+ "<reservation>"
										+ "<checkout stationID=\""+reservation.getCheckoutstationId()+"\" date=\""+reservation.getCheckoutdate()+"\" time=\""+reservation.getCheckouttime()+"\" language=\"ES\"/>"
										+ "<checkin stationID=\""+reservation.getCheckinstationId()+"\" date=\""+reservation.getCheckindate()+"\" time=\""+reservation.getCheckintime()+"\" language=\"ES\"/>"
							+ "</reservation>"
									+ "</serviceParameters>"
								+ "</serviceRequest>"
						+ "</message>";
			}
			
    		request += "&callerCode=".concat(Atributos.USER).concat("&password=").concat(Atributos.PASS);
		}
		catch (Exception e)
		{
			return request;
		}
		return request;
	}
	
	/**
     * Obtengo la lista de nodos de coches
     * @param texto
     * @throws Exception
     */
	public static Vector<Car> getResponseCars(String texto, Reservation reservation)
    {
    	String responseStr = "";
    	Vector<Car> vcars = new Vector<Car>();
    	try
    	{
    		URL obj = new URL(Atributos.PUSH_URL_AUTH);
    		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
           
    		//add request header
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    		//Send post request
    		con.setDoOutput(true);
    		con.setDoInput(true);
    		
    		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    		wr.writeBytes(texto);
    		wr.flush();
    		wr.close();

    		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		StringBuilder response = new StringBuilder();
   
    		while ((inputLine = in.readLine()) != null)
    		{
    			response.append(inputLine);
    		}
    		in.close();
    		responseStr = response.toString();
    		if (!responseStr.contains("carCategoryList"))
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
    			NodeList listcars = doc.getElementsByTagName("carCategory");
    			vcars = recogeVehiculos(listcars, reservation);
    		}
       }
       catch (Exception e)
       {
    	   return vcars;
       }
       return vcars;
    }
    
    /**
     * Inserto el listado de vehículos en base de datos
     * @param listcars
     * @throws Exception
     */
    public static Vector<Car> recogeVehiculos(NodeList listcars, Reservation reservation)
    {
    	Vector<Car> vcars = new Vector<Car>();
    	try
    	{
    		for (int i = 0; i < listcars.getLength() ; i ++) 
			{
				Element el = (Element) listcars.item(i);
				Car car = new Car();
				car.setCarCategoryAirCond(el.getAttribute("carCategoryAirCond"));
				car.setCarCategoryAutomatic(el.getAttribute("carCategoryAutomatic"));
				car.setCarCategoryCO2Quantity(Integer.parseInt(el.getAttribute("carCategoryCO2Quantity")));
				car.setCarCategoryCode(el.getAttribute("carCategoryCode"));
				car.setCarCategoryDoors(el.getAttribute("carCategoryDoors"));
				car.setCarCategoryName(el.getAttribute("carCategoryName"));
				car.setCarCategorySample(el.getAttribute("carCategorySample"));
				car.setCarCategorySeats(el.getAttribute("carCategorySeats"));
				car.setCarCategoryStatusCode(el.getAttribute("carCategoryStatusCode"));
				car.setCarCategoryType(el.getAttribute("carCategoryType"));
				car.setCarType(el.getAttribute("carType"));
				String requestquotes = getRequestQuotes(reservation,car);
				car = getResponseQuotes(car, reservation, requestquotes);
				//Sólo lo añado si la categoría coincide
				if (car.getCarType().equalsIgnoreCase(carTypeG))
				{
					//Y además tiene tarifa disponible, por lo que está disponible
					if (car.isHaytarifas())
						vcars.add(car);
				}
			}
    	}
    	catch (Exception e)
    	{
    		return vcars;
    	}
    	return vcars;
    }
    
    /**
	 * CBC Construyo el request de Quotes para enviar al WS
	 * y devuelvo el mismo objeto car con quote, reservation y delivery relleno
	 * @param car
	 * @param reservation
	 * @return
	 */
	public static String getRequestQuotes(Reservation reservation, Car car)
	{
		String request = "";
		try
		{
			request += Atributos.CABECERAXML;
			//Enviamos para recibir quote
			request += "<message>"
						+ "<serviceRequest serviceCode=\"getQuote\">"
						+ Atributos.LANGUAGE_ES
							+ "<serviceParameters>"
								+ "<reservation carCategory=\""+car.getCarCategoryCode()+"\">"
									+ "<checkout stationID=\""+reservation.getCheckoutstationId()+"\" date=\""+reservation.getCheckoutdate()+"\" time=\""+reservation.getCheckouttime()+"\" language=\"ES\"/>"
									+ "<checkin stationID=\""+reservation.getCheckinstationId()+"\" date=\""+reservation.getCheckindate()+"\" time=\""+reservation.getCheckintime()+"\" language=\"ES\"/>"
								+ "</reservation>"
								//Por el momento, por defecto, el conductor será de España
								+ "<driver countryOfResidence=\"ES\" />"
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
     * Obtengo la lista de nodos de reserva, quotes e insurance
     * @param car
     * @param reservation
     * @param texto
     * @throws Exception
     */
	public static Car getResponseQuotes(Car car, Reservation reservation, String texto)
    {
    	String responseStr = "";
    	try
    	{
    		URL obj = new URL(Atributos.PUSH_URL_AUTH);
    		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
           
    		//add request header
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    		//Send post request
    		con.setDoOutput(true);
    		con.setDoInput(true);
    		
    		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    		wr.writeBytes(texto);
    		wr.flush();
    		wr.close();

    		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		StringBuilder response = new StringBuilder();
   
    		while ((inputLine = in.readLine()) != null)
    		{
    			response.append(inputLine);
    		}
    		in.close();
    		responseStr = response.toString();
    		if (!responseStr.contains("quote"))
    		{
    			if (responseStr.contains("noratefound"))
    			{
    				car.setHaytarifas(false);
    			}
    			else
    				logger.error("No se ha podido obtener la información, error en el formato del xml.");
    		}
    		else
    		{
    			logger.info("Obtengo el listado de vehículos según los criterios seleccionados.");
    			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    			DocumentBuilder db = dbf.newDocumentBuilder();
    			InputSource is = new InputSource();
    			is.setCharacterStream(new StringReader(responseStr));
    			Document doc = db.parse(is);
    			
    			//Recogemos el nodo de reserva
    			NodeList listreservation = doc.getElementsByTagName("reservation");
    			reservation = recogeReservation(listreservation, reservation);
    			
    			//Recogemos la edad mínima para reservar
    			NodeList listage = doc.getElementsByTagName("ageLimit");
    			Element el = (Element) listage.item(0);
    			reservation.setMinAgeForCategory(el.getAttribute("minAgeForCategory"));
    			reservation.setMinAgeForCountry(el.getAttribute("minAgeForCountry"));
    			
    			//Incluimos el objeto reserva en el objeto coche
    			car.setReservation(reservation);
    			
    			//Obtenemos los datos de la estación de checkout o recogida de vehículo
    			Station stcheckout = new Station();
    			stcheckout.setCodstation(reservation.getCheckoutstationId());
    			stcheckout = JdbcStationsDao.existeStation(stcheckout, "");
    			car.setStationcheckout(stcheckout);
    			
    			//Obtenemos los datos de la estación de checkin o devolución de vehículo
    			Station stcheckin = new Station();
    			stcheckin.setCodstation(reservation.getCheckinstationId());
    			stcheckin = JdbcStationsDao.existeStation(stcheckin, "");
    			car.setStationcheckin(stcheckin);
    			
    			//Recogemos el nodo de quote
    			NodeList listquote = doc.getElementsByTagName("quote");
    			Quote quote = recogeQuote(listquote);
    			
    			//Incluimos el objeto quote en el objeto coche
    			car.setQuote(quote);
    			
    			//Recogemos el nodo de seguro
    			NodeList listinsurances = doc.getElementsByTagName("insurance");
    			Vector<Insurance> vinsurances = recogeInsurances(listinsurances);
    			
    			//Incluimos el vector de insurances en el objeto coche
    			car.setVinsurances(vinsurances);
    			
    			//Recogemos el nodo de tasas
    			NodeList listaxes = doc.getElementsByTagName("tax");
    			Vector<Tax> vtaxes = recogeTaxes(listaxes);
    			
    			//Incluimos el vector de tasas en el objeto coche
    			car.setVtaxes(vtaxes);
    			car.setHaytarifas(true);
    		}
       }
       catch (Exception e)
       {
    	   return car;
       }
       return car;
    }
	
	/**
     * Recogemos la reserva
     * @param listcars
     * @throws Exception
     */
    public static Reservation recogeReservation(NodeList listreservation, Reservation reservation)
    {
    	try
    	{
			Element el = (Element) listreservation.item(0);
			reservation.setCarCategory(el.getAttribute("carCategory"));
			reservation.setCarCategoryCO2Quantity(el.getAttribute("carCategoryCO2Quantity"));
			reservation.setCarCategoryType(el.getAttribute("carCategoryType"));
			reservation.setCountryOfReservation(el.getAttribute("countryOfReservation"));
			reservation.setDuration(Integer.parseInt(el.getAttribute("duration")));
			reservation.setProductfamily(el.getAttribute("productFamily"));
			reservation.setRateId(el.getAttribute("rateId"));
			reservation.setReservationDate(el.getAttribute("reservationDate"));
			reservation.setReservationTime(el.getAttribute("reservationTime"));
			reservation.setStatusCode(el.getAttribute("statusCode"));
			reservation.setType(el.getAttribute("type"));
    	}
    	catch (Exception e)
    	{
    		return reservation;
    	}
    	return reservation;
    }
    
    /**
     * Recogemos la reserva
     * @param listcars
     * @throws Exception
     */
    public static Quote recogeQuote(NodeList lisquote)
    {
    	Quote quote = new Quote();
    	try
    	{
			Element el = (Element) lisquote.item(0);
			quote.setIncludedKmUnit(el.getAttribute("IncludedKmUnit"));
			quote.setAreTaxesIncluded(el.getAttribute("areTaxesIncluded"));
			quote.setBaseCount(el.getAttribute("baseCount"));
			quote.setBasePrice(el.getAttribute("basePrice"));
			quote.setBaseType(el.getAttribute("baseType"));
			quote.setBookingCurrencyOfTotalRateEstimate(el.getAttribute("bookingCurrencyOfTotalRateEstimate"));
			quote.setCurrency(el.getAttribute("currency"));
			quote.setExtraKmPrice(el.getAttribute("extraKmPrice"));
			quote.setIncludedKm(el.getAttribute("includedKm"));
			quote.setIncludedKmType(el.getAttribute("includedKmType"));
			quote.setIsAuthorizedForPublicUse(el.getAttribute("isAuthorizedForPublicUse"));
			quote.setIsPrepaid(el.getAttribute("isPrepaid"));
			quote.setIsPriceSecret(el.getAttribute("isPriceSecret"));
			quote.setRentingCurrencyOfTotalRateEstimate(el.getAttribute("rentingCurrencyOfTotalRateEstimate"));
			quote.setTotalRateEstimate(el.getAttribute("totalRateEstimate"));
			quote.setTotalRateEstimateInBookingCurrency(el.getAttribute("totalRateEstimateInBookingCurrency"));
			quote.setTotalRateEstimateInRentingCurrency(el.getAttribute("totalRateEstimateInRentingCurrency"));
			quote.setXrsBasePrice(el.getAttribute("xrsBasePrice"));
    	}
    	catch (Exception e)
    	{
    		return quote;
    	}
    	return quote;
    }
    
    /**
     * Recogemos insurance
     * @param listcars
     * @throws Exception
     */
    public static Vector<Insurance> recogeInsurances(NodeList listinsurances)
    {
    	Vector<Insurance> vinsurances = new Vector<Insurance>();
    	try
    	{
    		for (int i = 0; i < listinsurances.getLength() ; i ++) 
			{
				Insurance insurance = new Insurance();
    			Element el = (Element) listinsurances.item(i);
    			insurance.setBkExcessWithPOM(el.getAttribute("bkExcessWithPOM"));
    			insurance.setCode(el.getAttribute("code"));
    			insurance.setDescr(el.getAttribute("descr"));
    			insurance.setExcessWithPOM(el.getAttribute("excessWithPOM"));
    			insurance.setPrice(el.getAttribute("price"));
    			insurance.setPriceInBookingCurrency(el.getAttribute("priceInBookingCurrency"));
    			insurance.setRentalPriceAI(el.getAttribute("rentalPriceAI"));
    			insurance.setRentalPriceInBookingCurrencyAI(el.getAttribute("rentalPriceInBookingCurrencyAI"));
    			insurance.setType(el.getAttribute("type"));
    			vinsurances.add(insurance);
			}
    	}
    	catch (Exception e)
    	{
    		return vinsurances;
    	}
    	return vinsurances;
    }
    
    /**
     * Recogemos las tasas
     * @param listaxes
     * @throws Exception
     */
    public static Vector<Tax> recogeTaxes(NodeList listaxes)
    {
    	Vector<Tax> vtaxes = new Vector<Tax>();
    	try
    	{
    		for (int i = 0; i < listaxes.getLength() ; i ++) 
			{
				Tax tax = new Tax();
    			Element el = (Element) listaxes.item(i);
    			tax.setTaxCode(el.getAttribute("bkExcessWithPOM"));
    			tax.setTaxRate(el.getAttribute("taxRate"));
    			vtaxes.add(tax);
			}
    	}
    	catch (Exception e)
    	{
    		return vtaxes;
    	}
    	return vtaxes;
    }
}