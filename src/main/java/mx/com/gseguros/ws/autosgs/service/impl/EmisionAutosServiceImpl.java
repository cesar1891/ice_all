package mx.com.gseguros.ws.autosgs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.consultas.dao.ConsultasPolizaDAO;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasDAOImpl;
import mx.com.gseguros.portal.consultas.dao.impl.ConsultasPolizaDAOImpl;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.Agente;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.BeneficiarioCotizacionDTO;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.Cobertura;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.CodigoPostal;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.ConfiguracionPaquete;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.Cotizacion;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.CotizacionNegocio;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.CotizacionRequest;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.FormasDePago_type0;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.GuardarCotizacionResponse;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.Inciso;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.Paquete;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.PolizaCotizacion;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.SDTClientesSDTClientesItem;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.TipoProducto;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.TipoVehiculo;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.TotalFormaPago;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.Version;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsGuardarCotizacion;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsGuardarCotizacionE;
import mx.com.gseguros.ws.autosgs.cotizacion.client.axis2.CotizacionIndividualWSServiceStub.WsGuardarCotizacionResponseE;
import mx.com.gseguros.ws.autosgs.dao.AutosSIGSDAO;
import mx.com.gseguros.ws.autosgs.emision.client.axis2.WsEmitirPolizaStub;
import mx.com.gseguros.ws.autosgs.emision.client.axis2.WsEmitirPolizaStub.SDTPoliza;
import mx.com.gseguros.ws.autosgs.emision.client.axis2.WsEmitirPolizaStub.WsEmitirPolizaEMITIRPOLIZA;
import mx.com.gseguros.ws.autosgs.emision.model.EmisionAutosVO;
import mx.com.gseguros.ws.autosgs.service.EmisionAutosService;
import mx.com.gseguros.ws.model.WrapperResultadosWS;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementacion de WS de Cotizacion y Emision de Autos
 * @author Hector
 *
 */
@Service
@Qualifier("emisionAutosServiceImpl")
public class EmisionAutosServiceImpl implements EmisionAutosService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EmisionAutosServiceImpl.class);
	
	private static final long WS_TIMEOUT =  3*60*1000; //3 minutos
	private static final long WS_TIMEOUT_EXTENDED = 55*60*1000;// 55 minutos, casi una hora por si hay otros procesos que suman la hora en las JSP
	
	@Value("${ws.cotizacion.autos.url}")
	private String endpointCotiza;
	
	@Value("${ws.emision.autos.url}")
	private String endpointEmite;
	
	@Autowired
	private StoredProceduresManager storedProceduresManager;
	
	@Autowired
	private AutosSIGSDAO autosSIGSDAO;
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
    private CotizacionDAO cotizacionDAO;
	
	@Autowired
	@Qualifier("consultasDAOICEImpl")
    private ConsultasPolizaDAO consultasPolizaDAO;
	
	public EmisionAutosVO cotizaEmiteAutomovilWS(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String ntramite, String cdtipsit, UserVO userVO) throws Exception{
		
		logger.debug(">>>>> Entrando a metodo WS Cotiza y Emite para Auto");
		
		EmisionAutosVO emisionAutoRes = null;
		SDTPoliza polizaEmiRes = null;
		Cotizacion datosCotizacionAuto = null;
		
		boolean exitoRecibosSigs = true;
		
		//if(true)return emisionAutoRes;
		
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("param1" , cdunieco);
		params.put("param2" , cdramo);
		params.put("param3" , estado);
		params.put("param4" , nmpoliza);
		params.put("param5" , nmsuplem);
		
		List<Map<String,String>> listaEndosos = null;
		List<Map<String,String>> listaBeneficiarios = null;
		
		try {
			listaEndosos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_WS_ENDOSO_AUTO.getNombre(), params, null);
		} catch (Exception e2) {
			logger.error("Error al obtener lista de endosos o emision para enviar datos al WS de autos.",e2);
		}
		
