package mx.com.aon.portal.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;


public class UserStringConverter implements Converter, Serializable {
	
	private static final long serialVersionUID = 3171706196483683019L;

	private static Logger logger = Logger.getLogger(UserStringConverter.class);

  public static  String TABLA_CANAL_PRODUCTO="SAV_CANAL_PRODUCTOS";
  public static  String MASCARA_DATE_ddMMYYYY="dd-MM-yyyy";
  public static  String MASCARA_DATE_ddMMYYYY_B="dd/MM/yyyy";
  public static  String MASCARA_DATE_YYYYMMdd="yyyy-MM-dd";
  public static  String MASCARA_TIMESTAMP="dd-MM-yyyy HH:mm:ss";
  public static  String MASCARA_TIMESTAMP_B="dd/MM/yyyy HH:mm:ss";
  /**
   * The default value specified to our Constructor, if any.
   */
  private Object defaultValue = null;
  private String mask;
    

  /**
   * Should we return the default value on conversion errors?
   */
  private boolean useDefault = true;

  public UserStringConverter() {

    this.defaultValue = null;
    this.useDefault = false;

  }

  public UserStringConverter(String mask) {
    this.mask=mask;
  }


  public UserStringConverter(Object defaultValue) {

    this.defaultValue = defaultValue;
    this.useDefault = true;

  }


  public Object convert(Class type, Object value) {
    SimpleDateFormat fDate;
    if (this.mask==null){
    fDate = new SimpleDateFormat(MASCARA_DATE_ddMMYYYY);
    }else{
      fDate = new SimpleDateFormat(this.mask);

    }
    if (value != null) {
      if (value instanceof java.sql.Date) {
        java.sql.Date fecha = (java.sql.Date) value;
        return fDate.format(fecha);
      } else
      {
        // Este else fue agregado por MAP el 19-08-2004
        if (value instanceof java.sql.Timestamp) {
            java.sql.Timestamp fecha = (java.sql.Timestamp)value;
            // cambio la mascara
            fDate.applyPattern(MASCARA_TIMESTAMP);
            return fDate.format(fecha);
        } else {
          if (value instanceof java.lang.Long) {
              Long valueLong = (Long)value;
              java.sql.Timestamp fecha = new Timestamp(valueLong);
              // cambio la mascara
              fDate.applyPattern(mask);
              return fDate.format(fecha);
          }
          else
            {
              return value.toString();
          }
        }
      }
      }
    else {
          return "";
      }
  }

}
