package mx.com.gseguros.portal.endosos.service;

import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaImapVO;
import mx.com.gseguros.portal.cotizacion.model.ManagerRespuestaSlistVO;

public interface EndososGrupoManager
{
	public ManagerRespuestaImapVO  endososGrupo();
    public ManagerRespuestaSlistVO buscarHistoricoPolizas(String nmpoliex,String rfc,String cdperson,String nombre,String cdsisrol);
    public ManagerRespuestaSlistVO cargarFamiliasPoliza(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem);
    public ManagerRespuestaSlistVO cargarIntegrantesFamilia(String cdunieco,String cdramo,String estado,String nmpoliza,String nmsuplem,String nmsitaux);
}