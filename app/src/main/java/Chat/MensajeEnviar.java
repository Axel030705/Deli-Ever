package Chat;

public class MensajeEnviar extends Mensaje{

    //Sin foto adjunta
    public MensajeEnviar(String mensaje,  String type_mensaje) {
        super(mensaje, type_mensaje);
    }

    //Con foto adjunta
    public MensajeEnviar(String mensaje, String type_mensaje,String urlFoto) {
        super(mensaje, type_mensaje, urlFoto);
    }

}
