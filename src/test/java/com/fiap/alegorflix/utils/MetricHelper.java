package com.fiap.alegorflix.utils;

import com.fiap.alegorflix.metrics.entity.Metric;
import com.fiap.alegorflix.movie.entity.Movie;

import java.time.LocalDate;

public abstract class MetricHelper {
    public static Metric createMetric(){
        return new Metric(10,3,2);
    }
}
