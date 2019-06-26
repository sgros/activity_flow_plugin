package p008pl.droidsonroids.gif;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

/* renamed from: pl.droidsonroids.gif.GifRenderingExecutor */
final class GifRenderingExecutor extends ScheduledThreadPoolExecutor {

    /* renamed from: pl.droidsonroids.gif.GifRenderingExecutor$InstanceHolder */
    private static final class InstanceHolder {
        private static final GifRenderingExecutor INSTANCE = new GifRenderingExecutor();

        private InstanceHolder() {
        }
    }

    static GifRenderingExecutor getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private GifRenderingExecutor() {
        super(1, new DiscardPolicy());
    }
}
