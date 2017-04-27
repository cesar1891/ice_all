<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>

Ext.onReady(function(){
    
    // Conversión para el tipo de moneda
    Ext.util.Format.thousandSeparator = ',';
    Ext.util.Format.decimalSeparator = '.';
	
    var _URL_CONSULTA_DATOS_ASEGURADO 		= '<s:url namespace="/consultasPoliza" action="consultaDatosAsegurado" />';
	var _cdUnieco                     		= '<s:property value="params.cdunieco" />';
	var _cdRamo                       		= '<s:property value="params.cdramo" />';
	var _estado                       		= '<s:property value="params.estado" />';
	var _nmpoliza                     		= '<s:property value="params.nmpoliza" />';
	var _nmsuplem                       	= '<s:property value="params.suplemento" />';
	var _URL_LOADER_VER_EXCLUSIONES         = '<s:url namespace="/consultasPoliza" action="includes/verClausulas" />';
	var _URL_CONSULTA_COPAGOS_POLIZA        = '<s:url namespace="/consultasPoliza" action="consultaCopagosPoliza" />';
	var _URL_LOADER_VER_TATRISIT            = '<s:url namespace="/consultasPoliza" action="includes/verDatosTatrisit" />';
	var _URL_LOADER_HISTORIAL_RECLAMACIONES = '<s:url namespace="/siniestros"    action="includes/historialReclamaciones" />';
  
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
            {type:'string', name:'parentesco'}
        ]
    });
    
    Ext.define('CopagosPolizaModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'int',    name:'orden'      },
              {type:'string',    name:'descripcion' },
              {type:'string',    name:'valor' },
              {type:'string',    name:'agrupador' , id:'agrupadorId'},
              {type:'string',    name:'ordenAgrupador' }
        ]
    });
    // Store
    var storeAsegurados = new Ext.data.Store({
     model: 'AseguradosModel',
     groupField : 'grupo',
     pageSize: 25,
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
    
    var storeCopagosPoliza = new Ext.data.Store({
        model: 'CopagosPolizaModel',
//        groupField : 'ordenAgrupador',
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_COPAGOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosCopagosPoliza'
            }
        }
    });
    
    
    
    // GRID PARA LOS DATOS DE COPAGOS/COBERTURAS
    var gridCopagosPoliza = Ext.create('Ext.grid.Panel', {
        width   : 570,
        viewConfig: {
            stripeRows: false,
            enableTextSelection: true
        },
        //title   : 'DATOS COPAGOS',
        store   : storeCopagosPoliza,
        autoScroll:true,
        id      : 'gridCopagosPoliza',
        columns: [
            {text:'Descripci\u00F3n', dataIndex:'descripcion', width:370, align:'left', sortable:false},
            {text:'Valor',            dataIndex:'valor',       width:200, align:'left', sortable:false}
        ]
    });
    gridCopagosPoliza.store.sort([
        { 
            property    : 'orden',
            direction   : 'ASC'
        }
    ]);
    
    windowCoberturas = Ext.create('Ext.window.Window', {
                        title       : 'COBERTURAS',
                        buttonAlign : 'center',
                        autoScroll  : true,
                        modal       : true,
                        closeAction : 'hide',
                        width       : 600,
                        height      : 500,
                        items: [gridCopagosPoliza]
                     });
                     
    var gridDatosAsegurado = Ext.create('Ext.grid.Panel', {
        title   : 'DATOS DE LOS ASEGURADOS',
        store   : storeAsegurados,
        id      : 'gridDatosAsegurado',
        width   : 830,
        height  : 480 ,
        autoScroll:true,
        renderTo: 'divAsegs',
        autoScroll:true,
        items:[{
           xtype:'textfield', name:'cdrfc', fieldLabel: 'RFC', readOnly: true, labelWidth: 120
        }],
        columns: [
            //{text:'Rol',dataIndex:'dsrol',width:130 , align:'left'},
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/page.png',
                tooltip      : 'Ver Coberturas',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex)
                {
                    var record = grid.getStore().getAt(rowIndex);
                    debug("Valor de respuesta de record ==>",record);
                    var values ={
                        'params.nmsituac'  : record.get('nmsituac'),
                        'params.cdunieco'  : _cdUnieco,
                        'params.cdramo'    : _cdRamo,
                        'params.estado'    : _estado,
                        'params.nmpoliza'  : _nmpoliza,
                        'params.suplemento': _nmsuplem,
                        'params.icodpoliza': _nmpoliza
                    };
                    
                    debug('form values:',values);
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
                    centrarVentanaInterna(windowCoberturas.show());
                }
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/lock.png',
                tooltip      : 'Ver endosos',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(grid,rowIndex) {
                    var record = grid.getStore().getAt(rowIndex);
                    params ={
                        'params.cdunieco': _cdUnieco,
                        'params.cdramo': _cdRamo,
                        'params.estado': _estado,
                        'params.nmpoliza': _nmpoliza,
                        'params.suplemento': _nmsuplem,
                        'params.nmsituac':record.get('nmsituac')
                    };
                    centrarVentanaInterna(Ext.create('Ext.window.Window', {
                        title       : 'Endosos',
                        modal       : true,
                        buttonAlign : 'center',
                        autoScroll  : true,
                        width       : 450,
                        height      : 455,
                        loader      :
                        {
                            url      : _URL_LOADER_VER_EXCLUSIONES,
                            scripts  : true,
                            autoLoad : true,
                            params   : params
                        }
                     }).show());
                }
            },
            {
                xtype        : 'actioncolumn',
                icon         : _CONTEXT+'/resources/fam3icons/icons/information.png',
                tooltip      : 'Ver detalle del asegurado',
                width        : 20,
                menuDisabled : true,
                sortable     : false,
                handler      : function(gridView, rowIndex, colIndex, item, e, record, row) {
                    
                    var values ={
                        'params.cdunieco'  : _cdUnieco,
                        'params.cdramo'    : _cdRamo,
                        'params.estado'    : _estado,
                        'params.nmpoliza'  : _nmpoliza,
                        'params.suplemento': _nmsuplem,
                        'params.icodpoliza': _nmpoliza,
                        'params.nmsituac'  : record.get('nmsituac'),
                        'params.cdtipsit'  : record.get('cdtipsit')
                        
                    };
                    
                    debug('parametros para obtener los datos de tatrisit:', values);
                    
                    // Se crea ventana para mostrar el detalle del asegurado:
                    centrarVentanaInterna(Ext.create('Ext.window.Window', {
                        title       : 'Detalle de ' + record.get('nombre') + ':',
                        modal       : true,
                        width       : 600,
                        items: [{
                            xtype  : 'panel',
                            name   : 'pnlDatosTatrisit',
                            autoScroll  : true,
                            loader: {
                                url : _URL_LOADER_VER_TATRISIT,
                                scripts  : true,
                                loadMask : true,
                                autoLoad : true,
                                ajaxOptions : {
                                    method: 'POST'
                                },
                                params: values
                            }
                        }]
                    }).show());
                    
                }
            },
            {
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
                    var values ={
                        'params.cdunieco'  : _cdUnieco,
                        'params.cdramo'    : _cdRamo,
                        'params.estado'    : _estado,
                        'params.nmpoliza'  : _nmpoliza,
                        'params.suplemento': _nmsuplem,
                        'params.nmsituac'  : record.get('nmsituac'),
                        'params.cdtipsit'  : record.get('cdtipsit'),
                        'params.swfonsin'  : "N"
                        
                    };
                    _reporteEdoCta(values,"Estado de Cuenta");//cdunieco,cdramo, cdperson, nmpoliza
                } 
            },
        	{text:'Plan',dataIndex:'dsplan',width:90 , align:'left'},
        	{text:'Tipo de <br/> asegurado',dataIndex:'parentesco',width:100 , align:'left'},
            {text:'Clave <br/>Asegurado',dataIndex:'cdperson',width:100,align:'left'},
            {text:'Nombre',dataIndex:'nombre',width:250,align:'left'},
            {text:'Estatus',dataIndex:'status',width:90,align:'left'},
            {text:'RFC',dataIndex:'cdrfc',width:110,align:'left'},
            {text:'Sexo',dataIndex:'sexo',width:90 , align:'left'},
            {text:'Grupo',dataIndex:'grupo', itemId: 'grupo',width:100, align:'left', hidden: true},
            {text:'Familia',dataIndex:'familia', itemId: 'familia',width:100, align:'left', hidden: true},
            {text:'Fecha Nac.',dataIndex:'fenacimi',width:100, align:'left',renderer: Ext.util.Format.dateRenderer('d/m/Y')}
        ],
        tbar: [{
                xtype : 'textfield',
                name : 'filtrarAseg',
                id : 'filtrarAseg',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Asegurado:</span>',
                labelWidth : 100,
                width: 260,
                maxLength : 50
            },'-',{
                xtype : 'textfield',
                name : 'filtrarCveAseg',
                id : 'filtrarCveAseg',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Cve. Asegurado:</span>',
                labelWidth : 120,
                width: 220,
                maxLength : 50
            },'-',{
                xtype : 'numberfield',
                name : 'filtrarFam',
                id : 'filtrarFam',
                fieldLabel : '<span style="color:white;font-size:12px;font-weight:bold;">Filtrar Familia:</span>',
                labelWidth : 80,
                width: 240
            },{
                xtype    : 'button',
                text     : 'Buscar',
                icon     : _CONTEXT+'/resources/fam3icons/icons/zoom.png',
                handler : function(btn) {
                    debug('antes de asignar valores', storeAsegurados.getProxy().extraParams);
                }                                
            }   
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
    
    var params ={
        'params.cdunieco': _cdUnieco,
        'params.cdramo': _cdRamo,
        'params.estado': _estado,
        'params.nmpoliza': _nmpoliza,
        'params.suplemento': _nmsuplem
    };
        
    cargaStorePaginadoLocal(storeAsegurados, _URL_CONSULTA_DATOS_ASEGURADO, 'datosAsegurados', params, function(options, success, response){
        if(!success){
            showMessage('Error', 'Error al obtener los copagos de la p\u00F3liza', 
                Ext.Msg.OK, Ext.Msg.ERROR)
        }              
        gridDatosAsegurado.store.sort([
            { 
                property    : 'orden',
                direction   : 'ASC'
            }
        ]);
    });	
});

    function siniestralidad(cdunieco,cdramo, cdperson, nmpoliza, proceso){
        var windowHistSinies = Ext.create('Ext.window.Window',{
            modal       : true,
            buttonAlign : 'center',
            width       : 800,
            height      : 500,
            autoScroll  : true,
            loader      : {
                url     : _URL_LOADER_HISTORIAL_RECLAMACIONES,
                params  : {
                    'params.cdperson'  : cdperson,
                    'params.cdramo'    : cdramo,
                    'params.nmpoliza'  : nmpoliza,
                    'params.cdunieco'  : cdunieco,
                    'params.proceso'  : proceso
                    
                },
                scripts  : true,
                loadMask : true,
                autoLoad : true,
                ajaxOptions: {
                    method: 'POST'
                }
            },
            buttons: [{
                icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
                text: 'Cerrar',
                handler: function() {
                    windowHistSinies.close();
                }
            }]
        }).show();
        centrarVentanaInterna(windowHistSinies);
    }

    function _reporteEdoCta(values, nombre)
    {
        debug('iniciando reporte...');
        debug(_reporteEdoCtaSin);
        debug(values);
        var me = this;
        var urlRequestViewRep = _urlViewReport 
                + '?destype=cache'
                + '&p_unieco='      + values['params.cdunieco']
                + '&p_ramo='        + values['params.cdramo']   
                + '&p_estado='      + values['params.estado']   
                + '&p_poliza='      + values['params.nmpoliza']
                + '&p_situac='      + values['params.nmsituac']
                + '&p_suplem='      + values['params.suplemento']
                + '&p_swfonsin='    + values['params.swfonsin']
                + "&desformat=PDF"
                + "&userid="        + _reportsServerUser
                + "&report="        + _reporteEdoCtaSin
                + "&paramform=no";                                             

        debug(urlRequestViewRep);
        var numRand = Math.floor((Math.random() * 100000) + 1);
        debug(numRand);
        var windowVerRep = Ext.create('Ext.window.Window',
        {
            title          : nombre
            ,width         : 800
            ,height        : 550
            ,collapsible   : true
            ,titleCollapse : true
            ,html : '<iframe innerframe="'
                    + numRand
                    + '" frameborder="0" width="100" height="100"'
                    + 'src="'
                    + _urlViewDoc 
                    + "?contentType=application/pdf&url="
                    + encodeURIComponent(urlRequestViewRep)
                    + "\">"
                    + '</iframe>'
            ,listeners :
            {
                resize : function(win,width,height,opt)
                {
                    debug(width,height);
                    $('[innerframe="'+ numRand+ '"]').attr(
                    {
                        'width'   : width - 11
                        ,'height' : height 
                    });
                }
            }
        }).show();
        centrarVentanaInterna(windowVerRep);
    } 
</script>
<div id="divAsegs" style="height:500px;"></div>