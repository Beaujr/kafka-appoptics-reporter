package com.appoptics.integrations.kafka.broker;


import java.util.HashMap;
import java.util.Map;

/**
 * A class for representing a gauge reading that might come from multiple samples
 * <p/>
 * See http://dev.librato.com/v1/post/metrics for why some fields are optional
 */
public class MultiSampleGaugeMeasurement implements Measurement {
    private final String name;
    private final Long count;
    private final Number sum;
    private final Number max;
    private final Number min;
    private final Number sumSquares;

    public MultiSampleGaugeMeasurement(String name,
                                       Long count,
                                       Number sum,
                                       Number max,
                                       Number min,
                                       Number sumSquares) {
        try {
            if (count == null || count == 0) {
                throw new IllegalArgumentException("The Librato API requires the count to be > 0 for complex metrics. See http://dev.librato.com/v1/post/metrics");
            }
            this.name = KafkaMetricsBatch.Preconditions.checkNotNull(name);
            this.count = count;
            this.sum = KafkaMetricsBatch.Preconditions.checkNumeric(sum);
            this.max = KafkaMetricsBatch.Preconditions.checkNumeric(max);
            this.min = KafkaMetricsBatch.Preconditions.checkNumeric(min);
            this.sumSquares = KafkaMetricsBatch.Preconditions.checkNumeric(sumSquares);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid multi-sample gauge measurement name=" + name, e);
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, Number> toMap() {
        final Map<String, Number> result = new HashMap<String, Number>(5);
        result.put("count", count);
        result.put("sum", sum);
        if (max != null) {
            result.put("max", max);
        }
        if (min != null) {
            result.put("min", min);
        }
        if (sumSquares != null) {
            result.put("sum_squares", sumSquares);
        }
        return result;
    }
}

