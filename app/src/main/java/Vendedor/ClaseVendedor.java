package Vendedor;

public class ClaseVendedor {
    private String nombre;
    private String correo;
    private String estado;
    private String uid; // Agregamos un campo UID para identificar el cliente en Firebase

    public ClaseVendedor() {
        // Constructor vacío requerido para Firebase Realtime Database
    }

    public ClaseVendedor(String nombre, String correo, String estado) {
        this.nombre = nombre;
        this.correo = correo;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getEstado() {
        return estado;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
