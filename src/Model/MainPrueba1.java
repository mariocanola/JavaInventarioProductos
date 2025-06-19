package Model;

import java.sql.Connection;

public class MainPrueba1 {
    public static void main(String[] args) {
        Connection conexion = ConexionDb.obtenerConexion();
        if (conexion != null) {
            System.out.println("Conexión exitosa.");
            ConexionDb.cerrarConexion(conexion);
        }
    }
}
