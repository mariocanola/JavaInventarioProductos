package DAO;

import Model.Producto;
import java.sql.*;

/**
 * Agrega un producto a la base de datos.
 * @param producto El producto a agregar.
 * @return true si se agregó correctamente, false si hubo error.
 */
public class productoDAO {

    // Método para obtener la conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/inventario", "root", ""); // Ajusta los parámetros de conexión
    }

    // Método para agregar un producto
    public boolean agregarProducto(Producto producto) {
        String query = "INSERT INTO productos (nombre, precio, cantidad, status, id_marca, id_categoria, id_sexo) VALUES (?, ?, ?, ?, ?, ?, ?)";
       
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Asignar los valores del objeto Producto al PreparedStatement
            stmt.setString(1, producto.darNombreProducto());
            stmt.setDouble(2, producto.darPrecio());
            stmt.setInt(3, producto.darCantidad());
            stmt.setString(4, producto.darStatus());
            stmt.setInt(5, producto.darIdMarca());
            stmt.setInt(6, producto.darIdCategoria());
            stmt.setInt(7, producto.darIdSexo());
            
            // Ejecutar la inserción
            int rowsInserted = stmt.executeUpdate();
            
            // Si se insertaron filas, significa que el producto fue agregado exitosamente
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Si ocurre un error, retornamos 'false'
        }
    }
}
