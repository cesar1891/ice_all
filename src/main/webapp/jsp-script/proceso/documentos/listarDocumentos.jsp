<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core"              prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"               prefix="fmt" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="/struts-tags"                                   prefix="s" %>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style></style>

<!-- Libreria EXTJS4 -->
<link href="../../../resources/extjs4/resources/my-custom-theme/my-custom-theme-all.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../../resources/extjs4/ext-all.js"></script>
<script type="text/javascript" src="../../../resources/extjs4/locale/ext-lang-es.js"></script>
        
<script>

////// overrides //////


////// variables //////
var _CONTEXT = '${ctx}';

var _p21_urlListarDirectorio            = '<s:url namespace="/seguridad"         action="listarDirectorio"            />';
var _p21_urlDescargaDoc                 ='../../../documentos/descargaDoc.action?';
var _p21_pathListarDirectorio           ='/RES/logs/gseguros';

var _p21_pathDescargarDirectorio        ='/RES/logs';
var _p21_subfolderDescargarDirectorio   ='gseguros';
var _p21_nombreArchDescargar            ='';


////// onReady //////

Ext.onReady(function()
{
    ////// modelos //////
    var modeloDocumentos = Ext.define('modeloDocumentos',
    {
        extend  : 'Ext.data.Model'
        ,fields :
        [
            {type:'string', name:'nombreDoc'},
            {type:'string', name:'tamDoc'},
            {type:'string', name:'modificado'},
            {type:'string', name:'permisos'},
            {type:'string', name:'propietario'}
        ]
    });
    
   
    ////// stores //////
    var storeDocumentos = Ext.create('Ext.data.Store', {
		model             : 'modeloDocumentos',
		proxy             : {
			type          : 'ajax'
			,url          : _p21_urlListarDirectorio
		    ,extraParams  :  {path:_p21_pathListarDirectorio} 
		    ,reader       :
		    {
		    	type      : 'json'
		      	,root     : 'slist1'
		    }
		    	,autoLoad : true
		},
		sorters           :[
	    	{
	    		sorterFn  : function(o1,o2){
	            	//console.log(o1.get('tamDoc'), "-------", o2.get('tamDoc'));
	            	//console.log('sorting:',o1,o2);
	                if (o1.get('tamDoc') === o2.get('tamDoc')){
	                	return 0;
	                }
	                return o1.get('tamDoc')-0 < o2.get('tamDoc')-0 ? -1 : 1;
	            }
	    	}
		]
	});
    
    
    ////// componentes //////
	var gridDocumentos = Ext.create('Ext.grid.Panel', {
	    title: 'Documentos del Servidor',
	    itemId: 'gridDocumentos',
	    //layout: 'fit',
	    flex  : 1,
		selType: 'cellmodel',
	    store: storeDocumentos,
	    columns: [
	        { text: 'nombreDoc', dataIndex:'nombreDoc'},
	        { text: 'tamDoc', dataIndex:'tamDoc'},
	        { text: 'modificado', dataIndex:'modificado'},
	        { text: 'permisos', dataIndex:'permisos'},
	        { text: 'propietario', dataIndex:'propietario'},
	        { xtype    :'actioncolumn',
	            text     : 'Descargar',
	            flex     : 1,
	            items: [{
	                tooltip : 'Descargar Documento',
	                icon    : '${ctx}/resources/fam3icons/icons/disk.png',
	                handler : function(gridDocumentos, rowIndex, colIndex) {
	                	var record = gridDocumentos.getStore().getAt(rowIndex);
	                	_p21_nombreArchDescargar = record.data.nombreDoc;
	                	console.log("record.nombreDoc ", _p21_nombreArchDescargar);
	                	descargar(_p21_nombreArchDescargar);
	                }
	            }]
	        }
	    	],
	    width: '100%',
	    forceFit: true,
	    //selType: 'cellmodel',
	    listeners: {
			boxready: function (sm, selectedRecord) {
				storeDocumentos.load()
			} 
	    }
	});
	
    ////// contenido //////
    Ext.create('Ext.Panel', {
	    bodyPadding: 5,  
	    width: '50%',
	    title: '',
	    itemId: 'panelPpal',
	    allign: 'center',
	    items: [
	    	{
	    		xtype: gridDocumentos
	    	}
	    ],
	    renderTo: 'divDocumentos'
	});
    ////// custom //////
	
	////// funciones //////
	
	function descargar(_p21_nombreArchDescargar){
    	Ext.create('Ext.form.Panel').submit(
        {
            url              : _p21_urlDescargaDoc
            , standardSubmit : true
            , target         : '_blank'
            , params         :
            {
            	path:_p21_pathDescargarDirectorio
		    	,subfolder:_p21_subfolderDescargarDirectorio
		    	,filename:_p21_nombreArchDescargar
            }
        });
    }

});
</script>

</head>
<body>
	<div id="divDocumentos" style="height:700px;" allign="right"></div>
</body>
</html>