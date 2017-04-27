/*
 *ICE-WIZARD
 * 
 * Creado el 02/04/2008 09:46:33 a.m. (dd/mm/aaaa hh:mm:ss)
 *
 * Copyright (C) Biosnet S.C.    
 * All rights reserved. Todos los derechos reservados.
 *
 * http://www.biosnetmx.com/
 */
package mx.com.gseguros.wizard.configuracion.producto.definicion.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.aon.portal.model.UserVO;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.utils.Utils;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ClausulaVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.PeriodoVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.ProductoVO;
import mx.com.gseguros.wizard.configuracion.producto.definicion.model.TipoPoliza;
import mx.com.gseguros.wizard.configuracion.producto.definicion.web.model.BoxingVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ProductoManager;
import mx.com.gseguros.wizard.configuracion.producto.service.TreeManager;
import mx.com.gseguros.wizard.configuracion.producto.util.WizardUtils;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * PrincipalProductosAction
 * 
 * <pre>
 *   Action que atiende todas las peticiones de creacion, modificacion y consulta
 *   de datos relacionados con la definicion de un producto nuevo,
 *   como son: clausulas, periodos y el producto en si mismo.
 * &lt;Pre&gt;
 * 
 * &#064;author &lt;a href=&quot;mailto:alfonso.marquez@biosnetmx.com&quot;&gt;Alfonso M&amp;aacuterquez&lt;/a&gt;
 * &#064;version	 2.0
 * 
 * &#064;since	 1.0
 * 
 */
public class PrincipalProductosAction extends Padre {

	private static final long serialVersionUID = 4913525502135960946L;

	private static final transient Log log = LogFactory
			.getLog(PrincipalProductosAction.class);
	/**
	 * Manager con implementacion de Endpoint para la consulta a BD.
	 */
	private ProductoManager productoManager;
	private TreeManager treeManager;
	/**
	 * Atributo de respuesta interpretado por strust como la correcta
	 * realizacion de. cada peticion.
	 */
	private boolean success;
	// producto
	private List<ProductoVO> editarProducto;
	private String banderaEditar;
	private String codigoRamoEditar;
	/**
	 * Atributo agregado como parametro de la petici�n por struts que indica si
	 * el metodo debe ejecutar creacion o modificacion del Producto.
	 */
	private String idProducto;
	private String mode;
	private int codigoRamo;
	private String descripcionRamo;
	private String descripcion;
	private String codigoTipoParametro;
	private String descripcionTipoParametro;
	private String codigoTipoRamo;
	private String descripcionTipoRamo;
	private String switchSuscripcion;
	private String switchClausulasNoTipificadas;
	private int tipoCalculoPolizasTemporales;
	private String switchRehabilitacion;
	private int mesesBeneficios;
	private String switchPrimasPeriodicas;
	private String switchPermisoPagosOtrasMonedas;
	private String switchReaseguro;
	private String switchSiniestros;
	private String switchTarifa;
	private String switchReinstalacionAutomatica;
	private String switchPrimasUnicas;
	private String switchDistintasPolizasPorAsegurado;
	private String switchPolizasDeclarativas;
	private String switchPreavisoCartera;
	private String switchTipo;
	private String codigoSwitchTipo;
	private String switchTipoDagnos;
	private String switchTipoOtro;
	private String switchTarifaDireccionalTotal;
	private int cantidadDiasReclamacion;
	private String switchSubincisos;
	private List<String> polizasPermitidas;
	private String renovable;
	private String temporal;
	private String vidaEntera;
	// catalogos de producto
	private List<LlaveValorVO> catalogoTipoProducto;
	private String claveTipoProducto;
	private String valorTipoProducto;
	private List<LlaveValorVO> catalogoTipoRamo;
	private String claveTipoRamo;
	private String valorTipoRamo;
	private List<LlaveValorVO> catalogoTipoPoliza;
	private String claveTipoPoliza;
	private String valorTipoPoliza;
	private List<LlaveValorVO> catalogoTipoSeguro;
	private String claveTipoSeguro;
	private String valorTipoSeguro;
	private String switchCancelacion;
	private String switchEndosos;
	
	// clausula
	/**
	 * Atributo agregado como parametro de la petici�n por struts que indica si
	 * el metodo debe ejecutar creacion o modificacion de la Clausula.
	 */
	private String idClausula;
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * ClausulaVO con los valores de la consulta.
	 */
	private List<ClausulaVO> clausulas;
	/**
	 * Atributo agregado por struts que contiene la clave de la clausula
	 * asociada al producto.
	 */
	private String clave;
	/**
	 * Atributo agregado por struts que contiene el nombre de la clausula
	 * asociada al producto.
	 */
	private String clausulaDescripcion;
	/**
	 * Atributo agregado por struts que contiene la clave de la clausula
	 * asociada al producto.
	 */
	private String linea;
	// catalogo de clausulas
	private List<ClausulaVO> clausulasCatalogo;
	private String claveCatalogoClausulas;
	// periodo
	/**
	 * Atributo agregado como parametro de la petici�n por struts que indica si
	 * el metodo debe ejecutar creacion o modificacion del Periodo.
	 */
	private String codigoPeriodo;
	/**
	 * Atributo de respuesta interpretado por strust con la lista de beans tipo
	 * PeriodoVO con los valores de la consulta.
	 */
	private List<PeriodoVO> periodos;

	/**
	 * Atributo agregado por struts que contiene la fecha inicial de los
	 * periodos de valides de cada producto.
	 */
	private String inicio;
	/**
	 * Atributo agregado por struts que contiene la fecha final de los periodos
	 * de valides de cada producto.
	 */
	private String fin;
	// combos test
	private List<LlaveValorVO> combos;
	private List<LlaveValorVO> combosHijo;
	private List<LlaveValorVO> combosNieto;

	private String combo;
	private String comboHijo;
	private String comboNieto;

	// cargar datos
	private ClausulaVO c;
	private BoxingVO bvo;

	private String mensaje;
    
    private boolean habilita;
    
    @Value("${user.server.layouts}")
    private String userServerLayouts;	

	@Value("${pass.server.layouts}")
    private String passServerLayouts;	

	@Value("${dominio.server.layouts}")
    private String dominioServerLayouts;	

