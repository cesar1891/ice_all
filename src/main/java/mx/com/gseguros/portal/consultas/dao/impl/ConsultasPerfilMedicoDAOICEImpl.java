package mx.com.gseguros.portal.consultas.dao.impl;


import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import mx.com.gseguros.portal.consultas.dao.ConsultasPerfilMedicoDAO;
import mx.com.gseguros.portal.consultas.model.PerfilAseguradoVO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import oracle.jdbc.OracleTypes;


public class ConsultasPerfilMedicoDAOICEImpl extends AbstractManagerDAO implements ConsultasPerfilMedicoDAO
{
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConsultasPerfilMedicoDAOICEImpl.class);
	

    //Consulta de Perfil Medico del Asegurado
  	@Override
  	public List<Map<String,String>> consultaPerfilAsegurados(Map<String,String> params) throws Exception {
  		Map<String,Object>respuesta   = ejecutaSP(new consultaPerfilMedico(getDataSource()),params);
  		PerfilAseguradoVO perfilAsegurado = new PerfilAseguradoVO ();		
  		List<Map<String,String>>lista = (List<Map<String,String>>)respuesta.get("pv_registro_o");
  		return lista;
  	}
  		
  	protected class consultaPerfilMedico extends StoredProcedure {
  		protected consultaPerfilMedico(DataSource dataSource) {
  			super(dataSource, "PKG_PERFIL_MEDICO.P_GET_PERFIL_ASEGURADOS");
  			declareParameter(new SqlParameter("pv_lsperson_i", OracleTypes.VARCHAR));
  			String[] cols = new String[]{
  					"CDPERSON"
  					,"CANT_ICD"
  					,"MAX_PERFIL"
  					,"NUM_PERFIL"
  					,"PERFIL_FINAL"
  			};
  			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
  	   		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
  	   		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
  	   		compile();
  		}
  	}
  	
  //Consulta de ICDs por asegurado
  	@Override
  	public List<Map<String,String>> consultaICDSAsegurado(Map<String,String> params) throws Exception {
  		Map<String,Object>respuesta   = ejecutaSP(new consultaICDS(getDataSource()),params);
  		PerfilAseguradoVO perfilAsegurado = new PerfilAseguradoVO ();		
  		List<Map<String,String>>lista = (List<Map<String,String>>)respuesta.get("pv_registro_o");
  		return lista;
  	}

  	protected class consultaICDS extends StoredProcedure {
  		protected consultaICDS(DataSource dataSource) {
  			super(dataSource, "PKG_PERFIL_MEDICO.P_GET_LISTA_ICDS");
  			declareParameter(new SqlParameter("pv_cdperson_i", OracleTypes.VARCHAR));
  			String[] cols = new String[]{
  				"CDPERSON"
  				,"CDICD"
  				,"DSICD"
  				,"PERFIL"
  			};
  			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR, new GenericMapper(cols)));
  			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
  			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
  			compile();
  		}
  	}
  		
}