package mx.com.aon.portal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielforradellas
 * Date: Oct 21, 2008
 * Time: 10:50:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SQLDateConverter implements Converter {

    public static  String MASCARA_DATE_ddMMYYYY="dd-MM-yyyy";
     public static  String MASCARA_DATE_ddMMYYYY_B="dd/MM/yyyy";
     public static  String MASCARA_DATE_YYYYMMdd="yyyy-MM-dd";
     public static  String MASCARA_TIMESTAMP="dd-MM-yyyy HH:mm:ss";
     public static  String MASCARA_TIMESTAMP_B="dd/MM/yyyy HH:mm:ss";


  /**
   * Default logger.
   */
  private static Logger logger = Logger.getLogger(SQLDateConverter.class);
  /**
   * Static definition with specified format.
   */
  protected static SimpleDateFormat formatDate = new SimpleDateFormat();
  /**
   * Flag to sinchronize the getInstance.
   */
  private static Object oSynchroFlag = new Object();

  /**
   * Create a {@link Converter} that will throw a {@link ConversionException}
   * if a conversion error occurs.
   */
  public SQLDateConverter() {
    this.defaultValue = null;
    this.useDefault = false;
  }

  /**
   * Create a {@link Converter} that will return the specified default value
   * if a conversion error occurs.
   *
   * @param defaultValue The default value to be returned
   */
  public SQLDateConverter(Date defaultValue) {
    this.defaultValue = defaultValue;
    this.useDefault = true;
  }



  /**
   * The default value specified to our Constructor, if any.
   */
  private Date defaultValue = null;
  /**
   * Should we return the default value on conversion errors?
   */
  private boolean useDefault = true;

  /**
   * Convert the specified input object into an output object of the
   * specified type.
   *
   * @param type Data type to which this value should be converted
   * @param value The input value to be converted
   *
   * @exception ConversionException if conversion cannot be performed
   *  successfully
   */
  public Object convert(Class type, Object value) {

    if (value == null) {
      if (useDefault) {
        return (defaultValue);
      } else {
        return null;
      }
    } else {

      if (value instanceof String) {
        try {
          String defaultPattern = null;
          String s = value.toString();
          // Guiones o Barras
          if(s.indexOf("/") >= 0){
            defaultPattern = MASCARA_DATE_ddMMYYYY_B;
          }
          else{
            // por default, viene con guiones
            defaultPattern = MASCARA_DATE_ddMMYYYY;
          }

          formatDate.applyPattern(defaultPattern);
          if (value.toString().trim().equalsIgnoreCase("")){
             return null;
          }
          Date finalParseDate = formatDate.parse(value.toString());

          return new java.sql.Date(finalParseDate.getTime());

        } catch (ParseException e) {
          logger.error("Error al convertir String a SQLDate: ",e);
          return (defaultValue);
        }
      }
      // Modific� MAP el 23-06-2004
      // Si es SQLDate, debe devolver el mismo valor
      if (value instanceof java.sql.Date){
          return value;
      }
      // Fin modificaci�n MAP

      return null;
    }
  }
}
