package Controlador;

import Modelo.ConexionBD;
import Modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.sql.CallableStatement;

import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Angelo Meza
 */
public class ctrlProductos {

    ConexionBD conexionBD = new ConexionBD();

   
   public List<Producto> obtenerProductos() throws ClassNotFoundException {
    List<Producto> productos = new ArrayList<>();

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        // Crear una nueva conexión
        ConexionBD conexion = new ConexionBD();
        conexion.openConnection(); // Abre la conexión directamente

        // Obtener la conexión
        conn = conexion.getConnection();

        // Crear la sentencia SQL para obtener los productos
        String sql = "SELECT Id_Producto, Nombre, Cantidad, Precio, Categoria, Proveedor FROM Productos";

        stmt = conn.prepareStatement(sql);
        rs = stmt.executeQuery();

        while (rs.next()) {
            String idProducto = rs.getString("Id_Producto");
            String nombre = rs.getString("Nombre");
            int cantidad = rs.getInt("Cantidad");
            double precio = rs.getDouble("Precio");
            String categoria = rs.getString("Categoria");
            String proveedor = rs.getString("Proveedor");

            // Crear un objeto Producto y añadirlo a la lista
            Producto producto = new Producto(idProducto, nombre, categoria, cantidad, precio, proveedor);
            productos.add(producto);
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
            // Cerrar la conexión correctamente
            conexionBD.closeConnection();
        } catch (SQLException e) {
            // Manejo de excepciones en caso de error al cerrar la conexión
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    return productos;
}

    //Método para insertar un nuevo producto 
    public void insertarNuevoProducto(
            String idProducto, String nombre, int cantidad, double precio, String categoria, String proveedor)
            throws SQLException, ClassNotFoundException {
        Connection conn = null;
        CallableStatement callableStatement = null;

        try {
            // Obtener la conexión de la clase ConexionBD utilizando openConnection
            conexionBD.openConnection();
            conn = conexionBD.getConnection();

            // Validar que la conexión no sea nula
            if (conn == null) {
                throw new SQLException("No se pudo establecer la conexión a la base de datos.");
            }

            // Llamar al stored procedure "InsertarProducto"
            String sql = "{call InsertarProducto(?, ?, ?, ?, ?)}";
            callableStatement = conn.prepareCall(sql);

            // Establecer los parámetros del stored procedure
            callableStatement.setString(1, nombre);
            callableStatement.setInt(2, cantidad);
            callableStatement.setDouble(3, precio);
            callableStatement.setString(4, categoria);
            callableStatement.setString(5, proveedor);

            // Ejecutar el stored procedure
            callableStatement.execute();
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (conn != null) {
                    conn.close(); // Cerrar la conexión
                }
            } catch (SQLException e) {
                // Manejo de excepciones en caso de error al cerrar la conexión
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    //Método para generar el ID
    public static String generarNuevoIDProducto() throws ClassNotFoundException {
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
                return "#PROD001";
            }

            String checkEmptyTableSQL = "SELECT COUNT(*) FROM Productos";
            stmt = conn.prepareStatement(checkEmptyTableSQL);
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                String maxIDSQL = "SELECT TO_NUMBER(SUBSTR(MAX(Id_Producto), 6)) + 1 FROM Productos";
                stmt = conn.prepareStatement(maxIDSQL);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    int ultimoID = rs.getInt(1);
                    String nuevoID = String.format("#PROD%03d", ultimoID);
                    return nuevoID;
                }
            }

            // En caso de que no haya registros anteriores o la tabla esté vacía
            return "#PROD001";
        } catch (SQLException e) {
            // Manejo de excepciones: muestra un mensaje de error y devuelve un valor predeterminado
            JOptionPane.showMessageDialog(null, "Error al generar el nuevo ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "#PROD001"; // Valor predeterminado en caso de error
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

    //Método para cargar datos en la tabla 
    public void cargarDatosTablaProductos(DefaultTableModel modeloTabla) {
        try {
            conexionBD.openConnection();

            // Crear la sentencia SQL para obtener los proveedores ordenados por ID
            String sql = "SELECT Id_Producto, Nombre, Cantidad, Precio, Categoria, Proveedor FROM PRODUCTOS ORDER BY Id_Producto ASC";

            // Crear la declaración y ejecutar la consulta
            PreparedStatement statement = conexionBD.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("Id_Producto");
                String nombre = resultSet.getString("Nombre");
                int cantidad = resultSet.getInt("Cantidad");
                double Precio = resultSet.getDouble("Precio");
                String categoria = resultSet.getString("Categoria");
                String proveedor = resultSet.getString("Proveedor");

                Object[] fila = {
                    id,
                    nombre,
                    cantidad,
                    Precio,
                    categoria,
                    proveedor
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

    //Método eliminar para un producto
    public void eliminarProducto(String idProveedor) {
        try {
            conexionBD.openConnection();

            // Crear la sentencia SQL para llamar al stored procedure de eliminar taller
            String sql = "CALL EliminarProducto(?)";

            // Crear la declaración y establecer el parámetro
            CallableStatement cstmt = conexionBD.getConnection().prepareCall(sql);
            cstmt.setString(1, idProveedor);

            // Ejecutar el stored procedure
            cstmt.execute();

            System.out.println("Producto eliminado correctamente.");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        } finally {
            try {
                conexionBD.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
     //Método para actualizar un producto
    public static void actualizarProducto(String idProducto, String nuevoNombre, int nuevaCantidad, double nuevoPrecio, String nuevaCategoria, String nuevoProveedor) throws SQLException, ClassNotFoundException {
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
            cstmt = conn.prepareCall("{call ModificarProducto(?, ?, ?, ?, ?,?)}");
            cstmt.setString(1, idProducto);
            cstmt.setString(2, nuevoNombre);
            cstmt.setInt(3, nuevaCantidad);
            cstmt.setDouble(4, nuevoPrecio);
            cstmt.setString(5, nuevaCategoria);
            cstmt.setString(6, nuevoProveedor);

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
}
