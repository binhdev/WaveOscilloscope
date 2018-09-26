package vn.app.oscilloscope.util;

public class Constants {
    static double[] TIME_FRAME = new double[]{
      15.0d, 20.0d, 50.0d
    };

    public static int DATA_BUFFER = 4096;
    public static int QUEUE_CAPACITY = 10;
    public static String SERVER_IP = "192.168.1.7";
    public static int SERVER_PORT = 50001;

    public static int SOCKET_TIMEOUT = 30000;
    public static int TIME_RESEND_COMMAND = 3000;
    public static int NUMBER_REPEAT_RESEND = 3;

    public static String COMMAND_OK = "{OK}";
}
