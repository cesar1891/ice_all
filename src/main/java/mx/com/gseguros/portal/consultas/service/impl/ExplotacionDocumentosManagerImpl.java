package mx.com.gseguros.portal.consultas.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal2.web.GenericVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.model.DescargaLotePdfVO;
import mx.com.gseguros.portal.consultas.model.DocumentoReciboParaMostrarDTO;
import mx.com.gseguros.portal.consultas.model.ImpresionLayoutVO;
import mx.com.gseguros.portal.consultas.service.ExplotacionDocumentosManager;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.documentos.model.Documento;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.general.dao.CatalogosDAO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.model.Reporte;
import mx.com.gseguros.portal.general.service.ImpresionService;
import mx.com.gseguros.portal.general.service.ReportesManager;
import mx.com.gseguros.portal.general.util.Catalogos;
import mx.com.gseguros.portal.general.util.EstatusTramite;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.portal.general.util.TipoArchivo;
import mx.com.gseguros.portal.general.util.TipoTramite;
import mx.com.gseguros.portal.mesacontrol.dao.MesaControlDAO;
import mx.com.gseguros.utils.DocumentosUtils;
import mx.com.gseguros.utils.FTPSUtils;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

@Service
public class ExplotacionDocumentosManagerImpl implements ExplotacionDocumentosManager
{
	private final static Logger logger = LoggerFactory.getLogger(ExplotacionDocumentosManagerImpl.class);
	
	@Autowired
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private MesaControlDAO mesaControlDAO;
	
	@Autowired
	private EmisionDAO emisionDAO;
	
	@Autowired
	private CatalogosDAO catalogosDAO;
	
	@Autowired
	private ReportesManager reportesManager;
	
	@Autowired
	private ImpresionService impresionService;
	
	@Value("${ruta.documentos.poliza}")
	private String rutaDocumentosPoliza;
	
	@Value("${ruta.documentos.temporal}")
	private String rutaDocumentosTemporal;
	
	@Value("${ruta.servidor.reports}")
	private String rutaServidorReports;
	
	@Value("${pass.servidor.reports}")
	private String passServidorReports;
	
	@Value("${rdf.impresion.remesa}")
	private String nombreReporteRemesa;
	
	@Value("${pdf.impresion.remesa.nombre}")
	private String nombreRemesaPdf;
	
	@Autowired
	@Qualifier("consultasDAOICEImpl")
	private ConsultasPolizaDAO consultasPolizaDAO;
	
	@Autowired
	private EndososDAO endososDAO;
	
