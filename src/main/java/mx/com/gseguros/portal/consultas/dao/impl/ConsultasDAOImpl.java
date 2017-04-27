package mx.com.gseguros.portal.consultas.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.cotizacion.model.ParametroGeneral;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.DynamicMapper;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

public class ConsultasDAOImpl extends AbstractManagerDAO implements ConsultasDAO
{
	private static final org.apache.log4j.Logger logger  = org.apache.log4j.Logger.getLogger(ConsultasDAOImpl.class);
	private static final org.slf4j.Logger        logger2 = org.slf4j.LoggerFactory.getLogger(ConsultasDAOImpl.class);
	
	@Override
	public List<Map<String,String>> consultaDinamica(String storedProcedure,LinkedHashMap<String,Object>params) throws Exception
	{
		Map<String,Object>result = this.ejecutaSP(new ConsultaDinamica(storedProcedure, params, getDataSource()), params);
		return (List<Map<String,String>>) result.get("pv_registro_o");
	}
	
	protected class ConsultaDinamica extends StoredProcedure
	{
		protected ConsultaDinamica(String storedProcedure, LinkedHashMap<String,Object> params, DataSource dataSource)
		{
			super(dataSource, storedProcedure);

			if(params!=null)
			{
				for(Entry<String,Object>param : params.entrySet())
				{
					declareParameter(new SqlParameter(param.getKey() , OracleTypes.VARCHAR));
				}
			}

			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DinamicMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			
			compile();
		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception
		{
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
		
		private class DinamicMapper implements RowMapper
		{
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				String cols="";
				Map<String,String> map=new LinkedHashMap<String,String>(0);
				ResultSetMetaData metaData = rs.getMetaData();
				int numCols=metaData.getColumnCount();
				for (int i=1;i<=numCols;i++)
				{
					String col=metaData.getColumnName(i);
					if(rowNum==0)
					{
						cols=cols+col+",";
					}
					if(col!=null&&(col.substring(0,2).equalsIgnoreCase("fe")||col.substring(0,2).equalsIgnoreCase("ff")))
					{
						map.put(col,Utils.formateaFecha(rs.getString(col)));
					}
					else
					{
						map.put(col,rs.getString(col));
					}			
				}
				if(rowNum==0)
				{
					logger.info("Columnas: "+cols);
				}
				return map;
			}
		}
	}
	
	@Override
	public List<Map<String,String>>cargarMpolizasPorParametrosVariables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsolici
			,String cdramant
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsolici" , nmsolici);
		params.put("cdramant" , cdramant);
		Map<String,Object>procResult  = ejecutaSP(new CargarMpolizasPorParametrosVariables(getDataSource()), params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log(
				 "\n*****************************************************"
				,"\n****** params="   , params
				,"\n****** registro=" , lista
				,"\n****** ...P_GET_MPOLIZAS_X_PAR_VAR ******"
				,"\n*****************************************************"
				));
		return lista;
	}
	
	protected class CargarMpolizasPorParametrosVariables extends StoredProcedure
    {
    	protected CargarMpolizasPorParametrosVariables(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GET_MPOLIZAS_X_PAR_VAR");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsolici" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramant" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO"  , "CDRAMO"   , "ESTADO"   , "NMPOLIZA" , "NMSUPLEM"
            		,"STATUS"   , "SWESTADO" , "NMSOLICI" , "FEAUTORI" , "CDMOTANU"
            		,"FEANULAC" , "SWAUTORI" , "CDMONEDA" , "FEINISUS" , "FEFINSUS"
            		,"OTTEMPOT" , "FEEFECTO" , "HHEFECTO" , "FEPROREN" , "FEVENCIM"
            		,"NMRENOVA" , "FERECIBO" , "FEULTSIN" , "NMNUMSIN" , "CDTIPCOA"
            		,"SWTARIFI" , "SWABRIDO" , "FEEMISIO" , "CDPERPAG" , "NMPOLIEX"
            		,"NMCUADRO" , "PORREDAU" , "SWCONSOL" , "NMPOLANT" , "NMPOLNVA"
            		,"FESOLICI" , "CDRAMANT" , "CDMEJRED" , "NMPOLDOC" , "NMPOLIZA2"
            		,"NMRENOVE" , "NMSUPLEE" , "TTIPCAMC" , "TTIPCAMV" , "SWPATENT"
            		,"NMPOLMST" , "PCPGOCTE" , "TIPOFLOT" , "RAMO"     , "CDUNIEXT"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>>cargarTconvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		Map<String,Object>procResult  = ejecutaSP(new CargarTconvalsit(getDataSource()), params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log(
				 "\n*********************************************"
				,"\n****** params="   , params
				,"\n****** registro=" , lista
				,"\n****** ...P_GET_TCONVALSIT ******"
				,"\n*********************************************"
				));
		return lista;
	}
	
	protected class CargarTconvalsit extends StoredProcedure
    {
    	protected CargarTconvalsit(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GET_TCONVALSIT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO"
            		,"CDRAMO"
            		,"ESTADO"
            		,"NMPOLIZA"
            		,"NMSITUAC"
            		,"NMSUPLEM"
            		,"STATUS"
            		,"CDTIPSIT"
            		,"OTVALOR01","OTVALOR02","OTVALOR03","OTVALOR04","OTVALOR05","OTVALOR06","OTVALOR07","OTVALOR08","OTVALOR09","OTVALOR10"
            		,"OTVALOR11","OTVALOR12","OTVALOR13","OTVALOR14","OTVALOR15","OTVALOR16","OTVALOR17","OTVALOR18","OTVALOR19","OTVALOR20"
            		,"OTVALOR21","OTVALOR22","OTVALOR23","OTVALOR24","OTVALOR25","OTVALOR26","OTVALOR27","OTVALOR28","OTVALOR29","OTVALOR30"
            		,"OTVALOR31","OTVALOR32","OTVALOR33","OTVALOR34","OTVALOR35","OTVALOR36","OTVALOR37","OTVALOR38","OTVALOR39","OTVALOR40"
            		,"OTVALOR41","OTVALOR42","OTVALOR43","OTVALOR44","OTVALOR45","OTVALOR46","OTVALOR47","OTVALOR48","OTVALOR49","OTVALOR50"
            		,"OTVALOR51","OTVALOR52","OTVALOR53","OTVALOR54","OTVALOR55","OTVALOR56","OTVALOR57","OTVALOR58","OTVALOR59","OTVALOR60"
            		,"OTVALOR61","OTVALOR62","OTVALOR63","OTVALOR64","OTVALOR65","OTVALOR66","OTVALOR67","OTVALOR68","OTVALOR69","OTVALOR70"
            		,"OTVALOR71","OTVALOR72","OTVALOR73","OTVALOR74","OTVALOR75","OTVALOR76","OTVALOR77","OTVALOR78","OTVALOR79","OTVALOR80"
            		,"OTVALOR81","OTVALOR82","OTVALOR83","OTVALOR84","OTVALOR85","OTVALOR86","OTVALOR87","OTVALOR88","OTVALOR89","OTVALOR90"
            		,"OTVALOR91","OTVALOR92","OTVALOR93","OTVALOR94","OTVALOR95","OTVALOR96","OTVALOR97","OTVALOR98","OTVALOR99"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>>cargarTbasvalsit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		Map<String,Object>procResult  = ejecutaSP(new CargarTbasvalsit(getDataSource()), params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log(
				 "\n*********************************************"
				,"\n****** params="   , params
				,"\n****** registro=" , lista
				,"\n****** ...P_GET_TBASVALSIT ******"
				,"\n*********************************************"
				));
		return lista;
	}
	
	protected class CargarTbasvalsit extends StoredProcedure
    {
    	protected CargarTbasvalsit(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GET_TBASVALSIT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO"
            		,"CDRAMO"
            		,"ESTADO"
            		,"NMPOLIZA"
            		,"NMSITUAC"
            		,"NMSUPLEM"
            		,"STATUS"
            		,"CDTIPSIT"
            		,"OTVALOR01","OTVALOR02","OTVALOR03","OTVALOR04","OTVALOR05","OTVALOR06","OTVALOR07","OTVALOR08","OTVALOR09","OTVALOR10"
            		,"OTVALOR11","OTVALOR12","OTVALOR13","OTVALOR14","OTVALOR15","OTVALOR16","OTVALOR17","OTVALOR18","OTVALOR19","OTVALOR20"
            		,"OTVALOR21","OTVALOR22","OTVALOR23","OTVALOR24","OTVALOR25","OTVALOR26","OTVALOR27","OTVALOR28","OTVALOR29","OTVALOR30"
            		,"OTVALOR31","OTVALOR32","OTVALOR33","OTVALOR34","OTVALOR35","OTVALOR36","OTVALOR37","OTVALOR38","OTVALOR39","OTVALOR40"
            		,"OTVALOR41","OTVALOR42","OTVALOR43","OTVALOR44","OTVALOR45","OTVALOR46","OTVALOR47","OTVALOR48","OTVALOR49","OTVALOR50"
            		,"OTVALOR51","OTVALOR52","OTVALOR53","OTVALOR54","OTVALOR55","OTVALOR56","OTVALOR57","OTVALOR58","OTVALOR59","OTVALOR60"
            		,"OTVALOR61","OTVALOR62","OTVALOR63","OTVALOR64","OTVALOR65","OTVALOR66","OTVALOR67","OTVALOR68","OTVALOR69","OTVALOR70"
            		,"OTVALOR71","OTVALOR72","OTVALOR73","OTVALOR74","OTVALOR75","OTVALOR76","OTVALOR77","OTVALOR78","OTVALOR79","OTVALOR80"
            		,"OTVALOR81","OTVALOR82","OTVALOR83","OTVALOR84","OTVALOR85","OTVALOR86","OTVALOR87","OTVALOR88","OTVALOR89","OTVALOR90"
            		,"OTVALOR91","OTVALOR92","OTVALOR93","OTVALOR94","OTVALOR95","OTVALOR96","OTVALOR97","OTVALOR98","OTVALOR99"
            		//MPOLISIT
            		,"SWREDUCI"    , "CDAGRUPA" , "CDESTADO"   , "FEFECSIT" , "FECHAREF" , "CDGRUPO"
            		, "NMSITUAEXT" , "NMSITAUX" , "NMSBSITEXT" , "CDPLAN"   , "CDASEGUR" , "DSGRUPO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public Map<String,String>cargarMpoliperSituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		Map<String,Object>procResult  = ejecutaSP(new CargarMpoliperSituac(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		Map<String,String>mpoliper    = null;
		if(lista!=null&&lista.size()==1)
		{
			mpoliper = lista.get(0);
		}
		else if(lista!=null&&lista.size()>1)
		{
			throw new ApplicationException("Registro de relacion poliza-persona duplicado");
		}
		logger.debug(Utils.log(
				 "\n**************************************************"
				,"\n****** params="   , params
				,"\n****** registro=" , mpoliper
				,"\n****** ...P_GET_MPOLIPER_SITUAC ******"
				,"\n**************************************************"
				));
		return mpoliper;
	}
	
