package rolecarws.maestros.dao;

public class Station
{
	private int idstation;
	private String codstation;
	private String descr;
	private int idprovincia;
	private City city;
	
	/**
	 * @return the idstation
	 */
	public int getIdstation()
	{
		return idstation;
	}
	/**
	 * @param idstation the idstation to set
	 */
	public void setIdstation(int idstation)
	{
		this.idstation = idstation;
	}
	/**
	 * @return the codstation
	 */
	public String getCodstation()
	{
		return codstation;
	}
	/**
	 * @param codstation the codstation to set
	 */
	public void setCodstation(String codstation)
	{
		this.codstation = codstation;
	}
	/**
	 * @return the descr
	 */
	public String getDescr()
	{
		return descr;
	}
	/**
	 * @param descr the descr to set
	 */
	public void setDescr(String descr)
	{
		this.descr = descr;
	}
	/**
	 * @return the idprovincia
	 */
	public int getIdprovincia()
	{
		return idprovincia;
	}
	/**
	 * @param idprovincia the idprovincia to set
	 */
	public void setIdprovincia(int idprovincia)
	{
		this.idprovincia = idprovincia;
	}
	/**
	 * @return the city
	 */
	public City getCity()
	{
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(City city)
	{
		this.city = city;
	}
}