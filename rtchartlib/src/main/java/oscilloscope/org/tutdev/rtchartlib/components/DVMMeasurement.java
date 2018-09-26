package oscilloscope.org.tutdev.rtchartlib.components;

public class DVMMeasurement extends HUD {

    protected float mAmplitude;
    protected float mFrequence;

    public DVMMeasurement(float mAmplitude, float mFrequence) {
        this.mAmplitude = mAmplitude;
        this.mFrequence = mFrequence;
    }

    public float getAmplitude() {
        return mAmplitude;
    }

    public void setAmplitude(float mAmplitude) {
        this.mAmplitude = mAmplitude;
    }

    public float getFrequence() {
        return mFrequence;
    }

    public void setFrequence(float mFrequence) {
        this.mFrequence = mFrequence;
    }
}
