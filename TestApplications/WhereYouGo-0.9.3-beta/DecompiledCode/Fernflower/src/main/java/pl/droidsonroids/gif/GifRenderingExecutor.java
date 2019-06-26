package pl.droidsonroids.gif;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

final class GifRenderingExecutor extends ScheduledThreadPoolExecutor {
   private GifRenderingExecutor() {
      super(1, new DiscardPolicy());
   }

   // $FF: synthetic method
   GifRenderingExecutor(Object var1) {
      this();
   }

   static GifRenderingExecutor getInstance() {
      return GifRenderingExecutor.InstanceHolder.INSTANCE;
   }

   private static final class InstanceHolder {
      private static final GifRenderingExecutor INSTANCE = new GifRenderingExecutor();
   }
}
