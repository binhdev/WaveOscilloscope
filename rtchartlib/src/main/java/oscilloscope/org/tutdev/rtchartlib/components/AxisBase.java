package oscilloscope.org.tutdev.rtchartlib.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import java.util.List;

import oscilloscope.org.tutdev.rtchartlib.utils.Utils;

public abstract class AxisBase extends ComponentBase {

    private int mGridColor = Color.GRAY;

    private float mGridLineWidth = 1f;

    private int mAxisLineColor = Color.GRAY;

    private float mAxisLineWidth = 1f;

    public float[] mEntries = new float[]{};

    private int mLabelCount = 6;

    protected boolean mDrawGridLines = true;

    protected boolean mDrawAxisLine = true;

    protected boolean mDrawLabels = true;

    protected List<LimitLine> mLimitLines;

    protected LimitLine mLimitLineHigher;

    protected LimitLine mLimitLineLower;

    protected float mSpaceMin = 0.f;

    protected float mSpaceMax = 0.f;

    public float mAxisMaximum = 0f;

    public float mAxisMinimum = 0f;

    private DashPathEffect mLimitLineDashPathEffect = null;

    public float mAxisRange = 0f;

    /**
     * default constructor
     */
    public AxisBase() {
        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(5f);

        this.mLimitLineHigher = new LimitLine(100, "Higher");
        this.mLimitLineLower = new LimitLine(900, "Lower");
    }

    public int getGridColor() {
        return mGridColor;
    }

    public void setGridColor(int mGridColor) {
        this.mGridColor = mGridColor;
    }

    public float getGridLineWidth() {
        return mGridLineWidth;
    }

    public void setGridLineWidth(float mGridLineWidth) {
        this.mGridLineWidth = mGridLineWidth;
    }

    public int getAxisLineColor() {
        return mAxisLineColor;
    }

    public void setAxisLineColor(int mAxisLineColor) {
        this.mAxisLineColor = mAxisLineColor;
    }

    public float getAxisLineWidth() {
        return mAxisLineWidth;
    }

    public void setAxisLineWidth(float mAxisLineWidth) {
        this.mAxisLineWidth = mAxisLineWidth;
    }

    public float[] getEntries() {
        return mEntries;
    }

    public void setEntries(float[] mEntries) {
        this.mEntries = mEntries;
    }

    public int getLabelCount() {
        return mLabelCount;
    }

    public boolean isDrawLabelsEnabled() {
        return mDrawLabels;
    }

    public boolean isDrawAxisLineEnabled() {
        return mDrawAxisLine;
    }

    public boolean isDrawGridLinesEnabled() {
        return mDrawGridLines;
    }

    public void setDrawGridLines(boolean mDrawGridLines) {
        this.mDrawGridLines = mDrawGridLines;
    }

    public boolean isDrawAxisLine() {
        return mDrawAxisLine;
    }

    public void setDrawAxisLine(boolean mDrawAxisLine) {
        this.mDrawAxisLine = mDrawAxisLine;
    }

    public boolean isDrawLabels() {
        return mDrawLabels;
    }

    public void setDrawLabels(boolean mDrawLabels) {
        this.mDrawLabels = mDrawLabels;
    }

    public List<LimitLine> getLimitLines() {
        return mLimitLines;
    }

    public void setLimitLines(List<LimitLine> limitLines) {
        this.mLimitLines = limitLines;
    }

    public LimitLine getLimitLineHigher() {
        return mLimitLineHigher;
    }

    public void setLimitLineHigher(LimitLine mLimitLineHigher) {
        this.mLimitLineHigher = mLimitLineHigher;
    }

    public LimitLine getLimitLineLower() {
        return mLimitLineLower;
    }

    public void setLimitLineLower(LimitLine mLimitLineLower) {
        this.mLimitLineLower = mLimitLineLower;
    }

    public void enableLimitLineDashedLine(float lineLength, float spaceLength, float phase) {
        mLimitLineDashPathEffect = new DashPathEffect(new float[]{
                lineLength, spaceLength
        }, phase);
    }

    public void setLimitLineDashedLine(DashPathEffect effect) {
        mLimitLineDashPathEffect = effect;
    }

    public DashPathEffect getLimitLineDashedLine(){
        return mLimitLineDashPathEffect;
    }

    public float getSpaceMin() {
        return mSpaceMin;
    }

    public void setSpaceMin(float mSpaceMin) {
        this.mSpaceMin = mSpaceMin;
    }

    public float getSpaceMax() {
        return mSpaceMax;
    }

    public void setSpaceMax(float mSpaceMax) {
        this.mSpaceMax = mSpaceMax;
    }

    public float getAxisMaximum() {
        return mAxisMaximum;
    }

    public void setAxisMaximum(float mAxisMaximum) {
        this.mAxisMaximum = mAxisMaximum;
    }

    public float getAxisMinimum() {
        return mAxisMinimum;
    }

    public void setAxisMinimum(float mAxisMinimum) {
        this.mAxisMinimum = mAxisMinimum;
    }

    public void setLabelCount(int count) {

        if (count > 25)
            count = 25;
        if (count < 2)
            count = 2;

        mLabelCount = count;
    }

    public void calculate(float dataMin, float dataMax) {

        // if custom, use value as is, else use data value
        float min = dataMin - mSpaceMin;
        float max = dataMax + mSpaceMax;

        // temporary range (before calculations)
        float range = Math.abs(max - min);

        // in case all values are equal
        if (range == 0f) {
            max = max + 1f;
            min = min - 1f;
        }

        this.mAxisMinimum = min;
        this.mAxisMaximum = max;

        // actual range
        this.mAxisRange = Math.abs(max - min);
    }
}
