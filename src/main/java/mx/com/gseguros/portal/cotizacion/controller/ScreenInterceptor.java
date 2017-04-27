package mx.com.gseguros.portal.cotizacion.controller;

import mx.com.aon.core.web.PrincipalCoreAction;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

public class ScreenInterceptor
{

    public static final int PANTALLA_COMPLEMENTARIOS_ASEGURADOS           = 1;
    public static final int PANTALLA_COMPLEMENTARIOS_COBERTURAS_ASEGURADO = 2;
    public static final int PANTALLA_COMPLEMENTARIOS_DOMICILIO_ASEGURADO  = 3;
    public static final int PANTALLA_COMPLEMENTARIOS_EXCLUSION_ASEGURADO  = 4;
    private Logger log=Logger.getLogger(ScreenInterceptor.class);
	
	public String intercept(PrincipalCoreAction action, int screen)
	{
                
        /**
		 * pantalla de datos complemetarios de asegurados
                 * @requiere:
                    cdunieco
                    cdramo
                    estado
                    nmpoliza
		 * @return:
                    mostrarPantallaAsegurados();
		 */
		if(screen==PANTALLA_COMPLEMENTARIOS_ASEGURADOS)
		{
			ComplementariosAction a=(ComplementariosAction)action;
			if(ActionContext.getContext().getSession()==null
                            ||ActionContext.getContext().getSession().get("USUARIO")==null
                            ||a.getMap1()==null
                    		||a.getMap1().isEmpty()
                    		||!a.getMap1().containsKey("cdunieco")
                            ||!a.getMap1().containsKey("cdramo")
                            ||!a.getMap1().containsKey("estado")
                            ||!a.getMap1().containsKey("nmpoliza")
                            )
			{
				return "denied";
			}
			else
			{
				return a.mostrarPantallaAsegurados();
			}
		}
		
		/**
		 * pantalla de coberturas de asegurado
                 * @requiere:
		 * @return:
                    mostrarPantallaAsegurados();
		 */
                else if(screen==PANTALLA_COMPLEMENTARIOS_COBERTURAS_ASEGURADO)
		{
			ComplementariosCoberturasAction a=(ComplementariosCoberturasAction)action;
			if(ActionContext.getContext().getSession()==null
                            ||ActionContext.getContext().getSession().get("USUARIO")==null
                            ||a.getSmap1()==null
                    		||a.getSmap1().isEmpty()
                    		||!a.getSmap1().containsKey("pv_cdunieco")
                            ||!a.getSmap1().containsKey("pv_cdramo")
                            ||!a.getSmap1().containsKey("pv_estado")
                            ||!a.getSmap1().containsKey("pv_nmpoliza")
                            ||!a.getSmap1().containsKey("pv_nmsituac")
                            ||!a.getSmap1().containsKey("pv_cdperson")
                            )
			{
				return "denied";
			}
			else
			{
				return a.mostrarPantallaCoberturas();
			}
		}
		
		/**
		pantalla de domicilio
        requiere:  
           smap1.pv_cdunieco
           smap1.pv_cdramo
           smap1.pv_estado
           smap1.pv_nmpoliza
           smap1.pv_nmsituac
           smap1.pv_cdperson          
		return:
        mostrarPantallaDomicilio();
		*/
        else if(screen==PANTALLA_COMPLEMENTARIOS_DOMICILIO_ASEGURADO)
		{
			ComplementariosCoberturasAction a=(ComplementariosCoberturasAction)action;
			if(ActionContext.getContext().getSession()==null
                            ||ActionContext.getContext().getSession().get("USUARIO")==null
                            ||a.getSmap1()==null
                    		||a.getSmap1().isEmpty()
                    		||!a.getSmap1().containsKey("pv_cdunieco")
                            ||!a.getSmap1().containsKey("pv_cdramo")
                            ||!a.getSmap1().containsKey("pv_estado")
                            ||!a.getSmap1().containsKey("pv_nmpoliza")
                            ||!a.getSmap1().containsKey("pv_nmsituac")
                            ||!a.getSmap1().containsKey("pv_cdperson")
                            )
			{
				return "denied";
			}
			else
			{
				return a.mostrarPantallaDomicilio();
			}
		}
		
        else if(screen==PANTALLA_COMPLEMENTARIOS_EXCLUSION_ASEGURADO)
		{
			ComplementariosCoberturasAction a=(ComplementariosCoberturasAction)action;
			if(ActionContext.getContext().getSession()==null
                            ||ActionContext.getContext().getSession().get("USUARIO")==null
                            ||a.getSmap1()==null
                    		||a.getSmap1().isEmpty()
                    		||!a.getSmap1().containsKey("pv_cdunieco")
                            ||!a.getSmap1().containsKey("pv_cdramo")
                            ||!a.getSmap1().containsKey("pv_estado")
                            ||!a.getSmap1().containsKey("pv_nmpoliza")
                            ||!a.getSmap1().containsKey("pv_nmsituac")
                            ||!a.getSmap1().containsKey("pv_cdperson")
                            )
			{
				return "denied";
			}
			else
			{
				return a.mostrarPantallaExclusion();
			}
		}
		
		return "denied";
	}
	
}
