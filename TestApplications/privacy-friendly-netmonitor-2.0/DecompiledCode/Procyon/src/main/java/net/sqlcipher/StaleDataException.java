// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

public class StaleDataException extends RuntimeException
{
    public StaleDataException() {
    }
    
    public StaleDataException(final String message) {
        super(message);
    }
}
