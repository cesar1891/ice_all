package mx.com.gseguros.wizard.configuracion.producto.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.dao.CustomStoredProcedure;
import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionVeinticincoAtributosVO;
import mx.com.gseguros.wizard.configuracion.producto.util.WizardUtils;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class TablaCincoClavesDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(TablaCincoClavesDAO.class);

	public static final String OBTIENE_VALORES_CLAVE = "OBTIENE_VALORES_CLAVE";
	public static final String BORRA_VALORES_CLAVE = "BORRA_VALORES_CLAVE";
	public static final String LISTA_CINCO_CLAVES = "LISTA_CINCO_CLAVES";
	public static final String OBTIENE_DESCRIPCIONES_1Y5 = "OBTIENE_DESCRIPCIONES_1Y5";
	
	protected void initDao() throws Exception {
		logger.info("Entrado a init...");
		addStoredProcedure(OBTIENE_VALORES_CLAVE, new ObtenerValoresClave(getDataSource()));
		addStoredProcedure(BORRA_VALORES_CLAVE, new BorraValoresClave(getDataSource()));
		addStoredProcedure(LISTA_CINCO_CLAVES, new ListaCincoClaves(getDataSource()));
		addStoredProcedure(OBTIENE_DESCRIPCIONES_1Y5, new DescripcionesClaves(getDataSource()));
		
		addStoredProcedure("OBTIENE_DESCRIPCION_ATRIBUTOS_CINCO_CLAVES",new DescripAtrCincoClaves(getDataSource()));
    	addStoredProcedure("OBTIENE_VALORES_ATRIBUTOS_CLAVES",new ValoresAtrCincoClaves(getDataSource()));
    	addStoredProcedure("INSERTA_VALORES_CINCO_CLAVES",new InsertaValoresCincoClaves(getDataSource()));
	}

	protected class ObtenerValoresClave extends CustomStoredProcedure {

		protected ObtenerValoresClave(DataSource dataSource) {
			super(dataSource, "PKG_TABAPOYO.P_OBTIENE_VALORES_CLAVES");

			declareParameter(new SqlParameter("PV_NMTABLA_I",
					OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ValoresClaveMapper()));
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
	
	protected class BorraValoresClave extends CustomStoredProcedure {

		protected BorraValoresClave(DataSource dataSource) {
			super(dataSource, "PKG_TABAPOYO.P_BORRA_VALORATT");

			declareParameter(new SqlParameter("pi_tabla", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_clave01", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_clave02", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_clave03", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_clave04", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pi_clave05", OracleTypes.VARCHAR));

	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
        }
	}

    protected class ValoresClaveMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	DescripcionCincoClavesVO clavesVO = new DescripcionCincoClavesVO();
        	clavesVO.setDescripcionClave1(rs.getString("OTCLAVE1"));
        	clavesVO.setDescripcionClave2(rs.getString("OTCLAVE2"));
        	clavesVO.setDescripcionClave3(rs.getString("OTCLAVE3"));
        	clavesVO.setDescripcionClave4(rs.getString("OTCLAVE4"));
        	clavesVO.setDescripcionClave5(rs.getString("OTCLAVE5"));
        	try {
				clavesVO.setFechaDesde(WizardUtils.parseDateBaseCincoClaves(rs.getString("FEDESDE")));
				clavesVO.setFechaHasta(WizardUtils.parseDateBaseCincoClaves(rs.getString("FEHASTA")));
			} catch (ParseException e) {
				logger.error("ERROR al parsear las fechas para la tabla de cinco claves", e);
				clavesVO.setFechaDesde(rs.getString("FEDESDE"));
				clavesVO.setFechaHasta(rs.getString("FEHASTA"));
			}
        	
        	
            return clavesVO;
        }
    }

    protected class ListaCincoClaves extends CustomStoredProcedure {
    	
    	protected ListaCincoClaves(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_CLAVES");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ClaveMapper()));
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
    
    protected class ClaveMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DatosClaveAtributoVO clavesVO = new DatosClaveAtributoVO();
    		clavesVO.setDescripcion(rs.getString("DSCLAVE1"));
    		clavesVO.setFormato(rs.getString("SWFORMA1"));
    		clavesVO.setDescripcionFormato(rs.getString("DSFORMA1"));
    		clavesVO.setMinimo(rs.getString("NMLMIN1"));
    		clavesVO.setMaximo(rs.getString("NMLMAX1"));
    		
    		return clavesVO;
    	}
    }

    protected class DescripcionesClaves extends CustomStoredProcedure {
    	
    	protected DescripcionesClaves(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_ATRIBUTOS");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DesClaveMapper()));
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
    
    protected class DesClaveMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DatosClaveAtributoVO clavesVO = new DatosClaveAtributoVO();
    		clavesVO.setNumeroClave(rs.getInt("CDATRIBU"));
    		clavesVO.setDescripcion(rs.getString("DSATRIBU"));
    		clavesVO.setFormato(rs.getString("SWFORMAT"));
    		clavesVO.setDescripcionFormato(rs.getString("DSFORMAT"));
    		clavesVO.setMinimo(rs.getString("NMLMIN"));
    		clavesVO.setMaximo(rs.getString("NMLMAX"));
    		
    		return clavesVO;
    	}
    }
    
protected class DescripAtrCincoClaves extends CustomStoredProcedure {
    	
    	protected DescripAtrCincoClaves(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_DESC_ATR");
    		declareParameter(new SqlParameter("PI_NMTABLA",OracleTypes.VARCHAR));
 
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new Desc5ClvMapper()));
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
    
    protected class Desc5ClvMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llave = new LlaveValorVO();
    		llave.setKey(rs.getString("CDATRIBU"));
    		llave.setValue(rs.getString("DSATRIBU"));
    		return llave;
    	}
    }

    protected class ValoresAtrCincoClaves extends CustomStoredProcedure {
    	
    	protected ValoresAtrCincoClaves(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_VALORES_ATRIBUTOS");
    		declareParameter(new SqlParameter("PV_NMTABLA_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTCLAVE1_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTCLAVE2_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTCLAVE3_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTCLAVE4_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTCLAVE5_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_FEDESDE_I",OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_FEHASTA_I",OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new Valores5ClvMapper()));
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
    
    protected class Valores5ClvMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DescripcionVeinticincoAtributosVO desc = new DescripcionVeinticincoAtributosVO();
    		desc.setDescripcion1(rs.getString("OTVALOR01"));
    		desc.setDescripcion2(rs.getString("OTVALOR02"));
    		desc.setDescripcion3(rs.getString("OTVALOR03"));
    		desc.setDescripcion4(rs.getString("OTVALOR04"));
    		desc.setDescripcion5(rs.getString("OTVALOR05"));
    		desc.setDescripcion6(rs.getString("OTVALOR06"));
    		desc.setDescripcion7(rs.getString("OTVALOR07"));
    		desc.setDescripcion8(rs.getString("OTVALOR08"));
    		desc.setDescripcion9(rs.getString("OTVALOR09"));
    		desc.setDescripcion10(rs.getString("OTVALOR10"));
    		desc.setDescripcion11(rs.getString("OTVALOR11"));
    		desc.setDescripcion12(rs.getString("OTVALOR12"));
    		desc.setDescripcion13(rs.getString("OTVALOR13"));
    		desc.setDescripcion14(rs.getString("OTVALOR14"));
    		desc.setDescripcion15(rs.getString("OTVALOR15"));
    		desc.setDescripcion16(rs.getString("OTVALOR16"));
    		desc.setDescripcion17(rs.getString("OTVALOR17"));
    		desc.setDescripcion18(rs.getString("OTVALOR18"));
    		desc.setDescripcion19(rs.getString("OTVALOR19"));
    		desc.setDescripcion20(rs.getString("OTVALOR20"));
    		desc.setDescripcion21(rs.getString("OTVALOR21"));
    		desc.setDescripcion22(rs.getString("OTVALOR22"));
    		desc.setDescripcion23(rs.getString("OTVALOR23"));
    		desc.setDescripcion24(rs.getString("OTVALOR24"));
    		desc.setDescripcion25(rs.getString("OTVALOR25"));
    		return desc;
    	}
    }
    
    protected class InsertaValoresCincoClaves extends CustomStoredProcedure {

		protected InsertaValoresCincoClaves(DataSource dataSource) {
			super(dataSource, "PKG_TABAPOYO.P_GUARDA_VALORES");

			  declareParameter(new SqlParameter("PI_TIP_TRAN", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE1", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE2", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE3", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE4", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE5", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_FEDESDE", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_FEHASTA", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE1_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE2_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE3_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE4_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTCLAVE5_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_FEDESDE_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_FEHASTA_ANT", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR01", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR02", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR03", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR04", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR05", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR06", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR07", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR08", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR09", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR10", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR11", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR12", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR13", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR14", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR15", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR16", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR17", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR18", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR19", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR20", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR21", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR22", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR23", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR24", OracleTypes.VARCHAR));
			  declareParameter(new SqlParameter("PI_OTVALOR25", OracleTypes.VARCHAR));
			  
	        declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            return mapper.build(map);
		}
	}
    
}
