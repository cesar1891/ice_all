package mx.com.gseguros.portal.catalogos.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.ObtieneTatriperMapper;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.catalogos.dao.ClienteDAO;
import mx.com.gseguros.portal.catalogos.dao.PersonasDAO;
import mx.com.gseguros.portal.cotizacion.dao.impl.ValidacionesCotizacionDAOSIGSImpl.ObtieneValidacionRetroactividad;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Utils;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.core.RowMapper;

public class ClienteDAOSIGSImpl extends AbstractManagerDAO implements ClienteDAO
{
	
	private static final Logger logger = Logger.getLogger(ClienteDAOSIGSImpl.class);

	@Override
	public String obtieneInformacionCliente(String sucursal, String ramo, String poliza) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vSucursal", sucursal);
		params.put("vRamo", ramo);
		params.put("vPoliza", poliza);
		Map<String, Object> mapResult = ejecutaSP(new ObtieneInformacionCliente(getDataSource()), params);
		//return (String) mapResult.get("vLeyenda");
		logger.debug("VAlor de REspuesta -->"+(String) mapResult.get("rs"));
		return (String) mapResult.get("rs");
	}
	
	public class ObtieneInformacionCliente extends StoredProcedure{
		protected ObtieneInformacionCliente(DataSource dataSource){
			super(dataSource, "sp_EndosoBClientesSIGS");
			declareParameter(new SqlParameter("vSucursal", Types.VARCHAR));
			declareParameter(new SqlParameter("vRamo", Types.VARCHAR));
			declareParameter(new SqlParameter("vPoliza", Types.VARCHAR));
			declareParameter(new SqlReturnResultSet("rs", new ResultSetExtractor<String>(){  
				@Override  
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {  
					String result = null;
					//logger.debug("Valor de Respuesta "+rs);
					while(rs.next()){  
						result = rs.getString(1)+"|"+rs.getString(2);
					}  
					return result;  
				}
			}));
			compile();
		}
	}
}