// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Point;
import android.net.Uri;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public interface ImageRegionDecoder
{
    Bitmap decodeRegion(final Rect p0, final int p1);
    
    Point init(final Context p0, final Uri p1) throws Exception;
    
    boolean isReady();
    
    void recycle();
}
