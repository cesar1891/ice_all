package mx.com.gseguros.wizard.configuracion.producto.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.dao.AbstractDAO;
import mx.com.aon.portal.dao.CustomStoredProcedure;
import mx.com.aon.portal.dao.WrapperResultadosGeneric;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ClausulaVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.PeriodoVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ProductoVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class ProductoDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(ProductoDAO.class);

	public static final String CLONAR_PRODUCTO = "CLONAR_PRODUCTO";
	public static final String GENERAR_PRODUCTO = "GENERAR_PRODUCTO";
	public static final String INSERTAR_PRODUCTO = "INSERTAR_PRODUCTO";
	public static final String BUSCAR_PRODUCTOS = "BUSCAR_PRODUCTOS";
	public static final String AGREGAR_CLAUSULA = "AGREGAR_CLAUSULA";	
	public static final String LISTA_CLAUSULAS = "LISTA_CLAUSULAS";	
	public static final String OBTIENE_TABLA_APOYO = "OBTIENE_TABLA_APOYO";	
	public static final String LISTA_TIPO_PRODUCTO = "LISTA_TIPO_PRODUCTO";	
	public static final String LISTA_TIPO_RAMO = "LISTA_TIPO_RAMO";	

	/***
	 * Para endpoints de nivelesProducto
	 */
	public static final String OBTENER_RAMA_ATRIBUTOS_PRODUCTO = "OBTENER_RAMA_ATRIBUTOS_PRODUCTO";	
	public static final String OBTENER_ATRIBUTOS_SITUACION = "OBTENER_ATRIBUTOS_SITUACION";	
	public static final String OBTENER_ATRIBUTOS_GARANTIA = "OBTENER_ATRIBUTOS_GARANTIA";	
	public static final String OBTENER_RAMA_CONCEPTOS_GLOBALES_PRODUCTO = "OBTENER_RAMA_CONCEPTOS_GLOBALES_PRODUCTO";	
	public static final String OBTENER_RAMA_SITUACION = "OBTENER_RAMA_SITUACION";	
	public static final String OBTENER_RAMA_DATOS_FIJOS_PRODUCTO = "OBTENER_RAMA_DATOS_FIJOS_PRODUCTO";	
	public static final String OBTENER_RAMA_ROLES_PRODUCTO = "OBTENER_RAMA_ROLES_PRODUCTO";	
	public static final String OBTENER_GARANTIA = "OBTENER_GARANTIA";	
	public static final String OBTIENE_LISTA_PLANES_RAMAS = "OBTIENE_LISTA_PLANES_RAMAS";	
	public static final String OBTENER_OBJETOS = "OBTENER_OBJETOS";	
	
	public static final String EDITAR_PRODUCTO_WIZARD = "EDITAR_PRODUCTO_WIZARD";	
	public static final String LISTA_RAMO_POLIZA = "LISTA_RAMO_POLIZA";	
	public static final String LISTA_CLAUSULAS_ASOCIADAS = "LISTA_CLAUSULAS_ASOCIADAS";	
	public static final String LISTA_PERIODOS = "LISTA_PERIODOS";	
	public static final String OBTIENE_LISTA_COBERTURAS_PLANES_CONFIGURACION = "OBTIENE_LISTA_COBERTURAS_PLANES_CONFIGURACION";

	public static final String OBTIENE_LISTA_PLANES_CONFIGURACION = "OBTIENE_LISTA_PLANES_CONFIGURACION";
	public static final String GUARDA_PLAN_CONFIGURACION = "GUARDA_PLAN_CONFIGURACION";
	public static final String EDITA_PLAN_CONFIGURACION = "EDITA_PLAN_CONFIGURACION";
	public static final String ELIMINA_PLAN_CONFIGURACION = "ELIMINA_PLAN_CONFIGURACION";

	public static final String GUARDA_COBERTURA_PLAN_CONFIGURACION = "GUARDA_COBERTURA_PLAN_CONFIGURACION";
	public static final String MODIFICA_COBERTURA_PLAN = "MODIFICA_COBERTURA_PLAN";
	public static final String BORRAR_PLANPRO = "BORRAR_PLANPRO";

	public static final String ELIMINAR_TIPO_POLIZA = "ELIMINAR_TIPO_POLIZA";
	public static final String INSERTAR_TIPO_POLIZA = "INSERTAR_TIPO_POLIZA";
	public static final String INSERTAR_PERIODOS = "INSERTAR_PERIODOS";
	public static final String INSERTAR_CLAUSULAS = "INSERTAR_CLAUSULAS";

	public static final String LISTA_PRODUCTOS = "LISTA_PRODUCTOS";
	
	
	protected void initDao() throws Exception {
		logger.info("Entrado a init...");
		addStoredProcedure(CLONAR_PRODUCTO, new ClonarProducto(getDataSource()));
		addStoredProcedure(GENERAR_PRODUCTO, new GenerarProducto(getDataSource()));
		addStoredProcedure(INSERTAR_PRODUCTO, new InsertarProcucto(getDataSource()));
		addStoredProcedure(BUSCAR_PRODUCTOS, new BuscarProductos(getDataSource()));
		addStoredProcedure(AGREGAR_CLAUSULA, new AgregarClausula(getDataSource()));
		addStoredProcedure(LISTA_CLAUSULAS, new ListaClausula(getDataSource()));
		addStoredProcedure(OBTIENE_TABLA_APOYO, new ObtieneTApoyo(getDataSource()));
		addStoredProcedure(LISTA_TIPO_PRODUCTO, new ListaTipoProd(getDataSource()));
		addStoredProcedure(LISTA_TIPO_RAMO, new ListaTipoRamo(getDataSource()));

		addStoredProcedure(OBTENER_RAMA_ATRIBUTOS_PRODUCTO, new ObtieneRamaAtrProd(getDataSource()));
		addStoredProcedure(OBTENER_ATRIBUTOS_SITUACION, new ObtieneRamaAtrSit(getDataSource()));
		addStoredProcedure(OBTENER_ATRIBUTOS_GARANTIA, new ObtieneRamaAtrGarant(getDataSource()));
		addStoredProcedure(OBTENER_RAMA_CONCEPTOS_GLOBALES_PRODUCTO, new ObtieneRamaConcGlob(getDataSource()));
		addStoredProcedure(OBTENER_RAMA_SITUACION, new ObtieneRamaSituacion(getDataSource()));
		addStoredProcedure(OBTENER_RAMA_DATOS_FIJOS_PRODUCTO, new ObtieneRamaDatFijos(getDataSource()));
		addStoredProcedure(OBTENER_RAMA_ROLES_PRODUCTO, new ObtieneRamaRoles(getDataSource()));
		addStoredProcedure(OBTENER_GARANTIA, new ObtieneGarantia(getDataSource()));
		addStoredProcedure(OBTIENE_LISTA_PLANES_RAMAS, new ObtieneRamaListaPlanes(getDataSource()));
		addStoredProcedure(OBTENER_OBJETOS, new ObtieneObjetos(getDataSource()));

		addStoredProcedure(EDITAR_PRODUCTO_WIZARD, new EditarProductoWizard(getDataSource()));
		addStoredProcedure(LISTA_RAMO_POLIZA, new ListaRamoPoliza(getDataSource()));
		addStoredProcedure(LISTA_CLAUSULAS_ASOCIADAS, new ListaClauAsociadas(getDataSource()));
		addStoredProcedure(LISTA_PERIODOS, new ListaPeriodos(getDataSource()));

		addStoredProcedure(OBTIENE_LISTA_COBERTURAS_PLANES_CONFIGURACION, new ListaCobsPlanesConf(getDataSource()));
		addStoredProcedure(GUARDA_PLAN_CONFIGURACION, new GuardaPlanConfiguracion(getDataSource()));
		addStoredProcedure(EDITA_PLAN_CONFIGURACION, new EditaPlanConfiguracion(getDataSource()));
		addStoredProcedure(ELIMINA_PLAN_CONFIGURACION, new EliminaPlanConfiguracion(getDataSource()));

		addStoredProcedure(OBTIENE_LISTA_PLANES_CONFIGURACION, new ListaPlanConfiguracion(getDataSource()));
		addStoredProcedure(GUARDA_COBERTURA_PLAN_CONFIGURACION, new GuardaCobPlanConfiguracion(getDataSource()));
		addStoredProcedure(MODIFICA_COBERTURA_PLAN, new ModificaCobPlanConfiguracion(getDataSource()));
		addStoredProcedure(BORRAR_PLANPRO, new BorraCobPlanConfiguracion(getDataSource()));

		addStoredProcedure(ELIMINAR_TIPO_POLIZA, new EliminaTipoPoliza(getDataSource()));
		addStoredProcedure(INSERTAR_TIPO_POLIZA, new InsertaTipoPoliza(getDataSource()));
		addStoredProcedure(INSERTAR_PERIODOS, new InsertaPeriodos(getDataSource()));
		addStoredProcedure(INSERTAR_CLAUSULAS, new InsertaClausulas(getDataSource()));
		
		addStoredProcedure(LISTA_PRODUCTOS, new ListaProductosIncisos(getDataSource()));
	}

	protected class ClonarProducto extends CustomStoredProcedure {

		protected ClonarProducto(DataSource dataSource) {
			super(dataSource, "PKG_PRODUCTO.p_clona_local");

			declareParameter(new SqlParameter("pv_cdramo_ori_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_cdramo_des_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsramo_des_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_lang_code_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_username_i",
					OracleTypes.VARCHAR));

			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_dsmensaje_o",
					OracleTypes.VARCHAR));
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			
			WrapperResultados wrapperResultados = mapper.build(map);
			return wrapperResultados;
		}
	}

	protected class InsertarProcucto extends CustomStoredProcedure {
		
		protected InsertarProcucto(DataSource dataSource) {
			
			super(dataSource, "PKG_WIZARD.p_insertproduct");
			
			declareParameter(new SqlParameter("pv_cdramo_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dsramo_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipora_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipram_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swsuscri_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swinflac_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swinclnt_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_ttipcalc_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swrescat_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mmrescat_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swreduci_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mmreduci_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swrehabi_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mmbenefi_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swantici_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_mmantici_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swbenefi_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpriper_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swsinomn_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swfronti_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swreaseg_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swsinsit_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swtarifa_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swreauto_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpriuni_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swindper_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swpoldec_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swcoaseg_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swprecar_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swtipo_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swreserv_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swcmptdi_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_nmprerec_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_swdipaco_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swsubinc_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_traza_i",		OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtippol_i",	OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdcaltippol_i",OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_dslinea_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swcancel_i",	OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swendoso_i",	OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_cdramo_o",	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_msg_id_o",	OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",	OracleTypes.VARCHAR));
			compile();
		}
		
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados resultados = mapper.build(map);
			
			resultados.setItemMap(new HashMap());
			logger.debug("pv_cdramo_o: " + map.get("pv_cdramo_o"));
			resultados.getItemMap().put("pv_cdramo_o", map.get("pv_cdramo_o") + "");
			
			return resultados;
			
		}
	}
	
	protected class AgregarClausula extends CustomStoredProcedure {
		
		protected AgregarClausula(DataSource dataSource) {
			
			super(dataSource, "PKG_WIZARD.p_insertclausul");
			
			declareParameter(new SqlParameter("P_CDCLAUSU", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("P_DSCLAUSU", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("P_NMLINEA", OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("P_DSLINEA", OracleTypes.CLOB));
			declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));	
			compile();
		}
		
		@SuppressWarnings("unchecked")
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados resultados = mapper.build(map);
			
			//resultados.setItemMap(new HashMap());
			//logger.debug("pv_msg_id_o: " + map.get("pv_msg_id_o"));
			//logger.debug("pv_title_o: " + map.get("pv_title_o"));
			//resultados.getItemMap().put("pv_msg_id_o", map.get("pv_msg_id_o") + "");
			//resultados.getItemMap().put("pv_title_o", map.get("pv_title_o") + "");
			
			return resultados;
			
		}
	}
	
	protected class GenerarProducto extends CustomStoredProcedure {
		protected GenerarProducto(DataSource dataSource) {
			super(dataSource, "P_EXEC_SIGSGTTA");

			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.NUMERIC));

			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();

		}

		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			//List result = (List) map.get("pv_registro_o");
			//wrapperResultados.setItemList(result);
			return wrapperResultados;
		}
	}
	
	protected class BuscarProductos extends CustomStoredProcedure {
		protected BuscarProductos(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_LOADPROD");
			
			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR,new ListaProductos()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			
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
	
	protected class ListaProductos implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			RamaVO valores = new RamaVO();
			
			valores.setCodigoObjeto(rs.getString(1));
			valores.setText(rs.getString(2));
			
			return valores;
		}
	}
	
	protected class ListaProductosIncisos extends CustomStoredProcedure {
		protected ListaProductosIncisos(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.p_loadINcisos");
			
			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR,new ProductosIncisosMapper()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			
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
	
	protected class ProductosIncisosMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductoVO producto = new ProductoVO();
			
			producto.setCodigoRamo(rs.getInt("CDRAMO"));
			producto.setDescripcionRamo(rs.getString("DSRAMO"));
			producto.setCodigoTipoParametro(rs.getString("CDTIPRAM"));
			producto.setCodigoTipoRamo(rs.getString("CDTIPORA"));
			producto.setCodigoTipoSeguro(rs.getString("CDTIPPOL"));
			producto.setCodigoTipoPoliza(rs.getString("CDCALTIPPOL"));
			producto.setSwitchSuscripcion(rs.getString("SWSUSCRI"));
			producto.setSwitchClausulasNoTipificadas(rs.getString("SWINCLNT"));
			producto.setTipoCalculoPolizasTemporales(rs.getInt("TTIPCALC"));
			producto.setSwitchRehabilitacion(rs.getString("SWREHABI"));
			producto.setMesesBeneficios(rs.getInt("MMBENEFI"));
			producto.setSwitchPrimasPeriodicas(rs.getString("SWPRIPER"));
			producto.setSwitchPermisoPagosOtrasMonedas(rs.getString("SWSINOMN"));
			producto.setSwitchReaseguro(rs.getString("SWREASEG"));
			producto.setSwitchSiniestros(rs.getString("SWSINSIT"));
			producto.setSwitchTarifa(rs.getString("SWTARIFA"));
			producto.setSwitchReinstalacionAutomatica(rs.getString("SWREAUTO"));
			producto.setSwitchPrimasUnicas(rs.getString("SWPRIUNI"));
			producto.setSwitchDistintasPolizasPorAsegurado(rs.getString("SWINDPER"));
			producto.setSwitchPolizasDeclarativas(rs.getString("SWPOLDEC"));
			producto.setSwitchPreavisoCartera(rs.getString("SWPRECAR"));
			producto.setSwitchTipo(rs.getString("SWTIPO"));
			producto.setSwitchTarifaDireccionalTotal(rs.getString("SWCMPTDI"));
			producto.setCantidadDiasReclamacion(rs.getInt("NMPREREC"));
			producto.setSwitchSubincisos(rs.getString("SWSUBINC"));
			producto.setDescripcion(rs.getString("DSLINEA"));
			
			return producto;
		}
	}
	
	protected class ListaClausula extends CustomStoredProcedure {
    	
    	protected ListaClausula(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_loadclausulas");
    		
    		//declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ListClauMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
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
    
    protected class ListClauMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ClausulaVO clausulaVO = new ClausulaVO();
    		//clausulaVO.setCodigoRamo( 	rs.getInt("CDRAMO")	);
    		clausulaVO.setCodigoClausula(  rs.getString("CDCLAUSU")   );
    		clausulaVO.setDescripcionClausula(  rs.getString("DSCLAUSU")   );
    		//clausulaVO.setDescripcionLinea(  rs.getString("DSLINEA")   );
    		
    		return clausulaVO;
    	}
    }

    protected class ObtieneTApoyo extends CustomStoredProcedure {
    	
    	protected ObtieneTApoyo(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_LISTA_TCATALOG");
    		
    		declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDIDIOMA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDREGION_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new TapoyoMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
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
    
    protected class TapoyoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("CODIGO")	);
    		llaveValorVO.setValue(  rs.getString("DESCRIPL")   );
    		return llaveValorVO;
    	}
    }

    protected class ListaTipoProd extends CustomStoredProcedure {
    	
    	protected ListaTipoProd(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADPRODUCTO");
    		
    		declareParameter(new SqlParameter("PV_CDTIPORA", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new TipoRaMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
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
    
    protected class TipoRaMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("CDTIPORA")	);
    		llaveValorVO.setValue(  rs.getString("DSTIPORA")   );
    		return llaveValorVO;
    	}
    }

    protected class ListaTipoRamo extends CustomStoredProcedure {
    	
    	protected ListaTipoRamo(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_loadtiporamo");
    		
    		declareParameter(new SqlParameter("P_CDTIPRAM", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new TipoRamoMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
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
    
    protected class TipoRamoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("CDTIPRAM")	);
    		llaveValorVO.setValue(  rs.getString("DSTIPRAM")   );
    		return llaveValorVO;
    	}
    }

    protected class ObtieneRamaAtrProd extends CustomStoredProcedure {
    	
    	protected ObtieneRamaAtrProd(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADATRIBVAR");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("CDATRIBU")	);
    		ramaVO.setText(  rs.getString("DSATRIBU")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaAtrSit extends CustomStoredProcedure {
    	
    	protected ObtieneRamaAtrSit(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOAD_ATRIBVARSIT");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaSitMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaSitMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("CDATRIBU")	);
    		ramaVO.setText(  rs.getString("DSATRIBU")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaAtrGarant extends CustomStoredProcedure {
    	
    	protected ObtieneRamaAtrGarant(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOAD_ATRIBVARGAR");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdgarant_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaAtrGarantMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaAtrGarantMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdatribu")	);
    		ramaVO.setText(  rs.getString("dsatribu")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaConcGlob extends CustomStoredProcedure {
    	
    	protected ObtieneRamaConcGlob(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADCONCEPGLOB");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaConcGlobMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaConcGlobMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdperiod")	);
    		ramaVO.setText(  rs.getString("orden")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaSituacion extends CustomStoredProcedure {
    	
    	protected ObtieneRamaSituacion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_TTIPOSITLIST");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaSituacionMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaSituacionMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdtipsit")	);
    		ramaVO.setText(  rs.getString("dstipsit")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaDatFijos extends CustomStoredProcedure {
    	
    	protected ObtieneRamaDatFijos(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_OBTTCMPFFLD");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaDatFijoMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaDatFijoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdbloque")	);
    		ramaVO.setText(  rs.getString("cdcampo")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaRoles extends CustomStoredProcedure {
    	
    	protected ObtieneRamaRoles(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADROLESINC");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdnivel_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaRolMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaRolMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdrol")	);
    		ramaVO.setText(  rs.getString("dsrol")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneGarantia extends CustomStoredProcedure {
    	
    	protected ObtieneGarantia(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADCOBERSITPRO");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdplan_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaGarantiaMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaGarantiaMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdgarant")	);
    		ramaVO.setText(  rs.getString("dsgarant")   );
    		return ramaVO;
    	}
    }

    protected class ObtieneRamaListaPlanes extends CustomStoredProcedure {
    	
    	protected ObtieneRamaListaPlanes(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_PLANES_X_SITUACION");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaPlanXsitMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaPlanXsitMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdplan")	);
    		ramaVO.setText(  rs.getString("dsplan")   );
    		return ramaVO;
    	}
    }

    protected class ListaCobsPlanesConf extends CustomStoredProcedure {
    	
    	protected ListaCobsPlanesConf(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_COBERTURAS");
    		
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdplan_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new CobPlanConfMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
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
    
    protected class CobPlanConfMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llave = new LlaveValorVO();
    		llave.setKey(rs.getString(1));
    		llave.setValue(rs.getString(2));
    		return llave;
    	}
    }

    protected class ListaPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected ListaPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_PLANES_X_SITUACION");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ListPlanConfMapper()));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
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
    
    protected class ListPlanConfMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llave = new LlaveValorVO();
    		llave.setKey(rs.getString(1));
    		llave.setValue(rs.getString(2));
    		return llave;
    	}
    }

    protected class ObtieneObjetos extends CustomStoredProcedure {
    	
    	protected ObtieneObjetos(DataSource dataSource) {
    		super(dataSource, "PKG_LISTAS.P_OBJETO_RAMO");
    		
    		declareParameter(new SqlParameter("pv_cdramo", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new RamaObjetoMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class RamaObjetoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		RamaVO ramaVO = new RamaVO();
    		ramaVO.setCodigoObjeto( 	rs.getString("cdtipobj")	);
    		ramaVO.setText(  rs.getString("dstipobj")   );
    		return ramaVO;
    	}
    }
    
    protected class EditarProductoWizard extends CustomStoredProcedure {
    	
    	protected EditarProductoWizard(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADTRAMOS");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new EditarProductoWizardMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class EditarProductoWizardMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ProductoVO producto = new ProductoVO();
			producto.setCodigoRamo(rs.getInt("CDRAMO"));
			producto.setDescripcionRamo(rs.getString("DSRAMO"));
			producto.setCodigoTipoRamo(rs.getString("CDTIPRAM"));
			producto.setDescripcionTipoParametro(rs.getString("DSTIPRAM"));
			producto.setCodigoTipoParametro(rs.getString("CDTIPORA"));
			producto.setDescripcionTipoRamo(rs.getString("DSTIPORA"));
			producto.setSwitchSuscripcion(rs.getString("SWSUSCRI"));
			producto.setTipoCalculoPolizasTemporales(rs.getInt("TTIPCALC"));
			producto.setSwitchRehabilitacion(rs.getString("SWREHABI"));
			producto.setMesesBeneficios(rs.getInt("MMBENEFI"));
			producto.setSwitchPermisoPagosOtrasMonedas(rs.getString("SWSINOMN"));
			producto.setSwitchReaseguro(rs.getString("SWREASEG"));
			producto.setSwitchTarifa(rs.getString("SWTARIFA"));
			producto.setSwitchReinstalacionAutomatica(rs.getString("SWREAUTO"));
			producto.setSwitchPrimasUnicas(rs.getString("SWPRIUNI"));
			producto.setSwitchDistintasPolizasPorAsegurado(rs.getString("SWINDPER"));
			producto.setSwitchPolizasDeclarativas(rs.getString("SWPOLDEC"));
			producto.setSwitchPreavisoCartera(rs.getString("SWPRECAR"));
			producto.setSwitchTipo(rs.getString("SWTIPO"));
			producto.setSwitchSubincisos(rs.getString("SWSUBINC"));
			producto.setDescripcion(rs.getString("DSLINEA"));
			producto.setCodigoTipoSeguro(rs.getString("CDTIPPOL"));
			producto.setCodigoTipoPoliza(rs.getString("CDCALTIPPOL"));
			producto.setSwitchCancelacion(rs.getString("SWCANCEL"));
			producto.setSwitchEndosos(rs.getString("SWENDOSO"));
			
			return producto;
    	}
    }
    
    protected class ListaRamoPoliza extends CustomStoredProcedure {
    	
    	protected ListaRamoPoliza(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOAD_TRAMOPOL");
    		
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ListaRamoPolizaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
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
    
    protected class ListaRamoPolizaMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		LlaveValorVO llaveValorVO = new LlaveValorVO();
    		llaveValorVO.setKey( 	rs.getString("ottempot")	);
    		llaveValorVO.setValue(  rs.getString("ottempot")   );
    		return llaveValorVO;
    	}
    }
  
    protected class ListaClauAsociadas extends CustomStoredProcedure {
    	
    	protected ListaClauAsociadas(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_loadclauasoc");
    		
    		declareParameter(new SqlParameter("P_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new ListaClauAsociadaMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ListaClauAsociadaMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ClausulaVO clausulaVO = new ClausulaVO();
    		clausulaVO.setCodigoRamo( 	rs.getInt("CDRAMO")	);
    		clausulaVO.setCodigoClausula(  rs.getString("CDCLAUSU")   );
    		clausulaVO.setDescripcionClausula(  rs.getString("DSCLAUSU")   );
    		clausulaVO.setDescripcionLinea(  rs.getString("DSLINEA")   );
    		
    		return clausulaVO;
    	}
    }

    protected class ListaPeriodos extends CustomStoredProcedure {
    	
    	protected ListaPeriodos(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_loadperdeval");
    		
    		declareParameter(new SqlParameter("P_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("P_REGISTRO", OracleTypes.CURSOR, new ListaPeriodoMapper()));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("P_REGISTRO");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    	
    }
    
    protected class ListaPeriodoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		PeriodoVO PeriodoVO = new PeriodoVO();
    		PeriodoVO.setCodigoRamo( 	rs.getInt("CDRAMO")	);
    		PeriodoVO.setCodigoPeriodo(  rs.getInt("CDPERIOD")   );
    		PeriodoVO.setInicio(  rs.getString("FEPRINCP")   );
    		PeriodoVO.setFin(  rs.getString("FEFINALP")   );
    		
    		return PeriodoVO;
    	}
    }

    protected class GuardaPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected GuardaPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_CONFG_CUENTA.P_INSERTA_PLAN");
    		
    		declareParameter(new SqlParameter("P_PRODUCTO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_CVE_PLAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_DES_PLAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("p_cve_sit", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class EditaPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected EditaPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_CONFG_CUENTA.P_GUARDA_PLAN");
    		
    		declareParameter(new SqlParameter("P_PRODUCTO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_CVE_PLAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_DES_PLAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class EliminaPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected EliminaPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_BORRA_PLANES_REG");
    		
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdplan_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class GuardaCobPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected GuardaCobPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_CONFG_CUENTA.P_INSERTA_PLANPRO");
    		
    		declareParameter(new SqlParameter("P_PRODUCTO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_PLAN", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_SITUACION", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_GARANTIA", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_OBLIG", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class ModificaCobPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected ModificaCobPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_GUARDA_TGARANTI");
    		
    		declareParameter(new SqlParameter("p_cdgarant_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("p_dsgarant_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class BorraCobPlanConfiguracion extends CustomStoredProcedure {
    	
    	protected BorraCobPlanConfiguracion(DataSource dataSource) {
    		super(dataSource, "PKG_CONFG_CUENTA.P_BORRAR_PLANPRO");
    		
    		declareParameter(new SqlParameter("p_producto", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("p_plan", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("p_situacion", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("p_garantia", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("p_oblig", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class EliminaTipoPoliza extends CustomStoredProcedure {
    	
    	protected EliminaTipoPoliza(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_DELETE_TRAMOPOL");
    		
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ottempot_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class InsertaTipoPoliza extends CustomStoredProcedure {
    	
    	protected InsertaTipoPoliza(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_INSERT_TRAMOPOL");
    		
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ottempot_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class InsertaPeriodos extends CustomStoredProcedure {
    	
    	protected InsertaPeriodos(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_insertperiodo");
    		
    		declareParameter(new SqlParameter("P_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_CDPERIOD", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_INICIO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_FIN", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }

    protected class InsertaClausulas extends CustomStoredProcedure {
    	
    	protected InsertaClausulas(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.p_insertclauasoc");
    		
    		declareParameter(new SqlParameter("P_CDRAMO", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("P_CDCLAUSU", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("PO_MSG_ID", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	@SuppressWarnings("unchecked")
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    	
    }
    
}