	protected class CargarMpoliperSituac extends StoredProcedure
    {
    	protected CargarMpoliperSituac(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GET_MPOLIPER_SITUAC");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO"  , "CDRAMO"   , "ESTADO"   , "NMPOLIZA"
            		,"NMSITUAC" , "CDROL"    , "CDPERSON" , "NMSUPLEM"
            		,"STATUS"   , "NMORDDOM" , "SWRECLAM" , "SWEXIPER"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public Map<String,String>cargarMpolisitSituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsituac" , nmsituac);
		Map<String,Object>procResult  = ejecutaSP(new CargarMpolisitSituac(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		Map<String,String>mpoliper    = null;
		if(lista!=null&&lista.size()==1)
		{
			mpoliper = lista.get(0);
		}
		else if(lista!=null&&lista.size()>1)
		{
			throw new ApplicationException("Registro de relacion poliza-situacion duplicado");
		}
		logger.debug(Utils.log(
				 "\n**************************************************"
				,"\n****** params="   , params
				,"\n****** registro=" , mpoliper
				,"\n****** ...P_GET_MPOLISIT_SITUAC ******"
				,"\n**************************************************"
				));
		return mpoliper;
	}
	
	protected class CargarMpolisitSituac extends StoredProcedure
    {
    	protected CargarMpolisitSituac(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GET_MPOLISIT_SITUAC");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO"  , "CDRAMO"     , "ESTADO"   , "NMPOLIZA" , "NMSITUAC"
            		,"NMSUPLEM" , "STATUS"     , "CDTIPSIT" , "SWREDUCI" , "CDAGRUPA"
            		,"CDESTADO" , "FEFECSIT"   , "FECHAREF" , "CDGRUPO"  , "NMSITUAEXT"
            		,"NMSITAUX" , "NMSBSITEXT" , "CDPLAN"   , "CDASEGUR" , "DSGRUPO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>>cargarTvalosit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		Map<String,Object>procResult  = ejecutaSP(new CargarTvalosit(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "...P_GET_TVALOSIT", params,lista);
		return lista;
	}
	
	protected class CargarTvalosit extends StoredProcedure
    {
    	protected CargarTvalosit(DataSource dataSource)
        {
            super(dataSource,"PKG_SATELITES2.P_GET_TVALOSIT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO"  ,"CDRAMO"   ,"ESTADO"   ,"NMPOLIZA" ,"NMSITUAC" ,"NMSUPLEM" ,"STATUS"   ,"CDTIPSIT"
            		,"OTVALOR01","OTVALOR02","OTVALOR03","OTVALOR04","OTVALOR05","OTVALOR06","OTVALOR07","OTVALOR08","OTVALOR09","OTVALOR10"
            		,"OTVALOR11","OTVALOR12","OTVALOR13","OTVALOR14","OTVALOR15","OTVALOR16","OTVALOR17","OTVALOR18","OTVALOR19","OTVALOR20"
            		,"OTVALOR21","OTVALOR22","OTVALOR23","OTVALOR24","OTVALOR25","OTVALOR26","OTVALOR27","OTVALOR28","OTVALOR29","OTVALOR30"
            		,"OTVALOR31","OTVALOR32","OTVALOR33","OTVALOR34","OTVALOR35","OTVALOR36","OTVALOR37","OTVALOR38","OTVALOR39","OTVALOR40"
            		,"OTVALOR41","OTVALOR42","OTVALOR43","OTVALOR44","OTVALOR45","OTVALOR46","OTVALOR47","OTVALOR48","OTVALOR49","OTVALOR50"
            		,"OTVALOR51","OTVALOR52","OTVALOR53","OTVALOR54","OTVALOR55","OTVALOR56","OTVALOR57","OTVALOR58","OTVALOR59","OTVALOR60"
            		,"OTVALOR61","OTVALOR62","OTVALOR63","OTVALOR64","OTVALOR65","OTVALOR66","OTVALOR67","OTVALOR68","OTVALOR69","OTVALOR70"
            		,"OTVALOR71","OTVALOR72","OTVALOR73","OTVALOR74","OTVALOR75","OTVALOR76","OTVALOR77","OTVALOR78","OTVALOR79","OTVALOR80"
            		,"OTVALOR81","OTVALOR82","OTVALOR83","OTVALOR84","OTVALOR85","OTVALOR86","OTVALOR87","OTVALOR88","OTVALOR89","OTVALOR90"
            		,"OTVALOR91","OTVALOR92","OTVALOR93","OTVALOR94","OTVALOR95","OTVALOR96","OTVALOR97","OTVALOR98","OTVALOR99"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
	public List<Map<String,String>>cargarMpoliage(
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
    	params.put("cdagente" , null);
    	Map<String,Object>procResult  = ejecutaSP(new CargarMpoliage(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException("No hay agentes para la poliza");
    	}
    	Utils.debugProcedure(logger, "...P_OBTIENE_MPOLIAGE2", params, lista);
    	return lista;
	}
    
    protected class CargarMpoliage extends StoredProcedure
    {
    	protected CargarMpoliage(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES.P_OBTIENE_MPOLIAGE2");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"CDUNIECO" , "CDRAMO"   , "ESTADO"   , "NMPOLIZA" , "CDAGENTE" , "NMSUPLEM"
            		,"STATUS"  , "CDTIPOAG" , "PORREDAU" , "NMCUADRO" , "CDSUCURS"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public void validarDatosCliente(
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
    	ejecutaSP(new ValidarDatosCliente(getDataSource()),params);
    		}
    
    protected class ValidarDatosCliente extends StoredProcedure
    {
    	protected ValidarDatosCliente(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES2.P_VALIDAR_CODIGO_EXTERNO_CTE");
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
    public void validarDatosObligatoriosPrevex(
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
    	ejecutaSP(new ValidarDatosObligatoriosPrevex(getDataSource()),params);
	}
    
    protected class ValidarDatosObligatoriosPrevex extends StoredProcedure
    {
    	protected ValidarDatosObligatoriosPrevex(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES.P_VALIDA_DATOS_OBLIG_PREVEX");
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
    public void validarAtributosDXN(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("nmsuplem" , nmsuplem);
    	ejecutaSP(new ValidarAtributosDXN(getDataSource()),params);
	}
    
    protected class ValidarAtributosDXN extends StoredProcedure
    {
    	protected ValidarAtributosDXN(DataSource dataSource)
    	{
    		super(dataSource,"PKG_SATELITES.P_VALIDA_ATRIB_FP_DXN");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_swexito_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
	public Map<String,String>cargarUltimoNmsuplemPoliza(
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
    	Map<String,Object>procResult = ejecutaSP(new CargarUltimoNmsuplemPoliza(getDataSource()),params);
    	Map<String,String>salida = new LinkedHashMap<String,String>();
    	salida.put("nmsuplem" , (String)procResult.get("pv_nmsuplem_o"));
    	return salida;
	}
    
    protected class CargarUltimoNmsuplemPoliza extends StoredProcedure
    {
    	protected CargarUltimoNmsuplemPoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_MAX_SUPLEMENTO");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_nmsuplem_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>cargarMpoliperOtrosRolesPorNmsituac(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsituac
			,String rolesPipes)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("nmsuplem" , nmsuplem);
    	params.put("nmsituac" , nmsituac);
    	params.put("roles"    , rolesPipes);
    	Map<String,Object>procResult  = ejecutaSP(new CargarMpoliperOtrosRolesPorNmsituac(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger, "...P_GET_MPOLIPER_OTROS_ROLES", params, lista);
    	return lista;
	}
    
    protected class CargarMpoliperOtrosRolesPorNmsituac extends StoredProcedure
    {
    	protected CargarMpoliperOtrosRolesPorNmsituac(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES2.P_GET_MPOLIPER_OTROS_ROLES");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("roles"    , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"CDUNIECO"    , "CDRAMO"
            		,"ESTADO"     , "NMPOLIZA"
            		,"NMSITUAC"   , "CDROL"
                    ,"CDPERSON"   , "NMSUPLEM"
                    ,"STATUS"     , "NMORDDOM"
                    ,"SWRECLAM"   , "SWEXIPER"
                    ,"CDPARENT"   , "PORBENEF"
                    ,"CDTIPIDE"   , "CDIDEPER"
                    ,"DSNOMBRE"   , "CDTIPPER"
                    ,"OTFISJUR"   , "OTSEXO"
                    ,"FENACIMI"   , "CDRFC"
                    ,"DSEMAIL"    , "DSNOMBRE1"
                    ,"DSAPELLIDO" , "DSAPELLIDO1"
                    ,"CDNACION"   , "DSCOMNOM"
                    ,"DSRAZSOC"   , "FEINGRESO"
                    ,"FEACTUAL"   , "DSNOMUSU"
                    ,"CDESTCIV"   , "CDGRUECO"
                    ,"CDSTIPPE"   , "NMNUMNOM"
                    ,"CURP"       , "CANALING"
                    ,"CONDUCTO"   , "PTCUMUPR"
                    ,"STATUS_PER" , "RESIDENCIA"
                    ,"NONGRATA"   , "CDIDEEXT"
                    ,"CDSUCEMI"
            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>cargarTiposSituacionPorRamo(String cdramo)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo" , cdramo);
    	Map<String,Object>procResult  = ejecutaSP(new CargarTiposSituacionPorRamo(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger, "...P_OBTIENE_SITUACION", params, lista);
    	return lista;
    }
    
    protected class CargarTiposSituacionPorRamo extends StoredProcedure
    {
    	protected CargarTiposSituacionPorRamo(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_OBTIENE_SITUACION");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            String[] cols=new String[]{"CDTIPSIT","DSTIPSIT"};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public boolean verificarCodigoPostalFronterizo(String cdpostal)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdpostal" , cdpostal);
    	Map<String,Object>procResult = ejecutaSP(new VerificarCodigoPostalFronterizo(getDataSource()),params);
    	boolean esFront = ((String)procResult.get("pv_fronterizo_o")).equals("S");
    	logger.debug(Utils.log("verificarCodigoPostalFronterizo=",esFront));
    	return esFront;
    }
    
    protected class VerificarCodigoPostalFronterizo extends StoredProcedure
    {
    	protected VerificarCodigoPostalFronterizo(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES2.P_VERIFICA_CDPOSTAL_FRONTER");
            declareParameter(new SqlParameter("cdpostal"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_fronterizo_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,String>cargarAtributosBaseCotizacion(String cdtipsit)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdtipsit" , cdtipsit);
    	Map<String,Object>procResult   = ejecutaSP(new CargarAtributosBaseCotizacion(getDataSource()),params);
    	List<Map<String,String>> lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException("No hay atributos base de cotizacion para la modalidad");
    	}
    	if(lista.size()>1)
    	{
    		throw new ApplicationException("Atributos base de cotizacion duplicados para la modalidad");
    	}
    	Utils.debugProcedure(logger, "...P_OBT_ATRIBUTOS", params, lista);
    	return lista.get(0);
    }
    
    protected class CargarAtributosBaseCotizacion extends StoredProcedure
    {
    	protected CargarAtributosBaseCotizacion(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES.P_OBT_ATRIBUTOS");
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            String[] cols=new String[]{"SEXO","FENACIMI","PARENTESCO","CODPOSTAL"};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,String>cargarInformacionPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdusuari
			)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("cdusuari" , cdusuari);
    	Map<String,Object>procResult  = ejecutaSP(new CargarInformacionPoliza(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null||lista.size()==0)
    	{
    		throw new ApplicationException("No hay informacion de poliza");
    	}
    	if(lista.size()>1)
    	{
    		throw new ApplicationException("Informacion de poliza duplicada");
    	}
    	Utils.debugProcedure(logger, "...P_GET_INFO_MPOLIZAS", params, lista);
    	return lista.get(0);
	}
    
    protected class CargarInformacionPoliza extends StoredProcedure
    {
    	protected CargarInformacionPoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES.P_GET_INFO_MPOLIZAS");
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"status"    , "swestado" , "nmsolici" , "feautori" , "cdmotanu" , "feanulac" , "swautori"
            		,"cdmoneda" , "feinisus" , "fefinsus" , "ottempot" , "feefecto" , "hhefecto" , "feproren"
            		,"fevencim" , "nmrenova" , "ferecibo" , "feultsin" , "nmnumsin" , "cdtipcoa" , "swtarifi"
            		,"swabrido" , "feemisio" , "cdperpag" , "nmpoliex" , "nmcuadro" , "porredau" , "swconsol"
            		,"nmpolant" , "nmpolnva" , "fesolici" , "cdramant" , "cdmejred" , "nmpoldoc" , "nmpoliza2"
            		,"nmrenove" , "nmsuplee" , "ttipcamc" , "ttipcamv" , "swpatent" , "cdagente" , "ramo"
            		,"cduniext"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_messages_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public String recuperarPorcentajeRecargoPorProducto(String cdramo,String cdperpag)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo"   , cdramo);
    	params.put("cdperpag" , cdperpag);
    	Map<String,Object>procResult=ejecutaSP(new RecuperarPorcentajeRecargoPorProducto(getDataSource()), params);
    	double recargo;
    	try
    	{
    		recargo=Double.parseDouble((String)procResult.get("pv_porcrcgo_o"));
    	}
    	catch(Exception ex)
    	{
    		logger.error(ex);
    		throw new ApplicationException("Error al obtener recargo por forma de pago");
    	}
    	String sRecargo=String.format("%.2f", recargo);
    	logger.debug(Utils.log("...P_GET_PORC_RECARGO result=",sRecargo));
    	return sRecargo;
    }
    
    protected class RecuperarPorcentajeRecargoPorProducto extends StoredProcedure
    {
    	protected RecuperarPorcentajeRecargoPorProducto(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_PORC_RECARGO");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("cdperpag" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_porcrcgo_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarValoresPantalla(
			String pantalla
			,String cdramo
			,String cdtipsit
			)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("pantalla" , pantalla);
    	params.put("cdramo"   , cdramo);
    	params.put("cdtipsit" , cdtipsit);
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarValoresPantalla(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_GET_VALORES_PANTALLA",params,lista);
    	return lista;
	}
    
    protected class RecuperarValoresPantalla extends StoredProcedure
    {
    	protected RecuperarValoresPantalla(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES2.P_GET_VALORES_PANTALLA");
    		declareParameter(new SqlParameter("pantalla" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"NAME","VALOR"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarValoresAtributosFactores(String cdramo,String cdtipsit)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo"   , cdramo);
    	params.put("cdtipsit" , cdtipsit);
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarValoresAtributosFactores(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_GET_VALORES_DEFECTO_FACTORES",params,lista);
    	return lista;
	}
    
    protected class RecuperarValoresAtributosFactores extends StoredProcedure
    {
    	protected RecuperarValoresAtributosFactores(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES2.P_GET_VALORES_DEFECTO_FACTORES");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"NAME","VALOR"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>obtieneContratantePoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String cdrol
			,String cdperson
			)throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("nmsituac" , nmsituac);
    	params.put("cdrol"    , cdrol);
    	params.put("cdperson" , cdperson);
    	Map<String,Object>procResult  = ejecutaSP(new ObtieneContratantePoliza(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_OBTIENE_MPOLIPER",params,lista);
    	return lista;
    }
    
    protected class ObtieneContratantePoliza extends StoredProcedure
    {
    	protected ObtieneContratantePoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES.P_OBTIENE_MPOLIPER");
    		declareParameter(new SqlParameter("cdunieco"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdrol" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
    		String[] cols=new String[]{
    				"NOMBRE","CDRFC","CDPERSON","CDIDEPER","CDIDEEXT", "NMSITUAC", "CDROL", "STATUS", "NMORDDOM", "SWRECLAM", "OTFISJUR", "DSNOMBRE","DSNOMBRE1","DSAPELLIDO","DSAPELLIDO1"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarPolizasEndosables(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmpoliex
			,String ramo
			,String cdagente
			,String statusVig
			,String finicio //Se agrega campo fecha de fin
			,String ffin//Se agrega campo fecha de fin
			,String cdsisrol
			)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco"  , cdunieco);
    	params.put("cdramo"    , cdramo);
    	params.put("estado"    , estado);
    	params.put("nmpoliza"  , nmpoliza);
    	params.put("nmpoliex"  , nmpoliex);
    	params.put("ramo"      , ramo);
    	params.put("cdagente"  , cdagente);
    	params.put("statusVig" , statusVig); 
    	params.put("finicio"   , finicio);//Se agrega campo fecha de inicio
    	params.put("ffin"      , ffin);//Se agrega campo fecha de fin
    	params.put("cdsisrol"  , cdsisrol);
    	
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarPolizasEndosables(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_GET_POLIZAS_PARA_ENDOSOS",params,lista);
    	return lista;
	}
    
    protected class RecuperarPolizasEndosables extends StoredProcedure
    {
    	protected RecuperarPolizasEndosables(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_POLIZAS_PARA_ENDOSOS");
            declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliex"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("ramo"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdagente"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("statusVig" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("finicio"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("ffin"      , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol"  , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		//MPOLIZAS
            		"CDUNIECO"  , "CDRAMO"   , "ESTADO"   , "NMPOLIZA"
            		,"NMSUPLEM" , "STATUS"   , "SWESTADO" , "NMSOLICI"
            		,"FEAUTORI" , "CDMOTANU" , "FEANULAC" , "SWAUTORI"
            		,"CDMONEDA" , "FEINISUS" , "FEFINSUS" , "OTTEMPOT"
            		,"FEEFECTO" , "HHEFECTO" , "FEPROREN" , "FEVENCIM"
            		,"NMRENOVA" , "FERECIBO" , "FEULTSIN" , "NMNUMSIN"
            		,"CDTIPCOA" , "SWTARIFI" , "SWABRIDO" , "FEEMISIO"
            		,"CDPERPAG" , "NMPOLIEX" , "NMCUADRO" , "PORREDAU"
            		,"SWCONSOL" , "NMPOLANT" , "NMPOLNVA" , "FESOLICI"
            		,"CDRAMANT" , "CDMEJRED" , "NMPOLDOC" , "NMPOLIZA2"
            		,"NMRENOVE" , "NMSUPLEE" , "TTIPCAMC" , "TTIPCAMV"
            		,"SWPATENT" , "NMPOLMST" , "PCPGOCTE" , "TIPOFLOT"
            		//MPERSONA
            		,"CDPERSON"   , "CDTIPIDE"    , "CDIDEPER" , "DSNOMBRE"
            		,"CDTIPPER"   , "OTFISJUR"    , "OTSEXO"   , "FENACIMI"
            		,"CDRFC"      , "FOTO"        , "DSEMAIL"  , "DSNOMBRE1"
            		,"DSAPELLIDO" , "DSAPELLIDO1" , "CDNACION" , "DSCOMNOM"
            		,"DSRAZSOC"   , "FEINGRESO"   , "FEACTUAL" , "DSNOMUSU"
            		,"CDESTCIV"   , "CDGRUECO"    , "CDSTIPPE" , "NMNUMNOM"
            		,"CURP"       , "CANALING"    , "CONDUCTO" , "PTCUMUPR"
            		,"STATUSPER"  , "RESIDENCIA"  , "NONGRATA" , "CDIDEEXT"
            		,"CDSUCEMI"
            		//TMESACONTROL
            		,"NTRAMITE"
            		//TVALOPOL
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
            		,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05"
            		,"DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
            		,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15"
            		,"DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
            		,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25"
            		,"DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
            		,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35"
            		,"DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
            		,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45"
            		,"DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
            		//OTROS
            		,"RAMO"
            		,"NMSITAUX", "CDTIPSIT"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarHistoricoPoliza(
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
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarHistoricoPoliza(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_GET_HISTORICO_POLIZA",params,lista);
    	return lista;
	}
    
    protected class RecuperarHistoricoPoliza extends StoredProcedure
    {
    	protected RecuperarHistoricoPoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_HISTORICO_POLIZA");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"NSUPLOGI" , "DSTIPSUP" , "FEEMISIO" , "FEINIVAL" , "FEFINVAL"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarIncisosPolizaGrupoFamilia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdgrupo
			,String nmfamili
			,String nivel
			,String start
			,String limit
			,String dsatribu
			,String otvalor
			)throws Exception
	{
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("cdgrupo"  , cdgrupo);
    	params.put("nmfamili" , nmfamili);
    	params.put("nivel"    , nivel);
    	params.put("start", start);
    	params.put("limit", limit);
    	params.put("dsatribu", dsatribu);
    	params.put("otvalor", otvalor);
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarIncisosPolizaGrupoFamilia(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Map<String,String>total = new HashMap<String,String>();
    	total.put("total", (String)procResult.get("pv_num_rec_o"));
    	lista.add(total);
    	construirClavesAtributos(lista);
    	Utils.debugProcedure(logger,"...P_GET_DATOS_INCISOS_F",params,lista);
    	return lista;
	}
    
    protected class RecuperarIncisosPolizaGrupoFamilia extends StoredProcedure
    {
    	protected RecuperarIncisosPolizaGrupoFamilia(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA_PRUEBA.P_GET_DATOS_INCISOS_F");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdgrupo"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmfamili" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("start" , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("limit" , OracleTypes.NUMBER));
            declareParameter(new SqlParameter("dsatribu" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("otvalor" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		//MPOLISIT
            		"CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
            		,"NMSITUAC"   , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
            		,"SWREDUCI"   , "CDAGRUPA" , "CDESTADO"   , "FEFECSIT"
            		,"FECHAREF"   , "CDGRUPO"  , "NMSITUAEXT" , "NMSITAUX"
            		,"NMSBSITEXT" , "CDPLAN"   , "CDASEGUR"   , "DSGRUPO"
            		//TVALOSIT
            		,"NMSUPLEM_TVAL"
            		,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05" , "OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
            		,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15" , "OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
            		,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25" , "OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
            		,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35" , "OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
            		,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45" , "OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
            		,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55" , "OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
            		,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65" , "OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR70"
            		,"OTVALOR71" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75" , "OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR80"
            		,"OTVALOR81" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85" , "OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR90"
            		,"OTVALOR91" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95" , "OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
            		,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05" , "DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
            		,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15" , "DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
            		,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25" , "DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
            		,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35" , "DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
            		,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45" , "DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
            		,"DSVALOR51" , "DSVALOR52" , "DSVALOR53" , "DSVALOR54" , "DSVALOR55" , "DSVALOR56" , "DSVALOR57" , "DSVALOR58" , "DSVALOR59" , "DSVALOR60"
            		,"DSVALOR61" , "DSVALOR62" , "DSVALOR63" , "DSVALOR64" , "DSVALOR65" , "DSVALOR66" , "DSVALOR67" , "DSVALOR68" , "DSVALOR69" , "DSVALOR70"
            		,"DSVALOR71" , "DSVALOR72" , "DSVALOR73" , "DSVALOR74" , "DSVALOR75" , "DSVALOR76" , "DSVALOR77" , "DSVALOR78" , "DSVALOR79" , "DSVALOR80"
            		,"DSVALOR81" , "DSVALOR82" , "DSVALOR83" , "DSVALOR84" , "DSVALOR85" , "DSVALOR86" , "DSVALOR87" , "DSVALOR88" , "DSVALOR89" , "DSVALOR90"
            		,"DSVALOR91" , "DSVALOR92" , "DSVALOR93" , "DSVALOR94" , "DSVALOR95" , "DSVALOR96" , "DSVALOR97" , "DSVALOR98" , "DSVALOR99"
            		//MPERSONA
            		,"CDPERSON"   , "CDTIPIDE"    , "CDIDEPER" , "DSNOMBRE"
            		,"CDTIPPER"   , "OTFISJUR"    , "OTSEXO"   , "FENACIMI"
            		,"CDRFC"      , "FOTO"        , "DSEMAIL"  , "DSNOMBRE1"
            		,"DSAPELLIDO" , "DSAPELLIDO1" , "CDNACION" , "DSCOMNOM"
            		,"DSRAZSOC"   , "FEINGRESO"   , "FEACTUAL" , "DSNOMUSU"
            		,"CDESTCIV"   , "CDGRUECO"    , "CDSTIPPE" , "NMNUMNOM"
            		,"CURP"       , "CANALING"    , "CONDUCTO" , "PTCUMUPR"
            		,"STATUSPER"  , "RESIDENCIA"  , "NONGRATA" , "CDIDEEXT"
            		,"CDSUCEMI"
            		//MPOLIPER
            		,"CDROL" , "NMORDDOM" , "SWRECLAM" , "SWEXIPER" , "CDPARENT" , "PORBENEF"
            		//TATRISIT:
            		,"DSATRIBU01" , "DSATRIBU02" , "DSATRIBU03" , "DSATRIBU04" , "DSATRIBU05" , "DSATRIBU06" , "DSATRIBU07" , "DSATRIBU08" , "DSATRIBU09" , "DSATRIBU10"
            		,"DSATRIBU11" , "DSATRIBU12" , "DSATRIBU13" , "DSATRIBU14" , "DSATRIBU15" , "DSATRIBU16" , "DSATRIBU17" , "DSATRIBU18" , "DSATRIBU19" , "DSATRIBU20"
            		,"DSATRIBU21" , "DSATRIBU22" , "DSATRIBU23" , "DSATRIBU24" , "DSATRIBU25" , "DSATRIBU26" , "DSATRIBU27" , "DSATRIBU28" , "DSATRIBU29" , "DSATRIBU30"
            		,"DSATRIBU31" , "DSATRIBU32" , "DSATRIBU33" , "DSATRIBU34" , "DSATRIBU35" , "DSATRIBU36" , "DSATRIBU37" , "DSATRIBU38" , "DSATRIBU39" , "DSATRIBU40"
            		,"DSATRIBU41" , "DSATRIBU42" , "DSATRIBU43" , "DSATRIBU44" , "DSATRIBU45" , "DSATRIBU46" , "DSATRIBU47" , "DSATRIBU48" , "DSATRIBU49" , "DSATRIBU50"
            		,"DSATRIBU51" , "DSATRIBU52" , "DSATRIBU53" , "DSATRIBU54" , "DSATRIBU55" , "DSATRIBU56" , "DSATRIBU57" , "DSATRIBU58" , "DSATRIBU59" , "DSATRIBU60"
            		,"DSATRIBU61" , "DSATRIBU62" , "DSATRIBU63" , "DSATRIBU64" , "DSATRIBU65" , "DSATRIBU66" , "DSATRIBU67" , "DSATRIBU68" , "DSATRIBU69" , "DSATRIBU70"
            		,"DSATRIBU71" , "DSATRIBU72" , "DSATRIBU73" , "DSATRIBU74" , "DSATRIBU75" , "DSATRIBU76" , "DSATRIBU77" , "DSATRIBU78" , "DSATRIBU79" , "DSATRIBU80"
            		,"DSATRIBU81" , "DSATRIBU82" , "DSATRIBU83" , "DSATRIBU84" , "DSATRIBU85" , "DSATRIBU86" , "DSATRIBU87" , "DSATRIBU88" , "DSATRIBU89" , "DSATRIBU90"
            		,"DSATRIBU91" , "DSATRIBU92" , "DSATRIBU93" , "DSATRIBU94" , "DSATRIBU95" , "DSATRIBU96" , "DSATRIBU97" , "DSATRIBU98" , "DSATRIBU99"
            		//MPLANES
            		,"DSPLAN"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_num_rec_o"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }

    @Override
    public List<Map<String,String>>recuperarDatosIncisoEnNivelPoliza(
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
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarDatosIncisoEnNivelPoliza(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	construirClavesAtributosIncisoEnPoliza(lista);
    	Utils.debugProcedure(logger,"...P_GET_DATOS_NIVEL_INC_Y_POLIZA",params,lista);
    	return lista;
    }
    
    protected class RecuperarDatosIncisoEnNivelPoliza extends StoredProcedure
    {
    	protected RecuperarDatosIncisoEnNivelPoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_DATOS_NIVEL_INC_Y_POLIZA");
    		declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
    		String[] cols=new String[]{
    				//MPOLISIT
    				"CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
    				,"NMSITUAC"   , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
    				,"SWREDUCI"   , "CDAGRUPA" , "CDESTADO"   , "FEFECSIT"
    				,"FECHAREF"   , "CDGRUPO"  , "NMSITUAEXT" , "NMSITAUX"
    				,"NMSBSITEXT" , "CDPLAN"   , "CDASEGUR"   , "DSGRUPO"
    				//TVALOSIT
    				,"NMSUPLEM_TVAL"
    				,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05" , "OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
    				,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15" , "OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
    				,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25" , "OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
    				,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35" , "OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
    				,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45" , "OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
    				,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55" , "OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
    				,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65" , "OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR70"
    				,"OTVALOR71" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75" , "OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR80"
    				,"OTVALOR81" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85" , "OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR90"
    				,"OTVALOR91" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95" , "OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
    				,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05" , "DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
    				,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15" , "DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
    				,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25" , "DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
    				,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35" , "DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
    				,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45" , "DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
    				,"DSVALOR51" , "DSVALOR52" , "DSVALOR53" , "DSVALOR54" , "DSVALOR55" , "DSVALOR56" , "DSVALOR57" , "DSVALOR58" , "DSVALOR59" , "DSVALOR60"
    				,"DSVALOR61" , "DSVALOR62" , "DSVALOR63" , "DSVALOR64" , "DSVALOR65" , "DSVALOR66" , "DSVALOR67" , "DSVALOR68" , "DSVALOR69" , "DSVALOR70"
    				,"DSVALOR71" , "DSVALOR72" , "DSVALOR73" , "DSVALOR74" , "DSVALOR75" , "DSVALOR76" , "DSVALOR77" , "DSVALOR78" , "DSVALOR79" , "DSVALOR80"
    				,"DSVALOR81" , "DSVALOR82" , "DSVALOR83" , "DSVALOR84" , "DSVALOR85" , "DSVALOR86" , "DSVALOR87" , "DSVALOR88" , "DSVALOR89" , "DSVALOR90"
    				,"DSVALOR91" , "DSVALOR92" , "DSVALOR93" , "DSVALOR94" , "DSVALOR95" , "DSVALOR96" , "DSVALOR97" , "DSVALOR98" , "DSVALOR99"
    				//MPERSONA
    				,"CDPERSON"   , "CDTIPIDE"    , "CDIDEPER" , "DSNOMBRE"
    				,"CDTIPPER"   , "OTFISJUR"    , "OTSEXO"   , "FENACIMI"
    				,"CDRFC"      , "FOTO"        , "DSEMAIL"  , "DSNOMBRE1"
    				,"DSAPELLIDO" , "DSAPELLIDO1" , "CDNACION" , "DSCOMNOM"
    				,"DSRAZSOC"   , "FEINGRESO"   , "FEACTUAL" , "DSNOMUSU"
    				,"CDESTCIV"   , "CDGRUECO"    , "CDSTIPPE" , "NMNUMNOM"
    				,"CURP"       , "CANALING"    , "CONDUCTO" , "PTCUMUPR"
    				,"STATUSPER"  , "RESIDENCIA"  , "NONGRATA" , "CDIDEEXT"
    				,"CDSUCEMI"
    				//MPOLIPER
    				,"CDROL" , "NMORDDOM" , "SWRECLAM" , "SWEXIPER" , "CDPARENT" , "PORBENEF"
    				//TATRISIT:
    				,"DSATRIBU01" , "DSATRIBU02" , "DSATRIBU03" , "DSATRIBU04" , "DSATRIBU05" , "DSATRIBU06" , "DSATRIBU07" , "DSATRIBU08" , "DSATRIBU09" , "DSATRIBU10"
    				,"DSATRIBU11" , "DSATRIBU12" , "DSATRIBU13" , "DSATRIBU14" , "DSATRIBU15" , "DSATRIBU16" , "DSATRIBU17" , "DSATRIBU18" , "DSATRIBU19" , "DSATRIBU20"
    				,"DSATRIBU21" , "DSATRIBU22" , "DSATRIBU23" , "DSATRIBU24" , "DSATRIBU25" , "DSATRIBU26" , "DSATRIBU27" , "DSATRIBU28" , "DSATRIBU29" , "DSATRIBU30"
    				,"DSATRIBU31" , "DSATRIBU32" , "DSATRIBU33" , "DSATRIBU34" , "DSATRIBU35" , "DSATRIBU36" , "DSATRIBU37" , "DSATRIBU38" , "DSATRIBU39" , "DSATRIBU40"
    				,"DSATRIBU41" , "DSATRIBU42" , "DSATRIBU43" , "DSATRIBU44" , "DSATRIBU45" , "DSATRIBU46" , "DSATRIBU47" , "DSATRIBU48" , "DSATRIBU49" , "DSATRIBU50"
    				,"DSATRIBU51" , "DSATRIBU52" , "DSATRIBU53" , "DSATRIBU54" , "DSATRIBU55" , "DSATRIBU56" , "DSATRIBU57" , "DSATRIBU58" , "DSATRIBU59" , "DSATRIBU60"
    				,"DSATRIBU61" , "DSATRIBU62" , "DSATRIBU63" , "DSATRIBU64" , "DSATRIBU65" , "DSATRIBU66" , "DSATRIBU67" , "DSATRIBU68" , "DSATRIBU69" , "DSATRIBU70"
    				,"DSATRIBU71" , "DSATRIBU72" , "DSATRIBU73" , "DSATRIBU74" , "DSATRIBU75" , "DSATRIBU76" , "DSATRIBU77" , "DSATRIBU78" , "DSATRIBU79" , "DSATRIBU80"
    				,"DSATRIBU81" , "DSATRIBU82" , "DSATRIBU83" , "DSATRIBU84" , "DSATRIBU85" , "DSATRIBU86" , "DSATRIBU87" , "DSATRIBU88" , "DSATRIBU89" , "DSATRIBU90"
    				,"DSATRIBU91" , "DSATRIBU92" , "DSATRIBU93" , "DSATRIBU94" , "DSATRIBU95" , "DSATRIBU96" , "DSATRIBU97" , "DSATRIBU98" , "DSATRIBU99"
    				//MPLANES
    				,"DSPLAN"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    private void construirClavesAtributosIncisoEnPoliza(List<Map<String,String>>lista)
	{
		for (Map<String,String> map : lista)
		{
			for(int i=1;i<=99;i++)
			{
				String dsatribu = map.get("DSATRIBU"+StringUtils.leftPad(String.valueOf(i), 2, "0"));
				if(StringUtils.isNotBlank(dsatribu))
				{
					map.put("CVE_P_"+dsatribu, map.get("OTVALOR"+StringUtils.leftPad(String.valueOf(i), 2, "0")));
					map.put("DES_P_"+dsatribu, map.get("DSVALOR"+StringUtils.leftPad(String.valueOf(i), 2, "0")));
				}
			}
		}
	}
    
    @Override
    public String recuperarValorAtributoUnico(
			String cdtipsit
			,String cdatribu
			,String otclave
			)throws Exception
	{
		
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdtipsit" , cdtipsit);
    	params.put("cdatribu" , cdatribu);
    	params.put("otclave"  , otclave);
    	Map<String,Object>procResult = ejecutaSP(new RecuperarValorAtributoUnico(getDataSource()),params);
    	String otvalor               = (String)procResult.get("pv_otvalor_o");
    	if(otvalor==null)
    	{
    		otvalor="";
    	}
    	logger.debug(Utils.log("...P_GET_OTVALOR_CAT_TATRISIT result=",otvalor));
    	return otvalor;
	}
    
    protected class RecuperarValorAtributoUnico extends StoredProcedure
    {
    	protected RecuperarValorAtributoUnico(DataSource dataSource)
    	{
    		super(dataSource , "PKG_SATELITES2.P_GET_OTVALOR_CAT_TATRISIT");
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("otclave"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_otvalor_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarGruposPoliza(
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
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarGruposPoliza(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_GET_GRUPOS_POLIZA",params,lista);
    	return lista;
	}
    
    protected class RecuperarGruposPoliza extends StoredProcedure
    {
    	protected RecuperarGruposPoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_GRUPOS_POLIZA");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"CDGRUPO" , "DSGRUPO"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>>recuperarFamiliasPoliza(
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
    	Map<String,Object>procResult  = ejecutaSP(new RecuperarFamiliasPoliza(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista=new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger,"...P_GET_FAMILIAS_POLIZA",params,lista);
    	return lista;
	}
    
    protected class RecuperarFamiliasPoliza extends StoredProcedure
    {
    	protected RecuperarFamiliasPoliza(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_FAMILIAS_POLIZA");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		"NMSITAUX" , "TITULAR"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    
    @Override
    public boolean esProductoSalud(String cdramo) throws Exception {
    	boolean esSalud = false;
    	Map<String,Object> params = new HashMap<String, Object>();
    	params.put("pv_cdramo_i" , cdramo);
    	Map<String,Object> result = ejecutaSP(new ValidaProductoSaludSP(getDataSource()),params);
    	if(Constantes.SI.equals(result.get("pv_swprosalud_o"))) {
    		esSalud = true;
    	}
    	return esSalud;
    }
    
    protected class ValidaProductoSaludSP extends StoredProcedure {
    	protected ValidaProductoSaludSP(DataSource dataSource) {
    		super(dataSource , "PKG_CONSULTA.P_VALIDA_PRODUCTO_SALUD");
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_swprosalud_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   ,  OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    ,  OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<String> recuperarDescripcionAtributosProducto(String cdramo) throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo" , cdramo);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarDescripcionAtributosProducto(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		throw new ApplicationException("No se encontraron nombres de atributos de situacion");
    	}
    	List<String>listaNombres = new ArrayList<String>();
    	for(Map<String,String>elem:lista)
    	{
    		listaNombres.add(elem.get("ATRIBUTO"));
    	}
    	Utils.debugProcedure(logger, "...P_GET_DSATRIBUS_TATRISIT", params, listaNombres);
    	return listaNombres;
    }
    
    protected class RecuperarDescripcionAtributosProducto extends StoredProcedure
    {
    	protected RecuperarDescripcionAtributosProducto(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_DSATRIBUS_TATRIPOL");
    		declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"ATRIBUTO"})));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   ,  OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    ,  OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @Override
    public List<String> recuperarDescripcionAtributosSituacionPorRamo(String cdramo) throws Exception
    {
    	Map<String,String>params=new LinkedHashMap<String,String>();
    	params.put("cdramo" , cdramo);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarDescripcionAtributosSituacionPorRamo(getDataSource()),params);
    	List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		throw new ApplicationException("No se encontraron nombres de atributos de situacion");
    	}
    	List<String>listaNombres = new ArrayList<String>();
    	for(Map<String,String>elem:lista)
    	{
    		listaNombres.add(elem.get("ATRIBUTO"));
    	}
    	Utils.debugProcedure(logger, "...P_GET_DSATRIBUS_TATRISIT", params, listaNombres);
    	return listaNombres;
    }
    
    protected class RecuperarDescripcionAtributosSituacionPorRamo extends StoredProcedure
    {
    	protected RecuperarDescripcionAtributosSituacionPorRamo(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_DSATRIBUS_TATRISIT");
            declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"ATRIBUTO"})));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   ,  OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    ,  OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,String> recuperarFechasLimiteEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String cdsisrol
			,String cdusuari
			,String cdtipsup
			)throws Exception
	{
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("estado"   , estado);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("cdsisrol" , cdsisrol);
    	params.put("cdusuari" , cdusuari);
    	params.put("cdtipsup" , cdtipsup);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarFechasLimiteEndoso(getDataSource()),params);
    	Map<String,String> result     = new HashMap<String,String>();
    	result.put("FECHA_MINIMA" , (String)procResult.get("pv_fechamin_o"));
    	result.put("FECHA_MAXIMA" , (String)procResult.get("pv_fechamax_o"));
    	result.put("FECHA_REFERENCIA" , (String)procResult.get("pv_fecharef_o"));
    	result.put("EDITABLE"     , (String)procResult.get("pv_editable_o"));
    	logger.debug(Utils.log("...P_GET_FECHAS_ENDOSO mapa=",result));
    	return result;
	}
    
    protected class RecuperarFechasLimiteEndoso extends StoredProcedure
    {
    	protected RecuperarFechasLimiteEndoso(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_FECHAS_ENDOSO");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_fechamin_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_fechamax_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_fecharef_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_editable_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>> recuperarEndososRehabilitables(
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
    	Map<String,Object>       procResult = ejecutaSP(new RecuperarEndososRehabilitables(getDataSource()),params);
    	List<Map<String,String>> lista      = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista = new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger, "...P_GET_ENDOSOS_X_POLIZA_A_REHAB", params, lista);
    	return lista;
	}
    
    protected class RecuperarEndososRehabilitables extends StoredProcedure
    {
    	protected RecuperarEndososRehabilitables(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CANCELA.P_GET_ENDOSOS_X_POLIZA_A_REHAB");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"NSUPLOGI"  , "CDDEVCIA" , "CDGESTOR" , "FEEMISIO" , "FEINIVAL" , "FEFINVAL"
            		,"FEEFECTO" , "FEPROREN" , "CDMONEDA" , "NMSUPLEM"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public List<Map<String,String>> recuperarEndososCancelables(
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
    	Map<String,Object>       procResult = ejecutaSP(new RecuperarEndososCancelables(getDataSource()),params);
    	List<Map<String,String>> lista      = (List<Map<String,String>>)procResult.get("pv_registro_o");
    	if(lista==null)
    	{
    		lista = new ArrayList<Map<String,String>>();
    	}
    	Utils.debugProcedure(logger, "...P_GET_ENDOSOS_X_POLIZA_A_CANC", params, lista);
    	return lista;
	}
    
    protected class RecuperarEndososCancelables extends StoredProcedure
    {
    	protected RecuperarEndososCancelables(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CANCELA.P_GET_ENDOSOS_X_POLIZA_A_CANC");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"NSUPLOGI"  , "CDDEVCIA" , "CDGESTOR" , "FEEMISIO" , "FEINIVAL" , "FEFINVAL"
            		,"FEEFECTO" , "FEPROREN" , "CDMONEDA" , "NMSUPLEM" , "FEINICIO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public boolean recuperarPermisoDevolucionPrimasUsuario(String cdusuari) throws Exception
    {
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("cdusuari" , cdusuari);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarPermisoDevolucionPrimasUsuario(getDataSource()),params);
    	logger.debug(Utils.log("****** ...P_GET_PERM_DEVOL_PRI_X_USUA permiso=",procResult.get("pv_permiso_o")));
    	return "S".equals((String)procResult.get("pv_permiso_o"));
    }
    
    protected class RecuperarPermisoDevolucionPrimasUsuario extends StoredProcedure
    {
    	protected RecuperarPermisoDevolucionPrimasUsuario(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_PERM_DEVOL_PRI_X_USUA");
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_permiso_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public String recuperarValorMaximoSituacionPorRol(String cdtipsit,String cdsisrol) throws Exception
    {
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("cdtipsit" , cdtipsit);
    	params.put("cdsisrol" , cdsisrol);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarValorMaximoSituacionPorRol(getDataSource()),params);
    	String valor = (String)procResult.get("pv_valor_o");
    	if(valor==null)
    	{
    		valor = "9999999";
    	}
    	logger.debug(Utils.log("\n****** ...P_GET_VALMAX_X_ROL valor=",valor));
    	return valor;
    }
    
    protected class RecuperarValorMaximoSituacionPorRol extends StoredProcedure
    {
    	protected RecuperarValorMaximoSituacionPorRol(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_VALMAX_X_ROL");
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_valor_o"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public String obtieneSubramoGS(String cdramo,String cdtipsit) throws Exception{
    	
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("pv_cdramo_i" , cdramo);
    	params.put("pv_cdtipsit_i" , cdtipsit);
    	Map<String,Object> procResult = ejecutaSP(new ObtieneSubramoGS(getDataSource()),params);
    	String valor = (String)procResult.get("pv_cdsubram_o");
    	logger.debug(Utils.log("\n****** ...P_OBTIENE_SUBRAMO_X_CDTIPSIT=",valor));
    	return valor;
	}
    
    protected class ObtieneSubramoGS extends StoredProcedure
    {
    	protected ObtieneSubramoGS(DataSource dataSource)
    	{
    		super(dataSource , "Pkg_Consulta.P_OBTIENE_SUBRAMO_X_CDTIPSIT");
            declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdsubram_o"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public String recuperarCdtipsitExtraExcel(
			int fila
			,String proc
			,String param1
			,String param2
			,String param3
			)throws Exception
    {
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("param1" , param1);
    	params.put("param2" , param2);
    	params.put("param3" , param3);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarCdtipsitExtraExcel(getDataSource(),proc),params);
    	logger.debug(Utils.log("\n****** procedimiento=",proc,"resultado=",procResult));
    	String valor = (String) procResult.get("pv_cdtipsit_o");
    	if(StringUtils.isBlank(valor))
    	{
//    		
//    	}
//    	else if(){
//    	    
//    	}
//    	else
//    	{
    	    throw new ApplicationException(
                    Utils.join("No se encuentra tipo de situacion con ",param1,", ",param2," y ",param3," en la fila ",fila)
                    );
    	}
    	return valor;
    }
    
    protected class RecuperarCdtipsitExtraExcel extends StoredProcedure
    {
    	protected RecuperarCdtipsitExtraExcel(DataSource dataSource, String proc)
    	{
    		super(dataSource , proc);
            declareParameter(new SqlParameter("param1" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("param2" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("param3" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdtipsit_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,String>recuperarCotizacionFlotillas(String cdramo,String nmpoliza,String cdusuari,String cdsisrol) throws Exception
    {
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("cdramo"   , cdramo);
    	params.put("nmpoliza" , nmpoliza);
    	params.put("cdusuari" , cdusuari);
    	params.put("cdsisrol" , cdsisrol);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarCotizacionFlotillas(getDataSource()),params);
    	Map<String,String> result     = new LinkedHashMap<String,String>();
    	result.put("cdunieco"       , (String)procResult.get("pv_cdunieco_o"));
    	result.put("estado"         , (String)procResult.get("pv_estado_o"));
    	result.put("nmpoliza"       , (String)procResult.get("pv_nmpoliza_o"));
    	result.put("nmsuplem"       , (String)procResult.get("pv_nmsuplem_o"));
    	result.put("tipoflot"       , (String)procResult.get("pv_tipoflot_o"));
    	result.put("fesolici"       , (String)procResult.get("pv_fesolici_o"));
    	result.put("feini"          , (String)procResult.get("pv_feini_o"));
    	result.put("fefin"          , (String)procResult.get("pv_fefin_o"));
    	result.put("ntramiteLigado" , (String)procResult.get("pv_ntramite_ligado_o"));
    	logger.debug(Utils.log("\npoliza=",result));
    	return result;
    }
    
    protected class RecuperarCotizacionFlotillas extends StoredProcedure
    {
    	protected RecuperarCotizacionFlotillas(DataSource dataSource)
    	{
    		super(dataSource , "PKG_CONSULTA.P_GET_DATOS_POLIZA_FLOTILLAS");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdunieco_o"        , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_estado_o"          , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmpoliza_o"        , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsuplem_o"        , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_tipoflot_o"        , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_fesolici_o"        , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_feini_o"           , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_fefin_o"           , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_ntramite_ligado_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"          , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"           , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,List<Map<String,String>>> recuperarEstadisticasCotizacionEmision(
			Date feinicio
			,Date fefin
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdagente
			) throws Exception
	{
    	Map<String,Object> params = new LinkedHashMap<String,Object>();
    	params.put("feinicio" , feinicio);
    	params.put("fefin"    , fefin);
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("cdagente" , cdagente);
    	params.put("cdusuari" , cdusuari);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarEstadisticasCotizacionEmision(getDataSource()),params);
    	
    	Map<String,List<Map<String,String>>> mapa = new HashMap<String,List<Map<String,String>>>();
    	
    	List<Map<String,String>> listaUnieco = (List<Map<String,String>>)procResult.get("pv_reg_unieco_o");
    	if(listaUnieco==null)
    	{
    		listaUnieco=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista unieco=",listaUnieco));
    	mapa.put("unieco",listaUnieco);
    	
    	List<Map<String,String>> listaRamo = (List<Map<String,String>>)procResult.get("pv_reg_ramo_o");
    	if(listaRamo==null)
    	{
    		listaRamo=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista ramo=",listaRamo));
    	mapa.put("ramo",listaRamo);
    	
    	List<Map<String,String>> listaUsuario = (List<Map<String,String>>)procResult.get("pv_reg_usuario_o");
    	if(listaUsuario==null)
    	{
    		listaUsuario=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista usuario=",listaUsuario));
    	mapa.put("usuario",listaUsuario);
    	
    	List<Map<String,String>> listaAgente = (List<Map<String,String>>)procResult.get("pv_reg_agente_o");
    	if(listaAgente==null)
    	{
    		listaAgente=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista agente=",listaAgente));
    	mapa.put("agente",listaAgente);
    	
    	return mapa;
	}
    
    protected class RecuperarEstadisticasCotizacionEmision extends StoredProcedure
    {
    	protected RecuperarEstadisticasCotizacionEmision(DataSource dataSource)
    	{
    		super(dataSource , "pkg_estadistica.pr_estadistica_4");
            declareParameter(new SqlParameter("feinicio" , OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter("fefin"    , OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o"   , OracleTypes.CURSOR));
            declareParameter(new SqlOutParameter("pv_reg_unieco_o" , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDUNIECO" , "DSUNIECO" , "SUCURSAL" , "COTIZACIONES" , "EMISIONES"})));
            declareParameter(new SqlOutParameter("pv_reg_ramo_o"   , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDRAMO" , "DSRAMO" , "PRODUCTO" , "COTIZACIONES" , "EMISIONES"})));
            declareParameter(new SqlOutParameter("pv_reg_usuario_o"   , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDUSUARI" , "DSUSUARI" , "USUARIO" , "COTIZACIONES" , "EMISIONES"})));
            declareParameter(new SqlOutParameter("pv_reg_agente_o"   , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDAGENTE" , "NOMBRE" , "AGENTE" , "COTIZACIONES" , "EMISIONES"})));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
            compile();
    	}
    }
    
    @Override
    public Map<String,List<Map<String,String>>> recuperarEstadisticasTareas(
			Date feinicio
			,Date fefin
			,String cdmodulo
			,String cdtarea
			,String cdunieco
			,String cdramo
			,String cdusuari
			,String cdsisrol
			) throws Exception
	{
    	Map<String,Object> params = new LinkedHashMap<String,Object>();
    	params.put("feinicio" , feinicio);
    	params.put("fefin"    , fefin);
    	params.put("cdmodulo" , cdmodulo);
    	params.put("cdtarea"  , cdtarea);
    	params.put("cdunieco" , cdunieco);
    	params.put("cdramo"   , cdramo);
    	params.put("cdsisrol" , cdsisrol);
    	params.put("cdusuari" , cdusuari);
    	Map<String,Object> procResult = ejecutaSP(new RecuperarEstadisticasTareas(getDataSource()),params);
    	
    	Map<String,List<Map<String,String>>> mapa = new HashMap<String,List<Map<String,String>>>();
    	
    	List<Map<String,String>> listaTarea = (List<Map<String,String>>)procResult.get("pv_reg_tarea_o");
    	if(listaTarea==null)
    	{
    		listaTarea=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista tarea=",listaTarea));
    	mapa.put("tarea",listaTarea);
    	
    	List<Map<String,String>> listaUnieco = (List<Map<String,String>>)procResult.get("pv_reg_unieco_o");
    	if(listaUnieco==null)
    	{
    		listaUnieco=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista unieco=",listaUnieco));
    	mapa.put("unieco",listaUnieco);
    	
    	List<Map<String,String>> listaRamo = (List<Map<String,String>>)procResult.get("pv_reg_ramo_o");
    	if(listaRamo==null)
    	{
    		listaRamo=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista ramo=",listaRamo));
    	mapa.put("ramo",listaRamo);
    	
    	List<Map<String,String>> listaUsuario = (List<Map<String,String>>)procResult.get("pv_reg_usuari_o");
    	if(listaUsuario==null)
    	{
    		listaUsuario=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista usuario=",listaUsuario));
    	mapa.put("usuario",listaUsuario);
    	
    	List<Map<String,String>> listaRol = (List<Map<String,String>>)procResult.get("pv_reg_sisrol_o");
    	if(listaRol==null)
    	{
    		listaRol=new ArrayList<Map<String,String>>();
    	}
    	logger.debug(Utils.log("\nlista rol=",listaRol));
    	mapa.put("rol",listaRol);
    	
    	return mapa;
	}
    
    protected class RecuperarEstadisticasTareas extends StoredProcedure
    {
    	protected RecuperarEstadisticasTareas(DataSource dataSource)
    	{
    		super(dataSource , "pkg_estadistica.P_GET_ESTADISTICA_TAREAS");
            declareParameter(new SqlParameter("feinicio" , OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter("fefin"    , OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter("cdmodulo" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtarea"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_reg_tarea_o"  , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDTAREA" , "DSTAREA" , "TAREA" , "TODAS" , "TIEMPO" , "ESCALA"})));
            declareParameter(new SqlOutParameter("pv_reg_unieco_o" , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDUNIECO" , "DSUNIECO" , "SUCURSAL" , "TODAS" , "TIEMPO" , "ESCALA"})));
            declareParameter(new SqlOutParameter("pv_reg_ramo_o"   , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDRAMO" , "DSRAMO" , "PRODUCTO" , "TODAS" , "TIEMPO" , "ESCALA"})));
            declareParameter(new SqlOutParameter("pv_reg_sisrol_o" , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDSISROL" , "DSSISROL" , "ROL" , "TODAS" , "TIEMPO" , "ESCALA"})));
            declareParameter(new SqlOutParameter("pv_reg_usuari_o" , OracleTypes.CURSOR
            		,new GenericMapper(new String[]{"CDUSUARI" , "DSUSUARI" , "USUARIO" , "TODAS" , "TIEMPO" , "ESCALA"})));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public String obtieneConteoSituacionCoberturaAmparada(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsit
			,String cdatribu
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdtipsit" , cdtipsit);
		params.put("cdatribu" , cdatribu);
		Map<String,Object> procResult = ejecutaSP(new ObtieneAtributosSituacionCoberturaAmparada(getDataSource()),params);
		String             conteo     = (String)procResult.get("pv_conteo_o");
		if(StringUtils.isBlank(conteo))
		{
			conteo="0";
		}
		logger.debug(Utils.log(
				 "\n**************************************************"
				,"\n****** ...P_GET_TATRISIT_AMPARADO ******"
				,"\n****** params=" , params
				,"\n****** conteo=" , conteo
				,"\n**************************************************"
				));
		return conteo;
	}
	
	protected class ObtieneAtributosSituacionCoberturaAmparada extends StoredProcedure
	{
		protected ObtieneAtributosSituacionCoberturaAmparada(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TATRISIT_AMPARADO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_conteo_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String validacionesSuplemento(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdtipsup
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdtipsup" , cdtipsup);
		Map<String,Object> procResult = ejecutaSP(new ValidacionesSuplemento(getDataSource()),params);
		String             error      = (String)procResult.get("pv_error_o");
		logger.debug(Utils.log(
				 "\n************************************************"
				,"\n****** params=" , params
				,"\n****** error="  , error
				,"\n****** ...P_VALIDA_SUPLEMENTO ******"
				,"\n************************************************"
				));
		return error;
	}
	
	protected class ValidacionesSuplemento extends StoredProcedure
	{
		protected ValidacionesSuplemento(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_VALIDA_SUPLEMENTO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_error_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarRevisionColectivosEndosos(
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
		Map<String,Object> procResult  = ejecutaSP(new RecuperarRevisionColectivosEndosos(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarRevisionColectivosEndosos extends StoredProcedure
	{
		protected RecuperarRevisionColectivosEndosos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_REVISION_COLECTIVOEND");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
            		"CDUNIECO"  , "CDRAMO"     , "ESTADO" , "NMPOLIZA" , "CDGRUPO"
            		,"NMSITUAC" , "PARENTESCO" , "NOMBRE" , "EDAD"     , "SEXO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> recuperarRevisionColectivos(
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
		Map<String,Object> procResult  = ejecutaSP(new RecuperarRevisionColectivos(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarRevisionColectivos extends StoredProcedure
	{
		protected RecuperarRevisionColectivos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_REVISION_COLECTIVO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
            		"CDUNIECO"  , "CDRAMO"     , "ESTADO" , "NMPOLIZA" , "CDGRUPO"
            		,"NMSITUAC" , "PARENTESCO" , "NOMBRE" , "EDAD"     , "SEXO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> recuperarRevisionColectivosFinal(
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
		Map<String,Object> procResult  = ejecutaSP(new RecuperarRevisionColectivosFinal(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarRevisionColectivosFinal extends StoredProcedure
	{
		protected RecuperarRevisionColectivosFinal(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ZWCENSOTRAD");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO"  , "CDRAMO"     , "ESTADO" , "NMPOLIZA" , "CDGRUPO"
					,"NMSITUAC" , "PARENTESCO" , "NOMBRE" , "EDAD"     , "SEXO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public boolean copiaDocumentosTdocupol(
			 String cduniecoOrig
			,String cdramoOrig
			,String estadoOrig
			,String nmpolizaOrig
			,String ntramiteDestino
			)throws Exception
			{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i" , cduniecoOrig);
		params.put("pv_cdramo_i"   , cdramoOrig);
		params.put("pv_estado_i"   , estadoOrig);
		params.put("pv_nmpoliza_i" , nmpolizaOrig);
		params.put("pv_ntramite_i" , ntramiteDestino);
		ejecutaSP(new CopiaDocumentosTdocupol(getDataSource()),params);
		return true;
	}
	
	protected class CopiaDocumentosTdocupol extends StoredProcedure
	{
		protected CopiaDocumentosTdocupol(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_INS_TDOCUPOL_DOCTOS_USUARIO");
			declareParameter(new SqlParameter("pv_cdunieco_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarDerechosPolizaPorPaqueteRamo1(String paquete) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("paquete" , paquete);
		Map<String,Object> procRes = ejecutaSP(new RecuperarDerechosPolizaPorPaqueteRamo1(getDataSource()),params);
		String derechos = (String) procRes.get("pv_derechos_o");
		if(StringUtils.isBlank(derechos))
		{
			throw new ApplicationException("No hay derechos parametrizados para el paquete");
		}
		logger2.debug("\nRecuperar derechos con paquete {}\nDerechos: {}" , paquete , derechos);
		return derechos;
	}
	
	protected class RecuperarDerechosPolizaPorPaqueteRamo1 extends StoredProcedure
	{
		protected RecuperarDerechosPolizaPorPaqueteRamo1(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DERPOL_X_PAQ_RAMO1");
			declareParameter(new SqlParameter("paquete" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_derechos_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public boolean validaPagoPolizaRepartido(
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
		Map<String,Object> procRes = ejecutaSP(new ValidaPagoPolizaRepartido(getDataSource()),params);
		boolean pagoRepartido = "S".equals((String)procRes.get("pv_repartido_o"));
		logger2.debug("\n ...P_GET_SWCONTRIBUTORIO pagoRepartido: {}",pagoRepartido);
		return pagoRepartido;
	}
	
	protected class ValidaPagoPolizaRepartido extends StoredProcedure
	{
		protected ValidaPagoPolizaRepartido(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_SWCONTRIBUTORIO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_repartido_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarAtributosPorRol(String cdtipsit,String cdsisrol) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipsit" , cdtipsit);
		params.put("cdsisrol" , cdsisrol);
		Map<String,Object> procRes = ejecutaSP(new RecuperarAtributosPorRol(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarAtributosPorRol extends StoredProcedure
	{
		protected RecuperarAtributosPorRol(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ATRIXROL_AUTOS");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR
					,new GenericMapper(new String[]{"CDATRIBU","APLICA","VALOR"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public boolean validaClientePideNumeroEmpleado(
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
		Map<String,Object> procRes = ejecutaSP(new ValidaClientePideNumeroEmpleado(getDataSource()),params);
		boolean pide = "S".equals((String)procRes.get("pv_swempleado_o"));
		logger2.debug("\n ...P_VALIDA_CLIENTE_NEMP pide= {}",pide);
		return pide;
	}
	
	protected class ValidaClientePideNumeroEmpleado extends StoredProcedure
	{
		protected ValidaClientePideNumeroEmpleado(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_VALIDA_CLIENTE_NEMP");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_swempleado_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>recuperarUsuariosReasignacionTramite(String ntramite, String cdusuari, String cdsisrol) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_ntramite_i" , ntramite);
		params.put("pv_cdusuari_i" , cdusuari);
		params.put("pv_cdsisrol_i" , cdsisrol);
		Map<String,Object> procRes = ejecutaSP(new RecuperarUsuariosReasignacionTramite(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException("No se encontraron usuarios");
		}
		for (Map<String, String> user : lista) {
		    new ContarTramitesUsuarioRol(user).start(); // Mando N hilos
		}
		int respuestas = 0;
		while (respuestas < lista.size()) { // Mientras tenga hilos pendientes
		    Thread.sleep(100);
		    respuestas = 0;
		    for (Map<String, String> user : lista) { // Cuento los hilos que han respondido
	            if (user.containsKey("TOTAL")) {
	                respuestas = respuestas + 1;
	            }
	        }
		}
		return lista;
	}
	
	protected class RecuperarUsuariosReasignacionTramite extends StoredProcedure
	{
		protected RecuperarUsuariosReasignacionTramite(DataSource dataSource)
		{
			super(dataSource,"P_MC_GET_USUARIOS_REASIGNA");
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
			        "NTRAMITE", "CDUSUARI_ACTUAL", "STATUS_ACTUAL", "CDSISROL_ACTUAL",
			        "CDUSUARI", "CDSISROL", "DSUSUARI", "STATUS", "DSSISROL"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public boolean validarVentanaDocumentosBloqueada(
			String ntramite
			,String cdtiptra
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("cdtiptra" , cdtiptra);
		params.put("cdusuari" , cdusuari);
		params.put("cdsisrol" , cdsisrol);
		Map<String,Object> procRes = ejecutaSP(new ValidarVentanaDocumentosBloqueada(getDataSource()),params);
		return "S".equals((String)procRes.get("pv_swbloqueo_o"));
	}
	
	protected class ValidarVentanaDocumentosBloqueada extends StoredProcedure
	{
		protected ValidarVentanaDocumentosBloqueada(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_VALIDA_VENT_DOCS_BLOQUEADA");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtiptra" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_swbloqueo_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarMovimientosEndosoAltaBajaAsegurados(
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
		Map<String,Object>       procRes = ejecutaSP(new RecuperarMovimientosEndosoAltaBajaAsegurados(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		construirClavesAtributos(lista);
		return lista;
	}
	
	protected class RecuperarMovimientosEndosoAltaBajaAsegurados extends StoredProcedure
	{
		protected RecuperarMovimientosEndosoAltaBajaAsegurados(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_MOV_ALTA_BAJA_ASEG");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            String[] cols=new String[]{
            		//MPOLISIT
            		"CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
            		,"NMSITUAC"   , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
            		,"SWREDUCI"   , "CDAGRUPA" , "CDESTADO"   , "FEFECSIT"
            		,"FECHAREF"   , "CDGRUPO"  , "NMSITUAEXT" , "NMSITAUX"
            		,"NMSBSITEXT" , "CDPLAN"   , "CDASEGUR"   , "DSGRUPO"
            		//TVALOSIT
            		,"NMSUPLEM_TVAL"
            		,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05" , "OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
            		,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15" , "OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
            		,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25" , "OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
            		,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35" , "OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
            		,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45" , "OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
            		,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55" , "OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
            		,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65" , "OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR70"
            		,"OTVALOR71" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75" , "OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR80"
            		,"OTVALOR81" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85" , "OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR90"
            		,"OTVALOR91" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95" , "OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
            		,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05" , "DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
            		,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15" , "DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
            		,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25" , "DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
            		,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35" , "DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
            		,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45" , "DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
            		,"DSVALOR51" , "DSVALOR52" , "DSVALOR53" , "DSVALOR54" , "DSVALOR55" , "DSVALOR56" , "DSVALOR57" , "DSVALOR58" , "DSVALOR59" , "DSVALOR60"
            		,"DSVALOR61" , "DSVALOR62" , "DSVALOR63" , "DSVALOR64" , "DSVALOR65" , "DSVALOR66" , "DSVALOR67" , "DSVALOR68" , "DSVALOR69" , "DSVALOR70"
            		,"DSVALOR71" , "DSVALOR72" , "DSVALOR73" , "DSVALOR74" , "DSVALOR75" , "DSVALOR76" , "DSVALOR77" , "DSVALOR78" , "DSVALOR79" , "DSVALOR80"
            		,"DSVALOR81" , "DSVALOR82" , "DSVALOR83" , "DSVALOR84" , "DSVALOR85" , "DSVALOR86" , "DSVALOR87" , "DSVALOR88" , "DSVALOR89" , "DSVALOR90"
            		,"DSVALOR91" , "DSVALOR92" , "DSVALOR93" , "DSVALOR94" , "DSVALOR95" , "DSVALOR96" , "DSVALOR97" , "DSVALOR98" , "DSVALOR99"
            		//MPERSONA
            		,"CDPERSON"   , "CDTIPIDE"    , "CDIDEPER" , "DSNOMBRE"
            		,"CDTIPPER"   , "OTFISJUR"    , "OTSEXO"   , "FENACIMI"
            		,"CDRFC"      , "FOTO"        , "DSEMAIL"  , "DSNOMBRE1"
            		,"DSAPELLIDO" , "DSAPELLIDO1" , "CDNACION" , "DSCOMNOM"
            		,"DSRAZSOC"   , "FEINGRESO"   , "FEACTUAL" , "DSNOMUSU"
            		,"CDESTCIV"   , "CDGRUECO"    , "CDSTIPPE" , "NMNUMNOM"
            		,"CURP"       , "CANALING"    , "CONDUCTO" , "PTCUMUPR"
            		,"STATUSPER"  , "RESIDENCIA"  , "NONGRATA" , "CDIDEEXT"
            		,"CDSUCEMI"
            		//MPOLIPER
            		,"CDROL" , "NMORDDOM" , "SWRECLAM" , "SWEXIPER" , "CDPARENT" , "PORBENEF"
            		//TATRISIT:
            		,"DSATRIBU01" , "DSATRIBU02" , "DSATRIBU03" , "DSATRIBU04" , "DSATRIBU05" , "DSATRIBU06" , "DSATRIBU07" , "DSATRIBU08" , "DSATRIBU09" , "DSATRIBU10"
            		,"DSATRIBU11" , "DSATRIBU12" , "DSATRIBU13" , "DSATRIBU14" , "DSATRIBU15" , "DSATRIBU16" , "DSATRIBU17" , "DSATRIBU18" , "DSATRIBU19" , "DSATRIBU20"
            		,"DSATRIBU21" , "DSATRIBU22" , "DSATRIBU23" , "DSATRIBU24" , "DSATRIBU25" , "DSATRIBU26" , "DSATRIBU27" , "DSATRIBU28" , "DSATRIBU29" , "DSATRIBU30"
            		,"DSATRIBU31" , "DSATRIBU32" , "DSATRIBU33" , "DSATRIBU34" , "DSATRIBU35" , "DSATRIBU36" , "DSATRIBU37" , "DSATRIBU38" , "DSATRIBU39" , "DSATRIBU40"
            		,"DSATRIBU41" , "DSATRIBU42" , "DSATRIBU43" , "DSATRIBU44" , "DSATRIBU45" , "DSATRIBU46" , "DSATRIBU47" , "DSATRIBU48" , "DSATRIBU49" , "DSATRIBU50"
            		,"DSATRIBU51" , "DSATRIBU52" , "DSATRIBU53" , "DSATRIBU54" , "DSATRIBU55" , "DSATRIBU56" , "DSATRIBU57" , "DSATRIBU58" , "DSATRIBU59" , "DSATRIBU60"
            		,"DSATRIBU61" , "DSATRIBU62" , "DSATRIBU63" , "DSATRIBU64" , "DSATRIBU65" , "DSATRIBU66" , "DSATRIBU67" , "DSATRIBU68" , "DSATRIBU69" , "DSATRIBU70"
            		,"DSATRIBU71" , "DSATRIBU72" , "DSATRIBU73" , "DSATRIBU74" , "DSATRIBU75" , "DSATRIBU76" , "DSATRIBU77" , "DSATRIBU78" , "DSATRIBU79" , "DSATRIBU80"
            		,"DSATRIBU81" , "DSATRIBU82" , "DSATRIBU83" , "DSATRIBU84" , "DSATRIBU85" , "DSATRIBU86" , "DSATRIBU87" , "DSATRIBU88" , "DSATRIBU89" , "DSATRIBU90"
            		,"DSATRIBU91" , "DSATRIBU92" , "DSATRIBU93" , "DSATRIBU94" , "DSATRIBU95" , "DSATRIBU96" , "DSATRIBU97" , "DSATRIBU98" , "DSATRIBU99"
            		//MPLANES
            		,"DSPLAN"
            		//MOVIMIENTOS
            		,"MOV"
    	            };
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	private void construirClavesAtributos(List<Map<String,String>>lista)
	{
		for (Map<String,String> map : lista)
		{
			for(int i=1;i<=99;i++)
			{
				String dsatribu = map.get("DSATRIBU"+StringUtils.leftPad(String.valueOf(i), 2, "0"));
				if(StringUtils.isNotBlank(dsatribu))
				{
					map.put("CVE_"+dsatribu, map.get("OTVALOR"+StringUtils.leftPad(String.valueOf(i), 2, "0")));
					map.put("DES_"+dsatribu, map.get("DSVALOR"+StringUtils.leftPad(String.valueOf(i), 2, "0")));
				}
			}
			if(map.get("DES_MODELO")==null && map.get("DES_MODELO__FRONTERIZO_")!=null){
			    String modelo=map.get("DES_MODELO__FRONTERIZO_");
	            map.put("DES_MODELO", modelo);
	            map.put("CLV_MODELO", modelo);
	            String key =getKeyByValue(map, "MODELO").substring(8);
	            logger.debug("key: -> "+key);
                map.put("OTVALOR"+key,modelo);
                map.put("DSVALOR"+key,modelo);
	        }
			if(map.get("DES_MARCA")==null && map.get("DES_MARCA__FRONTERIZO_")!=null){
			    String marca = map.get("DES_MARCA__FRONTERIZO_");
                map.put("DES_MARCA", marca );
                map.put("CLV_MARCA", marca);
                String keyMarca =getKeyByValue(map, "MARCA").substring(8);
                map.put("OTVALOR"+keyMarca,marca);
                map.put("DSVALOR"+keyMarca,marca);
            }
			if(map.get("DES_VERSION")==null && map.get("DES_VERSION__FRONTERIZO_")!=null){
			    String version = map.get("DES_VERSION__FRONTERIZO_");
                map.put("DES_VERSION", version);
                map.put("CLV_VERSION", version);
                String key =getKeyByValue(map, "VERSION").substring(8);
                map.put("OTVALOR"+key,version);
                map.put("DSVALOR"+key,version);
            }
			
		}
		logger.debug("lista--> "+lista);
		
		
	}
	
	private <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	@Override
	public String recuperarConteoTbloqueo(
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
		Map<String,Object> procRes = ejecutaSP(new RecuperarConteoTbloqueo(getDataSource()),params);
		String             conteo  = (String)procRes.get("pv_conteo_o");
		if(StringUtils.isBlank(conteo))
		{
			conteo = "0";
		}
		return conteo;
	}
	
	protected class RecuperarConteoTbloqueo extends StoredProcedure
	{
		protected RecuperarConteoTbloqueo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_COUNT_TBLOQUEO");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_conteo_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public Map<String,String> consultaFeNacContratanteAuto(Map<String,String> params)throws Exception{
		Map<String,String> fechas = null;
		
		Map<String,Object>procResult  = ejecutaSP(new ConsultaFeNacContratanteAuto(getDataSource()), params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		
		if(lista != null && !lista.isEmpty()){
			fechas =  lista.get(0);
		}else{
			fechas =  new HashMap<String, String>();
			fechas.put("APLICA", "N");
			fechas.put("FECHAMIN", "");
			fechas.put("FECHAMAX", "");
		}
		
		return fechas;
	}
	
	protected class ConsultaFeNacContratanteAuto extends StoredProcedure
	{
		protected ConsultaFeNacContratanteAuto(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBT_RANGOS_FECNAC");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			 String[] cols = new String[]{
	            		"APLICA"  , "FECHAMIN"   , "FECHAMAX"
            };
	        
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarSubramos(String cdramo) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo" , cdramo);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarSubramos(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarSubramos extends StoredProcedure
	{
		protected RecuperarSubramos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBT_RAMOS_X_PROD");
			declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDSUBRAM"  , "DESCRIPCION"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarTparagen(ParametroGeneral paragen) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("nomparam" , paragen.getNomparam());
		Map<String,Object>       procRes = ejecutaSP(new RecuperarTparagen(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException(Utils.join("No existe el parametro general ",paragen.toString()));
		}
		if(lista.size()>1)
		{
			throw new ApplicationException(Utils.join("Parametro general ",paragen.toString()," repetido"));
		}
		String val = lista.get(0).get("VALPARAM");
		logger.debug(Utils.join("\n****** ...P_OBTIENE_TPARAGEN ",paragen.getNomparam()," = ",val," ******"));
		return val;
	}
	
	protected class RecuperarTparagen extends StoredProcedure
	{
		protected RecuperarTparagen(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBTIENE_TPARAGEN");
			declareParameter(new SqlParameter("nomparam" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"VALPARAM"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarTiposRamo() throws Exception
	{
		Map<String,Object>       procRes = ejecutaSP(new RecuperarTiposRamo(getDataSource()),new LinkedHashMap<String,String>());
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarTiposRamo extends StoredProcedure
	{
		protected RecuperarTiposRamo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBTENER_TIPOS_RAMO");
			String[] cols = new String[]{
					"CDTIPRAM"  , "DSTIPRAM"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarRamosPorTipoRamo(String cdtipram) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipram" , cdtipram);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarRamosPorTipoRamo(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarRamosPorTipoRamo extends StoredProcedure
	{
		protected RecuperarRamosPorTipoRamo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_OBT_RAMOS_X_CDTIPRAM");
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDRAMO"  , "DSRAMO"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarSucursalesPorTipoRamo(String cdtipram) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipram" , cdtipram);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarSucursalesPorTipoRamo(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarSucursalesPorTipoRamo extends StoredProcedure
	{
		protected RecuperarSucursalesPorTipoRamo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_SUCURSALES_X_CDTIPRAM");
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDUNIECO"  , "DSUNIECO"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarPolizasParaImprimir(
			String cdtipram
			,String cduniecos
			,String cdramo
			,String ramo
			,String nmpoliza
			,Date fecha
			,String cdusuariLike
			,String cdagente
			,String cdusuariSesion
			,String cduniecoSesion
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdtipram"       , cdtipram);
		params.put("cduniecos"      , cduniecos);
		params.put("cdramo"         , cdramo);
		params.put("ramo"           , ramo);
		params.put("nmpoliza"       , nmpoliza);
		params.put("fecha"          , fecha);
		params.put("cdusuari"       , cdusuariLike);
		params.put("cdagente"       , cdagente);
		params.put("cdusuariSesion" , cdusuariSesion);
		params.put("cduniecoSesion" , cduniecoSesion);
		Map<String,Object> procRes = ejecutaSP(new RecuperarPolizasParaImprimir(getDataSource()),params);
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("\n****** ...P_GET_POLIZAS_PARA_IMPRIMIR lista: ",lista.size()," ******"));
		return lista;
	}
	
	protected class RecuperarPolizasParaImprimir extends StoredProcedure
	{
		protected RecuperarPolizasParaImprimir(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_POLIZAS_PARA_IMPRIMIR");
			declareParameter(new SqlParameter("cdtipram"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cduniecos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"         , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ramo"           , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fecha"          , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdusuari"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariSesion" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cduniecoSesion" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"cdtipram"
					,"dstipram"
					,"cdunieco"
					,"dsunieco"
					,"cdramo"
					,"dsramo"
					,"estado"
					,"nmpoliza"
					,"nmsuplem"
					,"cdtipsup"
					,"dstipsup"
					,"nsuplogi"
					,"cdgestor"
					,"cddevcia"
					,"feinival"
					,"ntramite"
					,"dssuplog"
					,"ramo"
					,"cdusuari"
					,"cdagente"
					,"dsagente"
					,"nmpoliex"
					,"numincisos"
					,"tiempoimp"
					,"tipoflot"
					,"cduniext"
					,"dsuniext"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarUltimoNmsuplem(
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
		Map<String,Object> procRes  = ejecutaSP(new RecuperarUltimoNmsuplem(getDataSource()),params);
		String             nmsuplem = (String)procRes.get("pv_nmsuplem_o");
		if(StringUtils.isBlank(nmsuplem))
		{
			throw new ApplicationException("No se encuentra el suplemento reciente");
		}
		return nmsuplem;
	}
	
	protected class RecuperarUltimoNmsuplem extends StoredProcedure
	{
		protected RecuperarUltimoNmsuplem(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_ULTIMO_NMSUPLEM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmsuplem_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarSecuenciaLote() throws Exception
	{
		Map<String,Object> procRes = ejecutaSP(new RecuperarSecuenciaLote(getDataSource()),new HashMap<String,String>());
		String             lote    = (String)procRes.get("pv_seqlote_o");
		if(StringUtils.isBlank(lote))
		{
			throw new ApplicationException("Error al generar la secuencia de lote");
		}
		return lote;
	}
	
	protected class RecuperarSecuenciaLote extends StoredProcedure
	{
		protected RecuperarSecuenciaLote(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_SEQLOTE");
			declareParameter(new SqlOutParameter("pv_seqlote_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarImpresionesDisponiblesPorTipoRamo(
			String cdtipram
			,String tipolote
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipram" , cdtipram);
		params.put("tipolote" , tipolote);
		Map<String,Object> procRes = ejecutaSP(new RecuperarImpresionesDisponiblesPorTipoRamo(getDataSource()),params);
		String             impdis  = (String)procRes.get("pv_impdis_o");
		if(StringUtils.isBlank(impdis))
		{
			throw new ApplicationException("Error al recuperar impresiones disponibles");
		}
		return impdis;
	}
	
	protected class RecuperarImpresionesDisponiblesPorTipoRamo extends StoredProcedure
	{
		protected RecuperarImpresionesDisponiblesPorTipoRamo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_IMP_DISP_X_CDTIPRAM");
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipolote" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_impdis_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String>recuperarDetalleImpresionLote(String lote, String tramite) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_lote_i" , lote);
		params.put("pv_ntramite_i", tramite);
		Map<String,Object> procRes    = ejecutaSP(new RecuperarDetalleImpresionLote(getDataSource()),params);
		String             requeridas = (String)procRes.get("pv_permiso_o");
		String             ejecutadas = (String)procRes.get("pv_suma_o");
		if(StringUtils.isBlank(requeridas)||StringUtils.isBlank(ejecutadas))
		{
			throw new ApplicationException("No se recuper\u00F3 el detalle de impresi\u00F3n de lote");
		}
		Map<String,String> result = new HashMap<String,String>();
		result.put("requeridas" , requeridas);
		result.put("ejecutadas" , ejecutadas);
		logger2.debug("****** ...P_GET_DET_IMP_LOTE salida: {}",result);
		return result;
	}
	
	protected class RecuperarDetalleImpresionLote extends StoredProcedure
	{
		protected RecuperarDetalleImpresionLote(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DET_IMP_LOTE");
			declareParameter(new SqlParameter("pv_lote_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_permiso_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_suma_o"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarImpresorasPorPapelYSucursal(
			String cdusuari
			,String papel
			,String activo
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("papel"    , papel);
		params.put("activo"   , activo);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarImpresorasPorPapelYSucursal(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_IMPRESORAS lista=",lista));
		return lista;
	}
	
	protected class RecuperarImpresorasPorPapelYSucursal extends StoredProcedure
	{
		protected RecuperarImpresorasPorPapelYSucursal(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_IMPRESORAS");
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("papel"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("activo"   , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"cdusuari"
					,"ip"
					,"nmimpres"
					,"num_bandejas"
					,"nombre"
					,"descrip"
					,"swactivo"
					,"charola1"
					,"charola2"
					,"swimpdpx"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarComboUsuarios(String cadena) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cadena" , cadena);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarComboUsuarios(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarComboUsuarios extends StoredProcedure
	{
		protected RecuperarComboUsuarios(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_USUARIOS");
			declareParameter(new SqlParameter("cadena" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"cdusuari"
					,"nombre"
					,"cdunieco"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> recuperarConfigImpresionSucursales(String cdusuari, String cdunieco, String cdtipram) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("cdunieco" , cdunieco);
		params.put("cdtipram" , cdtipram);
		params.put("swaplica" , null);
		Map<String,Object>      procRes = ejecutaSP(new RecuperarConfigImpresionSucursales(getDataSource()),params);
		List<Map<String,String>> lista  = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_TCNFIMPINCEXCSUC lista=",lista));
		return lista;
	}
	
	protected class RecuperarConfigImpresionSucursales extends StoredProcedure
	{
		protected RecuperarConfigImpresionSucursales(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TCNFIMPINCEXCSUC");
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swaplica" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"COD_USUARIO"
					,"SUC_USUARIO"
					,"TIPO_RAMO"
					,"SUC_PERMITIDA"
					,"SWAPLICA"
					,"DESCRIP"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> recuperarConfigImpresionAgentes(String cdusuari, String cdunieco, String cdtipram) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("cdunieco" , cdunieco);
		params.put("cdtipram" , cdtipram);
		params.put("swaplica" , null);
		Map<String,Object>      procRes = ejecutaSP(new RecuperarConfigImpresionAgentes(getDataSource()),params);
		List<Map<String,String>> lista  = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_TCNFIMPINCEXCAGT lista=",lista));
		return lista;
	}
	
	protected class RecuperarConfigImpresionAgentes extends StoredProcedure
	{
		protected RecuperarConfigImpresionAgentes(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TCNFIMPINCEXCAGT");
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swaplica" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"COD_USUARIO"
					,"SUC_USUARIO"
					,"TIPO_RAMO"
					,"AGENTE"
					,"SWAPLICA"
					,"DESCRIP"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movPermisoImpresionSucursal(
			String cdusuari
			,String cdunieco
			,String cdtipram
			,String cduniecoPer
			,String swaplica
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari"    , cdusuari);
		params.put("cdunieco"    , cdunieco);
		params.put("cdtipram"    , cdtipram);
		params.put("cduniecoPer" , cduniecoPer);
		params.put("swaplica"    , swaplica);
		params.put("accion"      , accion);
		ejecutaSP(new MovPermisoImpresionSucursal(getDataSource()),params);
	}
	
	protected class MovPermisoImpresionSucursal extends StoredProcedure
	{
		protected MovPermisoImpresionSucursal(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_TCNFIMPINCEXCSUC");
			declareParameter(new SqlParameter("cdusuari"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cduniecoPer" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swaplica"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movPermisoImpresionAgente(
			String cdusuari
			,String cdunieco
			,String cdtipram
			,String cdagentePer
			,String swaplica
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari"    , cdusuari);
		params.put("cdunieco"    , cdunieco);
		params.put("cdtipram"    , cdtipram);
		params.put("cdagentePer" , cdagentePer);
		params.put("swaplica"    , swaplica);
		params.put("accion"      , accion);
		ejecutaSP(new MovPermisoImpresionAgente(getDataSource()),params);
	}
	
	protected class MovPermisoImpresionAgente extends StoredProcedure
	{
		protected MovPermisoImpresionAgente(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_TCNFIMPINCEXCAGT");
			declareParameter(new SqlParameter("cdusuari"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagentePer" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swaplica"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarRecibosLote(
			String cdtipram
			,String cduniecos
			,Date feinicio
			,Date fefin
			,String cdusuari
			,String cdunieco
			,String tiporeciboimp
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdtipram"  , cdtipram);
		params.put("cduniecos" , cduniecos);
		params.put("feinicio"  , feinicio);
		params.put("fefin"     , fefin);
		params.put("cdusuari"  , cdusuari);
		params.put("cdunieco"  , cdunieco);
		params.put("tiporeciboimp", tiporeciboimp);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarRecibosLote(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_RECIBOS_PARA_HABILITAR lista=",lista));
		return lista;
	}
	
	protected class RecuperarRecibosLote extends StoredProcedure
	{
		protected RecuperarRecibosLote(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_RECIBOS_PARA_HABILITAR");
			declareParameter(new SqlParameter("cdtipram"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cduniecos" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feinicio"  , OracleTypes.DATE));
			declareParameter(new SqlParameter("fefin"     , OracleTypes.DATE));
			declareParameter(new SqlParameter("cdusuari"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tiporeciboimp", OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"cdunieco"
					,"dsunieco"
					,"cdramo"
					,"dsramo"
					,"estado"
					,"nmpoliza"
					,"nmsuplem"
					,"nmrecibo"
					,"cdgestor"
					,"cddevcia"
					,"primatot"
					,"feemisio"
					,"feinicio"
					,"fefinal"
					,"nmimpres"
					,"cdagente"
					,"cdtipram"
					,"ramo"
					,"dsagente"
					,"nmpoliex"
					,"cduniext"
					,"dsuniext"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarDetalleRemesa(String ntramite, String tipolote) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("tipolote" , tipolote);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarDetalleRemesa(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_DETALLE_REMESAS lista=",lista));
		return lista;
	}
	
	protected class RecuperarDetalleRemesa extends StoredProcedure
	{
		protected RecuperarDetalleRemesa(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DETALLE_REMESAS");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipolote" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"ntramite"
					,"cdunieco"
					,"cdramo"
					,"estado"
					,"nmpoliza"
					,"nmsuplem"
					,"tipotram"
					,"nmtraope"
					,"nmrecibo"
					,"cddevcia"
					,"cdgestor"
					,"nmimpres"
					,"ptimport"
					,"cdagente"
					,"nombagte"
					,"nsuplogi"
					,"descrip"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarArchivosParaImprimirLote(
			String lote
			,String papel
			,String tipolote
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("lote"     , lote);
		params.put("papel"    , papel);
		params.put("tipolote" , tipolote);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarArchivosParaImprimirLote(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_DOCUMENTOS_X_LOTE lista=",lista));
		return lista;
	}
	
	protected class RecuperarArchivosParaImprimirLote extends StoredProcedure
	{
		protected RecuperarArchivosParaImprimirLote(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DOCUMENTOS_X_LOTE");
			declareParameter(new SqlParameter("lote"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("papel"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipolote" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"cdagente"
					,"ordenimp"
					,"cdsubram"
					,"tipend"
					,"nmpoliza"
					,"cddocume"
					,"dsdocume"
					,"nmcopias"
					,"nmordina"
					,"ntramite"
					,"tipodoc"
					,"remesa"
					,"numend"
					,"swimpdpx"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarDatosPolizaParaDocumentos(
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
		Map<String,Object> procRes  = ejecutaSP(new RecuperarDatosPolizaParaDocumentos(getDataSource()),params);
		Map<String,String> result   = new HashMap<String,String>();
		String             ntramite = (String)procRes.get("pv_ntramite_o");
		String             nmsolici = (String)procRes.get("pv_nmsolici_o");
		if(StringUtils.isBlank(ntramite))
		{
			throw new ApplicationException("No hay tramite de emision");
		}
		if(StringUtils.isBlank(ntramite))
		{
			throw new ApplicationException("No hay solicitud");
		}
		result.put("ntramite" , ntramite);
		result.put("nmsolici" , nmsolici);
		logger.debug(Utils.log("****** ...P_RECUPERA_DATOS_EMI result=",result));
		return result;
	}
	
	protected class RecuperarDatosPolizaParaDocumentos extends StoredProcedure
	{
		protected RecuperarDatosPolizaParaDocumentos(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_RECUPERA_DATOS_EMI");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_nmsolici_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarTipoRamoPorCdramo(String cdramo) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo" , cdramo);
		Map<String,Object> procRes  = ejecutaSP(new RecuperarTipoRamoPorCdramo(getDataSource()),params);
		String             cdtipram = (String)procRes.get("pv_cdtipram_o");
		if(StringUtils.isBlank(cdtipram))
		{
			throw new ApplicationException(Utils.join("Error al recuperar el tipo de ramo para el ramo ",cdramo));
		}
		logger.debug(Utils.log("\n****** ...P_GET_CDTIPRAM_X_CDRAMO cdtipram=",cdtipram," ******"));
		return cdtipram;
	}
	
	protected class RecuperarTipoRamoPorCdramo extends StoredProcedure
	{
		protected RecuperarTipoRamoPorCdramo(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CDTIPRAM_X_CDRAMO");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipram_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarTramitePorNmsuplem(
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
		Map<String,Object> procRes  = ejecutaSP(new RecuperarTramitePorNmsuplem(getDataSource()),params);
		String             ntramite = (String)procRes.get("pv_ntramite_o");
		if(StringUtils.isBlank(ntramite))
		{
			throw new ApplicationException("No se puedo recuperar el tr\u00e1mite");
		}
		logger.debug(Utils.log("\n****** ...P_GET_TRAMITE_X_NMSUPLEM ntramite=",ntramite," ******"));
		return ntramite;
	}
	
	protected class RecuperarTramitePorNmsuplem extends StoredProcedure
	{
		protected RecuperarTramitePorNmsuplem(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TRAMITE_X_NMSUPLEM");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_ntramite_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarRemesaEmisionEndoso(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String ntramite
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("ntramite" , ntramite);
		Map<String,Object> procRes  = ejecutaSP(new VerificarRemesaEmisionEndosoAnterior(getDataSource()),params);
		String             lote     = (String)procRes.get("pv_lote_o");
		String             remesa   = (String)procRes.get("pv_remesa_o");
		String             cdtipimp = (String)procRes.get("pv_cdtipimp_o");
		Map<String,String> datos    = null;
		if(StringUtils.isNotBlank(lote)&&StringUtils.isNotBlank(remesa))
		{
			datos = new HashMap<String,String>();
			datos.put("lote"     , lote);
			datos.put("remesa"   , remesa);
			datos.put("cdtipimp" , cdtipimp);
		}
		logger.debug(Utils.log("\n****** ...P_GET_DATOS_REMESA_UNICA datos=",datos));
		return datos;
	}
	
	protected class VerificarRemesaEmisionEndosoAnterior extends StoredProcedure
	{
		protected VerificarRemesaEmisionEndosoAnterior(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_REMESA_UNICA");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_lote_o"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_remesa_o"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipimp_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarDstipsupPorCdtipsup(String cdtipsup) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipsup" , cdtipsup);
		Map<String,Object> procRes  = ejecutaSP(new RecuperarDstipsupPorCdtipsup(getDataSource()),params);
		String             dstipsup = (String)procRes.get("pv_dstipsup_o");
		if(StringUtils.isBlank(dstipsup))
		{
			throw new ApplicationException(Utils.join("No hay nombre de suplemento para clave ",cdtipsup));
		}
		return dstipsup;
	}
	
	protected class RecuperarDstipsupPorCdtipsup extends StoredProcedure
	{
		protected RecuperarDstipsupPorCdtipsup(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DSTIPSUP_X_CDTIPSUP");
			declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_dstipsup_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarSucursalesPermisoImpresion(
			String cdtipram
			,String cdusuari
			,String cdunieco
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipram" , cdtipram);
		params.put("cdusuari" , cdusuari);
		params.put("cdunieco" , cdunieco);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarSucursalesPermisoImpresion(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarSucursalesPermisoImpresion extends StoredProcedure
	{
		protected RecuperarSucursalesPermisoImpresion(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_SUCURSALES_PARA_IMPRIMIR");
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			String[] cols = new String[]{ "cdunieco" , "dsunieco" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> recuperarConfigImpresionUsuarios(String cdusuari, String cdunieco, String cdtipram) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("cdunieco" , cdunieco);
		params.put("cdtipram" , cdtipram);
		params.put("swaplica" , null);
		Map<String,Object>      procRes = ejecutaSP(new RecuperarConfigImpresionUsuarios(getDataSource()),params);
		List<Map<String,String>> lista  = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(Utils.log("****** ...P_GET_TCNFIMPINCEXCAGT lista=",lista));
		return lista;
	}
	
	protected class RecuperarConfigImpresionUsuarios extends StoredProcedure
	{
		protected RecuperarConfigImpresionUsuarios(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TCNFIMPINCEXCUSR");
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swaplica" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"COD_USUARIO"
					,"SUC_USUARIO"
					,"TIPO_RAMO"
					,"CDUSUARI_PERMISO"
					,"SWAPLICA"
					,"DESCRIP"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movPermisoImpresionUsuario(
			String cdusuari
			,String cdunieco
			,String cdtipram
			,String cdusuariPer
			,String swaplica
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari"    , cdusuari);
		params.put("cdunieco"    , cdunieco);
		params.put("cdtipram"    , cdtipram);
		params.put("cdusuariPer" , cdusuariPer);
		params.put("swaplica"    , swaplica);
		params.put("accion"      , accion);
		ejecutaSP(new MovPermisoImpresionUsuario(getDataSource()),params);
	}
	
	protected class MovPermisoImpresionUsuario extends StoredProcedure
	{
		protected MovPermisoImpresionUsuario(DataSource dataSource)
		{
			super(dataSource,"PKG_SATELITES2.P_MOV_TCNFIMPINCEXCUSR");
			declareParameter(new SqlParameter("cdusuari"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariPer" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swaplica"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarRolesTodos() throws Exception
	{
		Map<String,Object>       procRes = ejecutaSP(new RecuperarRolesTodosSP(getDataSource()),new HashMap<String,String>());
		List<Map<String,String>> roles   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(roles==null)
		{
			roles = new ArrayList<Map<String,String>>();
		}
		return roles;
	}
	
	protected class RecuperarRolesTodosSP extends StoredProcedure
	{
		protected RecuperarRolesTodosSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_TODOS_ROLES");
			String[] cols = new String[]{
					"CDSISROL"
					,"DSSISROL"
            };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> obtieneBeneficiariosPoliza(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
	) throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		
		Map<String,Object>       procRes = ejecutaSP(new ObtieneBeneficiariosPoliza(getDataSource()),params);
		List<Map<String,String>> roles   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(roles==null)
		{
			roles = new ArrayList<Map<String,String>>();
		}
		return roles;
	}
	
	protected class ObtieneBeneficiariosPoliza extends StoredProcedure
	{
		protected ObtieneBeneficiariosPoliza(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_WS_BENEF_AUTOS_COB_VIDA");
			
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NOMBRE"
					,"APEPAT"
					,"APEMAT"
					,"IDPARENTESCO"
					,"NUMCER"
					,"PORCENTAJE"
					,"TEXTO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarDatosFlujoEmision(String cdramo, String tipoflot) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("tipoflot" , tipoflot);
		Map<String,Object> procRes = ejecutaSP(new RecuperarDatosFlujoEmisionSP(getDataSource()),params);
		Map<String,String> result  = new HashMap<String,String>();
		result.put("cdtipflu"  , (String)procRes.get("pv_cdtipflu_o"));
		result.put("cdflujomc" , (String)procRes.get("pv_cdflujomc_o"));
		return result;
	}
	
	protected class RecuperarDatosFlujoEmisionSP extends StoredProcedure
	{
		protected RecuperarDatosFlujoEmisionSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_DATOS_FLUJO_EMI");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("tipoflot" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipflu_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdflujomc_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarDiasFechaFacturacion(String cdtipsit, String cdsisrol) throws Exception
	{logger.debug(Utils.log("VILS >>> ",cdtipsit,"/",cdsisrol));
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdtipsit_i" , cdtipsit);
		params.put("pv_cdsisrol_i" , cdsisrol);
		Map<String,Object> procRes  = ejecutaSP(new RecuperarDias(getDataSource()),params);
		String dias = (String)procRes.get("pv_rangofec_o");
		logger.debug(Utils.log("Dias permitidos para valor Factura: ",dias));
		if(StringUtils.isBlank(dias))
		{
			throw new ApplicationException(Utils.join("Error al consultar los dias para la fecha de facturacion ",cdtipsit,"/",cdsisrol));
		}
		return dias;
	}
	
	
	protected class RecuperarDias extends StoredProcedure
	{ 
		protected RecuperarDias(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA. P_GET_RANGO_FECHA_FACTURA");
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_rangofec_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
			
		}
	}
	
	@Override
	public void guardarDatosDemo(String ntramite, Map<String,String> params) throws Exception
	{
		Map<String,String> paramsDAO = new LinkedHashMap<String,String>();
		paramsDAO.put("ntramite" , ntramite);
		for(int i=1 ; i<=30 ; i++)
		{
			String indice = Utils.join("OTVALOR", StringUtils.leftPad(i+"", 2, "0"));
			paramsDAO.put(indice, params.get(indice));
		}
		ejecutaSP(new GuardarDatosDemoSP(getDataSource()),paramsDAO);
	}
	
	protected class GuardarDatosDemoSP extends StoredProcedure
	{ 
		protected GuardarDatosDemoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GUARDA_DATOS_DEMO");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			for(int i=1 ; i<=30 ; i++)
			{
				String indice = Utils.join("OTVALOR", StringUtils.leftPad(i+"", 2, "0"));
				declareParameter(new SqlParameter(indice, OracleTypes.VARCHAR));
			}
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
			
		}
	}
	
	@Override
	public Map<String,String> cargarDatosDemo(String ntramite) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		Map<String,Object> procRes = ejecutaSP(new CargarDatosDemoSP(getDataSource()),params);
		
		List<Map<String,String>> lista  = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		Map<String,String> datos = new HashMap<String,String>();
		
		if(lista!=null&&lista.size()==1)
		{
			datos = lista.get(0);
		}
		
		logger.debug(Utils.log("datos de demo=",datos));
		
		return datos;
	}
	
	protected class CargarDatosDemoSP extends StoredProcedure
	{
		protected CargarDatosDemoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_CARGA_DATOS_DEMO");
			
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"ntramite"
					,"OTVALOR01" ,"OTVALOR02" ,"OTVALOR03" ,"OTVALOR04" ,"OTVALOR05" ,"OTVALOR06" ,"OTVALOR07" ,"OTVALOR08" ,"OTVALOR09" ,"OTVALOR10"
					,"OTVALOR11" ,"OTVALOR12" ,"OTVALOR13" ,"OTVALOR14" ,"OTVALOR15" ,"OTVALOR16" ,"OTVALOR17" ,"OTVALOR18" ,"OTVALOR19" ,"OTVALOR20"
					,"OTVALOR21" ,"OTVALOR22" ,"OTVALOR23" ,"OTVALOR24" ,"OTVALOR25" ,"OTVALOR26" ,"OTVALOR27" ,"OTVALOR28" ,"OTVALOR29" ,"OTVALOR30"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarPermisoBotonEnviarCenso(String cdsisrol) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdsisrol" , cdsisrol);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarPermisoBotonEnviarCensoSP(getDataSource()),params);
		
		String permiso = (String)procRes.get("pv_permiso_o");
		if(StringUtils.isBlank(permiso))
		{
			permiso = "N";
		}
		
		return permiso;
	}
	
	protected class RecuperarPermisoBotonEnviarCensoSP extends StoredProcedure
	{
		protected RecuperarPermisoBotonEnviarCensoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PERMISO_BOTON_CENSO");
			
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_permiso_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public int recuperarConteoTbloqueoTramite(String ntramite)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarConteoTbloqueoTramiteSP(getDataSource()),params);
		List<Map<String,String>> list    = (List<Map<String,String>>)procRes.get("pv_registro_o");
		int bloqueos = 0;
		if(list!=null&&list.size()>0)
		{
			try
			{
				bloqueos = Integer.parseInt(list.get(0).get("REGS_TBLOQUEO"));
			}
			catch(Exception ex)
			{
				logger.error("Error al intentar parsear el num de bloqueos por tramite, se deja en 0",ex);
				bloqueos = 0;
			}
		}
		return bloqueos;
	}
	
	protected class RecuperarConteoTbloqueoTramiteSP extends StoredProcedure
	{
		protected RecuperarConteoTbloqueoTramiteSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_BLOQUEO_TRAMITE");
			
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols = new String[]{ "REGS_TBLOQUEO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarExclusionTurnados()throws Exception
	{
		Map<String,Object>       procRes = ejecutaSP(new RecuperarExclusionTurnadosSP(getDataSource()),new HashMap<String,String>());
		List<Map<String,String>> list    = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		return list;
	}
	
	protected class RecuperarExclusionTurnadosSP extends StoredProcedure
	{
		protected RecuperarExclusionTurnadosSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_EXCLUSIONES_TURNADO");
			
			String[] cols = new String[]{ "cdusuari", "dsusuari" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> cargarCotizadoresActivos(String cadena)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cadena" , cadena);
		Map<String,Object> procRes = ejecutaSP(new cargarCotizadoresActivosSP(getDataSource()),params);
		List<Map<String,String>> list    = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		return list;
	}
	
	protected class cargarCotizadoresActivosSP extends StoredProcedure
	{
		protected cargarCotizadoresActivosSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_COTIZADORES_ACTIVOS");
			declareParameter(new SqlParameter("cadena" , OracleTypes.VARCHAR));
			String[] cols = new String[]{ "cdusuari", "dsusuari" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> obtieneMotivosReexp(String cdramo, String cdtipsit) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdramo_i"   , cdramo);
		params.put("pv_cdtipsit_i" , cdtipsit);
		Map<String,Object> procRes = ejecutaSP(new ObtieneMotivosReexp(getDataSource()),params);
		List<Map<String,String>> list    = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		return list;
	}
	
	protected class ObtieneMotivosReexp extends StoredProcedure
	{
		protected ObtieneMotivosReexp(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_MOTIVOS_CANC_REEXP");
			declareParameter(new SqlParameter("pv_cdramo_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{ "codigo", "motivo" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarPermisoBotonEmitir(String cdsisrol, String cdusuari, String cdtipsit) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdsisrol" , cdsisrol);
		params.put("cdusuari" , cdusuari);
		params.put("cdtipsit" , cdtipsit);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarPermisoBotonEmitirSP(getDataSource()),params);
		
		String permiso = (String)procRes.get("pv_swemitir_o");
		if(StringUtils.isBlank(permiso))
		{
			permiso = "N";
		}
		
		return permiso;
	}
	
	protected class RecuperarPermisoBotonEmitirSP extends StoredProcedure
	{
		protected RecuperarPermisoBotonEmitirSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_PERMISO_BOTON_EMITIR");
			
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_swemitir_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public  List<Map<String,String>>  recuperarClavesPlanRamo4(String cdramo, String cdtipsit, String cdplan) throws Exception
	{
		
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo  );
		params.put("cdtipsit" , cdtipsit);
		params.put("cdplan" , cdplan);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarClavesPlanRamo4SP(getDataSource()),params);
		List<Map<String,String>> list  = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		return list;
	}
	
	protected class RecuperarClavesPlanRamo4SP extends StoredProcedure
	{
		protected RecuperarClavesPlanRamo4SP(DataSource dataSource)
		{
			super(dataSource,"PKG_TRAD.P_GET_PLANES_TRAD");
			declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdplan" , OracleTypes.VARCHAR));
			String[] cols = new String[]{ "otclave", "otvalor", "otvalor14"}; 	
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarCodigoCustom(String cdpantalla, String cdsisrol) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdpantalla" , cdpantalla);
		params.put("cdsisrol"   , cdsisrol);
		Map<String,Object> procRes = ejecutaSP(new RecuperarCodigoCustomSP(getDataSource()),params);
		String codigo = (String)procRes.get("codigo");
		if(StringUtils.isBlank(codigo))
		{
			codigo = Utils.join("/* sin codigo custom para pantalla ",cdpantalla," y rol ",cdsisrol," */");
		}
		codigo = Utils.join(
				 "\n/*********************************************************/"
				,"\n/****** INICIO DE CODIGO CUSTOM DESDE BASE DE DATOS ******/\n"
				,codigo
				,"\n/****** FIN DE CODIGO CUSTOM DESDE BASE DE DATOS ******/"
				,"\n/******************************************************/"
				);
		logger.debug(Utils.log("\n****** codigo=", codigo));
		return codigo;
	}
	
	protected class RecuperarCodigoCustomSP extends StoredProcedure
	{
		protected RecuperarCodigoCustomSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CODIGO_CUSTOM_PANTALLA");
			declareParameter(new SqlParameter("cdpantalla" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("codigo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<Map<String,String>> recuperarListaTatrisitSinPadre(String tipsit, String atribu) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdatribu" , atribu );
		params.put("cdtipsit" , tipsit);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarListaTatrisitSinPadreSP(getDataSource()),params);
		List<Map<String,String>> list  = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		return list;
	}
	
	protected class RecuperarListaTatrisitSinPadreSP extends StoredProcedure
	{
		protected RecuperarListaTatrisitSinPadreSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_LISTA_TATRISIT_SIN_PADRE");
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdatribu" , OracleTypes.VARCHAR));
			String[] cols = new String[]{ "otclave", "otvalor"}; 	
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarFormasDePagoPorRamoTipsit(String cdramo, String cdtipsit) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo  );
		params.put("cdtipsit" , cdtipsit);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarFormasDePagoPorRamoTipsitSP(getDataSource()),params);
		
		List<Map<String,String>> list = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		logger.debug(Utils.log("...P_GET_FORMA_PAGO list=",list));
		
		return list;
	}
	
	protected class RecuperarFormasDePagoPorRamoTipsitSP extends StoredProcedure
	{
		protected RecuperarFormasDePagoPorRamoTipsitSP(DataSource dataSource)
		{
			super(dataSource,"PKG_LISTAS.P_GET_FORMA_PAGO");
			declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"NMTABLA", "CDRAMO", "CDTIPSIT", "CODIGO", "DESCRIPC", "DESCRIPL"
			}; 	
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public List<Map<String,String>> recuperarClientesPorNombreApellido(String cadena) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cadena", cadena);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarClientesPorNombreApellidoSP(getDataSource()),params);
		
		List<Map<String,String>> list = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		logger.debug(Utils.log("...P_GET_CLIENTES_X_NOMBRE_APE list=",list));
		
		return list;
	}
	
	protected class RecuperarClientesPorNombreApellidoSP extends StoredProcedure
	{
		protected RecuperarClientesPorNombreApellidoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_CLIENTES_X_NOMBRE_APE");
			
			declareParameter(new SqlParameter("cadena" , OracleTypes.VARCHAR));
			
			String[] cols = new String[]{ "CDPERSON" , "NOMBRE" };
			
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarConveniosPorPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza, String cdcontra) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i" , cdramo);
		params.put("pv_estado_i" , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdtipsit_i" , cdtipsit);
		params.put("pv_cdperson_i" , cdcontra);
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarConveniosPorPoliza(getDataSource()),params);
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ regresa ",listaMapas.size()
				,"\n@@@@@@"
				));
		return listaMapas;
	}
	
	protected class RecuperarConveniosPorPoliza extends StoredProcedure
	{
		protected RecuperarConveniosPorPoliza(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_DATOS_CONVENIOS");
			declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDUNIECO","DSRAMO","CDRAMO","CDTIPSIT","NMPOLIZA","CONTRATANTE","AGENTE","DIASGRAC","LEYENDA","CODIGO","STATUS"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void insertarConvenioPoliza(String cdunieco, String cdramo, String estado, String cdtipsit, String nmpoliza, String diasgrac, String cdconven, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_diasgrac_i", diasgrac);
		params.put("pv_cdconven_i", cdconven);
		params.put("pv_status_i", status);
		params.put("pv_fecregis_i", fecregis);
		params.put("pv_cdusureg_i", cdusureg);
		params.put("pv_fecmodif_i", fecmodif);
		params.put("pv_cdusumod_i", cdusumod);
		params.put("pv_cdtipsit_i", cdtipsit);
		params.put("pv_accion_i", operacion);		
		Map<String,Object>  procRes    = ejecutaSP(new InsertarConvenioPoliza(getDataSource()),params);
	}
	
	protected class InsertarConvenioPoliza extends StoredProcedure
	{
		protected InsertarConvenioPoliza(DataSource dataSource)
		{
			super(dataSource,"pkg_satelites2.P_MOV_TCONVENIOS");			
			declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));    
			declareParameter(new SqlParameter("pv_diasgrac_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdconven_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecregis_i"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("pv_cdusureg_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecmodif_i"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("pv_cdusumod_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	public List<Map<String,String>> recuperarCancelacionesConveniosPorPoliza(String cdunieco, String cdramo, String cdtipsit, String estado, String nmpoliza) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i" , cdunieco);
		params.put("pv_cdramo_i" , cdramo);
		params.put("pv_estado_i" , estado);
		params.put("pv_nmpoliza_i" , nmpoliza);
		params.put("pv_cdtipsit_i" , cdtipsit);
		Map<String,Object>       procRes    = ejecutaSP(new RecuperarCancelacionesConveniosPorPoliza(getDataSource()),params);
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ regresa ",procRes.size()
				,"\n@@@@@@"
				));
		List<Map<String,String>> listaMapas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ regresa ",listaMapas.size()
				,"\n@@@@@@"
				));
		return listaMapas;
	}
	
	protected class RecuperarCancelacionesConveniosPorPoliza extends StoredProcedure
	{
		protected RecuperarCancelacionesConveniosPorPoliza(DataSource dataSource)
		{
			super(dataSource,"Pkg_Consulta.P_GET_DATOS_TPOLPROTEG");
			declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDUNIECO","DSRAMO","CDRAMO","CDTIPSIT","NMPOLIZA","CONTRATANTE","AGENTE","STATUS"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public void insertarCancelacionesConvenioPoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String status, Date fecregis, String cdusureg, Date fecmodif, String cdusumod, String operacion) throws Exception
	{
		
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i", cdramo);
		params.put("pv_estado_i", estado);
		params.put("pv_nmpoliza_i", nmpoliza);
		params.put("pv_status_i", status);
		params.put("pv_fecregis_i", fecregis);
		params.put("pv_cdusureg_i", cdusureg);
		params.put("pv_fecmodif_i", fecmodif);
		params.put("pv_cdusumod_i", cdusumod);		
		params.put("pv_accion_i", operacion);		
		Map<String,Object>  procRes    = ejecutaSP(new InsertarCancelacionesConvenioPoliza(getDataSource()),params);
	}
	
	protected class InsertarCancelacionesConvenioPoliza extends StoredProcedure
	{
		protected InsertarCancelacionesConvenioPoliza(DataSource dataSource)
		{
			super(dataSource,"pkg_satelites2.P_MOV_TPOLPROTEG");			
			declareParameter(new SqlParameter("pv_cdunieco_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i"   , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_estado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmpoliza_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_status_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecregis_i"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("pv_cdusureg_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_fecmodif_i"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("pv_cdusumod_i"   , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("pv_accion_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperaCoberturasExtraprima(String cdramo, String cdtipsit) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("cdtipsit" , cdtipsit);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperaCoberturasExtraprimaSP(getDataSource()),params);
		
		List<Map<String,String>> coberturasExtraprimas = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		logger.debug(Utils.log("\n****** lista coberturas extraprima=",coberturasExtraprimas));
		
		return coberturasExtraprimas;
	}
	
	protected class RecuperaCoberturasExtraprimaSP extends StoredProcedure
	{
		protected RecuperaCoberturasExtraprimaSP(DataSource dataSource)
		{
			super(dataSource,"PKG_CONSULTA.P_GET_COBERTURAS_EXTRAPRIMA");
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(new String[]{"CDGARANT"})));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	@Override
    public void modificaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol, String swmodifi, String accion) throws Exception{
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PV_CDRAMO_I", cdramo);
        params.put("PV_CDTIPSIT_I",  cdtipsit);
        params.put("PV_CDPLAN_I",   cdplan);
        params.put("PV_CDGARANT_I", cdgarant);
        params.put("PV_CDSISROL_I",cdsisrol);
        params.put("PV_SWMODIFI_I",swmodifi);
        params.put("PV_ACCION_I",accion);
        Map<String, Object> result = ejecutaSP(new ModificaPermisosEdicionCoberturasSP(getDataSource()), params);
    }
    
    protected class ModificaPermisosEdicionCoberturasSP extends StoredProcedure {
        protected ModificaPermisosEdicionCoberturasSP(DataSource dataSource) {
            super(dataSource, "PKG_DESARROLLO.P_MOV_TRELROLCOB");
            declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDPLAN_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDSISROL_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_SWMODIFI_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_ACCION_I",OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public List<Map<String,String>> consultaPermisosEdicionCoberturas(int cdramo, String cdtipsit, String cdplan, String cdgarant, String cdsisrol) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("PV_CDRAMO_I", cdramo);
        params.put("PV_CDTIPSIT_I",  cdtipsit);
        params.put("PV_CDPLAN_I",   cdplan);
        params.put("PV_CDGARANT_I", cdgarant);
        params.put("PV_CDSISROL_I",cdsisrol);
        Map<String, Object> mapResult = ejecutaSP(new ConsultaPermisosEdicionCoberturasSP(getDataSource()), params);
        logger.debug("res = "+ mapResult.get("pv_registro_o"));
        return (List<Map<String,String>>) mapResult.get("pv_registro_o");
    }

	protected class ConsultaPermisosEdicionCoberturasSP extends StoredProcedure {
    	protected ConsultaPermisosEdicionCoberturasSP(DataSource dataSource) {
    		super(dataSource, "PKG_DESARROLLO.P_GET_TRELROLCOB");
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDPLAN_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDSISROL_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new PermisosEdicionCoberturasMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    protected class PermisosEdicionCoberturasMapper  implements RowMapper<Map<String,String>> {
    	public Map<String,String> mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Map<String,String> consulta = new HashMap<String,String>();
    		consulta.put("NMTABLA", rs.getString("NMTABLA"));
    		consulta.put("PV_CDRAMO_I", rs.getString("OTCLAVE1"));
    		consulta.put("PV_CDTIPSIT_I", rs.getString("OTCLAVE2"));
    		consulta.put("PV_CDPLAN_I",   rs.getString("OTCLAVE3"));
    		consulta.put("PV_CDGARANT_I", rs.getString("OTCLAVE4"));
    		consulta.put("PV_CDSISROL_I",rs.getString("OTCLAVE5"));
    		consulta.put("FEDESDE", rs.getString("FEDESDE"));
    		consulta.put("FEHASTA", rs.getString("FEHASTA"));
    		consulta.put("MODIFICABLE", rs.getString("OTVALOR01"));
    		return consulta;
    	}
    }
    
    @Override
    public String recuperarCdpersonClienteTramite(String ntramite) throws Exception
    {
    	Map<String,String> params = new LinkedHashMap<String,String>();
    	params.put("ntramite", ntramite);
    	
    	Map<String,Object> procRes = ejecutaSP(new RecuperarCdpersonClienteTramiteSP(getDataSource()),params);
    	
    	String cdperson = (String)procRes.get("pv_cdperson_o");
    	
    	if(StringUtils.isBlank(cdperson))
    	{
    		cdperson = "";
    	}
    	
    	return cdperson;
    }
	
	protected class RecuperarCdpersonClienteTramiteSP extends StoredProcedure
	{
		protected RecuperarCdpersonClienteTramiteSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_TTRAMPER");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdperson_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}

	public String recuperarEsSaludDaniosTramite(String ntramite) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		Map<String,Object> procRes = ejecutaSP(new RecuperarEsSaludDaniosTramiteSP(getDataSource()),params);
		String esDaniosSalud = (String) procRes.get("pv_esdaniossalud_o");
		if(StringUtils.isBlank(esDaniosSalud))
		{
			esDaniosSalud = "";
		}
		return esDaniosSalud;
	}
	
	protected class RecuperarEsSaludDaniosTramiteSP extends StoredProcedure
	{
		protected RecuperarEsSaludDaniosTramiteSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_ES_SALUD_DANIOS_TRAMITE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_esdaniossalud_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"        , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"         , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public List<Map<String,String>> llenaCombo(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("PV_CDUNIECO_I",  cdunieco);
        params.put("PV_CDRAMO_I", cdramo);
        params.put("PV_CDPLAN_I",   estado);
        params.put("PV_NMPOLIZA_I", nmpoliza);
        Map<String, Object> mapResult = ejecutaSP(new ConsultaListaComboSP(getDataSource()), params);
        logger.debug("res = "+ mapResult.get("pv_registro_o"));
        return (List<Map<String,String>>) mapResult.get("pv_registro_o");
    }

	protected class ConsultaListaComboSP extends StoredProcedure {
    	protected ConsultaListaComboSP(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA_PRUEBA.P_Lista_Att_Inc");
            declareParameter(new SqlParameter("PV_CDUNIECO_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_ESTADO_I", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_NMPOLIZA_I", OracleTypes.VARCHAR));
            
            //SALIDA:
            String[] cols = new String[]{"PV_CDUNIECO_I","PV_CDRAMO_I","PV_ESTADO_I","PV_NMPOLIZA_I"};//{"dsatribu"};
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public String recuperarCduniextPorLlavePoliza(String cdunieco,String cdramo,String estado,String nmpoliza) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		Map<String,Object> procRes = ejecutaSP(new RecuperarCduniextPorLlavePolizaSP(getDataSource()),params);
		String cduniext = (String)procRes.get("pv_cduniext_o");
		if(cduniext == null)
		{
			cduniext = "";
		}
		logger2.debug("cduniext recuperado = {}",cduniext);
		return cduniext;
	}

	protected class RecuperarCduniextPorLlavePolizaSP extends StoredProcedure {
    	protected RecuperarCduniextPorLlavePolizaSP(DataSource dataSource) {
    		super(dataSource, "PKG_CONSULTA.P_GET_CDUNIEXT");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cduniext_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public Map<String,String> recuperarDatosFlujoEndoso(String cdramo, String cdtipsup) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("cdramo"   , cdramo);
		params.put("cdtipsup" , cdtipsup);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarDatosFlujoEndosoSP(getDataSource()),params);
		
		Map<String,String> result = new HashMap<String,String>();
		result.put("cdtipflu"  , (String)procRes.get("pv_cdtipflu_o"));
		result.put("cdflujomc" , (String)procRes.get("pv_cdflujomc_o"));
		
		logger.debug(Utils.log("P_GET_DATOS_FLUJO_END params = ",params," result = ",result));
		
		return result;
	}

	protected class RecuperarDatosFlujoEndosoSP extends StoredProcedure {
    	protected RecuperarDatosFlujoEndosoSP(DataSource dataSource) {
    		super(dataSource, "P_GET_DATOS_FLUJO_END");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdtipflu_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdflujomc_o" , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public Map<String, String> recuperarTtipsupl (String cdtipsup) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdtipsup", cdtipsup);
		Map<String, Object> procRes = ejecutaSP(new RecuperarTtipsuplSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null || lista.size() == 0) {
			throw new ApplicationException("No se encuentra el tipo de endoso");
		} else if (lista.size() > 1) {
			throw new ApplicationException("Tipo de endoso repetido");
		}
		return lista.get(0);
	}

	protected class RecuperarTtipsuplSP extends StoredProcedure {
    	protected RecuperarTtipsuplSP(DataSource dataSource) {
    		super(dataSource, "P_GET_TTIPSUPL_X_CDTIPSUP");
            declareParameter(new SqlParameter("cdtipsup", OracleTypes.VARCHAR));
            String[] cols = new String[] {
            		"CDTIPSUP", "CDREGION", "CDIDIOMA", "DSTIPSUP",
            		"SWSUPLEM", "SWTARIFI", "CDTIPDOC", "CDTIPEND"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	@Override
	public Map<String,String> recuperarDatosFlujoRenovacion(String cdramo, String tipoflot) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdramo"   , cdramo);
		params.put("tipoflot" , tipoflot);
		Map<String,Object> procRes = ejecutaSP(new RecuperarDatosFlujoRenovacionSP(getDataSource()),params);
		Map<String,String> result  = new HashMap<String,String>();
		result.put("cdtipflu"  , (String)procRes.get("pv_cdtipflu_o"));
		result.put("cdflujomc" , (String)procRes.get("pv_cdflujomc_o"));
		return result;
	}
	
	protected class RecuperarDatosFlujoRenovacionSP extends StoredProcedure
	{
		protected RecuperarDatosFlujoRenovacionSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_DATOS_FLUJO_RENOVA");
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("tipoflot" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipflu_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdflujomc_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarCorreoEmisionTramite (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarCorreoEmisionTramiteSP(getDataSource()), params);
		String correo = (String) procRes.get("pv_correo_o");
		if (StringUtils.isBlank(correo)) {
			correo = "";
		}
		return correo;
	}
	
	protected class RecuperarCorreoEmisionTramiteSP extends StoredProcedure
	{
		protected RecuperarCorreoEmisionTramiteSP(DataSource dataSource)
		{
			super(dataSource, "P_GET_CORREO_EMISION_TRAMITE");
            declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_correo_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerContratantes(
			String cdunieco,
			String cdramo,
			String cadena)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cdunieco_i", cdunieco);
		params.put("pv_cdramo_i"  , cdramo);
		params.put("pv_cadena_i"  , cadena);
		Map<String,Object>procResult  = ejecutaSP(new ObtenerContratantes(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "P_OBTENER_CONTRATANTES", params,lista);
		return lista;
	}
	
	protected class ObtenerContratantes extends StoredProcedure
    {
    	protected ObtenerContratantes(DataSource dataSource)
        {
            super(dataSource,"P_OBTENER_CONTRATANTES");
            declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cadena_i"  , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"cdperson"  ,"nombre_completo"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>> obtenerContratantesRfc(String cadena)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_cadena_i"  , cadena);
		Map<String,Object>procResult  = ejecutaSP(new ObtenerContratantesRfc(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "P_OBTENER_CONTRATANTES_RFC", params,lista);
		return lista;
	}
	
	protected class ObtenerContratantesRfc extends StoredProcedure
    {
    	protected ObtenerContratantesRfc(DataSource dataSource)
        {
            super(dataSource,"P_OBTENER_CONTRATANTES_RFC");
            declareParameter(new SqlParameter("pv_cadena_i"  , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"cdperson"  ,"nombre_completo"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
		
	@Override
	public List<Map<String,String>> cargaLayoutImpresion(String filename,
														 String tipoimp,
														 String cdusuario)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_filename_i"  , filename);
		params.put("pv_cdtipimp_i"  , tipoimp);
		params.put("pv_cdusuari_i"  , cdusuario);
		
		Map<String,Object>procResult  = ejecutaSP(new CargaLayoutImpresion(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		Utils.debugProcedure(logger, "P_OBTENER_CONTRATANTES_RFC", params,lista);
		return lista;
	}
	
	protected class CargaLayoutImpresion extends StoredProcedure
    {
    	protected CargaLayoutImpresion(DataSource dataSource)
        {
            super(dataSource,"PKG_IMPRESION.P_CARGA_LAYOUT_IMPRESION");
            declareParameter(new SqlParameter("pv_filename_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipimp_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i"  , OracleTypes.VARCHAR));
            
            String[] cols = new String[]{
            		"IDPROCESO"
            		,"CDTIPIMP"
            		,"SUCURSAL"
            		,"RAMO"
            		,"POLIZA"
            		,"CDTIPEND"
            		,"NUMENDOSO"
            		,"OBSERVAC"
            		,"SEQRECIBO"
            		,"NMCERTIF"
            		,"SWIMPRIME"
            		
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public String eliminaZwimpxlayout(String pv_idproceso_i)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_idproceso_i"  , pv_idproceso_i);
		
		Map<String,Object>procResult  = ejecutaSP(new EliminaZwimpxlayout(getDataSource()),params);
		return (String) procResult.get("pv_title_o");
	}
		
	
	protected class EliminaZwimpxlayout extends StoredProcedure
    {
    	protected EliminaZwimpxlayout(DataSource dataSource)
        {
            super(dataSource,"PKG_IMPRESION.P_ELIMINA_ZWIMPXLAYOUT");
            declareParameter(new SqlParameter("pv_idproceso_i"  , OracleTypes.VARCHAR));
          
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public List<Map<String,String>> getDocumentosLayout(String pv_idproceso_i
									  ,String pv_cdtipimp_i
									  ,String pv_papel_i
									  ,String pv_cdusuari_i
									  ,String pv_cdsisrol_i)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("pv_idproceso_i"  , pv_idproceso_i);
		params.put("pv_cdtipimp_i"  , pv_cdtipimp_i);
		params.put("pv_papel_i"  , pv_papel_i);
		params.put("pv_cdusuari_i"  , pv_cdusuari_i);
		params.put("pv_cdsisrol_i"  , pv_cdsisrol_i);
		
		Map<String,Object>procResult  = ejecutaSP(new GetDocumentosLayout(getDataSource()),params);
		return (List<Map<String, String>>) procResult.get("pv_registro_o");
	}
		
	
	protected class GetDocumentosLayout extends StoredProcedure
    {
    	protected GetDocumentosLayout(DataSource dataSource)
        {
            super(dataSource,"PKG_IMPRESION.P_GET_DOCUMENTOS_LAYOUT");
            declareParameter(new SqlParameter("pv_idproceso_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipimp_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_papel_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i"  , OracleTypes.VARCHAR));
            String[] cols = new String[]{
            		"cdbroker"
            		,"cdagente"
            		,"ordenimp"
            		,"cdsubram"
            		,"tipend"
            		,"nmpoliza"
            		,"cddocume"
            		,"dsdocume"
            		,"nmcopias"
            		,"numrec"
            		,"nmordina"
            		,"remesa"
            		,"tipodoc"
            		,"ntramite"
            		,"nmcertif"
            		,"nmsituac"
            		,"orden"
            		,"numend"
            		,"swimpdpx"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
    	}
    }
	
	@Override
	public void actualizaFlujoTramite(
			String ntramite,
			String cdflujomc,
			String cdtipflu
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite"    ,ntramite);
		params.put("cdflujomc"   ,cdflujomc);
		params.put("cdtipflu"    ,cdtipflu);
		ejecutaSP(new ActualizaFlujoTramite(getDataSource()),params);
	}
	
	protected class ActualizaFlujoTramite extends StoredProcedure{
		protected ActualizaFlujoTramite(DataSource dataSource){
			super(dataSource,"P_ACTUALIZA_FLUJOMC");
			declareParameter(new SqlParameter("ntramite"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipflu"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	@Override
    public List<Map<String,String>> formasPagoRetenedora(String administradora,String retenedora) throws Exception
    {
	 	Map<String,String> params=new HashMap<String, String>();
	 	params.put("pv_administradora_i", administradora);
	 	params.put("pv_retenedora_i", retenedora);
    	Map<String,Object>respuestaProcedure=ejecutaSP(new FormasPagoRetenedora(getDataSource()), params);
    	List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
    	
    	return lista;
    }
	
    protected class FormasPagoRetenedora extends StoredProcedure {
    	
    	protected FormasPagoRetenedora(DataSource dataSource) {
    		
    		super(dataSource, "PKG_RETENEDORAS.P_GET_FORMAS_PAGO_RETENEDORA");
			declareParameter(new SqlParameter("pv_administradora_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_retenedora_i" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"fpago",
					"fecha_termino"
			};
			
    		declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
    public List<Map<String,String>> obtieneCdagente(String pv_cdusuari_i) throws Exception
    {
        Map<String,String> params=new HashMap<String, String>();
        params.put("pv_cdusuari_i", pv_cdusuari_i);
        
        Map<String,Object>respuestaProcedure=ejecutaSP(new ObtieneCdagente(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        
        return lista;
    }
    
    protected class ObtieneCdagente extends StoredProcedure {
        
        protected ObtieneCdagente(DataSource dataSource) {
            
            super(dataSource, "Pkg_CONSULTA2.P_GET_CDAGENTE");
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            
            String[] cols = new String[]{
                    "cdagente",
                   
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    
    @Override
    public List<Map<String,String>> remesaDocumentosLayout(String pv_idproceso_i,
                                                            String pv_cdtipimp_i,
                                                            String pv_cdusuari_i,
                                                            String pv_cdsisrol_i) throws Exception
    {
        Map<String,String> params=new HashMap<String, String>();
        params.put("pv_idproceso_i", pv_idproceso_i);
        params.put("pv_cdtipimp_i", pv_cdtipimp_i);
        params.put("pv_cdusuari_i", pv_cdusuari_i);
        params.put("pv_cdsisrol_i", pv_cdsisrol_i);
        
        Map<String,Object>respuestaProcedure=ejecutaSP(new RemesaDocumentosLayout(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        
        return lista;
    }
    
    protected class RemesaDocumentosLayout extends StoredProcedure {
        
        protected RemesaDocumentosLayout(DataSource dataSource) {
            
            super(dataSource, "PKG_IMPRESION.P_GEN_REMESA_DOCUMENTOS_LAYOUT");
            declareParameter(new SqlParameter("pv_idproceso_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipimp_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String, String>> obtenerCursorTrafudoc(
            String cdfunci,
            String cdramo,
            String cdtipsit) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("cdfunci",  cdfunci);
        params.put("cdramo",   cdramo);
        params.put("cdtipsit", cdtipsit);
        Map<String, Object> procRes = ejecutaSP(new ObtenerCursorTrafudoc(getDataSource()), params);
        List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
        logger.info(
                new StringBuilder()
                .append("\n@@@ obtenerCursorTrafudoc @@@")
                .append("\n@@@ lista").append(lista)
                .append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                .toString()
                );
        return lista;
    }

    protected class ObtenerCursorTrafudoc extends StoredProcedure {
        protected ObtenerCursorTrafudoc(DataSource dataSource) {
            super(dataSource, "PKG_DOCUMENTOS.P_GET_TRAFUDOC");
            declareParameter(new SqlParameter("cdfunci",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo",   OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR,  new DynamicMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    
    @Override
    public List<Map<String,String>> obtieneRetAdmin(String administradora,String retenedora) throws Exception
    {
        Map<String,String> params=new HashMap<String, String>();
        params.put("pv_numsuc_i", administradora);
        params.put("pv_cveent_i", retenedora);
        Map<String,Object>respuestaProcedure=ejecutaSP(new ObtieneRetAdmin(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        
        return lista;
    }
    
    protected class ObtieneRetAdmin extends StoredProcedure {
        
        protected ObtieneRetAdmin(DataSource dataSource) {
            
            super(dataSource, "PKG_RETENEDORAS.P_GET_RSDXNADMRET");
            declareParameter(new SqlParameter("pv_numsuc_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cveent_i" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "OTCLAVE1"
                    ,"OTCLAVE2"
                    ,"OTVALOR01"
                    ,"OTVALOR02"
                    ,"OTVALOR03"
                    ,"OTVALOR04"
                    ,"OTVALOR05"
                    ,"OTVALOR06"
                    ,"OTVALOR07"
                    ,"OTVALOR08"
                    ,"OTVALOR09"
                    ,"OTVALOR10"
                    ,"OTVALOR11"
                    ,"OTVALOR12"
                    ,"OTVALOR13"
                    ,"OTVALOR14"
                    ,"OTVALOR15"
                    ,"OTVALOR16"
                    ,"OTVALOR17"
                    ,"OTVALOR18"
                    ,"OTVALOR19"
                    ,"OTVALOR20"
                    ,"OTVALOR21"
                    ,"OTVALOR22"
                    ,"OTVALOR23"
                    ,"OTVALOR24"
                    ,"OTVALOR25"
                    ,"OTVALOR26"
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public Map<String,String> obtieneUsuarioXAgente(String pv_cdagente_i) throws Exception
    {
        Map<String,String> params=new HashMap<String, String>();
        params.put("pv_cdagente_i", pv_cdagente_i);
        
        
        Map<String,Object>respuestaProcedure=ejecutaSP(new ObtieneUsuarioXAgente(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        if(lista.isEmpty()){
            throw new ApplicationException("No hay datos para el agente");
        }
        
        return lista.get(0);
    }
    
    protected class ObtieneUsuarioXAgente extends StoredProcedure {
        
        protected ObtieneUsuarioXAgente(DataSource dataSource) {
            
            super(dataSource, "Pkg_CONSULTA2.P_GET_TUSUARIO_X_AGENTE");
            declareParameter(new SqlParameter("pv_cdagente_i" , OracleTypes.VARCHAR));
            
            String[] cols = new String[]{
                    "CDUSUARI",
                    "DSUSUARI",
                    "CDUNIECO",
                    "CDPERSON",
                    "CDUNISLD"
                   
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String recuperaAgentePoliza(String cdunieco, String cdramo, String estado, String nmpoliza, String cdusuari) throws Exception{
        Map<String,String>params = new LinkedHashMap<String,String>();
        params.put("cdunieco" , cdunieco);
        params.put("cdramo"   , cdramo);
        params.put("estado"   , estado);
        params.put("nmpoliza" , nmpoliza);
        params.put("cdusuari" , cdusuari);
        Map<String,Object> procResult = ejecutaSP(new RecuperaAgentePoliza(getDataSource()),params);        
        Map<String,String> salida     = new LinkedHashMap<String,String>();
        String cdagente = (String)procResult.get("pv_cdagente_o");
        return cdagente;
    }
        
    protected class RecuperaAgentePoliza extends StoredProcedure
    {
        protected RecuperaAgentePoliza(DataSource dataSource)
        {
            super(dataSource,"P_GET_AGENTE_POLIZA");
            declareParameter(new SqlParameter("cdunieco" ,        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   ,        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   ,        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" ,        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" ,        OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdagente_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" ,  OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  ,  OracleTypes.VARCHAR));
            compile();
        }
    }
    
    
    @Override
    public String verificaFusFamilia(String pv_cdunieco_i,
                                      String pv_cdramo_i,
                                      String pv_estado_i,
                                      String pv_nmpoliza_i, 
                                      String pv_nmsuplem_i,
                                      String pv_cdusuari) throws Exception {
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdunieco_i",  pv_cdunieco_i);
        params.put("pv_cdramo_i",    pv_cdramo_i);
        params.put("pv_estado_i",    pv_estado_i);
        params.put("pv_nmpoliza_i",  pv_nmpoliza_i);
        params.put("pv_nmsuplem_i",  pv_nmsuplem_i);
        params.put("pv_cdusuari",    pv_cdusuari);
        Map<String, Object> procRes = ejecutaSP(new VerificaFusFamilia(getDataSource()), params);
        String resp = (String) procRes.get("pv_title_o");
        
        return resp;
    }

    protected class VerificaFusFamilia extends StoredProcedure {
        protected VerificaFusFamilia(DataSource dataSource) {
            super(dataSource, "pkg_db_report.val_tlockfus");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    
    @Override
    public List<Map<String,String>> titularesFus(       String pv_cdunieco_i,
                                      String pv_cdramo_i,
                                      String pv_estado_i,
                                      String pv_nmpoliza_i, 
                                      String pv_nmsuplem_i) throws Exception {
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdunieco_i",  pv_cdunieco_i);
        params.put("pv_cdramo_i",    pv_cdramo_i);
        params.put("pv_estado_i",    pv_estado_i);
        params.put("pv_nmpoliza_i",  pv_nmpoliza_i);
        params.put("pv_nmsuplem_i",  pv_nmsuplem_i);
        
        Map<String, Object> respuestaProcedure = ejecutaSP(new TitularesFus(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        if(lista.isEmpty()){
            throw new ApplicationException("No hay titulares");
        }
        
        return lista;
        
    }

    protected class TitularesFus extends StoredProcedure {
        protected TitularesFus(DataSource dataSource) {
            super(dataSource, "pkg_db_report.get_tit_fus");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "cdunieco",
                    "cdramo",
                    "estado",
                    "nmpoliza",
                    "cdperson",
                    "nombre",
                    "nmsitaux"
                   
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void actualizaEstadoTFusLock(String pv_cdunieco_i,
                                                            String pv_cdramo_i,
                                                            String pv_estado_i,
                                                            String pv_nmpoliza_i, 
                                                            String pv_nmsuplem_i,
                                                            String pv_swestado_i) throws Exception {
        
        if(pv_estado_i.equalsIgnoreCase("W") && pv_estado_i.equalsIgnoreCase("F") ){
            throw new ApplicationException("swestado no v\u00E1lido");
        }
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdunieco_i",  pv_cdunieco_i);
        params.put("pv_cdramo_i",    pv_cdramo_i);
        params.put("pv_estado_i",    pv_estado_i);
        params.put("pv_nmpoliza_i",  pv_nmpoliza_i);
        params.put("pv_nmsuplem_i",  pv_nmsuplem_i);
        params.put("pv_swestado_i",  pv_swestado_i);
        
        Map<String, Object> respuestaProcedure = ejecutaSP(new ActualizaEstadoTFusLock(getDataSource()), params);
        
        
        
        
    }

    protected class ActualizaEstadoTFusLock extends StoredProcedure {
        protected ActualizaEstadoTFusLock(DataSource dataSource) {
            super(dataSource, "pkg_db_report.set_estado_tlockfus");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_swestado_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>> docsXTitular(String pv_cdunieco_i,
                                                 String pv_cdramo_i  ,
                                                 String pv_estado_i  ,
                                                 String pv_nmpoliza_i, 
                                                 String pv_nmsuplem_i,
                                                 String pv_nmsitaux_i) throws Exception {
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdunieco_i",  pv_cdunieco_i);
        params.put("pv_cdramo_i"  ,  pv_cdramo_i  );
        params.put("pv_estado_i"  ,  pv_estado_i  );
        params.put("pv_nmpoliza_i",  pv_nmpoliza_i);
        params.put("pv_nmsuplem_i",  pv_nmsuplem_i);
        params.put("pv_nmsitaux_i",  pv_nmsitaux_i);
        
        Map<String, Object> respuestaProcedure = ejecutaSP(new DocsXTitular(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        
        
        return lista;
        
    }

    protected class DocsXTitular extends StoredProcedure {
        protected DocsXTitular(DataSource dataSource) {
            super(dataSource, "pkg_db_report.get_doc_fus");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsitaux_i",  OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "cdunieco",
                    "cdramo",
                    "estado",
                    "nmpoliza",
                    "nmsolici",
                    "ntramite",
                    "feinici",
                    "cddocume",
                    "dsdocume",
                    "tipmov",
                    "swvisible",
                    "cdtiptra",
                    "codidocu",
                    "fefecha",
                    "cdorddoc",
                    "cdmoddoc",
                    "nmcertif",
                    "nmsituac",
                    "cdusuari",
                    "cdsisrol"

                   
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void movTdocupolFus(String pv_cdunieco_i,
                                                 String pv_cdramo_i  ,
                                                 String pv_estado_i  ,
                                                 String pv_nmpoliza_i, 
                                                 String pv_nmsuplem_i,
                                                 String pv_nmsolici_i,
                                                 String pv_ntramite_i,
                                                 Date pv_feinici_i,
                                                 String pv_cddocume_i    ,
                                                 String pv_dsdocume_i    ,
                                                 String  pv_tipmov_i      ,
                                                 String pv_swvisible_i   ,
                                                 String pv_cdtiptra_i    ,
                                                 String pv_codidocu_i    ,
                                                 Date pv_fefecha_i     ,
                                                 String pv_cdorddoc_i    ,
                                                 String pv_cdmoddoc_i    ,
                                                 String pv_nmcertif_i    ,
                                                 String pv_nmsituac_i    ,
                                                 String pv_cdusuari_i    ,
                                                 String pv_cdsisrol_i    ) throws Exception {
        
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("pv_cdunieco_i",  pv_cdunieco_i);
        params.put("pv_cdramo_i"  ,  pv_cdramo_i  );
        params.put("pv_estado_i"  ,  pv_estado_i  );
        params.put("pv_nmpoliza_i",  pv_nmpoliza_i);
        params.put("pv_nmsuplem_i",  pv_nmsuplem_i);
        params.put("pv_nmsolici_i",  pv_nmsolici_i);
        params.put("pv_ntramite_i",  pv_ntramite_i);
        params.put("pv_feinici_i",   pv_feinici_i);
        params.put("pv_cddocume_i",  pv_cddocume_i);
        params.put("pv_dsdocume_i",  pv_dsdocume_i);
        params.put("pv_tipmov_i",    pv_tipmov_i);
        params.put("pv_swvisible_i", pv_swvisible_i);
        params.put("pv_cdtiptra_i",  pv_cdtiptra_i);
        params.put("pv_codidocu_i",  pv_codidocu_i);
        params.put("pv_fefecha_i",   pv_fefecha_i);
        params.put("pv_cdorddoc_i",  pv_cdorddoc_i);
        params.put("pv_cdmoddoc_i",  pv_cdmoddoc_i);
        params.put("pv_nmcertif_i",  pv_nmcertif_i);
        params.put("pv_nmsituac_i",  pv_nmsituac_i);
        params.put("pv_cdusuari_i",  pv_cdusuari_i);
        params.put("pv_cdsisrol_i",  pv_cdsisrol_i);
        
        Map<String, Object> respuestaProcedure = ejecutaSP(new MovTdocupolFus(getDataSource()), params);
        
        
    }

    protected class MovTdocupolFus extends StoredProcedure {
        protected MovTdocupolFus(DataSource dataSource) {
            super(dataSource, "pkg_db_report.p_mov_tdocupolfus ");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsolici_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_ntramite_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_feinici_i",  OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_cddocume_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_dsdocume_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_tipmov_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_swvisible_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtiptra_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_codidocu_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_fefecha_i",  OracleTypes.DATE));
            declareParameter(new SqlParameter("pv_cdorddoc_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdmoddoc_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmcertif_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsituac_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i",  OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
        
        
    }
    
    @Override
    public String obtieneNmsituaext(String pv_cdunieco_i,
                                                 String pv_cdramo_i  ,
                                                 String pv_estado_i  ,
                                                 String pv_nmpoliza_i, 
                                                 String pv_nmsuplem_i,
                                                 String pv_nmsitaux_i) throws Exception {
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdunieco_i",  pv_cdunieco_i);
        params.put("pv_cdramo_i"  ,  pv_cdramo_i  );
        params.put("pv_estado_i"  ,  pv_estado_i  );
        params.put("pv_nmpoliza_i",  pv_nmpoliza_i);
        params.put("pv_nmsuplem_i",  pv_nmsuplem_i);
        params.put("pv_nmsitaux_i",  pv_nmsitaux_i);
        
        Map<String, Object> mapResult = ejecutaSP(new ObtieneNmsituaext(getDataSource()), params);
        String resp = (String) mapResult.get("pv_nmsituaext_o");
        
        return resp;
        
    }

    protected class ObtieneNmsituaext extends StoredProcedure {
        protected ObtieneNmsituaext(DataSource dataSource) {
            super(dataSource, "PKG_CONSULTA2.P_GET_NRO_EMPLEADO");
            declareParameter(new SqlParameter("pv_cdunieco_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsitaux_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsituaext_o"    , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public boolean isServicioCargaFederal(
            String clvgs
            ) throws Exception
    {
        Map<String,String> params = new LinkedHashMap<String,String>();
        params.put("PV_AMIS" , clvgs);
        Map<String,Object> procRes = ejecutaSP(new isServicioCargaFederal(getDataSource()),params);
        String             servicioCarga  = (String)procRes.get("PV_SALIDA_O");
        if(!StringUtils.isBlank(servicioCarga))
        {
            if(servicioCarga.equals("22") || servicioCarga.equals("23"))
            {
                return true;
            }
        }
        return false;
    }
    
    protected class isServicioCargaFederal extends StoredProcedure
    {
        protected isServicioCargaFederal(DataSource dataSource)
        {
            super(dataSource,"P_GET_SERVICIO_CARGA");
            declareParameter(new SqlParameter("PV_AMIS" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_SALIDA_O" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<GenericVO> obtieneComentariosNegocio(
                                                 String pv_cdramo_i  ,
                                                 String pv_cdtipsit_i  ,
                                                 String pv_negocio_i) throws Exception {
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdramo_i"    ,  pv_cdramo_i);
        params.put("pv_cdtipsit_i"  ,  pv_cdtipsit_i  );
        params.put("pv_negocio_i"   ,  pv_negocio_i  );
     
        
        Map<String, Object> resultado = ejecutaSP(new ObtieneComentariosNegocio(getDataSource()), params);
        return (List<GenericVO>) resultado.get("pv_registro_o");
        
    }

    protected class ObtieneComentariosNegocio extends StoredProcedure {
        protected ObtieneComentariosNegocio(DataSource dataSource) {
            super(dataSource, "PKG_CONSULTA2.P_OBTIENE_COMENTARIOS_NEGOCIO");
            declareParameter(new SqlParameter("pv_cdramo_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_negocio_i",    OracleTypes.VARCHAR));
          
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new ObtieneComentariosMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    protected class ObtieneComentariosMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GenericVO(rs.getString("COMENTARIO"),rs.getString("COMENTARIO"));
        }
    }
    
    
    @Override
    public List<Map<String,String>> obtieneRangoPeriodoGracia(
                                      String pv_cdramo_i,
                                      String pv_cdtipsit_i,
                                      String pv_cdagente_i) throws Exception {
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_cdramo_i",  pv_cdramo_i);
        params.put("pv_cdtipsit_i",pv_cdtipsit_i);
        params.put("pv_cdagente_i",pv_cdagente_i);
        
        Map<String, Object> respuestaProcedure = ejecutaSP(new ObtieneRangoPeriodoGracia(getDataSource()), params);
        List<Map<String,String>>lista=(List<Map<String,String>>)respuestaProcedure.get("pv_registro_o");
        
        return lista;
        
    }

    protected class ObtieneRangoPeriodoGracia extends StoredProcedure {
        protected ObtieneRangoPeriodoGracia(DataSource dataSource) {
            super(dataSource, "PKG_CONSULTA2.P_OBTIENE_PGRACIA_AGENTE");
            declareParameter(new SqlParameter("pv_cdramo_i",  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdagente_i",    OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "MINIMO",
                    "MAXIMO"
                   
            };
            
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>> recuperarEndososSiniestralidad(
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
        Map<String,Object>       procResult = ejecutaSP(new RecuperarEndososSiniestralidad(getDataSource()),params);
        List<Map<String,String>> lista      = (List<Map<String,String>>)procResult.get("pv_registro_o");
        if(lista==null)
        {
            lista = new ArrayList<Map<String,String>>();
        }
        return lista;
    }
    
    protected class RecuperarEndososSiniestralidad extends StoredProcedure
    {
        protected RecuperarEndososSiniestralidad(DataSource dataSource)
        {
            super(dataSource , "PKG_CANCELA.P_GET_ENDOSOS_X_POLIZA_SINIES");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "NSUPLOGI"  , "CDDEVCIA" , "CDGESTOR" , "FEEMISIO" , "FEINIVAL" , "FEFINVAL"
                    ,"FEEFECTO" , "FEPROREN" , "CDMONEDA" , "NMSUPLEM" , "FEINICIO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>> recuperarEndososRehabilitablesSiniestralidad(
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
        Map<String,Object>       procResult = ejecutaSP(new RecuperarEndosoSiniestralidad(getDataSource()),params);
        List<Map<String,String>> lista      = (List<Map<String,String>>)procResult.get("pv_registro_o");
        if(lista==null)
        {
            lista = new ArrayList<Map<String,String>>();
        }
        return lista;
    }
    
    protected class RecuperarEndosoSiniestralidad extends StoredProcedure
    {
        protected RecuperarEndosoSiniestralidad(DataSource dataSource)
        {
            super(dataSource , "PKG_CANCELA.P_GET_ENDOSOS_REHAB_SINI");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "NSUPLOGI"  , "CDDEVCIA" , "CDGESTOR" , "FEEMISIO" , "FEINIVAL" , "FEFINVAL"
                    ,"FEEFECTO" , "FEPROREN" , "CDMONEDA" , "NMSUPLEM"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String,String>> recuperarDatosValorDefectoLayout(
            String cdsisrol
            ,String campo
            ,String renovacionGral
            )throws Exception
    {
        Map<String,String> params = new LinkedHashMap<String,String>();
        params.put("pv_rol_i" 		, cdsisrol);
        params.put("pv_campo_i"   	, campo);
        params.put("pv_proceso_i"   , renovacionGral);
        Map<String,Object>       procResult = ejecutaSP(new RecuperarDatosValorDefectoLayout(getDataSource()),params);
        List<Map<String,String>> lista      = (List<Map<String,String>>)procResult.get("pv_registro_o");
        if(lista==null)
        {
            lista = new ArrayList<Map<String,String>>();
        }
        return lista;
    }
    
    protected class RecuperarDatosValorDefectoLayout extends StoredProcedure
    {
        protected RecuperarDatosValorDefectoLayout(DataSource dataSource)
        {
            super(dataSource , "PKG_CONSULTA2.P_GET_CARGA_LAYOUT");
            declareParameter(new SqlParameter("pv_rol_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_campo_i"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_proceso_i"   , OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "CAMPO"  , "OBLIGATORIO"
            };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String esDXN(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem) throws Exception{
        Map<String,String> params = new LinkedHashMap<String,String>();
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i"  , cdramo);
        params.put("pv_estado_i"  , estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        params.put("pv_nmsuplem_i", nmsuplem);
        Map<String,Object> procRes = ejecutaSP(new EsDXN(getDataSource()),params);
        String esDxn  = (String)procRes.get("pv_esDXN_o");
        return esDxn;
    }
    
    protected class EsDXN extends StoredProcedure{
        protected EsDXN(DataSource dataSource){
            super(dataSource,"P_ES_DXN");
            declareParameter(new SqlParameter("pv_cdunieco_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmsuplem_i"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_esDXN_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_error_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    private class ContarTramitesUsuarioRol extends Thread {
        private Map<String, String> usuario;
        public ContarTramitesUsuarioRol (Map<String, String> usuario) {
            this.usuario = usuario;
            this.usuario.put("pv_cdusuari_i", this.usuario.get("CDUSUARI"));
            this.usuario.put("pv_cdsisrol_i", this.usuario.get("CDSISROL"));
        }
        @Override
        public void run () {
            try {
                usuario.put("TOTAL", (String) ejecutaSP(new ContarTramitesUsuarioRolSP(getDataSource()), usuario).get("pv_count_o"));
            } catch (Exception ex) {
                logger.debug(Utils.join("Error al contar tramites de ", this.usuario), ex);
                usuario.put("TOTAL", "ERROR");
            }
        }
    }
    
    protected class ContarTramitesUsuarioRolSP extends StoredProcedure {
        protected ContarTramitesUsuarioRolSP (DataSource dataSource) {
            super(dataSource,"P_MC_GET_CONTEO_TRAMITES_USER");
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_count_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
}