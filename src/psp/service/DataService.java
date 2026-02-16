package psp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
//Ainoha Yubero Timón
public class DataService {
    // Configuración de la base de datos remota
    private static final String IP = "192.168.20.118";
    private static final String DB = "sistema_monitoreo";
    private static final String URL = "jdbc:mysql://" + IP + ":3306/" + DB + "?useSSL=false&serverTimezone=UTC";
    private static final String USER = "Ainoha Yubero Timón";
    private static final String PASS = "12345"; // Cambia por tu contraseña

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static int guardarLectura(String sensorId, double temp) {
        String sql = "INSERT INTO lecturas_temperatura (sensor_id, valor_temp) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, sensorId);
            pstmt.setDouble(2, temp);
            pstmt.executeUpdate();

            // Recuperar el ID autoincremental
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Error al guardar en BD: " + e.getMessage());
        }
        return -1;
    }


    public static void vincularConBlockchain(int idRegistro, String blockHash) {
        String sql = "UPDATE lecturas_temperatura SET blockchain_hash = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, blockHash);
            pstmt.setInt(2, idRegistro);
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al vincular con Blockchain: " + e.getMessage());
        }
    }
}