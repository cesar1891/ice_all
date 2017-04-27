package mx.com.gseguros.portal.endosos.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.endosos.dao.EndososGrupoDAO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class EndososGrupoDAOImpl extends AbstractManagerDAO implements EndososGrupoDAO
{
	private static final Logger logger = Logger.getLogger(EndososGrupoDAOImpl.class);
	
	@Override
	public List<Map<String,String>>buscarHistoricoPolizas(String nmpoliex,String rfc,String cdperson,String nombre,String cdsisrol)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("nmpoliex" , nmpoliex);
		params.put("rfc"      , rfc);
		params.put("cdperson" , cdperson);
		params.put("nombre"   , nombre);
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************")
				.append("\n****** PKG_CONSULTA.P_GET_ENDOSOS_G_COL ******")
				.append("\n****** params=").append(params)
				.append("\n**********************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new BuscarHistoricoPolizas(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista=new ArrayList<Map<String,String>>();
		}
		logger.debug(
				new StringBuilder()
				.append("\n**********************************************")
				.append("\n****** registro=").append(lista)
				.append("\n****** PKG_CONSULTA.P_GET_ENDOSOS_G_COL ******")
				.append("\n**********************************************")
				.toString()
				);
		return lista;
	}
	
	protected class BuscarHistoricoPolizas extends StoredProcedure
	{
		protected BuscarHistoricoPolizas(DataSource dataSource)
		{
			super(dataSource, "PKG_CONSULTA.P_GET_ENDOSOS_G_COL");
			declareParameter(new SqlParameter("nmpoliex" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("rfc"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdperson" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nombre"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String cols[]=new String[]
					{
					"cdunieco"
					,"cdramo"
					,"estado"
					,"nmpoliza"
					,"nmsuplem"
					,"nmpoliex"
					,"nsuplogi"
					,"feemisio"
					,"feinival"
					,"dscoment"
					,"cdtipsit"
					,"dstipsit"
					,"prima_total"
					,"ntramite"
					};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarFamiliasPoliza(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** PKG_ENDOSOS.P_GET_FAMILIAS_POLIZA ******")
				.append("\n****** params=").append(params)
				.append("\n***********************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarFamiliasPoliza(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		for(Map<String,String>familia:lista)
		{
			familia.put("merged",new StringBuilder(familia.get("familia")).append("#_#").append(familia.get("titular")).toString());
		}
		logger.debug(
				new StringBuilder()
				.append("\n***********************************************")
				.append("\n****** lista=").append(lista)
				.append("\n****** PKG_ENDOSOS.P_GET_FAMILIAS_POLIZA ******")
				.append("\n***********************************************")
				.toString()
				);
		return lista;
	}
	
	protected class CargarFamiliasPoliza extends StoredProcedure
	{
		protected CargarFamiliasPoliza(DataSource dataSource)
		{
			super(dataSource, "PKG_ENDOSOS.P_GET_FAMILIAS_POLIZA");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			String cols[]=new String[]
					{
					"cdunieco"
					,"cdramo"
					,"estado"
					,"nmpoliza"
					,"nmsituac"
					,"familia"
					,"titular"
					,"nmsuplem"
					};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarIntegrantesFamilia(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,String nmsitaux)throws Exception
	{
		Map<String,String>params=new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsuplem" , nmsuplem);
		params.put("nmsitaux" , nmsitaux);
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** PKG_ENDOSOS.P_GET_INTEGRANTES_FAMILIA ******")
				.append("\n****** params=").append(params)
				.append("\n***************************************************")
				.toString()
				);
		Map<String,Object>procResult  = ejecutaSP(new CargarIntegrantesFamilia(getDataSource()),params);
		List<Map<String,String>>lista = (List<Map<String,String>>)procResult.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		logger.debug(
				new StringBuilder()
				.append("\n***************************************************")
				.append("\n****** lista=").append(lista)
				.append("\n****** PKG_ENDOSOS.P_GET_INTEGRANTES_FAMILIA ******")
				.append("\n***************************************************")
				.toString()
				);
		return lista;
	}
	
	protected class CargarIntegrantesFamilia extends StoredProcedure
	{
		protected CargarIntegrantesFamilia(DataSource dataSource)
		{
			super(dataSource, "PKG_ENDOSOS.P_GET_INTEGRANTES_FAMILIA");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsitaux" , OracleTypes.VARCHAR));
			String cols[]=new String[]
					{
					"cdunieco"
					,"cdramo"
					,"estado"
					,"nmpoliza"
					,"nmsuplem"
					,"nmsituac"
					,"parentesco"
					,"nombre"
					,"sexo"
					,"rfc"
					,"nmsuplem_endoso"
					,"familia"
					};
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
}