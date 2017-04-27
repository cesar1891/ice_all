package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TablaCincoClavesManager;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionVeinticincoAtributosVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.ValoresCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.util.WizardUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author Edgar Perez
 * @version 2.0
 * @since 1.0
 * 
 * Clase que implementa los metodos para agregar y obtener 
 * las los valores de la tabla de cinco claves asociadas a un producto
 *
 */
public class TablaCincoClavesManagerImpl implements TablaCincoClavesManager {

	private static Logger logger = Logger.getLogger(TablaCincoClavesManagerImpl.class);

	
	/**
	 * Implementacion que extrae las descripciones de la tabla
	 * de cinco claves.
	 * 
	 * @return cincoClaves DescripcionCincoClavesVO - objeto con la informacion de las
	 *         claves asociadas a la tabla de cinco claves.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public DescripcionCincoClavesVO obtieneDescripcionCincoClaves(String nmTabla)
			throws ApplicationException {
		//Endpoint endpoint = (Endpoint) endpoints.get("OBTIENE_DESCRIPCION_CINCO_CLAVES");
        DescripcionCincoClavesVO cincoClaves = null;
        try {
        	//cincoClaves = (DescripcionCincoClavesVO) endpoint.invoke(nmTabla);
        
        } catch (Exception e) {
            throw new ApplicationException("Error regresando descripcion de cinco claves");
        }
        return cincoClaves;
	}
	
	/**
	 * Implementacion que extrae las descripciones de los atributos de la tabla
	 * de cinco claves.
	 * 
	 * @return atributos List<LlaveValorVO> - lista de objetos LlaveValorVO con la informacion de los
	 *         atributos asociados a la claves para la tabla de cinco claves.
	 *         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public DescripcionVeinticincoAtributosVO obtieneDescripcionAtributosCincoClaves(String nmTabla)
			throws ApplicationException {
		
		List<LlaveValorVO> atributos = null;
		DescripcionVeinticincoAtributosVO descripcionAtributos= new DescripcionVeinticincoAtributosVO();
		//Endpoint endpoint = (Endpoint) endpoints.get("OBTIENE_DESCRIPCION_ATRIBUTOS_CINCO_CLAVES");
		
		try {
			//atributos = (List<LlaveValorVO>) endpoint.invoke(nmTabla);
			if(atributos != null){
				
				for(LlaveValorVO descAtributo :atributos){
					
					logger.debug("vaLOR DE LA LLAVE"+ descAtributo);	
					if(StringUtils.isNotBlank(descAtributo.getKey()) && StringUtils.isNotBlank(descAtributo.getValue())){
						
						switch(Integer.parseInt(descAtributo.getKey().trim())){
							case 1 : descripcionAtributos.setDescripcion1(descAtributo.getValue()); break;
							case 2 : descripcionAtributos.setDescripcion2(descAtributo.getValue()); break;
							case 3 : descripcionAtributos.setDescripcion3(descAtributo.getValue()); break;
							case 4 : descripcionAtributos.setDescripcion4(descAtributo.getValue()); break;
							case 5 : descripcionAtributos.setDescripcion5(descAtributo.getValue()); break;
							case 6 : descripcionAtributos.setDescripcion6(descAtributo.getValue()); break;
							case 7 : descripcionAtributos.setDescripcion7(descAtributo.getValue()); break;
							case 8 : descripcionAtributos.setDescripcion8(descAtributo.getValue()); break;
							case 9 : descripcionAtributos.setDescripcion9(descAtributo.getValue()); break;
							case 10 : descripcionAtributos.setDescripcion10(descAtributo.getValue()); break;
							case 11 : descripcionAtributos.setDescripcion11(descAtributo.getValue()); break;
							case 12 : descripcionAtributos.setDescripcion12(descAtributo.getValue()); break;
							case 13 : descripcionAtributos.setDescripcion13(descAtributo.getValue()); break;
							case 14 : descripcionAtributos.setDescripcion14(descAtributo.getValue()); break;
							case 15 : descripcionAtributos.setDescripcion15(descAtributo.getValue()); break;
							case 16 : descripcionAtributos.setDescripcion16(descAtributo.getValue()); break;
							case 17 : descripcionAtributos.setDescripcion17(descAtributo.getValue()); break;
							case 18 : descripcionAtributos.setDescripcion18(descAtributo.getValue()); break;
							case 19 : descripcionAtributos.setDescripcion19(descAtributo.getValue()); break;
							case 20 : descripcionAtributos.setDescripcion20(descAtributo.getValue()); break;
							case 21 : descripcionAtributos.setDescripcion21(descAtributo.getValue()); break;
							case 22 : descripcionAtributos.setDescripcion22(descAtributo.getValue()); break;
							case 23 : descripcionAtributos.setDescripcion23(descAtributo.getValue()); break;
							case 24 : descripcionAtributos.setDescripcion24(descAtributo.getValue()); break;
							case 25 : descripcionAtributos.setDescripcion25(descAtributo.getValue()); break;
							
						}
					}
				}
			}

		} catch (Exception e) {
			throw new ApplicationException("Error regresando atributos de cinco claves");
		}
		
		if(logger.isDebugEnabled())logger.debug("Los 25Atributos son " + descripcionAtributos);
		return descripcionAtributos;
	}

	
	public List<DescripcionCincoClavesVO> insertaValoresCincoClaves(String nmTabla , List<ValoresCincoClavesVO> listaValores5Claves) throws ApplicationException {
		List<DescripcionCincoClavesVO> clavesNoInsertadas = new ArrayList<DescripcionCincoClavesVO>();
		Map params = new HashMap<String, String>();
		params.put("nmTabla", nmTabla);
		 		
		for(ValoresCincoClavesVO clavesVO : listaValores5Claves ){
			WrapperResultados res = null;
			params.put("valores", clavesVO);
				
			try {
				if(logger.isDebugEnabled())logger.debug("params==>" + params);
				
				if(res != null && StringUtils.isNotBlank(res.getMsgId())){
					if(res.getMsgId().equals(Constantes.REGISTRO_DUPLICADO)){
						DescripcionCincoClavesVO claveRep = new DescripcionCincoClavesVO();
						claveRep.setDescripcionClave1(clavesVO.getDescripcionCincoClaves().getDescripcionClave1());
						claveRep.setDescripcionClave2(clavesVO.getDescripcionCincoClaves().getDescripcionClave2());
						claveRep.setDescripcionClave3(clavesVO.getDescripcionCincoClaves().getDescripcionClave3());
						claveRep.setDescripcionClave4(clavesVO.getDescripcionCincoClaves().getDescripcionClave4());
						claveRep.setDescripcionClave5(clavesVO.getDescripcionCincoClaves().getDescripcionClave5());
						claveRep.setFechaDesde(clavesVO.getFechaDesde());
						claveRep.setFechaHasta(clavesVO.getFechaHasta());
						clavesNoInsertadas.add(claveRep);
					}
				}
				
		
			} catch (Exception e) {
				logger.error("Error al insertar en TablaCincoClaves::" + e.getMessage() );
				throw new ApplicationException("Error al guardar la tabla de cinco claves. Consulte a su soporte");
			}		
		}
		
		return clavesNoInsertadas;
	}
	/*
	public ValoresCincoClavesVO obtieneValoresCincoClaves(String nmTabla,
			String tipoTabla) throws ApplicationException {
		Map params = new HashMap<String, String>();
 		params.put("nmTabla", nmTabla);
 		params.put("tipoTabla", tipoTabla);
		Endpoint endpoint = (Endpoint) endpoints.get("OBTIENE_VALORES_CINCO_CLAVES");
		ValoresCincoClavesVO valores5Claves = null;
		try {
			valores5Claves = (ValoresCincoClavesVO) endpoint.invoke(params);

		} catch (DaoException e) {
			throw new ApplicationException("Error regresando atributos de cinco claves");
		}
		return valores5Claves;
	}
	*/
	
