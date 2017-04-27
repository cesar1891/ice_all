/**
 * Copyright(c) 2006-2008, FeyaSoft Inc.
 */
package("feyaSoft.demouser");
 
/**
 * This JS is mainly used to handle action in 
 * edit demo user. 
 * Load data from server and assign fields in the form.
 *
 * @author fzhuang
 * @Date Oct 7, 2007
 */
feyaSoft.demouser.EditDemouser = function(dataStore, selectedId) {

    // pre-define fields in the form
	var lastname = new Ext.form.TextField({
        fieldLabel: 'Last Name',
        allowBlank: false,
        name: 'lastname',
        anchor: '90%' 
    });
    
	var firstname = new Ext.form.TextField({
        fieldLabel: 'First Name',
        allowBlank: false,
        name: 'firstname',
        anchor: '90%'  
    });  

	var username = new Ext.form.TextField({
        fieldLabel: 'User Name',
        allowBlank: false,
        name: 'username',
        readOnly: true,
        anchor: '90%'  
    });

	var password = new Ext.form.TextField({
        fieldLabel: 'Password',
        allowBlank: false,
        inputType: 'password',
        name: 'password',
        anchor: '90%'    
    });
    
	var email = new Ext.form.TextField({
	    vtype: 'email',
        fieldLabel: 'Email',           
        name: 'email',
        anchor: '90%'     
    });  
    
    var birthday = new Ext.form.DateField({
        fieldLabel: 'Birthday',           
        name: 'birthday',
        format:'M d, Y',
        anchor: '60%'   
    });  
    
    var note = new Ext.form.TextField({
        xtype: 'textarea',
        hideLabel: true,
        name: 'note',
        anchor: '100% -35'  // anchor width by percentage and height by raw adjustment 
    }); 
	  
    // create form panel
    var formPanel = new Ext.form.FormPanel({
        
        baseCls: 'x-plain',
        labelWidth: 75,
        url:'demouser/listDemouser.htm?action=editDemouser',
        
        reader: new Ext.data.JsonReader({
            root: 'results'
        }, ['id','username','lastname','firstname','password','email','birthday','note']
        ),

        items: [
            lastname, 
            firstname,
            username,
            password,
            email,
            birthday,
            note   
		]
	});

    // load form and assign value to fields
    formPanel.form.load({url:'demouser/listDemouser.htm?action=loadData&id='+selectedId, 
                         waitMsg:'Loading'});    
    
    // define window and show it in desktop
    var window = new Ext.Window({
        title: 'Edit Existing Demo User',
        width: 500,
        height:300,
        minWidth: 300,
        minHeight: 250,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: formPanel,

        buttons: [{
            text: 'Save', 
            handler: function() {
                // check form value 
                if (formPanel.form.isValid()) {
	 		        formPanel.form.submit({		
	 		            params:{id : selectedId},	      
			            waitMsg:'In processing',
			            failure: function(form, action) {
						    Ext.MessageBox.alert('Error Message', action.result.errorInfo);
						},
						success: function(form, action) {
						    Ext.MessageBox.alert('Confirm', action.result.info);
						    window.hide();
						    dataStore.load({params:{start:0, limit:10}});
						}
			        });                   
                } else{
					Ext.MessageBox.alert('Errors', 'Please fix the errors noted.');
				}             
	        }
        },{
            text: 'Cancel',
            handler: function(){window.hide();}
        }]
    });

    window.show();
};
