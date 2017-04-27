package mx.com.gseguros.confpantallas.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicComboVo;
import mx.com.gseguros.confpantallas.model.DinamicControlAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicData;
import mx.com.gseguros.confpantallas.model.DinamicItemBean;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrGetVo;
import mx.com.gseguros.confpantallas.model.ViewBean;
import mx.com.gseguros.confpantallas.model.ViewPanel;
import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AdminCargaPanelesDelegate {
	
	public AdminCargaPanelesDelegate() {
		super();
		//TODO: cambiar usando Spring Ioc:
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		dinamicDAO = (DinamicDaoInterface)context.getBean("dinamicDAOImpl");
	}
	
	private DinamicDaoInterface dinamicDAO;
	
	private Logger log = Logger.getAnonymousLogger();
	ArrayList<String> mapaCmb = new ArrayList<String>();
	ArrayList<String> mapaCmbHijo = new ArrayList<String>();
	
	public String GetListadePaneles(){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "listaPanelesExistentes");
			List<DinamicComboVo> ltsP = dinamicDAO.getListaCombox(data);
			JSONArray jsonObject = JSONArray.fromObject(ltsP);
			System.out.println(jsonObject);
			rgs = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	
	public String getDataCombo(String tabla, String valor){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "listaValoresTablaApoyo");
			data.put("tabla", tabla);

			List<DinamicComboVo> ltsP = dinamicDAO.getDinamicComboVo(data);
			JSONArray jsonObject = JSONArray.fromObject(ltsP);
			System.out.println(jsonObject);
			rgs = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
		
	}
	public String getDataGrid(String tabla, String valor){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "SqlQuery");
			data.put("tabla", tabla);
			data.put("valor", valor);
			String qry = dinamicDAO.getString(data);
			List<HashMap> ltsP = dinamicDAO.GetDataGrid(qry);
			JSONArray jsonObject = JSONArray.fromObject(ltsP);
			System.out.println(jsonObject);
			rgs = jsonObject.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	public String getDataComboHijo(String tabla, String valor){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "listaValoresTablaApoyoHijos");
			data.put("tabla", tabla);
			data.put("valor", valor);
			List<DinamicComboVo> ltsP = dinamicDAO.getDinamicComboVo(data);
			JSONArray jsonObject = JSONArray.fromObject(ltsP);
			System.out.println(jsonObject);
			rgs = jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
		
	}
	public List<DinamicData> GetListaTablas(List<String> comboNames, List<String> comboNamesHijo){
		List<DinamicData> ltsP = new ArrayList<DinamicData>();
		Iterator<String> it = comboNames.iterator();
		while (it.hasNext()) {
			DinamicData dt = new DinamicData();
			String txt =  it.next();
			dt.setNombreVar(txt);
			dt.setDescripcion(this.isSoon(comboNamesHijo, txt));
			ltsP.add(dt);
		}
		return ltsP;
	}
	public String isSoon(List<String> comboNamesHijo, String txt){
		String rgs = "llenaCombo";
		Iterator<String> it = comboNamesHijo.iterator();
		txt = "store"+txt;
		while (it.hasNext()) {
			if(txt.equals(it.next())){rgs="";break;}
		}
		return rgs;
	}
	public HashMap<String, Object> GeneraJson(String panel){
		log.info("Voy a buscar el ID del panel: "+panel);
		HashMap<String, Object> rgs = new HashMap<String, Object>();
		List<ViewBean> listViewBean = new ArrayList<ViewBean>();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("operacion", "getListas");
		data.put("panel", panel);
		data.put("query", "IdPanel");
		List<HashMap> listaIdPanel = this.GetLista(data);
		
		List<ViewPanel> ltsPaneles = new ArrayList<ViewPanel>();

		Integer cP = 0;
		Iterator<HashMap> itLpf = listaIdPanel.listIterator();
		while(itLpf.hasNext()) {
			Map rowI = itLpf.next();
			Integer idP = Integer.valueOf(rowI.get("IDPANEL").toString());
			Integer idT = Integer.valueOf(rowI.get("IDPADRE").toString());
			log.info("Trabajando con el panel: "+idP);
			log.info("Su panel papa es:::::::: "+idT);
			if(cP != idT){
				ViewPanel vP = new ViewPanel();
				vP.setId(rowI.get("IDPANEL").toString());
				vP.setIdPadre(rowI.get("IDPADRE").toString());
				cP = idT;
				data.clear();
				data.put("operacion", "getListas");
				data.put("id", idP);
				data.put("query", "AttrPanel");
				vP.setListaAttr(this.GetListaDinamicPanelAttrGetVo(data));
				vP.setListaCtrls(new ArrayList<String>());
				ltsPaneles.add(vP);
			}else{
				Iterator<ViewPanel> itPnl = ltsPaneles.iterator();
				while (itPnl.hasNext()) {
					ViewPanel viewPanel =  itPnl.next();
					if(viewPanel.getIdPadre().equals(rowI.get("IDPADRE").toString())){
						viewPanel.getListaCtrls().add(rowI.get("IDPANEL").toString());
						break;
					}
				}
				
			}
		}
		Iterator<ViewPanel> itPnl = ltsPaneles.iterator();
		while (itPnl.hasNext()) {
			ViewPanel viewPanel =  itPnl.next();
			Integer idP = Integer.valueOf(viewPanel.getId());
			ViewBean pnl = new ViewBean();
			pnl.setNombreVar("miVar"+this.getValorAttr(viewPanel.getListaAttr(), "id"));
			pnl.setCodigo(this.transToJson(viewPanel.getListaAttr(),idP, viewPanel.getListaCtrls(), "con"));
			pnl.setXtype(this.getValorAttr(viewPanel.getListaAttr(), "xtype"));
			listViewBean.add(pnl);
		}
		rgs.put("lista", listViewBean);
		rgs.put("listaCmb", mapaCmb);
		rgs.put("listaCmbHijo", mapaCmbHijo);
		return rgs;
	} 
	
	public String transToJson(List<DinamicPanelAttrGetVo> listaAttrPanel, Integer idP, ArrayList<String> listaCtrls, String cabecero){
		StringBuffer strJson = new StringBuffer();
		String rgs = "";
		String tipo = this.getValorAttr(listaAttrPanel, "xtype");
		if(cabecero.equals("con")){
			if(tipo.equals("form")){
				strJson.append("Ext.form.Panel({");
			}else if(tipo.equals("tabpanel")){
				strJson.append("Ext.TabPanel({");
			}else if(tipo.equals("window")){
				strJson.append("Ext.Window({");
			}else if(tipo.equals("panel")){
				strJson.append("Ext.Panel({");
			}
		}else{
			strJson.append("{");
		}
		
		Iterator<DinamicPanelAttrGetVo> lts = listaAttrPanel.iterator();
		while (lts.hasNext()) {
			DinamicPanelAttrGetVo attrLts = lts.next();
			strJson.append(attrLts.getAttr()).append(":");
			strJson.append(this.setStrAttr(attrLts.getValor(), attrLts.getTipo()));
			strJson.append(",");
		}
		ArrayList<Object> ltsCtrl = null;
		
		if(tipo.equals("form") || tipo.equals("tabpanel") || tipo.equals("panel") || tipo.equals("window")){
			strJson.append("items:[");
			//Conocer el numero y orden de controles ligados al panel
			ltsCtrl = this.transControlesAttrToJson(idP);
			strJson.append(ltsCtrl.get(0));
			Iterator<String> itlistaCtrls = listaCtrls.iterator();
			StringBuffer tmpStr = new StringBuffer();
			while (itlistaCtrls.hasNext()){
				String pnl = itlistaCtrls.next();
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("operacion", "getListas");
				data.put("id", pnl);
				data.put("query", "AttrPanel");
				tmpStr.append(this.transToJson(this.GetListaDinamicPanelAttrGetVo(data),Integer.parseInt(pnl), new ArrayList<String>(),"")).append(",");
				System.out.println(tmpStr.toString());
				System.out.println("pausa");
			}
			rgs = tmpStr.toString();
			if(!rgs.equals("")){
				rgs = rgs.substring(0, tmpStr.toString().length()-1);
				strJson.append(rgs);
			}
		}
		
		if(((List) ltsCtrl.get(1)).size() > 0){
			StringBuffer strConBtn = new StringBuffer(); 
			strConBtn.append("],buttons: [");
			Iterator<List> itlts = ((List) ltsCtrl.get(1)).iterator();
			while (itlts.hasNext()) {
				ArrayList<DinamicPanelAttrGetVo> ltsAttr = (ArrayList)itlts.next();
				strConBtn.append(this.getStrJsonAttrCtrol(ltsAttr,"",null,0));
			}
			rgs = strConBtn.toString();
			rgs = rgs.substring(0, strConBtn.toString().length()-1);
			rgs += "]"; 
			strJson.append(rgs);
		}else{
			strJson.append("]");
		}
		if(cabecero.equals("con")){
			strJson.append("});");
		}else{
			strJson.append("}");
		}
		rgs = strJson.toString();
		log.info(rgs);
		return rgs;
	}
	
	public ArrayList<Object> transControlesAttrToJson(Integer idP){
		ArrayList<Object> rgs = new ArrayList();
		StringBuffer strCtrlAttrJson = new StringBuffer();
		List<List> ltsCtrlAttrButton = new ArrayList<List>();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("operacion", "getListas");
		data.put("panelID", idP);
		data.put("query", "ListaIdControles");
		List<HashMap> listaIdControles = this.GetLista(data);
		
		log.info("Numero de controles a insertar: " + listaIdControles.size());
		Iterator<HashMap> itCA = listaIdControles.listIterator();
		while(itCA.hasNext()) {
			Map rowI = itCA.next();
			Integer idCtrl = Integer.valueOf(rowI.get("IDCONTROL").toString());
			String strCtrl = rowI.get("DESCRIPCION").toString();
			Integer rowCtrl = 0;
			Boolean isBton = false;
			if(strCtrl.indexOf("button") > -1){
				isBton = true;
				rowCtrl = this.IsFormularioConBotones(idCtrl, idP);
			}
			//Voy por los atributos del control
			data.clear();
			data.put("operacion", "getListas");
			data.put("idControl", idCtrl);
			data.put("idPanel", idP);
			data.put("query", "AttrControl");
			List<DinamicPanelAttrGetVo> listaAttrControl = this.GetListaDinamicPanelAttrGetVo(data);
			if((isBton) && rowCtrl > 0){
				ltsCtrlAttrButton.add(listaAttrControl);
			}else{
				log.info("Listado de atributos del control: " + listaAttrControl);
				strCtrlAttrJson.append(this.getStrJsonAttrCtrol(listaAttrControl,strCtrl,listaIdControles,idP));
			}
		}
		String rgsC = strCtrlAttrJson.toString();
		if(!rgsC.equals("")){
			rgsC = rgsC.substring(0, strCtrlAttrJson.toString().length()-1);
		}

		log.info("String total de atributos de controles: " + rgsC);	
		rgs.add(rgsC);
		rgs.add(ltsCtrlAttrButton);
		return rgs;
	}
	
	private String getStrJsonAttrCtrol(List<DinamicPanelAttrGetVo> listaAttrControl, String ctrl, List<HashMap> listaIdControles,Integer idP){
		StringBuffer stB = new StringBuffer();
		stB.append("{");
		Iterator<DinamicPanelAttrGetVo> lts = listaAttrControl.iterator();
		StringBuffer rgsAttr = new StringBuffer();
		while (lts.hasNext()) {
			DinamicPanelAttrGetVo attrLts = lts.next();
			if(!attrLts.getAttr().equals("row")){//Excluyo atributo....
				if(attrLts.getAttr().equals("store")){
					//voy a validar que exista el strore para que no truene en tiempo de construccion
					String nameStore = attrLts.getValor();
					if(!this.existeStore(nameStore).equals("")){
						rgsAttr.append(attrLts.getAttr()).append(":");
						rgsAttr.append("store"+nameStore);
						rgsAttr.append(",");
						mapaCmb.add(nameStore);
					}
				}else if (attrLts.getAttr().equals("estilo")){
					String attrG = this.setStrAttr(attrLts.getValor(), attrLts.getTipo());
					if(attrLts.getValor().equals("reset")){
						rgsAttr.append("handler").append(":").append("function() {this.up('form').getForm().reset();}").append(",");
					}else if (attrLts.getValor().equals("submit")){
						rgsAttr.append("formBind: true,");
						rgsAttr.append("handler: function() {var form = this.up('form').getForm();if (form.isValid()) {form.submit({success: function(form, action) {window.open('../../jsp/confpantallas/newpanel.jsp');},failure: function(form, action) {Ext.Msg.alert('Failed', 'fallo');}});}}");
						rgsAttr.append(",");
					}
				}else if (attrLts.getAttr().equals("isPadre") && ctrl.indexOf("datafield") > -1){
					String ctrlHijo = this.getValorAttr(listaAttrControl, "isPadre");
					String strtime = this.getAttrControl(ctrlHijo, "name", listaIdControles, idP, "value");
					rgsAttr.append("\n");
					rgsAttr.append("listeners:{blur: function (e){");
					rgsAttr.append("\n");
					rgsAttr.append("var tipo = '").append(getTipo(strtime)).append("';");
					rgsAttr.append("\n");
					rgsAttr.append("var time = ").append(getTimeD(strtime)).append(";");
					rgsAttr.append("\n");
					rgsAttr.append("var dateTemp = new Date(Ext.getCmp('"+this.getValorAttr(listaAttrControl, "id")+"').getValue());");
					rgsAttr.append("\n");
					rgsAttr.append("if(tipo === 'Y'){");
					rgsAttr.append("\n");
					rgsAttr.append("dateTemp.setFullYear(dateTemp.getFullYear()+time);");
					rgsAttr.append("\n");
					rgsAttr.append("}else if(tipo === 'M'){");
					rgsAttr.append("\n");
					rgsAttr.append("dateTemp.setMonth(dateTemp.getMonth()+time);");
					rgsAttr.append("}else if(tipo === 'D'){").append("\n");
					rgsAttr.append("dateTemp.setDate(dateTemp.getDate()+time);");
					rgsAttr.append("}var mes = dateTemp.getMonth() + 1;").append("\n");
					rgsAttr.append("mes = '' + mes;").append("\n");
					rgsAttr.append("var nC = mes.length;").append("\n");
					rgsAttr.append("if(nC == 1){mes = '0' + mes;}").append("\n");
					rgsAttr.append("var dia = dateTemp.getDate();").append("\n");
					rgsAttr.append("dia = '' + dia;").append("\n");
					rgsAttr.append("var nD = dia.length;").append("\n");
					rgsAttr.append("if(nD == 1){dia = '0' + dia;}").append("\n");
					rgsAttr.append("var ano = dateTemp.getFullYear();").append("\n");
					rgsAttr.append("ano = '' + ano;").append("\n");
					rgsAttr.append("\n");
					rgsAttr.append("Ext.getCmp('"+ctrlHijo+"').setValue(dia + '/'+ mes  +'/' + ano);");
					rgsAttr.append("\n");
					rgsAttr.append("}},");
				}else if (attrLts.getAttr().equals("isPadre")){
					String idCmb = this.getValorAttr(listaAttrControl, "isPadre");
					String strCmb = this.getAttrControl(idCmb, "name", listaIdControles, idP, "id");
					String strStore = "store"+this.getAttrControl(idCmb, "name", listaIdControles, idP, "store");
					mapaCmbHijo.add(strStore);
					rgsAttr.append("\n");
					rgsAttr.append("listeners").append(":");
					rgsAttr.append("{").append("select: function (combo, record){ ");
					rgsAttr.append("\n");
					rgsAttr.append("Ext.getCmp('").append(strCmb).append("').reset();");
					rgsAttr.append("\n");
					//rgsAttr.append(strStore).append(".clearFilter(true);");
					//rgsAttr.append("\n");
					//rgsAttr.append(strStore).append(".filter('key', combo.value);");
					//rgsAttr.append("\n");
					rgsAttr.append(strStore+".load({params:{valor:combo.value,tarea: 'llenaComboHijo'}});");
					rgsAttr.append("\n");
					rgsAttr.append("Ext.getCmp('").append(strCmb).append("').enable();");
					rgsAttr.append("\n");
					rgsAttr.append("}").append("}");
					rgsAttr.append(",");	
				}else if (attrLts.getAttr().equals("columns")){
					rgsAttr.append(attrLts.getAttr()).append(":");
					rgsAttr.append("[");
					rgsAttr.append(this.getAttrGrid(attrLts.getValor(), idP));
					rgsAttr.append("]");
					rgsAttr.append(",").append("\n");
					rgsAttr.append("store: Ext.create('Ext.data.Store', {").append("\n");
					rgsAttr.append("model: Ext.define('modelGridDef_").append(this.getValorAttr(listaAttrControl, "id")).append("',{ extend: 'Ext.data.Model', ").append("\n");
					rgsAttr.append("fields: [").append("\n");
					rgsAttr.append(this.getAttrModel(attrLts.getValor(), idP));
					rgsAttr.append("] }),").append("\n");
					rgsAttr.append("autoLoad: true,").append("\n");
					rgsAttr.append("proxy: { type: 'ajax', url : './confpantallas/cargainfo.action', ").append("\n");
					rgsAttr.append("reader: {type: 'json',root: 'success'}, extraParams: {tarea: 'llenaGrid', tabla:'");
					rgsAttr.append(idP).append("', valor:'").append(this.getValorAttr(listaAttrControl, "query")).append("'} } }),").append("\n");
				}else if (attrLts.getAttr().equals("query")){
					System.out.println("Brinco Atributo");
				}else if (attrLts.getAttr().equals("value") && ctrl.indexOf("datafield") > -1){
					System.out.println(attrLts.getAttr());
					rgsAttr.append("format: 'd/m/Y',");
					rgsAttr.append(attrLts.getAttr()).append(":");
					rgsAttr.append(this.getFechaFormat(attrLts.getValor(), attrLts.getTipo()));
					rgsAttr.append(",");
				}else{
					rgsAttr.append(attrLts.getAttr()).append(":");
					rgsAttr.append(this.setStrAttr(attrLts.getValor(), attrLts.getTipo()));
					rgsAttr.append(",");
				}
			}
		}
		String rgs = rgsAttr.toString();
		rgs = rgs.substring(0, rgsAttr.toString().length()-1);
		stB.append(rgs);
		if(ctrl.indexOf("combobox") > -1){
			String name = this.getValorAttr(listaAttrControl, "name");
			stB.append(isComboboxHijo(name,listaIdControles,idP));
		}
		stB.append("},");
		log.info("Total de atributos del Control: " + stB.toString());
		return stB.toString();
	}
	private String getTipo(String dato){
		String rgs = "";
		if(dato.indexOf("sysdate") > -1 || dato.indexOf("SYSDATE") > -1){
			String signo = getSigno(dato);
			if(!signo.equals("")){
				String resto = dato.substring(7).toUpperCase();
				if(resto.indexOf("A") > -1){
					rgs = "Y";
				}else if(resto.indexOf("M") > -1){
					rgs = "M";
				}else if(resto.indexOf("D") > -1){
					rgs = "D";
				}else{
					rgs = "D";
				}
			}
		}
		return rgs;
	}
	private String getTimeD(String dato){
		String rgs = "";
		if(dato.indexOf("sysdate") > -1 || dato.indexOf("SYSDATE") > -1){
			String signo = getSigno(dato);
			if(!signo.equals("")){
				String resto = dato.substring(7).toUpperCase();
				if(resto.indexOf("A") > -1){
					rgs = resto.substring(resto.indexOf("A")+1).trim();
				}else if(resto.indexOf("M") > -1){
					rgs = resto.substring(resto.indexOf("M")+1).trim();
				}else if(resto.indexOf("D") > -1){
					rgs = resto.substring(resto.indexOf("D")+1).trim();
				}else{
					if(signo.equals("mas")){
						rgs = resto = resto.substring(resto.indexOf("+")+1).trim();
					}else if(signo.equals("menos")){
						rgs = resto = resto.substring(resto.indexOf("-")+1).trim();
					}
				}
			}
		}
		return rgs;
	}
	private String getFechaFormat(String valor, String tipo){
		String rgs = valor;
		if(valor.indexOf("sysdate") > -1 || valor.indexOf("SYSDATE") > -1){
			String signo = getSigno(valor);
			if(!signo.equals("")){
				String resto = valor.substring(7).toUpperCase();
				String factor = "";
				Integer n = 0;
				if(resto.indexOf("A") > -1){
					factor = "Ano";
					resto = resto.substring(resto.indexOf("A")+1).trim();
					n = Integer.parseInt(resto);
				}else if(resto.indexOf("M") > -1){
					factor = "Mes";
					resto = resto.substring(resto.indexOf("M")+1).trim();
					n = Integer.parseInt(resto);
				}else if(resto.indexOf("D") > -1){
					factor = "Dia";
					resto = resto.substring(resto.indexOf("D")+1).trim();
					n = Integer.parseInt(resto);
				}else{
					factor = "Dia";
					if(signo.equals("mas")){
						resto = resto.substring(resto.indexOf("+")+1).trim();
					}else if(signo.equals("menos")){
						resto = resto.substring(resto.indexOf("-")+1).trim();
					}
					n = Integer.parseInt(resto);
				}
				rgs = this.setStrAttr(this.getFecha(signo, factor, n), tipo);
			}else{
				rgs = "new Date()";
			}
		}else{
			rgs = this.setStrAttr(valor, tipo);
		}
		return rgs;
	}
	
	private String getFecha(String signo, String factor,Integer n){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if(factor.equals("Dia") && signo.equals("mas")){
			cal.add(Calendar.DATE, n);
		}else if(factor.equals("Dia") && signo.equals("menos")){
			cal.add(Calendar.DATE, -n);
		}else if(factor.equals("Mes") && signo.equals("menos")){
			cal.add(Calendar.MONTH, -n);
		}else if(factor.equals("Mes") && signo.equals("mas")){
			cal.add(Calendar.MONTH, n);
		}else if(factor.equals("Ano") && signo.equals("menos")){
			cal.add(Calendar.YEAR, -n);
		}else if(factor.equals("Ano") && signo.equals("mas")){
			cal.add(Calendar.YEAR, n);
		}
		String dia = getDia00(cal.get(Calendar.DAY_OF_MONTH));
		String mes = getMes00(cal.get(Calendar.MONTH)+1);
		String ano = String.valueOf(cal.get(Calendar.YEAR));
		return dia + "/" + mes + "/" + ano; 
	}
	private String getDia00(int dia){
		String rgs = "";
		if(String.valueOf(dia).length() == 1){
			rgs = "0"+String.valueOf(dia);
		}else{
			rgs = String.valueOf(dia);
		}
		return rgs;
	}
	private String getMes00(int mes){
		String rgs = "";
		if(String.valueOf(mes).length() == 1){
			rgs = "0"+String.valueOf(mes);
		}else{
			rgs = String.valueOf(mes);
		}
		return rgs;
	}
	private String getAttrModel(String id, Integer panel){
		StringBuffer stB = new StringBuffer();
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "ListadeControlesGrid");
			data.put("id", String.valueOf(id));
			data.put("panel", String.valueOf(panel));
			List<Map> ltsP = dinamicDAO.GetListados(data);
			Iterator<Map> iltsP = ltsP.iterator();
			while (iltsP.hasNext()) {
				StringBuffer rgsAttr = new StringBuffer();
				rgsAttr.append("{");
				Map idCtrolM = iltsP.next();
				String idCtrol = idCtrolM.get("IDCONTROL").toString();
				data.put("query", "ListadeControlesGridAttr");
				data.put("control", idCtrol);
				List<DinamicControlAttrVo> ltsPAG = dinamicDAO.GetAttrGrid(data);
				Iterator<DinamicControlAttrVo> iltsPAG = ltsPAG.iterator();
				while (iltsPAG.hasNext()) {
					DinamicControlAttrVo ctrl = iltsPAG.next();
					if("tipoG".equals(ctrl.getAttr())){
						rgsAttr.append("type:").append(this.setStrAttr(ctrl.getValor(), ctrl.getTipo())).append(",");
					}else if("dataIndex".equals(ctrl.getAttr())){
						rgsAttr.append("name:").append(this.setStrAttr(ctrl.getValor(), ctrl.getTipo()));
					}
				}
				rgs = rgsAttr.toString();
				stB.append(rgs);
				stB.append("},");
			}
			System.out.println(ltsP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rgs = stB.toString();
		rgs = rgs.substring(0, stB.toString().length()-1);
		return rgs;
	}
	private String getAttrGrid(String id, Integer panel){
		StringBuffer stB = new StringBuffer();
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "ListadeControlesGrid");
			data.put("id", String.valueOf(id));
			data.put("panel", String.valueOf(panel));
			List<Map> ltsP = dinamicDAO.GetListados(data);
			Iterator<Map> iltsP = ltsP.iterator();
			while (iltsP.hasNext()) {
				StringBuffer rgsAttr = new StringBuffer();
				rgsAttr.append("{");
				Map idCtrolM = iltsP.next();
				String idCtrol = idCtrolM.get("IDCONTROL").toString();
				data.put("query", "ListadeControlesGridAttr");
				data.put("control", idCtrol);
				List<DinamicControlAttrVo> ltsPAG = dinamicDAO.GetAttrGrid(data);
				Iterator<DinamicControlAttrVo> iltsPAG = ltsPAG.iterator();
				while (iltsPAG.hasNext()) {
					DinamicControlAttrVo ctrl = iltsPAG.next();
					if(!"tipoG".equals(ctrl.getAttr())){
						rgsAttr.append(ctrl.getAttr()).append(":").append(this.setStrAttr(ctrl.getValor(), ctrl.getTipo())).append(",");
					}
				}
				rgs = rgsAttr.toString();
				rgs = rgs.substring(0, rgsAttr.toString().length()-1);
				stB.append(rgs);
				stB.append("},");
			}
			//JSONArray jsonObject = JSONArray.fromObject(ltsP);
			System.out.println(ltsP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rgs = stB.toString();
		rgs = rgs.substring(0, stB.toString().length()-1);
		return rgs;
	}
	private String getAttrControl(String control,String attrBusca,List<HashMap> listaIdControles,Integer idP,String attr){
		String rgs = "";
		HashMap<String, Object> data = new HashMap<String, Object>();
		Iterator<HashMap> itCA = listaIdControles.listIterator();
		while(itCA.hasNext()) {
			Map rowI = itCA.next();
			Integer idCtrl = Integer.valueOf(rowI.get("IDCONTROL").toString());
			data.clear();
			data.put("operacion", "getListas");
			data.put("idControl", idCtrl);
			data.put("idPanel", idP);
			data.put("query", "AttrControl");
			List<DinamicPanelAttrGetVo> listaAttrControl = this.GetListaDinamicPanelAttrGetVo(data);
			String isp = this.getValorAttr(listaAttrControl, attrBusca);
			if(control.equals(isp)){
				rgs = this.getValorAttr(listaAttrControl, attr);
				break;
			}
		}
		return rgs;
	}
	private String isComboboxHijo(String name,List<HashMap> listaIdControles,Integer idP){
		String rgs = "";
		HashMap<String, Object> data = new HashMap<String, Object>();
		Iterator<HashMap> itCA = listaIdControles.listIterator();
		while(itCA.hasNext()) {
			Map rowI = itCA.next();
			Integer idCtrl = Integer.valueOf(rowI.get("IDCONTROL").toString());
			data.clear();
			data.put("operacion", "getListas");
			data.put("idControl", idCtrl);
			data.put("idPanel", idP);
			data.put("query", "AttrControl");
			List<DinamicPanelAttrGetVo> listaAttrControl = this.GetListaDinamicPanelAttrGetVo(data);
			String isp = this.getValorAttr(listaAttrControl, "isPadre");
			if(name.equals(isp)){
				rgs = ",disabled:true";
				break;
			}
		}
		return rgs;
	}
	
	private Integer IsFormularioConBotones(Integer idC,Integer idP){
		Integer rgs = 0;
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("operacion", "getListas");
		data.put("panelID", idP);
		data.put("controlID", idC);
		data.put("query", "getTipoBoton");
		List<HashMap> dato = this.GetLista(data);
		Map mapD = dato.get(0);
		rgs = Integer.parseInt(mapD.get("VALOR").toString());
		log.info("El row del boton es: " + rgs);
		return rgs;
	}
	
	private String setStrAttr(String valor, String tipo ){
		String rgs = "";
		if("S".equals(tipo)){
			rgs = "'"+valor+"'";
		}else{
			rgs = valor;
		}
		return rgs;
	}
	
	private String existeStore(String name){
		String rgs = "";
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("operacion", "analizaStore");
			data.put("store", name);
			data.put("query", "nameTablaApoyoData");
			List<DinamicItemBean> ltsP = dinamicDAO.getDinamicItemBean(data);
			//JSONArray jsonObject = JSONArray.fromObject(ltsP);
			//System.out.println(jsonObject);
			//rgs = jsonObject.toString();
			if(ltsP.size() > 0){
				rgs = "existe";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	
	private String getValorAttr(List<DinamicPanelAttrGetVo> listaAttrPanel, String attr){
		String rgs = "";
		Iterator<DinamicPanelAttrGetVo> lts = listaAttrPanel.iterator();
		while (lts.hasNext()) {
			DinamicPanelAttrGetVo attrLts = lts.next();
			if(attrLts.getAttr().equals(attr)){
				rgs = attrLts.getValor();
				break;
			}
		}
		return rgs;
	}
	
	
	
	public List<DinamicPanelAttrGetVo> GetListaDinamicPanelAttrGetVo (HashMap<String, Object> data){
		List<DinamicPanelAttrGetVo> rgs = new ArrayList<DinamicPanelAttrGetVo>();
		try {
			HashMap<String, String> mapa = new HashMap<String, String>();
			if (data.get("id") != null){mapa.put("id", data.get("id").toString());}
			if (data.get("idPanel") != null){mapa.put("idPanel", data.get("idPanel").toString());}
			if (data.get("idControl") != null){mapa.put("idControl", data.get("idControl").toString());}
			mapa.put("query", data.get("query").toString());
			rgs = (List<DinamicPanelAttrGetVo>) dinamicDAO.getDinamicPanelAttrGetVo(mapa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	public List<HashMap> GetLista (HashMap<String, Object> data){
		List<HashMap> rgs = new ArrayList<HashMap>();
		try {
			HashMap<String, String> mapa = new HashMap<String, String>();
			if (data.get("panel") != null){mapa.put("panel", data.get("panel").toString());}
			if (data.get("panelID") != null){mapa.put("panelID", data.get("panelID").toString());}
			if (data.get("controlID") != null){mapa.put("controlID", data.get("controlID").toString());}
			mapa.put("query", data.get("query").toString());
			rgs = (List<HashMap>) dinamicDAO.GetListadosHM(mapa);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgs;
	}
	
	private String getSigno(String valor){
		String rgs = "";
		if(valor.indexOf("+") > -1){
			rgs = "mas";
		}else if(valor.indexOf("-") > -1){
			rgs = "menos";
		}
		return rgs;
	}

	public void setDinamicDAO(DinamicDaoInterface dinamicDAO) {
		this.dinamicDAO = dinamicDAO;
	}
	
}