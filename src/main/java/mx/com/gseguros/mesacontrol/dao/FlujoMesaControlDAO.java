package mx.com.gseguros.mesacontrol.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.mesacontrol.dao.impl.FlujoMesaControlDAOImpl;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.model.TvaloperVO;

public interface FlujoMesaControlDAO {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTtiptramc() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTtipflumc(String agrupamc, String cdtipmod) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTestadomc(String cdestadomc) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTpantamc(String cdpantmc) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTcompmc(String cdcompmc) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTprocmc(String cdprocmc) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTdocume() throws Exception;
	
	public List<Map<String, String>> recuperaTrequisi() throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTiconos() throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param swfinal TODO
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTflujomc(String cdtipflu, String swfinal)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluest(String cdtipflu, String cdflujomc, String cdestadomc)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdestadomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluestrol(String cdtipflu,
			String cdflujomc, String cdestadomc) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdestadomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluestavi(String cdtipflu,
			String cdflujomc, String cdestadomc) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdtipflumc
	 * @param cdpantmc
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @param subrayado
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTflupant(String cdtipflu,
			String cdflujomc, String cdpantmc) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTflucomp(String cdtipflu, String cdflujomc)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluproc(String cdtipflu, String cdflujomc)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluval(String cdtipflu, String cdflujomc, String cdvalida)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTflurev(String cdtipflu, String cdflujomc)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdrevisi
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTflurevdoc(String cdtipflu,
			String cdflujomc, String cdrevisi) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdtipflumc
	 * @param cdaccion
	 * @param dsaccion
	 * @param cdicono
	 * @param cdvalor
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluacc(String cdtipflu,String cdflujomc) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdaccion
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> recuperaTfluaccrol(String cdtipflu,
			String cdflujomc, String cdaccion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param dstipflu
	 * @param cdtiptra
	 * @param swmultipol
	 * @param swreqpol
	 * @param accion
	 * @throws Exception
	 */
	public String movimientoTtipflumc(String cdtipflu, String dstipflu, String cdtiptra,
			String swmultipol, String swreqpol, String cdtipsup, String cdtipmod, String swexterno, String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param dsflujomc
	 * @param swfinal
	 * @param accion
	 * @throws Exception
	 */
	public String movimientoTflujomc(
			String cdtipflu
			,String cdflujomc
			,String dsflujomc
			,String swfinal
			,String cdtipram
			,String swgrupo
			,String accion
			)throws Exception;

	/**
	 * 
	 * @param accion
	 * @param cdestadomc
	 * @param dsestadomc
	 * @throws Exception
	 */
	public void movimientoTestadomc(String accion, String cdestadomc, String dsestadomc)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdestadomc
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @param width
	 * @param height
	 * @param timemax
	 * @param timewrn1
	 * @param timewrn2
	 * @param swescala
	 * @param cdtipasig
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTfluest(String cdtipflu, String cdflujomc,
			String cdestadomc, String webid, String xpos, String ypos,
			String timemax, String timewrn1, String timewrn2,
			String cdtipasig, String statusout, String swfinnode, String cdetapa,
			String accion)
			throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdestadomc
	 * @param cdsisrol
	 * @param swver
	 * @param swtrabajo
	 * @param swcompra
	 * @param swreasig
	 * @param cdrolasig
	 * @param swverdef
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTfluestrol(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			,String cdsisrol
			,String swver
			,String swtrabajo
			,String swcompra
			,String swreasig
			,String cdrolasig
			,String swverdef
			,String accion
			)throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdestadomc
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @param timemax
	 * @param timewrn1
	 * @param timewrn2
	 * @param swescala
	 * @param cdtipasig
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTfluestavi(
			String cdtipflu,
			String cdflujomc,
			String cdestadomc,
			String cdaviso,
			String cdtipavi,
			String dscoment,
			String swautoavi,
			String dsmailavi,
			String cdusuariavi,
			String cdsisrolavi,
			String accion
			)throws Exception;

	/**
	 * 
	 * @param cdpantmc
	 * @param dspantmc
	 * @param urlpantmc
	 * @param swexterna
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTpantmc(String cdpantmc, String dspantmc, String urlpantmc,
			String swexterna, String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdpantmc
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTflupant(String cdtipflu, String cdflujomc, String cdpantmc,
			String webid, String xpos, String ypos, String accion)
			throws Exception;

	/**
	 * 
	 * @param accion
	 * @param cdcompmc
	 * @param dscompmc
	 * @param nomcomp
	 * @throws Exception
	 */
	public void movimientoTcompmc(String accion, String cdcompmc, String dscompmc,
			String nomcomp) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdcompmc
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @param subrayado
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTflucomp(String cdtipflu, String cdflujomc, String cdcompmc,
			String webid, String xpos, String ypos, String accion) throws Exception;

	/**
	 * 
	 * @param cdprocmc
	 * @param dsprocmc
	 * @param urlprocmc
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTprocmc(String cdprocmc, String dsprocmc, String urlprocmc,
			String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdprocmc
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @param width
	 * @param height
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTfluproc(String cdtipflu, String cdflujomc, String cdprocmc,
			String webid, String xpos, String ypos, String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdvalida
	 * @param dsvalida
	 * @param cdvalidafk
	 * @param webid
	 * @param xpos
	 * @param ypoS
	 * @param accion
	 * @throws Exception
	 */
	public String movimientoTfluval(String cdtipflu, String cdflujomc, String cdvalida,
			String dsvalida, String cdvalidafk, String webid, String xpos,
			String ypos, String jsvalida, String accion) throws Exception;

	/**
	 * 
	 * @param cddocume
	 * @param dsdocume
	 * @param cdtiptra
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTdocume(String cddocume, String dsdocume, String cdtiptra,
			String accion) throws Exception;

	/**
	 * 
	 * @param accion
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdrevisi
	 * @param dsrevisi
	 * @param webid
	 * @param xpos
	 * @param ypos
	 * @throws Exception
	 */
	public String movimientoTflurev(String cdtipflu, String cdflujomc,
			String cdrevisi, String dsrevisi, String webid, String xpos,
			String ypos,String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdrevisi
	 * @param cddocume
	 * @param swobliga
	 * @param subrayado
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTflurevdoc(String cdtipflu, String cdflujomc,
			String cdrevisi, String cddocume, String swobliga, String swlista,
			String accion) throws Exception;

	/**
	 * 
	 * @param cdicono
	 * @param accion
	 * @throws Exception
	 */
	public void actualizaIcono(String cdicono, String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdaccion
	 * @param dsaccion
	 * @param cdicono
	 * @param cdvalor
	 * @param idorigen
	 * @param iddestin
	 * @param accion
	 * @throws Exception
	 */
	public String movimientoTfluacc(String cdtipflu, String cdflujomc, String cdaccion,
			String dsaccion, String cdicono, String cdvalor, String idorigen,
			String iddestin, String swescala, String aux, String accion) throws Exception;

	/**
	 * 
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param cdaccion
	 * @param cdsisrol
	 * @param swpermiso
	 * @param accion
	 * @throws Exception
	 */
	public void movimientoTfluaccrol(String cdtipflu, String cdflujomc,
			String cdaccion, String cdsisrol, String swpermiso, String accion)
			throws Exception;
	
	public String ejecutaExpresion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdexpres
			)throws Exception;
	
	public void actualizaCoordenadas(
			String cdtipflu
			,String cdflujomc
			,String tipo
			,String clave
			,String webid
			,String xpos
			,String ypos
			)throws Exception;
	
	public List<Map<String,String>> recuperarTestadomcPorAgrupamc(String cdtiptra) throws Exception;
	
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
			,String dscontra
			,int start
			,int limit
			)throws Exception;
	
	public List<Map<String,String>> recuperarTtipsupl(String cdtiptra) throws Exception;
	
	public Map<String,String> recuperarPolizaUnica(
			String cdunieco
			,String ramo
			,String estado
			,String nmpoliza
			)throws Exception;
	
	public String ejecutaValidacion(String ntramite, String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac,
            String nmsuplem, String cdvalidafk, String cdusuari, String cdsisrol, String cdvalida) throws Exception;
	
	public List<Map<String,String>>cargarAccionesEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String cdentidad
			,String webid
			,String cdsisrol
			)throws Exception;
	
	public Map<String, Object> recuperarDocumentosRevisionFaltantes(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			,String ntramite
			,String cduser
			,String cdrol
			)throws Exception;
	
	public void actualizarStatusTramite(
			String ntramite
			,String status
			,Date fecstatu
			,String cdusuari
			,String cdunidspch
			)throws Exception;
	
	public Map<String,Object> recuperarDatosTramiteValidacionCliente(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String claveent
			,String webid
			,String ntramite
			,String status
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception;
	
	public Map<String,String> recuperarUsuarioParaTurnado(
			String cdsisrol
			,String tipoasig
			)throws Exception;
	
	public String recuperarRolRecienteTramite(String ntramite, String cdusuari) throws Exception;
	
	public void restarTramiteUsuario(String cdusuari,String cdsisrol) throws Exception;
	
	public Map<String,String> recuperarUsuarioHistoricoTramitePorRol(String ntramite, String cdsisrol) throws Exception;
	
	public void guardarHistoricoTramite(Date fecha, String ntramite, String cdusuari, String cdsisrol, String status, String cdunidspch, String cdtipasig) throws Exception;
	
	/**
	 * Se recupera una conexion a una pantalla o componente que no tenga ningun permiso
	 * @param cdtipflu
	 * @param cdflujomc
	 * @param tipoent
	 * @param claveent
	 * @param webid
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String claveent
			,String webid
			)throws Exception;
	
	public List<Map<String, String>> recuperaTtipflumcPorRolPorUsuario(String agrupamc,String cdsisrol,String cdusuari) throws Exception;
	
	public List<Map<String, String>> recuperaTflujomcPorRolPorUsuario(
			String cdtipflu
			,String swfinal
			,String cdsisrol
			,String cdusuari
			)throws Exception;
	
	public Map<String,String> recuperaTflujomc(String cdflujomc) throws Exception;
	
	public Map<String,String> recuperarPolizaUnicaDanios(
			String cduniext
			,String ramo
			,String nmpoliex
			)throws Exception;
	
	public List<Map<String,String>> recuperarTtipflurol(String cdtipflu) throws Exception;
	
	public void guardarTtipflurol(String cdtipflu, List<Map<String,String>> lista) throws Exception;
	
	public List<Map<String,String>> recuperarTflujorol(String cdtipflu, String cdflujomc) throws Exception;
	
	public void guardarTflujorol(
			String cdtipflu
			,String cdflujomc
			,List<Map<String,String>> lista
			)throws Exception;
	
	public Map<String,String> recuperarFlujoPorDescripcion(String descripcion) throws Exception;
	
	public void movimientoTflutit(
			String cdtipflu,
			String cdflujomc,
			String cdtitulo,
			String dstitulo,
			String webid,
			String xpos,
			String ypos,
			String accion
			)throws Exception;
	
	public List<Map<String, String>> recuperaTflutit(String cdtipflu, String cdflujomc, String cdtitulo) throws Exception;
	
	public String modificarDetalleTramiteMC(
			String ntramite,
			String nmordina,
			String comments,
			String cdusuari,
			String cdsisrol,
			Date fecha
			) throws Exception;
	
	public String movimientoTmail(String cdtipflu,
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
			String accion) throws Exception;
	
	public List<Map<String, String>> recuperaTflumail(String cdtipflu, String cdflujomc, String cdmail) throws Exception;
	
	public List<Map<String, String>> recuperaTvarmailSP() throws Exception;
	
	public void guardarMotivoRechazoTramite (String ntramite, String cdrazrecha) throws Exception;
	
	public List<Map<String, String>> recuperarChecklistInicial (String ntramite, String cdsisrol) throws Exception;
	
	public String ejecutaProcedureFlujoCorreo(String nomproc, String ntramite) throws Exception;
	
	public List<Map<String, String>> obtenerCorreosStatusTramite(String ntramite, String cdsisrol, String porEscalamiento) throws Exception;
	
	public void guardarMensajeCorreoEmision(String ntramite, String mensajeCorreoEmision) throws Exception;

	public List<Map<String, String>> recuperarTramitesSinFlag () throws Exception;
	
	public void insertarFlagTramite (
		String ntramite,
		Date fecstatu,
		String flag,
		Date fechaAmarilla,
		Date fechaRoja,
		Date fechaMaxima
	) throws Exception;

	public List<Map<String, String>> recuperarTramitesConFlagVencida () throws Exception;
	
	public Map<String, String> recuperarEstatusAnteriorVencido (String ntramite) throws Exception;
	
	public List<Map<String, String>> recuperarRolesPermisoRegresarVencido(String ntramite, String status) throws Exception;
	
	public Map<String, String> recuperarDatosVencimiento (String ntramite) throws Exception;
	
	public void movimientoTrequisi (String cdrequisi, String dsrequisi, String cdtiptra, boolean pideDato, String accion) throws Exception;
	
	public void movimientoTflurevreq (
			String cdtipflu, 
			String cdflujomc,
			String cdrevisi,
			String cdrequisi,
			String swobliga,
			String swlista,
			String accion
			) throws Exception;
	
	public List<Map<String, String>> recuperaTflurevreq (String cdtipflu, String cdflujomc, String cdrevisi) throws Exception;
	
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
	
	public List<Map<String, String>> recuperarRequisitosDocumentosObligatoriosFaltantes (String ntramite) throws Exception;
	
	public List<Map<String, String>> recuperarRequisitosDatosTramite (String ntramite) throws Exception;
	
	public List<Map<String, String>> recuperarTodasSucursales () throws Exception;
	
	public void movimientoTflusuc(
		String cdtipflu,
		String cdflujomc,
		String cdunieco,
		String webid,
		String xpos,
		String ypos,
		String nivel,
		String capacidad,
		String accion) throws Exception;
	
	/**
	 * Recupera el estatus por defecto para tramites nuevos
	 */
	public String recuperarEstatusDefectoRol (String cdsisrol) throws Exception;
	
	public List<Map<String, String>> recuperarValidacionPorCdvalidafk(String ntramite, String clave) throws Exception;
	
	public List<Map<String, String>> recuperarCotizacionesColectivasAprobadas(String ntramite) throws Exception;
	
	public String recuperaNombreMd5(String md5) throws Exception;
	
	public void guardarVentanaDatosTramite (String ntramite,
	        boolean swcdunieco,
	        boolean swcdramo,
	        boolean swestado,
	        boolean swnmpoliza,
	        boolean swnmsuplem,
	        boolean swnmsolici,
	        boolean swcdsucadm,
	        boolean swcdsucdoc,
	        boolean swcdsubram,
	        boolean swcdtiptra,
	        boolean swferecepc,
	        boolean swcdagente,
	        boolean swreferencia,
	        boolean swnombre,
	        boolean swfecstatu,
	        boolean swstatus,
	        boolean swcomments,
	        boolean swcdtipsit,
	        boolean swotvalor01,
	        boolean swotvalor02,
	        boolean swotvalor03,
	        boolean swotvalor04,
	        boolean swotvalor05,
	        boolean swotvalor06,
	        boolean swotvalor07,
	        boolean swotvalor08,
	        boolean swotvalor09,
	        boolean swotvalor10,
	        boolean swotvalor11,
	        boolean swotvalor12,
	        boolean swotvalor13,
	        boolean swotvalor14,
	        boolean swotvalor15,
	        boolean swotvalor16,
	        boolean swotvalor17,
	        boolean swotvalor18,
	        boolean swotvalor19,
	        boolean swotvalor20,
	        boolean swotvalor21,
	        boolean swotvalor22,
	        boolean swotvalor23,
	        boolean swotvalor24,
	        boolean swotvalor25,
	        boolean swotvalor26,
	        boolean swotvalor27,
	        boolean swotvalor28,
	        boolean swotvalor29,
	        boolean swotvalor30,
	        boolean swotvalor31,
	        boolean swotvalor32,
	        boolean swotvalor33,
	        boolean swotvalor34,
	        boolean swotvalor35,
	        boolean swotvalor36,
	        boolean swotvalor37,
	        boolean swotvalor38,
	        boolean swotvalor39,
	        boolean swotvalor40,
	        boolean swotvalor41,
	        boolean swotvalor42,
	        boolean swotvalor43,
	        boolean swotvalor44,
	        boolean swotvalor45,
	        boolean swotvalor46,
	        boolean swotvalor47,
	        boolean swotvalor48,
	        boolean swotvalor49,
	        boolean swotvalor50,
	        boolean swswimpres,
	        boolean swcdtipflu,
	        boolean swcdflujomc,
	        boolean swcdusuari,
	        boolean swcdtipsup,
	        boolean swswvispre,
	        boolean swcdpercli,
	        boolean swrenuniext,
	        boolean swrenramo,
	        boolean swrenpoliex,
	        boolean swsworigenmesa,
	        boolean swcdrazrecha,
	        boolean swcdunidspch,
	        String cdunieco,
	        String cdramo,
	        String estado,
	        String nmpoliza,
	        String nmsuplem,
	        String nmsolici,
	        String cdsucadm,
	        String cdsucdoc,
	        String cdsubram,
	        String cdtiptra,
	        String ferecepc,
	        String cdagente,
	        String referencia,
	        String nombre,
	        String fecstatu,
	        String status,
	        String comments,
	        String cdtipsit,
	        String otvalor01,
	        String otvalor02,
	        String otvalor03,
	        String otvalor04,
	        String otvalor05,
	        String otvalor06,
	        String otvalor07,
	        String otvalor08,
	        String otvalor09,
	        String otvalor10,
	        String otvalor11,
	        String otvalor12,
	        String otvalor13,
	        String otvalor14,
	        String otvalor15,
	        String otvalor16,
	        String otvalor17,
	        String otvalor18,
	        String otvalor19,
	        String otvalor20,
	        String otvalor21,
	        String otvalor22,
	        String otvalor23,
	        String otvalor24,
	        String otvalor25,
	        String otvalor26,
	        String otvalor27,
	        String otvalor28,
	        String otvalor29,
	        String otvalor30,
	        String otvalor31,
	        String otvalor32,
	        String otvalor33,
	        String otvalor34,
	        String otvalor35,
	        String otvalor36,
	        String otvalor37,
	        String otvalor38,
	        String otvalor39,
	        String otvalor40,
	        String otvalor41,
	        String otvalor42,
	        String otvalor43,
	        String otvalor44,
	        String otvalor45,
	        String otvalor46,
	        String otvalor47,
	        String otvalor48,
	        String otvalor49,
	        String otvalor50,
	        String swimpres,
	        String cdtipflu,
	        String cdflujomc,
	        String cdusuari,
	        String cdtipsup,
	        String swvispre,
	        String cdpercli,
	        String renuniext,
	        String renramo,
	        String renpoliex,
	        String sworigenmesa,
	        String cdrazrecha,
	        String cdunidspch) throws Exception;
	
	public String recuperarCdatribuPorDsatribuTatriflumc (String cdtipflu, String cdflujomc, String dsatribu) throws Exception;
	
	public void movimientoTfluaccrolLote (List<Map<String, String>> lista) throws Exception;
    
    public void actualizaCoordenadasLote (List<Map<String, String>> lista) throws Exception;
    
    public void movimientoTflurevdocLote (List<Map<String, String>> lista) throws Exception;
    
    public void movimientoTflurevreqLote (List<Map<String, String>> lista) throws Exception;
    
    public void pruebaGuardarObjeto (FlujoMesaControlDAOImpl.AlvaroObj objeto) throws Exception;
    
    public void pruebaGuardarLista (List<FlujoMesaControlDAOImpl.AlvaroObj> lista) throws Exception;
    
    public List<Map<String, String>> recuperarPropiedadesDespachadorSucursales (String cdtipram) throws Exception;
    
    public List<Map<String, String>> recuperarPropiedadesDespachadorUsuarios (String cdunieco, String nivel, String cdsisrol) throws Exception;
    
    public List<Map<String, String>> recuperarPropiedadesDespachadorUsuariosAll (String cdsisrol) throws Exception;
    
    public void actualizarTramiteSustituto(String ntramite, String ntrasust) throws Exception;
    
    public void cambiarTipoEndosoTramite (String ntramite, String cdtipsup) throws Exception;
    
    public String recuperarCorreoAgenteTramite (String ntramite) throws Exception;
    
    public FlujoVO generarYRecuperarFlujoRSTN (String ntramite, String cdusuari, String cdsisrol) throws Exception;
    
    public String obtenerSuplementoTramite(String ntramite)throws Exception;
    
    public void pruebaObjetoTvaloper(TvaloperVO objeto) throws Exception;
}
