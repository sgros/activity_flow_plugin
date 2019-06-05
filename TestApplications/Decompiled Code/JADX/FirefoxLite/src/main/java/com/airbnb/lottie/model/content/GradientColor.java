package com.airbnb.lottie.model.content;

import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.utils.MiscUtils;

public class GradientColor {
    private final int[] colors;
    private final float[] positions;

    public GradientColor(float[] fArr, int[] iArr) {
        this.positions = fArr;
        this.colors = iArr;
    }

    public float[] getPositions() {
        return this.positions;
    }

    public int[] getColors() {
        return this.colors;
    }

    public int getSize() {
        return this.colors.length;
    }

    public void lerp(GradientColor gradientColor, GradientColor gradientColor2, float f) {
        if (gradientColor.colors.length == gradientColor2.colors.length) {
            for (int i = 0; i < gradientColor.colors.length; i++) {
                this.positions[i] = MiscUtils.lerp(gradientColor.positions[i], gradientColor2.positions[i], f);
                this.colors[i] = GammaEvaluator.evaluate(f, gradientColor.colors[i], gradientColor2.colors[i]);
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot interpolate between gradients. Lengths vary (");
        stringBuilder.append(gradientColor.colors.length);
        stringBuilder.append(" vs ");
        stringBuilder.append(gradientColor2.colors.length);
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
