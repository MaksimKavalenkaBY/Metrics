package by.training.constants;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;

import by.training.bean.element.OptionsElement;
import by.training.options.MetricType;
import by.training.options.Period;
import by.training.options.RefreshInterval;
import by.training.options.Transport;

public abstract class AppDefaultConstants {

    public static final Dimension    DIMENSION             = new Dimension(100, 53);

    public static final DataFlavor   DATA_FLAVOR           = new DataFlavor(OptionsElement.class,
            OptionsElement.class.getSimpleName());

    public static final List<String> TYPE_METRIC_LIST      = new ArrayList<>();
    public static final List<String> TRANSPORT_LIST        = new ArrayList<>();
    public static final List<String> PERIOD_LIST           = new ArrayList<>();
    public static final List<String> REFRESH_INTERVAL_LIST = new ArrayList<>();

    static {
        for (MetricType typeMetric : MetricType.values()) {
            TYPE_METRIC_LIST.add(typeMetric.getTitle());
        }

        for (Transport transport : Transport.values()) {
            TRANSPORT_LIST.add(transport.getName());
        }

        for (Period period : Period.values()) {
            PERIOD_LIST.add(period.getName());
        }

        for (RefreshInterval refreshInterval : RefreshInterval.values()) {
            REFRESH_INTERVAL_LIST.add(refreshInterval.getName());
        }
    }

}
