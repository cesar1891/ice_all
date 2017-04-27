package mx.com.gseguros.portal.consultas.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.model.PagedMapList;
import mx.com.gseguros.portal.consultas.model.RecuperacionSimple;
import mx.com.gseguros.portal.consultas.service.RecuperacionSimpleManager;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlist2VO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSmapVO;
import mx.com.gseguros.portal.despachador.dao.DespachadorDAO;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.general.dao.IndicadoresDAO;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

public class RecuperacionSimpleManagerImpl implements RecuperacionSimpleManager {
	
	private final static Logger logger = LoggerFactory.getLogger(RecuperacionSimpleManagerImpl.class);
	
	private ConsultasDAO  consultasDAO;
	private CotizacionDAO cotizacionDAO;
	private EndososDAO    endososDAO;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private IndicadoresDAO indicadoresDAO;
	
	@Autowired
	private DespachadorDAO despachadorDAO;
	
	@Deprecated
	@Override
	public ManagerRespuestaSmapVO recuperacionSimple(
			RecuperacionSimple proc
			,Map<String,String>params
			,String cdsisrol
			,String cdusuari
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperacionSimple @@@@@@"
				,"\n@@@@@@ procedimiento=" , proc
				,"\n@@@@@@ parametros="    , params
				,"\n@@@@@@ cdsisrol="      , cdsisrol
				,"\n@@@@@@ cdusuari="      , cdusuari
				));
		
		ManagerRespuestaSmapVO resp=new ManagerRespuestaSmapVO(true);
		
		String paso = null;
		
		try
		{
			paso = "Recuperando datos";
			if(proc.equals(RecuperacionSimple.RECUPERAR_DESCUENTO_RECARGO_RAMO_5))
			{
				paso = "Recuperando rango de descuento/recargo";
				String cdtipsit = params.get("cdtipsit");
				String cdagente = params.get("cdagente");
				String negocio  = params.get("negocio");
				String tipocot  = params.get("tipocot");
				
				if(tipocot.equals("I"))
				{
				    resp.setSmap(cotizacionDAO.cargarRangoDescuentoRamo5(cdtipsit,cdagente,negocio,cdsisrol,cdusuari));
				}
				else if(tipocot.equals("P")||tipocot.equals("F"))
				{
					resp.setSmap(cotizacionDAO.cargarRangoDescuentoRamo5TodasSituaciones(cdagente,negocio,cdsisrol,cdusuari));
				}
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_DATOS_VEHICULO_RAMO_5))
			{
				paso = "Recuperando datos del vehiculo";
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSmap(cotizacionDAO.cargarDatosVehiculoRamo5(cdunieco,cdramo,estado,nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_ULTIMO_NMSUPLEM))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSmap(consultasDAO.cargarUltimoNmsuplemPoliza(cdunieco,cdramo,estado,nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.VERIFICAR_CODIGO_POSTAL_FRONTERIZO))
			{
				String cdpostal = params.get("cdpostal");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("fronterizo",consultasDAO.verificarCodigoPostalFronterizo(cdpostal)?"S":"N");
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_TEXTO_CLAUSULA_POLIZA))
			{
				String cdclausu = params.get("cdclausu");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("dsclausu" , Utils.join("Texto de la clausula ",cdclausu));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_PORCENTAJE_RECARGO_POR_PRODUCTO))
			{
				String cdramo   = params.get("cdramo");
				String cdperpag = params.get("cdperpag");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("recargo" , consultasDAO.recuperarPorcentajeRecargoPorProducto(cdramo,cdperpag));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_VALOR_ATRIBUTO_UNICO))
			{
				String cdtipsit = params.get("cdtipsit");
				String cdatribu = params.get("cdatribu");
				String otclave  = params.get("otclave");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("otvalor",consultasDAO.recuperarValorAtributoUnico(cdtipsit,cdatribu,otclave));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_DSATRIBUS_TATRIPOL))
			{
				String        cdramo       = params.get("cdramo");
				List<String>  listaNombres = consultasDAO.recuperarDescripcionAtributosProducto(cdramo);
				StringBuilder sb           = new StringBuilder();
				boolean       primero      = true;
				for(String nombre:listaNombres)
				{
					if(primero)
					{
						sb.append(Utils.join("CVE_P_"    , nombre));
						sb.append(Utils.join("@#@DES_P_" , nombre));
						primero = false;
					}
					else
					{
						sb.append(Utils.join("@#@CVE_P_" , nombre));
						sb.append(Utils.join("@#@DES_P_" , nombre));
					}
				}
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("listaNombres",sb.toString());
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_DSATRIBUS_TATRISIT))
			{
				String        cdramo       = params.get("cdramo");
				List<String>  listaNombres = consultasDAO.recuperarDescripcionAtributosSituacionPorRamo(cdramo);
				StringBuilder sb           = new StringBuilder();
				boolean       primero      = true;
				for(String nombre:listaNombres)
				{
					if(primero)
					{
						sb.append(Utils.join("CVE_"    , nombre));
						sb.append(Utils.join("@#@DES_" , nombre));
						primero = false;
					}
					else
					{
						sb.append(Utils.join("@#@CVE_" , nombre));
						sb.append(Utils.join("@#@DES_" , nombre));
					}
				}
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("listaNombres",sb.toString());
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_FECHAS_LIMITE_ENDOSO))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String cdtipsup = params.get("cdtipsup");
				resp.setSmap(new HashMap<String,String>());
				
				resp.getSmap().putAll(consultasDAO.recuperarFechasLimiteEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,cdsisrol
						,cdusuari
						,cdtipsup
						));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_PERMISO_USUARIO_DEVOLUCION_PRIMAS))
			{
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("permiso" , consultasDAO.recuperarPermisoDevolucionPrimasUsuario(cdusuari) ? "S" : "N");
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_VALOR_MAXIMO_SITUACION_POR_ROL))
			{
				String cdtipsit = params.get("cdtipsit");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("VALOR" , consultasDAO.recuperarValorMaximoSituacionPorRol(cdtipsit,cdsisrol));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_TATRISIT_AMPARADO))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsituac = params.get("nmsituac");
				String nmsuplem = params.get("nmsuplem");
				String cdtipsit = params.get("cdtipsit");
				String cdatribu = params.get("cdatribu");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("CONTEO" , consultasDAO.obtieneConteoSituacionCoberturaAmparada(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplem
						,cdtipsit
						,cdatribu
						));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_DERECHOS_POLIZA_POR_PAQUETE_RAMO_1))
			{
				String paquete = params.get("paquete");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("DERECHOS" , consultasDAO.recuperarDerechosPolizaPorPaqueteRamo1(paquete));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_CONTEO_BLOQUEO))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSmap(new HashMap<String,String>());
				resp.getSmap().put("CONTEO" , consultasDAO.recuperarConteoTbloqueo(cdunieco,cdramo,estado,nmpoliza));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ " , resp
				,"\n@@@@@@ recuperacionSimple @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
	@Deprecated
	@Override
	public ManagerRespuestaSlist2VO recuperacionSimpleLista(
			RecuperacionSimple proc
			,Map<String,String>params
			,String cdsisrol
			,String cdusuari
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperacionSimpleLista @@@@@@"
				,"\n@@@@@@ proc="     , proc
				,"\n@@@@@@ params="   , params
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdusuari=" , cdusuari
				));
		
		ManagerRespuestaSlist2VO resp=new ManagerRespuestaSlist2VO(true);
		
		String paso = null;
		
		try
		{
			paso = "Recuperando datos";
			if(proc.equals(RecuperacionSimple.RECUPERAR_DETALLES_COTIZACION_AUTOS_FLOTILLA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String cdperpag = params.get("cdperpag");
				resp.setSlist(cotizacionDAO.cargarDetallesCotizacionAutoFlotilla(cdunieco, cdramo, estado, nmpoliza, cdperpag));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_DETALLES_COBERTURAS_COTIZACION_AUTOS_FLOTILLA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String cdperpag = params.get("cdperpag");
				resp.setSlist(cotizacionDAO.cargarDetallesCoberturasCotizacionAutoFlotilla(cdunieco, cdramo, estado, nmpoliza, cdperpag));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_TVALOSIT))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsuplem = params.get("nmsuplem");
				resp.setSlist(Utils.concatenarParametros(consultasDAO.cargarTvalosit(cdunieco, cdramo, estado, nmpoliza, nmsuplem),false));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_CONFIGURACION_VALOSIT_FLOTILLAS))
			{
				String cdramo   = params.get("cdramo");
				String cdtipsit = params.get("cdtipsit");
				String negocio  = params.get("negocio");
				Map<String,List<Map<String,String>>> res = cotizacionDAO.cargarConfiguracionTvalositFlotillas(cdramo, cdtipsit, negocio);
				resp.setSlist(res.get("config"));
				resp.setSlist2(res.get("rangos"));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_MPOLIPER_OTROS_ROLES_POR_NMSITUAC))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsuplem = params.get("nmsuplem");
				String nmsituac = params.get("nmsituac");
				String roles    = params.get("roles");
				resp.setSlist(consultasDAO.cargarMpoliperOtrosRolesPorNmsituac(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac, roles));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_CLAUSULAS_POLIZA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsuplem = params.get("nmsuplem");
				
				List<Map<String,String>>lista=new ArrayList<Map<String,String>>();
				resp.setSlist(lista);
				
				Map<String,String>cla1=new HashMap<String,String>();
				lista.add(cla1);
				cla1.put("cdunieco" , cdunieco);
				cla1.put("cdramo"   , cdramo);
				cla1.put("estado"   , estado);
				cla1.put("nmpoliza" , nmpoliza);
				cla1.put("nmsuplem" , nmsuplem);
				cla1.put("status"   , "V");
				cla1.put("cdclausu" , "1");
				cla1.put("dsclausu" , "uno");
				cla1.put("dslinea"  , "Texto de la primer clausula");
				
				Map<String,String>cla2=new HashMap<String,String>();
				lista.add(cla2);
				cla2.put("cdunieco" , cdunieco);
				cla2.put("cdramo"   , cdramo);
				cla2.put("estado"   , estado);
				cla2.put("nmpoliza" , nmpoliza);
				cla2.put("nmsuplem" , nmsuplem);
				cla2.put("status"   , "V");
				cla2.put("cdclausu" , "2");
				cla2.put("dsclausu" , "dos");
				cla2.put("dslinea"  , "Texto de la segunda clausula");
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_VALORES_PANTALLA))
			{
				String pantalla = params.get("pantalla");
				String cdramo   = params.get("cdramo");
				String cdtipsit = params.get("cdtipsit");
				resp.setSlist(consultasDAO.recuperarValoresPantalla(pantalla,cdramo,cdtipsit));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_VALORES_ATRIBUTOS_FACTORES))
			{
				String cdramo   = params.get("cdramo");
				String cdtipsit = params.get("cdtipsit");
				resp.setSlist(consultasDAO.recuperarValoresAtributosFactores(cdramo,cdtipsit));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_POLIZAS_ENDOSABLES))
			{
				String cdunieco  = params.get("cdunieco");
				String cdramo    = params.get("cdramo");
				String estado    = params.get("estado");
				String nmpoliza  = params.get("nmpoliza");
				String nmpoliex  = params.get("nmpoliex");
				String ramo      = params.get("ramo");
				String cdagente  = params.get("cdagente");
				String statusVig = params.get("statusVig");
				//Se agregan campos para Filtrado por Fecha de Inicio y Fecha de fin
				String finicio   = params.get("finicio");
				String ffin      = params.get("ffin");
				resp.setSlist(consultasDAO.recuperarPolizasEndosables(cdunieco,cdramo,estado,nmpoliza,nmpoliex,ramo,cdagente,statusVig,finicio,ffin,cdsisrol));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_HISTORICO_POLIZA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarHistoricoPoliza(cdunieco,cdramo,estado,nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String cdgrupo  = params.get("cdgrupo");
				String nmfamili = params.get("nmfamili");
				String nivel    = params.get("nivel");
				String start	= params.get("start");
				String limit	= params.get("limit");
				String dsatribu = params.get("dsatribu");
				String otvalor	= params.get("otvalor");
				resp.setSlist(consultasDAO.recuperarIncisosPolizaGrupoFamilia(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,cdgrupo
						,nmfamili
						,nivel
						,start
						,limit
						,dsatribu
						,otvalor
						));
				
				if(Ramo.SERVICIO_PUBLICO.getCdramo().equalsIgnoreCase(cdramo) && params.containsKey("atrPol") && Constantes.SI.equalsIgnoreCase(params.get("atrPol"))){
					
					if(resp.getSlist() !=null && !resp.getSlist().isEmpty()){
						
						List<Map<String,String>> lista = consultasDAO.recuperarDatosIncisoEnNivelPoliza(
								cdunieco
								,cdramo
								,estado
								,nmpoliza
						);
						logger.debug("Lista de atributos a agregar" + lista);
						
						if(lista!= null && !lista.isEmpty()){
							Map<String,String> atrsPol = lista.get(0);
							
							Iterator<Entry<String, String>> itAtrs = atrsPol.entrySet().iterator();
							while(itAtrs.hasNext())
							{
								Entry<String,String> atr = (Map.Entry<String, String>) itAtrs.next();
								String llave = atr.getKey();
								String valor = atr.getValue();
								
								if(llave.startsWith("DSATRIBU")){
									llave = llave.replace("DSATRIBU", "DSATRIBU1");
								}else if(llave.startsWith("DSVALOR")){
									llave = llave.replace("DSVALOR", "DSVALOR1");
								}else if(llave.startsWith("OTVALOR")){
									llave = llave.replace("OTVALOR", "OTVALOR1");
								}
								
								//EVITAMOS QUE PLANCHE EL PLAN consultasDAO.recuperarDatosIncisoEnNivelPoliza REGRESA NULL ESTATICAMENTE EN DSPLAN
								if(llave.equals("DSPLAN") && valor==null){
									continue;
								}

								resp.getSlist().get(0).put(llave, valor);
								
							}
						}
						
						logger.debug("Lista de atributos despues de agregar" + resp.getSlist());
					}
					
				}
				
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_GRUPOS_POLIZA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarGruposPoliza(cdunieco,cdramo,estado,nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_FAMILIAS_POLIZA))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarFamiliasPoliza(cdunieco,cdramo,estado,nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_COBERTURAS_ENDOSO_DEVOLUCION_PRIMAS))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsituac = params.get("nmsituac");
				String tstamp   = params.get("tstamp");
				resp.setSlist(endososDAO.recuperarCoberturasEndosoDevolucionPrimas(cdunieco, cdramo, estado, nmpoliza, nmsituac, tstamp));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_ENDOSOS_REHABILITABLES))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarEndososRehabilitables(cdunieco, cdramo, estado, nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_ENDOSOS_CANCELABLES))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarEndososCancelables(cdunieco, cdramo, estado, nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_REVISION_COLECTIVOS))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarRevisionColectivos(cdunieco, cdramo, estado, nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_REVISION_COLECTIVOS_FINAL))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				resp.setSlist(consultasDAO.recuperarRevisionColectivosFinal(cdunieco, cdramo, estado, nmpoliza));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_REVISION_COLECTIVOS_ENDOSOS))
			{
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsuplem = params.get("nmsuplem");
				resp.setSlist(consultasDAO.recuperarRevisionColectivosEndosos(cdunieco, cdramo, estado, nmpoliza, nmsuplem));
			}
			else if(proc.equals(RecuperacionSimple.RECUPERAR_USUARIOS_REASIGNACION_TRAMITE))
			{
				String ntramite = params.get("ntramite");
				resp.setSlist(consultasDAO.recuperarUsuariosReasignacionTramite(ntramite, cdusuari, cdsisrol));
			} 
			else if(proc.equals(RecuperacionSimple.RECUPERAR_COBERTURAS_PRIMA_NETA))
            {
                String cdunieco = params.get("cdunieco");
                String cdramo   = params.get("cdramo");
                String estado   = params.get("estado");
                String nmpoliza = params.get("nmpoliza");
                String nmsituac = params.get("nmsituac");
                String tstamp   = params.get("tstamp");
                resp.setSlist(endososDAO.recuperarCoberturasEndosoPrimaNeta(cdunieco, cdramo, estado, nmpoliza, nmsituac, tstamp));
            }else if(proc.equals(RecuperacionSimple.RECUPERAR_ENDOSOS_SINIESTRALIDAD)){
                String cdunieco = params.get("cdunieco");
                String cdramo   = params.get("cdramo");
                String estado   = params.get("estado");
                String nmpoliza = params.get("nmpoliza");
                resp.setSlist(consultasDAO.recuperarEndososSiniestralidad(cdunieco, cdramo, estado, nmpoliza));
            }else if(proc.equals(RecuperacionSimple.RECUPERAR_ENDOSOS_SINIESTRALIDAD_REHA)){
                String cdunieco = params.get("cdunieco");
                String cdramo   = params.get("cdramo");
                String estado   = params.get("estado");
                String nmpoliza = params.get("nmpoliza");
                resp.setSlist(consultasDAO.recuperarEndososRehabilitablesSiniestralidad(cdunieco, cdramo, estado, nmpoliza));
            }
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ " , resp
				,"\n@@@@@@ recuperacionSimpleLista @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
	@Override
	public Map<String,String> recuperarMapa(
			String cdusuari
			,String cdsisrol
			,RecuperacionSimple consulta
			,Map<String,String> params
			,UserVO usuario
	)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarMapa @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ consulta=" , consulta
				,"\n@@@@@@ params="   , params
				,"\n@@@@@@ usuario="  , usuario
				));
		Map<String,String> mapa = new LinkedHashMap<String,String>();
		String             paso = "Recuperando datos";
		try
		{
			if(consulta.equals(RecuperacionSimple.RECUPERAR_IMPRESIONES_DISPONIBLES))
			{
				paso = "Recuperando impresiones disponibles";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String cdtipram = params.get("cdtipram");
				String tipolote = params.get("tipolote");
				
				Utils.validate(
						cdtipram  , "No se recibi\u00F3 el tipo de ramo"
						,tipolote , "No se recibi\u00F3 el tipo de lote"
						);
				
				String impdisp = consultasDAO.recuperarImpresionesDisponiblesPorTipoRamo(cdtipram, tipolote);
				
				mapa.put("imprdisp" , impdisp);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_DETALLE_IMPRESION_LOTE))
			{
				paso = "Recuperando detalle de impresiones de lote";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String lote = params.get("lote");
				String tramite = params.get("tramite");
				
				Utils.validate(lote, "No se recibi\u00F3 el lote");
				
				Map<String,String>requeridasYEjecutadas = consultasDAO.recuperarDetalleImpresionLote(lote,tramite);
				
				mapa.putAll(requeridasYEjecutadas);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_SWVISPRE_TRAMITE))
			{
				paso = "Recuperando estado de vista previa de tr\u00e1mite";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String ntramite = params.get("ntramite");
				
				Utils.validate(ntramite, "No se recibi\u00F3 el tr\u00e1mite");
				
				mapa.put("SWVISPRE" , mesaControlDAO.recuperarSwvispreTramite(ntramite));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_DIAS_FECHA_FACTURACION))
			{
				paso = "Recuperando fecha de facturaci\u00f3n";
				logger.debug(Utils.log(""," paso: ",paso));
				String cdtipsit = params.get("cdtipsit");
				mapa.put("dias" , consultasDAO.recuperarDiasFechaFacturacion(cdtipsit, cdsisrol));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_PERMISO_BOTON_GENERAR_COLECTIVO))
			{
				paso = "Recuperando permisos de bot\u00f3n";
				logger.debug(Utils.log(""," paso: ",paso));
				@SuppressWarnings("unused")
				String cdtipsit = params.get("cdtipsit");
				mapa.put("ACTIVAR_BOTON" , consultasDAO.recuperarPermisoBotonEnviarCenso(cdsisrol));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_ESTADO_BOTON_EMITIR))
			{
				paso = "Recuperando permisos de boton comprar";
				logger.debug(Utils.log(" paso: ",paso));
				String cdtipsit = params.get("cdtipsit");
				Utils.validate(
						 cdtipsit , "No se recibio el parametro cdtipsit"
						);
				mapa.put("ACTIVAR_BOTON_COMPRAR" , consultasDAO.recuperarPermisoBotonEmitir(cdsisrol,cdusuari,cdtipsit));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_CDUNIEXT_POR_LLAVE_POLIZA))
			{
				paso = "Recuperando cduniext";
				logger.debug(paso);
				
				mapa.put("cduniext" , consultasDAO.recuperarCduniextPorLlavePoliza(
						params.get("cdunieco"),
						params.get("cdramo"),
						params.get("estado"),
						params.get("nmpoliza")
						));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_FLUJO_POR_DESCRIPCION))
			{
				String descripcion = params.get("descripcion");
				
				paso = "Recuperando flujo por descripcion";
				
				logger.debug(Utils.log(paso, " descripcion = ", descripcion));
				
				mapa = flujoMesaControlDAO.recuperarFlujoPorDescripcion(params.get("descripcion"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_SI_ES_CDRAMO_DE_SALUD)) {
				String cdramo = params.get("cdramo");
				if (StringUtils.isNotBlank(cdramo)) {
					mapa.put(
						"salud",
						consultasDAO.esProductoSalud(cdramo)
							? "S"
							: "N"
					);
				} else {
					mapa.put("salud", "N");
				}
			} else if(consulta.equals(RecuperacionSimple.RECUPERAR_TABLERO_INDICADORES)) {
				paso = "Recuperando tablero de indicadores";
				logger.debug(paso);
				
				Map<String, String> res = indicadoresDAO.obtieneDashInicial(params.get("idcierre"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"),
						params.get("tipotramite"), params.get("cdagente"));
				
				Map<String, String> res2 = indicadoresDAO.obtieneDashPendientes(params.get("idcierre"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"),
						params.get("tipotramite"), params.get("cdagente"));
				
				mapa.putAll(res);
				mapa.putAll(res2);
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_CORREO_EMISION_TRAMITE)) {
				paso = "Recuperando correo de emisi\u00f3n";
				logger.debug(paso);
				String ntramite = params.get("ntramite");
				if (StringUtils.isNotBlank(ntramite)) {
					mapa.put("correo", Utils.cambiaGuionesBajosPorAcentosHtml(consultasDAO.recuperarCorreoEmisionTramite(ntramite)));
				} else {
					mapa.put("correo", "");
				}
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_ASEGURADO_COMPLETO_ENDOSO_ALTA)) {
				mapa = endososDAO.recuperarAseguradoCompletoEndosoAlta(params.get("cdunieco"), params.get("cdramo"), params.get("estado"),
						params.get("nmpoliza"), params.get("nmsuplem"), params.get("nmsituac"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_PERSONA_ENDOSO_ALTA)) {
				mapa = endososDAO.recuperarPersonaEndosoAlta(params.get("cdperson"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_CORREO_AGENTE_TRAMITE)) {
			    mapa .put("correoAgente", flujoMesaControlDAO.recuperarCorreoAgenteTramite(params.get("ntramite")));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_RANGO_DESCUENTO_RECARGO)) {
			    mapa = cotizacionDAO.recuperarRangoDescuentoRecargo(params.get("cdramo"), params.get("cdtipsit"), cdusuari, cdsisrol);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(""
				,"\n@@@@@@ mapa=",mapa
				,"\n@@@@@@ recuperarMapa @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return mapa;
	}
	
	@Override
	public List<Map<String,String>> recuperarLista(
			String cdusuari
			,String cdsisrol
			,RecuperacionSimple consulta
			,Map<String,String> params
			,UserVO usuario
	)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarLista @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ consulta=" , consulta
				,"\n@@@@@@ params="   , params
				,"\n@@@@@@ usuario="  , usuario
				));
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		String                   paso  = "Recuperando datos";
		try
		{
			if(consulta.equals(RecuperacionSimple.RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA))
			{
				paso = "Recuperando incisos de p\u00F3liza/grupo/familia";
				logger.debug(Utils.log(""," paso: ",paso));
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String cdgrupo  = params.get("cdgrupo");
				String nmfamili = params.get("nmfamili");
				String nivel    = params.get("nivel");
				String start    = params.get("start");
				String limit    = params.get("limit");
				String dsatribu = params.get("dsatribu");
				String otvalor	= params.get("otvalor");
				lista = consultasDAO.recuperarIncisosPolizaGrupoFamilia(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,cdgrupo
						,nmfamili
						,nivel
						,start
						,limit
						,dsatribu
						,otvalor
						);
//				if(consulta.equals(RecuperacionSimple.RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA)){
//					Map<String,String> total = lista.remove(lista.size()-1);				
//				}
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_MOVIMIENTOS_ENDOSO_ALTA_BAJA_ASEGURADO))
			{
				paso = "Recuperando movimientos de endoso de alta/baja de asegurados";
				logger.debug(Utils.log(""," paso: ",paso));
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nmsuplem  = params.get("nmsuplem");
				lista = consultasDAO.recuperarMovimientosEndosoAltaBajaAsegurados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_POLIZAS_PARA_EXPLOTAR_DOCS))
			{
				paso = "Recuperando polizas para explotar documentos";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String cdtipram     = params.get("cdtipram");
				String cduniecos    = params.get("cduniecos");
				String cdramo       = params.get("cdramo");
				String ramo         = params.get("ramo");
				String nmpoliza     = params.get("nmpoliza");
				String fefecha      = params.get("fefecha");
				String cdusuariLike = params.get("cdusuari");
				String cdagente     = params.get("cdagente");
				
				Utils.validate(
						cdtipram   , "No se recibi\u00F3 el tipo de ramo"
						,cduniecos , "No se recibieron sucursales"
						,fefecha   , "No se recibi\u00F3 la fecha"
						);
				
				lista = consultasDAO.recuperarPolizasParaImprimir(
						cdtipram
						,cduniecos
						,cdramo
						,ramo
						,nmpoliza
						,Utils.parse(fefecha)
						,cdusuariLike
						,cdagente
						,cdusuari
						,usuario.getCdUnieco()
						);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_IMPRESORAS))
			{
				paso = "Recuperando impresoras por papel";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String papel    = params.get("papel");
				String swactivo = params.get("swactivo");
				
				Utils.validate(
						papel     , "No se recibi\u00F3 el tipo de papel"
						);
				
				lista = consultasDAO.recuperarImpresorasPorPapelYSucursal(cdusuari,papel,swactivo);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_PERMISOS_IMPRESION))
			{
				paso = "Recuperando permisos por sucursal";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String cdusuariPer = params.get("cdusuari");
				String cdunieco    = params.get("cdunieco");
				String cdtipram    = params.get("cdtipram");
				
				Utils.validate(
						cdusuariPer , "No se recibi\u00F3 el usuario"
						,cdunieco   , "No se recibi\u00F3 la sucursal"
						,cdtipram   , "No se recibi\u00F3 el tipo de ramo"
						);
				
				List<Map<String,String>> sucursales = consultasDAO.recuperarConfigImpresionSucursales(cdusuariPer,cdunieco,cdtipram);
				
				paso = "Recuperando permisos por agente";
				logger.debug(Utils.log(""," paso: ",paso));
				
				List<Map<String,String>> agentes = consultasDAO.recuperarConfigImpresionAgentes(cdusuariPer,cdunieco,cdtipram);
				
				paso = "Recuperando permisos por usuario";
				logger.debug(Utils.log(""," paso: ",paso));
				
				List<Map<String,String>> usuarios = consultasDAO.recuperarConfigImpresionUsuarios(cdusuariPer,cdunieco,cdtipram);
				
				paso = "Integrando permisos";
				logger.debug(Utils.log(""," paso: ",paso));
				
				List<Map<String,String>> resumen = new ArrayList<Map<String,String>>();
				for(Map<String,String>sucursal:sucursales)
				{
					Map<String,String>nuevo = new HashMap<String,String>();
					nuevo.put("tipo"        , "S");
					nuevo.put("cdusuari"    , sucursal.get("COD_USUARIO"));
					nuevo.put("cdunieco"    , sucursal.get("SUC_USUARIO"));
					nuevo.put("cdtipram"    , sucursal.get("TIPO_RAMO"));
					nuevo.put("cduniecoPer" , sucursal.get("SUC_PERMITIDA"));
					nuevo.put("funcion"     , sucursal.get("SWAPLICA"));
					nuevo.put("descrip"     , sucursal.get("DESCRIP"));
					resumen.add(nuevo);
				}
				
				for(Map<String,String> agente:agentes)
				{
					Map<String,String>nuevo = new HashMap<String,String>();
					nuevo.put("tipo"        , "A");
					nuevo.put("cdusuari"    , agente.get("COD_USUARIO"));
					nuevo.put("cdunieco"    , agente.get("SUC_USUARIO"));
					nuevo.put("cdtipram"    , agente.get("TIPO_RAMO"));
					nuevo.put("cdagentePer" , agente.get("AGENTE"));
					nuevo.put("funcion"     , agente.get("SWAPLICA"));
					nuevo.put("descrip"     , agente.get("DESCRIP"));
					resumen.add(nuevo);
				}
				
				for(Map<String,String> usuarioPer:usuarios)
				{
					Map<String,String>nuevo = new HashMap<String,String>();
					nuevo.put("tipo"        , "U");
					nuevo.put("cdusuari"    , usuarioPer.get("COD_USUARIO"));
					nuevo.put("cdunieco"    , usuarioPer.get("SUC_USUARIO"));
					nuevo.put("cdtipram"    , usuarioPer.get("TIPO_RAMO"));
					nuevo.put("cdusuariPer" , usuarioPer.get("CDUSUARI_PERMISO"));
					nuevo.put("funcion"     , usuarioPer.get("SWAPLICA"));
					nuevo.put("descrip"     , usuarioPer.get("DESCRIP"));
					resumen.add(nuevo);
				}
				
				logger.debug(Utils.log("","@@@@@@ lista permisos=",resumen));
				lista = resumen;
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_RECIBOS_PARA_EXPLOTAR_DOCS))
			{
				paso = "Recuperando recibos en lote";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String cdtipram  = params.get("cdtipram");
				String cduniecos = params.get("cduniecos");
				String feproces  = params.get("feproces");
				String feimpres  = params.get("feimpres");
				String tiporeciboimp = params.get("tiporeciboimp");
				
				lista = consultasDAO.recuperarRecibosLote(
						cdtipram
						,cduniecos
						,Utils.parse(feproces)
						,Utils.parse(feimpres)
						,cdusuari
						,usuario.getCdUnieco()
						,tiporeciboimp
						);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_DETALLE_REMESA))
			{
				paso = "Recuperando detalle de remesa";
				logger.debug(Utils.log(""," paso: ",paso));
				
				String ntramite = params.get("ntramite");
				String tipolote = params.get("tipolote");
				
				lista = consultasDAO.recuperarDetalleRemesa(ntramite,tipolote);
			}
			
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TTIPTRAMC))
			{
				lista = flujoMesaControlDAO.recuperaTtiptramc();
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TTIPFLUMC))
			{	
				if(params!=null)
				{
					lista = flujoMesaControlDAO.recuperaTtipflumc(params.get("agrupamc"), params.get("cdtipmod"));
				}
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TESTADOMC))
			{	
				if(params==null)
				{
					params = new HashMap<String,String>();
				}
				lista = flujoMesaControlDAO.recuperaTestadomc(params.get("cdestadomc"));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TPANTMC))
			{
				if(params==null)
				{
					params = new HashMap<String,String>();
				}
				lista = flujoMesaControlDAO.recuperaTpantamc(params.get("CDPANTMC"));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TCOMPMC))
			{
				if(params==null)
				{
					params = new HashMap<String,String>();
				}
				lista = flujoMesaControlDAO.recuperaTcompmc(params.get("CDCOMPMC"));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TPROCMC))
			{
				if(params==null)
				{
					params = new HashMap<String,String>();
				}
				lista = flujoMesaControlDAO.recuperaTprocmc(params.get("CDPROCMC"));
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TDOCUME))
			{
				lista = flujoMesaControlDAO.recuperaTdocume();
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TREQUISI))
			{
				lista = flujoMesaControlDAO.recuperaTrequisi();
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TICONOS))
			{
				lista = flujoMesaControlDAO.recuperaTiconos();
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUJOMC))
			{
				String cdtipflu = params.get("cdtipflu");
				lista = flujoMesaControlDAO.recuperaTflujomc(cdtipflu, null);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUEST))
			{
				String cdtipflu   = params.get("cdtipflu");
				String cdflujomc  = params.get("cdflujomc");
				String cdestadomc = params.get("cdestadomc");
				lista = flujoMesaControlDAO.recuperaTfluest(cdtipflu, cdflujomc,cdestadomc);
				
				for(Map<String,String>mapa:lista)
				{
					if(StringUtils.isBlank(mapa.get("TIMEMAX")))
					{
						mapa.put("TIMEMAX","0");
					}
					if(StringUtils.isBlank(mapa.get("TIMEWRN1")))
					{
						mapa.put("TIMEWRN1","0");
					}
					if(StringUtils.isBlank(mapa.get("TIMEWRN2")))
					{
						mapa.put("TIMEWRN2","0");
					}
					
					Double tmax = Double.parseDouble(mapa.get("TIMEMAX"));
					Double wrn1 = Double.parseDouble(mapa.get("TIMEWRN1"));
					Double wrn2 = Double.parseDouble(mapa.get("TIMEWRN2"));
					
					mapa.put("TIMEMAXH" , String.format("%.0f",Math.floor(tmax/60d)));
					mapa.put("TIMEMAXM" , String.format("%.0f",Math.floor(tmax%60d)));
					
					mapa.put("TIMEWRN1H" , String.format("%.0f",Math.floor(wrn1/60d)));
					mapa.put("TIMEWRN1M" , String.format("%.0f",Math.floor(wrn1%60d)));
					
					mapa.put("TIMEWRN2H" , String.format("%.0f",Math.floor(wrn2/60d)));
					mapa.put("TIMEWRN2M" , String.format("%.0f",Math.floor(wrn2%60d)));
				}
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUESTROL))
			{
				String cdtipflu = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				String cdestadomc = params.get("cdestadomc");
				
				lista = flujoMesaControlDAO.recuperaTfluestrol(cdtipflu,cdflujomc,cdestadomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUESTAVI))
			{
				String cdtipflu = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				String cdestadomc = params.get("cdestadomc");
				lista = flujoMesaControlDAO.recuperaTfluestavi(cdtipflu, cdflujomc, cdestadomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUPANT))
			{
				String cdtipflu  = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				String cdpantmc  = params.get("cdpantmc");
				lista = flujoMesaControlDAO.recuperaTflupant(cdtipflu, cdflujomc,cdpantmc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUCOMP))
			{
				String cdtipflumc=params.get("cdtipflumc");
				String cdflujomc=params.get("cdflujomc");
				lista = flujoMesaControlDAO.recuperaTflucomp(cdtipflumc, cdflujomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUPROC))
			{
				String cdtipflu = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				
				lista = flujoMesaControlDAO.recuperaTfluproc(cdtipflu, cdflujomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUVAL))
			{
				String cdtipflu  = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				String cdvalida  = params.get("cdvalida");
				lista = flujoMesaControlDAO.recuperaTfluval(cdtipflu, cdflujomc, cdvalida);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUREV))
			{
				String cdtipflu = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				
				lista = flujoMesaControlDAO.recuperaTflurev(cdtipflu,cdflujomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUREVDOC))
			{
				String cdtipflu = params.get("cdtipflu");
				String cdflujomc = params.get("cdflujomc");
				String cdrevisi = params.get("cdrevisi");
				lista = flujoMesaControlDAO.recuperaTflurevdoc(cdtipflu, cdflujomc, cdrevisi);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUACC))
			{
				String cdtipflu=params.get("cdtipflu");
				String cdflujomc=params.get("cdflujomc");
				lista = flujoMesaControlDAO.recuperaTfluacc(cdtipflu, cdflujomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUACCROL))
			{
				String cdtipflu=params.get("cdtipflu");
				String cdflujomc=params.get("cdflujomc");
				String cdaccion=params.get("cdaccion");
				lista = flujoMesaControlDAO.recuperaTfluaccrol(cdtipflu, cdflujomc, cdaccion);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_ROLES))
			{
				lista = consultasDAO.recuperarRolesTodos();
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_EXCLUSION_TURNADOS))
			{
				lista = consultasDAO.recuperarExclusionTurnados();
			}	
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_CLAVES_PLAN_RAMO4))
			{
				String cdramo=params.get("cdramo"),
					   cdtipsit=params.get("cdtipsit"),
				       cdplan=params.get("cdplan");
				lista = consultasDAO.recuperarClavesPlanRamo4(cdramo, cdtipsit, cdplan);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_LISTA_TATRISIT_SIN_PADRE))
			{
				paso = "Recuperando lista Tatrisit sin Padre ";
				logger.debug(Utils.log(" paso: ",paso));
				
				String cdtipsit=params.get("cdtipsit"),
				       cdatribu=params.get("cdatribu");
				
				Utils.validate(
						 cdtipsit , "No se recibio el parametro cdtipsit"
						,cdatribu , "No se recibio el parametro cdatribu"
						);
				lista = consultasDAO.recuperarListaTatrisitSinPadre(cdtipsit,cdatribu);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_LISTA_FILTRO_PROPIEDADDES_INCISO))
			{
				paso = "Recuperando lista para combo de Busqueda";
				logger.debug(Utils.log(" paso: ",paso));
				
				String cdunieco=params.get("cdunieco");
				String cdramo=params.get("cdramo");
				String estado=params.get("estado");
				String nmpoliza=params.get("nmpoliza");
				
				Utils.validate(
						cdunieco , "No se recibio el parametro cdunieco",
						cdramo ,"No se recibio el parametro cdramo",
						estado ,"No se recibio el parametro estado",
						nmpoliza,"No se recibio el parametro nmpoliza");
				
				lista = consultasDAO.llenaCombo(cdunieco, cdramo, estado, nmpoliza);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TTIPFLUROL))
			{
				paso = "Recuperando permisos de tr\u00e1mite por rol";
				logger.debug(paso);
				
				String cdtipflu = params.get("cdtipflu");
				
				Utils.validate(cdtipflu, "No se recibi\u00f3 el tr\u00e1mite");
				
				lista = flujoMesaControlDAO.recuperarTtipflurol(cdtipflu);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUJOROL))
			{
				paso = "Recuperando permisos de proceso por rol";
				logger.debug(paso);
				
				String cdtipflu    = params.get("cdtipflu")
						,cdflujomc = params.get("cdflujomc");
				
				Utils.validate(
						cdtipflu   , "No se recibi\u00f3 el tr\u00e1mite"
						,cdflujomc , "No se recibi\u00f3 el proceso"
						);
				
				lista = flujoMesaControlDAO.recuperarTflujorol(cdtipflu,cdflujomc);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TFLUMAIL))
			{
				paso = "Recuperando permisos de proceso por rol";
				logger.debug(paso);
				
				String cdtipflu  = params.get("cdtipflu"),
					   cdflujomc = params.get("cdflujomc"),
					   cdmail    = params.get("cdmail");
				
				Utils.validate(
						cdtipflu   , "No se recibi\u00f3 el tr\u00e1mite"
						,cdflujomc , "No se recibi\u00f3 el proceso"
						,cdmail    , "No se recibi\u00f3 el codigo de mail"
						);
				
				lista = flujoMesaControlDAO.recuperaTflumail(cdtipflu, cdflujomc, cdmail);
			}
			else if(consulta.equals(RecuperacionSimple.RECUPERAR_TVARMAIL))
			{
				paso = "Recuperando permisos de proceso por rol";
				logger.debug(paso);
							
				lista = flujoMesaControlDAO.recuperaTvarmailSP();
			}
			else if (consulta.equals(RecuperacionSimple.RECUPERAR_MPOLIZAS_POR_PARAMETROS_VARIABLES)) {
				String cdunieco = params.get("cdunieco"),
						cdramo  = params.get("cdramo"),
						estado  = params.get("estado"),
						nmpoliza = params.get("nmpoliza"),
						nmsuplem = params.get("nmsuplem"),
						nmsolici = params.get("nmsolici"),
						cdramant = params.get("cdramant");
				lista = consultasDAO.cargarMpolizasPorParametrosVariables(
						cdunieco,
						cdramo,
						estado,
						nmpoliza,
						nmsuplem, 
						nmsolici,
						cdramant
				);
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_REQUISITOS_DATOS_TRAMITE)) {
				lista = flujoMesaControlDAO.recuperarRequisitosDatosTramite(params.get("ntramite"));
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_POR_LINEA_NEGOCIO) ) {
				
				lista = indicadoresDAO.obtieneTramitesPorLineaNegocio(params.get("idcierre"), params.get("cdetapa"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"));
				
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_LINEANEGOCIO_POR_RAMO) ) {
				
				PagedMapList paged = indicadoresDAO.obtieneTramitesLineaNegocioPorRamo(params.get("idcierre"), params.get("cdetapa"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", "1");
				
				PagedMapList paged2 = indicadoresDAO.obtieneTramitesLineaNegocioPorRamo(params.get("idcierre"), params.get("cdetapa"),
                        params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", Long.toString(paged.getTotalItems()));
				
				lista = paged2.getRangeList();
				
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_DETALLE_LINEA_NEGOCIO) ) {
				
				PagedMapList paged = indicadoresDAO.obtieneDetalleLineaNegocio(params.get("idcierre"), params.get("cdetapa"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", "1");
				
				PagedMapList paged2 = indicadoresDAO.obtieneDetalleLineaNegocio(params.get("idcierre"), params.get("cdetapa"),
                        params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", Long.toString(paged.getTotalItems()));
				
				lista =  paged2.getRangeList();
				
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_LINEANEGOCIO_POR_SUCURSAL) ) {
				
				lista = indicadoresDAO.obtieneLineaNegocioPorSucursal(params.get("idcierre"), params.get("cdetapa"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"));
				
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_LINEANEGOCIO_POR_USUARIO) ) {
				
			    PagedMapList paged = indicadoresDAO.obtieneLineaNegocioPorUsuario(params.get("idcierre"), params.get("cdetapa"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", "1");
			    PagedMapList paged2 = indicadoresDAO.obtieneLineaNegocioPorUsuario(params.get("idcierre"), params.get("cdetapa"),
                        params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", Long.toString(paged.getTotalItems()));
			    
			    lista =  paged2.getRangeList();
			    
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_POR_TIPO) ) {
				
			    PagedMapList paged = indicadoresDAO.obtieneTramitesPorTipo(params.get("idcierre"), params.get("cdetapa"),
						params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", "1");
			    PagedMapList paged2 = indicadoresDAO.obtieneTramitesPorTipo(params.get("idcierre"), params.get("cdetapa"),
                        params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), "0", Long.toString(paged.getTotalItems()));
				
			    lista =  paged2.getRangeList();
			    
			//} else if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_PENDIENTES_POR_DIAS) ) {
			} else if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_PENDIENTES_POR_HORAS) ) {
				
				PagedMapList paged = indicadoresDAO.obtieneTramitesPendientesPorDia(params.get("idcierre"),
						params.get("cdunieco"), params.get("lineaNegocio"), params.get("cdramo"),
						params.get("tipotramite"), params.get("cdagente"), params.get("numdias"), "0", "1");

				PagedMapList paged2 = indicadoresDAO.obtieneTramitesPendientesPorDia(params.get("idcierre"),
                        params.get("cdunieco"), params.get("lineaNegocio"), params.get("cdramo"),
                        params.get("tipotramite"), params.get("cdagente"), params.get("numdias"), "0", Long.toString(paged.getTotalItems()));
				
				lista =  paged2.getRangeList();
				
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_TODAS_SUCURSALES)) {
				lista = flujoMesaControlDAO.recuperarTodasSucursales();
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_VALIDACION_POR_CDVALIDAFK)) {
				lista = flujoMesaControlDAO.recuperarValidacionPorCdvalidafk(params.get("ntramite"), params.get("clave"));
				for (Map<String, String> i : lista) {
					i.put("cdusuari", cdusuari);
					i.put("cdsisrol", cdsisrol);
				}
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_COTIZACIONES_COLECTIVAS_APROBADAS)) {
				lista = flujoMesaControlDAO.recuperarCotizacionesColectivasAprobadas(params.get("ntramite"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_ASEGURADOS_ENDOSO_ALTA)) {
				lista = endososDAO.recuperarAseguradosEndosoAlta(params.get("cdunieco"), params.get("cdramo"), params.get("estado"),
						params.get("nmpoliza"), params.get("nmsuplem"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_PERSONAS_FISICAS_POR_RFC_MULTIPLE_DOMICILIO)) {
				lista = endososDAO.recuperarPersonasFisicasPorRFCMultipleDomicilio(params.get("rfc"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_BUSQUEDA_ASEGURADOS_END_COBERTURAS)) {
				lista = endososDAO.buscarAseguradosEndosoCoberturas(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"), params.get("nmsuplem"), params.get("cadena"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_COBERTURAS_AMPARADAS_CONFIRMADAS)) {
				lista = endososDAO.recuperarCoberturasAmparadasConfirmadas(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"), params.get("nmsituac"), params.get("nmsuplem"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_COBERTURAS_DISPONIBLES)) {
				lista = endososDAO.recuperarCoberturasDisponibles(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"), params.get("nmsituac"), params.get("nmsuplem"),
						usuario.getRolActivo().getClave());
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_COBERTURAS_AGREGADAS)) {
				lista = endososDAO.recuperarCoberturasAgregadas(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"), params.get("nmsituac"), params.get("nmsuplem"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_ASEGURADOS_AFECTADOS_END_COBERTURAS)) {
				lista = endososDAO.recuperarAseguradosAfectadosEndosoCoberturas(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"), params.get("nmsuplem"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_COBERTURAS_BORRADAS)) {
				lista = endososDAO.recuperarCoberturasBorradas(params.get("cdunieco"), params.get("cdramo"),
						params.get("estado"), params.get("nmpoliza"), params.get("nmsituac"), params.get("nmsuplem"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_DESPACHADOR_DATOS_SUCURSALES)) {
			    lista = flujoMesaControlDAO.recuperarPropiedadesDespachadorSucursales(params.get("cdtipram"));
			} else if (consulta.equals(RecuperacionSimple.RECUPERAR_DESPACHADOR_DATOS_USUARIOS)) {
                lista = flujoMesaControlDAO.recuperarPropiedadesDespachadorUsuarios(params.get("cdunieco"), params.get("cdnivel"),
                        params.get("cdsisrol"));
            } else if (consulta.equals(RecuperacionSimple.RECUPERAR_DESPACHADOR_DATOS_USER_ALL_X_ROL)) {
                lista = flujoMesaControlDAO.recuperarPropiedadesDespachadorUsuariosAll(params.get("cdsisrol"));
            } else if (consulta.equals(RecuperacionSimple.RECUPERAR_DESPACHADOR_DATOS_ZONA)) {
                lista = despachadorDAO.recuperarLogDespachadorZona(params.get("ntramite"), params.get("cdunieco"), params.get("estatus"));
            } else if (consulta.equals(RecuperacionSimple.RECUPERAR_HISTORIAL_TRAMITE)) {
                lista = despachadorDAO.recuperarHistorialMesaHora(params.get("ntramite"));
            } else if (consulta.equals(RecuperacionSimple.RECUPERAR_DETALLES_TRAMITE)) {
                lista = despachadorDAO.recuperarDetallesMesaHora(params.get("ntramite"));
            }
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(""
				,"\n@@@@@@ lista=",lista==null ? "null" : lista.size()
				,"\n@@@@@@ recuperarLista @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}

	
	@Override
	public PagedMapList recuperarListaPaginada(
	        String cdusuari
	        ,String cdsisrol
	        ,RecuperacionSimple consulta
	        ,Map<String,String> params
	        ,String start
            ,String limit
	        ,UserVO usuario
	        )throws Exception
	{
	    logger.debug(Utils.log(""
	            ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	            ,"\n@@@@@@ recuperarListaPaginada @@@@@@"
	            ,"\n@@@@@@ cdusuari=" , cdusuari
	            ,"\n@@@@@@ cdsisrol=" , cdsisrol
	            ,"\n@@@@@@ consulta=" , consulta
	            ,"\n@@@@@@ params="   , params
	            ,"\n@@@@@@ start="    , start
	            ,"\n@@@@@@ limit="    , limit
	            ,"\n@@@@@@ usuario="  , usuario
	            ));
	    
	    PagedMapList lista =  null;
	    String paso  = "Recuperando datos";
	    try
	    {
	        //if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_PENDIENTES_POR_DIAS_LP) ) {
	    	if( consulta.equals(RecuperacionSimple.RECUPERAR_TRAMITES_PENDIENTES_POR_HORAS_LP) ) {
                
                lista = indicadoresDAO.obtieneTramitesPendientesPorDia(params.get("idcierre"),
                        params.get("cdunieco"), params.get("lineaNegocio"), params.get("cdramo"),
                        params.get("tipotramite"), params.get("cdagente"), params.get("numdias"), start, limit);
                
            }else if( consulta.equals(RecuperacionSimple.RECUPERAR_LINEANEGOCIO_POR_USUARIO_LP) ) {
                
                lista = indicadoresDAO.obtieneLineaNegocioPorUsuario(params.get("idcierre"), params.get("cdetapa"),
                        params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), start, limit);
                
            }else if( consulta.equals(RecuperacionSimple.RECUPERAR_DETALLE_LINEA_NEGOCIO_LP) ) {
                
                lista = indicadoresDAO.obtieneDetalleLineaNegocio(params.get("idcierre"), params.get("cdetapa"),
                        params.get("cdunieco"), params.get("lineanegocio"), params.get("cdramo"), params.get("tipotramite"), params.get("cdagente"), start, limit);
                
            }
	    }
	    catch(Exception ex)
	    {
	        Utils.generaExcepcion(ex, paso);
	    }
	    logger.debug(Utils.log(""
	            ,"\n@@@@@@ lista Pagianda=", (lista==null|| lista.getRangeList() ==  null)? "null" : lista.getRangeList().size()
	                    ,"\n@@@@@@ recuperarLista @@@@@@"
	                    ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	            ));
	    return lista;
	}
	
	//////////////////////////////////////////////////////////////
	// GETTERS Y SETTERS /////////////////////////////////////////
	                                                            //
	public void setConsultasDAO(ConsultasDAO consultasDAO) {    //
		this.consultasDAO = consultasDAO;                       //
	}                                                           //
                                                                //
	public void setCotizacionDAO(CotizacionDAO cotizacionDAO) { //
		this.cotizacionDAO = cotizacionDAO;                     //
	}                                                           //
                                                                //
	public void setEndososDAO(EndososDAO endososDAO) {          //
		this.endososDAO = endososDAO;                           //
	}                                                           //
	//////////////////////////////////////////////////////////////
	
}