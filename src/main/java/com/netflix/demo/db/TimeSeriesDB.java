package com.netflix.demo.db;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSeriesDB {
    private Logger logger = LoggerFactory.getLogger(TimeSeriesDB.class);
    @Getter
    private final List<int[]> metrics;

    private final int metricCount;

    @Getter
    private final Map<String, Map<String, Bitmap>> bitMaps;

    public TimeSeriesDB(final int metricCount) {
        metrics = new ArrayList<>();
        this.metricCount = metricCount;
        bitMaps = new HashMap<>();
    }

    public synchronized void put(final Map<String, String> dimensions, final int[] values) {
        for (final Map.Entry<String, String> dimension : dimensions.entrySet()) {
            final Map<String, Bitmap> bitmapMap = bitMaps.getOrDefault(dimension.getKey(), new HashMap<>());

            final Bitmap bm = bitmapMap.getOrDefault(dimension.getValue(), new Bitmap());
            bm.add(metrics.size());

            bitmapMap.put(dimension.getValue(), bm);
            bitMaps.put(dimension.getKey(), bitmapMap);
        }
        this.metrics.add(values);
    }

    public Map<String, int[]> group(final String groupByDim, final String filterByDim,
                                     final String filterByVal) throws FlowsNotFoundException {
        if (!bitMaps.containsKey(groupByDim) || !bitMaps.containsKey(filterByDim) ||
            !bitMaps.get(filterByDim).containsKey(filterByVal)) {

            throw new FlowsNotFoundException("Flows not found");
        }
        final Bitmap filterMap = bitMaps.get(filterByDim).get(filterByVal);
        final Map<String, Bitmap> groupByCol = bitMaps.get(groupByDim);

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
