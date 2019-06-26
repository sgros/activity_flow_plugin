// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.LocaleController;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class ArchiveHintInnerCell extends FrameLayout
{
    private TextView headerTextView;
    private ImageView imageView;
    private ImageView imageView2;
    private TextView messageTextView;
    
    public ArchiveHintInnerCell(final Context context, final int n) {
        super(context);
        (this.imageView = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_nameMessage_threeLines"), PorterDuff$Mode.MULTIPLY));
        (this.headerTextView = new TextView(context)).setTextColor(Theme.getColor("chats_nameMessage_threeLines"));
        this.headerTextView.setTextSize(1, 20.0f);
        this.headerTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.headerTextView.setGravity(17);
        this.addView((View)this.headerTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 75.0f, 52.0f, 0.0f));
        (this.messageTextView = new TextView(context)).setTextColor(Theme.getColor("chats_message"));
        this.messageTextView.setTextSize(1, 14.0f);
        this.messageTextView.setGravity(17);
        this.addView((View)this.messageTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 110.0f, 52.0f, 0.0f));
        if (n != 0) {
            if (n != 1) {
                if (n == 2) {
                    this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 18.0f, 0.0f, 0.0f));
                    this.headerTextView.setText((CharSequence)LocaleController.getString("ArchiveHintHeader3", 2131558647));
                    this.messageTextView.setText((CharSequence)LocaleController.getString("ArchiveHintText3", 2131558650));
                    this.imageView.setImageResource(2131165346);
                }
            }
            else {
                this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 18.0f, 0.0f, 0.0f));
                this.headerTextView.setText((CharSequence)LocaleController.getString("ArchiveHintHeader2", 2131558646));
                this.messageTextView.setText((CharSequence)LocaleController.getString("ArchiveHintText2", 2131558649));
                this.imageView.setImageResource(2131165345);
            }
        }
        else {
            this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 20.0f, 8.0f, 0.0f));
            (this.imageView2 = new ImageView(context)).setImageResource(2131165342);
            this.imageView2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_unreadCounter"), PorterDuff$Mode.MULTIPLY));
            this.addView((View)this.imageView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 20.0f, 8.0f, 0.0f));
            this.headerTextView.setText((CharSequence)LocaleController.getString("ArchiveHintHeader1", 2131558645));
            this.messageTextView.setText((CharSequence)LocaleController.getString("ArchiveHintText1", 2131558648));
            this.imageView.setImageResource(2131165343);
        }
    }
}
