package vn.app.oscilloscope.util.producer;

import android.util.Log;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import vn.app.oscilloscope.util.Constants;
import vn.app.oscilloscope.util.client.Message;

public class UdpCommand {
    int mPort;
    String mIp;
    Message mMessage;
    InetAddress mInitAddress;
    volatile boolean isFinish = false;
    boolean isSuccess = false;
    DatagramSocket mDatagramSocket = null;

    public interface UdpCommandListener{
        void onSuccess();
        void onError(Exception error);
    }

    public UdpCommand(Message mMessage, String mIp, int mPort) {
        this.mMessage = mMessage;
        this.mIp = mIp;
        this.mPort = mPort;
    }

    enum Step{
        ONE, TWO
    }

    public void sendMessage(UdpCommandListener listener){
        Thread thread = new Thread(() -> {
            Step step = Step.ONE;
            try {
                mDatagramSocket = new DatagramSocket();
                mDatagramSocket.setSoTimeout(Constants.TIME_RESEND_COMMAND);
                mInitAddress = InetAddress.getByName(Constants.SERVER_IP);
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(mMessage);
                byte[] dataStepOne = jsonMessage.getBytes();

                byte[] dataStepTwo = Constants.COMMAND_OK.getBytes();

                DatagramPacket dpSendStepOne = new DatagramPacket(dataStepOne, dataStepOne.length, mInitAddress, Constants.SERVER_PORT);
                mDatagramSocket.send(dpSendStepOne);

                DatagramPacket dpSendStepTwo = new DatagramPacket(dataStepTwo, dataStepTwo.length, mInitAddress, Constants.SERVER_PORT);

                byte[] buffer = new byte[Constants.DATA_BUFFER];
                DatagramPacket receive = new DatagramPacket(buffer, 0, buffer.length);
                int counter = 0;

                try(DatagramSocket clientSocket = new DatagramSocket(Constants.SERVER_PORT)){
                    while(!isFinish){
                        try{
                            Log.e("Send data: ", counter + "");
                            clientSocket.receive(receive);
                            step = Step.TWO;
                            counter = 0;

                            clientSocket.send(dpSendStepTwo);
                            isFinish = true;
                            isSuccess = true;
                        }catch (SocketTimeoutException e){
                            if(counter > Constants.NUMBER_REPEAT_RESEND) {
                                isFinish = true;
                                listener.onError(e);
                            }

                            switch (step){
                                case ONE:
                                    clientSocket.send(dpSendStepOne);
                                    break;
                                case TWO:
                                    clientSocket.send(dpSendStepOne);
                                    break;
                            }
                            counter++;
                        }
                    }
                }catch (Exception ex){

                }


            }catch (IOException ex){

            }finally {
                closeDataSocket();
            }

            closeDataSocket();
            if(isSuccess){
                listener.onSuccess();
            }
        });
        thread.start();
    }

    private void closeDataSocket(){
        if(mDatagramSocket != null){
            mDatagramSocket.close();
        }
    }
}
