package com.netflix.demo.db;

import com.google.common.annotations.VisibleForTesting;
import lombok.Getter;
import org.roaringbitmap.RoaringBitmap;

public class Bitmap {

    @Getter
    private final RoaringBitmap rbm;

    public Bitmap() {
        rbm = new RoaringBitmap();
    }

    @VisibleForTesting
    public Bitmap(final RoaringBitmap rbm) {
        this.rbm = rbm;
    }

    public void add(final int num) {
        rbm.add(num);
    }

    public int[] and(final Bitmap bm) {
        RoaringBitmap andMap = RoaringBitmap.and(bm.getRbm(), this.rbm);
        return andMap.toArray();
    }
}
