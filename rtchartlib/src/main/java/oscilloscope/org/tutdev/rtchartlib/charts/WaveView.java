package oscilloscope.org.tutdev.rtchartlib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.Queue;

import oscilloscope.org.tutdev.rtchartlib.R;

public class WaveView extends SurfaceView implements SurfaceHolder.Callback {

    private final static String TAG = WaveView.class.getName();
    public static boolean mIsHold = true;

    private SurfaceHolder mSurfaceHolder;
    private Context mContext;
    volatile boolean isRunning;
    private Paint mPaint;
    private Paint mPaintXY;
    private Paint mPaintGridXY;

    private Thread mCurrentThread;
    private Canvas mCanvas;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

//    private ChannelManager mChannelManager;

    private int mWidth;
    private int mHeight;

    public final int ROW = 8;
    public final int COL = 10;

    private int mSpaceX;
    private int mSpaceY;

    private int mSpaceTwoPoint = 0;

    private Queue<PointF> mPoints = new LinkedList<>();

    private int x0 = 0;
    private int y0 = 0;

    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;


    Oriental mOriental;

    enum Oriental{
        INVALID,
        HORIZONTAL,
        VERTICAL
    }

    public WaveView(Context context) {
        super(context);
        isRunning = false;
        mPaint = new Paint();
        mCanvas = null;
        mCurrentThread = null;
        mOriental = Oriental.INVALID;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isRunning = false;
        mPaint = new Paint();
        mCanvas = null;
        mCurrentThread = null;

        mContext = context;
        mOriental = Oriental.INVALID;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mPaintXY = new Paint();
        mPaintXY.setColor(ContextCompat.getColor(mContext, R.color.coordXY));
        mPaintXY.setStyle(Paint.Style.STROKE);
        mPaintXY.setStrokeWidth(2.0f);

        mPaintGridXY = new Paint();
        mPaintGridXY.setColor(ContextCompat.getColor(mContext, R.color.line_grid_color));
        mPaintGridXY.setAlpha(50);
        mPaintGridXY.setStyle(Paint.Style.STROKE);
        mPaintGridXY.setStrokeWidth(1.0f);

        mPaint = new Paint();

        this.mScaleGestureDetector = new ScaleGestureDetector(context, new SimpleScaleGesture(this));
        this.mGestureDetector = new GestureDetector(context, new SimpleGesture(this));
    }

    private class SimpleGesture extends GestureDetector.SimpleOnGestureListener{
        final WaveView mWaveView;

        private SimpleGesture(WaveView waveView) {
            this.mWaveView = waveView;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return this.mWaveView.mOriental == Oriental.INVALID;
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            this.mWaveView.detectAreaPlot(motionEvent);
            return true;
        }
    }

    private class SimpleScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        final WaveView mWaveView;
        int margin = 80;
        float spanX;
        float spanY;
        float mW;
        float mH;

        private SimpleScaleGesture(WaveView waveView) {
            this.mWaveView = waveView;
            this.margin = 80;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            this.mWaveView.setIsHold(true);
            mScaleY *= detector.getScaleFactor();
            mScaleY = Math.max(0.1f, Math.min(mScaleY, 5.0f));
            Log.e("scale", mScaleY + "");
//            this.spanX = mScaleGestureDetector.getCurrentSpanX();
//            this.spanY = mScaleGestureDetector.getCurrentSpanY();
//            if (this.mWaveView.mOriental == Oriental.HORIZONTAL) {
//                if (this.mW > this.spanX + ((float) this.margin)) {
//                    mScaleX *= detector.getScaleFactor();
//                    mScaleX = (int)Math.max(0.1f, Math.min(mScaleX, 5.0f));
//                } else if (this.mW < this.spanX - ((float) this.margin)) {
//                    mScaleX -= this.spanX;
//                }
//                this.mW = this.spanX;
//            } else if (this.mWaveView.mOriental == Oriental.VERTICAL) {
//                if (this.mH > this.spanY + ((float) this.margin)) {
//                    mScaleY += this.spanY;
//                } else if (this.mH < this.spanY - ((float) this.margin)) {
//                    mScaleY -= this.spanY;
//                }
//                this.mH = this.spanY;
//            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mW = mWaveView.getWidth();
            mH = mWaveView.getHeight();
            this.mWaveView.mOriental = Oriental.VERTICAL;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            this.mWaveView.mOriental = Oriental.INVALID;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
        mSpaceY = mHeight / ROW;
        mSpaceX = mWidth / COL;
        x0 = mWidth / 2;
        y0 = mHeight / 2;
        drawChart(0, y0);
    }

    private Rect getRect(){
        int round = mHeight / 2 - 180;
        int round2 = round + 2 * 180;
        return new Rect(0, round, mHeight, round2);
    }

