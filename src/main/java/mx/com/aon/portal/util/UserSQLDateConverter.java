package mx.com.aon.portal.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.com.aon.portal.model.UserVO;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielforradellas
 * Date: Oct 21, 2008
 * Time: 10:50:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserSQLDateConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1880382166958715192L;
	
	public static  String MASCARA_DATE_ddMMYYYY="dd-MM-yyyy";
    public static  String MASCARA_DATE_ddMMYYYY_B="dd/MM/yyyy";
    public static  String MASCARA_DATE_YYYYMMdd="yyyy-MM-dd";
    public static  String MASCARA_TIMESTAMP="dd-MM-yyyy HH:mm:ss";
    public static  String MASCARA_TIMESTAMP_B="dd/MM/yyyy HH:mm:ss";


  /**
   * Default logger.
   */
  private static Logger logger = Logger.getLogger(UserSQLDateConverter.class);
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
  public UserSQLDateConverter(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * Create a {@link org.apache.commons.beanutils.Converter} that will return the specified default value
   * if a conversion error occurs.
   *
   * @param defaultValue The default value to be returned
   */
  public UserSQLDateConverter(Date defaultValue,String dateFormat) {
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


  public String obtenerUserDateClientFormat () {
		UserVO userVO = new UserVO();
		ThreadLocal<UserVO> tl = ConnectionCallInterceptor.getLocalUser();
		userVO = tl.get();

		String clientDateFormat = userVO.getClientFormatDate().toLowerCase();
		clientDateFormat = clientDateFormat.replace("d", "dd");
		clientDateFormat = clientDateFormat.replace("m", "MM");
		clientDateFormat = clientDateFormat.replace("y", "yyyy");

		return clientDateFormat;
  }

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

          logger.debug("En UserSQLDateConverter dateFormat: " + this.dateFormat);
          if (this.dateFormat == null || this.dateFormat.equals("")) {
	          if (obtenerUserDateClientFormat() == null || obtenerUserDateClientFormat().equals("")) {
	        	  defaultPattern = MASCARA_DATE_ddMMYYYY_B;
	          }else defaultPattern = obtenerUserDateClientFormat();
          }else {
        	  defaultPattern = dateFormat;
          }
          //defaultPattern = dateFormat;

          logger.debug("En UserSQLDateConverter se usara: " + defaultPattern);
          formatDate.applyPattern(defaultPattern);
          if (value.toString().trim().equalsIgnoreCase("")){
             return null;
          }
          Date finalParseDate = formatDate.parse(value.toString());

          return new java.sql.Date(finalParseDate.getTime());

        } catch (ParseException e) {
          logger.error("Error al convertir String a SQLDate: ",e);
          throw new RuntimeException("Formato de fecha invalido. Formato esperado: "+ dateFormat +" ,valor ingresado: "+ value,e);
//          return (defaultValue);
        }
      }
      // Modifica MAP el 23-06-2004
      // Si es SQLDate, debe devolver el mismo valor
      if (value instanceof java.sql.Date){
          return value;
      }
      // Fin modificacion MAP

      return null;
    }
  }
}
