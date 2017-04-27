package mx.com.gseguros.portal.rehabilitacion.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.portal.cancelacion.service.CancelacionManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.rehabilitacion.service.RehabilitacionManager;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.ws.ice2sigs.service.Ice2sigsService;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class RehabilitacionAction extends PrincipalCoreAction
{

	private static final long serialVersionUID  = 5306848466443116067L;
	private static org.apache.log4j.Logger log  = org.apache.log4j.Logger.getLogger(RehabilitacionAction.class);
	private final SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	private boolean exito           = false;
	private boolean success;
	private String  respuesta;
	private String  respuestaOculta = null;
	
	private Map<String,String>       smap1;
	private List<Map<String,String>> slist1;
	private RehabilitacionManager    rehabilitacionManager;
	private Map<String,Item>         imap;
	private PantallasManager         pantallasManager;
	private CancelacionManager       cancelacionManager;
	private transient Ice2sigsService ice2sigsService;
	

	@Autowired
	private DocumentosManager documentosManager;
	
	
	/////////////////////////////
	////// marco principal //////
	/*/////////////////////////*/
	public String marco()
	{
		log.debug(""
				+ "\n#####################################"
				+ "\n#####################################"
				+ "\n###### marco de rehabilitacion ######"
				+ "\n######                         ######"
				);
		try
		{

			imap=new HashMap<String,Item>(0);
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"MARCOREHABILITACION", "FILTRO", null));
			
			imap.put("itemsFiltro" , gc.getItems());
			
			gc.generaParcial(pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"MARCOREHABILITACION", "MODELO", null));
			
			imap.put("fieldsModelo" , gc.getFields());
			imap.put("columnsGrid"  , gc.getColumns());
			
			success=true;
		}
		catch(Exception ex)
		{
			log.error("error al cargar el marco de endosos",ex);
			success=false;
		}
		log.debug(""
				+ "\n######                         ######"
				+ "\n###### marco de rehabilitacion ######"
				+ "\n#####################################"
				+ "\n#####################################"
				);
		return SUCCESS;
	}
	/*/////////////////////////*/
	////// marco principal //////
	/////////////////////////////
	
	/////////////////////////////////////////////
	////// buscar polizas para rehabilitar //////
	/*/////////////////////////////////////////*/
	public String buscarPolizas()
	{
		log.debug(""
				+ "\n#######################################"
				+ "\n#######################################"
				+ "\n###### buscar polizas canceladas ######"
				+ "\n######                           ######"
				);
		log.debug("smap1: "+smap1);
		try
		{
			slist1=rehabilitacionManager.buscarPolizas(smap1);
			success=true;
		}
		catch(Exception ex)
		{
			log.error("error al obtener polizas",ex);
			slist1=null;
			success=false;
		}
		log.debug(""
				+ "\n######                           ######"
				+ "\n###### buscar polizas canceladas ######"
				+ "\n#######################################"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	/*/////////////////////////////////////////*/
	////// buscar polizas para rehabilitar //////
	/////////////////////////////////////////////
	
	//////////////////////////////////////////////
	////// pantalla de rehabilitacion unica //////
	/*
	smap1:
		ASEGURADO: "OCTAVIO  CORTINA  CEREZO"
		ASEGURADORA: "SALUD MATRIZ"
		CDELEMENTO: "6442"
		CDMONEDA: "001"
		CDPERSON: "512150"
		CDRAMO: "2"
		CDRAZON: "22"
		CDUNIECO: "1000"
		COMENTARIOS: "POR REQUERIR OTRO PRODUCTO"
		DSELEMEN: "GENERAL DE SEGUROS"
		DSRAZON: "A solicitud del Asegurado"
		ESTADO: "M"
		FECANCEL: "15/04/2014"
		FEEFECTO: "15/10/2013"
		FEVENCIM: "15/10/2014"
		NMCANCEL: "1"
		NMPOLIZA: "63"
		NMSITUAC: "1"
		NMSUPLEM: "245676312000000000"
		PRODUCTO: "SALUD VITAL" 
	*/
	/*//////////////////////////////////////////*/
	public String pantallaRehabilitacionUnica()
	{
		log.debug(""
				+ "\n###########################################"
				+ "\n###########################################"
				+ "\n###### pantalla rehabilitacion unica ######"
				+ "\n######                               ######"
				);
		log.debug("smap1: "+smap1);
		try
		{
			imap=new HashMap<String,Item>(0);
		
			if(smap1 != null) {
				smap1.put("COMENTARIOS", "");
			}
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			List<ComponenteVO>componentes = pantallasManager.obtenerComponentes(
					null, null, null,
					null, null, null,
					"PANTALLAREHABILITARUNICA", "FORM", null); 
			
			ComponenteVO preguntaAntiguedad = null;
			ComponenteVO algunoAntiguo      = null;
			
			for(ComponenteVO comIte : componentes)
			{
				if(comIte.getCatalogo()!=null&&comIte.getCatalogo().equalsIgnoreCase("SINO"))
				{
					preguntaAntiguedad = comIte;
				}
				if(comIte.getLabel()!=null&&comIte.getLabel().equalsIgnoreCase("ALGUNOANTIGUO"))
				{
					algunoAntiguo = comIte;
				}
			}
			
			String cdunieco = smap1.get("CDUNIECO");
			String cdramo   = smap1.get("CDRAMO");
			String estado   = smap1.get("ESTADO");
			String nmpoliza = smap1.get("NMPOLIZA");
			
			boolean algunoAntiguedad = rehabilitacionManager.validaAntiguedad(cdunieco, cdramo, estado, nmpoliza);
			
			if(algunoAntiguedad)
			{
				preguntaAntiguedad.setOculto(false);
				algunoAntiguo.setValue("'S'");
			}
			else
			{
				preguntaAntiguedad.setOculto(true);
				algunoAntiguo.setValue("'N'");
			}
			
			gc.generaParcial(componentes);
			
			imap.put("itemsForm" , gc.getItems());
		}
		catch(Exception ex)
		{
			log.error("error al mostrar pantalla de rehabilitacion unica",ex);
		}
		log.debug(""
				+ "\n######                               ######"
				+ "\n###### pantalla rehabilitacion unica ######"
				+ "\n###########################################"
				+ "\n###########################################"
				);
		return SUCCESS;
	}
	/*//////////////////////////////////////////*/
	////// pantalla de rehabilitacion unica //////
	//////////////////////////////////////////////
	
	/////////////////////////////////
	////// rehabilitacionUnica //////
	/*
	smap1.pv_cdunieco_i:1000
	smap1.pv_cdramo_i:2
	smap1.pv_estado_i:M
	smap1.pv_nmpoliza_i:58
	smap1.pv_feefecto_i:18/12/2013
	smap1.pv_fevencim_i:18/12/2014
	smap1.pv_fecancel_i:07/01/2014
	smap1.pv_nmcancel_i:2
	smap1.pv_cdrazon_i:9
	smap1.inutil:Cacelacion por Falta de Pago
	smap1.pv_cdperson_i:511754
	smap1.pv_cdmoneda_i:001
	smap1.pv_fereinst_i:24/02/2014
	smap1.pv_sino_i:N
	smap1.pv_comments_i:SD
	smap1.pv_nmsuplem_i:245666520230000000
	smap1.pv_algunoantiguo_i:S
	*/
	/*/////////////////////////////*/
	public String rehabilitacionUnica()
	{
		log.debug(""
				+ "\n##########################################"
				+ "\n##########################################"
				+ "\n###### rehabilitacion unica proceso ######"
				+ "\n######                              ######"
				+ "\nsmap1: "+smap1);
		success = true;
		try
		{
			boolean algunAntiguo   = smap1.get("pv_algunoantiguo_i")!=null && smap1.get("pv_algunoantiguo_i").equalsIgnoreCase("S");
			boolean seConservaAnti = smap1.get("pv_sino_i")         !=null && smap1.get("pv_sino_i")         .equalsIgnoreCase("S");
			String  cdunieco       = smap1.get("pv_cdunieco_i");
			String  cdramo         = smap1.get("pv_cdramo_i");
			String  estado         = smap1.get("pv_estado_i");
			String  nmpoliza       = smap1.get("pv_nmpoliza_i");
			String  fereinst       = smap1.get("pv_fereinst_i");
			UserVO  usuario        = (UserVO)session.get("USUARIO");
			String  cdusuari       = usuario.getUser();
			String  cdsisrol       = usuario.getRolActivo().getClave();
			
			exito = true;
			
			Map<String,Object> resRehab = null;
			try
			{
				resRehab = rehabilitacionManager.rehabilitarPoliza(smap1,cdusuari,cdsisrol);
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = ex.getMessage();
				respuestaOculta = ex.getMessage();
				log.error("Error al rehabilitar #"+timestamp,ex);
			}
			
			if(exito)
			{
				String nmsuplemRehab = (String)resRehab.get("pv_nmsuplem_o");
				
				if(algunAntiguo && !seConservaAnti)
				{
					rehabilitacionManager.borraAntiguedad(cdunieco, cdramo, estado, nmpoliza, nmsuplemRehab, fereinst);
				}
				
				String cdtipsup = TipoEndoso.REHABILITACION.getCdTipSup().toString();
				
				String ntramite = null;
				String nmsolici = null;
				
				Map<String,String> result = documentosManager.generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,"0" //nmsituac
						,nmsuplemRehab
						,DocumentosManager.PROCESO_ENDOSO
						,ntramite
						,nmsolici
						,null
						);
				
				ntramite = result.get("ntramite");
				nmsolici = result.get("nmsolici");
				
				String sucursal = cdunieco;
				if(StringUtils.isNotBlank(sucursal) && "1".equals(sucursal)) sucursal = "1000";
				
				// Ejecutamos el Web Service de Recibos:
				ice2sigsService.ejecutaWSrecibos(cdunieco, cdramo, 
						estado, nmpoliza, 
						nmsuplemRehab, null, 
						sucursal, "", ntramite, 
						true, cdtipsup, 
						(UserVO) session.get("USUARIO"));
			}
		}
		catch(Exception ex)
		{
			long timestamp  = System.currentTimeMillis();
			exito           = false; 
			respuesta       = "Error al rehabilitar #"+timestamp;
			respuestaOculta = ex.getMessage();
			log.error(respuesta,ex);
		}
		log.debug(""
				+ "\n######                              ######"
				+ "\n###### rehabilitacion unica proceso ######"
				+ "\n##########################################"
				+ "\n##########################################"
				);
		return SUCCESS;
	}
	/*/////////////////////////////*/
	////// rehabilitacionUnica //////
	/////////////////////////////////
	
	
	/////////////////////////////////
	////// getters and setters //////
	/*/////////////////////////////*/
	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setRehabilitacionManager(RehabilitacionManager rehabilitacionManager) {
		this.rehabilitacionManager = rehabilitacionManager;
	}

	public Map<String, Item> getImap() {
		return imap;
	}

	public void setImap(Map<String, Item> imap) {
		this.imap = imap;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public void setCancelacionManager(CancelacionManager cancelacionManager) {
		this.cancelacionManager = cancelacionManager;
	}

	public void setIce2sigsService(Ice2sigsService ice2sigsService) {
		this.ice2sigsService = ice2sigsService;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRespuestaOculta() {
		return respuestaOculta;
	}

	public void setRespuestaOculta(String respuestaOculta) {
		this.respuestaOculta = respuestaOculta;
	}

}