package ibao.alanger.alertcoldhfe;

public class ConectionConfig {

    private static final String ROOT= "http://35.167.15.182/AlertColdHFEBerries/";
    public static final String POST_LOGIN=ROOT+"autenticacion/login";
    public static final String GET_GETBASICS =ROOT+"zonas";
    public static final String POST_GETSENSOR = ROOT+"sensores";
    public static final String GET_VARIEDADES = ROOT+"variedades";
    public static final String POST_GETFORMATOS= ROOT+"formatos";
    public static final String POST_GETPERIODOS = ROOT+"periodos";
    public static final String POST_BATCH_UPLOAD = ROOT+"periodos/agregar";

    public static final int HTTP_OK = 0;
    public static final int HTTP_ERROR = 1;//ERROR FUNCIONAL SIN DATA

}
