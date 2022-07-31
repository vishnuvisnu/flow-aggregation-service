package com.netflix.demo.db;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MetricsDBTest {

   private static final Logger LOG = LoggerFactory.getLogger(MetricsDBTest.class);

   void addDimensions(final String d1Val, final String d2Val, final int m1,
                      final int m2, final MetricsDB metricsDB) {
      final Map<String, String> dims = new HashMap<>();
      dims.put("dim1", d1Val);
      dims.put("dim2", d2Val);

      final int[] metrics = new int[] {m1, m2};
      metricsDB.put(dims, metrics);
   }

   MetricsDB fillDB() {
      final MetricsDB metricsDB = new MetricsDB(2);

      for (int i = 0; i < 5; i++) {
         addDimensions("dim1Val1", "dim2Val1", 100, 200, metricsDB);
         addDimensions("dim1Val1", "dim2Val2", 150, 120, metricsDB);
         addDimensions("dim1Val2", "dim2Val2", 300, 400, metricsDB);
         addDimensions("dim1Val3", "dim2Val2", 500, 600, metricsDB);
      }

      return metricsDB;
   }

   @Test
   void testGroupBySuccess() throws FlowsNotFoundException {
      LOG.trace("testGroupBySuccess");
      final MetricsDB metricsDB = fillDB();
      final Map<String, int[]> grouped = metricsDB.group("dim1", "dim2", "dim2Val2");
      Assertions.assertThat(grouped.get("dim1Val1")).isEqualTo(new int[]{750, 600});
      Assertions.assertThat(grouped.get("dim1Val2")).isEqualTo(new int[]{1500, 2000});
      Assertions.assertThat(grouped.get("dim1Val3")).isEqualTo(new int[]{2500, 3000});
   }
}
