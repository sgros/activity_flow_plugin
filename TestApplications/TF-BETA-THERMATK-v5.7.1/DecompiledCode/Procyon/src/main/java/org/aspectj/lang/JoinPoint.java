// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.lang;

public interface JoinPoint
{
    Object getTarget();
    
    public interface StaticPart
    {
        String toString();
    }
}
