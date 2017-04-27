package mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.tmp.FormatoFecha;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TablaCincoClavesManagerJdbcTemplate;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionVeinticincoAtributosVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.ValoresCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.util.WizardUtils;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ValoresTablaCincoClavesAction extends Padre {

	private static final long serialVersionUID = -6495412115296934178L;
	private static final transient Log log = LogFactory
			.getLog(ValoresTablaCincoClavesAction.class);

	private boolean success;
	private TablaCincoClavesManagerJdbcTemplate tablaCincoClavesManagerJdbcTemplate;
	
	//Lista que guarda las claves 
	private List<DescripcionCincoClavesVO> listaClavesEditable;
	private List<LlaveValorVO> listaAtributosEditable;
	
	private String numeroFila;
	private String codigoClaveEditable;
	private String codigoClaveEditableTemporal;
	private String test;
	private int testInt = -1;
	// Atribtus de la cabecera
	private String numEditable;
	private String nombreEditable;
	private String descripcionEditable;
	// Atributos de descripcion cinco claves
	private String descripcionClave1;
	private String descripcionClave2;
	private String descripcionClave3;
	private String descripcionClave4;
	private String descripcionClave5;
	private String fechaDesde;
	private String fechaHasta;
	//Atributos para guardar descripciones editable
	private String identificador;
	// atributos de descripcion veinti cinco atributos
	private String descripcion1;
	private String descripcion2;
	private String descripcion3;
	private String descripcion4;
	private String descripcion5;
	private String descripcion6;
	private String descripcion7;
	private String descripcion8;
	private String descripcion9;
	private String descripcion10;
	private String descripcion11;
	private String descripcion12;
	private String descripcion13;
	private String descripcion14;
	private String descripcion15;
	private String descripcion16;
	private String descripcion17;
	private String descripcion18;
	private String descripcion19;
	private String descripcion20;
	private String descripcion21;
	private String descripcion22;
	private String descripcion23;
	private String descripcion24;
	private String descripcion25;
	//atributos para guardar el grid editable de atributos
	private String key;
	private String value;
	private String nick;
	
	private String mensajeResultado; 
	
	//Atributos de los grids ditables
	private List<DescripcionCincoClavesVO> listaParamsClaves;

	//Si se llegan a modificar las claves, las claves anteriores se guardan en la siguiente lista
	private List<DescripcionCincoClavesVO> listaParamsClavesOriginales;
	
	private List<LlaveValorVO> listaParamsAtributos;
	
	List<ValoresCincoClavesVO> listaValores5ClavesParams;
		
		
	private String numeroTabla;
	
	private int start = 0;
	private int limit = 20;
	private int totalCount;
	
	public String execute() throws Exception {

		return INPUT;
	}

	public String listaDescripcionCincoClavesJson() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("lista descripcion cinco claves Json");
		}
			if (numEditable != null) {
				try {
					PagedList pagedList = tablaCincoClavesManagerJdbcTemplate.obtieneValoresClave(numEditable, start , limit);
					listaClavesEditable = pagedList.getItemsRangeList();
					
					int i = 0; 
					for(DescripcionCincoClavesVO cincoClaves : listaClavesEditable){
						cincoClaves.setIdentificador(Integer.toString(i++));
					}
					
					if(log.isDebugEnabled())log.debug("Lista de claves inicial con sus atributos: "+ listaClavesEditable);
					setTotalCount(pagedList.getTotalItems());
					
				}catch(Exception e){
					if(log.isErrorEnabled())log.error("Error al obtener la lista de la tabla de Cinco Claves: " + e.getMessage());
					listaClavesEditable = new ArrayList<DescripcionCincoClavesVO>();
				}
				
			}else{
					listaClavesEditable = new ArrayList<DescripcionCincoClavesVO>();			
			}
		session.put("LISTA_CLAVES_EDITABLE", listaClavesEditable);
		success = true;
		return SUCCESS;
	}

	public String listaAtributosEditableJson() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if(session.containsKey("LISTA_CLAVES_EDITABLE") && StringUtils.isNotBlank(numEditable))listaClavesEditable = (List<DescripcionCincoClavesVO>) session.get("LISTA_CLAVES_EDITABLE");
		else{
			if(isDebugEnabled)log.error("Sin datos de LISTA_CLAVES_EDITABLE en Sesion");
		}
		DescripcionVeinticincoAtributosVO desctipcionAtributos = tablaCincoClavesManagerJdbcTemplate.obtieneDescripcionAtributosCincoClaves(numEditable);
		
		session.remove("LISTA_CLAVES_EDITABLE");
		
		/*
		 * Para agregar una lista dummy con todas las descripciones de los atributos, ya que si en la tabla incialmente no hay claves 
		 * fallaria la obtencion de estas descripciones en el tablaApoyo5Claves.js
		 */
		
		listaAtributosEditable = new ArrayList<LlaveValorVO>();
		listaAtributosEditable.addAll(tablaCincoClavesManagerJdbcTemplate.obtieneValorDummyAtributos("dummy" , desctipcionAtributos));
		
		
		if(listaClavesEditable!=null){
			for(DescripcionCincoClavesVO descripCincoClaves : listaClavesEditable){
				listaAtributosEditable.addAll(tablaCincoClavesManagerJdbcTemplate.obtieneValoresAtributos(numEditable , descripCincoClaves, desctipcionAtributos));
			}
		}
		
		listaClavesEditable = null;
		if(log.isDebugEnabled())log.debug("La lista de atributos final es: "+ listaAtributosEditable);
		
		success = true;
		return SUCCESS;
	}

	public String insertarValoresTablaEditable() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		ValoresCincoClavesVO vccVO= new ValoresCincoClavesVO();
		vccVO.setNumero(Integer.parseInt(numEditable));
		vccVO.setNombre(nombreEditable);
		vccVO.setDescripcion(descripcionEditable);
		if(session.containsKey("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL")&&!((List<DescripcionCincoClavesVO>)session.get("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL")).isEmpty()){
			List<DescripcionCincoClavesVO> ldceCompleta=(List<DescripcionCincoClavesVO>) session.get("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL");			
			List<DescripcionCincoClavesVO> ldceCompletaFechasYAtributos=new ArrayList<DescripcionCincoClavesVO>();			
			DescripcionCincoClavesVO dccFA=null;
			for(DescripcionCincoClavesVO dccVO: ldceCompleta){
				dccFA=dccVO;
				dccFA.setAtributos(dccVO.getAtributos());
				if(isDebugEnabled){
					log.debug("atributos"+dccFA.getAtributos());
				}
				ldceCompletaFechasYAtributos.add(dccFA);
			}
			session.put("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL", ldceCompletaFechasYAtributos);
		}
		success = true;
		return SUCCESS;
	}


	public String guardarCambiosClaves() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("Lista Params:"+listaParamsClaves);
			log.debug("numeroFila="+numeroFila+"codigoClaveEditable"+codigoClaveEditable+"codigoClaveEditableTemporal"+codigoClaveEditableTemporal);
		}
		List<DescripcionCincoClavesVO> ldccVOtemp= new ArrayList<DescripcionCincoClavesVO>();
