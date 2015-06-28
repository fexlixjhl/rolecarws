package rolecarws.main;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.StringUtils;

import rolecarws.data.dao.JdbcCitiesDao;
import rolecarws.data.dao.JdbcCountriesDao;
import rolecarws.data.dao.JdbcSchema;
import rolecarws.data.dao.JdbcStationsDao;
import rolecarws.maestros.dao.City;
import rolecarws.maestros.dao.Country;
import rolecarws.maestros.dao.SchemaSing;
import rolecarws.maestros.dao.Station;

public class Rolecarwsrunnable implements Runnable
{
	public static Log logger = LogFactory.getLog(Rolecarws.class);
	@Override
	public void run()
	{
		try
        {
			logger.info("Inicio de Proceso de extraccion de datos... Recuperando esquema online...");
			JdbcSchema.getSchemas();
			SchemaSing oSchema = SchemaSing.getInstance();
			//System.out.println("Off.line:: " + oSchema.getOffLine());
			//System.out.println("On.line:: " + oSchema.getOnLine());
			if (StringUtils.isNullOrEmpty(oSchema.getOffLine()) && StringUtils.isNullOrEmpty(oSchema.getOnLine())){
				logger.info("No se puede recuperar el esquema activo... Fin de Proceso.");
			}
			else{
				if (StringUtils.isNullOrEmpty(oSchema.getOffLine())){
					logger.info("Esquema off-line: A");
				}
				else{
					logger.info("Esquema off-line: ".concat(oSchema.getOffLine()));
				}
	        	logger.info("Recogemos los países en base a la llamada al WS.");
	        	List<Country> aCountries = JdbcCountriesDao.recogePaises();
				//Vector<Country> vcountries = JdbcCountriesDao.recogePaises();
				if (aCountries.size()>0)
				{
					logger.info("Recogemos las ciudades en base a la llamada al WS, el nº de paises es: " + aCountries.size());
					List<City> aCities = JdbcCitiesDao.recogeCiudades(aCountries);
					if (aCities.size()>0)
					{
						logger.info("Recogemos las estaciones en base a la llamada al WS, el nº de ciudades es: " + aCities.size());
						List<Station> aStations = JdbcStationsDao.recogeEstaciones(aCities);
						if (aStations.size()>0)
						{
							logger.info("El nº de estaciones es: " + aStations.size());
							logger.info("Ejecutando switch...");
							CopyOffLine.runCopy();
							logger.info("Fin de Proceso");
						}
						else{
							logger.info("SIN ESTACIONES.... Fin de Proceso");
						}
					}
				}
	        	
	//			Obtengo los vehículos en base a los parámetros de entrada de
	//			fecha de recogida, fecha de devolución, hora de recogida
	//			hora de devolución, tipo de coche, lugar de recogida y lugar de devolución
				
	//        	String checkindate = "20140822";
	//        	String checkoutdate = "20140815";
	//        	String checkintime = "1200";
	//        	String checkouttime = "1200";
	//        	String carType = "CR";
	//        	String checkinstationid="MADT04";
	//        	String checkoutstationid="MADT04";
	//        	
	//        	//Por defecto, ordeno por precio de forma ascendente
	//        	Vector<Car> vcars = JdbcCarsDao.recogeVehiculos(checkinstationid, checkindate, checkintime, checkoutstationid, checkoutdate, checkouttime, carType, 1);
	//        	for (int i=0;i<vcars.size();i++)
	//        	{
	//        		Car car = vcars.elementAt(i);
	//        		System.out.println("El precio del vehículo " + i + " es " + car.getQuote().getTotalRateEstimateInBookingCurrency());
	////        		System.out.println("El número de asientos del vehículo " + i + " son " + car.getCarCategorySeats());
	////        		System.out.println("Las emisiones de CO2 del vehículo " + i + " son " + car.getCarCategoryCO2Quantity());
	//        	}
			}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}