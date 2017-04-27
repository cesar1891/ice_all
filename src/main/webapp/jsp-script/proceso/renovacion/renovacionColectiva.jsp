<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// variables //////
var contexto					= '${ctx}';
var _renovarPolizas              = '<s:url namespace="/renovacion"  action="renovarColectivo" />';

var sesionDsrol   						      = _GLOBAL_CDSISROL;

////// variables //////

////// componentes dinamicos //////
var renova = Ext.create('Ext.data.Store', {
        fields: ['abbr', 'name'],
        data : [
            {"abbr":"RenPoliza", "name":"Renovacion por Poliza"},
            {"abbr":"RenFecha", "name":"Renovacion por Rango"}
        ]
    });
////// componentes dinamicos //////

Ext.onReady(function()
{
    Ext.Ajax.timeout = 60*60*1000;
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    ////// modelos //////
    ////// modelos //////
    
    ////// stores //////

    ////// stores //////
    
    ////// componentes //////
    
    ////// contenido //////
    
    Ext.create('Ext.panel.Panel',
    {
        renderTo    : '_p25_divpri'
        ,itemId     : 'formBusqueda'
        ,defaults   : { style : 'margin : 5px;' }
        ,border     : 0
        ,autoScroll : true
        ,items      : [
            Ext.create('Ext.form.Panel',{
                title     : 'Buscar p&oacute;lizas a renovar'
                ,itemId   : '_p25_busquedaForm'
                ,defaults : { style : 'margin :5px;' }
                ,layout   : {
                    type     : 'table'
                    ,columns : 3
                }
                ,items       : [{
									xtype : 'combobox',
									fieldLabel : 'Tipo Renovacion',
									labelAlign : 'left',
									store : new Ext.data.SimpleStore({
												data : [['COLECTIVO', 'RENOVACIÓN POR PÓLIZA'],
														['RENOVAR_X_FECHAS', 'RENOVACIÓN POR RANGO']],
												id : 'COLECTIVO',
												fields : ['value', 'text']
											}),
									valueField : 'value',
									value: 'COLECTIVO',
									displayField : 'text',
									editable : false,
									name : 'division',
									width: 250
									/*itemId : 'stationStatusReportClosedStatus',
									flex : 1,
									listeners : {
										'select' : function(combo, record) {
											// we can get the selected value using getValue()
											closedStatusSelectedID = this.getValue();
										}
									}*/
								},{
									xtype : 'combobox',
									fieldLabel : 'Sucursal',
									labelAlign : 'left',
									store : new Ext.data.SimpleStore({
												data : [['1000', '1000 - SALUD MATRIZ']],
												id : '1000',
												fields : ['value', 'text']
											}),
									valueField : 'value',
									value: '1000',
									displayField : 'text',
									editable : false,
									name : 'Sucursal',
									width: 250
								},{
									xtype : 'combobox',
									fieldLabel : 'Ramo',
									labelAlign : 'left',
									store : new Ext.data.SimpleStore({
												data : [['4', 'MULTISALUD'],
														['11', 'GASTOS MEDICOS MAYORES PRUEBA']],
												id : '4',
												fields : ['value', 'text']
											}),
									valueField : 'value',
									value: '4',
									displayField : 'text',
									editable : false,
									name : 'Ramo',
									width: 350
									/*itemId : 'stationStatusReportClosedStatus',
									flex : 1,
									listeners : {
										'select' : function(combo, record) {
											// we can get the selected value using getValue()
											closedStatusSelectedID = this.getValue();
										}
									}*/
								},{
							       xtype: 'textfield',
							        name: 'Poliza',
							        fieldLabel: 'No. Poliza',
							        allowBlank: false
						        },{
							        xtype: 'datefield',
							        anchor: '100%',
							        fieldLabel: 'Fecha Inicio',
							        name: 'feDesde',
							        value : new Date()
							        //maxValue: new Date()	
						        },{
			        				xtype: 'datefield',
							        anchor: '100%',
							        fieldLabel: 'Fecha Fin',
							        name: 'feHasta',
							        //maxValue: new Date()
							        value : new Date()
			        			}]
                ,buttonAlign : 'center'
                ,buttons     :
                [
                    {
                        text     : 'Renovar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : function(){
                        	 var ck = 'Renovando......';
                            _mask(ck); //Para bloquear pantalla
                            
                        	Ext.Ajax.request({
				        		url       : _renovarPolizas,
				        		params     : 
				        		{
				        			'params.cdunieco'       : _fieldByName('Sucursal').getValue()
									,'params.cdramo'        : _fieldByName('Ramo').getValue()
									,'params.nmpoliza'      : _fieldByName('Poliza').getValue()
									,'params.fecdesde'      : _fieldByName('feDesde').getValue()
									,'params.fechasta'      : _fieldByName('feHasta').getValue()
									,'params.procedimiento' : _fieldByName('division').getValue()
				        		},
				        		success  : function(response)
				        		{
				                	_unmask();
				            		var resp = Ext.decode(response.responseText);
				            		debug('resp: ',resp);
				            		if(resp.exito==true){
					                    mensajeCorrecto(
					                        'Renovación'
					                        ,'La Póliza ha sido Renovada')
				            		}
				            		else{
				            			mensajeError(resp.respuesta);
				            		}
				        		},
				        		failure  : function()
				        		{
				        			_unmask();
				            		errorComunicacion();
				        		}
			    		});
                        	
                        	
                        }
                    },
                    {
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat.png'
                        ,handler : function (){}
                    }
                ]
                ,listeners	:	{
                	click	:	function(options) {
                		debug('click panel');
                		}
                }
            })
        ]
    });
    ////// contenido //////
    
    ////// custom //////
    var form = _fieldById('_p25_busquedaForm');
    
    _fieldByName('division',form).getStore().on({
        load : function(me){
  		    var division = _fieldByName('division',form);
  		    division.setValue('COLECTIVO');  		    
  		    if(true){
  		        debug('division',division);
  		        division.store.removeAt(2);
  		    }  		    
  	    }
  	});
  	    
  	_fieldByName('division',form).on({
  	    change: function(me, newValue) {
  	    	debug(me, newValue);
  	    	
  	    	debug('Sucursal',_fieldByName('Sucursal').getValue());
			debug('Ramo'    ,_fieldByName('Ramo').getValue()    );
			debug('Poliza'  ,_fieldByName('Poliza').getValue()  );
			debug('feDesde' ,_fieldByName('feDesde').getValue() );
			debug('feHasta' ,_fieldByName('feHasta').getValue() );
			debug('division',_fieldByName('division').getValue());
			
  		    if(me.getValue() == 'COLECTIVO'){
  			    _fieldByName('feDesde',form).hide();
  				_fieldByName('feHasta',form).hide();
  				
  				_fieldByName('Sucursal',form).show();
  				_fieldByName('Ramo',form).show();
  				_fieldByName('Poliza',form).show();
  				
  				form.doLayout();
  		    }
  		    else if(me.getValue() == 'RENOVAR_X_FECHAS'){
  			    _fieldByName('feDesde',form).show();
  				_fieldByName('feHasta',form).show();
  				
  				_fieldByName('Sucursal',form).hide();
  				_fieldByName('Ramo',form).hide();
  				_fieldByName('Poliza',form).hide();
  				
  				form.doLayout();
  			}
  	    }
  	});
  	
  	
    ////// custom //////
    
    ////// loaders //////
  	_fieldByName('feDesde',form).hide();
  	_fieldByName('feHasta',form).hide();
    ////// loaders //////
});

