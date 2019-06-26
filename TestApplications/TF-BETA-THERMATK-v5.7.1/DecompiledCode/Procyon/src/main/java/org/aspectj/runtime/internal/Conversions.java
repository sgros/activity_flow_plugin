// 
// Decompiled by Procyon v0.5.34
// 

package org.aspectj.runtime.internal;

public final class Conversions
{
    public static Object booleanObject(final boolean value) {
        return new Boolean(value);
    }
    
    public static Object doubleObject(final double value) {
        return new Double(value);
    }
    
    public static Object floatObject(final float value) {
        return new Float(value);
    }
    
    public static Object intObject(final int value) {
        return new Integer(value);
    }
    
    public static Object longObject(final long value) {
        return new Long(value);
    }
}
