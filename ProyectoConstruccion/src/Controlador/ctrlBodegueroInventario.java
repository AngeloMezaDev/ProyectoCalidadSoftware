package Controlador;

import Modelo.ConexionBD;
import Modelo.Inventario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Angelo Meza
 */
public class ctrlBodegueroInventario {

    ConexionBD conexionBD = new ConexionBD();

    //Método para obtener el inventario de la base de datos
    public List<Inventario> obtenerInventario() throws ClassNotFoundException {
        List<Inventario> inventario = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Crear una nueva conexión
            ConexionBD conexion = new ConexionBD();
            conexion.openConnection(); // Abre la conexión directamente

            // Obtener la conexión
            conn = conexion.getConnection();

            // Crear la sentencia SQL para insertar en la columna "Estado" antes de buscar los productos
            String insertSql = "UPDATE Inventario SET Estado = CASE WHEN Cantidad = 0 THEN 'AGOTADO' ELSE 'EN STOCK' END";
            stmt = conn.prepareStatement(insertSql);
            stmt.executeUpdate();

            // Crear la sentencia SQL para obtener los productos
            String selectSql = "SELECT * FROM Inventario ORDER BY Id_Producto ASC";
            stmt = conn.prepareStatement(selectSql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String idProducto = rs.getString("Id_Producto");
                String nombre = rs.getString("Nombre");
                int cantidad = rs.getInt("Cantidad");
                double precio = rs.getDouble("Precio");
                String categoria = rs.getString("Categoria");
                String proveedor = rs.getString("Proveedor");
                String estado = rs.getString("Estado");

                Inventario inv = new Inventario(idProducto, nombre, categoria, cantidad, precio, proveedor, estado);
                inventario.add(inv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ctrlProductos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                conexionBD.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }

        return inventario;
    }

    //Método para actualizar un producto
    public static void actualizarProductoInventario(String Id_Producto, int cantidad) throws SQLException, ClassNotFoundException {
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
            cstmt = conn.prepareCall("{call ModificarCantidadInventario(?,?)}");
            cstmt.setString(1, Id_Producto);
            cstmt.setInt(2, cantidad);
            cstmt.execute();

            conn.commit(); // Confirmar la transacción
            JOptionPane.showMessageDialog(null, "Producto actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer la transacción en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    //Método para cargar datos en la tabla 

    public void cargarDatosTablaInventario(DefaultTableModel modeloTabla) {
        try {
            conexionBD.openConnection();

            // Crear la sentencia SQL para obtener los proveedores ordenados por ID
            String sql = "SELECT Id_Producto, Nombre, Cantidad, Precio, Categoria, Proveedor FROM INVENTARIO ORDER BY Id_Producto ASC";

            // Crear la declaración y ejecutar la consulta
            PreparedStatement statement = conexionBD.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id_Producto");
                String nombre = resultSet.getString("Nombre");
                int cantidad = resultSet.getInt("Cantidad");
                double precio = resultSet.getDouble("Precio");
                String categoria = resultSet.getString("Categoria");
                String proveedor = resultSet.getString("Proveedor");

               
                
                // Agrega el valor del estado al modelo de la tabla
                Object[] fila = {id, nombre, cantidad, precio, categoria, proveedor};
                modeloTabla.addRow(fila);
            }

            System.out.println("Datos del inventario cargados correctamente.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al obtener los datos del inventario: " + e.getMessage());
        } finally {
            try {
                conexionBD.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}
