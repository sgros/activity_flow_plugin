// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import java.util.Arrays;

public final class LottieResult<V>
{
    private final Throwable exception;
    private final V value;
    
    public LottieResult(final V value) {
        this.value = value;
        this.exception = null;
    }
    
    public LottieResult(final Throwable exception) {
        this.exception = exception;
        this.value = null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LottieResult)) {
            return false;
        }
        final LottieResult lottieResult = (LottieResult)o;
        return (this.getValue() != null && this.getValue().equals(lottieResult.getValue())) || (this.getException() != null && lottieResult.getException() != null && this.getException().toString().equals(this.getException().toString()));
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    public V getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { this.getValue(), this.getException() });
    }
}
