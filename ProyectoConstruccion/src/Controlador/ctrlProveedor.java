package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Modelo.ConexionBD;
import Modelo.Proveedor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Angelo Meza
 */
public class ctrlProveedor {

    ConexionBD conexionBD = new ConexionBD();

    // Obtener una lista de proveedores desde la base de datos
    public List<Proveedor> obtenerProveedores() throws ClassNotFoundException {
        List<Proveedor> proveedores = new ArrayList<>();
        ConexionBD conexionBD = new ConexionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexionBD.openConnection(); // Abrir la conexión

            conn = conexionBD.getConnection();
            String sql = "SELECT Id_Proveedor, Nombre, Direccion, Telefono, Email FROM Proveedor ORDER BY Id_Proveedor ASC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String idProveedor = rs.getString("Id_Proveedor");
                String nombre = rs.getString("Nombre");
                String direccion = rs.getString("Direccion");
                String telefono = rs.getString("Telefono");
                String email = rs.getString("Email");
                proveedores.add(new Proveedor(idProveedor, nombre, direccion, telefono, email));
            }
        } catch (SQLException e) {
            // Manejo de excepciones
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                    conexionBD.closeConnection(); // Cerrar la conexión
                }
            } catch (SQLException e) {
                // Manejo de excepciones en caso de error al cerrar la conexión
                e.printStackTrace();
            }
        }

        return proveedores;
    }

    // Método para generar el ID en el label lblId
    public static String generarNuevoID() throws ClassNotFoundException {
        ConexionBD conexionBD = new ConexionBD();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Abre la conexión a la base de datos utilizando el método openConnection
            conexionBD.openConnection();
            conn = conexionBD.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return "#PROV001";
            }

            String checkEmptyTableSQL = "SELECT COUNT(*) FROM Proveedor";
            stmt = conn.prepareStatement(checkEmptyTableSQL);
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                String maxIDSQL = "SELECT TO_NUMBER(SUBSTR(MAX(Id_Proveedor), 6)) + 1 FROM Proveedor";
                stmt = conn.prepareStatement(maxIDSQL);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    int ultimoID = rs.getInt(1);
                    String nuevoID = String.format("#PROV%03d", ultimoID);
                    return nuevoID;
                }
            }

            // En caso de que no haya registros anteriores o la tabla esté vacía
            return "#PROV001";
        } catch (SQLException e) {
            // Manejo de excepciones: muestra un mensaje de error y devuelve un valor predeterminado
            JOptionPane.showMessageDialog(null, "Error al generar el nuevo ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "#PROV001"; // Valor predeterminado en caso de error
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    // Cierra la conexión utilizando el método closeConnection
                    conexionBD.closeConnection();
                }
            } catch (SQLException e) {
                // Manejo de excepciones en caso de error al cerrar la conexión
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Método para ingresar un nuevo proveedor en la base de datos
    public static void nuevoProveedor(String nombre, String direccion, String telefono, String email) throws ClassNotFoundException {
        ConexionBD conexionBD = new ConexionBD();
        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            conexionBD.openConnection(); // Abrir la conexión

            conn = conexionBD.getConnection();
            conn.setAutoCommit(false); // Deshabilitar la confirmación automática

            // Llamar al procedimiento almacenado sin llaves
            cstmt = conn.prepareCall("CALL InsertarProveedor(?, ?, ?, ?)");
            cstmt.setString(1, nombre);
            cstmt.setString(2, direccion);
            cstmt.setString(3, telefono);
            cstmt.setString(4, email);
            cstmt.execute();

            conn.commit(); // Confirmar la transacción
            JOptionPane.showMessageDialog(null, "Proveedor agregado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer la transacción en caso de error
                } catch (SQLException rollbackException) {
                    System.err.println("Error al hacer rollback: " + rollbackException.getMessage());
                }
            }
            JOptionPane.showMessageDialog(null, "Error al agregar el proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (cstmt != null) {
                    cstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
                conexionBD.closeConnection(); // Cerrar la conexión
            } catch (SQLException closeException) {
                System.err.println("Error al cerrar la conexión: " + closeException.getMessage());
            }
        }
    }

    //Método para actualizar un proveedor
    public static void actualizarProveedor(String idProveedor, String nuevoNombre, String nuevaDireccion, String nuevoTelefono, String nuevoEmail) throws SQLException, ClassNotFoundException {
        ConexionBD conexionBD = new ConexionBD();
        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            conexionBD.openConnection(); // Abre la conexión

            conn = conexionBD.getConnection();

            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Error: No se pudo establecer la conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn.setAutoCommit(false); // Deshabilitar la confirmación automática

            // Llamar al procedimiento almacenado para actualizar el proveedor
            cstmt = conn.prepareCall("{call ActualizarProveedor(?, ?, ?, ?, ?)}");
            cstmt.setString(1, idProveedor);
            cstmt.setString(2, nuevoNombre);
            cstmt.setString(3, nuevaDireccion);
            cstmt.setString(4, nuevoTelefono);
            cstmt.setString(5, nuevoEmail);
            cstmt.execute();

            conn.commit(); // Confirmar la transacción
            JOptionPane.showMessageDialog(null, "Proveedor actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer la transacción en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de Null Pointer: " + npe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }

            conn.setAutoCommit(true); // Habilitar la confirmación automática
            conexionBD.closeConnection(); // Cierra la conexión
        }
    }

    public void cargarDatosTablaProveedor(DefaultTableModel modeloTabla) {
        try {
            conexionBD.openConnection();

            // Crear la sentencia SQL para obtener los proveedores ordenados por ID
            String sql = "SELECT Id_Proveedor, Nombre, Direccion, Telefono, Email FROM PROVEEDOR ORDER BY Id_Proveedor ASC";

            // Crear la declaración y ejecutar la consulta
            PreparedStatement statement = conexionBD.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id_Proveedor");
                String nombre = resultSet.getString("Nombre");
                String direccion = resultSet.getString("Direccion");
                String telefono = resultSet.getString("Telefono");
                String email = resultSet.getString("Email");

                Object[] fila = {
                    id,
                    nombre,
                    direccion,
                    telefono,
                    email
                };
                modeloTabla.addRow(fila);
            }

            System.out.println("Datos de los proveedores cargados correctamente.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al obtener los datos de los proveedores: " + e.getMessage());
        } finally {
            try {
                conexionBD.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    //Método para eliminar un proveedor
    public void eliminarTaller(String idProveedor) {
        try {
            conexionBD.openConnection();
            
            if (proveedorAsociadoAProducto(idProveedor)) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar el proveedor porque está asociado a uno o más productos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Crear la sentencia SQL para llamar al stored procedure de eliminar taller
            String sql = "CALL EliminarProveedor(?)";

            // Crear la declaración y establecer el parámetro
            CallableStatement cstmt = conexionBD.getConnection().prepareCall(sql);
            cstmt.setString(1, idProveedor);

            // Ejecutar el stored procedure
            cstmt.execute();

            System.out.println("Proveedor eliminado correctamente.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al eliminar el proveedor: " + e.getMessage());
        } finally {
            try {
                conexionBD.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private boolean proveedorAsociadoAProducto(String idProveedor) throws ClassNotFoundException {
        boolean proveedorAsociado = false;

        try {
            conexionBD.openConnection();
            // Consulta SQL para verificar si el proveedor está asociado a algún producto
            String query = "SELECT COUNT(*) FROM Productos WHERE Proveedor = ?";
            try ( PreparedStatement stmt = conexionBD.getConnection().prepareStatement(query)) {
                stmt.setString(1, idProveedor);

                try ( ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Si el recuento es mayor que 0, significa que el proveedor está asociado a productos
                        proveedorAsociado = rs.getInt(1) > 0;
                    }
                }
            }

            // Cierra la conexión a la base de datos
            conexionBD.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de la excepción (puedes lanzarla o manejarla según tu necesidad)
        }

        return proveedorAsociado;
    }
}
