package androidx.work;

import android.os.Build.VERSION;

public final class Constraints {
   public static final Constraints NONE = (new Constraints.Builder()).build();
   private ContentUriTriggers mContentUriTriggers;
   private NetworkType mRequiredNetworkType;
   private boolean mRequiresBatteryNotLow;
   private boolean mRequiresCharging;
   private boolean mRequiresDeviceIdle;
   private boolean mRequiresStorageNotLow;
   private long mTriggerContentUpdateDelay;
   private long mTriggerMaxContentDelay;

   public Constraints() {
      this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
      this.mTriggerContentUpdateDelay = -1L;
      this.mTriggerMaxContentDelay = -1L;
      this.mContentUriTriggers = new ContentUriTriggers();
   }

   Constraints(Constraints.Builder var1) {
      this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
      this.mTriggerContentUpdateDelay = -1L;
      this.mTriggerMaxContentDelay = -1L;
      this.mContentUriTriggers = new ContentUriTriggers();
      this.mRequiresCharging = var1.mRequiresCharging;
      boolean var2;
      if (VERSION.SDK_INT >= 23 && var1.mRequiresDeviceIdle) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mRequiresDeviceIdle = var2;
      this.mRequiredNetworkType = var1.mRequiredNetworkType;
      this.mRequiresBatteryNotLow = var1.mRequiresBatteryNotLow;
      this.mRequiresStorageNotLow = var1.mRequiresStorageNotLow;
      if (VERSION.SDK_INT >= 24) {
         this.mContentUriTriggers = var1.mContentUriTriggers;
         this.mTriggerContentUpdateDelay = var1.mTriggerContentUpdateDelay;
         this.mTriggerMaxContentDelay = var1.mTriggerContentMaxDelay;
      }

   }

   public Constraints(Constraints var1) {
      this.mRequiredNetworkType = NetworkType.NOT_REQUIRED;
      this.mTriggerContentUpdateDelay = -1L;
      this.mTriggerMaxContentDelay = -1L;
      this.mContentUriTriggers = new ContentUriTriggers();
      this.mRequiresCharging = var1.mRequiresCharging;
      this.mRequiresDeviceIdle = var1.mRequiresDeviceIdle;
      this.mRequiredNetworkType = var1.mRequiredNetworkType;
      this.mRequiresBatteryNotLow = var1.mRequiresBatteryNotLow;
      this.mRequiresStorageNotLow = var1.mRequiresStorageNotLow;
      this.mContentUriTriggers = var1.mContentUriTriggers;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Constraints var2 = (Constraints)var1;
         if (this.mRequiresCharging != var2.mRequiresCharging) {
            return false;
         } else if (this.mRequiresDeviceIdle != var2.mRequiresDeviceIdle) {
            return false;
         } else if (this.mRequiresBatteryNotLow != var2.mRequiresBatteryNotLow) {
            return false;
         } else if (this.mRequiresStorageNotLow != var2.mRequiresStorageNotLow) {
            return false;
         } else if (this.mTriggerContentUpdateDelay != var2.mTriggerContentUpdateDelay) {
            return false;
         } else if (this.mTriggerMaxContentDelay != var2.mTriggerMaxContentDelay) {
            return false;
         } else {
            return this.mRequiredNetworkType != var2.mRequiredNetworkType ? false : this.mContentUriTriggers.equals(var2.mContentUriTriggers);
         }
      } else {
         return false;
      }
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
      boolean var1;
      if (this.mContentUriTriggers.size() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int hashCode() {
      return ((((((this.mRequiredNetworkType.hashCode() * 31 + this.mRequiresCharging) * 31 + this.mRequiresDeviceIdle) * 31 + this.mRequiresBatteryNotLow) * 31 + this.mRequiresStorageNotLow) * 31 + (int)(this.mTriggerContentUpdateDelay ^ this.mTriggerContentUpdateDelay >>> 32)) * 31 + (int)(this.mTriggerMaxContentDelay ^ this.mTriggerMaxContentDelay >>> 32)) * 31 + this.mContentUriTriggers.hashCode();
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

   public void setContentUriTriggers(ContentUriTriggers var1) {
      this.mContentUriTriggers = var1;
   }

   public void setRequiredNetworkType(NetworkType var1) {
      this.mRequiredNetworkType = var1;
   }

   public void setRequiresBatteryNotLow(boolean var1) {
      this.mRequiresBatteryNotLow = var1;
   }

   public void setRequiresCharging(boolean var1) {
      this.mRequiresCharging = var1;
   }

   public void setRequiresDeviceIdle(boolean var1) {
      this.mRequiresDeviceIdle = var1;
   }

   public void setRequiresStorageNotLow(boolean var1) {
      this.mRequiresStorageNotLow = var1;
   }

   public void setTriggerContentUpdateDelay(long var1) {
      this.mTriggerContentUpdateDelay = var1;
   }

   public void setTriggerMaxContentDelay(long var1) {
      this.mTriggerMaxContentDelay = var1;
   }

   public static final class Builder {
      ContentUriTriggers mContentUriTriggers;
      NetworkType mRequiredNetworkType;
      boolean mRequiresBatteryNotLow;
      boolean mRequiresCharging = false;
      boolean mRequiresDeviceIdle = false;
      boolean mRequiresStorageNotLow;
      long mTriggerContentMaxDelay;
      long mTriggerContentUpdateDelay;

      public Builder() {
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
