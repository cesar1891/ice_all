package mx.com.gseguros.wizard.configuracion.producto.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.dao.CustomStoredProcedure;
import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.incisos.model.IncisoVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class IncisoDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(IncisoDAO.class);
	
	public static final String AGREGAR_INCISO = "AGREGAR_INCISO";
	public static final String OBTIENE_INCISOS = "OBTIENE_INCISOS";
	public static final String OBTIENE_INCISOS_DEL_PRODUCTO = "OBTIENE_INCISOS_DEL_PRODUCTO";
	public static final String ASOCIAR_INCISO = "ASOCIAR_INCISO";
	public static final String ELIMINAR_INCISO = "ELIMINAR_INCISO";
	
	protected void initDao() throws Exception {
		logger.info("Entrado a init en IncisoDAO...");
		addStoredProcedure(AGREGAR_INCISO, new AgregarInciso(getDataSource()));
		addStoredProcedure(OBTIENE_INCISOS, new ObtieneIncisos(getDataSource()));
		addStoredProcedure(OBTIENE_INCISOS_DEL_PRODUCTO, new ObtieneIncisosProducto(getDataSource()));
		addStoredProcedure(ASOCIAR_INCISO, new AsociarInciso(getDataSource()));
		addStoredProcedure(ELIMINAR_INCISO, new EliminarInciso(getDataSource()));
	}
	
	protected class AgregarInciso extends CustomStoredProcedure {
		
		protected AgregarInciso(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.p_insertinciso");
			
			declareParameter(new SqlParameter("P_CDTIPSIT", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("P_DSTIPSIT", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("P_SWSITDEC", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			
			compile();
		}
		
		@SuppressWarnings("unchecked")
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
              WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
              WrapperResultados wrapperResultados = mapper.build(map);
              if (map.get("PO_MSG_ID") != null) {
            	  wrapperResultados.setMsgId( (map.get("PO_MSG_ID")).toString() );
              }
              if (map.get("PV_TITLE_O") != null) {
            	  wrapperResultados.setMsgTitle( (String)map.get("PV_TITLE_O") );
              }
              
              return wrapperResultados;
          }
	}
	
	protected class ObtieneIncisos extends CustomStoredProcedure {
    	
    	protected ObtieneIncisos(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_loadINcisos");
    		
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new IncisosMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class IncisosMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		IncisoVO IncisoVO = new IncisoVO();
    		IncisoVO.setDstipsit(rs.getString("DSTIPSIT"));
    		IncisoVO.setCdtipsit(rs.getString("CDTIPSIT"));
//    		IncisoVO.setSwsitdec(rs.getString("SWSITDEC"));
//    		IncisoVO.setSwobliga(rs.getString("SWOBLIGA"));
//    		IncisoVO.setCdramo(rs.getString("CDRAMO"));
//    		IncisoVO.setSwinsert(rs.getString("SWINSERT"));
//    		IncisoVO.setCdagrupa(rs.getString("CDAGRUPA"));
//    		IncisoVO.setNmsituac(rs.getString("NMSITUAC"));
    		
    		return IncisoVO;
    	}
    }

    protected class ObtieneIncisosProducto extends CustomStoredProcedure {
    	
    	protected ObtieneIncisosProducto(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_buscainciprod");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new IncisosProdMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class IncisosProdMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		IncisoVO IncisoVO = new IncisoVO();
    		IncisoVO.setDstipsit(rs.getString("dstipsit"));
    		IncisoVO.setCdtipsit(rs.getString("cdtipsit"));
//    		IncisoVO.setSwsitdec(rs.getString("SWSITDEC"));
    		IncisoVO.setSwobliga(rs.getString("swobliga"));
    		IncisoVO.setCdramo(rs.getString("cdramo"));
    		IncisoVO.setSwinsert(rs.getString("swinsert"));
    		IncisoVO.setCdagrupa(rs.getString("cdagrupa"));
    		IncisoVO.setNmsituac(rs.getString("nmsituac"));
    		
    		return IncisoVO;
    	}
    }
    
    protected class AsociarInciso extends CustomStoredProcedure {
		
		protected AsociarInciso(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.p_asociarinciso");
			
			declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmsituac_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swobliga_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swinsert_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdagrupa_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dsriesgo_i", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
		}
		
		@SuppressWarnings("unchecked")
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
              WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
              WrapperResultados wrapperResultados = mapper.build(map);
              if (map.get("pv_msg_id_o") != null) {
            	  wrapperResultados.setMsgId( (map.get("pv_msg_id_o")).toString() );
              }
              if (map.get("pv_title_o") != null) {
            	  wrapperResultados.setMsgTitle( (String)map.get("pv_title_o") );
              }
              
              return wrapperResultados;
          }
	}

    protected class EliminarInciso extends CustomStoredProcedure {
    	
    	protected EliminarInciso(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_BORRA_RIESGO_REG");
    		
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		if (map.get("pv_msg_id_o") != null) {
    			wrapperResultados.setMsgId( (map.get("pv_msg_id_o")).toString() );
    		}
    		if (map.get("pv_title_o") != null) {
    			wrapperResultados.setMsgTitle( (String)map.get("pv_title_o") );
    		}
    		
    		return wrapperResultados;
    	}
    }
	
}
