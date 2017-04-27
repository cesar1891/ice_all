package mx.com.aon.portal.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

public class UserTimestampConverter implements Converter, Serializable {

    private static final long serialVersionUID = 4283347706841884222L;
	
	public static  String MASCARA_DATE_ddMMYYYY="dd-MM-yyyy";
    public static  String MASCARA_DATE_ddMMYYYY_B="dd/MM/yyyy";
    public static  String MASCARA_DATE_YYYYMMdd="yyyy-MM-dd";
    public static  String MASCARA_TIMESTAMP="dd-MM-yyyy HH:mm:ss";
    public static  String MASCARA_TIMESTAMP_B="dd/MM/yyyy HH:mm:ss";


  /**
   * Default logger.
   */
  private static Logger logger = Logger.getLogger(UserTimestampConverter.class);
  /**
   * Static definition with specified format.
   */
  protected static SimpleDateFormat formatDate = new SimpleDateFormat();

  private String dateFormat;
  /**
   * Flag to sinchronize the getInstance.
   */
  private static Object oSynchroFlag = new Object();

  /**
   * Create a {@link org.apache.commons.beanutils.Converter} that will throw a {@link org.apache.commons.beanutils.ConversionException}
   * if a conversion error occurs.
   */
  public UserTimestampConverter(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * Create a {@link org.apache.commons.beanutils.Converter} that will return the specified default value
   * if a conversion error occurs.
   *
   * @param defaultValue The default value to be returned
   */
  public UserTimestampConverter(Date defaultValue,String dateFormat) {
    this.defaultValue = defaultValue;
    this.dateFormat = dateFormat;
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
       * @exception org.apache.commons.beanutils.ConversionException if conversion cannot be performed
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
              defaultPattern = dateFormat;

              formatDate.applyPattern(defaultPattern);
              if (value.toString().trim().equalsIgnoreCase("")){
                 return null;
              }
              Timestamp finalParseTimestamp = new Timestamp(formatDate.parse(value.toString()).getTime());

              return finalParseTimestamp;

            } catch (ParseException e) {
                logger.error("Error al convertir String a Timestamp: ",e);
                throw new RuntimeException("Formato de fecha invalido. Formato esperado: "+ dateFormat +" ,valor ingresado: "+ value,e);
            }
          }
          // Si es SQLTimestamp, debe devolver el mismo valor
          if (value instanceof java.sql.Timestamp){
              return value;
          }
          // Cualquier otra cosa no puede convertirse
          return null;
        }
      }


}
