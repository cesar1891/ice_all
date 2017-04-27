<%@ include file="/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/resources/jsPlumb/jsPlumb-2.0.4.js?${now}"></script>
<script>
Ext.onReady(function()
{
    alert(1);
    
    jsPlumb.ready(function(){
        
        alert(2);
        
        var jsp1 = jsPlumb.getInstance();
        var jsp2 = jsPlumb.getInstance();
        
        jsp1.setContainer('div1');
        jsp2.setContainer('div2');
        
        jsp1.addEndpoint('div1');
        jsp2.addEndpoint('div2');
        
        //$('#div1').hide();
        
        jsp1.draggable('div1');
        jsp2.draggable('div2');
        
        /*jsp1.batch(function()
        {
            for(var i=0;i<100;i++)
            {
                jsp1.addEndpoint('div1');
            }
        });*/
        
        /*alert('zoom');
        
        $("#div1").css(
        {
            "-webkit-transform":"scale(0.5)"
            ,"-moz-transform":"scale(0.5)"
            ,"-ms-transform":"scale(0.5)"
            ,"-o-transform":"scale(0.5)"
            ,"transform":"scale(0.5)"
        });
        
        jsp1.setZoom(0.5);*/
        
        /*alert('zoom2');
        
        _setZoom(0.5,jsp1);*/
        
        /*jsp1.connect(
        {
            source    : 'div1'
            ,target   : 'div2'
            ,endpoint : 'Rectangle'
        });*/
        
        /*var common =
        {
            cssClass:"myCssClass"
        };
        
        jsp1.connect(
        {
            source     : "div1"
            ,target    : "div2"
            ,anchor    : [ "Continuous", { faces:["top","bottom"] }]
            ,endpoint  : [ "Dot", { radius:5, hoverClass:"myEndpointHover" }, common ]
            ,connector : [ "Bezier", { curviness:100 }, common ]
            ,overlays  : [
                [ "Arrow", { foldback:0.2 }, common ]
                ,[ "Label", { cssClass:"labelClass" } ]
            ]
        });*/
        
        /*jsp1.connect(
        {
            source : 'div1'
            ,target : 'div2'
            ,overlays :
            [
                [ 'PlainArrow', {location:1}]
            ]
        });*/
        
        jsp1.connect(
        {
            source:"div1"
            ,target:"div2"
            ,anchors:["Right", "Left" ]
            ,endpoint:"Rectangle"
            ,endpointStyle:{ fillStyle: "yellow" }
            ,detachable : false
        });
        
        jsp1.makeSource('div1');
        jsp2.makeTarget('div2');
        
        jsp1.addEndpoint('div3',{isSource:true,isTarget:true});
        jsp1.addEndpoint('div4',{isSource:true,isTarget:true});
        
        jsp1.addEndpoint('div5',{isSource:true,isTarget:true,anchor:'Continuous'});
        jsp1.addEndpoint('div6',{isSource:true,isTarget:true,anchor:['Top','Bottom','Right','Left']});
        jsp1.addEndpoint('div7',{isSource:true,isTarget:true});
        jsp1.addEndpoint('div8',{isSource:true,isTarget:true});
        
        jsp1.addEndpoint('div9',{isSource:true,isTarget:true});
        jsp1.addEndpoint('div10',{isSource:true,isTarget:true,anchor:['Top','Bottom','Right','Left']});
        jsp1.addEndpoint('div11',{isSource:true,isTarget:true});
        jsp1.addEndpoint('div12',{isSource:true,isTarget:true,anchor:'Continuous'});
        
    });
    
});

////// funciones //////
//https://jsplumbtoolkit.com/community/doc/zooming.html
_setZoom = function(zoom, instance, transformOrigin, el) {
  transformOrigin = transformOrigin || [ 0.5, 0.5 ];
  instance = instance || jsPlumb;
  el = el || instance.getContainer();
  var p = [ "webkit", "moz", "ms", "o" ],
      s = "scale(" + zoom + ")",
      oString = (transformOrigin[0] * 100) + "% " + (transformOrigin[1] * 100) + "%";

  for (var i = 0; i < p.length; i++) {
    el.style[p[i] + "Transform"] = s;
    el.style[p[i] + "TransformOrigin"] = oString;
  }

  el.style["transform"] = s;
  el.style["transformOrigin"] = oString;

  instance.setZoom(zoom);    
};
////// funciones //////

</script>
</head>
<body>
<div id="div1" style="width:200px;height:200px;border:1px solid red;display:inline-block;margin:5px;position:absolute;"></div>
<div id="div3" style="width:200px;height:200px;border:1px solid green;display:inline-block;margin:5px;"></div>
<div id="div2" style="width:200px;height:200px;border:1px solid blue;display:inline-block;margin:5px;position:absolute;"></div>
<div id="div4" style="width:200px;height:200px;border:1px solid yellow;display:inline-block;margin:5px;"></div>
<br/>
<div id="div5" style="width:200px;height:200px;border:1px solid red;display:inline-block;margin:5px;"></div>
<div id="div6" style="width:200px;height:200px;border:1px solid yellow;display:inline-block;margin:5px;"></div>
<div id="div7" style="width:200px;height:200px;border:1px solid green;display:inline-block;margin:5px;"></div>
<div id="div8" style="width:200px;height:200px;border:1px solid blue;display:inline-block;margin:5px;"></div>
<br/>
<div id="div9" style="width:200px;height:200px;border:1px solid blue;display:inline-block;margin:5px;"></div>
<div id="div10" style="width:200px;height:200px;border:1px solid red;display:inline-block;margin:5px;"></div>
<div id="div11" style="width:200px;height:200px;border:1px solid yellow;display:inline-block;margin:5px;"></div>
<div id="div12" style="width:200px;height:200px;border:1px solid green;display:inline-block;margin:5px;"></div>
</body>
</html>