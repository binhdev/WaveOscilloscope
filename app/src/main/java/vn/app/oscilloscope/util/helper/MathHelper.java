package vn.app.oscilloscope.util.helper;

import java.util.Random;

public class MathHelper {
    public static int randomNumber(int min, int max){
        Random random = new Random();
        return min + random.nextInt(max - min + 1);
    }
}
