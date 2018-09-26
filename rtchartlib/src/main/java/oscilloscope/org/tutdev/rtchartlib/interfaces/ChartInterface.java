package oscilloscope.org.tutdev.rtchartlib.interfaces;

import oscilloscope.org.tutdev.rtchartlib.data.DataSet;
import oscilloscope.org.tutdev.rtchartlib.utils.Transformer;

public interface ChartInterface {
    DataSet getData();

    Transformer getTransformer();
}
