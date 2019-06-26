// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.TextView$OnEditorActionListener;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$OnTouchListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import android.widget.Toast;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.view.MotionEvent;
import android.os.Bundle;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;

public class ReportOtherActivity extends BaseFragment
{
    private static final int done_button = 1;
    private long dialog_id;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private View headerLabelView;
    private int message_id;
    
    public ReportOtherActivity(final Bundle bundle) {
        super(bundle);
        this.dialog_id = this.getArguments().getLong("dialog_id", 0L);
        this.message_id = this.getArguments().getInt("message_id", 0);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("ReportChat", 2131560568));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ReportOtherActivity.this.finishFragment();
                }
                else if (n == 1 && ReportOtherActivity.this.firstNameField.getText().length() != 0) {
                    final TLRPC.InputPeer inputPeer = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer((int)ReportOtherActivity.this.dialog_id);
                    TLObject tlObject;
                    if (ReportOtherActivity.this.message_id != 0) {
                        tlObject = new TLRPC.TL_messages_report();
                        ((TLRPC.TL_messages_report)tlObject).peer = inputPeer;
                        ((TLRPC.TL_messages_report)tlObject).id.add(ReportOtherActivity.this.message_id);
                        final TLRPC.TL_inputReportReasonOther reason = new TLRPC.TL_inputReportReasonOther();
                        reason.text = ReportOtherActivity.this.firstNameField.getText().toString();
                        ((TLRPC.TL_messages_report)tlObject).reason = reason;
                    }
                    else {
                        tlObject = new TLRPC.TL_account_reportPeer();
                        ((TLRPC.TL_account_reportPeer)tlObject).peer = MessagesController.getInstance(ReportOtherActivity.this.currentAccount).getInputPeer((int)ReportOtherActivity.this.dialog_id);
                        final TLRPC.TL_inputReportReasonOther reason2 = new TLRPC.TL_inputReportReasonOther();
                        reason2.text = ReportOtherActivity.this.firstNameField.getText().toString();
                        ((TLRPC.TL_account_reportPeer)tlObject).reason = reason2;
                    }
                    ConnectionsManager.getInstance(ReportOtherActivity.this.currentAccount).sendRequest(tlObject, (RequestDelegate)_$$Lambda$ReportOtherActivity$1$PbLFyQbNnsMkC_qS1TkzMcffkwA.INSTANCE);
                    if (ReportOtherActivity.this.getParentActivity() != null) {
                        Toast.makeText((Context)ReportOtherActivity.this.getParentActivity(), (CharSequence)LocaleController.getString("ReportChatSent", 2131560573), 0).show();
                    }
                    ReportOtherActivity.this.finishFragment();
                }
            }
        });
        this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        final LinearLayout fragmentView = new LinearLayout(context);
        (super.fragmentView = (View)fragmentView).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        ((LinearLayout)super.fragmentView).setOrientation(1);
        super.fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$ReportOtherActivity$VcwTn_4nik4XOSC4IbcsIN4IckE.INSTANCE);
        (this.firstNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        final EditTextBoldCursor firstNameField = this.firstNameField;
        final int n = 3;
        firstNameField.setMaxLines(3);
        this.firstNameField.setPadding(0, 0, 0, 0);
        final EditTextBoldCursor firstNameField2 = this.firstNameField;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        firstNameField2.setGravity(gravity);
        this.firstNameField.setInputType(180224);
        this.firstNameField.setImeOptions(6);
        final EditTextBoldCursor firstNameField3 = this.firstNameField;
        int gravity2 = n;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        firstNameField3.setGravity(gravity2);
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ReportOtherActivity$JRCa_EXPGvX6N9BVVFwqPlLZM80(this));
        fragmentView.addView((View)this.firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        this.firstNameField.setHint((CharSequence)LocaleController.getString("ReportChatDescription", 2131560570));
        final EditTextBoldCursor firstNameField4 = this.firstNameField;
        firstNameField4.setSelection(firstNameField4.length());
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated") };
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard((View)this.firstNameField);
        }
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ReportOtherActivity$djrT4sV5rD__jM1owBrq9EJKfOk(this), 100L);
        }
    }
}
