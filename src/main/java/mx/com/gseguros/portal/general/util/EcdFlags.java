package mx.com.gseguros.portal.general.util;

public enum EcdFlags {
	//PERFIL_0("flag_white.png"),
	PERFIL_0("flag_blue.png"),
	PERFIL_1("flag_green.png"),
	PERFIL_2("flag_yellow.png"),
	PERFIL_3("flag_red.png")
	;
	
	private String perfil;
	
	private EcdFlags(String url){
		this.perfil=url;
	}
	
	public String getPerfil(){
		return perfil;
	}
}
