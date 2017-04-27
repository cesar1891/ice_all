<%@ page language="java" %>
<%@ include file="/taglibs.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Consulta de p&oacute;lizas</title>
        
        <script type="text/javascript" src="${ctx}/resources/extjs4/plugins/pagingpersistence/pagingselectionpersistence.js?${now}"></script>
        <script type="text/javascript">
        
var _CONTEXT = '${ctx}';

var _URL_CONSULTA_DATOS_ASEGURADO     = '<s:url namespace="/consultasPoliza" action="consultaAseguradosPoliza" />';
            
var ntramite = <s:property value="params.ntramite"/>;
var gridDatosAsegurado;

Ext.onReady(function() {
	
    // Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 1000*60*10; // 10 minutos
    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });

    Ext.selection.CheckboxModel.override({
        mode: 'SINGLE',
        allowDeselect: true
    });
    
    // Conversion para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
    
    /**INFORMACION DE LA SECCION DE ASEGURADOS**/
    //-------------------------------------------------------------------------------------------------------------
    // Modelo
    Ext.define('AseguradosModel', {
        extend: 'Ext.data.Model',
        fields: [
            {type:'string', name:'cdperson'},
            {type:'string', name:'cdrfc'},
            {type:'string', name:'cdrol'},
            {type:'string', name:'dsrol'},
            {type:'date'  , name:'fenacimi', dateFormat: 'd/m/Y'},
            {type:'string', name:'nmsituac'},
            {type:'string', name:'cdtipsit'},
            {type:'string', name:'sexo'},
            {type:'string', name:'nombre'},
            {type:'string', name:'status'},
            {type:'string', name:'grupo'},
            {type:'string', name:'cdgrupo'},
            {type:'string', name:'familia'},
            {type:'string', name:'cdfamilia'},
            {type:'string', name:'dsplan'},
            {type:'string', name:'parentesco'},
            {type:'string', name:'nmsitauc'}
        ],
        idProperty: 'cdperson'
    });
    
    // Store
    var storeAsegurados = new Ext.data.Store({
     model: 'AseguradosModel',
     groupField : 'grupo',
     pageSize: 15,
     proxy:
     {
          type: 'ajax',
          url : _URL_CONSULTA_DATOS_ASEGURADO,
	      reader:
	      {
	           type: 'json',
	           root: 'datosAsegurados',
	           totalProperty: 'totalCount',
	           simpleSortMode: true
	      }
     }
    });
    
    // Cargamos al inicio el store de asegurados:
    storeAsegurados.getProxy().setExtraParam('params.ntramite', ntramite);
    storeAsegurados.loadPage(1);
    
    gridDatosAsegurado = Ext.create('Ext.grid.Panel', {
        title   : 'DATOS DE LOS ASEGURADOS',
        store   : storeAsegurados,
        id      : 'gridDatosAsegurado',
        renderTo: 'dvConsultasPolizas',
        //width   : 950,
        width   : 810,
        height  : 500,
        autoScroll:true,
        selModel: Ext.create('Ext.selection.CheckboxModel', {mode: 'MULTI'}),
        plugins: [{ptype : 'pagingselectpersist'}],
        items:[{
           xtype:'textfield', name:'cdrfc', fieldLabel: 'RFC', readOnly: true, labelWidth: 120
        }],
        columns: [
            //{text:'Rol',dataIndex:'dsrol',width:130 , align:'left'},
        	/*{
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page.png',
                tooltip      : 'Ver Coberturas',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    var values = panelBusqueda.down('form').getForm().getValues();
                    
                    debug('record nmsituac:',record.get('nmsituac'));
                    values['params.nmsituac']=record.get('nmsituac');
                    
                    debug('form values:',values);
                    
//                    panelBusqueda.down('form').getForm().getValues()
                    
                    //Datos de Copagos de poliza
                    storeCopagosPoliza.load({
                        params: values,
                        callback: function(records, operation, success){
                            if(!success){
                                showMessage('Error', 'Error al obtener los copagos de la p\u00F3liza', 
                                    Ext.Msg.OK, Ext.Msg.ERROR)
                            }              
                            gridCopagosPoliza.store.sort([
						        { 
						            property    : 'orden',
						            direction   : 'ASC'
						        }
						    ]);
                        }
                    });
                    
                    
                    windowCoberturas.setTitle('COBERTURAS DE ' + record.get('nombre'));
                    windowCoberturas.show();
                }
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/lock.png',
                tooltip      : 'Ver endosos',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    var values = panelBusqueda.down('form').getForm().getValues();
                    debug('record nmsituac:',record.get('nmsituac'));
                    values['params.nmsituac']=record.get('nmsituac');
                    debug('form values:',values);
                }
            },{
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/information.png',
                tooltip      : 'Ver detalle del asegurado',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(gridView, rowIndex, colIndex, item, e, record, row) {
                    
                    // Se obtiene los parametros a enviar y se complementan:
                    var values = panelBusqueda.down('form').getForm().getValues();
                    values['params.nmsituac']=record.get('nmsituac');
                    values['params.cdtipsit']=record.get('cdtipsit');
                    debug('parametros para obtener los datos de tatrisit:', values);                	
                }
            },{
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/application_view_list.png',
                tooltip      : 'Siniestralidad',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    debug('record cdperson ==> :',record,record.get('cdperson'));
                    var values = panelBusqueda.down('form').getForm().getValues();
                    siniestralidad(null, null,record.get('cdperson'),null,"0");//cdunieco,cdramo, cdperson, nmpoliza
                }
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page_white_acrobat.png',
                tooltip      : 'Estado de cuenta',
                width        : 20,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
//                    debug('record cdperson ==> :',record,record.get('cdperson'));
                    var values = panelBusqueda.down('form').getForm().getValues();
                    
                    values['params.nmsituac']=record.get('nmsituac');
                    values['params.swfonsin']= "N";
                    _reporteEdoCta(values,"Estado de Cuenta");//cdunieco,cdramo, cdperson, nmpoliza
                }
                	
                
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page_white_database.png',
                tooltip      : 'Edo. Cuenta aplicaci&oacute;n Fondo',
                width        : 20,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
//                    debug('record cdperson ==> :',record,record.get('cdperson'));
                    var values = panelBusqueda.down('form').getForm().getValues();
                    
                    values['params.nmsituac']=record.get('nmsituac');
                    values['params.swfonsin']= "S";
                    
                    _reporteEdoCta(values,"Estado de Cuenta aplicaci&oacute;n Fondo");//cdunieco,cdramo, cdperson, nmpoliza
                }
            },*/
            {text:'Plan',dataIndex:'dsplan',width:90 , align:'left'},
            {text:'Tipo de <br/>asegurado',dataIndex:'parentesco',width:80 , align:'left'},
            {text:'Clave <br/>Asegurado',dataIndex:'cdperson',width:80,align:'left'},
            {text:'Nombre',dataIndex:'nombre',width:180,align:'left'},
            {text:'Estatus',dataIndex:'status',width:80,align:'left'},
            {text:'RFC',dataIndex:'cdrfc',width:100,align:'left'},
            {text:'Sexo',dataIndex:'sexo',width:60 , align:'left'},
            {text:'Grupo',dataIndex:'grupo', itemId: 'grupo',width:100, align:'left', hidden: true},
            {text:'Familia',dataIndex:'familia', itemId: 'familia',width:100, align:'left', hidden: true},
            {text:'Fecha Nac.',dataIndex:'fenacimi',width:90, align:'left',renderer: Ext.util.Format.dateRenderer('d/m/Y')}
        ],
        tbar: [/*{
                xtype : 'textfield',
                name : 'filtrarAseg',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Asegurado:</span>',
                labelWidth : 100,
                width: 260,
                maxLength : 50
            },'-',{
                xtype : 'textfield',
                name : 'filtrarCveAseg',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Cve. Asegurado:</span>',
                labelWidth : 120,
                width: 220,
                maxLength : 50
            },'-',{
                xtype : 'numberfield',
                name : 'filtrarFam',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Familia:</span>',
                labelWidth : 80,
                width: 240
            },*//*{
	            xtype    : 'button',
	            text     : 'Buscar',
	            icon     : _CONTEXT+'/resources/fam3icons/icons/zoom.png',
	            handler : function(btn) {
	            	debug('antes de asignar valores', storeAsegurados.getProxy().extraParams);
//		            storeAsegurados.getProxy().extraParams = panelBusqueda.down('form').getForm().getValues();
//		            storeAsegurados.getProxy().setExtraParam('params.nmsitaux', btn.up('grid').down('[name=filtrarFam]').getValue());
//		            storeAsegurados.getProxy().setExtraParam('params.nombre', btn.up('grid').down('textfield[name=filtrarAseg]').getValue());
//		            storeAsegurados.getProxy().setExtraParam('params.cdperson', btn.up('grid').down('textfield[name=filtrarCveAseg]').getValue());
	            	storeAsegurados.getProxy().setExtraParam('params.ntramite', btn.up('grid').down('[name=filtrarFam]').getValue());
		            debug('cdperson','params.cdperson', btn.up('grid').down('textfield[name=filtrarCveAseg]').getValue());
		            debug('despues de asignar valores', storeAsegurados.getProxy().extraParams);
		            //storeAsegurados.load();
		            storeAsegurados.loadPage(1);
	            }
                                
            }	*/
        ]
        ,features: [{
            groupHeaderTpl: '{name}',
            ftype:          'grouping',
            startCollapsed: false
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: storeAsegurados,
            displayInfo: true,
            displayMsg: 'Asegurados {0} - {1} of {2}',
            emptyMsg: "No hay asegurados"
        })
    });
    
    });
            
        </script>
        
    </head>
    <body>
        <div id="dvConsultasPolizas" style="height:550px"></div>
    </body>
</html>
