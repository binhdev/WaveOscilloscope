package oscilloscope.org.tutdev.rtchartlib.data;

import java.util.ArrayList;
import java.util.List;

public class DataSet {

    protected List<Entry> mValues = null;

    protected float mYMax = -Float.MAX_VALUE;

    protected float mYMin = Float.MAX_VALUE;

    protected float mXMax = -Float.MAX_VALUE;

    protected float mXMin = Float.MAX_VALUE;

    public DataSet(List<Entry> values, String label) {
        this.mValues = values;

        if (mValues == null)
            mValues = new ArrayList<>();

        calcMinMax();
    }

    public void calcMinMax() {

        if (mValues == null || mValues.isEmpty())
            return;

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;
        mXMax = -Float.MAX_VALUE;
        mXMin = Float.MAX_VALUE;

        for (Entry e : mValues) {
            calcMinMax(e);
        }
    }

    public void calcMinMaxY(float fromX, float toX) {

        if (mValues == null || mValues.isEmpty())
            return;

        mYMax = -Float.MAX_VALUE;
        mYMin = Float.MAX_VALUE;

        for (int i = 0; i < mValues.size(); i++) {

            // only recalculate y
            calcMinMaxY(mValues.get(i));
        }
    }

    protected void calcMinMax(Entry e) {

        if (e == null)
            return;

        calcMinMaxX(e);

        calcMinMaxY(e);
    }

    protected void calcMinMaxX(Entry e) {

        if (e.getX() < mXMin)
            mXMin = e.getX();

        if (e.getX() > mXMax)
            mXMax = e.getX();
    }

    protected void calcMinMaxY(Entry e) {

        if (e.getY() < mYMin)
            mYMin = e.getY();

        if (e.getY() > mYMax)
            mYMax = e.getY();
    }

    public int getEntryCount() {
        return mValues.size();
    }

    public List<Entry> getValues() {
        return mValues;
    }

    public void setValues(List<Entry> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    public float getYMin() {
        return mYMin;
    }

    public float getYMax() {
        return mYMax;
    }

    public float getXMin() {
        return mXMin;
    }

    public float getXMax() {
        return mXMax;
    }

    public void clear() {
        mValues.clear();
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        calcMinMax();
    }
}
