package mx.com.gseguros.wizard.configuracion.producto.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.service.PagedList;
import mx.com.aon.portal.service.impl.AbstractManagerJdbcTemplateInvoke;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TablaCincoClavesManagerJdbcTemplate;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionCincoClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DescripcionVeinticincoAtributosVO;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.ValoresCincoClavesVO;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Implementacion de TablaCincoClavesManagerJdbcTemplate para peticion de informacion valores para la tabla de cinco claves para el uso de JdbcTemplate
 *
 */
public class TablaCincoClavesManagerJdbcTemplateImpl extends AbstractManagerJdbcTemplateInvoke implements TablaCincoClavesManagerJdbcTemplate {
	
	@SuppressWarnings("unchecked")
	public PagedList obtieneValoresClave(String nmTabla , int start , int limit) throws ApplicationException {

		HashMap map = new HashMap();
		map.put("PV_NMTABLA_I", nmTabla);
		
        return pagedBackBoneInvoke(map, "OBTIENE_VALORES_CLAVE", start, limit);
	}
	
	public String borraValoresClave(String nmTabla , String clave1, String clave2, String clave3, String clave4, String clave5) throws ApplicationException{
		HashMap map = new HashMap();
		map.put("pi_tabla", nmTabla);
		map.put("pi_clave01", clave1);
		map.put("pi_clave02", clave2);
		map.put("pi_clave03", clave3);
		map.put("pi_clave04", clave4);
		map.put("pi_clave05", clave5);
		
		WrapperResultados res = returnBackBoneInvoke(map, "BORRA_VALORES_CLAVE");
		if(res==null || StringUtils.isBlank(res.getMsgText()))return "Error al intentar borrar el registro, consulte a su soporte";
        return res.getMsgText();
	
	}

	public List<DatosClaveAtributoVO> consultaCincoClaves(String nmtabla) throws ApplicationException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("PI_NMTABLA", nmtabla);
		
		WrapperResultados res = returnBackBoneInvoke(map, "LISTA_CINCO_CLAVES");
		
