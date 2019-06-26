// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

import java.util.List;
import com.airbnb.lottie.value.LottieValueCallback;

public interface KeyPathElement
{
     <T> void addValueCallback(final T p0, final LottieValueCallback<T> p1);
    
    void resolveKeyPath(final KeyPath p0, final int p1, final List<KeyPath> p2, final KeyPath p3);
}
