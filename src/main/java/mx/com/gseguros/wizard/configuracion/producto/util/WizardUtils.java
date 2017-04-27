/**
 * 
 */
package mx.com.gseguros.wizard.configuracion.producto.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Alfonso
 * 
 */
public class WizardUtils{
	private static final transient Log log = LogFactory
	.getLog(WizardUtils.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Metodo que verifica dos fechas, valida que la fechaIni sea menor a la fechaFin, los posibles resultados son los soguintes:
     * regresa [0] cuando fechaIni = fechaFin regresa [-1] cuando fechaIni > fechaFin regresa [1] cuando fechaIni < fechaFIn
     * 
     * @param finAnterior Date Fecha de In�cio
     * @param fechaFinal date Fecha de termino
     * @return int indicador de la comparaci�n
     */
    public static boolean validarRangoFechas(Date finAnterior, Date fechaFinal) {
        boolean indicador=false;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(finAnterior);
        if(log.isDebugEnabled()){
        	log.debug("%%%%%%%%%%%%%%%%%%%%% calendar 1"+ cal1);
        }
        cal2.setTime(fechaFinal);
        if(log.isDebugEnabled()){
        	log.debug("%%%%%%%%%%%%%%%%%%%%% calendar 2"+ cal2);
        }
        if (cal1.before(cal2)) {
            indicador = true;
        } else if (cal1.after(cal2)) {
                indicador = false;
        }else if (cal1.equals(cal2)) {
                    indicador = false;
        }
        
        return indicador;
    }

    public static Date parseDate(String date) throws ParseException {
    	Date temp=DateUtils.parseDate(date, Constantes.DATE_FORMATS);
    	if(log.isDebugEnabled()){
    		log.debug(temp.toString());
    	}
        return temp;
    }
    
    public static String parseDateCincoClaves(String date) throws ParseException {
    	//Date temp=DateUtils.parseDate(date, Constantes.DATE_FORMATS);
    	log.debug("@@@@@@@@@@@FECHA5CLAVES de ext "+date);
    	
    	String formato = "EEE MMM dd yyyy HH:mm:ss Z";
    	
    	if(date.contains("/"))return date;
    	
    	
    	int longFecha1Digito= date.length();
    	//log.debug("@@@@@@@@@@@longFecha1Digito de ext "+longFecha1Digito);
    	String dia;
    	String mes=date.substring(4,7);
    	String anio;
    	if (String.valueOf(longFecha1Digito).equals("27")){
    		dia=date.substring(8,9);
    		anio=date.substring(23,27);
    		dia="0"+dia;
    	}else{
    		dia=date.substring(8,10);
    		anio=date.substring(24,28);
    	}
    	//log.debug("@@@@@@@@@@@dia de ext "+dia);
    	//log.debug("@@@@@@@@@@@mes de ext "+mes);
    	//log.debug("@@@@@@@@@@@anio de ext "+anio);
    	/*int longitudDia= dia.length();
    	if (String.valueOf(longitudDia).equals("1")){
    		dia="0"+dia;
    	}*/
    	if(mes.equals("Jan")){
    		mes="01";
    		
    	}
    	if(mes.equals("Feb")){
    		mes="02";
    	}
    	if(mes.equals("Mar")){
    		mes="03";
    	}
    	if(mes.equals("Apr")){
    		mes="04";
    	}
    	if(mes.equals("May")){
    		mes="05";
    	}
    	if(mes.equals("Jun")){
    		mes="06";
    	}
    	if(mes.equals("Jul")){
    		mes="07";
    	}
    	if(mes.equals("Aug")){
    		mes="08";
    	}
    	if(mes.equals("Sep")){
    		mes="09";
    	}
    	if(mes.equals("Oct")){
    		mes="10";
    	}
    	if(mes.equals("Nov")){
    		mes="11";
    	}
    	if(mes.equals("Dec")){
    		mes="12";
    	}    	    
    	String fecha=dia+"/"+mes+"/"+anio;
    	/*if(log.isDebugEnabled()){
    		log.debug("FECHA PARSEADA para java y la base###### "+fecha);
    	}*/
        return fecha;
    }
    
    public static String parseDateBaseCincoClaves(String date) throws ParseException {
    	//Date temp=DateUtils.parseDate(date, Constantes.DATE_FORMATS);
    	//log.debug("BASEFECHA5CLAVES "+date);
    	String dia=date.substring(8,10);
    	String mes=date.substring(5,7);
    	String anio=date.substring(0,4);
    	
    	String fecha=dia+"/"+mes+"/"+anio;
    	/*if(log.isDebugEnabled()){
    		log.debug("BASEFECHAPARSEADA"+fecha);
    	}*/
        return fecha;
    }
    
    public static String parseDateInsertaCincoClaves(String date) throws ParseException {
    	//Date temp=DateUtils.parseDate(date, Constantes.DATE_FORMATS);
    	//log.debug("BASEFECHA5CLAVES "+date);
    	String mes=date.substring(0,2);
    	String dia=date.substring(3,5);
    	String anio=date.substring(6,8);
    	switch(Integer.parseInt(mes)){
		
    	case 1:
    		mes="Jan";
    		break;
    	case 2:
    		mes="Feb";
    		break;
    	case 3:
    		mes="Mar";
    		break;
    	case 4:
    		mes="Apr";
    		break;
    	case 5:
    		mes="May";
    		break;
    	case 6:
    		mes="Jun";
    		break;
    	case 7:
    		mes="Jul";
    		break;
    	case 8:
    		mes="Aug";
    		break;
    	case 9:
    		mes="Sep";
    		break;
    	case 10:
    		mes="Oct";
    		break;
    	case 11:
    		mes="Nov";
    		break;
    	case 12:
    		mes="Dec";
    		break;	
    	
    	}
    	String fecha=dia+"-"+mes+"-"+anio;
    	/*if(log.isDebugEnabled()){
    		log.debug("BASEFECHAPARSEADA"+fecha);
    	}*/
        return fecha;
    }

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(log.isDebugEnabled()){
        	log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!simple date format"+sdf.getCalendar().toString());
        }
        String temp =sdf.format(date);
        if(log.isDebugEnabled()){
        	log.debug("###########################String temp"+temp);
        }
        return temp;
    }
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if(log.isDebugEnabled()){
        	log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!simple date format"+sdf.getCalendar().toString());
        }
        String temp =sdf.format(date);
        if(log.isDebugEnabled()){
        	log.debug("###########################String temp"+temp);
        }
        return temp;
    }

