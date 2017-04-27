/**
 * Valida un RFC a partir de sus datos de entrada
 * @param {string} tipoPersona Indica si es Fisica o Moral
 * @param {string} nombre Primer nombre
 * @param {string} nombre2 Segundo nombre
 * @param {string} apaterno 
 * @param {string} amaterno 
 * @param {string} fecha Fecha de nacimiento para persona fisica, fecha inicio para persona moral en formato dd/mm/yyyy
 * @param {string} rfc RFC a validar
 * @return true si es valido, false si es invalido
 */
function validaRFC(tipoPersona, nombre, nombre2, apaterno, amaterno, fecha, rfc) {
    try {
    	var esValido = false;
    	var rfcGenerado = '';
    	
        if(tipoPersona == 'F') {// Persona fisica
        	
        	rfcGenerado = generaRFCPersonaFisica(nombre, nombre2, apaterno, amaterno, fecha);
        	
        } else if(tipoPersona == 'M' || tipoPersona == 'S') { // Persona moral y regimen Simplificado
            
        	rfcGenerado = generaRFCPersonaMoral(nombre, fecha);
        	
        } else {
            throw 'Error en la validacion, tipo de persona invalido: "' + tipoPersona + '"';
        }
        
        debug('Tipo de persona:', tipoPersona);
        debug('RFCs a comparar:', rfc.toUpperCase(), rfcGenerado);
        if(rfc.toUpperCase() == rfcGenerado) {
            esValido = true;
        }
        return esValido;
    } catch(e) {
        throw 'Error al validar RFC' + e.toString();
    }
}


/**
 * Genera un RFC de acuerdo a los datos solicitados
 * @param {} tipoPersona
 * @param {} nombre
 * @param {} nombre2
 * @param {} apaterno
 * @param {} amaterno
 * @param {} fecha
 */
function generaRFC(tipoPersona, nombre, nombre2, apaterno, amaterno, fecha) {
	
	// TODO: Validar fecha y lanzar error en caso de ser invalida
	
    try {
        if(tipoPersona == 'F') {
            //return apaterno.substr(0,2) + amaterno.substr(0,1) + nombre.substr(0, 1) +
                //fecha.substr(8, 2) + fecha.substr(3, 2) + fecha.substr(0, 2);
            return generaRFCPersonaFisica(nombre, nombre2, apaterno, amaterno, fecha);
        } else if(tipoPersona == 'M' || tipoPersona == 'S') { // Persona moral y regimen Simplificado
            //return nombre.substr(0,3) + fecha.substr(8, 2) + fecha.substr(3, 2) + fecha.substr(0, 2);
            return generaRFCPersonaMoral(nombre, fecha);
        } else {
            throw 'Error en la validacion, tipo de persona invalido';
        }
    } catch(e) {
        throw 'Error al generar RFC' + e.toString();
    }
}


function run(){
   
    var tipoPer = document.getElementById('tipoPersona').value;
    var name = document.getElementById('nombre').value;
    var name2 = document.getElementById('nombre2').value;
    var paterno = document.getElementById('apaterno').value;
    var materno = document.getElementById('amaterno').value;
    var date = document.getElementById('fecha').value;
    
    //alert( cuentaPalabras(entrada) );
    //alert('es vocal?' + esVocal('s')  );
    
    //document.getElementById('rfc').value = generaRFCPersonaMoral(name, date);
    
    document.getElementById('rfc').value = generaRFC(tipoPer, name, name2, paterno, materno, date);
    
    document.getElementById('numPalabras').value = cuentaPalabras(name);
    
    
    //debug(generaClaveDiferenciadoraHomonima(paterno + ' ' + materno + ' ' + name2 + ' ' + name));
}


/**
 * Genera el RFC de una persona física
 * @param {} nombre Nombre
 * @param {} nombre2 Segundo nombre
 * @param {} apaterno Apellido paterno
 * @param {} amaterno Apellido materno
 * @param {} fecha  Fecha de nacimiento en formato DD/MM/YYYY
 */
