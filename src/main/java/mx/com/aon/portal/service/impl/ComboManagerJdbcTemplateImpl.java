package mx.com.aon.portal.service.impl;

import java.util.HashMap;
import java.util.List;

import mx.com.aon.portal.service.CombosManager2;
import mx.com.aon.portal.util.ConvertUtil;
import mx.com.gseguros.exception.ApplicationException;

import org.apache.log4j.Logger;

public class ComboManagerJdbcTemplateImpl extends AbstractManagerJdbcTemplateInvoke implements CombosManager2 {

    //private static Logger logger = Logger.getLogger(ComboManagerJdbcTemplateImpl.class);
    
    private Logger log=Logger.getLogger(ComboManagerJdbcTemplateImpl.class);
    
    @SuppressWarnings("unchecked")
	public List comboRolFuncionalidades (String pv_cdelemento_i) throws ApplicationException{
        HashMap map = new HashMap();
        map.put("pv_cdelemento_i",pv_cdelemento_i);
        String endpointName = "COMBO_ROL_FUNCIONALIDAD";
        return  getAllBackBoneInvoke(map, endpointName);

    }

    @SuppressWarnings("unchecked")
	public List comboUsuarioFuncionalidades (String pv_cdelemento_i, String pv_cdsisrol_i) throws ApplicationException{
        HashMap map = new HashMap();
        map.put("pv_cdelemento_i", pv_cdelemento_i);
        map.put("pv_cdrol_i", pv_cdsisrol_i);
        String endpointName = "COMBO_USUARIO_FUNCIONALIDAD";
        return  getAllBackBoneInvoke(map, endpointName);
    }


    @SuppressWarnings("unchecked")
	public List comboCargaMasiva () throws ApplicationException{
        HashMap map = new HashMap();
        //los parametos van nulos porque en realidad no los esta utilizando el plsql pero necesito enviarlos como parametros
        map.put("pv_cdregion_i",null);
        map.put("pv_cdpais_i",null);
        String endpointName = "COMBO_CARGA_MASIVA";
        return  getAllBackBoneInvoke(map, endpointName);

    }

    @SuppressWarnings("unchecked")
	public List comboTiposActividad () throws ApplicationException{
        HashMap map = new HashMap();

        map.put("pv_cdtabla_i","TTIPOCANCEL");
        map.put("pv_cdidioma_i","1");
        map.put("pv_cdregion_i","1");
        String endpointName = "COMBO_TIPO_OPERACION";
        return  getAllBackBoneInvoke(map, endpointName);

    }
    
    @SuppressWarnings("unchecked")
	public List comboObtieneAlgoritmos () throws ApplicationException{
        HashMap map = new HashMap();
   
        String endpointName = "COMBO_OBTIENE_ALGORITMOS";
        return  getAllBackBoneInvoke(map, endpointName);

    }

    
    @SuppressWarnings("unchecked")
	public List obtienePais() throws ApplicationException {

    	HashMap map = new HashMap();
        String endpointName = "COMBO_OBTIENE_PAIS";
    	
		return getAllBackBoneInvoke(map, endpointName);
	}
    
