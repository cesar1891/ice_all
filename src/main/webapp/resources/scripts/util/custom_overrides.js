if(extjs_custom_override_mayusculas==true)
{
	debug('extjs_custom_override_mayusculas ENCENDIDO');
	//Se sobreescribe el componente TextField para que solo acepte may�sculas
    Ext.override(Ext.form.TextField,
    {
    	initComponent:function()
    	{
    		if(this.xtype=='textfield')
    		{
    			this.on("change",
				function()
				{
    				try
    				{
	    				if('string' == typeof this.getValue() && true !== this.sinmayus)
	    				{
	    					//debug('mayus de '+this.getValue());
	    					this.setValue(this.getValue().toUpperCase());
	    				}
    				}
    				catch(e){}
				},this);
    		}
    		return this.callParent();
    	}
	});
}
else
{
	debug('extjs_custom_override_mayusculas APAGADO');
}

Ext.define('Ext.grid.override.RowEditor', {
    override: 'Ext.grid.RowEditor',
    
    initComponent: function() {
        var me = this;
            
        me.callParent(arguments);
        
        me.getForm().on('validitychange', me.onValidityChange, me);
    },    
    
    onValidityChange: function() {
        var me = this,
            form = me.getForm(),
            valid = form.isValid();
        
        if (me.errorSummary && me.isVisible()) {
            me[valid ? 'hideToolTip' : 'showToolTip']();
        }
        me.updateButton(valid);
        me.isValid = valid;
    }
    
});


// Bug fix para tooltip en Firefox, se debe eliminar cuando se cambie la versi�n de EXTJS 4.2.1 a una superior
// Fuente: https://www.sencha.com/forum/showthread.php?260106-Tooltips-on-forms-and-grid-are-not-resizing-to-the-size-of-the-text&p=976013&viewfull=1
Ext.define('Ext.SubPixelRoundingFix', {
    override: 'Ext.dom.Element',
    
    getWidth: function(contentWidth, preciseWidth) {
        var me = this,
            dom = me.dom,
            hidden = me.isStyle('display', 'none'),
            rect, width, floating;

        if (hidden) {
            return 0;
        }

        // Gecko will in some cases report an offsetWidth that is actually less than the width of the
        // text contents, because it measures fonts with sub-pixel precision but rounds the calculated
        // value down. Using getBoundingClientRect instead of offsetWidth allows us to get the precise
        // subpixel measurements so we can force them to always be rounded up. See
        // https://bugzilla.mozilla.org/show_bug.cgi?id=458617
        // Rounding up ensures that the width includes the full width of the text contents.
        if (Ext.supports.BoundingClientRect) {
            rect = dom.getBoundingClientRect();
            // IE9 is the only browser that supports getBoundingClientRect() and
            // uses a filter to rotate the element vertically.  When a filter
            // is used to rotate the element, the getHeight/getWidth functions
            // are not inverted (see setVertical).
            width = (me.vertical && !Ext.isIE9 && !Ext.supports.RotatedBoundingClientRect) ?
                    (rect.bottom - rect.top) : (rect.right - rect.left);
            width = preciseWidth ? width : Math.ceil(width);
        } else {
            width = dom.offsetWidth;
        }

        // IE9/10 Direct2D dimension rounding bug: https://sencha.jira.com/browse/EXTJSIV-603
        // there is no need make adjustments for this bug when the element is vertically
        // rotated because the width of a vertical element is its rotated height
        if (Ext.supports.Direct2DBug && !me.vertical) {
            // get the fractional portion of the sub-pixel precision width of the element's text contents
            floating = me.adjustDirect2DDimension('width');
            if (preciseWidth) {
                width += floating;
            }
            // IE9 also measures fonts with sub-pixel precision, but unlike Gecko, instead of rounding the offsetWidth down,
            // it rounds to the nearest integer. This means that in order to ensure that the width includes the full
            // width of the text contents we need to increment the width by 1 only if the fractional portion is less than 0.5
            else if (floating > 0 && floating < 0.5) {
                width++;
            }
        }

        if (contentWidth) {
            width -= me.getBorderWidth("lr") + me.getPadding("lr");
        }
        
        return (width < 0) ? 0 : width;
    }
});

Ext.define('TextfieldCodificado', {
    extend : 'Ext.form.field.Text',
    constructor : function (config) {
        var me = this;
        Ext.apply(me, {
            inputType  : 'password',
            sinmayus   : true,
            token      : 0,
            allowBlank : false,
            minLength  : 6,
            listeners  : {
                afterrender : function (me) {
                    me.token = ('' + (new Date().getTime())).slice(-5);
                    me.allowBlank = false;
                    me.minLength = 6;
                },
                focus : function (me) {
                    me.setValue('');
                    me.allowBlank = false;
                    me.minLength = 6;
                    var bots = Ext.ComponentQuery.query('button[disabled=false]');
                    for (var i = 0; i < bots.length; i++) {
                        var bot = bots[i];
                        bot.TextfieldCodificadoDisabled = true;
                        bot.disable();
                    }
                },
                blur : function (me) {
                    var val = me.getValue(); 
                    var valor = me.token;
                    for (var i = 0; i < val.length ; i++) {
                        valor = '' + valor + ('x00000000000000000000' + (Number(me.token) * Number(val.charCodeAt(i)))).slice(-20);
                    }
                    me.setValue(valor);
                    me.allowBlank = false;
                    me.minLength = 6;
                    me.isValid();
                    var bots = Ext.ComponentQuery.query('button[TextfieldCodificadoDisabled=true]');
                    for (var i = 0; i < bots.length; i++) {
                        var bot = bots[i];
                        bot.TextfieldCodificadoDisabled = false;
                        bot.enable();
                    }
                }
            }
        });
        this.callParent(arguments);
    }
});