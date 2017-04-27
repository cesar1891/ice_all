Ext.Date.dayNames = [
    'Domingo',
    'Lunes',
    'Martes',
    'Mi&eacute;rcoles',
    'Jueves',
    'Viernes',
    'S&aacaute;bado'
];

Ext.Date.monthNames = [
    'Enero',
    'Febrero',
    'Marzo',
    'Abril',
    'Mayo',
    'Junio',
    'Julio',
    'Agosto',
    'Septiembre',
    'Octubre',
    'Noviembre',
    'Diciembre'
];

Ext.Date.monthNumbers = {
    'Ene': 0,
    'Feb': 1,
    'Mar': 2,
    'Abr': 3,
    'May': 4,
    'Jun': 5,
    'Jul': 6,
    'Ago': 7,
    'Sep': 8,
    'Oct': 9,
    'Nov': 10,
    'Dic': 11
};

Ext.Date.getShortMonthName = function(month) {
    return Date.monthNames[month].substring(0, 3);
};

Ext.Date.getShortDayName = function(day) {
    return Date.dayNames[day].substring(0, 3);
};

Ext.Date.getMonthNumber = function(name) {
	return Date.monthNumbers[name.substring(0, 1).toUpperCase() + name.substring(1, 3).toLowerCase()];
};

//Date.parseCodes.S.s = '(?:st|nd|rd|th)';

if (Ext.picker.Picker){
    Ext.override(Ext.picker.Picker, {
        doneText: 'Hecho'    
    });
}

if (Ext.picker.Date) {
    Ext.override(Ext.picker.Date, {
        'dayText': 'D&iacute;a',
        'monthText': 'Mes',
        'yearText': 'A&ntilde;o',
        'slotOrder': ['day','month','year']    
    });
}

if(Ext.IndexBar){
    Ext.override(Ext.IndexBar, {
        'letters': ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']    
    });
}

if(Ext.NestedList){
    Ext.override(Ext.NestedList, {
        'backText': 'Atr&aacute;s',
        'loadingText': 'Cargando...',
        'emptyText': 'No hay datos disponibles.'
    });
}

if(Ext.util.Format){
    Ext.util.Format.defaultDateFormat = 'd/m/Y';
}

if(Ext.MessageBox){
    Ext.MessageBox.OK.text = 'OK';
    Ext.MessageBox.CANCEL.text = 'Cancelar';
    Ext.MessageBox.YES.text = 'Si';
    Ext.MessageBox.NO.text = 'No';
}
