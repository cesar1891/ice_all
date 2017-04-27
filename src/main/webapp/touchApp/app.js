Ext.setup(
{
    onReady : function()
	{
		debug('>onReady');
		if(_estadoSesion=='arbol')
		{
		    mostrarArbol();
		}
		else if(_estadoSesion=='menu')
		{
		    mostrarMenus();
		}
		else
		{
		    mostrarLoginForm();
		}
		debug('<onReady');
	}
});

function mostrarLoginForm()
{
    debug('>mostrarLoginForm');
    Ext.Viewport.removeAll(true,true);
    Ext.Viewport.add(
        Ext.create('FormularioInicioSesion',
        {
            textos    :
            {
                titulo          : _t01
                ,tituloFieldSet : _t02
                ,usuario        : _t03
                ,contrasena     : _t04
                ,boton          : _t05
                ,instrucciones  : _t06
                ,usuarioValida  : _t07
                ,passwordValida : _t08
                ,msgError       : _t09
            }
            ,url      : _urlLogin
            ,callback : mostrarArbol
        })
    );
    debug('<mostrarLoginForm');
}

function mostrarArbol()
{
    debug('>mostrarArbol');
    maskui();
    Ext.Ajax.request(
    {
        url      : _urlPantallaArbolJson
        ,success : function(response)
        {
            unmaskui();
            var json=Ext.JSON.decode(response.responseText);
            debug('json:',json);
            if(!json.soloUnRol)
            {
                Ext.Viewport.removeAll(true,true);
                Ext.Viewport.add(
                    Ext.create('ArbolRoles',
                    {
                        callback   : mostrarMenus
                        ,textos    :
                        {
                            titulo          : _t10
                            ,codigoInvalido : _t11 
                        }
                        ,urlCargar : _urlCargarArbolRol
                        ,urlCodigo : _urlRegresaCodigoArbol
                    })
                );
            }
            else
            {
                mostrarMenus();
            }
        }
        ,failure : function()
        {
            unmaskui();
            errorComunicacion();
        }
    });
    debug('<mostrarArbol');
}

function mostrarMenus()
{
    debug('>mostrarMenus');
    Ext.Viewport.removeAll(true,true);
    Ext.Viewport.add(
        Ext.create('MenuPrincipal',
        {
            textos     :
            {
                titulo       : _t12
                ,botonLogout : _t13
                ,ligaNoMovil : _t14
            }
            ,urlCargar : _urlObtenerMenus
            ,logout    : cerrarSesion
        })
    );
    debug('<mostrarMenus');
}

function cerrarSesion()
{
    debug('>cerrarSesion');
    maskui();
    Ext.Ajax.request(
    {
        url       : _urlLogout
        ,callback : function()
        {
            unmaskui();
            mostrarLoginForm();
        }
    });
    debug('<cerrarSesion');
}