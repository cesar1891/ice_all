package mx.com.gseguros.portal.general.service.impl;

import java.util.List;
import java.util.Map;

import mx.com.gseguros.exception.DaoException;
import mx.com.gseguros.portal.general.dao.MenuDAO;
import mx.com.gseguros.portal.general.service.MenuManager;

import org.apache.log4j.Logger;

public class MenuManagerImpl implements MenuManager {
	
	private Logger logger = Logger.getLogger(MenuManagerImpl.class);
	
	
	private MenuDAO menuDAO;

	@Override
	public List<Map<String, String>> obtieneOpcionesLiga(Map<String, String> params) throws Exception{
		try {
			return menuDAO.obtieneOpcionesLiga(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> obtieneMenusPorRol(Map<String, String> params) throws Exception{
		try {
			return menuDAO.obtieneMenusPorRol(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> obtieneOpcionesMenu(Map<String, String> params) throws Exception{
		try {
			return menuDAO.obtieneOpcionesMenu(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public List<Map<String, String>> obtieneOpcionesSubMenu(Map<String, String> params) throws Exception{
		try {
			return menuDAO.obtieneOpcionesSubMenu(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String guardaOpcionLiga(Map<String, String> params) throws Exception{
		try {
			return menuDAO.guardaOpcionLiga(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String guardaMenu(Map<String, String> params) throws Exception{
		try {
			return menuDAO.guardaMenu(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String guardaOpcionMenu(Map<String, String> params) throws Exception{
		try {
			return menuDAO.guardaOpcionMenu(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	@Override
	public String eliminaOpcionLiga(Map<String, String> params) throws Exception{
		try {
			return menuDAO.eliminaOpcionLiga(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String eliminaMenu(Map<String, String> params) throws Exception{
		try {
			return menuDAO.eliminaMenu(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}

	@Override
	public String eliminaOpcionMenu(Map<String, String> params) throws Exception{
		try {
			return menuDAO.eliminaOpcionMenu(params);
		} catch (DaoException daoExc) {
			throw new Exception(daoExc.getMessage(), daoExc);
		}
	}
	
	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}
		
}