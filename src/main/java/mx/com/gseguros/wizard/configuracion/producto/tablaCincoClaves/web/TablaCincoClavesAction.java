package mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.wizard.configuracion.producto.model.ClavesVO;
import mx.com.gseguros.wizard.configuracion.producto.model.ListaDeValoresVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ListaDeValoresManager;
import mx.com.gseguros.wizard.configuracion.producto.service.TablaCincoClavesManagerJdbcTemplate;
import mx.com.gseguros.wizard.configuracion.producto.tablaCincoClaves.model.DatosClaveAtributoVO;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TablaCincoClavesAction extends Padre{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1814448881977431474L;
	private static final transient Log log = LogFactory.getLog(TablaCincoClavesAction.class);

	
	private ListaDeValoresManager listaDeValoresManager;
	private TablaCincoClavesManagerJdbcTemplate tablaCincoClavesManager;
	private boolean success;
	//attibutos de la primera cabecera
	private String nombre1;
	private String descripcion1;
	private String num1;
	private String tipoDeAcceso;
	private String switchModificacion;
	private String switchEnviar;
	private String clnatura;
	private String hiddenRadioTipoAcceso;
	private String radioTipoAcceso;
	//grids
	private List<DatosClaveAtributoVO> listaClaves;
	private List<DatosClaveAtributoVO> listaAtributos;
	private List<LlaveValorVO> listaClnatura;
	//atributos para guardar en el grid de claves
	private String codigoClave;
	private String descripcionClave;
	private String formatoClave;
	private String descripcionFormatoClave;
	private String maximoClave;
	private String minimoClave;
	//atributos para guardar en el grid de atributos
	private String codigoAtributo;
	private String descripcionAtributo;
	private String formatoAtributo;
	private String descripcionFormatoAtributo;
	private String maximoAtributo;
	private String minimoAtributo;
	
	private List<ListaDeValoresVO> editaCabeceraTabla5claves;
	
	private ListaDeValoresVO cabeceraTabla5claves;
	private List<DatosClaveAtributoVO> listaClaveTabla5claves;
	private List<DatosClaveAtributoVO> atributosTabla5claves;
	private String tipoTransaccion;
	private String edita;
	
	public String execute() throws Exception {
	
		return INPUT;
	}
		
	public String editaTabla5ClavesCabecera() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("editar tabla de cico claves + num1="+num1);
		}
    	editaCabeceraTabla5claves = new ArrayList<ListaDeValoresVO>();
    	
    	ListaDeValoresVO ldvVO = null;
    	if(num1 != null){
    		ldvVO = new ListaDeValoresVO();
    		cabeceraTabla5claves = listaDeValoresManager.consultaTabla(num1);
    		ldvVO.setNumeroTabla(cabeceraTabla5claves.getNumeroTabla());
    		ldvVO.setNombre(cabeceraTabla5claves.getNombre());
    		ldvVO.setDescripcion(cabeceraTabla5claves.getDescripcion());
    		ldvVO.setEnviarTablaErrores(cabeceraTabla5claves.getEnviarTablaErrores());
    	    ldvVO.setModificaValores(cabeceraTabla5claves.getModificaValores());
    		ldvVO.setClnatura(cabeceraTabla5claves.getClnatura());
    		ldvVO.setDsNatura(cabeceraTabla5claves.getDsNatura());
    		ldvVO.setOttipoac(cabeceraTabla5claves.getOttipoac());
    		
    		listaClaveTabla5claves = listaDeValoresManager.consultaClaves(num1);
    		session.put("LISTA_CINCO_CLAVES", listaClaveTabla5claves);
    		
    		atributosTabla5claves = listaDeValoresManager.consultaDescripciones(num1);
    		session.put("LISTA_VEINTICINCO_ATRIBUTOS", atributosTabla5claves);
    		
    	}
    	
    	editaCabeceraTabla5claves.add(ldvVO);
    	if(isDebugEnabled){
    		log.debug("!!!!!!!!!!!!!!editarListaDeValores + num1="+ldvVO.getNumeroTabla());
    	}
    	success = true;
    	return SUCCESS;
    }
	
	
	public String insertarTabla()throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("*********   ENTRANDO A INSERTAR TABLA DE APOYO  ***************");
			log.debug("Nombre1"+nombre1);
			log.debug("Descripcion1"+descripcion1);
			log.debug("Num1"+num1);
			log.debug("tipoDeAcceso"+tipoDeAcceso);
			log.debug("SwitchModificacion"+switchModificacion);
			log.debug("SwitchEnviar"+switchEnviar);
			log.debug("clnatura"+clnatura);
		}	
			for(DatosClaveAtributoVO claves :listaClaves){
				if("Numerico".equals(claves.getDescripcionFormato())){
					claves.setFormato("N");
				}if("Alfanumerico".equals(claves.getDescripcionFormato())){
					claves.setFormato("A");
				}if("Porcentaje".equals(claves.getDescripcionFormato())){
					claves.setFormato("P");
				}if("Fecha".equals(claves.getDescripcionFormato())){
					claves.setFormato("F");
				}
				if(isDebugEnabled){
					log.debug("numeroClave"+claves.getNumeroClave());
					log.debug("DescripcionClave"+claves.getDescripcion());
					log.debug("descripcionFormatoClave"+claves.getDescripcionFormato());
					log.debug("FormatoClave"+claves.getFormato());
					log.debug("maximoClave"+claves.getMaximo());
					log.debug("minimoClave"+claves.getMinimo());
				}
			}
			for(DatosClaveAtributoVO atributos: listaAtributos){
				if(atributos.getDescripcionFormato().equals("Numerico")){
					atributos.setFormato("N");
				}if(atributos.getDescripcionFormato().equals("Alfanumerico")){
					atributos.setFormato("A");
				}if(atributos.getDescripcionFormato().equals("Porcentaje")){
					atributos.setFormato("P");
				}if(atributos.getDescripcionFormato().equals("Fecha")){
					atributos.setFormato("F");
				}
				if(isDebugEnabled){
					log.debug("numeroAtributo"+atributos.getNumeroClave());
					log.debug("DescripcionAtributo"+atributos.getDescripcion());
					log.debug("descripcionFormatoAtributo"+atributos.getDescripcionFormato());
					log.debug("FormatoAtributo"+atributos.getFormato());
					log.debug("maximoAtributo"+atributos.getMaximo());
					log.debug("minimoAtributo"+atributos.getMinimo());
				}
			}
		
		/*if(switchModificacion==null){
			switchModificacion="N";				
		}
		if(switchEnviar==null){
			switchEnviar="N";
		}
		*/
		listaClnatura= (List<LlaveValorVO>) session.get("CATALOG_CLNATURA");
		for(LlaveValorVO lvVO:listaClnatura){
			if(lvVO.getValue().equals(clnatura)){
				clnatura=lvVO.getKey();
			}
		}

		/*if(tipoTransaccion == null || StringUtils.isBlank(tipoTransaccion)){			
			tipoTransaccion="1";//mandar este valor desde la jsp (1=insert, 2=update)
		}
		if(radioTipoAcceso != null && StringUtils.isNotBlank(radioTipoAcceso)){
			ldvVO.setOttipoac(radioTipoAcceso);
		}else{			
			ldvVO.setOttipoac(hiddenRadioTipoAcceso);
		}
		*/
		ListaDeValoresVO ldvVO= new ListaDeValoresVO();
		ldvVO.setNombre(nombre1);
		ldvVO.setDescripcion(descripcion1);
		ldvVO.setNumeroTabla(num1);
		ldvVO.setOttipoac(tipoDeAcceso);
		ldvVO.setOttipotb("5");
		ldvVO.setModificaValores(switchModificacion);
		ldvVO.setEnviarTablaErrores("S");
		ldvVO.setClnatura(clnatura);
		
		if(!StringUtils.isBlank(num1)){
			tipoTransaccion="2";
		}else{
			tipoTransaccion="1";
		}
		log.debug("TIPOTRANSACCION@="+tipoTransaccion);
		String numeroTabla=listaDeValoresManager.guardaListaValores(ldvVO);
		if(numeroTabla!=null){
				//if(!listaClaves.isEmpty()){					
					//listaClaves=(List<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES");
					ClavesVO claveVO=listaDeValoresManager.convierteAClaveVO(listaClaves);			
					listaDeValoresManager.guardaClaveListaDeValores(claveVO, tipoTransaccion, numeroTabla);
				//}
			
			//listaAtributos=(List<DatosClaveAtributoVO>) session.get("LISTA_VEINTICINCO_ATRIBUTOS");
			if(!listaAtributos.isEmpty()){
				
					List<ListaDeValoresVO> lldvVO= new ArrayList<ListaDeValoresVO>();
					ListaDeValoresVO ldvVOutil=null;
					int codigoAtribu = 0;
					for(DatosClaveAtributoVO dcaVO:listaAtributos){
						/*if(isDebugEnabled){
							log.debug("numeroAtributo*"+dcaVO.getNumeroClave());
							log.debug("DescripcionAtributo*"+dcaVO.getDescripcion());
							log.debug("descripcionFormatoAtributo*"+dcaVO.getDescripcionFormato());
							log.debug("FormatoAtributo*"+dcaVO.getFormato());
							log.debug("maximoAtributo*"+dcaVO.getMaximo());
							log.debug("minimoAtributo*"+dcaVO.getMinimo());
						}*/
						ldvVOutil= new ListaDeValoresVO();
						if(dcaVO.getNumeroClave()==0){
							codigoAtribu++;
							ldvVOutil.setCdAtribu(Integer.toString(codigoAtribu));
							
						}else{
							ldvVOutil.setCdAtribu(Integer.toString(dcaVO.getNumeroClave()));
							codigoAtribu=dcaVO.getNumeroClave();
						}
						//ldvVOutil.setCdAtribu(Integer.toString(dcaVO.getNumeroClave()));//se comenta por que no se necesita para insertar ya que es autogenerable y los atributos de la tabla no se pueden editar
						ldvVOutil.setDescripcionFormato(dcaVO.getDescripcion());
						ldvVOutil.setFormatoDescripcion(dcaVO.getFormato());
						ldvVOutil.setMaximoDescripcion(dcaVO.getMaximo());
						ldvVOutil.setMinimoDescripcion(dcaVO.getMinimo());
						lldvVO.add(ldvVOutil);
					}
				listaDeValoresManager.guardaDescripcionListaDeValores(lldvVO, tipoTransaccion, numeroTabla);
				
				/*if(session.containsKey("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL")&&!((List<DescripcionCincoClavesVO>)session.get("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL")).isEmpty()){
					List<DescripcionCincoClavesVO> ldceCompleta=(List<DescripcionCincoClavesVO>) session.get("DESCRIPCION_CINCO_CLAVES_EDITABLE_TEMPORAL");
					List<DescripcionCincoClavesVO> ldceInsertar = new ArrayList<DescripcionCincoClavesVO>();
					List<DescripcionCincoClavesVO> ldceModificar = new ArrayList<DescripcionCincoClavesVO>();
					for(DescripcionCincoClavesVO dccVO: ldceCompleta){
						if(dccVO.getIdentificador()!=null){
							ldceModificar.add(dccVO);
						}else{
							ldceInsertar.add(dccVO);				
						}
						if(isDebugEnabled){
							log.debug("atributos"+dccVO.getAtributos());
							log.debug("fechas");
							log.debug(dccVO.getFechaDesde());
							log.debug(dccVO.getFechaHasta());
						}
					}
				if(!ldceModificar.isEmpty()){		
						List<DescripcionCincoClavesVO> ldccVOBase= tablaCincoClavesManager.obtieneValoresClave(numeroTabla);
						List<DescripcionCincoClavesVO> ldceModificarAnterior = new ArrayList<DescripcionCincoClavesVO>();
						for(DescripcionCincoClavesVO dccVO:ldccVOBase){
							for(DescripcionCincoClavesVO dccVOt:ldceModificar){
								if(dccVO.getIdentificador()==dccVOt.getIdentificador()){
									ldceModificarAnterior.add(dccVO);
								}
							}
						}
						tablaCincoClavesManager.insertaValoresCincoClaves(ldceModificar, ldceModificarAnterior, "2", numeroTabla);
					}
					if(!ldceInsertar.isEmpty())
						tablaCincoClavesManager.insertaValoresCincoClaves(ldceInsertar, ldceInsertar, "1", numeroTabla);
				
				}*/
					
			}
		}
		if(isDebugEnabled){
			//log.debug("NMTABLA"+numeroTabla);
			log.debug("TIPOTRANSACCION**--"+tipoTransaccion);
		}
		success=true;
		return SUCCESS;
	}
	public String insertarAtributo(){
		
		if(session.containsKey("LISTA_VEINTICINCO_ATRIBUTOS")){
			if(descripcionFormatoAtributo!=null && descripcionFormatoAtributo.equals("Numerico")){
				formatoAtributo="N";
			}
			if(descripcionFormatoAtributo!=null && descripcionFormatoAtributo.equals("Alfanumerico")){
				formatoAtributo="A";
			}
			if(descripcionFormatoAtributo!=null && descripcionFormatoAtributo.equals("Porcentaje")){
				formatoAtributo="P";
			}
			if(descripcionFormatoAtributo!=null && descripcionFormatoAtributo.equals("Fecha")){
				formatoAtributo="F";
			}
			listaAtributos=(List<DatosClaveAtributoVO>) session.get("LISTA_VEINTICINCO_ATRIBUTOS");
			List<DatosClaveAtributoVO> listaAtributosNuevos= (List<DatosClaveAtributoVO>) session.get("LISTA_VEINTICINCO_ATRIBUTOS_NUEVOS");
			DatosClaveAtributoVO ldvVO = new DatosClaveAtributoVO();
			int tamagnoAtributos=listaAtributos.size();
			ldvVO.setNumeroClave(++tamagnoAtributos);
			ldvVO.setDescripcion(descripcionAtributo);
			ldvVO.setDescripcionFormato(descripcionFormatoAtributo);
			ldvVO.setFormato(formatoAtributo);
			ldvVO.setMaximo(maximoAtributo);
			ldvVO.setMinimo(minimoAtributo);
			listaAtributos.add(ldvVO);
			session.put("LISTA_VEINTICINCO_ATRIBUTOS", listaAtributos);
			session.put("LISTA_VEINTICINCO_ATRIBUTOS_NUEVOS", listaAtributosNuevos);
			success=true;
		}else{
			if(log.isDebugEnabled()){
				log.debug("entro al else");
			}
			success=false;
		}
		success=true;
		return SUCCESS;
	}
	public String insertarClave(){
		boolean isDebugEnabled = log.isDebugEnabled();
		/*if(session.containsKey("LISTA_CINCO_CLAVES") && edita==null){
			listaClaves=(List<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES");
			List<DatosClaveAtributoVO> listaClavesNuevas = (List<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES_NUEVAS");
			DatosClaveAtributoVO dcaVO= new DatosClaveAtributoVO();
			
			//log.debug("descripcionFormatoClave"+descripcionFormatoClave);
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Numerico")){
				formatoClave="N";
			}
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Alfanumerico")){
				formatoClave="A";
			}
			if(codigoClave!= null){
				for(DatosClaveAtributoVO lista: listaClaves){
					if(codigoClave.equals(lista.getNumeroClave())){
						dcaVO.setDescripcion(descripcionClave);
						dcaVO.setDescripcionFormato(descripcionFormatoClave);
						//log.debug("formatoClave"+formatoClave);
						dcaVO.setFormato(formatoClave);
						dcaVO.setMaximo(maximoClave);
						dcaVO.setMinimo(minimoClave);	
					}
				}
			}else{
			int contador=listaClaves.size();
			dcaVO.setNumeroClave(contador+1);
			dcaVO.setDescripcion(descripcionClave);
			dcaVO.setDescripcionFormato(descripcionFormatoClave);
			//log.debug("formatoClave"+formatoClave);
			dcaVO.setFormato(formatoClave);
			dcaVO.setMaximo(maximoClave);
			dcaVO.setMinimo(minimoClave);
			}
			listaClaves.add(dcaVO);
			session.put("LISTA_CINCO_CLAVES", listaClaves);
			session.put("LISTA_CINCO_CLAVES_NUEVAS", listaClavesNuevas);
			success=true;
		}
		
		if(num1!=null && edita.equals("base")){
			try {
				listaClaves=listaDeValoresManager.consultaClaves(num1);
			} catch (ApplicationException e1) {				
				e1.printStackTrace();
			}
			//ClavesVO claveVO=listaDeValoresManager.convierteAClaveVO(listaClaves);
			//List<DatosClaveAtributoVO> listaClaveBase = new ArrayList<DatosClaveAtributoVO>();
			//DatosClaveAtributoVO claveBase= new DatosClaveAtributoVO();
			
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Numerico")){
				formatoClave="N";
			}
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Alfanumerico")){
				formatoClave="A";
			}
			int codigoClave2=Integer.parseInt(codigoClave);
			codigoClave2=codigoClave2++;
			codigoClave= String.valueOf(codigoClave2);
			log.debug("CODIGOcLAVE"+codigoClave);
			log.debug("CODIGOcLAVE2"+codigoClave2);
			for(DatosClaveAtributoVO lista: listaClaves){				
				int contador=1;
				lista.setNumeroClave(contador);
				if(codigoClave.equals(lista.getNumeroClave())){					
					lista.setDescripcion(descripcionClave);
					lista.setDescripcionFormato(descripcionFormatoClave);
					lista.setFormato(formatoClave);
					lista.setMaximo(maximoClave);
					lista.setMinimo(minimoClave);
				}
				contador++;
				if(isDebugEnabled){				
					log.debug("listadescripcionClave"+lista.getDescripcion());
					log.debug("listanumeroClave"+lista.getNumeroClave());				
				}
			}
			if(isDebugEnabled){				
				log.debug("EDITABASEdescripcionClave"+descripcionClave);
				log.debug("EDITABASEdescripcionFormatoClave"+descripcionFormatoClave);
				for(DatosClaveAtributoVO lista: listaClaves){
					log.debug("listadescripcionClave"+lista.getDescripcion());
					log.debug("listanumeroClave"+lista.getNumeroClave());		
				}
			}
			//claveBase.setNumeroClave(Integer.parseInt(codigoClave));
			//claveBase.setDescripcion(descripcionClave);
			//claveBase.setDescripcionFormato(descripcionFormatoClave);
				//log.debug("formatoClave"+formatoClave);
			//claveBase.setFormato(formatoClave);
			//claveBase.setMaximo(maximoClave);
			//claveBase.setMinimo(minimoClave);
			//listaClaveBase.add(claveBase);
			
			if(!listaClaves.isEmpty()){
				ClavesVO claveBaseVO = new ClavesVO();
				//claveBaseVO=null;
				try {
					claveBaseVO=listaDeValoresManager.convierteAClaveVO(listaClaves);
					if(isDebugEnabled){
						log.debug("DESCRIPCION CLAVE EDITADA"+claveBaseVO.getClave());
						log.debug("FORMATO DESCRIPCION CLAVE EDITADA"+claveBaseVO.getFormatoClave());				
					}
				} catch (ApplicationException e) {					
					e.printStackTrace();
				}	
				try {
					listaDeValoresManager.guardaClaveListaDeValores(claveBaseVO, "2", num1);
				} catch (ApplicationException e) {				
					e.printStackTrace();
				}
			}else{
				success=false;
			}
		}
		else{
			success=false;
		}
		return SUCCESS;
		*/
		if(session.containsKey("LISTA_CINCO_CLAVES")){
			listaClaves=(List<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES");
			List<DatosClaveAtributoVO> listaClavesNuevas = (List<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES_NUEVAS");
			DatosClaveAtributoVO dcaVO= new DatosClaveAtributoVO();
			
			//log.debug("descripcionFormatoClave"+descripcionFormatoClave);
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Numerico")){
				formatoClave="N";
			}
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Alfanumerico")){
				formatoClave="A";
			}
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Porcentaje")){
				formatoClave="P";
			}
			if(descripcionFormatoClave!=null && descripcionFormatoClave.equals("Fecha")){
				formatoClave="F";
			}
			//log.debug("codigoClave@@@@@@"+codigoClave);
			/*if(!codigoClave.equals("0") && codigoClave!= null && StringUtils.isNotBlank(codigoClave)){
				for(DatosClaveAtributoVO lista: listaClaves){
					log.debug("NUMEROCLAVELISTA@@@@"+lista.getNumeroClave());
					if(codigoClave.equals(lista.getNumeroClave())){
						lista.setDescripcion(descripcionClave);
						lista.setDescripcionFormato(descripcionFormatoClave);
						lista.setFormato(formatoClave);
						lista.setMaximo(maximoClave);
						lista.setMinimo(minimoClave);	
						log.debug("descripcionClave"+descripcionClave);
						log.debug("listadescripcionClave"+lista.getDescripcion());
					}
				}
			}else{*/
			int contador=listaClaves.size();
			dcaVO.setNumeroClave(contador+1);
			dcaVO.setDescripcion(descripcionClave);
			dcaVO.setDescripcionFormato(descripcionFormatoClave);
			//log.debug("formatoClave"+formatoClave);
			dcaVO.setFormato(formatoClave);
			dcaVO.setMaximo(maximoClave);
			dcaVO.setMinimo(minimoClave);
			listaClaves.add(dcaVO);
			//}
			session.put("LISTA_CINCO_CLAVES", listaClaves);
			session.put("LISTA_CINCO_CLAVES_NUEVAS", listaClavesNuevas);
			success=true;
		}else{
			success=false;
		}
		return SUCCESS;
		
	}
	
	public String listaCincoClavesJson() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("lista cinco claves json");
		}
		if(!session.containsKey("LISTA_CINCO_CLAVES") || !session.containsKey("LISTA_CINCO_CLAVES_EN_SESSION") ){
			if(isDebugEnabled){
				log.debug("no contiene cargaManual en sesion");
				log.debug("tabla");
			}
			if(num1 != null && StringUtils.isNotBlank(num1) && !num1.equals("undefined")){
				if(isDebugEnabled){
					log.debug("tabla difenrente de null");
				}
				listaClaves=tablaCincoClavesManager.consultaCincoClaves(num1);
				for(DatosClaveAtributoVO lista: listaClaves){
					if("A".equals(lista.getFormato())){
						lista.setDescripcionFormato("Alfanumerico");
					}if("N".equals(lista.getFormato())){
						lista.setDescripcionFormato("Numerico");
					}if("P".equals(lista.getFormato())){
						lista.setDescripcionFormato("Porcentaje");
					}if("F".equals(lista.getFormato())){
						lista.setDescripcionFormato("Fecha");
					}
					
				}
			}			
		}
		if(listaClaves==null)
			listaClaves = new ArrayList<DatosClaveAtributoVO>();
		if(isDebugEnabled){
			log.debug("listaClaves="+listaClaves);
		}
		if(!session.containsKey("LISTA_CINCO_CLAVES_NUEVOS")){
			session.put("LISTA_CINCO_CLAVES_NUEVOS", new ArrayList<DatosClaveAtributoVO>());
		}
		success = true;
		return SUCCESS;
	}
	
	public String listaAtributosJson() throws ApplicationException{
		boolean isDebugEnabled = log.isDebugEnabled();
		if(isDebugEnabled){
			log.debug("lista veinti cinco atributos json");
		}
		if(!session.containsKey("LISTA_VEINTICINCO_ATRIBUTOS") || !session.containsKey("LISTA_VEINTICINCO_ATRIBUTOS_EN_SESSION") ){
			if(isDebugEnabled){
				log.debug("no contiene cargaManual en sesion");
				log.debug("tabla");
			}
			if(num1 != null && StringUtils.isNotBlank(num1) && !num1.equals("undefined")){
				if(isDebugEnabled){
					log.debug("tabla difenrente de null");
				}
				listaAtributos=listaDeValoresManager.consultaDescripciones(num1);
				for(DatosClaveAtributoVO lista: listaAtributos){
					if("A".equals(lista.getFormato())){
						lista.setDescripcionFormato("Alfanumerico");
					}if("N".equals(lista.getFormato())){
						lista.setDescripcionFormato("Numerico");
					}if("P".equals(lista.getFormato())){
						lista.setDescripcionFormato("Porcentaje");
					}if("F".equals(lista.getFormato())){
						lista.setDescripcionFormato("Fecha");
					}
					
					
				}
				//session.put("LISTA_VEINTICINCO_ATRIBUTOS_EN_SESSION","ok");	
			}			
		}//else
			//listaAtributos = (List<DatosClaveAtributoVO>) session.get("LISTA_VEINTICINCO_ATRIBUTOS");
		if(listaAtributos==null)
			listaAtributos = new ArrayList<DatosClaveAtributoVO>();
		if(isDebugEnabled){
			log.debug("listaAtributos="+listaAtributos);
		}
//		session.put("LISTA_VEINTICINCO_ATRIBUTOS", listaAtributos);
		/*
//		num1="22";
		if(session.containsKey("LISTA_VEINTICINCO_ATRIBUTOS")){
			listaAtributos=(List<DatosClaveAtributoVO>) session.get("LISTA_VEINTICINCO_ATRIBUTOS");
		}else{
			if(num1 != null && StringUtils.isNotBlank(num1)){
				listaAtributos=listaDeValoresManager.consultaDescripciones(num1);
				if(listaAtributos==null){
					listaAtributos = new ArrayList<DatosClaveAtributoVO>();
				}
			}else{
				listaAtributos = new ArrayList<DatosClaveAtributoVO>();
				DatosClaveAtributoVO ldvVO= new DatosClaveAtributoVO();
//				ldvVO.setNumeroClave(1);
//				ldvVO.setDescripcion("descripcion");
//				ldvVO.setFormato("A");
//				ldvVO.setDescripcionFormato("Alfanumerico");
//				ldvVO.setMaximo("2");
//				ldvVO.setMinimo("1");
//				listaAtributos.add(ldvVO);			
			}
			session.put("LISTA_VEINTICINCO_ATRIBUTOS", listaAtributos);
			
		}
		if(!session.containsKey("LISTA_VEINTICINCO_ATRIBUTOS_NUEVOS")){
			session.put("LISTA_VEINTICINCO_ATRIBUTOS_NUEVOS", new ArrayList<ListaDeValoresVO>());
		}*/
		success = true;
		return SUCCESS;
	}
	public String catalogoClnaturaJson() throws Exception{
		if(session.containsKey("CATALOGO_CLNATURA")){
			listaClnatura=(List<LlaveValorVO>) session.get("CATALOGO_CLNATURA");
		}else{
			listaClnatura=getTablasManager().obtenerListaNaturales();
			session.put("CATALOG_CLNATURA", listaClnatura);
		}
		
		success=true;
		return SUCCESS;
	}
	public String consultarEncabezado(){
//		ListaDeValoresVO ldvVO= atributosVariablesManager.consultaDescripciones(num1);
		success=true;		
		return SUCCESS;
	}
	
	public String eliminarAtributo(){
		if(log.isDebugEnabled()){
			log.debug("codigoAtributo"+codigoAtributo);
		}
		if(session.containsKey("LISTA_VEINTICINCO_ATRIBUTOS")){
			listaAtributos=(List<DatosClaveAtributoVO>) session.get("LISTA_VEINTICINCO_ATRIBUTOS");
			listaAtributos.remove(Integer.parseInt(codigoAtributo));
			session.put("LISTA_VEINTICINCO_ATRIBUTOS", listaAtributos);
			success=true;
		}else{
			success=false;
		}
		return SUCCESS;
	}
	public String eliminarClave(){		
		if(log.isDebugEnabled()){
			log.debug("eliminar clave tabla de cico claves + codigoClave="+codigoClave);
		}
		//codigoClave= codigoClave.replace(".action", "");
    	if(log.isDebugEnabled()){
    		log.debug("!!!despues del replace codigoClave="+codigoClave);
    	}
		/*if(log.isDebugEnabled()){
			log.debug("codigoClave"+codigoClave);
		}
		if(codigoClave!=null && session.containsKey("LISTA_CINCO_CLAVES")){
			listaClaves=(List<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES");
			listaClaves.remove(codigoClave);
			session.put("LISTA_CINCO_CLAVES", listaClaves);
			success=true;
		}else{
			success=false;
		}
		return SUCCESS;
		*/
    	if(codigoClave!=null && session.containsKey("LISTA_CINCO_CLAVES")){
            ArrayList<DatosClaveAtributoVO> temporal = (ArrayList<DatosClaveAtributoVO>) session.get("LISTA_CINCO_CLAVES");
            
                boolean valorLista = false;
               
                DatosClaveAtributoVO remover = null;
                for (DatosClaveAtributoVO  temp: temporal){
                    if(temp.getDescripcion().equals(codigoClave)){
                        
                        remover = temp;
                        valorLista = true;
                    }
                }
                if(remover!=null){
                   
                    temporal.remove(remover);
                    session.put("LISTA_CINCO_CLAVES", temporal);
                    success = true;      
                }
                if(valorLista){
                        session.put("LISTA_CINCO_CLAVES", temporal);
                        success = true;
                }
            }
            return SUCCESS;
	}
