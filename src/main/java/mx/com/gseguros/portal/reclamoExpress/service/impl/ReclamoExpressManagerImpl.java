package mx.com.gseguros.portal.reclamoExpress.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.com.gseguros.portal.consultas.dao.IConsultasAseguradoDAO;
import mx.com.gseguros.portal.consultas.model.ConsultaDatosGeneralesPolizaVO;
import mx.com.gseguros.portal.consultas.model.PolizaAseguradoVO;
import mx.com.gseguros.portal.general.model.BaseVO;
import mx.com.gseguros.portal.reclamoExpress.dao.ReclamoExpressDAO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressDetalleVO;
import mx.com.gseguros.portal.reclamoExpress.model.ReclamoExpressVO;
import mx.com.gseguros.portal.reclamoExpress.service.ReclamoExpressManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("reclamoExpressManagerImpl")
public class ReclamoExpressManagerImpl implements ReclamoExpressManager {
	
	private org.apache.log4j.Logger logger =org.apache.log4j.Logger.getLogger(ReclamoExpressManagerImpl.class);
	
	@Autowired
	@Qualifier("reclamoExpressDAOSIGSImpl")
	private ReclamoExpressDAO reclamoExpressDAOSIGS;
	
	@Autowired
	@Qualifier("consultasAseguradoDAOICEImpl")
	private IConsultasAseguradoDAO consultasAseguradoDAOICE;

	@Override
	public List<BaseVO> obtieneReclamos() throws Exception {
		List<BaseVO> reclamos = new ArrayList<BaseVO>();		
		reclamos = reclamoExpressDAOSIGS.obtieneReclamos();
		return reclamos;
	}
	
	@Override
	public List<BaseVO> obtieneSecuenciales(int reclamo) throws Exception {
		List<BaseVO> secuenciales = new ArrayList<BaseVO>();		
		secuenciales = reclamoExpressDAOSIGS.obtieneSecuenciales(reclamo);
		return secuenciales;
	}

	@Override
	public List<ReclamoExpressVO> obtieneReclamoExpress(int reclamo,
			int secuencial) throws Exception {
		List<ReclamoExpressVO> reclamosExpress = new ArrayList<ReclamoExpressVO>();
		reclamosExpress = reclamoExpressDAOSIGS.obtieneReclamoExpress(reclamo, secuencial);
					
		List<ConsultaDatosGeneralesPolizaVO> datosPolizas = new ArrayList<ConsultaDatosGeneralesPolizaVO>();
		
		//Recorremos el reclamoExpress
		Iterator it1 = reclamosExpress.iterator();
		while(it1.hasNext()){
			//Creamos una variable de referencia hacia el primer objeto
			ReclamoExpressVO reclamoAux;
			reclamoAux = (ReclamoExpressVO) it1.next();
			
			//Creamos un objeto de tipo PolizaAseguradoVO y lo configuramos con los valores
			PolizaAseguradoVO polizaAsegurado = new PolizaAseguradoVO();
			polizaAsegurado.setCdunieco(reclamoAux.getSucursal());
			polizaAsegurado.setCdramo("2");
			polizaAsegurado.setEstado("M");
			polizaAsegurado.setNmpoliza(reclamoAux.getPoliza());
			
			//Invocamos el DAO para obtener una lista
			datosPolizas = consultasAseguradoDAOICE.obtieneDatosPoliza(polizaAsegurado);
			
			ConsultaDatosGeneralesPolizaVO datoPoliza;
			Iterator it2 = datosPolizas.iterator();
			while(it2.hasNext()){
				datoPoliza = (ConsultaDatosGeneralesPolizaVO) it2.next();
				//Cambiamos el valor del Id del Cliente en la variable de referencia
				reclamoAux.setIdCliente(datoPoliza.getCdperson());
			}
			
			//Solo lo ejecutamos una vez
			break;
		}
		
		return reclamosExpress;
	}

	@Override
	public boolean guardaReclamoExpress(ReclamoExpressVO reclamoExpress)
			throws Exception {
		boolean exito = false;
		exito = reclamoExpressDAOSIGS.guardaReclamoExpress(reclamoExpress);
		return exito;
	}

	@Override
	public boolean guardaDetalleExpress(
			ReclamoExpressDetalleVO reclamoExpressDetalle) throws Exception {
		boolean exito = false;
		exito = reclamoExpressDAOSIGS.guardaDetalleExpress(reclamoExpressDetalle);
		return exito;
	}

	@Override
	public boolean borraDetalleExpress(
			ReclamoExpressDetalleVO reclamoExpressDetalle) throws Exception {
		boolean exito = false;
		exito = reclamoExpressDAOSIGS.borraDetalleExpress(reclamoExpressDetalle);
		return exito;
	}

	

	
	
	

}
