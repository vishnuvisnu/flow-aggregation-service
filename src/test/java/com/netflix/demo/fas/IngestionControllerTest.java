package com.netflix.demo.fas;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import com.netflix.demo.model.Flow;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngestionControllerTest {

    @Test
    void testPostSuccess1() throws FlowsNotFoundException {
        final IngestionController ingestion = new IngestionController();
        final List<Flow> flows = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            flows.add(new Flow("srcApp1", "destApp1", "vpcId1", 1, 10, 20));
            flows.add(new Flow("srcApp2", "destApp2", "vpcId2", 2, 10, 20));
        }
        ingestion.flows(flows);
        final QueryController query = new QueryController();
        final Flow flow1 = new Flow("srcApp1", "destApp1", "vpcId1", 1, 100, 200);
        Assertions.assertThat(query.flows(1))
                  .hasSameElementsAs(Arrays.asList(flow1));
        final Flow flow2 = new Flow("srcApp2", "destApp2", "vpcId2", 2, 100, 200);
        Assertions.assertThat(query.flows(2))
                  .hasSameElementsAs(Arrays.asList(flow2));
    }

    @Test
    void testPostSuccess2() throws FlowsNotFoundException {
        final IngestionController ingestion = new IngestionController();
        final List<Flow> flows = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            flows.add(new Flow("srcApp3", "destApp3", "vpcId3", 3, 10, 20));
            flows.add(new Flow("srcApp4", "destApp4", "vpcId4", 3, 10, 20));
        }
        ingestion.flows(flows);

        final QueryController query = new QueryController();
        final List<Flow> flows1 = new ArrayList<>();
        flows1.add(new Flow("srcApp3", "destApp3", "vpcId3", 3, 100, 200));
        flows1.add(new Flow("srcApp4", "destApp4", "vpcId4", 3, 100, 200));
        Assertions.assertThat(query.flows(3)).hasSameElementsAs(flows1);
    }

    @Test
    void testPostSuccess3() throws FlowsNotFoundException {
        final IngestionController ingestion = new IngestionController();
        final List<Flow> flows = new ArrayList<>();
        for (int i = 100; i < 10000; i++) {
            flows.add(new Flow("srcApp" + i, "destApp" + i, "vpcId" + i, 4, 10, 20));
        }
        ingestion.flows(flows);

        final List<Flow> expected = new ArrayList<>();
        for (int i = 100; i < 10000; i++) {
            expected.add(new Flow("srcApp" + i, "destApp" + i, "vpcId" + i, 4, 10, 20));
        }
        final QueryController query = new QueryController();

        Assertions.assertThat(flows.size()).isEqualTo(expected.size());
        Assertions.assertThat(query.flows(4)).hasSameElementsAs(expected);
    }

    @Test
    void testPostSuccess4() throws FlowsNotFoundException {
        final IngestionController ingestion = new IngestionController();
        final List<Flow> flows = new ArrayList<>();
        for (int i = 10000; i < 20000; i++) {
            flows.add(new Flow("srcApp10000", "destApp10000", "vpcId10000", 5, 10, 20));
        }
        ingestion.flows(flows);

        final List<Flow> expected = new ArrayList<>();
        expected.add(new Flow("srcApp10000", "destApp10000", "vpcId10000", 5, 10 * 10000, 20 * 10000));

        final QueryController query = new QueryController();

        Assertions.assertThat(expected.size()).isEqualTo(1);
        Assertions.assertThat(query.flows(5)).hasSameElementsAs(expected);
    }

    @Test
    void testPostSuccess5() throws FlowsNotFoundException {
        final IngestionController ingestion = new IngestionController();
        final List<Flow> flows = new ArrayList<>();
        for (int i = 20000; i < 30000; i++) {
            flows.add(new Flow("srcApp30000", "destApp30000", "vpcId30000", 6, 10, 20));
            flows.add(new Flow("srcApp" + i, "destApp" + i, "vpcId" + i, 7, 10, 20));
        }
        ingestion.flows(flows);

        final List<Flow> expected6 = new ArrayList<>();
        expected6.add(new Flow("srcApp30000", "destApp30000", "vpcId30000", 6, 10 * 10000, 20 * 10000));

        final List<Flow> expected7 = new ArrayList<>();
        for (int i = 20000; i < 30000; i++) {
            expected7.add(new Flow("srcApp" + i, "destApp" + i, "vpcId" + i, 7, 10, 20));
        }

        final QueryController query = new QueryController();

        Assertions.assertThat(expected6.size()).isEqualTo(1);
        Assertions.assertThat(query.flows(6)).hasSameElementsAs(expected6);

        Assertions.assertThat(expected7.size()).isEqualTo(10000);
        Assertions.assertThat(query.flows(7)).hasSameElementsAs(expected7);
    }
}
