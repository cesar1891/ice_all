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
import mx.com.gseguros.wizard.configuracion.producto.atributosVariables.model.AtributosVariablesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class AtributoDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(AtributoDAO.class);
	
	public static final String OBTIENE_VALORES_ATRIBUTOS = "OBTIENE_VALORES_ATRIBUTOS";
	public static final String ATRIBUTO_PADRE_PRODUCTO 	= "ATRIBUTO_PADRE_PRODUCTO";
	public static final String ATRIBUTO_PADRE_COBERTURA = "ATRIBUTO_PADRE_COBERTURA";
	public static final String ATRIBUTO_PADRE_OBJETO 	= "ATRIBUTO_PADRE_OBJETO";
	//public static final String ATRIBUTO_PADRE_RIESGO 	= "ATRIBUTO_PADRE_RIESGO";
	public static final String ATRIBUTO_PADRE_ROL 	= "ATRIBUTO_PADRE_ROL";
	public static final String GUARDA_ATRIBUTOS_VARIABLES = "GUARDA_ATRIBUTOS_VARIABLES";
	public static final String OBTIENE_PADRES = "OBTIENE_PADRES";
	
	public static final String OBTIENE_ATRIBUTO_VARIABLE_PRODUCTO = "OBTIENE_ATRIBUTO_VARIABLE_PRODUCTO";
	public static final String OBTIENE_ATRIBUTO_VARIABLE_RIESGO = "OBTIENE_ATRIBUTO_VARIABLE_RIESGO";
	public static final String OBTIENE_ATRIBUTO_VARIABLE_COBERTURA = "OBTIENE_ATRIBUTO_VARIABLE_COBERTURA";
	public static final String GUARDA_ATRIBUTOS_VARIABLES_GARANTIA = "GUARDA_ATRIBUTOS_VARIABLES_GARANTIA";

	public static final String GUARDA_ATRIBUTOS_VARIABLES_INCISO = "GUARDA_ATRIBUTOS_VARIABLES_INCISO";
	public static final String VALIDA_HIJOS_ATRIB_VAR_PRODUCTO = "VALIDA_HIJOS_ATRIB_VAR_PRODUCTO";
	public static final String VALIDA_HIJOS_ATRIB_VAR_INCISO_RIESGO = "VALIDA_HIJOS_ATRIB_VAR_INCISO_RIESGO";
	public static final String VALIDA_HIJOS_ATRIB_VAR_COBERTURA = "VALIDA_HIJOS_ATRIB_VAR_COBERTURA";

	public static final String ELIMINA_ATRIBUTOS_VARIABLES_PRODUCTO = "ELIMINA_ATRIBUTOS_VARIABLES_PRODUCTO";
	public static final String ELIMINA_ATRIBUTOS_VARIABLES_INCISO = "ELIMINA_ATRIBUTOS_VARIABLES_INCISO";
	public static final String ELIMINA_ATRIBUTOS_VARIABLES_GARANTIA = "ELIMINA_ATRIBUTOS_VARIABLES_GARANTIA";
	
	
	protected void initDao() throws Exception {

		addStoredProcedure(OBTIENE_VALORES_ATRIBUTOS, new ObtieneValoresAtributo(getDataSource()));
		addStoredProcedure(ATRIBUTO_PADRE_PRODUCTO, new AtributosPadresProducto(getDataSource()));
		addStoredProcedure(ATRIBUTO_PADRE_COBERTURA, new AtributosPadresCobertura(getDataSource()));
		addStoredProcedure(ATRIBUTO_PADRE_OBJETO, new AtributosPadresObjeto(getDataSource()));
		//addStoredProcedure(ATRIBUTO_PADRE_RIESGO, new AtributosPadresRiesgo(getDataSource()));
		addStoredProcedure(ATRIBUTO_PADRE_ROL, new AtributosPadresRol(getDataSource()));
		addStoredProcedure(GUARDA_ATRIBUTOS_VARIABLES, new GuardarAtributoProducto(getDataSource()));
		addStoredProcedure(OBTIENE_PADRES, new ObtienePadres(getDataSource()));

		addStoredProcedure(OBTIENE_ATRIBUTO_VARIABLE_PRODUCTO, new ObtieneAtrVarProd(getDataSource()));
		addStoredProcedure(OBTIENE_ATRIBUTO_VARIABLE_RIESGO, new ObtieneAtrVarRiesgo(getDataSource()));
		addStoredProcedure(OBTIENE_ATRIBUTO_VARIABLE_COBERTURA, new ObtieneAtrVarCobertura(getDataSource()));
		addStoredProcedure(GUARDA_ATRIBUTOS_VARIABLES_GARANTIA, new GuardaAtrVarGarantia(getDataSource()));

		addStoredProcedure(GUARDA_ATRIBUTOS_VARIABLES_INCISO, new GuardaAtrVarInciso(getDataSource()));
		
		addStoredProcedure(VALIDA_HIJOS_ATRIB_VAR_PRODUCTO, new ValidaAtrVarProducto(getDataSource()));
		addStoredProcedure(VALIDA_HIJOS_ATRIB_VAR_INCISO_RIESGO, new ValidaAtrVarIncisoRiesgo(getDataSource()));
		addStoredProcedure(VALIDA_HIJOS_ATRIB_VAR_COBERTURA, new ValidaAtrVarCobertura(getDataSource()));

		addStoredProcedure(ELIMINA_ATRIBUTOS_VARIABLES_PRODUCTO, new EliminaAtrVarProducto(getDataSource()));
		addStoredProcedure(ELIMINA_ATRIBUTOS_VARIABLES_INCISO, new EliminaAtrVarIncisoRiesgo(getDataSource()));
		addStoredProcedure(ELIMINA_ATRIBUTOS_VARIABLES_GARANTIA, new EliminaAtrVarCobertura(getDataSource()));


	}

    protected class ObtieneValoresAtributo extends CustomStoredProcedure {

        protected ObtieneValoresAtributo(DataSource dataSource) {
            super(dataSource, "PKG_WIZARD.LOAD_TABLA_APOYO");
           
            declareParameter(new SqlParameter("pv_ottipotb_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ValoresAtributoMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();

          }

        @Override
        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
              WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
              WrapperResultados wrapperResultados = mapper.build(map);
              List result = (List) map.get("pv_registro_o");
              wrapperResultados.setItemList(result);
              return wrapperResultados;
        }
      }
    
    protected class GuardaAtrVarGarantia extends CustomStoredProcedure {
    	
    	protected GuardaAtrVarGarantia(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_INSERTATRIBCOB");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDFORMATO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_ENDOSOS_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_EMISION_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_MINIMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_MAXIMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWOBLIGA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWCOTIZA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWDATCOM_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWCOMOBL_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWCOMUPD_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWENDOSO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWENDOBL_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWINSERT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_DSATRIBU_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlParameter("pv_cdatribu_padre_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmorden_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmagrupa_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdcondicvis_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_SWTARIFI_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    		
    	}
    	
    	@Override
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }

    protected class GuardaAtrVarInciso extends CustomStoredProcedure {
    	
    	protected GuardaAtrVarInciso(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_INSATRIBINC");
    		
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_DSATRIBU_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDFORMATO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_MINIMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_MAXIMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWOBLIGA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWPRODUC_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWSUPLEM_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWTARIFI_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWPRESEN_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTABLA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWDATCOM_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWCOMOBL_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWCOMUPD_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWENDOSO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWENDOBL_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDEXPRES_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWINSERT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDATRIBU_PADRE_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_NMORDEN_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_NMAGRUPA_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDCONDICVIS_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_SWSUSCRI_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    		
    	}
    	
    	@Override
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		return mapper.build(map);
    	}
    }
	
    protected class ValoresAtributoMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AtributosVariablesVO atributosVariablesVO = new AtributosVariablesVO();
        	
        	atributosVariablesVO.setCdTabla(rs.getString("CDTABLA"));
        	atributosVariablesVO.setDsTabla(rs.getString("DSTABLA"));
        	atributosVariablesVO.setNumeroTabla(rs.getString("NMTABLA"));

            return atributosVariablesVO;
        }
    }

    protected class ObtieneAtrVarProd extends CustomStoredProcedure {
    	
    	protected ObtieneAtrVarProd(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADATRIBVARPROD");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AtrVarProdMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    		
    	}
    	
    	@Override
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class AtrVarProdMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		AtributosVariablesVO atributosVariablesVO = new AtributosVariablesVO();
    		
    		atributosVariablesVO.setCdRamo(rs.getString("CDRAMO"));
//    		atributosVariablesVO.setCodigoSituacion(rs.getString("CDTIPSIT"));
//    		atributosVariablesVO.setCodigoGarantia(rs.getString("CDGARANT"));
    		atributosVariablesVO.setCodigoAtributo(rs.getString("CDATRIBU"));
    		atributosVariablesVO.setDescripcion(rs.getString("DSATRIBU"));
    		atributosVariablesVO.setCdFormato(rs.getString("SWFORMAT"));
    		atributosVariablesVO.setMinimo(rs.getString("NMLMIN"));
    		atributosVariablesVO.setMaximo(rs.getString("NMLMAX"));
    		atributosVariablesVO.setObligatorio(rs.getString("SWOBLIGA"));
    		atributosVariablesVO.setEmision(rs.getString("SWPRODUC"));
    		atributosVariablesVO.setEndosos(rs.getString("SWSUPLEM"));
    		atributosVariablesVO.setRetarificacion(rs.getString("SWTARIFI"));
    		atributosVariablesVO.setCotizador(rs.getString("SWPRESEN"));
    		atributosVariablesVO.setCodigoExpresion(rs.getString("CDEXPRES"));
    		atributosVariablesVO.setCdTabla(rs.getString("OTTABVAL"));
    		atributosVariablesVO.setDsTabla(rs.getString("DSTABLA"));
    		atributosVariablesVO.setCodigoPadre(rs.getString("CDATRIBU_PADRE"));
    		atributosVariablesVO.setOrden(rs.getString("NMORDEN"));
    		atributosVariablesVO.setAgrupador(rs.getString("NMAGRUPA"));
    		atributosVariablesVO.setCodigoCondicion(rs.getString("CDCONDICVIS"));
//    		atributosVariablesVO.setDsAtributoPadre(rs.getString("DSATRIBU_PADRE"));
//    		atributosVariablesVO.setDsCondicion(rs.getString("DSCONDICVIS"));
    		atributosVariablesVO.setDatoComplementario(rs.getString("SWDATCOM"));
    		atributosVariablesVO.setObligatorioComplementario(rs.getString("SWCOMOBL"));
    		atributosVariablesVO.setModificableComplementario(rs.getString("SWCOMUPD"));
    		atributosVariablesVO.setApareceEndoso(rs.getString("SWENDOSO"));
    		atributosVariablesVO.setObligatorioEndoso(rs.getString("SWENDOBL"));
//    		atributosVariablesVO.setCotizador(rs.getString("SWCOTIZA"));
    		
    		
    		return atributosVariablesVO;
    	}
    }

    protected class ObtieneAtrVarRiesgo extends CustomStoredProcedure {
    	
    	protected ObtieneAtrVarRiesgo(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADATRIBVARRIES");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AtrVarRiesgoMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    		
    	}
    	
    	@Override
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class AtrVarRiesgoMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		AtributosVariablesVO atributosVariablesVO = new AtributosVariablesVO();
    		
//    		atributosVariablesVO.setCdRamo(rs.getString("CDRAMO"));
    		atributosVariablesVO.setCodigoSituacion(rs.getString("CDTIPSIT"));
//    		atributosVariablesVO.setCodigoGarantia(rs.getString("CDGARANT"));
    		atributosVariablesVO.setCodigoAtributo(rs.getString("CDATRIBU"));
    		atributosVariablesVO.setDescripcion(rs.getString("DSATRIBU"));
    		atributosVariablesVO.setCdFormato(rs.getString("SWFORMAT"));
    		atributosVariablesVO.setMinimo(rs.getString("NMLMIN"));
    		atributosVariablesVO.setMaximo(rs.getString("NMLMAX"));
    		atributosVariablesVO.setObligatorio(rs.getString("SWOBLIGA"));
    		atributosVariablesVO.setEmision(rs.getString("SWPRODUC"));
    		atributosVariablesVO.setEndosos(rs.getString("SWSUPLEM"));
    		atributosVariablesVO.setRetarificacion(rs.getString("SWTARIFI"));
    		atributosVariablesVO.setCotizador(rs.getString("SWPRESEN"));
    		atributosVariablesVO.setCodigoExpresion(rs.getString("CDEXPRES"));
    		atributosVariablesVO.setCdTabla(rs.getString("OTTABVAL"));
    		atributosVariablesVO.setDsTabla(rs.getString("DSTABLA"));
    		atributosVariablesVO.setCodigoPadre(rs.getString("CDATRIBU_PADRE"));
    		atributosVariablesVO.setOrden(rs.getString("NMORDEN"));
    		atributosVariablesVO.setAgrupador(rs.getString("NMAGRUPA"));
    		atributosVariablesVO.setCodigoCondicion(rs.getString("CDCONDICVIS"));
    		atributosVariablesVO.setDsAtributoPadre(rs.getString("DSATRIBU_PADRE"));
    		atributosVariablesVO.setDsCondicion(rs.getString("DSCONDICVIS"));
    		atributosVariablesVO.setDatoComplementario(rs.getString("SWDATCOM"));
    		atributosVariablesVO.setObligatorioComplementario(rs.getString("SWCOMOBL"));
    		atributosVariablesVO.setModificableComplementario(rs.getString("SWCOMUPD"));
    		atributosVariablesVO.setApareceEndoso(rs.getString("SWENDOSO"));
    		atributosVariablesVO.setObligatorioEndoso(rs.getString("SWENDOBL"));
    		atributosVariablesVO.setAtributoParaTodos(rs.getString("SWSUSCRI"));
//    		atributosVariablesVO.setCotizador(rs.getString("SWCOTIZA"));
    		
    		
    		return atributosVariablesVO;
    	}
    }
    
    protected class ObtieneAtrVarCobertura extends CustomStoredProcedure {
    	
    	protected ObtieneAtrVarCobertura(DataSource dataSource) {
    		super(dataSource, "PKG_WIZARD.P_LOADATRIBVARCOB");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDGARANT_I", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("PV_CDATRIBU_I", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new AtrVarCobMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    		
    	}
    	
    	@Override
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }
    
    protected class AtrVarCobMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		AtributosVariablesVO atributosVariablesVO = new AtributosVariablesVO();
    		
    		atributosVariablesVO.setCdRamo(rs.getString("CDRAMO"));
    		atributosVariablesVO.setCodigoSituacion(rs.getString("CDTIPSIT"));
    		atributosVariablesVO.setCodigoGarantia(rs.getString("CDGARANT"));
    		atributosVariablesVO.setCodigoAtributo(rs.getString("CDATRIBU"));
    		atributosVariablesVO.setDescripcion(rs.getString("DSATRIBU"));
    		atributosVariablesVO.setCdFormato(rs.getString("SWFORMAT"));
    		atributosVariablesVO.setMinimo(rs.getString("NMLMIN"));
    		atributosVariablesVO.setMaximo(rs.getString("NMLMAX"));
    		atributosVariablesVO.setObligatorio(rs.getString("SWOBLIGA"));
    		atributosVariablesVO.setEmision(rs.getString("SWPRODUC"));
    		atributosVariablesVO.setEndosos(rs.getString("SWSUPLEM"));
    		atributosVariablesVO.setRetarificacion(rs.getString("SWTARIFI"));
