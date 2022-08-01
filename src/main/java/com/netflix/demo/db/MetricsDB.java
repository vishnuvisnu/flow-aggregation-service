package com.netflix.demo.db;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsDB {
    
    private Logger logger = LoggerFactory.getLogger(MetricsDB.class);
    @Getter
    private final List<int[]> metrics;

    private final int metricCount;

    @Getter
    private final Map<String, Map<String, Bitmap>> bitMaps;

    public MetricsDB(final int metricCount) {
        metrics = new ArrayList<>();
        this.metricCount = metricCount;
        bitMaps = new HashMap<>();
    }

    public synchronized void put(final Map<String, String> dimensions, final int[] values) {
        Preconditions.checkState(!dimensions.isEmpty());
        Preconditions.checkState(values.length != 0);

        for (final Map.Entry<String, String> dimension : dimensions.entrySet()) {
            final Map<String, Bitmap> bitmapMap = bitMaps.getOrDefault(dimension.getKey(), new HashMap<>());

            final Bitmap bm = bitmapMap.getOrDefault(dimension.getValue(), new Bitmap());
            bm.add(metrics.size());

            bitmapMap.put(dimension.getValue(), bm);
            bitMaps.put(dimension.getKey(), bitmapMap);
        }
        this.metrics.add(values);
    }

    /**
     * This function is used by read api to aggregate by one dimension and group by another dimension.
     * @param aggregateByDim dimension to aggregate by, for example, flow_key combination of source_app, dest_app and vpc_id.
     * @param groupByDim dimension to group by, for example, hour.
     * @param groupByVal dimension value to group by, for example, hour 1.
     * @return Map of flow_key and aggregated metric.
     */
    public Map<String, int[]> group(final String aggregateByDim, final String groupByDim,
                                     final String groupByVal) {
        Preconditions.checkState(bitMaps.containsKey(aggregateByDim));
        Preconditions.checkState(bitMaps.containsKey(groupByDim));
        Preconditions.checkState(bitMaps.get(groupByDim).containsKey(groupByVal));

        final Bitmap filterMap = bitMaps.get(groupByDim).get(groupByVal);
        final Map<String, Bitmap> groupByCol = bitMaps.get(aggregateByDim);

        final Map<String, int[]> grouped = new HashMap<>();
        for (final Map.Entry<String, Bitmap> row : groupByCol.entrySet()) {
            final int[] indices = row.getValue().and(filterMap);
            if (indices.length != 0) {
                final int[] metricValues = new int[metricCount];
                for (final int index : indices) {
                    for (int i = 0; i < metricCount; i++) {
                        metricValues[i] += this.metrics.get(index)[i];
                    }
                }
                grouped.put(row.getKey(), metricValues);
            }
        }

        return grouped;
    }
}
