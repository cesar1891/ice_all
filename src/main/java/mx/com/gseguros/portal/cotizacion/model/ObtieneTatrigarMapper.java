package mx.com.gseguros.portal.cotizacion.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public class ObtieneTatrigarMapper implements RowMapper
{
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		ComponenteVO result = new ComponenteVO();
		result.setFlagEsAtribu(true);
		result.setType(ComponenteVO.TIPO_TATRIGAR);
		result.setNameCdatribu(rs.getString("CDATRIBU"));
		result.setTipoCampo(rs.getString("SWFORMAT"));

		String sMinlen = rs.getString("NMLMIN");
		int minlen = -1;
		boolean fMinlen = false;
		if (StringUtils.isNotBlank(sMinlen))
		{
			try
			{
				minlen = (Integer) Integer.parseInt(sMinlen);
				fMinlen = true;
			}
			catch (Exception ex)
			{
				minlen = -1;
				fMinlen = false;
			}
		}
		result.setMinLength(minlen);
		result.setFlagMinLength(fMinlen);

		String sMaxlen = rs.getString("NMLMAX");
		int maxlen = -1;
		boolean fMaxlen = false;
		if (StringUtils.isNotBlank(sMaxlen))
		{
			try
			{
				maxlen = (Integer) Integer.parseInt(sMaxlen);
				fMaxlen = true;
			}
			catch (Exception ex)
			{
				maxlen = -1;
				fMaxlen = false;
			}
		}
		result.setMaxLength(maxlen);
		result.setFlagMaxLength(fMaxlen);

		String sObliga = rs.getString("SWOBLIGA");
		boolean isObliga = false;
		if (StringUtils.isNotBlank(sObliga)
				&& sObliga.equalsIgnoreCase(Constantes.SI))
		{
			isObliga = true;
		}
		result.setObligatorio(isObliga);

		result.setLabel(rs.getString("DSATRIBU"));
		result.setCatalogo(rs.getString("OTTABVAL"));

		String sDepend = rs.getString("CDTABLJ1");
		boolean isDepend = false;
		if (StringUtils.isNotBlank(sDepend))
		{
			isDepend = true;
		}
		result.setDependiente(isDepend);

		String valor = rs.getString("VALOR");
		if(StringUtils.isNotBlank(valor))
		{
			if(StringUtils.isNotBlank(result.getCatalogo()))
			{
				result.setValue(Utils.join("'"+valor+"'"));
			}
			else
			{
				result.setValue(valor);
			}
		}

		String minimo = rs.getString("MINIMO");
		if(StringUtils.isNotBlank(minimo))
		{
			result.setMinValue(minimo);
		}
		
		String maximo = rs.getString("MAXIMO");
		if (StringUtils.isNotBlank(maximo))
		{
			result.setMaxValue(maximo);
		}

		return result;
	}
}