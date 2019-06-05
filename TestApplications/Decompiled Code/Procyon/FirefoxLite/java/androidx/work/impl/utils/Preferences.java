// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import android.content.SharedPreferences;
import android.content.Context;

public class Preferences
{
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    
    public Preferences(final Context mContext) {
        this.mContext = mContext;
    }
    
    private SharedPreferences getSharedPreferences() {
        synchronized (Preferences.class) {
            if (this.mSharedPreferences == null) {
                this.mSharedPreferences = this.mContext.getSharedPreferences("androidx.work.util.preferences", 0);
            }
            return this.mSharedPreferences;
        }
    }
    
    public boolean needsReschedule() {
        return this.getSharedPreferences().getBoolean("reschedule_needed", false);
    }
    
    public void setNeedsReschedule(final boolean b) {
        this.getSharedPreferences().edit().putBoolean("reschedule_needed", b).apply();
    }
}
