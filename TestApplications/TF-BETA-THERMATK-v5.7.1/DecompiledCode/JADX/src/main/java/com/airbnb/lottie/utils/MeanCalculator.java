package com.airbnb.lottie.utils;

public class MeanCalculator {
    /* renamed from: n */
    private int f13n;
    private float sum;

    public void add(float f) {
        this.sum += f;
        this.f13n++;
        int i = this.f13n;
        if (i == Integer.MAX_VALUE) {
            this.sum /= 2.0f;
            this.f13n = i / 2;
        }
    }
}
