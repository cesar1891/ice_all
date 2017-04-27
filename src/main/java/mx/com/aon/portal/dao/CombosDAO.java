package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.portal.model.AtributosVariablesInstPagoVO;
import mx.com.aon.portal.model.BaseObjectVO;
import mx.com.aon.portal.model.CondicionCoberturaVO;
import mx.com.aon.portal.model.ElementoComboBoxVO;
import mx.com.aon.portal.model.FuncionalidadVO;
import mx.com.aon.portal.model.ItemVO;
import mx.com.aon.portal.model.Tabla_EquivalenciaVO;
import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class CombosDAO extends AbstractDAO{

    private static Logger logger = Logger.getLogger(CotizacionDAO.class);


    protected void initDao() throws Exception {
        addStoredProcedure("COMBO_CARGA_MASIVA",new ComboCargaMasiva(getDataSource()));
        addStoredProcedure("COMBO_TIPO_OPERACION",new ComboTipoCancelacion(getDataSource()));
        addStoredProcedure("COMBO_ROL_FUNCIONALIDAD",new ComboRolFuncionalidades(getDataSource()));
        addStoredProcedure("COMBO_USUARIO_FUNCIONALIDAD",new ComboUsuarioFuncionalidades(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_ALGORITMOS",new ComboObtieneAlgoritmos(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_PAIS",new ComboObtienePais(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_CATALOGO_AON",new ComboObtieneCatalogoAon(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_CATALOGO_EXTERNO",new ComboObtieneCatalogoExterno(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_CODIGO",new ComboObtieneCodigo(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_PAISES",new ComboObtienePaises(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_USO",new ComboObtieneUso(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_SISTEMA_EXTERNO",new ComboObtieneSistemaExterno(getDataSource()));
        addStoredProcedure("COMBO_CODIGO_POSTAL", new ComboCodigoPostal(getDataSource()));
        addStoredProcedure("COMBO_CATALOGOS_COMPUESTOS", new ComboCatalogosCompuestos(getDataSource()));
        addStoredProcedure("COMBO_OBTIENE_POLIZA",new ComboObtienePoliza (getDataSource()));
        addStoredProcedure("COMBO_LISTA_VALORES_INST_PAGO",new ObtenerListaValores(getDataSource()));
        addStoredProcedure("COMBO_CONDICION_INST_PAGO",new ObtenerCondicionInstrPago(getDataSource()));
        
    }



    protected class ComboCargaMasiva extends CustomStoredProcedure {

        protected ComboCargaMasiva(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_LISTA_CARGA_MASIVA");

            declareParameter(new SqlParameter("pv_cdregion_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdpais_i", OracleTypes.VARCHAR));

            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboResultadoMapper()));
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


    protected class ComboTipoCancelacion extends CustomStoredProcedure {

        protected ComboTipoCancelacion(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_LISTA_TCATALOG");


            declareParameter(new SqlParameter("pv_cdtabla_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdidioma_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdregion_i", OracleTypes.VARCHAR));

            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboValoresMapper()));
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


    protected class ComboResultadoMapper implements RowMapper{
    		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
    			ItemVO itemVO = new ItemVO();
    			itemVO.setId(rs.getString("CD"));
    			itemVO.setTexto(rs.getString("DS"));
    			return itemVO;
    		}
    }


    protected class ComboValoresMapper implements RowMapper{
    		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
    			ElementoComboBoxVO elementoComboBoxVO = new ElementoComboBoxVO();
    			elementoComboBoxVO.setCodigo(rs.getString("CODIGO"));
    			elementoComboBoxVO.setDescripcion(rs.getString("DESCRIPL"));
    			return elementoComboBoxVO;
                
            }
    }

    protected class ComboRolFuncionalidades extends CustomStoredProcedure {

        protected ComboRolFuncionalidades(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_ROLES_X_NIVEL");

            declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.VARCHAR));

            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboRolMapper()));
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

    protected class ComboUsuarioFuncionalidades extends CustomStoredProcedure {

        protected ComboUsuarioFuncionalidades(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_USUARIO_X_NIVELROL");

            declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdrol_i", OracleTypes.VARCHAR));

            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboUsuarioMapper()));
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



    protected class ComboUsuarioMapper implements RowMapper{
    		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
    			FuncionalidadVO funcionalidadVO = new FuncionalidadVO();
    			funcionalidadVO.setCdUsuario(rs.getString("CDUSUARI"));
    			funcionalidadVO.setDsNombre(rs.getString("DSUSUARI"));
    			return funcionalidadVO;
    		}
    }
    protected class ComboRolMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			FuncionalidadVO funcionalidadVO = new FuncionalidadVO();
			funcionalidadVO.setCdSisRol(rs.getString("CDSISROL"));
			funcionalidadVO.setDsSisRol(rs.getString("DSSISROL"));
			return funcionalidadVO;
		}
}
    
    // ComboObtieneAlgoritmos 
    protected class ComboObtieneAlgoritmos extends CustomStoredProcedure {

        protected ComboObtieneAlgoritmos(DataSource dataSource) {
            super(dataSource, "PKG_SEGURIDAD.P_OBTIENE_ALGORITMO");
           
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtieneAlgoritmosMapper()));
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

    protected class ComboObtieneAlgoritmosMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			ElementoComboBoxVO elementoComboBoxVO = new ElementoComboBoxVO();
			elementoComboBoxVO.setCodigo(rs.getString("IDGENERADOR"));
			elementoComboBoxVO.setDescripcion(rs.getString("DESCRIPCION"));
			return elementoComboBoxVO;

        }
    }
    //FIN ComboObtienePais
    
    
    protected class ComboObtienePais extends CustomStoredProcedure {

        protected ComboObtienePais(DataSource dataSource) {
            super(dataSource, "PKG_EQUIVALENCIA.P_OBTIENE_COUNTRY_NAME");
           
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtienePaisMapper()));
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
    
    protected class ComboObtienePaisMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
       	 Tabla_EquivalenciaVO tabla_EquivalenciaVO = new Tabla_EquivalenciaVO();
            
       	 tabla_EquivalenciaVO.setCountry_code(rs.getString("COUNTRY_CODE"));
       	 tabla_EquivalenciaVO.setNumCode(rs.getString("NUM_CODE"));
       	 tabla_EquivalenciaVO.setCountry_name(rs.getString("COUNTRY_NAME"));
       	 tabla_EquivalenciaVO.setRegionId(rs.getString("REGION_ID"));
       	             	             	                
         return tabla_EquivalenciaVO;
        }
    }    
    //FIN ComboObtienePais
    
    
    
