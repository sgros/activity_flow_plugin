package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class TileStates {
   private boolean mDone;
   private int mExpired;
   private int mNotFound;
   private Collection mRunAfters = new LinkedHashSet();
   private int mScaled;
   private int mTotal;
   private int mUpToDate;

   public void finaliseLoop() {
      this.mDone = true;
      Iterator var1 = this.mRunAfters.iterator();

      while(var1.hasNext()) {
         Runnable var2 = (Runnable)var1.next();
         if (var2 != null) {
            var2.run();
         }
      }

   }

   public void handleTile(Drawable var1) {
      ++this.mTotal;
      if (var1 == null) {
         ++this.mNotFound;
      } else {
         int var2 = ExpirableBitmapDrawable.getState(var1);
         if (var2 != -4) {
            if (var2 != -3) {
               if (var2 != -2) {
                  if (var2 != -1) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("Unknown state: ");
                     var3.append(var2);
                     throw new IllegalArgumentException(var3.toString());
                  }

                  ++this.mUpToDate;
               } else {
                  ++this.mExpired;
               }
            } else {
               ++this.mScaled;
            }
         } else {
            ++this.mNotFound;
         }
      }

   }

   public void initialiseLoop() {
      this.mDone = false;
      this.mTotal = 0;
      this.mUpToDate = 0;
      this.mExpired = 0;
      this.mScaled = 0;
      this.mNotFound = 0;
   }

   public String toString() {
      if (this.mDone) {
         StringBuilder var1 = new StringBuilder();
         var1.append("TileStates: ");
         var1.append(this.mTotal);
         var1.append(" = ");
         var1.append(this.mUpToDate);
         var1.append("(U) + ");
         var1.append(this.mExpired);
         var1.append("(E) + ");
         var1.append(this.mScaled);
         var1.append("(S) + ");
         var1.append(this.mNotFound);
         var1.append("(N)");
         return var1.toString();
      } else {
         return "TileStates";
      }
   }
}
