<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Seleccione Rol y Cliente </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
    
<table class="headlines" cellspacing="10" align="center" border="0">
    <tr valign="top" >
        <td class="headlines" colspan="2" >
            <table align="center">
                <tr>
                    <td>
                        <div id="lugar" ></div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
    
<!--<script type="text/javascript" src="${ctx}/resources/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="${ctx}/resources/extjs/ext-all.js"></script><!-- jtezva comentado -->
    
<link rel="stylesheet" href="${ctx}/resources/extjs4/resources/my-custom-theme/my-custom-theme-all.css"><!-- jtezva agregado -->
<script type="text/javascript" src="${ctx}/resources/extjs4/ext-all.js"></script><!-- jtezva agregado -->
<script type="text/javascript" src="${ctx}/resources/scripts/util/AON_utils.js"></script>
<script type="text/javascript">
    var _CONTEXT = "${ctx}";
    var _ACTION_ARBOL                          = "<s:url namespace='/seguridad' action='ArbolRolCliente' />";
    var _ACTION_PORTAL                         = "<s:url namespace='/'          action='load' />";
    var _ACTION_REGRESA                        = "<s:url namespace='/seguridad' action='regresaCodigo' />";
    var _ACTION_VALIDAR_CONFIGURACION_COMPLETA = "<s:url namespace='/principal' action='validaConfiguracionCompleta' />";
    var _MensajeLigaDirecta = '<s:property value="%{#session['MsgLigaDirecta']}" />';
    var _grabarEvento = function (cdmodulo,cdevento)
    {
        try
        {
            Ext.Ajax.request(
            {
                url     : '<s:url namespace="/servicios" action="grabarEvento" />'
                ,params :
                {
                    'params.cdmodulo'  : cdmodulo
                    ,'params.cdevento' : cdevento
                }
            });
        }
        catch(e)
        {
            debugError('Error al grabar evento',cdmodulo,cdevento,e);
        }
    };
</script>
<script type="text/javascript">
Ext.onReady(function(){
	
	//_grabarEvento('SEGURIDAD','ACCTREE');
	
    /*
    *	Si viene en sesión MessageConf != null, es porque tiene un único rol y no está completa la configuración para esa cuenta.
    *	Ver issue ACW-693
    */	
    <%
        if (session.getAttribute("MessageConf") != null)
        {
    %>
            Ext.Msg.alert('Error', '<%=session.getAttribute("MessageConf")%>', function () {
                window.location.replace(_CONTEXT);
            });	
    <%
        }
        else
        {
    %>
    
    /*
    *	PARA MENSAJE DE LIGA DIRECTA, Cuando no existe el usuario o no hay Roles
    */
    if(!Ext.isEmpty(_MensajeLigaDirecta)){
    	Ext.Msg.show({
            title:'Error',
            msg: _MensajeLigaDirecta,
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.ERROR
        });
    }
            
    Ext.define('RamaVO',
    {
        extend   : 'Ext.data.Model',
        fields   :
        [
            {name:'serialVersionUID',   type: 'int'     },
            {name:'id',                 type: 'string'  },
            {name:'text',               type: 'string'  },
            {name:'codigoObjeto',       type: 'string'  },
            {name:'leaf',               type: 'boolean' },
            {name:'allowDelete',        type: 'boolean' },
            {name:'expanded',           type: 'boolean' },
            {name:'nick',               type: 'string'  },
            {name:'name',               type: 'string'  },
            {name:'claveRol',           type: 'string'  },
            {name:'dsRol',              type: 'string'  },
            {name:'cdElemento',         type: 'string'  }
        ],
        hasMany: 'RamaVO'
    });
    
    var clientesRolesStore = Ext.create('Ext.data.TreeStore',
    {
        model:'RamaVO',
        proxy:
        {
            type: 'ajax',
            url: _ACTION_ARBOL,
            reader: { type: 'json' }
        }
    });

    Ext.create('Ext.tree.Panel',
    {
        id:'arbolProductosId',
        title: 'Seleccione Rol y Cliente',
        width: 300,
        store:clientesRolesStore,
        rootVisible: false,
        renderTo:'lugar',
        listeners:
        {
            itemclick: function(s,n)
            {
                //console.log(n);
                if (n.data.leaf==true)
                {
                    //console.log("leaf");
                    var treePath= n.getPath("codigoObjeto");
                    var codigoObjetoSplit=new Array();
                    codigoObjetoSplit= treePath.split("/");
                    var params2 =
                    {
                        codigoRol : n.data.codigoObjeto,
                        codigoCliente : codigoObjetoSplit[2]
                    };
                    //console.log(params2);
                    startMask ('arbolProductosId', 'Espere...');
                    Ext.Ajax.request(
                    {
                        url: _ACTION_REGRESA,
                        params:params2,
                        success:function(response,opts)
                        {
                            var jsonResp = Ext.decode(response.responseText);
                            if(jsonResp.codigoValido==true)
                            {
                                endMask();
                                Ext.MessageBox.show(
                                    {
                                        msg: 'Redireccionando...',
                                        width:300,
                                        wait:true,
                                        waitConfig:{interval:100}
                                    }
                                );
                                //_grabarEvento('SEGURIDAD','TREESELECT');
                                window.location.replace(_ACTION_PORTAL);
                            }
                            else
                            {
                                endMask();
                                //Ext.Msg.alert('Error', jsonResp.actionErrors[0]);
                                Ext.Msg.show({
                                    title:'Error',
                                    msg: 'C&oacute;digo inv&aacute;lido',
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR
                                });
                            }
                        },
                        failure:function(response,opts)
                        {
                            endMask();
                            //console.log("error");
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
        }
    });
    
    <%}%>
});
</script>    
</body>
</html>