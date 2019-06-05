package p008pl.droidsonroids.gif;

import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.support.annotation.RequiresApi;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import p008pl.droidsonroids.gif.GifTextureView.PlaceholderDrawListener;

@RequiresApi(14)
/* renamed from: pl.droidsonroids.gif.PlaceholderDrawingSurfaceTextureListener */
class PlaceholderDrawingSurfaceTextureListener implements SurfaceTextureListener {
    private final PlaceholderDrawListener mDrawer;

    PlaceholderDrawingSurfaceTextureListener(PlaceholderDrawListener drawer) {
        this.mDrawer = drawer;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        Canvas canvas = surface.lockCanvas(null);
        this.mDrawer.onDrawPlaceholder(canvas);
        surface.unlockCanvasAndPost(canvas);
        surface.release();
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }
}
