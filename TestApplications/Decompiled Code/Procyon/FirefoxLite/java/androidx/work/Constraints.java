// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import android.os.Build$VERSION;

public final class Constraints
{
    public static final Constraints NONE;
    private ContentUriTriggers mContentUriTriggers;
    private NetworkType mRequiredNetworkType;
    private boolean mRequiresBatteryNotLow;
    private boolean mRequiresCharging;
    private boolean mRequiresDeviceIdle;
    private boolean mRequiresStorageNotLow;
    private long mTriggerContentUpdateDelay;
    private long mTriggerMaxContentDelay;
    
    static {
        NONE = new Builder().build();
    }
    
    public Constraints() {
        this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
        this.mTriggerContentUpdateDelay = -1L;
        this.mTriggerMaxContentDelay = -1L;
        this.mContentUriTriggers = new ContentUriTriggers();
    }
    
    Constraints(final Builder builder) {
        this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
        this.mTriggerContentUpdateDelay = -1L;
        this.mTriggerMaxContentDelay = -1L;
        this.mContentUriTriggers = new ContentUriTriggers();
        this.mRequiresCharging = builder.mRequiresCharging;
        this.mRequiresDeviceIdle = (Build$VERSION.SDK_INT >= 23 && builder.mRequiresDeviceIdle);
        this.mRequiredNetworkType = builder.mRequiredNetworkType;
        this.mRequiresBatteryNotLow = builder.mRequiresBatteryNotLow;
        this.mRequiresStorageNotLow = builder.mRequiresStorageNotLow;
        if (Build$VERSION.SDK_INT >= 24) {
            this.mContentUriTriggers = builder.mContentUriTriggers;
            this.mTriggerContentUpdateDelay = builder.mTriggerContentUpdateDelay;
            this.mTriggerMaxContentDelay = builder.mTriggerContentMaxDelay;
        }
    }
    
    public Constraints(final Constraints constraints) {
        this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
        this.mTriggerContentUpdateDelay = -1L;
        this.mTriggerMaxContentDelay = -1L;
        this.mContentUriTriggers = new ContentUriTriggers();
        this.mRequiresCharging = constraints.mRequiresCharging;
        this.mRequiresDeviceIdle = constraints.mRequiresDeviceIdle;
        this.mRequiredNetworkType = constraints.mRequiredNetworkType;
        this.mRequiresBatteryNotLow = constraints.mRequiresBatteryNotLow;
        this.mRequiresStorageNotLow = constraints.mRequiresStorageNotLow;
        this.mContentUriTriggers = constraints.mContentUriTriggers;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final Constraints constraints = (Constraints)o;
            return this.mRequiresCharging == constraints.mRequiresCharging && this.mRequiresDeviceIdle == constraints.mRequiresDeviceIdle && this.mRequiresBatteryNotLow == constraints.mRequiresBatteryNotLow && this.mRequiresStorageNotLow == constraints.mRequiresStorageNotLow && this.mTriggerContentUpdateDelay == constraints.mTriggerContentUpdateDelay && this.mTriggerMaxContentDelay == constraints.mTriggerMaxContentDelay && this.mRequiredNetworkType == constraints.mRequiredNetworkType && this.mContentUriTriggers.equals(constraints.mContentUriTriggers);
        }
        return false;
    }
    
    public ContentUriTriggers getContentUriTriggers() {
        return this.mContentUriTriggers;
    }
    
    public NetworkType getRequiredNetworkType() {
        return this.mRequiredNetworkType;
    }
    
    public long getTriggerContentUpdateDelay() {
        return this.mTriggerContentUpdateDelay;
    }
    
    public long getTriggerMaxContentDelay() {
        return this.mTriggerMaxContentDelay;
    }
    
    public boolean hasContentUriTriggers() {
        return this.mContentUriTriggers.size() > 0;
    }
    
    @Override
    public int hashCode() {
        return ((((((this.mRequiredNetworkType.hashCode() * 31 + (this.mRequiresCharging ? 1 : 0)) * 31 + (this.mRequiresDeviceIdle ? 1 : 0)) * 31 + (this.mRequiresBatteryNotLow ? 1 : 0)) * 31 + (this.mRequiresStorageNotLow ? 1 : 0)) * 31 + (int)(this.mTriggerContentUpdateDelay ^ this.mTriggerContentUpdateDelay >>> 32)) * 31 + (int)(this.mTriggerMaxContentDelay ^ this.mTriggerMaxContentDelay >>> 32)) * 31 + this.mContentUriTriggers.hashCode();
    }
    
    public boolean requiresBatteryNotLow() {
        return this.mRequiresBatteryNotLow;
    }
    
    public boolean requiresCharging() {
        return this.mRequiresCharging;
    }
    
    public boolean requiresDeviceIdle() {
        return this.mRequiresDeviceIdle;
    }
    
    public boolean requiresStorageNotLow() {
        return this.mRequiresStorageNotLow;
    }
    
    public void setContentUriTriggers(final ContentUriTriggers mContentUriTriggers) {
        this.mContentUriTriggers = mContentUriTriggers;
    }
    
    public void setRequiredNetworkType(final NetworkType mRequiredNetworkType) {
        this.mRequiredNetworkType = mRequiredNetworkType;
    }
    
    public void setRequiresBatteryNotLow(final boolean mRequiresBatteryNotLow) {
        this.mRequiresBatteryNotLow = mRequiresBatteryNotLow;
    }
    
    public void setRequiresCharging(final boolean mRequiresCharging) {
        this.mRequiresCharging = mRequiresCharging;
    }
    
    public void setRequiresDeviceIdle(final boolean mRequiresDeviceIdle) {
        this.mRequiresDeviceIdle = mRequiresDeviceIdle;
    }
    
    public void setRequiresStorageNotLow(final boolean mRequiresStorageNotLow) {
        this.mRequiresStorageNotLow = mRequiresStorageNotLow;
    }
    
    public void setTriggerContentUpdateDelay(final long mTriggerContentUpdateDelay) {
        this.mTriggerContentUpdateDelay = mTriggerContentUpdateDelay;
    }
    
    public void setTriggerMaxContentDelay(final long mTriggerMaxContentDelay) {
        this.mTriggerMaxContentDelay = mTriggerMaxContentDelay;
    }
    
    public static final class Builder
    {
        ContentUriTriggers mContentUriTriggers;
        NetworkType mRequiredNetworkType;
        boolean mRequiresBatteryNotLow;
        boolean mRequiresCharging;
        boolean mRequiresDeviceIdle;
        boolean mRequiresStorageNotLow;
        long mTriggerContentMaxDelay;
        long mTriggerContentUpdateDelay;
        
        public Builder() {
            this.mRequiresCharging = false;
            this.mRequiresDeviceIdle = false;
            this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
            this.mRequiresBatteryNotLow = false;
            this.mRequiresStorageNotLow = false;
            this.mTriggerContentUpdateDelay = -1L;
            this.mTriggerContentMaxDelay = -1L;
            this.mContentUriTriggers = new ContentUriTriggers();
        }
        
        public Constraints build() {
            return new Constraints(this);
        }
    }
}
