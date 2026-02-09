package psp.server;

import psp.block.Block;
import psp.service.DataService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final List<Block> blockchain = new ArrayList<>();
    private static final double TEMP_LIMITE = 50.0;

    public static void main(String[] args) {

        // Bloque génesis
        blockchain.add(new Block("Genesis Block", "0"));

        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("Servidor de Monitoreo listo en puerto 6000...");

            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
                ) {

                    String data = in.readLine(); // SENSOR_A1;TEMP:38.5
                    System.out.println("Recibido: " + data);

                    String sensorId = data.split(";")[0];
                    double temp = Double.parseDouble(data.split(";")[1].split(":")[1]);

                    // 1. Validar integridad de la blockchain
                    if (!isChainValid()) {
                        System.err.println("ERROR CRÍTICO: Blockchain manipulada");
                        out.println("ERROR:Integridad comprometida");
                        break;
                    }

                    // 2. Comprobar temperatura crítica (FAIL-FAST)
                    if (temp > TEMP_LIMITE) {
                        System.err.println("CRÍTICO: Temperatura " + temp + "°C excede el límite.");
                        out.println("SISTEMA_APAGADO");
                        System.out.println("Simulando apagado de seguridad del servidor...");
                        break;
                    }

                    // 3. Guardar en base de datos SQL
                    int idGenerado = DataService.guardarLectura(sensorId, temp);

                    if (idGenerado != -1) {
                        // 4. Crear bloque en la blockchain
                        String prevHash = blockchain.get(blockchain.size() - 1).hash;
                        String dataHash = generarDataHash(sensorId, temp, String.valueOf(idGenerado));

                        Block nuevoBloque = new Block(dataHash, prevHash);
                        blockchain.add(nuevoBloque);

                        // 5. Vincular SQL con Blockchain
                        DataService.vincularConBlockchain(idGenerado, nuevoBloque.hash);

                        System.out.println("Bloque añadido. Hash: " + nuevoBloque.hash);
                        System.out.println(
                                "Sincronización completa: SQL (ID " + idGenerado +
                                        ") <-> Blockchain (Hash " + nuevoBloque.hash.substring(0, 8) + "...)"
                        );

                        out.println("OK");
                    } else {
                        out.println("ERROR:No se pudo guardar el registro");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= VALIDACIÓN BLOCKCHAIN =================

    public static boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.err.println("ALERTA: Hash incorrecto en bloque " + i);
                return false;
            }

            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.err.println("ALERTA: Enlace roto entre bloques " + (i - 1) + " y " + i);
                return false;
            }
        }
        return true;
    }

    // ================= HASH DEL REGISTRO SQL =================

    public static String generarDataHash(String sensorId, double temp, String idRegistro) {
        String registroCompleto = sensorId + temp + idRegistro;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(registroCompleto.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
