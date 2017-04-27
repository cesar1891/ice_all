Ext.define('VentanaDiagnostico',
{
    extend       : 'Ext.window.Window'
    ,title       : 'COMPONENTE DIAGN\u00d3STICO'
    ,itemId      : '_c1_instance'
    ,closeAction : 'destroy'
    ,width       : 600
    ,height      : 400
    ,modal       : true
    ,border      : 0
    ,defaults    :
    {
        style  : 'margin:5px;border-bottom:1px solid #CCCCCC;'
        ,xtype : 'displayfield'
        ,width : 250
    }
    ,layout   :
    {
        type     : 'table'
        ,columns : 2
    }
    ,constructor(config)
    {
        Ext.apply(this,
        {
            items    :
		    [
		        {
		            fieldLabel : 'TR\u00c1MITE'
		            ,value     : config.ntramite
		        }
		        ,{
		            fieldLabel : 'STATUS'
		            ,value     : config.status
		        }
		        ,{
		            fieldLabel : 'CLAVE DE TR\u00c1MITE'
		            ,value     : config.cdtipflu
		        }
		        ,{
		            fieldLabel : 'CLAVE DE PROCESO'
		            ,value     : config.cdflujomc
		        }
		        ,{
		            fieldLabel : 'TIPO DE ENTIDAD'
		            ,value     : config.tipoent
		        }
		        ,{
		            fieldLabel : 'CLAVE DE ENTIDAD'
		            ,value     : config.claveent
		        }
		        ,{
		            fieldLabel : 'ID DE ENTIDAD'
		            ,value     : config.webid
		        }
		        ,{
		            fieldLabel : 'CDUNIECO'
		            ,value     : config.cdunieco
		        }
		        ,{
		            fieldLabel : 'CDRAMO'
		            ,value     : config.cdramo
		        }
		        ,{
		            fieldLabel : 'ESTADO'
		            ,value     : config.estado
		        }
		        ,{
		            fieldLabel : 'NMPOLIZA'
		            ,value     : config.nmpoliza
		        }
		        ,{
		            fieldLabel : 'NMSITUAC'
		            ,value     : config.nmsituac
		        }
		        ,{
		            fieldLabel : 'NMSUPLEM'
		            ,value     : config.nmsuplem
		        }
		    ]
		    ,buttonAlign : 'center'
		    ,buttons     : _cargarBotonesEntidad
	        (
	            config.cdtipflu
	            ,config.cdflujomc
	            ,config.tipoent
	            ,config.claveent
	            ,config.webid
	            ,'_c1_instance'
	            ,config.ntramite
	            ,config.status
	            ,config.cdunieco
	            ,config.cdramo
	            ,config.estado
	            ,config.nmpoliza
	            ,config.nmsituac
	            ,config.nmsuplem
	        )
        });
        this.callParent(arguments);
    }
    ,mostrar : function()
    {
        var me = this;
        centrarVentanaInterna(me.show());
    }
});