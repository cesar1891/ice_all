package mx.com.gseguros.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.ValidationDataException;

/**
 * Utiler&iacute;as para validar datos de la aplicacion
 * @author Ricardo
 *
 */
public class Utils
{
	
	private static Logger           logger              = LoggerFactory.getLogger(Utils.class);
	private static SimpleDateFormat renderFechas        = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat renderFechasConHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static String           stringDateFormat    = "dd/MM/yyyy";
	private static String           TIMEZONE_DEFAULT_ID = "GMT0"; //Se manda TimeZone a 0 para los WS de ice2sigs, pues se movia un dia.
	
	/**
	 * Lanza una excepcion cuando la cadena es null o vac�a
	 * 
	 * @param cadena Elemento a validar
	 * @param mensaje Mensaje agregado a la excepcion
	 * @throws ValidationDataException
	 */
	public static void validate(String cadena, String mensaje) throws ValidationDataException {
		if(StringUtils.isBlank(cadena)) {
			throw new ValidationDataException(mensaje);
		}
	}
	
	
	/**
	 * Lanza una excepcion cuando el objeto es null
	 * 
	 * @param objeto Elemento a validar
	 * @param mensaje Mensaje agregado a la excepcion
	 * @throws ValidationDataException
	 */
	public static void validate(Object objeto, String mensaje) throws ValidationDataException {
		if(objeto==null) {
			throw new ValidationDataException(mensaje);
		}
	}
	
	
	/**
	 * Lanza una excepcion cuando la coleccion es null o vacia
	 * 
	 * @param coll Coleccion a validar
	 * @param mensaje Mensaje agregado a la excepcion
	 * @throws ValidationDataException
	 */
	public static void validate(Collection<?> coll, String mensaje) throws ValidationDataException {
		if(CollectionUtils.isEmpty(coll)) {
			throw new ValidationDataException(mensaje);
		}
	}
	
	
	/**
	 * Lanza una excepcion cuando el mapa es null o vacio
	 * 
	 * @param map Mapa a validar
	 * @param mensaje Mensaje agregado a la excepcion
	 * @throws ValidationDataException
	 */
	public static void validate(Map<?,?> map, String mensaje) throws ValidationDataException {
		if(MapUtils.isEmpty(map)) {
			throw new ValidationDataException(mensaje);
		}
	}
	
	
	/**
	 * Lanza una excepcion cuando la condicion es null o false
	 * 
	 * @param condicion Elemento a validar
	 * @param mensaje Mensaje agregado a la excepcion
	 * @throws ValidationDataException
	 */
	public static void validate(Boolean condicion, String mensaje) throws ValidationDataException {
		if(BooleanUtils.isNotTrue(condicion)) {
			throw new ValidationDataException(mensaje);
		}
	}
	
	
	/**
	 * Maneja excepciones de la aplicacion y devuelve el mensaje de error correspondiente
	 * @param e
	 * @return
	 */
	public static String manejaExcepcion(Exception e)
	{		
		String respuesta;
		if(e instanceof ApplicationException)
		{
			respuesta = new StringBuilder(e.getMessage()).append(" #").append(System.currentTimeMillis()).toString();
			if(StringUtils.isNotBlank(((ApplicationException)e).getTraza()))
			{
				logger.error(StringUtils.join((((ApplicationException) e).getTraza()),"\n",respuesta),e);
			}
			else
			{
				logger.error(respuesta, e);
			}
		}
		else
		{
			respuesta = new StringBuilder("Error del sistema #").append(System.currentTimeMillis()).toString();
			logger.error(respuesta, e);
		}
		return respuesta;
	}
	
	/**
	 * Genera una ApplicationException o la lanza si existe
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public static ApplicationException generaExcepcion(Exception e,String paso) throws Exception {
		
		if(e instanceof ApplicationException) {
			throw e;
		} else {
			throw new ApplicationException(Utils.join("Error en el proceso: ",paso), e);
		}
	}
	
	
	/**
	 * Obtiene el tama�o de una lista (null safe)
	 * @param lista
	 * @return Devuelve el tama&ntilde;o de la lista o null
	 */
	public static Integer size(Collection<?> lista) {
		if(lista == null) {
			return null;
		} else {
			return CollectionUtils.size(lista);
		}
	}
	
