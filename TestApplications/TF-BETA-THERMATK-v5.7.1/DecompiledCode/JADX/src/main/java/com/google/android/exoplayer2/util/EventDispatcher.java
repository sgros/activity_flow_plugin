package com.google.android.exoplayer2.util;

import android.os.Handler;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventDispatcher<T> {
    private final CopyOnWriteArrayList<HandlerAndListener<T>> listeners = new CopyOnWriteArrayList();

    public interface Event<T> {
        void sendTo(T t);
    }

    private static final class HandlerAndListener<T> {
        private final Handler handler;
        private final T listener;
        private boolean released;

        public HandlerAndListener(Handler handler, T t) {
            this.handler = handler;
            this.listener = t;
        }

        public void release() {
            this.released = true;
        }

        public void dispatch(Event<T> event) {
            this.handler.post(new C0228x1ab2001a(this, event));
        }

        public /* synthetic */ void lambda$dispatch$0$EventDispatcher$HandlerAndListener(Event event) {
            if (!this.released) {
                event.sendTo(this.listener);
            }
        }
    }

    public void addListener(Handler handler, T t) {
        boolean z = (handler == null || t == null) ? false : true;
        Assertions.checkArgument(z);
        removeListener(t);
        this.listeners.add(new HandlerAndListener(handler, t));
    }

    public void removeListener(T t) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            HandlerAndListener handlerAndListener = (HandlerAndListener) it.next();
            if (handlerAndListener.listener == t) {
                handlerAndListener.release();
                this.listeners.remove(handlerAndListener);
            }
        }
    }

    public void dispatch(Event<T> event) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((HandlerAndListener) it.next()).dispatch(event);
        }
    }
}