	@Value("${dominio.server.layouts2}")
    private String dominioServerLayouts2;	
	
	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Metodo <code>execute</code> con el que es llamado desde Struts para
	 * atender la petici�n web.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String execute() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("entro al execute");
		}
		codigoRamo =0;
		
		session.clear();
		return INPUT;
	}

	// metodos del combo
	public String test() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if (combo != null) {
			if(isDebugEnabled){
				log.debug(combo);
			}
		}
		if (comboHijo != null) {
			if(isDebugEnabled){
				log.debug(comboHijo);
			}
		}
		if (comboNieto != null) {
			if(isDebugEnabled){
				log.debug(comboNieto);
			}
		}

		return SUCCESS;

	}

	public String loadVO() {
		bvo = new BoxingVO();
		c = new ClausulaVO();
		c.setCodigoClausula("beep");
		c.setCodigoRamo(1);
		c.setDescripcionClausula("desc");
		c.setDescripcionLinea("desc plain");
		c.setNumeroLineas(15);
		bvo.setC(c);
		success = true;
		return SUCCESS;
	}

	public String testCombo() {
		combos = new ArrayList<LlaveValorVO>();
		LlaveValorVO c = null;
		for (int i = 0; i < 10; i++) {
			c = new LlaveValorVO();
			c.setKey(Integer.toString(i));
			c.setNick("nick" + Integer.toString(i));
			c.setValue("value" + Integer.toString(i));
			combos.add(c);
		}
		success = true;
		return SUCCESS;
	}

	public String testComboHijo() {
		combosHijo = new ArrayList<LlaveValorVO>();
		LlaveValorVO c = null;
		for (int i = 0; i < 10; i++) {
			c = new LlaveValorVO();
			c.setKey(Integer.toString(i));
			c.setNick("nick" + Integer.toString(i));
			if (combo != null) {
				c.setValue("value" + combo);
			} else {
				c.setValue("value" + Integer.toString(i));
			}

			combosHijo.add(c);
		}
		success = true;
		return SUCCESS;
	}

	public String testCombosNieto() {
		combosNieto = new ArrayList<LlaveValorVO>();
		LlaveValorVO c = null;
		for (int i = 0; i < 10; i++) {
			c = new LlaveValorVO();
			c.setKey(Integer.toString(i));
			c.setNick("nick" + Integer.toString(i));
			c.setValue("value" + Integer.toString(i));
			combosNieto.add(c);
		}
		success = true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>insertarProducto</code> quee es llamado desde Struts para
	 * insertar el producto en base a todos los datos capturados.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String insertarProducto() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log
					.debug("Dentro de PrincipalProductosAction ==> insertarProducto()");
			log.debug("!!!!!!!!!!!!!idProducto" + idProducto);
			log.debug("!!!!!!!!!!!!!mode" + mode);
			log.debug("!!!!!!!!!!!!!codigoRamo" + codigoRamo);
			log.debug("!!!!!!!!!!!!!descripcionRamo" + descripcionRamo);
			log.debug("!!!!!!!!!!!!!descripcion" + descripcion);
			log.debug("!!!!!!!!!!!!!codigoTipoParametro" + codigoTipoParametro);
			log.debug("!!!!!!!!!!!!!codigoTipoRamo" + codigoTipoRamo);
			log.debug("!!!!!!!!!!!!!descripcionTipoRamo" + descripcionTipoRamo);
			log.debug("!!!!!!!!!!!!!switchSuscripcion" + switchSuscripcion);
			log.debug("!!!!!!!!!!!!!switchClausulasNoTipificadas"
					+ switchClausulasNoTipificadas);
			log.debug("!!!!!!!!!!!!!tipoCalculoPolizasTemporales"
					+ tipoCalculoPolizasTemporales);
			log.debug("!!!!!!!!!!!!!switchRehabilitacion"
					+ switchRehabilitacion);
			log.debug("!!!!!!!!!!!!!mesesBeneficios" + mesesBeneficios);
			log.debug("!!!!!!!!!!!!!switchPrimasPeriodicas"
					+ switchPrimasPeriodicas);
			log.debug("!!!!!!!!!!!!!switchPermisoPagosOtrasMonedas"
					+ switchPermisoPagosOtrasMonedas);
			log.debug("!!!!!!!!!!!!!switchReaseguro" + switchReaseguro);
			log.debug("!!!!!!!!!!!!!switchSiniestros" + switchSiniestros);
			log.debug("!!!!!!!!!!!!!switchTarifa" + switchTarifa);
			log.debug("!!!!!!!!!!!!!switchReinstalacionAutomatica"
					+ switchReinstalacionAutomatica);
			log.debug("!!!!!!!!!!!!!switchPrimasUnicas" + switchPrimasUnicas);
			log.debug("!!!!!!!!!!!!!switchDistintasPolizasPorAsegurado"
					+ switchDistintasPolizasPorAsegurado);
			log.debug("!!!!!!!!!!!!!switchPolizasDeclarativas"
					+ switchPolizasDeclarativas);
			log.debug("!!!!!!!!!!!!!switchPreavisoCartera"
					+ switchPreavisoCartera);
			log.debug("!!!!!!!!!!!!!switchTipo" + switchTipo);
			log.debug("!!!!!!!!!!!!!switchTarifaDireccionalTotal"
					+ switchTarifaDireccionalTotal);
			log.debug("!!!!!!!!!!!!!cantidadDiasReclamacion"
					+ cantidadDiasReclamacion);
			log.debug("!!!!!!!!!!!!!switchSubincisos= " + switchSubincisos);
			log.debug("!!!!!!!!!!!!!renovable= " + renovable);
			log.debug("!!!!!!!!!!!!!temporal =" + temporal);
			log.debug("!!!!!!!!!!!!!vidaEntera= " + vidaEntera);
			log.debug("!!!!!!!!!!!!!claveTipoProducto= " + claveTipoProducto);
			log.debug("!!!!!!!!!!!!!valorTipoProducto= " + valorTipoProducto);
			log.debug("!!!!!!!!!!!!!claveTipoRamo= " + claveTipoRamo);
			log.debug("!!!!!!!!!!!!!valorTipoRamo= " + valorTipoRamo);
			log.debug("!!!!!!!!!!!!!claveTipoPoliza= " + claveTipoPoliza);
			log.debug("!!!!!!!!!!!!!valorTipoPoliza= " + valorTipoPoliza);
			log.debug("!!!!!!!!!!!!!claveTipoSeguro= " + claveTipoSeguro);
			log.debug("!!!!!!!!!!!!!valorTipoSeguro= " + valorTipoSeguro);
			log.debug("!!!!!!!!!!!!!switchTipo= "+switchTipo);
		}
		// if (switchSuscripcion == null) {
		switchSuscripcion = "N";
		// }
		// if (switchClausulasNoTipificadas == null) {
		switchClausulasNoTipificadas = "N";
		// }
		if (switchRehabilitacion == null) {
			switchRehabilitacion = "N";
		}else if(switchRehabilitacion.equals("on")){
			switchRehabilitacion="S";
		}
		// if (switchPrimasPeriodicas == null) {
		switchPrimasPeriodicas = "N";
		// }
		if (switchPermisoPagosOtrasMonedas == null) {
			switchPermisoPagosOtrasMonedas = "N";
		}
		// if (switchReaseguro == null) {
		switchReaseguro = "N";
		// }
		// if (switchSiniestros == null) {
		switchSiniestros = "N";
		// }
		// if (switchTarifa == null) {
		switchTarifa = "N";
		// }
		if (switchReinstalacionAutomatica == null) {
			switchReinstalacionAutomatica = "N";
		}
		// if (switchPrimasUnicas == null) {
		switchPrimasUnicas = "N";
		// }
		if (switchDistintasPolizasPorAsegurado == null) {
			switchDistintasPolizasPorAsegurado = "N";
		}
		// if (switchPolizasDeclarativas == null) {
		switchPolizasDeclarativas = "N";
		// }
		// if (switchPreavisoCartera == null) {
		switchPreavisoCartera = "N";
		// }
