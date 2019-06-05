package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;

final class DelimitedRangesSequence implements Sequence {
   private final Function2 getNextMatch;
   private final CharSequence input;
   private final int limit;
   private final int startIndex;

   public DelimitedRangesSequence(CharSequence var1, int var2, int var3, Function2 var4) {
      Intrinsics.checkParameterIsNotNull(var1, "input");
      Intrinsics.checkParameterIsNotNull(var4, "getNextMatch");
      super();
      this.input = var1;
      this.startIndex = var2;
      this.limit = var3;
      this.getNextMatch = var4;
   }

   public Iterator iterator() {
      return (Iterator)(new Iterator() {
         private int counter;
         private int currentStartIndex;
         private IntRange nextItem;
         private int nextSearchIndex;
         private int nextState = -1;

         {
            this.currentStartIndex = RangesKt.coerceIn(DelimitedRangesSequence.this.startIndex, 0, DelimitedRangesSequence.this.input.length());
            this.nextSearchIndex = this.currentStartIndex;
         }

         private final void calcNext() {
            int var1 = this.nextSearchIndex;
            byte var2 = 0;
            if (var1 < 0) {
               this.nextState = 0;
               this.nextItem = (IntRange)null;
            } else {
               label26: {
                  label25: {
                     if (DelimitedRangesSequence.this.limit > 0) {
                        ++this.counter;
                        if (this.counter >= DelimitedRangesSequence.this.limit) {
                           break label25;
                        }
                     }

                     if (this.nextSearchIndex <= DelimitedRangesSequence.this.input.length()) {
                        Pair var3 = (Pair)DelimitedRangesSequence.this.getNextMatch.invoke(DelimitedRangesSequence.this.input, this.nextSearchIndex);
                        if (var3 == null) {
                           this.nextItem = new IntRange(this.currentStartIndex, StringsKt.getLastIndex(DelimitedRangesSequence.this.input));
                           this.nextSearchIndex = -1;
                        } else {
                           var1 = ((Number)var3.component1()).intValue();
                           int var4 = ((Number)var3.component2()).intValue();
                           this.nextItem = RangesKt.until(this.currentStartIndex, var1);
                           this.currentStartIndex = var1 + var4;
                           var1 = this.currentStartIndex;
                           if (var4 == 0) {
                              var2 = 1;
                           }

                           this.nextSearchIndex = var1 + var2;
                        }
                        break label26;
                     }
                  }

                  this.nextItem = new IntRange(this.currentStartIndex, StringsKt.getLastIndex(DelimitedRangesSequence.this.input));
                  this.nextSearchIndex = -1;
               }

               this.nextState = 1;
            }

         }

         public boolean hasNext() {
            if (this.nextState == -1) {
               this.calcNext();
            }

            int var1 = this.nextState;
            boolean var2 = true;
            if (var1 != 1) {
               var2 = false;
            }

            return var2;
         }

         public IntRange next() {
            if (this.nextState == -1) {
               this.calcNext();
            }

            if (this.nextState != 0) {
               IntRange var1 = this.nextItem;
               if (var1 != null) {
                  this.nextItem = (IntRange)null;
                  this.nextState = -1;
                  return var1;
               } else {
                  throw new TypeCastException("null cannot be cast to non-null type kotlin.ranges.IntRange");
               }
            } else {
               throw (Throwable)(new NoSuchElementException());
            }
         }

         public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
         }
      });
   }
}
