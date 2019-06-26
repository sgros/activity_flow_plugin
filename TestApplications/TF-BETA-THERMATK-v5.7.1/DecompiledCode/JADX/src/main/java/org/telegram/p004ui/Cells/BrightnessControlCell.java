package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.SeekBarView;

/* renamed from: org.telegram.ui.Cells.BrightnessControlCell */
public class BrightnessControlCell extends FrameLayout {
    private ImageView leftImageView;
    private ImageView rightImageView;
    private SeekBarView seekBarView;

    /* Access modifiers changed, original: protected */
    public void didChangedValue(float f) {
    }

    public BrightnessControlCell(Context context) {
        super(context);
        this.leftImageView = new ImageView(context);
        this.leftImageView.setImageResource(C1067R.C1065drawable.brightness_low);
        addView(this.leftImageView, LayoutHelper.createFrame(24, 24.0f, 51, 17.0f, 12.0f, 0.0f, 0.0f));
        this.seekBarView = new SeekBarView(context) {
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.seekBarView.setReportChanges(true);
        this.seekBarView.setDelegate(new C3938-$$Lambda$GN4ZDAm3ZJLTBxjR6_2pFIyDFuo(this));
        addView(this.seekBarView, LayoutHelper.createFrame(-1, 30.0f, 51, 58.0f, 9.0f, 58.0f, 0.0f));
        this.rightImageView = new ImageView(context);
        this.rightImageView.setImageResource(C1067R.C1065drawable.brightness_high);
        addView(this.rightImageView, LayoutHelper.createFrame(24, 24.0f, 53, 0.0f, 12.0f, 17.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ImageView imageView = this.leftImageView;
        String str = Theme.key_profile_actionIcon;
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
        this.rightImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(48.0f), 1073741824));
    }

    public void setProgress(float f) {
        this.seekBarView.setProgress(f);
    }
}
