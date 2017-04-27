package mx.com.gseguros.portal.consultas.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.com.gseguros.portal.consultas.model.AseguradoVO;
import mx.com.gseguros.portal.consultas.model.PerfilAseguradoVO;
import mx.com.gseguros.portal.consultas.service.ConsultasPerfilMedicoManager;
import mx.com.gseguros.portal.consultas.service.ConsultasPolizaManager;
import mx.com.gseguros.portal.general.model.PolizaVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.consultas.dao.ConsultasPerfilMedicoDAO;

@Service
public class ConsultasPerfilMedicoManagerImpl implements ConsultasPerfilMedicoManager{
	
	static final Logger logger = LoggerFactory.getLogger(ConsultasPerfilMedicoManagerImpl.class);
		
	@Autowired
	@Qualifier("consultasPerfilMedicoDAOICEImpl")
	private ConsultasPerfilMedicoDAO consultasICEDAO;
	
	@Autowired
	@Qualifier("consultasPerfilMedicoDAOSISAImpl")
	private ConsultasPerfilMedicoDAO consultasSISADAO;
		
	@Value("${consultar.perfil.medico.sisa}")
    private boolean consultarPerfilMedicoSisa;
	
	@Autowired
	private ConsultasPolizaManager consultasPolizaManager;
	
	private List<AseguradoVO> datosAsegurados;
	private long totalCount;
	private List<Map<String, String>> list;
	
	
	//Consulta en SISA el perfil medico de la lista de asegurados dada 
	private List<Map<String, String>> consultaPerfilAseguradosSISA(Map<String,String> params) throws Exception{
		
		List<Map<String, String>> listaPerfil = new ArrayList<Map<String,String>>();
		String paso = "Realizando consulta en SISA";
		logger.debug(paso);
		
		try {
			List<Map<String, String>> listaICE = new ArrayList<Map<String,String>>();
			listaICE = consultasSISADAO.consultaPerfilAsegurados(params);
		}
		catch (Exception e){
			throw new ApplicationException("Ha ocurrido un error al consultar el perfil del asegurado en SICAPS",e);
		}
		return listaPerfil;
	}
	
	//Consulta en SICAPS el perfil medico de la lista de asegurados dada 
	private List<Map<String, String>> consultaPerfilAseguradosICE(Map<String,String> params) throws Exception{
		
		List<Map<String, String>> listaICE = new ArrayList<Map<String,String>>();
		String paso = "Realizando consulta en SICAPS";
		logger.debug(paso);
		
		try {
			listaICE = consultasICEDAO.consultaPerfilAsegurados(params);
		}
		catch (Exception e){
			throw new ApplicationException("Ha ocurrido un error al consultar el perfil del asegurado en SICAPS",e);
		}
		return listaICE;
	}
	
	//Consulta el perfil medico de los asegurados en ambas plataformas
	@Override
	public List<Map<String, String>> consultaPerfilAsegurados(Map<String, String> params) throws Exception{
		
		List<Map<String, String>> listaPerfil = new ArrayList<Map<String,String>>();
		String paso;
		
		try{
			if (consultarPerfilMedicoSisa){//se incorporara propiedad en el global para definir si debe ir a SISA consultar.perfil.medico.sisa = true o false
				try{
					paso = "Estableciendo comunicacion con SISA";
					logger.debug(paso);
					listaPerfil = consultaPerfilAseguradosSISA(params);
				}
				catch (Exception ex){ //en caso de que el llamado a SISA arroje un error, se debe llamar al SP de ICE
					paso = "Error haciendo el llamado al SP de SISA, se hara el llamado a SICAPS";
					logger.debug(paso);
					//llamar a la consulta de ICE
					listaPerfil=consultaPerfilAseguradosICE(params);
				}
			}
			else {
				paso = "La consulta de SISA se encuentra apagada, se consultara en SICAPS";
				logger.debug(paso);
				//llamar a la consulta de ICE
				listaPerfil=consultaPerfilAseguradosICE(params);
			}
		}
		catch (Exception e){
			throw new ApplicationException("Ha ocurrido un error al consultar el perfil de los asegurados",e);
		}
		return listaPerfil;
	};
	
