package mx.com.gseguros.portal.reexpedicionDocumentos.service.impl;

import mx.com.gseguros.portal.reexpedicionDocumentos.dao.ReexpedicionDocumentosDAO;
import mx.com.gseguros.portal.reexpedicionDocumentos.service.ReexpedicionDocumentosManager;

public class ReexpedicionDocumentosManagerImpl implements ReexpedicionDocumentosManager
{
	private ReexpedicionDocumentosDAO reexpedicionDocumentosDAO;

	public void setReexpedicionDocumentosDAO(
			ReexpedicionDocumentosDAO reexpedicionDocumentosDAO) {
		this.reexpedicionDocumentosDAO = reexpedicionDocumentosDAO;
	}
}