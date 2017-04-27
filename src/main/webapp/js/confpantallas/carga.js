Ext.onReady(function() {
	Ext.Ajax.timeout = 950000;

 	var image = Ext.create('Ext.Img', {
    	src: '../../images/confpantallas/icon/paneles.png',rowspan: 2
	});
	
	var modeloC = Ext.define('modelComboBox',{extend:'Ext.data.Model',fields:[{name:'key',type:'string'},{name:'value',type:'string'}]});

	var storePaneles = Ext.create('Ext.data.Store', {
     model: modeloC,
     proxy: {
         type: 'ajax',
         url: '../../confpantallas/cargainfo.action',
         reader: {
             type: 'json',root: 'success'
         },
					extraParams: {
						tarea: 'llenaComboPaneles'
					}
     },
     autoLoad: false
 });

	function CreateWin(id) {
		Ext.create('Ext.Window', {
			title: 'Carga de paneles parametrizados...',
			id: id,
			resizable: false,
			padding:5,
			closable: false,
			width: 380,
			height: 180,
			layout: {type: 'table',columns: 2},
			items:[image,{
					xtype:'combobox',
					labelAlign:'top',
        			fieldLabel: 'Selecciona el panel a presentar',
        			margin:'0 5 5 20',
        			width:205, 
        			id:'comboxPnl',
        			typeAhead:true,
        			store: storePaneles,
        			forceSelection: true,
    				valueField: 'key',
    				displayField: 'value',
    				triggerAction: 'all'

		}, {
		xtype:'button',text:'Cargar',margin:'5 5 0 160',listeners: {
			click: {
    			fn: function(c ){ 
    				if(Ext.getCmp('comboxPnl').getValue() != null){
    					Ext.Ajax.request({
    				        url: '../../confpantallas/pintapanel.action',
    				        params: {nombrepanel: Ext.getCmp('comboxPnl').getValue(),tarea:'pintaPanel'},
    				        success: function(response, opts){  
    				            window.open('../../jsp/confpantallas/newpanel.jsp');
    				        }
    					});
    				}
    			}
			}
		}
	}]
		}).show();}
	
	var win = CreateWin('login');
	
	
	
	
	
	
	
	
	
	
	
});///Termina Ext.onReady
function  trim (myString){return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');}