////// funciones //////

function _p25_limpiarFiltros(button,e)
{
    debug('>_p25_limpiarFiltros');
    var form = button.up('form');
    _fieldByName('tipo', form).enable();
    _fieldByName('anio', form).enable();
    _fieldByName('mes',  form).enable();
    var cdperson = _fieldByName('cdperson',form);
  	cdperson.store.proxy.extraParams['params.cdunieco'] = '';
  	cdperson.store.proxy.extraParams['params.cdramo']   = ''; 
    _fieldByName('fecini',form).setValue('');
  	_fieldByName('fecfin',form).setValue('');
  	_fieldByName('status',form).setValue('');
  	_fieldByName('cdunieco',form).setValue('');
  	_fieldByName('cdramo',form).setValue('');
  	_fieldByName('nmpoliza',form).setValue('');
  	_fieldByName('cdtipsit',form).setValue('');
  	_fieldByName('administradora',form).setValue('');
  	_fieldByName('retenedora',form).setValue('');
  	_fieldByName('cdperson',form).setValue('');
  	_fieldByName('anio',form).setValue('');
  	//_fieldByName('mes',form).setValue('');
    debug('<_p25_limpiarFiltros');
}

 function limpiarValoresCalendario(form){
    debug('>limpiarValoresCalendario', form);    
    _fieldByName('cdunieco',form).setValue('');
    _fieldByName('cdramo',  form).setValue('');
    _fieldByName('feaplica',form).setValue('');
    _fieldByName('feinicio',form).setValue('');
    _fieldByName('fefinal', form).setValue('');
    debug('<limpiarValoresCalendario');
 }

 function daydiff(first, second) {
     return Math.round((second-first)/(1000*60*60*24));
 }
 
 function isValidDate(date){
     debug('>isValidDate',date);
     var isDate = false;
     var dateArr = date.split('/');
     if(dateArr.length === 3){
        isDate = Ext.Date.isValid(dateArr[2],dateArr[1],dateArr[0]);
     }
     debug('<isValidDate',isDate);
     return isDate;
 }

 function toDate(year, month, day){
     debug('>toDate');
     var fecha;
     try{
        var mes = Number(month)-1;
        fecha = new Date(year, mes, day);
     }
     catch(err){
        debug('error al convertir fecha',err);
        return null;
     } 
     return fecha;
     debug('<toDate');
 }


////// funciones //////
</script>
</head>
<body><div id="_p25_divpri" style="height:600px;"></div></body>
</html>