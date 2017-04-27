// Fuente: http://jsfiddle.net/Whkbu/2/

/**
 * Grid PagingSelectionPersistence plugin
 * 
 * Maintains row selection state when moving between pages of a paginated grid
 *
 * Public Methods:
 * getPersistedSelection() - retrieve the array of selected records across all pages
 * clearPersistedSelection() - deselect records across all pages
 * 
 *
 * @class   Ext.ux.grid.plugin.PagingSelectionPersistence
 * @extends Ext.AbstractPlugin
 * @author  Bill Dami
 * @date    December 20th, 2011
 */
Ext.define('Ext.ux.grid.plugin.PagingSelectionPersistence', {
    alias: 'plugin.pagingselectpersist',
    extend: 'Ext.AbstractPlugin',
    pluginId: 'pagingSelectionPersistence',
    
    //array of selected records
    selection: [],
    //hash map of record id to selected state
    selected: {},
    
    init: function(grid) {
        console.log('init ()');
        this.grid = grid;
        this.selModel = this.grid.getSelectionModel();
        this.isCheckboxModel = (this.selModel.$className == 'Ext.selection.CheckboxModel');
        this.origOnHeaderClick = this.selModel.onHeaderClick;
        this.bindListeners();
    },
    
    destroy: function() {
        console.log('destroy ()');
        this.selection = [];
        this.selected = {};
        this.disable();
    },
    
    enable: function() {
        console.log('enable ()');
        var me = this;
        
        if(this.disabled && this.grid) {
            this.grid.getView().on('refresh', this.onViewRefresh, this);
            this.selModel.on('select', this.onRowSelect, this);
            this.selModel.on('deselect', this.onRowDeselect, this);
            
            if(this.isCheckboxModel) {
                //For CheckboxModel, we need to detect when the header deselect/select page checkbox
                //is clicked, to make sure the plugin's selection array is updated. This is because Ext.selection.CheckboxModel
                //interally supresses event firings for selectAll/deselectAll when its clicked
                this.selModel.onHeaderClick = function(headerCt, header, e) {
                    var isChecked = header.el.hasCls(Ext.baseCSSPrefix + 'grid-hd-checker-on');
                    me.origOnHeaderClick.apply(this, arguments);
                    
                    if(isChecked) {
                        me.onDeselectPage();
                    } else {
                        me.onSelectPage();
                    }
                };
            }
        }
    
        this.callParent();
    },
    
    disable: function() {
        console.log('disable ()');
        if(this.grid) {
            this.grid.getView().un('refresh', this.onViewRefresh, this);
            this.selModel.un('select', this.onRowSelect, this);
            this.selModel.un('deselect', this.onRowDeselect, this);
            this.selModel.onHeaderClick = this.origOnHeaderClick;
        }

        this.callParent();
    },
    
    bindListeners: function() {
        console.log('bindListeners ()');
        var disabled = this.disabled;
        
        this.disable();
        
        if(!disabled) {
            this.enable();
        }
    },
    
    onViewRefresh : function(view, eOpts) {
        console.log('onViewRefresh()', view);
        var store = this.grid.getStore(),
            sel = [],
            hdSelectState,
            rec,
            i;
        this.ignoreChanges = true;
        
        for(i = store.getCount() - 1; i >= 0; i--) {
            rec = store.getAt(i);
            
            if(this.selected[rec.getId()]) {
                sel.push(rec);
            }
        }

        this.selModel.select(sel, false);
        
        if(this.isCheckboxModel) {
            //For CheckboxModel, make sure the header checkbox is correctly
            //checked/unchecked when the view is refreshed depending on the 
            //selection state of the rows on that page (workaround for possible bug in Ext 4.0.7?)
            hdSelectState = (this.selModel.selected.getCount() === this.grid.getStore().getCount());
            this.selModel.toggleUiHeader(hdSelectState);
        }
        
        this.ignoreChanges = false;
        
        console.log('selection', this.selection);
        console.log('selected', this.selected);
    },
    
    onRowSelect: function(sm, rec, idx, eOpts) {
        console.log('onRowSelect()', sm, rec, idx);
        if(this.ignoreChanges === true) {
            return;
        }
        
        if(!this.selected[rec.getId()]) 
        {
            console.log('if , rec.getId()=' + rec.getId() );
            this.selection.push(rec);
            this.selected[rec.getId()] = true;
        } else {
            console.log('else , rec.getId()=' + rec.getId() );
        }
        console.log('selection', this.selection);
        console.log('selected', this.selected);
    },
    
    onRowDeselect: function(sm, rec, idx, eOpts) {
        console.log('onRowDeselect()', sm, rec, idx);
        var i;
        
        if(this.ignoreChanges === true) {
            return;
        }
        
        if(this.selected[rec.getId()])
        {
            for(i = this.selection.length - 1; i >= 0; i--) {
                if(this.selection[i].getId() == rec.getId()) {
                    this.selection.splice(i, 1);
                    this.selected[rec.getId()] = false;
                    break;
                }
            }
        }
        console.log('selection', this.selection);
        console.log('selected', this.selected);
    },
    
    onSelectPage: function() {
        console.log('onSelectPage()');
        var sel = this.selModel.getSelection(),
            len = this.getPersistedSelection().length,
            i;
        
        for(i = 0; i < sel.length; i++) {
            this.onRowSelect(this.selModel, sel[i]);
        }
        
        if(len !== this.getPersistedSelection().length) {
            this.selModel.fireEvent('selectionchange', this.selModel, [], {});
        }
        console.log('selection', this.selection);
        console.log('selected', this.selected);
    },
    
    onDeselectPage: function() {
        console.log('onDeselectPage()');
        var store = this.grid.getStore(),
            len = this.getPersistedSelection().length,
            i;
        
        for(i = store.getCount() - 1; i >= 0; i--) {
            this.onRowDeselect(this.selModel, store.getAt(i));
        }
        
        if(len !== this.getPersistedSelection().length) {
            this.selModel.fireEvent('selectionchange', this.selModel, [], {});
        }
        console.log('selection', this.selection);
        console.log('selected', this.selected);
    },
    
    getPersistedSelection: function() {
        console.log('getPersistedSelection()', this.selection, this.selected);
        return [].concat(this.selection);
    },
    
    clearPersistedSelection: function() {
        console.log('clearPersistedSelection()', this.selection, this.selected);
        var changed = (this.selection.length > 0);
        
        this.selection = [];
        this.selected = {};
        this.onViewRefresh();
        
        if(changed) {
            this.selModel.fireEvent('selectionchange', this.selModel, [], {});
        }
    }
});


