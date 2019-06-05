// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.content;

import com.airbnb.lottie.utils.GammaEvaluator;
import com.airbnb.lottie.utils.MiscUtils;

public class GradientColor
{
    private final int[] colors;
    private final float[] positions;
    
    public GradientColor(final float[] positions, final int[] colors) {
        this.positions = positions;
        this.colors = colors;
    }
    
    public int[] getColors() {
        return this.colors;
    }
    
    public float[] getPositions() {
        return this.positions;
    }
    
    public int getSize() {
        return this.colors.length;
    }
    
    public void lerp(final GradientColor gradientColor, final GradientColor gradientColor2, final float n) {
        if (gradientColor.colors.length == gradientColor2.colors.length) {
            for (int i = 0; i < gradientColor.colors.length; ++i) {
                this.positions[i] = MiscUtils.lerp(gradientColor.positions[i], gradientColor2.positions[i], n);
                this.colors[i] = GammaEvaluator.evaluate(n, gradientColor.colors[i], gradientColor2.colors[i]);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot interpolate between gradients. Lengths vary (");
        sb.append(gradientColor.colors.length);
        sb.append(" vs ");
        sb.append(gradientColor2.colors.length);
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
}
