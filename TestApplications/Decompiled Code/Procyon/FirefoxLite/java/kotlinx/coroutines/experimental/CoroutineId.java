// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;

final class CoroutineId extends AbstractCoroutineContextElement
{
    public static final Key Key;
    private final long id;
    
    static {
        Key = new Key(null);
    }
    
    public CoroutineId(final long id) {
        super(CoroutineId.Key);
        this.id = id;
    }
    
    public final long getId() {
        return this.id;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CoroutineId(");
        sb.append(this.id);
        sb.append(')');
        return sb.toString();
    }
    
    public static final class Key implements CoroutineContext.Key<CoroutineId>
    {
        private Key() {
        }
    }
}
