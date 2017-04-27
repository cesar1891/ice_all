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
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaIncisoVO;
import mx.com.gseguros.wizard.configuracion.producto.sumaAsegurada.model.SumaAseguradaVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class SumaAseguradaDAO extends AbstractDAO{
	
    private static Logger logger = Logger.getLogger(SumaAseguradaDAO.class);


    protected void initDao() throws Exception {
    	addStoredProcedure("AGREGAR_SUMA_ASEGURADA_INCISO_JDBC_TMPL",new GuardaSumaAseguradaInciso(getDataSource()));
    	addStoredProcedure("CATALOGO_TIPO_SUMA_ASEGURADA",new CatalogoTipoSumaAseg(getDataSource()));
    	addStoredProcedure("CATALOGO_SUMA_ASEGURADA",new CatalogoSumaAseg(getDataSource()));
    	addStoredProcedure("CATALOGO_MONEDA_SUMA_ASEGURADA",new CatalogoMonedaSumaAseg(getDataSource()));
    	addStoredProcedure("LISTA_SUMAS_ASEGURADAS",new ListaSumasAseg(getDataSource()));
    	addStoredProcedure("LISTA_SUMAS_ASEGURADAS_INCISO",new ListaSumasAsegInciso(getDataSource()));

    	addStoredProcedure("ELIMINAR_SUMA_ASEGURADA_PRODUCTO",new EliminaSumaAseguradaProducto(getDataSource()));
    	addStoredProcedure("ELIMINAR_SUMA_ASEGURADA_INCISO",new EliminaSumaAseguradaInciso(getDataSource()));
    	
    }

    
    protected class GuardaSumaAseguradaInciso extends CustomStoredProcedure {

        protected GuardaSumaAseguradaInciso(DataSource dataSource) {
            super(dataSource, "PKG_WIZARD.P_INSERTA_SUMA_ASEGURADA");
            declareParameter(new SqlParameter("PV_CDRAMO_I",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PV_CDCAPITA_I",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PV_CDTIPCAP_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_DSCAPITA_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDGARANT_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDPRESEN_I",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PV_CDTIPSIT_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_OTTABVAL_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_SWREAUTO_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdexpdef_i",OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_CDCAPITA_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            //wrapperResultados.setResultado((map.get("PV_CDCAPITA_O")).toString());
            if(logger.isDebugEnabled()){
            	logger.debug("P_INSERTA_SUMA_ASEGURADA PV_CDCAPITA_O=" + map.get("PV_CDCAPITA_O"));
            	logger.debug("P_INSERTA_SUMA_ASEGURADA PV_MSG_ID_O=" + map.get("PV_MSG_ID_O"));
            	logger.debug("P_INSERTA_SUMA_ASEGURADA PV_TITLE_O=" + map.get("PV_TITLE_O"));
            }
            return wrapperResultados;
        }
    }
    
    protected class SumaAsegMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llave = new LlaveValorVO();
    		llave.setKey(rs.getString(1));
    		llave.setValue(rs.getString(2));
    		return llave;
    	}
    }

    protected class CatalogoTipoSumaAseg extends CustomStoredProcedure {

        protected CatalogoTipoSumaAseg(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_TIPO_SUMA");
            
            declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new SumaAsegMapper()));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            compile();
          }
        
        @SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class CatalogoSumaAseg extends CustomStoredProcedure {

        protected CatalogoSumaAseg(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_CAPITAL");
            
            declareParameter(new SqlParameter("PV_CDRAMO_I",OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new SumaAsegMapper()));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class CatalogoMonedaSumaAseg extends CustomStoredProcedure {

        protected CatalogoMonedaSumaAseg(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_MONEDAS");
            declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new SumaAsegMapper()));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }

    protected class ListaSumasAseg extends CustomStoredProcedure {

        protected ListaSumasAseg(DataSource dataSource) {
            super(dataSource, "PKG_WIZARD.P_OBTIENE_MCAPITAL");
            declareParameter(new SqlParameter("PV_CDRAMO_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDCAPITA_I",OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ListaSumaAsegMapper()));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class ListaSumaAsegMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		SumaAseguradaVO suma = new SumaAseguradaVO();
    		suma.setCodigoRamo(rs.getString("CDRAMO"));
    		suma.setCodigoCapital(rs.getString("CDCAPITA"));
    		suma.setCodigoTipoCapital(rs.getString("CDTIPCAP"));
    		suma.setDescripcionCapital(rs.getString("DSCAPITA"));
    		suma.setCodigoMoneda(rs.getString("CDMONEDA"));
    		suma.setDescripcionMoneda(rs.getString("DSMONEDA"));
    		suma.setDescripcionTipoCapital(rs.getString("DSTIPCAP"));
    		
    		return suma;
    	}
    }

    protected class ListaSumasAsegInciso extends CustomStoredProcedure {

        protected ListaSumasAsegInciso(DataSource dataSource) {
            super(dataSource, "PKG_WIZARD.P_OBTIENE_SUMA_ASEGURADA");
            declareParameter(new SqlParameter("PV_CDRAMO_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDGARANT_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDCAPITA_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDTIPSIT_I",OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ListaSumaAsegIncisoMapper()));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("PV_REGISTRO_O");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    protected class ListaSumaAsegIncisoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		SumaAseguradaIncisoVO suma = new SumaAseguradaIncisoVO();
    		suma.setCodigoRamo(rs.getString("CDRAMO"));
    		suma.setCodigoCapital(rs.getString("CDCAPITA"));
    		suma.setDescripcionCapital(rs.getString("DSCAPITA"));
    		suma.setCodigoTipoCapital(rs.getString("CDTIPCAP"));
    		suma.setDescripcionTipoCapital(rs.getString("DSTIPCAP"));
    		suma.setCodigoCobertura(rs.getString("CDGARANT"));
    		suma.setDescripcionCobertura(rs.getString("DSGARANT"));
    		suma.setCodigoLeyenda(rs.getString("CDPRESEN"));
    		suma.setDescripcionLeyenda(rs.getString("LEYENDA"));
    		suma.setCodigoTipoSituacion(rs.getString("CDTIPSIT"));
//    		suma.setdesc(rs.getString("DSTIPSIT"));
    		suma.setSwitchReinstalacion(rs.getString("SWREAUTO"));
    		suma.setCodigoListaValor(rs.getString("OTTABVAL"));
    		suma.setDescripcionListaValor(rs.getString("DSTABLA"));
    		suma.setCodigoExpresion(rs.getString("CDEXPDEF"));
    		return suma;
    	}
    }
    
    protected class EliminaSumaAseguradaProducto extends CustomStoredProcedure {

        protected EliminaSumaAseguradaProducto(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_BORRA_SUMA");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDCAPITA_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }

    protected class EliminaSumaAseguradaInciso extends CustomStoredProcedure {
    	
    	protected EliminaSumaAseguradaInciso(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_BORRA_VALORES_SUMA");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDCAPITA_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }

    
    
}
