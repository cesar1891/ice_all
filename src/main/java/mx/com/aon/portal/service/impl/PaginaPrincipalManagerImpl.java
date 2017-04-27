package mx.com.aon.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.aon.portal.model.PortalVO;
import mx.com.aon.portal.service.PaginaPrincipalManager;
import mx.com.aon.portal.util.WrapperResultados;
import mx.com.gseguros.exception.ApplicationException;

public class PaginaPrincipalManagerImpl extends
AbstractManagerJdbcTemplateInvoke implements PaginaPrincipalManager {

	/**
	 *Serial Version 
	 */
	private static final long serialVersionUID = -6140188280302464695L;
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.aon.portal.service.PaginaPrincipalManager#getConfiguracionInicial(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PortalVO> getConfiguracionInicial(String rolUsuario, String elemento)throws ApplicationException{
		
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("PV_CDELEMENTO_I", elemento);
		params.put("PV_CDROL_I", rolUsuario);
		List<PortalVO> componentes=null;
		WrapperResultados res = this.returnBackBoneInvoke(params, "CARGA_COMPONENTES");
		componentes = (List<PortalVO>)res.getItemList();
			
		return componentes;
	
	}
	
	@SuppressWarnings("unchecked")
	public String getUserEmail(String cdUsuario)throws ApplicationException{
		String email=null;
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("pv_CDUSUARI_i", cdUsuario);
		
		ArrayList<String> emails;
		
		WrapperResultados res = this.returnBackBoneInvoke(params, "OBTIENE_EMAIL");
		emails=(ArrayList<String>) res.getItemList();
			
		if(emails!=null && !emails.isEmpty()){
			if(emails.get(0)!=null){
				email= (String) emails.get(0);
			}
		}
		return email;
	}

}

