<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Aviso de Hospitalizaci&oacute;n</title>

		<script type="text/javascript">
            
		    var _CONTEXT = '${ctx}';
            
            var _cdUnieco =  '<s:property value="params.cdunieco" />';
            var _cdRamo   =  '<s:property value="params.cdramo" />';
            var _estado   =  '<s:property value="params.estado" />';
            var _nmpoliza =  '<s:property value="params.nmpoliza" />';
            var _nmsituac =  '<s:property value="params.nmsituac" />';
            var _icodpoliza =  '<s:property value="params.icodpoliza" />';
            var _cdperson =  '<s:property value="params.cdperson" />';
            var _estatus  = '<s:property value="params.estatus" />';
            var _nombre   = '<s:property value="params.nombre" />'
            
            var _URL_AVISO_HOSPITALIZACION = '<s:url namespace="/consultasPoliza" action="avisoHospitalizaciones" />';
		            
			Ext.onReady(function() {
		
				Ext.define('AvisoHospModel', {
				    extend:'Ext.data.Model',
				    fields:['cdperson','estatus','nombre','fecha','hora','hospital','fecingreso','comentarios']
				});
				
				var storeHistoricoFarmacia = new Ext.data.Store({
			        model      : 'FarmaciaModel',
			        //autoLoad: true,
			        proxy     : {
			        	type        : 'ajax',
			            url         : _URL_AVISO_HOSPITALIZACION,
			            extraParams : {
			                'params.cdunieco' : _cdUnieco,
			                'params.cdramo'   : _cdRamo,
			                'params.estado'   : _estado,
			                'params.nmpoliza' : _nmpoliza,
			                'params.nmsituac' : _nmsituac,
			                'params.icodpoliza' : _icodpoliza,
			                'params.cdperson'  : _cdperson
			            },
			            reader : {
			                type : 'json',
			                root : 'avisoHospitalizacion'
			            }
			        }
			    });
			    
			    //TODO: Hacer store para el combo tomar el key value model
			    var storeHospitales = new Ext.data.Store({
                    model      : 'Generic',
                    //autoLoad: true,
                    proxy     : {
                        type        : 'ajax',
                        url         : _URL_AVISO_HOSPITALIZACION,
                        extraParams : {
                            'params.cdunieco' : _cdUnieco,
                            'params.cdramo'   : _cdRamo,
                            'params.estado'   : _estado,
                            'params.nmpoliza' : _nmpoliza,
                            'params.nmsituac' : _nmsituac,
                            'params.icodpoliza' : _icodpoliza,
                            'params.cdperson'  : _cdperson
                        },
                        reader : {
                            type : 'json',
                            root : 'avisoHospitalizacion'
                        }
                    }
                });
			    
				
				Ext.create('Ext.panel.Panel', {
					name      : 'pnlAvisoHospitalizacion',
					renderTo  : 'dvAvisoHospitalizacion',
					//layout:'fit',
					defaults  : {
						style : 'margin:5px',
						border : false
					},
	                items: [
	                   {
	                   layout : 'hbox',
	                   items :[
        	               {xtype: 'textfield', name: 'cdperson', fieldLabel: 'ID Afiliado',      readOnly: true, labelWidth: 120, width: 300, value : _cdperson},
        	               {xtype: 'textfield', name: 'estatus', fieldLabel: 'Estatus',      readOnly: true, labelWidth: 120, width: 300, value : _estatus}
    	               ]
	                   },
	                   {
                       layout : 'hbox',
                       items :[
                           {xtype: 'textfield', name: 'nombre', fieldLabel: 'Nombre',      readOnly: true, labelWidth: 120, width: 600, value : _nombre}
                           
                       ]
                       },
                       {
                       layout : 'hbox',
                       items :[
                           {xtype: 'datefield', name: 'fecha', fieldLabel: 'Fecha',      readOnly: true, labelWidth: 120, width: 300, value : new Date()},
                           {xtype: 'textfield', name: 'hora', fieldLabel: 'Hora',      readOnly: true, labelWidth: 120, width: 300, value : new Date().getHours() + ":" + new Date().getMinutes()}
                       ]
                       },
                       {
                       layout : 'hbox',
                       items :[                           
                           {xtype: 'combobox', name: 'hospital', fieldLabel: 'Hospital',      labelWidth: 120, width: 600, queryMode : 'remote', queryParam : '2', store:storeHospitales}
                       ]
                       },
                       {
                       layout : 'hbox',
                       items :[                           
                           {xtype: 'datefield', name: 'fecingreso', fieldLabel: 'Fecha ingreso',      labelWidth: 120, width: 300, value : new Date()}
                       ]
                       },
                       {
                       layout : 'hbox',
                       items :[                           
                           {xtype: 'textareafield', name: 'comentario', fieldLabel: 'Comentarios',      labelWidth: 120, width: 600}
                       ]
                       },
                       {
                       layout : 'hbox',
                       items :[
                            {xtype: 'label', name: '', text: '',      labelWidth: 120, width: 300},
                           {xtype: 'button', name: 'estatus', text: 'Continuar',      labelWidth: 120, width: 120}
                       ]
                       },
	                	{
	                	xtype       : 'grid',
					    store       : storeHistoricoFarmacia,
					    title       :  'Avisos anteriores:',
					    buttonAlign : 'center',
					    autoScroll  : 'true',
					    columns     : [{
					        header    : 'Fecha Registro',
					        dataIndex : 'iultimo',
					        flex      : 1
					    },{
                            header    : 'Hospital',
                            dataIndex : 'poliza',
                            flex      : 1
                        },{
                            header    : 'Fecha Ingreso',
                            dataIndex : 'tigrupo',
                            flex      : 1
                        },{
                            header    : 'Comentario',
                            dataIndex : 'maximo',
                            flex      : 1,
                            renderer  : 'usMoney'
                        }
                        ]
					}]
			    });
		            	
		    });
		</script>

    </head>
    <body>
        <div id="dvAvisoHospitalizacion"></div>
    </body>
</html>