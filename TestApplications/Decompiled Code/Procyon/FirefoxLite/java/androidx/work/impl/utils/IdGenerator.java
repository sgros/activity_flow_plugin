// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import android.content.SharedPreferences;
import android.content.Context;

public class IdGenerator
{
    private final Context mContext;
    private boolean mLoadedPreferences;
    private SharedPreferences mSharedPrefs;
    
    public IdGenerator(final Context mContext) {
        this.mContext = mContext;
    }
    
    private void loadPreferencesIfNecessary() {
        if (!this.mLoadedPreferences) {
            this.mSharedPrefs = this.mContext.getSharedPreferences("androidx.work.util.id", 0);
            this.mLoadedPreferences = true;
        }
    }
    
    private int nextId(final String s) {
        final SharedPreferences mSharedPrefs = this.mSharedPrefs;
        int n = 0;
        final int int1 = mSharedPrefs.getInt(s, 0);
        if (int1 != Integer.MAX_VALUE) {
            n = int1 + 1;
        }
        this.update(s, n);
        return int1;
    }
    
    private void update(final String s, final int n) {
        this.mSharedPrefs.edit().putInt(s, n).apply();
    }
    
    public int nextAlarmManagerId() {
        synchronized (IdGenerator.class) {
            this.loadPreferencesIfNecessary();
            return this.nextId("next_alarm_manager_id");
        }
    }
    
    public int nextJobSchedulerIdWithRange(int n, final int n2) {
        synchronized (IdGenerator.class) {
            this.loadPreferencesIfNecessary();
            final int nextId = this.nextId("next_job_scheduler_id");
            if (nextId >= n && nextId <= n2) {
                n = nextId;
            }
            else {
                this.update("next_job_scheduler_id", n + 1);
            }
            return n;
        }
    }
}
