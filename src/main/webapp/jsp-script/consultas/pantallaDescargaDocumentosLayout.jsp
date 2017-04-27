<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<!-- UX libraries -->
<script type="text/javascript"
	src="${ctx}/resources/extjs4/plugins/spreadsheet/ux-all-debug.js"></script>
<!-- Spread library -->
<script type="text/javascript"
	src="${ctx}/resources/extjs4/plugins/spreadsheet/spread-all-debug.js"></script>

<link type="text/css" rel="stylesheet"
	href="${ctx}/resources/extjs4/plugins/spreadsheet/css/spread.css" />
<%-- <script type="text/javascript" src="${ctx}/resources/extjs4/base_extjs4.js"></script> --%>
<script>





//////urls //////
var url_catalogo_ramo='<s:url namespace="/catalogos" action="obtieneCatalogo"                />';
var url_valida_layout='<s:url namespace="/consultas" action="verificaLayout"                />';
var url_descarga_layout='<s:url namespace="/consultas" action="descargaLayout"                />';

var url_borra_datos_layout='<s:url namespace="/consultas" action="borrarDatosLayout"                />';



////// urls //////

////// variables //////
var spreadWnd;
var tipo_documento=[];
var layout;
var filasIniciales=100;
var cdramo;
var tpdocum;
var winSpread=null;
var tipDoc;
var _p49_params = <s:property value="%{convertToJSON('params')}" escapeHtml="false" />;
debug('_p49_params:',_p49_params);



var _p49_dirIconos = '${icons}';
var tipo_ramo=Ext.create('Ext.form.ComboBox',{
	id:'idAutoGenerado_1473176076552_1042_0',
	cdatribu:'cdtipram',
	fieldLabel:'TIPO DE RAMO',
	label:'TIPO DE RAMO',
	orden:0,
	allowBlank:false,
	name:'cdtipram',
	readOnly:false,
	swobligaflot:false,
	swobligaemiflot:false,
	hidden:false,
	style:'margin:5px',
	typeAhead:true,
	anyMatch:true,
	displayField:'value',
	valueField:'key',
	matchFieldWidth:false,
	listConfig:{maxHeight:150,minWidth:120},
	forceSelection:true,
	editable:true,
	queryMode:'local',
	//listeners:{    blur:    {        fn:function()        {            /*debug('blur',Ext.getCmp('idAutoGenerado_1473176076552_1042_1'));*/            if(Ext.getCmp('idAutoGenerado_1473176076552_1042_1').heredar)            {                Ext.getCmp('idAutoGenerado_1473176076552_1042_1').heredar(true);            }            else            {                try{ _g_heredarCombo(true,'idAutoGenerado_1473176076552_1042_1','idAutoGenerado_1473176076552_1042_0'); }catch(e){ debugError(e); }            }        }    }},
	store:Ext.create('Ext.data.Store',{
	model:'Generic',
	autoLoad:true,
	proxy:{type:'ajax',
	url:url_catalogo_ramo,
	reader:{type:'json',
	root:'lista',
	rootProperty:'lista'
	},
	extraParams:{catalogo:'TIPOS_RAMO'}
	}
	})
	})
////// variables //////

////// overrides //////
////// overrides //////

////// componentes dinamicos //////



Ext.onReady(function(){
	
    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
	
	
	layout=Ext.create('Ext.panel.Panel',{
		itemId		: 'layout',
		height		: 600,
		autoScroll	: true,
		hidden		: true,
		buttons		:[

		       		  
		       		  ]
		
	});

			///////////////////////	
	
	Ext.create('Ext.panel.Panel',
		    {
		        renderTo  : '_p49_divpri'
		        ,itemId   : '_p49_panelpri'
		        ,defaults : { style : 'margin:5px;' }
		        ,border   : 0
		        ,items    :
					    [
					Ext.create('Ext.form.Panel',
					        {
					            itemId       : 'formBusq'
					            ,name		 : '_form'
					            ,title       : 'DESCARGA MEDIANTE LAYOUT'
					            ,defaults    : { style : 'margin:5px;' }
					            ,items       : [
						                            tipo_ramo
						                            
					                            ]
					            ,layout      :
					            {
					                type     : 'table'
					                ,columns : 3
					            }
					            	        })
					        ,
					        layout
					    ]
		    });
	
});




