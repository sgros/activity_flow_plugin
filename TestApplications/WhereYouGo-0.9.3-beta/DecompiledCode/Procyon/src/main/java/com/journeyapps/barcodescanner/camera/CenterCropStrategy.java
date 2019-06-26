// 
// Decompiled by Procyon v0.5.34
// 

package com.journeyapps.barcodescanner.camera;

import android.util.Log;
import android.graphics.Rect;
import com.journeyapps.barcodescanner.Size;

public class CenterCropStrategy extends PreviewScalingStrategy
{
    private static final String TAG;
    
    static {
        TAG = CenterCropStrategy.class.getSimpleName();
    }
    
    @Override
    protected float getScore(final Size size, final Size size2) {
        float n;
        if (size.width <= 0 || size.height <= 0) {
            n = 0.0f;
        }
        else {
            final Size scaleCrop = size.scaleCrop(size2);
            float n2 = scaleCrop.width * 1.0f / size.width;
            if (n2 > 1.0f) {
                n2 = (float)Math.pow(1.0f / n2, 1.1);
            }
            final float n3 = scaleCrop.width * 1.0f / size2.width + scaleCrop.height * 1.0f / size2.height;
            n = n2 * (1.0f / n3 / n3);
        }
        return n;
    }
    
    @Override
    public Rect scalePreview(final Size obj, final Size obj2) {
        final Size scaleCrop = obj.scaleCrop(obj2);
        Log.i(CenterCropStrategy.TAG, "Preview: " + obj + "; Scaled: " + scaleCrop + "; Want: " + obj2);
        final int n = (scaleCrop.width - obj2.width) / 2;
        final int n2 = (scaleCrop.height - obj2.height) / 2;
        return new Rect(-n, -n2, scaleCrop.width - n, scaleCrop.height - n2);
    }
}
