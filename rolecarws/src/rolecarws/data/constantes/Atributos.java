package rolecarws.data.constantes;

public class Atributos
{
	//For test
//	public static String PUSH_URL_AUTH = "https://applications-ptn.europcar.com/xrs/resxml";
	
	//For production
	public static final String PUSH_URL_AUTH = "https://applications.europcar.com/xrs/resxml";
	
	public static final String CABECERAXML = "XML-Request=<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String LANGUAGE_ES = "<serviceContext><localisation active=\"true\"><language code=\"es_ES\"/></localisation></serviceContext>";
	public static final String ESQUEMA= "rolecar";
	public static final String TABLACOUNTRIES = "countries";
	public static final String TABLACITIES = "cities";
	public static final String TABLASTATIONS = "stations";
	public static final String TABLACARS = "cars";
	public static final String TABLASCHEMA = "schema_sw";
	
	//Usuario y password de Rolecar
	public static final String USER = "924486681";
	public static final String PASS = "11072014";
}
