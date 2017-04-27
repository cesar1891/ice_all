/*
 * 
 * Creado el 24/01/2008 11:36:29 a.m. (dd/mm/aaaa hh:mm:ss)
 *
 * Copyright (C) Biosnet S.C.
 * All rights reserved. Todos los derechos reservados.
 *
 * http://www.biosnetmx.com/
*/
package mx.com.aon.portal.model;

import java.io.Serializable;
import java.util.List;

import mx.com.aon.portal.util.UserSQLDateConverter;
import mx.com.aon.portal.util.UserStringConverter;
import mx.com.aon.portal.util.UserTimestampConverter;
import mx.com.gseguros.portal.general.model.RolVO;
import mx.com.gseguros.utils.Utils;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

/**
 * Contiene la informaci&oacute;n del usuario ingresado
 */
public class UserVO implements Serializable {

    /**
     * UID Por defecto
     */
    private static final long serialVersionUID = -1963470818294994036L;
    
    private Logger logger = Logger.getLogger(UserVO.class);

    private String codigoPersona;
    private String name;
    private String lastName;
    private String perfil;
    private PerfilVO fuentesPerfil;
    private String cdUnieco;

    private String user;
    private String personId;
    private String email;
    private BaseObjectVO region;
    private BaseObjectVO pais;
    private BaseObjectVO idioma;
    private List<RolVO> roles;
    private EmpresaVO empresa;
    private int tamagnoPaginacionGrid;
    private String formatoNumerico;
    private String formatoFecha;
    private RolVO rolActivo;
    private String cdElemento;
    private String formatDate = "dd/MM/yyyy";
    private String clientFormatDate = "";
    private String decimalSeparator = ",";
    private List<MenuPrincipalVO> listaMenu;
    private List<MenuPrincipalVO> listaMenuVertical;
    private Converter converterStringToDate;
    private Converter converterToString;
    private Converter converterStringToTimestamp;
    private String currentUrl;
    private String method;
    
    /**
     * Clave interna de GSeguros del usuario que captura un tr&aacute;mite 
     */
    private String claveUsuarioCaptura;

    public UserVO () {
    	this.setFormatDate(formatDate);
    }
	
    /**
     * @return the rolActivo
     */
    public RolVO getRolActivo() {
        return rolActivo;
    }

    /**
     * @param rolActivo the rolActivo to set
     */
    public void setRolActivo(RolVO rolActivo) {
        this.rolActivo = rolActivo;
    }

    // Getters and Setters
/**
 * @return name
 */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return perfil
     */
    public String getPerfil() {
        return perfil;
    }

    /**
     * @param perfil
     */
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    /**
     * @return fuentesPerfil
     */
    public PerfilVO getFuentesPerfil() {
        return fuentesPerfil;
    }

    /**
     * @param fuentesPerfil
     */
    public void setFuentesPerfil(PerfilVO fuentesPerfil) {
        this.fuentesPerfil = fuentesPerfil;
    }

    /**
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }


    /**
     * @return roles
     */
    public List<RolVO> getRoles() {
        return roles;
    }

    /**
     * @param roles
     */
    public void setRoles(List<RolVO> roles) {
        this.roles = roles;
    }


    /**
     * @return
     */
    public int getTamagnoPaginacionGrid() {
        return tamagnoPaginacionGrid;
    }

    /**
     * @param tamagnoPaginacionGrid
     */
    public void setTamagnoPaginacionGrid(int tamagnoPaginacionGrid) {
        this.tamagnoPaginacionGrid = tamagnoPaginacionGrid;
    }

    /**
     * @return
     */
    public String getFormatoNumerico() {
        return formatoNumerico;
    }

    /**
     * @param formatoNumerico
     */
    public void setFormatoNumerico(String formatoNumerico) {
        this.formatoNumerico = formatoNumerico;
    }

    /**
     * @return
     */
    public String getFormatoFecha() {
        return formatoFecha;
    }

    /**
     * @param formatoFecha
     */
    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }

    /**
     * @return the codigoPersona
     */
    public String getCodigoPersona() {
        return codigoPersona;
    }

    /**
     * @param codigoPersona the codigoPersona to set
     */
    public void setCodigoPersona(String codigoPersona) {
        this.codigoPersona = codigoPersona;
    }


    public String getCdElemento() {
        return cdElemento;
    }

    public void setCdElemento(String cdElemento) {
        this.cdElemento = cdElemento;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
        converterStringToDate = new UserSQLDateConverter(formatDate);
        //converterToString = new UserStringConverter(formatDate);
        converterStringToTimestamp = new UserTimestampConverter(formatDate);
    }


    public Converter getConverterStringToDate() {
        return converterStringToDate;
    }


    public Converter getConverterToString() {
        return converterToString;
    }


    public Converter getConverterStringToTimestamp() {
        return converterStringToTimestamp;
    }

	public EmpresaVO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaVO empresa) {
		this.empresa = empresa;
	}

	public BaseObjectVO getIdioma() {
		return idioma;
	}

	public void setIdioma(BaseObjectVO idioma) {
		this.idioma = idioma;
	}

	public BaseObjectVO getPais() {
		return pais;
	}

	public void setPais(BaseObjectVO pais) {
		this.pais = pais;
	}

	public BaseObjectVO getRegion() {
		return region;
	}

	public void setRegion(BaseObjectVO region) {
		this.region = region;
	}

	public List<MenuPrincipalVO> getListaMenu() {
		return listaMenu;
	}

	public void setListaMenu(List<MenuPrincipalVO> listaMenu) {
		this.listaMenu = listaMenu;
	}

	public List<MenuPrincipalVO> getListaMenuVertical() {
		return listaMenuVertical;
	}

	public void setListaMenuVertical(List<MenuPrincipalVO> listaMenuVertical) {
		this.listaMenuVertical = listaMenuVertical;
	}


    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public String getClientFormatDate() {
		return clientFormatDate;
	}

	public void setClientFormatDate(String clientFormatDate) {
		logger.debug("En UserVO: " + clientFormatDate);
		if (clientFormatDate == null || clientFormatDate.equals("")) {
			this.clientFormatDate = "d/m/Y"; //Formato por defecto
		}else {
			this.clientFormatDate = clientFormatDate;
		}
		logger.debug("En UserVO queda: " + this.clientFormatDate);
		String _clientDateFormat = this.clientFormatDate.replace("d", "dd");
        _clientDateFormat = _clientDateFormat.replace("m", "MM");
        _clientDateFormat = _clientDateFormat.replace("Y", "yyyy");
        
        logger.debug("En UserVO el converter: " + _clientDateFormat);
        //Seteo del conversor por defecto
        converterToString = new UserStringConverter(_clientDateFormat);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClaveUsuarioCaptura() {
		return claveUsuarioCaptura;
	}

	public void setClaveUsuarioCaptura(String claveUsuarioCaptura) {
		this.claveUsuarioCaptura = claveUsuarioCaptura;
	}

	public String getCdUnieco() {
		return cdUnieco;
	}

	public void setCdUnieco(String cdUnieco) {
		this.cdUnieco = cdUnieco;
	}
	
	@Override
	public String toString()
	{
		return Utils.join(
				 "\n*********************"
				,"\n****** USUARIO ******"
				,"\n*** cdusuari   = " , this.user
				,"\n*** cdsisrol   = " , (this.rolActivo == null ? null : this.rolActivo.getClave())
				,"\n*** name       = " , this.name
				,"\n*** cdunieco   = " , this.cdUnieco
				,"\n*** elementoId = " , (this.empresa   == null ? null : this.empresa.getElementoId()) 
				,"\n*********************"
				);
	}

}