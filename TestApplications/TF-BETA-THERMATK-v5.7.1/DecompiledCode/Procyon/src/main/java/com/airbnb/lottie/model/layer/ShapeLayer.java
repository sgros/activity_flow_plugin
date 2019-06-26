// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import java.util.List;
import com.airbnb.lottie.model.KeyPath;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.graphics.Canvas;
import java.util.Collections;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.ContentGroup;

public class ShapeLayer extends BaseLayer
{
    private final ContentGroup contentGroup;
    
    ShapeLayer(final LottieDrawable lottieDrawable, final Layer layer) {
        super(lottieDrawable, layer);
        (this.contentGroup = new ContentGroup(lottieDrawable, this, new ShapeGroup("__container", layer.getShapes(), false))).setContents(Collections.emptyList(), Collections.emptyList());
    }
    
    @Override
    void drawLayer(final Canvas canvas, final Matrix matrix, final int n) {
        this.contentGroup.draw(canvas, matrix, n);
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        super.getBounds(rectF, matrix, b);
        this.contentGroup.getBounds(rectF, super.boundsMatrix, b);
    }
    
    protected void resolveChildKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
        this.contentGroup.resolveKeyPath(keyPath, n, list, keyPath2);
    }
}
