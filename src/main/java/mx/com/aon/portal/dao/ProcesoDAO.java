package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import mx.com.aon.flujos.cotizacion.model.AyudaCoberturaCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.CoberturaCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.DatosEntradaCotizaVO;
import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.SituacionVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.portal.cotizacion.model.AgentePolizaVO;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.ObtieneTatrigarMapper;
import mx.com.gseguros.portal.cotizacion.model.ObtieneTatripolMapper;
import mx.com.gseguros.portal.cotizacion.model.ObtieneTatrisitMapper;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.emision.model.DatosRecibosDxNVO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Ccomision;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteGeneral;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.ClienteSalud;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.DirCli;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Recibo;
import mx.com.gseguros.ws.ice2sigs.client.model.ReciboWrapper;
import oracle.jdbc.OracleTypes;

/**
 * Clase que contiene los accesos a datos que se encuentran en Base De Datos de
 * los procesos del sistema (Cotizacion, Emision, Endoso, etc.)
 * 
 * @author Adolfo
 * @see AbstractDAO
 * 
 */
@Deprecated
public class ProcesoDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(ProcesoDAO.class);

	public static final String EJECUTA_SIGSVALIPOL_EMI = "EJECUTA_SIGSVALIPOL_EMI";
	public static final String CALCULA_NUMERO_POLIZA = "CALCULA_NUMERO_POLIZA";
	public static final String GENERA_SUPLEMENTO_FISICO = "GENERA_SUPLEMENTO_FISICO";
	public static final String GENERA_SUPLEMENTO_LOGICO = "GENERA_SUPLEMENTO_LOGICO";
	public static final String GENERA_SUPLEMENTOS = "GENERA_SUPLEMENTOS";
	public static final String MOV_T_DESC_SUP = "MOV_T_DESC_SUP";
	public static final String INSERTA_MAESTRO_HISTORICO_POLIZAS = "INSERTA_MAESTRO_HISTORICO_POLIZAS";
	public static final String MOV_MPOLAGEN = "MOV_MPOLAGEN";
	public static final String MOV_MPOLIAGE = "MOV_MPOLIAGE";
	public static final String CLONAR_SITUACION = "CLONAR_SITUACION";
	public static final String EJECUTA_P_EXEC_SIGSVDEF = "EJECUTA_P_EXEC_SIGSVDEF";
	public static final String EJECUTA_P_EXEC_SIGSVDEFEND = "EJECUTA_P_EXEC_SIGSVDEFEND";
	public static final String PROC_INCISO_DEF = "PROC_INCISO_DEF";
	public static final String PROCESO_EMISION = "PROCESO_EMISION";
	public static final String OBTIENE_DESCRIPCION = "OBTIENE_DESCRIPCION";
	public static final String CALCULA_NUMERO_OBJETO = "CALCULA_NUMERO_OBJETO";
	public static final String OBTIENE_TVALOSIT_COTIZA = "OBTIENE_TVALOSIT_COTIZA";
	public static final String EXEC_VALIDADOR = "EXEC_VALIDADOR";
	public static final String P_MOV_MPOLIZAS = "P_MOV_MPOLIZAS";
	public static final String P_MOV_TVALOSIT = "P_MOV_TVALOSIT";
    public static final String P_MOV_MPOLISIT = "P_MOV_MPOLISIT";
    public static final String P_UPD_TVALOSIT = "P_UPD_TVALOSIT";
    public static final String P_CLONAR_PERSONAS="P_CLONAR_PERSONAS";
    public static final String OBTENER_RESULTADOS_COTIZACION="OBTENER_RESULTADOS_COTIZACION";
    public static final String OBTENER_RESULTADOS_COTIZACION2="OBTENER_RESULTADOS_COTIZACION2";
    public static final String OBTENER_COBERTURAS="OBTENER_COBERTURAS";
    public static final String OBTENER_AYUDA_COBERTURA="OBTENER_AYUDA_COBERTURA";
    public static final String OBTENER_TATRISIT="OBTENER_TATRISIT";
    public static final String OBTENER_TATRISIN="OBTENER_TATRISIN";
    public static final String OBTENER_TATRISIN_POLIZA="OBTENER_TATRISIN_POLIZA";
    public static final String OBTENER_TATRIPOL="OBTENER_TATRIPOL";
    public static final String OBTENER_DATOS_USUARIO="OBTENER_DATOS_USUARIO";
    public static final String INSERTAR_DETALLE_SUPLEMEN="INSERTAR_DETALLE_SUPLEME";
    public static final String COMPRAR_COTIZACION="COMPRAR_COTIZACION";
    public static final String GET_INFO_MPOLIZAS="GET_INFO_MPOLIZAS";
    public static final String OBTENER_TMANTENI="OBTENER_TMANTENI";
    public static final String OBTENER_ASEGURADOS="OBTENER_ASEGURADOS";
    public static final String OBTENER_DOCUMENTOS_POLIZA="OBTENER_DOCUMENTOS_POLIZA";
    public static final String OBTENER_LISTA_DOC_POLIZA_NUEVA="OBTENER_LISTA_DOC_POLIZA_NUEVA";
    public static final String OBTENER_POLIZA_COMPLETA="OBTENER_POLIZA_COMPLETA";
    public static final String P_MOV_TVALOPOL="P_MOV_TVALOPOL";
    public static final String P_MOV_TVALOGAR="P_MOV_TVALOGAR";
    public static final String P_MOV_TVALOPER="P_MOV_TVALOPER";
    public static final String P_GET_TVALOPOL="P_GET_TVALOPOL";
    public static final String P_GET_TVALOGAR="P_GET_TVALOGAR";
    public static final String P_GET_TVALOPER="P_GET_TVALOPER";
    public static final String GENERA_MPERSON="GENERA_MPERSON";
    public static final String P_MOV_MPERSONA="P_MOV_MPERSONA";
    public static final String P_MOV_MPOLIPER="P_MOV_MPOLIPER";
    public static final String P_BORRA_MPOLIPER="P_BORRA_MPOLIPER";
    public static final String P_EXISTE_DOMICILIO="P_EXISTE_DOMICILIO";
    public static final String OBTENER_COBERTURAS_USUARIO="OBTENER_COBERTURAS_USUARIO";
    public static final String P_MOV_MPOLIGAR="P_MOV_MPOLIGAR";
    public static final String P_MOV_MPOLICAP="P_MOV_MPOLICAP";
    public static final String OBTENER_DETALLES_COTIZACION="OBTENER_DETALLES_COTIZACION";
    public static final String OBTENER_TATRIGAR="OBTENER_TATRIGAR";
    public static final String OBTENER_TATRIPER="OBTENER_TATRIPER";
    public static final String P_GET_DOMICIL="P_GET_DOMICIL";
    public static final String P_GET_DOMICIL_GENERAL="P_GET_DOMICIL_GENERAL";
    public static final String P_MOV_MDOMICIL="P_MOV_MDOMICIL";
    public static final String EMITIR = "EMITIR";
    //public static final String GUARDAR_ARCHIVO_POLIZA = "GUARDAR_ARCHIVO_POLIZA";
    public static final String GUARDAR_ARCHIVO_PERSONA = "GUARDAR_ARCHIVO_PERSONA";
    public static final String OBTENER_TIPOS_CLAUSULAS_EXCLUSION = "OBTENER_TIPOS_CLAUSULAS_EXCLUSION";
    public static final String LOAD_MESA_CONTROL = "LOAD_MESA_CONTROL";
    public static final String LOAD_MESA_CONTROL_USUARIO = "LOAD_MESA_CONTROL_USUARIO";
    public static final String LOAD_DETALLE_MESA_CONTROL = "LOAD_DETALLE_MESA_CONTROL";
    public static final String OBTENER_EXCLUSIONES_X_TIPO = "OBTENER_EXCLUSIONES_X_TIPO";
    public static final String OBTENER_HTML_CLAUSULA="OBTENER_HTML_CLAUSULA";
    //public static final String P_MOV_MESACONTROL="P_MOV_MESACONTROL";
    public static final String P_MOV_TVALOSIN="P_MOV_TVALOSIN";
    //public static final String P_MOV_DMESACONTROL="P_MOV_DMESACONTROL";
    public static final String OBTIENE_DATOS_RECIBOS="OBTIENE_DATOS_RECIBOS";
    public static final String OBTIENE_DATOS_COMISIONES="OBTIENE_DATOS_COMISIONES";
    public static final String OBTIENE_CATALOGO_COLONIAS="OBTIENE_CATALOGO_COLONIAS";
    public static final String OBTIENE_DATOS_CLIENTE="OBTIENE_DATOS_CLIENTE";
    public static final String OBTIENE_DATOS_CLIENTE_GENERAL="OBTIENE_DATOS_CLIENTE_GENERAL";
    public static final String OBTIENE_DATOS_CLIENTE_GENERAL_X_CDPERSON="OBTIENE_DATOS_CLIENTE_GENERAL_X_CDPERSON";
    public static final String OBTIENE_DATOS_DOMICILIO_GENERAL_X_CDPERSON="OBTIENE_DATOS_DOMICILIO_GENERAL_X_CDPERSON";
    public static final String MESACONTROL_UPDATE_SOLICI="MESACONTROL_UPDATE_SOLICI";
    public static final String MESACONTROL_UPDATE_STATUS="MESACONTROL_UPDATE_STATUS";
    public static final String MESACONTROL_FINALIZAR_DETALLE="MESACONTROL_FINALIZAR_DETALLE";
    public static final String DOCUMENTOS_PREPARAR_CONTRARECIBO="DOCUMENTOS_PREPARAR_CONTRARECIBO";
    public static final String OBTIENE_VALOSIT_SITUAC="OBTIENE_VALOSIT_SITUAC";
    public static final String BUSCAR_RFC="BUSCAR_RFC";
    public static final String BORRAR_MPERSONA="BORRAR_MPERSONA";
    public static final String OBTENER_RAMOS="OBTENER_RAMOS";
    public static final String OBTENER_TIPSIT="OBTENER_TIPSIT";
    public static final String P_MOV_TBITACOBROS="P_MOV_TBITACOBROS";
    public static final String P_VAL_INFO_PERSONAS="P_VAL_INFO_PERSONAS";    
    public static final String VALIDA_TITULAR_MENOR_EDAD="VALIDA_TITULAR_MENOR_EDAD";    
    public static final String OBTIENE_DATOS_POLIZA_AGENTE =    "OBTIENE_DATOS_POLIZA_AGENTE";
    public static final String OBTIENE_DATOS_POLIZA_AGENTE_X_PUNTOS =    "OBTIENE_DATOS_POLIZA_AGENTE_X_PUNTOS";
	public static final String OBTIENE_DATOS_GENERAL_AGENTE =	"OBTIENE_DATOS_GENERAL_AGENTE";
	public static final String GUARDA_PORCENTAJE_POLIZA =	"GUARDA_PORCENTAJE_POLIZA";
	public static final String GUARDA_PORCENTAJE_POLIZA_X_PUNTOS =	"GUARDA_PORCENTAJE_POLIZA_X_PUNTOS";
	public static final String GUARDA_ELIMINA_PORCENTAJE_POLIZA =	"GUARDA_ELIMINA_PORCENTAJE_POLIZA";
	public static final String GUARDA_ELIMINA_PORCENTAJE_POLIZA_X_PUNTOS =	"GUARDA_ELIMINA_PORCENTAJE_POLIZA_X_PUNTOS";
	public static final String OBTIENE_DATOS_RECIBOS_DxN =	"OBTIENE_DATOS_RECIBOS_DxN";
	public static final String GUARDA_PERIODOS_DXN =	"GUARDA_PERIODOS_DXN";
	public static final String LANZA_PROCESO_DXN =	"LANZA_PROCESO_DXN";
	public static final String VALIDAR_EXTRAPRIMA       =	"VALIDAR_EXTRAPRIMA";
	public static final String OBTEN_CDTIPSIT_GS       =	"OBTEN_CDTIPSIT_GS";
	public static final String OBTEN_SUBRAMO_GS       =	"OBTEN_SUBRAMO_GS";
	public static final String VALIDAR_EXTRAPRIMA_SITUAC_READ  =	"VALIDAR_EXTRAPRIMA_SITUAC_READ";
	public static final String VALIDAR_EXTRAPRIMA_SITUAC   =	"VALIDAR_EXTRAPRIMA_SITUAC";
	public static final String P_OBTIENE_MESACONTROL_SUPER = "P_OBTIENE_MESACONTROL_SUPER";
	public static final String HABILITA_SIG_RECIBO = "HABILITA_SIG_RECIBO";
	public static final String VALIDA_USUARIO_SUCURSAL = "VALIDA_USUARIO_SUCURSAL";
	public static final String ACTUALIZA_CDIDEPER = "ACTUALIZA_CDIDEPER";
	public static final String VALIDA_DATOS_AUTOS = "VALIDA_DATOS_AUTOS";
	public static final String ACTUALIZA_POLALT = "ACTUALIZA_POLALT";
	public static final String CARGA_COBRANZA_MASIVA = "CARGA_COBRANZA_MASIVA";
	public static final String OBTIENE_COBRANZA_APLICADA = "OBTIENE_COBRANZA_APLICADA";
	public static final String OBTIENE_REMESA_APLICADA = "OBTIENE_REMESA_APLICADA";

	protected void initDao() throws Exception {
		addStoredProcedure(EJECUTA_SIGSVALIPOL_EMI, new EjecutarSIGSVALIPOL_EMI(getDataSource()));
		addStoredProcedure(CALCULA_NUMERO_POLIZA, new CalculaNumeroPoliza(getDataSource()));
		addStoredProcedure(GENERA_SUPLEMENTO_FISICO, new GeneraSuplementoFisico(getDataSource()));
		addStoredProcedure(GENERA_SUPLEMENTO_LOGICO, new GeneraSuplementoLogico(getDataSource()));
		addStoredProcedure(MOV_T_DESC_SUP, new MovTDescSup(getDataSource()));
		addStoredProcedure(INSERTA_MAESTRO_HISTORICO_POLIZAS, new MovMsupleme(getDataSource()));
		addStoredProcedure(MOV_MPOLIAGE, new MovMpoliage(getDataSource()));
		addStoredProcedure(EXEC_VALIDADOR, new EjecutaValidador(getDataSource()));
		addStoredProcedure(OBTIENE_TVALOSIT_COTIZA, new ObtenerTvalositCotiza(getDataSource()));
		addStoredProcedure(CLONAR_SITUACION, new ClonaSituacion(getDataSource()));
		addStoredProcedure(EJECUTA_P_EXEC_SIGSVDEF, new EjecutaSIGSVDEF(getDataSource()));
		addStoredProcedure(EJECUTA_P_EXEC_SIGSVDEFEND, new EjecutaSIGSVDEFEnd(getDataSource()));
		addStoredProcedure(P_MOV_MPOLIZAS, new InsertaMaestroPolizas(getDataSource()));
		addStoredProcedure(P_MOV_TVALOSIT, new InsertaValoresSituaciones(getDataSource()));
		addStoredProcedure(P_UPD_TVALOSIT, new ActualizaValoresSituaciones(getDataSource()));
        addStoredProcedure(P_MOV_MPOLISIT, new InsertaMPolisit(getDataSource()));
        addStoredProcedure(P_CLONAR_PERSONAS, new ClonaPersonas(getDataSource()));
		addStoredProcedure(OBTENER_RESULTADOS_COTIZACION, new ObtieneResultadosCotiza(getDataSource()));
		addStoredProcedure(OBTENER_RESULTADOS_COTIZACION2, new ObtieneResultadosCotiza2(getDataSource()));
        addStoredProcedure(OBTENER_COBERTURAS, new ObtieneCoberturas(getDataSource()));
        addStoredProcedure(OBTENER_AYUDA_COBERTURA, new ObtieneAyudaCoberturas(getDataSource()));
        addStoredProcedure(OBTENER_TATRISIT, new ObtieneTatrisit(getDataSource()));
        addStoredProcedure(OBTENER_TATRISIN, new ObtieneTatrisin(getDataSource()));
        addStoredProcedure(OBTENER_TATRISIN_POLIZA, new ObtieneTatrisinPoliza(getDataSource()));
        addStoredProcedure(OBTENER_TATRIPOL, new ObtieneTatripol(getDataSource()));
        addStoredProcedure(OBTENER_DATOS_USUARIO, new ObtieneDatosUsuario(getDataSource()));
        addStoredProcedure(INSERTAR_DETALLE_SUPLEMEN, new InsertarDetalleSuplemen(getDataSource()));
        addStoredProcedure(COMPRAR_COTIZACION, new ComprarCotizacion(getDataSource()));
        addStoredProcedure(GET_INFO_MPOLIZAS, new GetInfoMpolizas(getDataSource()));
        addStoredProcedure(OBTENER_TMANTENI, new ObtenerTmanteni(getDataSource()));
        addStoredProcedure(OBTENER_ASEGURADOS, new ObtenerAsegurados(getDataSource()));
        addStoredProcedure(OBTENER_DOCUMENTOS_POLIZA, new ObtenerDocumentosPoliza(getDataSource()));
        //addStoredProcedure(OBTENER_LISTA_DOC_POLIZA_NUEVA, new ObtenerListaDocPolizaNueva(getDataSource()));
        addStoredProcedure(OBTENER_POLIZA_COMPLETA, new ObtenerPolizaCompleta(getDataSource()));
        addStoredProcedure(P_MOV_TVALOPOL, new PMovTvalopol(getDataSource()));
        addStoredProcedure(P_MOV_TVALOGAR, new PMovTvalogar(getDataSource()));
        addStoredProcedure(P_MOV_TVALOPER, new PMovTvaloper(getDataSource()));
        addStoredProcedure(P_GET_TVALOPOL, new PGetTvalopol(getDataSource()));
        addStoredProcedure(P_GET_TVALOGAR, new PGetTvalogar(getDataSource()));
        addStoredProcedure(P_GET_TVALOPER, new PGetTvaloper(getDataSource()));
        addStoredProcedure(GENERA_MPERSON, new GeneraMperson(getDataSource()));
        addStoredProcedure(P_MOV_MPERSONA, new PMovMpersona(getDataSource()));
        addStoredProcedure(P_MOV_MPOLIPER, new PMovMpoliper(getDataSource()));
        addStoredProcedure(OBTENER_COBERTURAS_USUARIO, new ObtenerCoberturasUsuario(getDataSource()));
        addStoredProcedure(P_MOV_MPOLIGAR, new PMovMpoligar(getDataSource()));
        addStoredProcedure(P_MOV_MPOLICAP, new PMovMpolicap(getDataSource()));
        addStoredProcedure(OBTENER_DETALLES_COTIZACION, new ObtenerDetallesCotizacion(getDataSource()));
        addStoredProcedure(OBTENER_TATRIGAR, new ObtieneTatrigar(getDataSource()));
        addStoredProcedure(OBTENER_TATRIPER, new ObtieneTatriper(getDataSource()));
        addStoredProcedure(P_GET_DOMICIL, new ObtenerDomicilio(getDataSource()));
        addStoredProcedure(P_GET_DOMICIL_GENERAL, new ObtenerDomicilioGeneral(getDataSource()));
        addStoredProcedure(P_MOV_MDOMICIL, new PMovMdomicil(getDataSource()));
        addStoredProcedure(EMITIR, new Emitir(getDataSource()));
        //addStoredProcedure(GUARDAR_ARCHIVO_POLIZA, new GuardarArchivoPoliza(getDataSource()));
        addStoredProcedure(GUARDAR_ARCHIVO_PERSONA, new GuardarArchivoPersona(getDataSource()));
        addStoredProcedure(OBTENER_TIPOS_CLAUSULAS_EXCLUSION, new ObtenerTiposClausulasExclusion(getDataSource()));
        addStoredProcedure(LOAD_MESA_CONTROL, new ObtenerMesaControl(getDataSource()));
        addStoredProcedure(LOAD_MESA_CONTROL_USUARIO, new ObtenerMesaControlUsuario(getDataSource()));
        addStoredProcedure(P_OBTIENE_MESACONTROL_SUPER, new ObtenerMesaControlSuper(getDataSource()));
        addStoredProcedure(LOAD_DETALLE_MESA_CONTROL, new ObtenerDetalleMesaControl(getDataSource()));
        addStoredProcedure(OBTENER_EXCLUSIONES_X_TIPO, new ObtenerExclusionesXTipo(getDataSource()));
        addStoredProcedure(OBTENER_HTML_CLAUSULA, new ObtenerHtmlClausula(getDataSource()));
        //addStoredProcedure(P_MOV_MESACONTROL, new PMovMesacontrol(getDataSource()));
        addStoredProcedure(P_MOV_TVALOSIN, new PMovTvalosin(getDataSource()));
        //addStoredProcedure(P_MOV_DMESACONTROL, new PMovDmesacontrol(getDataSource()));
        addStoredProcedure(P_BORRA_MPOLIPER, new PBorraMpoliper(getDataSource()));
        addStoredProcedure(P_EXISTE_DOMICILIO, new PExisteDomicilio(getDataSource()));

        addStoredProcedure(OBTIENE_DATOS_RECIBOS, new ObtenDatosRecibos(getDataSource()));
        addStoredProcedure(OBTIENE_DATOS_COMISIONES, new ObtenDatosComisiones(getDataSource()));
        addStoredProcedure(OBTIENE_CATALOGO_COLONIAS, new ObtenCatalogoColonias(getDataSource()));
        addStoredProcedure(OBTIENE_DATOS_CLIENTE, new ObtenDatosCliente(getDataSource()));
        addStoredProcedure(OBTIENE_DATOS_CLIENTE_GENERAL, new ObtenDatosClienteGeneral(getDataSource()));
        addStoredProcedure(OBTIENE_DATOS_CLIENTE_GENERAL_X_CDPERSON, new ObtenDatosClienteGeneralXcdperson(getDataSource()));
        addStoredProcedure(OBTIENE_DATOS_DOMICILIO_GENERAL_X_CDPERSON, new ObtenDatosDomicilioGeneralXcdperson(getDataSource()));
        addStoredProcedure(MESACONTROL_UPDATE_SOLICI, new MesaControlUpdateSolici(getDataSource()));
        addStoredProcedure(MESACONTROL_UPDATE_STATUS, new MesaControlUpdateStatus(getDataSource()));
        addStoredProcedure(MESACONTROL_FINALIZAR_DETALLE, new MesaControlFinalizarDetalle(getDataSource()));
        addStoredProcedure(DOCUMENTOS_PREPARAR_CONTRARECIBO, new DocumentosPrepararContrarecibo(getDataSource()));
        addStoredProcedure(OBTIENE_VALOSIT_SITUAC,new ObtieneValositSituac(getDataSource()));
        addStoredProcedure(BUSCAR_RFC,new BuscarRFC(getDataSource()));
        addStoredProcedure(BORRAR_MPERSONA,new BorrarMPoliper(getDataSource()));
        addStoredProcedure(OBTENER_RAMOS,new ObtenerRamos(getDataSource()));
        addStoredProcedure(OBTENER_TIPSIT,new ObtenerTipsit(getDataSource()));
        addStoredProcedure(P_MOV_TBITACOBROS,new MovBitacobros(getDataSource()));
        addStoredProcedure(P_VAL_INFO_PERSONAS,new PValInfoPersonas(getDataSource()));
        addStoredProcedure(VALIDA_TITULAR_MENOR_EDAD,new ValidaTitularMenorEdad(getDataSource()));
        
        addStoredProcedure(OBTIENE_DATOS_POLIZA_AGENTE,      new ObtieneDatosPolizaAgente(getDataSource()));
        addStoredProcedure(OBTIENE_DATOS_POLIZA_AGENTE_X_PUNTOS,      new ObtieneDatosPolizaAgentePorPuntos(getDataSource()));
		addStoredProcedure(OBTIENE_DATOS_GENERAL_AGENTE,      new ObtieneDatosGeneralAgente(getDataSource()));
		addStoredProcedure(GUARDA_PORCENTAJE_POLIZA, new GuardarPorcentajePoliza(getDataSource()));
		addStoredProcedure(GUARDA_PORCENTAJE_POLIZA_X_PUNTOS, new GuardarPorcentajePolizaPorPuntos(getDataSource()));
		addStoredProcedure(GUARDA_ELIMINA_PORCENTAJE_POLIZA, new GuardarEliminarPorcentajePoliza(getDataSource()));
		addStoredProcedure(GUARDA_ELIMINA_PORCENTAJE_POLIZA_X_PUNTOS, new GuardarEliminarPorcentajePolizaPorPuntos(getDataSource()));
		addStoredProcedure(OBTIENE_DATOS_RECIBOS_DxN, new ObtenDatosRecibosDxN(getDataSource()));
		addStoredProcedure(GUARDA_PERIODOS_DXN, new GuardaPeriodosDxN(getDataSource()));
		addStoredProcedure(LANZA_PROCESO_DXN, new LanzaProcesoDxN(getDataSource()));
		addStoredProcedure(VALIDAR_EXTRAPRIMA, new ValidarExtraprima(getDataSource()));
		addStoredProcedure(OBTEN_CDTIPSIT_GS, new  ObtenCdtipsitGS(getDataSource()));
		addStoredProcedure(OBTEN_SUBRAMO_GS, new  ObtenSubramoGS(getDataSource()));
		addStoredProcedure(VALIDAR_EXTRAPRIMA_SITUAC, new ValidarExtraprimaSituac(getDataSource()));
		addStoredProcedure(VALIDAR_EXTRAPRIMA_SITUAC_READ, new ValidarExtraprimaSituacRead(getDataSource()));
		addStoredProcedure(HABILITA_SIG_RECIBO, new HabilitaSigRecibo(getDataSource()));
		addStoredProcedure(VALIDA_USUARIO_SUCURSAL, new ValidaUsuarioSucursal(getDataSource()));
		addStoredProcedure(ACTUALIZA_CDIDEPER, new ActualizaCdIdeper(getDataSource()));
		addStoredProcedure(VALIDA_DATOS_AUTOS, new ValidaDatosAutos(getDataSource()));
		addStoredProcedure(ACTUALIZA_POLALT, new ActualizaPolizaExterna(getDataSource()));
		addStoredProcedure(CARGA_COBRANZA_MASIVA, new CargaCobranzaMasiva(getDataSource()));
		addStoredProcedure(OBTIENE_COBRANZA_APLICADA, new ObtieneCobranzaAplicada(getDataSource()));
		addStoredProcedure(OBTIENE_REMESA_APLICADA, new ObtieneRemesaAplicada(getDataSource()));
	}
	
	protected class ActualizaCdIdeper extends CustomStoredProcedure {

		protected ActualizaCdIdeper(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_ACTUALIZA_CODCLI_EXTERNO");

			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdideper_i", OracleTypes.VARCHAR));

	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
	}
	protected class ValidaDatosAutos extends CustomStoredProcedure {
		
		protected ValidaDatosAutos(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_VALIDA_DAT_OBLIG_AUTOS");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	protected class ActualizaPolizaExterna extends CustomStoredProcedure {
		
		protected ActualizaPolizaExterna(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_ACTUALIZA_NMPOLIEX_AUTOS");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliex_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduniext_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ramo_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}

	protected class CargaCobranzaMasiva extends CustomStoredProcedure {
		
		protected CargaCobranzaMasiva(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_APLICA_COBRANZA_MASIVA");
			
			declareParameter(new SqlParameter("pv_archivo_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	
	
	protected class EjecutarSIGSVALIPOL_EMI extends CustomStoredProcedure {

		protected EjecutarSIGSVALIPOL_EMI(DataSource dataSource) {
			super(dataSource, "PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_EMI");

			declareParameter(new SqlParameter("pv_cdusuari_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdelemen_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));

	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
	}
	
	
	protected class CalculaNumeroPoliza extends CustomStoredProcedure {

		protected CalculaNumeroPoliza(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_CALC_NUMPOLIZA");

			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_nmpoliza_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			String numeroPoliza = null;
			if(map.get("pv_nmpoliza_o") != null) numeroPoliza = map.get("pv_nmpoliza_o").toString();
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("NUMERO_POLIZA", numeroPoliza);

			return wrapperResultados;
		}
	}
	protected class PExisteDomicilio extends CustomStoredProcedure {
		
		protected PExisteDomicilio(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_EXISTE_DAT_MDOMICIL");
			
			declareParameter(new SqlParameter("pv_cdideper_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_existe_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			String existe = null;
			String cdperson = null;
			
			if(map.get("pv_existe_o") != null)   existe   = map.get("pv_existe_o").toString();
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("EXISTE_DOMICILIO", existe);
			wrapperResultados.getItemMap().put("CDPERSON", cdperson);
			
			return wrapperResultados;
		}
	}

	protected class GeneraSuplementoFisico extends CustomStoredProcedure {
		
		protected GeneraSuplementoFisico(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_GENERA_SUPL_FISICO");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_fecha_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_hhinival_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_nmsuplem_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			String numeroSuplemento = null;

			if(map.get("pv_nmsuplem_o") != null) numeroSuplemento = map.get("pv_nmsuplem_o").toString();
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("SUPLEMENTO_FISICO", numeroSuplemento);
			
			return wrapperResultados;
		}
	}
	protected class GeneraSuplementoLogico extends CustomStoredProcedure {
		
		protected GeneraSuplementoLogico(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_GENERA_SUPL_LOGICO");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
			
			declareParameter(new SqlOutParameter("pv_nsuplogi_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			String numeroSuplemento = null;

			if(map.get("pv_nsuplogi_o") != null) numeroSuplemento = map.get("pv_nsuplogi_o").toString();
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("SUPLEMENTO_LOGICO", numeroSuplemento);
			
			return wrapperResultados;
		}
	}
	
	protected class MovTDescSup extends CustomStoredProcedure {
		
		protected MovTDescSup(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_MOV_TDESCSUP");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nsuplogi_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdtipsup_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_feemisio_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsolici_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_fesolici_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ferefere_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdseqpol_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cduser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nusuasus_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nlogisus_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	
	protected class MovMsupleme extends CustomStoredProcedure {
		
		protected MovMsupleme(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_MOV_MSUPLEME");
			
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feINival_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_hhinival_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fefINval_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_hhfinval_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swanula_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsuplogi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsupusua_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsupsess_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fesessio_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swconfir_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmrenova_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsuplori_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdorddoc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpolfro_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_pocofron_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpoldec_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tippodec_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	
	protected class MovMpoliage extends CustomStoredProcedure {
		
		protected MovMpoliage(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_MOV_MPOLIAGE");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdagente_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipoag_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_porredau_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmcuadro_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucurs_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_porparti_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
			
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	
	protected class EjecutaValidador extends CustomStoredProcedure {
    	
    	protected EjecutaValidador(DataSource dataSource) {
    		super(dataSource,"P_EXEC_VALIDADOR");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC)); 
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdbloque_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msgerror_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		
    		if(map.get("pv_msg_id_o") != null){
    			wrapperResultados.setMsgId(map.get("pv_msg_id_o").toString());
    		}else{
    			wrapperResultados.setMsgId("");
    		}
    		
    		if(map.get("pv_msgerror_o") != null){
    			wrapperResultados.setMsgText(map.get("pv_msgerror_o").toString());
    		}else{
    			wrapperResultados.setMsgText("");
    		}
			
    		return wrapperResultados;
    	}
    }
	
	
	
	protected class ObtenerTvalositCotiza extends CustomStoredProcedure {

        protected ObtenerTvalositCotiza(DataSource dataSource) {
            super(dataSource,"PKG_COTIZA.P_OBTIENE_TVALOSIT_COTIZA");
            
            declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new TvalositCotizaMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
         
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
        }
      }
    
    
    protected class TvalositCotizaMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	DatosEntradaCotizaVO tvalositVO = new DatosEntradaCotizaVO();
        	
        	tvalositVO.setDsAtribu(rs.getString("DSATRIBU"));
        	tvalositVO.setOtValor(rs.getString("OTVALOR")); 
        	tvalositVO.setDsNombre(rs.getString("DSNOMBRE"));
        	tvalositVO.setDsValor(rs.getString("DSVALOR")); 
         
            return tvalositVO;
        }
    }
    
    protected class ClonaSituacion extends CustomStoredProcedure {
    	
    	protected ClonaSituacion(DataSource dataSource) {
    		super(dataSource,"PKG_COTIZA.P_CLONAR_SITUACION");
    		
    		declareParameter(new SqlParameter("pv_cdelemen_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_fecha_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdusuario_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cadena_i", OracleTypes.VARCHAR));
    		    		
    		declareParameter(new SqlOutParameter("pv_record1_o", OracleTypes.CURSOR, new ClonaSituacionMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		
    		List result = (List) map.get("pv_record1_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    protected class ClonaSituacionMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		SituacionVO clonVO = new SituacionVO();
    		
    		clonVO.setCdTipsit(rs.getString("CDTIPSIT"));
    		clonVO.setCompAsegur(rs.getString("CDASEGUR"));
    		clonVO.setCdPlan(rs.getString("CDPLAN"));
    		clonVO.setNmSituac(rs.getString("NMSITUAC"));
    		
    		return clonVO;
    	}
    }
    protected class EjecutaSIGSVDEF extends CustomStoredProcedure {
    	
    	protected EjecutaSIGSVDEF(DataSource dataSource) {
    		super(dataSource,"P_EXEC_SIGSVDEF");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsup_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    		
    	}
    }
    
    protected class EjecutaSIGSVDEFEnd extends CustomStoredProcedure {
    	
    	protected EjecutaSIGSVDEFEnd(DataSource dataSource) {
    		super(dataSource,"P_EXEC_SIGSVDEFEND");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsup_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    		
    	}
    }
    
    protected class InsertaMaestroPolizas extends CustomStoredProcedure {
    	
    	protected InsertaMaestroPolizas(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES2.P_MOV_MPOLIZAS");
    		
    		declareParameter(new SqlParameter("pv_cdunieco"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo"    , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplem"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_status"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_swestado"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsolici"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_feautori"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdmotanu"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_feanulac"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_swautori"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdmoneda"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_feinisus"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_fefinsus"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ottempot"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_feefecto"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_hhefecto"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_feproren"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_fevencim"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmrenova"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_ferecibo"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_feultsin"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmnumsin"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdtipcoa"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_swtarifi"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_swabrido"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_feemisio"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdperpag"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmpoliex"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmcuadro"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_porredau"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_swconsol"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpolant"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpolnva"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_fesolici"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramant"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdmejred"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoldoc"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmpoliza2" , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmrenove"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplee"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_ttipcamc"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_ttipcamv"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_swpatent"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_pcpgocte"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_tipoflot"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_agrupador" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_accion"    , OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    
    ////////////////////////////////////////////
    ////// actualizar valores situaciones //////
    /*////////////////////////////////////////*/
protected class ActualizaValoresSituaciones extends CustomStoredProcedure {
    	
    	protected ActualizaValoresSituaciones(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES.P_UPD_TVALOSIT");
    		
    		declareParameter(new SqlParameter("pv_cdunieco", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsituac", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplem", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_status", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit", OracleTypes.VARCHAR));
    		for(int i=1;i<=99;i++)
    		{
    			declareParameter(new SqlParameter(
    					new StringBuilder("pv_otvalor")
    					.append(StringUtils.leftPad(String.valueOf(i),2,"0"))
    					.toString()
    					, OracleTypes.VARCHAR));
    		}
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    /*////////////////////////////////////////*/    
    ////// actualizar valores situaciones //////
    ////////////////////////////////////////////
    
    protected class InsertaValoresSituaciones extends CustomStoredProcedure {
    	
    	protected InsertaValoresSituaciones(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES2.P_MOV_TVALOSIT");
    		
    		declareParameter(new SqlParameter("pv_cdunieco", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsituac", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplem", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_status", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit", OracleTypes.VARCHAR));
    		for(int i=1;i<=99;i++)
    		{
    			declareParameter(new SqlParameter(
    					new StringBuilder("pv_otvalor")
    					.append(StringUtils.leftPad(String.valueOf(i),2,"0"))
    					.toString()
    					, OracleTypes.VARCHAR));
    		}
    		declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    
    
    protected class InsertaMPolisit extends CustomStoredProcedure
    {
    	protected InsertaMPolisit(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES.P_MOV_MPOLISIT");
            declareParameter(new SqlParameter("pv_cdunieco_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i",        OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado_i",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsituac_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsuplem_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_status_i",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_swreduci_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdagrupa_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdestado_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_fefecsit_i",      OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_fecharef_i",      OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_cdgrupo_i",       OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituaext_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsitaux_i",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsbsitext_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdplan_i",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdasegur_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_accion_i",        OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
            compile();
    	}
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
        {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
    }
    
    protected class ClonaPersonas extends CustomStoredProcedure
    {
    	protected ClonaPersonas(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_CLONAR_PERSONAS");
            declareParameter(new SqlParameter("pv_cdelemen_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsituac_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdtipsit_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fecha_i",     OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_cdusuario_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_p_nombre",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_s_nombre",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_apellidop",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_apellidom",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_sexo",        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fenacimi",    OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_parentesco",  OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",  OracleTypes.VARCHAR));
            compile();
    	}
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
        {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
    }
    
    ////////////////////////////////////////////
    ////// Override de obtener cotizacion //////
    /*////////////////////////////////////////*/
    protected class ObtieneResultadosCotiza extends CustomStoredProcedure
    {
    	protected ObtieneResultadosCotiza(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_GEN_TARIFICACION");
            declareParameter(new SqlParameter("pv_cdusuari_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdelemen_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_record_o", OracleTypes.CURSOR, new ResultadosCotizaMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_record_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ResultadosCotizaMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            //DecimalFormat formatter = new DecimalFormat("#,##0.00");
            ResultadoCotizacionVO result = new ResultadoCotizacionVO();
            result.setCdIdentifica(rs.getString("CDIDENTIFICA"));
            result.setCdUnieco(rs.getString("CDUNIECO"));
            result.setDsUnieco(	rs.getString("DSUNIECO"));
            result.setCdRamo(rs.getString("CDRAMO"));
            result.setEstado(rs.getString("ESTADO"));
            result.setNmPoliza(rs.getString("NMPOLIZA"));
            result.setNmSuplem(rs.getString("NMSUPLEM"));
            result.setStatus(rs.getString("STATUS"));
            result.setCdPlan(rs.getString("CDPLAN"));
            result.setDsPlan(rs.getString("DSPLAN"));
            result.setCdCiaaseg(rs.getString("CDCIAASEG"));
            result.setCdPerpag(rs.getString("CDPERPAG"));
            result.setDsPerpag(rs.getString("DSPERPAG"));
            result.setCdTipsit(rs.getString("CDTIPSIT"));
            //result.setDsTipsit(rs.getString("DSTIPSIT"));
            result.setFeEmisio(rs.getString("FEEMISIO"));
            result.setFeVencim(rs.getString("FEVENCIM"));
            result.setNumeroSituacion(rs.getString("NMSITUAC"));
            result.setMnPrima(rs.getString("MNPRIMA"));
            logger.debug("ResultadoCotizacionVO=" + result);
            return result;
        }
    }
    /*////////////////////////////////////////*/
    ////// Override de obtener cotizacion //////
    ////////////////////////////////////////////
    
    ////////////////////////////////////////////////////
    ////// Obtener resultados cotizacion con mapa //////
    /*////////////////////////////////////////////////*/
    protected class ObtieneResultadosCotiza2 extends CustomStoredProcedure
    {
    	protected ObtieneResultadosCotiza2(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_GEN_TARIFICACION");
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdelemen_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDIDENTIFICA" , "CDUNIECO" , "CDRAMO"    , "ESTADO"
            		,"NMPOLIZA"    , "NMSUPLEM" , "STATUS"    , "CDPLAN"
            		,"DSPLAN"      , "MNPRIMA"  , "CDCIAASEG" , "DSUNIECO"
            		,"CDPERPAG"    , "DSPERPAG" , "CDTIPSIT"  , "FEEMISIO"
            		,"FEVENCIM"    , "NMSITUAC"
            };
            declareParameter(new SqlOutParameter("pv_record_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
    	{
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_record_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    /*////////////////////////////////////////////////*/
    ////// Obtener resultados cotizacion con mapa //////
    ////////////////////////////////////////////////////
    
    ////////////////////////////////////////////
    ////// Override de obtener coberturas //////
    /*////////////////////////////////////////*/
    protected class ObtieneCoberturas extends CustomStoredProcedure
    {
    	protected ObtieneCoberturas(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_COBERTURAS");
            declareParameter(new SqlParameter("pv_usuario_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsituac_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdplan_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdcia_i",     OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_region_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_pais_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_idioma_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_record_o", OracleTypes.CURSOR, new ObtieneCoberturasMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_record_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneCoberturasMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            CoberturaCotizacionVO result=new CoberturaCotizacionVO();
            result.setDeducible(     rs.getString("deducible"));
            result.setCdGarant(      rs.getString("CDGARANT"));
            result.setDsGarant(      rs.getString("DSGARANT"));
            result.setSumaAsegurada( rs.getString("SUMASEG"));
            result.setOrden(         rs.getString("orden"));
            result.setCdCiaaseg(null);
            result.setCdRamo(null);
            return result;
        }
    }
    /*////////////////////////////////////////*/
    ////// Override de obtener coberturas //////
    ////////////////////////////////////////////
    
    //////////////////////////////////////
    ////// Obtener ayuda coberturas //////
    /*//////////////////////////////////*/
    protected class ObtieneAyudaCoberturas extends CustomStoredProcedure
    {
    	protected ObtieneAyudaCoberturas(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_AYUDA_COBERTURAS");
            declareParameter(new SqlParameter("pv_ciaaseg_i",   OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_garant_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("po_registro", OracleTypes.CURSOR, new ObtieneAyudaCoberturasMapper()));
            declareParameter(new SqlOutParameter("po_msg_id",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("p_out_title", OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("po_registro");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneAyudaCoberturasMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            AyudaCoberturaCotizacionVO ayuda=new AyudaCoberturaCotizacionVO();
            ayuda.setCdGarant(rs.getString("cdgarant"));
            ayuda.setDsAyuda(rs.getString("dsayuda"));
            ayuda.setDsGarant("dsgarant");
            return ayuda;
        }
    }
    /*//////////////////////////////////*/
    ////// Obtener ayuda coberturas //////
    //////////////////////////////////////
    
    //////////////////////////////////////////////
    ////// Obtiene campos tatrisit de tabla //////
    /*//////////////////////////////////////////*/
    protected class ObtieneTatrisit extends CustomStoredProcedure
    {
    	protected ObtieneTatrisit(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_SITUACION");
            declareParameter(new SqlParameter("pv_cdtipsit_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtieneTatrisitMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    /*//////////////////////////////////////////*/
    ////// Obtiene campos tatrisit de tabla //////
    //////////////////////////////////////////////
    
	//////////////////////////////////////////////
	////// Obtiene campos tatripol de tabla //////
	/*//////////////////////////////////////////*/
	protected class ObtieneTatripol extends CustomStoredProcedure
	{
		protected ObtieneTatripol(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_ATRI_POLIZA");
			declareParameter(new SqlParameter("pv_cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtippol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatripolMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	/*//////////////////////////////////////////*/
	////// Obtiene campos tatripol de tabla //////
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// Obtiene campos tatripol de tabla //////
	/*//////////////////////////////////////////*/
	protected class ObtieneTatrigar extends CustomStoredProcedure
	{
		protected ObtieneTatrigar(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_ATRI_GARANTIA");
			declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatrivar_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatrigarMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	/*//////////////////////////////////////////*/
	////// Obtiene campos tatripol de tabla //////
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// Obtiene campos tatripol de tabla //////
	/*//////////////////////////////////////////*/
	protected class ObtieneTatriper extends CustomStoredProcedure
	{
		protected ObtieneTatriper(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_ATRI_ROL");
			declareParameter(new SqlParameter("pv_cdramo_i",      	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i",         OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtieneTatriperMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	/*//////////////////////////////////////////*/
	////// Obtiene campos tatripol de tabla //////
	//////////////////////////////////////////////
    
    //////////////////////////////////////
    ////// Obtener datos usuario /////////
    /*//////////////////////////////////*/
    protected class ObtieneDatosUsuario extends CustomStoredProcedure
    {
    	protected ObtieneDatosUsuario(DataSource dataSource)
        {
            super(dataSource,"pkg_satelites.p_get_info_usuario");
            declareParameter(new SqlParameter(   "pv_cdusuari_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(   "pv_cdtipsit_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtieneDatosUsuarioMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneDatosUsuarioMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            DatosUsuario datosUsuario=new DatosUsuario();
            datosUsuario.setCdagente(rs.getString("cdagente"));
            datosUsuario.setCdperson(rs.getString("cdperson"));
            datosUsuario.setCdramo(rs.getString("cdramo"));
            datosUsuario.setCdtipsit(rs.getString("cdtipsit"));
            datosUsuario.setCdunieco(rs.getString("cdunieco"));
            datosUsuario.setCdusuari(rs.getString("cdusuari"));
            datosUsuario.setNmcuadro(rs.getString("nmcuadro"));
            datosUsuario.setNombre(rs.getString("nombre"));
            return datosUsuario;
        }
    }
    /*//////////////////////////////////*/
    ////// Obtener datos usuario /////////
    //////////////////////////////////////
    
    ///////////////////////////////////////
    ////// insertar detalle suplemen //////
    /*///////////////////////////////////*/
    protected class InsertarDetalleSuplemen extends CustomStoredProcedure
    {
    	protected InsertarDetalleSuplemen(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES.P_MOV_TDESCSUP");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nsuplogi_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsup_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_feemisio_i",  OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_nmsolici_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fesolici_i",  OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_ferefere_i",  OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_cdseqpol_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cduser_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nusuasus_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nlogisus_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperson_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_accion_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",	OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",	OracleTypes.VARCHAR));
            compile();
    	}
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
        {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
    }
    /*///////////////////////////////////*/
    ////// insertar detalle suplemen //////
    ///////////////////////////////////////
    
    ////////////////////////////////
    ////// comprar cotizacion //////
    /*////////////////////////////*/
    protected class ComprarCotizacion extends CustomStoredProcedure
    {
    	protected ComprarCotizacion(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_PROC_INCISO_DEF");
            declareParameter(new SqlParameter("pv_cdunieco",	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo",  	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado",  	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza",  	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac",  	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdelement",	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperson",	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdasegur",	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdplan",	OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperpag",	OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",	OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",	OracleTypes.VARCHAR));
            compile();
    	}
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
        {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
    }
    /*////////////////////////////*/
    ////// comprar cotizacion //////
    ////////////////////////////////
    
    //////////////////////////////////////////////
    ////// Obtiene informacion de poliza /////////
    /*//////////////////////////////////////////*/
    protected class GetInfoMpolizas extends CustomStoredProcedure
    {
    	protected GetInfoMpolizas(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES.P_GET_INFO_MPOLIZAS");
            declareParameter(new SqlParameter("pv_cdunieco",    OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new GetInfoMpolizasMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class GetInfoMpolizasMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            List<String>columns=new ArrayList<String>(0);
            columns.add("cdusuari");
            columns.add("cdunieco");
            columns.add("dsunieco");
            columns.add("cdperson");
            columns.add("cdagente");
            columns.add("nombre");
            columns.add("cdramo");
            columns.add("dsramo");
            columns.add("nmcuadro");
            columns.add("cdtipsit");
            columns.add("fesolici");
            columns.add("nmsolici");
            columns.add("feefecto");
            columns.add("feproren");
            columns.add("ottempot");
            columns.add("cdperpag");
            columns.add("cdplan");
            columns.add("dsplan");
            columns.add("nmrenova");
            columns.add("nmpolant");
            Map<String,Object>map=new HashMap<String,Object>(0);
            for(String columnName:columns)
            {
                if(columnName.substring(0,2).equals("fe"))
                {
                    map.put(columnName, rs.getDate(columnName));
                }
                else
                {
                    map.put(columnName, rs.getString(columnName));
                }
            }
            return map;
        }
    }
    /*//////////////////////////////////////////*/
    ////// Obtiene informacion de poliza /////////
    //////////////////////////////////////////////
    
    ///////////////////////////////////////////////////
    ////// obtiene un valosit para una situacion //////
    /*///////////////////////////////////////////////*/
    protected class ObtieneValositSituac extends CustomStoredProcedure
    {
    	protected ObtieneValositSituac(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_OBTIENE_TVALOSIT");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtieneValositSituacMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneValositSituacMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,Object> m=new HashMap<String,Object>(0);
			String columnas[]=new String[]{
					"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09","otvalor10",
					"otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19","otvalor20",
					"otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29","otvalor30",
					"otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39","otvalor40",
					"otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49","otvalor50",
					"nmsuplem",
					"status",
					"cdtipsit",
					"otvalor51","otvalor52","otvalor53","otvalor54","otvalor55","otvalor56","otvalor57","otvalor58","otvalor59","otvalor60",
					"otvalor61","otvalor62","otvalor63","otvalor64","otvalor65","otvalor66","otvalor67","otvalor68","otvalor69","otvalor70",
					"otvalor71","otvalor72","otvalor73","otvalor74","otvalor75","otvalor76","otvalor77","otvalor78","otvalor79","otvalor80",
					"otvalor81","otvalor82","otvalor83","otvalor84","otvalor85","otvalor86","otvalor87","otvalor88","otvalor89","otvalor90",
					"otvalor91","otvalor92","otvalor93","otvalor94","otvalor95","otvalor96","otvalor97","otvalor98","otvalor99"
			};
			for(String columna:columnas)
			{
				m.put(columna,rs.getString(columna));
			}
			return m;
		}
	}
    /*///////////////////////////////////////////////*/
    ////// obtiene un valosit para una situacion //////
    ///////////////////////////////////////////////////
    
    //////////////////////////////////////////////
    ////// obtiene tablas de catalogos ///////////
    /*//////////////////////////////////////////*/
    protected class ObtenerTmanteni extends CustomStoredProcedure
    {
    	protected ObtenerTmanteni(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_TMANTENI");
            declareParameter(new SqlParameter("pv_cdtabla",         OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerTmanteniMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtenerTmanteniMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            GenericVO generic=new GenericVO();
            generic.setKey(rs.getString("codigo"));
            generic.setValue(rs.getString("DESCRIPC"));
            return generic;
        }
    }
    /*//////////////////////////////////////////*/
    ////// obtiene tablas de catalogos ///////////
    //////////////////////////////////////////////
    
    //////////////////////////////////////////////
    ////// obtiene asegurados de poliza //////////
    /*
	nmsituac
	,cdrol
	,fenacimi
	,sexo
	,cdperson
	,nombre
	,segundo_nombre
	,Apellido_Paterno
	,Apellido_Materno
	,cdrfc
	,Parentesco
	,tpersona
	,nacional
	,swexiper
     */
    /*//////////////////////////////////////////*/
    protected class ObtenerAsegurados extends CustomStoredProcedure
    {
    	protected ObtenerAsegurados(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES.P_OBT_DATOS_MPOLIPER");
    		declareParameter(new SqlParameter("pv_cdunieco",    OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem",    OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerAseguradosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}

    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }

    protected class ObtenerAseguradosMapper implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Map<String,Object> r=new HashMap<String,Object>(0);
    		/*SELECT distinct a.nmsituac,a.cdrol cdrol,b.fenacimi fenacimi,b.otsexo sexo,
           a.cdperson cdperson,b.dsnombre nombre,b.dsnombre1 segundo_nombre,b.dsapellido Apellido_Paterno,
           b.dsapellido1 Apellido_Materno*/
    		/**
    		 * Problema:
    		 * Cuando cargo asegurados su fenacimi puede venir como 01/12/1990 o como 1990-12-01 00:00:00.0
    		 * Solucion:
    		 * Hacer este if
    		 */
    		String fenacimi=rs.getString("fenacimi");
    		if(fenacimi!=null)
    		{
    			System.out.println("#debugfenacimi "+fenacimi);
    			if(fenacimi.length()>10)
    			{
    				// 1 9 9 0 - 1 2 - 0 1 _ 0 0 : 0 0 : 0 0 . 0 <<CADENA
    				//0 1 2 3 4 5 6 7 8 9 k <<INDICE
    				String aux="";
    				aux+=fenacimi.substring(8,10);
    				aux+="/";
    				aux+=fenacimi.substring(5,7);
    				aux+="/";
    				aux+=fenacimi.substring(0,4);
    				fenacimi=aux;
    			}
    		}
    		String otfisjur=rs.getString("otfisjur");
    		if(otfisjur==null||otfisjur.length()==0)
    		{
    			otfisjur="F";
    		}
    		String cdnacion=rs.getString("cdnacion");
    		if(cdnacion==null||cdnacion.length()==0)
    		{
    			cdnacion="001";
    		}
    		r.put("nmsituac",			rs.getString("nmsituac"));
    		r.put("cdrol",				rs.getString("cdrol"));
    		r.put("fenacimi",			fenacimi);
    		r.put("sexo",				rs.getString("sexo"));
    		r.put("cdperson",			rs.getString("cdperson"));
    		r.put("nombre",				rs.getString("nombre"));
    		r.put("segundo_nombre",		rs.getString("segundo_nombre"));
    		r.put("Apellido_Paterno",	rs.getString("Apellido_Paterno"));
    		r.put("Apellido_Materno",	rs.getString("Apellido_Materno"));
    		r.put("cdrfc",				rs.getString("cdrfc"));
    		r.put("Parentesco",		    rs.getString("Parentesco"));
    		r.put("tpersona",           otfisjur);
    		r.put("nacional",           cdnacion);
    		r.put("swexiper",           rs.getString("swexiper"));
    		r.put("cdideper",           rs.getString("cdideper"));
    		r.put("cdideext",           rs.getString("cdideext"));
    		r.put("CANALING"          , rs.getString("CANALING"));
    		r.put("CONDUCTO"          , rs.getString("CONDUCTO"));
    		r.put("PTCUMUPR"          , rs.getString("PTCUMUPR"));
    		r.put("RESIDENCIA"        , rs.getString("RESIDENCIA"));
    		r.put("cdestciv",           rs.getString("cdestciv"));
    		r.put("ocup",               rs.getString("dsocupacion"));
    		r.put("numsoc",             rs.getString("numsoc"));
    		r.put("clvfam",             rs.getString("clvfam"));
    		return r;
    	}
    }
	/*//////////////////////////////////////////*/
	////// obtiene tablas de catalogos ///////////
	//////////////////////////////////////////////
    
    ///////////////////////////////
    ////// obtiene   mpoliza //////
    /*///////////////////////////*/
    protected class ObtenerPolizaCompleta extends CustomStoredProcedure
    {
    	protected ObtenerPolizaCompleta(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES.P_GET_INFO_MPOLIZAS");
    		declareParameter(new SqlParameter("pv_cdunieco",    OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo",      OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_estado",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari",    OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerPolizaCompletaMapper()));
    		declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}

    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }

    protected class ObtenerPolizaCompletaMapper implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Map<String,Object> m=new HashMap<String,Object>(0);
    		String columnas[]=new String[]{"status","swestado","nmsolici","feautori","cdmotanu","feanulac",
    				"swautori","cdmoneda","feinisus","fefinsus",
    	            "ottempot","feefecto","hhefecto","feproren","fevencim","nmrenova","ferecibo","feultsin","nmnumsin","cdtipcoa",
    	            "swtarifi","swabrido","feemisio","cdperpag","nmpoliex","nmcuadro","porredau","swconsol","nmpolant","nmpolnva",
    	            "fesolici","cdramant","cdmejred","nmpoldoc","nmpoliza2","nmrenove","nmsuplee","ttipcamc","ttipcamv","swpatent",
    	            "cdagente"};
    		for(String columna:columnas)
    		{
    			if(columna.substring(0,2).equals("fe"))
    			{
    				m.put(columna,rs.getDate(columna));
    			}
    			else
    			{
    				m.put(columna,rs.getString(columna));
    			}
    		}
    		return m;
    	}
    }
    /*///////////////////////////*/
    ////// obtiene   mpoliza //////
    ///////////////////////////////
    
    ///////////////////////////////////
    ///// movimientos de valopol //////
    /*///////////////////////////////*/
    protected class PMovTvalopol extends CustomStoredProcedure {
    	
    	protected PMovTvalopol(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES.P_MOV_TVALOPOL");
    		
    		declareParameter(new SqlParameter("pv_cdunieco", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_nmsuplem", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_status", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor01", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor02", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor03", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor04", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor05", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor06", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor07", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor08", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor09", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor10", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor11", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor12", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor13", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor14", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor15", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor16", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor17", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor18", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor19", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor20", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor21", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor22", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor23", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor24", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor25", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor26", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor27", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor28", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor29", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor30", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor31", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor32", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor33", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor34", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor35", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor36", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor37", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor38", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor39", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor40", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor41", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor42", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor43", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor44", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor45", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor46", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor47", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor48", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor49", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otvalor50", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    /*///////////////////////////////*/
	///// movimientos de valopol //////
    ///////////////////////////////////
    
	///////////////////////////////////
	///// movimientos de valopol //////
	/*///////////////////////////////*/
	protected class PMovTvalogar extends CustomStoredProcedure {
	
		protected PMovTvalogar(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES.P_MOV_TVALOGAR");
	
			declareParameter(new SqlParameter("pv_cdunieco",  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza",  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsituac",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status",    OracleTypes.VARCHAR));
			for(int i=1;i<=640;i++)
			{
				declareParameter(new SqlParameter(
						new StringBuilder()
						    .append("pv_otvalor")
						    .append(StringUtils.leftPad(String.valueOf(i),3,"0"))
						    .toString()
						,OracleTypes.VARCHAR));
			}
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {	
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*///////////////////////////////*/
	///// movimientos de valopol //////
	///////////////////////////////////
	
	///////////////////////////////////
	///// movimientos de valopol //////
	/*///////////////////////////////*/
	protected class PMovTvaloper extends CustomStoredProcedure {
	
		protected PMovTvaloper(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_MOV_TVALOPER");
	
			declareParameter(new SqlParameter("pv_cdunieco",  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza",  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsituac",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit",    OracleTypes.VARCHAR));
			
			declareParameter(new SqlParameter("pv_otvalor01", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor12", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor13", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor14", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor18", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor19", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor21", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor23", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor24", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor25", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor27", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor28", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor29", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor30", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor31", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor32", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor33", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor34", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor35", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor36", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor37", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor38", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor39", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor40", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor41", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor42", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor43", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor44", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor45", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor46", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor47", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor48", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor49", OracleTypes.VARCHAR));
			
			declareParameter(new SqlParameter("pv_otvalor50", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor51", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor52", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor53", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor54", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor55", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor56", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor57", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor58", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor59", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor60", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor61", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor62", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor63", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor64", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor65", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor66", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor67", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor68", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor69", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor70", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor71", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor72", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor73", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor74", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor75", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor76", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor77", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor78", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor79", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor80", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor81", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor82", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor83", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor84", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor85", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor86", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor87", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor88", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor89", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor90", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor91", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor92", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor93", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor94", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor95", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor96", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor97", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor98", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor99", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
	
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*///////////////////////////////*/
	///// movimientos de valopol //////
	///////////////////////////////////
    
	///////////////////////////////
	////// obtiene   valopol //////
	/*///////////////////////////*/
	protected class PGetTvalopol extends CustomStoredProcedure
	{
		protected PGetTvalopol(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GET_TVALOPOL");
			declareParameter(new SqlParameter("pv_cdunieco",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo",      OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_record_o",   OracleTypes.CURSOR, new PGetTvalopolMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_record_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class PGetTvalopolMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,Object> m=new HashMap<String,Object>(0);
			String columnas[]=new String[]{
					"cdunieco","cdramo","estado","nmpoliza","nmsuplem","status",
					"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09","otvalor10",
					"otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19","otvalor20",
					"otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29","otvalor30",
					"otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39","otvalor40",
					"otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49","otvalor50"
			};
			for(String columna:columnas)
			{
				m.put(columna,rs.getString(columna));
			}
			return m;
		}
	}
	/*///////////////////////////*/
	////// obtiene   valopol //////
	///////////////////////////////
	
	///////////////////////////////
	////// obtiene   valogar //////
	/*///////////////////////////*/
	protected class PGetTvalogar extends CustomStoredProcedure
	{
		protected PGetTvalogar(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GET_TVALOGAR");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_record_o",   OracleTypes.CURSOR, new PGetTvalogarMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_record_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class PGetTvalogarMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,Object> m=new HashMap<String,Object>(0);
			List<String> columnas=new ArrayList<String>();
			columnas.add("cdunieco");
			columnas.add("cdramo");
			columnas.add("estado");
			columnas.add("nmpoliza");
			columnas.add("nmsituac");
			columnas.add("cdgarant");
			columnas.add("nmsuplem");
			columnas.add("status");
			for(int i=1;i<=640;i++)
			{
			    columnas.add(
			    		new StringBuilder()
			    		.append("otvalor")
			    		.append(StringUtils.leftPad(String.valueOf(i),3,"0"))
			    		.toString()
			    		);
			}
			for(String columna:columnas)
			{
				m.put(columna,rs.getString(columna));
			}
			return m;
		}
	}
	/*///////////////////////////*/
	////// obtiene   valogar //////
	///////////////////////////////
	
	///////////////////////////////
	////// obtiene   valoper //////
	/*///////////////////////////*/
	protected class PGetTvaloper extends CustomStoredProcedure
	{
		protected PGetTvaloper(DataSource dataSource)
		{
			/*
			 pv_cdunieco_i
             pv_cdramo_i
             pv_estado_i
             pv_nmpoliza_i
             pv_nmsituac_i
             pv_cdrol_i
             pv_cdperson_i
            */
			super(dataSource,"PKG_COTIZA.P_GET_TVALOPER");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i",    OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_status_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i",       OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_record_o",   OracleTypes.CURSOR, new PGetTvaloperMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_record_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class PGetTvaloperMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,Object> m=new HashMap<String,Object>(0);
			String columnas[]=new String[]{
					"cdunieco","cdramo","estado","nmpoliza","nmsituac","cdrol","cdperson","nmsuplem",//"status",
					"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09","otvalor10",
					"otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19","otvalor20",
					"otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29","otvalor30",
					"otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39","otvalor40",
					"otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49","otvalor50"
			};
			for(String columna:columnas)
			{
				m.put(columna,rs.getString(columna));
			}
			return m;
		}
	}
	/*///////////////////////////*/
	////// obtiene   valoper //////
	///////////////////////////////
	
	////////////////////////////
	////// genera mperson //////
	/*////////////////////////*/
	protected class GeneraMperson extends CustomStoredProcedure {

		protected GeneraMperson(DataSource dataSource) {
			super(dataSource, "PKG_COTIZA.P_GET_CDPERSON");
			declareParameter(new SqlOutParameter("pv_cdperson_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			String numeroPoliza = null;
			if(map.get("pv_cdperson_o") != null) numeroPoliza = map.get("pv_cdperson_o").toString();
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("CDPERSON", numeroPoliza);

			return wrapperResultados;
		}
	}
	/*////////////////////////*/
	////// genera mperson //////
	////////////////////////////
	
	////////////////////////////
	////// p mov mpersona //////
	/*////////////////////////*/
	protected class PMovMpersona extends CustomStoredProcedure {
    	
    	protected PMovMpersona(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES2.P_MOV_MPERSONA");
    		declareParameter(new SqlParameter("pv_cdperson_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipide_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdideper_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnombre_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipper_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otfisjur_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otsexo_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fenacimi_i"    , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdrfc_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsemail_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnombre1_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsapellido_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsapellido1_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feingreso_i"   , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdnacion_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_canaling_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_conducto_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptcumupr_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_residencia_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nongrata_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdideext_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdestciv_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucemi_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuario_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsocupacion_i"    , OracleTypes.VARCHAR));//�� 
			declareParameter(new SqlParameter("pv_accion_i"      , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
	/*////////////////////////*/
	////// p mov mpersona //////
	////////////////////////////
	
	////////////////////////////
	////// p mov mpersona //////
	/*////////////////////////*/
	protected class PMovMpoliper extends CustomStoredProcedure {
	
		protected PMovMpoliper(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES.P_MOV_MPOLIPER");
			declareParameter(new SqlParameter("pv_cdunieco_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdunieco%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_cdramo_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdramo%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_estado_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.estado%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmpoliza_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmpoliza%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmsituac_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmsituac%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_cdrol_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdrol%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_cdperson_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdperson%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmsuplem_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmsuplem%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_status_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.status%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmorddom_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmorddom%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_swreclam_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.swreclam%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_accion_i", 		OracleTypes.VARCHAR));//
			declareParameter(new SqlParameter("pv_swexiper_i", 		OracleTypes.VARCHAR));//
	
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			
			compile();
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*////////////////////////*/
	////// p mov mpersona //////
	////////////////////////////
	
	////////////////////////////
	////// borra mpoliper //////
	/*////////////////////////*/
	protected class PBorraMpoliper extends CustomStoredProcedure {
		
		protected PBorraMpoliper(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES.P_BORRA_MPOLIPER");
			declareParameter(new SqlParameter("pv_cdunieco_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdunieco%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_cdramo_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdramo%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_estado_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.estado%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmpoliza_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmpoliza%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmsituac_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmsituac%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_cdrol_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdrol%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_cdperson_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.cdperson%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmsuplem_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmsuplem%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_status_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.status%TYPE   DEFAULT NULL,
			declareParameter(new SqlParameter("pv_nmorddom_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.nmorddom%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_swreclam_i", 		OracleTypes.VARCHAR));// IN  MPOLIPER.swreclam%TYPE DEFAULT NULL,
			declareParameter(new SqlParameter("pv_accion_i", 		OracleTypes.VARCHAR));//
	
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			
			compile();
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*////////////////////////*/
	////// borra mpoliper //////
	////////////////////////////
	
	///////////////////////////////
	////// obtiene   mpoliza //////
	/*///////////////////////////*/
	protected class ObtenerCoberturasUsuario extends CustomStoredProcedure
	{
		protected ObtenerCoberturasUsuario(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GET_COBERTURAS");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerCoberturasUsuarioMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerCoberturasUsuarioMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,String> map=new HashMap<String,String>(0);
			String columnas[]=new String[]{"GARANTIA","NOMBRE_GARANTIA","SWOBLIGA","SUMA_ASEGURADA","CDCAPITA",
					"status","cdtipbca","ptvalbas","swmanual","swreas","cdagrupa",
					"ptreduci","fereduci","swrevalo","orden","nmsituac", "cdtipsit"};
			for(String columna:columnas)
			{
				String string=rs.getString(columna);
				if(columna.equals("fereduci")&&string!=null&&string.length()>10)
				{
					// 1 9 9 0 - 1 2 - 0 1 _ 0 0 : 0 0 : 0 0 . 0 <<CADENA
    				//0 1 2 3 4 5 6 7 8 9 k <<INDICE
    				String aux="";
    				aux+=string.substring(8,10);
    				aux+="/";
    				aux+=string.substring(5,7);
    				aux+="/";
    				aux+=string.substring(0,4);
    				string=aux;
				}
				map.put(columna,string);
			}
			return map;
		}
	}
	/*///////////////////////////*/
	////// obtiene   mpoliza //////
	///////////////////////////////
	
	////////////////////////////
	////// p mov mpoligar //////
	/*////////////////////////*/
	protected class PMovMpoligar extends CustomStoredProcedure {
	
		protected PMovMpoligar(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES.P_MOV_MPOLIGAR");
			declareParameter(new SqlParameter("pv_cdunieco_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcapita_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipbca_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptvalbas_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swmanual_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swreas_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagrupa_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_ACCION", 	        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsup_i", 	    OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			
			compile();
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*////////////////////////*/
	////// p mov mpoligar //////
	////////////////////////////
	
	////////////////////////////
	////// p mov mpolicap //////
	/*////////////////////////*/
	protected class PMovMpolicap extends CustomStoredProcedure {
	
		protected PMovMpolicap(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES.P_MOV_MPOLICAP");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcapita_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptcapita_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptreduci_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fereduci_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swrevalo_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagrupa_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", 		OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*////////////////////////*/
	////// p mov mpolicap //////
	////////////////////////////
	
	////////////////////////////
	////// p mov mdomicil //////
	/*////////////////////////*/
	protected class PMovMdomicil extends CustomStoredProcedure {
	
		protected PMovMdomicil(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_MOV_MDOMICIL");
			/*
			pv_cdperson_i smap1.pv_cdperson
			pv_nmorddom_i smap1.NMORDDOM
			pv_msdomici_i smap1.DSDOMICI
			pv_nmtelefo_i smap1.NMTELEFO
			pv_cdpostal_i smap1.CODPOSTAL
			pv_cdedo_i    smap1.CDEDO
			pv_cdmunici_i smap1.CDMUNICI
			pv_cdcoloni_i smap1.CDCOLONI
			pv_nmnumero_i smap1.NMNUMERO
			pv_nmnumint_i smap1.NMNUMINT
			pv_accion_i   #U
			pv_msg_id_o   -
			pv_title_o    -
			*/
			declareParameter(new SqlParameter("pv_cdperson_i", 		OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmorddom_i", 		OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_msdomici_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmtelefo_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpostal_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdedo_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmunici_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcoloni_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmnumero_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmnumint_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipdom_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuario_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swactivo_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", 		OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	/*////////////////////////*/
	////// p mov mdomicil //////
	////////////////////////////
	
	///////////////////////////////
	////// p mov mesacontrol //////
	/*///////////////////////////*
	protected class PMovMesacontrol extends CustomStoredProcedure {
		protected PMovMesacontrol(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_MOV_MESACONTROL");
			declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucadm_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucdoc_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ferecepc_i"   , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cdagente_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_referencia_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nombre_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_festatus_i"   , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_status_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsolici_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor01"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor12"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor13"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor14"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor18"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor19"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor21"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor23"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor24"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor25"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor27"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor28"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor29"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor30"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor31"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor32"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor33"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor34"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor35"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor36"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor37"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor38"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor39"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor40"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor41"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor42"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor43"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor44"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor45"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor46"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor47"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor48"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor49"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor50"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swimpres"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"        , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_tramite_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("ntramite", map.get("pv_tramite_o"));

			return wrapperResultados;
		}
	}
	*/
	
	protected class PMovTvalosin extends CustomStoredProcedure {
		protected PMovTvalosin(DataSource dataSource) {
			super(dataSource,"PKG_SINIESTRO.P_MOV_TVALOSIN");
			declareParameter(new SqlParameter("pv_cdunieco", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_aaapertu", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsinies", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feregist", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor01"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor12"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor13"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor14"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor18"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor19"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor21"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor23"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor24"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor25"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor27"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor28"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor29"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor30"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor31"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor32"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor33"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor34"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor35"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor36"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor37"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor38"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor39"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor40"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor41"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor42"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor43"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor44"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor45"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor46"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor47"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor48"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor49"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor50"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("ntramite", map.get("pv_tramite_o"));

			return wrapperResultados;
		}
	}
	/*///////////////////////////*/
	////// p mov mesacontrol //////
	///////////////////////////////
	
	////////////////////////////////////
	////// movimientos bitacobros //////
	/*////////////////////////////////*/
	protected class MovBitacobros extends CustomStoredProcedure {
		protected MovBitacobros(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES.P_MOV_TBITACOBROS2");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcodigo_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mensaje_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_usuario_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdurlws_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_metodows_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_xmlin_i"  , OracleTypes.CLOB));
			declareParameter(new SqlParameter("pv_cderrws_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	////////////////////////////////////
	////// movimientos bitacobros //////
	/*////////////////////////////////*/
	
	/////////////////////////////////
	////// p mov d mesacontrol //////
	/*/////////////////////////////*
	protected class PMovDmesacontrol extends CustomStoredProcedure {
		protected PMovDmesacontrol(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_MOV_DMESACONTROL");
			declareParameter(new SqlParameter("pv_ntramite_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feinicio_i", 		OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("pv_cdclausu_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_comments_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmotivo_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i", 		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swagente_i", 		OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", 	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", 		OracleTypes.VARCHAR));
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			//wrapperResultados.getItemMap().put("ntramite", map.get("pv_tramite_o"));

			return wrapperResultados;
		}
	}
	/*/////////////////////////////*/
	////// p mov d mesacontrol //////
	/////////////////////////////////
	
	////////////////////////////////////////////
	////// obtener detalles de cotizacion //////
	/*////////////////////////////////////////*/
	protected class ObtenerDetallesCotizacion extends CustomStoredProcedure
	{
		protected ObtenerDetallesCotizacion(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GET_DETALLE_COTI");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdplan_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperpag_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_record_o", OracleTypes.CURSOR, new ObtenerDetallesCotizacionMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_record_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerDetallesCotizacionMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,String> map=new HashMap<String,String>(0);
			String columnas[]=new String[]{"nmsituac","parentesco","orden","Codigo_Garantia","Nombre_garantia","cdtipcon","Importe"};
			for(String columna:columnas)
			{
				map.put(columna,rs.getString(columna));
			}
			return map;
		}
	}
	/*////////////////////////////////////////*/
	////// obtener detalles de cotizacion //////
	////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// obtiene domicilio            //////////
	/*//////////////////////////////////////////*/
	protected class ObtenerDomicilioGeneral extends CustomStoredProcedure
	{
		protected ObtenerDomicilioGeneral(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_MDOMICIL_ART140");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmorddom_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_record_o",    OracleTypes.CURSOR, new ObtenerDomicilioMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",    OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",     OracleTypes.VARCHAR));
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_record_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerDomicilio extends CustomStoredProcedure
	{
		protected ObtenerDomicilio(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GET_MDOMICIL");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmorddom_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_record_o",    OracleTypes.CURSOR, new ObtenerDomicilioMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",    OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",     OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_record_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerDomicilioMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String,String> map=new LinkedHashMap<String,String>(0);
			map.put("CDPERSON"  , rs.getString("CDPERSON"));
			map.put("NMORDDOM"  , rs.getString("NMORDDOM"));
			map.put("DSDOMICI"  , rs.getString("DSDOMICI"));
			map.put("NMTELEFO"  , rs.getString("NMTELEFO"));
			map.put("CODPOSTAL"  , rs.getString("CODPOSTAL"));
			map.put("CDEDO"     , rs.getString("CDEDO"));
			map.put("estado"    , rs.getString("estado"));
			map.put("CDMUNICI"  , rs.getString("CDMUNICI"));
			map.put("Municipio" , rs.getString("Municipio"));
			map.put("CDCOLONI"  , rs.getString("CDCOLONI"));
			map.put("NMNUMERO"  , rs.getString("NMNUMERO"));
			map.put("NMNUMINT"  , rs.getString("NMNUMINT"));
			map.put("CDTIPDOM"  , rs.getString("CDTIPDOM"));
			map.put("SWACTIVO"  , rs.getString("SWACTIVO"));
			map.put("CDUSRCRE"  , rs.getString("CDUSRCRE"));
			return map;
		}
	}
	/*//////////////////////////////////////////*/
	////// obtiene domicilio           ///////////
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// emitir                       //////////
	/*//////////////////////////////////////////*/
	protected class Emitir extends CustomStoredProcedure
	{
		protected Emitir(DataSource dataSource)
		{
			super(dataSource,"PKG_EMISION.P_PROCESO_EMISION_GENERAL");
			declareParameter(new SqlParameter("pv_cdusuari",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdunieco",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdelement",     OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdcia",         OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdplan",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperpag",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecha",         OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_ntramite",      OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmpoliza_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmpoliex_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmsuplem_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_esdxn_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_message",      OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdideper_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("nmpoliza", map.get("pv_nmpoliza_o"));
			wrapperResultados.getItemMap().put("nmpoliex", map.get("pv_nmpoliex_o"));
			wrapperResultados.getItemMap().put("nmsuplem", map.get("pv_nmsuplem_o"));
			wrapperResultados.getItemMap().put("esdxn", map.get("pv_esdxn_o"));
			wrapperResultados.getItemMap().put("CDIDEPER", map.get("pv_cdideper_o"));

			return wrapperResultados;
		}
	}
	/*//////////////////////////////////////////*/
	////// emitir                      ///////////
	//////////////////////////////////////////////
	
	/////////////////////////////
	////// guardar archivo //////
	/*/////////////////////////*/
	/*
	protected class GuardarArchivoPoliza extends CustomStoredProcedure
	{
	
		protected GuardarArchivoPoliza(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_DOCUMENTOS");
			declareParameter(new SqlParameter("pv_cdunieco_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feinici_i"   , OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_cddocume_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsdocume_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsolici_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipmov_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swvisible_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_codidocu_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdorddoc"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmoddoc"       , OracleTypes.VARCHAR));
	
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
	
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	*/
	/*/////////////////////////*/
	////// guardar archivo //////
	/////////////////////////////
	
	protected class GuardarArchivoPersona extends CustomStoredProcedure
	{
	
		protected GuardarArchivoPersona(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_DOCUMENTOS_PERSONA");
			declareParameter(new SqlParameter("cdperson"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cddocume"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsdocume"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("codidocu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feinici"   , OracleTypes.DATE));
	
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
	
			compile();
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
	}
	
	//////////////////////////////////////////////
	////// obtiene documentos de poliza //////////
	/*//////////////////////////////////////////*/
	protected class ObtenerDocumentosPoliza extends CustomStoredProcedure
	{
		protected ObtenerDocumentosPoliza(DataSource dataSource)
		{
			/*
			pv_cdunieco_i
			pv_cdramo_i
			pv_estado_i
			pv_nmpoliza_i
			pv_registro_o
			pv_msg_id_o
			pv_title_o
			 */
			
			super(dataSource,"PKG_CONSULTA_PRUEBA.P_Get_documentos_f");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsdocume_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_start_i", 	  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_limit_i", 	  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsuplem_i",    OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtenerDocumentosPolizaMapper()));
			declareParameter(new SqlOutParameter("pv_num_rec_o",  OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerDocumentosPolizaMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cols[] = new String[] { "nmsolici", "cddocume", "dsdocume",
					"feinici", "ntramite", "tipmov", "nmsuplem", "nsuplogi",
					"editable", "feinicio", "fefinal", "nmsituac", "nmcertif", "cdmoddoc", "total", "tramite_endoso"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col.substring(0, 2).equals("fe"))
				{
					String fecha=rs.getString(col);
					if(fecha!=null&&fecha.length()>10)
					{
						// 1 9 9 0 - 1 2 - 0 1 _ 0 0 : 0 0 : 0 0 . 0 <<CADENA
	    				//0 1 2 3 4 5 6 7 8 9 k <<INDICE
	    				String aux="";
	    				aux+=fecha.substring(8,10);
	    				aux+="/";
	    				aux+=fecha.substring(5,7);
	    				aux+="/";
	    				aux+=fecha.substring(0,4);
	    				fecha=aux;
					}
					map.put(col,fecha);
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}
			return map;
		}
	}
	/*//////////////////////////////////////////*/
    ////// obtiene documentos de poliza //////////
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// obtiene documentos de poliza //////////
	/*//////////////////////////////////////////*/
	/*
	protected class ObtenerListaDocPolizaNueva extends CustomStoredProcedure
	{
		protected ObtenerListaDocPolizaNueva(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_Imp_documentos");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtenerListDocPolizaNuevaMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerListDocPolizaNuevaMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"nmsolici","nmsituac","descripc","descripl"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				map.put(col,rs.getString(col));
			}
			return map;
		}
	}
	*/
	/*//////////////////////////////////////////*/
	////// obtiene documentos de poliza //////////
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// obtiene documentos de poliza //////////
	/*//////////////////////////////////////////*/
	protected class ObtenerTiposClausulasExclusion extends CustomStoredProcedure
	{
		protected ObtenerTiposClausulasExclusion(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_tipos_clausulas");
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerTiposClausulasExclusionMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerTiposClausulasExclusionMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"cdtipcla","dstipcla","swgrapol"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				map.put(col,rs.getString(col));
			}	
			return map;
		}
	}
	/*//////////////////////////////////////////*/
	////// obtiene documentos de poliza //////////
	//////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////
	////// obtiene clausulas de exclusion por tipo //////////
	/*/////////////////////////////////////////////////////*/
	protected class ObtenerExclusionesXTipo extends CustomStoredProcedure
	{
		protected ObtenerExclusionesXTipo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_clausulas_x_tipo");
			declareParameter(new SqlParameter("pv_cdtipcla_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_descrip_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerExclusionesXTipoMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerExclusionesXTipoMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"cdclausu","dsclausu"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				map.put(col,rs.getString(col));
			}	
			return map;
		}
	}
	/*/////////////////////////////////////////////////////*/
	////// obtiene clausulas de exclusion por tipo //////////
	/////////////////////////////////////////////////////////
	
	///////////////////////////////////
	////// obtener html clausula //////
	/*///////////////////////////////*/
	protected class ObtenerHtmlClausula extends CustomStoredProcedure {

		protected ObtenerHtmlClausula(DataSource dataSource) {
			super(dataSource, "PKG_CONSULTA.P_texto_clausula");

			declareParameter(new SqlParameter("pv_cdclausu_i", OracleTypes.VARCHAR));	
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerHtmlClausulaMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
			compile();
		}
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	protected class ObtenerHtmlClausulaMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"cdclausu","dsclausu","dslinea"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				map.put(col,rs.getString(col));
			}	
			return map;
		}
	}
	/*///////////////////////////////*/
	////// obtener html clausula //////
	///////////////////////////////////

	
	/////////////////////////////////////
	////// obtener mesa de control //////
	/*/////////////////////////////////*/
	protected class ObtenerMesaControl extends CustomStoredProcedure
	{
		protected ObtenerMesaControl(DataSource dataSource)
		{
//			super(dataSource,"PKG_SATELITES2.P_OBTIENE_MESACONTROL");
			super(dataSource,"P_OBTIENE_MESACONTROL1");
			declareParameter(new SqlParameter("pv_cdunieco_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fedesde_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fehasta_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_contrarecibo_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoPago_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpresta_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("lote"              , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipolote"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoimpr"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari_busq"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dscontra"          , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o"  , OracleTypes.CURSOR, new ObtenerMesaControlMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerMesaControlMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{
					"ntramite"     , "cdunieco" , "cdramo"   , "dsramo"        , "estado"     , "nmpoliza"
					,"nmsolici"    , "cdsucadm" , "dssucadm" , "cdsucdoc"      , "dssucdoc"   , "cdsubram"
					,"cdtiptra"    , "ferecepc" , "cdagente" , "Nombre_agente" , "referencia" , "nombre"
					,"fecstatu"    , "status"   , "comments" , "cdtipsit"      , "comi"       , "prima_neta"
					,"prima_total" , "nmsuplem"
					,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09","otvalor10"
					,"otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19","otvalor20"
					,"otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29","otvalor30"
					,"otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39","otvalor40"
					,"otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49","otvalor50"
					,"contratante" , "cdusuari"
					};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col!=null&&col.substring(0,2).equals("fe"))
				{
					map.put(col,Utils.formateaFecha(rs.getString(col)));
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}	
			return map;
		}
	}
	/*/////////////////////////////////*/
	////// obtener mesa de control //////
	/////////////////////////////////////
	
	/////////////////////////////////////
	////// obtener mesa de control //////
	/*/////////////////////////////////*/
	protected class ObtenerMesaControlUsuario extends CustomStoredProcedure
	{
		protected ObtenerMesaControlUsuario(DataSource dataSource)
		{
			super(dataSource,"PKG_PRESINIESTRO.P_GET_MESACONTROL_USUARIO2");
			declareParameter(new SqlParameter("pv_cdunieco_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fedesde_i",       OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fehasta_i",       OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i",         OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtiptra_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_contrarecibo_i",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tipoPago_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nfactura_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpresta_i",      OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerMesaControlMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	////// obtener detalles de tramite(s) para mesa de control //////
	/*/////////////////////////////////////////////////////////////*/
	protected class ObtenerDetalleMesaControl extends CustomStoredProcedure
	{
		protected ObtenerDetalleMesaControl(DataSource dataSource)
		{
			super(dataSource,"P_CONS_DET_MESACTRL");
			declareParameter(new SqlParameter("pv_ntramite_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerDetalleMesaControlMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerDetalleMesaControlMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{
					"NTRAMITE"      , "NMORDINA"     , "CDTIPTRA"
					,"CDCLAUSU"     , "FECHAINI"     , "FECHAFIN"
					,"COMMENTS"     , "CDUSUARI_INI" , "CDUSUARI_FIN"
					,"usuario_ini"  , "usuario_fin"  , "cdmotivo"
					,"SWAGENTE"
					,"STATUS"        , "DSSTATUS"
					,"CDSISROL_INI"  , "DSSISROL_INI"
					,"CDSISROL_FIN"  , "DSSISROL_FIN"
					,"DSUSUARI_INI"  , "DSUSUARI_FIN"
					,"CDUSUARI_DEST" , "CDSISROL_DEST"
					,"DSUSUARI_DEST" , "DSSISROL_DEST"
					};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe"))
				{
					map.put(col,Utils.formateaFechaConHora(rs.getString(col)));
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}	
			return map;
		}
	}
	/*/////////////////////////////////////////////////////////////*/
	////// obtener detalles de tramite(s) para mesa de control //////
	/////////////////////////////////////////////////////////////////
	
	protected class ObtenDatosRecibos extends CustomStoredProcedure {
		
		protected ObtenDatosRecibos(DataSource dataSource) {
			super(dataSource, "pkg_consulta.P_cons_recibo_pol");

			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));			
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosRecibosMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}
	}

	
    protected class DatosRecibosMapper  implements RowMapper {
    
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ReciboWrapper recVO = new ReciboWrapper();
        	Recibo recibo = new Recibo();	    
        	
        	Calendar cal;
        	
        	recibo.setActRec(rs.getInt("actRec"));
        	recibo.setBonific(rs.getDouble("bonific"));
        	recibo.setComPri(rs.getDouble("comPri"));
        	recibo.setComRec(rs.getDouble("comRec"));
        	recibo.setDerecho(rs.getDouble("derecho"));
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("fecEmi"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		recibo.setFecEmi(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecEmi !!! " + rs.getString("fecEmi"));
        	}
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("fecIni"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		recibo.setFecIni(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecIni !!! " + rs.getString("fecIni"));
        	}
        
        	cal = Utils.getCalendarTimeZone0(rs.getString("fecPag"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		recibo.setFecPag(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecPag se envia 01/01/1900 !!! " + rs.getString("fecPag"));
        		recibo.setFecPag(Utils.getCalendarTimeZone0("01/01/1900", Constantes.FORMATO_FECHA));
        	}
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("fecSta"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		recibo.setFecSta(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecSta !!! " + rs.getString("fecSta"));
        	}
	        
        	cal = Utils.getCalendarTimeZone0(rs.getString("fecTer"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		recibo.setFecTer(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecTer !!! " + rs.getString("fecTer"));
        	}
	        	
	        	
        	recibo.setIva(rs.getDouble("iva"));
        	//logger.debug(">>>>>  Valor de iva: " +rs.getDouble("iva"));
        	
        	recibo.setNumAgt(rs.getInt("numAgt"));
        	recibo.setNumCli(rs.getInt("numCli"));
        	recibo.setNumEnd(rs.getInt("numEnd"));
        	//logger.debug(">>>>>  Valor de numEnd: " +rs.getInt("numEnd"));
        	
        	recibo.setNumMon(rs.getInt("numMon"));
        	recibo.setNumPol(rs.getInt("numPol"));
        	recibo.setNumRam(rs.getInt("numRam"));
        	recibo.setNumRec(rs.getInt("numRec"));
        	recibo.setNumSuc(rs.getInt("numSuc"));
        	recibo.setPriCom(rs.getDouble("priCom"));
        	recibo.setPriDot(rs.getDouble("priDot"));
        	recibo.setPrima(rs.getDouble("prima"));
        	//logger.debug(">>>>>  Valor de prima: " +rs.getDouble("prima"));
        	
        	recibo.setRecargo(rs.getDouble("recargo"));
        	recibo.setRmdbRn(rs.getInt("rmdbRn"));
        	recibo.setSaldo(rs.getDouble("saldo"));
        	recibo.setStatusr(rs.getString("statusr"));
        	
        	String tipoEndoso = rs.getString("tipEnd");
        	//logger.debug(">>>>>  Valor de tipEnd: " +rs.getString("tipEnd"));
        	
        	if(tipoEndoso == null)tipoEndoso = " ";
        	recibo.setTipEnd(tipoEndoso);
        	
        	recibo.setTipRec(rs.getString("tipRec"));
        	recibo.setTotRec(rs.getInt("totRec"));
        	
        	recVO.setRecibo(recibo);
        	recVO.setOperacion(rs.getString("TIPOPER"));
        	
        	String guardaDocumento =  rs.getString("GENERA_LIGA");
        	recVO.setGuardarDocumento((StringUtils.isNotBlank(guardaDocumento) &&  Constantes.SI.equalsIgnoreCase(guardaDocumento))?true:false);
        	
        	return recVO;
        }
    }

    protected class ObtenDatosComisiones extends CustomStoredProcedure {
    	
    	protected ObtenDatosComisiones(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_WS_COMISIONES_X_AGENTE");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosComisionesMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    protected class DatosComisionesMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Ccomision comision = new Ccomision();
    		Calendar cal;
    		
    		comision.setComDpri(rs.getDouble("comdpri"));
    		comision.setComDrec(rs.getDouble("comdrec"));
    		comision.setComVpri(rs.getDouble("comvpri"));
    		comision.setComVrec(rs.getDouble("comvrec"));
    		comision.setNumAgtc(rs.getInt("numAgtc"));
    		comision.setNumEndc(rs.getInt("numEndc"));
    		comision.setNumPolc(rs.getInt("numPolc"));
    		comision.setNumRamc(rs.getInt("numRamc"));
    		comision.setNumRecc(rs.getInt("numRecc"));
    		comision.setNumSucc(rs.getInt("numSucc"));
    		comision.setRmdbRn(rs.getInt("rmdbRn"));
    		
    		comision.setTipEndc(StringUtils.isBlank(rs.getString("tipEndc"))?" ":rs.getString("tipEndc"));
    		
    		return comision;
    	}
    }

    protected class ObtenDatosRecibosDxN extends CustomStoredProcedure {
    	
    	protected ObtenDatosRecibosDxN(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_CONS_DXN_RECIBOS");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosRecibosDxNMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    protected class DatosRecibosDxNMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DatosRecibosDxNVO datos =  new DatosRecibosDxNVO();
    		datos.setAdministradoraEmp(rs.getString("administradoraEmp"));
    		datos.setClaveEmp(rs.getString("claveEmp"));
    		datos.setCurpEmp(rs.getString("curpEmp"));
    		datos.setDepartamentoEmp(rs.getString("departamentoEmp"));
    		datos.setMaternoEmp(rs.getString("maternoEmp"));
    		datos.setNombreEmp(rs.getString("nombreEmp"));
    		datos.setPaternoEmp(rs.getString("paternoEmp"));
    		datos.setRetenedoraEmp(rs.getString("retenedoraEmp"));
    		datos.setRfcEmp(rs.getString("rfcEmp"));
    		datos.setClaveDescuento(rs.getString("claveDescuento"));
    		datos.setImpCob(rs.getString("impCob"));
    		datos.setNumPag(rs.getString("numPag"));
    		datos.setNumRel(rs.getString("numRel"));
    		datos.setNumeroAgente(rs.getString("numeroAgente"));
    		datos.setNumeroApoderado(rs.getString("numeroApoderado"));
    		datos.setPolizaEmi(rs.getString("polizaEmi"));
    		datos.setRamoEmi(rs.getString("ramoEmi"));
    		datos.setRenovacionAutomatica(rs.getString("renovacionAutomatica"));
    		datos.setSucursalEmi(rs.getString("sucursalEmi"));
    		
    		return datos;
    	}
    }

    protected class GuardaPeriodosDxN extends CustomStoredProcedure {
    	
    	protected GuardaPeriodosDxN(DataSource dataSource) {
    		super(dataSource, "PKG_SATELITES.P_INSERTA_CALENDARIO_DXN");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlParameter("pi_ADMINISTRADORA", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pi_ANIO", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pi_ESTATUS", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("Pi_FECHACORTE", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pi_FECHAEMISION", OracleTypes.VARCHAR));	
    		declareParameter(new SqlParameter("pi_FECHASTATUS", OracleTypes.VARCHAR));	
    		declareParameter(new SqlParameter("pi_FECHAINICIO", OracleTypes.VARCHAR));	
    		declareParameter(new SqlParameter("pi_FECHATERMINO", OracleTypes.VARCHAR));	
    		declareParameter(new SqlParameter("pi_HORAEMISION", OracleTypes.VARCHAR));	
    		declareParameter(new SqlParameter("pi_PERIODO", OracleTypes.VARCHAR));	
    		declareParameter(new SqlParameter("pi_RETENEDORA", OracleTypes.VARCHAR));	
    		
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("Po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }

    protected class LanzaProcesoDxN extends CustomStoredProcedure {
    	
    	protected LanzaProcesoDxN(DataSource dataSource) {
    		super(dataSource, "P_CALC_RECIBOS_DXN");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }
    
    protected class ObtenCatalogoColonias extends CustomStoredProcedure {
    	
    	protected ObtenCatalogoColonias(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_GET_COLONIAS");
    		
    		declareParameter(new SqlParameter("pv_codpostal_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ColoniasMapper()));
    		declareParameter(new SqlOutParameter("pv_messages_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    protected class ColoniasMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		GenericVO base = new GenericVO();	    
    		base.setKey(rs.getString("CODIGO"));
    		base.setValue(rs.getString("NOMBRE"));
    		return base;
    	}
    }

    
    protected class ObtenDatosCliente extends CustomStoredProcedure {
    	
    	protected ObtenDatosCliente(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_cons_sal_cli");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ClienteMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    protected class ClienteMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ClienteSalud cliente = new ClienteSalud();
    		//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT); 
    		SimpleDateFormat spdf = new SimpleDateFormat("dd/MM/yyyy");
        	Calendar cal;
    		
    		cliente.setAgrupaCli(rs.getInt("agrupaCli"));
    		cliente.setApellidomCli(rs.getString("apellidomCli"));
    		cliente.setApellidopCli(rs.getString("apellidopCli"));
    		cliente.setBannomCli(rs.getString("bannomCli"));
    		cliente.setCallecarCli(rs.getString("callecarCli"));
    		cliente.setCalleCli(rs.getString("calleCli"));
    		cliente.setCanconCli(rs.getInt("canconCli"));
    		cliente.setCelularCli(rs.getString("celularCli"));
    		cliente.setCheqdevCli(rs.getInt("cheqdevCli"));
    		cliente.setClaveCli(rs.getInt("claveCli"));
    		cliente.setCodcarCli(rs.getInt("codcarCli"));
    		cliente.setCodposCli(rs.getInt("codposCli"));
    		cliente.setColcarCli(rs.getString("colcarCli"));
    		cliente.setColoniaCli(rs.getString("coloniaCli"));
    		cliente.setEdocarCli(rs.getInt("edocarCli"));
    		cliente.setEdocivilCli(rs.getInt("edocivilCli"));
    		cliente.setEstadoCli(rs.getInt("estadoCli"));
    		cliente.setFaxCli(rs.getString("faxCli"));

    		
    		cal = Utils.getCalendarTimeZone0(rs.getString("fecaltaCli"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		cliente.setFecaltaCli(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecaltaCli !!! " + rs.getString("fecaltaCli"));
        	}
        	
        	cal = Utils.getCalendarTimeZone0(rs.getString("fecnacCli"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		cliente.setFecnacCli(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecnacCli !!! " + rs.getString("fecnacCli"));
        	}
        	
    		cliente.setFismorCli(rs.getInt("fismorCli"));
    		cliente.setGiroCli(rs.getInt("giroCli"));
    		
    		logger.debug("cliente.getGiroCli(): "+cliente.getGiroCli());
    		
    		cliente.setMuncarCli(rs.getString("muncarCli"));
    		cliente.setMunicipioCli(rs.getString("municipioCli"));
    		cliente.setNombreCli(rs.getString("nombreCli"));
    		cliente.setNumcarCli(rs.getString("numcarCli"));
    		cliente.setNumeroCli(rs.getString("numeroCli"));
    		cliente.setPobcarCli(rs.getString("pobcarCli"));
    		cliente.setPoblacionCli(rs.getString("poblacionCli"));
    		cliente.setRfcCli(rs.getString("rfcCli"));
    		cliente.setRmdbRn(rs.getInt("rmdbRn"));
    		cliente.setSexoCli(rs.getInt("sexoCli"));
    		cliente.setSinocurCli(rs.getInt("sinocurCli"));
    		cliente.setSucursalCli(rs.getInt("sucursalCli"));
    		cliente.setTelefonoCli(rs.getString("telefonoCli"));
    		cliente.setTituloCli(rs.getString("tituloCli"));
    		cliente.setTitulonobCli(rs.getString("titulonobCli"));
    		
    		return cliente;
    	}
    }

    
    protected class ObtenDatosClienteGeneralXcdperson extends CustomStoredProcedure {
    	
    	protected ObtenDatosClienteGeneralXcdperson(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_WS_CLIENTES_ART140");
    		
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_compania_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ClienteGeneralMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }

    protected class ObtenDatosDomicilioGeneralXcdperson extends CustomStoredProcedure {
    	
    	protected ObtenDatosDomicilioGeneralXcdperson(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_WS_CLIENTES_ART140DOMICI");
    		
    		declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_compania_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmorddom_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DireccionClienteGeneralMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class ObtenDatosClienteGeneral extends CustomStoredProcedure {
    	
    	protected ObtenDatosClienteGeneral(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_cons_sal_cli");
    		
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));			
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ClienteGeneralMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    protected class ClienteGeneralMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ClienteGeneral cliente = new ClienteGeneral();
    		//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT); 
    		SimpleDateFormat spdf = new SimpleDateFormat("dd/MM/yyyy");
    		Calendar cal;
    		
//    		String llaveCli = rs.getString("llaveCli"); 
//    		int claveCli = 0;
//    		
//    		if(StringUtils.isNotBlank(llaveCli)){
//    			claveCli = Integer.parseInt(llaveCli.substring(llaveCli.length()-10));
//    		}
    		
    		cliente.setAgrupaCli(rs.getInt("agrupaCli"));
    		cliente.setApellidomCli(rs.getString("apellidomCli"));
    		cliente.setApellidopCli(rs.getString("apellidopCli"));
    		cliente.setBannomCli(rs.getString("bannomCli"));
    		cliente.setCallecarCli(rs.getString("callecarCli"));
    		cliente.setCalleCli(rs.getString("calleCli"));
    		cliente.setCanconCli(rs.getInt("canconCli"));
    		cliente.setCelularCli(rs.getString("celularCli"));
    		cliente.setCheqdevCli(rs.getInt("cheqdevCli"));
    		cliente.setClaveCli(rs.getInt("claveCli"));//cliente.setClaveCli(claveCli);//se usa claveCli para uso exclusivo de cdperson en productos de salud, una vez platicado con Argenis
    		cliente.setCodcarCli(rs.getInt("codcarCli"));
    		cliente.setCodposCli(rs.getString("codposCli"));
    		cliente.setColcarCli(rs.getString("colcarCli"));
    		cliente.setColoniaCli(rs.getString("coloniaCli"));
    		cliente.setEdocarCli(rs.getInt("edocarCli"));
    		cliente.setEdocivilCli(rs.getInt("edocivilCli"));
    		cliente.setEstadoCli(rs.getInt("estadoCli"));
    		cliente.setFaxCli(rs.getString("faxCli"));
    		
    		
    		cal = Utils.getCalendarTimeZone0(rs.getString("fecaltaCli"), Constantes.FORMATO_FECHA);
    		if(cal != null){
    			cliente.setFecaltaCli(cal);
    		}else{
    			logger.error("NO SE PUDO PARSEAR LA FECHA fecaltaCli !!! " + rs.getString("fecaltaCli"));
    		}
    		
    		cal = Utils.getCalendarTimeZone0(rs.getString("fecnacCli"), Constantes.FORMATO_FECHA);
    		if(cal != null){
    			cliente.setFecnacCli(cal);
    		}else{
    			logger.error("NO SE PUDO PARSEAR LA FECHA fecnacCli !!! " + rs.getString("fecnacCli"));
    		}
    		
    		cliente.setFismorCli(rs.getInt("fismorCli"));
    		cliente.setGiroCli(rs.getInt("giroCli"));
    		
    		logger.debug("cliente.getGiroCli(): "+cliente.getGiroCli());
    		
    		cliente.setMuncarCli(rs.getString("muncarCli"));
    		cliente.setMunicipioCli(rs.getString("municipioCli"));
    		cliente.setNombreCli(rs.getString("nombreCli"));
    		cliente.setNumcarCli(rs.getString("numcarCli"));
    		cliente.setNumeroCli(rs.getString("numeroCli"));
    		cliente.setPobcarCli(rs.getString("pobcarCli"));
    		cliente.setPoblacionCli(rs.getString("poblacionCli"));
    		cliente.setRfcCli(rs.getString("rfcCli"));
    		cliente.setRmdbRn(rs.getInt("rmdbRn"));
    		cliente.setSexoCli(rs.getInt("sexoCli"));
    		cliente.setSinocurCli(rs.getInt("sinocurCli"));
    		cliente.setSucursalCli(rs.getInt("sucursalCli"));
    		cliente.setTelefonoCli(rs.getString("telefonoCli"));
    		cliente.setFaxCli(rs.getString("telefonoCli2"));
    		cliente.setCelularCli(rs.getString("telefonoCli3"));
    		
    		
    		logger.debug("TElefono cli : " + rs.getString("telefonoCli"));
    		
    		cliente.setTituloCli(rs.getString("tituloCli"));
    		cliente.setTitulonobCli(rs.getString("titulonobCli"));
    		
    		
    		cliente.setNacCli(rs.getString("nacCli"));
    		cliente.setCveEle(rs.getString("cveEle"));
    		cliente.setCurpCli(rs.getString("curpCli"));
    		cliente.setMailCli(rs.getString("mailCli"));
    		
    		
    		logger.debug("MailCli: " + rs.getString("mailCli"));
    		cliente.setOrirecCli(rs.getString("orirecCli"));
    		cliente.setAdmconCli(rs.getString("admconCli"));
    		cliente.setApodeCli(rs.getString("apodeCli"));
    		cliente.setPasaporteCli(rs.getString("pasaporteCli"));
    		
    		try{
    			cliente.setUsucapCli(rs.getInt("usucapCli"));
    		}
    		catch(Exception e){
    			logger.error("No se pudo mapear el codigo de usuario captura, se envia 0 por default, claveCli: "+rs.getInt("claveCli"),e);
    		}
    		
    		try{
    			cliente.setUsuautCli(rs.getInt("usuautCli"));
			}
			catch(Exception e){
				logger.error("No se pudo mapear el codigo de usuario Autoriza actualiza, se envia 0 por default, claveCli: "+rs.getInt("claveCli"),e);
			}
    		
    		cliente.setStatusCli(rs.getString("STATUSCLI"));
    		
    		cal = Utils.getCalendarTimeZone0(rs.getString("fecstaCli"), Constantes.FORMATO_FECHA);
    		if(cal != null){
    			cliente.setFecstaCli(cal);
    		}else{
    			logger.error("NO SE PUDO PARSEAR LA FECHA fecstaCli !!! " + rs.getString("fecstaCli"));
    		}
    		
    		cliente.setOcuPro(rs.getInt("ocuPro"));
    		cliente.setRazSoc(rs.getString("razSoc"));
    		cliente.setRamoCli(rs.getInt("ramoCli"));
    		cliente.setNumeroExterno(rs.getString("llaveCli"));

    		cliente.setMpoSepomex(rs.getInt("MPOSEPOMEX"));
    		
    		return cliente;
    	}
    }

    protected class DireccionClienteGeneralMapper  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DirCli dirCliente = new DirCli();
    		SimpleDateFormat spdf = new SimpleDateFormat("dd/MM/yyyy");
    		Calendar cal;
    		
    		dirCliente.setCalCli(rs.getString("calleCli"));
    		dirCliente.setCodPos(Integer.parseInt(rs.getString("codposCli")));
    		dirCliente.setColonia(rs.getString("coloniaCli"));
//    		dirCliente.setCveCia(param);// falta fijar valor
    		
    		
    		dirCliente.setCveCli(Integer.parseInt(rs.getString("llaveCli").substring(rs.getString("llaveCli").length()-10)));
    		
    		
    		dirCliente.setCveEst(rs.getShort("estadoCli"));
    		//dirCliente.setCveMpo(param);// no es necesario llenar
    		dirCliente.setCveMpoSpm(rs.getShort("MPOSEPOMEX"));
  
    		cal = Utils.getCalendarTimeZone0(spdf.format(new Date()), Constantes.FORMATO_FECHA);
    		if(cal != null){
    			dirCliente.setFecAct(cal);
    		}else{
    			logger.error("NO SE PUDO PARSEAR LA FECHA FecAct !!! " + spdf.format(new Date()));
    		}
    		
    		cal = Utils.getCalendarTimeZone0(rs.getString("fecaltaCli"), Constantes.FORMATO_FECHA);
        	if(cal != null){
        		dirCliente.setFecAlt(cal);
        	}else{
        		logger.error("NO SE PUDO PARSEAR LA FECHA fecaltaCli !!! " + rs.getString("fecaltaCli"));
        	}
    		
    		
    		dirCliente.setNumCli(rs.getString("NUMEROEXT"));
//    		dirCliente.setNumDir(param);// falta fijar valor
    		dirCliente.setNumInt(rs.getString("NUMEROINT"));
    		dirCliente.setPoblaci(rs.getString("poblacionCli"));
    		dirCliente.setStaDir(StringUtils.isNotBlank(rs.getString("SWACTIVO")) && Constantes.NO.equalsIgnoreCase(rs.getString("SWACTIVO"))? "0":"1");
    		dirCliente.setTipDir(rs.getInt("CDTIPDOM"));
//    		dirCliente.setUsuCap(param);// falta fijar valor
    		
    		return dirCliente;
    	}
    }
    
    ///////////////////////////////////////////////////////////
    ////// actualizar solici de tarea de mesa de control //////
    /*///////////////////////////////////////////////////////*/
    protected class MesaControlUpdateSolici extends CustomStoredProcedure {
    	/*
	    PROCEDURE P_UPDATE_NMSOLICI(pv_ntramite_i IN TDOCUPOL.NTRAMITE%TYPE,
	    pv_nmsolici_i IN TDOCUPOL.NMSOLICI%TYPE,
	    pv_msg_id_o   OUT GB_MESSAGES.msg_id%TYPE,
	    pv_title_o 
        */
    	protected MesaControlUpdateSolici(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES.P_UPDATE_NMSOLICI");
    		
    		declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsolici_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    /*///////////////////////////////////////////////////////*/
	////// actualizar solici de tarea de mesa de control //////
    ///////////////////////////////////////////////////////////
    
    ///////////////////////////////////////////////////////////
    ////// actualizar status de tarea de mesa de control //////
    /*///////////////////////////////////////////////////////*/
    protected class MesaControlUpdateStatus extends CustomStoredProcedure {
    	/*
	    PROCEDURE P_UPDATE_NMSOLICI(pv_ntramite_i IN TDOCUPOL.NTRAMITE%TYPE,
	    pv_nmsolici_i IN TDOCUPOL.NMSOLICI%TYPE,
	    pv_msg_id_o   OUT GB_MESSAGES.msg_id%TYPE,
	    pv_title_o 
        */
    	protected MesaControlUpdateStatus(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES.P_UPDATE_STATUS_MC");
    		
    		declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    /*///////////////////////////////////////////////////////*/
	////// actualizar status de tarea de mesa de control //////
    ///////////////////////////////////////////////////////////
    
    ///////////////////////////////////////////////////////////
    ////// actualizar status de tarea de mesa de control //////
    /*///////////////////////////////////////////////////////*/
    protected class MesaControlFinalizarDetalle extends CustomStoredProcedure {
    	protected MesaControlFinalizarDetalle(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES2.P_UPDATE_TDMESA");
    		
    		declareParameter(new SqlParameter("pv_nmordina_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ntramite_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdusuari_fin_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdsisrol_fin_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_comments_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_fechafin_i",     OracleTypes.TIMESTAMP));
    		//declareParameter(new SqlParameter("pv_cdmotivo_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O",    OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O",     OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    /*///////////////////////////////////////////////////////*/
	////// actualizar status de tarea de mesa de control //////
    ///////////////////////////////////////////////////////////
    
    ///////////////////////////////////////////////////////////
    ////// insertar documento para imprimir contrarecibo //////
    /*///////////////////////////////////////////////////////*/
    protected class DocumentosPrepararContrarecibo extends CustomStoredProcedure {
    	protected DocumentosPrepararContrarecibo(DataSource dataSource) {
    		super(dataSource,"PKG_SATELITES.P_PREPARA_CONTRARECIBO");
    		
    		declareParameter(new SqlParameter("pv_cdconrec_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ntramite_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cddocume_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_dsdocume_i",     OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O",    OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O",     OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
    }
    /*///////////////////////////////////////////////////////*/
    //////insertar documento para imprimir contrarecibo //////
    ///////////////////////////////////////////////////////////
    
    
	/*//////////////////////////////////////////*/
	////// obtiene documentos de poliza //////////
	//////////////////////////////////////////////
	
	
	////////////////////////
	////// buscar rfc //////
	/*////////////////////*/
	public class BuscarRFC extends CustomStoredProcedure
	{
		protected BuscarRFC(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_VALIDA_RFC");
			declareParameter(new SqlParameter("pv_rfc_i"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new BuscarRFCMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class BuscarRFCMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{
					"RFCCLI","NOMBRECLI","FENACIMICLI",
					"DIRECCIONCLI","CLAVECLI","CDIDEPER", "CDIDEEXT",
					"SEXO", "TIPOPERSONA", "NACIONALIDAD",
					"NOMBRE", "SNOMBRE", "APPAT",
					"APMAT","CODPOSTAL","CDEDO","CDMUNICI","DSDOMICIL",
					"NMNUMERO","NMNUMINT", "NMORDDOM"
					};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe"))
				{
					map.put(col,Utils.formateaFecha(rs.getString(col)));
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}	
			return map;
		}
	}
	/*////////////////////*/
	////// buscar rfc //////
	////////////////////////
	
	/////////////////////////////
	////// borrar mpoliper //////
	/*/////////////////////////*/
	protected class BorrarMPoliper extends CustomStoredProcedure
	{
		protected BorrarMPoliper(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_BORRA_MPOLIPER");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	/*/////////////////////////*/
	////// borrar mpoliper //////
	/////////////////////////////
	
	//////////////////////////////////////
	////// obtener ramos x cdunieco //////
	/*//////////////////////////////////*/
	public class ObtenerRamos extends CustomStoredProcedure
	{
		protected ObtenerRamos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBT_RAMO_X_CDUNIECO");
			declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerRamosMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerRamosMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"cdramo","dsramo"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe"))
				{
					map.put(col,Utils.formateaFecha(rs.getString(col)));
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}	
			return map;
		}
	}
	/*//////////////////////////////////*/
	////// obtener ramos x cdunieco //////
	//////////////////////////////////////
	
	///////////////////////////////////
	////// obtener tipsit x ramo //////
	/*///////////////////////////////*/
	public class ObtenerTipsit extends CustomStoredProcedure
	{
		protected ObtenerTipsit(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBTIENE_SITUACION");
			declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtenerTipsitMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class ObtenerTipsitMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"CDTIPSIT","DSTIPSIT"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe"))
				{
					map.put(col,Utils.formateaFecha(rs.getString(col)));
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}	
			return map;
		}
	}
	/*///////////////////////////////*/
	////// obtener tipsit x ramo //////
	///////////////////////////////////
	
	//////////////////////////////
	////// ValidaTitularMenorEdad //////
	/*//////////////////////////*/
	public class ValidaTitularMenorEdad extends CustomStoredProcedure
	{
		protected ValidaTitularMenorEdad(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_VALIDA_TITULAR_MENOR");
			declareParameter(new SqlParameter("pv_cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_titular_menor_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			
			String existeMenor = null;
			if(map.get("pv_titular_menor_o") != null) existeMenor = map.get("pv_titular_menor_o").toString();
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("EXISTE_TITULAR_MENOR", existeMenor);

			return wrapperResultados;
		}
	}

	/*///////////////////////////////*/
	////// ValidaTitularMenorEdad //////
	///////////////////////////////////
	
	
	//////////////////////////////
	////// PValInfoPersonas //////
	/*//////////////////////////*/
	public class PValInfoPersonas extends CustomStoredProcedure
	{
		protected PValInfoPersonas(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_VAL_INFO_PERSONAS");
			declareParameter(new SqlParameter("pv_cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new PValInfoPersonasMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class PValInfoPersonasMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String cols[]=new String[]{"cdperson","nombre"};
			Map<String,String> map=new HashMap<String,String>(0);
			for(String col:cols)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe"))
				{
					map.put(col,Utils.formateaFecha(rs.getString(col)));
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}	
			return map;
		}
	}
	//////////////////////////////
	////// PValInfoPersonas //////
	/*//////////////////////////*/
	
    protected class ObtieneDatosPolizaAgente extends CustomStoredProcedure {
    	
    	protected ObtieneDatosPolizaAgente(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SATELITES.P_OBTIENE_MPOLIAGE_PORCENTAJES");
    		declareParameter(new SqlParameter("pi_CDUNIECO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ESTADO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMPOLIZA", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_rc_cursor", OracleTypes.CURSOR, new DatosPolizaAgente()));
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("po_rc_cursor");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class DatosPolizaAgente  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		AgentePolizaVO consulta = new AgentePolizaVO();
    		consulta.setCdagente(rs.getString("cdagente"));
    		consulta.setCdtipoAg(rs.getString("cdtipoag"));
    		consulta.setDescripl(rs.getString("descripl"));
    		consulta.setPorparti(rs.getString("porparti"));
    		consulta.setPorredau(rs.getString("porredau"));
    		consulta.setNmsuplem(rs.getString("nmsuplem"));
    		consulta.setNmcuadro(rs.getString("nmcuadro"));
    		consulta.setCdsucurs(rs.getString("cdsucurs"));
    		consulta.setComision(rs.getString("pocompro"));
    		consulta.setNvacomision(rs.getString("nvopccom"));
    		return consulta;
    	}
    }

    
    protected class ObtieneDatosPolizaAgentePorPuntos extends CustomStoredProcedure {
    	
    	protected ObtieneDatosPolizaAgentePorPuntos(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SATELITES.P_OBTIENE_MPOLIAGE_PORCENTAJES");
    		declareParameter(new SqlParameter("pi_CDUNIECO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ESTADO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMPOLIZA", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_rc_cursor", OracleTypes.CURSOR, new DatosPolizaAgentePorPuntos()));
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("po_rc_cursor");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class DatosPolizaAgentePorPuntos  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		AgentePolizaVO consulta = new AgentePolizaVO();
    		consulta.setCdagente(rs.getString("cdagente"));
    		consulta.setCdtipoAg(rs.getString("cdtipoag"));
    		consulta.setDescripl(rs.getString("descripl"));
    		consulta.setPorparti(rs.getString("porparti"));
    		
    		/**
    		 * Se convierte a puntos porcentuales
    		 */
    		String comision = rs.getString("pocompro");
    		String porcCom  = rs.getString("porredau");
    		
    		try{
    			logger.debug("<<<<<<>>>>>> Antes de convertir el valor porcentual a punto porcentual <<<<<<>>>>>>");
    			logger.debug("::::::Comision: " + comision);
    			logger.debug("::::::Porcentaje Cesion de Comision: " + porcCom);
    			
	    		if(StringUtils.isNotBlank(comision) && StringUtils.isNotBlank(porcCom)){
	    			float fComision = Float.parseFloat(comision);
	    			float fporcCom  = Float.parseFloat(porcCom);
	    			float fporcComPP = 0;
	    			
	    			fporcComPP = (fporcCom * fComision) / 100;
	    			porcCom = Float.toString((float)(Math.round(fporcComPP*100.0)/100.0));
	    			
	    			logger.debug("::::: Despues de convertir la cesion de comision a punto porcentual de la comision: " + porcCom);
	    		}
    		}catch (Exception e){
    			logger.debug("Error al convertir el valor porcentual de la cesion de comision a punto porcentual");
    		}
    		
    		consulta.setPorredau(porcCom);
    		consulta.setNmsuplem(rs.getString("nmsuplem"));
    		consulta.setNmcuadro(rs.getString("nmcuadro"));
    		consulta.setCdsucurs(rs.getString("cdsucurs"));
    		consulta.setComision(comision);
    		consulta.setNvacomision(rs.getString("nvopccom"));
    		return consulta;
    	}
    }
    
    
    
    protected class ObtieneDatosGeneralAgente extends CustomStoredProcedure {
    	
    	protected ObtieneDatosGeneralAgente(DataSource dataSource) {
    		
    		super(dataSource, "PKG_LISTAS.P_OBTIENE_TTIPOAGE");
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosGeneralAgente()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o ", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class DatosGeneralAgente  implements RowMapper {
    	
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		GenericVO consulta = new GenericVO();
    		consulta.setKey(rs.getString("codigo"));
    		consulta.setValue(rs.getString("descripl"));
    		return consulta;
    	}
    }
    
    protected class GuardarPorcentajePoliza extends CustomStoredProcedure {
    	
    	protected GuardarPorcentajePoliza(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SATELITES2.P_MOV_MPOLIAGE_PORCENTAJES");
    		declareParameter(new SqlParameter("pi_CDUNIECO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ESTADO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMPOLIZA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE_NVO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMSUPLEM", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDTIPOAG", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORREDAU", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMCUADRO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDSUCURS", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORPARTI", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NVOPCCOM", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}   	

    }

    protected class GuardarPorcentajePolizaPorPuntos extends CustomStoredProcedure {
    	
    	protected GuardarPorcentajePolizaPorPuntos(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SATELITES2.P_MOV_MPOLIAGE_PORCENTAJES");
    		declareParameter(new SqlParameter("pi_CDUNIECO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ESTADO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMPOLIZA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE_NVO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMSUPLEM", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDTIPOAG", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORREDAU", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMCUADRO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDSUCURS", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORPARTI", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NVOPCCOM", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}   	
    	
    }
    
    
    protected class GuardarEliminarPorcentajePoliza extends CustomStoredProcedure {
    	
    	protected GuardarEliminarPorcentajePoliza(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SATELITES2.P_MOV_MPOLIAGE_PORCENTAJES2");
    		declareParameter(new SqlParameter("pi_CDUNIECO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ESTADO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMPOLIZA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE_NVO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMSUPLEM", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDTIPOAG", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORREDAU", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMCUADRO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDSUCURS", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORPARTI", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NVOPCCOM", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ACCION", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}   	

    }

    protected class GuardarEliminarPorcentajePolizaPorPuntos extends CustomStoredProcedure {
    	
    	protected GuardarEliminarPorcentajePolizaPorPuntos(DataSource dataSource) {
    		
    		super(dataSource, "PKG_SATELITES2.P_MOV_MPOLIAGE_PORCENTAJES2");
    		declareParameter(new SqlParameter("pi_CDUNIECO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ESTADO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMPOLIZA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE_NVO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDAGENTE", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMSUPLEM", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDTIPOAG", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORREDAU", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NMCUADRO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_CDSUCURS", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_PORPARTI", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_NVOPCCOM", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_ACCION", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_CDERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_DSERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("po_TIPOERROR", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}   	
    	
    }
    
    
    protected class ValidarExtraprima extends CustomStoredProcedure {
    	
    	protected ValidarExtraprima(DataSource dataSource) {
    		super(dataSource, "pkg_satelites.valida_extraprima");
    		declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",OracleTypes.CURSOR, new GenericMapper(new String[]{"STATUS" , "ASEGURADO"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o ", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}

    }

    protected class ObtenCdtipsitGS extends CustomStoredProcedure {
    	
    	protected ObtenCdtipsitGS(DataSource dataSource) {
    		super(dataSource, "Pkg_Consulta.P_OBTIENE_SUBRAMO");
    		declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_cdsubram_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_cdtipsit_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o ", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados=mapper.build(map);
    		wrapperResultados.setItemMap(new HashMap<String, Object>());
    		wrapperResultados.getItemMap().put("cdtipsitGS", map.get("pv_cdsubram_o"));
    		wrapperResultados.getItemMap().put("cdtipsit", map.get("pv_cdtipsit_o"));
    		return wrapperResultados;
    	}   	
    	
    }

    protected class ObtenSubramoGS extends CustomStoredProcedure {
    	
    	protected ObtenSubramoGS(DataSource dataSource) {
    		super(dataSource, "Pkg_Consulta.P_OBTIENE_SUBRAMO_X_CDTIPSIT");
    		declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_cdsubram_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o ", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados=mapper.build(map);
    		wrapperResultados.setItemMap(new HashMap<String, Object>());
    		wrapperResultados.getItemMap().put("subramoGS", map.get("pv_cdsubram_o"));
    		return wrapperResultados;
    	}   	
    	
    }
    
    protected class ValidarExtraprimaSituac extends CustomStoredProcedure {
    	
    	protected ValidarExtraprimaSituac(DataSource dataSource) {
    		super(dataSource, "pkg_satelites.valida_extraprima_situac");
    		declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_status_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o ", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados=mapper.build(map);
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("status", map.get("pv_status_o"));
			return wrapperResultados;
		}   	

    }
    
    protected class ValidarExtraprimaSituacRead extends CustomStoredProcedure {
    	
    	protected ValidarExtraprimaSituacRead(DataSource dataSource) {
    		super(dataSource, "pkg_satelites.valida_extraprima_situac_read");
    		declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_status_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o ", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados=mapper.build(map);
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("status", map.get("pv_status_o"));
			return wrapperResultados;
		}   	

    }

    protected class HabilitaSigRecibo extends CustomStoredProcedure {
    	
    	protected HabilitaSigRecibo(DataSource dataSource) {
    		super(dataSource, "PKG_SATELITES.P_HABILITAR_SIG_RECSUB");
    		declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados=mapper.build(map);
    		return wrapperResultados;
    	}   	
    	
    }
    
    protected class ObtenerMesaControlSuper extends CustomStoredProcedure
	{
    	
    	String columnas[]=new String[]{
    			"NTRAMITE" 
    		    ,"CDUNIECO" 
    		    ,"CDRAMO" 
    		    ,"ESTADO" 
    		    ,"NMPOLIZA" 
    		    ,"NMSOLICI" 
    		    ,"CDSUCADM" 
    		    ,"CDSUCDOC" 
    		    ,"CDSUBRAM" 
    		    ,"CDTIPTRA" 
    		    ,"FERECEPC" 
    		    ,"CDAGENTE" 
    		    ,"NOMBRE_AGENTE" 
    		    ,"REFERENCIA" 
    		    ,"NOMBRE" 
    		    ,"FECSTATU" 
    		    ,"STATUS" 
    		    ,"COMMENTS" 
    		    ,"CDTIPSIT"
    		    };
    	
		protected ObtenerMesaControlSuper(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_OBTIENE_MESACONTROL_SUPER");
			declareParameter(new SqlParameter("pv_cdunieco_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i",        OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",      OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
		}
	
		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			List result = (List) map.get("pv_registro_o");
			wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
    
    
    protected class ValidaUsuarioSucursal extends CustomStoredProcedure {
    	
    	protected ValidaUsuarioSucursal(DataSource dataSource) {
    		super(dataSource, "PKG_SATELITES.P_VALIDA_USUARIO_SUCURSAL");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdusuari_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o",OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }   	
    }
    
    protected class ObtieneTatrisin extends CustomStoredProcedure
    {
    	protected ObtieneTatrisin(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_SINIESTRO");
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtieneTatrisinMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneTatrisinPoliza extends CustomStoredProcedure
    {
    	protected ObtieneTatrisinPoliza(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_SINIESTRO_POL");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o",   OracleTypes.CURSOR, new ObtieneTatrisinMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",     OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",      OracleTypes.VARCHAR));
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneTatrisinMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ComponenteVO result=new ComponenteVO();
        	result.setFlagEsAtribu(true);
            result.setType(ComponenteVO.TIPO_TATRISIN);
            result.setNameCdatribu(rs.getString("CDATRIBU"));
            result.setTipoCampo(rs.getString("SWFORMAT"));
            
            String sMinlen = rs.getString("NMLMIN");
            int minlen = -1;
            boolean fMinlen = false;
            if(StringUtils.isNotBlank(sMinlen))
            {
            	try
            	{
            		minlen=(Integer)Integer.parseInt(sMinlen);
            		fMinlen = true;
            	}
            	catch(Exception ex)
            	{
            		minlen=-1;
            		fMinlen=false;
            	}
            }
            result.setMinLength(minlen);
            result.setFlagMinLength(fMinlen);
            
            String sMaxlen = rs.getString("NMLMAX");
            int maxlen = -1;
            boolean fMaxlen = false;
            if(StringUtils.isNotBlank(sMaxlen))
            {
            	try
            	{
            		maxlen=(Integer)Integer.parseInt(sMaxlen);
            		fMaxlen = true;
            	}
            	catch(Exception ex)
            	{
            		maxlen=-1;
            		fMaxlen=false;
            	}
            }
            result.setMaxLength(maxlen);
            result.setFlagMaxLength(fMaxlen);
            
            String sObliga = rs.getString("SWOBLIGA");
            boolean isObliga = false;
            if(StringUtils.isNotBlank(sObliga)&&sObliga.equalsIgnoreCase(Constantes.SI))
            {
            	isObliga = true;
            }
            result.setObligatorio(isObliga);
            
            result.setLabel(rs.getString("DSATRIBU"));
            result.setCatalogo(rs.getString("OTTABVAL"));
            
            String sDepend = rs.getString("CDTABLJ1");
            boolean isDepend = false;
            if(StringUtils.isNotBlank(sDepend))
            {
            	isDepend = true;
            }
            result.setDependiente(isDepend);
            
            result.setSwsuscri(rs.getString("SWSUSCRI"));
            result.setSwtarifi(rs.getString("SWTARIFI"));
            result.setSwpresen(rs.getString("SWPRESEN"));
            result.setDefaultValue(rs.getString("VALOR"));
            
            return result;
        }
    }
    
    
    protected class ObtieneCobranzaAplicada extends CustomStoredProcedure
    {
    	protected ObtieneCobranzaAplicada(DataSource dataSource)
        {
            super(dataSource,"PKG_CONSULTA.P_GET_ZWORKREM");
            String[] cols=new String[]{
            		"CDUNIECO" , "CDRAMO" , "ESTADO" , "NMPOLIZA" , "PTIMPORT"
            };
            declareParameter(new SqlOutParameter("pv_record_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
    	{
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_record_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
    protected class ObtieneRemesaAplicada extends CustomStoredProcedure
    {
    	protected ObtieneRemesaAplicada(DataSource dataSource)
        {
            super(dataSource,"PKG_CONSULTA.P_GET_TREMEAPL");
            String[] cols=new String[]{
            		"NMREMESA" , "FEAPLICA" , "CDUNIECO" , "CDRAMO" , "ESTADO" , "NMPOLIZA" , "NMRECIBO" , "PTIMPORT" , "TIPOPAGO" , "SALDO"
            };
            declareParameter(new SqlOutParameter("pv_record_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
    	{
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_record_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
    	}
    }
    
}