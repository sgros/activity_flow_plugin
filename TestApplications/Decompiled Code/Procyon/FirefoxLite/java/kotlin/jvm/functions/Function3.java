// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.jvm.functions;

import kotlin.Function;

public interface Function3<P1, P2, P3, R> extends Function<R>
{
    R invoke(final P1 p0, final P2 p1, final P3 p2);
}
