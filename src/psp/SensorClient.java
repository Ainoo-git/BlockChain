package psp;

import java.io.PrintWriter;
import java.net.Socket;

public class SensorClient {

    private static final String SENSOR_ID = "SENSOR_A1";

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        double temperatura = 42.5;
        out.println(SENSOR_ID + ";TEMP:" + temperatura);

        socket.close();
    }
}
