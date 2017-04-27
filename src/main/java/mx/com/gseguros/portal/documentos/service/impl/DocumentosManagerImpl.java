package mx.com.gseguros.portal.documentos.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.documentos.service.DocumentosManager;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DocumentosManagerImpl implements DocumentosManager
{
	private final static Logger logger = LoggerFactory.getLogger(DocumentosManagerImpl.class);
	
	@Value("${ruta.documentos.poliza}")
	private String rutaDocumentosPoliza;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Override
	public Map<String,String> generarDocumentosParametrizados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,int proceso
			,String ntramite
			,String nmsolici
			,Long stamp
			)throws Exception
	{
		if(stamp==null)
		{
			stamp = System.currentTimeMillis();
		}
		else
		{
			logger.debug(Utils.log(stamp,"Se ejecutan los documentos parametrizados asincronos"));
		}
		
		logger.debug(Utils.log(stamp
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ generarDocumentosParametrizados @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ proceso="  , proceso
				,"\n@@@@@@ ntramite=" , ntramite
				,"\n@@@@@@ nmsolici=" , nmsolici
				,"\n@@@@@@ stamp="    , stamp
				));
		
		Map<String,String> result = new HashMap<String,String>();
		String             paso   = "Iniciando generaci\u00F3n de documentos parametrizados";
		logger.debug(Utils.log(stamp,"paso=",paso));
		
		try
		{
			if(StringUtils.isBlank(ntramite)
					||StringUtils.isBlank(nmsolici))
			{
				paso = "Recuperando datos de p\u00F3liza para documentos";
				logger.debug(Utils.log(stamp,"paso=",paso));
				
				Map<String,String> datosPoliza = consultasDAO.recuperarDatosPolizaParaDocumentos(cdunieco,cdramo,estado,nmpoliza);
				ntramite = datosPoliza.get("ntramite");
				nmsolici = datosPoliza.get("nmsolici");
				
				logger.debug(Utils.log(stamp,"\ndatos recuperados ntramite=",ntramite,",nmsolici=",nmsolici));
			}
			
			result.put("ntramite" , ntramite);
			result.put("nmsolici" , nmsolici);
					
			paso = "Generando carpeta";
			logger.debug(Utils.log(stamp,"paso=",paso));
			
			String rutaCarpeta = Utils.join(rutaDocumentosPoliza,"/",ntramite);
			File   carpeta     = new File(rutaCarpeta);
			if(!carpeta.exists())
			{
				logger.debug(Utils.log(stamp,"\nNo existe la carpeta ",rutaCarpeta,", se va a crear"));
				carpeta.mkdir();
				if(carpeta.exists())
				{
					logger.debug(Utils.log(stamp,"\nCarpeta creada ",rutaCarpeta));
				}
				else
				{
					throw new ApplicationException("Error al crear la carpeta");
				}
			}
			
			paso = "Verificando proceso recibido";
			logger.debug(Utils.log(stamp,"paso=",paso));
			
			if(proceso!=DocumentosManager.PROCESO_EMISION
					&&proceso!=DocumentosManager.PROCESO_ENDOSO)
			{
				throw new ApplicationException("Tipo de proceso no soportado");
			}
			
			paso = "Generando parametrizaci\u00F3n de documentos";
			logger.debug(Utils.log(stamp,"paso=",paso));
			
			String cdorddoc = cotizacionDAO.insercionDocumentosParametrizados(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsituac
					,nmsuplem
					,proceso==DocumentosManager.PROCESO_EMISION ? "EMISION" : "ENDOSOS"
					);
			
			logger.debug(Utils.log(stamp,"\ncdorddoc generado=",cdorddoc));
			
			paso = "Generando y registrando documentos temporales";
			logger.debug(Utils.log(stamp,"paso=",paso));
			
			//List<Map<String,String>> docsATransferir = 
			cotizacionDAO.generarDocumentosBaseDatos(
            		cdorddoc
            		,nmsolici
            		,ntramite
            		);

			/*
			logger.debug(Utils.log(stamp,"\nlista documentos a transferir=",docsATransferir));

			paso = "Recuperando ruta temporal de documentos";
			logger.debug(Utils.log(stamp,"paso=",paso));
			
			String rutaDocsBaseDatos = consultasDAO.recuperarTparagen(ParametroGeneral.DIRECTORIO_REPORTES);
			logger.debug(Utils.log(stamp,"\nruta temporal documentos=",rutaDocsBaseDatos));
			
			paso = "Transfiriendo documentos";
			logger.debug(Utils.log(stamp,"paso=",paso));
			
			for(Map<String,String>doc:docsATransferir)
            {
				String origen  = Utils.join(rutaDocsBaseDatos,doc.get("CDDOCUME"));
				String destino = Utils.join(rutaDocumentosPoliza,"/",ntramite,"/",doc.get("CDDOCUME"));
            	try
            	{
            		logger.debug(Utils.log(stamp,"\nIntentando mover desde:",origen,",hacia:",destino));
            		FileUtils.moveFile(
            				new File(origen)
            				,new File(destino)
            				);
            	}
            	catch(Exception ex)
            	{
            		logger.debug(Utils.log(stamp,"\nError (#",stamp,") al mover desde:",origen,",hacia:",destino));
            		logger.error(Utils.join(stamp,"Error (#",stamp,") al transferir archivo "),ex);
            	}
            }
            */
			
			paso = "Se finaliza la generaci\u00F3n de documentos parametrizados";
			logger.debug(Utils.log(stamp,"paso=",paso));
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(stamp
				,"\n@@@@@@ result=",result
				,"\n@@@@@@ generarDocumentosParametrizados @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	@Deprecated
	public void guardarDocumento(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsuplem
			,Date feinici
			,String cddocume
			,String dsdocume
			,String nmsolici
			,String ntramite
			,String tipmov
			,String swvisible
			,String codidocu
			,String cdtiptra
			,String cdorddoc
			,Documento documento
			,String cdusuari
			,String cdsisrol, boolean sustituir
			)throws Exception
	{
		mesaControlDAO.guardarDocumento(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsuplem
				,feinici
				,cddocume
				,dsdocume
				,nmsolici
				,ntramite
				,tipmov
				,swvisible
				,codidocu
				,cdtiptra
				,cdorddoc
				,documento
				,cdusuari
				,cdsisrol, sustituir
				);
	}
	
	@Override
	public void generarDocumentosParametrizadosAsync(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,int proceso
			,String ntramite
			,String nmsolici
			)throws Exception
	{
		long stamp = System.currentTimeMillis();
		logger.debug(Utils.log(stamp
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ generarDocumentosParametrizadosAsync @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ proceso="  , proceso
				,"\n@@@@@@ ntramite=" , ntramite
				,"\n@@@@@@ nmsolici=" , nmsolici
				));
		
		new GenerarDocumentosParametrizadosAsyncThread(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsituac
				,nmsuplem
				,proceso
				,ntramite
				,nmsolici
				,stamp
				).start();
		
		logger.debug(Utils.log(stamp
				,"\n@@@@@@ generarDocumentosParametrizadosAsync @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	private class GenerarDocumentosParametrizadosAsyncThread extends Thread
	{
		private String cdunieco
		               ,cdramo
		               ,estado
		               ,nmpoliza
		               ,nmsituac
		               ,nmsuplem
		               ,ntramite
		               ,nmsolici;
		
		private long stamp;
		
		private int proceso;
		
		public GenerarDocumentosParametrizadosAsyncThread(
				String cdunieco
				,String cdramo
				,String estado
				,String nmpoliza
				,String nmsituac
				,String nmsuplem
				,int proceso
				,String ntramite
				,String nmsolici
				,long stamp
				)
		{
			this.cdunieco = cdunieco;
			this.cdramo   = cdramo;
			this.estado   = estado;
			this.nmpoliza = nmpoliza;
			this.nmsituac = nmsituac;
			this.nmsuplem = nmsuplem;
			this.proceso  = proceso;
			this.ntramite = ntramite;
			this.nmsolici = nmsolici;
			this.stamp    = stamp;
		}
		
		@Override
		public void run()
		{
			try
			{
				logger.debug(Utils.log(stamp
						,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
						,"\n@@@@@@ Se ejecutan documentos asincronos @@@@@@"
						,"\n@@@@@@ cdunieco=" , cdunieco
						,"\n@@@@@@ cdramo="   , cdramo
						,"\n@@@@@@ estado="   , estado
						,"\n@@@@@@ nmpoliza=" , nmpoliza
						,"\n@@@@@@ nmsituac=" , nmsituac
						,"\n@@@@@@ nmsuplem=" , nmsuplem
						,"\n@@@@@@ proceso="  , proceso
						,"\n@@@@@@ ntramite=" , ntramite
						,"\n@@@@@@ nmsolici=" , nmsolici
						));
				
				//---------------------------------------------------
				String paso = "Marcando tramite con status temporal";
				logger.debug(Utils.log(stamp,"paso=",paso));
				
				String statusOriginal = mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite, EstatusTramite.EN_DOCUMENTACION.getCodigo());
				
				//----------------------------
				paso = "Generando documentos";
				logger.debug(Utils.log(stamp,"paso=",paso));
				
				generarDocumentosParametrizados(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplem
						,proceso
						,ntramite
						,nmsolici
						,stamp
						);

				//-----------------------------------------------
				paso = "Regresando tramite a su status original";
				logger.debug(Utils.log(stamp,"paso=",paso));
				
				mesaControlDAO.marcarTramiteComoStatusTemporal(ntramite, statusOriginal);
				
				logger.debug(Utils.log(stamp
						,"\n@@@@@@ Se ejecutan documentos asincronos @@@@@@"
						,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
						));
			}
			catch(Exception ex)
			{
				
			}
		}
	}

	@Override
	public List<Map<String, String>> obtenerValorDefectoParametrizados(String cdsisrol, String campo, String renovacionGral)
			throws Exception {
		Map<String,String> result = new HashMap<String,String>();
		
		List<Map<String, String>> datosPoliza = consultasDAO.recuperarDatosValorDefectoLayout(cdsisrol,campo,renovacionGral);
		//datosPoliza.get("ntramite");
		//datosPoliza.get("nmsolici");
		return datosPoliza;
	}
}