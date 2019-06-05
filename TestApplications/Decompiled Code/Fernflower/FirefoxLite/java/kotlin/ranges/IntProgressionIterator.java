package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.collections.IntIterator;

public final class IntProgressionIterator extends IntIterator {
   private final int finalElement;
   private boolean hasNext;
   private int next;
   private final int step;

   public IntProgressionIterator(int var1, int var2, int var3) {
      boolean var4;
      label19: {
         super();
         this.step = var3;
         this.finalElement = var2;
         var3 = this.step;
         var4 = false;
         if (var3 > 0) {
            if (var1 > var2) {
               break label19;
            }
         } else if (var1 < var2) {
            break label19;
         }

         var4 = true;
      }

      this.hasNext = var4;
      if (!this.hasNext) {
         var1 = this.finalElement;
      }

      this.next = var1;
   }

   public boolean hasNext() {
      return this.hasNext;
   }

   public int nextInt() {
      int var1 = this.next;
      if (var1 == this.finalElement) {
         if (!this.hasNext) {
            throw (Throwable)(new NoSuchElementException());
         }

         this.hasNext = false;
      } else {
         this.next += this.step;
      }

      return var1;
   }
}
