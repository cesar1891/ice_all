Ext.define('modelSuperPanel', {
     extend: 'Ext.data.Model',
     fields: [
         {name: 'idPadre', 		type: 'string'},
         {name: 'idHijo',       	type: 'string'},
         {name: 'idGrid', 		type: 'string'},
         {name: 'name', 		type: 'string'},
         {name: 'order', 		type: 'int'},
         {name: 'isRequerido', 		type: 'boolean'},
         {name: 'isRequeridoMsg', 	type: 'string'},
         {name: 'isAnchor',			type:'int'},
         {name: 'width',			type:'int'},
         {name: 'height',			type:'int'},
         {name: 'etiqueta',	type: 'string'},
         {name: 'etiqueta_width',	type:'int'},
         {name: 'etiqueta_aling',	type:'string'},
         {name: 'isBloqueado',		type:'boolean'},
         {name: 'isSeleccionado',	type:'boolean'},
         {name: 'isEditable',		type:'boolean'},
         {name: 'textoSugerido',	type:'string'},
         {name: 'textoMax',			type:'int'},
         {name: 'textoMin',			type:'int'},
         {name: 'textoMaxMsg',		type:'string'},
         {name: 'textoMinMsg',		type:'string'},
         {name: 'soloLectura',		type:'boolean'},
         {name: 'toolTip',			type:'string'},
         {name: 'texto',			type:'string'},
         {name: 'valorMax',			type:'int'},
         {name: 'valorMin',			type:'int'},
         {name: 'valorMaxMsg',		type:'string'},
         {name: 'valorMinMsg',		type:'string'},
         {name: 'isFlechas',		type:'boolean'},
         {name: 'fecha',			type:'string'},
         {name: 'fechaMax',			type:'string'},
         {name: 'fechaMaxMsg',		type:'string'},
         {name: 'fechaMin',			type:'string'},
         {name: 'fechaMinMsg',		type:'string'},
         {name: 'imagenCls',		type:'string'},
         {name: 'imagen_aling',		type:'string'},
         {name: 'margen',			type:'string'},
         {name: 'padding',			type:'string'},
         {name: 'multiSelect',		type:'boolean'},
         {name: 'store',		type:'string'},
         {name: 'modo',		type:'string'},
         {name: 'selectconFoco',		type:'boolean'},
         {name: 'selectAction',		type:'string'},
         {name: 'valorDisplay',		type:'string'},
		 {name: 'valorId',		type:'string'},
		 {name: 'isFlecha',		type:'boolean'},
		 {name: 'isAutoComp',		type:'boolean'},
		 {name: 'delimitador',		type:'string'},
		 {name: 'row',type:'int'},
		 {name: 'tabs',type:'int'},
		 {name: 'isDesplegable',type:'boolean'},
		 {name: 'isFondo',type:'boolean'},
		 {name: 'isCerrable',type:'boolean'},
		 {name: 'bodyPadding',type:'string'},
		 {name: 'isAutoScroll',type:'boolean'},
		 {name: 'isResizable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'},
		 {name: 'titulo_Aling',type:'string'},
		 {name: 'columnas',			type:'int'},
		 {name: 'fechaInvalidTxt',	type:'string'},
		 {name: 'escala',type:'string'},
		 {name: 'tipo',type:'string'},
		 {name: 'tipoG',type:'string'},
		 {name: 'isPadre',type:'string'},
		 {name: 'url',type:'string'},
		 {name: 'estilo',type:'string'},
		 {name: 'titulo',type:'string'},
		 {name: 'isModal',type:'boolean'},
		 {name: 'isMinimizable',type:'boolean'},
		 {name: 'cordX',			type:'int'},
		 {name: 'cordY',			type:'int'},
		 {name: 'html',type:'string'},
		 {name: 'src',type:'string'},
		 {name: 'titulo_Posicion',type:'string'},
		 {name: 'dataIndex', type: 'string'},
		 {name: 'query', type: 'string'},
		 {name: 'columna_orden',type:'boolean'},
		 {name: 'columna_hidden',type:'boolean'},
         {name: 'columna_move',type:'boolean'},
         {name: 'columna_resize',type:'boolean'},
         {name: 'winEstilo',type:'string'},
         {name: 'isBorder',type:'boolean'}
     ]
});
Ext.define('modelBasico', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)', 			type: 'string'}
     ]
});
Ext.define('modelFormulario', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)', 				type: 'string'},
         {name: 'titulo',       	type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'height',			type:'int'},
         {name: 'width',			type:'int'},
         {name: 'isDesplegable',type:'boolean'},
         {name: 'margen',			type:'string'},
		 {name: 'isFondo',type:'boolean'},
		 {name: 'isCerrable',type:'boolean'},
		 {name: 'bodyPadding',type:'string'},
		 {name: 'isAutoScroll',type:'boolean'},
		 {name: 'isResizable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'},
		 {name: 'url',type:'string'},
		 {name: 'titulo_Aling',type:'string'}

     ]
});
Ext.define('modelBorder', {
    extend: 'Ext.data.Model',
    fields: [
        {name: '(id)', 			type: 'string'},
        {name: 'titulo',       	type: 'string'},
        {name: 'tipo', 			type: 'string'},
        {name: 'height',		type:'int'},
        {name: 'width',			type:'int'},
        {name: 'isDesplegable',type:'boolean'},
        {name: 'margen',			type:'string'},
        {name: 'isFondo',type:'boolean'},
        {name: 'isCerrable',type:'boolean'},
        {name: 'bodyPadding',type:'string'},
        {name: 'isAutoScroll',type:'boolean'},
        {name: 'isResizable',type:'boolean'},
        {name: 'isBodyBorder',type:'boolean'},
        {name: 'nombre', 	    type: 'string'},
        {name: 'titulo_Aling',type:'string'}
    ]
});
Ext.define('modelBorderSur', {
    extend: 'Ext.data.Model',
    fields: [
        {name: '(id)', 			type: 'string'},
        {name: 'titulo',       	type: 'string'},
        {name: 'bodyPadding',	type:'string'},	        
        {name: 'isFondo',		type:'boolean'},
        {name: 'height',		type:'int'},  
        {name: 'isAutoScroll',	type:'boolean'},
        {name: 'isCerrable',	type:'boolean'},
        {name: 'isBodyBorder',	type:'boolean'},
        {name: 'isDesplegable',	type:'boolean'},
        {name: 'nombre', 	    type: 'string'},
        {name: 'margen',		type:'string'},
        {name: 'titulo_Aling',	type:'string'},  
        {name: 'columnas',			type:'int'},
        {name: 'tipo', 			type: 'string'}
    ]
});
Ext.define('modelBorderCenter', {
    extend: 'Ext.data.Model',
    fields: [
        {name: '(id)', 			type: 'string'},
        {name: 'titulo',       	type: 'string'},
        {name: 'bodyPadding',	type:'string'},	        
        {name: 'isFondo',		type:'boolean'},
        {name: 'isAutoScroll',	type:'boolean'},
        {name: 'isCerrable',	type:'boolean'},
        {name: 'isBodyBorder',	type:'boolean'},
        {name: 'isDesplegable',	type:'boolean'},
        {name: 'nombre', 	    type: 'string'},
        {name: 'margen',		type:'string'},
        {name: 'titulo_Aling',	type:'string'},  
        {name: 'columnas',			type:'int'},
        {name: 'tipo', 			type: 'string'}
    ]
});
Ext.define('modelBorderIzq', {
    extend: 'Ext.data.Model',
    fields: [
        {name: '(id)', 			type: 'string'},
        {name: 'titulo',       	type: 'string'},
        {name: 'bodyPadding',	type:'string'},	        
        {name: 'isFondo',		type:'boolean'},
        {name: 'width',		type:'int'},  
        {name: 'isAutoScroll',	type:'boolean'},
        {name: 'isCerrable',	type:'boolean'},
        {name: 'isBodyBorder',	type:'boolean'},
        {name: 'isDesplegable',	type:'boolean'},
        {name: 'nombre', 	    type: 'string'},
        {name: 'margen',		type:'string'},
        {name: 'titulo_Aling',	type:'string'},  
        {name: 'columnas',			type:'int'},
        {name: 'tipo', 			type: 'string'}
    ]
});
Ext.define('modelColumnas', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)', 				type: 'string'},
         {name: 'titulo',       	type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'height',			type:'int'},
         {name: 'width',			type:'int'},
         {name: 'columnas',			type:'int'},
         {name: 'margen',			type:'string'},
         {name: 'isDesplegable',type:'boolean'},
		 {name: 'isFondo',type:'boolean'},
		 {name: 'isCerrable',type:'boolean'},
		 {name: 'bodyPadding',type:'string'},
		 {name: 'isAutoScroll',type:'boolean'},
		 {name: 'isResizable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'},
		 {name: 'url',type:'string'},
		 {name: 'titulo_Aling',type:'string'}

     ]
});
Ext.define('modelWindow', {
    extend: 'Ext.data.Model',
    fields: [
        {name: '(id)', 				type: 'string'},
        {name: 'titulo',       	type: 'string'},
        {name: 'tipo', 			type: 'string'},
        {name: 'nombre', 			type: 'string'},
        {name: 'height',			type:'int'},
        {name: 'width',			type:'int'},
        {name: 'isDesplegable',type:'boolean'},
        {name: 'winEstilo',type:'string'},
		 {name: 'isFondo',type:'boolean'},
		 {name: 'isCerrable',type:'boolean'},
		 {name: 'bodyPadding',type:'string'},
		 {name: 'isAutoScroll',type:'boolean'},
		 {name: 'isResizable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'},
		 {name: 'columnas',			type:'int'},
		 {name: 'titulo_Posicion',type:'string'},
		 {name: 'isModal',type:'boolean'},
		 {name: 'isMinimizable',type:'boolean'},
		 {name: 'cordX',			type:'int'},
		 {name: 'cordY',			type:'int'},
		 {name: 'titulo_Aling',type:'string'}

    ]
});
Ext.define('modelTabs', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)', 				type: 'string'},
         {name: 'titulo',       	type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'height',			type:'int'},
         {name: 'width',			type:'int'},
         {name: 'tabs',			type:'int'},
         {name: 'margen',			type:'string'},
         {name: 'isDesplegable',type:'boolean'},
		 {name: 'isFondo',type:'boolean'},
		 {name: 'isCerrable',type:'boolean'},
		 {name: 'bodyPadding',type:'string'},
		 {name: 'isAutoScroll',type:'boolean'},
		 {name: 'isResizable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'},
		 {name: 'titulo_Aling',type:'string'}

     ]
});