function generaRFCPersonaFisica(nombre1, nombre2, apaterno, amaterno, fecha) {
    
    // TODO: Analizar si le pasamos el nombre 1 y 2 en una sola variable
	
	
	var nombreComp = nombre1 + ' ' + nombre2; 
	
	// Se convierte a mayuscula y sin acentos:
	nombreComp = nombreComp.toUpperCase();
	nombreComp = reemplazaCaracteresLatinos(nombreComp);
    apaterno = apaterno.toUpperCase();
    apaterno = reemplazaCaracteresLatinos(apaterno);
    amaterno = amaterno.toUpperCase();
    amaterno = reemplazaCaracteresLatinos(amaterno);
    debug('A mayusculas y sin acentos:', nombreComp, apaterno, amaterno);
    
    //
    nombreComp = reemplazarCaracteresEspecialesPersonaFisica(nombreComp);
    apaterno = reemplazarCaracteresEspecialesPersonaFisica(apaterno);
    amaterno = reemplazarCaracteresEspecialesPersonaFisica(amaterno);
    debug('Reemplazar caracteres especiales por su descripcion:', nombreComp, apaterno, amaterno);
    
    // Se eliminan guiones, signos y puntos, etc:
    nombreComp = quitaEspaciosSignosYPuntos(nombreComp);
    apaterno = quitaEspaciosSignosYPuntos(apaterno);
    amaterno = quitaEspaciosSignosYPuntos(amaterno);
    debug('Sin espacios, signos ni puntos:', nombreComp, apaterno, amaterno);
    
    // REGLA 8
    // Cuando en el nombre de las personas físicas figuren artículos, preposiciones, conjunciones o contracciones 
    // no se tomarán como elementos de integración de la clave:
    nombreComp = quitaAbreviaturasPersonaFisica(nombreComp);
    apaterno   = quitaAbreviaturasPersonaFisica(apaterno);
    amaterno   = quitaAbreviaturasPersonaFisica(amaterno);
    debug('Sin abreviaturas persona Fisica:', nombreComp, apaterno, amaterno);
    
    // REGLA 6
    // Cuando el nombre es compuesto, o sea, que esta formado por dos o más palabras, 
    // se tomará para la conformación la letra inicial de la primera, 
    // siempre que no sea MARIA o JOSE dado su frecuente uso, en cuyo caso se tomará la primera letra de la segunda palabra.
    nombreComp  = nombreComp.replace(/\b(MARIA)\b/gi, '').replace(/\b(JOSE)\b/gi, '');
    
    
    ///////////
    var cveHomonima = generaClaveDiferenciadoraHomonima(apaterno + ' ' + amaterno + ' ' + nombre2 + ' ' + nombre1);
    debug('cveHomonima:' + cveHomonima);
    
    var tmp = '';
    
    // REGLA 7
    // En los casos en que la persona física tenga un solo apellido, 
    // se conformará con la primera y segunda letras del apellido paterno o materno, 
    // según figure en el acta de nacimiento, más la primera y segunda letras del nombre.
    // Ejemplos:
    // Juan Martínez       MAJU-420116
    // Gerarda Zafra       ZAGE-251115
    if(!apaterno || !amaterno) {
    	debug('No cuenta con algun apellido');
    	var apellidos = apaterno + amaterno;
    	debug('Apellido compuesto:', apellidos);
    	tmp = apellidos.match(/\b(\w){2}/i)[0];
    	tmp += nombreComp.match(/\b(\w){2}/i)[0];
    	
    } else {
    	debug('Tiene los 2 apellidos');
    	debug('Parametros:', nombreComp, apaterno, amaterno, fecha);
    	debug('apaterno="'+apaterno+'"');
    	// Si el apellido paterno tiene 1 o 2 letras:
    	if(apaterno.length == 1 || apaterno.length == 2) {
    		debug('Apellido paterno con 1 o 2 letras');
    		// En los casos en que el apellido paterno de la persona física se componga de una o dos letras, la clave se formará de la siguiente manera:
            // 1.  La primera letra del apellido paterno.
    		tmp = apaterno.match(/\b(\w)/i)[0];
    		debug('Con apaterno:', tmp);
            // 2.  La primera letra del apellido materno.
    		tmp += amaterno.match(/\b(\w)/i)[0];
    		debug('Con amaterno:', tmp);
            // 3.  La primera y segunda letra del nombre.
    		tmp += nombreComp.match(/\b(\w){2}/i)[0];
    		debug('Con nombre:', tmp);
    	} else {
    		
            // REGLA 1
            // Se integra la clave con los siguientes datos:
            // 1.  La primera letra del apellido paterno y la siguiente primera vocal del mismo.
            tmp = apaterno.match(/\b(\w)/i)[0];
            tmp += apaterno.substr(1).match(/[aeiou]/i)[0];
            debug('Con apaterno:', tmp);
            // 2.  La primera letra del apellido materno.
            tmp += amaterno.match(/\b(\w)/i)[0];
            debug('Con amaterno:', tmp);
            // 3.  La primera letra del nombre.
            tmp += nombreComp.match(/\b(\w)/i)[0];
            debug('Con nombre:', tmp);
        }
    }

    
    // REGLA 9
    // Cuando de las cuatro letras que formen la expresión alfabética, resulte una palabra inconveniente, 
    // la última letra será sustituida por una  “ X “.
    tmp = quitaPalabrasInconvenientesPersonaFisica(tmp);
    
    // REGLA 2
    // A continuación se anotará la fecha de nacimiento del contribuyente, en el siguiente orden:
    // Año: Se tomarán las dos últimas cifras, escribiéndolas con números arábigos.
    // Mes: Se tomará el mes de nacimiento en su número de orden, en un año de calendario, escribiéndolo con números arábigos.
    // Día: Se escribirá con números arábigos.
    tmp += fecha.substr(8, 2) + fecha.substr(3, 2) + fecha.substr(0, 2);
    debug('Con fecha:', tmp);
    
    // Se agrega la homoclave:
    tmp += cveHomonima;
    debug('Con homoclave parte 1:', tmp);
    
    // Se agrega el digito verificador:
    tmp += new String(generaDigitoVerificador(tmp));
    
    return tmp;
}


/**
 * Genera el RFC de una persona moral
 * @param {string} nombre Nombre de la persona moral
 * @param {string} fecha  Fecha de constitución en formato dd/mm/yyyy
 */
function generaRFCPersonaMoral(nombre, fecha) {
    
	var nombreOriginal = nombre;
	
    // Se convierte a mayuscula y sin acentos:
    nombre = nombre.toUpperCase();  
    nombre = reemplazaCaracteresLatinos(nombre);
    debug('A mayusculas y sin acentos:', nombre);
    
    //
    nombre = reemplazarCaracteresEspecialesPersonaMoral(nombre);
    debug('Reemplazar caracteres especiales por su descripcion:', nombre);
    
    // Se eliminan guiones, signos y puntos, etc:
    nombre = quitaEspaciosSignosYPuntos(nombre);
    debug('Sin espacios, signos ni puntos:', nombre);
    
    // Regla 9.-
    // Cuando en la denominación o razón social figuren artículos, preposiciones y conjunciones o contracciones 
    // no se tomaran como elementos de integración de la clave:
    nombre = quitaAbreviaturasPersonaMoral(nombre);
    debug('Sin abreviaturas persona Moral:', nombre);
    
    // REGLA 10
    // Cuando la denominación o razón social contenga en algún o en sus tres primeros elementos 
    // números arábigos, o números romanos, para efectos de conformación de la clave 
    // éstos se tomaran como escritos con letra y seguirán las reglas ya establecidas
    debug('cadena antes de reemplazar romanos:', nombre);
    nombre = nombre.replace(/\b(\w)+\b/gi, function myFunction(x) {
    	debug('value to deromanize', x);
    	var validator = /^M*(?:D?C{0,3}|C[MD])(?:L?X{0,3}|X[CL])(?:V?I{0,3}|I[XV])$/gi;
    	// Si tiene el patron de numero romano se convierte a numero y luego a letra:
    	if(validator.test(x)) {
    		var romanToWord = intToWord( Number(deromanize(x)) ).toUpperCase();
    		debug('validator true:', x, ', se devolvera:', romanToWord);
    		return romanToWord;
    	} else {
    		debug('validator false:', x, ', se devolvera:', x);
    		return x;
    	}
    });
    debug('cadena despues de reemplazar romanos', nombre);
    nombre = nombre.replace(/\b[0-9]+\b/g, function myFunction(x) {
    	debug('number to convert', x);
    	// Convierte el numero a letra:
        return intToWord(Number(x)).toUpperCase();
    });
    debug('cadena despues de reemplazar enteros', nombre);
    
    // Se cuentan las palabras despues de eliminar/reemplazar palabras:
    var numPalabras = cuentaPalabras(nombre);
    debug('numPalabras=', numPalabras);
    
    
    ///////////
    var cveHomonima = generaClaveDiferenciadoraHomonima(nombreOriginal);
    debug('cveHomonima:' + cveHomonima);
    
    
    // Regla 1.-
    
    // Regla 3:
    // Cuando la letra inicial de cualquiera de las tres primeras palabras 
    // de la denominación o razón social sea compuesta, 
    // únicamente se anotará la inicial de esta.
    // NO APLICA CODIGO
    
    //rfc = cuentaPalabras(nombre);
    
    if(numPalabras == 1) {
        nombre = obtienePalabra(nombre, 1).substr(0, 3);
    } else if(numPalabras == 2) {
        // REGLA 6ª.
        // Si la denominación o razón social se comprende de dos elementos, 
        // para efectos de la conformación de la clave, se tomará la letra inicial de la primera palabra 
        // y las dos primeras letras de la segunda
        nombre = obtienePalabra(nombre, 1).substr(0, 1) + obtienePalabra(nombre, 2).substr(0, 2); 
        
    } else if(numPalabras >= 3) {
        
        nombre = obtienePalabra(nombre, 1).substr(0, 1)
                + obtienePalabra(nombre, 2).substr(0, 1)
                + obtienePalabra(nombre, 3).substr(0, 1);
        
    } else {
        //throw 'No existen palabras para generar el RFC: "' + nombre + '"';
    }
    
    // Regla 8:
    // Cuando la denominación o razón social se componga de un solo elemento y sus letras no completen las tres requeridas, 
    // para efectos de conformación de la clave, se tomaran las empleadas por el contribuyente 
    // y las restantes se suplirán con una “X”:
    nombre = padding_right(nombre, 'X', 3);
    
    
    // Regla 2:
    // Se agrega la fecha de constitucion:
    nombre += fecha.substr(8, 2) + fecha.substr(3, 2) + fecha.substr(0, 2);
    
    
    // Se agrega la homoclave:
    nombre += cveHomonima;
    debug('Con homoclave parte 1:', nombre);
    
    // Se agrega el digito verificador:
    nombre += new String(generaDigitoVerificador(nombre));
    
    // TODO: Se trunca la cadena a 12 posiciones, ya que hay casos que genera 13 caracteres, revisar algoritmo con Rafael Guzman
    if(nombre.length > 12) {
    	debug('Se trunca RFC persona moral de ', nombre, ' a ', nombre.substr(0, 12));
        nombre = nombre.substr(0, 12);
    }
    
    return nombre;
}


