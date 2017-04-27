
Ext.namespace('Ext.ux', 'Ext.ux.plugins');

Ext.ux.plugins.HtmlEditorImageInsert = function(config) {
    
    config = config || {};
    
    Ext.apply(this, config);
    
    this.init = function(htmlEditor) {
        this.editor = htmlEditor;
        this.editor.on('render', onRender, this);
    };
    
  
    this.imageInsertConfig = {
        popTitle: config.popTitle || 'Image URL',
        popMsg: config.popMsg || 'Please select the URL of the image you want to insert:',
        popWidth: config.popWidth || 350,
        popValue: config.popValue || ''
    }
    
    
    this.imageInsert = function(){
       /*
        Ext.MessageBox.show({
       
            title: this.imageInsertConfig.popTitle,
            msg: this.imageInsertConfig.popMsg,
            width: this.imageInsertConfig.popWidth,
            buttons: Ext.MessageBox.OKCANCEL,
            prompt: false,
            value: this.imageInsertConfig.popValue,
            scope: this,
            fn: function(btn, text){ if ( btn == 'ok' ) this.editor.relayCmd('insertimage', text); }
       });
       */
    
            if(accionEdit == 'edit')
            {
            
            //  alert('edit');
            /*
              if( winUploadFile != '' ){
                  winUploadFile.close();
                 // alert('0CFile')
                }  
            */			
              if(  popup_winUpload != '' ){
                  //alert('0CpImg')
                  popup_winUpload.close();
              }  
              
              mostrarVentanaUploadImgEdit();
              
            }else{
            
              /* 
        		if( winUploadFile != '' ){
                  winUploadFile.close();
                 // alert('0CFile')
                }  
        	  */		
        			
                if(  popup_winUpload != '' ){
               //   alert('0CpImg')
                  popup_winUpload.close();
                }  

                mostrarVentanaAgregarUploadImag();
            }
      
         editor = this.editor;
    }
        
    
    function onRender() {
        if (!Ext.isSafari) {
            this.editor.tb.add({
                itemId : 'htmlEditorImage',
                cls : 'x-btn-icon x-edit-insertimage',
                enableToggle: true,
                scope: this,
                handler:function(){
                  this.imageInsert();
                 },
                clickEvent:'mousedown',
                tabIndex:-1
            });
        }
    }
}


