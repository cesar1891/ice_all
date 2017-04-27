function subirArchivoParse(elemento)
{
    //console.log('subirArchivoParse',elemento);
    $(elemento).find("div.frameSubirArchivo").each(function(){
        $element=$(this);
        //console.log('subirArchivoParse find',$element);
        $target=$element.attr("target");
        $value=$element.attr("value");
        $element.removeAttr('class');
        $element.attr({style:'border:0px solid red;'});
        $element.html('<iframe src="'+urlFrameArchivo+'?targetId='+$target+'&fileId='+$value+'" frameborder="no" scrolling="no" width="200" height="40"></iframe>');
    });
}

function afterExtReady(){
    subirArchivoParse(document);
}