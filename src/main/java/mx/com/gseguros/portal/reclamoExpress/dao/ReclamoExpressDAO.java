package mx.com.gseguros.portal.reclamoExpress.dao;

import java.util.List;

import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressDetalleVO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressVO;


public interface ReclamoExpressDAO {
	public List<BaseVO> obtieneReclamos() throws Exception;
	
	public List<BaseVO> obtieneSecuenciales(int reclamo) throws Exception;
	
	public List<ReclamoExpressVO> obtieneReclamoExpress(int reclamo, int secuencial) throws Exception;
	
	public boolean guardaReclamoExpress(ReclamoExpressVO reclamoExpress) throws Exception;
	
	public boolean guardaDetalleExpress(ReclamoExpressDetalleVO reclamoExpressDetalle) throws Exception;
	
	public boolean borraDetalleExpress(ReclamoExpressDetalleVO reclamoExpressDetalle) throws Exception;
}
