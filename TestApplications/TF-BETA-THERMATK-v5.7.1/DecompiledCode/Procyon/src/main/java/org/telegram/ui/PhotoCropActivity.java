// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Canvas;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Bitmaps;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.graphics.Point;
import org.telegram.messenger.ImageLoader;
import java.io.File;
import android.net.Uri;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import org.telegram.ui.ActionBar.BaseFragment;

public class PhotoCropActivity extends BaseFragment
{
    private static final int done_button = 1;
    private String bitmapKey;
    private PhotoEditActivityDelegate delegate;
    private boolean doneButtonPressed;
    private BitmapDrawable drawable;
    private Bitmap imageToCrop;
    private boolean sameBitmap;
    private PhotoCropView view;
    
    public PhotoCropActivity(final Bundle bundle) {
        super(bundle);
        this.delegate = null;
        this.sameBitmap = false;
        this.doneButtonPressed = false;
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackgroundColor(-13421773);
        super.actionBar.setItemsBackgroundColor(-12763843, false);
        super.actionBar.setTitleColor(-1);
        super.actionBar.setItemsColor(-1, false);
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("CropImage", 2131559176));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PhotoCropActivity.this.finishFragment();
                }
                else if (n == 1) {
                    if (PhotoCropActivity.this.delegate != null && !PhotoCropActivity.this.doneButtonPressed) {
                        final Bitmap bitmap = PhotoCropActivity.this.view.getBitmap();
                        if (bitmap == PhotoCropActivity.this.imageToCrop) {
                            PhotoCropActivity.this.sameBitmap = true;
                        }
                        PhotoCropActivity.this.delegate.didFinishEdit(bitmap);
                        PhotoCropActivity.this.doneButtonPressed = true;
                    }
                    PhotoCropActivity.this.finishFragment();
                }
            }
        });
        super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        final PhotoCropView photoCropView = new PhotoCropView(context);
        this.view = photoCropView;
        super.fragmentView = (View)photoCropView;
        ((PhotoCropView)super.fragmentView).freeform = this.getArguments().getBoolean("freeform", false);
        super.fragmentView.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        return super.fragmentView;
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.swipeBackEnabled = false;
        if (this.imageToCrop == null) {
            final String string = this.getArguments().getString("photoPath");
            final Uri uri = (Uri)this.getArguments().getParcelable("photoUri");
            if (string == null && uri == null) {
                return false;
            }
            if (string != null && !new File(string).exists()) {
                return false;
            }
            int n;
            if (AndroidUtilities.isTablet()) {
                n = AndroidUtilities.dp(520.0f);
            }
            else {
                final Point displaySize = AndroidUtilities.displaySize;
                n = Math.max(displaySize.x, displaySize.y);
            }
            final float n2 = (float)n;
            this.imageToCrop = ImageLoader.loadBitmap(string, uri, n2, n2, true);
            if (this.imageToCrop == null) {
                return false;
            }
        }
        this.drawable = new BitmapDrawable(this.imageToCrop);
        super.onFragmentCreate();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.bitmapKey != null && ImageLoader.getInstance().decrementUseCount(this.bitmapKey) && !ImageLoader.getInstance().isInCache(this.bitmapKey)) {
            this.bitmapKey = null;
        }
        if (this.bitmapKey == null) {
            final Bitmap imageToCrop = this.imageToCrop;
            if (imageToCrop != null && !this.sameBitmap) {
                imageToCrop.recycle();
                this.imageToCrop = null;
            }
        }
        this.drawable = null;
    }
    
    public void setDelegate(final PhotoEditActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class PhotoCropView extends FrameLayout
    {
        int bitmapHeight;
        int bitmapWidth;
        int bitmapX;
        int bitmapY;
        Paint circlePaint;
        int draggingState;
        boolean freeform;
        Paint halfPaint;
        float oldX;
        float oldY;
        Paint rectPaint;
        float rectSizeX;
        float rectSizeY;
        float rectX;
        float rectY;
        int viewHeight;
        int viewWidth;
        
        public PhotoCropView(final Context context) {
            super(context);
            this.rectPaint = null;
            this.circlePaint = null;
            this.halfPaint = null;
            this.rectSizeX = 600.0f;
            this.rectSizeY = 600.0f;
            this.rectX = -1.0f;
            this.rectY = -1.0f;
            this.draggingState = 0;
            this.oldX = 0.0f;
            this.oldY = 0.0f;
            this.init();
        }
        
        private void init() {
            (this.rectPaint = new Paint()).setColor(1073412858);
            this.rectPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
            this.rectPaint.setStyle(Paint$Style.STROKE);
            (this.circlePaint = new Paint()).setColor(-1);
            (this.halfPaint = new Paint()).setColor(-939524096);
            this.setBackgroundColor(-13421773);
            this.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    final float x = motionEvent.getX();
                    final float y = motionEvent.getY();
                    final int dp = AndroidUtilities.dp(14.0f);
                    if (motionEvent.getAction() == 0) {
                        final PhotoCropView this$1 = PhotoCropView.this;
                        final float rectX = this$1.rectX;
                        final float n = (float)dp;
                        Label_0419: {
                            if (rectX - n < x && rectX + n > x) {
                                final float rectY = this$1.rectY;
                                if (rectY - n < y && rectY + n > y) {
                                    this$1.draggingState = 1;
                                    break Label_0419;
                                }
                            }
                            final PhotoCropView this$2 = PhotoCropView.this;
                            final float rectX2 = this$2.rectX;
                            final float rectSizeX = this$2.rectSizeX;
                            if (rectX2 - n + rectSizeX < x && rectX2 + n + rectSizeX > x) {
                                final float rectY2 = this$2.rectY;
                                if (rectY2 - n < y && rectY2 + n > y) {
                                    this$2.draggingState = 2;
                                    break Label_0419;
                                }
                            }
                            final PhotoCropView this$3 = PhotoCropView.this;
                            final float rectX3 = this$3.rectX;
                            if (rectX3 - n < x && rectX3 + n > x) {
                                final float rectY3 = this$3.rectY;
                                final float rectSizeY = this$3.rectSizeY;
                                if (rectY3 - n + rectSizeY < y && rectY3 + n + rectSizeY > y) {
                                    this$3.draggingState = 3;
                                    break Label_0419;
                                }
                            }
                            final PhotoCropView this$4 = PhotoCropView.this;
                            final float rectX4 = this$4.rectX;
                            final float rectSizeX2 = this$4.rectSizeX;
                            if (rectX4 - n + rectSizeX2 < x && rectX4 + n + rectSizeX2 > x) {
                                final float rectY4 = this$4.rectY;
                                final float rectSizeY2 = this$4.rectSizeY;
                                if (rectY4 - n + rectSizeY2 < y && rectY4 + n + rectSizeY2 > y) {
                                    this$4.draggingState = 4;
                                    break Label_0419;
                                }
                            }
                            final PhotoCropView this$5 = PhotoCropView.this;
                            final float rectX5 = this$5.rectX;
                            if (rectX5 < x && rectX5 + this$5.rectSizeX > x) {
                                final float rectY5 = this$5.rectY;
                                if (rectY5 < y && rectY5 + this$5.rectSizeY > y) {
                                    this$5.draggingState = 5;
                                    break Label_0419;
                                }
                            }
                            PhotoCropView.this.draggingState = 0;
                        }
                        final PhotoCropView this$6 = PhotoCropView.this;
                        if (this$6.draggingState != 0) {
                            this$6.requestDisallowInterceptTouchEvent(true);
                        }
                        final PhotoCropView this$7 = PhotoCropView.this;
                        this$7.oldX = x;
                        this$7.oldY = y;
                    }
                    else if (motionEvent.getAction() == 1) {
                        PhotoCropView.this.draggingState = 0;
                    }
                    else if (motionEvent.getAction() == 2) {
                        final PhotoCropView this$8 = PhotoCropView.this;
                        final int draggingState = this$8.draggingState;
                        if (draggingState != 0) {
                            final float n2 = x - this$8.oldX;
                            final float n3 = y - this$8.oldY;
                            if (draggingState == 5) {
                                this$8.rectX += n2;
                                this$8.rectY += n3;
                                final float rectX6 = this$8.rectX;
                                final int bitmapX = this$8.bitmapX;
                                if (rectX6 < bitmapX) {
                                    this$8.rectX = (float)bitmapX;
                                }
                                else {
                                    final float rectSizeX3 = this$8.rectSizeX;
                                    final int bitmapWidth = this$8.bitmapWidth;
                                    if (rectX6 + rectSizeX3 > bitmapX + bitmapWidth) {
                                        this$8.rectX = bitmapX + bitmapWidth - rectSizeX3;
                                    }
                                }
                                final PhotoCropView this$9 = PhotoCropView.this;
                                final float rectY6 = this$9.rectY;
                                final int bitmapY = this$9.bitmapY;
                                if (rectY6 < bitmapY) {
                                    this$9.rectY = (float)bitmapY;
                                }
                                else {
                                    final float rectSizeY3 = this$9.rectSizeY;
                                    final int bitmapHeight = this$9.bitmapHeight;
                                    if (rectY6 + rectSizeY3 > bitmapY + bitmapHeight) {
                                        this$9.rectY = bitmapY + bitmapHeight - rectSizeY3;
                                    }
                                }
                            }
                            else if (draggingState == 1) {
                                final float rectSizeX4 = this$8.rectSizeX;
                                float n4 = n2;
                                if (rectSizeX4 - n2 < 160.0f) {
                                    n4 = rectSizeX4 - 160.0f;
                                }
                                final PhotoCropView this$10 = PhotoCropView.this;
                                final float rectX7 = this$10.rectX;
                                final int bitmapX2 = this$10.bitmapX;
                                float n5 = n4;
                                if (rectX7 + n4 < bitmapX2) {
                                    n5 = bitmapX2 - rectX7;
                                }
                                final PhotoCropView this$11 = PhotoCropView.this;
                                if (!this$11.freeform) {
                                    final float rectY7 = this$11.rectY;
                                    final int bitmapY2 = this$11.bitmapY;
                                    float n6 = n5;
                                    if (rectY7 + n5 < bitmapY2) {
                                        n6 = bitmapY2 - rectY7;
                                    }
                                    final PhotoCropView this$12 = PhotoCropView.this;
                                    this$12.rectX += n6;
                                    this$12.rectY += n6;
                                    this$12.rectSizeX -= n6;
                                    this$12.rectSizeY -= n6;
                                }
                                else {
                                    final float rectSizeY4 = this$11.rectSizeY;
                                    float n7 = n3;
                                    if (rectSizeY4 - n3 < 160.0f) {
                                        n7 = rectSizeY4 - 160.0f;
                                    }
                                    final PhotoCropView this$13 = PhotoCropView.this;
                                    final float rectY8 = this$13.rectY;
                                    final int bitmapY3 = this$13.bitmapY;
                                    float n8 = n7;
                                    if (rectY8 + n7 < bitmapY3) {
                                        n8 = bitmapY3 - rectY8;
                                    }
                                    final PhotoCropView this$14 = PhotoCropView.this;
                                    this$14.rectX += n5;
                                    this$14.rectY += n8;
                                    this$14.rectSizeX -= n5;
                                    this$14.rectSizeY -= n8;
                                }
                            }
                            else if (draggingState == 2) {
                                final float rectSizeX5 = this$8.rectSizeX;
                                float n9 = n2;
                                if (rectSizeX5 + n2 < 160.0f) {
                                    n9 = -(rectSizeX5 - 160.0f);
                                }
                                final PhotoCropView this$15 = PhotoCropView.this;
                                final float rectX8 = this$15.rectX;
                                final float rectSizeX6 = this$15.rectSizeX;
                                final int bitmapX3 = this$15.bitmapX;
                                final int bitmapWidth2 = this$15.bitmapWidth;
                                float n10 = n9;
                                if (rectX8 + rectSizeX6 + n9 > bitmapX3 + bitmapWidth2) {
                                    n10 = bitmapX3 + bitmapWidth2 - rectX8 - rectSizeX6;
                                }
                                final PhotoCropView this$16 = PhotoCropView.this;
                                if (!this$16.freeform) {
                                    final float rectY9 = this$16.rectY;
                                    final int bitmapY4 = this$16.bitmapY;
                                    float n11 = n10;
                                    if (rectY9 - n10 < bitmapY4) {
                                        n11 = rectY9 - bitmapY4;
                                    }
                                    final PhotoCropView this$17 = PhotoCropView.this;
                                    this$17.rectY -= n11;
                                    this$17.rectSizeX += n11;
                                    this$17.rectSizeY += n11;
                                }
                                else {
                                    final float rectSizeY5 = this$16.rectSizeY;
                                    float n12 = n3;
                                    if (rectSizeY5 - n3 < 160.0f) {
                                        n12 = rectSizeY5 - 160.0f;
                                    }
                                    final PhotoCropView this$18 = PhotoCropView.this;
                                    final float rectY10 = this$18.rectY;
                                    final int bitmapY5 = this$18.bitmapY;
                                    float n13 = n12;
                                    if (rectY10 + n12 < bitmapY5) {
                                        n13 = bitmapY5 - rectY10;
                                    }
                                    final PhotoCropView this$19 = PhotoCropView.this;
                                    this$19.rectY += n13;
                                    this$19.rectSizeX += n10;
                                    this$19.rectSizeY -= n13;
                                }
                            }
                            else if (draggingState == 3) {
                                final float rectSizeX7 = this$8.rectSizeX;
                                float n14 = n2;
                                if (rectSizeX7 - n2 < 160.0f) {
                                    n14 = rectSizeX7 - 160.0f;
                                }
                                final PhotoCropView this$20 = PhotoCropView.this;
                                final float rectX9 = this$20.rectX;
                                final int bitmapX4 = this$20.bitmapX;
                                float n15 = n14;
                                if (rectX9 + n14 < bitmapX4) {
                                    n15 = bitmapX4 - rectX9;
                                }
                                final PhotoCropView this$21 = PhotoCropView.this;
                                if (!this$21.freeform) {
                                    final float rectY11 = this$21.rectY;
                                    final float rectSizeX8 = this$21.rectSizeX;
                                    final int bitmapY6 = this$21.bitmapY;
                                    final int bitmapHeight2 = this$21.bitmapHeight;
                                    float n16 = n15;
                                    if (rectY11 + rectSizeX8 - n15 > bitmapY6 + bitmapHeight2) {
                                        n16 = rectY11 + rectSizeX8 - bitmapY6 - bitmapHeight2;
                                    }
                                    final PhotoCropView this$22 = PhotoCropView.this;
                                    this$22.rectX += n16;
                                    this$22.rectSizeX -= n16;
                                    this$22.rectSizeY -= n16;
                                }
                                else {
                                    final float rectY12 = this$21.rectY;
                                    final float rectSizeY6 = this$21.rectSizeY;
                                    final int bitmapY7 = this$21.bitmapY;
                                    final int bitmapHeight3 = this$21.bitmapHeight;
                                    float n17 = n3;
                                    if (rectY12 + rectSizeY6 + n3 > bitmapY7 + bitmapHeight3) {
                                        n17 = bitmapY7 + bitmapHeight3 - rectY12 - rectSizeY6;
                                    }
                                    final PhotoCropView this$23 = PhotoCropView.this;
                                    this$23.rectX += n15;
                                    this$23.rectSizeX -= n15;
                                    this$23.rectSizeY += n17;
                                    if (this$23.rectSizeY < 160.0f) {
                                        this$23.rectSizeY = 160.0f;
                                    }
                                }
                            }
                            else if (draggingState == 4) {
                                final float rectX10 = this$8.rectX;
                                final float rectSizeX9 = this$8.rectSizeX;
                                final int bitmapX5 = this$8.bitmapX;
                                final int bitmapWidth3 = this$8.bitmapWidth;
                                float n18 = n2;
                                if (rectX10 + rectSizeX9 + n2 > bitmapX5 + bitmapWidth3) {
                                    n18 = bitmapX5 + bitmapWidth3 - rectX10 - rectSizeX9;
                                }
                                final PhotoCropView this$24 = PhotoCropView.this;
                                if (!this$24.freeform) {
                                    final float rectY13 = this$24.rectY;
                                    final float rectSizeX10 = this$24.rectSizeX;
                                    final int bitmapY8 = this$24.bitmapY;
                                    final int bitmapHeight4 = this$24.bitmapHeight;
                                    float n19 = n18;
                                    if (rectY13 + rectSizeX10 + n18 > bitmapY8 + bitmapHeight4) {
                                        n19 = bitmapY8 + bitmapHeight4 - rectY13 - rectSizeX10;
                                    }
                                    final PhotoCropView this$25 = PhotoCropView.this;
                                    this$25.rectSizeX += n19;
                                    this$25.rectSizeY += n19;
                                }
                                else {
                                    final float rectY14 = this$24.rectY;
                                    final float rectSizeY7 = this$24.rectSizeY;
                                    final int bitmapY9 = this$24.bitmapY;
                                    final int bitmapHeight5 = this$24.bitmapHeight;
                                    float n20 = n3;
                                    if (rectY14 + rectSizeY7 + n3 > bitmapY9 + bitmapHeight5) {
                                        n20 = bitmapY9 + bitmapHeight5 - rectY14 - rectSizeY7;
                                    }
                                    final PhotoCropView this$26 = PhotoCropView.this;
                                    this$26.rectSizeX += n18;
                                    this$26.rectSizeY += n20;
                                }
                                final PhotoCropView this$27 = PhotoCropView.this;
                                if (this$27.rectSizeX < 160.0f) {
                                    this$27.rectSizeX = 160.0f;
                                }
                                final PhotoCropView this$28 = PhotoCropView.this;
                                if (this$28.rectSizeY < 160.0f) {
                                    this$28.rectSizeY = 160.0f;
                                }
                            }
                            final PhotoCropView this$29 = PhotoCropView.this;
                            this$29.oldX = x;
                            this$29.oldY = y;
                            this$29.invalidate();
                        }
                    }
                    return true;
                }
            });
        }
        
        private void updateBitmapSize() {
            if (this.viewWidth != 0 && this.viewHeight != 0) {
                if (PhotoCropActivity.this.imageToCrop != null) {
                    final float rectX = this.rectX;
                    final float n = (float)this.bitmapX;
                    final int bitmapWidth = this.bitmapWidth;
                    final float n2 = (rectX - n) / bitmapWidth;
                    final float rectY = this.rectY;
                    final float n3 = (float)this.bitmapY;
                    final int bitmapHeight = this.bitmapHeight;
                    final float n4 = (rectY - n3) / bitmapHeight;
                    final float n5 = this.rectSizeX / bitmapWidth;
                    final float n6 = this.rectSizeY / bitmapHeight;
                    final float n7 = (float)PhotoCropActivity.this.imageToCrop.getWidth();
                    final float n8 = (float)PhotoCropActivity.this.imageToCrop.getHeight();
                    final int viewWidth = this.viewWidth;
                    final float n9 = viewWidth / n7;
                    final int viewHeight = this.viewHeight;
                    final float n10 = viewHeight / n8;
                    if (n9 > n10) {
                        this.bitmapHeight = viewHeight;
                        this.bitmapWidth = (int)Math.ceil(n7 * n10);
                    }
                    else {
                        this.bitmapWidth = viewWidth;
                        this.bitmapHeight = (int)Math.ceil(n8 * n9);
                    }
                    this.bitmapX = (this.viewWidth - this.bitmapWidth) / 2 + AndroidUtilities.dp(14.0f);
                    this.bitmapY = (this.viewHeight - this.bitmapHeight) / 2 + AndroidUtilities.dp(14.0f);
                    if (this.rectX == -1.0f && this.rectY == -1.0f) {
                        if (this.freeform) {
                            this.rectY = (float)this.bitmapY;
                            this.rectX = (float)this.bitmapX;
                            this.rectSizeX = (float)this.bitmapWidth;
                            this.rectSizeY = (float)this.bitmapHeight;
                        }
                        else {
                            final int bitmapWidth2 = this.bitmapWidth;
                            final int bitmapHeight2 = this.bitmapHeight;
                            if (bitmapWidth2 > bitmapHeight2) {
                                this.rectY = (float)this.bitmapY;
                                this.rectX = (float)((this.viewWidth - bitmapHeight2) / 2 + AndroidUtilities.dp(14.0f));
                                final int bitmapHeight3 = this.bitmapHeight;
                                this.rectSizeX = (float)bitmapHeight3;
                                this.rectSizeY = (float)bitmapHeight3;
                            }
                            else {
                                this.rectX = (float)this.bitmapX;
                                this.rectY = (float)((this.viewHeight - bitmapWidth2) / 2 + AndroidUtilities.dp(14.0f));
                                final int bitmapWidth3 = this.bitmapWidth;
                                this.rectSizeX = (float)bitmapWidth3;
                                this.rectSizeY = (float)bitmapWidth3;
                            }
                        }
                    }
                    else {
                        final int bitmapWidth4 = this.bitmapWidth;
                        this.rectX = n2 * bitmapWidth4 + this.bitmapX;
                        final int bitmapHeight4 = this.bitmapHeight;
                        this.rectY = n4 * bitmapHeight4 + this.bitmapY;
                        this.rectSizeX = n5 * bitmapWidth4;
                        this.rectSizeY = n6 * bitmapHeight4;
                    }
                    this.invalidate();
                }
            }
        }
        
        public Bitmap getBitmap() {
            final float rectX = this.rectX;
            final float n = (float)this.bitmapX;
            final int bitmapWidth = this.bitmapWidth;
            final float n2 = (rectX - n) / bitmapWidth;
            final float n3 = (this.rectY - this.bitmapY) / this.bitmapHeight;
            final float n4 = this.rectSizeX / bitmapWidth;
            final float n5 = this.rectSizeY / bitmapWidth;
            final int n6 = (int)(n2 * PhotoCropActivity.this.imageToCrop.getWidth());
            final int n7 = (int)(n3 * PhotoCropActivity.this.imageToCrop.getHeight());
            final int n8 = (int)(n4 * PhotoCropActivity.this.imageToCrop.getWidth());
            final int n9 = (int)(n5 * PhotoCropActivity.this.imageToCrop.getWidth());
            int n10 = n6;
            if (n6 < 0) {
                n10 = 0;
            }
            int n11;
            if ((n11 = n7) < 0) {
                n11 = 0;
            }
            int n12 = n8;
            if (n10 + n8 > PhotoCropActivity.this.imageToCrop.getWidth()) {
                n12 = PhotoCropActivity.this.imageToCrop.getWidth() - n10;
            }
            int n13 = n9;
            if (n11 + n9 > PhotoCropActivity.this.imageToCrop.getHeight()) {
                n13 = PhotoCropActivity.this.imageToCrop.getHeight() - n11;
            }
            try {
                return Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, n10, n11, n12, n13);
            }
            catch (Throwable t) {
                FileLog.e(t);
                System.gc();
                try {
                    return Bitmaps.createBitmap(PhotoCropActivity.this.imageToCrop, n10, n11, n12, n13);
                }
                catch (Throwable t2) {
                    FileLog.e(t2);
                    return null;
                }
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            if (PhotoCropActivity.this.drawable != null) {
                BitmapDrawable access$100 = null;
                try {
                    PhotoCropActivity.this.drawable.setBounds(this.bitmapX, this.bitmapY, this.bitmapX + this.bitmapWidth, this.bitmapY + this.bitmapHeight);
                    access$100 = PhotoCropActivity.this.drawable;
                    try {
                        access$100.draw(canvas);
                    }
                    catch (Throwable access$100) {}
                }
                catch (Throwable t) {}
                FileLog.e((Throwable)access$100);
            }
            final int bitmapX = this.bitmapX;
            canvas.drawRect((float)bitmapX, (float)this.bitmapY, (float)(bitmapX + this.bitmapWidth), this.rectY, this.halfPaint);
            final float n = (float)this.bitmapX;
            final float rectY = this.rectY;
            canvas.drawRect(n, rectY, this.rectX, rectY + this.rectSizeY, this.halfPaint);
            final float rectX = this.rectX;
            final float rectSizeX = this.rectSizeX;
            final float rectY2 = this.rectY;
            canvas.drawRect(rectX + rectSizeX, rectY2, (float)(this.bitmapX + this.bitmapWidth), rectY2 + this.rectSizeY, this.halfPaint);
            final int bitmapX2 = this.bitmapX;
            canvas.drawRect((float)bitmapX2, this.rectSizeY + this.rectY, (float)(bitmapX2 + this.bitmapWidth), (float)(this.bitmapY + this.bitmapHeight), this.halfPaint);
            final float rectX2 = this.rectX;
            final float rectY3 = this.rectY;
            canvas.drawRect(rectX2, rectY3, rectX2 + this.rectSizeX, rectY3 + this.rectSizeY, this.rectPaint);
            final int dp = AndroidUtilities.dp(1.0f);
            final float rectX3 = this.rectX;
            final float n2 = (float)dp;
            final float rectY4 = this.rectY;
            final float n3 = (float)AndroidUtilities.dp(20.0f);
            final float rectY5 = this.rectY;
            final float n4 = (float)(dp * 3);
            canvas.drawRect(rectX3 + n2, rectY4 + n2, n3 + (rectX3 + n2), rectY5 + n4, this.circlePaint);
            final float rectX4 = this.rectX;
            final float rectY6 = this.rectY;
            canvas.drawRect(rectX4 + n2, rectY6 + n2, rectX4 + n4, rectY6 + n2 + AndroidUtilities.dp(20.0f), this.circlePaint);
            final float rectX5 = this.rectX;
            final float rectSizeX2 = this.rectSizeX;
            final float n5 = (float)AndroidUtilities.dp(20.0f);
            final float rectY7 = this.rectY;
            canvas.drawRect(rectX5 + rectSizeX2 - n2 - n5, rectY7 + n2, this.rectX + this.rectSizeX - n2, rectY7 + n4, this.circlePaint);
            final float rectX6 = this.rectX;
            final float rectSizeX3 = this.rectSizeX;
            final float rectY8 = this.rectY;
            canvas.drawRect(rectX6 + rectSizeX3 - n4, rectY8 + n2, rectX6 + rectSizeX3 - n2, rectY8 + n2 + AndroidUtilities.dp(20.0f), this.circlePaint);
            canvas.drawRect(this.rectX + n2, this.rectY + this.rectSizeY - n2 - AndroidUtilities.dp(20.0f), this.rectX + n4, this.rectY + this.rectSizeY - n2, this.circlePaint);
            final float rectX7 = this.rectX;
            canvas.drawRect(rectX7 + n2, this.rectY + this.rectSizeY - n4, AndroidUtilities.dp(20.0f) + (rectX7 + n2), this.rectY + this.rectSizeY - n2, this.circlePaint);
            final float rectX8 = this.rectX;
            final float rectSizeX4 = this.rectSizeX;
            final float n6 = (float)AndroidUtilities.dp(20.0f);
            final float rectY9 = this.rectY;
            final float rectSizeY = this.rectSizeY;
            canvas.drawRect(rectX8 + rectSizeX4 - n2 - n6, rectY9 + rectSizeY - n4, this.rectX + this.rectSizeX - n2, rectY9 + rectSizeY - n2, this.circlePaint);
            canvas.drawRect(this.rectX + this.rectSizeX - n4, this.rectY + this.rectSizeY - n2 - AndroidUtilities.dp(20.0f), this.rectX + this.rectSizeX - n2, this.rectY + this.rectSizeY - n2, this.circlePaint);
            for (int i = 1; i < 3; ++i) {
                final float rectX9 = this.rectX;
                final float rectSizeX5 = this.rectSizeX;
                final float n7 = rectSizeX5 / 3.0f;
                final float n8 = (float)i;
                final float rectY10 = this.rectY;
                canvas.drawRect(n7 * n8 + rectX9, rectY10 + n2, rectX9 + n2 + rectSizeX5 / 3.0f * n8, rectY10 + this.rectSizeY - n2, this.circlePaint);
                final float rectX10 = this.rectX;
                final float rectY11 = this.rectY;
                final float rectSizeY2 = this.rectSizeY;
                canvas.drawRect(rectX10 + n2, rectSizeY2 / 3.0f * n8 + rectY11, this.rectSizeX + (rectX10 - n2), rectY11 + rectSizeY2 / 3.0f * n8 + n2, this.circlePaint);
            }
        }
        
        protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
            super.onLayout(b, n, n2, n3, n4);
            this.viewWidth = n3 - n - AndroidUtilities.dp(28.0f);
            this.viewHeight = n4 - n2 - AndroidUtilities.dp(28.0f);
            this.updateBitmapSize();
        }
    }
    
    public interface PhotoEditActivityDelegate
    {
        void didFinishEdit(final Bitmap p0);
    }
}
