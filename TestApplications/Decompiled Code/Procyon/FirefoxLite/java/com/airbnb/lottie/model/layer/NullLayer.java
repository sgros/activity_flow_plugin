// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import android.graphics.RectF;
import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.LottieDrawable;

public class NullLayer extends BaseLayer
{
    NullLayer(final LottieDrawable lottieDrawable, final Layer layer) {
        super(lottieDrawable, layer);
    }
    
    @Override
    void drawLayer(final Canvas canvas, final Matrix matrix, final int n) {
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix) {
        super.getBounds(rectF, matrix);
        rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
