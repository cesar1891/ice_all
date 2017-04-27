/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

// Add the additional 'advanced' VTypes
Ext.apply(Ext.form.VTypes, {
  
  
  validaCatalogo: function(val, field) {
    if (field.initialPassField) {
      var cat1 = Ext.getCmp(field.initialPassField);
      return (val != cat1.getValue());
    }
    return true;
  }
  
  
});

