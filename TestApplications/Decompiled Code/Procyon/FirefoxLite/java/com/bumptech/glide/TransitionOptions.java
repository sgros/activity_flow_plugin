// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide;

import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.TransitionFactory;

public abstract class TransitionOptions<CHILD extends TransitionOptions<CHILD, TranscodeType>, TranscodeType> implements Cloneable
{
    private TransitionFactory<? super TranscodeType> transitionFactory;
    
    public TransitionOptions() {
        this.transitionFactory = NoTransition.getFactory();
    }
    
    @Override
    protected final CHILD clone() {
        try {
            return (CHILD)super.clone();
        }
        catch (CloneNotSupportedException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    final TransitionFactory<? super TranscodeType> getTransitionFactory() {
        return this.transitionFactory;
    }
}
