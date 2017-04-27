package mx.com.gseguros.confpantallas.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.confpantallas.model.DinamicComboVo;
import mx.com.gseguros.confpantallas.model.DinamicControlAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicControlVo;
import mx.com.gseguros.confpantallas.model.DinamicItemBean;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrGetVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelAttrVo;
import mx.com.gseguros.confpantallas.model.DinamicPanelVo;
import mx.com.gseguros.confpantallas.model.DinamicTatriVo;
import mx.com.gseguros.portal.dao.AbstractManagerDAO;
import mx.com.gseguros.portal.general.dao.PantallasDAO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DinamicDao extends AbstractManagerDAO implements DinamicDaoInterface {
	
	private static Logger logger = Logger.getLogger(DinamicDao.class);
	
	public List<DinamicControlAttrVo> GetAttrGrid(HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query").toString(),mapa);
		List<DinamicControlAttrVo> rgs  = getJdbcTemplate().query(qry,new BeanPropertyRowMapper(DinamicControlAttrVo.class));
		return rgs;
	}
	public List<Map> GetListados (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		List rgs = getJdbcTemplate().queryForList(qry);
		return rgs;
	}
	public List<HashMap> GetListadosHM (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		List rgs = getJdbcTemplate().queryForList(qry);
		return rgs;
	}
	public List<HashMap> GetDataGrid (String qry){
		List rgs = getJdbcTemplate().queryForList(qry);
		return rgs;
	}
	public List<DinamicTatriVo> getDatosControlTatrisit (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		List<DinamicTatriVo> rgs  = getJdbcTemplate().query(qry,new BeanPropertyRowMapper(DinamicTatriVo.class));
		return rgs;
	}
	public List<DinamicPanelAttrGetVo> getDinamicPanelAttrGetVo (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		List<DinamicPanelAttrGetVo> rgs  = getJdbcTemplate().query(qry,new BeanPropertyRowMapper(DinamicPanelAttrGetVo.class));
		return rgs;
	}
	public List<DinamicComboVo> getListaCombox (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"));
		@SuppressWarnings("unchecked")
		List<DinamicComboVo> rgs  = getJdbcTemplate().query(qry,new BeanPropertyRowMapper(DinamicComboVo.class));
		return rgs;
	}
	public List<DinamicComboVo> getDinamicComboVo (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		List<DinamicComboVo> rgs  = getJdbcTemplate().query(qry,new BeanPropertyRowMapper(DinamicComboVo.class));
		return rgs;
	}

	public List<DinamicItemBean> getDinamicItemBean (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		List<DinamicItemBean> rgs  = getJdbcTemplate().query(qry,new BeanPropertyRowMapper(DinamicItemBean.class));
		return rgs;
	}
	public String getString (HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		String rgs = getJdbcTemplate().queryForObject(qry,String.class);
		return rgs;
	}
	public void ejecuta(HashMap<String, String> mapa){
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		System.out.println("qry=" + qry);
		if(StringUtils.isNotBlank(qry)) {
			getJdbcTemplate().update(qry);
		}
	}
	public void ejecuta(String query, DinamicPanelVo objeto){
		String qry = this.getSqlSelect(query,objeto);
		getJdbcTemplate().update(qry);
	}
	public void ejecuta(String query, DinamicPanelAttrVo objeto){
		String qry = this.getSqlSelect(query,objeto);
		getJdbcTemplate().update(qry);
	}
	public void ejecuta(String query, DinamicControlVo objeto){
		String qry = this.getSqlSelect(query,objeto);
		getJdbcTemplate().update(qry);
	}
	public void ejecuta(String query, DinamicControlAttrVo objeto){
		String qry = this.getSqlSelect(query,objeto);
		getJdbcTemplate().update(qry);
	}
	public void ejecutaG(String query, DinamicControlAttrVo objeto){
		String qry = this.getSqlSelectG(query,objeto);
		getJdbcTemplate().update(qry);
	}
	public void ejecutaSql(String query, DinamicControlAttrVo objeto){
		String qry = this.getSqlSelectSql(query,objeto);
		getJdbcTemplate().update(qry);
	}
	public String setCFExtjs (HashMap<String, String> mapa){
		String rgs = "";
		this.ejecuta(mapa);
		rgs = mapa.get("panel");//TODO:es temporal, cambiar logica
		return rgs;
	}
	public String setPanel (HashMap<String, Object> mapa){
		String rgs = "";
		try{
			String panel = mapa.get("panel").toString();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("panel", panel);
			data.put("query", "actualPanel");
			String actualP = "";
			List ltsPaneles = this.GetListados(data);
			Iterator<Map> itlP = ltsPaneles.iterator();
			while (itlP.hasNext()) {
				Map map =  itlP.next();
				actualP = map.get("IDPANEL").toString();
				data.put("idpanel", actualP);
				data.put("query", "deletePanelAttr");
				this.ejecuta(data);
				data.put("query", "deleteActualPanel");
				this.ejecuta(data);
				data.put("query", "deleteControles");
				this.ejecuta(data);
				data.put("query", "deleteControlesAttr");
				this.ejecuta(data);
				data.put("query", "deleteControlesAttrGrid");
				this.ejecuta(data);
				data.put("query", "deleteControlesAttrSql");
				this.ejecuta(data);
				data.put("query", "deleteCodigoExtJS");
				this.ejecuta(data);
			}
			ArrayList<DinamicPanelVo> arryPaneles = (ArrayList<DinamicPanelVo>) mapa.get("newPanel");
			Iterator<DinamicPanelVo> itP = arryPaneles.iterator();
			while(itP.hasNext()) {
				this.ejecuta("insertActualPanel",itP.next());
			}

			ArrayList<ArrayList> llPa = (ArrayList<ArrayList>)mapa.get("listaAttr");
			Iterator<ArrayList> itLLP = llPa.iterator();
			while(itLLP.hasNext()) {
				ArrayList<DinamicPanelAttrVo> lts = itLLP.next();
				Iterator<DinamicPanelAttrVo> itL = lts.iterator();
				while(itL.hasNext()) {
					this.ejecuta("insertActualPanelAttr",itL.next());
				}
			}
		
			ArrayList<DinamicControlVo> arryControles = (ArrayList<DinamicControlVo>) mapa.get("listaControles");
			Iterator<DinamicControlVo> itC = arryControles.iterator();
			while(itC.hasNext()) {
				this.ejecuta("insertActualControl", itC.next());
			}

			ArrayList<ArrayList> llCa = (ArrayList<ArrayList>)mapa.get("ltsListaControlesArrt");
			Iterator<ArrayList> itLL = llCa.iterator();
			while(itLL.hasNext()) {
				ArrayList<DinamicControlAttrVo> lts = itLL.next();
				Iterator<DinamicControlAttrVo> itL = lts.iterator();
				while(itL.hasNext()) {
					this.ejecuta("insertActualControlAttr",itL.next());
				}
			}
			
			ArrayList<DinamicControlAttrVo> arryAttrGrid = (ArrayList<DinamicControlAttrVo>) mapa.get("lstdeLstCtrolGridAttr");
			Iterator<DinamicControlAttrVo> itCG = arryAttrGrid.iterator();
			while(itCG.hasNext()) {
				this.ejecutaG("insertActualControlAttrGrid", itCG.next());
			}

			ArrayList<DinamicControlAttrVo> arryAttrGridSql = (ArrayList<DinamicControlAttrVo>) mapa.get("lstdeLstCtrolGridSql");
			Iterator<DinamicControlAttrVo> itSql = arryAttrGridSql.iterator();
			while(itSql.hasNext()) {
				this.ejecutaSql("insertActualControlAttrGridSql", itSql.next());
			}

		}catch (Exception e){
			rgs = e.toString();
			System.out.println(e.getMessage());
		}
		return rgs;
	}
	private String getSqlSelect (String qry){
		String rgs = "";
		if(qry.equals("listaPanelesExistentes")){
			rgs = "SELECT DISTINCT (NAME_PANEL) key, NAME_PANEL value FROM DNC_PANELES";
		}
		return rgs;
	}
	private String getSqlSelect (String qry, DinamicControlAttrVo mapa){
		String rgs = "";
		if(qry.equals("insertActualControlAttr")){
			rgs = "INSERT INTO DNC_DOCCONTROL_ATTR VALUES ("+mapa.getIdattr()+","+mapa.getIdcontrol()+","+mapa.getIdpanel()+",'"+mapa.getAttr()+"','"+mapa.getValor()+"','"+mapa.getTipo()+"')";
		}
		return rgs;
	}
	private String getSqlSelectG (String qry, DinamicControlAttrVo mapa){
		String rgs = "";
		if(qry.equals("insertActualControlAttrGrid")){
			rgs = "INSERT INTO DNC_DOCCONTROLGRID_ATTR VALUES ("+mapa.getIdattr()+","+mapa.getIdcontrol()+","+mapa.getIdpanel()+",'"+mapa.getAttr()+"','"+mapa.getValor()+"','"+mapa.getTipo()+"')";
		}
		return rgs;
	}
	private String getSqlSelectSql (String qry, DinamicControlAttrVo mapa){
		String rgs = "";
		if(qry.equals("insertActualControlAttrGridSql")){
			rgs = "INSERT INTO DNC_DOCCONTROLGRID_SQL VALUES ("+mapa.getIdcontrol()+","+mapa.getIdpanel()+",'"+mapa.getAttr()+"','"+mapa.getValor()+"','"+mapa.getTipo()+"')";
		}
		return rgs;
	}	
	private String getSqlSelect (String qry, DinamicControlVo mapa){
		String rgs = "";
		if(qry.equals("insertActualControl")){
			rgs = "INSERT INTO DNC_DOCCONTROL VALUES ("+mapa.getIdControl()+","+mapa.getIdPanel()+",'"+mapa.getDescripcion()+"',"+mapa.getOrden()+")";
		}
		return rgs;
	}
	private String getSqlSelect (String qry, DinamicPanelAttrVo mapa){
		String rgs = "";
		if(qry.equals("insertActualPanelAttr")){
			rgs = "INSERT INTO DNC_PANELES_ATTR VALUES ("+mapa.getIdattr()+","+mapa.getIdpanel()+",'"+mapa.getAttr()+"','"+mapa.getValor()+"','"+mapa.getTipo()+"')";
		}
		return rgs;
	}
	private String getSqlSelect (String qry, DinamicPanelVo mapa){
		String rgs = "";
		if(qry.equals("insertActualPanel")){
			rgs = "INSERT INTO DNC_PANELES VALUES ("+mapa.getId()+",'"+mapa.getName()+"',"+mapa.getIdPadre()+","+mapa.getOrden()+")";
		}
		return rgs;
	}
	
	private String getSqlSelect (String qry, HashMap<String, String> mapa){
		
		logger.debug("qry="+ qry);
		logger.debug("mapa="+ mapa);
		
		String rgs = "";
		if(qry.equals("ListadeTablasPredeterminados")){
			rgs = "SELECT NOMBRE, TABLA FROM DNC_TABLAS ORDER BY IDCONTROL";
		}else if(qry.equals("ListadeRamosTatrisit")){
			rgs = "SELECT DISTINCT(CDTIPSIT) AS NOMBRE, CDTIPSIT AS TABLA FROM TATRISIT ORDER BY CDTIPSIT";
		}else if(qry.equals("ListadeControlesGrid")){	
			rgs = "SELECT DISTINCT(IDCONTROL) FROM DNC_DOCCONTROLGRID_ATTR WHERE IDATTR = '"+mapa.get("id")+"' AND IDPANEL = "+mapa.get("panel");	
		}else if(qry.equals("ListadeControlesGridAttr")){	
			rgs = "SELECT IDATTR,IDCONTROL,IDPANEL,ATTR,VALOR,TIPO FROM DNC_DOCCONTROLGRID_ATTR WHERE IDATTR = "+mapa.get("id")+" AND IDCONTROL = "+mapa.get("control")+" AND IDPANEL = "+mapa.get("panel");	
		}else if(qry.equals("ListadeCamposTatrisit")){
			rgs = "SELECT DSATRIBU AS NOMBRE, DSATRIBU AS TABLA FROM TATRISIT WHERE CDTIPSIT = '"+mapa.get("cdramo")+"' ORDER BY CDATRIBU";
		}else if(qry.equals("DatosControlTatrisit")){
			rgs = "SELECT CDTIPSIT CDRAMO, CDATRIBU, DSATRIBU, SWFORMAT, SWOBLIGA, NMLMAX, NMLMIN, OTTABVAL, SWPRODUC, SWSUPLEM, SWTARIFI, SWPRESEN FROM TATRISIT "
					+ "WHERE CDTIPSIT = '"+mapa.get("cdramo")+"' AND DSATRIBU = '"+mapa.get("descripcion")+"'";
		}else if(qry.equals("existePanel")){
			rgs = "SELECT DISTINCT(NAME_PANEL) FROM DNC_PANELES WHERE NAME_PANEL = '"+mapa.get("panel")+"'";
		}else if(qry.equals("maxPanel")){
			rgs = "SELECT MAX(IDPANEL) + 1 FROM DNC_PANELES";
		}else if(qry.equals("maxPadre")){
			rgs = "SELECT MAX(IDPADRE) FROM DNC_PANELES";
		}else if(qry.equals("actualPanel")){
			rgs = "SELECT IDPANEL FROM DNC_PANELES WHERE NAME_PANEL = '"+mapa.get("panel")+"'";
		}else if(qry.equals("deletePanelAttr")){
			rgs = "DELETE DNC_PANELES_ATTR WHERE IDPANEL = "+mapa.get("idpanel");
		}else if(qry.equals("deleteActualPanel")){
			rgs = "DELETE DNC_PANELES WHERE NAME_PANEL = '"+mapa.get("panel")+"'";
		}else if(qry.equals("deleteControles")){
			rgs = "DELETE DNC_DOCCONTROL WHERE IDPANEL = "+mapa.get("idpanel");
		}else if(qry.equals("deleteControlesAttr")){
			rgs = "DELETE DNC_DOCCONTROL_ATTR WHERE IDPANEL = "+mapa.get("idpanel");
		}else if(qry.equals("deleteControlesAttrGrid")){
			rgs = "DELETE DNC_DOCCONTROLGRID_ATTR WHERE IDPANEL = "+mapa.get("idpanel");
		}else if(qry.equals("deleteControlesAttrSql")){
			rgs = "DELETE DNC_DOCCONTROLGRID_SQL WHERE IDPANEL = "+mapa.get("idpanel");
		}else if(qry.equals("deleteCodigoExtJS")){
			rgs = "DELETE TPANTALLAS WHERE CDPANTALLA = "+mapa.get("idpanel");
		}else if(qry.equals("listaValoresTablaApoyo")){
			rgs = "SELECT A.OTCLAVE KEY, A.OTVALOR VALUE FROM TTAPVAT1 A, TTAPDSCL B "
					+ "WHERE A.NMTABLA = (SELECT C.NMTABLA FROM TTAPTABL C WHERE C.CDTABLA = '"+mapa.get("tabla")+"') "
					+ "AND A.NMTABLA = B.NMTABLA";
		}else if(qry.equals("listaValoresTablaApoyoHijos")){
			rgs = "SELECT HIJO.OTCLAVE KEY, HIJO.OTVALOR VALUE "
					+ "FROM TTAPVAT1 HIJO, TTAPTABL B , TTAPVAT1 PADRE, TTAPTABL D, TTAPVAAT REL, TTAPTABL C "
					+ "WHERE B.CDTABLA    = '"+mapa.get("tabla")+"' AND B.NMTABLA    = HIJO.NMTABLA "
					+ "AND B.CDTABLJ1   = D.CDTABLA AND D.NMTABLA    = PADRE.NMTABLA AND C.CDTABLA    = B.CDTABLJ2 "
					+ "AND C.NMTABLA    = REL.NMTABLA AND REL.OTCLAVE1 = PADRE.OTCLAVE     AND REL.OTCLAVE2 = HIJO.OTCLAVE "
					+ "AND REL.OTCLAVE1 = '"+mapa.get("valor")+"' GROUP BY HIJO.OTCLAVE, HIJO.OTVALOR "
					+ "ORDER BY VALUE DESC";
		}else if(qry.equals("IdPanel")){
			rgs = "SELECT IDPANEL, IDPADRE FROM DNC_PANELES WHERE NAME_PANEL = '"+mapa.get("panel")+"' ORDER BY ORDEN";
		}else if(qry.equals("AttrPanel")){
			rgs = "SELECT SATTR AS attr, SVALOR as valor, TIPO as tipo FROM DNC_PANELES_ATTR WHERE IDPANEL = "+mapa.get("id");
		}else if(qry.equals("ListaIdControles")){
			rgs = "SELECT IDCONTROL, DESCRIPCION FROM DNC_DOCCONTROL WHERE IDPANEL = "+mapa.get("panelID")+" ORDER BY ORDEN";
		}else if(qry.equals("AttrControl")){
			rgs = "SELECT ATTR AS attr, VALOR as valor, TIPO as tipo FROM DNC_DOCCONTROL_ATTR WHERE IDCONTROL = "+mapa.get("idControl")+" AND IDPANEL = "+mapa.get("idPanel");
		}else if(qry.equals("getTipoBoton")){
			rgs = "SELECT VALOR FROM DNC_DOCCONTROL_ATTR WHERE IDCONTROL = "+mapa.get("controlID")+" AND ATTR = 'row' AND IDPANEL = "+mapa.get("panelID");
		}else if(qry.equals("nameTablaApoyoData")){
			rgs = "SELECT A.OTCLAVE KEY, A.OTVALOR VALUE FROM TTAPVAT1 A, TTAPDSCL B "
					+ "WHERE A.NMTABLA = (SELECT C.NMTABLA FROM TTAPTABL C "
					+ "WHERE C.CDTABLA = '"+mapa.get("store")+"') AND A.NMTABLA = B.NMTABLA";
		}else if(qry.equals("SqlQuery")){
			rgs = "SELECT VALOR FROM DNC_DOCCONTROLGRID_SQL WHERE IDCONTROL = "+mapa.get("valor")+" AND IDPANEL = " +mapa.get("tabla");
		}else if(qry.equals("setCFExtjs")){
			//rgs = "INSERT INTO TPANTALLAS (CDPANTALLA,DATOS,COMPONENTES) VALUES ("+mapa.get("panel")+",'"+mapa.get("stores")+"','"+mapa.get("codigo")+"')";
			
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
			PantallasDAO pantallasDAOImpl = (PantallasDAO)context.getBean("pantallasDAOImpl");
			try {
				logger.debug("INICIO DE INSERCION DE CODIGO DE LA PANTALLA");
				pantallasDAOImpl.insertaPantalla(mapa.get("panel"), mapa.get("codigo"), mapa.get("stores"));
				logger.debug("FIN DE INSERCION DE CODIGO DE LA PANTALLA");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return rgs;
	}
	
	@Override
	public List<DinamicPanelVo> getPanelVORowMapper(String panel){
		 return getJdbcTemplate().query("SELECT IDPANEL, IDPADRE FROM DNC_PANELES WHERE NAME_PANEL = '" + panel + "' ORDER BY ORDEN", new RowMapper<DinamicPanelVo>() {
		    @Override  
		    public DinamicPanelVo mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	DinamicPanelVo e= new DinamicPanelVo();
		    	e.setId(rs.getInt(1));
		    	e.setIdPadre(rs.getInt(2));
		        return e;  
		    }  
		});  
	}
	
	@Override
	public List<DinamicPanelAttrVo> getPanelAttrVORowMapper(String noPanel){
		return getJdbcTemplate().query("SELECT SATTR, SVALOR, TIPO FROM DNC_PANELES_ATTR WHERE IDPANEL = " + noPanel, new RowMapper<DinamicPanelAttrVo>(){
			@Override
			public DinamicPanelAttrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DinamicPanelAttrVo e = new DinamicPanelAttrVo();
				e.setAttr(rs.getString(1));
				e.setValor(rs.getString(2));
				e.setTipo(rs.getString(3));
				return e;
			}
			
		});
	}

	@Override
	public List<DinamicControlVo> getControlVORowMapper(String noPanel){
		return getJdbcTemplate().query("SELECT IDCONTROL, DESCRIPCION FROM DNC_DOCCONTROL WHERE IDPANEL = " + noPanel + " ORDER BY ORDEN", new RowMapper<DinamicControlVo>(){
			@Override
			public DinamicControlVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DinamicControlVo e = new DinamicControlVo();
				e.setIdControl(rs.getInt(1));
				e.setDescripcion(rs.getString(2));
				return e;
			}
			
		});
	}

	@Override
	public List<DinamicControlAttrVo> getControlAttrVORowMapper(String noPanel, Integer noControl){
		return getJdbcTemplate().query("SELECT ATTR, VALOR, TIPO FROM DNC_DOCCONTROL_ATTR WHERE IDCONTROL = "+noControl+" AND IDPANEL = " + noPanel, new RowMapper<DinamicControlAttrVo>(){
			@Override
			public DinamicControlAttrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DinamicControlAttrVo e = new DinamicControlAttrVo();
				e.setAttr(rs.getString(1));
				e.setValor(rs.getString(2));
				e.setTipo(rs.getString(3));
				return e;
			}
		});
	}
	
	@Override
	public List<DinamicPanelAttrVo> getColumnasAttrVORowMapper(String namePanel){
		return getJdbcTemplate().query("SELECT IDCONTROL, ATTR, VALOR FROM DNC_DOCCONTROLGRID_ATTR WHERE IDPANEL IN ("
				+ "SELECT IDPANEL FROM DNC_PANELES WHERE NAME_PANEL = '"+namePanel+"') ORDER BY IDCONTROL ", new RowMapper<DinamicPanelAttrVo>(){
			@Override
			public DinamicPanelAttrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DinamicPanelAttrVo e = new DinamicPanelAttrVo();
				e.setIdattr(rs.getInt(1));
				e.setAttr(rs.getString(2));
				e.setValor(rs.getString(3));
				return e;
			}
		});
	}
	
	@Override
	public DinamicPanelVo mapRow(ResultSet rs, int rownumber)
			throws SQLException {
		return null;
	}
	

	
}