    //Detect click on plot
    private void detectAreaPlot(MotionEvent motionEvent) {
//        if (this.mChannelManager != null) {
//            if (this.mChannelManager.getCH1().isAvaiable() && getRect(this.mChannelManager.getCH1()).contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
//                this.mChannelManager.setChannelTaged(this.mChannelManager.getCH1());
//            }
//            if (this.mChannelManager.getCH2().isAvaiable() && getRect(this.mChannelManager.getCH2()).contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
//                this.mChannelManager.setChannelTaged(this.mChannelManager.getCH2());
//            }
//        }
    }

    public void updatePoint(PointF[] dataPoints, int x, int y){
        mPoints.clear();
        for (PointF dataPoint : dataPoints) {
            mPoints.add(dataPoint);
        }

        mSpaceTwoPoint = mWidth / dataPoints.length > 0 ? mWidth / dataPoints.length : 1;
        drawChart(x, y);
    }

    public void drawChart(int x, int y){
        if (mSurfaceHolder != null && mSurfaceHolder.getSurface().isValid()) {
            final Canvas lockCanvas = mSurfaceHolder.lockCanvas();
            if (lockCanvas != null) {
                reCalculator(x, y);
                drawCoordChart(lockCanvas);

                drawCH(lockCanvas);
                mSurfaceHolder.unlockCanvasAndPost(lockCanvas);
            }
        }
    }
    public void drawCoordChart(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(mContext, R.color.background_grid_color));
        drawXY(canvas);
        drawGridXY(canvas);
    }

    private void reCalculator(int x, int y) {
        this.y0 = y;
//        if (this.f1882m.m2170i().m2063f()) {
//            this.f1878i = this.f1882m.m2170i().m2058c();
//            short[] a = this.mPoints.m2170i().m2055a();
//            for (int i = 0; i < this.mPoints.size(); i++) {
//                this.mPoints.x = ad.m2025a(i, 0.0f, (float) this.f1882m.m2184y(), 0.0f, (float) mScreenWidth);
//                this.mPoints[i].y = ad.m2025a(a[i] + x, 8.0f, 248.0f, (float) mScreenHeight, 0.0f);
//            }
//        }
    }
    /**
     * Draw coordinate Descartes
     * @param canvas
     */
    void drawXY(Canvas canvas){
        Path path = new Path();
        path.moveTo(0.0f, (float) mHeight / 2);
        path.lineTo((float) mWidth, (float) mHeight / 2);
        canvas.drawPath(path, mPaintXY);

        path.moveTo((float) mWidth / 2, 0.0f);
        path.lineTo((float) mWidth / 2, (float) mHeight);
        canvas.drawPath(path, mPaintXY);
    }

    void drawGridXY(Canvas canvas){
        Path path = new Path();
        float f = ((float) mHeight) / ROW;
        for (int i = 1; i < ROW; i++) {
            if (i != 4) {
                float f2 = ((float) i) * f;
                path.moveTo(0.0f, f2);
                path.lineTo((float) mWidth, f2);
                canvas.drawPath(path, mPaintGridXY);
            }
        }
        f = ((float) mWidth) / COL;
        for (int i2 = 1; i2 < COL; i2++) {
            if (i2 != 5) {
                float f3 = ((float) i2) * f;
                path.moveTo(f3, 0.0f);
                path.lineTo(f3, (float) mHeight);
                canvas.drawPath(path, mPaintGridXY);
            }
        }
    }

    void drawCH(Canvas canvas) {
        if (this.mPoints.size() > 0) {
            Path path = new Path();
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.coordXY));
            mPaint.setStrokeWidth(1.0f);
            mPaint.setAlpha(20);
            canvas.drawRect(getRect(), mPaint);

            mPaint.setAlpha(255);
            mPaint.setStyle(Paint.Style.STROKE);

            PointF firtDataPoint = mPoints.peek();
            path.moveTo(firtDataPoint.x, mHeight/2 + firtDataPoint.y);
            Log.e("y0:", y0 + "");
            int i = 0;
            for (PointF dataPoint : mPoints) {
                i++;
                path.lineTo(dataPoint.x + i * mSpaceTwoPoint, mHeight/2 + dataPoint.y * mScaleY);
            }

            canvas.drawPath(path, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Object obj;
        boolean onTouchEvent = this.mScaleGestureDetector.onTouchEvent(motionEvent);
        boolean z = true;
        if (!this.mGestureDetector.onTouchEvent(motionEvent)) {
            if (!onTouchEvent) {
                obj = null;
                if (obj == null) {
                    if (super.onTouchEvent(motionEvent)) {
                        return true;
                    }
                    z = false;
                }
                return z;
            }
        }
        obj = 1;
        if (obj == null) {
            if (super.onTouchEvent(motionEvent)) {
                return true;
            }
            z = false;
        }
        return z;
    }

    public void setIsHold(boolean value){
        mIsHold = value;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
