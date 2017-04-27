package mx.com.gseguros.cotizacionautos.base.dao;

import java.util.HashMap;

import mx.com.gseguros.confpantallas.bd.ConnectDB;

import org.springframework.jdbc.core.JdbcTemplate;

public class ComparaDao {
	private ConnectDB conn=null;
	
	public String getString (HashMap<String, String> mapa){
		conn = new ConnectDB();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(conn.getDataSource());
		String qry = this.getSqlSelect(mapa.get("query"),mapa);
		String rgs = jdbcTemplate.queryForObject(qry,String.class);
		return rgs;
	}
	
	private String getSqlSelect (String qry, HashMap<String, String> mapa){
		String rgs = "";
		if(qry.equals("ExisteCotizacion")){
			rgs = "SELECT COUNT(NMPOLIZA) FROM MPOLISIT WHERE CDUNIECO = 1 AND CDRAMO = 720 AND ESTADO = 'W' AND NMPOLIZA = " + mapa.get("nmpoliza");
		}
		return rgs;
	}
}
