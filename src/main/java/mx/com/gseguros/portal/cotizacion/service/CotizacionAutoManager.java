package mx.com.gseguros.portal.cotizacion.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlist2SmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;


public interface CotizacionAutoManager
{
	
	public Map<String,Object> cotizacionAutoIndividual(
			String ntramite
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String cdusuari
			,String cdsisrol
			,FlujoVO flujo
			,boolean mostrarCamposComplementarios
			)throws Exception;
	public ManagerRespuestaSmapVO cargarRetroactividadSuplemento(
			String cdunieco
			,String cdramo
			,String cdtipsup
			,String cdusuari
			,String cdtipsit
			)throws Exception;
	public ManagerRespuestaSmapVO cargarSumaAseguradaRamo5(
			String cdtipsit
			,String clave
			,String modelo
			,String cdsisrol
			)throws Exception;
	public ManagerRespuestaImapSmapVO emisionAutoIndividual(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite
			,String cdusuari
			,String cdsisrol
			)throws Exception;
	public ManagerRespuestaSmapVO cargarDatosComplementariosAutoInd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception;
	public ManagerRespuestaSmapVO cargarValoresSituacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception;
	public ManagerRespuestaVoidVO movimientoMpoliper(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrol
			,String cdperson
			,String nmsuplem
			,String status
			,String nmorddom
			,String swreclam
			,String accion
			,String swexiper
			)throws Exception;
	public ManagerRespuestaVoidVO guardarComplementariosAutoIndividual(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String agenteSec
			,String porpartiSec
			,String feini
			,String fefin
			,Map<String,String>tvalopol
			,Map<String,String>tvalosit
			,String ntramite
			,String cdagente
			)throws Exception;
	public ManagerRespuestaSmapVO cargarConfiguracionCotizacion(
			String cdramo
			,String cdtipsit
			,String cdusuari
			)throws Exception;
	public ManagerRespuestaVoidVO guardarConfiguracionCotizacion(
			String cdramo
			,String cdtipsit
			,String cdusuari
			,Map<String,String>valores
			)throws Exception;
	public ManagerRespuestaSlistVO cargarParamerizacionConfiguracionCoberturas(
			String cdtipsit
			,String cdsisrol
			,String negocio
			,String tipoServicio
			,String modelo
			,String tipoPersona
			,String submarca
			,String clavegs
			)throws Exception;
	public ManagerRespuestaImapSmapVO cotizacionAutoFlotilla(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String ntramite
			,String tipoflot
			,boolean endoso
			,FlujoVO flujo
			,boolean renovacion
			)throws Exception;
	public ManagerRespuestaSlistSmapVO cotizarAutosFlotilla(
			String cdusuari
			,String cdsisrol
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String feini
			,String fefin
			,String cdagente
			,String cdpersonCli
			,String nmorddomCli
			,String cdideperCli
			,List<Map<String,String>> tvalosit
			,List<Map<String,String>> baseTvalosit
			,List<Map<String,String>> confTvalosit
			,boolean noTarificar
			,String tipoflot
			,Map<String,String>tvalopol
			,UserVO usuarioSesion
			,String cduniext
			,String renramo
			,String nmpoliex
			,String ntramite
			);
	public ManagerRespuestaVoidVO cargarValidacionTractocamionRamo5(String poliza,String rfc) throws Exception;
	public ManagerRespuestaSlistVO procesarCargaMasivaFlotilla(String cdramo,String cdtipsit,String respetar,File excel, String tipoflot)throws Exception;
	public ManagerRespuestaSlist2SmapVO cargarCotizacionAutoFlotilla(
			String cdramo
			,String nmpoliza
			,String cdusuari
			,String cdsisrol
			,String ntramiteIn
			)throws Exception;
	public ManagerRespuestaImapSmapVO emisionAutoFlotilla(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite
			,String cdusuari
			,String cdsisrol
			)throws Exception;
	public ManagerRespuestaVoidVO guardarComplementariosAutoFlotilla(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String agenteSec
			,String porpartiSec
			,String feini
			,String fefin
			,Map<String,String>tvalopol
			,List<Map<String,String>>tvalosit
			,String ntramite
			)throws Exception;
	
	public ManagerRespuestaSlistVO recotizarAutoFlotilla(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,boolean notarifica
			,String cdusuari
			,String cdelemen
			,String cdtipsit
			,String cdperpag
			)throws Exception;
	
	public ManagerRespuestaSmapVO cargarObligatorioTractocamionRamo5(String clave) throws Exception;
	
	public ManagerRespuestaSmapVO cargarDetalleNegocioRamo5(
			String negocio
			,String cdramo
			,String cdtipsit
			,String cdsisrol
			,String cdusuari
			)throws Exception;
	
	
	public ManagerRespuestaVoidVO guardarPantallaBeneficiarios(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,List<Map<String,String>>mpoliperMpersona
			,UserVO usuarioSesion
			)throws Exception;
	
	public ManagerRespuestaSlistVO cargarParamerizacionConfiguracionCoberturasRol(
			String cdtipsit
			,String cdsisrol
			)throws Exception;
	
	public String obtieneValidacionRetroactividad(
			String numSerie
			,Date feini) throws ApplicationException;
	
	public List<Map<String, String>> modificadorValorVehPYME(List<Map<String, String>> slist1, String cdsisrol, String cdpost, String cambio) throws Exception;
	public List<Map<String,String>> validaVacioDescRecg(List<Map<String,String>> slistPYME) throws Exception;
	public List<Map<String,String>> validaExcelCdtipsitXNegocio(String tipoflot, String negocio, List<Map<String,String>> slistPYME) throws Exception;
    
	public boolean aplicaDxn(String cdtipsit, 
                             String cdsisrol, 
                             String cdusuarii,
                             String cdagente) throws Exception;
}