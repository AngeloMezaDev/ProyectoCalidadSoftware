package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Angelo Meza
 */
public class ConexionBD {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "Meza";
    private static final String PASSWORD = "angelito252";

    private Connection connection;

    /**
     * Obtiene la conexión actual.
     *
     * @return la conexión actual.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Abre la conexión a la base de datos.
     *
     * @throws SQLException si ocurre un error al abrir la conexión.
     * @throws ClassNotFoundException si no se encuentra el controlador de la
     * base de datos.
     */
    public void openConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // Cargar el controlador de la base de datos Oracle
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // Establecer la conexión con la base de datos
            System.out.println("Conexión establecida");
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al abrir la conexión: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cierra la conexión a la base de datos.
     *
     * @throws SQLException si ocurre un error al cerrar la conexión.
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close(); // Cerrar la conexión con la base de datos
                System.out.println("Conexión cerrada exitosamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                throw e;
            }
        }
    }
    
      public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }
}
