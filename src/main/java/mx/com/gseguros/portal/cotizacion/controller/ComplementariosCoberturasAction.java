package mx.com.gseguros.portal.cotizacion.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.kernel.service.KernelManagerSustituto;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.service.ConsultasManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.endosos.service.EndososManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.service.PantallasManager;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.TipoEndoso;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.service.MesaControlManager;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

public class ComplementariosCoberturasAction extends PrincipalCoreAction {

	private static final long serialVersionUID = 1197781376427981845L;
	
	private KernelManagerSustituto kernelManager;
	private PantallasManager       pantallasManager;
	private EndososManager         endososManager;
	private Map<String,String>smap1;
	private Map<String,String>smap2;
	private Map<String,String>smap3;
	private Map<String,Object>omap1;
	private Map<String,Object>omap2;
	private Map<String,Object>omap3;
	private List<Map<String,String>>slist1;
	private List<Map<String,String>>slist2;
	private List<Map<String,Object>>olist1;
	private List<Map<String,Object>>olist2;
	private Item item1;
	private Item item2;
	private Item item3;
	private Item item4;
	private Map<String,String> spanel1;
	private Map<String,String> spanel2;
	private Map<String,String> spanel3;
	private Map<String,Object> opanel1;
	private Map<String,Object> opanel2;
	private Map<String,Object> opanel3;
	private final static Logger logger = LoggerFactory.getLogger(ComplementariosCoberturasAction.class);
	private boolean success;
	private boolean exito  =false;
	private Map<String,String>parametros;
	private String str1;
	private String str2;
	private String respuesta;
	private String respuestaOculta;
	
	@Autowired
	private MesaControlManager mesaControlManager;
	
	@Autowired
	private ConsultasPolizaManager consultasPolizaManager;
	
	@Autowired
	private ConsultasManager consultasManager;
	
	public String pantallaCoberturas()
	{
		logger.debug("smap1: "+smap1);
		ScreenInterceptor scrInt=new ScreenInterceptor();
		return scrInt.intercept(this, ScreenInterceptor.PANTALLA_COMPLEMENTARIOS_COBERTURAS_ASEGURADO);
	}
	
	public String mostrarPantallaCoberturas()
	{
		try
		{
			//modelo para cargar datos
			item1=new Item("fields",null,Item.ARR);
			item1.add(Item.crear(null,null,Item.OBJ).add("name","spanel1.asegurado"));
			item1.add(Item.crear(null,null,Item.OBJ).add("name","spanel1.producto"));
			
			//campos de formulario
			item2=new Item("items",null,Item.ARR);
			item2.add(Item.crear(null,null,Item.OBJ)
					.add(Item.crear("xtype",			"textfield"))
					.add(Item.crear("readOnly",			true))
					.add(Item.crear("name",				"spanel1.asegurado"))
					.add(Item.crear("fieldLabel",		"Asegurado"))
					.add(Item.crear("style",		    "margin:5px;"))
					);
			item2.add(Item.crear(null,null,Item.OBJ)
					.add(Item.crear("xtype",			"textfield"))
					.add(Item.crear("readOnly",			true))
					.add(Item.crear("name",				"spanel1.producto"))
					.add(Item.crear("fieldLabel",		"Producto"))
					.add(Item.crear("style",		    "margin:5px;"))
					);
		}
		catch(Exception ex)
		{
			logger.error("error al armar la pantalla de coberturas",ex);
			item1=null;
			item2=null;
		}
		return SUCCESS;
	}
	
