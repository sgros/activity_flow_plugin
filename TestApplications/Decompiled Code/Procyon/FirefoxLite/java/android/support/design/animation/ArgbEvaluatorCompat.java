// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.animation.TypeEvaluator;

public class ArgbEvaluatorCompat implements TypeEvaluator<Integer>
{
    private static final ArgbEvaluatorCompat instance;
    
    static {
        instance = new ArgbEvaluatorCompat();
    }
    
    public static ArgbEvaluatorCompat getInstance() {
        return ArgbEvaluatorCompat.instance;
    }
    
    public Integer evaluate(final float n, final Integer n2, final Integer n3) {
        final int intValue = n2;
        final float n4 = (intValue >> 24 & 0xFF) / 255.0f;
        final float n5 = (intValue >> 16 & 0xFF) / 255.0f;
        final float n6 = (intValue >> 8 & 0xFF) / 255.0f;
        final float n7 = (intValue & 0xFF) / 255.0f;
        final int intValue2 = n3;
        final float n8 = (intValue2 >> 24 & 0xFF) / 255.0f;
        final float n9 = (intValue2 >> 16 & 0xFF) / 255.0f;
        final float n10 = (intValue2 >> 8 & 0xFF) / 255.0f;
        final float n11 = (intValue2 & 0xFF) / 255.0f;
        final float n12 = (float)Math.pow(n5, 2.2);
        final float n13 = (float)Math.pow(n6, 2.2);
        final float n14 = (float)Math.pow(n7, 2.2);
        return Math.round((float)Math.pow(n12 + ((float)Math.pow(n9, 2.2) - n12) * n, 0.45454545454545453) * 255.0f) << 16 | Math.round((n4 + (n8 - n4) * n) * 255.0f) << 24 | Math.round((float)Math.pow(n13 + ((float)Math.pow(n10, 2.2) - n13) * n, 0.45454545454545453) * 255.0f) << 8 | Math.round((float)Math.pow(n14 + n * ((float)Math.pow(n11, 2.2) - n14), 0.45454545454545453) * 255.0f);
    }
}
