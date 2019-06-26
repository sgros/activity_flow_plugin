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
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class LocationPoweredCell extends FrameLayout
{
    private ImageView imageView;
    private TextView textView;
    private TextView textView2;
    
    public LocationPoweredCell(final Context context) {
        super(context);
        final LinearLayout linearLayout = new LinearLayout(context);
        this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        (this.textView = new TextView(context)).setTextSize(1, 16.0f);
        this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.textView.setText((CharSequence)"Powered by");
        linearLayout.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
        (this.imageView = new ImageView(context)).setImageResource(2131165390);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), PorterDuff$Mode.MULTIPLY));
        this.imageView.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        linearLayout.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(35, -2));
        (this.textView2 = new TextView(context)).setTextSize(1, 16.0f);
        this.textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.textView2.setText((CharSequence)"Foursquare");
        linearLayout.addView((View)this.textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
    }
}
