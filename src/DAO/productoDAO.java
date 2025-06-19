package DAO;

import Model.Producto;
import java.sql.*;

public class productoDAO {

    // Método para obtener la conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/inventario", "root", ""); // Ajusta los parámetros de conexión
    }

    // Método para agregar un producto
    public boolean agregarProducto(Producto producto) {
        // SQL para insertar un nuevo producto
        String query = "INSERT INTO producto (nombre, precio, cantidad, status, id_tema_parametro) VALUES (?, ?, ?, ?, ?)";
        
        // Conexión y PreparedStatement
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Asignar los valores del objeto Producto al PreparedStatement
            stmt.setString(1, producto.darNombreProducto());
            stmt.setDouble(2, producto.darPrecio());
            stmt.setInt(3, producto.darCantidad());
            stmt.setString(4, producto.darStatus());
            stmt.setInt(5, producto.darIdTemaParametro());  // Esto asume que 'id_tema_parametro' es un campo de la tabla
            
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