try{
		if(session.containsKey("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL")){
			log.debug("CONTIENE DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL");
			ldccVOtemp= (List<DescripcionCincoClavesVO>) session.get("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL");
			
		}
		else
			ldccVOtemp= (List<DescripcionCincoClavesVO>) session.get("LISTA_CLAVES_EDITABLE");
		if(listaParamsClaves!=null && !listaParamsClaves.isEmpty()){
			int identificaTemp=ldccVOtemp.size();
			
			for(DescripcionCincoClavesVO dccVO: listaParamsClaves){
				if(isDebugEnabled){
					log.debug(identificaTemp);
				}
				if(isDebugEnabled){
					log.debug("Antes del if"+dccVO.getIdentificadorTemporal());
				}
				if(dccVO.getIdentificadorTemporal()!=null && (dccVO.getIdentificadorTemporal().equals("Undefinded") || StringUtils.isBlank(dccVO.getIdentificadorTemporal()))){
					dccVO.setIdentificadorTemporal(null);
					if(isDebugEnabled){
						log.debug("desdepues de setearlo"+dccVO.getIdentificadorTemporal());
					}
				}
				
				if (isDebugEnabled) {
					log.debug("DescripcionClave1: " + dccVO.getDescripcionClave1());
					log.debug("DescripcionClave2: " + dccVO.getDescripcionClave2());
					log.debug("DescripcionClave3: " + dccVO.getDescripcionClave3());
					log.debug("DescripcionClave4: " + dccVO.getDescripcionClave4());
					log.debug("DescripcionClave5: " + dccVO.getDescripcionClave5());
					log.debug("Fecha Desde: " + dccVO.getFechaDesde());
					log.debug("Fecha Hasta: " + dccVO.getFechaHasta());
					
				}
				
				if(dccVO.getDescripcionClave1()!=null && (dccVO.getDescripcionClave1().equals("Undefinded") || StringUtils.isBlank(dccVO.getDescripcionClave1() )))
					dccVO.setDescripcionClave1(null);
				if(dccVO.getDescripcionClave2()!=null && (dccVO.getDescripcionClave2().equals("Undefinded") || StringUtils.isBlank(dccVO.getDescripcionClave2())))
					dccVO.setDescripcionClave2(null);
				if(dccVO.getDescripcionClave3()!=null && (dccVO.getDescripcionClave3().equals("Undefinded") || StringUtils.isBlank(dccVO.getDescripcionClave3())))
					dccVO.setDescripcionClave3(null);
				if(dccVO.getDescripcionClave4()!=null && (dccVO.getDescripcionClave4().equals("Undefinded") || StringUtils.isBlank(dccVO.getDescripcionClave4())))
					dccVO.setDescripcionClave4(null);
				if(dccVO.getDescripcionClave5()!=null && (dccVO.getDescripcionClave5().equals("Undefinded") || StringUtils.isBlank(dccVO.getDescripcionClave5())))
					dccVO.setDescripcionClave5(null);
				if(dccVO.getFechaDesde()!=null && (dccVO.getFechaDesde().equals("Undefinded") || StringUtils.isBlank(dccVO.getFechaDesde())))
					dccVO.setFechaDesde(null);
				else if(dccVO.getFechaDesde()!=null){
					if(dccVO.getFechaDesde().contains("GMT")){
						dccVO.setFechaDesde(FormatoFecha.format(FormatoFecha.parse(StringUtils.remove(dccVO.getFechaDesde(), "GMT").substring(4))));
					}else{
					dccVO.setFechaDesde(WizardUtils.parseDateCincoClaves(dccVO.getFechaDesde()));//FormatoFecha.format(FormatoFecha.parse(StringUtils.remove(dccVO.getFechaDesde(), "GMT").substring(4))));					
					}
				}
				if(dccVO.getFechaHasta()!=null && (dccVO.getFechaHasta().equals("Undefinded") || StringUtils.isBlank(dccVO.getFechaHasta())))
					dccVO.setFechaHasta(null);			
				else if(dccVO.getFechaHasta()!=null){
					if(dccVO.getFechaHasta().contains("GMT")){
						dccVO.setFechaHasta(FormatoFecha.format(FormatoFecha.parse(StringUtils.remove(dccVO.getFechaHasta(), "GMT").substring(4))));
					}else{						
						dccVO.setFechaHasta(WizardUtils.parseDateCincoClaves(dccVO.getFechaHasta()));//FormatoFecha.format(FormatoFecha.parse(StringUtils.remove(dccVO.getFechaHasta(), "GMT").substring(4))));
					}
				}
				if (isDebugEnabled) {
					log.debug("Identificador Temporal: " + dccVO.getIdentificadorTemporal());
					log.debug("Identificador: " + dccVO.getIdentificador());
					
				}
				
				if(dccVO.getIdentificador()!=null && !dccVO.getIdentificador().equals("Undefinded") && StringUtils.isNotBlank(dccVO.getIdentificador())){
					dccVO.setIdentificadorTemporal(dccVO.getIdentificador());
					dccVO.setAtributos(ldccVOtemp.get(Integer.parseInt(dccVO.getIdentificador())).getAtributos());
					ldccVOtemp.set(Integer.parseInt(dccVO.getIdentificador()), dccVO);					
				}else if(dccVO.getIdentificadorTemporal()!=null){
					dccVO.setAtributos(ldccVOtemp.get(Integer.parseInt(dccVO.getIdentificadorTemporal())).getAtributos());
					ldccVOtemp.set(identificaTemp, dccVO);
					log.debug("despues de setearlo"+dccVO.getIdentificadorTemporal());
				}else{
					dccVO.setIdentificadorTemporal(Integer.toString(identificaTemp));
					if(isDebugEnabled){
						log.debug("IdentificadorTemporal"+dccVO.getIdentificadorTemporal());
					}
					ldccVOtemp.add(dccVO);
					identificaTemp++;					
				}
			}
		}
		if(ldccVOtemp.isEmpty()){
			ldccVOtemp= (List<DescripcionCincoClavesVO>) session.get("LISTA_CLAVES_EDITABLE");
			log.debug("CARGO LISTA_CLAVES_EDITABLE");
			
		}
		log.debug("ldccVOtemp=" + ldccVOtemp);
		////////////////////////////////////////////////////
		log.debug("numeroFila=" + numeroFila);
		log.debug("listaParamsAtributos=" + listaParamsAtributos);
		////////////////////////////////////////////////////
		ldccVOtemp.get(Integer.parseInt(numeroFila)).setIdentificadorTemporal(numeroFila);
		ldccVOtemp.get(Integer.parseInt(numeroFila)).setAtributos(listaParamsAtributos);
		log.debug("Registro al que se le setean los atributos " + ldccVOtemp.get(Integer.parseInt(numeroFila)).getDescripcionClave1());
		for(DescripcionCincoClavesVO lista: ldccVOtemp){
			if(isDebugEnabled){
				log.debug("Descripcion1= "+lista.getDescripcionClave1() + "   Atributos= "+lista.getAtributos());
			}
		}
}catch(Exception e){
	log.error("ERROR:" + e, e);
}
		session.put("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL", ldccVOtemp);		
		success=true;
		return SUCCESS;
	}
	
	
	public String insertarValoresTablaCincoClaves()throws Exception{
		
		String fechaDesdeParam;
		String fechaHastaParam;
		String fechaDesdeAnteriorParam;
		String fechaHastaAnteriorParam;
		
		for(ValoresCincoClavesVO valores5Claves: listaValores5ClavesParams){
			fechaDesdeParam = valores5Claves.getFechaDesde();
			fechaHastaParam = valores5Claves.getFechaHasta();
			fechaDesdeAnteriorParam = valores5Claves.getFechaDesdeAnterior();
			fechaHastaAnteriorParam = valores5Claves.getFechaHastaAnterior();
			
			if(StringUtils.isNotBlank(fechaDesdeParam))
				if(!fechaDesdeParam.contains("/"))valores5Claves.setFechaDesde(WizardUtils.parseDateCincoClaves(fechaDesdeParam));
			
			if(StringUtils.isNotBlank(fechaHastaParam))
				if(!fechaHastaParam.contains("/"))valores5Claves.setFechaHasta(WizardUtils.parseDateCincoClaves(fechaHastaParam));
			
			if(StringUtils.isNotBlank(fechaDesdeAnteriorParam))
				if(!fechaDesdeAnteriorParam.contains("/"))valores5Claves.setFechaDesdeAnterior(WizardUtils.parseDateCincoClaves(fechaDesdeAnteriorParam));
			
			if(StringUtils.isNotBlank(fechaHastaAnteriorParam))
				if(!fechaHastaAnteriorParam.contains("/"))valores5Claves.setFechaHastaAnterior(WizardUtils.parseDateCincoClaves(fechaHastaAnteriorParam));
			
		}
		
		
		List<DescripcionCincoClavesVO> clavesNoInsertadas = tablaCincoClavesManagerJdbcTemplate.insertaValoresCincoClaves(numeroTabla , listaValores5ClavesParams);
		
		log.debug("Las claves no insertadas son : " + clavesNoInsertadas);
		
		/**************************************************************************************************************************************
		 *  Si NO se quiere obtener los registros no insertados y mostrarlos en el mensaje de respuesta, 
		 * comentar el siguiente for y solo regresar el mensaje:"Datos Guardados correctamente, los registros duplicados no han sido guardados"
		 ****************************************************************************************************************************************/
		
		mensajeResultado = "Datos Guardados correctamente. Los registros duplicados no han sido guardados: [";
		if(clavesNoInsertadas != null && !clavesNoInsertadas.isEmpty()){
			
			for(DescripcionCincoClavesVO noInsertada : clavesNoInsertadas){
				if(!mensajeResultado.equals("Datos Guardados correctamente. Los registros duplicados no han sido guardados: [")) mensajeResultado += " [";
				
				if(StringUtils.isNotBlank(noInsertada.getDescripcionClave1())){
					if(!mensajeResultado.endsWith("["))mensajeResultado += ",";
					mensajeResultado += noInsertada.getDescripcionClave1();
				}
				
				if(StringUtils.isNotBlank(noInsertada.getDescripcionClave2())){
					if(!mensajeResultado.endsWith("["))mensajeResultado += ",";
					mensajeResultado += noInsertada.getDescripcionClave2();
				}
				
				if(StringUtils.isNotBlank(noInsertada.getDescripcionClave3())){
					if(!mensajeResultado.endsWith("["))mensajeResultado += ",";
					mensajeResultado += noInsertada.getDescripcionClave3();
				}
				
				if(StringUtils.isNotBlank(noInsertada.getDescripcionClave4())){
					if(!mensajeResultado.endsWith("["))mensajeResultado += ",";
					mensajeResultado += noInsertada.getDescripcionClave4();
				}
				
				if(StringUtils.isNotBlank(noInsertada.getDescripcionClave5())){
					if(!mensajeResultado.endsWith("["))mensajeResultado += ",";
					mensajeResultado += noInsertada.getDescripcionClave5();
				}
				
				mensajeResultado += "]";	
			}
			
		}else {
			mensajeResultado = "Datos Guardados correctamente";
		}
		
		if(log.isDebugEnabled())log.debug("MENSAJEENGFINAL: "+ mensajeResultado);
		success=true;
		return SUCCESS;
	}
	
	public String borrarValoresTablaCincoClaves(){
		
		try{
			mensajeResultado = tablaCincoClavesManagerJdbcTemplate.borraValoresClave(numeroTabla, descripcionClave1, descripcionClave2, descripcionClave3, descripcionClave4, descripcionClave5);
			addActionMessage(mensajeResultado);
			success=true;
			return SUCCESS;		
		}catch(ApplicationException e){
			mensajeResultado = e.getMessage();
			if(log.isDebugEnabled())log.error("Error al intentar borrar el registro : "+ mensajeResultado);
			success=false;
			return SUCCESS;
		}catch(Exception e){
			mensajeResultado = e.getMessage();
			if(log.isDebugEnabled())log.error("Error al intentar borrar el registro : "+ mensajeResultado);
            success = false;
            return SUCCESS;
        }
	}
	
	public String limpiarSesionTablaCincoClaves() throws Exception{
    	if(log.isDebugEnabled()){
    		log.debug("borrando sesion 5 claves");
    	}
    	if(session.containsKey("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL"))
    		session.remove("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL"); 
    	if(session.containsKey("LISTA_CLAVES_EDITABLE"))
    		session.remove("LISTA_CLAVES_EDITABLE"); 
    	
    	success = true;
    	return SUCCESS;
    }
	
	
	// Getters && Setters

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the listaClavesEditable
	 */
	public List<DescripcionCincoClavesVO> getListaClavesEditable() {
		return listaClavesEditable;
	}

	/**
	 * @param listaClavesEditable
	 *            the listaClavesEditable to set
	 */
	public void setListaClavesEditable(
			List<DescripcionCincoClavesVO> listaClavesEditable) {
		this.listaClavesEditable = listaClavesEditable;
	}

	/**
	 * @return the listaAtributosEditable
	 */
	public List<LlaveValorVO> getListaAtributosEditable() {
		return listaAtributosEditable;
	}

	/**
	 * @param listaAtributosEditable
	 *            the listaAtributosEditable to set
	 */
	public void setListaAtributosEditable(
			List<LlaveValorVO> listaAtributosEditable) {
		this.listaAtributosEditable = listaAtributosEditable;
	}

	/**
	 * @return the test
	 */
	public String getTest() {
		return test;
	}

	/**
	 * @param test
	 *            the test to set
	 */
	public void setTest(String test) {
		this.test = test;
	}

	/**
	 * @return the testInt
	 */
	public int getTestInt() {
		return testInt;
	}

	/**
	 * @param testInt
	 *            the testInt to set
	 */
	public void setTestInt(int testInt) {
		this.testInt = testInt;
	}

	/**
	 * @return the codigoClaveEditable
	 */
	public String getCodigoClaveEditable() {
		return codigoClaveEditable;
	}

	/**
	 * @param codigoClaveEditable
	 *            the codigoClaveEditable to set
	 */
	public void setCodigoClaveEditable(String codigoClaveEditable) {
		this.codigoClaveEditable = codigoClaveEditable;
	}

	/**
	 * @return the descripcionClave1
	 */
	public String getDescripcionClave1() {
		return descripcionClave1;
	}

	/**
	 * @param descripcionClave1
	 *            the descripcionClave1 to set
	 */
	public void setDescripcionClave1(String descripcionClave1) {
		this.descripcionClave1 = descripcionClave1;
	}

	/**
	 * @return the descripcionClave2
	 */
	public String getDescripcionClave2() {
		return descripcionClave2;
	}

	/**
	 * @param descripcionClave2
	 *            the descripcionClave2 to set
	 */
	public void setDescripcionClave2(String descripcionClave2) {
		this.descripcionClave2 = descripcionClave2;
	}

	/**
	 * @return the descripcionClave3
	 */
	public String getDescripcionClave3() {
		return descripcionClave3;
	}

	/**
	 * @param descripcionClave3
	 *            the descripcionClave3 to set
	 */
	public void setDescripcionClave3(String descripcionClave3) {
		this.descripcionClave3 = descripcionClave3;
	}

	/**
	 * @return the descripcionClave4
	 */
	public String getDescripcionClave4() {
		return descripcionClave4;
	}

	/**
	 * @param descripcionClave4
	 *            the descripcionClave4 to set
	 */
	public void setDescripcionClave4(String descripcionClave4) {
		this.descripcionClave4 = descripcionClave4;
	}

	/**
	 * @return the descripcionClave5
	 */
	public String getDescripcionClave5() {
		return descripcionClave5;
	}

	/**
	 * @param descripcionClave5
	 *            the descripcionClave5 to set
	 */
	public void setDescripcionClave5(String descripcionClave5) {
		this.descripcionClave5 = descripcionClave5;
	}

	/**
	 * @return the fechaDesde
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}

	/**
	 * @param fechaDesde
	 *            the fechaDesde to set
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	/**
	 * @return the fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}

	/**
	 * @param fechaHasta
	 *            the fechaHasta to set
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	/**
	 * @return the descripcion1
	 */
	public String getDescripcion1() {
		return descripcion1;
	}

	/**
	 * @param descripcion1
	 *            the descripcion1 to set
	 */
	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}

	/**
	 * @return the descripcion2
	 */
	public String getDescripcion2() {
		return descripcion2;
	}

	/**
	 * @param descripcion2
	 *            the descripcion2 to set
	 */
	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}

	/**
	 * @return the descripcion3
	 */
	public String getDescripcion3() {
		return descripcion3;
	}

	/**
	 * @param descripcion3
	 *            the descripcion3 to set
	 */
	public void setDescripcion3(String descripcion3) {
		this.descripcion3 = descripcion3;
	}

	/**
	 * @return the descripcion4
	 */
	public String getDescripcion4() {
		return descripcion4;
	}

	/**
	 * @param descripcion4
	 *            the descripcion4 to set
	 */
	public void setDescripcion4(String descripcion4) {
		this.descripcion4 = descripcion4;
	}

	/**
	 * @return the descripcion5
	 */
	public String getDescripcion5() {
		return descripcion5;
	}

	/**
	 * @param descripcion5
	 *            the descripcion5 to set
	 */
	public void setDescripcion5(String descripcion5) {
		this.descripcion5 = descripcion5;
	}

	/**
	 * @return the descripcion6
	 */
	public String getDescripcion6() {
		return descripcion6;
	}

	/**
	 * @param descripcion6
	 *            the descripcion6 to set
	 */
	public void setDescripcion6(String descripcion6) {
		this.descripcion6 = descripcion6;
	}

	/**
	 * @return the descripcion7
	 */
	public String getDescripcion7() {
		return descripcion7;
	}

	/**
	 * @param descripcion7
	 *            the descripcion7 to set
	 */
	public void setDescripcion7(String descripcion7) {
		this.descripcion7 = descripcion7;
	}

	/**
	 * @return the descripcion8
	 */
	public String getDescripcion8() {
		return descripcion8;
	}

	/**
	 * @param descripcion8
	 *            the descripcion8 to set
	 */
	public void setDescripcion8(String descripcion8) {
		this.descripcion8 = descripcion8;
	}

	/**
	 * @return the descripcion9
	 */
	public String getDescripcion9() {
		return descripcion9;
	}

	/**
	 * @param descripcion9
	 *            the descripcion9 to set
	 */
	public void setDescripcion9(String descripcion9) {
		this.descripcion9 = descripcion9;
	}

	/**
	 * @return the descripcion10
	 */
	public String getDescripcion10() {
		return descripcion10;
	}

	/**
	 * @param descripcion10
	 *            the descripcion10 to set
	 */
	public void setDescripcion10(String descripcion10) {
		this.descripcion10 = descripcion10;
	}

	/**
	 * @return the descripcion11
	 */
	public String getDescripcion11() {
		return descripcion11;
	}

	/**
	 * @param descripcion11
	 *            the descripcion11 to set
	 */
	public void setDescripcion11(String descripcion11) {
		this.descripcion11 = descripcion11;
	}

	/**
	 * @return the descripcion12
	 */
	public String getDescripcion12() {
		return descripcion12;
	}

	/**
	 * @param descripcion12
	 *            the descripcion12 to set
	 */
	public void setDescripcion12(String descripcion12) {
		this.descripcion12 = descripcion12;
	}

	/**
	 * @return the descripcion13
	 */
	public String getDescripcion13() {
		return descripcion13;
	}

	/**
	 * @param descripcion13
	 *            the descripcion13 to set
	 */
	public void setDescripcion13(String descripcion13) {
		this.descripcion13 = descripcion13;
	}

	/**
	 * @return the descripcion14
	 */
	public String getDescripcion14() {
		return descripcion14;
	}

	/**
	 * @param descripcion14
	 *            the descripcion14 to set
	 */
	public void setDescripcion14(String descripcion14) {
		this.descripcion14 = descripcion14;
	}

	/**
	 * @return the descripcion15
	 */
	public String getDescripcion15() {
		return descripcion15;
	}

	/**
	 * @param descripcion15
	 *            the descripcion15 to set
	 */
	public void setDescripcion15(String descripcion15) {
		this.descripcion15 = descripcion15;
	}

	/**
	 * @return the descripcion16
	 */
	public String getDescripcion16() {
		return descripcion16;
	}

	/**
	 * @param descripcion16
	 *            the descripcion16 to set
	 */
	public void setDescripcion16(String descripcion16) {
		this.descripcion16 = descripcion16;
	}

	/**
	 * @return the descripcion17
	 */
	public String getDescripcion17() {
		return descripcion17;
	}

	/**
	 * @param descripcion17
	 *            the descripcion17 to set
	 */
	public void setDescripcion17(String descripcion17) {
		this.descripcion17 = descripcion17;
	}

	/**
	 * @return the descripcion18
	 */
	public String getDescripcion18() {
		return descripcion18;
	}

	/**
	 * @param descripcion18
	 *            the descripcion18 to set
	 */
	public void setDescripcion18(String descripcion18) {
		this.descripcion18 = descripcion18;
	}

	/**
	 * @return the descripcion19
	 */
	public String getDescripcion19() {
		return descripcion19;
	}

	/**
	 * @param descripcion19
	 *            the descripcion19 to set
	 */
	public void setDescripcion19(String descripcion19) {
		this.descripcion19 = descripcion19;
	}

	/**
	 * @return the descripcion20
	 */
	public String getDescripcion20() {
		return descripcion20;
	}

	/**
	 * @param descripcion20
	 *            the descripcion20 to set
	 */
	public void setDescripcion20(String descripcion20) {
		this.descripcion20 = descripcion20;
	}

	/**
	 * @return the descripcion21
	 */
	public String getDescripcion21() {
		return descripcion21;
	}

	/**
	 * @param descripcion21
	 *            the descripcion21 to set
	 */
	public void setDescripcion21(String descripcion21) {
		this.descripcion21 = descripcion21;
	}

	/**
	 * @return the descripcion22
	 */
	public String getDescripcion22() {
		return descripcion22;
	}

	/**
	 * @param descripcion22
	 *            the descripcion22 to set
	 */
	public void setDescripcion22(String descripcion22) {
		this.descripcion22 = descripcion22;
	}

	/**
	 * @return the descripcion23
	 */
	public String getDescripcion23() {
		return descripcion23;
	}

	/**
	 * @param descripcion23
	 *            the descripcion23 to set
	 */
	public void setDescripcion23(String descripcion23) {
		this.descripcion23 = descripcion23;
	}

	/**
	 * @return the descripcion24
	 */
	public String getDescripcion24() {
		return descripcion24;
	}

	/**
	 * @param descripcion24
	 *            the descripcion24 to set
	 */
	public void setDescripcion24(String descripcion24) {
		this.descripcion24 = descripcion24;
	}

	/**
	 * @return the descripcion25
	 */
	public String getDescripcion25() {
		return descripcion25;
	}

	/**
	 * @param descripcion25
	 *            the descripcion25 to set
	 */
	public void setDescripcion25(String descripcion25) {
		this.descripcion25 = descripcion25;
	}

	/**
	 * @return the numEditable
	 */
	public String getNumEditable() {
		return numEditable;
	}

	/**
	 * @param numEditable
	 *            the numEditable to set
	 */
	public void setNumEditable(String numEditable) {
		this.numEditable = numEditable;
	}

	/**
	 * @return the nombreEditable
	 */
	public String getNombreEditable() {
		return nombreEditable;
	}

	/**
	 * @param nombreEditable
	 *            the nombreEditable to set
	 */
	public void setNombreEditable(String nombreEditable) {
		this.nombreEditable = nombreEditable;
	}

	/**
	 * @return the descripcionEditable
	 */
	public String getDescripcionEditable() {
		return descripcionEditable;
	}

	/**
	 * @param descripcionEditable
	 *            the descripcionEditable to set
	 */
	public void setDescripcionEditable(String descripcionEditable) {
		this.descripcionEditable = descripcionEditable;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the listaParamsClaves
	 */
	public List<DescripcionCincoClavesVO> getListaParamsClaves() {
		return listaParamsClaves;
	}

	/**
	 * @param listaParamsClaves the listaParamsClaves to set
	 */
	public void setListaParamsClaves(
			List<DescripcionCincoClavesVO> listaParamsClaves) {
		this.listaParamsClaves = listaParamsClaves;
	}

	/**
	 * @return the listaParamsAtributos
	 */
	public List<LlaveValorVO> getListaParamsAtributos() {
		return listaParamsAtributos;
	}

	/**
	 * @param listaParamsAtributos the listaParamsAtributos to set
	 */
	public void setListaParamsAtributos(List<LlaveValorVO> listaParamsAtributos) {
		this.listaParamsAtributos = listaParamsAtributos;
	}

	/**
	 * @return the codigoClaveEditableTemporal
	 */
	public String getCodigoClaveEditableTemporal() {
		return codigoClaveEditableTemporal;
	}

	/**
	 * @param codigoClaveEditableTemporal the codigoClaveEditableTemporal to set
	 */
	public void setCodigoClaveEditableTemporal(String codigoClaveEditableTemporal) {
		this.codigoClaveEditableTemporal = codigoClaveEditableTemporal;
	}

	/**
	 * @return the numeroFila
	 */
	public String getNumeroFila() {
		return numeroFila;
	}

	/**
	 * @param numeroFila the numeroFila to set
	 */
	public void setNumeroFila(String numeroFila) {
		this.numeroFila = numeroFila;
	}

	public String getNumeroTabla() {
		return numeroTabla;
	}

	public void setNumeroTabla(String numeroTabla) {
		this.numeroTabla = numeroTabla;
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

	public void setTablaCincoClavesManagerJdbcTemplate(
			TablaCincoClavesManagerJdbcTemplate tablaCincoClavesManagerJdbcTemplate) {
		this.tablaCincoClavesManagerJdbcTemplate = tablaCincoClavesManagerJdbcTemplate;
	}

	public List<DescripcionCincoClavesVO> getListaParamsClavesOriginales() {
		return listaParamsClavesOriginales;
	}

	public void setListaParamsClavesOriginales(
			List<DescripcionCincoClavesVO> listaParamsClavesOriginales) {
		this.listaParamsClavesOriginales = listaParamsClavesOriginales;
	}

	public List<ValoresCincoClavesVO> getListaValores5ClavesParams() {
		return listaValores5ClavesParams;
	}

	public void setListaValores5ClavesParams(
			List<ValoresCincoClavesVO> listaValores5ClavesParams) {
		this.listaValores5ClavesParams = listaValores5ClavesParams;
	}

	public String getMensajeResultado() {
		return mensajeResultado;
	}

	public void setMensajeResultado(String mensajeResultado) {
		this.mensajeResultado = mensajeResultado;
	}


}