//ComboObtieneCatalogoAon    
    
    protected class ComboObtieneCatalogoAon extends CustomStoredProcedure {

        protected ComboObtieneCatalogoAon(DataSource dataSource) {
            super(dataSource, "PKG_EQUIVALENCIA.P_OBTIENE_DSTABLAACW");
            
            declareParameter(new SqlParameter("pv_country_code_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsistema_i", OracleTypes.VARCHAR));

            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtieneCatalogoAonMapper()));
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
    
    protected class ComboObtieneCatalogoAonMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
       	 Tabla_EquivalenciaVO tabla_EquivalenciaVO = new Tabla_EquivalenciaVO();
            
       	tabla_EquivalenciaVO.setCdtablaacw(rs.getString("CDTABLAACW")); 
       	             	             	                
         return tabla_EquivalenciaVO;
        }
    }    
    //FIN ComboObtieneCatalogoAon    
    
    
 //ComboObtieneCatalogoExterno
    
    
    protected class ComboObtieneCatalogoExterno extends CustomStoredProcedure {

        protected ComboObtieneCatalogoExterno(DataSource dataSource) {
            super(dataSource, "PKG_EQUIVALENCIA.P_OBTIENE_TCATAEXT");
            
            declareParameter(new SqlParameter("pv_country_code_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsistema_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otclave01_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_otvalor_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtablaext_i", OracleTypes.VARCHAR));
           
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtieneCatalogoExternoAonMapper()));
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
    
    protected class ComboObtieneCatalogoExternoAonMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
       	 Tabla_EquivalenciaVO tabla_EquivalenciaVO = new Tabla_EquivalenciaVO();
            
       	 tabla_EquivalenciaVO.setCountry_code(rs.getString("COUNTRY_CODE"));
     	 tabla_EquivalenciaVO.setCdsistema(rs.getString("CDSISTEMA"));
     	 tabla_EquivalenciaVO.setOtclave01(rs.getString("OTCLAVE01"));
     	 tabla_EquivalenciaVO.setOtvalor(rs.getString("OTVALOR"));
     	 tabla_EquivalenciaVO.setOtclave02(rs.getString("OTCLAVE02"));
     	 tabla_EquivalenciaVO.setOtclave03(rs.getString("OTCLAVE03"));
     	 tabla_EquivalenciaVO.setOtclave04(rs.getString("OTCLAVE04"));
     	 tabla_EquivalenciaVO.setOtclave05(rs.getString("OTCLAVE05"));
     	 
         return tabla_EquivalenciaVO;
        }
    }    
    //FIN ComboObtieneCatalogoExterno
    
    
 //ComboObtieneCodigo
        
    protected class ComboObtieneCodigo extends CustomStoredProcedure {

        protected ComboObtieneCodigo(DataSource dataSource) {
            super(dataSource, "PKG_EQUIVALENCIA.P_OBTIENE_CDSISTEMA");               // PKG_EQUIVALENCIA.P_OBTIENE_TCATEQUV
            
            declareParameter(new SqlParameter("pv_country_code_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtieneCodigoMapper()));
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
    
    protected class ComboObtieneCodigoMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
       	 Tabla_EquivalenciaVO tabla_EquivalenciaVO = new Tabla_EquivalenciaVO();
            
       	 tabla_EquivalenciaVO.setCdsistema(rs.getString("cdsistema"));
       	
         return tabla_EquivalenciaVO;
        }
    }    
    
    //FIN ComboObtieneCodigo
    
    protected class ComboObtienePaises extends CustomStoredProcedure {

        protected ComboObtienePaises(DataSource dataSource) {
            super(dataSource, "PKG_EQUIVALENCIA.P_OBTIENE_COUNTRIES");
           
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtienePaisesMapper()));
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

    protected class ComboObtienePaisesMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			ElementoComboBoxVO elementoComboBoxVO = new ElementoComboBoxVO();
			elementoComboBoxVO.setCodigo(rs.getString("COUNTRY_CODE"));
			elementoComboBoxVO.setDescripcion(rs.getString("COUNTRY_NAME"));
			return elementoComboBoxVO;

        }
    }
    
    protected class ComboObtieneUso extends CustomStoredProcedure {

        protected ComboObtieneUso(DataSource dataSource) {
            super(dataSource, "PKG_TCATALOG.P_LISTA_TCATALOG");
            
            declareParameter(new SqlParameter("pv_cdtabla_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtieneUsoMapper()));
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

    protected class ComboObtieneUsoMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			ElementoComboBoxVO elementoComboBoxVO = new ElementoComboBoxVO(); //arreglar mapper
			elementoComboBoxVO.setCodigo(rs.getString("CODIGO"));
			elementoComboBoxVO.setDescripcion(rs.getString("DESCRIPC"));
			return elementoComboBoxVO;

        }
    }
    
    
    protected class ComboObtieneSistemaExterno extends CustomStoredProcedure {

        protected ComboObtieneSistemaExterno(DataSource dataSource) {
            super(dataSource, "PKG_TCATALOG.P_LISTA_TCATALOG");
            
            declareParameter(new SqlParameter("pv_cdtabla_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboObtieneSistemaExternoMapper()));
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

    protected class ComboObtieneSistemaExternoMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			ElementoComboBoxVO elementoComboBoxVO = new ElementoComboBoxVO(); //arreglar mapper
			elementoComboBoxVO.setCodigo(rs.getString("CODIGO"));
			elementoComboBoxVO.setDescripcion(rs.getString("DESCRIPC"));
			return elementoComboBoxVO;

        }
    }
    
    
    protected class ComboCodigoPostal extends CustomStoredProcedure {

        protected ComboCodigoPostal(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_OBTIENE_CODPOS");
            declareParameter(new SqlParameter("pv_cdpais_i", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboCodigoPostalMapper()));
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
    
    protected class ComboCodigoPostalMapper implements RowMapper{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
			BaseObjectVO codigoPostal = new BaseObjectVO();
			codigoPostal.setValue(rs.getString("CDCODPOS"));
			codigoPostal.setLabel(rs.getString("DSCODPOS"));
			return codigoPostal;

        }
    }

    protected class ComboCatalogosCompuestos extends CustomStoredProcedure {
    	protected ComboCatalogosCompuestos (DataSource dataSource) {
    		super (dataSource, "PKG_CATALOGO.P_OBTIENE_COMBOS");
    		declareParameter(new SqlParameter("pv_columna_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cve1_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cve2_i", OracleTypes.VARCHAR));

    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboCatalogosCompuestosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMBER));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));

    		compile();
    	}
    	@SuppressWarnings("unchecked")
		public WrapperResultados mapWrapperResultados (Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_registro_o");
    		wrapperResultados.setItemList(result);

    		return wrapperResultados;
    	}
    }
    protected class ComboCatalogosCompuestosMapper implements RowMapper {
    	public Object mapRow (ResultSet rs, int rowNum) throws SQLException {
    		ElementoComboBoxVO comboBoxVO = new ElementoComboBoxVO ();
    		/**
    		 * IMPORTANTE:
    		 * 		Para estos combos, los datos obtenidos desde el sp son mapeados por posici�n en lugar
    		 * 		de mapearlos por nombre.
    		 * NO MODIFICAR!!!
    		 */
    		comboBoxVO.setCodigo(rs.getString(1));
    		comboBoxVO.setDescripcion(rs.getString(2));
    		return comboBoxVO;
    	}
    }
    
    protected class ComboObtienePoliza extends CustomStoredProcedure {

        protected ComboObtienePoliza(DataSource dataSource) {
            super(dataSource, "PKG_LISTAS.P_OBTIENE_NMPOLIEX");
          
            declareParameter(new SqlParameter("pv_cdunieco_i",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdramo_i",OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_cdperson_i",OracleTypes.NUMERIC));               
           
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtienePolizaMapper()));
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
          protected class ObtienePolizaMapper implements RowMapper{
      		public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
      			ElementoComboBoxVO elementoComboBoxVO = new ElementoComboBoxVO();
      			
      			elementoComboBoxVO.setCodigo(rs.getString("NMPOLIZA"));  
      			elementoComboBoxVO.setDescripcion(rs.getString("NMPOLIEX"));
      			
      			return elementoComboBoxVO;

              }
          }
          
          protected class ObtenerListaValores extends CustomStoredProcedure {

    	      protected ObtenerListaValores(DataSource dataSource) {
    	          super(dataSource, "PKG_WIZARD.LOAD_TABLA_APOYO");

    	          declareParameter(new SqlParameter("pv_ottipotb_i", OracleTypes.VARCHAR));
    	          declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtenerListaValoresMapper()));
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
    	 
    	 
    	 protected class ObtenerListaValoresMapper  implements RowMapper {
    	        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    	        	
    	        	
    	        	AtributosVariablesInstPagoVO atributosVariablesInstPagoVO = new AtributosVariablesInstPagoVO();

    	        	atributosVariablesInstPagoVO.setCdTabla(rs.getString("CDTABLA"));
    	        	atributosVariablesInstPagoVO.setDsTabla(rs.getString("DSTABLA"));
    	        	atributosVariablesInstPagoVO.setNmTabla(rs.getString("NMTABLA"));


    	            return atributosVariablesInstPagoVO;
    	        }
    	    }    
    	 
    	 protected class ObtenerCondicionInstrPago extends CustomStoredProcedure {

   	      protected ObtenerCondicionInstrPago(DataSource dataSource) {
   	          super(dataSource, "PKG_WIZARD.P_CONDICCOBERT");

   	          declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtenerCondicionMapper()));
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
   	 
   	 
   	 protected class ObtenerCondicionMapper  implements RowMapper {
   	        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
   	        	CondicionCoberturaVO condicionCoberturaVO = new CondicionCoberturaVO();
   	        	condicionCoberturaVO.setCdcondic(rs.getString("CDCONDIC"));
   	        	condicionCoberturaVO.setDscondic(rs.getString("DSCONDIC"));
   	        	
   	            return condicionCoberturaVO;
   	        }
   	    }
    
}