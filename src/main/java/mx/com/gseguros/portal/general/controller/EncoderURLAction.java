package mx.com.gseguros.portal.general.controller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("EncoderURLAction")
@Scope("prototype")
@ParentPackage(value="struts-default")
@Namespace("/seguridad")
public class EncoderURLAction extends PrincipalCoreAction {
	
	private static Logger logger = LoggerFactory.getLogger(EnvironmentAction.class);
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> params;
	
	private InputStream fileInputStream;
	protected String contentType;
	
	@Autowired
    private EmisionDAO emisionDAO;
	
	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
	
	@Value("${pass.servidor.reports}")
    private String passServidorReports;	
	
	/**
	 * Obtiene el flujo de bytes de un reporte
	 * @return 
	 * @throws Exception
	 */
	@Action
	(
		value   = "redireccionaReporte",
		results = 
	    {
				@Result(name="success", 
						type="stream", 
						params = {
							"contentType"       ,"${contentType}",
							"inputName"         ,"fileInputStream",
							"bufferSize"        ,"4096"
							// opcional "contentDisposition", "attachment; filename=\"${filename}\""
						}
				)
		}
	)
	public String redireccionaReporte() {
		try 
		{
			StringBuilder url = new StringBuilder()
			.append(rutaServidorReports)
            .append("?userid=").append(passServidorReports)
            .append("&destype=cache")
            .append("&desformat=PDF")
            .append("&ACCESSIBLE=YES")
            .append("&paramform=no");
			
			if(StringUtils.isNotBlank(params.get("report")))
			{
				for(Entry<String,String>en:params.entrySet())
	            {
	            	String key=en.getKey();
	            	String value=en.getValue();
	            	
	            	if(StringUtils.isNotBlank(value))
	            	{
	            		url.append("&").append(key).append("=").append(value.trim());
	            	}
	            }
				logger.debug(url.toString());
			}
			else
			{
				throw new ApplicationException("Falta Reporte");
			}
			
			fileInputStream = HttpUtil.obtenInputStream(url.toString());
			
		} catch(Exception e) {
			logger.error(contentType, new StringBuilder("Error: ").append(e.getMessage()), e);
		}
		return SUCCESS;
	}

	/**
     * Obtiene el flujo de bytes de un reporte
     * @return 
     * @throws Exception
     */
    @Action
    (
        value   = "redireccionaReporteVidaAuto",
        results = 
        {
                @Result(name="success", 
                        type="stream", 
                        params = {
                            "contentType"       ,"${contentType}",
                            "inputName"         ,"fileInputStream",
                            "bufferSize"        ,"4096"
                            // opcional "contentDisposition", "attachment; filename=\"${filename}\""
                        }
                )
        }
    )
    public String redireccionaReporteVidaAuto() {
        try 
        {
            StringBuilder url = new StringBuilder()
            .append(rutaServidorReports)
            .append("?userid=").append(passServidorReports)
            .append("&destype=cache")
            .append("&desformat=PDF")
            .append("&ACCESSIBLE=YES")
            .append("&paramform=no");
            
            if(StringUtils.isNotBlank(params.get("poliza")))
            {
               char[] caracteres = params.get("poliza").toCharArray();
               Integer cdunieco  = null,
                       cdramo    = null,
                       nmpoliza  = null;
               String endoso     = "",
                      tipoEndoso = "",
                      report     = params.get("report");
                     
               Integer numVal=0;
               String valor="";
               Integer caracteresLong = caracteres.length+1;
               for (int x=0;x<caracteresLong;x++)
               {   
                   if(x<caracteres.length && caracteres[x] !=',')
                   {
                       valor += String.valueOf(caracteres[x]);
                   }
                   else
                   { 
                       numVal++;
                       switch(numVal)
                       {
                         case 1:
                            try {
                                cdunieco    =Integer.parseInt(valor.trim());
                            } catch (Exception e) {
                                throw new ApplicationException("Parametro cdunieco erroneo");
                            }
                            logger.debug(Utils.log("sucursal: ",cdunieco));
                            valor="";
                         break;
                         
                         case 2:
                             try {
                                 cdramo         =Integer.parseInt(valor.trim());
                             } catch (Exception e) {
                                 throw new ApplicationException("Parametro cdramo erroneo");
                             }
                             
                             logger.debug(Utils.log("ramo: ",cdramo));
                             valor="";
                          break;
                          
                         case 3:
                             try {
                                 nmpoliza       =Integer.parseInt(valor.trim());
                             } catch (Exception e) {
                                 throw new ApplicationException("Parametro nmpoliza erroneo");
                             }
                             logger.debug(Utils.log("poliza: ",nmpoliza));
                             valor="";
                          break;
                          
                         case 4:
                             tipoEndoso     =valor.trim();
                             try {
                                 if(!tipoEndoso.equals("") && !tipoEndoso.equals("A") && !tipoEndoso.contains("D"))
                                 {
                                     throw new ApplicationException("Parametros tipoEndoso erroneo solo admite blanco o 'A' o 'D'");
                                 }
                             } catch (Exception e) {
                                 throw new ApplicationException("Parametro tipoEndoso erroneo");
                             }
                             logger.debug(Utils.log("tipoEndoso: ",tipoEndoso));
                             valor="";
                          break;
                          
                         case 5:
                             endoso         =valor.trim();
                             try {
                                 Integer.parseInt(endoso);
                             } catch (Exception e) {
                                 throw new ApplicationException("Parametro endoso erroneo");
                             }
                             logger.debug(Utils.log("endoso: ",endoso));
                             valor="";
                          break;
                          
                         default:
                             logger.error(contentType, new StringBuilder("Error: ").append("Parametros excedidos ignorados"));
                             throw new ApplicationException("Parametros ingresados excedidos");
                       }
                   }
               }
               
               Map<String,String> lista =emisionDAO.redireccionaReporteVidaAuto(cdunieco, cdramo, nmpoliza, tipoEndoso, endoso);
               
               if(StringUtils.isNotBlank(params.get("report")))
               {
                   url.append("&").append("report").append("=").append(params.get("report").trim());
                   for(Entry<String,String>en:lista.entrySet())
                   {
                       String key=en.getKey();
                       String value=en.getValue();
                       
                       if(StringUtils.isNotBlank(value))
                       {
                           url.append("&").append(key).append("=").append(value.trim());
                       }
                   }
                   logger.debug(url.toString());
               }
            }
            else
            {
                throw new ApplicationException("Faltan Datos de Poliza");
            }
            fileInputStream = HttpUtil.obtenInputStream(url.toString());
            
        } catch(Exception e) {
            logger.error(contentType, new StringBuilder("Error: ").append(e.getMessage()), e);
        }
        return SUCCESS;
    }
	
//Getters & setters

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

    public EmisionDAO getEmisionDAO() {
        return emisionDAO;
    }

    public void setEmisionDAO(EmisionDAO emisionDAO) {
        this.emisionDAO = emisionDAO;
    }
    
	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}
}