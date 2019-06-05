// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.util.pool;

public abstract class StateVerifier
{
    private StateVerifier() {
    }
    
    public static StateVerifier newInstance() {
        return new DefaultStateVerifier();
    }
    
    abstract void setRecycled(final boolean p0);
    
    public abstract void throwIfRecycled();
    
    private static class DefaultStateVerifier extends StateVerifier
    {
        private volatile boolean isReleased;
        
        DefaultStateVerifier() {
            super(null);
        }
        
        public void setRecycled(final boolean isReleased) {
            this.isReleased = isReleased;
        }
        
        @Override
        public void throwIfRecycled() {
            if (!this.isReleased) {
                return;
            }
            throw new IllegalStateException("Already released");
        }
    }
}
