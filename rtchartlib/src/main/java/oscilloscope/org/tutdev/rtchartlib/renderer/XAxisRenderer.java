package oscilloscope.org.tutdev.rtchartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import oscilloscope.org.tutdev.rtchartlib.components.XAxis;
import oscilloscope.org.tutdev.rtchartlib.utils.RTPointF;
import oscilloscope.org.tutdev.rtchartlib.utils.Utils;
import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public class XAxisRenderer extends AxisRenderer {

    protected XAxis mXAxis;

    public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis) {
        super(viewPortHandler, xAxis);
        this.mXAxis = xAxis;

        mAxisLabelPaint.setColor(Color.WHITE);
        mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
        mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10f));
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;
        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        RTPointF pointF = RTPointF.getInstance(0, 0);

    }

    @Override
    public void renderGridLines(Canvas c) {
        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setAlpha(50);

        Path path = new Path();
        float f = (mViewPortHandler.getChartWidth()) / mViewPortHandler.XAXIS_UNIT;
        for (int i2 = 1; i2 < mViewPortHandler.XAXIS_UNIT; i2++) {
            float f3 = ((float) i2) * f;
            path.moveTo(f3, 0.0f);
            path.lineTo(f3, mViewPortHandler.getChartHeight());
            c.drawPath(path, mGridPaint);
        }
    }

    @Override
    public void renderAxisLine(Canvas c) {
        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
    }

    @Override
    public void renderLimitLines(Canvas c) {

    }

    @Override
    public void renderMeasurementLine(Canvas c) {
        if(!mXAxis.isEnabled())
            return;

        Path path = new Path();
        mLimitLinePaint.setPathEffect(mXAxis.getLimitLineDashedLine());
        mLimitLinePaint.setStrokeWidth(3.0f);

        path.moveTo(mXAxis.getLimitLineHigher().getXOffset(), 0);
        path.lineTo(mXAxis.getLimitLineHigher().getXOffset(), mViewPortHandler.getChartHeight());
        c.drawPath(path, mLimitLinePaint);

        path.moveTo(mXAxis.getLimitLineLower().getXOffset(), 0);
        path.lineTo(mXAxis.getLimitLineLower().getXOffset(), mViewPortHandler.getChartHeight());
        c.drawPath(path, mLimitLinePaint);
    }
}
