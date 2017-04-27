/**
 * 
 */
package mx.com.aon.tmp;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mx.com.gseguros.utils.Constantes;


/**
 * @author eflores
 * @date 27/10/2008
 *
 */
public class AONCatwebUtils {

    /**
     * Descripci&oacute;n: Metodo que convierte una fecha de tipo date a String
     * @param date Fecha de tipo Date
     * @param pattern Patron a convertir la fecga por ejemplo dd/MM/yyyy
     * @return Regresa una fecha de tipo String
     */
    public static String format(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,new Locale("es","MX"));     
        return sdf.format(date);        
    }
    
    /**
     * Descripci&oacute;n: Metodo que convierte una fecha de tipo String a Date
     * @param date Fecha a comvertir a Date
     * @param pattern patron en que se encuentra la fecha en String por ejemplo dd/MM/yyyy
     * @return Regresa la fecha en tipo Date
     * @throws ParseException
     */
    public static Date parse(String date,String pattern) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,new Locale("es","MX"));
        return sdf.parse(date);
    }
    
    /**
     * Regresa el numero formateado
     * 
     * @return Numero con el formato correcto return String
     */
    public static String formatNumber(BigDecimal originalNumber) {
        String newNumber = "0.00";
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols sym = new DecimalFormatSymbols();
        sym.setDecimalSeparator('.');
        sym.setGroupingSeparator(',');
        formatter.setGroupingUsed(true);
        formatter.setDecimalFormatSymbols(sym);
        formatter.setMaximumFractionDigits(2);
        try {
            newNumber = formatter.format(originalNumber.doubleValue());
        } catch (NumberFormatException e) {
            return originalNumber + "";
        }
        return newNumber;
    }
    
    /**
     * Regresa la fecha actual en el formato estandar
     * @return
     */
    public static String getDateNow() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(Constantes.FORMATO_FECHA);
        return formatter.format(now.getTime());
    }
}