/*
var grid, store;

Ext.onReady(function(){
    Ext.tip.QuickTipManager.init();

    Ext.define('ForumThread', {
        extend: 'Ext.data.Model',
        fields: [
            'title', 'forumtitle', 'forumid', 'username',
            {name: 'replycount', type: 'int'},
            {name: 'lastpost', mapping: 'lastpost', type: 'date', dateFormat: 'timestamp'},
            'lastposter', 'excerpt', 'threadid'
        ],
        idProperty: 'threadid'
    });

    store = Ext.create('Ext.data.Store', {
        pageSize: 50,
        model: 'ForumThread',
        remoteSort: true,
        proxy: {
            type: 'jsonp',
            url: 'http://www.sencha.com/forum/topics-browse-remote.php',
            reader: {
                root: 'topics',
                totalProperty: 'totalCount'
            },
            simpleSortMode: true
        },
        sorters: [{
            property: 'lastpost',
            direction: 'DESC'
        }]
    });

    grid = Ext.create('Ext.grid.Panel', {
        id : 'test_grid',
        width: 600,
        height: 400,
        title: 'ExtJS.com - Browse Forums',
        store: store,
        selModel: Ext.create('Ext.selection.CheckboxModel'),
        plugins: [{ptype : 'pagingselectpersist'}],
        viewConfig: {
            id: 'gv',
            trackOver: true,
            stripeRows: true,
            loadMask: false
        },
        // grid columns
        columns:[{
            id: 'topic',
            text: "Topic",
            dataIndex: 'title',
            flex: 1,
            sortable: false
        },{
            text: "Author",
            dataIndex: 'username',
            width: 100,
            hidden: true,
            sortable: true
        },{
            text: "Replies",
            dataIndex: 'replycount',
            width: 70,
            align: 'right',
            sortable: true
        },{
            id: 'last',
            text: "Last Post",
            dataIndex: 'lastpost',
            width: 150,
            sortable: true
        }],
        bbar: Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true,
            emptyMsg: "No topics to display"
        }),
        renderTo: 'topic-grid'
    });

    // trigger the data store load
    store.loadPage(1);
});
*/