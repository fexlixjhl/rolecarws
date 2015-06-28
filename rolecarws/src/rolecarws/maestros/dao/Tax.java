package rolecarws.maestros.dao;

public class Tax
{
	private String taxCode;
	private String taxRate;
	
	/**
	 * @return the taxCode
	 */
	public String getTaxCode()
	{
		return taxCode;
	}
	/**
	 * @param taxCode the taxCode to set
	 */
	public void setTaxCode(String taxCode)
	{
		this.taxCode = taxCode;
	}
	/**
	 * @return the taxRate
	 */
	public String getTaxRate()
	{
		return taxRate;
	}
	/**
	 * @param taxRate the taxRate to set
	 */
	public void setTaxRate(String taxRate)
	{
		this.taxRate = taxRate;
	}
}