function navegacion(nivel){
	
	if(nivel==1){
		_fieldByName('cdtipram').show();
		_fieldById('tipodoc').hide();
	}
	else if(nivel==2){
		_fieldById('tipodoc').show();
		_fieldByName('cdtipram').hide();

		
	}
	
}


_fieldByName('cdtipram').on(
	    {
	        select : function(me,records)
	        {
	            var ck = 'Estableciendo opciones de tipo de documento';
	            try{
		            cdramo=records[0].data.key;
	
		            if(records[0].data.key=="10"){
		            	tipo_documento=[
		                                {"valor":"P", "name":"POLIZA/ENDOSO"},
		                                {"valor":"R", "name":"RECIBO"},
		                                {"valor":"C", "name":"CERTIFICADO"}
		                            ]
		            }else{
		            	
		            	tipo_documento=[
		                                {"valor":"P", "name":"POLIZA/ENDOSO"},
		                                {"valor":"R", "name":"RECIBO"}
		                            ]
		            }
	            
		            _fieldByName('_form').add(Ext.create('Ext.form.ComboBox', {
	                	itemId			: 'tipodoc',
	                	hidden			: true,
	                    fieldLabel		: 'TIPO DE DOCUMENTO',
	                    queryMode		: 'local',
	                    displayField	: 'name',
	                    valueField		: 'valor',
	                    store			: Ext.create('Ext.data.Store', {
		                                    fields: ['valor', 'name'],
		                                    data : tipo_documento
	                    					}),
	                    listeners:{
	                    	change:function(me,nuevo,viejo){
	                    		tipDoc=nuevo;
	                    		if(winSpread!=null)
	                    			winSpread.close();
	                    		winSpread = new Ext.window.Window({
	                    		        title: 'Descarga Layout',
	                    		        layout: 'fit',
	                    		        maximizable: false,
	                    		        draggable:false,
	                    		        resizable: false,
	                    		        closable: false,
	                    		        y:400,
	                    		        width: 1000,
	                    		        height: 500,
	                    		        buttons: [{
	                    		  		  text: 'Verificar',
	                    				  handler:function(){
	                    					var ck='Verificando datos'
	                    					try{  
	                    					_setLoading(true,winSpread);
	                    					_setLoading(true,'formBusq');
	                    					
	                    					  var store=_fieldByName('spreadLayout').getStore();
	                    					  var tipos={
	                    							  tpdocum:tpdocum,
	                    							  cdramo:cdramo
	                    					  }
	                    					  
	                    					  var datos=[];
	                    					 
	                    					var blancos=0;
	                    					  store.each(function(row){
	                    						  
	                    						  var d=row.data;
	                    						  
	                    							
	                    						  if(
	                    								d.SUCURSAL.trim()!='' &&
	                    								d.RAMO.trim()!='' &&
	                    								d.POLIZA.trim()!='' 
	                    								
	                    						  ){
	                    						datos.push(d);
	                    						  }else{
	                    							  blancos++;
	                    						  }
	                    					  });
	                    					  var quita_duplicados=false;
	                    					 datos.forEach(function(it,idx,arr){
	                    						for(var i=idx+1;i<datos.length;i++){
	                    							 var dt=arr[i];
	                    							 
	                    							if(it.SUCURSAL==dt.SUCURSAL
	                    								&& it.CDTIPEND==dt.CDTIPEND
	                    								&& it.NUMENDOSO==dt.NUMENDOSO
	                    								&& it.SEQRECIBO==dt.SEQRECIBO
	                    								&& it.NMCERTIF==dt.NMCERTIF
	                    								&& it.RAMO==dt.RAMO
	                    								&& it.POLIZA==dt.POLIZA
	                    							){
	                    								 
	                    										  datos[i]={};
	                    										quita_duplicados=true;
	                    							  }
	                    						 
	                    						 }
	                    					 }); 
	                    					 var d2=[];
	                    					 datos.forEach(function(it,idx,arr){
	                    						 
	                    						 if(it.SUCURSAL!=undefined){
	                    							d2.push(it);
	                    							    blancos++;
	                    					     }
	                    					 });
	                    					 
	                    					 datos=d2.slice();
	                    					 
	                    					
	                    				
	                    					 
	                    					  
	                    					if(datos.length<1){
	                    					    _setLoading(false,winSpread);
                                                _setLoading(false,'formBusq');
	                    						  mensajeWarning("El layout esta vacío");
	                    						  
	                    						  return;
	                    					  }
	                    					
	                    					for(var i=0;i<blancos;i++){
	                    						d2.push({});
	                    					}
	                    					store.loadData(d2);
	                    					
	                    					
	                    					
	                    					
	                    					var jsonData={list:datos,
	                    			    			params: tipos};
	                    					ck='Enviando peticion ajax ';
	                    					Ext.Ajax.request({
	                    					    url: url_valida_layout,
	                    					    timeout : 1000*60*60*3,
	                    					    jsonData: jsonData,
	                    					    success: function(response){
	                    					        _setLoading(false,winSpread);
	                    					        _setLoading(false,'formBusq');
		                    					        
		                    					     var ck = 'Decodificando respuesta verifica Layout';
		                    					     debug('###Datos enviados url=,datos=',url_valida_layout,jsonData);
		                    					     console.log(jsonData)
			                    		           try
			                    		           {
			                    		               var json = Ext.decode(response.responseText);
			                    		               debug('### respuesta verifica layout:',json);
			                    		               
			                    		               if(json.success==true)
			                    		               {
			                    		                   var verLay=generaLayout(json.params.tpdocum,true);
			                    		                   
			                    		                   var storeVerLay=verLay.getStore();
			                    		                   var descarga=false;
			                    		                   json.list.forEach(function(it,idx,arr){
			                    		                  	
			                    		                  	 arr[idx].VALIDO=it.VALIDO=='true';
			                    		                  	 if(arr[idx].VALIDO){
			                    		                  		 descarga=true;
			                    		                  	 }
			                    		                  	
			                    		                   });
			                    		                   
			                    		                  
			                    		                   
			                    		                   
			                    		              	     spreadWnd = new Ext.window.Window({
			                    		                       title: 'VERIFICACION LAYOUT',
			                    		                       name:'ventana_descarga',
			                    		                       layout: 'fit',
			                    		                       modal:true,
			                    		                       maximizable: true,
			                    		                       resizable: true,
			                    		                       width: 800,
			                    		                       height: 300,
			                    		                       buttons:[
			                    		                                {
			                    		                              	  text:'Descargar PDF Simple',
			                    		                              	  disabled:true,
			                    		                              	  itemId:'btnDescargar',
			                    		                              	  name:'btnDescargar',
			                    		                              	  duplex:false,
			                    		                              	  handler: function(btn){
			                    		                              		  descargarPDF(btn);
			                    		                              		  
			                    		                              		  
			                    		                              	  }
			                    		                                },
			                    		                                {
			                    		                              	  text:'Descargar PDF Duplex',
			                    		                              	  disabled:true,
			                    		                              	  itemId:'btnDescargarDplx',
			                    		                              	  name:'btnDescargarDplx',
			                    		                              	  duplex:true,
			                    		                              	  handler: function(btn){
			                    		                              		  descargarPDF(btn);
		//	                    		                               		  spreadWnd.close();
		//	                    		                               		  _fieldByName('spreadLayout').getStore().removeAll();
		//	                    		                               		  layout.removeAll();
			                    		                              		  
		//	                    		                               		  layout.add(generaLayout(tpdocum,false));
		//	                    		             	                    		layout.show();
			                    		                              		  
			                    		                              	  }
			                    		                                }
			                    		                                ,
			                    		                                {
			                    		                              	  text:'Regresar',
			                    		                              	  handler:function(){
			                    		                              		  
			                    		                              		  spreadWnd.close();
			                    		                              		  var storOrig=_fieldByName('spreadLayout').getStore();
			                    		                              		  layout.removeAll();
			                    		                              		  var lay=generaLayout(tpdocum,false);
			                    		                              		  var store2= lay.getStore();
			                    		                              		  store2.removeAll();
			                    		                              		  storOrig.each(function(record){
			                    		                              			  store2.add(record.copy());
			                    		                              			});
			                    		                              		 
		//	                    		             	                    		layout.add(lay);
		//	                    		             	                    		layout.show();
			                    		            	                    		
			                    		            	                    		winSpread.removeAll();
			                    		            	                    		winSpread.add(lay);
			                    		            	                    		winSpread.show();
			                    		            	                    		
			                    		                              		 
			                    		                              	  }
			                    		                                }
			                    		                                ],
			                    		                       items: [
			                    		                               verLay
			                    		                               ],
			                    		                       listeners:{
			                    		                      	 destroy:function(){
			                    		                      		 _setLoading(true,layout);
			                    		                      		 Ext.Ajax.request(
			                    		          				             {
			                    		          				                 url      : url_borra_datos_layout
			                    		          				                 ,success : function(response)
			                    		          				                 {
			                    		          				                     _setLoading(false,layout);
			                    		          				                     var ck = 'Decodificando respuesta al imprimir';
			                    		          				                     try
			                    		          				                     {
			                    		          				                         var json = Ext.decode(response.responseText);
			                    		          				                         if(json.success!=true ) {
			                    		          				                             mensajeError(json.message,"Error");
			                    		          				                         }
			                    		          				                     }
			                    		          				                     catch(e)
			                    		          				                     {
			                    		          				                         manejaException(e,ck);
			                    		          				                     }
			                    		          				                 }
			                    		          				                 ,failure  : function()
			                    		          				                 {
			                    		          				                     _setLoading(false,layout);
			                    		          				                     errorComunicacion(null,'Error al descargar');
			                    		          				                 }
			                    		          				             });
		
			                    		                      	 }
			                    		                       }
			                    		                   });
			                    		              	     
			                    		              	    
		
			                    		              	  _fieldByName('btnDescargar').setDisabled(!descarga);
			                    		              	  _fieldByName('btnDescargarDplx').setDisabled(!descarga);
			                    		                   spreadWnd.show();
			                    		                   spreadWnd.center();
			                    		                   storeVerLay.removeAll();
			                    		                   storeVerLay.loadData(json.list);
			                    		                   verLay.setEditable(false);
			                    		                   if(quita_duplicados){
			                    			       					mensajeWarning('Se borraron filas duplicadas');
			                    			       				}
		
			                    		               }
			                    		               else
			                    		               {
			                    		                   mensajeError(json.message);
			                    		               }
			                    		           }
			                    		           catch(e)
			                    		           {
			                    		               manejaException(e,ck);
			                    		           }
	                    					    },
	                    						 failure : function()
	                    		           {
	                    						     _setLoading(false,winSpread);
	                    						     _setLoading(false,'formBusq');
	                    		               errorComunicacion(null,'Error recuperando verificando layout');
	                    		           }
	                    						    
	                    					});
	                    					
	                    				 }catch(e){
	                    					 manejaException(e,ck);
	                    				  }
	                    			}
	                    				 
	                    			  }]
	                    		    });
	                    		winSpread.removeAll();
	                    		winSpread.add(generaLayout(nuevo));
	                    		

	                    		    // Show the spread window
	                    		    winSpread.showAt(0,95);

	                    		    // And center it
	                    		    //winSpread.center();
	                    		
	                    		
// 	                    		layout.removeAll();
// 	                    		layout.add(generaLayout(nuevo));
// 	                    		layout.show();
	                    	}
	                    }
	                    
	                }));
	            
	          
		            try
		            {
						navegacion(2);
		            }
		            catch(e)
		            {
		                manejaExeption(e,ck);
		            }
	        	}catch(e)
	            {
	                manejaExeption(e,ck);
	            }
	    
	        }
	    });
	    
