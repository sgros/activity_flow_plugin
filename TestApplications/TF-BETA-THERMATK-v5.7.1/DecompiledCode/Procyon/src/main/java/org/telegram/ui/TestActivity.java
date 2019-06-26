// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.Context;
import android.view.View;
import org.telegram.ui.Components.MediaActionDrawable;
import org.telegram.ui.ActionBar.BaseFragment;

public class TestActivity extends BaseFragment
{
    int num;
    int p;
    
    public TestActivity() {
        this.num = 0;
        this.p = 0;
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle("Test");
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    TestActivity.this.finishFragment();
                }
            }
        });
        final FrameLayout fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(-16777216);
        super.fragmentView = (View)fragmentView;
        final MediaActionDrawable imageDrawable = new MediaActionDrawable();
        imageDrawable.setIcon(2, false);
        final ImageView imageView = new ImageView(context);
        imageView.setImageDrawable((Drawable)imageDrawable);
        imageView.getClass();
        imageDrawable.setDelegate((MediaActionDrawable.MediaActionDrawableDelegate)new _$$Lambda$pmzqDjiJ3K2EPQb0Rq1MYHdTzL0(imageView));
        fragmentView.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 17));
        fragmentView.setOnClickListener((View$OnClickListener)new _$$Lambda$TestActivity$8SA6jL3MHG2fMuwJNTKXrp0KhUw(imageDrawable));
        return super.fragmentView;
    }
}
