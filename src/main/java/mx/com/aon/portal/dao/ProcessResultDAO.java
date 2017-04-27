package mx.com.aon.portal.dao;

import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

/**
 * Created by IntelliJ IDEA.
 * User: gabrielforradellas
 * Date: Oct 13, 2008
 * Time: 1:06:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class ProcessResultDAO extends AbstractDAO {

    private static Logger logger = Logger.getLogger(ProcessResultDAO.class);


    protected void initDao() throws Exception {
        addStoredProcedure("OBTIENE_MENSAJE",new ProcessResultDAO.ObtieneMensaje(getDataSource()));
    }


    protected class ObtieneMensaje extends CustomStoredProcedure {

      protected ObtieneMensaje(DataSource dataSource) {
          super(dataSource, "PKG_TRADUC.P_OBTIENE_MENSAJE");
          declareParameter(new SqlParameter("pv_msg_id_i", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_log_i", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdusuario_i", OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_dsprograma_i", OracleTypes.VARCHAR));

          declareParameter(new SqlOutParameter("pv_msg_text_o", OracleTypes.VARCHAR));
          declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
          compile();
        }


        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultados wrapperResultados = new WrapperResultados();
            wrapperResultados.setMsgText((String)map.get("pv_msg_text_o"));
            wrapperResultados.setMsgTitle((String)map.get("pv_title_o"));
            return wrapperResultados;
        }
    }

}
