package mx.com.gseguros.portal.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.gseguros.utils.Utils;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class GenericMapper implements RowMapper 
{
	
	private String[] columnas;
	private List<String> columnas2;
	private final static Logger logger = Logger.getLogger(GenericMapper.class);
	private boolean fechaConHora;
	
    public GenericMapper(String[] columnas)
    {
    	this.columnas=columnas;
    }
	
    public GenericMapper(String[] columnas, boolean fechaConHora)
    {
    	this.columnas     = columnas;
    	this.fechaConHora = fechaConHora;
    }
    
    public GenericMapper(List<String> columnas2)
    {
    	this.columnas2=columnas2;
    }
    
    public GenericMapper(List<String> columnas2, boolean fechaConHora)
    {
    	this.columnas2    = columnas2;
    	this.fechaConHora = fechaConHora;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
    	if(rowNum==0)
    	{
    		if(columnas!=null)
    		{
    			for(String col:columnas)
    			{
    				boolean found=false;
    				for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
    				{
    					if(col.equalsIgnoreCase(rs.getMetaData().getColumnName(i)))
    					{
    						found=true;
    						break;
    					}
    				}
    				if(!found)
    				{
    					throw new SQLException(Utils.join("No se encuentra la columna ",col));
    				}
    			}
    		}
    		else if(columnas2!=null)
    		{
    			for(String col:columnas2)
    			{
    				boolean found=false;
    				for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
    				{
    					if(col.equalsIgnoreCase(rs.getMetaData().getColumnName(i)))
    					{
    						found=true;
    						break;
    					}
    				}
    				if(!found)
    				{
    					throw new SQLException(Utils.join("No se encuentra la columna ",col));
    				}
    			}
    		}
    	}
		//Map<String,String> map=new HashMap<String,String>(0);
    	LinkedHashMap<String,String> map=new LinkedHashMap<String,String>(0);
		if(columnas!=null)
		{
			for(String col:columnas)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe")||col!=null&&col.substring(0,2).equalsIgnoreCase("ff"))
				{
					if(fechaConHora)
					{
						map.put(col,Utils.formateaFechaConHora(rs.getString(col)));
					}
					else
					{
						map.put(col,Utils.formateaFecha(rs.getString(col)));
					}
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}
		}
		else if(columnas2!=null)
		{
			for(String col:columnas2)
			{
				if(col!=null&&col.substring(0,2).equalsIgnoreCase("fe")||col!=null&&col.substring(0,2).equalsIgnoreCase("ff"))
				{
					if(fechaConHora)
					{
						map.put(col,Utils.formateaFechaConHora(rs.getString(col)));
					}
					else
					{
						map.put(col,Utils.formateaFecha(rs.getString(col)));
					}
				}
				else
				{
					map.put(col,rs.getString(col));
				}
			}
		}
		else
		{
			logger.error("No hay columnas para el mapper");
		}
		return map;
	}
    
}