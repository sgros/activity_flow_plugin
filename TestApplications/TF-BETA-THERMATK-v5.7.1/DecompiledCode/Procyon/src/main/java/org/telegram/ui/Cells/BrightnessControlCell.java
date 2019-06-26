// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.content.Context;
import org.telegram.ui.Components.SeekBarView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class BrightnessControlCell extends FrameLayout
{
    private ImageView leftImageView;
    private ImageView rightImageView;
    private SeekBarView seekBarView;
    
    public BrightnessControlCell(final Context context) {
        super(context);
        (this.leftImageView = new ImageView(context)).setImageResource(2131165324);
        this.addView((View)this.leftImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, 51, 17.0f, 12.0f, 0.0f, 0.0f));
        (this.seekBarView = new SeekBarView(context) {
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        }).setReportChanges(true);
        this.seekBarView.setDelegate((SeekBarView.SeekBarViewDelegate)new _$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo(this));
        this.addView((View)this.seekBarView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 30.0f, 51, 58.0f, 9.0f, 58.0f, 0.0f));
        (this.rightImageView = new ImageView(context)).setImageResource(2131165323);
        this.addView((View)this.rightImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 12.0f, 17.0f, 0.0f));
    }
    
    protected void didChangedValue(final float n) {
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.leftImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), PorterDuff$Mode.MULTIPLY));
        this.rightImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), PorterDuff$Mode.MULTIPLY));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
    }
    
    public void setProgress(final float progress) {
        this.seekBarView.setProgress(progress);
    }
}
