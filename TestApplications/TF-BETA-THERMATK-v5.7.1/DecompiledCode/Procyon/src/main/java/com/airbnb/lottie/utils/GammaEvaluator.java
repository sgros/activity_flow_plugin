// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

public class GammaEvaluator
{
    private static float EOCF_sRGB(float n) {
        if (n <= 0.04045f) {
            n /= 12.92f;
        }
        else {
            n = (float)Math.pow((n + 0.055f) / 1.055f, 2.4000000953674316);
        }
        return n;
    }
    
    private static float OECF_sRGB(float n) {
        if (n <= 0.0031308f) {
            n *= 12.92f;
        }
        else {
            n = (float)(Math.pow(n, 0.4166666567325592) * 1.0549999475479126 - 0.054999999701976776);
        }
        return n;
    }
    
    public static int evaluate(final float n, int round, final int n2) {
        final float n3 = (round >> 24 & 0xFF) / 255.0f;
        final float n4 = (round >> 16 & 0xFF) / 255.0f;
        final float n5 = (round >> 8 & 0xFF) / 255.0f;
        final float n6 = (round & 0xFF) / 255.0f;
        final float n7 = (n2 >> 24 & 0xFF) / 255.0f;
        final float n8 = (n2 >> 16 & 0xFF) / 255.0f;
        final float n9 = (n2 >> 8 & 0xFF) / 255.0f;
        final float n10 = (n2 & 0xFF) / 255.0f;
        final float eocf_sRGB = EOCF_sRGB(n4);
        final float eocf_sRGB2 = EOCF_sRGB(n5);
        final float eocf_sRGB3 = EOCF_sRGB(n6);
        final float eocf_sRGB4 = EOCF_sRGB(n8);
        final float eocf_sRGB5 = EOCF_sRGB(n9);
        final float eocf_sRGB6 = EOCF_sRGB(n10);
        final float oecf_sRGB = OECF_sRGB(eocf_sRGB + (eocf_sRGB4 - eocf_sRGB) * n);
        final float oecf_sRGB2 = OECF_sRGB(eocf_sRGB2 + (eocf_sRGB5 - eocf_sRGB2) * n);
        final float oecf_sRGB3 = OECF_sRGB(eocf_sRGB3 + n * (eocf_sRGB6 - eocf_sRGB3));
        round = Math.round((n3 + (n7 - n3) * n) * 255.0f);
        return Math.round(oecf_sRGB * 255.0f) << 16 | round << 24 | Math.round(oecf_sRGB2 * 255.0f) << 8 | Math.round(oecf_sRGB3 * 255.0f);
    }
}
