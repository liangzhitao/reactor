package org.reactor.sonar.data;

import static com.google.common.base.Objects.firstNonNull;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.Resource;

public class MetricWithValueResource {

    private static final double DEFAULT_VALUE = 0d;
    private static final String DEFAULT_FORMATTED_VALUE = "";

    private final Metric metric;
    private final Resource resource;

    public MetricWithValueResource(Metric metric, Resource resource) {
        this.metric = metric;
        this.resource = resource;
    }

    public String getMetricDesription() {
        return metric.getDescription();
    }

    public String getFormattedMetricValue() {
        return resource.getMeasureFormattedValue(metric.getKey(), DEFAULT_FORMATTED_VALUE);
    }

    public double getMetricValue() {
        return firstNonNull(resource.getMeasureValue(metric.getKey()), DEFAULT_VALUE);
    }
}
