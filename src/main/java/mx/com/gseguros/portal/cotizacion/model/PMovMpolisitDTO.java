package mx.com.gseguros.portal.cotizacion.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ESPEJO DE PKG_SATELITES.P_MOV_MPOLISIT		
 * @author jair
 *
 */
public class PMovMpolisitDTO
{
	
	private String cdunieco
	               ,cdramo
	               ,estado
	               ,nmpoliza
	               ,nmsituac
	               ,nmsuplem
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
	               ,accion;
	
	public PMovMpolisitDTO(
			String cdunieco
            ,String cdramo
            ,String estado
            ,String nmpoliza
            ,String nmsituac
            ,String nmsuplem
            ,String status
            ,String cdtipsit
            ,String swreduci
            ,String cdagrupa
            ,String cdestado
            ,String fefecsit
            ,String fecharef
            ,String cdgrupo
            ,String nmsituaext
            ,String nmsitaux
            ,String nmsbsitext
            ,String cdplan
            ,String cdasegur
            ,String accion)
	{
		this.cdunieco   = cdunieco;
        this.cdramo     = cdramo;
        this.estado     = estado;
        this.nmpoliza   = nmpoliza;
        this.nmsituac   = nmsituac;
        this.nmsuplem   = nmsuplem;
        this.status     = status;
        this.cdtipsit   = cdtipsit;
        this.swreduci   = swreduci;
        this.cdagrupa   = cdagrupa;
        this.cdestado   = cdestado;
        this.fefecsit   = fefecsit;
        this.fecharef   = fecharef;
        this.cdgrupo    = cdgrupo;
        this.nmsituaext = nmsituaext;
        this.nmsitaux   = nmsitaux;
        this.nmsbsitext = nmsbsitext;
        this.cdplan     = cdplan;
        this.cdasegur   = cdasegur;
        this.accion     = accion;
	}
	
	public String[] indexar()
	{
		return new String[]{
				this.cdunieco
		        ,this.cdramo
		        ,this.estado
		        ,this.nmpoliza
		        ,this.nmsituac
		        ,this.nmsuplem
		        ,this.status
		        ,this.cdtipsit
		        ,this.swreduci
		        ,this.cdagrupa
		        ,this.cdestado
		        ,this.fefecsit
		        ,this.fecharef
		        ,this.cdgrupo
		        ,this.nmsituaext
		        ,this.nmsitaux
		        ,this.nmsbsitext
		        ,this.cdplan
		        ,this.cdasegur
		        ,this.accion
		        };
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    public String getNmsituac() {
        return nmsituac;
    }

    public void setNmsituac(String nmsituac) {
        this.nmsituac = nmsituac;
    }
	
	
}