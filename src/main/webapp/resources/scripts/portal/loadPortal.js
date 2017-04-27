/*
 * AON
 * 
 * Creado el 24/01/2008 11:33:52 a.m. (dd/mm/aaaa hh:mm:ss)
 *
 * Copyright (C) Biosnet S.C.    
 * All rights reserved. Todos los derechos reservados.
 *
 * http://www.biosnetmx.com/
 */



/**
 * 
 * Carga de componentes ExtJs en la pantalla portal.jsp
 */
Ext.onReady(function(){
   
    var elementsBody = new Array();

    // Se asignan variables a los elementos de la pantalla
    elementsBody[0]  = Ext.get('top');
    elementsBody[1]  = Ext.get('nav');
    elementsBody[2]  = Ext.get('top_left');
    elementsBody[3]  = Ext.get('top_center');
    elementsBody[4]  = Ext.get('top_right');
    elementsBody[5]  = Ext.get('left_1');
    elementsBody[6]  = Ext.get('left_2');
    elementsBody[7]  = Ext.get('left_3');
    elementsBody[8]  = Ext.get('left_4');
    elementsBody[9]  = Ext.get('left_5');
    elementsBody[10] = Ext.get('right_1');
    elementsBody[11] = Ext.get('right_2');
    elementsBody[12] = Ext.get('right_3');
    elementsBody[13] = Ext.get('right_4');
    elementsBody[14] = Ext.get('right_5');
    elementsBody[15] = Ext.get('main');
    elementsBody[16] = Ext.get('news');
    elementsBody[17] = Ext.get('knewthat');
    elementsBody[18] = Ext.get('mainDown');
    elementsBody[19] = Ext.get('otherLeft');
    elementsBody[20] = Ext.get('otherRight');
    elementsBody[21] = Ext.get('others');
    elementsBody[22] = Ext.get('footer_left');
    elementsBody[23] = Ext.get('footer_center');
    elementsBody[24] = Ext.get('footer_right');

    var elementsPanel = new Array();
    
    // Se agregan todos los modulos con sus fuentes a la pantalla principal
    for(i = 0 ; i < elementsBody.length ; i++){
        if( sourceForPanel[i] || sourceForPanel[i] != "" ){
            elementsPanel[i] = new Ext.Panel({
                renderTo: elementsBody[i],
                border: false,
                autoHeight: true ,
                autoScroll: true,
                autoWidth: true,
                baseCls: null
            });
			elementsPanel[i].load({
			    url: _CONTEXT + sourceForPanel[i],
			    text: "Cargando...",
			    timeout: 30,
			    scripts: true
			});
        }else{
            var elemento = elementsBody[i].dom;
            elemento = elemento.parentNode;
            elemento.parentNode.removeChild(elemento);
        }
    }
    
    // Se elimina la columna izquierda si no tienen ninguna fuente
    if(!(sourceForPanel[5] || sourceForPanel[6] || 
          sourceForPanel[7] || sourceForPanel[8] || sourceForPanel[9] )){
        var elemento = Ext.get("ContenedorLeft").dom;
        elemento.parentNode.removeChild(elemento);
    }
    
    // Se elimina la columna derecha si no tienen ninguna fuente
    if(!(sourceForPanel[10] || sourceForPanel[11] || 
          sourceForPanel[12] || sourceForPanel[13] || sourceForPanel[14] )){
        var elemento = Ext.get("ContenedorRight").dom;
        elemento.parentNode.removeChild(elemento);
    }
    
});
