var storeIconos = Ext.create('Ext.data.Store',{
	autoLoad: true,
	fields: ['name', 'thumb', {name: 'leaf', defaultValue: true}],
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/icons.json',
        reader: {type: 'json',root: ''}
   }
});

var storeBasico = Ext.create('Ext.data.Store',{
	model: 'modelBasico',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBasico'}
   }
});

var storeSuperPanel = Ext.create('Ext.data.Store',{
	model: 'modelSuperPanel',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/spanel.json',
        reader: {type: 'json',root: ''}
   }
});

var storeFormulario = Ext.create('Ext.data.Store',{
	model: 'modelFormulario',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrFormulario'}
   }
});
var storeBorder = Ext.create('Ext.data.Store',{
	model: 'modelBorder',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBorder'}
   }
});
var storeBorderSur = Ext.create('Ext.data.Store',{
	model: 'modelBorderSur',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBorderSur'}
   }
});
var storeBorderNorte = Ext.create('Ext.data.Store',{
	model: 'modelBorderSur',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBorderSur'}
   }
});
var storeBorderIzq = Ext.create('Ext.data.Store',{
	model: 'modelBorderIzq',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBorderSur'}
   }
});
var storeBorderCenter = Ext.create('Ext.data.Store',{
	model: 'modelBorderCenter',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBorderSur'}
   }
});
var storeBorderDer = Ext.create('Ext.data.Store',{
	model: 'modelBorderIzq',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBorderSur'}
   }
});
var storeColumnas = Ext.create('Ext.data.Store',{
	model: 'modelColumnas',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrColumnas'}
   }
});
var storeTabs = Ext.create('Ext.data.Store',{
	model: 'modelTabs',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrTabs'}
   }
});
var storeTabsIn = Ext.create('Ext.data.Store',{
	model: 'modelTabsIn',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrTabsIn'}
   }
});
var storeAcordion = Ext.create('Ext.data.Store',{
	model: 'modelAcordion',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrAcordion'}
   }
});
var storeWindow = Ext.create('Ext.data.Store',{
	model: 'modelWindow',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrWindow'}
   }
});
var storeAcordionIn = Ext.create('Ext.data.Store',{
	model: 'modelAcordionIn',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrAcordionIn'}
   }
});
var storeTextAttr = Ext.create('Ext.data.Store',{
	model: 'modelTextAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrText'}
   }
});
var storeNumericAttr = Ext.create('Ext.data.Store',{
	model: 'modelNumericAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrNumeric'}
   }
});
var storePickerAttr = Ext.create('Ext.data.Store',{
	model: 'modelPickerAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrPicker'}
   }
});
var storeCheckAttr = Ext.create('Ext.data.Store',{
	model: 'modelCheckAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrCheck'}
   }
});
var storeRadioAttr = Ext.create('Ext.data.Store',{
	model: 'modelRadioAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrRadio'}
   }
});
var storeBotonAttr = Ext.create('Ext.data.Store',{
	model: 'modelBotonAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrBoton'}
   }
});
var storeComboAttr = Ext.create('Ext.data.Store',{
	model: 'modelComboAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrCombo'}
   }
});

var storeListaEtiqueta_Aling = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_etiqueta_Aling'}
   }
});
var storeListaEscala = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_Escala'}
   }
});
var storeListaTipoDatos = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_TipoDatos'}
   }
});
var storeListaEstilo = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_Estilo'}
   }
});
var storeListaWinEstilo = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_WinEstilo'}
   }
});
var storeListaIconCls = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_IconCls'}
   }
});
var storeListaIconAling = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_IconAling'}
   }
});
var storetitulo_Posicion = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_titulo_Posicion'}
   }
});
var storeListaModo = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_Modo'}
   }
});
var storeListaSelectAction = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_SelectAction'}
   }
});
var storeListaValorDisplay = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_ValorDisplay'}
   }
});
var storeListaValorId = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_ValorId'}
   }
});
var storeListaTituloAling = Ext.create('Ext.data.Store',{
	model: 'modelComboBox',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'lista_TituloAling'}
   }
});
var storeLabelAttr = Ext.create('Ext.data.Store',{
	model: 'modelLabelAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrLabel'}
   }
});
var storeImagenAttr = Ext.create('Ext.data.Store',{
	model: 'modelImagenAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrImagen'}
   }
});
var storeHiddenAttr = Ext.create('Ext.data.Store',{
	model: 'modelHiddenAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrHidden'}
   }
});
var storeGridAttr = Ext.create('Ext.data.Store',{
	model: 'modelGridAttr',
	autoLoad: true,
	proxy: {
    	type: 'ajax',
        url : '../../js/confpantallas/data/attr.json',
        reader: {type: 'json',root: 'attrGrid'}
   }
});
var storeG = Ext.create('Ext.data.Store', { 
	model: 'modelGridDef', 
	autoLoad: true, 
	proxy: { type: 'ajax', url : '../../js/confpantallas/data/attr.json', 
		reader: {type: 'json',root: 'attrGridDef'} } });


























