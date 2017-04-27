package mx.com.gseguros.portal.general.util;

public enum Ramo {
	
	AUTOS_FRONTERIZOS("16"),
	AUTOS_RESIDENTES ("5"),
	MULTISALUD       ("4"),
	RECUPERA         ("1"),
	SALUD_VITAL      ("2"),
	SERVICIO_PUBLICO ("6"),
	GASTOS_MEDICOS_MAYORES("7"),
	MESA_CONTROL     ("999"),
	GASTOS_MEDICOS_MAYORES_PRUEBA ("11");

	private String cdramo;

	private Ramo(String cdramo) {
		this.cdramo = cdramo;
	}

	public String getCdramo() {
		return cdramo;
	}
	
}