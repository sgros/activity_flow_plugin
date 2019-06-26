// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class ExtendedBitmapDrawable extends BitmapDrawable
{
    private boolean canInvert;
    private int orientation;
    
    public ExtendedBitmapDrawable(final Bitmap bitmap, final boolean canInvert, final int orientation) {
        super(bitmap);
        this.canInvert = canInvert;
        this.orientation = orientation;
    }
    
    public int getOrientation() {
        return this.orientation;
    }
    
    public boolean isCanInvert() {
        return this.canInvert;
    }
}
