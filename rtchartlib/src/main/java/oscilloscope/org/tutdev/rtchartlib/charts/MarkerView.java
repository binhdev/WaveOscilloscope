package oscilloscope.org.tutdev.rtchartlib.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import oscilloscope.org.tutdev.rtchartlib.R;
import oscilloscope.org.tutdev.rtchartlib.utils.Utils;

public class MarkerView extends View {
    /* renamed from: a */
    float mMarginX;
    /* renamed from: b */
    float mMarginY;
    /* renamed from: c */
    Point mPointMarker;
    /* renamed from: d */
    int mZeroX;
    /* renamed from: e */
    String mText;
    /* renamed from: f */
    protected int fX;
    /* renamed from: g */
    protected int fY;
    /* renamed from: h */
    MarkerViewListener mMarkerViewListner;
    /* renamed from: i */
    int mMarketColor;
    /* renamed from: j */
    Paint mPaint;
    /* renamed from: k */
    protected Point mPointZero;
    /* renamed from: l */
    private Paint mPaintMarker;
    /* renamed from: m */
    private Rect mRect;
    /* renamed from: n */
    private int mMarkerPosition;
    /* renamed from: o */
    private int mNotfound;

    public MarkerView(Context context) {
        super(context);
        this.mMarginX = 20.0f;
        this.mMarginY = 10.0f;
        this.mZeroX = 0;
        this.mText = "1";
        this.mMarkerPosition = 0;
        this.mNotfound = -1;
        Utils.init(context);
    }

    public MarkerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMarginX = 20.0f;
        this.mMarginY = 10.0f;
        this.mZeroX = 0;
        this.mText = "1";
        this.mMarkerPosition = 0;
        this.mNotfound = -1;
        this.mPaintMarker = new Paint(1);
        this.mRect = new Rect();
        this.mPointZero = new Point(0, 0);
        this.mPaint = new Paint(1);
        this.mPaint.setColor(Color.GREEN);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.MarkerView, 0, 0);
        try {
            this.mMarkerPosition = obtainStyledAttributes.getInteger(R.styleable.MarkerView_marker_position, 0);
            this.mText = obtainStyledAttributes.getString(R.styleable.MarkerView_marker_text);
            this.mMarketColor = obtainStyledAttributes.getColor(R.styleable.MarkerView_marker_color, Color.WHITE);
        } finally {
            obtainStyledAttributes.recycle();
        }
        Utils.init(context);
    }

    /* renamed from: a */
    private static float resizeText(Paint paint, Rect rect, String str) {
        paint.setTextSize(48.0f);
        Rect rect2 = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect2);
        return Math.min((((float) rect.width()) * 48.0f) / ((float) rect2.width()), (((float) rect.height()) * 48.0f) / ((float) rect2.height()));
    }

    /* renamed from: c */
    private void drawText(Canvas canvas) {
        Rect rect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.mPaint.setTextSize(resizeText(this.mPaint, rect, this.mText) * 0.5f);
        RectF rectF = new RectF(rect);
        rectF.right = this.mPaint.measureText(this.mText, 0, this.mText.length());
        rectF.bottom = this.mPaint.descent() - this.mPaint.ascent();
        rectF.left += (((float) rect.width()) - rectF.right) / 2.0f;
        rectF.top += (((float) rect.height()) - rectF.bottom) / 2.0f;
        canvas.drawText(this.mText, rectF.left, rectF.top - this.mPaint.ascent(), this.mPaint);
    }

    /* renamed from: a */
    void drawRect(Canvas canvas) {
        this.mPaintMarker.setColor(this.mMarketColor);
        Path path = new Path();
        path.moveTo(((float) this.mPointMarker.x) + this.mMarginX, 0.0f);
        path.lineTo((float) this.mPointMarker.x, 0.0f);
        path.lineTo((float) this.mPointMarker.x, (float) getHeight());
        path.lineTo(((float) this.mPointMarker.x) + this.mMarginX, (float) getHeight());
        path.lineTo((float) getWidth(), (float) this.mPointMarker.y);
        canvas.drawPath(path, this.mPaintMarker);
    }

    /* renamed from: b */
    void drawTriangle(Canvas canvas) {
        this.mPaintMarker.setColor(this.mMarketColor);
        Path path = new Path();
        path.moveTo((float) this.mPointMarker.x, 0.0f);
        path.lineTo(((float) this.mPointMarker.x) - this.mMarginX, 0.0f);
        path.lineTo(0.0f, (float) (getHeight() / 2));
        path.lineTo(((float) this.mPointMarker.x) - this.mMarginX, (float) getHeight());
        path.lineTo((float) this.mPointMarker.x, (float) getHeight());
        canvas.drawPath(path, this.mPaintMarker);
    }

    void drawRectHorizontal(Canvas canvas) {
        this.mPaintMarker.setColor(this.mMarketColor);
        Path path = new Path();
        path.moveTo(0.0f, this.mMarginY);
        path.lineTo(0, (float) getHeight());
        path.lineTo(getWidth(), (float) getHeight());
        path.lineTo(getWidth(), this.mMarginY );
        path.lineTo((float) this.mPointMarker.x, 0);
        canvas.drawPath(path, this.mPaintMarker);
    }

    public int getMarkerColor() {
        return this.mMarketColor;
    }

    public Point getPosition() {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        this.mPointZero.x = layoutParams.leftMargin + (getWidth() / 2);
        this.mPointZero.y = layoutParams.topMargin + (getHeight() / 2);
        return this.mPointZero;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (this.mMarkerPosition) {
            case 0:
                drawRect(canvas);
                break;
            case 1:
                drawTriangle(canvas);
                break;
            case 2:
                drawRectHorizontal(canvas);
                break;
            default:
                break;
        }
        drawText(canvas);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Point point = new Point(0, 0);
        super.onSizeChanged(w, h, oldw, oldh);
        switch (this.mMarkerPosition) {
            case MotionEvent.ACTION_DOWN:
                point = new Point(this.mZeroX, getHeight() / 2);
                break;
            case MotionEvent.ACTION_UP:
                point = new Point(getWidth() - this.mZeroX, getHeight() / 2);
                break;
            case MotionEvent.ACTION_MOVE:
                point = new Point(getWidth() / 2, getHeight());
                break;
            default:
                break;
        }
        this.mPointMarker = point;
        this.mMarginX = (float) ((int) (((double) (getWidth() - this.mZeroX)) * 0.6666666666666666d));
        this.mMarginY = (float) ((int) (((double) (getWidth() - this.mZeroX)) * 0.3333333333333333d));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        LayoutParams layoutParams;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                layoutParams = (LayoutParams) getLayoutParams();
                this.fX = rawX - layoutParams.leftMargin;
                this.fY = rawY - layoutParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                if (this.mMarkerViewListner != null) {
                    this.mMarkerViewListner.onMarkerTouchUp(this);
                    break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams = (LayoutParams) getLayoutParams();
                switch (this.mMarkerPosition) {
                    case 0:
                    case 1:
                        layoutParams.topMargin = rawY - this.fY;
                        break;
                    case 2:
                        layoutParams.leftMargin = rawX - this.fX;
                        break;
                    default:
                        break;
                }
                if (layoutParams.topMargin < 0) {
                    layoutParams.topMargin = 0;
                }
                setLayoutParams(layoutParams);
                break;
            default:
                break;
        }
        return true;
    }

    public void setMarkerColor(int i) {
        this.mMarketColor = i;
    }

    public void setTouchUpListener(MarkerViewListener MarkerViewListener) {
        this.mMarkerViewListner = MarkerViewListener;
    }
}