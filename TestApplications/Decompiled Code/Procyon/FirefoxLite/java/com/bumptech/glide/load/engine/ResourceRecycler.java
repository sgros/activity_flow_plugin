// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import android.os.Message;
import com.bumptech.glide.util.Util;
import android.os.Handler$Callback;
import android.os.Looper;
import android.os.Handler;

class ResourceRecycler
{
    private final Handler handler;
    private boolean isRecycling;
    
    ResourceRecycler() {
        this.handler = new Handler(Looper.getMainLooper(), (Handler$Callback)new ResourceRecyclerCallback());
    }
    
    public void recycle(final Resource<?> resource) {
        Util.assertMainThread();
        if (this.isRecycling) {
            this.handler.obtainMessage(1, (Object)resource).sendToTarget();
        }
        else {
            this.isRecycling = true;
            resource.recycle();
            this.isRecycling = false;
        }
    }
    
    private static class ResourceRecyclerCallback implements Handler$Callback
    {
        ResourceRecyclerCallback() {
        }
        
        public boolean handleMessage(final Message message) {
            if (message.what == 1) {
                ((Resource)message.obj).recycle();
                return true;
            }
            return false;
        }
    }
}
