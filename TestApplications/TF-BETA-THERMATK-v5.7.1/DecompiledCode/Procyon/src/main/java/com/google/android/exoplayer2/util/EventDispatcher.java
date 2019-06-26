// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.util.Iterator;
import android.os.Handler;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventDispatcher<T>
{
    private final CopyOnWriteArrayList<HandlerAndListener<T>> listeners;
    
    public EventDispatcher() {
        this.listeners = new CopyOnWriteArrayList<HandlerAndListener<T>>();
    }
    
    public void addListener(final Handler handler, final T t) {
        Assertions.checkArgument(handler != null && t != null);
        this.removeListener(t);
        this.listeners.add(new HandlerAndListener<T>(handler, t));
    }
    
    public void dispatch(final Event<T> event) {
        final Iterator<HandlerAndListener<T>> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().dispatch(event);
        }
    }
    
    public void removeListener(final T t) {
        for (final HandlerAndListener<T> o : this.listeners) {
            if (((HandlerAndListener<Object>)o).listener == t) {
                o.release();
                this.listeners.remove(o);
            }
        }
    }
    
    public interface Event<T>
    {
        void sendTo(final T p0);
    }
    
    private static final class HandlerAndListener<T>
    {
        private final Handler handler;
        private final T listener;
        private boolean released;
        
        public HandlerAndListener(final Handler handler, final T listener) {
            this.handler = handler;
            this.listener = listener;
        }
        
        public void dispatch(final Event<T> event) {
            this.handler.post((Runnable)new _$$Lambda$EventDispatcher$HandlerAndListener$uD_JKgYUi0f_RBL7K02WSc4AoE4(this, event));
        }
        
        public void release() {
            this.released = true;
        }
    }
}
