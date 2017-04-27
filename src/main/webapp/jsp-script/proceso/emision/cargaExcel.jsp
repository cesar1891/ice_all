<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// overrides //////
////// overrides //////
var _p30_urlCargaMasiva  = '<s:url namespace="/cargamasiva"         action="procesarCargaMasivaRecuperaInd" />';
var _p30_urlGeneraPoliza = '<s:url namespace="/cargamasiva"         action="generarPolizasRecuperaInd" />';

//MODELO//
Ext.define('_p21_modeloRevisionAsegurado',
	    {
	        extend  : 'Ext.data.Model'
	        ,fields :
	        [
	            'CDUNIECO'
	            ,'SUCURSAL'
	            ,'POLIZA'
	            ,'NOMBRE1'
	            ,'NOMBRE2'
	            ,'APEPAT'
	            ,'APEMAT'
	            ,'PRODUCTO'
	            ,'PLAN'
	            ,'ESQUEMA'
	            ,'PARENTESCO'
	            ,'FECNAC'
	            ,'RFC'
	            ,'SEXO'
	            ,'PESO'
	            ,'ESTATURA'
	            ,'FECINIVIG'
	            ,'MEMBRESIA'	
	            ,'Nombre_Completo'
	        ]
	    });
Ext.onReady(function(){

	Ext.create('Ext.form.Panel', {
	    title: 'CARGA DE ARCHIVOS SALUD',
	    bodyPadding: 5,
	    width: 350,
	    layout: 'anchor', // Fields will be arranged vertically, stretched to full width
	    defaults: {
	        anchor: '100%'
        },
	    items: [{
            xtype        : 'filefield',
            buttonOnly   : true,
            style        : 'margin:0px;',
            name         : 'file',
            style        : 'background:#223772;',
            buttonConfig : {
                text  : 'Carga masiva...',
                icon : '${ctx}/resources/fam3icons/icons/book_next.png'
            },
            listeners : {
                change : function(me) {   
                    var descripcion ='';
                    var msnIncInv='';
                    var indexofPeriod = me.getValue().lastIndexOf(".");
                    var uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod).toLowerCase();
                    debug('uploadedExtension:',uploadedExtension);
                    var valido = Ext.Array.contains(['xls','xlsx'], uploadedExtension);
                    if(!valido) {
                        mensajeWarning('Solo se permiten hojas de c&aacute;lculo');
                        me.reset();
                    } else {
                   	         me.up('form').submit({
	                        url : _p30_urlCargaMasiva
                   	    ,success :  function (form, action) {
                            debug('success:', action.response);
                            var ck , mask= 'Decodificando respuesta al recuperar p\u00f3liza para renovar';
                            try {
                                var json = Ext.decode(action.response.responseText);
                                debug("### peticion poliza SIGS: ", json);
                                debug("### peticion poliza SIGS smap1: ", json.smap1);
                                debug("### peticion poliza SIGS slist1: ", json.slist1);
                                //if (!Ext.isEmpty(json.smap1.ERROR)) {
                                	//mensajeError(json.smap1.ERROR);
                                	
                                	var store = Ext.create('Ext.data.Store',
                                            {
                                                model : '_p21_modeloRevisionAsegurado'
                                                ,data : json.slist1
                                            });
                                	centrarVentanaInterna(Ext.create('Ext.window.Window',
                                            {
                                                width   : 600
                                                ,height : 500
                                                ,title  : 'Revisar informacion de la poliza'
                                                ,closable : false
                                                 ,listeners :
                                                 {
                                                     afterrender : function()
                                                     {
                                                         if(json.smap1.length==0)
                                                         {
                                                             setTimeout(function(){mensajeError('No se registraron asegurados, favor de revisar a detalle los errores');},1000);
                                                         }
                                                     }
                                                 }
                                                ,items  :
                                                [
                                                    Ext.create('Ext.panel.Panel',
                                                    {
                                                        layout    : 'hbox'
                                                        ,border   : 0
                                                        ,defaults : { style : 'margin:5px;' }
                                                        ,height   : 40
                                                        ,items    :
                                                        [
                                                            {
                                                                xtype       : 'displayfield'
                                                                ,fieldLabel : 'Filas leidas'
                                                                ,value      : json.smap1.filasLeidas
                                                            }
                                                            ,{
                                                                xtype       : 'displayfield'
                                                                ,fieldLabel : 'Filas procesadas'
                                                                ,value      : json.smap1.filasProcesadas
                                                            }
                                                            ,{
                                                                xtype       : 'displayfield'
                                                                ,fieldLabel : 'Filas con error'
                                                                ,value      : json.smap1.filasErrores
                                                            }
                                                            ,{
                                                                xtype    : 'button'
                                                                ,text    : 'Ver errores'
                                                                ,hidden  : Number(json.smap1.filasErrores)==0
                                                                ,handler : function()
                                                                {
                                                                	centrarVentanaInterna(Ext.create('Ext.window.Window', //VENTANA PARA MOSTRAR LOS ERRORES
                                                                    {
                                                                        modal        : true
                                                                        ,closeAction : 'destroy'
                                                                        ,title       : 'Errores al procesar censo'
                                                                        ,width       : 800
                                                                        ,height      : 500
                                                                        ,items       :
                                                                        [
                                                                            {
                                                                                xtype       : 'textarea'
                                                                                ,fieldStyle : 'font-family: monospace'
                                                                                ,value      : json.smap1.ERROR
                                                                                ,readOnly   : true
                                                                                ,width      : 780
                                                                                ,height     : 440
                                                                            }
                                                                        ]
                                                                    }).show());
                                                                }
                                                            }
                                                        ]
                                                    })
                                                    ,Ext.create('Ext.grid.Panel',
                                                    {
                                                        height   : 450
                                                        ,columns :
                                                        [
                                                            {
                                                                text       : 'Cliente'
                                                                ,dataIndex : 'CDUNIECO'
                                                                ,width     : 80
                                                            }
                                                            ,{
                                                                text       : 'Sucursal'
                                                                ,dataIndex : 'SUCURSAL'
                                                                ,width     : 80
                                                            }
                                                            ,{
                                                                text       : 'Poliza'
                                                                ,dataIndex : 'POLIZA'
                                                                ,width     : 120
                                                            }
                                                            ,{
                                                                text       : 'Nombre'
                                                                ,dataIndex : 'Nombre_Completo'
                                                                ,width     : 200
                                                            }
                                                            ,{
                                                                text       : 'Producto'
                                                                ,dataIndex : 'PRODUCTO'
                                                                ,width     : 60
                                                            }
                                                            ,{
                                                                text       : 'Plan'
                                                                ,dataIndex : 'PLAN'
                                                                ,width     : 60
                                                            }
                                                        ]
                                                        ,store : store  //no deja SAVC 
                                                    })
                                                ]
                                                ,buttonAlign : 'center'
                                                 ,buttons     :
                                                 [
                                                    {
                                                    	text      : 'Aceptar y continuar'
                                                        ,icon     : '${ctx}/resources/fam3icons/icons/accept.png'
                                                        ,disabled : Number(json.smap1.filasErrores)>0
                                                        ,handler  : function(me)
                                                        {
                                                        	ck = 'GENERANDO POLIZAS...';
                                                            mask = _maskLocal(ck);
                                                        	var datos =
                                                            {
                                                        			 slist1 : json.slist1
                                                                    ,smap1  : json.smap1
                                                            };
                                                        	 debug('datos para confirmar:',datos);
                                                             me.up('window').destroy();
                                                            try{   
                                                            	if(Number(json.smap1.filasErrores)==0){
	                                                            Ext.Ajax.request(
	                                                            {
		                                                             url      : _p30_urlGeneraPoliza
		                                                            ,jsonData : datos
		                                                            ,success :  function (response) 
		                                                            {
		                                                            	mask.close();
		                                                            	mensajeWarning(json.smap1.filasProcesadas + ' POLIZAS GENERADAS CORRECTAMENTE');
		                                                            }
		                                                            ,failure : function()
		                                                            {
		                                                            	mask.close();
		                                                            	mensajeWarning('ERRROR AL PROCESAR POLIZAS');
		                                                            }
	                                                            });
                                                            }
                                                            }   
                                                            catch(e)
                                                            {
                                                                manejaException(e,ck);
                                                            }
                                                        }
                                                        ,listeners :
                                                        {
//                                                             afterrender : function(me)
//                                                             {
//                                                                 if(Number(json.smap1.filasErrores)>0)
//                                                                 {
//                                                                     _p21_desbloqueoBotonRol(me);
//                                                                 }
//                                                             }
                                                        }
                                                    }
                                                    ,{
                                                        text     : 'Modificar datos'
                                                        ,icon    : '${ctx}/resources/fam3icons/icons/pencil.png'
                                                        ,handler : function(me)
                                                        {
                                                            me.up('window').destroy();
                                                            _p21_tabpanel().setDisabled(false);
                                                            _p21_resubirCenso = 'S';
                                                        }
                                                    }
                                                ]
                                            }).show());
                                	
                                	
//                                 }else{
//                                 	alert("nada")
//                                 }     
                            } catch (e) {
                                manejaException(e,ck);
                            }
                        }
                        ,failure : function(form, action)
                        {
                        	alert('failure:', Ext.decode(action.response.responseText));
                        	
                            alert('hh');
                        }
                   	    });
                    }
                }
            }
	    }],
	    renderTo: 'dvCargaEmisionRecInd'
	});

});

</script>
</head>
<body>
    <div id="dvCargaEmisionRecInd" style="height:1100px;"></div>
</body>
</html>