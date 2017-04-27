<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>

<script type="text/javascript">

var _CONTEXT = '${ctx}';

var _cdUsuario = '<s:property value="params.cdusuario" />';
var _esAgente = '<s:property value="params.esAgente" />';
var CD_ROL_AGENTE = 'EJECUTIVOCUENTA';

var _URL_LoadRolesUsuario   = '<s:url namespace="/catalogos" action="obtieneRolesUsuario" />';
var _URL_GuardaRolesUsuario = '<s:url namespace="/catalogos" action="guardaRolesUsuario" />';

Ext.onReady(function() {

	Ext.define('modeloRoles',{
        extend: 'Ext.data.Model',
        fields: [{type:'string',    name:'CDSISROL'},
                 {type:'string',    name:'DSSISROL'},
                 {type:'boolean',   name:'EXISTE_ROL'}
				]
    });
    
    var storeRoles = Ext.create('Ext.data.JsonStore', {
    	model:'modeloRoles',
        proxy: {
            type: 'ajax',
            url: _URL_LoadRolesUsuario,
            reader: {
                type: 'json',
                root: 'loadList'
            }
        }
    });
    
    storeRoles.load({
    	params: {
    		'params.PV_CDUSUARIO_I' : _cdUsuario
    	}
    });
    
    var gridRoles = Ext.create('Ext.grid.Panel',{
		//title: 'Roles del usuario',
		renderTo : 'maindivRolesUsuario',
		autoScroll: true,
		store:  storeRoles,
		//collapsible: true,
		titleCollapse: true,
		style: 'margin:0px',
		height: 300,
		columns       :[ { dataIndex  : 'CDSISROL', hidden: true},
		                 { header     : 'Seleccione' ,dataIndex  : 'EXISTE_ROL', xtype: 'checkcolumn', menuDisabled : true,
                                       listeners: {
                                            beforecheckchange: function (check, rowIndex,checked)
                                            {
                                            	if(storeRoles.getAt(rowIndex).get('CDSISROL')==CD_ROL_AGENTE){
                                            		mensajeWarning('No se puede quitar el rol de AGENTE');
                                            		return false;
                                            	}
                                            }
                                       }
		                 },
		                 { header     : 'Rol del Sistema' ,dataIndex : 'DSSISROL', flex: 1 }
		 			   ],
		buttonAlign: 'center', 
		buttons:[{
				text: 'Actualizar Roles',
				icon    : _CONTEXT+'/resources/fam3icons/icons/disk.png',
				handler: function() {
					if(_esAgente == 'N'){
						
						var almenos1rol = false;
						storeRoles.each(function(record){
							if(record.get('EXISTE_ROL')){
								almenos1rol = true;
								return false;
							}
						});
						
						if(!almenos1rol){
							mensajeWarning('El usuario debe tener almenos un rol.');
							return;
						}
					}
					
					var saveList = [];
					storeRoles.getUpdatedRecords().forEach(function(record,index,arr){
						//para convertir boolean a string ya que el server no lo convierte bien
						record.data['EXISTE_ROL'] = record.get('EXISTE_ROL')==true?'I':'D';
						saveList.push(record.data);
					});
					
					if(saveList.length == 0){
						mensajeWarning('No se modificaron roles.');
						return;
					}
					
					gridRoles.setLoading(true);
					Ext.Ajax.request({
						url: _URL_GuardaRolesUsuario,
						jsonData: {
							params: {
								'PV_CDUSUARI_I' : _cdUsuario
					    	},
							saveList : 	saveList
						},
						success: function(response) {
							var res = Ext.decode(response.responseText);
							gridRoles.setLoading(false);
							
							if(res.success){
								storeRoles.reload();
								mensajeCorrecto('Aviso','Se ha guardado con exito.');
								windowLoader.close();
							}else{
								mensajeError('No se pudo guardar.');
							}
							
						},
						failure: function(){
							gridRoles.setLoading(false);
							mensajeError('No se pudo guardar.');
						}
					});
				}
		},{
			text: 'Cancelar',
			icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png',
			handler: function() {
				windowLoader.close();
			}
		}]
	});
	
});


</script>

<div id="maindivRolesUsuario" style="height:300px;"></div>