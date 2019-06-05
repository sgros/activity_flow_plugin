package androidx.work;

import android.os.Build.VERSION;

public final class Constraints {
    public static final Constraints NONE = new Builder().build();
    private ContentUriTriggers mContentUriTriggers = new ContentUriTriggers();
    private NetworkType mRequiredNetworkType = NetworkType.NOT_REQUIRED;
    private boolean mRequiresBatteryNotLow;
    private boolean mRequiresCharging;
    private boolean mRequiresDeviceIdle;
    private boolean mRequiresStorageNotLow;
    private long mTriggerContentUpdateDelay = -1;
    private long mTriggerMaxContentDelay = -1;

    public static final class Builder {
        ContentUriTriggers mContentUriTriggers = new ContentUriTriggers();
        NetworkType mRequiredNetworkType = NetworkType.NOT_REQUIRED;
        boolean mRequiresBatteryNotLow = false;
        boolean mRequiresCharging = false;
        boolean mRequiresDeviceIdle = false;
        boolean mRequiresStorageNotLow = false;
        long mTriggerContentMaxDelay = -1;
        long mTriggerContentUpdateDelay = -1;

        public Constraints build() {
            return new Constraints(this);
        }
    }

    Constraints(Builder builder) {
        this.mRequiresCharging = builder.mRequiresCharging;
        boolean z = VERSION.SDK_INT >= 23 && builder.mRequiresDeviceIdle;
        this.mRequiresDeviceIdle = z;
        this.mRequiredNetworkType = builder.mRequiredNetworkType;
        this.mRequiresBatteryNotLow = builder.mRequiresBatteryNotLow;
        this.mRequiresStorageNotLow = builder.mRequiresStorageNotLow;
        if (VERSION.SDK_INT >= 24) {
            this.mContentUriTriggers = builder.mContentUriTriggers;
            this.mTriggerContentUpdateDelay = builder.mTriggerContentUpdateDelay;
            this.mTriggerMaxContentDelay = builder.mTriggerContentMaxDelay;
        }
    }

    public Constraints(Constraints constraints) {
        this.mRequiresCharging = constraints.mRequiresCharging;
        this.mRequiresDeviceIdle = constraints.mRequiresDeviceIdle;
        this.mRequiredNetworkType = constraints.mRequiredNetworkType;
        this.mRequiresBatteryNotLow = constraints.mRequiresBatteryNotLow;
        this.mRequiresStorageNotLow = constraints.mRequiresStorageNotLow;
        this.mContentUriTriggers = constraints.mContentUriTriggers;
    }

    public NetworkType getRequiredNetworkType() {
        return this.mRequiredNetworkType;
    }

    public void setRequiredNetworkType(NetworkType networkType) {
        this.mRequiredNetworkType = networkType;
    }

    public boolean requiresCharging() {
        return this.mRequiresCharging;
    }

    public void setRequiresCharging(boolean z) {
        this.mRequiresCharging = z;
    }

    public boolean requiresDeviceIdle() {
        return this.mRequiresDeviceIdle;
    }

    public void setRequiresDeviceIdle(boolean z) {
        this.mRequiresDeviceIdle = z;
    }

    public boolean requiresBatteryNotLow() {
        return this.mRequiresBatteryNotLow;
    }

    public void setRequiresBatteryNotLow(boolean z) {
        this.mRequiresBatteryNotLow = z;
    }

    public boolean requiresStorageNotLow() {
        return this.mRequiresStorageNotLow;
    }

    public void setRequiresStorageNotLow(boolean z) {
        this.mRequiresStorageNotLow = z;
    }

    public long getTriggerContentUpdateDelay() {
        return this.mTriggerContentUpdateDelay;
    }

    public void setTriggerContentUpdateDelay(long j) {
        this.mTriggerContentUpdateDelay = j;
    }

    public long getTriggerMaxContentDelay() {
        return this.mTriggerMaxContentDelay;
    }

    public void setTriggerMaxContentDelay(long j) {
        this.mTriggerMaxContentDelay = j;
    }

    public void setContentUriTriggers(ContentUriTriggers contentUriTriggers) {
        this.mContentUriTriggers = contentUriTriggers;
    }

    public ContentUriTriggers getContentUriTriggers() {
        return this.mContentUriTriggers;
    }

    public boolean hasContentUriTriggers() {
        return this.mContentUriTriggers.size() > 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Constraints constraints = (Constraints) obj;
        if (this.mRequiresCharging == constraints.mRequiresCharging && this.mRequiresDeviceIdle == constraints.mRequiresDeviceIdle && this.mRequiresBatteryNotLow == constraints.mRequiresBatteryNotLow && this.mRequiresStorageNotLow == constraints.mRequiresStorageNotLow && this.mTriggerContentUpdateDelay == constraints.mTriggerContentUpdateDelay && this.mTriggerMaxContentDelay == constraints.mTriggerMaxContentDelay && this.mRequiredNetworkType == constraints.mRequiredNetworkType) {
            return this.mContentUriTriggers.equals(constraints.mContentUriTriggers);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((this.mRequiredNetworkType.hashCode() * 31) + this.mRequiresCharging) * 31) + this.mRequiresDeviceIdle) * 31) + this.mRequiresBatteryNotLow) * 31) + this.mRequiresStorageNotLow) * 31) + ((int) (this.mTriggerContentUpdateDelay ^ (this.mTriggerContentUpdateDelay >>> 32)))) * 31) + ((int) (this.mTriggerMaxContentDelay ^ (this.mTriggerMaxContentDelay >>> 32)))) * 31) + this.mContentUriTriggers.hashCode();
    }
}
