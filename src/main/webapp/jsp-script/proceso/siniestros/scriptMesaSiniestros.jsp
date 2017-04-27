<%@ include file="/taglibs.jsp"%>
<s:if test="false">
<script>
</s:if>
var _CONTEXT = '${ctx}';
 

/* ******************** CATALOGOS ******************** */

// Catalogo Tipos de pago a utilizar:
var _PAGO_DIRECTO = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@DIRECTO.codigo" />';
var _REEMBOLSO    = '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@REEMBOLSO.codigo" />';
var _INDEMNIZACION= '<s:property value="@mx.com.gseguros.portal.general.util.TipoPago@INDEMNIZACION.codigo" />';
var _URL_CATALOGOS                      = '<s:url namespace="/catalogos"   action="obtieneCatalogo" />';
// Catalogo Estatus de tramite a utilizar:
var _STATUS_TRAMITE_EN_REVISION_MEDICA      = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_REVISION_MEDICA.codigo" />';
var _STATUS_TRAMITE_RECHAZADO               = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@RECHAZADO.codigo" />';
var _STATUS_TRAMITE_EN_CAPTURA              = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_CAPTURA.codigo" />';
var _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@EN_ESPERA_DE_ASIGNACION.codigo" />';
var _STATUS_TRAMITE_CONFIRMADO              = '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@CONFIRMADO.codigo" />';
var _CAT_DESTINOPAGO                        = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@DESTINOPAGO"/>';
var _CAT_CONCEPTO                           = '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CATCONCEPTO"/>';
var _STATUS_DEVOLVER_TRAMITE				= '<s:property value="@mx.com.gseguros.portal.general.util.EstatusTramite@TRAMITE_EN_DEVOLUCION.codigo" />';
var _CATALOGO_CONCEPTOPAGO					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@CONCEPTOPAGO"/>';
var _TIPO_TRAMITE_SINIESTRO					= '<s:property value="@mx.com.gseguros.portal.general.util.TipoTramite@SINIESTRO.cdtiptra" />';
var _CATALOGO_PROVEEDORES  					= '<s:property value="@mx.com.gseguros.portal.general.util.Catalogos@PROVEEDORES"/>';

var _URL_LISTADO_ASEGURADO_POLIZA			= '<s:url namespace="/siniestros"       action="consultaListaAseguradoPoliza" />';
var _URL_CONSULTA_BENEFICIARIO				= '<s:url namespace="/siniestros"		action="consultaDatosBeneficiario" />';

/* *************************************************** */

// URLs:
var _UrlDocumentosPoliza        		= '<s:url namespace="/documentos" 	action="ventanaDocumentosPoliza"   />';
var _URL_CATALOGOS              		= '<s:url namespace="/catalogos"   	action="obtieneCatalogo" />';
var _URL_ActualizaStatusTramite 		= '<s:url namespace="/mesacontrol" 	action="actualizarStatusTramite" />';
var _URL_TurnarAOperadorReclamacion 	= '<s:url namespace="/mesacontrol" 	action="turnarAOperadorReclamacion" />';
var panDocUrlViewDoc     				= '<s:url namespace ="/documentos" 	action="descargaDocInline" />';
var mesConUrlDetMC        				= '<s:url namespace="/mesacontrol" 	action="obtenerDetallesTramite"    />';
var mesConUrlFinDetalleMC 				= '<s:url namespace="/mesacontrol" 	action="finalizarDetalleTramiteMC" />';
//AUTORIZACION DE SERVICIO
var _Url_AltaTramite_Previo      		= '<s:url namespace="/siniestros" 	action="includes/altaTramitePrevio"      />';
var _Url_ComplementoAltaTramite  		= '<s:url namespace="/siniestros" 	action="complementoAltaTramite"      />';
var _4_urlPantallaAutServ 				= '<s:url namespace="/siniestros" 	action="autorizacionServicios" />';
var _UrlRevisionDocsSiniestro   		= '<s:url namespace="/siniestros" 	action="includes/revisionDocumentos"        />';
var _UrlValidaDocumentosCargados		= '<s:url namespace="/siniestros" 	action="validaDocumentosCargados"        />';
var _UrlGenerarContrarecibo     		= '<s:url namespace="/siniestros" 	action="generarContrarecibo"       />';
var _URL_CONSULTA_TRAMITE       		= '<s:url namespace="/siniestros"   action="consultaListadoMesaControl" />';
var _URL_CONSULTA_GRID_ALTA_TRAMITE 	= '<s:url namespace="/siniestros"   action="consultaListadoAltaTramite" />';
var _UrlGeneraSiniestroTramite 			= '<s:url namespace="/siniestros" 	action="generaSiniestroTramite" />';
var _URL_VALIDA_FACTURAASEGURADO  		= '<s:url namespace="/siniestros"	action="validarFacturaAsegurado" />';
var _URL_NOMBRE_TURNADO   				= '<s:url namespace="/siniestros" 	action="obtieneUsuarioTurnado" />';

//SINIESTROS
var _UrlRechazarTramiteWindwow  		= '<s:url namespace="/siniestros" 	action="includes/rechazoReclamaciones" />';
var _UrlDetalleSiniestro        		= '<s:url namespace="/siniestros" 	action="detalleSiniestro" />';
var _UrlDetalleSiniestroDirecto 		= '<s:url namespace="/siniestros" 	action="afiliadosAfectados"        />';
var _UrlSolicitarPago           		= '<s:url namespace="/siniestros" 	action="solicitarPago"             />';
var _URL_VALIDAR_PROVEEDOR_PD			= '<s:url namespace="/siniestros"	action="validarProveedorPD" />'; // (EGS)
var _UrlSolicitarComplemento			= '<s:url namespace="/siniestros" 	action="solicitarComplemento"      />';
var _URL_CONCEPTODESTINO        		= '<s:url namespace="/siniestros"   action="guardarConceptoDestino" />';
var _mesasin_url_lista_reasignacion 	= '<s:url namespace="/siniestros" 	action="obtenerUsuariosPorRol" />';
var _selCobUrlAvanza              		= '<s:url namespace="/siniestros" 	action="afiliadosAfectados"/>';
var _urlSeleccionCobertura      		= '<s:url namespace="/siniestros" 	action="seleccionCobertura"        />';
var _URL_VAL_AJUSTADOR_MEDICO			= '<s:url namespace="/siniestros" 	action="consultaDatosValidacionAjustadorMed"/>';
var _URL_INF_ASEGURADO					= '<s:url namespace="/siniestros" 	action="consultaDatosAseguradoSiniestro"/>';
var _URL_POLIZA_UNICA					= '<s:url namespace="/siniestros"	action="consultaPolizaUnica"/>';
var _URL_MONTO_PAGO_SINIESTRO			= '<s:url namespace="/siniestros"	action="obtieneMontoPagoSiniestro"/>';
var _URL_P_MOV_MAUTSINI					= '<s:url namespace="/siniestros"	action="obtieneMensajeMautSini"/>';
var _URL_VALIDA_AUTESPECIFICA			= '<s:url namespace="/siniestros"	action="validaAutorizacionEspecial"/>';
var _URL_MESACONTROL					= '<s:url namespace="/mesacontrol" 	action="mcdinamica" />';
var _URL_EXISTE_COBERTURA				= '<s:url namespace="/siniestros" 	action="consultaExisteCoberturaTramite" />';
var _URL_VAL_CAUSASINI			        = '<s:url namespace="/siniestros" 	action="consultaInfCausaSiniestroProducto" />';
var _URL_VALIDA_COBASEGURADOS			= '<s:url namespace="/siniestros" 	action="validaLimiteCoberturaAsegurados"/>';
var _URL_VALIDA_IMPASEGURADOSINIESTRO	= '<s:url namespace="/siniestros" 	action="validaImporteTramiteAsegurados"/>';
var _0_urlRutaReporte                   = '<s:property value="rutaServidorReports" />';
var _0_reportsServerUser                = '<s:property value="passServidorReports" />';
var _0_reporteContraRecibo              = '<s:text name="rdf.siniestro.contrarecibo.nombre" />';

var _0_reporteRechazoReembolso          = '<s:text name="rdf.siniestro.cartarechazo.reembolso.nombre"/>';
var _0_reporteRechazoDirecto            = '<s:text name="rdf.siniestro.cartarechazo.pagodirecto.nombre" />';

var _0_urlEnviarCorreo                  = '<s:url namespace="/general"         action="enviaCorreo"/>';

