package oscilloscope.org.tutdev.rtchartlib.data;

public abstract class BaseEntry {

    private float y = 0f;

    public BaseEntry() {

    }

    public BaseEntry(float y) {
        this.y = y;
    }
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
