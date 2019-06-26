// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class ExpirableBitmapDrawable extends BitmapDrawable
{
    private static final int[] settableStatuses;
    private int[] mState;
    
    static {
        settableStatuses = new int[] { -2, -3, -4 };
    }
    
    public ExpirableBitmapDrawable(final Bitmap bitmap) {
        super(bitmap);
        this.mState = new int[0];
    }
    
    public static int getState(final Drawable drawable) {
        for (final int n : drawable.getState()) {
            final int[] settableStatuses = ExpirableBitmapDrawable.settableStatuses;
            for (int length2 = settableStatuses.length, j = 0; j < length2; ++j) {
                if (n == settableStatuses[j]) {
                    return n;
                }
            }
        }
        return -1;
    }
    
    public static void setState(final Drawable drawable, final int n) {
        drawable.setState(new int[] { n });
    }
    
    public int[] getState() {
        return this.mState;
    }
    
    public boolean isStateful() {
        return this.mState.length > 0;
    }
    
    public boolean setState(final int[] mState) {
        this.mState = mState;
        return true;
    }
}
