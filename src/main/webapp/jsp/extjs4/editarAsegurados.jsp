<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
--%>
<style>
.x-action-col-icon {
    height: 16px;
    width: 16px;
    margin-right: 8px;
}
</style>
<script>
    var sucursalUsuarioSesion = '<s:property value="%{#session['USUARIO'].cdUnieco}" />';
    var inputCduniecop2= '<s:property value="map1.cdunieco" />';
    var inputCdramop2=   '<s:property value="map1.cdramo" />';
    var inputEstadop2=   '<s:property value="map1.estado" />';
    var inputNmpolizap2= '<s:property value="map1.nmpoliza" />';
    var inputCdtipsitp2= '<s:property value="map1.cdtipsit" />';
    var inputNmOrdDomp2= '<s:property value="map1.NMORDDOM" />';
//  alert(inputNmOrdDomp2);
    debug('inputCdtipsitp2:',inputCdtipsitp2);
    var inputMaxLenContratante = <s:property value="map1.maxLenContratante" />;
    var gridPersonasp2;
    var storeRolesp2;
    var storeGenerosp2;
    var storePersonasp2;
    var storeTomadorp2;
    var storeTpersonasp2;
    var storeNacionesp2;
    var storeParentescop2;
    var storeEstcivp2; //ELP
    var storeOcupp2;
    var gridPersonasp2;
    var editorRolesp2;
    var editorGenerosp2;
    var editorNacionesp2;
