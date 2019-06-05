package kotlin.ranges;

import kotlin.collections.IntIterator;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;

public class IntProgression implements Iterable, KMappedMarker {
   public static final IntProgression.Companion Companion = new IntProgression.Companion((DefaultConstructorMarker)null);
   private final int first;
   private final int last;
   private final int step;

   public IntProgression(int var1, int var2, int var3) {
      if (var3 != 0) {
         if (var3 != Integer.MIN_VALUE) {
            this.first = var1;
            this.last = ProgressionUtilKt.getProgressionLastElement(var1, var2, var3);
            this.step = var3;
         } else {
            throw (Throwable)(new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation."));
         }
      } else {
         throw (Throwable)(new IllegalArgumentException("Step must be non-zero."));
      }
   }

   public boolean equals(Object var1) {
      boolean var3;
      label29: {
         if (var1 instanceof IntProgression) {
            if (this.isEmpty() && ((IntProgression)var1).isEmpty()) {
               break label29;
            }

            int var2 = this.first;
            IntProgression var4 = (IntProgression)var1;
            if (var2 == var4.first && this.last == var4.last && this.step == var4.step) {
               break label29;
            }
         }

         var3 = false;
         return var3;
      }

      var3 = true;
      return var3;
   }

   public final int getFirst() {
      return this.first;
   }

   public final int getLast() {
      return this.last;
   }

   public final int getStep() {
      return this.step;
   }

   public int hashCode() {
      int var1;
      if (this.isEmpty()) {
         var1 = -1;
      } else {
         var1 = (this.first * 31 + this.last) * 31 + this.step;
      }

      return var1;
   }

   public boolean isEmpty() {
      int var1 = this.step;
      boolean var2 = false;
      if (var1 > 0) {
         if (this.first <= this.last) {
            return var2;
         }
      } else if (this.first >= this.last) {
         return var2;
      }

      var2 = true;
      return var2;
   }

   public IntIterator iterator() {
      return (IntIterator)(new IntProgressionIterator(this.first, this.last, this.step));
   }

   public String toString() {
      StringBuilder var1;
      int var2;
      if (this.step > 0) {
         var1 = new StringBuilder();
         var1.append(this.first);
         var1.append("..");
         var1.append(this.last);
         var1.append(" step ");
         var2 = this.step;
      } else {
         var1 = new StringBuilder();
         var1.append(this.first);
         var1.append(" downTo ");
         var1.append(this.last);
         var1.append(" step ");
         var2 = -this.step;
      }

      var1.append(var2);
      String var3 = var1.toString();
      return var3;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final IntProgression fromClosedRange(int var1, int var2, int var3) {
         return new IntProgression(var1, var2, var3);
      }
   }
}
