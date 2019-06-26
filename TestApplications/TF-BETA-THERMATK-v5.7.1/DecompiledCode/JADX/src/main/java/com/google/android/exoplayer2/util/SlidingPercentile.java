package com.google.android.exoplayer2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SlidingPercentile {
    private static final Comparator<Sample> INDEX_COMPARATOR = C0229-$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4-U.INSTANCE;
    private static final int MAX_RECYCLED_SAMPLES = 5;
    private static final int SORT_ORDER_BY_INDEX = 1;
    private static final int SORT_ORDER_BY_VALUE = 0;
    private static final int SORT_ORDER_NONE = -1;
    private static final Comparator<Sample> VALUE_COMPARATOR = C0230-$$Lambda$SlidingPercentile$UufTq1Ma5g1qQu0Vqc6f2CE68bE.INSTANCE;
    private int currentSortOrder = -1;
    private final int maxWeight;
    private int nextSampleIndex;
    private int recycledSampleCount;
    private final Sample[] recycledSamples = new Sample[5];
    private final ArrayList<Sample> samples = new ArrayList();
    private int totalWeight;

    private static class Sample {
        public int index;
        public float value;
        public int weight;

        private Sample() {
        }
    }

    public SlidingPercentile(int i) {
        this.maxWeight = i;
    }

    public void reset() {
        this.samples.clear();
        this.currentSortOrder = -1;
        this.nextSampleIndex = 0;
        this.totalWeight = 0;
    }

    public void addSample(int i, float f) {
        Sample sample;
        ensureSortedByIndex();
        int i2 = this.recycledSampleCount;
        if (i2 > 0) {
            Sample[] sampleArr = this.recycledSamples;
            i2--;
            this.recycledSampleCount = i2;
            sample = sampleArr[i2];
        } else {
            sample = new Sample();
        }
        int i3 = this.nextSampleIndex;
        this.nextSampleIndex = i3 + 1;
        sample.index = i3;
        sample.weight = i;
        sample.value = f;
        this.samples.add(sample);
        this.totalWeight += i;
        while (true) {
            i = this.totalWeight;
            int i4 = this.maxWeight;
            if (i > i4) {
                i -= i4;
                Sample sample2 = (Sample) this.samples.get(0);
                i3 = sample2.weight;
                if (i3 <= i) {
                    this.totalWeight -= i3;
                    this.samples.remove(0);
                    i = this.recycledSampleCount;
                    if (i < 5) {
                        Sample[] sampleArr2 = this.recycledSamples;
                        this.recycledSampleCount = i + 1;
                        sampleArr2[i] = sample2;
                    }
                } else {
                    sample2.weight = i3 - i;
                    this.totalWeight -= i;
                }
            } else {
                return;
            }
        }
    }

    public float getPercentile(float f) {
        ensureSortedByValue();
        f *= (float) this.totalWeight;
        int i = 0;
        for (int i2 = 0; i2 < this.samples.size(); i2++) {
            Sample sample = (Sample) this.samples.get(i2);
            i += sample.weight;
            if (((float) i) >= f) {
                return sample.value;
            }
        }
        if (this.samples.isEmpty()) {
            f = Float.NaN;
        } else {
            ArrayList arrayList = this.samples;
            f = ((Sample) arrayList.get(arrayList.size() - 1)).value;
        }
        return f;
    }

    private void ensureSortedByIndex() {
        if (this.currentSortOrder != 1) {
            Collections.sort(this.samples, INDEX_COMPARATOR);
            this.currentSortOrder = 1;
        }
    }

    private void ensureSortedByValue() {
        if (this.currentSortOrder != 0) {
            Collections.sort(this.samples, VALUE_COMPARATOR);
            this.currentSortOrder = 0;
        }
    }
}
