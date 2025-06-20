package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Config.Config;

/**
 * Clase que maneja la conexión a la base de datos MySQL.
 * 
 * Esta clase proporciona un método estático para establecer una conexión
 * con la base de datos utilizando JDBC.
 * 
 * <p>Requiere que el driver JDBC de MySQL esté incluido en el classpath.</p>
 * 
 */
public class ConexionDB extends Config{

    /**
     * URL de conexión a la base de datos.
     * Incluye nombre de host, puerto, nombre de la base de datos
     * y parámetros opcionales como el uso de SSL y zona horaria.
     */
    private static String URL = null;

    /**
     * Nombre de usuario para acceder a la base de datos.
     */
    private static String USUARIO = null;

    /**
     * Contraseña del usuario de la base de datos.
     */
    private static String CONTRASENA = null;

    public static void init() {
        Config config = new Config();
        URL = config.getDB_URL();
        USUARIO = config.getDB_USUARIO();
        CONTRASENA = config.getDB_CONTRASENA();
    }
    /**
     * Establece y retorna una conexión con la base de datos MySQL.
     * 
     * @return Objeto {@link Connection} si la conexión fue exitosa, o {@code null} si falló.
     */
    public static Connection obtenerConexion() {
        Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión con la base de datos.
     * @param conexion La conexión que se desea cerrar.
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
}