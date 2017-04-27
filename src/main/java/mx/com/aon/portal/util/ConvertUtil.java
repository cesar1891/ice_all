package mx.com.aon.portal.util;

import java.sql.Date;
import java.sql.Timestamp;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielforradellas
 * Date: Oct 21, 2008
 * Time: 11:09:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConvertUtil {
    public static Date convertToDate(String value) throws ApplicationException {
        try {
            ThreadLocal<UserVO> tl = ConnectionCallInterceptor.getLocalUser();
            UserVO userVo = tl.get();
            return (Date)userVo.getConverterStringToDate().convert(java.sql.Date.class,value);
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }

    public static String convertToString(java.sql.Date value) throws RuntimeException {
        try {
            ThreadLocal<UserVO> tl = ConnectionCallInterceptor.getLocalUser();
            UserVO userVo = tl.get();
            return (String)userVo.getConverterToString().convert(java.lang.String.class,value);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }

    public static Object nvl (String value) throws ApplicationException {
        try {
            if (value == null )
                return null;
            if  (value.equals("")) { 
                return null;
            } else {
                return value;
            }

        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }

    /**
     *  convierte un String, que representa una fecha en el formato que el usuario
     * tiene configurado en el UserVO, a Timestamp
      * @param value
     * @return
     * @throws ApplicationException
     */
    public static Timestamp convertToTimeStamp(String value) throws ApplicationException {
        try {
            ThreadLocal<UserVO> tl = ConnectionCallInterceptor.getLocalUser();
            UserVO userVo = tl.get();
            return (Timestamp)userVo.getConverterStringToTimestamp().convert(Timestamp.class,value);
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }

    /**
     * convierte un Long, que representa un timestamp, a un String ue representa una fecha ,
     * en el formato que el usuario tiene confgurado en el UserVO
     * @param value
     * @return   un string que corresponde a la fecha segun formato del usuario
     * @throws ApplicationException
     */
    public static String convertToString(Long value) throws ApplicationException {
        try {
            ThreadLocal<UserVO> tl = ConnectionCallInterceptor.getLocalUser();
            UserVO userVo = tl.get();
            return (String)userVo.getConverterToString().convert(Long.class,value);
        } catch (Exception ex) {
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }

}