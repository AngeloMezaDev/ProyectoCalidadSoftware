package Vistas;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import Modelo.Excepciones;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Angelo Meza
 */
public class LoginTest {

    private Login login;
    private JFrame frmAdmin;

    public JFrame getFrmAdmin() {
        return frmAdmin;
    }

    @Before
    public void setUp() {
        login = new Login();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Login.
     */
    /**
     * Test of validarAcceso method, of class Login.
     */
    @Test
    public void testexisteUsuario() {
        // Establecer las credenciales del usuario
        login.setTxtUsuario(new JTextField("Mendoza"));

        // Verificar que el usuario existe
        boolean existe;
        // Obtener el nombre de usuario del campo de texto
        String nombreUsuario = login.getTxtUsuario().getText();

        // Verificar que el usuario existe
        boolean existeUsuario = login.existeUsuario(nombreUsuario);
        assertTrue(existeUsuario);

    }

    @Test
    public void validarAcceso() throws SQLException, ClassNotFoundException, Excepciones.CuentaBloqueadaException, Excepciones.UsuarioNoExistenteException, Excepciones.CredencialesInvalidasException {
        //Establecer las credenciales del usuario
        login.setTxtUsuario(new JTextField("angelo"));
        login.setTxtContrasena(new JPasswordField("angelo123"));

        //Obtener el usuario
        String nombreUsuario = login.getTxtUsuario().getText();
        // Obtener la contraseña del campo de texto
        char[] contrasena = login.getTxtContrasena().getPassword();

        // Convertir el arreglo de caracteres a una cadena
        String contrasenaEnCadena = new String(contrasena);
        //Validar el acceso
        boolean valido = login.validarAcceso(nombreUsuario, contrasenaEnCadena);
        assertTrue(valido);

    }

    /**
     * Test of validarAcceso method, of class Login.
     */
}
