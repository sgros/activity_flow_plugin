// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.lang;

public class NoAspectBoundException extends RuntimeException
{
    Throwable cause;
    
    public NoAspectBoundException(String string, final Throwable t) {
        if (t != null) {
            final StringBuffer sb = new StringBuffer();
            sb.append("Exception while initializing ");
            sb.append(string);
            sb.append(": ");
            sb.append(t);
            string = sb.toString();
        }
        super(string);
        this.cause = t;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
