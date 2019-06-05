// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class NoTransition<R> implements Transition<R>
{
    static final NoTransition<?> NO_ANIMATION;
    private static final TransitionFactory<?> NO_ANIMATION_FACTORY;
    
    static {
        NO_ANIMATION = new NoTransition<Object>();
        NO_ANIMATION_FACTORY = new NoAnimationFactory<Object>();
    }
    
    public static <R> TransitionFactory<R> getFactory() {
        return (TransitionFactory<R>)NoTransition.NO_ANIMATION_FACTORY;
    }
    
    @Override
    public boolean transition(final Object o, final ViewAdapter viewAdapter) {
        return false;
    }
    
    public static class NoAnimationFactory<R> implements TransitionFactory<R>
    {
        @Override
        public Transition<R> build(final DataSource dataSource, final boolean b) {
            return (Transition<R>)NoTransition.NO_ANIMATION;
        }
    }
}
