package mx.com.gseguros.portal.consultas.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ParametroGeneral;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.MailService;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.portal.renovacion.dao.RenovacionDAO;
import mx.com.gseguros.portal.siniestros.dao.SiniestrosDAO;
import mx.com.gseguros.utils.DocumentosUtils;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

@Service
public class ConsultasManagerImpl implements ConsultasManager
{
	private static Logger logger = Logger.getLogger(ConsultasManagerImpl.class);
	
	@Value("${ruta.documentos.poliza}")
    private String rutaDocumentosPoliza;
	
	@Value("${ruta.documentos.temporal}")
    private String rutaDocumentosTemporal;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private RenovacionDAO renovacionDAO;
	
	@Autowired
	private SiniestrosDAO siniestrosDAO;
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private MailService mailService;

	@Autowired
    private MesaControlDAO mesaControlDAO;
	
	@Value("${pass.servidor.reports}")
    private String passServidorReports;
    
    @Value("${rdf.impresion.remesa}")
    private String nombreReporteRemesa;
    
    @Value("${pdf.impresion.remesa.nombre}")
    private String nombreRemesaPdf;
    
    @Value("${ruta.servidor.reports}")
    private String rutaServidorReports;

	@Override
	public List<Map<String,String>> consultaDinamica(ObjetoBD objetoBD,LinkedHashMap<String,Object>params) throws Exception
	{
		logger.debug(""
				+ "\n##############################"
				+ "\n###### consultaDinamica ######"
				);
		logger.debug("storedProcedure: "+ objetoBD.getNombre());
		logger.debug("params: "+params);
		List<Map<String,String>> lista = consultasDAO.consultaDinamica(objetoBD.getNombre(), params);
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug("lista: "+lista);
		logger.debug(""
				+ "\n###### consultaDinamica ######"
				+ "\n##############################"
				);
		return lista;
	}
	
	@Override
	public void validarDatosCliente(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception {
		consultasDAO.validarDatosCliente(cdunieco, cdramo, estado, nmpoliza);
	}

	@Override
	public void validarDatosObligatoriosPrevex(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception
	{
		consultasDAO.validarDatosObligatoriosPrevex(cdunieco, cdramo, estado, nmpoliza);
	}
	
	@Override
	public void validarDatosDXN(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem)throws Exception
	{
		consultasDAO.validarAtributosDXN(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
	}
	
	@Override
	@Deprecated
	public Map<String,String>cargarAtributosBaseCotizacion(String cdtipsit)throws Exception
	{
		return consultasDAO.cargarAtributosBaseCotizacion(cdtipsit);
	}
	
	@Override
	@Deprecated
	public Map<String,String>cargarInformacionPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdusuari
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarInformacionPoliza @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ cdusuari=" , cdusuari
				));
		
		Map<String,String>datos=consultasDAO.cargarInformacionPoliza(cdunieco,cdramo,estado,nmpoliza,cdusuari);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ " , datos
				,"\n@@@@@@ cargarInformacionPoliza @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return datos;
	}
	
