<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Endosos</title>
<script>
    ///////////////////////
    ////// variables //////
    /*///////////////////*/
    var marendurlcata             = '<s:url namespace="/catalogos"  action="obtieneCatalogo" />';
    var marendurlramos            = '<s:url namespace="/"           action="obtenerRamos" />';
    var marendUrlFiltro           = '<s:url namespace="/endosos"    action="obtenerEndosos" />'
    var marenUrlObtenerAsegurados = '<s:url namespace="/"           action="cargarComplementariosAsegurados" />';
    var marendUrlDoc              = '<s:url namespace="/documentos" action="ventanaDocumentosPoliza" />';
    var marendStorePolizas;
    var marendStoreAsegurados;
    var marendStoreLigas;
    /*///////////////////*/
    ////// variables //////
    ///////////////////////
    
    ///////////////////////
    ////// funciones //////
    /*///////////////////*/
    function marendMostrarControlesFiltro(tipo)
    {
        Ext.getCmp('marendFilForm').getForm().reset();
        debug('marendMostrarControlesFiltro',tipo);
        if(tipo==1)
        {
        	Ext.getCmp('marendFilNmpoliex').show();
        	Ext.getCmp('marendFilRfc').hide();
        	Ext.getCmp('marendFilCdperson').hide();
        	Ext.getCmp('marendFilNombre').hide();
        }
        else if(tipo==2)
        {
            Ext.getCmp('marendFilNmpoliex').hide();
            Ext.getCmp('marendFilRfc').show();
            Ext.getCmp('marendFilCdperson').hide();
            Ext.getCmp('marendFilNombre').hide();
        }
        else if(tipo==3)
        {
            Ext.getCmp('marendFilNmpoliex').hide();
            Ext.getCmp('marendFilRfc').hide();
            Ext.getCmp('marendFilCdperson').show();
            Ext.getCmp('marendFilNombre').hide();
        }
        else if(tipo==4)
        {
            Ext.getCmp('marendFilNmpoliex').hide();
            Ext.getCmp('marendFilRfc').hide();
            Ext.getCmp('marendFilCdperson').hide();
            Ext.getCmp('marendFilNombre').show();
        }
    }
    
    function marendValidaOperacion(recordOperacion)
    {
    	if(recordOperacion.get('funcion')=='endosocoberturasalta')
    	{
    		debug('endososcoberturas');
    		var nAsegActivos=0;
    		var recordActivo;
    		marendStoreAsegurados.each(function(record)
	        {
	            if(record.get('activo')==true)
	            {
	            	nAsegActivos=nAsegActivos+1;
	            	recordActivo=record;
	            }
	        });
	        /*
    		if(nAsegActivos==1)
    		{
    			if(recordActivo.get('cdrol')==2)
    			{
    			*/
    				Ext.getCmp('marendMenuOperaciones').collapse();
                    Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
	    			Ext.getCmp('marendLoaderFrame').getLoader().load(
	                {
	                    url       : recordOperacion.get('liga')
	                    ,scripts  : true
	                    ,autoLoad : true
	                    ,jsonData :
	                    {
	                    	smap1 :
	                    	{
	                    	    CDUNIECO      : recordActivo.get('CDUNIECO')
	                    	    ,CDRAMO       : recordActivo.get('CDRAMO')
	                    	    ,ESTADO       : recordActivo.get('ESTADO')
	                    	    ,NMPOLIZA     : recordActivo.get('NMPOLIZA')
	                    	    //,pv_nmsituac     : recordActivo.get('nmsituac')
	                    	    ,CDPERSON     : recordActivo.get('cdperson')
	                    	    ,cdrfc           : recordActivo.get('cdrfc')
	                    	    ,pv_cdrol        : recordActivo.get('cdrol')
	                    	    ,nombreAsegurado : recordActivo.get('nombrecompleto')
	                    	    ,NTRAMITE        : recordActivo.get('NTRAMITE')
	                    	    ,botonCopiar     : '0'
	                    	    ,altabaja        : 'alta'
	                    	    ,cdtipsit        : recordActivo.get('CDTIPSIT')
	                    	    ,fenacimi        : Ext.Date.format(recordActivo.get('fenacimi'), 'd/m/Y')
	                    	}
	                    }
	                });
    			/*
    			}
    			else
   				{
    				Ext.Msg.show(
	                {
	                    title    : 'Error'
	                    ,icon    : Ext.Msg.WARNING
	                    ,msg     : 'No hay coberturas para el cliente, por favor seleccione un asegurado'
	                    ,buttons : Ext.Msg.OK
	                });
   				}
    		}
    		else
    		{
    			Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione un asegurado'
                    ,buttons : Ext.Msg.OK
                });
    		}
    		*/
    	}
    	else if(recordOperacion.get('funcion')=='endosocoberturasbaja')
        {
            debug('endososcoberturas');
            var nAsegActivos=0;
            var recordActivo;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;
                }
            });
            /*
            if(nAsegActivos==1)
            {
                if(recordActivo.get('cdrol')==2)
                {
                */
                    console.log('recordActivo=', recordActivo);
                    Ext.getCmp('marendMenuOperaciones').collapse();
                    Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                    Ext.getCmp('marendLoaderFrame').getLoader().load(
                    {
                        url       : recordOperacion.get('liga')
                        ,scripts  : true
                        ,autoLoad : true
                        ,jsonData :
                        {
                            smap1 :
                            {
                                CDUNIECO      : recordActivo.get('CDUNIECO')
                                ,CDRAMO       : recordActivo.get('CDRAMO')
                                ,ESTADO       : recordActivo.get('ESTADO')
                                ,NMPOLIZA     : recordActivo.get('NMPOLIZA')
                                //,pv_nmsituac     : recordActivo.get('nmsituac')
                                ,CDPERSON     : recordActivo.get('cdperson')
                                ,cdrfc           : recordActivo.get('cdrfc')
                                ,pv_cdrol        : recordActivo.get('cdrol')
                                ,nombreAsegurado : recordActivo.get('nombrecompleto')
                                ,NTRAMITE        : recordActivo.get('NTRAMITE')
                                ,botonCopiar     : '0'
                                ,altabaja        : 'baja'
                                ,cdtipsit        : recordActivo.get('CDTIPSIT')
                                ,fenacimi        : Ext.Date.format(recordActivo.get('fenacimi'), 'd/m/Y')
                            }
                        }
                    });
                /*
                }
                else
                {
                    Ext.Msg.show(
                    {
                        title    : 'Error'
                        ,icon    : Ext.Msg.WARNING
                        ,msg     : 'No hay coberturas para el cliente, por favor seleccione un asegurado'
                        ,buttons : Ext.Msg.OK
                    });
                }
            }
            else
            {
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione un asegurado'
                    ,buttons : Ext.Msg.OK
                });
            }
            */
        }
    	else if(recordOperacion.get('funcion')=='endosodomicilio'||recordOperacion.get('funcion')=='endosodomiciliosimple')
    	{
    		debug('endoso domicilio');
    		var nAsegActivos=0;
            var recordActivo;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;
                }
            });
            if(nAsegActivos==1)
            {
            	debug("recordActivo.get('nmsituac'):",recordActivo.get('nmsituac'));
            	debug("marendStoreAsegurados.getAt(1).get('cdperson'):",marendStoreAsegurados.getAt(1).get('cdperson'));
            	debug("recordActivo.get('cdperson'):",recordActivo.get('cdperson'));
            	debug("recordActivo.get('nmsituac')==0"
                        +"&& marendStoreAsegurados.getCount()>1"
                        +"&& marendStoreAsegurados.getAt(1).get('cdperson')!=recordActivo.get('cdperson')"
                        +"? '1' : '0':",recordActivo.get('nmsituac')==0
                        && marendStoreAsegurados.getCount()>1
                        && marendStoreAsegurados.getAt(1).get('cdperson')!=recordActivo.get('cdperson')
                        ? '1' : '0');
            	Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        smap1 :
                        {
                            pv_cdunieco      : recordActivo.get('CDUNIECO')
                            ,pv_cdramo       : recordActivo.get('CDRAMO')
                            ,pv_estado       : recordActivo.get('ESTADO')
                            ,pv_nmpoliza     : recordActivo.get('NMPOLIZA')
                            ,pv_nmsituac     : recordActivo.get('nmsituac')
                            ,pv_cdperson     : recordActivo.get('cdperson')
                            ,cdrfc           : recordActivo.get('cdrfc')
                            ,pv_cdrol        : recordActivo.get('cdrol')
                            ,nombreAsegurado : recordActivo.get('nombrecompleto')
                            ,botonCopiar     : '0'//recordActivo.get('cdrol')==1?'0':'1'//es asegurado?
                            ,cdtipsit        : recordActivo.get('CDTIPSIT')
                            ,ntramite        : recordActivo.get('NTRAMITE')
                            ,habilitaEdicion : recordActivo.get('nmsituac')==0
                                                   && marendStoreAsegurados.getCount()>1
                                                   && marendStoreAsegurados.getAt(1).get('cdperson')!=recordActivo.get('cdperson')
                                                   ? '1' : '0'
                        }
                    }
                });
            }
            else
            {
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione un asegurado'
                    ,buttons : Ext.Msg.OK
                });
            }
    	}
    	else if(recordOperacion.get('funcion')=='endosovalositbasico')
        {
            debug('endoso valosit basico');
            var nAsegActivos=0;
            var recordActivo;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;
                }
            });
            if(nAsegActivos==1)
            {
            	if(recordActivo.get('cdrol')==2)
            	{
	                Ext.getCmp('marendMenuOperaciones').collapse();
	                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
	                Ext.getCmp('marendLoaderFrame').getLoader().load(
	                {
	                    url       : recordOperacion.get('liga')
	                    ,scripts  : true
	                    ,autoLoad : true
	                    ,jsonData :
	                    {
	                        smap1 :
	                        {
	                            cdunieco  : recordActivo.get('CDUNIECO')
	                            ,cdramo   : recordActivo.get('CDRAMO')
	                            ,estado   : recordActivo.get('ESTADO')
	                            ,nmpoliza : recordActivo.get('NMPOLIZA')
	                            ,cdtipsit : recordActivo.get('CDTIPSIT')
	                            ,nmsituac : recordActivo.get('nmsituac') 
	                            ,ntramite : recordActivo.get('NTRAMITE')
	                            ,nmsuplem : recordActivo.get('NMSUPLEM')
	                        }
	                    }
	                });
            	}
            	else
            	{
            		Ext.Msg.show(
                    {
                        title    : 'Error'
                        ,icon    : Ext.Msg.WARNING
                        ,msg     : 'El cliente no puede ser seleccionado'
                        ,buttons : Ext.Msg.OK
                    });
            	}
            }
            else
            {
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione solo un asegurado'
                    ,buttons : Ext.Msg.OK
                });
            }
        }
    	else if(recordOperacion.get('funcion')=='endosonombres'||recordOperacion.get('funcion')=='endosonombressimple')
    	{
    		debug('endosonombres');
    		var nAsegActivos=0;
            var recordActivo;
            var arrayEditados=[];
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;//solo para tener una referencia de los datos de poliza
                    arrayEditados.push(record.raw);
                }
            });
            if(nAsegActivos>0)
            {
            	debug(arrayEditados);
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var json={};
                json['slist1']=arrayEditados;
                var smap1=
                    {
                        'cdunieco'  : recordActivo.get('CDUNIECO')
                        ,'cdramo'   : recordActivo.get('CDRAMO')
                        ,'cdtipsit' : recordActivo.get('CDTIPSIT')
                        ,'estado'   : recordActivo.get('ESTADO')
                        ,'nmpoliza' : recordActivo.get('NMPOLIZA') 
                        ,'ntramite' : recordActivo.get('NTRAMITE')
                    };
                json['smap1']=smap1;
                debug(json);
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData : json
                });
            }
            else
            {
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione al menos un asegurado'
                    ,buttons : Ext.Msg.OK
                });
            }
    	}
    	else if(recordOperacion.get('funcion')=='endosoclausulas')
   		{
    		debug('endosoclausulas');
            var nAsegActivos=0;
            var recordActivo;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;
                }
            });
            if(nAsegActivos==1)
            {
                if(recordActivo.get('cdrol')==2)
                {
                    Ext.getCmp('marendMenuOperaciones').collapse();
                    Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                    Ext.getCmp('marendLoaderFrame').getLoader().load(
                    {
                        url       : recordOperacion.get('liga')
                        ,scripts  : true
                        ,autoLoad : true
                        ,jsonData :
                        {
                            smap1 :
                            {
                                cdunieco  : recordActivo.get('CDUNIECO')
                                ,cdramo   : recordActivo.get('CDRAMO')
                                ,estado   : recordActivo.get('ESTADO')
                                ,nmpoliza : recordActivo.get('NMPOLIZA')
                                ,cdtipsit : recordActivo.get('CDTIPSIT')
                                ,nmsituac : recordActivo.get('nmsituac') 
                                ,ntramite : recordActivo.get('NTRAMITE')
                                ,nmsuplem : recordActivo.get('NMSUPLEM')
                            }
                        }
                    });
                }
                else
                {
                    Ext.Msg.show(
                    {
                        title    : 'Error'
                        ,icon    : Ext.Msg.WARNING
                        ,msg     : 'El cliente no puede ser seleccionado'
                        ,buttons : Ext.Msg.OK
                    });
                }
            }
            else
            {
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione solo un asegurado'
                    ,buttons : Ext.Msg.OK
                });
            }
   		}
    	else if(recordOperacion.get('funcion')=='endosoaltaasegurado'||recordOperacion.get('funcion')=='endosobajaasegurado')
        {
            debug('endosoaltabajaasegurado');
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                	nPolizasActivas=nPolizasActivas+1;
                	polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                    	'smap1'  : smap1
                    	,'smap2' :
                    	{
                    		alta : recordOperacion.get('funcion')=='endosoaltaasegurado'?'si':'no'
                    	}
                    }
                });
            }
            else
            {
                Ext.Msg.show(
                {
                    title    : 'Error'
                    ,icon    : Ext.Msg.WARNING
                    ,msg     : 'Seleccione la p&oacute;liza'
                    ,buttons : Ext.Msg.OK
                });
            }
        }
    	else if(recordOperacion.get('funcion')=='endosomasedad'||recordOperacion.get('funcion')=='endosomenosedad')
        {
            debug(recordOperacion.get('funcion'));
            var nAsegActivos=0;
            var recordActivo;
            var arrayEditados=[];
            var hayCliente=false;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;//solo para tener una referencia de los datos de poliza
                    arrayEditados.push(record.raw);
                    if(record.get("nmsituac")==0)
                    {
                    	hayCliente=true;
                    }
                }
            });
            if(nAsegActivos==1)
            {
            	if(!hayCliente)
            	{
	                debug(arrayEditados);
	                Ext.getCmp('marendMenuOperaciones').collapse();
	                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
	                var json={};
	                json['slist1']=arrayEditados;
	                var smap1=
	                    {
	                        'cdunieco'  : recordActivo.get('CDUNIECO')
	                        ,'cdramo'   : recordActivo.get('CDRAMO')
	                        ,'cdtipsit' : recordActivo.get('CDTIPSIT')
	                        ,'estado'   : recordActivo.get('ESTADO')
	                        ,'nmpoliza' : recordActivo.get('NMPOLIZA')
	                        ,'ntramite' : recordActivo.get('NTRAMITE')
	                        ,'masedad'  : recordOperacion.get('funcion')=='endosomasedad'?'si':'no'
	                    };
	                json['smap1']=smap1;
	                debug(json);
	                Ext.getCmp('marendLoaderFrame').getLoader().load(
	                {
	                    url       : recordOperacion.get('liga')
	                    ,scripts  : true
	                    ,autoLoad : true
	                    ,jsonData : json
	                });
            	}
            	else
           		{
            		mensajeWarning('No se puede seleccionar el cliente');
           		}
            }
            else
            {
                mensajeWarning('Seleccione solo un asegurado');
            }
        }
    	else if(recordOperacion.get('funcion')=='endosohombremujer'||recordOperacion.get('funcion')=='endosomujerhombre')
        {
            debug(recordOperacion.get('funcion'));
            var nAsegActivos=0;
            var recordActivo;
            var arrayEditados=[];
            var hayCliente=false;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;//solo para tener una referencia de los datos de poliza
                    arrayEditados.push(record.raw);
                    if(record.get("nmsituac")==0)
                    {
                        hayCliente=true;
                    }
                }
            });
            var valido=true;
            
            if(valido)
            {
            	valido=nAsegActivos==1;
            	if(!valido)
            	{
            		mensajeWarning('Seleccione solo un asegurado');
            	}
            }
            
            if(valido)
            {
            	valido=!hayCliente;
            	if(!valido)
            	{
            		mensajeWarning('No se puede seleccionar el cliente');
            	}
            }
            
            if(valido)
            {
            	var nHombres=0;
            	var nMujeres=0;
            	marendStoreAsegurados.each(function(record)
                {
                    if(record.get('activo')==true)
                    {
                        if(record.get("sexo")=="H")
                        {
                        	nHombres=nHombres+1;
                        }
                        else
                        {
                        	nMujeres=nMujeres+1;
                        }
                    }
                });
            	if(recordOperacion.get('funcion')=='endosohombremujer')
            	{
            		valido=nHombres>0&&nMujeres==0;
            	}
            	else
                {
                    valido=nHombres==0&&nMujeres>0;
                }
            	if(!valido)
            	{
            		mensajeWarning('Solo puede seleccionar: '+(recordOperacion.get('funcion')=='endosohombremujer'?'hombre':'mujer'));
            	}
            }
            
            if(valido)
            {
            	debug(arrayEditados);
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var json={};
                json['slist1']=arrayEditados;
                var smap1=
                {
                    'cdunieco'     : recordActivo.get('CDUNIECO')
                    ,'cdramo'      : recordActivo.get('CDRAMO')
                    ,'cdtipsit'    : recordActivo.get('CDTIPSIT')
                    ,'estado'      : recordActivo.get('ESTADO')
                    ,'nmpoliza'    : recordActivo.get('NMPOLIZA')
                    ,'ntramite'    : recordActivo.get('NTRAMITE')
                    ,'hombremujer' : recordOperacion.get('funcion')=='endosohombremujer'?'si':'no'
                };
                json['smap1']=smap1;
                debug(json);
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData : json
                });
            }
        }
    	else if(recordOperacion.get('funcion')=='endosodomiciliofull')
        {
            debug(recordOperacion.get('funcion'));
            var nAsegActivos=0;
            var recordActivo;
            var hayTitular=false;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;
                    if(record.get("nmsituac")==1)
                    {
                        hayTitular=true;
                    }
                }
            });
            var valido=true;
            
            if(valido)
            {
                valido=nAsegActivos==1&&hayTitular;
                if(!valido)
                {
                    mensajeWarning('Seleccione solo al titular');
                }
            }
            
            if(valido)
            {
                debug(recordActivo);
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var json={
                	smap1 : recordActivo.getData()
                };
                debug(json);
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData : json
                });
            }
        }
    	else if(recordOperacion.get('funcion')=='masdeducible'||recordOperacion.get('funcion')=='menosdeducible')
        {
            debug('masdeducible|menosdeducible');
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                        ,'smap2' :
                        {
                            masdeducible : recordOperacion.get('funcion')=='masdeducible'?'si':'no'
                        }
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
    	else if(recordOperacion.get('funcion')=='mascopago'||recordOperacion.get('funcion')=='menoscopago')
        {
            debug('mascopago|menoscopago');
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                        ,'smap2' :
                        {
                        	mascopago : recordOperacion.get('funcion')=='mascopago'?'si':'no'
                        }
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
    	else if(recordOperacion.get('funcion')=='reexpedicion')
        {
            debug('reexpedicion');
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
    	else if(recordOperacion.get('funcion')=='masextraprima'||recordOperacion.get('funcion')=='menosextraprima')
        {
    		debug(recordOperacion.get('funcion'));
            var nAsegActivos=0;
            var recordActivo;
            var hayCliente=false;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;
                    if(record.get("nmsituac")==0)
                    {
                        hayCliente=true;
                    }
                }
            });
            var valido=true;
            
            if(valido)
            {
                valido=nAsegActivos==1;
                if(!valido)
                {
                    mensajeWarning('Seleccione solo un asegurado');
                }
            }
            
            if(valido)
            {
            	valido=!hayCliente;
            	if(!valido)
            	{
            		mensajeWarning('No se puede seleccionar al cliente');
            	}
            }
            
            if(valido)
            {
                debug(recordActivo);
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var json={
                    smap1  : recordActivo.getData()
                    ,smap2 :
                    {
                    	masextraprima : recordOperacion.get('funcion')=='masextraprima'?'si':'no'
                    }
                };
                debug(json);
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData : json
                });
            }
        }
    	else if(recordOperacion.get('funcion')=='formapago')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
    	else if(recordOperacion.get('funcion')=='endosoagente')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosocontratante')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosodespago')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endososumaasegmas')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='34';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endososumaasegmenos')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='35';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosocoasegmas')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='36';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosocoasegmenos')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='37';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosotopemas')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='38';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosotopemenos')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='39';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosobeneficiarios')
        {
            debug(recordOperacion.get('funcion'));
            var nAsegActivos=0;
            var recordActivo;
            var arrayEditados=[];
            var hayCliente=false;
            marendStoreAsegurados.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nAsegActivos=nAsegActivos+1;
                    recordActivo=record;//solo para tener una referencia de los datos de poliza
                    arrayEditados.push(record.raw);
                    if(record.get("nmsituac")==0)
                    {
                        hayCliente=true;
                    }
                }
            });
            var valido=true;
            
            if(valido)
            {
                valido=nAsegActivos==1;
                if(!valido)
                {
                    mensajeWarning('Seleccione solo un asegurado');
                }
            }
            
            if(valido)
            {
                valido=!hayCliente;
                if(!valido)
                {
                    mensajeWarning('No se puede seleccionar el cliente');
                }
            }
            
            if(valido)
            {
                debug("arrayEditados",arrayEditados);
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var json={};
                //json['slist1']=arrayEditados;
                var smap1=
                {
                    'cdunieco'     : recordActivo.get('CDUNIECO')
                    ,'cdramo'      : recordActivo.get('CDRAMO')
                    ,'estado'      : recordActivo.get('ESTADO')
                    ,'nmpoliza'    : recordActivo.get('NMPOLIZA')
                    ,'nmsuplem'    : '0'
                    ,'nmsituac'    : recordActivo.get('nmsituac')
                    ,'cdrolPipes'  : '3'
                    ,'cdtipsup'    : '27'
                    ,'ntramite'    : recordActivo.get('NTRAMITE')
                    ,'ultimaImagen': 'S'
                };
                json['smap1']=smap1;
                debug(json);
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData : json
                });
            }
        }
        else if(recordOperacion.get('funcion')=='endosorehabilitacion')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'  : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosocancelautomend')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                smap1['cdtipsup']='54';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'         : smap1
                        ,'params' : smap1
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
        else if(recordOperacion.get('funcion')=='endosoParentescoAntiguedad'||recordOperacion.get('funcion')=='endosoParentescoAntiguedadSimple')
        {
            debug(recordOperacion.get('funcion'));
            var nPolizasActivas=0;
            var polizaActiva;
            marendStorePolizas.each(function(record)
            {
                if(record.get('activo')==true)
                {
                    nPolizasActivas=nPolizasActivas+1;
                    polizaActiva=record;
                }
            });
            if(nPolizasActivas==1)
            {
                Ext.getCmp('marendMenuOperaciones').collapse();
                Ext.getCmp('marendLoaderFrame').setTitle(recordOperacion.get('texto'));
                var smap1 = polizaActiva.raw;
                smap1['DSCOMENT']='';
                Ext.getCmp('marendLoaderFrame').getLoader().load(
                {
                    url       : recordOperacion.get('liga')
                    ,scripts  : true
                    ,autoLoad : true
                    ,jsonData :
                    {
                        'smap1'         : smap1
                        ,'endosoSimple' : recordOperacion.get('funcion')=='endosoParentescoAntiguedadSimple'
                    }
                });
            }
            else
            {
                mensajeError('Seleccione la p&oacute;liza');
            }
        }
    }
    
    function marendNavegacion(nivel)
    {
    	debug('marendNavegacion:',nivel);
    	if(nivel<=2)
    	{
	    	marendStorePolizas.removeAll();
    	}
    	if(nivel<=3)
    	{
    		marendStoreAsegurados.removeAll();
    	}
    	if(nivel<=4)
    	{
    		Ext.getCmp('marendMenuOperaciones').expand();
    		Ext.getCmp('marendLoaderFrame').setTitle();
    		Ext.getCmp('marendLoaderFrame').update('');
    	}
    }
    
    
    /**
     * Filtra los endosos disponibles segun el producto
     * 
     * @cdramo String cdramo del producto
     * @cdtipsit String cdtipsit del producto
     */
    function filtrarListaEndosos(cdramo, cdtipsit) {
    	
        Ext.Ajax.request({
            url    : marendurlcata,
            params : {
                catalogo : 'ENDOSOS',
                'params.cdramo': cdramo,
                'params.cdtipsit': cdtipsit
            },
            success : function(response) {
            	
                var json = Ext.decode(response.responseText);
                marendStoreLigas.clearFilter(false);
                debug('marendStoreLigas sin filtros:', marendStoreLigas);

                // Se genera una expresion regular para hacer el filtro de lista de endosos:
                var strEndososRegex = '^(';
                Ext.Array.each(json.lista, function(item, index, allItems) {
                	strEndososRegex += item.key;
                	if(index < allItems.length-1) {
                	    strEndososRegex += '|';
                	}
                	//Se asigna descripcion del endoso:
                	if(item.key != '0' && marendStoreLigas.findRecord("cdtipsup", item.key) != null) {
                		marendStoreLigas.findRecord("cdtipsup", item.key).set('texto', item.value);
                	}
                });
                strEndososRegex += ')$';
                debug('strEndososRegex=', strEndososRegex);
                
                marendStoreLigas.filter("cdtipsup", new RegExp(strEndososRegex));
                debug('marendStoreLigas modificado:', marendStoreLigas);
            }
        });
        
    }
    
    /*///////////////////*/
    ////// funciones //////
    ///////////////////////
    
