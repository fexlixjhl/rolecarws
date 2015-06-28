package rolecarws.utils;


import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

 
public class ConversoresFecha 
{
	//public static final Log logger = LogFactory.getLog(ConversoresFecha.class);
	
	/**
	 * Devuelve la fecha actual en formato dd/mm/aaaa
	 * @author cbordas
	 * @return java.lang.String
	 */
	public static String dameFecha(Timestamp fechaSistema)
	{
		String fecha = "";
		java.util.Calendar cal = new java.util.GregorianCalendar();
		cal.setTime(fechaSistema);
	
		int dia=cal.get(Calendar.DAY_OF_MONTH);
		fecha = (dia<10?"0"+dia:""+dia);
		
		int mes = cal.get(Calendar.MONTH) + 1;
		fecha += "/" + (mes<10?"0"+mes:""+mes);
	
		fecha += "/" + cal.get(Calendar.YEAR);
	
		fecha += " " ;
		
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		fecha += (hora<10?"0"+hora:""+hora);
	
		int minutos = cal.get(Calendar.MINUTE);
		fecha += ":" + (minutos<10?"0"+minutos:""+minutos);
	
		return fecha;
	}
	
	/**
	 * Devuelve la fecha actual en formato dd/mm/aaaa
	 * @author cbordas
	 * @return java.lang.String
	 */
	public static String dameFechaOrdenada(Timestamp fechaSistema)
	{
		String fecha = "";
		java.util.Calendar cal = new java.util.GregorianCalendar();
		cal.setTime(fechaSistema);
	
		fecha =  cal.get(Calendar.YEAR)+"";
		
		int mes = cal.get(Calendar.MONTH) + 1;
		fecha +=  (mes<10?"0"+mes:""+mes);
			
		int dia=cal.get(Calendar.DAY_OF_MONTH);
		fecha += (dia<10?"0"+dia:""+dia);
		
		return fecha;
	}
	