//  var editorNombreContratantep2;
    var urlCargarAseguradosp2='<s:url namespace="/" action="cargarComplementariosAsegurados" />';
    var urlCargarCatalogosp2      ='<s:url namespace="/catalogos"       action="obtieneCatalogo" />';
    var urlDatosComplementariosp2 ='<s:url namespace="/" action="datosComplementarios.action" />';
    var urlGuardarAseguradosp2='<s:url namespace="/" action="guardarComplementariosAsegurados" />';
    var urlCoberturasAseguradop2='<s:url namespace="/" action="editarCoberturas" />';
    var urlGenerarCdPersonp2='<s:url namespace="/" action="generarCdperson" />';
    var urlAutoRFCp2        ='<s:url namespace="/" action="buscarPersonasRepetidas" />';
    var urlDomiciliop2      ='<s:url namespace="/" action="pantallaDomicilio" />';
    var urlExclusionp2      ='<s:url namespace="/" action="pantallaExclusion" />';
    var urlValositp2        ='<s:url namespace="/" action="pantallaValosit" />';
    var _p31_urlPantallaCliente                = '<s:url namespace="/catalogos"  action="includes/personasLoader"              />'; 
    var urlPantallaBeneficiarios = '<s:url namespace="/catalogos" action="includes/pantallaBeneficiarios" />';
    
    var _RamoRecupera = ((Ramo.Recupera == inputCdramop2) && (TipoSituacion.RecuperaIndividual == inputCdtipsitp2))? true : false;
    
    var editorFechap2;
    var contextop2='${ctx}';
    var gridTomadorp2;
    var recordTomadorp2;
    var timeoutflagp2;
    var isCopiadop2=false;
    var respaldoContp2;
    var isRespaldoContp2=false;
    var editorRFCAp2;
    var editorRFCBp2;
    var timeoutBuscarRFCBp2;

    var campoAactualizar;
    
    var destruirContLoaderPersona;
    var obtieneDatosClienteContratante;
    
    var _p22_parentCallback         = false;
    var _contratanteSaved = false;
    
    var _nmOrdDomContratante = inputNmOrdDomp2;
    
    Ext.define('RFCPersona',
    {
        extend  : 'Ext.data.Model'
        ,fields : ["RFCCLI","NOMBRECLI","FENACIMICLI","DIRECCIONCLI","CLAVECLI","DISPLAY", "CDIDEPER", "CDIDEEXT"]
    });
    
    function rendererRolp2(v)
    {
        var leyenda='';
        if(typeof v == 'string')
                   //tengo solo el indice
        {
            //window.console&&console.log('string:');
            storeRolesp2.each(function(rec){
                //window.console&&console.log('iterando...',rec.data);
                if(rec.data.key==v)
                {
                    leyenda=rec.data.value; 
                }
            });
            //window.console&&console.log(leyenda);
        }
        else
        //tengo objeto que puede venir como Generic u otro mas complejo
        {
            //window.console&&console.log('object:');
            if(v.key&&v.value)
            //objeto Generic
            {
                leyenda=v.value;
            }
            else
            {
                leyenda=v.data.value;
            }
            //window.console&&console.log(leyenda);
        }
        //console.log('return',leyenda);
        return leyenda;
    }
    
    function rendererSexop2(v)
    {
        var leyenda='';
        if(typeof v == 'string')
                   //tengo solo el indice
        {
            //window.console&&console.log('string:');
            storeGenerosp2.each(function(rec){
                //window.console&&console.log('iterando...',rec.data);
                if(rec.data.key==v)
                {
                    leyenda=rec.data.value; 
                }
            });
            //window.console&&console.log(leyenda);
        }
        else
        //tengo objeto que puede venir como Generic u otro mas complejo
        {
            //window.console&&console.log('object:');
            if(v.key&&v.value)
            //objeto Generic
            {
                leyenda=v.value;
            }
            else
            {
                leyenda=v.data.value;
            }
            //window.console&&console.log(leyenda);
        }
        //console.log('return',leyenda);
        return leyenda;
    }
    
    function rendererTpersonap2(v)
    {
        var leyenda='';
        if(typeof v == 'string')
                   //tengo solo el indice
        {
            //window.console&&console.log('string:');
            storeTpersonasp2.each(function(rec){
                //window.console&&console.log('iterando...',rec.data);
                if(rec.data.key==v)
                {
                    leyenda=rec.data.value; 
                }
            });
            //window.console&&console.log(leyenda);
        }
        else
        //tengo objeto que puede venir como Generic u otro mas complejo
        {
            //window.console&&console.log('object:');
            if(v.key&&v.value)
            //objeto Generic
            {
                leyenda=v.value;
            }
            else
            {
                leyenda=v.data.value;
            }
            //window.console&&console.log(leyenda);
        }
        //console.log('return',leyenda);
        return leyenda;
    }
    
    function rendererNacionesp2(v)
    {
        var leyenda='';
        if(typeof v == 'string')
                   //tengo solo el indice
        {
            //window.console&&console.log('string:');
            storeNacionesp2.each(function(rec){
                //window.console&&console.log('iterando...',rec.data);
                if(rec.data.key==v)
                {
                    leyenda=rec.data.value; 
                }
            });
            //window.console&&console.log(leyenda);
        }
        else
        //tengo objeto que puede venir como Generic u otro mas complejo
        {
            //window.console&&console.log('object:');
            if(v.key&&v.value)
            //objeto Generic
            {
                leyenda=v.value;
            }
            else
            {
                leyenda=v.data.value;
            }
            //window.console&&console.log(leyenda);
        }
        //console.log('return',leyenda);
        return leyenda;
    }

    function rendererParentescop2(v)
    {
//        debug('>rendererParentescop2 args:',arguments,'.');
        var leyenda = '';
        try
        {
            if(!Ext.isEmpty(v))
            {
                storeParentescop2.each(function(rec)
                {
//                    debug('buscando',v,'en',rec.data,'.');
                    if(''+rec.get('key') === ''+v)
                    {
                        leyenda = rec.get('value');
                    }
                });
            }
            
            /*
	        if(typeof v == 'string')
	                   //tengo solo el indice
	        {
	            //window.console&&console.log('string:');
	            storeParentescop2.each(function(rec){
	                //window.console&&console.log('iterando...',rec.data);
	                if(rec.data.key==v)
	                {
	                    leyenda=rec.data.value; 
	                }
	            });
	            //window.console&&console.log(leyenda);
	        }
	        else
	        //tengo objeto que puede venir como Generic u otro mas complejo
	        {
	            //window.console&&console.log('object:');
	            if(v.key&&v.value)
	            //objeto Generic
	            {
	                leyenda=v.value;
	            }
	            else
	            {
	                leyenda=v.data.value;
	            }
	            //window.console&&console.log(leyenda);
	        }
	        //console.log('return',leyenda);
	        */
        }
        catch(e)
        {
            leyenda = '- ERROR -';
            debugError('error en rendererParentescop2:',e,'.');
        }
        return leyenda;
    }
    
    
    
    function rendererEstcivp2(v) //ELP
    {
        var leyenda='';
        if(typeof v == 'string')
                   //tengo solo el indice
        {
            //window.console&&console.log('string:');
            storeEstcivp2.each(function(rec){
                //window.console&&console.log('iterando...',rec.data);
                if(rec.data.key==v)
                {
                    leyenda=rec.data.value;
                }
            });
            //window.console&&console.log(leyenda);
        }
        
        return leyenda;
    }
    
    function rendererOcupp2(v) //ELP
    {
        var leyenda='';
        if(typeof v == 'string')
                   //tengo solo el indice
        {
            //window.console&&console.log('string:');
            storeOcupp2.each(function(rec){
                //window.console&&console.log('iterando...',rec.data);
                if(rec.data.key==v)
                {
                    leyenda=rec.data.value;
                }
            });
            //window.console&&console.log(leyenda);
        }
        
        return leyenda;
    }
    
    function editarDespuesValidacionesp2(incisosJson,banderaCoberODomici)
    {
        debug(incisosJson);
        var formPanel=Ext.getCmp('form1p2');
        var submitValues=formPanel.getForm().getValues();
        //console.log(submitValues);
        //console.log("###############################");
        submitValues['list1']=incisosJson;
        var map1={
        'pv_cdunieco' : inputCdunieco,
        'pv_cdramo'   : inputCdramo,
        'pv_estado'   : inputEstado,
        'pv_nmpoliza' : inputNmpoliza,
        'cdtipsit'    : inputCdtipsitp2,
        'pv_nmorddom' : _nmOrdDomContratante};
        submitValues['map1']=map1;
        //window.console&&console.log(submitValues);
        //Submit the Ajax request and handle the response
        formPanel.setLoading(true);
        /*Ext.MessageBox.show({
            msg: 'Cotizando...',
            width:300,
            wait:true,
            waitConfig:{interval:100}
        });*/
        Ext.Ajax.request(
        {
            url: urlGuardarAseguradosp2,
            jsonData:Ext.encode(submitValues),
            success:function(response,opts)
            {
                //Ext.MessageBox.hide();
                formPanel.setLoading(false);
                var jsonResp = Ext.decode(response.responseText);
                //window.console&&console.log(jsonResp);
                if(jsonResp.success==true)
                {
                    if(banderaCoberODomici&&banderaCoberODomici==true)
                    {
                        timeoutflagp2=3;
                    }
                    else
                    {
                        centrarVentanaInterna(Ext.Msg.show({
                            title:'Asegurados guardados',
                            msg: 'Se ha guardado la informaci&oacute;n',
                            buttons: Ext.Msg.OK
                        }));
                        expande(1);
                        
                        try
                        {
                            var boton = _fieldById('panDatComBotonRetarificar');
                            boton.mensajeInvalido = '';
                        }
                        catch(e)
                        {
                            debugError(e);
                        }
                    }
                }
                else
                {
                    Ext.Msg.show({
                        title:'Error',
                        msg: Ext.isEmpty(jsonResp.mensajeRespuesta)? 'No se pudo guardar' : jsonResp.mensajeRespuesta,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                    
                    timeoutflagp2=2;
                    
                    if(!Ext.isEmpty(jsonResp.mensajeRespuesta))
                    {
                        try
                        {
                            var boton = _fieldById('panDatComBotonRetarificar');
                            boton.mensajeInvalido = jsonResp.mensajeRespuesta+'<br/>Corrija y guarde nuevamente los datos de la pesta&ntilde;a "Editar asegurados" secci&oacute;n "Asegurados".';
                        }
                        catch(e)
                        {
                            debugError(e);
                        }
                    }
                }
            },
            failure:function(response,opts)
            {
                //Ext.MessageBox.hide();
                formPanel.setLoading(false);
                //window.console&&console.log("error");
                Ext.Msg.show({
                    title:'Error',
                    msg: 'Error de comunicaci&oacute;n',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
                timeoutflagp2=2;
            }
        });
    }
    
    //Agrega los valores por defectopara los campos nuevos agregados. ELP
    function valoresDefault(grid){ 
    	debug('>valoresDefault');
    	
    	var gridSource  = grid.getView().dataSource.data;
    	
    	//Valor por defecto para el campo de ocupacion ELP
    	for(var j=0;j < gridSource.length; j++){
    		
    		if(Ext.isEmpty(gridSource.getAt(j).data.ocup) || gridSource.getAt(j).data.ocup == '156'){
	    		gridSource.getAt(j).set('ocup','156');
	    		debug('-Ocupacion ',j,'- ',gridSource.getAt(j).data.ocup);
    		}
    		
    		debug(Ext.isEmpty(gridSource.getAt(j).data.cdestciv));
    		
    		if((gridSource.getAt(j).data.Parentesco == "T" && gridSource.getAt(j).data.cdestciv == 1)
    			&& (gridSource.getAt(j).data.Parentesco == "C" && gridSource.getAt(j).data.cdestciv == 2)){
    			gridSource.getAt(j).set('cdestciv','2');
    		}else if(Ext.isEmpty(gridSource.getAt(j).data.cdestciv)){
    			gridSource.getAt(j).set('cdestciv','1');
    		}
    		
    		
    	}
    	
    	debug('<valoresDefault');
    }
    
    //Pone el atributo allowBlank a los campos nuevos para una sucursal 1403 ELP
    function editarCamposPorCdunieco(grid,cdunieco){
    	debug('>editarCamposPorCdunieco');
    	
    	//Obtiene las columnas del grid
    	var gridColumns = grid.headerCt.getGridColumns();
    	
    	//Sucursal 1403 para el campo Estado Civil
    	for (var i = 0; i < gridColumns.length; i++) {
    	  if (gridColumns[i].dataIndex == "cdestciv" && cdunieco == 1403) {
	    		gridColumns[i].getEditor().allowBlank = false;
	    		debug(gridColumns[i].editor);
	    		break;
    	  }
    	}
    	
    	//Sucursal 1403 para el campo No. de Empleado
    	for (var i = 0; i < gridColumns.length; i++) {
    	  if (gridColumns[i].dataIndex == "numsoc" && cdunieco == 1403) {
	    		gridColumns[i].getEditor().allowBlank = false;
	    		gridColumns[i].getEditor().regex = /^\d+$/;
	    		debug(gridColumns[i].editor);
	    		break;
    	  }
    	}

    	//Sucursal 1403 para el campo Clave Familiar   	
    	for (var i = 0; i < gridColumns.length; i++) {
    	  if (gridColumns[i].dataIndex == "clvfam" && cdunieco == 1403) {
	    		gridColumns[i].getEditor().allowBlank = false;
	    		gridColumns[i].getEditor().regex = /^\d+$/;
	    		debug(gridColumns[i].editor);
	    		break;
    	  }
    	}
    	
    	//Sucursal 1403 para el campo Ocupacion
    	for (var i = 0; i < gridColumns.length; i++) {
    	  if (gridColumns[i].dataIndex == "ocup" && cdunieco == 1403) {
	    		gridColumns[i].getEditor().allowBlank = false;
	    		debug(gridColumns[i].editor);
	    		break;
    	  }
    	}
    	
    	debug('<editarCamposPorCdunieco');
    }
 
    //Valida que para sucursales diferentes de la 1403, los campos Edo civil y Ocupacion sean obligatorios solo para el titular. ELP
    function validaCamposTitular(grid,cdunieco){
    	debug('>validaCamposTitular');
    	
    	var gridColumns = grid.headerCt.getGridColumns();
    	var gridSource  = grid.getView().dataSource.data;
    	
    	
    	for(var i=0; i < gridSource.length; i++){
			if(gridSource.items[i].data.Parentesco !== "T" && cdunieco !== 1403){
				for (var j = 0; j < gridColumns.length; j++) {
		    	  		if (gridColumns[j].dataIndex === "cdestciv" && Ext.isEmpty(gridSource.items[i].data.cdestciv)){
		    	  			gridColumns[j].getEditor().allowBlank = true;
		    	  			debug('*Cuando no es tomador, lo pone no obligatorio',gridColumns[j].getEditor().allowBlank,'.');
		    	  		}
				}
			}
		}
		
		for(var i=0; i < gridSource.length; i++){
			if(gridSource.items[i].data.Parentesco !== "T" && cdunieco !== 1403){
				for (var j = 0; j < gridColumns.length; j++) {
		    	  		if (gridColumns[j].dataIndex === "ocup" && Ext.isEmpty(gridSource.items[i].data.ocup)){
		    	  			gridColumns[j].getEditor().allowBlank = true;
		    	  			debug('**Cuando no es tomador, lo pone no obligatorio',gridColumns[j].getEditor().allowBlank,'.');
		    	  		}
				}
			}else if(gridSource.items[i].data.Parentesco == "T" && cdunieco == 1403){
			    
			    for (var j = 0; j < gridColumns.length; j++) {
                    if (gridColumns[j].dataIndex === "ocup" && Ext.isEmpty(gridSource.items[i].data.ocup)){
                        /** A peticion de Adrian Olmos Fwd: (Nro.30) Para las pólizas de Clínica NOVA **/
                        gridColumns[j].getEditor().allowBlank = false;
                        debug('**Cuando es titular, lo pone como obligatorio obligatorio',gridColumns[j].getEditor().allowBlank,'.');
                    }
                }
			}	
		}
		
		debug('<validaCamposTitular');
    }
    
    //Valida que los datos sean validos y no se encuentren vacios solo para el registro de parentesco Titular. ELP
    function validaDatosAseguradosTitular(grid,cdunieco){
    	debug('>validaDatosAseguradosTitular');
    	
    	var size = grid.getView().dataSource.data.items.length;
		var errTitular = [];
		var err;
		
		
		for (var i=0; i < size; i++){
		

		        if((grid.getView().dataSource.data.items[i].data.Parentesco == "T") 
		        	&& (Ext.isEmpty(grid.getView().dataSource.data.items[i].data.cdestciv)) && cdunieco != 1403){
		        	
		            errTitular.push('Estado Civil ');
		            grid.getStore().getAt(i).set('cdestciv',null);
		        	
		        }
		
		        if(cdunieco != 1403 && grid.getView().dataSource.data.items[i].data.Parentesco == "T"
		        	&& Ext.isEmpty(grid.getView().dataSource.data.items[i].data.ocup)){
		           	
	        		errTitular.push('Ocupacion');
		        	grid.getStore().getAt(i).set('ocup','156');
		        	
		        }
				
		}
		
			
			if(!Ext.isEmpty(errTitular)){
				err = '\nEl titular tiene como obligatorios los campos '
				for(var i=0; i < errTitular.length;i++){
					err += errTitular[i];
				}
			}
		
		debug(err);
		
		debug('<validaDatosAseguradosTitular');
		
		return err;
		
    }
    
    //Valida que los datos sean validos y no se encuentren vacios solo para el registro de la sucursal 1403. ELP
    function validaDatosAsegurados(grid,cdunieco){
    	debug('>validaDatosAsegurados');
    	
    	var size = grid.getView().dataSource.data.items.length;
		var arrErrores = [];
		var err;
				
		for (var i=0; i < size; i++){
		
			debug('edociv ',grid.getView().dataSource.data.items[i].data.cdestciv);
			debug('ocup ',grid.getView().dataSource.data.items[i].data.ocup);
			
				if(cdunieco == 1403 && Ext.isEmpty(grid.getView().dataSource.data.items[i].data.cdestciv)){
		            arrErrores.push('Estado Civil\n');
		            grid.getStore().getAt(i).set('cdestciv',null);
		        }
			
			
		        if(cdunieco == 1403 && Ext.isEmpty(grid.getView().dataSource.data.items[i].data.numsoc)){
		            arrErrores.push('Numero de empleado\n');
		            grid.getStore().getAt(i).set('numsoc',null);
		        }
		
		        if(cdunieco == 1403 && Ext.isEmpty(grid.getView().dataSource.data.items[i].data.clvfam)){
		            arrErrores.push('Clave Familiar\n');
		            grid.getStore().getAt(i).set('clvfam',null);
		         }
		         
		         if(cdunieco == 1403 && Ext.isEmpty(grid.getView().dataSource.data.items[i].data.ocup)){
		            arrErrores.push('Ocupacion\n');
		            grid.getStore().getAt(i).set('ocup','156');
		        }
		
		}
		
		if(!Ext.isEmpty(arrErrores)){
			err = 'Los campos : ';
			for(var i=0; i < arrErrores.length;i++){
				err += arrErrores[i];
			}
			err +=' no son validos.';	
		}
		

		debug('<validaDatosAsegurados');
		
		return err;    	
    }
    
    //guardador
    function validarYGuardar()
    {
    debug("validarYGuardar flag:1");
    timeoutflagp2=1;
    if(Ext.getCmp('form1p2').getForm().isValid())
                        {
                            var incisosRecords = storePersonasp2.getRange();
                            if(incisosRecords&&incisosRecords.length>0)
                            {
                                var incisosJson = [];
                                var completos=true;
                                var storeSinCdperson=Ext.create('Ext.data.Store',{model:'Modelo1p2'});
                                var sinCdpersonlen=0;
                                var contratanteCumpleMaxLen = true;
                                
                                
                                var datosContr = obtieneDatosClienteContratante();
                                if(Ext.isEmpty(datosContr.nombre)){
                                    mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
                                    completos=false;
                                }else if(!Ext.isEmpty(datosContr.cdperson) && !_contratanteSaved){
                                    mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
                                    return false;
                                }
                                
                                //ver si el contratante es aparte
                                var hayContApart=true;
                                storePersonasp2.each(function(record,index)
                                {
                                    if(record.get('estomador')==true)
                                    {
                                        hayContApart=false;
                                    }
                                });
                                debug('*hayContApart',(hayContApart?'true':'false'));
                                
                                //para cuando el contratante es aparte
                                if(hayContApart)
                                {
                                    /*var recordContApart=storeTomadorp2.getAt(0);
                                    if(
                                        !recordContApart.get("nombre")
                                        ||recordContApart.get("nombre").length==0
                                        ||(
                                        (typeof recordContApart.get('tpersona')=='string' ?
                                                recordContApart.get('tpersona')           : 
                                                recordContApart.get('tpersona').get('key')
                                                )=='F'&&
                                        (
                                        !recordContApart.get("Apellido_Paterno")
                                        ||recordContApart.get("Apellido_Paterno").length==0
                                        ||!recordContApart.get("Apellido_Materno")
                                        ||recordContApart.get("Apellido_Materno").length==0
                                        )
                                        )
                                        ||!validarRFC(recordContApart.get("cdrfc"),recordContApart.get("tpersona"))
                                        )
                                    {
                                        //console.log("#incompleto:");
                                        //console.log(record);
                                        completos=false;                                    
                                    }
                                    debug('validando maxlen contratante aparte:',inputMaxLenContratante);
                                    var recordTmp = recordContApart;
                                    var lenTmp = 0;
                                    lenTmp = lenTmp + (recordTmp.get("nombre")==null?0:recordTmp.get("nombre").length);
                                    lenTmp = lenTmp + (recordTmp.get("segundo_nombre")==null?0:recordTmp.get("segundo_nombre").length);
                                    lenTmp = lenTmp + (recordTmp.get("Apellido_Paterno")==null?0:recordTmp.get("Apellido_Paterno").length);
                                    lenTmp = lenTmp + (recordTmp.get("Apellido_Materno")==null?0:recordTmp.get("Apellido_Materno").length);
                                    debug('lenTmp:',lenTmp);
                                    if(lenTmp>inputMaxLenContratante)
                                    {
                                        contratanteCumpleMaxLen = false;
                                    }
                                    */
                                    
                                    incisosJson.push({
                                        nmsituac:'0',
                                        cdrol:'1',
                                        fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                        sexo:     datosContr.sexo,
                                        cdperson: datosContr.cdperson,
                                        swexiper: 'S',
                                        cdideper: datosContr.cdideper,
                                        cdideext: datosContr.cdideext,
                                        nombre:   datosContr.nombre,
                                        segundo_nombre: datosContr.snombre,
                                        Apellido_Paterno: datosContr.appat,
                                        Apellido_Materno: datosContr.apmat,
                                        cdrfc:datosContr.rfc,
                                        tpersona : datosContr.tipoper,
                                        nacional : datosContr.naciona
                                    });
                                    /*if(!recordContApart.get("cdperson")||recordContApart.get("cdperson").length==0)
                                    {
                                        var recordSinCdperson=recordContApart.copy();
                                        recordSinCdperson.set('Parentesco','tomador');
                                        storeSinCdperson.add(recordSinCdperson);
                                        sinCdpersonlen++;
                                        //storeTomadorp2.removeAll();
                                    }*/
                                }
                                debug('*f2');
                                //!para cuando el contratante es aparte
                                
                                //revisar asegurados: completo, cdperson y json
                                storePersonasp2.each(function(recordAsegu,indexAsegu)
                                {
                                    //console.log(record);
                                    if(
                                        !recordAsegu.get("nombre")
                                        ||recordAsegu.get("nombre").length==0
                                        ||!recordAsegu.get("Apellido_Paterno")
                                        ||recordAsegu.get("Apellido_Paterno").length==0
                                        ||!recordAsegu.get("Apellido_Paterno")
                                        ||recordAsegu.get("Apellido_Paterno").length==0
                                        ||!recordAsegu.get("Apellido_Materno")
                                        ||recordAsegu.get("Apellido_Materno").length==0
                                        ||!validarRFC(recordAsegu.get("cdrfc"),recordAsegu.get("tpersona"))
                                        )
                                    {
                                        //console.log("#incompleto:");
                                        //console.log(record);
                                        completos=false;                                    
                                    }
                                    debug('*f3');
                                    if(!recordAsegu.get("cdperson")||recordAsegu.get("cdperson").length==0)
                                    {
                                        var recordSinCdperson=recordAsegu.copy();
                                        recordSinCdperson.set('Parentesco',indexAsegu);
                                        storeSinCdperson.add(recordSinCdperson);
                                        sinCdpersonlen++;
                                        //storePersonasp2.remove(recordAsegu);
                                    }
                                    debug('*f4');
                                    if((!hayContApart)&&recordAsegu.get('estomador'))
                                    {
                                        debug('se manda como contratante',recordAsegu);
                                        incisosJson.push({
                                            nmsituac:'0',
                                            cdrol:'1',
                                            fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                            sexo:     datosContr.sexo,
                                            cdperson: datosContr.cdperson,
                                            swexiper: 'S',
                                            cdideper: datosContr.cdideper,
                                            cdideext: datosContr.cdideext,
                                            nombre:   datosContr.nombre,
                                            segundo_nombre: datosContr.snombre,
                                            Apellido_Paterno: datosContr.appat,
                                            Apellido_Materno: datosContr.apmat,
                                            cdrfc:datosContr.rfc,
                                            tpersona : datosContr.tipoper,
                                            nacional : datosContr.naciona,
                                            cdestciv    :  recordAsegu.get('cdestciv'),
                                            numsoc      :  recordAsegu.get('numsoc'),
                                            clvfam      :  recordAsegu.get('clvfam'),
                                            ocup        :  recordAsegu.get('ocup'),
                                            parentesco	:  recordAsegu.get('Parentesco'),
                                            estomador   :  recordAsegu.get('estomador')
                                        });
                                        debug('validando maxlen contratante en los asegurados:',inputMaxLenContratante);
                                        var recordTmp = recordAsegu;
                                        var lenTmp = 0;
                                        lenTmp = lenTmp + (recordTmp.get("nombre")==null?0:recordTmp.get("nombre").length);
                                        lenTmp = lenTmp + (recordTmp.get("segundo_nombre")==null?0:recordTmp.get("segundo_nombre").length);
                                        lenTmp = lenTmp + (recordTmp.get("Apellido_Paterno")==null?0:recordTmp.get("Apellido_Paterno").length);
                                        lenTmp = lenTmp + (recordTmp.get("Apellido_Materno")==null?0:recordTmp.get("Apellido_Materno").length);
                                        debug('lenTmp:',lenTmp);
                                        if(lenTmp>inputMaxLenContratante)
                                        {
                                            contratanteCumpleMaxLen = false;
                                        }
                                    }
                                    debug('*f5');
                                    incisosJson.push({
                                        nmsituac:recordAsegu.get('nmsituac'),
                                        cdrol:typeof recordAsegu.get('cdrol')=='string'?recordAsegu.get('cdrol'):recordAsegu.get('cdrol').get('key'),
                                        fenacimi: typeof recordAsegu.get('fenacimi')=='string'?recordAsegu.get('fenacimi'):Ext.Date.format(recordAsegu.get('fenacimi'), 'd/m/Y'),
                                        sexo:typeof recordAsegu.get('sexo')=='string'?recordAsegu.get('sexo'):recordAsegu.get('sexo').get('key'),
                                        cdperson: recordAsegu.get('cdperson'),
                                        swexiper: recordAsegu.get('swexiper'),
                                        cdideper: recordAsegu.get('cdideper'),
                                        cdideext: recordAsegu.get('cdideext'),
                                        nombre: recordAsegu.get('nombre'),
                                        segundo_nombre: recordAsegu.get('segundo_nombre'),
                                        Apellido_Paterno: recordAsegu.get('Apellido_Paterno'),
                                        Apellido_Materno: recordAsegu.get('Apellido_Materno'),
                                        cdrfc: recordAsegu.get('cdrfc'),
                                        tpersona : typeof recordAsegu.get('tpersona')=='string'?recordAsegu.get('tpersona'):recordAsegu.get('tpersona').get('key'),
                                        nacional : typeof recordAsegu.get('nacional')=='string'?recordAsegu.get('nacional'):recordAsegu.get('nacional').get('key'),
                                		cdestciv 	:  recordAsegu.get('cdestciv'),
                                        numsoc   	:  recordAsegu.get('numsoc'),
                                        clvfam   	:  recordAsegu.get('clvfam'),
                                        ocup     	:  recordAsegu.get('ocup'),
                                        parentesco	:  recordAsegu.get('Parentesco'),
                                        estomador   :  recordAsegu.get('estomador')
                                    });
                                    debug('*f6');
                                });
                                
                                //tratar de hacer submit
                                if(contratanteCumpleMaxLen)
                                {
                                if(completos)
                                {
                                    if(sinCdpersonlen==0)
                                    {
                                        editarDespuesValidacionesp2(incisosJson,true);//manda el submit
                                    }
                                    else
                                    {
                                        Ext.getCmp('form1p2').setLoading(true);
                                        //mandar a traer los cdperson de las personas asincrono
                                        storeSinCdperson.each(function(recordIteSinCdperson,index)
                                        {
                                            //console.log(index);
                                            setTimeout(function()
                                            {
                                                //console.log("trigger");
                                                Ext.Ajax.request(
                                                {
                                                    url: urlGenerarCdPersonp2,
                                                    success:function(response,opts)
                                                    {
                                                        var jsonResp = Ext.decode(response.responseText);
                                                        debug("respuesta cdperson",jsonResp);
                                                        debug(recordIteSinCdperson);
                                                        //window.console&&console.log(jsonResp);
                                                        if(jsonResp.success==true)
                                                        {
                                                            try
                                                            {
                                                                recordIteSinCdperson.set('cdperson',jsonResp.cdperson);
                                                                
                                                                sinCdpersonlen--;
                                                                if(sinCdpersonlen==0)
                                                                {
                                                                    //restaurar stores
                                                                    storeSinCdperson.each(function(recordIteConCdperson)
                                                                    {
                                                                        debug('resultado iterando',recordIteConCdperson);
                                                                        if(recordIteConCdperson.get('Parentesco')=='tomador')
                                                                        {
                                                                            //ya no aplica
                                                                            storeTomadorp2.getAt(0).set('cdperson',recordIteConCdperson.get('cdperson'));
                                                                        }
                                                                        else
                                                                        {
                                                                            storePersonasp2.getAt(recordIteConCdperson.get('Parentesco')).set('cdperson',recordIteConCdperson.get('cdperson'));
                                                                        }
                                                                    });
                                                                    //procesar submit
                                                                    //console.log(storePersonasp2.getRange());
                                                                    //storePersonasp2.sync();
                                                                    //console.log(storePersonasp2.getRange());
                                                                    gridPersonasp2.getView().refresh();
                                                                    incisosJson=[];
                                                                    if(hayContApart)
                                                                    {
                                                                        incisosJson.push({
                                                                            nmsituac:'0',
                                                                            cdrol:'1',
                                                                            fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                                                            sexo:     datosContr.sexo,
                                                                            cdperson: datosContr.cdperson,
                                                                            swexiper: 'S',
                                                                            cdideper: datosContr.cdideper,
                                                                            cdideext: datosContr.cdideext,
                                                                            nombre:   datosContr.nombre,
                                                                            segundo_nombre: datosContr.snombre,
                                                                            Apellido_Paterno: datosContr.appat,
                                                                            Apellido_Materno: datosContr.apmat,
                                                                            cdrfc:datosContr.rfc,
                                                                            tpersona : datosContr.tipoper,
                                                                            nacional : datosContr.naciona
                                                                        });
                                                                    }
                                                                    storePersonasp2.each(function(recordAsegu2)
                                                                    {
                                                                        if((!hayContApart)&&recordAsegu2.get('estomador'))
                                                                        {
                                                                            debug('se manda como contratante',recordAsegu2);
                                                                            incisosJson.push({
                                                                                nmsituac:'0',
                                                                                cdrol:'1',
                                                                                fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                                                                sexo:     datosContr.sexo,
                                                                                cdperson: datosContr.cdperson,
                                                                                swexiper: 'S',
                                                                                cdideper: datosContr.cdideper,
                                                                                cdideext: datosContr.cdideext,
                                                                                nombre:   datosContr.nombre,
                                                                                segundo_nombre: datosContr.snombre,
                                                                                Apellido_Paterno: datosContr.appat,
                                                                                Apellido_Materno: datosContr.apmat,
                                                                                cdrfc:datosContr.rfc,
                                                                                tpersona : datosContr.tipoper,
                                                                                nacional : datosContr.naciona
                                                                               ,cdestciv :  recordAsegu2.get('cdestciv'),
										                                        clvfam   :  recordAsegu2.get('clvfam'),
										                                        numsoc   :  recordAsegu2.get('numsoc'),
										                                        ocup     :  recordAsegu2.get('ocup'),
										                                        parentesco	: recordAsegu2.get('Parentesco')
                                                                            });
                                                                        }
                                                                        incisosJson.push({
                                                                            nmsituac: recordAsegu2.get('nmsituac'),
                                                                            cdrol:typeof recordAsegu2.get('cdrol')=='string'?recordAsegu2.get('cdrol'):recordAsegu2.get('cdrol').get('key'),
                                                                            fenacimi: typeof recordAsegu2.get('fenacimi')=='string'?recordAsegu2.get('fenacimi'):Ext.Date.format(recordAsegu2.get('fenacimi'), 'd/m/Y'),
                                                                            sexo:typeof recordAsegu2.get('sexo')=='string'?recordAsegu2.get('sexo'):recordAsegu2.get('sexo').get('key'),
                                                                            cdperson: recordAsegu2.get('cdperson'),
                                                                            swexiper: recordAsegu2.get('swexiper'),
                                                                            cdideper: recordAsegu2.get('cdideper'),
                                                                            cdideext: recordAsegu2.get('cdideext'),
                                                                            nombre: recordAsegu2.get('nombre'),
                                                                            segundo_nombre: recordAsegu2.get('segundo_nombre'),
                                                                            Apellido_Paterno: recordAsegu2.get('Apellido_Paterno'),
                                                                            Apellido_Materno: recordAsegu2.get('Apellido_Materno'),
                                                                            cdrfc: recordAsegu2.get('cdrfc'),
                                                                            tpersona : typeof recordAsegu2.get('tpersona')=='string'?recordAsegu2.get('tpersona'):recordAsegu2.get('tpersona').get('key'),
                                                                            nacional : typeof recordAsegu2.get('nacional')=='string'?recordAsegu2.get('nacional'):recordAsegu2.get('nacional').get('key')
	                                                                   	   ,cdestciv :  recordAsegu2.get('cdestciv'),
									                                        numsoc   :  recordAsegu2.get('numsoc'),
									                                        clvfam   :  recordAsegu2.get('clvfam'),
									                                        ocup     :  recordAsegu2.get('ocup'),
									                                        parentesco	: recordAsegu2.get('Parentesco'),
									                                        estomador   : recordAsegu2.get('estomador')
                                                                        });
                                                                    });                
                                                                    Ext.getCmp('form1p2').setLoading(false);
                                                                    editarDespuesValidacionesp2(incisosJson,true);
                                                                }
                                                            }
                                                            catch(e)
                                                            {
                                                                debug(e);
                                                                Ext.Msg.show({
                                                                    title:'Error',
                                                                    msg: 'Error al procesar la informaci&oacute;n',
                                                                    buttons: Ext.Msg.OK,
                                                                    icon: Ext.Msg.ERROR
                                                                });
debug("validarYGuardar flag:2");
                timeoutflagp2=2;
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Ext.Msg.show({
                                                                title:'Error',
                                                                msg: 'Error al obtener la informaci&oacute;n',
                                                                buttons: Ext.Msg.OK,
                                                                icon: Ext.Msg.ERROR
                                                            });
debug("validarYGuardar flag:2");
                timeoutflagp2=2;
                                                        }
                                                    },
                                                    failure:function(response,opts)
                                                    {
                                                        Ext.Msg.show({
                                                            title:'Error',
                                                            msg: 'Error de comunicaci&oacute;n',
                                                            buttons: Ext.Msg.OK,
                                                            icon: Ext.Msg.ERROR
                                                        });
debug("validarYGuardar flag:2");
                timeoutflagp2=2;
                                                    }
                                                });
                                            },(index+1)*500);
                                        });
                                    }
                                }
                                else
                                {
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title:'Datos incompletos',
                                        msg: 'El nombre, apellidos y RFC son requeridos. Capture primero el contratante.',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.WARNING
                                    }));
debug("validarYGuardar flag:2");
                timeoutflagp2=2;
                                }
                                }
                                else
                                {
                                    mensajeError('El contratante excede de '+inputMaxLenContratante+' caracteres');
                                }
                            }
                            else
                            {
                                Ext.Msg.show({
                                    title:'Datos incompletos',
                                    msg: 'Favor de introducir al menos un asegurado',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.WARNING
                                });         
debug("validarYGuardar flag:2");
                timeoutflagp2=2;            
                            }
}else
        {
            debug("validarYGuardar flag:2");
            timeoutflagp2=2;
        }
    }
    //!guardador
    
    Ext.onReady(function(){
        
        storeParentescop2 = new Ext.data.Store({
            model:'Generic',
            proxy:
            {
                type: 'ajax',
                url : urlCargarCatalogosp2,
                extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PARENTESCO"/>'},
                reader:
                {
                    type: 'json',
                    root: 'lista'
                }
            }
        });
        
        storeParentescop2.load();
        
        storeEstcivp2 = new Ext.data.Store({ //ELP
            model:'Generic',
            proxy:
            {
                type: 'ajax',
                url : urlCargarCatalogosp2,
                extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TEDOCIVIL"/>'},
                reader:
                {
                    type: 'json',
                    root: 'lista'
                }
            }
        });
        
        storeEstcivp2.load();
        
        storeOcupp2 = new Ext.data.Store({
        	model:'Generic',
        	proxy:
        	{
        		type:	'ajax',
        		url :	urlCargarCatalogosp2,
        		extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@OCUPACION"/>'},
        		reader:
        		{
        			type: 'json',
        			root: 'lista'
        		}
        	}
        });
        
        storeOcupp2.load();
        
        Ext.define('Modelo1p2',{
            extend:'Ext.data.Model',
            <s:property value="item1" />
        });
        
        storeRolesp2 = new Ext.data.Store({
            model: 'Generic',
            autoLoad:true,
            proxy:
            {
                type: 'ajax',
                url : urlCargarCatalogosp2,
                extraParams:{catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@ROLES_POLIZA"/>'},
                reader:
                {
                    type: 'json',
                    root: 'lista'
                }
            }
        })
        
        storeGenerosp2 = new Ext.data.Store({
            model: 'Generic',
            autoLoad:true,
            proxy:
            {
                type: 'ajax',
                url : urlCargarCatalogosp2,
                extraParams:{catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@SEXO"/>'},
                reader:
                {
                    type: 'json',
                    root: 'lista'
                }
            }
        });
        
        storeNacionesp2 = new Ext.data.Store({
            model:'Generic',
            autoLoad:true,
            proxy:
            {
                type: 'ajax',
                url : urlCargarCatalogosp2,
                extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@NACIONALIDAD"/>'},
                reader:
                {
                    type: 'json',
                    root: 'lista'
                }
            }
        });

        storeTpersonasp2 = new Ext.data.Store({
            model:'Generic',
            autoLoad:true,
            proxy:
            {
                type: 'ajax',
                url : urlCargarCatalogosp2,
                extraParams : {catalogo:'<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@TIPOS_PERSONA"/>'},
                reader:
                {
                    type: 'json',
                    root: 'lista'
                }
            }
        });
        
        storePersonasp2 =new Ext.data.Store(
        {
            // destroy the store if the grid is destroyed
            //autoDestroy: true,
            model: 'Modelo1p2',
            autoLoad:true,
            proxy:
            {
                url:urlCargarAseguradosp2,
                type:'ajax',
                extraParams:
                {
                    'map1.pv_cdunieco':inputCduniecop2,
                    'map1.pv_cdramo':inputCdramop2,
                    'map1.pv_estado':inputEstadop2,
                    'map1.pv_nmpoliza':inputNmpolizap2,
                    'map1.pv_nmsuplem':0
                },
                reader:
                {
                    type: 'json',
                    root: 'list1'
                }
            }
            ,listeners:
            {
                load:function( store, records, successful, eOpts )
                {
                    debug('listener load');
                    var indexTomador=-1;
                    
                    store.each(function(record,index)
                    {
                        debug('iterando',record);
                        if('1'==(typeof record.get('cdrol')=='string'?record.get('cdrol'):record.get('cdrol').get('key'))&&indexTomador==-1)
                        {
                            debug('es tomador en indice '+index);
                            indexTomador=index;
                        }
                        else
                        {
                            debug('no es tomador');
                        }
                    });
                    
                    var recordContra = false;
                    if(indexTomador != -1){
                        recordContra=store.getAt(indexTomador);
                        storeTomadorp2.add(recordContra);
                        storePersonasp2.remove(recordContra);
                    }
                    
                    debug('checar cual si un asegurado es el contratante');
                    if(recordContra && recordContra.get('cdperson')&&recordContra.get('cdperson').length>0)
                    {
                        debug('el contratante se busca en asegurados con cdperson '+recordContra.get('cdperson'));
                        var cdpersonTomador=recordContra.get('cdperson');
                        store.each(function(record,index)
                        {
                            debug('iterando',record);
                            if(record.get('cdperson')==cdpersonTomador)
                            {
                                debug('es el contratante',record);                            
                                record.set('estomador',true);
                                recordTomadorp2=record.copy();
                                recordTomadorp2.set('cdrol','1');
                                recordTomadorp2.set('nmsituac','0');
//                                gridTomadorp2.setDisabled(true);
                                isCopiadop2=true;
                                debug('se puso en sesion recordTomadorp2',recordTomadorp2);
                                //gridTomadorp2.setDisabled(true);
                            }
                            else
                            {
                                debug('no es el tomador');
                            }
                        });
                        
                        if(!Ext.isEmpty(destruirContLoaderPersona)){
                            destruirContLoaderPersona();                                    
                        }
                        
                        gridTomadorp2.getLoader().destroy();
                        
                        gridTomadorp2.loader = new Ext.ComponentLoader({
                            url       : _p31_urlPantallaCliente
                            ,scripts  : true
                            ,autoLoad : false
                            ,ajaxOptions: {
                                    method: 'POST'
                             }
                        });
                        
                        
                        gridTomadorp2.getLoader().load({
                                params: {
                                    'smap1.cdperson' : cdpersonTomador,
                                    'smap1.cdideper' : recordContra.get('cdideper'),
                                    'smap1.cdideext' : recordContra.get('cdideext'),
                                    'smap1.esSaludDanios' : _RamoRecupera? 'D' : 'S',
                                    'smap1.polizaEnEmision': 'S',
                                    'smap1.esCargaClienteNvo' : 'N' ,
                                    'smap1.ocultaBusqueda' : 'S' ,
                                    'smap1.cargaCP' : '',
                                    'smap1.cargaTipoPersona' : '',
                                    'smap1.cargaSucursalEmi' : inputCduniecop2,
                                    'smap1.activaCveFamiliar': inputCduniecop2 == '1403'?'S':'N',   
                                    'smap1.tomarUnDomicilio' : 'S',
                                    'smap1.cargaOrdDomicilio' : inputNmOrdDomp2
                                }
                         });
                         _p22_parentCallback = _p29_personaSaved;
                         _contratanteSaved = true;
                    }
                    else
                    {
                        debug('el contratante no tiene cdperson, no se busca en los asegurados');
                        
                        if(!Ext.isEmpty(destruirContLoaderPersona)){
                            destruirContLoaderPersona();                                    
                        }
                        
                        gridTomadorp2.getLoader().destroy();
                        
                        gridTomadorp2.loader = new Ext.ComponentLoader({
                            url       : _p31_urlPantallaCliente
                            ,scripts  : true
                            ,autoLoad : false
                            ,ajaxOptions: {
                                    method: 'POST'
                             }
                        });
                        
                        
                        gridTomadorp2.getLoader().load({
                                params: {
                                    'smap1.cdperson' : '',
                                    'smap1.cdideper' : '',
                                    'smap1.cdideext' : '',
                                    'smap1.esSaludDanios' : _RamoRecupera? 'D' : 'S',
                                    'smap1.polizaEnEmision': 'S',
                                    'smap1.esCargaClienteNvo' : 'N' ,
                                    'smap1.ocultaBusqueda' : 'S' ,
                                    'smap1.cargaCP' : '',
                                    'smap1.cargaTipoPersona' : '',
                                    'smap1.cargaSucursalEmi' : inputCduniecop2,
                                    'smap1.activaCveFamiliar': inputCduniecop2 == '1403'?'S':'N',
                                    'smap1.tomarUnDomicilio' : 'S',
                                    'smap1.cargaOrdDomicilio' : inputNmOrdDomp2
                                }
                         });
                         _p22_parentCallback = _p29_personaSaved;
                         _contratanteSaved = false;
                    }
                    debug("load isCopiadop2:"+(isCopiadop2?'true':'false'));
                    
                    //Valida los titulares diferentes a la sucursal 1403 ELP
	                validaCamposTitular(gridPersonasp2,inputCduniecop2);
	                
	                valoresDefault(gridPersonasp2);
                }
            }               
        });
        
        
        storeTomadorp2 =new Ext.data.Store(
        {
            model     : 'Modelo1p2'
        });
        
//      editorNombreContratantep2=Ext.create('Ext.form.field.Text',
//      {
//          allowBlank  : false
//          ,fieldWidth : 300
//      });
        
//      editorNombreContratantep2.on('focus',function()
//        {
//            debug('focus en contratante');
//            $('.grid_tomador_p2_id_help').remove();
//            $('#grid_tomador_p2_id').after('<span class="grid_tomador_p2_id_help">'+this.value+'</span>');
//        });
//      
//      editorNombreContratantep2.on('change',function()
//      {
//          debug('cambio en contratante');
//          $('.grid_tomador_p2_id_help').remove();
//          $('#grid_tomador_p2_id').after('<span class="grid_tomador_p2_id_help">'+this.value+'</span>');
//      });
//      
//      editorNombreContratantep2.on('blur',function()
//        {
//            debug('blur en contratante');
//            $('.grid_tomador_p2_id_help').remove();
//        });
        
        editorRolesp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeRolesp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });
        
        editorGenerosp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeGenerosp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });
        
        editorGenerosBp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeGenerosp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });
        
        editorTpersonap2=Ext.create('Ext.form.ComboBox',
        {
            store: storeTpersonasp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });
        
        editorTpersonaBp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeTpersonasp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });
        
        editorNacionesp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeNacionesp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });
        
        editorNacionesBp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeNacionesp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false
        });

        
        editorParentescop2=Ext.create('Ext.form.ComboBox',
        {
            store: storeParentescop2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false,
            readOnly:true
        });
        
        editorParentescoBp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeParentescop2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false,
            readOnly:true
        });
        
        editorEstcivp2=Ext.create('Ext.form.ComboBox', //ELP
        {
            store: storeEstcivp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false,
            listeners: {
				'beforeselect' : function(combo, record, index, eOpts){
					var edicivPersona = gridPersonasp2.getSelectionModel().getLastSelected();
					if(edicivPersona.get('estomador')){
						Ext.Msg.show({
							title    : 'Aviso'
					        ,icon    : Ext.Msg.WARNING
					        ,msg     : 'No puede cambiar el Estado Civil del Contratante.'
					        ,buttons : Ext.Msg.OK
					    });
						return false;
					}
				}
            }
        });
        
        editorEstcivBp2=Ext.create('Ext.form.ComboBox',
        {
            store: storeEstcivp2,
            queryMode:'local',
            displayField: 'value',
            valueField: 'key',
            allowBlank:false,
            editable:false,
            listeners: {
				'beforeselect' : function(combo, record, index, eOpts){
					var edicivPersona = gridPersonasp2.getSelectionModel().getLastSelected();
					if(edicivPersona.get('estomador')){
						Ext.Msg.show({
							title    : 'Aviso'
					        ,icon    : Ext.Msg.WARNING
					        ,msg     : 'No puede cambiar el Estado Civil del Contratante.'
					        ,buttons : Ext.Msg.OK
					    });
						return false;
					}
				}
            }
        });
        
        editorOcupp2=Ext.create('Ext.form.ComboBox',
        {
        	store: storeOcupp2,
        	queryMode:'local',
        	displayField: 'value',
        	valueField: 'key',
        	value : 'value',
        	allowBlank:false,
        	editable:true
        });
        
        editorFechap2=Ext.create('Ext.form.field.Date',
        {
            format:'d/m/Y',
            allowBlank:false
        });
        
        editorRFCAp2=Ext.create('Ext.form.TextField',
        {
            allowBlank : false
            ,listeners :
            {
                'blur' : function( field )
                {
//                  gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdperson",'');
//                  gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("swexiper",'N');
//                  gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdideper",'');
                    
                    var valorFieldRFC = field.getValue();
                    
                    if(valorFieldRFC.length>8)
                    {
                        clearTimeout(timeoutBuscarRFCBp2);
                        timeoutBuscarRFCBp2=setTimeout(function()
                        {
//                          gridTomadorp2.setLoading(true);
                            Ext.Ajax.request
                            ({
                                url     : urlAutoRFCp2
                                ,timeout: 240000
                                ,params :
                                {
                                    'map1.pv_rfc_i'     : valorFieldRFC,
                                    'map1.cdtipsit'     : inputCdtipsitp2,
                                    'map1.pv_cdunieco_i':inputCduniecop2,
                                    'map1.pv_cdramo_i'  :inputCdramop2,
                                    'map1.pv_estado_i'  :inputEstadop2,
                                    'map1.pv_nmpoliza_i':inputNmpolizap2,
                                    'map1.esContratante': 'S'
                                }
                                ,success:function(response)
                                {
//                                  gridTomadorp2.setLoading(false);
                                    var json=Ext.decode(response.responseText);
                                    debug(json);
                                    if(json&&json.slist1&&json.slist1.length>0)
                                    {
                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                                        {
                                            width        : 600
                                            ,height      : 400
                                            ,modal       : true
                                            ,autoScroll  : true
                                            ,title       : 'Coincidencias'
                                            ,items       : Ext.create('Ext.grid.Panel',
                                                           {
                                                               store    : Ext.create('Ext.data.Store',
                                                                          {
                                                                              model     : 'RFCPersona'
                                                                              ,autoLoad : true
                                                                              ,proxy :
                                                                              {
                                                                                  type    : 'memory'
                                                                                  ,reader : 'json'
                                                                                  ,data   : json['slist1']
                                                                              }
                                                                          })
                                                               ,columns :
                                                               [
                                                                   {
                                                                       xtype         : 'actioncolumn'
                                                                       ,menuDisabled : true
                                                                       ,width        : 30
                                                                       ,items        :
                                                                       [
                                                                           {
                                                                               icon     : '${ctx}/resources/fam3icons/icons/accept.png'
                                                                               ,tooltip : 'Seleccionar usuario'
                                                                               ,handler : function(grid, rowIndex, colIndex) {
                                                                                   var record = grid.getStore().getAt(rowIndex);
                                                                                   debug(record);
                                                                                   
                                                                                   debug('cliente obtenido de WS? ', json.clienteWS);
                                                                                   
//                                                                                   gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdrfc",record.get("RFCCLI"));
                                                                                   
//                                                                                   if(json.clienteWS){
//                                                                                     gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdideper",record.get("CDIDEPER"));
//                                                                                   }else{
//                                                                                     gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdperson",record.get("CLAVECLI"));
//                                                                                     gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("cdideper",record.get("CDIDEPER"));
//                                                                                       gridTomadorp2.getView().getSelectionModel().getSelection()[0].set("swexiper",'S');
//                                                                                   }
//                                                                                   
                                                                                   grid.up().up().destroy();
                                                                               }
                                                                           }
                                                                       ]
                                                                   },{
                                                                       header     : 'ID. Asegurado'
                                                                       ,dataIndex : 'CLAVECLI'
                                                                       ,flex      : 1
                                                                   },{
                                                                       header     : 'RFC'
                                                                       ,dataIndex : 'RFCCLI'
                                                                       ,flex      : 1
                                                                   }
                                                                   ,{
                                                                       header     : 'Nombre'
                                                                       ,dataIndex : 'NOMBRECLI'
                                                                       ,flex      : 1
                                                                   }
                                                                   ,{
                                                                       header     : 'Direcci&oacute;n'
                                                                       ,dataIndex : 'DIRECCIONCLI'
                                                                       ,flex      : 3
                                                                   }
                                                               ]
                                                           })
                                        }).show());
                                    }
                                    else
                                    {
                                        var ven=Ext.Msg.show({
                                            title:'Sin coincidencias',
                                            msg: 'No hay coincidencias de RFC',
                                            buttons: Ext.Msg.OK
                                        });
                                        centrarVentanaInterna(ven);
                                    }
                                }
                                ,failure:function()
                                {
//                                  gridTomadorp2.setLoading(false);
                                    Ext.Msg.show({
                                        title:'Error',
                                        msg: 'Error de comunicaci&oacute;n',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    });
                                }
                            });
                        },0);
                    }
                }
            }
        });
        editorRFCBp2=Ext.create('Ext.form.TextField',
        {
            allowBlank : false
            ,listeners :
            {
                'blur' : function( field , event)
                {
                    var valorFieldRFC = field.getValue();
                    var esContratante = (!Ext.isEmpty(campoAactualizar.get('estomador'))&& campoAactualizar.get('estomador'))?true:false;
                    
                    if(esContratante){
                    	campoAactualizar.set("swexiper",'S');
                    	campoAactualizar.set("estomador",esContratante);
                    	return;
                    }else{
                    	campoAactualizar.set("swexiper",'N');
                    	campoAactualizar.set("estomador",esContratante);
                    	campoAactualizar.set("cdperson",'');
                        campoAactualizar.set("cdideper",'');
                        campoAactualizar.set("cdideext",'');
                    }                  
                    
                    if(valorFieldRFC.length>8)
                    {
                        clearTimeout(timeoutBuscarRFCBp2);
                        timeoutBuscarRFCBp2=setTimeout(function()
                        {
                            gridPersonasp2.setLoading(true);
                            Ext.Ajax.request
                            ({
                                url     : urlAutoRFCp2
                                ,timeout: 240000
                                ,params :
                                {
                                    'map1.pv_rfc_i'     : valorFieldRFC,
                                    'map1.cdtipsit'     : inputCdtipsitp2,
                                    'map1.pv_cdunieco_i':inputCduniecop2,
                                    'map1.pv_cdramo_i'  :inputCdramop2,
                                    'map1.pv_estado_i'  :inputEstadop2,
                                    'map1.pv_nmpoliza_i':inputNmpolizap2,
                                    'map1.esContratante': esContratante? 'S':'N'
                                }
                                ,success:function(response)
                                {
                                    gridPersonasp2.setLoading(false);
                                    var json=Ext.decode(response.responseText);
                                    debug(json);
                                    if(json && !json.success){
                                        mensajeError("Error al Buscar RFC, Intente nuevamente. Si el problema persiste consulte a soporte t&eacute;cnico.");
                                        return;
                                    }
                                    if(json&&json.slist1&&json.slist1.length>0)
                                    {
                                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                                        {
                                            width        : 600
                                            ,height      : 400
                                            ,modal       : true
                                            ,autoScroll  : true
                                            ,title       : 'Coincidencias'
                                            ,items       : Ext.create('Ext.grid.Panel',
                                                           {
                                                               store    : Ext.create('Ext.data.Store',
                                                                          {
                                                                              model     : 'RFCPersona'
                                                                              ,autoLoad : true
                                                                              ,proxy :
                                                                              {
                                                                                  type    : 'memory'
                                                                                  ,reader : 'json'
                                                                                  ,data   : json['slist1']
                                                                              }
                                                                          })
                                                               ,columns :
                                                               [
                                                                   {
                                                                       xtype         : 'actioncolumn'
                                                                       ,menuDisabled : true
                                                                       ,width        : 30
                                                                       ,items        :
                                                                       [
                                                                           {
                                                                               icon     : '${ctx}/resources/fam3icons/icons/accept.png'
                                                                               ,tooltip : 'Seleccionar usuario'
                                                                               ,handler : function(grid, rowIndex, colIndex) {
                                                                                   var record = grid.getStore().getAt(rowIndex);
                                                                                   debug(record);
                                                                                   debug('cliente obtenido de WS? ', json.clienteWS);
                                                                                   
                                                                                   campoAactualizar.set("cdrfc",record.get("RFCCLI"));
                                                                                   
                                                                                   if(json.clienteWS){
                                                                                       campoAactualizar.set("cdideper",record.get("CDIDEPER"));
                                                                                       campoAactualizar.set("cdideext",record.get("CDIDEEXT"));
                                                                                   }else{
                                                                                       campoAactualizar.set("cdperson",record.get("CLAVECLI"));
                                                                                       campoAactualizar.set("cdideper",record.get("CDIDEPER"));
                                                                                       campoAactualizar.set("cdideext",record.get("CDIDEEXT"));
                                                                                       campoAactualizar.set("swexiper",'S');
                                                                                   }
                                                                                   
                                                                                   grid.up().up().destroy();
                                                                               }
                                                                           }
                                                                       ]
                                                                   },{
                                                                       header     : 'ID. Asegurado'
                                                                       ,dataIndex : 'CLAVECLI'
                                                                       ,flex      : 1
                                                                   }
                                                                   ,{
                                                                       header     : 'RFC'
                                                                       ,dataIndex : 'RFCCLI'
                                                                       ,flex      : 1
                                                                   }
                                                                   ,{
                                                                       header     : 'Nombre'
                                                                       ,dataIndex : 'NOMBRECLI'
                                                                       ,flex      : 1
                                                                   }
                                                                   ,{
                                                                       header     : 'Direcci&oacute;n'
                                                                       ,dataIndex : 'DIRECCIONCLI'
                                                                       ,flex      : 3
                                                                   }
                                                               ]
                                                           })
                                        }).show());
                                    }
                                    else
                                    {
                                        var ven=Ext.Msg.show({
                                            title:'Sin coincidencias',
                                            msg: 'No hay coincidencias de RFC',
                                            buttons: Ext.Msg.OK
                                        });
                                        centrarVentanaInterna(ven);
                                    }
                                }
                                ,failure:function()
                                {
                                    gridPersonasp2.setLoading(false);
                                    Ext.Msg.show({
                                        title:'Error',
                                        msg: 'Error de comunicaci&oacute;n',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.ERROR
                                    });
                                }
                            });
                        },0);
                    }
                },
                'change' : function(field){
                        //Ultimo record modificado
                        campoAactualizar = gridPersonasp2.getSelectionModel().getLastSelected();
                }
            }
        });
        
