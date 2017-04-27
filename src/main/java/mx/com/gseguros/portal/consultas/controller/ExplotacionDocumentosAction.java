package mx.com.gseguros.portal.consultas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.DescargaLotePdfVO;
import mx.com.gseguros.portal.consultas.model.ImpresionLayoutVO;
import mx.com.gseguros.portal.consultas.service.ExplotacionDocumentosManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.general.util.TipoArchivo;
import mx.com.gseguros.utils.Utils;

@Controller("explotacionDocumentosAction")
@Scope("prototype")
@ParentPackage(value="default")
@Namespace("/consultas")
public class ExplotacionDocumentosAction extends PrincipalCoreAction
{
	private final static Logger logger = LoggerFactory.getLogger(ExplotacionDocumentosAction.class);
	
	private Map<String,String>       params;
	private List<Map<String,String>> list;
	private boolean                  success;
	private String                   message;
	private Map<String,Item>         items;
	private InputStream              fileInputStream;
	private String                   filename;
	private String                   contentType;
	private List<String>			 tramites;
	private boolean 				 dwnError;
	
	@Autowired
	private ExplotacionDocumentosManager explotacionDocumentosManager;
	@Value("${user.server.layouts}")
    private String userServerLayouts;	

	@Value("${pass.server.layouts}")
    private String passServerLayouts;	

	@Value("${directorio.server.layouts}")
    private String directorioServerLayouts;	

	@Value("${dominio.server.layouts}")
    private String dominioServerLayouts;	

	@Value("${dominio.server.layouts2}")
    private String dominioServerLayouts2;	

	public ExplotacionDocumentosAction()
	{
		this.session=ActionContext.getContext().getSession();
	}
	