/**
 * Genera la clave diferenciadora homonima en base al nombre o razon social
 * @param {} nombre Nombre de la persona o Razon social
 */
function generaClaveDiferenciadoraHomonima(nombre) {

	// Se convierte a mayuscula y sin acentos:
    nombre = nombre.toUpperCase();
    nombre = reemplazaCaracteresLatinos(nombre);
    nombre = quitaEspaciosSignosYPuntos(nombre);
    debug('Nombre para generar homoclave:"' + nombre + '"');
	
    // 1. Se asignaran valores a las letras del nombre o razón social de acuerdo a la tabla del Anexo 1:
    //    Espacio = 00  B = 12      O = 26
    //    0 = 00        C = 13      P = 27
    //    1 = 01        D = 14      Q = 28
    //    2 = 02        E = 15      R = 29
    //    3 = 03        F = 16      S = 32
    //    4 = 04        G = 17      T = 33
    //    5 = 05        H = 18      U = 34
    //    6 = 06        I = 19      V = 35
    //    7 = 07        J = 21      W = 36
    //    8 = 08        K = 22      X = 37
    //    9 = 09        L = 23      Y = 38
    //    & = 10        M = 24      Z = 39
    //    A = 11        N = 25      Ñ = 40
    
	var codigoTmp = '';
    for (var i = 0, len = nombre.length; i < len; i++) {
    	
        debug('caracter a reemplazar', nombre[i]);
    	
        // Si el caracter es un digito, se le agrega un cero al inicio
        if(nombre[i].match(/\d/g)) {
        	codigoTmp += '0'+nombre[i]; 
        } else if( nombre[i].match(/[A-Z]/g) || nombre[i].match(/\s/g) || nombre[i].match(/&/g) || nombre[i].match(/\u00D1/gi) ) {
        	// Si es letra, espacio, ampersand o ntilde lo convertimos:
        	codigoTmp += nombre[i].replace(/\s/g,'00').replace(/&/g, '10').replace(/\u00D1/gi, '40')
                 .replace(/A/gi,'11').replace(/B/gi,'12').replace(/C/gi,'13').replace(/D/gi,'14').replace(/E/gi,'15')
                 .replace(/F/gi,'16').replace(/G/gi,'17').replace(/H/gi,'18').replace(/I/gi,'19').replace(/J/gi,'21')
                 .replace(/K/gi,'22').replace(/L/gi,'23').replace(/M/gi,'24').replace(/N/gi,'25').replace(/O/gi,'26')
                 .replace(/P/gi,'27').replace(/Q/gi,'28').replace(/R/gi,'29').replace(/S/gi,'32').replace(/T/gi,'33')
                 .replace(/U/gi,'34').replace(/V/gi,'35').replace(/W/gi,'36').replace(/X/gi,'37').replace(/Y/gi,'38')
                 .replace(/Z/gi,'39');
        }/* else{
        	// Cualquier otro caracter se convierte a 00:
        	codigoTmp += '00';
        }*/
    }
	
    
    // 2. Se ordenan los valores de la siguiente manera:
    // G    O   M   E   Z          D     I    A    Z          E   M   M  A
    // 017  26  24  15  39  00  14  19  11  39  00  15  24  24  11
    // Se agrega un cero al valor de la primera letra para uniformar el criterio de los números a tomar de dos en dos:
    codigoTmp = '0'+codigoTmp;
    
    // 3. Se efectuaran las multiplicaciones de los números tomados de dos en dos para la posición de la pareja:
    var suma = 0;
    debug('codigoTmp=', codigoTmp);
    for (var i = 1, len = codigoTmp.length; i < len; i++) {
//    	debug('codigoTmp[i]=', codigoTmp[i], 'codigoTmp[i-1]=', codigoTmp[i-1]);
//    	
//    	debug('Se multiplica:', codigoTmp[i-1] + '' + codigoTmp[i], '*', codigoTmp[i], 
//    	           '=', Number('' + codigoTmp[i-1] + codigoTmp[i]) * Number(codigoTmp[i]));
    	suma += Number('' + codigoTmp[i-1] + codigoTmp[i]) * Number(codigoTmp[i]);
    }
    
    var strSuma = new String(suma);
    debug('suma:', suma);
    debug('suma.length:', strSuma.length);
    debug('suma.length-3:', Number(strSuma.length)-3);
    
    // 4. Se suma el resultado de las multiplicaciones y del resultado obtenido, 
    // se tomaran las tres ultimas cifras y estas se dividen entre el factor 34
    var cociente = Math.trunc( Number(strSuma.substr(Number(strSuma.length)-3)) / 34 );
    var residuo  = Number(strSuma.substr(Number(strSuma.length)-3)) % 34;
    
    debug('cociente', cociente);
    debug('residuo', residuo);
    
    // 5. Con el cociente y el residuo se consulta la tabla del Anexo II y se asigna la homonimia:
    // TABLA DE VALORES QUE SE ASIGNAN A LA CLAVE DIFERENCIADORA DE HOMONIMIO EN BASE AL COEFICIENTE Y AL RESIDUO
    //  0 = 1   10 = B  20 = L  30 = W
    //  1 = 2   11 = C  21 = M  31 = X
    //  2 = 3   12 = D  22 = N  32 = Y
    //  3 = 4   13 = E  23 = P  33 = Z
    //  4 = 5   14 = F  24 = Q  
    //  5 = 6   15 = G  25 = R  
    //  6 = 7   16 = H  26 = S  
    //  7 = 8   17 = I  27 = T  
    //  8 = 9   18 = J  28 = U  
    //  9 = A   19 = K  29 = V  
    var tablaAnexo2 = ['1','2','3','4','5','6','7','8','9','A',
                       'B','C','D','E','F','G','H','I','J','K',
                       'L','M','N','P','Q','R','S','T','U','V',
                       'W','X','Y','Z'];
                       
    var claveFinal = tablaAnexo2[cociente]+tablaAnexo2[residuo];
    debug('tablaAnexo2[cociente]=', tablaAnexo2[cociente]);
    debug('tablaAnexo2[residuo]=', tablaAnexo2[residuo]);
    debug('claveFinal=', claveFinal);
    
    //return codigoTmp;
    return claveFinal;
}


