package mx.com.gseguros.portal.consultas.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import mx.com.gseguros.portal.cotizacion.model.AgentePolizaVO;
import mx.com.gseguros.portal.general.model.ClausulaVO;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.ReciboVO;


public interface ConsultasPolizaManager {
		
	/**
     * Obtiene las polizas del Promotor que tenga asociadas a los asegurados de sus Agentes
     * @param user Usuario actual activo
     * @param rfc RFC del asegurado
     * @param cdperson C&oacute;digo de persona del asegurado
     * @param nombre Nombre del asegurado
     * @return Polizas asociadas al asegurado
     * @throws Exception
     */
	
	public List<PolizaAseguradoVO> obtienePolizasAsegPromotor(String user, String rfc, String cdperson, String nombre) throws Exception;
	
	/**
     * Obtiene las polizas asociadas al asegurado
     * @param user Usuario actual activo
     * @param rfc RFC del asegurado
     * @param cdperson C&oacute;digo de persona del asegurado
     * @param nombre Nombre del asegurado
     * @return Polizas asociadas al asegurado
     * @throws Exception
     */
	
	public List<PolizaAseguradoVO> obtienePolizasAsegurado(String user, String rfc, String cdperson, String nombre) throws Exception;
	
	/**
	 * 
	 * @param polizaAsegurado
	 * @return
	 * @throws Exception
	 */
	public List<SuplementoVO> obtieneHistoricoPoliza(PolizaAseguradoVO polizaAsegurado) throws Exception;

	/**
	 * Obtiene el historial de una poliza por medio de numero de poliza corto
	 * @param sucursal
	 * @param producto
	 * @param polizacorto
	 * @return
	 * @throws Exception
	 */
	public List<SuplementoVO> obtieneHistoricoPolizaCorto(String sucursal, String producto, String polizacorto, String cdsisrol) throws Exception;
	
	/**
	 * 
	 * @param polizaAsegurado
	 * @return
	 * @throws Exception
	 */
	public List<HistoricoVO> obtieneHistoricoPolizaSISA(PolizaAseguradoVO polizaAsegurado) throws Exception;
	
    
    /**
     * Obtiene los datos generales de la p&oacute;liza
     * @param polizaAsegurado
     * @return
     * @throws Exception
     */
    public List<PolizaDTO> obtieneDatosPoliza(PolizaAseguradoVO polizaAsegurado) throws Exception;

