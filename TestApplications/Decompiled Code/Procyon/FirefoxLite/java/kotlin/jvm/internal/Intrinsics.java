// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.UninitializedPropertyAccessException;
import kotlin.KotlinNullPointerException;
import java.util.List;
import java.util.Arrays;

public class Intrinsics
{
    private Intrinsics() {
    }
    
    public static boolean areEqual(final Object o, final Object obj) {
        boolean equals;
        if (o == null) {
            equals = (obj == null);
        }
        else {
            equals = o.equals(obj);
        }
        return equals;
    }
    
    public static void checkExpressionValueIsNotNull(final Object o, final String str) {
        if (o != null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" must not be null");
        throw sanitizeStackTrace(new IllegalStateException(sb.toString()));
    }
    
    public static void checkParameterIsNotNull(final Object o, final String s) {
        if (o == null) {
            throwParameterIsNullException(s);
        }
    }
    
    private static <T extends Throwable> T sanitizeStackTrace(final T t) {
        return sanitizeStackTrace(t, Intrinsics.class.getName());
    }
    
    static <T extends Throwable> T sanitizeStackTrace(final T t, final String s) {
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final int length = stackTrace.length;
        int n = -1;
        for (int i = 0; i < length; ++i) {
            if (s.equals(stackTrace[i].getClassName())) {
                n = i;
            }
        }
        final List<StackTraceElement> subList = Arrays.asList(stackTrace).subList(n + 1, length);
        t.setStackTrace(subList.toArray(new StackTraceElement[subList.size()]));
        return t;
    }
    
    public static void throwNpe() {
        throw sanitizeStackTrace(new KotlinNullPointerException());
    }
    
    private static void throwParameterIsNullException(final String str) {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        final String className = stackTraceElement.getClassName();
        final String methodName = stackTraceElement.getMethodName();
        final StringBuilder sb = new StringBuilder();
        sb.append("Parameter specified as non-null is null: method ");
        sb.append(className);
        sb.append(".");
        sb.append(methodName);
        sb.append(", parameter ");
        sb.append(str);
        throw sanitizeStackTrace(new IllegalArgumentException(sb.toString()));
    }
    
    public static void throwUninitializedProperty(final String s) {
        throw sanitizeStackTrace(new UninitializedPropertyAccessException(s));
    }
    
    public static void throwUninitializedPropertyAccessException(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("lateinit property ");
        sb.append(str);
        sb.append(" has not been initialized");
        throwUninitializedProperty(sb.toString());
    }
}
