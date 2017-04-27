Ext.onReady(function(){
    
    Ext.define('SubirArchivoPanel',{
        id:'formArchivo',
        extend:'Ext.form.Panel',
        bodyPadding: 5,
        frame:false,
        width:190,
        renderTo:'maindiv',
        layout:'hbox',
        url:URL_SUBIR_ARCHIVO,
        items:[
            {
                xtype: 'filefield',
                buttonOnly: true,
                name:'file',
                cAccept        : ['jpg','png','gif'],
                  listeners     : {
                      change : function(me) {
                          this.up().getForm().setValues({uploadKey:''});
                          // This gets the part of the file name after the last period
                          var indexofPeriod = me.getValue().lastIndexOf("."),
                              uploadedExtension = me.getValue().substr(indexofPeriod + 1, me.getValue().length - indexofPeriod);

                          // See if the extension is in the 
                                //array of acceptable file extensions
                          if (!Ext.Array.contains(this.cAccept, uploadedExtension)){
                              // Add the tooltip below to 
                                // the red exclamation point on the form field
                              me.setActiveError('Please upload files with an extension of :  ' + this.cAccept.join() + ' only!');
                            // Let the user know why the field is red and blank!
                              Ext.MessageBox.show({
                                  title   : 'File Type Error',
                                  msg   : 'Please upload files with an extension of :  ' + this.cAccept.join() + ' only!',
                                  buttons : Ext.Msg.OK,
                                  icon  : Ext.Msg.ERROR
                              });
                              // Set the raw value to null so that the extjs form submit
                              // isValid() method will stop submission.
                              me.reset();
                          }
                          else
                              {
                                  var uploadKey='SK_FILEUPLOAD_'+(new Date().getTime())+'_'+Math.floor((Math.random()*100000)+1);
                                  //target
                                  Ext.Ajax.request({
                                      url:urlPonerLlaveSesion,
                                      params:{uploadKey:uploadKey},
                                      callback:function(){
                                          Ext.select('[id="uploadKeyToBar"]').elements[0].value=uploadKey;
                                          Ext.getCmp('formArchivo').submit({
                                              standardSubmit:true,
                                              target:'iframeTarget'
                                          });
                                          Ext.select('[id="formBarra"]').elements[0].submit();
                                      }
                                  });
                              }
                      }
                  }
            },
            {
                xtype:'panel',
                border:false,
                html:'<iframe style="display:none;" id="iframeTarget" name="iframeTarget" width="100" height="50" frameborder="no"></iframe>'
            },
            {
                xtype:'panel',
                border:false,
                html:'<form id="formBarra" method="post" action="'+URL_BARRA_SUBIR_ARCHIVO+'" target="iframeBar"><input type="hidden" id="uploadKeyToBar" name="uploadKey" /></form></form><iframe width="100" height="20" frameborder="no" id="iframeBar" name="iframeBar"></iframe>'
            }
        ]
    });
    
    var subarrow=new SubirArchivoPanel();
    
});