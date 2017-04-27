package mx.com.gseguros.externo.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface StoredProceduresDAO
{
	public void                     procedureVoidCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception;
	
	public Map<String,String>       procedureMapCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception;
	
	public Map<String,Object>       procedureParamsCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>inputParamsValues,
			String[] inputParamsTypes,
			String[] outputParamsNames,
			String[] outputParamsTypes) throws Exception;
			
	public List<Map<String,String>> procedureListCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception;
			
	public Object[]                 procedureMixedCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception;
}