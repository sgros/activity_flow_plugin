// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

public class MeanCalculator
{
    private int n;
    private float sum;
    
    public void add(final float n) {
        this.sum += n;
        ++this.n;
        final int n2 = this.n;
        if (n2 == Integer.MAX_VALUE) {
            this.sum /= 2.0f;
            this.n = n2 / 2;
        }
    }
}
