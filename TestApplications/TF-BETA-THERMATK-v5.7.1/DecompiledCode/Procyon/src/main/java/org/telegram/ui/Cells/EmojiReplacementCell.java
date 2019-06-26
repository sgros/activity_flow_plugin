// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.Emoji;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ImageView$ScaleType;
import android.content.Context;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class EmojiReplacementCell extends FrameLayout
{
    private String emoji;
    private ImageView imageView;
    
    public EmojiReplacementCell(final Context context) {
        super(context);
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f, 1, 0.0f, 5.0f, 0.0f, 0.0f));
    }
    
    public String getEmoji() {
        return this.emoji;
    }
    
    public void invalidate() {
        super.invalidate();
        this.imageView.invalidate();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0f) + this.getPaddingLeft() + this.getPaddingRight(), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(54.0f), 1073741824));
    }
    
    public void setEmoji(final String emoji, final int n) {
        this.emoji = emoji;
        this.imageView.setImageDrawable(Emoji.getEmojiBigDrawable(emoji));
        if (n == -1) {
            this.setBackgroundResource(2131165862);
            this.setPadding(AndroidUtilities.dp(7.0f), 0, 0, 0);
        }
        else if (n == 0) {
            this.setBackgroundResource(2131165861);
            this.setPadding(0, 0, 0, 0);
        }
        else if (n == 1) {
            this.setBackgroundResource(2131165863);
            this.setPadding(0, 0, AndroidUtilities.dp(7.0f), 0);
        }
        else if (n == 2) {
            this.setBackgroundResource(2131165859);
            this.setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), 0);
        }
        final Drawable background = this.getBackground();
        if (background != null) {
            background.setAlpha(230);
            background.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel"), PorterDuff$Mode.MULTIPLY));
        }
    }
}
