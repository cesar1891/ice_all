package mx.com.gseguros.externo.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import mx.com.gseguros.externo.dao.StoredProceduresDAO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class StoredProceduresDAOImpl extends AbstractManagerDAO implements StoredProceduresDAO
{
	private final static Logger logger = Logger.getLogger(StoredProceduresDAOImpl.class);
	
	@Override
	public void procedureVoidCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		this.ejecutaSP(new ProcedureVoidCall(storedProcedureName, paramsValues, getDataSource(), paramsTypes), paramsValues);
	}
	
	protected class ProcedureVoidCall extends StoredProcedure
	{
		protected ProcedureVoidCall(String storedProcedure, LinkedHashMap<String,Object> params, DataSource dataSource, String[]paramsTypes)
		{
			super(dataSource, storedProcedure);
			if(params!=null)
			{
				int i=0;
				for(Entry<String,Object>param : params.entrySet())
				{
					int type=OracleTypes.VARCHAR;
					if(paramsTypes!=null)
					{
						if(paramsTypes[i].equals("VARCHAR"))
						{
							type=OracleTypes.VARCHAR;
						}
						else if(paramsTypes[i].equals("NUMERIC"))
						{
							type=OracleTypes.NUMERIC;
						}
						else if(paramsTypes[i].equals("DATE"))
						{
							type=OracleTypes.DATE;
						}
						else if(paramsTypes[i].equals("TIMESTAMP"))
						{
							type=OracleTypes.TIMESTAMP;
						}
						else if(paramsTypes[i].equals("CURSOR"))
						{
							type=OracleTypes.CURSOR;
						}
					}
					declareParameter(new SqlParameter(param.getKey() , type));
					i=i+1;
				}
			}
	        declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> procedureMapCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		Map<String,Object>result = this.ejecutaSP(new ProcedureMapCall(storedProcedureName, paramsValues, getDataSource(),paramsTypes), paramsValues);
		List<Map<String,String>>list = (List<Map<String,String>>) result.get("pv_registro_o");
		Map<String,String>map=null;
		if(list!=null&&list.size()>0)
		{
			map=list.get(0);
		}
		return map;
	}
	
	protected class ProcedureMapCall extends StoredProcedure
	{
		protected ProcedureMapCall(String storedProcedure, LinkedHashMap<String,Object> params, DataSource dataSource, String[]paramsTypes)
		{
			super(dataSource, storedProcedure);
			if(params!=null)
			{
				int i=0;
				for(Entry<String,Object>param : params.entrySet())
				{
					int type=OracleTypes.VARCHAR;
					if(paramsTypes!=null)
					{
						if(paramsTypes[i].equals("VARCHAR"))
						{
							type=OracleTypes.VARCHAR;
						}
						else if(paramsTypes[i].equals("NUMERIC"))
						{
							type=OracleTypes.NUMERIC;
						}
						else if(paramsTypes[i].equals("DATE"))
						{
							type=OracleTypes.DATE;
						}
						else if(paramsTypes[i].equals("TIMESTAMP"))
						{
							type=OracleTypes.TIMESTAMP;
						}
						else if(paramsTypes[i].equals("CURSOR"))
						{
							type=OracleTypes.CURSOR;
						}
					}
					declareParameter(new SqlParameter(param.getKey() , type));
					i=i+1;
				}
			}
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DinamicMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,Object> procedureParamsCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>inputParamsValues,
			String[] inputParamsTypes,
			String[] outputParamsNames,
			String[] outputParamsTypes) throws Exception
	{
		Map<String,Object>result = this.ejecutaSP(new ProcedureParamsCall(
				storedProcedureName, inputParamsValues, getDataSource(),inputParamsTypes,outputParamsNames,outputParamsTypes
				), inputParamsValues);
		return result;
	}
	
	protected class ProcedureParamsCall extends StoredProcedure
	{
		protected ProcedureParamsCall(String storedProcedure, LinkedHashMap<String,Object> inputParamsValues, DataSource dataSource
				,String[]inputParamsTypes
				,String[]outputParamsNames
				,String[]outputParamsTypes)
		{
			super(dataSource, storedProcedure);
			if(inputParamsValues!=null)
			{
				int i=0;
				for(Entry<String,Object>param : inputParamsValues.entrySet())
				{
					int type=OracleTypes.VARCHAR;
					if(inputParamsTypes!=null)
					{
						if(inputParamsTypes[i].equals("VARCHAR"))
						{
							type=OracleTypes.VARCHAR;
						}
						else if(inputParamsTypes[i].equals("NUMERIC"))
						{
							type=OracleTypes.NUMERIC;
						}
						else if(inputParamsTypes[i].equals("DATE"))
						{
							type=OracleTypes.DATE;
						}
						else if(inputParamsTypes[i].equals("TIMESTAMP"))
						{
							type=OracleTypes.TIMESTAMP;
						}
						else if(inputParamsTypes[i].equals("CURSOR"))
						{
							type=OracleTypes.CURSOR;
						}
					}
					declareParameter(new SqlParameter(param.getKey() , type));
					logger.debug("in param: "+param.getKey()+", tipo: "+type);
					i=i+1;
				}
			}
			if(outputParamsNames!=null)
			{
				for(int i=0;i<outputParamsNames.length;i++)
				{
					int type=OracleTypes.VARCHAR;
					if(outputParamsTypes!=null)
					{
						if(outputParamsTypes[i].equals("VARCHAR"))
						{
							type=OracleTypes.VARCHAR;
						}
						else if(outputParamsTypes[i].equals("NUMERIC"))
						{
							type=OracleTypes.NUMERIC;
						}
						else if(outputParamsTypes[i].equals("DATE"))
						{
							type=OracleTypes.DATE;
						}
						else if(outputParamsTypes[i].equals("TIMESTAMP"))
						{
							type=OracleTypes.TIMESTAMP;
						}
						else if(outputParamsTypes[i].equals("CURSOR"))
						{
							type=OracleTypes.CURSOR;
						}
					}
					if(type==OracleTypes.CURSOR)
					{
						declareParameter(new SqlOutParameter(outputParamsNames[i] , type, new DinamicMapper()));
						logger.debug("out param: "+outputParamsNames[i]+", tipo cursor");
					}
					else
					{
						declareParameter(new SqlOutParameter(outputParamsNames[i] , type));						
						logger.debug("out param: "+outputParamsNames[i]+", tipo: "+type);
					}
					i=i+1;
				}
			}
	        declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> procedureListCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		logger.debug(
				new StringBuilder()
				.append("\n*******************************************")
				.append("\n****** ").append(storedProcedureName)
				.append("\n****** params=").append(paramsValues)
				.append("\n*******************************************")
				.toString()
				);
		Map<String,Object>result = this.ejecutaSP(new ProcedureListCall(storedProcedureName, paramsValues, getDataSource(),paramsTypes), paramsValues);
		return (List<Map<String,String>>) result.get("pv_registro_o");
	}
	
	protected class ProcedureListCall extends StoredProcedure
	{
		protected ProcedureListCall(String storedProcedure, LinkedHashMap<String,Object> params, DataSource dataSource, String[]paramsTypes)
		{
			super(dataSource, storedProcedure);
			if(params!=null)
			{
				int i=0;
				for(Entry<String,Object>param : params.entrySet())
				{
					int type=OracleTypes.VARCHAR;
					if(paramsTypes!=null)
					{
						if(paramsTypes[i].equals("VARCHAR"))
						{
							type=OracleTypes.VARCHAR;
						}
						else if(paramsTypes[i].equals("NUMERIC"))
						{
							type=OracleTypes.NUMERIC;
						}
						else if(paramsTypes[i].equals("DATE"))
						{
							type=OracleTypes.DATE;
						}
						else if(paramsTypes[i].equals("TIMESTAMP"))
						{
							type=OracleTypes.TIMESTAMP;
						}
						else if(paramsTypes[i].equals("CURSOR"))
						{
							type=OracleTypes.CURSOR;
						}
					}
					declareParameter(new SqlParameter(param.getKey() , type));
					i=i+1;
				}
			}
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new DinamicMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Object[] procedureMixedCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		Object[] response = new Object[2];
		Map<String,Object>result = this.ejecutaSP(new ProcedureMixedCall(storedProcedureName, paramsValues, getDataSource(),paramsTypes), paramsValues);
		response[0]=(List<Map<String,String>>) result.get("pv_registro_o");
		Map<String,String>map=null;
		List<Map<String,String>>list=(List<Map<String,String>>)result.get("pv_parametros_o");
		if(list!=null&&list.size()>0)
		{
			map=list.get(0);
		}
		response[1]=map; 
		return response;
	}
	
	protected class ProcedureMixedCall extends StoredProcedure
	{
		protected ProcedureMixedCall(String storedProcedure, LinkedHashMap<String,Object> params, DataSource dataSource, String[]paramsTypes)
		{
			super(dataSource, storedProcedure);
			if(params!=null)
			{
				int i=0;
				for(Entry<String,Object>param : params.entrySet())
				{
					int type=OracleTypes.VARCHAR;
					if(paramsTypes!=null)
					{
						if(paramsTypes[i].equals("VARCHAR"))
						{
							type=OracleTypes.VARCHAR;
						}
						else if(paramsTypes[i].equals("NUMERIC"))
						{
							type=OracleTypes.NUMERIC;
						}
						else if(paramsTypes[i].equals("DATE"))
						{
							type=OracleTypes.DATE;
						}
						else if(paramsTypes[i].equals("TIMESTAMP"))
						{
							type=OracleTypes.TIMESTAMP;
						}
						else if(paramsTypes[i].equals("CURSOR"))
						{
							type=OracleTypes.CURSOR;
						}
					}
					declareParameter(new SqlParameter(param.getKey() , type));
					i=i+1;
				}
			}
			declareParameter(new SqlOutParameter("pv_registro_o"   , OracleTypes.CURSOR, new DinamicMapper()));
			declareParameter(new SqlOutParameter("pv_parametros_o" , OracleTypes.CURSOR, new DinamicMapper()));
	        declareParameter(new SqlOutParameter("pv_msg_id_o"     , OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o"      , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	private class DinamicMapper implements RowMapper
	{
		private Logger logger=Logger.getLogger(DinamicMapper.class);
	
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