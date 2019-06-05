// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.android;

import java.lang.reflect.Modifier;
import kotlin.jvm.internal.Intrinsics;
import java.lang.reflect.Method;

public final class AndroidExceptionPreHandlerKt
{
    private static final Method getter;
    
    static {
        Method getter2 = null;
        final boolean b = false;
        while (true) {
            try {
                final Method declaredMethod = Thread.class.getDeclaredMethod("getUncaughtExceptionPreHandler", (Class<?>[])new Class[0]);
                Intrinsics.checkExpressionValueIsNotNull(declaredMethod, "it");
                int n = b ? 1 : 0;
                if (Modifier.isPublic(declaredMethod.getModifiers())) {
                    final boolean static1 = Modifier.isStatic(declaredMethod.getModifiers());
                    n = (b ? 1 : 0);
                    if (static1) {
                        n = 1;
                    }
                }
                if (n != 0) {
                    getter2 = declaredMethod;
                }
                getter = getter2;
            }
            catch (Throwable t) {
                continue;
            }
            break;
        }
    }
}
