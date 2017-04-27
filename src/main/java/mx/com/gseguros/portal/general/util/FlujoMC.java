package mx.com.gseguros.portal.general.util;

public enum FlujoMC {
    
    SALUD_EMISION_INDIVIDUAL("12"),
    SALUD_EMISION_COLECTIVO("141"),
    SALUD_ENDOSO("246"),
    SALUD_RENOVACION_COLECIVO("264"),
    AUTOS_EMISION_INDIVIDUAL("120"),
    AUTOS_EMISION_PYME("180"),
    AUTOS_EMISION_FLOTILLA("181"),
    AUTOS_ENDOSO("202"),
    AUTOS_RENOVACION_INDIVIDUAL("220"),
    AUTOS_RENOVACION_PYME("240"),
    AUTOS_RENOVACION_FLOTILLA("241"),
    AUTOS_EMISION_EXTERNA("285"),
    AUTOS_ENDOSO_EXTERNO("10"),
    AUTOS_RENOVACION_EXTERNA("30");

    private String cdflujomc;

    private FlujoMC(String cdflujomc) {
        this.cdflujomc = cdflujomc;
    }

    public String getCdflujomc() {
        return cdflujomc;
    }
}