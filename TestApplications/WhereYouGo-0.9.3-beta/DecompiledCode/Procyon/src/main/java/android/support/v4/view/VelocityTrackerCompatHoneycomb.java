// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.VelocityTracker;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(11)
@RequiresApi(11)
class VelocityTrackerCompatHoneycomb
{
    public static float getXVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getXVelocity(n);
    }
    
    public static float getYVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getYVelocity(n);
    }
}
