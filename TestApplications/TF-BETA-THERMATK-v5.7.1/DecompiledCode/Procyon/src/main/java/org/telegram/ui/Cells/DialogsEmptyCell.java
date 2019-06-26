// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View$MeasureSpec;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$OnTouchListener;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;

public class DialogsEmptyCell extends LinearLayout
{
    private int currentAccount;
    private int currentType;
    private TextView emptyTextView1;
    private TextView emptyTextView2;
    
    public DialogsEmptyCell(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.setGravity(17);
        this.setOrientation(1);
        this.setOnTouchListener((View$OnTouchListener)_$$Lambda$DialogsEmptyCell$7lLGhZthID2bSlrXEXwZZGk1ZsM.INSTANCE);
        (this.emptyTextView1 = new TextView(context)).setTextColor(Theme.getColor("chats_nameMessage_threeLines"));
        this.emptyTextView1.setText((CharSequence)LocaleController.getString("NoChats", 2131559918));
        this.emptyTextView1.setTextSize(1, 20.0f);
        this.emptyTextView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTextView1.setGravity(17);
        this.addView((View)this.emptyTextView1, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 4.0f, 52.0f, 0.0f));
        this.emptyTextView2 = new TextView(context);
        String text;
        final String s = text = LocaleController.getString("NoChatsHelp", 2131559920);
        if (AndroidUtilities.isTablet()) {
            text = s;
            if (!AndroidUtilities.isSmallTablet()) {
                text = s.replace('\n', ' ');
            }
        }
        this.emptyTextView2.setText((CharSequence)text);
        this.emptyTextView2.setTextColor(Theme.getColor("chats_message"));
        this.emptyTextView2.setTextSize(1, 14.0f);
        this.emptyTextView2.setGravity(17);
        this.emptyTextView2.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
        this.addView((View)this.emptyTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 7.0f, 52.0f, 0.0f));
    }
    
    protected void onMeasure(final int n, int n2) {
        if ((n2 = View$MeasureSpec.getSize(n2)) == 0) {
            final int y = AndroidUtilities.displaySize.y;
            final int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
            if (Build$VERSION.SDK_INT >= 21) {
                n2 = AndroidUtilities.statusBarHeight;
            }
            else {
                n2 = 0;
            }
            n2 = y - currentActionBarHeight - n2;
        }
        if (this.currentType == 0) {
            final ArrayList<TLRPC.RecentMeUrl> hintDialogs = MessagesController.getInstance(this.currentAccount).hintDialogs;
            int n3 = n2;
            if (!hintDialogs.isEmpty()) {
                n3 = n2 - (AndroidUtilities.dp(72.0f) * hintDialogs.size() + hintDialogs.size() - 1 + AndroidUtilities.dp(50.0f));
            }
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(n3, 1073741824));
        }
        else {
            super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(166.0f), 1073741824));
        }
    }
    
    public void setType(final int currentType) {
        this.currentType = currentType;
        String text;
        if (this.currentType == 0) {
            final String s = text = LocaleController.getString("NoChatsHelp", 2131559920);
            if (AndroidUtilities.isTablet()) {
                text = s;
                if (!AndroidUtilities.isSmallTablet()) {
                    text = s.replace('\n', ' ');
                }
            }
        }
        else {
            final String s2 = text = LocaleController.getString("NoChatsContactsHelp", 2131559919);
            if (AndroidUtilities.isTablet()) {
                text = s2;
                if (!AndroidUtilities.isSmallTablet()) {
                    text = s2.replace('\n', ' ');
                }
            }
        }
        this.emptyTextView2.setText((CharSequence)text);
    }
}
