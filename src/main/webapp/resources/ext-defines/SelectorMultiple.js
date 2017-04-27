Ext.define('SelectorMultiple',{
    extend       : 'Ext.panel.Panel',
    itemId       : 'panVars',
    defaults     : { style : 'margin:5px;'},
    layout		 : {
    	type    : 'table',
    	columns : 2,
    	tdAttrs : { valign:'middle' }
    },
    constructor  : function(config){
        debug('config:',config);
        var me = this;
        Ext.apply(me,{
            items : [
                Ext.create('Ext.grid.Panel', {
                		itemId      : 'gridDisVar',
	                	width       : 350,
					    height      : 250,
					    collapsible : true,
					    title       : 'Disponibles',
					    hideHeaders : true,	
					    autoScroll  : true,
					    store		: Ext.create('Ext.data.Store',{
					        autoLoad : true
					    	,fields  :
					    		[
					    		 'CDVARMAIL'
					    		 ,'DSVARMAIL'
					    		 ,'BDFUNCTION'
					    		 ]
					    	,proxy   :
					    	{
					    	    type         : 'ajax'
					    	    ,url         : _GLOBAL_URL_RECUPERACION
					    	    ,extraParams : {
					    	    	'params.consulta' : 'RECUPERAR_TVARMAIL'
					    		}
					    		,reader      :
					    		{
					    			type  : 'json'
					    			,root : 'list'
					    		}
					    	}
					    }),
					    multiSelect : true,
					    columns       : [{
					        dataIndex : 'DSVARMAIL',
					        flex      : 1
					        },
					    	{
					        xtype     : 'actioncolumn',
					        width     : 30,
					        tooltip   : 'Agregar',
					        icon      : _GLOBAL_DIRECTORIO_ICONOS+'add.png',
					        handler   : function(me, row, col, item, e, record)
                            {
					        	me.up('panel').up('panel').down('[itemId=gridSelVar]').getStore().add(record.copy());
					        	debug('me',me.up('panel').up('panel').down('[itemId=gridSelVar]').getStore());
                            }
					        }]
				})
				,Ext.create('Ext.grid.Panel', {
					itemId      : 'gridSelVar',
	                width       : 350,
					height      : 250,
					collapsible : true,
				    title       : 'Seleccionadas',
				    hideHeaders : true,	
				    autoScroll  : true,
				    multiSelect : true,
					store       : Ext.create('Ext.data.Store',{
						fields :
					    [
					     'CDVARMAIL'
				    	 ,'DSVARMAIL'
				    	 ,'BDFUNCTION'
				    	]}),
					columns       : [{
						dataIndex : 'DSVARMAIL',
					    flex      : 1
					   	},
					    {
					    xtype     : 'actioncolumn',
				        width     : 30,
				        tooltip   : 'Borrar',
				        icon      : _GLOBAL_DIRECTORIO_ICONOS+'delete.png',
				        handler   : function(me, row, col, item, e, record)
                        {
				        	me.getStore().remove(record);
				        	debug('me',me.getStore());
                        }
				        
				        }]
				})
			]
        });
        this.callParent(arguments);
    },
    getValue : function(){
    	var me       = this;
    	var storeSel = me.down('[itemId=gridSelVar]').getStore();
    	var sel      = [];
    	storeSel.each(function(record){
    		sel.push(record.get('CDVARMAIL'));
    	});
    	return sel.join(',');
    },
    setValue: function(value){
    	var me          = this;
    	var storeDis    = me.down('[itemId=gridDisVar]').getStore();
    	var disp        = {};
    	var sel	        = []; 
    	var storeSel    = me.down('[itemId=gridSelVar]').getStore();
    	storeSel.removeAll();
    	if(!Ext.isEmpty(value)){
    		sel = value.split(',');    		
    		storeDis.each(function(record){
    			disp[record.get('CDVARMAIL')] = record;
    		});    	
    		for(var i = 0; i < sel.length; i++){
    			storeSel.add(disp[sel[i]].copy());
    		}
    	}
    }
//    ,buttons : [				
//               {
//            	   text    : 'value',
//            	   handler : function(me){
//            		   alert(me.up('panel').getValue());
//            	   }
//               },
//               {
//            	   text    : 'setValue',
//            	   handler : function(me){
//            		   alert(me.up('panel').setValue(prompt('introduzca valores')));
//            	   }
//               }
//               ]
});