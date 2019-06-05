// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

public final class JobSupportKt
{
    private static final Empty EmptyActive;
    private static final Empty EmptyNew;
    
    static {
        EmptyNew = new Empty(false);
        EmptyActive = new Empty(true);
    }
}
