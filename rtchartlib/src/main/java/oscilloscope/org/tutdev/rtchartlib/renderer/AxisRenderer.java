package oscilloscope.org.tutdev.rtchartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import oscilloscope.org.tutdev.rtchartlib.R;
import oscilloscope.org.tutdev.rtchartlib.components.AxisBase;
import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {
    protected AxisBase mAxis;
    protected Paint mGridPaint;
    protected Paint mAxisLinePaint;
    protected Paint mLimitLinePaint;
    protected Paint mAxisLabelPaint;

    public AxisRenderer(ViewPortHandler viewPortHandler, AxisBase axis) {
        super(viewPortHandler);
        this.mAxis = axis;

        if(mViewPortHandler != null) {

            mAxisLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            mGridPaint = new Paint();
            mGridPaint.setColor(Color.GRAY);
            mGridPaint.setAlpha(50);
            mGridPaint.setStyle(Paint.Style.STROKE);
            mGridPaint.setStrokeWidth(1.0f);

            mAxisLinePaint = new Paint();
            mAxisLinePaint.setColor(Color.BLACK);
            mAxisLinePaint.setStrokeWidth(1f);
            mAxisLinePaint.setStyle(Paint.Style.STROKE);

            mLimitLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLimitLinePaint.setColor(Color.GREEN);
            mLimitLinePaint.setStrokeWidth(3f);
            mLimitLinePaint.setStyle(Paint.Style.STROKE);
        }
    }

    /**
     * Draws the axis labels to the screen.
     *
     * @param c
     */
    public abstract void renderAxisLabels(Canvas c);

    /**
     * Draws the grid lines belonging to the axis.
     *
     * @param c
     */
    public abstract void renderGridLines(Canvas c);

    /**
     * Draws the line that goes alongside the axis.
     *
     * @param c
     */
    public abstract void renderAxisLine(Canvas c);

    /**
     * Draws the LimitLines associated with this axis to the screen.
     *
     * @param c
     */
    public abstract void renderLimitLines(Canvas c);

    public abstract void renderMeasurementLine(Canvas c);
}
