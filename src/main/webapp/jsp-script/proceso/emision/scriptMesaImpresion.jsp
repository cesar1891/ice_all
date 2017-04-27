<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<s:if test="false">
<script>
</s:if>
////// variables //////
var _ROL_MC_AUTOS =  'MCEMIAUT';
var _ROL_MC_SALUD =  'MESADECONTROL';

//Variable para conocer el rol activo del sistema.
var cdSisRolActivo = '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />';

var _4_urlActualizarStatusRemesa = '<s:url namespace="/consultas"    action="actualizarStatusRemesa"    />';
var mesConUrlDocu                = '<s:url namespace="/documentos"   action="ventanaDocumentosPoliza"   />';
var mesConUrlDetMC               = '<s:url namespace="/mesacontrol"  action="obtenerDetallesTramite"    />';
var mesConUrlFinDetalleMC        = '<s:url namespace="/mesacontrol"  action="finalizarDetalleTramiteMC" />';
var _4_urlRecuperacion           = '<s:url namespace="/recuperacion" action="recuperar"                 />';
var _4_onReversarTramite		 = '<s:url namespace="/mesacontrol"	 action="reversaTramite"			/>';
////// variables //////

Ext.onReady(function()
{
    ////// requires //////
    ////// requires //////
    
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
    
    ////// stores //////
    ////// stores //////
    
    ////// componentes //////
    ////// componentes //////
    
    ////// contenido //////
    ////// contenido //////
});


////// funciones //////

function _4_calculaDetalleImpresion(v,md,rec)
{
    debug('_4_calculaDetalleImpresion rec:',rec,'.');
    
    var calc = '';
    
    try
    {
        var ntramite = rec.get('ntramite');
        var req      = Number(rec.get('parametros.pv_otvalor04'));
        var eje      = Number(rec.get('parametros.pv_otvalor05'));
	    
	    debug('ntramite,req,eje {',ntramite,req,eje,'}');
	    
	    var prim = true;
	    
	    if(req%10==1)//necesito B
	    {
	        if(eje%10==0)//falta B
	        {
	            if(prim)
	            {
	                prim =  false;
	                calc += 'PAPELER\u00CDA';
	            }
	            else
	            {
	                calc += ', PAPELER\u00CDA';
	            }
	        }
	    }
	    
	    req = Math.floor(req/10);
	    eje = Math.floor(eje/10);
	    
	    if(req%10==1)//necesito M
	    {
	        if(eje%10==0)//falta M
	        {
	            if(prim)
	            {
	                prim =  false;
	                calc += 'RECIBOS';
	            }
	            else
	            {
	                calc += ', RECIBOS';
	            }
	        }
	    }
	    
	    req = Math.floor(req/10);
	    eje = Math.floor(eje/10);
	    
	    if(req%10==1)//necesito C
	    {
	        if(eje%10==0)//falta C
	        {
	            if(prim)
	            {
	                prim =  false;
	                calc += 'CREDENCIALES';
	            }
	            else
	            {
	                calc += ', CREDENCIALES';
	            }
	        }
	    }
	}
	catch(e)
	{
	    debugError(e);
	    calc = 'error';
	}
    
    return calc;
}

