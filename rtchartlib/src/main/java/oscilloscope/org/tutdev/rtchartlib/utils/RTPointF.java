package oscilloscope.org.tutdev.rtchartlib.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RTPointF extends ObjectPool.Poolable {

    private static ObjectPool<RTPointF> pool;

    public float x;
    public float y;

    static {
        pool = ObjectPool.create(32, new RTPointF(0,0));
        pool.setReplenishPercentage(0.5f);
    }

    public RTPointF() {
    }

    public RTPointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static RTPointF getInstance(float x, float y) {
        RTPointF result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static RTPointF getInstance() {
        return pool.get();
    }

    public static RTPointF getInstance(RTPointF copy) {
        RTPointF result = pool.get();
        result.x = copy.x;
        result.y = copy.y;
        return result;
    }

    public static void recycleInstance(RTPointF instance){
        pool.recycle(instance);
    }

    public static void recycleInstances(List<RTPointF> instances){
        pool.recycle(instances);
    }

    public static final Parcelable.Creator<RTPointF> CREATOR = new Parcelable.Creator<RTPointF>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        public RTPointF createFromParcel(Parcel in) {
            RTPointF r = new RTPointF(0,0);
            r.my_readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        public RTPointF[] newArray(int size) {
            return new RTPointF[size];
        }
    };

    public void my_readFromParcel(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    @Override
    protected ObjectPool.Poolable instantiate() {
        return new RTPointF(0,0);
    }
}