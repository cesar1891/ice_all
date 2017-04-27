package mx.com.gseguros.portal.cotizacion.model;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public class ObtieneTatripolMapper implements RowMapper
{
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		ComponenteVO result=new ComponenteVO();
		result.setFlagEsAtribu(true);
		result.setType(ComponenteVO.TIPO_TATRIPOL);
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
        
        String cdcondicvis = rs.getString("CDCONDICVIS");
        if(StringUtils.isBlank(cdcondicvis))
        {
        	cdcondicvis="I";
        }
        result.setCdcondicvis(cdcondicvis);
        
        String swpresemi = rs.getString("SWPRESEMI");
        if(StringUtils.isBlank(swpresemi))
        {
        	swpresemi="N";
        }
        result.setSwpresemi(swpresemi);
        
        String swpresemiflot = rs.getString("SWPRESEMIFLOT");
        if(StringUtils.isBlank(swpresemiflot))
        {
        	swpresemiflot="N";
        }
        result.setSwpresemiflot(swpresemiflot);
        
        String swpresen = rs.getString("SWPRESEN");
        if(StringUtils.isBlank(swpresen))
        {
        	swpresen="N";
        }
        result.setSwpresen(swpresen);
		
		return result;
	}
}