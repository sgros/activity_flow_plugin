package androidx.work.impl.model;

import android.arch.core.util.Function;
import android.os.Build.VERSION;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class WorkSpec {
   private static final String TAG = Logger.tagWithPrefix("WorkSpec");
   public static final Function WORK_INFO_MAPPER = new Function() {
      public List apply(List var1) {
         if (var1 == null) {
            return null;
         } else {
            ArrayList var2 = new ArrayList(var1.size());
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               var2.add(((WorkSpec.WorkInfoPojo)var3.next()).toWorkInfo());
            }

            return var2;
         }
      }
   };
   public long backoffDelayDuration;
   public BackoffPolicy backoffPolicy;
   public Constraints constraints;
   public long flexDuration;
   public String id;
   public long initialDelay;
   public Data input;
   public String inputMergerClassName;
   public long intervalDuration;
   public long minimumRetentionDuration;
   public Data output;
   public long periodStartTime;
   public int runAttemptCount;
   public long scheduleRequestedAt;
   public WorkInfo.State state;
   public String workerClassName;

   public WorkSpec(WorkSpec var1) {
      this.state = WorkInfo.State.ENQUEUED;
      this.input = Data.EMPTY;
      this.output = Data.EMPTY;
      this.constraints = Constraints.NONE;
      this.backoffPolicy = BackoffPolicy.EXPONENTIAL;
      this.backoffDelayDuration = 30000L;
      this.scheduleRequestedAt = -1L;
      this.id = var1.id;
      this.workerClassName = var1.workerClassName;
      this.state = var1.state;
      this.inputMergerClassName = var1.inputMergerClassName;
      this.input = new Data(var1.input);
      this.output = new Data(var1.output);
      this.initialDelay = var1.initialDelay;
      this.intervalDuration = var1.intervalDuration;
      this.flexDuration = var1.flexDuration;
      this.constraints = new Constraints(var1.constraints);
      this.runAttemptCount = var1.runAttemptCount;
      this.backoffPolicy = var1.backoffPolicy;
      this.backoffDelayDuration = var1.backoffDelayDuration;
      this.periodStartTime = var1.periodStartTime;
      this.minimumRetentionDuration = var1.minimumRetentionDuration;
      this.scheduleRequestedAt = var1.scheduleRequestedAt;
   }

   public WorkSpec(String var1, String var2) {
      this.state = WorkInfo.State.ENQUEUED;
      this.input = Data.EMPTY;
      this.output = Data.EMPTY;
      this.constraints = Constraints.NONE;
      this.backoffPolicy = BackoffPolicy.EXPONENTIAL;
      this.backoffDelayDuration = 30000L;
      this.scheduleRequestedAt = -1L;
      this.id = var1;
      this.workerClassName = var2;
   }

   public long calculateNextRunTime() {
      boolean var1 = this.isBackedOff();
      boolean var2 = false;
      boolean var3 = false;
      long var4;
      if (var1) {
         if (this.backoffPolicy == BackoffPolicy.LINEAR) {
            var3 = true;
         }

         if (var3) {
            var4 = this.backoffDelayDuration * (long)this.runAttemptCount;
         } else {
            var4 = (long)Math.scalb((float)this.backoffDelayDuration, this.runAttemptCount - 1);
         }

         return this.periodStartTime + Math.min(18000000L, var4);
      } else if (this.isPeriodic()) {
         if (VERSION.SDK_INT <= 22) {
            var3 = var2;
            if (this.flexDuration != this.intervalDuration) {
               var3 = true;
            }

            if (var3) {
               if (this.periodStartTime == 0L) {
                  var4 = -1L * this.flexDuration;
               } else {
                  var4 = 0L;
               }

               long var6;
               if (this.periodStartTime == 0L) {
                  var6 = System.currentTimeMillis();
               } else {
                  var6 = this.periodStartTime;
               }

               return var6 + this.intervalDuration + var4;
            } else {
               return this.periodStartTime + this.intervalDuration;
            }
         } else {
            return this.periodStartTime + this.intervalDuration - this.flexDuration;
         }
      } else {
         return this.periodStartTime + this.initialDelay;
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         WorkSpec var3 = (WorkSpec)var1;
         if (this.initialDelay != var3.initialDelay) {
            return false;
         } else if (this.intervalDuration != var3.intervalDuration) {
            return false;
         } else if (this.flexDuration != var3.flexDuration) {
            return false;
         } else if (this.runAttemptCount != var3.runAttemptCount) {
            return false;
         } else if (this.backoffDelayDuration != var3.backoffDelayDuration) {
            return false;
         } else if (this.periodStartTime != var3.periodStartTime) {
            return false;
         } else if (this.minimumRetentionDuration != var3.minimumRetentionDuration) {
            return false;
         } else if (this.scheduleRequestedAt != var3.scheduleRequestedAt) {
            return false;
         } else if (!this.id.equals(var3.id)) {
            return false;
         } else if (this.state != var3.state) {
            return false;
         } else if (!this.workerClassName.equals(var3.workerClassName)) {
            return false;
         } else {
            if (this.inputMergerClassName != null) {
               if (!this.inputMergerClassName.equals(var3.inputMergerClassName)) {
                  return false;
               }
            } else if (var3.inputMergerClassName != null) {
               return false;
            }

            if (!this.input.equals(var3.input)) {
               return false;
            } else if (!this.output.equals(var3.output)) {
               return false;
            } else if (!this.constraints.equals(var3.constraints)) {
               return false;
            } else {
               if (this.backoffPolicy != var3.backoffPolicy) {
                  var2 = false;
               }

               return var2;
            }
         }
      } else {
         return false;
      }
   }

   public boolean hasConstraints() {
      return Constraints.NONE.equals(this.constraints) ^ true;
   }

   public int hashCode() {
      int var1 = this.id.hashCode();
      int var2 = this.state.hashCode();
      int var3 = this.workerClassName.hashCode();
      int var4;
      if (this.inputMergerClassName != null) {
         var4 = this.inputMergerClassName.hashCode();
      } else {
         var4 = 0;
      }

      return ((((((((((((((var1 * 31 + var2) * 31 + var3) * 31 + var4) * 31 + this.input.hashCode()) * 31 + this.output.hashCode()) * 31 + (int)(this.initialDelay ^ this.initialDelay >>> 32)) * 31 + (int)(this.intervalDuration ^ this.intervalDuration >>> 32)) * 31 + (int)(this.flexDuration ^ this.flexDuration >>> 32)) * 31 + this.constraints.hashCode()) * 31 + this.runAttemptCount) * 31 + this.backoffPolicy.hashCode()) * 31 + (int)(this.backoffDelayDuration ^ this.backoffDelayDuration >>> 32)) * 31 + (int)(this.periodStartTime ^ this.periodStartTime >>> 32)) * 31 + (int)(this.minimumRetentionDuration ^ this.minimumRetentionDuration >>> 32)) * 31 + (int)(this.scheduleRequestedAt ^ this.scheduleRequestedAt >>> 32);
   }

   public boolean isBackedOff() {
      boolean var1;
      if (this.state == WorkInfo.State.ENQUEUED && this.runAttemptCount > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isPeriodic() {
      boolean var1;
      if (this.intervalDuration != 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("{WorkSpec: ");
      var1.append(this.id);
      var1.append("}");
      return var1.toString();
   }

   public static class IdAndState {
      public String id;
      public WorkInfo.State state;

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            WorkSpec.IdAndState var2 = (WorkSpec.IdAndState)var1;
            return this.state != var2.state ? false : this.id.equals(var2.id);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.id.hashCode() * 31 + this.state.hashCode();
      }
   }

   public static class WorkInfoPojo {
      public String id;
      public Data output;
      public WorkInfo.State state;
      public List tags;

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            WorkSpec.WorkInfoPojo var3;
            label46: {
               var3 = (WorkSpec.WorkInfoPojo)var1;
               if (this.id != null) {
                  if (this.id.equals(var3.id)) {
                     break label46;
                  }
               } else if (var3.id == null) {
                  break label46;
               }

               return false;
            }

            if (this.state != var3.state) {
               return false;
            } else {
               if (this.output != null) {
                  if (!this.output.equals(var3.output)) {
                     return false;
                  }
               } else if (var3.output != null) {
                  return false;
               }

               if (this.tags != null) {
                  var2 = this.tags.equals(var3.tags);
               } else if (var3.tags != null) {
                  var2 = false;
               }

               return var2;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         String var1 = this.id;
         int var2 = 0;
         int var3;
         if (var1 != null) {
            var3 = this.id.hashCode();
         } else {
            var3 = 0;
         }

         int var4;
         if (this.state != null) {
            var4 = this.state.hashCode();
         } else {
            var4 = 0;
         }

         int var5;
         if (this.output != null) {
            var5 = this.output.hashCode();
         } else {
            var5 = 0;
         }

         if (this.tags != null) {
            var2 = this.tags.hashCode();
         }

         return ((var3 * 31 + var4) * 31 + var5) * 31 + var2;
      }

      public WorkInfo toWorkInfo() {
         return new WorkInfo(UUID.fromString(this.id), this.state, this.output, this.tags);
      }
   }
}
