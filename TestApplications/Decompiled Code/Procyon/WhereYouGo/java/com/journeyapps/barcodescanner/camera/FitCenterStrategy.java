// 
// Decompiled by Procyon v0.5.34
// 

package com.journeyapps.barcodescanner.camera;

import android.util.Log;
import android.graphics.Rect;
import com.journeyapps.barcodescanner.Size;

public class FitCenterStrategy extends PreviewScalingStrategy
{
    private static final String TAG;
    
    static {
        TAG = FitCenterStrategy.class.getSimpleName();
    }
    
    @Override
    protected float getScore(final Size size, final Size size2) {
        float n;
        if (size.width <= 0 || size.height <= 0) {
            n = 0.0f;
        }
        else {
            final Size scaleFit = size.scaleFit(size2);
            float n2 = scaleFit.width * 1.0f / size.width;
            if (n2 > 1.0f) {
                n2 = (float)Math.pow(1.0f / n2, 1.1);
            }
            final float n3 = size2.width * 1.0f / scaleFit.width * (size2.height * 1.0f / scaleFit.height);
            n = n2 * (1.0f / n3 / n3 / n3);
        }
        return n;
    }
    
    @Override
    public Rect scalePreview(final Size obj, final Size obj2) {
        final Size scaleFit = obj.scaleFit(obj2);
        Log.i(FitCenterStrategy.TAG, "Preview: " + obj + "; Scaled: " + scaleFit + "; Want: " + obj2);
        final int n = (scaleFit.width - obj2.width) / 2;
        final int n2 = (scaleFit.height - obj2.height) / 2;
        return new Rect(-n, -n2, scaleFit.width - n, scaleFit.height - n2);
    }
}
