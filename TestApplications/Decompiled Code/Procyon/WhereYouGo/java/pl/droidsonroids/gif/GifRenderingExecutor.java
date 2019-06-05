// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

final class GifRenderingExecutor extends ScheduledThreadPoolExecutor
{
    private GifRenderingExecutor() {
        super(1, new DiscardPolicy());
    }
    
    static GifRenderingExecutor getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    private static final class InstanceHolder
    {
        private static final GifRenderingExecutor INSTANCE;
        
        static {
            INSTANCE = new GifRenderingExecutor(null);
        }
    }
}
