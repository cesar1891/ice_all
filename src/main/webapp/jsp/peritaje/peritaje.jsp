<%@ include file="/taglibs.jsp"%>
<html>
<head>
<style>
a._p1_front
{
    background: url('${ctx}/resources/images/peritaje/yellow20.png');
}
a._p1_front, a._p1_front_edited
{
    display         : block;
    lineHeight      : 100%;
    text-decoration : none;
}
a._p1_front_edited1
{
    background: url('${ctx}/resources/images/peritaje/red40.png');
}
a._p1_front_edited2
{
    background: url('${ctx}/resources/images/peritaje/red40.png');
}
a._p1_front_edited3
{
    background: url('${ctx}/resources/images/peritaje/red40.png');
}
a._p1_front_edited4
{
    background: url('${ctx}/resources/images/peritaje/red40.png');
}
</style>
<script>
////// overrides //////
Ext.override(Ext.field.Select,
{
    initialize : function()
    {
        this.callParent();
        this.setDefaultPhonePickerConfig(
        {
            cancelButton : 'Cancelar'
            ,doneButton  : 'Aceptar'
        });
        this.setDefaultTabletPickerConfig(
        {
            cancelButton : 'Cancelar'
            ,doneButton  : 'Aceptar'
        });
    }
});
Ext.override(Ext.Picker,
{
    initialize : function()
    {
        this.callParent();
        this.setCancelButton('Cancelar');
        this.setDoneButton('Aceptar');
    }
}); 
////// overrides //////

////// variables //////|

var _p1_urlObtenerListaOrdenesInspeccion    = '<s:url namespace="/peritaje" action="obtenerListaOrdenesInspeccion"    />';
var _p1_urlObtenerListaOrdenesAjuste        = '<s:url namespace="/peritaje" action="obtenerListaOrdenesAjuste"        />';
var _p1_urlObtenerDetalleOrdenInspeccion    = '<s:url namespace="/peritaje" action="obtenerDetalleOrdenInspeccion"    />';
var _p1_urlObtenerDetalleOrdenAjuste        = '<s:url namespace="/peritaje" action="obtenerDetalleOrdenAjuste"        />';
var _p1_urlObtenerDatosVehiculo             = '<s:url namespace="/peritaje" action="obtenerDatosVehiculo"             />';
var _p1_urlGuardarDatosVehiculo             = '<s:url namespace="/peritaje" action="guardarDatosVehiculo"             />';
var _p1_urlObtenerDatosSeguridad            = '<s:url namespace="/peritaje" action="obtenerDatosSeguridad"            />';
var _p1_urlGuardarDatosSeguridad            = '<s:url namespace="/peritaje" action="guardarDatosSeguridad"            />';
var _p1_urlObtenerDetalleAccesorios         = '<s:url namespace="/peritaje" action="obtenerDetalleAccesorios"         />';
var _p1_urlGuardarDetalleAccesorios         = '<s:url namespace="/peritaje" action="guardarDetalleAccesorios"         />';
var _p1_urlGuardarDetalleInspeccion         = '<s:url namespace="/peritaje" action="guardarDetalleInspeccion"         />';
var _p1_urlObtenerListaRepuestosDisponibles = '<s:url namespace="/peritaje" action="obtenerListaRepuestosDisponibles" />';
var _p1_urlObtenerDetalleAjuste             = '<s:url namespace="/peritaje" action="obtenerDetalleAjuste"             />';
var _p1_urlObtenerListaManoObra             = '<s:url namespace="/peritaje" action="obtenerListaManoObra"             />';
var _p1_urlDescargarDocumento               = '<s:url namespace="/peritaje" action="descargaDocumento"                />';
var _p1_urlGuardarRepuestosAjuste           = '<s:url namespace="/peritaje" action="guardarRepuestosAjuste"           />';
var _p1_urlGuardarManoObraAjuste            = '<s:url namespace="/peritaje" action="guardarManoObraAjuste"            />';
var _p1_urlObtenerDatosPresupuesto          = '<s:url namespace="/peritaje" action="obtenerDatosPresupuesto"          />'

var _p1_view;

var _p1_storeOrdenesInspeccion;
var _p1_gridOrdenesInspeccion;
var _p1_formOrden;
var _p1_tabbed;
var _p1_piezaSelected;
var _p1_danioPicker;
var _p1_inspRecordSelected;

var _p1_storeOrdenesAjuste;
var _p1_gridOrdenesAjuste;
var _p1_formOrdenAjuste;
var _p1_tabbedAjuste;
var _p1_storeRepuestosDisponibles;
var _p1_storeRepuestosSeleccionados;
var _p1_storeTrabajosDisponibles;
var _p1_storeTrabajosSeleccionados;
var _p1_formManoObra;

var _p1_cons=
{
	numeroSiniestro           : '<s:property value="consNumeroSiniestro"          />'
    ,numeroPoliza             : '<s:property value="consNumeroPoliza"             />'
    ,numeroCertificado        : '<s:property value="consNumeroCertificado"        />'
    ,nombreAsegurado          : '<s:property value="consNombreAsegurado"          />'
    ,telefonoOficinaAsegurado : '<s:property value="consTelefonoOficinaAsegurado" />'
    ,telefonoCelularAsegurado : '<s:property value="consTelefonoCelularAsegurado" />'
};
debug('_p1_cons:',_p1_cons);

var _p1_nombrePiezas =
[
	''
	,'LUZ DEL. DER.'
	,'LUZ DEL. IZQ.'
	,'RADIADOR'
	,'PARACHOQUE DELANTERO'
	,'ESPEJO RETROVISOR DER.'
	,'ESPEJO RETROVISOR IZQ.'
	,'PUERTA DEL. IZQ.'
	,'CRISTAL DEL. IZQ.'
	,'LLANTA DEL. IZQ.'
	,'GUARDAFANGOS DEL. IZQ.'
	,'CRISTAL IZQ. TRA.'
	,'PUERTA IZQ. TRA.'
	,'LLANTA IZQ. TRA.'
	,'GUARDAFANGOS TRA. IZQ.'
	,'LUZ TRA. IZQ.'
	,'LUZ TRA. DER.'
	,'PARACHOQUE TRASERO'
	,'CRISTAL DER. TRA.'
	,'LLANTA DER. TRA.'
	,'GUARDAFANGOS DER. TRA.'
	,'PUERTA DER. TRA.'
	,'CRISTAL DER. DEL.'
	,'PUERTA DER. DEL.'
	,'GUARDAFANGOS DER. DEL.'
	,'LLANTA DER. DEL.'
	,'CAPO'
	,'CRISTAL FRONTAL'
	,'TOLDO'
	,'CRISTAL TRASERO'
	,'MALETERO'
];
////// variables //////

