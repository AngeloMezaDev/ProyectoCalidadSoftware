package Modelo;

/**
 *
 * @author Angelo Meza
 */
public class Bodeguero {

    private int id;
    private String nombre;
    private String nombreUsuario;
    private String contraseña;
    private String confContraseña;
    private String direccion;
    private String telefono;
    private String email;

    public Bodeguero(int id, String nombre, String nombreUsuario, String contraseña, String confContraseña, String direccion, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.confContraseña = confContraseña;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfContraseña() {
        return confContraseña;
    }

    public void setConfContraseña(String confContraseña) {
        this.confContraseña = confContraseña;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
