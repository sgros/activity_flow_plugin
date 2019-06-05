// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.request.target;

import java.lang.ref.WeakReference;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver$OnPreDrawListener;
import java.util.Iterator;
import java.util.Collection;
import android.view.ViewGroup$LayoutParams;
import android.util.Log;
import android.view.Display;
import android.graphics.Point;
import android.view.WindowManager;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Preconditions;
import android.view.View;

public abstract class ViewTarget<T extends View, Z> extends BaseTarget<Z>
{
    private static boolean isTagUsedAtLeastOnce = false;
    private static Integer tagId;
    private final SizeDeterminer sizeDeterminer;
    protected final T view;
    
    public ViewTarget(final T t) {
        this(t, false);
    }
    
    public ViewTarget(final T t, final boolean b) {
        this.view = Preconditions.checkNotNull(t);
        this.sizeDeterminer = new SizeDeterminer(t, b);
    }
    
    private Object getTag() {
        if (ViewTarget.tagId == null) {
            return this.view.getTag();
        }
        return this.view.getTag((int)ViewTarget.tagId);
    }
    
    private void setTag(final Object tag) {
        if (ViewTarget.tagId == null) {
            ViewTarget.isTagUsedAtLeastOnce = true;
            this.view.setTag(tag);
        }
        else {
            this.view.setTag((int)ViewTarget.tagId, tag);
        }
    }
    
    @Override
    public Request getRequest() {
        final Object tag = this.getTag();
        Request request;
        if (tag != null) {
            if (!(tag instanceof Request)) {
                throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
            }
            request = (Request)tag;
        }
        else {
            request = null;
        }
        return request;
    }
    
    @Override
    public void getSize(final SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.getSize(sizeReadyCallback);
    }
    
    @Override
    public void onLoadCleared(final Drawable drawable) {
        super.onLoadCleared(drawable);
        this.sizeDeterminer.clearCallbacksAndListener();
    }
    
    @Override
    public void removeCallback(final SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.removeCallback(sizeReadyCallback);
    }
    
    @Override
    public void setRequest(final Request tag) {
        this.setTag(tag);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Target for: ");
        sb.append(this.view);
        return sb.toString();
    }
    
    static final class SizeDeterminer
    {
        static Integer maxDisplayLength;
        private final List<SizeReadyCallback> cbs;
        private SizeDeterminerLayoutListener layoutListener;
        private final View view;
        private final boolean waitForLayout;
        
        SizeDeterminer(final View view, final boolean waitForLayout) {
            this.cbs = new ArrayList<SizeReadyCallback>();
            this.view = view;
            this.waitForLayout = waitForLayout;
        }
        
        private static int getMaxDisplayLength(final Context context) {
            if (SizeDeterminer.maxDisplayLength == null) {
                final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
                final Point point = new Point();
                defaultDisplay.getSize(point);
                SizeDeterminer.maxDisplayLength = Math.max(point.x, point.y);
            }
            return SizeDeterminer.maxDisplayLength;
        }
        
        private int getTargetDimen(int n, final int n2, final int n3) {
            final int n4 = n2 - n3;
            if (n4 > 0) {
                return n4;
            }
            if (this.waitForLayout && this.view.isLayoutRequested()) {
                return 0;
            }
            n -= n3;
            if (n > 0) {
                return n;
            }
            if (!this.view.isLayoutRequested() && n2 == -2) {
                if (Log.isLoggable("ViewTarget", 4)) {
                    Log.i("ViewTarget", "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use .override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
                }
                return getMaxDisplayLength(this.view.getContext());
            }
            return 0;
        }
        
        private int getTargetHeight() {
            final int paddingTop = this.view.getPaddingTop();
            final int paddingBottom = this.view.getPaddingBottom();
            final ViewGroup$LayoutParams layoutParams = this.view.getLayoutParams();
            int height;
            if (layoutParams != null) {
                height = layoutParams.height;
            }
            else {
                height = 0;
            }
            return this.getTargetDimen(this.view.getHeight(), height, paddingTop + paddingBottom);
        }
        
        private int getTargetWidth() {
            final int paddingLeft = this.view.getPaddingLeft();
            final int paddingRight = this.view.getPaddingRight();
            final ViewGroup$LayoutParams layoutParams = this.view.getLayoutParams();
            int width;
            if (layoutParams != null) {
                width = layoutParams.width;
            }
            else {
                width = 0;
            }
            return this.getTargetDimen(this.view.getWidth(), width, paddingLeft + paddingRight);
        }
        
        private boolean isDimensionValid(final int n) {
            return n > 0 || n == Integer.MIN_VALUE;
        }
        
        private boolean isViewStateAndSizeValid(final int n, final int n2) {
            return this.isDimensionValid(n) && this.isDimensionValid(n2);
        }
        
        private void notifyCbs(final int n, final int n2) {
            final Iterator<SizeReadyCallback> iterator = new ArrayList<SizeReadyCallback>(this.cbs).iterator();
            while (iterator.hasNext()) {
                iterator.next().onSizeReady(n, n2);
            }
        }
        
        void checkCurrentDimens() {
            if (this.cbs.isEmpty()) {
                return;
            }
            final int targetWidth = this.getTargetWidth();
            final int targetHeight = this.getTargetHeight();
            if (!this.isViewStateAndSizeValid(targetWidth, targetHeight)) {
                return;
            }
            this.notifyCbs(targetWidth, targetHeight);
            this.clearCallbacksAndListener();
        }
        
        void clearCallbacksAndListener() {
            final ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this.layoutListener);
            }
            this.layoutListener = null;
            this.cbs.clear();
        }
        
        void getSize(final SizeReadyCallback sizeReadyCallback) {
            final int targetWidth = this.getTargetWidth();
            final int targetHeight = this.getTargetHeight();
            if (this.isViewStateAndSizeValid(targetWidth, targetHeight)) {
                sizeReadyCallback.onSizeReady(targetWidth, targetHeight);
                return;
            }
            if (!this.cbs.contains(sizeReadyCallback)) {
                this.cbs.add(sizeReadyCallback);
            }
            if (this.layoutListener == null) {
                this.view.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)(this.layoutListener = new SizeDeterminerLayoutListener(this)));
            }
        }
        
        void removeCallback(final SizeReadyCallback sizeReadyCallback) {
            this.cbs.remove(sizeReadyCallback);
        }
        
        private static final class SizeDeterminerLayoutListener implements ViewTreeObserver$OnPreDrawListener
        {
            private final WeakReference<SizeDeterminer> sizeDeterminerRef;
            
            SizeDeterminerLayoutListener(final SizeDeterminer referent) {
                this.sizeDeterminerRef = new WeakReference<SizeDeterminer>(referent);
            }
            
            public boolean onPreDraw() {
                if (Log.isLoggable("ViewTarget", 2)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("OnGlobalLayoutListener called listener=");
                    sb.append(this);
                    Log.v("ViewTarget", sb.toString());
                }
                final SizeDeterminer sizeDeterminer = this.sizeDeterminerRef.get();
                if (sizeDeterminer != null) {
                    sizeDeterminer.checkCurrentDimens();
                }
                return true;
            }
        }
    }
}
