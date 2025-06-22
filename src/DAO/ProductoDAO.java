package DAO;

import Model.Producto;
import Model.Parametro;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import Model.ConexionDB;

/**
 * Agrega un producto a la base de datos.
 * @param producto El producto a agregar.
 * @return true si se agregó correctamente, false si hubo error.
 */

/**
 * DAO encargado de la persistencia de entidades {@link Model.Producto}.
 * <p>
 * Proporciona operaciones básicas para:
 * <ul>
 *   <li>Insertar productos en la base de datos.</li>
 *   <li>Consultar valores de parámetros dinámicos (marcas, sexos, categorías).</li>
 * </ul>
 *
 * La clase utiliza {@link Model.ConexionDB} para obtener conexiones JDBC y se
 * responsabiliza de cerrar los recursos mediante try-with-resources.
 *
 * @author Mario
 * @since 1.0
 */
public class ProductoDAO {
    
    // Método para agregar un producto
    /**
     * Inserta un nuevo {@link Producto} en la tabla {@code productos}.
     *
     * @param producto instancia a persistir. Debe contener todos los campos requeridos.
     * @return {@code true} si la operación afectó al menos una fila; {@code false} en caso contrario.
     */
    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param producto el producto con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarProducto(Producto producto) {
        String query = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ?, status = ?, "
                + "id_marca = ?, id_categoria = ?, id_sexo = ?, ruta_img = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, producto.darNombreProducto());
            stmt.setDouble(2, producto.darPrecio());
            stmt.setInt(3, producto.darCantidad());
            stmt.setString(4, producto.darStatus());
            stmt.setInt(5, producto.darIdMarca());
            stmt.setInt(6, producto.darIdCategoria());
            stmt.setInt(7, producto.darIdSexo());
            stmt.setString(8, producto.darImagenPath());
            stmt.setInt(9, producto.darIdProducto());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Agrega un nuevo producto a la base de datos.
     *
     * @param producto el producto a agregar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean agregarProducto(Producto producto) {
        String query = "INSERT INTO productos (nombre, precio, cantidad, status, id_marca, id_categoria, id_sexo, ruta_img) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion(); 
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            // Asignar los valores del objeto Producto al PreparedStatement
            stmt.setString(1, producto.darNombreProducto());
            stmt.setDouble(2, producto.darPrecio());
            stmt.setInt(3, producto.darCantidad());
            stmt.setString(4, producto.darStatus());
            stmt.setInt(5, producto.darIdMarca());
            stmt.setInt(6, producto.darIdCategoria());
            stmt.setInt(7, producto.darIdSexo());
            stmt.setString(8, producto.darImagenPath());
            
            // Ejecutar la inserción
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setIdProducto(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Si ocurre un error, retornamos 'false'
        }
    }

    // Método para obtener parámetros por tema (marca, sexo, categoría)
    /**
     * Obtiene una lista de {@link Parametro parámetros} correspondientes a un tema.
     *
     * @param nombreTema nombre del tema (por ejemplo «marca», «sexo» o «categoria»).
     * @return lista de parámetros; si no existen devuelve lista vacía (nunca {@code null}).
     */
    public List<Parametro> obtenerParametrosPorTema(String nombreTema) {
        List<Parametro> parametros = new ArrayList<>();
        String query = "SELECT p.id, p.nombre " +
                "FROM `parametro` p " +
                "JOIN `tema_parametro` tp ON p.id = tp.id_parametro " +
                "JOIN `tema` t ON t.id = tp.id_tema " +
                "WHERE t.nombre = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombreTema);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Parametro parametro = new Parametro(
                        rs.getInt("id"),
                        rs.getString("nombre"));
                parametros.add(parametro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parametros;
    }

    /**
     * Obtiene una lista con todos los productos de la base de datos.
     *
     * @return lista de productos; si no hay, devuelve una lista vacía.
     */
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM productos";

        try (Connection conn = ConexionDB.obtenerConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getString("status"),
                    rs.getInt("id_marca"),
                    rs.getInt("id_categoria"),
                    rs.getInt("id_sexo"),
                    rs.getString("ruta_img")
                );
                productos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Obtiene una lista de productos filtrados por ID de marca.
     *
     * @param idMarca ID de la marca por la que filtrar.
     * @return lista de productos de la marca especificada; lista vacía si no hay coincidencias.
     */
    public List<Producto> obtenerProductosPorMarca(int idMarca) {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM productos WHERE id_marca = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idMarca);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio"),
                    rs.getString("status"),
                    rs.getInt("id_marca"),
                    rs.getInt("id_categoria"),
                    rs.getInt("id_sexo"),
                    rs.getString("ruta_img")
                );
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Obtiene un parámetro específico por su ID.
     *
     * @param id el ID del parámetro a buscar.
     * @return un objeto Parametro si se encuentra, o null si no.
     */
    public Parametro obtenerParametroPorId(int id) {
        String query = "SELECT id, nombre FROM parametro WHERE id = ?";
        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Parametro(
                    rs.getInt("id"),
                    rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra o hay un error
    }
    
    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * @param idProducto el ID del producto a eliminar
     * @return true si el producto fue eliminado exitosamente, false en caso contrario
     */
    public boolean eliminarProducto(int idProducto) {
        String query = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idProducto);
            int filasAfectadas = stmt.executeUpdate();
            
            // Retorna true si se eliminó exactamente un registro
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