function generaLayout(documento,verifica){
	var ck='Generando layout';
	try{
	
		var fields;
		tpdocum=documento;
		var columnas=[
			              {
							text: '#', xtype: 'rownumberer', sortable: false, editable: false, resizable: true, width: 40,
							tdCls: Ext.baseCSSPrefix + 'grid-cell-header ' + Ext.baseCSSPrefix + 'grid-cell-special'
						  }, 
						  {
					        header: 'SUCURSAL',
					        dataIndex: 'SUCURSAL',
					        xtype: 'numbercolumn',
					        format: '0',
					        cellwriter:function(value,pos){
				            	if(value>9999){
				            		return (value+'').substring(0,4);
				            	}
				            	return value;
				            }
					      }, 
					      {
					        header: 'RAMO',
					        dataIndex: 'RAMO',
					      	xtype: 'numbercolumn',
					        format: '0',
					        cellwriter:function(value,pos){
				            	if(value>999){
				            		return (value+'').substring(0,3);
				            	}
				            	return value;
				            }
					    }, {
					        header: 'POLIZA',
					        dataIndex: 'POLIZA',
					        xtype: 'numbercolumn',
					        format: '0',
					        cellwriter:function(value,pos){
				            	if((value+'').length>10){
				            		return (value+'').substring(0,10);
				            	}
				            	return value;
				            }
		    			}
				    ];
		
		
		if(verifica){
			columnas.splice(1,0,{
		        header: 'VALIDADO',
		      	xtype: 'booleancolumn',
		      	dataIndex: 'VALIDO',
		      	
		            trueText: 'Yes',
		            falseText: 'No',
		            renderer:function(valor, pos){
		            	if(valor){
			            	return '<div align="center" style="color:green;"><b>SI</b></div>'
	
		            	}
		            	return '<div align="center" style="color:red;"><b>NO</b></div>'
		            }
		    })
		    columnas.splice(2,0,
	    			{
		        header: 'OBSERVACIONES',
		        dataIndex: 'OBSERVAC',
		        format: '0',
		        width: 200,
		  });
		}
		
		
		ck='Estableciendo columnas del spread';
		fields=[
		        'VALIDO',
				 'OBSERVAC',
                'SUCURSAL',
                'RAMO',
                'POLIZA',
                'CDTIPEND',
                'NUMENDOSO',
                'SEQRECIBO',
                'NMCERTIF'
            ];
		
			columnas.push({
		        header: 'TIPO ENDOSO',
		        dataIndex: 'CDTIPEND',
		        cellwriter: function(value,position){
		        	if((value+'').length>2){
	            		value=(value+'').substring(0,1);
	            	}
		        	if(value.match(/^[A-Za-z]$/)==null){
		        		return "";
		        	}
		        	
		        	if(value==''){
		        		
		        		var dat=position.record.getData();
		        		dat.NUMENDOSO='';
		        		dat.CDTIPEND='';
		        		//position.record.setFields(dat,'raw');
		        		return "";
	            	}
		        	return value;
		        }
		  });
		columnas.push({
		            header: 'NO. ENDOSO',
		            dataIndex: 'NUMENDOSO',
		            xtype: 'numbercolumn',
		            format: '0',
		            cellwriter: function(value, position) {
		            	if((value+'').length>2){
		            		value=(value+'').substring(0,10);
		            	}
		            	if(position.record.get('CDTIPEND').trim()==''){
		            		
		            		return '';
		            	}
		                
		
		                return value;
		            }
		            
		      });

		
		
		switch(documento){
			case 'P': // para polizas/ endosos
				
					        
			        
	
			break;
			case 'R': // para recibos
				
				
	
				columnas.push({
			        header: 'RECIBO',
			        dataIndex: 'SEQRECIBO',
			        xtype: 'numbercolumn',
		            format: '0',
		            cellwriter:function(value,pos){
		            	if(value>999){
		            		return (value+'').substring(0,3);
		            	}
		            	return value;
		            }
			  });
			break;
			case 'C': // para certificados
			
			
				
				columnas.push({
			        header: 'CERTIFICADO',
			        dataIndex: 'NMCERTIF',
			        xtype: 'numbercolumn',
			        format: '0',
			        cellwriter:function(value,pos){
			        	
		            	if(value>9999){
		            		return (value+'').substring(0,4);
		            	}
		            	return value;
		            }
			  });
				
			break;
		}
		
		Ext.define('DescargaModel', {
	        extend: 'Ext.data.Model',
	        fields: fields    });
		
		var localDataStore = new Ext.data.Store({
		    storeId: 'descargaStore',
		    model: 'DescargaModel',
		    data: {},
		    proxy: {
		        type: 'memory',
		        reader: {
		            type: 'json'
		        }
		    },
		    
		});
	
		ck='creando spreadpanel';
		var spreadPanel = new Spread.grid.Panel({
			name	: 'spreadLayout',	
			
			  tbar: [{
		    	//xtype		: 'displayfield',
		    	text		: 'FILAS: '+filasIniciales,
		    	hidden		: verifica==true,
		    	disabled	: true,
		    	//fieldLabel	: 'Filas',
		    	style	: {color:'red', marginBottom: '10px'}
		    	
		    },
		    {
		        xtype      : 'numberfield',
		        name       : 'rowsToCreate',
		        minValue   : 0,
		        value      : 0,
		        width      : 55,
		        hidden		: verifica==true
		        
		    },{
		    	icon:  '${ctx}/resources/fam3icons/icons/table_add.png',
		    	hidden		: verifica==true,
		        text: 'Agregar Filas',
		        handler: function() {
		        	if(!this.previousSibling().isValid()){
		                Ext.Msg.alert('Aviso', 'Ingrese un n&uacute;mero v&aacute;lido');
		                return;
		            }
		            var numFilas = this.previousSibling().getValue();
		            for(var count=0 ;count<numFilas; count++)
		          		localDataStore.add({});
		            this.previousSibling().previousSibling().setText('FILAS: '+(localDataStore.count()));
		           
		        }
		    }],
		    store: localDataStore,
		    listeners: {
		       
		        afterrender: function(){
		        	for(var i=0;i<filasIniciales;i++){
		          		localDataStore.add({});
	
		        	}
		        }
		    
		    },
		    editable: true,
		    editModeStyling: true,
		    columns: columnas	});
	}catch(e)
    {
        manejaExeption(e,ck);
    }
	return spreadPanel;

	
}

