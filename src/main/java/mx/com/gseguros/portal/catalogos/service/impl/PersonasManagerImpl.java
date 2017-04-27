package mx.com.gseguros.portal.catalogos.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.catalogos.dao.ClienteDAO;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.catalogos.service.PersonasManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ParametroEndoso;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.portal.general.util.TipoSituacion;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;

@Service
public class PersonasManagerImpl implements PersonasManager
{
	private final static org.apache.log4j.Logger logger       = org.apache.log4j.Logger.getLogger(PersonasManagerImpl.class);
	private final static org.slf4j.Logger        logger2      = org.slf4j.LoggerFactory.getLogger(PersonasManagerImpl.class);
	private final static SimpleDateFormat        renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	
	private Map<String,Object>session;
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private PersonasDAO  personasDAO;
	
	@Autowired
	private EndososDAO   endososDAO;
	
	@Autowired
	private ClienteDAO  clienteDAOSIGS;
	
	@Autowired
	private AutosSIGSDAO autosSIGSDAO;
	
	/**
	 * Carga los componentes de la pantalla de personas
	 * @return exito,respuesta,respuestaOculta,itemMap
	 */
	@Override
	public Map<String,Object> pantallaPersonas(String cdsisrol,long timestamp) throws Exception
	{
		Map<String,Object>result=new HashMap<String,Object>();
		logger.info(timestamp
				+ "\n##############################"
				+ "\n###### pantallaPersonas ######"
				+ "\ncdsisrol: "+cdsisrol
				);
		boolean          exito           = true;
		String           respuesta       = null;
		String           respuestaOculta = null;
		String           pantalla        = "PANTALLA_PERSONAS";
		GeneradorCampos  gc              = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
		Map<String,Item> itemMap         = new HashMap<String,Item>();
		
		//componentes de busqueda
		if(exito)
		{
			try
			{
				String seccion = "BUSQUEDA";
				List<ComponenteVO>busqueda=pantallasDAO.obtenerComponentes(null,null,null,null,null,cdsisrol,pantalla,seccion,null);
				gc.generaComponentes(busqueda, true, false, true, false, false, false);
				itemMap.put(seccion,gc.getItems());
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener componentes de pantalla",ex);
				exito           = false;
				respuesta       = "Error al obtener componentes de pantalla #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		//modelo y columnas del grid
		if(exito)
		{
			try
			{
				String seccion = "MODELO_Y_GRID";
				List<ComponenteVO>fieldsYColumns=pantallasDAO.obtenerComponentes(null,null,null,null,null,cdsisrol,pantalla,seccion,null);
				gc.generaComponentes(fieldsYColumns, true,true,true,true,false,false);
				itemMap.put("gridModelFields"     , gc.getFields());
				itemMap.put("gridColumns"         , gc.getColumns());
				itemMap.put("datosGeneralesItems" , gc.getItems());
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener componentes de pantalla",ex);
				exito           = false;
				respuesta       = "Error al obtener componentes de pantalla #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			try
			{
				String seccion = "DOMICILIO";
				List<ComponenteVO>componentesDomicilio=pantallasDAO.obtenerComponentes(
						null,null,null,null,null,cdsisrol,pantalla,seccion,null);
				gc.generaComponentes(componentesDomicilio, true,true,true,false,false,false);
				itemMap.put("fieldsDomicilio" , gc.getFields());
				itemMap.put("itemsDomicilio"  , gc.getItems());
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener campos de domicilio",ex);
				exito           = false;
				respuesta       = "Error al obtener componentes de pantalla #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("itemMap"         , itemMap);
		logger.info(timestamp
				+ "\nresult: "+result
				+ "\n###### pantallaPersonas ######"
				+ "\n##############################"
				);
		return result;
	}
	
	@Override
	public Map<String,String> obtieneMunicipioYcolonia(Map<String, String> params) throws Exception{
		return personasDAO.obtieneMunicipioYcolonia(params);
	}

	@Override
	public void actualizaCodigoExterno(Map<String, String> params) throws Exception{
		personasDAO.actualizaCodigoExterno(params);
	}
	
	/**
	 * Buscar personas por RFC de PKG_CONSULTA.P_GET_MPERSONA
	 * @return exito,respuesta,respuestaOculta,listaPersonas
	 */
	@Override
	public Map<String,Object> obtenerPersonasPorRFC(String rfc,String nombre,String snombre,String apat,String amat, String validaTienePoliza, long timestamp) throws Exception
	{
		Map<String,Object>result=new HashMap<String,Object>();
		logger.info(timestamp
				+ "\n###################################"
				+ "\n###### obtenerPersonasPorRFC ######"
				+ "\nrfc: "+rfc
				);
		boolean exito                         = true;
		String  respuesta                     = null;
		String  respuestaOculta               = null;
		List<Map<String,String>>listaPersonas = null;
		
		if(exito)
		{
			try
			{
				listaPersonas=personasDAO.obtenerPersonasPorRFC(rfc, nombre, snombre, apat, amat, validaTienePoliza);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener personas por rfc",ex);
				exito           = false;
				respuesta       = "Error al obtener personas #"+timestamp;
				respuestaOculta = ex.getMessage();
				listaPersonas   = new ArrayList<Map<String,String>>();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("listaPersonas"   , listaPersonas);
		logger.info(timestamp+""
				+ "\nresult"+result
				+ "\n###### obtenerPersonasPorRFC ######"
				+ "\n###################################"
				);
		return result;
	}
	
	@Override
	public Map<String,Object> obtenerPersonaPorCdperson(String cdperson,long timestamp) throws Exception
	{
		Map<String,Object>result=new HashMap<String,Object>();
		logger.info(timestamp
				+ "\n#######################################"
				+ "\n###### obtenerPersonaPorCdperson ######"
				+ "\ncdperson: "+cdperson
				);
		boolean exito             = true;
		String  respuesta         = null;
		String  respuestaOculta   = null;
		Map<String,String>persona = null;
		
		if(exito)
		{
			try
			{
				Map<String,String>params=new HashMap<String,String>();
				params.put("pv_cdperson_i" , cdperson);
				persona=personasDAO.obtenerPersonaPorCdperson(params);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener persona por cdperson",ex);
				exito           = false;
				respuesta       = "Error al obtener datos de persona #"+timestamp;
				respuestaOculta = ex.getMessage();
				persona         = new HashMap<String,String>();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("persona"         , persona);
		logger.info(timestamp+""
				+ "\nresult"+result
				+ "\n###### obtenerPersonaPorCdperson ######"
				+ "\n#######################################"
				);
		return result;
	}
	
	/**
	 * Guardar pantalla de personas
	 * @return exito,respuesta,respuestaOculta,cdpersonNuevo
	 */
	@Override
	public Map<String,Object> guardarPantallaPersonas(String cdperson
			,String cdtipide
			,String cdideper
			,String dsnombre
			,String cdtipper
			,String otfisjur
			,String otsexo
			,Date   fenacimi
			,String cdrfc
			,String dsemail
			,String dsnombre1
			,String dsapellido
			,String dsapellido1
			,Date   feingreso
			,String cdnacion
			,String canaling
			,String conducto
			,String ptcumupr
			,String residencia
			,String nongrata
			,String cdideext
			,String cdestcivil
			,String cdsucemi
			,String nmtelefo
			,List<Map<String,String>> saveList
			,List<Map<String,String>> updateList
			,List<Map<String,String>> deleteList
			,boolean autosave
			,UserVO usuario
			,long   timestamp) throws Exception
	{
		Map<String,Object>result=new HashMap<String,Object>();
		logger.info(timestamp+""
				+ "\n#####################################"
				+ "\n###### guardarPantallaPersonas ######"
				+ "\n cdperson:"+cdperson
				+ "\n cdtipide:"+cdtipide
				+ "\n cdideper:"+cdideper
				+ "\n dsnombre:"+dsnombre
				+ "\n cdtipper:"+cdtipper
				+ "\n otfisjur:"+otfisjur
				+ "\n otsexo:"+otsexo
				+ "\n fenacimi:"+fenacimi
				+ "\n cdrfc:"+cdrfc
				+ "\n dsemail:"+dsemail
				+ "\n dsnombre1:"+dsnombre1
				+ "\n dsapellido:"+dsapellido
				+ "\n dsapellido1:"+dsapellido1
				+ "\n feingreso:"+feingreso
				+ "\n cdnacion:"+cdnacion
				+ "\n canaling:"+canaling
				+ "\n conducto:"+conducto
				+ "\n ptcumupr:"+ptcumupr
				+ "\n residencia:"+residencia
				+ "\n cdestcivil:"+cdestcivil
				+ "\n cdscuemi:"+cdsucemi
				+ "\n nmtelefo:"+nmtelefo
				+ "\n saveList:"+saveList
				+ "\n updateList:"+updateList
				+ "\n deleteList:"+deleteList
				+ "\n autosave:"+autosave
				);
		
		boolean exito           = true;
		String  respuesta       = null;
		String  respuestaOculta = null;
		String  cdpersonNuevo   = null;
		
		String usuarioCaptura =  null;
		
		if(usuario!=null){
			if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
				usuarioCaptura = usuario.getClaveUsuarioCaptura();
			}else{
				usuarioCaptura = usuario.getCodigoPersona();
			}
			
		}
		
		if(exito&&StringUtils.isBlank(cdperson))
		{
			
			try
			{
				cdperson      = personasDAO.obtenerNuevoCdperson();
				cdpersonNuevo = cdperson;
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener cdperson",ex);
				exito           = false;
				respuesta       = ex.getMessage()+" #"+timestamp;
				respuestaOculta = "sin respuesta oculta";
			}
		}
		
		if(exito && autosave)
		{
			try
			{
				personasDAO.actualizaFactoresArt140(
						cdperson, cdnacion, otfisjur,
						residencia, ptcumupr);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error en el movimiento de persona",ex);
				exito           = false;
				respuesta       = ex.getMessage()+" #"+timestamp;
				respuestaOculta = "sin respuesta oculta";
			}
		}
		
		if(exito && !autosave)
		{
			try
			{
				personasDAO.movimientosMpersona(
						cdperson, cdtipide, cdideper,
						dsnombre, cdtipper, otfisjur,
						otsexo, fenacimi, cdrfc,
						dsemail, dsnombre1, dsapellido,
						dsapellido1, feingreso, cdnacion,
						canaling, conducto, ptcumupr,
						residencia, nongrata, cdideext, cdestcivil, cdsucemi, usuarioCaptura, Constantes.INSERT_MODE);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error en el movimiento de persona",ex);
				exito           = false;
				respuesta       = ex.getMessage()+" #"+timestamp;
				respuestaOculta = "sin respuesta oculta";
			}
		}
		
		if(exito && !autosave)
		{
			try
			{
				for(Map<String,String> actualizar : updateList){
					personasDAO.movimientosMdomicil(
							cdperson, actualizar.get("NMORDDOM"), actualizar.get("DSDOMICI"),
							nmtelefo, actualizar.get("CODPOSTAL"), actualizar.get("CDEDO"),
							actualizar.get("CDMUNICI"), actualizar.get("CDCOLONI"), actualizar.get("NMNUMERO"),
							actualizar.get("NMNUMINT"),actualizar.get("CDTIPDOM"), usuarioCaptura, actualizar.get("SWACTIVO"), Constantes.UPDATE_MODE);
				}
				for(Map<String,String> eliminar : deleteList){
					personasDAO.movimientosMdomicil(
							cdperson, eliminar.get("NMORDDOM"), eliminar.get("DSDOMICI"),
							nmtelefo, eliminar.get("CODPOSTAL"), eliminar.get("CDEDO"),
							eliminar.get("CDMUNICI"), eliminar.get("CDCOLONI"), eliminar.get("NMNUMERO"),
							eliminar.get("NMNUMINT"),eliminar.get("CDTIPDOM"), usuarioCaptura, eliminar.get("SWACTIVO"), Constantes.DELETE_MODE);
				}
				for(Map<String,String> guardar : saveList){
					personasDAO.movimientosMdomicil(
							cdperson, null, guardar.get("DSDOMICI"),
							nmtelefo, guardar.get("CODPOSTAL"), guardar.get("CDEDO"),
							guardar.get("CDMUNICI"), guardar.get("CDCOLONI"), guardar.get("NMNUMERO"),
							guardar.get("NMNUMINT"), guardar.get("CDTIPDOM"), usuarioCaptura, guardar.get("SWACTIVO"), Constantes.INSERT_MODE);
				}
				
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error en el movimiento de domicilio",ex);
				exito           = false;
				respuesta       = ex.getMessage()+" #"+timestamp;
				respuestaOculta = "sin respuesta oculta";
			}
		}
		
		if(exito)
		{
			respuesta       = "Datos guardados correctamente";
			respuestaOculta = "Sin respuesta oculta";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("cdpersonNuevo"   , cdpersonNuevo);
		logger.info(timestamp+""
				+ "\nresult: "+result
				+ "\n###### guardarPantallaPersonas ######"
				+ "\n#####################################"
				);
		return result;
	}

	@Override
	public void guardarPantallaDomicilio(String cdperson, String nmorddom, String dsdomici, String nmtelefo,
			String cdpostal, String cdedo, String cdmunici, String cdcoloni, String nmnumero, String nmnumint, UserVO usuario, String swactivo,
			String accion, long timestamp) throws Exception {
		
		String usuarioCaptura =  null;
		
		if(usuario!=null){
			if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
				usuarioCaptura = usuario.getClaveUsuarioCaptura();
			}else{
				usuarioCaptura = usuario.getCodigoPersona();
			}
			
		}
		
		personasDAO.movimientosMdomicil(cdperson, nmorddom, dsdomici, nmtelefo, cdpostal, cdedo, cdmunici, cdcoloni,
				nmnumero, nmnumint, null,usuarioCaptura,swactivo,
				StringUtils.isNotEmpty(accion) ? accion : Constantes.INSERT_MODE);
	}

	/**
	 * Obtener el domicilio de una persona por su cdperson de PKG_CONSULTA.P_GET_MDOMICIL
	 * @return exito,respuesta,respuestaOculta,domicilio
	 */
	public Map<String,Object> obtenerDomicilioContratante(Map<String, String> params,long timestamp) throws Exception
	{
		Map<String,Object>result = new HashMap<String,Object>();
		logger.info(timestamp+""
				+ "\n#########################################"
				+ "\n###### obtenerDomicilioContratante ######"
				);
		boolean exito               = true;
		String  respuesta           = null;
		String  respuestaOculta     = null;
		Map<String,String>domicilio = null;
		
		if(exito)
		{
			try
			{
				domicilio = personasDAO.obtenerDomicilioContratante(params);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener domicilio del contratante ",ex);
				exito           = false;
				respuesta       = "No se encontr\u00f3 domicilio contratante #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("domicilio"       , domicilio);
		logger.info(timestamp+""
				+ "\nresult: "+result
				+ "\n###### obtenerDomicilioContratante ######"
				+ "\n#########################################"
				);
		return result;
	}
	
	/**
	 * Obtener el domicilio de una persona por su cdperson de PKG_CONSULTA.P_GET_MDOMICIL
	 * @return exito,respuesta,respuestaOculta,domicilio
	 */
	public Map<String,Object> obtenerDomicilioPorCdperson(String cdperson,String nmorddom,long timestamp) throws Exception
	{
		Map<String,Object>result = new HashMap<String,Object>();
		logger.info(timestamp+""
				+ "\n#########################################"
				+ "\n###### obtenerDomicilioPorCdperson ######"
				);
		boolean exito               = true;
		String  respuesta           = null;
		String  respuestaOculta     = null;
		Map<String,String>domicilio = null;
		
		if(exito)
		{
			try
			{
				domicilio = personasDAO.obtenerDomicilioPorCdperson(cdperson, nmorddom);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener domicilio por cdperson",ex);
				exito           = false;
				respuesta       = "No se encontr\u00f3 domicilio anterior #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("domicilio"       , domicilio);
		logger.info(timestamp+""
				+ "\nresult: "+result
				+ "\n###### obtenerDomicilioPorCdperson ######"
				+ "\n#########################################"
				);
		return result;
	}
	/**
	 * Obtener el domicilio de una persona por su cdperson de PKG_CONSULTA.P_GET_MDOMICIL
	 * @return exito,respuesta,respuestaOculta,domicilio
	 */
	public Map<String,Object> obtenerDomiciliosPorCdperson(String cdperson,long timestamp) throws Exception
	{
		Map<String,Object>result = new HashMap<String,Object>();
		logger.info(timestamp+""
				+ "\n#########################################"
				+ "\n###### obtenerDomiciliosPorCdperson ######"
				);
		boolean exito               = true;
		String  respuesta           = null;
		String  respuestaOculta     = null;
		List<Map<String,String>> domicilios = null;
		
		if(exito)
		{
			try
			{
				domicilios = personasDAO.obtenerDomiciliosPorCdperson(cdperson);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener domicilios por cdperson",ex);
				exito           = false;
				respuesta       = "No se encontr\u00f3 domiciliso anterior #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("domicilios"       , domicilios);
		logger.info(timestamp+""
//				+ "\nresult: "+result
				+ "\n###### obtenerDomiciliosPorCdperson ######"
				+ "\n#########################################"
				);
		return result;
	}
	
	/**
	 * Obtener los items de tatriper y los valores de tvaloper para un cdperson de PKG_LISTA.P_GET_ATRI_PER y PKG_CONSULTA.P_GET_TVALOPER
	 * @return exito,respuesta,respuestaOculta,itemsTatriper,fieldsTatriper,tvaloper
	 */
	public Map<String,Object> obtenerTatriperTvaloperPorCdperson(String cdperson, String cdrol,long timestamp) throws Exception
	{
		Map<String,Object>result=new HashMap<String,Object>();
		logger.info(timestamp+""
				+ "\n################################################"
				+ "\n###### obtenerTatriperTvaloperPorCdperson ######"
				+ "\n cdperson: "+cdperson
				);
		boolean            exito           = true;
		String             respuesta       = null;
		String             respuestaOculta = null;
		GeneradorCampos    gc              = null;
		Item               itemsTatriper   = null;
		Item               fieldsTatriper  = null;
		Map<String,String> tvaloper        = null;   
		
		if(exito)
		{
			try
			{
				List<ComponenteVO>atributos=personasDAO.obtenerAtributosPersona(cdperson, cdrol);
				gc=new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
				gc.setCdramo(Ramo.SALUD_VITAL.getCdramo());
				gc.setCdrol("1");
				gc.setCdtipsit(TipoSituacion.SALUD_VITAL.getCdtipsit());
				gc.generaComponentes(atributos, false, true, true, false, false, false);
				itemsTatriper=gc.getItems();
				fieldsTatriper=gc.getFields();
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener tatriper",ex);
				exito = false;
				respuesta = "Error al obtener datos adicionales #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			try
			{
				tvaloper=personasDAO.obtenerTvaloper(cdrol,cdperson);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error al obtener tvaloper",ex);
				exito = false;
				respuesta = "Error al obtener datos adicionales #"+timestamp;
				respuestaOculta = ex.getMessage();
			}
		}
		
		if(exito)
		{
			respuesta       = "Todo OK";
			respuestaOculta = "Todo OK";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		result.put("itemsTatriper"   , itemsTatriper);
		result.put("fieldsTatriper"  , fieldsTatriper);
		result.put("tvaloper"        , tvaloper);
		logger.info(timestamp+""
				+ "\nresult: "+result
				+ "\n###### obtenerTatriperTvaloperPorCdperson ######"
				+ "\n################################################"
				);
		return result;
	}
	
	/**
	 * Guardar los datos de tvaloper por cdperson con PKG_CONSULTA.P_MOV_TVALOPER
	 * @return exito,respuesta,respuestaOculta
	 */
	public Map<String,Object> guardarDatosTvaloper(String cdperson, String cdrol
			,String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05
			,String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10
			,String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15
			,String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20
			,String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25
			,String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30
			,String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35
			,String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40
			,String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45
			,String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50
			,String otvalor51, String otvalor52
			,long timestamp
			) throws Exception
	{
		Map<String,Object>result=new HashMap<String,Object>();
		logger.info(timestamp+""
				+ "\n#####################################"
				+ "\n###### guardarPantallaPersonas ######"
				+ "\n cdperson:"+cdperson
				+ "\n otvalor01:"+otvalor01
				+ "\n otvalor02:"+otvalor02
				+ "\n otvalor03:"+otvalor03
				+ "\n otvalor04:"+otvalor04
				+ "\n otvalor05:"+otvalor05
				+ "\n otvalor06:"+otvalor06
				+ "\n otvalor07:"+otvalor07
				+ "\n otvalor08:"+otvalor08
				+ "\n otvalor09:"+otvalor09
				+ "\n otvalor10:"+otvalor10
				+ "\n otvalor11:"+otvalor11
				+ "\n otvalor12:"+otvalor12
				+ "\n otvalor13:"+otvalor13
				+ "\n otvalor14:"+otvalor14
				+ "\n otvalor15:"+otvalor15
				+ "\n otvalor16:"+otvalor16
				+ "\n otvalor17:"+otvalor17
				+ "\n otvalor18:"+otvalor18
				+ "\n otvalor19:"+otvalor19
				+ "\n otvalor20:"+otvalor20
				+ "\n otvalor21:"+otvalor21
				+ "\n otvalor22:"+otvalor22
				+ "\n otvalor23:"+otvalor23
				+ "\n otvalor24:"+otvalor24
				+ "\n otvalor25:"+otvalor25
				+ "\n otvalor26:"+otvalor26
				+ "\n otvalor27:"+otvalor27
				+ "\n otvalor28:"+otvalor28
				+ "\n otvalor29:"+otvalor29
				+ "\n otvalor30:"+otvalor30
				+ "\n otvalor31:"+otvalor31
				+ "\n otvalor32:"+otvalor32
				+ "\n otvalor33:"+otvalor33
				+ "\n otvalor34:"+otvalor34
				+ "\n otvalor35:"+otvalor35
				+ "\n otvalor36:"+otvalor36
				+ "\n otvalor37:"+otvalor37
				+ "\n otvalor38:"+otvalor38
				+ "\n otvalor39:"+otvalor39
				+ "\n otvalor40:"+otvalor40
				+ "\n otvalor41:"+otvalor41
				+ "\n otvalor42:"+otvalor42
				+ "\n otvalor43:"+otvalor43
				+ "\n otvalor44:"+otvalor44
				+ "\n otvalor45:"+otvalor45
				+ "\n otvalor46:"+otvalor46
				+ "\n otvalor47:"+otvalor47
				+ "\n otvalor48:"+otvalor48
				+ "\n otvalor49:"+otvalor49
				+ "\n otvalor50:"+otvalor50
				+ "\n otvalor51:"+otvalor51
				+ "\n otvalor52:"+otvalor52
				);
		boolean exito           = true;
		String  respuesta       = null;
		String  respuestaOculta = null;
		
		if(exito)
		{
			try
			{
				Map<String,String>paramsValidarDocumentos=new HashMap<String,String>();
				paramsValidarDocumentos.put("cdperson",cdperson);
				paramsValidarDocumentos.put("cdrol",cdrol);
				personasDAO.validarDocumentosPersona(paramsValidarDocumentos);
				
				personasDAO.movimientosTvaloper("1",cdperson,
						otvalor01, otvalor02, otvalor03, otvalor04, otvalor05,
						otvalor06, otvalor07, otvalor08, otvalor09, otvalor10,
						otvalor11, otvalor12, otvalor13, otvalor14, otvalor15,
						otvalor16, otvalor17, otvalor18, otvalor19, otvalor20,
						otvalor21, otvalor22, otvalor23, otvalor24, otvalor25,
						otvalor26, otvalor27, otvalor28, otvalor29, otvalor30,
						otvalor31, otvalor32, otvalor33, otvalor34, otvalor35,
						otvalor36, otvalor37, otvalor38, otvalor39, otvalor40,
						otvalor41, otvalor42, otvalor43, otvalor44, otvalor45,
						otvalor46, otvalor47, otvalor48, otvalor49, otvalor50,
						otvalor51, otvalor52
						);
			}
			catch(Exception ex)
			{
				logger.error(timestamp+" error en el movimiento de tvaloper",ex);
				exito           = false;
				respuesta       = ex.getMessage()+" #"+timestamp;
				respuestaOculta = "sin respuesta oculta";
			}
		}
		
		if(exito)
		{
			respuesta       = "Datos guardados correctamente";
			respuestaOculta = "Sin respuesta oculta";
		}
		
		result.put("exito"           , exito);
		result.put("respuesta"       , respuesta);
		result.put("respuestaOculta" , respuestaOculta);
		logger.info(timestamp+""
				+ "\nresult: "+result
				+ "\n###### guardarPantallaPersonas ######"
				+ "\n#####################################"
				);
		return result;
	}
	
	@Override
	public List<Map<String,String>>cargarDocumentosPersona(String cdperson)throws Exception
	{
		logger.info(""
				+ "\n#####################################"
				+ "\n###### cargarDocumentosPersona ######"
				+ "\ncdperson: "+cdperson
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdperson",cdperson);
		List<Map<String,String>>lista=personasDAO.cargarDocumentosPersona(params);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		logger.info("lista size: "+lista.size());
		logger.info(""
				+ "\n###### cargarDocumentosPersona ######"
				+ "\n#####################################"
				);
		return lista;
	}
	
	@Override
	public String cargarNombreDocumentoPersona(String cdperson,String codidocu)throws Exception
	{
		logger.info(""
				+ "\n##########################################"
				+ "\n###### cargarNombreDocumentoPersona ######"
				+ "\ncdperson "+cdperson
				+ "\ncodidocu "+codidocu
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdperson",cdperson);
		params.put("codidocu",codidocu);
		String nombre=personasDAO.cargarNombreDocumentoPersona(params);
		logger.info("nombre: "+nombre);
		logger.info(""
				+ "\n###### cargarNombreDocumentoPersona ######"
				+ "\n##########################################"
				);
		return nombre;
	}

	@Override
	public String guardaAccionista(Map<String, String> params)throws Exception
	{
		return personasDAO.guardaAccionista(params);
	}

	@Override
	public String eliminaAccionistas(Map<String, String> params)throws Exception
	{
		return personasDAO.eliminaAccionistas(params);
	}

	@Override
	public String actualizaStatusPersona(Map<String, String> params)throws Exception
	{
		return personasDAO.actualizaStatusPersona(params);
	}
	
	@Override
	public List<Map<String,String>> obtieneAccionistas(Map<String, String> params)throws Exception{
		return personasDAO.obtieneAccionistas(params);
	}
	
	@Override
	public Map<String,Item> pantallaBeneficiarios(String cdunieco,String cdramo,String estado,String cdsisrol,String cdtipsup)throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaBeneficiarios @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdtipsup=" , cdtipsup
				));
		
		Map<String,Item> items = new HashMap<String,Item>();
		
		String paso = null;
		
		try
		{
			paso = "Validando suplemento permitido";
			logger.debug(Utils.log("","paso=",paso));
			
			try
			{
				Map<String,String>permiso=endososDAO.obtenerParametrosEndoso(
						ParametroEndoso.ENDOSO_PERMITIDO
						,cdramo
						,"x"
						,cdtipsup
						,null);
				if(!permiso.get("P1VALOR").equals("SI"))
				{
					throw new ApplicationException("No esta permitido");
				}
			}
			catch(Exception ex)
			{
				throw new ApplicationException("Suplemento no aplicable al producto");
			}
			
			paso = "Recuperando componentes relacion poliza-persona";
			logger.debug(Utils.log("","paso=",paso));
			
			List<ComponenteVO>componentesMpoliper=pantallasDAO.obtenerComponentes(
					null                     //cdtiptra
					,cdunieco
					,"|"+cdramo+"|"
					,null                    //cdtipsit
					,estado
					,cdsisrol
					,"PANTALLA_BENEFICIARIOS"//pantalla
					,"MPOLIPER"              //seccion
					,null                    //orden
					);
			
			paso = "Recuperando componentes persona";
			logger.debug(Utils.log("","paso=",paso));
			
			List<ComponenteVO>componentesMpersona=pantallasDAO.obtenerComponentes(
					null                     //cdtiptra
					,cdunieco
					,cdramo
					,null                    //cdtipsit
					,estado
					,cdsisrol
					,"PANTALLA_BENEFICIARIOS"//pantalla
					,"MPERSONA"              //seccion
					,null                    //orden
					);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			paso = "Construyendo componentes relacion poliza-persona";
			logger.debug(Utils.log("","paso=",paso));
			
			gc.generaComponentes(componentesMpoliper
					,true  //parcial
					,true  //conField
					,false //conItem
					,true  //conColumn
					,true  //conEditor
					,false //conButton
					);
			
			items.put("mpoliperFields"  , gc.getFields());
			items.put("mpoliperColumns" , gc.getColumns());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ items=", items
				,"\n@@@@@@ pantallaBeneficiarios @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return items;
	}
	
	@Override
	public Map<String,Item> pantallaPersona(String origen, String cdsisrol, String context) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaPersona @@@@@@"
				,"\n@@@@@@ origen="   , origen
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ context="  , context
				));
		
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		try
		{
			paso = "Recuperando componentes de pantalla de persona";
			List<ComponenteVO> componentes = pantallasDAO.obtenerComponentes(
					null                       //cdtiptra
					,null                      //cdunieco
					,null                      //cdramo
					,origen                    //cdtipsit
					,null                      //estado
					,cdsisrol
					,"PANTALLA_ESPEJO_PERSONA" //pantalla
					,"ITEMS"                   //seccion
					,null                      //orden
					);
			
			List<ComponenteVO> botones = pantallasDAO.obtenerComponentes(
					null                       //cdtiptra
					,null                      //cdunieco
					,null                      //cdramo
					,origen                    //cdtipsit
					,null                      //estado
					,cdsisrol
					,"PANTALLA_ESPEJO_PERSONA" //pantalla
					,"BUTTONS"                 //seccion
					,null                      //orden
					);
			
			logger2.debug("\nNumero de componentes recuperados: {}\nNumero de botones recuperados: {}",componentes.size(),botones.size());
			
			paso = "Generando componentes de pantalla de persona";
			GeneradorCampos gc = new GeneradorCampos(context);
			gc.generaComponentes(componentes, true, false, true, false, false, false);
			items.put("items" , gc.getItems());
			
			gc.generaComponentes(botones, true, false, false, false, false, true);
			items.put("buttons" , gc.getButtons());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ items=",items.size()
				,"\n@@@@@@ pantallaPersona @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return items;
	}
	
	@Override
	public String guardarPantallaEspPersona(Map<String,String>params, UserVO usuario) throws Exception
	{
		logger.debug(Utils.log(""
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarPantallaEspPersona @@@@@@"
				,"\n@@@@@@ params=",params
				));
		
		String paso = null;
		try
		{
			
			String usuarioCaptura =  null;
			
			if(usuario!=null){
				if(StringUtils.isNotBlank(usuario.getClaveUsuarioCaptura())){
					usuarioCaptura = usuario.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuario.getCodigoPersona();
				}
				
			}
			
			paso = "Guardando datos de persona";
			logger2.debug(Utils.log("","paso=",paso));
			
			personasDAO.movimientosMpersona(
				params.get("cdperson"),
				params.get("cdtipide"),
				params.get("cdideper"),
				params.get("dsnombre"),
				params.get("cdtipper"),
				params.get("otfisjur"),
				params.get("otsexo"),
				StringUtils.isNotBlank(params.get("fenacimi"))?renderFechas.parse(params.get("fenacimi")):null,
				params.get("cdrfc"),
				params.get("dsemail"),
				params.get("dsnombre1"),
				params.get("dsapellido"),
				params.get("dsapellido1"),
				StringUtils.isNotBlank(params.get("feingreso"))?renderFechas.parse(params.get("feingreso")):null,
				params.get("cdnacion"),
				params.get("canaling"),
				params.get("conducto"),
				params.get("ptcumupr"),
				params.get("residencia"),
				params.get("nongrata"),
				params.get("cdideext"),
				params.get("cdestciv"),
				params.get("cdsucemi"),
				usuarioCaptura,
				Constantes.UPDATE_MODE
			);
			
			String cdunieco  = params.get("cdunieco")
			       ,cdramo   = params.get("cdramo")
			       ,estado   = params.get("estado")
			       ,nmpoliza = params.get("nmpoliza")
			       ,nmsituac = params.get("nmsituac")
			       ,cdtipsit = params.get("cdtipsit");
			
			if(StringUtils.isNotBlank(cdunieco)
					&&StringUtils.isNotBlank(cdramo)
					&&StringUtils.isNotBlank(estado)
					&&StringUtils.isNotBlank(nmpoliza)
					&&StringUtils.isNotBlank(cdtipsit))
			{
				paso = "Sincronizando tvalosit";
				logger2.debug(Utils.log("","paso=",paso));
				
				personasDAO.sincronizaPersonaToValosit(
						params.get("otsexo")
						,StringUtils.isNotBlank(params.get("fenacimi"))?renderFechas.parse(params.get("fenacimi")):null
						,cdtipsit
						,cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,"0"
						);
			}
			
			paso = "Datos guardados correctamente";
			logger2.debug(Utils.log("","paso=",paso));
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(""
				,"\n@@@@@@ mensaje=",paso
				,"\n@@@@@@ guardarPantallaEspPersona @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return paso;
	}
	
	public Map<String,String>recuperarEspPersona(String cdperson) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarEspPersona @@@@@@"
				,"\n@@@@@@ cdperson=",cdperson
				));
		
		Map<String,String> persona = personasDAO.recuperarEspPersona(cdperson);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ persona=",persona
				,"\n@@@@@@ recuperarEspPersona @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return persona;
	}
	
	@Override
	public String guardarConfiguracionClientes(String rfc, String status, String tipoPersona, String cveAgente,
			String nombreCompleto, String domicilio, String observaciones,String proceso,  String cduser, Date fechaProcesamiento,
			String accion) throws Exception{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarConfiguracionClientes @@@@@@"
				,"\n@@@@@@ rfc =",rfc
				,"\n@@@@@@ status =",status
				,"\n@@@@@@ tipoPersona =",tipoPersona
				,"\n@@@@@@ cveAgente =",cveAgente
				,"\n@@@@@@ nombreCompleto =",nombreCompleto
				,"\n@@@@@@ domicilio=",domicilio
				,"\n@@@@@@ observaciones=",observaciones
				,"\n@@@@@@ proceso=",proceso
				,"\n@@@@@@ cduser =",cduser
				,"\n@@@@@@ fechaProcesamiento=", fechaProcesamiento
				,"\n@@@@@@ accion=",accion
				));
		
		String paso = null;
		try
		{
			paso = "Guardando clientes non gratos";
			logger2.debug("\nVoy a guardar datos de persona");
			Map<String, Object> paramsCliente= new HashMap<String, Object>();
			paramsCliente.put("pv_cdrfc_i",rfc);
			paramsCliente.put("pv_status_i",status);
			paramsCliente.put("pv_cdtipper_i",tipoPersona);
			paramsCliente.put("pv_cdagente_i",cveAgente);
			paramsCliente.put("pv_dsnombre_i",nombreCompleto);
			paramsCliente.put("pv_dsdomicil_i",domicilio);
			paramsCliente.put("pv_obsermot_i",observaciones);
			paramsCliente.put("pv_cdtipcli_i",proceso);
			paramsCliente.put("pv_cduser_i",cduser);
			paramsCliente.put("pv_fefecha_i",fechaProcesamiento);
			paramsCliente.put("pv_accion_i",accion);
			String res= personasDAO.guardarConfiguracionClientes(paramsCliente);
			paso = "Actualizamos la informaci\u00f3n del cliente por RFC";
			logger2.debug("\nVoy a guardar datos de persona");
			String activaCliente = null;
			if(accion.equalsIgnoreCase("I")){
				activaCliente = "S";
			}else{
				activaCliente = "N";
			}
			logger.debug(Utils.log("Valor de activaCliente ====> ",activaCliente));
			//1.- Clientes Non gratos
			if(proceso.equalsIgnoreCase("1")){ 
				personasDAO.actualizaClienteClientexTipo(rfc, activaCliente,proceso);
			}else if(proceso.equalsIgnoreCase("2")){ 
				//2.- Politicamente Expuesto
				personasDAO.actualizaClienteClientexTipo(rfc, activaCliente,proceso);
			}else{
				//3.- VIP
				personasDAO.actualizaClienteClientexTipo(rfc, activaCliente,proceso);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ mensaje=",paso
				,"\n@@@@@@ guardarConfiguracionClientes @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return paso;
	}
	
	@Override
	public List<Map<String, String>> obtieneListaClientesxTipo(String rfc, String proceso) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneListaClientesxTipo @@@@@@"
				,"\n@@@@@@ rfc=",rfc
				,"\n@@@@@@ proceso=",proceso
				));
		
		String paso = null;
		List<Map<String,String>> list = null;
		try
		{
			paso = "Obtenemos la lista de los clientes non gratos";
			logger2.debug("\nObtenemos la lista de los clientes non gratos");
			list = personasDAO.obtieneListaClientesxTipo(rfc, proceso);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ mensaje=",paso
				,"\n@@@@@@ obtieneListaClientesxTipo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return list;
	}
	
	@Override
	public List<GenericVO> consultaClientes(String cdperson) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ consultaClientes @@@@@@"
				,"\n@@@@@@ params=",cdperson
				));
		
		String paso = null;
		List<GenericVO> genericVO= null;
		try
		{
			paso = "Obtenemos la lista de los clientes non gratos";
			logger2.debug("\nObtenemos la lista de los clientes non gratos");
			genericVO = personasDAO.consultaClientes(cdperson);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ mensaje=",paso
				,"\n@@@@@@ consultaClientes @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return genericVO;
	}
	
	@Override
	public String obtieneInformacionCliente(String sucursal, String ramo, String poliza) throws Exception{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneInformacionCliente @@@@@@"
				,"\n@@@@@@ sucursal =",sucursal
				,"\n@@@@@@ ramo =",ramo
				,"\n@@@@@@ poliza =",poliza
				));
		
		String paso = null;
		try{
			paso = "Obtenemos la lista de los clientes non gratos";
			paso = clienteDAOSIGS.obtieneInformacionCliente(sucursal, ramo, poliza);
		}catch (Exception e){
			throw new ApplicationException("1", e);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ mensaje=",paso
				,"\n@@@@@@ obtieneInformacionCliente @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return paso;
	}
	
	@Override
	public String validaExisteAseguradoSicaps(String cdideper)throws Exception
	{
		return personasDAO.validaExisteAseguradoSicaps(cdideper);
	}
	
	@Override
	public Integer obtieneTipoCliWS(String codigoExterno, String compania) throws Exception{
		return autosSIGSDAO.obtieneTipoCliWS(codigoExterno, compania);
	}

	@Override
	public List<Map<String, String>> obtieneConfPatallaCli(String cdperson, String usuario, String rol, String tipoCliente) throws Exception{
		return personasDAO.obtieneConfPatallaCli(cdperson, usuario, rol, tipoCliente);
	}
	
	/*
	 * Getters y setters
	 */
	public void setPantallasDAO(PantallasDAO pantallasDAO) {
		this.pantallasDAO = pantallasDAO;
	}

	public void setPersonasDAO(PersonasDAO personasDAO) {
		this.personasDAO = personasDAO;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setEndososDAO(EndososDAO endososDAO) {
		this.endososDAO = endososDAO;
	}

    @Override
    public void guardarBeneficiarios(String cdunieco, String cdramo, String estado, String nmpoliza,
            String usuarioCaptura, List<Map<String, String>> beneficiarios) throws Exception {
        logger.debug(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
               ,"\n@@@@@@ guardarBeneficiarios@@@@@ @@@@@@"
               ,"\n@@@@@@ cdunieco      = " , cdunieco
               ,"\n@@@@@@ cdramo        = " , cdramo
               ,"\n@@@@@@ estado        = " , estado
               ,"\n@@@@@@ nmpoliza      = " , nmpoliza
               ,"\n@@@@@@ usuarioCaptura= " , usuarioCaptura
               ,"\n@@@@@@ beneficiarios = " , beneficiarios
               ));
       
        
        for(Map<String,String>rec:beneficiarios)
            {
                
                String cdperson =  personasDAO.obtieneCdperson();
                logger.debug("****cdperson: "+rec.get("CDPERSON"));
                String mov    = rec.get("mov");
                int agregar   = 1;
                int eliminar  = 2;
                int operacion = 0;
                if(StringUtils.isNotBlank(mov))
                {
                    if(mov.equals("+"))
                    {
                        operacion=agregar;
                    }
                    else if(mov.equals("-"))
                    {
                        operacion=eliminar;
                    }
                }
                
                if(operacion==agregar)
                {
                    personasDAO.movimientosMpersona(
                            rec.get("CDPERSON")
                            ,rec.get("CDTIPIDE")
                            ,rec.get("CDIDEPER")
                            ,rec.get("DSNOMBRE")
                            ,rec.get("CDTIPPER")
                            ,rec.get("OTFISJUR")
                            ,rec.get("OTSEXO")
                            ,StringUtils.isNotBlank(rec.get("FENACIMI"))?
                                    renderFechas.parse(rec.get("FENACIMI"))
                                    :null
                            ,rec.get("CDRFC")
                            ,rec.get("DSEMAIL")
                            ,rec.get("DSNOMBRE1")
                            ,rec.get("DSAPELLIDO")
                            ,rec.get("DSAPELLIDO1")
                            ,new Date()
                            ,rec.get("CDNACION")
                            ,rec.get("CANALING")
                            ,rec.get("CONDUCTO")
                            ,rec.get("PTCUMUPR")
                            ,rec.get("RESIDENCIA")
                            ,rec.get("NONGRATA")
                            ,rec.get("CDIDEEXT")
                            ,rec.get("CDESTCIV")
                            ,rec.get("CDSUCEMI")
                            ,usuarioCaptura
                            ,Constantes.INSERT_MODE);
                    
                    endososDAO.movimientoMpoliperBeneficiario(
                            cdunieco
                            ,cdramo
                            ,estado
                            ,nmpoliza
                            ,"0"
                            ,"3"
                            ,rec.get("CDPERSON")
                            ,"1"
                            ,"V"
                            ,rec.get("NMORDDOM")
                            ,rec.get("SWRECLAM")
                            ,"N" //swexiper
                            ,rec.get("CDPARENT")
                            ,rec.get("PORBENEF")
                            ,"I"
                            );
                }
                else if(operacion==eliminar)
                {
                    endososDAO.movimientoMpoliperBeneficiario(
                            cdunieco
                            ,cdramo
                            ,estado
                            ,nmpoliza
                            ,"0"
                            ,rec.get("CDROL")
                            ,rec.get("CDPERSON")
                            ,"1"
                            ,rec.get("STATUS")
                            ,rec.get("NMORDDOM")
                            ,rec.get("SWRECLAM")
                            ,rec.get("SWEXIPER")
                            ,rec.get("CDPARENT")
                            ,rec.get("PORBENEF")
                            ,"B"
                            );
                    
                    personasDAO.movimientosMpersona(
                            rec.get("CDPERSON")
                            ,rec.get("CDTIPIDE")
                            ,rec.get("CDIDEPER")
                            ,rec.get("DSNOMBRE")
                            ,rec.get("CDTIPPER")
                            ,rec.get("OTFISJUR")
                            ,rec.get("OTSEXO")
                            ,StringUtils.isNotBlank(rec.get("FENACIMI"))?
                                    renderFechas.parse(rec.get("FENACIMI"))
                                    :null
                            ,rec.get("CDRFC")
                            ,rec.get("DSEMAIL")
                            ,rec.get("DSNOMBRE1")
                            ,rec.get("DSAPELLIDO")
                            ,rec.get("DSAPELLIDO1")
                            ,new Date()
                            ,rec.get("CDNACION")
                            ,rec.get("CANALING")
                            ,rec.get("CONDUCTO")
                            ,rec.get("PTCUMUPR")
                            ,rec.get("RESIDENCIA")
                            ,rec.get("NONGRATA")
                            ,rec.get("CDIDEEXT")
                            ,rec.get("CDESTCIV")
                            ,rec.get("CDSUCEMI")
                            ,usuarioCaptura
                            ,"B");
                }
                else
                {
                    endososDAO.movimientoMpoliperBeneficiario(
                            cdunieco
                            ,cdramo
                            ,estado
                            ,nmpoliza
                            ,"0"
                            ,rec.get("CDROL")
                            ,rec.get("CDPERSON")
                            ,"1"
                            ,rec.get("STATUS")
                            ,rec.get("NMORDDOM")
                            ,rec.get("SWRECLAM")
                            ,rec.get("SWEXIPER")
                            ,rec.get("CDPARENT")
                            ,rec.get("PORBENEF")
                            ,"U"
                            );
                    
                    personasDAO.movimientosMpersona(
                            rec.get("CDPERSON")
                            ,rec.get("CDTIPIDE")
                            ,rec.get("CDIDEPER")
                            ,rec.get("DSNOMBRE")
                            ,rec.get("CDTIPPER")
                            ,rec.get("OTFISJUR")
                            ,rec.get("OTSEXO")
                            ,StringUtils.isNotBlank(rec.get("FENACIMI"))?
                                    renderFechas.parse(rec.get("FENACIMI"))
                                    :null
                            ,rec.get("CDRFC")
                            ,rec.get("DSEMAIL")
                            ,rec.get("DSNOMBRE1")
                            ,rec.get("DSAPELLIDO")
                            ,rec.get("DSAPELLIDO1")
                            ,new Date()
                            ,rec.get("CDNACION")
                            ,rec.get("CANALING")
                            ,rec.get("CONDUCTO")
                            ,rec.get("PTCUMUPR")
                            ,rec.get("RESIDENCIA")
                            ,rec.get("NONGRATA")
                            ,rec.get("CDIDEEXT")
                            ,rec.get("CDESTCIV")
                            ,rec.get("CDSUCEMI")
                            ,usuarioCaptura
                            ,Constantes.UPDATE_MODE);
                }
            }
        
        logger.debug(Utils.log(
                "\n@@@@@@ guardarBeneficiarios@@@@@ @@@@@@"
                ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ));
        
    }

    @Override
    public String obtieneAseguradoSICAPS(String nombres, String apellidoP, String apellidoM, Date fechaNac)throws Exception {
        return personasDAO.obtieneAseguradoSICAPS(nombres,apellidoP,apellidoM,fechaNac);
    }
		
}