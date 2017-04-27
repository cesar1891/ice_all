<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
--%>
<script>
///////////////////////
////// variables //////
/*///////////////////*/
var inputCduniecop4        = '<s:property value="smap1.CDUNIECO" />';
var inputCdramop4          = '<s:property value="smap1.CDRAMO" />';
var inputEstadop4          = '<s:property value="smap1.ESTADO" />';
var inputNmpolizap4        = '<s:property value="smap1.NMPOLIZA" />';
var inputNmsuplemp4        = '<s:property value="smap1.NMSUPLEM" />';
var inputCdpersonp4        = '<s:property value="smap1.CDPERSON" />';
var inputCdrfcp4           = '<s:property value="smap1.CDRFC" escapeHtml="false" />';
var inputAsegurado         = '<s:property value="smap1.DSNOMBRE" escapeHtml="false" /> <s:property value="smap1.DSNOMBRE1" escapeHtml="false" /> <s:property value="smap1.DSAPELLIDO" escapeHtml="false" /> <s:property value="smap1.DSAPELLIDO1" escapeHtml="false" />';
var inputCdtipsit          = '<s:property value="smap1.CDTIPSIT" />';
var inputNtramite          = '<s:property value="smap1.NTRAMITE" />';
var tipoFlotilla           = '<s:property value="smap1.TIPOFLOT" />';
var inputFechaInicio       = new Date();
var urlRegresarp4          = '<s:url namespace="/"        action="editarAsegurados" />';
var urlCargarp4            = '<s:url namespace="/catalogos"  action="obtenerDomicilioContratante" />';
var urlGuardarp4           = '<s:url namespace="/endosos" action="guardarEndosoDomicilioAuto" />';
var enddomUrlDoc           = '<s:url namespace="/documentos" action="ventanaDocumentosPolizaClon" />';
var _ComboColoniasUrl      = '<s:url namespace="/catalogos" action="obtieneCatalogo" />';

var _p4_urlRecuperacionSimple = '<s:url namespace="/emision" action="recuperacionSimple" />';
var _cdTipSupCambioDom = '<s:property value="@mx.com.gseguros.portal.general.util.TipoEndoso@CAMBIO_DOMICILIO.cdTipSup" />';

var esElContratanteP4   = true;
var formPanelp4;

var datosIniciales;

debug('inputCduniecop4'        , inputCduniecop4);
debug('inputCdramop4'          , inputCdramop4);
debug('inputEstadop4'          , inputEstadop4);
debug('inputNmpolizap4'        , inputNmpolizap4);
debug('inputCdpersonp4'        , inputCdpersonp4);
debug('inputCdrfcp4'           , inputCdrfcp4);
debug('inputCdtipsit'          , inputCdtipsit);
debug('inputNtramite'          , inputNtramite);

var _endDomAuto_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
debug('_endDomAuto_flujo:',_endDomAuto_flujo);

/*///////////////////*/
////// variables //////
///////////////////////

///////////////////////
////// funciones //////
/*///////////////////*/

/*///////////////////*/
////// funciones //////
///////////////////////

