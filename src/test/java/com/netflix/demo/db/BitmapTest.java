package com.netflix.demo.db;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Bitmap.class)
public class BitmapTest {

    @Test
    void testAddSuccess() {
        final Bitmap bm = new Bitmap();
        bm.add(10);
        Assertions.assertThat(bm.getRbm().first() == 10);
    }

    @Test
    void testAddMultipleSuccess() {
        final Bitmap bm1 = new Bitmap();
        final Bitmap bm2 = new Bitmap();
        for (int i = 0; i < 10; i++) {
            bm1.add(i);
        }

        for (int i = 0; i < 5; i++) {
            bm2.add(i);
        }

        int[] and = bm1.and(bm2);
        Assertions.assertThat(and.length).isEqualTo(5);
    }

    @Test
    void testAndSuccess() {
        final Bitmap bm1 = new Bitmap(RoaringBitmap.bitmapOf(0,1,2,3,4,5,6,7,8));
        final Bitmap bm2 = new Bitmap(RoaringBitmap.bitmapOf(5,6,7,8,9,10,11,12,13,14,15));

        int[] and = bm1.and(bm2);

        Assertions.assertThat(and.length).isEqualTo(4);
    }
}
