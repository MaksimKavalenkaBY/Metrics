package by.training.data.memory;

import java.util.LinkedList;
import java.util.List;

import by.training.bean.Dashboard;
import by.training.bean.Widget;

public abstract class Memory {

    private static List<Dashboard> dashboards      = new LinkedList<>();
    private static List<Widget>    widgets         = new LinkedList<>();

    private static int             dashboardLastId = 1;
    private static int             widgetLastId    = 1;

    public static List<Dashboard> getDashboards() {
        return dashboards;
    }

    public static void setDashboards(final List<Dashboard> dashboards) {
        Memory.dashboards = dashboards;
    }

    public static List<Widget> getWidgets() {
        return widgets;
    }

    public static void setWidgets(final List<Widget> widgets) {
        Memory.widgets = widgets;
    }

    public static int getDashboardLastId() {
        return dashboardLastId;
    }

    public static void incDashboardLastId() {
        ++dashboardLastId;
    }

    public static int getWidgetLastId() {
        return widgetLastId;
    }

    public static void incWidgetLastId() {
        ++widgetLastId;
    }

}