function _4_actualizarRemesaClic(bot)
{
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'ACTUALIZAR REMESA'
        ,modal       : true
        ,defaults    : { style : 'margin:5px;', border:0 }
        ,closeAction : 'destroy'
        ,items    : [{
        	layout : 'hbox',
        	hidden  : cdSisRolActivo == _ROL_MC_AUTOS || cdSisRolActivo == _ROL_MC_SALUD ? true : false,
        	defaults    : { style : 'margin:5px;'},
            items : [{
                xtype       : 'numberfield'
                ,fieldLabel : 'Remesa'
                ,status     : 36
                ,listeners:{
                    scope:this,
                    specialkey: function(f,e){
                        if(e.getKey()==e.ENTER){  
                        	agregarRemesa(f);
                        	f.reset();
                        	
                        }
                    }
                }
          	},{
                xtype    : 'button'
                ,icon    : '${icons}sitemap_color.png'
                ,text    : 'Marcar como armada'
                ,width   : 200
                ,status  : 36
                //,handler : _4_marcarRemesaClic
                ,handler : marcarRemesaClicGrupo
            },{
            	 xtype		 : 'gridpanel' 
                ,status     : 36
                ,itemId      : 'colleccionRemesaArmada'
                ,width       : 255
                ,height      : 80
                ,hideHeaders : true
                ,rowspan     : 2
                ,store       : Ext.create('Ext.data.Store',
                {
                    model : 'Generic'
                    ,data : []
                })
                ,columns :
                [
                    {
                        dataIndex : 'value'
                        ,flex     : 1
                    }
                    ,{
                        xtype    : 'actioncolumn'
                        ,width   : 30
                        ,icon    : '${icons}delete.png'
                        ,handler : function(view,row,col,item,e,record){ _fieldById('colleccionRemesaArmada').getStore().remove(record); }
                    }
                ]
            }
            
            
            ]
		},{
        	layout : 'hbox',
        	defaults    : { style : 'margin:5px;'},
            items : [{
                xtype       : 'numberfield'
                ,fieldLabel : 'Remesa'
                ,status     : 37
                ,listeners:{
                    scope:this,
                    specialkey: function(f,e){
                        if(e.getKey()==e.ENTER){                	
                        	
                        	agregarRemesa(f);
                        	f.reset();
                        	
                        }
                    }
                }
            },{
                xtype    : 'button'
                ,icon    : '${icons}user_comment.png'
                ,text    : 'Marcar como entrega f\u00EDsica'
                ,width   : 200
                ,status  : 37
                ,handler : marcarRemesaClicGrupo
            },{
           	 	  xtype		  : 'gridpanel' 
           	     ,status      : 37
                 ,itemId      : 'colleccionRemesaEntregFisica'
                 ,width       : 255
                 ,height      : 80
                 ,hideHeaders : true
                 ,rowspan     : 2
                 ,store       : Ext.create('Ext.data.Store',
                 {
                     model : 'Generic'
                     ,data : []
                 })
                 ,columns :
                 [
                     {
                         dataIndex : 'value'
                         ,flex     : 1
                     }
                     ,{
                         xtype    : 'actioncolumn'
                         ,width   : 30
                         ,icon    : '${icons}delete.png'
                         ,handler : function(view,row,col,item,e,record){ _fieldById('colleccionRemesaEntregFisica').getStore().remove(record); }
                     }
                 ]
             }
            ]
        },{
        	layout : 'hbox',
        	hidden  : cdSisRolActivo == _ROL_MC_AUTOS || cdSisRolActivo == _ROL_MC_SALUD ? true : false,
        	defaults    : { style : 'margin:5px;'},
            items : [{
                xtype       : 'numberfield'
                ,fieldLabel : 'Remesa'
                ,status     : 38
                ,listeners:{
                    scope:this,
                    specialkey: function(f,e){
                        if(e.getKey()==e.ENTER){                	
                        	
                        	agregarRemesa(f);
                        	f.reset();
                        	
                        }
                    }
                }
            },{
                xtype    : 'button'
                ,icon    : '${icons}package.png'
                ,text    : 'Marcar como entraga paqueter\u00EDa'
                ,width   : 200
                ,status  : 38
                ,handler : marcarRemesaClicGrupo
            },{
         	 	  xtype		  : 'gridpanel' 
         	 	 ,status      : 38
                 ,itemId      : 'colleccionRemesaEntregPaqueteria'
                 ,width       : 255
                 ,height      : 80
                 ,hideHeaders : true
                 ,rowspan     : 2
                 ,store       : Ext.create('Ext.data.Store',
                 {
                     model : 'Generic'
                     ,data : []
                 })
                 ,columns :
                      [
                          {
                              dataIndex : 'value'
                              ,flex     : 1
                          }
                          ,{
                              xtype    : 'actioncolumn'
                              ,width   : 30
                              ,icon    : '${icons}delete.png'
                              ,handler : function(view,row,col,item,e,record){ _fieldById('colleccionRemesaEntregPaqueteria').getStore().remove(record); }
                          }
                      ]
               }
            ]
        }]
    }).show());
}
function marcarRemesaClicGrupo(bot)
{
	 var ck = 'Solicitando cambio de status de remesa';
	 debug('marcarRemesaClicGrupo');
	 try{
		    var gridpanel = Ext.ComponentQuery.query('[xtype=gridpanel][status='+bot.status+']')[0];
		    var store=gridpanel.getStore();
		    
		    if(store.count()==0)
	    {
	        throw 'Favor de introducir remesa';
	    }
		    
		    
		    listaTramites="";
			store .each(function(rec){
		    	listaTramites+=rec.getData().value+",  ";
		    });
			listaTramites=listaTramites.trim().substr(0,listaTramites.length-3);
			centrarVentanaInterna(
					Ext.Msg.confirm("Confirmación"
									,"Las siguientes remesas se actualizarán:<br><br>"+listaTramites+"<br><br>¿Estás seguro?"
									,function(opt){
										if(opt=="yes"){
											
											   
	    var win = bot.up('window');
	    _setLoading(true,win);
												    
												    var tramites=new Array();
												    store .each(function(rec){
												    	tramites.push(rec.getData().value);
												    });
	    Ext.Ajax.request(
	    {
	        url      : _4_urlActualizarStatusRemesa
	        ,params  :
	        {
												             'params.status'    : bot.status
												            ,'tramites'  		: tramites
	        }
	        ,success : function(response)
	        {
	            _setLoading(false,win);
	            
												            var ck = 'Decodificando respuesta al cambiar status de remesa';
	            try
	            {
	                var json = Ext.decode(response.responseText);
	                debug('### actualizar status:',json);
	                if(json.success==true)
	                {
	                    mensajeCorrecto(
	                        'Remesa actualizada'
												                        ,'Las remesas han sido actualizadas'
	                        ,function()
	                        {
	                            win.destroy();
	                            loadMcdinStore();
	                        }
	                    );
	                }
	                else
	                {
	                    mensajeError(json.message);
	                }
	            }
	            catch(e)
	            {
	                manejaException(e,ck);
	            }
	        }
	        ,failure : function()
	        {
	            _setLoading(false,win);
	            errorComunicacion(null,'Error al solicitar cambio de status de remesa');
	        }
	    });
												
										}
			}));
	}
	catch(e)
	{
	    manejaException(e,ck);
	}
}

