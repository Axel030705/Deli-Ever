package Vendedor;

public class Producto {
    private String nombre;
    private String descripcion;
    private String precio;
    private String extra;
    private String imagenUrl;

    public Producto(String nombre, String descripcion, String precio, String extra) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.extra = extra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