//    		atributosVariablesVO.setCotizador(rs.getString("SWPRESEN"));
    		atributosVariablesVO.setCodigoExpresion(rs.getString("CDEXPRES"));
    		atributosVariablesVO.setCdTabla(rs.getString("OTTABVAL"));
    		atributosVariablesVO.setDsTabla(rs.getString("DSTABLA"));
    		atributosVariablesVO.setCodigoPadre(rs.getString("CDATRIBU_PADRE"));
    		atributosVariablesVO.setOrden(rs.getString("NMORDEN"));
    		atributosVariablesVO.setAgrupador(rs.getString("NMAGRUPA"));
    		atributosVariablesVO.setCodigoCondicion(rs.getString("CDCONDICVIS"));
//    		atributosVariablesVO.setDsAtributoPadre(rs.getString("DSATRIBU_PADRE"));
//    		atributosVariablesVO.setDsCondicion(rs.getString("DSCONDICVIS"));
    		atributosVariablesVO.setDatoComplementario(rs.getString("SWDATCOM"));
    		atributosVariablesVO.setObligatorioComplementario(rs.getString("SWCOMOBL"));
    		atributosVariablesVO.setModificableComplementario(rs.getString("SWCOMUPD"));
    		atributosVariablesVO.setApareceEndoso(rs.getString("SWENDOSO"));
    		atributosVariablesVO.setObligatorioEndoso(rs.getString("SWENDOBL"));
