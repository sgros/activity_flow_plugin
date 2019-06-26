// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.ActionBar;
import android.graphics.Shader$TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

public class SizeNotifierFrameLayout extends FrameLayout
{
    private Drawable backgroundDrawable;
    private int bottomClip;
    private SizeNotifierFrameLayoutDelegate delegate;
    private int keyboardHeight;
    private boolean occupyStatusBar;
    private WallpaperParallaxEffect parallaxEffect;
    private float parallaxScale;
    private boolean paused;
    private Rect rect;
    private float translationX;
    private float translationY;
    
    public SizeNotifierFrameLayout(final Context context) {
        super(context);
        this.rect = new Rect();
        this.occupyStatusBar = true;
        this.parallaxScale = 1.0f;
        this.paused = true;
        this.setWillNotDraw(false);
    }
    
    public Drawable getBackgroundImage() {
        return this.backgroundDrawable;
    }
    
    public int getKeyboardHeight() {
        final View rootView = this.getRootView();
        this.getWindowVisibleDisplayFrame(this.rect);
        final int height = rootView.getHeight();
        int statusBarHeight;
        if (this.rect.top != 0) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final int viewInset = AndroidUtilities.getViewInset(rootView);
        final Rect rect = this.rect;
        return height - statusBarHeight - viewInset - (rect.bottom - rect.top);
    }
    
    protected boolean isActionBarVisible() {
        return true;
    }
    
    public void notifyHeightChanged() {
        if (this.delegate != null) {
            final WallpaperParallaxEffect parallaxEffect = this.parallaxEffect;
            if (parallaxEffect != null) {
                this.parallaxScale = parallaxEffect.getScale(this.getMeasuredWidth(), this.getMeasuredHeight());
            }
            this.keyboardHeight = this.getKeyboardHeight();
            final Point displaySize = AndroidUtilities.displaySize;
            this.post((Runnable)new _$$Lambda$SizeNotifierFrameLayout$k4g_DFX6SvnMtnN4qrfdhEQSo18(this, displaySize.x > displaySize.y));
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        final Drawable backgroundDrawable = this.backgroundDrawable;
        if (backgroundDrawable != null) {
            if (backgroundDrawable instanceof ColorDrawable) {
                if (this.bottomClip != 0) {
                    canvas.save();
                    canvas.clipRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight() - this.bottomClip);
                }
                this.backgroundDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                this.backgroundDrawable.draw(canvas);
                if (this.bottomClip != 0) {
                    canvas.restore();
                }
            }
            else if (backgroundDrawable instanceof BitmapDrawable) {
                if (((BitmapDrawable)backgroundDrawable).getTileModeX() == Shader$TileMode.REPEAT) {
                    canvas.save();
                    final float n = 2.0f / AndroidUtilities.density;
                    canvas.scale(n, n);
                    this.backgroundDrawable.setBounds(0, 0, (int)Math.ceil(this.getMeasuredWidth() / n), (int)Math.ceil(this.getMeasuredHeight() / n));
                    this.backgroundDrawable.draw(canvas);
                    canvas.restore();
                }
                else {
                    int currentActionBarHeight;
                    if (this.isActionBarVisible()) {
                        currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                    }
                    else {
                        currentActionBarHeight = 0;
                    }
                    int statusBarHeight;
                    if (Build$VERSION.SDK_INT >= 21 && this.occupyStatusBar) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        statusBarHeight = 0;
                    }
                    final int n2 = currentActionBarHeight + statusBarHeight;
                    final int n3 = this.getMeasuredHeight() - n2;
                    final float n4 = this.getMeasuredWidth() / (float)this.backgroundDrawable.getIntrinsicWidth();
                    final float n5 = (this.keyboardHeight + n3) / (float)this.backgroundDrawable.getIntrinsicHeight();
                    float n6 = n4;
                    if (n4 < n5) {
                        n6 = n5;
                    }
                    final int n7 = (int)Math.ceil(this.backgroundDrawable.getIntrinsicWidth() * n6 * this.parallaxScale);
                    final int n8 = (int)Math.ceil(this.backgroundDrawable.getIntrinsicHeight() * n6 * this.parallaxScale);
                    final int n9 = (this.getMeasuredWidth() - n7) / 2 + (int)this.translationX;
                    final int n10 = (n3 - n8 + this.keyboardHeight) / 2 + n2 + (int)this.translationY;
                    canvas.save();
                    canvas.clipRect(0, n2, n7, this.getMeasuredHeight() - this.bottomClip);
                    this.backgroundDrawable.setAlpha(255);
                    this.backgroundDrawable.setBounds(n9, n10, n7 + n9, n8 + n10);
                    this.backgroundDrawable.draw(canvas);
                    canvas.restore();
                }
            }
        }
        else {
            super.onDraw(canvas);
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.notifyHeightChanged();
    }
    
    public void onPause() {
        final WallpaperParallaxEffect parallaxEffect = this.parallaxEffect;
        if (parallaxEffect != null) {
            parallaxEffect.setEnabled(false);
        }
        this.paused = true;
    }
    
    public void onResume() {
        final WallpaperParallaxEffect parallaxEffect = this.parallaxEffect;
        if (parallaxEffect != null) {
            parallaxEffect.setEnabled(true);
        }
        this.paused = false;
    }
    
    public void setBackgroundImage(final Drawable backgroundDrawable, final boolean b) {
        this.backgroundDrawable = backgroundDrawable;
        if (b) {
            if (this.parallaxEffect == null) {
                (this.parallaxEffect = new WallpaperParallaxEffect(this.getContext())).setCallback((WallpaperParallaxEffect.Callback)new _$$Lambda$SizeNotifierFrameLayout$9xVW1u9E8sqKGYKEgYUca0gqvJA(this));
                if (this.getMeasuredWidth() != 0 && this.getMeasuredHeight() != 0) {
                    this.parallaxScale = this.parallaxEffect.getScale(this.getMeasuredWidth(), this.getMeasuredHeight());
                }
            }
            if (!this.paused) {
                this.parallaxEffect.setEnabled(true);
            }
        }
        else {
            final WallpaperParallaxEffect parallaxEffect = this.parallaxEffect;
            if (parallaxEffect != null) {
                parallaxEffect.setEnabled(false);
                this.parallaxEffect = null;
                this.parallaxScale = 1.0f;
                this.translationX = 0.0f;
                this.translationY = 0.0f;
            }
        }
        this.invalidate();
    }
    
    public void setBottomClip(final int bottomClip) {
        this.bottomClip = bottomClip;
    }
    
    public void setDelegate(final SizeNotifierFrameLayoutDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setOccupyStatusBar(final boolean occupyStatusBar) {
        this.occupyStatusBar = occupyStatusBar;
    }
    
    public interface SizeNotifierFrameLayoutDelegate
    {
        void onSizeChanged(final int p0, final boolean p1);
    }
}
