package com.netflix.demo.db;

import com.netflix.demo.exceptions.FlowsNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSeriesDB {

    private final List<int[]> metrics;

    private final int metricCount;

    private final Map<String, Integer> dict;

    private final Map<Integer, String> bitMaps;

    public TimeSeriesDB(final int metricCount) {
        metrics = new ArrayList<>();
        this.metricCount = metricCount;
        dict = new HashMap<>();
        bitMaps = new HashMap<>();
    }

    public synchronized void put(final String dimension, final int[] metrics) {
        for (final Map.Entry<Integer, String> bitMapEntry : bitMaps.entrySet()) {
            String bitMap = bitMapEntry.getValue();
            if (bitMapEntry.getKey().equals(dimension)) {
                bitMap += '1';
            } else {
                bitMap += '0';
            }
            bitMaps.put(bitMapEntry.getKey(), bitMap);
        }
        this.metrics.add(metrics);
    }

    private List<Integer> intersection(final List<String> bitmaps) throws FlowsNotFoundException {

        if (bitmaps.isEmpty()) {
            throw new FlowsNotFoundException("Flows not found");
        }
        final int bitmapLen = bitmaps.get(0).length();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < bitmapLen; i++) {
            boolean allOnes = true;
            for (final String bitmap : bitmaps) {
                if(bitmap.charAt(i) == 0) {
                    allOnes = false;
                }
            }

            if (allOnes) {
                indices.add(i);
            }
        }

        return indices;
    }

    public int[] sum(final List<String> dimensions) throws FlowsNotFoundException {
        List<Integer> indices = intersection(dimensions);
        int[] metrics = new int[metricCount];
        for (final int index : indices) {
            for (int i = 0; i < metricCount; i++) {
                metrics[i] += this.metrics.get(index)[i];
            }
        }

        return metrics;
    }
}
