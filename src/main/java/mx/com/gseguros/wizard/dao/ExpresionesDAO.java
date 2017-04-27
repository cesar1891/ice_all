package mx.com.gseguros.wizard.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.dao.CustomStoredProcedure;
import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ClaveVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ComposerVariableVOClaveVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.HojaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.VariableVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class ExpresionesDAO extends AbstractDAO{
	
	private static Logger logger = Logger.getLogger(ExpresionesDAO.class);
	
	public static final String OBTIENE_TABLA_CINCO_CLAVES = "OBTIENE_TABLA_CINCO_CLAVES";
	public static final String OBTIENE_SECUENCIA_EXPRESION = "OBTIENE_SECUENCIA_EXPRESION";
	public static final String INSERTAR_LISTA_VARIABLES = "INSERTAR_LISTA_VARIABLES";
	public static final String INSERTAR_EXPRESION = "INSERTAR_EXPRESION";
	public static final String INSERTAR_LISTA_CLAVES = "INSERTAR_LISTA_CLAVES";
	public static final String ELIMINAR_VARIABLE_LOCAL_EXPRESION = "ELIMINAR_VARIABLE_LOCAL_EXPRESION";
	public static final String LISTA_FUNCIONES = "LISTA_FUNCIONES";
	public static final String LISTA_VARIABLES_TEMPORALES = "LISTA_VARIABLES_TEMPORALES";
	public static final String LISTA_COLUMNA = "LISTA_COLUMNA";
	public static final String LISTA_CLAVE = "LISTA_CLAVE";
	public static final String CLAVES_VARIABLE_LOCAL = "CLAVES_VARIABLE_LOCAL";
	public static final String OBTIENE_EXPRESION = "OBTIENE_EXPRESION";
	public static final String OBTIENE_VARIABLE_EXPRESION = "OBTIENE_VARIABLE_EXPRESION";
	public static final String VALIDA_EXPRESION = "VALIDA_EXPRESION";
	
	protected void initDao() throws Exception {
		addStoredProcedure( OBTIENE_TABLA_CINCO_CLAVES, new BuscarValoresTablas(getDataSource()));
		addStoredProcedure( OBTIENE_SECUENCIA_EXPRESION, new ObtieneSecuenciaExpresion(getDataSource()));
		addStoredProcedure( INSERTAR_LISTA_VARIABLES, new InsertarListaVariables(getDataSource()));
		addStoredProcedure( INSERTAR_EXPRESION, new InsertarExpresion(getDataSource()));
		addStoredProcedure( INSERTAR_LISTA_CLAVES, new InsertarListaClaves(getDataSource()));
		addStoredProcedure( ELIMINAR_VARIABLE_LOCAL_EXPRESION, new EliminarVaribaleLocalExpresion(getDataSource()));
		addStoredProcedure( LISTA_FUNCIONES, new ListaFunciones(getDataSource()));
		addStoredProcedure( LISTA_VARIABLES_TEMPORALES, new ListaVarTemp(getDataSource()));
		addStoredProcedure( LISTA_COLUMNA, new ListaColumna(getDataSource()));
		addStoredProcedure( LISTA_CLAVE, new ListaClave(getDataSource()));
		addStoredProcedure( CLAVES_VARIABLE_LOCAL, new ClavesVarLocal(getDataSource()));
		addStoredProcedure( OBTIENE_EXPRESION, new ObtieneExpresion(getDataSource()));
		addStoredProcedure( OBTIENE_VARIABLE_EXPRESION, new ObtieneVarExpresion(getDataSource()));
		addStoredProcedure( VALIDA_EXPRESION, new ValidaExpresion(getDataSource()));
	}
	
	protected class BuscarValoresTablas extends CustomStoredProcedure {
		
		protected BuscarValoresTablas(DataSource dataSource) {
			super(dataSource, "PKG_LISTAS.P_TABLAS");
			
			logger.debug("%%% Entro a m�todo BuscarValoresTablas sin par�metros para ejecutar PKG_LISTAS.P_TABLAS %%%");
		
			declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ValoresTablaMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
			compile();
		}
        
		@SuppressWarnings("unchecked")
        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
        }
		
	}

    protected class ValoresTablaMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	LlaveValorVO llaveValorVO = new LlaveValorVO();
        	
        	llaveValorVO.setKey( 	rs.getString("CDTABLA")	);
        	llaveValorVO.setValue(  rs.getString("TABLA")   );
        	llaveValorVO.setNick(	rs.getString("NMTABLA")	);
        	
            return llaveValorVO;
        }
    }

    protected class ListaFunciones extends CustomStoredProcedure {
    	
    	protected ListaFunciones(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_TFUNCDES");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListaFuncionesMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ListaFuncionesMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		HojaVO hoja = new HojaVO();
    		
    		hoja.setId(rs.getString(2));
    		hoja.setText(rs.getString(2));
    		hoja.setFuncion(rs.getString(3));
    		hoja.setDescripcion(rs.getString(4));
    		
    		return hoja;
    	}
    }

    protected class ListaVarTemp extends CustomStoredProcedure {
    	
    	protected ListaVarTemp(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENE_TVARIATM");
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListaVarTempMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ListaVarTempMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		HojaVO hoja = new HojaVO();
    		
    		hoja.setId(rs.getString(2));
    		hoja.setText(rs.getString(2));
    		hoja.setFuncion(rs.getString(1));
    		
    		return hoja;
    	}
    }

    protected class ListaColumna extends CustomStoredProcedure {
    	
    	protected ListaColumna(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_COLUMNAS");
    		
    		declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListaColumnaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ListaColumnaMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	LlaveValorVO llaveValorVO = new LlaveValorVO();
        	
        	llaveValorVO.setKey( 	rs.getString(1)	);
        	llaveValorVO.setValue(  rs.getString(2)   );
        	llaveValorVO.setNick(	rs.getString(3)	);
        	
            return llaveValorVO;
        }
    }

    protected class ListaClave extends CustomStoredProcedure {
    	
    	protected ListaClave(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_LLAVES");
    		
    		declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListaClaveMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ListaClaveMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ClaveVO llaveValorVO = new ClaveVO();
    		
    		llaveValorVO.setCodigoSecuencia(rs.getString("CLAVE"));
    		llaveValorVO.setClave(rs.getString("DSCLAVE1"));
    		
    		return llaveValorVO;
    	}
    }

    protected class ClavesVarLocal extends CustomStoredProcedure {
    	
    	protected ClavesVarLocal(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_GETTABCOLUMBYVAR");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDVARIAB_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTSELECT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ClavesVarLocalMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ClavesVarLocalMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ClaveVO llaveValorVO = new ClaveVO();
    		
    		llaveValorVO.setCodigoExpresionKey(rs.getString("CDEXPRES_KEY"));
    		llaveValorVO.setCodigoVariable(rs.getString("CDVARIAB"));
    		
    		return llaveValorVO;
    	}
    }
    
    protected class ObtieneExpresion extends CustomStoredProcedure {
    	
    	protected ObtieneExpresion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADEXPRES");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.INTEGER));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ExpresionMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List<LlaveValorVO> result = (List<LlaveValorVO>) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ExpresionMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ExpresionVO llaveValorVO = new ExpresionVO();
    		
    		llaveValorVO.setCodigoExpresion(rs.getInt("CDEXPRES"));
    		llaveValorVO.setOtExpresion(rs.getString("OTEXPRES"));
    		llaveValorVO.setSwitchRecalcular(rs.getString("SWRECALC"));
    		
    		return llaveValorVO;
    	}
    }
    
    protected class ObtieneVarExpresion extends CustomStoredProcedure {
    	
    	protected ObtieneVarExpresion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_GETTABCOLUMBYVAR");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.INTEGER));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new VarExpresionMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		
	    	try {
	    			
	    		List<ComposerVariableVOClaveVO> lstComposerVariableClave = (List<ComposerVariableVOClaveVO>) map.get("pv_registro_o");
	    		// Lista que contendr� el agrupamiento final de todos los elementos:
	    		List<VariableVO> listaFinalVariableVO = new ArrayList<VariableVO>();
	    		List<ClaveVO> listaTmpClaveVO = new ArrayList<ClaveVO>();
	    		for(ComposerVariableVOClaveVO composer : lstComposerVariableClave) {
	    			//Llenamos la lista de VariableVO sin repetir:
	    			if(composer.getVariableVO() != null 
	    					&& !listaFinalVariableVO.contains( composer.getVariableVO() )) {
	    				listaFinalVariableVO.add( composer.getVariableVO() );
	    			}
	    			//Creamos la lista de ClaveVO sin repetir:
	    			if(composer.getClaveVO() != null
	    					&& !listaTmpClaveVO.contains( composer.getClaveVO() )) {
	    				listaTmpClaveVO.add( composer.getClaveVO() );
	    			}
	    			
	    		}
	    		// Se agrupan y agregan los objetos ClaveVO como elementos hijos de los objetos VariableVO:
	    		for(ComposerVariableVOClaveVO composer : lstComposerVariableClave) {
	    			
	    			ClaveVO cveVO = composer.getClaveVO();
	    			int indexVarVO = listaFinalVariableVO.indexOf( composer.getVariableVO() );
	    			VariableVO varVO = listaFinalVariableVO.get( indexVarVO );
	    			// Si el elemento no existe en la lista, lo agregamos
					if(cveVO != null) {
						if( varVO.getClaves() != null && !varVO.getClaves().contains(cveVO) ) {
							listaFinalVariableVO.get( indexVarVO ).getClaves().add(cveVO);
						} else {
							List<ClaveVO> lstCveVO = new ArrayList<ClaveVO>();
							lstCveVO.add(cveVO);
							listaFinalVariableVO.get( indexVarVO ).setClaves(lstCveVO);
						}
					}
	    		}
	    		
				wrapperResultados.setItemList(listaFinalVariableVO);
				
	    	} catch(Exception e) {
    			logger.error(e.getMessage(), e);
    			throw e;
    		}
	    	return wrapperResultados;
    	}
    }
    
    protected class VarExpresionMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		
    		ComposerVariableVOClaveVO composerVariableClave = new ComposerVariableVOClaveVO();
    		
    		VariableVO varVO = new VariableVO();
    		varVO.setCodigoExpresion(rs.getInt("CDEXPRES"));
    		varVO.setCodigoVariable(rs.getString("CDVARIAB"));
    		varVO.setTabla(rs.getString("OTTABLA"));
    		varVO.setDescripcionTabla(rs.getString("DSTABLA"));
    		varVO.setDescripcionColumna(rs.getString("OTSELECT"));
    		varVO.setSwitchFormato(rs.getString("SWFORMAT"));
    		
    		composerVariableClave.setVariableVO(varVO);
    		
    		ClaveVO claveVO = new ClaveVO();
    		claveVO.setCodigoExpresionKey(rs.getString("CDEXPRES_KEY"));
    		claveVO.setClave(rs.getString("DSCLAVE"));
    		claveVO.setCodigoSecuencia(rs.getString("OTSECUEN"));
    		claveVO.setExpresion(rs.getString("OTEXPRES"));
    		claveVO.setRecalcular(rs.getString("SWRECALC"));
    		
    		composerVariableClave.setClaveVO(claveVO);
    		
    		return composerVariableClave;
    	}
    }

    protected class ObtieneSecuenciaExpresion extends CustomStoredProcedure {
    	
    	protected ObtieneSecuenciaExpresion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTIENE_CDEXPRES");
    		
    		logger.debug("%%% Entro a m�todo BuscarValoresTablas sin par�metros para ejecutar PKG_LISTAS.P_TABLAS %%%");
    		
    		declareParameter(new SqlOutParameter("PV_SEC_CDEXPRES", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		HashMap<String,Object> items =  new HashMap<String,Object>(); 
    		items.put("PV_SEC_CDEXPRES", (String)map.get("PV_SEC_CDEXPRES"));
    		wrapperResultados.setItemMap(items);
    		return wrapperResultados;
    	}
    	
    }

    protected class InsertarListaVariables extends CustomStoredProcedure {
    	
    	protected InsertarListaVariables(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_inserttvariabd");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDVARIAB_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTSELECT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWFORMAT_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class InsertarListaClaves extends CustomStoredProcedure {
    	
    	protected InsertarListaClaves(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_inserttvabdkey");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDVARIAB_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWRECALC_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTSECUEN_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTTIPORG_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTTIPEXP_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdexpres_key_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class InsertarExpresion extends CustomStoredProcedure {
    	
    	protected InsertarExpresion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_INSERTEXPRESI");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_OTEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWRECALC_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_SEC_CDEXPRES", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_OTTIPORG_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_OTTIPEXP_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		
    		WrapperResultados wrapperResultados = mapper.build(map);
    		HashMap<String,Object> items =  new HashMap<String,Object>(); 
    		items.put("PV_SEC_CDEXPRES", (String)map.get("PV_SEC_CDEXPRES"));
    		wrapperResultados.setItemMap(items);
    		return wrapperResultados;
    	}
    	
    }

    protected class EliminarVaribaleLocalExpresion extends CustomStoredProcedure {
    	
    	protected EliminarVaribaleLocalExpresion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_BORRAR_VARIABLE_LOCAL");
    		
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDVARIAB_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class ValidaExpresion extends CustomStoredProcedure
    {
    	
    	protected ValidaExpresion(DataSource dataSource)
    	{
    		super(dataSource, "PKG_WIZARD.P_PARSEADOR");
    		
    		declareParameter(new SqlParameter("pv_expresion_i"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdexpres_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ottipexp_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ottiporg_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otlength_i"   , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_otdepth_i"    , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_lang_code_o"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdvariable_i" , OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_msg_text_o"   , OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception
    	{
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }
    
}
