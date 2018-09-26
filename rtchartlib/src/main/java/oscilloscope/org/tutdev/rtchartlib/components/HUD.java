package oscilloscope.org.tutdev.rtchartlib.components;

import java.util.ArrayList;
import java.util.List;

public class HUD extends ComponentBase {

    protected boolean mDrawHUD = true;

    private List<String> mValues = new ArrayList<>();

    public boolean isDrawHUD() {
        return mDrawHUD;
    }

    public void setDrawHUD(boolean drawHUD) {
        this.mDrawHUD = drawHUD;
    }

    public void setValues(List<String> values){
        mValues.clear();
        mValues.addAll(values);
    }

    public List<String> getValues(){
        return mValues;
    }
}
