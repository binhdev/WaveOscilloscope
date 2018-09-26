package vn.app.oscilloscope.util.producer;

import android.util.Log;

import java.util.concurrent.BlockingQueue;

import vn.app.oscilloscope.ui.PointF;
import vn.app.oscilloscope.util.helper.ByteConverter;

public class DataProcessor implements Runnable {
    private final BlockingQueue<byte[]> messageQueue;

    public interface DataReceiverListener{
        void receive(PointF[] dataPoints);
    }

    DataReceiverListener listener;

    public DataProcessor(BlockingQueue<byte[]> messageQueue, DataReceiverListener listener) {
        this.messageQueue = messageQueue;
        this.listener = listener;
    }

    @Override
    public void run() {
        int counter = 0;
        while (true){
            try {
                byte[] rawData = this.messageQueue.take();
                counter++;
                short[] values = ByteConverter.byteToShort(rawData);
                PointF dataPoints[] = new PointF[values.length];

                short i = 0;
                for (short value : values) {
                    PointF dataPoint = new PointF(i , value);
                    dataPoints[i] = dataPoint;
//                    Log.e("value: ", value + "");
                    i++;
                }
                listener.receive(dataPoints);

                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
