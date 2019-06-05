package pl.droidsonroids.gif;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.support.annotation.RequiresApi;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;

@RequiresApi(14)
class PlaceholderDrawingSurfaceTextureListener implements SurfaceTextureListener {
   private final GifTextureView.PlaceholderDrawListener mDrawer;

   PlaceholderDrawingSurfaceTextureListener(GifTextureView.PlaceholderDrawListener var1) {
      this.mDrawer = var1;
   }

   public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
      Surface var5 = new Surface(var1);
      Canvas var4 = var5.lockCanvas((Rect)null);
      this.mDrawer.onDrawPlaceholder(var4);
      var5.unlockCanvasAndPost(var4);
      var5.release();
   }

   public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
      return false;
   }

   public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
   }

   public void onSurfaceTextureUpdated(SurfaceTexture var1) {
   }
}
