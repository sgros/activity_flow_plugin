// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.MotionEvent;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(12)
@RequiresApi(12)
class MotionEventCompatHoneycombMr1
{
    static float getAxisValue(final MotionEvent motionEvent, final int n) {
        return motionEvent.getAxisValue(n);
    }
    
    static float getAxisValue(final MotionEvent motionEvent, final int n, final int n2) {
        return motionEvent.getAxisValue(n, n2);
    }
}
