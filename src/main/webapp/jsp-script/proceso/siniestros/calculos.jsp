<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="/taglibs.jsp"%>
<script>

////// variables //////

var _p12_smap    = <s:property value='smapJson'                    escapeHtml='false' />;
var _p12_smap2   = <s:property value='smap2Json'                   escapeHtml='false' />;
var _p12_smap3   = <s:property value='smap3Json'                   escapeHtml='false' />;
var _p12_slist1  = <s:property value='facturasxSiniestroJson'      escapeHtml='false' />;//facturas(REEMBOLSO)/siniestros(P DIRECTO)
var _p12_slist2  = <s:property value='slist2Json'                  escapeHtml='false' />;//copago/deducible (TODOS)
var _p12_slist3  = <s:property value='slist3Json'                  escapeHtml='false' />;//proveedores (REEMBOLSO)
var _p12_llist1  = <s:property value='llist1Json'                  escapeHtml='false' />;//lista de lista de conceptos (TODOS)
var _p12_lhosp   = <s:property value='lhospJson'                   escapeHtml='false' />;//lista de totales hopitalizacion
var _p12_lpdir   = <s:property value='lpdirJson'                   escapeHtml='false' />;//lista de datos pdirecto
var _p12_lprem   = <s:property value='lpremJson'                   escapeHtml='false' />;//lista de datos preembolso
var _p12_listaWS = <s:property value='listaImportesWebServiceJson' escapeHtml='false' />;

var _p12_penalTotal = <s:property value='datosPenalizacionJson' escapeHtml='false' />; //Informacion de penalizacion
var _p12_coberturaxcal = <s:property value='datosCoberturaxCalJson' escapeHtml='false' />; //Informacion de penalizacion

var _CAUSA_ACCIDENTE = '<s:property value="@mx.com.gseguros.portal.general.util.CausaSiniestro@ACCIDENTE.codigo"/>';
var _TIPO_PAGO_DIRECTO     = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo"/>';
var _TIPO_PAGO_REEMBOLSO   = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo"/>';
debug('_p12_coberturaxcal:'    , _p12_coberturaxcal);
debug('penalizacion:'    , _p12_penalTotal);
debug('_p12_smap:'    , _p12_smap);
debug('_p12_smap2:'   , _p12_smap2);
debug('_p12_smap3:'   , _p12_smap3);
debug('_p12_slist1 FacturasxSiniestroJson:'  , _p12_slist1);
debug('_p12_slist2:'  , _p12_slist2);
debug('_p12_slist3:'  , _p12_slist3);
debug('_p12_llist1:'  , _p12_llist1);
debug('_p12_lhosp:'   , _p12_lhosp);
debug('_p12_lpdir:'   , _p12_lpdir);
debug('_p12_lprem:'   , _p12_lprem);
debug('_p12_listaWS:' , _p12_listaWS);

var _p12_formTramite;
var _p12_formFactura;
var _p12_formProveedor;
var _p12_formSiniestro;
var _p12_panelCalculo;
var _p12_paneles = [];
var _p12_formAutoriza;
var _p12_windowAutoriza;
var _p12_itemsRechazo = [ <s:property value="imap.rechazoitems" /> ];
    _p12_itemsRechazo[2]['width']  = 500;
    _p12_itemsRechazo[2]['height'] = 150;
var _p12_formRechazo;
var _p12_windowRechazo;

////// variables //////

