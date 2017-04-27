package mx.com.gseguros.portal.cotizacion.dao.impl;

import java.util.Map;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.portal.consultas.dao.impl.ConsultasAseguradoDAOSIGSImpl.consultaTelefonoAgenteSP;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasAseguradoDAOSISAImpl.ConsultaDatosTitularSP;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasAseguradoDAOSISAImpl.DatosTitularMapper;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasAseguradoDAOSISAImpl.enviarAvisoHospitalizacionSP;
import mx.com.gseguros.portal.consultas.model.AvisoHospitalizacionVO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosTitularVO;
import mx.com.gseguros.portal.cotizacion.dao.ValidacionesCotizacionDAO;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;

public class ValidacionesCotizacionDAOSIGSImpl extends AbstractManagerDAO implements ValidacionesCotizacionDAO
{
	private final static Logger logger = Logger.getLogger(ValidacionesCotizacionDAOSIGSImpl.class);

	@Override
	public String obtieneValidacionRetroactividad(String numSerie, Date feini) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vSerie", numSerie);
		params.put("vIniVigencia", feini);
		Map<String, Object> mapResult = ejecutaSP(new ObtieneValidacionRetroactividad(getDataSource()), params);
		//return (String) mapResult.get("vLeyenda");
		logger.debug("VAlor de REspuesta -->"+(String) mapResult.get("rs"));
		return (String) mapResult.get("rs");
	}
	
	public class ObtieneValidacionRetroactividad extends StoredProcedure{
		protected ObtieneValidacionRetroactividad(DataSource dataSource){
			super(dataSource, "sp_SerieCanceladaNoPagada");
			declareParameter(new SqlParameter("vSerie", Types.VARCHAR));
			declareParameter(new SqlParameter("vIniVigencia", Types.DATE));
			declareParameter(new SqlReturnResultSet("rs", new ResultSetExtractor<String>(){  
				@Override  
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {  
					String result = null;
					while(rs.next()){  
						result = rs.getString(1);
					}  
					return result;  
				}
			}));
			compile();
		}
	}
			/*declareParameter(new SqlParameter("vSerie", Types.INTEGER));
			declareParameter(new SqlParameter("vIniVigencia", Types.DATE));
			declareParameter(new SqlOutParameter("vLeyenda", Types.VARCHAR));
			compile();
		}
	}*/
	
	/*
	@Override
	public String consultaTelefonoAgente(String cdagente) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cdagente", cdagente);
		Map<String, Object> mapResult = ejecutaSP(new consultaTelefonoAgenteSP(getDataSource()), params);
		return (String) mapResult.get("rs");
	}
	
	public class consultaTelefonoAgenteSP extends StoredProcedure{
		protected consultaTelefonoAgenteSP(DataSource dataSource){
			super(dataSource, "sp_obtener_telefono_agente");
			declareParameter(new SqlParameter("cdagente", Types.INTEGER));
//			declareParameter(new SqlOutParameter("rs", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new ResultSetExtractor<String>(){  
				@Override  
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {  
					String result = null;
					while(rs.next()){  
						result = rs.getString(1);
					}  
					return result;  
				}
			}));
			compile();
		}
	}
	*/
	
}