	public String cargarPantallaCoberturas() 
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarPantallaCoberturas @@@@@@"
				,"\n@@@@@@ smap1=" , smap1, "\n",slist1
				));
		try
		{
			/*
			PL:
			pv_cdunieco_i   input
			pv_cdramo_i     input
			pv_estado_i     input
			pv_nmpoliza_i   input
			pv_nmsituac_i   input
			*/
			slist1=kernelManager.obtenerCoberturasUsuario(smap1);
			success=true;
		}
		catch(Exception ex)
		{
			spanel1=null;
			logger.error("error al cargar la pantalla de coberturas",ex);
			success=false;
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ slist1=",slist1
			    ,"\n@@@@@@ cargarPantallaCoberturas @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return SUCCESS;
	}
	
	public String guardarCoberturasUsuario()
	{
		logger.debug("smap1: {}", smap1);
		logger.debug("slist1: {}", Utils.log(slist1));
		try
		{
			int i=1;
			for(Map<String,String> cob:slist1)
			{
				/**poligar
				PL:
				pv_cdunieco_i   smap1 ready!
	            pv_cdramo_i     smap1 ready!
				pv_estado_i     smap1 ready!
				pv_nmpoliza_i   smap1 ready!
				pv_nmsituac_i   smap1 ready!
				pv_cdgarant_i   iterado GARANTIA
				pv_nmsuplem_i   #0
				pv_cdcapita_i   iterado CDCAPITA
				pv_status_i     iterado status
				pv_cdtipbca_i   iterado cdtipbca
				pv_ptvalbas_i   iterado ptvalbas
				pv_swmanual_i   iterado swmanual
				pv_swreas_i     iterado swreas
				pv_cdagrupa_i   iterado cdagrupa
				PV_ACCION       #I
				*
				Map<String,String>mapPoligarIterado=new HashMap<String,String>(0);
				mapPoligarIterado.putAll(smap1);
				mapPoligarIterado.put("pv_cdgarant_i", cob.get("GARANTIA"));
				mapPoligarIterado.put("pv_nmsuplem_i", "0");
				mapPoligarIterado.put("pv_cdcapita_i", cob.get("CDCAPITA"));
				mapPoligarIterado.put("pv_status_i",   cob.get("status"));
				mapPoligarIterado.put("pv_cdtipbca_i", cob.get("cdtipbca"));
				mapPoligarIterado.put("pv_ptvalbas_i", cob.get("ptvalbas"));
				mapPoligarIterado.put("pv_swmanual_i", cob.get("swmanual"));
				mapPoligarIterado.put("pv_swreas_i",   cob.get("swreas"));
				mapPoligarIterado.put("pv_cdagrupa_i", cob.get("cdagrupa"));
				mapPoligarIterado.put("PV_ACCION",     "I");
				logger.debug("Iteracion #"+i+" de movPoligar");
				kernelManager.movPoligar(mapPoligarIterado);
				/**/
				
				/**policap
				PL:
				pv_cdunieco_i   smap1 ready!
	            pv_cdramo_i     smap1 ready!
	            pv_estado_i     smap1 ready!
	            pv_nmpoliza_i   smap1 ready!
	            pv_nmsituac_i   smap1 ready!
	            pv_cdcapita_i   iterado CDCAPITA
	            pv_nmsuplem_i   #0
	            pv_status_i     iterado status
	            pv_ptcapita_i   SOLO NUMERICO iterado SUMA_ASEGURADA
	            pv_ptreduci_i   iterado ptreduci
	            pv_fereduci_i   iterado fereduci
	            pv_swrevalo_i   iterado swrevalo
	            pv_cdagrupa_i   iterado cdagrupa
	            pv_accion_i     #I
				*/
				Map<String,String>mapPolicapIterado=new HashMap<String,String>(0);
				mapPolicapIterado.putAll(smap1);
				mapPolicapIterado.put("pv_cdcapita_i", cob.get("CDCAPITA"));
				mapPolicapIterado.put("pv_nmsuplem_i", "0");
				mapPolicapIterado.put("pv_status_i", cob.get("status"));
				String ptcapita;
				try
				{
					Double aux=Double.parseDouble(cob.get("SUMA_ASEGURADA"));
					ptcapita=aux.toString();
				}
				catch(Exception ex)
				{
					ptcapita=null;
				}
				mapPolicapIterado.put("pv_ptcapita_i",ptcapita);
				mapPolicapIterado.put("pv_ptreduci_i", cob.get("ptreduci"));
				mapPolicapIterado.put("pv_fereduci_i", cob.get("fereduci"));
				mapPolicapIterado.put("pv_swrevalo_i", cob.get("swrevalo"));
				mapPolicapIterado.put("pv_cdagrupa_i", cob.get("cdagrupa"));
				mapPolicapIterado.put("pv_accion_i", "I");
				logger.debug("Iteracion #"+i+" de movPolicap");
				kernelManager.movPolicap(mapPolicapIterado);
				
				i++;
			}
			
			//////////////////////////////////////////////////////
			////// borrar coberturas que vienen en la lista //////
			////// de borrar slist2                         //////
			/*//////////////////////////////////////////////////*/
			i=1;
			for(Map<String,String> cob:slist2)
			{
				/**poligar
				PL:
				pv_cdunieco_i   smap1 ready!
	            pv_cdramo_i     smap1 ready!
				pv_estado_i     smap1 ready!
				pv_nmpoliza_i   smap1 ready!
				pv_nmsituac_i   smap1 ready!
				pv_cdgarant_i   iterado GARANTIA
				pv_nmsuplem_i   #0
				pv_cdcapita_i   iterado CDCAPITA
				pv_status_i     iterado status
				pv_cdtipbca_i   iterado cdtipbca
				pv_ptvalbas_i   iterado ptvalbas
				pv_swmanual_i   iterado swmanual
				pv_swreas_i     iterado swreas
				pv_cdagrupa_i   iterado cdagrupa
				PV_ACCION       #I
				*/
				Map<String,String>mapPoligarIterado=new HashMap<String,String>(0);
				mapPoligarIterado.putAll(smap1);
				mapPoligarIterado.put("pv_cdgarant_i", cob.get("GARANTIA"));
				mapPoligarIterado.put("pv_nmsuplem_i", "0");
				mapPoligarIterado.put("pv_cdcapita_i", cob.get("CDCAPITA"));
				mapPoligarIterado.put("pv_status_i",   cob.get("status"));
				mapPoligarIterado.put("pv_cdtipbca_i", cob.get("cdtipbca"));
				mapPoligarIterado.put("pv_ptvalbas_i", cob.get("ptvalbas"));
				mapPoligarIterado.put("pv_swmanual_i", cob.get("swmanual"));
				mapPoligarIterado.put("pv_swreas_i",   cob.get("swreas"));
				mapPoligarIterado.put("pv_cdagrupa_i", cob.get("cdagrupa"));
				mapPoligarIterado.put("PV_ACCION",     "B");
				mapPoligarIterado.put("pv_cdtipsup_i", TipoEndoso.EMISION_POLIZA.getCdTipSup().toString());
				logger.debug("Iteracion #"+i+" de movPoligar");
				kernelManager.movPoligar(mapPoligarIterado);
				
				i++;
			}
			/*//////////////////////////////////////////////////*/
			////// borrar coberturas que vienen en la lista //////
			////// de borrar slist2                         //////
			//////////////////////////////////////////////////////
			
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("Error al guardar las coberturas",ex);
			success=false;
		}
		return SUCCESS;
	}
	
	public String obtenerCamposTatrigar()
	{
		logger.debug(Utils.log(
		        "\n###################################",
				"\n###### obtenerCamposTatrigar ######",
				"\n###### smap1 = ", smap1));
		try
		{
			List<ComponenteVO>listTatri=kernelManager.obtenerTatrigar(smap1);
			GeneradorCampos genCam=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			genCam.setCdgarant(smap1.get("pv_cdgarant_i"));
			genCam.genera(listTatri);
			str1=genCam.getFields().toString();
			str2=genCam.getItems().toString();
			// f i e l d s : [ ... ]
			//0 1 2 3 4 5 6 7
			str1=str1.substring(7).replace("\n", "");
			// i t e m s : [ ... ]
			//0 1 2 3 4 5 6 7
			str2=str2.substring(6).replace("\n", "");
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los campos tatrigar",ex);
			success=false;
		}

		logger.info(""
				+ "\n###### obtenerCamposTatrigar ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String obtenerValoresTatrigar()
	{
		logger.info(""
				+ "\n####################################"
				+ "\n###### obtenerValoresTatrigar ######"
				+ "\nsmap1 "+smap1
				);
		try
		{
			/*
			pv_cdunieco_i      smap1 ready!
            pv_cdramo_i        smap1 ready!
            pv_estado_i        smap1 ready!
            pv_nmpoliza_i      smap1 ready!
            pv_nmsituac_i      smap1 ready!
            pv_cdgarant_i      smap1 ready!
			*/
			parametros=new HashMap<String,String>(0);
			Map<String,Object>parametrosCargados=kernelManager.obtenerValoresTatrigar(smap1);
			Iterator<Entry<String, Object>> it=parametrosCargados.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String,Object> entry=it.next();
				parametros.put("pv_"+entry.getKey(), (String)entry.getValue());
			}
			logger.debug("parametros: {}", parametros);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los valores de tatrigar",ex);
			success=true;
		}
		logger.info(""
				+ "\n###### obtenerValoresTatrigar ######"
				+ "\n####################################"
				);
		return SUCCESS;
	}
	
	public String guardarValoresTatrigar()
	{
		logger.debug("\n##############################"
				+ "\n###### Guardar tvalogar ######");
		try
		{
			/*
			pv_cdunieco  smap1 ready!
	        pv_cdramo    smap1 ready!
	        pv_estado    smap1 ready!
	        pv_nmpoliza  smap1 ready!
	        pv_nmsituac  smap1 ready!
	        pv_cdgarant  smap1 ready!
	        pv_nmsuplem  #0
	        pv_status    #V
	        */
			smap1.putAll(parametros);
			smap1.put("pv_status", "V");
			smap1.put("pv_nmsuplem", "0");
			logger.debug("smap1"+smap1);
	        logger.debug("parametros"+parametros);
	        kernelManager.pMovTvalogar(smap1);
	        success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar tvalogar",ex);
			success=false;
		}
		logger.debug("\n###### Guardar tvalogar ######"
				+ "\n##############################");
		return SUCCESS; 
	}
	
	public String pantallaDomicilio()
	{
		logger.debug("\n###################################"
				+ "\n###################################"
				+ "\n###### pantalla de domicilio ######"
				+ "\n######                       ######"
				+ "\n######                       ######");
		logger.debug("smap1: "+smap1);
		ScreenInterceptor scrInt=new ScreenInterceptor();
		return scrInt.intercept(this, ScreenInterceptor.PANTALLA_COMPLEMENTARIOS_DOMICILIO_ASEGURADO);
	}
	
	public String pantallaExclusion()
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###### pantalla de exclusion ######"
				);
		logger.debug("smap1: "+smap1);
		ScreenInterceptor scrInt=new ScreenInterceptor();
		return scrInt.intercept(this, ScreenInterceptor.PANTALLA_COMPLEMENTARIOS_EXCLUSION_ASEGURADO);
	}
	
	public String mostrarPantallaExclusion()
	{
		try
		{
			List<ComponenteVO>autocompleterICD=pantallasManager.obtenerComponentes(
					TipoTramite.POLIZA_NUEVA.getCdtiptra(), null, null
					, null, null, null
					, "PANTALLA_EXCLUSION", "COMBO_ICD", null);
			
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(autocompleterICD, true, false, true, false, false, false);
			
			item1=gc.getItems();
		}
		catch(Exception ex)
		{
			logger.error("error al mostrar la pantalla de exclusion",ex);
		}
		logger.debug(""
				+ "\n###### pantalla de exclusion ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	
	public String agregarExclusion()
	{
		logger.debug("\n###################################"
				+ "\n###################################"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###### agregar exclusion     ######"
				+ "\n######                       ######");
		logger.debug("smap1: "+smap1);
		try
		{
			endososManager.guardarMpolicot(smap1.get("pv_cdunieco_i"), smap1.get("pv_cdramo_i"), smap1.get("pv_estado_i"), smap1.get("pv_nmpoliza_i"), 
					smap1.get("pv_nmsituac_i"), smap1.get("pv_cdclausu_i"), smap1.get("pv_nmsuplem_i"), smap1.get("pv_status_i"), smap1.get("pv_cdtipcla_i"), 
					smap1.get("pv_swmodi_i"), smap1.get("pv_dslinea_i"), smap1.get("pv_accion_i"));
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al agregar la exclusion",ex);
			success=false;
		}
		logger.debug("\n######                       ######"
				+ "\n###### agregar exclusion     ######"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###################################"
				+ "\n###################################");
		return SUCCESS;
	}
	
	public String obtenerExclusionesPorTipo()
	{
		logger.debug(""
				+ "\n####################################################"
				+ "\n####################################################"
				+ "\n###### cargar clausulas de exclusion por tipo ######"
				+ "\n######                                        ######"
				+ "\n######                                        ######"
				);
		logger.debug("smap1: "+smap1);
		try
		{
			slist1=kernelManager.obtenerExclusionesPorTipo(smap1);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener las clausulas de exlusion por tipo",ex);
			success=false;
		}
		logger.debug(""
				+ "\n######                                        ######"
				+ "\n######                                        ######"
				+ "\n###### cargar clausulas de exclusion por tipo ######"
				+ "\n####################################################"
				+ "\n####################################################"
				);
		return SUCCESS;
	}
	
	public String cargarTiposClausulasExclusion()
	{
		logger.debug(""
				+ "\n####################################################"
				+ "\n####################################################"
				+ "\n###### cargar tipos de clausulas de exclusion ######"
				+ "\n###### sin parametros de entrada              ######"
				+ "\n######                                        ######"
				);
		try
		{
			slist1=kernelManager.cargarTiposClausulasExclusion();
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar los tipos de clausulas de exclusion",ex);
			success=false;
		}
		logger.debug(""
				+ "\n######                                        ######"
				+ "\n###### cargar tipos de clausulas de exclusion ######"
				+ "\n###### sin parametros de entrada              ######"
				+ "\n####################################################"
				+ "\n####################################################"
				);
		return SUCCESS;
	}
	
	public String guardarHtmlExclusion()
	{
		logger.debug("\n###################################"
				+ "\n###################################"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###### guardarHtmlExclusion  ######"
				+ "\n######                       ######");
		logger.debug("smap1: "+smap1);
		try
		{
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar html de la exclusion",ex);
			success=false;
		}
		logger.debug("\n######                       ######"
				+ "\n###### guardarHtmlExclusion  ######"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###################################"
				+ "\n###################################");
		return SUCCESS;
	}
	
	public String cargarHtmlExclusion()
	{
		logger.debug("\n###################################"
				+ "\n###################################"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###### cargar html de exclu  ######"
				+ "\n######                       ######");
		logger.debug("smap1: "+smap1);
		try
		{
			smap1=kernelManager.obtenerHtmlClausula(smap1);
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar el html de exclusion",ex);
			success=false;
		}
		logger.debug("\n######                       ######"
				+ "\n###### cargar html de exclu  ######"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###################################"
				+ "\n###################################");
		return SUCCESS;
	}
	
	public String guardarExclusiones()
	{
		logger.debug("\n###################################"
				+ "\n###################################"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###### save                  ######"
				+ "\n######                       ######");
		logger.debug("slist1: "+slist1);
		logger.debug("smap1: "+smap1);
		try
		{
			success=true;
		}
		catch(Exception ex)
		{
			logger.debug("error al guardar las excluciones",ex);
			success=false;
		}
		logger.debug("\n######                       ######"
				+ "\n###### save                  ######"
				+ "\n###### pantalla de exclusion ######"
				+ "\n###################################"
				+ "\n###################################");
		return SUCCESS;
	}
	
	public String mostrarPantallaDomicilio()
	{
		try
		{
			Map<String,String>paramTatriper=new HashMap<String,String>(0);
			paramTatriper.put("pv_cdramo_i"   , smap1.get("pv_cdramo"));
			paramTatriper.put("pv_cdrol_i"    , smap1.get("pv_cdrol"));
			paramTatriper.put("pv_cdtipsit_i" , smap1.get("cdtipsit"));
			paramTatriper.put("pv_cdperson_i" , smap1.get("pv_cdperson"));
			List<ComponenteVO>tatriper=kernelManager.obtenerTatriper(paramTatriper);
			GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.setCdrol(smap1.get("pv_cdrol"));
			gc.setCdramo(smap1.get("pv_cdramo"));
			gc.setCdtipsit(smap1.get("cdtipsit"));
			gc.genera(tatriper);
			item1=gc.getFields();
			item2=gc.getItems();
			
			gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			List<ComponenteVO>listaCamposDomicilio = pantallasManager.obtenerComponentes(
					null, null, smap1.get("pv_cdramo"), smap1.get("cdtipsit"), null, null, "ASEGURADO_DOMICILIO", "FORMULARIO", null);
			
			gc.generaComponentes(listaCamposDomicilio, true, false, true, false, false, false);
			
			item3=gc.getItems();
			
			/*item1=Item.crear("fields",null,Item.ARR)//quitame
					.add(Item.crear(null,null,Item.OBJ).add("name","parametros.pv_otvalor01"))//quitame
					.add(Item.crear(null,null,Item.OBJ).add("name","parametros.pv_otvalor02"))//quitame
					.add(Item.crear(null,null,Item.OBJ)//quitame
							.add("name"       , "parametros.pv_otvalor03")//quitame
							.add("type"       , "date")//quitame
							.add("dateFormat" , "d/m/Y")//quitame
							)//generador campos*/
			item1
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.asegurado"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.rfc"))
					/*
					map.put("CDPERSON" , rs.getString("CDPERSON"));
					map.put("NMORDDOM" , rs.getString("NMORDDOM"));
					map.put("DSDOMICI" , rs.getString("DSDOMICI"));
					map.put("NMTELEFO" , rs.getString("NMTELEFO"));
					map.put("CDPOSTAL" , rs.getString("CDPOSTAL"));
					map.put("CDEDO"    , rs.getString("CDEDO"));
					map.put("CDMUNICI" , rs.getString("CDMUNICI"));
					map.put("CDCOLONI" , rs.getString("CDCOLONI"));
					map.put("NMNUMERO" , rs.getString("NMNUMERO"));
					map.put("NMNUMINT" , rs.getString("NMNUMINT"));
					*/
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.NMTELEFO"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.NMORDDOM"))//1ero o segundo o tercero
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.DSDOMICI"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.NMTELEFO"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.CODPOSTAL"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.CDEDO"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.estado"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.CDMUNICI"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.Municipio"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.CDCOLONI"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.NMNUMERO"))
					.add(Item.crear(null,null,Item.OBJ).add("name","smap1.NMNUMINT"))
					;
			/*
			item2=Item.crear("items",null,Item.ARR)//generador campos
					.add(Item.crear(null,null,Item.OBJ)//quitame
							.add("name"       , "parametros.pv_otvalor01")//quitame
							.add("xtype"       , "textfield")//quitame
							.add("allowBlank" , false)//quitame
							)//generador campos
					.add(Item.crear(null,null,Item.OBJ)//quitame
							.add("name"       , "parametros.pv_otvalor02")//quitame
							.add("xtype"       , "textfield")//quitame
							.add("allowBlank" , true)//quitame
							)//generador campos
					.add(Item.crear(null,null,Item.OBJ)//quitame
							.add("name"       , "parametros.pv_otvalor03")//quitame
							.add("xtype"       , "datefield")//quitame
							.add("format"       , "d/m/Y")//quitame
							.add("allowBlank" , false)//quitame
							)//generador campos
					;*/
		}
		catch(Exception ex)
		{
			logger.error("error al mostrar la pantalla de domicilio",ex);
		}
		logger.debug("\n######                       ######"
				+ "\n######                       ######"
				+ "\n###### pantalla de domicilio ######"
				+ "\n###################################"
				+ "\n###################################");
		return SUCCESS;
	}
	
	public String cargarPantallaDomicilio()
	{
		logger.debug("\n##########################################"
				+ "\n##########################################"
				+ "\n###### cargar pantalla de domicilio ######"
				+ "\n######                              ######"
				+ "\n######                              ######");
		try
		{
			
			if(!smap1.containsKey("pv_nmsuplem_i") || StringUtils.isBlank(smap1.get("pv_nmsuplem_i"))){
				logger.debug("suplemento 9999");
				smap1.put("pv_nmsuplem_i" , "999999999999999999");
			}
			
			if(!smap1.containsKey("pv_nmsituac_i") || StringUtils.isBlank(smap1.get("pv_nmsituac_i"))){
				if(smap1.containsKey("pv_cdrol_i") && StringUtils.isNotBlank(smap1.get("pv_cdrol_i")) && "1".equalsIgnoreCase(smap1.get("pv_cdrol_i"))){
					String nmsitucRes = consultasPolizaManager.obtieneNmsituacContratantePoliza(smap1.get("pv_cdunieco_i"), smap1.get("pv_cdramo_i"), smap1.get("pv_estado_i"), smap1.get("pv_nmpoliza_i"));
					smap1.put("pv_nmsituac_i", nmsitucRes);
				}
			}
			
			logger.debug("smap1: {}", smap1);
			/*
			pv_cdunieco_i  smap1 ready!
            pv_cdramo_i    smap1 ready!
            pv_estado_i    smap1 ready!
            pv_nmpoliza_i  smap1 ready!
            pv_nmsituac_i  smap1 ready!
            pv_cdrol_i     smap1 ready!
            pv_cdperson_i  smap1 ready!
            pv_nmsumplem_i #0
            pv_status_i    #V    QUITADO X(
            pv_cdtipsit_i  session
            */
			
			//smap1.put("pv_status_i"   , "V");
			//smap1.put("pv_cdtipsit_i" ,  datUsu.getCdtipsit());
			Map<String,Object>parametrosCargados=null;
			try
			{
				parametrosCargados=kernelManager.obtenerValoresTatriper(smap1);
			}
			catch(Exception ex)
			{
				logger.debug("no hay valores para tatriper",ex);
				parametrosCargados=new HashMap<String,Object>(0);
			}
			parametros=new HashMap<String,String>(0);
			Iterator<Entry<String, Object>> it=parametrosCargados.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String,Object> entry=it.next();
				parametros.put("pv_"+entry.getKey(), (String)entry.getValue());
			}
			logger.debug("parametros: {}", parametros);
			/*parametros=new HashMap<String,String>(0);//quitame
			parametros.put("pv_otvalor01", "valor1");//quitame
			parametros.put("pv_otvalor02", "valor2");//quitame
			parametros.put("pv_otvalor03", "17/08/1990");//quitame
			
			String nombreAsegurado = smap1.get("nombreAsegurado");
			String rfcAsegurado    = smap1.get("cdrfc");
			smap1=new HashMap<String,String>(0);
			smap1.put("asegurado"    , nombreAsegurado);
			smap1.put("rfc"          , rfcAsegurado);
			smap1.put("telefono"     , "012464666589");
			smap1.put("codigoPostal" , "99999");
			smap1.put("ciudad"       , "Tlaxcala");
			smap1.put("delegacion"   , "tlaxcala");
			smap1.put("colonia"      , "Acuitlapilco");
			smap1.put("calle"        , "Zaragoza");
			smap1.put("exterior"     , "B");
			smap1.put("interior"     , "3");*/
			
			/*
			pv_cdunieco_i  smap1 ready!
            pv_cdramo_i    smap1 ready!
            pv_estado_i    smap1 ready!
            pv_nmpoliza_i  smap1 ready!
            pv_nmsituac_i  smap1 ready!
            pv_cdrol_i     smap1 ready!
            pv_cdperson_i  smap1 ready!
            pv_nmsumplem_i #0
            pv_status_i    #V    QUITADO X(
            pv_cdtipsit_i  session
            */
			
			/*
			pv_cdunieco_i  smap1 ready!
			pv_cdramo_i    smap1 ready!
			pv_estado_i    smap1 ready!
			pv_nmpoliza_i  smap1 ready!
			pv_nmsituac_i  smap1 ready!
			pv_nmsuplem_i  #0     ->se puso arriba
			pv_cdperson_i  smap1 ready!
			pv_cdtipsit_i  session->se puso arriba
		    */
			/*
			map.put("CDPERSON" , rs.getString("CDPERSON"));
			map.put("NMORDDOM" , rs.getString("NMORDDOM"));
			map.put("DSDOMICI" , rs.getString("DSDOMICI"));
			map.put("NMTELEFO" , rs.getString("NMTELEFO"));
			map.put("CDPOSTAL" , rs.getString("CDPOSTAL"));
			map.put("CDEDO"    , rs.getString("CDEDO"));
			map.put("CDMUNICI" , rs.getString("CDMUNICI"));
			map.put("CDCOLONI" , rs.getString("CDCOLONI"));
			map.put("NMNUMERO" , rs.getString("NMNUMERO"));
			map.put("NMNUMINT" , rs.getString("NMNUMINT"));
			*/
			
			/**
			 * PARA CUANDO VIENE DE ENDOSOS DE AUTOS SE CARGAN OTROS CATALOGOS
			 */
			if(StringUtils.isNotBlank(smap1.get("domGeneral")) && Constantes.SI.equalsIgnoreCase(smap1.get("domGeneral"))){
				smap1=kernelManager.obtenerDomicilioGeneral(smap1);
			}else{
				smap1=kernelManager.obtenerDomicilio(smap1);
			}
				
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar los datos de domicilio",ex);
			success=false;
		}
		
		logger.debug("\n######                              ######"
				+ "\n######                              ######"
				+ "\n###### cargar pantalla de domicilio ######"
				+ "\n##########################################"
				+ "\n##########################################");
		return SUCCESS;
	}
	
	public String guardarPantallaDomicilio()
	{
		logger.debug("\n###########################################"
				+ "\n###########################################"
				+ "\n###### guardar pantalla de domicilio ######"
				+ "\n######                               ######"
				+ "\n######                               ######");
		try
		{
			success=true;
			logger.debug("smap1: {}", smap1);
			logger.debug("parametros: {}", parametros);
			UserVO usuSes=(UserVO)session.get("USUARIO");
			
			String usuarioCaptura =  null;
			
			if(usuSes!=null){
				if(StringUtils.isNotBlank(usuSes.getClaveUsuarioCaptura())){
					usuarioCaptura = usuSes.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuSes.getCodigoPersona();
				}
				
			}
			
			
			/**
			 * Validar que el Codigo Postal pertenezca al Estado correcto
			 */
			if(smap1!=null&&smap1.size()>0 && smap1.containsKey("cdtipsit") && TipoSituacion.MULTISALUD.getCdtipsit().equalsIgnoreCase(smap1.get("cdtipsit"))){
				HashMap<String,String> params =  new HashMap<String, String>();
				params.put("pv_estado_i", smap1.get("CDEDO"));
				params.put("pv_codpos_i", smap1.get("CODPOSTAL"));
				endososManager.validaEstadoCodigoPostal(params);
			}
			
			/*
			pv_cdunieco    smap1  ready!
			pv_cdramo      smap1  ready!
			pv_estado      smap1  ready!
			pv_nmpoliza    smap1  ready!
			pv_nmsituac    smap1  ready!
			pv_nmsuplem    #0
			pv_status      #V
			pv_cdrol       smap1  ready!
			pv_cdperson    smap1  ready!
			pv_cdatribu    #null
			pv_cdtipsit    session
			pv_otvalor01   parametros ready!
			...
			*/
			if(parametros!=null&&parametros.size()>0)
			{
				parametros.putAll(smap1);
				parametros.put("pv_nmsuplem" , "0");
				parametros.put("pv_status"   , "0");
				parametros.put("pv_cdatribu" , null);
				parametros.put("pv_cdtipsit" , smap1.get("cdtipsit"));
				kernelManager.pMovTvaloper(parametros);
			}
			
			
			/////////////////////////////////////////
			////// guardar persona datos fijos //////
			/*/////////////////////////////////////*/
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
			Map<String,String>paramDomicil=new LinkedHashMap<String,String>(0);
			paramDomicil.put("pv_cdperson_i" , smap1.get("pv_cdperson"));
			paramDomicil.put("pv_nmorddom_i" , smap1.get("NMORDDOM"));
			paramDomicil.put("pv_msdomici_i" , smap1.get("DSDOMICI"));
			paramDomicil.put("pv_nmtelefo_i" , smap1.get("NMTELEFO"));
			paramDomicil.put("pv_cdpostal_i" , smap1.get("CODPOSTAL"));
        	paramDomicil.put("pv_cdedo_i"    , smap1.get("CDEDO"));
			paramDomicil.put("pv_cdmunici_i" , smap1.get("CDMUNICI"));
			paramDomicil.put("pv_cdcoloni_i" , smap1.get("CDCOLONI"));
			paramDomicil.put("pv_nmnumero_i" , smap1.get("NMNUMERO"));
			paramDomicil.put("pv_nmnumint_i" , smap1.get("NMNUMINT"));
			paramDomicil.put("pv_cdtipdom_i" , smap1.get("CDTIPDOM"));
			paramDomicil.put("pv_cdusuario_i", usuarioCaptura);
			paramDomicil.put("pv_swactivo_i",  Constantes.SI);
			paramDomicil.put("pv_accion_i"   , "U");			
			kernelManager.pMovMdomicil(paramDomicil);
			
			/*/////////////////////////////////////*/
			////// guardar persona datos fijos //////
			/////////////////////////////////////////
			
			exito=true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar los datos de domicilio",ex);
			str1=ex.getMessage();
			exito=false;
		}
		logger.debug("\n######                               ######"
				+ "\n######                               ######"
				+ "\n###### guardar pantalla de domicilio ######"
				+ "\n###########################################"
				+ "\n###########################################");
		return SUCCESS;
	}
	
	
	
	public String agregarExclusionDetalle()
	{
		logger.debug("\n#############################################"
				+ "\n#############################################"
				+ "\n###### pantalla de exclusion Detalle   ######"
				+ "\n###### agregar exclusion   Detalle     ######"
				+ "\n######                                 ######");
		logger.debug("omap1: "+omap1);
		try
		{
			UserVO usuarioSesion=(UserVO) this.session.get("USUARIO");
            logger.debug("se inserta detalle nuevo");
            logger.debug("omap1: {}", omap1);
            
            Iterator<Entry<String,String >> it=smap1.entrySet().iterator();
            omap1= new HashMap<String,Object>(0);
			while(it.hasNext())
			{
				Entry<String,String> entry=it.next();
				omap1.put(entry.getKey(), entry.getValue());
			}
			
			
            omap1.put("pv_feinicio_i"   , new Date());
            omap1.put("pv_cdusuari_i"   , usuarioSesion.getUser());
            omap1.put("pv_cdmotivo_i"   , null);
            omap1.put("pv_cdsisrol_i"   , usuarioSesion.getRolActivo().getClave());
            //kernelManager.movDmesacontrol(omap1);
            mesaControlManager.movimientoDetalleTramite(
            		smap1.get("pv_ntramite_i")
            		,new Date()
            		,smap1.get("pv_cdclausu_i")
            		,smap1.get("pv_comments_i")
            		,usuarioSesion.getUser()
            		,null//cdmotivo
            		,usuarioSesion.getRolActivo().getClave()
            		,"N"
            		,null
            		,false //TODO JTEZVA
            		);
            
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al agregar la exclusion",ex);
			success=false;
		}
		logger.debug("\n######                                ######"
				+ "\n###### agregar exclusion  Detalle     ######"
				+ "\n###### pantalla de exclusion  Detalle ######"
				+ "\n############################################"
				+ "\n############################################");
		return SUCCESS;
	}
	
	public String pantallaValosit()
	{
		logger.info(
				new StringBuilder()
				.append("\n##############################")
				.append("\n###### pantalla valosit ######")
				.append("\nsmap1=").append(smap1)
				.toString()
				);
		
		success = true;
		exito   = true;
		
		String  cdunieco  = null;
		String  cdramo    = null;
		String  estado    = null;
		String  nmpoliza  = null;
		String  cdtipsit  = null;
		String  sAgrupado = null;
		boolean agrupado  = false;
		String  cdusuari  = null;
		
		//datos
		if(exito)
		{
			try
			{
				UserVO usuario = (UserVO)session.get("USUARIO");
				cdusuari       = usuario.getUser();
				
				cdunieco  = smap1.get("cdunieco");
				cdramo    = smap1.get("cdramo");
				estado    = smap1.get("estado");
				nmpoliza  = smap1.get("nmpoliza");
				cdtipsit  = smap1.get("cdtipsit");
				sAgrupado = smap1.get("agrupado");
				if(StringUtils.isBlank(cdunieco)
						||StringUtils.isBlank(cdramo)
						||StringUtils.isBlank(estado)
						||StringUtils.isBlank(nmpoliza)
						||StringUtils.isBlank(cdtipsit)
						||StringUtils.isBlank(sAgrupado)
						)
				{
					throw new Exception("No hay datos suficientes");
				}
				agrupado=sAgrupado.equalsIgnoreCase("si");
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Datos incompletos #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		//obtener atributos
		if(exito)
		{
			try
			{
				smap1.put("timestamp",""+System.currentTimeMillis());
				
				//atributos readonly
				List<ComponenteVO>listaAtributosReadOnly=pantallasManager.obtenerComponentes(
						null, null, cdramo
						,cdtipsit, null, null
						,"PANTALLA_VALOSIT","READONLY",null
						);
				Map<String,String>mapaAtributosReadonly=new HashMap<String,String>();
				for(ComponenteVO iReadonly:listaAtributosReadOnly)
				{
					mapaAtributosReadonly.put(iReadonly.getNameCdatribu() , "DUMMY");
				}
				
				//atributos ocultos
				List<ComponenteVO>listaAtributosOcultos=pantallasManager.obtenerComponentes(
						null, null, cdramo
						,cdtipsit, null, null
						,"PANTALLA_VALOSIT","OCULTOS",null
						);
				Map<String,String>mapaAtributosOcultos=new HashMap<String,String>();
				for(ComponenteVO iOculto:listaAtributosOcultos)
				{
					mapaAtributosOcultos.put(iOculto.getNameCdatribu() , "DUMMY");
				}

				//tatrisit
				List<ComponenteVO>tatrisit=kernelManager.obtenerTatrisit(cdtipsit,cdusuari);
				GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.setCdtipsit(cdtipsit);
				
				List<ComponenteVO>tatriTemp=new ArrayList<ComponenteVO>();
				
				//si es agrupado solo dejar los atributos con N, si es individual solo los que tengan S
				for(ComponenteVO t:tatrisit)
				{
					if(agrupado)
					{
						if(t.getSwsuscri().equalsIgnoreCase("N"))//N=Agrupado
						{
							tatriTemp.add(t);
							t.setMenorCero(true);
							if(mapaAtributosReadonly.containsKey(t.getNameCdatribu()))
							{
								t.setSoloLectura(true);
							}
							if(mapaAtributosOcultos.containsKey(t.getNameCdatribu()))
							{
								t.setOculto(true);
							}
						}
					}
					else
					{
						if(t.getSwsuscri().equalsIgnoreCase("S"))//S=individual
						{
							tatriTemp.add(t);
							t.setMenorCero(true);
							if(mapaAtributosReadonly.containsKey(t.getNameCdatribu()))
							{
								t.setSoloLectura(true);
							}
							if(mapaAtributosOcultos.containsKey(t.getNameCdatribu()))
							{
								t.setOculto(true);
							}
						}
					}
				}
				tatrisit=tatriTemp;
				
				//ordenar codigo postal>estado>municipio
				if(agrupado&&
						(
								cdtipsit.equals(TipoSituacion.SALUD_VITAL.getCdtipsit())
								||cdtipsit.equals(TipoSituacion.SALUD_NOMINA.getCdtipsit())
								||cdtipsit.equals(TipoSituacion.MULTISALUD.getCdtipsit())
						)
					)
				{
					logger.debug("vamos a ordenar postal, estado y municipio");
					ComponenteVO[] tatriTemp2 = new ComponenteVO[tatrisit.size()];
					//buscar cp
					for(ComponenteVO t:tatrisit)
					{
						if((""+t.getLabel()).toUpperCase().indexOf("POSTAL")!=-1)
						{
							t.setComboVacio(true);
							tatriTemp2[0] = t;
							logger.debug("encontramos postal 0");
							break;
						}
					}
					//buscar estado
					for(ComponenteVO t:tatrisit)
					{
						if((""+t.getLabel()).toUpperCase().indexOf("ESTADO")!=-1)
						{
							tatriTemp2[1] = t;
							logger.debug("encontramos estado 1");
							break;
						}
					}
					//buscar municipio
					for(ComponenteVO t:tatrisit)
					{
						if((""+t.getLabel()).toUpperCase().indexOf("MUNICIPIO")!=-1)
						{
							tatriTemp2[2] = t;
							logger.debug("encontramos municipio 2");
							break;
						}
					}
					//agregar todos los demas
					int i = 3;
					for(ComponenteVO t:tatrisit)
					{
						if((""+t.getLabel()).toUpperCase().indexOf("POSTAL")==-1
								&&(""+t.getLabel()).toUpperCase().indexOf("ESTADO")==-1
								&&(""+t.getLabel()).toUpperCase().indexOf("MUNICIPIO")==-1
						)
						{
							tatriTemp2[i++] = t;
							logger.debug(Utils.log("ahora agregamos ",t.getLabel()));
						}
					}
					tatrisit = new ArrayList<ComponenteVO>();
					for(ComponenteVO c : tatriTemp2)
					{
						logger.debug(Utils.log("ahora movemos del array a la lista ",c.getLabel()));
						tatrisit.add(c);
					}
				}
				
				gc.genera(tatrisit);
				item1=gc.getFields();
				item2=gc.getItems();
			}
			catch(Exception ex)
			{
				long timestamp  = System.currentTimeMillis();
				exito           = false;
				respuesta       = "Error al obtener los componentes #"+timestamp;
				respuestaOculta = ex.getMessage();
				logger.error(respuesta,ex);
			}
		}
		
		logger.info(
				new StringBuilder()
				.append("\n###### pantalla valosit ######")
				.append("\n##############################")
				.toString()
				);
		
		String result = SUCCESS;
		if(!exito)
		{
			result = ERROR;
		}
		return result;
	}
	
	///////////////////////////////////////////////////////
	////// cargar valosit para datos complementarios //////
	/*///////////////////////////////////////////////////*/
	public String pantallaValositLoad()
	{
		logger.debug(""
				+ "\n###################################"
				+ "\n###################################"
				+ "\n###### pantalla valosit load ######"
				+ "\n######                       ######"
				);
		try
		{
			logger.debug("smap1: "+smap1);
			omap1=kernelManager.obtieneValositSituac(smap1);
			parametros=new LinkedHashMap<String,String>(0);
			Iterator it=omap1.entrySet().iterator();
			while(it.hasNext())
			{
				Entry en=(Entry)it.next();
				parametros.put("pv_"+(String)en.getKey(),(String)en.getValue());
			}
			omap1=null;
			success=true;
		}
		catch(Exception ex)
		{
			logger.error("error al cargar valosit",ex);
			success=false;
		}
		logger.debug(""
				+ "\n######                       ######"
				+ "\n###### pantalla valosit load ######"
				+ "\n###################################"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	/*///////////////////////////////////////////////////*/
	////// cargar valosit para datos complementarios //////
	///////////////////////////////////////////////////////
	
	/////////////////////////////////////////////
	////// guardar valosit situac o grupal //////
	/*/////////////////////////////////////////*/
	public String pantallaValositSave () {
		logger.debug(Utils.log(
		        "\n#################################",
				"\n###### pantallaValositSave ######",
				"\n###### smap1      = " , smap1,
				"\n###### parametros = " , parametros));
		try {
			UserVO usuario = Utils.validateSession(session);
			String usuarioCaptura = null,
			       cdunieco       = smap1.get("cdunieco"),
			       cdramo         = smap1.get("cdramo"),
			       estado         = smap1.get("estado"),
			       nmpoliza       = smap1.get("nmpoliza"),
			       nmsituac       = smap1.get("nmsituac"),
			       agrupado       = smap1.get("agrupado"),
			       paso           = null,
			       nmsuplem       = "0";
			Utils.validate(cdunieco , "Falta cdunieco",
			               cdramo   , "Falta cdramo",
			               estado   , "Falta estado",
			               nmpoliza , "Falta nmpoliza");
			try {
    			if (usuario != null) {
    				if (StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())) {
    					usuarioCaptura = usuario.getClaveUsuarioCaptura();
    				} else {
    					usuarioCaptura = usuario.getCodigoPersona();
    				}
    			}
    			/**
                 * Para borrar domicilio a asegurados cuando se cambia de la cotizacion
                 */
    			paso = "Verificando cambio de c\u00f3digo postal";
    			logger.debug(paso);
                if (consultasManager.esProductoSalud(cdramo)) {
                    logger.debug(" <<<<>>>>>> Entrando a Borrando de domicilios de asegurados si cambia el codigo postal <<<<<>>>>> ");
                    List<Map<String,String>> valositsPoliza = endososManager.obtenerValositUltimaImagen(cdunieco, cdramo, estado, nmpoliza, "0");
                    if (valositsPoliza != null && !valositsPoliza.isEmpty()) {
                        logger.debug(" <<<<>>>>>> Verificando vvalisit de 0 <<<<<>>>>> ");
                        Map<String,String> valosit =  valositsPoliza.get(0);
                        if (valosit!=null && valosit.containsKey("CDATRIBU") && StringUtils.isNotBlank(valosit.get("CDATRIBU"))) {
                            /**PARA OBTENER EL NUMERO DE ATRIBUTO DEL CODIGO POSTAL PARA ESTE PRODUCTO**/
                            String keyCodPostal = Utils.join("pv_otvalor", valosit.get("CDATRIBU"));
                            logger.debug(" <<<<>>>>>> Obteniendo llave de codigo postal  <<<<<>>>>> :::: {}", keyCodPostal);
                            if (StringUtils.isNotBlank(keyCodPostal)) {
                                String valorCodPosPantalla = parametros.get(keyCodPostal);
                                logger.debug(" <<<<>>>>>> Obteniendo VALOR de codigo postal de pantalla  <<<<<>>>>> :::: {}", valorCodPosPantalla);
                                if (StringUtils.isNotBlank(valorCodPosPantalla)) {
                                    logger.debug(" <<<<>>>>>> Borrando domicilios de asegurados si cambia el codigo postal <<<<<>>>>> ");
                                    paso = "Ejecutando borrado de domicilios condicionado";
                                    logger.debug(paso);
                                    mesaControlManager.borraDomicilioAsegSiCodposCambia(cdunieco, cdramo, estado, nmpoliza, "0",
                                            valorCodPosPantalla);
                                }
                            }
                        }
                    }
                }
                paso = "Recuperando incisos/asegurados";
                logger.debug(paso);
                List<Map<String, String>> incisos = consultasManager.cargarTvalosit(cdunieco, cdramo, estado, nmpoliza, "0");
    			if ("SI".equalsIgnoreCase(agrupado)) { // ACTUALIZAR TODOS LOS REGISTROS
    			    paso = "Actualizando todos los incisos/asegurados";
    			    logger.debug(paso);
    				// ACTUALIZAMOS TVALOSIT
    				for (Map<String, String> inciso : incisos) {
    				    this.procesoPantallaValosit(cdunieco, cdramo, estado, nmpoliza, inciso.get("NMSITUAC"), nmsuplem,
    				            usuarioCaptura, inciso, parametros);
    				}
    			} else { // ACTUALIZAR SOLO UNO
    				paso = Utils.join("Identificando inciso/asegurado ", nmsituac);
    				logger.debug(paso);
                    // ACTUALIZAMOS TVALOSIT
    				Map<String, String> inciso = null;
                    for (Map<String, String> ite: incisos) {
                        if (nmsituac.equals(ite.get("NMSITUAC"))) {
                            inciso = ite;
                            break;
                        }
                    }
                    if (inciso == null) {
                        throw new ApplicationException("No se encuentra el inciso/asegurado");
                    }
                    paso = Utils.join("Actualizando inciso/asegurado ", nmsituac);
                    logger.debug(paso);
                    this.procesoPantallaValosit(cdunieco, cdramo, estado, nmpoliza, nmsituac, nmsuplem, usuarioCaptura, inciso, parametros);
    			}
    			success=true;
			}
			catch (Exception ex) {
			    Utils.generaExcepcion(ex, paso);
			}
		} catch (Exception ex) {
			respuesta = Utils.manejaExcepcion(ex);
		}
		logger.debug(Utils.log(
		        "\n###### pantallaValositSave ######",
		        "\n#################################"));
		return SUCCESS;
	}
	/*/////////////////////////////////////////*/
	////// guardar valosit situac o grupal //////
	/////////////////////////////////////////////
	
	private void procesoPantallaValosit (String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac,
	        String nmsuplem, String usuarioCaptura, Map<String, String> inciso, Map<String, String> parametros) throws Exception {
	    logger.debug(Utils.log(
	            "\n####################################",
	            "\n###### procesoPantallaValosit ######",
	            "\n###### cdunieco       = " , cdunieco,
	            "\n###### cdramo         = " , cdramo,
	            "\n###### estado         = " , estado,
	            "\n###### nmpoliza       = " , nmpoliza,
	            "\n###### nmsituac       = " , nmsituac,
	            "\n###### nmsuplem       = " , nmsuplem,
	            "\n###### usuarioCaptura = " , usuarioCaptura,
	            "\n###### inciso         = " , inciso,
	            "\n###### parametros     = " , parametros));
	    String paso = null;
	    try {
	        paso = Utils.join("Actualizando inciso/asegurado ", nmsituac);
            logger.debug(paso);
            Map<String, String> paramsActTvalosit = new LinkedHashMap<String, String>();
            paramsActTvalosit.put("pv_cdunieco" , cdunieco);
            paramsActTvalosit.put("pv_cdramo"   , cdramo);
            paramsActTvalosit.put("pv_estado"   , estado);
            paramsActTvalosit.put("pv_nmpoliza" , nmpoliza);
            paramsActTvalosit.put("pv_nmsituac" , nmsituac);
            paramsActTvalosit.put("pv_nmsuplem" , inciso.get("NMSUPLEM"));
            paramsActTvalosit.put("pv_cdtipsit" , inciso.get("CDTIPSIT"));
            paramsActTvalosit.put("pv_status"   , inciso.get("STATUS"));
            
            /*
             * Dejamos todos en formato para insertar
             * pv_otvalor01, pv_otvalor02...
            */
            for (Entry<String, String> en : inciso.entrySet()) {
                String key = en.getKey();
                if (key.contains("OTVALOR")) {
                    String key2 = Utils.join("pv_otvalor", key.substring("OTVALOR".length())); // pv_otvalorXY (viene de OTVALORXY)
                    paramsActTvalosit.put(key2, en.getValue());
                }
            }
            // Agregamos los de pantalla
            for (Entry<String, String> en : parametros.entrySet()) {
                if (en.getKey().contains("otvalor")) {
                    paramsActTvalosit.put(en.getKey(), en.getValue());
                }
            }
            kernelManager.actualizaValoresSituaciones(paramsActTvalosit);
            
            if (Ramo.MULTISALUD.getCdramo().equals(cdramo)) {
                paso = Utils.join("Verificando datos de multisalud de asegurado ", inciso.get("NMSITUAC"));
                logger.debug(paso);
                String estadoanterior = inciso.get("OTVALOR04");
                String ciudadanterior = inciso.get("OTVALOR05");
                String estadonuevo    = parametros.get("pv_otvalor04");
                String ciudadnueva    = parametros.get("pv_otvalor05");
                if(!estadoanterior.equalsIgnoreCase(estadonuevo)
                        || !ciudadanterior.equalsIgnoreCase(ciudadnueva)
                        ) {
                    Map<String, String> persona = consultasManager.cargarMpoliperSituac(cdunieco, cdramo, estado, nmpoliza,
                            nmsuplem, nmsituac);
                    String swexiper = persona.get("SWEXIPER"),
                           cdperson = persona.get("CDPERSON"),
                           nmorddom = persona.get("NMORDDOM");
                    if (StringUtils.isBlank(swexiper)) {
                        swexiper = "N";
                    }
                    if (StringUtils.isBlank(nmorddom)) {
                        nmorddom = "1";
                    }
                    if ("N".equals(swexiper) && cdperson.length() >= "10000000".length()) { // SOLO PARA PERSONAS WORKING CON CDPERSON >10,000,000
                        logger.debug("mdomicil borrar para: {}", persona);
                        Map<String,String> paramBorrarDomicil=new LinkedHashMap<String,String>(0);
                        paramBorrarDomicil.put("pv_cdperson_i" , persona.get("CDPERSON"));
                        paramBorrarDomicil.put("pv_nmorddom_i" , nmorddom);
                        paramBorrarDomicil.put("pv_msdomici_i" , null);
                        paramBorrarDomicil.put("pv_nmtelefo_i" , null);
                        paramBorrarDomicil.put("pv_cdpostal_i" , null);
                        paramBorrarDomicil.put("pv_cdedo_i"    , null);
                        paramBorrarDomicil.put("pv_cdmunici_i" , null);
                        paramBorrarDomicil.put("pv_cdcoloni_i" , null);
                        paramBorrarDomicil.put("pv_nmnumero_i" , null);
                        paramBorrarDomicil.put("pv_nmnumint_i" , null);
                        paramBorrarDomicil.put("pv_cdtipdom_i" , null);
                        paramBorrarDomicil.put("pv_cdusuario_i", usuarioCaptura);
                        paramBorrarDomicil.put("pv_swactivo_i",  Constantes.NO);
                        paramBorrarDomicil.put("pv_accion_i"   , "B");//borrar
                        kernelManager.pMovMdomicil(paramBorrarDomicil);
                    }
                }
            }
	    } catch (Exception ex) {
	        Utils.generaExcepcion(ex, paso);
	    }
        logger.debug(Utils.log(
                "\n###### procesoPantallaValosit ######",
                "\n####################################"));
	}
	
	/////////////////////////////////
	////// getters and setters //////
	/*/////////////////////////////*/
	public void setKernelManager(KernelManagerSustituto km)
	{
		this.kernelManager=km;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public Map<String, String> getSmap2() {
		return smap2;
	}

	public void setSmap2(Map<String, String> smap2) {
		this.smap2 = smap2;
	}

	public Map<String, String> getSmap3() {
		return smap3;
	}

	public void setSmap3(Map<String, String> smap3) {
		this.smap3 = smap3;
	}

	public Map<String, Object> getOmap1() {
		return omap1;
	}

	public void setOmap1(Map<String, Object> omap1) {
		this.omap1 = omap1;
	}

	public Map<String, Object> getOmap2() {
		return omap2;
	}

	public void setOmap2(Map<String, Object> omap2) {
		this.omap2 = omap2;
	}

	public Map<String, Object> getOmap3() {
		return omap3;
	}

	public void setOmap3(Map<String, Object> omap3) {
		this.omap3 = omap3;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	public List<Map<String, Object>> getOlist1() {
		return olist1;
	}

	public void setOlist1(List<Map<String, Object>> olist1) {
		this.olist1 = olist1;
	}

	public List<Map<String, Object>> getOlist2() {
		return olist2;
	}

	public void setOlist2(List<Map<String, Object>> olist2) {
		this.olist2 = olist2;
	}

	public Item getItem1() {
		return item1;
	}

	public void setItem1(Item item1) {
		this.item1 = item1;
	}

	public Item getItem2() {
		return item2;
	}

	public void setItem2(Item item2) {
		this.item2 = item2;
	}

	public Item getItem3() {
		return item3;
	}

	public void setItem3(Item item3) {
		this.item3 = item3;
	}

	public Item getItem4() {
		return item4;
	}

	public void setItem4(Item item4) {
		this.item4 = item4;
	}

	public Map<String, String> getSpanel1() {
		return spanel1;
	}

	public void setSpanel1(Map<String, String> spanel1) {
		this.spanel1 = spanel1;
	}

	public Map<String, String> getSpanel2() {
		return spanel2;
	}

	public void setSpanel2(Map<String, String> spanel2) {
		this.spanel2 = spanel2;
	}

	public Map<String, String> getSpanel3() {
		return spanel3;
	}

	public void setSpanel3(Map<String, String> spanel3) {
		this.spanel3 = spanel3;
	}

	public Map<String, Object> getOpanel1() {
		return opanel1;
	}

	public void setOpanel1(Map<String, Object> opanel1) {
		this.opanel1 = opanel1;
	}

	public Map<String, Object> getOpanel2() {
		return opanel2;
	}

	public void setOpanel2(Map<String, Object> opanel2) {
		this.opanel2 = opanel2;
	}

	public Map<String, Object> getOpanel3() {
		return opanel3;
	}

	public void setOpanel3(Map<String, Object> opanel3) {
		this.opanel3 = opanel3;
	}

	public boolean isSuccess() {
		return success;
	}

	public Map<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public void setPantallasManager(PantallasManager pantallasManager) {
		this.pantallasManager = pantallasManager;
	}

	public boolean isExito() {
		return exito;
	}

	public void setExito(boolean exito) {
		this.exito = exito;
	}

	public void setEndososManager(EndososManager endososManager) {
		this.endososManager = endososManager;
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