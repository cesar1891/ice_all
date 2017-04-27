package mx.com.gseguros.portal.consultas.dao;

import java.util.List;

import mx.com.gseguros.portal.consultas.model.ConsultaDatosSiniestrosVO;

import org.apache.log4j.Logger;


public interface ConsultasSiniestroDAO {
	static Logger logger = Logger.getLogger(ConsultasSiniestroDAO.class);


	public List<ConsultaDatosSiniestrosVO> obtieneConsultaAseguradosPagoReembolso(String cdperson) throws Exception;


	public List<ConsultaDatosSiniestrosVO> obtieneConsultaFacturasPagoDirecto(String cdperson, String cdproveedor, String cdfactura) throws Exception;



}