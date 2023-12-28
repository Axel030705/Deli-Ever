package Chat;

public class Mensaje {

    private String mensaje;
    private String type_mensaje;
    public String urlFoto;
    public Mensaje() {
    }

    public Mensaje(String mensaje, String type_mensaje) {
        this.mensaje = mensaje;
        this.type_mensaje = type_mensaje;
    }

    public Mensaje(String mensaje, String type_mensaje, String urlFoto) {
        this.mensaje = mensaje;
        this.type_mensaje = type_mensaje;
        this.urlFoto = urlFoto;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getType_mensaje() {
        return type_mensaje;
    }
    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }
    public String getUrlFoto() {
        return urlFoto;
    }
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
