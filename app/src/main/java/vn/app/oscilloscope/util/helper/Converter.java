package vn.app.oscilloscope.util.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Converter {
    private static final int BYTES_IN_INT = 2;

    private Converter() {}

    public static byte [] convert(short [] array) {
        if (isEmptyInt(array)) {
            return new byte[0];
        }

        return writeInts(array);
    }

    public static short [] convert(byte [] array) {
        if (isEmptyByte(array)) {
            return new short[0];
        }

        return readInts(array);
    }

    private static byte [] writeInts(short [] array) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(array.length * BYTES_IN_INT);
            DataOutputStream dos = new DataOutputStream(bos);
            for (int i = 0; i < array.length; i++) {
                dos.writeShort(array[i]);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static short [] readInts(byte [] array) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(array);
            DataInputStream dataInputStream = new DataInputStream(bis);
            int size = array.length / BYTES_IN_INT;
            short[] res = new short[size];
            for (int i = 0; i < size; i++) {
                res[i] = dataInputStream.readShort();
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isEmptyInt(short[] array){
        return array.length == 0;
    }

    private static boolean isEmptyByte(byte[] array){
        return array.length == 0;
    }
}
