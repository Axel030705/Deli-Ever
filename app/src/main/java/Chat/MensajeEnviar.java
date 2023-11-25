package Chat;

public class MensajeEnviar extends Mensaje{

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String type_mensaje) {
        super(mensaje, nombre, fotoPerfil, type_mensaje);
    }

    public MensajeEnviar(String mensaje,String nombre, String fotoPerfil, String type_mensaje,String urlFoto) {
        super(mensaje, nombre, fotoPerfil, type_mensaje, urlFoto);
    }

}