function descargarPDF(btn){
	var ck='Descargando pdf';
	try{
		var me=btn.up('window');
		
		Ext.MessageBox.confirm('Confirmar',
				'Se marcarán como impresos los documentos seleccionados, ¿Desea continuar?',
				function(opc){
					
					if(opc!='yes' ){
						return;	
					}
					ck='Haciendo petición'
					
					
					Ext.create('Ext.form.Panel').submit(
				             {
				                 url             : url_descarga_layout
				                 ,params         :
				                 {
				                     'params.cdtipram' : cdramo,
				                     'params.duplex':btn.duplex?"S":"N"
				                     
				                 }
				                 ,standardSubmit : true
				                 ,target         : '_blank'
				                 ,timeout: 5
				             });
					
					_setLoading(true,layout);
					_mask('Descargando PDF...');
					Ext.Ajax.request(
				             {
				                 url      : _GLOBAL_URL_ESPERAR_DESCARGA_LOTE
				                // ,timeout : 1000*60*60*3
				                 ,callback:function(o,s,r){
				                		spreadWnd.close();
				                		  _fieldByName('spreadLayout').getStore().removeAll();
// 				                		  layout.removeAll();
				                		  
// 				                		  layout.add(generaLayout(tpdocum,false));
// 				                      		layout.show();
				                      		
				                      		
				                 }
				                 ,success : function(response)
				                 {
				                     
				                     _unmask();
				                     var ck = 'Decodificando respuesta al imprimir';
				                     try
				                     {
				                         var json = Ext.decode(response.responseText);
				                         	winSpread.removeAll();
	         	                    		winSpread.add(generaLayout(tipDoc));
	         	                    		winSpread.show();
				                         if(json.success==true && json.dwnError!=true)
				                         {
				                             mensajeCorrecto(
				                                 'Descarga correcta'
				                                 ,'Descarga correcta'
				                                 ,function()
				                                 {
				                                     //win.destroy();
				                                     //me.actualizarBotones();
				                                    // me.setActive();
				                                 }
				                             );
				                         }else if(json.dwnError==true){
				                        	
				                        	 centrarVentanaInterna(Ext.Msg.alert("Error","Se gener&oacute; el pdf pero con errores, se descargar&aacute; la lista de archivos no encontrados",function(){
				                         		debug('DescargarListaErrores');
				                               
				                                var ck = 'Descargando';
				                                try
				                                {
				                                    Ext.create('Ext.form.Panel').submit(
				                                    {
				                                        url             : _GLOBAL_URL_DESCARGAR_LISTA_ERROR_ARCHIVO
				                                        ,params         :
				                                        {
				                                           
				                                        }
				                                        ,standardSubmit : true
				                                        ,target         : '_blank'
				                                    });
				                               }
				                               catch(e)
				                               {
				                            	  
				                            	   manejaException(e,ck);
				                               }
				                         	}));
				                         }
				                         else
				                         {
				                             mensajeError(json.message,"Error");
				                         }
				                        
				                     }
				                     catch(e)
				                     {
				                    	 debugError(e);
				                         manejaException(e,ck);
				                     }
				                 }
				                 ,failure  : function()
				                 {
				                     _unmask();
				                	 winSpread.removeAll();
	     	                    		winSpread.add(generaLayout(tpdocum,false));
	     	                    		winSpread.show();
				                     _setLoading(false,layout);
				                     errorComunicacion(null,'Error al descargar');
				                    
				                 }
				             });
				        
					//
					
					
				
	
				}
		);
	}catch(e)
    {
        manejaExeption(e,ck);
    }	
	
	
}


	    

</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div id="_p49_divpri" style="height: 600px; border: 1px solid #CCCCCC;"></div>
	<div id="spread"></div>
</body>
</html>