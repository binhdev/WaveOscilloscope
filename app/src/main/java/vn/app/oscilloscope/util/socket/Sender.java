package vn.app.oscilloscope.util.socket;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender extends Thread implements Runnable {
    InetAddress mServerAddress;
    int mServerPort;
    DatagramSocket mSocket;
    String mMessage;

    public Sender(InetAddress serverAddress, int serverPort, String message) throws SocketException {
        this.mSocket = new DatagramSocket();
        this.mServerAddress = serverAddress;
        this.mServerPort = serverPort;
        this.mMessage = message;
    }

    @Override
    public void run() {

        byte[] BUFFER = new byte[4096];
        try {
            DatagramPacket datagramPacket;
            byte[] data = mMessage.getBytes();
            datagramPacket = new DatagramPacket(data, data.length, mServerAddress, mServerPort);
            mSocket.send(datagramPacket);

            DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
            mSocket.receive(incoming);
            String response = new String(incoming.getData());

            byte[] ok = "OK".getBytes();
            datagramPacket = new DatagramPacket(ok, ok.length, mServerAddress, mServerPort);
            mSocket.send(datagramPacket);

            while(true){
                mSocket.receive(incoming);
                Log.e("data: ", incoming.getData().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
}
