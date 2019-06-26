// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.os.Build$VERSION;
import android.graphics.Bitmap;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.Components.Crop.CropRotationWheel;
import org.telegram.ui.Components.Crop.CropView;
import android.widget.FrameLayout;

public class PhotoCropView extends FrameLayout
{
    private CropView cropView;
    private PhotoCropViewDelegate delegate;
    private boolean showOnSetBitmap;
    private CropRotationWheel wheelView;
    
    public PhotoCropView(final Context context) {
        super(context);
        (this.cropView = new CropView(this.getContext())).setListener((CropView.CropViewListener)new CropView.CropViewListener() {
            @Override
            public void onAspectLock(final boolean aspectLock) {
                PhotoCropView.this.wheelView.setAspectLock(aspectLock);
            }
            
            @Override
            public void onChange(final boolean b) {
                if (PhotoCropView.this.delegate != null) {
                    PhotoCropView.this.delegate.onChange(b);
                }
            }
        });
        this.cropView.setBottomPadding((float)AndroidUtilities.dp(64.0f));
        this.addView((View)this.cropView);
        (this.wheelView = new CropRotationWheel(this.getContext())).setListener((CropRotationWheel.RotationWheelListener)new CropRotationWheel.RotationWheelListener() {
            @Override
            public void aspectRatioPressed() {
                PhotoCropView.this.cropView.showAspectRatioDialog();
            }
            
            @Override
            public void onChange(final float rotation) {
                PhotoCropView.this.cropView.setRotation(rotation);
                if (PhotoCropView.this.delegate != null) {
                    PhotoCropView.this.delegate.onChange(false);
                }
            }
            
            @Override
            public void onEnd(final float n) {
                PhotoCropView.this.cropView.onRotationEnded();
            }
            
            @Override
            public void onStart() {
                PhotoCropView.this.cropView.onRotationBegan();
            }
            
            @Override
            public void rotate90Pressed() {
                PhotoCropView.this.rotate();
            }
        });
        this.addView((View)this.wheelView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 81, 0.0f, 0.0f, 0.0f, 0.0f));
    }
    
    public Bitmap getBitmap() {
        final CropView cropView = this.cropView;
        if (cropView != null) {
            return cropView.getResult();
        }
        return null;
    }
    
    public float getRectSizeX() {
        return this.cropView.getCropWidth();
    }
    
    public float getRectSizeY() {
        return this.cropView.getCropHeight();
    }
    
    public float getRectX() {
        return this.cropView.getCropLeft() - AndroidUtilities.dp(14.0f);
    }
    
    public float getRectY() {
        final float cropTop = this.cropView.getCropTop();
        final float n = (float)AndroidUtilities.dp(14.0f);
        int statusBarHeight;
        if (Build$VERSION.SDK_INT >= 21) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        return cropTop - n - statusBarHeight;
    }
    
    public void hideBackView() {
        this.cropView.hideBackView();
    }
    
    public boolean isReady() {
        return this.cropView.isReady();
    }
    
    public void onAppear() {
        this.cropView.willShow();
    }
    
    public void onAppeared() {
        final CropView cropView = this.cropView;
        if (cropView != null) {
            cropView.show();
        }
        else {
            this.showOnSetBitmap = true;
        }
    }
    
    public void onDisappear() {
        final CropView cropView = this.cropView;
        if (cropView != null) {
            cropView.hide();
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        final CropView cropView = this.cropView;
        if (cropView != null) {
            cropView.updateLayout();
        }
    }
    
    public void reset() {
        this.wheelView.reset();
        this.cropView.reset();
    }
    
    public void rotate() {
        final CropRotationWheel wheelView = this.wheelView;
        if (wheelView != null) {
            wheelView.reset();
        }
        this.cropView.rotate90Degrees();
    }
    
    public void setAspectRatio(final float aspectRatio) {
        this.cropView.setAspectRatio(aspectRatio);
    }
    
    public void setBitmap(final Bitmap bitmap, int visibility, final boolean freeform, final boolean b) {
        this.requestLayout();
        this.cropView.setBitmap(bitmap, visibility, freeform, b);
        final boolean showOnSetBitmap = this.showOnSetBitmap;
        visibility = 0;
        if (showOnSetBitmap) {
            this.showOnSetBitmap = false;
            this.cropView.show();
        }
        this.wheelView.setFreeform(freeform);
        this.wheelView.reset();
        final CropRotationWheel wheelView = this.wheelView;
        if (!freeform) {
            visibility = 4;
        }
        wheelView.setVisibility(visibility);
    }
    
    public void setDelegate(final PhotoCropViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setFreeform(final boolean freeform) {
        this.cropView.setFreeform(freeform);
    }
    
    public void showBackView() {
        this.cropView.showBackView();
    }
    
    public interface PhotoCropViewDelegate
    {
        void onChange(final boolean p0);
    }
}