	/**
	 * Implementacion que extrae los valores de las descripciones de la tabla
	 * de cinco claves.
	 * 
	 * @return listaClaves List<DescripcionCincoClavesVO> - lista de objetos DescripcionCincoClavesVO con la informacion de las
	 *         descripciones asociados a la claves para la tabla de cinco claves.
	 * @param numeroTabla - numero de tabla        
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<DescripcionCincoClavesVO> obtieneValoresClave(String nmTabla)throws ApplicationException {
		//Endpoint endpoint = (Endpoint) endpoints.get("OBTIENE_VALORES_CLAVE");
		List<DescripcionCincoClavesVO> listaClaves = null;
		try {
			//listaClaves = (List<DescripcionCincoClavesVO>) endpoint.invoke(nmTabla);
					if(listaClaves!=null && !listaClaves.isEmpty()){
		    int identifica=0;
		    for(DescripcionCincoClavesVO dccVO : listaClaves){
		     dccVO.setIdentificador(Integer.toString(identifica++));
		     try {
				dccVO.setFechaDesde(WizardUtils.parseDateBaseCincoClaves(dccVO.getFechaDesde()));
				dccVO.setFechaHasta(WizardUtils.parseDateBaseCincoClaves(dccVO.getFechaHasta()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     //List<LlaveValorVO> atributos = new ArrayList<LlaveValorVO>();
		     //atributos = obtieneValoresAtributos(nmTabla,dccVO);
		     //dccVO.setAtributos(atributos);
		    }
		}
		} catch (Exception e) {
			throw new ApplicationException("Error regresando claves para la tabla de cinco claves");
		}
		return listaClaves;
	}
	
	/**
	 * Implementacion que extrae los valores de los atributos de la tabla
	 * de cinco claves.
	 * 
	 * @return atributos DescripcionVeinticincoAtributosVO - objeto DescripcionVeinticincoAtributosVO con la informacion de los
	 *         atributos asociados a la claves para la tabla de cinco claves.
	 *         
	 * @param numeroTabla - numero de tabla
	 * @param claves - objeto DescripcionCincoClaves claves del los atributos a seleccionar         
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.
	 */
	public List<LlaveValorVO> obtieneValoresAtributos(String nmTabla,
			DescripcionCincoClavesVO claves, DescripcionVeinticincoAtributosVO descripcionAtributos)throws ApplicationException {
		
			/*logger.debug("$$$IDENTIFICADOR DE CLAVE="+claves.getIdentificador());
			logger.debug("$$$numero de tabla="+nmTabla);
			logger.debug("$$$getDescripcionClave1="+claves.getDescripcionClave1());
			logger.debug("$$$getDescripcionClave2="+claves.getDescripcionClave2());
			logger.debug("$$$getDescripcionClave3="+claves.getDescripcionClave3());
			logger.debug("$$$getDescripcionClave4="+claves.getDescripcionClave4());
			logger.debug("$$$getDescripcionClave5="+claves.getDescripcionClave5());
			logger.debug("$$$getFechaDesde="+claves.getFechaDesde());
			logger.debug("$$$getFechaDesde="+claves.getFechaHasta());
			*/
		
		Map params = new HashMap<String, String>();
 		params.put("nmTabla", nmTabla);
 		params.put("claves", claves);
 		
		
		DescripcionVeinticincoAtributosVO atributos = null;
		List<LlaveValorVO> listaAtributos = null;
		try {
		
			listaAtributos = new ArrayList<LlaveValorVO>();
			listaAtributos = convierteAListaLVVO(claves.getIdentificador(), descripcionAtributos, atributos);
			
		} catch (Exception e) {
			throw new ApplicationException("Error regresando atributos para la tabla de cinco claves");
		}
		
		return listaAtributos;
	}
	//***********************conversion de objetos a lista y viceversa*******************************
	
