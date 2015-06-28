package rolecarws.utils;

import java.util.Comparator;

import rolecarws.maestros.dao.Car;

@SuppressWarnings("rawtypes")
public class CarComparator implements Comparator
{
	private int orden = 0;
	public CarComparator(int orden)
	{
		this.orden = orden;
	}
	/**
	 * Ordena según el orden pasado como parámetro el vector de vehículos devuelto
     * Los órdenes son
     * 1: PRECIO DE FORMA ASCENDENTE
     * 2: PRECIO DE FORMA DESCENDENTE
     * 3: Nº DE ASIENTOS DE FORMA ASCENDENTE
     * 4: Nº DE ASIENTOS DE FORMA DESCENDENTE
     * 5: EMISIONES CO2 DE FORMA ASCENDENTE
     * 6: EMISIONES CO2 DE FORMA DESCENDENTE
	 * @param o1
	 * @param o2
	 * @param orden
	 * @return
	 */
	public int compare(Object o1, Object o2) 
	{
		int comparevar = 0;
		
		//Variables para ordenación por precio
		Float precioc1 = null;
		Float precioc2 = null;
		String precioc1S = "";
		String precioc2S = "";

		//Variables para ordenación por asientos
		int numas1 = 0;
		int numas2 = 0;
		
		//Variables para ordenación por emisión de CO2
		int emisco1 = 0;
		int emisco2 = 0;
		
		try
		{
			Car c1 = (Car) o1;
			Car c2 = (Car) o2;
			
			//Por defecto, el orden será por precio de forma ascendente
			switch(orden)
			{
				//Ordenación por defecto por precio de forma ascendente
				case 1:
					precioc1S = c1.getQuote().getTotalRateEstimateInBookingCurrency();
					precioc2S = c2.getQuote().getTotalRateEstimateInBookingCurrency();
					precioc1 = new Float(precioc1S);
					precioc2 = new Float(precioc2S);
					if (precioc1S==null || precioc1S.equalsIgnoreCase(""))
					{	
						comparevar = 1;
					}
					else if(precioc2S==null || precioc2S.equalsIgnoreCase(""))
					{
						comparevar = 0;
					}
					else if(precioc1.compareTo(precioc2)<0)
					{
						comparevar = -1;
					}
					else if(precioc1.compareTo(precioc2)>0)
					{
						comparevar = 1;
					}
					else
					{
						comparevar = 0;
					}
				break;
				
				//Ordenación por precio de forma descendente
				case 2:
					precioc1S = c1.getQuote().getTotalRateEstimateInBookingCurrency();
					precioc2S = c2.getQuote().getTotalRateEstimateInBookingCurrency();
					precioc1 = new Float(precioc1S);
					precioc2 = new Float(precioc2S);
					if (precioc2S==null || precioc2S.equalsIgnoreCase(""))
					{	
						comparevar = 1;
					}
					else if(precioc1S==null || precioc1S.equalsIgnoreCase(""))
					{
						comparevar = 0;
					}
					else if(precioc1.compareTo(precioc2)<0)
					{
						comparevar = 1;
					}
					else if(precioc1.compareTo(precioc2)>0)
					{
						comparevar = -1;
					}
					else
					{
						comparevar = 0;
					}
				break;
				
				//Ordenación por asientos de forma ascendente
				case 3:
					numas1 = Integer.parseInt(c1.getCarCategorySeats());
					numas2 = Integer.parseInt(c2.getCarCategorySeats());
					if(numas1>numas2)
					{
						comparevar = 1;
					}
					else if(numas1<numas2)
					{
						comparevar = -1;
					}
					else
					{
						comparevar = 0;
					}
				break;
				
				
				//Ordenación por asientos de forma descendente
				case 4:
					numas1 = Integer.parseInt(c1.getCarCategorySeats());
					numas2 = Integer.parseInt(c2.getCarCategorySeats());
					if(numas1<numas2)
					{
						comparevar = 1;
					}
					else if(numas1>numas2)
					{
						comparevar = -1;
					}
					else
					{
						comparevar = 0;
					}
				break;
					
				//Ordenación por emisión de CO2 de forma ascendente
				case 5:
					emisco1 = c1.getCarCategoryCO2Quantity();
					emisco2 = c2.getCarCategoryCO2Quantity();
					if(emisco1>emisco2)
					{
						comparevar = 1;
					}
					else if(emisco1<emisco2)
					{
						comparevar = -1;
					}
					else
					{
						comparevar = 0;
					}
					break;
					
				//Ordenación por emisión de CO2 de forma descendente
				case 6:
					emisco1 = c1.getCarCategoryCO2Quantity();
					emisco2 = c2.getCarCategoryCO2Quantity();
					if(emisco1<emisco2)
					{
						comparevar = 1;
					}
					else if(emisco1>emisco2)
					{
						comparevar = -1;
					}
					else
					{
						comparevar = 0;
					}
				break;
			}
		}
		catch (Exception e)
		{
			return comparevar;
		}
		
		return comparevar;
	}
	public boolean equals(Object o)
	{
		return this == o;
	}
}