function _4_onFolderClick(rowIndex)
{
    debug(rowIndex);
    var record=mcdinStore.getAt(rowIndex);
    debug(record);
    centrarVentanaInterna(Ext.create('Ext.window.Window',
    {
        title        : 'Documentaci&oacute;n'
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 700
        ,height      : 400
        ,autoScroll  : true
        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
        ,loader      :
        {
            url       : mesConUrlDocu
            ,params   :
            {
                'smap1.nmpoliza'  : '0'
                ,'smap1.cdunieco' : ''
                ,'smap1.cdramo'   : ''
                ,'smap1.estado'   : ''
                ,'smap1.nmsuplem' : '0'
                ,'smap1.ntramite' : record.get('ntramite')
                ,'smap1.nmsolici' : '0'
                ,'smap1.tipomov'  : '0'
            }
            ,scripts  : true
            ,autoLoad : true
        }
    }).show());
}

function _4_onClockClick(rowIndex)
{
    var record=mcdinStore.getAt(rowIndex);
    debug(record);
    var window=Ext.create('Ext.window.Window',
    {
        title        : 'Detalles del tr&aacute;mite '+record.get('ntramite')
        ,modal       : true
        ,buttonAlign : 'center'
        ,width       : 630
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
                        ,hidden	   : true
                        ,format    : 'd M Y H:i'
                        ,width     : 90
                    }
                    ,{
                        header     : 'Usuario fin'
                        ,dataIndex : 'usuario_fin'
                        ,hidden		  : true
                        ,width     : 150
                    }
                    ,{
                        text	: 'Preview'
                        ,dataIndex : 'COMMENTS'
                        ,width 	: 200
                        
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
                        debug('record',record);
                        if(cellIndex<6)
                        {
                            Ext.getCmp('inputReadDetalleHtmlVisor').setValue((_4_smap1.cdsisrol!='EJECUTIVOCUENTA'||record.raw.SWAGENTE=='S')?record.get('COMMENTS'):'');
                        }
                        else if(cellIndex==6&&$(td).find('img').length>0)
                        {
                            debug('finalizar');
                            centrarVentanaInterna(Ext.create('Ext.window.Window',
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
                            }).show());
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
                ,width    : 620
                ,height   : 200
                ,readOnly : true
            })
        ]
    }).show();
    centrarVentanaInterna(window);
    Ext.getCmp('inputReadDetalleHtmlVisor').getToolbar().hide();
}