		return (List<DatosClaveAtributoVO>) res.getItemList();
		
	}

	public List<DatosClaveAtributoVO> consultaDescripciones(String nmtabla) throws ApplicationException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("PI_NMTABLA", nmtabla);
		
		WrapperResultados res = returnBackBoneInvoke(map, "OBTIENE_DESCRIPCIONES_1Y5");
		
		return (List<DatosClaveAtributoVO>) res.getItemList();
		
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
		
		HashMap<String, Object> params =  new HashMap<String, Object>();
		params.put("PI_NMTABLA", nmTabla);
		
		try {
			WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_DESCRIPCION_ATRIBUTOS_CINCO_CLAVES");
			atributos = (List<LlaveValorVO>) res.getItemList();
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
	 		params.put("PV_NMTABLA_I", nmTabla);
	 		params.put("PV_FEDESDE_I", claves.getFechaDesde());
	 		params.put("PV_FEHASTA_I", claves.getFechaHasta());
	 		params.put("PV_OTCLAVE1_I", claves.getDescripcionClave1());
	 		params.put("PV_OTCLAVE2_I", claves.getDescripcionClave2());
	 		params.put("PV_OTCLAVE3_I", claves.getDescripcionClave3());
	 		params.put("PV_OTCLAVE4_I", claves.getDescripcionClave4());
	 		params.put("PV_OTCLAVE5_I", claves.getDescripcionClave5());
	 		
			
			DescripcionVeinticincoAtributosVO atributos = null;
			List<LlaveValorVO> listaAtributos = null;
			try {
				WrapperResultados res = returnBackBoneInvoke(params, "OBTIENE_VALORES_ATRIBUTOS_CLAVES");
				atributos = (DescripcionVeinticincoAtributosVO) res.getItemList().get(0);
				listaAtributos = new ArrayList<LlaveValorVO>();
				listaAtributos = convierteAListaLVVO(claves.getIdentificador(), descripcionAtributos, atributos);
				
			} catch (Exception e) {
				logger.error(e);
				throw new ApplicationException("Error regresando atributos para la tabla de cinco claves");
			}
			
			return listaAtributos;
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
	 
	 public List<DescripcionCincoClavesVO> insertaValoresCincoClaves(String nmTabla , List<ValoresCincoClavesVO> listaValores5Claves) throws ApplicationException {
			List<DescripcionCincoClavesVO> clavesNoInsertadas = new ArrayList<DescripcionCincoClavesVO>();
			 		
			for(ValoresCincoClavesVO clavesVO : listaValores5Claves ){
				WrapperResultados res = null;
				  Map params = new HashMap<String, String>();
				  params.put("PI_TIP_TRAN", clavesVO.getTipoTransito());
				  params.put("PI_NMTABLA", nmTabla);
				  params.put("PI_OTCLAVE1", clavesVO.getDescripcionCincoClaves().getDescripcionClave1());
				  params.put("PI_OTCLAVE2", clavesVO.getDescripcionCincoClaves().getDescripcionClave2());
				  params.put("PI_OTCLAVE3", clavesVO.getDescripcionCincoClaves().getDescripcionClave3());
				  params.put("PI_OTCLAVE4", clavesVO.getDescripcionCincoClaves().getDescripcionClave4());
				  params.put("PI_OTCLAVE5", clavesVO.getDescripcionCincoClaves().getDescripcionClave5());
				  params.put("PI_FEDESDE", clavesVO.getFechaDesde());
				  params.put("PI_FEHASTA", clavesVO.getFechaHasta());
				  params.put("PI_OTCLAVE1_ANT", clavesVO.getDescripcionCincoClavesAnterior().getDescripcionClave1());
				  params.put("PI_OTCLAVE2_ANT", clavesVO.getDescripcionCincoClavesAnterior().getDescripcionClave2());
				  params.put("PI_OTCLAVE3_ANT", clavesVO.getDescripcionCincoClavesAnterior().getDescripcionClave3());
				  params.put("PI_OTCLAVE4_ANT", clavesVO.getDescripcionCincoClavesAnterior().getDescripcionClave4());
				  params.put("PI_OTCLAVE5_ANT", clavesVO.getDescripcionCincoClavesAnterior().getDescripcionClave5());
				  params.put("PI_FEDESDE_ANT", clavesVO.getFechaDesdeAnterior());
				  params.put("PI_FEHASTA_ANT", clavesVO.getFechaHastaAnterior());
				  params.put("PI_OTVALOR01", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion1());
				  params.put("PI_OTVALOR02", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion2());
				  params.put("PI_OTVALOR03", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion3());
				  params.put("PI_OTVALOR04", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion4());
				  params.put("PI_OTVALOR05", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion5());
				  params.put("PI_OTVALOR06", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion6());
				  params.put("PI_OTVALOR07", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion7());
				  params.put("PI_OTVALOR08", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion8());
				  params.put("PI_OTVALOR09", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion9());
				  params.put("PI_OTVALOR10", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion10());
				  params.put("PI_OTVALOR11", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion11());
				  params.put("PI_OTVALOR12", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion12());
				  params.put("PI_OTVALOR13", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion13());
				  params.put("PI_OTVALOR14", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion14());
				  params.put("PI_OTVALOR15", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion15());
				  params.put("PI_OTVALOR16", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion16());
				  params.put("PI_OTVALOR17", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion17());
				  params.put("PI_OTVALOR18", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion18());
				  params.put("PI_OTVALOR19", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion19());
				  params.put("PI_OTVALOR20", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion20());
				  params.put("PI_OTVALOR21", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion21());
				  params.put("PI_OTVALOR22", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion22());
				  params.put("PI_OTVALOR23", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion23());
				  params.put("PI_OTVALOR24", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion24());
				  params.put("PI_OTVALOR25", clavesVO.getDescripcionVeinticincoAtributos().getDescripcion25());
				
				try {
					if(logger.isDebugEnabled())logger.debug("params==>" + params);
					res = returnBackBoneInvoke(params, "INSERTA_VALORES_CINCO_CLAVES");
					
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
	
}


