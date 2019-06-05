package androidx.work.impl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public Preferences(Context context) {
        this.mContext = context;
    }

    public boolean needsReschedule() {
        return getSharedPreferences().getBoolean("reschedule_needed", false);
    }

    public void setNeedsReschedule(boolean z) {
        getSharedPreferences().edit().putBoolean("reschedule_needed", z).apply();
    }

    private SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences;
        synchronized (Preferences.class) {
            if (this.mSharedPreferences == null) {
                this.mSharedPreferences = this.mContext.getSharedPreferences("androidx.work.util.preferences", 0);
            }
            sharedPreferences = this.mSharedPreferences;
        }
        return sharedPreferences;
    }
}