Ext.onReady(function()
{
	////// modelos //////
	Ext.define('_p12_Tramite',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="imap.tramiteFields" /> ]
	});
	Ext.define('_p12_Factura',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="imap.facturaFields" /> ]
	});
	Ext.define('_p12_Siniestro',
    {
        extend  : 'Ext.data.Model'
        ,fields : [ <s:property value="imap.siniestroFields" /> ]
    });
	Ext.define('_p12_Proveedor',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="imap.proveedorFields" /> ]
	});
	Ext.define('_p12_Concepto',
	{
		extend  : 'Ext.data.Model'
		,fields : [ <s:property value="imap.conceptoFields" /> ]
	});
	////// modelos //////

	////// componentes //////
	var totalglobal = 0.0;
	var totalIVA		 = 0.0;
	var totalISR 		 = 0.0;
	var totalIVARet 	 = 0.0;
	var totalImpCedular  = 0.0;
	//PAGO DIRECTO
    if(_p12_smap.OTVALOR02== _TIPO_PAGO_DIRECTO)
    {
		debug('PAGO DIRECTO');
		var indice;
		//RECORREMOS LAS FACTURAS
		for(indice = 0;indice<_p12_slist1.length;indice++)
		{
			debug('Iterando siniestros n '+(indice+1));
			debug("_p12_slist1.siniestroPD");
			//va el panel de Asegurados
			var indiceSiniestro;
			var indiceConcepto ;
			var panelSiniestro;

			var panelFacturasPD = Ext.create('Ext.form.Panel',
			{
				title		: 'FACTURA',
				layout		:
				{
					type     : 'table'
					,columns : 2
				}
				,border   : 0
				,defaults :
				{
					style : 'margin : 5px;'
				}
				,items    :
                        [
                            {
                            	xtype       : 'displayfield'
                            	,fieldLabel : 'N&Uacute;MERO DE FACTURA'
                            	,name       : 'NFACTURA'
                            },
                            {
                            	xtype       : 'displayfield'
                            	,fieldLabel : 'FECHA'
                            	,name       : 'FFACTURA'
                            	,valueToRaw : function(value){
									return Ext.Date.format(value,'d M Y');
								}
                            },
                            {
                            	xtype       : 'displayfield'
                            	,fieldLabel : 'COBERTURA'
                            	,name       : 'DSGARANT'
                            },
                            {
                            	xtype       : 'displayfield'
                            	,fieldLabel : 'DESCUENTO %'
                            	,name       : 'DESCPORC'
                            },
                            {
                            	xtype       : 'displayfield'
                            	,fieldLabel : 'DESCUENTO $'
                            	,name       : 'DESCNUME'
                            },
                            {
                            	xtype       : 'displayfield'
                            	,fieldLabel : 'SERVICIO'
                            	,name       : 'DESCSERVICIO'
                            }
                        ]
                    });
                    
                    panelFacturasPD.loadRecord(new _p12_Factura(_p12_slist1[indice]));
                    _p12_paneles.push(panelFacturasPD);
                    
            for(indiceSiniestro = 0;indiceSiniestro<_p12_slist1[indice].siniestroPD.length ;indiceSiniestro++)
            {
            	debug("VALOR DE _p12_slist1[indice].siniestroPD[indiceSiniestro].conceptosAsegurado");
            	debug(_p12_slist1[indice].siniestroPD[indiceSiniestro].conceptosAsegurado);
                debug('Iterando siniestros n '+(indiceSiniestro+1));
                var panelSiniestro = Ext.create('Ext.form.Panel',
                {
                	layout    :
                    {
                    	type     : 'table'
                    	,columns : 2
                    }
                    ,border   : 0
    	            ,defaults :
    	            {
    	                style : 'margin : 5px;'
    	            }
                    ,items    :
                    [
                        {
                        	xtype       : 'displayfield'
                        	,fieldLabel : 'N&Uacute;MERO DE SINIESTRO'
                        	,name       : 'NMSINIES'
                        }
                        ,{
                        	xtype       : 'displayfield'
                        	,fieldLabel : 'SUCURSAL DE LA P&Oacute;LIZA'
                        	,name       : 'CDUNIECO'
                        }
                        ,{
                            xtype       : 'displayfield'
                            ,fieldLabel : 'P&Oacute;LIZA'
                            ,name       : 'NMPOLIZA'
                        }
                        ,{
                            xtype       : 'displayfield'
                            ,fieldLabel : 'CLAVE ASEGURADO'
                            ,name       : 'CDPERSON'
                        }
                        ,{
                            xtype       : 'displayfield'
                            ,fieldLabel : 'ASEGURADO'
                            ,name       : 'NOMBRE'
                        }
                        ,{
                            xtype       : 'displayfield'
                            ,fieldLabel : 'FECHA DE OCURRENCIA'
                            ,name       : 'FEOCURRE'
                            ,valueToRaw : function(value)
                            {
                            	return Ext.Date.format(value,'d M Y');
                            }
                        }
                    ]
                });
                panelSiniestro.loadRecord(new _p12_Siniestro(_p12_slist1[indice].siniestroPD[indiceSiniestro]));
                
                if(_p12_slist1[indice].TIPOFORMATOCALCULO =="1"){
                    debug("HOSPITALIZACION");
                    var causaSiniestro =  _p12_slist1[indice].siniestroPD[indiceSiniestro].CAUSASINIESTRO;
                    var importe        =  _p12_lhosp[indice].PRECIO*1.0 ;
                    var descuento      =  _p12_lhosp[indice].DESCPRECIO*1.0;
                    var subttDesc      =  importe - descuento;
                    var deducible      =  0;
                    var sDeducible     =  _p12_slist1[indice].siniestroPD[indiceSiniestro].conceptosAsegurado[indiceSiniestro].DEDUCIBLE;
                    
                    debug("CAUSA SINIESTRO -->"+causaSiniestro);
                    debug("Importe -->"+importe);
                    debug("descuento -->"+descuento);
                    debug('subttDesc -->' , subttDesc);
                    debug('sDeducible -->' , sDeducible);
                    
                    if(causaSiniestro != _CAUSA_ACCIDENTE){
                        if(
                                !(!sDeducible
                                ||sDeducible.toLowerCase()=='na'
                                ||sDeducible.toLowerCase()=='no')
                        )
                        {
                            deducible = sDeducible.replace(',','')*1.0;
                        }
                    }
                    var subttDedu = subttDesc - deducible;
                    debug('subttDedu -->' , subttDedu);
                    
                    if(causaSiniestro != _CAUSA_ACCIDENTE){
                        var copagoPesos       = _p12_slist1[indice].siniestroPD[indiceSiniestro].COPAGOPESOS;
                        var copagoPorcentajes = _p12_slist1[indice].siniestroPD[indiceSiniestro].COPAGOPORCENTAJES;
                        
                        var copagoaplica =  (copagoPesos*1.0) + (subttDedu*(copagoPorcentajes/100.0));
                    }else{
                        var copagoaplica = 0.0;
                    }
                    
                    debug("copagoaplica--->"+copagoaplica);
                    var iva       = _p12_lhosp[indice].IVA*1.0;
                    var baseIva =   _p12_lhosp[indice].BASEIVA*1.0;
                    var ivaRetenido = _p12_lhosp[indice].IVARETENIDO*1.0;
                    debug('subttDedu',subttDedu);
                    debug('subttDesc',subttDesc);
                    debug('deducible',deducible);
                    debug('subttDedu',subttDedu);
                    debug('copago',copago);
                    debug('tipcopag',tipcopag);
                    debug('copagoaplica',copagoaplica);
                    totalIVA 		 =  totalIVA + iva;
                    totalISR  		 =  _p12_lhosp[indice].IMPISR*1.0;
                    totalIVARet 	 =  totalIVARet + ivaRetenido;
                    totalImpCedular  =  _p12_lhosp[indice].IMPCED*1.0;
                    var total =  (subttDedu + iva) - (copagoaplica + ivaRetenido+ totalISR + totalImpCedular);
                    debug('total',total);
                    totalglobal = totalglobal + total;
                    var panelCuentas = Ext.create('Ext.panel.Panel',
                    {
                        title     : 'Resumen'
                        ,border   : 1
                        ,defaults :
                        {
                            style : 'margin : 5px;'
                        }
                        ,items    :
                        [
                            {
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Importe'
                                ,value      : importe
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Descuento'
                                ,value      : descuento
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Subtotal'
                                ,value      : subttDesc
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Deducible'
                                ,value      : deducible
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Subtotal'
                                ,value      : subttDedu
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Copago' //  PAGO DIRECRO
                                ,value      : copagoaplica
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Base IVA'
                                ,value      : baseIva
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            },{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'IVA' //IVA PAGO DIRECTO HOSPI
                                ,value      : iva
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            },{
                                xtype       : 'displayfield'
                                    ,labelWidth : 200
                                    ,fieldLabel : 'IVA Retenido' //IVA PAGO DIRECTO HOSPI
                                    ,value      : ivaRetenido
                                    ,valueToRaw : function(value)
                                    {
                                        return Ext.util.Format.usMoney(value);
                                    }
                                }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                    ,fieldLabel : 'ISR' //IVA PAGO DIRECTO HOSPI
                                    ,value      : totalISR
                                    ,valueToRaw : function(value)
                                    {
                                        return Ext.util.Format.usMoney(value);
                                    }
                            ////		
                                }
                            ,{
                                xtype       : 'displayfield'
                                    ,labelWidth : 200
                                    ,fieldLabel : 'IMP. CEDULAR' //IVA PAGO DIRECTO HOSPI
                                    ,value      : totalImpCedular
                                    ,valueToRaw : function(value)
                                    {
                                        return Ext.util.Format.usMoney(value);
                                    }
                                }
                            ,{
                                xtype       : 'displayfield'
                                ,labelWidth : 200
                                ,fieldLabel : 'Total'
                                ,value      : total
                                ,valueToRaw : function(value)
                                {
                                    return Ext.util.Format.usMoney(value);
                                }
                            }
                        ]
                    });
                    var panelIterado = Ext.create('Ext.panel.Panel',
                    {
                    	title           : 'SINIESTRO',
                    	defaults       :
                        {
                            style : 'margin : 5px;'
                        }
                        ,items          :
                        [
							panelSiniestro
                            ,panelCuentas
                        ]
                    });
                    _p12_paneles.push(panelIterado);
                }
                else{
                	debug('PAGO DIRECTO');
                	totalglobal = totalglobal + _p12_lpdir[indiceSiniestro].total*1.0;
                	totalIVA   =  totalIVA + _p12_lpdir[indiceSiniestro].ivaTotalMostrar*1.0;
                	totalISR =  totalISR + _p12_lpdir[indiceSiniestro].iSRMostrar*1.0;
                	totalIVARet =  totalIVARet + _p12_lpdir[indiceSiniestro].ivaRetenidoMostrar*1.0;
                	totalImpCedular =  totalImpCedular + _p12_lpdir[indiceSiniestro].totalcedular*1.0;
                	var gridCuentas = Ext.create('Ext.grid.Panel',
                	{
                		store    : Ext.create('Ext.data.Store',
                		{
                			autoLoad : true
                			,model   : '_p12_Concepto'
                			,proxy   :
                			{
                				type    : 'memory'
                				,reader : 'json'
                				,data   : _p12_slist1[indice].siniestroPD[indiceSiniestro].conceptosAsegurado 
                			}
                		})
                		,height  : 180
                		,columns :
                		[
                		    {
                		    	header     : 'Tipo concepto'
                		    	,dataIndex : 'DESCRIPC' 
                		    	//,flex      : 1
                		    }
                		    ,{
                		    	header     : 'Concepto'
                		    	,dataIndex : 'OTVALOR'
                		    	//,flex      : 1
                		    }
                		    ,{
                                header     : 'Cantidad'
                                ,dataIndex : 'CANTIDAD'
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Importe arancel'
                                ,dataIndex : 'IMP_ARANCEL'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Subtotal arancel'
                                ,dataIndex : 'SUBTTARANCEL'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Descuento'
                                ,dataIndex : 'DESTOAPLICA'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Subtotal descuento'
                                ,dataIndex : 'SUBTTDESCUENTO'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Copago'
                                ,dataIndex : 'COPAGOAPLICA'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Subtotal copago'
                                ,dataIndex : 'SUBTTCOPAGO'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'ISR'
                                ,dataIndex : 'ISRAPLICA'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Impuesto cedular'
                                ,dataIndex : 'CEDUAPLICA'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Subtotal impuestos'
                                ,dataIndex : 'SUBTTIMPUESTOS'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'IVA'
                                ,dataIndex : 'IVAAPLICA'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'IVA RETENIDO'
                                ,dataIndex : 'IVARETENIDO'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Importe autom&aacute;tico'
                                ,dataIndex : 'PTIMPORTAUTO'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Importe facturado'
                                ,dataIndex : 'PTIMPORT'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Autorizaci&oacute;n<br/>m&eacute;dica'
                                ,dataIndex : 'AUTMEDIC'
                                ,renderer  : function(v)
                                {
                                	var r ='N/A';
                                	if(v=='S')
                                	{
                                		r='SI';
                                	}
                                	else if(v=='N')
                                    {
                                        r='NO';
                                    }
                                	return r;
                                }
                                //,flex      : 1
                            }
                		    ,{
                                header     : 'Valor usado'
                                ,dataIndex : 'VALORUSADO'
                                ,renderer  : Ext.util.Format.usMoney
                                //,flex      : 1
                            }
                		    ,{
                		    	header     : 'Observaciones<br/>M&eacute;dicas'
                		    	,dataIndex : 'COMMENME'
                		    	//,flex      : 1
                		    }
                		]
                		//,viewConfig :
    	                //{
    	                    //listeners :
    	                    //{
    	                        //refresh : function(dataview)
    	                        //{
    	                            //Ext.each(dataview.panel.columns, function(column)
    	                            //{
    	                                //column.autoSize();
    	                            //});
    	                        //}
    	                    //}
    	                //}
                	    //,listeners :
                	    //{
                	    	//itemclick : _p12_mostrarWindowAutoriza
                	    //}
                	});
    	            var panelTotales = Ext.create('Ext.form.Panel',
    	            {
    	                layout    :
    	                {
    	                    type     : 'table'
    	                    ,columns : 1
    	                }
    	                ,border   : 0
    	                ,width    : 800
    	                ,defaults :
    	                {
    	                    style : 'margin : 5px;'
    	                }
    	                ,items    :
    	                [
    	                    {
    	                        xtype       : 'displayfield'
    	                        ,fieldLabel : 'SUBTOTAL'
    	                        ,labelWidth : 200
    	                        ,value      : ((_p12_lpdir[indiceSiniestro].total)*1) + ((_p12_lpdir[indiceSiniestro].iSRMostrar)*1) + ((_p12_lpdir[indiceSiniestro].ivaRetenidoMostrar)*1) - ((_p12_lpdir[indiceSiniestro].ivaTotalMostrar)*1)
    	                        ,valueToRaw : function(value)
    	                        {
    	                        	return Ext.util.Format.usMoney(value);
    	                        }
    	                    },
    	                    {
    	                        xtype       : 'displayfield'
    	                        ,fieldLabel : 'IVA'
    	                        ,labelWidth : 200
    	                        ,value      : _p12_lpdir[indiceSiniestro].ivaTotalMostrar
    	                        ,valueToRaw : function(value)
    	                        {
    	                        	return Ext.util.Format.usMoney(value);
    	                        }
    	                    },
    	                    {
    	                        xtype       : 'displayfield'
    	                        ,fieldLabel : 'IVA Retenido'
    	                        ,labelWidth : 200
    	                        ,value      :  _p12_lpdir[indiceSiniestro].ivaRetenidoMostrar
    	                        ,valueToRaw : function(value)
    	                        {
    	                        	return Ext.util.Format.usMoney(value);
    	                        }
    	                    },
    	                    
    	                    {
    	                        xtype       : 'displayfield'
    	                        ,fieldLabel : 'ISR'
    	                        ,labelWidth : 200
    	                        ,value      : _p12_lpdir[indiceSiniestro].iSRMostrar
    	                        ,valueToRaw : function(value)
    	                        {
    	                        	return Ext.util.Format.usMoney(value);
    	                        }
    	                    },
    	                    {
    	                        xtype       : 'displayfield'
    	                        ,fieldLabel : 'TOTAL'
    	                        ,labelWidth : 200
    	                        ,value      :  _p12_lpdir[indiceSiniestro].total
    	                        ,valueToRaw : function(value)
    	                        {
    	                        	return Ext.util.Format.usMoney(value);
    	                        }
    	                    }
    	                ]
    	            });
    	            var panelIterado = Ext.create('Ext.panel.Panel',
    	            {
    	            	title           : 'SINIESTRO',
    	            	collapsible   : true
    	            	,titleCollapse : true
    	            	,collapsed     : true
    	            	,defaults      :
    	                {
    	                    style : 'margin : 5px;'
    	                }
    	                ,items         :
    	                [
    	                    panelSiniestro
    	                    ,gridCuentas
    	                    ,panelTotales
    	                ]
    	            });
    	            _p12_paneles.push(panelIterado);
                }
            }
        }
    }
    else
    {
    	//PAGO POR REEMBOLSO
        debug('REEMBOLSO');
        var indice;
        for(indice = 0;indice<_p12_slist1.length;indice++)
        {
            debug('Iterando facturas n '+(indice+1));
            var panelFactura = Ext.create('Ext.form.Panel',
            {
                layout    :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,border   : 0
                ,defaults :
                {
                    style : 'margin : 5px;'
                }
                ,items    :
                [
                    {
                        xtype       : 'displayfield'
                        ,fieldLabel : 'N&Uacute;MERO DE FACTURA'
                        ,name       : 'NFACTURA'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'FECHA'
                        ,name       : 'FFACTURA'
                        ,valueToRaw : function(v)
                        {
                        	return Ext.Date.format(v,'d M Y');
                        }
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'COBERTURA'
                        ,name       : 'DSGARANT'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'SUBCOBERTURA'
                        ,name       : 'DSSUBGAR'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'DESCUENTO %'
                        ,name       : 'DESCPORC'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'DESCUENTO $'
                        ,name       : 'DESCNUME'
                       	,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'SERVICIO'
                        ,name       : 'DESCSERVICIO'
                        ,colspan    : 2
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'PROVEEDOR'
                        ,name       : 'NOMBREPROVEEDOR'
                        ,colspan    : 2
                    }
                ]
            });
            panelFactura.loadRecord(new _p12_Factura(_p12_slist1[indice]));
            
            var gridCuentas = Ext.create('Ext.grid.Panel',
            {
                store    : Ext.create('Ext.data.Store',
                {
                    autoLoad : true
                    ,model   : '_p12_Concepto'
                    ,proxy   :
                    {
                        type    : 'memory'
                        ,reader : 'json'
                        ,data   : _p12_llist1[indice]
                    }
                })
                ,height  : 180
                ,columns :
                [
                    {
                        header     : 'Tipo concepto'
                        ,dataIndex : 'DESCRIPC' 
                        //,flex      : 1
                    }
                    ,{
                        header     : 'Concepto'
                        ,dataIndex : 'OTVALOR'
                        //,flex      : 1
                    }
                    ,{
                        header     : 'Importe neto'
                        ,dataIndex : 'PTIMPORT'
                        ,renderer  : Ext.util.Format.usMoney
                        //,flex      : 1
                    }
                    ,{
                        header     : 'Ajuste'
                        ,dataIndex : 'PTIMPORT_AJUSTADO'
                        ,renderer  : Ext.util.Format.usMoney
                        //,flex      : 1
                    }
                    ,{
                        header     : 'Subtotal'
                        ,dataIndex : 'SUBTOTAL'
                        ,renderer  : Ext.util.Format.usMoney
                        //,flex      : 1
                    }
                ]
                ,viewConfig :
                {
                    listeners :
                    {
                        refresh : function(dataview)
                        {
                            Ext.each(dataview.panel.columns, function(column)
                            {
                                column.autoSize();
                            });
                        }
                    }
                }
            });
            debug('indice,_p12_lprem[indice]',indice,_p12_lprem[indice]);
            
            var ptimpoajus  = _p12_lprem[indice].SUBTOTAL*1.0;
            var destopor    = _p12_slist1[indice].DESCPORC*1.0;
            var destoimp    = _p12_slist1[indice].DESCNUME*1.0;
            var destoaplica = (ptimpoajus*(_p12_slist1[indice].DESCPORC/100.0)) + destoimp;
            var subttdesc   = ptimpoajus-destoaplica;
            var sDeducible  = _p12_slist2[indice].DEDUCIBLE;
            var deducible   = 0;
            var _facturaIndividual = _p12_slist1[indice];
            var causaSiniestro = _p12_penalTotal[indice].causaSiniestro;
            
            if(
                    !(!sDeducible
                    ||sDeducible.toLowerCase()=='na'
                    ||sDeducible.toLowerCase()=='no')
                    )
            {
                deducible = sDeducible.replace(',','')*1.0;
                
                //if(_facturaIndividual.CDGARANT=='18HO'||_facturaIndividual.CDGARANT=='18MA')
               	if(_p12_coberturaxcal[indice].tipoFormatoCalculo =='1')
               	{
                	if(causaSiniestro == _CAUSA_ACCIDENTE){
                		deducible = 0;
                	}
               	}else{
               		if(causaSiniestro == _CAUSA_ACCIDENTE){
                		deducible = 0;
                	}
               	}
            }
            
            
            var subttdeduc  = subttdesc-deducible;
            _p12_slist2[indice].COPAGOAUX = _p12_slist2[indice].COPAGO;
            _p12_slist2[indice].COPAGO = 0;
            if(
                !(!_p12_slist2[indice].COPAGOAUX
                ||_p12_slist2[indice].COPAGOAUX.toLowerCase()=='na'
                ||_p12_slist2[indice].COPAGOAUX.toLowerCase()=='no')    
            )
            {
                _p12_slist2[indice].COPAGO = _p12_slist2[indice].COPAGOAUX.replace(",","");
            }
            var copago      = _p12_slist2[indice].COPAGO*1.0;
            var tipcopag    = _p12_slist2[indice].TIPOCOPAGO;
            
            if(_p12_coberturaxcal[indice].tipoFormatoCalculo =='1'){
            	if(causaSiniestro != _CAUSA_ACCIDENTE){
            		var copagoPesos       = _p12_penalTotal[indice].copagoPesos;
            		var copagoPorcentajes = _p12_penalTotal[indice].copagoPorcentajes;
            		var copagoaplica 	  = (copagoPesos*1.0) + (subttdeduc*(copagoPorcentajes/100.0));
            	}else{
            		var copagoaplica = 0.0;
            	}
           	}else{
           		if(causaSiniestro != _CAUSA_ACCIDENTE){
           		if(tipcopag=='$'){
                    var copagoaplica = copago;
                }
                else if(tipcopag=='%'){
                    var copagoaplica = subttdeduc*(copago/100.0);
                }
                else{
                    var copagoaplica = 0.0;
                }
           		}else{
           			var copagoaplica = 0.0;
           		}
           	}
            var total = subttdeduc - copagoaplica;
            debug("VALOR DEL TOTAL---> ",_p12_slist1[indice]['TOTALFACTURA']);
            _p12_slist1[indice]['TOTALFACTURA']=total;
            totalglobal = totalglobal + total;

			var panelTotales = Ext.create('Ext.panel.Panel',
            {
            	defaults :
            	{
            		style : 'margin : 5px;'
            	}
                ,border  : 0
                ,layout  :
                {
                	type     : 'table'
                	,columns : 2
                }
                ,items   :
                [
                    {
                    	xtype       : 'displayfield'
                    	,labelWidth : 200
                    	,fieldLabel : 'Total neto'
                    	,value      : _p12_lprem[indice].TOTALNETO
                    	,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                    	xtype       : 'displayfield'
                    	,labelWidth : 200
                    	,fieldLabel : 'Subtotal ajustado'
                    	,value      : _p12_lprem[indice].SUBTOTAL
                    	,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                    	xtype : 'label'
                    }
                    ,{
                    	xtype       : 'displayfield'
                    	,labelWidth : 200
                    	,value      : destoaplica
                    	,fieldLabel : 'Ajuste'
                    	,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                    	xtype : 'label'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,labelWidth : 200
                        ,value      : subttdesc
                        ,fieldLabel : 'Total autorizado'
                        ,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                        xtype : 'label'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,labelWidth : 200
                        ,value      : deducible
                        ,fieldLabel : 'Deducible'
                        ,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                        xtype : 'label'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,labelWidth : 200
                        ,value      : subttdeduc
                        ,fieldLabel : 'Subtotal despu&eacute;s deducible'
                        ,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                        xtype : 'label'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,labelWidth : 200
                        ,value      : copagoaplica
                        ,fieldLabel : 'Copago'  // PAGO POR REEMBOLSO
                        ,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    ,{
                        xtype : 'label'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,labelWidth : 200
                        ,value      : total
                        ,fieldLabel : 'Total a pagar'
                        ,valueToRaw : function(value)
                        {
                            return Ext.util.Format.usMoney(value);
                        }
                    }
                    
                ]
            });
            var panelIterado = Ext.create('Ext.panel.Panel',
            {
                title          : 'FACTURA'
                ,collapsible   : true
                ,titleCollapse : true
                ,collapsed     : true
                ,defaults      :
                {
                    style : 'margin : 5px;'
                }
                ,items         :
                [
                    panelFactura
                    ,gridCuentas
                    ,panelTotales
                ]
            });
            _p12_paneles.push(panelIterado);
        }
    }
    
    _p12_paneles.push(Ext.create('Ext.form.Panel',
    	    {
    	    	title     : 'TOTAL PAGAR'
    	    	,defaults :
    	    	{
    	    		style : 'margin : 5px;'
    	    	}
    	        //,layout : 'hbox'
    	        ,layout  :
    	        {
    	        	type     : 'table'
    	        	,columns : 1
    	        }
    	    	,items    :
    	    	[
    	    	    {
    	    	    	xtype       : 'displayfield'
    	    	    	,labelWidth : 200
    	    	    	,fieldLabel : 'TOTAL DEL TR&Aacute;MITE'
    	    	    	,value      : totalglobal
    	    	    	,valueToRaw : function(value)
    	                {
    	                    return Ext.util.Format.usMoney(value);
    	                }
    	    	    }
    	    	    ,
    	    	    {
    	    	    	xtype       : 'displayfield'
    	    	    	,labelWidth : 200
    	    	    	,fieldLabel : 'IVA'
    	    	    	,value      : totalIVA
    	    	    	,valueToRaw : function(value)
    	                {
    	                    return Ext.util.Format.usMoney(value);
    	                }
    	    	    }
    	    	    ,
    	    	    {
    	    	    	xtype       : 'displayfield'
    	    	    	,labelWidth : 200
    	    	    	,fieldLabel : 'ISR'
    	    	    	,value      : totalISR
    	    	    	,valueToRaw : function(value)
    	                {
    	                    return Ext.util.Format.usMoney(value);
    	                }
    	    	    }
    	    	    ,
    	    	    {
    	    	    	xtype       : 'displayfield'
    	    	    	,labelWidth : 200
    	    	    	,fieldLabel : 'IVA RETENIDO'
    	    	    	,value      : totalIVARet
    	    	    	,valueToRaw : function(value)
    	                {
    	                    return Ext.util.Format.usMoney(value);
    	                }
    	    	    }
    	    	    ,
    	    	    {
    	    	    	xtype       : 'displayfield'
    	    	    	,labelWidth : 200
    	    	    	,fieldLabel : 'IMPUESTO CEDULAR'
    	    	    	,value      : totalImpCedular
    	    	    	,valueToRaw : function(value)
    	                {
    	                    return Ext.util.Format.usMoney(value);
    	                }
    	    	    }
    	    	]
    	    }));
    	    

    ////// componentes //////
	

	_p12_formAutoriza = Ext.create('Ext.form.Panel',
	{
		items : [ <s:property value="imap.autorizaItems" /> ] 
	});
	
	_p12_windowAutoriza = Ext.create('Ext.window.Window',
	{
		title        : 'Autorizar concepto'
		,width       : 600
		,height      : 400
		,closeAction : 'hide'
		,items       : _p12_formAutoriza
		,modal       : true
		,buttonAlign : 'center'
		,buttons     :
		[
		    {
		    	text     : 'Guardar'
		    	,icon    : '${ctx}/resources/fam3icons/icons/key.png'
		    	//,handler : _p12_autorizarConcepto
		    }
		]
	});
	
	_p12_formTramite = Ext.create('Ext.form.Panel',
	{
		title   : 'TR&Aacute;MITE'
		,layout :
		{
			type     : 'table'
			,columns : 2
		}
		,items  : [ <s:property value="imap.tramiteItems" /> ]
	});
	
	_p12_formFactura = Ext.create('Ext.form.Panel',
    {
        title   : 'FACTURA'
        ,layout :
        {
            type     : 'table'
            ,columns : 2
        }
	    ,hidden : _p12_smap.PAGODIRECTO=='N'
        ,items  : [ <s:property value="imap.facturaItems" /> ]
    });
	
	_p12_formSiniestro = Ext.create('Ext.form.Panel',
    {
        title   : 'SINIESTRO'
        ,layout :
        {
            type     : 'table'
            ,columns : 2
        }
        ,hidden : _p12_smap.PAGODIRECTO=='S'
        ,items  : [ <s:property value="imap.siniestroItems" /> ]
    });
	
	_p12_formProveedor = Ext.create('Ext.form.Panel'
	,{
		title   : 'PROVEEDOR'
		,layout :
	    {
	        type     : 'table'
	        ,columns : 2
	    }
	    ,hidden : _p12_smap.PAGODIRECTO=='N'
	    ,items  : [ <s:property value="imap.proveedorItems" /> ]
	});
	
	
	_p12_panelCalculo = Ext.create('Ext.panel.Panel',
	{
		title     : 'C&Aacute;LCULO'
		,autoScroll : true
		,height: 1800
		,defaults :
		{
			style : 'margin : 5px;'
		}
		,items    : _p12_paneles
	});
	
	Ext.create('Ext.panel.Panel',
	{
		defaults  : { style : 'margin : 5px;' }
	    ,renderTo : '_p12_divpri'
	    ,items    :
	    [
	        _p12_formTramite
	        ,_p12_formProveedor
	        ,_p12_panelCalculo
	    ]
	});
	////// contenido //////
	_p12_formTramite.loadRecord(new _p12_Tramite(_p12_smap));
	_p12_formProveedor.loadRecord(new _p12_Proveedor(_p12_smap3));
});

