package Modelo;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Angelo Meza
 */


public class Pedido_Compra {
    private int numeroPedido;
    private Date fechaPedido;
    private Date fechaEntrega;
    private List<Producto> productosSolicitados;
    private List<Integer> cantidadesSolicitadas;

    // Constructor
    public Pedido_Compra(int numeroPedido, Date fechaPedido, Date fechaEntrega, List<Producto> productosSolicitados, List<Integer> cantidadesSolicitadas) {
        this.numeroPedido = numeroPedido;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.productosSolicitados = productosSolicitados;
        this.cantidadesSolicitadas = cantidadesSolicitadas;
    }

    // Getters y setters

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public List<Producto> getProductosSolicitados() {
        return productosSolicitados;
    }

    public void setProductosSolicitados(List<Producto> productosSolicitados) {
        this.productosSolicitados = productosSolicitados;
    }

    public List<Integer> getCantidadesSolicitadas() {
        return cantidadesSolicitadas;
    }

    public void setCantidadesSolicitadas(List<Integer> cantidadesSolicitadas) {
        this.cantidadesSolicitadas = cantidadesSolicitadas;
    }
    
}

