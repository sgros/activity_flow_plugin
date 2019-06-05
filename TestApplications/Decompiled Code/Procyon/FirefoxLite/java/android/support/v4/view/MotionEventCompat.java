// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.MotionEvent;

public final class MotionEventCompat
{
    @Deprecated
    public static int getActionMasked(final MotionEvent motionEvent) {
        return motionEvent.getActionMasked();
    }
    
    public static boolean isFromSource(final MotionEvent motionEvent, final int n) {
        return (motionEvent.getSource() & n) == n;
    }
}
