// 
// Decompiled by Procyon v0.5.34
// 

package com.davemorrissey.labs.subscaleview.decoder;

public class CompatDecoderFactory<T> implements DecoderFactory<T>
{
    private Class<? extends T> clazz;
    
    public CompatDecoderFactory(final Class<? extends T> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public T make() throws IllegalAccessException, InstantiationException {
        return (T)this.clazz.newInstance();
    }
}
