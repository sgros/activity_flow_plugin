// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.circularreveal;

import android.graphics.Shader;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.graphics.Color;
import android.graphics.Path$Direction;
import android.support.design.widget.MathUtils;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.os.Build$VERSION;
import android.view.View;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class CircularRevealHelper
{
    public static final int STRATEGY;
    private boolean buildingCircularRevealCache;
    private final Delegate delegate;
    private boolean hasCircularRevealCache;
    private Drawable overlayDrawable;
    private CircularRevealWidget.RevealInfo revealInfo;
    private final Paint revealPaint;
    private final Path revealPath;
    private final Paint scrimPaint;
    private final View view;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            STRATEGY = 2;
        }
        else if (Build$VERSION.SDK_INT >= 18) {
            STRATEGY = 1;
        }
        else {
            STRATEGY = 0;
        }
    }
    
    private void drawOverlayDrawable(final Canvas canvas) {
        if (this.shouldDrawOverlayDrawable()) {
            final Rect bounds = this.overlayDrawable.getBounds();
            final float n = this.revealInfo.centerX - bounds.width() / 2.0f;
            final float n2 = this.revealInfo.centerY - bounds.height() / 2.0f;
            canvas.translate(n, n2);
            this.overlayDrawable.draw(canvas);
            canvas.translate(-n, -n2);
        }
    }
    
    private float getDistanceToFurthestCorner(final CircularRevealWidget.RevealInfo revealInfo) {
        return MathUtils.distanceToFurthestCorner(revealInfo.centerX, revealInfo.centerY, 0.0f, 0.0f, (float)this.view.getWidth(), (float)this.view.getHeight());
    }
    
    private void invalidateRevealInfo() {
        if (CircularRevealHelper.STRATEGY == 1) {
            this.revealPath.rewind();
            if (this.revealInfo != null) {
                this.revealPath.addCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, Path$Direction.CW);
            }
        }
        this.view.invalidate();
    }
    
    private boolean shouldDrawCircularReveal() {
        final CircularRevealWidget.RevealInfo revealInfo = this.revealInfo;
        final boolean b = false;
        final boolean b2 = revealInfo == null || this.revealInfo.isInvalid();
        if (CircularRevealHelper.STRATEGY == 0) {
            boolean b3 = b;
            if (!b2) {
                b3 = b;
                if (this.hasCircularRevealCache) {
                    b3 = true;
                }
            }
            return b3;
        }
        return b2 ^ true;
    }
    
    private boolean shouldDrawOverlayDrawable() {
        return !this.buildingCircularRevealCache && this.overlayDrawable != null && this.revealInfo != null;
    }
    
    private boolean shouldDrawScrim() {
        return !this.buildingCircularRevealCache && Color.alpha(this.scrimPaint.getColor()) != 0;
    }
    
    public void buildCircularRevealCache() {
        if (CircularRevealHelper.STRATEGY == 0) {
            this.buildingCircularRevealCache = true;
            this.hasCircularRevealCache = false;
            this.view.buildDrawingCache();
            final Bitmap drawingCache = this.view.getDrawingCache();
            Bitmap bitmap;
            if ((bitmap = drawingCache) == null) {
                bitmap = drawingCache;
                if (this.view.getWidth() != 0) {
                    bitmap = drawingCache;
                    if (this.view.getHeight() != 0) {
                        bitmap = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Bitmap$Config.ARGB_8888);
                        this.view.draw(new Canvas(bitmap));
                    }
                }
            }
            if (bitmap != null) {
                this.revealPaint.setShader((Shader)new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP));
            }
            this.buildingCircularRevealCache = false;
            this.hasCircularRevealCache = true;
        }
    }
    
    public void destroyCircularRevealCache() {
        if (CircularRevealHelper.STRATEGY == 0) {
            this.hasCircularRevealCache = false;
            this.view.destroyDrawingCache();
            this.revealPaint.setShader((Shader)null);
            this.view.invalidate();
        }
    }
    
    public void draw(final Canvas canvas) {
        if (this.shouldDrawCircularReveal()) {
            switch (CircularRevealHelper.STRATEGY) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unsupported strategy ");
                    sb.append(CircularRevealHelper.STRATEGY);
                    throw new IllegalStateException(sb.toString());
                }
                case 2: {
                    this.delegate.actualDraw(canvas);
                    if (this.shouldDrawScrim()) {
                        canvas.drawRect(0.0f, 0.0f, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
                        break;
                    }
                    break;
                }
                case 1: {
                    final int save = canvas.save();
                    canvas.clipPath(this.revealPath);
                    this.delegate.actualDraw(canvas);
                    if (this.shouldDrawScrim()) {
                        canvas.drawRect(0.0f, 0.0f, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
                    }
                    canvas.restoreToCount(save);
                    break;
                }
                case 0: {
                    canvas.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.revealPaint);
                    if (this.shouldDrawScrim()) {
                        canvas.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.scrimPaint);
                        break;
                    }
                    break;
                }
            }
        }
        else {
            this.delegate.actualDraw(canvas);
            if (this.shouldDrawScrim()) {
                canvas.drawRect(0.0f, 0.0f, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
            }
        }
        this.drawOverlayDrawable(canvas);
    }
    
    public Drawable getCircularRevealOverlayDrawable() {
        return this.overlayDrawable;
    }
    
    public int getCircularRevealScrimColor() {
        return this.scrimPaint.getColor();
    }
    
    public CircularRevealWidget.RevealInfo getRevealInfo() {
        if (this.revealInfo == null) {
            return null;
        }
        final CircularRevealWidget.RevealInfo revealInfo = new CircularRevealWidget.RevealInfo(this.revealInfo);
        if (revealInfo.isInvalid()) {
            revealInfo.radius = this.getDistanceToFurthestCorner(revealInfo);
        }
        return revealInfo;
    }
    
    public boolean isOpaque() {
        return this.delegate.actualIsOpaque() && !this.shouldDrawCircularReveal();
    }
    
    public void setCircularRevealOverlayDrawable(final Drawable overlayDrawable) {
        this.overlayDrawable = overlayDrawable;
        this.view.invalidate();
    }
    
    public void setCircularRevealScrimColor(final int color) {
        this.scrimPaint.setColor(color);
        this.view.invalidate();
    }
    
    public void setRevealInfo(final CircularRevealWidget.RevealInfo revealInfo) {
        if (revealInfo == null) {
            this.revealInfo = null;
        }
        else {
            if (this.revealInfo == null) {
                this.revealInfo = new CircularRevealWidget.RevealInfo(revealInfo);
            }
            else {
                this.revealInfo.set(revealInfo);
            }
            if (MathUtils.geq(revealInfo.radius, this.getDistanceToFurthestCorner(revealInfo), 1.0E-4f)) {
                this.revealInfo.radius = Float.MAX_VALUE;
            }
        }
        this.invalidateRevealInfo();
    }
    
    interface Delegate
    {
        void actualDraw(final Canvas p0);
        
        boolean actualIsOpaque();
    }
}
