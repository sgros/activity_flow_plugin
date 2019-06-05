package androidx.work.impl.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class IdGenerator {
    private final Context mContext;
    private boolean mLoadedPreferences;
    private SharedPreferences mSharedPrefs;

    public IdGenerator(Context context) {
        this.mContext = context;
    }

    public int nextJobSchedulerIdWithRange(int i, int i2) {
        synchronized (IdGenerator.class) {
            loadPreferencesIfNecessary();
            int nextId = nextId("next_job_scheduler_id");
            if (nextId >= i) {
                if (nextId <= i2) {
                    i = nextId;
                }
            }
            update("next_job_scheduler_id", i + 1);
        }
        return i;
    }

    public int nextAlarmManagerId() {
        int nextId;
        synchronized (IdGenerator.class) {
            loadPreferencesIfNecessary();
            nextId = nextId("next_alarm_manager_id");
        }
        return nextId;
    }

    private int nextId(String str) {
        int i = 0;
        int i2 = this.mSharedPrefs.getInt(str, 0);
        if (i2 != Integer.MAX_VALUE) {
            i = i2 + 1;
        }
        update(str, i);
        return i2;
    }

    private void update(String str, int i) {
        this.mSharedPrefs.edit().putInt(str, i).apply();
    }

    private void loadPreferencesIfNecessary() {
        if (!this.mLoadedPreferences) {
            this.mSharedPrefs = this.mContext.getSharedPreferences("androidx.work.util.id", 0);
            this.mLoadedPreferences = true;
        }
    }
}
