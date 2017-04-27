Ext.require([ 'Ext.form.*', 'Ext.data.*', 'Ext.chart.*', 'Ext.grid.Panel','Ext.layout.container.Column', 'Ext.selection.CheckboxModel' ]);

Ext.onReady(function() {

	Ext.create('Ext.tab.Panel', {
    	    //width: 800,
    	    //height: 400,
    	    renderTo: 'dvDetalleReclamacion',
    	    border : false,
    	    items: [{
    	            title: 'Informaci&oacute;n General',
    	            loader: {
	    	        	url: _URL_LOADER_INFO_GRAL_RECLAMACION,
	    	        	scripts: true,
	    	        	autoLoad: false,
	    	        	loadMask : true,
	    	        	ajaxOptions: {
	    	        		method: 'POST'
	    	        	}
	    	        },
	                listeners : {
	                    activate : function(tab) {
	                        tab.loader.load();
	                    }
	                }
	    	    }/*, {
	    	        title: 'Revisi&oacute;n Administrativa',
	    	        loader: {
	    	        	url: _URL_LOADER_REV_ADMIN,
	    	        	scripts: true,
	    	        	autoLoad: false,
	    	        	loadMask : true,
	    	        	ajaxOptions: {
	    	        		method: 'POST'
	    	        	},
	    	        	params : {
	    	        		'params.cdunieco' : _CDUNIECO,
	    	        		'params.cdramo'   : _CDRAMO,
	    	        		'params.estado'   : _ESTADO,
	    	        		'params.nmpoliza' : _NMPOLIZA
	    	        	}
	    	        },
	                listeners : {
	                    activate : function(tab) {
	                        tab.loader.load();
	                    }
	                }
	    	    }*/, {
	    	        title: 'C&aacute;lculos',
	    	        loader: {
	    	        	url: _URL_LOADER_CALCULOS,
	    	        	scripts: true,
	    	        	autoLoad: false,
	    	        	loadMask : true,
	    	        	ajaxOptions: {
	    	        		method: 'POST'
	    	        	},
	    	        	params : {
	    	        		'params.ntramite' : _NTRAMITE
	    	        	}
	    	        },
	                listeners : {
	                    activate : function(tab) {
	                        tab.loader.load();
	                    }
	                }
	    	    }]
    	});
	
	//para visualizar la información de los documentos
    /*var venDocuTramite=Ext.create('Ext.window.Window',
    {
        title           : 'Documentos del tr&aacute;mite '+ _NTRAMITE//+inputNtramite
        ,closable       : false
        ,width          : 370
        ,height         : 300
        ,autoScroll     : true
        ,collapsible    : true
        ,titleCollapse  : true
        ,startCollapsed : true
        ,resizable      : false
        ,loader         :
        {
            scripts   : true
            ,autoLoad : true
            ,url      : _UrlDocumentosPoliza
            ,params   :
            {
                'smap1.ntramite'   : _NTRAMITE
                ,'smap1.cdtippag' : _TIPOPAGO
                ,'smap1.cdtipate' : ''
                ,'smap1.cdtiptra' : _TIPO_TRAMITE_SINIESTRO
                ,'smap1.cdunieco' : _CDUNIECO
                ,'smap1.cdramo'   : _CDRAMO
                ,'smap1.estado'   : _ESTADO
                ,'smap1.nmpoliza' : _NMPOLIZA
                ,'smap1.nmsuplem' : _NMSUPLEM
                ,'smap1.nmsolici' : ''
                ,'smap1.tipomov'  : _TIPOPAGO
            }
        }
    }).showAt(600,0);
    venDocuTramite.collapse();*/
});