Ext.onReady(function(){
    
	
	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
	
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('Modelo1p4',{
        extend     : 'Ext.data.Model',
        <s:property value="item1" />
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/

    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*/
    
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    formPanelp4=Ext.create('Ext.form.Panel',
    {
        renderTo    : 'maindivp4',
        border      : 0,
        buttonAlign : 'center',
        url         : urlGuardarp4,
        items       :
        [
            Ext.create('Ext.panel.Panel',
            {
                title         : 'Domicilio',
                collapsible   : true,
                titleCollapse : true,
                style         : 'margin:5px;',
                defaults      :
                {
                    style : 'margin : 5px;'
                },
                layout        :
                {
                    type    : 'table',
                    columns : 3
                },
                items:
                [
                    {
                        fieldLabel : 'Contratante',
                        xtype      : 'textfield',
                        width      : 400,
                        readOnly   : true,
                        name       : 'smap1.asegurado'
                    },
                    {
                        fieldLabel : 'RFC',
                        xtype      : 'textfield',
                        readOnly   : true,
                        name       : 'smap1.rfc'
                    }
                ]
            }),
            Ext.create('Ext.panel.Panel',
            {
                title           : 'Direcci&oacute;n',
                itemId          : 'panelDireccionp4',
                collapsible     : true,
                titleCollapse   : true,
                style           : 'margin:5px;',
                defaults        :
                {
                    style : 'margin:5px;'
                },
                layout          :
                {
                    type    : 'table',
                    columns : 2
                },
                items           :
                [
                    /*
                    {
                        fieldLabel     : 'Consecutivo',
                        xtype          : 'numberfield',
                        name           : 'smap1.NMORDDOM',
                        readOnly       : true
                    },
                    {
                        fieldLabel     : 'C&oacute;digo postal',
                        xtype          : 'textfield',
                        name           : 'smap1.CODPOSTAL',
                        readOnly       : true
                    },
                    {
                        xtype          : 'textfield',
                        name           : 'smap1.CDEDO',
                        hidden         : true
                    },
                    {
                        fieldLabel     : 'Estado / Ciudad',
                        xtype          : 'textfield',
                        name           : 'smap1.estado',
                        readOnly       : true
                    },
                    {
                        xtype          : 'textfield',
                        name           : 'smap1.CDMUNICI',
                        hidden         : true
                    },
                    {
                        fieldLabel     : 'Delegaci&oacute;n / Municipio',
                        xtype          : 'textfield',
                        name           : 'smap1.Municipio',
                        readOnly       : true
                    },
                    {
                        fieldLabel     : 'Tel&eacute;fono',
                        xtype          : 'textfield',
                        name           : 'smap1.NMTELEFO',
                        allowBlank     : true,
                        readOnly       : !esElContratanteP4
                    },{
                        xtype:'combo',
                        id:'coloniaId',//id
                        name:'smap1.CDCOLONI',
                        fieldLabel:'Colonia',
                        displayField: 'value',
                        valueField: 'key',
                        readOnly: !esElContratanteP4,
                        store:Ext.create('Ext.data.Store', {
                            model:'Generic',
                            autoLoad:false,
                            proxy:
                            {
                                type: 'ajax',
                                url: _ComboColoniasUrl,
                                reader:
                                {
                                    type: 'json',
                                    root: 'lista'
                                }
                            }
                        }),
                        editable:true,
                        queryMode:'local',
                        style:'margin:5px;',
                        allowBlank:false
                    },
                    {
                        fieldLabel     : 'Calle',
                        xtype          : 'textfield',
                        name           : 'smap1.DSDOMICI',
                        allowBlank     : false,
                        readOnly       : !esElContratanteP4
                    },
                    {
                        fieldLabel     : 'Exterior',
                        xtype          : 'textfield',
                        name           : 'smap1.NMNUMERO',
                        maxLength      : 10,
                        allowBlank     : false,
                        readOnly       : !esElContratanteP4
                    },
                    {
                        fieldLabel     : 'Interior',
                        xtype          : 'textfield',
                        name           : 'smap1.NMNUMINT',
                        maxLength      : 10,
                        allowBlank     : true,
                        readOnly       : !esElContratanteP4
                    }
                    */
                    <s:property value="item3" />
                ]
            })
            ,Ext.create('Ext.panel.Panel',
            {
            	title     : 'Informaci&oacute;n del endoso'
            	,style    : 'margin : 5px;'
            	,defaults :
            	{
            		style : 'margin : 5px;'
            	}
            	,layout   :
            	{
            		type     : 'table'
            		,columns : 2
            	}
            	,items    :
            	[
					{
					    xtype       : 'datefield'
					    ,fieldLabel : 'Fecha de efecto'
					    ,format     : 'd/m/Y'
					    ,allowBlank : false
					    ,name       : 'smap2.pv_fecha_i'
					}
            	]
            })
        ],
        buttons:
        [
            {
                text:'Generar endoso',
                icon: '${ctx}/resources/fam3icons/icons/key.png',
                handler:function()
                {
                    if(this.up().up().getForm().isValid())
                    {
                        var panelMask = new Ext.LoadMask('maindivp4', {msg:"Confirmando..."});
                        
                        var paramsSubmit =
                        {
                            'smap1.pv_cdunieco' : inputCduniecop4,
                            'smap1.pv_cdramo'   : inputCdramop4,
                            'smap1.pv_estado'   : inputEstadop4,
                            'smap1.pv_nmpoliza' : inputNmpolizap4,
                            'smap1.pv_nmsuplem_i': inputNmsuplemp4,
                            'smap1.pv_nmsituac' : '0',
                            'smap1.pv_cdperson' : inputCdpersonp4,
                            'smap1.pv_cdrol'    : '1',
                            'smap1.NMORDDOM'    : _nmOrdDomEnd().getValue(),
                            'smap1.TIPOFLOT'    : tipoFlotilla,
                            'smap1.NTRAMITE'    : inputNtramite,
                            'smap2.cdtipsit'    : inputCdtipsit,
                            'smap3.cdperson'    : inputCdpersonp4,
                            'smap3.calle'       : datosIniciales.data['smap1.DSDOMICI'],
                            'smap3.cp'          : datosIniciales.data['smap1.CODPOSTAL'],
                            'smap3.numext'      : datosIniciales.data['smap1.NMNUMERO'],
                            'smap3.numint'      : datosIniciales.data['smap1.NMNUMINT'],
                            'smap3.cdedo'       : datosIniciales.data['smap1.CDEDO'],
                            'smap3.cdmunici'    : datosIniciales.data['smap1.CDMUNICI'],
                            'smap3.cdcoloni'    : datosIniciales.data['smap1.CDCOLONI']
                        };
                        
                        if(!Ext.isEmpty(_endDomAuto_flujo))
                        {
                            paramsSubmit['flujo.ntramite']  = _endDomAuto_flujo.ntramite;
                            paramsSubmit['flujo.status']    = _endDomAuto_flujo.status;
                            paramsSubmit['flujo.cdtipflu']  = _endDomAuto_flujo.cdtipflu;
                            paramsSubmit['flujo.cdflujomc'] = _endDomAuto_flujo.cdflujomc;
                            paramsSubmit['flujo.webid']     = _endDomAuto_flujo.webid;
                            paramsSubmit['flujo.tipoent']   = _endDomAuto_flujo.tipoent;
                            paramsSubmit['flujo.claveent']  = _endDomAuto_flujo.claveent;
                            paramsSubmit['flujo.cdunieco']  = _endDomAuto_flujo.cdunieco;
                            paramsSubmit['flujo.cdramo']    = _endDomAuto_flujo.cdramo;
                            paramsSubmit['flujo.estado']    = _endDomAuto_flujo.estado;
                            paramsSubmit['flujo.nmpoliza']  = _endDomAuto_flujo.nmpoliza;
                            paramsSubmit['flujo.nmsituac']  = _endDomAuto_flujo.nmsituac;
                            paramsSubmit['flujo.nmsuplem']  = _endDomAuto_flujo.nmsuplem;
                            paramsSubmit['flujo.aux']       = _endDomAuto_flujo.aux;
                        }
                        
						panelMask.show();
                        this.up().up().getForm().submit(
                        {
                            params   : paramsSubmit
                            ,success : function(response,opts)
                            {
                                panelMask.hide();
                                var json=Ext.decode(opts.response.responseText);
                            	//////////////////////////////////
                                ////// usa codigo del padre //////
                                /*//////////////////////////////*/
//                                marendNavegacion(2);
                                /*//////////////////////////////*/
                                ////// usa codigo del padre //////
                                //////////////////////////////////
                                
                                var callbackRemesa = function()
                                {
                                    marendNavegacion(2);
                                };
                                
                                Ext.Msg.show({
                                    title:'Endoso generado',
                                    msg: json.mensaje,
                                    buttons: Ext.Msg.OK
                                    ,fn    : function()
                                    {
                                        _generarRemesaClic(
                                            true
                                            ,inputCduniecop4
                                            ,inputCdramop4
                                            ,inputEstadop4
                                            ,inputNmpolizap4
                                            ,callbackRemesa
                                        );
                                    }
                                });
                            },
                            failure:function(response,opts)
                            {
                                panelMask.hide();
                            	var json=Ext.decode(opts.response.responseText);
                                mensajeError(json.error);
                            }
                        });
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
            /*,{
                text     : 'Documentos'
                ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                ,handler : function()
                {
                    Ext.create('Ext.window.Window',
                    {
                        title        : 'Documentos del tr&aacute;mite '+inputNtramite
                        ,modal       : true
                        ,buttonAlign : 'center'
                        ,width       : 600
                        ,height      : 400
                        ,autoScroll  : true
                        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                        ,loader      :
                        {
                            url       : enddomUrlDoc
                            ,params   :
                            {
                                'smap1.nmpoliza'  : inputNmpolizap4
                                ,'smap1.cdunieco' : inputCduniecop4
                                ,'smap1.cdramo'   : inputCdramop4
                                ,'smap1.estado'   : inputEstadop4
                                ,'smap1.nmsuplem' : '0'
                                ,'smap1.ntramite' : inputNtramite
                                ,'smap1.nmsolici' : ''
                                ,'smap1.tipomov'  : '0'
                            }
                            ,scripts  : true
                            ,autoLoad : true
                        }
                    }).show();
                }
            }*/
        ]
    });
    
    
    _comboEstadoEnd().forceSelection = false;
    _comboMunicipioEnd().forceSelection = false;
    
    _codPosEnd().setReadOnly(true);
    
    _comboEstadoEnd().setReadOnly(true);
    _comboMunicipioEnd().setReadOnly(true);
    _comboColoniasEnd().setReadOnly(true);
    
    
//    if(!_p4_habilitaEdicion)//si es asegurado solo puede leer cp, estado y municipio
//    {
//        _comboEstadoEnd().setReadOnly(true);//estado
//        _comboMunicipioEnd().setReadOnly(true);//municipio
//    }
    
    //establecer cargar colonia al cambiar cod pos
    _codPosEnd().on('blur',function()
    {
        debug('cod pos change');
        _comboColoniasEnd().getStore().load(
        {
            params :
            {
                'params.cp' : _codPosEnd().getValue()
            }
            ,callback : function()
            {
                var hay=false;
                _comboColoniasEnd().getStore().each(function(record)
                {
                    if(_comboColoniasEnd().getValue()==record.get('key'))
                    {
                        hay=true;
                    }
                });
                if(!hay)
                {
                    _comboColoniasEnd().setValue('');
                }
            }
        });
    });
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    Ext.define('LoaderFormp4',
    {
        extend:'Modelo1p4',
        proxy:
        {
            extraParams:
            {
                'smap1.pv_cdunieco_i'   : inputCduniecop4,
                'smap1.pv_cdramo_i'     : inputCdramop4,
                'smap1.pv_estado_i'     : inputEstadop4,
                'smap1.pv_nmpoliza_i'   : inputNmpolizap4,
                'smap1.pv_nmsuplem_i'   : inputNmsuplemp4,
                'smap1.pv_nmsituac_i'   : '0',
                'smap1.pv_cdperson_i'   : inputCdpersonp4,
                'smap1.pv_nmorddom_i'   : '',// obtiene el asignado a la poliza
                'smap1.pv_cdrol_i'      : '1',
                'smap1.nombreAsegurado' : '',
                'smap1.cdrfc'           : inputCdrfcp4,
                'smap1.pv_cdtipsit_i'   : inputCdtipsit,
                'smap1.domGeneral'      : 'S'
            },
            type:'ajax',
            url : urlCargarp4,
            reader:
            {
                type:'json'
            }
        }
    });

    var loaderFormp4=Ext.ModelManager.getModel('LoaderFormp4');
    loaderFormp4.load(123, {
        success: function(resp) {
            debug(resp);
            resp.data['smap1.asegurado'] = inputAsegurado;
            resp.data['smap1.rfc']       = inputCdrfcp4;
            
            debug('Valores a Cargar: ' , resp);
            
            datosIniciales = resp;
           
            _comboColoniasEnd().getStore().load(
            {
                params :
                {
                    'params.cp' : resp.data['smap1.CODPOSTAL']
                },
                 callback: function(records, operation, success) {
                 	formPanelp4.loadRecord(resp);
           			heredarPanel(formPanelp4);
           			//formPanelp4.loadRecord(resp);
			    }
            });
            
        },
        failure:function()
        {
            Ext.Msg.show({
                title:'Error',
                icon: Ext.Msg.ERROR,
                msg: 'Error al cargar',
                buttons: Ext.Msg.OK
            });
        }
    });
    
    
    _fieldByName('smap1.NMNUMERO').regex = /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/;
    _fieldByName('smap1.NMNUMERO').regexText = 'Solo d&iacute;gitos, letras, espacios y guiones';
    _fieldByName('smap1.NMNUMINT').regex = /^[A-Za-z\u00C1\u00C9\u00CD\u00D3\u00DA\u00E1\u00E9\u00ED\u00F3\u00FA\u00F1\u00D10-9-\s]*$/;
    _fieldByName('smap1.NMNUMINT').regexText = 'Solo d&iacute;gitos, letras, espacios y guiones';
    
    Ext.ComponentQuery.query('[name=smap1.NMTELEFO]')[Ext.ComponentQuery.query('[name=smap1.NMTELEFO]').length-1].hide();
    
    
    Ext.Ajax.request(
    {
        url      : _p4_urlRecuperacionSimple
        ,params  :
        {
            'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
            ,'smap1.cdunieco'     : inputCduniecop4
            ,'smap1.cdramo'       : inputCdramop4
            ,'smap1.estado'       : inputEstadop4
            ,'smap1.nmpoliza'     : inputNmpolizap4
            ,'smap1.cdtipsup'     : _cdTipSupCambioDom
        }
        ,success : function(response)
        {
            var json = Ext.decode(response.responseText);
            debug('### fechas:',json);
            if(json.exito)
            {
                _fieldByName('smap2.pv_fecha_i',formPanelp4).setMinValue(json.smap1.FECHA_MINIMA);
                _fieldByName('smap2.pv_fecha_i',formPanelp4).setMaxValue(json.smap1.FECHA_MAXIMA);
                _fieldByName('smap2.pv_fecha_i',formPanelp4).setValue(json.smap1.FECHA_REFERENCIA);
                _fieldByName('smap2.pv_fecha_i',formPanelp4).setReadOnly(json.smap1.EDITABLE=='N');
                _fieldByName('smap2.pv_fecha_i',formPanelp4).isValid();
            }
            else
            {
                mensajeError(json.respuesta);
            }
        }
        ,failure : function()
        {
            errorComunicacion();
        }
    });
    
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
    
    function _nmOrdDomEnd(){
	    return Ext.ComponentQuery.query('[name=smap1.NMORDDOM]')[Ext.ComponentQuery.query('[name=smap1.NMORDDOM]').length-1];
	}
    function _codPosEnd(){
	    return Ext.ComponentQuery.query('[name=smap1.CODPOSTAL]')[Ext.ComponentQuery.query('[name=smap1.CODPOSTAL]').length-1];
	}
	function _comboEstadoEnd(){
	    return Ext.ComponentQuery.query('[name=smap1.CDEDO]')[Ext.ComponentQuery.query('[name=smap1.CDEDO]').length-1];
	}
	function _comboMunicipioEnd(){
	    return Ext.ComponentQuery.query('[name=smap1.CDMUNICI]')[Ext.ComponentQuery.query('[name=smap1.CDMUNICI]').length-1];
	}
	function _comboColoniasEnd(){
	    return Ext.ComponentQuery.query('[name=smap1.CDCOLONI]')[Ext.ComponentQuery.query('[name=smap1.CDCOLONI]').length-1];
	}

});
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<%--
    </head>
    <body>
--%>
        <div id="maindivp4" style="height:600px;"></div>
<%--
    </body>
</html>
--%>