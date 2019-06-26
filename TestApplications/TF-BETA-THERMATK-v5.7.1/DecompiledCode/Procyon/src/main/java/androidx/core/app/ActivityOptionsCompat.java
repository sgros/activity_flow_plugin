// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.app;

import android.os.Bundle;
import android.app.ActivityOptions;
import android.os.Build$VERSION;
import android.content.Context;

public class ActivityOptionsCompat
{
    protected ActivityOptionsCompat() {
    }
    
    public static ActivityOptionsCompat makeCustomAnimation(final Context context, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeCustomAnimation(context, n, n2));
        }
        return new ActivityOptionsCompat();
    }
    
    public Bundle toBundle() {
        return null;
    }
    
    private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat
    {
        private final ActivityOptions mActivityOptions;
        
        ActivityOptionsCompatImpl(final ActivityOptions mActivityOptions) {
            this.mActivityOptions = mActivityOptions;
        }
        
        @Override
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }
    }
}
