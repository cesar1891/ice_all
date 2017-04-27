package mx.com.aon.portal.dao;

import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class PrincipalDAO extends AbstractDAO {
	protected void initDao () throws Exception {
		addStoredProcedure("GUARDAR_NUEVA_CONFIGURACION", new AgregarNuevaConfiguracion(getDataSource()));
		addStoredProcedure("GUARDA_CONFIGURACION", new EditarNuevaConfiguracion(getDataSource()));
		addStoredProcedure("VALIDAR_CONFIGURACION_COMPLETA", new ValidarConfiguracionCompleta(getDataSource()));
	}
	
	protected class AgregarNuevaConfiguracion extends CustomStoredProcedure {
		protected AgregarNuevaConfiguracion (DataSource dataSource) {
			super(dataSource, "PKG_CONFPAGINA.P_INSERTA_CONFIGURA");
			
			declareParameter(new SqlParameter("pv_cdconfig_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsconfig_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdseccion_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swhabilitado_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsespecificacion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dscontenido_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsarchivo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dscontenidosec_i", OracleTypes.VARCHAR));

			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
		@SuppressWarnings("unchecked")
		public WrapperResultados mapWrapperResultados (Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados res = mapper.build(map);

			return res;
		}
	}
	
	protected class EditarNuevaConfiguracion extends CustomStoredProcedure {
		protected EditarNuevaConfiguracion (DataSource dataSource) {
			super (dataSource, "PKG_CONFPAGINA.P_GUARDA_CONFIGURA");
			
			declareParameter(new SqlParameter("pv_cdconfig_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsconfig_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdseccion_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swhabilitado_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsespecificacion_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dscontenido_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipo_i", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsarchivo_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dscontenidosec_i", OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_act_padre_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_act_hijo_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
		
		public WrapperResultados mapWrapperResultados (Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric ();
			WrapperResultados res = mapper.build(map);
			/*if (!map.get("pv_act_padre_o").toString().equals("")) {
				res.setResultado(map.get("pv_act_padre_o").toString());
			}
			if (!map.get("pv_msg_id_o").toString().equals("")){
				res.setResultado(map.get("pv_msg_id_o").toString());
			}*/
			return res;
		}
	}

	/**
	 * Valida si el usuario ha completado la configuraci&oacute;n
	 * 
	 * @author Usuario
	 *
	 */
	protected class ValidarConfiguracionCompleta extends CustomStoredProcedure {
		protected ValidarConfiguracionCompleta (DataSource dataSource) {
			super (dataSource, "PKG_AON_CHECKLIST.P_ENTRADA_COMPLETA");
			
			declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();
		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados res = mapper.build(map);

			return res;
		}
		
	}
}