	public static String convierteListaEnXml(List<Map<String,String>>lista)
	{
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><lista>");
		for(Map<String,String>registro:lista)
		{
			sb.append("<registro>");
			for(Entry<String,String>en:registro.entrySet())
			{
				sb.append("<")
				  .append(en.getKey())
				  .append(">")
				  .append(en.getValue()!=null?en.getValue():"")
				  .append("</")
				  .append(en.getKey())
				  .append(">");
			}
			sb.append("</registro>");
		}
		return sb.append("</lista>").toString();
	}
	
	public static String generaTimestamp()
	{
		return Long.toString(System.currentTimeMillis());
	}
	
	public static UserVO validateSession(Map<String,Object>session)throws Exception
	{
		Utils.validate(session                , "No hay sesion");
		Utils.validate(session.get("USUARIO") , "No hay usuario en la sesion");
		return (UserVO)session.get("USUARIO");
	}
	
	public static void validate(String... args) throws ValidationDataException
	{
		for(int i=0;i<args.length;i=i+2)
		{
			Utils.validate(args[i],args[i+1]);
		}
	}
	
	public static ApplicationException generaExcepcion(Exception e,String paso,String traza) throws Exception
	{		
		if(e instanceof ApplicationException)
		{
			((ApplicationException)e).setTraza(traza);
			throw e;
		}
		else
		{
			throw new ApplicationException(Utils.join("Error en el proceso: ",paso), e, traza);
		}
	}

	@Deprecated
	public static void validate(StringBuilder sb, String cadena, String mensaje) throws ValidationDataException {
		if(StringUtils.isBlank(cadena)) {
			throw new ValidationDataException(mensaje,sb.toString());
		}
	}
	
	@Deprecated
	public static void validate(StringBuilder sb, String... args) throws ValidationDataException
	{
		for(int i=0;i<args.length;i=i+2)
		{
			Utils.validate(sb, args[i] , args[i+1]);
		}
	}
	
	@Deprecated
	public static void validate(StringBuilder sb, Map<?,?> map, String mensaje) throws ValidationDataException {
		if(MapUtils.isEmpty(map)) {
			throw new ValidationDataException(mensaje,sb.toString());
		}
	}
	
