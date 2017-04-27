package mx.com.gseguros.portal.general.util;

public enum TipoModelado {
	FLUJOS_PROCESOS(1),
	SUCURSALES(2);
	private final int cdtipmod;
	private TipoModelado(int cdtipmod) {
		this.cdtipmod = cdtipmod;
	}
	public int getCdtipmod() {
		return cdtipmod;
	}
}