package Modelo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Angelo Meza
 */


public class Informe_Inventario {
    private Date periodoTiempo;
    private List<Producto> productosIncluidos;
    private double valorTotal;

    // Constructor
    public Informe_Inventario(Date periodoTiempo, List<Producto> productosIncluidos, double valorTotal) {
        this.periodoTiempo = periodoTiempo;
        this.productosIncluidos = productosIncluidos;
        this.valorTotal = valorTotal;
    }

    // Getters y setters
    public Date getPeriodoTiempo() {
        return periodoTiempo;
    }

    public void setPeriodoTiempo(Date periodoTiempo) {
        this.periodoTiempo = periodoTiempo;
    }

    public List<Producto> getProductosIncluidos() {
        return productosIncluidos;
    }

    public void setProductosIncluidos(List<Producto> productosIncluidos) {
        this.productosIncluidos = productosIncluidos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}

