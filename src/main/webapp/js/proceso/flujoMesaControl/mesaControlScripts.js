
function actualizarMCSigs (cduniext,ramo,nmpoliex,ntramite,callback)
{	
	var mask, ck = 'Actuzalizando Mesa de Control';
    try 
    {
    	mask = _maskLocal(ck);
        Ext.Ajax.request(
        {
              url      : _p54_urlactulizaNumFolioMcSigs
             ,params : 
             {
                'params.cdunieco' : cduniext,
                'params.cdramo'   : ramo,
                'params.nmpoliza' : nmpoliex,
                'params.numtra'   : ntramite
             },
            success : function (response) 
            {
            	mask.close();
            	var ck = 'Actuzalizando Mesa de Control';
            	try
            	{
                	var json = Ext.decode(response.responseText);
                	if (json.success === true) 
                	{
                        mask.close();
                        if(!Ext.isEmpty(callback))
                        {
                            callback(true);
                        }
                	}
                	else
                	{
                	   mask.close()
                	   mensajeError('Error cambiando el estatus de tramite de la poliza');
                	   if(!Ext.isEmpty(callback))
                	   {
                	       callback(false);
                	   }
                	}
            	}
            	catch (e) 
                {
                    manejaException(e, ck);
                }
            }
            ,failure : function()
            {
                mask.close();
                errorComunicacion(null,'Error cambiando el estatus de tramite de la poliza');
            }
         });
    } 
    catch (e) 
    {
        manejaException(e, ck);
    }
}
