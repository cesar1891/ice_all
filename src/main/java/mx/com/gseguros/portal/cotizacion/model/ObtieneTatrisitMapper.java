package mx.com.gseguros.portal.cotizacion.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.com.gseguros.portal.general.model.ComponenteVO;
import mx.com.gseguros.utils.Constantes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class ObtieneTatrisitMapper implements RowMapper
{
	private static final Logger logger = Logger.getLogger(ObtieneTatrisitMapper.class);

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		ComponenteVO result = new ComponenteVO();
		
		String[] cols=new String[]{
				"CDATRIBU"
				,"SWFORMAT"
				,"NMLMIN"
				,"NMLMAX"
				,"SWOBLIGA"
				,"DSATRIBU"
				,"OTTABVAL"
				,"CDTABLJ1"
				,"SWSUSCRI"
				,"SWTARIFI"
				,"SWPRESEN"
				,"VALOR"
				,"OTVALOR01"
				,"SWACTUAL"
				,"SWGRUPO"
				,"SWGRUPOLINEA"
				,"SWGRUPOFACT"
				,"SWGRUPOEXTR"
				,"NMPANELCOTI"
				,"NMPANELFLOT"
				,"SWCOLFLOT"
				,"COLFLOTROL"
				,"SWPRESENFLOT"
				,"SWCOMPFLOT"
				,"NMORDENFLOT"
				,"SWOBLIGAFLOT"
				,"SWOBLIGAEMIFLOT"
		};
		for(String col:cols)
		{
			try
			{
				rs.getString(col);
			}
			catch(Exception ex)
			{
				throw new SQLException("No se encuentra la columna "+col);
			}
		}
		
		result.setFlagEsAtribu(true);
		result.setType(ComponenteVO.TIPO_TATRISIT);
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
		if (StringUtils.isNotBlank(sDepend)) {
			isDepend = true;
		}
		result.setDependiente(isDepend);

		result.setSwsuscri(rs.getString("SWSUSCRI"));
		result.setSwtarifi(rs.getString("SWTARIFI"));
		result.setSwpresen(rs.getString("SWPRESEN"));
		result.setDefaultValue(rs.getString("VALOR"));
		result.setValue(rs.getString("OTVALOR01"));
		result.setSoloLectura(!(StringUtils.isNotBlank(rs.getString("SWACTUAL")) && rs
				.getString("SWACTUAL").equalsIgnoreCase("S")));
		
		String swGrupo = rs.getString("SWGRUPO");
		if(StringUtils.isBlank(swGrupo))
		{
			swGrupo="N";
		}
		result.setSwGrupo(swGrupo);
		
		String swGrupoLinea = rs.getString("SWGRUPOLINEA");
		if(StringUtils.isBlank(swGrupoLinea))
		{
			swGrupoLinea="N";
		}
		result.setSwGrupoLinea(swGrupoLinea);
		
		String swGrupoFact = rs.getString("SWGRUPOFACT");
		if(StringUtils.isBlank(swGrupoFact))
		{
			swGrupoLinea="N";
		}
		result.setSwGrupoFact(swGrupoFact);
		
		String swGrupoExtr = rs.getString("SWGRUPOEXTR");
		if(StringUtils.isBlank(swGrupoExtr))
		{
			swGrupoExtr="N";
		}
		result.setSwGrupoExtr(swGrupoExtr);
		
		String nmpanelcoti = rs.getString("NMPANELCOTI");
		if(StringUtils.isBlank(nmpanelcoti))
		{
			nmpanelcoti = "1";
		}
		result.setNmpanelcoti(nmpanelcoti);
		
		String nmpanelflot = rs.getString("NMPANELFLOT");
		if(StringUtils.isBlank(nmpanelflot))
		{
			nmpanelflot = "";
		}
		result.setNmpanelflot(nmpanelflot);
		
		String swcolumna = rs.getString("SWCOLFLOT");
		if(StringUtils.isBlank(swcolumna))
		{
			swcolumna = "N";
		}
		result.setColumna(swcolumna);
		
		String colflotrol = rs.getString("COLFLOTROL");
		if(StringUtils.isBlank(colflotrol))
		{
			colflotrol = "*";
		}
		result.setCotflotrol(colflotrol);

		String swpresenflot = rs.getString("SWPRESENFLOT");
		if(StringUtils.isBlank(swpresenflot))
		{
			swpresenflot="";
		}
		result.setSwpresenflot(swpresenflot);
		
		String swCompFlot = rs.getString("SWCOMPFLOT");
		if(StringUtils.isBlank(swCompFlot))
		{
			swCompFlot="N";
		}
		result.setSwCompFlot(swCompFlot);
		
		String sNmordenFlot = rs.getString("NMORDENFLOT");
		int nmordenFlot     = 0;
		if(StringUtils.isNotBlank(sNmordenFlot))
		{
			nmordenFlot=Integer.parseInt(sNmordenFlot);
		}
		result.setNmordenFlot(nmordenFlot);
		
		String sObligaFlot = rs.getString("SWOBLIGAFLOT");
		boolean isObligaFlot = false;
		if (StringUtils.isNotBlank(sObligaFlot)
				&& sObligaFlot.equalsIgnoreCase(Constantes.SI))
		{
			isObligaFlot = true;
		}
		result.setObligatorioFlot(isObligaFlot);
		
		String sObligaEmiFlot = rs.getString("SWOBLIGAEMIFLOT");
		boolean isObligaEmiFlot = false;
		if (StringUtils.isNotBlank(sObligaEmiFlot)
				&& sObligaEmiFlot.equalsIgnoreCase(Constantes.SI))
		{
			isObligaEmiFlot = true;
		}
		result.setObligatorioEmiFlot(isObligaEmiFlot);

		return result;
	}
}