package mx.com.gseguros.wizard.configuracion.producto.expresiones.model;

import java.io.Serializable;
/**
 * Composer con atributo VariableVO y ClaveVO
 * @author Ricardo
 *
 */
public class ComposerVariableVOClaveVO implements Serializable {
	
	private static final long serialVersionUID = -1744943958874411770L;

	private VariableVO variableVO;
	
	private ClaveVO claveVO;

	public VariableVO getVariableVO() {
		return variableVO;
	}

	public void setVariableVO(VariableVO variableVO) {
		this.variableVO = variableVO;
	}

	public ClaveVO getClaveVO() {
		return claveVO;
	}

	public void setClaveVO(ClaveVO claveVO) {
		this.claveVO = claveVO;
	}
	
}