package com.fiap.alegorflix.utils;

import com.fiap.alegorflix.metrics.entity.Metric;

public abstract class MetricHelper {
    public static Metric createMetric(){
        return new Metric(10,3,2);
    }
}