/**
 * Genera el digito verificador en base al RFC
 * @param {} rfc
 */
function generaDigitoVerificador(rfc) {
	
	// 1.  Se asignaran los valores del Anexo III a las letras y números 
	// del registro federal de contribuyentes formado a 12 posiciones
	// TABLA DE VALORES PARA LO GENERACIÓN DEL CÓDIGO VERIFICADOR DEL REGISTRO FEDERAL DE CONTRIBUYENTES.
    //    0   00  D   13  P   26
    //    1   01  E   14  Q   27
    //    2   02  F   15  R   28
    //    3   03  G   16  S   29
    //    4   04  H   17  T   30
    //    5   05  I   18  U   31
    //    6   06  J   19  V   32
    //    7   07  K   20  W   33
    //    8   08  L   21  X   34
    //    9   09  M   22  Y   35
    //    A   10  N   23  Z   36
    //    B   11  &   24  BLANCO  37
    //    C   12  O   25  Ñ   38
    // TODOS LOS CARACTERES QUE NO SE ENCUENTREN EN ESTA TABLA SE LES ASIGNARA UN VALOR DE “ 00 “.
	var digitoTmp = '';
    for (var i = 0, len = rfc.length; i < len; i++) {
        
//        debug('caracter a reemplazar', rfc[i]);
        
        // Si el caracter es un digito, se le agrega un cero al inicio
        if(rfc[i].match(/\d/g)) {
            digitoTmp += '0'+rfc[i]; 
        } else if( rfc[i].match(/[A-Z]/g) || rfc[i].match(/\s/g) || rfc[i].match(/&/g) || rfc[i].match(/\u00D1/gi) ) {
        	// TODO: abreviar este tipo de else if (en una sola rexeg)
            // Si es letra, espacio, ampersand o ntilde lo convertimos:
            digitoTmp += rfc[i].replace(/\s/g,'37').replace(/&/g, '24').replace(/\u00D1/gi, '38')
                 .replace(/A/gi,'10').replace(/B/gi,'11').replace(/C/gi,'12').replace(/D/gi,'13').replace(/E/gi,'14')
                 .replace(/F/gi,'15').replace(/G/gi,'16').replace(/H/gi,'17').replace(/I/gi,'18').replace(/J/gi,'19')
                 .replace(/K/gi,'20').replace(/L/gi,'21').replace(/M/gi,'22').replace(/N/gi,'23').replace(/O/gi,'25')
                 .replace(/P/gi,'26').replace(/Q/gi,'27').replace(/R/gi,'28').replace(/S/gi,'29').replace(/T/gi,'30')
                 .replace(/U/gi,'31').replace(/V/gi,'32').replace(/W/gi,'33').replace(/X/gi,'34').replace(/Y/gi,'35')
                 .replace(/Z/gi,'36');
        } else{
            // Cualquier otro caracter se convierte a 00:
            digitoTmp += '00';
        }
    }
    
    // 2.  Una vez asignados los valores se aplicara la siguiente forma tomando como base  el factor 13 en orden descendente a cada letra y número del R.F.C. para su multiplicación, de acuerdo a la siguiente formula:
    // (Vi * (Pi + 1)) + (Vi * (Pi + 1)) + ..............+ (Vi * (Pi + 1))      MOD 11
    // Vi    Valor asociado al carácter de acuerdo a la tabla del Anexo III.
    // Pi    Posición que ocupa el i-esimo carácter tomando de derecha a izquierda es decir P toma los valores de 1 a 12.
    
    
    var suma = 0;
    debug('digitoTmp=', digitoTmp);
    for (var i = 1, len = digitoTmp.length, factor=13; i < len; i=i+2, factor--) {
//        debug('digitoTmp[i]=', digitoTmp[i], 'digitoTmp[i-1]=', digitoTmp[i-1]);
//        
//        debug( 'Se multiplica:', digitoTmp[i-1] + '' + digitoTmp[i], '*', factor, 
//                   '=', Number('' + digitoTmp[i-1] + digitoTmp[i]) * factor );
        suma += Number('' + digitoTmp[i-1] + digitoTmp[i]) * factor;
    }
    debug('suma:', suma);
    
    // 3.  El resultado de la suma se divide entre el factor 11.
    // Si el residuo es igual a cero, este será el valor que se le asignara al dígito verificador.
    // Si el residuo es mayor a cero se restara este al factor 11:  11-3 =8
    // Si el residuo es igual a 10 el dígito verificador será  “ A”.
    // Si el residuo es igual a cero el dígito verificador será cero.
    var residuo  = suma % 11;
    var digitoVerif = '';
    if(residuo == 0) {
        digitoVerif = '0';
    } else if(residuo == 10) {
        digitoVerif = 'A';
    } else {
        digitoVerif = new String(11-residuo);
    }
    
    return digitoVerif;
}


/**
 * Cuenta el numero de palabras en un texto
 * @param {} txt Texto de entrada
 * @return Numero de palabras encontradas
 */
