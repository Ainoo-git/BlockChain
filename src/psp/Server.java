package psp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5000;
    private static final double LIMITE_TEMP = 50.0;

    public static void main(String[] args) throws IOException {

        BlockchainManager blockchain = new BlockchainManager();
        ServerSocket server = new ServerSocket(PORT);

        System.out.println("Servidor escuchando en el puerto " + PORT);

        while (true) {
            Socket cliente = server.accept();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(cliente.getInputStream())
            );

            String mensaje = in.readLine(); // SENSOR_A1;TEMP:42.5
            String[] partes = mensaje.split(";");

            String sensorId = partes[0];
            double temp = Double.parseDouble(partes[1].split(":")[1]);

            if (!blockchain.isChainValid()) {
                System.out.println("Blockchain corrupta. Conexión rechazada.");
                cliente.close();
                continue;
            }

            if (temp > LIMITE_TEMP) {
                System.out.println("APAGADO CRÍTICO - Temperatura: " + temp);
                break;
            }

            int id = DataService.guardarLectura(sensorId, temp);
            String dataHash = sensorId + ":" + temp + ":" + id;

            blockchain.addBlock(dataHash);
            DataService.vincularConBlockchain(id, dataHash);

            cliente.close();
        }
    }
}