function _4_onReversarClic(row){
    debug('>_4_onReversarClic row:',row);
    var record=mcdinStore.getAt(row);
    debug('record.get(ntramite): ', record.get('ntramite'));
    var tramite = record.get('ntramite');
    
  var ck = 'Solicitando Revesar Tramite';
    
    try
    {
	    
	    
	    var win = mcdinGrid;
	    _setLoading(true,win);
	    Ext.Ajax.request(
	    {
	        url      : _4_onReversarTramite
	        ,params  :
	        {
	            'smap1.pv_ntramite' : record.get('ntramite')
	            
	        }
	        ,success : function(response)
	        {
	            _setLoading(false,win);
	            
	            var vk = 'Decodificando respuesta al cambiar status de tramite';
	            try
	            {
	                var json = Ext.decode(response.responseText);
	                debug('### actualizar status reverso:',json);
	                if(json.success==true)
	                {
	                    mensajeCorrecto(
	                        'Reverso actualizado'
	                        ,'El reverso ha sido actualizado'
	                        ,function()
	                        {
	                        	var params1=
		    	    			{
		    	    				'smap1.pv_cdunieco_i'      : _fieldByName('smap2.pv_cdunieco_i').getValue()
		    	    			    ,'smap1.pv_cdramo_i'       : _fieldByName('smap2.pv_cdramo_i').getValue()
		    	                    ,'smap1.pv_cdtipsit_i'     : _fieldByName('smap2.pv_cdtipsit_i').getValue()
		    	                    ,'smap1.pv_estado_i'       : _fieldByName('smap2.pv_estado_i').getValue()
		    	    			    ,'smap1.pv_nmpoliza_i'     : _fieldByName('smap2.pv_nmpoliza_i').getValue()
		    	                    ,'smap1.pv_cdagente_i'     : _fieldByName('smap2.pv_cdagente_i').getValue()
		    	    			    ,'smap1.pv_ntramite_i'     : _fieldByName('smap2.pv_ntramite_i').getValue()
		    	                    ,'smap1.pv_status_i'       : _fieldByName('smap2.pv_status_i').getValue()
		    	                    ,'smap1.pv_fedesde_i'      : Ext.isEmpty(_fieldByName('smap2.pv_fedesde_i').getValue())?'':Ext.Date.format(_fieldByName('smap2.pv_fedesde_i').getValue(),'d/m/Y')
		    	                    ,'smap1.pv_fehasta_i'      : Ext.isEmpty(_fieldByName('smap2.pv_fehasta_i').getValue())?'':Ext.Date.format(_fieldByName('smap2.pv_fehasta_i').getValue(),'d/m/Y')
		    	                    ,'smap1.pv_cdtiptra_i'     : mcdinInput['tiptra']
		    	    				,'smap1.pv_contrarecibo_i' : Ext.isEmpty(_fieldByName('smap2.pv_contrarecibo_i').getValue())?'':_fieldByName('smap2.pv_contrarecibo_i').getValue()
		    	    				,'smap1.pv_tipoPago_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_tipoPago_i').getValue())?'':_fieldByName('smap2.pv_tipoPago_i').getValue()
		    	    				,'smap1.pv_nfactura_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_nfactura_i').getValue())?'':_fieldByName('smap2.pv_nfactura_i').getValue()
		    	    				,'smap1.pv_cdpresta_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_cdpresta_i').getValue())?'':_fieldByName('smap2.pv_cdpresta_i').getValue()
		    	    				,'smap1.filtro'            : loadMcdinStoreFiltro
		    	    				,'smap1.lote'              : _getValueByName('smap1.lote'          , false)
		    	    				,'smap1.tipolote'          : _getValueByName('smap1.tipolote'      , false)
		    	    				,'smap1.tipoimpr'          : _getValueByName('smap1.tipoimpr'      , false)
		    	    				,'smap1.cdusuari_busq'     : _getValueByName('smap1.cdusuari_busq' , false)
		    	    			};
	                           cargaStorePaginadoLocal(mcdinStore, mcdinUrlCargar, 'olist1', params1, function()
	                           {
	                           
	                           }, mcdinGrid);
	                        }
	                    );
	                }
	                else
	                {
	                    mensajeError(json.message);
	                }
	            }
	            catch(e)
	            {
	                manejaException(e,ck);
	            }
	        }
	        ,failure : function()
	        {
	            _setLoading(false,win);
	            errorComunicacion(null,'Error al solicitar cambio de reverso');
	        }
	    });
	}
	catch(e)
	{
	    manejaException(e,ck);
	}
}
function _4_onContinuarImpresionClic(row)
{
    debug('>_4_onContinuarImpresionClic row:',row,'.');
    var ck = 'Preparando ventana de impresi\u00F3n';
    try
    {
        var record = mcdinStore.getAt(row);
        debug('record:',record);
        var lote = record.get('parametros.pv_otvalor01');
        centrarVentanaInterna(
            Ext.MessageBox.confirm(
                'Confirmar'
                ,'¿Desea continuar la impresi\u00F3n del lote '+lote+'?'
                ,function(btn)
                {
                    if(btn === 'yes')
                    {
                        var ck = 'Invocando ventana de impresi\u00F3n';
                        try
                        {
                            centrarVentanaInterna(Ext.create('VentanaImpresionLote',
                            {
                                lote        : lote
                                ,tramite    : record.get('ntramite')
                                ,cdtipram   : record.get('parametros.pv_otvalor03')
                                ,cdtipimp   : record.get('parametros.pv_otvalor02')
                                ,tipolote   : record.get('parametros.pv_otvalor06')
                                ,callback   : function(){ mcdinStore.removeAll(); }
                                ,closable   : false
                                ,cancelable : true
                            }).show());
                        }
                        catch(e)
                        {
                            manejaException(e,ck);
                        }
                    }
                }
            )
        );  
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _4_onVerDetalleLoteClic(row)
{
    debug('_4_onVerDetalleLoteClic row:',row);
    var ck = 'Recuperando detalle de lote';
    try
    {
    
        var record = mcdinStore.getAt(row);
        debug('record:',record);
        
        var lote = record.get('parametros.pv_otvalor01');
        debug('lote:',lote);
    
        _setLoading(true,mcdinGrid);
        Ext.Ajax.request(
        {
            url      : mcdinUrlCargar
            ,params  :
            {
                'smap1.pv_cdunieco_i'      : ''
				,'smap1.pv_ntramite_i'     : ''
				,'smap1.pv_cdramo_i'       : ''
				,'smap1.pv_nmpoliza_i'     : ''
				,'smap1.pv_estado_i'       : ''
				,'smap1.pv_cdagente_i'     : ''
				,'smap1.pv_status_i'       : '0'
				,'smap1.pv_cdtipsit_i'     : ''
				,'smap1.pv_fedesde_i'      : ''
				,'smap1.pv_fehasta_i'      : ''
				,'smap1.pv_cdtiptra_i'     : '20'
				,'smap1.pv_contrarecibo_i' : ''
				,'smap1.pv_tipoPago_i'     : ''
				,'smap1.pv_nfactura_i'     : ''
				,'smap1.pv_cdpresta_i'     : ''
				,'smap1.cdtipram'          : ''
				,'smap1.lote'              : lote
				,'smap1.tipolote'          : ''
				,'smap1.tipoimpr'          : ''
				,'smap1.cdusuari_busq'     : ''
            }
            ,success : function(response)
            {
                _setLoading(false,mcdinGrid);
                var ck = 'Decodificando respuesta al recuperar detalle de lote';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### detalle lote:',json);
                    if(json.success==true)
                    {
                        ck = 'Construyendo ventana de detalle';
                        var ven = centrarVentanaInterna(Ext.create('Ext.window.Window',
                        {
                            title        : 'DETALLE DE LOTE '+lote
                            ,modal       : true
                            ,closeAction : 'destroy'
                            ,items       :
                            [
                                Ext.create('Ext.grid.Panel',
                                {
                                    width    : 700
                                    ,height  : 300
                                    ,columns : [ <s:property value="imap1.gridColumns" /> ]
                                    ,store   : Ext.create('Ext.data.Store',
                                    {
                                        model     : 'McdinModelo'
                                        ,autoLoad : true
                                        ,proxy    :
                                        {
                                            reader : 'json'
                                            ,type  : 'memory'
                                            ,data  : json.olist1
                                        }
                                    })
                                })
                            ]
                        }).show());
                    }
                    else
                    {
                        mensajeError('No se pudo cargar detalle de lote');
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                _setLoading(false,mcdinGrid);
                errorComunicacion(null,'Error recuperando detalle de lote');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function agregarRemesa(txtField){
	
	var store= Ext.ComponentQuery.query('[xtype=gridpanel][status='+txtField.status+']')[0];
	store=store.getStore();
	debug('agregarRemesaArmada');
    var ck = 'Verificar que han introducido una remesa';
    
    try
    {
	   
		var remesa={"value":txtField.getValue()};
		
		if(store.find('value',remesa.value)==-1 && !Ext.isEmpty(txtField.getValue())){
			store.add(remesa);
		}
    }catch(e)
	{
	    manejaException(e,ck);
	}
	
	
	
}

function _4_onVerDetalleRemesaClic(row)
{
    debug('_4_onVerDetalleRemesaClic row:',row);
    var ck = 'Recuperando detalle de remesa';
    try
    {
    
        var record = mcdinStore.getAt(row);
        debug('record:',record);
        
        var remesa    = record.get('ntramite')
            ,tipolote = record.get('parametros.pv_otvalor06');
        debug('remesa,tipolote:',remesa,tipolote,'.');
    
        _setLoading(true,mcdinGrid);
        Ext.Ajax.request(
        {
            url      : _4_urlRecuperacion
            ,params  :
            {
                'params.consulta'  : 'RECUPERAR_DETALLE_REMESA'
                ,'params.ntramite' : remesa
                ,'params.tipolote' : tipolote
            }
            ,success : function(response)
            {
                _setLoading(false,mcdinGrid);
                var ck = 'Decodificando respuesta al recuperar detalle de remesa';
                try
                {
                    var json = Ext.decode(response.responseText);
                    debug('### detalle remesa:',json);
                    if(json.success==true)
                    {
                        ck = 'Construyendo ventana de detalle';
                        centrarVentanaInterna(Ext.create('Ext.window.Window',
                        {
                            title        : 'DETALLE DE REMESA '+remesa
                            ,modal       : true
                            ,closeAction : 'destroy'
                            ,items       :
                            [
                                Ext.create('Ext.grid.Panel',
                                {
                                    width    : 700
                                    ,height  : 300
                                    ,columns :
                                    [
                                        {
                                            text       : 'REMESA'
                                            ,dataIndex : 'ntramite'
                                            ,width     : 90
                                        }
                                        ,{
                                            text       : 'SUCURSAL'
                                            ,dataIndex : 'cdunieco'
                                            ,width     : 90
                                        }
                                        ,{
                                            text       : 'PRODUCTO'
                                            ,dataIndex : 'cdramo'
                                            ,width     : 90
                                        }
                                        ,{
                                            text       : 'P\u00D3LIZA'
                                            ,dataIndex : 'nmpoliza'
                                            ,width     : 100
                                        }
                                        ,{
                                            text       : 'TR\u00C1MITE<br/>EMISI\u00D3N/ENDOSO'
                                            ,dataIndex : 'nmtraope'
                                            ,width     : 120
                                            ,hidden    : tipolote=='R'
                                        }
                                        ,{
                                            text       : 'NO.'
                                            ,dataIndex : 'nsuplogi'
                                            ,width     : 40
                                            ,hidden    : tipolote=='R'
                                        }
                                        ,{
                                            text       : 'MOVIMIENTO'
                                            ,dataIndex : 'descrip'
                                            ,width     : 160
                                            ,hidden    : tipolote=='R'
                                        }
                                        ,{
                                            text       : 'RECIBO'
                                            ,dataIndex : 'descrip'
                                            ,width     : 100
                                            ,hidden    : tipolote=='P'
                                        }
                                        ,{
                                            text       : 'IMPORTE'
                                            ,dataIndex : 'ptimport'
                                            ,width     : 100
                                            ,hidden    : tipolote=='P'
                                            ,renderer  : Ext.util.Format.usMoney
                                        }
                                        ,{
                                            text       : 'AGENTE'
                                            ,dataIndex : 'nombagte'
                                            ,width     : 200
                                        }
                                    ]
                                    ,store   : Ext.create('Ext.data.Store',
                                    {
                                        fields    :
                                        [
                                            'ntramite'  , 'cdunieco' , 'cdramo'
                                            ,'nmpoliza' , 'nmtraope' , 'nmrecibo'
                                            ,'descrip'  , 'ptimport' , 'nombagte'
                                            ,'nsuplogi'
                                        ]
                                        ,autoLoad : true
                                        ,proxy    :
                                        {
                                            reader : 'json'
                                            ,type  : 'memory'
                                            ,data  : json.list
                                        }
                                    })
                                })
                            ]
                        }).show());
                    }
                    else
                    {
                        mensajeError('No se pudo cargar detalle de lote');
                    }
                }
                catch(e)
                {
                    manejaException(e,ck);
                }
            }
            ,failure : function()
            {
                _setLoading(false,mcdinGrid);
                errorComunicacion(null,'Error recuperando detalle de remesa');
            }
        });
    }
    catch(e)
    {
        manejaException(e,ck);
    }
}

function _4_onContinuarDescargaClic(row){
	 debug('>_4_onContinuarImpresionClic row:',row,'.');
	    var ck = 'Preparando ventana de impresi\u00F3n';
	    try
	    {
	        var record = mcdinStore.getAt(row);
	        debug('record:',record);
	        var lote = record.get('parametros.pv_otvalor01');
	        centrarVentanaInterna(
	            Ext.MessageBox.confirm(
	                'Confirmar'
	                ,'¿Desea continuar la impresi\u00F3n del lote '+lote+'?'
	                ,function(btn)
	                {
	                    if(btn === 'yes')
	                    {
	                        var ck = 'Invocando ventana de impresi\u00F3n';
	                        try
	                        {
	                            centrarVentanaInterna(Ext.create('VentanaDescargaLote',
	                            {
	                                lote        : lote
	                                ,tramite    : record.get('ntramite')
	                                ,cdtipram   : record.get('parametros.pv_otvalor03')
	                                ,cdtipimp   : record.get('parametros.pv_otvalor02')
	                                ,tipolote   : record.get('parametros.pv_otvalor06')
	                                ,callback   : function(){ mcdinStore.removeAll(); }
	                                ,closable   : false
	                                ,cancelable : true
	                            }).show());
	                        }
	                        catch(e)
	                        {
	                            manejaException(e,ck);
	                        }
	                    }
	                }
	            )
	        );  
	    }
	    catch(e)
	    {
	        manejaException(e,ck);
	    }
}
// Se agrega funcion para reversar mas de un tramite
function _4_reversaTramitesClic(){
	debug('>_4_reversaTramitesClic');
	
	selected=[];
	selctn = mcdinGrid.getSelectionModel();
	var s = selctn.getSelection();
	Ext.each(s, function(item) {
		debug('item: ', item.data.ntramite);
	    selected.push(item.data.ntramite);
	});
	debug('selected-> ', selected);
	
    
  var ck = 'Solicitando Revesar Tramite';
    
    try
    {
	    
	    
	    var win = mcdinGrid;
	    _setLoading(true,win);
	    Ext.Ajax.request(
	    {
	        url      : _4_onReversarTramite
	        ,params  :
	        {
	            'smap1.pv_ntramite' : selected
	            
	        }
	        ,success : function(response)
	        {
	            _setLoading(false,win);
	            
	            var vk = 'Decodificando respuesta al cambiar status de tramite';
	            try
	            {
	                var json = Ext.decode(response.responseText);
	                debug('### actualizar status reverso:',json);
	                if(json.success==true)
	                {
	                    mensajeCorrecto(
	                        'Reverso actualizado'
	                        ,'El reverso ha sido actualizado'
	                        ,function()
	                        {
	                        	var params1=
		    	    			{
		    	    				'smap1.pv_cdunieco_i'      : _fieldByName('smap2.pv_cdunieco_i').getValue()
		    	    			    ,'smap1.pv_cdramo_i'       : _fieldByName('smap2.pv_cdramo_i').getValue()
		    	                    ,'smap1.pv_cdtipsit_i'     : _fieldByName('smap2.pv_cdtipsit_i').getValue()
		    	                    ,'smap1.pv_estado_i'       : _fieldByName('smap2.pv_estado_i').getValue()
		    	    			    ,'smap1.pv_nmpoliza_i'     : _fieldByName('smap2.pv_nmpoliza_i').getValue()
		    	                    ,'smap1.pv_cdagente_i'     : _fieldByName('smap2.pv_cdagente_i').getValue()
		    	    			    ,'smap1.pv_ntramite_i'     : _fieldByName('smap2.pv_ntramite_i').getValue()
		    	                    ,'smap1.pv_status_i'       : _fieldByName('smap2.pv_status_i').getValue()
		    	                    ,'smap1.pv_fedesde_i'      : Ext.isEmpty(_fieldByName('smap2.pv_fedesde_i').getValue())?'':Ext.Date.format(_fieldByName('smap2.pv_fedesde_i').getValue(),'d/m/Y')
		    	                    ,'smap1.pv_fehasta_i'      : Ext.isEmpty(_fieldByName('smap2.pv_fehasta_i').getValue())?'':Ext.Date.format(_fieldByName('smap2.pv_fehasta_i').getValue(),'d/m/Y')
		    	                    ,'smap1.pv_cdtiptra_i'     : mcdinInput['tiptra']
		    	    				,'smap1.pv_contrarecibo_i' : Ext.isEmpty(_fieldByName('smap2.pv_contrarecibo_i').getValue())?'':_fieldByName('smap2.pv_contrarecibo_i').getValue()
		    	    				,'smap1.pv_tipoPago_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_tipoPago_i').getValue())?'':_fieldByName('smap2.pv_tipoPago_i').getValue()
		    	    				,'smap1.pv_nfactura_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_nfactura_i').getValue())?'':_fieldByName('smap2.pv_nfactura_i').getValue()
		    	    				,'smap1.pv_cdpresta_i'	   : Ext.isEmpty(_fieldByName('smap2.pv_cdpresta_i').getValue())?'':_fieldByName('smap2.pv_cdpresta_i').getValue()
		    	    				,'smap1.filtro'            : loadMcdinStoreFiltro
		    	    				,'smap1.lote'              : _getValueByName('smap1.lote'          , false)
		    	    				,'smap1.tipolote'          : _getValueByName('smap1.tipolote'      , false)
		    	    				,'smap1.tipoimpr'          : _getValueByName('smap1.tipoimpr'      , false)
		    	    				,'smap1.cdusuari_busq'     : _getValueByName('smap1.cdusuari_busq' , false)
		    	    			};
	                           cargaStorePaginadoLocal(mcdinStore, mcdinUrlCargar, 'olist1', params1, function()
	                           {
	                           
	                           }, mcdinGrid);
	                        }
	                    );
	                }
	                else
	                {
	                    mensajeError(json.message);
	                }
	            }
	            catch(e)
	            {
	                manejaException(e,ck);
	            }
	        }
	        ,failure : function()
	        {
	            _setLoading(false,win);
	            errorComunicacion(null,'Error al solicitar cambio de reverso');
	        }
	    });
	}
	catch(e)
	{
	    manejaException(e,ck);
	}
}
////// funciones //////
<s:if test="false">
</script>
</s:if>
</script>
<script type="text/javascript"
	src="${defines}VentanaImpresionLote.js?n=${now}"></script>
	<script type="text/javascript"
	src="${defines}VentanaDescargaLote.js?n=${now}"></script>
<script>