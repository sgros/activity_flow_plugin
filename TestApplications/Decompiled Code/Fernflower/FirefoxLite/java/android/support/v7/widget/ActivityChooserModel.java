package android.support.v7.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ActivityChooserModel extends DataSetObservable {
   static final String LOG_TAG = "ActivityChooserModel";
   private static final Map sDataModelRegistry = new HashMap();
   private static final Object sRegistryLock = new Object();
   private final List mActivities;
   private ActivityChooserModel.OnChooseActivityListener mActivityChoserModelPolicy;
   private ActivityChooserModel.ActivitySorter mActivitySorter;
   boolean mCanReadHistoricalData;
   final Context mContext;
   private final List mHistoricalRecords;
   private boolean mHistoricalRecordsChanged;
   final String mHistoryFileName;
   private int mHistoryMaxSize;
   private final Object mInstanceLock;
   private Intent mIntent;
   private boolean mReadShareHistoryCalled;
   private boolean mReloadActivities;

   private boolean addHistoricalRecord(ActivityChooserModel.HistoricalRecord var1) {
      boolean var2 = this.mHistoricalRecords.add(var1);
      if (var2) {
         this.mHistoricalRecordsChanged = true;
         this.pruneExcessiveHistoricalRecordsIfNeeded();
         this.persistHistoricalDataIfNeeded();
         this.sortActivitiesIfNeeded();
         this.notifyChanged();
      }

      return var2;
   }

   private void ensureConsistentState() {
      boolean var1 = this.loadActivitiesIfNeeded();
      boolean var2 = this.readHistoricalDataIfNeeded();
      this.pruneExcessiveHistoricalRecordsIfNeeded();
      if (var1 | var2) {
         this.sortActivitiesIfNeeded();
         this.notifyChanged();
      }

   }

   private boolean loadActivitiesIfNeeded() {
      boolean var1 = this.mReloadActivities;
      int var2 = 0;
      if (var1 && this.mIntent != null) {
         this.mReloadActivities = false;
         this.mActivities.clear();
         List var3 = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);

         for(int var4 = var3.size(); var2 < var4; ++var2) {
            ResolveInfo var5 = (ResolveInfo)var3.get(var2);
            this.mActivities.add(new ActivityChooserModel.ActivityResolveInfo(var5));
         }

         return true;
      } else {
         return false;
      }
   }

   private void persistHistoricalDataIfNeeded() {
      if (this.mReadShareHistoryCalled) {
         if (this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty(this.mHistoryFileName)) {
               (new ActivityChooserModel.PersistHistoryAsyncTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[]{new ArrayList(this.mHistoricalRecords), this.mHistoryFileName});
            }

         }
      } else {
         throw new IllegalStateException("No preceding call to #readHistoricalData");
      }
   }

   private void pruneExcessiveHistoricalRecordsIfNeeded() {
      int var1 = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
      if (var1 > 0) {
         this.mHistoricalRecordsChanged = true;

         for(int var2 = 0; var2 < var1; ++var2) {
            ActivityChooserModel.HistoricalRecord var3 = (ActivityChooserModel.HistoricalRecord)this.mHistoricalRecords.remove(0);
         }

      }
   }

   private boolean readHistoricalDataIfNeeded() {
      if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty(this.mHistoryFileName)) {
         this.mCanReadHistoricalData = false;
         this.mReadShareHistoryCalled = true;
         this.readHistoricalDataImpl();
         return true;
      } else {
         return false;
      }
   }

   private void readHistoricalDataImpl() {
      // $FF: Couldn't be decompiled
   }

   private boolean sortActivitiesIfNeeded() {
      if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
         this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
         return true;
      } else {
         return false;
      }
   }

   public Intent chooseActivity(int var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label208: {
         try {
            if (this.mIntent == null) {
               return null;
            }
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label208;
         }

         ComponentName var4;
         Intent var26;
         try {
            this.ensureConsistentState();
            ActivityChooserModel.ActivityResolveInfo var3 = (ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(var1);
            var4 = new ComponentName(var3.resolveInfo.activityInfo.packageName, var3.resolveInfo.activityInfo.name);
            var26 = new Intent(this.mIntent);
            var26.setComponent(var4);
            if (this.mActivityChoserModelPolicy != null) {
               Intent var5 = new Intent(var26);
               if (this.mActivityChoserModelPolicy.onChooseActivity(this, var5)) {
                  return null;
               }
            }
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label208;
         }

         label198:
         try {
            ActivityChooserModel.HistoricalRecord var28 = new ActivityChooserModel.HistoricalRecord(var4, System.currentTimeMillis(), 1.0F);
            this.addHistoricalRecord(var28);
            return var26;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label198;
         }
      }

      while(true) {
         Throwable var27 = var10000;

         try {
            throw var27;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public ResolveInfo getActivity(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int getActivityCount() {
      // $FF: Couldn't be decompiled
   }

   public int getActivityIndex(ResolveInfo var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label231: {
         List var3;
         int var4;
         try {
            this.ensureConsistentState();
            var3 = this.mActivities;
            var4 = var3.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label231;
         }

         int var5 = 0;

         while(true) {
            if (var5 >= var4) {
               try {
                  return -1;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }
            }

            try {
               if (((ActivityChooserModel.ActivityResolveInfo)var3.get(var5)).resolveInfo == var1) {
                  return var5;
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            ++var5;
         }
      }

      while(true) {
         Throwable var26 = var10000;

         try {
            throw var26;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public ResolveInfo getDefaultActivity() {
      Object var1 = this.mInstanceLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label123: {
         try {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
               ResolveInfo var15 = ((ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(0)).resolveInfo;
               return var15;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            return null;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void setDefaultActivity(int var1) {
      Object var2 = this.mInstanceLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label191: {
         ActivityChooserModel.ActivityResolveInfo var3;
         ActivityChooserModel.ActivityResolveInfo var4;
         try {
            this.ensureConsistentState();
            var3 = (ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(var1);
            var4 = (ActivityChooserModel.ActivityResolveInfo)this.mActivities.get(0);
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label191;
         }

         float var5;
         if (var4 != null) {
            try {
               var5 = var4.weight - var3.weight + 5.0F;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label191;
            }
         } else {
            var5 = 1.0F;
         }

         label179:
         try {
            ComponentName var28 = new ComponentName(var3.resolveInfo.activityInfo.packageName, var3.resolveInfo.activityInfo.name);
            ActivityChooserModel.HistoricalRecord var26 = new ActivityChooserModel.HistoricalRecord(var28, System.currentTimeMillis(), var5);
            this.addHistoricalRecord(var26);
            return;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label179;
         }
      }

      while(true) {
         Throwable var27 = var10000;

         try {
            throw var27;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   public static final class ActivityResolveInfo implements Comparable {
      public final ResolveInfo resolveInfo;
      public float weight;

      public ActivityResolveInfo(ResolveInfo var1) {
         this.resolveInfo = var1;
      }

      public int compareTo(ActivityChooserModel.ActivityResolveInfo var1) {
         return Float.floatToIntBits(var1.weight) - Float.floatToIntBits(this.weight);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 == null) {
            return false;
         } else if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            ActivityChooserModel.ActivityResolveInfo var2 = (ActivityChooserModel.ActivityResolveInfo)var1;
            return Float.floatToIntBits(this.weight) == Float.floatToIntBits(var2.weight);
         }
      }

      public int hashCode() {
         return Float.floatToIntBits(this.weight) + 31;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append("resolveInfo:");
         var1.append(this.resolveInfo.toString());
         var1.append("; weight:");
         var1.append(new BigDecimal((double)this.weight));
         var1.append("]");
         return var1.toString();
      }
   }

   public interface ActivitySorter {
      void sort(Intent var1, List var2, List var3);
   }

   public static final class HistoricalRecord {
      public final ComponentName activity;
      public final long time;
      public final float weight;

      public HistoricalRecord(ComponentName var1, long var2, float var4) {
         this.activity = var1;
         this.time = var2;
         this.weight = var4;
      }

      public HistoricalRecord(String var1, long var2, float var4) {
         this(ComponentName.unflattenFromString(var1), var2, var4);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 == null) {
            return false;
         } else if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            ActivityChooserModel.HistoricalRecord var2 = (ActivityChooserModel.HistoricalRecord)var1;
            if (this.activity == null) {
               if (var2.activity != null) {
                  return false;
               }
            } else if (!this.activity.equals(var2.activity)) {
               return false;
            }

            if (this.time != var2.time) {
               return false;
            } else {
               return Float.floatToIntBits(this.weight) == Float.floatToIntBits(var2.weight);
            }
         }
      }

      public int hashCode() {
         int var1;
         if (this.activity == null) {
            var1 = 0;
         } else {
            var1 = this.activity.hashCode();
         }

         return ((var1 + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("[");
         var1.append("; activity:");
         var1.append(this.activity);
         var1.append("; time:");
         var1.append(this.time);
         var1.append("; weight:");
         var1.append(new BigDecimal((double)this.weight));
         var1.append("]");
         return var1.toString();
      }
   }

   public interface OnChooseActivityListener {
      boolean onChooseActivity(ActivityChooserModel var1, Intent var2);
   }

   private final class PersistHistoryAsyncTask extends AsyncTask {
      PersistHistoryAsyncTask() {
      }

      public Void doInBackground(Object... param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
