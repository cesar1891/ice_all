package mx.com.gseguros.externo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.externo.dao.StoredProceduresDAO;
import mx.com.gseguros.externo.service.StoredProceduresManager;

import org.apache.log4j.Logger;

public class StoredProceduresManagerImpl implements StoredProceduresManager
{

	private final static Logger logger = Logger.getLogger(StoredProceduresManagerImpl.class);
	private StoredProceduresDAO storedProceduresDAO;

	@Override
	public void procedureVoidCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		logger.info(""
				+ "\n###############################"
				+ "\n###### procedureVoidCall ######"
				);
		logger.info("storedProcedureName: "+storedProcedureName);
		logger.info("paramsValues: "+paramsValues);
		logger.info("paramsTypes: "+paramsTypes);
		storedProceduresDAO.procedureVoidCall(storedProcedureName, paramsValues, paramsTypes);
		logger.info(""
				+ "\n###### procedureVoidCall ######"
				+ "\n###############################"
				);
	}
	
	@Override
	public Map<String,String> procedureMapCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		logger.info(""
				+ "\n##############################"
				+ "\n###### procedureMapCall ######"
				);
		logger.info("storedProcedureName: "+storedProcedureName);
		logger.info("paramsValues: "+paramsValues);
		logger.info("paramsTypes: "+paramsTypes);
		Map<String,String>map=storedProceduresDAO.procedureMapCall(storedProcedureName, paramsValues, paramsTypes);
		if(map==null)
		{
			map=new HashMap<String,String>();
		}
		logger.info("response: "+map);
		logger.info(""
				+ "\n###### procedureMapCall ######"
				+ "\n##############################"
				);
		return map;
	}
	
	@Override
	public Map<String,Object> procedureParamsCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>inputParamsValues,
			String[] inputParamsTypes,
			String[] outputParamsNames,
			String[] outputParamsTypes) throws Exception
	{
		logger.info(""
				+ "\n#################################"
				+ "\n###### procedureParamsCall ######"
				);
		logger.info("storedProcedureName: "+storedProcedureName);
		logger.info("inputParamsValues: "+inputParamsValues);
		logger.info("inputParamsTypes: "+inputParamsTypes);
		logger.info("outputParamsNames: "+outputParamsNames);
		logger.info("outputParamsTypes: "+outputParamsTypes);
		Map<String,Object>map=
				storedProceduresDAO.procedureParamsCall(storedProcedureName, inputParamsValues, inputParamsTypes, 
						outputParamsNames, outputParamsTypes);
		if(map==null)
		{
			map=new HashMap<String,Object>();
		}
		logger.info("map: "+map);
		logger.info(""
				+ "\n###### procedureParamsCall ######"
				+ "\n#################################"
				);
		return map;
	}
	
	@Override
	public List<Map<String,String>> procedureListCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		logger.info(""
				+ "\n###############################"
				+ "\n###### procedureListCall ######"
				);
		logger.info("storedProcedureName: "+storedProcedureName);
		logger.info("paramsValues: "+paramsValues);
		logger.info("paramsTypes: "+paramsTypes);
		List<Map<String,String>>lista=storedProceduresDAO.procedureListCall(storedProcedureName, paramsValues, paramsTypes);
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		logger.info("response list size: "+lista.size());
		logger.info("response list: "+lista);
		logger.info(""
				+ "\n###### procedureListCall ######"
				+ "\n###############################"
				);
		return lista;
	}
	
	@Override
	public Object[] procedureMixedCall(
			String storedProcedureName,
			LinkedHashMap<String,Object>paramsValues,
			String[] paramsTypes) throws Exception
	{
		logger.info(""
				+ "\n################################"
				+ "\n###### procedureMixedCall ######"
				);
		logger.info("storedProcedureName: "+storedProcedureName);
		logger.info("paramsValues: "+paramsValues);
		logger.info("paramsTypes: "+paramsTypes);
		Object[]vector=storedProceduresDAO.procedureMixedCall(storedProcedureName, paramsValues, paramsTypes);
		if(vector[0]==null)
		{
			vector[0]=new ArrayList<Map<String,String>>();
		}
		if(vector[1]==null)
		{
			vector[1]=new HashMap<String,String>();
		}
		logger.info("response list [0] size: "+((List<Map<String,String>>)vector[0]).size());
		logger.info("response list [0]: "+(List<Map<String,String>>)vector[0]);
		logger.info("response map: "+(Map<String,String>)vector[1]);
		logger.info(""
				+ "\n###### procedureMixedCall ######"
				+ "\n################################"
				);
		return vector;
	}
	
	public void setStoredProceduresDAO(StoredProceduresDAO storedProceduresDAO) {
		this.storedProceduresDAO = storedProceduresDAO;
	}
	
}