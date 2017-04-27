package com.peritaje.siniestro.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.gseguros.utils.HttpUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.peritaje.siniestro.manager.PeritajeManager;

public class PeritajeAction extends PrincipalCoreAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6407965208108117295L;
	public static final String consNumeroSiniestro          = "NMSINIES";
	public static final String consNumeroPoliza             = "NMPOLIZA";
	public static final String consNumeroCertificado        = "NMCERTI";
	public static final String consNombreAsegurado          = "NOMBRE";
	public static final String consTelefonoOficinaAsegurado = "TELEOFICIN";
	public static final String consTelefonoCelularAsegurado = "CELULAR";
	
	private static Logger logger = Logger.getLogger(PeritajeAction.class);
	
	private Map<String,String>       strMapIn;
	private Map<String,String>       strMapOut;
	private List<Map<String,String>> strListMapIn;
	private List<Map<String,String>> strListMapOut;
	private PeritajeManager          peritajeManager;
	private String                   mensaje;
	private boolean                  success;
	private InputStream              fileInputStream;
	private String                   contentType;
	private String                   filename;

	@Value("${ruta.servidor.reports}")
    private String rutaServidorReports;
	
	@Value("${pass.servidor.reports}")
    private String passServidorReports;	
	
	@Value("${ruta.documentos.eperitaje}")
    private String rutaDocumentosEperitaje;
	
	public String execute()
	{
		logger.info(""
				+ "\n#####################"
				+ "\n###### execute ######"
				);
		logger.info(""
				+ "\n###### execute ######"
				+ "\n#####################"
				);
		return SUCCESS;
	}
	
	public String obtenerListaOrdenesInspeccion()
	{
		logger.info(""
				+ "\n###########################################"
				+ "\n###### obtenerListaOrdenesInspeccion ######"
				);
		try
		{
			strListMapOut = peritajeManager.obtenerListaOrdenesInspeccion();
			success       = true;
			mensaje       = "Todo OK";
			logger.info("strListMapOut lista size: "+strListMapOut.size());
		}
		catch(Exception ex)
		{
			logger.error("error al obtener la lista de ordenes de inspeccion",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerListaOrdenesInspeccion ######"
				+ "\n###########################################"
				);
		return SUCCESS;
	}
	
	public String obtenerListaOrdenesAjuste()
	{
		logger.info(""
				+ "\n#######################################"
				+ "\n###### obtenerListaOrdenesAjuste ######"
				);
		try
		{
			strListMapOut = peritajeManager.obtenerListaOrdenesAjuste();
			success       = true;
			mensaje       = "Todo OK";
			logger.info("strListMapOut lista size: "+strListMapOut.size());
		}
		catch(Exception ex)
		{
			logger.error("error al obtener la lista de ordenes de ajuste",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerListaOrdenesAjuste ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDetalleOrdenInspeccion()
	{
		logger.info(""
				+ "\n###########################################"
				+ "\n###### obtenerDetalleOrdenInspeccion ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDetalleOrdenInspeccion(strMapIn.get("nmorden"));
			success   = true;
			mensaje   = "Todo OK";
			logger.info("strMapOut: "+strMapOut);
		}
		catch(Exception ex)
		{
			logger.error("error al obtener el detalle de la orden de inspeccion",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerDetalleOrdenInspeccion ######"
				+ "\n###########################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDetalleOrdenAjuste()
	{
		logger.info(""
				+ "\n#######################################"
				+ "\n###### obtenerDetalleOrdenAjuste ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDetalleOrdenAjuste(strMapIn.get("nmorden"));
			success   = true;
			mensaje   = "Todo OK";
			logger.info("strMapOut: "+strMapOut);
		}
		catch(Exception ex)
		{
			logger.error("error al obtener el detalle de la orden de ajuste",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerDetalleOrdenAjuste ######"
				+ "\n#######################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDatosVehiculo()
	{
		logger.info(""
				+ "\n##################################"
				+ "\n###### obtenerDatosVehiculo ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDatosVehiculo(strMapIn.get("nmorden"));
			success   = true;
			mensaje   = "Todo OK";
			logger.info("strMapOut: "+strMapOut);
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los datos del vehiculo",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerDatosVehiculo ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	
	/*
	 * strMapIn:
		CAPPUESTOS: 5
		DESTINADO: "CARGA"
		KMACTUAL: 5500
		NMORDEN
		POSEECABINA: 1
		SERIALCARR: "2HHYD2821BH900118"
		SERIALMOT: "J37A17000819"
		TAPICERIA: "TELA"
		TONELADAS: 3.5
		TRANSMISION: "SINCRONICA"
	 */
	public String guardarDatosVehiculo()
	{
		logger.info(""
				+ "\n##################################"
				+ "\n###### guardarDatosVehiculo ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			String nmorden = strMapIn.get("NMORDEN");
			String serialmot = strMapIn.get("SERIALMOT");
			String serialcarr = strMapIn.get("SERIALCARR");
			String kmactual = strMapIn.get("KMACTUAL");
			String cappuestos = strMapIn.get("CAPPUESTOS");
			String transmision = strMapIn.get("TRANSMISION");
			String tapiceria = strMapIn.get("TAPICERIA");
			String poseecabina = strMapIn.get("POSEECABINA");
			if(poseecabina.equalsIgnoreCase("1"))
			{
				poseecabina="SI";
			}
			else
			{
				poseecabina="NO";
			}
			String destinado = strMapIn.get("DESTINADO");
			String toneladas = strMapIn.get("TONELADAS");
			peritajeManager.guardarDatosVehiculo(nmorden, serialmot, serialcarr, kmactual,
					cappuestos, transmision, tapiceria, poseecabina, destinado, toneladas);
			success = true;
			mensaje = "Datos guardados";
		}
		catch(Exception ex)
		{
			logger.error("error al guardar datos del vehiculo",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### guardarDatosVehiculo ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDatosSeguridad()
	{
		logger.info(""
				+ "\n###################################"
				+ "\n###### obtenerDatosSeguridad ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDatosSeguridad(strMapIn.get("nmorden"));
			success   = true;
			mensaje   = "Todo OK";
			logger.info("strMapOut: "+strMapOut);
		}
		catch(Exception ex)
		{
			logger.error("error al obtener los datos de seguridad",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerDatosSeguridad ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	/*
	 * strMapIn:
	 * ALARMA
	 * BOVEDA
	 * CORTACORR
	 * DISPSATELITAL
	 * GRABAVIDRIO
	 * NMORDEN
	 * TRANCAPALAN
	 * TRANCAPEDAL
	 */
	public String guardarDatosSeguridad()
	{
		logger.info(""
				+ "\n###################################"
				+ "\n###### guardarDatosSeguridad ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			String nmorden       = strMapIn.get("NMORDEN");
			String alarma        = strMapIn.get("ALARMA");
			String boveda        = strMapIn.get("BOVEDA");
			String cortacorr     = strMapIn.get("CORTACORR");
			String dispsatelital = strMapIn.get("DISPSATELITAL");
			String grabavidrio   = strMapIn.get("GRABAVIDRIO");
			String trancapalan   = strMapIn.get("TRANCAPALAN");
			String trancapedal   = strMapIn.get("TRANCAPEDAL");
			if(alarma.equalsIgnoreCase("1"))
			{
				alarma="SI";
			}
			else
			{
				alarma="NO";
			}
			if(boveda.equalsIgnoreCase("1"))
			{
				boveda="SI";
			}
			else
			{
				boveda="NO";
			}
			if(cortacorr.equalsIgnoreCase("1"))
			{
				cortacorr="SI";
			}
			else
			{
				cortacorr="NO";
			}
			if(dispsatelital.equalsIgnoreCase("1"))
			{
				dispsatelital="SI";
			}
			else
			{
				dispsatelital="NO";
			}
			if(grabavidrio.equalsIgnoreCase("1"))
			{
				grabavidrio="SI";
			}
			else
			{
				grabavidrio="NO";
			}
			if(trancapalan.equalsIgnoreCase("1"))
			{
				trancapalan="SI";
			}
			else
			{
				trancapalan="NO";
			}
			if(trancapedal.equalsIgnoreCase("1"))
			{
				trancapedal="SI";
			}
			else
			{
				trancapedal="NO";
			}
			peritajeManager.guardarDatosSeguridad(nmorden, alarma, boveda, cortacorr,
					dispsatelital, grabavidrio, trancapalan, trancapedal);
			success = true;
			mensaje = "Datos guardados";
		}
		catch(Exception ex)
		{
			logger.error("error al guardar datos de seguridad",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### guardarDatosSeguridad ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDetalleAccesorios()
	{
		logger.info(""
				+ "\n######################################"
				+ "\n###### obtenerDetalleAccesorios ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDetalleAccesorios(strMapIn.get("nmorden"));
			success   = true;
			mensaje   = "Todo OK";
			logger.info("strMapOut: "+strMapOut);
		}
		catch(Exception ex)
		{
			logger.error("error al obtener el detalle de accesorios",ex);
			success = false;
			mensaje = ex.getMessage();
		}
		logger.info(""
				+ "\n###### obtenerDetalleAccesorios ######"
				+ "\n######################################"
				);
		return SUCCESS;
	}

	public String guardarDetalleAccesorios()
	{
		logger.info(""
				+ "\n######################################"
				+ "\n###### guardarDetalleAccesorios ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			String nmorden      = strMapIn.get("NMORDEN");
			String radiofijo    = strMapIn.get("RADIOFIJO");
			String radiorepro   = strMapIn.get("RADIOREPRO");
			String radiocd      = strMapIn.get("RADIOCD");
			String llantarepues = strMapIn.get("LLANTAREPUES");
			String aireacondic  = strMapIn.get("AIREACONDIC");
			String blindaje     = strMapIn.get("BLINDAJE");
			String estacaoplata = strMapIn.get("ESTACAOPLATA");
			String cava         = strMapIn.get("CAVA");
			String rines        = strMapIn.get("RINES");
			String tazas        = strMapIn.get("TAZAS");
			if(radiofijo.equalsIgnoreCase("1"))
			{
				radiofijo="SI";
			}
			else
			{
				radiofijo="NO";
			}
			if(radiorepro.equalsIgnoreCase("1"))
			{
				radiorepro="SI";
			}
			else
			{
				radiorepro="NO";
			}
			if(radiocd.equalsIgnoreCase("1"))
			{
				radiocd="SI";
			}
			else
			{
				radiocd="NO";
			}
			if(llantarepues.equalsIgnoreCase("1"))
			{
				llantarepues="SI";
			}
			else
			{
				llantarepues="NO";
			}
			if(aireacondic.equalsIgnoreCase("1"))
			{
				aireacondic="SI";
			}
			else
			{
				aireacondic="NO";
			}
			if(blindaje.equalsIgnoreCase("1"))
			{
				blindaje="SI";
			}
			else
			{
				blindaje="NO";
			}
			if(estacaoplata.equalsIgnoreCase("1"))
			{
				estacaoplata="SI";
			}
			else
			{
				estacaoplata="NO";
			}
			peritajeManager.guardarDetalleAccesorios(nmorden, radiofijo, radiorepro, radiocd,
					llantarepues, aireacondic, blindaje, estacaoplata, cava, rines, tazas);
			success = true;
			mensaje = "Datos guardados";
			
		}
		catch(Exception ex)
		{
			logger.error("error al guardar detalle de accesorios",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### guardarDetalleAccesorios ######"
				+ "\n######################################"
				);
		return SUCCESS;
	}
	
	public String guardarDetalleInspeccion()
	{
		logger.info(""
				+ "\n######################################"
				+ "\n###### guardarDetalleInspeccion ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			boolean ajuste       = strMapIn.containsKey("ajuste");
			String nmorden       = strMapIn.get("NMORDEN");
			String otvalor01     = strMapIn.get("otvalor01");
			String otvalor02     = strMapIn.get("otvalor02");
			String otvalor03     = strMapIn.get("otvalor03");
			String otvalor04     = strMapIn.get("otvalor04");
			String otvalor05     = strMapIn.get("otvalor05");
			String otvalor06     = strMapIn.get("otvalor06");
			String otvalor07     = strMapIn.get("otvalor07");
			String otvalor08     = strMapIn.get("otvalor08");
			String otvalor09     = strMapIn.get("otvalor09");
			String otvalor10     = strMapIn.get("otvalor10");
			String otvalor11     = strMapIn.get("otvalor11");
			String otvalor12     = strMapIn.get("otvalor12");
			String otvalor13     = strMapIn.get("otvalor13");
			String otvalor14     = strMapIn.get("otvalor14");
			String otvalor15     = strMapIn.get("otvalor15");
			String otvalor16     = strMapIn.get("otvalor16");
			String otvalor17     = strMapIn.get("otvalor17");
			String otvalor18     = strMapIn.get("otvalor18");
			String otvalor19     = strMapIn.get("otvalor19");
			String otvalor20     = strMapIn.get("otvalor20");
			String otvalor21     = strMapIn.get("otvalor21");
			String otvalor22     = strMapIn.get("otvalor22");
			String otvalor23     = strMapIn.get("otvalor23");
			String otvalor24     = strMapIn.get("otvalor24");
			String otvalor25     = strMapIn.get("otvalor25");
			String otvalor26     = strMapIn.get("otvalor26");
			String otvalor27     = strMapIn.get("otvalor27");
			String otvalor28     = strMapIn.get("otvalor28");
			String otvalor29     = strMapIn.get("otvalor29");
			String otvalor30     = strMapIn.get("otvalor30");
			String observaciones = strMapIn.get("OBSERVACIONES");
			peritajeManager.guardarDetalleInspeccion(nmorden,
					otvalor01  , otvalor02 , otvalor03 , otvalor04 , otvalor05
					,otvalor06 , otvalor07 , otvalor08 , otvalor09 , otvalor10
					,otvalor11 , otvalor12 , otvalor13 , otvalor14 , otvalor15
					,otvalor16 , otvalor17 , otvalor18 , otvalor19 , otvalor20
					,otvalor21 , otvalor22 , otvalor23 , otvalor24 , otvalor25
					,otvalor26 , otvalor27 , otvalor28 , otvalor29 , otvalor30
					,observaciones
					);
			
			String rutaReports    = rutaServidorReports;
			String passReports    = passServidorReports;
			String nombreReporte  = "INSPE_VEHICULO.rdf";
			if(ajuste)
			{
				nombreReporte  = "ORDENAUTO.rdf";
			}
			String rutaDocumentos = rutaDocumentosEperitaje;
			String rutaCarpeta    = rutaDocumentos+"/"+nmorden;
			String nombreArchivo  = "/inspeccion_"+(System.currentTimeMillis())+".pdf";
			if(ajuste)
			{
				nombreArchivo  = "/ajuste_"+(System.currentTimeMillis())+".pdf";
			}
			String filePath       = rutaCarpeta+nombreArchivo;
			File   carpeta        = new File(rutaCarpeta);
			
			if(!carpeta.exists())
            {
            	logger.debug("no existe la carpeta::: "+carpeta);
            	carpeta.mkdir();
            	if(!carpeta.exists())
            	{
            		throw new Exception("Error al crear la carpeta");
            	}
            }
			
			String requestUrl = rutaReports
					+ "?destype=cache"
					+ "&desformat=PDF"
					+ "&userid="+passReports
					+ "&report="+nombreReporte
					+ "&paramform=no"
					+ "&ACCESSIBLE=YES"
					+ "&p_nmorden="+nmorden
					+ "&desname="+filePath;
			
			HttpUtil.generaArchivo(requestUrl, filePath);
			
			success = true;
			mensaje = filePath;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar detalle de inspeccion",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### guardarDetalleInspeccion ######"
				+ "\n######################################"
				);
		return SUCCESS;
	}
	
	public String obtenerListaRepuestosDisponibles()
	{
		logger.info(""
				+ "\n##############################################"
				+ "\n###### obtenerListaRepuestosDisponibles ######"
				);
		try
		{
			strListMapOut=peritajeManager.obtenerListaRepuestos();
			
			mensaje = "Todo OK";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener lista de repuestos disponibles",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### obtenerListaRepuestosDisponibles ######"
				+ "\n##############################################"
				);
		return SUCCESS;
	}
	
	public String obtenerListaManoObra()
	{
		logger.info(""
				+ "\n##################################"
				+ "\n###### obtenerListaManoObra ######"
				);
		try
		{
			strListMapOut=peritajeManager.obtenerListaManoObra();
			
			mensaje = "Todo OK";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener lista de mano de obra",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### obtenerListaManoObra ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDetalleAjuste()
	{
		logger.info(""
				+ "\n##################################"
				+ "\n###### obtenerDetalleAjuste ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDetalleAjuste(strMapIn.get("nmorden"));
			mensaje = "Detalle obtenido";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener detalle de ajuste",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### obtenerDetalleAjuste ######"
				+ "\n##################################"
				);
		return SUCCESS;
	}

	public String descargaDocumento()
	{
		logger.info(""
				+ "\n###############################"
				+ "\n###### descargaDocumento ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			contentType     = "application/pdf";
			filename        = strMapIn.get("filename");
			fileInputStream = new FileInputStream(strMapIn.get("ruta"));
		}
		catch (Exception ex)
		{
			logger.error("error al descargar documento",ex);
		}

		success = true;
		logger.info(""
				+ "\n###### descargaDocumento ######"
				+ "\n###############################"
				);
		return SUCCESS;
	}
	
	public String guardarRepuestosAjuste()
	{
		logger.info(""
				+ "\n####################################"
				+ "\n###### guardarRepuestosAjuste ######"
				);
		logger.info("strMapIn: "+strMapIn);
		logger.info("strListMapIn: "+strListMapIn);
		try
		{
			String nmorden = strMapIn.get("nmorden");
			peritajeManager.guardarRepuestosAjuste(nmorden,strListMapIn);
			mensaje = "Datos guardados";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar repuestos ajuste",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### guardarRepuestosAjuste ######"
				+ "\n####################################"
				);
		return SUCCESS;
	}
	
	public String guardarManoObraAjuste()
	{
		logger.info(""
				+ "\n###################################"
				+ "\n###### guardarManoObraAjuste ######"
				);
		logger.info("strMapIn: "+strMapIn);
		logger.info("strListMapIn: "+strListMapIn);
		try
		{
			String nmorden = strMapIn.get("nmorden");
			peritajeManager.guardarManoObraAjuste(nmorden,strListMapIn);
			mensaje = "Datos guardados";
			success = true;
		}
		catch(Exception ex)
		{
			logger.error("error al guardar mano obra ajuste",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### guardarManoObraAjuste ######"
				+ "\n###################################"
				);
		return SUCCESS;
	}
	
	public String obtenerDatosPresupuesto()
	{
		logger.info(""
				+ "\n#####################################"
				+ "\n###### obtenerDatosPresupuesto ######"
				);
		logger.info("strMapIn: "+strMapIn);
		try
		{
			strMapOut = peritajeManager.obtenerDatosPresupuesto(strMapIn.get("nmorden"));
			mensaje   = "Presupuesto obtenido";
			success   = true;
		}
		catch(Exception ex)
		{
			logger.error("error al obtener datos de presupuesto",ex);
			mensaje = ex.getMessage();
			success = false;
		}
		logger.info(""
				+ "\n###### obtenerDatosPresupuesto ######"
				+ "\n#####################################"
				);
		return SUCCESS;
	}
	
	public Map<String, String> getStrMapIn() {
		return strMapIn;
	}

	public void setStrMapIn(Map<String, String> strMapIn) {
		this.strMapIn = strMapIn;
	}

	public Map<String, String> getStrMapOut() {
		return strMapOut;
	}

	public void setStrMapOut(Map<String, String> strMapOut) {
		this.strMapOut = strMapOut;
	}

	public List<Map<String, String>> getStrListMapIn() {
		return strListMapIn;
	}

	public void setStrListMapIn(List<Map<String, String>> strListMapIn) {
		this.strListMapIn = strListMapIn;
	}

	public List<Map<String, String>> getStrListMapOut() {
		return strListMapOut;
	}

	public void setStrListMapOut(List<Map<String, String>> strListMapOut) {
		this.strListMapOut = strListMapOut;
	}

	public void setPeritajeManager(PeritajeManager peritajeManager) {
		this.peritajeManager = peritajeManager;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getConsNumeroPoliza() {
		return consNumeroPoliza;
	}

	public String getConsNumeroCertificado() {
		return consNumeroCertificado;
	}

	public String getConsNombreAsegurado() {
		return consNombreAsegurado;
	}

	public String getConsTelefonoOficinaAsegurado() {
		return consTelefonoOficinaAsegurado;
	}

	public String getConsTelefonoCelularAsegurado() {
		return consTelefonoCelularAsegurado;
	}
	
	public String getConsNumeroSiniestro() {
		return consNumeroSiniestro;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getRutaServidorReports() {
		return rutaServidorReports;
	}

	public String getPassServidorReports() {
		return passServidorReports;
	}

	public String getRutaDocumentosEperitaje() {
		return rutaDocumentosEperitaje;
	}
	
}