package mx.com.gseguros.wizard.configuracion.producto.expresiones.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ClaveVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.HojaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.VariableVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ExpresionesManager;
import mx.com.gseguros.wizard.configuracion.producto.web.ExpresionesPadre;


public class PrincipalExpresionesAction extends ExpresionesPadre {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7829485807063178325L;
	private static final transient Log log = LogFactory.getLog(PrincipalExpresionesAction.class);
	//private ExpresionesManager expresionesManager;
	
	private List<ExpresionVO> editarExpresion;
	//listas de catalogos
	private List<HojaVO> listaFuncionesJson;
	private List<HojaVO> listaVariablesTemporalesJson;
	private List<RamaVO> listaCamposDelProductoJson;
	private List<VariableVO> listaComboVariables;
	
	
	private List<LlaveValorVO> listaTablaJson;
	private List<LlaveValorVO> listaColumnaJson;
	private List<LlaveValorVO> listaClaveJson;
	private List<ClaveVO> listaPropertyGridJson;
	private List<ClaveVO> listaClave;
	private String expresionId;
	//attributos de la Variable
	private String idAction;
	private String codigo;
	private String nombre;
	private String descripcionTabla;
	private String tabla;
	private String descripcionColumna;
	private String columna;
	private String clave;
	private String recalcular;
	private String expresion;
	private String switchFormato;
	//attributos de Expresiones
	private String descripcion;
	private String codigoExpresion;
	private String otExpresiones;
	private String otExpresion;
	private String switchRecalcular;
	private String ottiporg;
	
	private String codigoNombreVariableLocal;
	private String editaVariableLocal;
	private String nombreVariableLocal;
	private String claveSeleccionada;
	private String mensajeDelAction;
	
	private String mensajeValidacion;
	
	private String codigoExpresionSession;
	
	private boolean success;
	
    /**
     * Atributo agregado como parametro de la petici�n por struts que indica
     * el inicio de el n�mero de linea en cual iniciar
     */
    protected int start;
    
    /**
     * Atributo agregado como parametro de la petici�n por struts que indica la cantidad
     * de registros a ser consultados
     */
    protected int limit=20;
    
    /**
     * Atributo de respuesta interpretado por strust con el n�mero de registros totales
     * que devuelve la consulta.
     */
    protected int totalCount;
	
	
	@Override
	public String execute() throws Exception {
		session.clear();
		return INPUT;
	}
	
	public String entrarWizard() throws Exception {
		log.debug(" ******** Entrando a Wizard de Parametrizacion *********");
		return SUCCESS;
	}
	
