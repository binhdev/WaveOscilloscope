package vn.app.oscilloscope.util.helper;

public class ByteConverter {
    private static short byteToShort(byte hight, byte low){
        return (short) (((int)hight << 8) | low);
    }

    public static short[] byteToShort(byte[] bytes){
        short[] rs = new short[bytes.length / 2];
        for(int i=0; i < bytes.length; i += 2){
            rs[i / 2] = byteToShort(bytes[i], bytes[i+1]);
        }
        return rs;
    }
}
