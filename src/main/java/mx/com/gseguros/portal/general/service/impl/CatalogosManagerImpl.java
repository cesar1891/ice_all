package mx.com.gseguros.portal.general.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.general.dao.CatalogosDAO;
import mx.com.gseguros.portal.general.service.CatalogosManager;
import mx.com.gseguros.portal.general.util.Catalogos;
import mx.com.gseguros.portal.general.util.Rango;
import mx.com.gseguros.portal.general.util.TipoModelado;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.general.util.Validacion;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.wizard.dao.TablasApoyoDAO;
import mx.com.gseguros.wizard.dao.WizardDAO;

public class CatalogosManagerImpl implements CatalogosManager {
	
	private CatalogosDAO catalogosDAO;
	
	@Autowired
	private WizardDAO wizardDAO;

	@Autowired
	private TablasApoyoDAO tablasApoyoDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private FlujoMesaControlDAO flujoMesaControlDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogosManagerImpl.class);
	
	
	@Override
	public List<GenericVO> getTmanteni(Catalogos catalogo) throws Exception {
		return catalogosDAO.obtieneTmanteni(catalogo.getCdTabla());
	}
	
	
	@Override
	public List<GenericVO> obtieneColonias(String codigoPostal) throws Exception {
		return catalogosDAO.obtieneColonias(codigoPostal);
	}

	@Override
	public List<GenericVO> obtieneMunicipios(String cdEstado) throws Exception {
		return catalogosDAO.obtieneMunicipios(cdEstado);
	}

	@Override
	public List<GenericVO> obtieneZonasPorModalidad(String cdtipsit) throws Exception {
		return catalogosDAO.obtieneZonasPorModalidad(cdtipsit);
	}
	
	@Override
	public List<GenericVO> obtieneAtributosSituacion(String cdAtribu, String cdTipSit, String idPadre, String cdSisRol) throws Exception{
		String otValor = StringUtils.isNotBlank(idPadre) ? idPadre : null;
		return catalogosDAO.obtieneAtributosSituacion(cdAtribu, cdTipSit, otValor, cdSisRol);
	}
	
	@Override
	public List<GenericVO> obtieneAtributosSiniestro(String cdAtribu,
			String cdTipSit, String idPadre) throws Exception {
		
		String otValor = StringUtils.isNotBlank(idPadre) ? idPadre : null; 
		return catalogosDAO.obtieneAtributosSiniestro(cdAtribu, cdTipSit, otValor);
	}
	
	@Override
	public List<GenericVO> obtieneAtributosPoliza(String cdAtribu,
			String cdRamo, String idPadre) throws Exception {
		
		String otValor = StringUtils.isNotBlank(idPadre) ? idPadre : null;
		return catalogosDAO.obtieneAtributosPoliza(cdAtribu, cdRamo, otValor);
	}
	
	
	@Override
	public List<GenericVO> obtieneAtributosGarantia(String cdAtribu,
			String cdTipSit, String cdRamo, String idPadre, String cdGarant, String cdSisrol) // se agrega par�metro cdSisrol para considerar restricciones por rol (EGS)
			throws Exception {
		
		String valAnt = StringUtils.isNotBlank(idPadre) ? idPadre : null;
		return catalogosDAO.obtieneAtributosGarantia(cdAtribu, cdTipSit, cdRamo, valAnt, cdGarant, cdSisrol);
	}
	
	
	@Override
	public List<GenericVO> obtieneAtributosRol(String cdAtribu,
			String cdTipSit, String cdRamo, String valAnt, String cdRol)
			throws Exception {
		valAnt = StringUtils.isNotBlank(valAnt) ? valAnt : null;
		return catalogosDAO.obtieneAtributosRol(cdAtribu, cdTipSit, cdRamo, valAnt, cdRol);
	}
	
	
	@Override
	public List<GenericVO> obtieneRolesSistema(String dsRol) throws Exception {
		return catalogosDAO.obtieneRolesSistema(dsRol);
	}
	
	@Override
	public List<GenericVO> obtieneSucursales(String cdunieco,String cdusuari) throws Exception {
		return catalogosDAO.obtieneSucursales(cdunieco,cdusuari);
	}
	
	@Override
	public List<GenericVO> obtieneAgentes(String claveONombre) throws Exception {
		return catalogosDAO.obtieneAgentes(claveONombre);
	}
	
	public void setCatalogosDAO(CatalogosDAO catalogosDAO) {
		this.catalogosDAO = catalogosDAO;
	}
	
	@Override
	public List<GenericVO> obtieneStatusTramite(Map<String,String> params) throws Exception
	{
		return catalogosDAO.obtieneStatusTramite(params);
	}
	
	@Override
	public String obtieneCantidadMaxima(String cdramo, String cdtipsit, TipoTramite tipoTramite, Rango rango, Validacion validacion) throws Exception {
		return catalogosDAO.obtieneCantidadMaxima(cdramo, cdtipsit, tipoTramite, rango, validacion);
	}
	
	@Override
	public List<GenericVO> cargarAgentesPorPromotor(String cdusuari)throws Exception
	{
		logger.info(""
				+ "\n######################################"
				+ "\n###### cargarAgentesPorPromotor ######"
				+ "\ncdusuari "+cdusuari
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("cdusuari",cdusuari);
		List<GenericVO>lista=catalogosDAO.cargarAgentesPorPromotor(params);
		if(lista==null)
		{
			lista=new ArrayList<GenericVO>();
		}
		logger.info(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarAgentesPorPromotor ######"
				+ "\n######################################"
				);
		return lista;
	}
	
	@Override
	public List<GenericVO> cargarServicioPublicoAutos(String substring,String cdramo,String cdtipsit)throws Exception
	{
		logger.info(""
				+ "\n########################################"
				+ "\n###### cargarServicioPublicoAutos ######"
				+ "\nsubstring "+substring
				+ "\ncdramo "+cdramo
				+ "\ncdtipsit "+cdtipsit
				);
		Map<String,String>params=new HashMap<String,String>();
		params.put("substring" , substring);
		params.put("cdtipsit"  , cdtipsit);
		params.put("cdramo"    , cdramo);
		List<GenericVO>lista=catalogosDAO.cargarServicioPublicoAutos(params);
		if(lista==null)
		{
			lista=new ArrayList<GenericVO>();
		}
		logger.info(""
				+ "\nlista size "+lista.size()
				+ "\n###### cargarServicioPublicoAutos ######"
				+ "\n########################################"
				);
		return lista;
	}
	
	@Override
	public String agregaCodigoPostal(Map<String, String> params)throws Exception
	{
		return catalogosDAO.agregaCodigoPostal(params);
	}

	@Override
	public String asociaZonaCodigoPostal(Map<String, String> params)throws Exception
	{
		return catalogosDAO.asociaZonaCodigoPostal(params);
	}
	
	@Override
	public List<Map<String, String>> obtieneTablasApoyo(Map<String,String> params) throws Exception
	{
		return wizardDAO.obtieneTablasApoyo(params);
	}

	@Override
	public String guardaTablaApoyo(Map<String,String> params) throws Exception
	{
		return wizardDAO.guardaTablaApoyo(params);
	}
	
	@Override
	public boolean guardaClavesTablaApoyo(Map<String, String> params, List<Map<String, String>> saveList) throws Exception
	{
		
		boolean add1 = false;
		boolean add2 = false;
		boolean add3 = false;
		boolean add4 = false;
		boolean add5 = false;
		
		HashMap<String, Map<String, String>> existentes =  new HashMap<String, Map<String, String>>();
		List<Map<String, String>> inexistentes = new ArrayList<Map<String,String>>();
		inexistentes.addAll(saveList);
		
		//Identificando que llaves ya existen y quitandolas de la lista de inexistentes
		for(Map<String, String> clave : saveList){
			if(StringUtils.isNotBlank(clave.get("NUMCLAVE"))){
				if("1".equals(clave.get("NUMCLAVE"))){
					existentes.put("1", clave);
					inexistentes.remove(clave);
				}else if("2".equals(clave.get("NUMCLAVE"))){
					existentes.put("2", clave);
					inexistentes.remove(clave);
				}else if("3".equals(clave.get("NUMCLAVE"))){
					existentes.put("3", clave);
					inexistentes.remove(clave);
				}else if("4".equals(clave.get("NUMCLAVE"))){
					existentes.put("4", clave);
					inexistentes.remove(clave);
				}else if("5".equals(clave.get("NUMCLAVE"))){
					existentes.put("5", clave);
					inexistentes.remove(clave);
				}
			}
		}
		
		logger.debug("Claves existentes: "+ existentes);
		logger.debug("Claves nuevas/ a generar: "+ inexistentes);
		
		
		/** Si no hay nada que guardar **/
		if(existentes.isEmpty() && inexistentes.isEmpty()){
			logger.debug("Sin claves que guardar");
			return true;
		}
		
		/** Si no la tabla no se creo con ninguna clave y la tabla no tiene claves se inserta **/
		if(existentes.isEmpty()){
			params.put("pi_tip_tran", "1");
		}
		
		Map<String, String> claveAgregar = null;
		
		//Agregando Existentes
		if(existentes.containsKey("1")){
			claveAgregar = existentes.get("1");
			params.put("pi_dsclave1", claveAgregar.get("DSCLAVE1"));
			params.put("pi_swforma1", claveAgregar.get("SWFORMA1"));
			params.put("pi_nmlmin1", claveAgregar.get("NMLMIN1"));
			params.put("pi_nmlmax1", claveAgregar.get("NMLMAX1"));
		}else {
			add1 = true;
		}
		if(existentes.containsKey("2")){
			claveAgregar = existentes.get("2");
			params.put("pi_dsclave2", claveAgregar.get("DSCLAVE1"));
			params.put("pi_swforma2", claveAgregar.get("SWFORMA1"));
			params.put("pi_nmlmin2", claveAgregar.get("NMLMIN1"));
			params.put("pi_nmlmax2", claveAgregar.get("NMLMAX1"));
		}else {
			add2 = true;
		}
		if(existentes.containsKey("3")){
			claveAgregar = existentes.get("3");
			params.put("pi_dsclave3", claveAgregar.get("DSCLAVE1"));
			params.put("pi_swforma3", claveAgregar.get("SWFORMA1"));
			params.put("pi_nmlmin3", claveAgregar.get("NMLMIN1"));
			params.put("pi_nmlmax3", claveAgregar.get("NMLMAX1"));
		}else {
			add3 = true;
		}
		if(existentes.containsKey("4")){
			claveAgregar = existentes.get("4");
			params.put("pi_dsclave4", claveAgregar.get("DSCLAVE1"));
			params.put("pi_swforma4", claveAgregar.get("SWFORMA1"));
			params.put("pi_nmlmin4", claveAgregar.get("NMLMIN1"));
			params.put("pi_nmlmax4", claveAgregar.get("NMLMAX1"));
		}else {
			add4 = true;
		}
		if(existentes.containsKey("5")){
			claveAgregar = existentes.get("5");
			params.put("pi_dsclave5", claveAgregar.get("DSCLAVE1"));
			params.put("pi_swforma5", claveAgregar.get("SWFORMA1"));
			params.put("pi_nmlmin5", claveAgregar.get("NMLMIN1"));
			params.put("pi_nmlmax5", claveAgregar.get("NMLMAX1"));
		}else {
			add5 = true;
		}
		
		//Agregando las que aun no existen
		if(add1){
			if(!inexistentes.isEmpty()){
				claveAgregar = new HashMap<String, String>(inexistentes.get(0));
				params.put("pi_dsclave1", claveAgregar.get("DSCLAVE1"));
				params.put("pi_swforma1", claveAgregar.get("SWFORMA1"));
				params.put("pi_nmlmin1", claveAgregar.get("NMLMIN1"));
				params.put("pi_nmlmax1", claveAgregar.get("NMLMAX1"));
				inexistentes.remove(0);
			}else{
				params.put("pi_dsclave1", null);
				params.put("pi_swforma1", null);
				params.put("pi_nmlmin1", null);
				params.put("pi_nmlmax1", null);
			}
		}
		
		if(add2){
			if(!inexistentes.isEmpty()){
				claveAgregar = new HashMap<String, String>(inexistentes.get(0));
				params.put("pi_dsclave2", claveAgregar.get("DSCLAVE1"));
				params.put("pi_swforma2", claveAgregar.get("SWFORMA1"));
				params.put("pi_nmlmin2", claveAgregar.get("NMLMIN1"));
				params.put("pi_nmlmax2", claveAgregar.get("NMLMAX1"));
				inexistentes.remove(0);
			}else{
				params.put("pi_dsclave2", null);
				params.put("pi_swforma2", null);
				params.put("pi_nmlmin2", null);
				params.put("pi_nmlmax2", null);
			}
		}
		
		if(add3){
			if(!inexistentes.isEmpty()){
				claveAgregar = new HashMap<String, String>(inexistentes.get(0));
				params.put("pi_dsclave3", claveAgregar.get("DSCLAVE1"));
				params.put("pi_swforma3", claveAgregar.get("SWFORMA1"));
				params.put("pi_nmlmin3", claveAgregar.get("NMLMIN1"));
				params.put("pi_nmlmax3", claveAgregar.get("NMLMAX1"));
				inexistentes.remove(0);
			}else{
				params.put("pi_dsclave3", null);
				params.put("pi_swforma3", null);
				params.put("pi_nmlmin3", null);
				params.put("pi_nmlmax3", null);
			}
		}
		
		if(add4){
			if(!inexistentes.isEmpty()){
				claveAgregar = new HashMap<String, String>(inexistentes.get(0));
				params.put("pi_dsclave4", claveAgregar.get("DSCLAVE1"));
				params.put("pi_swforma4", claveAgregar.get("SWFORMA1"));
				params.put("pi_nmlmin4", claveAgregar.get("NMLMIN1"));
				params.put("pi_nmlmax4", claveAgregar.get("NMLMAX1"));
				inexistentes.remove(0);
			}else{
				params.put("pi_dsclave4", null);
				params.put("pi_swforma4", null);
				params.put("pi_nmlmin4", null);
				params.put("pi_nmlmax4", null);
			}
		}
		
		if(add5){
			if(!inexistentes.isEmpty()){
				claveAgregar = new HashMap<String, String>(inexistentes.get(0));
				params.put("pi_dsclave5", claveAgregar.get("DSCLAVE1"));
				params.put("pi_swforma5", claveAgregar.get("SWFORMA1"));
				params.put("pi_nmlmin5", claveAgregar.get("NMLMIN1"));
				params.put("pi_nmlmax5", claveAgregar.get("NMLMAX1"));
				inexistentes.remove(0);
			}else{
				params.put("pi_dsclave5", null);
				params.put("pi_swforma5", null);
				params.put("pi_nmlmin5", null);
				params.put("pi_nmlmax5", null);
			}
		}
		
		
		wizardDAO.guardaClavesTablaApoyo(params);
		
		return true;
	}

	@Override
	public boolean guardaAtributosTablaApoyo(Map<String, String> params, List<Map<String, String>> saveList) throws Exception{
		
		params.put("pi_tip_tran", "1");//Para que haga inserts
		
		for(Map<String, String> atributo : saveList){
				params.put("pi_cdatribu", atributo.get("CDATRIBU"));
				params.put("pi_dsatribu", atributo.get("DSATRIBU"));
				params.put("pi_swformat", atributo.get("SWFORMAT"));
				params.put("pi_nmlmin",   atributo.get("NMLMIN"));
				params.put("pi_nmlmax",   atributo.get("NMLMAX"));
				
				wizardDAO.guardaAtributosTablaApoyo(params);
		}
		
		return true; 
	}
	
	@Override
	public List<Map<String, String>> obtieneClavesTablaApoyo(Map<String,String> params) throws Exception
	{
		return wizardDAO.obtieneClavesTablaApoyo(params);
	}
	
	@Override
	public List<Map<String, String>> obtieneAtributosTablaApoyo(Map<String,String> params) throws Exception
	{
		return wizardDAO.obtieneAtributosTablaApoyo(params);
	}
	
	@Override
	public List<GenericVO> cargarDescuentosPorAgente(
    		String tipoUnidad
    		,String uso
    		,String zona
    		,String promotoria
    		,String cdagente
    		,String cdtipsit
    		,String cdatribu)throws Exception
    {
		logger.info(
				new StringBuilder()
				.append("\n#######################################")
				.append("\n###### cargarDescuentosPorAgente ######")
				.append("\n###### tipoUnidad").append(tipoUnidad)
				.append("\n###### uso").append(uso)
				.append("\n###### zona").append(zona)
				.append("\n###### promotoria").append(promotoria)
				.append("\n###### cdagente").append(cdagente)
				.append("\n###### cdtipsit").append(cdtipsit)
				.append("\n###### cdatribu").append(cdatribu)
				.toString()
				);
		return catalogosDAO.cargarDescuentosPorAgente(tipoUnidad,uso,zona,promotoria,cdagente,cdtipsit,cdatribu);
    }
	
	@Override
	public List<GenericVO>cargarListaNegocioServicioPublico(
			String cdtipsit
			,String cdatribu
			,String tipoUnidad
			,String cdagente)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarListaNegocioServicioPublico @@@@@@")
				.append("\n@@@@@@ cdtipsit=")  .append(cdtipsit)
				.append("\n@@@@@@ cdatribu=")  .append(cdatribu)
				.append("\n@@@@@@ tipoUnidad=").append(tipoUnidad)
				.append("\n@@@@@@ cdagente=")  .append(cdagente)
				.toString()
				);
		List<GenericVO>lista = null;
		try
		{
			lista = catalogosDAO.cargarListaNegocioServicioPublico(cdtipsit,cdatribu,tipoUnidad,cdagente);
		}
		catch(Exception ex)
		{
			logger.error("Error al cargar negocios para serv. publico",ex);
			lista = new ArrayList<GenericVO>();
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarListaNegocioServicioPublico @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarModelosPorSubmarcaRamo5(String submarca,String cdtipsit)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarModelosPorSubmarcaRamo5 @@@@@@")
				.append("\n@@@@@@ submarca=").append(submarca)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.toString()
				);
		List<GenericVO>lista=null;
		try
		{
			lista = catalogosDAO.cargarModelosPorSubmarcaRamo5(submarca,cdtipsit);
		}
		catch(Exception ex)
		{
			logger.error("Error al obtener modelos por submarca ramo 5",ex);
			lista = new ArrayList<GenericVO>();
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarModelosPorSubmarcaRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarVersionesPorModeloSubmarcaRamo5(String submarca,String modelo,String cdtipsit)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarVersionesPorModeloSubmarcaRamo5 @@@@@@")
				.append("\n@@@@@@ submarca=").append(submarca)
				.append("\n@@@@@@ modelo=")  .append(modelo)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.toString()
				);
		List<GenericVO>lista=null;
		try
		{
			lista = catalogosDAO.cargarVersionesPorModeloSubmarcaRamo5(submarca,modelo,cdtipsit);
		}
		catch(Exception ex)
		{
			logger.error("Error al obtener versiones por modelo y submarca ramo 5",ex);
			lista = new ArrayList<GenericVO>();
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarVersionesPorModeloSubmarcaRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarAutosPorCadenaRamo5(
			String cadena
			,String cdtipsit
			,String servicio
			,String uso
			)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarAutosPorCadenaRamo5 @@@@@@")
				.append("\n@@@@@@ cadena=")  .append(cadena)
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.append("\n@@@@@@ servicio=").append(servicio)
				.append("\n@@@@@@ uso=")     .append(uso)
				.toString()
				);
		List<GenericVO>lista=null;
		try
		{
			lista = catalogosDAO.cargarAutosPorCadenaRamo5(cadena,cdtipsit,servicio,uso);
		}
		catch(Exception ex)
		{
			logger.error("Error al obtener autos de ramo 5 por cadena",ex);
			lista = new ArrayList<GenericVO>();
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarAutosPorCadenaRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarTtapvat1(String cdtabla)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarTtapvat1 @@@@@@")
				.append("\n@@@@@@ cdtabla=").append(cdtabla)
				.toString()
				);
		
		List<GenericVO>lista=null;
		
		try
		{
			lista = catalogosDAO.cargarTtapvat1(cdtabla);
		}
		catch(Exception ex)
		{
			logger.error("Error al cargar ttapvat1",ex);
			lista = new ArrayList<GenericVO>();
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista.size()>15?lista.size():lista)
				.append("\n@@@@@@ cargarTtapvat1 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarNegocioPorCdtipsitRamo5(String cdtipsit)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarNegocioPorCdtipsitRamo5 @@@@@@")
				.append("\n@@@@@@ cdtipsit=").append(cdtipsit)
				.toString()
				);
		
		List<GenericVO>lista=null;
		
		try
		{
			lista = catalogosDAO.cargarNegocioPorCdtipsitRamo5(cdtipsit);
		}
		catch(Exception ex)
		{
			logger.error("Error al cargar negocios por cdtipsit",ex);
			lista = new ArrayList<GenericVO>();
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarNegocioPorCdtipsitRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarUsosPorNegocioRamo5(String cdnegocio,String cdtipsit,String servicio,String tipocot)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarUsosPorNegocioRamo5 @@@@@@")
				.append("\n@@@@@@ cdnegocio=").append(cdnegocio)
				.append("\n@@@@@@ cdtipsit=") .append(cdtipsit)
				.append("\n@@@@@@ servicio=") .append(servicio)
				.append("\n@@@@@@ tipocot=")  .append(tipocot)
				.toString()
				);
		
		List<GenericVO>lista=null;
		
		try
		{
			lista = catalogosDAO.cargarUsosPorNegocioRamo5(cdnegocio,cdtipsit,servicio,tipocot);
		}
		catch(Exception ex)
		{
			logger.error("Error al cargar usos por negocio",ex);
			lista = new ArrayList<GenericVO>();
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarUsosPorNegocioRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarMarcasPorNegocioRamo5(String cdnegocio,String cdtipsit)
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarMarcasPorNegocioRamo5 @@@@@@")
				.append("\n@@@@@@ cdnegocio=").append(cdnegocio)
				.append("\n@@@@@@ cdtipsit=") .append(cdtipsit)
				.toString()
				);
		
		List<GenericVO>lista=null;
		
		try
		{
			lista = catalogosDAO.cargarMarcasPorNegocioRamo5(cdnegocio,cdtipsit);
		}
		catch(Exception ex)
		{
			logger.error("Error al cargar marcas por negocio",ex);
			lista = new ArrayList<GenericVO>();
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarMarcasPorNegocioRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarNegociosPorAgenteRamo5(
			String cdagente
			,String cdsisrol
			,String tipoflot
			)throws Exception
	{
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ cargarNegociosPorAgenteRamo5 @@@@@@")
				.append("\n@@@@@@ cdagente=").append(cdagente)
				.append("\n@@@@@@ cdsisrol=").append(cdsisrol)
				.append("\n@@@@@@ tipoflot=").append(tipoflot)
				.toString()
				);
		
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(cdagente))
		{
			lista=catalogosDAO.cargarNegociosPorAgenteRamo5(cdagente,cdsisrol,tipoflot);
		}
		
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@ lista=").append(lista)
				.append("\n@@@@@@ cargarNegociosPorAgenteRamo5 @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarCargasPorNegocioRamo5(String cdsisrol,String negocio)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarCargasPorNegocioRamo5 @@@@@@"
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ negocio="  , negocio
				));
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(negocio))
		{
			lista=catalogosDAO.cargarCargasPorNegocioRamo5(cdsisrol, negocio);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ cargarCargasPorNegocioRamo5 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarPlanesPorNegocioModeloClavegsRamo5(
    		String cdtipsit
    		,String modelo
    		,String negocio
    		,String clavegs
    		,String servicio
    		,String tipoflot
			)throws Exception
    {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarPlanesPorNegocioModeloClavegsRamo5 @@@@@@"
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				,"\n@@@@@@ modelo="   , modelo
				,"\n@@@@@@ negocio="  , negocio
				,"\n@@@@@@ clavegs="  , clavegs
				,"\n@@@@@@ servicio=" , servicio
				,"\n@@@@@@ tipoflot=" , tipoflot
				));
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(modelo))
		{
			lista=catalogosDAO.cargarPlanesPorNegocioModeloClavegsRamo5(cdtipsit,modelo,negocio,clavegs,servicio,tipoflot);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ cargarPlanesPorNegocioModeloClavegsRamo5 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }
	
	@Override
	public List<GenericVO>cargarNegociosPorTipoSituacionAgenteRamo5(
			String cdtipsit
			,String cdagente
			,String producto
			,String cdsisrol
			) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarNegociosPorTipoSituacionAgenteRamo5 @@@@@@"
				,"\n@@@@@@ cdtipsit" , cdtipsit
				,"\n@@@@@@ cdagente" , cdagente
				,"\n@@@@@@ producto" , producto
				,"\n@@@@@@ cdsisrol" , cdsisrol
				));
		
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(cdagente))
		{
			lista=catalogosDAO.cargarNegociosPorTipoSituacionAgenteRamo5(cdtipsit,cdagente,producto,cdsisrol);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ cargarNegociosPorTipoSituacionAgenteRamo5 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarTiposSituacionPorNegocioRamo5(String negocio,String producto)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarTiposSituacionPorNegocioRamo5 @@@@@@"
				,"\n@@@@@@ negocio="  , negocio
				,"\n@@@@@@ producto=" , producto
				));
		
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(negocio))
		{
			lista=catalogosDAO.cargarTiposSituacionPorNegocioRamo5(negocio,producto);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ cargarTiposSituacionPorNegocioRamo5 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarCuadrosPorSituacion(String cdtipsit)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarCuadrosPorSituacion @@@@@@"
				,"\n@@@@@@ cdtipsit=",cdtipsit
				));
		
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(cdtipsit))
		{
			lista=catalogosDAO.cargarCuadrosPorSituacion(cdtipsit);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ cargarCuadrosPorSituacion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO>cargarSumaAseguradaRamo4(String cdsisrol,String cdplan)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarSumaAseguradaRamo4 @@@@@@"
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdplan="   , cdplan
				));
		
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(cdplan))
		{
			lista=catalogosDAO.recuperarSumaAseguradaRamo4(cdsisrol,cdplan);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ cargarSumaAseguradaRamo4 @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO>recuperarTiposServicioPorAuto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTiposServicioPorAuto @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				));
		
		List<GenericVO>lista=new ArrayList<GenericVO>();
		if(StringUtils.isNotBlank(cdunieco)
				&&StringUtils.isNotBlank(cdramo)
				&&StringUtils.isNotBlank(estado)
				&&StringUtils.isNotBlank(nmpoliza)
				&&StringUtils.isNotBlank(nmsituac)
				&&StringUtils.isNotBlank(nmsuplem)
				)
		{
			lista = catalogosDAO.recuperarTiposServicioPorAuto(cdunieco,cdramo,estado,nmpoliza,nmsituac,nmsuplem);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTiposServicioPorAuto @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
		
	}
	
	@Override
	public List<GenericVO> recuperarListaTiposValorRamo5PorRol(String cdtipsit,String cdsisrol,String cdusuari) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarListaTiposValorRamo5PorRol @@@@@@"
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdusuari=" , cdusuari
				));
		
		List<GenericVO> lista = catalogosDAO.recuperarListaTiposValorRamo5PorRol(cdtipsit, cdsisrol,cdusuari);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarListaTiposValorRamo5PorRol @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarModulosEstadisticas() throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarModulosEstadisticas @@@@@@"
				));
		
		List<GenericVO> lista = catalogosDAO.recuperarModulosEstadisticas();
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarModulosEstadisticas @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTareasEstadisticas(String cdmodulo) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTareasEstadisticas @@@@@@"
				,"\n@@@@@@ cdmodulo=",cdmodulo
				));
		
		List<GenericVO> lista = catalogosDAO.recuperarTareasEstadisticas(cdmodulo);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarTareasEstadisticas @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}

	@Override
	public List<GenericVO> obtieneListaParentesco() throws Exception
	{
		List<GenericVO> lista = new ArrayList<GenericVO>();
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneListaParentesco @@@@@@"
				));
		
		Map<String, String> params =  new HashMap<String, String>();
		
		params.put("PV_NMTABLA_I", "3395");
		params.put("PV_OTCLAVE1_I", null);
		params.put("PV_LIMITE_I",   null);
		Map<String,Object> result = tablasApoyoDAO.obtieneValoresTablaApoyo1clave(params);
		
		List<Map<String,String>> listaResult = (List<Map<String, String>>) result.get("PV_REGISTRO_O");
		
		GenericVO generic= null;
		if(listaResult!= null){
			for(Map<String,String> parent : listaResult){
				generic = new GenericVO();
				generic.setKey(parent.get("OTCLAVE1"));
				generic.setValue(parent.get("OTVALOR01"));
				lista.add(generic);
			}
			
		}
		
		logger.debug(Utils.log(
				"\n@@@@@@ obtieneListaParentesco @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> obtieneAgenteEspecifico(String cdagente) throws Exception {
		return catalogosDAO.obtieneAgenteEspecifico(cdagente);
	}
	
	@Override
	public List<GenericVO> recuperarListaPools() throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarListaPools @@@@@@"
				));
		
		List<GenericVO> lista = catalogosDAO.recuperarListaPools();

		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista.size()
				,"\n@@@@@@ recuperarListaPools @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarGruposPoliza(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarGruposPoliza @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				
				));
		
		String paso           = null;
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			paso = "Recuperando grupos";
			List<Map<String,String>> listaMapas = consultasDAO.recuperarGruposPoliza(cdunieco,cdramo,estado,nmpoliza);
			for(Map<String,String>grupo:listaMapas)
			{
				lista.add(new GenericVO(grupo.get("CDGRUPO"), grupo.get("DSGRUPO")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarGruposPoliza @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarSubramos(String cdramo) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarSubramos @@@@@@"
				,"\n@@@@@@ cdramo=" , cdramo
				
				));
		
		String paso           = null;
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			paso = "Recuperando grupos";
			List<Map<String,String>> listaMapas = consultasDAO.recuperarSubramos(cdramo);
			for(Map<String,String>grupo:listaMapas)
			{
				lista.add(new GenericVO(grupo.get("CDSUBRAM"), grupo.get("DESCRIPCION")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarSubramos @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Deprecated
	@Override
	public List<GenericVO> recuperarTiposRamo() throws Exception
	{

		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTiposRamo @@@@@@"
				));
		
		String paso           = null;
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			paso = "Recuperando tipos de ramo";
			List<Map<String,String>> listaMapas = consultasDAO.recuperarTiposRamo();
			for(Map<String,String>tipoRamo:listaMapas)
			{
				lista.add(new GenericVO(tipoRamo.get("CDTIPRAM"), tipoRamo.get("DSTIPRAM")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTiposRamo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Deprecated
	@Override
    public List<GenericVO> recuperarRamosPorTipoRamo(String cdtipram) throws Exception
    {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarRamosPorTipoRamo @@@@@@"
				,"\n@@@@@@ cdtipram=" , cdtipram
				
				));
		
		String paso           = null;
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			paso = "Recuperando ramos por tipo de ramo";
			List<Map<String,String>> listaMapas = consultasDAO.recuperarRamosPorTipoRamo(cdtipram);
			for(Map<String,String>ramo:listaMapas)
			{
				lista.add(new GenericVO(ramo.get("CDRAMO"), ramo.get("DSRAMO")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarRamosPorTipoRamo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }
	
	@Deprecated
	@Override
    public List<GenericVO> recuperarSucursalesPorTipoRamo(String cdtipram) throws Exception
    {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarSucursalesPorTipoRamo @@@@@@"
				,"\n@@@@@@ cdtipram=" , cdtipram
				
				));
		
		String paso           = null;
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			paso = "Recuperando sucursales por tipo de ramo";
			List<Map<String,String>> listaMapas = consultasDAO.recuperarSucursalesPorTipoRamo(cdtipram);
			for(Map<String,String>ramo:listaMapas)
			{
				lista.add(new GenericVO(ramo.get("CDUNIECO"), ramo.get("DSUNIECO")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}

		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarSucursalesPorTipoRamo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }

	@Deprecated
	@Override
    public List<GenericVO> recuperarComboUsuarios(String cadena) throws Exception
    {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarComboUsuarios @@@@@@"
				,"\n@@@@@@ cadena=",cadena
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		String          paso  = "Recuperando usuarios";
		try
		{
			List<Map<String,String>> listaUsuarios = consultasDAO.recuperarComboUsuarios(cadena);
			for(Map<String,String> usuario : listaUsuarios)
			{
				lista.add(new GenericVO(usuario.get("cdusuari"),usuario.get("nombre"),usuario.get("cdunieco")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarComboUsuarios @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }
	
	@Deprecated
	@Override
    public List<GenericVO> recuperarSucursalesPermisoImpresion(
    		String cdtipram
    		,String cdusuari
    		,String cdunieco
    		) throws Exception
    {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarSucursalesPermisoImpresion @@@@@@"
				,"\n@@@@@@ cdtipram=" , cdtipram
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdunieco=" , cdunieco
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		String          paso  = "Recuperando usuarios";
		try
		{
			List<Map<String,String>> listaSucursales = consultasDAO.recuperarSucursalesPermisoImpresion(
					cdtipram
		    		,cdusuari
		    		,cdunieco
		    		);
			for(Map<String,String> sucursal : listaSucursales)
			{
				lista.add(new GenericVO(sucursal.get("cdunieco"),sucursal.get("dsunieco")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarSucursalesPermisoImpresion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }
	
	@Override
	public List<GenericVO> obtieneAtributosExcel(Catalogos catalogo) throws Exception {
		return catalogosDAO.obtieneAtributosExcel(catalogo.getCdTabla());
	}
	
	@Override
	@Deprecated
    public List<GenericVO> recuperarTtiptramc() throws Exception
    {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTtiptramc @@@@@@"
				));
		
		String          paso  = null;
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			List<Map<String,String>> mapas = flujoMesaControlDAO.recuperaTtiptramc();
			for(Map<String,String> mapa:mapas)
			{
				lista.add(new GenericVO(mapa.get("CDTIPTRA"),mapa.get("DSTIPTRA")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ recuperarTtiptramc @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
    }
	
	@Override
	@Deprecated
	public List<GenericVO> recuperarTestadomcPorAgrupamc(String agrupamc, String extras) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTestadomcPorAgrupamc @@@@@@"
				,"\n@@@@@@ agrupamc=" , agrupamc
				,"\n@@@@@@ extras="   , extras
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		if("S".equals(extras))
		{
			lista.add(new GenericVO("-1" , "-TAREAS PENDIENTES-"));
			lista.add(new GenericVO("0"  , "-TODOS-"));
		}
		List<Map<String,String>> statusFlujo = flujoMesaControlDAO.recuperarTestadomcPorAgrupamc(agrupamc);
		for(Map<String,String>statusIte:statusFlujo)
		{
			lista.add(new GenericVO(statusIte.get("CDESTADOMC"),statusIte.get("DSESTADOMC")));
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTestadomcPorAgrupamc @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTtipflumc(String agrupamc) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTtipflumc @@@@@@"
				,"\n@@@@@@ agrupamc=",agrupamc
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String,String>> tiposFlujo = flujoMesaControlDAO.recuperaTtipflumc(agrupamc, String.valueOf(TipoModelado.FLUJOS_PROCESOS.getCdtipmod()));
		for(Map<String,String>tipoFlujo:tiposFlujo)
		{
			lista.add(new GenericVO(
					tipoFlujo.get("CDTIPFLU")
					,tipoFlujo.get("DSTIPFLU")
					,tipoFlujo.get("CDTIPTRA")
					,tipoFlujo.get("CDTIPSUP")
					,tipoFlujo.get("SWREQPOL")
					));
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTtipflumc @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTflujomc(String cdtipflu, String swfinal) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTflujomc @@@@@@"
				,"\n@@@@@@ cdtipflu=" , cdtipflu
				,"\n@@@@@@ swfinal="  , swfinal
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String,String>> flujos = flujoMesaControlDAO.recuperaTflujomc(cdtipflu, swfinal);
		for(Map<String,String>flujo:flujos)
		{
			lista.add(new GenericVO(flujo.get("CDFLUJOMC"),flujo.get("DSFLUJOMC")));
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTflujomc @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTtipsupl(String cdtiptra,String ninguno) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTtipsupl @@@@@@"
				,"\n@@@@@@ cdtiptra=" , cdtiptra
				,"\n@@@@@@ ninguno="  , ninguno
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		if("S".equals(ninguno))
		{
			lista.add(new GenericVO("0","NINGUNO"));
		}
		List<Map<String,String>> flujos = flujoMesaControlDAO.recuperarTtipsupl(cdtiptra);
		for(Map<String,String>flujo:flujos)
		{
			lista.add(new GenericVO(flujo.get("CDTIPSUP"),flujo.get("DSTIPSUP")));
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTtipsupl @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> obtieneCatalogoParentescoAutos()	throws Exception {
		return catalogosDAO.obtieneCatalogoParentescoAutos();
	}
	
	@Override
	public List<GenericVO> recuperarTdocume(String cdtiptra) throws Exception
	{
		long stamp = System.currentTimeMillis();
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTdocume @@@@@@"
				,"\n@@@@@@ stamp="    , stamp
				,"\n@@@@@@ cdtiptra=" , cdtiptra
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		String paso = null;
		
		try
		{
			paso = "Recuperando documentos";
			logger.debug(Utils.log("\n@@@@@@ ",stamp," ",paso));
			List<Map<String,String>> documentos = flujoMesaControlDAO.recuperaTdocume();
			
			paso = "filtrando documentos";
			logger.debug(Utils.log("\n@@@@@@ ",stamp," ",paso));
			for(Map<String,String> documento : documentos)
			{
				if(cdtiptra.equals(documento.get("CDTIPTRA")))
				{
					lista.add(new GenericVO(documento.get("CDDOCUME"),documento.get("DSDOCUME")));
				}
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ stamp=" , stamp
				,"\n@@@@@@ lista=" , lista
				,"\n@@@@@@ recuperarTdocume @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> cargarCotizadoresActivos(String cadena) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ cargarCotizadoresACTIVOS @@@@@@"
				,"\n@@@@@@ cadena: @@@@@@",cadena
				 ));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		String paso = null;
		
		try
		{
			paso = "Recuperando documentos";
			logger.debug(Utils.log("\n@@@@@@ ",paso));
			List<Map<String,String>> documentos = consultasDAO.cargarCotizadoresActivos(cadena);
			
			for(Map<String,String>elemento:documentos)
			{
				lista.add(new GenericVO(elemento.get("cdusuari"),elemento.get("dsusuari")));
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cargarCotizadoresACTIVOS @@@@@@"
				,"\n@@@@@@ cadena: @@@@@@",cadena
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}

	@Override
	public List<GenericVO> obtieneMotivosReexp(String cdramo, String cdtipsit) throws Exception
	{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ obtieneMotivosReexp @@@@@@"
				,"\n@@@@@@ cdramo: @@@@@@",cdramo
				,"\n@@@@@@ cdtipsit: @@@@@@",cdtipsit
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		String paso = null;
		
		try
		{
			paso = "Obtieniendo lista de motivos de reexpedicion";
			logger.debug(Utils.log("\n@@@@@@ ",paso));
			List<Map<String,String>> motivos = consultasDAO.obtieneMotivosReexp(cdramo,cdtipsit);
			
			for(Map<String,String>elemento:motivos)
			{
				lista.add(new GenericVO(elemento.get("codigo"),elemento.get("motivo")));
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				"\n@@@@@@ obtieneMotivosReexp @@@@@@"
				,"\n@@@@@@ cdramo: @@@@@@"  ,cdramo
				,"\n@@@@@@ cdtipsit: @@@@@@",cdtipsit
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarFormasDePagoPorRamoTipsit(String cdramo, String cdtipsit) throws Exception
	{

		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarFormasDePagoPorRamoTipsit @@@@@@"
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ cdtipsit=" , cdtipsit
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		String paso = null;
		
		try
		{
			paso = "Obtieniendo lista de formas de pago por ramo y tipsit";
			logger.debug(paso);
			
			List<Map<String,String>> formasPago = consultasDAO.recuperarFormasDePagoPorRamoTipsit(cdramo,cdtipsit);
			
			for(Map<String,String>elemento:formasPago)
			{
				lista.add(new GenericVO(elemento.get("CODIGO"),elemento.get("DESCRIPC")));
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=", lista
				,"\n@@@@@@ recuperarFormasDePagoPorRamoTipsit @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarClientesPorNombreApellido(String cadena) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarClientesPorNombreApellido @@@@@@"
				,"\n@@@@@@ cadena=", cadena
				));
		
		String paso = "Recuperando clientes";
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		try
		{
			List<Map<String,String>> clientesMapas = consultasDAO.recuperarClientesPorNombreApellido(cadena);
			
			for(Map<String,String> clienteMapa : clientesMapas)
			{
				lista.add(new GenericVO(clienteMapa.get("CDPERSON"),clienteMapa.get("NOMBRE")));
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=", lista
				,"\n@@@@@@ recuperarClientesPorNombreApellido @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		return lista;
	}
	
	@Override
	public List<GenericVO> obtieneCatalogoConvenios() throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ obtieneCatalogoConvenios @@@@@@")
				.toString()
				);
		return catalogosDAO.recuperarListaConvenios();
	}
	
	@Override
	public List<GenericVO> recuperaContratantesSalud(String nombre) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantesSalud @@@@@@")
				.toString()
				);
		return catalogosDAO.recuperarContratantesSalud(nombre);
	}
	
	@Override
	public List<GenericVO> recuperarTipoRamoColectivo(String tipoRamo) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperarTipoRamoColectivo @@@@@@")
				.toString()
				);
		return catalogosDAO.recuperaRamosColectivoTipoRamo(tipoRamo);
	}
	
	@Override
	public List<GenericVO> recuperarTipoRamoSituacionColectivo(String tipoRamo, String cdramo) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperarTipoRamoSituacionColectivo @@@@@@")
				.toString()
				);
		return catalogosDAO.recuperarTiposRamoSituacionColectivo(tipoRamo, cdramo);
	}
	
	@Override
	public List<GenericVO> recuperarListaFiltroPropiedadesInciso(String cdunieco, String cdramo,String estado,String nmpoliza) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperarListaFiltroPropiedadesInciso @@@@@@")
				.toString()
				);
		return catalogosDAO.recuperarListaFiltroPropiedadesInciso(cdunieco, cdramo,estado,nmpoliza);
	}
	
	@Override
	public List<GenericVO> recuperarTtipflumcPorRolPorUsuario(String agrupamc,String cdsisrol,String cdusuari) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTtipflumcPorRolPorUsuario @@@@@@"
				,"\n@@@@@@ agrupamc=" , agrupamc
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdusuari=" , cdusuari
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String,String>> tiposFlujo = flujoMesaControlDAO.recuperaTtipflumcPorRolPorUsuario(agrupamc,cdsisrol,cdusuari);
		for(Map<String,String>tipoFlujo:tiposFlujo)
		{
			lista.add(new GenericVO(
					tipoFlujo.get("CDTIPFLU")
					,tipoFlujo.get("DSTIPFLU")
					,tipoFlujo.get("CDTIPTRA")
					,tipoFlujo.get("CDTIPSUP")
					,tipoFlujo.get("SWREQPOL")
					));
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTtipflumcPorRolPorUsuario @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTflujomcPorRolPorUsuario(
			String cdtipflu
			,String swfinal
			,String cdsisrol
			,String cdusuari
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTflujomcPorRolPorUsuario @@@@@@"
				,"\n@@@@@@ cdtipflu=" , cdtipflu
				,"\n@@@@@@ swfinal="  , swfinal
				));
		
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String,String>> flujos = flujoMesaControlDAO.recuperaTflujomcPorRolPorUsuario(
				cdtipflu
				,swfinal
				,cdsisrol
				,cdusuari
				);
		for(Map<String,String>flujo:flujos)
		{
			lista.add(
					new GenericVO(
							flujo.get("CDFLUJOMC")
							,flujo.get("DSFLUJOMC")
							,flujo.get("CDTIPRAM")
							,flujo.get("SWGRUPO")
					)
			);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=",lista
				,"\n@@@@@@ recuperarTflujomcPorRolPorUsuario @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarSucursalesPorFlujo(String cdflujomc) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarSucursalesPorFlujo @@@@@@"
				,"\n@@@@@@ cdflujomc=" , cdflujomc
				));

		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		String paso = null;
		
		try
		{
			if(StringUtils.isBlank(cdflujomc))
			{
				paso = "Recuperando todas las sucursales";
				
				lista = obtieneSucursales(null, null);
			}
			else
			{
				paso = "Recuperando flujo";
				
				Map<String,String> flujo = flujoMesaControlDAO.recuperaTflujomc(cdflujomc);
				
				String cdtipram = flujo.get("CDTIPRAM");
				
				lista = recuperarSucursalesPorTipoRamo(cdtipram);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista=" , lista
				,"\n@@@@@@ recuperarSucursalesPorFlujo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarRamosPorSucursalPorTipogrupo(String cdunieco, String tipogrupo) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarRamosPorSucursalPorTipogrupo @@@@@@"
				,"\n@@@@@@ cdunieco  = " , cdunieco
				,"\n@@@@@@ tipogrupo = " , tipogrupo
				));
		
		String paso = "Recuperando ramos por sucursal por tipo grupo";
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			lista = catalogosDAO.recuperarRamosPorSucursalPorTipogrupo(cdunieco,tipogrupo);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista = " , lista
				,"\n@@@@@@ recuperarRamosPorSucursalPorTipogrupo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTipsitPorRamoPorTipogrupo(String cdramo, String tipogrupo) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTipsitPorRamoPorTipogrupo @@@@@@"
				,"\n@@@@@@ cdramo    = " , cdramo
				,"\n@@@@@@ tipogrupo = " , tipogrupo
				));
		
		String paso = "Recuperando tipos de situaci\u00f3n por ramo por tipo grupo";
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			lista = catalogosDAO.recuperarTipsitPorRamoPorTipogrupo(cdramo,tipogrupo);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista = " , lista
				,"\n@@@@@@ recuperarTipsitPorRamoPorTipogrupo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarTiposDeEndosoPorCdramoPorCdtipsit(String cdramo, String cdtipsit, String vigente) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarTiposDeEndosoPorCdramoPorCdtipsit @@@@@@"
				,"\n@@@@@@ cdramo   = " , cdramo
				,"\n@@@@@@ cdtipsit = " , cdtipsit
				,"\n@@@@@@ vigente  = " , vigente
				));
		
		String paso = "Recuperando tipos de endoso por ramo por tipo de situaci\00f3n";
		List<GenericVO> lista = new ArrayList<GenericVO>();
		try
		{
			lista = catalogosDAO.recuperarTiposDeEndosoPorCdramoPorCdtipsit(cdramo,cdtipsit,vigente);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista = " , lista
				,"\n@@@@@@ recuperarTiposDeEndosoPorCdramoPorCdtipsit @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperarMotivosRechazo (String ntramite) throws Exception {
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarMotivosRechazo @@@@@@"
				,"\n@@@@@@ ntramite = ", ntramite
				));
		
		String paso = "Recuperando motivos de rechazo";
		List<GenericVO> lista = new ArrayList<GenericVO>();
		
		try
		{
			lista = catalogosDAO.recuperarMotivosRechazo(ntramite);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ lista = " , lista
				,"\n@@@@@@ recuperarMotivosRechazo @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperaContratantes(String cdunieco, String cdramo, String cadena) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantes @@@@@@")
				.toString()
				);
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String, String >> result = consultasDAO.obtenerContratantes(cdunieco, cdramo, cadena);
		if(null != result && !result.isEmpty()){
			for(Map<String, String> map : result){
				GenericVO genericVO = new GenericVO();
				genericVO.setKey(map.get("cdperson"));
				genericVO.setValue(map.get("nombre_completo"));
				lista.add(genericVO);
			}
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantes @@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperaContratantesRfc(String cadena) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantesRfc @@@@@@")
				.toString()
				);
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String, String >> result = consultasDAO.obtenerContratantesRfc(cadena);
		if(null != result && !result.isEmpty()){
			for(Map<String, String> map : result){
				GenericVO genericVO = new GenericVO();
				genericVO.setKey(map.get("cdperson"));
				genericVO.setValue(map.get("nombre_completo"));
				lista.add(genericVO);
			}
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantesRfc @@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperaTablaApoyo1(String cdtabla) throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantesRfc @@@@@@")
				.toString()
				);
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String, String >> result = catalogosDAO.obtenerTablaApoyo1(cdtabla);
		if(null != result && !result.isEmpty()){
			for(Map<String, String> map : result){
				GenericVO genericVO = new GenericVO();
				genericVO.setKey(map.get("otclave"));
				genericVO.setValue(map.get("otvalor"));
				lista.add(genericVO);
			}
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaContratantesRfc @@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO> recuperaCamposExclusionRenovacion() throws Exception {
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaCamposExclusionRenovacion @@@@@@")
				.toString()
				);
		List<GenericVO> lista = new ArrayList<GenericVO>();
		List<Map<String, String >> result = catalogosDAO.obtenerCamposExclusionRenovacion();
		if(null != result && !result.isEmpty()){
			for(Map<String, String> map : result){
				GenericVO genericVO = new GenericVO();
				genericVO.setKey(map.get("otvalor01"));
				genericVO.setValue(map.get("otclave1"));
				lista.add(genericVO);
			}
		}
		logger.info(
				new StringBuilder()
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ recuperaCamposExclusionRenovacion @@@@@@")
				.toString()
				);
		return lista;
	}
	
	@Override
	public List<GenericVO> obtieneCatalogoDescAtrib(String cdRamo, String dsAtribu, String idPadre) throws Exception {		
		String otValor = StringUtils.isNotBlank(idPadre) ? idPadre : null;
		return catalogosDAO.obtieneCatalogoDescAtrib(cdRamo, dsAtribu, otValor);
	}
	
	@Override
    public List<GenericVO> obtieneCatalogoRetAdminAgente(String pv_numsuc_i, String pv_cdagente_i) throws Exception {      
       
        return catalogosDAO.obtieneCatalogoRetAdminAgente(pv_numsuc_i, pv_cdagente_i);
    }
	
	@Override
    public boolean guardaDescripcionCortaCobertura(String cdgarant, String descCorta) throws Exception{
        return catalogosDAO.guardaDescripcionCortaCobertura(cdgarant, descCorta);
    }

	@Override
	public List<GenericVO> obtieneIdsCierres() throws Exception{
	    return catalogosDAO.obtieneIdsCierres();
	}
	
	@Override
    public List<GenericVO> obtieneClaveDescuentoSubRamo(String pv_numsuc_i, 
                                                        String pv_cveent_i,
                                                        String pv_cdramo_i,
                                                        String pv_cdtipsit_i) throws Exception  {      
       
        return catalogosDAO.obtieneClaveDescuentoSubRamo(pv_numsuc_i, pv_cveent_i, pv_cdramo_i, pv_cdtipsit_i);
    }
	
	@Override
    public List<GenericVO> obtieneAdministradoraXAgente(String pv_cdagente_i) throws Exception  {      
       
        return catalogosDAO.obtieneAdminXAgente(pv_cdagente_i);
    }
	
   @Override
    public List<GenericVO> recuperarListaFiltroPropiedadInciso(String cdramo,String cdtipsit, String nivel) throws Exception {
        logger.info(
                new StringBuilder()
                .append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                .append("\n@@@@@@ recuperarListaFiltroPropiedadInciso @@@@@@")
                .toString()
                );
        return catalogosDAO.recuperarListaFiltroPropiedadInciso(cdramo, cdtipsit, nivel);
    }
   
   @Override
   public List<GenericVO> obtieneComentariosNegocio(
           String cdramo
           ,String cdtipsit
           ,String negocio
           )throws Exception{
       return consultasDAO.obtieneComentariosNegocio(cdramo, cdtipsit, negocio);
               
   }
   
   @Override
   public List<GenericVO> recuperarTiposEndosoPorTramite (String ntramite) throws Exception {
       logger.debug("{}", Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
                                    "\n@@@@@@ recuperarTiposEndosoPorTramite @@@@@@",
                                    "\n@@@@@@ ntramite = ", ntramite));
       String paso = null;
       List<GenericVO> lista = new ArrayList<GenericVO>();
       try {
           paso = "Recuperando tipos de endoso";
           logger.debug(paso);
           List<Map<String, String>> listaMapas = catalogosDAO.recuperarTiposEndosoPorTramite(ntramite);
           if (listaMapas != null && listaMapas.size() > 0) {
               for (Map<String, String> mapa : listaMapas) {
                   lista.add(new GenericVO(mapa.get("CDTIPSUP"), mapa.get("DSTIPSUP")));
               }
           }
       } catch (Exception ex) {
           Utils.generaExcepcion(ex, paso);
       }
       logger.debug("{}", Utils.log("\n@@@@@@ lista = ", lista,
                                    "\n@@@@@@ recuperarTiposEndosoPorTramite @@@@@@",
                                    "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
       return lista;
   }
	
}