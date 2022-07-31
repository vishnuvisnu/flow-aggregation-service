package com.netflix.demo.db;

import com.netflix.demo.exceptions.FlowsNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TimeSeriesDBTest {

   private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesDBTest.class);

   void addDimensions(final String d1Val, final String d2Val, final int m1,
                      final int m2, final TimeSeriesDB timeSeriesDB) {
      final Map<String, String> dims = new HashMap<>();
      dims.put("dim1", d1Val);
      dims.put("dim2", d2Val);

      final int[] metrics = new int[] {m1, m2};
      timeSeriesDB.put(dims, metrics);
   }

   TimeSeriesDB fillDB() {
      final TimeSeriesDB timeSeriesDB = new TimeSeriesDB(2);
      for (int i = 0; i < 10; i++) {
         addDimensions("100", "600", 100, 200, timeSeriesDB);
      }

      for (int i = 0; i < 5; i++) {
         addDimensions("800", "600", 150, 120, timeSeriesDB);
      }

      for (int i = 0; i < 5; i++) {
         addDimensions("500", "600", 300, 400, timeSeriesDB);
      }

      for (int i = 0; i < 5; i++) {
         addDimensions("500", "700", 500, 600, timeSeriesDB);
      }

      return timeSeriesDB;
   }

   @Test
   void testGroupBySuccess() throws FlowsNotFoundException {
      LOG.trace("testGroupBySuccess");
      final TimeSeriesDB timeSeriesDB = fillDB();
      final Map<String, int[]> grouped = timeSeriesDB.group("dim1", "dim2", "600");
      Assertions.assertThat(grouped.get("500")).isEqualTo(new int[]{1500, 2000});
      Assertions.assertThat(grouped.get("100")).isEqualTo(new int[]{1000, 2000});
      Assertions.assertThat(grouped.get("800")).isEqualTo(new int[]{750, 600});
   }
}
