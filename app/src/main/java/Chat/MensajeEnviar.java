package Chat;

public class MensajeEnviar extends Mensaje{

    //Sin foto adjunta
    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String type_mensaje) {
        super(mensaje, nombre, fotoPerfil, type_mensaje);
    }

    //Con foto adjunta
    public MensajeEnviar(String mensaje,String nombre, String fotoPerfil, String type_mensaje,String urlFoto) {
        super(mensaje, nombre, fotoPerfil, type_mensaje, urlFoto);
    }

}