function _p12_validaAutorizaciones()
{
	var result = '';
	debug('_p12_validaAutorizaciones');
	var esPagoDirecto = _p12_smap.PAGODIRECTO=='S';
	debug('esPagoDirecto:',esPagoDirecto);
	if(esPagoDirecto)
	{
		var esHospital =  _p12_slist1[0].TIPOFORMATOCALCULO =='1';
		debug('esHospital:',esHospital);
		if(esHospital&&false)
		{
			debug('validando hospitalizacion pago directo');
		}
	}
	else
	{
		debug('validando reembolso');
		var facturas = _p12_slist1;
		debug('facturas a validar:',facturas);
		var i;
		for(i=0;i<facturas.length;i++)
		{
			var facturaIte = facturas[i];
			debug("FACTURA --->>"+i);
			debug(facturaIte);
			
			if(+ facturaIte.PTIMPORT <= 0){
				result = result + 'Verifica el Importe ' + facturaIte.NFACTURA + '<br/>';
			}
			
			if(+facturaIte.DCTONUEX > +facturaIte.DESCNUME){
				result = result + 'Verifica el Descuento ' + facturaIte.NFACTURA + '<br/>';
			}
			
			if(facturaIte.AUTRECLA!='S')
            {
                result = result + 'Reclamaciones no autoriza la factura ' + facturaIte.NFACTURA + '<br/>';
            }
            if(facturaIte.AUTMEDIC!='S')
            {
                result = result + 'El m&eacute;dico no autoriza la factura ' + facturaIte.NFACTURA + '<br/>';
            }
            
            
            
            var conceptos = _p12_llist1[i];
            //console.log(conceptos);
            var j;
            for(j=0;j<conceptos.length;j++)
            {
            	var conceptosInt = conceptos[j];
            	
            	if(+ conceptosInt.PTIMPORT <= 0){
    				result = result + 'Verifica el Importe del concepto ' + conceptosInt.NFACTURA + '<br/>';
    			}
    			
    			if(+conceptosInt.DCTOIMEX > +conceptosInt.DESTOIMP){
    				result = result + 'Verifica el Descuento ' + conceptosInt.NFACTURA + '<br/>';
    			}
    			if(+conceptosInt.PTPCIOEX > +conceptosInt.PTPRECIO){
    				result = result + 'Verifica el Precio ' + conceptosInt.NFACTURA + '<br/>';
    			}
            	
            }
		}
	}
	return result;
}
////// funciones //////

</script>
<div id="_p12_divpri" style="height:2400px;"></div>