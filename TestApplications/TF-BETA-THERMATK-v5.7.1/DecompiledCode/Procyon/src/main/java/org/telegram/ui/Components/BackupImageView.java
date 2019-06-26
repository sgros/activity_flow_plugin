// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.SecureDocument;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import org.telegram.messenger.ImageLocation;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import org.telegram.messenger.ImageReceiver;
import android.view.View;

public class BackupImageView extends View
{
    private int height;
    private ImageReceiver imageReceiver;
    private int width;
    
    public BackupImageView(final Context context) {
        super(context);
        this.width = -1;
        this.height = -1;
        this.init();
    }
    
    public BackupImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.width = -1;
        this.height = -1;
        this.init();
    }
    
    public BackupImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.width = -1;
        this.height = -1;
        this.init();
    }
    
    private void init() {
        this.imageReceiver = new ImageReceiver(this);
    }
    
    public ImageReceiver getImageReceiver() {
        return this.imageReceiver;
    }
    
    public int getRoundRadius() {
        return this.imageReceiver.getRoundRadius();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.imageReceiver.onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.imageReceiver.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.width != -1 && this.height != -1) {
            final ImageReceiver imageReceiver = this.imageReceiver;
            final int n = (this.getWidth() - this.width) / 2;
            final int height = this.getHeight();
            final int height2 = this.height;
            imageReceiver.setImageCoords(n, (height - height2) / 2, this.width, height2);
        }
        else {
            this.imageReceiver.setImageCoords(0, 0, this.getWidth(), this.getHeight());
        }
        this.imageReceiver.draw(canvas);
    }
    
    public void setAspectFit(final boolean aspectFit) {
        this.imageReceiver.setAspectFit(aspectFit);
    }
    
    public void setImage(final String s, final String s2, final Drawable drawable) {
        this.setImage(ImageLocation.getForPath(s), s2, null, null, drawable, null, null, 0, null);
    }
    
    public void setImage(final String s, final String s2, final String s3, final String s4) {
        this.setImage(ImageLocation.getForPath(s), s2, ImageLocation.getForPath(s3), s4, null, null, null, 0, null);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final Bitmap bitmap, final int n, final Object o) {
        this.setImage(imageLocation, s, null, null, null, bitmap, null, n, o);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final Bitmap bitmap, final Object o) {
        this.setImage(imageLocation, s, null, null, null, bitmap, null, 0, o);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final Drawable drawable, final int n, final Object o) {
        this.setImage(imageLocation, s, null, null, drawable, null, null, n, o);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final Drawable drawable, final Object o) {
        this.setImage(imageLocation, s, null, null, drawable, null, null, 0, o);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final String s2, final Drawable drawable, final Object o) {
        this.setImage(imageLocation, s, null, null, drawable, null, s2, 0, o);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final ImageLocation imageLocation2, final String s2, final int n, final Object o) {
        this.setImage(imageLocation, s, imageLocation2, s2, null, null, null, n, o);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final ImageLocation imageLocation2, final String s2, Drawable drawable, final Bitmap bitmap, final String s3, final int n, final Object o) {
        if (bitmap != null) {
            drawable = (Drawable)new BitmapDrawable((Resources)null, bitmap);
        }
        this.imageReceiver.setImage(imageLocation, s, imageLocation2, s2, drawable, n, s3, o, 0);
    }
    
    public void setImage(final ImageLocation imageLocation, final String s, final ImageLocation imageLocation2, final String s2, final String s3, final int n, final int n2, final Object o) {
        this.imageReceiver.setImage(imageLocation, s, imageLocation2, s2, null, n, s3, o, n2);
    }
    
    public void setImage(final SecureDocument secureDocument, final String s) {
        this.setImage(ImageLocation.getForSecureDocument(secureDocument), s, null, null, null, null, null, 0, null);
    }
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        this.imageReceiver.setImageBitmap(imageBitmap);
    }
    
    public void setImageDrawable(final Drawable imageBitmap) {
        this.imageReceiver.setImageBitmap(imageBitmap);
    }
    
    public void setImageResource(final int n) {
        this.imageReceiver.setImageBitmap(this.getResources().getDrawable(n));
        this.invalidate();
    }
    
    public void setImageResource(final int n, final int n2) {
        final Drawable drawable = this.getResources().getDrawable(n);
        if (drawable != null) {
            drawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(n2, PorterDuff$Mode.MULTIPLY));
        }
        this.imageReceiver.setImageBitmap(drawable);
        this.invalidate();
    }
    
    public void setOrientation(final int n, final boolean b) {
        this.imageReceiver.setOrientation(n, b);
    }
    
    public void setRoundRadius(final int roundRadius) {
        this.imageReceiver.setRoundRadius(roundRadius);
        this.invalidate();
    }
    
    public void setSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