//    public static String formatingFromExt(String extDate){
//    	
//    	return extDate;
//    }
    /**
     * M�todo que valida si la cadena es valida para una fecha, de acuerdo a la constante de fechas FormatConstants.DATE_FORMAT,
     * que es "dd/MM/yyyy"
     * 
     * @param cadena con los caracteres de la fecha
     * @return true si es valida, false si no
     */
    public static boolean isValidDate(String cadena) {

        boolean fechaValida = false;

        if (cadena != null) {
            if (cadena.length() != 10) {
                fechaValida = false;
            } else {
                if (StringUtils.isNumeric(cadena.substring(0, 2)) && StringUtils.isNumeric(cadena.substring(3, 5)) && StringUtils.isNumeric(cadena.substring(6))
                        && cadena.charAt(2) == '/' && cadena.charAt(5) == '/' && NumberUtils.toInt(cadena.substring(0, 2)) > 0 && NumberUtils.toInt(cadena.substring(0, 2)) <= 31
                        && NumberUtils.toInt(cadena.substring(3, 5)) > 0 && NumberUtils.toInt(cadena.substring(3, 5)) <= 12 && NumberUtils.toInt(cadena.substring(6)) >= 1900
                        && NumberUtils.toInt(cadena.substring(6)) <= 2100) {

                    fechaValida = true;
                }
            }
        } else {
            fechaValida = false;
        }

        return fechaValida;
    }

}