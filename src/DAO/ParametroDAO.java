package DAO;

import Model.Parametro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParametroDAO {

    // Método para obtener la conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/inventario", "root", ""); // Reemplaza los valores de conexión según sea necesario
    }

    public List<Parametro> obtenerParametrosPorTema(String nombreTema) {
        List<Parametro> parametros = new ArrayList<>();
        String query = "SELECT p.id, p.nombre " +
                "FROM `parametro` p " +    // Asegurándonos de que las tablas estén entre comillas
                "JOIN `temaparametro` tp ON p.id = tp.id_parametro " +
                "JOIN `tema` t ON t.id = tp.id_tema " +
                "WHERE t.nombre = ?";

        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombreTema);  // Seteamos el nombre del tema (marca, sexo, categoría)

            ResultSet rs = stmt.executeQuery();  // Ejecutamos la consulta

            while (rs.next()) {
                // Añadimos los resultados al ArrayList
                Parametro parametro = new Parametro(
                    rs.getInt("id"),
                    rs.getString("nombre")
                );
                parametros.add(parametro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parametros;
    }
}
