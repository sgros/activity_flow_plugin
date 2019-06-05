// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;

public final class CoroutineName extends AbstractCoroutineContextElement
{
    public static final Key Key;
    private final String name;
    
    static {
        Key = new Key(null);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof CoroutineName && Intrinsics.areEqual(this.name, ((CoroutineName)o).name));
    }
    
    public final String getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        final String name = this.name;
        int hashCode;
        if (name != null) {
            hashCode = name.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CoroutineName(");
        sb.append(this.name);
        sb.append(')');
        return sb.toString();
    }
    
    public static final class Key implements CoroutineContext.Key<CoroutineName>
    {
        private Key() {
        }
    }
}
