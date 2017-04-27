package mx.com.gseguros.portal.consultas.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasDAOImpl;
import mx.com.gseguros.portal.consultas.model.AseguradoDetalleVO;
import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.consultas.model.CoberturaBasicaVO;
import mx.com.gseguros.portal.consultas.model.ContratanteVO;
import mx.com.gseguros.portal.consultas.model.CopagoVO;
import mx.com.gseguros.portal.consultas.model.DatosComplementariosVO;
import mx.com.gseguros.portal.consultas.model.HistoricoFarmaciaVO;
import mx.com.gseguros.portal.consultas.model.HistoricoVO;
import mx.com.gseguros.portal.consultas.model.PeriodoVigenciaVO;
import mx.com.gseguros.portal.consultas.model.PlanVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.consultas.model.ReciboAgenteVO;
import mx.com.gseguros.portal.consultas.model.SuplementoVO;
import mx.com.gseguros.portal.consultas.model.TarifaVO;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.AgentePolizaVO;
import mx.com.gseguros.portal.general.model.ClausulaVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.ReciboVO;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;
import net.sf.jasperreports.engine.util.LinkedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ConsultasPolizaManagerImpl implements ConsultasPolizaManager {
	
	private org.apache.log4j.Logger logger =org.apache.log4j.Logger.getLogger(ConsultasPolizaManagerImpl.class);
	
	@Autowired
	@Qualifier("consultasDAOICEImpl")
	private ConsultasPolizaDAO consultasPolizaDAOICE;
	
	@Autowired
	@Qualifier("consultasDAOSISAImpl")
	private ConsultasPolizaDAO consultasPolizaDAOSISA;

//	@Autowired
//	@Qualifier("consultasAseguradoDAOSIGSImpl")
//	private IConsultasAseguradoDAO consultasAseguradoDAOSIGS;
	
	@Autowired
	private AutosSIGSDAO autosDAOSIGS;
	
	@Autowired
    private CotizacionDAO cotizacionDAO;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Override
	public List<PolizaAseguradoVO> obtienePolizasAsegPromotor(String user,
			String rfc, String cdperson, String nombre) throws Exception {
		
		List<PolizaAseguradoVO> polizasICE  = consultasPolizaDAOICE.obtienePolizasAsegPromotor(user, rfc, cdperson, nombre);
//		List<PolizaAseguradoVO> polizasSISA = consultasPolizaDAOSISA.obtienePolizasAsegurado(user, rfc, cdperson, nombre);
		
		// Fusionamos las listas:
        List<PolizaAseguradoVO> polizasFusionadas = polizasICE;
//        if(polizasFusionadas == null) {
//        	polizasFusionadas = new ArrayList<PolizaAseguradoVO>();
//        }
//        polizasFusionadas.addAll(polizasSISA);
        
		return polizasFusionadas;
	}
	
	@Override
	public List<PolizaAseguradoVO> obtienePolizasAsegurado(String user,
			String rfc, String cdperson, String nombre) throws Exception {
		
		List<PolizaAseguradoVO> polizasICE  = consultasPolizaDAOICE.obtienePolizasAsegurado(user, rfc, cdperson, nombre);
//		List<PolizaAseguradoVO> polizasSISA = consultasPolizaDAOSISA.obtienePolizasAsegurado(user, rfc, cdperson, nombre);
		
		// Fusionamos las listas:
        List<PolizaAseguradoVO> polizasFusionadas = polizasICE;
//        if(polizasFusionadas == null) {
//        	polizasFusionadas = new ArrayList<PolizaAseguradoVO>();
//        }
//        polizasFusionadas.addAll(polizasSISA);
        
		return polizasFusionadas;
	}
	
	@Override
	public List<SuplementoVO> obtieneHistoricoPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		
		List<SuplementoVO> suplementos;
		
		// Si iCodPoliza viene vacio, es informacion de ICE, sino es de SISA:
//		if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza())){
		if(polizaAsegurado.getNombreAsegurado().isEmpty()){
			suplementos = consultasPolizaDAOICE.obtieneHistoricoPoliza(polizaAsegurado);			
		}else{
			suplementos = consultasPolizaDAOICE.obtieneHistoricoPolizaAsegurado(polizaAsegurado);
		}
//		} else {
//			suplementos = consultasPolizaDAOSISA.obtieneHistoricoPoliza(polizaAsegurado);
//		}
				
		return suplementos;
	}

	@Override
	public List<SuplementoVO> obtieneHistoricoPolizaCorto(String sucursal, String producto, String polizacorto, String cdsisrol) 
			throws Exception{
		
		List<SuplementoVO> suplementos;  
		
		// Si iCodPoliza viene vacio, es informacion de ICE, sino es de SISA:
//		if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza())){
		suplementos = consultasPolizaDAOICE.obtieneHistoricoPolizaCorto(sucursal, producto, polizacorto, cdsisrol); 
//		} else {
//			suplementos = consultasPolizaDAOSISA.obtieneHistoricoPoliza(polizaAsegurado);
//		}
		
		return suplementos;
	}
	
	@Override
	public List<HistoricoVO> obtieneHistoricoPolizaSISA(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		
		List<HistoricoVO> historico = new ArrayList<HistoricoVO>();  
		
		//Si iCodPoliza es nulo, es informacion de ICE, sino es de SISA:
//		if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza()) == false){			
//			historico = consultasPolizaDAOSISA.obtieneHistoricoPolizaSISA(polizaAsegurado);
//		}
				
		return historico;
	}

	@Override
	public List<PolizaDTO> obtieneDatosPoliza(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		 List<PolizaDTO> datosPolizas;
		 
		 //Si iCodPoliza no es nulo, es informacion de SISA.
//		 if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza())){
			 datosPolizas = consultasPolizaDAOICE.obtieneDatosPoliza(polizaAsegurado);
//		 } else {
//			 datosPolizas = consultasPolizaDAOSISA.obtieneDatosPoliza(polizaAsegurado);
//		 }
		 
		 return datosPolizas;
	}

	@Override
	public List<Map<String, String>> obtieneDatosPolizaTvalopol(
			PolizaAseguradoVO polizaAsegurado) throws Exception {
		List<Map<String, String>> datosPolizas;
		
		//Si iCodPoliza no es nulo, es informacion de SISA.
//		 if(StringUtils.isBlank(polizaAsegurado.getIcodpoliza())){
		datosPolizas = consultasPolizaDAOICE.obtieneDatosPolizaTvalopol(polizaAsegurado);
//		 } else {
//			 datosPolizas = consultasPolizaDAOSISA.obtieneDatosPoliza(polizaAsegurado);
//		 }
		
		return datosPolizas;
	}
	
	@Override
	public List<DatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<DatosComplementariosVO> datosComplementarios = new ArrayList<DatosComplementariosVO>();
		
		//Si iCodPoliza no es nulo, es informacion de SISA.
