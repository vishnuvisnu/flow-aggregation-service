package com.netflix.demo.db;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.netflix.demo.exceptions.FlowsNotFoundException;
import com.netflix.demo.model.Flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowsDB {

    private static final String HOUR_DIMENSION_NAME = "hour";

    private static final String COMBO_DIMENSION_VALUE = "SrcAppDestAppVpcId";
    private static FlowsDB db = null;
    public static FlowsDB getInstance() {
        if (db == null) {
            db = new FlowsDB();
        }
        return db;
    }
    private final MetricsDB metricsDB;
    private FlowsDB() {
        // Instantiating with 2 as flows currently have 2 metrics: bytesRx and bytesTx.
        metricsDB = new MetricsDB(2);
    }

    @VisibleForTesting
    protected FlowsDB(final MetricsDB metricsDB) {
        this.metricsDB = metricsDB;
    }
    public List<Flow> get(final int hour) throws FlowsNotFoundException {
        final List<Flow> flows = new ArrayList<>();
        final Map<String, int[]> metrics =  metricsDB.group(COMBO_DIMENSION_VALUE, HOUR_DIMENSION_NAME, Integer.toString(hour));
        for (final Map.Entry<String, int[]> metric : metrics.entrySet()) {
            final int[] values = metric.getValue();
            final String flowKey = metric.getKey();
            final String[] flowAttrs = flowKey.split(",");

            final Flow flow = new Flow(flowAttrs[0], flowAttrs[1], flowAttrs[2], hour, values[0], values[1]);
            flows.add(flow);
        }
        return flows;
    }
    public synchronized void put(final Flow flow) {
        Preconditions.checkNotNull(flow);

        final Map<String, String> dimensions = new HashMap<>();
        dimensions.put(HOUR_DIMENSION_NAME, Integer.toString(flow.getHour()));
        final String flowKey = String.join(",", flow.getSrcApp(), flow.getDestApp(), flow.getVpcId());
        dimensions.put(COMBO_DIMENSION_VALUE, flowKey);

        metricsDB.put(dimensions, new int[] {flow.getBytesRx(), flow.getBytesTx()});
    }
}
