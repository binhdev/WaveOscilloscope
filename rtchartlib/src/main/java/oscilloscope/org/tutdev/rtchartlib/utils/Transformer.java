package oscilloscope.org.tutdev.rtchartlib.utils;

import android.graphics.Matrix;
import android.graphics.Path;

import oscilloscope.org.tutdev.rtchartlib.data.DataSet;
import oscilloscope.org.tutdev.rtchartlib.rtutils.TimeBase;
import oscilloscope.org.tutdev.rtchartlib.rtutils.VoltBase;

public class Transformer {

    protected Matrix mMatrixValueToPx = new Matrix();

    protected ViewPortHandler mViewPortHandler;

    public Transformer(ViewPortHandler viewPortHandler){
        this.mViewPortHandler = viewPortHandler;
    }

    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {

        float scaleX = mViewPortHandler.contentWidth() / deltaX;
        float scaleY = mViewPortHandler.contentHeight() / deltaY;

        if (Float.isInfinite(scaleX)) {
            scaleX = 0;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0;
        }

        // setup all matrices
        mMatrixValueToPx.reset();
        mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    public void pathValueToPixel(Path path){
        path.transform(mMatrixValueToPx);
    }

    public float calculateDistance(DataSet dataSet){
        int count = dataSet.getValues().size();
        return mViewPortHandler.getChartWidth() - count > 0 ? mViewPortHandler.getChartWidth() / (count-1) : 1;
    }

    public void pixelsToValue(float[] pixels){

    }

    public float calculateVolt(float lower, float higher, VoltBase voltBase){
        float distance = Math.abs(higher - lower);
        return pixelToValue(distance, 3.0f);
    }

    public float calculateTime(float lower, float higher, TimeBase timeBase){
        float distance = Math.abs(higher - lower);
        return pixelToValue(distance, 3.0f);
    }

    private float pixelToValue(float value, float scale){
        return value * scale;
    }

}
