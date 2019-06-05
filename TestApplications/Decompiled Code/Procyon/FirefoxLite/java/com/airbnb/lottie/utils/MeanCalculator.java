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
        if (this.n == Integer.MAX_VALUE) {
            this.sum /= 2.0f;
            this.n /= 2;
        }
    }
}