//		if (switchTipo == null ) {
//			switchTipo = "0";
//		}else{
//			log.debug("switchTipo="+switchTipo);
//		}
//		if (switchTipoDagnos != null ) {
//			switchTipo = "1";
//		}if (switchTipo != null ) {
//			switchTipo = "0";
//		}
		if(codigoSwitchTipo==null){
			codigoSwitchTipo="1";
		}
		// if (switchTarifaDireccionalTotal == null) {
		switchTarifaDireccionalTotal = "N";
		// }
		if (switchSubincisos == null) {
			switchSubincisos = "N";
		}
		if (switchCancelacion == null) {
			switchCancelacion = "N";
		}			
		
		TipoPoliza tipoPoliza = null;
		
		List<TipoPoliza> listTiposPoliza = new ArrayList<TipoPoliza>();
		if (StringUtils.isNotBlank(renovable)) {
		    tipoPoliza = new TipoPoliza("R", true);
        } else {
            tipoPoliza = new TipoPoliza("R", false);
        }
        listTiposPoliza.add(tipoPoliza);
			
		if (StringUtils.isNotBlank(temporal)) {
			tipoPoliza = new TipoPoliza("T", true);
        } else {
            tipoPoliza = new TipoPoliza("T", false);
        }
        listTiposPoliza.add(tipoPoliza);
			
		if (StringUtils.isNotBlank(vidaEntera)) {
			tipoPoliza = new TipoPoliza("P", true);
        } else {
            tipoPoliza = new TipoPoliza("P", false);
        }
        listTiposPoliza.add(tipoPoliza);
                
		//log.debug(StringEscapeUtils.unescapeHtml(descripcion));
		//log.debug(StringEscapeUtils.escapeHtml(descripcion));
		boolean result = true;
		ProductoVO productoVO = new ProductoVO();
		productoVO.setCodigoRamo(codigoRamo);
		productoVO.setDescripcionRamo(StringEscapeUtils.escapeHtml4(descripcionRamo));
		productoVO.setDescripcion(StringEscapeUtils.escapeHtml4(descripcion));
		productoVO.setCodigoTipoParametro(claveTipoProducto);
		productoVO.setCodigoTipoRamo(claveTipoRamo);
		productoVO.setCodigoTipoPoliza(claveTipoPoliza);
		productoVO.setCodigoTipoSeguro(claveTipoSeguro);

		productoVO.setSwitchSuscripcion(switchSuscripcion);
		productoVO
				.setSwitchClausulasNoTipificadas(switchClausulasNoTipificadas);
		productoVO
				.setTipoCalculoPolizasTemporales(tipoCalculoPolizasTemporales);
		productoVO.setSwitchRehabilitacion(switchRehabilitacion);
		productoVO.setMesesBeneficios(mesesBeneficios);
		productoVO.setSwitchPrimasPeriodicas(switchPrimasPeriodicas);
		productoVO
				.setSwitchPermisoPagosOtrasMonedas(switchPermisoPagosOtrasMonedas);
		productoVO.setSwitchReaseguro(switchReaseguro);
		productoVO.setSwitchSiniestros(switchSiniestros);
		productoVO.setSwitchTarifa(switchTarifa);
		productoVO
				.setSwitchReinstalacionAutomatica(switchReinstalacionAutomatica);
		productoVO.setSwitchPrimasUnicas(switchPrimasUnicas);
		productoVO
				.setSwitchDistintasPolizasPorAsegurado(switchDistintasPolizasPorAsegurado);
		productoVO.setSwitchPolizasDeclarativas(switchPolizasDeclarativas);
		productoVO.setSwitchPreavisoCartera(switchPreavisoCartera);
		productoVO.setSwitchTipo(codigoSwitchTipo);
		productoVO
				.setSwitchTarifaDireccionalTotal(switchTarifaDireccionalTotal);
		productoVO.setCantidadDiasReclamacion(0); // TODO hacerlo 0
		productoVO.setSwitchSubincisos(switchSubincisos);
		productoVO.setListTiposPoliza(listTiposPoliza);
		productoVO.setSwitchCancelacion(switchCancelacion);
		productoVO.setSwitchEndosos(switchEndosos);
		success = true;
		if (isDebugEnabled) {
			log.debug("banderaEditar="+banderaEditar);
		}
		if(banderaEditar!=null && banderaEditar.equals("1")){
			productoVO.setCodigoRamo(Integer.parseInt(codigoRamoEditar));
			codigoRamo= Integer.parseInt(codigoRamoEditar);
		}
			
        if (isDebugEnabled) {
            log.debug(":: success : " + success);
        }
        
		if (success) {
			mensaje = productoManager.insertarProducto(productoVO, success);
			codigoRamo = productoVO.getCodigoRamo();
			
			
			if (success) {
				// agregar Clausulas y Periodos
                if (isDebugEnabled) {
                    log.debug(":: session.containsKey.CLAUSULAS : " + session.containsKey("CLAUSULAS"));
                }
				if(session.containsKey("CLAUSULAS") && !((List<ClausulaVO>)session.get("CLAUSULAS")).isEmpty()){
					clausulas = (List<ClausulaVO>) session.get("CLAUSULAS");
					List<ClausulaVO> agregarClausulas = new ArrayList<ClausulaVO>();				
					for(ClausulaVO cvo:clausulas){
                        if (isDebugEnabled) {
                            log.debug(":: cvo : " + cvo.getCodigoClausula());
                        }
						if(cvo.getIdBase()==null)
							agregarClausulas.add(cvo);
						
					}				
					if(!agregarClausulas.isEmpty())
						productoManager.asociarClausulas(clausulas, codigoRamo, success);					
				}
				if (!success)
					result = false;				
					periodos = (List<PeriodoVO>) session.get("PERIODOS");
					if(periodos!=null && !periodos.isEmpty()){
						for(PeriodoVO periodo:periodos){							
							periodo.setFin(periodo.getFinFormato());
							periodo.setInicio(periodo.getInicioFormato());
						}
					}
					productoManager.agregarPeriodos(periodos, codigoRamo, success);
					if (!result)
						success = false;
				clearSession();
				List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
				session.put("ARBOL_PRODUCTOS_RECARGAR", temporalTree);
				session.remove("ARBOL_PRODUCTOS");
			}
		}
