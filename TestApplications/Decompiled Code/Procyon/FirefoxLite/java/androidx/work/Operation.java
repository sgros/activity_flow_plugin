// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import android.annotation.SuppressLint;

public interface Operation
{
    @SuppressLint({ "SyntheticAccessor" })
    public static final IN_PROGRESS IN_PROGRESS = new IN_PROGRESS();
    @SuppressLint({ "SyntheticAccessor" })
    public static final SUCCESS SUCCESS = new SUCCESS();
    
    public abstract static class State
    {
        State() {
        }
        
        public static final class FAILURE extends State
        {
            private final Throwable mThrowable;
            
            public FAILURE(final Throwable mThrowable) {
                this.mThrowable = mThrowable;
            }
            
            public Throwable getThrowable() {
                return this.mThrowable;
            }
            
            @Override
            public String toString() {
                return String.format("FAILURE (%s)", this.mThrowable.getMessage());
            }
        }
        
        public static final class IN_PROGRESS extends State
        {
            private IN_PROGRESS() {
            }
            
            @Override
            public String toString() {
                return "IN_PROGRESS";
            }
        }
        
        public static final class SUCCESS extends State
        {
            private SUCCESS() {
            }
            
            @Override
            public String toString() {
                return "SUCCESS";
            }
        }
    }
}
