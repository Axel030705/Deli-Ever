package Cliente.Pedidos;

import java.io.Serializable;

public class PedidoClase implements Serializable {
    private String idPedido;
    private String Fecha_Hora;
    private String Nombre_Cliente;
    private String Direccion;
    private String Producto;
    private String Monto;
    private String Estado;
    private String Descuento;
    private String idTienda;
    private String ImgProducto;
    private String Cantidad;
    private String idCliente;
    private String idProducto;
    private String Calificado;

    public PedidoClase() {

    }

    public PedidoClase(String idPedido, String fecha_Hora, String nombre_Cliente, String direccion, String producto, String monto, String estado, String descuento, String idTienda, String imgProducto, String cantidad, String idCliente, String idProducto, String calificado) {
        this.idPedido = idPedido;
        Fecha_Hora = fecha_Hora;
        Nombre_Cliente = nombre_Cliente;
        Direccion = direccion;
        Producto = producto;
        Monto = monto;
        Estado = estado;
        Descuento = descuento;
        this.idTienda = idTienda;
        ImgProducto = imgProducto;
        Cantidad = cantidad;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        Calificado = calificado;
    }
    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getFecha_Hora() {
        return Fecha_Hora;
    }

    public void setFecha_Hora(String fecha_Hora) {
        Fecha_Hora = fecha_Hora;
    }

    public String getNombre_Cliente() {
        return Nombre_Cliente;
    }

    public void setNombre_Cliente(String nombre_Cliente) {
        Nombre_Cliente = nombre_Cliente;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public String getMonto() {
        return Monto;
    }

    public void setMonto(String monto) {
        Monto = monto;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String promocion) {
        Descuento = promocion;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getImgProducto() {
        return ImgProducto;
    }

    public void setImgProducto(String imgProducto) {
        ImgProducto = imgProducto;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getCalificado() {
        return Calificado;
    }

    public void setCalificado(String calificado) {
        Calificado = calificado;
    }
}


