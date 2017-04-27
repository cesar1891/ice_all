<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _cdUsuario = '<s:property value="params.cdusuario" />';

var _CONTEXT = '${ctx}';
var urlLoadImpresora   = '<s:url namespace="/catalogos" action="obtieneImpresorasUsuario" />';
var _URL_guardaImpresorasUsuario = '<s:url namespace="/catalogos" action="guardaImpresorasUsuario" />';

var nImpresora;
var ck;




Ext.onReady(function() {
	Ext.define('modeloImpresoras',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'IMPRESORA'},
                 {type:'string',	name:'IP'},
                 {type:'string',	name:'TIPO'},
                 {type:'string',    name:'DESCRIPCION'},
                 {type:'string',   name:'DISPONIBLE'},
                 {type:'boolean',   name:'ALTA'}
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
	    		'params.pv_cdusuario_i' : _cdUsuario
	    	},
	    	callback: function(records, operation, success) {
	    		windowLoader.setLoading(false);
	        }
	    });
		
	 
	 
	 
	 ////////////////////////////////////////////////////////
	 
	 var formImp=Ext.create('Ext.form.Panel', {
			url:_URL_guardaImpresorasUsuario,
		    border: false,
		    modal:true,
		    title: 'User Form',
		    height: 130,
		    width: 280,
		    bodyPadding: 10,
		    defaultType: 'textfield',
		    items: [
		        {
		            fieldLabel: 'IP ',
		            maxLength:15,
		        	minLength:7,
		            name: 'IP'
		        },
		        {
		        	xtype : 'numberfield',
		            fieldLabel: 'TIPO ',
		            maxValue:100,
		        	minValue:1,
		            name: 'TIPO'
		        },
		        {
		            fieldLabel: 'NOMBRE ',
		            name: 'NOMBRE'
		        }
		        ,
		        {
		            fieldLabel: 'DESCRIPCIÓN ',
		            name: 'DESCRIPCION'
		        }
		        ,
		        {
		            xtype: 'checkboxfield',
		            fieldLabel: 'ACTIVO ',
		            name: 'SWACTIVO',
		            checked:true
		        }
		        ,
		        {
		        	xtype: 'hiddenfield',
		        	value:'true',
		        	name:'NIMPRESORA'
		        }
		        
		    ],
		    buttonAlign: 'center',
		    buttons: [
		              {
		                  text: 'Guardar',
		                  icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
		                  handler: function() {
		                	  try{
		                		  
		                      ck='Guardeando impresora nueva';
		                      var form = this.up('form').getForm(); // get the basic form
		                      
		                      ck='validando form impresora nueva';
		                      if (form.isValid()) { // make sure the form contains valid data before submitting
		                    	    
		                    	  var dat=form.getValues();
		                      		dat.CDUSUARIO=_cdUsuario;
		                      		dat.TIPO=''+dat.TIPO;
		                    	  //console.log(dat);
		                      
	      						nImpresora.setLoading(true);
							     ck='Enviando datos impresora nueva';
		                    	  Ext.Ajax.request({
		      						url: _URL_guardaImpresorasUsuario,
		      						jsonData: {
		      							
		      							params : 	dat
		      						},
		      						success: function(response) {
		      							ck='Decodificanco respuesta';
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
		      							ck='Falló peticion';
		      							nImpresora.setLoading(false);
		      							mensajeError('No se pudo guardar.');
		      							
		      						}
		      					});
		                    	  
		                    	  
		                      } else { // display error alert if the data is invalid
		                          Ext.Msg.alert('Invalid Data', 'Please correct form errors.')
		                          this.up('window').close();
				  					windowLoader.close();
		                      }
		                  }catch(e)
		                  {
		                      manejaExeption(e,ck);
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
		});
		
		

	 
	 /////////////////////////////////////////////////
	

		// Create the combo box, attached to the states data store
		var comboSN=Ext.create('Ext.form.ComboBox', {
		    
		    store: Ext.create('Ext.data.Store', {
			    fields: ['valor', 'disp'],
			    data : [
			        {"valor":"SI", "disp":"SI"},
			        {"valor":"NO", "disp":"NO"}
			    ]
			}),
		    queryMode: 'local',
		    displayField: 'disp',
		    valueField: 'valor'
		});
	 
	 
	 var gridImpresoras = Ext.create('Ext.grid.Panel',{
			//title: 'Roles del usuario',
			renderTo : 'maindivRolesUsuario',
			autoScroll: true,
			 plugins: [
			           Ext.create('Ext.grid.plugin.CellEditing', {
			               clicksToEdit: 1
			           })
			       ],
			store:  storeImpresoras,
			//collapsible: true,
			titleCollapse: true,
			style: 'margin:0px',
			height: 300,
			columns       :[ 
			                 { header     : 'Seleccione' 
			                	 ,dataIndex  : 'ALTA'
			                	 , xtype: 'checkcolumn'
			                	 , menuDisabled : true
			                	 ,listeners: {
	                                            beforecheckchange: function (check, rowIndex,checked)
	                                            {
// 	                                            	if(storeRoles.getAt(rowIndex).get('CDSISROL')==CD_ROL_AGENTE){
// 	                                            		mensajeWarning('No se puede quitar el rol de AGENTE');
// 	                                            		return false;
// 	                                            	}
	                                            }
	                                       }
			                 },
			                 { header     : 'Impresora' ,dataIndex : 'IMPRESORA', flex: 1 }
			                 ,{ header     : 'Descripción' ,dataIndex : 'DESCRIPCION', editor: 'textfield',flex: 1 }
			               //  ,{ header     : 'Visible' ,dataIndex : 'DISPONIBLE', editor: comboSN, flex: 1 }

			 			   ],
			buttonAlign: 'center', 
			buttons:[{
					text: 'Actualizar',
					icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
					handler: function() {
							try{
						var saveList = [];
						var salir=false;
						ck='registrando rows a modificar';
      					storeImpresoras.getUpdatedRecords().forEach(function(record,index,arr){
      						
      						
      						//para convertir boolean a string ya que el server no lo convierte bien
      						record.data['ALTA'] = record.get('ALTA')==true?'S':'N';
      						record.data['DISPONIBLE'] = record.get('DISPONIBLE')=='SI'?'S':'N';

							if(record.get('ALTA')=='SI' && (''+record.get('DESCRIPCION')).trim()==''){
									mensajeWarning('Asigna una descripción');
									salir=true;
									return;
      						}
      						saveList.push(record.data);
      						console.log(record.data);
      					});
      					if(salir){
      						storeImpresoras.reload();
      						return;
      					}
      					if(saveList.length == 0 ){
      						//mensajeWarning('No se modificaron roles.');
      						windowLoader.close();

      						return ;
      					}
      					
      					ck='Haciendo petición guardar datos';
      					var ventana=_fieldById('asignaImp');
      					ventana.setLoading(true);
    					Ext.Ajax.request({
    						url: _URL_guardaImpresorasUsuario,
    						jsonData: {
    							params: {
    								'CDUSUARIO' : _cdUsuario
    					    	},
    							saveList : 	saveList
    						},
    						success: function(response) {
    							ck='Decodificando respuesta';
    							var res = Ext.decode(response.responseText);
    							ventana.setLoading(false);
    							
    							if(res.success){
    								storeImpresoras.reload();
    								mensajeCorrecto('Aviso','Se ha guardado con exito.');
    								windowLoader.close();
    							}else{
    								mensajeError('No se pudo guardar.');
    							}
    							
    						},
    						failure: function(){
    							ventana.setLoading(false);
    							mensajeError('No se pudo guardar.');
    						}
    					});
      					
					}catch(e)
		            {
		                manejaExeption(e,ck);
		            }
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
					url:_URL_guardaImpresorasUsuario,
				    border: false,
				    modal:true,
				    title: 'User Form',
				    height: 130,
				    width: 280,
				    bodyPadding: 10,
				    defaultType: 'textfield',
				    items: [
				        {
				            fieldLabel: 'IP ',
				            maxLength:15,
				        	minLength:7,
				            name: 'IP',
				            allowBlank:false
				        },
				        {
				        	xtype : 'numberfield',
				            fieldLabel: 'TIPO ',
				            maxValue:100,
				        	minValue:1,
				            name: 'TIPO',
				            allowBlank:false
				        },
				        {
				            fieldLabel: 'NOMBRE ',
				            name: 'NOMBRE',
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
				        ,
				        {
				            xtype: 'checkboxfield',
				            fieldLabel: 'ACTIVO ',
				            name: 'SWACTIVO',
				            checked:true
				        }
				        ,
				        {
				        	xtype: 'hiddenfield',
				        	value:'true',
				        	name:'NIMPRESORA'
				        }
				        
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
				                      		dat.CDUSUARIO=_cdUsuario;
				                      		dat.TIPO=''+dat.TIPO;
				                      		if(dat.SWACTIVO== undefined){
				                      			dat.SWACTIVO='N';
				                      		}else{
				                      			dat.SWACTIVO='S';
				                      		}
				                    	  //console.log(dat);
				                      
			      						nImpresora.setLoading(true);

				                    	  Ext.Ajax.request({
				      						url: _URL_guardaImpresorasUsuario,
				      						jsonData: {
				      							
				      							params : 	dat
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
	 
	


	
});

</script>
<div id="maindivRolesUsuario" style="height:300px;"></div>