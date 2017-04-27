package mx.com.gseguros.portal.consultas.dao;

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


public interface ConsultasPolizaDAO {
	
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
	
	
	/*
	 * Obtiene el hist&oacute;rico (suplementos) de una p&oacute;liza
	 * @param nmpoliex N&uacute;mero externo/completo de la p&oacute;liza
	 * @return El hist&oacute;rico (suplementos) de la p&oacute;liza
	 * @throws Exception
	 *
	public List<ConsultaDatosSuplementoVO> obtieneHistoricoPoliza(String nmpoliex, String icodpoliza) throws Exception;
	*/
	
	
	/**
	 * 
	 * @param polizaAsegurado
	 * @return
	 * @throws Exception
	 */
	public List<SuplementoVO> obtieneHistoricoPoliza(PolizaAseguradoVO polizaAsegurado) throws Exception;
	
	/**
	 * recupera historico de suplementos en los que
	 * se encuentra el asegurado insertado
	 * @param polizaAsegurado
	 * @return
	 * @throws Exception
	 */
	public List<SuplementoVO> obtieneHistoricoPolizaAsegurado(PolizaAseguradoVO polizaAsegurado) throws Exception;
	
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
	
	
	/*
	 * Obtiene los datos generales de la p&oacute;liza
	 * @param cdunieco
	 * @param cdramo
	 * @param estado
	 * @param nmpoliza
	 * @return
	 * @throws Exception
	 *
    public List<PolizaVO> obtieneDatosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String icodpoliza) throws Exception;
    */
    
    
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
     * Obtiene datos del contratante
     * @param poliza
     * @return
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
     * @param poliza,start,limit
     * @return
     * @throws Exception
     */
    public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza,long start,long limit) throws Exception;
    

    /**
     * Obtiene las exclusiones/clausulas de la poliza
     * @param poliza
     * @return
     * @throws Exception
     */
    public List<ClausulaVO> obtieneEndososPoliza(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene un mensaje para el agente dependiendo la poliza recibida
     * @param poliza
     * @return
     * @throws Exception
     */
    public String obtieneMensajeAgente (PolizaVO poliza) throws Exception;
    
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
     * Obtiene el detalle del asegurado
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
    
	public List<Map<String, String>> getQueryResult(String query, String usuario) throws Exception;
	
	public List<Map<String, String>> executePLSQL(String query,String usuario) throws Exception;
	
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
    public List<Map<String, String>> consultaIncisosPoliza(String cdunieco, String cdramo, String estado, String nmpoliza)
			throws Exception;
    
    /**
     * Inserta en una bitacora las polizas generadas para recupera remota  
     * @param fecha    Fecha de proceso
     * @param nombre   Nombre del archivo procesado
     * @param polizas  Numero de polizas procesadas 
     * @param rango    Rango de polizas procesadas
     * @param usuario  
     */
    public String insertaBitacora(Date fecha,String nombre,int polizas,String rango,String usuario) 
    		throws Exception;

    
}