package rolecarws.maestros.dao;

public class City
{
	private Country country;
	private int idcity;
	private String codcity;
	private String descr;
	private int idprovincia;
	
	/**
	 * @return the country
	 */
	public Country getCountry()
	{
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country)
	{
		this.country = country;
	}
	/**
	 * @return the idcity
	 */
	public int getIdcity()
	{
		return idcity;
	}
	/**
	 * @param idcity the idcity to set
	 */
	public void setIdcity(int idcity)
	{
		this.idcity = idcity;
	}
	/**
	 * @return the codcity
	 */
	public String getCodcity()
	{
		return codcity;
	}
	/**
	 * @param codcity the codcity to set
	 */
	public void setCodcity(String codcity)
	{
		this.codcity = codcity;
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
}