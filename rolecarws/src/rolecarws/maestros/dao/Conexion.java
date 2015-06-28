package rolecarws.maestros.dao;

import java.sql.Timestamp;

public class Conexion
{
	private int idconnection;
	private int idaccpoint;
	private String mac;
	private int tipo;
	private String descr;
	private Timestamp falta;
	private Timestamp fmodif;
	private int procesado;
	private int usersmac;
	private String horaInicio;
	private String horaFin;
	private String fecha;
	
	public String getFecha()
	{
		return fecha;
	}
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	public String getHoraInicio()
	{
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio)
	{
		this.horaInicio = horaInicio;
	}
	public String getHoraFin()
	{
		return horaFin;
	}
	public void setHoraFin(String horaFin)
	{
		this.horaFin = horaFin;
	}
	public int getUsersmac()
	{
		return usersmac;
	}
	public void setUsersmac(int usersmac)
	{
		this.usersmac = usersmac;
	}
	public int getIdconnection()
	{
		return idconnection;
	}
	public void setIdconnection(int idconnection)
	{
		this.idconnection = idconnection;
	}
	public int getIdaccpoint()
	{
		return idaccpoint;
	}
	public void setIdaccpoint(int idaccpoint)
	{
		this.idaccpoint = idaccpoint;
	}
	public String getMac()
	{
		return mac;
	}
	public void setMac(String mac)
	{
		this.mac = mac;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo)
	{
		this.tipo = tipo;
	}
	public Timestamp getFalta()
	{
		return falta;
	}
	public void setFalta(Timestamp falta)
	{
		this.falta = falta;
	}
	public Timestamp getFmodif()
	{
		return fmodif;
	}
	public void setFmodif(Timestamp fmodif)
	{
		this.fmodif = fmodif;
	}
	public int getProcesado()
	{
		return procesado;
	}
	public void setProcesado(int procesado)
	{
		this.procesado = procesado;
	}
	public String getDescr()
	{
		return descr;
	}
	public void setDescr(String descr)
	{
		this.descr = descr;
	}
}