//      Ext.define('GridTomadorP2',
//          {
//          extend         : 'Ext.grid.Panel'
//          ,id            : 'grid_tomador_p2_id'
//          ,title         : 'Contratante'
//          ,store         : storeTomadorp2
//          ,frame         : false
//          ,style         : 'margin:5px'
//          ,selModel      :
//          {
//                selType: 'cellmodel'
//            }
//          ,requires      :
//              [
//                'Ext.selection.CellModel',
//                'Ext.grid.*',
//                'Ext.data.*',
//                'Ext.util.*',
//                'Ext.form.*'
//            ]
//          ,xtype         : 'cell-editing'
//          ,initComponent : function()
//          {
//              debug('initComponent');
//              this.cellEditing = new Ext.grid.plugin.CellEditing({
//                    clicksToEdit: 1
//                });
//              Ext.apply(this,
//                  {
//                  plugins : [this.cellEditing]
//                  ,s:property value="item3"
//                  ,listeners:
//                    {
//                        // add the validation after render so that validation is not triggered when the record is loaded.
//                        afterrender: function (grid)
//                        {
//                            var view = grid.getView();
//                         // validation on record level through "itemupdate" event
//                            view.on('itemupdate', function (record, y, node, options) {
//                                this.validateRow(this.getColumnIndexes(), record, y, true);
//                            }, grid);
//                        }
//                    }
//                  });
//              this.callParent();
//          }
//          ,getColumnIndexes: function () {
//                var me, columnIndexes;
//                me = this;
//                columnIndexes = [];
//                Ext.Array.each(me.columns, function (column)
//                {
//                    // only validate column with editor
//                    if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
//                        columnIndexes.push(column.dataIndex);
//                    } else {
//                        columnIndexes.push(undefined);
//                    }
//                });
//                //console.log(columnIndexes);
//                return columnIndexes;
//            }
//            ,validateRow: function (columnIndexes,record, y)
//            //hace que una celda de columna con allowblank=false tenga el estilo rojito
//            {
//                var view = this.getView();
//                Ext.each(columnIndexes, function (columnIndex, x)
//                {
//                    if(columnIndex)
//                    {
//                        var cell=view.getCellByPosition({row: y, column: x});
//                        cellValue=record.get(columnIndex);
//                        if((cell.addCls)&&((!cellValue)||(cellValue.lenght==0)))
//                        {
//                            //cell.addCls("custom-x-form-invalid-field");
//                        }
//                    }
//                });
//                return false;
//            }
//            ,onDomiciliosClick:function(grid,rowIndex)
//            {
//                var me=this;
//                debug("domicilios.click");
//                debug("validarYGuardar");
//                validarYGuardar();
//                setTimeout(function(){me.onDomiciliosInter(grid,rowIndex)},500);
//            }
//            ,onDomiciliosInter:function(grid,rowIndex)
//            {
//                var me=this;
//                debug("interval called");
//                if(timeoutflagp2==1)
//                {
//                    debug("interval: 1");
//                    setTimeout(function(){me.onDomiciliosInter(grid,rowIndex)},500);
//                }
//                else if(timeoutflagp2==3)
//                {
//                    debug("interval: 3 proceder");
//                    me.onDomiciliosSave(grid,rowIndex);
//                }
//                else
//                {
//                    debug("finish: "+timeoutflagp2)
//                }
//            }
//            ,onDomiciliosSave:function(grid,rowIndex)
//            {
//                var record=this.getStore().getAt(rowIndex);
// if(Ext.getCmp('domicilioAccordionEl'))
//                {
//                    Ext.getCmp('domicilioAccordionEl').destroy();
//                }
//                accordion.add(
//                {
//                    id:'domicilioAccordionEl'
//                    ,title:'Editar domicilio de '+record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno')
//                    ,cls:'claseTitulo'
//                    ,loader:
//                    {
//                        url : urlDomiciliop2
//                        ,params:
//                        {
//                            'smap1.pv_cdunieco'     : inputCduniecop2,
//                            'smap1.pv_cdramo'       : inputCdramop2,
//                            'smap1.pv_estado'       : inputEstadop2,
//                            'smap1.pv_nmpoliza'     : inputNmpolizap2,
//                            'smap1.pv_nmsituac'     : '0',
//                            'smap1.pv_cdperson'     : record.get('cdperson'),
//                            'smap1.pv_cdrol'        : '1',
//                            'smap1.nombreAsegurado' : record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno'),
//                            'smap1.cdrfc'           : record.get('cdrfc'),
//                            'smap1.botonCopiar'     : '0',
//                            'smap1.cdtipsit'        : inputCdtipsitp2
//                        }
//                        ,autoLoad:true
//                        ,scripts:true
//                    }
//                });
//            }
//          });
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////// Inicio de declaracion de grid                                                                             //////
        ////// http://docs.sencha.com/extjs/4.2.1/extjs-build/examples/build/KitchenSink/ext-theme-neptune/#cell-editing //////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Ext.define('EditorIncisosp2', {
            extend: 'Ext.grid.Panel',

            requires: [
                'Ext.selection.CellModel',
                'Ext.grid.*',
                'Ext.data.*',
                'Ext.util.*',
                'Ext.form.*'
            ],
            xtype: 'cell-editing',

            title: 'Asegurados',
            frame: false,
            //collapsible:true,
            //titleCollapse:true,
            style:'margin:5px;',
            //title:'Asegurados',

            initComponent: function() {
                this.cellEditing = new Ext.grid.plugin.CellEditing({
                    clicksToEdit: 1
                });

                Ext.apply(this, {
//                  	width: 750,
                    height: 200,
                    plugins: [this.cellEditing],
                    store: storePersonasp2,
                    <s:property value="item2" />,
                    selModel: {
                        selType: 'cellmodel'
                    },
                    autoScroll : true,
                    tbar: [{
                        icon    : '${ctx}/resources/fam3icons/icons/user_delete.png',
                        text    : 'Quitar/Cambiar Contratante',
                        handler : function(){
                            destruirContLoaderPersona();     
                            
                            gridTomadorp2.getLoader().destroy();
                            
                            gridTomadorp2.loader = new Ext.ComponentLoader({
                                url       : _p31_urlPantallaCliente
                                ,scripts  : true
                                ,autoLoad : false
                                ,ajaxOptions: {
                                        method: 'POST'
                                 }
                            });
                            
                            gridTomadorp2.getLoader().load({
                                    params: {
                                        'smap1.cdperson' : '',
                                        'smap1.cdideper' : '',
                                        'smap1.cdideext' : '',
                                        'smap1.esSaludDanios' : _RamoRecupera? 'D' : 'S',
                                        'smap1.polizaEnEmision': 'S',
                                        'smap1.esCargaClienteNvo' : 'N' ,
                                        'smap1.ocultaBusqueda' : 'S' ,
                                        'smap1.cargaCP' : '',
                                        'smap1.cargaTipoPersona' : '',
                                        'smap1.cargaSucursalEmi' : inputCduniecop2,
                                        'smap1.activaCveFamiliar': inputCduniecop2 == '1403'?'S':'N',
                                        'smap1.tomarUnDomicilio' : 'S',
                                        'smap1.cargaOrdDomicilio' : inputNmOrdDomp2
                                    }
                             });
                             _p22_parentCallback = _p29_personaSaved;
                             _contratanteSaved = false;
                             
                             storePersonasp2.each(function(recordI,index)
                                {
                                    if(recordI.get('estomador'))
                                    {
                                        recordI.set('estomador',false);
                                        recordI.set('fenacimi' ,Ext.Date.format(new Date(), 'd/m/Y'));
                                        recordI.set('tpersona' ,'F');
                                        recordI.set('cdperson' ,'');
                                        recordI.set('nombre'   ,'');
                                        recordI.set('segundo_nombre'  ,'');
                                        recordI.set('Apellido_Paterno','');
                                        recordI.set('Apellido_Materno','');
                                        recordI.set('cdrfc'   ,'');
                                        recordI.set('cdideper','');
                                        recordI.set('cdideext','');
                                        recordI.set('swexiper','N');
                                        recordI.set('cdestciv','');
                                        recordI.set('clvfam','');
                                        recordI.set('numsoc','');
                                        recordI.set('ocup'  ,'');
                                    }
                                    
                              });
                              
                              gridPersonasp2.getView().headerCt.child("[dataIndex=estomador]").enable();
                              
                        }
                    }],
                    /*tbar: [{
                        icon:'resources/extjs4/resources/fam3icons/icons/add.png',
                        text: 'Agregar',
                        scope: this,
                        handler: this.onAddClick
                    }],*/
                    /*http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29*/
                    //valida las celdas y les pone el estilo rojito
                    listeners:
                    {
                        // add the validation after render so that validation is not triggered when the record is loaded.
                        afterrender: function (grid)
                        {
                            var view = grid.getView();
                            
                            //Validacion para La sucursal 1403
                            debug('editarCamposCdunieco->');
                            editarCamposPorCdunieco(gridPersonasp2,inputCduniecop2);
                            
                         // validation on record level through "itemupdate" event
                            view.on('itemupdate', function (record, y, node, options) {
                                
                                var hayTomador=false;
                                storePersonasp2.each(function(recordI,index)
                                {
                                    if(recordI.get('estomador'))
                                    {
                                        hayTomador=true;
                                        gridPersonasp2.getView().headerCt.child("[dataIndex=estomador]").disable();
                                    }
                                });
                                debug('hay tomador '+(hayTomador?'true':'false'));
                                
                                
                                if(!hayTomador)
                                {
                                    if(isCopiadop2)
                                    {
                                        isCopiadop2=false;
                                        var recordCont=storeTomadorp2.getAt(0);
                                        recordCont.set('nombre','');
                                        recordCont.set('segundo_nombre','');
                                        recordCont.set('Apellido_Paterno','');
                                        recordCont.set('Apellido_Materno','');
                                        recordCont.set('fenacimi',Ext.Date.format(new Date(), 'd/m/Y'));
                                        recordCont.set('cdrol','1');
                                        recordCont.set('nmsituac','0');
                                        recordCont.set('cdrfc','');
                                        recordCont.set('cdestciv','2'); //recordAsegu.get('cdestciv')
                                        recordCont.set('cdperson','');
                                        recordCont.set('swexiper','N');
                                        recordCont.set('cdideper','');
                                        recordCont.set('cdideext','');
                                        debug('se reinicia',recordCont);
                                        
//                                        gridTomadorp2.update('');
//                                        if(!Ext.isEmpty(destruirContLoaderPersona)){
//                                          destruirContLoaderPersona();                                    
//                                      }
                                    }
                                    
                                    
                                }
//                              gridTomadorp2.setDisabled(hayTomador);
                                if(record.get('estomador'))
                                {
                                    if(!isCopiadop2)
                                    {
                                        isCopiadop2=true;
                                    }   
                                    storePersonasp2.each(function(recordI,index)
                                    {
                                        if(y!=index)
                                        {
                                            recordI.set('estomador',false);
                                        }
                                    });
                                    debug('tomador update:',record);
                                    recordTomadorp2=record.copy();
                                    debug('se puso en sesion recordTomadorp2',recordTomadorp2);
                                    storeTomadorp2.removeAll();
                                    storeTomadorp2.add(recordTomadorp2);
                                
                                }
                                this.validateRow(this.getColumnIndexes(), record, y, true);
                            }, grid);
                            
                        },
                        beforeedit: function (grid, e, eOpts)
                        {
                            //console.log("beforeedit");
                            console.log("e.column.xtype",e.column.xtype);
//                          e.column.xtype !== 'actioncolumn';
                            return e.column.xtype !== 'actioncolumn';//para que no edite sobre actioncolumn
                        }/*,
                        beforecellclick: function( vwTable, td, cellIndex, record, tr, rowIndex, e, eOpts ){
                            if(record.get("estomador"))return true; //Deja editar cuando un conrtratante es agregado.
                        }*/
                   }/*http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29*/

                });

                this.callParent();

                /*this.on('afterlayout', this.loadStore, this, {
                    delay: 1,
                    single: true
                })*/
            },

            /*http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29*/
            //regresa las columnas con editor que tengan allowBlank=false (requeridas)
            getColumnIndexes: function () {
                var me, columnIndexes;
                me = this;
                columnIndexes = [];
                Ext.Array.each(me.columns, function (column)
                {
                    // only validate column with editor
                    if (column.getEditor&&Ext.isDefined(column.getEditor())&&column.getEditor().allowBlank==false) {
                        columnIndexes.push(column.dataIndex);
                    } else {
                        columnIndexes.push(undefined);
                    }
                });
                //console.log(columnIndexes);
                return columnIndexes;
            },
            validateRow: function (columnIndexes,record, y)
            //hace que una celda de columna con allowblank=false tenga el estilo rojito
            {
                var view = this.getView();
                Ext.each(columnIndexes, function (columnIndex, x)
                {
                    if(columnIndex)
                    {
                        var cell=view.getCellByPosition({row: y, column: x});
                        cellValue=record.get(columnIndex);
                        if((cell.addCls)&&((!cellValue)||(cellValue.lenght==0)))
                        {
                            cell.addCls("custom-x-form-invalid-field");
                        }
                    }
                });
                return false;
            }/*http://www.sencha.com/forum/showthread.php?141626-Grid-Validation-with-Error-Indication-%28suggestions-needed%29*/,

            /*onAddClick: function(){
                //window.parent.scrollTo(0,600);
                // Create a model instance
                var rec = new Modelo1({
                    nmsituac:'',
                    cdrol: new Generic({key:storeRoles.getAt(0).data.key,value:storeRoles.getAt(0).data.value}),
                    fenacimi: new Date(),
                    sexo: new Generic({key:storeGeneros.getAt(0).data.key,value:storeGeneros.getAt(0).data.value}),
                    cdperson:'',
                    nombre: '',
                    segundo_nombre: '',
                    Apellido_Paterno: '',
                    Apellido_Materno: '',
                    cdrfc:''
                });

                this.getStore().insert(0, rec);
                
                this.validateRow(this.getColumnIndexes(), this.getStore().getAt(0), 0, true);
                
                //para acomodarse en la primer celda para editar
                this.cellEditing.startEditByPosition({
                    row: 0, 
                    column: 0
                });
            },*/
            
            onEditarInter:function(grid,rowIndex)
            {
                var me=this;
                debug("interval called");
                if(timeoutflagp2==1)
                {
                    debug("interval: 1");
                    setTimeout(function(){me.onEditarInter(grid,rowIndex)},500);
                }
                else if(timeoutflagp2==3)
                {
                    debug("interval: 3 proceder");
                    me.onEditarSave(grid,rowIndex);
                }
                else
                {
                    debug("finish: "+timeoutflagp2)
                }
            },
            
            onEditarClick:function(grid,rowIndex)
            {
            	
            	var validaNegocioCvFam;
                if(inputCduniecop2 == 1403){
                	validaNegocioCvFam = validaDatosAsegurados(gridPersonasp2,inputCduniecop2);
                }
                if(!Ext.isEmpty(validaNegocioCvFam)){
        		    mensajeWarning(validaNegocioCvFam);
        		    return false;
        		}
            	
                var me=this;
                debug("domicilios.click");
                debug("validarYGuardar");
                validarYGuardar();
                setTimeout(function(){me.onEditarInter(grid,rowIndex)},500);
            },
            
            onEditarSave:function(grid,rowIndex)
            {
                var record=this.getStore().getAt(rowIndex);
                if(Ext.getCmp('coberturasAccordionEl'))
                {
                    Ext.getCmp('coberturasAccordionEl').destroy();
                }
                accordion.add(
                {
                    id:'coberturasAccordionEl'
                    ,title:'Editar coberturas de '+record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno')
                    ,cls:'claseTitulo'
                    ,loader:
                    {
                        url : urlCoberturasAseguradop2
                        ,params:{
                            'smap1.pv_cdunieco' : inputCduniecop2,
                            'smap1.pv_cdramo'   : inputCdramop2,
                            'smap1.pv_estado'   : inputEstadop2,
                            'smap1.pv_nmpoliza' : inputNmpolizap2,
                            'smap1.pv_nmsituac' : record.get('nmsituac'),
                            'smap1.pv_cdperson' : record.get('cdperson'),
                            'smap1.pv_cdtipsit' : inputCdtipsitp2
                        }
                        ,autoLoad:true
                        ,scripts:true
                    }
                });
                /*
                Ext.create('Ext.form.Panel').submit({
                    standardSubmit:true,
                });
                */
            },
            
            onDomiciliosSave:function(grid,rowIndex)
            {
                var record=this.getStore().getAt(rowIndex);
                if(Ext.getCmp('domicilioAccordionEl'))
                {
                    Ext.getCmp('domicilioAccordionEl').destroy();
                }
                accordion.add(
                {
                    id:'domicilioAccordionEl'
                    ,title:'Editar domicilio de '+record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno')
                    ,cls:'claseTitulo'
                    ,loader:
                    {
                        url : urlDomiciliop2
                        ,params:
                        {
                            'smap1.pv_cdunieco'     : inputCduniecop2,
                            'smap1.pv_cdramo'       : inputCdramop2,
                            'smap1.pv_estado'       : inputEstadop2,
                            'smap1.pv_nmpoliza'     : inputNmpolizap2,
                            'smap1.pv_nmsituac'     : record.get('nmsituac'),
                            'smap1.pv_cdperson'     : record.get('cdperson'),
                            'smap1.pv_cdrol'        : record.get('estomador')==true?'1':record.get('cdrol'),
                            'smap1.nombreAsegurado' : record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno'),
                            'smap1.cdrfc'           : record.get('cdrfc'),
                            'smap1.botonCopiar'     : rowIndex>0?'1':'0',
                            'smap1.cdtipsit'        : inputCdtipsitp2,
                            'smap1.nmorddom'        : _nmOrdDomContratante
                        }
                        ,autoLoad:true
                        ,scripts:true
                    }
                });
            },
            
            onExclusionSave:function(grid,rowIndex)
            {
                var record=this.getStore().getAt(rowIndex);
                if(Ext.getCmp('exclusionAccordionEl'))
                {
                    Ext.getCmp('exclusionAccordionEl').destroy();
                }
                accordion.add(
                {
                    id:'exclusionAccordionEl'
                    ,title:'Editar exclusiones de '+record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno')
                    ,cls:'claseTitulo'
                    ,loader:
                    {
                        url : urlExclusionp2
                        ,params:
                        {
                            'smap1.pv_cdunieco'     : inputCduniecop2,
                            'smap1.pv_cdramo'       : inputCdramop2,
                            'smap1.pv_estado'       : inputEstadop2,
                            'smap1.pv_nmpoliza'     : inputNmpolizap2,
                            'smap1.pv_nmsituac'     : record.get('nmsituac'),
                            'smap1.pv_nmsuplem'     : '0',
                            'smap1.pv_cdperson'     : record.get('cdperson'),
                            'smap1.pv_cdrol'        : record.get('cdrol'),
                            'smap1.nombreAsegurado' : record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno'),
                            'smap1.cdrfc'           : record.get('cdrfc'),
                            'smap1.botonCopiar'     : rowIndex>0?'1':'0'
                        }
                        ,autoLoad:true
                        ,scripts:true
                    }
                });
            },
            
            onExclusionInter:function(grid,rowIndex)
            {
                var me=this;
                debug("interval called");
                if(timeoutflagp2==1)
                {
                    debug("interval: 1");
                    setTimeout(function(){me.onExclusionInter(grid,rowIndex)},500);
                }
                else if(timeoutflagp2==3)
                {
                    debug("interval: 3 proceder");
                    me.onExclusionSave(grid,rowIndex);
                }
                else
                {
                    debug("finish: "+timeoutflagp2)
                }
            },
            
            onDomiciliosInter:function(grid,rowIndex)
            {
                var me=this;
                debug("interval called");
                if(timeoutflagp2==1)
                {
                    debug("interval: 1");
                    setTimeout(function(){me.onDomiciliosInter(grid,rowIndex)},500);
                }
                else if(timeoutflagp2==3)
                {
                    debug("interval: 3 proceder");
                    me.onDomiciliosSave(grid,rowIndex);
                }
                else
                {
                    debug("finish: "+timeoutflagp2)
                }
            },
            
            onDomiciliosClick:function(grid,rowIndex)
            {
            	var validaNegocioCvFam;
                if(inputCduniecop2 == 1403){
                	validaNegocioCvFam = validaDatosAsegurados(gridPersonasp2,inputCduniecop2);
                }
                if(!Ext.isEmpty(validaNegocioCvFam)){
        		    mensajeWarning(validaNegocioCvFam);
        		    return false;
        		}
            	
                var me=this;
                debug("domicilios.click");
                debug("validarYGuardar");
                validarYGuardar();
                setTimeout(function(){me.onDomiciliosInter(grid,rowIndex)},500);
            },
            
            onExclusionClick:function(grid,rowIndex)
            {
            	
            	
            	var validaNegocioCvFam;
                if(inputCduniecop2 == 1403){
                	validaNegocioCvFam = validaDatosAsegurados(gridPersonasp2,inputCduniecop2);
                }
                if(!Ext.isEmpty(validaNegocioCvFam)){
        		    mensajeWarning(validaNegocioCvFam);
        		    return false;
        		}
                
                var me=this;
                debug("onExclusionClick");
                debug("validarYGuardar");
                validarYGuardar();
                setTimeout(function(){me.onExclusionInter(grid,rowIndex)},500);
            },

            onRemoveClick: function(grid, rowIndex){
                this.getStore().removeAt(rowIndex);
            }
            ,onTomadorClick : function(grid,rowIndex)
            {
                /*var record=grid.getStore().getAt(rowIndex);
                debug('es tomador',record);
                //gridTomadorp2.setDisabled(true);
                grid.getStore().each(function(rec,idx)
                {
                    rec.set('estomador',false);
                });
                record.set('estomador',true);
                recordTomadorp2=record.copy();
                debug('se puso en sesion recordTomadorp2',recordTomadorp2);
                storeTomadorp2.removeAll();
                storeTomadorp2.add(recordTomadorp2);*/
            }
            ,onBeneficiariosClick : function(grid,row)
            {
                var record=grid.getStore().getAt(row);
                if(Ext.getCmp('beneficiariosAccordionEl'))
                {
                    Ext.getCmp('beneficiariosAccordionEl').destroy();
                }
                accordion.add(
                {
                    id:'beneficiariosAccordionEl'
                    ,title:'Editar beneficiarios de '+record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno')
                    ,cls:'claseTitulo'
                    ,loader:
                    {
                        url : urlPantallaBeneficiarios
                        ,params   :
                        {
                            'smap1.cdunieco'      : inputCduniecop2
                            ,'smap1.cdramo'       : inputCdramop2
                            ,'smap1.estado'       : inputEstadop2
                            ,'smap1.nmpoliza'     : inputNmpolizap2
                            ,'smap1.nmsuplem'     : 0
                            ,'smap1.nmsituac'     : record.get('nmsituac')
                            ,'smap1.cdrolPipes'   : '3'
                            ,'smap1.cdtipsup'     : '1'
                            ,'smap1.ultimaImagen' : 'N'
                        }
                        ,autoLoad:true
                        ,scripts:true
                    }
                });
            }
            ,onValositClick : function(grid,rowIndex)
            {
            	
            	var validaNegocioCvFam;
                if(inputCduniecop2 == 1403){
                	validaNegocioCvFam = validaDatosAsegurados(gridPersonasp2,inputCduniecop2);
                }
                if(!Ext.isEmpty(validaNegocioCvFam)){
        		    mensajeWarning(validaNegocioCvFam);
        		    return false;
        		}
            	
                var record=grid.getStore().getAt(rowIndex);
                debug(record);
                if(Ext.getCmp('valositAccordionEl'))
                {
                    Ext.getCmp('valositAccordionEl').destroy();
                }
                accordion.add(
                {
                    id:'valositAccordionEl'
                    ,title:'Editar situación de '+record.get('nombre')+' '+(record.get('segundo_nombre')?record.get('segundo_nombre')+' ':' ')+record.get('Apellido_Paterno')+' '+record.get('Apellido_Materno')
                    ,cls:'claseTitulo'
                    ,loader:
                    {
                        url : urlValositp2
                        ,params   :
                        {
                            'smap1.cdunieco'  : inputCduniecop2
                            ,'smap1.cdramo'   : inputCdramop2
                            ,'smap1.estado'   : inputEstadop2
                            ,'smap1.nmpoliza' : inputNmpolizap2
                            ,'smap1.cdtipsit' : inputCdtipsitp2
                            ,'smap1.agrupado' : 'no'
                            ,'smap1.nmsituac' : record.get('nmsituac')
                        }
                        ,autoLoad:true
                        ,scripts:true
                    }
                });
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////// Fin de declaracion de grid                                                                                //////
        ////// http://docs.sencha.com/extjs/4.2.1/extjs-build/examples/build/KitchenSink/ext-theme-neptune/#cell-editing //////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        gridPersonasp2=new EditorIncisosp2();
        
        gridPersonasp2.getView().headerCt.child("[dataIndex=estomador]").on({
            beforecheckchange: function(chkCol, rowIndex, checked, eOpts){
                if(checked){
                    var datosContr = obtieneDatosClienteContratante();
                    if(Ext.isEmpty(datosContr.cdperson)){
                        mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
                        return false;
                    }else if(!Ext.isEmpty(datosContr.cdperson) && !_contratanteSaved){
                        mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
                        return false;
                    }else if("F" != datosContr.tipoper){
                        mensajeWarning('El Contratante como Asegurado no puede ser una persona Moral o R&eacute;gimen Simplificado');
                        return false;
                    }
                }
            },
            checkchange: function(chkCol, rowIndex, checked, eOpts){
                if(checked){
                    chkCol.disable();
                    var datosContr = obtieneDatosClienteContratante();
                    debug('Datos de Contratante para row: ' + datosContr);
                    debug('Estado civil contratante', datosContr);
                    recordContr = gridPersonasp2.getStore().getAt(rowIndex);
//                  recordContr.set('nmsituac','0');
//                  recordContr.set('cdrol'   ,'1');
                    recordContr.set('fenacimi', datosContr.fenacimi);
                    recordContr.set('sexo'    , datosContr.sexo);
                    recordContr.set('tpersona', datosContr.tipoper);
                    recordContr.set('nacional', datosContr.naciona);
                    recordContr.set('cdperson', datosContr.cdperson);
                    recordContr.set('nombre'  , datosContr.nombre);
                    recordContr.set('segundo_nombre'  , datosContr.snombre);
                    recordContr.set('Apellido_Paterno', datosContr.appat);
                    recordContr.set('Apellido_Materno', datosContr.apmat);
                    recordContr.set('cdrfc'   , datosContr.rfc);
                    recordContr.set('cdideper', datosContr.cdideper);
                    recordContr.set('cdideext', datosContr.cdideext);
                    recordContr.set('swexiper', 'S');
                    //ELP Datos traido de los datos del Contratante.
                    recordContr.set('cdestciv', datosContr.cdestciv);                    
                }
                
            }
        });
        
        
        
        gridTomadorp2= Ext.create('Ext.panel.Panel',
            {
                itemId      : '_p31_clientePanel'
                ,title      : 'Contratante'
                ,height     : 400
                ,autoScroll : true
                ,loader     :
                {
                    url       : _p31_urlPantallaCliente
                    ,scripts  : true
                    ,autoLoad : false
                    ,ajaxOptions: {
                            method: 'POST'
                     }
                }
            });
        
        Ext.create('Ext.form.Panel',{
            id:'form1p2',
            renderTo:'maindivasegurados',
            frame:false,
            //collapsible:true,
            //titleCollapse:true,
            border:0,
            buttonAlign:'center',
            items:[
                gridTomadorp2
                ,gridPersonasp2
            ],
            buttons:[
                <%--
                {
                    text:'Regresar',
                    hidden:true,
                    icon: contextop2+'/resources/extjs4/resources/my-custom-theme/images/toolbar/scroll-left.png',
                    handler:function()
                    {
                        Ext.create('Ext.form.Panel').submit({
                            url : urlDatosComplementariosp2,
                            standardSubmit:true,
                            params:{
                                'cdunieco' :  inputCduniecop2,
                                'cdramo' :    inputCdramop2,
                                'estado' :    inputEstadop2,
                                'nmpoliza' :  inputNmpolizap2
                            }
                        });
                    }
                },
                --%>
                {
                    text:'Guardar',
                    icon: contextop2+'/resources/fam3icons/icons/accept.png',
                    handler:function(){
                        debug('form1p2',Ext.getCmp('form1p2').getForm().isValid());
                        if(Ext.getCmp('form1p2').getForm().isValid())
                        {
                            var incisosRecords = storePersonasp2.getRange();
                            if(incisosRecords&&incisosRecords.length>0)
                            {
                                var incisosJson = [];
                                var completos=true;
                                var storeSinCdperson=Ext.create('Ext.data.Store',{model:'Modelo1p2'});
                                var sinCdpersonlen=0;
                                var contratanteCumpleMaxLen = true;
                                
                                var datosContr = obtieneDatosClienteContratante();
                                if(Ext.isEmpty(datosContr.nombre)){
                                    mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
                                    completos=false;
                                }else if(!Ext.isEmpty(datosContr.cdperson) && !_contratanteSaved){
                                    mensajeWarning('Primero debe de caputurar y guardar el Contratante.');
                                    return false;
                                }
                                
                                //Validacion de campos en el grid de asegurados. ELP
                                var mensajeError;
                                
                                if(inputCduniecop2 == 1403){
                                	mensajeError = validaDatosAsegurados(gridPersonasp2,inputCduniecop2);
                                }else{
                                	mensajeError = validaDatosAseguradosTitular(gridPersonasp2,inputCduniecop2);
                                }
                                	
                                if(!Ext.isEmpty(mensajeError)){
								    mensajeWarning(mensajeError);
								    return false;
								}
								
								var gridSource = gridPersonasp2.getView().dataSource.data;
								
								var hayConyuge = false;
								var titularCasado =  false;
								
								var clavesFam = [];
								var claveFamRepetida = false;
								var titularSinOcupacion = false;
								
								for(var j=0;j < gridSource.length;j++){
									if(gridSource.getAt(j).data.Parentesco == "C"){
										hayConyuge = true;
						    		}
									if(gridSource.getAt(j).data.Parentesco == "T" && gridSource.getAt(j).data.cdestciv == 2){
										titularCasado = true;
									}
									
									var ocupacionRec = gridSource.getAt(j).data.ocup;
									if(inputCduniecop2 == 1403 && gridSource.getAt(j).data.Parentesco == "T" 
									        && ( Ext.isEmpty(ocupacionRec) || ocupacionRec == '156' || ocupacionRec == 'n')){
									    titularSinOcupacion = true;
									}
									
									if(gridSource.getAt(j).data.cdrol == '2' && !Ext.isEmpty(gridSource.getAt(j).data.clvfam)){
										if( !Ext.Array.contains(clavesFam, gridSource.getAt(j).data.clvfam)){
										    Ext.Array.push(clavesFam, gridSource.getAt(j).data.clvfam);
										}else {
										    claveFamRepetida = true;
										}
									}
								}
								
								/** Se agrega validacion para no poder utilizar la misma clave familiar en todos los integrantes de la familia**/
								if( inputCduniecop2 == 1403 && titularSinOcupacion){
								    mensajeWarning('La ocupaci&oacute;n del Titular es obligatoria.');
                                    return false;
								}
								
								/** Se agrega validacion para no poder utilizar la misma clave familiar en todos los integrantes de la familia**/
								if(claveFamRepetida){
								    mensajeWarning('No se puede utilizar mas de una vez la Clave Familiar entre los integrantes de una familia.');
                                    return false;
								}
								
								for(var j=0;j < gridSource.length;j++){
									if(gridSource.getAt(j).data.Parentesco == "C" && titularCasado && gridSource.getAt(j).data.cdestciv != 2){
						    			mensajeWarning('El parentesco del conyuge debe ser Casado(a).');
						    			return false;
						    		}/*else if(gridSource.getAt(j).data.Parentesco == "T" && gridSource.getAt(j).data.cdestciv != 2 && hayConyuge){
										mensajeWarning('El parentesco del titular debe ser Casado(a), puesto que existe un conyuge.');
										return false;
						    		}*/	
								}
								
                                //ver si el contratante es aparte
                                var hayContApart=true;
                                storePersonasp2.each(function(record,index)
                                {
                                    if(record.get('estomador')==true)
                                    {
                                        hayContApart=false;
                                    }
                                });
                                debug('hayContApart',(hayContApart?'true':'false'));
                                
                                //para cuando el contratante es aparte
                                if(hayContApart)
                                {
                                    /*var recordContApart=storeTomadorp2.getAt(0);
                                    if(
                                        !recordContApart.get("nombre")
                                        ||recordContApart.get("nombre").length==0
                                        ||(
                                        (typeof recordContApart.get('tpersona')=='string' ?
                                                recordContApart.get('tpersona')           : 
                                                recordContApart.get('tpersona').get('key')
                                                )=='F'&&
                                        (
                                        !recordContApart.get("Apellido_Paterno")
                                        ||recordContApart.get("Apellido_Paterno").length==0
                                        ||!recordContApart.get("Apellido_Materno")
                                        ||recordContApart.get("Apellido_Materno").length==0
                                        )
                                        )
                                        ||!validarRFC(recordContApart.get("cdrfc"),recordContApart.get("tpersona"))
                                        )
                                    {
                                        //console.log("#incompleto:");
                                        //console.log(record);
                                        completos=false;                                    
                                    }
                                    debug('validando maxlen contratante aparte:',inputMaxLenContratante);
                                    var recordTmp = recordContApart;
                                    var lenTmp = 0;
                                    lenTmp = lenTmp + (recordTmp.get("nombre")==null?0:recordTmp.get("nombre").length);
                                    lenTmp = lenTmp + (recordTmp.get("segundo_nombre")==null?0:recordTmp.get("segundo_nombre").length);
                                    lenTmp = lenTmp + (recordTmp.get("Apellido_Paterno")==null?0:recordTmp.get("Apellido_Paterno").length);
                                    lenTmp = lenTmp + (recordTmp.get("Apellido_Materno")==null?0:recordTmp.get("Apellido_Materno").length);
                                    debug('lenTmp:',lenTmp);
                                    if(lenTmp>inputMaxLenContratante)
                                    {
                                        contratanteCumpleMaxLen = false;
                                    }*/
                                    
                                    
                                    incisosJson.push({
                                        nmsituac:'0',
                                        cdrol:'1',
                                        fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                        sexo:     datosContr.sexo,
                                        cdperson: datosContr.cdperson,
                                        swexiper: 'S',
                                        cdideper: datosContr.cdideper,
                                        cdideext: datosContr.cdideext,
                                        nombre:   datosContr.nombre,
                                        segundo_nombre: datosContr.snombre,
                                        Apellido_Paterno: datosContr.appat,
                                        Apellido_Materno: datosContr.apmat,
                                        cdrfc:datosContr.rfc,
                                        tpersona : datosContr.tipoper,
                                        nacional : datosContr.naciona
                                    });
                                    /*if(!recordContApart.get("cdperson")||recordContApart.get("cdperson").length==0)
                                    {
                                        var recordSinCdperson=recordContApart.copy();
                                        recordSinCdperson.set('Parentesco','tomador');
                                        storeSinCdperson.add(recordSinCdperson);
                                        sinCdpersonlen++;
                                        //storeTomadorp2.removeAll();
                                    }*/
                                }
                                debug('**f2');
                                //!para cuando el contratante es aparte
                                
                                //revisar asegurados: completo, cdperson y json
                                storePersonasp2.each(function(recordAsegu,indexAsegu)
                                {
                                    //console.log(record);
                                    if(
                                        !recordAsegu.get("nombre")
                                        ||recordAsegu.get("nombre").length==0
                                        ||!recordAsegu.get("Apellido_Paterno")
                                        ||recordAsegu.get("Apellido_Paterno").length==0
                                        ||!recordAsegu.get("Apellido_Paterno")
                                        ||recordAsegu.get("Apellido_Paterno").length==0
                                        ||!recordAsegu.get("Apellido_Materno")
                                        ||recordAsegu.get("Apellido_Materno").length==0
                                        ||!validarRFC(recordAsegu.get("cdrfc"),recordAsegu.get("tpersona")) 
                                        )
                                    {
                                        //console.log("#incompleto:");
                                        //console.log(record);
                                        completos=false;                                    
                                    }
                                    debug('**f3');
                                    if(!recordAsegu.get("cdperson")||recordAsegu.get("cdperson").length==0)
                                    {
                                        var recordSinCdperson=recordAsegu.copy();
                                        recordSinCdperson.set('Parentesco',indexAsegu);
                                        storeSinCdperson.add(recordSinCdperson);
                                        sinCdpersonlen++;
                                        //storePersonasp2.remove(recordAsegu);
                                    }
                                    debug('**f4');
                                    if((!hayContApart)&&recordAsegu.get('estomador'))
                                    {
                                        debug('se manda como contratante',recordAsegu);
                                        debug('estomador',recordAsegu.get('estomador'));
                                        incisosJson.push({
                                            nmsituac:'0',
                                            cdrol:'1',
                                            fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                            sexo:     datosContr.sexo,
                                            cdperson: datosContr.cdperson,
                                            swexiper: 'S',
                                            cdideper: datosContr.cdideper,
                                            cdideext: datosContr.cdideext,
                                            nombre:   datosContr.nombre,
                                            segundo_nombre: datosContr.snombre,
                                            Apellido_Paterno: datosContr.appat,
                                            Apellido_Materno: datosContr.apmat,
                                            cdrfc:datosContr.rfc,
                                            tpersona    : datosContr.tipoper,
                                            nacional    : datosContr.naciona,
                                            cdestciv    :  recordAsegu.get('cdestciv'),
                                            numsoc      :  recordAsegu.get('numsoc'),
                                            clvfam      :  recordAsegu.get('clvfam'),
                                            ocup        :  recordAsegu.get('ocup'),
                                            parentesco	:  recordAsegu.get('Parentesco'),
                                            estomador   :  recordAsegu.get('estomador')
                                        });
                                        debug('validando maxlen contratante en los asegurados:',inputMaxLenContratante);
                                        var recordTmp = recordAsegu;
                                        var lenTmp = 0;
                                        lenTmp = lenTmp + (recordTmp.get("nombre")==null?0:recordTmp.get("nombre").length);
                                        lenTmp = lenTmp + (recordTmp.get("segundo_nombre")==null?0:recordTmp.get("segundo_nombre").length);
                                        lenTmp = lenTmp + (recordTmp.get("Apellido_Paterno")==null?0:recordTmp.get("Apellido_Paterno").length);
                                        lenTmp = lenTmp + (recordTmp.get("Apellido_Materno")==null?0:recordTmp.get("Apellido_Materno").length);
                                        debug('lenTmp:',lenTmp);
                                        if(lenTmp>inputMaxLenContratante)
                                        {
                                            contratanteCumpleMaxLen = false;
                                        }
                                    }
                                    debug('**f5');
                                    incisosJson.push({
                                        nmsituac:recordAsegu.get('nmsituac'),
                                        cdrol:typeof recordAsegu.get('cdrol')=='string'?recordAsegu.get('cdrol'):recordAsegu.get('cdrol').get('key'),
                                        fenacimi: typeof recordAsegu.get('fenacimi')=='string'?recordAsegu.get('fenacimi'):Ext.Date.format(recordAsegu.get('fenacimi'), 'd/m/Y'),
                                        sexo:typeof recordAsegu.get('sexo')=='string'?recordAsegu.get('sexo'):recordAsegu.get('sexo').get('key'),
                                        cdperson: recordAsegu.get('cdperson'),
                                        swexiper: recordAsegu.get('swexiper'),
                                        cdideper: recordAsegu.get('cdideper'),
                                        cdideext: recordAsegu.get('cdideext'),
                                        nombre: recordAsegu.get('nombre'),
                                        segundo_nombre: recordAsegu.get('segundo_nombre'),
                                        Apellido_Paterno: recordAsegu.get('Apellido_Paterno'),
                                        Apellido_Materno: recordAsegu.get('Apellido_Materno'),
                                        cdrfc		: recordAsegu.get('cdrfc'),
                                        tpersona 	: typeof recordAsegu.get('tpersona')=='string'?recordAsegu.get('tpersona'):recordAsegu.get('tpersona').get('key'),
                                        nacional 	: typeof recordAsegu.get('nacional')=='string'?recordAsegu.get('nacional'):recordAsegu.get('nacional').get('key'),
                                        cdestciv 	:  recordAsegu.get('cdestciv'),
                                        numsoc   	:  recordAsegu.get('numsoc'),
                                        clvfam   	:  recordAsegu.get('clvfam'),
                                        ocup     	:  recordAsegu.get('ocup'),
                                        parentesco	: recordAsegu.get('Parentesco'),
                                        estomador   : recordAsegu.get('estomador')
                                    });
                                    debug('**f6');
                                    
                                    debug('¬Datos en recordAsegu',recordAsegu);
                                    
                                    debug('recordAsegu :',recordAsegu.get('cdestciv'),'.');
                                    debug('recordAsegu :',recordAsegu.get('numsoc'),'.');
                                    debug('recordAsegu :',recordAsegu.get('clvfam'),'.');
                                    debug('recordAsegu :',recordAsegu.get('ocup'),'.');
                                    debug('recordAsegu :',recordAsegu.get('Parentesco'),'.');
                                    debug('recordAsegu :',recordAsegu.get('estomador'),'.');
                                    
                                    
                                });
                                
                                //tratar de hacer submit
                                if(contratanteCumpleMaxLen)
                                {
                                if(completos)
                                {
                                    debug('tratar de hacer submit');
                                    if(sinCdpersonlen==0)
                                    {
                                        debug('incisosJson',incisosJson);
                                        editarDespuesValidacionesp2(incisosJson);//manda el submit
                                    }
                                    else
                                    {
                                        Ext.getCmp('form1p2').setLoading(true);
                                        //mandar a traer los cdperson de las personas asincrono
                                        storeSinCdperson.each(function(recordIteSinCdperson,index)
                                        {
                                            //console.log(index);
                                            setTimeout(function()
                                            {
                                                //console.log("trigger");
                                                Ext.Ajax.request(
                                                {
                                                    url: urlGenerarCdPersonp2,
                                                    success:function(response,opts)
                                                    {
                                                        var jsonResp = Ext.decode(response.responseText);
                                                        debug("respuesta cdperson",jsonResp);
                                                        debug(recordIteSinCdperson);
                                                        //window.console&&console.log(jsonResp);
                                                        if(jsonResp.success==true)
                                                        {
                                                            try
                                                            {
                                                                recordIteSinCdperson.set('cdperson',jsonResp.cdperson);
                                                                
                                                                sinCdpersonlen--;
                                                                if(sinCdpersonlen==0)
                                                                {
                                                                    //restaurar stores
                                                                    storeSinCdperson.each(function(recordIteConCdperson)
                                                                    {
                                                                        debug('resultado iterando',recordIteConCdperson);
                                                                        if(recordIteConCdperson.get('Parentesco')=='tomador')
                                                                        {
                                                                            //ya no aplica
                                                                            storeTomadorp2.getAt(0).set('cdperson',recordIteConCdperson.get('cdperson'));
                                                                        }
                                                                        else
                                                                        {
                                                                            storePersonasp2.getAt(recordIteConCdperson.get('Parentesco')).set('cdperson',recordIteConCdperson.get('cdperson'));
                                                                        }
                                                                    });
                                                                    //procesar submit
                                                                    //console.log(storePersonasp2.getRange());
                                                                    //storePersonasp2.sync();
                                                                    //console.log(storePersonasp2.getRange());
                                                                    gridPersonasp2.getView().refresh();
                                                                    incisosJson=[];
                                                                    if(hayContApart)
                                                                    {
                                                                        incisosJson.push({
                                                                            nmsituac:'0',
                                                                            cdrol:'1',
                                                                            fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                                                            sexo:     datosContr.sexo,
                                                                            cdperson: datosContr.cdperson,
                                                                            swexiper: 'S',
                                                                            cdideper: datosContr.cdideper,
                                                                            cdideext: datosContr.cdideext,
                                                                            nombre:   datosContr.nombre,
                                                                            segundo_nombre: datosContr.snombre,
                                                                            Apellido_Paterno: datosContr.appat,
                                                                            Apellido_Materno: datosContr.apmat,
                                                                            cdrfc:datosContr.rfc,
                                                                            tpersona : datosContr.tipoper,
                                                                            nacional : datosContr.naciona									                                        
                                                                        });
                                                                    }
                                                                    storePersonasp2.each(function(recordAsegu2)
                                                                    {
                                                                        if((!hayContApart)&&recordAsegu2.get('estomador'))
                                                                        {
                                                                            debug('se manda como contratante',recordAsegu2);
                                                                            incisosJson.push({
                                                                                nmsituac:'0',
                                                                                cdrol:'1',
                                                                                fenacimi: typeof datosContr.fenacimi=='string'?datosContr.fenacimi:Ext.Date.format(datosContr.fenacimi, 'd/m/Y'),
                                                                                sexo:     datosContr.sexo,
                                                                                cdperson: datosContr.cdperson,
                                                                                swexiper: 'S',
                                                                                cdideper: datosContr.cdideper,
                                                                                cdideext: datosContr.cdideext,
                                                                                nombre:   datosContr.nombre,
                                                                                segundo_nombre: datosContr.snombre,
                                                                                Apellido_Paterno: datosContr.appat,
                                                                                Apellido_Materno: datosContr.apmat,
                                                                                cdrfc:datosContr.rfc,
                                                                                tpersona : datosContr.tipoper,
                                                                                nacional : datosContr.naciona,
                                                                                cdestciv :  recordAsegu2.get('cdestciv'),
										                                        clvfam   :  recordAsegu2.get('clvfam'),
										                                        numsoc   :  recordAsegu2.get('numsoc'),
										                                        ocup     :  recordAsegu2.get('ocup'),
										                                        parentesco	: recordAsegu2.get('Parentesco')
                                                                            });
                                                                        }
                                                                        incisosJson.push({
                                                                            nmsituac: recordAsegu2.get('nmsituac'),
                                                                            cdrol:typeof recordAsegu2.get('cdrol')=='string'?recordAsegu2.get('cdrol'):recordAsegu2.get('cdrol').get('key'),
                                                                            fenacimi: typeof recordAsegu2.get('fenacimi')=='string'?recordAsegu2.get('fenacimi'):Ext.Date.format(recordAsegu2.get('fenacimi'), 'd/m/Y'),
                                                                            sexo:typeof recordAsegu2.get('sexo')=='string'?recordAsegu2.get('sexo'):recordAsegu2.get('sexo').get('key'),
                                                                            cdperson: recordAsegu2.get('cdperson'),
                                                                            swexiper: recordAsegu2.get('swexiper'),
                                                                            cdideper: recordAsegu2.get('cdideper'),
                                                                            cdideext: recordAsegu2.get('cdideext'),
                                                                            nombre: recordAsegu2.get('nombre'),
                                                                            segundo_nombre: recordAsegu2.get('segundo_nombre'),
                                                                            Apellido_Paterno: recordAsegu2.get('Apellido_Paterno'),
                                                                            Apellido_Materno: recordAsegu2.get('Apellido_Materno'),
                                                                            cdrfc: recordAsegu2.get('cdrfc'),
                                                                            tpersona : typeof recordAsegu2.get('tpersona')=='string'?recordAsegu2.get('tpersona'):recordAsegu2.get('tpersona').get('key'),
                                                                            nacional : typeof recordAsegu2.get('nacional')=='string'?recordAsegu2.get('nacional'):recordAsegu2.get('nacional').get('key')
                                                                           ,cdestciv :  recordAsegu2.get('cdestciv'),
									                                        numsoc   :  recordAsegu2.get('numsoc'),
									                                        clvfam   :  recordAsegu2.get('clvfam'),
									                                        ocup     :  recordAsegu2.get('ocup'),
									                                        parentesco	: recordAsegu2.get('Parentesco'),
									                                        estomador   : recordAsegu2.get('estomador')
                                                                        });
                                                                    });                
                                                                    Ext.getCmp('form1p2').setLoading(false);
                                                                    editarDespuesValidacionesp2(incisosJson);
                                                                }
                                                            }
                                                            catch(e)
                                                            {
                                                                debug(e);
                                                                Ext.Msg.show({
                                                                    title:'Error',
                                                                    msg: 'Error al procesar la informaci&oacute;n',
                                                                    buttons: Ext.Msg.OK,
                                                                    icon: Ext.Msg.ERROR
                                                                });
                                                                formPanel.setLoading(false);
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Ext.Msg.show({
                                                                title:'Error',
                                                                msg: 'Error al obtener la informaci&oacute;n',
                                                                buttons: Ext.Msg.OK,
                                                                icon: Ext.Msg.ERROR
                                                            });
                                                            formPanel.setLoading(false);
                                                        }
                                                    },
                                                    failure:function(response,opts)
                                                    {
                                                        Ext.Msg.show({
                                                            title:'Error',
                                                            msg: 'Error de comunicaci&oacute;n',
                                                            buttons: Ext.Msg.OK,
                                                            icon: Ext.Msg.ERROR
                                                        });
                                                        formPanel.setLoading(false);
                                                    }
                                                });
                                            },(index+1)*500);
                                        });
                                    }
                                }
                                else
                                {
                                    centrarVentanaInterna(Ext.Msg.show({
                                        title:'Datos incompletos',
                                        msg: 'El nombre, apellidos y RFC son requeridos. Capture primero el contratante.',
                                        buttons: Ext.Msg.OK,
                                        icon: Ext.Msg.WARNING
                                    }));
                                }
                            }
                            else
                            {
                                mensajeError('El contratante excede de '+inputMaxLenContratante+' caracteres');
                            }
                            }
                            else
                            {
                                Ext.Msg.show({
                                    title:'Datos incompletos',
                                    msg: 'Favor de introducir al menos un asegurado',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.WARNING
                                });                     
                            }
                        }
                        else
                        {
                            Ext.Msg.show({
                                title:'Datos incompletos',
                                msg: 'Favor de llenar los campos requeridos',
                                buttons: Ext.Msg.OK,
                                icon: Ext.Msg.WARNING
                            });
                        }
                    }
                }
                ,{
                    text:'Cancelar',
                    icon : contextop2+ '/resources/fam3icons/icons/cancel.png',
                    handler:function(){
                        expande(1);
                    }
                }
            ]
        });
        
        function _p29_loadCallback()
{
    var vigen = _fieldByLabel('VIGENCIA');
    vigen.hide();
    var feini = _fieldByName('feini');
    var fefin = _fieldByName('fefin');
    feini.on(
    {
        change : function(me,val)
        {
            try
            {
                fefin.setValue(Ext.Date.add(val,Ext.Date.DAY,vigen.getValue()))
            }
            catch(e)
            {
                debug(e);
            }
        }
    });
    
    Ext.Ajax.request(
    {
        url     : _p29_urlCargarRetroactividadSuplemento
        ,params :
        {
            'smap1.cdunieco'  : _p29_smap1.cdunieco
            ,'smap1.cdramo'   : _p29_smap1.cdramo
            ,'smap1.cdtipsup' : 1
            ,'smap1.cdusuari' : _p29_smap1.cdusuari
            ,'smap1.cdtipsit' : _p29_smap1.cdtipsit
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### obtener retroactividad:',json);
            if(json.exito)
            {
                feini.setMinValue(Ext.Date.add(new Date(),Ext.Date.DAY,(json.smap1.retroac-0)*-1));
                feini.setMaxValue(Ext.Date.add(new Date(),Ext.Date.DAY,json.smap1.diferi-0));
                feini.isValid();
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : errorComunicacion
    });
    
    if(_p29_smap1.cdramo+'x'=='5x')
    {
        Ext.Ajax.request(
        {
            url     : _p29_urlRecuperacionSimple
            ,params :
            {
                'smap1.procedimiento' : 'RECUPERAR_DATOS_VEHICULO_RAMO_5'
                ,'smap1.cdunieco'     : _p29_smap1.cdunieco
                ,'smap1.cdramo'       : _p29_smap1.cdramo
                ,'smap1.estado'       : _p29_smap1.estado
                ,'smap1.nmpoliza'     : _p29_smap1.nmpoliza 
            }
            ,success : function(response)
            {
                var json = Ext.decode(response.responseText);
                debug('### recuperar datos vehiculo ramo 5:',json);
                if(json.exito)
                {
                    var _f1_aux=
                    [
                        {
                            xtype       : 'displayfield'
                            ,fieldLabel : 'VEH&Iacute;CULO'
                            ,value      : json.smap1.datos
                        }
                    ];
                    var form=_fieldById('_p29_adicionalesForm');
                    var anteriores=form.removeAll(false);
                    form.add(
                    {
                        xtype       : 'displayfield'
                        ,fieldLabel : 'VEH&Iacute;CULO'
                        ,value      : json.smap1.datos
                    });
                    for(var i=0;i<anteriores.length;i++)
                    {
                        form.add(anteriores[i]);
                    }
                }
                else
                {
                    mensajeError(json.respuesta);
                }
            }
            ,failure : function(){ errorComunicacion(); }
        });
    }
}

function _p29_personaSaved(json)
{
    debug('>_p29_personaSaved');
    
    _nmOrdDomContratante = json.smap1.NMORDDOM;
    var datosContr = obtieneDatosClienteContratante();
    _contratanteSaved = true;
    
    
    storePersonasp2.each(function(recordContr,index){
        if(recordContr.get('estomador')==true){

        //  recordContr.set('nmsituac','0');
        //  recordContr.set('cdrol'   ,'1');
            recordContr.set('fenacimi', datosContr.fenacimi);
            recordContr.set('sexo'    , datosContr.sexo);
            recordContr.set('tpersona', datosContr.tipoper);
            recordContr.set('nacional', datosContr.naciona);
            recordContr.set('cdperson', datosContr.cdperson);
            recordContr.set('nombre'  , datosContr.nombre);
            recordContr.set('segundo_nombre'  , datosContr.snombre);
            recordContr.set('Apellido_Paterno', datosContr.appat);
            recordContr.set('Apellido_Materno', datosContr.apmat);
            recordContr.set('cdrfc'   , datosContr.rfc);
            recordContr.set('cdideper', datosContr.cdideper);
            recordContr.set('cdideext', datosContr.cdideext);
            recordContr.set('swexiper', 'S');
            
        }
    });
        
    debug('<_p29_personaSaved');
}
        
});
    
</script>
<%--
</head>
<body>
--%>
<div id="maindivasegurados" style="height:800px;"></div>
<%--
</body>
</html>
--%>