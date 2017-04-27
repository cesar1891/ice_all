package mx.com.gseguros.portal.catalogos.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.endosos.controller.EndososAction;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneralRespuesta;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService.Estatus;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;

public class PersonasAction extends PrincipalCoreAction
{

	private boolean                  exito            = false;
	private Map<String,Item>         imap             = null;
	private final Logger             logger           = Logger.getLogger(PersonasAction.class); 
	private PersonasManager          personasManager;
	private SimpleDateFormat         renderFechas     = new SimpleDateFormat("dd/MM/yyyy");
	private String                   respuesta;
	private String                   respuestaOculta  = null;
	private static final long        serialVersionUID = -5438595581905207477L;
	private List<Map<String,String>> slist1;
	private List<Map<String,String>> saveList;
	private List<Map<String,String>> updateList;
	private List<Map<String,String>> deleteList;
	private Map<String,String>       params;
	private Map<String,String>       smap1;
	private Map<String,String>       smap2;
	private Map<String,String>       smap3;
	
	private FlujoVO flujo;
	
	@Autowired
	private transient Ice2sigsService ice2sigsService;
	private boolean personaWS;
	
	@Autowired
	private EndososManager endososManager;
	
	public PersonasAction()
	{
		logger.debug("new PersonasAction");
		this.session=ActionContext.getContext().getSession();
	}
	