var windowLoader;
var msgWindow;

	_4_botonesGrid =
	[
        <s:property value="imap1.gridbuttons" />
	];
	
	function altaTramiteWindow(){
	    windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 830,
	        height      : 620,
	        title: 'Alta de Tr&aacute;mite Previo',
	        autoScroll  : true,
	        loader      : {
	            url     : _Url_AltaTramite_Previo,
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
	    centrarVentana(windowLoader);
	}
	
	function complementarAltaWindow(grid,rowIndex){
		
		var record = grid.getStore().getAt(rowIndex);
		if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
			mensajeWarning('No se puede complementar el tr&aacute;mite ya se encuentra rechazado');
			return;
		}
		else{
			var record = grid.getStore().getAt(rowIndex);
			Ext.create("Ext.form.Panel").submit(
					{
						url     : _Url_ComplementoAltaTramite,
						params :{ 'params.ntramite' : record.get('ntramite')},
						standardSubmit:true
					});
		}
	}
	
	
	function revDocumentosWindow(grid,rowIndex,colIndex){
	    var record = grid.getStore().getAt(rowIndex);
	    
	    windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 600,
	        height      : 400,
	        autoScroll  : true,
	        loader      : {
	            url     : _UrlRevisionDocsSiniestro,
	            params  : {
	                'params.nmTramite'  : record.get('ntramite'),
	                'params.cdTipoPago' : record.get('parametros.pv_otvalor02'),
	                'params.cdTipoAtencion'  : record.get('parametros.pv_otvalor07'),
	                'params.tieneCR'  : !Ext.isEmpty(record.get('parametros.pv_otvalor01'))
	            },
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
	    centrarVentana(windowLoader);
	}
	
	function rechazarTramiteWindow(grid,rowIndex,colIndex){
		
		var record = grid.getStore().getAt(rowIndex);
		if(record.get('status') == _STATUS_TRAMITE_RECHAZADO){
			mensajeWarning('Este tr&aacute;mite ya se encuentra rechazado!');
			return;
		}
		
		windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 700,
	        height      : 500,
	        autoScroll  : true,
	        loader      : {
	            url     : _UrlRechazarTramiteWindwow,
	            params  : {
	            	'params.cdunieco' : record.get('cdunieco'),
		    		'params.cdramo'   : record.get('cdramo'),
		    		'params.estado'   : record.get('estado'),
		    		'params.nmpoliza' : record.get('nmpoliza'),
		    		'params.nmsuplem' : record.get('nmsuplem'),
		    		'params.nmsolici' : record.get('nmsolici'),
	                'params.nmTramite'  : record.get('ntramite'),
	                'params.tipopago'   : record.get('parametros.pv_otvalor02'),
	                'params.cdtipsit'  : record.get('cdtipsit')
	            },
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
		
		centrarVentana(windowLoader);
	}
	
	function documentosWindow(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
	    debug('record',record);
	    
	    windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        title       : 'Documentos del siniestro',
	        width       : 600,
	        height      : 400,
	        autoScroll  : true,
	        cls         : 'VENTANA_DOCUMENTOS_CLASS',
	        loader      : {
	            url     : _UrlDocumentosPoliza,
	            params  : {
	                'smap1.ntramite'  : record.get('ntramite')
	                ,'smap1.cdtippag' : record.get('parametros.pv_otvalor02')
	                ,'smap1.cdtipate' : record.get('parametros.pv_otvalor07')
	                ,'smap1.cdtiptra' : _TIPO_TRAMITE_SINIESTRO
                    ,'smap1.cdunieco' : record.get('cdsucdoc')
                    ,'smap1.cdramo'   : record.get('cdramo')
                    ,'smap1.estado'   : record.get('estado')
	                ,'smap1.nmpoliza' : record.get('nmpoliza')
                    ,'smap1.nmsuplem' : '0'
                    ,'smap1.nmsolici' : ''
                    ,'smap1.tipomov'  : record.get('parametros.pv_otvalor02')
	            },
	            scripts  : true,
	            loadMask : true,
	            autoLoad : true,
	            ajaxOptions: {
	            	method: 'POST'
	            }
	        }
	    }).show();
	    centrarVentana(windowLoader);
	}
	
	function generaContrareciboWindow(grid,rowIndex,colIndex){
		
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url: _UrlGenerarContrarecibo,
			params: {
		    		'paramsO.pv_cdunieco_i' : record.get('cdunieco'),
		    		'paramsO.pv_cdramo_i'   : record.get('cdramo'),
		    		'paramsO.pv_estado_i'   : record.get('estado'),
		    		'paramsO.pv_nmpoliza_i' : record.get('nmpoliza'),
		    		'paramsO.pv_nmsuplem_i' : record.get('nmsuplem'),
		    		'paramsO.pv_ntramite_i' : record.get('ntramite'),
		    		'paramsO.pv_nmsolici_i' : record.get('nmsolici'),
		    		'paramsO.pv_cdtippag_i' : record.get('parametros.pv_otvalor02'),
		    		'paramsO.pv_cdtipate_i' : record.get('parametros.pv_otvalor07'),
		    		'paramsO.pv_tipmov_i'   : record.get('parametros.pv_otvalor02'),
		    		'paramsO.pv_pagoAut_i'  : "0" //pago Normal 
		    	
			},
			success: function(response, opt) {
				var jsonRes=Ext.decode(response.responseText);

				if(jsonRes.success == true){
					loadMcdinStore();
					
					var numRand=Math.floor((Math.random()*100000)+1);
		        	debug('numRand a: ',numRand);
		        	var windowVerDocu=Ext.create('Ext.window.Window',
		        	{
		        		title          : 'Contrarecibo de Documentos del Siniestro'
		        		,width         : 700
		        		,height        : 500
		        		,collapsible   : true
		        		,titleCollapse : true
		        		,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
		        		                 +'src="'+panDocUrlViewDoc+'?subfolder=' + record.get('ntramite') + '&filename=' + '<s:text name="siniestro.contrarecibo.nombre"/>' +'">'
		        		                 +'</iframe>'
		        		,listeners     :
		        		{
		        			resize : function(win,width,height,opt){
		                        debug(width,height);
		                        $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
		                    }
		        		}
		        	}).show();
		        	windowVerDocu.center();
					}else {
						mensajeError(jsonRes.msgResult);
					}
			},
			failure: function(){
				mensajeError('No se pudo generar contrarecibo.');
			}
		});
	}
	
	
    function enviarCorreoContraRecibo(grid,rowIndex,colIndex){
        var record = grid.getStore().getAt(rowIndex);
        Ext.create('Ext.window.Window',
        {
            title : 'Enviar Contra-Recibo'
            ,width : 550
            ,modal : true
            ,height : 150
            ,buttonAlign : 'center'
            ,bodyPadding : 5
            ,items :
            [
                {
                    xtype       : 'textfield'
                    ,id         : '_0_idInputCorreos'
                    ,fieldLabel : 'Correo(s)'
                    ,emptyText  : 'Correo(s) separados por ;'
                    ,labelWidth : 100
                    ,allowBlank : false
                    ,blankText  : 'Introducir correo(s) separados por ;'
                    ,width      : 500
                }
            ]
            ,buttons :
            [
                {
                    text : 'Enviar'
                    ,icon : _CONTEXT+'/resources/fam3icons/icons/accept.png'
                    ,handler : function()
                    {
                        var me = this;
                        if (Ext.getCmp('_0_idInputCorreos').getValue().length > 0
                                &&Ext.getCmp('_0_idInputCorreos').getValue() != 'Correo(s) separados por ;')
                        {
                            me.up().up().setLoading(true);
                            Ext.Ajax.request(
                            {
                                url : _0_urlEnviarCorreo
                                ,params :
                                {
                                    to : Ext.getCmp('_0_idInputCorreos').getValue(),
                                    urlArchivo : _0_urlRutaReporte
                                                 + '?p_usuario='      + "rherdez"
                                                 + "&p_TRAMITE="      + record.get('ntramite')
                                                 + '&destype=cache'
                                                 + "&desformat=PDF"
                                                 + "&userid="         + _0_reportsServerUser
                                                 + "&ACCESSIBLE=YES"
                                                 + "&report="         + _0_reporteContraRecibo
                                                 + "&paramform=no",
                                    nombreArchivo : 'Contrarecibo_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf',
                                    asunto:'Contra-Recibo',
                                    mensaje :'Estimado(a) cliente,anexamos a este e-mail el contrarecibo de su(s) factura(s)  y nos ponemos a sus apreciables órdenes.'
                                },
                                callback : function(options,success,response)
                                {
                                    me.up().up().setLoading(false);
                                    if (success)
                                    {
                                        var json = Ext.decode(response.responseText);
                                        if (json.success == true)
                                        {
                                            me.up().up().destroy();
                                            Ext.Msg.show(
                                            {
                                                title : 'Correo enviado'
                                                ,msg : 'El correo ha sido enviado'
                                                ,buttons : Ext.Msg.OK
                                            });
                                        }
                                        else
                                        {
                                            mensajeError('Error al enviar');
                                        }
                                    }
                                    else
                                    {
                                        errorComunicacion();
                                    }
                                }
                            });
                        }
                        else
                        {
                            mensajeWarning('Introduzca al menos un correo');
                        }
                    }
                }
                ,
                {
                    text     : 'Cancelar'
                    ,icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png'
                    ,handler : function()
                    {
                        this.up().up().destroy();
                    }
                }
            ]
        }).show();
        Ext.getCmp('_0_idInputCorreos').focus();
    }
    
    function enviarCartaRechazoReclamo(grid,rowIndex,colIndex){
        var record = grid.getStore().getAt(rowIndex);
        if(record.get('status') != _STATUS_TRAMITE_RECHAZADO){
            mensajeWarning('Este tr&aacute;mite no se encuentra rechazado!');
            return;
        }
        
        Ext.create('Ext.window.Window',
        {
            title : 'Enviar Carta Rechazo Contra-Recibo'
            ,width : 550
            ,modal : true
            ,height : 150
            ,buttonAlign : 'center'
            ,bodyPadding : 5
            ,items :
            [
                {
                    xtype       : 'textfield'
                    ,id         : '_0_idInputCorreos'
                    ,fieldLabel : 'Correo(s)'
                    ,emptyText  : 'Correo(s) separados por ;'
                    ,labelWidth : 100
                    ,allowBlank : false
                    ,blankText  : 'Introducir correo(s) separados por ;'
                    ,width      : 500
                }
            ]
            ,buttons :
            [
                {
                    text : 'Enviar'
                    ,icon : _CONTEXT+'/resources/fam3icons/icons/accept.png'
                    ,handler : function()
                    {
                        var me = this;
                        var nombreRDF = null;
                        if (Ext.getCmp('_0_idInputCorreos').getValue().length > 0
                                &&Ext.getCmp('_0_idInputCorreos').getValue() != 'Correo(s) separados por ;')
                        {
                            me.up().up().setLoading(true);
                            
                            if(record.get('parametros.pv_otvalor02') =="1"){
                            	nombreRDF =_0_reporteRechazoDirecto;
                            }else{
                            	nombreRDF =_0_reporteRechazoReembolso;
                            }
                            
                            Ext.Ajax.request(
                            {
                                url : _0_urlEnviarCorreo
                                ,params :
                                {
                                    to : Ext.getCmp('_0_idInputCorreos').getValue(),
                                    urlArchivo : _0_urlRutaReporte
                                                 + '?p_usuario='      + "rherdez"
                                                 + "&p_ntramite="      + record.get('ntramite')
                                                 + '&destype=cache'
                                                 + "&desformat=PDF"
                                                 + "&userid="         + _0_reportsServerUser
                                                 + "&ACCESSIBLE=YES"
                                                 + "&report="         + nombreRDF
                                                 + "&paramform=no",
                                    nombreArchivo : 'Rechazo_'+Ext.Date.format(new Date(),'Y-d-m_g_i_s_u')+'.pdf',
                                    asunto:'Rechazo Contra-Recibo',
                                    mensaje :'Estimado(a) cliente,anexamos a este e-mail el rechazo del contrarecibo de su(s) factura(s)  y nos ponemos a sus apreciables órdenes.'
                                },
                                callback : function(options,success,response)
                                {
                                    me.up().up().setLoading(false);
                                    if (success)
                                    {
                                        var json = Ext.decode(response.responseText);
                                        if (json.success == true)
                                        {
                                            me.up().up().destroy();
                                            Ext.Msg.show(
                                            {
                                                title : 'Correo enviado'
                                                ,msg : 'El correo ha sido enviado'
                                                ,buttons : Ext.Msg.OK
                                            });
                                        }
                                        else
                                        {
                                            mensajeError('Error al enviar');
                                        }
                                    }
                                    else
                                    {
                                        errorComunicacion();
                                    }
                                }
                            });
                        }
                        else
                        {
                            mensajeWarning('Introduzca al menos un correo');
                        }
                    }
                }
                ,
                {
                    text     : 'Cancelar'
                    ,icon    : _CONTEXT+'/resources/fam3icons/icons/cancel.png'
                    ,handler : function()
                    {
                        this.up().up().destroy();
                    }
                }
            ]
        }).show();
        Ext.getCmp('_0_idInputCorreos').focus();
    }
	
	function turnarAreclamaciones(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		
		Ext.Ajax.request({
			url: _UrlValidaDocumentosCargados,
			params: {
				'params.PV_NTRAMITE_I' : record.get('ntramite'),
				'params.PV_CDRAMO_I'   : record.get('cdramo'),
				'params.PV_cdtippag_I' : record.get('parametros.pv_otvalor02'),
				'params.PV_CDTIPATE_I' : record.get('parametros.pv_otvalor07')
			},
			success: function(response, opt) {
				var jsonRes=Ext.decode(response.responseText);

				if(jsonRes.success == true){
					Ext.Ajax.request({
						url: _URL_VALIDA_FACTURAASEGURADO
						,params	:	{
							'smap.ntramite' : record.get('ntramite'),
							'smap.tipoPago' : record.get('parametros.pv_otvalor02')
						}
						,success : function (response){
							var jsonRes=Ext.decode(response.responseText);
							if(jsonRes.loadList[0].faltaAsegurados =="0"){
								var comentariosText = Ext.create('Ext.form.field.TextArea', {
									fieldLabel: 'Observaciones'
									,labelWidth: 150
									,width: 600
									,name:'smap1.comments'
									,height: 250
									,allowBlank : false
								});
								
								windowLoader = Ext.create('Ext.window.Window',{
									modal       : true,
									buttonAlign : 'center',
									width       : 663,
									height      : 430,
									autoScroll  : true,
									items       : [
													Ext.create('Ext.form.Panel', {
													title: 'Turnar a Coordinador de Reclamaciones',
													width: 650,
													url: _URL_ActualizaStatusTramite,
													bodyPadding: 5,
													items: [comentariosText,{
										                xtype       : 'radiogroup'
										                ,fieldLabel : 'Mostrar al agente'
										                ,columns    : 2
										                ,width      : 250
										                ,style      : 'margin:5px;'
										                ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
										                ,items      :
										                [
										                    {
										                        boxLabel    : 'Si'
										                        ,itemId     : 'SWAGENTE2'
										                        ,name       : 'SWAGENTE2'
										                        ,inputValue : 'S'
										                        ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
										                    }
										                    ,{
										                        boxLabel    : 'No'
										                        ,name       : 'SWAGENTE2'
										                        ,inputValue : 'N'
                                                                ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
										                    }
										                ]
										            }],
													buttonAlign:'center',
													buttons: [{
														text: 'Turnar'
														,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
														,buttonAlign : 'center',
														handler: function() {
															var formPanel = this.up().up();
															if (formPanel.form.isValid()) {
																
																if(record.get('parametros.pv_otvalor02') == _REEMBOLSO || record.get('parametros.pv_otvalor02') == _INDEMNIZACION){
																	Ext.Ajax.request({
																		url: _UrlGeneraSiniestroTramite,
																		params: {
																			'params.pv_ntramite_i' : record.get('ntramite')
																		},
																		success: function(response, opt) {
																			
																			var jsonRes=Ext.decode(response.responseText);

																			if(jsonRes.success == true){
																				formPanel.form.submit({
																					waitMsg:'Procesando...',
																					params: {
																						'smap1.ntramite' : record.get('ntramite'), 
																						'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
																						,'smap1.swagente' : _fieldById('SWAGENTE2').getGroupValue()
																					},
																					failure: function(form, action)
																					{
																						debug(action);
																						switch (action.failureType)
																						{
																							case Ext.form.action.Action.CONNECT_FAILURE:
																								errorComunicacion();
																								break;
																							case Ext.form.action.Action.SERVER_INVALID:
																								mensajeError(action.result.mensaje);
																								break;
																						}
																					},
																					success: function(form, action) {
																						Ext.Ajax.request(
																						{
																							url: _URL_TurnarAOperadorReclamacion,
																							params: {
																									'smap1.ntramite' : record.get('ntramite'), 
																									'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
																									,'smap1.rol_destino'     : 'operadorsini'
																									,'smap1.usuario_destino' : ''
																							},
																							success:function(response,opts){
																								Ext.Ajax.request(
																				    	        {
																				    	            url     : _URL_NOMBRE_TURNADO
																				    	            ,params : 
																				    	            {           
																				    	                'params.ntramite': record.get('ntramite'),
																				    	                'params.rolDestino': 'operadorsini'
																				    	            }
																				    	            ,success : function (response)
																				    	            {
																				    	                if(record.get('cdramo') =="11"){
    																				    	                var usuarioTurnadoSiniestro1 = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
    																				    	                ///mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
    														        	            						debug("Usuario turnado1 ==>"+usuarioTurnadoSiniestro1);
    																				    	                var fields = usuarioTurnadoSiniestro1.split('|');
                                                                                                            var Nombre = fields[0];
                                                                                                            var usuario = fields[1];
    																				    	                
                                                                                                            Ext.Ajax.request(
                                                                                                            {
                                                                                                                url: _URL_ActualizaStatusTramite,
                                                                                                                params: {
                                                                                                                        'smap1.ntramite' : record.get('ntramite'), 
                                                                                                                        'smap1.status'   : _STATUS_TRAMITE_EN_REVISION_MEDICA
                                                                                                                        ,'smap1.rol_destino'     : 'medajustador'
                                                                                                                        ,'smap1.usuario_destino' : ''
                                                                                                                        ,'smap1.rol_inicial'     : 'OPERADORSINI'
                                                                                                                        ,'smap1.usuario_inicial' :  usuario 
                                                                                                                },
                                                                                                                success:function(response,opts){
                                                                                                                    Ext.Ajax.request(
                                                                                                                    {
                                                                                                                        url     : _URL_NOMBRE_TURNADO
                                                                                                                        ,params : 
                                                                                                                        {           
                                                                                                                            'params.ntramite': record.get('ntramite'),
                                                                                                                            'params.rolDestino': 'medajustador'
                                                                                                                        }
                                                                                                                        ,success : function (response)
                                                                                                                        {
                                                                                                                            var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
                                                                                                                            debug("Usuario turnado2 ==>"+usuarioTurnadoSiniestro);
                                                                                                                            mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
                                                                                                                            loadMcdinStore();
                                                                                                                            windowLoader.close();
                                                                                                                            
                                                                                                                        },
                                                                                                                        failure : function ()
                                                                                                                        {
                                                                                                                            me.up().up().setLoading(false);
                                                                                                                            centrarVentanaInterna(Ext.Msg.show({
                                                                                                                                title:'Error',
                                                                                                                                msg: 'Error de comunicaci&oacute;n',
                                                                                                                                buttons: Ext.Msg.OK,
                                                                                                                                icon: Ext.Msg.ERROR
                                                                                                                            }));
                                                                                                                        }
                                                                                                                    });
                                                                                                                },
                                                                                                                failure:function(response,opts)
                                                                                                                {
                                                                                                                    Ext.Msg.show({
                                                                                                                        title:'Error',
                                                                                                                        msg: 'Error de comunicaci&oacute;n',
                                                                                                                        buttons: Ext.Msg.OK,
                                                                                                                        icon: Ext.Msg.ERROR
                                                                                                                    });
                                                                                                                }
                                                                                                            });
																				    	                }else{
																				    	                	var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																				    	                	mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
														        	            							loadMcdinStore();
														        	            							windowLoader.close();
																				    	                }
																				    	            },
																				    	            failure : function ()
																				    	            {
																				    	                me.up().up().setLoading(false);
																				    	                centrarVentanaInterna(Ext.Msg.show({
																				    	                    title:'Error',
																				    	                    msg: 'Error de comunicaci&oacute;n',
																				    	                    buttons: Ext.Msg.OK,
																				    	                    icon: Ext.Msg.ERROR
																				    	                }));
																				    	            }
																				    	        });
																							},
																							failure:function(response,opts)
																							{
																								Ext.Msg.show({
																									title:'Error',
																									msg: 'Error de comunicaci&oacute;n',
																									buttons: Ext.Msg.OK,
																									icon: Ext.Msg.ERROR
																								});
																							}
																						});
																					}
																				});
																			}else{
																				mensajeError('Error al generar Siniestro para Area de Reclamaciones');
																			}
																		},
																		failure: function(){
																			mensajeError('Error al generar Siniestro para Area de Reclamaciones');
																		}
																	});
																	
																}else{
																	formPanel.form.submit({
																		waitMsg:'Procesando...',
																		params: {
																			'smap1.ntramite' : record.get('ntramite'), 
																			'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
																		},
																		failure: function(form, action) {
																			debug(action);
																			switch (action.failureType)
																			{
																				case Ext.form.action.Action.CONNECT_FAILURE:
																					errorComunicacion();
																					break;
																				case Ext.form.action.Action.SERVER_INVALID:
																					mensajeError(action.result.mensaje);
																					break;
																			}
																		},
																		success: function(form, action) {
																			Ext.Ajax.request(
																			{
																				url: _URL_TurnarAOperadorReclamacion,
																				params: {
																						'smap1.ntramite' : record.get('ntramite'), 
																						'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
																						,'smap1.rol_destino'     : 'operadorsini'
																						,'smap1.usuario_destino' : ''
																				},
																				success:function(response,opts){
																					Ext.Ajax.request(
																	    	        {
																	    	            url     : _URL_NOMBRE_TURNADO
																	    	            ,params : 
																	    	            {           
																	    	                'params.ntramite': record.get('ntramite'),
																	    	                'params.rolDestino': 'operadorsini'
																	    	            }
																	    	            ,success : function (response)
																	    	            {
																	    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
																	    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
											        	            						loadMcdinStore();
											        	            						windowLoader.close();
																	    	            },
																	    	            failure : function ()
																	    	            {
																	    	                me.up().up().setLoading(false);
																	    	                centrarVentanaInterna(Ext.Msg.show({
																	    	                    title:'Error',
																	    	                    msg: 'Error de comunicaci&oacute;n',
																	    	                    buttons: Ext.Msg.OK,
																	    	                    icon: Ext.Msg.ERROR
																	    	                }));
																	    	            }
																	    	        });
																				},
																				failure:function(response,opts)
																				{
																					Ext.Msg.show({
																						title:'Error',
																						msg: 'Error de comunicaci&oacute;n',
																						buttons: Ext.Msg.OK,
																						icon: Ext.Msg.ERROR
																					});
																				}
																			});
																		}
																	});
																}
																
															} else {
																Ext.Msg.show({
																	   title: 'Aviso',
																	   msg: 'Complete la informaci&oacute;n requerida',
																	   buttons: Ext.Msg.OK,
																	   icon: Ext.Msg.WARNING
																   });
															}
														}
													},{
														text: 'Cancelar',
														icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
														buttonAlign : 'center',
														handler: function() {
															windowLoader.close();
														}
													}
													]
												})  
											]
									}).show();
								
									centrarVentana(windowLoader);
							
							}else{
								mensajeError("Falta asegurados para la factura:"+jsonRes.loadList[0].facturasFaltantes);
							}
						},
						failure: function(){
							mensajeError('Error al turnar.');
						}
					});
					}else {
						mensajeError(jsonRes.msgResult);
					}
			},
			failure: function(){
				mensajeError('Error al turnar.');
			}
		});
	        	
	}
	
	function detalleReclamacionWindow(grid,rowIndex,colIndex)
	{
		var record = grid.getStore().getAt(rowIndex);
		debug('record Valor de respuesta :',record.data);
		Ext.Ajax.request({
			url	 : _URL_INF_ASEGURADO
			,params:{
				'params.ntramite': record.get('ntramite')
			}
			,success : function (response)
			{
				if(Ext.decode(response.responseText).datosValidacion != null){
					var json = Ext.decode(response.responseText).datosValidacion[0];
					debug("Valor del Json --> ",json);
					var formapago = json.OTVALOR02;
					debug('formapago:',formapago);
					var esPagoDirecto = formapago == _PAGO_DIRECTO;
					debug('esPagoDirecto:',esPagoDirecto ? 'si' : 'no');
					
					var params = {};
					
					params['params.tipopago'] = formapago;
					params['params.ntramite'] = json.NTRAMITE;
					var conCoberYSubcober = false;
					debug('conCoberYSubcober:',conCoberYSubcober ? 'si' : 'no');
					var urlDestino;
					
					if(esPagoDirecto){
						urlDestino = _UrlDetalleSiniestroDirecto;
						debug('urlDestino_1 :',urlDestino);
						debug('params_1 :',params);
						Ext.create('Ext.form.Panel').submit({
							url             : urlDestino
							,params         : params
						    ,standardSubmit : true
						});
					}
					else{
						//AUTORIZACION ESPECIAL
						if(json.OTVALOR22 > 0 && json.NMAUTESP == 0){
							windowAutEsp = Ext.create('Ext.window.Window',{
								modal       : true,
								buttonAlign : 'center',
								title: 'Autorizaci&oacute;n Especial',
								autoScroll  : true,
								items       : [
									panelModificacion = Ext.create('Ext.form.Panel', {
										bodyPadding: 5,
										items: [
											{	xtype: 'numberfield'
												,fieldLabel: 'N&uacute;mero de autorizaci&oacute;n'
												,name		: 'txtAutEspecial'
												,allowBlank : false
											}
										],
										buttonAlign:'center',
										buttons: [
											{
												text: 'Aceptar'
												,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
												,buttonAlign : 'center',
												handler: function() { 
													if (panelModificacion.form.isValid()) {
														var datos=panelModificacion.form.getValues();
														Ext.Ajax.request({
															url     : _URL_VALIDA_AUTESPECIFICA
															,params:{
																'params.ntramite'  : json.NTRAMITE,
																'params.tipoPago'  : json.OTVALOR02,
																'params.nfactura'  : json.NFACTURA,
																'params.cdunieco'  : json.CDUNIECO,
																'params.cdramo'    : json.CDRAMO,
																'params.estado'    : json.ESTADO,
																'params.nmpoliza'  : json.NMPOLIZA,
																'params.nmsuplem'  : json.NMSUPLEM,
																'params.nmsituac'  : json.NMSITUAC,
																'params.nmautesp'  : datos.txtAutEspecial,
																'params.nmsinies'  : json.NMSINIES
																//'params.cdperson'  : json.CDPERSON,
																//'params.cdtipsit'  : json.CDTIPSIT
															}
															,success : function (response){
																debug("Valor Ext.decode(response.responseText).validacionGeneral ====>",Ext.decode(response.responseText).validacionGeneral);
																if(Ext.decode(response.responseText).validacionGeneral =="1"){
																	mensajeCorrecto('&Eacute;XITO','Se ha asociado correctamente.',function(){
																		windowAutEsp.close();
																		Ext.create('Ext.form.Panel').submit(
																		{
																			url		: _URL_MESACONTROL
																			,standardSubmit : true
																			,params         :
																			{
																				'smap1.gridTitle'      : 'Siniestros en espera'
																				,'smap2.pv_cdtiptra_i' : 16
																			}
																		});
																	});
																	
																}else{
																	//Mensaje de Error
																	mensajeError("Autorizaci&oacute;n especial no valida para este tr&aacute;mite.");
																}
															},
															failure : function (){
																me.up().up().setLoading(false);
																centrarVentanaInterna(Ext.Msg.show({
																	title:'Error',
																	msg: 'Error de comunicaci&oacute;n',
																	buttons: Ext.Msg.OK,
																	icon: Ext.Msg.ERROR
																}));
															}
														});
														
													}else {
														Ext.Msg.show({
															title: 'Aviso',
															msg: 'Complete la informaci&oacute;n requerida',
															buttons: Ext.Msg.OK,
															icon: Ext.Msg.WARNING
														});
													}
												}
											},{
												text: 'Cancelar',
												icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
												buttonAlign : 'center',
												handler: function() {
													windowAutEsp.close();
												}
											}
										]
									})  
								]
							}).show();
							centrarVentana(windowAutEsp);
							
						}else{
							// Pago diferente a Directo
							if(json.OTVALOR12 && json.OTVALOR12.length>0){
								conCoberYSubcober = true;
							}
							debug("Valor de conCoberYSubcober",conCoberYSubcober);// 
							if(conCoberYSubcober){ // true
								urlDestino = _selCobUrlAvanza;
								debug('urlDestino_2 :',urlDestino);
								debug('params_2:',params);
								Ext.create('Ext.form.Panel').submit({
									url             : urlDestino
									,params         : params
								    ,standardSubmit : true
								});
							}else{
								//PASAMOS LOS VALORES PARA SELECCIONAR LA COBERTURA Y SUBCOBERTURA
								Ext.Ajax.request( {
									url     : _URL_POLIZA_UNICA
									,params : {
										'params.cdunieco': record.get('cdunieco'),
										'params.cdramo'  : json.CDRAMO,
										'params.estado'  : json.ESTADO,
										'params.nmpoliza': json.NMPOLIZA,
										'params.cdperson': json.CDPERSON
									}
									,success : function (response){
										if(Ext.decode(response.responseText).polizaUnica != null) {
											var jsonValorAsegurado = Ext.decode(response.responseText).polizaUnica[0];
											debug("Valor de respuesta ---> : ",jsonValorAsegurado);
											
											urlDestino = _urlSeleccionCobertura;
											params['params.cdunieco']  = record.get('cdunieco');
											params['params.otvalor02'] = json.OTVALOR02;
											params['params.cdramo']    = json.CDRAMO;
											params['params.cdtipsit']  = json.CDTIPSIT;
											params['params.nmpoliza']  = json.NMPOLIZA;
											params['params.nmsituac']  = json.NMSITUAC;
											params['params.estado']    = json.ESTADO;
											params['params.periodoEspera']    = jsonValorAsegurado.diasAsegurado;
											params['params.feocurre']    = json.FEOCURRE;
											params['params.nmsuplem'] = json.NMSUPLEM;
											debug('urlDestino_4 :',urlDestino);
											debug('params_4 :',params);
											Ext.create('Ext.form.Panel').submit(
											{
												url             : urlDestino
												,params         : params
											    ,standardSubmit : true
											});
										}
									},
									failure : function (){
										me.up().up().setLoading(false);
										centrarVentanaInterna(Ext.Msg.show({
											title:'Error',
											msg: 'Error de comunicaci&oacute;n',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR
										}));
									}
								});
							}
						}
					}
				}
			},
			failure : function (){
				me.up().up().setLoading(false);
				Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				});
			}
		});
	}
	
	function turnarAareaMedica(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		 var comentariosText = Ext.create('Ext.form.field.TextArea', {
        	fieldLabel: 'Observaciones'
    		,labelWidth: 150
    		,width: 600
    		,name:'smap1.comments'
			,height: 250
			,allowBlank : false
        });
		
		windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 663,
	        height      : 430,
	        autoScroll  : true,
	        items       : [
        	        		Ext.create('Ext.form.Panel', {
        	                title: 'Turnar al Area M&eacute;dica',
        	                width: 650,
        	                url: _URL_ActualizaStatusTramite,
        	                bodyPadding: 5,
        	                items: [comentariosText,{
        	                    xtype       : 'radiogroup'
       	                        ,fieldLabel : 'Mostrar al agente'
       	                        ,columns    : 2
       	                        ,width      : 250
       	                        ,style      : 'margin:5px;'
       	                        ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
       	                        ,items      :
       	                        [
       	                            {
       	                                boxLabel    : 'Si'
       	                                ,itemId     : 'SWAGENTE3'
       	                                ,name       : 'SWAGENTE3'
       	                                ,inputValue : 'S'
       	                                ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
       	                            }
       	                            ,{
       	                                boxLabel    : 'No'
       	                                ,name       : 'SWAGENTE3'
       	                                ,inputValue : 'N'
                                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
       	                            }
       	                        ]
       	                    }],
        	        	    buttonAlign:'center',
        	        	    buttons: [{
        	            		text: 'Turnar'
        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
        	            		,buttonAlign : 'center',
        	            		handler: function() {
        	            	    	if (this.up().up().form.isValid()) {
        	            	    		this.up().up().form.submit({
        	            		        	waitMsg:'Procesando...',
        	            		        	params: {
        	            		        		'smap1.ntramite' : record.get('ntramite'), 
        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_REVISION_MEDICA
        	            		        		,'smap1.rol_destino'     : 'medajustador'
        	            		        		,'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
        	            		        		,'smap1.swagente' : _fieldById('SWAGENTE3').getGroupValue()
        	            		        	},
        	            		        	failure: function(form, action) {
        	            		        		debug(action);
        	            		        		switch (action.failureType)
                                                {
                                                    case Ext.form.action.Action.CONNECT_FAILURE:
                                                        errorComunicacion();
                                                        break;
                                                    case Ext.form.action.Action.SERVER_INVALID:
                                                    	mensajeError(action.result.mensaje);
                                                    	break;
                                                }
        	            		        		//mensajeError('No se pudo turnar.');
        	            					},
        	            					success: function(form, action) {
        	            						Ext.Ajax.request(
								    	        {
								    	            url     : _URL_NOMBRE_TURNADO
								    	            ,params : 
								    	            {           
								    	                'params.ntramite': record.get('ntramite'),
								    	                'params.rolDestino': 'medajustador'
								    	            }
								    	            ,success : function (response)
								    	            {
								    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
								    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
		        	            						loadMcdinStore();
		        	            						windowLoader.close();
								    	            },
								    	            failure : function ()
								    	            {
								    	                me.up().up().setLoading(false);
								    	                centrarVentanaInterna(Ext.Msg.show({
								    	                    title:'Error',
								    	                    msg: 'Error de comunicaci&oacute;n',
								    	                    buttons: Ext.Msg.OK,
								    	                    icon: Ext.Msg.ERROR
								    	                }));
								    	            }
								    	        });
        	            					}
        	            				});
        	            			} else {
        	            				Ext.Msg.show({
        	            	                   title: 'Aviso',
        	            	                   msg: 'Complete la informaci&oacute;n requerida',
        	            	                   buttons: Ext.Msg.OK,
        	            	                   icon: Ext.Msg.WARNING
        	            	               });
        	            			}
        	            		}
        	            	},{
        	            	    text: 'Cancelar',
        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
        	            	    buttonAlign : 'center',
        	            	    handler: function() {
        	            	        windowLoader.close();
        	            	    }
        	            	}
        	            	]
        	            })  
	            	]
	    }).show();
		centrarVentana(windowLoader);
	}
	
	function turnarAreclamacionesMedAjustador(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url     : _URL_P_MOV_MAUTSINI
			,params : {
				'params.ntramite': record.get('ntramite')
			}
			,success : function (response) {
				var respuestaMensaje = Ext.decode(response.responseText).mensaje;
				Ext.Ajax.request({
					url: _UrlValidaDocumentosCargados,
					params: {
						'params.PV_NTRAMITE_I' : record.get('ntramite'),
						'params.PV_CDRAMO_I'   : record.get('cdramo'),
						'params.PV_cdtippag_I' : record.get('parametros.pv_otvalor02'),
						'params.PV_CDTIPATE_I' : record.get('parametros.pv_otvalor07')
					},
					success: function(response, opt) {
						var jsonRes=Ext.decode(response.responseText);

						if(jsonRes.success == true){
							var comentariosText = Ext.create('Ext.form.field.TextArea', {
			                	fieldLabel: 'Observaciones'
			            		,labelWidth: 150
			            		,width: 600
			            		,name:'smap1.comments'
			            		, value : respuestaMensaje
			        			,height: 250
			        			,allowBlank : false
			                });
			        		
			        		windowLoader = Ext.create('Ext.window.Window',{
			        	        modal       : true,
			        	        buttonAlign : 'center',
			        	        width       : 663,
			        	        height      : 430,
			        	        autoScroll  : true,
			        	        items       : [
					        	        		Ext.create('Ext.form.Panel', {
					        	                title: 'Turnar a Coordinador de Reclamaciones',
					        	                width: 650,
					        	                url: _URL_ActualizaStatusTramite,
					        	                bodyPadding: 5,
					        	                items: [comentariosText,{
					        	                    xtype       : 'radiogroup'
				        	                        ,fieldLabel : 'Mostrar al agente'
				        	                        ,columns    : 2
				        	                        ,width      : 250
				        	                        ,style      : 'margin:5px;'
				        	                        ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
				        	                        ,items      :
				        	                        [
				        	                            {
				        	                                boxLabel    : 'Si'
				        	                                ,itemId     : 'SWAGENTE4'
				        	                                ,name       : 'SWAGENTE4'
				        	                                ,inputValue : 'S'
				        	                                ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
				        	                            }
				        	                            ,{
				        	                                boxLabel    : 'No'
				        	                                ,name       : 'SWAGENTE4'
				        	                                ,inputValue : 'N'
                                                            ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
				        	                            }
				        	                        ]
				        	                    }],
					        	        	    buttonAlign:'center',
					        	        	    buttons: [{
					        	            		text: 'Turnar'
					        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
					        	            		,buttonAlign : 'center',
					        	            		handler: function() {
					        	            			var formPanel = this.up().up();
					        	            	    	if (formPanel.form.isValid()) {
					        	            	    		formPanel.form.submit({
									        	            		        	waitMsg:'Procesando...',
									        	            		        	params: {
									        	            		        		'smap1.ntramite' : record.get('ntramite'), 
									        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_ESPERA_DE_ASIGNACION
									        	            		        		,'smap1.swagente' : _fieldById('SWAGENTE4').getGroupValue()
									        	            		        	},
									        	            		        	failure: function(form, action)
									        	            		        	{
									        	            		        		debug(action);
									        	            		        		switch (action.failureType)
									        	            		        		{
									        	            		        		    case Ext.form.action.Action.CONNECT_FAILURE:
									        	            		        		    	errorComunicacion();
										        	            		                    break;
										        	            		                case Ext.form.action.Action.SERVER_INVALID:
										        	            		                	mensajeError(action.result.mensaje);
										        	            		                	break;
										        	            		            }
									        	            		        		//mensajeError('No se pudo turnar.');
									        	            					},
									        	            					success: function(form, action) {
									        	            						mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito.');
									        	            						loadMcdinStore();
									        	            						windowLoader.close();
									        	            						
									        	            					}
								        	            					});
					        	            			} else {
					        	            				Ext.Msg.show({
					        	            	                   title: 'Aviso',
					        	            	                   msg: 'Complete la informaci&oacute;n requerida',
					        	            	                   buttons: Ext.Msg.OK,
					        	            	                   icon: Ext.Msg.WARNING
					        	            	               });
					        	            			}
					        	            		}
					        	            	},{
					        	            	    text: 'Cancelar',
					        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
					        	            	    buttonAlign : 'center',
					        	            	    handler: function() {
					        	            	        windowLoader.close();
					        	            	    }
					        	            	}
					        	            	]
					        	            })  
			        	            	]
			        	    	}).show();
			        		
			        			centrarVentana(windowLoader);
							}else {
								mensajeError(jsonRes.msgResult);
							}
					},
					failure: function(){
						mensajeError('Error al turnar.');
					}
				});
			},
			failure : function (){
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
	}
	
	
	
	function turnarAoperadorReclamaciones(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		var comentariosText = Ext.create('Ext.form.field.TextArea', {
        	fieldLabel: 'Observaciones'
    		,labelWidth: 150
    		,width: 600
    		,name:'smap1.comments'
			,height: 250
			,allowBlank : false
        });
		
		windowLoader = Ext.create('Ext.window.Window',{
	        modal       : true,
	        buttonAlign : 'center',
	        width       : 663,
	        height      : 430,
	        autoScroll  : true,
	        items       : [
        	        		Ext.create('Ext.form.Panel', {
        	                title: 'Turnar a Operador de Reclamaciones',
        	                width: 650,
        	                url: _URL_ActualizaStatusTramite,
        	                bodyPadding: 5,
        	                items: [comentariosText,{
        	                    xtype       : 'radiogroup'
       	                        ,fieldLabel : 'Mostrar al agente'
       	                        ,columns    : 2
       	                        ,width      : 250
       	                        ,style      : 'margin:5px;'
       	                        ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
       	                        ,items      :
       	                        [
       	                            {
       	                                boxLabel    : 'Si'
       	                                ,itemId     : 'SWAGENTE5'
       	                                ,name       : 'SWAGENTE5'
       	                                ,inputValue : 'S'
       	                                ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
       	                            }
       	                            ,{
       	                                boxLabel    : 'No'
       	                                ,name       : 'SWAGENTE5'
       	                                ,inputValue : 'N'
                                        ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
       	                            }
       	                        ]
       	                    }],
        	        	    buttonAlign:'center',
        	        	    buttons: [{
        	            		text: 'Turnar'
        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
        	            		,buttonAlign : 'center',
        	            		handler: function() {
        	            	    	if (this.up().up().form.isValid()) {
        	            	    		this.up().up().form.submit({
        	            		        	waitMsg:'Procesando...',
        	            		        	params: {
        	            		        		'smap1.ntramite' : record.get('ntramite'), 
        	            		        		'smap1.status'   : _STATUS_TRAMITE_EN_CAPTURA
        	            		        		,'smap1.rol_destino'     : 'operadorsini'
                                                ,'smap1.usuario_destino' : colIndex.length>3 ? colIndex : ''
                                                ,'smap1.swagente' : _fieldById('SWAGENTE5').getGroupValue()
        	            		        	},
        	            		        	failure: function(form, action) {
        	            		        		debug(action);
        	            		        		switch (action.failureType)
                                                {
                                                    case Ext.form.action.Action.CONNECT_FAILURE:
                                                        errorComunicacion();
                                                        break;
                                                    case Ext.form.action.Action.SERVER_INVALID:
                                                    	mensajeError(action.result.mensaje);
                                                    	break;
                                                }
        	            		        		//mensajeError('No se pudo turnar.');
        	            					},
        	            					success: function(form, action) {
        	            						Ext.Ajax.request(
   								    	        {
   								    	            url     : _URL_NOMBRE_TURNADO
   								    	            ,params : 
   								    	            {           
   								    	                'params.ntramite': record.get('ntramite'),
   								    	                'params.rolDestino': 'operadorsini'
   								    	            }
   								    	            ,success : function (response)
   								    	            {
   								    	                var usuarioTurnadoSiniestro = Ext.decode(response.responseText).usuarioTurnadoSiniestro;
   								    	                mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito a: '+usuarioTurnadoSiniestro);
   		        	            						loadMcdinStore();
   		        	            						windowLoader.close();
   								    	            },
   								    	            failure : function ()
   								    	            {
   								    	                me.up().up().setLoading(false);
   								    	                centrarVentanaInterna(Ext.Msg.show({
   								    	                    title:'Error',
   								    	                    msg: 'Error de comunicaci&oacute;n',
   								    	                    buttons: Ext.Msg.OK,
   								    	                    icon: Ext.Msg.ERROR
   								    	                }));
   								    	            }
   								    	        });
        	            						
        	            					}
        	            				});
        	            			} else {
        	            				Ext.Msg.show({
        	            	                   title: 'Aviso',
        	            	                   msg: 'Complete la informaci&oacute;n requerida',
        	            	                   buttons: Ext.Msg.OK,
        	            	                   icon: Ext.Msg.WARNING
        	            	               });
        	            			}
        	            		}
        	            	},{
        	            	    text: 'Cancelar',
        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
        	            	    buttonAlign : 'center',
        	            	    handler: function() {
        	            	        windowLoader.close();
        	            	    }
        	            	}
        	            	]
        	            })  
	            	]
	    }).show();
		
		centrarVentana(windowLoader);
	}
	
	function solicitarPago(grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		debug("Valor del record :",record);
		if(record.get('status') == _STATUS_TRAMITE_CONFIRMADO){
			mensajeWarning('Ya se ha solicitado el pago para este tr&aacute;mite.');	
			return;
		}else{
			Ext.Ajax.request({
				url	 : _URL_EXISTE_COBERTURA
				,params:{
					'params.ntramite'  : record.get('ntramite'),
					'params.tipoPago'  : record.get('parametros.pv_otvalor02')
				}
				,success : function (response) {
					//Obtenemos los datos
					var valCobertura = Ext.decode(response.responseText).datosValidacion;
					var i = 0;
					var banderaExisteCobertura = 0;
					var resultCobertura= "";
					if(valCobertura.length > 0){
						//Mostramos el mensaje de Error y no podra continuar
						debug("Valor de Respuesta ===>",valCobertura.length);
						for(var i = 0; i < valCobertura.length; i++){
							banderaExisteCobertura = "1";
							resultCobertura = resultCobertura + 'La Factura ' + valCobertura[i].NFACTURA + ' del siniestro '+ valCobertura[i].NMSINIES+ ' requiere actualizar la cobertura no amparada. <br/>';
						}
						if(banderaExisteCobertura == "1"){
							centrarVentanaInterna(mensajeWarning(resultCobertura));
						}
					}else{
						//validamos los montos 
						Ext.Ajax.request({
							url	: _URL_MONTO_PAGO_SINIESTRO
							,params:{
								'params.ntramite' : record.get('ntramite'),
								'params.cdramo'   : record.get('cdramo'),
								'params.tipoPago' : record.get('parametros.pv_otvalor02')
							}
							,success : function (response){
								var jsonRespuesta =Ext.decode(response.responseText);//.datosInformacionAdicional[0];
								debug("Valor de Respuesta", jsonRespuesta);
								
								if(jsonRespuesta.success == true){
									if( record.get('parametros.pv_otvalor02') ==_PAGO_DIRECTO){
										//mostrarSolicitudPago(grid,rowIndex,colIndex); --------------->
										_11_validaProveedorPagoDirecto(grid,rowIndex,colIndex); // (EGS) validamos solo un proveedor en reclamo pago directo
										//_11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex); // (EGS) se comenta aquí pero se agrega en funcion _11_validaProveedorPagoDirecto()
									}else{
										Ext.Ajax.request({
											url	 : _URL_VAL_AJUSTADOR_MEDICO
											,params:{
												'params.ntramite': record.get('ntramite')
											}
											,success : function (response)
											{
												if(Ext.decode(response.responseText).datosValidacion != null){
													var autAM = null;
													var result ="";
													banderaValidacion = "0";
													var json = Ext.decode(response.responseText).datosValidacion;
													if(json.length > 0){
														for(var i = 0; i < json.length; i++){
															if(json[i].AREAAUTO =="ME"){
																var valorValidacion = json[i].SWAUTORI+"";
																if(valorValidacion == null || valorValidacion == ''|| valorValidacion == 'null'){
																	banderaValidacion = "1";
																	result = result + 'El m&eacute;dico no autoriza la factura ' + json[i].NFACTURA + '<br/>';
																}
															}
														}
														if(banderaValidacion == "1"){
															centrarVentanaInterna(mensajeWarning(result));
														}else{
															//mostrarSolicitudPago(grid,rowIndex,colIndex);--------------->
															_11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex);
														}
													}else{
														centrarVentanaInterna(mensajeWarning('El m&eacute;dico no ha autizado la factura'));
													}
												}
											},
											failure : function (){
												me.up().up().setLoading(false);
												Ext.Msg.show({
													title:'Error',
													msg: 'Error de comunicaci&oacute;n',
													buttons: Ext.Msg.OK,
													icon: Ext.Msg.ERROR
												});
											}
										});
									}
								}else {
									centrarVentanaInterna(mensajeWarning(jsonRespuesta.mensaje));
								}
							},
							failure : function (){
								Ext.Msg.show({
									title:'Error',
									msg: 'Error de comunicaci&oacute;n',
									buttons: Ext.Msg.OK,
									icon: Ext.Msg.ERROR
								});
							}
						});
					}
				},failure : function () {
					Ext.Msg.show({
						title:'Error',
						msg: 'Error de comunicaci&oacute;n',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					});
				}
			});
		}
	}
	

	function generarComplementoSiniestro (grid,rowIndex,colIndex){
		var record = grid.getStore().getAt(rowIndex);
		debug("VALOR DEL RECORD -->>>",record);
		debug("VALOR DEL record.get('status')");
		debug(record.get('status'));
		if(record.get('status') != _STATUS_TRAMITE_CONFIRMADO){
		mensajeWarning('El tr&aacute;mite debe estar confirmado.');
		return;
		}else{
			msgWindow = Ext.Msg.show({
				title: 'Aviso',
				msg: '&iquest; Deseas generar el complemento del tr&aacute;mite '+record.get('ntramite')+'?',
				buttons: Ext.Msg.YESNO,
				icon: Ext.Msg.QUESTION,
				fn: function(buttonId, text, opt){
					if(buttonId == 'yes'){
						mcdinGrid.setLoading(true);
						Ext.Ajax.request({
							url: _UrlSolicitarComplemento,
							params: {
								'params.pv_ntramite_i' : record.get('ntramite')
							},
							success: function(response, opts) {
								//mcdinGrid.setLoading(false);
								var respuesta = Ext.decode(response.responseText);
								debug("VALOR DE RESPUESTA ", respuesta);
								if(respuesta.success==true){
									mcdinGrid.setLoading(false);
									mensajeCorrecto('Aviso','Se ha generado el complemento  del tr&aacute;mite');//+respuesta.msgResult);
								}else {
									mcdinGrid.setLoading(false);
									mensajeError("Error al generar el complemento");
								}
							},
							failure: function(){
								mcdinGrid.setLoading(false);
								mensajeError('No se pudo solicitar el pago.');
							}
						});
	
					}
				}
			});
			centrarVentana(msgWindow);
		}
	}
	
	
	//Validamos si existe las Validaciones 
	function _11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		myMask.show();
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url     : _URL_VALIDA_COBASEGURADOS
			,params:{
				'params.ntramite'  : record.get('ntramite')
			}
			,success : function (response) {
				json = Ext.decode(response.responseText);
				if(json.success==false){
					myMask.hide();
					centrarVentanaInterna(mensajeWarning(json.msgResult));
				}else{
					myMask.hide();
					_11_validaImporteAseguradoTramite(grid,rowIndex,colIndex);
				}
			},
			failure : function (){
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
	}
	
	//Validamos si existe las Validaciones 
	function _11_validaImporteAseguradoTramite(grid,rowIndex,colIndex){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"loading..."});
		myMask.show();
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url     : _URL_VALIDA_IMPASEGURADOSINIESTRO
			,params:{
				'params.ntramite'  : record.get('ntramite'),
				'params.tipopago'  : record.get('parametros.pv_otvalor02')
			}
			,success : function (response) {
				json = Ext.decode(response.responseText);
				if(json.success==false){
					myMask.hide();
					centrarVentanaInterna(mensajeWarning(json.msgResult));
				}else{
					myMask.hide();
					mostrarSolicitudPago(grid,rowIndex,colIndex);
				}
			},
			failure : function (){
				centrarVentanaInterna(Ext.Msg.show({
					title:'Error',
					msg: 'Error de comunicaci&oacute;n',
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
	}
	
	
	
	function mostrarSolicitudPago(grid,rowIndex,colIndex){
		storeDestinoPago = Ext.create('Ext.data.JsonStore', {
		    model:'Generic',
	        proxy:
	        {
	            type: 'ajax',
	            url: _URL_CATALOGOS,
	            extraParams : {catalogo:_CAT_DESTINOPAGO},
	            reader:
	            {
	                type: 'json',
	                root: 'lista'
	            }
	        }
	    });
		storeDestinoPago.load();
		
		storeConceptoPago = Ext.create('Ext.data.JsonStore', {
			model:'Generic',
			autoLoad:true,
			proxy: {
				type: 'ajax',
				url: _URL_CATALOGOS,
				extraParams : {catalogo:_CATALOGO_CONCEPTOPAGO},
				reader: {
					type: 'json',
					root: 'lista'
				}
			}
		});
		
		storeAsegurados2 = Ext.create('Ext.data.Store', {
			model:'Generic',
			autoLoad:false,
			proxy: {
				type: 'ajax',
				url : _URL_LISTADO_ASEGURADO_POLIZA,
				reader: {
					type: 'json',
					root: 'listaAsegurado'
				}
			}
		});
		
		//1.- Validamos que no existe 
		msgWindow = Ext.Msg.show({
	        title: 'Aviso',
	        msg: '&iquest;Esta seguro que desea solicitar el pago?',
	        buttons: Ext.Msg.YESNO,
	        icon: Ext.Msg.QUESTION,
	        fn: function(buttonId, text, opt){
	        	if(buttonId == 'yes'){
	        		var record = grid.getStore().getAt(rowIndex);
	        		var recordAdicional = grid.getStore().getAt(rowIndex);
	        		
	        		storeConceptoPago.load({
						params : {
							'params.cdramo': record.get('cdramo')
						}
					});
	        		
					storeAsegurados2.load({
						params:{
							'params.cdunieco': record.raw.cdunieco,
							'params.cdramo': record.raw.cdramo,
							'params.estado': record.raw.estado,
							'params.nmpoliza': record.raw.nmpoliza
						}
					});
	        		
	        		var pagocheque = Ext.create('Ext.form.field.ComboBox',
   		    	    {
   		    	        colspan	   :2,				fieldLabel   	: 'Destino Pago', 	name			:'destinoPago',
   		    	        allowBlank : false,			editable     	: true,			displayField    : 'value',
   		    	        valueField:'key',			forceSelection  : true,			width			:350,
   		    	        queryMode    :'local',		store 			: storeDestinoPago
   		    	    });
   		    		
   		    		var concepPago = Ext.create('Ext.form.field.ComboBox',
   		    	    {
   		    	        colspan	   :2,				fieldLabel   	: 'Concepto Pago', 	name			:'concepPago',
   		    	        allowBlank : false,			editable     	: true,			displayField    : 'value',
   		    	        valueField:'key',			forceSelection  : true,			width			:350,
   		    	        queryMode    :'local',		store 			: storeConceptoPago
   		    	    });
	        		
   		    	    var idCveBeneficiario = Ext.create('Ext.form.field.Number',
					{
						colspan	   :2,				fieldLabel   	: 'Id. Beneficiario', 	name			:'idCveBeneficiario',
						allowBlank : false,			editable     	: true,				width			:350
					});
						
   		    		var cmbBeneficiario= Ext.create('Ext.form.ComboBox',{
						name:'cmbBeneficiario',			fieldLabel: 'Beneficiario',			queryMode: 'local'/*'remote'*/,			displayField: 'value',
						valueField: 'key',				editable:true,						forceSelection : true,		matchFieldWidth: false,
						queryParam: 'params.cdperson',	minChars  : 2, 						store : storeAsegurados2,	triggerAction: 'all',
						width		 : 350,
						allowBlank: record.get('parametros.pv_otvalor02') == _PAGO_DIRECTO,
						hidden : record.get('parametros.pv_otvalor02') == _PAGO_DIRECTO,
						listeners : {
							'select' : function(e) {
								Ext.Ajax.request({
									url     : _URL_CONSULTA_BENEFICIARIO
									,params:{
										'params.cdunieco'  : record.raw.cdunieco,
										'params.cdramo'    : record.raw.cdramo,
										'params.estado'    : record.raw.estado,
										'params.nmpoliza'  : record.raw.nmpoliza,
										'params.cdperson'  : e.getValue()
									}
									,success : function (response) {
										json = Ext.decode(response.responseText);
										if(json.success==false){
											Ext.Msg.show({
												title:'Beneficiario',
												msg: json.mensaje,
												buttons: Ext.Msg.OK,
												icon: Ext.Msg.WARNING
											});
											panelModificacion.query('combo[name=cmbBeneficiario]')[0].setValue('')
										}
									},
									failure : function (){
										me.up().up().setLoading(false);
										centrarVentanaInterna(Ext.Msg.show({
											title:'Error',
											msg: 'Error de comunicaci&oacute;n',
											buttons: Ext.Msg.OK,
											icon: Ext.Msg.ERROR
										}));
									}
								});
							}
						}
					});
   		    		
					Ext.Ajax.request({
						url     : _URL_VAL_CAUSASINI
						,params : {
							'params.cdramo'   : record.raw.cdramo,
							'params.cdtipsit' : record.raw.cdtipsit,
							'params.causaSini': 'IDBENEFI',
							'params.cveCausa' : record.get('parametros.pv_otvalor02')
						}
						,success : function (response){
							var datosExtras = Ext.decode(response.responseText);
							if(Ext.decode(response.responseText).datosInformacionAdicional != null){
								var cveCauSini=Ext.decode(response.responseText).datosInformacionAdicional[0];
								debug("Valor de Respuesta ==> ",record.raw.cdramo,record.raw.cdtipsit,record.get('parametros.pv_otvalor02'),cveCauSini);
								if(cveCauSini.REQVALIDACION =="S"){
									//Visualizamos el campo
									panelModificacion.down('[name=idCveBeneficiario]').show();
								}else{
									//ocultamos el campo
									panelModificacion.down('[name=idCveBeneficiario]').setValue('0');
									panelModificacion.down('[name=idCveBeneficiario]').hide();
								}
							}
						},
						failure : function (){
							me.up().up().setLoading(false);
							centrarVentanaInterna(Ext.Msg.show({
								title:'Error',
								msg: 'Error de comunicaci&oacute;n',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.ERROR
							}));
						}
					});
					
					
   		    		var cdramoTramite="";
   		    		var cdtipsitTramite ="";
	        		//LLAMADA A LA MESA DE CONTROL PARA VERIFICAR LOS CAMPOS OTVALOR18 Y OTVALOR19
	        		Ext.Ajax.request({
	        			url     : _URL_CONSULTA_TRAMITE
	        			,params:{
	        				'params.ntramite': record.get('ntramite')
	                    }
	        			,success : function (response)
	        			{
		        			 	if(Ext.decode(response.responseText).listaMesaControl != null)
		        		    	{
		        			    	var json=Ext.decode(response.responseText).listaMesaControl[0];
		        			    	cdramoTramite = json.cdramomc;
		           		    		cdtipsitTramite = json.cdtipsitmc;
		           		    		panelModificacion.query('combo[name=cmbBeneficiario]')[0].setValue(json.otvalor04mc);
		        			    	if(json.otvalor18mc !=null)
	        			    		{
		        			    		panelModificacion.query('combo[name=destinoPago]')[0].setValue(json.otvalor18mc);
	        			    		}
		        			    	
		        			    	if(json.otvalor19mc !=null)
	        			    		{
		        			    		panelModificacion.query('combo[name=concepPago]')[0].setValue(json.otvalor19mc);
	        			    		}
	        			    		if(json.otvalor27mc !=null)
									{
										panelModificacion.query('[name=idCveBeneficiario]')[0].setValue(json.otvalor27mc);
									}
	        		    		}
	        			    },
	        			    failure : function ()
	        			    {
	        			        me.up().up().setLoading(false);
	        			        Ext.Msg.show({
	        			            title:'Error',
	        			            msg: 'Error de comunicaci&oacute;n',
	        			            buttons: Ext.Msg.OK,
	        			            icon: Ext.Msg.ERROR
	        			        });
	        			    }
	        		});
	        		
		    		
			        windowCvePago = Ext.create('Ext.window.Window',{
				        modal       : true,
				        buttonAlign : 'center',
				        width       : 550,
				        autoScroll  : true,
				        items       : [
			        	        		panelModificacion = Ext.create('Ext.form.Panel', {
			        	                title: 'Destino de Pago',
			        	                bodyPadding: 5,
			        	                items: [pagocheque,
			        	                        concepPago,
												cmbBeneficiario,
												idCveBeneficiario],
			        	        	    buttonAlign:'center',
			        	        	    buttons: [{
			        	            		text: 'Solicitar'
			        	            		,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
			        	            		,buttonAlign : 'center',
			        	            		handler: function() { 
			        	            			if (panelModificacion.form.isValid()) {
			        	            				var datos=panelModificacion.form.getValues();
			        	            				
			        	            				Ext.Ajax.request({
			        	        	        			url     : _URL_CONCEPTODESTINO
			        	        	        			,jsonData: {
			        	        	        				params:{
			        	        	        					ntramite:record.get('ntramite'),
			        	        	        					cdtipsit:cdtipsitTramite,
			        	        	        					destinoPago:datos.destinoPago,
			        	        	        					concepPago:datos.concepPago,
			        	        	        					beneficiario : datos.cmbBeneficiario,
			        	        	        					cvebeneficiario : datos.idCveBeneficiario,
																tipoPago : record.get('parametros.pv_otvalor02')
			        	        	        				}
	        	                                        }
			        	        	        			,success : function (response)
			        	        	        			{
			        	        		        			 	
			        	        	        				windowCvePago.close();
			        	        	        				mcdinGrid.setLoading(true);
			        	        	     	        		Ext.Ajax.request({
			        	        	     						url: _UrlSolicitarPago,
			        	        	     						params: {
			        	        	     				    		'params.pv_ntramite_i' : record.get('ntramite'),
			        	        	     				    		'params.pv_tipmov_i'   : record.get('parametros.pv_otvalor02')
			        	        	     				    	},
			        	        	     						success: function(response, opts) {
			        	        	     							mcdinGrid.setLoading(false);
			        	        	     							var respuesta = Ext.decode(response.responseText);
			        	        	     							if(respuesta.success){
			        	        	     								mensajeCorrecto('Aviso','El pago se ha solicitado con &eacute;xito.');	
			        	        	     							}else {
			        	        	     								mensajeError(respuesta.mensaje);
			        	        	     							}
			        	        	     							
			        	        	     						},
			        	        	     						failure: function(){
			        	        	     							mcdinGrid.setLoading(false);
			        	        	     							mensajeError('No se pudo solicitar el pago.');
			        	        	     						}
			        	        	     					});
			        	        	        			    },
			        	        	        			    failure : function ()
			        	        	        			    {
			        	        	        			        me.up().up().setLoading(false);
			        	        	        			        Ext.Msg.show({
			        	        	        			            title:'Error',
			        	        	        			            msg: 'Error de comunicaci&oacute;n',
			        	        	        			            buttons: Ext.Msg.OK,
			        	        	        			            icon: Ext.Msg.ERROR
			        	        	        			        });
			        	        	        			    }
			        	        	        		});
			        	            			}else {
			        	                            Ext.Msg.show({
			        	                                   title: 'Aviso',
			        	                                   msg: 'Complete la informaci&oacute;n requerida',
			        	                                   buttons: Ext.Msg.OK,
			        	                                   icon: Ext.Msg.WARNING
			        	                               });
			        	                        }
			        	            		}
			        	            	},{
			        	            	    text: 'Cancelar',
			        	            	    icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
			        	            	    buttonAlign : 'center',
			        	            	    handler: function() {
			        	            	        windowCvePago.close();
			        	            	    }
			        	            	}
			        	            	]
			        	            })  
				            	]
				    }).show();
					centrarVentana(windowCvePago);

	        	}
	        	
	        }
	    });
		centrarVentana(msgWindow);
	}

	
	function _4_onClockClick(grid,rowIndex)
	{
	    var record=grid.getStore().getAt(rowIndex);
	    debug(record);
	    var window=Ext.create('Ext.window.Window',
	    {
	        title        : 'Detalles del tr&aacute;mite '+record.get('ntramite')
	        ,modal       : true
	        ,buttonAlign : 'center'
	        ,width       : 700
	        ,height      : 400
	        
	        ,items       :
	        [
	            Ext.create('Ext.grid.Panel',
	            {
	                height      : 190
	                ,autoScroll : true
	                ,store      : new Ext.data.Store(
	                {
	                    model     : 'DetalleMC'
	                    ,autoLoad : true
	                    ,proxy    :
	                    {
	                        type         : 'ajax'
	                        ,url         : mesConUrlDetMC
	                        ,extraParams :
	                        {
	                            'smap1.pv_ntramite_i' : record.get('ntramite')
	                        }
	                        ,reader      :
	                        {
	                            type  : 'json'
	                            ,root : 'slist1'
	                        }
	                    }
	                })
	                ,columns : 
	                [
	                    {
	                        header     : 'Tr&aacute;mite'
	                        ,dataIndex : 'NTRAMITE'
	                        ,width     : 60
	                    }
	                    ,{
	                        header     : 'No.'
	                        ,dataIndex : 'NMORDINA'
	                        ,width     : 40
	                    }
	                    ,{
	                        header     : 'Fecha de inicio'
	                        ,xtype     : 'datecolumn'
	                        ,dataIndex : 'FECHAINI'
	                        ,format    : 'd M Y H:i'
	                        ,width     : 130
	                    }
	                    ,{
	                        header     : 'Usuario inicio'
	                        ,dataIndex : 'usuario_ini'
	                        ,width     : 150
	                    }
	                    ,{
	                        header     : 'Fecha de fin'
	                        ,xtype     : 'datecolumn'
	                        ,dataIndex : 'FECHAFIN'
	                        ,format    : 'd M Y H:i'
	                        ,width     : 90
	                    }
	                    ,{
	                        header     : 'Usuario fin'
	                        ,dataIndex : 'usuario_fin'
	                        ,width     : 150
	                    }
	                    ,{
	                        width         : 30
	                        ,menuDisabled : true
	                        ,dataIndex    : 'FECHAFIN'
	                        ,renderer     : function(value)
	                        {
	                            debug(value);
	                            if(value&&value!=null)
	                            {
	                                value='';
	                            }
	                            else
	                            {
	                                value='<img src="${ctx}/resources/fam3icons/icons/accept.png" style="cursor:pointer;" data-qtip="Finalizar" />';
	                            }
	                            return value;
	                        }
	                    }
	                    /*,{
	                        width         : 30
	                        ,menuDisabled : true
	                        ,dataIndex    : 'CDCLAUSU'
	                        ,renderer     : function(value)
	                        {
	                            debug(value);
	                            if(value&&value!=null&&value.length>0)
	                            {
	                                value='<img src="${ctx}/resources/fam3icons/icons/printer.png" style="cursor:pointer;" data-qtip="Imprimir" />';
	                            }
	                            else
	                            {
	                                value='';
	                            }
	                            return value;
	                        }
	                    }*/
	                ]
	                ,listeners :
	                {
	                    cellclick : function(grid, td,
	                            cellIndex, record, tr,
	                            rowIndex, e, eOpts)
	                    {
	                        debug(record);
	                        if(cellIndex<6)
	                        {
	                            Ext.getCmp('inputReadDetalleHtmlVisor').setValue((_4_smap1.cdsisrol!='EJECUTIVOCUENTA'||record.raw.SWAGENTE=='S')?record.get('COMMENTS'):'');
	                        }
	                        else if(cellIndex==6&&$(td).find('img').length>0)
	                        {
	                            debug('finalizar');
	                            Ext.create('Ext.window.Window',
	                            {
	                                title        : 'Finalizar detalle'
	                                ,width       : 600
	                                ,height      : 400
	                                ,buttonAlign : 'center'
	                                ,modal       : true
	                                ,closable    : false
	                                ,autoScroll  : true
	                                ,items       :
	                                [
	                                    Ext.create('Ext.form.HtmlEditor', {
	                                        id      : 'inputHtmlEditorFinalizarDetalleMesCon'
	                                        ,width  : 570
	                                        ,height : 300
	                                        ,value  : record.get('COMMENTS')
	                                    })
	                                ]
	                                ,buttons     :
	                                [
	                                    {
	                                        text     : 'Guardar'
	                                        ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
	                                        ,handler : function()
	                                        {
	                                            var win=this.up().up();
	                                            win.setLoading(true);
	                                            Ext.Ajax.request
	                                            ({
	                                                url      : mesConUrlFinDetalleMC
	                                                ,params  :
	                                                {
	                                                    'smap1.pv_ntramite_i'  : record.get('NTRAMITE')
	                                                    ,'smap1.pv_nmordina_i' : record.get('NMORDINA')
	                                                    ,'smap1.pv_comments_i' : Ext.getCmp('inputHtmlEditorFinalizarDetalleMesCon').getValue()
	                                                }
	                                                ,success : function (response)
	                                                {
	                                                    var json=Ext.decode(response.responseText);
	                                                    if(json.success==true)
	                                                    {
	                                                        win.destroy();
	                                                        window.destroy();
	                                                        Ext.Msg.show({
	                                                            title:'Detalle actualizado',
	                                                            msg: 'Se finaliz&oacute; el detalle',
	                                                            buttons: Ext.Msg.OK
	                                                        });
	                                                    }
	                                                    else
	                                                    {
	                                                        win.setLoading(false);
	                                                        Ext.Msg.show({
	                                                            title:'Error',
	                                                            msg: 'Error al finalizar detalle',
	                                                            buttons: Ext.Msg.OK,
	                                                            icon: Ext.Msg.ERROR
	                                                        });
	                                                    }
	                                                }
	                                                ,failure : function()
	                                                {
	                                                    win.setLoading(false);
	                                                    Ext.Msg.show({
	                                                        title:'Error',
	                                                        msg: 'Error de comunicaci&oacute;n',
	                                                        buttons: Ext.Msg.OK,
	                                                        icon: Ext.Msg.ERROR
	                                                    });
	                                                }
	                                            });
	                                        }
	                                    }
	                                    ,{
	                                        text     : 'Cancelar'
	                                        ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
	                                        ,handler : function()
	                                        {
	                                            this.up().up().destroy();
	                                        }
	                                    }
	                                ]
	                            }).show();
	                        }
	                        /*else if(cellIndex==5&&$(td).find('img').length>0)
	                        {
	                            debug("APRETASTE EL BOTON IMPRIMIR PARA EL RECORD:",record);
	                        }*/
	                    }
	                }
	            })
	            ,Ext.create('Ext.form.HtmlEditor',
	            {
	                id        : 'inputReadDetalleHtmlVisor'
	                ,width    : 690
	                ,height   : 200
	                ,readOnly : true
	            })
	        ]
	    }).show();
	    window.center();
	    Ext.getCmp('inputReadDetalleHtmlVisor').getToolbar().hide();
	}
	
function reasignarClick(grid,dataIndex)
{
	var record=grid.getStore().getAt(dataIndex);
	debug('reasignarClick record:',record);
	var rol='';
	if(record.get('status') == _STATUS_TRAMITE_EN_REVISION_MEDICA)
	{
		rol='medajustador';
	}
	else if(record.get('status') == _STATUS_TRAMITE_EN_CAPTURA)
	{
		rol='operadorsini';
	}
	debug('rol:',rol);
	
	var usuario = record.get('parametros.pv_otvalor16');
	debug('usuario encargado:',usuario);
	
	var valido = rol && usuario;
	if(!valido)
	{
		mensajeWarning('El tr&aacute;mite solo se puede reasignar si est&aacute; asignado a alg&uacute;n usuario'
		+' y en status "En captura" o "En revisi&oacute;n m&eacute;dica"');
	}
	
	if(valido)
	{
		Ext.Ajax.request(
		{
			url      : _mesasin_url_lista_reasignacion
			,params  :
			{
				'smap.cdsisrol' : rol
			}
			,success : function(response)
			{
				var json = Ext.decode(response.responseText);
				debug('respuesta:',json);
				var ventana = Ext.create('Ext.window.Window',
				{
					title   : 'Seleccionar usuario'
					,modal  : true
					,width  : 400
					,height : 300
					,overflowY: 'scroll' //(EGS)
					,items  :
					[
					    Ext.create('Ext.grid.Panel',
					    {
					    	columns :
					    	[
					    	    {
					    	    	header     : 'Nombre'
					    	    	,dataIndex : 'DSUSUARIO'
					    	    	,flex      : 1
					    	    }
					    	    ,{
					    	    	xtype         : 'actioncolumn'
					    	    	,width        : 20
					    	    	,menuDisabled : true
					    	    	,sortable     : false
					    	    	,icon         : '${ctx}/resources/fam3icons/icons/accept.png'
					    	    	,handler      : function(gridVen,rowIndexVen)
					    	    	{
					    	    		var record  = gridVen.getStore().getAt(rowIndexVen);
					    	    		var usuarioNuevo = record.get('CDUSUARIO');
					    	    		debug('usuarioNuevo:',usuarioNuevo);
					    	    		if(rol=='medajustador')
					    	    		{
					    	    			turnarAareaMedica(grid,dataIndex,usuarioNuevo);
					    	    			ventana.destroy();
					    	    		}
					    	    		else if(rol=='operadorsini')
					    	    		{
					    	    			turnarAoperadorReclamaciones(grid,dataIndex,usuarioNuevo);
					    	    			ventana.destroy();
					    	    		}
					    	    	}
					    	    }
					    	]
					        ,store  : Ext.create('Ext.data.Store',
					        {
					        	fields    : ['CDUSUARIO','DSUSUARIO']
					        	,autoLoad : true
					        	,proxy    :
					        	{
					        		type    : 'memory'
					        		,reader : 'json'
					        	    ,data   : json.slist1					        		
					        	}
					        })
					    })
					]
				});
				ventana.show();
				centrarVentanaInterna(ventana);
			}
		    ,failure : errorComunicacion
		});
	}
}

function turnarDevolucionTramite(grid,rowIndex,colIndex){
	var record = grid.getStore().getAt(rowIndex);
	var comentariosText = Ext.create('Ext.form.field.TextArea', {
		fieldLabel: 'Observaciones'
		,labelWidth: 150
		,width: 600
		,name:'smap1.comments'
		,height: 250
		,allowBlank : false
	});
	
	windowLoader = Ext.create('Ext.window.Window',{
		modal       : true,
		buttonAlign : 'center',
		width       : 663,
		height      : 430,
		autoScroll  : true,
		items       : [
			Ext.create('Ext.form.Panel', {
				title: 'Devolver Tr&aacute;mite',
				width: 650,
				url: _URL_ActualizaStatusTramite,
				bodyPadding: 5,
				items: [comentariosText,{
	                xtype       : 'radiogroup'
                    ,fieldLabel : 'Mostrar al agente'
                    ,columns    : 2
                    ,width      : 250
                    ,style      : 'margin:5px;'
                    ,hidden     : _GLOBAL_CDSISROL===RolSistema.Agente
                    ,items      :
                    [
                        {
                            boxLabel    : 'Si'
                            ,itemId     : 'SWAGENTE6'
                            ,name       : 'SWAGENTE6'
                            ,inputValue : 'S'
                            ,checked    : _GLOBAL_CDSISROL===RolSistema.Agente
                        }
                        ,{
                            boxLabel    : 'No'
                            ,name       : 'SWAGENTE6'
                            ,inputValue : 'N'
                            ,checked    : _GLOBAL_CDSISROL!==RolSistema.Agente
                        }
                    ]
                }],
				buttonAlign:'center',
				buttons: [{
					text: 'Devolver'
					,icon:_CONTEXT+'/resources/fam3icons/icons/accept.png'
					,buttonAlign : 'center',
					handler: function() {
						var formPanel = this.up().up();
						if (formPanel.form.isValid()) {
							formPanel.form.submit({
								waitMsg:'Procesando...',
								params: {
									'smap1.ntramite' : record.get('ntramite'), 
									'smap1.status'   : _STATUS_DEVOLVER_TRAMITE
									,'smap1.swagente' : _fieldById('SWAGENTE6').getGroupValue()
								},
								failure: function(form, action)
								{
									debug(action);
									switch (action.failureType)
									{
										case Ext.form.action.Action.CONNECT_FAILURE:
											errorComunicacion();
										break;
										case Ext.form.action.Action.SERVER_INVALID:
											mensajeError(action.result.mensaje);
										break;
									}
								},
								success: function(form, action) {
									mensajeCorrecto('Aviso','Se ha turnado con &eacute;xito.');
									loadMcdinStore();
									windowLoader.close();

								}
							});
						}else {
							Ext.Msg.show({
								title: 'Aviso',
								msg: 'Complete la informaci&oacute;n requerida',
								buttons: Ext.Msg.OK,
								icon: Ext.Msg.WARNING
							});
						}
					}
				},{
					text: 'Cancelar',
					icon:_CONTEXT+'/resources/fam3icons/icons/cancel.png',
					buttonAlign : 'center',
					handler: function() {
						windowLoader.close();
					}
				}
				]
			})
		]
	}).show();
	centrarVentana(windowLoader);
}

	//(EGS) Validamos solo un proveedor en reclamo pago directo
	function _11_validaProveedorPagoDirecto(grid,rowIndex,colIndex){
		var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"loading..."});
		myMask.show();
		var record = grid.getStore().getAt(rowIndex);
		Ext.Ajax.request({
			url		:	_URL_VALIDAR_PROVEEDOR_PD
			,params	:{
				'params.ntramite'	: record.get('ntramite')
			}
			,success : function(response,opts) {
				json = Ext.decode(response.responseText);
				var mensaje = json.mensaje;
				debug("success...",response.responseText);
				if(mensaje > 1){
					myMask.hide();
					centrarVentanaInterna(Ext.Msg.show({
						title: 'No es posible solicitar el pago',
						msg : 'Est&aacute; tratando de enviar un Pago Directo, para diferentes proveedores',
						buttons: Ext.Msg.OK,
						icon: Ext.Msg.ERROR
					}));
				}else{
					_11_validaAseguroLimiteCoberturas(grid,rowIndex,colIndex);
				}
			}
			,failure : function(response,opts){
				var obj = Ext.decode(response.responseText);
				var mensaje = obj.mensaje;
				debug("failure...",obj.mensaje);
				centrarVentanaInterna(Ext.Msg.show({
					title: 'Error',
					msg: Ext.isEmpty(mensaje) ? 'Error de comunicaci&oacute;n' : mensaje,
					buttons: Ext.Msg.OK,
					icon: Ext.Msg.ERROR
				}));
			}
		});
	}


Ext.onReady(function()
		{
		    
			// Se aumenta el timeout para todas las peticiones:
			Ext.Ajax.timeout = 1000*60*120;
		    Ext.override(Ext.form.Basic, { timeout: Ext.Ajax.timeout / 1000 });
		    Ext.override(Ext.data.proxy.Server, { timeout: Ext.Ajax.timeout });
		    Ext.override(Ext.data.Connection, { timeout: Ext.Ajax.timeout });
    
			/////////////////////
			////// modelos //////
			Ext.define('DetalleMC',{
		        extend:'Ext.data.Model',
		        fields:
		        [
		            "NTRAMITE"
		            ,"NMORDINA"
		            ,"CDTIPTRA"
		            ,"CDCLAUSU"
		            ,{name:"FECHAINI",type:'date',dateFormat:'d/m/Y H:i'}
		            ,{name:"FECHAFIN",type:'date',dateFormat:'d/m/Y H:i'}
		            ,"COMMENTS"
		            ,"CDUSUARI_INI"
		            ,"CDUSUARI_FIN"
		            ,"usuario_ini"
		            ,"usuario_fin"
		        ]
		    });
		    ////// modelos //////
			/////////////////////
		});
<s:if test="false">
</script>
</s:if>