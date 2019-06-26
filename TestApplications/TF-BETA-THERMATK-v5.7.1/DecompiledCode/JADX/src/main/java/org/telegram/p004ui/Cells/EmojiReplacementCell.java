package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.Emoji;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.EmojiReplacementCell */
public class EmojiReplacementCell extends FrameLayout {
    private String emoji;
    private ImageView imageView;

    public EmojiReplacementCell(Context context) {
        super(context);
        this.imageView = new ImageView(context);
        this.imageView.setScaleType(ScaleType.CENTER);
        addView(this.imageView, LayoutHelper.createFrame(42, 42.0f, 1, 0.0f, 5.0f, 0.0f, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec((AndroidUtilities.m26dp(52.0f) + getPaddingLeft()) + getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(54.0f), 1073741824));
    }

    public void setEmoji(String str, int i) {
        this.emoji = str;
        this.imageView.setImageDrawable(Emoji.getEmojiBigDrawable(str));
        if (i == -1) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_left);
            setPadding(AndroidUtilities.m26dp(7.0f), 0, 0, 0);
        } else if (i == 0) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_center);
            setPadding(0, 0, 0, 0);
        } else if (i == 1) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_right);
            setPadding(0, 0, AndroidUtilities.m26dp(7.0f), 0);
        } else if (i == 2) {
            setBackgroundResource(C1067R.C1065drawable.stickers_back_all);
            setPadding(AndroidUtilities.m26dp(3.0f), 0, AndroidUtilities.m26dp(3.0f), 0);
        }
        Drawable background = getBackground();
        if (background != null) {
            background.setAlpha(230);
            background.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_stickersHintPanel), Mode.MULTIPLY));
        }
    }

    public String getEmoji() {
        return this.emoji;
    }

    public void invalidate() {
        super.invalidate();
        this.imageView.invalidate();
    }
}
