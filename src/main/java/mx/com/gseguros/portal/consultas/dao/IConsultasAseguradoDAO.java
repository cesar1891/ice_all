package mx.com.gseguros.portal.consultas.dao;

import java.util.List;

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
import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.consultas.model.HistoricoVO;
import mx.com.gseguros.portal.consultas.model.PeriodoVigenciaVO;
import mx.com.gseguros.portal.consultas.model.PlanVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.portal.general.model.SolicitudCxPVO;

public interface IConsultasAseguradoDAO {
	/**
     * Obtiene los asegurados asociados a un criterio de b�squeda
     * @param rfc RFC del asegurado
     * @param cdperson C&oacute;digo de persona del asegurado
     * @param nombre Nombre del asegurado
     * @return ConsultaResultadosAseguradoVO
     * @throws Exception
     */
	public List<ConsultaResultadosAseguradoVO> obtieneResultadosAsegurado(String rfc, String cdperson, String nombre) throws Exception;
	
	/**
	 * 
	 * @param polizaAsegurado
	 * @return
	 * @throws Exception
	 */
	public List<ConsultaPolizaActualVO> obtienePolizaActual(PolizaAseguradoVO polizaAsegurado) throws Exception;
	
	/**
     * Obtiene los datos complementarios de la p&oacute;liza
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<ConsultaDatosComplementariosVO> obtieneDatosComplementarios(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene los datos generales de la p&oacute;liza
     * @param polizaAsegurado
     * @return
     * @throws Exception
     */
    public List<ConsultaDatosGeneralesPolizaVO> obtieneDatosPoliza(PolizaAseguradoVO polizaAsegurado) throws Exception;
    
    /**
     * Obtiene el detalle del asegurado
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<AseguradoDetalleVO> obtieneAseguradoDetalle(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene datos del titular
     * @param poliza
     * @param asegurado
     * @return
     */
    public List<ConsultaDatosTitularVO> obtieneDatosTitular(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
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
    public List<AseguradoVO> obtieneAsegurados(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene los endosos del asegurado
     * @param poliza
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<EndosoVO> obtieneEndososPoliza(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    /**
     * Obtiene las enfermedades cr�nicas del asegurado
     * @param poliza
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<EnfermedadVO> obtieneEnfermedades(PolizaVO poliza, AseguradoVO asegurado) throws Exception;
    
    
    /**
     * Obtiene los datos del plan
     * @param poliza
     * @return
     */
    public List<PlanVO> obtieneDatosPlan(PolizaVO poliza) throws Exception;
    
    /**
     * Obtiene los copagos de la p&oacute;liza
     * @param poliza
     * @return
     */
    public List<CopagoVO> obtieneCopagosPoliza(PolizaVO poliza) throws Exception;
    
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
	 * 
	 * @param polizaAsegurado
	 * @return
	 * @throws Exception
	 */
	public List<HistoricoVO> obtieneHistoricoAsegurado(PolizaAseguradoVO polizaAsegurado, AseguradoVO asegurado) throws Exception;
	
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
     * Obtiene lista de hospitales
     * @return
     * @throws Exception
     */
    public List<BaseVO> obtieneHospitales(String filtro) throws Exception;
    
    /**
     * Obtiene avisos de hospitalizacion
     * @param asegurado
     * @return
     * @throws Exception
     */
    public List<AvisoHospitalizacionVO> obtieneAvisosAnteriores(AseguradoVO asegurado) throws Exception;
    
    /**
     * Inserta aviso de hospitalizacion
     * @param aviso
     * @return
     * @throws Exception
     */
    public String enviarAvisoHospitalizacion(AvisoHospitalizacionVO aviso) throws Exception;

    /**
     * Obtiene telefono de agente
     * @param cdagente
     * @return
     * @throws Exception
     */
    public String consultaTelefonoAgente(String cdagente) throws Exception;
    
    /**
     * Actualiza el estatus de envio de aviso hospitalizacion
     * @param iCodAviso
     * @return
     * @throws Exception
     */
	public void actualizaEstatusEnvio(String iCodAviso) throws Exception;

	public List<SolicitudCxPVO> obtieneListadoSolicitudesCxp() throws Exception;
}