//    		atributosVariablesVO.setCotizador(rs.getString("SWCOTIZA"));
    		
    		
    		return atributosVariablesVO;
    	}
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    
    
	protected class AtributosPadresProducto extends CustomStoredProcedure {

		protected AtributosPadresProducto(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_ATRIBVARPOL_PADRE");

			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.NUMERIC));

			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR, new ListaValores()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}

	}

	protected class AtributosPadresRiesgo extends CustomStoredProcedure {

		protected AtributosPadresRiesgo(DataSource dataSource) {
			super(dataSource, "");
		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

	}

	protected class AtributosPadresRol extends CustomStoredProcedure {

		protected AtributosPadresRol(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_ATRIBVARROL_PADRE");

			declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.NUMERIC));

			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR, new ListaValores()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();

		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}

	}

	protected class AtributosPadresCobertura extends CustomStoredProcedure {

		protected AtributosPadresCobertura(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_ATRIBVARGAR_PADRE");

			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdtipsit_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.NUMERIC));

			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR, new ListaValores()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();

		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}

	}

	protected class AtributosPadresObjeto extends CustomStoredProcedure {

		protected AtributosPadresObjeto(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_ATRIBVAROBJ_PADRE");

			declareParameter(new SqlParameter("pv_cdtipobj_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR, new ListaValores()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}

	}

	protected class GuardarAtributoProducto extends CustomStoredProcedure {
		
		protected GuardarAtributoProducto(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_INSERTATRIBPOL");
			
			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_clavecampo_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_descripcion_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdformato_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_obligatorio_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_maximo_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_minimo_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdtabla_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_emision_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_endosos_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_retarificacion_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cotizador_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swdatcom_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swcomobl_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swcomupd_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swendoso_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swendobl_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_padre_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmorden_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_nmagrupa_i",
					OracleTypes.NUMERIC));
			declareParameter(new SqlParameter("pv_cdcondicvis_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_swinsert_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdexpres_i",
					OracleTypes.NUMERIC));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));

            compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			
			return mapper.build(map);
		}
	}
	
	protected class ListaValores implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			LlaveValorVO valores = new LlaveValorVO();
			
			valores.setKey(rs.getString(1));
			valores.setValue(rs.getString(2));
			
			return valores;
		}
	}
	
	protected class ObtienePadres extends CustomStoredProcedure {

		protected ObtienePadres(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_ATRIBVAR_PADRE");

			declareParameter(new SqlParameter("PV_CDTIPSIT_I",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDATRIBU_I",
					OracleTypes.VARCHAR));

			declareParameter(new SqlOutParameter("pv_registro_o",
					OracleTypes.CURSOR, new ListaValores()));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}

		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
		}

	}
	protected class ValidaAtrVarProducto extends CustomStoredProcedure {
		
		protected ValidaAtrVarProducto(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_VALIDA_HIJOS_TATRIPOL");
			
			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.VARCHAR));
			
			
			declareParameter(new SqlOutParameter("pv_existe_o",
					OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			HashMap<String, Object> itemmap = new HashMap<String, Object>();
			itemmap.put("pv_existe_o", map.get("pv_existe_o"));
			
			wrapperResultados.setItemMap(itemmap);
			return wrapperResultados;
		}
		
	}
	protected class ValidaAtrVarIncisoRiesgo extends CustomStoredProcedure {
		
		protected ValidaAtrVarIncisoRiesgo(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_VALIDA_HIJOS_TATRISIT");
			
			declareParameter(new SqlParameter("pv_cdtipsit_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_existe_o",
					OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			HashMap<String, Object> itemmap = new HashMap<String, Object>();
			itemmap.put("pv_existe_o", map.get("pv_existe_o"));
			
			wrapperResultados.setItemMap(itemmap);
			return wrapperResultados;
		}
		
	}
	protected class ValidaAtrVarCobertura extends CustomStoredProcedure {
		
		protected ValidaAtrVarCobertura(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_VALIDA_HIJOS_TATRIGAR");
			
			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdtipsit_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdgarant_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_existe_o",
					OracleTypes.VARCHAR));
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			WrapperResultados wrapperResultados = mapper.build(map);
			HashMap<String, Object> itemmap = new HashMap<String, Object>();
			itemmap.put("pv_existe_o", map.get("pv_existe_o"));
			
			wrapperResultados.setItemMap(itemmap);
			return wrapperResultados;
		}
		
	}

	protected class EliminaAtrVarProducto extends CustomStoredProcedure {
		
		protected EliminaAtrVarProducto(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_BORRA_TATRIPOL");
			
			declareParameter(new SqlParameter("pv_cdramo_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
		
	}

	protected class EliminaAtrVarIncisoRiesgo extends CustomStoredProcedure {
		
		protected EliminaAtrVarIncisoRiesgo(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_BORRA_TATRISIT");
			
			declareParameter(new SqlParameter("pv_cdtipsit_i",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cdatribu_i",
					OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
		
	}

	protected class EliminaAtrVarCobertura extends CustomStoredProcedure {
		
		protected EliminaAtrVarCobertura(DataSource dataSource) {
			super(dataSource, "PKG_WIZARD.P_BORRA_TATRIGAR");
			
			declareParameter(new SqlParameter("PV_CDRAMO_I",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDTIPSIT_I",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDGARANT_I",
					OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("PV_CDATRIBU_I",
					OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o",
					OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o",
					OracleTypes.VARCHAR));
			compile();
		}
		
		@Override
		public WrapperResultados mapWrapperResultados(Map map) throws Exception {
			WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
			return mapper.build(map);
		}
		
	}

}