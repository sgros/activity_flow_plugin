// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.text.TextUtils$TruncateAt;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View$MeasureSpec;
import android.widget.FrameLayout;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.widget.LinearLayout$LayoutParams;
import android.view.View;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.widget.LinearLayout;

public class PhotoPickerSearchCell extends LinearLayout
{
    private PhotoPickerSearchCellDelegate delegate;
    
    public PhotoPickerSearchCell(final Context context, final boolean b) {
        super(context);
        this.setOrientation(0);
        final SearchButton searchButton = new SearchButton(context);
        searchButton.textView1.setText((CharSequence)LocaleController.getString("SearchImages", 2131560650));
        searchButton.textView2.setText((CharSequence)LocaleController.getString("SearchImagesInfo", 2131560651));
        searchButton.imageView.setImageResource(2131165813);
        this.addView((View)searchButton);
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)searchButton.getLayoutParams();
        layoutParams.weight = 0.5f;
        layoutParams.topMargin = AndroidUtilities.dp(4.0f);
        layoutParams.height = AndroidUtilities.dp(48.0f);
        layoutParams.width = 0;
        searchButton.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        searchButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (PhotoPickerSearchCell.this.delegate != null) {
                    PhotoPickerSearchCell.this.delegate.didPressedSearchButton(0);
                }
            }
        });
        final FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(0);
        this.addView((View)frameLayout);
        final LinearLayout$LayoutParams layoutParams2 = (LinearLayout$LayoutParams)frameLayout.getLayoutParams();
        layoutParams2.topMargin = AndroidUtilities.dp(4.0f);
        layoutParams2.height = AndroidUtilities.dp(48.0f);
        layoutParams2.width = AndroidUtilities.dp(4.0f);
        frameLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        final SearchButton searchButton2 = new SearchButton(context);
        searchButton2.textView1.setText((CharSequence)LocaleController.getString("SearchGifs", 2131560648));
        searchButton2.textView2.setText((CharSequence)"GIPHY");
        searchButton2.imageView.setImageResource(2131165812);
        this.addView((View)searchButton2);
        final LinearLayout$LayoutParams layoutParams3 = (LinearLayout$LayoutParams)searchButton2.getLayoutParams();
        layoutParams3.weight = 0.5f;
        layoutParams3.topMargin = AndroidUtilities.dp(4.0f);
        layoutParams3.height = AndroidUtilities.dp(48.0f);
        layoutParams3.width = 0;
        searchButton2.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        if (b) {
            searchButton2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (PhotoPickerSearchCell.this.delegate != null) {
                        PhotoPickerSearchCell.this.delegate.didPressedSearchButton(1);
                    }
                }
            });
        }
        else {
            searchButton2.setAlpha(0.5f);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0f), 1073741824));
    }
    
    public void setDelegate(final PhotoPickerSearchCellDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface PhotoPickerSearchCellDelegate
    {
        void didPressedSearchButton(final int p0);
    }
    
    private class SearchButton extends FrameLayout
    {
        private ImageView imageView;
        private View selector;
        private TextView textView1;
        private TextView textView2;
        
        public SearchButton(final Context context) {
            super(context);
            this.setBackgroundColor(-15066598);
            (this.selector = new View(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.addView(this.selector, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
            (this.textView1 = new TextView(context)).setGravity(16);
            this.textView1.setTextSize(1, 14.0f);
            this.textView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textView1.setTextColor(-1);
            this.textView1.setSingleLine(true);
            this.textView1.setEllipsize(TextUtils$TruncateAt.END);
            this.addView((View)this.textView1, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 51.0f, 8.0f, 4.0f, 0.0f));
            (this.textView2 = new TextView(context)).setGravity(16);
            this.textView2.setTextSize(1, 10.0f);
            this.textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.textView2.setTextColor(-10066330);
            this.textView2.setSingleLine(true);
            this.textView2.setEllipsize(TextUtils$TruncateAt.END);
            this.addView((View)this.textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 51.0f, 26.0f, 4.0f, 0.0f));
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (Build$VERSION.SDK_INT >= 21) {
                this.selector.drawableHotspotChanged(motionEvent.getX(), motionEvent.getY());
            }
            return super.onTouchEvent(motionEvent);
        }
    }
}
