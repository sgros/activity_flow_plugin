package kotlinx.coroutines.experimental.android;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AndroidExceptionPreHandler.kt */
public final class AndroidExceptionPreHandlerKt {
    private static final Method getter;

    static {
        Method method = null;
        try {
            int i = 0;
            Method declaredMethod = Thread.class.getDeclaredMethod("getUncaughtExceptionPreHandler", new Class[0]);
            Intrinsics.checkExpressionValueIsNotNull(declaredMethod, "it");
            if (Modifier.isPublic(declaredMethod.getModifiers()) && Modifier.isStatic(declaredMethod.getModifiers())) {
                i = 1;
            }
            if (i != 0) {
                method = declaredMethod;
            }
        } catch (Throwable unused) {
        }
        getter = method;
    }
}
