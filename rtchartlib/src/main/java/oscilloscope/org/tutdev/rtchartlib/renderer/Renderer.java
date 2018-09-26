package oscilloscope.org.tutdev.rtchartlib.renderer;

import oscilloscope.org.tutdev.rtchartlib.utils.ViewPortHandler;

public abstract class Renderer {
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
