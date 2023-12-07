package Modelo;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Casos de prueba para la clase ConexionBD
 */
public class ConexionBDTest {

    private ConexionBD conexionBD;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        conexionBD = new ConexionBD();
        conexionBD.openConnection();
    }

    @After
    public void tearDown() throws SQLException {
        conexionBD.closeConnection();
    }

    /**
     * Caso de prueba para verificar que getConnection() devuelve una conexión no nula
     */
    @Test
    public void getConnection_shouldReturnNonNullConnection() throws SQLException {
        Connection connection = conexionBD.getConnection();

        assertNotNull("La conexión no debe ser nula", connection);

        conexionBD.closeConnection(); 
    }

    /**
     * Caso de prueba para verificar que openConnection() abre la conexión a la base de datos
     */
    @Test
    public void openConnection_shouldOpenTheConnection() throws SQLException, ClassNotFoundException {
        conexionBD.openConnection();

        assertTrue("La conexión debe estar abierta", conexionBD.isConnected());

    }

    /**
     * Caso de prueba para verificar que closeConnection() cierra la conexión a la base de datos
     */
    @Test
    public void closeConnection_shouldCloseTheConnection() throws SQLException {
        conexionBD.closeConnection();

        assertFalse("La conexión debe estar cerrada", conexionBD.isConnected());

        
    }
}
