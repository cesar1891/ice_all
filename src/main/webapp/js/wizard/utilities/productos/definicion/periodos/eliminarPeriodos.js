// Despues de ir al acction, si puede eliminar correctamente el periodo lo elimina del grid
package("ice.wizard.productos.definicion.periodos");

ice.wizard.productos.definicion.periodos.DeleteDemouser = function(dataStore, gridPanel) {

    // get the selected items
    var m = gridPanel.getSelections();
    if(m.length > 0)
    {
    	Ext.MessageBox.confirm('Message', 
    	    'Do you really want to delete them?', 
    	    function(btn) {
	    	     if(btn == 'yes')
		         {	
					var jsonData = "[";
			        for(var i = 0, len = m.length; i < len; i++){        		
						var ss = "{\"id\":\"" + m[i].get("id") + "\"}";
						if(i==0)
			           		jsonData = jsonData + ss ;
					   	else
							jsonData = jsonData + "," + ss;	
						dataStore.remove(m[i]);								
			        }	
					jsonData = jsonData + "]";
					dataStore.load({params:{start:0, limit:10, delData:jsonData}});		
			    }
		    } 
		);	
    }
    else
    {
    	Ext.MessageBox.alert('Error', 
    	    'To process delete action, please select at least one item to continue'
    	);
    }       

};