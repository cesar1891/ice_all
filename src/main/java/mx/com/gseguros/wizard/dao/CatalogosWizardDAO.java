package mx.com.gseguros.wizard.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.dao.CustomStoredProcedure;
import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.coberturas.model.CoberturaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class CatalogosWizardDAO extends AbstractDAO{
	
	private static Logger logger = Logger.getLogger(CatalogosWizardDAO.class);
	
	public static final String OBTIENE_COBERTURAS = "OBTIENE_COBERTURAS";
	public static final String OBTIENE_CONDICION_COBERTURA = "OBTIENE_CONDICION_COBERTURA";
	public static final String OBTIENE_RAMO_COBERTURA = "OBTIENE_RAMO_COBERTURA";
	public static final String OBTIENE_SUMA_ASEGURADA_COBERTURA = "OBTIENE_SUMA_ASEGURADA_COBERTURA";

	public static final String INSERTA_COBERTURA = "INSERTA_COBERTURA";
	public static final String ASOCIA_COBERTURA = "ASOCIA_COBERTURA";
	public static final String OBTIENE_COBERTURA_ASOCIADA = "OBTIENE_COBERTURA_ASOCIADA";
	
	protected void initDao() throws Exception {
		addStoredProcedure( OBTIENE_COBERTURAS, new ObtieneCoberturas(getDataSource()));
		addStoredProcedure( OBTIENE_CONDICION_COBERTURA, new ObtieneCondicionCobertura(getDataSource()));
		addStoredProcedure( OBTIENE_RAMO_COBERTURA, new ObtieneRamoCobertura(getDataSource()));
		addStoredProcedure( OBTIENE_SUMA_ASEGURADA_COBERTURA, new ObtieneSACobertura(getDataSource()));

		addStoredProcedure( INSERTA_COBERTURA, new InsertaCobertura(getDataSource()));
		addStoredProcedure( ASOCIA_COBERTURA, new AsociaCobertura(getDataSource()));
		addStoredProcedure( OBTIENE_COBERTURA_ASOCIADA, new ObtieneCobAsociada(getDataSource()));
	}
	
	protected class ObtieneCoberturas extends CustomStoredProcedure {
		
		protected ObtieneCoberturas(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_LOADCOBERT");
			
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new CoberturasMapper()));
			declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			compile();
		}
        
		@SuppressWarnings("unchecked")
        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("PV_REGISTRO_O");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
        }
		
	}

    protected class CoberturasMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	LlaveValorVO llaveValorVO = new LlaveValorVO();
        	llaveValorVO.setKey( 	rs.getString("CDGARANT")	);
        	llaveValorVO.setValue(  rs.getString("DSGARANT")   );
        	
            return llaveValorVO;
        }
    }

    protected class ObtieneCondicionCobertura extends CustomStoredProcedure {
    	
    	protected ObtieneCondicionCobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_CONDICCOBERT");
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new CondCobMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class CondCobMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("CDCONDIC")	);
    		llaveValorVO.setValue(  rs.getString("DSCONDIC")   );
    		
    		return llaveValorVO;
    	}
    }

    protected class ObtieneRamoCobertura extends CustomStoredProcedure {
    	
    	protected ObtieneRamoCobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_RAMOCOBERTURAS");
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new RamoCobMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamoCobMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("CDTIPRAM")	);
    		llaveValorVO.setValue(  rs.getString("DSTIPRAM")   );
    		
    		return llaveValorVO;
    	}
    }

    protected class ObtieneSACobertura extends CustomStoredProcedure {
    	
    	protected ObtieneSACobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_SUMASEGCOBERT");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new SACobMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    protected class SACobMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("CDCAPITA")	);
    		llaveValorVO.setValue(  rs.getString("DSCAPITA")   );
    		
    		return llaveValorVO;
    	}
    }


    protected class ObtieneCobAsociada extends CustomStoredProcedure {
    	
    	protected ObtieneCobAsociada(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADCOBERSITREG");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new CobAsoMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class CobAsoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		CoberturaVO cob = new CoberturaVO();
    		cob.setClaveCobertura(rs.getString("CDGARANT"));
    		cob.setDescripcionCobertura(rs.getString("DSGARANT"));
    		cob.setSumaAsegurada(rs.getString("SWCAPITA"));
    		cob.setCodigoCapital(rs.getString("CDCAPITA"));
    		cob.setDescripcionCapital(rs.getString("DSCAPITA"));
    		cob.setObligatorio(rs.getString("SWOBLIGA"));
    		cob.setInserta(rs.getString("SWINSERT"));
    		cob.setCodigoCondicion(rs.getString("CDCONDIC"));
    		cob.setDescripcionCondicion(rs.getString("DSCONDIC"));
    		
    		return cob;
    	}
    }

    protected class InsertaCobertura extends CustomStoredProcedure {
    	
    	protected InsertaCobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_ADD_COBERTURA");
    		
    		declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_DSGARANT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPOGA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPRAM_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWREAUTO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWINFLAC_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class AsociaCobertura extends CustomStoredProcedure {
    	
    	protected AsociaCobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_ASOCIACOBERINCISO");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDCAPITA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDCONDIC_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWINSERT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWOBLIGA_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }
    
}
