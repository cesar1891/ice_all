package mx.com.aon.portal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class ObtieneTatriperMapper implements RowMapper
{
	private final Logger logger=Logger.getLogger(ObtieneTatriperMapper.class);
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ComponenteVO result=new ComponenteVO();
		result.setFlagEsAtribu(true);
		result.setType(ComponenteVO.TIPO_TATRIPER);
		result.setNameCdatribu(rs.getString("CDATRIBU"));
		result.setTipoCampo(rs.getString("SWFORMAT"));
		
		String sMinlen = rs.getString("NMLMIN");
        int minlen = -1;
        boolean fMinlen = false;
        if(StringUtils.isNotBlank(sMinlen))
        {
        	try
        	{
        		minlen=(Integer)Integer.parseInt(sMinlen);
        		fMinlen = true;
        	}
        	catch(Exception ex)
        	{
        		minlen=-1;
        		fMinlen=false;
        	}
        }
        result.setMinLength(minlen);
        result.setFlagMinLength(fMinlen);
        
        String sMaxlen = rs.getString("NMLMAX");
        int maxlen = -1;
        boolean fMaxlen = false;
        if(StringUtils.isNotBlank(sMaxlen))
        {
        	try
        	{
        		maxlen=(Integer)Integer.parseInt(sMaxlen);
        		fMaxlen = true;
        	}
        	catch(Exception ex)
        	{
        		maxlen=-1;
        		fMaxlen=false;
        	}
        }
        result.setMaxLength(maxlen);
        result.setFlagMaxLength(fMaxlen);
		
        String sObliga = rs.getString("SWOBLIGA");
        boolean isObliga = false;
        if(StringUtils.isNotBlank(sObliga)&&sObliga.equalsIgnoreCase(Constantes.SI))
        {
        	isObliga = true;
        }
        result.setObligatorio(isObliga);
		
		result.setLabel(rs.getString("DSATRIBU"));
		result.setCatalogo(rs.getString("OTTABVAL"));
		
		String sDepend = rs.getString("CDTABLJ1");
        boolean isDepend = false;
        if(StringUtils.isNotBlank(sDepend))
        {
        	isDepend = true;
        }
        result.setDependiente(isDepend);
        
        result.setCodidocu(rs.getString("codigo"));

        try{
        	if("DOC".equalsIgnoreCase(rs.getString("DATDOC"))){
        		result.setTieneDocu(rs.getString("DATDOC"));
    		}
        	result.setIcon(rs.getString("CARGADO"));
        }catch(Exception esql){
        	logger.debug("NO se encontro DATDOC");
        }
        
        logger.debug("CDATRIBU "+rs.getString("CDATRIBU")+" DSATRIBU: "+rs.getString("DSATRIBU")+" CODIDOCU: "+rs.getString("codigo"));
		
		return result;
	}
}