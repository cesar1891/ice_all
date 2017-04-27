Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {

	
    Ext.selection.CheckboxModel.override( {
        mode: 'SINGLE',
        allowDeselect: true
    });

    Ext.define('CopagosPolizaModel',{
        extend: 'Ext.data.Model',
        fields: [
              {type:'int',    name:'orden'      },
              {type:'string',    name:'descripcion' },
              {type:'string',    name:'valor' },
              {type:'string',    name:'agrupador' }
        ]
    });
    

    // Store
    var storeCopagosPoliza = new Ext.data.Store({
        model: 'CopagosPolizaModel',
//        groupField : 'agrupador',
        proxy: {
           type: 'ajax',
           url : _URL_CONSULTA_COPAGOS_POLIZA,
            reader: {
                type: 'json',
                root: 'datosCopagosPoliza'
            }
        }
    });

    
    
 // GRID PARA LOS DATOS DE COPAGOS
    var gridCopagosPoliza = Ext.create('Ext.grid.Panel', {
        width   : 500,
        viewConfig: {
            stripeRows: false,
            enableTextSelection: true
        },
        //title   : 'DATOS COPAGOS',
        store   : storeCopagosPoliza,
        autoScroll:true,
        id      : 'gridCopagosPoliza',
        columns: [
            //{text:'Orden',            dataIndex:'orden',       width:50, sortable:false, hidden:true},
            {text:'Descripci\u00F3n', dataIndex:'descripcion', width:300, align:'left', sortable:false},
            {text:'Valor',            dataIndex:'valor',       width:200, align:'left', sortable:false}
        ]
//        ,features: [{
//            groupHeaderTpl: '{name}',
//            ftype:          'grouping',
//            startCollapsed: false
//        }]
    });
    gridCopagosPoliza.store.sort([
        { 
            property    : 'orden',
            direction   : 'ASC'
        }
    ]);
    
    
    Ext.create('Ext.panel.Panel',
    	    {
    	        border    : 0
    	        ,renderTo : 'div_clau2'
    	        ,items    :
    	        [
    	         	gridCopagosPoliza
    	        ]
    	    });
    
    /*http://localhost:8080/gseguros/consultasPoliza/consultaCopagosPoliza.action?params.cdunieco=1009&params.cdramo=2&params.estado=M&params.nmpoliza=36&params.suplemento=1*/
    
    var params = {
        	'params.cdunieco':_7_smap1.cdunieco,
        	'params.estado':_7_smap1.estado,
        	'params.cdramo':_7_smap1.cdramo,
        	'params.nmpoliza':_7_smap1.nmpoliza,
        	'params.suplemento':_7_smap1.suplemento,
        	'params.nmsituac':_7_smap1.nmsituac
    };
    
    storeCopagosPoliza.load({
        params: params,
        callback: function(records, operation, success){
            if(success){
                if(records.length <= 0){
                    Ext.Msg.show({
                         title: 'Aviso',
                         msg: 'No se encontraron datos',
                         buttons: Ext.Msg.OK,
                         icon: Ext.Msg.ERROR
                     });
                }
            }else{
                Ext.Msg.show({
                     title: 'Error',
                     msg: 'Error al obtener los datos',
                     buttons: Ext.Msg.OK,
                     icon: Ext.Msg.ERROR
                 });
            }
        }
    });
});