//		codigoRamo = productoVO.getCodigoRamo();
		log.debug("PRODDDDDDDDDDDDD1: " + productoVO.getCodigoRamo());
		log.debug("PRODDDDDDDDDDDDD2: " + codigoRamo);
		return SUCCESS;
	}

	public String editarProducto() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("entro a editar producto");
		}
		editarProducto = new ArrayList<ProductoVO>();
		ProductoVO productoActual = null;
		if(session.containsKey("CODIGO_NIVEL0")){			
			String tempId= (String) session.get("CODIGO_NIVEL0");
			if(isDebugEnabled){
				log.debug("!!!!!!!!!!!!!!!!!!!!tempId="+tempId);
			}
			productoActual = productoManager.editarProducto(tempId);
            if(isDebugEnabled){
                log.debug("productoActual.getTemporal="+productoActual.getTemporal());
                log.debug("productoActual.getRenovable="+productoActual.getRenovable());
                log.debug("productoActual.getVidaEntera="+productoActual.getVidaEntera());
            }
			if(isDebugEnabled){
				log.debug("id de producto"+productoActual.getCodigoRamo());
			}
			if(productoActual!=null){
                habilita = true;
				if(productoActual.getCodigoTipoPoliza()!=null){
					if(!session.containsKey("CATALOGO_TIPO_POLIZA")){}
						catalogoTipoPolizaJSON();
					catalogoTipoPoliza = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_POLIZA");	
					if(catalogoTipoPoliza!=null && !catalogoTipoPoliza.isEmpty()){
						for(LlaveValorVO poliza:catalogoTipoPoliza){
							if(poliza.getKey().equals(productoActual.getCodigoTipoPoliza()))
								productoActual.setDescripcionTipoPoliza(poliza.getValue());
						}
					}
				}
				if(productoActual.getCodigoTipoSeguro()!=null){
					if(!session.containsKey("CATALOGO_TIPO_SEGURO")){}
						catalogoTipoSeguroJSON();
					catalogoTipoSeguro = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_SEGURO");	
					if(catalogoTipoSeguro!=null && !catalogoTipoSeguro.isEmpty()){
						for(LlaveValorVO seguro:catalogoTipoSeguro){
							if(seguro.getKey().equals(productoActual.getCodigoTipoSeguro()))
								productoActual.setDescripcionTipoSeguro(seguro.getValue());
						}
					}
				}

			}
			editarClausulas(tempId);
			editarPeriodos(tempId);
		}else{			
            if(isDebugEnabled){
                log.debug("session not containsKey CODIGO_NIVEL0");
            }
			productoActual = new ProductoVO();
			List<ClausulaVO> clausulasDelProducto= new ArrayList<ClausulaVO>();
			session.put("CLAUSULAS", clausulasDelProducto);
			List<PeriodoVO> periodosProducto= new ArrayList<PeriodoVO>();
			session.put("PERIODOS", periodosProducto);
		}		
		editarProducto.add(productoActual);		
		success= true;
		return SUCCESS;
	}
	/**
	 * Metodo <code>asociarPeriodo</code> que es llamado desde Struts por cada
	 * nuevo perido de valides asociado a un producto y guardados en una
	 * variable temporal.
	 * 
	 * @param periodos -
	 *            Lista de Bean's con todos los periodos de valides de un
	 *            producto llendo por struts.
	 * @param productoId -
	 *            identificador del producto.
	 * @return boolean - boleano indica si ocurrio algun problema durante la
	 *         ejecucion el metodo.
	 * @throws Exception
	 */
	public String asociarPeriodo() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (isDebugEnabled) {
			log.debug("!!!!!!!!!!!!!!esto es inicio desde la pantalla"+ inicio);
			log.debug("!!!!!!!!!!!!!!esto es fin desde la pantalla" + fin);
		}
		boolean continuar = true;
		boolean editar = false;
		ArrayList<PeriodoVO> temp = (ArrayList<PeriodoVO>) session.get("PERIODOS");
		if (temp==null || temp.isEmpty()) {
			listaDePeriodosJSON();
			//continuar =true;
		}
		PeriodoVO periodoVO = new PeriodoVO();

		periodoVO.setInicio(inicio);
		periodoVO.setInicioFormato(inicio);
		periodoVO.setFin(fin);
		periodoVO.setFinFormato(fin);
		
		if (!temp.isEmpty()) {
				String finAnterior = temp.get(temp.size() - 1).getFinFormato();				
				if((codigoPeriodo!=null && StringUtils.isNotBlank(codigoPeriodo) && !codigoPeriodo.equals("-1"))){
					if (isDebugEnabled) {
						log.debug("@@@@@@@@@@@@@@@@@@@codigoPeriodo" + codigoPeriodo);
					}
					int integerCodigoPeriodo = Integer.parseInt(codigoPeriodo);
					periodoVO.setCodigoPeriodo(integerCodigoPeriodo);
					
//					integerCodigoPeriodo++;
//					if(temp.size()>integerCodigoPeriodo){
//						PeriodoVO periodoPosterior = temp.get(integerCodigoPeriodo);
//						continuar = productoManager.validacionDePeriodo(finAnterior,
//							WizardUtils.parseDate(inicio), WizardUtils.parseDate(fin),
//							periodoPosterior.getInicioFormato());
//					}
					editar=true;
				}else{
					periodoVO.setCodigoPeriodo(temp.size() + 1);
					if (isDebugEnabled) {
						log.debug("@@@@@@@@@@@@@@@@@@@finAnterior" + finAnterior);
					}
					continuar = productoManager.validacionDePeriodo(finAnterior,
							WizardUtils.parseDate(inicio), WizardUtils.parseDate(fin));
				}
			
		}else{periodoVO.setCodigoPeriodo(1);}
		if (continuar) {
			if(editar){
				int integerCodigoPeriodo = Integer.parseInt(codigoPeriodo);
				integerCodigoPeriodo--;
				temp.set(integerCodigoPeriodo,periodoVO);
			}else
				temp.add(periodoVO);
//			if(codigoPeriodo==null || (codigoPeriodo!=null && codigoPeriodo.equals("-1")))
//				periodoVO.setCodigoPeriodo(temp.size() + 1);
//			else
//				
//			
			
			periodos = temp;
			session.put("PERIODOS", temp);
			success = true;
		} else {
			success = false;
		}

		return SUCCESS;
	}
	public void editarPeriodos(String cdRamo) throws Exception{
		List<PeriodoVO> lpvo = null;
		lpvo = productoManager.periodosJson(Integer.parseInt(cdRamo));
		if(lpvo!=null && !lpvo.isEmpty()){
			int idBase=0;
			for(PeriodoVO pvo:lpvo){
				pvo.setIdBase(Integer.toString(idBase));
				idBase++;
			}
		}else
			lpvo= new ArrayList<PeriodoVO>();			
		
		session.put("PERIODOS", lpvo);
	}
	/**
	 * Metodo <code>eliminarPeriodos</code> que son borrados desde
	 * Struts cada periodo de valides de un producto de forma temporal. 
	 * 
	 * 
	 * @param periodoTemp -
	 *            Lista de Bean's con todos los periodos de valides de un
	 *            producto llendo por struts.
	 * @param productoId -
	 *            identificador del producto.
	 * @throws ApplicationException
	 */
	public String eliminarPeriodos(){
		if(session.containsKey("PERIODOS")){
			List<PeriodoVO> temp= (List<PeriodoVO>) session.get("PERIODOS");
			//log.debug("lista size"+temp.size());
			/*for(PeriodoVO lista: temp){
				log.debug("CODIGO PERIODO TEMP"+lista.getCodigoPeriodo());
				log.debug("fecha ini PERIODO TEMP"+lista.getInicioFormato());
			}*/
			//log.debug("!!!!!!!CODIGO PERIODO"+codigoPeriodo);
			int codPeriodo= Integer.parseInt(codigoPeriodo);
			codPeriodo=codPeriodo-1;
			if(temp.size()>= codPeriodo){
				temp.remove(codPeriodo);
				session.put("PERIODOS", temp);
			}
		}
		success= true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>listaDePeriodosJSON</code> con el que son llamado desde
	 * Struts todas los periodos de valides de un producto.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String listaDePeriodosJSON() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("sntro a lista de periodos json");
		}
		periodos = null;
