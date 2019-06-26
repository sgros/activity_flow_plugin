// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;
import android.graphics.SurfaceTexture;
import android.support.annotation.RequiresApi;
import android.view.TextureView$SurfaceTextureListener;

@RequiresApi(14)
class PlaceholderDrawingSurfaceTextureListener implements TextureView$SurfaceTextureListener
{
    private final GifTextureView.PlaceholderDrawListener mDrawer;
    
    PlaceholderDrawingSurfaceTextureListener(final GifTextureView.PlaceholderDrawListener mDrawer) {
        this.mDrawer = mDrawer;
    }
    
    public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
        final Surface surface = new Surface(surfaceTexture);
        final Canvas lockCanvas = surface.lockCanvas((Rect)null);
        this.mDrawer.onDrawPlaceholder(lockCanvas);
        surface.unlockCanvasAndPost(lockCanvas);
        surface.release();
    }
    
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
        return false;
    }
    
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
    }
    
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }
}
