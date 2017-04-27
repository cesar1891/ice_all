package mx.com.gseguros.portal.cotizacion.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.aon.portal.dao.ObtieneTatriperMapper;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.model.AseguradosFiltroVO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.ConfiguracionCoberturaDTO;
import mx.com.gseguros.portal.cotizacion.model.DatosUsuario;
import mx.com.gseguros.portal.cotizacion.model.ObtieneTatrigarMapper;
import mx.com.gseguros.portal.cotizacion.model.ObtieneTatripolMapper;
import mx.com.gseguros.portal.cotizacion.model.ObtieneTatrisitMapper;
import mx.com.gseguros.portal.cotizacion.model.PInsertaTbasvalsitDTO;
import mx.com.gseguros.portal.cotizacion.model.PInsertaTconvalsitDTO;
import mx.com.gseguros.portal.cotizacion.model.PMovMpolisitDTO;
import mx.com.gseguros.portal.cotizacion.model.PMovTvalositDTO;
import mx.com.gseguros.portal.cotizacion.model.ParametroCotizacion;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

public class CotizacionDAOImpl extends AbstractManagerDAO implements CotizacionDAO
{
	private final static Logger logger = Logger.getLogger(CotizacionDAOImpl.class);
	
	@Override
	public void movimientoTvalogarGrupoCompleto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,Map<String,String>valores
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdgrupo"  , cdgrupo);
		params.put("cdgarant" , cdgarant);
		params.put("status"   , status);
		
