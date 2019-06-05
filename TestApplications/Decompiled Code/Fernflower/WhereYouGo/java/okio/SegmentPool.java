package okio;

final class SegmentPool {
   static final long MAX_SIZE = 65536L;
   static long byteCount;
   static Segment next;

   private SegmentPool() {
   }

   static void recycle(Segment var0) {
      if (var0.next == null && var0.prev == null) {
         if (!var0.shared) {
            synchronized(SegmentPool.class){}

            Throwable var10000;
            boolean var10001;
            label166: {
               try {
                  if (byteCount + 8192L > 65536L) {
                     return;
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label166;
               }

               label160:
               try {
                  byteCount += 8192L;
                  var0.next = next;
                  var0.limit = 0;
                  var0.pos = 0;
                  next = var0;
                  return;
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label160;
               }
            }

            while(true) {
               Throwable var13 = var10000;

               try {
                  throw var13;
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  continue;
               }
            }
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   static Segment take() {
      synchronized(SegmentPool.class){}

      Throwable var10000;
      boolean var10001;
      label158: {
         Segment var13;
         try {
            if (next != null) {
               var13 = next;
               next = var13.next;
               var13.next = null;
               byteCount -= 8192L;
               return var13;
            }
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label158;
         }

         try {
            ;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label158;
         }

         var13 = new Segment();
         return var13;
      }

      while(true) {
         Throwable var0 = var10000;

         try {
            throw var0;
         } catch (Throwable var10) {
            var10000 = var10;
            var10001 = false;
            continue;
         }
      }
   }
}
