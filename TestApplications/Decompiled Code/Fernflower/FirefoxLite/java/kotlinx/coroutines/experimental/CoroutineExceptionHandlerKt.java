package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class CoroutineExceptionHandlerKt {
   public static final void handleCoroutineException(CoroutineContext var0, Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      Intrinsics.checkParameterIsNotNull(var1, "exception");

      Throwable var10000;
      label60: {
         CoroutineExceptionHandler var2;
         boolean var10001;
         try {
            var2 = (CoroutineExceptionHandler)var0.get((CoroutineContext.Key)CoroutineExceptionHandler.Key);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label60;
         }

         if (var2 != null) {
            try {
               var2.handleException(var0, var1);
               return;
            } catch (Throwable var3) {
               var10000 = var3;
               var10001 = false;
            }
         } else {
            label61: {
               try {
                  if (var1 instanceof CancellationException) {
                     return;
                  }
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label61;
               }

               Job var10;
               try {
                  var10 = (Job)var0.get((CoroutineContext.Key)Job.Key);
               } catch (Throwable var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label61;
               }

               if (var10 != null) {
                  try {
                     var10.cancel(var1);
                  } catch (Throwable var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label61;
                  }
               }

               try {
                  CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(var0, var1);
                  return;
               } catch (Throwable var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }
         }
      }

      Throwable var9 = var10000;
      if (var9 == var1) {
         throw var1;
      } else {
         var1 = (Throwable)(new RuntimeException("Exception while trying to handle coroutine exception", var1));
         ExceptionsKt.addSuppressed(var1, var9);
         throw var1;
      }
   }
}