//		if(session.containsKey("CODIGO_NIVEL0"))
//			idProducto=(String) session.get("CODIGO_NIVEL0");
//		else
//		idProducto="1";
		if (session.containsKey("PERIODOS") && !((List)session.get("PERIODOS")).isEmpty()) {
			periodos = (List<PeriodoVO>) session.get("PERIODOS");
			int codigoTemporal=1;
			for(PeriodoVO listaPeriodos: periodos){				
				listaPeriodos.setCodigoPeriodo(codigoTemporal);
				codigoTemporal++;
			}
			/*for(PeriodoVO listaPeriodos: periodos ){
				log.debug("periodo inicio formato"+listaPeriodos.getInicioFormato());
				log.debug("periodo fin formato"+listaPeriodos.getFinFormato());
				log.debug("periodo inicio"+listaPeriodos.getInicio());
				log.debug("periodo fin"+listaPeriodos.getFin());
			}*/
		} else {
//			if (idProducto != null) {
//				periodos = productoManager.periodosJson(Integer
//						.parseInt(idProducto));
//				
//			} else {
				periodos = new ArrayList<PeriodoVO>();
//			}				
			session.put("PERIODOS", periodos);
		}
		success = true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>editarClausula</code> con el que son llamado desde Struts
	 * los datos de la Clausulas para editarla.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public void editarClausulas(String cdRamo) throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("esta en el metodo editar clausulas+ cdRamo"+cdRamo);
		}
		List<ClausulaVO> lcvo = null;
		lcvo = productoManager.clausulasJson(Integer.parseInt(cdRamo));
		if(isDebugEnabled){
			log.debug(lcvo);
		}
		if(lcvo!=null && !lcvo.isEmpty()){
			if(isDebugEnabled){
				log.debug(lcvo.isEmpty());
			}
			clausulasCatalogo= new ArrayList<ClausulaVO>();
			if(!session.containsKey("CATALOGO_CLAUSULAS"))
				catalogoDeClausulasJSON();
			clausulasCatalogo = (List<ClausulaVO>) session.get("CATALOGO_CLAUSULAS");
			int idBase=0;
			List<Integer> clavesARemover= new ArrayList<Integer>(); 
			for(ClausulaVO cvo:lcvo){
				cvo.setIdBase(Integer.toString(idBase));
				idBase++;
				if(clausulasCatalogo!=null && !clausulasCatalogo.isEmpty()){
					int posicion=0;
					for(ClausulaVO lvvo:clausulasCatalogo){
						if(cvo.getCodigoClausula().equals(lvvo.getCodigoClausula())){
							clavesARemover.add(posicion);
						}
						posicion++;	
					}
				}
			}
			if(!clavesARemover.isEmpty()){
				for(int i:clavesARemover){
                    if(clausulasCatalogo.size() > i){
                        clausulasCatalogo.remove(i);    
                    }					
				}
			}
			session.put("CATALOGO_CLAUSULAS", clausulasCatalogo);
		}else
			lcvo= new ArrayList<ClausulaVO>();
		session.put("CLAUSULAS", lcvo);
		
	}

	/**
	 * Metodo <code>asociarClausula</code> que es llamado desde Struts para
	 * insertar un nueva Clausula en un arreglo.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String asociarClausula() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Asociar Clausula " + claveCatalogoClausulas);
            log.debug("!!!!!!!!!!!!!!!session.containsKey.CATALOGO_CLAUSULAS " + session.containsKey("CATALOGO_CLAUSULAS"));
		}
        
        ClausulaVO clausulaVO = new ClausulaVO();
		clausulaVO.setDescripcionClausula(claveCatalogoClausulas);
		if(session.containsKey("CATALOGO_CLAUSULAS")){
    		List<ClausulaVO> catalogo = (List<ClausulaVO>)session.get("CATALOGO_CLAUSULAS");
    		ClausulaVO clausulaRemover = null;
    		for (ClausulaVO clau : catalogo) {
    			if (clau.getDescripcionClausula().equals(claveCatalogoClausulas)) {
    				clausulaVO.setCodigoClausula(clau.getCodigoClausula());
    				if (clausulaVO.getDescripcionClausula() != null) {
    					clausulaRemover = clau;
    				}
    			}
    		}
    		if (clausulaRemover != null) {
    			catalogo.remove(clausulaRemover);
    			session.put("CATALOGO_CLAUSULAS", catalogo);
    			List<ClausulaVO> temp = (List<ClausulaVO>) session.get("CLAUSULAS");
                /////////////////////////////////Validacion
                for (ClausulaVO clausula : temp) {
                    if (clausulaVO.getCodigoClausula().equalsIgnoreCase(clausula.getCodigoClausula())) {
                        if(isDebugEnabled){
                            log.debug("!!!!!!!!!!!!!!! Se repite la clausula");
                            log.debug("clausula" + clausula.getCodigoClausula());
                        } 
                        mensaje = "La Clausula seleccionada ya est� asociada";
                        
                        success = false;
                        return SUCCESS;
                    }
                }
                ///////////////////////////////////////////
    			temp.add(clausulaVO);
                if(isDebugEnabled){
                    log.debug("!!!!!!!!!!!!!!! Se asocio la clausula");
                }
    			clausulas = temp;
    			session.put("CLAUSULAS", temp);
    			//log.debug(temp);
    		}
		}else{
            catalogoDeClausulasJSON();
            
            List<ClausulaVO> catalogo = (List<ClausulaVO>)session.get("CATALOGO_CLAUSULAS");
            ClausulaVO clausulaRemover = null;
            for (ClausulaVO clau : catalogo) {
                if (clau.getDescripcionClausula().equals(claveCatalogoClausulas)) {
                    clausulaVO.setCodigoClausula(clau.getCodigoClausula());
                    if (clausulaVO.getDescripcionClausula() != null) {
                        clausulaRemover = clau;
                    }
                }
            }
            if(isDebugEnabled){
                log.debug("!!!!!!!!!!!!!!! clausulaRemover " + clausulaRemover);
            }
            if (clausulaRemover != null) {
                catalogo.remove(clausulaRemover);
                session.put("CATALOGO_CLAUSULAS", catalogo);
                List<ClausulaVO> temp = (List<ClausulaVO>) session.get("CLAUSULAS");
                
                if (temp != null) {
                    temp.add(clausulaVO);
                
                    if(isDebugEnabled){
                        log.debug("!!!!!!!!!!!!!!! Se asocio la clausula");
                    }
                }
                
                clausulas = temp;
                session.put("CLAUSULAS", temp);
                //log.debug(temp);
            }
		}
		success = true;
		return SUCCESS;
	}

	/**
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String agregarClausula() throws Exception {
        boolean isDebugEnabled = log.isDebugEnabled();
        if(isDebugEnabled){
            log.debug("-> agregarClausula");
        }
		ClausulaVO clausulaVO = new ClausulaVO();
		clausulaVO.setCodigoClausula(clave);
		clausulaVO.setDescripcionClausula(clausulaDescripcion);
		clausulaVO.setDescripcionLinea(linea);
		clausulaVO.setNumeroLineas(2);
		
		WrapperResultados resultado = new WrapperResultados();
		
		resultado = productoManager.agregarClausula(clausulaVO, success);
		mensaje = resultado.getMsgText();
		
		if(resultado.getMsgTitle().equals("2")){
			success = true;
		}
		log.debug("resultado.getMsgText==" + resultado.getMsgText());
		log.debug("resultado.getMsgTitle==" + resultado.getMsgTitle());
		
		catalogoDeClausulasJSON();
		return SUCCESS;
	}

	public String eliminarClausula() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		String[] separaClave= claveCatalogoClausulas.split(",");
		if(isDebugEnabled){
			log.debug("Uno"+separaClave[0]);
		}
		claveCatalogoClausulas= separaClave[0];
		ClausulaVO clausulaVO = new ClausulaVO();
		clausulaVO.setDescripcionClausula(claveCatalogoClausulas);
		List<ClausulaVO> catalogo = (List<ClausulaVO>)session.get("CLAUSULAS");
		int clausulaRemover = -1;
		int contador=0;
		for (ClausulaVO clau : catalogo) {
			if (clau.getCodigoClausula().equals(claveCatalogoClausulas)) {
				clausulaVO.setCodigoClausula(clau.getCodigoClausula());
				clausulaRemover = contador;
				
			}
			contador++;
		}
		if (clausulaRemover != -1) {
			catalogo.remove(clausulaRemover);
			session.put("CLAUSULAS", catalogo);	
			List<ClausulaVO> temp = (List<ClausulaVO>) session.get("CATALOGO_CLAUSULAS");
			temp.add(clausulaVO);
			clausulas = temp;
			session.put("CATALOGO_CLAUSULAS", temp);
		}
		success=true;
		return SUCCESS;
	}

	/**
	 * Metodo <code>listaDeClausualasJSON</code> con el que son llamado desde
	 * Struts todas las clausulas asociadas a un producto.
	 * 
	 * @return success
	 * @throws Exception
	 */
	public String listaDeClausulasJSON() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("clausulas json");
		}