    @SuppressWarnings("unchecked")
	public List obtenerCatalogoAon(String pv_country_code_i, String pv_cdsistema_i) throws ApplicationException {
		HashMap map = new HashMap();
		
		map.put("pv_country_code_i", pv_country_code_i);
		map.put("pv_cdsistema_i",pv_cdsistema_i);
		
		return getAllBackBoneInvoke(map,"COMBO_OBTIENE_CATALOGO_AON");
	}
    /*
     * pv_country_code_i", OracleTypes.VARCHAR));
            declareParameter(new SqlParameter("pv_cdsistema_i", OracleTypes.VARCHAR));
     * @see mx.com.aon.portal.service.CombosManager2#obtenerCatalogoExterno(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    
    @SuppressWarnings("unchecked")
	public List obtenerCatalogoExterno(String pv_country_code_i, String pv_cdsistema_i, String pv_otclave01_i, String pv_otvalor_i, String pv_cdtablaext_i) throws ApplicationException {
		HashMap map = new HashMap();
		map.put("pv_country_code_i", pv_country_code_i);
		map.put("pv_cdsistema_i", pv_cdsistema_i);
		map.put("pv_otclave01_i", pv_otclave01_i);
		map.put("pv_otvalor_i", pv_otvalor_i);
		map.put("pv_cdtablaext_i", pv_cdtablaext_i);

		return getAllBackBoneInvoke(map,"COMBO_OBTIENE_CATALOGO_EXTERNO");
	}	
    
    @SuppressWarnings("unchecked")
	public List obtenerCodigo(String pv_country_code_i) throws ApplicationException {
		HashMap map = new HashMap();
		
		map.put("pv_country_code_i", pv_country_code_i);
		
		return getAllBackBoneInvoke(map,"COMBO_OBTIENE_CODIGO");
	}
    
    @SuppressWarnings("unchecked")
	public List comboObtienePaises()throws ApplicationException {
   	 HashMap map = new HashMap();
          
       String endpointName = "COMBO_OBTIENE_PAISES";
       return  getAllBackBoneInvoke(map, endpointName);
    }
       
    @SuppressWarnings("unchecked")
	public List comboObtieneUso()throws ApplicationException {
   	 HashMap map = new HashMap();
   	 map.put("pv_cdtabla_i","INDUSO");  //se usa la tabla logica CINDUSO pero no se cual es la clave
          
     String endpointName = "COMBO_OBTIENE_USO";
     return  getAllBackBoneInvoke(map, endpointName);
    }
    
    @SuppressWarnings("unchecked")
	public List comboObtieneSistemaExterno()throws ApplicationException {
      	 HashMap map = new HashMap();
      	 map.put("pv_cdtabla_i","CSYSEXTR");  //se usa la tabla logica CINDUSO pero no se cual es la clave
             
        String endpointName = "COMBO_OBTIENE_SISTEMA_EXTERNO";
        return  getAllBackBoneInvoke(map, endpointName);
       }
    
    @SuppressWarnings("unchecked")
    public List comboCodigoPostal(String pv_cdpais_i)throws ApplicationException {
      	 HashMap map = new HashMap();
           map.put("pv_cdpais_i", pv_cdpais_i);
          String endpointName = "COMBO_CODIGO_POSTAL";
          return  getAllBackBoneInvoke(map, endpointName);
       }

	@SuppressWarnings("unchecked")
	public List comboCatalogosCompuestos(String cdColumna, String cdClave1,
			String cdClave2) throws ApplicationException {
		HashMap map = new HashMap ();
		map.put("pv_columna_i", cdColumna);
		map.put("pv_cve1_i", cdClave1);
		map.put("pv_cve2_i", cdClave2);

		return getAllBackBoneInvoke(map, "COMBO_CATALOGOS_COMPUESTOS");
	}
	
	 @SuppressWarnings("unchecked")
	public List comboObtienePoliza(String pv_cdunieco_i, String pv_cdramo_i, String pv_cdperson_i )throws ApplicationException {
		 HashMap map = new HashMap();
		 map.put("pv_cdunieco_i", ConvertUtil.nvl( pv_cdunieco_i));
		 map.put("pv_cdramo_i", ConvertUtil.nvl( pv_cdramo_i));
		 map.put("pv_cdperson_i", ConvertUtil.nvl( pv_cdperson_i));
	       
	    String endpointName = "COMBO_OBTIENE_POLIZA";
	    return  getAllBackBoneInvoke(map, endpointName);
	}

	 public List comboListaValores (String ottipotb) throws ApplicationException{
	        HashMap map = new HashMap();

	        map.put("pv_ottipotb_i", ottipotb);
	       
	        
	        String endpointName = "COMBO_LISTA_VALORES_INST_PAGO";
	        return  getAllBackBoneInvoke(map, endpointName);

	    }
	 public List comboCondicionInstPago () throws ApplicationException{
	        HashMap map = new HashMap();

	        String endpointName = "COMBO_CONDICION_INST_PAGO";
	        return  getAllBackBoneInvoke(map, endpointName);

	    }
     
}