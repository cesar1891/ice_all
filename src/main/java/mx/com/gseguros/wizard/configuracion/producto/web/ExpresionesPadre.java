package mx.com.gseguros.wizard.configuracion.producto.web;

import java.util.ArrayList;
import java.util.List;

import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ClaveVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.ExpresionVO;
import mx.com.gseguros.wizard.configuracion.producto.expresiones.model.VariableVO;
import mx.com.gseguros.wizard.configuracion.producto.service.ExpresionesManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ExpresionesPadre extends Padre{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2223613492103902708L;
	private static final transient Log log = LogFactory.getLog(ExpresionesPadre.class);	
	protected ExpresionesManager expresionesManager;
	

	
	public int insertarExpresion(boolean success, ExpresionVO e)throws Exception{
		log.debug("!!!!!!!!!!!!!Dentro de insertar Expresion");
		int codigoExpresion = expresionesManager.insertarExpresion(e, success);
		e.setCodigoExpresion(codigoExpresion);
		
		log.debug("codigoExpresion +"+codigoExpresion);
		//if(codigoExpresion!=0){
		//	expresionesManager.agregarVariables(e.getVariablesTemporales(), codigoExpresion, success);
//			if(ActionContext.getContext().getSession().containsKey("CATALOGO_VARIABLES")){
//				log.debug("session contiene CATALOGO_VARIABLES");
//				List<VariableVO> temp=(List<VariableVO>)ActionContext.getContext().getSession().get("CATALOGO_VARIABLES");
//				if(!temp.isEmpty()){
//					log.debug("CATALOGO_VARIABLES no es empty");
//					ActionContext.getContext().getSession().remove("CATALOGO_VARIABLES");					
//					e.setVariablesTemporales(temp);					
//					expresionesManager.agregarVariables(temp, codigoExpresion, success);					
//				}
//			}
			
		//}
		return codigoExpresion;
	}
	
	public List<ExpresionVO> cargaExpresion (int codigoExpresion)throws Exception{
		log.debug("codigo expresion en expresionesPadre ==> cargaExpresion() = " + codigoExpresion);
		List<ExpresionVO> listaExpresion = null;
		if(codigoExpresion != 0){	
			listaExpresion = expresionesManager.obtieneExpresion(codigoExpresion);
		}else{			
			codigoExpresion = expresionesManager.obtieneSecuenciaExpresion();
			listaExpresion = new ArrayList<ExpresionVO>();
			ExpresionVO e = new ExpresionVO();
			e.setCodigoExpresion(codigoExpresion);
			listaExpresion.add(e);
		}
		if(!(listaExpresion!=null && !listaExpresion.isEmpty())){
			listaExpresion = new ArrayList<ExpresionVO>();
			ExpresionVO e = new ExpresionVO();
			e.setCodigoExpresion(codigoExpresion);
			listaExpresion.add(e);
		}	
		return listaExpresion;
	}
	
	public List<VariableVO> cargaVariablesExpresion (int codigoExpresion)throws Exception{
		
		List<VariableVO> listaVariable = null;
		if(codigoExpresion != 0){	
			listaVariable = expresionesManager.obtieneVariableExpresion(codigoExpresion);
			if(listaVariable!=null && listaVariable.isEmpty()){
				List<ClaveVO> clavesVariableLocal= null;
				for(VariableVO variableLocal: listaVariable){
					log.debug("variableLocal.getCodigoExpresion()="+variableLocal.getCodigoExpresion());
					log.debug("variableLocal.getCodigoVariable()="+variableLocal.getCodigoVariable());
					log.debug("variableLocal.getColumna()="+variableLocal.getColumna());
					log.debug("variableLocal.getTabla()="+variableLocal.getTabla());
					clavesVariableLocal= expresionesManager.obtieneClaves(variableLocal);
					if(clavesVariableLocal==null)
						clavesVariableLocal = new ArrayList<ClaveVO>();
					variableLocal.setClaves(clavesVariableLocal);
				}
			}
		}
		return listaVariable;
	}

	//Getters && Setters
	public void setExpresionesManager(ExpresionesManager expresionesManager) {
		this.expresionesManager = expresionesManager;
	}

		
}
