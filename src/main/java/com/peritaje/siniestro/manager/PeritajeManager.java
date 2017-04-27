package com.peritaje.siniestro.manager;

import java.util.List;
import java.util.Map;


public interface PeritajeManager {
	
	/**
	 * NMORDEN
	 * NMPLACA
	 * MARCA
	 * MODELO
	 * COLOR
	 */
	public List<Map<String,String>> obtenerListaOrdenesInspeccion() throws Exception;
	
	public List<Map<String,String>> obtenerListaOrdenesAjuste() throws Exception;
	
	/**
	 *  ANNO: "2014"
		COLOR: "NEGRO"
		LUGARINSP: "TORRE PREVISORA"
		MARCA: "TOYOTA"
		MODELO: "COROLLA"
		NMORDEN: "191920"
		NMPLACA: "AGA59L"
		OBSERVACIONES: "ESTE VEHICULO REQUIERE REVISION ESPECIAL EN LOS CAUCHOS Y EN EL TREN DELANTERO"
		SERIALCARR: "2HHYD2821BH900118"
		SERIALMOT: "J37A17000819"
		TIPOINSP: "INSPECCIÓN"
		VERSION: "SE"
	 */
	public Map<String,String> obtenerDetalleOrdenInspeccion(String nmorden) throws Exception;
	
	/**
	 * ?
	 */
	public Map<String,String> obtenerDetalleOrdenAjuste(String nmorden) throws Exception;
	
	/**
	 * CAPPUESTOS
	 * DESTINADO
	 * KMACTUAL
	 * NMORDEN
	 * POSEECABINA
	 * SERIALCARR
	 * SERIALMOT
	 * TAPICERIA
	 * TONELADAS
	 * TRANSMISION
	 */
	public Map<String,String> obtenerDatosVehiculo(String nmorden) throws Exception;
	
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
			,String toneladas) throws Exception;
	
	/**
	 * ALARMA
		BOVEDA
		CORTACORR
		DISPSATELITAL
		GRABAVIDRIO
		NMORDEN
		TRANCAPALAN
		TRANCAPEDAL
	 */
	public Map<String,String> obtenerDatosSeguridad(String nmorden) throws Exception;

	public void guardarDatosSeguridad(String nmorden, String alarma,
			String boveda, String cortacorr, String dispsatelital,
			String grabavidrio, String trancapalan, String trancapedal) throws Exception;
	
	/**
	 * NMORDEN,RADIOFIJO,RADIOREPRO,RADIOCD,LLANTAREPUES,AIREACONDIC,BLINDAJE,ESTACAOPLATA,CAVA,RINES,TAZAS
	 */
	public Map<String,String> obtenerDetalleAccesorios(String nmorden) throws Exception;

	public void guardarDetalleAccesorios(String nmorden, String radiofijo,
			String radiorepro, String radiocd, String llantarepues,
			String aireacondic, String blindaje, String estacaoplata,
			String cava, String rines, String tazas) throws Exception;

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
			String otvalor29, String otvalor30, String observaciones) throws Exception;
	
	/**
	 * ?
	 */
	public Map<String,String> obtenerDetalleAjuste(String nmorden) throws Exception;

	/**
	 * ?
	 */
	public List<Map<String, String>> obtenerListaRepuestos() throws Exception;
	
	/**
	 * ?
	 */
	public List<Map<String, String>> obtenerListaManoObra() throws Exception;

	public void guardarRepuestosAjuste(String nmorden,List<Map<String, String>> repuestos) throws Exception;
	
	public void guardarManoObraAjuste(String nmorden,List<Map<String, String>> repuestos) throws Exception;

	/**
	 * ?
	 */
	public Map<String, String> obtenerDatosPresupuesto(String nmorden) throws Exception;
}