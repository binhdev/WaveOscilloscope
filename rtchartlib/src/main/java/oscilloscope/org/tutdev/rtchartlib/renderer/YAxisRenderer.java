package oscilloscope.org.tutdev.rtchartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import oscilloscope.org.tutdev.rtchartlib.components.YAxis;
import oscilloscope.org.tutdev.rtchartlib.utils.Utils;
import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public class YAxisRenderer extends AxisRenderer {

    protected YAxis mYAxis;

    protected Paint mZeroLinePaint;

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis) {
        super(viewPortHandler, yAxis);

        this.mYAxis = yAxis;

        if(mViewPortHandler != null) {

            mAxisLabelPaint.setColor(Color.BLACK);
            mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));

            mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mZeroLinePaint.setColor(Color.GRAY);
            mZeroLinePaint.setStrokeWidth(1f);
            mZeroLinePaint.setStyle(Paint.Style.STROKE);
        }

        mGridPaint.setColor(Color.WHITE);
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;
        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());
    }

    @Override
    public void renderGridLines(Canvas c) {
        if (!mYAxis.isEnabled())
            return;

        mGridPaint.setColor(mYAxis.getGridColor());
        mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
        mGridPaint.setAlpha(50);

        if (mYAxis.isDrawGridLinesEnabled()) {
            Path path = new Path();

            float f = (mViewPortHandler.getChartHeight()) / mViewPortHandler.YAXIS_UNIT;
            for (int i = 1; i < mViewPortHandler.YAXIS_UNIT; i++) {
                float f2 = ((float) i) * f;
                path.moveTo(0.0f, f2);
                path.lineTo(mViewPortHandler.getChartWidth(), f2);
                c.drawPath(path, mGridPaint);
            }
        }
    }

    @Override
    public void renderAxisLine(Canvas c) {
        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;
        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());
    }

    @Override
    public void renderLimitLines(Canvas c) {

    }

    @Override
    public void renderMeasurementLine(Canvas c) {
        if(!mYAxis.isEnabled())
            return;

        Path path = new Path();
        mLimitLinePaint.setPathEffect(mYAxis.getLimitLineDashedLine());

        path.moveTo(0, mYAxis.getLimitLineHigher().getYOffset());
        path.lineTo(mViewPortHandler.getChartWidth(), mYAxis.getLimitLineHigher().getYOffset());
        c.drawPath(path, mLimitLinePaint);

        path.moveTo(0, mYAxis.getLimitLineLower().getYOffset());
        path.lineTo(mViewPortHandler.getChartWidth(), mYAxis.getLimitLineLower().getYOffset());
        c.drawPath(path, mLimitLinePaint);
    }
}