	public String funcionesArbolJSON() throws Exception {
		listaFuncionesJson= expresionesManager.funcionesArbol();
		if(listaFuncionesJson==null){
			listaFuncionesJson = new ArrayList<HojaVO>();			
		}
		success = true;
		return SUCCESS;
	}
	public String variablesTemporalesArbolJSON() throws Exception {
		listaVariablesTemporalesJson = expresionesManager.variablesTemporalesArbol();
		
		if(listaVariablesTemporalesJson==null){
			listaVariablesTemporalesJson = new ArrayList<HojaVO>();			
		}
		
		success = true;
		return SUCCESS;
	}
	public String camposDelProductoArbolJSON() throws Exception {
		listaCamposDelProductoJson = expresionesManager.camposDelProductoArbol();
		
		if(listaCamposDelProductoJson==null){
			listaCamposDelProductoJson = new ArrayList<RamaVO>();			
		}
		
		success = true;
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String listaTablaJSON() throws Exception{
		
		PagedList pagedList = expresionesManager.listaTabla(start, limit);
		listaTablaJson = (List<LlaveValorVO>)pagedList.getItemsRangeList();
		totalCount = pagedList.getTotalItems();
		
		//listaTablaJson = expresionesManager.listaTabla();
		
			if(listaTablaJson == null){
				listaTablaJson  = new ArrayList<LlaveValorVO>();			
			}
			
		session.put("LISTA_TABLA", listaTablaJson);
		success = true;
		return SUCCESS;
	}

	public String listaColumnaJSON() throws Exception{

		if(tabla != null && StringUtils.isNotBlank(tabla) && !tabla.equals("undefined")){
			listaColumnaJson=expresionesManager.listaColumna(tabla);
			
		}else{
			listaColumnaJson = new ArrayList<LlaveValorVO>();
		}
		session.put("LISTA_COLUMNA", listaColumnaJson);
		success = true;
		return SUCCESS;
	}
	
	
	
	public String limpiarSesion(){
		success = true;
		
		if(session.containsKey("LISTA_CLAVES_COMPLETA"))listaClave = (List<ClaveVO>) session.get("LISTA_CLAVES_COMPLETA");
		else return SUCCESS;
		
		if(StringUtils.isBlank(codigoExpresion)){
			if(log.isDebugEnabled())log.debug("CdExpress nulo! para limpiar session");
			listaClave = null;
			session.remove("LISTA_CLAVES_COMPLETA");
			return SUCCESS;
		}
		
		
		
		if(log.isDebugEnabled())log.debug("Lista completa de LISTA CLAVE para limpiar session: "+ listaClave);
		
		if(listaClave != null && !listaClave.isEmpty()){
			
			List<ExpresionVO> listaExpresionOriginal = expresionesManager.obtieneExpresion(Integer.parseInt(codigoExpresion));
			
			if(listaExpresionOriginal!=null && !listaExpresionOriginal.isEmpty()){
				ExpresionVO expresionOriginal =  listaExpresionOriginal.get(0);
				descripcion = expresionOriginal.getOtExpresion();
				if(StringUtils.isBlank(descripcion)){
					if(log.isDebugEnabled())log.debug("No se pudo Obtener la Expresion para el cdExpress: "+ codigoExpresion);
					return SUCCESS;
				}
				if(log.isDebugEnabled())log.debug("Codigo Original para cdExpress: " + codigoExpresion + "es: " + descripcion);
			}
			
			try {
					for(ClaveVO cvo: listaClave){
						if(StringUtils.isNotBlank(cvo.getCodigoVariable())){
							WrapperResultados mensaje =  expresionesManager.validarExpresion(Integer.parseInt(codigoExpresion), descripcion, "P", cvo.getCodigoVariable());
							if(mensaje != null){
								mensajeDelAction = mensaje.getMsgText();
								if(StringUtils.isNotBlank(mensaje.getMsgId())){
									 if(!mensaje.getMsgId().equals("0")){
										 
										 if(log.isDebugEnabled()) log.debug("ELIMINANDO VARIABLE DE BD: "+ cvo.getCodigoVariable());
										 
										 Map<String, String> params =  new HashMap<String, String>();
										 params.put("CODIGO_EXPRESION", codigoExpresion);
										 params.put("CODIGO_VARIABLE", cvo.getCodigoVariable());
										 params.put("PV_CDEXPRES_I", codigoExpresion);
										 params.put("PV_CDVARIAB_I", cvo.getCodigoVariable());
										 expresionesManager.eliminarVariableLocalExpresion(params);
									 }
									 if(log.isDebugEnabled())log.debug("mensajeDelAction: "+ mensajeDelAction);
								}else {
									if(log.isDebugEnabled())log.debug("Error al validar clave. Consulte a su soporte");
								}
							}else {
								if(log.isDebugEnabled())log.debug("Error al validar clave. Consulte a su soporte");
							}
							
							
						}
					}
				
			} catch (Exception exception) {
				
				mensajeDelAction = exception.getMessage();
			}
				
		}
		log.debug("mensajeDelAction Final: "+ mensajeDelAction);
		session.remove("LISTA_CLAVES_COMPLETA");

		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String validarExpresion()
	{
		log.info(""
				+ "\n##############################"
				+ "\n###### validarExpresion ######"
				);
		try {
			log.info("codigoExpresion: "+codigoExpresion);
			log.info("descripcion: "+descripcion);
			WrapperResultados mensaje =  expresionesManager.validarExpresion(Integer.parseInt(codigoExpresion), descripcion, "P", "0");
			log.debug("respuesta: "+mensaje);
			if(mensaje != null)
			{
				mensajeValidacion = mensaje.getMsgText();
				if(StringUtils.isNotBlank(mensaje.getMsgId()))
				{
					 if(!mensaje.getMsgId().equals("0"))
					 {
						 success = false;
					 }
					 else
					 {
						 success = true;
					 }
				}
				else
				{
					success = false;
				}
			}
			else
			{
				success = false;
				mensajeValidacion = "Error al validar. Consulte a su soporte";
			}
			 
			log.debug("Valida expresion en metodo validar 1 " + mensajeValidacion);
			
			/*if (StringUtils.isBlank(mensajeValidacion)) {
				mensajeValidacion = "";//"Sintaxis correcta";
				success = true;
			}*/
		}
		catch (Exception ex)
		{
			success = false;
			mensajeValidacion = ex.getMessage();
		}
		log.info(""
				+ "\n###### validarExpresion ######"
				+ "\n##############################"
				);
		return SUCCESS;
	}

	public String listaClaveJSON() throws Exception{
		if(session.containsKey("CATALOGO_VARIABLES") && !((List)session.get("CATALOGO_VARIABLES")).isEmpty()
				&& nombreVariableLocal!=null && StringUtils.isNotBlank(nombreVariableLocal)&& !nombreVariableLocal.equals("undefined")){
			
			List<VariableVO> variablesLocalesTemporales =(List<VariableVO>)session.get("CATALOGO_VARIABLES");
			for(VariableVO variableLocal: variablesLocalesTemporales){
				if(variableLocal.getCodigoVariable().equals(nombreVariableLocal))
					listaClave = variableLocal.getClaves();
			}
			
		}else{
			log.debug("tabla="+tabla);
			log.debug("columna="+columna);
			if(tabla != null && StringUtils.isNotBlank(tabla) && !tabla.equals("undefined")
					&& columna != null && StringUtils.isNotBlank(columna) && !columna.equals("undefined")){
				listaClave = expresionesManager.listaClave(tabla);
			}
		}
		if( listaClave == null )
			listaClave = new ArrayList<ClaveVO>();
		if(listaClaveJson==null)
			listaClaveJson = new ArrayList<LlaveValorVO>();
		session.put("LISTA_CLAVE", listaClave);
		success = true;
		return SUCCESS;
	}
	public String listaEditableGridClavesJSON() throws Exception{
		if(tabla != null && StringUtils.isNotBlank(tabla) && !tabla.equals("undefined")
				&& columna != null && StringUtils.isNotBlank(columna) && !columna.equals("undefined")){
			listaClave= expresionesManager.listaClave(tabla);
			log.debug("Clave en el action"+listaClave.get(0).getClave());
			List<LlaveValorVO> listaClaves=new ArrayList<LlaveValorVO>();
			LlaveValorVO comboVO=null;
			
			for(ClaveVO claveVO:listaClave){
				comboVO =new LlaveValorVO();
				comboVO.setKey(claveVO.getClave());
				comboVO.setValue(claveVO.getClave());
				listaClaves.add(comboVO);
			}
			listaClaveJson= listaClaves;
			log.debug("clave del json a mandar"+listaClaveJson.get(0).getValue());
		}else{
			listaClave = new ArrayList<ClaveVO>();
			listaClaveJson = new ArrayList<LlaveValorVO>();
		}
		session.put("LISTA_CLAVE", listaClave);
		success = true;
		return SUCCESS;
	}
	
	public String listaComboVariablesJSON()throws Exception{
		listaComboVariables = expresionesManager.obtieneVariableExpresion(Integer.parseInt(codigoExpresion));
		
		listaClave = new ArrayList<ClaveVO>();
		if(listaComboVariables!=null && !listaComboVariables.isEmpty()){
			for(VariableVO vvo: listaComboVariables){
				if(vvo.getClaves()!=null && !vvo.getClaves().isEmpty()){
					for(ClaveVO cvo: vvo.getClaves()){
						if(cvo.getRecalcular()!=null && cvo.getRecalcular().equals("S"))
							cvo.setSwitchRecalcualar(true);
					}
				}
				ClaveVO addKey = new ClaveVO();
				addKey.setCodigoVariable(vvo.getCodigoVariable());
				listaClave.add(addKey);
			}
		}
		
		log.debug("SUBIENDO LISTA CLAVES COMPLETA: "+ listaClave);
		session.put("LISTA_CLAVES_COMPLETA", listaClave);
		
		
		
		session.put("CATALOGO_VARIABLES", listaComboVariables);
		success = true;
		return SUCCESS;
	}
	public String llenarClave() throws Exception{
		List<ClaveVO> temp=(ArrayList<ClaveVO>)session.get("LISTA_CLAVE");
		log.debug("dentro de llenar clave");
		log.debug("clave"+clave);
		if(recalcular!=null){
			recalcular="S";
		}else{
			recalcular="N";
		}
		log.debug(recalcular);
		for(ClaveVO claveVO:temp){
			if(claveVO.getClave().equals(clave)){
				claveVO.setExpresion(expresion);
				claveVO.setRecalcular(recalcular);
			}			
		}
		
		session.put("LISTA_CLAVE", temp);
		success=true;
		return SUCCESS;
	}

	public String agregarVariable() throws Exception{
		success = true;
		mensajeDelAction = "failure";

		success = true;
		log.debug("codigoNombreVariableLocal = " + codigoNombreVariableLocal);
		log.debug("claveSeleccionada = " + claveSeleccionada);
		log.debug("codigoExpresion = " + codigoExpresion);

		VariableVO variableVO= new VariableVO();		
		variableVO.setColumna(Integer.parseInt(columna));
		variableVO.setCodigoVariable(claveSeleccionada);
		variableVO.setTabla(tabla);
		variableVO.setDescripcionColumna(descripcionColumna);
		variableVO.setDescripcionTabla(descripcionTabla);
		variableVO.setSwitchFormato(switchFormato);

		log.debug("listaClave = " + listaClave.toString());
		if(listaClave!=null && !listaClave.isEmpty()){
			//Se recuperan los valores '+' '&' que fueron codificados al enviar los params (si es que los hay)
			for(ClaveVO clave: listaClave){
				clave.setExpresion( decodificaCaracteresConConflicto(clave.getExpresion()) );
			}
			
			try {
				mensajeDelAction="";
				boolean validaPrimero = false;
				log.debug("lista clave sigue su viaje + listaClave = " + listaClave);
				if(listaClave!=null && !listaClave.isEmpty()){
					log.debug("lista claves !=null && isNotEmpty()");
					for(ClaveVO cvo: listaClave){
						log.debug("Valida expresion" + cvo.getCodigoExpresionKey());
							//if(cvo.getCodigoExpresionKey()!=null && StringUtils.isNotBlank(cvo.getCodigoExpresionKey())){	
//								mensajeValidacion =expresionesManager.validarExpresion(Integer.parseInt(cvo.getCodigoExpresionKey()), cvo.getExpresion(), "K");
							int codigoExpres = expresionesManager.obtieneSecuenciaExpresion();
							
							WrapperResultados mensaje =  expresionesManager.validarExpresion(codigoExpres, cvo.getExpresion(), "K", "0");
							
							log.debug("mensajes resultantes: HHH text : "+ mensaje.getMsgText());
							log.debug("mensajes resultantes: HHH id : "+ mensaje.getMsgId());
							
							if(mensaje != null){
								mensajeDelAction = mensaje.getMsgText();
								if(StringUtils.isNotBlank(mensaje.getMsgId())){
									 if(!mensaje.getMsgId().equals("0"))success = false;
									 	else success = true; 
								}else {
									success = false;
									mensajeDelAction = "Error al validar. Consulte a su soporte";
									log.debug("PROBLEMA EN 1HHH");
								}
							}else {
								success = false;
								mensajeDelAction = "Error al validar. Consulte a su soporte";
								log.debug("PROBLEMA EN 2HHH");
							}
							
							try{
								cvo.setCodigoExpresionKey(Integer.toString(codigoExpres));
							}catch(Exception e){
								if(log.isDebugEnabled())log.debug("Error al obtener el nuevo codigo de expresion", e);
							}
							
							log.debug("lista clave sigue su viajeHHH + listaClave = " + listaClave);
							if(!success)break;
					}
				}
			} catch (Exception exception) {
				success = false;
				mensajeDelAction = exception.getMessage();
			}
				
			if(success){
				List<VariableVO> variables = new ArrayList<VariableVO>();
				variables.add(variableVO);
				try {
					Map<String, String> params =  new HashMap<String, String>();
					params.put("CODIGO_EXPRESION", codigoExpresion);
					params.put("CODIGO_VARIABLE", claveSeleccionada);
					params.put("PV_CDEXPRES_I", codigoExpresion);
					params.put("PV_CDVARIAB_I", claveSeleccionada);
					expresionesManager.eliminarVariableLocalExpresion(params);
				} catch (Exception exception) {
					log.debug("si trono");
				}
				
				success = expresionesManager.agregarVariables(variables, Integer.parseInt(codigoExpresion), ottiporg);
				
				if(success){
					success = expresionesManager.agregarClave(listaClave, Integer.parseInt(codigoExpresion), variableVO.getCodigoVariable(), ottiporg);
					if(success){
						mensajeDelAction = "Variable guardada exitosamente";
					}else {
						mensajeDelAction = "Error al guardar las claves. Consulte a su soporte";
					}
						
				}
				else {
					mensajeDelAction = "Error al guardar la variable. Consulte a su soporte";
				}
				/*
				listaComboVariables = expresionesManager.obtieneVariableExpresion(Integer.parseInt(codigoExpresion));
				if(listaComboVariables!=null && !listaComboVariables.isEmpty()){
					for(VariableVO vvo: listaComboVariables){
						log.debug("vvo.getCodigoVariable() = " + vvo.getCodigoVariable());
						if(vvo.getCodigoVariable().toUpperCase().equals(claveSeleccionada.toUpperCase())){
							listaClave = vvo.getClaves();
							log.debug("vvo.getClaves() = " + listaClave);
						}
					}
				}*/
				
			}
			/*
			for(ClaveVO clave: listaClave){				
				if("S".equals(clave.getRecalcular())){
					clave.setSwitchRecalcualar(true);
				}else{
					clave.setSwitchRecalcualar(false);
				}
			}				
			variableVO.setClaves(listaClave);
			*/
		}
		log.debug("mensajeDelAction = " + mensajeDelAction);
		return SUCCESS;
	}
	
	/**
	 * Devuelve el valor '+' y '&' a las cadenas codificadas con '@PLUS@' y '@AMP@' respectivamente.
	 * Se codificaron estos caracteres para que no tuvieran problemas en el envio de params. 
	 * @param cadena
	 */
	private String decodificaCaracteresConConflicto(String cadena){
		return cadena.replaceAll("@PLUS@", "+").replaceAll("@AMP@", "&");
		//return StringUtils.replace(cadena, "@PLUS@","+");
	}
	
	public String agregarExpresion() throws Exception{
		
		log.info(""
				+ "\n########################"
				+ "\n### agregarExpresion ###"
				);
		log.debug(descripcion);
		otExpresion=descripcion;
		otExpresiones=descripcion;
		mensajeDelAction = "failure";
		//success=true;
		log.debug("codigoExpresionSession"+codigoExpresionSession);
		if(!(codigoExpresionSession != null  && StringUtils.isNotBlank(codigoExpresionSession) && codigoExpresionSession.equals("undefined")))
			codigoExpresionSession = "EXPRESION";
		
		//codigoExpresion="1";
		if(switchRecalcular==null){
			switchRecalcular="N";
		}else if(switchRecalcular.equals("on")){
				switchRecalcular="S";			
		}
		if(codigoExpresion==null || StringUtils.isBlank(codigoExpresion) || codigoExpresion.equals("undefined")){
			codigoExpresion="0";
		}
		log.debug("session.containsKey('CATALOGO_VARIABLES')="+session.containsKey("CATALOGO_VARIABLES"));
		if(session.containsKey("CATALOGO_VARIABLES"))
		{
			log.debug("session.get('CATALOGO_VARIABLES')="+session.get("CATALOGO_VARIABLES"));
			if(session.get("CATALOGO_VARIABLES")!=null)
			{
				log.debug("((List)session.get('CATALOGO_VARIABLES')).isEmpty()="+((List)session.get("CATALOGO_VARIABLES")).isEmpty());				
			}
		}
		log.debug("switchRecalcular="+switchRecalcular);
		ExpresionVO exp = new ExpresionVO();
		exp.setCodigoExpresion(Integer.parseInt(codigoExpresion));
		exp.setOtExpresion(otExpresion);
		exp.setOtExpresiones(otExpresiones);
		exp.setSwitchRecalcular(switchRecalcular);
		exp.setOttiporg(ottiporg);
		exp.setOttipexp("P");//PADRE
		try
		{
			log.debug("Valida expresion: " + codigoExpresion);
			
			WrapperResultados mensajeExp = expresionesManager.validarExpresion(Integer.parseInt(codigoExpresion), descripcion, "P", "0");
			if(mensajeExp != null)
			{
				mensajeDelAction = mensajeExp.getMsgText();
				if(StringUtils.isNotBlank(mensajeExp.getMsgId()))
				{
					if(!mensajeExp.getMsgId().equals("0"))
					{
						success = false;
					}
					else
					{
						success = true;
					}
				}
				else
				{
					success = false;
				}
			}
			else
			{
				success = false;
				mensajeDelAction = "Error al validar. Consulte a su soporte";
			}
			
			session.put(codigoExpresionSession, exp.getCodigoExpresion());
		
			if(success)
			{
				if(session.containsKey("LISTA_CLAVES_COMPLETA"))
				{
					listaClave = (List<ClaveVO>) session.get("LISTA_CLAVES_COMPLETA");
				}
				//listaClave = null;
				log.debug("Lista completa de LISTA CLAVE: "+ listaClave); 
				if(listaClave != null && !listaClave.isEmpty())
				{
					try
					{
						for(ClaveVO cvo: listaClave)
						{
							if(/*StringUtils.isNotBlank(cvo.getCodigoExpresionKey())&& */
									StringUtils.isNotBlank(cvo.getCodigoVariable()))
							{
								WrapperResultados mensaje = expresionesManager.validarExpresion(
										Integer.parseInt(/*cvo.getCodigoExpresionKey()*/codigoExpresion),
										descripcion,
										"P",
										cvo.getCodigoVariable());
								if(mensaje != null)
								{
									mensajeDelAction = mensaje.getMsgText();
									if(StringUtils.isNotBlank(mensaje.getMsgId()))
									{
										if(!mensaje.getMsgId().equals("0"))
										{
											success = false;
										}
										else
										{
											success = true;
										}
									}
									else
									{
										success = false;
										mensajeDelAction = "Error al validar clave. Consulte a su soporte";
									}
								}
								else
								{
									success = false;
									mensajeDelAction = "Error al validar clave. Consulte a su soporte";
								}
								if(!success)break;
							}
						}
					}
					catch (Exception exception)
					{
						success = false;
						mensajeDelAction = exception.getMessage();
					}	
				}
			}
		}
		catch (Exception exception)
		{
			success = false;
			mensajeDelAction = exception.getMessage();
			log.debug("before validate");
			List<String> banderaVariablesTemporales = validaVariablesTemporales();		
			log.debug("after validate +banderaVariablesTemporales="+banderaVariablesTemporales);
			if(banderaVariablesTemporales!=null  && !banderaVariablesTemporales.isEmpty())
			{
				mensajeDelAction+=" \nLas variables locales que no \n utilicen en la expresion \ndebe borrarlas: ";
				for(String variableEnDesuso: banderaVariablesTemporales)
				{
					mensajeDelAction+="\n"+variableEnDesuso;
				}
			}
		}
		
//		if(mensajeDelAction!=null && StringUtils.isNotBlank(mensajeDelAction) && !mensajeDelAction.equals("failure"))
//			success = false;			
//		if (StringUtils.isBlank(mensajeDelAction) || mensajeDelAction.equals("Registro Creado"))
//			success = true;
		if(success)
		{
			int codigoExpresion = 0;
			try
			{
				codigoExpresion = insertarExpresion(success, exp);
			}
			catch(Exception exception)
			{
				log.error("Error:" + exception.getMessage(), exception);
			}
			log.debug("En agregar expresion despues de guardar expresion, codigoExpresion = " + codigoExpresion);
		}
		log.debug("mensajeDelAction = " + mensajeDelAction);
		log.info(""
				+ "\n### agregarExpresion ###"
				+ "\n########################"
				);
		return SUCCESS;
	}
	
	public List<String> validaVariablesTemporales()
	{
		log.debug("entry validate temporal variables");
		List<String> variablesEnDesuso = new ArrayList<String>();
		if(session.containsKey("CATALOGO_VARIABLES") && !((List<VariableVO>)session.get("CATALOGO_VARIABLES")).isEmpty()){
			log.debug("first if + descripcion = " + descripcion);
			List<VariableVO> variablesTemporales= (List<VariableVO>) session.get("CATALOGO_VARIABLES");
			log.debug("CATALOGO_VARIABLES="+variablesTemporales);
			String valida;
			for(VariableVO vvo: variablesTemporales){
				log.debug("code temporal variables="+vvo.getCodigoVariable());
				valida = "$"+vvo.getCodigoVariable();
				if(!descripcion.toUpperCase().contains(valida.toUpperCase())){
					log.debug("second if");
					variablesEnDesuso.add(vvo.getCodigoVariable());
				}else{
					log.debug("second else");
				}	
			}
		}else{
			log.debug("first else");
		}
		return variablesEnDesuso;
	}
	public String editarExpresion() throws Exception
	{
		log.debug(""
				+ "\n#############################"
				+ "\n###### editarExpresion ######"
				);
		log.debug("codigoExpresionSession"+codigoExpresionSession);
		session.remove("");
		if( codigoExpresion!=null && StringUtils.isNotBlank(codigoExpresion) ){
			editarExpresion = cargaExpresion(Integer.parseInt(codigoExpresion));			
			if (editarExpresion.size() == 0){
				ExpresionVO expresion = new ExpresionVO();
				expresion.setCodigoExpresion(Integer.parseInt(codigoExpresion));
				editarExpresion.add(expresion);
			}
			listaComboVariables = cargaVariablesExpresion (Integer.parseInt(codigoExpresion));
			session.put("CATALOGO_VARIABLES", listaComboVariables);
		}else{
			ExpresionVO evo = new ExpresionVO();
			evo.setCodigoExpresion(0);
			evo.setOtExpresion("");
			evo.setOtExpresiones("");
			evo.setSwitchRecalcular("N");
			session.put("CATALOGO_VARIABLES", new ArrayList<VariableVO>());
			editarExpresion.add(evo);
		}
		success = true;
		log.debug(""
				+ "\n###### editarExpresion ######"
				+ "\n#############################"
				);
		return SUCCESS;
	}
	public String eliminarVariable() throws Exception{
		log.debug("Clave Seleccionada = " + claveSeleccionada);		
		log.debug("Codigo expreison = " + codigoExpresion);
		Map<String, String> params =  new HashMap<String, String>();
		params.put("CODIGO_EXPRESION", codigoExpresion);
		params.put("CODIGO_VARIABLE", claveSeleccionada);
		params.put("PV_CDEXPRES_I", codigoExpresion);
		params.put("PV_CDVARIAB_I", claveSeleccionada);
		expresionesManager.eliminarVariableLocalExpresion(params);

		success = true;
		return SUCCESS;
	}
	
	/**
	 * Metodo para saber si existe una Expresion en BD dada su clave
	 * @return
	 * @throws Exception
	 */
	public String existeExpresion() throws Exception{
		success = expresionesManager.existeExpresion(codigoExpresion);

		return SUCCESS;
	}
	
	public String eliminarExpresionSession() throws Exception{
		log.debug("codigoExpresionSession"+codigoExpresionSession);
		if(!(codigoExpresionSession != null  && StringUtils.isNotBlank(codigoExpresionSession) && codigoExpresionSession.equals("undefined")))
			codigoExpresionSession = "EXPRESION";
		if(session.containsKey(codigoExpresionSession))
			session.remove(codigoExpresionSession);
		if(session.containsKey("CATALOGO_VARIABLES"))			
			session.remove("CATALOGO_VARIABLES");
			
		success = true;
		return SUCCESS;
	}
	
	//getters && setters
	public void setExpresionesManager(ExpresionesManager expresionesManager) {
		this.expresionesManager = expresionesManager;
	}

	public List<HojaVO> getListaFuncionesJson() {
		return listaFuncionesJson;
	}

	public void setListaFuncionesJson(List<HojaVO> listaFuncionesJson) {
		this.listaFuncionesJson = listaFuncionesJson;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<HojaVO> getListaVariablesTemporalesJson() {
		return listaVariablesTemporalesJson;
	}

	public void setListaVariablesTemporalesJson(
			List<HojaVO> listaVariablesTemporalesJson) {
		this.listaVariablesTemporalesJson = listaVariablesTemporalesJson;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getColumna() {
		return columna;
	}

	public void setColumna(String columna) {
		this.columna = columna;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getRecalcular() {
		return recalcular;
	}

	public void setRecalcular(String recalcular) {
		this.recalcular = recalcular;
	}

	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion = expresion;
	}

	public String getCodigoExpresion() {
		return codigoExpresion;
	}

	public void setCodigoExpresion(String codigoExpresion) {
		this.codigoExpresion = codigoExpresion;
	}

	public String getOtExpresiones() {
		return otExpresiones;
	}

	public void setOtExpresiones(String otExpresiones) {
		this.otExpresiones = otExpresiones;
	}

	public String getOtExpresion() {
		return otExpresion;
	}

	public void setOtExpresion(String otExpresion) {
		this.otExpresion = otExpresion;
	}

	public String getSwitchRecalcular() {
		return switchRecalcular;
	}

	public void setSwitchRecalcular(String switchRecalcular) {
		this.switchRecalcular = switchRecalcular;
	}

	public List<VariableVO> getListaComboVariables() {
		return listaComboVariables;
	}

	public void setListaComboVariables(List<VariableVO> listaComboVariables) {
		this.listaComboVariables = listaComboVariables;
	}

	public List<LlaveValorVO> getListaTablaJson() {
		return listaTablaJson;
	}

	public void setListaTablaJson(List<LlaveValorVO> listaTablaJson) {
		this.listaTablaJson = listaTablaJson;
	}

	public List<LlaveValorVO> getListaColumnaJson() {
		return listaColumnaJson;
	}

	public void setListaColumnaJson(List<LlaveValorVO> listaColumnaJson) {
		this.listaColumnaJson = listaColumnaJson;
	}

	public List<LlaveValorVO> getListaClaveJson() {
		return listaClaveJson;
	}

	public void setListaClaveJson(List<LlaveValorVO> listaClaveJson) {
		this.listaClaveJson = listaClaveJson;
	}

	public String getExpresionId() {
		return expresionId;
	}

	public void setExpresionId(String expresionId) {
		this.expresionId = expresionId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<ClaveVO> getListaPropertyGridJson() {
		return listaPropertyGridJson;
	}

	public void setListaPropertyGridJson(List<ClaveVO> listaPropertyGridJson) {
		this.listaPropertyGridJson = listaPropertyGridJson;
	}

	public List<ClaveVO> getListaClave() {
		return listaClave;
	}

	public void setListaClave(List<ClaveVO> listaClave) {
		this.listaClave = listaClave;
	}

	public String getClaveSeleccionada() {
		return claveSeleccionada;
	}

	public void setClaveSeleccionada(String claveSeleccionada) {
		this.claveSeleccionada = claveSeleccionada;
	}

	public void setListaCamposDelProductoJson(
			List<RamaVO> listaCamposDelProductoJson) {
		this.listaCamposDelProductoJson = listaCamposDelProductoJson;
	}

	public String getMensajeDelAction() {
		return mensajeDelAction;
	}

	public void setMensajeDelAction(String mensajeDelAction) {
		this.mensajeDelAction = mensajeDelAction;
	}

	public String getDescripcionTabla() {
		return descripcionTabla;
	}

	public void setDescripcionTabla(String descripcionTabla) {
		this.descripcionTabla = descripcionTabla;
	}

	public String getDescripcionColumna() {
		return descripcionColumna;
	}

	public void setDescripcionColumna(String descripcionColumna) {
		this.descripcionColumna = descripcionColumna;
	}

	public List<ExpresionVO> getEditarExpresion() {
		return editarExpresion;
	}

	public void setEditarExpresion(List<ExpresionVO> editarExpresion) {
		this.editarExpresion = editarExpresion;
	}

	public String getIdAction() {
		return idAction;
	}

	public void setIdAction(String idAction) {
		this.idAction = idAction;
	}

	public String getNombreVariableLocal() {
		return nombreVariableLocal;
	}

	public void setNombreVariableLocal(String nombreVariableLocal) {
		this.nombreVariableLocal = nombreVariableLocal;
	}

	public String getCodigoNombreVariableLocal() {
		return codigoNombreVariableLocal;
	}

	public void setCodigoNombreVariableLocal(String codigoNombreVariableLocal) {
		this.codigoNombreVariableLocal = codigoNombreVariableLocal;
	}

	public String getEditaVariableLocal() {
		return editaVariableLocal;
	}

	public void setEditaVariableLocal(String editaVariableLocal) {
		this.editaVariableLocal = editaVariableLocal;
	}

	public String getCodigoExpresionSession() {
		return codigoExpresionSession;
	}

	public void setCodigoExpresionSession(String codigoExpresionSession) {
		this.codigoExpresionSession = codigoExpresionSession;
	}

	public String getSwitchFormato() {
		return switchFormato;
	}

	public void setSwitchFormato(String switchFormato) {
		this.switchFormato = switchFormato;
	}

	/**
	 * @return the mensajeValidacion
	 */
	public String getMensajeValidacion() {
		return mensajeValidacion;
	}

	/**
	 * @param mensajeValidacion the mensajeValidacion to set
	 */
	public void setMensajeValidacion(String mensajeValidacion) {
		this.mensajeValidacion = mensajeValidacion;
	}

	public String getOttiporg() {
		return ottiporg;
	}

	public void setOttiporg(String ottiporg) {
		this.ottiporg = ottiporg;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	

}