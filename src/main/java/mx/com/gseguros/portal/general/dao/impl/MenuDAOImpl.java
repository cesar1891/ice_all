package mx.com.gseguros.portal.general.dao.impl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.MenuDAO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class MenuDAOImpl extends AbstractManagerDAO implements MenuDAO {

	private Logger logger = Logger.getLogger(MenuDAOImpl.class);
	
    @Override
    public List<Map<String, String>> obtieneOpcionesLiga(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneOpcionesLiga(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneOpcionesLiga extends StoredProcedure {
    	
    	protected ObtieneOpcionesLiga(DataSource dataSource) {
    		super(dataSource, "PKG_MENU.P_OBTIENE_OPCIONES");
    		declareParameter(new SqlParameter("pv_dstitulo_i", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDTITULO"
    				,"DSTITULO"
    				,"DSURL"
    				,"SWTIPDES"
    				,"DSTIPDES"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @Override
    public List<Map<String, String>> obtieneMenusPorRol(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneMenusPorRol(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneMenusPorRol extends StoredProcedure {
    	
    	protected ObtieneMenusPorRol(DataSource dataSource) {
    		super(dataSource, "PKG_MENU.P_OBTIENE_MENU");
    		declareParameter(new SqlParameter("pv_cdmenu_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_dsmenu_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDMENU" , "DSMENU"    , "CDELEMENTO" , "CDPERSON"
    				,"CDROL" , "CDUSUARIO" , "CDESTADO"   , "CDTIPOMENU"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @Override
    public List<Map<String, String>> obtieneOpcionesMenu(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneOpcionesMenu(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneOpcionesMenu extends StoredProcedure {
    	
    	protected ObtieneOpcionesMenu(DataSource dataSource) {
    		super(dataSource, "PKG_MENU.OPTIENE_MENU_PADRE");
    		declareParameter(new SqlParameter("PV_CDMENU_I", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDNIVEL" , "CDNIVEL_PADRE" , "DSMENU_EST" , "CDTITULO" , "DSTITULO"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }

    @Override
    public List<Map<String, String>> obtieneOpcionesSubMenu(Map params) throws Exception {
    	Map<String, Object> resultado = ejecutaSP(new ObtieneOpcionesSubMenu(getDataSource()), params);
    	return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
    
    protected class ObtieneOpcionesSubMenu extends StoredProcedure {
    	
    	protected ObtieneOpcionesSubMenu(DataSource dataSource) {
    		super(dataSource, "PKG_MENU.OBTIENE_MENU_HIJOS");
    		declareParameter(new SqlParameter("PV_CDMENU_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDNIVEL_PADRE_I", OracleTypes.VARCHAR));
    		String[] cols = new String[]{
    				"CDNIVEL" , "CDNIVEL_PADRE" , "DSMENU_EST" , "CDTITULO" , "DSTITULO"
    		};
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
    
    @Override
	public String guardaOpcionLiga(Map params)
			throws Exception {
		logger.debug("Params guardaOpcionLiga: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new GuardaOpcionLiga(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class GuardaOpcionLiga extends StoredProcedure {

		protected GuardaOpcionLiga(DataSource dataSource) {
			super(dataSource, "PKG_MENU.P_GUARDA_OPCION");
			
			declareParameter(new SqlParameter("pv_cdtitulo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dstitulo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsurl_i", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String guardaMenu(Map params)
			throws Exception {
		logger.debug("Params guardaMenu: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new GuardaMenu(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class GuardaMenu extends StoredProcedure {
		
		protected GuardaMenu(DataSource dataSource) {
			super(dataSource, "PKG_MENU.P_GUARDA_MENU");
			
			declareParameter(new SqlParameter("pv_cdmenu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsmenu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdusuario_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdestado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipomenu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String guardaOpcionMenu(Map params)
			throws Exception {
		logger.debug("Params guardaOpcionMenu: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new GuardaOpcionMenu(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class GuardaOpcionMenu extends StoredProcedure {
		
		protected GuardaOpcionMenu(DataSource dataSource) {
			super(dataSource, "PKG_MENU.P_GUARDA_OPCION_MENU");
			
			declareParameter(new SqlParameter("pv_cdmenu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdnivel_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsmenu_est_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdnivel_padre_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtitulo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdestado_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String eliminaOpcionLiga(Map params)
			throws Exception {
		logger.debug("Params eliminaOpcionLiga: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new EliminaOpcionLiga(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class EliminaOpcionLiga extends StoredProcedure {
		
		protected EliminaOpcionLiga(DataSource dataSource) {
			super(dataSource, "PKG_MENU.P_BORRA_OPCION");
			
			declareParameter(new SqlParameter("pv_cdtitulo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String eliminaMenu(Map params)
			throws Exception {
		logger.debug("Params eliminaMenu: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new EliminaMenu(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class EliminaMenu extends StoredProcedure {
		
		protected EliminaMenu(DataSource dataSource) {
			super(dataSource, "PKG_MENU.P_ELIMINA_MENU");
			
			declareParameter(new SqlParameter("pv_cdmenu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
			compile();
		}
	}

	@Override
	public String eliminaOpcionMenu(Map params)
			throws Exception {
		logger.debug("Params eliminaOpcionMenu: "+ params);
		Map<String, Object> mapResult = ejecutaSP(new EliminaOpcionMenu(getDataSource()), params);
		
		return (String) mapResult.get("pv_title_o");
	}
	
	protected class EliminaOpcionMenu extends StoredProcedure {
		
		protected EliminaOpcionMenu(DataSource dataSource) {
			super(dataSource, "PKG_MENU.P_ELIMINA_OPCION");
			
			declareParameter(new SqlParameter("pv_cdmenu_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdnivel_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
			compile();
		}
	}
    
}