	//metodo llamado desde el action para la consulta de asegurados y sus perfiles medicos
	@Override
	public List<AseguradoVO> obtienePerfilAsegurados(PolizaVO poliza, long start, long limit) throws Exception {
		try{
			this.datosAsegurados = consultasPolizaManager.obtieneAsegurados(poliza,start,limit);			
			String listaPersonas = "";
			//consulta la informacion de los asegurados
			String paso="Consultando informacion de asegurados";
			logger.debug(paso);
			List<AseguradoVO> datosAseguradosDef = new ArrayList<AseguradoVO>();
			
			if(datosAsegurados != null && datosAsegurados.size() > 0) {
				paso="consulta de asegurados exitosa: "+datosAsegurados.size();
				logger.debug(paso);
				totalCount = datosAsegurados.get(0).getTotal();
				
				//se debe armar la lista de cdperson para hacer la consulta de perfiles
				String listaPersonasTmp="";
				for(int k=0; k<datosAsegurados.size(); k++){
					AseguradoVO asegurado = new AseguradoVO();
		        	asegurado = datosAsegurados.get(k);
		        	listaPersonasTmp =  asegurado.getCdperson() + ",";
		        	listaPersonas = listaPersonas + listaPersonasTmp;
				}
				paso = "armando la lista de personas para consultar el perfil medico: ";
				logger.debug(paso);
				listaPersonas=listaPersonas.substring(0, listaPersonas.length()-1);
				
				//consulta la informacion de perfiles
				paso="consultando la informacion de los perfiles de las siguientes personas: "+listaPersonas;
				logger.debug(paso);
				
				Map<String,String> parametro = new HashMap<String,String>();
				parametro.put("pv_lsperson_i", listaPersonas);
				try{
		            list = consultaPerfilAsegurados(parametro);
		            paso = "Consulta exitosa de los perfiles: " + list.size();
		            logger.debug(paso);
		            //se debe recorrer la lista de AseguradoVO y recorrer la lista de perfiles para completar el objeto 
		            for(int i=0; i<datosAsegurados.size(); i++){
		            	
		            	//se obtienen valores del asegurado
		            	AseguradoVO asegurado = new AseguradoVO();
		            	asegurado = datosAsegurados.get(i);
		            	
		            	for(int j=0; j<list.size(); j++){
		            		//se le agregan los datos del perfil
		            		if (datosAsegurados.get(i).getCdperson().equals(list.get(j).get("CDPERSON"))){
		            			String cantIcd = list.get(j).get("CANT_ICD");
		            			datosAsegurados.get(i).setCantIcd(cantIcd);
		            			
		            			String maxPerfil = list.get(j).get("MAX_PERFIL");
		            			datosAsegurados.get(i).setMaxPerfil(maxPerfil);
		            			
		            			String numPerfil = list.get(j).get("NUM_PERFIL");
		            			datosAsegurados.get(i).setNumPerfil(numPerfil);
		            			
		            			String perfilFinal=list.get(j).get("PERFIL_FINAL");
		            			datosAsegurados.get(i).setPerfilFinal(perfilFinal);
		            		}//if
		            	}//for j
		            }//for i
		            paso= "agregando informacion de perfiles a los asegurados: ";
            		logger.debug(paso);
				}//try
				catch (Exception ex){
					logger.debug("Error consultando el perfil de los asegurados");
				}
			}//if
		}//try
		catch (Exception e){
			throw new ApplicationException("Ha ocurrido un error al consultar los asegurados",e);
		}
		logger.debug("retorno datos de asegurados: "+datosAsegurados.size());
		return datosAsegurados;
	}
	
	//consulta la lista de ICDs de un asegurado dado
	@Override
	public PerfilAseguradoVO consultaICDSAsegurado(Map<String,String> params) throws Exception{
		PerfilAseguradoVO listaIcds = new PerfilAseguradoVO();
		String paso;
		try{
			paso = "Consultando lista de ICDs del asegurado";
			logger.debug(paso);
			List<Map<String, String>> lista;
			lista = (List<Map<String, String>>) consultasICEDAO.consultaICDSAsegurado(params);
			listaIcds.setIcds(lista);
		}
		catch (Exception e){
			throw new ApplicationException("Ha ocurrido un error al consultar los ICDs del asegurado",e);
		}
		return listaIcds;
	};
	
	
}