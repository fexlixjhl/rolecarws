package rolecarws.utils;

public class Formatea 
{
	/**
	 * CBC Sobrecargo las barras para evitar problemas en consultas sql
	 * @param texto
	 * @return
	 */
	public static String formateaBarras(String texto)
	{
		try
		{
			texto = texto.replaceAll("/", "//");
		}
		catch (Exception e)
		{
			return texto;
		}
		return texto;
	}
	/**
	 * CBC Sobrecargo las comillas simples para evitar problemas en consultas sql
	 * @param texto
	 * @return
	 */
	public static String formateaComillas(String texto)
	{
		try
		{
			texto = texto.replaceAll("'", "''");
		}
		catch (Exception e)
		{
			return texto;
		}
		return texto;
	}
}