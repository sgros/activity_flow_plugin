// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

public class SlidingPercentile
{
    private static final Comparator<Sample> INDEX_COMPARATOR;
    private static final int MAX_RECYCLED_SAMPLES = 5;
    private static final int SORT_ORDER_BY_INDEX = 1;
    private static final int SORT_ORDER_BY_VALUE = 0;
    private static final int SORT_ORDER_NONE = -1;
    private static final Comparator<Sample> VALUE_COMPARATOR;
    private int currentSortOrder;
    private final int maxWeight;
    private int nextSampleIndex;
    private int recycledSampleCount;
    private final Sample[] recycledSamples;
    private final ArrayList<Sample> samples;
    private int totalWeight;
    
    static {
        INDEX_COMPARATOR = (Comparator)_$$Lambda$SlidingPercentile$IHMSNRVWSvKImU2XQD2j4ISb4_U.INSTANCE;
        VALUE_COMPARATOR = (Comparator)_$$Lambda$SlidingPercentile$UufTq1Ma5g1qQu0Vqc6f2CE68bE.INSTANCE;
    }
    
    public SlidingPercentile(final int maxWeight) {
        this.maxWeight = maxWeight;
        this.recycledSamples = new Sample[5];
        this.samples = new ArrayList<Sample>();
        this.currentSortOrder = -1;
    }
    
    private void ensureSortedByIndex() {
        if (this.currentSortOrder != 1) {
            Collections.sort(this.samples, SlidingPercentile.INDEX_COMPARATOR);
            this.currentSortOrder = 1;
        }
    }
    
    private void ensureSortedByValue() {
        if (this.currentSortOrder != 0) {
            Collections.sort(this.samples, SlidingPercentile.VALUE_COMPARATOR);
            this.currentSortOrder = 0;
        }
    }
    
    public void addSample(int weight, final float value) {
        this.ensureSortedByIndex();
        int recycledSampleCount = this.recycledSampleCount;
        Sample e;
        if (recycledSampleCount > 0) {
            final Sample[] recycledSamples = this.recycledSamples;
            --recycledSampleCount;
            this.recycledSampleCount = recycledSampleCount;
            e = recycledSamples[recycledSampleCount];
        }
        else {
            e = new Sample();
        }
        e.index = this.nextSampleIndex++;
        e.weight = weight;
        e.value = value;
        this.samples.add(e);
        this.totalWeight += weight;
        while (true) {
            weight = this.totalWeight;
            final int maxWeight = this.maxWeight;
            if (weight <= maxWeight) {
                break;
            }
            final int n = weight - maxWeight;
            final Sample sample = this.samples.get(0);
            weight = sample.weight;
            if (weight <= n) {
                this.totalWeight -= weight;
                this.samples.remove(0);
                weight = this.recycledSampleCount;
                if (weight >= 5) {
                    continue;
                }
                final Sample[] recycledSamples2 = this.recycledSamples;
                this.recycledSampleCount = weight + 1;
                recycledSamples2[weight] = sample;
            }
            else {
                sample.weight = weight - n;
                this.totalWeight -= n;
            }
        }
    }
    
    public float getPercentile(float value) {
        this.ensureSortedByValue();
        final float n = (float)this.totalWeight;
        int i = 0;
        int n2 = 0;
        while (i < this.samples.size()) {
            final Sample sample = this.samples.get(i);
            n2 += sample.weight;
            if (n2 >= value * n) {
                return sample.value;
            }
            ++i;
        }
        if (this.samples.isEmpty()) {
            value = Float.NaN;
        }
        else {
            final ArrayList<Sample> samples = this.samples;
            value = samples.get(samples.size() - 1).value;
        }
        return value;
    }
    
    public void reset() {
        this.samples.clear();
        this.currentSortOrder = -1;
        this.nextSampleIndex = 0;
        this.totalWeight = 0;
    }
    
    private static class Sample
    {
        public int index;
        public float value;
        public int weight;
    }
}
