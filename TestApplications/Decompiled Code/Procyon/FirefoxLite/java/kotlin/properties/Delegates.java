// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.properties;

public final class Delegates
{
    public static final Delegates INSTANCE;
    
    static {
        INSTANCE = new Delegates();
    }
    
    private Delegates() {
    }
    
    public final <T> ReadWriteProperty<Object, T> notNull() {
        return new NotNullVar<T>();
    }
}
