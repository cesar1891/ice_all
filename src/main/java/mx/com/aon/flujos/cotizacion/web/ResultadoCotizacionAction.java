/**
 * 
 */
package mx.com.aon.flujos.cotizacion.web;

/**
 * 
 * Clase Action para el control y visualizacion de datos de la pantalla de resultados del proceso de cotizaci&oacute;n
 * 
 * @author aurora.lozada
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import mx.com.aon.configurador.pantallas.model.components.ColumnGridVO;
import mx.com.aon.configurador.pantallas.model.components.GridVO;
import mx.com.aon.configurador.pantallas.model.components.ItemVO;
import mx.com.aon.configurador.pantallas.model.components.RecordVO;
import mx.com.aon.core.web.PrincipalCoreAction;
import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;
import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

public class ResultadoCotizacionAction extends PrincipalCoreAction {
	
	private static final long serialVersionUID = -5578184550261043277L;
	
	public static final String DATOS_GRID = "DATOS_GRID";

    private boolean success;
    
    private JSONArray dataResult = new JSONArray();

    /**
     * Metodo que obtiene datos resultado para el grid de la pantalla de resultado del proceso de Cotizacion
     * 
     * @return Cadena SUCCESS
     */
    @SuppressWarnings("unchecked")
    public String obtenerResultado() throws Exception {

        boolean isDebugueable = logger.isDebugEnabled();
        if (isDebugueable) {
            logger.debug("######################################################");
            logger.debug("### Llegando a metodo obtenerResultado en ResultadoCotizacionAction...");
            logger.debug("######################################################");
        }

        logger.debug("### Llenando lista del grid... ");

        List<RecordVO> rVo = new ArrayList<RecordVO>();
        List<ColumnGridVO> cVo = new ArrayList<ColumnGridVO>();
        List<ItemVO> aseguradoras = new ArrayList<ItemVO>();
        List<ItemVO> pagos = new ArrayList<ItemVO>();
        List<ResultadoCotizacionVO> resultadosDatos = new ArrayList<ResultadoCotizacionVO>();
        List<DynaBean> registrosTransformados = new ArrayList<DynaBean>();

        GridVO gResultado = new GridVO();

        gResultado = (GridVO) session.get(DATOS_GRID);

        if (gResultado == null || gResultado.equals("")) {
            logger.debug("resultadosDatos null");
        } else {
            rVo = gResultado.getRecordList();
            cVo = gResultado.getColumnList();
            aseguradoras = gResultado.getItemListAseguradora();
            pagos = gResultado.getItemListPago();
            resultadosDatos = gResultado.getResultList();
            logger.debug("gResultado-----" + gResultado);
            logger.debug("RecordVO-----" + rVo.size());
            logger.debug("ColumnGridVO-----" + cVo.size());
            logger.debug("resultadosDatos-----" + resultadosDatos.size());
            
            //////Atributos estaticos///////////////////////////
            DynaProperty[] properties = new DynaProperty[18 + rVo.size()];
            properties[0] = new DynaProperty("cdIdentifica", String.class);
            properties[1] = new DynaProperty("cdUnieco", String.class);
            properties[2] = new DynaProperty("cdRamo", String.class);
            properties[3] = new DynaProperty("estado", String.class);
            properties[4] = new DynaProperty("nmPoliza", String.class);
            properties[5] = new DynaProperty("nmSuplem", String.class);
            properties[6] = new DynaProperty("status", String.class);
            properties[7] = new DynaProperty("cdCiaaseg", String.class);
            properties[8] = new DynaProperty("dsUnieco", String.class);
            properties[9] = new DynaProperty("cdPerpag", String.class);
            properties[10] = new DynaProperty("dsPerpag", String.class);
            properties[11] = new DynaProperty("cdTipsit", String.class);
            properties[12] = new DynaProperty("dsTipsit", String.class);
            properties[13] = new DynaProperty("cdPlan", String.class);
            properties[14] = new DynaProperty("dsPlan", String.class);
            properties[15] = new DynaProperty("feEmisio", String.class);
            properties[16] = new DynaProperty("feVencim", String.class);
            properties[17] = new DynaProperty("numeroSituacion", String.class);
            
            
            //////Atributos dinamicos conforme a los planes//////////////////////////
            int m = 18;
            logger.debug("####m before---" + m);
            for (RecordVO pl : rVo) {
                properties[m] = new DynaProperty(pl.getName(), String.class);
                m++;
            }// for pagos

            logger.debug("####properties size final---" + properties.length);

            DynaClass resultDynaClass = new BasicDynaClass("result", null, properties);

            Integer identifica = 1;

            for (ItemVO aseg : aseguradoras) {
                logger.debug("####aseguradora---" + aseg.getDescripcion());
                for (ItemVO perp : pagos) {
                    logger.debug("####pago--" + perp.getDescripcion());

                    DynaBean r = resultDynaClass.newInstance();
                   
                    r.set("cdIdentifica", identifica.toString());
                    r.set("cdCiaaseg", aseg.getClave());
                    r.set("dsUnieco", aseg.getDescripcion());
                    r.set("cdPerpag", perp.getClave());
                    r.set("dsPerpag", perp.getDescripcion());
                    
                    identifica++;

                    registrosTransformados.add(r);
               
                }// for pagos
           }// for aseguradoras

        
            for (int g = 0; g < registrosTransformados.size(); g++) {
                logger.debug("####for registro numero--" + g);

                for (ResultadoCotizacionVO rDatos : resultadosDatos) {
                    logger.debug("####for resultado--" + rDatos.getCdIdentifica());
                
                   
                    if (rDatos.getCdCiaaseg().equals(registrosTransformados.get(g).get("cdCiaaseg"))
                            && rDatos.getCdPerpag().equals(registrosTransformados.get(g).get("cdPerpag"))) {
                        logger.debug("####Entr\u00F3 a una aseguradora y tipo pago iguales--" + registrosTransformados.get(g).get("cdIdentifica"));

                        registrosTransformados.get(g).set("cdUnieco", rDatos.getCdUnieco());
                        registrosTransformados.get(g).set("cdRamo", rDatos.getCdRamo());
                        registrosTransformados.get(g).set("estado", rDatos.getEstado());
                        registrosTransformados.get(g).set("nmPoliza", rDatos.getNmPoliza());
                        registrosTransformados.get(g).set("nmSuplem", rDatos.getNmSuplem());
                        registrosTransformados.get(g).set("status", rDatos.getStatus());
                        registrosTransformados.get(g).set("cdTipsit", rDatos.getCdTipsit());
                        registrosTransformados.get(g).set("dsTipsit", rDatos.getDsTipsit());
                        registrosTransformados.get(g).set("cdPlan", rDatos.getCdPlan());
                        registrosTransformados.get(g).set("dsPlan", rDatos.getDsPlan());
                        registrosTransformados.get(g).set("feEmisio", rDatos.getFeEmisio());
                        registrosTransformados.get(g).set("feVencim", rDatos.getFeVencim());
                        registrosTransformados.get(g).set("numeroSituacion", rDatos.getNumeroSituacion());
                        
                        ///Quitar espacios a descripcion del plan/////////////////////
                        String sPlanSinBlancos = "";
                        StringTokenizer stTexto = new StringTokenizer(rDatos.getDsPlan());
                        while (stTexto.hasMoreElements())
                            sPlanSinBlancos += stTexto.nextElement();
                        logger.debug("####dsplan sin espacios--" + sPlanSinBlancos + "--");
                        logger.debug("####rDatos.getMnPrima()        --" + rDatos.getMnPrima());
                        logger.debug("####rDatos.getCdPlan()         --" + rDatos.getCdPlan());
                        logger.debug("####rDatos.getDsPlan()         --" + rDatos.getDsPlan());
                        logger.debug("####rDatos.getNumeroSituacion()--" + rDatos.getNumeroSituacion());
                        logger.debug("####getDynaClass().getDynaProperty--" + 
                                registrosTransformados.get(g).getDynaClass().getDynaProperty(sPlanSinBlancos));

                        if (registrosTransformados.get(g).getDynaClass().getDynaProperty(sPlanSinBlancos)
                                != null) {
                            logger.debug("####llenado de valores a datos dinamicos---");
                            //jtezva sumador de cotizaciones
                            if(registrosTransformados.get(g).get(sPlanSinBlancos)!=null
                                    &&((String)registrosTransformados.get(g).get(sPlanSinBlancos)).length()>0)
                            {
                                //Ya exist&iacute;a un valor aqu&iacute;
                                Double prima=Double.parseDouble((String)registrosTransformados.get(g).get(sPlanSinBlancos));
                                prima+=Double.parseDouble(rDatos.getMnPrima().replace(",", ""));
                                registrosTransformados.get(g).set(sPlanSinBlancos, prima.toString());
                                logger.debug("####valor de prima sumado: "+prima.toString());
                            }
                            else
                            {
                                registrosTransformados.get(g).set(sPlanSinBlancos, rDatos.getMnPrima().replace(",", ""));
                                logger.debug("####primer valor de prima: "+rDatos.getMnPrima().replace(",", ""));
                            }
                            //!jtezva sumador de cotizaciones
                            //registrosTransformados.get(g).set(sPlanSinBlancos, rDatos.getMnPrima().replace(",", ""));
                            registrosTransformados.get(g).set("CD" + sPlanSinBlancos, rDatos.getCdPlan());
                            registrosTransformados.get(g).set("DS" + sPlanSinBlancos, rDatos.getDsPlan());
                            registrosTransformados.get(g).set("NM" + sPlanSinBlancos, rDatos.getNumeroSituacion());
                        }
                      
                    }//if
                
                    
                }// for result
                dataResult.add(registrosTransformados.get(g));
            }// for nuevos registros
            
        
        }// else
       
        logger.debug("####dataResult--" + dataResult);
        success = true;
        return SUCCESS;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public JSONArray getDataResult() {
		return dataResult;
	}

	public void setDataResult(JSONArray dataResult) {
		this.dataResult = dataResult;
	}
        
}
