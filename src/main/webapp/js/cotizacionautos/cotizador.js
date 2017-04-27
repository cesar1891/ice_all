Ext.onReady(function() {

    var image = Ext.create('Ext.Img', {
    	src: '../../images/cotizacionautos/panelCot.png',rowspan: 2,width: 170,
	});
	
    function CreateWin(id) {
		Ext.create('Ext.Window', {
			title: 'Número de Cotización Anterior...',
			id: id,
			resizable: false,
			padding:0,
			closable: false,
			width: 370,
			height: 168,
			layout: {type: 'table',columns: 2},
			items:[image,{
		        xtype: 'numberfield',
		        labelAlign:'top',
		        margin:'0 0 0 20',width: 150,
		        id: 'nmpoliza',
		        fieldLabel: 'No. de Cotización',
		        minValue: 0
		    },{
		xtype:'button',text:'Cargar',margin:'0 0 0 90',listeners: {
			click: {
    			fn: function(c ){ 
    				if(Ext.getCmp('nmpoliza').getValue() != null){
    					Ext.Ajax.request({
    				        url: '../../cotizacionautos/buscaCotizacion.action',
    				        params: {nmpoliza: Ext.getCmp('nmpoliza').getValue(),tarea:'buscaCotizacion'},
    				        success: function(response, opts){  
								var text = trim(response.responseText);
								var jsonResponse = Ext.JSON.decode(text);
								if(jsonResponse.success == '0'){
									alert('Lo sentimos pero la cotización no existe.');
								}else{
			    				    window.open('../../jsp/cotizacionautos/panel.jsp?nmpoliza=' + Ext.getCmp('nmpoliza').getValue());
								}
    				        }
    					});
    				}
    			}
			}
		}
	}]
		}).show();}
	
	var win = CreateWin('cotanterior');

	
});///Termina Ext.onReady
function  trim (myString){return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');}











