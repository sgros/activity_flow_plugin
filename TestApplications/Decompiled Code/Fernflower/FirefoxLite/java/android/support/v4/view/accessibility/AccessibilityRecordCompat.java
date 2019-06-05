package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityRecord;

public class AccessibilityRecordCompat {
   private final AccessibilityRecord mRecord;

   public static void setMaxScrollX(AccessibilityRecord var0, int var1) {
      if (VERSION.SDK_INT >= 15) {
         var0.setMaxScrollX(var1);
      }

   }

   public static void setMaxScrollY(AccessibilityRecord var0, int var1) {
      if (VERSION.SDK_INT >= 15) {
         var0.setMaxScrollY(var1);
      }

   }

   @Deprecated
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AccessibilityRecordCompat var2 = (AccessibilityRecordCompat)var1;
         if (this.mRecord == null) {
            if (var2.mRecord != null) {
               return false;
            }
         } else if (!this.mRecord.equals(var2.mRecord)) {
            return false;
         }

         return true;
      }
   }

   @Deprecated
   public int hashCode() {
      int var1;
      if (this.mRecord == null) {
         var1 = 0;
      } else {
         var1 = this.mRecord.hashCode();
      }

      return var1;
   }
}
