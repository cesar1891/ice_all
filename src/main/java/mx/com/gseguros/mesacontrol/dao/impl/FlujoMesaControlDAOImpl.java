package mx.com.gseguros.mesacontrol.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.data.jdbc.support.oracle.SqlStructValue;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.mesacontrol.dao.FlujoMesaControlDAO;
import mx.com.gseguros.mesacontrol.model.FlujoVO;
import mx.com.gseguros.mesacontrol.model.TvaloperVO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

public class FlujoMesaControlDAOImpl extends AbstractManagerDAO implements FlujoMesaControlDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(FlujoMesaControlDAOImpl.class);
	
	// SP 1
	@Override
	public List<Map<String,String>> recuperaTtiptramc() throws Exception
	{
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTtiptramcSP(getDataSource()),new HashMap<String,String>());
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTtiptramcSP extends StoredProcedure
	{
		protected RecuperaTtiptramcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TTIPTRAMC");
			String[] cols=new String[]{ "CDTIPTRA" , "DSTIPTRA" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	// SP 2
	@Override
	public List<Map<String,String>> recuperaTtipflumc(String agrupamc, String cdtipmod) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("agrupamc" , agrupamc);
		params.put("cdtipmod" , cdtipmod);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTtipflumcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTtipflumcSP extends StoredProcedure
	{
		protected RecuperaTtipflumcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TTIPFLUMC");
			declareParameter(new SqlParameter("agrupamc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipmod" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU" , "DSTIPFLU", "CDTIPTRA","SWMULTIPOL","SWREQPOL","CDTIPSUP", "CDTIPMOD", "SWEXTERNO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 3
	@Override
	public List<Map<String,String>> recuperaTestadomc(String cdestadomc) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdestadomc" , cdestadomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTestadomcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTestadomcSP extends StoredProcedure
	{
		protected RecuperaTestadomcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TESTADOMC");
			declareParameter(new SqlParameter("cdestadomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDESTADOMC" , "DSESTADOMC" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 4
	@Override
	public List<Map<String,String>> recuperaTpantamc(String cdpantmc) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdpantmc" , cdpantmc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTpantamcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTpantamcSP extends StoredProcedure
	{
		protected RecuperaTpantamcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TPANTMC");
			declareParameter(new SqlParameter("cdpantmc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDPANTMC" , "DSPANTMC" , "URLPANTMC" , "SWEXTERNA"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	//SP 5
	@Override
	public List<Map<String,String>> recuperaTcompmc(String cdcompmc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdcompmc" , cdcompmc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTcompmcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTcompmcSP extends StoredProcedure
	{
		protected RecuperaTcompmcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TCOMPMC");
			declareParameter(new SqlParameter("cdcompmc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{"CDCOMPMC","DSCOMPMC","NOMCOMP"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	//SP 6
	@Override
	public List<Map<String,String>> recuperaTprocmc(String cdprocmc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdprocmc" , cdprocmc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTprocmcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTprocmcSP extends StoredProcedure
	{
		protected RecuperaTprocmcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TPROCMC");
			declareParameter(new SqlParameter("cdprocmc", OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDPROCMC" , "DSPROCMC", "URLPROCMC" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 7
	@Override
	public List<Map<String,String>> recuperaTdocume() throws Exception {
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTdocumeSP(getDataSource()),new HashMap<String,String>());
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null) {
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTdocumeSP extends StoredProcedure {
		protected RecuperaTdocumeSP(DataSource dataSource) {
			super(dataSource,"PKG_MESACONTROL.P_GET_TDOCUME");
			String[] cols=new String[]{ "CDDOCUME","DSDOCUME","CDTIPTRA","DSTIPTRA" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 7
	@Override
	public List<Map<String,String>> recuperaTrequisi() throws Exception {
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTrequisiSP(getDataSource()),new HashMap<String,String>());
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null) {
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTrequisiSP extends StoredProcedure {
		protected RecuperaTrequisiSP(DataSource dataSource) {
			super(dataSource,"PKG_MESACONTROL.P_GET_TREQUISI");
			String[] cols=new String[]{ "CDREQUISI","DSREQUISI","CDTIPTRA","DSTIPTRA","SWPIDEDATO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 8
	@Override
	public List<Map<String,String>> recuperaTiconos() throws Exception
	{
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTiconosSP(getDataSource()),new HashMap<String,String>());
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTiconosSP extends StoredProcedure
	{
		protected RecuperaTiconosSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TICONOS");
			String[] cols=new String[]{ "CDICONO" , "DSICONO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 9
	@Override
	public List<Map<String,String>> recuperaTflujomc(String cdtipflu, String swfinal) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		params.put("swfinal"  , swfinal);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflujomcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflujomcSP extends StoredProcedure
	{
		protected RecuperaTflujomcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUJOMC");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swfinal"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","DSFLUJOMC","SWFINAL","CDTIPRAM", "SWGRUPO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 10
	@Override
	public List<Map<String, String>> recuperaTfluest(String cdtipflu, String cdflujomc, String cdestadomc) throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"  , cdflujomc);
		params.put("cdestadomc" , cdestadomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluestSP(getDataSource()), params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluestSP extends StoredProcedure
	{
		protected RecuperaTfluestSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUEST");
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestadomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDTIPFLU"   , "CDFLUJOMC" , "CDESTADOMC"
					,"WEBID"     , "XPOS"      , "YPOS"
					,"TIMEMAX"   , "TIMEWRN1"  , "TIMEWRN2"
					,"CDTIPASIG" , "DSESTADOMC" , "STATUSOUT"
					,"SWFINNODE" , "CDETAPA"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 11
	@Override
	public List<Map<String,String>> recuperaTfluestrol(String cdtipflu, String cdflujomc, String cdestadomc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdestadomc" , cdestadomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluestrolSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluestrolSP extends StoredProcedure
	{
		protected RecuperaTfluestrolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUESTROL");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestadomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDESTADOMC", "CDSISROL", "SWVER", "SWTRABAJO", 
										"SWCOMPRA", "SWREASIG", "CDROLASIG", "SWVERDEF" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 12
	@Override
	public List<Map<String,String>> recuperaTfluestavi(String cdtipflu, String cdflujomc, String cdestadomc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		
		params.put("cdtipflu", cdtipflu);
		params.put("cdflujomc", cdflujomc);
		params.put("cdestadomc", cdestadomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluestaviSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluestaviSP extends StoredProcedure
	{
		protected RecuperaTfluestaviSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUESTAVI");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestadomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDESTADOMC","CDAVISO","CDTIPAVI","DSCOMENT","SWAUTOAVI","DSMAILAVI","CDUSUARIAVI","CDSISROLAVI"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 13
	@Override
	public List<Map<String,String>> recuperaTflupant(String cdtipflu,String cdflujomc,String cdpantmc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
	    params.put("cdflujomc" , cdflujomc);
	    params.put("cdpantmc"  , cdpantmc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflupantSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflupantSP extends StoredProcedure
	{
		protected RecuperaTflupantSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUPANT");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdpantmc"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDPANTMC","WEBID","XPOS","YPOS", "DSPANTMC" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 14
	@Override
	public List<Map<String,String>> recuperaTflucomp(String cdtipflu, String cdflujomc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflucompSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflucompSP extends StoredProcedure
	{
		protected RecuperaTflucompSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUCOMP");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDCOMPMC","WEBID","XPOS","YPOS","DSCOMPMC" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 15
	@Override
	public List<Map<String,String>> recuperaTfluproc(String cdtipflu, String cdflujomc) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu", cdtipflu);
		params.put("cdflujomc", cdflujomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluprocSP(getDataSource()), params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null) {
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluprocSP extends StoredProcedure {
		protected RecuperaTfluprocSP(DataSource dataSource) {
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUPROC");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc", OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDPROCMC","WEBID","XPOS","YPOS","DSPROCMC" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 16
	@Override
	public List<Map<String, String>> recuperaTfluval(String cdtipflu, String cdflujomc, String cdvalida) throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdvalida"  , cdvalida);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluvalSP(getDataSource()), params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluvalSP extends StoredProcedure
	{
		protected RecuperaTfluvalSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUVAL");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdvalida"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU", "CDFLUJOMC", "CDVALIDA", "DSVALIDA", "CDVALIDAFK", "WEBID", "XPOS", "YPOS", "JSVALIDA"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 17
	@Override
	public List<Map<String,String>> recuperaTflurev(String cdtipflu, String cdflujomc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflurevSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflurevSP extends StoredProcedure
	{
		protected RecuperaTflurevSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUREV");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDREVISI", "DSREVISI", "WEBID", "XPOS", "YPOS" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 18
	@Override
	public List<Map<String,String>> recuperaTflurevdoc(String cdtipflu, String cdflujomc, String cdrevisi) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		
		params.put("cdtipflu",cdtipflu);
		params.put("cdflujomc",cdflujomc);
		params.put("cdrevisi",cdrevisi);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflurevdocSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflurevdocSP extends StoredProcedure
	{
		protected RecuperaTflurevdocSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUREVDOC");
			declareParameter(new SqlParameter("cdtipflu", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrevisi", OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDREVISI","CDDOCUME","SWOBLIGA", "SWLISTA"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 19
	@Override
	public List<Map<String,String>> recuperaTfluacc(String cdtipflu,String cdflujomc) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
	    params.put("cdflujomc" , cdflujomc);

		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluaccSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluaccSP extends StoredProcedure
	{
		protected RecuperaTfluaccSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUACC");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDACCION","DSACCION","CDICONO","CDVALOR","IDORIGEN","IDDESTIN","SWESCALA","AUX"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 20
	@Override
	public List<Map<String,String>> recuperaTfluaccrol(String cdtipflu, String cdflujomc, String cdaccion) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdaccion" , cdaccion);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTfluaccrolSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTfluaccrolSP extends StoredProcedure
	{
		protected RecuperaTfluaccrolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUACCROL");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdaccion" , OracleTypes.VARCHAR));
			
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDACCION","CDSISROL","SWPERMISO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 21
	@Override
	public String movimientoTtipflumc(
			String cdtipflu
			,String dstipflu
			,String cdtiptra
			,String swmultipol
			,String swreqpol
			,String cdtipsup
			,String cdtipmod
            ,String swexterno
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("dstipflu"   , dstipflu);
		params.put("cdtiptra"   , cdtiptra);
		params.put("swmultipol" , swmultipol);
		params.put("swreqpol"   , swreqpol);
		params.put("cdtipsup"   , cdtipsup);
		params.put("cdtipmod"   , cdtipmod);
        params.put("swexterno"  , swexterno);
		params.put("accion"     , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoTtipflumcSP(getDataSource()),params);
		
		String cdtipfluSalida = cdtipflu;
		
		if("I".equals(accion))
		{
			cdtipfluSalida = (String)procRes.get("pv_cdtipflu_o");
			logger.debug("PKG_MESACONTROL.P_MOV_TTIPFLUMC cdtipflu generado '{}'",cdtipfluSalida);
		}
		
		return cdtipfluSalida;
	}
	
	protected class MovimientoTtipflumcSP extends StoredProcedure
	{
		protected MovimientoTtipflumcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TTIPFLUMC");
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dstipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtiptra"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swmultipol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swreqpol"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsup"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipmod"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swexterno"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipflu_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 22
	@Override
	public String movimientoTflujomc(
			String cdtipflu
			,String cdflujomc
			,String dsflujomc
			,String swfinal
			,String cdtipram
			,String swgrupo
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("dsflujomc" , dsflujomc);
		params.put("swfinal"   , swfinal);
		params.put("cdtipram"  , cdtipram);
		params.put("swgrupo"   , swgrupo);
		params.put("accion"    , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoTflujomcSP(getDataSource()),params);
		
		String cdflujomcSalida = cdflujomc;
		
		if("I".equals(accion))
		{
			cdflujomcSalida = (String)procRes.get("pv_cdtipflu_o");
			logger.debug("PKG_MESACONTROL.P_MOV_TFLUJOMC genera el flujo '{}'",cdflujomcSalida);
		}
		
		return cdflujomcSalida;
	}
	
	protected class MovimientoTflujomcSP extends StoredProcedure
	{
		protected MovimientoTflujomcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUJOMC");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swfinal"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipram"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swgrupo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdtipflu_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 23
	@Override
	public void movimientoTestadomc(String accion, String	cdestadomc, String dsestadomc) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdestadomc",cdestadomc);
		params.put("dsestadomc",dsestadomc);
		params.put("accion",accion);
		ejecutaSP(new MovimientoTestadomcSP(getDataSource()),params);
	}
	
	protected class MovimientoTestadomcSP extends StoredProcedure
	{
		protected MovimientoTestadomcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TESTADOMC");
			declareParameter(new SqlParameter("cdestadomc", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsestadomc", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 24
	@Override
	public void movimientoTfluest(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			,String webid
			,String xpos
			,String ypos
			,String timemax
			,String timewrn1
			,String timewrn2
			,String cdtipasig
			,String statusout
			,String swfinnode
			,String cdetapa
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"  , cdflujomc);
		params.put("cdestadomc" , cdestadomc);
		params.put("webid"      , webid);
		params.put("xpos"       , xpos);
		params.put("ypos"       , ypos);
		params.put("timemax"    , timemax);
		params.put("timewrn1"   , timewrn1);
		params.put("timewrn2"   , timewrn2);
		params.put("cdtipasig"  , cdtipasig);
		params.put("statusout"  , statusout);
		params.put("swfinnode"  , swfinnode);
		params.put("cdetapa"    , cdetapa);
		params.put("accion"     , accion);
		ejecutaSP(new MovimientoTfluestSP(getDataSource()),params);
	}
	
	protected class MovimientoTfluestSP extends StoredProcedure
	{
		protected MovimientoTfluestSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUEST");
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestadomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("xpos"       , OracleTypes.VARCHAR));		
			declareParameter(new SqlParameter("ypos"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("timemax"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("timewrn1"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("timewrn2"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipasig"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("statusout"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swfinnode"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdetapa"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 25
	@Override
	public void movimientoTfluestrol(
			String cdtipflu
			,String cdflujomc
			,String cdestadomc
			,String cdsisrol
			,String swver
			,String swtrabajo
			,String swcompra
			,String swreasig
			,String cdrolasig
			,String swverdef
			,String accion
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"  , cdflujomc);
		params.put("cdestadomc" , cdestadomc);
		params.put("cdsisrol"   , cdsisrol);
		params.put("swver"      , swver);
		params.put("swtrabajo"  , swtrabajo);
		params.put("swcompra"   , swcompra);
		params.put("swreasig"   , swreasig);
		params.put("cdrolasig"  , cdrolasig);
		params.put("swverdef"   , swverdef);
		params.put("accion"     , accion);
		
		ejecutaSP(new MovimientoTfluestrolSP(getDataSource()),params);
	}
	
	protected class MovimientoTfluestrolSP extends StoredProcedure
	{
		protected MovimientoTfluestrolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUESTROL");
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestadomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swcompra"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swreasig"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrolasig"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swver"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swtrabajo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swverdef"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 26
	@Override
	public void movimientoTfluestavi(
			String cdtipflu,
			String cdflujomc,
			String cdestadomc,
			String cdaviso,
			String cdtipavi,
			String dscoment,
			String swautoavi,
			String dsmailavi,
			String cdusuariavi,
			String cdsisrolavi,
			String accion
			)throws Exception {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"    , cdtipflu);
		params.put("cdflujomc"   , cdflujomc);
		params.put("cdestadomc"  , cdestadomc);
		params.put("cdaviso"     , cdaviso);
		params.put("cdtipavi"    , cdtipavi);
		params.put("dscoment"    , dscoment);
		params.put("swautoavi"   , swautoavi);
		params.put("dsmailavi"   , dsmailavi);
		params.put("cdusuariavi" , cdusuariavi);
		params.put("cdsisrolavi" , cdsisrolavi);
		params.put("accion"      , accion);
		ejecutaSP(new MovimientoTfluestaviSP(getDataSource()),params);
	}
	
	protected class MovimientoTfluestaviSP extends StoredProcedure {
		protected MovimientoTfluestaviSP(DataSource dataSource) {
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUESTAVI");
			declareParameter(new SqlParameter("cdtipflu"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdestadomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdaviso"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipavi"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dscoment"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swautoavi"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsmailavi"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuariavi" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrolavi" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 27
	@Override
	public void movimientoTpantmc (String cdpantmc, String dspantmc, 
			String urlpantmc, String swexterna, String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdpantmc"   , cdpantmc);
		params.put("dspantmc"   , dspantmc);
		params.put("urlpantmc"   , urlpantmc);
		params.put("swexterna" , swexterna);
		params.put("accion"     , accion);
		ejecutaSP(new MovimientoTpantmcSP(getDataSource()),params);
	}
	
	protected class MovimientoTpantmcSP extends StoredProcedure
	{
		protected MovimientoTpantmcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TPANTMC");
			declareParameter(new SqlParameter("cdpantmc"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dspantmc"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("urlpantmc"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swexterna" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 28
	@Override
	public void movimientoTflupant(
			String cdtipflu
			,String cdflujomc
			,String cdpantmc
			,String webid
			,String xpos
			,String ypos
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdpantmc" , cdpantmc);
		params.put("webid"   , webid);
		params.put("xpos"   , xpos);
		params.put("ypos"   , ypos);
		params.put("accion"     , accion);
		ejecutaSP(new MovimientoTflupantSP(getDataSource()),params);
	}
	
	protected class MovimientoTflupantSP extends StoredProcedure
	{
		protected MovimientoTflupantSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUPANT");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdpantmc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("xpos"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ypos"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 29
	@Override
	public void movimientoTcompmc(String accion, String cdcompmc, String dscompmc, String	nomcomp) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdcompmc",cdcompmc);
		params.put("dscompmc",dscompmc);
		params.put("nomcomp",nomcomp);
		params.put("accion",accion);
		ejecutaSP(new MovimientoTcompmcSP(getDataSource()),params);
	}
	
	protected class MovimientoTcompmcSP extends StoredProcedure
	{
		protected MovimientoTcompmcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TCOMPMC");
			declareParameter(new SqlParameter("cdcompmc", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dscompmc", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nomcomp", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 30
	@Override
	public void movimientoTflucomp(
			String cdtipflu
			,String cdflujomc
			,String cdcompmc
			,String webid
			,String xpos
			,String ypos
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdcompmc"  , cdcompmc);
		params.put("webid"     , webid);
		params.put("xpos"      , xpos);
		params.put("ypos"      , ypos);
		params.put("accion"    , accion);
		ejecutaSP(new MovimientoTflucompSP(getDataSource()),params);
	}
	
	protected class MovimientoTflucompSP extends StoredProcedure
	{
		protected MovimientoTflucompSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUCOMP");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdcompmc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("xpos"      , OracleTypes.VARCHAR));		
			declareParameter(new SqlParameter("ypos"      , OracleTypes.VARCHAR));	
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 31
	@Override
	public void movimientoTprocmc(String cdprocmc,String dsprocmc,String urlprocmc,String accion) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdprocmc"   , cdprocmc);
		params.put("dsprocmc"   , dsprocmc);
		params.put("urlprocmc"   , urlprocmc);
		params.put("accion"     , accion);
		
		ejecutaSP(new MovimientoTprocmcSP(getDataSource()),params);
	}
	
	protected class MovimientoTprocmcSP extends StoredProcedure
	{
		protected MovimientoTprocmcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TPROCMC");
			declareParameter(new SqlParameter("cdprocmc"     , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("dsprocmc"     , OracleTypes.VARCHAR));		
			declareParameter(new SqlParameter("urlprocmc"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 32
	@Override
	public void movimientoTfluproc(
			String cdtipflu,
			String cdflujomc,
			String cdprocmc,
			String webid,
			String xpos,
			String ypos,
			String accion) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdprocmc"  , cdprocmc);
		params.put("webid"     , webid);
		params.put("xpos"      , xpos);
		params.put("ypos"      , ypos);
		params.put("accion"    , accion);
		ejecutaSP(new MovimientoTfluprocSP(getDataSource()),params);
	}
	
	protected class MovimientoTfluprocSP extends StoredProcedure
	{
		protected MovimientoTfluprocSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUPROC");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdprocmc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("xpos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ypos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 33
	@Override
	public String movimientoTfluval(String cdtipflu, String cdflujomc,
			String cdvalida, String dsvalida, String cdvalidafk, String webid,
			String xpos, String ypos, String jsvalida, String accion) throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"  , cdflujomc);
		params.put("cdvalida"   , cdvalida);
		params.put("dsvalida"   , dsvalida);
		params.put("cdvalidafk" , cdvalidafk);
		params.put("webid"      , webid);
		params.put("xpos"       , xpos);
		params.put("ypos"       , ypos);
		params.put("jsvalida"   , jsvalida);
		params.put("accion"     , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoTfluvalSP(getDataSource()),params);
		return (String)procRes.get("cdvalida");
	}
	
	protected class MovimientoTfluvalSP extends StoredProcedure
	{
		protected MovimientoTfluvalSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUVAL");
			declareParameter(new SqlParameter     ("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdflujomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlInOutParameter("cdvalida"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsvalida"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdvalidafk" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("webid"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("xpos"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("ypos"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("jsvalida"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 34
	@Override
	public void movimientoTdocume(
			String cddocume
			,String dsdocume
			,String cdtiptra
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cddocume" , cddocume);
		params.put("dsdocume" , dsdocume);
		params.put("cdtiptra" , cdtiptra);
		params.put("accion"   , accion);
		ejecutaSP(new MovimientoTdocumeSP(getDataSource()),params);
	}
	
	protected class MovimientoTdocumeSP extends StoredProcedure
	{
		protected MovimientoTdocumeSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TDOCUME");
			declareParameter(new SqlParameter("cddocume"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsdocume" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtiptra" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 35
	@Override
	public String movimientoTflurev(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			,String dsrevisi
			,String webid
			,String xpos
			,String ypos
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdrevisi"  , cdrevisi);
		params.put("dsrevisi"  , dsrevisi);
		params.put("webid"     , webid);
		params.put("xpos"      , xpos);
		params.put("ypos"      , ypos);
		params.put("accion"    , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoTflurevSP(getDataSource()),params);
		return (String)procRes.get("cdrevisi");
	}
	
	protected class MovimientoTflurevSP extends StoredProcedure
	{
		protected MovimientoTflurevSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUREV");
			declareParameter(new SqlParameter     ("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlInOutParameter("cdrevisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsrevisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("webid"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("xpos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("ypos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 36
	@Override
	public void movimientoTflurevdoc(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			,String cddocume
			,String swobliga
			,String swlista
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdrevisi"  , cdrevisi);
		params.put("cddocume"  , cddocume);
		params.put("swobliga"  , swobliga);
		params.put("swlista"   , swlista);
		params.put("accion"    , accion);
		ejecutaSP(new MovimientoTflurevdocSP(getDataSource()),params);
	}
	
	protected class MovimientoTflurevdocSP extends StoredProcedure
	{
		protected MovimientoTflurevdocSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUREVDOC");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrevisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cddocume"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swobliga"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swlista"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 37
	@Override
	public void actualizaIcono(String cdicono,String accion) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdicono"   , cdicono);
		params.put("accion"     , accion);
		
		ejecutaSP(new ActualizaIconoSP(getDataSource()),params);
	}
	
	protected class ActualizaIconoSP extends StoredProcedure
	{
		protected ActualizaIconoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_UPT_ICONO");
			declareParameter(new SqlParameter("cdicono"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 38
	@Override
	public String movimientoTfluacc(
			String cdtipflu,
			String cdflujomc,
			String cdaccion,
			String dsaccion,
			String cdicono,
			String cdvalor,
			String idorigen,
			String iddestin,
			String swescala,
			String aux,
			String accion) throws Exception {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdaccion"  , cdaccion);
		params.put("dsaccion"  , dsaccion);
		params.put("cdicono"   , cdicono);
		params.put("cdvalor"   , cdvalor);
		params.put("idorigen"  , idorigen);
		params.put("iddestin"  , iddestin);
		params.put("swescala"  , "S".equals(swescala) ? "S" : "N");
		params.put("aux"       , aux);
		params.put("accion"    , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoTfluaccSP(getDataSource()),params);
		return (String)procRes.get("cdaccion");
	}
	
	protected class MovimientoTfluaccSP extends StoredProcedure {
		protected MovimientoTfluaccSP(DataSource dataSource) {
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUACC");
			declareParameter(new SqlParameter     ("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlInOutParameter("cdaccion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsaccion"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdicono"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdvalor"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("idorigen"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("iddestin"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("swescala"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("aux"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 39
	@Override
	public void movimientoTfluaccrol(String cdtipflu, String cdflujomc,
			String cdaccion, String cdsisrol, String swpermiso, String accion)
			throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"   , cdflujomc);
		params.put("cdaccion"   , cdaccion);
		params.put("cdsisrol"   , cdsisrol);
		params.put("swpermiso"   , swpermiso);
		params.put("accion"     , accion);
		ejecutaSP(new MovimientoTfluaccrolSP(getDataSource()),params);
	}
	
	protected class MovimientoTfluaccrolSP extends StoredProcedure
	{
		protected MovimientoTfluaccrolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUACCROL");
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdaccion"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swpermiso" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String ejecutaExpresion(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdexpres
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("cdramo"   , cdramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		params.put("nmsituac" , nmsituac);
		params.put("nmsuplem" , nmsuplem);
		params.put("cdexpres" , cdexpres);
		return (String)ejecutaSP(new EjecutaExpresionSP(getDataSource()),params).get("pv_result_o");
	}
	
	protected class EjecutaExpresionSP extends StoredProcedure
	{
		protected EjecutaExpresionSP(DataSource dataSource)
		{
			super(dataSource,"P_EXEC_EXPRESION");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdexpres" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_result_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizaCoordenadas(
			String cdtipflu
			,String cdflujomc
			,String tipo
			,String clave
			,String webid
			,String xpos
			,String ypos
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("tipo"      , tipo);
		params.put("clave"     , clave);
		params.put("webid"     , webid);
		params.put("xpos"      , xpos);
		params.put("ypos"      , ypos);
		ejecutaSP(new ActualizaCoordenadasSP(getDataSource()),params);
	}
	
	protected class ActualizaCoordenadasSP extends StoredProcedure
	{
		protected ActualizaCoordenadasSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_ACTUALIZA_COORDS");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipo"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clave"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("xpos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ypos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarTestadomcPorAgrupamc(String agrupamc) throws Exception
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("agrupamc" , agrupamc);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarTestadomcPorAgrupamcSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarTestadomcPorAgrupamcSP extends StoredProcedure
	{
		protected RecuperarTestadomcPorAgrupamcSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TESTADOMC_X_AGRUPAMC");
			declareParameter(new SqlParameter("agrupamc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDESTADOMC" , "DSESTADOMC" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,Object> recuperarTramites(
			String agrupamc
			,String status
			,String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String cdtipsit
			,String estado
			,String nmpoliza
			,String cdagente
			,String ntramite
			,String fedesde
			,String fehasta
			,String cdpersonCliente
			,String dscontra
			,int start
			,int limit
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("agrupamc"        , agrupamc);
		params.put("status"          , status);
		params.put("cdusuari"        , cdusuari);
		params.put("cdsisrol"        , cdsisrol);
		params.put("cdunieco"        , cdunieco);
		params.put("cdramo"          , cdramo);
		params.put("cdtipsit"        , cdtipsit);
		params.put("estado"          , estado);
		params.put("nmpoliza"        , nmpoliza);
		params.put("cdagente"        , cdagente);
		params.put("ntramite"        , ntramite);
		params.put("fedesde"         , fedesde);
		params.put("fehasta"         , fehasta);
//		params.put("cdpersonCliente" , cdpersonCliente);
		params.put("dscontra"        , dscontra);
		params.put("start"           , start);
		params.put("limit"           , limit);
		Map<String,Object> procRes = ejecutaSP(new RecuperarTramitesSP(getDataSource()),params);
		Map<String,Object> result  = new HashMap<String,Object>();
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		Integer total = 0;
		try
		{
			total = Integer.parseInt((String)procRes.get("pv_total_o"));
		}
		catch(Exception ex)
		{
			total = 0;
		}
		String flag = null;
		for (Map<String, String> tra : lista) {
			flag = tra.get("FLAG");
			String ordenFlag = "5";
			if ("X".equals(flag)) {
				ordenFlag = "1";
			} else if ("R".equals(flag)) {
				ordenFlag = "2";
			} else if ("A".equals(flag)) {
				ordenFlag = "3";
			} else if ("V".equals(flag)) {
				ordenFlag = "4";
			}
			tra.put("ORDENFLAG", ordenFlag);
		}
		logger.debug(Utils.log("\n******lista=",lista,"\n******total=",total));
		result.put("lista" , lista);
		result.put("total" , total);
		return result;
	}
	
	protected class RecuperarTramitesSP extends StoredProcedure
	{
		protected RecuperarTramitesSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TRAMITES");
//		    super(dataSource,"P_GET_TRAMITES1");
			declareParameter(new SqlParameter("agrupamc"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunieco"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipsit"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"          , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdagente"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fedesde"         , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fehasta"         , OracleTypes.VARCHAR));
			//declareParameter(new SqlParameter("cdpersonCliente" , OracleTypes.VARCHAR));			
			declareParameter(new SqlParameter("start"           , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("limit"           , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dscontra"        , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"NTRAMITE"    , "CDTIPFLU"           , "DSTIPFLU"    , "CDFLUJOMC" , "DSFLUJOMC"
					,"STATUS"     , "DSSTATUS"           , "CDUNIECO"    , "CDRAMO"    , "CDTIPSIT"
					,"DSTIPSIT"   , "ESTADO"             , "NMPOLIZA"    , "FECSTATU"  , "FERECEPC"
					,"NMSOLICI"   , "NOMBRE_CONTRATANTE" , "RESPONSABLE" , "RAMO"      , "DSTIPSUP"
					,"CDTIPRAM"   , "DSTIPRAM"           , "NMPOLIEX"    , "CDTIPTRA"  , "ULTIMO_MODIFICA"
					,"NRO_ENDOSO" , "CDUNIEXT"           , "CDSUCADM"    , "FLAG"      , "VENCIMIENTO"
					,"CDETAPA"    , "RENPOLIEX"          , "NTRASUST"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols,true)));
			declareParameter(new SqlOutParameter("pv_total_o"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarTtipsupl(String cdtiptra) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtiptra" , cdtiptra);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarTtipsupl(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperarTtipsupl extends StoredProcedure
	{
		protected RecuperarTtipsupl(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TTIPSUPL");
			declareParameter(new SqlParameter("cdtiptra" , OracleTypes.VARCHAR));
			String cols[]=new String[]{ "CDTIPSUP", "DSTIPSUP" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarPolizaUnica(
			String cdunieco
			,String ramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdunieco" , cdunieco);
		params.put("ramo"     , ramo);
		params.put("estado"   , estado);
		params.put("nmpoliza" , nmpoliza);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarPolizaUnicaSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new ApplicationException(Utils.join("No existe la p\u00f3liza para la sucursal ",cdunieco," y el ramo ",ramo));
		}
		else if(lista.size()>1)
		{
			throw new ApplicationException(Utils.join("P\u00f3liza duplicada para la sucursal ",cdunieco," y el ramo ",ramo));
		}
		return lista.get(0);
	}
	
	protected class RecuperarPolizaUnicaSP extends StoredProcedure
	{
		protected RecuperarPolizaUnicaSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_REVISA_POLIZA_PARA_TRAMITE");
			declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ramo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
			String[] cols = new String[] {
					"CDUNIECO",
					"CDRAMO",
					"FEEFECTO",
					"FEEMISIO",
					"CONTRATANTE",
					"AGENTE",
					"CDAGENTE",
					"CDTIPSIT",
					"STATUSPOL",
					"CDAGENTE"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	//TODO: NO SE ESTA USANDO
	@Override
	public String ejecutaValidacion(String ntramite, String cdunieco, String cdramo, String estado, String nmpoliza, String nmsituac,
	        String nmsuplem, String cdvalidafk, String cdusuari, String cdsisrol, String cdvalida) throws Exception {
		Map<String,String> params = new LinkedHashMap<String,String>();
		//params.put("status"     , status);//<<< NO SE USA
		params.put("cdunieco"   , cdunieco);
		params.put("cdramo"     , cdramo);
		params.put("estado"     , estado);
		params.put("nmpoliza"   , nmpoliza);
		params.put("nmsituac"   , nmsituac);
		params.put("nmsuplem"   , nmsuplem);
		params.put("cdvalidafk" , cdvalidafk);
		params.put("ntramite"   , ntramite);
        params.put("cdusuari"   , cdusuari);
        params.put("cdsisrol"   , cdsisrol);
        params.put("cdvalida"   , cdvalida);
		
		Map<String,Object> procRes = ejecutaSP(new EjecutaValidacionSP(getDataSource()),params);
		
		String error = (String)procRes.get("pv_mensaje_o");
		if(StringUtils.isNotBlank(error))
		{
			throw new ApplicationException(error);
		}
		
		return (String)procRes.get("pv_result_o");
	}
	
	protected class EjecutaValidacionSP extends StoredProcedure
	{
		protected EjecutaValidacionSP(DataSource dataSource)
		{
			super(dataSource,"P_EXEC_VALIDACION");
			declareParameter(new SqlParameter("cdunieco"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdramo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estado"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliza"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsituac"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmsuplem"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdvalidafk" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdvalida"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_result_o"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_mensaje_o" , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>>cargarAccionesEntidad(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String cdentidad
			,String webid
			,String cdsisrol
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("tipoent"   , tipoent);
		params.put("cdentidad" , cdentidad);
		params.put("webid"     , webid);
		params.put("cdsisrol"  , cdsisrol);
		Map<String,Object>       procRes = ejecutaSP(new CargarAccionesEntidadSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class CargarAccionesEntidadSP extends StoredProcedure
	{
		protected CargarAccionesEntidadSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUACC_X_ENTIDAD");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoent"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdentidad" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"  , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"CDTIPFLU"  , "CDFLUJOMC"  , "CDACCION" , "DSACCION"
					,"CDICONO"  , "CDVALOR"    , "IDORIGEN" , "IDDESTIN", "AUX"
					,"CDESTADOMC" , "WEBIDESTADO"
					,"CDPANTMC"   , "WEBIDPANT"
					,"CDCOMPMC"   , "WEBIDCOMP"
					,"CDPROCMC"   , "WEBIDPROC"
					,"CDVALIDA"   , "WEBIDVALIDA"
					,"CDREVISI"   , "WEBIDREVISI"
					,"CDMAIL"     , "WEBIDMAIL"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String, Object> recuperarDocumentosRevisionFaltantes(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			,String ntramite
			,String cduser
			,String cdrol
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdrevisi"  , cdrevisi);
		params.put("ntramite"  , ntramite);
		params.put("cduser"    , cduser);
		params.put("cdrol"     , cdrol);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarDocumentosRevisionFaltantesSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		String swconfirm = (String) procRes.get("pv_swconfirm_o");
		if (StringUtils.isBlank(swconfirm)) {
			throw new ApplicationException("No hay estado de confirmaci\u00f3n para revisi\u00f3n");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("lista", lista);
		result.put("swconfirm" , swconfirm);
		return result;
	}
	
	protected class RecuperarDocumentosRevisionFaltantesSP extends StoredProcedure
	{
		protected RecuperarDocumentosRevisionFaltantesSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_DOCS_FALTAN_REVISI");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrevisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cduser"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrol"  , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"TIPO", "CLAVE" , "DESCRIP", "SWLISTA", "SWOBLIGA", "SWACTIVO", "SWPIDEDATO", "DSDATO"
			};
			declareParameter(new SqlOutParameter("pv_swconfirm_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o"  , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void actualizarStatusTramite(
			String ntramite
			,String status
			,Date fecstatu
			,String cdusuari
			,String cdunidspch
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("ntramite"   , ntramite);
		params.put("status"     , status);
		params.put("fecstatu"   , fecstatu);
		params.put("cdusuari"   , cdusuari);
        params.put("cdunidspch" , cdunidspch);
		ejecutaSP(new ActualizarStatusTramiteSP(getDataSource()),params);
	}
	
	protected class ActualizarStatusTramiteSP extends StoredProcedure
	{
		protected ActualizarStatusTramiteSP(DataSource dataSource)
		{
			super(dataSource,"P_DSPCH_ACT_STATUS_TRAMITE");
			declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fecstatu"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("cdusuari"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdunidspch" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,Object> recuperarDatosTramiteValidacionCliente(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String claveent
			,String webid
			,String ntramite
			,String status
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("CDTIPFLU"  , cdtipflu);
		params.put("CDFLUJOMC" , cdflujomc);
		params.put("TIPOENT"   , tipoent);
		params.put("CLAVEENT"  , claveent);
		params.put("WEBID"     , webid);
		params.put("NTRAMITE"  , ntramite);
		params.put("STATUS"    , status);
		params.put("CDUNIECO"  , cdunieco);
		params.put("CDRAMO"    , cdramo);
		params.put("ESTADO"    , estado);
		params.put("NMPOLIZA"  , nmpoliza);
		params.put("NMSITUAC"  , nmsituac);
		params.put("NMSUPLEM"  , nmsuplem);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarDatosTramiteValidacionClienteSP(getDataSource()),params);
		List<Map<String,String>> tramite = (List<Map<String,String>>)procRes.get("TRAMITE");
		if(tramite==null||tramite.size()==0)
		{
			throw new ApplicationException("No hay tr\u00e1mite");
		}
		if(tramite.size()>1)
		{
			throw new ApplicationException("Tr\u00e1mite duplicado");
		}
		procRes.put("TRAMITE" , tramite.get(0));
		procRes.putAll(params);
		return procRes;
	}
	
	protected class RecuperarDatosTramiteValidacionClienteSP extends StoredProcedure
	{
		protected RecuperarDatosTramiteValidacionClienteSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_DATOS_VALIDACION_JS");
			declareParameter(new SqlParameter("CDTIPFLU"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("CDFLUJOMC"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("TIPOENT"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("CLAVEENT"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("WEBID"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("NTRAMITE"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("STATUS"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("CDUNIECO"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("CDRAMO"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ESTADO"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("NMPOLIZA"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("NMSITUAC"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("NMSUPLEM"   , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NTRAMITE"  ,"CDUNIECO" ,"CDRAMO"   ,"ESTADO"   ,"NMPOLIZA" ,"NMSUPLEM"  ,"NMSOLICI" ,"CDSUCADM"
					,"CDSUCDOC" ,"CDSUBRAM" ,"CDTIPTRA" ,"FERECEPC" ,"CDAGENTE" ,"REFERENCIA","NOMBRE"   ,"FECSTATU"
					,"STATUS"   ,"COMMENTS" ,"CDTIPSIT"
					,"OTVALOR01","OTVALOR02","OTVALOR03","OTVALOR04","OTVALOR05","OTVALOR06" ,"OTVALOR07","OTVALOR08","OTVALOR09","OTVALOR10"
					,"OTVALOR11","OTVALOR12","OTVALOR13","OTVALOR14","OTVALOR15","OTVALOR16" ,"OTVALOR17","OTVALOR18","OTVALOR19","OTVALOR20"
					,"OTVALOR21","OTVALOR22","OTVALOR23","OTVALOR24","OTVALOR25","OTVALOR26" ,"OTVALOR27","OTVALOR28","OTVALOR29","OTVALOR30"
					,"OTVALOR31","OTVALOR32","OTVALOR33","OTVALOR34","OTVALOR35","OTVALOR36" ,"OTVALOR37","OTVALOR38","OTVALOR39","OTVALOR40"
					,"OTVALOR41","OTVALOR42","OTVALOR43","OTVALOR44","OTVALOR45","OTVALOR46" ,"OTVALOR47","OTVALOR48","OTVALOR49","OTVALOR50"
					,"SWIMPRES" ,"CDTIPFLU" ,"CDFLUJOMC","CDUSUARI" ,"CDTIPSUP"
					,"RENUNIEXT" , "RENRAMO" , "RENPOLIEX"
			};
			declareParameter(new SqlOutParameter("TRAMITE" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarUsuarioParaTurnado(
			String cdsisrol
			,String tipoasig
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdsisrol" , cdsisrol);
		params.put("tipoasig" , tipoasig);
		Map<String,Object> procRes = ejecutaSP(new RecuperarUsuarioParaTurnadoSP(getDataSource()),params);
		String cdusuari = (String) procRes.get("pv_cdusuari_o");
		String dsusuari = (String) procRes.get("pv_dsusuari_o");
		Map<String,String> usuario = new HashMap<String,String>();
		usuario.put("cdusuari" , cdusuari);
		usuario.put("dsusuari" , dsusuari);
		return usuario;
	}
	
	protected class RecuperarUsuarioParaTurnadoSP extends StoredProcedure
	{
		protected RecuperarUsuarioParaTurnadoSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_USER_PARA_ASIG_TRAMITE");
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoasig" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdusuari_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_dsusuari_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String recuperarRolRecienteTramite(String ntramite, String cdusuari) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("cdusuari" , cdusuari);
		Map<String,Object> procRes = ejecutaSP(new RecuperarRolRecienteTramiteSP(getDataSource()),params);
		String cdsisrol = (String)procRes.get("pv_cdsisrol_o");
		if(StringUtils.isBlank(cdsisrol))
		{
			throw new ApplicationException("No hay rol de usuario origen");
		}
		return cdsisrol;
	}
	
	protected class RecuperarRolRecienteTramiteSP extends StoredProcedure
	{
		protected RecuperarRolRecienteTramiteSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_CDSISROL_MAX_DETALLE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdsisrol_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void restarTramiteUsuario(String cdusuari, String cdsisrol) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdusuari" , cdusuari);
		params.put("cdsisrol" , cdsisrol);
		ejecutaSP(new RestarTramiteUsuarioSP(getDataSource()),params);
	}
	
	protected class RestarTramiteUsuarioSP extends StoredProcedure
	{
		protected RestarTramiteUsuarioSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_RESTA_TAREA_USUARIO");
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarUsuarioHistoricoTramitePorRol(String ntramite, String cdsisrol) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		params.put("cdsisrol" , cdsisrol);
		Map<String,Object> procRes = ejecutaSP(new RecuperarUsuarioHistoricoTramitePorRolSP(getDataSource()),params);
		String cdusuari = (String) procRes.get("pv_cdusuari_o");
		String dsusuari = (String) procRes.get("pv_dsusuari_o");
		Map<String,String> usuario = new HashMap<String,String>();
		usuario.put("cdusuari" , cdusuari);
		usuario.put("dsusuari" , dsusuari);
		return usuario;
	}
	
	protected class RecuperarUsuarioHistoricoTramitePorRolSP extends StoredProcedure
	{
		protected RecuperarUsuarioHistoricoTramitePorRolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_USER_HIST_TRAM_X_ROL");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_cdusuari_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_dsusuari_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarHistoricoTramite(Date fecha, String ntramite, String cdusuari, String cdsisrol, String status,
			String cdunidspch, String cdtipasig) throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("fecha"      , fecha);
		params.put("ntramite"   , ntramite);
		params.put("cdusuari"   , cdusuari);
		params.put("cdsisrol"   , cdsisrol);
		params.put("status"     , status);
		params.put("cdunidspch" , cdunidspch);
		params.put("cdtipasig"  , cdtipasig);
		ejecutaSP(new GuardarHistoricoTramiteSP(getDataSource()),params);
	}
	
	protected class GuardarHistoricoTramiteSP extends StoredProcedure
	{
		protected GuardarHistoricoTramiteSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_INSERTA_THMESACONTROL");
			declareParameter(new SqlParameter("fecha"      , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdunidspch" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtipasig"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisos(
			String cdtipflu
			,String cdflujomc
			,String tipoent
			,String claveent
			,String webid
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("tipoent"   , tipoent);
		params.put("claveent"  , claveent);
		params.put("webid"     , webid);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisosSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		Map<String,String> conexionFantasma = null;
		
		if(lista!=null)
		{
			if(lista.size()>1)
			{
				throw new ApplicationException("Hay m\u00e1s de una conexi\u00f3n fantasma");
			}
			else if(lista.size()==1)
			{
				conexionFantasma = lista.get(0);
			}
		}
		
		return conexionFantasma;
	}
	
	protected class RecuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisosSP extends StoredProcedure
	{
		protected RecuperarPropiedadesDePantallaComponenteActualPorConexionSinPermisosSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_RECUPERA_CONEXION_FANTASMA");
			
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tipoent"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("claveent"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"      , OracleTypes.VARCHAR));
			
			String[] cols=new String[]{ "CDENTIDAD" , "TIPOENT" , "WEBID" };
			
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperaTtipflumcPorRolPorUsuario(String agrupamc,String cdsisrol,String cdusuari) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("agrupamc" , agrupamc);
		params.put("cdsisrol" , cdsisrol);
		params.put("cdusuari" , cdusuari);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTtipflumcPorRolPorUsuarioSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTtipflumcPorRolPorUsuarioSP extends StoredProcedure
	{
		protected RecuperaTtipflumcPorRolPorUsuarioSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TTIPFLUROL");
			declareParameter(new SqlParameter("agrupamc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU" , "DSTIPFLU", "CDTIPTRA","SWMULTIPOL","SWREQPOL","CDTIPSUP" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperaTflujomcPorRolPorUsuario(
			String cdtipflu
			,String swfinal
			,String cdsisrol
			,String cdusuari
			)throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		params.put("swfinal"  , swfinal);
		params.put("cdsisrol" , cdsisrol);
		params.put("cdusuari" , cdusuari);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflujomcPorRolPorUsuarioSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflujomcPorRolPorUsuarioSP extends StoredProcedure
	{
		protected RecuperaTflujomcPorRolPorUsuarioSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUJOROL");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swfinal"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","DSFLUJOMC","SWFINAL","CDTIPRAM", "SWGRUPO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperaTflujomc(String cdflujomc) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("cdflujomc" , cdflujomc);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperaTflujomcIndividualSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		if(lista==null)
		{
			throw new ApplicationException("No hay flujo");
		}
		
		if(lista.size()>1)
		{
			throw new ApplicationException("Flujo repetido");
		}
		
		return lista.get(0);
	}
	
	protected class RecuperaTflujomcIndividualSP extends StoredProcedure
	{
		protected RecuperaTflujomcIndividualSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUJOMC_IND");
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","DSFLUJOMC","SWFINAL","CDTIPRAM" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarPolizaUnicaDanios(
			String cduniext
			,String ramo
			,String nmpoliex
			)throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cduniext" , cduniext);
		params.put("ramo"     , ramo);
		params.put("nmpoliex" , nmpoliex);
		Map<String,Object>       procRes = ejecutaSP(new RecuperarPolizaUnicaDaniosSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
		    throw new ApplicationException("La p\u00f3liza no se encuentra registrada en SICAPS. Por favor generar el tr\u00E1mite con el tipo: RENOVACI\u00D3N NO SICAPS.");
		}
		else if(lista.size()>1)
		{
			throw new ApplicationException(Utils.join("P\u00f3liza duplicada para la sucursal ",cduniext," y el ramo ",ramo));
		}
		return lista.get(0);
	}
	
	protected class RecuperarPolizaUnicaDaniosSP extends StoredProcedure
	{
		protected RecuperarPolizaUnicaDaniosSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_REVISA_POLIZA_DANIOS_TRAMITE");
			declareParameter(new SqlParameter("cduniext" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ramo"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmpoliex" , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"CDUNIECO"
					,"CDRAMO"
					,"ESTADO"
					,"NMPOLIZA"
					,"FEEFECTO"
					,"FEEMISIO"
					,"CONTRATANTE"
					,"AGENTE"
					,"CDTIPSIT"
					,"STATUSPOL"
					,"CDAGENTE"
					,"CDTIPSIT"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarTtipflurol(String cdtipflu) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu" , cdtipflu);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarTtipflurolSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		if(lista == null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		
		logger.debug(Utils.log("lista ttipflurol=",lista));
		
		return lista;
	}
	
	protected class RecuperarTtipflurolSP extends StoredProcedure
	{
		protected RecuperarTtipflurolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TTIPFLUROL");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"CDTIPFLU"
					,"CDSISROL"
					,"SWACTIVO"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarTtipflurol(String cdtipflu, List<Map<String,String>> lista) throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdtipflu" , cdtipflu);
		
		String[][] array = new String[lista.size()][];
		
		int i = 0;
		for(Map<String,String> permiso : lista)
		{
			array[i++] = new String[]{
					permiso.get("CDTIPFLU")
					,permiso.get("CDSISROL")
					,permiso.get("SWACTIVO")
			};
		}
		
		params.put("array" , new SqlArrayValue(array));
		
		ejecutaSP(new GuardarTtipflurolSP(getDataSource()),params);
	}
	
	protected class GuardarTtipflurolSP extends StoredProcedure
	{
		protected GuardarTtipflurolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TTIPFLUROL");
			declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("array"    , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperarTflujorol(String cdtipflu, String cdflujomc) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarTflujorolSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>)procRes.get("pv_registro_o");
		
		if(lista == null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		
		logger.debug(Utils.log("lista tflujorol=",lista));
		
		return lista;
	}
	
	protected class RecuperarTflujorolSP extends StoredProcedure
	{
		protected RecuperarTflujorolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUJOROL");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"CDTIPFLU"
					,"CDFLUJOMC"
					,"CDSISROL"
					,"SWACTIVO"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarTflujorol(
			String cdtipflu
			,String cdflujomc
			,List<Map<String,String>> lista
			)throws Exception
	{
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		
		String[][] array = new String[lista.size()][];
		
		int i = 0;
		for(Map<String,String> permiso : lista)
		{
			array[i++] = new String[]{
					permiso.get("CDTIPFLU")
					,permiso.get("CDFLUJOMC")
					,permiso.get("CDSISROL")
					,permiso.get("SWACTIVO")
			};
		}
		
		params.put("array" , new SqlArrayValue(array));
		
		ejecutaSP(new GuardarTflujorolSP(getDataSource()),params);
	}
	
	protected class GuardarTflujorolSP extends StoredProcedure
	{
		protected GuardarTflujorolSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUJOROL");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("array"     , OracleTypes.ARRAY , "LISTA_LISTAS_VARCHAR2"));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String,String> recuperarFlujoPorDescripcion(String descripcion) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		
		params.put("descripcion" , descripcion);
		
		Map<String,Object> procRes = ejecutaSP(new RecuperarFlujoPorDescripcionSP(getDataSource()),params);
		
		List<Map<String,String>> lista = (List<Map<String,String>>) procRes.get("pv_registro_o");
		
		if(lista == null || lista.size() == 0)
		{
			throw new ApplicationException("No se encuentra el flujo de proceso");
		}
		
		if(lista.size()>1)
		{
			throw new ApplicationException("Flujo de proceso duplicado");
		}
		
		logger.debug(Utils.log("Proceso recuperado para la descripcion '",descripcion,"' = ",lista.get(0)));
		
		return lista.get(0);
	}
	
	protected class RecuperarFlujoPorDescripcionSP extends StoredProcedure
	{
		protected RecuperarFlujoPorDescripcionSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUJOMC_X_DESC");
			declareParameter(new SqlParameter("descripcion" , OracleTypes.VARCHAR));
			String cols[]=new String[]{
					"CDTIPFLU"
					,"CDFLUJOMC"
					,"DSFLUJOMC"
					,"SWFINAL"
					,"CDTIPRAM"
					,"SWGRUPO"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	
	// SP 35
	@Override
	public void movimientoTflutit(
			String cdtipflu
			,String cdflujomc
			,String cdtitulo
			,String dstitulo
			,String webid
			,String xpos
			,String ypos
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdtitulo"  , cdtitulo);
		params.put("dstitulo"  , dstitulo);
		params.put("webid"     , webid);
		params.put("xpos"      , xpos);
		params.put("ypos"      , ypos);
		params.put("accion"    , accion);
		ejecutaSP(new MovimientoTflutitSP(getDataSource()),params);
	}
	
	protected class MovimientoTflutitSP extends StoredProcedure
	{
		protected MovimientoTflutitSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUTIT");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtitulo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dstitulo"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("webid"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("xpos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ypos"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperaTflutit(String cdtipflu, String cdflujomc, String cdtitulo) throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdtitulo"  , cdtitulo);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflutitSP(getDataSource()), params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflutitSP extends StoredProcedure
	{
		protected RecuperaTflutitSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUTIT");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtitulo"  , OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU", "CDFLUJOMC", "CDTITULO", "DSTITULO", "WEBID", "XPOS", "YPOS"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String modificarDetalleTramiteMC(
			String ntramite,
			String nmordina,
			String comments,
			String cdusuari,
			String cdsisrol,
			Date fecha
			) throws Exception
	{
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("ntramite" , ntramite);
		params.put("nmordina" , nmordina);
		params.put("comments" , comments);
		params.put("cdusuari" , cdusuari);
		params.put("cdsisrol" , cdsisrol);
		params.put("fecha"    , fecha);
		Map<String, Object> procRes = ejecutaSP(new ModificarDetalleTramiteMCSP(getDataSource()),params);
		String comment = (String)procRes.get("pv_comments_o");
		if (StringUtils.isBlank(comment)) {
			throw new ApplicationException("Comentario no regresado");
		}
		return comment;
	}
	
	protected class ModificarDetalleTramiteMCSP extends StoredProcedure
	{
		protected ModificarDetalleTramiteMCSP(DataSource dataSource)
		{
			super(dataSource,"P_ACT_TDMESACONTROL");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("nmordina" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("comments" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fecha"    , OracleTypes.TIMESTAMP));
			declareParameter(new SqlOutParameter("pv_comments_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public String movimientoTmail(
			String cdtipflu,
			String cdflujomc,
			String cdmail,
			String dsmail,
			String dsdestino,
			String dsasunto,
			String dsmensaje,
			String vardestino,
			String varasunto,
			String varmensaje,
			String webid,
			String xpos,
			String ypos,
			String accion) throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"  , cdflujomc);
		params.put("cdmail"     , cdmail);
		params.put("dsmail"     , dsmail);
		params.put("dsdestino"  , dsdestino);
		params.put("dsasunto"   , dsasunto);
		params.put("dsmensaje"  , dsmensaje);
		params.put("vardestino" , vardestino);
		params.put("varasunto"  , varasunto);
		params.put("varmensaje" , varmensaje);
		params.put("webid"      , webid);
		params.put("xpos"       , xpos);
		params.put("ypos"       , ypos);
		params.put("accion"     , accion);
		Map<String,Object> procRes = ejecutaSP(new MovimientoTflumailSP(getDataSource()),params);
		return (String)procRes.get("cdmail");
	}
	
	protected class MovimientoTflumailSP extends StoredProcedure
	{
		protected MovimientoTflumailSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUMAIL");
			declareParameter(new SqlParameter     ("cdtipflu"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("cdflujomc"    , OracleTypes.VARCHAR));
			declareParameter(new SqlInOutParameter("cdmail"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsmail"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsdestino"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsasunto"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("dsmensaje"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("vardestino"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("varasunto"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("varmensaje"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("webid"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("xpos"         , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("ypos"         , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter     ("accion"       , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperaTflumail(String cdtipflu, String cdflujomc, String cdmail) throws Exception 
	{
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdmail"    , cdmail);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflumailSP(getDataSource()), params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflumailSP extends StoredProcedure
	{
		protected RecuperaTflumailSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUMAIL");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdmail"    , OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"CDTIPFLU",
                    "CDFLUJOMC",
                    "CDMAIL",
                    "DSMAIL",
                    "DSDESTINO",
                    "DSASUNTO",
                    "DSMENSAJE",
                    "VARDESTINO",
                    "VARASUNTO",
                    "VARMENSAJE",
                    "WEBID",
                    "XPOS",
                    "YPOS"
                    };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperaTvarmailSP() throws Exception 
	{
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTvarmailSP(getDataSource()), new LinkedHashMap<String, String>());
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTvarmailSP extends StoredProcedure
	{
		protected RecuperaTvarmailSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TVARMAIL");
			String[] cols=new String[]{
					"CDVARMAIL",
	                "DSVARMAIL",
	                "BDFUNCTION"
	                };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarMotivoRechazoTramite (String ntramite, String cdrazrecha) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite"   , ntramite);
		params.put("cdrazrecha" , cdrazrecha);
		ejecutaSP(new GuardarMotivoRechazoTramite(getDataSource()), params);
	}
	
	protected class GuardarMotivoRechazoTramite extends StoredProcedure
	{
		protected GuardarMotivoRechazoTramite(DataSource dataSource)
		{
			super(dataSource,"P_ACT_MOTIVO_RECHAZO_TRAMITE");
			declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrazrecha" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarChecklistInicial (String ntramite, String cdsisrol) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		params.put("cdsisrol", cdsisrol);
		Map<String, Object> procRes = ejecutaSP(new RecuperarChecklistInicialSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarChecklistInicial lista = ", lista));
		return lista;
	}
	
	protected class RecuperarChecklistInicialSP extends StoredProcedure
	{
		protected RecuperarChecklistInicialSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_ACCION_CHECKLIST");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"CDTIPFLU",
		            "CDFLUJOMC",
		            "CLAVEDEST",
		            "WEBIDDEST",
		            "AUX",
		            "CDREVISI",
		            "DSREVISI",
		            "JSVALIDA",
		            "PRIORIDAD"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public String ejecutaProcedureFlujoCorreo(String nomproc, String ntramite) throws Exception{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("ntramite" , ntramite);
		Map<String,Object> procRes = ejecutaSP(new EjecutaProcedureFlujoCorreo(getDataSource(), nomproc),params);		
		String resultado = (String)procRes.get("pv_result_o");
		return resultado;
	}
	
	protected class EjecutaProcedureFlujoCorreo extends StoredProcedure
	{
		protected EjecutaProcedureFlujoCorreo(DataSource dataSource, String nomproc)
		{
			super(dataSource,nomproc);
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_result_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> obtenerCorreosStatusTramite(String ntramite, String cdsisrol, String porEscalamiento) throws Exception{
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite" , ntramite);
		params.put("cdsisrol" , cdsisrol);
		params.put("swescala" , porEscalamiento);
		Map<String, Object> procRes = ejecutaSP(new ObtenerCorreosStatusTramite(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		return lista;
	}
	
	protected class ObtenerCorreosStatusTramite extends StoredProcedure
	{
		protected ObtenerCorreosStatusTramite(DataSource dataSource)
		{
			super(dataSource,"P_GET_MAIL_STATUS_TRAMITE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swescala" , OracleTypes.VARCHAR));
			String[] cols = new String[]{
					"dsmail",
					"dsdestino",
					"dsasunto", 
					"dsmensaje", 
					"vardestino", 
					"varasunto", 
					"varmensaje"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarMensajeCorreoEmision(String ntramite, String mensajeCorreoEmision) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite" , ntramite);
		params.put("mail"     , mensajeCorreoEmision);
		ejecutaSP(new GuardarMensajeCorreoEmisionSP(getDataSource()), params);
	}
	
	protected class GuardarMensajeCorreoEmisionSP extends StoredProcedure
	{
		protected GuardarMensajeCorreoEmisionSP(DataSource dataSource)
		{
			super(dataSource,"P_INS_CORREO_EMISION_TRAMITE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("mail"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarTramitesSinFlag () throws Exception {
		Map<String, Object> procRes = ejecutaSP(new RecuperarTramitesSinFlagSP(getDataSource()), new HashMap<String, String>());
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("Lista de tramites sin flag = ", lista));
		return lista;
	}
	
	protected class RecuperarTramitesSinFlagSP extends StoredProcedure
	{
		protected RecuperarTramitesSinFlagSP(DataSource dataSource)
		{
			super(dataSource,"P_FLAG_GET_TRA_SIN_FLAG");
			String[] cols = new String[] {
					"NTRAMITE",
					"CDTIPFLU",
					"CDFLUJOMC", 
					"FECSTATU", 
					"STATUS", 
					"TIMEMAX", 
					"TIMEWRN1", 
					"TIMEWRN2"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols, true)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void insertarFlagTramite (
		String ntramite,
		Date fecstatu,
		String flag,
		Date fechaAmarilla,
		Date fechaRoja,
		Date fechaMaxima
	) throws Exception {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("ntramite"   , ntramite);
		params.put("fecstatu"   , fecstatu);
		params.put("flag"       , flag);
		params.put("feamarilla" , fechaAmarilla);
		params.put("feroja"     , fechaRoja);
		params.put("fevencim"   , fechaMaxima);
		ejecutaSP(new InsertarFlagTramiteSP(getDataSource()), params);
	}
	
	protected class InsertarFlagTramiteSP extends StoredProcedure
	{
		protected InsertarFlagTramiteSP(DataSource dataSource)
		{
			super(dataSource,"P_FLAG_INSERTA_FLAG");
			declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("fecstatu"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("flag"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("feamarilla" , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("feroja"     , OracleTypes.TIMESTAMP));
			declareParameter(new SqlParameter("fevencim"   , OracleTypes.TIMESTAMP));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarTramitesConFlagVencida () throws Exception {
		Map<String, Object> procRes = ejecutaSP(new RecuperarTramitesConFlagVencidaSP(getDataSource()), new HashMap<String, String>());
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("Lista de tramites con flag vencida = ", lista));
		return lista;
	}
	
	protected class RecuperarTramitesConFlagVencidaSP extends StoredProcedure
	{
		protected RecuperarTramitesConFlagVencidaSP(DataSource dataSource)
		{
			super(dataSource,"P_FLAG_GET_FLAGS_VENCIDAS");
			String[] cols = new String[] {
					"NTRAMITE",
					"FECSTATU",
					"FLAG",
					"FEAMARILLA",
					"FEROJA",
					"FEVENCIM",
					"NUEVAFLAG",
					"CDUNIECO",
					"CDRAMO",
					"ESTADO",
					"NMPOLIZA",
					"NMSUPLEM",
					"STATUS",
					"CDTIPFLU",
					"CDFLUJOMC",
					"STATUSOUT"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols, true)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String, String> recuperarEstatusAnteriorVencido (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarEstatusAnteriorVencidoSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null || lista.size () == 0) {
			throw new ApplicationException("No se encuentra el estatus anterior al vencimiento");
		}
		return lista.get(0);
	}
	
	protected class RecuperarEstatusAnteriorVencidoSP extends StoredProcedure
	{
		protected RecuperarEstatusAnteriorVencidoSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_STATUS_ANTES_VENCIM");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols = new String[] {
				"CDSTATUS",
				"DSSTATUS",
				"CDTIPFLU",
				"CDFLUJOMC"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols, false)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarRolesPermisoRegresarVencido(String ntramite, String status) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite" , ntramite);
		params.put("status"   , status);
		Map<String, Object> procRes = ejecutaSP(new RecuperarRolesPermisoRegresarVencidoSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		return lista;
	}
	
	protected class RecuperarRolesPermisoRegresarVencidoSP extends StoredProcedure
	{
		protected RecuperarRolesPermisoRegresarVencidoSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_ROLES_PERM_VENCIDO");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("status"   , OracleTypes.VARCHAR));
			String[] cols = new String[] {
				"CDSISROL",
				"DSSISROL"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols, false)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public Map<String, String> recuperarDatosVencimiento (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarDatosVencimientoSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null || lista.size () == 0) {
			throw new ApplicationException("No se encuentran los datos del vencimiento");
		}
		return lista.get(0);
	}
	
	protected class RecuperarDatosVencimientoSP extends StoredProcedure
	{
		protected RecuperarDatosVencimientoSP(DataSource dataSource)
		{
			super(dataSource,"P_GET_DATOS_VENCIMIENTO");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols = new String[] {
				"FECSTATU",
				"FLAG",
				"FEAMARILLA",
				"FEROJA",
				"FEVENCIM",
				"NMORDINA",
				"FEREGISTRO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols, true)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTrequisi(
			String cdrequisi
			,String dsrequisi
			,String cdtiptra
			,boolean pideDato
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdrequisi"  , cdrequisi);
		params.put("dsrequisi"  , dsrequisi);
		params.put("cdtiptra"   , cdtiptra);
		params.put("swpidedato" , pideDato ? "S" : "N");
		params.put("accion"     , accion);
		ejecutaSP(new MovimientoTrequisiSP(getDataSource()),params);
	}
	
	protected class MovimientoTrequisiSP extends StoredProcedure
	{
		protected MovimientoTrequisiSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TREQUISI");
			declareParameter(new SqlParameter("cdrequisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsrequisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdtiptra"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swpidedato" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"     , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTflurevreq(
			String cdtipflu
			,String cdflujomc
			,String cdrevisi
			,String cdrequisi
			,String swobliga
			,String swlista
			,String accion
			) throws Exception
	{
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("cdrevisi"  , cdrevisi);
		params.put("cdrequisi" , cdrequisi);
		params.put("swobliga"  , swobliga);
		params.put("swlista"   , swlista);
		params.put("accion"    , accion);
		ejecutaSP(new MovimientoTflurevreqSP(getDataSource()),params);
	}
	
	protected class MovimientoTflurevreqSP extends StoredProcedure
	{
		protected MovimientoTflurevreqSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUREVREQ");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrevisi"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrequisi" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swobliga"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("swlista"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("accion"    , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String,String>> recuperaTflurevreq(String cdtipflu, String cdflujomc, String cdrevisi) throws Exception
	{
		Map<String,String>       params  = new LinkedHashMap<String,String>();
		
		params.put("cdtipflu",cdtipflu);
		params.put("cdflujomc",cdflujomc);
		params.put("cdrevisi",cdrevisi);
		Map<String,Object>       procRes = ejecutaSP(new RecuperaTflurevreqSP(getDataSource()),params);
		List<Map<String,String>> lista   = (List<Map<String,String>>)procRes.get("pv_registro_o");
		if(lista==null)
		{
			lista = new ArrayList<Map<String,String>>();
		}
		return lista;
	}
	
	protected class RecuperaTflurevreqSP extends StoredProcedure
	{
		protected RecuperaTflurevreqSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_GET_TFLUREVREQ");
			declareParameter(new SqlParameter("cdtipflu", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrevisi", OracleTypes.VARCHAR));
			String[] cols=new String[]{ "CDTIPFLU","CDFLUJOMC","CDREVISI","CDREQUISI","SWOBLIGA", "SWLISTA"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void marcarRequisitoRevision (
			String cdtipflu,
			String cdflujomc,
			String ntramite,
			String cdrequisi,
			boolean activo,
			String dsdato,
			String cdusuari,
			String cdsisrol) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu"  , cdtipflu);
		params.put("cdflujomc" , cdflujomc);
		params.put("ntramite"  , ntramite);
		params.put("cdrequisi" , cdrequisi);
		params.put("activo"    , activo ? "S" : "N");
		params.put("dsdato"    , dsdato);
		params.put("cdusuari"  , cdusuari);
		params.put("cdsisrol"  , cdsisrol);
		ejecutaSP(new MarcarRequisitoRevisionSP(getDataSource()), params);
	}
	
	protected class MarcarRequisitoRevisionSP extends StoredProcedure
	{
		protected MarcarRequisitoRevisionSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TREQUITRA");
			declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrequisi" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("activo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("dsdato"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"  , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void marcarRevisionConfirmada (
			String cdtipflu,
			String cdflujomc,
			String ntramite,
			String cdrevisi,
			boolean confirmada,
			String cdusuari,
			String cdsisrol) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdtipflu"   , cdtipflu);
		params.put("cdflujomc"  , cdflujomc);
		params.put("ntramite"   , ntramite);
		params.put("cdrevisi"   , cdrevisi);
		params.put("confirmada" , confirmada ? "S" : "N");
		params.put("cdusuari"   , cdusuari);
		params.put("cdsisrol"   , cdsisrol);
		ejecutaSP(new MarcarRevisionConfirmadaSP(getDataSource()), params);
	}
	
	protected class MarcarRevisionConfirmadaSP extends StoredProcedure
	{
		protected MarcarRevisionConfirmadaSP(DataSource dataSource)
		{
			super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUREVSW");
			declareParameter(new SqlParameter("cdtipflu"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdflujomc"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdrevisi"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("confirmada" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdusuari"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cdsisrol"   , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarRequisitosDocumentosObligatoriosFaltantes (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarRequisitosDocumentosObligatoriosFaltantesSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarRequisitosDocumentosObligatoriosFaltantes lista = ",lista));
		return lista;
	}
	
	protected class RecuperarRequisitosDocumentosObligatoriosFaltantesSP extends StoredProcedure {
		protected RecuperarRequisitosDocumentosObligatoriosFaltantesSP (DataSource dataSource) {
			super(dataSource,"P_GET_LISTA_REQ_REV_FALTAN");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			String[] cols = new String[]{ "TIPO", "DESCRIP"};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarRequisitosDatosTramite (String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarRequisitosDatosTramiteSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarRequisitosDatosTramite lista = ",lista));
		return lista;
	}
	
	protected class RecuperarRequisitosDatosTramiteSP extends StoredProcedure {
		protected RecuperarRequisitosDatosTramiteSP (DataSource dataSource) {
			super(dataSource,"P_GET_REQUISITOS_DATOS_TRAMITE");
			declareParameter(new SqlParameter("ntramite", OracleTypes.VARCHAR));
			String[] cols = new String[] {
					"CDREQUISI" , "DSREQUISI" , "CDUSUARI" , "DSUSUARI",
                    "CDSISROL"  , "DSSISROL"  , "FEFECHA"  , "DSDATO", "SWACTIVO"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarTodasSucursales () throws Exception {
		Map<String, Object> procRes = ejecutaSP(new RecuperarTodasSucursalesSP(getDataSource()), new HashMap<String, String>());
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarTodasSucursales lista = ",lista));
		return lista;
	}
	
	protected class RecuperarTodasSucursalesSP extends StoredProcedure {
		protected RecuperarTodasSucursalesSP (DataSource dataSource) {
			super(dataSource,"PKG_MESACONTROL.P_GET_TODAS_SUCURSALES");
			String[] cols = new String[] { "CDUNIECO" , "DSUNIECO" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void movimientoTflusuc(
			String cdtipflu,
			String cdflujomc,
			String cdunieco,
			String webid,
			String xpos,
			String ypos,
			String nivel,
			String capacidad,
			String accion) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public String recuperarEstatusDefectoRol (String cdsisrol) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("cdsisrol", cdsisrol);
		Map<String,Object> procRes = ejecutaSP(new RecuperarEstatusDefectoRolSP(getDataSource()), params);
		String estatus = (String) procRes.get("pv_status_o");
		logger.debug("P_GET_STATUS_NUEVO_TRA_X_ROL Estatus por defecto para el rol {}: {}", cdsisrol, estatus);
		return estatus;
	}
	
	protected class RecuperarEstatusDefectoRolSP extends StoredProcedure {
		protected RecuperarEstatusDefectoRolSP (DataSource dataSource) {
			super(dataSource,"P_GET_STATUS_NUEVO_TRA_X_ROL");
			declareParameter(new SqlParameter("cdsisrol", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_status_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarValidacionPorCdvalidafk(String ntramite, String clave) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite" , ntramite);
		params.put("clave"    , clave);
		Map<String, Object> procRes = ejecutaSP(new RecuperarValidacionPorCdvalidafkSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarValidacionPorCdvalidafk lista = ",lista));
		return lista;
	}
	
	protected class RecuperarValidacionPorCdvalidafkSP extends StoredProcedure {
		protected RecuperarValidacionPorCdvalidafkSP (DataSource dataSource) {
			super(dataSource,"P_GET_ACC_VALIDAC_FLOTANTE");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("clave"    , OracleTypes.VARCHAR));
			String[] cols = new String[] { "CDTIPFLU" , "CDFLUJOMC", "TIPOENT", "CDENTIDAD", "WEBID", "STATUS" };
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public List<Map<String, String>> recuperarCotizacionesColectivasAprobadas(String ntramite) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("ntramite", ntramite);
		Map<String, Object> procRes = ejecutaSP(new RecuperarCotizacionesColectivasAprobadasSP(getDataSource()), params);
		List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
		if (lista == null) {
			lista = new ArrayList<Map<String, String>>();
		}
		logger.debug(Utils.log("recuperarCotizacionesColectivasAprobadas lista = ",lista));
		return lista;
	}
	
	protected class RecuperarCotizacionesColectivasAprobadasSP extends StoredProcedure {
		protected RecuperarCotizacionesColectivasAprobadasSP (DataSource dataSource) {
			super(dataSource,"P_GET_COTI_COLEC_APROBADAS");
			declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
			String[] cols = new String[] {
					"CDUNIECO",
					"CDRAMO",
					"ESTADO",
					"NMPOLIZA",
					"PRIMA"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	public String recuperaNombreMd5(String md5) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("md5", md5);
		Map<String, Object> procRes = ejecutaSP(new RecuperaNombreMd5SP(getDataSource()), params);
		String nombre = (String) procRes.get("pv_salida_o");
		if (StringUtils.isBlank(nombre)) {
			throw new ApplicationException("MD5 NO EXISTE");
		}
		return nombre;
	}
	
	protected class RecuperaNombreMd5SP extends StoredProcedure {
		protected RecuperaNombreMd5SP (DataSource dataSource) {
			super(dataSource,"P_GET_NOMBREMD5");
			declareParameter(new SqlParameter("md5" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_salida_o" , OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
			compile();
		}
	}
	
	@Override
	public void guardarVentanaDatosTramite (String ntramite,
            boolean swcdunieco,
            boolean swcdramo,
            boolean swestado,
            boolean swnmpoliza,
            boolean swnmsuplem,
            boolean swnmsolici,
            boolean swcdsucadm,
            boolean swcdsucdoc,
            boolean swcdsubram,
            boolean swcdtiptra,
            boolean swferecepc,
            boolean swcdagente,
            boolean swreferencia,
            boolean swnombre,
            boolean swfecstatu,
            boolean swstatus,
            boolean swcomments,
            boolean swcdtipsit,
            boolean swotvalor01,
            boolean swotvalor02,
            boolean swotvalor03,
            boolean swotvalor04,
            boolean swotvalor05,
            boolean swotvalor06,
            boolean swotvalor07,
            boolean swotvalor08,
            boolean swotvalor09,
            boolean swotvalor10,
            boolean swotvalor11,
            boolean swotvalor12,
            boolean swotvalor13,
            boolean swotvalor14,
            boolean swotvalor15,
            boolean swotvalor16,
            boolean swotvalor17,
            boolean swotvalor18,
            boolean swotvalor19,
            boolean swotvalor20,
            boolean swotvalor21,
            boolean swotvalor22,
            boolean swotvalor23,
            boolean swotvalor24,
            boolean swotvalor25,
            boolean swotvalor26,
            boolean swotvalor27,
            boolean swotvalor28,
            boolean swotvalor29,
            boolean swotvalor30,
            boolean swotvalor31,
            boolean swotvalor32,
            boolean swotvalor33,
            boolean swotvalor34,
            boolean swotvalor35,
            boolean swotvalor36,
            boolean swotvalor37,
            boolean swotvalor38,
            boolean swotvalor39,
            boolean swotvalor40,
            boolean swotvalor41,
            boolean swotvalor42,
            boolean swotvalor43,
            boolean swotvalor44,
            boolean swotvalor45,
            boolean swotvalor46,
            boolean swotvalor47,
            boolean swotvalor48,
            boolean swotvalor49,
            boolean swotvalor50,
            boolean swswimpres,
            boolean swcdtipflu,
            boolean swcdflujomc,
            boolean swcdusuari,
            boolean swcdtipsup,
            boolean swswvispre,
            boolean swcdpercli,
            boolean swrenuniext,
            boolean swrenramo,
            boolean swrenpoliex,
            boolean swsworigenmesa,
            boolean swcdrazrecha,
            boolean swcdunidspch,
            String cdunieco,
            String cdramo,
            String estado,
            String nmpoliza,
            String nmsuplem,
            String nmsolici,
            String cdsucadm,
            String cdsucdoc,
            String cdsubram,
            String cdtiptra,
            String ferecepc,
            String cdagente,
            String referencia,
            String nombre,
            String fecstatu,
            String status,
            String comments,
            String cdtipsit,
            String otvalor01,
            String otvalor02,
            String otvalor03,
            String otvalor04,
            String otvalor05,
            String otvalor06,
            String otvalor07,
            String otvalor08,
            String otvalor09,
            String otvalor10,
            String otvalor11,
            String otvalor12,
            String otvalor13,
            String otvalor14,
            String otvalor15,
            String otvalor16,
            String otvalor17,
            String otvalor18,
            String otvalor19,
            String otvalor20,
            String otvalor21,
            String otvalor22,
            String otvalor23,
            String otvalor24,
            String otvalor25,
            String otvalor26,
            String otvalor27,
            String otvalor28,
            String otvalor29,
            String otvalor30,
            String otvalor31,
            String otvalor32,
            String otvalor33,
            String otvalor34,
            String otvalor35,
            String otvalor36,
            String otvalor37,
            String otvalor38,
            String otvalor39,
            String otvalor40,
            String otvalor41,
            String otvalor42,
            String otvalor43,
            String otvalor44,
            String otvalor45,
            String otvalor46,
            String otvalor47,
            String otvalor48,
            String otvalor49,
            String otvalor50,
            String swimpres,
            String cdtipflu,
            String cdflujomc,
            String cdusuari,
            String cdtipsup,
            String swvispre,
            String cdpercli,
            String renuniext,
            String renramo,
            String renpoliex,
            String sworigenmesa,
            String cdrazrecha,
            String cdunidspch) throws Exception {
	    Map<String, Object> params = new LinkedHashMap<String, Object>();
	    params.put("ntramite", ntramite);
	    params.put("swcdunieco", swcdunieco ? "S" : "N");
	    params.put("swcdramo", swcdramo ? "S" : "N");
	    params.put("swestado", swestado ? "S" : "N");
	    params.put("swnmpoliza", swnmpoliza ? "S" : "N");
	    params.put("swnmsuplem", swnmsuplem ? "S" : "N");
	    params.put("swnmsolici", swnmsolici ? "S" : "N");
	    params.put("swcdsucadm", swcdsucadm ? "S" : "N");
	    params.put("swcdsucdoc", swcdsucdoc ? "S" : "N");
	    params.put("swcdsubram", swcdsubram ? "S" : "N");
	    params.put("swcdtiptra", swcdtiptra ? "S" : "N");
	    params.put("swferecepc", swferecepc ? "S" : "N");
	    params.put("swcdagente", swcdagente ? "S" : "N");
	    params.put("swreferencia", swreferencia ? "S" : "N");
	    params.put("swnombre", swnombre ? "S" : "N");
	    params.put("swfecstatu", swfecstatu ? "S" : "N");
	    params.put("swstatus", swstatus ? "S" : "N");
	    params.put("swcomments", swcomments ? "S" : "N");
	    params.put("swcdtipsit", swcdtipsit ? "S" : "N");
	    params.put("swotvalor01", swotvalor01 ? "S" : "N");
	    params.put("swotvalor02", swotvalor02 ? "S" : "N");
	    params.put("swotvalor03", swotvalor03 ? "S" : "N");
	    params.put("swotvalor04", swotvalor04 ? "S" : "N");
	    params.put("swotvalor05", swotvalor05 ? "S" : "N");
	    params.put("swotvalor06", swotvalor06 ? "S" : "N");
	    params.put("swotvalor07", swotvalor07 ? "S" : "N");
	    params.put("swotvalor08", swotvalor08 ? "S" : "N");
	    params.put("swotvalor09", swotvalor09 ? "S" : "N");
	    params.put("swotvalor10", swotvalor10 ? "S" : "N");
	    params.put("swotvalor11", swotvalor11 ? "S" : "N");
	    params.put("swotvalor12", swotvalor12 ? "S" : "N");
	    params.put("swotvalor13", swotvalor13 ? "S" : "N");
	    params.put("swotvalor14", swotvalor14 ? "S" : "N");
	    params.put("swotvalor15", swotvalor15 ? "S" : "N");
	    params.put("swotvalor16", swotvalor16 ? "S" : "N");
	    params.put("swotvalor17", swotvalor17 ? "S" : "N");
	    params.put("swotvalor18", swotvalor18 ? "S" : "N");
	    params.put("swotvalor19", swotvalor19 ? "S" : "N");
	    params.put("swotvalor20", swotvalor20 ? "S" : "N");
	    params.put("swotvalor21", swotvalor21 ? "S" : "N");
	    params.put("swotvalor22", swotvalor22 ? "S" : "N");
	    params.put("swotvalor23", swotvalor23 ? "S" : "N");
	    params.put("swotvalor24", swotvalor24 ? "S" : "N");
	    params.put("swotvalor25", swotvalor25 ? "S" : "N");
	    params.put("swotvalor26", swotvalor26 ? "S" : "N");
	    params.put("swotvalor27", swotvalor27 ? "S" : "N");
	    params.put("swotvalor28", swotvalor28 ? "S" : "N");
	    params.put("swotvalor29", swotvalor29 ? "S" : "N");
	    params.put("swotvalor30", swotvalor30 ? "S" : "N");
	    params.put("swotvalor31", swotvalor31 ? "S" : "N");
	    params.put("swotvalor32", swotvalor32 ? "S" : "N");
	    params.put("swotvalor33", swotvalor33 ? "S" : "N");
	    params.put("swotvalor34", swotvalor34 ? "S" : "N");
	    params.put("swotvalor35", swotvalor35 ? "S" : "N");
	    params.put("swotvalor36", swotvalor36 ? "S" : "N");
	    params.put("swotvalor37", swotvalor37 ? "S" : "N");
	    params.put("swotvalor38", swotvalor38 ? "S" : "N");
	    params.put("swotvalor39", swotvalor39 ? "S" : "N");
	    params.put("swotvalor40", swotvalor40 ? "S" : "N");
	    params.put("swotvalor41", swotvalor41 ? "S" : "N");
	    params.put("swotvalor42", swotvalor42 ? "S" : "N");
	    params.put("swotvalor43", swotvalor43 ? "S" : "N");
	    params.put("swotvalor44", swotvalor44 ? "S" : "N");
	    params.put("swotvalor45", swotvalor45 ? "S" : "N");
	    params.put("swotvalor46", swotvalor46 ? "S" : "N");
	    params.put("swotvalor47", swotvalor47 ? "S" : "N");
	    params.put("swotvalor48", swotvalor48 ? "S" : "N");
	    params.put("swotvalor49", swotvalor49 ? "S" : "N");
	    params.put("swotvalor50", swotvalor50 ? "S" : "N");
	    params.put("swswimpres", swswimpres ? "S" : "N");
	    params.put("swcdtipflu", swcdtipflu ? "S" : "N");
	    params.put("swcdflujomc", swcdflujomc ? "S" : "N");
	    params.put("swcdusuari", swcdusuari ? "S" : "N");
	    params.put("swcdtipsup", swcdtipsup ? "S" : "N");
	    params.put("swswvispre", swswvispre ? "S" : "N");
	    params.put("swcdpercli", swcdpercli ? "S" : "N");
	    params.put("swrenuniext", swrenuniext ? "S" : "N");
	    params.put("swrenramo", swrenramo ? "S" : "N");
	    params.put("swrenpoliex", swrenpoliex ? "S" : "N");
	    params.put("swsworigenmesa", swsworigenmesa ? "S" : "N");
	    params.put("swcdrazrecha", swcdrazrecha ? "S" : "N");
	    params.put("swcdunidspch", swcdunidspch ? "S" : "N");
	    params.put("cdunieco", cdunieco);
	    params.put("cdramo", cdramo);
	    params.put("estado", estado);
	    params.put("nmpoliza", nmpoliza);
	    params.put("nmsuplem", nmsuplem);
	    params.put("nmsolici", nmsolici);
	    params.put("cdsucadm", cdsucadm);
	    params.put("cdsucdoc", cdsucdoc);
	    params.put("cdsubram", cdsubram);
	    params.put("cdtiptra", cdtiptra);
	    params.put("ferecepc", Utils.parse(ferecepc));
	    params.put("cdagente", cdagente);
	    params.put("referencia", referencia);
	    params.put("nombre", nombre);
	    params.put("fecstatu", Utils.parse(fecstatu));
	    params.put("status", status);
	    params.put("comments", comments);
	    params.put("cdtipsit", cdtipsit);
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
	    params.put("swimpres", swimpres);
	    params.put("cdtipflu", cdtipflu);
	    params.put("cdflujomc", cdflujomc);
	    params.put("cdusuari", cdusuari);
	    params.put("cdtipsup", cdtipsup);
	    params.put("swvispre", swvispre);
	    params.put("cdpercli", cdpercli);
	    params.put("renuniext", renuniext);
	    params.put("renramo", renramo);
	    params.put("renpoliex", renpoliex);
	    params.put("sworigenmesa", sworigenmesa);
	    params.put("cdrazrecha", cdrazrecha);
	    params.put("cdunidspch", cdunidspch);
	    ejecutaSP(new GuardarVentanaDatosTramiteSP(getDataSource()), params);
	}
    
    protected class GuardarVentanaDatosTramiteSP extends StoredProcedure {
        protected GuardarVentanaDatosTramiteSP (DataSource dataSource) {
            super(dataSource,"P_COMP_SAVE_VENT_DATOS_TRAMITE");
            declareParameter(new SqlParameter("ntramite" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdramo" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swestado" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swnmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swnmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swnmsolici" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdsucadm" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdsucdoc" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdsubram" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdtiptra" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swferecepc" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdagente" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swreferencia" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swnombre" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swfecstatu" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swstatus" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcomments" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdtipsit" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor01" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor02" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor03" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor04" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor05" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor06" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor07" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor08" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor09" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor10" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor11" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor12" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor13" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor14" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor15" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor16" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor17" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor18" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor19" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor20" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor21" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor22" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor23" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor24" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor25" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor26" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor27" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor28" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor29" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor30" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor31" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor32" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor33" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor34" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor35" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor36" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor37" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor38" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor39" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor40" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor41" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor42" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor43" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor44" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor45" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor46" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor47" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor48" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor49" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swotvalor50" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swswimpres" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdtipflu" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdflujomc" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdtipsup" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swswvispre" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdpercli" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swrenuniext" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swrenramo" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swrenpoliex" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swsworigenmesa" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdrazrecha" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swcdunidspch" , OracleTypes.VARCHAR));
            
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsolici" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsucadm" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsucdoc" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsubram" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtiptra" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("ferecepc" , OracleTypes.DATE));
            declareParameter(new SqlParameter("cdagente" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("referencia" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nombre" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("fecstatu" , OracleTypes.DATE));
            declareParameter(new SqlParameter("status" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("comments" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsit" , OracleTypes.VARCHAR));
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
            declareParameter(new SqlParameter("swimpres" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipflu" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdusuari" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdtipsup" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("swvispre" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdpercli" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("renuniext" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("renramo" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("renpoliex" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("sworigenmesa" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdrazrecha" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdunidspch" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String recuperarCdatribuPorDsatribuTatriflumc (String cdtipflu, String cdflujomc, String dsatribu) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("cdtipflu"  , cdtipflu);
        params.put("cdflujomc" , cdflujomc);
        params.put("dsatribu"  , dsatribu);
        Map<String, Object> procRes = ejecutaSP(new RecuperarCdatribuPorDsatribuTatriflumcSP(getDataSource()), params);
        String cdatribu = (String) procRes.get("pv_cdatribu_o");
        return cdatribu;
    }
    
    protected class RecuperarCdatribuPorDsatribuTatriflumcSP extends StoredProcedure {
        protected RecuperarCdatribuPorDsatribuTatriflumcSP (DataSource dataSource) {
            super(dataSource,"P_MESA_GET_CDATRIBU_TATRIFLUMC");
            declareParameter(new SqlParameter("cdtipflu"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdflujomc" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("dsatribu"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdatribu_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void movimientoTfluaccrolLote (List<Map<String, String>> lista) throws Exception {
        String[][] array = new String[lista.size()][];
        int i = 0;
        for (Map<String, String> obj : lista) {
            array[i++] = new String[]{
                    obj.get("cdtipflu"),
                    obj.get("cdflujomc"),
                    obj.get("cdaccion"),
                    obj.get("cdsisrol"),
                    obj.get("swpermiso"),
                    obj.get("accion")
            };
        }
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("array", new SqlArrayValue(array));
        ejecutaSP(new MovimientoTfluaccrolLoteSP(getDataSource()),params);
    }
    
    protected class MovimientoTfluaccrolLoteSP extends StoredProcedure {
        protected MovimientoTfluaccrolLoteSP (DataSource dataSource) {
            super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUACCROL_LOTE");
            declareParameter(new SqlParameter("array", OracleTypes.ARRAY, "LISTA_LISTAS_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void actualizaCoordenadasLote (List<Map<String, String>> lista) throws Exception {
        String[][] array = new String[lista.size()][];
        int i = 0;
        for (Map<String, String> obj : lista) {
            array[i++] = new String[]{
                    obj.get("cdtipflu"),
                    obj.get("cdflujomc"),
                    obj.get("tipo"),
                    obj.get("clave"),
                    obj.get("webid"),
                    obj.get("xpos"),
                    obj.get("ypos")
            };
        }
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("array", new SqlArrayValue(array));
        ejecutaSP(new ActualizaCoordenadasLoteSP(getDataSource()),params);
    }
    
    protected class ActualizaCoordenadasLoteSP extends StoredProcedure
    {
        protected ActualizaCoordenadasLoteSP(DataSource dataSource)
        {
            super(dataSource,"PKG_MESACONTROL.P_ACTUALIZA_COORDS_LOTE");
            declareParameter(new SqlParameter("array", OracleTypes.ARRAY, "LISTA_LISTAS_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void movimientoTflurevdocLote (List<Map<String, String>> lista) throws Exception {
        String[][] array = new String[lista.size()][];
        int i = 0;
        for (Map<String, String> obj : lista) {
            array[i++] = new String[]{
                    obj.get("cdtipflu"),
                    obj.get("cdflujomc"),
                    obj.get("cdrevisi"),
                    obj.get("cddocume"),
                    obj.get("swobliga"),
                    obj.get("swlista"),
                    obj.get("accion")
            };
        }
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("array", new SqlArrayValue(array));
        ejecutaSP(new MovimientoTflurevdocLoteSP(getDataSource()),params);
    }
    
    protected class MovimientoTflurevdocLoteSP extends StoredProcedure
    {
        protected MovimientoTflurevdocLoteSP(DataSource dataSource)
        {
            super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUREVDOC_LOTE");
            declareParameter(new SqlParameter("array", OracleTypes.ARRAY, "LISTA_LISTAS_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void movimientoTflurevreqLote (List<Map<String, String>> lista) throws Exception {
        String[][] array = new String[lista.size()][];
        int i = 0;
        for (Map<String, String> obj : lista) {
            array[i++] = new String[]{
                    obj.get("cdtipflu"),
                    obj.get("cdflujomc"),
                    obj.get("cdrevisi"),
                    obj.get("cdrequisi"),
                    obj.get("swobliga"),
                    obj.get("swlista"),
                    obj.get("accion")
            };
        }
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("array", new SqlArrayValue(array));
        ejecutaSP(new MovimientoTflurevreqLoteSP(getDataSource()),params);
    }
    
    protected class MovimientoTflurevreqLoteSP extends StoredProcedure
    {
        protected MovimientoTflurevreqLoteSP(DataSource dataSource)
        {
            super(dataSource,"PKG_MESACONTROL.P_MOV_TFLUREVREQ_LOTE");
            declareParameter(new SqlParameter("array", OracleTypes.ARRAY, "LISTA_LISTAS_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    public static class AlvaroObj {
        private int cdunieco, cdramo, nmpoliza;
        private String estado;
        public int getCdunieco() {
            return cdunieco;
        }
        public void setCdunieco(int cdunieco) {
            this.cdunieco = cdunieco;
        }
        public int getCdramo() {
            return cdramo;
        }
        public void setCdramo(int cdramo) {
            this.cdramo = cdramo;
        }
        public int getNmpoliza() {
            return nmpoliza;
        }
        public void setNmpoliza(int nmpoliza) {
            this.nmpoliza = nmpoliza;
        }
        public String getEstado() {
            return estado;
        }
        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
    
    @Override
    public void pruebaGuardarObjeto (AlvaroObj objeto) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pv_objeto_i", new SqlStructValue<AlvaroObj>(objeto));
        ejecutaSP(new PruebaGuardarObjetoSP(getDataSource()), params);
    }
    
    protected class PruebaGuardarObjetoSP extends StoredProcedure {
        protected PruebaGuardarObjetoSP (DataSource dataSource) {
            super(dataSource,"P_MESA_RECIBE_OBJETO");
            declareParameter(new SqlParameter("pv_objeto_i", OracleTypes.STRUCT, "ALVAROOBJ"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    // http://stackoverflow.com/questions/26773736/pass-array-as-input-parameter-to-an-oracle-stored-procedure-using-simple-jdbc-ca
    // https://manthapavankumar.wordpress.com/2013/10/19/handling-oracle-stored-procedures-with-arrays-or-table-types-in-spring/
    public void pruebaGuardarLista (List<AlvaroObj> lista) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        AlvaroObj[] array = new AlvaroObj[lista.size()];
        int i = 0;
        for (AlvaroObj obj : lista) {
            array[i++] = obj;
        }
        params.put("pv_lista_i", new SqlArrayValue<AlvaroObj>(array, "ALVAROOBJ"));
        ejecutaSP(new PruebaGuardarListaSP(getDataSource()), params);
    }
    
    protected class PruebaGuardarListaSP extends StoredProcedure {
        protected PruebaGuardarListaSP (DataSource dataSource) {
            super(dataSource,"P_MESA_RECIBE_LISTA");
            declareParameter(new SqlParameter("pv_lista_i", OracleTypes.ARRAY, "ALVAROTABLE"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String, String>> recuperarPropiedadesDespachadorSucursales (String cdtipram) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("cdtipram", cdtipram);
        Map<String, Object> procRes = ejecutaSP(new RecuperarPropiedadesDespachadorSucursalesSP(getDataSource()), params);
        List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
        if (lista == null) {
            lista = new ArrayList<Map<String, String>>();
        }
        logger.debug("recuperarPropiedadesDespachadorSucursales lista: {}", Utils.log(lista));
        return lista;
    }
    
    protected class RecuperarPropiedadesDespachadorSucursalesSP extends StoredProcedure {
        protected RecuperarPropiedadesDespachadorSucursalesSP (DataSource dataSource) {
            super(dataSource,"P_DSPCH_LOG_SUCUR");
            declareParameter(new SqlParameter("cdtipram" , OracleTypes.VARCHAR));
            String[] cols = new String[] {
                    "CDUNIECO", "CDUNIZON", "NMCAPACI", "CDNIVEL", "SWAPOYO", "SWACTIVA", "TRAMITES", "DSUNIECO", "DSUNIZON" , "DSNIVEL"
                    };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String, String>> recuperarPropiedadesDespachadorUsuarios (String cdunieco, String nivel, String cdsisrol) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("cdunieco" , cdunieco);
        params.put("nivel"    , nivel);
        params.put("cdsisrol" , cdsisrol);
        Map<String, Object> procRes = ejecutaSP(new RecuperarPropiedadesDespachadorUsuariosSP(getDataSource()), params);
        List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
        if (lista == null) {
            lista = new ArrayList<Map<String, String>>();
        }
        logger.debug("recuperarPropiedadesDespachadorUsuarios lista: {}", Utils.log(lista));
        return lista;
    }
    
    protected class RecuperarPropiedadesDespachadorUsuariosSP extends StoredProcedure {
        protected RecuperarPropiedadesDespachadorUsuariosSP (DataSource dataSource) {
            super(dataSource,"P_DSPCH_LOG_USERROL");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nivel"    , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
            String[] cols = new String[] {
                    "CDUSUARI", "DSUSUARI", "SWACTIVO", "CDSISROL", "CDESTADO", "DSSISROL", "EXCLUIDO", "TRAMITES", "TRAMITESROL"
                    };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String, String>> recuperarPropiedadesDespachadorUsuariosAll (String cdsisrol) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("cdsisrol" , cdsisrol);
        Map<String, Object> procRes = ejecutaSP(new RecuperarPropiedadesDespachadorUsuariosAllSP(getDataSource()), params);
        List<Map<String, String>> lista = (List<Map<String, String>>) procRes.get("pv_registro_o");
        if (lista == null) {
            lista = new ArrayList<Map<String, String>>();
        }
        logger.debug("recuperarPropiedadesDespachadorUsuariosAll lista: {}", Utils.log(lista));
        return lista;
    }
    
    protected class RecuperarPropiedadesDespachadorUsuariosAllSP extends StoredProcedure {
        protected RecuperarPropiedadesDespachadorUsuariosAllSP (DataSource dataSource) {
            super(dataSource,"P_DSPCH_LOG_USERALL");
            declareParameter(new SqlParameter("cdsisrol" , OracleTypes.VARCHAR));
            String[] cols = new String[] {
                    "CDUSUARI", "DSUSUARI", "SWACTIVO", "CDSISROL", "CDESTADO", "DSSISROL", "EXCLUIDO",
                    "SUCURSAL_DANIOS", "SUCURSAL_SALUD", "SWSUSMAT", "TRAMITES", "TRAMITESROL"
                    };
            declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void actualizarTramiteSustituto(String ntramite, String ntrasust) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_ntramite_i", ntramite);
        params.put("pv_ntrasust_i", ntrasust);
        Map<String, Object> procRes = ejecutaSP(new ActualizarTramiteSustitutoSP(getDataSource()), params);
        String error = (String) procRes.get("pv_dserror_o");
        if (StringUtils.isNotBlank(error)) {
            throw new ApplicationException(error);
        }
    }
    
    protected class ActualizarTramiteSustitutoSP extends StoredProcedure {
        protected ActualizarTramiteSustitutoSP (DataSource dataSource) {
            super(dataSource, "P_MC_UPD_NTRASUST_TRAMITE");
            declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_ntrasust_i" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_dserror_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void cambiarTipoEndosoTramite (String ntramite, String cdtipsup) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_ntramite_i", ntramite);
        params.put("pv_cdtipsup_i", cdtipsup);
        Map<String, Object> procRes = ejecutaSP(new CambiarTipoEndosoTramiteSP(getDataSource()), params);
        String error = (String) procRes.get("pv_dserror_o");
        if (StringUtils.isNotBlank(error)) {
            throw new ApplicationException(error);
        }
    }
    
    protected class CambiarTipoEndosoTramiteSP extends StoredProcedure {
        protected CambiarTipoEndosoTramiteSP (DataSource dataSource) {
            super(dataSource, "P_MC_UPD_TIPO_ENDOSO_TRAMITE");
            declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdtipsup_i" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_dserror_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String recuperarCorreoAgenteTramite (String ntramite) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_ntramite_i", ntramite);
        Map<String, Object> procRes = ejecutaSP(new RecuperarCorreoAgenteTramiteSP(getDataSource()), params);
        String mail = (String) procRes.get("pv_result_o");
        logger.debug("recuperarCorreoAgenteTramite 3 correo: {}", mail);
        return mail;
    }
    
    protected class RecuperarCorreoAgenteTramiteSP extends StoredProcedure {
        protected RecuperarCorreoAgenteTramiteSP (DataSource dataSource) {
            super(dataSource, "P_MAIL_GET_MAIL_AGE");
            declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_result_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public FlujoVO generarYRecuperarFlujoRSTN (String ntramite, String cdusuari, String cdsisrol) throws Exception {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("pv_ntramite_i", ntramite);
        params.put("pv_cdusuari_i", cdusuari);
        params.put("pv_cdsisrol_i", cdsisrol);
        Map<String, Object> procRes = ejecutaSP(new GenerarYRecuperarFlujoRSTN(getDataSource()), params);
        FlujoVO flujo = new FlujoVO();
        flujo.setNtramite(ntramite);
        flujo.setStatus((String)    procRes.get("pv_status_o"));
        flujo.setCdtipflu((String)  procRes.get("pv_cdtipflu_o"));
        flujo.setCdflujomc((String) procRes.get("pv_cdflujomc_o"));
        flujo.setWebid((String)     procRes.get("pv_webid_o"));
        flujo.setTipoent((String)   procRes.get("pv_tipoent_o"));
        flujo.setClaveent((String)  procRes.get("pv_claveent_o"));
        flujo.setCdunieco((String)  procRes.get("pv_cdunieco_o"));
        flujo.setCdramo((String)    procRes.get("pv_cdramo_o"));
        flujo.setEstado((String)    procRes.get("pv_estado_o"));
        flujo.setNmpoliza((String)  procRes.get("pv_nmpoliza_o"));
        flujo.setNmsituac((String)  procRes.get("pv_nmsituac_o"));
        flujo.setNmsuplem((String)  procRes.get("pv_nmsuplem_o"));
        flujo.setAux((String)       procRes.get("pv_aux_o"));
        return flujo;
    }
    
    protected class GenerarYRecuperarFlujoRSTN extends StoredProcedure {
        protected GenerarYRecuperarFlujoRSTN (DataSource dataSource) {
            super(dataSource, "P_MC_MOV_FLUJO_RSTN");
            declareParameter(new SqlParameter("pv_ntramite_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuari_i" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i" , OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_status_o"    , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdtipflu_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdflujomc_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_webid_o"     , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_tipoent_o"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_claveent_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdunieco_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_cdramo_o"    , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_estado_o"    , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmpoliza_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsituac_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsuplem_o"  , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_aux_o"       , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"    , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"     , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String obtenerSuplementoTramite(String ntramite)throws Exception{
        Map<String,String> params = new LinkedHashMap<String,String>();
        params.put("ntramite"   , ntramite);;
        Map<String,Object> procRes = ejecutaSP(new ObtenerSuplementoTramite(getDataSource()),params);        
        String cdtipfluSalida = (String)procRes.get("pv_nmsuplem_o");      
        return cdtipfluSalida;
    }
    
    protected class ObtenerSuplementoTramite extends StoredProcedure{
        protected ObtenerSuplementoTramite(DataSource dataSource){
            super(dataSource,"P_GET_NMSUPLEM_X_NTRAMITE");
            declareParameter(new SqlParameter("ntramite"   , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsuplem_o" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void pruebaObjetoTvaloper(TvaloperVO tvaloper) throws Exception {

        logger.debug("tvaloperVO={}", tvaloper);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pv_objeto_i", new SqlStructValue<TvaloperVO>(tvaloper));
        ejecutaSP(new PruebaObjetoTvaloperSP(getDataSource()), params);
    }

    protected class PruebaObjetoTvaloperSP extends StoredProcedure {
        protected PruebaObjetoTvaloperSP (DataSource dataSource) {
            super(dataSource,"PRUEBA_TVALOPER_OBJECT");
            declareParameter(new SqlParameter("pv_objeto_i",     Types.STRUCT, "TVALOPER_OBJECT"));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
}