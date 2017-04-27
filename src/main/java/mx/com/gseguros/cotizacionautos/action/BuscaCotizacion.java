package mx.com.gseguros.cotizacionautos.action;

import mx.com.gseguros.cotizacionautos.delegate.AdminCotizaciones;

public class BuscaCotizacion {
	private String nmpoliza;
	private String tarea;
	private String regreso;
	
	 public String execute() {
		 System.out.println("Ya ingrese a mi action BuscaCotizacion:::::");
		 AdminCotizaciones adm = new AdminCotizaciones();
		 if(getTarea().equals("buscaCotizacion")){
			 setRegreso(adm.exitePoliza(getNmpoliza()));
		 }else if (getTarea().equals("pintaCompara")){
			 setRegreso(adm.pintaComparativo(getNmpoliza()));
		 }
		 return "SUCCESS";
	 }

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	public String getRegreso() {
		return regreso;
	}

	public void setRegreso(String regreso) {
		this.regreso = regreso;
	}

	public String getNmpoliza() {
		return nmpoliza;
	}

	public void setNmpoliza(String nmpoliza) {
		this.nmpoliza = nmpoliza;
	}

}
