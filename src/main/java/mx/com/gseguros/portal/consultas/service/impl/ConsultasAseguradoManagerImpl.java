package mx.com.gseguros.portal.consultas.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.dao.IConsultasAseguradoDAO;
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
import mx.com.gseguros.portal.general.model.SolicitudCxPVO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("consultasAseguradoManagerImpl")
public class ConsultasAseguradoManagerImpl implements ConsultasAseguradoManager {

	static final Logger logger = LoggerFactory.getLogger(ConsultasAseguradoManagerImpl.class);
	
	@Autowired
	@Qualifier("consultasAseguradoDAOICEImpl")
	private IConsultasAseguradoDAO consultasAseguradoDAOICE;
	
	@Autowired
	@Qualifier("consultasAseguradoDAOSISAImpl")
	private IConsultasAseguradoDAO consultasAseguradoDAOSISA;
	
	@Autowired
	@Qualifier("consultasAseguradoDAOSIGSImpl")
	private IConsultasAseguradoDAO consultasAseguradoDAOSIGS;
		
	@Override
	public List<ConsultaResultadosAseguradoVO> obtieneResultadosAsegurado(String rfc,
			String cdperson, String nombre) throws Exception {
		List<ConsultaResultadosAseguradoVO> aseguradosSISA;
		aseguradosSISA = new ArrayList<ConsultaResultadosAseguradoVO>();
		aseguradosSISA = consultasAseguradoDAOSISA.obtieneResultadosAsegurado(rfc, cdperson, nombre);
		
		List<ConsultaResultadosAseguradoVO> aseguradosICE;
		aseguradosICE = new ArrayList<ConsultaResultadosAseguradoVO>();
		aseguradosICE = consultasAseguradoDAOICE.obtieneResultadosAsegurado(rfc, cdperson, nombre);
		
		//Fusionamos los asegurados
		List<ConsultaResultadosAseguradoVO> aseguradosFusionados;
		aseguradosFusionados = new ArrayList<ConsultaResultadosAseguradoVO>();
		
		if(aseguradosICE != null){
			if(aseguradosICE.size() > 0){				
				aseguradosFusionados.addAll(aseguradosICE);
			}
		}
		
		if(aseguradosSISA != null){
			if(aseguradosSISA.size() > 0){
				aseguradosFusionados.addAll(aseguradosSISA);
			}
		}
		
		return aseguradosFusionados;
	}
	
