package oscilloscope.org.tutdev.rtchartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.Align;
import android.graphics.Path;
import oscilloscope.org.tutdev.rtchartlib.data.DataSet;
import oscilloscope.org.tutdev.rtchartlib.data.Entry;
import oscilloscope.org.tutdev.rtchartlib.interfaces.ChartInterface;
import oscilloscope.org.tutdev.rtchartlib.utils.RTPointF;
import oscilloscope.org.tutdev.rtchartlib.utils.Transformer;
import oscilloscope.org.tutdev.rtchartlib.utils.Utils;
import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public class DataRenderer extends Renderer {

    protected Paint mRenderPaint;

    protected Paint mDrawPaint;

    protected Paint mValuePaint;

    protected ChartInterface mChart;

    protected float mScaleY = 1f;

    public DataRenderer(ViewPortHandler viewPortHandler, ChartInterface chart) {
        super(viewPortHandler);
        mChart = chart;

        mRenderPaint = new Paint();
        mRenderPaint.setColor(Color.YELLOW);
        mRenderPaint.setStrokeWidth(3.0f);
        mRenderPaint.setAlpha(255);
        mRenderPaint.setStyle(Style.STROKE);

        mDrawPaint = new Paint(Paint.DITHER_FLAG);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.rgb(63, 63, 63));
        mValuePaint.setTextAlign(Align.CENTER);
        mValuePaint.setTextSize(Utils.convertDpToPixel(9f));
    }

    public void drawData(final Canvas canvas){
        DataSet dataSet = mChart.getData();
        if(dataSet.getValues().size() == 0)
            return;

        Path path = new Path();

        Transformer trans = mChart.getTransformer();
        float distance = trans.calculateDistance(dataSet);
        RTPointF firtDataPoint = RTPointF.getInstance(0, mViewPortHandler.getChartHeight()/ 2);
        path.moveTo(firtDataPoint.x, firtDataPoint.y);

        int i = 0;
        for (Entry entry : dataSet.getValues()) {
            path.lineTo(i * distance, mViewPortHandler.getChartHeight()/ 2 + mScaleY * entry.getY());
            i++;
        }

        canvas.drawPath(path, mRenderPaint);
    }

    public void drawValues(Canvas c){

    }

    public void setScaleY(float value){
        mScaleY = value;
    }
}
