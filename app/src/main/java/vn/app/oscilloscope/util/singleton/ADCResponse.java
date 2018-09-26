package vn.app.oscilloscope.util.singleton;

public final class ADCResponse {

    private static ADCResponse instance;
    private int cmd;
    private int multiplier;

    private ADCResponse(){}

    public static ADCResponse getInstance(){
        if(instance == null){
            instance = new ADCResponse();
        }
        return instance;
    }

    public void setResponse(int cmd, int multiplier){
        this.cmd = cmd;
        this.multiplier = multiplier;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
