package mx.com.gseguros.mesacontrol.model;

public enum AgrupadorMC
{
	PRINCIPAL("INCLUYE 1-EMISION, 15-ENDOSO, Y 17-EMISION EN ESPERA");

	private String descrip;

	private AgrupadorMC(String descrip) {
		this.descrip = descrip;
	}

	public String getDescrip() {
		return descrip;
	}
}