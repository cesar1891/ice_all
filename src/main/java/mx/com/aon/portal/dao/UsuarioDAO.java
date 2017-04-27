package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.model.UsuarioRolEmpresaVO;
import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
@Deprecated
public class UsuarioDAO extends AbstractDAO {

    private static Logger logger = Logger.getLogger(UsuarioDAO.class);


    protected void initDao() throws Exception {
        addStoredProcedure("CARGA_ROLES_CLIENTES",new CargaRolesClientes(getDataSource()));
    }


    protected class CargaRolesClientes extends CustomStoredProcedure {

      protected CargaRolesClientes(DataSource dataSource) {
          super(dataSource, "PKG_CONFPAGINA.P_OBTIENE_ROLES_CLIENTE");
          declareParameter(new SqlParameter("PV_CDUSUARIO_I", OracleTypes.VARCHAR));
          declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new UsuarioRolEmpresaMapper()));
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

    protected class UsuarioRolEmpresaMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            UsuarioRolEmpresaVO usuarioRolEmpresaVO  = new UsuarioRolEmpresaVO();
            usuarioRolEmpresaVO.setCdUsuario(rs.getString("CDUSUARIO"));
            usuarioRolEmpresaVO.setCdPerson(rs.getString("CDPERSON"));
            usuarioRolEmpresaVO.setDsUsuario(rs.getString("DSUSUARI"));
            usuarioRolEmpresaVO.setCdElemento(rs.getString("CDELEMENTO"));
            usuarioRolEmpresaVO.setDsElemen(rs.getString("DSELEMEN"));
            usuarioRolEmpresaVO.setCdSisRol(rs.getString("CDSISROL"));
            usuarioRolEmpresaVO.setDsSisRol(rs.getString("DSSISROL"));
            usuarioRolEmpresaVO.setCdUnieco(rs.getString("CDUNIECO"));
            return usuarioRolEmpresaVO;
        }
    }

}
