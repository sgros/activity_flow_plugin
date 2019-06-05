package kotlin.io;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

final class LinesSequence implements Sequence {
   private final BufferedReader reader;

   public LinesSequence(BufferedReader var1) {
      Intrinsics.checkParameterIsNotNull(var1, "reader");
      super();
      this.reader = var1;
   }

   public Iterator iterator() {
      return (Iterator)(new Iterator() {
         private boolean done;
         private String nextValue;

         public boolean hasNext() {
            String var1 = this.nextValue;
            boolean var2 = true;
            if (var1 == null && !this.done) {
               this.nextValue = LinesSequence.this.reader.readLine();
               if (this.nextValue == null) {
                  this.done = true;
               }
            }

            if (this.nextValue == null) {
               var2 = false;
            }

            return var2;
         }

         public String next() {
            if (this.hasNext()) {
               String var1 = this.nextValue;
               this.nextValue = (String)null;
               if (var1 == null) {
                  Intrinsics.throwNpe();
               }

               return var1;
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