	/**
	 * Formatea un string con el contenido de una fecha
	 * @param fecha Fecha formateada
	 * @return
	 */
	public static String formateaFecha(String fecha) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			if(fecha!=null && fecha.length() > 10) {
				// 1990-12-01_00:00:00.0 <<CADENA
				// 0123456789k           <<INDICE
				strBuilder.append(fecha.substring(8, 10)).append("/")
						.append(fecha.substring(5, 7)).append("/")
						.append(fecha.substring(0, 4));
			} else {
				strBuilder.append(fecha);
			}
		} catch (Exception e) {
			logger.error("Error al formatear la fecha", e);
		}
		//logger.debug("#debug fecha="+strBuilder);
		return strBuilder.toString();
	}
	
	/**
	 * Formatea un string con el contenido de una fecha
	 * @param fecha Fecha formateada
	 * @return
	 */
	public static String formateaFechaConHora(String fecha) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			if(fecha!=null && fecha.length() > 10) {
				//  1 9 9 0 - 1 2 - 0 1 _ 0 0 : 0 0 : 00.0 <<CADENA
				// 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6       <<INDICE
				strBuilder.append(fecha.substring(8, 10)).append("/")
						.append(fecha.substring(5, 7)).append("/")
						.append(fecha.substring(0, 4))
						.append(" ")
						.append(fecha.substring(11,16));
			} else {
				strBuilder.append(fecha);
			}
		} catch (Exception e) {
			logger.error("Error al formatear la fecha", e);
		}
		//logger.debug("#debug fecha="+strBuilder);
		return strBuilder.toString();
	}
	
	/**
	 * Convierte una fecha String a Calendar
	 * @param fecha String
	 * @return devuelve objeto Calendar
	 */
	public static Calendar getCalendarTimeZone0(String fecha, String dateFormat) {
		
		Calendar  cal = null;
		
		try {
			//Asumimos que tiene formato dd/MM/YYYY
			//TODO:preparar este metodo  para otros formatos
			if(Utils.esFechaValida(fecha, dateFormat)){
				String [] fechaArr = fecha.split("/");   
				int dia  =  Integer.parseInt(fechaArr[0]);
				int mes  =  Integer.parseInt(fechaArr[1])-1;
				int anio =  Integer.parseInt(fechaArr[2]);
				cal = Calendar.getInstance();
				cal.set(anio, mes, dia);
				cal.setTimeZone(TimeZone.getTimeZone(TIMEZONE_DEFAULT_ID));
				logger.debug("TIMEZONE_DEFAULT_ID" + TIMEZONE_DEFAULT_ID);
			}
		} catch (Exception e) {
			logger.error("Error al formatear la fecha", e);
		}
		
		return cal;
	}
	
	/**
	 * Convierte una fecha String a Calendar con el TimeZone que ya trae el Server
	 * @param fecha String
	 * @return devuelve objeto Calendar
	 */
	public static Calendar getCalendarServerTimeZone(String fecha, String dateFormat) {
		
		Calendar  cal = null;
		
		try {
			//Asumimos que tiene formato dd/MM/YYYY
			//TODO:preparar este metodo  para otros formatos
			if(Utils.esFechaValida(fecha, dateFormat)){
				String [] fechaArr = fecha.split("/");   
				int dia  =  Integer.parseInt(fechaArr[0]);
				int mes  =  Integer.parseInt(fechaArr[1])-1;
				int anio =  Integer.parseInt(fechaArr[2]);
				cal = Calendar.getInstance();
				cal.set(anio, mes, dia);
			}
		} catch (Exception e) {
			logger.error("Error al formatear la fecha", e);
		}
		
		return cal;
	}

	/**
	 * Convierte una fecha String a XMLGregorianCalendar
	 * @param fecha String dd/MM/YYYY
	 * @return devuelve objeto XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar getXmlGregCalendar(String fecha, String dateFormat) {
		
		XMLGregorianCalendar xmlCal = null;
		
		try {
			//Asumimos que tiene formato dd/MM/YYYY
			//TODO:preparar este metodo  para otros formatos
			if(Utils.esFechaValida(fecha, dateFormat)){
				String [] fechaArr = fecha.split("/");   
				int dia  =  Integer.parseInt(fechaArr[0]);
				int mes  =  Integer.parseInt(fechaArr[1])-1;
				int anio =  Integer.parseInt(fechaArr[2]);

				xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
				xmlCal.setDay(dia);
				xmlCal.setMonth(mes);
				xmlCal.setYear(anio);
			}
		} catch (Exception e) {
			logger.error("Error al formatear la fecha", e);
		}
		
		return xmlCal;
	}
	
	public static boolean esFechaValida(String dateToValidate, String dateFormat){
		 
		if(dateToValidate == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
 
		try {
			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Indica si un email tiene formato valido
	 * @param email email a validar
	 * @return true si es valido, false si no
	 */
	public static boolean esEmailValido(String email){
		
		String emailRegExp = "(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})";
        Pattern pattern = Pattern.compile(emailRegExp);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
	}

	public static void main(String[] args)
	{
		//System.out.println(Utils.getCalendarServerTimeZone("11/25/2014", "dd/MM/yyyy"));
		//System.out.println(Utils.esFechaValida("11/25/2014", "dd/MM/yyyy"));
		Utils.calcularFechaLaboral(Calendar.getInstance(),(60*4)+(60*8*8));
	}
	
	/*
	public static String formateaNumero(String numero)
	{
		String num="";
		if(numero!=null)
		{
			int l=numero.length();
			for(int p=l-1,i=0;p>=0;p--,i++)
			{
				//p es posicion
				//i es iteracion
				String digito=numero.substring(p, p+1);
				String regex="\\d";
				if(digito.matches(regex))//copiar digito
				{
					num+=digito;
				}
				else if(i<=2)//0 ó 1 ó 2
				{
					num+=".";
				}
			}
			String tmp="";
			for(int i=num.length()-1;i>=0;i--)
			{
				tmp+=num.charAt(i);
			}
			num=tmp;
		}
		logger.debug("formateaNumero entrada: "+numero+", salida:"+num);
		return num;
	}
	*/
	
	/**
	 * RECIBE UN MAPA DE TIPO STRING Y PONE TODAS LAS LLAVES QUE ENPIECEN CON "FE" Y "PV_FE" COMO TIPO DATE
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> ponFechas(Map<String,String>params) throws Exception
	{
		Map<String,Object> omap=new HashMap<String,Object>(0);
		if(params!=null)
		{
			for(Entry<String,String> en:params.entrySet())
			{
				String llave=(String)en.getKey();
				if((llave.length()>=2&&llave.substring(0,2).equalsIgnoreCase("fe"))||(llave.length()>=5&&llave.substring(0,5).equalsIgnoreCase("pv_fe")))
				{
					String fecha=en.getValue();
					if(fecha!=null&&fecha.length()>0)
					{
//						omap.put(llave,renderFechas.parse(fecha));
						omap.put( llave, getCalendarServerTimeZone(fecha, stringDateFormat).getTime() );
					}
					else
					{
						omap.put(llave,null);
					}
				}
				else
				{
					omap.put(llave,en.getValue());
				}
			}
		}
		logger.debug(new StringBuilder("ponFechas ").append(params).append("=INTO=").append(omap).toString());
		return omap;
	}
	
	
	/**
	 * Indica si el User-Agent recibido corresponde a un dispositivo m&oacute;vil o no
	 * @param userAgent User-Agent del cliente que realiz&oacute; la petici&oacute;n
	 * @return true si el origen de la petici&oacute;n es de un dispositivo m&oacute;vil, false si no 
	 */
	public static boolean esSesionMovil(String userAgent) {
		boolean esMovil=false;
		// Revisamos si el user agent del browser corresponde a un dispositivo movil:
		String regExMobile1 = "(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|android|blackberry|playbook|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od|ad)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*";
		String regExMobile2 = "(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-";
		esMovil=userAgent.matches(regExMobile1) || userAgent.substring(0, 4).matches(regExMobile2);
		return esMovil;
	}

	public static String join(Object... args)
	{
		StringBuilder sb = new StringBuilder();
		for(Object arg:args)
		{
			sb.append(arg);
		}
		return sb.toString();
	}

	public static String log(Object... args)
	{
		boolean       debug = false;
		StringBuilder sb    = new StringBuilder();
		
		boolean vieneStamp = args[0] instanceof Long;
		
		if(vieneStamp)
		{
			sb.append("\n[").append(args[0]).append("]: ");
		}
		
		for(Object arg:args)
		{
			try
			{
				if(arg!=null && arg instanceof List)
				{
					sb.append("<tamanio de lista ").append(((List)arg).size()).append(">");
				}
				
				if(arg==null)
				{
					sb.append("null");
				}
				else if(arg.toString().length()>1000&&!debug)
				{
					sb.append(arg.toString().substring(0, 1000)).append("...");
				}
				else
				{
					sb.append(arg);
				}
			}
			catch(Exception ex)
			{
			    sb.append(arg);
			}
		}
		
		if(vieneStamp)
		{
			sb.append(" ^[").append(args[0]).append("]");
		}
		
		return sb.toString();
	}
	
	public static Map<String,String>concatenarParametros(Map<String,String>otvalor,boolean filtrar)
	{
		Map<String,String>concat=new LinkedHashMap<String,String>();
		for(Entry<String,String>en:otvalor.entrySet())
		{
			String key = en.getKey();
			String val = en.getValue();
			if(StringUtils.isBlank(val))
			{
				val="";
			}
			if(StringUtils.isNotBlank(key)
					&&key.length()>"otvalor".length()
					&&key.substring(0,"otvalor".length()).equalsIgnoreCase("otvalor")
					)
			{
				concat.put(Utils.join("parametros.pv_otvalor",key.substring(key.length()-2)),val);
			}
			else if(!filtrar)
			{
				concat.put(key,val);
			}
		}
		logger.debug(Utils.log("\nconcatenarParametros ",otvalor,"\nen ",concat));
		return concat;
	}
	
	public static List<Map<String,String>>concatenarParametros(List<Map<String,String>>lista,boolean filtrar)
	{
		List<Map<String,String>>concat=new ArrayList<Map<String,String>>();
		for(Map<String,String>otvalor:lista)
		{
			concat.add(Utils.concatenarParametros(otvalor,filtrar));
		}
		return concat;
	}
	
	@Deprecated
	public static void debugProcedure(org.apache.log4j.Logger logger2,String storedProcedureName,Map<String,?>params)
    {
		return;
		/*int len = storedProcedureName.length();
		logger.debug
		(
				Utils.log
				(
				 "\n*******",StringUtils.leftPad("",len,"*"),"*******"
				,"\n****** ",storedProcedureName," ******"
				,"\n****** params=",params
				,"\n*******",StringUtils.leftPad("",len,"*"),"*******"
				)
		);*/
    }
    
	@Deprecated
    public static void debugProcedure(org.apache.log4j.Logger logger2,String storedProcedureName,Map<String,?>params,List<?>lista)
    {
    	return;
    	/*int len = storedProcedureName.length();
    	logger.debug
    	(
    			Utils.log
    			(
    					 "\n*******",StringUtils.leftPad("",len,"*"),"*******"
    					,"\n****** params=",params
    					,"\n****** registro=",lista
    					,"\n****** ",storedProcedureName," ******"
    					,"\n*******",StringUtils.leftPad("",len,"*"),"*******"
    			)
    	);*/
    }
	
	@Deprecated
	public static void debugProcedure(Logger logger2,String storedProcedureName,Map<String,?>params)
    {
		return;
		/*int len = storedProcedureName.length();
		logger.debug
		(
				Utils.log
				(
				 "\n*******",StringUtils.leftPad("",len,"*"),"*******"
				,"\n****** ",storedProcedureName," ******"
				,"\n****** params=",params
				,"\n*******",StringUtils.leftPad("",len,"*"),"*******"
				)
		);*/
    }
    
	@Deprecated
    public static void debugProcedure(Logger logger2,String storedProcedureName,Map<String,?>params,List<?>lista)
    {
    	return;
    	/*int len = storedProcedureName.length();
    	logger.debug
    	(
    			Utils.log
    			(
    					 "\n*******",StringUtils.leftPad("",len,"*"),"*******"
    					,"\n****** params=",params
    					,"\n****** registro=",lista
    					,"\n****** ",storedProcedureName," ******"
    					,"\n*******",StringUtils.leftPad("",len,"*"),"*******"
    			)
    	);*/
    }
    
    public static Date parse(String fecha) throws Exception
    {
        if (fecha == null) {
            return null;
        }
    	return renderFechas.parse(fecha);
    }
    
    public static String format(Date fecha) throws Exception
    {
    	return renderFechas.format(fecha);
    }
    
    public static String formatConHora(Date fecha) throws Exception
    {
    	return renderFechasConHora.format(fecha);
    }
    
    public static String[][] convierteMapasEnArreglos(List<Map<String,String>>lista)
    {
    	String[][] arreglos = new String[lista.size()][];
    	int i = 0;
    	for(Map<String,String>elem:lista)
    	{
    		arreglos[i++] = Utils.convierteMapaEnArreglo(elem);
    	}
    	return arreglos;
    }
    
    public static String[] convierteMapaEnArreglo(Map<String,String>mapa)
    {
    	String[] arreglo = new String[mapa.entrySet().size()];
    	int i = 0;
    	for(Entry<String,String>en:mapa.entrySet())
    	{
    		arreglo[i++] = en.getValue();
    	}
    	logger.debug("\nEntrada {}\nSalida {}",mapa,arreglo);
    	return arreglo;
    }
    
    public static boolean isRowEmpty(Row row)
    {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++)
        {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
            {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Obtiene el valor de una celda como String
     * 
     * @param celda Celda que contiene el valor
     * @return Valor de la celda, o cadena vacia en caso de error
     */
    public static String getCellValue(Cell celda) {
        String strValor = "";
        try {
            if(celda.getCellType() == Cell.CELL_TYPE_STRING) {
                strValor = celda.getStringCellValue();
            } else if(celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                strValor = String.valueOf(celda.getNumericCellValue());
            }
        } catch (Exception e) {
            logger.warn("Error al obtener valor de la celda, se devuelve cadena vacia", e);
        }
        return strValor;
    }
    
    
    public static String NVL(String origen, String reemplazo)
    {
    	if(StringUtils.isBlank(origen))
    		return reemplazo;
    	return origen;
    }
    
    /**
     * 
     * @param inicio fecha de inicio
     * @param minutos que se deben sumar a esa fecha
     * @return la fecha final
     */
    public static Calendar calcularFechaLaboral(Calendar inicio, int minutos)
    {
    	StringBuilder sb = new StringBuilder(Utils.log(
    			 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			,"\n@@@@@@ calcularFechaLaboral @@@@@@"
    			,"\n@@@@@@ inicio=", inicio, "\n@@@@@@minutos=", minutos, "\n"
    			));
    	
    	Calendar fin = (Calendar)inicio.clone();
    	
    	while(minutos>0)
    	{
    		sb.append(Utils.log("\nIteracion minutos a sumar=", minutos, ", fin=", fin, "\n"));
    		
    		int minutosFin = (fin.get(Calendar.HOUR_OF_DAY)*60)+fin.get(Calendar.MINUTE);
    		sb.append(Utils.log("\nMinutos fin=", minutosFin));
    		
    		int restanteDia = 1020 - minutosFin;
    		sb.append(Utils.log("\nRestante dia=", restanteDia));
    		
    		int minutosSumados = 0;
    		
    		boolean sumarDia = false;
    		
    		if(minutos > restanteDia)
    		{
    			sumarDia        = true;
    			minutosSumados = restanteDia;
    			sb.append(Utils.log("\nLos minutos que quiero restar superan lo disponible del dia, le resto solo lo del dia: ", minutosSumados));
    		}
    		else
    		{
    			minutosSumados = minutos;
    			sb.append(Utils.log("\nLos minutos que quiero restar entran en el dia: ", minutosSumados));
    		}
    		
    		minutos -= minutosSumados;
    		
    		if(sumarDia)
    		{
    			sb.append(Utils.log("\nVoy a sumar un dia a:", fin, "\n"));
    			fin.set(Calendar.HOUR_OF_DAY, 9);
    			fin.set(Calendar.MINUTE, 0);
    			if(fin.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)
    			{
    				fin.add(Calendar.DATE, 3);
    				sb.append(Utils.log("\nSe paso de viernes a lunes:", fin, "\n"));
    			}
    			else if(fin.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
    			{
    				fin.add(Calendar.DATE, 2);
    				sb.append(Utils.log("\nSe paso de sabado a lunes:", fin, "\n"));
    			}
    			else
    			{
    				fin.add(Calendar.DATE, 1);
    				sb.append(Utils.log("\nSe sumo el dia simple:", fin, "\n"));
    			}
    		}
    		else
    		{
    			sb.append(Utils.log("\nSumare solo los minutos restados:", minutosSumados));
    			fin.add(Calendar.MINUTE, minutosSumados);
    		}
    	}
    	
    	sb.append(Utils.log(
    			 "\n"
    			,"\n@@@@@@ fin=", fin
    			,"\n@@@@@@ calcularFechaLaboral @@@@@@"
    			,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
    			));
    	
    	//logger.debug(sb.toString());
    	
    	return fin;
    }
    
    public static Date parseConHora(String fecha) throws Exception
    {
    	return renderFechasConHora.parse(fecha);
    }
    
    public static String cambiaAcentosUnicodePorGuionesBajos(String texto) {
    	return texto
    			.replace("\u00e1", "_a_")
    			.replace("\u00e9", "_e_")
				.replace("\u00ed", "_i_")
				.replace("\u00f3", "_o_")
				.replace("\u00fa", "_u_")
				.replace("\u00c1", "_A_")
				.replace("\u00c9", "_E_")
				.replace("\u00cd", "_I_")
				.replace("\u00d3", "_O_")
				.replace("\u00da", "_U_");
    }
    
    public static String cambiaGuionesBajosPorAcentosUnicode(String texto) {
    	return texto
    			.replace("_a_", "\u00e1")
    			.replace("_e_", "\u00e9")
				.replace("_i_", "\u00ed")
				.replace("_o_", "\u00f3")
				.replace("_u_", "\u00fa")
				.replace("_A_", "\u00c1")
				.replace("_E_", "\u00c9")
				.replace("_I_", "\u00cd")
				.replace("_O_", "\u00d3")
				.replace("_U_", "\u00da");
    }
    
    public static String cambiaGuionesBajosPorAcentosHtml(String texto) {
    	return texto
    			.replace("_a_", "&aacute;")
    			.replace("_e_", "&eacute;")
				.replace("_i_", "&iacute;")
				.replace("_o_", "&oacute;")
				.replace("_u_", "&uacute;")
				.replace("_A_", "&Aacute;")
				.replace("_E_", "&Eacute;")
				.replace("_I_", "&Iacute;")
				.replace("_O_", "&Oacute;")
				.replace("_U_", "&Uacute;");
    }
    
    public static String cambiaAcentosUnicodePorAcentosHtml(String texto) {
    	return texto
    			.replace("\u00e1", "&aacute;")
    			.replace("\u00e9", "&eacute;")
				.replace("\u00ed", "&iacute;")
				.replace("\u00f3", "&oacute;")
				.replace("\u00fa", "&uacute;")
				.replace("\u00c1", "&Aacute;")
				.replace("\u00c9", "&Eacute;")
				.replace("\u00cd", "&Iacute;")
				.replace("\u00d3", "&Oacute;")
				.replace("\u00da", "&Uacute;");
    }
    
    public static String convierteTextfieldCodificadoEnMD5(String cadena) throws Exception{
    	StringBuilder dec = new StringBuilder();
    	long token = Long.valueOf(cadena.substring(0, 5));
    	cadena = cadena.substring(5);
    	long _char;
    	while (cadena.length() > 0) {
    		_char = Long.valueOf(cadena.substring(0, 20));
    		cadena = cadena.substring(20);
    		dec.append(Character.toString((char) ((int) _char / token)));
    	}
    	cadena = dec.toString();
    	// convertir a MD5 https://www.mkyong.com/java/java-md5-hashing-example/
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	md.update(cadena.getBytes());
    	byte byteData[] = md.digest();
    	//convert the byte to hex format method 1
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < byteData.length; i++) {
    		sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    	}
    	return sb.toString();
    }
    
    /**
	 * Formatea un long que representa una fecha en milisegundos a un string con el contenido de una fecha
	 * @param fecha
	 * @return
	 */
	public static String formateaFechaMilisegundos(long fecha) {
		String sFecha="";
		try {
			final Calendar cal = Calendar.getInstance();
        	cal.setTimeInMillis(fecha);
        	final SimpleDateFormat sdfParser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("ES"));
        	sFecha = sdfParser.format(cal.getTime());
		} catch (Exception e) {
			logger.error("Error al formateaFechaMilisegundos ", e);
		}
		return sFecha;
	}
	
}