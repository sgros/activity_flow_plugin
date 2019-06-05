// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import android.graphics.RectF;
import android.graphics.Matrix;
import android.graphics.Canvas;

public interface DrawingContent extends Content
{
    void draw(final Canvas p0, final Matrix p1, final int p2);
    
    void getBounds(final RectF p0, final Matrix p1);
}
