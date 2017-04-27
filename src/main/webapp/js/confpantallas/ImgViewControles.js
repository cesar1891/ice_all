Ext.define('Ext.org.ImgViewControles', {
    extend: 'Ext.view.View',
    alias : 'widget.imageviewctrol',
    requires: ['Ext.data.Store'],
    mixins: {
        dragSelector: 'Ext.ux.js.DragSelector',
        draggable   : 'Ext.ux.js.DraggableCtrl'
    },
    tpl: [
        '<tpl for=".">',
            '<div class="thumb-wrap">',
                '<div class="thumb">',
                    (!Ext.isIE6? '<img src="../../images/confpantallas/controles/{thumb}" />' : 
                    '<div style="width:56px;height:56px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'../../images/confpantallas/controles/{thumb}\')"></div>'),
                '</div>',
                '<span>{name}</span>',
            '</div>',
        '</tpl>'
    ],
    
    itemSelector: 'div.thumb-wrap',
    multiSelect: true,
    singleSelect: false,
    cls: 'x-image-view-ctrl',
    autoScroll: true,
    
    
    initComponent: function() {
        this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            id: 'storeIconsctrol',
            fields: ['name', 'thumb', {name: 'leaf', defaultValue: true}],
            proxy: {
                type: 'ajax',
                url : '../../js/confpantallas/data/controles.json',
                reader: {
                    type: 'json',
                    root: ''
                }
            }
        });
this.mixins.dragSelector.init(this);
this.mixins.draggable.init(this, {
            ddConfig: {
                ddGroup: 'controlesDD'
            },
            ghostTpl: [
                '<tpl for=".">',
                    '<img src="../../images/confpantallas/controles/{thumb}" />',
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