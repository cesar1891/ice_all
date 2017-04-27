package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.aon.configurador.pantallas.model.PantallaVO;
import mx.com.aon.configurador.pantallas.model.components.ComboClearOnSelectVO;
import mx.com.aon.flujos.cotizacion.model.ObjetoCotizacionVO;
import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;
import mx.com.aon.portal.model.BaseObjectVO;
import mx.com.aon.portal.model.CotizacionMasivaVO;
import mx.com.aon.portal.util.WrapperResultados;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class CotizacionDAO extends AbstractDAO{

    private static Logger logger = Logger.getLogger(CotizacionDAO.class);

    public static final String GENERAR_COTIZACION_MASIVA = "GENERAR_COTIZACION_MASIVA";
    public static final String RUTA_COTIZACION = "RUTA_COTIZACION";
    public static final String NUMERO_PROCESO_COTIZACION = "NUMERO_PROCESO_COTIZACION";
    public static final String P_OBTIENE_TVALOSIT_COTIZA = "P_OBTIENE_TVALOSIT_COTIZA";
    
    
    public static final String OBTIENE_PANTALLA_FINAL = "OBTIENE_PANTALLA_FINAL";
    public static final String GET_COMBO_PADRE = "GET_COMBO_PADRE";
    public static final String OBTIENE_ETIQUETA_PRODUCTO_ESPECIAL = "OBTIENE_ETIQUETA_PRODUCTO_ESPECIAL";
    public static final String OBTIENE_LISTA_COTIZACION_ESPECIAL = "OBTIENE_LISTA_COTIZACION_ESPECIAL";
    public static final String OBTIENE_lISTA_TIPO_PRODUCTO_ESPECIAL = "OBTIENE_lISTA_TIPO_PRODUCTO_ESPECIAL";
    
    
    public static final String OBTIENE_RESULTADOS = "OBTIENE_RESULTADOS";

    protected void initDao() throws Exception {
        addStoredProcedure("BUSCAR_COTIZACIONES_MASIVAS",new BuscarCotizacionesMasivas(getDataSource()));
        addStoredProcedure("APROBAR_COTIZACION",new AprobarCotizacion(getDataSource()));
        addStoredProcedure("BORRAR_COTIZACION",new BorrarCotizacion(getDataSource()));
        addStoredProcedure(GENERAR_COTIZACION_MASIVA, new GenerarCotizacionMasiva(getDataSource()));
        addStoredProcedure(RUTA_COTIZACION, new ObtenerArchivoCotizacion(getDataSource()));
        addStoredProcedure(NUMERO_PROCESO_COTIZACION, new NumeroProcesoCotizacionMasiva(getDataSource()));
        addStoredProcedure(P_OBTIENE_TVALOSIT_COTIZA, new ObtenerTvalositCotiza(getDataSource()));

        
        addStoredProcedure(OBTIENE_PANTALLA_FINAL, new ObtienePantallaFinal(getDataSource()));
        addStoredProcedure(GET_COMBO_PADRE, new ObtieneComboPadre(getDataSource()));
        addStoredProcedure(OBTIENE_ETIQUETA_PRODUCTO_ESPECIAL, new ObtieneEtiquetaProductoEspecial(getDataSource()));
        addStoredProcedure(OBTIENE_LISTA_COTIZACION_ESPECIAL, new ObtieneCotizacionEspecial(getDataSource()));
        addStoredProcedure(OBTIENE_lISTA_TIPO_PRODUCTO_ESPECIAL, new ObtieneTipoProductoEspecial(getDataSource()));
        
        addStoredProcedure(OBTIENE_RESULTADOS, new ObtieneResultadosCotiza(getDataSource()));
    }

    protected class BuscarCotizacionesMasivas extends CustomStoredProcedure {

      protected BuscarCotizacionesMasivas(DataSource dataSource) {
          super(dataSource, "PKG_COTIZA.P_CONSULTA_COTIZA_MASIVA");
         
          declareParameter(new SqlParameter("pv_cdelement", OracleTypes.VARCHAR)); //es NUMBER
          declareParameter(new SqlParameter("pv_asegura", OracleTypes.VARCHAR)); //es NUMBER
          declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR)); //es NUMBER
          declareParameter(new SqlParameter("pv_cdlayout", OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_fedesde_i", OracleTypes.VARCHAR)); //es NUMBER
          declareParameter(new SqlParameter("pv_fehasta_i", OracleTypes.VARCHAR)); //es NUMBER
          
          declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new CotizacionnMasivaMapper()));
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

    protected class CotizacionnMasivaMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	CotizacionMasivaVO cotizacionMasivaVO = new CotizacionMasivaVO();
        	
        	cotizacionMasivaVO.setCdAsegura(rs.getString("CDASEGURA"));
        	cotizacionMasivaVO.setDsAsegura(rs.getString("DSASEGURA"));
        	cotizacionMasivaVO.setCdRamo(rs.getString("CDRAMO"));
        	cotizacionMasivaVO.setDsRamo(rs.getString("DSRAMO"));
        	cotizacionMasivaVO.setCdPerson(rs.getString("CDPERSON"));
        	cotizacionMasivaVO.setDsNombre(rs.getString("DSNOMBRE"));
        	cotizacionMasivaVO.setCarga(rs.getString("CARGA"));
        	cotizacionMasivaVO.setNmPoliza(rs.getString("NMPOLIZA"));
        	cotizacionMasivaVO.setFeInivig(rs.getString("FEINIVIG"));
        	cotizacionMasivaVO.setFeFinvig(rs.getString("FEFINVIG"));
        	cotizacionMasivaVO.setPrima(rs.getString("PRIMA"));
        	cotizacionMasivaVO.setCdplan(rs.getString("CDPLAN"));
        	cotizacionMasivaVO.setNmsituac(rs.getString("NMSITUAC"));        	
        	cotizacionMasivaVO.setEstado(rs.getString("ESTADO"));
        	cotizacionMasivaVO.setCdtipsit(rs.getString("CDTIPSIT"));
        	cotizacionMasivaVO.setCdcia(rs.getString("CDCIA"));
        	cotizacionMasivaVO.setCdunieco(rs.getString("CDUNIECO"));
        	        	      	 
            return cotizacionMasivaVO;
        }
    }

    protected class AprobarCotizacion extends CustomStoredProcedure {

      protected AprobarCotizacion(DataSource dataSource) {
          super(dataSource, "PKG_EMISION.P_PROCESO_EMISION_MASIVO");
          declareParameter(new SqlParameter("pv_cdusuari", OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_cdunieco", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdramo", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_estado", OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_nmpoliza", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_nmsituac", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_nmsuplem", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdelement", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdcia", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdplan", OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_cdperpag", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdperson", OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_fecha", OracleTypes.DATE));
          
          //declareParameter(new SqlOutParameter("pv_message", OracleTypes.VARCHAR));
          declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
          declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
      
          compile();
        }


        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            //wrapperResultados.setResultado((String)(map.get("pv_message")));
            return mapper.build(map);
        }
    }


    protected class BorrarCotizacion extends CustomStoredProcedure {

      protected BorrarCotizacion(DataSource dataSource) {
          super(dataSource, "PKG_EMISION.P_BORRA_COTIZA_MASIVA");
          
          declareParameter(new SqlParameter("pv_cdusuari",OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_cdunieco",OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdramo",OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_estado",OracleTypes.VARCHAR));
          declareParameter(new SqlParameter("pv_nmpoliza",OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_nmsituac",OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_nmsuplem",OracleTypes.NUMERIC));
          declareParameter(new SqlParameter("pv_cdelement",OracleTypes.NUMERIC));
                              
          //declareParameter(new SqlOutParameter("pv_message", OracleTypes.VARCHAR));
          declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
          declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
          compile();
        }


        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            //wrapperResultados.setResultado((String)(map.get("pv_message")));
            return mapper.build(map);
        }
    }
 
    

    protected class GenerarCotizacionMasiva extends CustomStoredProcedure {
    	
    	protected GenerarCotizacionMasiva(DataSource dataSource) {
    		super(dataSource, "PKG_COTIZA.P_GENERA_COTIZACION_MASIVA");
    		
    		declareParameter(new SqlParameter("pv_cdelemen_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_idproces_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_filename_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_producto_i", OracleTypes.NUMERIC));
            declareParameter(new SqlParameter("pv_fechapro_i", OracleTypes.DATE));
            
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            
            return wrapperResultados;
        }
    }
    
    protected class NumeroProcesoCotizacionMasiva extends CustomStoredProcedure {
    	
    	protected NumeroProcesoCotizacionMasiva(DataSource dataSource) {
    		super(dataSource, "PKG_COTIZA.P_GEN_SEQUENCE_PROC_MASIVO");
    		
    		declareParameter(new SqlOutParameter("pv_idproces_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            
            wrapperResultados.setItemMap(new HashMap<String, Object>());
            wrapperResultados.getItemMap().put("ID_PROCESO", map.get("pv_idproces_o")); 
            return wrapperResultados;
        }
    }
    
    protected class ObtenerArchivoCotizacion extends CustomStoredProcedure {
    	
    	protected ObtenerArchivoCotizacion(DataSource dataSource) {
    		super(dataSource, "PKG_COTIZA.P_CARGA_ARCHIVO");
    		
    		declareParameter(new SqlOutParameter("pv_ruta_o", OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
            
            logger.debug("RUTA_COTIZACIONDAO: " + map.get("pv_ruta_o"));
            
            wrapperResultados.setItemMap(new HashMap<String, Object>());
            wrapperResultados.getItemMap().put("RUTA", map.get("pv_ruta_o"));            
            //wrapperResultados.setResultado((String)(map.get("pv_message")));
            return wrapperResultados;
        }
    }
    
    //OBTIENE_TVALOSIT_COTIZA 
    protected class ObtenerTvalositCotiza extends CustomStoredProcedure {

        protected ObtenerTvalositCotiza(DataSource dataSource) {
            super(dataSource,"PKG_COTIZA.P_OBTIENE_TVALOSIT_COTIZA");
            
            declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR)); //es NUMBER
            declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR)); //es NUMBER
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR)); //es NUMBER
            declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new TvalositCotizaMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
            compile();
          }

        public WrapperResultados mapWrapperResultados(Map map) throws Exception {
            WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
            WrapperResultados wrapperResultados = mapper.build(map);
         
            logger.debug("CotizacionDAO.ObtenerTvalositCotiza(): " + map.get("pv_registro_o"));
            
            List result = (List) map.get("pv_registro_o");
            wrapperResultados.setItemList(result);
            return wrapperResultados;
        }
      }
    
    
    protected class TvalositCotizaMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	CotizacionMasivaVO cotizacionMasivaVO = new CotizacionMasivaVO();
        	
        	cotizacionMasivaVO.setDsatribu( rs.getString("DSATRIBU") );
        	cotizacionMasivaVO.setOtvalor(  rs.getString("OTVALOR")); 
        	cotizacionMasivaVO.setDsnombre( rs.getString("DSNOMBRE"));
        	cotizacionMasivaVO.setDsvalor(  rs.getString("DSVALOR")); 
         
            return cotizacionMasivaVO;
        }
    }
    
    /*PARA EL CAMBIO DE VELOCITY BACKBONE A JDBC EN EL PROCESO DE COTIACION */
    
    
    protected class ObtienePantallaFinal extends CustomStoredProcedure {

        protected ObtienePantallaFinal(DataSource dataSource) {
            super(dataSource,"PKG_CONFIGURA_PANTALLA2.P_OBTIENE_PANTALLA_FINAL");
            
            declareParameter(new SqlParameter("pv_cdelemento_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR)); 
            declareParameter(new SqlParameter("pv_cdtitulo_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsisrol_i", OracleTypes.VARCHAR));
            
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new PantallaFinalMapper()));
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
    
    
    protected class PantallaFinalMapper  implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        	PantallaVO pantallaVO = new PantallaVO();
        	
        	pantallaVO.setCdPantalla(rs.getString("CDPANTALLA"));
        	pantallaVO.setDsArchivo(rs.getString("DSARCHIVO"));
        	pantallaVO.setDsArchivoSec(rs.getString("DSARCHIVOSEC"));
        	pantallaVO.setDsCampos(rs.getString("DSCAMPOS"));
        	pantallaVO.setDsLabels(rs.getString("DSLABEL"));
         
            return pantallaVO;
        }
    }
    
    
    protected class ObtieneComboPadre extends CustomStoredProcedure {
    	
    	protected ObtieneComboPadre(DataSource dataSource) {
    		super(dataSource,"PKG_WIZARD.P_OBTIENE_PADRE");
    		
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_ottabval_i", OracleTypes.VARCHAR)); 
    		
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ComboPadreMapper()));
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
    
    
    protected class ComboPadreMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ComboClearOnSelectVO combo = new ComboClearOnSelectVO();
    		
    		combo.setCdatribu(rs.getString("CDATRIBU"));
    		combo.setOttabval(rs.getString("OTTABVAL"));
    		
    		return combo;
    	}
    }
    
    
    protected class ObtieneEtiquetaProductoEspecial extends CustomStoredProcedure {
    	
    	protected ObtieneEtiquetaProductoEspecial(DataSource dataSource) {
    		super(dataSource,"PKG_COTIZA.P_TIENE_OBJETOS");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR)); 
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("PV_MSG_ID_O", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("PV_TITLE_O", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		String etiqueta = "";
    		if(map.get("PV_REGISTRO_O") != null) etiqueta = map.get("PV_REGISTRO_O").toString();
    		
			wrapperResultados.setItemMap(new HashMap<String, Object>());
			wrapperResultados.getItemMap().put("ETIQUETA", etiqueta);
		
    		return wrapperResultados;
    	}
    }
    
    protected class ObtieneCotizacionEspecial extends CustomStoredProcedure {
    	
    	protected ObtieneCotizacionEspecial(DataSource dataSource) {
    		super(dataSource,"PKG_COTIZA.P_OBTIENE_MPOLIOBJ");
    		
    		declareParameter(new SqlParameter("PV_CDUNIECO", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("PV_CDRAMO", OracleTypes.NUMERIC)); 
    		declareParameter(new SqlParameter("PV_ESTADO", OracleTypes.VARCHAR)); 
    		declareParameter(new SqlParameter("PV_NMPOLIZA", OracleTypes.NUMERIC)); 
    		declareParameter(new SqlParameter("PV_NMSITUAC", OracleTypes.NUMERIC)); 
    		declareParameter(new SqlParameter("PV_NMOBJETO", OracleTypes.NUMERIC)); 
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new CotizacionEspecialMapper()));
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
    
    
    protected class CotizacionEspecialMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		ObjetoCotizacionVO cotizacion = new ObjetoCotizacionVO();
    		
    		cotizacion.setCdUnieco(rs.getString("CDUNIECO"));
    		cotizacion.setCdRamo(rs.getString("CDRAMO"));
    		cotizacion.setEstado(rs.getString("ESTADO"));
    		cotizacion.setNmPoliza(rs.getString("NMPOLIZA"));
    		cotizacion.setNmSituac(rs.getString("NMSITUAC"));
    		cotizacion.setCdTipobj(rs.getString("CDTIPOBJ"));
    		cotizacion.setNmSuplem(rs.getString("NMSUPLEM"));
    		cotizacion.setStatus(rs.getString("STATUS"));
    		cotizacion.setCdGarant(rs.getString("CDGARANT"));
    		cotizacion.setCdTipcon(rs.getString("CDTIPCON"));
    		cotizacion.setCdContar(rs.getString("CDCONTAR"));
    		cotizacion.setNmImport(rs.getString("NMIMPORT"));
    		cotizacion.setOrden(rs.getString("ORDEN"));
    		cotizacion.setDsGarant(rs.getString("DSGARANT"));
    		cotizacion.setDsTipcon(rs.getString("DSTIPCON"));
    		cotizacion.setDsContar(rs.getString("DSCONTAR"));
    		cotizacion.setNmObjeto(rs.getString("NMOBJETO"));
    		cotizacion.setDsObjeto(rs.getString("DSOBJETO"));
    		cotizacion.setPtObjeto(rs.getString("PTOBJETO"));
    		cotizacion.setCdAgrupa(rs.getString("CDAGRUPA"));
    		cotizacion.setNmValor(rs.getString("NMVALOR"));
    		cotizacion.setDsDescripcion(rs.getString("DSDESCRIPCION"));
    		
    		return cotizacion;
    	}
    }
    
    
    protected class ObtieneTipoProductoEspecial extends CustomStoredProcedure {
    	
    	protected ObtieneTipoProductoEspecial(DataSource dataSource) {
    		super(dataSource,"PKG_LISTAS.P_OBJETO_RAMO");
    		
    		declareParameter(new SqlParameter("PV_CDRAMO_I", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("PV_CDTIPSIT_I", OracleTypes.VARCHAR)); 
    		
    		declareParameter(new SqlOutParameter("PV_REGISTRO_O", OracleTypes.CURSOR, new ProductoEspecialMapper()));
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
    
    
    protected class ProductoEspecialMapper  implements RowMapper {
    	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    		BaseObjectVO tipo = new BaseObjectVO();
    		
    		tipo.setLabel(rs.getString("DSTIPOBJ"));
    		tipo.setValue(rs.getString("CDTIPOBJ"));
    		
    		return tipo;
    	}
    }
    
    
protected class ObtieneResultadosCotiza extends CustomStoredProcedure {
    	
    	protected ObtieneResultadosCotiza(DataSource dataSource) {
    		super(dataSource,"PKG_COTIZA.P_GEN_TARIFICACION");
    		
    		declareParameter(new SqlParameter("pv_cdusuari_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdelemen_i", OracleTypes.NUMERIC));
    		declareParameter(new SqlParameter("pv_cdtipsit_i", OracleTypes.VARCHAR));
    		
    		declareParameter(new SqlOutParameter("pv_record_o", OracleTypes.CURSOR, new ResultadosCotizaMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.NUMERIC));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    	
    	public WrapperResultados mapWrapperResultados(Map map) throws Exception {
    		WrapperResultadosGeneric mapper = new WrapperResultadosGeneric();
    		WrapperResultados wrapperResultados = mapper.build(map);
    		List result = (List) map.get("pv_record_o");
    		wrapperResultados.setItemList(result);
    		return wrapperResultados;
    	}
    }

	protected class ResultadosCotizaMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			DecimalFormat formatter = new DecimalFormat("#,##0.00");
			
			ResultadoCotizacionVO result = new ResultadoCotizacionVO();
			
			result.setCdIdentifica(rs.getString("CDIDENTIFICA"));
			result.setCdUnieco(rs.getString("CDUNIECO"));
			result.setDsUnieco(	rs.getString("DSUNIECO"));
			result.setCdRamo(rs.getString("CDRAMO"));
			result.setEstado(rs.getString("ESTADO"));
			result.setNmPoliza(rs.getString("NMPOLIZA"));
			result.setNmSuplem(rs.getString("NMSUPLEM"));
			result.setStatus(rs.getString("STATUS"));
			result.setCdPlan(rs.getString("CDPLAN"));
			result.setDsPlan(rs.getString("DSPLAN"));
			result.setCdCiaaseg(rs.getString("CDCIAASEG"));
			result.setCdPerpag(rs.getString("CDPERPAG"));
			result.setDsPerpag(rs.getString("DSPERPAG"));
			result.setCdTipsit(rs.getString("CDTIPSIT"));
			//result.setDsTipsit(rs.getString("DSTIPSIT"));
			result.setFeEmisio(rs.getString("FEEMISIO"));
			result.setFeVencim(rs.getString("FEVENCIM"));
			result.setNumeroSituacion(rs.getString("NMSITUAC"));
			result.setMnPrima(rs.getString("MNPRIMA"));

			/*try{
				//if(logger.isDebugEnabled())logger.debug("Antes de parseo mnprima: " + rs.getDouble("MNPRIMA"));
				result.setMnPrima(formatter.format(rs.getDouble("MNPRIMA")));
				//if(logger.isDebugEnabled())logger.debug("Despues de parseo mnprima: " + result.getMnPrima());
			}catch(NumberFormatException nfe){
				logger.error("Error al parsear el valor de la mnprima "+ rs.getString("MNPRIMA"), nfe);
				result.setMnPrima(rs.getString("MNPRIMA"));
			}*/
			
			return result;
		}
	}
    
}


