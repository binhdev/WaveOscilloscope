package vn.app.oscilloscope.ui.graph;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import oscilloscope.org.tutdev.rtchartlib.charts.Chart;
import oscilloscope.org.tutdev.rtchartlib.charts.TriggerLevel;
import oscilloscope.org.tutdev.rtchartlib.charts.ZeroLine;
import oscilloscope.org.tutdev.rtchartlib.components.XAxis;
import oscilloscope.org.tutdev.rtchartlib.components.YAxis;
import oscilloscope.org.tutdev.rtchartlib.data.DataSet;
import oscilloscope.org.tutdev.rtchartlib.data.Entry;
import vn.app.oscilloscope.R;
import vn.app.oscilloscope.ui.BaseActivity;
import vn.app.oscilloscope.ui.PointF;
import vn.app.oscilloscope.util.Constants;
import vn.app.oscilloscope.util.producer.DataProcessor;
import vn.app.oscilloscope.util.producer.UdpUnicastClient;

public class GraphActivity extends BaseActivity {

    @BindView(R.id.chart)
    public Chart mChart;

    @BindView(R.id.amplitude_lower)
    public ZeroLine mAmplitudeLower;

    @BindView(R.id.amplitude_higher)
    public ZeroLine mAmplitudeHigher;

    @BindView(R.id.timeline_lower)
    public ZeroLine mTimeLineLower;

    @BindView(R.id.timeline_higher)
    public ZeroLine TimeLineHigher;

    private boolean isRunning = false;

    UdpUnicastClient client;
    DataProcessor dataProcessor;
    BlockingQueue<byte[]> messageQueue;

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        init();
    }

    private void init(){
        ButterKnife.bind(this);
        List<Entry> entryList = new ArrayList<>();
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mChart.getHUDAmplitude().setTypeface(tf);

        DataSet dataSet = new DataSet(entryList, "Oscilloscope");
        mChart.setData(dataSet);
        mChart.notifityDataChange();

        messageQueue = new ArrayBlockingQueue<>(Constants.QUEUE_CAPACITY);
        client = new UdpUnicastClient(Constants.SERVER_PORT, messageQueue);
        dataProcessor = new DataProcessor(messageQueue, dataPoints -> {
            entryList.clear();
            for (PointF point : dataPoints) {
                Entry entry = new Entry(point.x, value(point.y));
                entryList.add(entry);
            }
            mChart.setData(dataSet);
            mChart.notifityDataChange();
        });

        /**
         * Limit lines
         */
        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.getLimitLineHigher().setYOffset(500);
//        yAxisLeft.getLimitLineHigher().setYOffset(mTimeLineLower.getLeft() + mTimeLineLower.getWidth() / 2);

        yAxisLeft.getLimitLineLower().setYOffset(600);
//        yAxisLeft.getLimitLineLower().setYOffset(TimeLineHigher.getLeft() + TimeLineHigher.getWidth() / 2);
        yAxisLeft.setGridColor(Color.WHITE);

        yAxisLeft.enableLimitLineDashedLine(10f, 10f, 0);

        XAxis xAxis = mChart.getXAxis();
        xAxis.getLimitLineHigher().setXOffset(500);
//        xAxis.getLimitLineHigher().setXOffset(mAmplitudeLower.getTop() + mAmplitudeLower.getHeight() / 2);
        xAxis.getLimitLineLower().setXOffset(600);
//        xAxis.getLimitLineLower().setXOffset(mAmplitudeHigher.getTop() + mAmplitudeHigher.getHeight() / 2);
        xAxis.setGridColor(Color.WHITE);

        xAxis.enableLimitLineDashedLine(10f, 10f, 0);

        mAmplitudeLower.setTouchUpListener(view -> {
            yAxisLeft.getLimitLineHigher().setYOffset(view.getTop() + view.getHeight()/2);
        });

        mAmplitudeHigher.setTouchUpListener(view -> {
            yAxisLeft.getLimitLineLower().setYOffset(view.getTop() + view.getHeight()/2);
        });

        mTimeLineLower.setTouchUpListener(view -> {
            xAxis.getLimitLineHigher().setXOffset(view.getLeft() + view.getWidth()/2);
        });

        TimeLineHigher.setTouchUpListener(view -> {
            xAxis.getLimitLineLower().setXOffset(view.getLeft() + view.getWidth()/2);
        });
    }

    public void onRunClick(View view){
        switch (view.getId()){
            case R.id.btn_run:
                start();
                break;
        }
    }

    private void start(){
//        Message message = new Message(111, 123);
//        UdpCommand udpCommand = new UdpCommand(message, Constants.SERVER_IP, Constants.SERVER_PORT);
//        udpCommand.sendMessage(new UdpCommand.UdpCommandListener() {
//            @Override
//            public void onSuccess() {
//                Log.e("Send message", "success");
//            }
//
//            @Override
//            public void onError(Exception error) {
//                Log.e("Send message", "error: " + error.toString());
//            }
//        });
        executorService.submit(client);
        executorService.submit(dataProcessor);
    }

    private float value(float e){
        return e > 800 ? 800 : e;
    }
}