//		params.put("param6" , tipopol);
		
		if(listaEndosos!=null && !listaEndosos.isEmpty()){
		
			for(Map<String,String> endosoIt : listaEndosos){
				
				//Se invoca servicio para obtener los datos del auto
				try
				{
					params.put("param6" , endosoIt.get("TIPOEND"));//tipoend
					params.put("param7" , endosoIt.get("NUMEND"));//numend
					
					List<Map<String,String>>lista = null;
					
					if(cdramo.equalsIgnoreCase(Ramo.AUTOS_FRONTERIZOS.getCdramo())){
						
						lista = storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_AUTO.getNombre(), params, null);
						
						try {
							listaBeneficiarios = consultasDAO.obtieneBeneficiariosPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
						} catch (Exception e2) {
							logger.error("Error al obtener lista de beneficiarios para WS de autos.",e2);
							return null;
						}
					}else if(cdramo.equalsIgnoreCase(Ramo.SERVICIO_PUBLICO.getCdramo())){
						
						lista = storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_SRV_PUBLICO.getNombre(), params, null);
					}else if(cdramo.equalsIgnoreCase(Ramo.AUTOS_RESIDENTES.getCdramo())){
						
						lista = storedProceduresManager.procedureListCall(
								ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_RESIDENTES.getNombre(), params, null);
						
						try {
							listaBeneficiarios = consultasDAO.obtieneBeneficiariosPoliza(cdunieco, cdramo, estado, nmpoliza, nmsuplem);
						} catch (Exception e2) {
							logger.error("Error al obtener lista de beneficiarios para WS de autos.",e2);
							return null;
						}
					}
					
					if(lista!=null && lista.size()>0)
					{
						Map<String,String>m=lista.get(0);
						datosCotizacionAuto=new Cotizacion();
						
						datosCotizacionAuto.setNumFolio(Integer.valueOf(m.get("NUMFOLIO")));
						datosCotizacionAuto.setVigencia(Integer.valueOf(m.get("VIGENCIA")));
						datosCotizacionAuto.setIdNegocio(Integer.valueOf(m.get("IDNEGOCIO")));
						datosCotizacionAuto.setIdBanco(Integer.valueOf(m.get("IDBANCO")));
						datosCotizacionAuto.setMesesSinIntereses(Integer.valueOf(m.get("MESESSININTERESES")));
						datosCotizacionAuto.setIdOrigenSolicitud(Integer.valueOf(m.get("IDORIGENSOLICITUD")));
						datosCotizacionAuto.setFinVigencia(Utils.getCalendarServerTimeZone(m.get("FINVIGENCIA"), Constantes.FORMATO_FECHA));
						datosCotizacionAuto.setClaveGS(Integer.valueOf(m.get("CLAVEGS")));
		
						datosCotizacionAuto.setPolizaTracto(m.get("POLIZATRACTO"));
						
						datosCotizacionAuto.setContarPAI(Boolean.valueOf(m.get("CONTARPAI")));
						datosCotizacionAuto.setNombreAlterno(m.get("NOMBREALTERNO"));
						
						CotizacionNegocio cotNeg =  new CotizacionNegocio();
						
						cotNeg.setIdTarifa(Integer.valueOf(m.get("IDTARIFA")));
						cotNeg.setUdi(Double.valueOf(m.get("UDI")));
						cotNeg.setMultianual(Integer.valueOf(m.get("MULTIANUAL")));
						cotNeg.setPeriodoGracia(Integer.valueOf(m.get("PERIODOGRACIA")));
						cotNeg.setFondoEspecial(Double.valueOf(m.get("FONDOESPECIAL")));
						cotNeg.setF1(Double.valueOf(m.get("F1")));
						cotNeg.setF2(Double.valueOf(m.get("F2")));
						cotNeg.setF3(Double.valueOf(m.get("F3")));
						cotNeg.setPorcentajeBono(Double.valueOf(m.get("PORCENTAJEBONO")));
						cotNeg.setIdProveedorUdi(Integer.valueOf(m.get("IDPROVEEDORUDI")));
						cotNeg.setMontoCedido(Double.valueOf(m.get("MONTOCEDIDO")));
						
						cotNeg.setNmsolici(Integer.valueOf(m.get("NMSOLICI")));
						cotNeg.setTipoCotizacion(m.get("TIPOCOTIZACION"));

						cotNeg.setTipoProveedorUdi(m.get("TIPOPROVEEDORUDI"));
						
						datosCotizacionAuto.setCotizacionNegocio(cotNeg);
						
						/**
						 * PARA ENDOSOS
						 */
						PolizaCotizacion polizaCotizacion = new PolizaCotizacion();
						polizaCotizacion.setIdMotivoEndoso(Integer.valueOf(m.get("IDMOTIVOENDOSO")));
						polizaCotizacion.setNumEndoso(Integer.valueOf(m.get("NUMENDOSO")));
						polizaCotizacion.setNumPoliza(Integer.valueOf(m.get("NUMPOLIZA")));
						polizaCotizacion.setRamo(Integer.valueOf(m.get("RAMO")));
						polizaCotizacion.setSucursalEmisora(Integer.valueOf(m.get("SUCURSALEMISORA")));
						
						String tipoEndAuto = StringUtils.isBlank(m.get("TIPOENDOSO"))?" " : m.get("TIPOENDOSO");
						int valTipoEnd = tipoEndAuto.charAt(0);
						logger.debug("Valor Numerico de endoso a enviar: "+ valTipoEnd);
						polizaCotizacion.setTipoEndoso(new org.apache.axis2.databinding.types.UnsignedShort((long)valTipoEnd));
						datosCotizacionAuto.setPolizaCotizacion(polizaCotizacion);
						
						//idagente y sucursal
						Agente agente=new Agente();
						datosCotizacionAuto.setAgente(agente);
						agente.setIdAgente(m.get("IDAGENTE"));
						agente.setSucursal(Integer.valueOf(m.get("SUCURSAL")));
						
						//cve_cli
						SDTClientesSDTClientesItem cliente = new SDTClientesSDTClientesItem();
						datosCotizacionAuto.setCliente(cliente);
						cliente.setCve_cli(Integer.valueOf(m.get("CVE_CLI").substring(m.get("CVE_CLI").length()-10)));
						cliente.setFis_mor("");//null
						cliente.setNom_cli("");//null
						cliente.setApe_pat("");//null
						cliente.setApe_mat("");//null
						cliente.setRaz_soc("");//null
						cliente.setAne_cli("");//null
						cliente.setRfc_cli("");//null
						cliente.setCve_ele("");//null
						cliente.setCurpcli("");//null
						cliente.setCal_cli("");//null
						cliente.setNum_cli("");//null
						cliente.setColonia("");//null
						cliente.setPoblaci("");//null
						cliente.setNac_ext("");//null
						cliente.setTelefo1("");//null
						cliente.setTelefo2("");//null
						cliente.setTelefo3("");//null
						cliente.setCor_ele("");//null
						cliente.setPag_web("");//null
						cliente.setFue_ing("");//null
						cliente.setAdm_con("");//null
						cliente.setCar_pub("");//null
						cliente.setNom_car("");//null
						cliente.setPer_car("");//null
						cliente.setApo_cli("");//null
						cliente.setDom_ori("");//null
						cliente.setNum_pas("");//null
						cliente.setSta_cli("");//null
						
						
						/**
						 * PARA LISTA DE BENEFICIARIOS
						 */
						
						if(listaBeneficiarios!=null && !listaBeneficiarios.isEmpty()){
							
							for(Map<String,String> beneficiarioIt : listaBeneficiarios){
								
								BeneficiarioCotizacionDTO benefAdd=  new BeneficiarioCotizacionDTO();
								
								benefAdd.setNombre(beneficiarioIt.get("NOMBRE"));
								benefAdd.setApePat(beneficiarioIt.get("APEPAT"));
								benefAdd.setApeMat(beneficiarioIt.get("APEMAT"));
								benefAdd.setIdParentesco(Integer.parseInt(beneficiarioIt.get("IDPARENTESCO")));
								benefAdd.setNumCer(Integer.parseInt(beneficiarioIt.get("NUMCER")));
								benefAdd.setPorcentaje(Double.parseDouble(beneficiarioIt.get("PORCENTAJE")));
								benefAdd.setTexto(beneficiarioIt.get("TEXTO"));
								
								datosCotizacionAuto.addBeneficiarios(benefAdd);
							}
						}
						
						datosCotizacionAuto.setFormasDePago(new FormasDePago_type0());//null
						
						//codigo
						CodigoPostal cp=new CodigoPostal();
						datosCotizacionAuto.setCodigoPostal(cp);
						cp.setCodigo(Integer.valueOf(m.get("CODIGO")));
						
						//cveusuariocaptura
						if(userVO != null) logger.info("Clave del Usuario Captura Externo en Sesion: " + userVO.getClaveUsuarioCaptura());
						if(userVO != null && StringUtils.isNotBlank(userVO.getClaveUsuarioCaptura())){
							datosCotizacionAuto.setCveUsuarioCaptura(Integer.valueOf(userVO.getClaveUsuarioCaptura()));
						}else{
							datosCotizacionAuto.setCveUsuarioCaptura(Integer.valueOf(m.get("CVEUSUARIOCAPTURA")));
						}
						logger.info("Clave del Usuario Captura a enviar: "+ datosCotizacionAuto.getCveUsuarioCaptura());
						
						//descuentoagente
						datosCotizacionAuto.setDescuentoAgente(Double.valueOf(m.get("DESCUENTOAGENTE")));
						datosCotizacionAuto.setDescuentoCliente(Double.valueOf(m.get("DESCUENTOCLIENTE")));
						datosCotizacionAuto.setDescuentoFacultamiento(Double.valueOf(m.get("DESCUENTOFACULTAMIENTO")));
						datosCotizacionAuto.setPorcentajeCesionComision(Double.valueOf(m.get("PORCENTAJECESIONCOMISION")));
						
						//idagentecompartido
						int aux=0;
						if(m.get("IDAGENTECOMPARTIDO")!=null)
						{
							aux=Integer.valueOf(m.get("IDAGENTECOMPARTIDO")).intValue();
						}
						datosCotizacionAuto.setIdAgenteCompartido(aux);
						
						//porcencomisionagente2
						aux=0;
						if(m.get("PORCENCOMISIONAGENTE2")!=null)
						{
							aux=Integer.valueOf(m.get("PORCENCOMISIONAGENTE2")).intValue();
						}
						datosCotizacionAuto.setPorcenComisionAgente2(aux);
						
						//idestatuscotizacion
						datosCotizacionAuto.setIdEstatusCotizacion(Integer.valueOf(m.get("IDESTATUSCOTIZACION")));
						
						//tipoproducto
						TipoProducto tipoProducto=TipoProducto.ROJO;
						if(StringUtils.isNotBlank(m.get("TIPOPRODUCTO"))
								&&m.get("TIPOPRODUCTO").equalsIgnoreCase("VERDE"))
						{
							tipoProducto=TipoProducto.VERDE;
						}
						datosCotizacionAuto.setTipoProducto(tipoProducto);
						
						//tipoServicio
						datosCotizacionAuto.setTipoServicio(Integer.valueOf(m.get("TIPOSERVICIO")));
						
						//versiontarifa
						datosCotizacionAuto.setVersionTarifa(Integer.valueOf(m.get("VERSIONTARIFA")));
						
						//inicioVigencia
						datosCotizacionAuto.setInicioVigencia(Utils.getCalendarServerTimeZone(m.get("INICIOVIGENCIA"), Constantes.FORMATO_FECHA));
						
						//moneda
						datosCotizacionAuto.setMoneda(Integer.valueOf(m.get("MONEDA")));
						
						//formapago
						datosCotizacionAuto.setFormaPago(Integer.valueOf(m.get("FORMAPAGO")));
						
						TotalFormaPago totalFormaPago=new TotalFormaPago();
						datosCotizacionAuto.setTotalFormaPago(totalFormaPago);
						
						
						datosCotizacionAuto.setIdDireccion(Integer.valueOf(m.get("IDDIRECCION")));
						datosCotizacionAuto.setRenueva(m.get("RENUEVA"));
						
						//derechopoliza
						totalFormaPago.setDerechoPoliza(Double.valueOf(m.get("DERECHOPOLIZA")));
						
						//iva
						totalFormaPago.setIva(Double.valueOf(m.get("IVA")));
						
						//pagosubsecuente
						totalFormaPago.setPagoSubSecuente(Double.valueOf(m.get("PAGOSUBSECUENTE")));
						
						//primaneta
						totalFormaPago.setPrimaNeta(Double.valueOf(m.get("PRIMANETA")));
						
						//primerpago
						totalFormaPago.setPrimerPago(Double.valueOf(m.get("PRIMERPAGO")));
						
						//recargopagofraccionado
						totalFormaPago.setRecargoPagoFraccionado(Double.valueOf(m.get("RECARGOPAGOFRACCIONADO")));
						
						totalFormaPago.setDerechoAgente(Double.valueOf(m.get("DERECHOAGENTE")));
						totalFormaPago.setDerechoPromotor(Double.valueOf(m.get("DERECHOPROMOTOR")));
						
						Map<String,Integer[]>mapaIndicesIncisos=new HashMap<String,Integer[]>();
						List<Inciso> incisos=new ArrayList<Inciso>();
						
						//encontrar todos los incisos
						{
							int indiceRow    = 0;
							int indiceInciso = 0;
							Inciso nuevoInciso;
							Version nuevoVersion;
							
							for(Map<String,String>row:lista)
							{
								nuevoVersion = new Version();
								nuevoVersion.setAmis(Integer.valueOf(row.get("AMIS")));
								nuevoInciso = new Inciso();
								nuevoInciso.setVersion(nuevoVersion);
								
								String nmsituac=row.get("NMSITUAC");
								if(!mapaIndicesIncisos.containsKey(nmsituac))
								{
									mapaIndicesIncisos.put(nmsituac,new Integer[]{Integer.valueOf(indiceInciso),Integer.valueOf(indiceRow)});
									incisos.add(nuevoInciso);
									indiceInciso=indiceInciso+1;
								}
								indiceRow = indiceRow+1;
							}
						}
						
						Map<String,List<Map<String,String>>>listaRowCoberturaPorInciso=new HashMap<String,List<Map<String,String>>>();
						
						//encontrar todas las coberturas por inciso
						for(Entry<String,Integer[]>indiceInciso:mapaIndicesIncisos.entrySet())
						{
							String nmsituac=indiceInciso.getKey();
							List<Map<String,String>>coberturasDelInciso=new ArrayList<Map<String,String>>();
							for(Map<String,String>row:lista)
							{
								if(row.get("NMSITUAC").equalsIgnoreCase(nmsituac))
								{
									coberturasDelInciso.add(row);
								}
							}
							listaRowCoberturaPorInciso.put(nmsituac,coberturasDelInciso);
						}
						
						//llenar todos los incisos
						for(Entry<String,Integer[]>indiceInciso:mapaIndicesIncisos.entrySet())
						{
							String nmsituac=indiceInciso.getKey();
							int indiceArray;
							int indiceRow;
							{
								Integer[] indices = indiceInciso.getValue();
								indiceArray       = indices[0].intValue();
								indiceRow         = indices[1].intValue();
							}
							Inciso incisoIterado   = incisos.get(indiceArray);
							Map<String,String> row = lista.get(indiceRow);
							
							//idtipovalor
							incisoIterado.setIdTipoValor(Integer.valueOf(row.get("IDTIPOVALOR")));
							
							//menorDeTreinta JTEZVA JTEZVA JTEZVA JTEZVA JTEZVA 1 MAYO 2015
							incisoIterado.setMenorDeTreinta("S".equals(row.get("MENORDETREINTA")));
							
							//modelo
							incisoIterado.setModelo(Integer.valueOf(row.get("MODELO")));
							
							//nummotor
							incisoIterado.setNumMotor(row.get("NUMMOTOR"));
							
							//numserie
							incisoIterado.setNumSerie(row.get("NUMSERIE"));
							
							//numplacas
							incisoIterado.setNumPlacas(row.get("NUMPLACAS"));
							
							//numeconomico
							incisoIterado.setNumEconomico(row.get("NUMECONOMICO"));
							
							//numocupantes
							incisoIterado.setNumOcupantes(Integer.valueOf(row.get("NUMOCUPANTES")));
							
							//beneficiariopreferente
							incisoIterado.setBeneficiarioPref(row.get("BENEFICIARIOPREFERENTE"));
							
							//tipvehica
							incisoIterado.setTipVehiCA(Integer.valueOf(row.get("TIPVEHICA")));
							
							incisoIterado.setIdInciso(Integer.valueOf(row.get("NMSITUAC")));
							
							//tipovehiculo
							TipoVehiculo tipoVehiculo=null;
							
							if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("FRONTERIZO")){
								tipoVehiculo=TipoVehiculo.FRONTERIZO;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("PICKUP")){
								tipoVehiculo=TipoVehiculo.AUTO_PICKUP;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("SERVICIO_PUBLICO_AUTOS")){
								tipoVehiculo=TipoVehiculo.SERVICIO_PUBLICO_AUTOS;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("SERVICIO_PUBLICO_MICROS")){
								tipoVehiculo=TipoVehiculo.SERVICIO_PUBLICO_MICROS;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("AUTO_PICKUP")){
								tipoVehiculo=TipoVehiculo.AUTO_PICKUP;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("CAMION")){
								tipoVehiculo=TipoVehiculo.CAMION;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("MOTO_RESIDENTE")){
								tipoVehiculo=TipoVehiculo.MOTO_RESIDENTE;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("SEMI_INDISTINTO")){
								tipoVehiculo=TipoVehiculo.SEMI_INDISTINTO;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("TRACTOCAMION_ARMADO")){
								tipoVehiculo=TipoVehiculo.TRACTOCAMION_ARMADO;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("AUTO_TURISTA")){
								tipoVehiculo=TipoVehiculo.AUTO_TURISTA;
							}else if(StringUtils.isNotBlank(row.get("TIPOVEHICULO")) && row.get("TIPOVEHICULO").equalsIgnoreCase("LICENCIA_TURISTA")){
								tipoVehiculo=TipoVehiculo.LICENCIA_TURISTA;
							}
							
							incisoIterado.setTipoVehiculo(tipoVehiculo);
							
							//valor
							incisoIterado.setValor(Double.valueOf(row.get("VALOR")));
							
							//conductor
							incisoIterado.setConductor(row.get("CONDUCTOR"));
		
							//Tipo Servicio
							incisoIterado.setIdTipoServicio(Integer.valueOf(row.get("TIPOSERVICIO")));
							
							//TipoUso
							incisoIterado.setTipoUso(Integer.valueOf(row.get("TIPOUSO")));
							
							incisoIterado.setIdTipoCarga(Integer.valueOf(row.get("IDTIPOCARGA")));
							incisoIterado.setAdaptaciones(row.get("ADAPTACIONES"));
							incisoIterado.setEquipoEspecial(row.get("EQUIPOESPECIAL"));
							incisoIterado.setSituacionRiesgo(row.get("SITUACIONRIESGO"));
							
							/**
							 * TODO: Poner validacion de WS para setNumSerieValido
							 */
							incisoIterado.setNumSerieValido(Boolean.valueOf(row.get("NUMSERIEVALIDO")));
							
							incisoIterado.setAseguradoAlterno(row.get("ASEGURADOALTERNO"));
							
							//version
							Version version=new Version();
							version.setDescripcion(row.get("DESCRIPCION"));
							version.setAmis(Integer.valueOf(row.get("AMIS")));
							incisoIterado.setVersion(version);
							
							ConfiguracionPaquete confPaq=new ConfiguracionPaquete();
							incisoIterado.setConfiguracionPaquete(confPaq);
							
							
							incisoIterado.setPrimaNeta(Double.valueOf(row.get("PRIMANETAINC")));
							
							
							incisoIterado.setCilindraje(row.get("CILINDRAJE"));
							incisoIterado.setDescuentoInciso(Double.valueOf(row.get("DESCUENTOINCISO")));
							
							incisoIterado.setFechaFactura(Utils.getCalendarServerTimeZone(row.get("FECHAFACTURA"), Constantes.FORMATO_FECHA));
							
							incisoIterado.setSaRcvDias(Integer.valueOf(row.get("SARCVDIAS")));
							
							//versionTarifa
							confPaq.setVersionTarifa(Integer.valueOf(row.get("VERSIONTARIFAINC")));
							
							List<Paquete> paquetesIncisoIterado = new ArrayList<Paquete>();
							Paquete paqueteIncisoIterado        = new Paquete();
							paquetesIncisoIterado.add(paqueteIncisoIterado);
							
							//idconfiguracionpaquete
							paqueteIncisoIterado.setIdConfiguracionPaquete(Integer.valueOf(row.get("IDCONFIGURACIONPAQUETE")));
							
							//seleccionado
							boolean seleccionado = false;
							if(StringUtils.isNotBlank(row.get("SELECCIONADO"))
									&&row.get("SELECCIONADO").equalsIgnoreCase("TRUE"))
							{
								seleccionado = true;
							}
							paqueteIncisoIterado.setSeleccionado(seleccionado);
							
							List<Map<String,String>>listaCoberturasIncisoIterado=listaRowCoberturaPorInciso.get(nmsituac);
							
							List<Cobertura>coberturasIncisoIterado=new ArrayList<Cobertura>();
							
							////// iterar cada cobertura //////
							for(Map<String,String>rowCoberturaIncisoIterado:listaCoberturasIncisoIterado)
							{
								Cobertura coberturaIteradaPaqueteIncisoIterado=new Cobertura();
								
								//idcobertura
								coberturaIteradaPaqueteIncisoIterado.setIdCobertura(Integer.parseInt(rowCoberturaIncisoIterado.get("IDCOBERTURA")));
								
								//seleccionadocob
								boolean seleccionadoCob = false;
								if(StringUtils.isNotBlank(rowCoberturaIncisoIterado.get("SELECCIONADOCOB"))
										&&rowCoberturaIncisoIterado.get("SELECCIONADOCOB").equalsIgnoreCase("TRUE"))
								{
									seleccionadoCob = true;
								}
								coberturaIteradaPaqueteIncisoIterado.setSeleccionado(seleccionadoCob);
								
								//deducible
								coberturaIteradaPaqueteIncisoIterado.setDeducible(Double.valueOf(rowCoberturaIncisoIterado.get("DEDUCIBLE")));
								
								//suma_asegurada
								coberturaIteradaPaqueteIncisoIterado.setSuma_asegurada(Double.valueOf(rowCoberturaIncisoIterado.get("SUMA_ASEGURADA")));
								
								//prima_bruta
								coberturaIteradaPaqueteIncisoIterado.setPrima_bruta(Double.valueOf(rowCoberturaIncisoIterado.get("PRIMA_BRUTA")));
								
								//prima_netacob
								coberturaIteradaPaqueteIncisoIterado.setPrima_neta(Double.valueOf(rowCoberturaIncisoIterado.get("PRIMA_NETACOB")));
								
								//comision
								coberturaIteradaPaqueteIncisoIterado.setComision(Double.valueOf(rowCoberturaIncisoIterado.get("COMISION")));
		
								coberturasIncisoIterado.add(coberturaIteradaPaqueteIncisoIterado);
							}
							
							Cobertura[] listaCoberturasPaqueteAux=new Cobertura[coberturasIncisoIterado.size()];
							for(int i=0;i<listaCoberturasPaqueteAux.length;i++)
							{
								listaCoberturasPaqueteAux[i]=coberturasIncisoIterado.get(i);
							}
							paqueteIncisoIterado.setCoberturas(listaCoberturasPaqueteAux);
							
							Paquete[]listaPaquetesIncisoIteradoAux=new Paquete[paquetesIncisoIterado.size()];
							for(int i=0;i<listaPaquetesIncisoIteradoAux.length;i++)
							{
								listaPaquetesIncisoIteradoAux[i]=paquetesIncisoIterado.get(i);
							}
							confPaq.setPaquetes(listaPaquetesIncisoIteradoAux);
						}
						
						Inciso[]incisosCotizacionAux=new Inciso[incisos.size()];
						for(int i=0;i<incisosCotizacionAux.length;i++)
						{
							incisosCotizacionAux[i]=incisos.get(i);
						}
						datosCotizacionAuto.setIncisos(incisosCotizacionAux);
						
					}
				} catch (Exception e1) {
					logger.error("Error en obtencion y mapeo de datos para envio de Emision WS Autos",e1);
					return null;
				}	
				
				if(datosCotizacionAuto != null){
					try{
						WrapperResultadosWS resultWSCot = this.ejecutaCotizacionAutosWS(datosCotizacionAuto);
								
						GuardarCotizacionResponse cotRes = (GuardarCotizacionResponse) resultWSCot.getResultadoWS();
						
						if(cotRes != null && cotRes.getExito()){
							logger.debug("Respuesta de WS Cotizacion Codigo(): " +cotRes.getCodigo());
							logger.debug("Respuesta de WS Cotizacion Mensaje(): " +cotRes.getMensaje());
							
							long numSolicitud = cotRes.getCotizacion().getIdCotizacion();
							logger.debug("Numero de Cotizacion Generada: " + numSolicitud);
							
							WrapperResultadosWS resultWSEmi = this.ejecutaEmisionAutosWS(numSolicitud);
							
							if(resultWSEmi != null && resultWSEmi.getResultadoWS() != null){
								polizaEmiRes = (SDTPoliza)resultWSEmi.getResultadoWS();
								
								if(polizaEmiRes.getNumpol() == 0){
									logger.error("Numero de Poliza de Emision en 0 para la Cotizacion: " + numSolicitud);
									if(!endosoIt.get("TIPOEND").equalsIgnoreCase("E")){
										LinkedHashMap<String, Object> paramsEnd = new LinkedHashMap<String, Object>();
									
										paramsEnd.put("param1" , cdunieco);
										paramsEnd.put("param2" , cdramo);
										paramsEnd.put("param3" , estado);
										paramsEnd.put("param4" , nmpoliza);
										paramsEnd.put("param5" , nmsuplem);
										paramsEnd.put("param6" , endosoIt.get("TIPOEND"));
										paramsEnd.put("param7" , endosoIt.get("NUMEND"));
										paramsEnd.put("param8" , 0);
										
										try {
											storedProceduresManager.procedureVoidCall(
													ObjetoBD.ACTUALIZA_ENDOSO_SIGS.getNombre(), paramsEnd, null);
										} catch (Exception e2) {
											logger.error("Error al actualizar el numero de endoso.",e2);
										}
									}
									return null;
									
								}
								
								emisionAutoRes = new EmisionAutosVO();
								emisionAutoRes.setNmpoliex(Long.toString(polizaEmiRes.getNumpol()));
								emisionAutoRes.setSubramo(Short.toString(polizaEmiRes.getRamos()));
								emisionAutoRes.setSucursal(Short.toString(polizaEmiRes.getSucursal()));
								emisionAutoRes.setNumeroEndoso(Long.toString(polizaEmiRes.getEndoso()));
								emisionAutoRes.setTipoEndoso(polizaEmiRes.getTipendo());
								
								if(!endosoIt.get("TIPOEND").equalsIgnoreCase("E")){
									LinkedHashMap<String, Object> paramsEnd = new LinkedHashMap<String, Object>();
								
									paramsEnd.put("param1" , cdunieco);
									paramsEnd.put("param2" , cdramo);
									paramsEnd.put("param3" , estado);
									paramsEnd.put("param4" , nmpoliza);
									paramsEnd.put("param5" , nmsuplem);
									paramsEnd.put("param6" , endosoIt.get("TIPOEND"));
									paramsEnd.put("param7" , endosoIt.get("NUMEND"));
									paramsEnd.put("param8" , polizaEmiRes.getEndoso());
									
									try {
										storedProceduresManager.procedureVoidCall(
												ObjetoBD.ACTUALIZA_ENDOSO_SIGS.getNombre(), paramsEnd, null);
									} catch (Exception e2) {
										logger.error("Error al actualizar el numero de endoso.",e2);
									}
								}
									
							}
							
						}else{
							logger.error("Error en la cotizacion de Autos WS, respuesta no exitosa");
							return null;
						}
						
					} catch(WSException wse){
						logger.error("Error en WS de Autos, xml enviado: " + wse.getPayload(), wse);
						if(!endosoIt.get("TIPOEND").equalsIgnoreCase("E")){
							LinkedHashMap<String, Object> paramsEnd = new LinkedHashMap<String, Object>();
						
							paramsEnd.put("param1" , cdunieco);
							paramsEnd.put("param2" , cdramo);
							paramsEnd.put("param3" , estado);
							paramsEnd.put("param4" , nmpoliza);
							paramsEnd.put("param5" , nmsuplem);
							paramsEnd.put("param6" , endosoIt.get("TIPOEND"));
							paramsEnd.put("param7" , endosoIt.get("NUMEND"));
							paramsEnd.put("param8" , 0);
							
							try {
								storedProceduresManager.procedureVoidCall(
										ObjetoBD.ACTUALIZA_ENDOSO_SIGS.getNombre(), paramsEnd, null);
							} catch (Exception e2) {
								logger.error("Error al actualizar el numero de endoso para la poliza: "+ nmpoliza,e2);
							}
						}
						return null;
					} catch (Exception e){
						logger.error("Error en WS de Autos: " + e.getMessage(),e);
						if(!endosoIt.get("TIPOEND").equalsIgnoreCase("E")){
							LinkedHashMap<String, Object> paramsEnd = new LinkedHashMap<String, Object>();
						
							paramsEnd.put("param1" , cdunieco);
							paramsEnd.put("param2" , cdramo);
							paramsEnd.put("param3" , estado);
							paramsEnd.put("param4" , nmpoliza);
							paramsEnd.put("param5" , nmsuplem);
							paramsEnd.put("param6" , endosoIt.get("TIPOEND"));
							paramsEnd.put("param7" , endosoIt.get("NUMEND"));
							paramsEnd.put("param8" , 0);
							
							try {
								storedProceduresManager.procedureVoidCall(
										ObjetoBD.ACTUALIZA_ENDOSO_SIGS.getNombre(), paramsEnd, null);
							} catch (Exception e2) {
								logger.error("Error al actualizar el numero de endoso para la poliza: " + nmpoliza,e2);
							}
						}
						return null;
					}
				}else{
					
					if(cdramo.equalsIgnoreCase(Ramo.AUTOS_FRONTERIZOS.getCdramo())){
						logger.error("Error, No se encontraron datos a enviar para el WebService de Cotizacion-Emision/Endosos Autos");
						logger.error("SP: " + ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_AUTO.getNombre());
						logger.error("Parametros: " + params);
					}else if(cdramo.equalsIgnoreCase(Ramo.SERVICIO_PUBLICO.getCdramo())){
						logger.error("Error, No se encontraron datos a enviar para el WebService de Cotizacion-Emision/Endosos Autos");
						logger.error("SP: " + ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_SRV_PUBLICO.getNombre());
						logger.error("Parametros: " + params);
					}else if(cdramo.equalsIgnoreCase(Ramo.AUTOS_RESIDENTES.getCdramo())){
						logger.error("Error, No se encontraron datos a enviar para el WebService de Cotizacion-Emision/Endosos Autos");
						logger.error("SP: " + ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_RESIDENTES.getNombre());
						logger.error("Parametros: " + params);
					} else {
						logger.error("Error, No se tienen datos del Auto");
					}
					
					return null;
				}
		
		
			}
			
			Integer valida = enviaRecibosAutosSigs(cdunieco, cdramo,estado, nmpoliza, nmsuplem, emisionAutoRes.getNmpoliex(), emisionAutoRes.getSubramo(), emisionAutoRes.getSucursal());
			
			if(valida == null || valida != 0){
				exitoRecibosSigs = false;
			}
			
			if(!exitoRecibosSigs){
				emisionAutoRes.setExitoRecibos(false);
				emisionAutoRes.setResRecibos((valida == null) ?9999 : valida);
				logger.error("Error al Ejecutar la emision de la poliza de autos: "+nmpoliza+" valida:"+ valida);
			}else{
				emisionAutoRes.setExitoRecibos(true);
				emisionAutoRes.setResRecibos(valida);
			}
			
			
			
			
			Integer res=integraDxnAutos( cdunieco,  cdramo,
		             estado,  nmpoliza,
		             emisionAutoRes.getNmpoliex(),
		             emisionAutoRes.getSubramo(),
		             emisionAutoRes.getSucursal());
			
			
			
			logger.debug(Utils.log("Respuesta spIntegraDxN_sigs : ",res));
			if(res==null || res!=0){
			    return null;
			}
			
			
			
			
		}else if(listaEndosos!=null && listaEndosos.isEmpty()){
			
			/**
			 * Para cuando se envia una retarificacion, y a final de cuenta no se hace, no se generan endosos resultantes del SP, se considera un endoso B
			 */
			emisionAutoRes = new EmisionAutosVO();
			emisionAutoRes.setEndosoSinRetarif(true);
		}
		
		return emisionAutoRes;
	}
	
	public List<Map<String,String>> obtieneEndososImprimir(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem){
		
		logger.debug(">>>>> Entrando a metodo Obtiene Endosos a Imprimir");
		
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("param1" , cdunieco);
		params.put("param2" , cdramo);
		params.put("param3" , estado);
		params.put("param4" , nmpoliza);
		params.put("param5" , nmsuplem);
		
		List<Map<String,String>> listaEndosos = null;
		
		try {
			listaEndosos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_IMP_WS_ENDOSO_AUTO.getNombre(), params, null);
		} catch (Exception e2) {
			logger.error("Error al obtener lista de endosos o emision a Imprimir para WS de autos.",e2);
		}
		
		return listaEndosos;
	}
	
	private WrapperResultadosWS ejecutaCotizacionAutosWS(Cotizacion datosCotizacionAuto) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		CotizacionIndividualWSServiceStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpointCotiza));
			stubGS = new CotizacionIndividualWSServiceStub(endpointCotiza);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT_EXTENDED);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		
		CotizacionRequest cotizacionRequest = new CotizacionRequest();
		cotizacionRequest.setCodigo(0);
		cotizacionRequest.setCotizacion(datosCotizacionAuto);
		
		WsGuardarCotizacion wsGuardarCotizacion =  new WsGuardarCotizacion();
		wsGuardarCotizacion.setArg0(cotizacionRequest);
		
		WsGuardarCotizacionE wsGuardarCotizacionE = new WsGuardarCotizacionE();
		wsGuardarCotizacionE.setWsGuardarCotizacion(wsGuardarCotizacion);
		
		WsGuardarCotizacionResponseE wsGuardarCotizacionResponseE = null;
		GuardarCotizacionResponse responseCot = null;
		
		try {
			wsGuardarCotizacionResponseE = stubGS.wsGuardarCotizacion(wsGuardarCotizacionE);
			responseCot = wsGuardarCotizacionResponseE.getWsGuardarCotizacionResponse().get_return();
			resultWS.setResultadoWS(responseCot);
			resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
			logger.debug("Xml enviado para obtener Cotizacion de auto: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
		} catch (Exception re) {
			throw new WSException("Error de conexion Cotizacion Autos: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resultWS;
	}
	
	private WrapperResultadosWS ejecutaEmisionAutosWS(long numSolicitud) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		WsEmitirPolizaStub stubGS = null;
		
		try {
			logger.info(new StringBuffer("endpoint a invocar=").append(endpointEmite));
			stubGS = new WsEmitirPolizaStub(endpointEmite);
			stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		} catch (AxisFault e) {
			logger.error(e);
			throw new Exception("Error de preparacion de Axis2: " + e.getMessage());
		}
		
		
		WsEmitirPolizaEMITIRPOLIZA wsEmitirPolizaEMITIRPOLIZA = new WsEmitirPolizaEMITIRPOLIZA();
		wsEmitirPolizaEMITIRPOLIZA.setVnumsolicitud(numSolicitud);
		
		OMElement wsEmitirResponse = null;
		SDTPoliza resultadoWS = null; 
		String polRes = null;
		
		try {
			wsEmitirResponse = stubGS.eMITIRPOLIZA(wsEmitirPolizaEMITIRPOLIZA);
			
			logger.debug("XML Respuesta emision WS: " +  wsEmitirResponse);
			
			OMElement sdtPol = wsEmitirResponse.getFirstChildWithName(new QName("KB_WSEmisionPoliza", "Sdtpoliza")); 
			polRes =  sdtPol.getFirstChildWithName(new QName("KB_WSEmisionPoliza","numpol")).getText();
				
			if(StringUtils.isNotBlank(polRes)){

				resultadoWS =  new SDTPoliza();
				resultadoWS.setEndoso(Long.parseLong(sdtPol.getFirstChildWithName(new QName("KB_WSEmisionPoliza","endoso")).getText()));
				resultadoWS.setNumpol(Long.parseLong(sdtPol.getFirstChildWithName(new QName("KB_WSEmisionPoliza","numpol")).getText()));
				resultadoWS.setRamos(Short.parseShort(sdtPol.getFirstChildWithName(new QName("KB_WSEmisionPoliza","ramos")).getText()));
				resultadoWS.setSucursal(Short.parseShort(sdtPol.getFirstChildWithName(new QName("KB_WSEmisionPoliza","sucursal")).getText()));
				resultadoWS.setTipendo(sdtPol.getFirstChildWithName(new QName("KB_WSEmisionPoliza","tipendo")).getText());
				
				resultWS.setResultadoWS(resultadoWS);
				resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());

			}
			logger.debug("Xml enviado para emitir poliza de auto: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			
		} catch (Exception re) {
			throw new WSException("Error del llamado al WS Emision Autos: " + re.getMessage(), re, stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
		}
		
		return resultWS;
	}
	
	
	public Integer enviaRecibosAutosSigs(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmpoliex, String subramo, String sucursal) throws Exception{
		
		logger.debug(">>>>> Entrando a metodo WS Envia Recibos para Auto");
		
		Integer valida = null;
		Integer errorEjec  = new Integer(99999);
		List<Map<String,String>> recibos = null;
		
		//Se invoca servicio para obtener los datos del recibos
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
//			params.put("param6" , tipopol);
			
			recibos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_RECIBOS_AUTOS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Emision WS Autos",e1);
			return errorEjec;
		}	
		
		if(recibos != null && !recibos.isEmpty()){
			
			for(Map<String,String> reciboIt : recibos){
				try{
					String fechaInicio = reciboIt.get("FECINI");
					String fechaTermino = reciboIt.get("FECTER");
					
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("Sucursal"        , sucursal);
					params.put("Ramo"            , subramo);
					params.put("Poliza"          , nmpoliex);
					params.put("TipoEndoso"      , StringUtils.isBlank(reciboIt.get("TIPEND"))?" " : reciboIt.get("TIPEND"));
					params.put("NumeroEndoso"    , reciboIt.get("NUMEND"));
					params.put("Recibo"          , reciboIt.get("NUMREC"));
					params.put("TotalRecibos"    , reciboIt.get("TOTALREC"));
					params.put("PrimaNeta"       , reciboIt.get("PRIMA"));
					params.put("Iva"             , reciboIt.get("IVA"));
					params.put("Recargo"         , reciboIt.get("RECARGOS"));
					params.put("Derechos"        , reciboIt.get("DERECHOS"));
					params.put("CesionComision"  , reciboIt.get("CESIONCOM"));
					params.put("ComisionPrima"   , reciboIt.get("COMISIONPRIMA"));
					params.put("ComisionRecargo" , reciboIt.get("COMISIONRECARGO"));
					params.put("FechaInicio"     , fechaInicio);
					params.put("FechaTermino"    , fechaTermino);
					params.put("Modo"            , reciboIt.get("MODO"));
					params.put("Estatus"         , reciboIt.get("ESTATUS"));
					
					Integer res = autosSIGSDAO.insertaReciboAuto(params);
					
					logger.debug("Respuesta al insertar recibo: " + reciboIt.get("NUMREC")+ " - "+res);
					
					if(res == null || res != 0){
						logger.debug("Recibo no exitoso, retornando false");
						//return false;
					}
					
				} catch (Exception e){
					logger.error("Error en Envio Recibo Auto: " + e.getMessage(),e);
					//return false;
				}
				//break;
			}
			
			try{
				
				LinkedHashMap<String, Object> paramsEnd = new LinkedHashMap<String, Object>();
				paramsEnd.put("param1" , cdunieco);
				paramsEnd.put("param2" , cdramo);
				paramsEnd.put("param3" , estado);
				paramsEnd.put("param4" , nmpoliza);
				paramsEnd.put("param5" , nmsuplem);
				
				List<Map<String,String>> listaEndosos = null;
				
				try {
					listaEndosos = storedProceduresManager.procedureListCall(
							ObjetoBD.OBTIENE_DATOS_WS_ENDOSO_AUTO.getNombre(), paramsEnd, null);
				} catch (Exception e2) {
					logger.error("Error al obtener lista de endosos o emision para confirmaRecibosAuto y ejecutaVidaPorRecibo.",e2);
				}
				
				if(listaEndosos != null && !listaEndosos.isEmpty()){
					
					for(Map<String,String> endosoIt : listaEndosos){
						
						String tipoEndoso =  " ";
						
						if(StringUtils.isNotBlank(endosoIt.get("TIPOEND")) && !"E".equalsIgnoreCase(endosoIt.get("TIPOEND"))){
							tipoEndoso = endosoIt.get("TIPOEND");
						}
							
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("Sucursal"    , sucursal);
						params.put("Ramo"        , subramo);
						params.put("Poliza"      , nmpoliex);
						params.put("TipoEndoso"  , tipoEndoso);
						params.put("NumeroEndoso", endosoIt.get("NUMEND"));
						
						valida = autosSIGSDAO.confirmaRecibosAuto(params);
						logger.debug("Respuesta al validar recibos y emision para el Endoso: "+endosoIt.get("NUMEND")+" Tipo: "+endosoIt.get("TIPOEND")+". :: Respuesta: "+valida);
						
						if(valida == null || valida != 0){
							logger.error("Error en la validacion de envio de recibos a SIGS, No se han enviado correctamente la emision para el Endoso: "+endosoIt.get("NUMEND")+" Tipo: "+endosoIt.get("TIPOEND")+".");
							return valida;
						}else{
							logger.info("Envio de Recibos de Auto a SIGS realizado correctamente... Llamando spVidaxRecibo...");
							valida = autosSIGSDAO.ejecutaVidaPorRecibo(params);
							
							if(valida == null || valida != 0){
								logger.error("Error al llamado de spVidaxRecibo SIGS, No se ejecuto el proceso correctamente para el Endoso: "+endosoIt.get("NUMEND")+" Tipo: "+endosoIt.get("TIPOEND")+". Respuesta: "+valida);
								return valida;
							}else{
								logger.info("Llamado de spVidaxRecibo SIGS realizado correctamente...");
							}
						}
					}
				}else{
					logger.warn("Aviso, No se tienen datos de Endosos Autos para confirmaRecibosAuto y ejecutaVidaPorRecibo");
					return errorEjec;
				}
				
			} catch (Exception e){
				logger.error("Error en validacion de Emision Exitosa y VidaPorRecibo! " + e.getMessage(),e);
				throw e;
//				return errorEjec;
			}
		}else{
			logger.warn("Aviso, No se tienen datos de Recibos Autos");
			return errorEjec;
		}
		
		return valida;
	}
	
	public Integer integraDxnAutos(String cdunieco, String cdramo,
            String estado, String nmpoliza,String nmpoliext,String ramo,String cduniex){
	    
	    Map<String,String> datos=null;
	    
	    Integer respuesta=null;
	    Map<String,String> datosEnviar=new HashMap<String, String>();
	    
	    try {
	        
	        if(consultasDAO.esProductoSalud(cdramo)){
	            return 0;
	        }
            datos=cotizacionDAO.cargarTvalopol(cdunieco, cdramo, estado, nmpoliza);
            logger.debug(Utils.log("Datos tvalopol: ",datos));
            if(datos.get("parametros.pv_otvalor08")==null || datos.get("parametros.pv_otvalor08").trim().equals("") || datos.get("parametros.pv_otvalor08").trim().equals("-1")){
                return 0;
            }
            
        
              
        } catch (Exception e) {
            
            logger.error("Error recuperando los datos de la poliza");
            logger.error(e);
        }
	    
    	    
	        String ret=datos.get("parametros.pv_otvalor09");
	        ret=ret.substring(4);
	        String clave_des=datos.get("parametros.pv_otvalor15");
	        clave_des=clave_des.substring(10).trim();
	        
    	    datosEnviar.put("vSucursal", cduniex);
    	    datosEnviar.put("vRamo", ramo);
    	    datosEnviar.put("vPoliza", nmpoliext);
    	    datosEnviar.put("vAdministradora", datos.get("parametros.pv_otvalor08"));
    	    datosEnviar.put("vRetenedora", ret);
    	    datosEnviar.put("vClaveDescuento", clave_des);
    	    datosEnviar.put("vClaveEmpleado", datos.get("parametros.pv_otvalor10"));
    	    datosEnviar.put("vNombreEmpleado", datos.get("parametros.pv_otvalor11"));
    	    datosEnviar.put("vPaternoEmpleado", datos.get("parametros.pv_otvalor12"));
    	    datosEnviar.put("vMaternoEmpleado", datos.get("parametros.pv_otvalor13"));
    	    datosEnviar.put("vRFCEmpleado", datos.get("parametros.pv_otvalor14"));
    	    datosEnviar.put("vCurpEmpleado", datos.get("parametros.pv_otvalor16"));
    	    datosEnviar.put("vAnexo1", "");
    	    datosEnviar.put("vAnexo2", "");
    	    datosEnviar.put("vAnexo3", "");
    	    datosEnviar.put("vAnexo4", "");
    	    
	    
	    try {
            respuesta=autosSIGSDAO.integraDxnAutos(datosEnviar);
        } catch (Exception e) {
            logger.error("Error al enviar datos dxn {}",e);
            
            return null;
            
        }
	    
	    
	    
	    
	    return respuesta;
	}
	
	public int endosoCambioNombreClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem){
		
		logger.debug(">>>>> Entrando a metodo Cambio nombre cliente.");
		
		int numeroEndosoRes = 0;
		List<Map<String,String>> datos = null;
		
		//Se invoca servicio para obtener los datos del CAMBIO DOMICIL
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			datos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_END_NOM_CLI_SIGS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Cambio nombre cliente.",e1);
			return 0;
		}	
		
		if(datos != null && !datos.isEmpty()){
			Map<String,String> datosEnd = datos.get(0);
			try{
				
				HashMap<String, Object> paramsEnd = new HashMap<String, Object>();
				paramsEnd.put("vIdMotivo"  , datosEnd.get("IDMOTIVO"));
				paramsEnd.put("vSucursal"  , datosEnd.get("SUCURSAL"));
				paramsEnd.put("vRamo"      , datosEnd.get("RAMO"));
				paramsEnd.put("vPoliza"    , datosEnd.get("POLIZA"));
				paramsEnd.put("vNombre"    , datosEnd.get("NOMBRE"));
				paramsEnd.put("vAPaterno"   , datosEnd.get("APATERNO"));
				paramsEnd.put("vAMaterno"    , datosEnd.get("AMATERNO"));
				paramsEnd.put("vRasonSocial" , datosEnd.get("RASONSOCIAL"));
				paramsEnd.put("vFEndoso"   , datosEnd.get("FENDOSO"));
				
				Integer res = autosSIGSDAO.endosoNombreCliente(paramsEnd);
				
				logger.debug("Respuesta de Cambio nombre cliente, numero de endoso: " + res);
				
				if(res == null || res == 0){
					logger.debug("Endoso Cambio nombre cliente no exitoso");
				}else{
					numeroEndosoRes = res.intValue();
					
					try{
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("param1" , cdunieco);
						params.put("param2" , cdramo);
						params.put("param3" , estado);
						params.put("param4" , nmpoliza);
						params.put("param5" , nmsuplem);
						params.put("param6" , numeroEndosoRes);
						
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.ACTUALIZA_ENDB_DE_SIGS.getNombre(), params, null);
						
						
					} catch (Exception e1) {
						logger.error("Error en llamar al PL de obtencion de datos para Cambio nombre cliente para SIGS",e1);
						return 0;
					}	
				}
				
			} catch (Exception e){
				logger.error("Error en Cambio nombre cliente Auto: " + e.getMessage(),e);
			}
			
		}else{
			logger.warn("Aviso, No se tienen datos de Cambio nombre cliente.");
			return 0;
		}
		
		return numeroEndosoRes;
	}

	public int endosoCambioRfcClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem){
		
		logger.debug(">>>>> Entrando a metodo Cambio Rfc cliente.");
		
		int numeroEndosoRes = 0;
		List<Map<String,String>> datos = null;
		
		//Se invoca servicio para obtener los datos del CAMBIO DOMICIL
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			datos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_END_RFC_SIGS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Cambio Rfc cliente.",e1);
			return 0;
		}	
		
		if(datos != null && !datos.isEmpty()){
			Map<String,String> datosEnd = datos.get(0);
			try{
				
				HashMap<String, Object> paramsEnd = new HashMap<String, Object>();
				paramsEnd.put("vIdMotivo", datosEnd.get("IDMOTIVO"));
				paramsEnd.put("vSucursal", datosEnd.get("SUCURSAL"));
				paramsEnd.put("vRamo"    , datosEnd.get("RAMO"));
				paramsEnd.put("vPoliza"  , datosEnd.get("POLIZA"));
				paramsEnd.put("vRFC"     , datosEnd.get("RFC"));
				paramsEnd.put("vFEndoso" , datosEnd.get("FENDOSO"));
				
				Integer res = autosSIGSDAO.endosoRfcCliente(paramsEnd);
				
				logger.debug("Respuesta de Cambio Rfc cliente, numero de endoso: " + res);
				
				if(res == null || res == 0){
					logger.debug("Endoso Cambio Rfc cliente no exitoso");
				}else{
					numeroEndosoRes = res.intValue();
					
					try{
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("param1" , cdunieco);
						params.put("param2" , cdramo);
						params.put("param3" , estado);
						params.put("param4" , nmpoliza);
						params.put("param5" , nmsuplem);
						params.put("param6" , numeroEndosoRes);
						
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.ACTUALIZA_ENDB_DE_SIGS.getNombre(), params, null);
						
						
					} catch (Exception e1) {
						logger.error("Error en llamar al PL de obtencion de datos para Cambio Rfc cliente para SIGS",e1);
						return 0;
					}	
				}
				
			} catch (Exception e){
				logger.error("Error en Envio Cambio Rfc cliente Auto: " + e.getMessage(),e);
			}
			
		}else{
			logger.warn("Aviso, No se tienen datos de Cambio Rfc cliente.");
			return 0;
		}
		
		return numeroEndosoRes;
	}

	public int endosoCambioClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem){
		
		logger.debug(">>>>> Entrando a metodo Cambio cliente.");
		
		int numeroEndosoRes = 0;
		List<Map<String,String>> datos = null;
		
		//Se invoca servicio para obtener los datos del CAMBIO DOMICIL
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			datos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_END_NUM_CLI_SIGS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Cambio cliente.",e1);
			return 0;
		}	
		
		if(datos != null && !datos.isEmpty()){
			Map<String,String> datosEnd = datos.get(0);
			try{
				
				HashMap<String, Object> paramsEnd = new HashMap<String, Object>();
				paramsEnd.put("vIdMotivo"  , datosEnd.get("IDMOTIVO"));
				paramsEnd.put("vSucursal"  , datosEnd.get("SUCURSAL"));
				paramsEnd.put("vRamo"      , datosEnd.get("RAMO"));
				paramsEnd.put("vPoliza"    , datosEnd.get("POLIZA"));
				paramsEnd.put("vCveCliente"    , datosEnd.get("CVECLIENTE"));
				paramsEnd.put("vFEndoso"   , datosEnd.get("FENDOSO"));
				
				Integer res = autosSIGSDAO.endosoCambioCliente(paramsEnd);
				
				logger.debug("Respuesta de Cambio cliente, numero de endoso: " + res);
				
				if(res == null || res == 0){
					logger.debug("Endoso Cambio cliente no exitoso");
				}else{
					numeroEndosoRes = res.intValue();
					
					try{
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("param1" , cdunieco);
						params.put("param2" , cdramo);
						params.put("param3" , estado);
						params.put("param4" , nmpoliza);
						params.put("param5" , nmsuplem);
						params.put("param6" , numeroEndosoRes);
						
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.ACTUALIZA_ENDB_DE_SIGS.getNombre(), params, null);
						
						
					} catch (Exception e1) {
						logger.error("Error en llamar al PL de obtencion de datos para Cambio cliente para SIGS",e1);
						return 0;
					}	
				}
				
			} catch (Exception e){
				logger.error("Error en Envio  Cambio cliente Auto: " + e.getMessage(),e);
			}
			
		}else{
			logger.warn("Aviso, No se tienen datos de Cambio cliente.");
			return 0;
		}
		
		return numeroEndosoRes;
	}
	
	
	public int endosoCambioDomicil(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion){
		
		logger.debug(">>>>> Entrando a metodo Cambio Domicilio contratante Auto Sin Modificacion de Codigo Postal");
		
		int numeroEndosoRes = 0;
		List<Map<String,String>> datos = null;
		
		//Se invoca servicio para obtener los datos del CAMBIO DOMICIL
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			datos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_END_DOM_SIGS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Cambio Domicil para SIGS",e1);
			return 0;
		}	
		
		if(datos != null && !datos.isEmpty()){
			Map<String,String> datosEnd = datos.get(0);
			try{
				
				String usuarioCaptura =  null;
				
				if(usuarioSesion!=null){
					if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
						usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
					}else{
						usuarioCaptura = usuarioSesion.getCodigoPersona();
					}
					
				}
				
				HashMap<String, Object> paramsEnd = new HashMap<String, Object>();
				paramsEnd.put("vSucursal"  , datosEnd.get("SUCURSAL"));
				paramsEnd.put("vRamo"      , datosEnd.get("RAMO"));
				paramsEnd.put("vPoliza"    , datosEnd.get("POLIZA"));
				paramsEnd.put("vTEndoso"   , StringUtils.isBlank(datosEnd.get("TENDOSO"))?" " : datosEnd.get("TENDOSO"));
				paramsEnd.put("vEndoso"    , datosEnd.get("ENDOSO"));
				paramsEnd.put("vIdMotivo"  , datosEnd.get("IDMOTIVO"));
				paramsEnd.put("vCalle"     , datosEnd.get("CALLE"));
				paramsEnd.put("vNumero"    , datosEnd.get("NUMERO"));

				paramsEnd.put("vNumInt"    , datosEnd.get("NUMINT"));
				paramsEnd.put("vNumDir"   , datosEnd.get("NUMDIR"));
			
				paramsEnd.put("vColonia"   , datosEnd.get("COLONIA"));
				paramsEnd.put("vTelefono1" , datosEnd.get("TELEFONO1"));
				paramsEnd.put("vTelefono2" , datosEnd.get("TELEFONO2"));
				paramsEnd.put("vTelefono3" , datosEnd.get("TELEFONO3"));
				paramsEnd.put("vFEndoso"   , datosEnd.get("FENDOSO"));

				paramsEnd.put("vUSER"   , usuarioCaptura);
				
				Integer res = autosSIGSDAO.endosoDomicilio(paramsEnd);
				
				logger.debug("Respuesta de cambio domicil, numero de endoso: " + res);
				
				if(res == null || res == 0){
					logger.debug("Endoso domicilio no exitoso");
				}else{
					numeroEndosoRes = res.intValue();
					
					try{
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("param1" , cdunieco);
						params.put("param2" , cdramo);
						params.put("param3" , estado);
						params.put("param4" , nmpoliza);
						params.put("param5" , nmsuplem);
						params.put("param6" , numeroEndosoRes);
						
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.ACTUALIZA_ENDB_DE_SIGS.getNombre(), params, null);
						
						
					} catch (Exception e1) {
						logger.error("Error en llamar al PL de obtencion de datos para Cambio Domicil para SIGS",e1);
						return 0;
					}	
				}
				
			} catch (Exception e){
				logger.error("Error en Envio datos domicilio: " + e.getMessage(),e);
			}
			
		}else{
			logger.warn("Aviso, No se tienen datos de Cambio Domicilio");
			return 0;
		}
		
		return numeroEndosoRes;
	}

	public int actualizaDatosCambioDomicilCP(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion){
		
		logger.debug(">>>>> Entrando a metodo actualizaDatosCambioDomicil Codigo Postal");
		
		int numeroEndosoRes = 0;
		List<Map<String,String>> datos = null;
		
		//Se invoca servicio para obtener los datos del CAMBIO DOMICIL
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			datos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_END_DOM_SIGS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Cambio Domicil Codigo Postal para SIGS",e1);
			return 0;
		}	
		
		if(datos != null && !datos.isEmpty()){
			
			String usuarioCaptura =  null;
			
			if(usuarioSesion!=null){
				if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
					usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
				}else{
					usuarioCaptura = usuarioSesion.getCodigoPersona();
				}
				
			}
			
			Map<String,String> datosEnd = datos.get(0);
			try{
				
				HashMap<String, Object> paramsEnd = new HashMap<String, Object>();
				paramsEnd.put("vSucursal"  , datosEnd.get("SUCURSAL"));
				paramsEnd.put("vRamo"      , datosEnd.get("RAMO"));
				paramsEnd.put("vPoliza"    , datosEnd.get("POLIZA"));
				paramsEnd.put("vTEndoso"   , StringUtils.isBlank(datosEnd.get("TENDOSO"))?" " : datosEnd.get("TENDOSO"));
				paramsEnd.put("vEndoso"    , datosEnd.get("ENDOSO"));
				paramsEnd.put("vCPostal"   , datosEnd.get("CPOSTAL"));
				paramsEnd.put("vCveEdo"    , datosEnd.get("CVEEDO"));
				paramsEnd.put("vDesMun"    , datosEnd.get("DESMUN"));
				paramsEnd.put("vMunCepomex", datosEnd.get("MUNCEPOMEX"));
				paramsEnd.put("vColonia"   , datosEnd.get("COLONIA"));
				paramsEnd.put("vTelefono"  , datosEnd.get("TELEFONO1"));
				paramsEnd.put("vCalle"     , datosEnd.get("CALLE"));
				paramsEnd.put("vNumero"    , datosEnd.get("NUMERO"));
				paramsEnd.put("vNumInt"    , datosEnd.get("NUMINT"));
				paramsEnd.put("vNumDir"    , datosEnd.get("NUMDIR"));
				paramsEnd.put("vUSER"      , usuarioCaptura);
				
				Integer res = autosSIGSDAO.cambioDomicilioCP(paramsEnd);
				
				logger.debug("Respuesta de cambio domicilio Codigo Postal, numero de endoso: " + res);
				
				if(res == null || res == 0){
					logger.debug("Endoso domicilio Codigo Postal no exitoso");
				}else{
					numeroEndosoRes = res.intValue();
				}
				
			} catch (Exception e){
				logger.error("Error en Endoso domicilio Codigo postal : " + e.getMessage(),e);
			}
			
		}else{
			logger.warn("Aviso, No se tienen datos de Cambio Domicilio Codigo Postal");
			return 0;
		}
		
		return numeroEndosoRes;
	}

	public int actualizaDatosCambioDomicilSinCP(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion){
		
		logger.debug(">>>>> Entrando a metodo Cambio Domicilio Sin Codigo Postal y cambio de Colonia");
		
		int numeroEndosoRes = 0;
		List<Map<String,String>> datos = null;
		
		//Se invoca servicio para obtener los datos del CAMBIO DOMICIL
		try{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			datos = storedProceduresManager.procedureListCall(
					ObjetoBD.OBTIENE_DATOS_END_DOM_SIGS.getNombre(), params, null);
			
			
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Cambio Domicil Sin Codigo Postal y cambio de Colonia para SIGS",e1);
			return 0;
		}	
		
		if(datos != null && !datos.isEmpty()){
			Map<String,String> datosEnd = datos.get(0);
			try{
				
				String usuarioCaptura =  null;
				
				if(usuarioSesion!=null){
					if(StringUtils.isNotBlank(usuarioSesion.getClaveUsuarioCaptura())){
						usuarioCaptura = usuarioSesion.getClaveUsuarioCaptura();
					}else{
						usuarioCaptura = usuarioSesion.getCodigoPersona();
					}
					
				}
				
				HashMap<String, Object> paramsEnd = new HashMap<String, Object>();
				paramsEnd.put("vSucursal"  , datosEnd.get("SUCURSAL"));
				paramsEnd.put("vRamo"      , datosEnd.get("RAMO"));
				paramsEnd.put("vPoliza"    , datosEnd.get("POLIZA"));
				paramsEnd.put("vTEndoso"   , StringUtils.isBlank(datosEnd.get("TENDOSO"))?" " : datosEnd.get("TENDOSO"));
				paramsEnd.put("vEndoso"    , datosEnd.get("ENDOSO"));
				paramsEnd.put("vIdMotivo"  , datosEnd.get("IDMOTIVO"));
				paramsEnd.put("vCalle"     , datosEnd.get("CALLE"));
				paramsEnd.put("vNumero"    , datosEnd.get("NUMERO"));
				paramsEnd.put("vColonia"   , datosEnd.get("COLONIA"));
				paramsEnd.put("vCodpostal" , datosEnd.get("CPOSTAL"));
				paramsEnd.put("vTelefono1" , datosEnd.get("TELEFONO1"));
				paramsEnd.put("vTelefono2" , datosEnd.get("TELEFONO2"));
				paramsEnd.put("vTelefono3" , datosEnd.get("TELEFONO3"));

				paramsEnd.put("vCveEdo" , datosEnd.get("CVEEDO"));
				paramsEnd.put("vMpioSPM", datosEnd.get("MPIOSPM"));
				paramsEnd.put("vNumInt" , datosEnd.get("NUMINT"));
				paramsEnd.put("vNumDir" , datosEnd.get("NUMDIR"));
				
				paramsEnd.put("vFEndoso", datosEnd.get("FENDOSO"));
				paramsEnd.put("vUSER"   , usuarioCaptura);
				
				Integer res = autosSIGSDAO.cambioDomicilioSinCPColonia(paramsEnd);
				
				logger.debug("Respuesta de cambio domicil Sin Codigo Postal y cambio de Colonia, numero de endoso: " + res);
				
				if(res == null || res == 0){
					logger.debug("Endoso domicilio Sin Codigo Postal y cambio de Colonia no exitoso");
				}else{
					numeroEndosoRes = res.intValue();
					
					try{
						LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
						params.put("param1" , cdunieco);
						params.put("param2" , cdramo);
						params.put("param3" , estado);
						params.put("param4" , nmpoliza);
						params.put("param5" , nmsuplem);
						params.put("param6" , numeroEndosoRes);
						
						storedProceduresManager.procedureVoidCall(
								ObjetoBD.ACTUALIZA_ENDB_DE_SIGS.getNombre(), params, null);
						
						
					} catch (Exception e1) {
						logger.error("Error en llamar al PL de obtencion de datos para Cambio Domicil Sin Codigo Postal y cambio de Colonia para SIGS",e1);
						return 0;
					}	
				}
				
			} catch (Exception e){
				logger.error("Error en Envio datos domicilio Sin Codigo Postal y cambio de Colonia: " + e.getMessage(),e);
			}
			
		}else{
			logger.warn("Aviso, No se tienen datos de Cambio Domicilio Sin Codigo Postal y cambio de Colonia");
			return 0;
		}
		
		return numeroEndosoRes;
	}
	
}