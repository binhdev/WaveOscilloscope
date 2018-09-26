package vn.app.oscilloscope.util.producer;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import vn.app.oscilloscope.util.Constants;

public class UdpUnicastClient implements Runnable {
    private final int port;
    private final BlockingQueue<byte[]> messageQue;

    public UdpUnicastClient(int port, BlockingQueue<byte[]> messageQue) {
        this.port = port;
        this.messageQue = messageQue;
    }

    @Override
    public void run() {
        try(DatagramSocket clientSocket = new DatagramSocket(port)){
            clientSocket.setSoTimeout(Constants.SOCKET_TIMEOUT);
            while (true){
                byte[] buffer = new byte[Constants.DATA_BUFFER];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);

                clientSocket.receive(datagramPacket);
                this.messageQue.put(datagramPacket.getData());
                Log.e("messageQue Size: ", messageQue.size() + "");
            }
        }catch (SocketException e){
            Log.e("SocketException: ", e.toString());
        }catch (IOException e){
            Log.e("IOException: ", e.toString());
        }catch (InterruptedException e){
            Log.e("InterruptedException: ", e.toString());
        }
    }
}