	@Override
	@Deprecated
	public List<Map<String,String>>cargarMpolizasPorParametrosVariables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsolici
			,String cdramant
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarMpolizasPorParametrosVariables @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ nmsolici=" , nmsolici
				,"\n@@@@@@ cdramant=" , cdramant
				));

		List<Map<String,String>>lista=consultasDAO.cargarMpolizasPorParametrosVariables(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsuplem
				,nmsolici
				,cdramant
				);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=" , lista
				,"\n@@@@@@ cargarMpolizasPorParametrosVariables @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}

	@Override
	public List<Map<String,String>> obtieneContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrol
			,String cdperson
			)throws Exception
			{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneContratantePoliza @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				,"\n@@@@@@ cdrol="    , cdrol
				,"\n@@@@@@ cdperson=" , cdperson
				));
		
		List<Map<String,String>>lista=consultasDAO.obtieneContratantePoliza(
				  cdunieco
				, cdramo
				, estado
				, nmpoliza
				, nmsituac
				, cdrol
				, cdperson
				);
		
		logger.debug(Utils.log(
				"\n@@@@@@ lista=" , lista
				,"\n@@@@@@ obtieneContratantePoliza @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
			}
	
	
	@Override
	public boolean esProductoSalud(String cdramo) throws Exception {
		return consultasDAO.esProductoSalud(cdramo);
	}
	
	@Override
	public String validacionesSuplemento(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsup
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ validacionesSuplemento @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		
		String error=consultasDAO.validacionesSuplemento(cdunieco,cdramo,estado,nmpoliza,nmsituac,nmsuplem,cdtipsup);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ error=",error
				,"\n@@@@@@ validacionesSuplemento @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return error;
	}
	
	
	public boolean copiarArchivosUsuarioTramite(String cduniecoOrigen, String cdramoOrigen, String estadoOrigen, 
			String nmpolizaOrigen, String ntramiteDestino, String rutaDocumentos)throws Exception{
		
		//copiar documentos de usuario
		List<Map<String,String>>iPolizaUserDocs=renovacionDAO.cargarDocumentosSubidosPorUsuario(cduniecoOrigen,cdramoOrigen,estadoOrigen,nmpolizaOrigen);
		for(Map<String,String>iUserDoc:iPolizaUserDocs)
		{
			String ntramiteAnt = iUserDoc.get("ntramite");
			String cddocume    = iUserDoc.get("cddocume");
			
			File doc = new File(
					new StringBuilder(rutaDocumentos)
					.append("/")
					.append(ntramiteAnt)
					.append("/")
					.append(cddocume)
					.toString()
					);
			
			File newDoc = new File(
					new StringBuilder(rutaDocumentos)
					.append("/")
					.append(ntramiteDestino)
					.append("/")
					.append(cddocume)
					.toString()
					);
			
			if(doc!=null&&doc.exists())
			{
				try
				{
					FileUtils.copyFile(doc, newDoc);
				}
				catch(Exception ex)
				{
					logger.error("Error copiando archivo de usuario",ex);
				}
			}
			else
			{
				logger.error(new StringBuilder("No existe el documento").append(doc).toString());
			}
		}
		
		consultasDAO.copiaDocumentosTdocupol(cduniecoOrigen, cdramoOrigen, estadoOrigen, 
				nmpolizaOrigen, ntramiteDestino);
		
		return true;
	}
	
	@Deprecated
	@Override
	public boolean validaClientePideNumeroEmpleado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ validaClientePideNumeroEmpleado @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		
		boolean pide = consultasDAO.validaClientePideNumeroEmpleado(cdunieco,cdramo,estado,nmpoliza);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ pide=",pide
				,"\n@@@@@@ validaClientePideNumeroEmpleado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return pide;
	}
	
	@Deprecated
	@Override
	public boolean validarVentanaDocumentosBloqueada(
			String ntramite
			,String cdtiptra
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ validarVentanaDocumentosBloqueada @@@@@@"
				));
		
		boolean bloqueada = consultasDAO.validarVentanaDocumentosBloqueada(ntramite,cdtiptra,cdusuari,cdsisrol);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ bloqueada=",bloqueada
				,"\n@@@@@@ validarVentanaDocumentosBloqueada @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return bloqueada;
	}
	
	
	public Map<String,String> consultaFeNacContratanteAuto(Map<String,String> params)throws Exception{
		
		Map<String,String> res = null;
		
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ consultaFeNacContratanteAuto @@@@@@"
				,"\n@@@@@@ params=" , params
				));
		
		res = consultasDAO.consultaFeNacContratanteAuto(params);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ res=",res
				,"\n@@@@@@ validaClientePideNumeroEmpleado @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return res;
	}
	
	@Override
	@Deprecated
	public String recuperarTparagen(ParametroGeneral paragen) throws Exception
	{
		return consultasDAO.recuperarTparagen(paragen);
	}
	
	@Deprecated
	@Override
	public Map<String,String> recuperarDatosFlujoEmision(String cdramo, String tipoflot) throws Exception
	{
		return consultasDAO.recuperarDatosFlujoEmision(cdramo,tipoflot);
	}
	
	@Override
	public String recuperarCodigoCustom(String cdpantalla, String cdsisrol) throws Exception
	{
		logger.debug(Utils.log("\n@@@@@@ > recuperarCodigoCustom cdpantalla=", cdpantalla, ", cdsisrol=", cdsisrol));
		
		String codigo = consultasDAO.recuperarCodigoCustom(cdpantalla,cdsisrol);
		
		logger.debug(Utils.log("\n@@@@@@ < recuperarCodigoCustom codigo=", codigo));
		return codigo;
	}
	
	@Override
	public void modificaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol, String swmodifi, String accion) throws Exception{
		consultasDAO.modificaPermisosEdicionCoberturas(cdramo, cdtipsit, cdplan, cdgarant, cdsisrol, swmodifi, accion);
	}
	
	@Override
	public List<Map<String,String>> consultaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol) throws Exception{
		return	consultasDAO.consultaPermisosEdicionCoberturas(cdramo,cdtipsit,cdplan,cdgarant,cdsisrol);
	}
	
	@Override
	@Deprecated
	public String recuperarCdpersonClienteTramite(String ntramite) throws Exception
	{
		return consultasDAO.recuperarCdpersonClienteTramite(ntramite);
	}
	
	@Override
	@Deprecated
	public Map<String,String> recuperarDatosFlujoEndoso(String cdramo, String cdtipsup) throws Exception
	{
		return consultasDAO.recuperarDatosFlujoEndoso(cdramo, cdtipsup);
	}
	
	@Override
	public boolean esTramiteSalud (String ntramite) throws Exception {
		logger.debug(Utils.log(
			"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
			"\n@@@@@@ esTramiteSalud @@@@@@",
			"\n@@@@@@ ntramite = ", ntramite
		));
		boolean esSalud = false;
		String paso = null;
		try {
			paso = "Recuperando datos tr\u00e1mite";
			logger.debug(paso);
			Map<String, String> tramite = siniestrosDAO.obtenerTramiteCompleto(ntramite);
			String cdramo = tramite.get("CDRAMO");
			if (StringUtils.isBlank(cdramo)) {
				throw new ApplicationException("El tr\u00e1mite no tiene producto");
			}
			esSalud = this.esProductoSalud(cdramo);
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
			"\n@@@@@@ esSalud = ", esSalud,
			"\n@@@@@@ esTramiteSalud @@@@@@",
			"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
		));
		return esSalud;
	}
	
	@Override
	public void actualizaFlujoTramite(String ntramite, String cdflujomc, String cdtipflu) throws Exception {
		logger.debug(Utils.log(
			"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
			"\n@@@@@@ actualizaFlujoTramite @@@@@@",
			"\n@@@@@@ ntramite = ", ntramite,
			"\n@@@@@@ cdflujomc = ", cdflujomc,
			"\n@@@@@@ cdtipflu = ", cdtipflu
		));
		String paso = null;
		try {
			paso = "Actualizando datos tr\u00e1mite";
			consultasDAO.actualizaFlujoTramite(ntramite, cdflujomc, cdtipflu);
			paso = "Termino de actualizar datos tr\u00e1mite";
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
	}
	
	public ManagerRespuestaImapVO pantallaTrafudoc(String cdsisrol) throws Exception{
	        ManagerRespuestaImapVO resp = new ManagerRespuestaImapVO(true);
	        String paso = "";
	        //obtener componentes
	        try{
	            paso = "antes de obtener componentes busqueda";
	            List<ComponenteVO>componentesBusqueda = pantallasDAO.obtenerComponentes(
	                    null,null,null,null,null,cdsisrol,"TRAFUDOC_DINAMICA","BUSQUEDA",null);
	            
	            GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
	            
	            gc.generaComponentes(componentesBusqueda, true, false, true, false, false, false);
	            
	            Map<String,Item> imap = new HashMap<String,Item>();
	            resp.setImap(imap);         
	            imap.put("busquedaItems" , gc.getItems());
	        }
	        catch(Exception ex){
	            Utils.generaExcepcion(ex, paso);
	        }
	        return resp;
	}
	
	@Override
	public List<Map<String, String>> obtenerCursorTrafudoc(String cdfunci, String cdramo, String cdtipsit) throws Exception{
	    String paso = "";
	    List<Map<String, String>> lista = new ArrayList<Map<String,String>>();
	    try{
	        paso = "Antes de obtener cursor trafudoc;";
	        lista = consultasDAO.obtenerCursorTrafudoc(cdfunci, cdramo, cdtipsit);
	    }
	    catch(Exception ex){
	        Utils.generaExcepcion(ex, paso);
	    }
	    return lista;
	}
	
	@Deprecated
	@Override
	public List<Map<String,String>>cargarTvalosit (String cdunieco, String cdramo, String estado, String nmpoliza
            ,String nmsuplem)throws Exception {
	    return consultasDAO.cargarTvalosit(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
	}
	
	@Override
	@Deprecated
	public Map<String,String>cargarMpoliperSituac (String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem,
            String nmsituac)throws Exception {
	    return consultasDAO.cargarMpoliperSituac(cdunieco, cdramo, estado, nmpoliza, nmsuplem, nmsituac);
	}
	
	@Override
	public String recuperaAgentePoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdusuari) throws Exception{
	    String paso = "";
	    String cdagente = null;
	    try{
	        paso = "Antes de recuperar agente";
	        cdagente = consultasDAO.recuperaAgentePoliza(cdunieco, cdramo, estado, nmpoliza, cdusuari);
	    }
	    catch(Exception ex){
	        Utils.generaExcepcion(ex, paso);
	    }
	    return cdagente;
	}
	
	@Override
    public String documentosXFamilia(String pv_cdunieco_i,
                                                 String pv_cdramo_i,
                                                 String pv_estado_i,
                                                 String pv_nmpoliza_i, 
                                                 String pv_nmsuplem_i,
                                                 String pv_cdusuari)
    {
        long stamp = System.currentTimeMillis();
        logger.debug(Utils.log(
                 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ,"\n@@@@@@ documentosXFamilia @@@@@@"
                ,"\n@@@@@@ pv_cdunieco_i=" , pv_cdunieco_i
                ,"\n@@@@@@ pv_cdramo_i="   , pv_cdramo_i
                ,"\n@@@@@@ pv_estado_i="   , pv_estado_i
                ,"\n@@@@@@ pv_nmpoliza_i=" , pv_nmpoliza_i
                ,"\n@@@@@@ pv_nmsuplem_i=" , pv_nmsuplem_i
                ,"\n@@@@@@ pv_cdusuari="   , pv_cdusuari
                ));
        
        String resp=null;
        
        try
        {
            resp = consultasDAO.verificaFusFamilia(pv_cdunieco_i
                                                    , pv_cdramo_i
                                                    , pv_estado_i
                                                    , pv_nmpoliza_i
                                                    , pv_nmsuplem_i
                                                    , pv_cdusuari);
        }
        catch(Exception ex)
        {
            logger.error(Utils.join("Error al verificar fusfamilia #"),ex);
        }
        
        logger.debug(Utils.log(
                 "\n@@@@@@ resp=" , resp
                ,"\n@@@@@@ documentosXFamilia @@@@@@"
                ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ));
        return resp;
    }
	
	@Override
    public void ejecutaFusionFam(  String pv_cdunieco_i,
                                   String pv_cdramo_i,
                                   String pv_estado_i,
                                   String pv_nmpoliza_i, 
                                   String pv_nmsuplem_i,
                                   String pv_tipoMov_i,
                                   String pv_cdtiptra_i,
                                   UserVO usuario)
    {
        long stamp = System.currentTimeMillis();
        logger.debug(Utils.log(
                 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ,"\n@@@@@@ ejecutaFusionFam @@@@@@"
                ,"\n@@@@@@ pv_cdunieco_i=" , pv_cdunieco_i
                ,"\n@@@@@@ pv_cdramo_i="   , pv_cdramo_i
                ,"\n@@@@@@ pv_estado_i="   , pv_estado_i
                ,"\n@@@@@@ pv_nmpoliza_i=" , pv_nmpoliza_i
                ,"\n@@@@@@ pv_nmsuplem_i=" , pv_nmsuplem_i
                ));
        
        List<Map<String,String>> titulares=null;
        String paso="";
        try
        {
            paso="Actualizando el estado de tfuslock";
            consultasDAO.actualizaEstadoTFusLock(pv_cdunieco_i, pv_cdramo_i, pv_estado_i, pv_nmpoliza_i, pv_nmsuplem_i, "W");
            
            paso="Obteniendo titulares";
            titulares = consultasDAO.titularesFus( pv_cdunieco_i
                                                 , pv_cdramo_i
                                                 , pv_estado_i
                                                 , pv_nmpoliza_i
                                                 , pv_nmsuplem_i);
            
            for(Map<String,String> tit: titulares){
                List<File>files = new ArrayList<File>();
                // traemos los docs y hacemos un for
                List<Map<String,String>> documentos = consultasDAO.docsXTitular(pv_cdunieco_i
                                                                                , pv_cdramo_i
                                                                                , pv_estado_i
                                                                                , pv_nmpoliza_i
                                                                                , pv_nmsuplem_i
                                                                                , tit.get("nmsitaux"));
                paso="Regenera y descarga documentos";
                
                
                
                try{
                regeneraDocs(documentos, new File(Utils.join(rutaDocumentosTemporal,"/Errores_fusion_familia_",pv_cdunieco_i
                                                                                ,"_", pv_cdramo_i
                                                                                ,"_", pv_estado_i
                                                                                ,"_", pv_nmpoliza_i
                                                                                ,"_", pv_nmsuplem_i
                                                                                ,"_", tit.get("nmsitaux"))), pv_cdramo_i, new AtomicBoolean(false), null, null);
                }catch(Exception e){
                    logger.equals("Error regenerando docs");
                }
                logger.debug(Utils.log("Titular: ",tit,
                                       "\nDocumentos: ",documentos));
                if(documentos.isEmpty()){
                    logger.warn("No hay documentos que fusionar");
                    continue;
                }
                paso="Ordenando documentos ";
                logger.debug(Utils.join("Documentos sin ordenar: ",documentos));
                
                Collections.sort(documentos,new Comparator<Map<String,String>>() {
                    
                    
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        String dsdocume1=o1.get("dsdocume");
                        String dsdocume2=o2.get("dsdocume");
                        
                        if(dsdocume1==null)
                            return -1;
                        
                        String []tipos=new String[]{"C","RCL","CB","OS","OSE","IM","IME","CR","OTRO"};
                        List<String> lTipos=Arrays.asList(tipos);
                       
                        
                        if( lTipos.indexOf(dsdocume1)>lTipos.indexOf(dsdocume2)){
                            return 1;
                        }else if(lTipos.indexOf(dsdocume1)==lTipos.indexOf(dsdocume2)){
                            return 0;
                        }
                        return -1;
                    }
                    
                    private String tipo(String dsdocume){
                        if(dsdocume.contains("CERTIFICADO") || dsdocume.matches("^.*CAR.TULA.*$")){
                            return "C";
                        }else if(dsdocume.matches("(?i)^.*Relaci.n.*COBERTURAS.*L.MITES.*")){
                            return "RCL";
                        }else if(dsdocume.contains("CARTA")){
                            return "CB";
                        }else if(dsdocume.matches("^.*ORDEN.*SERVICIO((?!(ESPECIALISTA)).)*$")){
                            return "OS";
                        }else if(dsdocume.matches("^.*ORDEN.*SERVICIO.*ESPECIALISTA.*$")){
                            return "OSE";
                        }else if(dsdocume.matches("^.*INFORME.*M.DICO((?!(ESPECIALISTA)).)*$")){
                            return "IM";
                        }else if(dsdocume.matches("^.*INFORME.*M.DICO.*ESPECIALISTA.*$")){
                            return "IME";
                        }else if(dsdocume.contains("CREDENCIAL")){
                            return "CR";
                        }else{
                            return "OTRO";
                        }
                    }
                    
                });
                logger.debug(Utils.join("Documentos ordenados: ",documentos));
                String ntramite = "";
                for(Map<String,String> archivo:documentos){
                    
                    ntramite = archivo.get("ntramite");
                    String cddocume = archivo.get("cddocume");
                    String dsdocume = archivo.get("dsdocume");
                    String filePath = Utils.join(rutaDocumentosPoliza,"/",ntramite,"/",cddocume);
                    String nmpoliza = archivo.get("nmpoliza");
                    logger.debug(Utils.log("\ntramite,archivo=",ntramite,",",cddocume));
                    
                    if(cddocume.toLowerCase().indexOf("://")!=-1)
                    {
                        paso = "Descargando archivo remoto";
                        cddocume=cddocume.replaceAll("\\s", "").trim();
                        logger.debug(paso);
                        long timestamp = System.currentTimeMillis();
                        long rand      = new Double(1000d*Math.random()).longValue();
                        filePath       = Utils.join(
                                rutaDocumentosTemporal
                                ,"/poliza_"    , archivo.get("nmpoliza")
                                ,"nmsupleme_"  , archivo.get("nmsupleme")
                                ,"_dsdocume_"  , archivo.get("dsdocume")
                                ,"_tramite_" , ntramite
                                ,"_t_"       , timestamp , "_" , rand
                                ,".pdf"
                                );
                        
                        
                        
                        File local = new File(filePath);
                        
                        try{
                            InputStream remoto = HttpUtil.obtenInputStream(cddocume.replace("https","http").replace("HTTPS","HTTP"));
                            FileUtils.copyInputStreamToFile(remoto, local);
                        }catch(ConnectException ex){
                            logger.error("Error al descargar documento: ",ex);
                            
                        }catch(Exception ex){
                            logger.error("Error al descargar documento: ",ex);
                        }
                    }
                    files.add(new File(filePath));
                }
                paso="Obteniendo cdideext";
                String nmsituaext="-";
               try{
                   
                    nmsituaext= consultasDAO.obtieneNmsituaext(pv_cdunieco_i, pv_cdramo_i, pv_estado_i, pv_nmpoliza_i, pv_nmsuplem_i, tit.get("nmsitaux"));
                    logger.debug("Respuesta obtieneNmsituaext: "+nmsituaext);
                    
               }catch(Exception e){
                   logger.error("Error obteniendo nmsituaext");
               }
               paso="Fusionando documentos";
                String nombreArchivo=Utils.join(
                        rutaDocumentosPoliza
                        ,"/",ntramite
                        ,"/F_"//,tit.get("nmsitaux"),"_" //quitar nmsitaux y abajo tambien
                        ,pv_cdunieco_i
                        ,"-",pv_cdramo_i
                        ,"-",pv_nmpoliza_i
                        ,"-",StringUtils.leftPad(nmsituaext, 10, "0")
                        ,".pdf"
                        );
                
                File fusionado = DocumentosUtils.mixPdf(files,new File(nombreArchivo));
                
                paso="guardando en tdocupolfus";
                consultasDAO.movTdocupolFus(pv_cdunieco_i, pv_cdramo_i, pv_estado_i, pv_nmpoliza_i, pv_nmsuplem_i, null, ntramite, new Date()
                        , Utils.join("F_",pv_cdunieco_i
                        ,"-",pv_cdramo_i
                        ,"-",pv_nmpoliza_i
                        ,"-",StringUtils.leftPad(nmsituaext, 10, "0")
                        ,".pdf")
                        , Utils.join(" ",pv_cdunieco_i
                                ,"-",pv_cdramo_i
                                ,"-",pv_nmpoliza_i
                                ,"-",StringUtils.leftPad(nmsituaext, 10, "0")
                                ,"")
                        , pv_tipoMov_i
                        , null
                        , pv_cdtiptra_i
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , usuario.getUser()
                        , usuario.getRolActivo().getClave());
                consultasDAO.actualizaEstadoTFusLock(pv_cdunieco_i, pv_cdramo_i, pv_estado_i, pv_nmpoliza_i, pv_nmsuplem_i, "F");
                
                        

            }
            consultasDAO.actualizaEstadoTFusLock(pv_cdunieco_i, pv_cdramo_i, pv_estado_i, pv_nmpoliza_i, pv_nmsuplem_i, "F");
            
            String [] to    = new String[]{usuario.getEmail()};
            String asunto   = "FUSIONAR POR TITULAR TERMINADO";
            String mensaje  = Utils.join(
                                            "Estimado(a): ",usuario.getName()
                                           ,"<br /><br />"
                                           ,"Le notificamos que la fusi&oacute;n de documentos por titulares de la p&oacute;liza: "
                                           ,pv_nmpoliza_i
                                           ,!pv_nmsuplem_i.trim().equals("0")?" y endoso: "+pv_nmsuplem_i:""
                                           ," ha finalizado."
                                           ,"<br /><br />Saludos cordiales."
                                        );
            //consultasDAO.actualizaEstadoTFusLock(pv_cdunieco_i, pv_cdramo_i, pv_estado_i, pv_nmpoliza_i, pv_nmsuplem_i, "F");
            mailService.enviaCorreo(to, null, null, asunto, mensaje, new String[0], true);
            
            
        }
        catch(Exception ex)
        {
            logger.error(Utils.join("Error al verificar fusfamilia #"),ex);
        }
        
        logger.debug(Utils.log(
                 "\n@@@@@@ "
                ,"\n@@@@@@ ejecutaFusionFam @@@@@@"
                ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ));
       
    }
	
    public boolean copiarArchivosRenovacionColectivo(String cduniecoOrigen, String cdramoOrigen, String estadoOrigen, 
            String nmpolizaOrigen, String ntramiteDestino, String rutaDocumentos)throws Exception{
        logger.debug("copiarArchivosRenovacionColectivo =====> ");
        //copiar documentos de usuario
        List<Map<String,String>>iPolizaUserDocs=renovacionDAO.cargarDocumentosSubidosPorUsuario(cduniecoOrigen,cdramoOrigen,estadoOrigen,nmpolizaOrigen);
        for(Map<String,String>iUserDoc:iPolizaUserDocs)
        {
            String ntramiteAnt = iUserDoc.get("ntramite");
            String cddocume    = iUserDoc.get("cddocume");
            
            File carpeta=new File(rutaDocumentos + "/" + ntramiteDestino);
            logger.debug("Valor de la carpeta ==> "+carpeta);
            
            if(!carpeta.exists()){
                logger.debug("no existe la carpeta::: "+ntramiteDestino);
                carpeta.mkdir();
                if(carpeta.exists()){
                    logger.debug("carpeta creada");
                } else {
                    logger.debug("carpeta NO creada");
                }
            } else {
                logger.debug("existe la carpeta::: "+ntramiteDestino);
            }
            
            File doc = new File(
                    new StringBuilder(rutaDocumentos)
                    .append("/")
                    .append(ntramiteAnt)
                    .append("/")
                    .append(cddocume)
                    .toString()
                    );
            
            logger.debug("Valor doc ==>"+doc);
            File newDoc = new File(
                    new StringBuilder(rutaDocumentos)
                    .append("/")
                    .append(ntramiteDestino)
                    .append("/")
                    .append(cddocume)
                    .toString()
                    );
            
            logger.debug("Valor newDoc ==>"+newDoc);
            if(doc!=null&&doc.exists())
            {
                try
                {
                    FileUtils.copyFile(doc, newDoc);
                }
                catch(Exception ex)
                {
                    logger.error("Error copiando archivo de usuario",ex);
                }
            }
            else
            {
                renovacionDAO.eliminacionRegistros(ntramiteDestino,cddocume);
                logger.error(new StringBuilder("No existe el documento").append(doc).toString());
            }
        }        
        /*consultasDAO.copiaDocumentosTdocupol(cduniecoOrigen, cdramoOrigen, estadoOrigen, 
                nmpolizaOrigen, ntramiteDestino);*/
        
        return true;
    }
    
    
    
    /**
     * Recibe una lista de documentos para regenerarlos en el caso de que no existan o esten corruptos
     * 
     * @param documentos Documentos a verificar, si no existen o estan corruptos se regeneraran
     * @param archivoErrrores  Archivo de errores con la lista de documentos que se intentaron regenerar y fallaron
     * @param cdtipram Tipo de Ramo
     * @param hayErrores Indica si se escribieron errores en el archivo de errores
     * @param lote      Numero de lote (opcional)
     * @param cdusuari  Usuario que ejecuta el proceso (opcional)
     * @return Lista de archivos correctos (ya existentes y regenerados exitosamente)
     * @throws Exception
     */
    private List<Map<String, String>> regeneraDocs(List<Map<String, String>> documentos, File archivoErrrores,
            String cdramo, AtomicBoolean hayErrores, String lote, String cdusuari) throws Exception {
        
        logger.debug(Utils.join("@@@@@@@@@@@@@@@@@@@@@@@@"
                               ,"@@@@@ regeneraDocs"
                               ,"@@@@@ documentos = ", documentos
                              , "@@@@@ cdramo = ", cdramo));
        List<Map<String, String>> docsExisten = new ArrayList<Map<String, String>>();
        try{
    
            descargaUrl(documentos);
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoErrrores, true));
            bw.append("POLIZA,DOCUMENTO,DESCRIPCION\r\n");
            // bw.close();
    
            for (Map<String, String> archivo : documentos) {
    
                String ntramite = archivo.get("ntramite");
                String cddocume = archivo.get("cddocume");
                String dsdocume = archivo.get("dsdocume");
                String nmpoliza = archivo.get("nmpoliza");
                String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
                boolean existe = true;
                logger.debug(Utils.join("\ntramite,archivo=", ntramite, ",", cddocume));
    
                if (cddocume.toLowerCase().indexOf("://") != -1) {
    
                    logger.debug("Usando archivo local descargado");
    
                    filePath = archivo.get("descargadoEn");
    
                    if (StringUtils.isBlank(filePath)) {
    
                        logger.error(Utils.join("Error en descarga-: ", cddocume));
                        continue;
                    }
    
                }
                File f = new File(filePath);
                logger.debug("Verificando si existe el archivo");
    
                if (!f.exists() || !DocumentosUtils.verificaPDF(f)) {
    
                    if (consultasDAO.esProductoSalud(cdramo)) {
    
                        String dirTramite = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/");
                        logger.debug(Utils.join("\n@@@@ Creando directorio : ", dirTramite));
                        File dir = new File(dirTramite);
                        dir.mkdirs();
                        logger.debug("Regenerando Docs");
                        mesaControlDAO.regeneraRemesaReport(ntramite, cddocume);
                    }
                    
                    if(lote!= null && cdusuari!=null && nombreRemesaPdf.equals(cddocume)){
                        try{
                                String urlReporteCotizacion = Utils.join(
                                        rutaServidorReports
                                      , "?p_lote="     , lote
                                      , "&p_usr_imp="  , cdusuari
                                      , "&p_ntramite=" , ntramite
                                      , "&destype=cache"
                                      , "&desformat=PDF"
                                      , "&userid="        , passServidorReports
                                      , "&ACCESSIBLE=YES"
                                      , "&report="        , nombreReporteRemesa
                                      , "&paramform=no"
                                      );
                              
                              String pathRemesa=Utils.join(
                                      rutaDocumentosPoliza
                                      ,"/",ntramite
                                      ,"/",nombreRemesaPdf
                                      );
                              
                              HttpUtil.generaArchivo(urlReporteCotizacion, pathRemesa);
                        }catch(Exception e){
                            logger.debug(Utils.join("Error tratando de regenerar remesa lote:",lote," tramite: ",ntramite));
                        }
                    }
                    
                    
    
                    f = new File(filePath);
    
                    if (!f.exists() || !DocumentosUtils.verificaPDF(f)) {
    
                        logger.error(Utils.join("\n@@@@No existe el archivo: ", filePath, "\n", archivo));
                        existe = false;
    
                    }
    
                }
    
                if (!existe) {
                    logger.debug("Escribiendo errores");
                    bw.append(Utils.join(nmpoliza == null ? "" : nmpoliza, ",\"", cddocume, "\"", ",\"", dsdocume, "\"",
                            "\r\n"));
    
                    hayErrores.set(true);
    
                } else {
    
                    docsExisten.add(archivo);
                }
    
            }
            bw.close();
        }catch(Exception e){
            Utils.generaExcepcion(e, "Error regenerando documentos");
        }
        
        logger.debug(Utils.log(
                
                "\n@@@@@@ regeneraDocs @@@@@@"
               ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ));

        return docsExisten;
    }   
    
    /**
     * Descarga si hay archivos remotos en la lista y agrega un campo
     * [descargadoEn] que contiene la ruta del archivo local ya descargado.
     * 
     * @param original
     */
    private void descargaUrl(List<Map<String, String>> original) {
        
        String paso = "";
        
        logger.debug(Utils.join("\nLista de entrada a descargaUrl: ", original));

        for (Map<String, String> archivo : original) {
            try {
                String ntramite = archivo.get("ntramite");
                String cddocume = archivo.get("cddocume");
                String dsdocume = archivo.get("dsdocume");
                String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
                String nmpoliza = archivo.get("nmpoliza");

                logger.debug(Utils.log("\ntramite,archivo=", ntramite, ",", cddocume));

                if (cddocume.toLowerCase().indexOf("://") != -1) {
                    
                    paso = "Descargando archivo remoto";
                    cddocume = cddocume.replaceAll("\\s", "").trim();
                    logger.debug(paso);
                    long timestamp = System.currentTimeMillis();
                    long rand = new Double(1000d * Math.random()).longValue();
                    filePath = Utils.join(rutaDocumentosTemporal, "/Descargado", "_remesa_", archivo.get("remesa"),
                            "_tramite_", ntramite, "_t_", timestamp, "_", rand, ".pdf");

                    archivo.put("descargadoEn", filePath);

                    File local = new File(filePath);

                    try {
                        paso = "guardando en local";
                        InputStream remoto = HttpUtil
                                .obtenInputStream(cddocume.replace("https", "http").replace("HTTPS", "HTTP"));
                        FileUtils.copyInputStreamToFile(remoto, local);
                        logger.debug(Utils.join("\nArchivo remoto: ", cddocume, " Archivo local: ", filePath));
                    } catch (ConnectException ex) {
                        logger.error(Utils.join("Error al descargar documento : ", cddocume), ex);

                    } catch (Exception ex) {
                        logger.error("Error en descargaUrl: ", ex);
                    }
                }
            } catch (Exception e) {
                logger.error("Error descargando url");

            }

           
        }

    }

    @Override
    public String recuperarDstipsupPorCdtipsup(String cdtipsup) throws Exception{
        return consultasDAO.recuperarDstipsupPorCdtipsup(cdtipsup);
    }
    
    @Override
    public String recuperarTramitePorNmsuplem(
            String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            ,String nmsuplem
            )throws Exception{
        return consultasDAO.recuperarTramitePorNmsuplem(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
                
    }
    
    @Override
    public List<Map<String,String>> obtieneRangoPeriodoGracia(String pv_cdramo_i, String pv_cdtipsit_i, String pv_cdagente_i)
    		throws Exception{
        return consultasDAO.obtieneRangoPeriodoGracia(pv_cdramo_i, pv_cdtipsit_i, pv_cdagente_i);
                
    }

    @Override
    public boolean esDxn(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception{
        String paso  = "";
        String esDxn = "";
        boolean esDXN = false;
        try{
            paso = "Antes de consultar si poliza es DXN";
            esDxn = consultasDAO.esDXN(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
            
            logger.debug("<<<>>> ES DXN : "+esDxn);
            if(esDxn.equals("S")){
                esDXN = true;
            }
            else{
                esDXN = false;
            }
        }
        catch(Exception ex){
            Utils.generaExcepcion(ex, paso);
        }
        return esDXN;
    }
}