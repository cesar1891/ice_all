package mx.com.gseguros.ws.autosgs.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.WSException;
import mx.com.gseguros.externo.service.StoredProceduresManager;
import mx.com.gseguros.portal.general.util.ObjetoBD;
import mx.com.gseguros.portal.general.util.Ramo;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Agente;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Cobertura;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.CodigoPostal;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.ConfiguracionPaquete;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Cotizacion;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Cotizacion.FormasDePago;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.CotizacionIndividualWS;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.CotizacionIndividualWSService;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.CotizacionRequest;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.GuardarCotizacionResponse;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Inciso;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Paquete;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.SDTClientesSDTClientesItem;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.TipoProducto;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.TipoVehiculo;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.TotalFormaPago;
import mx.com.gseguros.ws.autosgs.cotizacion.client.jaxws.Version;
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
 * Implementacion de WS de Cotizacion y Emision de Autos con JAXWS
 * @author Hector
 *Deprecated, se debe de regenerar el cliente de jaxws 21-10-2014
 */
@Service
@Qualifier("emisionAutosServiceJAXWSImpl")
@Deprecated
public class EmisionAutosServiceJAXWSImpl implements EmisionAutosService {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EmisionAutosServiceJAXWSImpl.class);
	
	private static final long WS_TIMEOUT =  20000;
	
	@Value("${ws.cotizacion.autos.url}")
	private String endpointCotiza;
	
	@Value("${ws.emision.autos.url}")
	private String endpointEmite;
	
	@Autowired
	private StoredProceduresManager storedProceduresManager;
	
	public EmisionAutosVO cotizaEmiteAutomovilWS(String cdunieco,
			String cdramo, String estado, String nmpoliza,
			String nmsuplem, String ntramite, String cdtipsit, UserVO userVO){
		
		logger.debug(">>>>> Entrando a metodo WS Cotiza y Emite para Auto");
		
		EmisionAutosVO emisionAutoRes = null;
		SDTPoliza polizaEmiRes = null;
		Cotizacion datosCotizacionAuto = null;
		
		//Se invoca servicio para obtener los datos del auto
		try
		{
			LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
			params.put("param1" , cdunieco);
			params.put("param2" , cdramo);
			params.put("param3" , estado);
			params.put("param4" , nmpoliza);
			params.put("param5" , nmsuplem);
			
			List<Map<String,String>>lista = null;
			
			if(cdramo.equalsIgnoreCase(Ramo.AUTOS_FRONTERIZOS.getCdramo())){
				lista = storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_AUTO.getNombre(), params, null);
			}else if(cdramo.equalsIgnoreCase(Ramo.SERVICIO_PUBLICO.getCdramo())){
				lista = storedProceduresManager.procedureListCall(ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_SRV_PUBLICO.getNombre(), params, null);
			}else if(cdramo.equalsIgnoreCase(Ramo.AUTOS_RESIDENTES.getCdramo())){
				lista = storedProceduresManager.procedureListCall(
						ObjetoBD.OBTIENE_DATOS_WS_COTIZACION_RESIDENTES.getNombre(), params, null);
			}
			
			if(lista!=null && lista.size()>0)
			{
				Map<String,String>m=lista.get(0);
				datosCotizacionAuto=new Cotizacion();
				
				datosCotizacionAuto.setNumFolio(Integer.valueOf(m.get("NUMFOLIO")));
				datosCotizacionAuto.setVigencia(Integer.valueOf(m.get("VIGENCIA")));
				datosCotizacionAuto.setIdBanco(Integer.valueOf(m.get("IDBANCO")));
				datosCotizacionAuto.setMesesSinIntereses(Integer.valueOf(m.get("MESESSININTERESES")));
				datosCotizacionAuto.setIdOrigenSolicitud(Integer.valueOf(m.get("IDORIGENSOLICITUD")));
				datosCotizacionAuto.setFinVigencia(Utils.getXmlGregCalendar(m.get("FINVIGENCIA"), Constantes.FORMATO_FECHA));
				datosCotizacionAuto.setClaveGS(Integer.valueOf(m.get("CLAVEGS")));
				
				//idagente y sucursal
				Agente agente=new Agente();
				datosCotizacionAuto.setAgente(agente);
				agente.setIdAgente(m.get("IDAGENTE"));
				agente.setSucursal(Integer.valueOf(m.get("SUCURSAL")));
				
				//cve_cli
				SDTClientesSDTClientesItem cliente = new SDTClientesSDTClientesItem();
				datosCotizacionAuto.setCliente(cliente);
				cliente.setCveCli(Integer.valueOf(m.get("CVE_CLI").substring(m.get("CVE_CLI").length()-10)));
				cliente.setFisMor("");//null
				cliente.setNomCli("");//null
				cliente.setApePat("");//null
				cliente.setApeMat("");//null
				cliente.setRazSoc("");//null
				cliente.setAneCli("");//null
				cliente.setRfcCli("");//null
				cliente.setCveEle("");//null
				cliente.setCurpcli("");//null
				cliente.setCalCli("");//null
				cliente.setNumCli("");//null
				cliente.setColonia("");//null
				cliente.setPoblaci("");//null
				cliente.setNacExt("");//null
				cliente.setTelefo1("");//null
				cliente.setTelefo2("");//null
				cliente.setTelefo3("");//null
				cliente.setCorEle("");//null
				cliente.setPagWeb("");//null
				cliente.setFueIng("");//null
				cliente.setAdmCon("");//null
				cliente.setCarPub("");//null
				cliente.setNomCar("");//null
				cliente.setPerCar("");//null
				cliente.setApoCli("");//null
				cliente.setDomOri("");//null
				cliente.setNumPas("");//null
				cliente.setStaCli("");//null
				
				datosCotizacionAuto.setFormasDePago(new FormasDePago());//null
				
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
				datosCotizacionAuto.setDescuentoAgente(Integer.valueOf(m.get("DESCUENTOAGENTE")));
				
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
				datosCotizacionAuto.setInicioVigencia(Utils.getXmlGregCalendar(m.get("INICIOVIGENCIA"), Constantes.FORMATO_FECHA));
				
				//moneda
				datosCotizacionAuto.setMoneda(Integer.valueOf(m.get("MONEDA")));
				
				//formapago
				datosCotizacionAuto.setFormaPago(Integer.valueOf(m.get("FORMAPAGO")));
				
				TotalFormaPago totalFormaPago=new TotalFormaPago();
				datosCotizacionAuto.setTotalFormaPago(totalFormaPago);
				
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
				
				/**
				 * TODO: regenerar cliente:
				 */
				//totalFormaPago.setDerechoAgente(Double.valueOf(m.get("DERECHOAGENTE")));
				//totalFormaPago.setDerechoPromotor(Double.valueOf(m.get("DERECHOPROMOTOR")));
				
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
						nuevoVersion.setAmis(Integer.valueOf(m.get("AMIS")));
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
					}
					
					incisoIterado.setTipoVehiculo(tipoVehiculo);
					
					//valor
					incisoIterado.setValor(Double.valueOf(row.get("VALOR")));
					
					//conductor
					incisoIterado.setConductor(row.get("CONDUCTOR"));

					//TipoUso
					incisoIterado.setConductor(row.get("CONDUCTOR"));
					
					//primanetainc
					incisoIterado.setTipoUso(Integer.valueOf(row.get("TIPOUSO")));
					
					//version
					Version version=new Version();
					version.setDescripcion(row.get("DESCRIPCION"));
					incisoIterado.setVersion(version);
					
					ConfiguracionPaquete confPaq=new ConfiguracionPaquete();
					incisoIterado.setConfiguracionPaquete(confPaq);
					
					//versionTarifa
					confPaq.setVersionTarifa(Integer.valueOf(row.get("VERSIONTARIFAINC")));
					
					Paquete paqueteIncisoIterado        = new Paquete();
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
					
					
					////// iterar cada cobertura //////
					for(Map<String,String>rowCoberturaIncisoIterado:listaCoberturasIncisoIterado)
					{
						Cobertura coberturaDeInciso=new Cobertura();
						//idcobertura
						coberturaDeInciso.setIdCobertura(Integer.parseInt(rowCoberturaIncisoIterado.get("IDCOBERTURA")));
						//seleccionadocob
						boolean seleccionadoCob = false;
						if(StringUtils.isNotBlank(rowCoberturaIncisoIterado.get("SELECCIONADOCOB"))
								&&rowCoberturaIncisoIterado.get("SELECCIONADOCOB").equalsIgnoreCase("TRUE")) {
							seleccionadoCob = true;
						}
						coberturaDeInciso.setSeleccionado(seleccionadoCob);
						//deducible
						coberturaDeInciso.setDeducible(Double.valueOf(rowCoberturaIncisoIterado.get("DEDUCIBLE")));
						//suma_asegurada
						coberturaDeInciso.setSumaAsegurada(Double.valueOf(rowCoberturaIncisoIterado.get("SUMA_ASEGURADA")));
						//prima_bruta
						coberturaDeInciso.setPrimaBruta(Double.valueOf(rowCoberturaIncisoIterado.get("PRIMA_BRUTA")));
						//prima_netacob
						coberturaDeInciso.setPrimaNeta(Double.valueOf(rowCoberturaIncisoIterado.get("PRIMA_NETACOB")));
						//comision
						coberturaDeInciso.setComision(Double.valueOf(rowCoberturaIncisoIterado.get("COMISION")));
						//coberturasIncisoIterado.add(coberturaIteradaPaqueteIncisoIterado);
						paqueteIncisoIterado.getCoberturas().add(coberturaDeInciso);
					}
					
					confPaq.getPaquetes().add(paqueteIncisoIterado);

				}
				
				Inciso[]incisosCotizacionAux=new Inciso[incisos.size()];
				for(int i=0;i<incisosCotizacionAux.length;i++)
				{
					datosCotizacionAuto.getIncisos().add(incisos.get(i));
				}
				
				
			}
		} catch (Exception e1) {
			logger.error("Error en llamar al PL de obtencion de datos para Emision WS Autos",e1);
		}	
		
		if(datosCotizacionAuto != null){
			try{
				WrapperResultadosWS resultWSCot = this.ejecutaCotizacionAutosWS(datosCotizacionAuto);
						
				GuardarCotizacionResponse cotRes = (GuardarCotizacionResponse) resultWSCot.getResultadoWS();
				
				if(cotRes != null && cotRes.isExito()){
					logger.debug("Respuesta de WS Cotizacion Codigo(): " +cotRes.getCodigo());
					logger.debug("Respuesta de WS Cotizacion Mensaje(): " +cotRes.getMensaje());
					
					long numSolicitud = cotRes.getCotizacion().getIdCotizacion();
					logger.debug("Numero de Cotizacion Generada: " + numSolicitud);
					
					WrapperResultadosWS resultWSEmi = this.ejecutaEmisionAutosWS(numSolicitud);
					
					if(resultWSEmi != null && resultWSEmi.getResultadoWS() != null){
						polizaEmiRes = (SDTPoliza)resultWSEmi.getResultadoWS();
						emisionAutoRes = new EmisionAutosVO();
						emisionAutoRes.setNmpoliex(Long.toString(polizaEmiRes.getNumpol()));
						emisionAutoRes.setSubramo(Short.toString(polizaEmiRes.getRamos()));
						emisionAutoRes.setSucursal(Short.toString(polizaEmiRes.getSucursal()));
					}
					
				}else{
					logger.error("Error en la cotizacion de Autos WS, respuesta no exitosa");
				}
				
			} catch(WSException wse){
				logger.error("Error en WS de Autos, xml enviado: " + wse.getPayload(), wse);
			} catch (Exception e){
				logger.error("Error en WS de Autos: " + e.getMessage(),e);
			}
		}else{
			logger.error("Error, No se tienen datos del Auto");
		}
		
		return emisionAutoRes;
	}
	
	public List<Map<String,String>> obtieneEndososImprimir(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem){
		/**
		 * Sin Codigo
		 */
		return null;
	}
	
	private WrapperResultadosWS ejecutaCotizacionAutosWS(Cotizacion datosCotizacionAuto) throws Exception{
		
		WrapperResultadosWS resultWS = new WrapperResultadosWS();
		CotizacionIndividualWSService serviceCot = null;
		CotizacionIndividualWS cotizacionInd = null; 
		
		logger.debug("INVICANDO COTIZACION CON JAXWS...");
		
		try {
			
			logger.info(new StringBuffer("Endpoint a invocar=").append(endpointCotiza));
			
			URL wsdlLocation = new URL(endpointCotiza+"?wsdl");
			String targetNamespace="http://com.gs.cotizador.ws.cotizacionindividual";
            String name="CotizacionIndividualWSService";
            serviceCot =  new CotizacionIndividualWSService(wsdlLocation, new QName(targetNamespace, name));
//			serviceCot =  new CotizacionIndividualWSService(wsdlLocation);
			cotizacionInd = serviceCot.getCotizacionIndividualWSPort();
			
			/**
			 * TODO:
			 * stubGS._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			 */
			
		} catch (Exception e) {
			logger.error("Error en WS JAXWS: " + e.getMessage(),e);
			throw new Exception("Error de preparacion de JAXWS: " + e.getMessage());
		}
		
		CotizacionRequest cotizacionRequest = new CotizacionRequest();
		cotizacionRequest.setCotizacion(datosCotizacionAuto);
		
		try {
			GuardarCotizacionResponse resCot = cotizacionInd.wsGuardarCotizacion(cotizacionRequest);

			resultWS.setResultadoWS(resCot);
			/**
			 * TODO: 
			 * resultWS.setXmlIn(stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			 * logger.debug("Xml enviado para obtener Cotizacion de auto: " + stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString());
			 */
			
		} catch (Exception re) {
			throw new WSException("Error de conexion Cotizacion Autos: " + re.getMessage(), re, null/**TODO:
			stubGS._getServiceClient().getLastOperationContext().getMessageContext("Out").getEnvelope().toString()**/);
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

	@Override
	public Integer enviaRecibosAutosSigs(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, String nmpoliex, String subramo, String sucursal) {
		// TODO Auto-generated method stub
		return 99999;
	}

	@Override
	public int endosoCambioDomicil(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int actualizaDatosCambioDomicilCP(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int actualizaDatosCambioDomicilSinCP(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem, UserVO usuarioSesion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endosoCambioClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endosoCambioRfcClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endosoCambioNombreClienteAutos(String cdunieco, String cdramo,
			String estado, String nmpoliza, String nmsuplem) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}