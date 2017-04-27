package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.model.MenuPrincipalVO;
import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class MenuPrincipalDAO extends AbstractDAO {

    protected void initDao() throws Exception {                
        addStoredProcedure("CARGA_MENU_PRINCIPAL",new ObtieneMenuPrincipal(getDataSource()));
        addStoredProcedure("CARGA_MENU_VERTICAL",new ObtieneMenuVertical(getDataSource()));
    }
 
    
    protected class ObtieneMenuPrincipal extends CustomStoredProcedure {

        protected ObtieneMenuPrincipal(DataSource dataSource) {
            super(dataSource, "PKG_MENU.P_FORMA_MENU");
            
            declareParameter(new SqlParameter("PV_CDELEMENTO_I",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("PV_CDROL_I",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("PV_CDUSUARIO_I",OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new MenuPrincipalMapper()));
            declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("PV_TEXT_O", OracleTypes.VARCHAR));
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
    
    protected class ObtieneMenuVertical extends CustomStoredProcedure {

        protected ObtieneMenuVertical(DataSource dataSource) {
            super(dataSource, "PKG_MENU.P_FORMA_MENU_VERTICAL");
            
            declareParameter(new SqlParameter("pv_cdelemento_i",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdrol_i",OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdusuario_i",OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new MenuPrincipalVerticalMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_text_o", OracleTypes.VARCHAR));
            compile();
          }

        @SuppressWarnings("unchecked")
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
              WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
              WrapperResultados wrapperResultados = mapper.build(map);
              List result = (List) map.get("pv_registro_o");
              wrapperResultados.setItemList(result);
              return wrapperResultados;
          }
    }

    
    protected class MenuPrincipalMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			MenuPrincipalVO menuPrincipalVO = new MenuPrincipalVO();

			menuPrincipalVO.setClaveRamo(rs.getString("CDRAMO"));
			menuPrincipalVO.setClaveTipoSituacion(rs.getString("CDTIPSIT"));
			//menuPrincipalVO.setCls(cls)
			//menuPrincipalVO.setHandler(handler)
			menuPrincipalVO.setHref(rs.getString("DSURL"));
			//menuPrincipalVO.setIcon(icon)
			//menuPrincipalVO.setMenu(menu)
			//menuPrincipalVO.setNavURL(navURL)
			menuPrincipalVO.setNivel(rs.getString("CDNIVEL"));
			menuPrincipalVO.setNivelPadre(rs.getString("CDNIVEL_PADRE"));
			menuPrincipalVO.setText(rs.getString("DSMENU_EST"));
			//menuPrincipalVO.setXtype(xtype)
			menuPrincipalVO.setCdTitulo(rs.getString("CDTITULO"));
			menuPrincipalVO.setSwTipdes(rs.getString("SWTIPDES"));

			return menuPrincipalVO;
		}
    }

    protected class MenuPrincipalVerticalMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			MenuPrincipalVO menuPrincipalVO = new MenuPrincipalVO();

			menuPrincipalVO.setClaveRamo(rs.getString("CDRAMO"));
			menuPrincipalVO.setClaveTipoSituacion(rs.getString("CDTIPSIT"));
			//menuPrincipalVO.setCls(cls)
			//menuPrincipalVO.setHandler(handler)
			menuPrincipalVO.setHref(rs.getString("DSURL"));
			//menuPrincipalVO.setIcon(icon)
			//menuPrincipalVO.setMenu(menu)
			//menuPrincipalVO.setNavURL(navURL)
			menuPrincipalVO.setNivel(rs.getString("CDNIVEL"));
			menuPrincipalVO.setNivelPadre(rs.getString("CDNIVEL_PADRE"));
			menuPrincipalVO.setText(rs.getString("DSMENU_EST"));
			//menuPrincipalVO.setXtype(xtype)
			menuPrincipalVO.setCdTitulo(rs.getString("CDTITULO"));

			return menuPrincipalVO;
		}
    }


}

