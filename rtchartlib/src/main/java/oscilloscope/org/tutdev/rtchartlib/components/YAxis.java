package oscilloscope.org.tutdev.rtchartlib.components;

import android.graphics.Color;

public class YAxis extends AxisBase {

    protected float mMinWidth = 0.f;

    protected float mMaxWidth = Float.POSITIVE_INFINITY;

    protected float yOffsetZeroLine;

    public float getMinWidth(){
        return mMinWidth;
    }

    public void setMinWidth(float minWidth){
        mMinWidth = minWidth;
    }

    public float getMaxWidth(){
        return mMaxWidth;
    }

    public void setMaxWidth(float maxWidth){
        mMaxWidth = maxWidth;
    }

    public void calculate(float dataMin, float dataMax){

        float min = dataMin;
        float max = dataMax;

        float range = Math.abs(max - min);

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }

        this.mAxisMinimum = min;

        this.mAxisMaximum = max;

        // calc actual range
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }

    public void setYOffsetZeroLine(float value){
        yOffsetZeroLine = value;
    }

    public float getYOffsetZeroLine(){
        return yOffsetZeroLine;
    }
}
