// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview.decoder;

public interface DecoderFactory<T>
{
    T make() throws IllegalAccessException, InstantiationException;
}
