package mx.com.gseguros.portal.emision.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.consultas.model.PolizaDTO;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.cotizacion.model.Item;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaVoidVO;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.portal.emision.service.EmisionManager;
import mx.com.gseguros.portal.general.dao.PantallasDAO;
import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.portal.general.util.GeneradorCampos;
import mx.com.gseguros.utils.HttpUtil;
import mx.com.gseguros.utils.Utils;

public class EmisionManagerImpl implements EmisionManager
{
	private Map<String,Object> session;
	
	private static Logger logger = LoggerFactory.getLogger(EmisionManagerImpl.class);
	private static SimpleDateFormat renderFechas = new SimpleDateFormat("dd/MM/yyyy");
	@Value("${sigs.generarPolizaRecupera.url}")
	private String urlGeneraPolizaRecupera;
	
	private PantallasDAO pantallasDAO;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Autowired
	private EmisionDAO emisionDAO;
	
	@Autowired
	@Qualifier("consultasDAOICEImpl")
	private ConsultasPolizaDAO consultasPolizaDAO;
	
	@Value("${url.descargar.documentos}")
	private String urlDescargarDocumentosLibre;
	
	@Override
	public ManagerRespuestaImapVO construirPantallaClausulasPoliza() throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ construirPantallaClausulasPoliza @@@@@@"
				));
		
		ManagerRespuestaImapVO resp=new ManagerRespuestaImapVO(true);
		resp.setImap(new HashMap<String,Item>());
		
		String paso = null;
		
		try
		{
			paso = "Recuperando combo de clausulas por poliza";
			List<ComponenteVO>comboClausulas=pantallasDAO.obtenerComponentes(
					null  //cdtiptra
					,null //cdunieco
					,null //cdramo
					,null //cdtipsit
					,null //estado
					,null //cdsisrol
					,"PANTALLA_CLAUSULAS_POLIZA"
					,"COMBO_CLAUSULAS"
					,null //orden
					);
			Utils.validate(comboClausulas , "Error al obtener el combo de clausulas por poliza");
			GeneradorCampos gc = new GeneradorCampos(ServletActionContext.getServletContext().getServletContextName());
			gc.generaComponentes(comboClausulas, true, false, true, false, false, false);
			resp.getImap().put("comboClausulas" , gc.getItems());
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex,paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ ",resp
				,"\n@@@@@@ construirPantallaClausulasPoliza @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
	@Override
	public ManagerRespuestaVoidVO guardarClausulasPoliza(List<Map<String,String>>clausulas)
	{
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ guardarClausulasPoliza @@@@@@"
				,"\n@@@@@@ clausulas=",clausulas
				));
		
		ManagerRespuestaVoidVO resp=new ManagerRespuestaVoidVO(true);
		
		logger.debug(Utils.log(
				 "\n@@@@@@ ",resp
				,"\n@@@@@@ guardarClausulasPoliza @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return resp;
	}
	
	/*
	@Deprecated
	@Override
	public String insercionDocumentosParametrizados(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String proceso
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ insercionDocumentosParametrizados @@@@@@"
				,"\n@@@@@@ cdunieco=" , cdunieco
				,"\n@@@@@@ cdramo="   , cdramo
				,"\n@@@@@@ estado="   , estado
				,"\n@@@@@@ nmpoliza=" , nmpoliza
				,"\n@@@@@@ nmsituac=" , nmsituac
				,"\n@@@@@@ nmsuplem=" , nmsuplem
				,"\n@@@@@@ proceso="  , proceso
				));
		
		String cdorddoc = null
		       ,paso    = null;
		try
		{
			paso = "Generando documentos parametrizados";
			logger.debug("Paso: {}",paso);
			cdorddoc = cotizacionDAO.insercionDocumentosParametrizados(
					cdunieco
					,cdramo
					,estado
					,nmpoliza
					,nmsituac
					,nmsuplem
					,proceso
					);
			logger.debug("cdorddoc: {}",cdorddoc);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ cdorddoc=",cdorddoc
				,"\n@@@@@@ insercionDocumentosParametrizados @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return cdorddoc;
	}
	*/
	
	/*
	 * Getters y setters
	 */
	
	@Override
	public void setSession(Map<String,Object>session)
	{
		this.session=session;
	}

	public void setPantallasDAO(PantallasDAO pantallasDAO) {
		this.pantallasDAO = pantallasDAO;
	}

	@Override
	public void getActualizaCuadroComision(Map<String, Object> paramsPoliage) throws Exception {
		try {
			cotizacionDAO.getActualizaCuadroComision(paramsPoliage);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	@Deprecated
	public void actualizaNmsituaextMpolisit(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac 
			,String nmsuplem
			,String nmsituaext
			)throws Exception
	{
		cotizacionDAO.actualizaNmsituaextMpolisit(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsituac
				,nmsuplem
				,nmsituaext
				);
	}
	
	@Override
	@Deprecated
	public void actualizaDatosMpersona(
			String cdunieco
			,String cdramo
			,String estado
			,String nmpoliza
			,String nmsituac
			,String nmsuplem
			,String cdestciv
			,String ocup
			)throws Exception
	{
		cotizacionDAO.actualizaDatosMpersona(
				cdunieco
				,cdramo
				,estado
				,nmpoliza
				,nmsituac
				,nmsuplem
				,cdestciv
				,ocup);
	}
	
	@Deprecated
	@Override
	public void validarDocumentoTramite (String ntramite, String cddocume) throws Exception {
		emisionDAO.validarDocumentoTramite(ntramite, cddocume);
	}
	
	@Override
	@Deprecated
	public String recuperarTramiteCotizacion (String cdunieco, String cdramo, String estado, String nmpoliza) throws Exception {
		return emisionDAO.recuperarTramiteCotizacion(cdunieco, cdramo, estado, nmpoliza);
	}
	
	public boolean revierteEmision(String cdunieco, String cdramo, String estado, String nmpoliza, String nmsuplem){
		
		logger.debug(" <<<<<<>>>>>> Entrando a Revierte Emision <<<<<<>>>>>>");
		try{
			emisionDAO.revierteEmision(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
			return true;
		}catch(Exception e){
			logger.error("XXXXXxxx  ERROR AL REVETIR LA EMISION xxxXXXXX:::"+e.getMessage(), e);
			return false;
		}
	}
	
	@Override
	public String generarLigasDocumentosEmisionLocalesIce (String ntramite) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ generarLigasDocumentosEmisionLocalesIce @@@@@@",
				"\n@@@@@@ ntramite = ", ntramite));
		String ligas = "",
			paso = null;
		try {
			paso = "Recuperando documentos generados por parametrizaci\u00f3n";
			logger.debug(paso);
			List<Map<String, String>> docsGenerados = emisionDAO.recuperarDocumentosGeneradosPorParametrizacion(ntramite);
			if (docsGenerados.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (Map<String, String> doc : docsGenerados) {
					sb.append(Utils.join(
							"<br/><br/><a style=\"font-weight: bold\" href=\"",
							urlDescargarDocumentosLibre,
							"?subfolder=", ntramite,
							"&filename=", doc.get("CDDOCUME"),
							"\">",
							doc.get("DSDOCUME"),
							"</a>"));
				}
				ligas = sb.toString();
			}
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				"\n@@@@@@ ligas =", ligas,
				"\n@@@@@@ generarLigasDocumentosEmisionLocalesIce @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"));
		return ligas;
	}
	
	@Override
	public ManagerRespuestaSlistVO procesarCargaMasivaRecupera(File excel) throws Exception {
		String errores = "";
		String campo = "";
		double cambio = 0;
		String nombre = "";
		int entero = 0;
		String num_suc = "";
		String nombre_Completo = "";
		logger.debug(new StringBuilder().append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
				.append("\n@@@@@@ procesarCargaMasivaRecupera @@@@@@").append("\n@@@@@@ excel=").append(excel)
				.toString());

		ManagerRespuestaSlistVO resp = new ManagerRespuestaSlistVO(true);
		resp.setSlist(new ArrayList<Map<String, String>>());

		String paso = null;

		try {
			paso = "Iniciando procesador de hoja de calculo";
			FileInputStream input = new FileInputStream(excel);
			XSSFWorkbook workbook = new XSSFWorkbook(input);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			StringBuilder sb;

			paso = "Iterando filas";
			int fila = 1;
			while (rowIterator.hasNext()) {
				fila = fila + 1;
				paso = new StringBuilder("Iterando fila ").append(fila).toString();
				logger.debug("PASO: " + paso);
				Row row = rowIterator.next();

				if (fila == 2) {
					row = rowIterator.next();
				}
				logger.debug("este " + Utils.isRowEmpty(row));
				if (Utils.isRowEmpty(row)) {
					break;
				}
				sb = new StringBuilder();

				Map<String, String> record = new LinkedHashMap<String, String>();
				int col = 0;
				for (int i = 0; i <= 17; i++) {
					paso = "Columna numero: " + i;
					PolizaAseguradoVO datos = new PolizaAseguradoVO();
					col = i;
					Cell cell = row.getCell(i);

					if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
						if (i != 14 && i != 15) {
							if (i == 11 || i == 16) {
								campo = cell.toString().trim();
								SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
								SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
								campo = sdf2.format(sdf1.parse(campo));

							} else {
								campo = String.format("%.2f", cell.getNumericCellValue());
								cambio = Double.parseDouble(campo);
								entero = (int) cambio;
								campo = String.valueOf(entero);
							}
						} else {
							campo = String.format("%.2f", cell.getNumericCellValue());
						}
					} else
						campo = cell.toString().trim();
					// asignacion de nombres
					switch (i) {
					case 0:
						nombre = "CDUNIECO";
						if (!isNumeric(String.valueOf(campo))) {
							errores = errores + "Error en el campo 'CDPERSON' de la fila " + (fila - 1)
									+ " debe de ser numerico \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 1:
						nombre = "SUCURSAL";
						if (!campo.trim().equals("1998")) {
							errores = errores + "Error en el campo 'SUCURSAL' de la fila " + (fila - 1)
									+ " sucursal invalida \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						} else
							num_suc = cell.toString().trim();
						datos.setCdunieco(cell.toString().trim());
						break;
					case 2:
						/// VALIDAR QUE NO EXISTA POLIZA EN MPOLIZAS///
						nombre = "POLIZA";
						double numPol = Double.parseDouble(campo);
						datos.setCdunieco(String.valueOf(num_suc));
						datos.setCdramo("1");
						datos.setEstado("M");
						datos.setNmpoliza(campo.trim());
						List<PolizaDTO> datosPolizas = consultasPolizaDAO.obtieneDatosPoliza(datos);
						for (PolizaDTO t : datosPolizas) {
							errores = errores + "Error en el campo 'POLIZA' de la fila " + (fila - 1)
									+ " numero de poliza " + datos.getNmpoliza() + " existente \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 3:
						nombre = "NOMBRE1";
						if (campo.trim().matches("([a-z]|[A-Z]|\\s)+") == false) {
							errores = errores + "Error en el campo 'NOMBRE' de la fila " + (fila - 1)
									+ " debe de ser texto \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						nombre_Completo = campo;
						break;
					case 4:
						nombre = "NOMBRE2";
						nombre_Completo += " " + campo;
						break;
					case 5:
						nombre = "APEPAT";
						if (campo.trim().matches("([a-z]|[A-Z]|\\s)+") == false) {
							errores = errores + "Error en el campo 'APELLIDO PATERNO' de la fila " + (fila - 1)
									+ " debe de ser texto \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						nombre_Completo += " " + campo;
						break;
					case 6:
						nombre = "APEMAT";
						nombre_Completo += " " + campo;
						record.put("Nombre_Completo", nombre_Completo);
						break;
					case 7:
						nombre = "PRODUCTO";
						if (!campo.trim().equals("RI")) {
							errores = errores + "Error en el campo 'PRODUCTO' de la fila " + (fila - 1)
									+ " solo se acepta producto 'RI' \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 8:
						nombre = "PLAN";
						if (!campo.trim().equals("70")) {
							errores = errores + "Error en el campo 'PLAN' de la fila " + (fila - 1)
									+ " solo se acepta plan '70' \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 9:
						nombre = "ESQUEMA";
						double esq1 = Double.parseDouble(campo);
						int esquema = (int) esq1;
						if (esquema != 20 && esquema != 50 && esquema != 100) {
							errores = errores + "Error en el campo 'ESQUEMA' de la fila " + (fila - 1)
									+ " solo se aceptan esquemas de '20,50 \u00F3 100' \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 10:
						nombre = "PARENTESCO";
						if (!campo.trim().equals("T")) {
							errores = errores + "Error en el campo 'PARENTESCO' de la fila " + (fila - 1)
									+ " solo se acepta 'T' \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 11:
						nombre = "FECNAC";
						Date auxDate = null;
						Date fecha = new Date();
						// FECHA NACIMIENTO
						try {
							auxDate = cell.getDateCellValue();
							logger.debug(Utils.log("�auxDate: ", auxDate));
							if (auxDate != null) {
								Calendar cal = Calendar.getInstance();
								cal.setTime(auxDate);
								if (cal.get(Calendar.YEAR) > 2100 || cal.get(Calendar.YEAR) < 1900) {
									throw new ApplicationException("El anio de la fecha no es valido");
								}
								Integer[] fecDif = diferencia(auxDate, fecha);
								if (fecDif[2] < 0 && fecDif[2] > 64) {
									errores = errores + "Error en el campo 'FECHA DE NACIMIENTO' de la fila "
											+ (fila - 1) + " no entra dentro del rango valido de edad '0-64'\n";
									resp.setRespuesta(errores);
									resp.setExito(false);
								}

							}
							logger.debug("FECHA NACIMIENTO: "
									+ (auxDate != null ? renderFechas.format(auxDate) + "|" : "|"));
						} catch (Exception ex) {
							errores = errores + "Error en el campo 'FECHA DE NACIMIENTO' de la fila " + (fila - 1)
									+ " formato invalido \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 12:
						nombre = "RFC";
						break;
					case 13:
						nombre = "SEXO";
						if ((!campo.trim().equals("H")) && (!campo.trim().equals("M"))) {
							errores = errores + "Error en el campo 'SEXO' de la fila " + (fila - 1)
									+ " solo se acepta 'H \u00F3 M' \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 14:
						nombre = "PESO";
						break;
					case 15:
						nombre = "ESTATURA";
						break;
					case 16:
						nombre = "FECINIVIG";
						auxDate = null;
						fecha = new Date();
						// FECHA NACIMIENTO
						try {
							auxDate = cell.getDateCellValue();
							logger.debug(Utils.log("�auxDate: ", auxDate));
							if (auxDate != null) {
								Calendar cal = Calendar.getInstance();
								cal.setTime(auxDate);
								if (cal.get(Calendar.YEAR) > 2100 || cal.get(Calendar.YEAR) < 1900) {
									throw new ApplicationException("El anio de la fecha no es valido");
								}
								Integer[] fecDif = diferencia(auxDate, fecha);
								if (fecDif[0] > 3 || fecDif[1] != 0 || fecDif[2] != 0) {
									errores = errores + "Error en el campo 'FECHA INICIO DE VIGENCIA' de la fila "
											+ (fila - 1) + " es mayor o menor a tres dias \n";
									resp.setRespuesta(errores);
									resp.setExito(false);
								}
							}
							logger.debug("FECHA NACIMIENTO: "
									+ (auxDate != null ? renderFechas.format(auxDate) + "|" : "|"));
						} catch (Exception ex) {
							errores = errores + "Error en el campo 'FECHA INICIO DE VIGENCIA' de la fila " + (fila - 1)
									+ " formato invalido \n";
							resp.setRespuesta(errores);
							resp.setExito(false);
						}
						break;
					case 17:
						nombre = "MEMBRESIA";
						break;
					}
					if (cell.getCellType() == cell.CELL_TYPE_NUMERIC)
						logger.debug("CELDA NUMERO: " + String.format("%.2f", cell.getNumericCellValue()) + "  " + i);
					else
						logger.debug("CELDA: " + cell + "  " + i);
					record.put(nombre, campo);
				} // end if
				resp.getSlist().add(record);
			}
			List<String> listaAseg = comparar("El asegurado ", " esta duplicado", resp.getSlist(), "NOMBRE1", "NOMBRE2","APEPAT", "APEMAT");
			List listaAseg2 = new ArrayList(listaAseg);
			Iterator it = listaAseg2.iterator();
			while (it.hasNext()) {
				errores += it.next() + " \n";
				resp.setRespuesta(errores);
				resp.setExito(false);
			}
			List<String> listaMem = comparar("La membresia ", " esta duplicada", resp.getSlist(), "MEMBRESIA");
			List listaMem2 = new ArrayList(listaMem);
			it = listaMem2.iterator();
			while (it.hasNext()) {
				errores += it.next() + " \n";
				resp.setRespuesta(errores);
				resp.setExito(false);
			}

		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(resp.getRespuesta());
		logger.info(new StringBuilder().append("\n@@@@@@ ").append(resp)
				.append("\n@@@@@@ procesarCargaMasivaRecupera @@@@@@")
				.append("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@").toString());
		return resp;
	}

	
	public String generarPoliza(String cdperson, String sucursal, String poliza, String nombre1, String nombre2,
			String apePat, String apeMat, String producto, String cve_plan, String esq_suma_aseg, String parentesco,
			String f_nacimiento, String RFC, String sexo, String peso, String estatura, String fecinivig,
			String membresia) {
		String mensaje = "";
		boolean exito;
		ArrayList<String> resultados = new ArrayList<String>();
		try {
			String params = Utils.join("cdperson=", cdperson, "&sucursal=", sucursal, "&poliza=", poliza, "&nombre1=",
					nombre1, "&nombre2=", nombre2, "&apePat=", apePat, "&apeMat=", apeMat, "&producto=", producto,
					"&cvePlan=", cve_plan, "&esqSumaAseg=", esq_suma_aseg, "&parentesco=", parentesco, "&fNacimiento=",
					f_nacimiento, "&rfc=", RFC, "&sexo=", sexo, "&peso=", peso, "&estatura=", estatura, "&fecinivig=",
					fecinivig, "&membresia=", membresia),
					respuestaWS = HttpUtil.sendPost(urlGeneraPolizaRecupera, params);
			HashMap<String, String> someObject = (HashMap<String, String>) JSONUtil.deserialize(respuestaWS);
			mensaje = someObject.get("mensaje");
			if (mensaje != null) {
				return mensaje;
			}
			logger.debug("ENTRO ");
		} catch (Exception ex) {
			mensaje = Utils.manejaExcepcion(ex);
		}
		return mensaje;
	}

	public boolean isNumeric(String str) {
		return (str.matches("[+-]?\\d*(\\.\\d+)?") && str.equals("") == false);
	}

	public Integer[] diferencia(Date fecha1, Date fecha2) {
		Integer[] resultado = new Integer[4];
		DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		String[] aFechaIng = fecha.format(fecha1).split("/");
		Integer diaInicio = Integer.parseInt(aFechaIng[0]);
		Integer mesInicio = Integer.parseInt(aFechaIng[1]);
		Integer anioInicio = Integer.parseInt(aFechaIng[2]);

		String[] aFecha = fecha.format(fecha2).split("/");
		Integer diaActual = Integer.parseInt(aFecha[0]);
		Integer mesActual = Integer.parseInt(aFecha[1]);
		Integer anioActual = Integer.parseInt(aFecha[2]);
		int b = 0;
		int dias = 0;
		int mes = 0;
		int anios = 0;
		int meses = 0;
		mes = mesInicio - 1;
		if (mes == 2) {
			if ((anioActual % 4 == 0) && ((anioActual % 100 != 0) || (anioActual % 400 == 0))) {
				b = 29;
			} else {
				b = 28;
			}
		} else if (mes <= 7) {
			if (mes == 0) {
				b = 31;
			} else if (mes % 2 == 0) {
				b = 30;
			} else {
				b = 31;
			}
		} else if (mes > 7) {
			if (mes % 2 == 0) {
				b = 31;
			} else {
				b = 30;
			}
		}
		if ((anioInicio > anioActual) || (anioInicio == anioActual && mesInicio > mesActual)
				|| (anioInicio == anioActual && mesInicio == mesActual && diaInicio > diaActual)) {
		} else {
			if (mesInicio <= mesActual) {
				anios = anioActual - anioInicio;
				if (diaInicio <= diaActual) {
					meses = mesActual - mesInicio;
					dias = b - (diaInicio - diaActual);
				} else {
					if (mesActual == mesInicio) {
						anios = anios - 1;
					}
					meses = (mesActual - mesInicio - 1 + 12) % 12;
					dias = b - (diaInicio - diaActual);
				}
			} else {
				anios = anioActual - anioInicio - 1;
				if (diaInicio > diaActual) {
					meses = mesActual - mesInicio - 1 + 12;
					dias = b - (diaInicio - diaActual);
				} else {
					meses = mesActual - mesInicio + 12;
					dias = diaActual - diaInicio;
				}
			}
		}
		if (meses == 0 && anios == 0) {
			System.out.println(diaInicio + "---" + diaActual);
			if (diaInicio < diaActual) {
				dias = diaActual - diaInicio;
			} else if (diaInicio == diaActual) {
				dias = 0;
			}
		}
		resultado[0] = dias;
		resultado[1] = meses;
		resultado[2] = anios;
		return resultado;
	}

	public void generaBitacora(Date fecha, String nombre, int polizas, String rango, String usuario) {
		try {
			consultasPolizaDAO.insertaBitacora(fecha, nombre, polizas, rango, usuario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List comparar(String M1, String M2, List<Map<String, String>> listaOriginal, final String... args) {
		Set<Map<String, String>> polizasSet = new TreeSet<Map<String, String>>(new Comparator<Map<String, String>>() {
			public int compare(Map<String, String> m1, Map<String, String> m2) {
				String cadena = "";
				String cadena2 = "";
				for (int i = 0; i < args.length; i++) {
					if (i == 3) {
						cadena += m1.get(args[i]).toString().toUpperCase();
						cadena2 += m2.get(args[i]).toString().toUpperCase();
					} else {
						cadena += m1.get(args[i]);
						cadena2 += m2.get(args[i]);
					}
				}
				logger.debug(cadena + "####" + cadena2);
				return cadena.compareTo(cadena2);
			}
		});
		// Almacenamos los duplicados en una lista:
		String error = "";
		List<String> duplicados = new ArrayList<String>();
		for (Map<String, String> p : listaOriginal) {
			// Guardarla en lista o imprimir el mensaje de duplicidad:
			error = M1;
			if (!polizasSet.add(p)) {
				for (int i = 0; i < args.length; i++) {
					error += p.get(args[i])+" ";
					System.out.println(error);
				}
				error += M2;
				duplicados.add(error);
			}
		}
		return duplicados;
	}

	@Override
	public void insertaBitacora(Date fecha, String nombre, int polizas, String rango, String usuario) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}