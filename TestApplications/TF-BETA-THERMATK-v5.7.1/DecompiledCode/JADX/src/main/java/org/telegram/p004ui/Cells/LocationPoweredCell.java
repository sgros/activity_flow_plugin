package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.LocationPoweredCell */
public class LocationPoweredCell extends FrameLayout {
    private ImageView imageView;
    private TextView textView;
    private TextView textView2;

    public LocationPoweredCell(Context context) {
        super(context);
        LinearLayout linearLayout = new LinearLayout(context);
        addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
        this.textView = new TextView(context);
        this.textView.setTextSize(1, 16.0f);
        TextView textView = this.textView;
        String str = Theme.key_windowBackgroundWhiteGrayText3;
        textView.setTextColor(Theme.getColor(str));
        this.textView.setText("Powered by");
        linearLayout.addView(this.textView, LayoutHelper.createLinear(-2, -2));
        this.imageView = new ImageView(context);
        this.imageView.setImageResource(C1067R.C1065drawable.foursquare);
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
        this.imageView.setPadding(0, AndroidUtilities.m26dp(2.0f), 0, 0);
        linearLayout.addView(this.imageView, LayoutHelper.createLinear(35, -2));
        this.textView2 = new TextView(context);
        this.textView2.setTextSize(1, 16.0f);
        this.textView2.setTextColor(Theme.getColor(str));
        this.textView2.setText("Foursquare");
        linearLayout.addView(this.textView2, LayoutHelper.createLinear(-2, -2));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(56.0f), 1073741824));
    }
}
