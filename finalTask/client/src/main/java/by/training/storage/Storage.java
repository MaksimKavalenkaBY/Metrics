package by.training.storage;

import static by.training.constants.ResponseStatus.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import by.training.bean.concurrent.ConcurrentBoundedSkipListSet;
import by.training.bean.element.ParametersElement;
import by.training.bean.metric.Metric;
import by.training.constants.ResponseStatus;
import by.training.dao.TransportDAO;
import by.training.options.MetricType;
import by.training.options.RefreshInterval;
import by.training.options.Transport;

public class Storage implements Runnable {

    private static final int                     MAX_COUNT = 604800;

    private final ScheduledExecutorService       executor  = Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?>                   future;
    private MetricType                           metricType;
    private TransportDAO                         dao;

    private ConcurrentBoundedSkipListSet<Metric> storage;

    public Storage(final MetricType metricType) {
        this.metricType = metricType;
        storage = new ConcurrentBoundedSkipListSet<>(MAX_COUNT);
        future = executor.scheduleWithFixedDelay(this, 0, RefreshInterval.SECOND_5.getTime(),
                TimeUnit.SECONDS);
    }

    public Metric getLast() {
        if (storage.isEmpty()) {
            synchronized (this) {
                storage.add(dao.getLast(metricType));
            }
        }
        return storage.last();
    }

    public Set<Metric> getList(final Date from) {
        checkAndLoad(from);
        return storage.tailSet(new Metric(from, 0), true);
    }

    public Set<Metric> getList(final Date from, final Date to) {
        checkAndLoad(from, to);
        return storage.subSet(new Metric(from, 0), true, new Metric(to, 0), true);
    }

    public void createTransport(final Transport transport, final ParametersElement parameters) {
        synchronized (this) {
            dao = transport.createDAO(parameters);
        }
    }

    public void setTransport(final Transport transport, final ParametersElement parameters) {
        synchronized (this) {
            dao.close();
            dao = transport.createDAO(parameters);
        }
    }

    public void setParameters(final ParametersElement parameters) {
        synchronized (this) {
            dao.setParameters(parameters);
        }
    }

    public void setMetricType(final MetricType metricType) {
        synchronized (this) {
            storage.clear();
            this.metricType = metricType;
        }
    }

    private void checkAndLoad(final Date from) {
        if (dao.getStatus() != NOT_FOUND) {
            if (storage.isEmpty()) {
                getLast();
            } else if (storage.first().getDate().after(from)) {
                storage.addAll(loadBefore(from));
            } else {
                storage.addAll(loadAfter());
            }
        }
    }

    private void checkAndLoad(final Date from, final Date to) {
        if (dao.getStatus() != NOT_FOUND) {
            if (storage.isEmpty()) {
                storage.addAll(loadBetween(from, to));
            } else if (storage.first().getDate().after(from)) {
                if (storage.first().getDate().compareTo(to) <= 0) {
                    storage.addAll(loadBefore(from));
                } else {
                    storage.clear();
                    storage.addAll(loadBetween(from, to));
                }
            } else if (storage.last().getDate().before(to)) {
                if (storage.last().getDate().compareTo(from) >= 0) {
                    storage.addAll(loadAfter());
                } else {
                    storage.clear();
                    storage.addAll(loadBetween(from, to));
                }
            }
        }
    }

    private List<Metric> loadBefore(final Date from) {
        synchronized (this) {
            return dao.getList(metricType, from, storage.first().getDate());
        }
    }

    private List<Metric> loadAfter() {
        synchronized (this) {
            return dao.getList(metricType, storage.last().getDate(), new Date(0));
        }
    }

    private List<Metric> loadBetween(final Date from, final Date to) {
        synchronized (this) {
            return dao.getList(metricType, from, to);
        }
    }

    public ResponseStatus getStatus() {
        return dao.getStatus();
    }

    public void deactivate() {
        future.cancel(false);
        synchronized (this) {
            dao.close();
        }
    }

    @Override
    public void run() {
        checkAndLoad(getLast().getDate());
    }

}
