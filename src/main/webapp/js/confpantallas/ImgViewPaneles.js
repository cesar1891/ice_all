Ext.define('Ext.org.ImgViewPaneles', {
    extend: 'Ext.view.View',
    alias : 'widget.imageview',
    requires: ['Ext.data.Store'],
    mixins: {
        dragSelector: 'Ext.ux.js.DragSelector',
        draggable   : 'Ext.ux.js.Draggable'
    },
    
    tpl: [
        '<tpl for=".">',
            '<div class="thumb-wrap">',
                '<div class="thumb">',
                    (!Ext.isIE6? '<img src="../../images/confpantallas/icon/{thumb}" />' : 
                    '<div style="width:76px;height:76px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'../../images/confpantallas/icon/{thumb}\')"></div>'),
                '</div>',
                '<span>{name}</span>',
            '</div>',
        '</tpl>'
    ],
    
    itemSelector: 'div.thumb-wrap',
    multiSelect: true,
    singleSelect: false,
    cls: 'x-image-view',
    autoScroll: true,
    listeners: {
        click: {
            element: 'el',
            fn: function(element){ 
            	var name = element.target.nameProp;
            	if(name != undefined){
             		name = name.substring(0,element.target.nameProp.indexOf("."));
            		clearDummy();
            		var pnl = Ext.getCmp('atributosPanel');
            		pnl.setActiveTab(0);
            		pnl = Ext.getCmp('tabEjemplos');
            		pnl.add(getDummy(name));
            	}
            }
        }
    },
    
    initComponent: function() {
        this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            id: 'storeIcons',
            fields: ['name', 'thumb', {name: 'leaf', defaultValue: true}],
            proxy: {
                type: 'ajax',
                url : '../../js/confpantallas/data/icons.json',
                reader: {
                    type: 'json',
                    root: ''
                }
            }
        });
        
        this.mixins.dragSelector.init(this);
        this.mixins.draggable.init(this, {
            ddConfig: {
                ddGroup: 'organizerDD'
            },
            ghostTpl: [
                '<tpl for=".">',
                    '<img src="../../images/confpantallas/icon/{thumb}" />',
                    '<tpl if="xindex % 4 == 0"><br /></tpl>',
                '</tpl>',
                '<div class="count">',
                    '{[values.length]} images selected',
                '<div>'
            ]
        });
        
        this.callParent();
    }
});