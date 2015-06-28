package rolecarws.maestros.dao;

import java.util.Vector;

public class Car
{
	private String carCategoryAirCond;
	private String carCategoryAutomatic;
	private int carCategoryCO2Quantity;
	private String carCategoryCode;
	private String carCategoryDoors;
	private String carCategoryName;
	private String carCategorySample;
	private String carCategorySeats;
	private String carCategoryStatusCode;
	private String carCategoryType;
	private String carType;
	private Reservation reservation;
	private Quote quote;
	private Vector<Insurance> vinsurances;
	private Vector<Tax> vtaxes;
	private Station stationcheckout;
	private Station stationcheckin;
	private boolean haytarifas;

	/**
	 * @return the carCategoryAirCond
	 */
	public String getCarCategoryAirCond()
	{
		return carCategoryAirCond;
	}
	/**
	 * @param carCategoryAirCond the carCategoryAirCond to set
	 */
	public void setCarCategoryAirCond(String carCategoryAirCond)
	{
		this.carCategoryAirCond = carCategoryAirCond;
	}
	/**
	 * @return the carCategoryAutomatic
	 */
	public String getCarCategoryAutomatic()
	{
		return carCategoryAutomatic;
	}
	/**
	 * @param carCategoryAutomatic the carCategoryAutomatic to set
	 */
	public void setCarCategoryAutomatic(String carCategoryAutomatic)
	{
		this.carCategoryAutomatic = carCategoryAutomatic;
	}
	/**
	 * @return the carCategoryCO2Quantity
	 */
	public int getCarCategoryCO2Quantity()
	{
		return carCategoryCO2Quantity;
	}
	/**
	 * @param carCategoryCO2Quantity the carCategoryCO2Quantity to set
	 */
	public void setCarCategoryCO2Quantity(int carCategoryCO2Quantity)
	{
		this.carCategoryCO2Quantity = carCategoryCO2Quantity;
	}
	/**
	 * @return the carCategoryCode
	 */
	public String getCarCategoryCode()
	{
		return carCategoryCode;
	}
	/**
	 * @param carCategoryCode the carCategoryCode to set
	 */
	public void setCarCategoryCode(String carCategoryCode) 
	{
		this.carCategoryCode = carCategoryCode;
	}
	/**
	 * @return the carCategoryDoors
	 */
	public String getCarCategoryDoors()
	{
		return carCategoryDoors;
	}
	/**
	 * @param carCategoryDoors the carCategoryDoors to set
	 */
	public void setCarCategoryDoors(String carCategoryDoors)
	{
		this.carCategoryDoors = carCategoryDoors;
	}
	/**
	 * @return the carCategoryName
	 */
	public String getCarCategoryName()
	{
		return carCategoryName;
	}
	/**
	 * @param carCategoryName the carCategoryName to set
	 */
	public void setCarCategoryName(String carCategoryName)
	{
		this.carCategoryName = carCategoryName;
	}
	/**
	 * @return the carCategorySample
	 */
	public String getCarCategorySample()
	{
		return carCategorySample;
	}
	/**
	 * @param carCategorySample the carCategorySample to set
	 */
	public void setCarCategorySample(String carCategorySample)
	{
		this.carCategorySample = carCategorySample;
	}
	/**
	 * @return the carCategorySeats
	 */
	public String getCarCategorySeats()
	{
		return carCategorySeats;
	}
	/**
	 * @param carCategorySeats the carCategorySeats to set
	 */
	public void setCarCategorySeats(String carCategorySeats)
	{
		this.carCategorySeats = carCategorySeats;
	}
	/**
	 * @return the carCategoryStatusCode
	 */
	public String getCarCategoryStatusCode()
	{
		return carCategoryStatusCode;
	}
	/**
	 * @param carCategoryStatusCode the carCategoryStatusCode to set
	 */
	public void setCarCategoryStatusCode(String carCategoryStatusCode)
	{
		this.carCategoryStatusCode = carCategoryStatusCode;
	}
	/**
	 * @return the carCategoryType
	 */
	public String getCarCategoryType()
	{
		return carCategoryType;
	}
	/**
	 * @param carCategoryType the carCategoryType to set
	 */
	public void setCarCategoryType(String carCategoryType)
	{
		this.carCategoryType = carCategoryType;
	}
	/**
	 * @return the carType
	 */
	public String getCarType()
	{
		return carType;
	}
	/**
	 * @param carType the carType to set
	 */
	public void setCarType(String carType)
	{
		this.carType = carType;
	}
	/**
	 * @return the reservation
	 */
	public Reservation getReservation()
	{
		return reservation;
	}
	/**
	 * @param reservation the reservation to set
	 */
	public void setReservation(Reservation reservation)
	{
		this.reservation = reservation;
	}
	/**
	 * @return the quote
	 */
	public Quote getQuote()
	{
		return quote;
	}
	/**
	 * @param quote the quote to set
	 */
	public void setQuote(Quote quote)
	{
		this.quote = quote;
	}
	/**
	 * @return the vinsurances
	 */
	public Vector<Insurance> getVinsurances()
	{
		return vinsurances;
	}
	/**
	 * @param vinsurances the vinsurances to set
	 */
	public void setVinsurances(Vector<Insurance> vinsurances)
	{
		this.vinsurances = vinsurances;
	}
	/**
	 * @return the vtaxes
	 */
	public Vector<Tax> getVtaxes()
	{
		return vtaxes;
	}
	/**
	 * @param vtaxes the vtaxes to set
	 */
	public void setVtaxes(Vector<Tax> vtaxes)
	{
		this.vtaxes = vtaxes;
	}
	/**
	 * @return the stationcheckout
	 */
	public Station getStationcheckout()
	{
		return stationcheckout;
	}
	/**
	 * @param stationcheckout the stationcheckout to set
	 */
	public void setStationcheckout(Station stationcheckout)
	{
		this.stationcheckout = stationcheckout;
	}
	/**
	 * @return the stationcheckin
	 */
	public Station getStationcheckin()
	{
		return stationcheckin;
	}
	/**
	 * @param stationcheckin the stationcheckin to set
	 */
	public void setStationcheckin(Station stationcheckin)
	{
		this.stationcheckin = stationcheckin;
	}
	/**
	 * @return the nohaytarifas
	 */
	public boolean isHaytarifas()
	{
		return haytarifas;
	}
	/**
	 * @param nohaytarifas the nohaytarifas to set
	 */
	public void setHaytarifas(boolean haytarifas)
	{
		this.haytarifas = haytarifas;
	}
}