//		if(session.containsKey("CODIGO_NIVEL0"))
//			idProducto=(String) session.get("CODIGO_NIVEL0");
//		else
//			idProducto="1";
		if (!session.containsKey("CLAUSULAS")) {
//			if (idProducto != null) {
//				clausulas = productoManager.clausulasJson(Integer.parseInt(idProducto));
//				log.debug("algo no repetido");
//			} else {
				clausulas = new ArrayList<ClausulaVO>();
//			}
			session.put("CLAUSULAS", clausulas);
		} else {
			clausulas = (List<ClausulaVO>) session.get("CLAUSULAS");
		}
		success = true;
		return SUCCESS;
	}

	// llenado de catalogos
	public String catalogoDeClausulasJSON() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("catalogo json");
		}
		List<ClausulaVO> catalogoARemover = null;
		if (session.containsKey("CATALOGO_CLAUSULAS")) {
//			clausulasCatalogo = productoManager.catalogoClausulasJson();
//			session.put("CATALOGO_CLAUSULAS", clausulasCatalogo);
			if(isDebugEnabled){
				log.debug("banderaEditar= " + banderaEditar);
			}
			if (banderaEditar!=null && banderaEditar.equals("1")) {
				//clausulas = productoManager.clausulasJson(Integer.parseInt(idProducto));
				if(session.containsKey("CLAUSULAS") && !((List<ClausulaVO>) session.get("CLAUSULAS")).isEmpty()
						&& !((List<ClausulaVO>)session.get("CATALOGO_CLAUSULAS")).isEmpty()){
					List<ClausulaVO> clausulasAsociadas = (List<ClausulaVO>) session.get("CLAUSULAS");
					clausulasCatalogo = productoManager.catalogoClausulasJson();
					if(!clausulasCatalogo.isEmpty()){
						catalogoARemover = new ArrayList<ClausulaVO>();
    					for(ClausulaVO clausulaAsociada:clausulasAsociadas){
    						for(ClausulaVO clausulaEnCatalogo:clausulasCatalogo){
    							if(clausulaAsociada.getCodigoClausula().equals(clausulaEnCatalogo.getCodigoClausula()))
    								catalogoARemover.add(clausulaEnCatalogo);							
    						}
    					}
    					clausulasCatalogo.removeAll(catalogoARemover);
					}
				}
				
			} else {
				clausulasCatalogo = (List<ClausulaVO>) session.get("CATALOGO_CLAUSULAS");
			}
			
		} else {
			clausulasCatalogo = productoManager.catalogoClausulasJson();
		}
		session.put("CATALOGO_CLAUSULAS", clausulasCatalogo);
		success = true;
		return SUCCESS;
	}

	public String catalogoTipoProductoJSON() throws Exception {
		if (!session.containsKey("CATALOGO_TIPO_PRODUCTO")) {
			catalogoTipoProducto = productoManager.catalogoTipoProductoJson();
			session.put("CATALOGO_TIPO_PRODCUTO", catalogoTipoProducto);
		} else {
			catalogoTipoProducto = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_PRODUCTO");
		}
		success = true;
		return SUCCESS;
	}

	public String catalogoTipoRamoJSON() throws Exception {
		if (!session.containsKey("CATALOGO_TIPO_RAMO")) {
			catalogoTipoRamo = productoManager.catalogoTipoRamoJson();
			session.put("CATALOGO_TIPO_RAMO", catalogoTipoRamo);
		} else {
			catalogoTipoRamo = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_RAMO");
		}
		success = true;
		return SUCCESS;
	}

	public String catalogoTipoPolizaJSON() throws Exception {
		boolean isDebugEnabled = log.isDebugEnabled();
		if (!session.containsKey("CATALOGO_TIPO_POLIZA")) {
			catalogoTipoPoliza = productoManager.catalogoTipoPolizaJson();
			session.put("CATALOGO_TIPO_POLIZA", catalogoTipoPoliza);
		} else {
			catalogoTipoPoliza = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_POLIZA");
		}
		if(isDebugEnabled){
			log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@esta en catalogoTipoPolizaJSON"
				+ catalogoTipoPoliza);
		}
		success = true;
		return SUCCESS;
	}

	public String catalogoTipoSeguroJSON() throws Exception {
		if (!session.containsKey("CATALOGO_TIPO_SEGURO")) {
			catalogoTipoSeguro = productoManager.catalogoTipoSeguroJson();
			session.put("CATALOGO_TIPO_SEGURO", catalogoTipoSeguro);
		} else {
			catalogoTipoSeguro = (List<LlaveValorVO>) session.get("CATALOGO_TIPO_SEGURO");
		}
		success = true;
		return SUCCESS;
	}

	public String clearSession() {
		//session.remove("CLAUSULAS");
		session.remove("CATALOGO_CLAUSULAS");
		session.remove("PERIODOS");
		success = true;
		return SUCCESS;
	}
    
    public String clearClausulasSession() {
        session.remove("CLAUSULAS");
        session.remove("CATALOGO_CLAUSULAS");
        session.remove("PERIODOS");
        success = true;
        return SUCCESS;
    }
	
	public String generarProducto() {
		
		try {
			mensaje = productoManager.generarProducto(codigoRamo + "");
		} catch (ApplicationException e) {
			log.error("Error al generar el producto: " + e.getMessage());
			mensaje = e.getMessage();
			success = false;
			return SUCCESS;
		}
		
		success = true;
		return SUCCESS;
	}
	
	public String generarProducto2() {
		
		log.debug(Utils.log(
				 "\n###########################################"
				,"\n###### generarProducto2 ######"
				,"\n###### codigoRamo=",codigoRamo
				));
		try {
			mensaje = productoManager.generarProducto(codigoRamo + "",dominioServerLayouts,userServerLayouts,passServerLayouts); 
			mensaje = productoManager.generarProducto(codigoRamo + "",dominioServerLayouts2,userServerLayouts,passServerLayouts); 
		} catch (ApplicationException e) {
			log.error("Error al generar el producto: " + e.getMessage());
			mensaje = e.getMessage();
			success = false;
			return SUCCESS;
		}
		log.debug(Utils.log(
				"\n###### generarProducto2 ######"
				,"\n###########################################"
				));
		
		success = true;
		return SUCCESS;
	}
	
	public String clonarProducto() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("-> PrincipalProductosAction.clonarProducto");
		}
		
		UserVO usuario = (UserVO)session.get("USUARIO");
		
		try {
			mensaje = productoManager.clonarProducto(codigoRamo + "", descripcionRamo, "1", usuario.getUser());
			success = true;
			session.remove("ARBOL_PRODUCTOS");
		} catch (ApplicationException ae) {
			log.error("-> ApplicationException al ejecutar clonarProducto", ae);
			mensaje = ae.getMessage();
			success = false;
		}
		
		if (log.isDebugEnabled()) {
			log.debug(".. mensaje : " + mensaje);
		}
		
		return SUCCESS;
	}

	// Getters and Setters

	// Metodo de acceso utilizado por Spring para agregar el manager
	public void setProductoManager(ProductoManager productoManager) {
		this.productoManager = productoManager;
	}

	// Metodos de acceso de los atributos agregados por struts con los
	// parametros de la consulta
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<ClausulaVO> getClausulas() {
		return clausulas;
	}

	public void setClausulas(List<ClausulaVO> clausulas) {
		this.clausulas = clausulas;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClausulaDescripcion() {
		return clausulaDescripcion;
	}

	public void setClausulaDescripcion(String clausulaDescripcion) {
		this.clausulaDescripcion = clausulaDescripcion;
	}

	public List<PeriodoVO> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<PeriodoVO> periodos) {
		this.periodos = periodos;
	}

	public String getLinea() {
		return linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public String getIdClausula() {
		return idClausula;
	}

	public void setIdClausula(String idClausula) {
		this.idClausula = idClausula;
	}

	public List<LlaveValorVO> getCombos() {
		return combos;
	}

	public void setCombos(List<LlaveValorVO> combos) {
		this.combos = combos;
	}

	public String getCombo() {
		return combo;
	}

	public void setCombo(String combo) {
		this.combo = combo;
	}

	public List<LlaveValorVO> getCombosHijo() {
		return combosHijo;
	}

	public void setCombosHijo(List<LlaveValorVO> combosHijo) {
		this.combosHijo = combosHijo;
	}

	public List<LlaveValorVO> getCombosNieto() {
		return combosNieto;
	}

	public void setCombosNieto(List<LlaveValorVO> combosNieto) {
		this.combosNieto = combosNieto;
	}

	public String getComboHijo() {
		return comboHijo;
	}

	public void setComboHijo(String comboHijo) {
		this.comboHijo = comboHijo;
	}

	public String getComboNieto() {
		return comboNieto;
	}

	public void setComboNieto(String comboNieto) {
		this.comboNieto = comboNieto;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDescripcionRamo() {
		return descripcionRamo;
	}

	public void setDescripcionRamo(String descripcionRamo) {
		this.descripcionRamo = descripcionRamo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigoTipoRamo() {
		return codigoTipoRamo;
	}

	public void setCodigoTipoRamo(String codigoTipoRamo) {
		this.codigoTipoRamo = codigoTipoRamo;
	}

	public String getDescripcionTipoRamo() {
		return descripcionTipoRamo;
	}

	public void setDescripcionTipoRamo(String descripcionTipoRamo) {
		this.descripcionTipoRamo = descripcionTipoRamo;
	}

	public String getSwitchSuscripcion() {
		return switchSuscripcion;
	}

	public void setSwitchSuscripcion(String switchSuscripcion) {
		this.switchSuscripcion = switchSuscripcion;
	}

	public String getSwitchClausulasNoTipificadas() {
		return switchClausulasNoTipificadas;
	}

	public void setSwitchClausulasNoTipificadas(
			String switchClausulasNoTipificadas) {
		this.switchClausulasNoTipificadas = switchClausulasNoTipificadas;
	}

	public String getSwitchRehabilitacion() {
		return switchRehabilitacion;
	}

	public void setSwitchRehabilitacion(String switchRehabilitacion) {
		this.switchRehabilitacion = switchRehabilitacion;
	}

	public String getSwitchPrimasPeriodicas() {
		return switchPrimasPeriodicas;
	}

	public void setSwitchPrimasPeriodicas(String switchPrimasPeriodicas) {
		this.switchPrimasPeriodicas = switchPrimasPeriodicas;
	}

	public String getSwitchPermisoPagosOtrasMonedas() {
		return switchPermisoPagosOtrasMonedas;
	}

	public void setSwitchPermisoPagosOtrasMonedas(
			String switchPermisoPagosOtrasMonedas) {
		this.switchPermisoPagosOtrasMonedas = switchPermisoPagosOtrasMonedas;
	}

	public String getSwitchReaseguro() {
		return switchReaseguro;
	}

	public void setSwitchReaseguro(String switchReaseguro) {
		this.switchReaseguro = switchReaseguro;
	}

	public String getSwitchSiniestros() {
		return switchSiniestros;
	}

	public void setSwitchSiniestros(String switchSiniestros) {
		this.switchSiniestros = switchSiniestros;
	}

	public String getSwitchTarifa() {
		return switchTarifa;
	}

	public void setSwitchTarifa(String switchTarifa) {
		this.switchTarifa = switchTarifa;
	}

	public String getSwitchReinstalacionAutomatica() {
		return switchReinstalacionAutomatica;
	}

	public void setSwitchReinstalacionAutomatica(
			String switchReinstalacionAutomatica) {
		this.switchReinstalacionAutomatica = switchReinstalacionAutomatica;
	}

	public String getSwitchPrimasUnicas() {
		return switchPrimasUnicas;
	}

	public void setSwitchPrimasUnicas(String switchPrimasUnicas) {
		this.switchPrimasUnicas = switchPrimasUnicas;
	}

	public String getSwitchDistintasPolizasPorAsegurado() {
		return switchDistintasPolizasPorAsegurado;
	}

	public void setSwitchDistintasPolizasPorAsegurado(
			String switchDistintasPolizasPorAsegurado) {
		this.switchDistintasPolizasPorAsegurado = switchDistintasPolizasPorAsegurado;
	}

	public String getSwitchPolizasDeclarativas() {
		return switchPolizasDeclarativas;
	}

	public void setSwitchPolizasDeclarativas(String switchPolizasDeclarativas) {
		this.switchPolizasDeclarativas = switchPolizasDeclarativas;
	}

	public String getSwitchPreavisoCartera() {
		return switchPreavisoCartera;
	}

	public void setSwitchPreavisoCartera(String switchPreavisoCartera) {
		this.switchPreavisoCartera = switchPreavisoCartera;
	}

	public String getSwitchTipo() {
		return switchTipo;
	}

	public void setSwitchTipo(String switchTipo) {
		this.switchTipo = switchTipo;
	}

	public String getSwitchTarifaDireccionalTotal() {
		return switchTarifaDireccionalTotal;
	}

	public void setSwitchTarifaDireccionalTotal(
			String switchTarifaDireccionalTotal) {
		this.switchTarifaDireccionalTotal = switchTarifaDireccionalTotal;
	}

	public String getSwitchSubincisos() {
		return switchSubincisos;
	}

	public void setSwitchSubincisos(String switchSubincisos) {
		this.switchSubincisos = switchSubincisos;
	}

	public String getCodigoTipoParametro() {
		return codigoTipoParametro;
	}

	public void setCodigoTipoParametro(String codigoTipoParametro) {
		this.codigoTipoParametro = codigoTipoParametro;
	}

	public String getDescripcionTipoParametro() {
		return descripcionTipoParametro;
	}

	public void setDescripcionTipoParametro(String descripcionTipoParametro) {
		this.descripcionTipoParametro = descripcionTipoParametro;
	}

	public String getRenovable() {
		return renovable;
	}

	public void setRenovable(String renovable) {
		this.renovable = renovable;
	}

	public String getTemporal() {
		return temporal;
	}

	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}

	public String getVidaEntera() {
		return vidaEntera;
	}

	public void setVidaEntera(String vidaEntera) {
		this.vidaEntera = vidaEntera;
	}

	public List<ClausulaVO> getClausulasCatalogo() {
		return clausulasCatalogo;
	}

	public void setClausulasCatalogo(List<ClausulaVO> clausulasCatalogo) {
		this.clausulasCatalogo = clausulasCatalogo;
	}

	public String getClaveCatalogoClausulas() {
		return claveCatalogoClausulas;
	}

	public void setClaveCatalogoClausulas(String claveCatalogoClausulas) {
		this.claveCatalogoClausulas = claveCatalogoClausulas;
	}

	public int getTipoCalculoPolizasTemporales() {
		return tipoCalculoPolizasTemporales;
	}

	public void setTipoCalculoPolizasTemporales(int tipoCalculoPolizasTemporales) {
		this.tipoCalculoPolizasTemporales = tipoCalculoPolizasTemporales;
	}

	public int getMesesBeneficios() {
		return mesesBeneficios;
	}

	public void setMesesBeneficios(int mesesBeneficios) {
		this.mesesBeneficios = mesesBeneficios;
	}

	public int getCantidadDiasReclamacion() {
		return cantidadDiasReclamacion;
	}

	public void setCantidadDiasReclamacion(int cantidadDiasReclamacion) {
		this.cantidadDiasReclamacion = cantidadDiasReclamacion;
	}

	public void setCodigoRamo(int codigoRamo) {
		this.codigoRamo = codigoRamo;
	}

	public int getCodigoRamo() {
		return codigoRamo;
	}

	public String getClaveTipoProducto() {
		return claveTipoProducto;
	}

	public void setClaveTipoProducto(String claveTipoProducto) {
		this.claveTipoProducto = claveTipoProducto;
	}

	public String getClaveTipoRamo() {
		return claveTipoRamo;
	}

	public void setClaveTipoRamo(String claveTipoRamo) {
		this.claveTipoRamo = claveTipoRamo;
	}

	public String getClaveTipoPoliza() {
		return claveTipoPoliza;
	}

	public void setClaveTipoPoliza(String claveTipoPoliza) {
		this.claveTipoPoliza = claveTipoPoliza;
	}

	public String getClaveTipoSeguro() {
		return claveTipoSeguro;
	}

	public void setClaveTipoSeguro(String claveTipoSeguro) {
		this.claveTipoSeguro = claveTipoSeguro;
	}

	public List<LlaveValorVO> getCatalogoTipoProducto() {
		return catalogoTipoProducto;
	}

	public void setCatalogoTipoProducto(List<LlaveValorVO> catalogoTipoProducto) {
		this.catalogoTipoProducto = catalogoTipoProducto;
	}

	public List<LlaveValorVO> getCatalogoTipoRamo() {
		return catalogoTipoRamo;
	}

	public void setCatalogoTipoRamo(List<LlaveValorVO> catalogoTipoRamo) {
		this.catalogoTipoRamo = catalogoTipoRamo;
	}

	public List<LlaveValorVO> getCatalogoTipoPoliza() {
		return catalogoTipoPoliza;
	}

	public void setCatalogoTipoPoliza(List<LlaveValorVO> catalogoTipoPoliza) {
		this.catalogoTipoPoliza = catalogoTipoPoliza;
	}

	public List<LlaveValorVO> getCatalogoTipoSeguro() {
		return catalogoTipoSeguro;
	}

	public void setCatalogoTipoSeguro(List<LlaveValorVO> catalogoTipoSeguro) {
		this.catalogoTipoSeguro = catalogoTipoSeguro;
	}

	public ClausulaVO getC() {
		return c;
	}

	public void setC(ClausulaVO c) {
		this.c = c;
	}

	public BoxingVO getBvo() {
		return bvo;
	}

	public void setBvo(BoxingVO bvo) {
		this.bvo = bvo;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	/**
	 * @return the valorTipoProducto
	 */
	public String getValorTipoProducto() {
		return valorTipoProducto;
	}

	/**
	 * @param valorTipoProducto
	 *            the valorTipoProducto to set
	 */
	public void setValorTipoProducto(String valorTipoProducto) {
		this.valorTipoProducto = valorTipoProducto;
	}

	/**
	 * @return the valorTipoRamo
	 */
	public String getValorTipoRamo() {
		return valorTipoRamo;
	}

	/**
	 * @param valorTipoRamo
	 *            the valorTipoRamo to set
	 */
	public void setValorTipoRamo(String valorTipoRamo) {
		this.valorTipoRamo = valorTipoRamo;
	}

	/**
	 * @return the valorTipoPoliza
	 */
	public String getValorTipoPoliza() {
		return valorTipoPoliza;
	}

	/**
	 * @param valorTipoPoliza
	 *            the valorTipoPoliza to set
	 */
	public void setValorTipoPoliza(String valorTipoPoliza) {
		this.valorTipoPoliza = valorTipoPoliza;
	}

	/**
	 * @return the valorTipoSeguro
	 */
	public String getValorTipoSeguro() {
		return valorTipoSeguro;
	}

	/**
	 * @param valorTipoSeguro
	 *            the valorTipoSeguro to set
	 */
	public void setValorTipoSeguro(String valorTipoSeguro) {
		this.valorTipoSeguro = valorTipoSeguro;
	}

	/**
	 * @return the polizasPermitidas
	 */
	public List<String> getPolizasPermitidas() {
		return polizasPermitidas;
	}

	/**
	 * @param polizasPermitidas
	 *            the polizasPermitidas to set
	 */
	public void setPolizasPermitidas(List<String> polizasPermitidas) {
		this.polizasPermitidas = polizasPermitidas;
	}

	/**
	 * @param treeManager
	 *            the treeManager to set
	 */
	public void setTreeManager(TreeManager treeManager) {
		this.treeManager = treeManager;
	}

	/**
	 * @return the codigoPeriodo
	 */
	public String getCodigoPeriodo() {
		return codigoPeriodo;
	}

	/**
	 * @param codigoPeriodo the codigoPeriodo to set
	 */
	public void setCodigoPeriodo(String codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}

	/**
	 * @return the editarProducto
	 */
	public List<ProductoVO> getEditarProducto() {
		return editarProducto;
	}

	/**
	 * @param editarProducto the editarProducto to set
	 */
	public void setEditarProducto(List<ProductoVO> editarProducto) {
		this.editarProducto = editarProducto;
	}

	/**
	 * @return the banderaEditar
	 */
	public String getBanderaEditar() {
		return banderaEditar;
	}

	/**
	 * @param banderaEditar the banderaEditar to set
	 */
	public void setBanderaEditar(String banderaEditar) {
		this.banderaEditar = banderaEditar;
	}

	/**
	 * @return the switchTipoDagnos
	 */
	public String getSwitchTipoDagnos() {
		return switchTipoDagnos;
	}

	/**
	 * @param switchTipoDagnos the switchTipoDagnos to set
	 */
	public void setSwitchTipoDagnos(String switchTipoDagnos) {
		this.switchTipoDagnos = switchTipoDagnos;
	}

	/**
	 * @return the switchTipoOtro
	 */
	public String getSwitchTipoOtro() {
		return switchTipoOtro;
	}

	/**
	 * @param switchTipoOtro the switchTipoOtro to set
	 */
	public void setSwitchTipoOtro(String switchTipoOtro) {
		this.switchTipoOtro = switchTipoOtro;
	}

	/**
	 * @return the codigoRamoEditar
	 */
	public String getCodigoRamoEditar() {
		return codigoRamoEditar;
	}

	/**
	 * @param codigoRamoEditar the codigoRamoEditar to set
	 */
	public void setCodigoRamoEditar(String codigoRamoEditar) {
		this.codigoRamoEditar = codigoRamoEditar;
	}

	/**
	 * @return the codigoSwitchTipo
	 */
	public String getCodigoSwitchTipo() {
		return codigoSwitchTipo;
	}

	/**
	 * @param codigoSwitchTipo the codigoSwitchTipo to set
	 */
	public void setCodigoSwitchTipo(String codigoSwitchTipo) {
		this.codigoSwitchTipo = codigoSwitchTipo;
	}

	public String getSwitchCancelacion() {
		return switchCancelacion;
	}

	public void setSwitchCancelacion(String switchCancelacion) {
		this.switchCancelacion = switchCancelacion;
	}

    public String getSwitchEndosos() {
		return switchEndosos;
	}

	public void setSwitchEndosos(String switchEndosos) {
		this.switchEndosos = switchEndosos;
	}

	/**
     * @return the habilita
     */
    public boolean isHabilita() {
        return habilita;
    }

    /**
     * @param habilita the habilita to set
     */
    public void setHabilita(boolean habilita) {
        this.habilita = habilita;
    }

}
