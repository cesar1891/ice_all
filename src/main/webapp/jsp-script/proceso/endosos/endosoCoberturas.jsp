<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
    ///////////////////////
    //////variables //////
    /*///////////////////*/
    var CD_ROL_SUSCRIPTOR       = 'SUSCRIPTOR';//|SUSCRIAUTO|TECNISUSCRI|EMISUSCRI|JEFESUSCRI|GERENSUSCRI|SUBDIRSUSCRI|
    var CD_ROL_SUSCRIPTOR_AUTO  = 'SUSCRIAUTO';//|SUSCRIAUTO|TECNISUSCRI|EMISUSCRI|JEFESUSCRI|GERENSUSCRI|SUBDIRSUSCRI|
    var CD_ROL_ACTUAL           = '<s:property value="%{#session['USUARIO'].rolActivo.clave}" />';
    var CD_RAMO_AUTOS_RESIDENTES= '5';
    var storeCoberturasActuales_p3;
    var storeCoberturasEditadas_p3;
    var storeCoberturasDisponibles_p3;
    var storeIncisos_p3;
    var panelCoberturasp3;
    var paramsincisos    = <s:property value="%{convertToJSON('slist1')}" escapeHtml="false" />;
    var slist2		     = <s:property value="%{convertToJSON('slist2')}" escapeHtml="false" />;
    var _p3_smap1        = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
    var pantallaOrigen   = '<s:property value="smap1.pantallaOrigen" />';
    var inputCduniecop3  = '<s:property value="smap1.CDUNIECO" />';
    var inputCdramop3    = '<s:property value="smap1.CDRAMO" />';
    var inputEstadop3    = '<s:property value="smap1.ESTADO" />';
    var inputNmpolizap3  = '<s:property value="smap1.NMPOLIZA" />';
    var inputCdpersonap3 = '<s:property value="smap1.CDPERSON" />';
    var inputNtramitep3  = '<s:property value="smap1.NTRAMITE" />';
    var inputAltabajap3  = '<s:property value="smap1.altabaja" />';
   
    var columnasTatrisit                               = [<s:property value="columnas" escapeHtml="false" />];
    var _endcob_urlObtenerComponenteSituacionCobertura = '<s:url namespace="/endosos"    action="obtenerComponenteSituacionCobertura"  />';
    var urlCargarCoberturasp3                          = '<s:url namespace="/"           action="cargarPantallaCoberturas"             />';
    var urlCargarCoberturasDispp3                      = '<s:url namespace="/endosos"    action="obtenerCoberturasDisponibles"         />';
    var urlGuardarCoberturasp3                         = '<s:url namespace="/"           action="guardarCoberturasUsuario"             />';
    var urlTatrip3                                     = '<s:url namespace="/"           action="obtenerCamposTatrigar"                />';
    var urlLoadTatrip3                                 = '<s:url namespace="/"           action="obtenerValoresTatrigar"               />';
    var urlRecuperacionSimplep3                        = '<s:url namespace="/emision"    action="recuperacionSimple"                   />';
    var urlRecuperacionSimpleListap3                   = '<s:url namespace="/emision"    action="recuperacionSimpleLista"              />';
    var endcobUrlDoc                                   = '<s:url namespace="/documentos" action="ventanaDocumentosPoliza"              />';
    var endcobUrlGuardar                               = '<s:url namespace="/endosos"    action="guardarEndosoCoberturas"              />';
    var urlPantallaBeneficiarios                       = '<s:url namespace="/catalogos"  action="includes/pantallaBeneficiarios"       />';
    var url_PantallaPreview                            = '<s:url namespace="/endosos"    action="includes/previewEndosos"              />';
    var _p30_urlViewDoc                                = '<s:url namespace="/documentos"      action="descargaDocInline"               />';
    var _RUTA_DOCUMENTOS_TEMPORAL                      = '<s:text name="ruta.documentos.temporal"                                      />';
   
    debug('inputCduniecop3',inputCduniecop3);
    debug('inputCdramop3',inputCdramop3);
    debug('inputEstadop3',inputEstadop3);
    debug('inputNmpolizap3',inputNmpolizap3);
    debug('inputCdpersonap3',inputCdpersonap3);
    debug('inputNtramitep3',inputNtramitep3);
    debug('inputAltabajap3',inputAltabajap3);
    
    var _p3_flujo = <s:property value="%{convertToJSON('flujo')}" escapeHtml="false" />;
     
    debug('_p3_flujo:',_p3_flujo);
    /*///////////////////*/
    //////variables //////
    ///////////////////////
    
    ///////////////////////
    ////// funciones //////
    /*///////////////////*/
