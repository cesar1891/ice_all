
package mx.com.gseguros.wizard.configuracion.producto.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.dao.CustomStoredProcedure;
import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class ListaValoresDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(ListaValoresDAO.class);

	public static final String OBTIENE_CATALOGO_LISTA_VALORES = "OBTIENE_CATALOGO_LISTA_VALORES";
	public static final String OBTIENE_LISTA_CARGA_MANUAL = "OBTIENE_LISTA_CARGA_MANUAL";
	public static final String OBTIENE_LISTA_CLAVES_DEPENDENCIAS = "OBTIENE_LISTA_CLAVES_DEPENDENCIAS";
	public static final String GUARDA_LISTA_DE_VALORES = "GUARDA_LISTA_DE_VALORES";
	public static final String GUARDA_CLAVE_LISTA_DE_VALORES = "GUARDA_CLAVE_LISTA_DE_VALORES";
	public static final String GUARDA_DESCRIPCION_LISTA_DE_VALORES = "GUARDA_DESCRIPCION_LISTA_DE_VALORES";
	public static final String GUARDA_LISTA_CARGA_MANUAL = "GUARDA_LISTA_CARGA_MANUAL";
	public static final String ELIMINA_LISTA_CARGA_MANUAL = "ELIMINA_LISTA_CARGA_MANUAL";
	public static final String OBTIENE_TABLA_1Y5_CLAVES = "OBTIENE_TABLA_1Y5_CLAVES";
	public static final String OBTIENE_CLAVES_1Y5 = "OBTIENE_CLAVES_1Y5";
	public static final String OBTIENE_DESCRIPCIONES_1Y5 = "OBTIENE_DESCRIPCIONES_1Y5";
	public static final String OBTIENE_CLAVE_LISTA_DE_VALORES = "OBTIENE_CLAVE_LISTA_DE_VALORES";
	public static final String OBTIENE_DESCRIPCION_LISTA_DE_VALORES = "OBTIENE_DESCRIPCION_LISTA_DE_VALORES";

	public static final String ELIMINAR_TABLA_CLAVE = "ELIMINAR_TABLA_CLAVE";
	
	
	protected void initDao() throws Exception {
		logger.info("Entrado a init...");
		addStoredProcedure(OBTIENE_CATALOGO_LISTA_VALORES, new ObtenerCatalogoListaValores(getDataSource()));
		addStoredProcedure(OBTIENE_LISTA_CARGA_MANUAL, new ObtenerListaCargaManual(getDataSource()));
		addStoredProcedure(OBTIENE_LISTA_CLAVES_DEPENDENCIAS, new ObtenerListaCvesDependencias(getDataSource()));
		addStoredProcedure(GUARDA_LISTA_DE_VALORES, new GuardaListaValores(getDataSource()));
		addStoredProcedure(GUARDA_CLAVE_LISTA_DE_VALORES, new GuardaCveListaValores(getDataSource()));
		addStoredProcedure(GUARDA_DESCRIPCION_LISTA_DE_VALORES, new GuardaDesListaValores(getDataSource()));
		addStoredProcedure(GUARDA_LISTA_CARGA_MANUAL, new GuardaListaCargaManual(getDataSource()));
		addStoredProcedure(ELIMINA_LISTA_CARGA_MANUAL, new EliminaListaCargaManual(getDataSource()));

		addStoredProcedure(OBTIENE_TABLA_1Y5_CLAVES, new ObtieneTabla1y5(getDataSource()));
		addStoredProcedure(OBTIENE_CLAVES_1Y5, new ObtieneClaves1y5(getDataSource()));
		addStoredProcedure(OBTIENE_DESCRIPCIONES_1Y5, new ObtieneDes1y5(getDataSource()));
		addStoredProcedure(OBTIENE_CLAVE_LISTA_DE_VALORES, new ObtieneClaveListaValores(getDataSource()));
		addStoredProcedure(OBTIENE_DESCRIPCION_LISTA_DE_VALORES, new ObtieneDesListaValores(getDataSource()));

		addStoredProcedure(ELIMINAR_TABLA_CLAVE, new EliminarTablaClave(getDataSource()));
	}

	protected class ObtenerCatalogoListaValores extends CustomStoredProcedure {

		protected ObtenerCatalogoListaValores(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.LOAD_TABLA_APOYO");

			declareParameter(new SqlParameter("PV_OTTIPOTB_I",
					OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("LCUR_REGISTRO_I", OracleTypes.CURSOR, new ListaCatalogoMapper()));
	        declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
			
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("LCUR_REGISTRO_I");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}
	}
	
	protected class ObtenerListaCargaManual extends CustomStoredProcedure {

		protected ObtenerListaCargaManual(DataSource dataSource) {
			super(dataSource, "PKG_TABAPOYO.P_OBTENER_VALORATT_TODO");

			declareParameter(new SqlParameter("PI_TABLA", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_ATRIB_DESC", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_NOM_CLAVE01", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_NOM_CLAVE02", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_VAL_CLAVE02", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_NOM_CLAVE03", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_VAL_CLAVE03", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_NOM_CLAVE04", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_VAL_CLAVE04", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_NOM_CLAVE05", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PI_VAL_CLAVE05", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ListaCargaManualMapper()));
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

    protected class ListaCatalogoMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	ListaDeValoresVO valorVO = new ListaDeValoresVO();
        	valorVO.setCdCatalogo1(rs.getString("CDTABLA"));
        	valorVO.setDsCatalogo1(rs.getString("DSTABLA"));
        	
            return valorVO;
        }
    }
    protected class ListaCargaManualMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	LlaveValorVO valorVO = new LlaveValorVO();
        	valorVO.setKey(rs.getString("OTCLAVE"));
        	valorVO.setValue(rs.getString("OTVALOR"));
        	
            return valorVO;
        }
    }
    
    
    protected class ObtenerListaCvesDependencias extends CustomStoredProcedure {

		protected ObtenerListaCvesDependencias(DataSource dataSource) {
			super(dataSource, "PKG_LISTAS.P_GET_ATRB_TAB_APOYO");

			declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_LANGCODE", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new CvesDepMapper()));
	        declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
	        declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
	        declareParameter(new SqlOutParameter("PV_MSG_TEXT", OracleTypes.VARCHAR));
			
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

    protected class CvesDepMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	LlaveValorVO valorVO = new LlaveValorVO();
        	valorVO.setKey(rs.getString(1));
        	valorVO.setValue(rs.getString(2));
        	
            return valorVO;
        }
    }

    protected class GuardaListaValores extends CustomStoredProcedure {
    	
    	protected GuardaListaValores(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_GUARDA_TABLA");
    		
    		declareParameter(new SqlParameter("PI_CDTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlInOutParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_OTTIPOAC", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_OTTIPOTB", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWMODIFI", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWERROR", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_CLNATURA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_CDTABLJ1", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_CDTABLJ2", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_CDTABLJ3", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    		
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		HashMap<String, Object> items =  new HashMap<String, Object>();
    		items.put("PI_NMTABLA", (String)map.get("PI_NMTABLA"));
    		wrapperResultados.setItemMap(items);
    		return wrapperResultados;
    	}
    }

    protected class GuardaCveListaValores extends CustomStoredProcedure {
    	
    	protected GuardaCveListaValores(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_GUARDA_CLAVES");
    		
    		declareParameter(new SqlParameter("PI_TIP_TRAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSCLAVE1", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWFORMA1", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMIN1", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMAX1", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSCLAVE2", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWFORMA2", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMIN2", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMAX2", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSCLAVE3", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWFORMA3", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMIN3", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMAX3", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSCLAVE4", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWFORMA4", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMIN4", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMAX4", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSCLAVE5", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWFORMA5", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMIN5", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMAX5", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    		
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }

    protected class GuardaDesListaValores extends CustomStoredProcedure {
    	
    	protected GuardaDesListaValores(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_GUARDA_ATRIBUTOS");
    		
    		declareParameter(new SqlParameter("PI_TIP_TRAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_CDATRIBU", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_DSATRIBU", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_SWFORMAT", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMIN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_NMLMAX", OracleTypes.VARCHAR));

    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    		
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }

    protected class GuardaListaCargaManual extends CustomStoredProcedure {
    	
    	protected GuardaListaCargaManual(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_GUARDA_VALORES_UNA");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_OTCLAVE", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_OTVALOR", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PI_ACCION", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    		
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }

    protected class EliminaListaCargaManual extends CustomStoredProcedure {
    	
    	protected EliminaListaCargaManual(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_BORRA_VALORATT");
    		
    		declareParameter(new SqlParameter("pi_tabla", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_clave01", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_clave02", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_clave03", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_clave04", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pi_clave05", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    		
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }
    
    protected class ObtieneTabla1y5 extends CustomStoredProcedure {

		protected ObtieneTabla1y5(DataSource dataSource) {
			super(dataSource, "PKG_TABAPOYO.P_OBTIENE_TABLA");

			declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new T1y5Mapper()));
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

    protected class T1y5Mapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	
        	Map<String,String> map=new LinkedHashMap<String,String>(0);
    		ResultSetMetaData metaData = rs.getMetaData();
    		int numCols=metaData.getColumnCount();
    		for (int i=1;i<=numCols;i++){
    			String col=metaData.getColumnName(i);
    			map.put(col,col);			
    		}
        	ListaDeValoresVO valorVO = new ListaDeValoresVO();
        	
        	if(map.containsKey("NMTABLA")) valorVO.setNumeroTabla(rs.getString("NMTABLA"));
        	if(map.containsKey("CDTABLA")) valorVO.setNombre(rs.getString("CDTABLA"));
        	if(map.containsKey("DSTABLA")) valorVO.setDescripcion(rs.getString("DSTABLA"));
        	if(map.containsKey("CLNATURA")) valorVO.setClnatura(rs.getString("CLNATURA"));
        	if(map.containsKey("DSNATURA")) valorVO.setDsNatura(rs.getString("DSNATURA"));
        	if(map.containsKey("OTTIPOAC")) valorVO.setOttipoac(rs.getString("OTTIPOAC"));
        	if(map.containsKey("SWMODIFI")) valorVO.setModificaValores(rs.getString("SWMODIFI"));
        	if(map.containsKey("SWERROR")) valorVO.setEnviarTablaErrores(rs.getString("SWERROR"));
        	if(map.containsKey("CDTABLJ1")) valorVO.setCdCatalogo1(rs.getString("CDTABLJ1"));
        	if(map.containsKey("CDTABLJ2")) valorVO.setCdCatalogo2(rs.getString("CDTABLJ2"));
        	if(map.containsKey("CDTABLJ3")) valorVO.setClaveDependencia(rs.getString("CDTABLJ3"));
        	if(map.containsKey("DSTABLJ1")) valorVO.setDsCatalogo1(rs.getString("DSTABLJ1"));
        	if(map.containsKey("DSTABLJ2")) valorVO.setDsCatalogo2(rs.getString("DSTABLJ2"));
        	if(map.containsKey("DSCLAVE1")) valorVO.setClave(rs.getString("DSCLAVE1"));
        	if(map.containsKey("SWFORMA1")) valorVO.setFormatoClave(rs.getString("SWFORMA1"));
        	if(map.containsKey("NMLMIN1")) valorVO.setMinimoClave(rs.getString("NMLMIN1"));
        	if(map.containsKey("NMLMAX1")) valorVO.setMaximoClave(rs.getString("NMLMAX1"));
        	if(map.containsKey("CDATRIBU")) valorVO.setCdAtribu(rs.getString("CDATRIBU"));
        	if(map.containsKey("DSATRIBU")) valorVO.setDescripcionFormato(rs.getString("DSATRIBU"));
        	if(map.containsKey("SWFORMAT")) valorVO.setFormatoDescripcion(rs.getString("SWFORMAT"));
        	if(map.containsKey("NMLMIN")) valorVO.setMinimoDescripcion(rs.getString("NMLMIN"));
        	if(map.containsKey("NMLMAX")) valorVO.setMaximoDescripcion(rs.getString("NMLMAX"));
        	
            return valorVO;
        }
    }

    protected class ObtieneClaves1y5 extends CustomStoredProcedure {
    	
    	protected ObtieneClaves1y5(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_CLAVES");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new C1y5Mapper()));
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
    
    protected class C1y5Mapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DatosClaveAtributoVO clave = new DatosClaveAtributoVO();
    		clave.setDescripcion(rs.getString("DSCLAVE1"));
    		clave.setFormato(rs.getString("SWFORMA1"));
    		clave.setDescripcionFormato(rs.getString("DSFORMA1"));
    		clave.setMinimo(rs.getString("NMLMIN1"));
    		clave.setMaximo(rs.getString("NMLMAX1"));
    		
    		return clave;
    	}
    }

    protected class ObtieneClaveListaValores extends CustomStoredProcedure {
    	
    	protected ObtieneClaveListaValores(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_CLAVES");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new T1y5Mapper()));
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
    
    protected class ObtieneDes1y5 extends CustomStoredProcedure {
    	
    	protected ObtieneDes1y5(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_ATRIBUTOS");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new Des1y5Mapper()));
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
    
    protected class Des1y5Mapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		DatosClaveAtributoVO clave = new DatosClaveAtributoVO();
    		clave.setNumeroClave(rs.getInt("CDATRIBU"));
    		clave.setDescripcion(rs.getString("DSATRIBU"));
    		clave.setFormato(rs.getString("SWFORMAT"));
    		clave.setDescripcionFormato(rs.getString("DSFORMAT"));
    		clave.setMinimo(rs.getString("NMLMIN"));
    		clave.setMaximo(rs.getString("NMLMAX"));
    		
    		return clave;
    	}
    }
    

    protected class ObtieneDesListaValores extends CustomStoredProcedure {
    	
    	protected ObtieneDesListaValores(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_OBTIENE_ATRIBUTOS");
    		
    		declareParameter(new SqlParameter("PI_NMTABLA", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new T1y5Mapper()));
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

    protected class EliminarTablaClave extends CustomStoredProcedure {
    	
    	protected EliminarTablaClave(DataSource dataSource) {
    		super(dataSource, "PKG_TABAPOYO.P_BORRA_TABLA");
    		
    		declareParameter(new SqlParameter("pi_nmtabla_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		
    		compile();
    		
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }
    
}
