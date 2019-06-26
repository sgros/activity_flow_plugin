package org.telegram.p004ui.Components;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC.TL_replyKeyboardMarkup;

/* renamed from: org.telegram.ui.Components.BotKeyboardView */
public class BotKeyboardView extends LinearLayout {
    private TL_replyKeyboardMarkup botButtons;
    private int buttonHeight;
    private ArrayList<TextView> buttonViews = new ArrayList();
    private LinearLayout container;
    private BotKeyboardViewDelegate delegate;
    private boolean isFullSize;
    private int panelHeight;
    private ScrollView scrollView;

    /* renamed from: org.telegram.ui.Components.BotKeyboardView$1 */
    class C27411 implements OnClickListener {
        C27411() {
        }

        public void onClick(View view) {
            BotKeyboardView.this.delegate.didPressedButton((KeyboardButton) view.getTag());
        }
    }

    /* renamed from: org.telegram.ui.Components.BotKeyboardView$BotKeyboardViewDelegate */
    public interface BotKeyboardViewDelegate {
        void didPressedButton(KeyboardButton keyboardButton);
    }

    public BotKeyboardView(Context context) {
        super(context);
        setOrientation(1);
        this.scrollView = new ScrollView(context);
        addView(this.scrollView);
        this.container = new LinearLayout(context);
        this.container.setOrientation(1);
        this.scrollView.addView(this.container);
        ScrollView scrollView = this.scrollView;
        String str = Theme.key_chat_emojiPanelBackground;
        AndroidUtilities.setScrollViewEdgeEffectColor(scrollView, Theme.getColor(str));
        setBackgroundColor(Theme.getColor(str));
    }

    public void setDelegate(BotKeyboardViewDelegate botKeyboardViewDelegate) {
        this.delegate = botKeyboardViewDelegate;
    }

    public void setPanelHeight(int i) {
        this.panelHeight = i;
        if (this.isFullSize) {
            TL_replyKeyboardMarkup tL_replyKeyboardMarkup = this.botButtons;
            if (tL_replyKeyboardMarkup != null && tL_replyKeyboardMarkup.rows.size() != 0) {
                this.buttonHeight = !this.isFullSize ? 42 : (int) Math.max(42.0f, ((float) (((this.panelHeight - AndroidUtilities.m26dp(30.0f)) - ((this.botButtons.rows.size() - 1) * AndroidUtilities.m26dp(10.0f))) / this.botButtons.rows.size())) / AndroidUtilities.density);
                i = this.container.getChildCount();
                int dp = AndroidUtilities.m26dp((float) this.buttonHeight);
                for (int i2 = 0; i2 < i; i2++) {
                    View childAt = this.container.getChildAt(i2);
                    LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                    if (layoutParams.height != dp) {
                        layoutParams.height = dp;
                        childAt.setLayoutParams(layoutParams);
                    }
                }
            }
        }
    }

    public void invalidateViews() {
        for (int i = 0; i < this.buttonViews.size(); i++) {
            ((TextView) this.buttonViews.get(i)).invalidate();
        }
    }

    public boolean isFullSize() {
        return this.isFullSize;
    }

    public void setButtons(TL_replyKeyboardMarkup tL_replyKeyboardMarkup) {
        TL_replyKeyboardMarkup tL_replyKeyboardMarkup2 = tL_replyKeyboardMarkup;
        this.botButtons = tL_replyKeyboardMarkup2;
        this.container.removeAllViews();
        this.buttonViews.clear();
        int i = 0;
        this.scrollView.scrollTo(0, 0);
        if (tL_replyKeyboardMarkup2 != null && this.botButtons.rows.size() != 0) {
            this.isFullSize = !tL_replyKeyboardMarkup2.resize;
            this.buttonHeight = !this.isFullSize ? 42 : (int) Math.max(42.0f, ((float) (((this.panelHeight - AndroidUtilities.m26dp(30.0f)) - ((this.botButtons.rows.size() - 1) * AndroidUtilities.m26dp(10.0f))) / this.botButtons.rows.size())) / AndroidUtilities.density);
            int i2 = 0;
            while (i2 < tL_replyKeyboardMarkup2.rows.size()) {
                TL_keyboardButtonRow tL_keyboardButtonRow = (TL_keyboardButtonRow) tL_replyKeyboardMarkup2.rows.get(i2);
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(i);
                this.container.addView(linearLayout, LayoutHelper.createLinear(-1, this.buttonHeight, 15.0f, i2 == 0 ? 15.0f : 10.0f, 15.0f, i2 == tL_replyKeyboardMarkup2.rows.size() - 1 ? 15.0f : 0.0f));
                float size = 1.0f / ((float) tL_keyboardButtonRow.buttons.size());
                int i3 = 0;
                while (i3 < tL_keyboardButtonRow.buttons.size()) {
                    KeyboardButton keyboardButton = (KeyboardButton) tL_keyboardButtonRow.buttons.get(i3);
                    View textView = new TextView(getContext());
                    textView.setTag(keyboardButton);
                    textView.setTextColor(Theme.getColor(Theme.key_chat_botKeyboardButtonText));
                    textView.setTextSize(1, 16.0f);
                    textView.setGravity(17);
                    textView.setBackgroundDrawable(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.m26dp(4.0f), Theme.getColor(Theme.key_chat_botKeyboardButtonBackground), Theme.getColor(Theme.key_chat_botKeyboardButtonBackgroundPressed)));
                    textView.setPadding(AndroidUtilities.m26dp(4.0f), i, AndroidUtilities.m26dp(4.0f), i);
                    textView.setText(Emoji.replaceEmoji(keyboardButton.text, textView.getPaint().getFontMetricsInt(), AndroidUtilities.m26dp(16.0f), i));
                    View view = textView;
                    linearLayout.addView(view, LayoutHelper.createLinear(0, -1, size, 0, 0, i3 != tL_keyboardButtonRow.buttons.size() - 1 ? 10 : 0, 0));
                    view.setOnClickListener(new C27411());
                    this.buttonViews.add(view);
                    i3++;
                    i = 0;
                }
                i2++;
                i = 0;
            }
        }
    }

    public int getKeyboardHeight() {
        return this.isFullSize ? this.panelHeight : ((this.botButtons.rows.size() * AndroidUtilities.m26dp((float) this.buttonHeight)) + AndroidUtilities.m26dp(30.0f)) + ((this.botButtons.rows.size() - 1) * AndroidUtilities.m26dp(10.0f));
    }
}
