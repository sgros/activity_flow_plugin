// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.FrameLayout;

class PhotoViewer$13 extends FrameLayout
{
    final /* synthetic */ PhotoViewer this$0;
    
    PhotoViewer$13(final PhotoViewer this$0, final Context context) {
        this.this$0 = this$0;
        super(context);
    }
    
    protected void onLayout(final boolean b, int i, int n, int n2, int childCount) {
        childCount = this.getChildCount();
        final int n3 = n2 - i;
        View view = null;
        View child;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if ((int)child.getTag() == -1) {
                child.layout(n3 - this.getPaddingRight() - child.getMeasuredWidth(), this.getPaddingTop(), n3 - this.getPaddingRight() + child.getMeasuredWidth(), this.getPaddingTop() + child.getMeasuredHeight());
                view = child;
            }
            else if ((int)child.getTag() == -2) {
                n2 = (n = n3 - this.getPaddingRight() - child.getMeasuredWidth());
                if (view != null) {
                    n = n2 - (view.getMeasuredWidth() + AndroidUtilities.dp(8.0f));
                }
                child.layout(n, this.getPaddingTop(), child.getMeasuredWidth() + n, this.getPaddingTop() + child.getMeasuredHeight());
            }
            else {
                child.layout(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + child.getMeasuredWidth(), this.getPaddingTop() + child.getMeasuredHeight());
            }
        }
    }
}
