package mx.com.gseguros.portal.general.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import mx.com.gseguros.portal.general.dao.RecibosDAO;
import mx.com.gseguros.portal.general.dao.impl.ReportesDAOImpl.ObtieneReporteMapper;
import mx.com.gseguros.portal.general.dao.impl.ReportesDAOImpl.ObtieneReporteSP;
import mx.com.gseguros.portal.general.model.DetalleReciboVO;
import mx.com.gseguros.portal.general.model.ReciboVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.ice2sigs.client.axis2.ServicioGSServiceStub.Recibo;
import mx.com.gseguros.ws.ice2sigs.client.model.ReciboWrapper;
import oracle.jdbc.OracleTypes;

public class RecibosDAOImpl extends AbstractManagerDAO implements RecibosDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(RecibosDAOImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<ReciboVO> obtieneRecibos(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ObtieneRecibos(getDataSource()), params);
		return (List<ReciboVO>) result.get("pv_registro_o");
	}
	
	protected class ObtieneRecibos extends StoredProcedure {
    	protected ObtieneRecibos(DataSource dataSource) {
    		super(dataSource, "Pkg_Consulta.P_OBTIENE_RECIBOS");
    		declareParameter(new SqlParameter("pv_cdunieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdramo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmpoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmsuplem_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new RecibosMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
    	}
    }
	
	protected class RecibosMapper implements RowMapper<ReciboVO> {
		@Override
		public ReciboVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReciboVO recibo = new ReciboVO();
			recibo.setNmrecibo(rs.getString("NMRECIBO"));
			recibo.setFeinicio(rs.getString("FEINICIO"));
			recibo.setFefinal( rs.getString("FEFINAL"));
			recibo.setFeemisio(rs.getString("FEEMISIO"));
			recibo.setCdestado(rs.getString("CDESTADO"));
			recibo.setPtimport(rs.getString("PTIMPORT"));
			recibo.setDsestado(rs.getString("DSESTADO"));
			recibo.setTiporeci(rs.getString("TIPORECI"));
			recibo.setDstipore(rs.getString("DSTIPORE"));
			recibo.setConsecutivo(rs.getString("CONSECUTIVO"));
			recibo.setCliente(rs.getString("CLIENTE"));
			
			return recibo;
		}
	}

	
	@Override
	public List<DetalleReciboVO> obtieneDetalleRecibo(Map<String, Object> params) throws Exception {
		Map<String, Object> result = ejecutaSP(new ConsultaDetalleRecibo(getDataSource()), params); 
		return (List<DetalleReciboVO>) result.get("pv_RegDatos_o");
	}
	
	protected class ConsultaDetalleRecibo extends StoredProcedure {
		protected ConsultaDetalleRecibo(DataSource dataSource) {
			super(dataSource, "Pkg_Consulta.P_OBTIENE_MRECIDET");
    		declareParameter(new SqlParameter("pv_cdUnieco_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_cdRamo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_Estado_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_NmPoliza_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlParameter("pv_nmRecibo_i", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_RegDatos_o", OracleTypes.CURSOR, new DetalleReciboMapper()));
    		declareParameter(new SqlOutParameter("pv_msg_id_o", OracleTypes.VARCHAR));
    		declareParameter(new SqlOutParameter("pv_title_o", OracleTypes.VARCHAR));
    		compile();
		}
	}
	
	protected class DetalleReciboMapper implements RowMapper<DetalleReciboVO> {
		@Override
		public DetalleReciboVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DetalleReciboVO detRecibo = new DetalleReciboVO();
			detRecibo.setCdtipcon(rs.getString("CdTipCon"));
			detRecibo.setDstipcon(rs.getString("DsTipCon"));
			detRecibo.setPtimport(rs.getString("ptimport")); 
			return detRecibo;
		}
	}
	
	@Override
    public List<Map<String, String>> obtenerDatosRecibosSISA(String cdunieco, String cdramo, String estado, String nmpoliza) 
            throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i", cdramo);
        params.put("pv_estado_i", estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        Map<String, Object> mapResult = ejecutaSP(new ObtenerDatosRecibosSISA(getDataSource()), params);
        return (List<Map<String, String>>) mapResult.get("pv_registro_o");
    }
    
    protected class ObtenerDatosRecibosSISA extends StoredProcedure {
        protected ObtenerDatosRecibosSISA(DataSource dataSource) {
            super(dataSource, "PKG_RECIBOS_SISA_SIGS.P_GET_INFO_RECIBOS_SISA");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            String[] cols = new String[]{
                    "nmimpres",
                    "documento",
                    "folio",
                    "feemisio",
                    "feinicio",
                    "fefinal",
                    "prima_total",
                    "art41",
                    "nmrecibo",
                    "primas_sfp",
                    "recargo",
                    "gastos_exped",
                    "iva",
                    "codigo_serial",
                    "status",
                    "fecha_pago",
                    "des_ben_max",
                    "cdunieco",
                    "cdsubram",
                    "nmpoliza",                    
                    "feexpedi",
                    "nmsuplem",
                    "ntramite",
                    "nsuplogi",
                    "cdtipsup",
                    "dstipsup",
                    "nmsolici",
                    "swdespago",
                    "consecutivo"
            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }

    public String consolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, String usuario, List<Map<String, String>> lista) throws Exception{
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        String[] array = new String[lista.size()];        
        int i = 0;
        for(Map<String,String> recibo : lista){
            array[i++] = recibo.get("nmrecibo");
        }
        params.put("cdunieco", cdunieco);
        params.put("cdramo"  , cdramo);
        params.put("estado"  , estado);
        params.put("nmpoliza", nmpoliza);
        params.put("usuario" , usuario);
        params.put("array"   ,    new SqlArrayValue(array));
        Map<String,Object> procResult = ejecutaSP(new ConsolidarRecibos(getDataSource()),params);
        String folio                  = procResult.get("pv_nmfolcon_o").toString();
        return folio;
    }
    
    protected class ConsolidarRecibos extends StoredProcedure{
        protected ConsolidarRecibos(DataSource dataSource){
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_CONSOLIDA_RECIBOS");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("usuario"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("array"    , OracleTypes.ARRAY , "LISTA_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_nmfolcon_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    public void desconsolidarRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, String usuario, String folio) throws Exception{
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("cdunieco", cdunieco);
        params.put("cdramo"  , cdramo);
        params.put("estado"  , estado);
        params.put("nmpoliza", nmpoliza);
        params.put("usuario" , usuario);
        params.put("folio"   , folio);
        ejecutaSP(new DesconsolidarRecibos(getDataSource()),params);
    }
    
    protected class DesconsolidarRecibos extends StoredProcedure{
        protected DesconsolidarRecibos(DataSource dataSource){
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_DESCONSOLIDA_RECIBOS");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("usuario"  , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("folio" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o" , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"  , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<DetalleReciboVO> obtieneDetalleReciboSISA(String cdunieco, 
                                                          String cdramo, 
                                                          String estado, 
                                                          String nmpoliza,
                                                          String nmrecibo,
                                                          String nmfolcon) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("cdunieco",  cdunieco);
        params.put("cdramo",    cdramo);
        params.put("estado",    estado);
        params.put("nmpoliza",  nmpoliza);
        params.put("nmrecibo",  nmrecibo);
        params.put("nmfolcon",  nmfolcon);
        Map<String, Object> result = ejecutaSP(new ObtieneDetalleReciboSISA(getDataSource()), params); 
        return (List<DetalleReciboVO>) result.get("pv_registro_o");
    }
    
    protected class ObtieneDetalleReciboSISA extends StoredProcedure {
        protected ObtieneDetalleReciboSISA(DataSource dataSource) {
            super(dataSource, "PKG_RECIBOS_SISA_SIGS.P_OBTIENE_MRECIDET");
            declareParameter(new SqlParameter("cdunieco",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo",           OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado",           OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmrecibo",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmfolcon",         OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DetalleReciboMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    public List<Map<String, String>> obtenerInfoRecibos(String cdunieco, String cdramo, String estado, String nmpoliza, String nmrecibo, String nmsuplem) throws Exception{
        Map<String, String> params = new HashMap<String, String>();
        params.put("cdunieco",  cdunieco);
        params.put("cdramo",    cdramo);
        params.put("estado",    estado);
        params.put("nmpoliza",  nmpoliza);
        params.put("nmrecibo",  nmrecibo);
        params.put("nmsuplem",  nmsuplem);
        Map<String, Object> result = ejecutaSP(new ObtenerInfoRecibos(getDataSource()), params);
        return (List<Map<String, String>>) result.get("pv_registro_o");
    }
    
    protected class ObtenerInfoRecibos extends StoredProcedure {
        protected ObtenerInfoRecibos(DataSource dataSource) {
            super(dataSource, "PKG_RECIBOS_SISA_SIGS.P_CONS_RECIBO_POL");
            declareParameter(new SqlParameter("cdunieco",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo",           OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado",           OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmrecibo",         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmsuplem",         OracleTypes.VARCHAR));
//            String[] cols = new String[]{
//                    "rmdbRn",
//                    "numSuc",
//                    "numRam",
//                    "numPol",
//                    "Recibo",
//                    "numRec",     
//                    "tipEnd",
//                    "numEnd",
//                    "numMon",
//                    "tipRec",
//                    "prima",
//                    "iva",
//                    "recargo",
//                    "derecho",
//                    "bonific",
//                    "saldo",
//                    "priCom",
//                    "comPri",
//                    "comRec",
//                    "actRec",
//                    "totRec",
//                    "numCli",
//                    "numAgt",
//                    "statusr",
//                    "genera_liga",
//                    "fecSta",
//                    "fecEmi",
//                    "fecIni",
//                    "fecTer",
//                    "fecPag",
//                    "priDot",
//                    "TIPOPER"
//            };
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new DatosRecibosMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    protected class DatosRecibosMapper  implements RowMapper {
        
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReciboWrapper recVO = new ReciboWrapper();
            Recibo recibo = new Recibo();       
            
            Calendar cal;
            
            recibo.setActRec(rs.getInt("actRec"));
            recibo.setBonific(rs.getDouble("bonific"));
            recibo.setComPri(rs.getDouble("comPri"));
            recibo.setComRec(rs.getDouble("comRec"));
            recibo.setDerecho(rs.getDouble("derecho"));
            
            cal = Utils.getCalendarTimeZone0(rs.getString("fecEmi"), Constantes.FORMATO_FECHA);
            if(cal != null){
                recibo.setFecEmi(cal);
            }else{
                logger.warn("NO SE PUDO PARSEAR LA FECHA fecEmi !!! " + rs.getString("fecEmi"));
            }
            
            cal = Utils.getCalendarTimeZone0(rs.getString("fecIni"), Constantes.FORMATO_FECHA);
            if(cal != null){
                recibo.setFecIni(cal);
            }else{
                logger.warn("NO SE PUDO PARSEAR LA FECHA fecIni !!! " + rs.getString("fecIni"));
            }
        
            cal = Utils.getCalendarTimeZone0(rs.getString("fecPag"), Constantes.FORMATO_FECHA);
            if(cal != null){
                recibo.setFecPag(cal);
            }else{
                logger.warn("NO SE PUDO PARSEAR LA FECHA fecPag se envia 01/01/1900 !!! " + rs.getString("fecPag"));
                recibo.setFecPag(Utils.getCalendarTimeZone0("01/01/1900", Constantes.FORMATO_FECHA));
            }
            
            cal = Utils.getCalendarTimeZone0(rs.getString("fecSta"), Constantes.FORMATO_FECHA);
            if(cal != null){
                recibo.setFecSta(cal);
            }else{
                logger.warn("NO SE PUDO PARSEAR LA FECHA fecSta !!! " + rs.getString("fecSta"));
            }
            
            cal = Utils.getCalendarTimeZone0(rs.getString("fecTer"), Constantes.FORMATO_FECHA);
            if(cal != null){
                recibo.setFecTer(cal);
            }else{
                logger.warn("NO SE PUDO PARSEAR LA FECHA fecTer !!! " + rs.getString("fecTer"));
            }
                
                
            recibo.setIva(rs.getDouble("iva"));
            //logger.debug(">>>>>  Valor de iva: " +rs.getDouble("iva"));
            
            recibo.setNumAgt(rs.getInt("numAgt"));
            recibo.setNumCli(rs.getInt("numCli"));
            recibo.setNumEnd(rs.getInt("numEnd"));
            //logger.debug(">>>>>  Valor de numEnd: " +rs.getInt("numEnd"));
            
            recibo.setNumMon(rs.getInt("numMon"));
            recibo.setNumPol(rs.getInt("numPol"));
            recibo.setNumRam(rs.getInt("numRam"));
            recibo.setNumRec(rs.getInt("numRec"));
            recibo.setNumSuc(rs.getInt("numSuc"));
            recibo.setPriCom(rs.getDouble("priCom"));
            recibo.setPriDot(rs.getDouble("priDot"));
            recibo.setPrima(rs.getDouble("prima"));
            //logger.debug(">>>>>  Valor de prima: " +rs.getDouble("prima"));
            
            recibo.setRecargo(rs.getDouble("recargo"));
            recibo.setRmdbRn(rs.getInt("rmdbRn"));
            recibo.setSaldo(rs.getDouble("saldo"));
            recibo.setStatusr(rs.getString("statusr"));
            
            String tipoEndoso = rs.getString("tipEnd");
            //logger.debug(">>>>>  Valor de tipEnd: " +rs.getString("tipEnd"));
            
            if(tipoEndoso == null)tipoEndoso = " ";
            recibo.setTipEnd(tipoEndoso);
            
            recibo.setTipRec(rs.getString("tipRec"));
            recibo.setTotRec(rs.getInt("totRec"));
            
            recVO.setRecibo(recibo);
            recVO.setOperacion(rs.getString("TIPOPER"));
            
            String guardaDocumento =  rs.getString("GENERA_LIGA");
            recVO.setGuardarDocumento((StringUtils.isNotBlank(guardaDocumento) &&  Constantes.SI.equalsIgnoreCase(guardaDocumento))?true:false);
            
            return recVO;
        }
    }

    @Override
    public InputStream obtenerReporte(String cdunieco, String cdramo, String estado, String nmpoliza, String[] lista) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();   
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i",   cdramo);
        params.put("pv_estado_i",   estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        params.put("array",         new SqlArrayValue(lista));        
        InputStream archivo =  null;
        try {
            Map<String, Object> resultado = ejecutaSP(new ObtieneReporteSP(getDataSource()), params);
            logger.debug("resultado:"+resultado);
            ArrayList<InputStream> inputList = (ArrayList<InputStream>) resultado.get("pv_registro_o");
            archivo = inputList.get(0);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        
        return archivo;
    }
        
    protected class ObtieneReporteSP extends StoredProcedure {
        protected ObtieneReporteSP(DataSource dataSource) {
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_GET_REP_COS_AFI_REC");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("array",            OracleTypes.ARRAY, "LISTA_VARCHAR2"));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneReporteMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    protected class ObtieneReporteMapper implements RowMapper<InputStream> {
        public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
            OracleLobHandler lobHandler = new OracleLobHandler();
            return new ByteArrayInputStream(lobHandler.getClobAsString(rs, "DATA").getBytes()); //.getBlobAsBytes(rs, "DATA"));
        }
    }
    
    @Override
    public InputStream obtenerReporteRecibos(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();   
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i",   cdramo);
        params.put("pv_estado_i",   estado);
        params.put("pv_nmpoliza_i", nmpoliza);   
        InputStream archivo =  null;
        try {
            Map<String, Object> resultado = ejecutaSP(new ObtenerReporteRecibos(getDataSource()), params);
            logger.debug("resultado:"+resultado);
            ArrayList<InputStream> inputList = (ArrayList<InputStream>) resultado.get("pv_registro_o");
            archivo = inputList.get(0);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        
        return archivo;
    }
        
    protected class ObtenerReporteRecibos extends StoredProcedure {
        protected ObtenerReporteRecibos(DataSource dataSource) {
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_GET_REP_REC_SISA");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new ObtieneReporteMapper()));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public List<Map<String, String>> obtenerBitacoraConsolidacion(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();   
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i",   cdramo);
        params.put("pv_estado_i",   estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        Map<String, Object> resultado = ejecutaSP(new ObtenerBitacoraConsolidacion(getDataSource()), params);
        logger.debug(Utils.log(
                "\n###########################################",
                "\n###### obtenerBitacoraConsolidacion ######",
                "\n###### resultado=",resultado
               ));
        return (List<Map<String, String>>) resultado.get("pv_registro_o");
    }
        
    protected class ObtenerBitacoraConsolidacion extends StoredProcedure {
        protected ObtenerBitacoraConsolidacion(DataSource dataSource) {
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_OBTIENE_TBITACONS");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            String cols[] = new String[] {
                    "USUARIO",
                    "RECIBOS",
                    "ACCION",
                    "FECHA",
                    "NMFOLCON"};
            declareParameter(new SqlOutParameter("pv_registro_o", OracleTypes.CURSOR, new GenericMapper(cols)));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public void borrarDocumentoReciboConsolidado(String cdunieco, String cdramo, String estado, String nmpoliza, String nmfolio) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();   
        params.put("pv_cdunieco_i", cdunieco);
        params.put("pv_cdramo_i",   cdramo);
        params.put("pv_estado_i",   estado);
        params.put("pv_nmpoliza_i", nmpoliza);
        params.put("pv_nmfolcon_i", nmfolio);
        ejecutaSP(new BorrarDocumentoReciboConsolidado(getDataSource()), params);
        logger.debug(Utils.log(
                "\n###########################################",
                "\n###### borrarDocumentoReciboConsolidado ######"
               ));
    }
        
    protected class BorrarDocumentoReciboConsolidado extends StoredProcedure {
        protected BorrarDocumentoReciboConsolidado(DataSource dataSource) {
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_BORRAR_TDOCUPOL_RECIBO");
            declareParameter(new SqlParameter("pv_cdunieco_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdramo_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_estado_i",      OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmpoliza_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_nmfolcon_i",    OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o",   OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o",    OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String obtenerLigaRecibo(String cdunieco, String cdramo, String estado, String nmpoliza, String folio) throws Exception{
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("cdunieco", cdunieco);
        params.put("cdramo"  , cdramo);
        params.put("estado"  , estado);
        params.put("nmpoliza", nmpoliza);
        params.put("nmfolcon", folio);
        Map<String,Object> procResult = ejecutaSP(new ObtenerLigaRecibo(getDataSource()),params);
        String liga                  = procResult.get("pv_liga_o").toString();
        return liga;
    }
    
    protected class ObtenerLigaRecibo extends StoredProcedure{
        protected ObtenerLigaRecibo(DataSource dataSource){
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_OBTENER_LIGA_RECIBO_CON");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmfolcon" , OracleTypes.VARCHAR));            
            declareParameter(new SqlOutParameter("pv_liga_o"    , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String obtenerSuplementoEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("cdunieco", cdunieco);
        params.put("cdramo"  , cdramo);
        params.put("estado"  , estado);
        params.put("nmpoliza", nmpoliza);
        Map<String,Object> procResult = ejecutaSP(new ObtenerSuplementoEmision(getDataSource()),params);
        String nmsuplem               = procResult.get("pv_nmsuplem_o").toString();
        return nmsuplem;
    }
    
    protected class ObtenerSuplementoEmision extends StoredProcedure{
        protected ObtenerSuplementoEmision(DataSource dataSource){
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_OBTENER_SUPLEM_EMI");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_nmsuplem_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
    
    @Override
    public String obtenerTramiteEmision(String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception{
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("cdunieco", cdunieco);
        params.put("cdramo"  , cdramo);
        params.put("estado"  , estado);
        params.put("nmpoliza", nmpoliza);
        Map<String,Object> procResult = ejecutaSP(new ObtenerTramiteEmision(getDataSource()),params);
        String nmsuplem               = procResult.get("pv_ntramite_o").toString();
        return nmsuplem;
    }
    
    protected class ObtenerTramiteEmision extends StoredProcedure{
        protected ObtenerTramiteEmision(DataSource dataSource){
            super(dataSource,"PKG_RECIBOS_SISA_SIGS.P_OBTENER_TRAMITE_EMI");
            declareParameter(new SqlParameter("cdunieco" , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("cdramo"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("estado"   , OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("nmpoliza" , OracleTypes.VARCHAR));
            declareParameter(new SqlOutParameter("pv_ntramite_o", OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_msg_id_o"  , OracleTypes.NUMERIC));
            declareParameter(new SqlOutParameter("pv_title_o"   , OracleTypes.VARCHAR));
            compile();
        }
    }
}