function cuentaPalabras(txt) {
	txt = txt.replace(/[\.]{1,}/gi," "); // reemplazamos los puntos por espacios
    txt = txt.replace(/(^\s*)|(\s*$)/gi,"");
    txt = txt.replace(/[ ]{2,}/gi," ");
    txt = txt.replace(/\n /,"\n");
    
    //debug('txt=="', txt, '"');
    
    if(txt.length > 0) {
    	return txt.split(" ").length;
    } else {
    	return 0;
    }
}

/**
 * Reemplaza caracteres latinos, letras con acento y tildes
 * @param {} txt
 * @return {}
 */
function reemplazaCaracteresLatinos(txt) {
	return txt.replace(/\u00E1/g, 'a').replace(/\u00C1/g, 'A')
              .replace(/\u00E9/g, 'e').replace(/\u00C9/g, 'E')
              .replace(/\u00ED/g, 'i').replace(/\u00CD/g, 'I')
              .replace(/\u00F3/g, 'o').replace(/\u00D3/g, 'O')
              .replace(/\u00FA/g, 'u').replace(/\u00DA/g, 'U').replace(/\u00FC/gi,"u").replace(/\u00DC/,"U")
              .replace(/\u00F1/g, 'n').replace(/\u00D1/g, 'N');
}

/**
 * 
 * @param {} txt
 * @return {}
 */
function obtienePalabra(txt, pos) {
    txt = txt.replace(/[\.]{1,}/gi," "); // reemplazamos los puntos por espacios
    txt = txt.replace(/(^\s*)|(\s*$)/gi,"");
    txt = txt.replace(/[ ]{2,}/gi," ");
    txt = txt.replace(/\n /,"\n");
    
    return txt.split(" ")[pos-1];
}

/**
 * 
 * @param {} palabra
 * @return {}
 */
function quitaEspaciosSignosYPuntos(texto) {
    return texto.replace(/[,]{1,}/gi," ") // reemplazamos las comas por espacios
                .replace(/[\.]{1,}/gi," ") // reemplazamos los puntos por espacios
                .replace(/(^\s*)|(\s*$)/gi,"")
                .replace(/[ ]{2,}/gi," ")
                .replace(/\n /,"\n").trim();
    return txt;
}


function quitaPalabrasInconvenientesPersonaFisica(texto) {
    
    return texto.replace(/\b(BUEI)\b/gi, 'BUEX')
                .replace(/\b(BUEY)\b/gi, 'BUEX')
                .replace(/\b(CACA)\b/gi, 'CACX')
                .replace(/\b(CACO)\b/gi, 'CACX')
                .replace(/\b(CAGA)\b/gi, 'CAGX')
                .replace(/\b(CAGO)\b/gi, 'CAGX')
                .replace(/\b(CAKA)\b/gi, 'CAKX')
                .replace(/\b(COGE)\b/gi, 'COGX')
                .replace(/\b(COJA)\b/gi, 'COJX')
                .replace(/\b(COJE)\b/gi, 'COJX')
                .replace(/\b(COJI)\b/gi, 'COJX')
                .replace(/\b(COJO)\b/gi, 'COJX')
                .replace(/\b(CULO)\b/gi, 'CULX')
                .replace(/\b(FETO)\b/gi, 'FETX')
                .replace(/\b(GUEY)\b/gi, 'GUEX')
                .replace(/\b(JOTO)\b/gi, 'JOTX')
                .replace(/\b(KACA)\b/gi, 'KACX')
                .replace(/\b(KACO)\b/gi, 'KACX')
                .replace(/\b(KAGA)\b/gi, 'KAGX')
                .replace(/\b(KAGO)\b/gi, 'KAGX')
                .replace(/\b(KOGE)\b/gi, 'KOGX')
                .replace(/\b(KOJO)\b/gi, 'KOJX')
                .replace(/\b(KAKA)\b/gi, 'KAKX')
                .replace(/\b(KULO)\b/gi, 'KULX')
                .replace(/\b(MAME)\b/gi, 'MAMX')
                .replace(/\b(MAMO)\b/gi, 'MAMX')
                .replace(/\b(MEAR)\b/gi, 'MEAX')
                .replace(/\b(MEON)\b/gi, 'MEOX')
                .replace(/\b(MION)\b/gi, 'MIOX')
                .replace(/\b(MOCO)\b/gi, 'MOCX')
                .replace(/\b(MULA)\b/gi, 'MULX')
                .replace(/\b(PEDA)\b/gi, 'PEDX')
                .replace(/\b(PEDO)\b/gi, 'PEDX')
                .replace(/\b(PENE)\b/gi, 'PENX')
                .replace(/\b(PUTA)\b/gi, 'PUTX')
                .replace(/\b(PUTO)\b/gi, 'PUTX')
                .replace(/\b(QULO)\b/gi, 'QULX')
                .replace(/\b(RATA)\b/gi, 'RATX')
                .replace(/\b(RUIN)\b/gi, 'RUIX');
}

function quitaAbreviaturasPersonaFisica(texto) {
    
    return texto.replace('-', '')
//                .replace(/\b(A C)\b/gi, '')
                .replace(/\b(A EN P)\b/gi, '').replace(/\b(A  EN P)\b/gi, '')
//                .replace(/\b(S DE RL)\b/gi, '').replace(/\b(S DE R L)\b/gi, '').replace(/\b(S  DE R L)\b/gi, '')
                .replace(/\b(COMPAÑÍA)\b/gi, '').replace(/\b(COMPAÑIA)\b/gi, '').replace(/\b(COMPANIA)\b/gi, '').replace(/\b(COMPA&ÍA)\b/gi, '')
                .replace(/\b(CIA)\b/gi, '').replace(/\b(CÍA)\b/gi, '')
                .replace(/\b(SOC)\b/gi, '')
                .replace(/\b(COOP)\b/gi, '')
                .replace(/\b(S EN C POR A)\b/gi, '').replace(/\b(S  EN C POR A)\b/gi, '')
//                .replace(/\b(S EN NC)\b/gi, '').replace(/\b(S EN N C)\b/gi, '')
                .replace(/\b(SA DE CV)\b/gi, '').replace(/\b(S A DE C V)\b/gi, '').replace(/\b(S A  DE C V)\b/gi, '').replace(/\b(S A)\b/gi, '').replace(/\b(SA)\b/gi, '')
//                .replace(/\b(PARA)\b/gi, '')
//                .replace(/\b(POR)\b/gi, '')
//                .replace(/\b(AL)\b/gi, '')
                .replace(/\b(SC)\b/gi, '').replace(/\b(S C)\b/gi, '').replace(/\b(SCS)\b/gi, '').replace(/\b(S C S)\b/gi, '')
//                .replace(/\b(SCL)\b/gi, '').replace(/\b(S C L)\b/gi, '')
//                .replace(/\b(SNC)\b/gi, '').replace(/\b(S N C)\b/gi, '')
//                .replace(/\b(OF)\b/gi, '')
//                .replace(/\b(COMPANY)\b/gi, '')
                .replace(/\b(MC)\b/gi, '').replace(/\b(MAC)\b/gi, '')
                .replace(/\b(VAN)\b/gi, '')
                .replace(/\b(VON)\b/gi, '')
                .replace(/\b(MI)\b/gi, '')
                .replace(/\b(SRL MI)\b/gi, '')
//                .replace(/\b(SA MI)\b/gi, '')
//                .replace(/\b(SRL CV MI)\b/gi, '')
                // Articulos:
//                .replace(/\b(CON)\b/gi, '')
                .replace(/\b(DE)\b/gi, '')
                .replace(/\b(DEL)\b/gi, '')
//                .replace(/\b(EL)\b/gi, '')
//                .replace(/\b(EN)\b/gi, '')
                .replace(/\b(LA)\b/gi, '')
                .replace(/\b(LOS)\b/gi, '')
                .replace(/\b(LAS)\b/gi, '')
                .replace(/\b(SUS)\b/gi, '')
                .replace(/\b(THE)\b/gi, '')
                .replace(/\b(AND)\b/gi, '')
                .replace(/\b(CO)\b/gi, '')
                .replace(/\b(Y)\b/gi, '')
                .replace(/\b(A)\b/gi, '').trim();
                
}