Ext.define('modelTabsIn', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)', 				type: 'string'},
         {name: 'titulo',       	type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'height',			type:'int'},
         {name: 'width',			type:'int'},
         {name: 'columnas',			type:'int'},
         {name: 'isFondo',type:'boolean'},
         {name: 'bodyPadding',type:'string'},
         {name: 'isAutoScroll',type:'boolean'},
   		 {name: 'isCerrable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'}


     ]
});
Ext.define('modelAcordion', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)',type: 'string'},
         {name: 'titulo',type: 'string'},
         {name: 'tipo',type: 'string'},
         {name: 'nombre',type: 'string'},
         {name: 'height',type:'int'},
         {name: 'width',type:'int'},
         {name: 'tabs',type:'int'},
         {name: 'margen',type:'string'},
         {name: 'isDesplegable',type:'boolean'},
		 {name: 'isFondo',type:'boolean'},
		 {name: 'isCerrable',type:'boolean'},
		 {name: 'bodyPadding',type:'string'},
		 {name: 'isAutoScroll',type:'boolean'},
		 {name: 'isResizable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'},
		 {name: 'titulo_Aling',type:'string'}
     ]
});
Ext.define('modelAcordionIn', {
     extend: 'Ext.data.Model',
     fields: [
         {name: '(id)', 				type: 'string'},
         {name: 'titulo',       	type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'height',			type:'int'},
         {name: 'width',			type:'int'},
         {name: 'columnas',			type:'int'},
         {name: 'isFondo',type:'boolean'},
         {name: 'bodyPadding',type:'string'},
         {name: 'isAutoScroll',type:'boolean'},
   		 {name: 'isCerrable',type:'boolean'},
		 {name: 'isBodyBorder',type:'boolean'}
     ]
});
Ext.define('modelTextAttr', {
     extend: 'Ext.data.Model',
     fields: [
     	 {name: '(id)', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'etiqueta',       	type: 'string'},
         {name: 'etiqueta_aling',	type:'string'},
         {name: 'isRequerido', 		type: 'boolean'},
         {name: 'isRequeridoMsg', 	type: 'string'},
         {name: 'isAnchor',			type:'int'},
         {name: 'tipo', 			type: 'string'},
         {name: 'width',			type:'int'},
         {name: 'height',			type:'int'},
         {name: 'etiqueta_width',	type:'int'},
         {name: 'isBloqueado',		type:'boolean'},
         {name: 'textoSugerido',	type:'string'},
         {name: 'isPadre',			type:'string'},
         {name: 'textoMax',			type:'int'},
         {name: 'textoMin',			type:'int'},
         {name: 'textoMaxMsg',		type:'string'},
         {name: 'textoMinMsg',		type:'string'},
         {name: 'soloLectura',		type:'boolean'},
         {name: 'toolTip',			type:'string'},
         {name: 'texto',			type:'string'},
         {name: 'margen',			type:'string'},
         {name: 'padding',			type:'string'},
         {name: 'zuprimir',			type:'boolean'}
     ]
});
Ext.define('modelNumericAttr', {
     extend: 'Ext.data.Model',
     fields: [
     		{name: '(id)', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'etiqueta',       	type: 'string'},
         {name: 'etiqueta_aling',	type:'string'},
         {name: 'etiqueta_width',	type:'int'},
         {name: 'height',			type:'int'},
         {name: 'isAnchor',			type:'int'},
         {name: 'isBloqueado',		type:'boolean'},
         {name: 'isRequerido', 		type: 'boolean'},
         {name: 'isRequeridoMsg', 	type: 'string'},
         {name: 'isEditable',		type:'boolean'},
		 {name: 'soloLectura',		type:'boolean'},
         {name: 'texto',			type:'int'},
         {name: 'textoMax',			type:'int'},
         {name: 'textoMaxMsg',		type:'string'},
         {name: 'textoMin',			type:'int'},
         {name: 'textoMinMsg',		type:'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'toolTip',			type:'string'},
         {name: 'width',			type:'int'},
         {name: 'valorMax',			type:'int'},
         {name: 'valorMin',			type:'int'},
         {name: 'valorMaxMsg',		type:'string'},
         {name: 'valorMinMsg',		type:'string'},
         {name: 'isFlechas',		type:'boolean'},
         {name: 'margen',			type:'string'},
         {name: 'padding',			type:'string'},
		 {name: 'zuprimir',			type:'boolean'}
     ]
});
Ext.define('modelPickerAttr', {extend: 'Ext.data.Model',fields: [{name: '(id)',type: 'string'},{name: 'nombre',type: 'string'},{name: 'etiqueta',type: 'string'},{name: 'etiqueta_aling',type:'string'},{name: 'etiqueta_width',type:'int'},{name: 'height',type:'int'},{name: 'isAnchor',type:'int'},{name: 'isBloqueado',type:'boolean'},{name: 'isRequerido',type: 'boolean'},{name: 'isRequeridoMsg',type: 'string'},{name: 'isEditable',type:'boolean'},{name: 'soloLectura',type:'boolean'},{name: 'fecha',type:'string'},{name: 'textoMax',type:'int'},{name: 'textoMaxMsg',type:'string'},{name: 'textoMin',type:'int'},{name: 'textoMinMsg',type:'string'},{name: 'tipo',type: 'string'},{name: 'toolTip',type:'string'},{name: 'width',type:'int'},{name: 'fechaMax',type:'string'},{name: 'fechaMaxMsg',type:'string'},{name: 'fechaMin',type:'string'},{name: 'textoSugerido',type:'string'},{name: 'fechaMinMsg',type:'string'},{name: 'margen',type:'string'},{name: 'padding',type:'string'},{name: 'fechaInvalidTxt',type:'string'},{name: 'isPadre',type:'string'},{name: 'zuprimir',type:'boolean'}]});
Ext.define('modelCheckAttr', {extend: 'Ext.data.Model',fields: [{name: '(id)',type: 'string'},{name: 'etiqueta',type: 'string'},{name: 'etiqueta_width',type:'int'},{name: 'etiqueta_aling',type:'string'},{name: 'height',type:'int'},{name: 'isAnchor',type:'int'},{name: 'isBloqueado',type:'boolean'},{name: 'isSeleccionado',type:'boolean'},{name: 'nombre',type: 'string'},{name: 'soloLectura',type:'boolean'},{name: 'tipo',type: 'string'},{name: 'toolTip',type:'string'},{name: 'width',type:'int'},{name: 'margen',type:'string'},{name: 'padding',type:'string'},{name: 'zuprimir',type:'boolean'}]});
Ext.define('modelRadioAttr', {extend: 'Ext.data.Model',fields: [{name: '(id)',type: 'string'},{name: 'etiqueta',type: 'string'},{name: 'etiqueta_width',type:'int'},{name: 'etiqueta_aling',type:'string'},{name: 'height',type:'int'},{name: 'isAnchor',type:'int'},{name: 'isBloqueado',type:'boolean'},{name: 'isSeleccionado',type:'boolean'},{name: 'nombre',type: 'string'},{name: 'soloLectura',type:'boolean'},{name: 'tipo',type: 'string'},{name: 'toolTip',type:'string'},{name: 'width',type:'int'},{name: 'margen',type:'string'},{name: 'padding',type:'string'},{name: 'zuprimir',type:'boolean'}]});
Ext.define('modelBotonAttr', {extend: 'Ext.data.Model',fields: [{name: '(id)',type: 'string'},{name: 'estilo',type:'string'},{name: 'texto',type: 'string'},{name: 'height',type:'int'},{name: 'isAnchor',type:'int'},{name: 'isBloqueado',type:'boolean'},{name: 'nombre',type: 'string'},{name: 'tipo',type: 'string'},{name: 'toolTip',type:'string'},{name: 'width',type:'int'},{name: 'imagenCls',type:'string'},{name: 'imagen_aling',type:'string'},{name: 'margen',type:'string'},{name: 'padding',type:'string'},{name: 'escala',type:'string'},{name: 'row',type:'int'},{name: 'zuprimir',type:'boolean'}]});
Ext.define('modelComboAttr', {extend: 'Ext.data.Model',fields: [{name: '(id)',type: 'string'},{name: 'height',type:'int'},{name: 'width',type:'int'},{name: 'tipo',type: 'string'},{name: 'etiqueta',type: 'string'},{name: 'etiqueta_width',type:'int'},{name: 'etiqueta_aling',type:'string'},{name: 'isAnchor',type:'int'},{name: 'isBloqueado',type:'boolean'},{name: 'isRequerido',type: 'boolean'},{name: 'isRequeridoMsg',type: 'string'},{name: 'isEditable',type:'boolean'},{name: 'nombre',type: 'string'},{name: 'soloLectura',type:'boolean'},{name: 'texto',type:'string'},{name: 'textoSugerido',type:'string'},{name: 'textoMax',type:'int'},{name: 'textoMin',type:'int'},{name: 'textoMaxMsg',type:'string'},{name: 'textoMinMsg',type:'string'},{name: 'margen',type:'string'},{name: 'padding',type:'string'},{name: 'toolTip',type:'string'},{name: 'multiSelect',type:'boolean'},{name: 'store',type:'string'},{name: 'modo',type:'string'},{name: 'selectconFoco',type:'boolean'},{name: 'selectAction',type:'string'},{name: 'valorDisplay',type:'string'},{name: 'valorId',type:'string'},{name: 'isFlecha',type:'boolean'},{name: 'isAutoComp',type:'boolean'},{name: 'delimitador',type:'string'},{name: 'isPadre',type:'string'},{name: 'zuprimir',type:'boolean'}]});
Ext.define('modelComboBox',{extend:'Ext.data.Model',fields:[{name:'key',type:'string'},{name:'value',type:'string'}]});
Ext.define('modelTree',{extend:'Ext.data.Model',fields:[{name:'text',type:'string'}]});
Ext.define('modelLabelAttr', {
     extend: 'Ext.data.Model',
     fields: [
     	 {name: '(id)', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'width',			type:'int'},
         {name: 'height',			type:'int'},
         {name: 'margen',			type:'string'},
         {name: 'texto',			type:'string'},
         {name: 'html',type:'string'},
         {name: 'zuprimir',			type:'boolean'}
     ]
});
Ext.define('modelImagenAttr', {
     extend: 'Ext.data.Model',
     fields: [
     	 {name: '(id)', 			type: 'string'},
         {name: 'nombre', 			type: 'string'},
         {name: 'tipo', 			type: 'string'},
         {name: 'width',			type:'int'},
         {name: 'height',			type:'int'},
         {name: 'src',	type:'string'},
         {name: 'margen',			type:'string'},
         {name: 'zuprimir',			type:'boolean'}
     ]
});
Ext.define('modelHiddenAttr', {
    extend: 'Ext.data.Model',
    fields: [
    	 {name: '(id)', 			type: 'string'},
        {name: 'nombre', 			type: 'string'},
        {name: 'tipo', 			type: 'string'},
        {name: 'texto',			type:'string'},
        {name: 'zuprimir',			type:'boolean'}
    ]
});
Ext.define('modelGridAttr', {
    extend: 'Ext.data.Model',
    fields: [
    	 {name: '(id)', 			type: 'string'},
        {name: 'nombre', 			type: 'string'},
        {name: 'tipo', 			type: 'string'},
        {name: 'width',			type:'int'},
        {name: 'height',			type:'int'},
        {name: 'margen',			type:'string'},
        {name: 'zuprimir',			type:'boolean'},
        {name: 'query', 	type: 'string'},
        {name: 'titulo',type:'string'},
        {name: 'titulo_Aling',type:'string'},
        {name: 'columna_orden',type:'boolean'},
        {name: 'isResizable',type:'boolean'},
        {name: 'isFondo',type:'boolean'},
        {name: 'columna_hidden',type:'boolean'},
        {name: 'columna_move',type:'boolean'},
        {name: 'columna_resize',type:'boolean'},
        {name: 'isDesplegable',type:'boolean'},
        {name: 'isCerrable',type:'boolean'},
        {name: 'isBodyBorder',type:'boolean'}
    ]
});
Ext.define('modelGridDef',{ extend: 'Ext.data.Model', 
	fields: [ {name: 'texto', type: 'string'},
	          {name: 'width', type: 'int'},
	          {name: 'tipoG', type: 'string'},
	          {name: 'name', type: 'string'},
	          {name: 'dataIndex', type: 'string'}
	 ] 
});




















































