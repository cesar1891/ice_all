Ext.namespace('Ext.ux', 'Ext.ux.plugins');
//alert(8888);
/**
 * EditorGrid validation plugin
 * Adds validation functions to the grid
 *
 * @author  Jozef Sakalos, aka Saki
 * @version 0.1
 *
 * Usage: 
 * grid = new Ext.grid.EditorGrid({plugins:new Ext.ux.plugins.GridValidator(), ...})
 */
Ext.ux.plugins.GridValidator = function(config) {

 
	// initialize plugin
    this.init = function(grid) {
        Ext.apply(grid, {
            /**
             * Checks if a grid cell is valid
             * @param {Integer} col Cell column index
             * @param {Integer} row Cell row index
             * @return {Boolean} true = valid, false = invalid
             */
            isCellValid:function(col, row) {
                if(!this.colModel.isCellEditable(col, row)) {
                    return true;
                }
                var ed = this.colModel.getCellEditor(col, row);
                
                if(!ed) {
                    return true;
                }
                var record = this.store.getAt(row);
                
                if(!record) {
                    return true;
                }
                var field = this.colModel.getDataIndex(col);
                ed.field.setValue(record.data[field]);
               // ed.field.css += ' x-form-invalid';
			    //ed.field.attr = 'ext:qtip="Please enter a value"; ext:qclass="x-form-invalid-tip"';
                
               // record.data[field].css += 'x-form-invalid';
               // record.data[field].attr = 'ext:qtip="Please enter a value"; ext:qclass="x-form-invalid-tip"';
                
                //extGrid_renderer('',ed,record);
                //alert(ed.field.markInvalid());
                //record.data[field].setValue().markInvalid();
                
                return ed.field.isValid(true);
            } // end of function isCellValid

            /**
             * Checks if grid has valid data
             * @param {Boolean} editInvalid true to automatically start editing of the first invalid cell
             * @return {Boolean} true = valid, false = invalid
             */
            ,isValid:function(editInvalid) {
                var cols = this.colModel.getColumnCount();
                var rows = this.store.getCount();
                var r, c;
                var valid = true;
                
                for(r = 0; r < rows; r++) {
                    for(c = 0; c < cols; c++) {
                        valid = this.isCellValid(c, r);
                        if(!valid) {
                        	//this.markInvalid();
                        	break;
                        }
                    }
                    if(!valid) {
                        //this.markInvalid();
                        break;
                    }
                }
                if(editInvalid && !valid) {
                    this.startEditing(r, c);
                }
                return valid;
            } // end of function isValid
	/**
             * Checks if row has valid data
             * @param {Integer} row index
             * @param {Boolean} editInvalid true to automatically start editing of the first invalid cell
             * @return {Boolean} true = valid, false = invalid
             */
            ,isRowValid:function(row, editInvalid) {
                var cols = this.colModel.getColumnCount();
                var c;
                var valid = true;
                for(c = 0; c < cols; c++) {
                    valid = this.isCellValid(c, row);
                    if(!valid) {
                        break;
                    }
                }
                if(editInvalid && !valid) {
                    this.startEditing(row, c);
                }
                return valid;
            } // end of function isRowValid
        });
    }; // end of function init
}; // GridValidator plugin end 