//		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
//			 datosComplementarios = consultasPolizaDAOSISA.obtieneDatosComplementarios(poliza, asegurado);
//		 }
		 return datosComplementarios;
		
	}

	
	@Override
	public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza, String nmsituac) throws Exception {
		
		List<CopagoVO> copagos;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			
			copagos = consultasPolizaDAOICE.obtieneCopagosPoliza(poliza,nmsituac);
			
			/**
			 * Se comenta para desagrupar
			 * 
			 * // Agregamos un campo que agrupe los resultados:
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
			}*/
			
//			logger.debug("agrupadores final:" + agrupadores);
			
//		} else {
//			copagos = consultasPolizaDAOSISA.obtieneCopagosPoliza(poliza);
//		}
		return copagos;
	}
	
	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasPoliza(PolizaVO poliza) throws Exception {
		
		List<CoberturaBasicaVO> coberturasPoliza;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			coberturasPoliza = new ArrayList<CoberturaBasicaVO>();			
//		} else {
//			coberturasPoliza = consultasPolizaDAOSISA.obtieneCoberturasPoliza(poliza);
//		}
		return coberturasPoliza;
	}
	
	@Override
	public List<CoberturaBasicaVO> obtieneCoberturasBasicas(PolizaVO poliza) throws Exception {
		
		List<CoberturaBasicaVO> coberturasBasicas;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			coberturasBasicas = new ArrayList<CoberturaBasicaVO>();			
//		} else {
//			coberturasBasicas = consultasPolizaDAOSISA.obtieneCoberturasBasicas(poliza);
//		}
		return coberturasBasicas;
	}
	
	@Override
	public List<PlanVO> obtieneDatosPlan(PolizaVO poliza) throws Exception {
		List<PlanVO> datosPlan = new ArrayList<PlanVO>();
		
		//Si iCodPoliza no es nulo, es informacion de SISA.
//		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
//			 datosPlan = consultasPolizaDAOSISA.obtieneDatosPlan(poliza);
//		 }
		 return datosPlan;
		
	}
	

	@Override
	public List<ContratanteVO> obtieneDatosContratante(PolizaVO poliza) throws Exception {
		List<ContratanteVO> datosContratante = new ArrayList<ContratanteVO>();
		
		//Si iCodPoliza no es nulo, es informacion de SISA.
//		 if(StringUtils.isBlank(poliza.getIcodpoliza()) == false){		
//			 datosContratante = consultasPolizaDAOSISA.obtieneDatosContratante(poliza);
//		 }
		 return datosContratante;
		
	}
	
		
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza) throws Exception {
		
		List<AseguradoVO> asegurados;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			asegurados = consultasPolizaDAOICE.obtieneAsegurados(poliza);
//		} else {
//			asegurados = consultasPolizaDAOSISA.obtieneAsegurados(poliza);
//		}
		return asegurados;
	}
	
	@Override
	public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, long start, long limit) throws Exception {
		
		List<AseguradoVO> asegurados;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			asegurados = consultasPolizaDAOICE.obtieneAsegurados(poliza,start,limit);
//		} else {
//			asegurados = consultasPolizaDAOSISA.obtieneAsegurados(poliza);
//		}
		return asegurados;
	}
	
	
	@Override
	public List<ClausulaVO> obtieneEndososPoliza(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		
		List<ClausulaVO> clausulas;
		
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		  if(StringUtils.isBlank(poliza.getIcodpoliza())){
			  logger.debug("Clausulas de ICE");
			  clausulas=consultasPolizaDAOICE.obtieneEndososPoliza(poliza, asegurado);
//		  } else {
//			  logger.debug("Clausulas de SISA");
//			  clausulas=consultasPolizaDAOSISA.obtieneEndososPoliza(poliza, asegurado);
//		}
		  if(clausulas != null && clausulas.size()>0) {
			  logger.debug("Clausulas encontradas:" + clausulas.size());
			  logger.debug("Clausulas:" + clausulas);
		  } else {
			  logger.debug("No hay clausulas");
		  }
		  
		 return clausulas;
	}
	
	
	@Override
	public List<HistoricoFarmaciaVO> obtieneHistoricoFarmacia(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<HistoricoFarmaciaVO> historicoFarmacia;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			  logger.debug("Historico Farmacia de ICE");
			  historicoFarmacia = new ArrayList<HistoricoFarmaciaVO>();
//		} else {
//			  logger.debug("Historico Farmacia de SISA");
//			  
//			  historicoFarmacia = consultasPolizaDAOSISA.obtieneHistoricoFarmacia(poliza, asegurado);
//		}
//		  if(historicoFarmacia != null && historicoFarmacia.size()>0) {
//			  logger.debug("Historico Farmacia registros:" + historicoFarmacia.size());
//			  logger.debug("Historico Farmacia:" + historicoFarmacia);
//		  } else {
//			  logger.debug("No se encontraron registros de Historico Farmacia");
//		  }
		  return historicoFarmacia;
	}
	
	@Override
	public List<PeriodoVigenciaVO> obtienePeriodosVigencia(PolizaVO poliza,
			AseguradoVO asegurado) throws Exception {
		List<PeriodoVigenciaVO> periodosVigencia;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			  logger.debug("Periodos Vigencia de ICE");
			  
			  periodosVigencia = new ArrayList<PeriodoVigenciaVO>();
//		} else {
//			  logger.debug("Historico Farmacia de SISA");
//			  
//			  periodosVigencia = consultasPolizaDAOSISA.obtienePeriodosVigencia(poliza, asegurado);
//		}
//		  if(periodosVigencia != null && periodosVigencia.size()>0) {
//			  logger.debug("Periodos Vigencia registros:" + periodosVigencia.size());
//			  logger.debug("Periodos Vigencia:" + periodosVigencia);
//		  } else {
//			  logger.debug("No se encontraron registros de Historico Farmacia");
//		  }
		  return periodosVigencia;
	}
	
	@Override
	public List<ClausulaVO> obtieneClausulasPoliza(PolizaVO poliza, AseguradoVO asegurado) throws Exception {
		return consultasPolizaDAOICE.obtieneEndososPoliza(poliza, asegurado);
	}
	
	
	@Override
	public String obtieneMensajeAgente(PolizaVO poliza) throws Exception {
		return consultasPolizaDAOICE.obtieneMensajeAgente(poliza);
	}
	
	
	@Override
	public List<AseguradoDetalleVO> obtieneAseguradoDetalle(
			AseguradoVO asegurado) throws Exception {
//		return consultasPolizaDAOSISA.obtieneAseguradoDetalle(asegurado);
		return null;
	}
	

	@Override
	public List<ReciboVO> obtieneRecibosPoliza(PolizaVO poliza)
			throws Exception {
		return null;
	}
	
	
	@Override
	public List<DetalleReciboVO> obtieneDetalleRecibo(PolizaVO poliza,
			ReciboVO recibo) throws Exception {
		return null;
	}
	
	
	@Override
	public List<AgentePolizaVO> obtieneAgentesPoliza(PolizaVO poliza) throws Exception {
		List<AgentePolizaVO> agentes;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			agentes = consultasPolizaDAOICE.obtieneAgentesPoliza(poliza);
//		} else {
//			agentes = consultasPolizaDAOSISA.obtieneAgentesPoliza(poliza);
//		}
		return agentes;
	}
	
	
	@Override
	public List<ReciboAgenteVO> obtieneRecibosAgente(PolizaVO poliza)
			throws Exception {
		List<ReciboAgenteVO> recibosAgente;
		// Si iCodPoliza viene vacio, es informacion de ICE:
//		if(StringUtils.isBlank(poliza.getIcodpoliza())){
			recibosAgente = consultasPolizaDAOICE.obtieneRecibosAgente(poliza);
//		} else {
//			recibosAgente = consultasPolizaDAOSISA.obtieneRecibosAgente(poliza);
//		}
		return recibosAgente;
	}

	@Override
	public List<Map<String, String>> ejecutaQuery(String query, String password)
			throws Exception {
		String paso = "entro a ejecuta query";
		List<Map<String, String>> lista = null;
		try{
			paso = "verificando password";
			String usuario = flujoMesaControlDAO.recuperaNombreMd5(Utils.convierteTextfieldCodificadoEnMD5(password));
			paso = "entro a ejecuta query";
			if(query.trim().toUpperCase().startsWith("SELECT")){
				paso = "ejecuta select";
				lista = consultasPolizaDAOICE.getQueryResult(query, usuario);
			}
			else{
				paso = "ejecuta plsql";
				lista = new ArrayList<Map<String, String>>();
				consultasPolizaDAOICE.executePLSQL(query, usuario);
			}
		}catch(Exception ex){
			Utils.generaExcepcion(ex, paso);
		}
		return lista;
	}
	
	@Override
	public List<TarifaVO> obtieneTarifasPoliza(PolizaVO poliza) throws Exception {
		return consultasPolizaDAOICE.obtieneTarifasPoliza(poliza);
	}

	@Override
    public List<Map<String,String>> obtieneRecibosPolizaAuto(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception{
		return consultasPolizaDAOICE.obtieneRecibosPolizaAuto(cdunieco, cdramo, cdestado, nmpoliza, nmsuplem);
	}

	@Override
	public int obtieneNumeroDeIncisosPoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception{
		return consultasPolizaDAOICE.obtieneNumeroDeIncisosPoliza(cdunieco, cdramo, cdestado, nmpoliza, nmsuplem);
	}

	@Override
	public String obtieneNmsituacContratantePoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza) throws Exception{
		return consultasPolizaDAOICE.obtieneNmsituacContratantePoliza(cdunieco, cdramo, cdestado, nmpoliza);
	}

	@Deprecated
	@Override
	public void actualizaTramiteMC(PolizaVO poliza,String estra) throws Exception{
	autosDAOSIGS.actualizaTramiteMC(poliza,estra);
	}
	
	@Override
	public void actualizaTramiteEmisionMC(String inNumsuc,String inNumram,String inNumpol,String inRensuc,String inRenram,String inRenpol,String inUsuario) throws Exception{
	autosDAOSIGS.actualizaTramiteEmisionMC(inNumsuc,inNumram,inNumpol,inRensuc,inRenram,inRenpol,inUsuario);
	}
	
	@Override
	public Integer spIdentificaRenovacion(String inNumsuc,String inNumram,String inNumpol,Date vFechaEmision,Date vInicioVigencia,Date vFinVigencia,String inRensuc,String inRenram,String inRenpol) throws Exception {
           return autosDAOSIGS.spIdentificaRenovacion(inNumsuc, inNumram, inNumpol, vFechaEmision, vInicioVigencia, vFinVigencia, inRensuc, inRenram, inRenpol);
    }
	
	@Override
	public Map<String, String> obtieneDatosLigasRecibosPoliza(String cdunieco, String cdramo, String cdestado,
			String nmpoliza, String nmsuplem) throws Exception {
		return consultasPolizaDAOICE.obtieneDatosLigasRecibosPoliza(cdunieco, cdramo, cdestado, nmpoliza, nmsuplem);
	}
	
	@Override
    public List<Map<String, String>> consultaIncisosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception {
		return consultasPolizaDAOICE.consultaIncisosPoliza(cdunieco, cdramo, estado, nmpoliza);
	}
	
	@Override
    public Map<String, String> obtieneTvalopol(String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception {
        return cotizacionDAO.cargarTvalopol(cdunieco, cdramo, estado, nmpoliza);
    }
	
	@Override
    public List<Map<String, String>> obtieneCoberturas( String cdramo, String cdtipsit, String cdplan)
            throws Exception {
        return cotizacionDAO.getCoberturas(cdramo, cdplan, cdtipsit);
    }
	
}