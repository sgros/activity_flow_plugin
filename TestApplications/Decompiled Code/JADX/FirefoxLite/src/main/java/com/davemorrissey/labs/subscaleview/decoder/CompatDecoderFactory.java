package com.davemorrissey.labs.subscaleview.decoder;

public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    private Class<? extends T> clazz;

    public CompatDecoderFactory(Class<? extends T> cls) {
        this.clazz = cls;
    }

    public T make() throws IllegalAccessException, InstantiationException {
        return this.clazz.newInstance();
    }
}
