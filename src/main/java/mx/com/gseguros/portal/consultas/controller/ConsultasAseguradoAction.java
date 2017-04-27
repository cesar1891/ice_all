package mx.com.gseguros.portal.consultas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.AseguradoDetalleVO;
import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.consultas.model.AvisoHospitalizacionVO;
import mx.com.gseguros.portal.consultas.model.CoberturaBasicaVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosComplementariosVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosGeneralesPolizaVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosTitularVO;
import mx.com.gseguros.portal.consultas.model.ConsultaPolizaActualVO;
import mx.com.gseguros.portal.consultas.model.ConsultaResultadosAseguradoVO;
import mx.com.gseguros.portal.consultas.model.ContratanteVO;
import mx.com.gseguros.portal.consultas.model.CopagoVO;
import mx.com.gseguros.portal.consultas.model.EndosoVO;
import mx.com.gseguros.portal.consultas.model.EnfermedadVO;
import mx.com.gseguros.portal.consultas.model.HistoricoFarmaciaVO;
import mx.com.gseguros.portal.consultas.model.HistoricoVO;
import mx.com.gseguros.portal.consultas.model.PeriodoVigenciaVO;
import mx.com.gseguros.portal.consultas.model.PlanVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.service.ConsultasAseguradoManager;
import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.service.MailService;
import mx.com.gseguros.portal.general.service.MailServiceForSms;
import mx.com.gseguros.portal.general.util.RolSistema;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;



/**
*
* @author MAVR
*/
@ParentPackage(value="default")
@Namespace("/consultasAsegurado")
@Controller(value="ConsultasAseguradoAction")
@Scope(value="prototype")
public class ConsultasAseguradoAction extends PrincipalCoreAction { 

	private static final long serialVersionUID = 7279218302506728952L;

	static final Logger logger = LoggerFactory.getLogger(ConsultasAseguradoAction.class);
	
	/**
     * Success property
     */
    private boolean success;
    
    private String mensajeRes;
    
    private String iCodAviso;
    
	/**
	 * Indica si el usuario es de CallCenter
	 */
	private boolean usuarioCallCenter;
    
    @Autowired
    private MailServiceForSms mailServiceForSms;
    
    @Autowired
    private MailService mailService;
	
	@Autowired
	@Qualifier("consultasAseguradoManagerImpl")
    private ConsultasAseguradoManager consultasAseguradoManager;
	
	/*
	@Autowired
	@Qualifier("consultasDAOSISAImpl")
	private IConsultasPolizaDAO consultasPolizaDAO;
	*/
	private Map<String, String> params = new HashMap<String, String>();
	
	private List<?> resultados;
	
	private List<ConsultaResultadosAseguradoVO> resultadosAsegurado;
	
	private List<ConsultaPolizaActualVO> datosPolizaActual;
	
	private ConsultaDatosComplementariosVO datosComplementarios;
	
	private ConsultaDatosGeneralesPolizaVO datosPoliza;
	
	private List<AseguradoDetalleVO> datosAseguradoDetalle;
	
	private ConsultaDatosTitularVO datosTitular;
	
	private ContratanteVO datosContratante;
	
	private List<AseguradoVO> datosAsegurados;
	
	private List<EndosoVO> datosEndosos;
	
	private List<EnfermedadVO> datosEnfermedades;
	
	private PlanVO datosPlan;
	
	private List<CopagoVO> datosCopagosPoliza;
	
	private List<CoberturaBasicaVO> datosCoberturasPoliza;
	
	private List<CoberturaBasicaVO> datosCoberturasBasicas;
	
	private List<HistoricoVO> datosHistorico;
	
	private List<HistoricoFarmaciaVO> historicoFarmacia;
	
	private List<PeriodoVigenciaVO> periodosVigencia;
	
	private List<BaseVO> datosHospitales;
	
	private List<AvisoHospitalizacionVO> datosAvisosAnteriores;
	