	@Override
	public Map<String,Item> pantallaExplotacionDocumentos(String cdusuari, String cdsisrol) throws Exception
	{
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaExplotacionDocumentos @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				));
		
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		
		try
		{
			paso = "Recuperando componentes";
			logger.debug(paso);
			
			List<ComponenteVO> itemsFormBusq = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_DOCUMENTOS"
					,"FORM_BUSQUEDA"
					,null //orden
					);
			
			List<ComponenteVO> compsGridPolizas = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_DOCUMENTOS"
					,"GRID_POLIZAS"
					,null //orden
					);
			
			List<ComponenteVO> comboOrden = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_DOCUMENTOS"
					,"COMBO_ORDEN"
					,null //orden
					);
			
			paso = "Construyendo componentes";
			logger.debug(paso);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsFormBusq, true, false, true, false, false, false);
			items.put("itemsFormBusq" , gc.getItems());
			
			gc.generaComponentes(compsGridPolizas, true, true, false, true, false, false);
			items.put("gridPolizasFields" , gc.getFields());
			
			gc.generaComponentes(comboOrden, true, false, false, true, true, false); 
			items.put("comboOrden" , gc.getColumns());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.join(
				 "\n@@@@@@ pantallaExplotacionDocumentos @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return items;
	}
	
	
	
	@Override
	public String generarLote(
			String cdusuari
			,String cdsisrol
			,String cdtipram
			,String cdtipimp
			,String tipolote
			,List<Map<String, String>> movs
			)throws Exception
	{
		StringBuilder sb = new StringBuilder(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ generarLote @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdtipram=" , cdtipram
				,"\n@@@@@@ cdtipimp=" , cdtipimp
				,"\n@@@@@@ tipolote=" , tipolote
				,"\n@@@@@@ movs="     , movs
				));
		
		String lote  = null
			   ,paso = null;
		
		try
		{
			paso = "Generando secuencia";
			sb.append("\n").append(paso);
			
			lote = consultasDAO.recuperarSecuenciaLote();
			
			paso = "Agrupando movimientos por agente";
			sb.append("\n").append(paso);
			
			Map<String,Object> agentes = new HashMap<String,Object>();
			
			for(Map<String,String>mov:movs)
			{
				String cdagente = mov.get("cdagente");
				if(!agentes.containsKey(cdagente))
				{
					agentes.put(cdagente,new ArrayList<Map<String,String>>());
					sb.append(Utils.log("\n@@@@@@ se crea el agrupador para el agente {}",cdagente));
				}
				sb.append(Utils.log("\n@@@@@@ se agrega mov. al agente {}",cdagente));
				((List<Map<String,String>>)agentes.get(cdagente)).add(mov);
			}
			
			paso = "Recuperando impresiones requeridas";
			sb.append("\n").append(paso);
			
			String impReq = consultasDAO.recuperarImpresionesDisponiblesPorTipoRamo(cdtipram,tipolote);
			
			paso = "Generando tr\u00E1mites de agentes";
			sb.append("\n").append(paso);
			
			Map<String,String> valores = new HashMap<String,String>();
			valores.put("otvalor01" , lote);
			valores.put("otvalor02" , cdtipimp);
			valores.put("otvalor03" , cdtipram);
			valores.put("otvalor04" , impReq);   //impresiones requeridas
			valores.put("otvalor05" , "0");      //impresiones ejecutadas
			valores.put("otvalor06" , tipolote); //POLIZA - RECIBO
			
			for(Entry<String,Object>agente:agentes.entrySet())
			{
				String                   cdagente   = agente.getKey();
				List<Map<String,String>> movsAgente = (List<Map<String,String>>)agente.getValue();
				
				paso = "Generando tr\u00E1mite iterado";
				sb.append("\n").append(paso);
				
				String ntramite = mesaControlDAO.movimientoMesaControl (
						null  //cdunieco
						,null //cdramo
						,null //estado
						,null //nmpoliza
						,null //nmsuplem
						,null //cdsucadm
						,null //cdsucdoc
						,TipoTramite.IMPRESION.getCdtiptra()
						,new Date() //ferecepc
						,cdagente
						,null //referencia
						,null //nombre
						,new Date() //festatus
						,EstatusTramite.IMPRESION_PENDIENTE.getCodigo()
						,null //comments
						,null //nmsolici
						,null //cdtipsit
						,cdusuari
						,cdsisrol
						,null //swimpres
						,null //cdtipflu
						,null //cdflujomc
						,valores, null, null, null, null, false, null
						);
				
				mesaControlDAO.movimientoDetalleTramite(
						ntramite
						,new Date()                //feinicio
						,null                      //cdclausu
						,"Nuevo registro de lote"  //comments
						,cdusuari
						,null                      //cdmotivo
						,cdsisrol
						,"S"
						,null
						,null
						,EstatusTramite.IMPRESION_PENDIENTE.getCodigo()
						,false
						);
				
				for(Map<String,String>movAgente:movsAgente)
				{
					paso = "Registrando relaci\u00F3n de movimiento iterado";
					sb.append("\n").append(paso);
					
					emisionDAO.insertarMpoliimp(
							ntramite
							,movAgente.get("cdunieco")
							,movAgente.get("cdramo")
							,movAgente.get("estado")
							,movAgente.get("nmpoliza")
							,movAgente.get("nmsuplem")
							,tipolote
							//,"P".equals(tipolote) ? movAgente.get("ntramite") : null
							,"P".equals(tipolote) ? endososDAO.obtenerNtramiteEmision(
									movAgente.get("cdunieco")
									,movAgente.get("cdramo")
									,movAgente.get("estado")
									,movAgente.get("nmpoliza")
									) : null
							,"R".equals(tipolote) ? movAgente.get("nmrecibo") : null
							,StringUtils.isBlank(movAgente.get("orden")) ? "1" : movAgente.get("orden")
							);
					
					if("P".equals(tipolote))
					{
						paso = "Marcando tr\u00E1mite original iterado";
						sb.append("\n").append(paso);
						
						emisionDAO.marcarTramiteImpreso(
								movAgente.get("ntramite")
								,"S"
								);
					}
				}
				
				paso = "Generando carpeta de tr\u00E1mite de agentes";
				sb.append("\n").append(paso);
				
				String rutaCarpeta = Utils.join(rutaDocumentosPoliza,"/",ntramite);
				File   carpeta     = new File(rutaCarpeta);
				if(!carpeta.exists())
				{
					sb.append(Utils.log("\n@@@@@@ Se va a crear la carpeta ",carpeta));
					if(!carpeta.mkdir())
					{
						throw new ApplicationException("No se pudo crear la carpeta para los documentos");
					}
				}
				
				paso = "Generando remesa pdf";
				sb.append("\n").append(paso);
				
				String urlReporteCotizacion = Utils.join(
						  rutaServidorReports
						, "?p_lote="     , lote
						, "&p_usr_imp="  , cdusuari
						, "&p_ntramite=" , ntramite
	                    , "&destype=cache"
	                    , "&desformat=PDF"
	                    , "&userid="        , passServidorReports
	                    , "&ACCESSIBLE=YES"
	                    , "&report="        , nombreReporteRemesa
	                    , "&paramform=no"
	                    );
				
				String pathRemesa=Utils.join(
						rutaDocumentosPoliza
						,"/",ntramite
						,"/remesa.pdf"
						);
				
				HttpUtil.generaArchivo(urlReporteCotizacion, pathRemesa);
				
				mesaControlDAO.guardarDocumento(
						null          //cdunieco
						,null         //cdramo
						,null         //estado
						,null         //nmpoliza
						,null         //nmsuplem
						,new Date()   //feinici
						,"remesa.pdf" //cddocume
						,"REMESA PDF" //dsdocume
						,null         //nmsolici
						,ntramite
						,"1"          //tipmov
						,null         //swvisible
						,null         //codidocu
						,TipoTramite.IMPRESION.getCdtiptra()
						,"0"
						,Documento.REMESA_IMPRESION_LOTE, null, null, false
						);
				
				paso = "Generando remesa excel";
				sb.append("\n").append(paso);
				
				Map<String,String> paramsExcel = new LinkedHashMap<String,String>();
				paramsExcel.put("pv_lote_i"     , lote);
				paramsExcel.put("pv_usr_imp_i"  , cdusuari);
				paramsExcel.put("pv_ntramite_i" , ntramite);
				
				InputStream excel = reportesManager.obtenerDatosReporte(Reporte.REMESA.getCdreporte()
						,cdusuari
						,paramsExcel
						);
				
				String nombreExcel = Utils.join("remesa",TipoArchivo.XLS.getExtension());
				
				FileUtils.copyInputStreamToFile(excel, new File(Utils.join(
								rutaDocumentosPoliza,"/",ntramite,"/",nombreExcel
				)));
				
				mesaControlDAO.guardarDocumento(
						null            //cdunieco
						,null           //cdramo
						,null           //estado
						,null           //nmpoliza
						,null           //nmsuplem
						,new Date()     //feinici
						,nombreExcel    //cddocume
						,"REMESA EXCEL" //dsdocume
						,null           //nmsolici
						,ntramite
						,"1"            //tipmov
						,null           //swvisible
						,null           //codidocu
						,TipoTramite.IMPRESION.getCdtiptra()
						,null           //cdorddoc
						,null           //cdmoddoc
, null, null, false
						);
				
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		
		sb.append(Utils.log(
				 "\n@@@@@@ lote=",lote
			    ,"\n@@@@@@ generarLote @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		logger.debug(sb.toString());
		
		return lote;
	}
	
	
	@Override
    public File imprimirLote(
            String lote, 
            String hoja, 
            String peso, 
            String cdtipram, 
            String cdtipimp, 
            String tipolote,
            String dsimpres, 
            String charola1, 
            String charola2, 
            String cdusuari, 
            String cdsisrol, 
            boolean test,
            boolean esDuplex
            ) throws Exception 
	{
        StringBuilder sb = new StringBuilder(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@", 
                "\n@@@@@@ imprimirLote @@@@@@",
                "\n@@@@@@ lote=", lote, 
                "\n@@@@@@ hoja=", hoja, 
                "\n@@@@@@ peso=", peso, 
                "\n@@@@@@ cdtipram=", cdtipram,
                "\n@@@@@@ cdtipimp=", cdtipimp, 
                "\n@@@@@@ tipolote=", tipolote, 
                "\n@@@@@@ dsimpres=", dsimpres,
                "\n@@@@@@ charola1=", charola1, 
                "\n@@@@@@ charola2=", charola2, 
                "\n@@@@@@ cdusuari=", cdusuari,
                "\n@@@@@@ cdsisrol=", cdsisrol, 
                "\n@@@@@@ test=", test, 
                "\n@@@@@@ swimpdpx=", esDuplex
                ));

        String paso = "Iniciando impresi\u00F3n";
        sb.append("\n").append(paso);
        AtomicBoolean hayErrores = new AtomicBoolean(false);
        File noExiste = null;

        try {
            paso = "Recuperando archivos";
            sb.append("\n").append(paso);

            List<Map<String, String>> listaArchivos = consultasDAO.recuperarArchivosParaImprimirLote(lote, hoja,
                    tipolote);

            sb.append(Utils.log("\nlista=", listaArchivos));
            
            paso = "Descargando archivos";
            sb.append("\n").append(paso);

            descargaUrl(listaArchivos);
            sb.append(Utils.join("Salida de descarga Url", listaArchivos));
            paso = "Armando juegos de impresiones";
            sb.append("\n").append(paso);

            listaArchivos = this.armaJuegosDeImpresiones(listaArchivos);
            List<Map<String, String>> listaArchivosOriginales = listaArchivos;
            paso = "Regenerando documentos";
            sb.append("\n").append(paso);
            long timestamp = System.currentTimeMillis();
            long rand = new Double(1000d * Math.random()).longValue();
            noExiste = new File(Utils.join(rutaDocumentosTemporal, "/archivos_no_encontrados_lote_", lote, "_",
                    timestamp, rand, ".txt"));

            listaArchivos = regeneraDocs(listaArchivos, noExiste, cdtipram, hayErrores,lote,cdusuari);

            paso = "Imprimiendo archivos";
            sb.append("\n").append(paso);

            boolean apagado = false;

            paso = "generando Archivo";
            for (Map<String, String> archivo : listaArchivos) {

                try {
                    logger.debug("archivo={} rutaDocumentosPoliza={} nmcopias={} ntramite={} cddocume={} swimpdpx{}",
                            archivo, rutaDocumentosPoliza, archivo.get("nmcopias"), archivo.get("ntramite"),
                            archivo.get("cddocume"), archivo.get("swimpdpx"));
                    if (!apagado) {
                        String ntramite = archivo.get("ntramite");
                        String cddocume = archivo.get("cddocume");
                        String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
                        String papelDoc = archivo.get("tipodoc");
                        String nmpoliza = archivo.get("nmpoliza");

                        sb.append(Utils.log("\ntramite,archivo=", ntramite, ",", cddocume));

                        if (cddocume.toLowerCase().indexOf("://") != -1) {
                            paso = "Usando archivo local descargado";

                            sb.append("\n").append(paso);
                            sb.append(Utils.join("\nEl archivo: ",cddocume," ya existe, se usar\u00E1 :",archivo.get("descargadoEn")));
                            filePath = archivo.get("descargadoEn");

                            if (StringUtils.isBlank(filePath)) {

                                logger.error(Utils.join("Error en descarga-: ", cddocume));
                                continue;
                            }

                        }

                        paso = "Imprimiendo archivo";
                        sb.append("\n").append(paso);

                        boolean swImpDpxArchivo = "S".equalsIgnoreCase(archivo.get("swimpdpx")) ? true : false;

                        logger.debug("swImpDpxArchivo={}", swImpDpxArchivo);

                        logger.debug("Se imprimira duplex? {}", (esDuplex && swImpDpxArchivo));

                        try {
                            impresionService.imprimeDocumento(filePath, dsimpres,
                                    Integer.parseInt(archivo.get("nmcopias")) // numCopias
                                    , hoja.length() > 1 ? ("M".equals(papelDoc) ? charola2 : charola1) : charola1,
                                    (esDuplex && swImpDpxArchivo));
                        } catch (Exception e) {
                        	logger.error("Error al ejecutar el comando de impresion: ",e);
                            String dsdocume = archivo.get("dsdocume");
                            hayErrores.set(true);
                            BufferedWriter bw = new BufferedWriter(new FileWriter(noExiste, true));

                            bw.append(Utils.join(nmpoliza == null ? "" : nmpoliza, ",\"", cddocume, "\"", ",\"",
                                    dsdocume, "\"", ",\"ERROR EN LA IMPRESORA\"", "\r\n"));
                            bw.close();

                        }
                        if (test) {
                            apagado = true;
                        }
                        
                    }
                } catch (Exception e) {
                    logger.error("Error Generando archivo", e);
                    // hayErrores.set(true);
                }
            }

            listaArchivos = listaArchivosOriginales;

            paso = "Actualizando remesas, emisiones y endosos";
            sb.append("\n").append(paso);

            /*
             * en este procedimiento se actualizan las sumas, y si estan
             * completas: se marcan las remesas como impresas, y tambien los
             * hijos cuando son emisiones/endosos
             */
            boolean impresos = emisionDAO.sumarImpresiones(lote, tipolote, peso);

            if ("R".equals(tipolote)) {
                paso = "Actualizando recibos";
                sb.append("\n").append(paso);

                List<DocumentoReciboParaMostrarDTO> listaRecibos = new ArrayList<DocumentoReciboParaMostrarDTO>();
                for (Map<String, String> archivo : listaArchivos) {
                    listaRecibos
                            .add(new DocumentoReciboParaMostrarDTO(archivo.get("ntramite"), archivo.get("cddocume")));
                }

                emisionDAO.mostrarRecibosImpresosListaDeListas(listaRecibos);
            }

            paso = "Recuperando remesas del lote";
            sb.append("\n").append(paso);

            List<Map<String, String>> remesas = mesaControlDAO.recuperarTramites(null // cdunieco
                    , null // ntramite
                    , null // cdramo
                    , null // nmpoliza
                    , null // estado
                    , null // cdagente
                    , "0" // status
                    , null // cdtipsit
                    , null // fedesde
                    , null // fehasta
                    , cdsisrol, TipoTramite.IMPRESION.getCdtiptra(), null // contrarecibo
                    , null // tipoPago
                    , null // nfactura
                    , null // cdpresta
                    , null // cdusuari
                    , null // cdtipram
                    , lote, null // tipolote
                    , null // tipoimpr
                    , null // cdusuari_busq
            );

            for (Map<String, String> remesa : remesas) {
                paso = "Guardando detalle de remesa";
                sb.append("\n").append(paso);

                mesaControlDAO
                        .movimientoDetalleTramite(remesa.get("ntramite"), new Date() // feinicio
                                , null // cdclausu
                                ,
                                Utils.join(
                                        impresos ? "Se realiz\u00F3 la impresi\u00F3n final de la remesa ("
                                                : "Se realiz\u00F3 una impresi\u00F3n de la remesa (",
                                        "B".equals(hoja) ? ("papeler\u00EDa")
                                                : ("M".equals(hoja) ? ("recibos")
                                                        : ("C".equals(hoja) ? ("credenciales")
                                                                : ("papeler\u00EDa y recibos"))),
                                        ")"),
                                cdusuari, null // cdmotivo
                                , cdsisrol, "S", null, null, remesa.get("status"), impresos);
            }

        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso, sb.toString());
        }

        sb.append(Utils.log("\n@@@@@@ imprimirLote @@@@@@", "\n@@@@@@@@@@@@@@@@@@@@@@@@@@"));

        logger.debug(sb.toString());

        if (hayErrores.get()) {
            return noExiste;
        }

        return null;
    }
	
	@Override
	public Map<String,Item> pantallaExplotacionRecibos(String cdusuari, String cdsisrol) throws Exception
	{
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaExplotacionRecibos @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				));
		
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		
		try
		{
			paso = "Recuperando componentes";
			logger.debug(paso);
			
			List<ComponenteVO> itemsFormBusq = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_RECIBOS"
					,"FORM_BUSQUEDA"
					,null //orden
					);
			
			List<ComponenteVO> compsGridPolizas = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_RECIBOS"
					,"GRID_RECIBOS"
					,null //orden
					);
			
			paso = "Construyendo componentes";
			logger.debug(paso);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsFormBusq, true, false, true, false, false, false);
			items.put("itemsFormBusq" , gc.getItems());
			
			gc.generaComponentes(compsGridPolizas, true, true, false, true, false, false);
			items.put("gridRecibosFields" , gc.getFields());
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.join(
				 "\n@@@@@@ pantallaExplotacionRecibos @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return items;
	}
	
	@Override
	public Map<String,Item> pantallaPermisosImpresion(String cdusuari, String cdsisrol) throws Exception
	{
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaPermisosImpresion @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				));
		
		Map<String,Item> items = new HashMap<String,Item>();
		String           paso  = null;
		
		try
		{
			paso = "Recuperando componentes";
			logger.debug(paso);
			
			List<ComponenteVO> itemsFormBusq = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_PERMISOS"
					,"FORM_BUSQUEDA"
					,null //orden
					);
			
			List<ComponenteVO> itemsAgregarPerm = pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,"EXPLOTACION_PERMISOS"
					,"FORM_PERMISO"
					,null //orden
					);
			
			paso = "Construyendo componentes";
			logger.debug(paso);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(itemsFormBusq, true, false, true, false, false, false);
			items.put("itemsFormBusq" , gc.getItems());
			
			gc.generaComponentes(itemsAgregarPerm, true, false, true, false, false, false);
			items.put("itemsAgregarPerm" , gc.getItems());
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.join(
				 "\n@@@@@@ pantallaPermisosImpresion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return items;
	}
	
	@Override
	public void movPermisoImpresion(
			String tipo
			,String cdusuari
			,String cdunieco
			,String cdtipram
			,String clave
			,String funcion
			,String accion
			)throws Exception
	{
		logger.debug(Utils.join(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ movPermisoImpresion @@@@@@"
				,"\n@@@@@@ tipo="     , tipo
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdtipram=" , cdtipram
				,"\n@@@@@@ clave="    , clave
				,"\n@@@@@@ funcion="  , funcion
				,"\n@@@@@@ accion="   , accion
				));
		String paso = null;
		try
		{
			paso = "Borrando permiso";
			logger.debug("@@@@@@ paso: {}",paso);
			
			if("S".equals(tipo))
			{
				consultasDAO.movPermisoImpresionSucursal(
						cdusuari
						,cdunieco
						,cdtipram
						,clave
						,funcion
						,accion
						);
			}
			else if("A".equals(tipo))
			{
				consultasDAO.movPermisoImpresionAgente(
						cdusuari
						,cdunieco
						,cdtipram
						,clave
						,funcion
						,accion
						);
			}
			else if("U".equals(tipo))
			{
				consultasDAO.movPermisoImpresionUsuario(
						cdusuari
						,cdunieco
						,cdtipram
						,clave
						,funcion
						,accion
						);
			}
			else
			{
				throw new ApplicationException("Tipo de permiso no soportado");
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
	}
	
	@Override
	public void actualizarStatusRemesa(
			 List<String> tramites
			,String status
			,String cdusuari
			,String cdsisrol
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ actualizarStatusRemesa @@@@@@"
				,"\n@@@@@@ tramites=" , tramites
				,"\n@@@@@@ status="   , status
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				));
		
		String paso = null;
		String mensaje="";
		
		for(String ntramite: tramites){
			try
			{
				paso = "Actualizando status de remesa";
				logger.debug("@@@@@@ paso: {}",paso);
				
				mesaControlDAO.actualizarStatusRemesa(ntramite,status);
				
				paso = "Actualizando status de hijos de remesa";
				logger.debug("@@@@@@ paso: {}",paso);
				
				mesaControlDAO.actualizarHijosRemesa(null, ntramite, status);
				
				paso = "Recuperando status";
				logger.debug("@@@@@@ paso: {}",paso);
				
				List<GenericVO> statuses = catalogosDAO.obtieneTmanteni(Catalogos.MC_ESTATUS_TRAMITE.getCdTabla());
				String          dsStatus = null;
				for(GenericVO statusIte : statuses)
				{
					if(statusIte.getKey().equals(status))
					{
						dsStatus = statusIte.getValue();
						break;
					}
				}
				if(StringUtils.isBlank(dsStatus))
				{
					throw new ApplicationException("No se encuentra el status");
				}
				
				paso = "Guardando detalle";
				logger.debug("@@@@@@ paso: {}",paso);
				
				mesaControlDAO.movimientoDetalleTramite(
						ntramite
						,new Date() //feinicio
						,null       //cdclausu
						,Utils.join("Remesa actualizada a status '",dsStatus,"'")
						,cdusuari
						,null       //cdmotivo
						,cdsisrol
						,"S"
						,null
						,null
						,status
						,true
						);
			}
			catch(Exception ex)
			{
				if(ex instanceof ApplicationException){
					
					mensaje=Utils.join(mensaje,"Remesa <b>",ntramite,"</b>: ",ex.getMessage(),"<br>");
					
				}else{
					Utils.generaExcepcion(ex, paso);
				}
			}
		}
		
		if(!"".equals(mensaje)){
			throw new ApplicationException(mensaje);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ actualizarStatusRemesa @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	
	/**
	 * Se comenta porque ya no se usa, aunque está funcional
	 */
	/*
	@Override
	public Map<String,String> generarRemesaEmisionEndoso(
			String cdusuari
			,String cdsisrol
			,String cdtipimp
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			)throws Exception
	{
		StringBuilder sb = new StringBuilder(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ generarRemesaEmisionEndoso @@@@@@"
				,"\n@@@@@@ cdusuari=" , cdusuari
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdtipimp=" , cdtipimp
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				));
		
		String             paso     = null;
		Map<String,String> result   = new HashMap<String,String>();
		String             lote     = null;
		String             cdtipram = null;
		String             remesa   = null;
		
		try
		{
			paso = "Recuperando suplemento";
			sb.append("\n").append(paso);
			
			String nmsuplem = consultasDAO.recuperarUltimoNmsuplem(cdunieco,cdramo,estado,nmpoliza);
			
			paso = "Recuperando tr\u00e1mite";
			sb.append("\n").append(paso);
			
			String ntramiteOpe  = consultasDAO.recuperarTramitePorNmsuplem(cdunieco,cdramo,estado,nmpoliza,nmsuplem)
			       ,ntramiteEmi = endososDAO.obtenerNtramiteEmision(cdunieco, cdramo, estado, nmpoliza);
			
			paso = "Recuperando tipo de ramo";
			sb.append("\n").append(paso);
			
			cdtipram = consultasDAO.recuperarTipoRamoPorCdramo(cdramo);
			
			result.put("cdtipram" , cdtipram);
			
			paso = "Verificando remesa existente";
			sb.append("\n").append(paso);
			
			Map<String,String> datosRemesa = consultasDAO.recuperarRemesaEmisionEndoso(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsuplem
					,ntramiteOpe
					);
			
			if(datosRemesa!=null)
			{
				lote     = datosRemesa.get("lote");
				remesa   = datosRemesa.get("remesa");
				cdtipimp = datosRemesa.get("cdtipimp");
				
				result.put("lote"     , lote);
				result.put("remesa"   , remesa);
				result.put("cdtipimp" , cdtipimp);
				result.put("nueva"    , "N");
			}
			else
			{
				paso = "Recuperando agente";
				sb.append("\n").append(paso);
				
				String cdagente = null;
				
				List<AgentePolizaVO> agentes = consultasPolizaDAO.obtieneAgentesPoliza(new PolizaVO(cdunieco,cdramo,estado,nmpoliza));
				for(AgentePolizaVO agente : agentes)
				{
					if("1".equals(agente.getCdtipoAg()))
					{
						cdagente = agente.getCdagente();
					}
				}
				if(StringUtils.isBlank(cdagente))
				{
					throw new ApplicationException("No se encuentra el agente principal");
				}
				
				paso = "Generando secuencia";
				sb.append("\n").append(paso);
				
				lote = consultasDAO.recuperarSecuenciaLote();
				
				result.put("lote"  , lote);
				result.put("nueva" , "S");
				
				paso = "Recuperando impresiones requeridas";
				sb.append("\n").append(paso);
				
				String impReq = consultasDAO.recuperarImpresionesDisponiblesPorTipoRamo(cdtipram, "P");
				
				paso = "Generando tr\u00E1mite de agente";
				sb.append("\n").append(paso);
				
				Map<String,String> valores = new HashMap<String,String>();
				valores.put("otvalor01" , lote);
				valores.put("otvalor02" , cdtipimp);
				valores.put("otvalor03" , cdtipram);
				valores.put("otvalor04" , impReq); //impresiones requeridas
				valores.put("otvalor05" , "0");    //impresiones ejecutadas
				valores.put("otvalor06" , "P");    //POLIZA - RECIBO
				
				remesa = mesaControlDAO.movimientoMesaControl(
						null  //cdunieco
						,null //cdramo
						,null //estado
						,null //nmpoliza
						,null //nmsuplem
						,null //cdsucadm
						,null //cdsucdoc
						,TipoTramite.IMPRESION.getCdtiptra()
						,new Date() //ferecepc
						,cdagente
						,null //referencia
						,null //nombre
						,new Date() //festatus
						,EstatusTramite.IMPRESION_PENDIENTE.getCodigo()
						,null //comments
						,null //nmsolici
						,null //cdtipsit
						,cdusuari
						,cdsisrol
						,null //swimpres
						,null //cdtipflu
						,null //cdflujomc
						,valores, null
						);
				
				result.put("remesa" , remesa);
				
				mesaControlDAO.movimientoDetalleTramite(
						remesa
						,new Date()                //feinicio
						,null                      //cdclausu
						,"Nuevo registro de lote desde emisi\u00F3n/endoso"  //comments
						,cdusuari
						,null                      //cdmotivo
						,cdsisrol
						,"S"
						,null
						,null
						,EstatusTramite.IMPRESION_PENDIENTE.getCodigo()
						,true
						);
				
				paso = "Registrando relaci\u00F3n de movimiento";
				sb.append("\n").append(paso);
				
				emisionDAO.insertarMpoliimp(
						remesa
						,cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsuplem
						,"P"
						,ntramiteEmi
						,null
						,"1"
						);
				
				paso = "Marcando tr\u00E1mite original";
				sb.append("\n").append(paso);
				
				emisionDAO.marcarTramiteImpreso(
						ntramiteOpe
						,"S"
						);
				
				paso = "Generando carpeta de tr\u00E1mite de agente";
				sb.append("\n").append(paso);
				
				String rutaCarpeta = Utils.join(rutaDocumentosPoliza,"/",remesa);
				File   carpeta     = new File(rutaCarpeta);
				if(!carpeta.exists())
				{
					sb.append(Utils.log("\n@@@@@@ Se va a crear la carpeta ",carpeta));
					if(!carpeta.mkdir())
					{
						throw new ApplicationException("No se pudo crear la carpeta para los documentos");
					}
				}
				
				paso = "Generando remesa pdf";
				sb.append("\n").append(paso);
				
				String urlReporteCotizacion = Utils.join(
						  rutaServidorReports
						, "?p_lote="     , lote
						, "&p_usr_imp="  , cdusuari
						, "&p_ntramite=" , remesa
	                    , "&destype=cache"
	                    , "&desformat=PDF"
	                    , "&userid="        , passServidorReports
	                    , "&ACCESSIBLE=YES"
	                    , "&report="        , nombreReporteRemesa
	                    , "&paramform=no"
	                    );
				
				String pathRemesa=Utils.join(
						rutaDocumentosPoliza
						,"/",remesa
						,"/remesa.pdf"
						);
				
				HttpUtil.generaArchivo(urlReporteCotizacion, pathRemesa);
				
				mesaControlDAO.guardarDocumento(
						null          //cdunieco
						,null         //cdramo
						,null         //estado
						,null         //nmpoliza
						,null         //nmsuplem
						,new Date()   //feinici
						,"remesa.pdf" //cddocume
						,"REMESA PDF" //dsdocume
						,null         //nmsolici
						,remesa
						,"1"          //tipmov
						,null         //swvisible
						,null         //codidocu
						,TipoTramite.IMPRESION.getCdtiptra()
						,"0"
						,Documento.REMESA_IMPRESION_LOTE, null, null
						);
				
				paso = "Generando remesa excel";
				sb.append("\n").append(paso);
				
				Map<String,String> paramsExcel = new LinkedHashMap<String,String>();
				paramsExcel.put("pv_lote_i"     , lote);
				paramsExcel.put("pv_usr_imp_i"  , cdusuari);
				paramsExcel.put("pv_ntramite_i" , remesa);
				
				InputStream excel = reportesManager.obtenerDatosReporte(Reporte.REMESA.getCdreporte()
						,cdusuari
						,paramsExcel
						);
				
				String nombreExcel = Utils.join("remesa",TipoArchivo.XLS.getExtension());
				
				FileUtils.copyInputStreamToFile(excel, new File(Utils.join(
								rutaDocumentosPoliza,"/",remesa,"/",nombreExcel
				)));
				
				mesaControlDAO.guardarDocumento(
						null            //cdunieco
						,null           //cdramo
						,null           //estado
						,null           //nmpoliza
						,null           //nmsuplem
						,new Date()     //feinici
						,nombreExcel    //cddocume
						,"REMESA EXCEL" //dsdocume
						,null           //nmsolici
						,remesa
						,"1"            //tipmov
						,null           //swvisible
						,null           //codidocu
						,TipoTramite.IMPRESION.getCdtiptra()
						,null           //cdorddoc
						,null           //cdmoddoc
						,null
						,null
						);
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		
		sb.append(Utils.log(
				 "\n@@@@@@ result=",result
			    ,"\n@@@@@@ generarRemesaEmisionEndoso @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		logger.debug(sb.toString());
		
		return result;
	}
	*/
	
	@Override
	public Map<String,String> marcarImpresionOperacion(
			String cdusuari
			,String cdsisrol
			,String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String marcar
			,String ntramiteIn
			)throws Exception
	{
		StringBuilder sb = new StringBuilder(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ marcarImpresionOperacion @@@@@@"
				,"\n@@@@@@ cdusuari="   , cdusuari
				,"\n@@@@@@ cdsisrol="   , cdsisrol
				,"\n@@@@@@ cdunieco="   , cdunieco
				,"\n@@@@@@ cdramo="     , cdramo
				,"\n@@@@@@ estado="     , estado
				,"\n@@@@@@ nmpoliza="   , nmpoliza
				,"\n@@@@@@ marcar="     , marcar
				,"\n@@@@@@ ntramiteIn=" , ntramiteIn
				));
		
		Map<String,String> result = new HashMap<String,String>();
		String             paso   = null;
		
		try
		{
			paso = "Recuperando suplemento";
			sb.append("\n").append(paso);
			
			String nmsuplem = consultasDAO.recuperarUltimoNmsuplem(cdunieco,cdramo,estado,nmpoliza);
			
			
			String ntramite = ntramiteIn;
			
			if (StringUtils.isBlank(ntramite)) {
				paso = "Recuperando tr\u00e1mite";
				sb.append("\n").append(paso);
				
				ntramite = consultasDAO.recuperarTramitePorNmsuplem(cdunieco,cdramo,estado,nmpoliza,nmsuplem);
			}
			
			paso = "Marcando tr\u00e1mite";
			sb.append("\n").append(paso);
			
			Map<String,Boolean> preguntarMarcado = mesaControlDAO.marcarImpresionOperacion(
					cdsisrol
					,ntramite
					,marcar
					);
			
			Boolean preguntar = preguntarMarcado.get("preguntar");
			Boolean marcado   = preguntarMarcado.get("marcado");
			
			if(preguntar==null||marcado==null)
			{
				throw new ApplicationException("No se pudo recuperar status de operaci\u00f3n");
			}
			
			result.put("preguntar" , preguntar.equals(Boolean.TRUE) ? "S" : "N");
			result.put("marcado"   , marcado.equals(Boolean.TRUE)   ? "S" : "N");
			
			if(marcado.equals(Boolean.TRUE))
			{
				paso = "Guardando detalle";
				sb.append("\n").append(paso);
				
				mesaControlDAO.movimientoDetalleTramite(
						ntramite
						,new Date()                //feinicio
						,null                      //cdclausu
						,"El tr\u00e1mite se marc\u00f3 como impreso"  //comments
						,cdusuari
						,null                      //cdmotivo
						,cdsisrol
						,"S"
						,null
						,null
						,EstatusTramite.IMPRESO.getCodigo()
						,true
						);
			}
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		
		sb.append(Utils.log(
				 "\n@@@@@@ result=",result
				,"\n@@@@@@ marcarImpresionOperacion @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		logger.debug(sb.toString());
		
		return result;
	}
	
	@Override
	public String recuperarColumnasGridPol(
			String cdsisrol
			,String cdtipram
			,String pantalla
			)throws Exception
	{
		StringBuilder sb = new StringBuilder(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ recuperarColumnasGridPol @@@@@@"
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				,"\n@@@@@@ cdtipram=" , cdtipram
				,"\n@@@@@@ pantalla=" , pantalla
				));
		
		String columns = null
		       ,paso   = null;
		
		try
		{
			paso = "Recuperando componentes";
			logger.debug(paso);
			
			List<ComponenteVO> compsGridPolizas = pantallasDAO.obtenerComponentes(
					cdtipram 
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,cdsisrol
					,pantalla
					,"GRID_POLIZAS_CDTIPRAM"
					,null //orden
					);
			
			paso = "Construyendo componentes";
			logger.debug(paso);
			
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			
			gc.generaComponentes(compsGridPolizas, true, false, false, true, false, false);
			columns = gc.getColumns().toString();
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso, sb.toString());
		}
		
		sb.append(Utils.log(
				 "\n@@@@@@ columns=",columns
				,"\n@@@@@@ recuperarColumnasGridPol @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		
		logger.debug(sb.toString());
		
		return columns;
	}
	
	@Override
    public DescargaLotePdfVO descargarLote(
            String lote, 
            String hoja, 
            String peso, 
            String cdtipram, 
            String cdtipimp,
            String tipolote, 
            String cdusuari, 
            String cdsisrol
            ) throws Exception 
	{
        StringBuilder sb = new StringBuilder(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@", 
                "\n@@@@@@ descargarLote @@@@@@",
                "\n@@@@@@ lote=", lote, 
                "\n@@@@@@ hoja=", hoja, 
                "\n@@@@@@ peso=", peso, 
                "\n@@@@@@ cdtipram=", cdtipram,
                "\n@@@@@@ cdtipimp=", cdtipimp, 
                "\n@@@@@@ tipolote=", tipolote, 
                "\n@@@@@@ cdusuari=", cdusuari,
                "\n@@@@@@ cdsisrol=", cdsisrol));

        InputStream inputStream = null;
        DescargaLotePdfVO pdfReturn = new DescargaLotePdfVO();

        String paso = "Iniciando descarga";
        sb.append("\n").append(paso);

        try {
            // TODO si el papel es multiple hay que mandar por charola
            // especifica (B por la 1, M por la 2)
            if (hoja.length() > 1) {
                hoja = hoja.substring(0, 1);
                // throw new ApplicationException("No soporta intercalada,
                // consulte a desarrollo");
            }

            paso = "Recuperando archivos";
            sb.append("\n").append(paso);

            List<Map<String, String>> listaArchivos = consultasDAO.recuperarArchivosParaImprimirLote(lote,
                    hoja.equalsIgnoreCase("B") ? "BM" : hoja, tipolote);
            paso = "Descargando archivos";
            sb.append("\n").append(paso);

            descargaUrl(listaArchivos);

            paso = "Regenerando archivos";
            long timestamp = System.currentTimeMillis();
            long rand = new Double(1000d * Math.random()).longValue();
            File noExiste = new File(Utils.join(rutaDocumentosTemporal, "/archivos_no_encontrados_lote_", lote, "_",
                    timestamp, rand, ".txt"));
            AtomicBoolean hayErrores = new AtomicBoolean(false);

            if (!"C".equals(hoja)) {
                listaArchivos = armaJuegosDeImpresiones(listaArchivos);
            }
            List<Map<String, String>> listaArchivosOriginal = listaArchivos;
            listaArchivos = regeneraDocs(listaArchivos, noExiste, cdtipram, hayErrores,lote,cdusuari);

            sb.append(Utils.log("\nlista=", listaArchivos));

            paso = "Fusionando archivos";
            sb.append("\n").append(paso);

            List<File> files = new ArrayList<File>();
            paso = "Generando archivo";

            for (Map<String, String> archivo : listaArchivos) {
                try {
                    String ntramite = archivo.get("ntramite");
                    String cddocume = archivo.get("cddocume");
                    String dsdocume = archivo.get("dsdocume");
                    String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
                    String nmpoliza = archivo.get("nmpoliza");

                    sb.append(Utils.log("\ntramite,archivo=", ntramite, ",", cddocume));

                    if (cddocume.toLowerCase().indexOf("://") != -1) {
                        paso = "Usando archivo local descargado";
                        
                        sb.append("\n").append(paso);
                        sb.append(Utils.join("\nEl archivo: ",cddocume," ya existe, se usar\u00E1 :",archivo.get("descargadoEn")));

                        filePath = archivo.get("descargadoEn");

                        if (StringUtils.isBlank(filePath)) {

                            logger.error(Utils.join("Error en descarga-: ", cddocume));
                            continue;
                        }
                    }

                    files.add(new File(filePath));

                } catch (Exception e) {
                    logger.error("Error Generando archivo", e);
                    hayErrores.set(true);
                    ;
                }

            }

            if (hayErrores.get()) {
                pdfReturn.setErrores(noExiste);
            } else {
                pdfReturn.setErrores(null);
            }

            

            paso = "Creando archivo fusionado";
            File fusionado = DocumentosUtils.mixPdf(files, new File(Utils.join(rutaDocumentosTemporal, "/lote_", lote,
                    "_fusion_papel_", hoja, "_t_", System.currentTimeMillis(), ".pdf")));
            borrarArchivosTemporales(listaArchivos);
            if (fusionado == null || !fusionado.exists()) {
                throw new ApplicationException("El archivo no fue creado");
            }

            inputStream = new FileInputStream(fusionado);

            listaArchivos = listaArchivosOriginal;

            paso = "Actualizando remesas, emisiones y endosos";
            sb.append("\n").append(paso);

            /*
             * en este procedimiento se actualizan las sumas, y si estan
             * completas: se marcan las remesas como impresas, y tambien los
             * hijos cuando son emisiones/endosos
             */
            boolean impresos = emisionDAO.sumarImpresiones(lote, tipolote, peso);

            if ("R".equals(tipolote)) {
                paso = "Actualizando recibos";
                sb.append("\n").append(paso);

                List<DocumentoReciboParaMostrarDTO> listaRecibos = new ArrayList<DocumentoReciboParaMostrarDTO>();
                for (Map<String, String> archivo : listaArchivos) {
                    listaRecibos
                            .add(new DocumentoReciboParaMostrarDTO(archivo.get("ntramite"), archivo.get("cddocume")));
                }
                logger.debug("listaRecibos: {}", listaRecibos);
                emisionDAO.mostrarRecibosImpresosListaDeListas(listaRecibos);
            }

            paso = "Recuperando remesas del lote";
            sb.append("\n").append(paso);

            List<Map<String, String>> remesas = mesaControlDAO.recuperarTramites(null // cdunieco
                    , null // ntramite
                    , null // cdramo
                    , null // nmpoliza
                    , null // estado
                    , null // cdagente
                    , "0" // status
                    , null // cdtipsit
                    , null // fedesde
                    , null // fehasta
                    , cdsisrol, TipoTramite.IMPRESION.getCdtiptra(), null // contrarecibo
                    , null // tipoPago
                    , null // nfactura
                    , null // cdpresta
                    , null // cdusuari
                    , null // cdtipram
                    , lote, null // tipolote
                    , null // tipoimpr
                    , null // cdusuari_busq
            );

            for (Map<String, String> remesa : remesas) {
                paso = "Guardando detalle de remesa";
                sb.append("\n").append(paso);

                mesaControlDAO
                        .movimientoDetalleTramite(remesa.get("ntramite"), new Date() // feinicio
                                , null // cdclausu
                                ,
                                Utils.join(
                                        impresos ? "Se realiz\u00F3 la impresi\u00F3n final de la remesa ("
                                                : "Se realiz\u00F3 una impresi\u00F3n de la remesa (",
                                        "B".equals(hoja) ? ("papeler\u00EDa")
                                                : ("M".equals(hoja) ? ("recibos")
                                                        : ("C".equals(hoja) ? ("credenciales")
                                                                : ("papeler\u00EDa y recibos"))),
                                        ")"),
                                cdusuari, null // cdmotivo
                                , cdsisrol, "S", null, null, remesa.get("status"), impresos);
            }

        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso, sb.toString());
        }

        sb.append(Utils.log("\n@@@@@@ inputStream=", inputStream, "\n@@@@@@ descargarLote @@@@@@",
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"));

        logger.debug(sb.toString());

        pdfReturn.setFileInput(inputStream);
        return pdfReturn;
    }

	
	
	
	@Override
    public DescargaLotePdfVO descargarLoteDplx(
            String lote, 
            String hoja, 
            String peso, 
            String cdtipram, 
            String cdtipimp,
            String tipolote, 
            String cdusuari, 
            String cdsisrol
            ) throws Exception 
	{

        StringBuilder sb = new StringBuilder(Utils.log(
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@",
                "\n@@@@@@ descargarLoteDplx @@@@@@", 
                "\n@@@@@@ lote=", lote, 
                "\n@@@@@@ hoja=", hoja,
                "\n@@@@@@ peso=", peso, 
                "\n@@@@@@ cdtipram=", cdtipram, 
                "\n@@@@@@ cdtipimp=", cdtipimp, 
                "\n@@@@@@ tipolote=", tipolote,
                "\n@@@@@@ cdusuari=", cdusuari, 
                "\n@@@@@@ cdsisrol=", cdsisrol));

        InputStream inputStream = null;
        DescargaLotePdfVO pdfReturn = new DescargaLotePdfVO();

        String paso = "Iniciando descarga";
        sb.append("\n").append(paso);

        try {
            // TODO si el papel es multiple hay que mandar por charola
            // especifica (B por la 1, M por la 2)
            if (hoja.length() > 1) {
                // throw new ApplicationException("No soporta intercalada,
                // consulte a desarrollo");
            }

            paso = "Recuperando archivos";
            sb.append("\n").append(paso);

            List<Map<String, String>> listaArchivos = consultasDAO.recuperarArchivosParaImprimirLote(lote,
                    hoja.equalsIgnoreCase("B") ? "BM" : hoja, tipolote);

            paso = "Descargando archivos";
            sb.append("\n").append(paso);

            descargaUrl(listaArchivos);

            long timestamp = System.currentTimeMillis();
            long rand = new Double(1000d * Math.random()).longValue();
            File noExiste = new File(Utils.join(rutaDocumentosTemporal, "/archivos_no_encontrados_lote_", lote, "_",
                    timestamp, rand, ".txt"));
            paso = "Regenerando archivos";
            sb.append("\n").append(paso);
            AtomicBoolean hayErrores = new AtomicBoolean(false);

            if (!"C".equals(hoja)) {
                listaArchivos = armaJuegosDeImpresiones(listaArchivos);
            }
            List<Map<String, String>> listaArchivosOriginal = listaArchivos;
            listaArchivos = regeneraDocs(listaArchivos, noExiste, cdtipram, hayErrores,lote,cdusuari);

            sb.append(Utils.log("\nlista=", listaArchivos));

            paso = "Fusionando archivos";
            sb.append("\n").append(paso);

            List<File> files = new ArrayList<File>();

            for (Map<String, String> archivo : listaArchivos) {

                try {
                    String ntramite = archivo.get("ntramite");
                    String cddocume = archivo.get("cddocume");
                    String swimpdpx = archivo.get("swimpdpx");
                    String dsdocume = archivo.get("dsdocume");
                    String nmpoliza = archivo.get("nmpoliza");

                    String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
                    sb.append(Utils.log("\ntramite,archivo=", ntramite, ",", cddocume));

                    if (cddocume.toLowerCase().indexOf("://") != -1) {

                        paso = "Usando archivo local descargado";

                        sb.append("\n").append(paso);
                        sb.append(Utils.join("\nEl archivo: ",cddocume," ya existe, se usar\u00E1 :",archivo.get("descargadoEn")));

                        filePath = archivo.get("descargadoEn");

                        if (StringUtils.isBlank(filePath)) {

                            logger.error(Utils.join("Error en descarga-: ", cddocume));
                            continue;
                        }

                    }

                    if (!dsdocume.toLowerCase().contains("recibo") && "N".equalsIgnoreCase(swimpdpx.trim())) {
                        File blanco=new File(Utils
                                .join(rutaDocumentosTemporal, "/blanco", lote, "_t_", timestamp, "_", rand, ".pdf"));
                        files.add(DocumentosUtils.blancoParaDuplex(new File(filePath), blanco ));
                        try{
                            blanco.delete();
                        }catch(Exception e){
                            logger.error("Error borrando pdf blanco");
                        }
                    } else {
                        files.add(new File(filePath));
                    }

                } catch (Exception e) {
                    logger.error("Error Generando archivo", e);
                    hayErrores.set(true);
                }

            }

            if (hayErrores.get()) {
                pdfReturn.setErrores(noExiste);
            } else {
                pdfReturn.setErrores(null);
            }

            paso = "Creando archivo fusionado";
            File fusionado = DocumentosUtils.mixPdf(files, new File(Utils.join(rutaDocumentosTemporal, "/lote_", lote,
                    "_fusion_papel_", hoja, "_t_", System.currentTimeMillis(), ".pdf")));
            
            borrarArchivosTemporales(listaArchivos);
            if (fusionado == null || !fusionado.exists()) {
                throw new ApplicationException("El archivo no fue creado");
            }

            inputStream = new FileInputStream(fusionado);
            listaArchivos = listaArchivosOriginal;
            paso = "Actualizando remesas, emisiones y endosos";
            sb.append("\n").append(paso);

            /*
             * en este procedimiento se actualizan las sumas, y si estan
             * completas: se marcan las remesas como impresas, y tambien los
             * hijos cuando son emisiones/endosos
             */
            boolean impresos = emisionDAO.sumarImpresiones(lote, tipolote, peso);

            if ("R".equals(tipolote)) {
                paso = "Actualizando recibos";
                sb.append("\n").append(paso);

                List<DocumentoReciboParaMostrarDTO> listaRecibos = new ArrayList<DocumentoReciboParaMostrarDTO>();
                for (Map<String, String> archivo : listaArchivos) {
                    listaRecibos
                            .add(new DocumentoReciboParaMostrarDTO(archivo.get("ntramite"), archivo.get("cddocume")));
                }
                logger.debug("listaRecibos: {}", listaRecibos);

                emisionDAO.mostrarRecibosImpresosListaDeListas(listaRecibos);
            }

            paso = "Recuperando remesas del lote";
            sb.append("\n").append(paso);

            List<Map<String, String>> remesas = mesaControlDAO.recuperarTramites(null // cdunieco
                    , null // ntramite
                    , null // cdramo
                    , null // nmpoliza
                    , null // estado
                    , null // cdagente
                    , "0" // status
                    , null // cdtipsit
                    , null // fedesde
                    , null // fehasta
                    , cdsisrol, TipoTramite.IMPRESION.getCdtiptra(), null // contrarecibo
                    , null // tipoPago
                    , null // nfactura
                    , null // cdpresta
                    , null // cdusuari
                    , null // cdtipram
                    , lote, null // tipolote
                    , null // tipoimpr
                    , null // cdusuari_busq
            );

            for (Map<String, String> remesa : remesas) {
                paso = "Guardando detalle de remesa";
                sb.append("\n").append(paso);

                mesaControlDAO
                        .movimientoDetalleTramite(remesa.get("ntramite"), new Date() // feinicio
                                , null // cdclausu
                                ,
                                Utils.join(
                                        impresos ? "Se realiz\u00F3 la impresi\u00F3n final de la remesa ("
                                                : "Se realiz\u00F3 una impresi\u00F3n de la remesa (",
                                        "B".equals(hoja) ? ("papeler\u00EDa")
                                                : ("M".equals(hoja) ? ("recibos")
                                                        : ("C".equals(hoja) ? ("credenciales")
                                                                : ("papeler\u00EDa y recibos"))),
                                        ")"),
                                cdusuari, null // cdmotivo
                                , cdsisrol, "S", null, null, remesa.get("status"), impresos);
            }

        } catch (Exception ex) {
            Utils.generaExcepcion(ex, paso, sb.toString());
        }

        sb.append(Utils.log("\n@@@@@@ inputStream=", inputStream, "\n@@@@@@ descargarLoteDplx @@@@@@",
                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@"));

        logger.debug(sb.toString());
        pdfReturn.setFileInput(inputStream);
        return pdfReturn;
    }

	
	/*public static void main(String[] args)
	{
		List<Map<String,String>> lista = new ArrayList<Map<String,String>>();
		
		Map<String,String> m1 = new LinkedHashMap<String,String>();
		m1.put("m1"     , "");
		m1.put("conteo" , "1");
		
		Map<String,String> m2 = new LinkedHashMap<String,String>();
		m2.put("m2"     , "");
		m2.put("conteo" , "3");
		
		Map<String,String> m3 = new LinkedHashMap<String,String>();
		m3.put("m3"     , "");
		m3.put("conteo" , "3");
		
		Map<String,String> m4 = new LinkedHashMap<String,String>();
		m4.put("m4"     , "");
		m4.put("conteo" , "3");
		
		Map<String,String> m5 = new LinkedHashMap<String,String>();
		m5.put("m5"     , "");
		m5.put("conteo" , "1");
		
		Map<String,String> m6 = new LinkedHashMap<String,String>();
		m6.put("m6"     , "");
		m6.put("conteo" , "1");
		
		lista.add(m1);
		lista.add(m2);
		lista.add(m3);
		lista.add(m4);
		lista.add(m5);
		lista.add(m6);
		
		try
		{
			ExplotacionDocumentosManagerImpl.armaJuegosDeImpresiones(lista, "conteo");
		}
		catch(Exception ex)
		{
			logger.error(Utils.manejaExcepcion(ex));
		}
	}*/
	
	private List<Map<String,String>> armaJuegosDeImpresiones(List<Map<String,String>> listaOriginal) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ armaJuegosDeImpresiones @@@@@@"
				,"\n@@@@@@ listaOriginal=" , listaOriginal
				));
		
		List<Map<String,String>> listaArmada = new ArrayList<Map<String,String>>();
		
		String paso         = null
		       ,keyConteo   = "nmcopias"
		       ,keyNmpoliza = "nmpoliza"
		       ,keyNumend   = "numend";
		
		try
		{
			paso = "Verificando propiedad de conteo";
			logger.debug(paso);
			
			try
			{
				for(Map<String,String> el:listaOriginal)
				{
					Integer.parseInt(el.get(keyConteo));
				}
			}
			catch(Exception ex)
			{
				logger.error(paso, ex);
				throw new ApplicationException("La propiedad de conteo no es correcta para todos los elementos de la lista");
			}
			
			paso = "Procesando lista";
			logger.debug(paso);
			
			for(int i=0 ; i<listaOriginal.size() ; i++)
			{
				Map<String,String> el = listaOriginal.get(i);
				
				int conteo = Integer.parseInt(el.get(keyConteo));
				
				if(conteo==0)
				{
					logger.debug(Utils.log("No se pasa elemento porque tiene 0 i=", i, " el=",el));
				}
				else if(conteo==1)
				{
					logger.debug(Utils.log("Se pasa limpio el elemento i=", i, " el=",el));
					
					el.put(keyConteo, String.valueOf(conteo-1));
					listaArmada.add(el);
				}
				else
				{
					logger.debug(Utils.log("Se recorren por multiple a partir de i=", i, " el=",el));
					
					int finMultiples = -1;
					
					String nmpolizaEl = el.get(keyNmpoliza)
					       ,numendEl  = el.get(keyNumend);
					
					for(int j=i ; j<listaOriginal.size() ; j++)
					{
						Map<String,String> elMultiplesCopias = listaOriginal.get(j);
						
						int conteoElMultiplesCopias = Integer.parseInt(elMultiplesCopias.get(keyConteo));
						
						String nmpolizaElMultiplesCopias = elMultiplesCopias.get(keyNmpoliza)
						       ,numendElMultiplesCopias  = elMultiplesCopias.get(keyNumend);
						
						if(conteoElMultiplesCopias < 2
								|| !nmpolizaEl.equals(nmpolizaElMultiplesCopias)
								|| !numendEl.equals(numendElMultiplesCopias)
						)
						{
							logger.debug(Utils.log("termina el buscado de multiples con j=", j, " el=", elMultiplesCopias));
							finMultiples = j;
							break;
						}
						
						logger.debug(Utils.log("Se incluye en el recorrido por multiples j=", j, " el=",elMultiplesCopias));
					}
					
					if(finMultiples == -1)
					{
						logger.debug("Hasta el ultimo elemento es multiple");
						finMultiples = listaOriginal.size();
					}
					
					logger.debug("Se van a empezar a pasar los marcados como multiples en un do-while>for");
					
					boolean algunMultiplePasado = false;
					
					do
					{
						logger.debug(Utils.log("Se inicia pase de multiples desde ", i, " hasta ", finMultiples));
						
						algunMultiplePasado = false;
						
						for(int j=i; j<finMultiples ; j++)
						{
							Map<String,String> elMultipleParaMover = listaOriginal.get(j);
							
							int conteoElMultipleParaMover = Integer.parseInt(elMultipleParaMover.get(keyConteo));
							
							if(conteoElMultipleParaMover>0)
							{
								logger.debug(Utils.log("Se mueve elemento multiple j=", j, " porque tiene conteo=", conteoElMultipleParaMover));
								
								elMultipleParaMover.put(keyConteo, String.valueOf(conteoElMultipleParaMover-1));
								
								listaArmada.add(elMultipleParaMover);
								
								algunMultiplePasado = true;
							}
							else
							{
								logger.debug(Utils.log("Se igonar elemento multiple j=", j, " porque tiene conteo=", conteoElMultipleParaMover));
							}
						}
					}
					while(algunMultiplePasado);
				}
				
			}
			
			
			paso = "Reiniciando contadores";
			logger.debug(paso);
			
			for(int i=0 ; i<listaArmada.size() ; i++)
			{
				listaArmada.get(i).put(keyConteo, "1");
			}
			
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ listaArmada=", listaArmada
				,"\n@@@@@@ armaJuegosDeImpresiones @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return listaArmada;
	}
	
		
		   /**
	     * Recibe una lista de documentos para regenerarlos en el caso de que no existan o esten corruptos
	     * 
	     * @param documentos Documentos a verificar, si no existen o estan corruptos se regeneraran
	     * @param archivoErrrores  Archivo de errores con la lista de documentos que se intentaron regenerar y fallaron
	     * @param cdtipram Tipo de Ramo
	     * @param hayErrores Indica si se escribieron errores en el archivo de errores
	     * @param lote      Numero de lote (opcional)
	     * @param cdusuari  Usuario que ejecuta el proceso (opcional)
	     * @return Lista de archivos correctos (ya existentes y regenerados exitosamente)
	     * @throws Exception
	     */
	    private List<Map<String, String>> regeneraDocs(List<Map<String, String>> documentos, File archivoErrrores,
	            String cdtipram, AtomicBoolean hayErrores, String lote, String cdusuari) throws Exception {
	        
	        logger.debug(Utils.join("@@@@@@@@@@@@@@@@@@@@@@@@"
	                               ,"@@@@@ regeneraDocs"
	                               ,"@@@@@ documentos = ", documentos
	                              , "@@@@@ cdtipram = ", cdtipram));
	        List<Map<String, String>> docsExisten = new ArrayList<Map<String, String>>();
	        try{
	    
	            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoErrrores, true));
	            bw.append("POLIZA,DOCUMENTO,DESCRIPCION\r\n");
	            // bw.close();
	    
	            for (Map<String, String> archivo : documentos) {
	    
	                String ntramite = archivo.get("ntramite");
	                String cddocume = archivo.get("cddocume");
	                String dsdocume = archivo.get("dsdocume");
	                String nmpoliza = archivo.get("nmpoliza");
	                String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
	                boolean existe = true;
	                logger.debug(Utils.join("\ntramite,archivo=", ntramite, ",", cddocume));
	    
	                if (cddocume.toLowerCase().indexOf("://") != -1) {
	    
	                    logger.debug("Usando archivo local descargado");
	    
	                    filePath = archivo.get("descargadoEn");
	    
	                    if (StringUtils.isBlank(filePath)) {
	    
	                        logger.error(Utils.join("Error en descarga-: ", cddocume));
	                        continue;
	                    }
	    
	                }
	                File f = new File(filePath);
	                logger.debug("Verificando si existe el archivo");
	    
	                if (!f.exists() || !DocumentosUtils.verificaPDF(f)) {
	    
	                    if (cdtipram.trim().equals("10")) {
	    
	                        String dirTramite = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/");
	                        logger.debug(Utils.join("\n@@@@ Creando directorio : ", dirTramite));
	                        File dir = new File(dirTramite);
	                        dir.mkdirs();
	                        logger.debug("Regenerando Docs");
	                        mesaControlDAO.regeneraRemesaReport(ntramite, cddocume);
	                    }
	                    
	                    if(lote!= null && cdusuari!=null && nombreRemesaPdf.equals(cddocume)){
	                        try{
	                                String urlReporteCotizacion = Utils.join(
	                                        rutaServidorReports
	                                      , "?p_lote="     , lote
	                                      , "&p_usr_imp="  , cdusuari
	                                      , "&p_ntramite=" , ntramite
	                                      , "&destype=cache"
	                                      , "&desformat=PDF"
	                                      , "&userid="        , passServidorReports
	                                      , "&ACCESSIBLE=YES"
	                                      , "&report="        , nombreReporteRemesa
	                                      , "&paramform=no"
	                                      );
	                              
	                              String pathRemesa=Utils.join(
	                                      rutaDocumentosPoliza
	                                      ,"/",ntramite
	                                      ,"/",nombreRemesaPdf
	                                      );
	                              
	                              HttpUtil.generaArchivo(urlReporteCotizacion, pathRemesa);
	                        }catch(Exception e){
	                            logger.debug(Utils.join("Error tratando de regenerar remesa lote:",lote," tramite: ",ntramite));
	                        }
	                    }
	                    
	                    
	    
	                    f = new File(filePath);
	    
	                    if (!f.exists() || !DocumentosUtils.verificaPDF(f)) {
	    
	                        logger.debug(Utils.join("\n@@@@No existe el archivo: ", filePath, "\n", archivo));
	                        existe = false;
	    
	                    }
	    
	                }
	    
	                if (!existe) {
	                    logger.debug("Escribiendo errores");
	                    bw.append(Utils.join(nmpoliza == null ? "" : nmpoliza, ",\"", cddocume, "\"", ",\"", dsdocume, "\"",
	                            "\r\n"));
	    
	                    hayErrores.set(true);
	    
	                } else {
	    
	                    docsExisten.add(archivo);
	                }
	    
	            }
	            bw.close();
	        }catch(Exception e){
	            Utils.generaExcepcion(e, "Error regenerando documentos");
	        }
	        
	        logger.debug(Utils.log(
	                
	                "\n@@@@@@ regeneraDocs @@@@@@"
	               ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	               ));

	        return docsExisten;
	    }	
		
		
		
	public ImpresionLayoutVO verificaLayout(
			List<Map<String,String>> layout,
			String tpdocum,
			String cdusuari,
			String cdsisrol,
			String usuario,
			String pass,
			String dirLayouts,
			String server1,
			String server2
			)throws Exception{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ verificaLayout @@@@@@@@@@@@@@@"
				,"\n@@@@@@ usuario=" , usuario
				,"\n@@@@@@ pass=" , pass
				,"\n@@@@@@ dirLayouts=" , dirLayouts
				,"\n@@@@@@ server1=" , server1
				,"\n@@@@@@ server2=" , server2
				,"\n@@@@@@ cdsisrol=" , cdsisrol
				
				));
		List<Map<String,String>> respuesta=null;
		String paso="";
		long timestamp = System.currentTimeMillis();
		long rand      = new Double(1000d*Math.random()).longValue();
		File archivoTxt = new File(Utils.join(rutaDocumentosTemporal,"/layoutPdf_"
												,timestamp,rand,".txt"));
		PrintStream output     = new PrintStream(archivoTxt);
		ImpresionLayoutVO layoutReturn=new ImpresionLayoutVO();
		
		paso="Escribiendo layout";
		for(Map<String,String> m: layout){
			
			
			
			output.print(m.get("SUCURSAL")==null?"":m.get("SUCURSAL"));
			output.print("|");
			output.print(m.get("RAMO")==null?"":m.get("RAMO"));
			output.print("|");
			output.print(m.get("POLIZA")==null?"":m.get("POLIZA"));
			output.print("|");
			output.print(m.get("CDTIPEND")==null?"":m.get("CDTIPEND"));
			output.print("|");
			output.print(m.get("NUMENDOSO")==null?"":m.get("NUMENDOSO"));
			output.print("|");
			output.print(m.get("SEQRECIBO")==null?"":m.get("SEQRECIBO"));
			output.print("|");
			output.print(m.get("NMCERTIF")==null?"":m.get("NMCERTIF"));
			output.print("|");
			output.println();
			
			
		}
		
		output.close();
		
		
		paso="Guardando layout en el server";
		
		logger.debug(Utils.join("@@@@ Archivo local=  ",archivoTxt.getAbsolutePath()));
		boolean transferidoAmbosServer=FTPSUtils.upload(server1
													,usuario
													, pass
													, archivoTxt.getAbsolutePath()
													, dirLayouts+"/"+archivoTxt.getName()) 
											&&
										FTPSUtils.upload(server2
													, usuario
													, pass
													, archivoTxt.getAbsolutePath()
													, dirLayouts+"/"+archivoTxt.getName())
				;
		
		if(!transferidoAmbosServer)
		{
			throw new ApplicationException("Error al transferir archivo al servidor");
		}
		
		respuesta=consultasDAO.cargaLayoutImpresion(archivoTxt.getName(), tpdocum, cdusuari);
		
		
		
		logger.debug("@@@@ Respuesta de cargaLayoutImpresion: ");
		logger.debug(respuesta.toString());
		
		paso="Ordenando layout";
		respuesta=sortLayout(layout, respuesta);
		
		layoutReturn.setValidacion(respuesta);
		logger.debug(respuesta.toString());
		
		if(respuesta !=null && !respuesta.isEmpty()){
			String pv_idproceso_i=respuesta.get(0).get("IDPROCESO");
			
			//List<Map<String, String>> documentos = consultasDAO.getDocumentosLayout(pv_idproceso_i, tpdocum, "B", cdusuari, cdsisrol);
			
			Map<String,String> map=new HashMap<String, String>();
			map.put("pv_idproceso_i", pv_idproceso_i);
			map.put("pv_cdtipimp_i", tpdocum);
			map.put("pv_papel_i", "B");
			map.put("pv_cdusuari_i", cdusuari);
			map.put("pv_cdsisrol_i", cdsisrol);
			
			layoutReturn.setDocumentos(map);
			
			
			for(Map<String,String> m: respuesta){
				if((m.get("OBSERVAC"))==null){
					m.put("VALIDO", "true");
				}else{
					m.put("VALIDO", "false");
				}
				if(m.get("SWIMPRIME").equalsIgnoreCase("N")){
					m.put("VALIDO", "false");
				}
				
			}
			
		}

		
		
		
		
		
		
		
		logger.debug(Utils.log(
				 "\n@@@@@@ respuesta = ", respuesta
				,"\n@@@@@@ verificaLayout @@@@@@@@@@@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return layoutReturn;
		
	}
	
	 @Override
	    public DescargaLotePdfVO generaPdfLayout(Map<String, String> map, String cdtipram, String hoja, boolean duplex)
	            throws Exception {

	        logger.debug(Utils.log("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "\n@@@@@@ generaPdfLayout @@@@@@@@@@@@@@@",
	                "\n@@@@@@ map=", map, "\n@@@@@@ cdtipram=", cdtipram, "\n@@@@@@ hoja=", hoja

	        ));

	        consultasDAO.remesaDocumentosLayout(map.get("pv_idproceso_i"), map.get("pv_cdtipimp_i"),
	                map.get("pv_cdusuari_i"), map.get("pv_cdsisrol_i"));
	        List<Map<String, String>> documentos = consultasDAO.getDocumentosLayout(map.get("pv_idproceso_i"),
	                map.get("pv_cdtipimp_i"), map.get("pv_papel_i"), map.get("pv_cdusuari_i"), map.get("pv_cdsisrol_i"));
	        logger.debug(Utils.log(

	                "\n@@@@@@ documentos=", documentos

	        ));

	        String paso = "";
	        InputStream inputStream = null;
	        DescargaLotePdfVO pdfReturn = new DescargaLotePdfVO();

	        try {
	            
	            paso = "Descargando archivos";
	            logger.debug(paso);

	            descargaUrl(documentos);
	            
	            long timestamp = System.currentTimeMillis();
	            long rand = new Double(1000d * Math.random()).longValue();
	            StringBuilder sb = new StringBuilder();
	            List<File> files = new ArrayList<File>();
	            File noExiste = new File(Utils.join(rutaDocumentosTemporal, "/archivos_no_encontrados_layout", "_",
	                    timestamp, rand, ".txt"));
	            AtomicBoolean hayErrores = new AtomicBoolean(false);
	            // Se intenta regenerar archivos si no existen, devuelve los
	            // archivos que si existen y crea una lista en un archivo con los
	            // documentos no encontrados
	            documentos = regeneraDocs(documentos, noExiste, cdtipram, hayErrores,null,null);

	            documentos = armaJuegosDeImpresiones(documentos);

	            logger.debug(Utils.log("\n@@@@ Documentos a imprimir: ", documentos));

	            for (Map<String, String> archivo : documentos) {
	                String ntramite = archivo.get("ntramite");
	                String cddocume = archivo.get("cddocume");
	                String dsdocume = archivo.get("dsdocume");
	                String nmpoliza = archivo.get("nmpoliza");
	                String swimpdpx = archivo.get("swimpdpx");
	                String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);

	                sb.append(Utils.log("\ntramite,archivo=", ntramite, ",", cddocume));

	                if (cddocume.toLowerCase().indexOf("://") != -1) {
	                    paso = "Usando archivo local descargado";

	                    sb.append("\n").append(paso);

	                    filePath = archivo.get("descargadoEn");

	                    if (StringUtils.isBlank(filePath)) {

	                        logger.error(Utils.join("Error en descarga-: ", cddocume));
	                        continue;
	                    }
	                }

	                if (duplex && !dsdocume.toLowerCase().contains("recibo") && "N".equalsIgnoreCase(swimpdpx.trim())) {
	                    files.add(DocumentosUtils.blancoParaDuplex(new File(filePath), new File(Utils
	                            .join(rutaDocumentosTemporal, "/blanco_layout", "_t_", timestamp, "_", rand, ".pdf"))));
	                } else {
	                    files.add(new File(filePath));
	                }

	            }

	            File fusionado = DocumentosUtils.mixPdf(files, new File(Utils.join(rutaDocumentosTemporal, "/layout",
	                    "_fusion_papel_", hoja, "_t_", System.currentTimeMillis(), ".pdf")));

	            borrarArchivosTemporales(documentos);
	            
	            if (fusionado == null || !fusionado.exists()) {
	                throw new ApplicationException("El archivo no fue creado");
	            }

	            inputStream = new FileInputStream(fusionado);

	            pdfReturn.setFileInput(inputStream);
	            if (hayErrores.get()) {
	                pdfReturn.setErrores(noExiste);
	            } else {
	                pdfReturn.setErrores(null);
	            }

	        } catch (Exception ex) {
	            // ex.printStackTrace();
	            Utils.generaExcepcion(ex, paso);
	        }
	        logger.debug(Utils.log("\n@@@@@@ respuesta = ", pdfReturn, "\n@@@@@@ generaPdfLayout @@@@@@@@@@@@@@@",
	                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));

	        return pdfReturn;
	    }

	
	private List<Map<String, String>> sortLayout(List<Map<String, String>> original,List<Map<String, String>> respuesta){
		logger.debug(Utils.join("@@@@ original  = ",original));
		logger.debug(Utils.join("@@@@ respuesta = ",respuesta));
		List<Map<String, String>> orden=new ArrayList<Map<String,String>>();
		
		for(Map<String, String> m1:original){
			for(Map<String, String> m2:respuesta){
				
				if(
						   m1.get("SUCURSAL").trim().equals(m2.get("SUCURSAL")==null?"":m2.get("SUCURSAL").trim()) 
						&& m1.get("RAMO").trim().equals(m2.get("RAMO")==null?"":m2.get("RAMO").trim()) 
						&& m1.get("POLIZA").trim().equals(m2.get("POLIZA")==null?"":m2.get("POLIZA").trim())
						&& m1.get("CDTIPEND").trim().equalsIgnoreCase(m2.get("CDTIPEND")==null?"":m2.get("CDTIPEND").trim())
						&& m1.get("NUMENDOSO").trim().equals(m2.get("NUMENDOSO")==null?"":m2.get("NUMENDOSO").trim())
						&& m1.get("SEQRECIBO").trim().equals(m2.get("SEQRECIBO")==null?"":m2.get("SEQRECIBO").trim())
						&& m1.get("NMCERTIF").trim().equals(m2.get("NMCERTIF")==null?"":m2.get("NMCERTIF").trim())
				){
					orden.add(m2);
					continue;
				}
			}
			
		}
		
		logger.debug(Utils.join("@@@@ orden=",orden));
		return orden;
	}
	
	public String borrarDatosLayout(String pv_idproceso_i) throws Exception{
		String paso="";
		String msg="";
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ borrarDatosLayout @@@@@@@@@@@@@@@"
				,"\n@@@@@@ pv_idproceso_i=" , pv_idproceso_i
				
				
				
				));
		try{
			paso="Borrando datos";
			msg= consultasDAO.eliminaZwimpxlayout(pv_idproceso_i);
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ msg = ", msg
				,"\n@@@@@@ borrarDatosLayout @@@@@@@@@@@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return msg;
	}
	@Override
	public String obtenerCdagente(String pv_cdusuari_i) throws Exception{
        String paso="";
        String cdagente="";
        logger.debug(Utils.log(
                 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ,"\n@@@@@@ obtenerCdagente @@@@@@@@@@@@@@@"
                ,"\n@@@@@@ pv_cdusuari_i=" , pv_cdusuari_i
                
                
                
                ));
        try{
            paso="Buscando cdagente";
            List<Map<String,String>>lista= consultasDAO.obtieneCdagente(pv_cdusuari_i);
            
            if(lista.isEmpty()){
                return null;
            }
            cdagente=lista.get(0).get("cdagente");    
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            Utils.generaExcepcion(ex, paso);
        }
        
        logger.debug(Utils.log(
                 "\n@@@@@@ cdagente = ", cdagente
                ,"\n@@@@@@ obtenerCdagente @@@@@@@@@@@@@@@"
                ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ));
        return cdagente;
    }
	
	
	@Override
    public List<Map<String,String>> polizasImprimirPromotor(String promotor,
            String cdtipram
            ,String cduniecos
            ,String cdramo
            ,String ramo
            ,String nmpoliza
            ,String fefecha
            ,String cdusuariLike
            ,String cdagente
            ,String cdusuariSesion
            ,String cduniecoSesion
            ,UserVO usuario) throws Exception{
        String paso="";
        List<Map<String,String>> resultado=new ArrayList<Map<String,String>>();
        logger.debug(Utils.log(
                 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ,"\n@@@@@@ polizasImprimirPromotor @@@@@@@@@@@@@@@"
                ,"\n@@@@@@ pv_idproceso_i=" 
                
                
                
                ));
        try{
            paso="Borrando datos";
            Map<String,String>params=new HashMap<String,String>();
            params.put("cdusuari",promotor);
            List<GenericVO> agentes = catalogosDAO.cargarAgentesPorPromotor(params);
            
            if(cdagente!=null && !"".equals(cdagente.trim())){
                List<Map<String,String>> lista = consultasDAO.recuperarPolizasParaImprimir(
                        cdtipram
                        ,cduniecos
                        ,cdramo
                        ,ramo
                        ,nmpoliza
                        ,Utils.parse(fefecha)
                        ,cdusuariLike
                        ,cdagente
                        ,usuario.getUser()
                        ,usuario.getCdUnieco()
                        );
                resultado=lista;
                
            }else{
            
                for(GenericVO agente:agentes){
                    
                    paso = "Recuperando polizas para explotar documentos";
                    logger.debug(Utils.log(""," paso: ",paso));
                    
                    
                    
                    List<Map<String,String>> lista = consultasDAO.recuperarPolizasParaImprimir(
                            cdtipram
                            ,cduniecos
                            ,cdramo
                            ,ramo
                            ,nmpoliza
                            ,Utils.parse(fefecha)
                            ,cdusuariLike
                            ,agente.getKey()
                            ,usuario.getUser()
                            ,usuario.getCdUnieco()
                            );
                    logger.debug(Utils.log(lista));
                    resultado.addAll(lista);
                    
                }
            }
            
            
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            Utils.generaExcepcion(ex, paso);
        }
        
        logger.debug(Utils.log(
                 "\n@@@@@@ resultado = ", resultado
                ,"\n@@@@@@ polizasImprimirPromotor @@@@@@@@@@@@@@@"
                ,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                ));
        return resultado;
    }
	
	/**
     * Descarga si hay archivos remotos en la lista y agrega un campo
     * [descargadoEn] que contiene la ruta del archivo local ya descargado.
     * 
     * @param original
     */
    private void descargaUrl(List<Map<String, String>> original) {
        
        String paso = "";
        
        logger.debug(Utils.join("\nLista de entrada a descargaUrl: ", original));

        for (Map<String, String> archivo : original) {
            try {
                String ntramite = archivo.get("ntramite");
                String cddocume = archivo.get("cddocume");
                String dsdocume = archivo.get("dsdocume");
                String filePath = Utils.join(rutaDocumentosPoliza, "/", ntramite, "/", cddocume);
                String nmpoliza = archivo.get("nmpoliza");

                logger.debug(Utils.log("\ntramite,archivo=", ntramite, ",", cddocume));

                if (cddocume.toLowerCase().indexOf("://") != -1) {
                    
                    paso = "Descargando archivo remoto";
                    cddocume = cddocume.replaceAll("\\s", "").trim();
                    logger.debug(paso);
                    long timestamp = System.currentTimeMillis();
                    long rand = new Double(1000d * Math.random()).longValue();
                    filePath = Utils.join(rutaDocumentosTemporal, "/Descargado", "_remesa_", archivo.get("remesa"),
                            "_tramite_", ntramite, "_t_", timestamp, "_", rand, ".pdf");

                    archivo.put("descargadoEn", filePath);

                    File local = new File(filePath);

                    try {
                        paso = "guardando en local";
                        InputStream remoto = HttpUtil
                                .obtenInputStream(cddocume.replace("https", "http").replace("HTTPS", "HTTP"));
                        FileUtils.copyInputStreamToFile(remoto, local);
                        logger.debug(Utils.join("\nArchivo remoto: ", cddocume, " Archivo local: ", filePath));
                    } catch (ConnectException ex) {
                        logger.error(Utils.join("Error al descargar documento : ", cddocume), ex);

                    } catch (Exception ex) {
                        logger.error("Error al descargar documento: ", ex);
                    }
                }
            } catch (Exception e) {
                logger.error("Error descargando url");

            }

           
        }

    }

    /**
     * Borra los archivos descargados por descargaUrl()
     * @param lista generada por descargaUrl()
     */
    private void borrarArchivosTemporales(List<Map<String, String>> lista) {

        logger.debug(Utils.join("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@-"
                    ,"\n@@@@@ borrarArchivosTemporales"
                    ,"\n@@@@@ lista = ",lista));
        
        try {

            for (Map<String, String> it : lista) {

                if(it.get("descargadoEn")==null){
                    continue;
                }
                logger.debug("Borrando archivo: "+it.get("descargadoEn"));
                File f = new File(it.get("descargadoEn"));
                if(f.delete())
                    logger.debug("File was successfully deleted.");
                else
                    logger.debug("File was not deleted.");

            }
        } catch (Exception e) {
            logger.error("Error borrando archivos temporales", e);
        }
        logger.debug(Utils.join("@@@@@ borrarArchivosTemporales",
                                "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
                );
    }
	
	
	
		
}


