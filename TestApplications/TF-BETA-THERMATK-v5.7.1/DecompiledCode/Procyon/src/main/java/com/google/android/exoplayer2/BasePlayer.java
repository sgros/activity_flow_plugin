// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Util;

public abstract class BasePlayer implements Player
{
    protected final Timeline.Window window;
    
    public BasePlayer() {
        this.window = new Timeline.Window();
    }
    
    public final int getBufferedPercentage() {
        final long bufferedPosition = this.getBufferedPosition();
        final long duration = this.getDuration();
        int constrainValue = 100;
        if (bufferedPosition != -9223372036854775807L && duration != -9223372036854775807L) {
            if (duration != 0L) {
                constrainValue = Util.constrainValue((int)(bufferedPosition * 100L / duration), 0, 100);
            }
        }
        else {
            constrainValue = 0;
        }
        return constrainValue;
    }
    
    public final long getContentDuration() {
        final Timeline currentTimeline = this.getCurrentTimeline();
        long durationMs;
        if (currentTimeline.isEmpty()) {
            durationMs = -9223372036854775807L;
        }
        else {
            durationMs = currentTimeline.getWindow(this.getCurrentWindowIndex(), this.window).getDurationMs();
        }
        return durationMs;
    }
    
    public final void seekTo(final long n) {
        this.seekTo(this.getCurrentWindowIndex(), n);
    }
    
    protected static final class ListenerHolder
    {
        public final EventListener listener;
        private boolean released;
        
        public ListenerHolder(final EventListener listener) {
            this.listener = listener;
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && ListenerHolder.class == o.getClass() && this.listener.equals(((ListenerHolder)o).listener));
        }
        
        @Override
        public int hashCode() {
            return this.listener.hashCode();
        }
        
        public void invoke(final ListenerInvocation listenerInvocation) {
            if (!this.released) {
                listenerInvocation.invokeListener(this.listener);
            }
        }
    }
    
    protected interface ListenerInvocation
    {
        void invokeListener(final EventListener p0);
    }
}