function quitaAbreviaturasPersonaMoral(texto) {
	
	return texto.replace('-', '')
            	//.replace('A C ', '')
	            .replace(/\b(A C)\b/gi, '')
	            //.replace('A  EN P ', '')
	            .replace(/\b(A  EN P)\b/gi, '')
                //.replace('S DE RL', '').replace('S DE R L', '').replace('S  DE R L', '')
                .replace(/\b(S DE RL)\b/gi, '').replace(/\b(S DE R L)\b/gi, '').replace(/\b(S  DE R L)\b/gi, '')
                //.replace('COMPAÑÍA ', '').replace('COMPAÑIA ', '').replace('COMPANIA ', '')
                .replace(/\b(COMPAÑÍA)\b/gi, '').replace(/\b(COMPAÑIA)\b/gi, '').replace(/\b(COMPANIA)\b/gi, '')
                //.replace('CIA ', '').replace('CÍA ', '')
                .replace(/\b(CIA)\b/gi, '').replace(/\b(CÍA)\b/gi, '')
                //.replace('SOCIEDAD ', '')
                .replace(/\b(SOCIEDAD)\b/gi, '')
                //.replace('COOPERATIVA ', '')
                .replace(/\b(COOPERATIVA)\b/gi, '')
                //.replace('S EN C POR A ', '').replace('S  EN C POR A ', '')
                .replace(/\b(S EN C POR A)\b/gi, '').replace(/\b(S  EN C POR A)\b/gi, '')
                //.replace('S EN NC ', '').replace('S EN N C  ', '')
                .replace(/\b(S EN NC)\b/gi, '').replace(/\b(S EN N C)\b/gi, '')
                //.replace('S A  DE C V ', '').replace('S A', '')
                .replace(/\b(SA DE CV)\b/gi, '').replace(/\b(S A DE C V)\b/gi, '').replace(/\b(S A  DE C V)\b/gi, '').replace(/\b(S A)\b/gi, '')
                //.replace('PARA ', '')
                .replace(/\b(PARA)\b/gi, '')
                //.replace('POR ', '')
                .replace(/\b(POR)\b/gi, '')
                //.replace('AL ', '')
                .replace(/\b(AL)\b/gi, '')
                //.replace('S C ', '').replace('S C S ', '')
                .replace(/\b(S C)\b/gi, '').replace(/\b(S C S)\b/gi, '')
                //.replace('SCL ', '').replace('S C L ', '')
                .replace(/\b(SCL)\b/gi, '').replace(/\b(S C L)\b/gi, '')
                //.replace('SNC ', '').replace('S N C ', '')
                .replace(/\b(SNC)\b/gi, '').replace(/\b(S N C)\b/gi, '')
                //.replace('OF ', '')
                .replace(/\b(OF)\b/gi, '')
                //.replace('COMPANY ', '')
                .replace(/\b(COMPANY)\b/gi, '')
                //.replace('MC ', '')
                .replace(/\b(MC)\b/gi, '')
                //.replace('VON ', '')
                .replace(/\b(VON)\b/gi, '')
                //.replace('MI ', '')
                .replace(/\b(MI)\b/gi, '')
                //.replace('SRL CV ', '')
                .replace(/\b(SRL CV)\b/gi, '')
                //.replace('SA MI ', '')
                .replace(/\b(SA MI)\b/gi, '')
                //.replace('SRL CV MI ', '')
                .replace(/\b(SRL CV MI)\b/gi, '')
                // Articulos:
                //.replace(' DE ', '')
                .replace(/\b(DE)\b/gi, '')
                //.replace(' DEL ', '')
                .replace(/\b(DEL)\b/gi, '')
                //.replace(' EL ', '')
                .replace(/\b(EL)\b/gi, '')
                //.replace(' LA ', '')
                .replace(/\b(LA)\b/gi, '')
                //.replace(' LOS ', '')
                .replace(/\b(LOS)\b/gi, '')
                //.replace(' LAS ', '')
                .replace(/\b(LAS)\b/gi, '')
                //.replace(' E ', '') // TODO: Crear una funcion que busque coincidencias por palabra completa
                .replace(/\b(E)\b/gi, '')
                //.replace(' Y ', '')
                .replace(/\b(Y)\b/gi, '').trim();
                //.replace(/(^\s*)|(\s*$)/gi,"")
                
}

function reemplazarCaracteresEspecialesPersonaFisica(txt) {
    
    // REGLA 10
    // Cuando aparezcan formando parte del nombre, apellido paterno y apellido materno los caracteres especiales, 
	// éstos deben de excluirse para el cálculo del homónimo y del dígito verificador. 
	// Los caracteres se interpretarán, sí y sólo si, están en forma individual 
	// dentro del nombre, apellido paterno y apellido materno. 
    txt = txt.replace(/\u0027(?=\s|$)/gi, 'APOSTROFE').replace(/\u2018(?=\s|$)/gi, 'APOSTROFE').replace(/\u2019(?=\s|$)/gi, 'APOSTROFE')
             //.replace(/\.(?=\s|$)/gi, 'PUNTO')// TODO: buscar expresion regular que lo arregle
             ;
    // Ahora se reemplazan los caracteres especiales que no vienen en forma individual:
    txt = txt.replace(/\u0027/gi, '').replace(/\u2018/gi, '').replace(/\u2019/gi, '')
             //.replace(/\.(?=\s|$)/gi, '')// TODO: buscar expresion regular que lo arregle
             ;
    return txt;
}

