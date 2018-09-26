package vn.app.oscilloscope.util.client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    @SerializedName("CMD")
    @Expose
    public int cmd;

    @SerializedName("DATA")
    @Expose
    public int data;

    public Message(int cmd, int data) {
        this.cmd = cmd;
        this.data = data;
    }
}
