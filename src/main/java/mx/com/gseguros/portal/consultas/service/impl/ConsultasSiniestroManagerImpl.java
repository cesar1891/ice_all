package mx.com.gseguros.portal.consultas.service.impl;

import java.util.List;

import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.consultas.dao.ConsultasSiniestroDAO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosSiniestrosVO;
import mx.com.gseguros.portal.consultas.service.ConsultasSiniestroManager;

public class ConsultasSiniestroManagerImpl implements ConsultasSiniestroManager {
	
	private ConsultasSiniestroDAO consultasSiniestroDAO;
		
	@Override
	public List<ConsultaDatosSiniestrosVO> getConsultaAseguradosPagoReembolso(String cdperson) throws Exception {
		try {
			return consultasSiniestroDAO.obtieneConsultaAseguradosPagoReembolso(cdperson);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	public void setConsultasSiniestroDAO(ConsultasSiniestroDAO consultasSiniestroDAO) {
		this.consultasSiniestroDAO = consultasSiniestroDAO;
	}

	@Override
	public List<ConsultaDatosSiniestrosVO> getConsultaFacturasPagoDirecto(String cdperson, String cdproveedor, String cdfactura) throws Exception {
		try {
			return consultasSiniestroDAO.obtieneConsultaFacturasPagoDirecto(cdperson,cdproveedor,cdfactura);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
}