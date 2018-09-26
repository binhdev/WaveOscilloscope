package oscilloscope.org.tutdev.rtchartlib.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import oscilloscope.org.tutdev.rtchartlib.components.HUD;
import oscilloscope.org.tutdev.rtchartlib.utils.Utils;
import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public class HUDRenderer extends Renderer {

    protected HUD mHUD;
    protected Paint mValuePaint;
    protected Paint mBorderPaint;
    protected Paint mBackgroundPaint;

    public HUDRenderer(ViewPortHandler viewPortHandler, HUD hud) {
        super(viewPortHandler);
        mHUD = hud;

        mBorderPaint = new Paint();
        mBorderPaint.setColor(Color.GREEN);
        mBorderPaint.setAlpha(50);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(1.0f);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAlpha(20);

        mValuePaint = new Paint();
        mValuePaint.setColor(Color.YELLOW);
        mValuePaint.setStyle(Paint.Style.STROKE);
        mValuePaint.setStrokeWidth(2.0f);
        mValuePaint.setTextSize(mHUD.getTextSize());
        Log.e("TextSize:", mHUD.getTextSize() + "");
    }

    public void renderHUD(Canvas canvas){
        if(!mHUD.isDrawHUD())
            return;

        float xMargin = Utils.convertDpToPixel(20.0f);
        float yMargin = Utils.convertDpToPixel(20.0f);

        String maxValueLength = getMaxLengthValue();
        Rect boundMax = getBoundText(maxValueLength);
        int wText = boundMax.right - boundMax.left;
        int hText = boundMax.bottom - boundMax.top;
        float wHUD = wText + xMargin * 2;
        float hHUD = (hText + yMargin) * mHUD.getValues().size() + yMargin;

        RectF mRectContent = new RectF(mHUD.getXOffset(), mHUD.getYOffset(), mHUD.getYOffset() + wHUD, mHUD.getYOffset() + hHUD);

        canvas.drawRect(mRectContent, mBackgroundPaint);
        canvas.drawRect(mRectContent, mBorderPaint);

        for (int i=0; i < mHUD.getValues().size(); i++){
            String value = mHUD.getValues().get(i);
            canvas.drawText(value, xMargin, mHUD.getYOffset() + (hText + yMargin) * (i+1) , mValuePaint);
        }
    }

    private Rect getBoundText(String text){
        Rect bound = new Rect();
        mValuePaint.getTextBounds(text, 0, text.length(), bound);
        return bound;
    }

    private String getMaxLengthValue(){
        String max = "";
        for (String v : mHUD.getValues()) {
            if(v.length() > max.length())
                max = v;
        }
        return max;
    }

    private void drawValues(){

    }
}
