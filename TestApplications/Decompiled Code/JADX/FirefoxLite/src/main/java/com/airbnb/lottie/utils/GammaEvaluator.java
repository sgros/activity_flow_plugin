package com.airbnb.lottie.utils;

public class GammaEvaluator {
    private static float OECF_sRGB(float f) {
        return f <= 0.0031308f ? f * 12.92f : (float) ((Math.pow((double) f, 0.4166666567325592d) * 1.0549999475479126d) - 0.054999999701976776d);
    }

    private static float EOCF_sRGB(float f) {
        return f <= 0.04045f ? f / 12.92f : (float) Math.pow((double) ((f + 0.055f) / 1.055f), 2.4000000953674316d);
    }

    public static int evaluate(float f, int i, int i2) {
        float f2 = ((float) ((i >> 24) & 255)) / 255.0f;
        float f3 = ((float) ((i >> 8) & 255)) / 255.0f;
        float f4 = ((float) (i & 255)) / 255.0f;
        float f5 = ((float) ((i2 >> 24) & 255)) / 255.0f;
        float f6 = ((float) ((i2 >> 16) & 255)) / 255.0f;
        float f7 = ((float) ((i2 >> 8) & 255)) / 255.0f;
        float f8 = ((float) (i2 & 255)) / 255.0f;
        float EOCF_sRGB = EOCF_sRGB(((float) ((i >> 16) & 255)) / 255.0f);
        f3 = EOCF_sRGB(f3);
        f4 = EOCF_sRGB(f4);
        f6 = EOCF_sRGB(f6);
        f3 += (EOCF_sRGB(f7) - f3) * f;
        f4 += f * (EOCF_sRGB(f8) - f4);
        f2 = (f2 + ((f5 - f2) * f)) * 255.0f;
        return (((Math.round(OECF_sRGB(EOCF_sRGB + ((f6 - EOCF_sRGB) * f)) * 255.0f) << 16) | (Math.round(f2) << 24)) | (Math.round(OECF_sRGB(f3) * 255.0f) << 8)) | Math.round(OECF_sRGB(f4) * 255.0f);
    }
}
