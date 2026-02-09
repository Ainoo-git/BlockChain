package psp;

public class DataService {

    public static int guardarLectura(String sensorId, double temp) {
        System.out.println("SQL INSERT -> Sensor: " + sensorId + " Temp: " + temp);
        return (int) (Math.random() * 10000);
    }

    public static void vincularConBlockchain(int id, String hash) {
        System.out.println("SQL UPDATE -> ID: " + id + " Hash: " + hash);
    }
}
