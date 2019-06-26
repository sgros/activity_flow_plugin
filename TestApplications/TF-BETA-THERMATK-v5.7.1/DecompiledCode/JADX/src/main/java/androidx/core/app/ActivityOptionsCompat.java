package androidx.core.app;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;

public class ActivityOptionsCompat {

    private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat {
        private final ActivityOptions mActivityOptions;

        ActivityOptionsCompatImpl(ActivityOptions activityOptions) {
            this.mActivityOptions = activityOptions;
        }

        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }
    }

    public Bundle toBundle() {
        return null;
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int i, int i2) {
        if (VERSION.SDK_INT >= 16) {
            return new ActivityOptionsCompatImpl(ActivityOptions.makeCustomAnimation(context, i, i2));
        }
        return new ActivityOptionsCompat();
    }

    protected ActivityOptionsCompat() {
    }
}
