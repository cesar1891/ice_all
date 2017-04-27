package mx.com.gseguros.portal.emision.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import mx.com.gseguros.portal.consultas.dao.ConsultasDAO;
import mx.com.gseguros.portal.emision.dao.EmisionDAO;
import mx.com.gseguros.portal.emision.service.ClienteTramiteManager;
import mx.com.gseguros.portal.siniestros.dao.SiniestrosDAO;
import mx.com.gseguros.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteTramiteManagerImpl implements ClienteTramiteManager
{
	private static Logger logger = LoggerFactory.getLogger(ClienteTramiteManagerImpl.class);
	
	@Autowired
	private ConsultasDAO consultasDAO;
	
	@Autowired
	private EmisionDAO emisionDAO;
	
	@Autowired
	private SiniestrosDAO siniestrosDAO;
	
	@Override
	public Map<String,String> pantallaContratanteTramite(String ntramite) throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ pantallaContratanteTramite @@@@@@"
				,"\n@@@@@@ ntramite=", ntramite
				));
		
		String paso = null;
		
		Map<String,String> result = new LinkedHashMap<String,String>();
		
		try
		{
			paso = "Recuperando cliente actual";
			
			String cdperson = consultasDAO.recuperarCdpersonClienteTramite(ntramite);
			
			paso = "Recuperando tipo de ramo";
			
			String esSaludDanios = consultasDAO.recuperarEsSaludDaniosTramite(ntramite);
			
			result.put("cdperson"      , cdperson);
			result.put("esSaludDanios" , esSaludDanios);
		}
		catch(Exception ex)
		{
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				 "\n@@@@@@ result=", result
				,"\n@@@@@@ pantallaContratanteTramite @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return result;
	}
	
	@Override
	public void movimientoClienteTramite(String ntramite, String cdperson, String accion) throws Exception
	{
		emisionDAO.movimientoClienteTramite(ntramite,cdperson,accion);
	}
	
	@Override
	public String recuperarNmsoliciTramite (String ntramite) throws Exception {
		logger.debug(Utils.log(
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"\n@@@@@@ recuperarNmsoliciTramite @@@@@@",
				"\n@@@@@@ ntramite = ", ntramite
		));
		
		String nmsolici = null, paso = null;
		
		try {
			paso = "Recuperando tr\u00e1mite";
			logger.debug(paso);
			Map<String, String> tramite = siniestrosDAO.obtenerTramiteCompleto(ntramite);
			logger.debug("Tramite recuperado = {}", tramite);
			nmsolici = tramite.get("NMSOLICI");
		} catch (Exception ex) {
			Utils.generaExcepcion(ex, paso);
		}
		
		logger.debug(Utils.log(
				"\n@@@@@@ nmsolici = ", nmsolici,
				"\n@@@@@@ recuperarNmsoliciTramite @@@@@@",
				"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
		));
		return nmsolici;
	}
}