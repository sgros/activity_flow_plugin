// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif.transforms;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Rect;

public interface Transform
{
    void onBoundsChange(final Rect p0);
    
    void onDraw(final Canvas p0, final Paint p1, final Bitmap p2);
}
