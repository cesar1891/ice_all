///////////////////////
////// variables //////
/*///////////////////*/
var formulario;//deprecated
/*///////////////////*/
////// variables //////
///////////////////////
//deprecated
$(document).ready(function(){//deprecated
    console.log("jQuery listo");//deprecated
});//deprecated
Ext.onReady(function(){//deprecated
    console.log("Ext listo"); //deprecated
    //deprecated
    formulario=Ext.create('Ext.form.Panel',{//deprecated
        frame:true,//deprecated
        title:'Datos complementarios',//deprecated
        renderTo:'maindiv',//deprecated
        width:800,//deprecated
        bodyPadding:5,//deprecated
        items:[//deprecated
            {//deprecated
                xtype:'textfield',//deprecated
                fieldLabel:'texto',//deprecated
                labelWidth:200//deprecated
            },//deprecated
            {//deprecated
                id:'MiIdArchivo',//deprecated
                xtype:'textfield',//deprecated
                fieldLabel:'archivo',//deprecated
                labelWidth:200//deprecated
            },//deprecated
            {//deprecated
                xtype:'panel',//deprecated
                border:false,//deprecated
                height:50,//deprecated
                width:250,//deprecated
                html:'<div class="frameSubirArchivo" target="MiIdArchivo" value="0"></div>'//deprecated
            },//deprecated
            {//deprecated
                xtype:'panel',//deprecated
                border:false,//deprecated
                height:50,//deprecated
                width:250,//deprecated
                html:'<div class="frameSubirArchivo" target="MiIdArchivo" value="0"></div>'//deprecated
            }//deprecated
        ]//deprecated
    });//deprecated
    //deprecated
});//deprecated