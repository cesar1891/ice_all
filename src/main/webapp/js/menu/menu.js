/**
 * Creación de los menús horizontal y vertical 
 */

Ext.onReady(function() {
	
	// fix hide submenu (in chrome 43)
    Ext.override(Ext.menu.Menu, {
        onMouseLeave: function(e) {
        var me = this;
    
    
        // BEGIN FIX
        var visibleSubmenu = false;
        me.items.each(function(item) { 
            if(item.menu && item.menu.isVisible()) { 
                visibleSubmenu = true;
            }
        })
        if(visibleSubmenu) {
            //console.log('apply fix hide submenu');
            return;
        }
        // END FIX
    
    
        me.deactivateActiveItem();
    
    
        if (me.disabled) {
            return;
        }
    
    
        me.fireEvent('mouseleave', me, e);
        }
    });
    
    Ext.QuickTips.init();
    
    //Cargar Contenido para el Menu Horizontal:
    Ext.Ajax.request({
        url    : 'obtieneMenuPrincipal.action',
        success : function(resp) {
            var tb = Ext.create('Ext.toolbar.Toolbar', {
                items: Ext.JSON.decode(resp.responseText),
                renderTo: 'nav-toolbar',
                listeners:{
                    render: function(){
                        
                        //Se asignan dinamicamente el ancho del div 'header' y del menu(tb)
                        var obj = document.getElementById('mainbody');
                        this.width = obj.offsetWidth;
                        
                        var header = document.getElementById('header'); 
                        header.style.width = obj.offsetWidth;
                    }
                }
            });
            tb.resumeLayouts(true);
            
        },
        failure : function() {
            Ext.MessageBox.alert('Error', 'Error cargando el Men&uacute; Principal');
        }
    });
    
    
    //Cargar Contenido para el Menu Vertical:
    Ext.Ajax.request({
        url    : 'obtieneMenuVertical.action',
        success : function(resp) {
            
            //Se crea el nodo del menu vertical:
            var tagUL = document.createElement("ul");
            //Se crean elementos para el nodo (li con links):
            var items = Ext.JSON.decode(resp.responseText);
            //console.log('items=', items);
            Ext.each(items, function(item) {
                if(!Ext.isEmpty(item.text)){
                    var nuevoTagLI = document.createElement("li");
                    var nuevoLink = document.createElement('a');
                    nuevoLink.setAttribute('href', _CONTEXT + item.href);
                    nuevoLink.appendChild(document.createTextNode(item.text));
                    nuevoTagLI.appendChild(nuevoLink);
                    tagUL.appendChild(nuevoTagLI);
                    //alert('item=' + item.href + ',' + item.text);
                }
            });
            //Se agrega el nodo al menu vertical:
            var divMenuVertical = document.getElementById('nav-toolbar-vertical');
            divMenuVertical.appendChild(tagUL);
            
            /*
            var pnlMenuVert = Ext.create('Ext.Panel', {
                renderTo: 'nav-toolbar-vertical',
                title: 'Standard w/Short Text',
                lbar: [
                    { xtype: 'button', text: 'Button 1' }
                ]
            });
            */
            
        },
        failure : function() {
            Ext.MessageBox.alert('Error', 'Error cargando el Men&uacute; Vertical');
        }
    });
    
});