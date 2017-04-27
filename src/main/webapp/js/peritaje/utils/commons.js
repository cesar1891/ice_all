var _global_overlay;
var _global_overlay_count;

function debug(a,b,c,d)
{
	if(false)
	{
	   	if(d!=undefined)
	   	{
	   		console.log(a,b,c,d);
	   	}
	   	else if(c!=undefined)
	   	{
	   		console.log(a,b,c);
	   	}
	   	else if(b!=undefined)
	   	{
	   		console.log(a,b);
	   	}
	   	else
	   	{
	   		console.log(a);
	   	}
	}
}

function mensajeCorrecto(mensaje,titulo,funcion)
{
	Ext.Msg.alert(titulo?titulo:'Aviso',mensaje,funcion?funcion:Ext.emptyFn);
}

function mensajeError(mensaje,titulo,funcion)
{
	Ext.Msg.alert(titulo?titulo:'Error',mensaje,funcion?funcion:Ext.emptyFn);
}

function errorComunicacion()
{
	setLoading(false);
	Ext.Msg.alert('Error','Error de comunicaci&oacute;n',Ext.emptyFn);
}

function setLoading(flag)
{
	if(!_global_overlay)
	{
		_global_overlay_count=0;
		_global_overlay = Ext.Viewport.add({
			xtype       : 'panel'
			,modal      : true
			,centered   : true
			,scrollable : true
			,html       : '<table width="180"><tr><td align="center" valign="center" height="80">Cargando...</td></tr></table>'
			,width      : 200
			,height     : 100
		});
	}
	if(flag)
	{
		_global_overlay_count=_global_overlay_count+1;
		_global_overlay.show();
	}
	else
	{
		_global_overlay_count=_global_overlay_count-1;
		if(_global_overlay_count<=0)
		{
			_global_overlay_count=0;
			_global_overlay.hide();
		}
	}
	debug('_global_overlay_count:',_global_overlay_count);
}