	/**
	 * Implementacion que convierte List<DatosClaveAtributoVO> a ClavesVO 
	 * 
	 * @return clavesVO ClavesVO - objeto ClavesVO con la informacion de las
	 *         claves asociadas a la  tabla de cinco claves.
	 *         
	 * @param datosClaveAtributo - List<DatosClaveAtributoVO>
	 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.        
	 */
	 public ClavesVO convierteAClaveVO(List<DatosClaveAtributoVO> dca){
		  ClavesVO clavesVO= new ClavesVO();  
		  
		  if(dca!=null && !dca.isEmpty()){
		   for(DatosClaveAtributoVO dcaVO: dca){
		    switch(dcaVO.getNumeroClave()){
		    case 1:
		     clavesVO.setClave(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave(dcaVO.getFormato());
		     clavesVO.setMaximoClave(dcaVO.getMaximo());
		     clavesVO.setMinimoClave(dcaVO.getMinimo());
		     break;
		    case 2:
		     clavesVO.setClave2(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave2(dcaVO.getFormato());
		     clavesVO.setMaximoClave2(dcaVO.getMaximo());
		     clavesVO.setMinimoClave2(dcaVO.getMinimo());
		     break; 
		    case 3:
		     clavesVO.setClave3(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave3(dcaVO.getFormato());
		     clavesVO.setMaximoClave3(dcaVO.getMaximo());
		     clavesVO.setMinimoClave3(dcaVO.getMinimo());
		     break;
		    case 4:
		     clavesVO.setClave4(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave4(dcaVO.getFormato());
		     clavesVO.setMaximoClave4(dcaVO.getMaximo());
		     clavesVO.setMinimoClave4(dcaVO.getMinimo());
		     break;
		    case 5:
		     clavesVO.setClave5(dcaVO.getDescripcion());
		     clavesVO.setFormatoClave5(dcaVO.getFormato());
		     clavesVO.setMaximoClave5(dcaVO.getMaximo());
		     clavesVO.setMinimoClave5(dcaVO.getMinimo());
		     break;
		    }
		    
		   }
		  }
		  return clavesVO;
		 }
	 
	 /**
		 * Implementacion que convierte ClavesVO a List<DatosClaveAtributoVO> 
		 * 
		 * @return listaDatosClaveAtributo - List<DatosClaveAtributoVO>  
		 *         
		 * @param clavesVO ClavesVO - objeto ClavesVO   
		 * @throws ApplicationException -
	 *             Es lanzada en errores de configuracion de aplicacion error en
	 *             las consultas a BD.          
		 */
	 public List<DatosClaveAtributoVO> convierteAListaDCAVO(ClavesVO clavesVO){
		  List<DatosClaveAtributoVO> ldcaVO= new ArrayList<DatosClaveAtributoVO>();
		  DatosClaveAtributoVO dcaVO=null;
		  if(clavesVO.getClave()!=null && StringUtils.isNotBlank(clavesVO.getClave())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(1);
		   dcaVO.setDescripcion(clavesVO.getClave());
		   dcaVO.setFormato(clavesVO.getFormatoClave());
		   dcaVO.setMaximo(clavesVO.getMaximoClave());
		   dcaVO.setMinimo(clavesVO.getMinimoClave());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave2()!=null && StringUtils.isNotBlank(clavesVO.getClave2())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(2);
		   dcaVO.setDescripcion(clavesVO.getClave2());
		   dcaVO.setFormato(clavesVO.getFormatoClave2());
		   dcaVO.setMaximo(clavesVO.getMaximoClave2());
		   dcaVO.setMinimo(clavesVO.getMinimoClave2());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave3()!=null && StringUtils.isNotBlank(clavesVO.getClave3())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(3);
		   dcaVO.setDescripcion(clavesVO.getClave3());
		   dcaVO.setFormato(clavesVO.getFormatoClave3());
		   dcaVO.setMaximo(clavesVO.getMaximoClave3());
		   dcaVO.setMinimo(clavesVO.getMinimoClave3());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave4()!=null && StringUtils.isNotBlank(clavesVO.getClave4())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(4);
		   dcaVO.setDescripcion(clavesVO.getClave4());
		   dcaVO.setFormato(clavesVO.getFormatoClave4());
		   dcaVO.setMaximo(clavesVO.getMaximoClave4());
		   dcaVO.setMinimo(clavesVO.getMinimoClave4());
		   ldcaVO.add(dcaVO);
		  }
		  if(clavesVO.getClave5()!=null && StringUtils.isNotBlank(clavesVO.getClave5())){
		   dcaVO= new DatosClaveAtributoVO();
		   dcaVO.setNumeroClave(5);
		   dcaVO.setDescripcion(clavesVO.getClave5());
		   dcaVO.setFormato(clavesVO.getFormatoClave5());
		   dcaVO.setMaximo(clavesVO.getMaximoClave5());
		   dcaVO.setMinimo(clavesVO.getMinimoClave5());
		   ldcaVO.add(dcaVO);
		  }
		  return ldcaVO;
		 }
	 
	 /**
		 * Implementacion que convierte DescripcionVeinticincoAtributosVO a List<LlaveValorVO> 
		 * 
		 * @return llvVO lista de objetos LlaveValorVO - List<LlaveValorVO>  
		 *         
		 * @param dva descripcion veinticinco atributos - objeto DescripcionVeinticincoAtributosVO
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.             
		 */
	 public List<LlaveValorVO> convierteAListaLVVO(String identificador , DescripcionVeinticincoAtributosVO descripcionAtributos , DescripcionVeinticincoAtributosVO dva){
			
		 	List<LlaveValorVO> llvVO= new ArrayList<LlaveValorVO>();
			
		 	LlaveValorVO lvVO= null;
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion1())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("1");
				lvVO.setValue(descripcionAtributos.getDescripcion1());
				lvVO.setNick(dva.getDescripcion1());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion2())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("2");
				lvVO.setValue(descripcionAtributos.getDescripcion2());
				lvVO.setNick(dva.getDescripcion2());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion3())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("3");
				lvVO.setValue(descripcionAtributos.getDescripcion3());
				lvVO.setNick(dva.getDescripcion3());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion4())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("4");
				lvVO.setValue(descripcionAtributos.getDescripcion4());
				lvVO.setNick(dva.getDescripcion4());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion5())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("5");
				lvVO.setValue(descripcionAtributos.getDescripcion5());
				lvVO.setNick(dva.getDescripcion5());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion6())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("6");
				lvVO.setValue(descripcionAtributos.getDescripcion6());
				lvVO.setNick(dva.getDescripcion6());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion7())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("7");
				lvVO.setValue(descripcionAtributos.getDescripcion7());
				lvVO.setNick(dva.getDescripcion7());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion8())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("8");
				lvVO.setValue(descripcionAtributos.getDescripcion8());
				lvVO.setNick(dva.getDescripcion8());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion9())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("9");
				lvVO.setValue(descripcionAtributos.getDescripcion9());
				lvVO.setNick(dva.getDescripcion9());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion10())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("10");
				lvVO.setValue(descripcionAtributos.getDescripcion10());
				lvVO.setNick(dva.getDescripcion10());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion11())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("11");
				lvVO.setValue(descripcionAtributos.getDescripcion11());
				lvVO.setNick(dva.getDescripcion11());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion12())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("12");
				lvVO.setValue(descripcionAtributos.getDescripcion12());
				lvVO.setNick(dva.getDescripcion12());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion13())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("13");
				lvVO.setValue(descripcionAtributos.getDescripcion13());
				lvVO.setNick(dva.getDescripcion13());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion14())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("14");
				lvVO.setValue(descripcionAtributos.getDescripcion14());
				lvVO.setNick(dva.getDescripcion14());
				llvVO.add(lvVO);
			}		
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion15())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("15");
				lvVO.setValue(descripcionAtributos.getDescripcion15());
				lvVO.setNick(dva.getDescripcion15());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion16())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("16");
				lvVO.setValue(descripcionAtributos.getDescripcion16());
				lvVO.setNick(dva.getDescripcion16());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion17())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("17");
				lvVO.setValue(descripcionAtributos.getDescripcion17());
				lvVO.setNick(dva.getDescripcion17());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion18())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("18");
				lvVO.setValue(descripcionAtributos.getDescripcion18());
				lvVO.setNick(dva.getDescripcion18());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion19())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("19");
				lvVO.setValue(descripcionAtributos.getDescripcion19());
				lvVO.setNick(dva.getDescripcion19());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion20())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("20");
				lvVO.setValue(descripcionAtributos.getDescripcion20());
				lvVO.setNick(dva.getDescripcion20());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion21())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("21");
				lvVO.setValue(descripcionAtributos.getDescripcion21());
				lvVO.setNick(dva.getDescripcion21());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion22())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("22");
				lvVO.setValue(descripcionAtributos.getDescripcion22());
				lvVO.setNick(dva.getDescripcion22());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion23())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("23");
				lvVO.setValue(descripcionAtributos.getDescripcion23());
				lvVO.setNick(dva.getDescripcion23());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion24())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("24");
				lvVO.setValue(descripcionAtributos.getDescripcion24());
				lvVO.setNick(dva.getDescripcion24());
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion25())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("25");
				lvVO.setValue(descripcionAtributos.getDescripcion25());
				lvVO.setNick(dva.getDescripcion25());
				llvVO.add(lvVO);
			}	
			
			return llvVO;
		}
	 
	 
	 /**
	  * Implementacion del metrodo para obtener la lista de descripcion de atributos que sirve como axiliar en caso de que no haya aun claves agregadas
	  * @param identificador
	  * @param descripcionAtributos
	  * @return Lista de descripciones de atributos dummy
	  */
	 public List<LlaveValorVO> obtieneValorDummyAtributos(String identificador , DescripcionVeinticincoAtributosVO descripcionAtributos){
			
		 	List<LlaveValorVO> llvVO= new ArrayList<LlaveValorVO>();
			
		 	LlaveValorVO lvVO= null;
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion1())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("1");
				lvVO.setValue(descripcionAtributos.getDescripcion1());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion2())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("2");
				lvVO.setValue(descripcionAtributos.getDescripcion2());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion3())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("3");
				lvVO.setValue(descripcionAtributos.getDescripcion3());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion4())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("4");
				lvVO.setValue(descripcionAtributos.getDescripcion4());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion5())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("5");
				lvVO.setValue(descripcionAtributos.getDescripcion5());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion6())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("6");
				lvVO.setValue(descripcionAtributos.getDescripcion6());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion7())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("7");
				lvVO.setValue(descripcionAtributos.getDescripcion7());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion8())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("8");
				lvVO.setValue(descripcionAtributos.getDescripcion8());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion9())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("9");
				lvVO.setValue(descripcionAtributos.getDescripcion9());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion10())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("10");
				lvVO.setValue(descripcionAtributos.getDescripcion10());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion11())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("11");
				lvVO.setValue(descripcionAtributos.getDescripcion11());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion12())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("12");
				lvVO.setValue(descripcionAtributos.getDescripcion12());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion13())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("13");
				lvVO.setValue(descripcionAtributos.getDescripcion13());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion14())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("14");
				lvVO.setValue(descripcionAtributos.getDescripcion14());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}		
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion15())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("15");
				lvVO.setValue(descripcionAtributos.getDescripcion15());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion16())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("16");
				lvVO.setValue(descripcionAtributos.getDescripcion16());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion17())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("17");
				lvVO.setValue(descripcionAtributos.getDescripcion17());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion18())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("18");
				lvVO.setValue(descripcionAtributos.getDescripcion18());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion19())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("19");
				lvVO.setValue(descripcionAtributos.getDescripcion19());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion20())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("20");
				lvVO.setValue(descripcionAtributos.getDescripcion20());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion21())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("21");
				lvVO.setValue(descripcionAtributos.getDescripcion21());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion22())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("22");
				lvVO.setValue(descripcionAtributos.getDescripcion22());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion23())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("23");
				lvVO.setValue(descripcionAtributos.getDescripcion23());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion24())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("24");
				lvVO.setValue(descripcionAtributos.getDescripcion24());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}
			if(StringUtils.isNotBlank(descripcionAtributos.getDescripcion25())){
				lvVO= new LlaveValorVO();
				lvVO.setId(identificador);
				lvVO.setKey("25");
				lvVO.setValue(descripcionAtributos.getDescripcion25());
				lvVO.setNick("");
				llvVO.add(lvVO);
			}	
			
			return llvVO;
		}
		
	 /**
		 * Implementacion que convierte List<LlaveValorVO> a DescripcionVeinticincoAtributosVO 
		 * 
		 * @return dvaVO DescripcionVeinticincoAtributosVO - objeto DescripcionVeinticincoAtributosVO 
		 *         
		 * @param llvVO lista LlaveValorVO - List<LlaveValorVO>
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.              
		 */
		public DescripcionVeinticincoAtributosVO convierteAdvaVO(List<LlaveValorVO> llvVO){
			DescripcionVeinticincoAtributosVO dvaVO= new DescripcionVeinticincoAtributosVO();
			if(llvVO!=null && !llvVO.isEmpty()){
				for(LlaveValorVO lvVO: llvVO){
					logger.debug("lvVO.getKey()=" + lvVO.getKey());
					switch(Integer.parseInt(lvVO.getKey())){
					case 1:
						dvaVO.setDescripcion1(lvVO.getNick());
						break;
					case 2:
						dvaVO.setDescripcion2(lvVO.getNick());
						break;
					case 3:
						dvaVO.setDescripcion3(lvVO.getNick());
						break;
					case 4:
						dvaVO.setDescripcion4(lvVO.getNick());
						break;
					case 5:
						dvaVO.setDescripcion5(lvVO.getNick());
						break;
					case 6:
						dvaVO.setDescripcion6(lvVO.getNick());
						break;
					case 7:
						dvaVO.setDescripcion7(lvVO.getNick());
						break;
					case 8:
						dvaVO.setDescripcion8(lvVO.getNick());
						break;
					case 9:
						dvaVO.setDescripcion9(lvVO.getNick());
						break;
					case 10:
						dvaVO.setDescripcion10(lvVO.getNick());
						break;
					case 11:
						dvaVO.setDescripcion11(lvVO.getNick());
						break;
					case 12:
						dvaVO.setDescripcion12(lvVO.getNick());
						break;
					case 13:
						dvaVO.setDescripcion13(lvVO.getNick());
						break;
					case 14:
						dvaVO.setDescripcion14(lvVO.getNick());
						break;
					case 15:
						dvaVO.setDescripcion15(lvVO.getNick());
						break;
					case 16:
						dvaVO.setDescripcion16(lvVO.getNick());
						break;
					case 17:
						dvaVO.setDescripcion17(lvVO.getNick());
						break;
					case 18:
						dvaVO.setDescripcion18(lvVO.getNick());
						break;
					case 19:
						dvaVO.setDescripcion19(lvVO.getNick());
						break;
					case 20:
						dvaVO.setDescripcion20(lvVO.getNick());
						break;
					case 21:
						dvaVO.setDescripcion21(lvVO.getNick());
						break;
					case 22:
						dvaVO.setDescripcion22(lvVO.getNick());
						break;
					case 23:
						dvaVO.setDescripcion23(lvVO.getNick());
						break;
					case 24:
						dvaVO.setDescripcion24(lvVO.getNick());
						break;
					case 25:
						dvaVO.setDescripcion25(lvVO.getNick());
						break;
					}						
				}
			}
			return dvaVO;
		}
		
		/**
		 * Implementacion que une la descripcion de Veinticinco Atributos con sus valores 
		 *      
		 * @param llvVO
		 * @param ldVO
		 * @return jllvVO lista LlaveValorVO - List<LlaveValorVO>
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.        
		 */
		public List<LlaveValorVO> juntaListas(List<LlaveValorVO> llvVO, List<LlaveValorVO> ldVO){
			  logger.debug("Tamano llvVO=" + llvVO.size());
			  logger.debug("Tamano ldVO=" + ldVO.size());
			  
			  List<LlaveValorVO> jllvVO= new ArrayList<LlaveValorVO>();
			  if(!llvVO.isEmpty()){
			   int maximo=llvVO.size();
			   LlaveValorVO lvVO= null;
			   for(int i=0;i<maximo;i++){
			    lvVO= new LlaveValorVO();
			    lvVO.setKey(Integer.toString(i));
			    lvVO.setValue(llvVO.get(i).getValue());
			    //validacion para que no se intente obtener un elemento inexistente:
			    if(ldVO.size() >= maximo){
			    	lvVO.setNick(ldVO.get(i).getValue());
			    }
			    jllvVO.add(i,lvVO);    
			   }
			  }
			  return jllvVO;
			 } 
			  
		/**
		 * Implementacion que ordena una lista de tipo List<LlaveValorVO> 
		 * @param llvVO
		 * @return nllvVO lista LlaveValorVO - List<LlaveValorVO>
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.    
		 */
		public List<LlaveValorVO> ordenaLlvVO(List<LlaveValorVO> llvVO){
			  List<LlaveValorVO> nllvVO= new ArrayList<LlaveValorVO>();
			  for(LlaveValorVO lvVO:llvVO){
			   nllvVO.add(Integer.parseInt(lvVO.getKey()), lvVO);
			  }
			  return nllvVO;
			 }
	 
		/**
		 * convierte una lista de valores a una lista de LlaveValorVO 
		 * @param lldvVO List<ListaDeValoresVO>
		 * @return List<LlaveValor>
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.
		 */
		public List<LlaveValorVO> convierteALlaveValorVO(List<ListaDeValoresVO> lldvVO){
			   List<LlaveValorVO> llvVO = new ArrayList<LlaveValorVO>();
			   LlaveValorVO lvVO = null;
			   for(ListaDeValoresVO ldvVO: lldvVO){
			    lvVO = new LlaveValorVO();
			    lvVO.setKey(ldvVO.getCdAtribu());
			    lvVO.setValue(ldvVO.getDescripcionFormato());
			    llvVO.add(lvVO);
			   }   
			   return llvVO;
			  }
		
		/**
		 * convierte una lista List<DatosClaveAtributoVO> de valores a una lista de LlaveValorVO 
		 * @param lldvVO List<DatosClaveAtributoVO>
		 * @return List<LlaveValor>
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.
		 */
		 public List<LlaveValorVO> convierteALlaveValorVODeDatos(List<DatosClaveAtributoVO> lldvVO){
		      List<LlaveValorVO> llvVO = new ArrayList<LlaveValorVO>();
		      LlaveValorVO lvVO = null;
		      for(DatosClaveAtributoVO ldvVO: lldvVO){
		       lvVO = new LlaveValorVO();
		       lvVO.setKey(Integer.toString(ldvVO.getNumeroClave()));
		       lvVO.setValue(ldvVO.getDescripcion());
		       llvVO.add(lvVO);
		      }   
		      return llvVO;
		     }
		
		/**
		 * une la lista de tipo LlaveValorVO original con la temporal
		 * @param base List<LlaveValorVO>
		 * @param temporal List<LlaveValorVO>
		 * @return base List<LlaveValorVO>
		 * @throws ApplicationException -
		 *             Es lanzada en errores de configuracion de aplicacion error en
		 *             las consultas a BD.
		 */
		public List<LlaveValorVO> juntaBaseYTemporal(List<LlaveValorVO> base,List<LlaveValorVO> temporal){
			   int max=temporal.size();
			   for(int i =0; i<max; i++){
				   temporal.get(i).setNick(base.get(i).getNick());
			   }
			   return temporal;
			  }

	
}
