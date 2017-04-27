package com.peritaje.siniestro.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.dao.impl.GenericMapper;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.peritaje.siniestro.dao.PeritajeDAO;

public class PeritajeDAOImpl extends AbstractManagerDAO implements PeritajeDAO
{
	
	private static Logger logger = Logger.getLogger(PeritajeDAOImpl.class);
	
	@Override
	public List<Map<String,String>> obtenerListaOrdenesInspeccion(Map<String,String>params) throws Exception
	{
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerListaOrdenesInspeccion(getDataSource()), params);
		return (List<Map<String,String>>)respuesta.get("pv_registro_o");
	}
	
	protected class ObtenerListaOrdenesInspeccion extends StoredProcedure
	{
		protected ObtenerListaOrdenesInspeccion(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_ORDENES_INSP");
			String[] cols=new String[]{
					"NMORDEN","NMPLACA","MARCA","MODELO","COLOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerListaOrdenesAjuste(Map<String,String>params) throws Exception
	{
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerListaOrdenesAjuste(getDataSource()), params);
		return (List<Map<String,String>>)respuesta.get("pv_registro_o");
	}
	
	protected class ObtenerListaOrdenesAjuste extends StoredProcedure
	{
		protected ObtenerListaOrdenesAjuste(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_ORDENES_AJUS");
			String[] cols=new String[]{
					"NMORDEN","NMPLACA","MARCA","MODELO","COLOR"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDetalleOrdenInspeccion(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_nmorden_i",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDetalleOrdenInspeccion(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe la orden");
		}
		if(lista.size()>1)
		{
			throw new Exception("Orden duplicada");
		}
		return lista.get(0);
	}
	
	protected class ObtenerDetalleOrdenInspeccion extends StoredProcedure
	{
		protected ObtenerDetalleOrdenInspeccion(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_DET_ORDENES_INSP");
			declareParameter(new SqlParameter("pv_nmorden_i", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMORDEN" , "TIPOINSP" , "LUGARINSP" , "NMPLACA" , "MARCA"
					,"MODELO" , "VERSION"  , "COLOR"     , "ANNO"    , "SERIALMOT" , "SERIALCARR" , "OBSERVACIONES"
					};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDetalleOrdenAjuste(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_nmorden_i",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDetalleOrdenAjuste(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe la orden");
		}
		if(lista.size()>1)
		{
			throw new Exception("Orden duplicada");
		}
		return lista.get(0);
	}
	
	protected class ObtenerDetalleOrdenAjuste extends StoredProcedure
	{
		protected ObtenerDetalleOrdenAjuste(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_DET_ORDENES_AJUS");
			declareParameter(new SqlParameter("pv_nmorden_i", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMORDEN" , "LUGARAJUS" , "NMPLACA"   , "MARCA"      , "MODELO"       , "VERSION"
					,"COLOR"  , "ANNO"      , "SERIALMOT" , "SERIALCARR" , "DECLASINIEST" , "DESCRIPDANOS"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDatosVehiculo(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_nmorden_i",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDatosVehiculo(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe la orden");
		}
		if(lista.size()>1)
		{
			throw new Exception("Orden duplicada");
		}
		return lista.get(0);
	}
	
	protected class ObtenerDatosVehiculo extends StoredProcedure
	{
		protected ObtenerDatosVehiculo(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_DET_DATOS_VEHICULOS");
			declareParameter(new SqlParameter("pv_nmorden_i", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMORDEN"      , "SERIALMOT" , "SERIALCARR"  , "KMACTUAL"  , "CAPPUESTOS"
					,"TRANSMISION" , "TAPICERIA" , "POSEECABINA" , "DESTINADO" , "TONELADAS"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void guardarDatosVehiculo(
			String nmorden
			,String serialmot
			,String serialcarr
			,String kmactual
			,String cappuestos
			,String transmision
			,String tapiceria
			,String poseecabina
			,String destinado
			,String toneladas) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_nmorden_i"     , nmorden);
		params.put("pv_serialmot_i"   , serialmot);
		params.put("pv_serialcarr_i"  , serialcarr);
		params.put("pv_kmactual_i"    , kmactual);
		params.put("pv_cappuestos_i"  , cappuestos);
		params.put("pv_transmision_i" , transmision);
		params.put("pv_tapiceria_i"   , tapiceria);
		params.put("pv_poseecabina_i" , poseecabina);
		params.put("pv_destinado_i"   , destinado);
		params.put("pv_toneladas_i"   , toneladas);
		logger.debug("params: "+params);
		this.ejecutaSP(new GuardarDatosVehiculo(getDataSource()), params);
	}
	
	protected class GuardarDatosVehiculo extends StoredProcedure
	{
		protected GuardarDatosVehiculo(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.MOV_DATOS_VEHICULOS");
			declareParameter(new SqlParameter("pv_nmorden_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_serialmot_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_serialcarr_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_kmactual_i"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cappuestos_i"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_transmision_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_tapiceria_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_poseecabina_i" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_destinado_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_toneladas_i"   , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDatosSeguridad(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_nmorden_i",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDatosSeguridad(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe la orden");
		}
		if(lista.size()>1)
		{
			throw new Exception("Orden duplicada");
		}
		return lista.get(0);
	}
	
	protected class ObtenerDatosSeguridad extends StoredProcedure
	{
		protected ObtenerDatosSeguridad(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_DET_DATOS_SEGURIDAD");
			declareParameter(new SqlParameter("pv_nmorden_i", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMORDEN","CORTACORR","ALARMA","BOVEDA","GRABAVIDRIO","TRANCAPEDAL","TRANCAPALAN","DISPSATELITAL"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void guardarDatosSeguridad(String nmorden, String alarma,
			String boveda, String cortacorr, String dispsatelital,
			String grabavidrio, String trancapalan, String trancapedal) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("pv_nmorden_i"       , nmorden);
		params.put("pv_alarma_i"        , alarma);
		params.put("pv_boveda_i"        , boveda);
		params.put("pv_cortacorr_i"     , cortacorr);
		params.put("pv_dispsatelital_i" , dispsatelital);
		params.put("pv_grabavidrio_i"   , grabavidrio);
		params.put("pv_trancapalan_i"   , trancapalan);
		params.put("pv_trancapedal_i"   , trancapedal);
		logger.debug("params: "+params);
		this.ejecutaSP(new GuardarDatosSeguridad(getDataSource()), params);
	}
	
	protected class GuardarDatosSeguridad extends StoredProcedure
	{
		protected GuardarDatosSeguridad(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.MOV_DATOS_SEGURIDAD");
			declareParameter(new SqlParameter("pv_nmorden_i"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_cortacorr_i"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_alarma_i"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_boveda_i"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_grabavidrio_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_trancapedal_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_trancapalan_i"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("pv_dispsatelital_i" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDetalleAccesorios(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("nmorden",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDetalleAccesorios(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe la orden");
		}
		if(lista.size()>1)
		{
			throw new Exception("Orden duplicada");
		}
		return lista.get(0);
	}
	
	protected class ObtenerDetalleAccesorios extends StoredProcedure
	{
		protected ObtenerDetalleAccesorios(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_DET_DATOS_ACCESORIOS");
			declareParameter(new SqlParameter("nmorden", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMORDEN"      , "RADIOFIJO" , "RADIOREPRO"   , "RADIOCD" , "LLANTAREPUES"
					,"AIREACONDIC" , "BLINDAJE"  , "ESTACAOPLATA" , "CAVA"    , "RINES"        , "TAZAS"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void guardarDetalleAccesorios(String nmorden, String radiofijo,
			String radiorepro, String radiocd, String llantarepues,
			String aireacondic, String blindaje, String estacaoplata,
			String cava, String rines, String tazas) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("nmorden"      , nmorden);
		params.put("radiofijo"    , radiofijo);
		params.put("radiorepro"   , radiorepro);
		params.put("radiocd"      , radiocd);
		params.put("llantarepues" , llantarepues);
		params.put("aireacondic"  , aireacondic);
		params.put("blindaje"     , blindaje);
		params.put("estacaoplata" , estacaoplata);
		params.put("cava"         , cava);
		params.put("rines"        , rines);
		params.put("tazas"        , tazas);
		logger.debug("params: "+params);
		this.ejecutaSP(new GuardarDetalleAccesorios(getDataSource()), params);
	}
	
	protected class GuardarDetalleAccesorios extends StoredProcedure
	{
		protected GuardarDetalleAccesorios(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.MOV_DATOS_ACCESORIOS");
			declareParameter(new SqlParameter("nmorden"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("radiofijo"    , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("radiorepro"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("radiocd"      , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("llantarepues" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("aireacondic"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("blindaje"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("estacaoplata" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("cava"         , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("rines"        , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("tazas"        , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void guardarDetalleInspeccion(String nmorden, String otvalor01,
			String otvalor02, String otvalor03, String otvalor04,
			String otvalor05, String otvalor06, String otvalor07,
			String otvalor08, String otvalor09, String otvalor10,
			String otvalor11, String otvalor12, String otvalor13,
			String otvalor14, String otvalor15, String otvalor16,
			String otvalor17, String otvalor18, String otvalor19,
			String otvalor20, String otvalor21, String otvalor22,
			String otvalor23, String otvalor24, String otvalor25,
			String otvalor26, String otvalor27, String otvalor28,
			String otvalor29, String otvalor30, String observaciones) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("nmorden"       , nmorden);
		params.put("otvalor01"     , otvalor01);
		params.put("otvalor02"     , otvalor02);
		params.put("otvalor03"     , otvalor03);
		params.put("otvalor04"     , otvalor04);
		params.put("otvalor05"     , otvalor05);
		params.put("otvalor06"     , otvalor06);
		params.put("otvalor07"     , otvalor07);
		params.put("otvalor08"     , otvalor08);
		params.put("otvalor09"     , otvalor09);
		params.put("otvalor10"     , otvalor10);
		params.put("otvalor11"     , otvalor11);
		params.put("otvalor12"     , otvalor12);
		params.put("otvalor13"     , otvalor13);
		params.put("otvalor14"     , otvalor14);
		params.put("otvalor15"     , otvalor15);
		params.put("otvalor16"     , otvalor16);
		params.put("otvalor17"     , otvalor17);
		params.put("otvalor18"     , otvalor18);
		params.put("otvalor19"     , otvalor19);
		params.put("otvalor20"     , otvalor20);
		params.put("otvalor21"     , otvalor21);
		params.put("otvalor22"     , otvalor22);
		params.put("otvalor23"     , otvalor23);
		params.put("otvalor24"     , otvalor24);
		params.put("otvalor25"     , otvalor25);
		params.put("otvalor26"     , otvalor26);
		params.put("otvalor27"     , otvalor27);
		params.put("otvalor28"     , otvalor28);
		params.put("otvalor29"     , otvalor29);
		params.put("otvalor30"     , otvalor30);
		params.put("observaciones" , observaciones);
		logger.debug("params: "+params);
		this.ejecutaSP(new GuardarDetalleInspeccion(getDataSource()), params);
	}
	
	protected class GuardarDetalleInspeccion extends StoredProcedure
	{
		protected GuardarDetalleInspeccion(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.MOV_DATOS_DET_INSPEC");
			declareParameter(new SqlParameter("nmorden"       , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor01"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor02"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor03"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor04"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor05"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor06"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor07"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor08"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor09"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor10"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor11"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor12"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor13"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor14"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor15"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor16"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor17"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor18"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor19"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor20"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor21"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor22"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor23"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor24"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor25"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor26"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor27"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor28"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor29"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor30"     , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("observaciones" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDetalleAjuste(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("nmorden",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDetalleAjuste(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("No existe la orden");
		}
		if(lista.size()>1)
		{
			throw new Exception("Orden duplicada");
		}
		return lista.get(0);
	}
	
	protected class ObtenerDetalleAjuste extends StoredProcedure
	{
		protected ObtenerDetalleAjuste(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_DET_DATOS_AJUS");
			declareParameter(new SqlParameter("nmorden", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"NMORDEN"  , "NMSINIES" , "NMPOLIZA" , "NMCERTI" , "NOMBRE" , "TELEOFICIN"
					,"CELULAR" , "MARCA"    , "MODELO"   , "NMPLACA" , "ANNO"   , "TRANSMISION" , "KMACTUAl"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerListaRepuestos(Map<String,String>params) throws Exception
	{
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerListaRepuestos(getDataSource()), params);
		return (List<Map<String,String>>)respuesta.get("pv_registro_o");
	}
	
	protected class ObtenerListaRepuestos extends StoredProcedure
	{
		protected ObtenerListaRepuestos(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_LISTA_REPUESTOS");
			String[] cols=new String[]{
					"CODIGO" , "DESCRIPCION" , "MONTO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public List<Map<String,String>> obtenerListaManoObra(Map<String,String>params) throws Exception
	{
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerListaManoObra(getDataSource()), params);
		return (List<Map<String,String>>)respuesta.get("pv_registro_o");
	}
	
	protected class ObtenerListaManoObra extends StoredProcedure
	{
		protected ObtenerListaManoObra(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_LISTA_MANODOBRA");
			String[] cols=new String[]{
					"CODIGO" , "DESCRIPCION" , "MONTO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void guardarRepuestosAjuste(String nmorden,List<Map<String, String>> repuestos) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("nmorden",nmorden);
		int i=1;
		for(Map<String,String>repuesto:repuestos)
		{
			params.put("otvalor"+i,repuesto.get("otvalor"));
			params.put("monto"+i,repuesto.get("monto"));
			i=i+1;
		}
		for(i=1;i<=50;i++)
		{
			if(!params.containsKey("otvalor"+i))
			{
				params.put("otvalor"+i,null);
			}
			if(!params.containsKey("monto"+i))
			{
				params.put("monto"+i,null);
			}
		}
		logger.debug("params: "+params);
		this.ejecutaSP(new GuardarRepuestosAjuste(getDataSource()), params);
	}
	
	protected class GuardarRepuestosAjuste extends StoredProcedure
	{
		protected GuardarRepuestosAjuste(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.MOV_DATOS_REPUESTOS");
			declareParameter(new SqlParameter("nmorden"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor1"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto1"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor2"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto2"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor3"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto3"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor4"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto4"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor5"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto5"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor6"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto6"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor7"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto7"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor8"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto8"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor9"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto9"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor40" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto40" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor41" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto41" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor42" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto42" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor43" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto43" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor44" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto44" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor45" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto45" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor46" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto46" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor47" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto47" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor48" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto48" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor49" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto49" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor50" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto50" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public void guardarManoObraAjuste(String nmorden,List<Map<String, String>> repuestos) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("nmorden",nmorden);
		int i=1;
		for(Map<String,String>repuesto:repuestos)
		{
			params.put("otvalor"+i,repuesto.get("otvalor"));
			params.put("monto"+i,repuesto.get("monto"));
			i=i+1;
		}
		for(i=1;i<=40;i++)
		{
			if(!params.containsKey("otvalor"+i))
			{
				params.put("otvalor"+i,null);
			}
			if(!params.containsKey("monto"+i))
			{
				params.put("monto"+i,null);
			}
		}
		logger.debug("params: "+params);
		this.ejecutaSP(new GuardarManoObraAjuste(getDataSource()), params);
	}
	
	protected class GuardarManoObraAjuste extends StoredProcedure
	{
		protected GuardarManoObraAjuste(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.MOV_DATOS_MANOOBRA");
			declareParameter(new SqlParameter("nmorden"   , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor1"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto1"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor2"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto2"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor3"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto3"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor4"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto4"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor5"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto5"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor6"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto6"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor7"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto7"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor8"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto8"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor9"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto9"  , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto10" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto11" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto12" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto13" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto14" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto15" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto16" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto17" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto18" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto19" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto20" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto21" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto22" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto23" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto24" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto25" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto26" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto27" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto28" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto29" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto30" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto31" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto32" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto33" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto34" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto35" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto36" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto37" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto38" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto39" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter("otvalor40" , OracleTypes.VARCHAR));
			declareParameter(new SqlParameter(  "monto40" , OracleTypes.VARCHAR));
			
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
	@Override
	public Map<String,String> obtenerDatosPresupuesto(String nmorden) throws Exception
	{
		Map<String,String>params=new HashMap<String,String>();
		params.put("NMORDEN",nmorden);
		Map<String,Object>respuesta=this.ejecutaSP(new ObtenerDatosPresupuesto(getDataSource()), params);
		List<Map<String,String>>lista=(List<Map<String,String>>)respuesta.get("pv_registro_o");
		if(lista==null||lista.size()==0)
		{
			throw new Exception("Error al obtener presupuesto");
		}
		logger.debug("lista: "+lista);
		String res[]=new String[]{
				"LATONERI"
				,"MOMECANI"
				,"CROMADO"
				,"ALINEACI"
				,"MOELECTR"
				,"REPUESTO"
				,"TAPICERI"
				,"AACONDIC"
				,"SUBTOTAL"
		};
		for(int i=0;i<lista.size();i++)
		{
			Map<String,String>linea=lista.get(i);
			params.put(res[i],linea.get("MONTO"));
		}
		logger.debug("params: "+params);
		params.put("IVA"   , String.format("%2f", (Double.parseDouble(params.get("SUBTOTAL"))*0.11d)));
		params.put("TOTAL" , String.format("%2f", (Double.parseDouble(params.get("SUBTOTAL"))*1.11d)));
		return params;
	}
	
	protected class ObtenerDatosPresupuesto extends StoredProcedure
	{
		protected ObtenerDatosPresupuesto(DataSource dataSource)
		{
			super(dataSource,"PKG_PERITAJE.GET_PRESUPUESTO");
			declareParameter(new SqlParameter("NMORDEN", OracleTypes.VARCHAR));
			String[] cols=new String[]{
					"ORDEN" , "TITULO" , "MONTO"
			};
			declareParameter(new SqlOutParameter("pv_registro_o" , OracleTypes.CURSOR  , new GenericMapper(cols)));
			declareParameter(new SqlOutParameter("pv_msg_id_o"   , OracleTypes.NUMERIC));
			declareParameter(new SqlOutParameter("pv_title_o"    , OracleTypes.VARCHAR));
		}
	}
	
}