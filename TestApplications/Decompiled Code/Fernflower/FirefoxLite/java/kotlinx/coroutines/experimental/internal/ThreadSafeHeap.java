package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

public final class ThreadSafeHeap {
   private ThreadSafeHeapNode[] a;
   public volatile int size;

   private final void siftDownFrom(int var1) {
      int var2 = var1;

      while(true) {
         int var3 = var2 * 2 + 1;
         if (var3 >= this.size) {
            return;
         }

         ThreadSafeHeapNode[] var4 = this.a;
         if (var4 == null) {
            Intrinsics.throwNpe();
         }

         int var5 = var3 + 1;
         var1 = var3;
         ThreadSafeHeapNode var6;
         Comparable var9;
         if (var5 < this.size) {
            var6 = var4[var5];
            if (var6 == null) {
               Intrinsics.throwNpe();
            }

            var9 = (Comparable)var6;
            ThreadSafeHeapNode var7 = var4[var3];
            if (var7 == null) {
               Intrinsics.throwNpe();
            }

            var1 = var3;
            if (var9.compareTo(var7) < 0) {
               var1 = var5;
            }
         }

         var6 = var4[var2];
         if (var6 == null) {
            Intrinsics.throwNpe();
         }

         var9 = (Comparable)var6;
         ThreadSafeHeapNode var8 = var4[var1];
         if (var8 == null) {
            Intrinsics.throwNpe();
         }

         if (var9.compareTo(var8) <= 0) {
            return;
         }

         this.swap(var2, var1);
         var2 = var1;
      }
   }

   private final void siftUpFrom(int var1) {
      while(var1 > 0) {
         ThreadSafeHeapNode[] var2 = this.a;
         if (var2 == null) {
            Intrinsics.throwNpe();
         }

         int var3 = (var1 - 1) / 2;
         ThreadSafeHeapNode var4 = var2[var3];
         if (var4 == null) {
            Intrinsics.throwNpe();
         }

         Comparable var6 = (Comparable)var4;
         ThreadSafeHeapNode var5 = var2[var1];
         if (var5 == null) {
            Intrinsics.throwNpe();
         }

         if (var6.compareTo(var5) <= 0) {
            return;
         }

         this.swap(var1, var3);
         var1 = var3;
      }

   }

   private final void swap(int var1, int var2) {
      ThreadSafeHeapNode[] var3 = this.a;
      if (var3 == null) {
         Intrinsics.throwNpe();
      }

      ThreadSafeHeapNode var4 = var3[var2];
      if (var4 == null) {
         Intrinsics.throwNpe();
      }

      ThreadSafeHeapNode var5 = var3[var1];
      if (var5 == null) {
         Intrinsics.throwNpe();
      }

      var3[var1] = var4;
      var3[var2] = var5;
      var4.setIndex(var1);
      var5.setIndex(var2);
   }

   public final ThreadSafeHeapNode firstImpl() {
      ThreadSafeHeapNode[] var1 = this.a;
      ThreadSafeHeapNode var2;
      if (var1 != null) {
         var2 = var1[0];
      } else {
         var2 = null;
      }

      return var2;
   }

   public final boolean isEmpty() {
      boolean var1;
      if (this.size == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final ThreadSafeHeapNode peek() {
      synchronized(this){}

      ThreadSafeHeapNode var1;
      try {
         var1 = this.firstImpl();
      } finally {
         ;
      }

      return var1;
   }

   public final boolean remove(ThreadSafeHeapNode var1) {
      Intrinsics.checkParameterIsNotNull(var1, "node");
      synchronized(this){}
      boolean var4 = false;

      boolean var2;
      label52: {
         try {
            var4 = true;
            if (var1.getIndex() < 0) {
               var4 = false;
               break label52;
            }

            this.removeAtImpl(var1.getIndex());
            var4 = false;
         } finally {
            if (var4) {
               ;
            }
         }

         var2 = true;
         return var2;
      }

      var2 = false;
      return var2;
   }

   public final ThreadSafeHeapNode removeAtImpl(int var1) {
      boolean var2;
      if (this.size > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         ThreadSafeHeapNode[] var3 = this.a;
         if (var3 == null) {
            Intrinsics.throwNpe();
         }

         --this.size;
         ThreadSafeHeapNode var4;
         if (var1 < this.size) {
            label34: {
               this.swap(var1, this.size);
               int var6 = (var1 - 1) / 2;
               if (var1 > 0) {
                  var4 = var3[var1];
                  if (var4 == null) {
                     Intrinsics.throwNpe();
                  }

                  Comparable var5 = (Comparable)var4;
                  var4 = var3[var6];
                  if (var4 == null) {
                     Intrinsics.throwNpe();
                  }

                  if (var5.compareTo(var4) < 0) {
                     this.swap(var1, var6);
                     this.siftUpFrom(var6);
                     break label34;
                  }
               }

               this.siftDownFrom(var1);
            }
         }

         var4 = var3[this.size];
         if (var4 == null) {
            Intrinsics.throwNpe();
         }

         var4.setIndex(-1);
         var3[this.size] = (ThreadSafeHeapNode)null;
         return var4;
      } else {
         throw (Throwable)(new IllegalStateException("Check failed.".toString()));
      }
   }
}
