package rolecarws.maestros.dao;

public class Country
{
	private int idcountry;
	private String codcountry;
	private String descr;
	
	/**
	 * @return the idcountry
	 */
	public int getIdcountry()
	{
		return idcountry;
	}
	/**
	 * @param idcountry the idcountry to set
	 */
	public void setIdcountry(int idcountry)
	{
		this.idcountry = idcountry;
	}
	/**
	 * @return the codcountry
	 */
	public String getCodcountry()
	{
		return codcountry;
	}
	/**
	 * @param codcountry the codcountry to set
	 */
	public void setCodcountry(String codcountry)
	{
		this.codcountry = codcountry;
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
}