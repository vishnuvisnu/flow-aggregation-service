package com.netflix.demo.db;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import com.netflix.demo.model.Flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowsDB {

    private static FlowsDB db = null;
    public static FlowsDB getInstance() {
        if (db == null) {
            db = new FlowsDB();
        }
        return db;
    }
    private final Map<String, Integer> dict;
    private final Map<Integer, Map<Integer, Flow>> hours;
    private final TimeSeriesDB timeSeriesDB;
    private FlowsDB() {
        dict = new HashMap<>();
        hours = new HashMap<>();
        // Instantiating with 2 as flows currently have 2 metrics: bytesRx and bytesTx.
        timeSeriesDB = new TimeSeriesDB(2);
    }
    public List<Flow> get(final int hour) throws FlowsNotFoundException {
        if (!hours.containsKey(hour)) {
            throw new FlowsNotFoundException(String.format("Failed to find flows for hour %d", hour));
        }

        return new ArrayList<>(hours.get(hour).values());
    }
    public void put(final Flow flow) {
        final Map<Integer, Flow> hourFlows = hours.getOrDefault(flow.getHour(), new HashMap<>());
        final Integer dbKey = getDbKey(flow);
        final Flow existingFlow = hourFlows.get(dbKey);
        if (existingFlow != null) {
            existingFlow.setBytesRx(existingFlow.getBytesRx() + flow.getBytesRx());
            existingFlow.setBytesTx(existingFlow.getBytesTx() + flow.getBytesTx());

            hourFlows.put(dbKey, existingFlow);
        } else {
            hourFlows.put(dbKey, flow);
        }

        hours.put(flow.getHour(), hourFlows);
    }

    private int getDbKey(final Flow flow) {
        return flow.hashCode();
    }
}
