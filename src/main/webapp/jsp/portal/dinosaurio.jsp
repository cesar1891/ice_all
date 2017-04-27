<%-- 
    Document   : dinosaurio
    Created on : 15/08/2013, 12:44:12 PM
    Author     : Jair
--%>
<%@ include file="/taglibs.jsp"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <script type="text/javascript" src="${ctx}/resources/extjs4/ext-all.js"></script>
        <script type="text/javascript" src="${ctx}/resources/extjs4/base_extjs4.js"></script>
        <script>
            
            Ext.define('Generic',
            {
                extend   : 'Ext.data.Model',
                fields   :
                [
                    {name:'key',     type: 'string'},
                    {name:'value',  type: 'string'}
                ]
            });
            
            Ext.define('Generic2',
            {
                extend   : 'Ext.data.Model',
                fields   :
                [
                    {name:'key',     type: 'string'},
                    {name:'value',  type: 'string'}
                ]
            });
            
            Ext.define('Carrito2',
            {
                extend   : 'Ext.data.Model',
                fields   :
                [
                    {name:'id',     type: 'numeric'},
                    {name:'marca',  type: 'string'},
                    {name:'anio',   type: 'numeric'}
                ]
            });
            
            Ext.define('Dinosaurio2',
            {
                extend   : 'Ext.data.Model',
                fields:
                [
                    {name:'id',      type: 'numeric'},
                    {name:'nombre',  type: 'string'},
                    {name:'edad',    type: 'numeric'}
                ],
                hasOne:
                [{
                    name: 'hijo',
                    model: 'Generic',
                    foreignKey: 'hijo',
                    associationKey: 'hijo'
                },{
                    name: 'hijo2',
                    model: 'Generic2',
                    foreignKey: 'hijo2',
                    associationKey: 'hijo2'
                }],
                hasMany:
                [{
                    name: 'carritos',
                    model: 'Carrito2',
                    foreignKey: 'carritos',
                    associationKey: 'carritos',
                    getterName: 'getCarritos'
                }],
                proxy:
                {
                    type:'ajax',
                    url:'<s:url action="dinosaurio2" namespace="/" />',
                    reader:{
                        type:'json',
                        root:'dino'
                    }
                }
            });
            
            /*Ext.define('MiCotiza',{
                extend:'CotizacionSalud',
                constructor:function(){this.callParent(arguments);}
            });*/
            
            console.log(Ext.create('CotizacionSalud',{}));
            
            var dino2=Ext.ModelManager.getModel('GeCiudad');
            dino2.load(123, {
                success: function(dino) {
                    console.log('###response',dino);
                    console.log('###carritos',dino.get('nombre'));
                }
            });
            
            var Carrito=function()
            {
                this.id="";
                this.marca="";
                this.anio="";
            };
            var Dinosaurio=function()
            {
                this.id="";
                this.nombre="";
                this.edad="";
                this.carritos=[];
            };
            /*
            */
            
            var dinosaurio=new Dinosaurio();
            console.log(dinosaurio);
            dinosaurio.id=25;
            dinosaurio.nombre="Dino";
            dinosaurio.edad=600;
            console.log(dinosaurio);
            
            var c1=new Carrito();
            c1.id=1;
            c1.marca="Nissan";
            c1.anio=2012;
            console.log(c1);
            dinosaurio.carritos.push(c1);
            console.log(dinosaurio);
            
            var c2=new Carrito();
            c2.id=2;
            c2.marca="Chevrolet";
            c2.anio=2014;
            console.log(c2);
            dinosaurio.carritos.push(c2);
            console.log(dinosaurio);
            /*
            Ext.Ajax.request(
            {
                url: "<s:url action="dinosaurio2" namespace="/" />",
                jsonData:{dino:dinosaurio,llave:'yave'},
                success:function(response,opts)
                {
                    var jsonResp = Ext.decode(response.responseText);
                    console.log(jsonResp);
                },
                failure:function(response,opts)
                {
                    console.log("error");
                    Ext.Msg.show({
                        title:'Error',
                        msg: 'Error de comunicaci&oacute;n',
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                }
            });*/
            
        </script>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>RAWR!</h1>
    </body>
</html>
