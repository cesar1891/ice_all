package mx.com.gseguros.wizard.configuracion.producto.general;

import java.util.ArrayList;
import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.HojaVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.RamaVO;
import mx.com.gseguros.wizard.configuracion.producto.model.LlaveValorVO;
import mx.com.gseguros.wizard.configuracion.producto.service.TreeManager;
import mx.com.gseguros.wizard.configuracion.producto.web.Padre;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ArbolProductosAction extends Padre{
	/**
	 * 
	 */
	private static final long serialVersionUID = 62849187843414571L;
	private static final transient Log log = LogFactory
	.getLog(ArbolProductosAction.class);
	private boolean success;
	private TreeManager treeManager;
	private List<RamaVO> listaProductos;
	private List<LlaveValorVO> codigos;
	private String nivel;
	private String tipoObjeto;
	private List<String> posicion;
	private String posicionProducto;
	private List<HojaVO> listaFuncionesJson; 
	
	public String execute() throws Exception {
		return INPUT;
	}

	public String listaProductosJson() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		
		if(isDebugEnabled){
			log.debug("entro al metodo lista productos json");
		}
		if(session.containsKey("ARBOL_PRODUCTOS")){
			if(isDebugEnabled){
				log.debug("si contiene el arbol de productos en session");
			}
			listaProductos=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
		}else{
			listaProductos=treeManager.obtenerProductos();			
			if(session.containsKey("ARBOL_PRODUCTOS_RECARGAR") 
			&& !((List<RamaVO>)session.get("ARBOL_PRODUCTOS_RECARGAR")).isEmpty()) {
				if(isDebugEnabled){
					log.debug("Entro al id de lista Productos json");
				}
				List<RamaVO> temporalTree=(List<RamaVO>) session.get("ARBOL_PRODUCTOS_RECARGAR");
				int posicionProductoActual=-1;
				boolean nueva = true;
				boolean skip= true;
				int longitudArbol= temporalTree.size();
				int posicionNuevoProducto=-1;
				for(RamaVO producto:listaProductos){
					nueva = true;		
					for(int i=0;i<longitudArbol;i++){
						if(Integer.parseInt(producto.getCodigoObjeto())==Integer.parseInt(temporalTree.get(i).getCodigoObjeto())){
							nueva=false;													
						}else{
							posicionNuevoProducto = i;
						}						
					}
					if(nueva){
						producto.setPosicion(posicionNuevoProducto);
						temporalTree.add(posicionNuevoProducto,producto);
						skip=false;
						
					}
				}
				if(!skip){
					longitudArbol= temporalTree.size();				
					for(int i=0;i<longitudArbol;i++){
						temporalTree.get(i).setPosicion(i);
					}
				}
				if(session.containsKey("CODIGO_NIVEL0") && skip){					
					String idProductoEnSession=(String)session.get("CODIGO_NIVEL0");
					if(isDebugEnabled){
						log.debug("contiene codigo nivel cero en session"+idProductoEnSession);
					}
					for(RamaVO ramaProducto:temporalTree){												
						if(idProductoEnSession.equals(ramaProducto.getCodigoObjeto())){
							posicionProductoActual=ramaProducto.getPosicion();
						}
					}
					String nombreProducto = null;
					for(RamaVO ramaProducto:listaProductos){												
						if(idProductoEnSession.equals(ramaProducto.getCodigoObjeto())){
							nombreProducto = ramaProducto.getText();
						}
					}
					if(isDebugEnabled){
						log.debug("nombrePrducto="+nombreProducto);
					}
					if(posicionProductoActual!=-1){
						if(isDebugEnabled){
							log.debug("posicionProductoActual!=-1" +" && podicionProductoActual ="+posicionProductoActual);
						}
						codigos = new ArrayList<LlaveValorVO>();
						LlaveValorVO codigo1= new LlaveValorVO();
						codigo1.setKey(idProductoEnSession);
						codigos.add(codigo1);
						if(isDebugEnabled){
							log.debug("temporalTree.size()="+temporalTree.size());
						}
						if(temporalTree.size()>=posicionProductoActual){
							RamaVO productoActual=temporalTree.get(posicionProductoActual);
							if(nombreProducto!=null)
							productoActual.setText(nombreProducto);
							temporalTree.set(posicionProductoActual, llenandoProductoMetodo(productoActual));
						}
					}
				}
				listaProductos=temporalTree;
				session.remove("ARBOL_PRODUCTOS_RECARGAR");
			}
			if(listaProductos==null){
				listaProductos= new ArrayList<RamaVO>();
			}
			session.put("ARBOL_PRODUCTOS", listaProductos);
		}
		success=true;
		return SUCCESS;
	}
	
	public String llenandoEstructuraProducto() throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		
		success=true;
		if(isDebugEnabled){
			log.debug("entro al metodo");
			log.debug("nivel"+nivel+"codigos"+codigos+"posicionProducto"+posicionProducto);
		}
		
		if(session.containsKey("ARBOL_PRODUCTOS")){
			if(isDebugEnabled){
				log.debug("entro al if");
			}
			listaProductos=(List<RamaVO>) session.get("ARBOL_PRODUCTOS");
			if(codigos!=null && !codigos.isEmpty() && nivel!=null && StringUtils.isNotBlank(nivel) && !nivel.equals("undefined")
					&& posicionProducto!=null && StringUtils.isNotBlank(posicionProducto) && !posicionProducto.equals("undefined")){
				for(RamaVO producto: listaProductos){
					producto.setExpanded(false);
				}
				
				RamaVO productoActual=listaProductos.get(Integer.parseInt(posicionProducto));
				//productoActual.setExpanded(true);
				productoActual =llenandoProductoMetodo(productoActual);
				//if(isDebugEnabled){
					//log.debug(productoActual);
				//}
				listaProductos.set(Integer.parseInt(posicionProducto), productoActual);					
				removerLlavesSession(Integer.parseInt(nivel));				
				session.put("CODIGO_NIVEL0", codigos.get(0).getKey());
			}
			if(isDebugEnabled && listaProductos != null){
				log.debug("objeto lista produtos size: " + listaProductos.size());
			}
			session.put("ARBOL_PRODUCTOS", listaProductos);
		}
		success=true;
		return SUCCESS;
	}
	public RamaVO llenandoProductoMetodo(RamaVO productoActual)throws Exception{
		boolean isDebugEnabled = log.isDebugEnabled();
		
		this.nivel = "1";
		if(isDebugEnabled){
			log.debug("el problema va mas lejos");
		}
		productoActual.setExpanded(true);
		int codeIndex=Integer.parseInt(nivel);
		ArrayList<String> parametros1 = new ArrayList<String>();
		parametros1.add(codigos.get(0).getKey());
		if(isDebugEnabled){
			log.debug("parametros1.get(0)"+parametros1.get(0));
		}
		
		List<RamaVO> children = treeManager.obtenerEstructuraProducto();
		
		for(RamaVO hijo:children){							
			if("DatosFijos".equals(hijo.getTipoObjeto())){
				hijo.setLeaf(true);
			}else{
				List<RamaVO> hijos3= treeManager.obtenerNiveles(hijo.getTipoObjeto(),parametros1);
			
				if(hijos3!=null && !hijos3.isEmpty()){	
						for(RamaVO ramaNivel3:hijos3){
						ArrayList<String> parametros2 = new ArrayList<String>();
						parametros2.addAll(parametros1);
						parametros2.add(ramaNivel3.getCodigoObjeto());
						if("Inciso".equals(ramaNivel3.getTipoObjeto())){
							List<RamaVO> ramasNivel4 = treeManager.obtenerEstructuraSituacion();
							if(ramasNivel4!=null && !ramasNivel4.isEmpty()){
								for(RamaVO ramaNivel4: ramasNivel4){
									if(isDebugEnabled){
										log.debug("tipo de objeto nivel 4"+ramaNivel4.getTipoObjeto());
									}	
									
										List<RamaVO> ramasNivel5 = treeManager.obtenerNiveles(ramaNivel4.getTipoObjeto(), parametros2);	
										
										log.debug("NIVEL5: " + ramasNivel5.size());
										
										if(ramasNivel5!=null && !ramasNivel5.isEmpty()){															
											for(RamaVO ramaNivel5:ramasNivel5){
												
												ArrayList<String> parametros3 = new ArrayList<String>();
												parametros3.addAll(parametros2);
												parametros3.add(ramaNivel5.getCodigoObjeto());
												List<RamaVO> ramasNivel6=null;
												/*if("Cobertura".equals(ramaNivel4.getTipoObjeto())){
													ramasNivel6 = treeManager.obtenerEstructuraCobertura();																											
												}*/																								
												/*if("Objeto".equals(ramaNivel4.getTipoObjeto())){
													ramasNivel6 = treeManager.obtenerEstructuraObjetos();																											
												}*/
												if("Planes".equals(ramaNivel4.getTipoObjeto())){
													ramasNivel6 = treeManager.obtenerEstructuraPlanes();																											
												}
												//log.debug("RAMAS NIVEL 6" + ramasNivel6.size());
												if(ramasNivel6!=null && !ramasNivel6.isEmpty()){
													for(RamaVO ramaNivel6: ramasNivel6){
														if(isDebugEnabled){
															log.debug("ramaNivel6.getCodigoObjeto() = " + ramaNivel6.getCodigoObjeto());
															log.debug("!!!!!!!!!!!!!!!!!!!!parametros3.get(0)="+parametros3.get(0));
															log.debug("!!!!!!!!!!!!!!!!!!!!parametros3.get(1)="+parametros3.get(1));
															log.debug("!!!!!!!!!!!!!!!!!!!!parametros3.get(2)="+parametros3.get(2));
														}
														List<RamaVO> ramasNivel7=treeManager.obtenerNiveles(ramaNivel6.getTipoObjeto(), parametros3);
														if(ramasNivel7!=null && !ramasNivel7.isEmpty()){
															for(RamaVO ramaNivel7:ramasNivel7){
																	ArrayList<String> parametros4 = new ArrayList<String>();
																	parametros4.addAll(parametros3);
																	parametros4.add(ramaNivel7.getCodigoObjeto());
																	if("Cobertura".equals(ramaNivel6.getTipoObjeto())){
																		List<RamaVO> ramasNivel8=null;
																		ramasNivel8 = treeManager.obtenerEstructuraCobertura();
																		if(ramasNivel8!=null && !ramasNivel8.isEmpty()){
																			for(RamaVO ramaNivel8:ramasNivel8){
																				List<RamaVO> ramasNivel9=treeManager.obtenerNiveles(ramaNivel8.getTipoObjeto(), parametros4);
																				if(ramasNivel9!=null && !ramasNivel9.isEmpty()){
																					for(RamaVO ramaNivel9:ramasNivel9){
																						ramaNivel9.setLeaf(true);
																					}
																					ramaNivel8.setChildren(ramasNivel9.toArray());
																				}else{
																					ramaNivel8.setLeaf(true);
																				}
																			}
																			ramaNivel7.setChildren(ramasNivel8.toArray());
																		}else{
																			ramaNivel7.setLeaf(true);
																		}
																	}else{
																		ramaNivel7.setLeaf(true);
																	}
															}
															ramaNivel6.setChildren(ramasNivel7.toArray());																
														}else{
															if(isDebugEnabled){
																log.debug("seteo rama Nivel 6 leaf=true");
															}
															ramaNivel6.setLeaf(true);
														}
													}
													ramaNivel5.setChildren(ramasNivel6.toArray());
												}else{
													ramaNivel5.setLeaf(true);
												}
												ramaNivel4.setChildren(ramasNivel5.toArray());
											}
										}else{
											ramaNivel4.setLeaf(true);
										}
//									}else if("Rol".equals(ramaNivel4.getTipoObjeto()) || "AtributosVariables".equals(ramaNivel4.getTipoObjeto())){													
//										
//										List<RamaVO> ramasNivel5 = treeManager.obtenerNiveles(ramaNivel4.getTipoObjeto(), parametros2);													
//										if(ramasNivel5!=null && !ramasNivel5.isEmpty()){
//											for(RamaVO ramaNivel5:ramasNivel5){
//												ramaNivel5.setLeaf(true);
//											}
//											ramaNivel4.setChildren(ramasNivel5.toArray());
//										}else{
//											ramaNivel4.setLeaf(true);
//										}
//									}else {
//										ramaNivel4.setLeaf(true);
//									}
								}
								ramaNivel3.setChildren(ramasNivel4.toArray());
							}else{
								ramaNivel3.setLeaf(true);
							}
						}else{
							ramaNivel3.setLeaf(true);											
						}
					}
				hijo.setChildren(hijos3.toArray());							
			}else{
				hijo.setLeaf(true);
			}
			}
		
		}
		productoActual.setChildren(children.toArray());
		success=true;
		return productoActual;
	}
	public void removerLlavesSession(int nivelRemover){

		for(int i=nivelRemover;i<7;i++){
			String llaveEnSession="CODIGO_NIVEL"+Integer.toString(i);
			if(session.containsKey(llaveEnSession)){
				session.remove(llaveEnSession);
			}	
		}
	}

	 public String subirCodigosASession(){
		  boolean isDebugEnabled = log.isDebugEnabled();
		  
		  int codeIndex=Integer.parseInt(nivel);
		  if(isDebugEnabled){
			  log.debug("codeIndex="+codeIndex);
		  }
		  log.debug("Valor de list codigos: "+ codigos);
		  if(codigos!=null && !codigos.isEmpty()){
		   int cs=codigos.size();
		   if(isDebugEnabled){
			   log.debug("codigos.size()="+cs);
		   }
		   removerLlavesSession(cs);
		   for(int i=0;i<cs;i++){
		    String llaveEnSession="CODIGO_NIVEL"+Integer.toString(i);
		    if(isDebugEnabled){
		    	log.debug("debugueaLlaves"+llaveEnSession);
		    }
		    session.put(llaveEnSession,codigos.get(i).getKey());
		    String debugueaValoresEnSession= (String) session.get(llaveEnSession);
		    if(isDebugEnabled){
		    	log.debug("debugueaLlaves"+debugueaValoresEnSession);
		    }
		   }
		  }
		  if(tipoObjeto!=null && tipoObjeto.equals("Rol")){
		   if(nivel.equals("3")){
		    String codigoRol=(String)session.get("CODIGO_NIVEL2");
		    if(isDebugEnabled){
		    	log.debug("codigoRol="+codigoRol);
		    }
		    session.put("CODIGO_ROL", codigoRol);
		    session.remove("CODIGO_NIVEL2");
		   }
		   if(nivel.equals("5")){
		    String codigoRol=(String)session.get("CODIGO_NIVEL4");
		    if(isDebugEnabled){
		    	log.debug("codigoRol="+codigoRol);
		    }
		    session.put("CODIGO_ROL", codigoRol);
		    session.remove("CODIGO_NIVEL4");
		   }
		  }
		  if(tipoObjeto!=null && tipoObjeto.equals("AtributosVariables")){
			  if(isDebugEnabled){
				  log.debug("tipo objeto = AtributosVariables");
			  }
		   if(nivel.equals("3")){
		    String codigoAtributoVariable=(String)session.get("CODIGO_NIVEL2");
		    if(isDebugEnabled){
		    	log.debug("codigoAtributoVariable="+codigoAtributoVariable);
		    }
		    session.put("CODIGO_ATRIBUTO_VARIABLE_UNICO", codigoAtributoVariable);
		    session.remove("CODIGO_NIVEL2");
		   }
		   if(nivel.equals("5")){
		    String codigoAtributoVariable=(String)session.get("CODIGO_NIVEL4");
		    if(isDebugEnabled){
		    	log.debug("codigoAtributoVariable="+codigoAtributoVariable);
		    }
		    session.put("CODIGO_ATRIBUTO_VARIABLE_UNICO", codigoAtributoVariable);
		    session.remove("CODIGO_NIVEL4");
		   }
		   if(nivel.equals("7")){
		    String codigoAtributoVariable=(String)session.get("CODIGO_NIVEL6");
		    if(isDebugEnabled){
		    	log.debug("codigoAtributoVariable="+codigoAtributoVariable);
		    }
		    session.put("CODIGO_ATRIBUTO_VARIABLE_UNICO", codigoAtributoVariable);
		    session.remove("CODIGO_NIVEL6");
		   }
		  }
		  success= true;
		  return SUCCESS;
		 }
	public String vistaPantallasJson(){
		listaFuncionesJson = new ArrayList<HojaVO>();

		HojaVO hojaVO = null;
		
		hojaVO = new HojaVO();
		hojaVO.setId("1");
		hojaVO.setText("Definicion de Producto");
		hojaVO.setFuncion("Producto");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("2");
		hojaVO.setText("Riesgo");			
		hojaVO.setFuncion("Inciso");
		listaFuncionesJson.add(hojaVO);
			
		hojaVO = new HojaVO();			
		hojaVO.setId("3");
		hojaVO.setText("Datos Variables");			
		hojaVO.setFuncion("AtributosVariables");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("4");
		hojaVO.setText("Roles");			
		hojaVO.setFuncion("Rol");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("5");
		hojaVO.setText("Vista Previa");			
		hojaVO.setFuncion("DataView");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("6");
		hojaVO.setText("Coberturas");			
		hojaVO.setFuncion("Cobertura");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("7");
		hojaVO.setText("Datos Fijos");			
		hojaVO.setFuncion("DatosFijos");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("8");
		hojaVO.setText("Concepto Global");			
		hojaVO.setFuncion("ConceptoGlobal");
		listaFuncionesJson.add(hojaVO);
		
		hojaVO = new HojaVO();			
		hojaVO.setId("9");
		hojaVO.setText("Objeto");			
		hojaVO.setFuncion("Objeto");
		listaFuncionesJson.add(hojaVO);
		success=true;
		return SUCCESS;
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
	 * @return the listaProductos
	 */
	public List<RamaVO> getListaProductos() {
		return listaProductos;
	}


	/**
	 * @param listaProductos the listaProductos to set
	 */
	public void setListaProductos(List<RamaVO> listaProductos) {
		this.listaProductos = listaProductos;
	}

	/**
	 * @param treeManager the treeManager to set
	 */
	public void setTreeManager(TreeManager treeManager) {
		this.treeManager = treeManager;
	}

	/**
	 * @return the codigos
	 */
	public List<LlaveValorVO> getCodigos() {
		return codigos;
	}

	/**
	 * @param codigos the codigos to set
	 */
	public void setCodigos(List<LlaveValorVO> codigos) {
		this.codigos = codigos;
	}

	/**
	 * @return the nivel
	 */
	public String getNivel() {
		return nivel;
	}

	/**
	 * @param nivel the nivel to set
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	/**
	 * @return the posicion
	 */
	public List<String> getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(List<String> posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the listaFuncionesJson
	 */
	public List<HojaVO> getListaFuncionesJson() {
		return listaFuncionesJson;
	}

	/**
	 * @param listaFuncionesJson the listaFuncionesJson to set
	 */
	public void setListaFuncionesJson(List<HojaVO> listaFuncionesJson) {
		this.listaFuncionesJson = listaFuncionesJson;
	}

	/**
	 * @return the posicionProducto
	 */
	public String getPosicionProducto() {
		return posicionProducto;
	}

	/**
	 * @param posicionProducto the posicionProducto to set
	 */
	public void setPosicionProducto(String posicionProducto) {
		this.posicionProducto = posicionProducto;
	}

	/**
	 * @return the tipoObjeto
	 */
	public String getTipoObjeto() {
		return tipoObjeto;
	}

	/**
	 * @param tipoObjeto the tipoObjeto to set
	 */
	public void setTipoObjeto(String tipoObjeto) {
		this.tipoObjeto = tipoObjeto;
	}

}
