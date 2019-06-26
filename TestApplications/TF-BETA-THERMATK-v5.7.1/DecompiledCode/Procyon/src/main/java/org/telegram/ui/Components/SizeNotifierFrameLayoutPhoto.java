// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Point;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.view.WindowManager;
import android.graphics.Rect;
import android.widget.FrameLayout;

public class SizeNotifierFrameLayoutPhoto extends FrameLayout
{
    private SizeNotifierFrameLayoutPhotoDelegate delegate;
    private int keyboardHeight;
    private Rect rect;
    private WindowManager windowManager;
    private boolean withoutWindow;
    
    public SizeNotifierFrameLayoutPhoto(final Context context) {
        super(context);
        this.rect = new Rect();
    }
    
    public int getKeyboardHeight() {
        final View rootView = this.getRootView();
        this.getWindowVisibleDisplayFrame(this.rect);
        final boolean withoutWindow = this.withoutWindow;
        int statusBarHeight = 0;
        if (withoutWindow) {
            final int height = rootView.getHeight();
            if (this.rect.top != 0) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            final int viewInset = AndroidUtilities.getViewInset(rootView);
            final Rect rect = this.rect;
            return height - statusBarHeight - viewInset - (rect.bottom - rect.top);
        }
        int n;
        if ((n = AndroidUtilities.displaySize.y - this.rect.top - (rootView.getHeight() - AndroidUtilities.getViewInset(rootView))) <= Math.max(AndroidUtilities.dp(10.0f), AndroidUtilities.statusBarHeight)) {
            n = 0;
        }
        return n;
    }
    
    public void notifyHeightChanged() {
        if (this.delegate != null) {
            this.keyboardHeight = this.getKeyboardHeight();
            final Point displaySize = AndroidUtilities.displaySize;
            this.post((Runnable)new Runnable() {
                final /* synthetic */ boolean val$isWidthGreater = displaySize.x > displaySize.y;
                
                @Override
                public void run() {
                    if (SizeNotifierFrameLayoutPhoto.this.delegate != null) {
                        SizeNotifierFrameLayoutPhoto.this.delegate.onSizeChanged(SizeNotifierFrameLayoutPhoto.this.keyboardHeight, this.val$isWidthGreater);
                    }
                }
            });
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.notifyHeightChanged();
    }
    
    public void setDelegate(final SizeNotifierFrameLayoutPhotoDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setWithoutWindow(final boolean withoutWindow) {
        this.withoutWindow = withoutWindow;
    }
    
    public interface SizeNotifierFrameLayoutPhotoDelegate
    {
        void onSizeChanged(final int p0, final boolean p1);
    }
}
