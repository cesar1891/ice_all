/* ***************** Manejo de la expiracion de la sesión: ***************** */
Ext.onReady(function () {

    Ext.ns('App');

    App.BTN_OK = 'ok';
    App.BTN_YES = 'yes';
    // 1 min. before notifying the user her session will expire. Change this to a reasonable interval.
    App.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN = _GLOBAL_MUESTRA_EXTENSION_TIMEOUT;
    // 1 min. to kill the session after the user is notified.
    App.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN    = _GLOBAL_OCULTA_EXTENSION_TIMEOUT;
    // The page that kills the server-side session variables.
    //App.SESSION_KILL_URL = 'kill-session.html';
    App.SESSION_KILL_URL = _GLOBAL_URL_LOGOUT;

    // Helper that converts minutes to milliseconds.
    App.toMilliseconds = function (minutes) {
        return minutes * 60 * 1000;
    }

    // Helper that simulates AJAX request.
    App.simulateAjaxRequest = function () {

        Ext.Ajax.request({
            //url: 'foo.html',
        	url: _GLOBAL_URL_MANTENER_SESION_ACTIVA,
            success: Ext.emptyFn,
            failure: Ext.emptyFn
        });
    }

    // Helper that simulates request to kill server-side session variables.
    App.simulateAjaxRequestToKillServerSession = function () {

        Ext.Ajax.request({
            url: App.SESSION_KILL_URL,
            success: Ext.emptyFn,
            failure: Ext.emptyFn
        });
    }

    // Notifies user that her session is about to time out.
    App.sessionAboutToTimeoutPromptTask = new Ext.util.DelayedTask(function () {

        console.log('sessionAboutToTimeoutPromptTask');

        Ext.Msg.confirm(
            'Tu sesi&oacute;n est&aacute; a punto de expirar',
            'Tu sesi&oacute;n expirar&aacute; en ' +
            App.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN +
            ' minuto(s). Quieres continuar con tu sesi&oacute;n?',
            function (btn, text) {

                if (btn == App.BTN_YES) {
                    // Simulate resetting the server-side session timeout timer
                    // by sending an AJAX request.
                    App.simulateAjaxRequest();
                } else {
                    // Send request to kill server-side session.
                    App.simulateAjaxRequestToKillServerSession();
                }
            }
            );

        App.killSessionTask.delay(App.toMilliseconds(
          App.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN));
    });

    // Schedules a request to kill server-side session.
    App.killSessionTask = new Ext.util.DelayedTask(function () {
        console.log('killSessionTask');
        App.simulateAjaxRequestToKillServerSession();
    });

    // Starts the session timeout workflow after an AJAX request completes.
    Ext.Ajax.on('requestcomplete', function (conn, response, options) {

        if (options.url !== App.SESSION_KILL_URL) {
            // Reset the client-side session timeout timers.
            // Note that you must not reset if the request was to kill the server-side session.
            App.sessionAboutToTimeoutPromptTask.delay(App.toMilliseconds(App.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN));
            App.killSessionTask.cancel();
        } else {
            // Notify user her session timed out.
            Ext.Msg.alert('Sesi&oacute;n finalizada', 'Tu sesi&oacute;n ha finalizado. Por favor inicia una nueva sesi&oacute;n.',
                function (btn, text) {
                    if (btn == App.BTN_OK) {
                        // Redirecciona al login:
                    	Ext.create('Ext.form.Panel').submit({
                    	   	url            : _GLOBAL_URL_LOGIN,
                            target         : '_top',
                            standardSubmit : true
                        });
                    }
                }
            );
        }
    });

    // The rest of your app's code would go here. I will just simulate
    // an AJAX request so the session timeout workflow gets started.
    //App.simulateAjaxRequest();
});
/* ***************** Fin de Manejo de la expiracion de la sesión ***************** */
