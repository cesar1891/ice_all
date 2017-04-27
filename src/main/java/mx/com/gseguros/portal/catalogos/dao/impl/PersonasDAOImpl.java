package mx.com.gseguros.portal.catalogos.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.aon.portal.dao.ObtieneTatriperMapper;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

public class PersonasDAOImpl extends AbstractManagerDAO implements PersonasDAO
{
	
	private static final Logger logger = Logger.getLogger(PersonasDAOImpl.class);
	
	/**
	 * Obtiene personas por RFC de PKG_CONSULTA.P_GET_MPERSONA
	 * @param rfc
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,String>>obtenerPersonasPorRFC(String rfc, String nombre, String nombre2, String apat, String amat,
            String validaTienePoliza) throws Exception {
	    Map<String, String> params = new LinkedHashMap<String, String>();
	    params.put("pv_cdrfc_i"    , rfc);
        params.put("pv_dsnombre_i" , nombre);
        params.put("pv_dsnombre1_i" , nombre2);
        params.put("pv_dsapellido_i"   , apat);
        params.put("pv_dsapellido1_i"   , amat);
        params.put("pv_validapol_i"   , validaTienePoliza);
		Map<String, Object> resultado = ejecutaSP(new ObtenerPersonasPorRFC(getDataSource()), params);
		List<Map<String,String>>listaPersonas=(List<Map<String,String>>)resultado.get("pv_registro_o");
		if(listaPersonas==null)
		{
			listaPersonas=new ArrayList<Map<String,String>>();
		}
		logger.debug("listaPersonas size: "+listaPersonas.size());
		return listaPersonas;
	}
	
	protected class ObtenerPersonasPorRFC extends StoredProcedure
	{
    	protected ObtenerPersonasPorRFC(DataSource dataSource) {
            super(dataSource,"PKG_CONSULTA.P_GET_MPERSONA");
            declareParameter(new SqlParameter("pv_cdrfc_i",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsnombre_i",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_validapol_i",OracleTypes.VARCHAR));
//            declareParameter(new SqlParameter("pv_dsnombre1_i",OracleTypes.VARCHAR));
//            declareParameter(new SqlParameter("pv_dsapellido_i",OracleTypes.VARCHAR));
//            declareParameter(new SqlParameter("pv_dsapellido1_i",OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"CDPERSON"
            		,"CDIDEPER"
            		,"DSL_CDIDEPER"
            		,"DSNOMBRE"
            		,"DSNOMBRE1"
            		,"DSAPELLIDO"
            		,"DSAPELLIDO1"
            		,"FENACIMI"
            		,"CDNACION"
            		,"OTFISJUR"
            		,"OTSEXO"
            		,"CDRFC"
            		,"CANALING"
            		,"CONDUCTO"
            		,"FEINGRESO"
            		,"PTCUMUPR"
            		,"STATUS"
            		,"RESIDENTE"
            		,"NOMBRE_COMPLETO"
            		,"CDIDEEXT"
            		,"DSL_CDIDEEXT"
            		,"DIRECCIONCLI"
            		,"CDESTCIV"
            		,"CDSUCEMI"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Deprecated
	@Override
	public Map<String,String>obtenerPersonaPorCdperson(Map<String,String>params) throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_MPERSONA_X_CDPERSON ******")
				.append("\n****** params=").append(params)
				.append("\n****************************************************")
				.toString()
				);
		Map<String, Object> resultado         = ejecutaSP(new CargarPersonaPorCdperson(getDataSource()), params);
		List<Map<String,String>>listaPersonas = (List<Map<String,String>>)resultado.get("pv_registro_o");
		if(listaPersonas==null||listaPersonas.size()==0)
		{
			throw new Exception("No se encuentra la persona");
		}
		logger.debug("listaPersonas size: "+listaPersonas.size());
		return listaPersonas.get(0);
	}
	
	@Override
	public Map<String,String>cargarPersonaPorCdperson(String cdperson)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdperson_i" , cdperson);
		logger.debug(Utils.log(
				 "\n****************************************************"
				,"\n****** PKG_CONSULTA.P_GET_MPERSONA_X_CDPERSON ******"
				,"\n****** params=",params
				,"\n****************************************************"
				));
		Map<String,Object>procResult     = ejecutaSP(new CargarPersonaPorCdperson(getDataSource()), params);
		List<Map<String,String>>registro = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(registro==null||registro.size()==0)
		{
			throw new ApplicationException("La persona no existe");
		}
		if(registro.size()>1)
		{
			throw new ApplicationException("Persona duplicada");
		}
		logger.debug(Utils.log(
				 "\n****************************************************"
				,"\n****** params="   , params
				,"\n****** registro=" , registro.get(0)
				,"\n****** PKG_CONSULTA.P_GET_MPERSONA_X_CDPERSON ******"
				,"\n****************************************************"
				));
		return registro.get(0);
	}
	
	protected class CargarPersonaPorCdperson extends StoredProcedure
	{
    	protected CargarPersonaPorCdperson(DataSource dataSource) {
            super(dataSource,"PKG_CONSULTA.P_GET_MPERSONA_X_CDPERSON");
            declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDPERSON"
            		,"CDIDEPER"
            		,"DSL_CDIDEPER"
            		,"DSNOMBRE"
            		,"DSNOMBRE1"
            		,"DSAPELLIDO"
            		,"DSAPELLIDO1"
            		,"FENACIMI"
            		,"CDNACION"
            		,"OTFISJUR"
            		,"OTSEXO"
            		,"CDRFC"
            		,"CANALING"
            		,"CONDUCTO"
            		,"FEINGRESO"
            		,"PTCUMUPR"
            		,"STATUS"
            		,"RESIDENTE"
            		,"CDIDEEXT"
            		,"DSL_CDIDEEXT"
            		,"CDESTCIV"
            		,"CDSUCEMI"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	/**
	 * Guarda mpersona con PKG_SATELITES2.P_MOV_MPERSONA
	 */
	@Override
	public void movimientosMpersona(String cdperson
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
			,String usuarioExternoInterno
			,String accion) throws Exception
	{
		Map<String,Object>params=new LinkedHashMap<String,Object>();
		params.put("cdperson"    , cdperson);
		params.put("cdtipide"    , cdtipide);
		params.put("cdideper"    , cdideper);
		params.put("dsnombre"    , dsnombre);
		params.put("cdtipper"    , cdtipper);
		params.put("otfisjur"    , otfisjur);
		params.put("otsexo"      , otsexo);
		params.put("fenacimi"    , fenacimi);
		params.put("cdrfc"       , cdrfc);
		params.put("dsemail"     , dsemail);
		params.put("dsnombre1"   , dsnombre1);
		params.put("dsapellido"  , dsapellido);
		params.put("dsapellido1" , dsapellido1);
		params.put("feingreso"   , feingreso);
		params.put("cdnacion"    , cdnacion);
		params.put("canaling"    , canaling);
		params.put("conducto"    , conducto);
		params.put("ptcumupr"    , ptcumupr);
		params.put("residencia"  , residencia);
		params.put("nongrata"    , nongrata);
		params.put("cdideext"    , cdideext);
		params.put("cdestcivil"  , cdestcivil);
		params.put("pv_cdsucemi_i", cdsucemi);
		params.put("cdusuarioExtInt",    usuarioExternoInterno);
		params.put("accion"      , accion);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES2.P_MOV_MPERSONA ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new MovimientosMpersona(getDataSource()), params);
	}
	
	protected class MovimientosMpersona extends StoredProcedure
	{
    	protected MovimientosMpersona(DataSource dataSource) {
            super(dataSource,"PKG_SATELITES2.P_MOV_MPERSONA");
    		declareParameter(new SqlParameter("cdperson"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipide"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdideper"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsnombre"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipper"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otfisjur"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otsexo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fenacimi"    , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdrfc"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsemail"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsnombre1"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsapellido"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsapellido1" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feingreso"   , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdnacion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("canaling"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("conducto"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ptcumupr"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("residencia"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nongrata"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdideext"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestcivil"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsucemi_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuarioExtInt"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"      , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }

	
	@Override
	public void actualizaFactoresArt140(String cdperson, String cdnacion, String otfisjur,String residencia, String ptcumupr) throws Exception
	{
		Map<String,Object>params=new LinkedHashMap<String,Object>();
		params.put("pv_cdperson_i"    , cdperson);
		params.put("pv_cdnacion_i"    , cdnacion);
		params.put("pv_otfisjur_i"    , otfisjur);
		params.put("pv_residencia_i"  , residencia);
		params.put("pv_ptcumupr_i"    , ptcumupr);
		
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES2.P_ACT_DAT_OBLIG_PARAM_ART140 ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new ActualizaFactoresArt140(getDataSource()), params);
			}
	
	protected class ActualizaFactoresArt140 extends StoredProcedure
	{
		protected ActualizaFactoresArt140(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_ACT_DAT_OBLIG_PARAM_ART140");
			declareParameter(new SqlParameter("pv_cdperson_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdnacion_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otfisjur_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_residencia_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ptcumupr_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * obtener domicilio por cdperson desde PKG_CONSULTA.P_GET_MDOMICIL
	 */
	@Override
	public Map<String,String> obtenerDomicilioPorCdperson(String cdperson, String nmorddom) throws Exception
	{
		Map<String,String>domicilio = null;
		Map<String,String>params    = new LinkedHashMap<String,String>();
		params.put("cdperson",cdperson);
		params.put("nmorddom",nmorddom);
		logger.debug(
				new StringBuilder()
				.append("\n*****************************************")
				.append("\n****** PKG_CONSULTA.P_GET_MDOMICIL ******")
				.append("\n****** params=").append(params)
				.append("\n*****************************************")
				.toString()
				);
		Map<String, Object> resultado = ejecutaSP(new ObtenerDomicilioPorCdperson(getDataSource()), params);
		List<Map<String,String>>listaDomicilios=(List<Map<String,String>>)resultado.get("pv_registro_o");
		if(listaDomicilios==null)
		{
			listaDomicilios=new ArrayList<Map<String,String>>();
		}
		if(listaDomicilios.size()>0)
		{
			domicilio=listaDomicilios.get(0);
		}
		logger.debug("domicilio: "+domicilio);
		return domicilio;
	}
	

	@Override
	public List<Map<String,String>> obtenerDomiciliosPorCdperson(String cdperson) throws Exception
	{
		Map<String,String>params    = new LinkedHashMap<String,String>();
		params.put("cdperson",cdperson);
		params.put("nmorddom",null);
		logger.debug(
				new StringBuilder()
				.append("\n*****************************************")
				.append("\n****** PKG_CONSULTA.P_GET_MDOMICIL ******")
				.append("\n****** params=").append(params)
				.append("\n*****************************************")
				.toString()
				);
		Map<String, Object> resultado = ejecutaSP(new ObtenerDomicilioPorCdperson(getDataSource()), params);
		List<Map<String,String>>listaDomicilios=(List<Map<String,String>>)resultado.get("pv_registro_o");
		if(listaDomicilios==null)
		{
			listaDomicilios=new ArrayList<Map<String,String>>();
		}
//		logger.debug("listaDomicilios: "+listaDomicilios);
		return listaDomicilios;
	}
	
	protected class ObtenerDomicilioPorCdperson extends StoredProcedure
	{
    	protected ObtenerDomicilioPorCdperson(DataSource dataSource) {
            super(dataSource,"PKG_CONSULTA.P_GET_MDOMICIL");
    		declareParameter(new SqlParameter("cdperson"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmorddom"    , OracleTypes.VARCHAR));
    		String[] cols=new String[]{
    				"CDPERSON" , "NMORDDOM" , "DSDOMICI"  , "NMTELEFO" , "CODPOSTAL" , "CDEDO"
    				,"ESTADO"  , "CDMUNICI" , "MUNICIPIO" , "CDCOLONI" , "COLONIA","NMNUMERO"  , "NMNUMINT", "CDTIPDOM","DSTIPDOM","SWACTIVO", "CDUSRCRE"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	
	@Override
	public Map<String,String> obtenerDomicilioContratante(Map<String, String> params) throws Exception
	{
		Map<String,String>domicilio = null;
		logger.debug(
				new StringBuilder()
				.append("\n*****************************************")
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_CONTRATANTE ******")
				.append("\n****** params=").append(params)
				.append("\n*****************************************")
				.toString()
				);
		Map<String, Object> resultado = ejecutaSP(new ObtenerDomicilioContratante(getDataSource()), params);
		List<Map<String,String>>listaDomicilios=(List<Map<String,String>>)resultado.get("pv_registro_o");
		if(listaDomicilios==null)
		{
			listaDomicilios=new ArrayList<Map<String,String>>();
		}
		if(listaDomicilios.size()>0)
		{
			domicilio=listaDomicilios.get(0);
		}
		logger.debug("domicilio: "+domicilio);
		
		return domicilio;
	}
	
	protected class ObtenerDomicilioContratante extends StoredProcedure
	{
    	protected ObtenerDomicilioContratante(DataSource dataSource) {
            super(dataSource,"PKG_CONSULTA.P_GET_DATOS_CONTRATANTE");
    		declareParameter(new SqlParameter("pv_cdunieco_i"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_estado_i"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i"    , OracleTypes.VARCHAR));
    		String[] cols=new String[]{
    				"NMORDDOM" , "DSDOMICI"  , "NMTELEFO" , "CODPOSTAL" , "CDEDO"
    				, "CDMUNICI", "CDCOLONI" ,"NMNUMERO"  , "NMNUMINT", "CDTIPDOM","SWACTIVO", "CDUSRCRE"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	/**
	 * Obtener nuevo cdperson de PKG_SATELITES.P_GEN_CDPERSON
	 */
	@Override
	public String obtenerNuevoCdperson() throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES.P_GEN_CDPERSON ******")
				.append("\n****** sin parametros")
				.append("\n******************************************")
				.toString()
				);
		Map<String, Object> resultado = ejecutaSP(new ObtenerNuevoCdperson(getDataSource()), params);
		String cdperson = (String)resultado.get("pv_cdperson_o");
		if(StringUtils.isBlank(cdperson))
		{
			throw new Exception("Error al generar clave de persona");
		}
		return cdperson;
	}
	protected class ObtenerNuevoCdperson extends StoredProcedure
	{
    	protected ObtenerNuevoCdperson(DataSource dataSource) {
            super(dataSource,"PKG_SATELITES.P_GEN_CDPERSON");
            declareParameter(new SqlOutParameter("pv_cdperson_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }

	
	@Override
	public String validaExisteRFC(String cdrfc) throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdrfc_i", cdrfc);
		Map<String, Object> resultado = ejecutaSP(new ValidaExisteRFC(getDataSource()), params);
		String res = (String)resultado.get("pv_title_o");
		return res;
	}
	protected class ValidaExisteRFC extends StoredProcedure
	{
		protected ValidaExisteRFC(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_VALIDA_EXISTE_RFC");
			declareParameter(new SqlOutParameter("pv_cdrfc_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/**
	 * movimientos de domicilio por cdperson de PKG_SATELITES2.P_MOV_MDOMICIL
	 */
	@Override
	public void movimientosMdomicil(String cdperson
			,String nmorddom
			,String dsdomici
			,String nmtelefo
			,String cdpostal
			,String cdedo
			,String cdmunici
			,String cdcoloni
			,String nmnumero
			,String nmnumint
			,String cdtipdom
			,String usuarioExternoInterno
			,String swactivo
			,String accion) throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdperson" , cdperson);
		params.put("nmorddom" , nmorddom);
		params.put("dsdomici" , dsdomici);
		params.put("nmtelefo" , nmtelefo);
		params.put("cdpostal" , cdpostal);
		params.put("cdedo"    , cdedo);
		params.put("cdmunici" , cdmunici);
		params.put("cdcoloni" , cdcoloni);
		params.put("nmnumero" , nmnumero);
		params.put("nmnumint" , nmnumint);
		params.put("cdtipdom" , cdtipdom);
		params.put("cdusuarioExtInt" , usuarioExternoInterno);
		params.put("swactivo" , swactivo);
		params.put("accion"   , accion);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES2.P_MOV_MDOMICIL ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new MovimientosMdomicil(getDataSource()), params);
	}
	
	protected class MovimientosMdomicil extends StoredProcedure
	{
    	protected MovimientosMdomicil(DataSource dataSource) {
            super(dataSource,"PKG_SATELITES2.P_MOV_MDOMICIL");
    		declareParameter(new SqlParameter("cdperson"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmorddom"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsdomici"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmtelefo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdpostal"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdedo"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmunici"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdcoloni"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmnumero"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmnumint"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipdom"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuarioExtInt", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swactivo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"         , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	/**
	 * Obtener los componentes de tatriper por cdrol y cdperson de PKG_LISTAS.P_GET_ATRI_PER
	 */
	@Override
	public List<ComponenteVO> obtenerAtributosPersona(String cdperson, String cdrol) throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdperson" , cdperson);
		params.put("cdrol" , cdrol);
		logger.debug(
				new StringBuilder()
				.append("\n**************************************")
				.append("\n****** PKG_LISTAS.P_GET_ATRIPER ******")
				.append("\n****** params=").append(params)
				.append("\n**************************************")
				.toString()
				);
		Map<String,Object>resultado = ejecutaSP(new ObtenerAtributosPersona(getDataSource()), params);
		List<ComponenteVO>atributos=(List<ComponenteVO>) resultado.get("pv_registro_o");
		if(atributos==null)
		{
			atributos=new ArrayList<ComponenteVO>();
		}
		logger.info("obtenerAtributosPersona lista size: "+atributos.size());
		return atributos;
	}
	
	protected class ObtenerAtributosPersona extends StoredProcedure
	{
    	protected ObtenerAtributosPersona(DataSource dataSource) {
            super(dataSource,"PKG_LISTAS.P_GET_ATRIPER");
			declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatriperMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	/**
	 * Obtiene los valores de tvaloper para un cdperson y un cdrol de PKG_CONSULTA.P_GET_TVALOPER
	 */
	@Override
	public Map<String,String> obtenerTvaloper(String cdrol,String cdperson) throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdrol"    , cdrol);
		params.put("cdperson" , cdperson);
		logger.debug(
				new StringBuilder()
				.append("\n*****************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOPER ******")
				.append("\n****** params=").append(params)
				.append("\n*****************************************")
				.toString()
				);
		Map<String,Object>respuesta=ejecutaSP(new ObtenerTvaloper(getDataSource()), params);
		List<Map<String,String>>listaTvaloper=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		Map<String,String>tvaloper=new LinkedHashMap<String,String>();
		if(listaTvaloper!=null&&listaTvaloper.size()>0)
		{
			tvaloper=listaTvaloper.get(0);
		}
		logger.info("obtenerTvaloper map: "+tvaloper);
		return tvaloper;
	}
	
	protected class ObtenerTvaloper extends StoredProcedure
	{
    	protected ObtenerTvaloper(DataSource dataSource) {
            super(dataSource,"PKG_CONSULTA.P_GET_TVALOPER");
			declareParameter(new SqlParameter("cdrol"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDROL"      , "CDPERSON"
					,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05"
					,"OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
					,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15"
					,"OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
					,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25"
					,"OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
					,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35"
					,"OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
					,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45"
					,"OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
					,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55","OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
					,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65","OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR60"
					,"OTVALOR61" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75","OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR60"
					,"OTVALOR61" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85","OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR60"
					,"OTVALOR61" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95","OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	/**
	 * Movimientos de tvaloper por cdperson de PKG_SATELITES.P_MOV_TVALOPER_NUEVO
	 */
	@Override
	public void movimientosTvaloper(String cdrol, String cdperson
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
			,String otvalor51,String otvalor52
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdrol"        , cdrol);
		params.put("cdperson"     , cdperson);
		params.put("pv_otvalor01" , otvalor01);
		params.put("pv_otvalor02" , otvalor02);
		params.put("pv_otvalor03" , otvalor03);
		params.put("pv_otvalor04" , otvalor04);
		params.put("pv_otvalor05" , otvalor05);
		params.put("pv_otvalor06" , otvalor06);
		params.put("pv_otvalor07" , otvalor07);
		params.put("pv_otvalor08" , otvalor08);
		params.put("pv_otvalor09" , otvalor09);
		params.put("pv_otvalor10" , otvalor10);
		params.put("pv_otvalor11" , otvalor11);
		params.put("pv_otvalor12" , otvalor12);
		params.put("pv_otvalor13" , otvalor13);
		params.put("pv_otvalor14" , otvalor14);
		params.put("pv_otvalor15" , otvalor15);
		params.put("pv_otvalor16" , otvalor16);
		params.put("pv_otvalor17" , otvalor17);
		params.put("pv_otvalor18" , otvalor18);
		params.put("pv_otvalor19" , otvalor19);
		params.put("pv_otvalor20" , otvalor20);
		params.put("pv_otvalor21" , otvalor21);
		params.put("pv_otvalor22" , otvalor22);
		params.put("pv_otvalor23" , otvalor23);
		params.put("pv_otvalor24" , otvalor24);
		params.put("pv_otvalor25" , otvalor25);
		params.put("pv_otvalor26" , otvalor26);
		params.put("pv_otvalor27" , otvalor27);
		params.put("pv_otvalor28" , otvalor28);
		params.put("pv_otvalor29" , otvalor29);
		params.put("pv_otvalor30" , otvalor30);
		params.put("pv_otvalor31" , otvalor31);
		params.put("pv_otvalor32" , otvalor32);
		params.put("pv_otvalor33" , otvalor33);
		params.put("pv_otvalor34" , otvalor34);
		params.put("pv_otvalor35" , otvalor35);
		params.put("pv_otvalor36" , otvalor36);
		params.put("pv_otvalor37" , otvalor37);
		params.put("pv_otvalor38" , otvalor38);
		params.put("pv_otvalor39" , otvalor39);
		params.put("pv_otvalor40" , otvalor40);
		params.put("pv_otvalor41" , otvalor41);
		params.put("pv_otvalor42" , otvalor42);
		params.put("pv_otvalor43" , otvalor43);
		params.put("pv_otvalor44" , otvalor44);
		params.put("pv_otvalor45" , otvalor45);
		params.put("pv_otvalor46" , otvalor46);
		params.put("pv_otvalor47" , otvalor47);
		params.put("pv_otvalor48" , otvalor48);
		params.put("pv_otvalor49" , otvalor49);
		params.put("pv_otvalor50" , otvalor50);
		params.put("pv_otvalor51" , otvalor51);
		params.put("pv_otvalor52" , otvalor52);
		
		String[] inputKeys = new String[] {
				"pv_cdunieco","pv_cdramo","pv_estado","pv_nmpoliza","pv_nmsituac","pv_nmsuplem","pv_status","pv_cdrol","pv_cdperson","pv_cdatribu","pv_cdtipsit",
                "pv_otvalor01","pv_otvalor02","pv_otvalor03","pv_otvalor04","pv_otvalor05","pv_otvalor06","pv_otvalor07","pv_otvalor08","pv_otvalor09","pv_otvalor10",
                "pv_otvalor11","pv_otvalor12","pv_otvalor13","pv_otvalor14","pv_otvalor15","pv_otvalor16","pv_otvalor17","pv_otvalor18","pv_otvalor19","pv_otvalor20",
                "pv_otvalor21","pv_otvalor22","pv_otvalor23","pv_otvalor24","pv_otvalor25","pv_otvalor26","pv_otvalor27","pv_otvalor28","pv_otvalor29","pv_otvalor30",
                "pv_otvalor31","pv_otvalor32","pv_otvalor33","pv_otvalor34","pv_otvalor35","pv_otvalor36","pv_otvalor37","pv_otvalor38","pv_otvalor39","pv_otvalor40",
                "pv_otvalor41","pv_otvalor42","pv_otvalor43","pv_otvalor44","pv_otvalor45","pv_otvalor46","pv_otvalor47","pv_otvalor48","pv_otvalor49","pv_otvalor50",
                "pv_otvalor51","pv_otvalor52","pv_otvalor53","pv_otvalor54","pv_otvalor55","pv_otvalor56","pv_otvalor57","pv_otvalor58","pv_otvalor59","pv_otvalor60",
                "pv_otvalor61","pv_otvalor62","pv_otvalor63","pv_otvalor64","pv_otvalor65","pv_otvalor66","pv_otvalor67","pv_otvalor68","pv_otvalor69","pv_otvalor70",
                "pv_otvalor71","pv_otvalor72","pv_otvalor73","pv_otvalor74","pv_otvalor75","pv_otvalor76","pv_otvalor77","pv_otvalor78","pv_otvalor79","pv_otvalor80",
                "pv_otvalor81","pv_otvalor82","pv_otvalor83","pv_otvalor84","pv_otvalor85","pv_otvalor86","pv_otvalor87","pv_otvalor88","pv_otvalor89","pv_otvalor90",
                "pv_otvalor91","pv_otvalor92","pv_otvalor93","pv_otvalor94","pv_otvalor95","pv_otvalor96","pv_otvalor97","pv_otvalor98","pv_otvalor99"
		};
    	for(String key:inputKeys) {
    		if(!params.containsKey(key)) {
    			params.put(key, null);
    		}
    	}
    	
		logger.debug(
				new StringBuilder()
				.append("\n************************************************")
				.append("\n****** PKG_SATELITES.P_MOV_TVALOPER_NUEVO ******")
				.append("\n****** params=").append(params)
				.append("\n************************************************")
				.toString()
				);
		ejecutaSP(new MovimientosTvaloper(getDataSource()), params);
	}
	
	protected class MovimientosTvaloper extends StoredProcedure
	{
    	protected MovimientosTvaloper(DataSource dataSource)
    	{
            super(dataSource,"PKG_SATELITES2.P_MOV_TVALOPER_NUEVO");
            declareParameter(new SqlParameter("cdrol"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperson"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor01" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor40" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor41" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor42" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor43" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor44" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor45" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor46" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor47" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor48" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor49" , OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor50", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor51", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor52", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor53", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor54", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor55", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor56", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor57", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor58", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor59", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor60", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor61", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor62", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor63", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor64", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor65", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor66", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor67", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor68", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor69", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor70", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor71", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor72", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor73", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor74", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor75", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor76", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor77", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor78", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor79", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor80", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor81", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor82", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor83", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor84", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor85", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor86", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor87", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor88", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor89", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor90", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor91", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor92", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor93", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor94", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor95", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor96", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor97", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor98", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor99", OracleTypes.VARCHAR));
			
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	
	@Override
	public void insertaTvaloper(
			String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac,
			String nmsuplem, String status, String cdrol, String cdperson, String cdatribu, String cdtipsit,
			String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05,
			String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10,
			String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15,
			String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20,
			String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25,
			String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30,
			String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35,
			String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40,
			String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45,
			String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50,
			String otvalor51, String otvalor52
			) throws Exception {
		
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put("pv_cdunieco",  cdunieco);
		params.put("pv_cdramo",    cdramo);
		params.put("pv_estado",    estado);
		params.put("pv_nmpoliza",  nmpoliza);
		params.put("pv_nmsituac",  nmsituac);
		params.put("pv_nmsuplem",  nmsuplem);
		params.put("pv_status",    status);
		params.put("pv_cdrol",     cdrol);
		params.put("pv_cdperson",  cdperson);
		params.put("pv_cdatribu",  cdatribu);
		params.put("pv_cdtipsit",  cdtipsit);
		params.put("pv_otvalor01", otvalor01);
		params.put("pv_otvalor02", otvalor02);
		params.put("pv_otvalor03", otvalor03);
		params.put("pv_otvalor04", otvalor04);
		params.put("pv_otvalor05", otvalor05);
		params.put("pv_otvalor06", otvalor06);
		params.put("pv_otvalor07", otvalor07);
		params.put("pv_otvalor08", otvalor08);
		params.put("pv_otvalor09", otvalor09);
		params.put("pv_otvalor10", otvalor10);
		params.put("pv_otvalor11", otvalor11);
		params.put("pv_otvalor12", otvalor12);
		params.put("pv_otvalor13", otvalor13);
		params.put("pv_otvalor14", otvalor14);
		params.put("pv_otvalor15", otvalor15);
		params.put("pv_otvalor16", otvalor16);
		params.put("pv_otvalor17", otvalor17);
		params.put("pv_otvalor18", otvalor18);
		params.put("pv_otvalor19", otvalor19);
		params.put("pv_otvalor20", otvalor20);
		params.put("pv_otvalor21", otvalor21);
		params.put("pv_otvalor22", otvalor22);
		params.put("pv_otvalor23", otvalor23);
		params.put("pv_otvalor24", otvalor24);
		params.put("pv_otvalor25", otvalor25);
		params.put("pv_otvalor26", otvalor26);
		params.put("pv_otvalor27", otvalor27);
		params.put("pv_otvalor28", otvalor28);
		params.put("pv_otvalor29", otvalor29);
		params.put("pv_otvalor30", otvalor30);
		params.put("pv_otvalor31", otvalor31);
		params.put("pv_otvalor32", otvalor32);
		params.put("pv_otvalor33", otvalor33);
		params.put("pv_otvalor34", otvalor34);
		params.put("pv_otvalor35", otvalor35);
		params.put("pv_otvalor36", otvalor36);
		params.put("pv_otvalor37", otvalor37);
		params.put("pv_otvalor38", otvalor38);
		params.put("pv_otvalor39", otvalor39);
		params.put("pv_otvalor40", otvalor40);
		params.put("pv_otvalor41", otvalor41);
		params.put("pv_otvalor42", otvalor42);
		params.put("pv_otvalor43", otvalor43);
		params.put("pv_otvalor44", otvalor44);
		params.put("pv_otvalor45", otvalor45);
		params.put("pv_otvalor46", otvalor46);
		params.put("pv_otvalor47", otvalor47);
		params.put("pv_otvalor48", otvalor48);
		params.put("pv_otvalor49", otvalor49);
		params.put("pv_otvalor50", otvalor50);
		params.put("pv_otvalor51", otvalor51);
		params.put("pv_otvalor52", otvalor52);
		
		String[] inputKeys = new String[] {
				"pv_cdunieco","pv_cdramo","pv_estado","pv_nmpoliza","pv_nmsituac","pv_nmsuplem","pv_status","pv_cdrol","pv_cdperson","pv_cdatribu","pv_cdtipsit",
                "pv_otvalor01","pv_otvalor02","pv_otvalor03","pv_otvalor04","pv_otvalor05","pv_otvalor06","pv_otvalor07","pv_otvalor08","pv_otvalor09","pv_otvalor10",
                "pv_otvalor11","pv_otvalor12","pv_otvalor13","pv_otvalor14","pv_otvalor15","pv_otvalor16","pv_otvalor17","pv_otvalor18","pv_otvalor19","pv_otvalor20",
                "pv_otvalor21","pv_otvalor22","pv_otvalor23","pv_otvalor24","pv_otvalor25","pv_otvalor26","pv_otvalor27","pv_otvalor28","pv_otvalor29","pv_otvalor30",
                "pv_otvalor31","pv_otvalor32","pv_otvalor33","pv_otvalor34","pv_otvalor35","pv_otvalor36","pv_otvalor37","pv_otvalor38","pv_otvalor39","pv_otvalor40",
                "pv_otvalor41","pv_otvalor42","pv_otvalor43","pv_otvalor44","pv_otvalor45","pv_otvalor46","pv_otvalor47","pv_otvalor48","pv_otvalor49","pv_otvalor50",
                "pv_otvalor51","pv_otvalor52","pv_otvalor53","pv_otvalor54","pv_otvalor55","pv_otvalor56","pv_otvalor57","pv_otvalor58","pv_otvalor59","pv_otvalor60",
                "pv_otvalor61","pv_otvalor62","pv_otvalor63","pv_otvalor64","pv_otvalor65","pv_otvalor66","pv_otvalor67","pv_otvalor68","pv_otvalor69","pv_otvalor70",
                "pv_otvalor71","pv_otvalor72","pv_otvalor73","pv_otvalor74","pv_otvalor75","pv_otvalor76","pv_otvalor77","pv_otvalor78","pv_otvalor79","pv_otvalor80",
                "pv_otvalor81","pv_otvalor82","pv_otvalor83","pv_otvalor84","pv_otvalor85","pv_otvalor86","pv_otvalor87","pv_otvalor88","pv_otvalor89","pv_otvalor90",
                "pv_otvalor91","pv_otvalor92","pv_otvalor93","pv_otvalor94","pv_otvalor95","pv_otvalor96","pv_otvalor97","pv_otvalor98","pv_otvalor99"
		};
    	for(String key:inputKeys) {
    		if(!params.containsKey(key)) {
    			params.put(key, null);
    		}
    	}
		
		ejecutaSP(new PMovTvaloper(getDataSource()), params);
	}
	
	protected class PMovTvaloper extends StoredProcedure {
		
		protected PMovTvaloper(DataSource dataSource) {
			super(dataSource,"PKG_SATELITES2.P_MOV_TVALOPER");
			
			declareParameter(new SqlParameter("pv_cdunieco",  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo",    OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza",  OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsituac",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status",    OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol",     OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit",  OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor01", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor02", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor03", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor04", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor05", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor06", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor07", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor08", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor09", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor10", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor11", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor12", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor13", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor14", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor15", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor16", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor17", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor18", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor19", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor20", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor21", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor22", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor23", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor24", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor25", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor26", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor27", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor28", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor29", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor30", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor31", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor32", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor33", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor34", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor35", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor36", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor37", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor38", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor39", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor40", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor41", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor42", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor43", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor44", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor45", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor46", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor47", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor48", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor49", OracleTypes.VARCHAR));
			
			declareParameter(new SqlParameter("pv_otvalor50", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor51", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor52", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor53", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor54", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor55", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor56", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor57", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor58", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor59", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor60", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor61", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor62", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor63", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor64", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor65", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor66", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor67", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor68", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor69", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor70", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor71", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor72", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor73", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor74", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor75", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor76", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor77", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor78", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor79", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor80", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor81", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor82", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor83", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor84", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor85", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor86", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor87", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor88", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor89", OracleTypes.VARCHAR));

			declareParameter(new SqlParameter("pv_otvalor90", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor91", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor92", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor93", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor94", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor95", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor96", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor97", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor98", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor99", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
	
			compile();
		}
	}
	
	
	@Override
	public List<Map<String,String>>cargarDocumentosPersona(Map<String,String> params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_DOCUMENTOS_PERSONA ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new CargarDocumentosPersona(getDataSource()), params);
		return (List<Map<String,String>>)resultado.get("pv_registro_o");
	}
	
	protected class CargarDocumentosPersona extends StoredProcedure
	{
		private String columnas[]=new String[]{
				"cddocume"
				,"dsdocume"
				,"cdperson"
				,"feinici"
				,"liga"
		};
		
    	protected CargarDocumentosPersona(DataSource dataSource)
    	{
            super(dataSource,"PKG_CONSULTA.P_GET_DOCUMENTOS_PERSONA");
            declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	
	@Override
	public List<Map<String,String>>obtieneAccionistas(Map<String,String> params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_ESTRUC_CORPORATIVA ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new ObtieneAccionistas(getDataSource()), params);
		return (List<Map<String,String>>)resultado.get("pv_registro_o");
	}
	
	protected class ObtieneAccionistas extends StoredProcedure
	{
		
		protected ObtieneAccionistas(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ESTRUC_CORPORATIVA");
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtpesco_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMORDINA" , "DSNOMBRE" , "CDNACION" , "PORPARTI"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void validarDocumentosPersona(Map<String,String> params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n********************************************************")
				.append("\n****** PKG_SATELITES.P_VALIDA_DOCTOS_OBLIGATORIOS ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************************")
				.toString()
				);
		ejecutaSP(new ValidarDocumentosPersona(getDataSource()), params);
	}
	
	protected class ValidarDocumentosPersona extends StoredProcedure
	{
    	protected ValidarDocumentosPersona(DataSource dataSource)
    	{
            super(dataSource,"PKG_SATELITES.P_VALIDA_DOCTOS_OBLIGATORIOS");
            declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrol" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public String cargarNombreDocumentoPersona(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n********************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_NOMBRE_CDDOCUME_PERSONA ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new CargarNombreDocumentoPersona(getDataSource()), params);
		return (String)resultado.get("pv_cddocume_o");
	}
	
	protected class CargarNombreDocumentoPersona extends StoredProcedure
	{
    	protected CargarNombreDocumentoPersona(DataSource dataSource)
    	{
            super(dataSource,"PKG_CONSULTA.P_GET_NOMBRE_CDDOCUME_PERSONA");
            declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("codidocu" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_cddocume_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }

	@Override
	public String guardaAccionista(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES.P_MOV_TESCOPER ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new GuardaAccionista(getDataSource()), params);
		return (String)resultado.get("pv_msg_id_o");
	}
	
	protected class GuardaAccionista extends StoredProcedure
	{
		protected GuardaAccionista(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_TESCOPER");
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtpesco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmordina_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnombre_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdnacion_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_porparti_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String eliminaAccionistas(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************")
				.append("\n****** PKG_SATELITES.P_ELIMINA_TESCOPER ******")
				.append("\n****** params=").append(params)
				.append("\n**********************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new EliminaAccionistas(getDataSource()), params);
		return (String)resultado.get("pv_msg_id_o");
	}
	
	protected class EliminaAccionistas extends StoredProcedure
	{
		protected EliminaAccionistas(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_ELIMINA_TESCOPER");
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String actualizaStatusPersona(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n******************************************************")
				.append("\n****** PKG_SATELITES.P_ACTUALIZA_STATUS_PERSONA ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new ActualizaStatusPersona(getDataSource()), params);
		logger.debug("Estatus de la persona: " + resultado.get("pv_dsstatus_o") );
		return (String)resultado.get("pv_dsstatus_o");
	}
	
	protected class ActualizaStatusPersona extends StoredProcedure
	{
		protected ActualizaStatusPersona(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_ACTUALIZA_STATUS_PERSONA");
			declareParameter(new SqlParameter("pv_cdperson_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_dsstatus_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public Map<String,String> obtieneMunicipioYcolonia(Map<String, String> params) throws Exception{
		Map<String,Object>resultado=ejecutaSP(new ObtieneMunicipioYcolonia(getDataSource()), params);
		logger.debug("resultado de municipio y colonia: " + resultado.get("pv_registro_o"));
		return ((List<Map<String,String>>)resultado.get("pv_registro_o")).get(0);
	}
	
	protected class ObtieneMunicipioYcolonia extends StoredProcedure
	{
		protected ObtieneMunicipioYcolonia(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_RECUPERA_MUNICIPIO_COLONIA");
			declareParameter(new SqlParameter("pv_cdpostal_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdedo_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsmunici_i"    	, OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dscoloni_i"    , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDMUNICI" , "CDCOLONI"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void actualizaCodigoExterno(Map<String, String> params) throws Exception{
		ejecutaSP(new ActualizaCodigoExterno(getDataSource()), params);
	}
	
	protected class ActualizaCodigoExterno extends StoredProcedure
	{
		protected ActualizaCodigoExterno(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACTUALIZA_CDIDEPER_ART140");
			declareParameter(new SqlParameter("pv_cdperson_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdideper_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swsalud_i"    	, OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarEspPersona(String cdperson) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdperson" , cdperson);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarEspPersona(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>) procRes.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException(Utils.join("No se encuentra la persona con clave ",cdperson));
		}
		else if(lista.size()>1)
		{
			throw new ApplicationException(Utils.join("Registro de persona duplicado con clave ",cdperson));
		}
		return lista.get(0);
	}
	
	protected class RecuperarEspPersona extends StoredProcedure
	{
		protected RecuperarEspPersona(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ESPEJO_MPERSONA");
			declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"cdperson"  , "cdtipide"  , "cdideper"   , "dsnombre"    , "cdtipper"
					,"otfisjur" , "otsexo"    , "fenacimi"   , "cdrfc"       , "foto"
					,"dsemail"  , "dsnombre1" , "dsapellido" , "dsapellido1" , "cdnacion"
					,"dscomnom" , "dsrazsoc"  , "feingreso"  , "feactual"    , "dsnomusu"
					,"cdestciv" , "cdgrueco"  , "cdstippe"   , "nmnumnom"    , "curp"
					,"canaling" , "conducto"  , "ptcumupr"   , "status"      , "residencia"
					,"nongrata" , "cdideext"  , "cdsucemi"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	public String obtieneCdperson() throws Exception {
		
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String,Object> resultado = ejecutaSP(new ObtieneCdpersonSP(getDataSource()), params);
		return (String)resultado.get("pv_cdperson_o");
	}
	
	protected class ObtieneCdpersonSP extends StoredProcedure {
		
		protected ObtieneCdpersonSP(DataSource dataSource) {
			
			super(dataSource, "PKG_COTIZA.P_GET_CDPERSON");
			declareParameter(new SqlOutParameter("pv_cdperson_o", OracleTypes.VARCHAR));
		    declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
		    declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
	public void actualizaCdideper(String cdunieco, String cdramo, String estado, String nmpoliza, 
			String nmsuplem, String cdideper) throws Exception {
		
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_cdideper_i", cdideper);
		ejecutaSP(new ActualizaCdideperSP(getDataSource()), params);
	}
	
	protected class ActualizaCdideperSP extends StoredProcedure {

		protected ActualizaCdideperSP(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_ACTUALIZA_CODCLI_EXTERNO");

			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i",   OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdideper_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o",OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
		}
	}
	
	@Override
	public String guardarConfiguracionClientes(Map<String, Object> paramsCliente) throws Exception {
		Map<String, Object> mapResult = ejecutaSP(new GuardarClienteNonGratos(getDataSource()), paramsCliente);
		return (String) mapResult.get("pv_msg_id_o");
	}
	
	protected class GuardarClienteNonGratos extends StoredProcedure {
		protected GuardarClienteNonGratos(DataSource dataSource) {
			super(dataSource, "PKG_DESARROLLO.P_VALIDA_CDRFC_TPERNGRA");
			declareParameter(new SqlParameter("pv_cdrfc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipper_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnombre_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsdomicil_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_obsermot_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipcli_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cduser_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fefecha_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String actualizaClienteClientexTipo(String rfc, String activaCliente, String tipCliente) throws Exception {
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_activaCliente_i", activaCliente);
		params.put("pv_cdtipcli_i", tipCliente);
		Map<String, Object> resultado = ejecutaSP(new ActualizaClienteClientexTipo(getDataSource()), params);
		return (String) resultado.get("pv_msg_id_o");
	}
	
	protected class ActualizaClienteClientexTipo extends StoredProcedure {
		protected ActualizaClienteClientexTipo(DataSource dataSource) {
			super(dataSource, "PKG_DESARROLLO.P_ACT_INF_CLIENTE");
			declareParameter(new SqlParameter("pv_cdrfc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_activaCliente_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipcli_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneListaClientesxTipo(String rfc, String proceso) throws Exception {
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdrfc_i", rfc);
		params.put("pv_proceso_i", proceso);
		Map<String,Object>resultado=ejecutaSP(new ObtieneListaClientesxTipo(getDataSource()), params);
		logger.debug("resultado de municipio y colonia: " + resultado.get("pv_registro_o"));
		return ((List<Map<String,String>>)resultado.get("pv_registro_o"));
		
	}
	
	protected class ObtieneListaClientesxTipo extends StoredProcedure
	{
		protected ObtieneListaClientesxTipo(DataSource dataSource)
		{
			super(dataSource,"PKG_DESARROLLO.P_GET_TPERNGRA2");
			//super(dataSource,"PKG_DESARROLLO.P_GET_TPERNGRA");
			declareParameter(new SqlParameter("pv_cdrfc_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_proceso_i"    , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDRFC", 	"STATUS", 	"CDTIPPER", 	"CDAGENTE",		"DSNOMBRE",	"DSDOMICIL", 
					"OBSERMOT", "CDUSER", 	"FEFECHA", 		"DESCAGENTE", 	"DESTIPOPER"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<GenericVO> consultaClientes(String cdperson) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdperson_i", cdperson);
		Map<String,Object> resultadoMap=this.ejecutaSP(new ConsultaClientes(this.getDataSource()), params);
		return (List<GenericVO>) resultadoMap.get("pv_registro_o");
	}
	protected class ConsultaClientes extends StoredProcedure
	{
		protected ConsultaClientes(DataSource dataSource)
		{
			super(dataSource, "PKG_DESARROLLO.P_GET_USUARIOS_RFC");
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR,new ClientesNonGratos()));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class ClientesNonGratos  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GenericVO consulta = new GenericVO();
        	consulta.setKey(rs.getString("CDRFC"));
        	consulta.setValue(rs.getString("DSNOMBRE"));
            return consulta;
        }
    }

	@Override
	public String obtieneInformacionCliente(String sucursal, String ramo, String poliza) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String validaExisteAseguradoSicaps(String cdideper)throws Exception
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pv_cdideper_i", cdideper);
		Map<String,Object>resultado=ejecutaSP(new ValidaExisteAseguradoSicaps(getDataSource()), params);
		logger.debug("Estatus de la persona: " + resultado.get("pv_cdperson_o") );
		return (String)resultado.get("pv_cdperson_o");
	}
	
	protected class ValidaExisteAseguradoSicaps extends StoredProcedure
	{
		protected ValidaExisteAseguradoSicaps(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_VALIDA_PERSONA_SICAPS");
			declareParameter(new SqlParameter("pv_cdideper_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdperson_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtieneConfPatallaCli(String cdperson, String usuario, String rol, String tipoCliente) throws Exception{
		
		List<Map<String,String>> camposConf = null;
		
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdsisrol_i", rol);
		params.put("pv_cdusuari_i", usuario);
		params.put("pv_cdperson_i", cdperson);
		params.put("pv_tpcteext_i", tipoCliente);
		Map<String,Object>resultado=ejecutaSP(new ObtieneConfPatallaCli(getDataSource()), params);
		logger.debug("Resultado de campos a configurar" + resultado.get("pv_registro_o"));
		
		List<Map<String,String>> resCampos = ((List<Map<String,String>>)resultado.get("pv_registro_o"));
		HashMap< String, Map<String,String> > camposUnicos = new HashMap< String, Map<String,String> >();
		
		if(resCampos != null && !resCampos.isEmpty()){
			
			for(Map<String,String> campo : resCampos){
				
				if(camposUnicos.containsKey(campo.get("CVECAMPO"))){
					
					if(StringUtils.isNotBlank(campo.get("USUARIO")) && campo.get("USUARIO").equalsIgnoreCase(usuario)){
						camposUnicos.put(campo.get("CVECAMPO"), campo);
					}
				}else{
					camposUnicos.put(campo.get("CVECAMPO"), campo);
				}
			}
		}
		
		camposConf =  new ArrayList<Map<String,String>>(camposUnicos.values());
		
		return camposConf;
		
	}
	
	protected class ObtieneConfPatallaCli extends StoredProcedure
	{
		protected ObtieneConfPatallaCli(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CAMPOS_CLIENTE_X_USUROL");
			declareParameter(new SqlParameter("pv_cdsisrol_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tpcteext_i"    , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CVECAMPO", "SOLOLECTURA", "OCULTO", "USUARIO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void sincronizaPersonaToValosit(
			String sexo
			,Date fenacimi
			,String cdtipsit
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("sexo"     , sexo);
		params.put("fenacimi" , fenacimi);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		ejecutaSP(new SincronizaPersonaToValositSP(getDataSource()),params);
	}
	
	protected class SincronizaPersonaToValositSP extends StoredProcedure
	{
		protected SincronizaPersonaToValositSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_SYNC_PERSONA_TO_VALOSIT");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("sexo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fenacimi" , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
    @Override
    public String obtieneAseguradoSICAPS(String nombres, String apellidoP, String apellidoM, Date fechaNac)throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pv_nombres_i", nombres);
        params.put("pv_apellidoP_i", apellidoP);
        params.put("pv_apellidoM_i", apellidoM);
        params.put("pv_fechaNac_i", fechaNac);
        Map<String,Object>resultado=ejecutaSP(new ObtieneAseguradoSICAPS(getDataSource()), params);
        logger.debug("Estatus de la persona: " + resultado.get("pv_existe_o") );
        return (String)resultado.get("pv_existe_o");
    }
    
    protected class ObtieneAseguradoSICAPS extends StoredProcedure
    {
        protected ObtieneAseguradoSICAPS(DataSource dataSource)
        {
            super(dataSource,"PKG_CONSULTA2.P_OBTIENE_ASEGURADO_SICAPS");
            declareParameter(new SqlParameter("pv_nombres_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_apellidoP_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_apellidoM_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fechaNac_i"    , OracleTypes.DATE));
            declareParameter(new SqlOutParameter("pv_existe_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }

}