	/**
	 * Carga los elementos de la pantalla de asegurados
	 * @return SUCCESS/ERROR
	 */
	public String pantallaPersonas()
	{
		long timestamp = System.currentTimeMillis();
		String result  = null;
		logger.info(timestamp
				+ "\n##############################"
				+ "\n###### pantallaPersonas ######"
				+ "\nsmap1 "+smap1
				);
		try
		{
			Map<String,Object>managerResult=personasManager.pantallaPersonas(obtenerCdsisrolSesion(),timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			imap            = (Map<String,Item>)managerResult.get("itemMap");
			result          = SUCCESS;
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al cargar pantalla de personas",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
			result          = ERROR;
		}
		logger.info(timestamp
				+ "\n###### pantallaPersonas ######"
				+ "\n##############################"
				);
		return result;
	}
	
	/**
	 * Carga personas por rfc
	 * @return SUCCESS
	 */
	public String obtenerPersonasPorRFC()throws Exception
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp
				+ "\n###################################"
				+ "\n###### obtenerPersonasPorRFC ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			personaWS = false;
			
			if("dummyForAllQuery".equals(smap1.get("rfc")) || "dummyForAllQuery".equals(smap1.get("nombre"))){
				exito = true;
				return SUCCESS;
			}
			
			/**
			 * UPPERCASE PARA EVITAR BUSQUEDAS CON MINUSCULA
			 */
			if(StringUtils.isNotBlank(smap1.get("rfc"))){
				smap1.put("rfc", smap1.get("rfc").toUpperCase());
			}
			if(StringUtils.isNotBlank(smap1.get("nombre"))){
				smap1.put("nombre", smap1.get("nombre").toUpperCase());
			}
			
			Map<String,Object>managerResult=personasManager.obtenerPersonasPorRFC(
					smap1.get("rfc"),
					smap1.get("nombre"),
					smap1.get("snombre"),
					smap1.get("apat"),
					smap1.get("amat"),
					smap1.get("validaTienePoliza"),
					timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			slist1          = (List<Map<String,String>>)managerResult.get("listaPersonas");
			
			logger.debug("Exito de busqueda de BD: " + exito);
			
			if(slist1 == null || slist1.isEmpty()){
				logger.debug("...Busqueda de Persona en WS...");
				String saludDanios = smap1.get("esSalud");
				
				ClienteGeneral clienteGeneral = new ClienteGeneral();
		    	clienteGeneral.setRfcCli(smap1.get("rfc"));
		    	//clienteGeneral.setRamoCli(213);
		    	clienteGeneral.setClaveCia(saludDanios);
		    	clienteGeneral.setNombreCli(smap1.get("nombre"));
		    	
		    	ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, null, Ice2sigsService.Operacion.CONSULTA_GENERAL, clienteGeneral, null, false);
		    	
		    	if(clientesRes == null || (Estatus.EXITO.getCodigo() != clientesRes.getCodigo())){
		    		
		    		logger.debug("Error en WS, exito false");
		    		exito           = false;
					respuesta       = "No se encontr\u00F3 la persona. Consulte a soporte.";
					respuestaOculta = "No se encontr\u00F3 la persona. Consulte a soporte.";
					slist1          = null;
					
		    		return SUCCESS;
		    	}else{
		    		exito = true;
		    	}
		    	
		    	ClienteGeneral[] listaClientesGS = clientesRes.getClientesGeneral();
		    	if(listaClientesGS != null && listaClientesGS.length > 0 ){
		    		logger.debug("Agregando Personas de GS a Lista, " + listaClientesGS.length);
		    		personaWS = true;
		    		
		    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    		Calendar calendar =  Calendar.getInstance();
			    	HashMap<String, String> agregar = null;
			    	
			    	for(ClienteGeneral cli: listaClientesGS){
			    		agregar = new HashMap<String,String>();
			    		
			    		agregar.put("CDPERSON", "1");
			    		
			    		if("S".equalsIgnoreCase(saludDanios)){
			    			agregar.put("CDIDEEXT", cli.getNumeroExterno());//Para Salud
			    			agregar.put("CDIDEPER", "");
			    		}else{
			    			agregar.put("CDIDEPER", cli.getNumeroExterno());//Para Danios
			    			agregar.put("CDIDEEXT", "");
			    		}
				    	
				    	agregar.put("CDRFC",    cli.getRfcCli());
				    	agregar.put("DSNOMBRE", (cli.getFismorCli() == 1) ? cli.getNombreCli() : cli.getRazSoc());
				    	agregar.put("DSNOMBRE1",   "");
				    	
				    	String apellidoPat = "";
				    	if(StringUtils.isNotBlank(cli.getApellidopCli()) && !cli.getApellidopCli().trim().equalsIgnoreCase("null")){
				    		apellidoPat = cli.getApellidopCli();
				    	}
				    	agregar.put("DSAPELLIDO", (cli.getFismorCli() == 1) ? apellidoPat : "");
				    	
				    	String apellidoMat = "";
				    	if(StringUtils.isNotBlank(cli.getApellidomCli()) && !cli.getApellidomCli().trim().equalsIgnoreCase("null")){
				    		apellidoMat = cli.getApellidomCli();
				    	}
				    	agregar.put("DSAPELLIDO1", (cli.getFismorCli() == 1) ? apellidoMat : "");
				    	
				    	if(cli.getFecnacCli()!= null){
				    		calendar.set(cli.getFecnacCli().get(Calendar.YEAR), cli.getFecnacCli().get(Calendar.MONTH), cli.getFecnacCli().get(Calendar.DAY_OF_MONTH));
							agregar.put("FENACIMI", sdf.format(calendar.getTime()));
				    	}else {
				    		agregar.put("FENACIMI", "");
				    	}
				    	agregar.put("DIRECCIONCLI", cli.getCalleCli()+" "+(StringUtils.isNotBlank(cli.getNumeroCli())?cli.getNumeroCli():"")+(StringUtils.isNotBlank(cli.getCodposCli())?" C.P. "+cli.getCodposCli():"")+" "+cli.getColoniaCli()+" "+cli.getMunicipioCli());
				    	
				    	agregar.put("NOMBRE_COMPLETO", cli.getRfcCli()+" - "+ ((cli.getFismorCli() == 1) ? (cli.getNombreCli()+" "+cli.getApellidopCli()+" "+cli.getApellidomCli()) : cli.getRazSoc()) + " - " + agregar.get("DIRECCIONCLI"));
				    	
				    	agregar.put("CODPOSTAL", cli.getCodposCli());
				    	String edoAdosPos = Integer.toString(cli.getEstadoCli());
		    			if(edoAdosPos.length() ==  1){
		    				edoAdosPos = "0"+edoAdosPos;
		    			}
				    	agregar.put("CDEDO", edoAdosPos);
				    	agregar.put("CDMUNICI", "");
				    	agregar.put("DSDOMICIL", cli.getCalleCli());
				    	agregar.put("NMNUMERO", cli.getNumeroCli());
				    	agregar.put("NMNUMINT", "");
				    	
				    	String sexo = "H"; //Hombre
				    	if(cli.getSexoCli() > 0){
				    		if(cli.getSexoCli() == 2) sexo = "M";
				    	}
				    	agregar.put("OTSEXO",     sexo);
				    	
				    	String tipoPersona = "F"; //Fisica
				    	if(cli.getFismorCli() > 0){
				    		if(cli.getFismorCli() == 2){
				    			tipoPersona = "M";
				    		}else if(cli.getFismorCli() == 3){
				    			tipoPersona = "S";
				    		}
				    	}
				    	agregar.put("OTFISJUR",     tipoPersona);
				    	
				    	String nacionalidad = "001";// Nacional
				    	if(StringUtils.isNotBlank(cli.getNacCli()) && !cli.getNacCli().equalsIgnoreCase("1")){
				    		nacionalidad = "002";
				    	}
				    	agregar.put("CDNACION",     nacionalidad);

				    	agregar.put("CANALING",  "");
				    	agregar.put("CONDUCTO",  "");
				    	agregar.put("FEINGRESO", "");
				    	agregar.put("PTCUMUPR",  "");
				    	agregar.put("STATUS",    "INCOMPLETO");
				    	agregar.put("RESIDENTE", "");
				    	
				    	slist1.add(agregar);
				    	
			    	}
			    	
			    	exito           = true;
					respuesta       = "Ok.";
					respuestaOculta = "Ok.";
		    	}else {
		    		logger.debug("No se encontraron clientes en GS.");
		    	}
			}
			
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al obtener personas por RFC",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		
		if(slist1 != null && !slist1.isEmpty()){
			smap1.put("cliEncontrado", "S");
		}else{
			smap1.put("cliEncontrado", "N");
		}
		
		logger.info(timestamp
				+ "\n###### obtenerPersonasPorRFC ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}

	/**
	 * Importa Persona/Cliente Externo de Salud o Danios 
	 * @return SUCCESS
	 */
	public String importaPersonaExtWS()
	{
		logger.info(
				"\n###################################"
				+ "\n###### importaPersonaExtWS ######"
				+ "\n params: "+params
				);
		try
		{
			logger.debug("...Busqueda de Persona en WS...");
			String saludDanios = params.get("esSalud");
			
			ClienteGeneral clienteGeneral = new ClienteGeneral();
			clienteGeneral.setClaveCia(saludDanios);
			clienteGeneral.setNumeroExterno(params.get("codigoCliExt"));
			
			ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, null, Ice2sigsService.Operacion.CONSULTA_GENERAL, clienteGeneral, null, false);
			
			if(clientesRes == null || (Estatus.EXITO.getCodigo() != clientesRes.getCodigo())){
				
				logger.debug("Error en WS, exito false");
				exito           = false;
				respuesta       = "No se encontr\u00F3 la persona al Importar. Consulte a soporte.";
				respuestaOculta = "No se encontr\u00F3 la persona al Importar. Consulte a soporte.";
				slist1          = null;
				
				return SUCCESS;
			}else{
				exito = true;
			}
			
			ClienteGeneral[] listaClientesGS = clientesRes.getClientesGeneral();
			if(listaClientesGS != null && listaClientesGS.length == 1 ){
				logger.debug("Importando Persona de GS ");
				
				ClienteGeneral cliImport = listaClientesGS[0];
				
				String apellidoPat = "";
		    	if(StringUtils.isNotBlank(cliImport.getApellidopCli()) && !cliImport.getApellidopCli().trim().equalsIgnoreCase("null")){
		    		apellidoPat = cliImport.getApellidopCli();
		    	}
		    	
		    	String apellidoMat = "";
		    	if(StringUtils.isNotBlank(cliImport.getApellidomCli()) && !cliImport.getApellidomCli().trim().equalsIgnoreCase("null")){
		    		apellidoMat = cliImport.getApellidomCli();
		    	}
		    	
	    		Calendar calendar =  Calendar.getInstance();
	    		
	    		String sexo = null; //Por default se pide mandar en nulo en vez de Hombre(H)
		    	/**
	    		if(cliImport.getSexoCli() > 0){
		    		if(cliImport.getSexoCli() == 2) sexo = "M";
		    	}**/
		    	
		    	String tipoPersona = "F"; //Fisica
		    	if(cliImport.getFismorCli() > 0){
		    		if(cliImport.getFismorCli() == 2){
		    			tipoPersona = "M";
		    		}else if(cliImport.getFismorCli() == 3){
		    			tipoPersona = "S";
		    		}
		    	}
		    	
		    	String nacionalidad = "001";// Nacional
		    	if(StringUtils.isNotBlank(cliImport.getNacCli()) && !cliImport.getNacCli().equalsIgnoreCase("1")){
		    		nacionalidad = "002";
		    	}
		    	
		    	
		    	if(cliImport.getFecnacCli()!= null){
		    		calendar.set(cliImport.getFecnacCli().get(Calendar.YEAR), cliImport.getFecnacCli().get(Calendar.MONTH), cliImport.getFecnacCli().get(Calendar.DAY_OF_MONTH));
		    	}
		    	
		    	Calendar calendarIngreso =  Calendar.getInstance();
		    	if(cliImport.getFecaltaCli() != null){
		    		calendarIngreso.set(cliImport.getFecaltaCli().get(Calendar.YEAR), cliImport.getFecaltaCli().get(Calendar.MONTH), cliImport.getFecaltaCli().get(Calendar.DAY_OF_MONTH));
		    	}
				
		    	String edoAdosPos2 = Integer.toString(cliImport.getEstadoCli());
    			if(edoAdosPos2.length() ==  1){
    				edoAdosPos2 = "0"+edoAdosPos2;
    			}
    			
    			String codPosImp = cliImport.getCodposCli();
                if(StringUtils.isNotBlank(codPosImp) && codPosImp.length() == 4){
                    codPosImp = "0"+codPosImp;//Se agrega un cero a la izquierda del codigo postal en caso de que falte
                }

                long timestamp=System.currentTimeMillis();
    			
    			logger.debug("Canal de Ingreso:" + cliImport.getCanconCli());
    			
    			params.put("pv_cdpostal_i", codPosImp);
    			params.put("pv_cdedo_i",    edoAdosPos2);
    			params.put("pv_dsmunici_i", cliImport.getMunicipioCli());
    			params.put("pv_dscoloni_i", cliImport.getColoniaCli());
    			
    			Map<String,String> munycol= personasManager.obtieneMunicipioYcolonia(params);
    			
    			
    			saveList = new ArrayList<Map<String,String>>();
    			updateList = new ArrayList<Map<String,String>>();
    			deleteList = new ArrayList<Map<String,String>>();

    			Map<String,String> domicilioImp = new HashMap<String,String>();
    			
    			domicilioImp.put("NMORDDOM",null);
    			domicilioImp.put("CODPOSTAL", codPosImp);
    			domicilioImp.put("CDEDO", codPosImp+edoAdosPos2);
    			domicilioImp.put("CDMUNICI",munycol.get("CDMUNICI"));
    			domicilioImp.put("CDCOLONI",munycol.get("CDCOLONI"));
    			domicilioImp.put("DSDOMICI",cliImport.getCalleCli());
    			domicilioImp.put("NMNUMERO",cliImport.getNumeroCli());
    			domicilioImp.put("NMNUMINT",null);
    			
    			saveList.add(domicilioImp);
    			
    			UserVO usuario=(UserVO)session.get("USUARIO");
    			
    			Map<String,Object>managerResult = personasManager.guardarPantallaPersonas(null,//cdperson
						"1",//cdidepe
						"S".equalsIgnoreCase(saludDanios)? null : cliImport.getNumeroExterno(),
						(cliImport.getFismorCli() == 1) ? cliImport.getNombreCli() : cliImport.getRazSoc(),
						"1",//cdtipper
						tipoPersona, sexo, calendar.getTime(), cliImport.getRfcCli(), cliImport.getMailCli()
						,null //segundo nombre
						,(cliImport.getFismorCli() == 1) ? apellidoPat : ""
						,(cliImport.getFismorCli() == 1) ? apellidoMat : ""
						,calendarIngreso.getTime()
						,nacionalidad
						,cliImport.getCanconCli() <= 0 ? "0" : (Integer.toString(cliImport.getCanconCli()))// canaling
						,null// conducto
						,null// ptcumupr
						,null// residencia
						,null// nongrata
						/**
                		 * 
                		 * SE PONE EL CDIDEEXT EN BLANCO POR PETICION DE ARGENIS POR PROBLEMAS DE SUCURSAL CON EL CLIENTE
                		 * SE MANDAN A CREAR NUEVOS CLIENTES.
                		 * 
                		 * Codigo Original:
                		 * "S".equalsIgnoreCase(saludDanios)? cliImport.getNumeroExterno() : null 
                		 * 
                		 **/
						,null//"S".equalsIgnoreCase(saludDanios)? cliImport.getNumeroExterno() : null
						,cliImport.getEdocivilCli()<=0 ?"0" : Integer.toString(cliImport.getEdocivilCli())
						,Integer.toString(cliImport.getSucursalCli())
						,cliImport.getTelefonoCli()
						,saveList
						,updateList
						,deleteList
						,false
						,usuario
						,timestamp);
				
				
				exito                = (Boolean)managerResult.get("exito");
				String cdpersonNuevo = (String)managerResult.get("cdpersonNuevo");
				params.put("cdperson", cdpersonNuevo);
				params.put("codigoExterno", cliImport.getNumeroExterno());

				params.put("coloniaImp",    StringUtils.isBlank(munycol.get("CDCOLONI")) && StringUtils.isNotBlank(cliImport.getColoniaCli())   ? cliImport.getColoniaCli()  : "");
				params.put("municipioImp" , StringUtils.isBlank(munycol.get("CDMUNICI")) && StringUtils.isNotBlank(cliImport.getMunicipioCli()) ? cliImport.getMunicipioCli(): "");
				
				if(exito){
					managerResult=personasManager.guardarDatosTvaloper(
							cdpersonNuevo, "1", cliImport.getCveEle(),cliImport.getPasaporteCli(),null,null,null,null,null
							,cliImport.getOrirecCli(),null,null,cliImport.getNacCli(),null,null,null,null,null,null
							,null,null,(cliImport.getOcuPro() > 0) ? Integer.toString(cliImport.getOcuPro()) : "0"
							,null,null,null,null,cliImport.getCurpCli(),null,null,null,null,null,null,null,null,null
							,null,null,null,cliImport.getTelefonoCli(),cliImport.getMailCli(),null,null,null,null,null,null,null,null,null
							,null,null, cliImport.getFaxCli(), cliImport.getCelularCli(),timestamp);
					
					exito                = (Boolean)managerResult.get("exito");
					respuesta            = (String)managerResult.get("respuesta");
					respuestaOculta      = (String)managerResult.get("respuestaOculta");
					
				}
				
			}else {
				logger.debug("No se encontr\u00F3 coincidencia con el WS o hay mas de una.");
				if(listaClientesGS == null){
					logger.debug("Lista de Clientes es nula");	
				}else{
					logger.debug("Tamanio de la Lista de Clientes: " + listaClientesGS.length);
				}
				exito           = false;
				respuesta       = "Error al importar persona externa en la edicion.";
				respuestaOculta = "Error al importar persona externa en la edicion.";
			}
		}
		catch(Exception ex)
		{
			logger.error("Error inesperado al importar persona por WS",ex);
			exito           = false;
			respuesta       = "Error inesperado";
			respuestaOculta = ex.getMessage();
		}
		logger.info(
				"\n###### importaPersonaExtWS ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	/**
	 * Importa Persona/Cliente Externo de Salud o Danios 
	 * @return SUCCESS
	 */
	public String importaPersonaExtWSNoSicaps()
	{
		logger.info(
				"\n###########################################"
				+ "\n###### importaPersonaExtWSNoSicaps ######"
				+ "\n params: "+params
				);
		try
		{
			logger.debug("...Busqueda de Persona en WS...");
			String saludDanios = params.get("esSalud");
			
			ClienteGeneral clienteGeneral = new ClienteGeneral();
			clienteGeneral.setClaveCia(saludDanios);
			clienteGeneral.setNumeroExterno(params.get("codigoCliExt"));
			
			ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, null, Ice2sigsService.Operacion.CONSULTA_GENERAL, clienteGeneral, null, false);
			
			if(clientesRes == null || (Estatus.EXITO.getCodigo() != clientesRes.getCodigo())){
				
				logger.debug("Error en WS, exito false");
				exito           = false;
				respuesta       = "No se encontr\u00F3 la persona al Importar. Consulte a soporte.";
				respuestaOculta = "No se encontr\u00F3 la persona al Importar. Consulte a soporte.";
				slist1          = null;
				
				return SUCCESS;
			}else{
				exito = true;
			}
			
			ClienteGeneral[] listaClientesGS = clientesRes.getClientesGeneral();
			
			if(listaClientesGS != null && listaClientesGS.length == 1 ){
				logger.debug("Importando Persona de GS ");
				
				ClienteGeneral cliImport = listaClientesGS[0];
				
				//Validamos si ya existe el registro 
				logger.debug("VALOR cliImport getNumeroExterno ===>"+cliImport.getNumeroExterno());
				String validarAsegurado = personasManager.validaExisteAseguradoSicaps(cliImport.getNumeroExterno());
				logger.debug("Validamos si existe ====> "+validarAsegurado);
				if(Integer.parseInt(validarAsegurado) > 0){
					params.put("cdpersonNuevo", validarAsegurado);
					exito                = true;
					respuesta            = "Ya existe un asegurado por lo que no se actualiza";
					respuestaOculta      = "Ya existe un asegurado por lo que no se actualiza";
				}else{
					String apellidoPat = "";
			    	if(StringUtils.isNotBlank(cliImport.getApellidopCli()) && !cliImport.getApellidopCli().trim().equalsIgnoreCase("null")){
			    		apellidoPat = cliImport.getApellidopCli();
			    	}
			    	
			    	String apellidoMat = "";
			    	if(StringUtils.isNotBlank(cliImport.getApellidomCli()) && !cliImport.getApellidomCli().trim().equalsIgnoreCase("null")){
			    		apellidoMat = cliImport.getApellidomCli();
			    	}
			    	
		    		Calendar calendar =  Calendar.getInstance();
		    		
		    		String sexo = null; //Por default se pide mandar en nulo en vez de Hombre(H)
			    	/**
		    		if(cliImport.getSexoCli() > 0){
			    		if(cliImport.getSexoCli() == 2) sexo = "M";
			    	}**/
			    	
			    	String tipoPersona = "F"; //Fisica
			    	if(cliImport.getFismorCli() > 0){
			    		if(cliImport.getFismorCli() == 2){
			    			tipoPersona = "M";
			    		}else if(cliImport.getFismorCli() == 3){
			    			tipoPersona = "S";
			    		}
			    	}
			    	
			    	String nacionalidad = "001";// Nacional
			    	if(StringUtils.isNotBlank(cliImport.getNacCli()) && !cliImport.getNacCli().equalsIgnoreCase("1")){
			    		nacionalidad = "002";
			    	}
			    	
			    	
			    	if(cliImport.getFecnacCli()!= null){
			    		calendar.set(cliImport.getFecnacCli().get(Calendar.YEAR), cliImport.getFecnacCli().get(Calendar.MONTH), cliImport.getFecnacCli().get(Calendar.DAY_OF_MONTH));
			    	}
			    	
			    	Calendar calendarIngreso =  Calendar.getInstance();
			    	if(cliImport.getFecaltaCli() != null){
			    		calendarIngreso.set(cliImport.getFecaltaCli().get(Calendar.YEAR), cliImport.getFecaltaCli().get(Calendar.MONTH), cliImport.getFecaltaCli().get(Calendar.DAY_OF_MONTH));
			    	}
					
			    	String edoAdosPos2 = Integer.toString(cliImport.getEstadoCli());
	    			if(edoAdosPos2.length() ==  1){
	    				edoAdosPos2 = "0"+edoAdosPos2;
	    			}
	    			
	    			String codPosImp = cliImport.getCodposCli();
	                if(StringUtils.isNotBlank(codPosImp) && codPosImp.length() == 4){
	                    codPosImp = "0"+codPosImp;//Se agrega un cero a la izquierda del codigo postal en caso de que falte
	                }
	    			
	    			long timestamp=System.currentTimeMillis();
	    			
	    			logger.debug("Canal de Ingreso:" + cliImport.getCanconCli());
	    			
	    			params.put("pv_cdpostal_i", codPosImp);
	    			params.put("pv_cdedo_i",    edoAdosPos2);
	    			params.put("pv_dsmunici_i", cliImport.getMunicipioCli());
	    			params.put("pv_dscoloni_i", cliImport.getColoniaCli());
	    			
	    			Map<String,String> munycol= personasManager.obtieneMunicipioYcolonia(params);
	    			
	    			
	    			saveList = new ArrayList<Map<String,String>>();
	    			updateList = new ArrayList<Map<String,String>>();
	    			deleteList = new ArrayList<Map<String,String>>();

	    			Map<String,String> domicilioImp = new HashMap<String,String>();
	    			
	    			domicilioImp.put("NMORDDOM",null);
	    			domicilioImp.put("CODPOSTAL", codPosImp);
	    			domicilioImp.put("CDEDO",     codPosImp+edoAdosPos2);
	    			domicilioImp.put("CDMUNICI",munycol.get("CDMUNICI"));
	    			domicilioImp.put("CDCOLONI",munycol.get("CDCOLONI"));
	    			domicilioImp.put("DSDOMICI",cliImport.getCalleCli());
	    			domicilioImp.put("NMNUMERO",cliImport.getNumeroCli());
	    			domicilioImp.put("NMNUMINT",null);
	    			
	    			saveList.add(domicilioImp);
	    			
	    			UserVO usuario=(UserVO)session.get("USUARIO");
	    			
	    			Map<String,Object>managerResult = personasManager.guardarPantallaPersonas(null,//cdperson
							"1",//cdidepe
							"S".equalsIgnoreCase(saludDanios)? null : cliImport.getNumeroExterno(),
							(cliImport.getFismorCli() == 1) ? cliImport.getNombreCli() : cliImport.getRazSoc(),
							"1",//cdtipper
							tipoPersona, sexo, calendar.getTime(), cliImport.getRfcCli(), cliImport.getMailCli()
							,null //segundo nombre
							,(cliImport.getFismorCli() == 1) ? apellidoPat : ""
							,(cliImport.getFismorCli() == 1) ? apellidoMat : ""
							,calendarIngreso.getTime()
							,nacionalidad
							,cliImport.getCanconCli() <= 0 ? "0" : (Integer.toString(cliImport.getCanconCli()))// canaling
							,null// conducto
							,null// ptcumupr
							,null// residencia
							,null// nongrata
							/**
	                		 * 
	                		 * SE PONE EL CDIDEEXT EN BLANCO POR PETICION DE ARGENIS POR PROBLEMAS DE SUCURSAL CON EL CLIENTE
	                		 * SE MANDAN A CREAR NUEVOS CLIENTES.
	                		 * 
	                		 * Codigo Original:
	                		 * "S".equalsIgnoreCase(saludDanios)? cliImport.getNumeroExterno() : null 
	                		 * 
	                		 **/
							,null//"S".equalsIgnoreCase(saludDanios)? cliImport.getNumeroExterno() : null
							,cliImport.getEdocivilCli()<=0 ?"0" : Integer.toString(cliImport.getEdocivilCli())
							,Integer.toString(cliImport.getSucursalCli())
							,cliImport.getTelefonoCli()
							,saveList
							,updateList
							,deleteList
							,false
							,usuario
							,timestamp);
					
					
					exito                = (Boolean)managerResult.get("exito");
					String cdpersonNuevo = (String)managerResult.get("cdpersonNuevo");
					params.put("cdperson", cdpersonNuevo);
					params.put("cdpersonNuevo", cdpersonNuevo);
					params.put("codigoExterno", cliImport.getNumeroExterno());

					params.put("coloniaImp",    StringUtils.isBlank(munycol.get("CDCOLONI")) && StringUtils.isNotBlank(cliImport.getColoniaCli())   ? cliImport.getColoniaCli()  : "");
					params.put("municipioImp" , StringUtils.isBlank(munycol.get("CDMUNICI")) && StringUtils.isNotBlank(cliImport.getMunicipioCli()) ? cliImport.getMunicipioCli(): "");
					
					if(exito){
						managerResult=personasManager.guardarDatosTvaloper(
								cdpersonNuevo, "1", cliImport.getCveEle(),cliImport.getPasaporteCli(),null,null,null,null,null
								,cliImport.getOrirecCli(),null,null,cliImport.getNacCli(),null,null,null,null,null,null
								,null,null,(cliImport.getOcuPro() > 0) ? Integer.toString(cliImport.getOcuPro()) : "0"
								,null,null,null,null,cliImport.getCurpCli(),null,null,null,null,null,null,null,null,null
								,null,null,null,cliImport.getTelefonoCli(),cliImport.getMailCli(),null,null,null,null,null,null,null,null,null
								,null,null, cliImport.getFaxCli(), cliImport.getCelularCli(),timestamp);
						
						exito                = (Boolean)managerResult.get("exito");
						respuesta            = (String)managerResult.get("respuesta");
						respuestaOculta      = (String)managerResult.get("respuestaOculta");
						
					}
				}
				
			}else {
				logger.debug("No se encontr\u00F3 coincidencia con el WS o hay mas de una.");
				if(listaClientesGS == null){
					logger.debug("Lista de Clientes es nula");	
				}else{
					logger.debug("Tamanio de la Lista de Clientes: " + listaClientesGS.length);
				}
				exito           = false;
				respuesta       = "Error al importar persona externa en la edicion.";
				respuestaOculta = "Error al importar persona externa en la edicion.";
			}
		}
		catch(Exception ex)
		{
			logger.error("Error inesperado al importar persona por WS",ex);
			exito           = false;
			respuesta       = "Error inesperado";
			respuestaOculta = ex.getMessage();
		}
		logger.info(
				"\n###### importaPersonaExtWSNoSicaps ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	
	
	public String obtenerPersonaPorCdperson()
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp
				+ "\n#######################################"
				+ "\n###### obtenerPersonaPorCdperson ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			Map<String,Object>managerResult=personasManager.obtenerPersonaPorCdperson(
					smap1.get("cdperson"),
					timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			smap2           = (Map<String,String>)managerResult.get("persona");
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al obtener datos de persona",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp
				+ "\n###### obtenerPersonaPorCdperson ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}

	public String obtieneConfPatallaCli()
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp
				+ "\n###################################"
				+ "\n###### obtieneConfPatallaCli ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			Integer tipoCliente = null;
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			
			if(StringUtils.isBlank(smap1.get("codigoExterno"))){
				
				logger.debug(">>> Complementado Codigo externo: " + smap1.get("codigoExterno"));
				Map<String,Object> datos = personasManager.obtenerPersonaPorCdperson(smap1.get("cdperson"), timestamp);
				Map<String,String> persona = (Map<String, String>) datos.get("persona");
						 
				if("D".equalsIgnoreCase(smap1.get("esSalud"))){
					if(persona != null && persona.containsKey("CDIDEPER") && StringUtils.isNotBlank((String)persona.get("CDIDEPER"))){
						smap1.put("codigoExterno",(String)persona.get("CDIDEPER"));
					}
				}else if("S".equalsIgnoreCase(smap1.get("esSalud"))){
					if(persona!=null && persona.containsKey("CDIDEEXT") && StringUtils.isNotBlank((String)persona.get("CDIDEEXT"))){
						smap1.put("codigoExterno",(String)persona.get("CDIDEEXT"));
					}
				}
				
				logger.debug(">>> Codigo externo Obtenido: " + smap1.get("codigoExterno"));
			}
			
			if(StringUtils.isNotBlank(smap1.get("codigoExterno"))){
				tipoCliente = personasManager.obtieneTipoCliWS(smap1.get("codigoExterno"), smap1.get("esSalud"));
			}else{
				/**
				 *  Si no hay codigo externo puede ser un cliente nuevo o se realizo la busqueda con un codigo externo
				 *  cruzado entre salud y danios por lo que se crea uno nuevo en la emision para la compania que se esta usando
				 *  Se fija tipo cliente externo a nuevo
				 */
				tipoCliente = new Integer(1);
			}
			
			if(StringUtils.isNotBlank(smap1.get("modoAgregar")) && Constantes.SI.equals(smap1.get("modoAgregar"))){
				tipoCliente = new Integer(1);
			}
			
			slist1=personasManager.obtieneConfPatallaCli(smap1.get("cdperson"), usuario.getUser(),usuario.getRolActivo().getClave(), tipoCliente.toString());
			
			exito = true;
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al obtener datos de persona",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp
				+ "\n###### obtieneConfPatallaCli ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	/**
	 * Guarda los datos de la pantalla de personas
	 * @return SUCCESS
	 */
	public String guardarPantallaPersonas()
	{
		long timestamp = System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n#####################################"
				+ "\n###### guardarPantallaPersonas ######"
				+ "\nsmap1: "+smap1
				+ "\nsmap2: "+smap2
				+ "\nsmap3: "+smap3
				);
		try
		{
			Date fechaNacimi = new Date();
			
			if(StringUtils.isNotBlank(smap1.get("FENACIMI"))){
				fechaNacimi = renderFechas.parse(smap1.get("FENACIMI"));
			}
			
			UserVO usuario=(UserVO)session.get("USUARIO");
					
			Map<String,Object>managerResult=personasManager.guardarPantallaPersonas(
					smap1.get("CDPERSON")
					,null
					,smap1.get("CDIDEPER")
					,smap1.get("DSNOMBRE")
					,null
					,smap1.get("OTFISJUR")
					,smap1.get("OTSEXO")
					,fechaNacimi
					,smap1.get("CDRFC")
					,null
					,smap1.get("DSNOMBRE1")
					,smap1.get("DSAPELLIDO")
					,smap1.get("DSAPELLIDO1")
					,renderFechas.parse(smap1.get("FEINGRESO"))
					,smap1.get("CDNACION")
					,smap1.get("CANALING")
					,smap1.get("CONDUCTO")
					,smap1.get("PTCUMUPR")
					,smap1.get("RESIDENTE")
					,smap1.get("NONGRATA")
					,smap1.get("CDIDEEXT") 
					,smap1.get("CDESTCIV") //estado civil
					,smap1.get("CDSUCEMI") //Sucursal Emision
					,smap2.get("NMTELEFO")
					,saveList
					,updateList
					,deleteList
					,Constantes.SI.equalsIgnoreCase(smap3.get("AUTOSAVE"))?true:false
					,usuario
					,timestamp
					);
			exito                = (Boolean)managerResult.get("exito");
			respuesta            = (String)managerResult.get("respuesta");
			respuestaOculta      = (String)managerResult.get("respuestaOculta");
			String cdpersonNuevo = (String)managerResult.get("cdpersonNuevo");
			if(StringUtils.isNotBlank(cdpersonNuevo))
			{
				smap1.put("CDPERSON",cdpersonNuevo);
			}
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al guardar pantalla de personas",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### guardarPantallaPersonas ######"
				+ "\n#####################################"
				);
		return SUCCESS;
	}

	
	/**
	 * Guarda los datos de la pantalla de personas
	 * @return SUCCESS
	 */
	public String guardarDomicilioAsegurado()
	{
		long timestamp = System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n#####################################"
				+ "\n###### guardarDomicilioAsegurado ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			personasManager.guardarPantallaDomicilio(
					smap1.get("CDPERSON")
					,smap1.get("NMORDDOM")
					,smap1.get("DSDOMICI")
					,smap1.get("NMTELEFO")
					,smap1.get("CODPOSTAL")
					,smap1.get("CDEDO")
					,smap1.get("CDMUNICI")
					,smap1.get("CDCOLONI")
					,smap1.get("NMNUMERO")
					,smap1.get("NMNUMINT")
					,usuario
					,"S"//Domicilio activo
					,smap1.get("accion")
					,timestamp
					);
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al guardar pantalla de guardarDomicilioAsegurado",ex);
			exito = false;
			respuesta = "Error: " + ex.getMessage()+", "+timestamp;
		}
		logger.info(timestamp+""
				+ "\n###### guardarDomicilioAsegurado ######"
				+ "\n#####################################"
				);
		exito = true;
		return SUCCESS;
	}
	
	
	
	/**
	 * Obtener la direccion de una persona por su CDPERSON
	 * @return SUCCESS
	 */
	public String obtenerDomicilioPorCdperson()
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n#########################################"
				+ "\n###### obtenerDomicilioPorCdperson ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			Map<String,Object> managerResult=personasManager.obtenerDomicilioPorCdperson(smap1.get("cdperson"),smap1.get("nmorddom"),timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			smap1           = (Map<String,String>)managerResult.get("domicilio");
		}
		catch(Exception ex)
		{
			if(smap1!=null && smap1.containsKey("AUTOSAVE") && Constantes.SI.equalsIgnoreCase(smap1.get("AUTOSAVE"))){
				logger.error(timestamp+"Persona sin domicilio, error sin impacto");
			}else{
				logger.error(timestamp+" error inesperado al obtener domicilio por cdperson",ex);
			}
			
			
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### obtenerDomicilioPorCdperson ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}

	/**
	 * Obtener la direccion del contratante de una poliza
	 * @return SUCCESS
	 */
	public String obtenerDomicilioContratante()
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n#########################################"
				+ "\n###### obtenerDomicilioContratante ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			Map<String,Object> managerResult=personasManager.obtenerDomicilioContratante(smap1,timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			smap1           = (Map<String,String>)managerResult.get("domicilio");
		}
		catch(Exception ex)
		{
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### obtenerDomicilioContratante ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}

	/**
	 * Obtener la direccion de una persona por su CDPERSON
	 * @return SUCCESS
	 */
	public String obtenerDomiciliosPorCdperson()
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n#########################################"
				+ "\n###### obtenerDomiciliosPorCdperson ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			Map<String,Object> managerResult=personasManager.obtenerDomiciliosPorCdperson(smap1.get("cdperson"),timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			slist1          = (List<Map<String,String>>)managerResult.get("domicilios");
		}
		catch(Exception ex)
		{
			if(smap1!=null && smap1.containsKey("AUTOSAVE") && Constantes.SI.equalsIgnoreCase(smap1.get("AUTOSAVE"))){
				logger.error(timestamp+"Persona sin domicilio, error sin impacto");
			}else{
				logger.error(timestamp+" error inesperado al obtener domicilio por cdperson",ex);
			}
			
			
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### obtenerDomicilioPorCdperson ######"
				+ "\n#########################################"
				);
		return SUCCESS;
	}
	
	/**
	 * Obtener los items de tatriper y los valores de tvaloper para un cdperson
	 * @return SUCCESS
	 */
	public String obtenerTatriperTvaloperPorCdperson()
	{
		long timestamp=System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n################################################"
				+ "\n###### obtenerTatriperTvaloperPorCdperson ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			Map<String,Object> managerResult=personasManager.obtenerTatriperTvaloperPorCdperson(smap1.get("cdperson"),smap1.get("cdrol"),timestamp);
			exito           = (Boolean)managerResult.get("exito");
			respuesta       = (String)managerResult.get("respuesta");
			respuestaOculta = (String)managerResult.get("respuestaOculta");
			smap1.put("itemsTatriper"  , ((Item)managerResult.get("itemsTatriper")).toString());
			smap1.put("fieldsTatriper" , ((Item)managerResult.get("fieldsTatriper")).toString());
			smap2=(Map<String,String>)managerResult.get("tvaloper");
			
			Map<String,String>aux=new HashMap<String,String>();
			for(Entry<String,String>en:smap2.entrySet())
			{
				if(en.getKey().substring(0, 3).equalsIgnoreCase("OTV"))
				{
					aux.put("parametros.pv_otvalor"+en.getKey().substring("OTVALOR".length(), en.getKey().length()),en.getValue());
				}
			}
			smap2.putAll(aux);
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al obtener tatriper y tvaloper por cdperson",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### obtenerTatriperTvaloperPorCdperson ######"
				+ "\n################################################"
				);
		return SUCCESS;
	}
	
	/**
	 * Guarda los datos de tvaloper para un cdperson
	 * @return SUCCESS
	 */
	public String guardarDatosTvaloper()
	{
		long timestamp = System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n##################################"
				+ "\n###### guardarDatosTvaloper ######"
				+ "\nsmap1: "+smap1
				+ "\nsaveList:   "+saveList
				+ "\nupdateList: "+updateList
				
				);
		try
		{
			Map<String,Object>managerResult=personasManager.guardarDatosTvaloper(
				smap1.get("cdperson"),smap1.get("cdrol")
				,smap1.get("parametros.pv_otvalor01") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor01") 
				,smap1.get("parametros.pv_otvalor02") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor02")
				,smap1.get("parametros.pv_otvalor03") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor03")
				,smap1.get("parametros.pv_otvalor04") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor04")
				,smap1.get("parametros.pv_otvalor05") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor05")
				,smap1.get("parametros.pv_otvalor06") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor06")
				,smap1.get("parametros.pv_otvalor07") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor07")
				,smap1.get("parametros.pv_otvalor08") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor08")
				,smap1.get("parametros.pv_otvalor09") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor09")
				,smap1.get("parametros.pv_otvalor10") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor10")
				,smap1.get("parametros.pv_otvalor11") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor11")
				,smap1.get("parametros.pv_otvalor12") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor12")
				,smap1.get("parametros.pv_otvalor13") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor13")
				,smap1.get("parametros.pv_otvalor14") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor14")
				,smap1.get("parametros.pv_otvalor15") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor15")
				,smap1.get("parametros.pv_otvalor16") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor16")
				,smap1.get("parametros.pv_otvalor17") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor17")
				,smap1.get("parametros.pv_otvalor18") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor18")
				,smap1.get("parametros.pv_otvalor19") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor19")
				,smap1.get("parametros.pv_otvalor20") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor20")
				,smap1.get("parametros.pv_otvalor21") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor21")
				,smap1.get("parametros.pv_otvalor22") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor22")
				,smap1.get("parametros.pv_otvalor23") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor23")
				,smap1.get("parametros.pv_otvalor24") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor24")
				,smap1.get("parametros.pv_otvalor25") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor25")
				,smap1.get("parametros.pv_otvalor26") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor26")
				,smap1.get("parametros.pv_otvalor27") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor27")
				,smap1.get("parametros.pv_otvalor28") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor28")
				,smap1.get("parametros.pv_otvalor29") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor29")
				,smap1.get("parametros.pv_otvalor30") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor30")
				,smap1.get("parametros.pv_otvalor31") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor31")
				,smap1.get("parametros.pv_otvalor32") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor32")
				,smap1.get("parametros.pv_otvalor33") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor33")
				,smap1.get("parametros.pv_otvalor34") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor34")
				,smap1.get("parametros.pv_otvalor35") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor35")
				,smap1.get("parametros.pv_otvalor36") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor36")
				,smap1.get("parametros.pv_otvalor37") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor37")
				,smap1.get("parametros.pv_otvalor38") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor38")
				,smap1.get("parametros.pv_otvalor39") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor39")
				,smap1.get("parametros.pv_otvalor40") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor40")
				,smap1.get("parametros.pv_otvalor41") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor41")
				,smap1.get("parametros.pv_otvalor42") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor42")
				,smap1.get("parametros.pv_otvalor43") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor43")
				,smap1.get("parametros.pv_otvalor44") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor44")
				,smap1.get("parametros.pv_otvalor45") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor45")
				,smap1.get("parametros.pv_otvalor46") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor46")
				,smap1.get("parametros.pv_otvalor47") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor47")
				,smap1.get("parametros.pv_otvalor48") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor48")
				,smap1.get("parametros.pv_otvalor49") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor49")
				,smap1.get("parametros.pv_otvalor50") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor50")
				,smap1.get("parametros.pv_otvalor51") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor51")
				,smap1.get("parametros.pv_otvalor52") == null ? "SIN_VALOR" : smap1.get("parametros.pv_otvalor52")
				,timestamp
				);
			
			
			params = new HashMap<String, String>();
   		 	params.put("pv_cdperson_i", smap1.get("cdperson"));
   		 	params.put("pv_cdrol_i", smap1.get("cdrol"));
   		 	
			this.actualizaStatusPersona();
						
			exito                = (Boolean)managerResult.get("exito");
			respuesta            = (String)managerResult.get("respuesta");
			respuestaOculta      = (String)managerResult.get("respuestaOculta");
		
			UserVO usuario=(UserVO)session.get("USUARIO");
			
			if(exito){
				logger.debug("...Guarda datos de Persona en WS...");
				String saludDanios = smap1.get("esSalud");
				
				ClienteGeneral clienteGeneral = new ClienteGeneral();
		    	clienteGeneral.setClaveCia(saludDanios);
		    	clienteGeneral.setNumeroExterno(smap1.get("codigoExterno"));
		    	
		    	ClienteGeneralRespuesta clientesRes = null;
		    	ClienteGeneralRespuesta clientesRes2 = null;
		    	
		    	boolean tienePrimerCompania = StringUtils.isNotBlank(smap1.get("codigoExterno"));
		    	boolean tieneSegundaCompania = StringUtils.isNotBlank(smap1.get("codigoExterno2"));
		    	
		    	if(tienePrimerCompania){
		    	     //Actualiza cliente en primer compania
		    		 clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap1.get("cdperson"), Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral, null, false);
		    		 
		    		 //Actualiza cliente en segunda compania
		    		 if(tieneSegundaCompania){
		    			 ClienteGeneral clienteGeneral2 = new ClienteGeneral();
		    			 clienteGeneral2.setClaveCia((saludDanios.equalsIgnoreCase("S"))?"D":"S");
		 		    	 clienteGeneral2.setNumeroExterno(smap1.get("codigoExterno2"));
		 		    	 clientesRes2 = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap1.get("cdperson"), Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral2, null, false);
		    		 }
		    	}else {
		    		 //Inserta cliente en primer compania
		    		 clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap1.get("cdperson"), Ice2sigsService.Operacion.INSERTA, clienteGeneral, null, false);
		    		 
		    		 //Actualiza cliente en segunda compania
		    		 if(tieneSegundaCompania){
		    			 ClienteGeneral clienteGeneral2 = new ClienteGeneral();
		    			 clienteGeneral2.setClaveCia((saludDanios.equalsIgnoreCase("S"))?"D":"S");
		 		    	 clienteGeneral2.setNumeroExterno(smap1.get("codigoExterno2"));
		    			 clientesRes2 = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap1.get("cdperson"), Ice2sigsService.Operacion.ACTUALIZA, clienteGeneral2, null, false);
		    			 
		    		 }
		    		 
		    	}
		    	clientesRes.setCodigo(0);
		    	if(clientesRes != null && (Estatus.EXITO.getCodigo() == clientesRes.getCodigo())){
		    	
		    		exito = true;
		    		if(!tienePrimerCompania){
		    			smap1.put("codigoExterno", clientesRes.getClientesGeneral()[0].getNumeroExterno());
		    			logger.debug("Codigo externo obtenido: " + clientesRes.getClientesGeneral()[0].getNumeroExterno());
		    			
		    			 params = new HashMap<String, String>();
			    		 params.put("pv_cdperson_i", smap1.get("cdperson"));
			    		 params.put("pv_swsalud_i"  , saludDanios);
			    		 params.put("pv_cdideper_i"  , clientesRes.getClientesGeneral()[0].getNumeroExterno());
			    		 
			    		 personasManager.actualizaCodigoExterno(params);
		    		}else{
		    			logger.debug("Codigo externo actualizado smap1: "+ smap1.get("codigoExterno"));
		    		}
		    		
		    	}else{
		    		
		    		logger.debug("Error en WS, exito false");
		    		exito           = false;
					respuesta       = "No se encontr\u00F3 la persona al Guardar. Consulte a soporte.";
					respuestaOculta = "No se encontr\u00F3 la persona al Guardar. Consulte a soporte.";
					slist1          = null;
					
		    		return SUCCESS;
		    	}
		    	
		    	boolean exitoDomicilios = true; 
		    	if(clientesRes != null && (Estatus.EXITO.getCodigo() == clientesRes.getCodigo())){
		    		exitoDomicilios = ice2sigsService.ejecutaWSdireccionClienteGeneral(smap1.get("cdperson"), saludDanios, saveList, updateList, !tienePrimerCompania, usuario);
	    		 }
		    	
		    	if(clientesRes2 != null && (Estatus.EXITO.getCodigo() == clientesRes2.getCodigo())){
		    		/**
		    		 * TODO: Validar si necesita insertar las direcciones en el codigo de compania contrario
		    		 */
	    			 ice2sigsService.ejecutaWSdireccionClienteGeneral(smap1.get("cdperson"), (saludDanios.equalsIgnoreCase("S"))?"D":"S", saveList, updateList, !tieneSegundaCompania, usuario);
	    		 }
		    	exitoDomicilios=true;
		    	if(!exitoDomicilios){
		    		logger.debug("Error al enviar domiciliosWS, exito false");
		    		exito           = false;
					respuesta       = "No se han enviado correctamente los domicilios.";
					respuestaOculta = "No se han enviado correctamente los domicilios.";
					slist1          = null;
					
		    		return SUCCESS;
		    	}
			}
		
		}
		catch(Exception ex)
		{
			logger.error(timestamp+" error inesperado al guardar datos de tvaloper",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### guardarDatosTvaloper ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}

	/**
	 * Guarda los datos de un cliente en una compania  (Salud/Danios) usando Web Service
	 * @return SUCCESS
	 */
	public String guardarClienteCompania()
	{
		long timestamp = System.currentTimeMillis();
		logger.info(timestamp+""
				+ "\n##################################"
				+ "\n###### Crea Cliente Compania ######"
				+ "\nsmap1: "+smap1
				);
		try
		{
			String saludDanios = smap1.get("esSalud");
			logger.debug("...Crear nueva Persona en WS... , Compania: " + saludDanios);
			
			
			ClienteGeneral clienteGeneral = new ClienteGeneral();
			clienteGeneral.setClaveCia(saludDanios);
			
			ClienteGeneralRespuesta clientesRes = ice2sigsService.ejecutaWSclienteGeneral(null, null, null, null, null, null, smap1.get("cdperson"), Ice2sigsService.Operacion.INSERTA, clienteGeneral, null, false);
			
			if(clientesRes == null || (Estatus.EXITO.getCodigo() != clientesRes.getCodigo())){
				
				logger.error("Error en WS, exito false,Error al crear codigo externo de cliente");
				exito           = false;
				respuesta       = "Error al crear codigo externo de cliente";
				respuestaOculta = "Error al crear codigo externo de cliente";
				slist1          = null;
				
				return SUCCESS;
			}else{
				exito = true;

				smap1.put("codigoExternoGen", clientesRes.getClientesGeneral()[0].getNumeroExterno());
				logger.debug("Codigo externo obtenido: " + clientesRes.getClientesGeneral()[0].getNumeroExterno());
				
				params = new HashMap<String, String>();
				params.put("pv_cdperson_i", smap1.get("cdperson"));
				params.put("pv_swsalud_i"  , saludDanios);
				params.put("pv_cdideper_i"  , clientesRes.getClientesGeneral()[0].getNumeroExterno());
				
				personasManager.actualizaCodigoExterno(params);
			
			}
		}
		catch(Exception ex)
		{
			logger.error(timestamp+"Error inesperado al guardar datos de persona en WS",ex);
			exito           = false;
			respuesta       = "Error inesperado #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(timestamp+""
				+ "\n###### guardarDatosTvaloper ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	
	public String obtieneAccionistas()
	{
		exito = false;
		
		try{
			slist1 =personasManager.obtieneAccionistas(params);
			exito = true;
		}catch(Exception ex){
			logger.error("Error al obtener los accionistas",ex);
			respuesta = ex.getMessage();
		}
		
		return SUCCESS;
	}

	public String eliminaAccionistas()
	{
		exito = false;
		
		try{
			personasManager.eliminaAccionistas(params);
			exito = true;
		}catch(Exception ex){
			logger.error("Error al eliminaAccionistas",ex);
			respuesta = ex.getMessage();
		}
		
		return SUCCESS;
	}

	public String actualizaStatusPersona()
	{
		exito = false;
		
		try{
			respuesta = personasManager.actualizaStatusPersona(params);
			exito = true;
		}catch(Exception ex){
			logger.error("Error al actualizaStatusPersona",ex);
			respuesta = ex.getMessage();
		}
		
		return SUCCESS;
	}

	public String guardaAccionista()
	{
		exito = false;
		
		try{
			logger.debug("Guardando lista de accionistas: ");
			logger.debug("Params: " + params);
			logger.debug("DeleteList: " + deleteList);
			logger.debug("SaveList: " + saveList);
			logger.debug("UpdateList: " + updateList);
			
			for(Map<String,String> del : deleteList){
				params.put("pv_accion_i"  , "D");
				
				params.put("pv_nmordina_i", del.get("NMORDINA"));
				params.put("pv_dsnombre_i", del.get("DSNOMBRE"));
				params.put("pv_cdnacion_i", del.get("CDNACION"));
				params.put("pv_porparti_i", del.get("PORPARTI"));
				
				personasManager.guardaAccionista(params);
			}
			
			for(Map<String,String> up : updateList){
				params.put("pv_accion_i"  , "U");
				
				params.put("pv_nmordina_i", up.get("NMORDINA"));
				params.put("pv_dsnombre_i", up.get("DSNOMBRE"));
				params.put("pv_cdnacion_i", up.get("CDNACION"));
				params.put("pv_porparti_i", up.get("PORPARTI"));
				
				personasManager.guardaAccionista(params);
			}
			
			for(Map<String,String> save : saveList){
				params.put("pv_accion_i"  , "I");
				
				params.put("pv_nmordina_i", save.get("NMORDINA"));
				params.put("pv_dsnombre_i", save.get("DSNOMBRE"));
				params.put("pv_cdnacion_i", save.get("CDNACION"));
				params.put("pv_porparti_i", save.get("PORPARTI"));
				
				personasManager.guardaAccionista(params);
			}
			
			exito = true;
		}catch(Exception ex){
			logger.error("Error al obtener los guardaAccionista",ex);
			respuesta = ex.getMessage();
		}
		
		return SUCCESS;
	}
	
	public String pantallaDocumentosPersona()
	{
		logger.info(""
				+ "\n###########################################"
				+ "\n######## pantallaDocumentosPersona ########"
				);
		logger.info(""
				+ "\n######## pantallaDocumentosPersona ########"
				+ "\n###########################################"
				);
		return SUCCESS;
	}
	
	public String cargarDocumentosPersona()
	{
		logger.info(""
				+ "\n#####################################"
				+ "\n###### cargarDocumentosPersona ######"
				+ "\nsmap1 "+smap1
				);
		try
		{
		    slist1=personasManager.cargarDocumentosPersona(smap1.get("cdperson"));
		}
		catch(Exception ex)
		{
			logger.error("error al obtener documentos de persona",ex);
			slist1=new ArrayList<Map<String,String>>();
		}
		logger.info(""
				+ "\n###### cargarDocumentosPersona ######"
				+ "\n#####################################"
				);
		return SUCCESS;
	}
	
	public String cargarNombreDocumentoPersona()
	{
		logger.info(""
				+ "\n##########################################"
				+ "\n###### cargarNombreDocumentoPersona ######"
				+ "\nsmap1 "+smap1
				);
		try
		{
			String nombre = personasManager.cargarNombreDocumentoPersona(smap1.get("cdperson"),smap1.get("codidocu"));
			if(StringUtils.isNotBlank(nombre))
			{
				smap1.put("cddocume",nombre);
				exito           = true;
				respuesta       = "Todo OK";
				respuestaOculta = "Todo OK";
			}
			else
			{
				exito           = false;
				respuesta       = "No hay documento";
				respuestaOculta = "Sin respuesta oculta";
			}
		}
		catch(Exception ex)
		{
			long timestamp=System.currentTimeMillis();
			logger.error("error al obtener nombre de archivo para persona #"+timestamp,ex);
			exito           = false;
			respuesta       = "Error al obtener archivo #"+timestamp;
			respuestaOculta = ex.getMessage();
		}
		logger.info(""
				+ "\n###### cargarNombreDocumentoPersona ######"
				+ "\n##########################################"
				);
		return SUCCESS;
	}
	
	private String obtenerCdsisrolSesion()
	{
		UserVO usuario=(UserVO)session.get("USUARIO");
		return usuario.getRolActivo().getClave();
	}
	
	public String pantallaBeneficiarios()
	{
		logger.debug(Utils.log(""
				,"\n###################################"
				,"\n###### pantallaBeneficiarios ######"
				,"\n###### smap1=",smap1
				));
		
		String result = ERROR;
		
		try
		{
			String paso = null;
			try
			{	
				paso = "Validando datos de entrada";
				
				Utils.validate(smap1, "No se recibieron datos");
				
				EndososAction endososAction = new EndososAction();
				endososAction.setEndososManager(endososManager);
				endososAction.transformaEntrada(smap1, slist1, true);
				
				//validados
				String cdunieco    = smap1.get("cdunieco")
				       ,cdramo     = smap1.get("cdramo")
				       ,estado     = smap1.get("estado")
				       ,nmpoliza   = smap1.get("nmpoliza")
				       ,nmsuplem   = smap1.get("nmsuplem")
				       ,nmsituac   = smap1.get("nmsituac")
				       ,cdrolPipes = smap1.get("cdrolPipes")
				       ,cdtipsup   = smap1.get("cdtipsup")
				       ,ntramite   = smap1.get("ntramite");
				
				Utils.validate(
						cdunieco   , "No se recibio la sucursal"
						,cdramo     , "No se recibio el producto"
						,estado     , "No se recibio el estado de la poliza"
						,nmpoliza   , "No se recibio el numero de poliza"
						,nmsuplem   , "No se recibio el numero de suplemento"
						,nmsituac   , "No se recibio el numero de situacion"
						,cdrolPipes , "No se recibieron los roles permitidos"
						,cdtipsup   , "No se recibio el tipo de suplemento"
						);
				
				if(!cdtipsup.equals("1"))
				{
					Utils.validate(ntramite, "No se recibio el numero de tramite");
				}
				
				//no validados
				String ultimaImagen = smap1.get("ultimaImagen");
				if(StringUtils.isBlank(ultimaImagen)
						||!ultimaImagen.equals("S"))
				{
					ultimaImagen="N";
				}
				
				smap1.put("ultimaImagen" , ultimaImagen);
				
				String cdsisrol = Utils.validateSession(session).getRolActivo().getClave();
				
				imap = personasManager.pantallaBeneficiarios(cdunieco,cdramo,estado,cdsisrol,cdtipsup);
				
				result = SUCCESS;
			}
			catch(Exception ex)
			{
				Utils.generaExcepcion(ex, paso);
			}
		}
		catch(Exception ex)
		{
			respuesta = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(""
				,"\n###### pantallaBeneficiarios ######"
				,"\n###################################"
				));
		
		return result;
	}
	
	/*
	 * GETTERS Y SETTERS
	 */
	public boolean isSuccess() {
		return true;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRespuestaOculta() {
		return respuestaOculta;
	}

	public void setRespuestaOculta(String respuestaOculta) {
		this.respuestaOculta = respuestaOculta;
	}

	public void setPersonasManager(PersonasManager personasManager)
	{
		this.personasManager = personasManager;
		this.personasManager.setSession(session);
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public Map<String, String> getSmap2() {
		return smap2;
	}

	public void setSmap2(Map<String, String> smap2) {
		this.smap2 = smap2;
	}

	public Map<String, String> getSmap3() {
		return smap3;
	}

	public void setSmap3(Map<String, String> smap3) {
		this.smap3 = smap3;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Map<String, String>> getSaveList() {
		return saveList;
	}

	public void setSaveList(List<Map<String, String>> saveList) {
		this.saveList = saveList;
	}

	public List<Map<String, String>> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<Map<String, String>> deleteList) {
		this.deleteList = deleteList;
	}

	public List<Map<String, String>> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<Map<String, String>> updateList) {
		this.updateList = updateList;
	}

	public boolean isPersonaWS() {
		return personaWS;
	}

	public void setPersonaWS(boolean personaWS) {
		this.personaWS = personaWS;
	}

	public FlujoVO getFlujo() {
		return flujo;
	}

	public void setFlujo(FlujoVO flujo) {
		this.flujo = flujo;
	}
	
}