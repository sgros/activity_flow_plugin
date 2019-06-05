package androidx.work;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class WorkInfo {
   private UUID mId;
   private Data mOutputData;
   private WorkInfo.State mState;
   private Set mTags;

   public WorkInfo(UUID var1, WorkInfo.State var2, Data var3, List var4) {
      this.mId = var1;
      this.mState = var2;
      this.mOutputData = var3;
      this.mTags = new HashSet(var4);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         WorkInfo var3;
         label46: {
            var3 = (WorkInfo)var1;
            if (this.mId != null) {
               if (this.mId.equals(var3.mId)) {
                  break label46;
               }
            } else if (var3.mId == null) {
               break label46;
            }

            return false;
         }

         if (this.mState != var3.mState) {
            return false;
         } else {
            if (this.mOutputData != null) {
               if (!this.mOutputData.equals(var3.mOutputData)) {
                  return false;
               }
            } else if (var3.mOutputData != null) {
               return false;
            }

            if (this.mTags != null) {
               var2 = this.mTags.equals(var3.mTags);
            } else if (var3.mTags != null) {
               var2 = false;
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   public WorkInfo.State getState() {
      return this.mState;
   }

   public int hashCode() {
      UUID var1 = this.mId;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = this.mId.hashCode();
      } else {
         var3 = 0;
      }

      int var4;
      if (this.mState != null) {
         var4 = this.mState.hashCode();
      } else {
         var4 = 0;
      }

      int var5;
      if (this.mOutputData != null) {
         var5 = this.mOutputData.hashCode();
      } else {
         var5 = 0;
      }

      if (this.mTags != null) {
         var2 = this.mTags.hashCode();
      }

      return ((var3 * 31 + var4) * 31 + var5) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("WorkInfo{mId='");
      var1.append(this.mId);
      var1.append('\'');
      var1.append(", mState=");
      var1.append(this.mState);
      var1.append(", mOutputData=");
      var1.append(this.mOutputData);
      var1.append(", mTags=");
      var1.append(this.mTags);
      var1.append('}');
      return var1.toString();
   }

   public static enum State {
      BLOCKED,
      CANCELLED,
      ENQUEUED,
      FAILED,
      RUNNING,
      SUCCEEDED;

      public boolean isFinished() {
         boolean var1;
         if (this != SUCCEEDED && this != FAILED && this != CANCELLED) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }
   }
}
