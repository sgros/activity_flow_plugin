package androidx.work;

import android.net.Uri;
import java.util.HashSet;
import java.util.Set;

public final class ContentUriTriggers {
   private final Set mTriggers = new HashSet();

   public void add(Uri var1, boolean var2) {
      ContentUriTriggers.Trigger var3 = new ContentUriTriggers.Trigger(var1, var2);
      this.mTriggers.add(var3);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         ContentUriTriggers var2 = (ContentUriTriggers)var1;
         return this.mTriggers.equals(var2.mTriggers);
      } else {
         return false;
      }
   }

   public Set getTriggers() {
      return this.mTriggers;
   }

   public int hashCode() {
      return this.mTriggers.hashCode();
   }

   public int size() {
      return this.mTriggers.size();
   }

   public static final class Trigger {
      private final boolean mTriggerForDescendants;
      private final Uri mUri;

      Trigger(Uri var1, boolean var2) {
         this.mUri = var1;
         this.mTriggerForDescendants = var2;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            ContentUriTriggers.Trigger var3 = (ContentUriTriggers.Trigger)var1;
            if (this.mTriggerForDescendants != var3.mTriggerForDescendants || !this.mUri.equals(var3.mUri)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public Uri getUri() {
         return this.mUri;
      }

      public int hashCode() {
         return this.mUri.hashCode() * 31 + this.mTriggerForDescendants;
      }

      public boolean shouldTriggerForDescendants() {
         return this.mTriggerForDescendants;
      }
   }
}
