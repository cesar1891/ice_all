<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">
var urlLoadImpresora   = '<s:url namespace="/catalogos" action="obtieneImpresorasUsuario" />';
var _URL_guardaEditaImpresora= '<s:url namespace="/catalogos" action="guardaEditaImpresora" />';
var _URL_habilitaDeshabilitaImpresora='<s:url namespace="/catalogos" action="habilitaDeshabilitaImpresora" />';
var _CONTEXT = '${ctx}';
var ck='';
var nImpresora;

Ext.onReady(function() {
	
	

	
	Ext.define('modeloImpresoras',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'IMPRESORA'},
                 {type:'string',	name:'IP'},
                 {type:'string',	name:'TIPO'},
                 {type:'string',    name:'DESCRIPCION'},
                 {type:'string',    name:'DISPONIBLE'},
                 {type:'string',    name:'ALTA'},
                
				]
    });
	
	var storeImpresoras = Ext.create('Ext.data.JsonStore', {
    	model:'modeloImpresoras',
        proxy: {
            type: 'ajax',
            url: urlLoadImpresora,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
	
	windowLoader.setLoading(true);
	storeImpresoras.load({
	    	params: {
	    		'params.pv_cdusuario_i' : null
	    	},
	    	callback: function(records, operation, success) {
	    		
	    		windowLoader.setLoading(false);
	        }
	});
	
	var comboSN=Ext.create('Ext.form.ComboBox', {
		  editable:false,
	    store: Ext.create('Ext.data.Store', {
		    fields: ['valor', 'disp'],
		    data : [
		        {"valor":"1", "disp":"1"},
		        {"valor":"2", "disp":"2"}
		    ]
		}),
	    queryMode: 'local',
	    displayField: 'disp',
	    valueField: 'valor'
	});
	
	var gridImpresoras = Ext.create('Ext.grid.Panel',{
		itemId:"gridImp",
		renderTo : 'maindivRolesUsuario',
		selModel:{
            mode      : 'SIMPLE'
		},
		selType: 'checkboxmodel',
		autoScroll: true,
		 plugins: [
		           Ext.create('Ext.grid.plugin.CellEditing', {
		               clicksToEdit: 1
		           })
		       ],
		store:  storeImpresoras,
		tbar:[
		      {
		    	  text		: 'Nueva Impresora',
		    	  icon	: '${ctx}/resources/fam3icons/icons/add.png',
		    	  handler	: function(){
		    		  nImpresora= ventanaImpresoraN();
						nImpresora.show();

						// And center it
						nImpresora.center();
		    	  }
		      }
		      ],
		//collapsible: true,
		titleCollapse: true,
		style: 'margin:0px',
		height: 300,
		columns       :[ 
						
		                 { header     : 'Impresora' ,dataIndex : 'IMPRESORA', flex: 1 }
		                 ,{ header     : 'Visible' ,dataIndex : 'DISPONIBLE',  flex: 1 }
		                 ,{ header     : 'Descripción' ,dataIndex : 'DESCRIPCION', editor: 'textfield',flex: 1,
		                	 allowBlank:false}
		                 ,{ header     : 'No. bandejas' ,dataIndex : 'TIPO', editor: comboSN, flex: 1 ,
		                	 allowBlank:false}
		                 ,{ header     : 'IP' ,dataIndex : 'IP', editor:{
		                	 	xtype: 'textfield',
					            maxLength:15,
					        	minLength:7,
					            name: 'IP',
					            allowBlank:false
					        }, flex: 1 }

		 			   ],
		buttonAlign: 'center', 
		buttons:[{
				text: 'Guardar/Actualizar Impresoras',
				icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
				handler: function() {
						try{
					var saveList = [];
					var salir=false;
					ck='registrando rows a modificar';
					var updateRecords=storeImpresoras.getUpdatedRecords();
					if(updateRecords.length<1){
						return;
					}
					
					updateRecords.forEach(function(record,index,arr){
  						if( record.data.IP=='' || (record.data.IP+'').length>15 || (record.data.IP+'').length<7
  							||	(record.data.DESCRIPCION+'')==''	
  						){
  							Ext.Msg.alert("Datos incorrectos","Uno o más campos tienen información incorrecta")
  							salir=true;
  							return false;
  						}
  						
  						saveList.push(record.data);
  						
  					});
					if(salir){
						return;
					}
					
					ck='Haciendo petición guardar impresora';
  					
  					windowLoader.setLoading(true);
					Ext.Ajax.request({
						url: _URL_guardaEditaImpresora,
						jsonData: {
							saveList : 	saveList
						},
						success: function(response) {
							
							ck='Decodificando respuesta';
							var res = Ext.decode(response.responseText);
							windowLoader.setLoading(false);
							
							if(res.success){
								storeImpresoras.reload();
								mensajeCorrecto('Aviso','Se ha guardado con exito.');
								windowLoader.close();
							}else{
								mensajeError('No se pudo guardar.');
							}
							
						},
						failure: function(){
							windowLoader.setLoading(false);
							mensajeError('No se pudo guardar.');
						}
					});
  					
				}catch(e)
	            {
	                //manejaExeption(e,ck);
	                throw e;
	            }
				}
		},
		{
			text	: 'Habilitar',
			icon	: '${ctx}/resources/fam3icons/icons/page_green.png',
			handler	: function(){
				Ext.Msg.confirm("Habilita impresora"
								,"¿Estas seguro de habilitar esta impresora?"
								,function(opc){
									if(opc==='yes'){
										habilitaDeshabilita('S');
									}
								});
				
				
				
			}
		}
		,
		{
			text	: 'Deshabilitar',
			icon	: '${ctx}/resources/fam3icons/icons/page_red.png',
			handler	: function(){
				
				Ext.Msg.confirm("Habilita impresora"
						,"¿Estas seguro de deshabilitar esta impresora?"
						,function(opc){
							if(opc==='yes'){
								habilitaDeshabilita('N');
							}
						});

			}
		}
		,{
			text: 'Cancelar',
			icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png',
			handler: function() {
				windowLoader.close();
			}
		}]
	});
	
	 function ventanaImpresoraN(){
			return new Ext.window.Window({
			    title: 'Agregar Impresora',
			    layout: 'fit',
			    maximizable: true,
			    resizable: true,
			    width: 500,
			    height: 350,
			    items: [Ext.create('Ext.form.Panel', {
					//url:_URL_guardaImpresorasUsuario,
				    border: false,
				    modal:true,
				    title: 'Nueva Impresora',
				    height: 130,
				    width: 280,
				    bodyPadding: 10,
				    defaultType: 'textfield',
				    items: [
				        {
				            fieldLabel: 'IP ',
				            maxLength:15,
				        	minLength:4,
				            name: 'IP',
				            allowBlank:false
				        },
				        {
				        	xtype : 'combobox',
				            fieldLabel: 'BANDEJAS  ',
				            editable:false,
				            maxValue:3,
				        	minValue:1,
				            name: 'TIPO',
				            store: Ext.create('Ext.data.Store', {
				    		    fields: ['valor', 'disp'],
				    		    data : [
				    		        {"valor":"1", "disp":"1"},
				    		        {"valor":"2", "disp":"2"}
				    		    ]
				    		}),
				    	    queryMode: 'local',
				    	    displayField: 'disp',
				    	    valueField: 'valor' ,
				            allowBlank:false,
				            
				        },
				        {
				            fieldLabel: 'NOMBRE ',
				            name: 'IMPRESORA',
				            allowBlank:false,
				            validator:function(){
				            	
				            	if(storeImpresoras.find('NOMBRE',this.getValue().toLowerCase())>-1){
				            		return "Esta impresora ya existe";
				            	}
				            	return true;
				            }
				        }
				        ,
				        {
				            fieldLabel: 'DESCRIPCIÓN ',
				            name: 'DESCRIPCION',
				            allowBlank:false
				        }
// 				        ,
// 				        {
// 				            xtype: 'checkboxfield',
// 				            fieldLabel: 'ACTIVO ',
// 				            name: 'SWACTIVO',
// 				            checked:true
// 				        }
// 				        ,
// 				        {
// 				        	xtype: 'hiddenfield',
// 				        	value:'true',
// 				        	name:'NIMPRESORA'
// 				        }
				        
				    ],
				    buttonAlign: 'center',
				    buttons: [
				              {
				                  text: 'Guardar',
				                  icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
				                  handler: function() {
				                      var form = this.up('form').getForm(); // get the basic form
				                      if (form.isValid()) { // make sure the form contains valid data before submitting
				                    	    
				                    	  var dat=form.getValues();
				                      		
				                      		dat.BANDEJAS=''+dat.BANDEJAS;
				                      		
				                    	  //console.log(dat);
				                      
			      						nImpresora.setLoading(true);

				                    	  Ext.Ajax.request({
				      						url: _URL_guardaEditaImpresora,
				      						jsonData: {
				      							saveList: [dat]
				      						},
				      						success: function(response) {
				      							var res = Ext.decode(response.responseText);
				      							nImpresora.setLoading(false);
				      							
				      							if(res.success){
				      								storeImpresoras.reload();
				      								mensajeCorrecto('Aviso','Se ha guardado con exito.');
				      								nImpresora.close();
				      							}else{
				      								mensajeError('No se pudo guardar.');
				      							}
				      							
				      						},
				      						failure: function(){
				      							nImpresora.setLoading(false);
				      							mensajeError('No se pudo guardar.');
				      							
				      						}
				      					});
				                    	  
				                    	  
				                      } else { // display error alert if the data is invalid
				                          Ext.Msg.alert('Datos inválidos', 'Datos no válidos.')
				                          
				                      }
				                  }
				              },
				              {
				  				text: 'Cancelar',
				  				icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png',
				  				handler: function() {
									this.up('window').close();
				  					
				  				}
				              }
				          ]
				})]
			});

		}
	 
	 
	 function habilitaDeshabilita(hd){
		 try{
				var sm=gridImpresoras.getSelectionModel();
				if(sm.getCount()<=0){
					return;
				}
				var sel =sm.getSelection();
				saveList=[];
				sel.forEach(function(s,idx){
					s.data.enable=hd;
					saveList.push(s.data);
				});
					
				ck='Haciendo petición habilita impresora';
			
				windowLoader.setLoading(true);
				Ext.Ajax.request({
					url: _URL_habilitaDeshabilitaImpresora,
					jsonData: {
						saveList : 	saveList
					},
					success: function(response) {
						ck='Decodificando respuesta';
						var res = Ext.decode(response.responseText);
						windowLoader.setLoading(false);
						
						if(res.success){
							storeImpresoras.reload();
							mensajeCorrecto('Aviso','Se ha guardado con exito.');
							windowLoader.close();
						}else{
							mensajeError('No se pudo guardar.');
						}
						
					},
					failure: function(){
						windowLoader.setLoading(false);
						mensajeError('No se pudo guardar.');
					}
				});
			}catch(e)
         {
             manejaExeption(e,ck);
         }
	 }
	 
	
	
});
</script>
<div id="maindivRolesUsuario" style="height:300px;"></div>