function endcobSumit(form,confirmar)
    	{
        debug('generar endoso',confirmar);
        //var form=this.up().up();
        if(form.isValid())
        {
            // Eliminamos los filtros para que enviemos todas las coberturas editadas:
            storeCoberturasEditadas_p3.clearFilter();
        	
            var json={};
            json['omap1']=form.getValues();
            json['omap1']['pv_cdunieco_i'] = inputCduniecop3;
            json['omap1']['pv_cdramo_i']   = inputCdramop3;
            json['omap1']['pv_estado_i']   = inputEstadop3;
            json['omap1']['pv_nmpoliza_i'] = inputNmpolizap3;
            json['omap1']['pv_ntramite_i'] = inputNtramitep3;
            json['omap1']['tipoflot']      = _p3_smap1.TIPOFLOT;
            var slist1=[];
            json['slist1']=slist1;
            storeCoberturasEditadas_p3.each(function(record)
            {
                slist1.push(
                {
                    garantia   : record.get('GARANTIA')
                    ,cdcapita  : record.get('CDCAPITA')
                    ,status    : record.get('status')
                    ,ptcapita  : record.get('SUMA_ASEGURADA')
                    ,ptreduci  : record.get('ptreduci')
                    ,fereduci  : record.get('fereduci')
                    ,swrevalo  : record.get('swrevalo')
                    ,cdagrupa  : record.get('cdagrupa')
                    ,cdtipbca  : record.get('cdtipbca')
                    ,ptvalbas  : record.get('ptvalbas')
                    ,swmanual  : record.get('swmanual')
                    ,swreas    : record.get('swreas')
                    ,cdatribu1 : record.get('cdatribu1')
                    ,otvalor1  : record.get('otvalor1')
                    ,cdatribu2 : record.get('cdatribu2')
                    ,otvalor2  : record.get('otvalor2')
                    ,cdatribu3 : record.get('cdatribu3')
                    ,otvalor3  : record.get('otvalor3')
                    ,nmsituac  : record.get('nmsituac')
                    ,cdtipsit  : record.get('cdtipsit')
                });
            });
            
            _p3_smap1['cdperson']  = inputCdpersonap3;
            _p3_smap1['altabaja']  = inputAltabajap3;
            _p3_smap1['confirmar'] = confirmar;
            
            json['smap1']=_p3_smap1;
            
            if(!Ext.isEmpty(_p3_flujo))
            {
                json['flujo'] = _p3_flujo;
            }
            
            debug(json);
            
            if(slist1.length <= 0){
				mensajeWarning('No se modificaron coberturas.');
				return;
            }
            
            _setLoading(true,form);
            myMask = _maskLocal('Cargando...');
            myMask.show();
           
            function endConfirma(json){
            	myMask = _maskLocal('Cargando...');
                myMask.show();
            	json.smap1.confirmar = 'si';
            	confirmar = 'si';
            	
            	if(!Ext.isEmpty(_p3_flujo))
                {
                    json['flujo'] = _p3_flujo;
                }
            	
            	Ext.Ajax.request(
                    {
                        url       : endcobUrlGuardar
                        ,jsonData : json
                        ,timeout  : 180000
                        ,success  : function(response)
                        {
                            _setLoading(false,form);
                            json=Ext.decode(response.responseText);
                            debug(json);
                            if(json.success==true)
                            {
                                var callbackRemesa = function()
                                {
                                    //////////////////////////////////
                                    ////// usa codigo del padre //////
                                    /*//////////////////////////////*/
                                    marendNavegacion(2);
                                    /*//////////////////////////////*/
                                    ////// usa codigo del padre //////
                                    //////////////////////////////////
                                };
                                Ext.Msg.show(
                                {
                                    title   : 'Endoso generado',
                                    msg     : json.mensaje,
                                    buttons : Ext.Msg.OK,
                                    fn      : function()
                                    {
                                        if(confirmar=='si')
                                        {
                                            _generarRemesaClic(
                                                true
                                                ,inputCduniecop3
                                                ,inputCdramop3
                                                ,inputEstadop3
                                                ,inputNmpolizap3
                                                ,callbackRemesa
                                            ); myMask.close();
                                        }
                                        else
                                        {
                                            //////////////////////////////////
                                            ////// usa codigo del padre //////
                                            /*//////////////////////////////*/
                                            marendNavegacion(4); myMask.close();
                                            /*//////////////////////////////*/
                                            ////// usa codigo del padre //////
                                            //////////////////////////////////
                                        }
                                    }
                                });
                            }
                            else
                            {
                                mensajeError(json.error);
                            }
                        }
                        ,failure  : function()
                        {
                            _setLoading(false,form);
                            _setLoading(false,Ext.getCmp('tarifa'))
                            Ext.Msg.show(
                            {
                                title   : 'Error',
                                icon    : Ext.Msg.ERROR,
                                msg     : 'Error de comunicaci&oacute;n',
                                buttons : Ext.Msg.OK
                            });
                        }
                    });
            }
            
            if(confirmar=='auto'){
            	json.smap1.confirmar = 'auto';
            	if(!Ext.isEmpty(_p3_flujo))
                {
                    json['flujo'] = _p3_flujo;
                }
            	Ext.Ajax.request(
                    {
                        url       : endcobUrlGuardar
                        ,jsonData : json
                       // ,timeout  : 180000
                        ,success  : function(response)
                                    {
                                    	
                                        _setLoading(false,form);
                                        jsonpreview=Ext.decode(response.responseText);
                                        debug('**json**',jsonpreview);
                                        if(jsonpreview.success==true)
                                        {
                                            myMask.close();
                                            Ext.create('Ext.window.Window',
                                            {
                                                title        : 'Tarifa final'
                                                ,itemId          : 'tarifa'
                                                ,autoScroll  : true
                                                ,modal       : true
                                                ,buttonAlign : 'center'
                                                ,width       : 600
                                                ,height      : 550
                                                ,defaults    : { width: 650 }
                                                ,closable    : false
                                                ,autoScroll  : true
                                                ,loader      :
                                                    {
                                                        url       : url_PantallaPreview
                                                        ,params   :
                                                            {
                                                                'smap4.nmpoliza'  : inputNmpolizap3
                                                                ,'smap4.cdunieco' : inputCduniecop3
                                                                ,'smap4.cdramo'   : inputCdramop3
                                                                ,'smap4.estado'   : inputEstadop3
                                                                ,'smap4.nmsuplem' : jsonpreview.smap2.pv_nmsuplem_o
                                                                ,'smap4.nsuplogi' : jsonpreview.smap2.pv_nsuplogi_o
                                                            }
                                                        ,scripts  : true
                                                        ,autoLoad : true
                                                     }
                                                ,buttons:[{
                                                            text     : 'Confirmar endoso'
                                                            ,name    : 'endosoButton'
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/award_star_gold_3.png'
                                                            ,handler : function(me){
                                                            	            var form=Ext.getCmp('endoso');
                                                                            debug('***json',json);
                                                                            me.up('window').destroy();
                                                                            endConfirma(json);
                                                                            
                                                                        }
                                                           },
                                                           {
                                                            text    : 'Cancelar'
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/cancel.png'
                                                            ,handler : function (me){
                                                                            me.up('window').destroy();
                                                                            marendNavegacion(2);
                                                                            }
                                                           },{
                                                            text     : 'Documentos'
                                                            ,name    : 'documentoButton'
                                                            ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                                                            ,handler  :function(){
                                                                 var numRand=Math.floor((Math.random()*100000)+1);
                                                                 debug(numRand);
                                                                 centrarVentanaInterna(Ext.create('Ext.window.Window', {
                                                                    title          : 'Vista previa'
                                                                    ,width         : 700
                                                                    ,height        : 500
                                                                    ,collapsible   : true
                                                                    ,titleCollapse : true
                                                                    ,html          : '<iframe innerframe="'+numRand+'" frameborder="0" width="100" height="100"'
                                                                                     +'src="'+_p30_urlViewDoc+"?&path="+_RUTA_DOCUMENTOS_TEMPORAL+"&filename="+jsonpreview.smap2.pdfEndosoNom_o+"\">"
                                                                                     +'</iframe>'
                                                                    ,listeners     : {
                                                                        resize : function(win,width,height,opt){
                                                                            debug(width,height);
                                                                            $('[innerframe="'+numRand+'"]').attr({'width':width-20,'height':height-60});
                                                                    }
                                                                  }}).show());
                                                            }
                                                            ,hidden   : _p3_smap1.TIPOFLOT!= TipoFlotilla.Flotilla? false :true
                                                            } ]
                                             }).show();
                                            
                                                //////////////////////////////////
                                                ////// usa codigo del padre //////
                                                /*//////////////////////////////*/
                                                //marendNavegacion(2);
                                                /*//////////////////////////////*/
                                                ////// usa codigo del padre //////
                                                //////////////////////////////////
                                            
                                            
                                        }
                                        else
                                        {
                                            mensajeError(jsonpreview.error);
                                        }
                                    }
                        ,failure  : function()
                        {
                            _setLoading(false,form);
                            _setLoading(false,Ext.getCmp('tarifa'))
                            Ext.Msg.show(
                            {
                                title   : 'Error',
                                icon    : Ext.Msg.ERROR,
                                msg     : 'Error de comunicaci&oacute;n',
                                buttons : Ext.Msg.OK
                            });
                        }
                });
                           
            } else if (confirmar=='si'){
            	endConfirma(json);
            	 myMask.close();
            }
            
            
            
        } else {
            Ext.Msg.show(
            {
                title   : 'Datos imcompletos',
                icon    : Ext.Msg.WARNING,
                msg     : 'Favor de llenar los campos requeridos',
                buttons : Ext.Msg.OK
            });
        }
    }
    /*///////////////////*/
    ////// funciones //////
    ///////////////////////
    
    Ext.onReady(function() {
    	
    	Ext.Ajax.timeout = 1*60*60*1000; // 1 hora
    	
    	Ext.destroy(Ext.ComponentQuery.query('#grdIncisosp3'));

        /////////////////////
        ////// Modelos //////
        /*/////////////////*/
        Ext.define('CoberturaModel_p3', {
            extend : 'Ext.data.Model',
            fields : [ {
                name : 'GARANTIA'
            }, {
                name : 'NOMBRE_GARANTIA'
            }, {
                name : 'SWOBLIGA'
            }, {
                name : 'SUMA_ASEGURADA'
            }, {
                name : 'CDCAPITA'
            }, {
                name : 'status'
            }, {
                name : 'cdtipbca'
            }, {
                name : 'ptvalbas'
            }, {
                name : 'swmanual'
            }, {
                name : 'swreas'
            }, {
                name : 'cdagrupa'
            }, {
                name : 'ptreduci'
            }, {
                name : 'fereduci',
                type : 'date',
                dateFormat : 'd/m/Y'
            }, {
                name : 'swrevalo'
            }
            ,'cdatribu1'
            ,'otvalor1'
            ,'cdatribu2'
            ,'otvalor2'
            ,'cdatribu3'
            ,'otvalor3'
            ,'nmsituac'
            ,'cdtipsit'
            ]//,
            //idProperty: 'nmsituac'
        });
        
        Ext.define('ModelInciso_p3',{
            extend  : 'Ext.data.Model',
            fields :[
                // De MPOLISIT:
                "CDUNIECO"    , "CDRAMO"   , "ESTADO"     , "NMPOLIZA"
                ,"NMSITUAC"   , "NMSUPLEM" , "STATUS"     , "CDTIPSIT"
                ,"SWREDUCI"   , "CDAGRUPA" , "CDESTADO"   , "CDGRUPO"
                ,"NMSITUAEXT" , "NMSITAUX" , "NMSBSITEXT" , "CDPLAN"
                ,"CDASEGUR"   , "DSGRUPO"
                ,{ name : 'FEFECSIT' , type : 'date' , dateFormat : 'd/m/Y' }
                ,{ name : 'FECHAREF' , type : 'date' , dateFormat : 'd/m/Y' }
                // De TVALOSIT:
                ,'NMSUPLEM_TVAL'
                ,"OTVALOR01" , "OTVALOR02" , "OTVALOR03" , "OTVALOR04" , "OTVALOR05" , "OTVALOR06" , "OTVALOR07" , "OTVALOR08" , "OTVALOR09" , "OTVALOR10"
                ,"OTVALOR11" , "OTVALOR12" , "OTVALOR13" , "OTVALOR14" , "OTVALOR15" , "OTVALOR16" , "OTVALOR17" , "OTVALOR18" , "OTVALOR19" , "OTVALOR20"
                ,"OTVALOR21" , "OTVALOR22" , "OTVALOR23" , "OTVALOR24" , "OTVALOR25" , "OTVALOR26" , "OTVALOR27" , "OTVALOR28" , "OTVALOR29" , "OTVALOR30"
                ,"OTVALOR31" , "OTVALOR32" , "OTVALOR33" , "OTVALOR34" , "OTVALOR35" , "OTVALOR36" , "OTVALOR37" , "OTVALOR38" , "OTVALOR39" , "OTVALOR40"
                ,"OTVALOR41" , "OTVALOR42" , "OTVALOR43" , "OTVALOR44" , "OTVALOR45" , "OTVALOR46" , "OTVALOR47" , "OTVALOR48" , "OTVALOR49" , "OTVALOR50"
                ,"OTVALOR51" , "OTVALOR52" , "OTVALOR53" , "OTVALOR54" , "OTVALOR55" , "OTVALOR56" , "OTVALOR57" , "OTVALOR58" , "OTVALOR59" , "OTVALOR60"
                ,"OTVALOR61" , "OTVALOR62" , "OTVALOR63" , "OTVALOR64" , "OTVALOR65" , "OTVALOR66" , "OTVALOR67" , "OTVALOR68" , "OTVALOR69" , "OTVALOR70"
                ,"OTVALOR71" , "OTVALOR72" , "OTVALOR73" , "OTVALOR74" , "OTVALOR75" , "OTVALOR76" , "OTVALOR77" , "OTVALOR78" , "OTVALOR79" , "OTVALOR80"
                ,"OTVALOR81" , "OTVALOR82" , "OTVALOR83" , "OTVALOR84" , "OTVALOR85" , "OTVALOR86" , "OTVALOR87" , "OTVALOR88" , "OTVALOR89" , "OTVALOR90"
                ,"OTVALOR91" , "OTVALOR92" , "OTVALOR93" , "OTVALOR94" , "OTVALOR95" , "OTVALOR96" , "OTVALOR97" , "OTVALOR98" , "OTVALOR99"
                ,"DSVALOR01" , "DSVALOR02" , "DSVALOR03" , "DSVALOR04" , "DSVALOR05" , "DSVALOR06" , "DSVALOR07" , "DSVALOR08" , "DSVALOR09" , "DSVALOR10"
                ,"DSVALOR11" , "DSVALOR12" , "DSVALOR13" , "DSVALOR14" , "DSVALOR15" , "DSVALOR16" , "DSVALOR17" , "DSVALOR18" , "DSVALOR19" , "DSVALOR20"
                ,"DSVALOR21" , "DSVALOR22" , "DSVALOR23" , "DSVALOR24" , "DSVALOR25" , "DSVALOR26" , "DSVALOR27" , "DSVALOR28" , "DSVALOR29" , "DSVALOR30"
                ,"DSVALOR31" , "DSVALOR32" , "DSVALOR33" , "DSVALOR34" , "DSVALOR35" , "DSVALOR36" , "DSVALOR37" , "DSVALOR38" , "DSVALOR39" , "DSVALOR40"
                ,"DSVALOR41" , "DSVALOR42" , "DSVALOR43" , "DSVALOR44" , "DSVALOR45" , "DSVALOR46" , "DSVALOR47" , "DSVALOR48" , "DSVALOR49" , "DSVALOR50"
                ,"DSVALOR51" , "DSVALOR52" , "DSVALOR53" , "DSVALOR54" , "DSVALOR55" , "DSVALOR56" , "DSVALOR57" , "DSVALOR58" , "DSVALOR59" , "DSVALOR60"
                ,"DSVALOR61" , "DSVALOR62" , "DSVALOR63" , "DSVALOR64" , "DSVALOR65" , "DSVALOR66" , "DSVALOR67" , "DSVALOR68" , "DSVALOR69" , "DSVALOR70"
                ,"DSVALOR71" , "DSVALOR72" , "DSVALOR73" , "DSVALOR74" , "DSVALOR75" , "DSVALOR76" , "DSVALOR77" , "DSVALOR78" , "DSVALOR79" , "DSVALOR80"
                ,"DSVALOR81" , "DSVALOR82" , "DSVALOR83" , "DSVALOR84" , "DSVALOR85" , "DSVALOR86" , "DSVALOR87" , "DSVALOR88" , "DSVALOR89" , "DSVALOR90"
                ,"DSVALOR91" , "DSVALOR92" , "DSVALOR93" , "DSVALOR94" , "DSVALOR95" , "DSVALOR96" , "DSVALOR97" , "DSVALOR98" , "DSVALOR99"
                // De MPERSONA:
                ,"CDPERSON"    , "CDTIPIDE"  , "CDIDEPER"   , "DSNOMBRE"
                ,"CDTIPPER"    , "OTFISJUR"  , "OTSEXO"     , "CDRFC"
                ,"FOTO"        , "DSEMAIL"   , "DSNOMBRE1"  , "DSAPELLIDO"
                ,"DSAPELLIDO1" , "CDNACION"  , "DSCOMNOM"   , "DSRAZSOC"
                ,"DSNOMUSU"    , "CDESTCIV"  , "CDGRUECO"   , "CDSTIPPE"
                ,"NMNUMNOM"    , "CURP"      , "CANALING"   , "CONDUCTO"
                ,"PTCUMUPR"    , "STATUSPER" , "RESIDENCIA" , "NONGRATA"
                ,"CDIDEEXT"    , "CDSUCEMI"
                ,{ name : 'FENACIMI'  , type : 'date' , dateFormat : 'd/m/Y' }
                ,{ name : 'FEINGRESO' , type : 'date' , dateFormat : 'd/m/Y' }
                ,{ name : 'FEACTUAL'  , type : 'date' , dateFormat : 'd/m/Y' }
                // De MPOLIPER:
                ,"CDROL" , "NMORDDOM" , "SWRECLAM" , "SWEXIPER" , "CDPARENT" , "PORBENEF"
                // CUSTOM
                ,'ATRIBUTOS','NOMBRECOMPLETO'
            ]
        });
        
        // Si el origen es el marco general de endosos, obtenemos los incisos de sus parametros:
        if(pantallaOrigen == 'MARCO_ENDOSOS_GENERAL') {
            // Seteamos los nuevos campos para el modelo:
            var arrAtributos = [];
            debug('paramsIncisos:', Ext.Object.getKeys(paramsincisos));
            debug('keys:', Ext.Object.getKeys(paramsincisos[0]));
            Ext.each(Ext.Object.getKeys(paramsincisos[0]), function(name) {
                arrAtributos.push(name);
            });
            Ext.ModelManager.getModel('ModelInciso_p3').setFields(arrAtributos);
        }
        /*/////////////////*/
        ////// Modelos //////
        /////////////////////
        ////////////////////
        ////// Stores //////
        /*////////////////*/
        storeCoberturasActuales_p3 = Ext.create('Ext.data.Store', {
            storeId : 'storeCoberturasActuales_p3',
            model : 'CoberturaModel_p3',
            proxy : {
                type : 'ajax',
                url : urlCargarCoberturasp3,
                extraParams : {
                    'smap1.pv_cdunieco_i' : inputCduniecop3,
                    'smap1.pv_cdramo_i' : inputCdramop3,
                    'smap1.pv_estado_i' : inputEstadop3,
                    'smap1.pv_nmpoliza_i' : inputNmpolizap3
                },
                reader : {
                    type : 'json',
                    root : 'slist1'
                }
            },
            autoLoad : false,

        });
        
        storeCoberturasEditadas_p3 = Ext.create('Ext.data.Store', {
            storeId : 'storeCoberturasEditadas_p3',
            model : 'CoberturaModel_p3'
        });
        
        storeCoberturasDisponibles_p3 = Ext.create('Ext.data.Store', {
            storeId : 'storeCoberturasDisponibles_p3',
            model : 'CoberturaModel_p3',
            proxy : {
                type : 'ajax',
                url : urlCargarCoberturasDispp3,
                extraParams : {
                    'smap1.pv_cdunieco_i' : inputCduniecop3,
                    'smap1.pv_cdramo_i' : inputCdramop3,
                    'smap1.pv_estado_i' : inputEstadop3,
                    'smap1.pv_nmpoliza_i' : inputNmpolizap3,
                    'smap1.pv_cdsisrol_i' : CD_ROL_ACTUAL
                },
                reader : {
                    type : 'json',
                    root : 'slist1'
                }
            },
            autoLoad : false
        });
        
        // Se cargan los incisos según la pantalla origen:
        storeIncisos_p3 = Ext.create('Ext.data.Store', {
            model: 'ModelInciso_p3',
            autoLoad: false
        });
        // Si el origen es el marco general de endosos, obtenemos los incisos de sus parametros:
        if(pantallaOrigen == 'MARCO_ENDOSOS_GENERAL') {
        	
        	storeIncisos_p3.setProxy({
                type   : 'memory',
                reader :{
                    type : 'json',
                    root : 'slist1'
                }
            });
            
            storeIncisos_p3.add(paramsincisos);
            
            storeIncisos_p3.each(function(record, index) {
            	// Cargamos todas las coberturas actuales y disponibles para esos incisos:
                storeCoberturasActuales_p3.load({
                    addRecords: true,
                    params: {'smap1.pv_nmsituac_i' : record.get("NMSITUAC")},
                    callback: function(records, operation, success) {
                    	
                        // VALIDACION: Si es Suscriptor y es un Endoso de Baja, permitimos eliminar todas las coberturas (SWOBLIGA='N'):
                        if (inputAltabajap3 === 'baja'
                            && (
                                CD_ROL_ACTUAL == CD_ROL_SUSCRIPTOR
                                || RolSistema.puedeSuscribirAutos(CD_ROL_ACTUAL)
                            )) {
                            Ext.Array.each(records, function(item, index, allItems) {
                                item.set('SWOBLIGA','N');
                            });
                            storeCoberturasActuales_p3.commitChanges();
                        }
                    }
                });
                
                storeCoberturasDisponibles_p3.load({
                    addRecords: true,
                    params: {'smap1.pv_nmsituac_i' : record.get("NMSITUAC")}
                });
            });
        } else { // Sino hacemos la consulta:
        	
        	storeIncisos_p3 = Ext.create('Ext.data.Store', {
                model    : 'ModelInciso_p3',
                autoLoad : false,
                proxy    : {
                    type   : 'ajax',
                    url    : urlRecuperacionSimpleListap3,
                    extraParams :{
                        'smap1.procedimiento' : 'RECUPERAR_INCISOS_POLIZA_GRUPO_FAMILIA',
                        'smap1.cdunieco'      : inputCduniecop3,
                        'smap1.cdramo'        : inputCdramop3,
                        'smap1.estado'        : inputEstadop3,
                        'smap1.nmpoliza'      : inputNmpolizap3,
                        'smap1.cdgrupo'       : '',
                        'smap1.nmfamili'      : '',
                        'smap1.nivel'         : 'DUMMY'
                    },
                    reader :{
                        type             : 'json',
                        root            : 'slist1',
                        successProperty : 'success',
                        messageProperty : 'respuesta'
                    }
                }
            });
            storeIncisos_p3.load({
                callback: function(records, operation, success) {
                    if(success){
                    	
                    	///////////////// Para llenar los datos de incisos a mostrar: ///////////////////
                    	// Seteamos los nuevos campos para el modelo:
                        var arrAtributos = [];
                        var keys = Ext.Object.getKeys(records[0].raw);
                        //debug('paramsIncisos:', Ext.Object.getKeys(records));
                        //debug('keys:', keys);
                        Ext.each(keys, function(name) {
                            arrAtributos.push(name);
                        });
                        Ext.ModelManager.getModel('ModelInciso_p3').setFields(arrAtributos);
                        ///////////////////////////////////////////////////////////////////////
                    	
                        Ext.Array.each(records, function(record, index, recordsItSelf) {
                        	
                        	///////////////// Para llenar los datos de incisos a mostrar: ///////////////////
                        	Ext.Array.each(keys, function(item, index, allItems) {
                        		if(item.slice(0,4)=='CVE_'||item.slice(0,4)=='DES_')
                        		{
                        		    record.set(item, record.raw[item]);
                        		}
                            });
                            ///////////////////////////////////////////////////////////////////////
                            
                            debug('record data nuevo:',record.data);
                        	
                        	// Cargamos todas las coberturas actuales y disponibles para esos incisos:
                            storeCoberturasActuales_p3.load({
                                addRecords: true,
                                params: {'smap1.pv_nmsituac_i' : record.get("NMSITUAC")},
                                callback: function(records, operation, success) {
                                    
                                    // VALIDACION: Si es Suscriptor y es un Endoso de Baja, permitimos eliminar todas las coberturas (SWOBLIGA='N'):
                                	if (inputAltabajap3 === 'baja'
                                	    && (
                                	        CD_ROL_ACTUAL == CD_ROL_SUSCRIPTOR
                                	        || RolSistema.puedeSuscribirAutos(CD_ROL_ACTUAL)
                                	    )) {
                                	    Ext.Array.each(records, function(item, index, allItems) {
                                	        item.set('SWOBLIGA','N');
                                	    });
                                	    storeCoberturasActuales_p3.commitChanges();
                                	}
                                }
                            });
                            
                            storeCoberturasDisponibles_p3.load({
                                addRecords: true,
                                params: {'smap1.pv_nmsituac_i' : record.get("NMSITUAC")}
                            });
                        });
                    } else {
                        showMessage('Error', 'No hay incisos para la p&oacute;liza', Ext.Msg.OK, Ext.Msg.ERROR)
                    }
                }
            });
        }
        /*////////////////*/
        ////// Stores //////
        ////////////////////
        
        /////////////////////////
        ////// Componentes //////
        /*/////////////////////*/
        /*/////////////////////*/
        ////// Componentes //////
        /////////////////////////
        
        ///////////////////////
        ////// Contenido //////
        /*///////////////////*/
        Ext.create('Ext.panel.Panel', {
            defaults :{
                style : 'margin : 5px;'
            },
            renderTo : 'pan_usu_cob_divgrid',
            border   : 0,
            items    :
            [{
                layout: 'hbox',
                items: [
                	{
                    //Ext.create('Ext.grid.Panel',{
                        xtype    : 'grid',
                        itemId   : 'grdIncisosp3',
                        columns  : columnasTatrisit,
                        width    : 910,
                        selModel : {
                            selType   : 'checkboxmodel',
                            mode      : 'SINGLE',
                            listeners : {
                                selectionchange : function(me, selected, eOpts) {
                                    debug('nmsituac seleccionado:', me.getSelection()[0].get('NMSITUAC'));
                                    
                                    //Filtramos el contenido de las coberturas de acuerdo al inciso elegido:
                                    var nmsituacFiltro = me.getSelection()[0].get('NMSITUAC');
                                    var funcionFiltro  = function(record)
                                    {
                                        return Number(record.get('nmsituac'))===Number(nmsituacFiltro);
                                    };
                                    storeCoberturasDisponibles_p3.clearFilter();
                                    storeCoberturasDisponibles_p3.filterBy(funcionFiltro);
                                    
                                    storeCoberturasActuales_p3.clearFilter();
                                    storeCoberturasActuales_p3.filterBy(funcionFiltro);
                                    
                                    storeCoberturasEditadas_p3.clearFilter();
                                    storeCoberturasEditadas_p3.filterBy(funcionFiltro);
                                }
                            }
                        },
                        store : storeIncisos_p3
                    //})
                    }
                ]
            },{
                layout   : {
                    type    : 'table',
                    columns : 2
                },
                items: [
                    Ext.create('Ext.grid.Panel', {
                        title          : 'Coberturas actuales'
                        ,icon          : '${ctx}/resources/fam3icons/icons/accept.png'
                        ,store         : storeCoberturasActuales_p3
                        ,buttonAlign   : 'center'
                        ,titleCollapse : true
                        ,collapsible   : false
                        ,height        : 250
                        ,width         : 455
                        ,tools         :
                        [
                           {
                               type     : 'help'
                               ,tooltip : 'Coberturas que tiene actualmente el asegurado'
                           }
                        ]
                        ,columns       :
                        [
                            {
                                header     : 'Cobertura'
                                ,dataIndex : 'NOMBRE_GARANTIA'
                                ,width     : 265
                            }
                            ,{
                                menuDisabled : true
                                ,width       : 30
                                ,dataIndex   : 'SWOBLIGA'
                                ,renderer    : function(value)
                                {
                                    var rvalue = '';
                                    if (value == 'N'&&inputAltabajap3=='baja')
                                    {
                                        rvalue = '<img src="${ctx}/resources/fam3icons/icons/delete.png" data-qtip="Eliminar" style="cursor:pointer;">';
                                    }
                                    return rvalue;
                                }
                            }
                            ,{
                                header     : 'Suma asegurada'
                                ,dataIndex : 'SUMA_ASEGURADA'
                                ,width     : 110
                            }
                            ,{
                                header     : 'No.'
                                ,dataIndex : 'nmsituac'
                                ,width     : 15
                                ,hidden    : true
                            }
                        ]
                        ,listeners :
                        {
                            cellclick : function(grid, td, cellIndex, record)
                            {
                                debug('cellclick');
                                debug('grid=', Ext.ComponentQuery.query('#grdIncisosp3'));
                                debug('grid selModel=', Ext.ComponentQuery.query('#grdIncisosp3')[0].getSelectionModel());
                                var hayIncisoSeleccionado = Ext.ComponentQuery.query('#grdIncisosp3')[0].getSelectionModel().hasSelection();
                                var incisoSelected =        Ext.ComponentQuery.query('#grdIncisosp3')[0].getSelectionModel().getSelection()[0];
                                debug('inciso seleccionado?', hayIncisoSeleccionado);
                                debug('incisoSelected=', incisoSelected);
                                debug('cellIndex=', cellIndex);
                                
                                try{
                        			if(_p3_smap1.CDRAMO==Ramo.ServicioPublico &&
                        			   _p3_smap1.cdtipsup == TipoEndoso.BajaCoberturas){ 
                           				var garant=$.grep(slist2,function(ele){
                           					return ele.cdgarant==record.get("GARANTIA")
                           				});
                           				
                       					if(garant[0].SWOBLIG == "1"){
                       						mensajeWarning('Esta cobertura es incancelable');
                       						return;
                       					}
                        			
                            		}
                        		}catch(e){
                        			debugError(e)
                        		}
                                
                                
                                if(cellIndex==1 && hayIncisoSeleccionado) {
                                	if(record.get('SWOBLIGA')=='N' && inputAltabajap3=='baja') {
                                        storeCoberturasEditadas_p3.add(record);
                                        storeCoberturasActuales_p3.remove(record)
                                    }
                                } else {
                                    mensajeWarning('Debe seleccionar un inciso para continuar');
                                }
                            }
                        }
                    }),
                    Ext.create('Ext.grid.Panel', {
                        title          : 'Coberturas eliminadas'
                        ,icon          : '${ctx}/resources/fam3icons/icons/delete.png'
                        ,store         : storeCoberturasEditadas_p3
                        ,buttonAlign   : 'center'
                        ,hidden        : inputAltabajap3=='alta'
                        ,titleCollapse : true
                        ,collapsible   : false
                        ,tools         :
                        [
                            {
                                type     : 'help'
                                ,tooltip : 'Coberturas que ten&iacute;a el asegurado y ser&aacute;n eliminadas'
                            }
                        ]
                        ,height        : 250
                        ,width         : 455
                        ,columns       :
                        [
                            {
                                header     : 'Cobertura'
                                ,dataIndex : 'NOMBRE_GARANTIA'
                                ,width     : 265
                            }
                            ,{
                                menuDisabled : true
                                ,width       : 30
                                ,icon        : '${ctx}/resources/fam3icons/icons/cancel.png'
                                ,renderer    : function(value)
                                {
                                    return '<img src="${ctx}/resources/fam3icons/icons/control_rewind_blue.png" data-qtip="Volver a agregar" style="cursor:pointer;">';
                                }
                            }
                            ,{
                                header     : 'Suma asegurada'
                                ,dataIndex : 'SUMA_ASEGURADA'
                                ,width     : 110
                            }
                            ,{
                                header     : 'No.'
                                ,dataIndex : 'nmsituac'
                                ,width     : 15
                                ,hidden    : true
                            }
                        ]
                        ,listeners :
                        {
                            cellclick : function(grid, td, cellIndex, record)
                            {
                                debug('cellclick');
                                if(cellIndex==1)
                                {
                                    storeCoberturasActuales_p3.add(record);
                                    storeCoberturasEditadas_p3.remove(record);
                                }
                            }
                        }
                    })
                    ,Ext.create('Ext.grid.Panel',
                    {
                        title          : 'Coberturas disponibles'
                        ,icon          : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,titleCollapse : true
                        ,collapsible   : false
                        ,hidden        : inputAltabajap3=='baja'
                        ,tools         :
                        [
                           {
                               type     : 'help'
                               ,tooltip : 'Coberturas disponibles para agregar al asegurado'
                           }
                        ]
                        ,store         : storeCoberturasDisponibles_p3
                        ,height        : 250
                        ,width         : 455
                        ,columns       :
                        [
                            {
                                header     : 'Cobertura'
                                ,dataIndex : 'NOMBRE_GARANTIA'
                                ,width     : 265
                            }
                            ,{
                                menuDisabled : true
                                ,width       : 30
                                ,renderer    : function(value)
                                {
                                    return '<img src="${ctx}/resources/fam3icons/icons/add.png" data-qtip="Agregar" style="cursor:pointer;">';
                                }
                            }
                            ,{
                                header     : 'Suma asegurada'
                                ,dataIndex : 'SUMA_ASEGURADA'
                                ,width     : 110
                                ,hidden    : true
                            }
                            ,{
                                header     : 'No.'
                                ,dataIndex : 'nmsituac'
                                ,width     : 15
                                ,hidden    : true
                            }
                         ]
                         ,listeners :
                         {
                             cellclick : function(grid, td, cellIndex, record)
                             {
                             	 debug('cellclick');
                                 debug('cdtipsit elegido=', record.get('cdtipsit'));
                                 debug('grid=', Ext.ComponentQuery.query('#grdIncisosp3'));
                                 debug('grid selModel=', Ext.ComponentQuery.query('#grdIncisosp3')[0].getSelectionModel());
                                 var hayIncisoSeleccionado = Ext.ComponentQuery.query('#grdIncisosp3')[0].getSelectionModel().hasSelection();
                                 var incisoSelected =        Ext.ComponentQuery.query('#grdIncisosp3')[0].getSelectionModel().getSelection()[0];
                                 debug('inciso seleccionado?', hayIncisoSeleccionado);
                                 debug('incisoSelected=', incisoSelected);
                                 debug('cellIndex=', cellIndex);
                                 if(cellIndex==1 && hayIncisoSeleccionado)
                                 {
                                	 debug('cellIndex==1 && hayIncisoSeleccionado:',cellIndex,hayIncisoSeleccionado);
                                     Ext.Ajax.request(
                                     {
                                         url     : _endcob_urlObtenerComponenteSituacionCobertura
                                         ,params :
                                         {
                                             'smap1.cdramo'    : inputCdramop3
                                             ,'smap1.cdtipsit' : record.get('cdtipsit')
                                             ,'smap1.cdgarant' : record.get('GARANTIA')
                                             ,'smap1.cdtipsup' : inputAltabajap3=='alta'?'6':'7'
                                         }
                                         ,success : function(response)
                                         {
                                             var json = Ext.decode(response.responseText);
                                             debug('### obtener componente situacion Vil  cobertura:',json);
                                             if(json.exito)
                                             {
                                            	 if('040'.lastIndexOf(record.get('GARANTIA'))!=-1)
                                                 {
                                                   debug('BENEFICIARIOS: ',inputCduniecop3,inputCdramop3,inputEstadop3,inputNmpolizap3);
                                                   
                                                   centrarVentanaInterna(
                                                         Ext.create('Ext.window.Window',
                                                         {
                                                              title      : 'Al agregar seguro de Vida, usted '
                                                             ,modal      : true
                                                             ,width      : 1000
                                                             ,minHeight  : 300
                                                             ,itemId     : '_p29_BeneficiarioPanel'
                                                             ,height     : 300
                                                             ,autoScroll : false
                                                             ,loader:
                                                             {
                                                                 url : urlPantallaBeneficiarios
                                                                 ,params   :
                                                                 {
                                                                     'smap1.cdunieco'      : inputCduniecop3
                                                                     ,'smap1.cdramo'       : inputCdramop3
                                                                     ,'smap1.estado'       : inputEstadop3
                                                                     ,'smap1.nmpoliza'     : inputNmpolizap3
                                                                     ,'smap1.nmsuplem'     : '1'
                                                                     ,'smap1.nmsituac'     : '0'
                                                                     ,'smap1.cdrolPipes'   : '3'
                                                                     ,'smap1.cdtipsup'     : '1'
                                                                     ,'smap1.ultimaImagen' : 'N'
                                                                 }
                                                                 ,autoLoad:true
                                                                 ,scripts:true
                                                             }
                                                         }).show());
                                                   
                                                   debug('Vil inputCdpersonap3: ',incisoSelected.data.CVE_CONDUCTOR);
                                                   mensajeCorrecto('Aviso', 'En caso de que en la emisión original de la póliza se haya declarado que el contratante ha estado expuesto o padecido una enfermedad crónica o grave en los últimos 12 meses, la cobertura VIDAUTO no se amparará.');
                                                   
                                                 }
                                            	 
                                                 if(json.smap1.CONITEM=='true')
                                                 {
                                                     centrarVentanaInterna(
                                                         Ext.create('Ext.window.Window',
                                                         {
                                                             title   : 'Valor de cobertura'
                                                             ,modal  : true
                                                             ,width  : 300
                                                             ,minHeight : 150
                                                             ,items  : Ext.decode('['+json.smap1.item+']')
                                                             ,buttonAlign : 'center'
                                                             ,buttons     :
                                                             [
                                                                 {
                                                                     text     : 'Aceptar'
                                                                     ,icon    : '${ctx}/resources/fam3icons/icons/accept.png'
                                                                     ,handler : function(me)
                                                                     {
                                                                         var item1 = me.up().up().items.items[0];
                                                                         var item2 = me.up().up().items.items[1];
                                                                         var item3 = me.up().up().items.items[2];
                                                                         
                                                                         var valido = true;
                                                                         if(!Ext.isEmpty(item1))
                                                                         {
                                                                             valido = valido && !Ext.isEmpty(item1.getValue());
                                                                         }
                                                                         if(!Ext.isEmpty(item2))
                                                                         {
                                                                             valido = valido && !Ext.isEmpty(item2.getValue());
                                                                         }
                                                                         if(!Ext.isEmpty(item3))
                                                                         {
                                                                             valido = valido && !Ext.isEmpty(item3.getValue());
                                                                         }
                                                                         
                                                                         if(valido)
                                                                         {
                                                                             storeCoberturasEditadas_p3.add(record);
                                                                             storeCoberturasDisponibles_p3.remove(record);
                                                                             
                                                                             if(!Ext.isEmpty(item1))
                                                                             {
                                                                                 record.set('cdatribu1' , item1.cdatribu);
                                                                                 record.set('otvalor1'  , item1.getValue());
                                                                             }
                                                                             if(!Ext.isEmpty(item2))
                                                                             {
                                                                                 record.set('cdatribu2' , item2.cdatribu);
                                                                                 record.set('otvalor2'  , item2.getValue());
                                                                             }
                                                                             if(!Ext.isEmpty(item3))
                                                                             {
                                                                                 record.set('cdatribu3' , item3.cdatribu);
                                                                                 record.set('otvalor3'  , item3.getValue());
                                                                             }
                                                                             
                                                                             debug('record:',record);
                                                                             me.up().up().destroy();
                                                                         }
                                                                     }
                                                                 }
                                                             ]
                                                         }).show()
                                                     );
                                                 }
                                                 else
                                                 {
                                                     storeCoberturasEditadas_p3.add(record);
                                                     storeCoberturasDisponibles_p3.remove(record);
                                                 }
                                             }
                                             else
                                             {
                                                 mensajeError(json.respuesta);
                                             }
                                         }
                                         ,failure : function()
                                         {
                                             errorComunicacion();
                                         }
                                     });
                                 } else {
                                    mensajeWarning('Debe seleccionar un inciso para continuar');
                                 }
                             }
                         }
                    })
                    ,Ext.create('Ext.grid.Panel',
                    {
                        title          : 'Coberturas agregadas'
                        ,icon          : '${ctx}/resources/fam3icons/icons/add.png'
                        ,store         : storeCoberturasEditadas_p3
                        ,buttonAlign   : 'center'
                        ,colspan       : inputAltabajap3=='alta'?2:1
                        ,titleCollapse : true
                        ,collapsible   : false
                        ,hidden        : inputAltabajap3=='baja'
                        ,height        : 250
                        ,width         : 455
                        ,tools         :
                        [
                           {
                               type     : 'help'
                               ,tooltip : 'Nuevas coberturas que se van a agregar al asegurado'
                           }
                        ]
                        ,columns       :
                        [
                            {
                                header     : 'Cobertura'
                                ,dataIndex : 'NOMBRE_GARANTIA'
                                ,width     : 265
                            }
                            ,{
                                menuDisabled : true
                                ,width       : 30
                                ,renderer    : function(value)
                                {
                                    return '<img src="${ctx}/resources/fam3icons/icons/delete.png" data-qtip="No agregar" style="cursor:pointer;">';
                                }
                            }
                            ,{
                                header     : 'Suma asegurada'
                                ,dataIndex : 'SUMA_ASEGURADA'
                                ,width     : 110
                                ,hidden    : true
                            }
                            ,{
                                header     : 'No.'
                                ,dataIndex : 'nmsituac'
                                ,width     : 15
                                ,hidden    : true
                            }
                        ]
                        ,listeners :
                        {
                            cellclick : function(grid, td, cellIndex, record)
                            {
                                debug('cellclick');
                                if(cellIndex==1)
                                {
                                    storeCoberturasDisponibles_p3.add(record);
                                    storeCoberturasEditadas_p3.remove(record);
                                }
                            }
                        }
                    })
                    ,Ext.create('Ext.form.Panel',
                    {
                        title        : 'Informaci&oacute;n del endoso'
                        ,id          : 'endoso' 
                        ,heigth      : 200
                        ,buttonAlign : 'center'
                        ,style       : 'margin : 5px; margin-bottom : 200px;'
                        ,colspan     : 2
                        ,layout      :
                        {
                            type     : 'table'
                            ,columns : 2
                        }
                        ,defaults    :
                        {
                            style : 'margin : 5px;'
                        }
                        ,items       :
                        [
                            {
                                xtype       : 'datefield'
                                ,fieldLabel : 'Fecha de efecto'
                                ,format     : 'd/m/Y'
                                ,value      : new Date()
                                ,allowBlank : false
                                ,name       : 'pv_fecha_i'
                            }
                        ]
                        ,buttons     :
                        [
                            {
                                text     : 'Guardar endoso'
                                ,icon    : '${ctx}/resources/fam3icons/icons/disk.png'
                                ,hidden  : true
                                ,handler : function(me)
                                {
                                    var form=me.up().up();
                                    endcobSumit(form,'no');
                                }
                            }
                            ,{
                                text     : 'Emitir'
                                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                                ,handler : function(){ var form=Ext.getCmp('endoso');
                                		                debug('endcobSumitpreview',form);
                                		                if(   inputCdramop3 == Ramo.AutosFronterizos
												           || inputCdramop3 == Ramo.ServicioPublico
												           || inputCdramop3 == Ramo.AutosResidentes)
												           	{
												           		//endcobSumitpreview(form,'no');
												           		endcobSumit(form,'auto');
												           	}else
												           		{
												           			endcobSumit(form,'si');
												           		}
                                		                
                                						
                                						
                                				      }
                            
                             }
                            /* ,{
                                text     : 'Confirmar endoso'
                                ,icon    : '${ctx}/resources/fam3icons/icons/key.png'
                                ,handler : function(me)
                                {
                                    var form=me.up().up();
                                    endcobSumit(form,'si');
                                }
                            }*/                           
                            ,{
                                text     : 'Documentos'
                                ,icon    : '${ctx}/resources/fam3icons/icons/printer.png'
                                ,handler : function()
                                {
                                    Ext.create('Ext.window.Window',
                                    {
                                        title        : 'Documentos del tr&aacute;mite '+inputNtramitep3
                                        ,modal       : true
                                        ,buttonAlign : 'center'
                                        ,width       : 600
                                        ,height      : 400
                                        ,autoScroll  : true
                                        ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                        ,loader      :
                                        {
                                            url       : endcobUrlDoc
                                            ,params   :
                                            {
                                                'smap1.nmpoliza'  : inputNmpolizap3
                                                ,'smap1.cdunieco' : inputCduniecop3
                                                ,'smap1.cdramo'   : inputCdramop3
                                                ,'smap1.estado'   : inputEstadop3
                                                ,'smap1.nmsuplem' : '0'
                                                ,'smap1.ntramite' : inputNtramitep3
                                                ,'smap1.nmsolici' : ''
                                                ,'smap1.tipomov'  : '0'
                                            }
                                            ,scripts  : true
                                            ,autoLoad : true
                                        }
                                    }).show();
                                }
                            }
                        ]
                    })
                ]
            }]
        });
        /*///////////////////*/
        ////// Contenido //////
        ///////////////////////
        
        ////// custom //////
        ////// custom //////
        
        ////// loader //////
        // Para AUTOS RESIDENTES (cdramo 5) recuperamos las fechas limites de endoso:
        if(inputCdramop3 == CD_RAMO_AUTOS_RESIDENTES) {
        	
        	Ext.Ajax.request(
            {
                url      : urlRecuperacionSimplep3
                ,params  :
                {
                    'smap1.procedimiento' : 'RECUPERAR_FECHAS_LIMITE_ENDOSO'
                    ,'smap1.cdunieco'     : inputCduniecop3
                    ,'smap1.cdramo'       : inputCdramop3
                    ,'smap1.estado'       : inputEstadop3
                    ,'smap1.nmpoliza'     : inputNmpolizap3
                    ,'smap1.cdtipsup'     : inputAltabajap3=='alta'?'6':'7'
                }
                ,success : function(response)
                {
                    var json = Ext.decode(response.responseText);
                    debug('### fechas:',json);
                    if(json.exito)
                    {
                        _fieldByName('pv_fecha_i').setMinValue(json.smap1.FECHA_MINIMA);
                        _fieldByName('pv_fecha_i').setMaxValue(json.smap1.FECHA_MAXIMA);
                        _fieldByName('pv_fecha_i').setReadOnly(json.smap1.EDITABLE=='N');
                        _fieldByName('pv_fecha_i').isValid();
                    }
                    else
                    {
                        mensajeError(json.respuesta);
                    }
                }
                ,failure : function()
                {
                    errorComunicacion();
                }
            });
        }
        ////// loader //////
    });
<%@ include file="/jsp-script/proceso/documentos/scriptImpresionRemesaEmisionEndoso.jsp"%>
</script>
<div id="pan_usu_cob_divgrid"></div>