	@Action(value   = "pantallaExplotacionDocumentos",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/consultas/pantallaExplotacionDocumentos.jsp")
            }
	)
	public String pantallaExplotacionDocumentos()
	{
		logger.debug(Utils.log(
				 "\n###########################################"
				,"\n###### pantallaExplotacionDocumentos ######"
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			items = explotacionDocumentosManager.pantallaExplotacionDocumentos(usuario.getUser(),usuario.getRolActivo().getClave());
			if(params==null){
			    params=new HashMap<String, String>();
			}
			params.put("cdusuari", usuario.getUser());
			params.put("cdagente", explotacionDocumentosManager.obtenerCdagente(usuario.getUser()));
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaExplotacionDocumentos ######"
				,"\n###########################################"
				));
		return result;
	}
	
	@Action(value           = "generarLote",
			results         = { @Result(name="success", type="json") },
            interceptorRefs = {
			    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
			})
	public String generarLote()
	{
		logger.debug(Utils.log(
				 "\n#########################"
				,"\n###### generarLote ######"
				,"\n###### params=" , params
				,"\n###### list="   , list
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron par\u00E1metros");
			
			String cdtipram  = params.get("cdtipram")
			       ,cdtipimp = params.get("cdtipimp")
			       ,tipolote = params.get("tipolote");
			
			Utils.validate(
					cdtipram  , "No se recibi\u00F3 el tipo de ramo"
					,cdtipimp , "No se recibi\u00F3 el tipo de impresi\u00F3n"
					,tipolote , "No se recibi\u00F3 el tipo de lote"
					);
			
			if(!"P".equals(tipolote)
					&&!"R".equals(tipolote))
			{
				throw new ApplicationException("Tipo de lote incorrecto");
			}
			
			Utils.validate(list , "No se recibieron movimientos");
			
			for(Map<String,String>mov:list)
			{
				Utils.validate(
						mov.get("cdunieco")  , "Los movimientos no tienen sucursal"
						,mov.get("cdramo")   , "Los movimientos no tienen producto"
						,mov.get("estado")   , "Los movimientos no tienen estado"
						,mov.get("nmpoliza") , "Los movimientos no tienen p\u00F3liza"
						,mov.get("nmsuplem") , "Los movimientos no tienen suplemento"
						,mov.get("cdagente") , "Los movimientos no tienen agente"
						);
				
				if("P".equals(tipolote))
				{
					Utils.validate(mov.get("ntramite") , "Los movimientos no tienen tr\u00E1mite");
				}
				else
				{
					Utils.validate(mov.get("nmrecibo") , "Los movimientos no tienen n\u00FAmero de recibo");
				}
				
			}
			
			String lote = explotacionDocumentosManager.generarLote(
					usuario.getUser()
					,usuario.getRolActivo().getClave()
					,cdtipram
					,cdtipimp
					,tipolote
					,list
					);
			
			params.put("lote" , lote);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### generarLote ######"
				,"\n#########################"
				));
		
		return SUCCESS;
	}
	
	@Action(value   = "imprimirLote",
			results = { @Result(name="success", type="json") }
	)
	public String imprimirLote()
	{
		logger.debug(Utils.log(
				 "\n##########################"
				,"\n###### imprimirLote ######"
				,"\n###### params ######",params
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params , "No se recibieron datos");
			
			String lote     = params.get("lote");
			String hoja     = params.get("hoja");
			String peso     = params.get("peso");
			String cdtipram = params.get("cdtipram");
			String cdtipimp = params.get("cdtipimp");
			String tipolote = params.get("tipolote");
			String dsimpres = params.get("dsimpres");
			String charola1 = params.get("charola1");
			String charola2 = params.get("charola2");
			String test     = params.get("test");
			String soportaDuplex   = params.get("swimpdpx"); // Indica si la impresora soporta la opcion duplex
			
			Utils.validate(
					lote      , "No se recibi\u00F3 el lote"
					,hoja     , "No se recibi\u00F3 el tipo de hoja"
					,peso     , "No se recibi\u00F3 el peso"
					,cdtipram , "No se recibi\u00F3 el tipo de ramo"
					,cdtipimp , "No se recibi\u00F3 el tipo de impresi\u00F3n"
					,tipolote , "No se recibi\u00F3 el tipo de lote"
					,dsimpres , "No se recibi\u00F3 la impresora"
					,charola1 , "No se recibi\u00F3 la charola"
					);
			
			File noExiste=explotacionDocumentosManager.imprimirLote(
					lote
					,hoja
					,peso
					,cdtipram
					,cdtipimp
					,tipolote
					,dsimpres
					,charola1
					,charola2
					,cdusuari
					,cdsisrol
					,"S".equals(test)
					,"S".equals(soportaDuplex)
					);
			if(noExiste!=null){
				dwnError=true;
				session.put("fileDownErr", noExiste);
			}
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### imprimirLote ######"
				,"\n##########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "pantallaExplotacionRecibos",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/consultas/pantallaExplotacionRecibos.jsp")
            }
	)
	public String pantallaExplotacionRecibos()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### pantallaExplotacionRecibos ######"
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			items = explotacionDocumentosManager.pantallaExplotacionRecibos(usuario.getUser(),usuario.getRolActivo().getClave());
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaExplotacionRecibos ######"
				,"\n########################################"
				));
		return result;
	}
	
	@Action(value   = "pantallaPermisosImpresion",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/consultas/pantallaPermisosImpresion.jsp")
            }
	)
	public String pantallaPermisosImpresion()
	{
		logger.debug(Utils.log(
				 "\n#######################################"
				,"\n###### pantallaPermisosImpresion ######"
				));
		
		String result = ERROR;
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			items = explotacionDocumentosManager.pantallaPermisosImpresion(usuario.getUser(),usuario.getRolActivo().getClave());
			
			result = SUCCESS;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### pantallaPermisosImpresion ######"
				,"\n#######################################"
				));
		return result;
	}
	
	@Action(value   = "movPermisoImpresion",
			results = { @Result(name="success", type="json") }
	)
	public String movPermisoImpresion()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### movPermisoImpresion ######"
				,"\n###### params=",params
				));
		
		try
		{
			Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String tipo     = params.get("tipo");
			String cdusuari = params.get("cdusuari");
			String cdunieco = params.get("cdunieco");
			String cdtipram = params.get("cdtipram");
			String clave    = params.get("clave");
			String funcion  = params.get("funcion");
			String accion   = params.get("accion");
			
			Utils.validate(
					tipo      , "No se recibi\u00F3 el tipo"
					,cdusuari , "No se recibi\u00F3 el usuario"
					,cdunieco , "No se recibi\u00F3 la sucursal"
					,cdtipram , "No se recibi\u00F3 el tipo de ramo"
					,clave    , "No se recibi\u00F3 la clave"
					,funcion  , "No se recibi\u00F3 el switch"
					,accion   , "No se recibi\u00F3 la operaci\u00F3n"
					);
			
			explotacionDocumentosManager.movPermisoImpresion(
					tipo
					,cdusuari
					,cdunieco
					,cdtipram
					,clave
					,funcion
					,accion
					);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### movPermisoImpresion ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "actualizarStatusRemesa",
			results = { @Result(name="success", type="json") }
	)
	public String actualizarStatusRemesa()
	{
		logger.debug(Utils.log(
				 "\n####################################"
				,"\n###### actualizarStatusRemesa ######"
				,"\n###### params=",params
				,"\n###### tramites=",tramites
				));
		
		try
		{
			UserVO usuario  = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params , "No se recibieron datos");
			Utils.validate(tramites , "No se recibieron tramites");
			
			String status   = params.get("status");
			
			Utils.validate(
					status  , "No se recibi\u00F3 el status"
					);
			
			explotacionDocumentosManager.actualizarStatusRemesa(tramites,status,cdusuari,cdsisrol);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### actualizarStatusRemesa ######"
				,"\n####################################"
				));
		return SUCCESS;
		
	}
	
	
	
	
	/*Action(value   = "generarRemesaEmisionEndoso",
			results = { @Result(name="success", type="json") }
	)
	public String generarRemesaEmisionEndoso()
	{
		logger.debug(Utils.log(
				 "\n########################################"
				,"\n###### generarRemesaEmisionEndoso ######"
				,"\n###### params=",params
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdunieco = params.get("cdunieco");
			String cdramo   = params.get("cdramo");
			String estado   = params.get("estado");
			String nmpoliza = params.get("nmpoliza");
			String cdtipimp = params.get("cdtipimp");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00F3 la sucursal"
					,cdramo   , "No se recibi\u00F3 el producto"
					,estado   , "No se recibi\u00F3 el estado de p\u00F3liza"
					,nmpoliza , "No se recibi\u00F3 el n\u00famero de p\u00F3liza"
					,cdtipimp , "No se recibi\u00F3 el tipo de impresi\u00F3n"
			);
			
			Map<String,String> datosRemesa = explotacionDocumentosManager.generarRemesaEmisionEndoso(
					usuario.getUser()
					,usuario.getRolActivo().getClave()
					,cdtipimp
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					);
			
			params.putAll(datosRemesa);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### params="  , params
				,"\n###### generarRemesaEmisionEndoso ######"
				,"\n########################################"
				));
		return SUCCESS;
	}
	*/
	
	@Action(value   = "marcarImpresionOperacion",
			results = { @Result(name="success", type="json") }
	)
	public String marcarImpresionOperacion()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### marcarImpresionOperacion ######"
				,"\n###### params=",params
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdunieco   = params.get("cdunieco");
			String cdramo     = params.get("cdramo");
			String estado     = params.get("estado");
			String nmpoliza   = params.get("nmpoliza");
			String marcar     = params.get("marcar");
			String ntramiteIn = params.get("ntramiteIn");
			
			Utils.validate(
					cdunieco  , "No se recibi\u00F3 la sucursal"
					,cdramo   , "No se recibi\u00F3 el producto"
					,estado   , "No se recibi\u00F3 el estado de p\u00F3liza"
					,nmpoliza , "No se recibi\u00F3 el n\u00famero de p\u00F3liza"
			);
			
			Map<String,String> preguntarMarcado = explotacionDocumentosManager.marcarImpresionOperacion(
					usuario.getUser()
					,usuario.getRolActivo().getClave()
					,cdunieco
					,cdramo
					,estado
					,nmpoliza
					,marcar
					,ntramiteIn
					);
			
			params.putAll(preguntarMarcado);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### params="  , params
				,"\n###### marcarImpresionOperacion ######"
				,"\n######################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "recuperarColumnasGridPol",
			results = { @Result(name="success", type="json") }
	)
	public String recuperarColumnasGridPol()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### recuperarColumnasGridPol ######"
				,"\n###### params=",params
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			
			Utils.validate(params , "No se recibieron datos");
			
			String cdtipram = params.get("cdtipram");
			String pantalla = params.get("pantalla");
			
			Utils.validate(
					cdtipram  , "No se recibi\u00F3 el tipo de ramo"
					,pantalla , "No se recibi\u00F3 la pantalla"
			);
			
			String columns = explotacionDocumentosManager.recuperarColumnasGridPol(
					usuario.getRolActivo().getClave()
					,cdtipram
					,pantalla
					);
			
			params.put("columns" , columns);
			
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### params="  , params
				,"\n###### recuperarColumnasGridPol ######"
				,"\n######################################"
				));
		return SUCCESS;
	}
	
	public String descargarLote()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### descargarLote ######"
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params , "No se recibieron datos");
			
			String lote     = params.get("lote");
			String hoja     = params.get("hoja");
			String peso     = params.get("peso");
			String cdtipram = params.get("cdtipram");
			String cdtipimp = params.get("cdtipimp");
			String tipolote = params.get("tipolote");
			
			Utils.validate(
					lote      , "No se recibi\u00F3 el lote"
					,hoja     , "No se recibi\u00F3 el tipo de hoja"
					,peso     , "No se recibi\u00F3 el peso"
					,cdtipram , "No se recibi\u00F3 el tipo de ramo"
					,cdtipimp , "No se recibi\u00F3 el tipo de impresi\u00F3n"
					,tipolote , "No se recibi\u00F3 el tipo de lote"
					);
			
			DescargaLotePdfVO dlp = explotacionDocumentosManager.descargarLote(
					lote
					,hoja
					,peso
					,cdtipram
					,cdtipimp
					,tipolote
					,cdusuari
					,cdsisrol
					);
			fileInputStream=dlp.getFileInput();
			
			contentType = TipoArchivo.PDF.getContentType();
			filename    = Utils.join("descarga_lote_",lote,"_papel_",hoja,".pdf");
			
			success = true;
			session.put("fileDownErr", dlp.getErrores());
			session.put("descargarLote" , "S");
			logger.debug(Utils.log(
					"\n###### fileError=  ",dlp.getErrores()));
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
			session.put("descargarLote" , message);
		}
		
		logger.debug(Utils.log(
				 "\n###### descargarLote ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	public String descargarLoteDplx()
	{
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### descargarLoteDplx ######"
				));
		
		try
		{
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params , "No se recibieron datos");
			
			String lote     = params.get("lote");
			String hoja     = params.get("hoja");
			String peso     = params.get("peso");
			String cdtipram = params.get("cdtipram");
			String cdtipimp = params.get("cdtipimp");
			String tipolote = params.get("tipolote");
			
			Utils.validate(
					lote      , "No se recibi\u00F3 el lote"
					,hoja     , "No se recibi\u00F3 el tipo de hoja"
					,peso     , "No se recibi\u00F3 el peso"
					,cdtipram , "No se recibi\u00F3 el tipo de ramo"
					,cdtipimp , "No se recibi\u00F3 el tipo de impresi\u00F3n"
					,tipolote , "No se recibi\u00F3 el tipo de lote"
					);
			
			
			DescargaLotePdfVO dlp = explotacionDocumentosManager.descargarLoteDplx(
					lote
					,hoja
					,peso
					,cdtipram
					,cdtipimp
					,tipolote
					,cdusuari
					,cdsisrol
					);
			
			fileInputStream=dlp.getFileInput();
			
			contentType = TipoArchivo.PDF.getContentType();
			filename    = Utils.join("descarga_lote_",lote,"_papel_",hoja,".pdf");
			
			success = true;
			session.put("fileDownErr",dlp.getErrores());
			session.put("descargarLote" , "S");
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
			session.put("descargarLote" , message);
		}
		
		logger.debug(Utils.log(
				 "\n###### descargarLoteDplx ######"
				,"\n###########################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "esperarDescargaLote",
			results = { @Result(name="success", type="json") }
	)
	public String esperarDescargaLote()
	{
		logger.debug(Utils.log(
				 "\n#################################"
				,"\n###### esperarDescargaLote ######"
				));
		
		try
		{
			Utils.validateSession(session);
			
			String descargarLote = null;
			File errores=null;
			
			while(StringUtils.isBlank(descargarLote))
			{
				descargarLote = (String)session.get("descargarLote");
				Thread.sleep(250l);
			}
			errores=(File)session.get("fileDownErr");
			session.put("descargarLote" , "");
			if("S".equals(descargarLote) && errores==null)
			{
				success = true;
			}
			else if(errores!=null){
				success=true;
				dwnError=true;
			}
			else
			{
				message = descargarLote;
			}
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### esperarDescargaLote ######"
				,"\n#################################"
				));
		return SUCCESS;
	}
	
	public String descargaListaError(){
		
		logger.debug(Utils.log(
				 "\n################################"
				,"\n###### descargaListaError ######"
				));
		
		try{
			File err=(File) session.get("fileDownErr");
			fileInputStream=new FileInputStream(err);
			contentType = TipoArchivo.CSV.getContentType();
			filename    = Utils.join("lista_errores.csv");
			session.remove("fileDownErr");
		}
		catch(Exception ex)
		{
			success=false;
			message = Utils.manejaExcepcion(ex);
		}
		
		success=true;
		
		logger.debug(Utils.log(
				 "\n###### descargaListaError ########"
				,"\n##################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "pantallaDescargaDocumentosLayout",
	        results = {
			    @Result(name="error"   , location="/jsp-script/general/errorPantalla.jsp"),
                @Result(name="success" , location="/jsp-script/consultas/pantallaDescargaDocumentosLayout.jsp")
            }
	)
	public String pantallaDescargaDocumentosLayout(){
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### pantallaDescargaDocumentosLayout ######"
				));
		
		logger.debug(Utils.log(
				 "\n###### pantallaDescargaDocumentosLayout ################"
				,"\n########################################################"
				));
		return SUCCESS;
	}

	public String descargaLayout(){
		
		logger.debug(Utils.log(
				 "\n############################"
				,"\n###### descargaLayout ######"
				));
		
		try{
			String cdtipram=params.get("cdtipram");
			Map<String, String> map=(Map<String, String>) session.get("layout.datos.para.documentos");
			
			DescargaLotePdfVO datos = explotacionDocumentosManager.generaPdfLayout(map,
																					cdtipram, 
																					"B",
																					params.get("duplex").equals("S"));
			
			session.put("fileDownErr",datos.getErrores());
			fileInputStream=datos.getFileInput();
			
			contentType = TipoArchivo.PDF.getContentType();
			filename    = Utils.join("layout.pdf");
			session.put("descargarLote" , "S");
			
			
		}catch(Exception ex){
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### descargaLayout ################"
				,"\n#######################################"
				));
		
		success=true;
		return SUCCESS;
	}
	@Action(value           = "verificaLayout",
			results         = { @Result(name="success", type="json") },
					interceptorRefs = {
						    @InterceptorRef(value = "json", params = {"enableSMD", "true", "ignoreSMDMethodInterfaces", "false" })
						}
            )
	public String verificaLayout(){
		
		
		logger.debug(Utils.log(
				 "\n###########################"
				,"\n###### verificaLayout ######"
				));
		logger.debug(Utils.log(
				 "\n###### params= ",params,
				 "\n###### list= ",list
				 ));
		
		try{
			
			UserVO usuario = Utils.validateSession(session);
			String cdusuari = usuario.getUser();
			String cdsisrol = usuario.getRolActivo().getClave();
			
			Utils.validate(params , "No se recibieron datos");
			
			String tpdocum  = params.get("tpdocum");
			String cdramo   = params.get("cdramo");
			
			Utils.validate(
					tpdocum      , "No se recibi\u00F3 el tipo de documento"
					,cdramo     , "No se recibi\u00F3 el ramo"
					
					);
		    ImpresionLayoutVO lay=explotacionDocumentosManager.verificaLayout(
					  list
					, tpdocum
					, cdusuari
					, cdsisrol
					, this.userServerLayouts
					, this.passServerLayouts
					, this.directorioServerLayouts
					, this.dominioServerLayouts
					, this.dominioServerLayouts2
					);
		    list=lay.getValidacion();
		    session.put("layout.datos.para.documentos", lay.getDocumentos());
			success =true;
			
		}catch(Exception ex){
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### verificaLayout ###########"
				,"\n#################################"
				));
		return SUCCESS;
		
	}
	
	
	@Action(value   = "borrarDatosLayout",
			results = { @Result(name="success", type="json") }
	)
	public String borrarDatosLayout()
	{
		logger.debug(Utils.log(
				 "\n######################################"
				,"\n###### borrarDatosLayout ######"
				,"\n###### params=",params
				));
		
		try
		{
			String pv_idproceso_i;
			Map<String, String> map=(Map<String, String>) session.get("layout.datos.para.documentos");
			pv_idproceso_i=map.get("pv_idproceso_i");
			message=explotacionDocumentosManager.borrarDatosLayout(pv_idproceso_i);
			success = true;
		}
		catch(Exception ex)
		{
			message = Utils.manejaExcepcion(ex);
		}
		
		logger.debug(Utils.log(
				 "\n###### success=" , success
				,"\n###### message=" , message
				,"\n###### params="  , params
				,"\n###### marcarImpresionOperacion ######"
				,"\n######################################"
				));
		return SUCCESS;
	}
	
	@Action(value   = "polizasImprimirPromotor",
            results = { @Result(name="success", type="json") }
    )
    public String polizasImprimirPromotor()
    {
        logger.debug(Utils.log(
                 "\n######################################"
                ,"\n###### polizasImprimirPromotor ######"
                ,"\n###### params=",params
                ));
        
        try
        {
            

            
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
            UserVO usuario  = Utils.validateSession(session);
            String cdusuari = usuario.getUser();
            String cdsisrol = usuario.getRolActivo().getClave();
            
            list=explotacionDocumentosManager.polizasImprimirPromotor(
                                                    cdusuari, 
                                                    cdtipram, 
                                                    cduniecos, 
                                                    cdramo, 
                                                    ramo, 
                                                    nmpoliza, 
                                                    fefecha, 
                                                    cdusuariLike, 
                                                    cdagente, 
                                                    cdusuari, 
                                                    cdsisrol, 
                                                    usuario);
            success = true;
        }
        catch(Exception ex)
        {
            message = Utils.manejaExcepcion(ex);
        }
        
        logger.debug(Utils.log(
                 "\n###### success=" , success
                ,"\n###### message=" , message
                ,"\n###### params="  , params
                ,"\n###### polizasImprimirPromotor ######"
                ,"\n######################################"
                ));
        return SUCCESS;
    }
	
	////////////////// Getters y setters ///////////////////////////
	                                                              //
	public Map<String, String> getParams() {                      //
		return params;                                            //
	}                                                             //
	                                                              //
	public void setParams(Map<String, String> params) {           //
		this.params = params;                                     //
	}                                                             //
	                                                              //
	public List<Map<String, String>> getList() {                  //
		return list;                                              //
	}                                                             //
	                                                              //
	public void setList(List<Map<String, String>> list) {         //
		this.list = list;                                         //
	}                                                             //
	                                                              //
	public boolean isSuccess() {                                  //
		return success;                                           //
	}                                                             //
	                                                              //
	public void setSuccess(boolean success) {                     //
		this.success = success;                                   //
	}                                                             //
	                                                              //
	public String getMessage() {                                  //
		return message;                                           //
	}                                                             //
	                                                              //
	public void setMessage(String message) {                      //
		this.message = message;                                   //
	}                                                             //
	                                                              //
	public Map<String, Item> getItems() {                         //
		return items;                                             //
	}                                                             //
                                                                  //
	public void setItems(Map<String, Item> items) {               //
		this.items = items;                                       //
	}                                                             //
                                                                  //
	public InputStream getFileInputStream() {                     //
		return fileInputStream;                                   //
	}                                                             //
                                                                  //
	public void setFileInputStream(InputStream fileInputStream) { //
		this.fileInputStream = fileInputStream;                   //
	}                                                             //
                                                                  //
	public String getFilename() {                                 //
		return filename;                                          //
	}                                                             //
                                                                  //
	public void setFilename(String filename) {                    //
		this.filename = filename;                                 //
	}                                                             //
                                                                  //
	public String getContentType() {                              //
		return contentType;                                       //
	}                                                             //
                                                                  //
	public void setContentType(String contentType) {              //
		this.contentType = contentType;                           //
	} 
	
	
	
	public boolean isDwnError() {
		return dwnError;
	}

	public void setDwnError(boolean dwnError) {
		this.dwnError = dwnError;
	}

	public List<String> getTramites() {
		return tramites;
	}

	public void setTramites(List<String> tramites) {
		this.tramites = tramites;
	}
	
	public String getUserServerLayouts() {
		return userServerLayouts;
	}

	public String getPassServerLayouts() {
		return passServerLayouts;
	}

	public String getDirectorioServerLayouts() {
		return directorioServerLayouts;
	}

	public String getDominioServerLayouts() {
		return dominioServerLayouts;
	}

	public String getDominioServerLayouts2() {
		return dominioServerLayouts2;
	}


	
	//
                                                                  //
    ////////////////////////////////////////////////////////////////
}