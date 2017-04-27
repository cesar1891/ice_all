package mx.com.gseguros.mesacontrol.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.cotizacion.model.Item;

public interface FlujoMesaControlManager
{
	
	public Map<String,Item> workflow(
			String cdsisrol
			)throws Exception;
	
	public String movimientoTtipflumc(
			String accion
			,String cdtipflu
			,String dstipflu
			,String cdtiptra
			,String swreqpol
			,String swmultipol
			,String cdtipsup
			,String cdtipmod
            ,String swexterno
			)throws Exception;
	
	public String movimientoTflujomc(
			String accion
			,String cdtipflu
			,String cdflujomc
			,String dsflujomc
			,String swfinal
			,String cdtipram
			,String swgrupo
			)throws Exception;
	
	public void movimientoCatalogo(
			String accion
			,String tipo
			,Map<String,String> params
			)throws Exception;
	
	public String registrarEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipo
			,String clave
			,String webid
			,String xpos
			,String ypos
			)throws Exception;
	
	public void borrarEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipo
			,String clave
			,String webid
			)throws Exception;
	
	public List<Map<String,String>> cargarModelado(
			String cdtipflu
			,String cdflujomc
			)throws Exception;
	
	public Map<String,Object> cargarDatosEstado(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			)throws Exception;
	
	public void guardarDatosEstado(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			,String accion
			,String webid
			,String xpos
			,String ypos
			,String timemaxh
			,String timemaxm
			,String timewrn1h
			,String timewrn1m
			,String timewrn2h
			,String timewrn2m
			,String cdtipasig
			,String swescala
			,List<Map<String,String>>list
			,String statusout
			,boolean swfinnode
			,String cdetapa
			)throws Exception;
	
	public String registrarConnection(
			String cdtipflu
			,String cdflujomc
			,String idorigen
			,String iddestin
			)throws Exception;
	
	public Map<String,String> cargarDatosValidacion(
			String cdtipflu
			,String cdflujomc
			,String cdvalida
			)throws Exception;
	
	public void guardarDatosValidacion(
			String cdtipflu
			,String cdflujomc
			,String cdvalida
			,String webid
			,String xpos
			,String ypos
			,String dsvalida
			,String cdvalidafk
			,String jsvalida
			,String accion
			)throws Exception;
	
	public void guardarCoordenadas(
			String cdtipflu
			,String cdflujomc
			,List<Map<String,String>>list
			)throws Exception;
	
	public String ejecutaValidacion(FlujoVO flujo, String cdvalidafk, String cdusuari, String cdsisrol)throws Exception;
	
	public Map<String,Object> cargarDatosRevision(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			)throws Exception;
	
	public void guardarDatosRevision(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			,String dsrevisi
			,String accion
			,String webid
			,String xpos
			,String ypos
			,List<Map<String,String>>list
			)throws Exception;
	
	public void movimientoTdocume(
			String accion
			,String cddocume
			,String dsdocume
			,String cdtiptra
			)throws Exception;
	
	public void borrarConnection(
			String cdtipflu
			,String cdflujomc
			,String cdaccion
			)throws Exception;
	
	public Map<String,Object> cargarDatosAccion(
			String cdtipflu
			,String cdflujomc
			,String cdaccion
			)throws Exception;
	
	public void guardarDatosAccion(
			String cdtipflu
			,String cdflujomc
			,String cdaccion
			,String dsaccion
			,String accion
			,String idorigen
			,String iddestin
			,String cdvalor
			,String cdicono
			,String swescala
			,String aux
			,List<Map<String,String>>list
			)throws Exception;
	
	public Map<String,Item> debugScreen() throws Exception;
	
	public Map<String,Object> mesaControl(
			String cdsisrol
			,String agrupamc
			,String cdusuari
			)throws Exception;
	
	public Map<String,Object> recuperarTramites(
			String agrupamc
			,String status
			,String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String cdagente
			,String ntramite
			,String fedesde
			,String fehasta
			,String cdpersonCliente
			,String filtro
			,String dscontra
			,int start
			,int limit
			)throws Exception;
	
	public Map<String,String> recuperarPolizaUnica(
			String cdunieco
			,String ramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public String registrarTramite(
            String cdunieco , String cdramo    , String estado     , String nmpoliza,
            String nmsuplem , String cdsucadm  , String cdsucdoc   , String cdtiptra,
            Date ferecepc   , String cdagente  , String referencia , String nombre,
            Date festatus   , String status    , String comments   , String nmsolici,
            String cdtipsit , String cdusuari  , String cdsisrol   , String swimpres,
            String cdtipflu , String cdflujomc ,
            Map<String, String> valores,
            String cdtipsup , String cduniext  , String ramo       , String nmpoliex,
            boolean origenMesa, boolean inyectadoDesdeSigs)throws Exception;
	
	public void actualizaTramiteMC(
			  String nmpoliza ,String cdunieco ,String cdramo ,String estado  ,String ntramite
			 ,String cdtiptra ,String cduniext ,String ramo   ,String nmpoliex
			)throws Exception;
	
	public List<Map<String,String>>cargarAccionesEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String cdentidad
			,String webid
			,String cdsisrol
			)throws Exception;
	
	public void procesoDemo(
			FlujoVO flujo
			,String cdusuari
			,String cdsisrol
			)throws Exception;
	
	public Map<String, Object> ejecutaRevision(FlujoVO flujo, String cduser, String cdrol)throws Exception;
	
	/*
	JTEZVA 1 NOV 2016 SE DEJA DE USAR
	public String turnarTramite(
			String ntramite
			,String statusOld
			,String cdtipasigOld
			,String statusNew
			,String cdtipasigNew
			,String cdusuariSes
			,String cdsisrolSes
			,String comments
			,boolean cerrado
			,String swagente
			)throws Exception;
	*/
	
	public Map<String,Object> recuperarDatosTramiteValidacionCliente(FlujoVO flujo)throws Exception;
	
	/*
	JTEZVA 1 NOV 2016 SE DEJA DE USAR
	public String turnarDesdeComp(
			String cdusuari
			,String cdsisrol
			,String cdtipflu
			,String cdflujomc
			,String ntramite
			,String statusOld
			,String statusNew
			,String swagente
			,String comments
			,boolean cerrado, String cdrazrecha
			)throws Exception;
	*/
	
	/**
	 * Este procedimiento se usa para salto entre pantallas o componentes
	 * Si de pantalla A pasaste a pantalla B sin usar botones de flujo, es decir a mano, la instancia de flujo aun apunta a pantalla A
	 * con este metodo busca una conexion fantasma sin permisos que seria B, y actualiza su tipo, id y webid
	 * NOTA: No regresa nada, actualiza sobre el mismo objeto recibido
	 * @param flujo
	 * @throws Exception
	 */
	public void recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(FlujoVO flujo) throws Exception;
	
	public Map<String,String> recuperarPolizaUnicaDanios(
			String cduniext
			,String ramo
			,String nmpoliex
			)throws Exception;
	
	public void guardarTtipflurol(String cdtipflu, List<Map<String,String>> lista) throws Exception;
	
	public void guardarTflujorol(
			String cdtipflu
			,String cdflujomc
			,List<Map<String,String>> lista
			)throws Exception;
	
	public Map<String,String> cargarDatosTitulo(
			String cdtipflu
			,String cdflujomc
			,String webid
			)throws Exception;
	
	public void guardarDatosTitulo(
			String cdtipflu
			,String cdflujomc
			,String cdtitulo
			,String webid
			,String xpos
			,String ypos
			,String dstitulo
			,String accion
			)throws Exception;
	
	public String modificarDetalleTramiteMC(
			String ntramite,
			String nmordina,
			String comments,
			String cdusuari,
			String cdsisrol
			) throws Exception;
	
	public void guardarDatosCorreo(
			String cdtipflu,
			String cdflujomc,
			String cdmail,
			String dsmail,
			String dsdestino,
			String dsasunto,
			String dsmensaje,
			String vardestino,
			String varasunto,
			String varmensaje,
			String webid,
			String xpos,
			String ypos,
			String accion)throws Exception;
	
	public Map<String,String> cargarDatosCorreo(
			String cdtipflu
			,String cdflujomc
			,String cdmail)throws Exception;
	
	public Map<String, String> recuperarChecklistInicial (String ntramite, String cdsisrol) throws Exception;
	
	/**
	 * SOBRECARGADO
	 */
	@Deprecated
	public Map<String, String> enviaCorreoFlujo(FlujoVO flujo, Map<String, String> params) throws Exception;
	
	public Map<String, String> enviaCorreoFlujo(FlujoVO flujo, Map<String, String> params, boolean soloCorreosRecibidos,
	        String correosRecibidos) throws Exception;
	
	/**
	 * SOBRECARGADO
	 */
	@Deprecated
	public void mandarCorreosStatusTramite(String ntramite, String cdsisrol, boolean porEscalamiento) throws Exception;
	
	public void mandarCorreosStatusTramite(String ntramite, String cdsisrol, boolean porEscalamiento, boolean soloCorreosRecibidos,
	        String correosRecibidos) throws Exception;
	
	@Deprecated
	public void guardarMensajeCorreoEmision(String ntramite, String mensajeCorreoEmision) throws Exception;
	
	public Map<String, String> regresarTramiteVencido (String ntramite, boolean soloRevisar, String cdusuari, String cdsisrol) throws Exception;
	
	public void movimientoTrequisi(
			String accion
			,String cdrequisi
			,String dsrequisi
			,String cdtiptra
			,boolean pideDato
			)throws Exception;
	
	public void marcarRequisitoRevision (
			String cdtipflu,
			String cdflujomc,
			String ntramite,
			String cdrequisi,
			boolean activo,
			String dsdato,
			String cdusuari,
			String cdsisrol) throws Exception;
	
	public void marcarRevisionConfirmada (
			String cdtipflu,
			String cdflujomc,
			String ntramite,
			String cdrevisi,
			boolean confirmada,
			String cdusuari,
			String cdsisrol) throws Exception;
	
	@Deprecated
	public List<Map<String, String>> recuperarRequisitosDocumentosObligatoriosFaltantes (String ntramite) throws Exception;
	
	@Deprecated
	/**
	 * Recupera el estatus por defecto para tramites nuevos
	 */
	public String recuperarEstatusDefectoRol (String cdsisrol) throws Exception;
	
	@Deprecated
	public void actualizaStatusMesaControl(String ntramite, String status) throws Exception;
	
	public void recuperarCotiColec(String cdusuari, String cdsisrol, String ntramite, String nmsolici, String status) throws Exception;
	
	public Map<String,String> tramiteMC(String ntramite, String nmsolici, String cdunieco, String cdramo, String cdtipsit) throws Exception;
	
	public void guardarVentanaDatosTramite (String ntramite, Map<String, String> datos) throws Exception;
	
	public void guardarAuxiliarFlujo (String ntramite, String auxiliar) throws Exception;
	
	@Deprecated
	public List<Map<String,String>> recuperaTtipflumc(String agrupamc, String cdtipmod) throws Exception;
    
    public void pruebaGuardarObjeto () throws Exception;
	
	public void pruebaGuardarLista () throws Exception;
	
	public void cambiarTipoEndosoTramite (String ntramite, String status, String cdtipsup, String comments, boolean swagente,
	        String cdusuari, String cdsisrol) throws Exception;
	
	public FlujoVO generarYRecuperarFlujoRSTN (String ntramite, String cdusuari, String cdsisrol) throws Exception;
	
	@Deprecated
	public Map<String,String> recuperaTflujomc(String cdflujomc) throws Exception;
	
	public String obtenerSuplementoTramite(String ntramite) throws Exception;
}