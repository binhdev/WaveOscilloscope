package oscilloscope.org.tutdev.rtchartlib.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Entry extends BaseEntry implements Parcelable {

    private float x = 0f;

    public Entry() {}

    public Entry(float x, float y) {
        super(y);
        this.x = x;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.x);
        dest.writeFloat(this.getY());
        dest.writeInt(0);
    }

    protected Entry(Parcel in) {
        this.x = in.readFloat();
        this.setY(in.readFloat());
    }

    public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
}