		for(int i=1;i<=640;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),3,"0")).toString(),null);
		}
		
		params.putAll(valores);
		
		logger.debug(
				new StringBuilder()
				.append("\n******************************************************")
				.append("\n****** PKG_SATELITES.P_MOV_TVALOGAR_GRUPO_DINAM ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************************")
				.toString()
				);
		ejecutaSP(new MovimientoTvalogarGrupoCompleto(getDataSource()), params);
	}
	
	protected class MovimientoTvalogarGrupoCompleto extends StoredProcedure
	{
		protected MovimientoTvalogarGrupoCompleto(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_TVALOGAR_GRUPO_DINAM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgarant" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			for(int i=1;i<=640;i++)
			{
				declareParameter(
						new SqlParameter
						(
								new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),3,"0")).toString()
								,OracleTypes.VARCHAR
						)
				);
			}
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTvalogarGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,String cdatribu
			,String valor
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdgrupo"  , cdgrupo);
		params.put("cdgarant" , cdgarant);
		params.put("status"   , status);
		params.put("cdatribu" , cdatribu);
		params.put("valor"    , valor);
		logger.debug(
				new StringBuilder()
				.append("\n************************************************")
				.append("\n****** PKG_SATELITES.P_MOV_TVALOGAR_GRUPO ******")
				.append("\n****** params=").append(params)
				.append("\n************************************************")
				.toString()
				);
		ejecutaSP(new MovimientoTvalogarGrupo(getDataSource()), params);
	}
	
	protected class MovimientoTvalogarGrupo extends StoredProcedure
	{
		protected MovimientoTvalogarGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_TVALOGAR_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgarant" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("valor"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoMpolisitTvalositGrupo(Map<String,String>params)throws Exception
	{
		ejecutaSP(new MovimientoMpolisitTvalositGrupo(getDataSource()), params);
	}
	
	protected class MovimientoMpolisitTvalositGrupo extends StoredProcedure
	{
		protected MovimientoMpolisitTvalositGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACTUALIZA_MPOLISIT_TVALOSIT");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor06" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor07" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor08" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor09" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor16" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoMpoligarGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdtipsit
			,String cdgrupo
			,String cdgarant
			,String status
			,String cdmoneda
			,String accion
			,String respvalogar
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("nmsuplem"    , nmsuplem);
		params.put("cdtipsit"    , cdtipsit);
		params.put("cdgrupo"     , cdgrupo);
		params.put("cdgarant"    , cdgarant);
		params.put("status"      , status);
		params.put("cdmoneda"    , cdmoneda);
		params.put("accion"      , accion);
		params.put("respvalogar" , respvalogar);
		logger.debug(
				new StringBuilder()
				.append("\n************************************************")
				.append("\n****** PKG_SATELITES.P_MOV_MPOLIGAR_GRUPO ******")
				.append("\n****** params=").append(params)
				.append("\n************************************************")
				.toString()
				);
		ejecutaSP(new MovimientoMpoligarGrupo(getDataSource()), params);
	}
	
	protected class MovimientoMpoligarGrupo extends StoredProcedure
	{
		protected MovimientoMpoligarGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_MPOLIGAR_GRUPO");
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgarant"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmoneda"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("respvalogar" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarDatosCotizacionGrupo2(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String ntramite
			)throws Exception,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("ntramite" , ntramite);
		logger.debug(
				new StringBuilder()
				.append("\n********************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_COTIZACION_GRUPO2 ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************************")
				.toString()
				);
		Map<String,Object>      procResult = ejecutaSP(new CargarDatosCotizacionGrupo2(getDataSource()), params);
		List<Map<String,String>>listaDatos = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(listaDatos==null||listaDatos.size()==0)
		{
			throw new Exception("No se pudo cargar la poliza");
		}
		return listaDatos.get(0);
	}
	
	protected class CargarDatosCotizacionGrupo2 extends StoredProcedure
	{
		protected CargarDatosCotizacionGrupo2(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_COTIZACION_GRUPO2");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"cdrfc"
					,"cdperson"
					,"cdideper_"
					,"cdideext_"
					,"nombre"
					,"codpostal"
					,"cdedo"
					,"cdmunici"
					,"dsdomici"
					,"nmnumero"
					,"nmnumint"
					            ,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09"
					,"otvalor10","otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19"
					,"otvalor20","otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29"
					,"otvalor30","otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39"
					,"otvalor40","otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49"
					,"otvalor50"
					,"ntramite"
					,"nmpoliza"
					,"cdperpag"
					,"cdagente"
					,"clasif"
					,"pcpgocte"
					,"cdpool"
					,"swexiper"
					,"feini"
					,"fefin"
					,"nmpolant"
					,"nmrenova"
					,"nmorddom"
				};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarDatosCotizacionGrupo(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new CargarDatosCotizacionGrupo(getDataSource()), params);
		List<Map<String,String>>listaDatos=(List<Map<String,String>>)resultado.get("pv_registro_o");
		Map<String,String>datos=new LinkedHashMap<String,String>();
		if(listaDatos!=null&&listaDatos.size()>0)
		{
			datos=listaDatos.get(0);
		}
		return datos;
	}
	
	protected class CargarDatosCotizacionGrupo extends StoredProcedure
	{
		private String[] columnas=new String[]{
			"cdrfc"
			,"cdperson"
			,"cdideper_"
			,"cdideext_"
			,"nombre"
			,"codpostal"
			,"cdedo"
			,"cdmunici"
			,"dsdomici"
			,"nmnumero"
			,"nmnumint"
			,"cdgiro"
			,"cdrelconaseg"
			,"cdformaseg"
			,"ntramite"
			,"nmpoliza"
			,"cdperpag"
			,"cdagente"
			,"clasif"
			,"pcpgocte"
			,"tipoDerPol"
			,"morbilidad"
			,"montoDerPol"
			,"recargoPers"
			,"recargoPago"
			,"dctocmer"
			,"cdpool"
			,"swexiper"
			,"feini"
			,"fefin"
			,"nmpolant"
			,"nmrenova"
			,"nmorddom"
			,"numcontrato"
		};
		
		protected CargarDatosCotizacionGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_COTIZACION_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarDatosCotizacionGrupoEndoso(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new CargarDatosCotizacionGrupoEndoso(getDataSource()), params);
		List<Map<String,String>>listaDatos=(List<Map<String,String>>)resultado.get("pv_registro_o");
		Map<String,String>datos=new LinkedHashMap<String,String>();
		if(listaDatos!=null&&listaDatos.size()>0)
		{
			datos=listaDatos.get(0);
		}
		logger.debug(Utils.log("PKG_CONSULTA.P_GET_DATOS_COTIZACION_ENDOSO registro=",datos));
		return datos;
	}
	
	protected class CargarDatosCotizacionGrupoEndoso extends StoredProcedure
	{
		private String[] columnas=new String[]{
			"cdrfc"
			,"cdperson"
			,"cdideper_"
			,"cdideext_"
			,"nombre"
			,"codpostal"
			,"cdedo"
			,"cdmunici"
			,"dsdomici"
			,"nmnumero"
			,"nmnumint"
			,"cdgiro"
			,"cdrelconaseg"
			,"cdformaseg"
			,"ntramite"
			,"nmpoliza"
			,"cdperpag"
			,"cdagente"
			,"clasif"
			,"pcpgocte"
			,"tipoDerPol"
			,"morbilidad"
			,"montoDerPol"
			,"recargoPers"
			,"recargoPago"
			,"dctocmer"
			,"cdpool"
			,"swexiper"
			,"feini"
			,"fefin"
			,"nmpolant"
			,"nmrenova"
			,"nmorddom"
		};
		
		protected CargarDatosCotizacionGrupoEndoso(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_COTIZACION_ENDOSO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarGruposCotizacion2(String cdunieco,String cdramo,String estado,String nmpoliza)throws Exception, Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_GRUPOS_COTIZACION2 ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString()
				);
		Map<String,Object>      procResult  = ejecutaSP(new CargarGruposCotizacion2(getDataSource()), params);
		List<Map<String,String>>listaGrupos = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(listaGrupos==null||listaGrupos.size()==0)
		{
			throw new Exception("No se encontraron grupos");
		}
		return listaGrupos;
	}
	
	protected class CargarGruposCotizacion2 extends StoredProcedure
	{
		protected CargarGruposCotizacion2(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_GRUPOS_COTIZACION2");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] columnas=new String[]
					{
					"letra"
					,"cdplan"
					,"nombre"
					            ,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09"
					,"otvalor10","otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19"
					,"otvalor20","otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29"
					,"otvalor30","otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39"
					,"otvalor40","otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49"
					,"otvalor50","otvalor51","otvalor52","otvalor53","otvalor54","otvalor55","otvalor56","otvalor57","otvalor58","otvalor59"
					,"otvalor60","otvalor61","otvalor62","otvalor63","otvalor64","otvalor65","otvalor66","otvalor67","otvalor68","otvalor69"
					,"otvalor70","otvalor71","otvalor72","otvalor73","otvalor74","otvalor75","otvalor76","otvalor77","otvalor78","otvalor79"
					,"otvalor80","otvalor81","otvalor82","otvalor83","otvalor84","otvalor85","otvalor86","otvalor87","otvalor88","otvalor89"
					,"otvalor90","otvalor91","otvalor92","otvalor93","otvalor94","otvalor95","otvalor96","otvalor97","otvalor98","otvalor99"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarGruposCotizacion(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new CargarGruposCotizacion(getDataSource()), params);
		List<Map<String,String>>listaGrupos=(List<Map<String,String>>)resultado.get("pv_registro_o");
		return listaGrupos;
	}
	
	protected class CargarGruposCotizacion extends StoredProcedure
	{
		protected CargarGruposCotizacion(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_GRUPOS_COTIZACION");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] columnas=new String[]
					{
					"ptsumaaseg"
					,"incrinfl"
					,"extrreno"
					,"cesicomi"
					,"pondubic"
					,"descbono"
					,"gastadmi"
					,"utilidad"
					,"comiagen"
					,"comiprom"
					,"bonoince"
					,"otrogast"
					,"nombre"
					,"ayudamater"
					,"letra"
					,"cdplan"
					,"dsplanl"
					,"cdplanorig"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarDatosGrupoLinea(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new CargarDatosGrupoLinea(getDataSource()), params);
		List<Map<String,String>>listaDatos=(List<Map<String,String>>)resultado.get("pv_registro_o");
		Map<String,String>datos=new LinkedHashMap<String,String>();
		if(listaDatos!=null&&listaDatos.size()>0)
		{
			datos=listaDatos.get(0);
		}
		return datos;
	}
	
	protected class CargarDatosGrupoLinea extends StoredProcedure
	{
		protected CargarDatosGrupoLinea(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_GRUPO_LINEA");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"CDGRUPO"
					,"NMSITUAC"
					,"DEDUCIBLE"
					,"ASISINTE"
					,"EMEREXTR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarTvalogarsGrupo(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new CargarTvalogarsGrupo(getDataSource()), params);
		List<Map<String,String>>listaTvalogars=(List<Map<String,String>>)resultado.get("pv_registro_o");
		return listaTvalogars;
	}
	
	protected class CargarTvalogarsGrupo extends StoredProcedure
	{
		private List<String> columnas=new ArrayList<String>();
		protected CargarTvalogarsGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TVALOGARS_GRUPO");
			
			columnas.add("amparada");
			columnas.add("cdgarant");
			columnas.add("swobliga");
			for(int i=1;i<=640;i++)
			{
				columnas.add(
						new StringBuilder()
						    .append("parametros.pv_otvalor")
						    .append(StringUtils.leftPad(String.valueOf(i),3,"0"))
						    .toString()
						    );
			}
			
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(columnas)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarTarifasPorEdad(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_COTIZA.P_OBTIENE_COTIZACION_COLECTIVO ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new CargarTarifasPorEdad(getDataSource()), params);
		List<Map<String,String>>listaTvalogars=(List<Map<String,String>>)resultado.get("pv_registro_o");
		return listaTvalogars;
	}
	
	protected class CargarTarifasPorEdad extends StoredProcedure
	{
		protected CargarTarifasPorEdad(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_OBTIENE_COTIZACION_COLECTIVO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"EDAD"
					,"HOMBRES"
					,"MUJERES"
					,"TARIFA_UNICA_HOMBRES"
					,"TARIFA_UNICA_MUJERES"
					,"TARIFA_TOTAL_HOMBRES"
					,"TARIFA_TOTAL_MUJERES"
					,"DERPOL_TOTAL_GENERAL"
					,"RECARGOS_TOTAL_GENERAL"
					,"IVA_TOTAL_GENERAL"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarTarifasPorCobertura(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_COTIZA.P_OBTIENE_PRIMA_PROM_COBERTURA ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>resultado=ejecutaSP(new CargarTarifasPorCobertura(getDataSource()), params);
		List<Map<String,String>>listaTvalogars=(List<Map<String,String>>)resultado.get("pv_registro_o");
		return listaTvalogars;
	}
	
	protected class CargarTarifasPorCobertura extends StoredProcedure
	{
		protected CargarTarifasPorCobertura(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_OBTIENE_PRIMA_PROM_COBERTURA");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"DSGARANT" , "PRIMA_PROMEDIO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String cargarNombreAgenteTramite(String ntramite)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("ntramite",ntramite);
		Map<String,Object>resultado=ejecutaSP(new CargarNombreAgenteTramite(getDataSource()), params);
		return (String)resultado.get("pv_nombre_o");
	}
	
	protected class CargarNombreAgenteTramite extends StoredProcedure
	{
		protected CargarNombreAgenteTramite(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_NOMBRE_AGENTE_TRAMITE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nombre_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarPermisosPantallaGrupo(String cdsisrol,String status)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdsisrol" , cdsisrol);
		params.put("status"   , status);
		logger.debug(
				new StringBuilder()
				.append("\n********************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_PERMISOS_PANTALLA_GRUPO ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************************")
				.toString());
		Map<String,Object>resultado=ejecutaSP(new CargarPermisosPantallaGrupo(getDataSource()), params);
		List<Map<String,String>>listaDatos=(List<Map<String,String>>)resultado.get("pv_registro_o");
		Map<String,String>datos=new LinkedHashMap<String,String>();
		if(listaDatos!=null&&listaDatos.size()>0)
		{
			datos=listaDatos.get(0);
		}
		return datos;
	}
	
	protected class CargarPermisosPantallaGrupo extends StoredProcedure
	{
		protected CargarPermisosPantallaGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PERMISOS_PANTALLA_GRUPO");
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"LINEA_EXTENDIDA"
					,"DETALLE_LINEA"
					,"COBERTURAS"
					,"FACTORES_EN_COBERTURAS"
					,"FACTORES"
					,"BLOQUEO_CONCEPTO"
					,"BLOQUEO_EDITORES"
					,"VENTANA_DOCUMENTOS"
					,"EXTRAPRIMAS"
					,"EXTRAPRIMAS_EDITAR"
					,"ASEGURADOS"
					,"ASEGURADOS_EDITAR"
					,"COBERTURAS_BOTON"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public Map<String,String>obtieneTipoValorAutomovil(Map<String,String>params)throws Exception
	{
		Map<String,Object>resultado=ejecutaSP(new ObtieneTipoValorAutomovil(getDataSource()), params);
		
		Map<String,String>datos=new LinkedHashMap<String,String>();
		datos.put("pv_etiqueta_o", (String) resultado.get("pv_etiqueta_o"));
		return datos;
	}
	
	protected class ObtieneTipoValorAutomovil extends StoredProcedure
	{
		protected ObtieneTipoValorAutomovil(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_VALAUT_X_CP");
			declareParameter(new SqlParameter("pv_cdpostal_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipveh_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_etiqueta_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String obtieneCodigoPostalAutomovil(Map<String,String>params)throws Exception
	{
		
		String codigoPostal  =  null;
		
		Map<String,Object>respuestaProcedure=ejecutaSP(new ObtieneCodigoPostalAutomovil(getDataSource()), params);
			
		List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
		Map<String,String>respuesta=null;
		if(lista!=null&&lista.size()>0)
		{
			respuesta=lista.get(0);
			if(respuesta.containsKey("CDPOSTAL")){
				codigoPostal = respuesta.get("CDPOSTAL");
			}
		}
		return codigoPostal;
	}
	
	protected class ObtieneCodigoPostalAutomovil extends StoredProcedure
	{
		protected ObtieneCodigoPostalAutomovil(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CODIPOSTAL_X_SITUACION");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i"   , OracleTypes.VARCHAR));
			
			String[] cols=new String[]{"CDPOSTAL"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarCensoCompletoMultisalud(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdestadoCli
			,String cdmuniciCli
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("censo"       , nombreArchivo);
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("cdestado"    , cdestadoCli);
		params.put("cdmunici"    , cdmuniciCli);
		params.put("cdplan1"     , cdplan1);
		params.put("cdplan2"     , cdplan2);
		params.put("cdplan3"     , cdplan3);
		params.put("cdplan4"     , cdplan4);
		params.put("cdplan5"     , cdplan5);
		params.put("complemento" , complemento);
		ejecutaSP(new GuardarCensoCompletoMultisalud(getDataSource()),params);
	}
	
	protected class GuardarCensoCompletoMultisalud extends StoredProcedure
	{
		protected GuardarCensoCompletoMultisalud(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_LAYOUT_CENSO_MS_COLEC_DEF");
			declareParameter(new SqlParameter("censo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmunici"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan1"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan2"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan3"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan4"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan5"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("complemento" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosExtraprimas(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO ******")
				.append("\n****** params=").append(params)
				.append("\n*************************************************")
				.toString());
		Map<String,Object>resultado   = ejecutaSP(new CargarAseguradosExtraprimas(getDataSource()), params);
		List<Map<String,String>>lista = (List<Map<String,String>>)resultado.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class CargarAseguradosExtraprimas extends StoredProcedure
	{
		protected CargarAseguradosExtraprimas(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NOMBRE"   , "NMSITUAC"   , "EDAD"             , "FENACIMI" 
                    ,"SEXO"    , "PARENTESCO" , "OCUPACION"        , "EXTPRI_OCUPACION"
                    ,"PESO"    , "ESTATURA"   , "EXTPRI_SOBREPESO" , "FAMILIA"
                    ,"TITULAR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
		
	@Override
	public void guardarExtraprimaAsegurado(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String ocupacion
			,String extraprimaOcupacion
			,String peso
			,String estatura
			,String extraprimaSobrepeso
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco"            , cdunieco );
		params.put("cdramo"              , cdramo );
		params.put("estado"              , estado );
		params.put("nmpoliza"            , nmpoliza );
		params.put("nmsuplem"            , nmsuplem );
		params.put("nmsituac"            , nmsituac );
		params.put("ocupacion"           , ocupacion );
		params.put("extraprimaOcupacion" , extraprimaOcupacion );
		params.put("peso"                , peso );
		params.put("estatura"            , estatura );
		params.put("extraprimaSobrepeso" , extraprimaSobrepeso );
		ejecutaSP(new GuardarExtraprimaAsegurado(getDataSource()),params);
	}
	
	protected class GuardarExtraprimaAsegurado extends StoredProcedure
	{
		protected GuardarExtraprimaAsegurado(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_ACTUALIZA_TVALOSIT_DAT_ADIC");
			declareParameter(new SqlParameter("cdunieco"            , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"              , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"              , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"            , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"            , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac"            , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ocupacion"           , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("extraprimaOcupacion" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("peso"                , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estatura"            , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("extraprimaSobrepeso" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarExtraprimaAsegurado(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			List<Map<String,String>> valores)throws Exception{
		logger.debug(
				new StringBuilder()
				.append("\n******************************************************")
				.append("\n****** pkg_procs_cartera.blk_update_tvs_msc ******")
				.append("\n****** cdunieco=").append(cdunieco)
				.append("\n****** cdramo=").append(cdramo)
				.append("\n****** estado=").append(estado)
				.append("\n****** nmpoliza=").append(nmpoliza)
				.append("\n****** nmsuplem=").append(nmsuplem)
				.append("\n****** nmsuplem=").append(nmsuplem)
				.append("\n****** valores=").append(valores)
				.append("\n******************************************************")
				.toString()
				);
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		String[][] array = new String[valores.size()][];		
		int i = 0;
		for(Map<String,String> valor : valores){
			ArrayList<String> otvalores = new ArrayList<String>(0);			
			otvalores.add(valor.get("nmsituac"));
			otvalores.add(valor.get("extpri_ocupacion"));
			otvalores.add(valor.get("ocupacion"));			
			otvalores.add(valor.get("peso"));
			otvalores.add(valor.get("estatura"));
			otvalores.add(valor.get("extpri_estatura"));
			otvalores.add(valor.get("cdgrupo"));
			array[i++] = otvalores.toArray(new String[otvalores.size()]);
		}
		params.put("cdunieco", cdunieco);
		params.put("cdramo", cdramo);
		params.put("estado", estado);
		params.put("nmpoliza", nmpoliza);
		params.put("nmsuplem", nmsuplem);
		params.put("array" , new SqlArrayValue(array));
		ejecutaSP(new GuardarExtraprimaAseguradoBC(getDataSource()),params);
	}
	
	protected class GuardarExtraprimaAseguradoBC extends StoredProcedure
	{
		protected GuardarExtraprimaAseguradoBC(DataSource dataSource)
		{
			super(dataSource,"pkg_procs_cartera.blk_update_tvs_msc");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"	  , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("array"    , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosGrupo(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_ASEGURADOS_GRUPO ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************")
				.toString()
				);
		Map<String,Object>respuesta   = ejecutaSP(new CargarAseguradosGrupo(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class CargarAseguradosGrupo extends StoredProcedure
	{
		protected CargarAseguradosGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ASEGURADOS_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDGRUPO"
					,"NMSITUAC"
					,"CDPERSON"
					,"PARENTESCO"
					,"NOMBRE"
					,"SEGUNDO_NOMBRE"
					,"APELLIDO_PATERNO"
					,"APELLIDO_MATERNO"
					,"FECHA_NACIMIENTO"
					,"SEXO"
					,"NACIONALIDAD"
					,"RFC"
					,"CDROL"
					,"SWEXIPER"
					,"CDIDEPER"
					,"FAMILIA"
					,"TITULAR"
					,"FEANTIGU"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosGrupo(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String cdgrupo,
			String start,
			String limit)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_ASEGURADOS_GRUPO ******")
				.append("\n****** cdunieco=").append(cdunieco)
				.append("\n****** cdramo=").append(cdramo)
				.append("\n****** estado=").append(estado)
				.append("\n****** nmpoliza=").append(nmpoliza)
				.append("\n****** nmsuplem=").append(nmsuplem)
				.append("\n****** cdgrupo=").append(cdgrupo)
				.append("\n****** start=").append(start)
				.append("\n****** limit=").append(limit)
				.append("\n********************************************")
				.toString()
				);
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdunieco", cdunieco);
		params.put("cdramo", cdramo);
		params.put("estado", estado);
		params.put("nmpoliza", nmpoliza);
		params.put("nmsuplem", nmsuplem);
		params.put("cdgrupo", cdgrupo);
		params.put("start", start);
		params.put("limit", limit);
		Map<String,Object> respuesta   = ejecutaSP(new CargarAseguradosGrupoPag(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Map<String,String> total = new HashMap<String,String>();
		total.put("total", String.valueOf(respuesta.get("pv_num_o")));
		lista.add(total);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_ASEGURADOS_GRUPO ******")
				.append("\n****** total=").append(String.valueOf(respuesta.get("pv_num_o")))
				.append("\n*******************************************************")
				.toString()
				);
		return lista;
	}
	
	protected class CargarAseguradosGrupoPag extends StoredProcedure
	{
		protected CargarAseguradosGrupoPag(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ASEGURADOS_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("start"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("limit"    , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDGRUPO"
					,"NMSITUAC"
					,"CDPERSON"
					,"PARENTESCO"
					,"NOMBRE"
					,"SEGUNDO_NOMBRE"
					,"APELLIDO_PATERNO"
					,"APELLIDO_MATERNO"
					,"FECHA_NACIMIENTO"
					,"SEXO"
					,"NACIONALIDAD"
					,"RFC"
					,"CDROL"
					,"SWEXIPER"
					,"CDIDEPER"
					,"FAMILIA"
					,"TITULAR"
					,"FEANTIGU"
			};
			declareParameter(new SqlOutParameter("pv_num_o"   	 , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void borrarMpoliperTodos(String cdunieco,String cdramo,String estado,String nmpoliza)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n********************************************")
				.append("\n****** PKG_SATELITES.P_BORRA_MPOLIPER ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************")
				.toString()
				);
		ejecutaSP(new BorrarMpoliperTodos(getDataSource()),params);
	}
	
	protected class BorrarMpoliperTodos extends StoredProcedure
	{
		protected BorrarMpoliperTodos(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_BORRA_MPOLIPER");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void borrarMpoliperGrupo(Map<String,String>params)throws Exception
	{
		ejecutaSP(new BorrarMpoliperGrupo(getDataSource()),params);
	}
	

	
	protected class BorrarMpoliperGrupo extends StoredProcedure
	{
		protected BorrarMpoliperGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_BORRA_MPOLIPER_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	@Deprecated
	public Map<String,String>cargarTipoSituacion(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TIPO_SITUACION ******")
				.append("\n****** params=").append(params)
				.append("\n***********************************************")
				.toString()
				);
		Map<String,Object>respuestaProcedure=ejecutaSP(new CargarTipoSituacion(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
		Map<String,String>respuesta=null;
		if(lista!=null&&lista.size()>0)
		{
			respuesta=lista.get(0);
		}
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** registro=").append(respuesta)
				.append("\n****** PKG_CONSULTA.P_GET_TIPO_SITUACION ******")
				.append("\n***********************************************")
				.toString()
				);
		return respuesta;
	}
	
	@Override
	public Map<String,String>cargarTipoSituacion(String cdramo,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TIPO_SITUACION ******")
				.append("\n****** params=").append(params)
				.append("\n***********************************************")
				.toString()
				);
		Map<String,Object>respuestaProcedure=ejecutaSP(new CargarTipoSituacion(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
		Map<String,String>respuesta=null;
		if(lista!=null&&lista.size()>0)
		{
			respuesta=lista.get(0);
		}
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** registro=").append(respuesta)
				.append("\n****** PKG_CONSULTA.P_GET_TIPO_SITUACION ******")
				.append("\n***********************************************")
				.toString()
				);
		return respuesta;
	}
	
	protected class CargarTipoSituacion extends StoredProcedure
	{
		protected CargarTipoSituacion(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TIPO_SITUACION");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String cols [] = new String[]{
					"SITUACION" , "AGRUPACION" , "MODALIDAD"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String cargarCduniecoAgenteAuto(String cdagente, String cdtipram)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdagente" , cdagente);
		params.put("cdtipram" , cdtipram);
		Map<String,Object>respuestaProcedure=ejecutaSP(new CargarCduniecoAgenteAuto(getDataSource()),params);
		return (String)respuestaProcedure.get("pv_cdunieco_o");
	}
	
	protected class CargarCduniecoAgenteAuto extends StoredProcedure
	{
		protected CargarCduniecoAgenteAuto(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CDUNIECO_X_AGENTE_AUTO");
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdunieco_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> obtenerDatosAgente(String cdagente,String cdramo)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdagente" , cdagente);
		params.put("cdramo"   , cdramo);
		logger.debug(
				new StringBuilder()
				.append("\n********************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_AGENTE_X_CDAGENTE ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************************")
				.toString()
				);
		Map<String,Object>procedureResult=ejecutaSP(new ObtenerDatosAgente(getDataSource()),params);
		List<Map<String,String>>listaAux=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new ApplicationException("No hay datos del agente para este producto");
		}
		else if(listaAux.size()>1)
		{
			throw new ApplicationException("Datos repetidos del agente para este producto");
		}
		return listaAux.get(0);
	}
	
	protected class ObtenerDatosAgente extends StoredProcedure
	{
		protected ObtenerDatosAgente(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_AGENTE_X_CDAGENTE");
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"CDAGENTE"
					,"NMCUADRO"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class CargarNumeroPasajerosPorTipoUnidad extends StoredProcedure
	{
		protected CargarNumeroPasajerosPorTipoUnidad(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_NUM_PASAJEROS");
			declareParameter(new SqlParameter("cdtipsit"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoUnidad" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NUMPASAJEROS" , "PASAJMIN" , "PASAJMAX" , "CLAVEGS" , "SUMASEG"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> obtenerParametrosCotizacion(
			ParametroCotizacion parametro
			,String cdramo
			,String cdtipsit
			,String clave4
			,String clave5)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("parametro" , parametro.getParametro());
		params.put("cdramo"    , cdramo);
		params.put("cdtipsit"  , cdtipsit);
		params.put("clave4"    , clave4);
		params.put("clave5"    , clave5);
		logger.debug(
				new StringBuilder()
				.append("\n************************************************")
				.append("\n****** PKG_LISTAS.P_GET_PARAMS_COTIZACION ******")
				.append("\n****** params=").append(params)
				.append("\n************************************************")
				.toString()
				);
		Map<String,Object>procedureResult = ejecutaSP(new ObtenerParametrosCotizacion(getDataSource()),params);
		List<Map<String,String>>listaAux  = (List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new ApplicationException("No hay parametros");
		}
		if(listaAux.size()>1)
		{
			throw new ApplicationException("Parametros duplicados");
		}
		logger.debug(
				new StringBuilder()
				.append("\n************************************************")
				.append("\n****** registro=").append(listaAux.get(0))
				.append("\n****** PKG_LISTAS.P_GET_PARAMS_COTIZACION ******")
				.append("\n************************************************")
				.toString()
				);
		return listaAux.get(0);
	}
	
	protected class ObtenerParametrosCotizacion extends StoredProcedure
	{
		protected ObtenerParametrosCotizacion(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_PARAMS_COTIZACION");
			declareParameter(new SqlParameter("parametro" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clave4"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clave5"    , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"P1CLAVE"
					,"P1VALOR"
					,"P2CLAVE"
					,"P2VALOR"
					,"P3CLAVE"
					,"P3VALOR"
					,"P4CLAVE"
					,"P4VALOR"
					,"P5CLAVE"
					,"P5VALOR"
					,"P6CLAVE"
					,"P6VALOR"
					,"P7CLAVE"
					,"P7VALOR"
					,"P8CLAVE"
					,"P8VALOR"
					,"P9CLAVE"
					,"P9VALOR"
					,"P10CLAVE"
					,"P10VALOR"
					,"P11CLAVE"
					,"P11VALOR"
					,"P12CLAVE"
					,"P12VALOR"
					,"P13CLAVE"
					,"P13VALOR"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> cargarAutoPorClaveGS(String cdramo,String clavegs,String cdtipsit,String cdsisrol,String tipoUnidad)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("clavegs"  , clavegs);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdsisrol" , cdsisrol);
		params.put("tipounidad" , tipoUnidad);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_CONSULTA.P_GET_VEHICULOS ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		Map<String,Object>procedureResult=ejecutaSP(new CargarAutoPorClaveGS(getDataSource()),params);
		List<Map<String,String>>listaAux=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new Exception("Auto no encontrado");
		}
		if(listaAux.size()>1)
		{
			logger.debug(
					new StringBuilder()
					.append("\n******************************************")
					.append("\n****** PKG_CONSULTA.P_GET_VEHICULOS ******")
					.append("\n****** lista=").append(listaAux)
					.append("\n******************************************")
					.toString()
					);
			//throw new Exception("Auto duplicado");
		}
		return listaAux.get(0);
	}
	
	protected class CargarAutoPorClaveGS extends StoredProcedure
	{
		protected CargarAutoPorClaveGS(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_VEHICULOS");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clavegs"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipounidad" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"TIPUNI"
					,"MARCA"
					,"SUBMARCA"
					,"CLAVEGS"
					,"MODELO"
					,"VERSION"
					,"NUMPASAJEROS"
					,"PASAJMIN"
					,"PASAJMAX"
					,"SUMASEG"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> cargarClaveGSPorAuto(Map<String,String>params)throws Exception
	{
		Map<String,Object>procedureResult=ejecutaSP(new CargarClaveGSPorAuto(getDataSource()),params);
		List<Map<String,String>>listaAux=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new Exception("Clave GS no encontrada");
		}
		if(listaAux.size()>1)
		{
			logger.debug("lista: "+listaAux);
			//throw new Exception("Clave GS duplicada");
		}
		return listaAux.get(0);
	}
	
	protected class CargarClaveGSPorAuto extends StoredProcedure
	{
		protected CargarClaveGSPorAuto(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_VEHICULOS_X_MODELO");
			declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CLAVEGS" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
    public Map<String,String>cargarSumaAseguradaAuto(Map<String,String>params)throws Exception
    {
		Map<String,Object>procedureResult=ejecutaSP(new CargarSumaAseguradaAuto(getDataSource()),params);
		List<Map<String,String>>listaAux=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new Exception("Suma asegurada no encontrada");
		}
		if(listaAux.size()>1)
		{
			throw new Exception("Suma asegurada duplicada");
		}
		return listaAux.get(0);
	}
	
	protected class CargarSumaAseguradaAuto extends StoredProcedure
	{
		protected CargarSumaAseguradaAuto(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_SUMA_ASEGURADA_AUTOS");
			declareParameter(new SqlParameter("version"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"SUMASEG" , "FACREDUC" , "FACINCREM"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoMpolicotICD(Map<String,String>params)throws Exception
	{
		ejecutaSP(new MovimientoMpolicotICD(getDataSource()),params);
	}
	
	protected class MovimientoMpolicotICD extends StoredProcedure
	{
		protected MovimientoMpolicotICD(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_MPOLICOTICD");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdclausu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("icd"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarMpolicotICD(Map<String,String>params)throws Exception
	{
		Map<String,Object>procedureResult=ejecutaSP(new CargarMpolicotICD(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class CargarMpolicotICD extends StoredProcedure
	{
		protected CargarMpolicotICD(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_MPOLICOTICD");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdclausu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"ICD" , "DESCRIPCION"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarConfiguracionGrupo(Map<String,String>params)throws Exception
	{
		Map<String,Object>procedureResult=ejecutaSP(new CargarConfiguracionGrupo(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class CargarConfiguracionGrupo extends StoredProcedure
	{
		protected CargarConfiguracionGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_CONF_GRUPO");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NOMBRE","TIPO","BASE","CDATRISIT","TIPOGAR","CDGARANT","CDATRIGAR","ORDEN","ETIQUETA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<ComponenteVO>cargarTatrisit(String cdtipsit,String cdusuari)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("cdusuari" , cdusuari);
		Utils.debugProcedure(logger, "PKG_LISTAS.P_GET_ATRI_SITUACION", params);
		Map<String,Object>procResult = ejecutaSP(new CargarTatrisit(getDataSource()),params);
		List<ComponenteVO>lista      = (List<ComponenteVO>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No hay tatrisit");
		}
		Utils.debugProcedure(logger, "PKG_LISTAS.P_GET_ATRI_SITUACION", params,lista);
		return lista;
	}
	
	protected class CargarTatrisit extends StoredProcedure
    {
    	protected CargarTatrisit(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_SITUACION");
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatrisitMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<ComponenteVO>cargarTatripol(String cdramo,String cdtipsit,String cdtippol)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdtippol" , cdtippol);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_LISTAS.P_GET_ATRI_POLIZA ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		Map<String,Object>procResult = ejecutaSP(new CargarTatripol(getDataSource()),params);
		List<ComponenteVO>lista      = (List<ComponenteVO>)procResult.get("pv_registro_o");
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_LISTAS.P_GET_ATRI_POLIZA ******")
				.append("\n****** params=").append(params)
				.append("\n****** registro=").append(lista)
				.append("\n******************************************")
				.toString()
				);
		return lista;
	}
	
	protected class CargarTatripol extends StoredProcedure
    {
    	protected CargarTatripol(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_POLIZA");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtippol" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatripolMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public ComponenteVO cargarComponenteTatrisit(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_LISTAS.P_GET_ATRI_UNICO_SITUACION ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString()
				);
		Map<String,Object>procedureResult=ejecutaSP(new CargarComponenteTatrisit(getDataSource()),params);
		List<ComponenteVO>lista=(List<ComponenteVO>)procedureResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe el componente");
		}
		else if(lista.size()>1)
		{
			throw new Exception("Componente repetido");
		}
		return lista.get(0);
	}
	
	protected class CargarComponenteTatrisit extends StoredProcedure
    {
    	protected CargarComponenteTatrisit(DataSource dataSource)
        {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_UNICO_SITUACION");
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatrisitMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public ComponenteVO cargarComponenteTatrigar(Map<String,String>params)throws Exception
	{
		Map<String,Object>procedureResult=ejecutaSP(new CargarComponenteTatrigar(getDataSource()),params);
		List<ComponenteVO>lista=(List<ComponenteVO>)procedureResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe el componente");
		}
		else if(lista.size()>1)
		{
			throw new Exception("Componente repetido");
		}
		return lista.get(0);
	}

	protected class CargarComponenteTatrigar extends StoredProcedure
	{
		protected CargarComponenteTatrigar(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_ATRI_UNICO_GARANTIA");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgarant" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatrigarMapper()));
			declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void validarDescuentoAgente(
			String tipoUnidad
			,String uso
			,String zona
			,String promotoria
			,String cdagente
			,String descuento)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("tipoUnidad" , tipoUnidad);
		params.put("uso"        , uso);
		params.put("zona"       , zona);
		params.put("promotoria" , promotoria);
		params.put("cdagente"   , cdagente);
		params.put("descuento"  , descuento);
		ejecutaSP(new ValidarDescuentoAgente(getDataSource()),params);
	}
	
	protected class ValidarDescuentoAgente extends StoredProcedure
	{
		protected ValidarDescuentoAgente(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_VALIDA_DESCUENTO_COMERCIAL");
			declareParameter(new SqlParameter("tipoUnidad" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("uso"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("zona"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("promotoria" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("descuento"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	/*
	@Override
	public List<Map<String,String>>impresionDocumentosPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String ntramite)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("ntramite" , ntramite);
		Map<String,Object>procedureResult=ejecutaSP(new ImpresionDocumentosPoliza(getDataSource()),params);
		List<Map<String,String>>listaDocumentos=(List<Map<String,String>>)procedureResult.get("pv_registro_o");
		if(listaDocumentos==null||listaDocumentos.size()==0)
		{
			throw new Exception("No hay documentos parametrizados");
		}
		logger.debug(new StringBuilder("\n&&&&&& Lista documentos size=").append(listaDocumentos.size()).toString());
		return listaDocumentos;
	}
	
	protected class ImpresionDocumentosPoliza extends StoredProcedure
	{
		protected ImpresionDocumentosPoliza(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_Imp_documentos");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"nmsolici"
					,"nmsituac"
					,"descripc"
					,"descripl"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	*/
	
	@Override
	public void movimientoTdescsup(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nsuplogi
			,String cdtipsup
			,Date feemisio
			,String nmsolici
			,Date fesolici
			,Date ferefere
			,String cdseqpol
			,String cdusuari
			,String nusuasus
			,String nlogisus
			,String cdperson
			,String accion)throws Exception
	{
		Map<String,Object>params=new LinkedHashMap<String,Object>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nsuplogi" , nsuplogi);
		params.put("cdtipsup" , cdtipsup);
		params.put("feemisio" , feemisio);
		params.put("nmsolici" , nmsolici);
		params.put("fesolici" , fesolici);
		params.put("ferefere" , ferefere);
		params.put("cdseqpol" , cdseqpol);
		params.put("cdusuari" , cdusuari);
		params.put("nusuasus" , nusuasus);
		params.put("nlogisus" , nlogisus);
		params.put("cdperson" , cdperson);
		params.put("accion"   , accion);
		ejecutaSP(new MovimientoTdescsup(getDataSource()),params);
	}
	
	protected class MovimientoTdescsup extends StoredProcedure
	{
		protected MovimientoTdescsup(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_MOV_TDESCSUP");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nsuplogi" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feemisio" , OracleTypes.DATE));
			declareParameter(new SqlParameter("nmsolici" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fesolici" , OracleTypes.DATE));
			declareParameter(new SqlParameter("ferefere" , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdseqpol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nusuasus" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nlogisus" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public DatosUsuario cargarInformacionUsuario(String cdusuari,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("cdtipsit" , cdtipsit);
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************")
				.append("\n****** pkg_satelites.p_get_info_usuario ******")
				.append("\n****** params=").append(params)
				.append("\n**********************************************")
				.toString()
				);
		Map<String,Object>procResult = ejecutaSP(new CargarInformacionUsuario(getDataSource()),params);
		List<DatosUsuario>listaAux   = (List<DatosUsuario>)procResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new ApplicationException("No hay datos de usuario");
		}
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************")
				.append("\n****** registro=").append(listaAux.get(0))
				.append("\n****** pkg_satelites.p_get_info_usuario ******")
				.append("\n**********************************************")
				.toString()
				);
		return listaAux.get(0);
	}
	
	protected class CargarInformacionUsuario extends StoredProcedure
    {
    	protected CargarInformacionUsuario(DataSource dataSource)
        {
            super(dataSource,"pkg_satelites.p_get_info_usuario");
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneDatosUsuarioMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    protected class ObtieneDatosUsuarioMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            DatosUsuario datosUsuario=new DatosUsuario();
            datosUsuario.setCdagente(rs.getString("cdagente"));
            datosUsuario.setCdperson(rs.getString("cdperson"));
            datosUsuario.setCdramo  (rs.getString("cdramo"));
            datosUsuario.setCdtipsit(rs.getString("cdtipsit"));
            datosUsuario.setCdunieco(rs.getString("cdunieco"));
            datosUsuario.setCdusuari(rs.getString("cdusuari"));
            datosUsuario.setNmcuadro(rs.getString("nmcuadro"));
            datosUsuario.setNombre  (rs.getString("nombre"));
            return datosUsuario;
        }
    }
    
    @Override
    public Map<String,String>cargarClienteCotizacion(String cdunieco,String cdramo,String estado,String nmpoliza)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	logger.debug(
    			new StringBuilder()
    			.append("\n***************************************************")
    			.append("\n****** PKG_CONSULTA.P_GET_CLIENTE_COTIZACION ******")
    			.append("\n****** params=").append(params)
    			.append("\n***************************************************")
    			.toString()
    			);
    	Map<String,Object>procResult=ejecutaSP(new CargarClienteCotizacion(getDataSource()),params);
    	List<Map<String,String>>lista=(List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new Exception("No se encontraron datos");
    	}
    	if(lista.size()>1)
    	{
    		throw new Exception("Datos duplicados");
    	}
    	return lista.get(0);
    }
	
	protected class CargarClienteCotizacion extends StoredProcedure
    {
    	protected CargarClienteCotizacion(DataSource dataSource)
        {
            super(dataSource,"PKG_CONSULTA.P_GET_CLIENTE_COTIZACION");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols=new String[]
            		{
            		"NOMBRE"
            		,"CDPERSON"
            		};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public Map<String,String>cargarConceptosGlobalesGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdperpag)throws Exception,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdperpag" , cdperpag);
		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** PKG_COTIZA.P_OBT_CPTOS_GLOBALES_COLECT ******")
				.append("\n****** params=").append(params)
				.append("\n****************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarConceptosGlobalesGrupo(getDataSource()),params);
		List<Map<String,String>>listaAux=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(listaAux==null||listaAux.size()==0)
		{
			throw new Exception("No se encontraron conceptos globales");
		}
		if(listaAux.size()>1)
		{
			throw new Exception("Se encontraron conceptos globales repetidos");
		}
		return listaAux.get(0);
	}
	
	protected class CargarConceptosGlobalesGrupo extends StoredProcedure
    {
    	protected CargarConceptosGlobalesGrupo(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_OBT_CPTOS_GLOBALES_COLECT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
            String[] cols=new String[]
            		{
            		"PRIMA_NETA"
            		,"DERPOL"
            		,"RECARGOS"
            		,"IVA"
            		};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public String calculaNumeroPoliza(String cdunieco,String cdramo,String estado)throws Exception,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		logger.debug(
				new StringBuilder()
				.append("\n********************************************")
				.append("\n****** PKG_SATELITES.P_CALC_NUMPOLIZA ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CalculaNumeroPoliza(getDataSource()),params);
		String nmpoliza = (String)procResult.get("pv_nmpoliza_o");
		if(StringUtils.isBlank(nmpoliza))
		{
			throw new Exception("No se puede calcular el numero de poliza");
		}
		return nmpoliza;
	}
	
	protected class CalculaNumeroPoliza extends StoredProcedure
    {
    	protected CalculaNumeroPoliza(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES.P_CALC_NUMPOLIZA");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmpoliza_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public void movimientoPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String status
			,String swestado
			,String nmsolici
            ,String feautori
            ,String cdmotanu
            ,String feanulac
            ,String swautori
            ,String cdmoneda
            ,String feinisus
            ,String fefinsus
            ,String ottempot
            ,String feini
            ,String hhefecto
            ,String fefin
            ,String fevencim
            ,String nmrenova
            ,String ferecibo
            ,String feultsin
            ,String nmnumsin
            ,String cdtipcoa
            ,String swtarifi
            ,String swabrido
            ,String feemisio
            ,String cdperpag
            ,String nmpoliex
            ,String nmcuadro
            ,String porredau
            ,String swconsol
            ,String nmpolant
            ,String nmpolnva
            ,String fesolici
            ,String cdramant
            ,String cdmejred
            ,String nmpoldoc
            ,String nmpoliza2
            ,String nmrenove
            ,String nmsuplee
            ,String ttipcamc
            ,String ttipcamv
            ,String swpatent
            ,String pcpgocte
            ,String tipoflot
            ,String agrupador
            ,String accion
			) throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco"  , cdunieco);
		params.put("cdramo"    , cdramo);
		params.put("estado"    , estado);
		params.put("nmpoliza"  , nmpoliza);
		params.put("nmsuplem"  , nmsuplem);
		params.put("status"    , status);
		params.put("swestado"  , swestado);
		params.put("nmsolici"  , nmsolici);
        params.put("feautori"  , feautori);
        params.put("cdmotanu"  , cdmotanu);
        params.put("feanulac"  , feanulac);
        params.put("swautori"  , swautori);
        params.put("cdmoneda"  , cdmoneda);
        params.put("feinisus"  , feinisus);
        params.put("fefinsus"  , fefinsus);
        params.put("ottempot"  , ottempot);
        params.put("feefecto"  , feini);
        params.put("hhefecto"  , hhefecto);
        params.put("feproren"  , fefin);
        params.put("fevencim"  , fevencim);
        params.put("nmrenova"  , nmrenova);
        params.put("ferecibo"  , ferecibo);
        params.put("feultsin"  , feultsin);
        params.put("nmnumsin"  , nmnumsin);
        params.put("cdtipcoa"  , cdtipcoa);
        params.put("swtarifi"  , swtarifi);
        params.put("swabrido"  , swabrido);
        params.put("feemisio"  , feemisio);
        params.put("cdperpag"  , cdperpag);
        params.put("nmpoliex"  , nmpoliex);
        params.put("nmcuadro"  , nmcuadro);
        params.put("porredau"  , porredau);
        params.put("swconsol"  , swconsol);
        params.put("nmpolant"  , nmpolant);
        params.put("nmpolnva"  , nmpolnva);
        params.put("fesolici"  , fesolici);
        params.put("cdramant"  , cdramant);
        params.put("cdmejred"  , cdmejred);
        params.put("nmpoldoc"  , nmpoldoc);
        params.put("nmpoliza2" , nmpoliza2);
        params.put("nmrenove"  , nmrenove);
        params.put("nmsuplee"  , nmsuplee);
        params.put("ttipcamc"  , ttipcamc);
        params.put("ttipcamv"  , ttipcamv);
        params.put("swpatent"  , swpatent);
        params.put("pcpgocte"  , pcpgocte);
        params.put("tipoflot"  , tipoflot);
        params.put("agrupador" , agrupador);
        params.put("accion"    , accion);
        logger.debug(
        		new StringBuilder()
        		.append("\n*******************************************")
        		.append("\n****** PKG_SATELITES2.P_MOV_MPOLIZAS ******")
        		.append("\n****** params=").append(params)
        		.append("\n*******************************************")
        		.toString()
        		);
        ejecutaSP(new MovimientoPoliza(getDataSource()),params);
	}
	
	protected class MovimientoPoliza extends StoredProcedure
	{
		protected MovimientoPoliza(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES2.P_MOV_MPOLIZAS");
    		declareParameter(new SqlParameter("cdunieco"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("cdramo"    , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("status"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("swestado"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsolici"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("feautori"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdmotanu"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("feanulac"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("swautori"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdmoneda"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("feinisus"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("fefinsus"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("ottempot"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("feefecto"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("hhefecto"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("feproren"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("fevencim"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmrenova"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("ferecibo"  ,  OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("feultsin"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmnumsin"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("cdtipcoa"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("swtarifi"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("swabrido"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("feemisio"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdperpag"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("nmpoliex"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmcuadro"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("porredau"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("swconsol"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpolant"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpolnva"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("fesolici"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramant"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdmejred"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoldoc"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("nmpoliza2" , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("nmrenove"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("nmsuplee"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("ttipcamc"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("ttipcamv"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("swpatent"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pcpgocte"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("tipoflot"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("agrupador" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));	
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void movimientoTvalopol(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String status
			,Map<String,String>valores)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("status"   , status);
		for(int i=1;i<=50;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i), 2, "0")).toString(),null);
		}
		params.putAll(valores);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES.P_MOV_TVALOPOL ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new MovimientoTvalopol(getDataSource()),params);
	}
	
	protected class MovimientoTvalopol extends StoredProcedure
	{
		protected MovimientoTvalopol(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES.P_MOV_TVALOPOL");	
    		declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("status"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor01" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor02" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor03" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor04" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor05" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor06" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor07" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor08" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor09" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor10" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor11" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor12" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor13" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor14" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor15" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor16" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor17" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor18" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor19" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor20" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor21" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor22" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor23" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor24" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor25" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor26" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor27" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor28" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor29" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor30" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor31" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor32" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor33" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor34" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor35" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor36" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor37" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor38" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor39" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor40" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor41" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor42" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor43" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor44" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor45" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor46" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor47" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor48" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor49" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("otvalor50" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void procesarCenso(
			String nombreProcedure
			,String cdusuari
			,String cdsisrol
			,String nombreCenso
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsit
			,String cdagente
			,String codpostal
			,String cdedo
			,String cdmunici
			,String complemento
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdusuari"    , cdusuari);
		params.put("cdsisrol"    , cdsisrol);
		params.put("nombreCenso" , nombreCenso);
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("cdtipsit"    , cdtipsit);
		params.put("cdagente"    , cdagente);
		params.put("codpostal"   , codpostal);
		params.put("cdedo"       , cdedo);
		params.put("cdmunici"    , cdmunici);
		params.put("complemento" , complemento);
		logger.debug(
				new StringBuilder()
				.append("\n********************************************")
				.append("\n****** EJECUTA PROCEDIMIENTO DE CENSO ******")
				.append("\n****** procedimiento=").append(nombreProcedure)
				.append("\n****** params=").append(params)
				.append("\n********************************************")
				.toString()
				);
		ejecutaSP(new ProcesarCenso(getDataSource(),nombreProcedure),params);
	}
	
	protected class ProcesarCenso extends StoredProcedure
	{
		protected ProcesarCenso(DataSource dataSource,String nombreProcedure)
		{
    		super(dataSource,nombreProcedure);	
    		declareParameter(new SqlParameter("cdusuari"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdsisrol"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nombreCenso" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdtipsit"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdagente"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("codpostal"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdedo"       , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdmunici"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("complemento" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void actualizaMpolisitTvalositGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo
			,String nombreGrupo
			,String cdplan
			,Map<String,String>valores)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("cdgrupo"     , cdgrupo);
		params.put("nombreGrupo" , nombreGrupo);
		params.put("cdplan"      , cdplan);
		
		for(int i=1;i<=99;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString(),null);
		}
		
		params.putAll(valores);
		
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************************")
				.append("\n****** PKG_SATELITES2.P_ACT_MPOLISIT_TVALOSIT_DINAM ******")
				.append("\n****** params=").append(params)
				.append("\n**********************************************************")
				.toString()
				);
		ejecutaSP(new ActualizaMpolisitTvalositGrupo(getDataSource()),params);
	}
	
	protected class ActualizaMpolisitTvalositGrupo extends StoredProcedure
	{
		protected ActualizaMpolisitTvalositGrupo(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES2.P_ACT_MPOLISIT_TVALOSIT_DINAM");	
    		declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdgrupo"     , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nombreGrupo" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdplan"      , OracleTypes.VARCHAR));
    		for(int i=1;i<=99;i++)
    		{
    			declareParameter(new SqlParameter(
    					new StringBuilder("otvalor")
    					.append(StringUtils.leftPad(String.valueOf(i),2,"0"))
    					.toString()
    					, OracleTypes.VARCHAR));
    		}
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void valoresPorDefecto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdgarant
			,String cdtipsup)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgarant" , cdgarant);
		params.put("cdtipsup" , cdtipsup);
		logger.debug(
				new StringBuilder()
				.append("\n*****************************")
				.append("\n****** P_EXEC_SIGSVDEF ******")
				.append("\n****** params=").append(params)
				.append("\n*****************************")
				.toString()
				);
		ejecutaSP(new ValoresPorDefecto(getDataSource()),params);
	}
	
	protected class ValoresPorDefecto extends StoredProcedure
	{
		protected ValoresPorDefecto(DataSource dataSource)
		{
    		super(dataSource,"P_EXEC_SIGSVDEF");	
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdgarant" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void movimientoMpoliper(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrol
			,String cdperson
			,String nmsuplem
			,String status
			,String nmorddom
			,String swreclam
			,String accion
			,String swexiper)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("cdrol"    , cdrol);
		params.put("cdperson" , cdperson);
		params.put("nmsuplem" , nmsuplem);
		params.put("status"   , status);
		params.put("nmorddom" , nmorddom);
		params.put("swreclam" , swreclam);
		params.put("accion"   , accion);
		params.put("swexiper" , swexiper);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES.P_MOV_MPOLIPER ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new MovimientoMpoliper(getDataSource()),params);
	}
	
	protected class MovimientoMpoliper extends StoredProcedure
	{
		protected MovimientoMpoliper(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES.P_MOV_MPOLIPER");	
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdrol"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmorddom" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("swreclam" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("swexiper" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void movimientoMpoliage(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdagente
			,String nmsuplem
			,String status
			,String cdtipoag
			,String porredau
			,String nmcuadro
			,String cdsucurs
			,String accion
			,String ntramite
			,String porparti)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("cdagente" , cdagente);
		params.put("nmsuplem" , nmsuplem);
		params.put("status"   , status);
		params.put("cdtipoag" , cdtipoag);
		params.put("porredau" , porredau);
		params.put("nmcuadro" , nmcuadro);
		params.put("cdsucurs" , cdsucurs);
		params.put("accion"   , accion);
		params.put("ntramite" , ntramite);
		params.put("porparti" , porparti);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES.P_MOV_MPOLIAGE ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new MovimientoMpoliage(getDataSource()),params);
	}
	
	protected class MovimientoMpoliage extends StoredProcedure
	{
		protected MovimientoMpoliage(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES.P_MOV_MPOLIAGE");	
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipoag" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("porredau" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmcuadro" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsucurs" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("porparti" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void tarificaEmi(
			String cdusuari
			,String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("cdelemen" , cdelemen);
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdtipsit" , cdtipsit);
		logger.debug(
				new StringBuilder()
				.append("\n**************************************************")
				.append("\n****** PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_EMI ******")
				.append("\n****** params=").append(params)
				.append("\n**************************************************")
				.toString()
				);
		ejecutaSP(new TarificaEmi(getDataSource()),params);
	}
	
	protected class TarificaEmi extends StoredProcedure
	{
		protected TarificaEmi(DataSource dataSource)
		{
			super(dataSource, "PKG_COTIZA.P_EJECUTA_SIGSVALIPOL_EMI");	
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdelemen" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>obtenerTiposSituacion()throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		logger.debug(
				new StringBuilder()
				.append("\n************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TIPOS_SITUACION ******")
				.append("\n****** sin parametros")
				.append("\n************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new ObtenerTiposSituacion(getDataSource()),params);
		List<Map<String,String>>lista=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No hay tipos de situacion");
		}
		return lista;
	}
	
	protected class ObtenerTiposSituacion extends StoredProcedure
	{
		protected ObtenerTiposSituacion(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_GET_TIPOS_SITUACION");
			String[] cols=new String[]
					{
					"CDRAMO"
					,"CDTIPSIT"
					,"SITUACION"
					,"AGRUPACION"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarSituacionesGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO_DINAM ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarSituacionesGrupo(getDataSource()),params);
		List<Map<String,String>>situaciones=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(situaciones==null||situaciones.size()==0)
		{
			throw new Exception("No hay situaciones para el grupo");
		}
		return situaciones;
	}
	
	protected class CargarSituacionesGrupo extends StoredProcedure
	{
		protected CargarSituacionesGrupo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO_DINAM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"nombre"
					,"nmsituac"
					,"familia"
					,"titular"
					,"parentesco"
					            ,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09"
					,"otvalor10","otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19"
					,"otvalor20","otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29"
					,"otvalor30","otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39"
					,"otvalor40","otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49"
					,"otvalor50","otvalor51","otvalor52","otvalor53","otvalor54","otvalor55","otvalor56","otvalor57","otvalor58","otvalor59"
					,"otvalor60","otvalor61","otvalor62","otvalor63","otvalor64","otvalor65","otvalor66","otvalor67","otvalor68","otvalor69"
					,"otvalor70","otvalor71","otvalor72","otvalor73","otvalor74","otvalor75","otvalor76","otvalor77","otvalor78","otvalor79"
					,"otvalor80","otvalor81","otvalor82","otvalor83","otvalor84","otvalor85","otvalor86","otvalor87","otvalor88","otvalor89"
					,"otvalor90","otvalor91","otvalor92","otvalor93","otvalor94","otvalor95","otvalor96","otvalor97","otvalor98","otvalor99"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaValoresSituacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,Map<String,String>valores)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		
		for(int i=1;i<=99;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString(),null);
		}
		
		params.putAll(valores);
		
		logger.debug(
				new StringBuilder()
				.append("\n******************************************************")
				.append("\n****** PKG_SATELITES.P_ACTUALIZA_TVALOSIT_DINAM ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************************")
				.toString()
				);
		ejecutaSP(new ActualizaValoresSituacion(getDataSource()),params);
	}
	
	protected class ActualizaValoresSituacion extends StoredProcedure
	{
		protected ActualizaValoresSituacion(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_ACTUALIZA_TVALOSIT_DINAM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			for(int i=1;i<=99;i++)
			{
				declareParameter(
						new SqlParameter(
								new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString()
								,OracleTypes.VARCHAR
								)
				);
			}
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void validarCambioZonaGMI(
			String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String codpostal)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco"  , cdunieco);
		params.put("cdramo"    , cdramo);
		params.put("cdtipsit"  , cdtipsit);
		params.put("estado"    , estado);
		params.put("nmpoliza"  , nmpoliza);
		params.put("nmsuplem"  , nmsuplem);
		params.put("nmsituac"  , nmsituac);
		params.put("codpostal" , codpostal);
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************************")
				.append("\n****** PKG_SATELITES2.P_VAL_ELIM_PENAL_CAM_ZONA_GMM ******")
				.append("\n****** params=").append(params)
				.append("\n**********************************************************")
				.toString()
				);
		ejecutaSP(new ValidarCambioZonaGMI(getDataSource()),params);
	}
	
	protected class ValidarCambioZonaGMI extends StoredProcedure
	{
		protected ValidarCambioZonaGMI(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_VAL_ELIM_PENAL_CAM_ZONA_GMM");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("codpostal" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void validarEnfermedadCatastGMI(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String circHosp)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		params.put("circHosp" , circHosp);
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************************")
				.append("\n****** PKG_SATELITES2.P_VAL_COB_ENFERM_CATASTROF_GMM ******")
				.append("\n****** params=").append(params)
				.append("\n***********************************************************")
				.toString()
				);
		ejecutaSP(new ValidarEnfermedadCatastGMI(getDataSource()),params);
	}
	
	protected class ValidarEnfermedadCatastGMI extends StoredProcedure
	{
		protected ValidarEnfermedadCatastGMI(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_VAL_COB_ENFERM_CATASTROF_GMM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("circHosp" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarRetroactividadSuplemento(
			String cdunieco
			,String cdramo
			,String cdtipsup
			,String cdusuari
			,String cdtipsit)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("cdtipsup" , cdtipsup);
		params.put("cdusuari" , cdusuari);
		params.put("cdtipsit" , cdtipsit);
		logger.debug(
				new StringBuilder()
				.append("\n*********************************************")
				.append("\n****** PKG_SATELITES.P_OBT_RETRO_DIFER ******")
				.append("\n****** params=").append(params)
				.append("\n*********************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarRetroactividadSuplemento(getDataSource()),params);
		List<?>lista = (List<?>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("Falta parametrizar la retroactividad");
		}
		if(lista.size()>1)
		{
			throw new ApplicationException("La retroactividad parametrizada se repite");
		}
		Map<?,?>retroactividad=(Map<?,?>)lista.get(0);
		logger.debug(
				new StringBuilder()
				.append("\n*********************************************")
				.append("\n****** salida=").append(retroactividad)
				.append("\n****** PKG_SATELITES.P_OBT_RETRO_DIFER ******")
				.append("\n*********************************************")
				.toString()
				);
		Map<String,String>aux=new LinkedHashMap<String,String>();
		for(Entry<?,?>en:retroactividad.entrySet())
		{
			aux.put((String)en.getKey(),(String)en.getValue());
		}
		return aux;
	}
	
	protected class CargarRetroactividadSuplemento extends StoredProcedure
	{
		protected CargarRetroactividadSuplemento(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_OBT_RETRO_DIFER");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"retroac"
					,"diferi"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarSumaAseguradaRamo5(
			String cdtipsit
			,String clave
			,String modelo
			,String cdsisrol)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("clave"    , clave);
		params.put("modelo"   , modelo);
		params.put("cdsisrol" , cdsisrol);
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_SUMA_ASEG_RAMO_5 ******")
				.append("\n****** params=").append(params)
				.append("\n*************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarSumaAseguradaRamo5(getDataSource()),params);
		logger.debug(new StringBuilder()
				.append("\n****** VILS DEBBUG SUMAASEG ******")
				.append("\n****** params=").append(procResult)
				.append("\n*************************************************")
				.toString()
				);
		List<Map<String,String>>lista=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay suma asegurada para el auto");
		}
		else if(lista.size()>1)
		{
			throw new ApplicationException("Suma asegurada duplicada para el auto");
		}
		Map<String,String>mapa=lista.get(0);
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** registro=").append(mapa)
				.append("\n****** PKG_CONSULTA.P_GET_SUMA_ASEG_RAMO_5 ******")
				.append("\n*************************************************")
				.toString()
				);
		return mapa;
	}
	
	protected class CargarSumaAseguradaRamo5 extends StoredProcedure
	{
		protected CargarSumaAseguradaRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_SUMA_ASEG_RAMO_5");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clave"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"sumaseg"
					,"facreduc"
					,"facincrem"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarDatosComplementariosAutoInd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_COMP_AUTO_IND ******")
				.append("\n****** params=").append(params)
				.append("\n****************************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarDatosComplementariosAutoInd(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay datos de poliza");
		}
		if(lista.size()>1)
		{
			throw new ApplicationException("Datos de poliza repetidos");
		}
		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** params=").append(params)
				.append("\n****** registro=").append(lista.get(0))
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_COMP_AUTO_IND ******")
				.append("\n****************************************************")
				.toString()
				);
		return lista.get(0);
	}
	
	protected class CargarDatosComplementariosAutoInd extends StoredProcedure
	{
		protected CargarDatosComplementariosAutoInd(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_COMP_AUTO_IND");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					    "cdunieco"
					    ,"cdramo"
					    ,"estado"
					    ,"nmpoliza"
					    ,"cdperpag"
					    ,"cdplan"
					    ,"feini"
					    ,"fefin"
					    ,"cdperson"
					    ,"cdideper"
					    ,"cdideext"
					    ,"swexiper"
					    ,"agente_sec"
					    ,"porparti"
					    ,"prima_total"
					    ,"cdpostal"
					    ,"otfisjur"
					    ,"nmorddom"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarTvalopol(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n***************************************")
				.append("\n****** PKG_COTIZA.P_GET_TVALOPOL ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarTvalopol(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		Map<String,String>datos       = new LinkedHashMap<String,String>();
		if(lista!=null&&lista.size()>0)
		{
			datos=lista.get(0);
		}
		Map<String,String>aux=new LinkedHashMap<String,String>();
		for(Entry<String,String>en:datos.entrySet())
		{
			aux.put(new StringBuilder("parametros.pv_").append(en.getKey()).toString(),en.getValue());
		}
		datos=aux;
		logger.debug(
				new StringBuilder()
				.append("\n***************************************")
				.append("\n****** params=").append(params)
				.append("\n****** registro=").append(datos)
				.append("\n****** PKG_COTIZA.P_GET_TVALOPOL ******")
				.append("\n***************************************")
				.toString()
				);
		return datos;
	}
	
	protected class CargarTvalopol extends StoredProcedure
	{
		protected CargarTvalopol(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GET_TVALOPOL");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					    "cdunieco"
					    ,"cdramo"
					    ,"estado"
					    ,"nmpoliza"
					    ,"nmsuplem"
					    ,"status"
					                ,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09"
					    ,"otvalor10","otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19"
					    ,"otvalor20","otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29"
					    ,"otvalor30","otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39"
					    ,"otvalor40","otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49"
					    ,"otvalor50"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("nmpoliza" , nmpoliza);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmsituac" , nmsituac);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************")
				.append("\n****** PKG_COTIZA.P_OBTIENE_TVALOSIT ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarTvalosit(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay valores de situacion"); 
		}
		if(lista.size()>1)
		{
			throw new ApplicationException("Valores de situacion duplicados");
		}
		Map<String,String>datos = lista.get(0);
		Map<String,String>aux   = new LinkedHashMap<String,String>();
		for(Entry<String,String>en:datos.entrySet())
		{
			aux.put(new StringBuilder("parametros.pv_").append(en.getKey()).toString(),en.getValue());
		}
		datos=aux;
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************")
				.append("\n****** params=").append(params)
				.append("\n****** registro=").append(datos)
				.append("\n****** PKG_COTIZA.P_OBTIENE_TVALOSIT ******")
				.append("\n*******************************************")
				.toString()
				);
		return datos;
	}
	
	protected class CargarTvalosit extends StoredProcedure
	{
		protected CargarTvalosit(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_OBTIENE_TVALOSIT");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					    "cdtipsit"
					    ,"nmsuplem"
					    ,"status"
					                ,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09"
					    ,"otvalor10","otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19"
					    ,"otvalor20","otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29"
					    ,"otvalor30","otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39"
					    ,"otvalor40","otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49"
					    ,"otvalor50","otvalor51","otvalor52","otvalor53","otvalor54","otvalor55","otvalor56","otvalor57","otvalor58","otvalor59"
					    ,"otvalor60","otvalor61","otvalor62","otvalor63","otvalor64","otvalor65","otvalor66","otvalor67","otvalor68","otvalor69"
					    ,"otvalor70","otvalor71","otvalor72","otvalor73","otvalor74","otvalor75","otvalor76","otvalor77","otvalor78","otvalor79"
					    ,"otvalor80","otvalor81","otvalor82","otvalor83","otvalor84","otvalor85","otvalor86","otvalor87","otvalor88","otvalor89"
					    ,"otvalor90","otvalor91","otvalor92","otvalor93","otvalor94","otvalor95","otvalor96","otvalor97","otvalor98","otvalor99"
					    ,"seguroVida"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaMpolizas(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String swestado,
			String nmsolici,
			Date   feautori,
			String cdmotanu,
			Date   feanulac,
			String swautori,
			String cdmoneda,
			Date   feinisus,
			Date   fefinsus,
			String ottempot,
			Date   feefecto,
			String hhefecto,
			Date   feproren,
			Date   fevencim,
			String nmrenova,
			Date   ferecibo,
			Date   feultsin,
			String nmnumsin,
			String cdtipcoa,
			String swtarifi,
			String swabrido,
			Date   feemisio,
			String cdperpag,
			String nmpoliex,
			String nmcuadro,
			String porredau,
			String swconsol,
			String nmpolant,
			String nmpolnva,
			Date   fesolici,
			String cdramant,
			String cdmejred,
			String nmpoldoc,
			String nmpoliza2,
			String nmrenove,
			String nmsuplee,
			String ttipcamc,
			String ttipcamv,
			String swpatent,
			String nmpolmst,
			String pcpgocte)throws Exception
	{
		Map<String,Object>params=new LinkedHashMap<String,Object>();
		params.put("cdunieco"  , cdunieco );
		params.put("cdramo"    , cdramo );
		params.put("estado"    , estado );
		params.put("nmpoliza"  , nmpoliza );
		params.put("nmsuplem"  , nmsuplem );
		params.put("swestado"  , swestado );
		params.put("nmsolici"  , nmsolici );
		params.put("feautori"  , feautori );
		params.put("cdmotanu"  , cdmotanu );
		params.put("feanulac"  , feanulac );
		params.put("swautori"  , swautori );
		params.put("cdmoneda"  , cdmoneda );
		params.put("feinisus"  , feinisus );
		params.put("fefinsus"  , fefinsus );
		params.put("ottempot"  , ottempot );
		params.put("feefecto"  , feefecto );
		params.put("hhefecto"  , hhefecto );
		params.put("feproren"  , feproren );
		params.put("fevencim"  , fevencim );
		params.put("nmrenova"  , nmrenova );
		params.put("ferecibo"  , ferecibo );
		params.put("feultsin"  , feultsin );
		params.put("nmnumsin"  , nmnumsin );
		params.put("cdtipcoa"  , cdtipcoa );
		params.put("swtarifi"  , swtarifi );
		params.put("swabrido"  , swabrido );
		params.put("feemisio"  , feemisio );
		params.put("cdperpag"  , cdperpag );
		params.put("nmpoliex"  , nmpoliex );
		params.put("nmcuadro"  , nmcuadro );
		params.put("porredau"  , porredau );
		params.put("swconsol"  , swconsol );
		params.put("nmpolant"  , nmpolant );
		params.put("nmpolnva"  , nmpolnva );
		params.put("fesolici"  , fesolici );
		params.put("cdramant"  , cdramant );
		params.put("cdmejred"  , cdmejred );
		params.put("nmpoldoc"  , nmpoldoc );
		params.put("nmpoliza2" , nmpoliza2 );
		params.put("nmrenove"  , nmrenove );
		params.put("nmsuplee"  , nmsuplee );
		params.put("ttipcamc"  , ttipcamc );
		params.put("ttipcamv"  , ttipcamv );
		params.put("swpatent"  , swpatent );
		params.put("nmpolmst"  , nmpolmst );
		params.put("pcpgocte"  , pcpgocte );
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************")
				.append("\n****** PKG_SATELITES2.P_UPDATE_MPOLIZAS ******")
				.append("\n****** params=").append(params)
				.append("\n**********************************************")
				.toString()
				);
		ejecutaSP(new ActualizaMpolizas(getDataSource()),params);
	}
	
	protected class ActualizaMpolizas extends StoredProcedure
	{
		protected ActualizaMpolizas(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_UPDATE_MPOLIZAS");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swestado"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsolici"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feautori"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdmotanu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feanulac"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("swautori"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmoneda"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feinisus"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("fefinsus"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("ottempot"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feefecto"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("hhefecto"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feproren"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("fevencim"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("nmrenova"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ferecibo"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("feultsin"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("nmnumsin"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipcoa"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swtarifi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swabrido"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feemisio"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdperpag"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliex"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmcuadro"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("porredau"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swconsol"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpolant"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpolnva"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fesolici"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdramant"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmejred"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoldoc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmrenove"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplee"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ttipcamc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ttipcamv"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swpatent"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpolmst"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pcpgocte"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void borrarAgentesSecundarios(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco"  , cdunieco );
		params.put("cdramo"    , cdramo );
		params.put("estado"    , estado );
		params.put("nmpoliza"  , nmpoliza );
		params.put("nmsuplem"  , nmsuplem );
		logger.debug(
				new StringBuilder()
				.append("\n********************************************************")
				.append("\n****** PKG_SATELITES2.P_BORRA_AGENTES_SECUNDARIOS ******")
				.append("\n****** params=").append(params)
				.append("\n********************************************************")
				.toString()
				);
		ejecutaSP(new BorrarAgentesSecundarios(getDataSource()),params);
	}
	
	protected class BorrarAgentesSecundarios extends StoredProcedure
	{
		protected BorrarAgentesSecundarios(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_BORRA_AGENTES_SECUNDARIOS");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarConfiguracionCotizacion(
			String cdramo
			,String cdtipsit
			,String cdusuari)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdusuari" , cdusuari);
		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** PKG_SATELITES2.P_GET_CONFIG_COTIZACION ******")
				.append("\n****** params=").append(params)
				.append("\n****************************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarConfiguracionCotizacion(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay configuracion de cotizacion"); 
		}
		Map<String,String>datos = new LinkedHashMap<String,String>();
		for(Map<String,String>valor:lista)
		{
			datos.put(
					new StringBuilder("parametros.pv_otvalor")
					.append(StringUtils.leftPad(valor.get("cdatribu"),2,"0"))
					.toString()
					,valor.get("valor")
					);
		}
		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** params=").append(params)
				.append("\n****** registro=").append(datos)
				.append("\n****** PKG_SATELITES2.P_GET_CONFIG_COTIZACION ******")
				.append("\n****************************************************")
				.toString()
				);
		return datos;
	}
	
	protected class CargarConfiguracionCotizacion extends StoredProcedure
	{
		protected CargarConfiguracionCotizacion(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_CONFIG_COTIZACION");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					    "cdatribu"
					    ,"valor"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarConfiguracionCotizacion(
			String cdramo
			,String cdtipsit
			,String cdusuari
			,Map<String,String>valores)throws Exception
	{
		Map<String,String>params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdusuari" , cdusuari);
		for(int i=1;i<=99;i++)
		{
			params.put(new StringBuilder("valor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString(),null);
		}
		params.putAll(valores);

		logger.debug(
				new StringBuilder()
				.append("\n****************************************************")
				.append("\n****** PKG_SATELITES2.P_MOV_CONFIG_COTIZACION ******")
				.append("\n****** params=").append(params)
				.append("\n****************************************************")
				.toString()
				);
		ejecutaSP(new GuardarConfiguracionCotizacion(getDataSource()),params);
	}
	
	protected class GuardarConfiguracionCotizacion extends StoredProcedure
	{
		protected GuardarConfiguracionCotizacion(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_CONFIG_COTIZACION");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			for(int i=1;i<=99;i++)
			{
				declareParameter(new SqlParameter(
						new StringBuilder("valor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString()
						, OracleTypes.VARCHAR));
			}
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarRangoDescuentoRamo5(
			String cdtipsit
			,String cdagente
			,String negocio
			,String cdsisrol
			,String cdusuari
			)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("cdagente" , cdagente);
		params.put("negocio"  , negocio);
		params.put("cdsisrol" , cdsisrol);
		params.put("cdusuari" , cdusuari);
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_SATELITES2.P_GET_DESC_RECA_RAMO_5 ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarRangoDescuentoRamo5(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay descuento/recargo para el agente");
		}
		if(lista.size()>1)
		{
			throw new ApplicationException("Descuento/recargo para el agente duplicado");
		}
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** registro=").append(lista.get(0))
				.append("\n****** params=")  .append(params)
				.append("\n****** PKG_SATELITES2.P_GET_DESC_RECA_RAMO_5 ******")
				.append("\n***************************************************")
				.toString()
				);
		return lista.get(0);
	}
	
	protected class CargarRangoDescuentoRamo5 extends StoredProcedure
	{
		protected CargarRangoDescuentoRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_DESC_RECA_RAMO_5");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("negocio"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "min" , "max" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<List<Map<String,String>>>cargarParamerizacionConfiguracionCoberturas(
			String cdtipsit
			,String cdsisrol
			,String negocio
			,String tipoServicio
			,String modelo
			,String tipoPersona
			,String submarca
			,String clavegs)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit"     , cdtipsit);
		params.put("cdsisrol"     , cdsisrol);
		params.put("negocio"      , negocio);
		params.put("tipoServicio" , tipoServicio);
		params.put("modelo"       , modelo);
		params.put("tipoPersona"  , tipoPersona);
		params.put("submarca"     , submarca);
		params.put("clavegs"      , clavegs);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_PARAMETROS_CONFIG_AUTO ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>procResult = null;
		List<Map<String,String>>tatrist =new ArrayList<Map<String,String>>();
		List<Map<String,String>>atrixrol=new ArrayList<Map<String,String>>();
		List<Map<String,String>>atrixant=new ArrayList<Map<String,String>>();
		List<Map<String,String>>atrixper=new ArrayList<Map<String,String>>();
		List<Map<String,String>>atrixcam=new ArrayList<Map<String,String>>();
		List<Map<String,String>>atrirang=new ArrayList<Map<String,String>>();
	
		if(cdtipsit.contains("AF") || cdtipsit.contains("PU") ||(submarca.equals("00000") && clavegs.equals("00000")))
		{
			try 
			{
				procResult = ejecutaSP(new CargarParamerizacionConfiguracionCoberturasFronterizos(getDataSource()),params);
			}
			catch(Exception ex)
			{
				procResult=null;
			}
		}
		else
		{
		procResult = ejecutaSP(new CargarParamerizacionConfiguracionCoberturas(getDataSource()),params);
		}
		
		if(procResult!=null)
		{	
			tatrist = (List<Map<String,String>>)procResult.get("pv_cur_tatrisit_o");
			if(tatrist==null)
			{
				tatrist=new ArrayList<Map<String,String>>();
			}
			atrixrol = (List<Map<String,String>>)procResult.get("pv_cur_atrixrol_o");
			if(atrixrol==null)
			{
				atrixrol=new ArrayList<Map<String,String>>();
			}
			atrixant = (List<Map<String,String>>)procResult.get("pv_cur_atrixant_o");
			if(atrixant==null)
			{
				atrixant=new ArrayList<Map<String,String>>();
			}
			atrixper = (List<Map<String,String>>)procResult.get("pv_cur_atrixper_o");
			if(atrixper==null)
			{
				atrixper=new ArrayList<Map<String,String>>();
			}
			atrixcam = (List<Map<String,String>>)procResult.get("pv_cur_atrixcam_o");
			if(atrixcam==null)
			{
				atrixcam=new ArrayList<Map<String,String>>();
			}
			atrirang = (List<Map<String,String>>)procResult.get("pv_cur_atrirang_o");
			if(atrirang==null)	
			{
				atrirang=new ArrayList<Map<String,String>>();
			}
		}


		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** params=")  .append(params)
				.append("\n****** tatrist=") .append(tatrist)
				.append("\n****** atrixrol=").append(atrixrol)
				.append("\n****** atrixant=").append(atrixant)
				.append("\n****** atrixper=").append(atrixper)
				.append("\n****** atrixcam=").append(atrixcam)
				.append("\n****** atrirang=").append(atrirang)
				.append("\n****** PKG_CONSULTA.P_GET_PARAMETROS_CONFIG_AUTO ******")
				.append("\n*******************************************************")
				.toString()
				);
		List<List<Map<String,String>>>lista=new ArrayList<List<Map<String,String>>>();
		lista.add(tatrist);
		lista.add(atrixrol);
		lista.add(atrixant);
		lista.add(atrixper);
		lista.add(atrixcam);
		lista.add(atrirang);
		return lista;
	}
	
	protected class CargarParamerizacionConfiguracionCoberturas extends StoredProcedure
	{
		protected CargarParamerizacionConfiguracionCoberturas(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PARAMETROS_CONFIG_AUTO");
			declareParameter(new SqlParameter("cdtipsit"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("negocio"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoServicio" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoPersona"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("submarca"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clavegs"      , OracleTypes.VARCHAR));
			String[] cols  = new String[]{ "cdatribu" , "aplica" , "valor" };
			String[] cols2 = new String[]{ "cdatribu" , "minimo" , "maximo" };
			declareParameter(new SqlOutParameter("pv_cur_tatrisit_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixrol_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixant_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixper_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixcam_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrirang_o" , OracleTypes.CURSOR, new GenericMapper(cols2)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"       , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"        , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	protected class CargarParamerizacionConfiguracionCoberturasFronterizos extends StoredProcedure
	{
		protected CargarParamerizacionConfiguracionCoberturasFronterizos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PARAMETROS_CONFIG_FRONT");
			declareParameter(new SqlParameter("cdtipsit"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("negocio"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoServicio" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("modelo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoPersona"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("submarca"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clavegs"      , OracleTypes.VARCHAR));
			String[] cols  = new String[]{ "cdatribu" , "aplica" , "valor" };
			String[] cols2 = new String[]{ "cdatribu" , "minimo" , "maximo" };
			declareParameter(new SqlOutParameter("pv_cur_tatrisit_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixrol_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixant_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixper_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrirang_o" , OracleTypes.CURSOR, new GenericMapper(cols2)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"       , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"        , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>cargarDatosVehiculoRamo5(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza)throws ApplicationException, Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n**************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_AUTO_RAMO_5 ******")
				.append("\n****** params=")  .append(params)
				.append("\n**************************************************")
				.toString()
				);
		Map<String,Object>procResult = ejecutaSP(new CargarDatosVehiculoRamo5(getDataSource()),params);
		List<Map<String,String>>aux  = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(aux==null||aux.size()==0)
		{
			throw new ApplicationException("No se encontraron datos del auto");
		}
		else if(aux.size()>1)
		{
			throw new ApplicationException("Datos del auto duplicados");
		}
		logger.debug(
				new StringBuilder()
				.append("\n**************************************************")
				.append("\n****** params=")  .append(params)
				.append("\n****** registro=").append(aux.get(0))
				.append("\n****** PKG_CONSULTA.P_GET_DATOS_AUTO_RAMO_5 ******")
				.append("\n**************************************************")
				.toString()
				);
		return aux.get(0);
	}
	
	protected class CargarDatosVehiculoRamo5 extends StoredProcedure
	{
		protected CargarDatosVehiculoRamo5(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_AUTO_RAMO_5");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] cols=new String[]{"datos"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarTbasvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,Map<String,String>valores)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("status"   , status);
		params.put("cdtipsit" , cdtipsit);
		for(int i=1;i<=99;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString(),null);
		}
		params.putAll(valores);
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** PKG_SATELITES2.P_INSERTA_TBASVALSIT ******")
				.append("\n****** params=").append(params)
				.append("\n*************************************************")
				.toString()
				);
		ejecutaSP(new GuardarTbasvalsit(getDataSource()),params);
	}
	
	protected class GuardarTbasvalsit extends StoredProcedure
	{
		protected GuardarTbasvalsit(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES2.P_INSERTA_TBASVALSIT");	
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
    		for(int i=1;i<=99;i++)
    		{
    			declareParameter(new SqlParameter(
    					new StringBuilder("otvalor")
    					.append(StringUtils.leftPad(String.valueOf(i),2,"0"))
    					.toString()
    					, OracleTypes.VARCHAR));
    		}
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void guardarTconvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,Map<String,String>valores)throws ApplicationException,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("status"   , status);
		params.put("cdtipsit" , cdtipsit);
		for(int i=1;i<=99;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString(),null);
		}
		params.putAll(valores);
		logger.debug(
				new StringBuilder()
				.append("\n*************************************************")
				.append("\n****** PKG_SATELITES2.P_INSERTA_TCONVALSIT ******")
				.append("\n****** params=").append(params)
				.append("\n*************************************************")
				.toString()
				);
		ejecutaSP(new GuardarTconvalsit(getDataSource()),params);
	}
	
	protected class GuardarTconvalsit extends StoredProcedure
	{
		protected GuardarTconvalsit(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES2.P_INSERTA_TCONVALSIT");	
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
    		for(int i=1;i<=99;i++)
    		{
    			declareParameter(new SqlParameter(
    					new StringBuilder("otvalor")
    					.append(StringUtils.leftPad(String.valueOf(i),2,"0"))
    					.toString()
    					, OracleTypes.VARCHAR));
    		}
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void borrarTbasvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception
	{
		Map<String,String>params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** PKG_SATELITES2.P_BORRA_TBASVALSIT ******")
				.append("\n****** params=").append(params)
				.append("\n***********************************************")
				.toString()
				);
		ejecutaSP(new BorrarTbasvalsit(getDataSource()),params);
	}
	
	protected class BorrarTbasvalsit extends StoredProcedure
	{
		protected BorrarTbasvalsit(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES2.P_BORRA_TBASVALSIT");	
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void borrarTconvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception
	{
		Map<String,String>params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** PKG_SATELITES2.P_BORRA_TCONVALSIT ******")
				.append("\n****** params=").append(params)
				.append("\n***********************************************")
				.toString()
				);
		ejecutaSP(new BorrarTconvalsit(getDataSource()),params);
	}
	
	protected class BorrarTconvalsit extends StoredProcedure
	{
		protected BorrarTconvalsit(DataSource dataSource)
		{
    		super(dataSource,"PKG_SATELITES2.P_BORRA_TCONVALSIT");	
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public void movimientoMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,String swreduci
			,String cdagrupa
			,String cdestado
			,Date   fefecsit
			,Date   fecharef
			,String cdgrupo
			,String nmsituaext
			,String nmsitaux
			,String nmsbsitext
			,String cdplan
			,String cdasegur
			,String accion)throws Exception
	{
		Map<String,Object>params=new LinkedHashMap<String,Object>();
		params.put("cdunieco"   , cdunieco);
		params.put("cdramo"     , cdramo);
		params.put("estado"     , estado);
		params.put("nmpoliza"   , nmpoliza);
		params.put("nmsituac"   , nmsituac);
		params.put("nmsuplem"   , nmsuplem);
		params.put("status"     , status);
		params.put("cdtipsit"   , cdtipsit);
		params.put("swreduci"   , swreduci);
		params.put("cdagrupa"   , cdagrupa);
		params.put("cdestado"   , cdestado);
		params.put("fefecsit"   , fefecsit);
		params.put("fecharef"   , fecharef);
		params.put("cdgrupo"    , cdgrupo);
		params.put("nmsituaext" , nmsituaext);
		params.put("nmsitaux"   , nmsitaux);
		params.put("nmsbsitext" , nmsbsitext);
		params.put("cdplan"     , cdplan);
		params.put("cdasegur"   , cdasegur);
		params.put("accion"     , accion);
		logger.debug(
				new StringBuilder()
				.append("\n******************************************")
				.append("\n****** PKG_SATELITES.P_MOV_MPOLISIT ******")
				.append("\n****** params=").append(params)
				.append("\n******************************************")
				.toString()
				);
		ejecutaSP(new MovimientoMpolisit(getDataSource()),params);
	}
	
	protected class MovimientoMpolisit extends StoredProcedure
	{
		protected MovimientoMpolisit(DataSource dataSource)
		{
	        super(dataSource,"PKG_SATELITES.P_MOV_MPOLISIT");
		    declareParameter(new SqlParameter("cdunieco"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("cdramo"     , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("estado"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("nmpoliza"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("nmsituac"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("nmsuplem"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("status"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdtipsit"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("swreduci"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdagrupa"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("cdestado"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("fefecsit"   , OracleTypes.DATE));
		    declareParameter(new SqlParameter("fecharef"   , OracleTypes.DATE));
		    declareParameter(new SqlParameter("cdgrupo"    , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("nmsituaext" , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("nmsitaux"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("nmsbsitext" , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdplan"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdasegur"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
		    declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
		    compile();
		}
	}
	
	@Override
	public void movimientoTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,Map<String,String>valores
			,String accion
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("status"   , status);
		params.put("cdtipsit" , cdtipsit);
		for(int i=1;i<=99;i++)
		{
			params.put(new StringBuilder("otvalor").append(StringUtils.leftPad(String.valueOf(i),2,"0")).toString(),null);
		}
		params.put("accion"   , accion);
		params.putAll(valores);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************")
				.append("\n****** PKG_SATELITES2.P_MOV_TVALOSIT ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************")
				.toString()
				);
		ejecutaSP(new MovimientoTvalosit(getDataSource()),params);
	}
	
	protected class MovimientoTvalosit extends StoredProcedure
	{
		protected MovimientoTvalosit(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_TVALOSIT");
			
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			for(int i=1;i<=99;i++)
			{
				declareParameter(new SqlParameter(
						new StringBuilder("otvalor")
						.append(StringUtils.leftPad(String.valueOf(i),2,"0"))
						.toString()
						,OracleTypes.VARCHAR));
			}
			declareParameter(new SqlParameter("accion" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void clonarPersonas(
			String cdelemen
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdtipsit
			,Date   fecha
			,String cdusuari
			,String nombre1
			,String nombre2
			,String apellido1
			,String apellido2
			,String sexo
			,Date   fenacimi
			,String parentesco
			)throws Exception
	{
		Map<String,Object>params = new LinkedHashMap<String,Object>();
		params.put("cdelemen"   , cdelemen);
		params.put("cdunieco"   , cdunieco);
		params.put("cdramo"     , cdramo);
		params.put("estado"     , estado);
		params.put("nmpoliza"   , nmpoliza);
		params.put("nmsituac"   , nmsituac);
		params.put("cdtipsit"   , cdtipsit);
		params.put("fecha"      , fecha);
		params.put("cdusuari"   , cdusuari);
		params.put("nombre1"    , nombre1);
		params.put("nombre2"    , nombre2);
		params.put("apellido1"  , apellido1);
		params.put("apellido2"  , apellido2);
		params.put("sexo"       , sexo);
		params.put("fenacimi"   , fenacimi);
		params.put("parentesco" , parentesco);
		Utils.debugProcedure(logger, "PKG_COTIZA.P_CLONAR_PERSONAS", params);
		ejecutaSP(new ClonarPersonas(getDataSource()),params);
	}
    
    protected class ClonarPersonas extends StoredProcedure
    {
    	protected ClonarPersonas(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_CLONAR_PERSONAS");
            declareParameter(new SqlParameter("cdelemen"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdunieco"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("cdramo"     , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("estado"     , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("nmsituac"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("cdtipsit"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("fecha"      , OracleTypes.DATE));
            declareParameter(new SqlParameter("cdusuari"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nombre1"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nombre2"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("apellido1"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("apellido2"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("sexo"       , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("fenacimi"   , OracleTypes.DATE));
            declareParameter(new SqlParameter("parentesco" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>cargarResultadosCotizacion(
			String cdusuari
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdelemen
			,String cdtipsit
			)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdusuari" , cdusuari);
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("cdelemen" , cdelemen);
    	params.put("cdtipsit" , cdtipsit);
    	logger.debug(
    			new StringBuilder()
    			.append("\n*******************************************")
    			.append("\n****** PKG_COTIZA.P_GEN_TARIFICACION ******")
    			.append("\n****** params=").append(params)
    			.append("\n*******************************************")
    			.toString()
    			);
    	Map<String,Object>procResult  = ejecutaSP(new CargarResultadosCotizacion(getDataSource()),params);
    	logger.debug(procResult);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_record_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException("No hay resultados de cotizacion");
    	}
    	logger.debug(
    			new StringBuilder()
    			.append("\n*******************************************")
    			.append("\n****** params=")  .append(params)
    			.append("\n****** registro=").append(lista)
    			.append("\n****** PKG_COTIZA.P_GEN_TARIFICACION ******")
    			.append("\n*******************************************")
    			.toString()
    			);
    	return lista;
	}
    
    protected class CargarResultadosCotizacion extends StoredProcedure
    {
    	protected CargarResultadosCotizacion(DataSource dataSource)
        {
            super(dataSource,"PKG_COTIZA.P_GEN_TARIFICACION");
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdelemen" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDIDENTIFICA" , "CDUNIECO" , "CDRAMO"    , "ESTADO"
            		,"NMPOLIZA"    , "NMSUPLEM" , "STATUS"    , "CDPLAN"
            		,"DSPLAN"      , "MNPRIMA"  , "CDCIAASEG" , "DSUNIECO"
            		,"CDPERPAG"    , "DSPERPAG" , "CDTIPSIT"  , "FEEMISIO"
            		,"FEVENCIM"    , "NMSITUAC"
            };
            declareParameter(new SqlOutParameter("pv_record_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
     @Override
     public List<Map<String,String>>cargarParametrizacionExcel(
 			String proceso
 			,String cdramo
 			,String cdtipsit)throws Exception
 	{
    	 Map<String,String>params=new LinkedHashMap<String,String>();
    	 params.put("proceso"  , proceso);
    	 params.put("cdramo"   , cdramo);
    	 params.put("cdtipsit" , cdtipsit);
    	 logger.debug(
    			 new StringBuilder()
    			 .append("\n******************************************************")
    			 .append("\n****** PKG_SATELITES2.P_GET_CONFIGURACION_EXCEL ******")
    			 .append("\n****** params=").append(params)
    			 .append("\n******************************************************")
    			 .toString()
    			 );
    	 Map<String,Object>procResult  = ejecutaSP(new CargarParametrizacionExcel(getDataSource()),params);
    	 List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	 if(lista==null||lista.size()==0)
    	 {
    		 throw new ApplicationException("No hay configuracion de excel para el proceso/producto/modalidad");
    	 }
    	 return lista;
 	}
     
     protected class CargarParametrizacionExcel extends StoredProcedure
     {
     	protected CargarParametrizacionExcel(DataSource dataSource)
         {
             super(dataSource,"PKG_SATELITES2.P_GET_CONFIGURACION_EXCEL");
             declareParameter(new SqlParameter("proceso"  , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
             String[] cols = new String[]{
             		"COLUMNA"    , "CDTIPSIT"        , "PROPIEDAD"  , "TIPO"
             		,"REQUERIDO" , "DECODE"          , "CDTABLA1"   , "TIPOATRI"
             		,"VALOR"     , "ORIGEN_CDTIPSIT" , "OTRA_PROP1" , "OTRO_VAL1"
             		,"DESCRIPCION"
             };
             declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
             declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
             declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
             compile();
     	}
     }
     
     @Override
     public String cargarClaveTtapvat1(
    		 String cdtabla
    		 ,String otvalor
    		 ,Map<String,List<Map<String,String>>>buffer
    		 )throws Exception
     {
    	 List<Map<String,String>> lista = null;
    	 if(buffer!=null&&buffer.containsKey(cdtabla))
    	 {
    		 lista = buffer.get(cdtabla);
    		 logger.debug(Utils.log(
    				  "\n*****************************************"
    				 ,"\n****** P_GET_CLAVE_TTAPVAT1 buffer ******"
    				 ,"\n****** lista="    , lista
    				 ,"\n****** buscando=" , otvalor
    				 ,"\n*****************************************"
    				 ));
    	 }
    	 else
    	 {
    		 Map<String,String>params=new LinkedHashMap<String,String>();
        	 params.put("cdtabla" , cdtabla);
        	 logger.debug(Utils.log(
        			  "\n*************************************************"
        			 ,"\n****** PKG_SATELITES2.P_GET_CLAVE_TTAPVAT1 ******"
        			 ,"\n****** params=",params
        			 ,"\n*************************************************"
        	 ));
        	 Map<String,Object> procResult = ejecutaSP(new CargarClaveTtapvat1(getDataSource()),params);
        	 lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
        	 Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_CLAVE_TTAPVAT1", params, lista);
        	 if(buffer!=null&&lista!=null&&lista.size()>0)
        	 {
        		 buffer.put(cdtabla,lista);
        	 }
    	 }
    	 if(lista==null||lista.size()==0)
    	 {
    		 throw new ApplicationException(Utils.join(
    				 "No hay registros en la(s) tabla(s) de apoyo"
    				 ));
    	 }
    	 String otclave = null;
    	 for(Map<String,String>elem:lista)
    	 {
    		 if(elem.get("OTVALOR").trim().equals(otvalor.trim()))	
    		 {
    			otclave = elem.get("OTCLAVE");
    		 }
    	 }
    	 if(otclave==null)
    	 {
    		 throw new ApplicationException("No existe la clave en los registros de la tabla de apoyo");
    	 }
    	 return otclave;
     }
     
     protected class CargarClaveTtapvat1 extends StoredProcedure
     {
     	protected CargarClaveTtapvat1(DataSource dataSource)
         {
             super(dataSource,"PKG_SATELITES2.P_GET_CLAVE_TTAPVAT1");
             declareParameter(new SqlParameter("cdtabla" , OracleTypes.VARCHAR));
             declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"OTCLAVE","OTVALOR"})));
             declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
             declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
             compile();
     	}
     }
     
     @Override
     public List<Map<String,String>>cargarResultadosCotizacionAutoFlotilla(
 			String cdunieco
 			,String cdramo
 			,String estado
 			,String nmpoliza)throws Exception
 	{
    	 Map<String,String>params=new LinkedHashMap<String,String>();
    	 params.put("cdunieco" , cdunieco);
    	 params.put("cdramo"   , cdramo);
    	 params.put("estado"   , estado);
    	 params.put("nmpoliza" , nmpoliza);
    	 Utils.debugProcedure(logger,"PKG_SATELITES2.P_GEN_TARIFICA_AUTO_FLOT",params);
    	 Map<String,Object>procResult  = ejecutaSP(new CargarResultadosCotizacionAutoFlotilla(getDataSource()),params);
    	 List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	 if(lista==null||lista.size()==0)
    	 {
    		 throw new ApplicationException("No hay resultados de cotizacion");
    	 }
    	 Utils.debugProcedure(logger,"PKG_SATELITES2.P_GEN_TARIFICA_AUTO_FLOT",params,lista);
    	 return lista;
 	}
     
     protected class CargarResultadosCotizacionAutoFlotilla extends StoredProcedure
     {
     	protected CargarResultadosCotizacionAutoFlotilla(DataSource dataSource)
         {
             super(dataSource,"PKG_SATELITES2.P_GEN_TARIFICA_AUTO_FLOT");
             declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
             String[] cols=new String[]{"CDPERPAG","DSPERPAG","PRIMA"};
             declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
             declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
             declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
             compile();
     	}
     }
     
     @Override
     public List<Map<String,String>>cargarDetallesCotizacionAutoFlotilla(
 			String cdunieco
 			,String cdramo
 			,String estado
 			,String nmpoliza
 			,String cdperpag
 			)throws Exception
 	{
    	 Map<String,String>params=new LinkedHashMap<String,String>();
    	 params.put("cdunieco" , cdunieco);
    	 params.put("cdramo"   , cdramo);
    	 params.put("estado"   , estado);
    	 params.put("nmpoliza" , nmpoliza);
    	 params.put("cdperpag" , cdperpag);
    	 Utils.debugProcedure(logger,"PKG_SATELITES2.P_GET_DETALLE_COTI_AUTO_FLOT", params);
    	 Map<String,Object>procResult  = ejecutaSP(new CargarDetallesCotizacionAutoFlotilla(getDataSource()),params);
    	 List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	 if(lista==null||lista.size()==0)
    	 {
    		 throw new ApplicationException("No hay detalles de cotizacion");
    	 }
    	 Utils.debugProcedure(logger,"PKG_SATELITES2.P_GET_DETALLE_COTI_AUTO_FLOT", params,lista);
    	 return lista;
 	}
     
     protected class CargarDetallesCotizacionAutoFlotilla extends StoredProcedure
     {
     	protected CargarDetallesCotizacionAutoFlotilla(DataSource dataSource)
     	{
     		super(dataSource,"PKG_SATELITES2.P_GET_DETALLE_COTI_AUTO_FLOT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DetalleCotizacionAutoFLotillaMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
     	}
     }
     
     protected class DetalleCotizacionAutoFLotillaMapper implements RowMapper<Map<String,String>> {
         public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
             Map<String, String> map = new HashMap<String, String>();
             map.put("CDUNIECO", rs.getString("CDUNIECO"));
             map.put("CDRAMO", rs.getString("CDRAMO"));
             map.put("ESTADO", rs.getString("ESTADO"));
             map.put("NMPOLIZA", rs.getString("NMPOLIZA"));
             map.put("NMSITUAC", rs.getString("NMSITUAC"));
             map.put("PRIMA", rs.getString("PRIMA"));
             map.put("COBERTURA", rs.getString("COBERTURA"));
             map.put("TITULO", rs.getString("TITULO"));
             map.put("ORDEN", rs.getString("ORDEN"));
             return map;
         }
     }
     
     @Override
     public List<Map<String,String>>cargarDetallesCoberturasCotizacionAutoFlotilla(
 			String cdunieco
 			,String cdramo
 			,String estado
 			,String nmpoliza
 			,String cdperpag
 			)throws Exception
 	{
    	 Map<String,String>params=new LinkedHashMap<String,String>();
    	 params.put("cdunieco" , cdunieco);
    	 params.put("cdramo"   , cdramo);
    	 params.put("estado"   , estado);
    	 params.put("nmpoliza" , nmpoliza);
    	 params.put("cdperpag" , cdperpag);
    	 Utils.debugProcedure(logger,"PKG_SATELITES2.P_GET_DETALLE_COBER_AUTO_FLOT", params);
    	 Map<String,Object>procResult  = ejecutaSP(new CargarDetallesCoberturasCotizacionAutoFlotilla(getDataSource()),params);
    	 List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	 if(lista==null||lista.size()==0)
    	 {
    		 throw new ApplicationException("No hay detalles de cotizacion");
    	 }
    	 Utils.debugProcedure(logger,"PKG_SATELITES2.P_GET_DETALLE_COBER_AUTO_FLOT", params,lista);
    	 return lista;
 	}
     
    protected class CargarDetallesCoberturasCotizacionAutoFlotilla extends StoredProcedure
     {
     	protected CargarDetallesCoberturasCotizacionAutoFlotilla(DataSource dataSource)
         {
             super(dataSource,"PKG_SATELITES2.P_GET_DETALLE_COBER_AUTO_FLOT");
             declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
             declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
             String[] cols=new String[]{
            		 "CDUNIECO"
            		 ,"CDRAMO"
            		 ,"ESTADO"
            		 ,"NMPOLIZA"
            		 ,"NMSITUAC"
            		 ,"CDGARANT"
            		 ,"NMSUPLEM"
            		 ,"CDCAPITA"
            		 ,"COBERTURA"
            		 ,"SUMASEG"
            		 ,"TITULO"
            		 ,"ORDEN"
            		 };
             declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
             declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
             declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
             compile();
     	}
     }

    @Override
    public String cargarTabuladoresGMIParche(
    		String circulo
    		,String cdatribu)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("circulo"  , circulo);
    	params.put("cdatribu" , cdatribu);
    	Utils.debugProcedure(logger, "PKG_LISTAS.P_RECUPERA_TABULADORES", params);
    	Map<String,Object>procResult     = ejecutaSP(new CargarTabuladoresGMIParche(getDataSource()),params);
    	List<Map<String,String>>listaAux = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(listaAux==null||listaAux.size()==0)
    	{
    		throw new ApplicationException("No hay tabulador");
    	}
    	if(listaAux.size()>1)
    	{
    		throw new ApplicationException("Tabulador duplicado");
    	}
    	return listaAux.get(0).get("TABULADOR");
    }
    
    protected class CargarTabuladoresGMIParche extends StoredProcedure
    {
    	protected CargarTabuladoresGMIParche(DataSource dataSource)
    	{
    		super(dataSource,"PKG_LISTAS.P_RECUPERA_TABULADORES");
            declareParameter(new SqlParameter("circulo"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{ "TABULADOR" })));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public void sigsvdefEnd(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdgarant
			,String cdtipsup)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("nmsituac" , nmsituac);
    	params.put("nmsuplem" , nmsuplem);
    	params.put("cdgarant" , cdgarant);
    	params.put("cdtipsup" , cdtipsup);
    	Utils.debugProcedure(logger, "P_EXEC_SIGSVDEFEND", params);
    	ejecutaSP(new SigsvdefEnd(getDataSource()),params);
	}
    
    protected class SigsvdefEnd extends StoredProcedure
    {
    	protected SigsvdefEnd(DataSource dataSource)
    	{
    		super(dataSource,"P_EXEC_SIGSVDEFEND");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdgarant" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public void borrarMpoliperSituac0(
    		String cdunieco
    		,String cdramo
    		,String estado
    		,String nmpoliza
    		,String nmsuplem
    		,String cdrol)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("nmsituac" , null);
    	params.put("cdrol"    , cdrol);
    	params.put("cdperson" , null);
    	params.put("nmsuplem" , nmsuplem);
    	params.put("status"   , null);
    	params.put("nmorddom" , null);
    	params.put("swreclam" , null);
    	params.put("accion"   , null);
    	Utils.debugProcedure(logger, "PKG_SATELITES.P_BORRA_MPOLIPER", params);
    	ejecutaSP(new BorrarMpoliperSituac0(getDataSource()),params);
    }
    
    protected class BorrarMpoliperSituac0 extends StoredProcedure
    {
    	protected BorrarMpoliperSituac0(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES.P_BORRA_MPOLIPER");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdrol"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmorddom" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swreclam" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public String cargarTipoVehiculoRamo5(String clave)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("clave" , clave);
    	Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_TIPOVEHI_RAMO5", params);
    	Map<String,Object>procResult  = ejecutaSP(new CargarTipoVehiculoRamo5(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException("No se encontro el tipo de vehiculo");
    	}
    	if(lista.size()>1)
    	{
    		throw new ApplicationException("Tipo de vehiculo duplicado");
    	}
    	return lista.get(0).get("TIPOVEHI");
    }
    
    protected class CargarTipoVehiculoRamo5 extends StoredProcedure
    {
    	protected CargarTipoVehiculoRamo5(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES2.P_GET_TIPOVEHI_RAMO5");
            declareParameter(new SqlParameter("clave" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"TIPOVEHI"})));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,String>cargarDetalleNegocioRamo5(String negocio, String cdramo, String cdtipsit, String cdsisrol, String cdusuari) throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("negocio" , negocio);
    	params.put("cdramo" , cdramo);
    	params.put("cdtipsit" , cdtipsit);
    	params.put("cdsisrol" , cdsisrol);
    	params.put("cdusuari" , cdusuari);
    	Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_DETALLE_NEGOCIO_RAMO5", params);
    	Map<String,Object>procResult  = ejecutaSP(new CargarDetalleNegocioRamo5(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException("No se encontro detalle de negocio");
    	}
    	if(lista.size()>1)
    	{
    		throw new ApplicationException("Detalle de negocio duplicado");
    	}
    	return lista.get(0);
    }
    
    protected class CargarDetalleNegocioRamo5 extends StoredProcedure
    {
    	protected CargarDetalleNegocioRamo5(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES2.P_GET_DETALLE_NEGOCIO_RAMO5");
    		declareParameter(new SqlParameter("negocio" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdtipsit", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdsisrol", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdusuari", OracleTypes.VARCHAR));
    		String[] cols=new String[]{
    				"TARIFA"
    				,"UDI"
    				,"MULTIANUAL"
    				,"PERIODO_GRACIA"
    				,"FONDO_ESPECIAL"
    				,"F1"
    				,"F2"
    				,"F3"
    				,"PORC_BONO"
    				,"LIMITE_SUPERIOR"
    				,"DXN"
    				};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public Map<String,List<Map<String,String>>>cargarConfiguracionTvalositFlotillas(
    		String cdramo
    		,String cdtipsit
    		,String negocio
    		)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo"   , cdramo);
    	params.put("cdtipsit" , cdtipsit);
    	params.put("negocio"  , negocio);
    	Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_CONFIG_VALOSIT_FLOTILLAS", params);
    	Map<String,Object>procResult   = ejecutaSP(new CargarConfiguracionTvalositFlotillas(getDataSource()),params);
    	List<Map<String,String>>lista  = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	List<Map<String,String>>lista2 = (List<Map<String,String>>)procResult.get("pv_atrirang_o");
    	Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_CONFIG_VALOSIT_FLOTILLAS config", params, lista);
    	Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_CONFIG_VALOSIT_FLOTILLAS rangos", params, lista2);
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException(Utils.join("No hay configuracion para los valores del ramo ",cdramo," y subramo ",cdtipsit));
    	}
    	if(lista2==null)
    	{
    		lista2 = new ArrayList<Map<String,String>>();
    	}
    	Map<String,List<Map<String,String>>> res = new HashMap<String,List<Map<String,String>>>();
    	res.put("config" , lista);
    	res.put("rangos" , lista2);
    	return res;
    }
    
    protected class CargarConfiguracionTvalositFlotillas extends StoredProcedure
    {
    	protected CargarConfiguracionTvalositFlotillas(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES2.P_GET_CONFIG_VALOSIT_FLOTILLAS");
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("negocio"  , OracleTypes.VARCHAR));
    		String[] cols=new String[]{
    				"CDATRIBU"
    				,"VALOR"
    				};
    		String[] cols2=new String[]{
    				"CDTIPSIT"
    				,"CDATRIBU"
    				,"MINIMO"
    				,"MAXIMO"
    				};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_atrirang_o" , OracleTypes.CURSOR, new GenericMapper(cols2)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public boolean cargarBanderaCambioCuadroPorProducto(String cdramo)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo" , cdramo);
    	Utils.debugProcedure(logger, "PKG_CONSULTA.P_GET_BANDERA_CAMBIO_CUACOM", params);
    	Map<String,Object>procResult  = ejecutaSP(new CargarBanderaCambioCuadroPorProducto(getDataSource()),params);
    	String bandera                = (String)procResult.get("pv_bandera_o");
    	return StringUtils.isNotBlank(bandera)&&bandera.equals("S");
    }
    
    protected class CargarBanderaCambioCuadroPorProducto extends StoredProcedure
    {
    	protected CargarBanderaCambioCuadroPorProducto(DataSource dataSource)
    	{
    		super(dataSource,"PKG_CONSULTA.P_GET_BANDERA_CAMBIO_CUACOM");
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_bandera_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public Map<String,String>cargarRangoDescuentoRamo5TodasSituaciones(
    		String cdagente
    		,String negocio
    		,String cdsisrol
    		,String cdusuari
    		)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdagente" , cdagente);
    	params.put("negocio"  , negocio);
    	params.put("cdsisrol" , cdsisrol);
    	params.put("cdusuari" , cdusuari);
    	Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_DESCUENTO_RAMO5_TODAS",params);
    	Map<String,Object>procResult  = ejecutaSP(new CargarRangoDescuentoRamo5TodasSituaciones(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No hay descuento/recargo para el agente");
		}
		if(lista.size()>1)
		{
			throw new ApplicationException("Descuento/recargo para el agente duplicado");
		}
		Utils.debugProcedure(logger, "PKG_SATELITES2.P_GET_DESCUENTO_RAMO5_TODAS",params,lista);
		return lista.get(0);
    }
	
	protected class CargarRangoDescuentoRamo5TodasSituaciones extends StoredProcedure
	{
		protected CargarRangoDescuentoRamo5TodasSituaciones(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_GET_DESCUENTO_RAMO5_TODAS");
			declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("negocio"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "min" , "max" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void validarCodpostalTarifa(String codpostal,String cdtipsit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("codpostal" , codpostal);
		params.put("cdtipsit"  , cdtipsit);
		Utils.debugProcedure(logger, "PKG_SATELITES.P_VALIDA_TARIFA",params);
		ejecutaSP(new ValidarCodpostalTarifa(getDataSource()),params);
	}
	
	protected class ValidarCodpostalTarifa extends StoredProcedure
	{
		protected ValidarCodpostalTarifa(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES.P_VALIDA_TARIFA");
			declareParameter(new SqlParameter("codpostal" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public boolean validaDomicilioCotizacionTitular(Map<String,String> params)throws Exception{
		Utils.debugProcedure(logger, "PKG_SATELITES2.P_VALIDA_DOMICILIO_TITULAR",params);
		
		Map<String,Object>procResult=ejecutaSP(new ValidaDomicilCotTitular(getDataSource()),params);
		String resVal = (String)procResult.get("pv_swdomici_o");
		
		boolean resValidacion =(StringUtils.isNotBlank(resVal)&&resVal.equalsIgnoreCase("S"));
		
		logger.debug(Utils.log("PKG_SATELITES2.P_VALIDA_DOMICILIO_TITULAR result=",resValidacion));
		return resValidacion;
	}
	
	protected class ValidaDomicilCotTitular extends StoredProcedure
	{
		protected ValidaDomicilCotTitular(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_VALIDA_DOMICILIO_TITULAR");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmorddom_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_swdomici_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public boolean validarCuadroComisionNatural(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		Utils.debugProcedure(logger, "PKG_SATELITES2.P_VALIDA_CUADRO_COM_NATURAL",params);
		Map<String,Object>procResult=ejecutaSP(new ValidarCuadroComisionNatural(getDataSource()),params);
		String cuadroNatural=(String)procResult.get("pv_swcamcua_o");
		boolean cuadroNaturalBol=StringUtils.isNotBlank(cuadroNatural)&&cuadroNatural.equalsIgnoreCase("S");
		logger.debug(Utils.log("PKG_SATELITES2.P_VALIDA_CUADRO_COM_NATURAL result=",cuadroNatural));
		return cuadroNaturalBol;
	}
	
	protected class ValidarCuadroComisionNatural extends StoredProcedure
	{
		protected ValidarCuadroComisionNatural(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_VALIDA_CUADRO_COM_NATURAL");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_swcamcua_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void aplicarAjustesCotizacionPorProducto(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdtipsit
			,String tipocot
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("cdtipsit" , cdtipsit);
		params.put("tipocot"  , tipocot);
		Utils.debugProcedure(logger, "PKG_SATELITES2.P_AJUSTES_COTIZACION_PRODUCTO", params);
		ejecutaSP(new AplicarAjustesCotizacionPorProducto(getDataSource()),params);
	}
	
	protected class AplicarAjustesCotizacionPorProducto extends StoredProcedure
	{
		protected AplicarAjustesCotizacionPorProducto(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_AJUSTES_COTIZACION_PRODUCTO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipocot"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<List<Map<String,String>>>cargarParamerizacionConfiguracionCoberturasRol(
			String cdtipsit
			,String cdsisrol)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdtipsit"     , cdtipsit);
		params.put("cdsisrol"     , cdsisrol);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_PARAMS_CONFIG_AUTO_ROL ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>procResult    = ejecutaSP(new CargarParamerizacionConfiguracionCoberturasRol(getDataSource()),params);
		List<Map<String,String>>tatrist = (List<Map<String,String>>)procResult.get("pv_cur_tatrisit_o");
		if(tatrist==null)
		{
			tatrist=new ArrayList<Map<String,String>>();
		}
		List<Map<String,String>>atrixrol = (List<Map<String,String>>)procResult.get("pv_cur_atrixrol_o");
		if(atrixrol==null)
		{
			atrixrol=new ArrayList<Map<String,String>>();
		}
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** params=")  .append(params)
				.append("\n****** tatrist=") .append(tatrist)
				.append("\n****** atrixrol=").append(atrixrol)
				.append("\n****** PKG_CONSULTA.P_GET_PARAMS_CONFIG_AUTO_ROL ******")
				.append("\n*******************************************************")
				.toString()
				);
		List<List<Map<String,String>>>lista=new ArrayList<List<Map<String,String>>>();
		lista.add(tatrist);
		lista.add(atrixrol);
		return lista;
	}
	
	protected class CargarParamerizacionConfiguracionCoberturasRol extends StoredProcedure
	{
		protected CargarParamerizacionConfiguracionCoberturasRol(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PARAMS_CONFIG_AUTO_ROL");
			declareParameter(new SqlParameter("cdtipsit"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"     , OracleTypes.VARCHAR));
			String[] cols  = new String[]{ "cdatribu" , "aplica" , "valor" };
			declareParameter(new SqlOutParameter("pv_cur_tatrisit_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_cur_atrixrol_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"       , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"        , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String cargarPorcentajeCesionComisionAutos(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		Utils.debugProcedure(logger, "PKG_CONSULTA.P_GET_PORC_CESION_COMISION", params);
		Map<String,Object>procResult = ejecutaSP(new CargarPorcentajeCesionComisionAutos(getDataSource()),params);
		String cesion                = (String)procResult.get("pv_porreadu_o");
		if(StringUtils.isBlank(cesion))
		{
			throw new ApplicationException("No se recupero cesion de comision para la poliza");
		}
		logger.debug(Utils.log("****** PKG_CONSULTA.P_GET_PORC_CESION_COMISION recupera: ",cesion));
		return cesion;
	}
	
	protected class CargarPorcentajeCesionComisionAutos extends StoredProcedure
	{
		protected CargarPorcentajeCesionComisionAutos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PORC_CESION_COMISION");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_porreadu_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void ejecutaValoresDefectoTarificacionConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		params.put("tipotari" , tipotari);
		params.put("cdperpag" , cdperpag);
		Utils.debugProcedure(logger, "P_EJECUTA_LANZATARI", params);
		ejecutaSP(new EjecutaValoresDefectoTarificacionConcurrente(getDataSource()),params);
	}
	
	protected class EjecutaValoresDefectoTarificacionConcurrente extends StoredProcedure
	{
		protected EjecutaValoresDefectoTarificacionConcurrente(DataSource dataSource)
		{
			super(dataSource,"P_EJECUTA_LANZATARI");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipotari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoMpolisitLote(List<PMovMpolisitDTO> lista)throws Exception
	{
		String[][] array = new String[lista.size()][];
		int        i     = 0;
		for(PMovMpolisitDTO pMovIte : lista)
		{
			array[i++] = pMovIte.indexar();
		}
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("array" , new SqlArrayValue(array));
		Utils.debugProcedure(logger, "PKG_PROCESA_XML.PR_PROC_MPOLISIT_LISTA", params);
		ejecutaSP(new MovimientoMpolisitArray(getDataSource()),params);
	}
	
	protected class MovimientoMpolisitArray extends StoredProcedure
	{
		protected MovimientoMpolisitArray(DataSource dataSource)
		{
			super(dataSource,"PKG_PROCESA_XML.PR_PROC_MPOLISIT_LISTA");
			declareParameter(new SqlParameter("array" , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTvalositLote(List<PMovTvalositDTO> lista)throws Exception
	{
		String[][] array = new String[lista.size()][];
		int        i     = 0;
		for(PMovTvalositDTO pMovIte : lista)
		{
			array[i++] = pMovIte.indexar();
		}
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("array" , new SqlArrayValue(array));
		Utils.debugProcedure(logger, "PKG_PROCESA_XML.PR_PROC_TVALOSIT_LISTA", params);
		ejecutaSP(new MovimientoTvalositArray(getDataSource()),params);
	}
	
	protected class MovimientoTvalositArray extends StoredProcedure
	{
		protected MovimientoTvalositArray(DataSource dataSource)
		{
			super(dataSource,"PKG_PROCESA_XML.PR_PROC_TVALOSIT_LISTA");
			declareParameter(new SqlParameter("array" , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTbasvalsitLote(List<PInsertaTbasvalsitDTO> lista)throws Exception
	{
		String[][] array = new String[lista.size()][];
		int        i     = 0;
		for(PInsertaTbasvalsitDTO pMovIte : lista)
		{
			array[i++] = pMovIte.indexar();
		}
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("array" , new SqlArrayValue(array));
		Utils.debugProcedure(logger, "PKG_PROCESA_XML.PR_PROC_TBASVALSIT_LISTA", params);
		ejecutaSP(new MovimientoTbasvalsitArray(getDataSource()),params);
	}
	
	protected class MovimientoTbasvalsitArray extends StoredProcedure
	{
		protected MovimientoTbasvalsitArray(DataSource dataSource)
		{
			super(dataSource,"PKG_PROCESA_XML.PR_PROC_TBASVALSIT_LISTA");
			declareParameter(new SqlParameter("array" , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTconvalsitLote(List<PInsertaTconvalsitDTO> lista)throws Exception
	{
		String[][] array = new String[lista.size()][];
		int        i     = 0;
		for(PInsertaTconvalsitDTO pMovIte : lista)
		{
			array[i++] = pMovIte.indexar();
		}
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("array" , new SqlArrayValue(array));
		Utils.debugProcedure(logger, "PKG_PROCESA_XML.PR_PROC_TCONVALSIT_LISTA", params);
		ejecutaSP(new MovimientoTconvalsitArray(getDataSource()),params);
	}
	
	protected class MovimientoTconvalsitArray extends StoredProcedure
	{
		protected MovimientoTconvalsitArray(DataSource dataSource)
		{
			super(dataSource,"PKG_PROCESA_XML.PR_PROC_TCONVALSIT_LISTA");
			declareParameter(new SqlParameter("array" , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoMpoliagr(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdagrupa
			,String nmsuplem
			,String status
			,String cdperson
			,String nmorddom
			,String cdforpag
			,String cdbanco
			,String cdsucurs
			,String cdcuenta
			,String cdrazon
			,String swregula
			,String cdperreg
			,Date feultreg
			,String cdgestor
			,String cdrol
			,String cdbanco2
			,String cdsucurs2
			,String cdcuenta2
			,String cdtipcta
			,String cdtipcta2
			,String cdpagcom
			,String nmpresta
			,String nmpresta2
			,String cdbanco3
			,String cdsucurs3
			,String cdcuenta3
			,String cdtipcta3
			,String nmpresta3
			,String nmcuenta
			,String accion
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdunieco"  , cdunieco);
		params.put("cdramo"    , cdramo);
		params.put("estado"    , estado);
		params.put("nmpoliza"  , nmpoliza);
		params.put("cdagrupa"  , cdagrupa);
		params.put("nmsuplem"  , nmsuplem);
		params.put("status"    , status);
		params.put("cdperson"  , cdperson);
		params.put("nmorddom"  , nmorddom);
		params.put("cdforpag"  , cdforpag);
		params.put("cdbanco"   , cdbanco);
		params.put("cdsucurs"  , cdsucurs);
		params.put("cdcuenta"  , cdcuenta);
		params.put("cdrazon"   , cdrazon);
		params.put("swregula"  , swregula);
		params.put("cdperreg"  , cdperreg);
		params.put("feultreg"  , feultreg);
		params.put("cdgestor"  , cdgestor);
		params.put("cdrol"     , cdrol);
		params.put("cdbanco2"  , cdbanco2);
		params.put("cdsucurs2" , cdsucurs2);
		params.put("cdcuenta2" , cdcuenta2);
		params.put("cdtipcta"  , cdtipcta);
		params.put("cdtipcta2" , cdtipcta2);
		params.put("cdpagcom"  , cdpagcom);
		params.put("nmpresta"  , nmpresta);
		params.put("nmpresta2" , nmpresta2);
		params.put("cdbanco3"  , cdbanco3);
		params.put("cdsucurs3" , cdsucurs3);
		params.put("cdcuenta3" , cdcuenta3);
		params.put("cdtipcta3" , cdtipcta3);
		params.put("nmpresta3" , nmpresta3);
		params.put("nmcuenta"  , nmcuenta);
		params.put("accion"    , accion);
		Utils.debugProcedure(logger, "PKG_SATELITES.P_MOV_MPOLIAGR", params);
		ejecutaSP(new MovimientoMpoliagr(getDataSource()),params);
	}
	
	protected class MovimientoMpoliagr extends StoredProcedure
	{
		protected MovimientoMpoliagr(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES.P_MOV_MPOLIAGR");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("cdagrupa"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperson"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("nmorddom"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("cdforpag"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdbanco"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsucurs"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdcuenta"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrazon"   , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("swregula"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperreg"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("feultreg"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdgestor"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrol"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdbanco2"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsucurs2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdcuenta2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipcta"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipcta2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdpagcom"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("nmpresta"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpresta2" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdbanco3"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsucurs3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdcuenta3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipcta3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpresta3" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmcuenta"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>validarReemplazoDocumentoCotizacion(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		Map<String,Object> procResult = ejecutaSP(new ValidarReemplazoDocumentoCotizacion(getDataSource()),params);
		Map<String,String> result     = new LinkedHashMap<String,String>();
		result.put("cdtipsit" , (String)procResult.get("pv_cdtipsit_o"));
		result.put("cdplan"   , (String)procResult.get("pv_cdplan_o"));
		result.put("cdperpag" , (String)procResult.get("pv_cdperpag_o"));
		result.put("tipoflot" , (String)procResult.get("pv_tipoflot_o"));
		result.put("ntramite" , (String)procResult.get("pv_ntramite_o"));
		result.put("cdusuari" , (String)procResult.get("pv_cdusuari_o"));
		result.put("reporte"  , (String)procResult.get("pv_reporte_o"));
		return result;
	}
	
	protected class ValidarReemplazoDocumentoCotizacion extends StoredProcedure
	{
		protected ValidarReemplazoDocumentoCotizacion(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_VALIDA_REMPLAZO_DOC_COTI");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipsit_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdplan_o"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdperpag_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_tipoflot_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdusuari_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_reporte_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void grabarEvento(
			StringBuilder sb
			,String cdmodulo
			,String cdevento
			,Date fecha
			,String cdusuari
			,String cdsisrol
			,String ntramite
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsolici
			,String cdagente
			,String cdusuariDes
			,String cdsisrolDes
			,String status
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdmodulo"    , cdmodulo);
		params.put("cdevento"    , cdevento);
		params.put("fecha"       , fecha);
		params.put("cdusuari"    , cdusuari);
		params.put("cdsisrol"    , cdsisrol);
		params.put("ntramite"    , ntramite);
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("nmsolici"    , nmsolici);
		params.put("cdagente"    , cdagente);
		params.put("cdusuariDes" , cdusuariDes);
		params.put("cdsisrolDes" , cdsisrolDes);
		params.put("status"      , status);
		sb.append(Utils.join(
				 "\n********************************************"
				,"\n****** PKG_ESTADISTICA.P_GRABA_EVENTO ******"
				,"\n****** params=" , params
				,"\n********************************************"
				));
		ejecutaSP(new GrabarEvento(getDataSource()),params);
	}
	
	protected class GrabarEvento extends StoredProcedure
	{
		protected GrabarEvento(DataSource dataSource)
		{
			super(dataSource, "PKG_ESTADISTICA.P_GRABA_EVENTO");
			declareParameter(new SqlParameter("cdmodulo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdevento"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fecha"       , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("cdusuari"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsolici"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariDes" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrolDes" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void ejecutaValoresDefectoConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		params.put("tipotari" , tipotari);
		params.put("cdperpag" , cdperpag);
		Utils.debugProcedure(logger, "P_Ejecuta_lanzadef", params);
		ejecutaSP(new EjecutaValoresDefectoConcurrente(getDataSource()),params);
	}
	
	protected class EjecutaValoresDefectoConcurrente extends StoredProcedure
	{
		protected EjecutaValoresDefectoConcurrente(DataSource dataSource)
		{
			super(dataSource,"P_Ejecuta_lanzadef");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipotari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void ejecutaTarificacionConcurrente(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String tipotari
			,String cdperpag
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		params.put("tipotari" , tipotari);
		params.put("cdperpag" , cdperpag);
		Utils.debugProcedure(logger, "P_Ejecuta_lanzatar", params);
		ejecutaSP(new EjecutaTarificacionConcurrente(getDataSource()),params);
	}
	
	protected class EjecutaTarificacionConcurrente extends StoredProcedure
	{
		protected EjecutaTarificacionConcurrente(DataSource dataSource)
		{
			super(dataSource,"P_Ejecuta_lanzatar");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipotari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaValoresDefectoSituacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		ejecutaSP(new ActualizaValoresDefectoSituacion(getDataSource()),params);
	}
	
	protected class ActualizaValoresDefectoSituacion extends StoredProcedure
	{
		protected ActualizaValoresDefectoSituacion(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACTUALIZA_VALORES_X_DEF_SIT");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void procesaLayoutCensoMultisalud(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdestadoCli
			,String cdmuniciCli
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("censo"       , nombreArchivo);
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("cdestado"    , cdestadoCli);
		params.put("cdmunici"    , cdmuniciCli);
		params.put("cdplan1"     , cdplan1);
		params.put("cdplan2"     , cdplan2);
		params.put("cdplan3"     , cdplan3);
		params.put("cdplan4"     , cdplan4);
		params.put("cdplan5"     , cdplan5);
		params.put("complemento" , complemento);
		ejecutaSP(new ProcesaLayoutCensoMultisalud(getDataSource()),params);
	}
	
	protected class ProcesaLayoutCensoMultisalud extends StoredProcedure
	{
		protected ProcesaLayoutCensoMultisalud(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_LAYOUT_CENSO_MS_COLECTIVO");
			declareParameter(new SqlParameter("censo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmunici"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan1"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan2"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan3"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan4"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan5"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("complemento" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String guardarConfiguracionGarantias(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdpaquete
			,boolean nuevo
			,String dspaquete
			,String derpol
			,List<ConfiguracionCoberturaDTO>lista
			)throws Exception
	{
		Map<String,Object>params = new LinkedHashMap<String,Object>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdplan"   , cdplan);
		params.put("cdpaquete" , cdpaquete);
		params.put("nuevo"     , nuevo ? "S" : "N");
		params.put("dspaquete" , dspaquete);
		params.put("derpol"    , derpol);
		
		String[][] array = new String[lista.size()][];
		int        i     = 0;
		for(ConfiguracionCoberturaDTO e : lista)
		{
			array[i++] = e.indexar();
		}
		params.put("llave" , ConfiguracionCoberturaDTO.obtenerLlaveAtributos());
		params.put("array" , new SqlArrayValue(array));
		
		Map<String,Object> procRes = ejecutaSP(new GuardarConfiguracionGarantias(getDataSource()),params);
		return (String) procRes.get("pv_cdpaquete_o");
	}
	
	protected class GuardarConfiguracionGarantias extends StoredProcedure
	{
		protected GuardarConfiguracionGarantias(DataSource dataSource)
		{
			super(dataSource,"PKG_COTIZA.P_GUARDA_CONFIG_COBERTURAS_RC");
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdpaquete" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nuevo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dspaquete" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("derpol"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("llave"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("array"     , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_cdpaquete_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtenerCoberturasPlanColec(
			String cdramo
			,String cdtipsit
			,String cdplan
			,String cdsisrol
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"    , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdplan"   , cdplan);
		params.put("cdsisrol", cdsisrol);
		Map<String,Object>       procRes = ejecutaSP(new ObtenerCoberturasPlanColec(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class ObtenerCoberturasPlanColec extends StoredProcedure
	{
		protected ObtenerCoberturasPlanColec(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_COBERTURAS_COLEC");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDGARANT" , "DSGARANT" , "SWOBLIGA", "SWEDITABLE" , "SWSELECCIONADO" , "SWVISIBLE"
				};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String, String>> obtieneCobeturasNombrePlan(
			String cdramo
			,String cdtipsit
			,String cdplan
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"    , cdramo);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdplan"   , cdplan);
		Map<String,Object>       procRes = ejecutaSP(new ObtieneCobeturasNombrePlan(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class ObtieneCobeturasNombrePlan extends StoredProcedure
	{
		protected ObtieneCobeturasNombrePlan(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_COBERTURAS_NVOPLAN");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan"   , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDGARANT" , "DSGARANT" , "SWOBLIGA", "SWEDITABLE" , "SWSELECCIONADO", "DSGARANT_CORTA"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public  boolean ejectutaBloqueoProcesoTramite(
            String ntramite
           ,String claveProceso
           ,String cdrol
           ,String descripcion
           ,String valor
           ,String operacion
           )throws Exception
	{
	    Map<String,String> params = new LinkedHashMap<String,String>();
	    params.put("pv_ntramite_i"    , ntramite);
	    params.put("pv_tipoproceso_i" , claveProceso);
	    params.put("pv_cdrol_i"         , cdrol);
	    params.put("pv_descripcion_i" , descripcion);
	    params.put("pv_valor_i"       , valor);
	    params.put("pv_accion_i"      , operacion);
	    
	    ejecutaSP(new EjectutaBloqueoProcesoTramite(getDataSource()),params);
	 
	    return true;
	}
	
	@Override
    public Map<String, String> consultaBloqueoProcesoTramite(
            String ntramite
            ,String claveProceso
            )throws Exception
    {
        Map<String,String> params = new LinkedHashMap<String,String>();
        params.put("pv_ntramite_i"    , ntramite);
        params.put("pv_tipoproceso_i" , claveProceso);
        Map<String,Object>       procRes = ejecutaSP(new ConsultaBloqueoProcesoTramite(getDataSource()),params);
        List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
        
        Map<String, String> resultado =  null;
        if( lista !=null && !lista.isEmpty())
        {
            resultado  = lista.get(0);
        }else{
            resultado =  new HashMap<String, String>();
        }
        return resultado;
    }
    
    protected class ConsultaBloqueoProcesoTramite extends StoredProcedure
    {
        protected ConsultaBloqueoProcesoTramite(DataSource dataSource)
        {
            super(dataSource,"P_CONSULTA_BLOQPROCTRA");
            declareParameter(new SqlParameter("pv_ntramite_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_tipoproceso_i" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
                    "NTRAMITE" , "TIPOPROCESO" , "ROL", "DESCRIPCION" , "VALOR"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
	
	protected class EjectutaBloqueoProcesoTramite extends StoredProcedure
	{
	    protected EjectutaBloqueoProcesoTramite(DataSource dataSource)
	    {
	        super(dataSource,"P_MOV_BLOQPROCTRA");
	        declareParameter(new SqlParameter("pv_ntramite_i"   , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_tipoproceso_i" , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_cdrol_i"   , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_descripcion_i"   , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_valor_i"   , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
	        compile();
	    }
	}

	@Override
	public List<Map<String,String>> obtieneDatosContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		Map<String,Object>       procRes = ejecutaSP(new ObtieneDatosContratantePoliza(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class ObtieneDatosContratantePoliza extends StoredProcedure
	{
		protected ObtieneDatosContratantePoliza(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CONTRATANTE_POLIZA");
			declareParameter(new SqlParameter("cdunieco"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDPERSON" , "SWEXIPER"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String insercionDocumentosParametrizados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String proceso
			)throws Exception
	{
		if(!"EMISION".equals(proceso)
				&&!"ENDOSOS".equals(proceso)
				)
		{
			throw new ApplicationException("El proceso no es correcto");
		}
		
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("proceso"  , proceso);
		Map<String,Object> procRes  = ejecutaSP(new InsercionDocumentosParametrizados(getDataSource()),params);
		String             cdorddoc = (String)procRes.get("pv_cdorddoc_o");
		if(StringUtils.isBlank(cdorddoc))
		{
			throw new ApplicationException("No se gener\u00F3 el ordinal de documentos");
		}
		return cdorddoc;
	}
	
	protected class InsercionDocumentosParametrizados extends StoredProcedure
	{
		protected InsercionDocumentosParametrizados(DataSource dataSource)
		{
			super(dataSource,"P_INSERT_DOCUMENTOS");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("proceso"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdorddoc_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaCesionComision(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		ejecutaSP(new ActualizaCesionComision(getDataSource()),params);
	}
	
	protected class ActualizaCesionComision extends StoredProcedure
	{
		protected ActualizaCesionComision(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACTUALIZA_CESIONCOMISION");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void actualizaDomicilioAseguradosColectivo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdpostal
			,String cdedo
			,String cdmunici
			)throws Exception {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_nmsuplem_i" , nmsuplem);
		params.put("pv_cdpostal_i" , cdpostal);
		params.put("pv_cdedo_i"    , cdedo);
		params.put("pv_cdmunici_i" , cdmunici);
		ejecutaSP(new ActualizaDomicilioAseguradosColectivo(getDataSource()),params);
	}
	
	protected class ActualizaDomicilioAseguradosColectivo extends StoredProcedure
	{
		protected ActualizaDomicilioAseguradosColectivo(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACT_TVALOSIT_ATRIB_DOMICILIO");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpostal_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdedo_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdmunici_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoMsupleme(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			Date feinival,
			String hhinival,
			Date fefinval,
			String hhfinval,
			String swanula,
			String nsuplogi,
			String nsupusua,
			String nsupsess,
			String fesessio,
			String swconfir,
			String nmrenova,
			String nsuplori,
			String cdorddoc,
			String swpolfro,
			String pocofron,
			String swpoldec,
			String tippodec,
			String accion) throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_estado_i"  , estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_nmsuplem_i", nmsuplem);
		params.put("pv_feinival_i", feinival);
		params.put("pv_hhinival_i", hhinival);
		params.put("pv_fefinval_i", fefinval);
		params.put("pv_hhfinval_i", hhfinval);
		params.put("pv_swanula_i" , swanula);
		params.put("pv_nsuplogi_i", nsuplogi);
		params.put("pv_nsupusua_i", nsupusua);
		params.put("pv_nsupsess_i", nsupsess);
		params.put("pv_fesessio_i", fesessio);
		params.put("pv_swconfir_i", swconfir);
		params.put("pv_nmrenova_i", nmrenova);
		params.put("pv_nsuplori_i", nsuplori);
		params.put("pv_cdorddoc_i", cdorddoc);
		params.put("pv_swpolfro_i", swpolfro);
		params.put("pv_pocofron_i", pocofron);
		params.put("pv_swpoldec_i", swpoldec);
		params.put("pv_tippodec_i", tippodec);
		params.put("pv_accion_i"  , accion);
		ejecutaSP(new MovimientoMsupleme(getDataSource()),params);
	}
	
	protected class MovimientoMsupleme extends StoredProcedure {
		protected MovimientoMsupleme(DataSource dataSource) {
			super(dataSource, "PKG_SATELITES.P_MOV_MSUPLEME");
			
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feinival_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_hhinival_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fefinval_i", OracleTypes.DATE));
			declareParameter(new SqlParameter("pv_hhfinval_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swanula_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsuplogi_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsupusua_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsupsess_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fesessio_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swconfir_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmrenova_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nsuplori_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdorddoc_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpolfro_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_pocofron_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpoldec_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tippodec_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
		}
	}
    
	
	@Override
	public void procesaIncisoDefinitivo(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac,
			String cdelement, String cdperson, String cdasegur, String cdplan, String cdperpag) throws Exception {
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco", cdunieco);
		params.put("pv_cdramo"  , cdramo);
		params.put("pv_estado"  , estado);
		params.put("pv_nmpoliza", nmpoliza);
		params.put("pv_nmsituac", nmsituac);
		params.put("pv_cdelement",cdelement);
		params.put("pv_cdperson", cdperson);
		params.put("pv_cdasegur", cdasegur);
		params.put("pv_cdplan",   cdplan);
		params.put("pv_cdperpag", cdperpag);
		ejecutaSP(new ProcesaIncisoDefinitivoSP(getDataSource()),params);
	}
	
	protected class ProcesaIncisoDefinitivoSP extends StoredProcedure {
    	protected ProcesaIncisoDefinitivoSP(DataSource dataSource) {
            super(dataSource,"PKG_COTIZA.P_PROC_INCISO_DEF");
            declareParameter(new SqlParameter("pv_cdunieco",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdelement", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperson",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdasegur",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdplan",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdperpag",  OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",	OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",	OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>> recuperarListaDocumentosParametrizados(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdorddoc" , cdorddoc);
		params.put("nmsolici" , nmsolici);
		params.put("ntramite" , ntramite);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarListaDocumentosParametrizados(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "pkg_imp_document.p_imp_doc", params, lista);
		return lista;
	}
	
	protected class RecuperarListaDocumentosParametrizados extends StoredProcedure
	{
		protected RecuperarListaDocumentosParametrizados(DataSource dataSource)
		{
			super(dataSource,"pkg_imp_document.p_imp_doc");
			declareParameter(new SqlParameter("cdorddoc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsolici" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"DSMODDOC","C_COMMAND","NOM_PDF"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> generarDocumentosBaseDatos(
			String cdorddoc
			,String nmsolici
			,String ntramite
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdorddoc" , cdorddoc);
		params.put("nmsolici" , nmsolici);
		params.put("ntramite" , ntramite);
		Map<String,Object>       procRes = ejecutaSP(new GenerarDocumentosBaseDatos(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			throw new ApplicationException("El movimiento no gener\u00F3 documentos");
		}
		logger.debug(Utils.log("\n****** pkg_db_report.set_data_report Lista de documentos a transferir=",lista));
		return lista;
	}
	
	protected class GenerarDocumentosBaseDatos extends StoredProcedure
	{
		protected GenerarDocumentosBaseDatos(DataSource dataSource)
		{
			super(dataSource,"pkg_db_report.set_data_report");
			declareParameter(new SqlParameter("cdorddoc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsolici" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDUNIECO"  , "CDRAMO"    , "ESTADO"   , "NMPOLIZA" , "NMSOLICI"
					,"NMSUPLEM" , "NTRAMITE"  , "FEINICI"  , "CDDOCUME" , "DSDOCUME"
					,"TIPMOV"   , "SWVISIBLE" , "CDTIPTRA" , "CODIDOCU" , "FEFECHA" , "CDORDDOC"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public Map<String, String> obtieneValidacionRetroactividad(String numSerie,
			String feini) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getActualizaCuadroComision(Map<String, Object> params) throws Exception
	{
		ejecutaSP(new GetActualizaCuadroComision(this.getDataSource()), params);
	}
	
	protected class GetActualizaCuadroComision extends StoredProcedure
	{
		protected GetActualizaCuadroComision(DataSource dataSource)
		{
			super(dataSource, "PKG_SATELITES2.P_ACTUALIZA_CUADROCOMISION");
			declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmcuadro_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void eliminarMpolirec(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		ejecutaSP(new EliminarMpolirecSP(getDataSource()),params);
	}
	
	protected class EliminarMpolirecSP extends StoredProcedure
	{
		protected EliminarMpolirecSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ELIMINA_MPOLIREC");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizarFefecsitMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		ejecutaSP(new ActualizarFefecsitMpolisitSP(getDataSource()),params);
	}
	
	protected class ActualizarFefecsitMpolisitSP extends StoredProcedure
	{
		protected ActualizarFefecsitMpolisitSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACTUALIZA_FEFECSIT_MPOLISIT");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarPantallaActTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String otvalor01,String otvalor02,String otvalor03,String otvalor04,String otvalor05,String otvalor06,String otvalor07,String otvalor08,String otvalor09,String otvalor10
			,String otvalor11,String otvalor12,String otvalor13,String otvalor14,String otvalor15,String otvalor16,String otvalor17,String otvalor18,String otvalor19,String otvalor20
			,String otvalor21,String otvalor22,String otvalor23,String otvalor24,String otvalor25,String otvalor26,String otvalor27,String otvalor28,String otvalor29,String otvalor30
			,String otvalor31,String otvalor32,String otvalor33,String otvalor34,String otvalor35,String otvalor36,String otvalor37,String otvalor38,String otvalor39,String otvalor40
			,String otvalor41,String otvalor42,String otvalor43,String otvalor44,String otvalor45,String otvalor46,String otvalor47,String otvalor48,String otvalor49,String otvalor50
			,String otvalor51,String otvalor52,String otvalor53,String otvalor54,String otvalor55,String otvalor56,String otvalor57,String otvalor58,String otvalor59,String otvalor60
			,String otvalor61,String otvalor62,String otvalor63,String otvalor64,String otvalor65,String otvalor66,String otvalor67,String otvalor68,String otvalor69,String otvalor70
			,String otvalor71,String otvalor72,String otvalor73,String otvalor74,String otvalor75,String otvalor76,String otvalor77,String otvalor78,String otvalor79,String otvalor80
			,String otvalor81,String otvalor82,String otvalor83,String otvalor84,String otvalor85,String otvalor86,String otvalor87,String otvalor88,String otvalor89,String otvalor90
			,String otvalor91,String otvalor92,String otvalor93,String otvalor94,String otvalor95,String otvalor96,String otvalor97,String otvalor98,String otvalor99
			,String swvalor01,String swvalor02,String swvalor03,String swvalor04,String swvalor05,String swvalor06,String swvalor07,String swvalor08,String swvalor09,String swvalor10
			,String swvalor11,String swvalor12,String swvalor13,String swvalor14,String swvalor15,String swvalor16,String swvalor17,String swvalor18,String swvalor19,String swvalor20
			,String swvalor21,String swvalor22,String swvalor23,String swvalor24,String swvalor25,String swvalor26,String swvalor27,String swvalor28,String swvalor29,String swvalor30
			,String swvalor31,String swvalor32,String swvalor33,String swvalor34,String swvalor35,String swvalor36,String swvalor37,String swvalor38,String swvalor39,String swvalor40
			,String swvalor41,String swvalor42,String swvalor43,String swvalor44,String swvalor45,String swvalor46,String swvalor47,String swvalor48,String swvalor49,String swvalor50
			,String swvalor51,String swvalor52,String swvalor53,String swvalor54,String swvalor55,String swvalor56,String swvalor57,String swvalor58,String swvalor59,String swvalor60
			,String swvalor61,String swvalor62,String swvalor63,String swvalor64,String swvalor65,String swvalor66,String swvalor67,String swvalor68,String swvalor69,String swvalor70
			,String swvalor71,String swvalor72,String swvalor73,String swvalor74,String swvalor75,String swvalor76,String swvalor77,String swvalor78,String swvalor79,String swvalor80
			,String swvalor81,String swvalor82,String swvalor83,String swvalor84,String swvalor85,String swvalor86,String swvalor87,String swvalor88,String swvalor89,String swvalor90
			,String swvalor91,String swvalor92,String swvalor93,String swvalor94,String swvalor95,String swvalor96,String swvalor97,String swvalor98,String swvalor99
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("otvalor01", otvalor01);
		params.put("otvalor02", otvalor02);
		params.put("otvalor03", otvalor03);
		params.put("otvalor04", otvalor04);
		params.put("otvalor05", otvalor05);
		params.put("otvalor06", otvalor06);
		params.put("otvalor07", otvalor07);
		params.put("otvalor08", otvalor08);
		params.put("otvalor09", otvalor09);
		params.put("otvalor10", otvalor10);
		params.put("otvalor11", otvalor11);
		params.put("otvalor12", otvalor12);
		params.put("otvalor13", otvalor13);
		params.put("otvalor14", otvalor14);
		params.put("otvalor15", otvalor15);
		params.put("otvalor16", otvalor16);
		params.put("otvalor17", otvalor17);
		params.put("otvalor18", otvalor18);
		params.put("otvalor19", otvalor19);
		params.put("otvalor20", otvalor20);
		params.put("otvalor21", otvalor21);
		params.put("otvalor22", otvalor22);
		params.put("otvalor23", otvalor23);
		params.put("otvalor24", otvalor24);
		params.put("otvalor25", otvalor25);
		params.put("otvalor26", otvalor26);
		params.put("otvalor27", otvalor27);
		params.put("otvalor28", otvalor28);
		params.put("otvalor29", otvalor29);
		params.put("otvalor30", otvalor30);
		params.put("otvalor31", otvalor31);
		params.put("otvalor32", otvalor32);
		params.put("otvalor33", otvalor33);
		params.put("otvalor34", otvalor34);
		params.put("otvalor35", otvalor35);
		params.put("otvalor36", otvalor36);
		params.put("otvalor37", otvalor37);
		params.put("otvalor38", otvalor38);
		params.put("otvalor39", otvalor39);
		params.put("otvalor40", otvalor40);
		params.put("otvalor41", otvalor41);
		params.put("otvalor42", otvalor42);
		params.put("otvalor43", otvalor43);
		params.put("otvalor44", otvalor44);
		params.put("otvalor45", otvalor45);
		params.put("otvalor46", otvalor46);
		params.put("otvalor47", otvalor47);
		params.put("otvalor48", otvalor48);
		params.put("otvalor49", otvalor49);
		params.put("otvalor50", otvalor50);
		params.put("otvalor51", otvalor51);
		params.put("otvalor52", otvalor52);
		params.put("otvalor53", otvalor53);
		params.put("otvalor54", otvalor54);
		params.put("otvalor55", otvalor55);
		params.put("otvalor56", otvalor56);
		params.put("otvalor57", otvalor57);
		params.put("otvalor58", otvalor58);
		params.put("otvalor59", otvalor59);
		params.put("otvalor60", otvalor60);
		params.put("otvalor61", otvalor61);
		params.put("otvalor62", otvalor62);
		params.put("otvalor63", otvalor63);
		params.put("otvalor64", otvalor64);
		params.put("otvalor65", otvalor65);
		params.put("otvalor66", otvalor66);
		params.put("otvalor67", otvalor67);
		params.put("otvalor68", otvalor68);
		params.put("otvalor69", otvalor69);
		params.put("otvalor70", otvalor70);
		params.put("otvalor71", otvalor71);
		params.put("otvalor72", otvalor72);
		params.put("otvalor73", otvalor73);
		params.put("otvalor74", otvalor74);
		params.put("otvalor75", otvalor75);
		params.put("otvalor76", otvalor76);
		params.put("otvalor77", otvalor77);
		params.put("otvalor78", otvalor78);
		params.put("otvalor79", otvalor79);
		params.put("otvalor80", otvalor80);
		params.put("otvalor81", otvalor81);
		params.put("otvalor82", otvalor82);
		params.put("otvalor83", otvalor83);
		params.put("otvalor84", otvalor84);
		params.put("otvalor85", otvalor85);
		params.put("otvalor86", otvalor86);
		params.put("otvalor87", otvalor87);
		params.put("otvalor88", otvalor88);
		params.put("otvalor89", otvalor89);
		params.put("otvalor90", otvalor90);
		params.put("otvalor91", otvalor91);
		params.put("otvalor92", otvalor92);
		params.put("otvalor93", otvalor93);
		params.put("otvalor94", otvalor94);
		params.put("otvalor95", otvalor95);
		params.put("otvalor96", otvalor96);
		params.put("otvalor97", otvalor97);
		params.put("otvalor98", otvalor98);
		params.put("otvalor99", otvalor99);
		params.put("swvalor01", swvalor01);
		params.put("swvalor02", swvalor02);
		params.put("swvalor03", swvalor03);
		params.put("swvalor04", swvalor04);
		params.put("swvalor05", swvalor05);
		params.put("swvalor06", swvalor06);
		params.put("swvalor07", swvalor07);
		params.put("swvalor08", swvalor08);
		params.put("swvalor09", swvalor09);
		params.put("swvalor10", swvalor10);
		params.put("swvalor11", swvalor11);
		params.put("swvalor12", swvalor12);
		params.put("swvalor13", swvalor13);
		params.put("swvalor14", swvalor14);
		params.put("swvalor15", swvalor15);
		params.put("swvalor16", swvalor16);
		params.put("swvalor17", swvalor17);
		params.put("swvalor18", swvalor18);
		params.put("swvalor19", swvalor19);
		params.put("swvalor20", swvalor20);
		params.put("swvalor21", swvalor21);
		params.put("swvalor22", swvalor22);
		params.put("swvalor23", swvalor23);
		params.put("swvalor24", swvalor24);
		params.put("swvalor25", swvalor25);
		params.put("swvalor26", swvalor26);
		params.put("swvalor27", swvalor27);
		params.put("swvalor28", swvalor28);
		params.put("swvalor29", swvalor29);
		params.put("swvalor30", swvalor30);
		params.put("swvalor31", swvalor31);
		params.put("swvalor32", swvalor32);
		params.put("swvalor33", swvalor33);
		params.put("swvalor34", swvalor34);
		params.put("swvalor35", swvalor35);
		params.put("swvalor36", swvalor36);
		params.put("swvalor37", swvalor37);
		params.put("swvalor38", swvalor38);
		params.put("swvalor39", swvalor39);
		params.put("swvalor40", swvalor40);
		params.put("swvalor41", swvalor41);
		params.put("swvalor42", swvalor42);
		params.put("swvalor43", swvalor43);
		params.put("swvalor44", swvalor44);
		params.put("swvalor45", swvalor45);
		params.put("swvalor46", swvalor46);
		params.put("swvalor47", swvalor47);
		params.put("swvalor48", swvalor48);
		params.put("swvalor49", swvalor49);
		params.put("swvalor50", swvalor50);
		params.put("swvalor51", swvalor51);
		params.put("swvalor52", swvalor52);
		params.put("swvalor53", swvalor53);
		params.put("swvalor54", swvalor54);
		params.put("swvalor55", swvalor55);
		params.put("swvalor56", swvalor56);
		params.put("swvalor57", swvalor57);
		params.put("swvalor58", swvalor58);
		params.put("swvalor59", swvalor59);
		params.put("swvalor60", swvalor60);
		params.put("swvalor61", swvalor61);
		params.put("swvalor62", swvalor62);
		params.put("swvalor63", swvalor63);
		params.put("swvalor64", swvalor64);
		params.put("swvalor65", swvalor65);
		params.put("swvalor66", swvalor66);
		params.put("swvalor67", swvalor67);
		params.put("swvalor68", swvalor68);
		params.put("swvalor69", swvalor69);
		params.put("swvalor70", swvalor70);
		params.put("swvalor71", swvalor71);
		params.put("swvalor72", swvalor72);
		params.put("swvalor73", swvalor73);
		params.put("swvalor74", swvalor74);
		params.put("swvalor75", swvalor75);
		params.put("swvalor76", swvalor76);
		params.put("swvalor77", swvalor77);
		params.put("swvalor78", swvalor78);
		params.put("swvalor79", swvalor79);
		params.put("swvalor80", swvalor80);
		params.put("swvalor81", swvalor81);
		params.put("swvalor82", swvalor82);
		params.put("swvalor83", swvalor83);
		params.put("swvalor84", swvalor84);
		params.put("swvalor85", swvalor85);
		params.put("swvalor86", swvalor86);
		params.put("swvalor87", swvalor87);
		params.put("swvalor88", swvalor88);
		params.put("swvalor89", swvalor89);
		params.put("swvalor90", swvalor90);
		params.put("swvalor91", swvalor91);
		params.put("swvalor92", swvalor92);
		params.put("swvalor93", swvalor93);
		params.put("swvalor94", swvalor94);
		params.put("swvalor95", swvalor95);
		params.put("swvalor96", swvalor96);
		params.put("swvalor97", swvalor97);
		params.put("swvalor98", swvalor98);
		params.put("swvalor99", swvalor99);
		ejecutaSP(new GuardarPantallaActTvalositSP(getDataSource()),params);
	}
	
	protected class GuardarPantallaActTvalositSP extends StoredProcedure
	{
		protected GuardarPantallaActTvalositSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_ACT_VALOSIT_SEGUN_SWITCH");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor01" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor02" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor03" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor04" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor05" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor06" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor07" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor08" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor09" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor40" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor41" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor42" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor43" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor44" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor45" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor46" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor47" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor48" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor49" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor50" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor51" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor52" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor53" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor54" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor55" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor56" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor57" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor58" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor59" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor60" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor61" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor62" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor63" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor64" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor65" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor66" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor67" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor68" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor69" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor70" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor71" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor72" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor73" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor74" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor75" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor76" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor77" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor78" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor79" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor80" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor81" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor82" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor83" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor84" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor85" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor86" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor87" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor88" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor89" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor90" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor91" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor92" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor93" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor94" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor95" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor96" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor97" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor98" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor99" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor01" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor02" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor03" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor04" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor05" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor06" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor07" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor08" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor09" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor40" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor41" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor42" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor43" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor44" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor45" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor46" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor47" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor48" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor49" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor50" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor51" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor52" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor53" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor54" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor55" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor56" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor57" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor58" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor59" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor60" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor61" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor62" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor63" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor64" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor65" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor66" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor67" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor68" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor69" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor70" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor71" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor72" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor73" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor74" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor75" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor76" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor77" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor78" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor79" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor80" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor81" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor82" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor83" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor84" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor85" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor86" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor87" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor88" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor89" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor90" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor91" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor92" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor93" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor94" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor95" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor96" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor97" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor98" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swvalor99" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void borrarIncisoCotizacion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		Map<String,Object> procRes = ejecutaSP(new BorrarIncisoCotizacionSP(getDataSource()),params);
		String error = (String)procRes.get("pv_error_o");
		if(StringUtils.isNotBlank(error))
		{
			throw new ApplicationException(error);
		}
	}
	
	protected class BorrarIncisoCotizacionSP extends StoredProcedure
	{
		protected BorrarIncisoCotizacionSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_BORRA_INCISO_COTIZACION");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_error_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void reenumerarSituaciones(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		ejecutaSP(new ReenumerarSituacionesSP(getDataSource()),params);
	}
	
	protected class ReenumerarSituacionesSP extends StoredProcedure
	{
		protected ReenumerarSituacionesSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_RENUMERA_NMSITUAC");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public boolean isEstatusGeneraDocumentosCotizacion(String status) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("status" , status);
		Map<String,Object> procRes = ejecutaSP(new IsEstatusGeneraDocumentosCotizacionSP(getDataSource()),params);
		String conteo = (String)procRes.get("pv_conteo_o");
		logger.debug(Utils.log("conteo=",conteo));
		return Integer.parseInt(conteo)>0;
	}
	
	protected class IsEstatusGeneraDocumentosCotizacionSP extends StoredProcedure
	{
		protected IsEstatusGeneraDocumentosCotizacionSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_STATUS_GENERAN_COTI");
			declareParameter(new SqlParameter("status" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_conteo_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarCensoCompletoMultisaludEndoso(
			String nombreArchivo
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdestadoCli
			,String cdmuniciCli
			,String cdplan1
			,String cdplan2
			,String cdplan3
			,String cdplan4
			,String cdplan5
			,String complemento
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("censo"       , nombreArchivo);
		params.put("cdunieco"    , cdunieco);
		params.put("cdramo"      , cdramo);
		params.put("estado"      , estado);
		params.put("nmpoliza"    , nmpoliza);
		params.put("cdestado"    , cdestadoCli);
		params.put("cdmunici"    , cdmuniciCli);
		params.put("cdplan1"     , cdplan1);
		params.put("cdplan2"     , cdplan2);
		params.put("cdplan3"     , cdplan3);
		params.put("cdplan4"     , cdplan4);
		params.put("cdplan5"     , cdplan5);
		params.put("complemento" , complemento);
		params.put("nmsuplem"    , nmsuplem);
		ejecutaSP(new GuardarCensoCompletoMultisaludEndoso(getDataSource()),params);
	}
	
	protected class GuardarCensoCompletoMultisaludEndoso extends StoredProcedure
	{
		protected GuardarCensoCompletoMultisaludEndoso(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_LAYOUT_CENSO_MS_COLEC_DEFEND");
			declareParameter(new SqlParameter("censo"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestado"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmunici"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan1"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan2"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan3"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan4"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan5"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("complemento" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void restaurarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		ejecutaSP(new RestaurarRespaldoCensoSP(getDataSource()),params);
	}
	
	protected class RestaurarRespaldoCensoSP extends StoredProcedure
	{
		protected RestaurarRespaldoCensoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_LAYOUT_REESTABLECE_CENSO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void borrarRespaldoCenso(
			String cdunieco
			,String cdramo
			,String nmpoliza
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("nmpoliza" , nmpoliza);
		ejecutaSP(new BorrarRespaldoCensoSP(getDataSource()),params);
	}
	
	protected class BorrarRespaldoCensoSP extends StoredProcedure
	{
		protected BorrarRespaldoCensoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_EMISION.P_LIMPIEZA_RESPALDOS_EMISION");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
    @Override
	public void insertaMorbilidad(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem) throws Exception {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("nmsuplem" , nmsuplem);
    	ejecutaSP(new InsertaMorbilidadSP(getDataSource()),params);
	}
    
    protected class InsertaMorbilidadSP extends StoredProcedure {
    	protected InsertaMorbilidadSP(DataSource dataSource) {
    		super(dataSource,"P_INSERTA_MORBILIDAD");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
	@Override
	public void guardarLayoutGenerico(
			String nombreArchivo
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pi_filename"       , nombreArchivo);
		ejecutaSP(new GuardarLayoutGenerico(getDataSource()),params);
	}
	
	protected class GuardarLayoutGenerico extends StoredProcedure
	{
		protected GuardarLayoutGenerico(DataSource dataSource)
		{
			super(dataSource,"PKG_NOVA.P_CONF_LAYOU_SINIESTRO");
			declareParameter(new SqlParameter("pi_filename"       , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTbloqueo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("accion"   , accion);
		ejecutaSP(new MovimientoTbloqueoSP(getDataSource()),params);
	}
	
	protected class MovimientoTbloqueoSP extends StoredProcedure
	{
		protected MovimientoTbloqueoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_TBLOQUEO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void insertaRegistroInfoCenso (List<Map<String, String>> lista) throws Exception {
	    String[][] array = new String[lista.size()][];
        
        int i = 0;
        for (Map<String,String> permiso : lista) {
            array[i++] = new String[]{
                    permiso.get("pv_cdunieco_i"),
                    permiso.get("pv_cdramo_i"),
                    permiso.get("pv_estado_i"),
                    permiso.get("pv_nmpoliza_i"),
                    permiso.get("pv_cdgrupo_i"),
                    permiso.get("pv_parentesco_i"),
                    permiso.get("pv_dsapellido_i"),
                    permiso.get("pv_dsapellido1_i"),
                    permiso.get("pv_dsnombre_i"),
                    permiso.get("pv_dsnombre1_i"),
                    permiso.get("pv_otsexo_i"),
                    permiso.get("pv_fenacimi_i"),
                    permiso.get("pv_cdpostal_i"),
                    permiso.get("pv_dsestado_i"),
                    permiso.get("pv_dsmunicipio_i"),
                    permiso.get("pv_dscolonia_i"),
                    permiso.get("pv_dsdomici_i"),
                    permiso.get("pv_nmnumero_i"),
                    permiso.get("pv_nmnumint_i"),
                    permiso.get("pv_cdrfc_i"),
                    permiso.get("pv_dsemail_i"),
                    permiso.get("pv_nmtelefo_i"),
                    permiso.get("pv_identidad_i"),
                    permiso.get("pv_fecantig_i"),
                    permiso.get("pv_expocupacion_i"),
                    permiso.get("pv_peso_i"),
                    permiso.get("pv_estatura_i"),
                    permiso.get("pv_expsobrepeso_i"),
                    permiso.get("pv_edocivil_i"),
                    permiso.get("pv_feingresoempleo_i"),
                    permiso.get("pv_plaza_i"),
                    permiso.get("pv_idasegurado_i")
            };
        }
        
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("array" , new SqlArrayValue(array));
		ejecutaSP(new InsertaReistroInfoCenso(getDataSource()), params);
	}
	
	protected class InsertaReistroInfoCenso extends StoredProcedure
	{
		protected InsertaReistroInfoCenso(DataSource dataSource)
		{
			super(dataSource,"PKG_TRAD.P_INS_ZWCENSOTRAD_REG_BUENO");
			/*declareParameter(new SqlParameter("pv_cdunieco_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgrupo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_parentesco_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsapellido_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsapellido1_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnombre_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsnombre1_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otsexo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fenacimi_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdpostal_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsestado_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsmunicipio_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dscolonia_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsdomici_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmnumero_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmnumint_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrfc_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsemail_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmtelefo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_identidad_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecantig_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_expocupacion_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_peso_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estatura_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_expsobrepeso_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_edocivil_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_feingresoempleo_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_plaza_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_idasegurado_i"    , OracleTypes.VARCHAR));
			*/
			declareParameter(new SqlParameter("array"    , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String consultaExtraprimOcup(String cdtipsit) throws Exception {
		
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdtipsit_i" , cdtipsit);
		Map<String, Object> resultado = ejecutaSP(new BuscaExtraprimOcup(getDataSource()), params);
		String otvalor = (String) resultado.get("pv_cdatribu_o");
		return otvalor; 
	}
	
	protected class BuscaExtraprimOcup extends StoredProcedure {
		protected BuscaExtraprimOcup(DataSource dataSource) {
			super(dataSource,"PKG_CONS_INCISOS.P_GET_EXTRAPRIMOCUP");
			declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdatribu_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosExtraprimas(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		params.put("start"    , start);
		params.put("limit"    , limit);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarAseguradosExtraprimasPag(getDataSource()),params);
		List<Map<String,String>>situaciones=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(situaciones==null||situaciones.size()==0)
		{
			throw new Exception("No hay situaciones para el grupo");
		}
		Map<String,String> total = new HashMap<String,String>();
    	total.put("total", (String) procResult.get("pv_num_o"));
    	situaciones.add(total);
    	logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO ******")
				.append("\n****** total=").append((String) procResult.get("pv_num_o"))
				.append("\n*******************************************************")
				.toString()
				);
		return situaciones;
	}
	
	protected class CargarAseguradosExtraprimasPag extends StoredProcedure
	{
		protected CargarAseguradosExtraprimasPag(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("start"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("limit"    , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
						"NOMBRE"   , "NMSITUAC"   , "EDAD"             , "FENACIMI" 
	                    ,"SEXO"    , "PARENTESCO" , "OCUPACION"        , "EXTPRI_OCUPACION"
	                    ,"PESO"    , "ESTATURA"   , "EXTPRI_SOBREPESO" , "FAMILIA"
	                    ,"TITULAR"
					};
			declareParameter(new SqlOutParameter("pv_num_o"   	 , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarSituacionesGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		params.put("start"    , start);
		params.put("limit"    , limit);
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO_DINAM ******")
				.append("\n****** params=").append(params)
				.append("\n*******************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarSituacionesGrupoPag(getDataSource()),params);
		List<Map<String,String>>situaciones=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(situaciones==null||situaciones.size()==0)
		{
			throw new Exception("No hay situaciones para el grupo");
		}
		Map<String,String> total = new HashMap<String,String>();
    	total.put("total", (String) procResult.get("pv_num_o"));
    	situaciones.add(total);
    	logger.debug(
				new StringBuilder()
				.append("\n*******************************************************")
				.append("\n****** PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO_DINAM ******")
				.append("\n****** total=").append((String) procResult.get("pv_num_o"))
				.append("\n*******************************************************")
				.toString()
				);
		return situaciones;
	}
	
	protected class CargarSituacionesGrupoPag extends StoredProcedure
	{
		protected CargarSituacionesGrupoPag(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TVALOSIT_X_GRUPO_DINAM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("start"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("limit"    , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
					"nombre"
					,"nmsituac"
					,"familia"
					,"titular"
					,"parentesco"
					,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09","otvalor10"
					,"otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19","otvalor20"
					,"otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29","otvalor30"
					,"otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39","otvalor40"
					,"otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49","otvalor50"
					,"otvalor51","otvalor52","otvalor53","otvalor54","otvalor55","otvalor56","otvalor57","otvalor58","otvalor59","otvalor60"
					,"otvalor61","otvalor62","otvalor63","otvalor64","otvalor65","otvalor66","otvalor67","otvalor68","otvalor69","otvalor70"
					,"otvalor71","otvalor72","otvalor73","otvalor74","otvalor75","otvalor76","otvalor77","otvalor78","otvalor79","otvalor80"
					,"otvalor81","otvalor82","otvalor83","otvalor84","otvalor85","otvalor86","otvalor87","otvalor88","otvalor89","otvalor90"
					,"otvalor91","otvalor92","otvalor93","otvalor94","otvalor95","otvalor96","otvalor97","otvalor98","otvalor99","cdatexoc"
					};
			declareParameter(new SqlOutParameter("pv_num_o"   	 , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaValoresSituacion(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			List<Map<String,String>> valores)throws Exception{
		logger.debug(
				new StringBuilder()
				.append("\n******************************************************")
				.append("\n****** pkg_procs_cartera.blk_update_tvs ******")
				.append("\n****** cdunieco=").append(cdunieco)
				.append("\n****** cdramo=").append(cdramo)
				.append("\n****** estado=").append(estado)
				.append("\n****** nmpoliza=").append(nmpoliza)
				.append("\n****** nmsuplem=").append(nmsuplem)
				.append("\n****** valores=").append(valores)
				.append("\n******************************************************")
				.toString()
				);
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		String[][] array = new String[valores.size()][];		
		int i = 0;
		for(Map<String,String> valor : valores){
			ArrayList<String> otvalores = new ArrayList<String>(0);			
			otvalores.add(valor.get("nmsituac"));
			for(int j = 1; j <= 99; j++){
				otvalores.add(valor.get("otvalor"+((j < 10 ? "0" : "") + j)));
			}
			array[i++] = otvalores.toArray(new String[otvalores.size()]);
		}
		params.put("cdunieco", cdunieco);
		params.put("cdramo", cdramo);
		params.put("estado", estado);
		params.put("nmpoliza", nmpoliza);
		params.put("nmsuplem", nmsuplem);
		params.put("array" , new SqlArrayValue(array));
		ejecutaSP(new ActualizaValoresSituacionBC(getDataSource()),params);
	}
	
	protected class ActualizaValoresSituacionBC extends StoredProcedure
	{
		protected ActualizaValoresSituacionBC(DataSource dataSource)
		{
			super(dataSource,"pkg_procs_cartera.blk_update_tvs");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"	  , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("array"    , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> actualizaValoresSituacionTitulares(
			String cdunieco,
			String cdramo,
			String estado,
			String nmpoliza,
			String nmsuplem,
			String cdtipsit,
			String valor,
			String cdgrupo)throws Exception{
		logger.debug(
				new StringBuilder()
				.append("\n******************************************************")
				.append("\n****** pkg_procs_cartera.p_actualiza_tvalosit_titulares ******")
				.append("\n****** cdunieco=").append(cdunieco)
				.append("\n****** cdramo=").append(cdramo)
				.append("\n****** estado=").append(estado)
				.append("\n****** nmpoliza=").append(nmpoliza)
				.append("\n****** nmsuplem=").append(nmsuplem)
				.append("\n****** cdtipsit=").append(cdtipsit)
				.append("\n****** valor=").append(valor)
				.append("\n****** cdgrupo=").append(cdgrupo)
				.append("\n******************************************************")
				.toString()
				);
		Map<String,Object> params = new LinkedHashMap<String,Object>();		
		params.put("cdunieco", cdunieco);
		params.put("cdramo"  , cdramo);
		params.put("estado"  , estado);
		params.put("nmpoliza", nmpoliza);
		params.put("nmsuplem", nmsuplem);
		params.put("cdtipsit", cdtipsit);
		params.put("valor"   , Integer.parseInt(valor));
		params.put("cdgrupo" , cdgrupo);
		Map<String,Object>     procResult = ejecutaSP(new ActualizaValoresSituacionTitulares(getDataSource()), params);
		List<Map<String,String>> listaDatos = (List<Map<String,String>>)procResult.get("pv_registro_o");
		logger.debug(Utils.log("listaDatos",listaDatos));
		if(listaDatos==null||listaDatos.size()==0)
		{
			throw new Exception("No se pudo cargar la poliza");
		}
		return listaDatos;
	}
	
	protected class ActualizaValoresSituacionTitulares extends StoredProcedure
	{
		protected ActualizaValoresSituacionTitulares(DataSource dataSource)
		{
			super(dataSource,"pkg_procs_cartera.p_actualiza_tvalosit_titulares");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"	  , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("valor"     , OracleTypes.NUMBER));
			declareParameter(new SqlParameter("cdgrupo"   , OracleTypes.VARCHAR));
			String[] cols = new String[]{"NMSITUAC"};
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaNmsituaextMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac 
			,String nmsuplem
			,String nmsituaext
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco"   , cdunieco);
		params.put("cdramo"     , cdramo);
		params.put("estado"     , estado);
		params.put("nmpoliza"   , nmpoliza);
		params.put("nmsituac"   , nmsituac);
		params.put("nmsuplem"   , nmsuplem);
		params.put("nmsituaext" , nmsituaext);
		ejecutaSP(new ActualizaNmsituaextMpolisitSP(getDataSource()),params);
	}
	
	protected class ActualizaNmsituaextMpolisitSP extends StoredProcedure
	{
		protected ActualizaNmsituaextMpolisitSP(DataSource dataSource)
		{
			super(dataSource,"P_ACT_NMSITUAEXT_MPOLISIT");
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"	    , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituaext"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaDatosMpersona(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdestciv
			,String ocup
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco", cdunieco);
		params.put("cdramo", cdramo);
		params.put("estado"  , estado);
		params.put("nmpoliza", nmpoliza);
		params.put("nmsituac", nmsituac);
		params.put("nmsuplem", nmsuplem);
		params.put("cdestciv", cdestciv);
		params.put("ocup"    , ocup);
		ejecutaSP(new ActualizaDatosMpersonaSP(getDataSource()),params);
	}
	
	protected class ActualizaDatosMpersonaSP extends StoredProcedure
	{
		protected ActualizaDatosMpersonaSP(DataSource dataSource)
		{
			super(dataSource,"P_ACT_DAT_MPERSONA");
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"	    , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("nmpoliza"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestciv"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ocup"        , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public String validandoCoberturasPLan(
			  String cdunieco
     		 ,String cdramo
     		 ,String estado //estado
     		 ,String nmpoliza
     		 ,String nmsuplem)throws Exception{
		
		logger.debug(
				new StringBuilder()
				.append("\n**************JOHAN PROCEDURE*********************************")
				.append("\n************* PROCEDURE : P_COBERTURAS_PAQUETEO **************")
				.append("\n****** cdunieco=").append(cdunieco)
				.append("\n****** cdramo=").append(cdramo)
				.append("\n****** estado=").append(estado)
				.append("\n****** nmpoliza=").append(nmpoliza)
				.append("\n****** nmsuplem=").append(nmsuplem)
				.append("\n******************************************************")
				.toString()
				);
		String mensaje = "";
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();		
		params.put("cdunieco", cdunieco);
		params.put("cdramo",   cdramo);
		params.put("estado",   estado);
		params.put("nmpoliza", nmpoliza);
		params.put("nmsuplem", nmsuplem);
		Map<String,Object> procResult = ejecutaSP(new validandoCoberturasPLan(getDataSource()), params);
		
		Map<String, String> newMap = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Map<String, Object> intermediate = (Map)Collections.checkedMap(newMap, String.class, String.class);
		intermediate.putAll(procResult);
		
		if(StringUtils.isNotBlank(newMap.get("pv_bandera_o")) && newMap.get("pv_bandera_o").contains("1"))
		{
			mensaje = newMap.get("pv_title_o");
			return mensaje;
		}
		else 
		{
			//return false;
			return "";
		}
	}
	
	protected class validandoCoberturasPLan extends StoredProcedure
	{
		protected validandoCoberturasPLan(DataSource dataSource)
		{
			super(dataSource,"P_COBERTURAS_PAQUETE");
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"	  , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_bandera_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String[] obtenerCorreosReportarIncidenciasPorTipoSituacion(
			String ramo
			)throws Exception,Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("PV_CDRAMO_I" , ramo);
		
		logger.debug(
				new StringBuilder()
				.append("\n*****************************************")
				.append("\n**** P_OBTENER_CORREOS_X_SITUACION ******")
				.append("\n**** CDTIPSIT=").append(params)
				.append("\n*****************************************")
				.toString()
				);
		
		Map<String,Object> procResult = ejecutaSP(new obtenerCorreosReportarIncidenciasPorTipoSituacion(getDataSource()), params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procResult.get("PV_CORREOS_O");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException(Utils.join("No existe correos registrados para enviar relacionados al tipo de ramo: ",params.get("cdtipsit").toString()));
		}
		else
		{
			String[] arrEmails = null;
			List<String> listaCorreo = new ArrayList<String>();
			for(Map<String,String>en:lista)
			{
				listaCorreo.add(en.get("OTVALOR01"));
			}
				
			List<String> listaCorreoSeparados = new ArrayList<String>();
			for(String strMails : listaCorreo) 
			{
				// Se agregan todos los e-mails separados por ";":
				listaCorreoSeparados.addAll( Arrays.asList( StringUtils.split(strMails, ";") ) );
			}
			arrEmails = listaCorreoSeparados.toArray(new String[listaCorreo.size()]);
			return arrEmails;
		}
	}
	
	protected class obtenerCorreosReportarIncidenciasPorTipoSituacion extends StoredProcedure
	{
		protected obtenerCorreosReportarIncidenciasPorTipoSituacion(DataSource dataSource)
		{
			super(dataSource,"P_OBTENER_CORREOS_X_SITUACION");
			declareParameter(new SqlParameter("PV_CDRAMO_I" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("PV_CORREOS_O" , OracleTypes.CURSOR, new GenericMapper(new String[]{"OTVALOR01"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile(); 	 	
		}
	}
	
	@Override
	public String obtenSumaAseguradosMedicamentos(
			String cdramo,
			String cdtipsit,
			String cdgarant
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo", cdramo);
		params.put("cdtipsit", cdtipsit);
		params.put("cdgarant", cdgarant);
		
		Map<String,Object> obtReg = ejecutaSP(new obtenSumaAseguradaMedicamentos(getDataSource()), params);
		
		String sumaAseguradaMed = (String) obtReg.get("pv_sadefault_o");
		
		logger.debug(Utils.log(sumaAseguradaMed));
		
		return sumaAseguradaMed;
	}
	
	
	protected class obtenSumaAseguradaMedicamentos extends StoredProcedure
	{
		protected obtenSumaAseguradaMedicamentos(DataSource dataSource){
			super(dataSource,"P_SUMA_ASEG_DEFECTO");
			declareParameter(new SqlParameter("cdramo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgarant"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_sadefault_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile(); 
		}
	}

	@Override
	public Map<String, String> obtieneValidacionDescuentoR6(String tipoUnidad, String uso, String zona,
			String promotoria, String cdagente, String cdtipsit, String cdatribu) throws Exception {
		Map<String,String> params = new LinkedHashMap<String,String>();
		Map<String,String> descuento = new LinkedHashMap<String,String>();

	    params.put("pv_tipo_unidad", tipoUnidad);
		params.put("pv_uso", uso);
		params.put("pv_zona", zona);
		params.put("pv_promotoria", promotoria);
		params.put("pv_cdagente", cdagente);
		params.put("pv_cdtipsit_i", cdtipsit);
		params.put("pv_cdatribu_i", cdatribu);
		
		Map<String,Object> resultado = ejecutaSP(new ObtieneValidacionDescuentoR6(getDataSource()), params);
		List<Map<String,String>>listaDatos = (List<Map<String,String>>)resultado.get("pv_registro_o");
		if(listaDatos==null||listaDatos.size()==0)
		{
			throw new Exception("No se pudo cargar la poliza");
		}else{
			logger.debug("Lista de resultado: " +listaDatos);
			Map<String,String> datos = listaDatos.get(0);
			descuento.put("RANGO_MINIMO", "0");
			descuento.put("RANGO_MAXIMO", "20");
		}
		return descuento;
	}
	
	protected class ObtieneValidacionDescuentoR6 extends StoredProcedure
	{
		protected ObtieneValidacionDescuentoR6(DataSource dataSource){
			super(dataSource,"PKG_CONSULTA.P_GET_RANGOS_DESCUENTO_COMER");
			declareParameter(new SqlParameter("pv_tipo_unidad"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_uso"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_zona"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_promotoria"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagente"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i"    , OracleTypes.VARCHAR));
			
			String[] cols=new String[]{
					"RANGO_MINIMO"
					,"RANGO_MAXIMO"
				};
			
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile(); 
		}
	}
	
	@Override
	public List<ComponenteVO> recuperarTatrirol (String cdramo, String cdrol, String cdtipsit, String cdperson) throws Exception {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdrol"    , cdrol);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdperson" , cdperson);
		Map<String,Object> procResult = ejecutaSP(new RecuperarTatrirolSP(getDataSource()),params);
		List<ComponenteVO> lista = (List<ComponenteVO>)procResult.get("pv_registro_o");
		if (lista==null || lista.size()==0) {
			throw new Exception("No hay tatrirol");
		}
		logger.debug(Utils.log("recuperarTatrirol lista = ", lista));
		return lista;
	}
	
	protected class RecuperarTatrirolSP extends StoredProcedure {
    	protected RecuperarTatrirolSP (DataSource dataSource) {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_ROL");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdrol"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatriperMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public void movimientoMpolisitV2(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String status
			,String cdtipsit
			,String swreduci
			,String cdagrupa
			,String cdestado
			,Date   fefecsit
			,Date   fecharef
			,String cdgrupo
			,String nmsituaext
			,String nmsitaux
			,String nmsbsitext
			,String cdplan
			,String cdasegur
			,String accion) throws Exception {
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdunieco"   , cdunieco);
		params.put("cdramo"     , cdramo);
		params.put("estado"     , estado);
		params.put("nmpoliza"   , nmpoliza);
		params.put("nmsituac"   , nmsituac);
		params.put("nmsuplem"   , nmsuplem);
		params.put("status"     , status);
		params.put("cdtipsit"   , cdtipsit);
		params.put("swreduci"   , swreduci);
		params.put("cdagrupa"   , cdagrupa);
		params.put("cdestado"   , cdestado);
		params.put("fefecsit"   , fefecsit);
		params.put("fecharef"   , fecharef);
		params.put("cdgrupo"    , cdgrupo);
		params.put("nmsituaext" , nmsituaext);
		params.put("nmsitaux"   , nmsitaux);
		params.put("nmsbsitext" , nmsbsitext);
		params.put("cdplan"     , cdplan);
		params.put("cdasegur"   , cdasegur);
		params.put("accion"     , accion);
		ejecutaSP(new MovimientoMpolisitV2SP(getDataSource()),params);
	}
	
	protected class MovimientoMpolisitV2SP extends StoredProcedure {
		protected MovimientoMpolisitV2SP (DataSource dataSource) {
	        super(dataSource,"P_MOV_MPOLISIT_V2");
		    declareParameter(new SqlParameter("cdunieco"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("cdramo"     , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("estado"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("nmpoliza"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("nmsituac"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("nmsuplem"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("status"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdtipsit"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("swreduci"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdagrupa"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("cdestado"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("fefecsit"   , OracleTypes.DATE));
		    declareParameter(new SqlParameter("fecharef"   , OracleTypes.DATE));
		    declareParameter(new SqlParameter("cdgrupo"    , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("nmsituaext" , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("nmsitaux"   , OracleTypes.NUMERIC));
		    declareParameter(new SqlParameter("nmsbsitext" , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdplan"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("cdasegur"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
		    declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
		    compile();
		}
	}
	
	
	@Override
	public void insertaMpoligrup(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String dsgrupo, String cdplan, String dsplanVariable, String cdcveplan, String nmsumaaseg, String nmdeducible,
			String swmat, String swmed, String swee) throws Exception {
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdtipsit_i" , cdtipsit);
		params.put("pv_cdgrupo_i"  , cdgrupo);
		params.put("pv_dsgrupo_i"  , dsgrupo);
		params.put("pv_cdplan_i"   , cdplan);
		params.put("pv_cdcveplan_i", cdcveplan);
		params.put("pv_nmsumaaseg_i", nmsumaaseg);
		params.put("pv_nmdeducible_i", nmdeducible);
		params.put("pv_swmat_i"    , swmat);
		params.put("pv_swmed_i"    , swmed);
		params.put("pv_swee_i"     , swee);
		params.put("pv_dsplan_variable_i", dsplanVariable);
		ejecutaSP(new MovimientoMpoligrupSP(getDataSource()),params);
	}
	
	protected class MovimientoMpoligrupSP extends StoredProcedure {
		protected MovimientoMpoligrupSP (DataSource dataSource) {
	        super(dataSource,"PKG_COTIZA.P_INSERTA_MPOLIGRUP");
		    declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_estado_i"  , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdgrupo_i" , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_dsgrupo_i" , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdplan_i"  , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdcveplan_i",OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_nmsumaaseg_i",  OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_nmdeducible_i", OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_swmat_i"  ,  OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_swmed_i"  ,  OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_swee_i"   ,  OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_dsplan_variable_i",  OracleTypes.VARCHAR));
		    declareParameter(new SqlOutParameter("pv_msg_id_o",OracleTypes.NUMERIC));
		    declareParameter(new SqlOutParameter("pv_title_o" ,OracleTypes.VARCHAR));
		    compile();
		}
	}

	
	@Override
	public void eliminaDocsCotiza(String cdunieco, String cdramo, String estado, String nmpoliza, String ntramite) throws Exception {
	    
	    Map<String,Object> params = new LinkedHashMap<String,Object>();
	    params.put("pv_cdunieco_i" , cdunieco);
	    params.put("pv_cdramo_i"   , cdramo);
	    params.put("pv_estado_i"   , estado);
	    params.put("pv_nmpoliza_i" , nmpoliza);
	    params.put("pv_ntramite_i" , ntramite);
	    ejecutaSP(new EliminaDocsCotiza(getDataSource()),params);
	}
	
	protected class EliminaDocsCotiza extends StoredProcedure {
	    protected EliminaDocsCotiza (DataSource dataSource) {
	        super(dataSource,"P_DELETE_DOCS_COTIZA");
	        declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_estado_i"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_ntramite_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o",OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o" ,OracleTypes.VARCHAR));
	        compile();
	    }
	}
	
	
	@Override
	public void insertaMgrupogar(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit,
			String cdgrupo, String cdplan, String nmsumaaseg) throws Exception {
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdtipsit_i" , cdtipsit);
		params.put("pv_cdgrupo_i"  , cdgrupo);
		params.put("pv_cdplan_i"   , cdplan);
		params.put("pv_nmsumaaseg_i", nmsumaaseg);
		ejecutaSP(new MovimientoMgrupogarSP(getDataSource()),params);
	}
	
	protected class MovimientoMgrupogarSP extends StoredProcedure {
		protected MovimientoMgrupogarSP (DataSource dataSource) {
	        super(dataSource,"PKG_COTIZA.P_INSERTA_MGRUPOGAR");
		    declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdramo_i"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_estado_i"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdgrupo_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdplan_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_nmsumaaseg_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
		    declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
		    compile();
		}
	}
	
	
	@Override
	public void eliminarGrupos(String cdunieco, String cdramo, String estado, String nmpoliza, String cdtipsit)
			throws Exception {
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_estado_i"   , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdtipsit_i" , cdtipsit);
		ejecutaSP(new EliminarGruposSP(getDataSource()),params);
	}
	
	protected class EliminarGruposSP extends StoredProcedure {
		protected EliminarGruposSP (DataSource dataSource) {
	        super(dataSource,"PKG_COTIZA.P_ELIMINA_GRUPOS");
		    declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdramo_i"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_estado_i"     , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
		    declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
		    declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
		    compile();
		}
	}

    @Override
    public List<ComponenteVO> obtenerAtributosPolizaOriginal(Map<String, String> params) throws Exception {
        Map<String,Object> procResult = ejecutaSP(new ObtenerAtributosPolizaOriginal(getDataSource()),params);
        List<ComponenteVO> lista = (List<ComponenteVO>)procResult.get("pv_registro_o");
        if (lista==null || lista.size()==0) {
            throw new Exception("No hay informacion de la poliza original");
        }
        logger.debug(Utils.log("Recupera la informacion de la poliza original lista = ", lista));
        return lista;
    }
    
    protected class ObtenerAtributosPolizaOriginal extends StoredProcedure {
        protected ObtenerAtributosPolizaOriginal (DataSource dataSource) {
            super(dataSource,"PKG_LISTAS.P_GET_ATRI_GARANTIA_X_POLIZA");
            declareParameter(new SqlParameter("pv_cdunieco_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgrupo_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdplan_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_sexo_i"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdgarant_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdatrivar_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new ObtieneTatrigarMapper()));
            declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }

    @Override
    public String obtenerAseguradoDuplicado(HashMap<String, Object> paramPersona) throws Exception {
        Map<String, Object> resultado = ejecutaSP(new ObtenerAseguradoDuplicado(getDataSource()), paramPersona);
        return (String) resultado.get("pv_existe_o");
    }

    protected class ObtenerAseguradoDuplicado extends StoredProcedure {
        protected ObtenerAseguradoDuplicado(DataSource dataSource) {          
            super(dataSource, "PKG_SESAS.P_VALIDA_AFI_DUPLICADO");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsnombre_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsnombre1_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsapellido_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsapellido1_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_genero_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fenacimi_i",    OracleTypes.DATE));
            declareParameter(new SqlOutParameter("pv_existe_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }	
	@Override
    public List<Map<String,String>> buscaEmpleado(
                                                String pv_administradora_i
                                                ,String pv_retenedora_i,
                                                 String pv_clave_i
    											,String pv_nombre_i
    											,String pv_apellido_paterno_i
    											,String pv_apellido_materno_i
    											,String pv_rfc_i) throws Exception
    {
	 	Map<String,String> params=new HashMap<String, String>();
	 	params.put("pv_administradora_i", pv_administradora_i);
	 	params.put("pv_retenedora_i", pv_retenedora_i);
	 	params.put("pv_clave_i",  pv_clave_i.trim().equals("")?null:pv_clave_i);
	 	params.put("pv_nombre_i", pv_nombre_i.trim().equals("")?null:pv_nombre_i);
	 	params.put("pv_apellido_paterno_i", pv_apellido_paterno_i.trim().equals("")?null:pv_apellido_paterno_i);
	 	params.put("pv_apellido_materno_i", pv_apellido_materno_i.trim().equals("")?null:pv_apellido_materno_i);
	 	params.put("pv_rfc_i", pv_rfc_i.trim().equals("")?null:pv_rfc_i);
    	Map<String,Object>respuestaProcedure=ejecutaSP(new BuscaEmpleado(getDataSource()), params);
    	List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
    	
    	return lista;
    }
	
    protected class BuscaEmpleado extends StoredProcedure {
    	
    	protected BuscaEmpleado(DataSource dataSource) {
    		
    		super(dataSource, "PKG_RETENEDORAS.P_GET_EMPLEADOS");
    		declareParameter(new SqlParameter("pv_administradora_i" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_retenedora_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_clave_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nombre_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_apellido_paterno_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_apellido_materno_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_rfc_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"OTCLAVE1","OTCLAVE2","clave_empleado","nombre","apellido_p","apellido_m","rfc","curp","OTVALOR06","OTVALOR07"
			};
			
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public String guardaEmpleado(String pv_numsuc_i
                                                ,String pv_cveent_i
                                                ,String pv_cveemp_i
                                                ,String pv_nomemp_i
                                                ,String pv_apaterno_i
                                                ,String pv_amaterno_i
                                                ,String pv_rfc_i
                                                ,String pv_curp_i
                                                ,String pv_usuario_i
                                                ,String pv_feregist_i
                                                ,String pv_accion_i
                                                ) throws Exception
    {
        Map<String,String> params=new HashMap<String, String>();
        params.put("pv_numsuc_i", pv_numsuc_i);
        params.put("pv_cveent_i", pv_cveent_i);
        params.put("pv_cveemp_i", pv_cveemp_i);
        params.put("pv_nomemp_i", pv_nomemp_i);
        params.put("pv_apaterno_i", pv_apaterno_i);
        params.put("pv_amaterno_i", pv_amaterno_i);
        params.put("pv_rfc_i", pv_rfc_i);
        params.put("pv_curp_i", pv_curp_i);
        params.put("pv_usuario_i", pv_usuario_i);
        params.put("pv_feregist_i", pv_feregist_i);
        params.put("pv_accion_i", pv_accion_i);
        Map<String,Object>respuestaProcedure=ejecutaSP(new GuardaEmpleado(getDataSource()), params);
        String m=(String)respuestaProcedure.get("pv_registro_o");
        
        return m;
    }
    
    protected class GuardaEmpleado extends StoredProcedure {
        
        protected GuardaEmpleado(DataSource dataSource) {
            
            super(dataSource, "PKG_RETENEDORAS.P_MOV_EMPLEADOS");
            declareParameter(new SqlParameter("pv_numsuc_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cveent_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cveemp_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nomemp_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_apaterno_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_amaterno_i" , OracleTypes.VARCHAR));
            
            declareParameter(new SqlParameter("pv_rfc_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_curp_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_usuario_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_feregist_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_accion_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public boolean aplicaDxn( String cdtipsit,
                                    String cdsisrol,
                                    String cdusuari) throws Exception {
        boolean aplicaDxn = false;
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("pv_cdtipsit_i" , cdtipsit);
        params.put("pv_cdsisrol_i" , cdsisrol);
        params.put("pv_cdusuari_i" , cdusuari);
        Map<String,Object> result = ejecutaSP(new AplicaDxn(getDataSource()),params);
        if(Constantes.SI.equals(result.get("pv_swaplica_o"))) {
            aplicaDxn = true;
        }
        return aplicaDxn;
    }
    
    protected class AplicaDxn extends StoredProcedure {
        protected AplicaDxn(DataSource dataSource) {
            super(dataSource , "PKG_RETENEDORAS.P_GET_APLICA_DXN");
            declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_swaplica_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   ,  OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    ,  OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String validaCertificadoGrupo(HashMap<String, Object> paramExclusion) throws Exception {
        Map<String, Object> resultado = ejecutaSP(new ValidaCertificadoGrupo(getDataSource()), paramExclusion);
        logger.debug( resultado.get("pv_status_o"));
        return (String) resultado.get("pv_status_o");
    }

    protected class ValidaCertificadoGrupo extends StoredProcedure {
        protected ValidaCertificadoGrupo(DataSource dataSource) {
            super(dataSource, "PKG_SESAS.P_VALIDA_TITULAR_VIGENTE");
            declareParameter(new SqlParameter("pv_cdunieco_i",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsitaux_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_status_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
        }
    }  
    
    @Override
    public String validaDatosAutoSigs(List<Map<String,String>> incisosSigs) throws Exception
    {
        Map<String,Object> params = new LinkedHashMap<String,Object>();   
        String[][] array = new String[incisosSigs.size()][];
        
        int i = 0;
        for(Map<String,String> incisos : incisosSigs)
        {
            array[i++] = new String[]{
                     incisos.get("cdplan")
                    ,incisos.get("cdtipsit")
                    ,incisos.get("nmsituac")
                    ,incisos.get("parametros.pv_otvalor01")
                    ,incisos.get("parametros.pv_otvalor02")
                    ,incisos.get("parametros.pv_otvalor03")
                    ,incisos.get("parametros.pv_otvalor04")
                    ,incisos.get("parametros.pv_otvalor05")
                    ,incisos.get("parametros.pv_otvalor06")
                    ,incisos.get("parametros.pv_otvalor07")
                    ,incisos.get("parametros.pv_otvalor08")
                    ,incisos.get("parametros.pv_otvalor09")
                    ,incisos.get("parametros.pv_otvalor10")
                    ,incisos.get("parametros.pv_otvalor11")
                    ,incisos.get("parametros.pv_otvalor12")
                    ,incisos.get("parametros.pv_otvalor13")
                    ,incisos.get("parametros.pv_otvalor14")
                    ,incisos.get("parametros.pv_otvalor15")
                    ,incisos.get("parametros.pv_otvalor16")
                    ,incisos.get("parametros.pv_otvalor17")
                    ,incisos.get("parametros.pv_otvalor18")
                    ,incisos.get("parametros.pv_otvalor19")
                    ,incisos.get("parametros.pv_otvalor20")
                    ,incisos.get("parametros.pv_otvalor21")
                    ,incisos.get("parametros.pv_otvalor22")
                    ,incisos.get("parametros.pv_otvalor23")
                    ,incisos.get("parametros.pv_otvalor24")
                    ,incisos.get("parametros.pv_otvalor25")
                    ,incisos.get("parametros.pv_otvalor26")
                    ,incisos.get("parametros.pv_otvalor27")
                    ,incisos.get("parametros.pv_otvalor28")
                    ,incisos.get("parametros.pv_otvalor29")
                    ,incisos.get("parametros.pv_otvalor30")
                    ,incisos.get("parametros.pv_otvalor31")
                    ,incisos.get("parametros.pv_otvalor32")
                    ,incisos.get("parametros.pv_otvalor33")
                    ,incisos.get("parametros.pv_otvalor34")
                    ,incisos.get("parametros.pv_otvalor35")
                    ,incisos.get("parametros.pv_otvalor36")
                    ,incisos.get("parametros.pv_otvalor37")
                    ,incisos.get("parametros.pv_otvalor38")
                    ,incisos.get("parametros.pv_otvalor39")
                    ,incisos.get("parametros.pv_otvalor40")
                    ,incisos.get("parametros.pv_otvalor41")
                    ,incisos.get("parametros.pv_otvalor42")
                    ,incisos.get("parametros.pv_otvalor43")
                    ,incisos.get("parametros.pv_otvalor44")
                    ,incisos.get("parametros.pv_otvalor45")
                    ,incisos.get("parametros.pv_otvalor46")
                    ,incisos.get("parametros.pv_otvalor47")
                    ,incisos.get("parametros.pv_otvalor48")
                    ,incisos.get("parametros.pv_otvalor49")
                    ,incisos.get("parametros.pv_otvalor50")
                    ,incisos.get("parametros.pv_otvalor51")
                    ,incisos.get("parametros.pv_otvalor52")
                    ,incisos.get("parametros.pv_otvalor53")
                    ,incisos.get("parametros.pv_otvalor54")
                    ,incisos.get("parametros.pv_otvalor55")
                    ,incisos.get("parametros.pv_otvalor56")
                    ,incisos.get("parametros.pv_otvalor57")
                    ,incisos.get("parametros.pv_otvalor58")
                    ,incisos.get("parametros.pv_otvalor59")
                    ,incisos.get("parametros.pv_otvalor60")
                    ,incisos.get("parametros.pv_otvalor61")
                    ,incisos.get("parametros.pv_otvalor62")
                    ,incisos.get("parametros.pv_otvalor63")
                    ,incisos.get("parametros.pv_otvalor64")
                    ,incisos.get("parametros.pv_otvalor65")
                    ,incisos.get("parametros.pv_otvalor66")
                    ,incisos.get("parametros.pv_otvalor67")
                    ,incisos.get("parametros.pv_otvalor68")
                    ,incisos.get("parametros.pv_otvalor69")
                    ,incisos.get("parametros.pv_otvalor70")
                    ,incisos.get("parametros.pv_otvalor71")
                    ,incisos.get("parametros.pv_otvalor72")
                    ,incisos.get("parametros.pv_otvalor73")
                    ,incisos.get("parametros.pv_otvalor74")
                    ,incisos.get("parametros.pv_otvalor75")
                    ,incisos.get("parametros.pv_otvalor76")
                    ,incisos.get("parametros.pv_otvalor77")
                    ,incisos.get("parametros.pv_otvalor78")
                    ,incisos.get("parametros.pv_otvalor79")
                    ,incisos.get("parametros.pv_otvalor80")
                    ,incisos.get("parametros.pv_otvalor81")
                    ,incisos.get("parametros.pv_otvalor82")
                    ,incisos.get("parametros.pv_otvalor83")
                    ,incisos.get("parametros.pv_otvalor84")
                    ,incisos.get("parametros.pv_otvalor85")
                    ,incisos.get("parametros.pv_otvalor86")
                    ,incisos.get("parametros.pv_otvalor87")
                    ,incisos.get("parametros.pv_otvalor88")
                    ,incisos.get("parametros.pv_otvalor89")
                    ,incisos.get("parametros.pv_otvalor90")
                    ,incisos.get("parametros.pv_otvalor91")
                    ,incisos.get("parametros.pv_otvalor92")
                    ,incisos.get("parametros.pv_otvalor93")
                    ,incisos.get("parametros.pv_otvalor94")
                    ,incisos.get("parametros.pv_otvalor95")
                    ,incisos.get("parametros.pv_otvalor96")
                    ,incisos.get("parametros.pv_otvalor97")
                    ,incisos.get("parametros.pv_otvalor98")
                    ,incisos.get("parametros.pv_otvalor99")
                    ,incisos.get("personalizado")
            };
        }
        params.put("array" , new SqlArrayValue(array));
        Map<String, Object> procRes = ejecutaSP(new validaDatosAutoSigs(getDataSource()),params);
        String resultado = (String) procRes.get("pv_salida_o");
        if (resultado==null)
        {
            resultado="";
        }
        return resultado;
    }
    
    protected class validaDatosAutoSigs extends StoredProcedure
    {
        protected validaDatosAutoSigs(DataSource dataSource)
        {
            super(dataSource,"P_RENO_VALIDA_AUTOS_SIGS");
            declareParameter(new SqlParameter   ("array"       , OracleTypes.ARRAY    ,"LISTA_LISTAS_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_salida_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void refrescarCensoColectivo( String cdunieco,String cdramo,String estado, String nmpoliza, String nmsolici)throws Exception
    {
        Map<String,String> params = new LinkedHashMap<String,String>();
        params.put("pv_cdunieco_i" , cdunieco);
        params.put("pv_cdramo_i"   , cdramo);
        params.put("pv_estado_i"   , estado);
        params.put("pv_nmpoliza_i" , nmpoliza);
        params.put("pv_nmpolnew_i" , nmsolici);
        ejecutaSP(new RefrescarCensoColectivo(getDataSource()),params);
    }
    
    protected class RefrescarCensoColectivo extends StoredProcedure
    {
        protected RefrescarCensoColectivo(DataSource dataSource)
        {
            super(dataSource,"PKG_RENOVA.P_REFRESCA_POLIZA_A_RENOVAR");
            declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpolnew_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
	//public void cargarAseguradosFiltroGrupo(HashMap<String,String> params)throws Exception
	public AseguradosFiltroVO cargarAseguradosFiltroGrupo (HashMap<String,String> params)throws Exception
	{

		Map<String,Object>respuesta   = ejecutaSP(new CargarAseguradosFiltroGrupoSP(getDataSource()),params);
		
		AseguradosFiltroVO aseguradosFiltro = new AseguradosFiltroVO ();		
		List<Map<String,String>>lista = (List<Map<String,String>>)respuesta.get("pv_registro_o");
		
		int total = Integer.parseInt(respuesta.get("pv_num_o").toString());
		aseguradosFiltro.setTotal(total);

		aseguradosFiltro.setAsegurados(lista);

		logger.debug("viendo el contenido de aseguradosFiltro " + "lista.size() " + lista.size() + " isempty " +aseguradosFiltro.getAsegurados().isEmpty() + " total " + aseguradosFiltro.getTotal());

		return aseguradosFiltro;
	}
	
	
	protected class CargarAseguradosFiltroGrupoSP extends StoredProcedure
	{
		protected CargarAseguradosFiltroGrupoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA_PRUEBA.P_GET_ASEGURADOS_GRUPO_FILTRO");			
			declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsuplem_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgrupo_i"  , OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("pv_nmsitaux_i"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_start_i"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_limit_i"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsatribu_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i"  , OracleTypes.VARCHAR));  
			declareParameter(new SqlOutParameter("pv_num_o"  , OracleTypes.NUMERIC));
			
			String[] cols = new String[]{
					"CDGRUPO"
					,"NMSITUAC"
					,"CDPERSON"
					,"PARENTESCO"
					,"NOMBRE"
					,"SEGUNDO_NOMBRE"
					,"APELLIDO_PATERNO"
					,"APELLIDO_MATERNO"
					,"FECHA_NACIMIENTO"
					,"SEXO"
					,"NACIONALIDAD"
					,"RFC"
					,"CDROL"
					,"SWEXIPER"
					,"CDIDEPER"
					,"FAMILIA"
					,"TITULAR"
					,"FEANTIGU"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarAseguradosFiltroExtraprimas(Map<String,String>params)throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_CONSULTA_PRUEBA.P_GET_TVALOSIT_X_GRUPO_FILT ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString());
		Map<String,Object>resultado   = ejecutaSP(new CargarAseguradosFiltroExtraprimasSP(getDataSource()), params);
		List<Map<String,String>>lista = (List<Map<String,String>>)resultado.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	
	protected class CargarAseguradosFiltroExtraprimasSP extends StoredProcedure
	{
		protected CargarAseguradosFiltroExtraprimasSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA_PRUEBA.P_GET_TVALOSIT_X_GRUPO_FILT");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("start"    , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("limit"    , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsatribu_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i"    , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
						"NOMBRE"   , "NMSITUAC"   , "EDAD"             , "FENACIMI" 
	                    ,"SEXO"    , "PARENTESCO" , "OCUPACION"        , "EXTPRI_OCUPACION"
	                    ,"PESO"    , "ESTATURA"   , "EXTPRI_SOBREPESO" , "FAMILIA"
	                    ,"TITULAR"
					};
			declareParameter(new SqlOutParameter("pv_num_o"   	 , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarSituacionesFiltroGrupo(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String cdgrupo
			,String start
			,String limit
			,String filtro
			,String valorFiltro)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdgrupo"  , cdgrupo);
		params.put("start"  , start);
		params.put("limit"  , limit);
		params.put("pv_dsatribu_i"  , filtro);
		params.put("pv_otvalor_i"  , valorFiltro);
		logger.debug(
				new StringBuilder()
				.append("\n**************************************************************")
				.append("\n****** PKG_CONSULTA_PRUEBA.P_GET_TVALOSIT_X_GRUPO_DINAM_FILTRO ******")
				.append("\n****** params=").append(params)
				.append("\n**************************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new CargarSituacionesFiltroGrupoSP(getDataSource()),params);
		List<Map<String,String>>situaciones=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(situaciones==null||situaciones.size()==0)
		{
			throw new Exception("No hay situaciones para el grupo");
		}
		return situaciones;
	}
	
	protected class CargarSituacionesFiltroGrupoSP extends StoredProcedure
	{
		protected CargarSituacionesFiltroGrupoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA_PRUEBA.P_GET_TVALOSIT_X_GRUPO_D_FILT");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("start"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("limit"  , OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsatribu_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_otvalor_i"  , OracleTypes.VARCHAR));
			String[] cols=new String[]
					{
							
					"nombre"
					,"nmsituac"
					,"familia"
					,"titular"
					,"parentesco"
					            ,"otvalor01","otvalor02","otvalor03","otvalor04","otvalor05","otvalor06","otvalor07","otvalor08","otvalor09"
					,"otvalor10","otvalor11","otvalor12","otvalor13","otvalor14","otvalor15","otvalor16","otvalor17","otvalor18","otvalor19"
					,"otvalor20","otvalor21","otvalor22","otvalor23","otvalor24","otvalor25","otvalor26","otvalor27","otvalor28","otvalor29"
					,"otvalor30","otvalor31","otvalor32","otvalor33","otvalor34","otvalor35","otvalor36","otvalor37","otvalor38","otvalor39"
					,"otvalor40","otvalor41","otvalor42","otvalor43","otvalor44","otvalor45","otvalor46","otvalor47","otvalor48","otvalor49"
					,"otvalor50","otvalor51","otvalor52","otvalor53","otvalor54","otvalor55","otvalor56","otvalor57","otvalor58","otvalor59"
					,"otvalor60","otvalor61","otvalor62","otvalor63","otvalor64","otvalor65","otvalor66","otvalor67","otvalor68","otvalor69"
					,"otvalor70","otvalor71","otvalor72","otvalor73","otvalor74","otvalor75","otvalor76","otvalor77","otvalor78","otvalor79"
					,"otvalor80","otvalor81","otvalor82","otvalor83","otvalor84","otvalor85","otvalor86","otvalor87","otvalor88","otvalor89"
					,"otvalor90","otvalor91","otvalor92","otvalor93","otvalor94","otvalor95","otvalor96","otvalor97","otvalor98","otvalor99"
					};
			declareParameter(new SqlOutParameter("pv_num_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public HashMap<String, String> obtieneCodigosPostalesProductos() throws Exception {
		
		HashMap<String,String> mapaCodigosPostales =  new HashMap<String, String>();
		
		Map<String,String>params=new LinkedHashMap<String,String>();
		logger.debug(
				new StringBuilder()
				.append("\n**************************************************************")
				.append("\n****** P_GET_CODIGOS_POSTALES ******")
				.append("\n****** params=").append(params)
				.append("\n**************************************************************")
				.toString()
				);
		Map<String,Object>procResult=ejecutaSP(new ObtieneCodigosPostalesProductoSP(getDataSource()),params);
		List<Map<String,String>>codigosPostales=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(codigosPostales==null||codigosPostales.size()==0)
		{
			throw new Exception("No hay codigos postales para validacion de domicilios asegurados en productos colectivos.");
		}
		
		for(Map<String,String> codPos: codigosPostales){
			mapaCodigosPostales.put(codPos.get("CODIGOPOSTAL"), null);
		}
		
		procResult = null;
		codigosPostales = null;
		
		logger.debug("Tamanio de codigos postales = "+mapaCodigosPostales.size());
		
		return mapaCodigosPostales;
	}
	
	protected class ObtieneCodigosPostalesProductoSP extends StoredProcedure
	{
		protected ObtieneCodigosPostalesProductoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_CODIGOS_POSTALES");
			String[] cols=new String[]
					{"CODIGOPOSTAL"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> getCoberturas(String pv_cdramo_i, String pv_cdplan_i, String pv_cdtipsit_i) throws Exception {
	    
	    Map<String,Object> params = new LinkedHashMap<String,Object>();
	    params.put("pv_cdramo_i" , pv_cdramo_i);
	    params.put("pv_cdplan_i"   , pv_cdplan_i);
	    params.put("pv_cdtipsit_i"   , pv_cdtipsit_i);
	
	    Map<String,Object>procResult=ejecutaSP(new GetCoberturas(getDataSource()),params);
		List<Map<String,String>>coberturas=(List<Map<String,String>>)procResult.get("pv_registro_o");
		if(coberturas==null||coberturas.size()==0)
		{
			throw new Exception("No hay situaciones para el grupo");
		}
		return coberturas;

	}
	
	protected class GetCoberturas extends StoredProcedure {
	    protected GetCoberturas (DataSource dataSource) {
	        super(dataSource,"Pkg_Listas.P_COBERTURAS");
	        declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_cdplan_i"  , OracleTypes.VARCHAR));
	        declareParameter(new SqlParameter("pv_cdtipsit_i"  , OracleTypes.VARCHAR));
	        
	        String[] cols=new String[]
					{
					"cdgarant"
					,"dsgarant"
					,"fecha_add"
					,"SWOBLIG"
					};
	        
	        
	        declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
	        declareParameter(new SqlOutParameter("pv_msg_id_o",OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o" ,OracleTypes.VARCHAR));
	        compile();
	    }
	}
	
	@Override
    public String aplicaDescAutos(String pcdunieco, String pcdramo, String pnmpoliza, String pdesc, String pisflot) throws Exception {
        
	    String mensaje = "";
        try 
        {
            Map<String,Object> params = new LinkedHashMap<String,Object>();
            params.put("PCDUNIECO" , pcdunieco);
            params.put("PCDRAMO"   , pcdramo);
            params.put("PNMPOLIZA" , pnmpoliza);
            params.put("PDESC"     , pdesc);
            params.put("PISFLOT"   , pisflot);
            
            Map<String, Object> procRes =ejecutaSP(new AplicaDescAutos(getDataSource()),params);
            
            int idMsn = Integer.parseInt(procRes.get("PV_MSG_ID_O")+"");
            if(idMsn==0)
            {
                mensaje = procRes.get("PV_SALIDA_O")!=null && !procRes.get("PV_SALIDA_O").toString().isEmpty()?procRes.get("PV_SALIDA_O").toString():"";
            }
            else
            {
                throw new Exception("Error al aplicar descuento a tarifas.");   
            }
            return mensaje;
            
        } catch (Exception e) 
        {
            throw new Exception("Error al aplicar descuento en Mpolirec."+mensaje);
        } 
    }
    
    protected class AplicaDescAutos extends StoredProcedure {
        protected AplicaDescAutos (DataSource dataSource) {
            super(dataSource,"P_APLICA_DESCUENTO_AUTOS");
            declareParameter(new SqlParameter("PCDUNIECO" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PCDRAMO"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PNMPOLIZA" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PDESC"     , OracleTypes.FLOAT));
            declareParameter(new SqlParameter("PISFLOT"   , OracleTypes.SMALLINT));
            declareParameter(new SqlOutParameter("PV_SALIDA_O" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O" ,OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O"  ,OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
	public String aplicaRecargoPagoFraccionado(String cdunieco, String cdramo, String nmpoliza, String recargoPF, String flotilla)throws Exception
	{
    	String mensaje = "";
        try 
        {
	    	Map<String,String>params=new LinkedHashMap<String,String>();
			params.put("PCDUNIECO" , cdunieco);
			params.put("PCDRAMO"   , cdramo);
			params.put("PNMPOLIZA"   , nmpoliza);
			params.put("PRF" , recargoPF);
			params.put("PISFLOT" , flotilla);
			
			logger.debug(
					new StringBuilder()
					.append("\n***************************************************")
					.append("\n****** P_APLICA_RF_AUTOS **************************")
					.append("\n****** params=").append(params)
					.append("\n***************************************************")
					.toString());
			Map<String,Object>procRes   = ejecutaSP(new AplicaRecargoPagoFraccionado(getDataSource()), params);
        
		    int idMsn = Integer.parseInt(procRes.get("PV_MSG_ID_O")+"");
			if(idMsn==0)
	        {
	            mensaje = procRes.get("PV_SALIDA_O")!=null && !procRes.get("PV_SALIDA_O").toString().isEmpty()?procRes.get("PV_SALIDA_O").toString():"";
	        }
	        else
	        {
	            throw new Exception("Error al aplicar recargo por pago fraccionado.");   
	        }
	        return mensaje;
        }
		catch (Exception e) 
        {
            throw new Exception("Error al aplicar recargo por pago fraccionado."+mensaje);
        } 
	}
	
	protected class AplicaRecargoPagoFraccionado extends StoredProcedure
	{
		protected AplicaRecargoPagoFraccionado(DataSource dataSource)
		{
			super(dataSource,"P_APLICA_RF_AUTOS");
			declareParameter(new SqlParameter("PCDUNIECO" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PCDRAMO"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PNMPOLIZA" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PRF"     , OracleTypes.FLOAT));
            declareParameter(new SqlParameter("PISFLOT"   , OracleTypes.SMALLINT));
            declareParameter(new SqlOutParameter("PV_SALIDA_O" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O" ,OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O"  ,OracleTypes.VARCHAR));
            compile();
		}
	}
	
    @Override
    public Map<String, String> recuperarRangoDescuentoRecargo (String cdramo, String cdtipsit, String cdusuari, String cdsisrol) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdramo_i"   , cdramo);
        params.put("pv_cdtipsit_i" , cdtipsit);
        params.put("pv_cdusuari_i" , cdusuari);
        params.put("pv_cdsisrol_i" , cdsisrol);
        Map<String, Object> procRes = ejecutaSP(new RecuperarRangoDescuentoRecargoSP(getDataSource()), params);
        Map<String, String> result = new HashMap<String, String>();
        result.put("MINIMO", (String) procRes.get("pv_minimo_o"));
        result.put("MAXIMO", (String) procRes.get("pv_maximo_o"));
        if (StringUtils.isBlank(result.get("MINIMO")) || StringUtils.isBlank(result.get("MAXIMO"))) {
            throw new ApplicationException("No hay rango de descuento/recargo");
        }
        return result;
    }
    
    protected class RecuperarRangoDescuentoRecargoSP extends StoredProcedure {
        protected RecuperarRangoDescuentoRecargoSP (DataSource dataSource) {
            super(dataSource,"P_COT_GET_MIN_MAX_DESC_RECAR");
            declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_minimo_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_maximo_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>>cargarResultadosCotizacionLicenciaFlotilla(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza)throws Exception
	{
   	 Map<String,String>params=new LinkedHashMap<String,String>();
   	 params.put("cdunieco" , cdunieco);
   	 params.put("cdramo"   , cdramo);
   	 params.put("estado"   , estado);
   	 params.put("nmpoliza" , nmpoliza);
   	
   	 Map<String,Object>procResult  = ejecutaSP(new CargarResultadosCotizacionLicenciaFlotilla(getDataSource()),params);
   	 List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
   	 if(lista==null||lista.size()==0)
   	 {
   		 throw new ApplicationException("No hay resultados de cotizacion");
   	 }
   	 
   	 return lista;
	}
    
    protected class CargarResultadosCotizacionLicenciaFlotilla extends StoredProcedure
    {
    	protected CargarResultadosCotizacionLicenciaFlotilla(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GEN_TARIFICA_LICENCIAS_FLOT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols=new String[]{"CDPERPAG","DSPERPAG","PRIMA"};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
}