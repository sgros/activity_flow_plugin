// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.widget.LinearLayout$LayoutParams;
import android.view.View$OnClickListener;
import org.telegram.messenger.Emoji;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.View;
import android.content.Context;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import android.widget.LinearLayout;

public class BotKeyboardView extends LinearLayout
{
    private TLRPC.TL_replyKeyboardMarkup botButtons;
    private int buttonHeight;
    private ArrayList<TextView> buttonViews;
    private LinearLayout container;
    private BotKeyboardViewDelegate delegate;
    private boolean isFullSize;
    private int panelHeight;
    private ScrollView scrollView;
    
    public BotKeyboardView(final Context context) {
        super(context);
        this.buttonViews = new ArrayList<TextView>();
        this.setOrientation(1);
        this.addView((View)(this.scrollView = new ScrollView(context)));
        (this.container = new LinearLayout(context)).setOrientation(1);
        this.scrollView.addView((View)this.container);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("chat_emojiPanelBackground"));
        this.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
    }
    
    public int getKeyboardHeight() {
        int panelHeight;
        if (this.isFullSize) {
            panelHeight = this.panelHeight;
        }
        else {
            panelHeight = this.botButtons.rows.size() * AndroidUtilities.dp((float)this.buttonHeight) + AndroidUtilities.dp(30.0f) + (this.botButtons.rows.size() - 1) * AndroidUtilities.dp(10.0f);
        }
        return panelHeight;
    }
    
    public void invalidateViews() {
        for (int i = 0; i < this.buttonViews.size(); ++i) {
            this.buttonViews.get(i).invalidate();
        }
    }
    
    public boolean isFullSize() {
        return this.isFullSize;
    }
    
    public void setButtons(final TLRPC.TL_replyKeyboardMarkup botButtons) {
        this.botButtons = botButtons;
        this.container.removeAllViews();
        this.buttonViews.clear();
        this.scrollView.scrollTo(0, 0);
        if (botButtons != null && this.botButtons.rows.size() != 0) {
            int buttonHeight;
            if (!(this.isFullSize = !botButtons.resize)) {
                buttonHeight = 42;
            }
            else {
                buttonHeight = (int)Math.max(42.0f, (this.panelHeight - AndroidUtilities.dp(30.0f) - (this.botButtons.rows.size() - 1) * AndroidUtilities.dp(10.0f)) / this.botButtons.rows.size() / AndroidUtilities.density);
            }
            this.buttonHeight = buttonHeight;
            for (int i = 0; i < botButtons.rows.size(); ++i) {
                final TLRPC.TL_keyboardButtonRow tl_keyboardButtonRow = botButtons.rows.get(i);
                final LinearLayout linearLayout = new LinearLayout(this.getContext());
                linearLayout.setOrientation(0);
                final LinearLayout container = this.container;
                final int buttonHeight2 = this.buttonHeight;
                float n;
                if (i == 0) {
                    n = 15.0f;
                }
                else {
                    n = 10.0f;
                }
                float n2;
                if (i == botButtons.rows.size() - 1) {
                    n2 = 15.0f;
                }
                else {
                    n2 = 0.0f;
                }
                container.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, buttonHeight2, 15.0f, n, 15.0f, n2));
                final float n3 = 1.0f / tl_keyboardButtonRow.buttons.size();
                for (int j = 0; j < tl_keyboardButtonRow.buttons.size(); ++j) {
                    final TLRPC.KeyboardButton tag = tl_keyboardButtonRow.buttons.get(j);
                    final TextView e = new TextView(this.getContext());
                    e.setTag((Object)tag);
                    e.setTextColor(Theme.getColor("chat_botKeyboardButtonText"));
                    e.setTextSize(1, 16.0f);
                    e.setGravity(17);
                    e.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor("chat_botKeyboardButtonBackground"), Theme.getColor("chat_botKeyboardButtonBackgroundPressed")));
                    e.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
                    e.setText(Emoji.replaceEmoji(tag.text, e.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
                    int n4;
                    if (j != tl_keyboardButtonRow.buttons.size() - 1) {
                        n4 = 10;
                    }
                    else {
                        n4 = 0;
                    }
                    linearLayout.addView((View)e, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -1, n3, 0, 0, n4, 0));
                    e.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            BotKeyboardView.this.delegate.didPressedButton((TLRPC.KeyboardButton)view.getTag());
                        }
                    });
                    this.buttonViews.add(e);
                }
            }
        }
    }
    
    public void setDelegate(final BotKeyboardViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setPanelHeight(int i) {
        this.panelHeight = i;
        if (this.isFullSize) {
            final TLRPC.TL_replyKeyboardMarkup botButtons = this.botButtons;
            if (botButtons != null && botButtons.rows.size() != 0) {
                if (!this.isFullSize) {
                    i = 42;
                }
                else {
                    i = (int)Math.max(42.0f, (this.panelHeight - AndroidUtilities.dp(30.0f) - (this.botButtons.rows.size() - 1) * AndroidUtilities.dp(10.0f)) / this.botButtons.rows.size() / AndroidUtilities.density);
                }
                this.buttonHeight = i;
                final int childCount = this.container.getChildCount();
                final int dp = AndroidUtilities.dp((float)this.buttonHeight);
                View child;
                LinearLayout$LayoutParams layoutParams;
                for (i = 0; i < childCount; ++i) {
                    child = this.container.getChildAt(i);
                    layoutParams = (LinearLayout$LayoutParams)child.getLayoutParams();
                    if (layoutParams.height != dp) {
                        layoutParams.height = dp;
                        child.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    }
                }
            }
        }
    }
    
    public interface BotKeyboardViewDelegate
    {
        void didPressedButton(final TLRPC.KeyboardButton p0);
    }
}
