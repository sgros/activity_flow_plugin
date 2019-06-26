// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ImageView$ScaleType;
import android.content.Context;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.widget.FrameLayout;

@SuppressLint({ "NewApi" })
public class PhotoAttachCameraCell extends FrameLayout
{
    private ImageView imageView;
    
    public PhotoAttachCameraCell(final Context context) {
        super(context);
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setImageResource(2131165495);
        this.imageView.setBackgroundColor(-16777216);
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(80, 80.0f));
        this.setFocusable(true);
    }
    
    public ImageView getImageView() {
        return this.imageView;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogCameraIcon"), PorterDuff$Mode.MULTIPLY));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
    }
}