    /**
     * Obtiene los datos generales de la p&oacute;liza
     * @param polizaAsegurado
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> obtieneDatosPolizaTvalopol(PolizaAseguradoVO polizaAsegurado) throws Exception;
    
    
    /**
     * Obtiene los datos complementarios de la p&oacute;liza
     * @param poliza
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<DatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    
    /**
     * Obtiene los copagos de la p&oacute;liza
     * @param poliza
     * @return
     */
    public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza, String nmsituac) throws Exception;
    
    /**
     * Obtiene las coberturas
     * @param poliza
     * @return
     */
    public List<CoberturaBasicaVO> obtieneCoberturasPoliza(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene las coberturas b�sicas
     * @param poliza
     * @return
     */
    public List<CoberturaBasicaVO> obtieneCoberturasBasicas(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene los datos del plan
     * @param poliza
     * @return
     */
    public List<PlanVO> obtieneDatosPlan(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene los datos del contratante
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<ContratanteVO> obtieneDatosContratante(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene los asegurados de la p&oacute;liza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene los asegurados de la p&oacute;liza
     * @param poliza, start,limit
     * @return
     * @throws Exception
     */
    public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, long start,long limit) throws Exception;
    

    /**
     * Obtiene las exclusiones/clausulas de la poliza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<ClausulaVO> obtieneEndososPoliza(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene las clausulas de la poliza
     * @param poliza
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<ClausulaVO> obtieneClausulasPoliza(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene mensaje para el agente, para la poliza enviada 
     * @param poliza
     * @return
     * @throws Exception
     */
    public String obtieneMensajeAgente(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene las tarifas de una poliza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<TarifaVO> obtieneTarifasPoliza(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene hist�rico de farmacia
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<HistoricoFarmaciaVO> obtieneHistoricoFarmacia(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene periodos de vigencia
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<PeriodoVigenciaVO> obtienePeriodosVigencia(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * 
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<AseguradoDetalleVO> obtieneAseguradoDetalle(AseguradoVO asegurado) throws Exception;
    
    
    /**
     * Obtiene los recibos asociados a una p&oacute;liza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<ReciboVO> obtieneRecibosPoliza(PolizaVO poliza) throws Exception;
    
    
    /**
     * Obtiene los datos de un recibo asociado a una p&oacute;liza
     * @param poliza
     * @param recibo
     * @return
     * @throws Exception
     */
    public List<DetalleReciboVO> obtieneDetalleRecibo(PolizaVO poliza, ReciboVO recibo) throws Exception;
    
    
    /**
     * Obtiene los agentes asociados a una p&oacute;liza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<AgentePolizaVO> obtieneAgentesPoliza(PolizaVO poliza) throws Exception;
    
    
    /**
     * Obtiene los recibos del agente asociados a una p&oacute;liza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<ReciboAgenteVO> obtieneRecibosAgente(PolizaVO poliza) throws Exception;

    /**
     * Metodo para obtener los recibos de polizas de autos
     * @param cdunieco
     * @param cdramo
     * @param cdestado
     * @param nmpoliza
     * @param nmsuplem
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> obtieneRecibosPolizaAuto(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception;
    
    /**
     * Obtiene el numero de incisos de una poliza 
     * @param cdunieco
     * @param cdramo
     * @param cdestado
     * @param nmpoliza
     * @param nmsuplem
     * @return
     * @throws Exception
     */
    public int obtieneNumeroDeIncisosPoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception;
    
    /**
     * Obtiene el numero de nmsituac del contratate de una poliza 
     * @param cdunieco
     * @param cdramo
     * @param cdestado
     * @param nmpoliza
     * @return
     * @throws Exception
     */
    public String obtieneNmsituacContratantePoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza) throws Exception;

    /**
     * Obtiene el numero de nmsituac del contratate de una poliza 
     * @param cdunieco
     * @param cdramo
     * @param nmpoliza
     * @param numtra
     * @throws Exception
     */
    @Deprecated
	public void actualizaTramiteMC(PolizaVO poliza, String estra) throws Exception;
    
    /**
     * Obtiene el numero de nmsituac del contratate de una poliza 
     * @param cdunieco
     * @param cdramo
     * @param nmpoliza
     * @param numtra
     * @throws Exception
     */
    @Deprecated
	public void actualizaTramiteEmisionMC(String inNumsuc,String inNumram,String inNumpol,String inRensuc,String inRenram,String inRenpol,String inUsuario) throws Exception;
    
    public List<Map<String, String>> ejecutaQuery(String query, String password) throws Exception;
    
    /**
     * Para obtener los datos de tramite, solici, tipmov de un suplemento de poliza y enviar las ligas de recibos
     * @param cdunieco
     * @param cdramo
     * @param cdestado
     * @param nmpoliza
     * @param nmsuplem
     * @return
     * @throws Exception
     */
    public Map<String, String> obtieneDatosLigasRecibosPoliza(String cdunieco,String cdramo,String cdestado,String nmpoliza,String nmsuplem) throws Exception;
    
    /**
     * Consulta los incisos de una poliza
     * @param cdunieco Sucursal de la poliza
     * @param cdramo   Ramo de la poliza
     * @param estado   Estado de la poliza
     * @param nmpoliza Numero de poliza
     * @return Incisos de la poliza
     * @throws Exception
     */
    public List<Map<String, String>> consultaIncisosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception;

    /**
     * Actualiza sigs poliza de renovacion 
     * @param inNumsuc
     * @param inNumram
     * @param inNumpol
     * @param vFechaEmision
     * @param string
     * @param string2
     * @param inRensuc
     * @param inRenram
     * @param inRenpol
     * @throws Exception
     */
    public Integer spIdentificaRenovacion(String inNumsuc,String inNumram,String inNumpol,Date vFechaEmision,Date vInicioVigencia,Date vFinVigencia,String inRensuc,String inRenram,String inRenpol) throws Exception;
    
    public Map<String, String> obtieneTvalopol(String cdunieco, String cdramo, String estado, String nmpoliza)
            throws Exception;

	public List<Map<String, String>> obtieneCoberturas(String cdramo, String cdtipsit, String cdplan) throws Exception;

}