package mx.com.gseguros.cotizacionautos.delegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.gseguros.cotizacionautos.base.dao.ComparaDao;
import mx.com.gseguros.cotizacionautos.model.AseguradoraVO;
import mx.com.gseguros.cotizacionautos.model.Contenedor;
import mx.com.gseguros.cotizacionautos.model.ContenedorC;
import mx.com.gseguros.cotizacionautos.model.PlanVO;
import mx.com.gseguros.cotizacionautos.model.TipoClickCob;
import mx.com.gseguros.cotizacionautos.model.TipoClickCompra;
import mx.com.gseguros.cotizacionautos.model.TipoConceptoCompra;
import mx.com.gseguros.cotizacionautos.model.TipoConceptoPagosSub;
import mx.com.gseguros.cotizacionautos.model.TipoConceptoPrimerPago;
import mx.com.gseguros.cotizacionautos.model.TipoConceptoTotal;
import mx.com.gseguros.cotizacionautos.model.TipoImagen;
import mx.com.gseguros.cotizacionautos.model.TipoImporte;
import mx.com.gseguros.cotizacionautos.model.TipoMoneda;


public class AdminCotizaciones {

	public String exitePoliza (String nmpoliza){
		String rgs = "";
		System.out.println(nmpoliza);
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("query", "ExisteCotizacion");
		data.put("nmpoliza", nmpoliza);
		try {
			ComparaDao dao = new ComparaDao();
			rgs = dao.getString(data);
			System.out.println(rgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	public String pintaComparativo (String nmpoliza){
		String rgs = "";
		ArrayList ltsPlanes = new ArrayList();
		
		ltsPlanes.add(new PlanVO("907","../../images/cotizacionautos/907.png", "aseguradora"));
		ltsPlanes.add(new PlanVO("906","../../images/cotizacionautos/906.png", "aseguradora"));
		ltsPlanes.add(new PlanVO("908","../../images/cotizacionautos/qualitas.png", "aseguradora"));
		
		ltsPlanes.add(new PlanVO("AP35","../../images/cotizacionautos/aonAP35.png", ","));
		ltsPlanes.add(new PlanVO("AP51","../../images/cotizacionautos/aonAP51.png", ","));
		
		ltsPlanes.add(new PlanVO("AM35","../../images/cotizacionautos/aonAM35.png", ","));
		ltsPlanes.add(new PlanVO("AM51","../../images/cotizacionautos/aonAM51.png", ","));
		
		ltsPlanes.add(new PlanVO("LM35","../../images/cotizacionautos/aonLM35.png", ","));
		//ltsPlanes.add(new PlanVO("LM51","../../images/cotizacionautos/aonLM51.png", ","));
		
		ltsPlanes.add(new PlanVO("RC51","../../images/cotizacionautos/aonRC51.png", ","));
		
		
		ltsPlanes.add(this.getAseguradora("mapfre",","));
		ltsPlanes.add(this.getAseguradora("aba",","));
		ltsPlanes.add(this.getAseguradora("qualitas",","));
		
		//JSONArray jsonArrayLUC = JSONArray.fromObject( ltsPlanes );
		//System.out.println("ltsPlanes: " + jsonArrayLUC);
		
		rgs = this.CreaHTML(ltsPlanes, "");
		
		/*rgs = "[{ltsPlanes:" +jsonArrayLUC.toString()+"}";
		
		ArrayList ltsAseguradoras = new ArrayList();
		ltsAseguradoras.add(this.getAseguradora("aba",","));
		ltsAseguradoras.add(this.getAseguradora("mapfre",","));  
		
		JSONArray jsonArrayAseguradoras = JSONArray.fromObject( ltsAseguradoras );
		System.out.println("Aseguradoras: " + jsonArrayAseguradoras);
		rgs = rgs + ",{Aseguradoras:" + jsonArrayAseguradoras.toString() +"}]";
		
		//rgs = "{Aseguradoras:" +jsonArrayAseguradoras.toString()+"}";*/
		
		return rgs;
	}
	
	
private AseguradoraVO getAseguradora (String aseg, String coma){
		
		AseguradoraVO cero = new AseguradoraVO();
		cero.setName(aseg);
		String h = "";
		if (aseg.endsWith("aba")){
			h = "50";
		}else if (aseg.endsWith("mapfre")){
			h = "70";
		}else if (aseg.endsWith("qualitas")){
			h = "55";
		}
		
		cero.setColImagen(new TipoImagen("../../images/cotizacionautos/"+aseg+".png",h)); //Objeto donde va la imagen de la aseguradora
		cero.setColConceptoTotal(new TipoConceptoTotal()); //Objeto donde va el concepto TOTAL

		List ltsTotales = new ArrayList();
		//Plan Amplia Plus Ded 3 y 5
		Contenedor AmpPlus35 = new Contenedor();
		AmpPlus35.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		AmpPlus35.setColImporte(new TipoImporte("10,890.00")); //Objeto que pinta la cantidad y/o costo.
		AmpPlus35.setColClickCob(new TipoClickCob(aseg + "AP35","AP35",aseg)); //Objeto que pinta el boton de coberturas
		ltsTotales.add(AmpPlus35);
		//Plan Amplia Plus Ded 5 y 10
		Contenedor AmpPlus510 = new Contenedor();
		AmpPlus510.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		AmpPlus510.setColImporte(new TipoImporte("10,590.00")); //Objeto que pinta la cantidad y/o costo.
		AmpPlus510.setColClickCob(new TipoClickCob(aseg +"AP51","AP51",aseg)); //Objeto que pinta el boton de coberturas
		ltsTotales.add(AmpPlus510);
		//Plan Amplia Ded 3 y 5
		Contenedor Amp35 = new Contenedor();
		Amp35.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		Amp35.setColImporte(new TipoImporte("9,590.00")); //Objeto que pinta la cantidad y/o costo.
		Amp35.setColClickCob(new TipoClickCob(aseg+"AM35","AM35",aseg)); //Objeto que pinta el boton de coberturas
		ltsTotales.add(Amp35);
		//Plan Amplia Ded 5 y 10
		Contenedor Amp510 = new Contenedor();
		Amp510.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		Amp510.setColImporte(new TipoImporte("8,590.00")); //Objeto que pinta la cantidad y/o costo.
		Amp510.setColClickCob(new TipoClickCob(aseg+"AM51","AM51",aseg)); //Objeto que pinta el boton de coberturas
		ltsTotales.add(Amp510);
		//Plan Limitada 5
		Contenedor Limitada5 = new Contenedor();
		Limitada5.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		Limitada5.setColImporte(new TipoImporte("7,590.00")); //Objeto que pinta la cantidad y/o costo.
		Limitada5.setColClickCob(new TipoClickCob(aseg+"LM35","LM35",aseg)); //Objeto que pinta el boton de coberturas
		ltsTotales.add(Limitada5);
		//Plan Limitada 10
		Contenedor Limitada10 = new Contenedor();
		Limitada10.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		Limitada10.setColImporte(new TipoImporte("6,590.00")); //Objeto que pinta la cantidad y/o costo.
		Limitada10.setColClickCob(new TipoClickCob(aseg+"LM51","LM51",aseg)); //Objeto que pinta el boton de coberturas
		//ltsTotales.add(Limitada10);
		//Plan Responsabilidad Civil
		Contenedor ResponCivil = new Contenedor();
		ResponCivil.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		ResponCivil.setColImporte(new TipoImporte("590.00")); //Objeto que pinta la cantidad y/o costo.
		ResponCivil.setColClickCob(new TipoClickCob(aseg+"RC51","RC51",aseg)); //Objeto que pinta el boton de coberturas
		ltsTotales.add(ResponCivil);
		
		cero.setPanelTotales(ltsTotales);
		
		cero.setColConceptoPrimerPago(new TipoConceptoPrimerPago()); //Objeto donde va el concepto PRIMER PAGO
		List ltsPrimerPago = new ArrayList();
		//Plan Amplia Plus Ded 3 y 5
		Contenedor PPAmpPlus35 = new Contenedor();
		PPAmpPlus35.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPAmpPlus35.setColImporte(new TipoImporte("1,000.00")); //Objeto que pinta la cantidad y/o costo.
		PPAmpPlus35.setColClickCob(new TipoClickCob(aseg+"PPAP35","PPAP35",aseg)); //Objeto que pinta el boton de coberturas
		ltsPrimerPago.add(PPAmpPlus35);
		//Plan Amplia Plus Ded 5 y 10
		Contenedor PPAmpPlus510 = new Contenedor();
		PPAmpPlus510.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPAmpPlus510.setColImporte(new TipoImporte("1,590.00")); //Objeto que pinta la cantidad y/o costo.
		PPAmpPlus510.setColClickCob(new TipoClickCob(aseg+"PPAP510","PPAP510",aseg)); //Objeto que pinta el boton de coberturas
		ltsPrimerPago.add(PPAmpPlus510);
		//Plan Amplia Ded 3 y 5
		Contenedor PPAmp35 = new Contenedor();
		PPAmp35.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPAmp35.setColImporte(new TipoImporte("590.00")); //Objeto que pinta la cantidad y/o costo.
		PPAmp35.setColClickCob(new TipoClickCob(aseg+"PPA35","PPA35",aseg)); //Objeto que pinta el boton de coberturas
		ltsPrimerPago.add(PPAmp35);
		//Plan Amplia Ded 5 y 10
		Contenedor PPAmp510 = new Contenedor();
		PPAmp510.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPAmp510.setColImporte(new TipoImporte("560.00")); //Objeto que pinta la cantidad y/o costo.
		PPAmp510.setColClickCob(new TipoClickCob(aseg+"PPA510","PPA510",aseg)); //Objeto que pinta el boton de coberturas
		ltsPrimerPago.add(PPAmp510);
		//Plan Limitada 5
		Contenedor PPLimitada5 = new Contenedor();
		PPLimitada5.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPLimitada5.setColImporte(new TipoImporte("510.00")); //Objeto que pinta la cantidad y/o costo.
		PPLimitada5.setColClickCob(new TipoClickCob(aseg+"PPL5","PPL5",aseg)); //Objeto que pinta el boton de coberturas
		ltsPrimerPago.add(PPLimitada5);
		//Plan Limitada 10
		Contenedor PPLimitada10 = new Contenedor();
		PPLimitada10.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPLimitada10.setColImporte(new TipoImporte("490.00")); //Objeto que pinta la cantidad y/o costo.
		PPLimitada10.setColClickCob(new TipoClickCob(aseg+"PPL10","PPL10",aseg)); //Objeto que pinta el boton de coberturas
		//ltsPrimerPago.add(PPLimitada10);
		//Plan Responsabilidad Civil
		Contenedor PPResponCivil = new Contenedor();
		PPResponCivil.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PPResponCivil.setColImporte(new TipoImporte("30.00")); //Objeto que pinta la cantidad y/o costo.
		PPResponCivil.setColClickCob(new TipoClickCob(aseg+"PPRC","PPRC",aseg)); //Objeto que pinta el boton de coberturas
		ltsPrimerPago.add(PPResponCivil);
		
		cero.setPanelPrimerPago(ltsPrimerPago);
		
		cero.setColConceptoPagosSubsecuentes(new TipoConceptoPagosSub("1")); //Objeto donde va el concepto PAGOS SUBSECUENTES
		List ltsPagosSub = new ArrayList();
		//Plan Amplia Plus Ded 3 y 5
		Contenedor PSAmpPlus35 = new Contenedor();
		PSAmpPlus35.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSAmpPlus35.setColImporte(new TipoImporte("500.00")); //Objeto que pinta la cantidad y/o costo.
		PSAmpPlus35.setColClickCob(new TipoClickCob(aseg+"PSAP35","PSAP35",aseg)); //Objeto que pinta el boton de coberturas
		ltsPagosSub.add(PSAmpPlus35);
		//Plan Amplia Plus Ded 5 y 10
		Contenedor PSAmpPlus510 = new Contenedor();
		PSAmpPlus510.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSAmpPlus510.setColImporte(new TipoImporte("720.00")); //Objeto que pinta la cantidad y/o costo.
		PSAmpPlus510.setColClickCob(new TipoClickCob(aseg+"PSAP510","PSAP510",aseg)); //Objeto que pinta el boton de coberturas
		ltsPagosSub.add(PSAmpPlus510);
		//Plan Amplia Ded 3 y 5
		Contenedor PSAmp35 = new Contenedor();
		PSAmp35.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSAmp35.setColImporte(new TipoImporte("295.00")); //Objeto que pinta la cantidad y/o costo.
		PSAmp35.setColClickCob(new TipoClickCob(aseg+"PSA35","PSA35",aseg)); //Objeto que pinta el boton de coberturas
		ltsPagosSub.add(PSAmp35);
		//Plan Amplia Ded 5 y 10
		Contenedor PSAmp510 = new Contenedor();
		PSAmp510.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSAmp510.setColImporte(new TipoImporte("270.00")); //Objeto que pinta la cantidad y/o costo.
		PSAmp510.setColClickCob(new TipoClickCob(aseg+"PSA510","PSA510",aseg)); //Objeto que pinta el boton de coberturas
		ltsPagosSub.add(PSAmp510);
		//Plan Limitada 5
		Contenedor PSLimitada5 = new Contenedor();
		PSLimitada5.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSLimitada5.setColImporte(new TipoImporte("260.00")); //Objeto que pinta la cantidad y/o costo.
		PSLimitada5.setColClickCob(new TipoClickCob(aseg+"PSL5","PSL5",aseg)); //Objeto que pinta el boton de coberturas
		ltsPagosSub.add(PSLimitada5);
		//Plan Limitada 10
		Contenedor PSLimitada10 = new Contenedor();
		PSLimitada10.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSLimitada10.setColImporte(new TipoImporte("235.00")); //Objeto que pinta la cantidad y/o costo.
		PSLimitada10.setColClickCob(new TipoClickCob(aseg+"PSL10","PSL10",aseg)); //Objeto que pinta el boton de coberturas
		//ltsPagosSub.add(PSLimitada10);
		//Plan Responsabilidad Civil
		Contenedor PSResponCivil = new Contenedor();
		PSResponCivil.setColMoneda(new TipoMoneda("$")); //Objeto que pinta el signo de pesos.
		PSResponCivil.setColImporte(new TipoImporte("3.00")); //Objeto que pinta la cantidad y/o costo.
		PSResponCivil.setColClickCob(new TipoClickCob(aseg+"PSRC","PSRC",aseg)); //Objeto que pinta el boton de coberturas
		ltsPagosSub.add(PSResponCivil);
		
		cero.setPanelPagosSubsecuentes(ltsPagosSub);
		
		cero.setColConceptoCompra(new TipoConceptoCompra()); //Objeto donde va el concepto COMPRAR
		List ltsComprar = new ArrayList();
		//Plan Amplia Plus Ded 3 y 5
		ContenedorC CompAmpPlus35 = new ContenedorC();
		CompAmpPlus35.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompAmpPlus35.setColClickCompra(new TipoClickCompra(aseg+"CompAP35",",",aseg,"CompAP35","1")); //Objeto que pinta el boton de coberturas
		ltsComprar.add(CompAmpPlus35);
		//Plan Amplia Plus Ded 5 y 10
		ContenedorC CompAmpPlus510 = new ContenedorC();
		CompAmpPlus510.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompAmpPlus510.setColClickCompra(new TipoClickCompra(aseg+"CompAP510",",",aseg,"CompAP510","1")); //Objeto que pinta el boton de coberturas
		ltsComprar.add(CompAmpPlus510);
		//Plan Amplia Ded 3 y 5
		ContenedorC CompAmp35 = new ContenedorC();
		CompAmp35.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompAmp35.setColClickCompra(new TipoClickCompra(aseg+"CompA35",",",aseg,"CompA35","1")); //Objeto que pinta el boton de coberturas
		ltsComprar.add(CompAmp35);
		//Plan Amplia Plus Ded 5 y 10
		ContenedorC CompAmp510 = new ContenedorC();
		CompAmp510.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompAmp510.setColClickCompra(new TipoClickCompra(aseg+"CompA510",",",aseg,"CompA510","1")); //Objeto que pinta el boton de coberturas
		ltsComprar.add(CompAmp510);
		//Plan Limitada 5
		ContenedorC CompLimitada5 = new ContenedorC();
		CompLimitada5.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompLimitada5.setColClickCompra(new TipoClickCompra(aseg+"CompLimitada5",",",aseg,"CompLimitada5","1")); //Objeto que pinta el boton de coberturas
		ltsComprar.add(CompLimitada5);
		//Plan Limitada 10
		ContenedorC CompLimitada10 = new ContenedorC();
		CompLimitada10.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompLimitada10.setColClickCompra(new TipoClickCompra(aseg+"CompLimitada10",",",aseg,"CompLimitada10","1")); //Objeto que pinta el boton de coberturas
		//ltsComprar.add(CompLimitada10);
		//Plan Responsabilidad Civil
		ContenedorC CompRC = new ContenedorC();
		CompRC.setColMoneda(new TipoMoneda("")); //Objeto que pinta el signo de pesos.
		CompRC.setColClickCompra(new TipoClickCompra(aseg+"CompRC","",aseg,"CompRC","1")); //Objeto que pinta el boton de coberturas
		ltsComprar.add(CompRC);
		cero.setPanelCompra(ltsComprar);
		cero.setComa(coma);
		return cero;
	}

public String CreaHTML (ArrayList<Serializable>lista, String navegador){
	StringBuffer html = new StringBuffer();
	html.append("<table cellspacing=\"0px\"><tr><td><img width = 99  height = 30 src=\"../../images/cotizacionautos/aseguradora.png\"/></td>");
	for(int i = 0; i<lista.size(); i++){
		Object o = (Object)lista.get(i);
		if(o.getClass().getName().indexOf("PlanVO") >= 0){
			PlanVO p = (PlanVO)lista.get(i);
			html.append("<td>").append(p.getUrl()).append("</td>");
		}
	}
	html.append("</tr></table>");
	for(int i = 0; i<lista.size(); i++){
		Object o = (Object)lista.get(i);
		if(o.getClass().getName().indexOf("AseguradoraVO") >= 0){
			AseguradoraVO p = (AseguradoraVO)lista.get(i);
			html.append("<table border=\"1\" rules=\"none\"><tr>")
			.append("<td>").append(p.getColImagen().getUrl()).append("</td>");
			//Se mete parche para cambiar el orden, si se quiere quitar habra que meter la 
			//informaci�n del los planes desde el origen.
			List<Contenedor> pnlsTemp = p.getPanelTotales();
			List<ContenedorC> pnlsC = p.getPanelCompra();
			for(int j = 0; j<pnlsC.size(); j++){
				ContenedorC cont = (ContenedorC)pnlsC.get(j);
				Contenedor contemp = (Contenedor)pnlsTemp.get(j);
				String tipoNA = contemp.getColImporte().getHtml().toUpperCase();
				String tta = "", ttc = "";
				int etx = tipoNA.indexOf("N/A");
				int etxA = tipoNA.indexOf("APLICA");
				if(etx >= 0 || etxA >= 0){
					tipoNA = "disabled";
				}else{
					tipoNA = "";
					tta = "<SPAN title=\"Selecciona los paquetes que deseas Comparar � selecciona uno si deseas Comprar.\">";
					ttc = "</SPAN>";
				}
				int wMon = Integer.parseInt(cont.getColMoneda().getWidth());
				if(navegador.equals("Firefox") || navegador.equals("IE7")){
					wMon = wMon-3;
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColMoneda().getHeight())
				.append("px; ").append(cont.getColMoneda().getStyle())
				.append("\">").append(cont.getColMoneda().getHtml())
				.append("</td>");
				html.append("<td style=\"").append(cont.getColClickCompra().getStyle())
				.append("\">").append(tta).append("<input ").append(tipoNA).append(" type=\"checkbox\" name=\"").append(cont.getColClickCompra().getId())
				.append("\" id=\"").append(cont.getColClickCompra().getId()).append("@")
				.append(cont.getColClickCompra().getNmsituac()).append("@")
				.append(cont.getColClickCompra().getCdaseg()).append("@")
				.append(cont.getColClickCompra().getCdplan())
				.append("\">").append(ttc).append("</td>");
				//totales contemp
				if(tipoNA.equals("")){
					html.append("<td rowspan=").append(contemp.getColClickCob().getRowspan())
					.append("><div id=\"").append(contemp.getColClickCob().getId()).append("\">")
					.append(contemp.getColClickCob().getUrl()).append(contemp.getColClickCob().getCdplan())
					.append(".png\" style=\"cursor: pointer;\" onclick=\"fopV(\"").append(contemp.getColClickCob().getCdaseg()).append("\",\"")
					.append(contemp.getColClickCob().getCdplan()).append("\",\"")
					.append(contemp.getColClickCob().getId())
					.append("\",\"\")\"").append("></div></td>");
				}else{
					html.append("<td rowspan=").append(contemp.getColClickCob().getRowspan())
					.append("><div id=\"").append(contemp.getColClickCob().getId()).append("\">")
					.append(contemp.getColClickCob().getUrl()).append(contemp.getColClickCob().getCdplan())
					.append(".png\"></div></td>");
				}
			}
			html.append("</tr><tr>")
			.append("<td style=\"padding-left: 5px;font-size: 9pt; font-family: sans-serif;\" width=")
			.append(p.getColConceptoTotal().getWidth())
			.append(" height=").append(p.getColConceptoTotal().getHeight())
			.append("><b>").append(p.getColConceptoTotal().getHtml()).append("</b></td>");
			List<Contenedor> pnls = p.getPanelTotales();
			for(int j = 0; j<pnls.size(); j++){
				Contenedor cont = (Contenedor)pnls.get(j);
				int wMon = Integer.parseInt(cont.getColMoneda().getWidth());
				if(navegador.equals("Firefox")  || navegador.equals("IE7")){
					wMon = wMon-3;
				}
				String tipoNA = cont.getColImporte().getHtml().toUpperCase();
				String color = "";
				int etx = tipoNA.indexOf("N/A");
				int etxA = tipoNA.indexOf("APLICA");
				if(etx >= 0 || etxA >= 0){
					color = "Silver";
				}else{
					color = "Black";
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColMoneda().getHeight())
				.append("px; ").append(cont.getColMoneda().getStyle())
				.append("\"><font color=\"").append(color).append("\">").append(cont.getColMoneda().getHtml())
				.append("</font></td>");
				wMon = Integer.parseInt(cont.getColImporte().getWidth());
				if(navegador.equals("Firefox")  || navegador.equals("IE7")){
					wMon = wMon-4;
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColImporte().getHeight())
				.append("px; font-size: 9pt; ").append(cont.getColImporte().getStyle())
				.append("\"><b><font color=\"").append(color).append("\">").append(cont.getColImporte().getHtml())
				.append("</font></b></td>");
			}
			html.append("</tr><tr>")
			.append("<td style=\"padding-left: 5px;font-size: 8pt; font-family: sans-serif;\" width=")
			.append(p.getColConceptoPrimerPago().getWidth())
			.append(" height=").append(p.getColConceptoPrimerPago().getHeight())
			.append(">").append(p.getColConceptoPrimerPago().getHtml()).append("</td>");
			List<Contenedor> pnlsPP = p.getPanelPrimerPago();
			for(int j = 0; j<pnlsPP.size(); j++){
				Contenedor cont = (Contenedor)pnlsPP.get(j);
				int wMon = Integer.parseInt(cont.getColMoneda().getWidth());
				if(navegador.equals("Firefox")  || navegador.equals("IE7")){
					wMon = wMon-3;
				}
				String tipoNA = cont.getColImporte().getHtml().toUpperCase();
				String color = "";
				int etx = tipoNA.indexOf("N/A");
				int etxA = tipoNA.indexOf("APLICA");
				if(etx >= 0 || etxA >= 0){
					color = "Silver";
				}else{
					color = "Black";
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColMoneda().getHeight())
				.append("px; ").append(cont.getColMoneda().getStyle())
				.append("\"><font color=\"").append(color).append("\">").append(cont.getColMoneda().getHtml())
				.append("</font></td>");
				wMon = Integer.parseInt(cont.getColImporte().getWidth());
				if(navegador.equals("Firefox")  || navegador.equals("IE7")){
					wMon = wMon-4;
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColImporte().getHeight())
				.append("px; font-size: 8pt; ").append(cont.getColImporte().getStyle())
				.append("\"><font color=\"").append(color).append("\">").append(cont.getColImporte().getHtml())
				.append("</font></td>");
			}
			html.append("</tr><tr>")
			.append("<td style=\"padding-left: 5px;font-size: 8pt; font-family: sans-serif;\" width=")
			.append(p.getColConceptoPagosSubsecuentes().getWidth())
			.append(" height=").append(p.getColConceptoPagosSubsecuentes().getHeight())
			.append(">").append(p.getColConceptoPagosSubsecuentes().getHtml()).append("</td>");

			List<Contenedor> pnlsPS = p.getPanelPagosSubsecuentes();
			for(int j = 0; j<pnlsPS.size(); j++){
				Contenedor cont = (Contenedor)pnlsPS.get(j);
				int wMon = Integer.parseInt(cont.getColMoneda().getWidth());
				if(navegador.equals("Firefox")  || navegador.equals("IE7")){
					wMon = wMon-3;
				}
				String tipoNA = cont.getColImporte().getHtml().toUpperCase();
				String color = "";
				int etx = tipoNA.indexOf("N/A");
				int etxA = tipoNA.indexOf("APLICA");
				if(etx >= 0 || etxA >= 0){
					color = "Silver";
				}else{
					color = "Black";
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColMoneda().getHeight())
				.append("px; ").append(cont.getColMoneda().getStyle())
				.append("\"><font color=\"").append(color).append("\">").append(cont.getColMoneda().getHtml())
				.append("</font></td>");
				wMon = Integer.parseInt(cont.getColImporte().getWidth());
				if(navegador.equals("Firefox")  || navegador.equals("IE7")){
					wMon = wMon-4;
				}
				html.append("<td style=\"width:").append(wMon)
				.append("px; heigh:").append(cont.getColImporte().getHeight())
				.append("px; font-size: 8pt; ").append(cont.getColImporte().getStyle())
				.append("\"><font color=\"").append(color).append("\">").append(cont.getColImporte().getHtml())
				.append("</font></td>");
			}

			html.append("</tr></table>");
		}
	}
	//html.append("</body></html>");
	System.out.println("***HHHH**** ltsPlanes FOP: " + html.toString());
	return html.toString();
}

}
