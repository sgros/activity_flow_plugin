// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

final class CompletedIdempotentResult
{
    public final Object result;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CompletedIdempotentResult[");
        sb.append(this.result);
        sb.append(']');
        return sb.toString();
    }
}