Ext.onReady(function()
{
	// Se aumenta el timeout para todas las peticiones:
    Ext.Ajax.timeout = 60000; // 60 seconds
	
    /////////////////////
    ////// modelos //////
    /*/////////////////*/
    Ext.define('Ramo',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            "cdramo"
            ,"dsramo"
        ]
    });
    
    Ext.define('marendPoliza',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
			"CDUNIECO" 
			,"CDRAMO" 
			,"ESTADO" 
			,"NMPOLIZA" 
			,"NMSUPLEM" 
			,"NMPOLIEX" 
			,"NSUPLOGI"
			,"DSCOMENT" 
			,"CDTIPSIT" 
			,"DSTIPSIT" 
			,"PRIMA_TOTAL"
			,"NTRAMITE"
			,{
                name        : "FEEMISIO"
                ,type       : "date"
                ,dateFormat : "d/m/Y"
            }
            ,{
                name        : "FEINIVAL"
                ,type       : "date"
                ,dateFormat : "d/m/Y"
            }
            ,{
            	name  : 'activo'
            	,type : 'boolean'
            }
        ]
    });
    
    Ext.define('Liga',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            "cdtipsup"
            ,"texto"
            ,"liga"
            ,"funcion"
        ]
    });
    
    Ext.define('MarEndAsegurado',
    {
    	extend  : 'Ext.data.Model'
    	,fields :
    	[
            "nmsituac"
            ,"cdrol"
            ,{
            	name        : 'fenacimi'
            	,type       : 'date'
            	,dateFormat : 'd/m/Y'
            }
            ,"sexo"
            ,"cdperson"
            ,"nombre"
            ,"segundo_nombre"
            ,"Apellido_Paterno"
            ,"Apellido_Materno"
            ,"cdrfc"
            ,"Parentesco"
            ,"tpersona"
            ,"nacional"
            ,"swexiper"
            ,{
            	name  : 'activo'
            	,type : 'boolean'
            }
            ,'nombrecompleto'
            ,'CDUNIECO'
            ,'CDRAMO'
            ,'ESTADO'
            ,'NMPOLIZA'
            ,'NMPOLIEX'
            ,'NSUPLOGI'
            ,'CDTIPSIT'
            ,'NTRAMITE'
            ,'NMSUPLEM'
    	]
    });
    /*/////////////////*/
    ////// modelos //////
    /////////////////////
    
    ////////////////////
    ////// stores //////
    /*////////////////*/
    marendStorePolizas = Ext.create('Ext.data.Store',
    {
        pageSize  : 10
        ,autoLoad : true
        ,model    : 'marendPoliza'
        ,proxy    :
        {
            enablePaging  : true
            ,reader       : 'json'
            ,type         : 'memory'
            ,data         : []
        }
    });
    
    marendStoreAsegurados = Ext.create('Ext.data.Store',
    {
    	autoLoad : false
    	,model   : 'MarEndAsegurado'
    	,proxy   :
    	{
    		type    : 'ajax'
    		,url    : marenUrlObtenerAsegurados
    		,reader :
    		{
    			type  : 'json'
    			,root : 'list1'
    		}
    	}
    });
    
    marendStoreLigas = Ext.create('Ext.data.Store',
    {
        autoLoad  : true
        ,model    : 'Liga'
        ,pageSize : 50
        ,proxy    :
        {
            enablePaging  : true
            ,reader       : 'json'
            ,type         : 'memory'
            ,data         :
            [
				{
					cdtipsup : '0'
				    ,texto   : 'CORRECCI&Oacute;N DE NOMBRE Y RFC *'//nombres
				    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoNombresSimple" />'
				    ,funcion : 'endosonombressimple'
				},{
					cdtipsup : '0'
                    ,texto   : 'CAMBIO DE DOMICILIO *'//domicilio
                    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoDomicilioSimple" />'
                    ,funcion : 'endosodomiciliosimple'
                },{
					cdtipsup : '0'
                    ,texto   : 'CORRECI&Oacute;N ASEGURADOS ANTIGUEDAD Y PARENTESCO *'//valosit
                    ,liga    : '<s:url namespace="/endosos" action="entrarEndosoParentescoAntiguedad" />'
                    ,funcion : 'endosoParentescoAntiguedadSimple'
                },{
                	cdtipsup : '2'
                    ,texto   : '2'//nombres
                	,liga    : '<s:url namespace="/endosos" action="pantallaEndosoNombres" />'
                	,funcion : 'endosonombres'
                },{
                	cdtipsup : '3'
                    ,texto   : '3'//domicilio
                	,liga    : '<s:url namespace="/endosos" action="pantallaEndosoDomicilio" />'
                	,funcion : 'endosodomicilio'
                },{
                    cdtipsup : '4'
                    ,texto   : '4'//valosit
                    ,liga    : '<s:url namespace="/endosos" action="entrarEndosoParentescoAntiguedad" />'
                    ,funcion : 'endosoParentescoAntiguedad'
                },{
                    cdtipsup : '6'
                    ,texto   : '6'//alta coberturas
                    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoCoberturas" />'
                    ,funcion : 'endosocoberturasalta'
                },{
                    cdtipsup : '7'
                    ,texto   : '7'//baja coberturas
                    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoCoberturas" />'
                    ,funcion : 'endosocoberturasbaja'
                },{
                    cdtipsup : '8'
                    ,texto   : '8'//clausulas
                    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoClausulas" />'
                    ,funcion : 'endosoclausulas'
                },{
                    cdtipsup : '9'
                    ,texto   : '9'//alta asegurado
                    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoAltaBajaAsegurado" />'
                    ,funcion : 'endosoaltaasegurado'
                },{
                    cdtipsup : '10'
                    ,texto   : '10'//baja asegurado
                    ,liga    : '<s:url namespace="/endosos" action="pantallaEndosoAltaBajaAsegurado" />'
                    ,funcion : 'endosobajaasegurado'
                },{
                    cdtipsup : '15'
                    ,texto   : '15'//mas edad
                    ,liga    : '<s:url namespace="/endosos" action="endosoEdad" />'
                    ,funcion : 'endosomasedad'
                },{
                    cdtipsup : '16'
                    ,texto   : '16'//menos edad
                    ,liga    : '<s:url namespace="/endosos" action="endosoEdad" />'
                    ,funcion : 'endosomenosedad'
                },{
                    cdtipsup : '20'
                    ,texto   : '20'//hombre -> mujer
                    ,liga    : '<s:url namespace="/endosos" action="endosoSexo" />'
                    ,funcion : 'endosohombremujer'
                },{
                	cdtipsup : '21'
                    ,texto   : '21'//mujer -> hombre
                    ,liga    : '<s:url namespace="/endosos" action="endosoSexo" />'
                    ,funcion : 'endosomujerhombre'
                },{
                    cdtipsup : '31'
                    ,texto   : '31'//domicilio full
                    ,liga    : '<s:url namespace="/endosos" action="endosoDomicilioFull" />'
                    ,funcion : 'endosodomiciliofull'
                },{
                    cdtipsup : '17'
                    ,texto   : '17'
                    ,liga    : '<s:url namespace="/endosos" action="endosoDeducible" />'
                    ,funcion : 'masdeducible'
                },{
                    cdtipsup : '18'
                    ,texto   : '18'
                    ,liga    : '<s:url namespace="/endosos" action="endosoDeducible" />'
                    ,funcion : 'menosdeducible'
                },{
                	cdtipsup : '11'
                    ,texto   : '11'
                    ,liga    : '<s:url namespace="/endosos" action="endosoCopago" />'
                    ,funcion : 'mascopago'
                },{
                    cdtipsup : '12'
                    ,texto   : '12'
                    ,liga    : '<s:url namespace="/endosos" action="endosoCopago" />'
                    ,funcion : 'menoscopago'
                },{
                    cdtipsup : '24'
                    ,texto   : '24'
                    ,liga    : '<s:url namespace="/endosos" action="endosoReexpedicion" />'
                    ,funcion : 'reexpedicion'
                },{
                    cdtipsup : '13'
                    ,texto   : '13'
                    ,liga    : '<s:url namespace="/endosos" action="endosoExtraprima" />'
                    ,funcion : 'masextraprima'
                },{
                    cdtipsup : '14'
                    ,texto   : '14'
                    ,liga    : '<s:url namespace="/endosos" action="endosoExtraprima" />'
                    ,funcion : 'menosextraprima'
                },{
                    cdtipsup : '26'
                    ,texto   : '26'
                    ,liga    : '<s:url namespace="/endosos" action="endosoFormaPago" />'
                    ,funcion : 'formapago'
                },{
                    cdtipsup : '19'
                    ,texto   : '19'
                    ,liga    : '<s:url namespace="/endosos" action="endosoAgente" />'
                    ,funcion : 'endosoagente'
                },{
                    cdtipsup : '28'
                    ,texto   : '28'
                    ,liga    : '<s:url namespace="/endosos" action="endosoContratante" />'
                    ,funcion : 'endosocontratante'
                },{
                    cdtipsup : '32'
                    ,texto   : '32'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoDespago" />'
                    ,funcion : 'endosodespago'
                },{
                    cdtipsup : '34'
                    ,texto   : '34'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoAtributosSituacionGeneral" />'
                    ,funcion : 'endososumaasegmas'
                },{
                    cdtipsup : '35'
                    ,texto   : '35'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoAtributosSituacionGeneral" />'
                    ,funcion : 'endososumaasegmenos'
                },{
                    cdtipsup : '36'
                    ,texto   : '36'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoAtributosSituacionGeneral" />'
                    ,funcion : 'endosocoasegmas'
                },{
                    cdtipsup : '37'
                    ,texto   : '37'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoAtributosSituacionGeneral" />'
                    ,funcion : 'endosocoasegmenos'
                },{
                    cdtipsup : '38'
                    ,texto   : '38'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoAtributosSituacionGeneral" />'
                    ,funcion : 'endosotopemas'
                },{
                    cdtipsup : '39'
                    ,texto   : '39'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoAtributosSituacionGeneral" />'
                    ,funcion : 'endosotopemenos'
                },{
                    cdtipsup : '27'
                    ,texto   : '27'
                    ,liga    : '<s:url namespace="/catalogos" action="includes/pantallaBeneficiariosSMD" />'
                    ,funcion : 'endosobeneficiarios'
                },{
                    cdtipsup : '54'
                    ,texto   : '54'
                    ,liga    : '<s:url namespace="/endosos" action="includes/endosoCancelacionAuto" />'
                    ,funcion : 'endosocancelautomend'
                },{
                    cdtipsup : '58'
                    ,texto   : '58'
                    ,liga    : '<s:url namespace="/endosos" action="endosoRehabilitacionEnd" />'
                    ,funcion : 'endosorehabilitacion'
                }
            ]
        }
    });
    
    /*////////////////*/
    ////// stores //////
    ////////////////////
    
    /////////////////////////
    ////// componentes //////
    /*/////////////////////*
    Ext.define('Panel1',{
        extend:'Ext.panel.Panel',
        title:'Objeto asegurado',
        layout:{
            type:'column',
            columns:2
        },
        frame:false,
        style:'margin : 5px;',
        collapsible:true,
        titleCollapse:true,
        <s:property value="item2" />
    });
    Ext.define('FormPanel',{
        extend:'Ext.form.Panel',
        renderTo:'maindiv',
        frame:false,
        buttonAlign:'center',
        items:[
            new Panel1()
        ]
    });
    /*/////////////////////*/
    ////// componentes //////
    /////////////////////////
    
    ///////////////////////
    ////// contenido //////
    /*///////////////////*/
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : 'marcoEndososDiv'
        ,title    : 'Endosos'
        ,defaults :
        {
            style : 'margin : 5px'
        }
        ,items    :
        [
            Ext.create('Ext.form.Panel',
            {
                title          : 'B&uacute;squeda'
                ,id            : 'marendFilForm'
                //,width         : 1000
                ,url           : marendUrlFiltro
                ,layout        :
                {
                    type     : 'table'
                    ,columns : 3
                }
                ,defaults      :
                {
                    style : 'margin:5px;'
                }
                ,collapsible   : true
                ,titleCollapse : true
                ,buttonAlign   : 'center'
                ,frame         : true
                ,items         :
                [
                    {
                    	xtype       : 'textfield'
                    	,id         : 'marendFilNmpoliex'
                    	,fieldLabel : 'N&uacute;mero de p&oacute;liza'
                    	,name       : 'smap1.pv_nmpoliex_i'
                    }
                    ,{
                        xtype : 'textfield'
                        ,id   : 'marendFilRfc'
                        ,fieldLabel : 'RFC'
                        ,name       : 'smap1.pv_cdrfc_i'
                    }
                    ,{
                        xtype : 'numberfield'
                        ,id   : 'marendFilCdperson'
                        ,fieldLabel : 'Clave de asegurado'
                        ,name       : 'smap1.pv_cdperson_i'
                    }
                    ,{
                        xtype : 'textfield'
                        ,id   : 'marendFilNombre'
                        ,fieldLabel : 'Nombre'
                        ,name       : 'smap1.pv_nombre_i'
                    }
                ]
                ,buttons       :
                [
                    {
                        text    : 'Tipo de b&uacute;squeda'
                        ,icon   : '${ctx}/resources/fam3icons/icons/cog.png'
                        ,menu   :
                        {
                            xtype  : 'menu'
                            ,items :
                            [
                                {
                                    text     : 'Por p&oacute;liza'
                                    ,handler : function(){marendMostrarControlesFiltro(1);}
                                }
                                ,{
                                    text     : 'Por RFC'
                                    ,handler : function(){marendMostrarControlesFiltro(2);}
                                }
                                ,{
                                    text     : 'Por clave de asegurado'
                                    ,handler : function(){marendMostrarControlesFiltro(3);}
                                }
                                ,{
                                    text     : 'Por nombre'
                                    ,handler : function(){marendMostrarControlesFiltro(4);}
                                }
                            ]
                        }
                    }
                    ,{
                        text     : 'Buscar'
                        ,id      : 'marendFilBotGen'
                        ,icon    : '${ctx}/resources/fam3icons/icons/zoom.png'
                        ,handler : function()
                        {
                            if(this.up().up().isValid())
                            {
                            	var loadMaskTabla = new Ext.LoadMask(this.up().up(), {msg:"Buscando..."});
								loadMaskTabla.show();
                            	
                            	marendNavegacion(2);
                                this.up().up().submit(
                                {
                                    success  : function(form,action)
                                    {
                                    	
                                    	loadMaskTabla.hide();
                                    	
                                        debug(action);
                                        var json = Ext.decode(action.response.responseText);
                                        debug(json);
                                        if(json.success==true&&json.slist1&&json.slist1.length>0)
                                        {
                                            marendStorePolizas.removeAll();
                                            marendStorePolizas.add(json.slist1);
                                            debug(marendStorePolizas);
                                        }
                                        else
                                        {
                                            centrarVentanaInterna(Ext.Msg.show(
                                            {
                                                title    : 'Sin resultados'
                                                ,msg     : 'No hay resultados'
                                                ,icon    : Ext.Msg.WARNING
                                                ,buttons : Ext.Msg.OK
                                            }));
                                        }
                                    }
                                    ,failure : function()
                                    {
                                    	loadMaskTabla.hide();
                                    	
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
                            else
                            {
                                Ext.Msg.show(
                                {
                                    title   : 'Datos imcompletos',
                                    icon    : Ext.Msg.WARNING,
                                    msg     : 'Favor de llenar los campos requeridos',
                                    buttons : Ext.Msg.OK
                                });
                            }
                        }
                    }
                    ,{
                        text     : 'Limpiar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/control_repeat_blue.png'
                        ,handler : function()
                        {
                            this.up().up().getForm().reset();
                        }
                    }
                ]
            })
            ,Ext.create('Ext.grid.Panel',
            {
                title          : 'Hist&oacute;rico de movimientos'
                //,width         : 1000
                ,collapsible   : true
                ,titleCollapse : true
                ,height        : 200
                ,store         : marendStorePolizas
                ,frame         : true
                ,tbar          :
                [
                    {
                        text     : 'Marcar/desmarcar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/table_lightning.png'
                        ,handler : function()
                        {
                        	marendNavegacion(3);
                            var nRecordsActivos=0;
                            marendStorePolizas.each(function(record)
                            {
                                if(record.get('activo')==true)
                                {
                                    nRecordsActivos=nRecordsActivos+1;
                                }
                            });
                            marendStorePolizas.each(function(record)
                            {
                                record.set('activo',nRecordsActivos!=marendStorePolizas.getCount());
                            });
                        }
                    }
                ]
                ,columns       :
                [
                    {
                        dataIndex     : 'activo'
                        ,xtype        : 'checkcolumn'
                        ,width        : 30
                        ,menuDisabled : true
                        ,listeners    :
                        {
                        	checkchange : function(column, recordIndex, checked)
                        	{
                        		debug('checkchange');
                        	    marendNavegacion(3);
                        	}
                        }
                    }
                    /*
                    "CDUNIECO" 
                ,"CDRAMO" 
                ,"ESTADO" 
                ,"NMPOLIZA" 
                ,"NMSUPLEM" 
                ,"NMPOLIEX" 
                ,"NSUPLOGI" 
                ,"FEEMISIO" 
                ,"FEINIVAL" 
                ,"DSCOMENT" 
                ,"CDTIPSIT" 
                ,"DSTIPSIT" 
                ,"PRIMA_TOTAL"
                    */
                    ,{
                        header     : 'Tr&aacute;mite'
                        ,dataIndex : 'NTRAMITE'
                        ,width     : 70
                    }
                    ,{
                    	header     : 'Sucursal'
                    	,dataIndex : 'CDUNIECO'
                    	,flex      : 1
                    }
                    ,{
                        header     : 'Producto'
                        ,dataIndex : 'CDRAMO'
                        ,flex      : 1
                    }
                    ,{
                        header     : 'P&oacute;liza'
                        ,dataIndex : 'NMPOLIEX'
                        ,flex      : 1
                    }
                    ,{
                        header     : 'No. Endoso'
                        ,dataIndex : 'NSUPLOGI'
                        ,flex      : 1
                    }
                    ,{
                        header     : 'Tipo de endoso'
                        ,dataIndex : 'DSCOMENT'
                        ,flex      : 1
                    }
                    ,{
                        header     : 'Fecha de emisi&oacute;n'
                        ,dataIndex : 'FEEMISIO'
                        ,flex      : 1
                        ,xtype     : 'datecolumn'
                        ,format    : 'd M Y'
                    }
                    ,{
                        header     : 'Inicio de vigencia'
                        ,dataIndex : 'FEINIVAL'
                        ,flex      : 1
                        ,xtype     : 'datecolumn'
                        ,format    : 'd M Y'
                    }
                    ,{
                        header     : 'Prima total'
                        ,dataIndex : 'PRIMA_TOTAL'
                        ,renderer  : Ext.util.Format.usMoney
                        ,flex      : 1
                    }
                    ,{
                    	header     : 'nmsuplem'
                    	,dataIndex : 'NMSUPLEM'
                    	,width     : 150
                    	,hidden    : true
                    }
                    ,{
                    	xtype         : 'actioncolumn'
                    	,width        : 30
                    	,icon         : '${ctx}/resources/fam3icons/icons/printer.png'
                    	,menuDisabled : true
                    	,tooltip      : 'Documentos'
                    	,sortable     : false
                    	,handler      : function(grid,rowIndex)
                    	{
                    		var record=grid.getStore().getAt(rowIndex);
                    		debug(record);
                    		Ext.create('Ext.window.Window',
                            {
                                title        : 'Documentos del tr&aacute;mite '+record.get('NTRAMITE')
                                ,modal       : true
                                ,buttonAlign : 'center'
                                ,width       : 600
                                ,height      : 400
                                ,autoScroll  : true
                                ,cls         : 'VENTANA_DOCUMENTOS_CLASS'
                                ,loader      :
                                {
                                    url       : marendUrlDoc
                                    ,params   :
                                    {
                                        'smap1.nmpoliza'  : record.get('NMPOLIZA')
                                        ,'smap1.cdunieco' : record.get('CDUNIECO')
                                        ,'smap1.cdramo'   : record.get('CDRAMO')
                                        ,'smap1.estado'   : record.get('ESTADO')
                                        ,'smap1.nmsuplem' : '0'
                                        ,'smap1.ntramite' : record.get('NTRAMITE')
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
                ,listeners     :
                {
                	'cellclick' : function(grid, td, cellIndex, record)
                    {
                    	filtrarListaEndosos(record.get('CDRAMO'), record.get('CDTIPSIT'));
                		marendNavegacion(3);
                        debug(record);
                        marendStoreAsegurados.load(
                        {
                        	params    :
                        	{
                        		'map1.pv_cdunieco'  : record.get('CDUNIECO')
                        		,'map1.pv_cdramo'   : record.get('CDRAMO')
                        		,'map1.pv_estado'   : record.get('ESTADO')
                        		,'map1.pv_nmpoliza' : record.get('NMPOLIZA')
                        		,'map1.pv_nmsuplem' : record.get('NMSUPLEM')
                        	}
                            ,callback : function(records)
                            {
                            	debug('callback',records);
                            	if(records)
                            	{
                            		for(i=0;i<records.length;i++)
                            		{
                            			records[i].set('nombrecompleto',
                            					records[i].get('nombre')
                            					+' '+(records[i].get('segundo_nombre')?records[i].get('segundo_nombre'):'')
                            					+' '+records[i].get('Apellido_Paterno')
                            					+' '+records[i].get('Apellido_Materno')
                            					);
                            			records[i].set('CDUNIECO' , record.get('CDUNIECO'));
                            			records[i].set('CDRAMO'   , record.get('CDRAMO'));
                            			records[i].set('ESTADO'   , record.get('ESTADO'));
                            			records[i].set('NMPOLIZA' , record.get('NMPOLIZA'));
                            			records[i].set('NMPOLIEX' , record.get('NMPOLIEX'));
                            			records[i].set('NSUPLOGI' , record.get('NSUPLOGI'));
                            			records[i].set('CDTIPSIT' , record.get('CDTIPSIT'));
                            			records[i].set('NTRAMITE' , record.get('NTRAMITE'));
                            			records[i].set('NMSUPLEM' , record.get('NMSUPLEM'));
                            		}
                            	}
                            }
                        });
                    }
                }
            })
            ,Ext.create('Ext.grid.Panel',
            {
            	id             : 'marendGridAsegurados'
            	,title         : 'Cliente y asegurados'
            	,frame         : true
            	,store         : marendStoreAsegurados
            	,height        : 200
            	,titleCollapse : true
            	,collapsible   : true
            	,columns       :
            	[
					{
					    dataIndex     : 'activo'
					    ,xtype        : 'checkcolumn'
					    ,width        : 30
					    ,menuDisabled : true
                        ,listeners    :
                        {
                            checkchange : function(column, recordIndex, checked)
                            {
                                debug('checkchange');
                                marendNavegacion(4);
                            }
                        }
					}
					,{
						header     : 'P&oacute;liza'
						,dataIndex : 'NMPOLIEX'
						,flex      : 1
					}
					,{
                        header     : 'Endoso'
                        ,dataIndex : 'NSUPLOGI'
                        ,flex      : 1
                    }
            	    ,{
            	    	header     : 'Rol'
            	    	,dataIndex : 'nmsituac'
            	    	,flex      : 1
            	    	,renderer  : function(value)
            	    	{
            	    		var text='Cliente';
            	    		if(value>0)
            	    		{
            	    			text='Asegurado';
            	    		}
            	    		return text;
            	    	}
            	    }
            	    ,{
            	    	header     : 'Parentesco'
            	    	,dataIndex : 'Parentesco'
            	    	,flex      : 1
            	    	,renderer  : function(value)
            	    	{
            	    		var text='';
            	    		if(value=='T')
            	    		{
            	    			text='Titular'
            	    		}
            	    		else if(value=='C')
                            {
                                text='C&oacute;nyugue'
                            }
            	    		else if(value=='H')
                            {
                                text='Hijo'
                            }
            	    		else if(value=='P')
                            {
                                text='Padre'
                            }
            	    		else if(value=='D')
                            {
                                text='Dependiente'
                            }
            	    		return text;
            	    	}
            	    }
            	    ,{
            	    	header     : 'Nombre'
            	    	,dataIndex : 'nombrecompleto'
            	    	,flex      : 2
            	    }
            	    ,{
                        header     : 'RFC'
                        ,dataIndex : 'cdrfc'
                        ,flex      : 1
                    }
            	    ,{
                        header     : 'Tipo de persona'
                        ,dataIndex : 'tpersona'
                        ,flex      : 1
                        ,renderer  : function(value)
                        {
                        	var text='';
                        	if(value=='M')
                        	{
                        		text='Moral';
                        	}
                        	else if(value=='F')
                       		{
                        		text='F&iacute;sica';
                       		}
                        	else if(value=='S')
                            {
                        		text='Simplificado';
                            }
                        	return text;
                        }
                    }
            	    ,{
            	    	header     : 'Sexo'
            	    	,dataIndex : 'sexo'
            	    	,width     : 60
            	    }
            	]
                ,tbar          :
                [
                    {
                        text     : 'Marcar/desmarcar'
                        ,icon    : '${ctx}/resources/fam3icons/icons/table_lightning.png'
                        ,handler : function()
                        {
                        	marendNavegacion(4);
                            var nRecordsActivos=0;
                            marendStoreAsegurados.each(function(record)
                            {
                                if(record.get('activo')==true)
                                {
                                    nRecordsActivos=nRecordsActivos+1;
                                }
                            });
                            marendStoreAsegurados.each(function(record)
                            {
                                record.set('activo',nRecordsActivos!=marendStoreAsegurados.getCount());
                            });
                        }
                    }
                ]
                ,listeners :
                {
                	cellclick : function()
                	{
                		marendNavegacion(4);
                	}
                }
            })
            ,Ext.create('Ext.panel.Panel',
            {
                id        : 'marendLoaderFrameParent'
                ,layout   : 'border'
                //,width    : 1000
                ,height   : 1000
                ,border   : 0
                ,items    : 
                [
                    Ext.create('Ext.grid.Panel',
                    {
                        style        : 'margin-right : 5px;'
                        ,id          : 'marendMenuOperaciones'
                        ,width       : 450
                        ,region      : 'west'
                        ,collapsible : true
                        ,margins     : '0 5 0 0'
                        ,layout      : 'fit'
                        ,frame       : false
                        ,title       : 'Operaciones'
                        ,store       : marendStoreLigas
                        ,hideHeaders : true
                        ,columns     :
                        [
                            {
                                dataIndex : 'texto'
                                ,flex     : 1
                            }
                        ]
                        ,listeners   :
                        {
                            'cellclick' : function(grid, td, cellIndex, record)
                            {
                            	marendNavegacion(4);
                                marendValidaOperacion(record);
                            }
                        }
                    })
                    ,Ext.create('Ext.panel.Panel',
                    {
                        frame      : true
                        ,region    : 'center'
                        ,id        : 'marendLoaderFrame'
                        ,loader    :
                        {
                            autoLoad: false
                        }
                    })
                ]
            })
        ]
    });
    /*///////////////////*/
    ////// contenido //////
    ///////////////////////
    
    //////////////////////
    ////// cargador //////
    /*//////////////////*/
    /*//////////////////*/
    ////// cargador //////
    //////////////////////
    
    marendMostrarControlesFiltro(1);
    
});
</script>
</head>
<body>
<div id="marcoEndososDiv" style="height:1500px;"></div>
</body>
</html>