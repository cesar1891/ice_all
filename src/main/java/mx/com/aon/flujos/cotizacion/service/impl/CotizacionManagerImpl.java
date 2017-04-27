package mx.com.aon.flujos.cotizacion.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.aon.configurador.pantallas.model.components.ColumnGridVO;
import mx.com.aon.configurador.pantallas.model.components.GridVO;
import mx.com.aon.configurador.pantallas.model.components.ItemVO;
import mx.com.aon.configurador.pantallas.model.components.RecordVO;
import mx.com.aon.flujos.cotizacion.model.ResultadoCotizacionVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CotizacionManagerImpl {
	
	protected final transient Logger log = Logger.getLogger(CotizacionManagerImpl.class);
	
	/**
	 * Metodo antiguo utilizado para adaptar los datos de cotizacion y mostrarlos en un grid
	 * @param resultList
	 * @return GridVO con los resultados
	 * @throws Exception
	 */
	public GridVO adaptarDatosCotizacion(List <ResultadoCotizacionVO> resultList) throws Exception {
		GridVO gridVo = new GridVO();
		if(resultList == null) return gridVo;
		
		List<RecordVO> recordListFinal = null;
		List<ColumnGridVO> columnListFinal = null;
		//List<ResultadoCotizacionVO> resultListFinal = resultList;
		List<ItemVO> itemListPlanFinal = null;
		List<ItemVO> itemListAseguradoraFinal = null;
		List<ItemVO> itemListPagoFinal = null;
		
		LinkedHashMap<String,String> planes = new LinkedHashMap<String, String>();
		HashMap<String,String> asegs = new HashMap<String, String>();
		HashMap<String,String> perpags = new HashMap<String, String>();
		
		for(ResultadoCotizacionVO result : resultList){
			ItemVO item = null;
			
			/*Para llenar la lista de planes*/
			if(result.getCdPlan() !=null && !planes.containsKey(result.getCdPlan())){
				planes.put(result.getCdPlan(), result.getDsPlan());
			}
			/*Para llenar la lista de aseguradoras*/ 
			if(result.getCdCiaaseg() !=null && !asegs.containsKey(result.getCdCiaaseg())){
				asegs.put(result.getCdCiaaseg(), result.getDsUnieco());
				
				if(itemListAseguradoraFinal == null) itemListAseguradoraFinal = new ArrayList<ItemVO>();
				
				item = new ItemVO();
				item.setClave(result.getCdCiaaseg());
				item.setDescripcion(result.getDsUnieco()); 
				
				itemListAseguradoraFinal.add(item);
			}
			
			/*Para llenar la lista de las formas de pago*/ 
			if(result.getCdPerpag() !=null && !perpags.containsKey(result.getCdPerpag())){
				perpags.put(result.getCdPerpag(), result.getDsPerpag());

				if(itemListPagoFinal == null) itemListPagoFinal = new ArrayList<ItemVO>();
				
				item = new ItemVO();
				item.setClave(result.getCdPerpag());
				item.setDescripcion(result.getDsPerpag()); 
				
				itemListPagoFinal.add(item);
			}
		}
		
		log.debug("viendo orden hhh: "+planes);
		
		
		Iterator It = planes.entrySet().iterator();
        while (It.hasNext()) {
        	Map.Entry entry = (Map.Entry) It.next();
        	String cdPlan = (String)entry.getKey();
        	String dsPlan = (String)entry.getValue();
        	String trimDsPlan = StringUtils.deleteWhitespace(dsPlan);
        	String CDtrimDsPlan = "CD"+trimDsPlan;
        	String DStrimDsPlan = "DS"+trimDsPlan;
        	String NMtrimDsPlan = "NM"+trimDsPlan;
        	
        	
        	/*PARA LLENAR EL List <ItemVO> itemListPlan */
        	if(itemListPlanFinal == null) itemListPlanFinal = new ArrayList<ItemVO>();
        	
        	ItemVO item = new ItemVO();
			item.setClave(cdPlan);
			item.setDescripcion(trimDsPlan); //al pasar el xsl a este metodo se encontro que se esta mandando el la descripcion sin espacios 
			
			itemListPlanFinal.add(item);
        	
        	/*PARA FORMAR EL List<ColumnGridVO> columnList */
        	
        	if(columnListFinal == null ) columnListFinal = new ArrayList<ColumnGridVO>();
        	
        	ColumnGridVO column = new ColumnGridVO();
        	column.setHeader(dsPlan);
        	column.setDataIndex(trimDsPlan);
        	column.setWidth(100);
        	column.setSortable(false);
        	column.setId(trimDsPlan);
        	column.setHidden(false);

        	columnListFinal.add(column);
        	
        	column = new ColumnGridVO();
        	column.setHeader(CDtrimDsPlan);
        	column.setDataIndex(CDtrimDsPlan);
        	column.setWidth(100);
        	column.setSortable(false);
        	column.setId(CDtrimDsPlan);
        	column.setHidden(true);
        	
        	columnListFinal.add(column);
        	
        	column = new ColumnGridVO();
        	column.setHeader(DStrimDsPlan);
        	column.setDataIndex(DStrimDsPlan);
        	column.setWidth(100);
        	column.setSortable(false);
        	column.setId(DStrimDsPlan);
        	column.setHidden(true);
        	
        	columnListFinal.add(column);
        	
        	column = new ColumnGridVO();
        	column.setHeader(NMtrimDsPlan);
        	column.setDataIndex(NMtrimDsPlan);
        	column.setWidth(100);
        	column.setSortable(false);
        	column.setId(NMtrimDsPlan);
        	column.setHidden(true);
        	
        	columnListFinal.add(column);
        	
        	
        	/*PARA FORMAR EL List<RecordVO> recordList; */
        	
        	if(recordListFinal == null ) recordListFinal = new ArrayList<RecordVO>();
        	
        	RecordVO record = new RecordVO();
        	record.setName(trimDsPlan);
        	record.setType("string");
        	record.setMapping(trimDsPlan);
        	
        	recordListFinal.add(record);
        	
        	record = new RecordVO();
        	record.setName(CDtrimDsPlan);
        	record.setType("string");
        	record.setMapping(CDtrimDsPlan);
        	
        	recordListFinal.add(record);
        	
        	record = new RecordVO();
        	record.setName(DStrimDsPlan);
        	record.setType("string");
        	record.setMapping(DStrimDsPlan);
        	
        	recordListFinal.add(record);
        	
        	record = new RecordVO();
        	record.setName(NMtrimDsPlan);
        	record.setType("string");
        	record.setMapping(NMtrimDsPlan);
        	
        	recordListFinal.add(record);
        }
		
        gridVo.setRecordList(recordListFinal);
        gridVo.setColumnList(columnListFinal);
		gridVo.setItemListPlan(itemListPlanFinal);
		gridVo.setItemListAseguradora(itemListAseguradoraFinal);
		gridVo.setItemListPago(itemListPagoFinal);
		gridVo.setResultList(resultList);
		
		return gridVo;
	} 
	
}