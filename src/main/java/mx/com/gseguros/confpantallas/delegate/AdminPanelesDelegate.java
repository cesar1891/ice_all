package mx.com.gseguros.confpantallas.delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mx.com.gseguros.confpantallas.base.dao.DinamicDaoInterface;
import mx.com.gseguros.confpantallas.model.DinamicAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicControlAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicControlVo;
import mx.com.gseguros.confpantallas.model.DinamicData;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelVo;
import mx.com.gseguros.confpantallas.model.ViewBean;
import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AdminPanelesDelegate {
	
	private Logger logger = Logger.getLogger(AdminPanelesDelegate.class);
	
	public AdminPanelesDelegate() {
		super();
		//TODO: cambiar usando Spring Ioc:
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		dinamicDAO = (DinamicDaoInterface)context.getBean("dinamicDAOImpl");
	}
	
	private DinamicDaoInterface dinamicDAO;
	
	int consec = 1;
	int intMaxp = 0;
	Collection<DinamicAttrVo> listPaneles = null;
	ArrayList<DinamicControlVo> lstControles = new ArrayList<DinamicControlVo>();
	ArrayList<ArrayList> lstdeLstCtrolAttr = new ArrayList<ArrayList>();
	ArrayList<DinamicControlAttrVo> lstdeLstCtrolGridAttr = new ArrayList<DinamicControlAttrVo>();
	ArrayList<DinamicControlAttrVo> lstdeLstCtrolGridSql = new ArrayList<DinamicControlAttrVo>();
	
	public boolean ExistePanel(String panel) {
		boolean existe = false;
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "existePanel");
			data.put("panel", panel);
			dinamicDAO.getString(data);
			//Si no hubo excepcion, se obtuvo resultado, y si existe:
			existe = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.warn("El panel no existe es new...");
		}
		return existe;
	}
	
	public HashMap<String, String> SetPanel (String panel, String json) {
		HashMap<String, String> rgs = new HashMap<String, String>();
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			data.put("query", "maxPanel");
			String maxP = dinamicDAO.getString(data);
			intMaxp = Integer.parseInt(maxP);
			data.clear();
			data.put("query", "maxPadre");
			String maxPadre = dinamicDAO.getString(data);
			int intMaxPadre = Integer.parseInt(maxPadre);

			JSONArray jsonArrayUbicacion = JSONArray.fromObject(json);
			listPaneles =  JSONArray.toCollection(jsonArrayUbicacion, DinamicAttrVo.class);
			Iterator<DinamicAttrVo> it = listPaneles.iterator();
			List<DinamicAttrVo> arryPaneles = new ArrayList<DinamicAttrVo>();
			while(it.hasNext()) {
				DinamicAttrVo vo = it.next();
				if(vo.getIdPadre().equals("superpanel") && !vo.getIdHijo().equals("superpanel")){
					arryPaneles.add(vo);
				}
			}
			System.out.println(arryPaneles.toString());

			ArrayList<DinamicPanelVo> ltsNewPaneles = new ArrayList<DinamicPanelVo>();
			ArrayList<ArrayList> lstAttrPaneles = new ArrayList<ArrayList>();
			int cOrden = 0;
			it = arryPaneles.iterator();
			HashMap<String, Object> datas = new HashMap<String, Object>();
			while(it.hasNext()) {
				//ciclo para insertar panel
				intMaxPadre ++;
				DinamicAttrVo vo =  (DinamicAttrVo)it.next();
				ltsNewPaneles.add(new DinamicPanelVo(intMaxp, panel, intMaxPadre,vo.getOrder()));
				lstAttrPaneles.add(this.creaInsertPanel(vo));
				Boolean isIn = false;
				Iterator<DinamicAttrVo> itH = listPaneles.iterator();
				cOrden = vo.getOrder();
				while(itH.hasNext()) {
					DinamicAttrVo voH =  (DinamicAttrVo)itH.next();
					if(vo.getIdHijo().equals(voH.getIdPadre()) && (this.tieneHijos(voH.getIdHijo()))){
					//if(vo.getIdHijo().equals(voH.getIdPadre()) && (this.tienePapa(voH.getIdPadre(),voH.getIdHijo()))){
						isIn = true;
						cOrden ++;
						intMaxp ++;
						//intMaxPadre ++;
						ltsNewPaneles.add(new DinamicPanelVo(intMaxp, panel, intMaxPadre,cOrden));
						lstAttrPaneles.add(this.creaInsertPanel(voH));
						setArrayControles(voH.getIdHijo());
					}
				}
				if((!isIn)){
					setArrayControles(vo.getIdHijo());
				}
				intMaxp++;
			}
			datas.clear();
			datas.put("operacion", "setPanel");
			datas.put("panel", panel);
			datas.put("newPanel", ltsNewPaneles);
			datas.put("listaAttr", lstAttrPaneles);
			datas.put("listaControles", lstControles);
			datas.put("ltsListaControlesArrt", lstdeLstCtrolAttr);
			datas.put("lstdeLstCtrolGridAttr", lstdeLstCtrolGridAttr);
			datas.put("lstdeLstCtrolGridSql", lstdeLstCtrolGridSql);
			dinamicDAO.setPanel(datas);
			// tengo que man dar a obtener el codigo ExtJS
			AdminCargaPanelesDelegate adm = new AdminCargaPanelesDelegate();
			 HashMap<String, Object> dataExt = adm.GeneraJson(panel);
			 List<ViewBean> listadePaneles = (List<ViewBean>) dataExt.get("lista");
			 StringBuffer stl = new StringBuffer();
			 
			 stl.append("new Ext.form.Panel({autoScroll:true, border: false,");
			 stl.append("items:[");
			 for (int i = 1; i <= listadePaneles.size(); i++){
				 
				 stl.append(listadePaneles.get(i-1).getCodigo().replace(";", ""));
				 if(i != listadePaneles.size()){
					 stl.append(",");
				 }
				 stl.append("\n");
			 }
			 stl.append("]");
			 stl.append("});");
			 System.out.println(stl.toString());
			 List<DinamicData> lt = adm.GetListaTablas((List<String>) dataExt.get("listaCmb"),(List<String>) dataExt.get("listaCmbHijo"));
			 StringBuffer st = new StringBuffer();
			 if(lt.size() > 0){
				 //sesion.put("listaCatalogosEA", lt);
				 System.out.println("este es el codigo de los stores");
				 System.out.println(lt);
				 //st.append("Ext.QuickTips.init();").append("\n");
				 st.append("Ext.define('ComboData', {extend: 'Ext.data.Model',fields: [{type: 'string', name: 'key'},{type: 'string', name: 'value'}]});").append("\n");
				 for (int i = 0; i < lt.size(); i++){
					 DinamicData cp = lt.get(i);
					 st.append("var val").append(cp.getNombreVar()).append(" = '").append(cp.getNombreVar()).append("';").append("\n");
					 st.append("var store").append(cp.getNombreVar()).append("= Ext.create('Ext.data.Store',{model:'ComboData',proxy: {type: 'ajax',url: '../confpantallas/cargainfo.action',reader: {type: 'json',root: 'success'},extraParams: {tarea: '");
					 st.append(cp.getDescripcion()).append("', tabla:val").append(cp.getNombreVar()).append(", valor:val").append(cp.getNombreVar()).append("}},autoLoad: false});").append("\n");
				 }
				 System.out.println(st.toString());
			 }

			 HashMap<String, String> dataE = new HashMap<String, String>();
			 dataE.put("query", "setCFExtjs");
			 dataE.put("panel", maxP);
			 dataE.put("stores", st.toString());
			 dataE.put("codigo", stl.toString());
            dinamicDAO.setCFExtjs(dataE);
            rgs.put("panel", maxP);
            rgs.put("rgs", "");
		} catch (Exception e) {
			e.printStackTrace();
            rgs.put("panel", "");
            rgs.put("rgs", e.toString());
		}
		return rgs;
	}
	
	private Boolean tieneHijos(String hijo){
		Boolean rgs = false;
		Iterator<DinamicAttrVo> itH = listPaneles.iterator();
		while(itH.hasNext()) {
			DinamicAttrVo voH =  (DinamicAttrVo)itH.next();
			if(voH.getIdPadre().equals(hijo)){
				rgs = true;
				break;
			}
		}
		return rgs;
	}


	private Boolean tienePapa(String papa, String hijo){
		Boolean rgs = false;
		Iterator<DinamicAttrVo> itH = listPaneles.iterator();
		while(itH.hasNext()) {
			DinamicAttrVo voH =  (DinamicAttrVo)itH.next();
			if(voH.getIdHijo().equals(papa) && voH.getIdPadre().equals("superpanel")){
				rgs = this.tieneHijos(hijo);
				break;
			}
		}
		return rgs;
	}

	
	private void setArrayControles(String nombreP){
		List<DinamicAttrVo> arryControles = this.getListaControles(nombreP);
		Iterator<DinamicAttrVo> itC = arryControles.iterator();
		int ctrol = 1;
		while(itC.hasNext()) {
			DinamicAttrVo voC = itC.next();
			lstControles.add(new DinamicControlVo(ctrol,intMaxp,voC.getName(),voC.getOrder()));
			lstdeLstCtrolAttr.add(this.creaInsertControlAttrGral(voC, ctrol, intMaxp));
			if("textfield".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertTextAttr(voC, ctrol, intMaxp));
			}else if("numberfield".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertNumericAttr(voC, ctrol, intMaxp));
			}else if("datefield".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertDatefieldAttr(voC, ctrol, intMaxp));
			}else if("checkboxfield".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertCheckBoxAttr(voC, ctrol, intMaxp));
			}else if("radiofield".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertRadioAttr(voC, ctrol, intMaxp));
			}else if("button".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertButtonAttr(voC, ctrol, intMaxp));
			}else if("combobox".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertComboboxAttr(voC, ctrol, intMaxp));
			}else if("label".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertLabelAttr(voC, ctrol, intMaxp));
			}else if("image".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertImagenAttr(voC, ctrol, intMaxp));
			}else if("hidden".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertHiddenAttr(voC, ctrol, intMaxp));
			}else if("gridpanel".equals(voC.getTipo())){
				lstdeLstCtrolAttr.add(this.creaInsertGridAttr(voC, ctrol, intMaxp));
				String col = "", idG = "";
				Iterator<ArrayList> itG = lstdeLstCtrolAttr.iterator();
				while(itG.hasNext()) {
					ArrayList<DinamicControlAttrVo> ltaGrid = itG.next();
					Iterator<DinamicControlAttrVo> itGC = ltaGrid.iterator();
					while(itGC.hasNext()) {
						DinamicControlAttrVo ctrG = itGC.next();
						if("columns".equals(ctrG.getAttr())){
							col = ctrG.getValor();
						}else if ("id".equals(ctrG.getAttr())){
							idG = ctrG.getValor();
						}
					}
				}
				setListaColumnasGrid(idG,Integer.parseInt(col),intMaxp);
			}
			ctrol++;
		}
	}
	
	private void setListaColumnasGrid(String ctrol, Integer col, Integer nPanel){
		int cons = 0;
		Iterator<DinamicAttrVo> itH = listPaneles.iterator();
		while(itH.hasNext()) {
			DinamicAttrVo voH =  (DinamicAttrVo)itH.next();
			if(voH.getIdGrid().equals(ctrol)){
				cons ++;
				lstdeLstCtrolGridAttr.add(new DinamicControlAttrVo(col,cons,  nPanel, "text", voH.getTexto(), "S"));
				lstdeLstCtrolGridAttr.add(new DinamicControlAttrVo(col,cons,  nPanel, "width", String.valueOf(voH.getWidth()), "N"));
				lstdeLstCtrolGridAttr.add(new DinamicControlAttrVo(col,cons,  nPanel, "tipoG", voH.getTipoG(), "S"));
				lstdeLstCtrolGridAttr.add(new DinamicControlAttrVo(col,cons,  nPanel, "dataIndex", voH.getDataIndex(), "S"));
			}
		}
	}
	
	private ArrayList<DinamicAttrVo> getListaControles(String panel){
		ArrayList<DinamicAttrVo> rgs = new ArrayList<DinamicAttrVo>();
		Iterator<DinamicAttrVo> itC = listPaneles.iterator();
		while(itC.hasNext()) {
			DinamicAttrVo voC = itC.next();
			if(voC.getIdPadre().equals(panel) && voC.getIdGrid().equals("")){
				rgs.add(voC);
			}
		}
		return rgs;
	}
	

	private ArrayList<DinamicPanelAttrVo> creaInsertPanel(DinamicAttrVo vo){
		ArrayList<DinamicPanelAttrVo> rgs = new ArrayList<DinamicPanelAttrVo>();
		int consec = 1;
		//logger.debug("creaInsertPanel() this.consec=" + this.consec);
		//logger.debug("creaInsertPanel() consec=" + consec);
		if(!vo.getIdHijo().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "id", vo.getIdHijo(), "S"));consec++;}
		if(!vo.getTitulo().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "title", vo.getTitulo(), "S"));consec++;}
		if(vo.getTipo().equals("form")){
			if(!vo.getTipo().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", vo.getTipo(), "S"));consec++;}
		}else if(vo.getTipo().equals("border")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "border", "S"));consec++;
		}else if(vo.getTipo().equals("sur")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "region", "south", "S"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
		}else if(vo.getTipo().equals("norte")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "region", "north", "S"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
		}else if(vo.getTipo().equals("izq")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "region", "west", "S"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
		}else if(vo.getTipo().equals("der")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "region", "east", "S"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
		}else if(vo.getTipo().equals("centro")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "region", "center", "S"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
		}else if(vo.getTipo().equals("form_columns")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "form", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
		}else if(vo.getTipo().equals("form_tabs")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "tabpanel", "S"));consec++;
		}else if(vo.getTipo().equals("window")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "window", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "column", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "modal", vo.getIsModal(), "B"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "minimizable", vo.getIsMinimizable(), "B"));consec++;
			if(vo.getColumnas() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "columns", String.valueOf(vo.getColumnas()), "N"));consec++;}
			if(vo.getCordX() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "x", String.valueOf(vo.getCordX()), "N"));consec++;}
			if(vo.getCordY() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "y", String.valueOf(vo.getCordY()), "N"));consec++;}
			if(!vo.getTitulo_Posicion().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "headerPosition", vo.getTitulo_Posicion(), "S"));consec++;}
		}else if(vo.getTipo().equals("form_acordion")){
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "xtype", "panel", "S"));consec++;
			rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "layout", "accordion", "S"));consec++;
		}
		if(!vo.getName().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "name", vo.getName(), "S"));consec++;}
		if(vo.getHeight() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "height", String.valueOf(vo.getHeight()), "N"));consec++;}
		if(vo.getWidth() > 0){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "width", String.valueOf(vo.getWidth()), "N"));consec++;}
		rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "collapsible", vo.getIsDesplegable(), "B"));consec++;
		if(!vo.getMargen().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "margin", vo.getMargen(), "S"));consec++;}
		if(!vo.getUrl().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "url", vo.getUrl(), "S"));consec++;}
		rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "frame", vo.getIsFondo(), "B"));consec++;
		rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "closable", vo.getIsCerrable(), "B"));consec++;
		if(!vo.getBodyPadding().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "bodyPadding", vo.getBodyPadding(), "S"));consec++;}
		rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "autoScroll", vo.getIsAutoScroll(), "B"));consec++;
		rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "resizable", vo.getIsResizable(), "B"));consec++;
		rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "bodyBorder", vo.getIsBodyBorder(), "B"));consec++;
		if(!vo.getTitulo_Aling().equals("")){rgs.add(new DinamicPanelAttrVo(consec, intMaxp, "titleAlign", vo.getTitulo_Aling(), "S"));consec++;}
		return rgs;
	}
	
	private ArrayList<DinamicControlAttrVo> creaInsertControlAttrGral(DinamicAttrVo vo, int nControl, int nPanel){
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		//logger.debug("creaInsertControlAttrGral() this.consec=" + this.consec);
		consec = 1;
		//logger.debug("creaInsertControlAttrGral() this.consec aumentado=" + this.consec);
		if(!vo.getIdHijo().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "id", vo.getIdHijo(), "S"));consec++;}
		if(!vo.getName().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "name", vo.getName(), "S"));consec++;}
		if(!vo.getEtiqueta().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "fieldLabel", vo.getEtiqueta(), "S"));consec++;}
		if(!vo.getEtiqueta_aling().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "labelAlign", vo.getEtiqueta_aling(), "S"));consec++;}
		if(!vo.getIsRequeridoMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "blankText", vo.getIsRequeridoMsg(), "S"));consec++;}
		if(!vo.getTipo().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "xtype", vo.getTipo(), "S"));consec++;}
		if(!vo.getTextoSugerido().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "emptyText", vo.getTextoSugerido(), "S"));consec++;}
		if(!vo.getTextoMaxMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "maxLengthText", vo.getTextoMaxMsg(), "S"));consec++;}
		if(!vo.getTextoMinMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "minLengthText", vo.getTextoMinMsg(), "S"));consec++;}
		if(!vo.getToolTip().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "listeners", "{afterrender: function(){Ext.QuickTips.register({target: this.id,text:''"+vo.getToolTip()+"'',dismissDelay: 2000}) ;}}", "C"));consec++;}
		if(!vo.getMargen().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "margin", vo.getMargen(), "S"));consec++;}
		if(!vo.getPadding().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "padding", vo.getPadding(), "S"));consec++;}
		if(!vo.getIsAnchor().equals("0")){
			rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "anchor", vo.getIsAnchor()+"%", "S"));consec++;}
		
		if(vo.getWidth() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "width", String.valueOf(vo.getWidth()), "N"));consec++;}
		if(vo.getHeight() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "height", String.valueOf(vo.getHeight()), "N"));consec++;}
		if(vo.getEtiqueta_width() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "labelWidth", String.valueOf(vo.getEtiqueta_width()), "N"));consec++;}
		if(vo.getTextoMax() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "maxLength", String.valueOf(vo.getTextoMax()), "N"));consec++;}
		if(vo.getTextoMin() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "minLength", String.valueOf(vo.getTextoMin()), "N"));consec++;}
		return rgs;
		//rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		//rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "allowBlank", vo.getIsRequerido(), "B"));consec++;
		//rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;

	}

	private ArrayList<DinamicControlAttrVo> creaInsertComboboxAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertComboboxAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		if(!vo.getDelimitador().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "delimiter", vo.getDelimitador(), "S"));consec++;}
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "editable", vo.getIsEditable(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "hideTrigger", vo.getIsFlecha(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "allowBlank", vo.getIsRequerido(), "B"));consec++;
		if(!vo.getModo().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "queryMode", vo.getModo(), "S"));consec++;}
		if(!vo.getIsPadre().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "isPadre", vo.getIsPadre(), "S"));consec++;}
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "multiSelect", vo.getMultiSelect(), "B"));consec++;
		if(!vo.getSelectAction().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "triggerAction", vo.getSelectAction(), "S"));consec++;}
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "selectOnFocus", vo.getSelectconFoco(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;
		if(!vo.getStore().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "store", vo.getStore(), "S"));consec++;}
		if(!vo.getTexto().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "value", vo.getTexto(), "S"));consec++;}
		if(!vo.getValorDisplay().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "displayField", vo.getValorDisplay(), "S"));consec++;}
		if(!vo.getValorId().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "valueField", vo.getValorId(), "S"));consec++;}
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "typeAhead", vo.getIsAutoComp(), "B"));consec++;
		
		return rgs;
	}

	private ArrayList<DinamicControlAttrVo> creaInsertButtonAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertButtonAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		if(!vo.getEscala().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "scale", vo.getEscala(), "S"));consec++;}
		if(!vo.getImagenCls().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "iconCls", vo.getImagenCls(), "S"));consec++;}
		if(!vo.getImagen_aling().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "iconAling", vo.getImagen_aling(), "S"));consec++;}
		if(!vo.getTexto().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "text", vo.getTexto(), "S"));consec++;}
		if(!vo.getEstilo().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "estilo", vo.getEstilo(), "S"));consec++;}
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "row", String.valueOf(vo.getRow()), "N"));consec++;
		return rgs;
	}
	
	private ArrayList<DinamicControlAttrVo> creaInsertRadioAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertRadioAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "checked", vo.getIsSeleccionado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;
		if(!vo.getEtiqueta_aling().equals("") && vo.getEtiqueta_aling().equals("right")){
			rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "hideLabel", "true", "B"));consec++;
			rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "boxLabel", vo.getEtiqueta(), "S"));consec++;
		}
		return rgs;
	}

	private ArrayList<DinamicControlAttrVo> creaInsertCheckBoxAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertCheckBoxAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "checked", vo.getIsSeleccionado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;
		if(!vo.getEtiqueta_aling().equals("") && vo.getEtiqueta_aling().equals("right")){
			rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "hideLabel", "true", "B"));consec++;
			rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "boxLabel", vo.getEtiqueta(), "S"));consec++;
		}
		
		return rgs;
		
	}
	private ArrayList<DinamicControlAttrVo> creaInsertDatefieldAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertDatefieldAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "editable", vo.getIsEditable(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "allowBlank", vo.getIsRequerido(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;
		if(!vo.getFecha().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "value", vo.getFecha(), "S"));consec++;}
		if(!vo.getFechaInvalidTxt().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "invalidText", vo.getFechaInvalidTxt(), "S"));consec++;}
		if(!vo.getFechaMax().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "maxValue", vo.getFechaMax(), "S"));consec++;}
		if(!vo.getFechaMaxMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "maxText", vo.getFechaMaxMsg(), "S"));consec++;}
		if(!vo.getFechaMin().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "minValue", vo.getFechaMin(), "S"));consec++;}
		if(!vo.getFechaMinMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "minText", vo.getFechaMinMsg(), "S"));consec++;}
		if(!vo.getIsPadre().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "isPadre", vo.getIsPadre(), "S"));consec++;}
		return rgs;
		
	}
	private ArrayList<DinamicControlAttrVo> creaInsertNumericAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertNumericAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "editable", vo.getIsEditable(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "hideTrigger", vo.getIsFlechas(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "allowBlank", vo.getIsRequerido(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;
		if(!vo.getTexto().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "value", vo.getTexto(), "S"));consec++;}
		if(vo.getValorMax() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "maxValue", String.valueOf(vo.getValorMax()), "N"));consec++;}
		if(!vo.getValorMaxMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "maxText", vo.getValorMaxMsg(), "S"));consec++;}
		if(vo.getValorMin() > 0){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "minValue", String.valueOf(vo.getValorMin()), "N"));consec++;}
		if(!vo.getValorMinMsg().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "minText", vo.getValorMinMsg(), "S"));consec++;}
		return rgs;
		
	}
	private ArrayList<DinamicControlAttrVo> creaInsertTextAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertTextAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "allowBlank", vo.getIsRequerido(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "disabled", vo.getIsBloqueado(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "readOnly", vo.getSoloLectura(), "B"));consec++;
		if(!vo.getIsPadre().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "isPadre", vo.getIsPadre(), "S"));consec++;}
		if(!vo.getTexto().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "value", vo.getTexto(), "S"));consec++;}
		return rgs;
		
	}
	private ArrayList<DinamicControlAttrVo> creaInsertLabelAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertLabelAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		if(!vo.getTexto().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "text", vo.getTexto(), "S"));consec++;}
		if(!vo.getHtml().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "html", vo.getHtml(), "S"));consec++;}
		return rgs;
	}
	private ArrayList<DinamicControlAttrVo> creaInsertImagenAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertImagenAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		if(!vo.getSrc().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "src", vo.getSrc(), "S"));consec++;}
		return rgs;
	}
	private ArrayList<DinamicControlAttrVo> creaInsertHiddenAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertHiddenAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		if(!vo.getTexto().equals("")){rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "value", vo.getTexto(), "S"));consec++;}
		return rgs;
	}
	private ArrayList<DinamicControlAttrVo> creaInsertGridAttr(DinamicAttrVo vo, int nControl, int nPanel){
		//logger.debug("creaInsertGridAttr() this.consec=" + this.consec);
		ArrayList<DinamicControlAttrVo> rgs = new ArrayList<DinamicControlAttrVo>();
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "columns", String.valueOf(consec), "S"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "enableColumnHide", vo.getColumna_hidden(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "enableColumnMove", vo.getColumna_move(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "sortableColumns", vo.getColumna_orden(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "enableColumnResize", vo.getColumna_resize(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec, nControl,  nPanel, "bodyBorder", vo.getIsBodyBorder(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec, nControl,  nPanel, "closable", vo.getIsCerrable(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec, nControl,  nPanel, "collapsible", vo.getIsDesplegable(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec, nControl,  nPanel, "frame", vo.getIsFondo(), "B"));consec++;
		rgs.add(new DinamicControlAttrVo(consec, nControl,  nPanel, "resizable", vo.getIsResizable(), "B"));consec++;
		if(!vo.getTitulo().equals("")){rgs.add(new DinamicControlAttrVo(consec,  nControl,  nPanel, "title", vo.getTitulo(), "S"));consec++;}
		if(!vo.getTitulo_Aling().equals("")){rgs.add(new DinamicControlAttrVo(consec,  nControl,  nPanel, "titleAlign", vo.getTitulo_Aling(), "S"));consec++;}
		if(!vo.getQuery().equals("")){
			rgs.add(new DinamicControlAttrVo(consec,nControl,  nPanel, "query", String.valueOf(consec), "S"));
			String sql = vo.getQuery().replaceAll("'", "''");
			lstdeLstCtrolGridSql.add(new DinamicControlAttrVo(0,consec,  nPanel, "query", sql, "S"));
			consec++;
		}
		return rgs;
	}

	public void setDinamicDAO(DinamicDaoInterface dinamicDAO) {
		this.dinamicDAO = dinamicDAO;
	}
	
}