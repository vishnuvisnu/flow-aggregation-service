package com.netflix.demo.db;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import com.netflix.demo.model.Flow;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class FlowsDBTest {

    private static final String HOUR_DIMENSION_NAME = "hour";

    private static final String COMBO_DIMENSION_VALUE = "SrcAppDestAppVpcId";

    @Mock
    MetricsDB metricsDBMock;

    @Test
    void testGroupSuccess() throws FlowsNotFoundException {
        final FlowsDB flowsDB = new FlowsDB(metricsDBMock);
        final Map<String, int[]> metrics = new HashMap<>();
        metrics.put("srcApp,destApp,vpcId", new int[]{10, 20});
        Mockito.when(metricsDBMock.group(COMBO_DIMENSION_VALUE, HOUR_DIMENSION_NAME, "1"))
               .thenReturn(metrics);
        final List<Flow> flows = flowsDB.get(1);
        Assertions.assertThat(flows.size()).isEqualTo(1);
        final Flow flow = flows.get(0);
        Assertions.assertThat(flow.getSrcApp()).isEqualTo("srcApp");
        Assertions.assertThat(flow.getDestApp()).isEqualTo("destApp");
        Assertions.assertThat(flow.getVpcId()).isEqualTo("vpcId");
        Assertions.assertThat(flow.getHour()).isEqualTo(1);
        Assertions.assertThat(flow.getBytesRx()).isEqualTo(10);
        Assertions.assertThat(flow.getBytesTx()).isEqualTo(20);
    }

    @Test
    void testPutSuccess() {
        final FlowsDB flowsDB = new FlowsDB(metricsDBMock);
        final Flow flow = new Flow("srcApp", "destApp", "vpcId", 1, 10, 20);
        final Map<String, String> dimensions = new HashMap<>();
        dimensions.put(HOUR_DIMENSION_NAME, "1");
        dimensions.put(COMBO_DIMENSION_VALUE, "srcApp,destApp,vpcId");
        Mockito.doNothing().when(metricsDBMock).put(dimensions, new int[] {10, 20});

        flowsDB.put(flow);
    }
}
