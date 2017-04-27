<%@ include file="/taglibs.jsp"%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var pantallaValositMainContent;
var pantallaValositUrlLoad                  = '<s:url namespace="/"        action="pantallaValositLoad"        />';
var pantallaValositUrlSave                  = '<s:url namespace="/"        action="pantallaValositSave"        />';
var pantallaValositUrlValidarCambioZonaGMI  = '<s:url namespace="/emision" action="validarCambioZonaGMI"       />';
var pantallaValositUrlValidarEnfermCatasGMI = '<s:url namespace="/emision" action="validarEnfermedadCatastGMI" />';

var pantallaValositInputCdunieco = '<s:property value="smap1.cdunieco" />';
var pantallaValositInputCdramo   = '<s:property value="smap1.cdramo" />';
var pantallaValositInputEstado   = '<s:property value="smap1.estado" />';
var pantallaValositInputNmpoliza = '<s:property value="smap1.nmpoliza" />';
var pantallaValositInputCdtipsit = '<s:property value="smap1.cdtipsit" />';
var pantallaValositInputAgrupado = '<s:property value="smap1.agrupado" />';
var pantallaValositInputNmsituac = '<s:property value="smap1.nmsituac" />';
debug('A. Variables declaradas:');
debug('pantallaValositInputCdunieco:' , pantallaValositInputCdunieco);
debug('pantallaValositInputCdramo:'   , pantallaValositInputCdramo);
debug('pantallaValositInputEstado:'   , pantallaValositInputEstado);
debug('pantallaValositInputNmpoliza:' , pantallaValositInputNmpoliza);
debug('pantallaValositInputCdtipsit:' , pantallaValositInputCdtipsit);
debug('pantallaValositInputAgrupado:' , pantallaValositInputAgrupado);
debug('pantallaValositInputNmsituac:' , pantallaValositInputNmsituac);
debug("timestam struts: <s:property value='smap1.timestamp' />");
/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/
debug('B. Funciones declaradas');
/*///////////////////*/
////// funciones //////
///////////////////////
Ext.onReady(function()
{
	debug('0. Ready');
	
	/////////////////////
	////// modelos //////
	/*/////////////////*/
	Ext.define('ModeloValisit',
	{
        extend:'Ext.data.Model'
        ,<s:property value="item1" />
    });
	debug('1. Modelos definidos');
    /*/////////////////*/	
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    debug('2. Stores definidos');
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    Ext.define('PanelValosit',
    {
    	extend       : 'Ext.form.Panel'
    	,url         : pantallaValositUrlSave
    	,border      : 0
    	,buttonAlign : 'center'
    	,layout      :
    	{
    		type     : 'table'
    		,columns : 2
    	}
        ,defaults    :
        {
        	style : 'margin:5px;'
        }
        ,<s:property value="item2" />
        ,buttons     :
        [
            {
            	text     : 'Guardar'
	            ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
	            ,handler : function (me) {
	                var mask, ck = 'Guardando datos variables de situaci\u00f3n';
	                try {
		            	var form = pantallaValositMainContent;
		            	debug(me);
		            	if (form.isValid()) {
		            		mask = _maskLocal(ck);
		            		var values = Object.assign(form.getValues(), {
		            		    'smap1.cdunieco' : pantallaValositInputCdunieco,
                                'smap1.cdramo'   : pantallaValositInputCdramo,
                                'smap1.estado'   : pantallaValositInputEstado,
                                'smap1.nmpoliza' : pantallaValositInputNmpoliza,
                                'smap1.cdtipsit' : pantallaValositInputCdtipsit,
                                'smap1.agrupado' : pantallaValositInputAgrupado,
                                'smap1.nmsituac' : pantallaValositInputNmsituac
		            		});
		            		debug('values:', values);
		            		Ext.Ajax.request({
		            		    url     : pantallaValositUrlSave,
		            		    params  : values,
		            		    success : function (response) {
		            		        mask.close();
		            		        var ck = 'Decodificando respuesta al guardar cambios';
		            		        try {
		            		            var json = Ext.decode(response.responseText);
		            		            debug('AJAX json guardar valosit:', json);
		            		            if (json.success !== true) {
		            		                throw json.respuesta;
		            		            }
		            		            try {
		            		                window.parent.scrollTo(0,0);
		            		            } catch (e) {}
		            		            try {
		            		                expande(2);
		            		            } catch (e) {}
		            		        } catch (e) {
		            		            manejaException(e, ck);
		            		        }
	                            },
	                            failure : function () {
	                                mask.close();
	                                errorComunicacion(null, 'Error al guardar datos variables de situaci\u00f3n');
	                            }
	                        });
		            	} else {
		            		throw 'Favor de llenar los campos requeridos';
		            	}
		            } catch (e) {
		                manejaException(e, ck, mask);
		            }
	            }
            }
        ]
    });
    debug('3. Componentes definidos');
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    pantallaValositMainContent=new PanelValosit();
    pantallaValositMainContent.render('maindivpantallavalosit<s:property value='smap1.timestamp' />');
    debug('4. Contenido cargado');
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    Ext.define('LoaderForm',
    {
        extend:'ModeloValisit',
        proxy:
        {
            extraParams:
            {
            	'smap1.pv_cdunieco_i'  : pantallaValositInputCdunieco
            	,'smap1.pv_nmpoliza_i' : pantallaValositInputNmpoliza
            	,'smap1.pv_cdramo_i'   : pantallaValositInputCdramo
            	,'smap1.pv_estado_i'   : pantallaValositInputEstado
            	,'smap1.pv_nmsituac_i' : pantallaValositInputAgrupado=='si'?'1':pantallaValositInputNmsituac
            },
            type:'ajax',
            url : pantallaValositUrlLoad,
            reader:{
                type:'json'
            }
        }
    });

    var loaderForm=Ext.ModelManager.getModel('LoaderForm');
    loaderForm.load(123, {
        success: function(resp) {
            //console.log(resp);
            pantallaValositMainContent.loadRecord(resp);
        },
        failure:function()
        {
            Ext.Msg.show({
                title:'Error',
                icon: Ext.Msg.ERROR,
                msg: 'Error al cargar datos de cotizaci&oacute;n',
                buttons: Ext.Msg.OK
            });
        }
    });
    
    //parche para ramo 6
    if(pantallaValositInputCdramo+'x'=='6x'
        &&pantallaValositInputAgrupado=='si')
    {
        expande(2);
    
        //banco -> meses
        _fieldByName('parametros.pv_otvalor18').on(
        {
            'select' : function()
            {
                if(_fieldByName('parametros.pv_otvalor18').getValue()+'x'=='0x')
                {
                    _fieldByName('parametros.pv_otvalor19').allowBlank=true;
                }
                else
                {
                    _fieldByName('parametros.pv_otvalor19').allowBlank=false;
                }
                _fieldByName('parametros.pv_otvalor19').isValid();
            }
        });
    }
    
    if(pantallaValositParche!=false)
    {
        panDatComAux1=panDatComAux1+1;
        if(panDatComAux1==2)
        {
            pantallaValositParche();
        }
        else
        {
            debug('tvalosit>todavia no se parcha tvalosit');
        }
    }
    
    /*//////////////////*/    
    ////// cargador //////
    //////////////////////
    
    ////// custom //////
    if(pantallaValositInputCdramo=='7'&&pantallaValositInputCdtipsit=='GMI'&&pantallaValositInputAgrupado=='si')
    {
        _fieldLikeLabel('POSTAL',pantallaValositMainContent).on(
        {
            select : function(v,records)
            {
                debug('POSTAL select:',records[0].get('value'));
                Ext.Ajax.request(
                {
                    url     : pantallaValositUrlValidarCambioZonaGMI
                    ,params :
                    {
                        'smap1.cdramo'     : pantallaValositInputCdramo
                        ,'smap1.cdtipsit'  : pantallaValositInputCdtipsit
                        ,'smap1.codpostal' : records[0].get('value')
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### validar eliminacion cambio zona:',json);
                        if(json.exito)
                        {
                            _fieldLikeLabel('CAMBIO DE ZONA',pantallaValositMainContent).reset();
                            _fieldLikeLabel('CAMBIO DE ZONA',pantallaValositMainContent).show();
                        }
                        else
                        {
                            _fieldLikeLabel('CAMBIO DE ZONA',pantallaValositMainContent).setValue('N');
                            _fieldLikeLabel('CAMBIO DE ZONA',pantallaValositMainContent).hide();
                        }
                    }
                    ,failure : function()
                    {
                        errorComunicacion();
                    }
                });
            }
        });
        
        _fieldLikeLabel('CULO HOSPITALARIO',pantallaValositMainContent).on(
        {
            select : function(v,records)
            {
                debug('CIRCULO HOSP select:',records[0].get('value'));
                Ext.Ajax.request(
                {
                    url     : pantallaValositUrlValidarEnfermCatasGMI
                    ,params :
                    {
                        'smap1.cdramo'    : pantallaValositInputCdramo
                        ,'smap1.circHosp' : records[0].get('value')
                    }
                    ,success : function(response)
                    {
                        var json=Ext.decode(response.responseText);
                        debug('### validar enfermedad catastrofica:',json);
                        if(json.exito)
                        {
                            _fieldLikeLabel('ENFERMEDAD CATAS',pantallaValositMainContent).reset();
                            _fieldLikeLabel('ENFERMEDAD CATAS',pantallaValositMainContent).show();
                        }
                        else
                        {
                            _fieldLikeLabel('ENFERMEDAD CATAS',pantallaValositMainContent).setValue('N');
                            _fieldLikeLabel('ENFERMEDAD CATAS',pantallaValositMainContent).hide();
                        }
                    }
                    ,failure : function()
                    {
                        errorComunicacion();
                    }
                });
            }
        });
    }
    
    if (pantallaValositInputCdramo == Ramo.GastosMedicosMayoresPrueba
        && pantallaValositInputCdtipsit == TipoSituacion.GastosMedicosMayoresPruebaInd) {
        if (!Ext.isEmpty(_fieldByLabel('PLAN', pantallaValositMainContent, true))
            && !Ext.isEmpty(_fieldByLabel('DEDUCIBLE', pantallaValositMainContent, true))) {
            var planCmp = _fieldByLabel('PLAN', pantallaValositMainContent, true),
                deduCmp = _fieldByLabel('DEDUCIBLE', pantallaValositMainContent, true);
            deduCmp.anidado = true;
            deduCmp.heredar = function () {
                deduCmp.getStore().load({
                    params : {
                        'params.idPadre' : planCmp.getValue()
                    }
                });
            };
        }
    }
    ////// custom //////
});
</script>
<div id="maindivpantallavalosit<s:property value='smap1.timestamp' />" style="height:1500px;"></div>