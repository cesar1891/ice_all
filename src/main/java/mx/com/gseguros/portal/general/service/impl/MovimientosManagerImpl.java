package mx.com.gseguros.portal.general.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.portal.model.UserVO;
import mx.com.gseguros.exception.ApplicationException;
import mx.com.gseguros.portal.cotizacion.dao.CotizacionDAO;
import mx.com.gseguros.portal.endosos.dao.EndososDAO;
import mx.com.gseguros.portal.general.model.Movimiento;
import mx.com.gseguros.portal.general.service.MovimientosManager;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientosManagerImpl implements MovimientosManager
{
	private final static Logger logger = LoggerFactory.getLogger(MovimientosManagerImpl.class);
	
	@Autowired
	private EndososDAO endososDAO;
	
	@Autowired
	private CotizacionDAO cotizacionDAO;
	
	@Override
	public void ejecutar(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ ejecutar @@@@@@"
				,"\n@@@@@@ cdusuari="   , usuario.getUser()
				,"\n@@@@@@ movimiento=" , movimiento
				,"\n@@@@@@ params="     , params
				,"\n@@@@@@ list="       , list
				));
		String paso = "Ejecutando movimiento";
		try
		{
			if(movimiento.equals(Movimiento.DESHACER_PASO_ASEGURADO))
			{
				paso = "Revirtiendo movimiento";
				logger.debug("Paso: {}",paso);
				String cdunieco        = params.get("CDUNIECO");
				String cdramo          = params.get("CDRAMO");
				String estado          = params.get("ESTADO");
				String nmpoliza        = params.get("NMPOLIZA");
				String nmsituac        = params.get("NMSITUAC");
				String cdtipsit        = params.get("CDTIPSIT");
				String nmsuplem_endoso = params.get("nmsuplem_endoso");
				String mov             = params.get("MOV");
				String cdtipsup        = params.get("cdtipsup");
				
				Utils.validate(
						cdunieco         , "No se recibi\u00F3 la sucursal"
						,cdramo          , "No se recibi\u00F3 el producto"
						,estado          , "No se recibi\u00F3 el estado de p\u00F3liza"
						,nmpoliza        , "No se recibi\u00F3 el n\u00FAmero de p\u00F3liza"
						,nmsituac        , "No se recibi\u00F3 el n\u00FAmero de situaci\u00F3n"
						,cdtipsit        , "No se recibi\u00F3 el tipo de situaci\u00F3n"
						,nmsuplem_endoso , "No se recibi\u00F3 el suplemento"
						,mov             , "No se recibi\u00F3 el movimiento"
						,cdtipsup        , "No se recibi\u00F3 el tipo de endoso"
						);
				
				if("-".equals(mov))
				{
					paso = "Revirtiendo relaci\u00F3n p\u00F3liza-situaci\u00F3n";
					logger.debug("Paso: {}",paso);
					cotizacionDAO.movimientoMpolisit(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,nmsuplem_endoso
							,"M" //status
							,cdtipsit
							,null //swreduci
							,null //cdagrupa
							,null //cdestado
							,null //fefecsit
							,null //fecharef
							,null //cdgrupo
							,null //nmsituaext
							,null //nmsitaux
							,null //nmsbsitext
							,null //cdplan
							,null //cdasegur
							,"X"
							);
					
					paso = "Revirtiendo valores adicionales de situaci\u00F3n";
					logger.debug("Paso: {}",paso);
					cotizacionDAO.movimientoTvalosit(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,nmsuplem_endoso
							,"M" //status
							,cdtipsit
							,new HashMap<String,String>()
							,"X"
							);
					
					paso = "Revirtiendo relaci\u00F3n p\u00F3liza-persona";
					logger.debug("Paso: {}",paso);
					cotizacionDAO.movimientoMpoliper(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,nmsituac
							,"2"  //cdrol
							,null //cdperson
							,nmsuplem_endoso
							,"M"  //status
							,null //nmorddom
							,null //swreclam
							,"X"  //accion
							,null //swexiper
							);
					
					paso = "Revirtiendo indicador temporal";
					logger.debug("Paso: {}",paso);
					endososDAO.movimientoTworksupEnd(
							cdunieco
							,cdramo
							,estado
							,nmpoliza
							,cdtipsup
							,nmsuplem_endoso
							,nmsituac
							,"B"
							);
				}
			}
			else if(movimiento.equals(Movimiento.SACAENDOSO))
			{
				paso = "Revirtiendo cambios de endoso";
				logger.debug("Paso: {}",paso);
				String cdunieco = params.get("cdunieco");
				String cdramo   = params.get("cdramo");
				String estado   = params.get("estado");
				String nmpoliza = params.get("nmpoliza");
				String nsuplogi = params.get("nsuplogi");
				String nmsuplem = params.get("nmsuplem");
				
				Utils.validate(
						cdunieco  , "No se recibi\u00F3 la sucursal"
						,cdramo   , "No se recibi\u00F3 el producto"
						,estado   , "No se recibi\u00F3 el estado de p\u00F3liza"
						,nmpoliza , "No se recibi\u00F3 el n\u00FAmero de p\u00F3liza"
						,nsuplogi , "No se recibi\u00F3 el consecutivo"
						,nmsuplem , "No se recibi\u00F3 el suplemento"
						);
				
				endososDAO.sacaEndoso(cdunieco,cdramo,estado,nmpoliza,nsuplogi,nmsuplem);
			}
		}
		catch(Exception ex)
		{
			logger.error("Error ejecutando movimiento",ex);
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ ejecutar @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@"
				));
	}
	
	@Override
	public Map<String,String> ejecutarRecuperandoMapa(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ ejecutarRecuperandoMapa @@@@@@"
				,"\n@@@@@@ cdusuari="   , usuario.getUser()
				,"\n@@@@@@ movimiento=" , movimiento
				,"\n@@@@@@ params="     , params
				,"\n@@@@@@ list="       , list
				));
		Map<String,String> mapa = new HashMap<String,String>();
		String             paso = "Ejecutando movimiento";
		try
		{
			if(movimiento.equals(Movimiento.PASO_QUITAR_ASEGURADO))
			{
				Utils.validate(params.get("FEPROREN") , "No hay fecha de inicio");
				Utils.validate(params.get("cdtipsup") , "No hay clave de endoso");
				
				String cdusuari = usuario.getUser();
				String cdelemen = usuario.getEmpresa().getElementoId();
				Date   fechaEnd = Utils.parse(params.get("FEPROREN"));
				String cdunieco = params.get("CDUNIECO");
				String cdramo   = params.get("CDRAMO");
				String estado   = params.get("ESTADO");
				String nmpoliza = params.get("NMPOLIZA");
				String cdtipsup = params.get("cdtipsup");
				String nmsituac = params.get("NMSITUAC");
				String cdperson = params.get("CDPERSON");
				String status   = params.get("STATUS");
				String nmorddom = params.get("NMORDDOM");
				String swreclam = params.get("SWRECLAM");
				String swexiper = params.get("SWEXIPER");
				String cdtipsit = params.get("CDTIPSIT");
				String swreduci = params.get("SWREDUCI");
				String cdagrupa = params.get("CDAGRUPA");
				String cdestado = params.get("CDESTADO");
				Date   fefecsit = null;
				if(StringUtils.isNotBlank(params.get("FEFECSIT")))
				{
					fefecsit = Utils.parse(params.get("FEFECSIT"));
				}
				Date fecharef = null;
				if(StringUtils.isNotBlank(params.get("FECHAREF")))
				{
					fecharef = Utils.parse(params.get("FECHAREF"));
				}
				String cdgrupo    = params.get("CDGRUPO");
				String nmsituaext = params.get("NMSITUAEXT");
				String nmsitaux   = params.get("NMSITAUX");
				String nmsbsitext = params.get("NMSBSITEXT");
				String cdplan     = params.get("CDPLAN");
				String cdasegur   = params.get("CDASEGUR");
				
				if(StringUtils.isNotBlank(params.get("sleep")))
				{
					paso  = "Procesando tiempo de espera";
					Thread.sleep(Long.parseLong(params.get("sleep")));
				}
				
				paso = "Guardando endoso";
				logger.debug("Paso: {}",paso);
				Map<String,String> endoso = endososDAO.iniciarEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,fechaEnd
						,cdelemen
						,cdusuari
						,"END"
						,cdtipsup
						);
				String nmsuplemEndoso = endoso.get("pv_nmsuplem_o");
				String nsuplogiEndoso = endoso.get("pv_nsuplogi_o");
				logger.debug("nmsuplemEndoso: {}, nsuplogiEndoso: {}",nmsuplemEndoso,nsuplogiEndoso);
				mapa.put("nmsuplem_endoso" , nmsuplemEndoso);
				mapa.put("nsuplogi"        , nsuplogiEndoso);
				
				paso = "Quitando relaci\u00F3n p\u00F3liza-situaci\u00F3n";
				logger.debug("Paso: {}",paso);
				cotizacionDAO.movimientoMpolisit(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplemEndoso
						,status
						,cdtipsit
						,swreduci
						,cdagrupa
						,cdestado
						,fefecsit
						,fecharef
						,cdgrupo
						,nmsituaext
						,nmsitaux
						,nmsbsitext
						,cdplan
						,cdasegur
						,"B"
						);
				
				paso = "Quitando valores adicionales de situaci\u00F3n";
				logger.debug("Paso: {}",paso);
				cotizacionDAO.movimientoTvalosit(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,nmsuplemEndoso
						,status
						,cdtipsit
						,new HashMap<String,String>()
						,"B"
						);
				
				paso = "Quitando relaci\u00F3n p\u00F3liza-persona";
				logger.debug("Paso: {}",paso);
				cotizacionDAO.movimientoMpoliper(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,nmsituac
						,"2" //cdrol
						,cdperson
						,nmsuplemEndoso
						,status
						,nmorddom
						,swreclam
						,"B" //accion
						,swexiper
						);
				
				paso = "Guardando indicador temporal";
				logger.debug("Paso: {}",paso);
				endososDAO.movimientoTworksupEnd(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,cdtipsup
						,nmsuplemEndoso
						,nmsituac
						,"I"
						);
			}
		}
		catch(Exception ex)
		{
			logger.error("Error ejecutando movimiento",ex);
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ mapa=",mapa
				,"\n@@@@@@ ejecutarRecuperandoMapa @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return mapa;
	}
	
	@Override
	public Map<String,String> ejecutarRecuperandoMapaAltaFamilia(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception
	{
		logger.debug(Utils.log(
				 "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				,"\n@@@@@@ ejecutarRecuperandoMapaAltaFamilia @@@@@@"
				,"\n@@@@@@ cdusuari="   , usuario.getUser()
				,"\n@@@@@@ movimiento=" , movimiento
				,"\n@@@@@@ params="     , params
				,"\n@@@@@@ list="       , list
				));
		Map<String,String> mapa = new HashMap<String,String>();
		String             paso = "Ejecutando movimiento";
		try
		{
			if(movimiento.equals(Movimiento.AGREGAR_FAMILIA))
			{
				Utils.validate(params.get("FEEFECTO") , "No hay fecha de inicio");
				Utils.validate(params.get("cdtipsup") , "No hay clave de endoso");
				
				String cdusuari = usuario.getUser();
				String cdelemen = usuario.getEmpresa().getElementoId();
				Date   fechaEnd = Utils.parse(params.get("FEEFECTO"));
				String cdunieco = params.get("CDUNIECO");
				String cdramo   = params.get("CDRAMO");
				String estado   = params.get("ESTADO");
				String nmpoliza = params.get("NMPOLIZA");
				String cdtipsup = params.get("cdtipsup");
				
				paso = "Guardando endoso";
				logger.debug("Paso: {}",paso);
				Map<String,String> endoso = endososDAO.iniciarEndoso(
						cdunieco
						,cdramo
						,estado
						,nmpoliza
						,fechaEnd
						,cdelemen
						,cdusuari
						,"END"
						,cdtipsup
						);
				
				String nmsuplemEndoso = endoso.get("pv_nmsuplem_o");
				String nsuplogiEndoso = endoso.get("pv_nsuplogi_o");
				logger.debug("nmsuplemEndoso: {}, nsuplogiEndoso: {}",nmsuplemEndoso,nsuplogiEndoso);
				mapa.put("nmsuplem_endoso" , nmsuplemEndoso);
				mapa.put("nsuplogi"        , nsuplogiEndoso);
				
				/*paso = "Quitando relaci\u00F3n p\u00F3liza-situaci\u00F3n";
				logger.debug("Paso: {}",paso);
				cotizacionDAO.movimientoMpolisit();
				
				paso = "Quitando valores adicionales de situaci\u00F3n";
				logger.debug("Paso: {}",paso);
				cotizacionDAO.movimientoTvalosit();
				
				paso = "Quitando relaci\u00F3n p\u00F3liza-persona";
				logger.debug("Paso: {}",paso);
				cotizacionDAO.movimientoMpoliper();
				
				paso = "Guardando indicador temporal";
				logger.debug("Paso: {}",paso);
				endososDAO.movimientoTworksupEnd();*/
			}
		}
		catch(Exception ex)
		{
			logger.error("Error ejecutando movimiento",ex);
			Utils.generaExcepcion(ex, paso);
		}
		logger.debug(Utils.log(
				 "\n@@@@@@ mapa=",mapa
				,"\n@@@@@@ ejecutarRecuperandoMapaAltaFamilia @@@@@@"
				,"\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
				));
		return mapa;
	}
	
	@Override
	public List<Map<String,String>> ejecutarRecuperandoLista(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception
	{
		throw new ApplicationException("No se ha implementado");
	}
	
	@Override
	public Map<String,Object> ejecutarRecuperandoMapaLista(
			UserVO usuario
			,Movimiento movimiento
			,Map<String,String> params
			,List<Map<String,String>> list
			)throws Exception
	{
		throw new ApplicationException("No se ha implementado");
	}
}