	public static Date dameFechaDatePuntos(String fecha)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(Integer.parseInt(fecha.substring(6,10)), Integer.parseInt(fecha.substring(3,5))-1, Integer.parseInt(fecha.substring(0,2)));
		Date dfecha = new Date(calendar.getTimeInMillis());
		return dfecha;
	}
	
	public static Date dameFechaDateGuiones(String fecha)
	{
		Date dfecha;
		int anio;
		int mes;
		int dia;
		
		StringTokenizer st = new StringTokenizer(fecha, "-", false);
		
		try 
		{
			//logger.info("Formateamos");
			anio = Integer.parseInt(st.nextToken());
			//logger.info("anio:"+anio);
			mes = Integer.parseInt(st.nextToken());
			//logger.info("mes:"+mes);
			dia = Integer.parseInt(st.nextToken());
			//logger.info("dia:"+dia);
			
			Calendar calendar = new GregorianCalendar();
			calendar.set(anio, mes-1, dia);
			dfecha = new Date(calendar.getTimeInMillis());
			//logger.info("dfecha:"+dfecha);
			
		} 
		catch (Throwable t)
		{
			//logger.error("ERROR al formatear fecha "+ fecha);
			dfecha = null;
		}
		return dfecha;
	}
	
	/**
	 * Convierte ddmmaaaa a ddmmaa
	 * @param cadena_fecha
	 * @return
	 */
	public static String convertirDD7MM7AAAAtoDDMMAA(String cadena_fecha)
	{
		String result = "";
		result = cadena_fecha.substring(0,2) + cadena_fecha.substring(3,5) + cadena_fecha.substring(8,10);
		return result;
	}
	
	public static Date getFechaSincro(String fecha)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(Integer.parseInt(fecha.substring(6,10)), Integer.parseInt(fecha.substring(3,5))-1, Integer.parseInt(fecha.substring(0,2)));
		Date dfecha = new Date(calendar.getTimeInMillis());
		return dfecha;
	}
	
	/**
	 * Formateo fecha de yyyy-mm-dd a dd-mm-yyyy 
	 * @param fecha
	 * @return
	 */
	public static String formateaFecha(Date fecha)
	{
		String fechaS = "";
		try 
		{
			if (fecha!=null && !fecha.equals(""))
			{
				fechaS = String.valueOf(fecha);
				String[] arrFecha = fechaS.split("-");
				fechaS = arrFecha[2] + "-" + arrFecha[1] + "-" + arrFecha[0];
			}
			
		} 
		catch (Exception e) 
		{
			return fechaS;
		}
		return fechaS;
	}
	
	/**
	 * Formateo fecha de dd-mm-yyyy a yyyy-mm-dd 
	 * @param fechaS
	 * @return
	 */

	public static Date formateaFechaSQL(String fechaS)
	{
		Date fecha = null;
		String fechaux = "";
		try 
		{
			if (fechaS!=null && !fechaS.equals(""))
			{
				fechaux = String.valueOf(fechaS);
				String[] arrFecha = fechaux.split("-");
				fechaux = arrFecha[2] + "-" + arrFecha[1] + "-" + arrFecha[0];
				fecha = Date.valueOf(fechaux);
			}
		} 
		catch (Exception e) 
		{
			return fecha;
		}
		return fecha;
	}
	
	/**
	 * Formateo fecha de yyyy-mm-dd a dd/mm/yyyy 
	 * @param fecha
	 * @return
	 */
	
	public static String formateaFechaBarras(Date fecha)
	{
		String fechaS = "";
		try 
		{
			if (fecha!=null && !fecha.equals(""))
			{
				fechaS = String.valueOf(fecha);
				String[] arrFecha = fechaS.split("-");
				fechaS = arrFecha[2] + "/" + arrFecha[1] + "/" + arrFecha[0];
			}
			
		} 
		catch (Exception e) 
		{
			return fechaS;
		}
		return fechaS;
	}
	
	/**
	 * Formateo fecha de dd/mm/yyyy a yyyy-mm-dd
	 * @param fechaS
	 * @return
	 */
	
	public static Date formateaFechaBarrasSQL(String fechaS)
	{
		Date fecha = new Date(System.currentTimeMillis());
		try 
		{
			if (fecha!=null && !fecha.equals(""))
			{
				String[] arrFecha = fechaS.split("/");
				fechaS = arrFecha[2] + "-" + arrFecha[1] + "-" + arrFecha[0];
				fecha = Date.valueOf(fechaS);
			}
			
		} 
		catch (Exception e) 
		{
			return fecha;
		}
		return fecha;
	}
	
	/**
	 * Formateo hora y le quito los ceros de milisegundos
	 * @param hora
	 * @return
	 */
	public static String formateaZero(Time hora)
	{
		String horaS = "00:00";
		try 
		{
			if (hora!=null && !hora.equals(""))
			{
				horaS = hora.toString();
				String[] arrHora = horaS.split(":");
				horaS = arrHora[0] + ":" + arrHora[1];
			}
		} 
		catch (Exception e) 
		{
			return horaS;
		}
		return horaS;
	}
	
	/**
	 * Convierte un formato fecha 01-10-2013 a 2013-10-01 o de 2013-10-01 a 01-10-2013
	 * cbc si el día o el mes sólo tiene un dígito, añade 0's por la izquierda
	 * @param fecha
	 * @return
	 */
	public static String formateaFechaGuion(String fecha)
	{
		String fechaS = "";
		try 
		{
			if (fecha!=null && !fecha.equals(""))
			{
				String[] arrFecha = fecha.split("-");
				fechaS = (arrFecha[2].length()<2?"0".concat(arrFecha[2]):"".concat(arrFecha[2]));
				fechaS += "-";
				fechaS += (arrFecha[1].length()<2?"0".concat(arrFecha[1]):"".concat(arrFecha[1]));
				fechaS += "-";
				fechaS += (arrFecha[0].length()<2?"0".concat(arrFecha[0]):"".concat(arrFecha[0]));
			}
			
		} 
		catch (Exception e) 
		{
			//logger.error("Error formato fecha: "+e.getMessage());
			return fechaS;
			
		}
		return fechaS;
	}
	public static int formateaHoraInt(String horaS)
	{
		String [] arrfechaS = new String[3];
		int horaint = 0;
		
		try 
		{
			if (horaS!=null && !horaS.equalsIgnoreCase(""))
			{
				arrfechaS = horaS.split(":");
				//Multiplico por 60 para que me devuelva los minutos
				int horas = Integer.parseInt(arrfechaS[0]) * 60;
				int minutos = Integer.parseInt(arrfechaS[1]);
				
				horaint = horas + minutos;
			}
			
		} 
		catch (Exception e) 
		{
			return horaint;
		}
		return horaint;
	}
	/**
	 * Funciona que resta una hora inicial y una hora final en minutos
	 * @param horaI
	 * @param horaF
	 * @return
	 */
	public static int RestaHora(int horaI,int horaF)
	{
		//String [] arrfechaS = new String[3];
		int horaresta = 0;
		
		try 
		{
			horaresta = horaF - horaI;
			
		} 
		catch (Exception e) 
		{
			return horaresta;
		}
		return horaresta;
	}
}