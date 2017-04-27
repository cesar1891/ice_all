<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
var smap1 = <s:property value="%{convertToJSON('smap1')}"  escapeHtml="false" />;
Ext.onReady(function()
{
    Ext.create('Ext.panel.Panel',
    {
        renderTo  : '_p42_divpri'
        ,defaults : { style:'margin:5px;' }
        ,items    :
        [
            Ext.create('Ext.panel.Panel',
            {
                defaults : { style : 'margin:5px;' }
                ,layout  :
                {
                    type     : 'table'
                    ,columns : 2
                }
                ,items   :
                [
                    {
                        xtype       : 'displayfield'
                        ,fieldLabel : 'codigo'
                        ,value      : '<s:property value="smap1.error" escapeHtml="false" />'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'timestamp'
                        ,value      : '<s:property value="smap1.timestamp" escapeHtml="false" />'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'fecha'
                        ,value      : '<s:property value="smap1.fecha" escapeHtml="false" />'
                    }
                    ,{
                        xtype       : 'displayfield'
                        ,fieldLabel : 'respuesta'
                        ,value      : '<s:property value="respuesta" escapeHtml="false" />'
                    }
                ]
            })
            ,Ext.create('Ext.panel.Panel',
            {
                html        : smap1.log
                ,autoScroll : true
                ,maxHeight  : 800
            })
        ]
    });
});
</script>
</head>
<body><div id="_p42_divpri"></div></body>
</html>