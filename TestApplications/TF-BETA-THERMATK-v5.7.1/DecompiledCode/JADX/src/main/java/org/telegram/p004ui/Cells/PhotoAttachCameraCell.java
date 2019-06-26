package org.telegram.p004ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

@SuppressLint({"NewApi"})
/* renamed from: org.telegram.ui.Cells.PhotoAttachCameraCell */
public class PhotoAttachCameraCell extends FrameLayout {
    private ImageView imageView;

    public PhotoAttachCameraCell(Context context) {
        super(context);
        this.imageView = new ImageView(context);
        this.imageView.setScaleType(ScaleType.CENTER);
        this.imageView.setImageResource(C1067R.C1065drawable.instant_camera);
        this.imageView.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        addView(this.imageView, LayoutHelper.createFrame(80, 80.0f));
        setFocusable(true);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(86.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(80.0f), 1073741824));
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogCameraIcon), Mode.MULTIPLY));
    }
}