//getters y setters
	/**
	 * @param listaDeValoresManager the listaDeValoresManager to set
	 */
	public void setListaDeValoresManager(
			ListaDeValoresManager listaDeValoresManager) {
		this.listaDeValoresManager = listaDeValoresManager;
	}
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}


	/**
	 * @return the listaClaves
	 */
	public List<DatosClaveAtributoVO> getListaClaves() {
		return listaClaves;
	}

	/**
	 * @param listaClaves the listaClaves to set
	 */
	public void setListaClaves(List<DatosClaveAtributoVO> listaClaves) {
		this.listaClaves = listaClaves;
	}

	/**
	 * @return the nombre1
	 */
	public String getNombre1() {
		return nombre1;
	}

	/**
	 * @param nombre1 the nombre1 to set
	 */
	public void setNombre1(String nombre1) {
		this.nombre1 = nombre1;
	}

	/**
	 * @return the descripcion1
	 */
	public String getDescripcion1() {
		return descripcion1;
	}

	/**
	 * @param descripcion1 the descripcion1 to set
	 */
	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}

	/**
	 * @return the tipoDeAcceso
	 */
	public String getTipoDeAcceso() {
		return tipoDeAcceso;
	}

	/**
	 * @param tipoDeAcceso the tipoDeAcceso to set
	 */
	public void setTipoDeAcceso(String tipoDeAcceso) {
		this.tipoDeAcceso = tipoDeAcceso;
	}

	/**
	 * @return the switchModificacion
	 */
	public String getSwitchModificacion() {
		return switchModificacion;
	}

	/**
	 * @param switchModificacion the switchModificacion to set
	 */
	public void setSwitchModificacion(String switchModificacion) {
		this.switchModificacion = switchModificacion;
	}

	/**
	 * @return the switchEnviar
	 */
	public String getSwitchEnviar() {
		return switchEnviar;
	}

	/**
	 * @param switchEnviar the switchEnviar to set
	 */
	public void setSwitchEnviar(String switchEnviar) {
		this.switchEnviar = switchEnviar;
	}
	/**
	 * @return the descripcionClave
	 */
	public String getDescripcionClave() {
		return descripcionClave;
	}
	/**
	 * @param descripcionClave the descripcionClave to set
	 */
	public void setDescripcionClave(String descripcionClave) {
		this.descripcionClave = descripcionClave;
	}
	/**
	 * @return the formatoClave
	 */
	public String getFormatoClave() {
		return formatoClave;
	}
	/**
	 * @param formatoClave the formatoClave to set
	 */
	public void setFormatoClave(String formatoClave) {
		this.formatoClave = formatoClave;
	}

	/**
	 * @return the descripcionAtributo
	 */
	public String getDescripcionAtributo() {
		return descripcionAtributo;
	}
	/**
	 * @param descripcionAtributo the descripcionAtributo to set
	 */
	public void setDescripcionAtributo(String descripcionAtributo) {
		this.descripcionAtributo = descripcionAtributo;
	}
	/**
	 * @return the formatoAtributo
	 */
	public String getFormatoAtributo() {
		return formatoAtributo;
	}
	/**
	 * @param formatoAtributo the formatoAtributo to set
	 */
	public void setFormatoAtributo(String formatoAtributo) {
		this.formatoAtributo = formatoAtributo;
	}

	/**
	 * @return the num1
	 */
	public String getNum1() {
		return num1;
	}
	/**
	 * @param num1 the num1 to set
	 */
	public void setNum1(String num1) {
		this.num1 = num1;
	}
	/**
	 * @return the maximoClave
	 */
	public String getMaximoClave() {
		return maximoClave;
	}
	/**
	 * @param maximoClave the maximoClave to set
	 */
	public void setMaximoClave(String maximoClave) {
		this.maximoClave = maximoClave;
	}
	/**
	 * @return the minimoClave
	 */
	public String getMinimoClave() {
		return minimoClave;
	}
	/**
	 * @param minimoClave the minimoClave to set
	 */
	public void setMinimoClave(String minimoClave) {
		this.minimoClave = minimoClave;
	}
	/**
	 * @return the maximoAtributo
	 */
	public String getMaximoAtributo() {
		return maximoAtributo;
	}
	/**
	 * @param maximoAtributo the maximoAtributo to set
	 */
	public void setMaximoAtributo(String maximoAtributo) {
		this.maximoAtributo = maximoAtributo;
	}
	/**
	 * @return the minimoAtributo
	 */
	public String getMinimoAtributo() {
		return minimoAtributo;
	}
	/**
	 * @param minimoAtributo the minimoAtributo to set
	 */
	public void setMinimoAtributo(String minimoAtributo) {
		this.minimoAtributo = minimoAtributo;
	}
	/**
	 * @return the codigoClave
	 */
	public String getCodigoClave() {
		return codigoClave;
	}
	/**
	 * @param codigoClave the codigoClave to set
	 */
	public void setCodigoClave(String codigoClave) {
		this.codigoClave = codigoClave;
	}
	/**
	 * @return the codigoAtributo
	 */
	public String getCodigoAtributo() {
		return codigoAtributo;
	}
	/**
	 * @param codigoAtributo the codigoAtributo to set
	 */
	public void setCodigoAtributo(String codigoAtributo) {
		this.codigoAtributo = codigoAtributo;
	}
	/**
	 * @return the clnatura
	 */
	public String getClnatura() {
		return clnatura;
	}
	/**
	 * @param clnatura the clnatura to set
	 */
	public void setClnatura(String clnatura) {
		this.clnatura = clnatura;
	}
	/**
	 * @return the listaClnatura
	 */
	public List<LlaveValorVO> getListaClnatura() {
		return listaClnatura;
	}
	/**
	 * @param listaClnatura the listaClnatura to set
	 */
	public void setListaClnatura(List<LlaveValorVO> listaClnatura) {
		this.listaClnatura = listaClnatura;
	}
	/**
	 * @return the descripcionFormatoClave
	 */
	public String getDescripcionFormatoClave() {
		return descripcionFormatoClave;
	}
	/**
	 * @param descripcionFormatoClave the descripcionFormatoClave to set
	 */
	public void setDescripcionFormatoClave(String descripcionFormatoClave) {
		this.descripcionFormatoClave = descripcionFormatoClave;
	}
	/**
	 * @return the descripcionFormatoAtributo
	 */
	public String getDescripcionFormatoAtributo() {
		return descripcionFormatoAtributo;
	}
	/**
	 * @param descripcionFormatoAtributo the descripcionFormatoAtributo to set
	 */
	public void setDescripcionFormatoAtributo(String descripcionFormatoAtributo) {
		this.descripcionFormatoAtributo = descripcionFormatoAtributo;
	}
	/**
	 * @return the listaAtributos
	 */
	public List<DatosClaveAtributoVO> getListaAtributos() {
		return listaAtributos;
	}
	/**
	 * @param listaAtributos the listaAtributos to set
	 */
	public void setListaAtributos(List<DatosClaveAtributoVO> listaAtributos) {
		this.listaAtributos = listaAtributos;
	}

	public List<ListaDeValoresVO> getEditaCabeceraTabla5claves() {
		return editaCabeceraTabla5claves;
	}

	public void setEditaCabeceraTabla5claves(
			List<ListaDeValoresVO> editaCabeceraTabla5claves) {
		this.editaCabeceraTabla5claves = editaCabeceraTabla5claves;
	}

	public ListaDeValoresVO getCabeceraTabla5claves() {
		return cabeceraTabla5claves;
	}

	public void setCabeceraTabla5claves(ListaDeValoresVO cabeceraTabla5claves) {
		this.cabeceraTabla5claves = cabeceraTabla5claves;
	}

	public List<DatosClaveAtributoVO> getListaClaveTabla5claves() {
		return listaClaveTabla5claves;
	}

	public void setListaClaveTabla5claves(
			List<DatosClaveAtributoVO> listaClaveTabla5claves) {
		this.listaClaveTabla5claves = listaClaveTabla5claves;
	}

	public void setAtributosTabla5claves(
			List<DatosClaveAtributoVO> atributosTabla5claves) {
		this.atributosTabla5claves = atributosTabla5claves;
	}

	public String getHiddenRadioTipoAcceso() {
		return hiddenRadioTipoAcceso;
	}

	public void setHiddenRadioTipoAcceso(String hiddenRadioTipoAcceso) {
		this.hiddenRadioTipoAcceso = hiddenRadioTipoAcceso;
	}

	public String getRadioTipoAcceso() {
		return radioTipoAcceso;
	}

	public void setRadioTipoAcceso(String radioTipoAcceso) {
		this.radioTipoAcceso = radioTipoAcceso;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getEdita() {
		return edita;
	}

	public void setEdita(String edita) {
		this.edita = edita;
	}

	public void setTablaCincoClavesManager(
			TablaCincoClavesManagerJdbcTemplate tablaCincoClavesManager) {
		this.tablaCincoClavesManager = tablaCincoClavesManager;
	}

	


}