function reemplazarCaracteresEspecialesPersonaMoral(txt) {
	
	// REGLA 12
    // Cuando aparezcan formando parte de la denominación o razón social los caracteres especiales, 
	// éstos deben de excluirse para el cálculo del homónimo y del dígito verificador. 
	// Los caracteres se interpretarán, sí y sólo si, están en forma individual dentro del texto de la denominación o razón social.
	txt = txt.replace(/@(?=\s|$)/gi, 'ARROBA')
	         .replace(/\u0027(?=\s|$)/gi, 'APOSTROFE').replace(/\u2018(?=\s|$)/gi, 'APOSTROFE').replace(/\u2019(?=\s|$)/gi, 'APOSTROFE')
	         .replace(/\%(?=\s|$)/gi, 'PORCIENTO')
	         .replace(/#(?=\s|$)/gi, 'NUMERO')
	         .replace(/!(?=\s|$)/gi, 'ADMIRACION')
	         //.replace(/\.(?=\s|$)/gi, 'PUNTO')// TODO: buscar expresion regular que lo arregle
	         .replace(/\$(?=\s|$)/gi, 'PESOS')
	         .replace(/\x22(?=\s|$)/gi, 'COMILLAS').replace(/\u201C(?=\s|$)/gi, 'COMILLAS').replace(/\u201D(?=\s|$)/gi, 'COMILLAS')
	         .replace(/-(?=\s|$)/gi, 'GUION')
	         .replace(/\/(?=\s|$)/gi, 'DIAGONAL')
	         .replace(/\+(?=\s|$)/gi, 'SUMA')
	         .replace(/\((?=\s|$)/gi, 'ABRE PARENTESIS')
	         .replace(/\)(?=\s|$)/gi, 'CIERRA PARENTESIS');
	// Ahora se reemplazan los caracteres especiales que no vienen en forma individual:
	txt = txt.replace(/@/gi, '')
	         .replace(/\u0027/gi, '').replace(/\u2018/gi, '').replace(/\u2019/gi, '')
             .replace(/\%/gi, '')
             .replace(/#/gi, '')
             .replace(/!/gi, '')
             //.replace(/\.(?=\s|$)/gi, '')// TODO: buscar expresion regular que lo arregle
             .replace(/\$/gi, '')
             .replace(/\x22/gi, '').replace(/\u201C/gi, '').replace(/\u201D/gi, '')
             .replace(/-/gi, '')
             .replace(/\//gi, '')
             .replace(/\+/gi, '')
             .replace(/\(/gi, '')
             .replace(/\)/gi, '');
    return txt;
}


/**
 * Indica si la letra es vocal
 * @param {} letra
 * @return {Boolean} true si es vocal, false si no lo es
 */
function esVocal(letra) {
    if (letra == 'A' || letra == 'E' || letra == 'I' || letra == 'O'
            || letra == 'U' || letra == 'a' || letra == 'e' || letra == 'i'
            || letra == 'o' || letra == 'u')
        return true;
    else
        return false;
}



/**
 * right padding s with c to a total of n chars
 * Fuente: http://eureka.ykyuen.info/2011/08/23/javascript-leftright-pad-a-string/
 * @param {} s
 * @param {} c
 * @param {} n
 * @return {}
 */
function padding_right(s, c, n) {
  if (! s || ! c || s.length >= n) {
    return s;
  }
  var max = (n - s.length)/c.length;
  for (var i = 0; i < max; i++) {
    s += c;
  }
  return s;
}


/**
 * Convierte un numero entero a letra
 * 
 * @param {int} n Numero entero a convertir
 * @return {string} Representacion del numero en letras 
 */
function intToWord(n) {
    
    var bloque1 = [];
    bloque1.push('');
    bloque1.push('Uno');
    bloque1.push('Dos');
    bloque1.push('Tres');
    bloque1.push('Cuatro');
    bloque1.push('Cinco');
    bloque1.push('Seis');
    bloque1.push('Siete');
    bloque1.push('Ocho');
    bloque1.push('Nueve');
    bloque1.push('Diez');
    bloque1.push('Once');
    bloque1.push('Doce');
    bloque1.push('Trece');
    bloque1.push('Catorce');
    bloque1.push('Quince');
    bloque1.push('Dieciseis');
    bloque1.push('Diecisiete');
    bloque1.push('Dieciocho');
    bloque1.push('Diecinueve');
    bloque1.push('Veinte');
    bloque1.push('Veintiuno');
    bloque1.push('Veintidos');
    bloque1.push('Veintitres');
    bloque1.push('Veinticuatro');
    bloque1.push('Veinticinco');
    bloque1.push('Veintiseis');
    bloque1.push('Veintisiete');
    bloque1.push('Veintiocho');
    bloque1.push('Veintinueve');
    bloque1.push('Treinta');
    bloque1.push('Treinta Y Uno');
    bloque1.push('Treinta Y Dos');
    bloque1.push('Treinta Y Tres');
    bloque1.push('Treinta Y Cuatro');
    bloque1.push('Treinta Y Cinco');
    bloque1.push('Treinta Y Seis');
    bloque1.push('Treinta Y Siete');
    bloque1.push('Treinta Y Ocho');
    bloque1.push('Treinta Y Nueve');
    bloque1.push('Cuarenta');
    bloque1.push('Cuarenta Y Uno');
    bloque1.push('Cuarenta Y Dos');
    bloque1.push('Cuarenta Y Tres');
    bloque1.push('Cuarenta Y Cuatro');
    bloque1.push('Cuarenta Y Cinco');
    bloque1.push('Cuarenta Y Seis');
    bloque1.push('Cuarenta Y Siete');
    bloque1.push('Cuarenta Y Ocho');
    bloque1.push('Cuarenta Y Nueve');
    bloque1.push('Cincuenta');
    bloque1.push('Cincuenta Y Uno');
    bloque1.push('Cincuenta Y Dos');
    bloque1.push('Cincuenta Y Tres');
    bloque1.push('Cincuenta Y Cuatro');
    bloque1.push('Cincuenta Y Cinco');
    bloque1.push('Cincuenta Y Seis');
    bloque1.push('Cincuenta Y Siete');
    bloque1.push('Cincuenta Y Ocho');
    bloque1.push('Cincuenta Y Nueve');
    bloque1.push('Sesenta');
    bloque1.push('Sesenta Y Uno');
    bloque1.push('Sesenta Y Dos');
    bloque1.push('Sesenta Y Tres');
    bloque1.push('Sesenta Y Cuatro');
    bloque1.push('Sesenta Y Cinco');
    bloque1.push('Sesenta Y Seis');
    bloque1.push('Sesenta Y Siete');
    bloque1.push('Sesenta Y Ocho');
    bloque1.push('Sesenta Y Nueve');
    bloque1.push('Setenta');
    bloque1.push('Setenta Y Uno');
    bloque1.push('Setenta Y Dos');
    bloque1.push('Setenta Y Tres');
    bloque1.push('Setenta Y Cuatro');
    bloque1.push('Setenta Y Cinco');
    bloque1.push('Setenta Y Seis');
    bloque1.push('Setenta Y Siete');
    bloque1.push('Setenta Y Ocho');
    bloque1.push('Setenta Y Nueve');
    bloque1.push('Ochenta');
    bloque1.push('Ochenta Y Uno');
    bloque1.push('Ochenta Y Dos');
    bloque1.push('Ochenta Y Tres');
    bloque1.push('Ochenta Y Cuatro');
    bloque1.push('Ochenta Y Cinco');
    bloque1.push('Ochenta Y Seis');
    bloque1.push('Ochenta Y Siete');
    bloque1.push('Ochenta Y Ocho');
    bloque1.push('Ochenta Y Nueve');
    bloque1.push('Noventa');
    bloque1.push('Noventa Y Uno');
    bloque1.push('Noventa Y Dos');
    bloque1.push('Noventa Y Tres');
    bloque1.push('Noventa Y Cuatro');
    bloque1.push('Noventa Y Cinco');
    bloque1.push('Noventa Y Seis');
    bloque1.push('Noventa Y Siete');
    bloque1.push('Noventa Y Ocho');
    bloque1.push('Noventa Y Nueve');

    var bloque2 = [];
    bloque2.push('');
    bloque2.push('Ciento');
    bloque2.push('Doscientos');
    bloque2.push('trescientos');
    bloque2.push('Cuatrocientos');
    bloque2.push('Quinientos');
    bloque2.push('Seiscientos');
    bloque2.push('Setecientos');
    bloque2.push('Ochocientos');
    bloque2.push('Novecientos');
    
    if (isNaN(n)) {
        throw 'No es un n\u00FAmero';
    }
    
    if (Number(n) > 999999999) {
        throw 'Excede el n\u00FAmero m\u00E1ximo a convertir';
    }
    
    var trios = [];
    trios[2] = n % 1000;
    n = Math.floor(n / 1000);

    trios[1] = n % 1000;
    n = Math.floor(n / 1000);

    trios[0] = n;

    var cadena = '';

    for (var i = 0; i < 3; i++) {
        var trio = trios[i];

        if (trio > 0) {
            var n99 = trio % 100;
            var n9 = Math.floor(trio / 100);

            var cadTmp = '';
            if (trio == 1) {
                if (i == 0) {
                    cadTmp = ' Un';
                } else if (i == 2) {
                    cadTmp = ' Uno';
                }
            } else if (trio == 100) {
                cadTmp = 'Cien';
            } else if (n99 == 1) {
                if (i == 0 || i == 1) {
                    cadTmp = bloque2[n9] + ' Un';
                } else if (i == 2) {
                    cadTmp = bloque2[n9] + ' ' + bloque1[n99];
                }
            } else if (n99 > 20 && n99 % 10 == 1) {
                if (i == 0 || i == 1) {
                    if (n99 == 21) {
                        cadTmp = bloque2[n9] + ' Veinitun';
                    } else if (n99 == 31) {
                        cadTmp = bloque2[n9] + ' Treinta y un';
                    } else if (n99 == 41) {
                        cadTmp = bloque2[n9] + ' Cuarenta y un';
                    } else if (n99 == 51) {
                        cadTmp = bloque2[n9] + ' Cincuenta y un';
                    } else if (n99 == 61) {
                        cadTmp = bloque2[n9] + ' Sesenta y un';
                    } else if (n99 == 71) {
                        cadTmp = bloque2[n9] + ' Setenta y un';
                    } else if (n99 == 81) {
                        cadTmp = bloque2[n9] + ' Ochenta y un';
                    } else if (n99 == 91) {
                        cadTmp = bloque2[n9] + ' Noventa y un';
                    }
                } else if (i == 2) {
                    cadTmp = bloque2[n9] + ' ' + bloque1[n99];
                }
            } else {
                cadTmp = bloque2[n9] + ' ' + bloque1[n99];
            }

            if (i == 0) {
                if (trio == 1) {
                    cadTmp = cadTmp + ' Millon ';
                } else {
                    cadTmp = cadTmp + ' Millones ';
                }
            } else if (i == 1) {
                cadTmp = cadTmp + ' Mil ';
            }

            cadena = cadena + cadTmp;
        }
    }

    return cadena;
}


/**
 * Convierte un numero romano a numero entero
 * 
 * @param {string} roman Numero romano a convertir
 * @return {int} Representacion entera del numero romano 
 */
function deromanize(roman) {
    var roman = roman.toUpperCase(), validator = /^M*(?:D?C{0,3}|C[MD])(?:L?X{0,3}|X[CL])(?:V?I{0,3}|I[XV])$/, token = /[MDLV]|C[MD]?|X[CL]?|I[XV]?/g, key = {
        M  : 1000,
        CM : 900,
        D  : 500,
        CD : 400,
        C  : 100,
        XC : 90,
        L  : 50,
        XL : 40,
        X  : 10,
        IX : 9,
        V  : 5,
        IV : 4,
        I  : 1
    }, num = 0, m;
    if (!(roman && validator.test(roman))) {
        //return false;
        throw 'No es un numero romano v\u00E1lido';
    }
    while (m = token.exec(roman)) {
        num += key[m[0]];
    }
    return num;
}


/*
function romanize (num) {
    if (!+num)
        return false;
    var digits = String(+num).split(""),
        key = ["","C","CC","CCC","CD","D","DC","DCC","DCCC","CM",
               "","X","XX","XXX","XL","L","LX","LXX","LXXX","XC",
               "","I","II","III","IV","V","VI","VII","VIII","IX"],
        roman = "",
        i = 3;
    while (i--)
        roman = (key[+digits.pop() + (i * 10)] || "") + roman;
    return Array(+digits.join("") + 1).join("M") + roman;
}
*/