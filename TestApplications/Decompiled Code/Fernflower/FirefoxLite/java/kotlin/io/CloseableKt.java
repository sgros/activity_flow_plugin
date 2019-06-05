package kotlin.io;

import java.io.Closeable;
import kotlin.ExceptionsKt;

public final class CloseableKt {
   public static final void closeFinally(Closeable var0, Throwable var1) {
      if (var0 != null) {
         if (var1 == null) {
            var0.close();
         } else {
            try {
               var0.close();
            } catch (Throwable var2) {
               ExceptionsKt.addSuppressed(var1, var2);
            }
         }
      }

   }
}
