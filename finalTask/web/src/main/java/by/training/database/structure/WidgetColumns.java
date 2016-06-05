package by.training.database.structure;

public enum WidgetColumns {

    ID("Id"), NAME("Name"), METRIC_TYPE("MetricType"), PERIOD("Period"), REFRESH_INTERVAL(
            "RefreshInterval"), START("Start"), END("End");

    private String column;

    private WidgetColumns(final String column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return column;
    }

}
