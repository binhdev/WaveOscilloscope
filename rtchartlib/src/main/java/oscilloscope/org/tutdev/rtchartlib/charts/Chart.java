package oscilloscope.org.tutdev.rtchartlib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import oscilloscope.org.tutdev.rtchartlib.components.HUD;
import oscilloscope.org.tutdev.rtchartlib.components.XAxis;
import oscilloscope.org.tutdev.rtchartlib.components.YAxis;
import oscilloscope.org.tutdev.rtchartlib.data.DataSet;
import oscilloscope.org.tutdev.rtchartlib.interfaces.ChartInterface;
import oscilloscope.org.tutdev.rtchartlib.renderer.DataRenderer;
import oscilloscope.org.tutdev.rtchartlib.renderer.HUDRenderer;
import oscilloscope.org.tutdev.rtchartlib.renderer.XAxisRenderer;
import oscilloscope.org.tutdev.rtchartlib.renderer.YAxisRenderer;
import oscilloscope.org.tutdev.rtchartlib.rtutils.TimeBase;
import oscilloscope.org.tutdev.rtchartlib.rtutils.VoltBase;
import oscilloscope.org.tutdev.rtchartlib.utils.Transformer;
import oscilloscope.org.tutdev.rtchartlib.utils.Utils;
import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public class Chart extends SurfaceView implements SurfaceHolder.Callback, ChartInterface {

    protected ViewPortHandler mViewPortHandler = new ViewPortHandler();

    protected XAxis mXAxis;
    protected YAxis mAxisLeft;

    protected YAxisRenderer mAxisRendererLeft;
    protected XAxisRenderer mXAxisRenderer;

    protected DataRenderer mRenderer;

    protected HUD mHUDAmplitude;
    protected HUDRenderer mHUDAmplitudeRender;

    private SurfaceHolder mSurfaceHolder;
    protected Transformer mTransformer;

    private DataSet mData;
    private ScaleGestureDetector mScaleGestureDetector;

    private Oriental mOriental;

    TimeBase eTimeBase = TimeBase.HDIV_2mS;
    VoltBase eVoltBase = VoltBase.VDIV_1V;

    private enum Oriental{
        INVALID,
        HORIZONTAL,
        VERTICAL
    }

    private float mScaleX = 1f;
    private float mScaleY = 1f;

    public Chart(Context context) {
        super(context);

        init();
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    protected void init(){
        Utils.init(getContext());
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mTransformer = new Transformer(mViewPortHandler);
        mTransformer.prepareMatrixValuePx(0,
                mViewPortHandler.getChartWidth(),
                mViewPortHandler.getChartHeight(),
                0);

        mXAxis = new XAxis();
        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, mXAxis);

        mAxisLeft = new YAxis();
        mAxisRendererLeft = new YAxisRenderer(mViewPortHandler, mAxisLeft);

        mRenderer = new DataRenderer(mViewPortHandler, this);

        mHUDAmplitude = new HUD();
        List<String> values = new ArrayList<>();
        mHUDAmplitude.setValues(values);
        mHUDAmplitude.setTextSize(20.0f);
        mHUDAmplitudeRender = new HUDRenderer(mViewPortHandler, mHUDAmplitude);

        this.mScaleGestureDetector = new ScaleGestureDetector(getContext(), new SimpleScaleGesture());
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mViewPortHandler.setChartDimens(width, height);
        notifityDataChange();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void notifityDataChange(){
        if (mSurfaceHolder != null && mSurfaceHolder.getSurface().isValid()) {
            final Canvas lockCanvas = mSurfaceHolder.lockCanvas();
            if (lockCanvas != null) {
                renderBackground(lockCanvas);
                renderAxis(lockCanvas);
                renderData(lockCanvas);

                updateHUD();
                mHUDAmplitudeRender.renderHUD(lockCanvas);
                mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
            }
        }
    }

    private void renderBackground(Canvas canvas){
        canvas.drawColor(Color.GRAY);
    }

    private void renderAxis(Canvas canvas){

        //Render XAxis
        mXAxisRenderer.renderGridLines(canvas);
        mXAxisRenderer.renderAxisLabels(canvas);
        mXAxisRenderer.renderAxisLine(canvas);
        mXAxisRenderer.renderLimitLines(canvas);
        mXAxisRenderer.renderMeasurementLine(canvas);

        //Render YAxis
        mAxisRendererLeft.renderGridLines(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLine(canvas);
        mAxisRendererLeft.renderLimitLines(canvas);
        mAxisRendererLeft.renderMeasurementLine(canvas);

    }

    public void setData(DataSet dataSet){
        this.mData = dataSet;
    }

    private void renderData(Canvas canvas){
        mRenderer.drawData(canvas);
    }

    @Override
    public DataSet getData() {
        return mData;
    }

    public XAxis getXAxis() {
        return mXAxis;
    }

    public YAxis getAxisLeft() {
        return mAxisLeft;
    }

    public Transformer getTransformer() {
        return mTransformer;
    }

    public TimeBase geteTimeBase() {
        return eTimeBase;
    }

    public void seteTimeBase(TimeBase eTimeBase) {
        this.eTimeBase = eTimeBase;
    }

    public VoltBase geteVoltBase() {
        return eVoltBase;
    }

    public void seteVoltBase(VoltBase eVoltBase) {
        this.eVoltBase = eVoltBase;
    }

    private void updateHUD(){
        List<String> values = new ArrayList<>();
        float time = mTransformer.calculateTime(mXAxis.getLimitLineHigher().getXOffset(), mXAxis.getLimitLineLower().getXOffset(), eTimeBase);
        float volt = mTransformer.calculateVolt(mAxisLeft.getLimitLineHigher().getYOffset(), mAxisLeft.getLimitLineLower().getYOffset(), eVoltBase);
        values.add("CH1 - Volt: " + volt );
        values.add("CH1 - Time: " + time);
        values.add("CH3: 100mv");
        mHUDAmplitude.setValues(values);
    }

    public HUD getHUDAmplitude(){
        return mHUDAmplitude;
    }

    /**
     * Gesture Listener
     */


    private class SimpleGesture extends GestureDetector.SimpleOnGestureListener{

        private SimpleGesture() {

        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return true;
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }
    }

    private class SimpleScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        int margin = 80;
        float spanX;
        float spanY;
        float mW;
        float mH;

        private SimpleScaleGesture() {
            this.margin = 80;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (mOriental == Oriental.HORIZONTAL) {
                if(mScaleX < 0 || mScaleX> 5)
                    return true;
                if(detector.getPreviousSpanX() < detector.getCurrentSpanX())
                    mScaleX++;
                else
                    mScaleX--;
            } else if (mOriental == Oriental.VERTICAL) {
                if(mScaleY < 0 || mScaleY > 5)
                    return true;
                if(detector.getPreviousSpanY() < detector.getCurrentSpanY())
                    mScaleY++;
                else
                    mScaleY--;
                mRenderer.setScaleY(mScaleY);
            }
            Log.i("mScaleX: ", mScaleX + "");
            Log.i("mScaleY: ", mScaleY + "");
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            if(detector.getCurrentSpanX() > detector.getCurrentSpanY())
                mOriental = Oriental.HORIZONTAL;
            else if(Math.abs(detector.getCurrentSpanX() - detector.getCurrentSpanY()) > 0)
                mOriental = Oriental.VERTICAL;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mOriental = Oriental.INVALID;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
