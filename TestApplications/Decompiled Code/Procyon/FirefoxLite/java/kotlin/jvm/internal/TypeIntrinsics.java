// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.internal;

import kotlin.Function;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.markers.KMutableIterable;
import kotlin.jvm.internal.markers.KMappedMarker;

public class TypeIntrinsics
{
    public static Iterable asMutableIterable(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableIterable)) {
            throwCce(o, "kotlin.collections.MutableIterable");
        }
        return castToIterable(o);
    }
    
    public static Object beforeCheckcastToFunctionOfArity(final Object o, final int i) {
        if (o != null && !isFunctionOfArity(o, i)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("kotlin.jvm.functions.Function");
            sb.append(i);
            throwCce(o, sb.toString());
        }
        return o;
    }
    
    public static Iterable castToIterable(final Object o) {
        try {
            return (Iterable)o;
        }
        catch (ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static int getFunctionArity(final Object o) {
        if (o instanceof FunctionBase) {
            return ((FunctionBase)o).getArity();
        }
        if (o instanceof Function0) {
            return 0;
        }
        if (o instanceof Function1) {
            return 1;
        }
        if (o instanceof Function2) {
            return 2;
        }
        if (o instanceof Function3) {
            return 3;
        }
        if (o instanceof Function4) {
            return 4;
        }
        if (o instanceof Function5) {
            return 5;
        }
        if (o instanceof Function6) {
            return 6;
        }
        if (o instanceof Function7) {
            return 7;
        }
        if (o instanceof Function8) {
            return 8;
        }
        if (o instanceof Function9) {
            return 9;
        }
        if (o instanceof Function10) {
            return 10;
        }
        if (o instanceof Function11) {
            return 11;
        }
        if (o instanceof Function12) {
            return 12;
        }
        if (o instanceof Function13) {
            return 13;
        }
        if (o instanceof Function14) {
            return 14;
        }
        if (o instanceof Function15) {
            return 15;
        }
        if (o instanceof Function16) {
            return 16;
        }
        if (o instanceof Function17) {
            return 17;
        }
        if (o instanceof Function18) {
            return 18;
        }
        if (o instanceof Function19) {
            return 19;
        }
        if (o instanceof Function20) {
            return 20;
        }
        if (o instanceof Function21) {
            return 21;
        }
        if (o instanceof Function22) {
            return 22;
        }
        return -1;
    }
    
    public static boolean isFunctionOfArity(final Object o, final int n) {
        return o instanceof Function && getFunctionArity(o) == n;
    }
    
    private static <T extends Throwable> T sanitizeStackTrace(final T t) {
        return Intrinsics.sanitizeStackTrace(t, TypeIntrinsics.class.getName());
    }
    
    public static ClassCastException throwCce(final ClassCastException ex) {
        throw sanitizeStackTrace(ex);
    }
    
    public static void throwCce(final Object o, final String str) {
        String name;
        if (o == null) {
            name = "null";
        }
        else {
            name = o.getClass().getName();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" cannot be cast to ");
        sb.append(str);
        throwCce(sb.toString());
    }
    
    public static void throwCce(final String s) {
        throw throwCce(new ClassCastException(s));
    }
}
