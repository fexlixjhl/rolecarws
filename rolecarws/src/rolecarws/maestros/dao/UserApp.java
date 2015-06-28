package rolecarws.maestros.dao;

import java.sql.Date;
import java.util.Vector;

public class UserApp
{
	private boolean resultado_oper;
	private int iduser;
	private String username;
	private String userpass;
	private String mail;
	private Vector<String> mac;
	private String nombre;
	private int idnivel1;
	private char genero;
	private Date fechanac;
	private int hijos;
	private String message;
	
	public boolean isResultado_oper()
	{
		return resultado_oper;
	}
	public void setResultado_oper(boolean resultado_oper)
	{
		this.resultado_oper = resultado_oper;
	}
	public int getIduser()
	{
		return iduser;
	}
	public void setIduser(int iduser)
	{
		this.iduser = iduser;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getUserpass()
	{
		return userpass;
	}
	public void setUserpass(String userpass)
	{
		this.userpass = userpass;
	}
	public String getMail()
	{
		return mail;
	}
	public void setMail(String mail)
	{
		this.mail = mail;
	}
	public Vector<String> getMac()
	{
		return mac;
	}
	public void setMac(Vector<String> mac)
	{
		this.mac = mac;
	}
	public String getNombre()
	{
		return nombre;
	}
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	public int getIdnivel1()
	{
		return idnivel1;
	}
	public void setIdnivel1(int idnivel1)
	{
		this.idnivel1 = idnivel1;
	}
	public char getGenero()
	{
		return genero;
	}
	public void setGenero(char genero)
	{
		this.genero = genero;
	}
	public Date getFechanac()
	{
		return fechanac;
	}
	public void setFechanac(Date fechanac)
	{
		this.fechanac = fechanac;
	}
	public int getHijos()
	{
		return hijos;
	}
	public void setHijos(int hijos)
	{
		this.hijos = hijos;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
}