	/**
	 * Metodo de entrada a consulta de polizas
	 */
	public String pantallaConsultasAsegurado() throws Exception {

		// Obtenemos el rol de sistema del usuario en sesion:
		UserVO usuario = (UserVO) session.get("USUARIO");
		String cdRolSistema = usuario.getRolActivo().getClave();
		// Si es Call Center, tendra permiso de ver Aviso Hospitalizacion:
		if (cdRolSistema.equals(RolSistema.CALLCENTER.getCdsisrol())) {
			usuarioCallCenter = true;
		}
		return SUCCESS;
	}
	
	/**
     * Obtiene las coincidencias de asegurados
     * @return String result
     */
    public String consultaResultadosAsegurado(){
    	logger.debug(" **** Entrando a consultaResultadosAsegurado ****");
    	try {
    		
    		//logger.debug("Nueva implementacion...");
    		resultadosAsegurado = consultasAseguradoManager.obtieneResultadosAsegurado(params.get("rfc"), params.get("cdperson"), params.get("nombre"));
    		
    		if(resultadosAsegurado != null) {
    			logger.debug("Coincidencias por asegurado encontradas: {}", resultadosAsegurado.size());
    		}
    		
    	}catch( Exception e){
    		logger.error("Error al obtener consultaResultadosAsegurado ",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
    
    /**
     * Obtiene los datos de la poliza actual del asegurado
     * @return String result
     */
    public String consultaPolizaActual(){
    	logger.debug(" **** Entrando a consultaPolizaActual ****");
    	mensajeRes = "";
    	try {    		    		
    		PolizaAseguradoVO poliza = new PolizaAseguradoVO();
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		poliza.setNmpoliex(params.get("nmpoliex"));
    		datosPolizaActual = consultasAseguradoManager.obtienePolizaActual(poliza);
    		
    		if(datosPolizaActual != null) {
    			logger.debug("Poliza actual encontrada: {}", datosPolizaActual.size());
    		}
    		
    		
    	}catch( Exception e){
    		success = false;
    		logger.error("Error al obtener los consultaPolizaActual ",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
	
    /**
     * Obtiene los datos complementarios de la Seccion Datos Generales
     * @return String result
     */
    public String consultaDatosComplementarios(){
    	logger.debug(" **** Entrando a Consulta de Datos Complementarios ****");
        try {
			
        	PolizaVO poliza = new PolizaVO();    		
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		poliza.setCdunieco(params.get("cdunieco"));
    		poliza.setCdramo(params.get("cdramo"));
    		poliza.setEstado(params.get("estado"));
    		poliza.setNmpoliza(params.get("nmpoliza"));
    		poliza.setNmsuplem(params.get("suplemento"));
    		poliza.setNmsituac(params.get("nmsituac"));
    		AseguradoVO asegurado = new AseguradoVO();
    		asegurado.setCdperson(params.get("cdperson"));
    					
			List<ConsultaDatosComplementariosVO> listaAux = consultasAseguradoManager.obtieneDatosComplementarios(poliza, asegurado);
			
			if(listaAux!=null && !listaAux.isEmpty())	datosComplementarios = listaAux.get(0);
			
        	
        	logger.debug("Resultado de la consulta de datos complementarios: {}" , datosComplementarios);
        	
        }catch( Exception e){
            logger.error("Error al obtener los datos complementarios ",e);
            return SUCCESS;
        }
        
        success = true;
        return SUCCESS;
    }
	
    /**
     * Obtiene los datos generales de una p\u00f3liza
     * @return String result
     */
    public String consultaDatosPoliza(){
    	logger.debug(" **** Entrando a Consulta de Poliza ****");
        try {			        	
        	PolizaAseguradoVO polizaAseguradoVO = new PolizaAseguradoVO();
        	polizaAseguradoVO.setCdunieco(params.get("cdunieco"));
        	polizaAseguradoVO.setCdramo(params.get("cdramo"));
        	polizaAseguradoVO.setEstado(params.get("estado"));
        	polizaAseguradoVO.setNmpoliza(params.get("nmpoliza"));
        	polizaAseguradoVO.setIcodpoliza(params.get("icodpoliza"));
        	
        	List<ConsultaDatosGeneralesPolizaVO> lista = consultasAseguradoManager.obtieneDatosPoliza(polizaAseguradoVO);
        	
        	if(lista!=null && !lista.isEmpty())	datosPoliza = lista.get(0);
        	
        	logger.debug("Resultado de la consulta de poliza: {}" , datosPoliza);
        	
        }catch( Exception e){
            logger.error("Error al obtener los datos de la poliza ",e);
            return SUCCESS;
        }
        
        success = true;
        return SUCCESS;
    }
    
    /**
     * Obtiene la informacion del asegurado
     * @return String result
     */
    public String consultaAseguradoDetalle(){
		logger.debug(" *** Entrando a consultaAseguradoDetalle ****");
		try{
			PolizaVO poliza = new PolizaVO();
        	poliza.setCdunieco(params.get("cdunieco"));
        	poliza.setCdramo(params.get("cdramo"));
        	poliza.setEstado(params.get("estado"));
        	poliza.setNmpoliza(params.get("nmpoliza"));
        	poliza.setNmsuplem(params.get("suplemento"));        	
        	poliza.setIcodpoliza(params.get("icodpoliza"));
			AseguradoVO asegurado = new AseguradoVO();
			asegurado.setCdperson(params.get("cdperson"));
			
			datosAseguradoDetalle = consultasAseguradoManager.obtieneAseguradoDetalle(poliza, asegurado);
			
			logger.debug("Resultado de consultaAseguradoDetalle: {}" , datosAseguradoDetalle);
			
		}catch(Exception e){
			logger.error("Error al obtener el detalle del asegurado.",e);
			return SUCCESS;
		}
		
		success = true;
		return SUCCESS;
	}
    
    /**
     * Obtiene los datos del titular
     * @return String result
     */
    public String consultaDatosTitular(){
    	logger.debug(" **** Entrando a Consulta de Titular ****");
        try {
			        	
        	PolizaVO polizaVO = new PolizaVO();
        	polizaVO.setCdunieco(params.get("cdunieco"));
        	polizaVO.setCdramo(params.get("cdramo"));
        	polizaVO.setEstado(params.get("estado"));
        	polizaVO.setNmpoliza(params.get("nmpoliza"));
        	polizaVO.setIcodpoliza(params.get("icodpoliza"));
        	polizaVO.setNmsituac(params.get("nmsituac"));
        	AseguradoVO aseguradoVO = new AseguradoVO();
    		aseguradoVO.setCdperson(params.get("cdperson"));
        	
        	List<ConsultaDatosTitularVO> lista = consultasAseguradoManager.obtieneDatosTitular(polizaVO, aseguradoVO);
        	
        	if(lista!=null && !lista.isEmpty())	datosTitular = lista.get(0);
        	
        	logger.debug("Resultado de la consulta de titular: {}" , datosTitular);
        	
        }catch( Exception e){
            logger.error("Error al obtener los datos del titular ",e);
            return SUCCESS;
        }
        
        success = true;
        return SUCCESS;
    }
    
    /**
     * Obtiene los datos del contratante
     * @return String result
     */
    public String consultaDatosContratante(){
    	logger.debug(" **** Entrando a Consulta de Contratante ****");
        try {
			        	
        	PolizaVO polizaVO = new PolizaVO();
        	polizaVO.setCdunieco(params.get("cdunieco"));
        	polizaVO.setCdramo(params.get("cdramo"));
        	polizaVO.setEstado(params.get("estado"));
        	polizaVO.setNmpoliza(params.get("nmpoliza"));        	
        	polizaVO.setIcodpoliza(params.get("icodpoliza"));
        	
        	List<ContratanteVO> lista = consultasAseguradoManager.obtieneDatosContratante(polizaVO);
        	
        	if(lista!=null && !lista.isEmpty())	datosContratante = lista.get(0);
        	
        	logger.debug("Resultado de la consulta de contratante: {}" , datosContratante);
        	
        }catch( Exception e){
            logger.error("Error al obtener los datos del contratante ",e);
            return SUCCESS;
        }
        
        success = true;
        return SUCCESS;
    }
    
    /**
     * Obtiene los datos del asegurado
     * @return String result
     */
    public String consultaDatosAsegurado(){
    	logger.debug(" **** Entrando a consultaDatosAsegurado ****");
    	try {
    		    		
    		PolizaVO poliza = new PolizaVO();
    		poliza.setCdunieco(params.get("cdunieco"));
    		poliza.setCdramo(params.get("cdramo"));
    		poliza.setEstado(params.get("estado"));
    		poliza.setNmpoliza(params.get("nmpoliza"));
    		poliza.setNmsuplem(params.get("suplemento"));
    		poliza.setNmsituac(params.get("nmsituac"));
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		AseguradoVO asegurado = new AseguradoVO();
    		asegurado.setCdperson(params.get("cdperson"));
    		datosAsegurados = consultasAseguradoManager.obtieneAsegurados(poliza,asegurado);
    		
    		logger.debug("Resultado de la consultaDatosAsegurado: {}" , datosAsegurados);
    		
    	}catch( Exception e){
    		logger.error("Error al obtener los datos del Asegurado ",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
    
    /**
     * Obtiene los endosos de una poliza
     * @return String result
     */    
    public String consultaDatosEndosos(){
    	logger.debug(" **** Entrando a consultaDatosEndosos ****");
    	PolizaVO poliza = new PolizaVO();
    	poliza.setCdunieco(params.get("cdunieco"));
    	poliza.setCdramo(params.get("cdramo"));
    	poliza.setEstado(params.get("estado"));
    	poliza.setNmpoliza(params.get("nmpoliza"));
    	poliza.setNmsuplem(params.get("suplemento"));
    	poliza.setNmsituac(params.get("nmsituac"));
    	poliza.setIcodpoliza(params.get("icodpoliza"));
		AseguradoVO asegurado = new AseguradoVO();
		asegurado.setCdperson(params.get("cdperson"));
    	try {    		
    		datosEndosos = consultasAseguradoManager.obtieneEndososPoliza(poliza, asegurado);
    		logger.debug("Resultados de la consulta de endosos: {}" , datosEndosos.size());
    		success = true;
    	} catch(Exception e){
    		logger.error("Error al obtener las clausulas de la poliza",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    /**
     * Obtiene las enfermedades cronicas
     * @return String result
     */    
    public String consultaDatosEnfermedades(){
    	logger.debug(" **** Entrando a consultaDatosEnfermedades ****");
    	PolizaVO poliza = new PolizaVO();
    	poliza.setCdunieco(params.get("cdunieco"));
    	poliza.setCdramo(params.get("cdramo"));
    	poliza.setEstado(params.get("estado"));
    	poliza.setNmpoliza(params.get("nmpoliza"));
    	poliza.setNmsuplem(params.get("suplemento"));
    	poliza.setNmsituac(params.get("nmsituac"));
    	poliza.setIcodpoliza(params.get("icodpoliza"));
		AseguradoVO asegurado = new AseguradoVO();
		asegurado.setCdperson(params.get("cdperson"));
    	try {    		
    		logger.debug("El asegurado a buscar es: {}" , params.get("cdperson"));
    		datosEnfermedades = consultasAseguradoManager.obtieneEnfermedades(poliza, asegurado);
    		success = true;
    	} catch(Exception e){
    		logger.error("Error al obtener las enfermedades cronicas",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    /**
     * Obtiene los datos del plan
     * @return String result
     */
    public String consultaDatosPlan(){
    	logger.debug(" **** Entrando a Consulta de Plan ****");
        try {
			        	
        	PolizaVO polizaVO = new PolizaVO();
        	polizaVO.setCdunieco(params.get("cdunieco"));
        	polizaVO.setCdramo(params.get("cdramo"));
        	polizaVO.setEstado(params.get("estado"));
        	polizaVO.setNmpoliza(params.get("nmpoliza"));
        	polizaVO.setIcodpoliza(params.get("icodpoliza"));
        	
        	List<PlanVO> lista = consultasAseguradoManager.obtieneDatosPlan(polizaVO);
        	
        	if(lista!=null && !lista.isEmpty())	datosPlan = lista.get(0);
        	
        	logger.debug("Resultado de la consulta de plan: {}" , datosPlan);
        	
        }catch( Exception e){
            logger.error("Error al obtener los datos del plan ",e);
            return SUCCESS;
        }
        
        success = true;
        return SUCCESS;
    }
    
    /**
     * Obtiene los copagos de una poliza
     * @return String result
     */
    public String consultaCopagosPoliza(){
    	logger.debug(" **** Entrando a consultaCopagosPoliza ****");
    	try {
    		    		
    		PolizaVO poliza = new PolizaVO();
    		poliza.setCdunieco(params.get("cdunieco"));
    		poliza.setCdramo(params.get("cdramo"));
    		poliza.setEstado(params.get("estado"));
    		poliza.setNmpoliza(params.get("nmpoliza"));
    		poliza.setNmsuplem(params.get("suplemento"));
    		poliza.setNmsituac(params.get("nmsituac"));
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		
    		datosCopagosPoliza = consultasAseguradoManager.obtieneCopagosPoliza(poliza);
    		   		
    		
    		logger.debug("Resultado de consultaCopagosPoliza: {}" , datosCopagosPoliza);
    		
    	}catch( Exception e){
    		logger.error("Error al obtener los copagos de la poliza ",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
    
    /**
     * Obtiene las coberturas
     * @return String result
     */
    public String consultaCoberturasPoliza(){
    	logger.debug(" **** Entrando a consultaCoberturasPoliza ****");
    	try {
    		    		
    		PolizaVO poliza = new PolizaVO();
    		poliza.setCdunieco(params.get("cdunieco"));
    		poliza.setCdramo(params.get("cdramo"));
    		poliza.setEstado(params.get("estado"));
    		poliza.setNmpoliza(params.get("nmpoliza"));    		
    		poliza.setNmsituac(params.get("nmsituac"));
    		poliza.setNmsuplem(params.get("suplemento"));
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		datosCoberturasPoliza = consultasAseguradoManager.obtieneCoberturasPoliza(poliza);
    		   		
    		
    		logger.debug("Resultado de consultaCoberturas: {}" , datosCoberturasPoliza);
    		
    	}catch( Exception e){
    		logger.error("Error al obtener las coberturas",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
    
    /**
     * Obtiene las coberturas basicas
     * @return String result
     */
    public String consultaCoberturasBasicas(){
    	logger.debug(" **** Entrando a consultaConsultaCoberturasBasicas ****");
    	try {
    		    		
    		PolizaVO poliza = new PolizaVO();
    		poliza.setCdunieco(params.get("cdunieco"));
    		poliza.setCdramo(params.get("cdramo"));
    		poliza.setEstado(params.get("estado"));
    		poliza.setNmpoliza(params.get("nmpoliza"));
    		poliza.setNmsuplem(params.get("suplemento"));
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		
    		datosCoberturasBasicas = consultasAseguradoManager.obtieneCoberturasBasicas(poliza);
    		   		
    		
    		logger.debug("Resultado de consultaCoberturasBasicas: {}" , datosCoberturasBasicas);
    		
    	}catch( Exception e){
    		logger.error("Error al obtener las coberturas basicas ",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
    
    /**
     * Obtiene los datos del historico de una p�liza
     * @return String result
     */
    public String consultaDatosHistorico(){
    	logger.debug(" **** Entrando a consultaDatosHistorico ****");
    	mensajeRes = "";
    	try {
    		    		
    		PolizaAseguradoVO poliza = new PolizaAseguradoVO();
    		poliza.setIcodpoliza(params.get("icodpoliza"));
    		poliza.setNmpoliex(params.get("nmpoliex"));
    		poliza.setCdramo(params.get("cdramo"));
    		AseguradoVO asegurado = new AseguradoVO();
    		asegurado.setCdperson(params.get("cdperson"));
    		datosHistorico = consultasAseguradoManager.obtieneHistoricoAsegurado(poliza,asegurado);
    		
    		logger.debug("Resultado de la consultaDatosHistorico: {}" , datosHistorico);
    		    		   		
    	}catch( Exception e){
    		success = false;
    		logger.error("Error al obtener los consultaDatosHistorico ",e);
    		return SUCCESS;
    	}
    	
    	success = true;
    	return SUCCESS;
    	
    }
    
    /**
     * Obtiene los datos del avisos anteriores
     * @return String result
     */
    public String consultaAvisosAnteriores(){
    	logger.debug(" **** Entrando a consultaAvisosAnteriores ****");
    	PolizaVO poliza = new PolizaVO();
    	poliza.setIcodpoliza(params.get("icodpoliza"));
    	AseguradoVO asegurado = new AseguradoVO();
    	asegurado.setCdperson(params.get("cdperson"));
    	try{    		
    		datosAvisosAnteriores = consultasAseguradoManager.obtieneAvisosAnteriores(poliza, asegurado);
    		logger.debug("Resultado de la consultaAvisosAnteriores: {}" , datosAvisosAnteriores);
    	}catch(Exception e){
    		logger.error("Error al obtener avisos anteriores.",e);
    		return 	SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    /**
     * Obtiene el historico de farmacia
     * @return String result
     */	
    public String consultaDatosHistoricoFarmacia(){
    	logger.debug(" **** Entrando a consultaDatosHistoricoFarmacia ****");
    	PolizaVO poliza = new PolizaVO();
    	poliza.setCdunieco(params.get("cdunieco"));
    	poliza.setCdramo(params.get("cdramo"));
    	poliza.setEstado(params.get("estado"));
    	poliza.setNmpoliza(params.get("nmpoliza"));
    	poliza.setNmsuplem(params.get("suplemento"));
    	poliza.setNmsituac(params.get("nmsituac"));
    	poliza.setIcodpoliza(params.get("icodpoliza"));
		AseguradoVO asegurado = new AseguradoVO();
		asegurado.setCdperson(params.get("cdperson"));
    	try {
    		historicoFarmacia = consultasAseguradoManager.obtieneHistoricoFarmacia(poliza, asegurado);
    		success = true;
    	} catch(Exception e){
    		logger.error("Error al obtener el historico de farmacia",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    /**
     * Obtiene periodos de vigencia
     * @return String result
     */	
    public String consultaPeriodosVigencia(){
    	logger.debug(" **** Entrando a consultaPeriodosVigencia ****");
    	PolizaVO poliza = new PolizaVO();
    	poliza.setCdunieco(params.get("cdunieco"));
    	poliza.setCdramo(params.get("cdramo"));
    	poliza.setEstado(params.get("estado"));
    	poliza.setNmpoliza(params.get("nmpoliza"));
    	poliza.setNmsuplem(params.get("suplemento"));
    	poliza.setNmsituac(params.get("nmsituac"));
    	poliza.setIcodpoliza(params.get("icodpoliza"));
		AseguradoVO asegurado = new AseguradoVO();
		asegurado.setCdperson(params.get("cdperson"));
    	try {
    		
    		periodosVigencia = consultasAseguradoManager.obtienePeriodosVigencia(poliza, asegurado);
    		success = true;
    	} catch(Exception e){
    		logger.error("Error al obtener periodos de vigencia",e);
    		return SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    public String consultaHospitales(){
    	logger.debug("***Entrando a consultaHospitales***");
    	PolizaVO poliza = new PolizaVO();
    	String filtro = params.get("hospital");
    	poliza.setIcodpoliza(params.get("icodpoliza"));
    	try{
    		datosHospitales = consultasAseguradoManager.obtieneHospitales(filtro,poliza);
    	}catch(Exception e){
    		logger.error("Error al obtener hospitales",e);
    		return 	SUCCESS;
    	}
    	success = true;
    	return SUCCESS;
    }
    
    public String enviarAvisoHospitalizacion() {
    	logger.debug("***Entrando a enviarAvisoHospitalizacion***");
    	AvisoHospitalizacionVO aviso = new AvisoHospitalizacionVO();
    	UserVO usuario = (UserVO)session.get("USUARIO");
    	String telefonoAgente="";
    	String mensajeAgente="";
    	boolean smsSend, mailSend;
		String [] mailSms = { "smsgs@gseguros.com.mx" };
		String [] mails = { "ahernandezc@gsalud.com.mx","nlromeroc@gsalud.com.mx","jvargas@gsalud.com.mx" };
		String [] adjuntos = new String[0];
		mensajeAgente = "EL AFILIADO(" + params.get("cdperson") + "), " + params.get("nombre") +
                ", DE LA POLIZA DE GSALUD: (" + params.get("cdunieco") + "/ ";
				if(StringUtils.isNotBlank(params.get("icodpoliza"))){
					mensajeAgente+=(params.get("cdramo"));
				}else{
					mensajeAgente+=(params.get("cdsubram"));
				}
				mensajeAgente+="/ " + params.get("nmpoliza") + "), SE ENCUENTRA HOSPITALIZADO.";
    	PolizaVO poliza = new PolizaVO();
    	poliza.setIcodpoliza(params.get("icodpoliza"));
    	aviso.setCdperson(params.get("cdperson"));
    	aviso.setNmpoliza(params.get("nmpoliza"));
    	aviso.setCdagente(params.get("cdagente"));
    	aviso.setCdpresta(params.get("hospital"));
    	aviso.setFeingreso(params.get("feingreso"));
    	aviso.setCdusuari(usuario.getUser());
    	aviso.setComentario(params.get("comentario"));
    	try{
    		iCodAviso = consultasAseguradoManager.enviarAvisoHospitalizacion(aviso, poliza);
			telefonoAgente = consultasAseguradoManager.consultaTelefonoAgente(params.get("cdagente"));
			logger.debug("El telefono del agente {} es: {}", params.get("cdagente"), telefonoAgente);
			if(StringUtils.isBlank(telefonoAgente)) {
				throw new ApplicationException("3");
			}
			smsSend = mailServiceForSms.enviaCorreo(mailSms, null, null, telefonoAgente, mensajeAgente, adjuntos, false);
			mailSend = mailService.enviaCorreo(mails, null, null, "Aviso de Hospitalizacion", mensajeAgente, adjuntos, false);
    		if(!smsSend && !mailSend)
    		{
    			throw new ApplicationException("4");
    		}else{
    			consultasAseguradoManager.actualizaEstatusEnvio(iCodAviso, poliza);
			}
    		mensajeRes = "Se registr\u00f3 y envi\u00f3 Aviso de Hospitalizaci\u00f3n.";
    		success = true;
    		
    	}catch(Exception e){
    		mensajeRes = e.getMessage();
    		logger.error(e.getMessage(),e);
    	}
    	logger.debug("***Saliendo de enviarAvisoHospitalizacion***");
    	return SUCCESS;
    }
    
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public void setParam(Map<String, String> params) {
		this.params = params;
	}
	public String getParamsJson() {
		try {
			return JSONUtil.serialize(params);
		} catch (Exception e) {
			logger.error("Error al generar JSON de params",e);
			return null;
		}
	}

	public List<?> getResultados() {
		return resultados;
	}

	public void setResultados(List<?> resultados) {
		this.resultados = resultados;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMensajeRes() {
		return mensajeRes;
	}

	public void setMensajeRes(String mensajeRes) {
		this.mensajeRes = mensajeRes;
	}

	public String getiCodAviso() {
		return iCodAviso;
	}

	public void setiCodAviso(String iCodAviso) {
		this.iCodAviso = iCodAviso;
	}

	public boolean isUsuarioCallCenter() {
		return usuarioCallCenter;
	}

	public void setUsuarioCallCenter(boolean usuarioCallCenter) {
		this.usuarioCallCenter = usuarioCallCenter;
	}

	public List<ConsultaResultadosAseguradoVO> getResultadosAsegurado() {
		return resultadosAsegurado;
	}

	public void setResultadosAsegurado(
			List<ConsultaResultadosAseguradoVO> resultadosAsegurado) {
		this.resultadosAsegurado = resultadosAsegurado;
	}

	public List<ConsultaPolizaActualVO> getDatosPolizaActual() {
		return datosPolizaActual;
	}

	public void setDatosPolizaActual(List<ConsultaPolizaActualVO> datosPolizaActual) {
		this.datosPolizaActual = datosPolizaActual;
	}

	public ConsultaDatosComplementariosVO getDatosComplementarios() {
		return datosComplementarios;
	}

	public void setDatosComplementarios(
			ConsultaDatosComplementariosVO datosComplementarios) {
		this.datosComplementarios = datosComplementarios;
	}

	public ConsultaDatosGeneralesPolizaVO getDatosPoliza() {
		return datosPoliza;
	}

	public void setDatosPoliza(ConsultaDatosGeneralesPolizaVO datosPoliza) {
		this.datosPoliza = datosPoliza;
	}

	public ContratanteVO getDatosContratante() {
		return datosContratante;
	}

	public void setDatosContratante(ContratanteVO datosContratante) {
		this.datosContratante = datosContratante;
	}

	public List<AseguradoVO> getDatosAsegurados() {
		return datosAsegurados;
	}

	public void setDatosAsegurados(List<AseguradoVO> datosAsegurados) {
		this.datosAsegurados = datosAsegurados;
	}

	public PlanVO getDatosPlan() {
		return datosPlan;
	}

	public void setDatosPlan(PlanVO datosPlan) {
		this.datosPlan = datosPlan;
	}

	public List<CopagoVO> getDatosCopagosPoliza() {
		return datosCopagosPoliza;
	}

	public void setDatosCopagosPoliza(List<CopagoVO> datosCopagosPoliza) {
		this.datosCopagosPoliza = datosCopagosPoliza;
	}

	public List<CoberturaBasicaVO> getDatosCoberturasPoliza() {
		return datosCoberturasPoliza;
	}

	public void setDatosCoberturasPoliza(
			List<CoberturaBasicaVO> datosCoberturasPoliza) {
		this.datosCoberturasPoliza = datosCoberturasPoliza;
	}

	public List<CoberturaBasicaVO> getDatosCoberturasBasicas() {
		return datosCoberturasBasicas;
	}

	public void setDatosCoberturasBasicas(
			List<CoberturaBasicaVO> datosCoberturasBasicas) {
		this.datosCoberturasBasicas = datosCoberturasBasicas;
	}

	public List<HistoricoVO> getDatosHistorico() {
		return datosHistorico;
	}

	public void setDatosHistorico(List<HistoricoVO> datosHistorico) {
		this.datosHistorico = datosHistorico;
	}

	public List<HistoricoFarmaciaVO> getHistoricoFarmacia() {
		return historicoFarmacia;
	}

	public void setHistoricoFarmacia(List<HistoricoFarmaciaVO> historicoFarmacia) {
		this.historicoFarmacia = historicoFarmacia;
	}

	public List<PeriodoVigenciaVO> getPeriodosVigencia() {
		return periodosVigencia;
	}

	public void setPeriodosVigencia(
			List<PeriodoVigenciaVO> periodosVigencia) {
		this.periodosVigencia = periodosVigencia;
	}

	public List<BaseVO> getDatosHospitales() {
		return datosHospitales;
	}

	public void setDatosHospitales(List<BaseVO> datosHospitales) {
		this.datosHospitales = datosHospitales;
	}

	public List<AvisoHospitalizacionVO> getDatosAvisosAnteriores() {
		return datosAvisosAnteriores;
	}

	public void setDatosAvisosAnteriores(List<AvisoHospitalizacionVO> datosAvisosAnteriores) {
		this.datosAvisosAnteriores = datosAvisosAnteriores;
	}

	public List<AseguradoDetalleVO> getDatosAseguradoDetalle() {
		return datosAseguradoDetalle;
	}

	public void setDatosAseguradoDetalle(
			List<AseguradoDetalleVO> datosAseguradoDetalle) {
		this.datosAseguradoDetalle = datosAseguradoDetalle;
	}

	public List<EndosoVO> getDatosEndosos() {
		return datosEndosos;
	}

	public void setDatosEndosos(List<EndosoVO> datosEndosos) {
		this.datosEndosos = datosEndosos;
	}
	
	public List<EnfermedadVO> getDatosEnfermedades() {
		return datosEnfermedades;
	}

	public void setDatosEnfermedades(List<EnfermedadVO> datosEnfermedades) {
		this.datosEnfermedades = datosEnfermedades;
	}

	public ConsultaDatosTitularVO getDatosTitular() {
		return datosTitular;
	}

	public void setDatosTitular(ConsultaDatosTitularVO datosTitular) {
		this.datosTitular = datosTitular;
	}
	
	
}
