package by.training.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import by.training.bean.Widget;
import by.training.constants.ActionConstants;
import by.training.constants.PropertyConstants;
import by.training.dao.IDashboardDAO;
import by.training.dao.IWidgetDAO;
import by.training.database.structure.DashboardColumns;
import by.training.database.structure.DashboardWidgetColumns;
import by.training.database.structure.WidgetColumns;
import by.training.exception.IllegalDataException;
import by.training.factory.DashboardFactory;
import by.training.factory.WidgetFactory;
import by.training.options.MetricType;
import by.training.options.Period;
import by.training.options.RefreshInterval;

public abstract class EditDataAction {

    public static void edit(final HttpServletRequest request, final ServletResponse response)
            throws IllegalDataException {
        DataAction.checkError(request);
        ActionConstants action = (ActionConstants) request.getAttribute(PropertyConstants.ACTION);

        switch (action) {
            case ADD_DASHBOARD:
                DashboardEditData.addDashboard(request);
                break;
            case MODIFY_DASHBOARD:
                DashboardEditData.modifyDashboard(request);
                break;
            case DELETE_DASHBOARD:
                DashboardEditData.deleteDashboard(request);
                break;
            case ADD_WIDGET:
                WidgetEditData.addWidget(request);
                break;
            case MODIFY_WIDGET:
                WidgetEditData.modifyWidget(request);
                break;
            case DELETE_WIDGET:
                WidgetEditData.deleteWidget(request);
                break;
            case CHART:
                refreshChart(request, response);
                break;
            default:
                break;
        }
    }

    private static void refreshChart(final ServletRequest request, final ServletResponse response) {
        try (IWidgetDAO widgetDAO = WidgetFactory.getEditor()) {
            int id = Integer.valueOf(request.getParameter(WidgetColumns.ID.toString()));
            Widget widget = widgetDAO.getWidget(id);
            try (PrintWriter out = response.getWriter()) {
                if (widget.getPeriod() != Period.CUSTOM) {
                    out.print("http://localhost:8080/metrics/rest/metric/"
                            + widget.getMetricType().name() + "/"
                            + widget.getPeriod().getDate().getTime() + "_0");
                } else {
                    out.print("http://localhost:8080/metrics/rest/metric/"
                            + widget.getMetricType().name() + "/" + widget.getFromDate().getTime()
                            + "_" + widget.getToDate().getTime());
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class DashboardEditData {

        private static void addDashboard(final HttpServletRequest request) {
            try (IDashboardDAO dashboardDAO = DashboardFactory.getEditor()) {
                String name = (String) request.getAttribute(DashboardColumns.NAME.toString());
                String description = (String) request
                        .getAttribute(DashboardColumns.DESCRIPTION.toString());
                @SuppressWarnings("unchecked")
                List<Integer> widgetIds = (List<Integer>) request
                        .getAttribute(DashboardWidgetColumns.ID_WIDGET.toString());
                List<Widget> widgets = new ArrayList<>(widgetIds.size());

                try (IWidgetDAO widgetDAO = WidgetFactory.getEditor()) {
                    for (Integer id : widgetIds) {
                        widgets.add(widgetDAO.getWidget(id));
                    }
                }
                dashboardDAO.addDashboard(name, description, widgets);
            }
        }

        private static void modifyDashboard(final HttpServletRequest request) {
            try (IDashboardDAO dashboardDAO = DashboardFactory.getEditor()) {
                int id = (int) request.getAttribute(DashboardColumns.ID.toString());
                String name = (String) request.getAttribute(DashboardColumns.NAME.toString());
                String description = (String) request
                        .getAttribute(DashboardColumns.DESCRIPTION.toString());
                @SuppressWarnings("unchecked")
                List<Integer> widgetIds = (List<Integer>) request
                        .getAttribute(DashboardWidgetColumns.ID_WIDGET.toString());
                List<Widget> widgets = new ArrayList<>(widgetIds.size());

                try (IWidgetDAO widgetDAO = WidgetFactory.getEditor()) {
                    for (Integer idWidget : widgetIds) {
                        widgets.add(widgetDAO.getWidget(idWidget));
                    }
                }
                dashboardDAO.modifyDashboard(id, name, description, widgets);
            }
        }

        private static void deleteDashboard(final HttpServletRequest request) {
            try (IDashboardDAO dashboardDAO = DashboardFactory.getEditor()) {
                int id = (int) request.getAttribute(DashboardColumns.ID.toString());
                dashboardDAO.deleteDashboard(id);
            }
        }

    }

    private static class WidgetEditData {

        private static void addWidget(final HttpServletRequest request) {
            try (IWidgetDAO widgetDAO = WidgetFactory.getEditor()) {
                String name = (String) request.getAttribute(WidgetColumns.NAME.toString());
                MetricType metricType = (MetricType) request
                        .getAttribute(WidgetColumns.METRIC_TYPE.toString());
                RefreshInterval refreshInterval = (RefreshInterval) request
                        .getAttribute(WidgetColumns.REFRESH_INTERVAL.toString());
                Period period = (Period) request.getAttribute(WidgetColumns.PERIOD.toString());

                boolean isCustom = (boolean) request.getAttribute(Period.CUSTOM.toString());
                if (!isCustom) {
                    widgetDAO.addWidget(name, metricType, refreshInterval, period, null, null);
                } else {
                    Date from = (Date) request.getAttribute(WidgetColumns.FROM_DATE.toString());
                    Date to = (Date) request.getAttribute(WidgetColumns.TO_DATE.toString());
                    widgetDAO.addWidget(name, metricType, refreshInterval, period, from, to);
                }
            }
        }

        private static void modifyWidget(final HttpServletRequest request) {
            try (IWidgetDAO widgetDAO = WidgetFactory.getEditor()) {
                int id = (int) request.getAttribute(WidgetColumns.ID.toString());
                String name = (String) request.getAttribute(WidgetColumns.NAME.toString());
                MetricType metricType = (MetricType) request
                        .getAttribute(WidgetColumns.METRIC_TYPE.toString());
                RefreshInterval refreshInterval = (RefreshInterval) request
                        .getAttribute(WidgetColumns.REFRESH_INTERVAL.toString());
                Period period = (Period) request.getAttribute(WidgetColumns.PERIOD.toString());

                boolean isCustom = (boolean) request.getAttribute(Period.CUSTOM.toString());
                if (!isCustom) {
                    widgetDAO.modifyWidget(id, name, metricType, refreshInterval, period, null,
                            null);
                } else {
                    Date from = (Date) request.getAttribute(WidgetColumns.FROM_DATE.toString());
                    Date to = (Date) request.getAttribute(WidgetColumns.TO_DATE.toString());
                    widgetDAO.modifyWidget(id, name, metricType, refreshInterval, period, from, to);
                }
            }
        }

        private static void deleteWidget(final HttpServletRequest request) {
            try (IWidgetDAO widgetDAO = WidgetFactory.getEditor()) {
                int id = (int) request.getAttribute(WidgetColumns.ID.toString());
                widgetDAO.deleteWidget(id);
            }
        }

    }

}
