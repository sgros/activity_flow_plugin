package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.ChatUnreadCell */
public class ChatUnreadCell extends FrameLayout {
    private FrameLayout backgroundLayout;
    private ImageView imageView;
    private TextView textView;

    public ChatUnreadCell(Context context) {
        super(context);
        this.backgroundLayout = new FrameLayout(context);
        this.backgroundLayout.setBackgroundResource(C1067R.C1065drawable.newmsg_divider);
        this.backgroundLayout.getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_unreadMessagesStartBackground), Mode.MULTIPLY));
        addView(this.backgroundLayout, LayoutHelper.createFrame(-1, 27.0f, 51, 0.0f, 7.0f, 0.0f, 0.0f));
        this.imageView = new ImageView(context);
        this.imageView.setImageResource(C1067R.C1065drawable.ic_ab_new);
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_unreadMessagesStartArrowIcon), Mode.MULTIPLY));
        this.imageView.setPadding(0, AndroidUtilities.m26dp(2.0f), 0, 0);
        this.backgroundLayout.addView(this.imageView, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 10.0f, 0.0f));
        this.textView = new TextView(context);
        this.textView.setPadding(0, 0, 0, AndroidUtilities.m26dp(1.0f));
        this.textView.setTextSize(1, 14.0f);
        this.textView.setTextColor(Theme.getColor(Theme.key_chat_unreadMessagesStartText));
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
    }

    public void setText(String str) {
        this.textView.setText(str);
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public FrameLayout getBackgroundLayout() {
        return this.backgroundLayout;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(40.0f), 1073741824));
    }
}
