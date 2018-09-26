package oscilloscope.org.tutdev.rtchartlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {

    private static DisplayMetrics mMetrics;

    public static void init(Context context) {
        if (context == null) {
            Resources res = context.getResources();
            mMetrics = res.getDisplayMetrics();
        }
    }

    public static float convertDpToPixel(float dp) {

        if (mMetrics == null) {

            Log.e("RTChartLib-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                            " calling Utils.convertDpToPixel(...). Otherwise conversion does not " +
                            "take place.");
            return dp;
        }

        return dp * mMetrics.density;
    }

    public static float convertPixelsToDp(float px) {

        if (mMetrics == null) {

            Log.e("MPChartLib-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                            " calling Utils.convertPixelsToDp(...). Otherwise conversion does not" +
                            " take place.");
            return px;
        }

        return px / mMetrics.density;
    }
}