Ext.setup({onReady:function()
{
    ////// modelos //////
    Ext.define('_p1_OrdenInspeccion',
    {
        extend  : 'Ext.data.Model'
        ,config :
        {
            fields :
            [
                'AIREACONDIC'
                ,'ALARMA'
			    ,'ANNO'
			    ,'BLINDAJE'
			    ,'BOVEDA'
			    ,'CAPPUESTOS'
			    ,'CAVA'
			    ,'COLOR'
			    ,'CORTACORR'
			    ,'DECLASINIEST'
			    ,'DESCRIPDANOS'
			    ,'DESTINADO'
			    ,'DISPSATELITAL'
			    ,'ESTACAOPLATA'
			    ,'GRABAVIDRIO'
			    ,'KMACTUAL'
			    ,'LLANTAREPUES'
			    ,'LUGARAJUS'
			    ,'LUGARINSP'
			    ,'MARCA'
			    ,'MODELO'
                ,'NMORDEN'
			    ,'NMPLACA'
			    ,'OBSERVACIONES'
			    ,'POSEECABINA'
			    ,'RADIOCD'
			    ,'RADIOFIJO'
			    ,'RADIOREPRO'
			    ,'RINES'
			    ,'SERIALCARR'
			    ,'SERIALMOT'
			    ,'TAPICERIA'
			    ,'TAZAS'
                ,'TIPOINSP'
                ,'TONELADAS'
                ,'TRANCAPALAN'
                ,'TRANCAPEDAL'
			    ,'TRANSMISION'
			    ,'VERSION'
			    ,_p1_cons.numeroSiniestro
			    ,_p1_cons.numeroPoliza
			    ,_p1_cons.numeroCertificado
			    ,_p1_cons.nombreAsegurado
			    ,_p1_cons.telefonoOficinaAsegurado
			    ,_p1_cons.telefonoCelularAsegurado
            ]
        }
    });
    
    Ext.define('_p1_Repuesto',
    {
    	extend  : 'Ext.data.Model'
    	,config :
    	{
    		fields :
    		[
    		    'CODIGO'
    		    ,'DESCRIPCION'
    		    ,'MONTO'
    		]
    	}
    });
    
    Ext.define('_p1_TrabajoDisponible',
    {
    	extend  : 'Ext.data.Model'
    	,config :
    	{
    		fields :
	    	[
	    	    'CODIGO'
	    	    ,'DESCRIPCION'
	    	    ,'MONTO'
	    	]
    	}
    });
    
    Ext.define('_p1_TrabajoSeleccionado',
    {
        extend  : 'Ext.data.Model'
        ,config :
        {
        	fields :
	        [
	            'NMORDEN'
	            ,'NMPIEZA'
	            ,'DSPIEZA'
	            ,'CODIGO'
	            ,'DESCRIPCION'
	            ,'MONTO'
	        ]
        }
    });
    ////// modelos //////

    ////// stores //////
    _p1_storeOrdenesInspeccion = Ext.create('Ext.data.Store',
    {
        model     : '_p1_OrdenInspeccion'
        ,autoLoad : false
        ,proxy    :
        {
        	url     : _p1_urlObtenerListaOrdenesInspeccion
        	,type   : 'ajax'
        	,reader :
        	{
        		type          : 'json'
        		,rootProperty : 'strListMapOut'
        	}
        }
    });
    
    _p1_storeRepuestosDisponibles = Ext.create('Ext.data.Store',
    {
        model     : '_p1_Repuesto'
        ,autoLoad : false
        ,grouper  : function(record)
        {
        	return record.get('DESCRIPCION')[0];
        }
        ,proxy    :
        {
            url     : _p1_urlObtenerListaRepuestosDisponibles
            ,type   : 'ajax'
            ,reader :
            {
                type          : 'json'
                ,rootProperty : 'strListMapOut'
            }
        }
    });
    
    _p1_storeRepuestosSeleccionados = Ext.create('Ext.data.Store',
    {
        model     : '_p1_Repuesto'
    });
    
    _p1_storeTrabajosDisponibles = Ext.create('Ext.data.Store',
    {
        model     : '_p1_TrabajoDisponible'
        ,autoLoad : false
        ,proxy    :
        {
        	url     : _p1_urlObtenerListaManoObra
            ,type   : 'ajax'
            ,reader :
            {
                type          : 'json'
                ,rootProperty : 'strListMapOut'
            }
        }
    });
    
    _p1_storeTrabajosSeleccionados = Ext.create('Ext.data.Store',
    {
    	model : '_p1_TrabajoSeleccionado'
    });
    
    _p1_storeOrdenesAjuste = Ext.create('Ext.data.Store',
    {
        model     : '_p1_OrdenInspeccion'
        ,autoLoad : false
        ,proxy    :
        {
            url     : _p1_urlObtenerListaOrdenesAjuste
            ,type   : 'ajax'
            ,reader :
            {
                type          : 'json'
                ,rootProperty : 'strListMapOut'
            }
        }
    });
    ////// stores //////

    ////// componentes //////
    Ext.define('_p1_ComboTipoServicio',
    {
        extend      : 'Ext.field.Text'
        ,config     :
        {
            label    : 'Tipo de inspecci&oacute;n'
	        ,name : 'TIPOINSP'
            /*,options :
            [
                { value  : '1', text : 'Inspección'}
                ,{ value : '2', text : 'Reinspección'}
                ,{ value : '3', text : 'Aumento de SA'}
                ,{ value : '4', text : 'Inclusión de accesorios'}
            ]*/
        }
    });
    
    Ext.define('_p1_ComboLugarInspeccion',
    {
        extend      : 'Ext.field.Text'
        ,config     :
        {
            label    : 'Lugar de inspecci&oacute;n'
            ,name    : 'LUGARINSP'
            /*,options :
            [
                { value  : '1', text : 'México'}
                ,{ value : '2', text : 'Puebla'}
                ,{ value : '3', text : 'Tlaxcala'}
            ]*/
        }
    });
    
    Ext.define('_p1_FieldPlaca',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Placa'
            ,name : 'NMPLACA'
        }
    });
    
    Ext.define('_p1_FieldModelo',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Modelo'
            ,name : 'MODELO'
        }
    });
    
    Ext.define('_p1_ComboColor',
    {
        extend      : 'Ext.field.Text'
        ,config     :
        {
            label    : 'Color'
            ,name    : 'COLOR'
            /*,options :
            [
                { value  : 'RO', text : 'Rojo'     }
                ,{ value : 'VE', text : 'Azul'     }
                ,{ value : 'AZ', text : 'Amarillo' }
            ]*/
        }
    });
    
    Ext.define('_p1_ComboTipoTransmision',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Transmisi&oacute;n'
            ,name    : 'TRANSMISION'
            ,options :
            [
                { value  : 'AUTOMATICA' , text : 'AUTOMATICA' }
                ,{ value : 'SINCRONICA' , text : 'SINCRONICA' }
                ,{ value : 'TRIPTONICA' , text : 'TRIPTONICA' }
            ]
        }
    });
    
    Ext.define('_p1_ComboDanio',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Da&ntilde;o'
            ,options :
            [
                { value  : '0'   , text : 'Ninguno' }
                ,{ value : '10'  , text : '10%' }
                ,{ value : '20'  , text : '20%' }
                ,{ value : '30'  , text : '30%' }
                ,{ value : '40'  , text : '40%' }
                ,{ value : '50'  , text : '50%' }
                ,{ value : '60'  , text : '60%' }
                ,{ value : '70'  , text : '70%' }
                ,{ value : '80'  , text : '80%' }
                ,{ value : '90'  , text : '90%' }
                ,{ value : '100' , text : 'Total' }
            ]
        }
    });
    
    Ext.define('_p1_ComboDanioAgrupado',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Da&ntilde;o'
            ,options :
            [
                {  value : 'NINGUNO' , text : 'NINGUNO' }
                ,{ value : 'LEVE'    , text : 'LEVE'    }
                ,{ value : 'MEDIO'   , text : 'MEDIO'   }
                ,{ value : 'FUERTE'  , text : 'FUERTE'  }
                ,{ value : 'SALVAR'  , text : 'SALVAR'  }
                ,{ value : 'CAMBIO'  , text : 'CAMBIO'  }
            ]
        }
    });
    
    Ext.define('_p1_ComboTrabajoDisponible',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label         : 'Trabajo'
            ,store        : _p1_storeTrabajosDisponibles
            ,valueField   : 'CODIGO'
            ,displayField : 'DESCRIPCION'
        }
    });
    
    Ext.define('_p1_ComboMaterialTapiceria',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Tapicer&iacute;a'
            ,name    : 'TAPICERIA'
            ,options :
            [
                { value  : 'TELA'      , text : 'TELA'      }
                ,{ value : 'CUERO'     , text : 'CUERO'     }
                ,{ value : 'SEMICUERO' , text : 'SEMICUERO' }
            ]
        }
    });
    
    Ext.define('_p1_ComboDestinado',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Destinado'
            ,name    : 'DESTINADO'
            ,options :
            [
                { value  : 'PARTICULAR' , text : 'PARTICULAR' }
                ,{ value : 'CARGA'      , text : 'CARGA'    }
            ]
        }
    });
    
    Ext.define('_p1_ComboMaterialCava',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Cava'
            ,name    : 'CAVA'
            ,options :
            [
                { value  : 'ALUMINIO' , text : 'ALUMINIO' }
                ,{ value : 'HIERRO'   , text : 'HIERRO'   }
                ,{ value : 'FIBRA'    , text : 'FIBRA'    }
            ]
        }
    });
    
    Ext.define('_p1_ComboTipoRines',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Rines'
            ,name    : 'RINES'
            ,options :
            [
                { value  : 'ORIGINALES'    , text : 'ORIGINALES'    }
                ,{ value : 'NO ORIGINALES' , text : 'NO ORIGINALES' }
            ]
        }
    });
    
    Ext.define('_p1_ComboTipoTazas',
    {
        extend      : 'Ext.field.Select'
        ,config     :
        {
            label    : 'Tazas'
            ,name    : 'TAZAS'
        	,options :
            [
                { value  : 'ORIGINALES'    , text : 'ORIGINALES'    }
                ,{ value : 'NO ORIGINALES' , text : 'NO ORIGINALES' }
            ]
        }
    });
    
    Ext.define('_p1_FieldCapacidadToneladas',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label     : 'Capacidad en toneladas'
            ,name     : 'TONELADAS'
            ,minValue : 0
        }
    });
    
    Ext.define('_p1_FieldImporte',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label     : 'Importe'
        }
    });
    
    Ext.define('_p1_ComboCabina',
    {
        extend      : 'Ext.field.Toggle'
        ,config     :
        {
            label    : '¿Pose&aacute; cabina?'
            ,name    : 'POSEECABINA'
            /*,options :
            [
                { value  : 'N', text : 'No' }
                ,{ value : 'S', text : 'Si' }
            ]*/
        }
    });
    
    Ext.define('_p1_FieldAnio',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label     : 'A&ntilde;o'
            ,name     : 'ANNO'
            ,minValue : 1800
            ,maxValue : 2100
        }
    });
    
    Ext.define('_p1_FieldSerialMotor',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Serial del motor'
            ,name : 'SERIALMOT'
        }
    });
    
    Ext.define('_p1_FieldSerialCarroceria',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Serial de la carrocer&iacute;a'
            ,name : 'SERIALCARR'
        }
    });
    
    Ext.define('_p1_FieldObservaciones',
    {
        extend  : 'Ext.field.TextArea'
        ,config :
        {
            label   : 'Observaciones'
            ,name   : 'OBSERVACIONES'
            ,height : 300
        }
    });
    
    Ext.define('_p1_FieldMarca',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Marca'
            ,name : 'MARCA'
        }
    });
    
    Ext.define('_p1_FieldVersion',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Versi&oacute;n'
            ,name : 'VERSION'
        }
    });
    
    Ext.define('_p1_FieldKilometraje',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label : 'Kilometraje actual'
            ,name : 'KMACTUAL'
        }
    });
    
    Ext.define('_p1_FieldNumeroInspeccion',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label : 'No. Orden'
            ,name : 'NMORDEN'
        }
    });
    
    Ext.define('_p1_FieldNumeroSiniestro',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'No. Siniestro'
            ,name : _p1_cons.numeroSiniestro
        }
    });
    
    Ext.define('_p1_FieldNumeroPoliza',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'No. P&oacute;liza'
            ,name : _p1_cons.numeroPoliza
        }
    });
    
    Ext.define('_p1_FieldNumeroCertificado',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label : 'No. Certificado'
            ,name : _p1_cons.numeroCertificado
        }
    });
    
    Ext.define('_p1_FieldAsegurado',
    {
        extend  : 'Ext.field.Text'
        ,config :
        {
            label : 'Asegurado'
            ,name : _p1_cons.nombreAsegurado
        }
    });
    
    Ext.define('_p1_FieldTelefonoOficinaAsegurado',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label : 'Tel&eacute;fono de oficina'
            ,name : _p1_cons.telefonoOficinaAsegurado
        }
    });
    
    Ext.define('_p1_FieldTelefonoCelularAsegurado',
    {
        extend  : 'Ext.field.Number'
        ,config :
        {
            label : 'Tel&eacute;fono celular'
            ,name : _p1_cons.telefonoCelularAsegurado
        }
    });
    
    Ext.define('_p1_FieldCapacidadPuestos',
    {
        extend  : 'Ext.field.Spinner'
        ,config :
        {
            label      : 'Capacidad de puestos'
            ,name      : 'CAPPUESTOS'
            ,minValue  : 1
            ,maxValue  : 100
            ,stepValue : 1
        }
    });
    
    Ext.define('_p1_GridOrdenesInspeccion',
    {
        extend  : 'Ext.List'
        ,config : 
        {
            title             : '&Oacute;rdenes de inspecci&oacute;n'
            ,store            : _p1_storeOrdenesInspeccion
            ,itemTpl          : 'No.{NMORDEN} [{NMPLACA}] {MARCA} - {MODELO} - {COLOR}'
            ,onItemDisclosure : function(){}
            ,listeners        :
            {
                itemtap : _p1_itemtap
            }
        }
    });
    
    Ext.define('_p1_GridOrdenesAjuste',
    {
        extend  : 'Ext.List'
        ,config : 
        {
            title             : '&Oacute;rdenes de ajuste'
            ,store            : _p1_storeOrdenesAjuste
            ,itemTpl          : 'No.{NMORDEN} [{NMPLACA}] {MARCA} - {MODELO} - {COLOR}'
            ,onItemDisclosure : function(){}
            ,listeners        :
            {
                itemtap : _p1_itemtapAjuste
            }
        }
    });
    
    Ext.define('_p1_FormOrden',
    {
        extend  : 'Ext.form.Panel'
        ,config :
        {
            title : 'Detalle de Orden y Veh&iacute;culo'
        }
        ,initialize : function ()
        {
            debug('_p1_FormOrden initialize');
            this.callParent();
            this.setItems(
            [
                {
                    xtype  : 'fieldset'
                    ,items :
                    [
                        new  _p1_FieldNumeroInspeccion({readOnly:true})
                        ,new _p1_ComboTipoServicio({readOnly:true})
                        ,new _p1_ComboLugarInspeccion({readOnly:true})
                        ,new _p1_FieldPlaca({readOnly:true})
                        ,new _p1_FieldModelo({readOnly:true})
                        ,new _p1_FieldVersion({readOnly:true})
                        ,new _p1_ComboColor({readOnly:true})
                        ,new _p1_FieldAnio({readOnly:true})
                        ,new _p1_FieldSerialMotor({readOnly:true})
                        ,new _p1_FieldSerialCarroceria({readOnly:true})
                        ,new _p1_FieldObservaciones({readOnly:true})
                    ]
                }
                ,{
                    xtype   : 'toolbar'
                    ,docked : 'bottom'
                    ,layout :
                    {
                        pack : 'center'
                    }
                    ,items  :
                    {
                        xtype    : 'button'
                        ,text    : 'Continuar'
                        ,iconCls : 'arrow_right'
                        ,handler : _p1_continuar
                    }
                }
            ]);
        }
    });
    
    Ext.define('_p1_FormOrdenAjuste',
    {
        extend  : 'Ext.form.Panel'
        ,config :
        {
            title : 'Detalle de Orden y Veh&iacute;culo'
        }
        ,initialize : function ()
        {
            debug('_p1_FormOrdenAjuste initialize');
            this.callParent();
            this.setItems(
            [
                {
                    xtype  : 'fieldset'
                    ,items :
                    [
                        new  _p1_FieldNumeroInspeccion({readOnly:true})
                        ,new _p1_ComboLugarInspeccion({readOnly:true,name:'LUGARAJUS'})
                        ,new _p1_FieldPlaca({readOnly:true})
                        ,new _p1_FieldModelo({readOnly:true})
                        ,new _p1_FieldVersion({readOnly:true})
                        ,new _p1_ComboColor({readOnly:true})
                        ,new _p1_FieldAnio({readOnly:true})
                        ,new _p1_FieldSerialMotor({readOnly:true})
                        ,new _p1_FieldSerialCarroceria({readOnly:true})
                        ,new _p1_FieldObservaciones(
                        {
                        	label     : 'Declaraci&oacute;n del siniestro'
                        	,name     : 'DECLASINIEST'
                        	,readOnly : true
                        })
                        ,new _p1_FieldObservaciones(
                        {
                            label     : 'Descripci&oacute;n de da&ntilde;os'
                            ,name     : 'DESCRIPDANOS'
                            ,readOnly : true
                        })
                    ]
                }
                ,{
                    xtype   : 'toolbar'
                    ,docked : 'bottom'
                    ,layout :
                    {
                        pack : 'center'
                    }
                    ,items  :
                    {
                        xtype    : 'button'
                        ,text    : 'Continuar'
                        ,iconCls : 'arrow_right'
                        ,handler : _p1_continuarAjuste
                    }
                }
            ]);
        }
    });
    
    Ext.define('_p1_Tabbed',
    {
        extend      : 'Ext.tab.Panel'
        ,config     :
        {
            title           : 'Datos de la inspecci&oacute;n'
            ,tabBarPosition : 'bottom'
        }
        ,initialize : function()
        {
            debug('_p1_Tabbed initialize');
            this.callParent();
            /*this.setItems(
            [
                {
                    xtype  : 'fieldset'
                    ,title : 'Datos del veh&iacute;culo'
                    ,items :
                    [
                        new _p1_FieldMarca({readOnly:true})
                        ,new _p1_FieldModelo({readOnly:true})
                        ,new _p1_FieldPlaca({readOnly:true})
                    ]
                }
            ]);
            */
            this.setItems(
            [
                {
                    xtype    : 'formpanel'
                    ,title   : 'Informaci&oacute;n'
                    ,iconCls : 'info'
                    ,itemId  : 'inspTabInformacion'
                    ,items   :
                    {
                        xtype  : 'fieldset'
                        ,title : 'Informaci&oacute;n'
                        ,items :
                        [
                            new _p1_FieldMarca({readOnly:true})
                            ,new _p1_FieldModelo({readOnly:true})
                            ,new _p1_FieldPlaca({readOnly:true})
                        ]
                    }
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Veh&iacute;culo'
                    ,iconCls : 'favorites'
                    ,itemId  : 'inspTabVehiculo'
                    ,items   :
                    [
                        {
                            xtype   : 'fieldset'
                            ,title  : 'Datos del veh&iacute;culo'
                            ,items  :
                            [
                                new _p1_FieldNumeroInspeccion({hidden:true})
                                ,new _p1_FieldSerialMotor()
                                ,new _p1_FieldSerialCarroceria()
                                ,new _p1_FieldKilometraje()
                                ,new _p1_FieldCapacidadPuestos()
                                ,new _p1_ComboTipoTransmision()
                                ,new _p1_ComboMaterialTapiceria()
                                ,new _p1_ComboCabina()
                                ,new _p1_ComboDestinado()
                                ,new _p1_FieldCapacidadToneladas()
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDatosVehiculo
                        }
                    ]
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Seguridad'
                    ,iconCls : 'settings'
                    ,itemId  : 'inspTabSeguridad'
                    ,items   :
                    [
                        {
                            xtype  : 'fieldset'
                            ,title : 'Sistemas de seguridad'
                            ,items :
                            [
                                 new _p1_FieldNumeroInspeccion({hidden:true})
                                ,new Ext.form.Toggle({ label : 'Cortacorriente'        , name : 'CORTACORR'})
                                ,new Ext.form.Toggle({ label : 'Alarma'                , name : 'ALARMA'})
                                ,new Ext.form.Toggle({ label : 'B&oacute;veda'         , name : 'BOVEDA'})
                                ,new Ext.form.Toggle({ label : 'Grabado de vidrio'     , name : 'GRABAVIDRIO'})
                                ,new Ext.form.Toggle({ label : 'Tranca pedal'          , name : 'TRANCAPEDAL'})
                                ,new Ext.form.Toggle({ label : 'Tranca palanca'        , name : 'TRANCAPALAN'})
                                ,new Ext.form.Toggle({ label : 'Dispositivo satelital' , name : 'DISPSATELITAL'})
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDatosSeguridad
                        }
                    ]
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Accesorios'
                    ,iconCls : 'add'
                    ,itemId  : 'inspTabAccesorios'
                    ,items   :
                    [
                        {
                            xtype  : 'fieldset'
                            ,title : 'Accesorios'
                            ,items :
                            [
                                new _p1_FieldNumeroInspeccion({hidden : true})
                                ,new Ext.form.Toggle({ label : 'Radio fijo'         , name : 'RADIOFIJO'})
                                ,new Ext.form.Toggle({ label : 'Radio reproductor'  , name : 'RADIOREPRO' })
                                ,new Ext.form.Toggle({ label : 'Radio CD'           , name : 'RADIOCD'})
                                ,new Ext.form.Toggle({ label : 'Caucho de repuesto' , name : 'LLANTAREPUES'})
                                ,new Ext.form.Toggle({ label : 'Aire acondicionado' , name : 'AIREACONDIC'})
                                ,new Ext.form.Toggle({ label : 'Blindaje'           , name : 'BLINDAJE'})
                                ,new Ext.form.Toggle({ label : 'Estaca/Plataforma'  , name : 'ESTACAOPLATA'})
                                ,new _p1_ComboMaterialCava()
                                ,new _p1_ComboTipoRines()
                                ,new _p1_ComboTipoTazas()
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDetallesAccesorios
                        }
                    ]
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Evaluaci&oacute;n'
                    ,iconCls : 'compose'
                    ,items   :
                    [
                        {
                            xtype   : 'carousel'
                            ,height : 350
                            ,items  :
                            [
                                {
                                    html :
                                    [
                                    '<table align="center" style="height:212px;width:300px;margin-top:50px;background:url(\'${ctx}/resources/images/peritaje/audi/front300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi(this,5);return false;" class="_p1_front _p1_elem5">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,6);return false;" class="_p1_front _p1_elem6">&nbsp;</a></td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi(this,5);return false;" class="_p1_front _p1_elem5">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,6);return false;" class="_p1_front _p1_elem6">&nbsp;</a></td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,1);return false;" class="_p1_front _p1_elem1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,1);return false;" class="_p1_front _p1_elem1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,2);return false;" class="_p1_front _p1_elem2">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,2);return false;" class="_p1_front _p1_elem2">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,1);return false;" class="_p1_front _p1_elem1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,1);return false;" class="_p1_front _p1_elem1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,2);return false;" class="_p1_front _p1_elem2">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,2);return false;" class="_p1_front _p1_elem2">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,3);return false;" class="_p1_front _p1_elem3">&nbsp;</a></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,4);return false;" class="_p1_front _p1_elem4">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:156px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/izqA300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,7);return false;" class="_p1_front _p1_elem7">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,7);return false;" class="_p1_front _p1_elem7">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,7);return false;" class="_p1_front _p1_elem7">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,9);return false;" class="_p1_front _p1_elem9">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,9);return false;" class="_p1_front _p1_elem9">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,8);return false;" class="_p1_front _p1_elem8">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,9);return false;" class="_p1_front _p1_elem9">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,9);return false;" class="_p1_front _p1_elem9">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,10);return false;" class="_p1_front _p1_elem10">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:181px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/izqB300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,11);return false;" class="_p1_front _p1_elem11">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,11);return false;" class="_p1_front _p1_elem11">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,11);return false;" class="_p1_front _p1_elem11">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td></td><td>&nbsp;</td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td>&nbsp;</td><td></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,14);return false;" class="_p1_front _p1_elem14">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,12);return false;" class="_p1_front _p1_elem12">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td><td><a href="#" onclick="_p1_audi(this,13);return false;" class="_p1_front _p1_elem13">&nbsp;</a></td><td>&nbsp;</td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:212px;width:300px;margin-top:50px;background:url(\'${ctx}/resources/images/peritaje/audi/back300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,15);return false;" class="_p1_front _p1_elem15">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,15);return false;" class="_p1_front _p1_elem15">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,15);return false;" class="_p1_front _p1_elem15">&nbsp;</a></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,16);return false;" class="_p1_front _p1_elem16">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,16);return false;" class="_p1_front _p1_elem16">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,16);return false;" class="_p1_front _p1_elem16">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,17);return false;" class="_p1_front _p1_elem17">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:179px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/derA300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,18);return false;" class="_p1_front _p1_elem18">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,18);return false;" class="_p1_front _p1_elem18">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,18);return false;" class="_p1_front _p1_elem18">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,20);return false;" class="_p1_front _p1_elem20">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,21);return false;" class="_p1_front _p1_elem21">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,19);return false;" class="_p1_front _p1_elem19">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:157px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/derB300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,22);return false;" class="_p1_front _p1_elem22">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,22);return false;" class="_p1_front _p1_elem22">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,22);return false;" class="_p1_front _p1_elem22">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,23);return false;" class="_p1_front _p1_elem23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,25);return false;" class="_p1_front _p1_elem25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,25);return false;" class="_p1_front _p1_elem25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi(this,25);return false;" class="_p1_front _p1_elem25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,25);return false;" class="_p1_front _p1_elem25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,24);return false;" class="_p1_front _p1_elem24">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:124px;width:300px;margin-top:100px;background:url(\'${ctx}/resources/images/peritaje/audi/top300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,29);return false;" class="_p1_front _p1_elem29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,29);return false;" class="_p1_front _p1_elem29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,30);return false;" class="_p1_front _p1_elem30">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,30);return false;" class="_p1_front _p1_elem30">&nbsp;</a></td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,29);return false;" class="_p1_front _p1_elem29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,29);return false;" class="_p1_front _p1_elem29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,30);return false;" class="_p1_front _p1_elem30">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,30);return false;" class="_p1_front _p1_elem30">&nbsp;</a></td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,26);return false;" class="_p1_front _p1_elem26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,27);return false;" class="_p1_front _p1_elem27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,28);return false;" class="_p1_front _p1_elem28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,29);return false;" class="_p1_front _p1_elem29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,29);return false;" class="_p1_front _p1_elem29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,30);return false;" class="_p1_front _p1_elem30">&nbsp;</a></td><td><a href="#" onclick="_p1_audi(this,30);return false;" class="_p1_front _p1_elem30">&nbsp;</a></td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                            ]
                        }
                    ]
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Detalle'
                    ,iconCls : 'more'
                    ,itemId  : 'inspTabDetalleDanio'
                    ,items   :
                    [
                        {
                            xtype   : 'fieldset'
                            ,title  : 'Detalles de evaluaci&oacute;n'
                            ,itemId : 'fieldsetDetalleDanio'
                            ,items  :
                            [
                                new _p1_FieldNumeroInspeccion({hidden : true})
                                ,new _p1_ComboDanio({ name : 'otvalor01' , hidden : true , readOnly : true , label : 'Faro Del Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor02' , hidden : true , readOnly : true , label : 'Faro Del Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor03' , hidden : true , readOnly : true , label : 'Parilla'})
                                ,new _p1_ComboDanio({ name : 'otvalor04' , hidden : true , readOnly : true , label : 'Defensa Del'})
                                ,new _p1_ComboDanio({ name : 'otvalor05' , hidden : true , readOnly : true , label : 'Retrovisor Del'})
                                ,new _p1_ComboDanio({ name : 'otvalor06' , hidden : true , readOnly : true , label : 'Retrovisor Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor07' , hidden : true , readOnly : true , label : 'Puerta Del Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor08' , hidden : true , readOnly : true , label : 'Cristal Del Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor09' , hidden : true , readOnly : true , label : 'Llanta Del Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor10' , hidden : true , readOnly : true , label : 'Guardafangos Del Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor11' , hidden : true , readOnly : true , label : 'Cristal Tra Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor12' , hidden : true , readOnly : true , label : 'Puerta Tra Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor13' , hidden : true , readOnly : true , label : 'Llanta Tra Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor14' , hidden : true , readOnly : true , label : 'Guardafangos Tra Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor15' , hidden : true , readOnly : true , label : 'Faro Tra Izq'})
                                ,new _p1_ComboDanio({ name : 'otvalor16' , hidden : true , readOnly : true , label : 'Faro Tra Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor17' , hidden : true , readOnly : true , label : 'Defensa Tra'})
                                ,new _p1_ComboDanio({ name : 'otvalor18' , hidden : true , readOnly : true , label : 'Cristal Tra Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor19' , hidden : true , readOnly : true , label : 'Llanta Tra Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor20' , hidden : true , readOnly : true , label : 'Guardafangos Tra Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor21' , hidden : true , readOnly : true , label : 'Puerta Tra Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor22' , hidden : true , readOnly : true , label : 'Cristal Del Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor23' , hidden : true , readOnly : true , label : 'Puerta Del Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor24' , hidden : true , readOnly : true , label : 'Guardafangos Del Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor25' , hidden : true , readOnly : true , label : 'Llanta Del Der'})
                                ,new _p1_ComboDanio({ name : 'otvalor26' , hidden : true , readOnly : true , label : 'Cofre'})
                                ,new _p1_ComboDanio({ name : 'otvalor27' , hidden : true , readOnly : true , label : 'Cristal Frontal'})
                                ,new _p1_ComboDanio({ name : 'otvalor28' , hidden : true , readOnly : true , label : 'Toldo'})
                                ,new _p1_ComboDanio({ name : 'otvalor29' , hidden : true , readOnly : true , label : 'Cristal Trasero'})
                                ,new _p1_ComboDanio({ name : 'otvalor30' , hidden : true , readOnly : true , label : 'Cajuela'})
                                ,new _p1_FieldObservaciones()
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDetalleInspeccion
                        }
                    ]
                }
            ]);
        }
    });
    
    Ext.define('_p1_TabbedAjuste',
    {
        extend      : 'Ext.tab.Panel'
        ,config     :
        {
            title           : 'Datos del ajuste'
            ,tabBarPosition : 'bottom'
            ,listeners      :
            {
            	activeitemchange : function (tabpanel,value, oldValue)
            	{
            		debug('_p1_TabbedAjuste activate:',tabpanel.getActiveItem());
            		debug('value',value,'oldValue',oldValue);
            		debug('index',tabpanel.indexOf(value));
            		_p1_view.getNavigationBar().setTitle(tabpanel.getActiveItem().config.titulo);
            		if(tabpanel.indexOf(value)==6)
            		{
            			_p1_cargarPresupuesto();
            		}
            	}
            }
        }
        ,initialize : function()
        {
            debug('_p1_Tabbed initialize');
            this.callParent();
            this.setItems(
            [
                {
                    xtype    : 'formpanel'
                    ,title   : 'Informaci&oacute;n'
                    ,titulo  : 'Informaci&oacute;n'
                    ,iconCls : 'info'
                    ,itemId  : 'ajusTabInformacion'
                    ,items   :
                    {
                        xtype  : 'fieldset'
                        ,title : 'Informaci&oacute;n'
                        ,items :
                        [
                            new _p1_FieldNumeroSiniestro({readOnly : true})
                            ,new _p1_FieldNumeroPoliza({readOnly : true})
                            ,new _p1_FieldNumeroCertificado({readOnly : true})
                            ,new _p1_FieldAsegurado({readOnly:true})
                            ,new _p1_FieldTelefonoOficinaAsegurado({readOnly:true})
                            ,new _p1_FieldTelefonoCelularAsegurado({readOnly:true})
                            ,new _p1_FieldMarca({readOnly:true})
                            ,new _p1_FieldModelo({readOnly:true})
                            ,new _p1_FieldPlaca({readOnly:true})
                            ,new _p1_FieldAnio({readOnly:true})
                            ,new _p1_ComboTipoTransmision({readOnly:true})
                            ,new _p1_FieldKilometraje({readOnly:true})
                        ]
                    }
                }
                ,{
                	xtype             : 'list'
                	,grouped          : true
                	,indexBar         : true
                	,title            : 'Repuestos'
                	,titulo           : 'Repuestos disponibles'
                	,iconCls          : 'add'
                	,store            : _p1_storeRepuestosDisponibles
                	,itemTpl          : '{DESCRIPCION}'
                	,onItemDisclosure : _p1_agergarRepuestoDisponible
                }
                ,{
                	xtype             : 'list'
                    ,title            : 'Repuestos'
                    ,titulo           : 'Repuestos seleccionados'
                    ,iconCls          : 'more'
                    ,store            : _p1_storeRepuestosSeleccionados
                    ,itemTpl          : '{DESCRIPCION}'
                    ,onItemDisclosure : _p1_quitarRepuestoDisponible
                }/*
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Veh&iacute;culo'
                    ,iconCls : 'favorites'
                    ,itemId  : 'inspTabVehiculo'
                    ,items   :
                    [
                        {
                            xtype   : 'fieldset'
                            ,title  : 'Datos del veh&iacute;culo'
                            ,items  :
                            [
                                new _p1_FieldNumeroInspeccion({hidden:true})
                                ,new _p1_FieldSerialMotor()
                                ,new _p1_FieldSerialCarroceria()
                                ,new _p1_FieldKilometraje()
                                ,new _p1_FieldCapacidadPuestos()
                                ,new _p1_ComboTipoTransmision()
                                ,new _p1_ComboMaterialTapiceria()
                                ,new _p1_ComboCabina()
                                ,new _p1_ComboDestinado()
                                ,new _p1_FieldCapacidadToneladas()
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDatosVehiculo
                        }
                    ]
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Seguridad'
                    ,iconCls : 'settings'
                    ,itemId  : 'inspTabSeguridad'
                    ,items   :
                    [
                        {
                            xtype  : 'fieldset'
                            ,title : 'Sistemas de seguridad'
                            ,items :
                            [
                                 new _p1_FieldNumeroInspeccion({hidden:true})
                                ,new Ext.form.Toggle({ label : 'Cortacorriente'        , name : 'CORTACORR'})
                                ,new Ext.form.Toggle({ label : 'Alarma'                , name : 'ALARMA'})
                                ,new Ext.form.Toggle({ label : 'B&oacute;veda'         , name : 'BOVEDA'})
                                ,new Ext.form.Toggle({ label : 'Grabado de vidrio'     , name : 'GRABAVIDRIO'})
                                ,new Ext.form.Toggle({ label : 'Tranca pedal'          , name : 'TRANCAPEDAL'})
                                ,new Ext.form.Toggle({ label : 'Tranca palanca'        , name : 'TRANCAPALAN'})
                                ,new Ext.form.Toggle({ label : 'Dispositivo satelital' , name : 'DISPSATELITAL'})
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDatosSeguridad
                        }
                    ]
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Accesorios'
                    ,iconCls : 'add'
                    ,itemId  : 'inspTabAccesorios'
                    ,items   :
                    [
                        {
                            xtype  : 'fieldset'
                            ,title : 'Sistemas de seguridad'
                            ,items :
                            [
                                new _p1_FieldNumeroInspeccion({hidden : true})
                                ,new Ext.form.Toggle({ label : 'Radio fijo'         , name : 'RADIOFIJO'})
                                ,new Ext.form.Toggle({ label : 'Radio reproductor'  , name : 'RADIOREPRO' })
                                ,new Ext.form.Toggle({ label : 'Radio CD'           , name : 'RADIOCD'})
                                ,new Ext.form.Toggle({ label : 'Caucho de repuesto' , name : 'LLANTAREPUES'})
                                ,new Ext.form.Toggle({ label : 'Aire acondicionado' , name : 'AIREACONDIC'})
                                ,new Ext.form.Toggle({ label : 'Blindaje'           , name : 'BLINDAJE'})
                                ,new Ext.form.Toggle({ label : 'Estaca/Plataforma'  , name : 'ESTACAOPLATA'})
                                ,new _p1_ComboMaterialCava()
                                ,new _p1_ComboTipoRines()
                                ,new _p1_ComboTipoTazas()
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDetallesAccesorios
                        }
                    ]
                }*/
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Mano de obra'
                    ,titulo  : 'Mano de obra'
                    ,iconCls : 'compose'
                    ,items   :
                    [
                        {
                            xtype   : 'carousel'
                            ,height : 350
                            ,items  :
                            [
                                {
                                    html :
                                    [
                                    '<table align="center" style="height:212px;width:300px;margin-top:50px;background:url(\'${ctx}/resources/images/peritaje/audi/front300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi2(this,5);return false;" class="_p1_front _p1_elemajus5">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,6);return false;" class="_p1_front _p1_elemajus6">&nbsp;</a></td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi2(this,5);return false;" class="_p1_front _p1_elemajus5">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,6);return false;" class="_p1_front _p1_elemajus6">&nbsp;</a></td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,1);return false;" class="_p1_front _p1_elemajus1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,1);return false;" class="_p1_front _p1_elemajus1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,2);return false;" class="_p1_front _p1_elemajus2">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,2);return false;" class="_p1_front _p1_elemajus2">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,1);return false;" class="_p1_front _p1_elemajus1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,1);return false;" class="_p1_front _p1_elemajus1">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,2);return false;" class="_p1_front _p1_elemajus2">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,2);return false;" class="_p1_front _p1_elemajus2">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,3);return false;" class="_p1_front _p1_elemajus3">&nbsp;</a></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,4);return false;" class="_p1_front _p1_elemajus4">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:156px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/izqA300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,7);return false;" class="_p1_front _p1_elemajus7">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,7);return false;" class="_p1_front _p1_elemajus7">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,7);return false;" class="_p1_front _p1_elemajus7">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,9);return false;" class="_p1_front _p1_elemajus9">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,9);return false;" class="_p1_front _p1_elemajus9">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,8);return false;" class="_p1_front _p1_elemajus8">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,9);return false;" class="_p1_front _p1_elemajus9">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,9);return false;" class="_p1_front _p1_elemajus9">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,10);return false;" class="_p1_front _p1_elemajus10">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:181px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/izqB300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,11);return false;" class="_p1_front _p1_elemajus11">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,11);return false;" class="_p1_front _p1_elemajus11">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,11);return false;" class="_p1_front _p1_elemajus11">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td></td><td>&nbsp;</td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td>&nbsp;</td><td></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,14);return false;" class="_p1_front _p1_elemajus14">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,12);return false;" class="_p1_front _p1_elemajus12">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td><td><a href="#" onclick="_p1_audi2(this,13);return false;" class="_p1_front _p1_elemajus13">&nbsp;</a></td><td>&nbsp;</td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:212px;width:300px;margin-top:50px;background:url(\'${ctx}/resources/images/peritaje/audi/back300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,15);return false;" class="_p1_front _p1_elemajus15">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,15);return false;" class="_p1_front _p1_elemajus15">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,15);return false;" class="_p1_front _p1_elemajus15">&nbsp;</a></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,16);return false;" class="_p1_front _p1_elemajus16">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,16);return false;" class="_p1_front _p1_elemajus16">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,16);return false;" class="_p1_front _p1_elemajus16">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,17);return false;" class="_p1_front _p1_elemajus17">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:179px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/derA300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,18);return false;" class="_p1_front _p1_elemajus18">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,18);return false;" class="_p1_front _p1_elemajus18">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,18);return false;" class="_p1_front _p1_elemajus18">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,20);return false;" class="_p1_front _p1_elemajus20">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,21);return false;" class="_p1_front _p1_elemajus21">&nbsp;</a></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,19);return false;" class="_p1_front _p1_elemajus19">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:157px;width:300px;margin-top:75px;background:url(\'${ctx}/resources/images/peritaje/audi/derB300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,22);return false;" class="_p1_front _p1_elemajus22">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,22);return false;" class="_p1_front _p1_elemajus22">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,22);return false;" class="_p1_front _p1_elemajus22">&nbsp;</a></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,23);return false;" class="_p1_front _p1_elemajus23">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,25);return false;" class="_p1_front _p1_elemajus25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,25);return false;" class="_p1_front _p1_elemajus25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td></td><td><a href="#" onclick="_p1_audi2(this,25);return false;" class="_p1_front _p1_elemajus25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,25);return false;" class="_p1_front _p1_elemajus25">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,24);return false;" class="_p1_front _p1_elemajus24">&nbsp;</a></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                                ,{
                                    html :
                                    [
                                    '<table align="center" style="height:124px;width:300px;margin-top:100px;background:url(\'${ctx}/resources/images/peritaje/audi/top300.png\') no-repeat;" border="0">'
                                    ,'<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,29);return false;" class="_p1_front _p1_elemajus29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,29);return false;" class="_p1_front _p1_elemajus29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,30);return false;" class="_p1_front _p1_elemajus30">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,30);return false;" class="_p1_front _p1_elemajus30">&nbsp;</a></td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,29);return false;" class="_p1_front _p1_elemajus29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,29);return false;" class="_p1_front _p1_elemajus29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,30);return false;" class="_p1_front _p1_elemajus30">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,30);return false;" class="_p1_front _p1_elemajus30">&nbsp;</a></td></tr>'
                                    ,'<tr><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,26);return false;" class="_p1_front _p1_elemajus26">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,27);return false;" class="_p1_front _p1_elemajus27">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,28);return false;" class="_p1_front _p1_elemajus28">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,29);return false;" class="_p1_front _p1_elemajus29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,29);return false;" class="_p1_front _p1_elemajus29">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,30);return false;" class="_p1_front _p1_elemajus30">&nbsp;</a></td><td><a href="#" onclick="_p1_audi2(this,30);return false;" class="_p1_front _p1_elemajus30">&nbsp;</a></td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>&nbsp;</td></tr>'
                                    ,'</table>'
                                    ].join("")
                                    ,cls : 'card'
                                }
                            ]
                        }
                    ]
                }
                ,{
                    xtype             : 'list'
                    ,title            : 'Trabajos'
                    ,titulo           : 'Trabajos seleccionados'
                    ,iconCls          : 'more'
                    ,store            : _p1_storeTrabajosSeleccionados
                    ,itemTpl          : '{DSPIEZA} - {DESCRIPCION} : $ {MONTO}'
                    ,onItemDisclosure : _p1_quitarTrabajoSeleccionado
                }
                ,{
                    xtype    : 'formpanel'
                    ,title   : 'Detalle'
                    ,titulo  : 'Detalle'
                    ,iconCls : 'settings'
                    ,itemId  : 'ajusTabDetalleDanio'
                    ,items   :
                    [
                        {
                            xtype   : 'fieldset'
                            ,title  : 'Detalles de mano de obra'
                            ,itemId : 'fieldsetDetalleDanioAjus'
                            ,items  :
                            [
                                new _p1_FieldNumeroInspeccion({hidden : true})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor01' , hidden : true , readOnly : true , label : 'Faro Del Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor02' , hidden : true , readOnly : true , label : 'Faro Del Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor03' , hidden : true , readOnly : true , label : 'Parilla'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor04' , hidden : true , readOnly : true , label : 'Defensa Del'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor05' , hidden : true , readOnly : true , label : 'Retrovisor Del'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor06' , hidden : true , readOnly : true , label : 'Retrovisor Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor07' , hidden : true , readOnly : true , label : 'Puerta Del Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor08' , hidden : true , readOnly : true , label : 'Cristal Del Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor09' , hidden : true , readOnly : true , label : 'Llanta Del Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor10' , hidden : true , readOnly : true , label : 'Guardafangos Del Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor11' , hidden : true , readOnly : true , label : 'Cristal Tra Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor12' , hidden : true , readOnly : true , label : 'Puerta Tra Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor13' , hidden : true , readOnly : true , label : 'Llanta Tra Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor14' , hidden : true , readOnly : true , label : 'Guardafangos Tra Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor15' , hidden : true , readOnly : true , label : 'Faro Tra Izq'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor16' , hidden : true , readOnly : true , label : 'Faro Tra Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor17' , hidden : true , readOnly : true , label : 'Defensa Tra'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor18' , hidden : true , readOnly : true , label : 'Cristal Tra Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor19' , hidden : true , readOnly : true , label : 'Llanta Tra Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor20' , hidden : true , readOnly : true , label : 'Guardafangos Tra Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor21' , hidden : true , readOnly : true , label : 'Puerta Tra Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor22' , hidden : true , readOnly : true , label : 'Cristal Del Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor23' , hidden : true , readOnly : true , label : 'Puerta Del Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor24' , hidden : true , readOnly : true , label : 'Guardafangos Del Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor25' , hidden : true , readOnly : true , label : 'Llanta Del Der'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor26' , hidden : true , readOnly : true , label : 'Cofre'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor27' , hidden : true , readOnly : true , label : 'Cristal Frontal'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor28' , hidden : true , readOnly : true , label : 'Toldo'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor29' , hidden : true , readOnly : true , label : 'Cristal Trasero'})
                                ,new _p1_ComboDanioAgrupado({ name : 'otvalor30' , hidden : true , readOnly : true , label : 'Cajuela'})
                                ,new _p1_FieldImporte({ name : 'LATONERI' , readOnly : true , label : 'Latoner&iacute;a y pintura'})
                                ,new _p1_FieldImporte({ name : 'MOMECANI' , readOnly : true , label : 'M.O. Mec&aacute;nica'})
                                ,new _p1_FieldImporte({ name : 'CROMADO'  , readOnly : true , label : 'Cromado'})
                                ,new _p1_FieldImporte({ name : 'ALINEACI' , readOnly : true , label : 'Alineaci&oacute;n y balanceo'})
                                ,new _p1_FieldImporte({ name : 'MOELECTR' , readOnly : true , label : 'M.O. Electricidad'})
                                ,new _p1_FieldImporte({ name : 'REPUESTO' , readOnly : true , label : 'Repuestos'})
                                ,new _p1_FieldImporte({ name : 'TAPICERI' , readOnly : true , label : 'Tapicer&iacute;a'})
                                ,new _p1_FieldImporte({ name : 'AACONDIC' , readOnly : true , label : 'Aire acondicionado'})
                                ,new _p1_FieldImporte({ name : 'SUBTOTAL' , readOnly : true , label : 'SUBTOTAL'})
                                ,new _p1_FieldImporte({ name : 'IVA'      , readOnly : true , label : 'IVA'})
                                ,new _p1_FieldImporte({ name : 'TOTAL'    , readOnly : true , label : 'TOTAL'})
                                ,new Ext.form.Toggle({label : 'Generar orden inmediata',name : 'micheck'})
                                ,new _p1_FieldObservaciones()
                            ]
                        }
                        ,{
                            xtype    : 'button'
                            ,text    : 'Guardar'
                            ,ui      : 'confirm'
                            ,handler : _p1_guardarDetalleAjuste
                        }
                    ]
                }
            ]);
        }
    });
    ////// componentes //////

    ////// contenido //////
    _p1_formManoObra = Ext.Viewport.add(
   	{
        xtype       : 'formpanel'
        ,modal      : true
        ,centered   : true
        ,scrollable : true
        ,closable   : true
        ,width      : 300
        ,height     : 300
        ,items      :
        [
            {
            	xtype   : 'fieldset'
            	,items  :
            	[
            	    new _p1_ComboDanioAgrupado({name : 'danio'})
            	    ,new _p1_ComboTrabajoDisponible(
            	    {
            	    	name       : 'trabajo'
            	    	,listeners :
            	    	{
            	    		change : function(field,newval,oldval)
            	    		{
            	    			debug('newval',field.getRecord());
            	    			field.up().items.items[2].setValue(field.getRecord().get('MONTO'));
            	    		}
            	    	}
            	    })
            	    ,new _p1_FieldImporte({name : 'importe'})
            	]
            }
            ,{
            	xtype    : 'button'
            	,ui      : 'confirm'
            	,text    : 'Agregar'
            	,handler : _p1_audi2_change
            }
            ,{
                xtype    : 'button'
                ,ui      : 'decline'
                ,text    : 'Cancelar'
                ,handler : function(){_p1_formManoObra.hide();}
            }
        ]
    });
    _p1_formManoObra.hide();
    
    _p1_danioPicker = Ext.create('Ext.Picker',
    {
        slots:
        [
            {
                name  : 'nombre'
                ,data :
                [   
                    { value  : '0'   , text : 'Ninguno' }
                    ,{ value : '10'  , text : '10%' }
                    ,{ value : '20'  , text : '20%' }
                    ,{ value : '30'  , text : '30%' }
                    ,{ value : '40'  , text : '40%' }
                    ,{ value : '50'  , text : '50%' }
                    ,{ value : '60'  , text : '60%' }
                    ,{ value : '70'  , text : '70%' }
                    ,{ value : '80'  , text : '80%' }
                    ,{ value : '90'  , text : '90%' }
                    ,{ value : '100' , text : 'Total' }
                ]
            }
        ]
        ,listeners :
        {
            change : _p1_audi_change
        }
    });
    
    _p1_view = Ext.create('Ext.NavigationView',
    {
        defaultBackButtonText : 'Atr&aacute;s'
        ,items                :
        [
           Ext.create('Ext.Container',
           {
               title  : 'e-Peritaje'
               ,items :
               [
                   {
                       xtype   : 'toolbar'
                       ,docked : 'bottom'
                       ,layout :
                       {
                           pack : 'center'
                       }
                       ,items  :
                       [
                           {
                               xtype    : 'button'
                               ,text    : '&Oacute;rdenes de inspecci&oacute;n'
                               ,handler : _p1_mostrarGridInspeccion
                           }
                           ,{
                               xtype    : 'button'
                               ,text    : '&Oacute;rdenes de ajuste'
                               ,handler : _p1_mostrarGridAjuste
                               ,hidden  : false
                           }
                       ]
                   }
                   ,{
                       xtype : 'panel'
                       ,html :
                       [
                           '<table align="center" border="0"><tr><td align="center">',
                           '<img style="maxWidth:65%;maxHeight:65%;" src="${ctx}/resources/images/peritaje/empresa/previsoraLogo.png" / >',
                           '<p style="font-weight:bold;font-size:1.2em;">Bienvenido a e-Peritaje</p>',
                           '<p style="margin-top:5px;margin-bottom:10px;">En las pesta&ntilde;as inferiores puede acceder ',
                           'a las &oacute;rdenes pendientes.</p>',
                           '<p style="color:gray;font-size:0.8em;">e-Peritaje 2014</p></td></tr></table>'
                       ].join("")
                   }
               ]
           })
        ]
    });
    
    Ext.Viewport.add(_p1_view);
    ////// contenido //////

    ////// loader //////
    ////// loader //////
}});

////// funciones //////
function _p1_mostrarGridInspeccion()
{
    _p1_gridOrdenesInspeccion = new _p1_GridOrdenesInspeccion();
    setLoading(true);
    _p1_storeOrdenesInspeccion.load(
    {
        callback : function (records,operation,success)
        {
            setLoading(false);
            if(!success)
            {
                mensajeError('Error al obtener &oacute;rdenes');
            }
        }
    });
    _p1_view.push(_p1_gridOrdenesInspeccion);
}

function _p1_itemtap(dataview,index,target,record)
{
    debug('itemtap on:',record);
    _p1_formOrden = new _p1_FormOrden();
    _p1_view.push(_p1_formOrden);
    setLoading(true);
    Ext.Ajax.request(
    {
    	url     : _p1_urlObtenerDetalleOrdenInspeccion
    	,params :
    	{
    		'strMapIn.nmorden' : record.get('NMORDEN')
    	}
        ,success : function(response)
        {
        	setLoading(false);
        	var json = Ext.decode(response.responseText);
        	debug('respuesta:',json);
        	if(json.success)
        	{
        		_p1_inspRecordSelected = new _p1_OrdenInspeccion(json.strMapOut);
        		_p1_formOrden.setRecord(_p1_inspRecordSelected);
        	}
        	else
        	{
        		mensajeError(json.mensaje);
        	}
        }
        ,failure : errorComunicacion
    });
}

function _p1_continuar()
{
    debug('continuar');
    _p1_tabbed = new _p1_Tabbed();
    _p1_view.push(_p1_tabbed);
    Ext.ComponentQuery.query('#inspTabInformacion')[0].setRecord(_p1_inspRecordSelected);
    Ext.ComponentQuery.query('#inspTabDetalleDanio')[0].setRecord(_p1_inspRecordSelected);
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p1_urlObtenerDatosVehiculo
        ,params :
        {
            'strMapIn.nmorden' : _p1_inspRecordSelected.get('NMORDEN')
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
            	var form = Ext.ComponentQuery.query('#inspTabVehiculo')[0];
            	debug('form vehiculo:',form);
            	debug('record vehiculo:',new _p1_OrdenInspeccion(json.strMapOut));
            	form.setRecord(new _p1_OrdenInspeccion(json.strMapOut));
            	debug('form.values:',form.getValues());
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
    
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p1_urlObtenerDatosSeguridad
        ,params :
        {
            'strMapIn.nmorden' : _p1_inspRecordSelected.get('NMORDEN')
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                var form = Ext.ComponentQuery.query('#inspTabSeguridad')[0];
                debug('form seguridad:',form);
                debug('record seguridad:',new _p1_OrdenInspeccion(json.strMapOut));
                form.setRecord(new _p1_OrdenInspeccion(json.strMapOut));
                debug('form.values:',form.getValues());
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
    
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p1_urlObtenerDetalleAccesorios
        ,params :
        {
            'strMapIn.nmorden' : _p1_inspRecordSelected.get('NMORDEN')
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                var form = Ext.ComponentQuery.query('#inspTabAccesorios')[0];
                debug('form accesorios:',form);
                debug('record accesorios:',new _p1_OrdenInspeccion(json.strMapOut));
                form.setRecord(new _p1_OrdenInspeccion(json.strMapOut));
                debug('form.values:',form.getValues());
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
}

function _p1_guardarDatosVehiculo()
{
	debug('_p1_guardarDatosVehiculo');
	var form = Ext.ComponentQuery.query('#inspTabVehiculo')[0];
	//////
	setLoading(true);
	Ext.Ajax.request(
    {
        url       : _p1_urlGuardarDatosVehiculo
        ,jsonData :
        {
            strMapIn : form.getValues()
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
            	mensajeCorrecto(json.mensaje);
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
	//////
}

function _p1_guardarDatosSeguridad()
{
    debug('_p1_guardarDatosSeguridad');
    var form = Ext.ComponentQuery.query('#inspTabSeguridad')[0];
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url       : _p1_urlGuardarDatosSeguridad
        ,jsonData :
        {
            strMapIn : form.getValues()
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                mensajeCorrecto(json.mensaje);
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
}

function _p1_guardarDetallesAccesorios()
{
    debug('_p1_guardarDetallesAccesorios');
    var form = Ext.ComponentQuery.query('#inspTabAccesorios')[0];
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url       : _p1_urlGuardarDetalleAccesorios
        ,jsonData :
        {
            strMapIn : form.getValues()
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                mensajeCorrecto(json.mensaje);
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
}

function _p1_guardarDetalleInspeccion()
{
	debug('_p1_guardarDetalleInspeccion');
    var form = Ext.ComponentQuery.query('#inspTabDetalleDanio')[0];
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url       : _p1_urlGuardarDetalleInspeccion
        ,jsonData :
        {
            strMapIn : form.getValues()
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                mensajeCorrecto('En unos segundos iniciar&aacute; la descarga del reporte generado, por favor verifique sus descargas');
            	window.open(
            	    '${ctx}/peritaje/descargaDocumento.action?strMapIn.ruta='+encodeURIComponent(json.mensaje)+'&strMapIn.filename=inspeccion.pdf'
            	    ,'_blank'
            	    ,'width=800, height=600'
            	);
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
}

function _p1_audi(ahref,elem)
{
    debug('_p1_audi:',ahref,elem);
    _p1_piezaSelected = elem;
    debug(Ext.ComponentQuery.query('#fieldsetDetalleDanio'));
    var formValue = Ext.ComponentQuery.query('#fieldsetDetalleDanio')[0].items.items[elem].getValue();
    debug('formValue:',formValue);
    _p1_danioPicker.setValue({nombre:formValue});
    _p1_danioPicker.show();
}

function _p1_audi_change(picker , value)
{
    debug('picker,value:',picker,value);
    value = value.nombre;
    debug('picker value:',value);
    
    Ext.ComponentQuery.query('#fieldsetDetalleDanio')[0].items.items[_p1_piezaSelected].setValue(value);
    
    var elems=Ext.query('a._p1_elem'+_p1_piezaSelected);
    debug('elems',elems);
    for(var i=0;i<elems.length;i++)
    {
        Ext.get(elems[i]).removeCls('_p1_front_edited1');
        Ext.get(elems[i]).removeCls('_p1_front_edited2');
        Ext.get(elems[i]).removeCls('_p1_front_edited3');
        Ext.get(elems[i]).removeCls('_p1_front_edited4');
    }
    if(value*1>0 && value*1<=30)
    {
        for(var i=0;i<elems.length;i++)
        {
            Ext.get(elems[i]).addCls('_p1_front_edited1');
        }
    }
    else if(value*1>30 && value*1<=60)
    {
        for(var i=0;i<elems.length;i++)
        {
            Ext.get(elems[i]).addCls('_p1_front_edited2');
        }
    }
    else if(value*1>60 && value*1<100)
    {
        for(var i=0;i<elems.length;i++)
        {
            Ext.get(elems[i]).addCls('_p1_front_edited3');
        }
    }
    else if(value*1==100)
    {
        for(var i=0;i<elems.length;i++)
        {
            Ext.get(elems[i]).addCls('_p1_front_edited4');
        }
    }
}

function _p1_mostrarGridAjuste()
{
    debug('_p1_mostrarGridAjuste');
    _p1_gridOrdenesAjuste = new _p1_GridOrdenesAjuste();
    setLoading(true);
    _p1_storeOrdenesAjuste.load(
    {  
        callback : function (records,operation,success)
        {
            setLoading(false);
            if(!success)
            {
                mensajeError('Error al obtener &oacute;rdenes');
            }
        }
    });
    _p1_view.push(_p1_gridOrdenesAjuste);
}

function _p1_itemtapAjuste(dataview,index,target,record)
{
	debug('_p1_itemtapAjuste on:',record);
    _p1_formOrdenAjuste = new _p1_FormOrdenAjuste();
    _p1_view.push(_p1_formOrdenAjuste);
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p1_urlObtenerDetalleOrdenAjuste
        ,params :
        {
            'strMapIn.nmorden' : record.get('NMORDEN')
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                _p1_inspRecordSelected = new _p1_OrdenInspeccion(json.strMapOut);
                _p1_formOrdenAjuste.setRecord(_p1_inspRecordSelected);
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
}

function _p1_continuarAjuste()
{
    debug('continuar');
    _p1_storeRepuestosSeleccionados.removeAll();
    _p1_storeTrabajosSeleccionados.removeAll();
    
    _p1_tabbedAjuste = new _p1_TabbedAjuste();
    _p1_view.push(_p1_tabbedAjuste);
    
    //////
    setLoading(true);
    _p1_storeRepuestosDisponibles.load(
    {
        callback : function (records,operation,success)
        {
            setLoading(false);
            if(!success)
            {
                mensajeError('Error al obtener &oacute;rdenes');
            }
        }
    });
    //////
    
    //////
    setLoading(true);
    Ext.Ajax.request(
    {
        url     : _p1_urlObtenerDetalleAjuste
        ,params :
        {
            'strMapIn.nmorden' : _p1_inspRecordSelected.get('NMORDEN')
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                var form = Ext.ComponentQuery.query('#ajusTabInformacion')[0];
                debug('form detalles ajuste:',form);
                debug('record detalles ajuste:',new _p1_OrdenInspeccion(json.strMapOut));
                form.setRecord(new _p1_OrdenInspeccion(json.strMapOut));
                debug('form.values:',form.getValues());
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
    
    //////
    setLoading(true);
    _p1_storeTrabajosDisponibles.load(
    {
        callback : function (records,operation,success)
        {
            setLoading(false);
            if(!success)
            {
                mensajeError('Error al obtener trabajos disponibles');
            }
        }
    });
    //////
}

function _p1_agergarRepuestoDisponible(record)
{
	debug('_p1_agergarRepuestoDisponible',record);
	_p1_storeRepuestosSeleccionados.add(record);
	_p1_storeRepuestosDisponibles.remove(record);
	_p1_tabbedAjuste.setActiveItem(2);
}

function _p1_quitarRepuestoDisponible(record)
{
    debug('_p1_quitarRepuestoDisponible',record);
    _p1_storeRepuestosDisponibles.add(record);
    _p1_storeRepuestosSeleccionados.remove(record);
    _p1_tabbedAjuste.setActiveItem(1);
}

function _p1_audi2(ahref,elem)
{
    debug('_p1_audi2:',ahref,elem);
    _p1_piezaSelected = elem;
    _p1_formManoObra.reset();
    _p1_formManoObra.show();
    debug(Ext.ComponentQuery.query('#fieldsetDetalleDanioAjus'));
    var formValue = Ext.ComponentQuery.query('#fieldsetDetalleDanioAjus')[0].items.items[elem].getValue();
    debug('formValue:',formValue);
    _p1_formManoObra.items.items[0].items.items[0].setValue(formValue);
}

function _p1_audi2_change()
{
    debug('_p1_audi2_change');
    var values = _p1_formManoObra.getValues();
    var value = values['danio'];
    var recordTrabajo = _p1_formManoObra.items.items[0].items.items[1].getRecord();
    debug('values:',values,'recordTrabajo:',recordTrabajo);
    
    Ext.ComponentQuery.query('#fieldsetDetalleDanioAjus')[0].items.items[_p1_piezaSelected].setValue(value);
    
    var recordTrabajoSeleccionado = new _p1_TrabajoSeleccionado(
    {
        NMORDEN      : _p1_inspRecordSelected.get('NMORDEN')
        ,NMPIEZA     : _p1_piezaSelected
        ,DSPIEZA     : _p1_nombrePiezas[_p1_piezaSelected]
        ,CODIGO      : recordTrabajo.get('CODIGO')
        ,DESCRIPCION : recordTrabajo.get('DESCRIPCION')
        ,MONTO       : values['importe']
    });
    debug('recordTrabajoSeleccionado:',recordTrabajoSeleccionado);
    _p1_storeTrabajosSeleccionados.add(recordTrabajoSeleccionado);
    
    var elems=Ext.query('a._p1_elemajus'+_p1_piezaSelected);
    debug('elems',elems);
    for(var i=0;i<elems.length;i++)
    {
        Ext.get(elems[i]).removeCls('_p1_front_edited1');
        if(value!='NINGUNO')
        {
            Ext.get(elems[i]).addCls('_p1_front_edited1');
        }
    }
    
    _p1_formManoObra.hide();
    _p1_tabbedAjuste.setActiveItem(4);
}

function _p1_quitarTrabajoSeleccionado(record)
{
	debug('_p1_quitarTrabajoSeleccionado');
	_p1_storeTrabajosSeleccionados.remove(record);
	_p1_tabbedAjuste.setActiveItem(3);
}

function _p1_guardarDetalleAjuste()
{
	debug('_p1_guardarDetalleAjuste');
    //////
    setLoading(true);
    var form = Ext.ComponentQuery.query('#ajusTabDetalleDanio')[0];
    var json3 =
    {
        strMapIn : form.getValues()
    };
    json3.strMapIn['ajuste']='si';
    Ext.Ajax.request(
    {
        url       : _p1_urlGuardarDetalleInspeccion
        ,jsonData : json3  
        ,success  : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
            	if(Ext.ComponentQuery.query('#fieldsetDetalleDanioAjus')[0].down('[name="micheck"]').getValue()==1)
                {
            		mensajeCorrecto('En unos segundos iniciar&aacute; la descarga del reporte generado, por favor verifique sus descargas');
	                window.open(
                        '${ctx}/peritaje/descargaDocumento.action?strMapIn.ruta='+encodeURIComponent(json.mensaje)+'&strMapIn.filename=inspeccion.pdf'
                        ,'_blank'
                        ,'width=800, height=600'
                    );
                }
            	else
            	{
            		mensajeCorrecto('Datos guardados');
            	}
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure  : errorComunicacion
    });
	//////
}

function _p1_cargarPresupuesto()
{
	debug('_p1_cargarPresupuesto');
	//////
	setLoading(true);
    setLoading(true);
    var smap =
    {
        nmorden : _p1_inspRecordSelected.get('NMORDEN')
    };
    var slist = [];
    _p1_storeRepuestosSeleccionados.each(function(record)
    {
        slist.push(
        {
            otvalor : record.get('CODIGO')
            ,monto  : record.get('MONTO')
        });
    });
    var json =
    {
        strMapIn      : smap
        ,strListMapIn : slist
    };
    debug('datos a enviar:',json);
    Ext.Ajax.request(
    {
        url       : _p1_urlGuardarRepuestosAjuste
        ,jsonData : json
        ,success  : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
                if(_global_overlay_count==0)
                {
                	_p1_cargarPresupuestoRecord();
                }
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
    var slist2 = [];
    _p1_storeTrabajosSeleccionados.each(function(record)
    {
        slist2.push(
        {
            otvalor : record.get('CODIGO')
            ,monto  : record.get('MONTO')
        });
    });
    var json2 =
    {
        strMapIn      : smap
        ,strListMapIn : slist2
    };
    debug('datos a enviar:',json2);
    Ext.Ajax.request(
    {
        url       : _p1_urlGuardarManoObraAjuste
        ,jsonData : json2
        ,success  : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
            	if(_global_overlay_count==0)
                {
                    _p1_cargarPresupuestoRecord();
                }
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
    //////
}

function _p1_cargarPresupuestoRecord()
{
	debug('_p1_cargarPresupuestoRecord');
	//////
	setLoading(true);
	Ext.Ajax.request(
    {
        url      : _p1_urlObtenerDatosPresupuesto
        ,params  :
        {
        	'strMapIn.nmorden' : _p1_inspRecordSelected.get('NMORDEN')
        }
        ,success : function(response)
        {
            setLoading(false);
            var json = Ext.decode(response.responseText);
            debug('respuesta:',json);
            if(json.success)
            {
            	var objeto   = json.strMapOut;
            	var fieldset =  Ext.ComponentQuery.query('#fieldsetDetalleDanioAjus')[0];
            	fieldset.items.items[0].setValue(objeto.NMORDEN);
            	fieldset.down('[name="LATONERI"]').setValue(objeto.LATONERI);
            	fieldset.down('[name="MOMECANI"]').setValue(objeto.MOMECANI);
            	fieldset.down('[name="CROMADO"]') .setValue(objeto.CROMADO);
            	fieldset.down('[name="ALINEACI"]').setValue(objeto.ALINEACI);
            	fieldset.down('[name="MOELECTR"]').setValue(objeto.MOELECTR);
            	fieldset.down('[name="REPUESTO"]').setValue(objeto.REPUESTO);
            	fieldset.down('[name="TAPICERI"]').setValue(objeto.TAPICERI);
            	fieldset.down('[name="AACONDIC"]').setValue(objeto.AACONDIC);
            	fieldset.down('[name="SUBTOTAL"]').setValue(objeto.SUBTOTAL);
            	fieldset.down('[name="IVA"]')     .setValue(objeto.IVA);
            	fieldset.down('[name="TOTAL"]')   .setValue(objeto.TOTAL);
            }
            else
            {
                mensajeError(json.mensaje);
            }
        }
        ,failure : errorComunicacion
    });
	//////
}
////// funciones //////
</script>
</head>
<body>
</body>
</html>