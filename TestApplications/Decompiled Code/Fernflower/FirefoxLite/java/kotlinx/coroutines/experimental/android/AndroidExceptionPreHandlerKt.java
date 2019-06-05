package kotlinx.coroutines.experimental.android;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import kotlin.jvm.internal.Intrinsics;

public final class AndroidExceptionPreHandlerKt {
   private static final Method getter;

   static {
      Method var0 = null;
      boolean var1 = false;

      label34: {
         Method var2;
         boolean var3;
         label39: {
            boolean var10001;
            try {
               var2 = Thread.class.getDeclaredMethod("getUncaughtExceptionPreHandler");
               Intrinsics.checkExpressionValueIsNotNull(var2, "it");
            } catch (Throwable var6) {
               var10001 = false;
               break label34;
            }

            var3 = var1;

            boolean var4;
            try {
               if (!Modifier.isPublic(var2.getModifiers())) {
                  break label39;
               }

               var4 = Modifier.isStatic(var2.getModifiers());
            } catch (Throwable var5) {
               var10001 = false;
               break label34;
            }

            var3 = var1;
            if (var4) {
               var3 = true;
            }
         }

         if (var3) {
            var0 = var2;
         }
      }

      getter = var0;
   }

   // $FF: synthetic method
   public static final Method access$getGetter$p() {
      return getter;
   }
}
