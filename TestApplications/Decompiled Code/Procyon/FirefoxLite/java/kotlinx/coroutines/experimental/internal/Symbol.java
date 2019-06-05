// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.internal;

import kotlin.jvm.internal.Intrinsics;

public final class Symbol
{
    private final String symbol;
    
    public Symbol(final String symbol) {
        Intrinsics.checkParameterIsNotNull(symbol, "symbol");
        this.symbol = symbol;
    }
    
    @Override
    public String toString() {
        return this.symbol;
    }
}