	@Override
	public List<ConsultaPolizaActualVO> obtienePolizaActual(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		
		List<ConsultaPolizaActualVO> polizaActual;
		polizaActual = new ArrayList<ConsultaPolizaActualVO>();
		
		// Si iCodPoliza viene vacio, es informaci&oacute;n de ICE, sino es de SISA:
		if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza())){
			polizaActual = consultasAseguradoDAOICE.obtienePolizaActual(polizaAsegurado);
		} else {
			polizaActual = consultasAseguradoDAOSISA.obtienePolizaActual(polizaAsegurado);
		}
				
		return polizaActual;
	}
	
	@Override
	public List<ConsultaDatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<ConsultaDatosComplementariosVO> datosComplementarios = new ArrayList<ConsultaDatosComplementariosVO>();
		
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 datosComplementarios = consultasAseguradoDAOSISA.obtieneDatosComplementarios(poliza, asegurado);
		 } else {
			 datosComplementarios = consultasAseguradoDAOICE.obtieneDatosComplementarios(poliza, asegurado);
		 }
		 return datosComplementarios;
		
	}
	
	@Override
	public List<ConsultaDatosGeneralesPolizaVO> obtieneDatosPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		 List<ConsultaDatosGeneralesPolizaVO> datosPolizas = new ArrayList<ConsultaDatosGeneralesPolizaVO>();
		 
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza()) == false){		
			 datosPolizas = consultasAseguradoDAOSISA.obtieneDatosPoliza(polizaAsegurado);
		 } else {
			 datosPolizas = consultasAseguradoDAOICE.obtieneDatosPoliza(polizaAsegurado);
		 }
				 
		 return datosPolizas;
	}
	
	@Override
	public List<AseguradoDetalleVO> obtieneAseguradoDetalle(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<AseguradoDetalleVO> datosAseguradoDetalle = new ArrayList<AseguradoDetalleVO>();
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){
			datosAseguradoDetalle = consultasAseguradoDAOSISA.obtieneAseguradoDetalle(poliza,asegurado);
		} else {
			datosAseguradoDetalle = consultasAseguradoDAOICE.obtieneAseguradoDetalle(poliza,asegurado);
		}
		return datosAseguradoDetalle;
	}
	
	@Override
	public List<ConsultaDatosTitularVO> obtieneDatosTitular(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		List<ConsultaDatosTitularVO> datosTitular = new ArrayList<ConsultaDatosTitularVO>();
		
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 datosTitular = consultasAseguradoDAOSISA.obtieneDatosTitular(poliza, asegurado);
		 } else {
			 datosTitular = consultasAseguradoDAOICE.obtieneDatosTitular(poliza, asegurado);
		 }
		 return datosTitular;
		
	}
		
	@Override
	public List<ContratanteVO> obtieneDatosContratante(PolizaVO poliza) throws Exception {
		List<ContratanteVO> datosContratante = new ArrayList<ContratanteVO>();
		
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 datosContratante = consultasAseguradoDAOSISA.obtieneDatosContratante(poliza);
		 } else {
			 datosContratante = consultasAseguradoDAOICE.obtieneDatosContratante(poliza);
		 }
		 return datosContratante;
		
	}
	
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		
		List<AseguradoVO> asegurados = new ArrayList<AseguradoVO>();
		
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 asegurados = consultasAseguradoDAOSISA.obtieneAsegurados(poliza, asegurado);
		 } else {
			 asegurados = consultasAseguradoDAOICE.obtieneAsegurados(poliza, asegurado);
		 }
		
		return asegurados;
	}
	
	@Override
	public List<EndosoVO> obtieneEndososPoliza(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<EndosoVO> endosos = new ArrayList<EndosoVO>();
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 endosos = consultasAseguradoDAOSISA.obtieneEndososPoliza(poliza, asegurado);
		 } else {
			 endosos = consultasAseguradoDAOICE.obtieneEndososPoliza(poliza, asegurado);
		 }
		return endosos;
	}
	
	@Override
	public List<EnfermedadVO> obtieneEnfermedades(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<EnfermedadVO> enfermedades = new ArrayList<EnfermedadVO>();
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 enfermedades = consultasAseguradoDAOSISA.obtieneEnfermedades(poliza, asegurado);
		 } 
		 /*Por el momento no se registran las enfermedades cr&oacute;nicas en ICE.
		 else {
			 enfermedades = consultasAseguradoDAOICE.obtieneEnfermedades(poliza, asegurado);
		 }*/
		return enfermedades;
	}

	@Override
	public List<PlanVO> obtieneDatosPlan(PolizaVO poliza) throws Exception {
		List<PlanVO> datosPlan = new ArrayList<PlanVO>();
		
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			 datosPlan = consultasAseguradoDAOSISA.obtieneDatosPlan(poliza);
		 } else {
			 datosPlan = consultasAseguradoDAOICE.obtieneDatosPlan(poliza);
		 }
		 return datosPlan;
		
	}
	
	@Override
	public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza) throws Exception {
		
		List<CopagoVO> copagos;
		// Si iCodPoliza viene vacio, es informaci&oacute;n de ICE:
		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			
			copagos = consultasAseguradoDAOICE.obtieneCopagosPoliza(poliza);
			
			/**
			 * Se comenta agruopador de copagos
			 
			// Agregamos un campo que agrupe los resultados:
			String agrupador = null;
			Iterator<CopagoVO> itCopagos = copagos.iterator();
		
			int ordenOrig = 0;
			String[] arrayLetras = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","x","y","z"};
			
			Map<String, Integer> agrupadores = new HashMap<String, Integer>();
			
			
			while (itCopagos.hasNext()) {
				CopagoVO copagoVO = itCopagos.next();
				// Si el copago tiene Nivel Padre se asigna como agrupador:
				if(copagoVO.getNivel() == 1) {
					agrupador = copagoVO.getDescripcion();
					agrupadores.put(agrupador, 0);
					ordenOrig++;
				} else {
					if(agrupador != null && agrupadores.get(agrupador) != null) {
						agrupadores.put(agrupador, agrupadores.get(agrupador)+1);
						logger.debug("agrupadores:" + agrupadores);
					}
				}
				/*
				// Si el copago no es visible o no hay descripcion, lo eliminamos:
				if(!copagoVO.isVisible() || StringUtils.isBlank(copagoVO.getDescripcion())) {
					itCopagos.remove();
				}
				
				copagoVO.setAgrupador(agrupador);
				copagoVO.setOrdenAgrupador(arrayLetras[ordenOrig-1]+" "+agrupador);
			}
			
			// Se eliminan los agrupadores que no tienen hijos:
			for(Iterator<Map.Entry<String, Integer>> it = agrupadores.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Integer> entry = it.next();
				if(entry.getValue() == 0 ) {
					it.remove();
				}
			}
			
			logger.debug("map final:"+ agrupadores);
			
			itCopagos = copagos.iterator();
			while (itCopagos.hasNext()) {
				CopagoVO copagoVO = itCopagos.next();
				for (String keyAgrupa : agrupadores.keySet()) {
					if(keyAgrupa.equals(copagoVO.getDescripcion())) {
						logger.debug("se elimina " + copagoVO);
						itCopagos.remove();
						continue;
					}				
				}
			}
			
			logger.debug("agrupadores final:" + agrupadores);*/
			
		} else {
			copagos = consultasAseguradoDAOSISA.obtieneCopagosPoliza(poliza);
		}
		return copagos;
	}
	
	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasPoliza(PolizaVO poliza) throws Exception {
		
		List<CoberturaBasicaVO> coberturasPoliza = new ArrayList<CoberturaBasicaVO>();
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){
			coberturasPoliza = consultasAseguradoDAOSISA.obtieneCoberturasPoliza(poliza);
		} else {
			coberturasPoliza = consultasAseguradoDAOICE.obtieneCoberturasPoliza(poliza);
		}
		return coberturasPoliza;
	}
	
	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasBasicas(PolizaVO poliza) throws Exception {
		
		List<CoberturaBasicaVO> coberturasBasicas = new ArrayList<CoberturaBasicaVO>();
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
			coberturasBasicas = consultasAseguradoDAOSISA.obtieneCoberturasBasicas(poliza);
		}
		return coberturasBasicas;
	}
	
	@Override
	public List<HistoricoVO> obtieneHistoricoAsegurado(
			PolizaAseguradoVO polizaAsegurado, AseguradoVO asegurado) throws Exception {
		
		List<HistoricoVO> historico = new ArrayList<HistoricoVO>();  
		
		//Si iCodPoliza es nulo, es informaci&oacute;n de ICE, sino es de SISA:
		if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza()) == false){			
			historico = consultasAseguradoDAOSISA.obtieneHistoricoAsegurado(polizaAsegurado, asegurado);
		} else {
			historico = consultasAseguradoDAOICE.obtieneHistoricoAsegurado(polizaAsegurado, asegurado);
		}
				
		return historico;
	}
	
	@Override
	public List<HistoricoFarmaciaVO> obtieneHistoricoFarmacia(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<HistoricoFarmaciaVO> historicoFarmacia = new ArrayList<HistoricoFarmaciaVO>();
		//Si iCodPoliza es nulo, es informaci&oacute;n de ICE, sino es de SISA:
		if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){			  
			  historicoFarmacia = consultasAseguradoDAOSISA.obtieneHistoricoFarmacia(poliza, asegurado);
		}
		  
		return historicoFarmacia;
	}
	
	@Override
	public List<PeriodoVigenciaVO> obtienePeriodosVigencia(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<PeriodoVigenciaVO> periodosVigencia = new ArrayList<PeriodoVigenciaVO>();
		// Si iCodPoliza viene vacio, es informaci&oacute;n de ICE:
		if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){			  
			  periodosVigencia = consultasAseguradoDAOSISA.obtienePeriodosVigencia(poliza, asegurado);
		}
		  
	  return periodosVigencia;
	}

	@Override
	public List<BaseVO> obtieneHospitales(String filtro, PolizaVO poliza) throws Exception {
		List<BaseVO> hospitales = new ArrayList<BaseVO>();
		// Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		if(StringUtils.isNotBlank(poliza.getIcodpoliza())){
			hospitales = consultasAseguradoDAOSISA.obtieneHospitales("%"+filtro+"%");
		} else{
			hospitales = consultasAseguradoDAOICE.obtieneHospitales(filtro);
		}
		return hospitales;
	}
	
	@Override
	public List<AvisoHospitalizacionVO> obtieneAvisosAnteriores(
			PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		List<AvisoHospitalizacionVO> avisosHospitalizacion = new ArrayList<AvisoHospitalizacionVO>();
		//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
		if(StringUtils.isNotBlank(poliza.getIcodpoliza())){
			avisosHospitalizacion = consultasAseguradoDAOSISA.obtieneAvisosAnteriores(asegurado);
		}else{
			avisosHospitalizacion = consultasAseguradoDAOICE.obtieneAvisosAnteriores(asegurado);
		}
		return avisosHospitalizacion;
	}

	@Override
	public String enviarAvisoHospitalizacion(AvisoHospitalizacionVO aviso, PolizaVO poliza)
			throws ApplicationException {
		try{
			String iCodAviso;
			//Si iCodPoliza no es nulo, es informaci&oacute;n de SISA.
			if(StringUtils.isNotBlank(poliza.getIcodpoliza())){
				iCodAviso = consultasAseguradoDAOSISA.enviarAvisoHospitalizacion(aviso);
			}else{
				iCodAviso = consultasAseguradoDAOICE.enviarAvisoHospitalizacion(aviso);
			}
			return iCodAviso;
		}catch (Exception e){
			throw new ApplicationException("1", e);
		}
	}

	@Override
	public String consultaTelefonoAgente(String cdagente)
			throws ApplicationException {
		try {
			String telefonoAgente = "";
				telefonoAgente = consultasAseguradoDAOSIGS.consultaTelefonoAgente(cdagente);
			return telefonoAgente;
		} catch (Exception e) {
			throw new ApplicationException("2", e);
		}
	}

	@Override
	public void actualizaEstatusEnvio(String iCodAviso, PolizaVO poliza)
			throws ApplicationException {
		try {
			if(StringUtils.isNotBlank(poliza.getIcodpoliza())){
				//Si iCodPoliza no es nulo, enviar flujo SISA
				consultasAseguradoDAOSISA.actualizaEstatusEnvio(iCodAviso);
			}else{
				//Si iCodPoliza es nulo, enviar flujo ICE
				consultasAseguradoDAOICE.actualizaEstatusEnvio(iCodAviso);
			}
		} catch (Exception e) {
			throw new ApplicationException("5",e);
		}
	}

	@Override
	public List<SolicitudCxPVO> obtieneListadoSolicitudesCxp() throws Exception {
		try {
			List<SolicitudCxPVO> loadList = new ArrayList<SolicitudCxPVO>();
			loadList = consultasAseguradoDAOSIGS.obtieneListadoSolicitudesCxp();
			return loadList;
		} catch (Exception e) {
			throw new ApplicationException("2", e);
		}
	}

	/*@Override
	public public String obtieneListadoSolicitudesCxp() throws Exception {
			try {
				String loadList ="";
				loadList = consultasAseguradoDAOSIGS.obtieneListadoSolicitudesCxp();
				return loadList;
			} catch (Exception e) {
				throw new ApplicationException("2", e);
			}
	}*/
	
}
