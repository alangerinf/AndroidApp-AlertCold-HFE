package ibao.alanger.alertcoldhfe.model;

public class SensorIntoPallet {

    private String SENSOR_ID;
    private String ZONA;
    private String SUBZONA;
    private String SENSOR;
    private String PERIODO;
    private String FECHA_INICIO;
    private String FECHA_FIN;
    private String TEMP_MIN;
    private String TEMP_MAX;
    private String TEMP_INICIO;
    private String TEMP_FIN;


    private String CANT_CAJAS;
    private String FORMATO;
    private String VARIEDAD;

    SensorIntoPallet(){

         SENSOR_ID="";
         ZONA="";
         SUBZONA="";
         SENSOR="";
         PERIODO="";
         FECHA_INICIO="";
         FECHA_FIN="";
         TEMP_MIN="";
         TEMP_MAX="";
         TEMP_INICIO="";
         TEMP_FIN="";

        CANT_CAJAS ="";
        FORMATO ="";
        VARIEDAD ="";

    }


    public String getSENSOR_ID() {
        return SENSOR_ID;
    }

    public void setSENSOR_ID(String SENSOR_ID) {
        this.SENSOR_ID = SENSOR_ID;
    }

    public String getZONA() {
        return ZONA;
    }

    public void setZONA(String ZONA) {
        this.ZONA = ZONA;
    }

    public String getSUBZONA() {
        return SUBZONA;
    }

    public void setSUBZONA(String SUBZONA) {
        this.SUBZONA = SUBZONA;
    }

    public String getSENSOR() {
        return SENSOR;
    }

    public void setSENSOR(String SENSOR) {
        this.SENSOR = SENSOR;
    }

    public String getPERIODO() {
        return PERIODO;
    }

    public void setPERIODO(String PERIODO) {
        this.PERIODO = PERIODO;
    }

    public String getFECHA_INICIO() {
        return FECHA_INICIO;
    }

    public void setFECHA_INICIO(String FECHA_INICIO) {
        this.FECHA_INICIO = FECHA_INICIO;
    }

    public String getFECHA_FIN() {
        return FECHA_FIN;
    }

    public void setFECHA_FIN(String FECHA_FIN) {
        this.FECHA_FIN = FECHA_FIN;
    }

    public String getTEMP_MIN() {
        return TEMP_MIN;
    }

    public void setTEMP_MIN(String TEMP_MIN) {
        this.TEMP_MIN = TEMP_MIN;
    }

    public String getTEMP_MAX() {
        return TEMP_MAX;
    }

    public void setTEMP_MAX(String TEMP_MAX) {
        this.TEMP_MAX = TEMP_MAX;
    }

    public String getTEMP_INICIO() {
        return TEMP_INICIO;
    }

    public void setTEMP_INICIO(String TEMP_INICIO) {
        this.TEMP_INICIO = TEMP_INICIO;
    }

    public String getTEMP_FIN() {
        return TEMP_FIN;
    }

    public void setTEMP_FIN(String TEMP_FIN) {
        this.TEMP_FIN = TEMP_FIN;
    }


    public String getCANT_CAJAS() {
        return CANT_CAJAS;
    }

    public void setCANT_CAJAS(String CANT_CAJAS) {
        this.CANT_CAJAS = CANT_CAJAS;
    }

    public String getFORMATO() {
        return FORMATO;
    }

    public void setFORMATO(String FORMATO) {
        this.FORMATO = FORMATO;
    }

    public String getVARIEDAD() {
        return VARIEDAD;
    }

    public void setVARIEDAD(String VARIEDAD) {
        this.VARIEDAD = VARIEDAD;
    }
}
