/**
 * 
 */
package utilerias;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * @author jesus
 *
 */
public class Utilerias {
	private final static Logger LOGGER = Logger.getLogger(Utilerias.class.getName());
	public Calendar fechaActual(Date fecha){
		Calendar calendario = new GregorianCalendar();
		calendario.setTime(fecha);
		return calendario;
	}
	public Calendar sumaDiasFecha(Calendar calendario, int dias) {
		Calendar fecha = new GregorianCalendar();
		fecha.setTime(calendario.getTime());
		fecha.add(Calendar.DAY_OF_YEAR, dias);
		return fecha;
	}
	public String nombreMeses(Calendar calendario) {
		String nombreMes = "";
		try {
			int mes = calendario.get(Calendar.MONTH);
			switch(mes) {
			case 0:
				nombreMes ="enero";
				break;
			case 1:
				nombreMes ="febrero";
				break;
			case 2:
				nombreMes ="marzo";
				break;
			case 3:
				nombreMes ="abril";
				break;
			case 4:
				nombreMes ="mayo";
				break;
			case 5:
				nombreMes ="junio";
				break;
			case 6:
				nombreMes ="julio";
				break;
			case 7:
				nombreMes ="agosto";
				break;
			case 8:
				nombreMes ="septiembre";
				break;
			case 9:
				nombreMes ="octubre";
				break;
			case 10:
				nombreMes ="noviembre";
				break;
			case 11:
				nombreMes ="diciembre";
				break;
			}
			
		}catch(Exception e) {
			LOGGER.info("Error al obtener el nombre del mes "+e );
		}
		return nombreMes;
	}

}
