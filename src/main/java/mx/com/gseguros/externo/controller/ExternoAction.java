package mx.com.gseguros.externo.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.Constantes;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

public class ExternoAction extends PrincipalCoreAction 
{
	private String                   error;
	private Logger                   logger = Logger.getLogger(ExternoAction.class);
	private static final long        serialVersionUID = -1813666957634022439L;
	private List<Map<String,String>> slist1;
	private List<Map<String,String>> slist2;
	private Map<String,String>       smap1;
	private StoredProceduresManager  storedProceduresManager;
	private boolean                  success;
	private String                   estadoSesion;
	
	public String cotizar()
	{
		this.session=ActionContext.getContext().getSession();
		logger.info(""
				+ "\n#####################"
				+ "\n###### cotizar ######"
				);
		logger.info("DATOS DE ENTRADA");
		logger.info("smap1: "+smap1);
		logger.info("slist1: "+slist1);
		logger.info("sesion: "+session);
		
		boolean exito = true;
		Map<String,String>planes=null;
		slist2=null;
		
		//LLAMAR CLASE EXTERNA
		if(exito)
		{
			try
			{
				File carpeta         = new File("/u01/wlserver/wldomain/gseguros");
				String clase         = "Cotizar";
				String metodo        = "cotizar";
				Class<?>[]parametros = new Class[]
						{
						Map.class      //smap1
						,List.class    //slist1
						,Map.class     //sesion
						,Object.class  //logger
						,Object.class  //manager
						};
				ClassLoader loader = new URLClassLoader(new URL[]{carpeta.toURI().toURL()},getClass().getClassLoader());
				Class<?>    cls    = loader.loadClass(clase);
				Method      method = cls.getMethod(metodo, parametros);
				
				UserVO usuario = (UserVO)session.get("USUARIO");
				Map<String,String>sesion=new HashMap<String,String>();
				sesion.put("CDUSUARI",usuario.getUser());
				sesion.put("CDELEMEN",usuario.getEmpresa().getElementoId());
				
				Object[]result=(Object[])method.invoke(cls.newInstance(),smap1,slist1,sesion,logger,storedProceduresManager);
				planes=(Map<String,String>)result[0];
				slist2=(List<Map<String,String>>)result[1];
			}
			catch(Exception ex)
			{
				logger.error("error en invocacion externa",ex);
				exito   = false;
				Throwable cause=ex.getCause();
				while(cause.getCause()!=null)
				{
					cause=cause.getCause();
				}
				error   = cause.getMessage();
				success = false;
			}
		}
		
		if(exito)
		{
			try
			{
				///////////////////////////////////
	            ////// columnas para el grid //////
	            List<ComponenteVO>tatriPlanes=new ArrayList<ComponenteVO>();
	            
	            ////// 1. forma de pago //////
	            ComponenteVO tatriCdperpag=new ComponenteVO();
	        	tatriCdperpag.setType(ComponenteVO.TIPO_GENERICO);
	        	tatriCdperpag.setLabel("CDPERPAG");
	        	tatriCdperpag.setTipoCampo(ComponenteVO.TIPOCAMPO_NUMERICO);
	        	tatriCdperpag.setNameCdatribu("CDPERPAG");
	        	
	        	/*Map<String,String>mapaCdperpag=new HashMap<String,String>();
	        	mapaCdperpag.put("OTVALOR10","CDPERPAG");
	        	tatriCdperpag.setMapa(mapaCdperpag);*/
	        	tatriPlanes.add(tatriCdperpag);
	        	
	        	ComponenteVO tatriDsperpag=new ComponenteVO();
	        	tatriDsperpag.setType(ComponenteVO.TIPO_GENERICO);
	        	tatriDsperpag.setLabel("Forma de pago");
	        	tatriDsperpag.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
	        	tatriDsperpag.setNameCdatribu("DSPERPAG");
	        	tatriDsperpag.setColumna(Constantes.SI);
	        	
	        	/*Map<String,String>mapaDsperpag=new HashMap<String,String>();
	        	mapaDsperpag.put("OTVALOR08","S");
	        	mapaDsperpag.put("OTVALOR10","DSPERPAG");
	        	tatriDsperpag.setMapa(mapaDsperpag);*/
	        	tatriPlanes.add(tatriDsperpag);
	        	////// 1. forma de pago //////
	        	
	        	////// 2. nmsituac //////
	        	ComponenteVO tatriNmsituac=new ComponenteVO();
	        	tatriNmsituac.setType(ComponenteVO.TIPO_GENERICO);
	        	tatriNmsituac.setLabel("NMSITUAC");
	        	tatriNmsituac.setTipoCampo(ComponenteVO.TIPOCAMPO_NUMERICO);
	        	tatriNmsituac.setNameCdatribu("NMSITUAC");
	        	
	        	/*Map<String,String>mapaNmsituac=new HashMap<String,String>();
	        	mapaNmsituac.put("OTVALOR10","NMSITUAC");
	        	tatriNmsituac.setMapa(mapaNmsituac);*/
	        	tatriPlanes.add(tatriNmsituac);
	        	////// 2. nmsituac //////
	        	
	        	////// 2. planes //////
	            for(Entry<String,String>plan:planes.entrySet())
	            {
	            	////// prima
	            	ComponenteVO tatriPrima=new ComponenteVO();
	            	tatriPrima.setType(ComponenteVO.TIPO_GENERICO);
	            	tatriPrima.setLabel(plan.getValue());
	            	tatriPrima.setTipoCampo(ComponenteVO.TIPOCAMPO_PORCENTAJE);
	            	tatriPrima.setColumna(Constantes.SI);
	            	tatriPrima.setRenderer(ComponenteVO.RENDERER_MONEY_EXT);
	            	tatriPrima.setNameCdatribu("MNPRIMA"+plan.getKey());
	            	
	            	/*Map<String,String>mapaPlan=new HashMap<String,String>();
	            	mapaPlan.put("OTVALOR08","S");
	            	mapaPlan.put("OTVALOR09","MONEY");
	            	mapaPlan.put("OTVALOR10","MNPRIMA"+plan.getKey());
	            	tatriPrima.setMapa(mapaPlan);*/
	            	tatriPlanes.add(tatriPrima);
	            	
	            	////// cdplan
	            	ComponenteVO tatriCdplan=new ComponenteVO();
	             	tatriCdplan.setType(ComponenteVO.TIPO_GENERICO);
	             	tatriCdplan.setLabel("CDPLAN"+plan.getKey());
	             	tatriCdplan.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
	             	tatriCdplan.setNameCdatribu("CDPLAN"+plan.getKey());
	             	tatriCdplan.setColumna(ComponenteVO.COLUMNA_OCULTA);
	             	
	             	/*Map<String,String>mapaCdplan=new HashMap<String,String>();
	             	//mapaCdplan.put("OTVALOR08","H");
	             	mapaCdplan.put("OTVALOR10","CDPLAN"+plan.getKey());
	             	tatriCdplan.setMapa(mapaCdplan);*/
	             	tatriPlanes.add(tatriCdplan);
	             	
	             	////// dsplan
	             	ComponenteVO tatriDsplan=new ComponenteVO();
	             	tatriDsplan.setType(ComponenteVO.TIPO_GENERICO);
	             	tatriDsplan.setLabel("DSPLAN"+plan.getKey());
	             	tatriDsplan.setTipoCampo(ComponenteVO.TIPOCAMPO_ALFANUMERICO);
	             	tatriDsplan.setNameCdatribu("DSPLAN"+plan.getKey());
	             	
	             	/*Map<String,String>mapaDsplan=new HashMap<String,String>();
	             	//mapaDsplan.put("OTVALOR08","H");
	             	mapaDsplan.put("OTVALOR10","DSPLAN"+plan.getKey());
	             	tatriDsplan.setMapa(mapaDsplan);*/
	             	tatriPlanes.add(tatriDsplan);
	            }
	            ////// 2. planes //////
	            
	            GeneradorCampos gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
	            gc.setEsMovil(session!=null&&session.containsKey("ES_MOVIL")&&((Boolean)session.get("ES_MOVIL"))==true);
	            if(!gc.isEsMovil()&&smap1.containsKey("movil"))
	            {
	            	gc.setEsMovil(true);
	            }
	            gc.genera(tatriPlanes);
	            
	            String columnas = gc.getColumns().toString();
	            // c o l u m n s : [
	            //0 1 2 3 4 5 6 7 8
	            smap1.put("columnas",columnas.substring(8));
	            
	            String fields = gc.getFields().toString();
	            // f i e l d s : [
	            //0 1 2 3 4 5 6 7
	            smap1.put("fields",fields.substring(7));
	            ////// columnas para el grid //////
	            ///////////////////////////////////
	            
				success=true;
			}
			catch(Exception ex)
			{
				logger.error("error al cotizar",ex);
				error=ex.getMessage();
				success = false;
			}
		}
		
		logger.info(""
				+ "\n###### cotizar ######"
				+ "\n#####################"
				); 
		return SUCCESS;
	}
	
	public String aplicacionMovil()
	{
		logger.info(""
				+ "\n#############################"
				+ "\n###### aplicacionMovil ######"
				);
		logger.debug("estadoSesion: "+estadoSesion);
		logger.info(""
				+ "\n###### aplicacionMovil ######"
				+ "\n#############################"
				);
		return SUCCESS;
	}
	
	public void setStoredProceduresManager(
			StoredProceduresManager storedProceduresManager) {
		this.storedProceduresManager = storedProceduresManager;
	}

	public List<Map<String, String>> getSlist1() {
		return slist1;
	}

	public void setSlist1(List<Map<String, String>> slist1) {
		this.slist1 = slist1;
	}

	public Map<String, String> getSmap1() {
		return smap1;
	}

	public void setSmap1(Map<String, String> smap1) {
		this.smap1 = smap1;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<Map<String, String>> getSlist2() {
		return slist2;
	}

	public void setSlist2(List<Map<String, String>> slist2) {
		this.slist2 = slist2;
	}

	public String getEstadoSesion() {
		return estadoSesion;
	}

	public void setEstadoSesion(String estadoSesion) {
		this.estadoSesion = estadoSesion;
	}
	
}