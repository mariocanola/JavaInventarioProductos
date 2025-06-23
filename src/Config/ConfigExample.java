package Config;
public class ConfigExample {
// Constantes para la conexión a la base de datos MySQL
    public static final String DB_URL = "jdbc:mysql://localhost:3306/inventario?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    public static final String DB_USUARIO = "";
    public static final String DB_CONTRASENA = "";

    public String getDB_URL() {
        return DB_URL;
    }

    public String getDB_USUARIO() {
        return DB_USUARIO;
    }

    public String getDB_CONTRASENA() {
        return DB_CONTRASENA;
    }
}