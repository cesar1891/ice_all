package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.model.PortalVO;
import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class PaginaPrincipalDAO extends AbstractDAO {

    private static Logger logger = Logger.getLogger(PaginaPrincipalDAO.class);


    protected void initDao() throws Exception {
        addStoredProcedure("CARGA_COMPONENTES",new CargaComponentes(getDataSource()));
        addStoredProcedure("OBTIENE_EMAIL",new ObtieneEmail(getDataSource()));
    }


    protected class CargaComponentes extends CustomStoredProcedure {

      protected CargaComponentes(DataSource dataSource) {
          super(dataSource, "PKG_CONFPAGINA.P_OBTIENE_PAGINA_FINAL");
          declareParameter(new SqlParameter("PV_CDROL_I", OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("PV_CDELEMENTO_I", OracleTypes.VARCHAR));
          declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ComponenteMapper()));
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



    protected class ComponenteMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            PortalVO componente  = new PortalVO();
            componente.setClaveConfigura(rs.getString("CDCONFIGURA"));
            componente.setDescripcionConfigura(rs.getString("DSCONFIGURA"));
            componente.setClaveSeccion(rs.getString("CDSECCION"));
            componente.setClaveElemento(rs.getString("CDELEMENTO"));
            componente.setEspecificacion(rs.getString("DSESPECIFICACION"));
            componente.setContenido(rs.getString("DSCONTENIDO"));
            componente.setClaveTipo(rs.getString("CDTIPO"));
            componente.setDescripcionArchivo(rs.getString("DSARCHIVO"));
            componente.setOtroContenido(rs.getString("DSCONTENIDOSEC"));
            return componente;
        }
    }

    protected class ObtieneEmail extends CustomStoredProcedure {
    	
    	protected ObtieneEmail(DataSource dataSource) {
    		super(dataSource, "PKG_CATBO.P_OBTIENE_EMAIL");
    		declareParameter(new SqlParameter("pv_CDUSUARI_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new EmailMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    
    
    protected class EmailMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		String email = rs.getString("DSEMAIL");
    		return email;
    	}
    }
    

    


}
