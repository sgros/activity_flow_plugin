// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Assertions;
import android.os.Handler;

public final class PlayerMessage
{
    private boolean deleteAfterDelivery;
    private Handler handler;
    private boolean isCanceled;
    private boolean isDelivered;
    private boolean isProcessed;
    private boolean isSent;
    private Object payload;
    private long positionMs;
    private final Sender sender;
    private final Target target;
    private final Timeline timeline;
    private int type;
    private int windowIndex;
    
    public PlayerMessage(final Sender sender, final Target target, final Timeline timeline, final int windowIndex, final Handler handler) {
        this.sender = sender;
        this.target = target;
        this.timeline = timeline;
        this.handler = handler;
        this.windowIndex = windowIndex;
        this.positionMs = -9223372036854775807L;
        this.deleteAfterDelivery = true;
    }
    
    public boolean blockUntilDelivered() throws InterruptedException {
        synchronized (this) {
            Assertions.checkState(this.isSent);
            Assertions.checkState(this.handler.getLooper().getThread() != Thread.currentThread());
            while (!this.isProcessed) {
                this.wait();
            }
            return this.isDelivered;
        }
    }
    
    public boolean getDeleteAfterDelivery() {
        return this.deleteAfterDelivery;
    }
    
    public Handler getHandler() {
        return this.handler;
    }
    
    public Object getPayload() {
        return this.payload;
    }
    
    public long getPositionMs() {
        return this.positionMs;
    }
    
    public Target getTarget() {
        return this.target;
    }
    
    public Timeline getTimeline() {
        return this.timeline;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getWindowIndex() {
        return this.windowIndex;
    }
    
    public boolean isCanceled() {
        synchronized (this) {
            return this.isCanceled;
        }
    }
    
    public void markAsProcessed(final boolean b) {
        synchronized (this) {
            this.isDelivered |= b;
            this.isProcessed = true;
            this.notifyAll();
        }
    }
    
    public PlayerMessage send() {
        Assertions.checkState(this.isSent ^ true);
        if (this.positionMs == -9223372036854775807L) {
            Assertions.checkArgument(this.deleteAfterDelivery);
        }
        this.isSent = true;
        this.sender.sendMessage(this);
        return this;
    }
    
    public PlayerMessage setPayload(final Object payload) {
        Assertions.checkState(this.isSent ^ true);
        this.payload = payload;
        return this;
    }
    
    public PlayerMessage setType(final int type) {
        Assertions.checkState(this.isSent ^ true);
        this.type = type;
        return this;
    }
    
    public interface Sender
    {
        void sendMessage(final PlayerMessage p0);
    }
    
    public interface Target
    {
        void handleMessage(final int p0, final Object p1) throws ExoPlaybackException;
    }
}
