package mx.com.gseguros.portal.general.util;

import org.apache.regexp.recompile;

public enum TipoFormaPago {
	
	MENSUAL(1),
	ANUAL(12),
	TRIMESTRAL(61),
	SEMESTRAL(63),
	CONTADO(64),
	DXNQUINCENAL(6),
	DXNCATORCENAL(7),
	DXNMENSUAL(8),
	DXN16DIAS(9),
    DXNSEMANAL(10),
    DXNANUAL(2),
    DXNDOCENAL(11);
	
	private int codigo;
	
	private TipoFormaPago(int codigo){
		this.codigo=codigo;
	
		
	}
	
	public int getCodigo(){
		return codigo;
	}
	
}
