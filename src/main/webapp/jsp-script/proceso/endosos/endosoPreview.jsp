<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
////// urls //////
var _urlRecotizarPreview = '<s:url namespace="/endosos"          action="retarificarEndosos" />';
////// urls //////

////// variables //////


////// variables //////

Ext.onReady(function()
{	
	// Se aumenta el timeout para todas las peticiones:
	Ext.Ajax.timeout = 485000; // 8 min
	Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
	Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
	Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
	var nmpoliza = '<s:property value="smap4.nmpoliza" />';
	var cdunieco = '<s:property value="smap4.cdunieco" />';
	var cdramo   = '<s:property value="smap4.cdramo"   />';	
	var estado   = '<s:property value="smap4.estado"   />';
	var nmsuplem = '<s:property value="smap4.nmsuplem" />';
	var nsuplogi = '<s:property value="smap4.nsuplogi" />';
	
	
	debug('### nmpoliza: ',nmpoliza);
	debug('### cdunieco: ',cdunieco);
	debug('### cdramo:   ',cdramo);
	debug('### estado:   ',estado);
	debug('### nmsuplem: ',nmsuplem);
	debug('### nsuplogi: ',nsuplogi);
	
	////// modelos //////
	Ext.define('_p31_modeloDetalleCotizacion',{
		extend : 'Ext.data.Model'
        ,fields :[
        	'COBERTURA'
            ,{
                name : 'PRIMA'
                ,type : 'float'
            }
            ,'TITULO'
        ]
    });
//////Funciones///////

/////Funnciones//////
	
	Ext.Ajax.request({
		url     : _urlRecotizarPreview
        ,params :{
        	'smap4.cdunieco'          : cdunieco
            ,'smap4.cdramo'           : cdramo
            ,'smap4.estado'           : estado
            ,'smap4.nmpoliza'         : nmpoliza
            ,'smap4.nmsuplem'         : nmsuplem
            ,'smap4.nsuplogi'         : nsuplogi
        }
        ,success : function(response, options){
        	var jsonpreview = Ext.decode(response.responseText);
            debug('### succes, jsonpreview:', jsonpreview);
            if(Ext.isEmpty(jsonpreview.slist1)) {
            	Ext.Msg.show({
            		title    : 'Error no hay datos'
                    ,msg     : 'No hay Informacion por mostrar...  '
                    ,buttons: Ext.Msg.OK
					,icon: Ext.Msg.ERROR
                });
                var botonConfirmar =_fieldByName('endosoButton');
                botonConfirmar.setDisabled(true);
                
                var botonDocumento=_fieldByName('documentoButton');
                botonDocumento.setDisabled(true);
            } else {
            	Ext.create('Ext.grid.Panel',{
            		renderTo: '_p29_divpri'
                    ,store    : Ext.create('Ext.data.Store',{
                    				model       : '_p31_modeloDetalleCotizacion'
                        			,groupField : 'TITULO'
                        			,sorters    :[{
                        				sorterFn : function(o1,o2){
                        								debug('sorting:',o1,o2);
                                						if (o1.get('COBERTURA') == o2.get('COBERTURA')){
                                							return 0;
                                						}
                                						return o1.get('COBERTURA') < o2.get('COBERTURA') ? -1 : 1;
                            						}
                        			}]
                        			,proxy :{
                        				type    : 'memory'
                            			,reader : 'json'
                        			}
                        			,data : jsonpreview.slist1
                    			})
                    ,columns :[
                        {
                            header           : 'Nombre de la cobertura'
                            ,dataIndex       : 'COBERTURA'
                            //,width           : 480
                             ,flex            : 2
                            ,summaryType     : 'count'
                            ,summaryRenderer : function(value){
                            						return Ext.String.format('Total de {0} cobertura{1}',value,value !== 1 ? 's': '');
                            					}
                        }
                        ,{
                            header       : 'Importe por cobertura'
                            ,dataIndex   : 'PRIMA'
                            //,width       : 150
                             ,flex       : 1
                            ,renderer    : Ext.util.Format.usMoney
                            ,align       : 'right'
                            ,summaryType : 'sum'
                        } 
                    ]
                    ,features :[
                        {
                            groupHeaderTpl :[
                                '{name:this.formatName}'
                                ,{
                                    formatName : function(name) {
                                    				return name.split("_")[1];
                                    			 }
                                }
                            ]
                            ,ftype          : 'groupingsummary'
                            ,startCollapsed : RolSistema.puedeSuscribirAutos(_GLOBAL_CDSISROL) !== true
                            ,listeners      :{
                                groupexpand : function(view,node,group){
                                    				if(RolSistema.puedeSuscribirAutos(_GLOBAL_CDSISROL) !== true){
                                    					this.collapseAll();
                                    				}
                                				}
                            }                                    
                        }
                    ],
					bbar: Ext.create('Ext.toolbar.Toolbar',{
						buttonAlign : 'right',
						items       :[
							'->'
							,Ext.create('Ext.form.Label',{
								style          : 'color:white;'
								,initComponent : function(){
													var sum = 0;
													for ( var i = 0; i < jsonpreview.slist1.length; i++){
														sum += parseFloat(jsonpreview.slist1[i].PRIMA);
													}
													this.setText('Total: '+ Ext.util.Format.usMoney(sum));
													this.callParent();
												 }
							})
						]
					})
                })
     		}
        },
        failure : function(response, options) {
        	debug('failure:', response)
            errorComunicacion(null,'Error al cancelar endoso');
        }
    });
})
</script>
</head>
<body><div id="_p29_divpri" style="height:700px;"></div></body>
</html>