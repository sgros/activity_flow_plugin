// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.Looper;
import android.os.Message;
import java.util.concurrent.CountDownLatch;
import android.os.Handler;

public class DispatchQueue extends Thread
{
    private volatile Handler handler;
    private CountDownLatch syncLatch;
    
    public DispatchQueue(final String name) {
        this.handler = null;
        this.syncLatch = new CountDownLatch(1);
        this.setName(name);
        this.start();
    }
    
    public void cancelRunnable(final Runnable runnable) {
        try {
            this.syncLatch.await();
            this.handler.removeCallbacks(runnable);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void cleanupQueue() {
        try {
            this.syncLatch.await();
            this.handler.removeCallbacksAndMessages((Object)null);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void handleMessage(final Message message) {
    }
    
    public void postRunnable(final Runnable runnable) {
        this.postRunnable(runnable, 0L);
    }
    
    public void postRunnable(final Runnable runnable, final long n) {
        try {
            this.syncLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (n <= 0L) {
            this.handler.post(runnable);
        }
        else {
            this.handler.postDelayed(runnable, n);
        }
    }
    
    public void recycle() {
        this.handler.getLooper().quit();
    }
    
    @Override
    public void run() {
        Looper.prepare();
        this.handler = new Handler() {
            public void handleMessage(final Message message) {
                DispatchQueue.this.handleMessage(message);
            }
        };
        this.syncLatch.countDown();
        Looper.loop();
    }
    
    public void sendMessage(final Message message, final int n) {
        try {
            this.syncLatch.await();
            if (n <= 0) {
                this.handler.sendMessage(message);
            }
            else {
                this.handler.sendMessageDelayed(message, (long)n);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
}
