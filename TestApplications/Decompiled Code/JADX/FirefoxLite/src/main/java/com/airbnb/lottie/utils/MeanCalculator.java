package com.airbnb.lottie.utils;

public class MeanCalculator {
    /* renamed from: n */
    private int f35n;
    private float sum;

    public void add(float f) {
        this.sum += f;
        this.f35n++;
        if (this.f35n == Integer.MAX_VALUE) {
            this.sum /= 2.0f;
            this.f35n /= 2;
        }
    }
}
