package Controlador;

import Modelo.ConexionBD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Angelo Meza
 */
public class ctrlBodegueroAdmin {

    ConexionBD conexionBD = new ConexionBD();

    public void AgregarBodeguero(String nombreUsuario, String contrasena) {
        CallableStatement cs = null;

        try {
            // Obtener la conexión a la base de datos
            conexionBD.openConnection();
            Connection connection = conexionBD.getConnection();

            // Llamar al procedimiento almacenado AgregarBodeguero
            cs = connection.prepareCall("{call AgregarBodeguero(?, ?)}");
            cs.setString(1, nombreUsuario);
            cs.setString(2, contrasena);

            // Ejecutar el procedimiento almacenado
            cs.execute();

            System.out.println("Bodeguero agregado exitosamente.");
        } catch (SQLException | ClassNotFoundException e) {
            // Manejo de excepciones (puedes personalizar según tus necesidades)
            System.err.println("Error al agregar bodeguero: " + e.getMessage());
        } finally {
            // Cerrar recursos
            try {
                if (cs != null) {
                    cs.close();
                }
                conexionBD.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    public int obtenerValorActualSecuencia() throws SQLException {
        try {
            conexionBD.openConnection(); // Abre la conexión a la base de datos

            Connection connection = conexionBD.getConnection(); // Obtiene la conexión actual

            if (connection == null) {
                System.out.println("La conexión es nula");
                throw new SQLException("La conexión es nula");
            }

            // Llama a NEXTVAL para inicializar la secuencia
            String initSql = "SELECT usuarios_seq.NEXTVAL FROM DUAL";
            try ( PreparedStatement initStatement = connection.prepareStatement(initSql)) {
                initStatement.executeQuery();
            }

            // Ahora puedes obtener el valor actual de la secuencia
            String sql = "SELECT usuarios_seq.CURRVAL FROM DUAL";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(sql);  ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    System.out.println("No se pudo obtener el valor actual de la secuencia.");
                    throw new SQLException("No se pudo obtener el valor actual de la secuencia.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al obtener el valor actual de la secuencia: " + e.getMessage());
            throw new SQLException("Error al obtener el valor actual de la secuencia: " + e.getMessage(), e);
        } finally {
            try {
                conexionBD.closeConnection(); // Cierra la conexión después de realizar la operación
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}
