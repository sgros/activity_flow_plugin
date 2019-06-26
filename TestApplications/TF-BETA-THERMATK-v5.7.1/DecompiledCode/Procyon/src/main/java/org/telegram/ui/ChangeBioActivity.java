// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.FileLog;
import android.view.KeyEvent;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.os.Vibrator;
import android.text.TextUtils;
import android.text.Spanned;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import android.view.View$OnTouchListener;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import android.view.MotionEvent;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.view.View;
import android.widget.TextView;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChangeBioActivity extends BaseFragment
{
    private static final int done_button = 1;
    private TextView checkTextView;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private TextView helpTextView;
    
    private void saveName() {
        final TLRPC.UserFull userFull = MessagesController.getInstance(super.currentAccount).getUserFull(UserConfig.getInstance(super.currentAccount).getClientUserId());
        if (this.getParentActivity() != null) {
            if (userFull != null) {
                String about;
                if ((about = userFull.about) == null) {
                    about = "";
                }
                final String replace = this.firstNameField.getText().toString().replace("\n", "");
                if (about.equals(replace)) {
                    this.finishFragment();
                    return;
                }
                final AlertDialog alertDialog = new AlertDialog((Context)this.getParentActivity(), 3);
                final TLRPC.TL_account_updateProfile tl_account_updateProfile = new TLRPC.TL_account_updateProfile();
                tl_account_updateProfile.about = replace;
                tl_account_updateProfile.flags |= 0x4;
                final int sendRequest = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_updateProfile, new _$$Lambda$ChangeBioActivity$JZsWl0x8uGEwh0dAVWe1jdDZvHs(this, alertDialog, userFull, replace, tl_account_updateProfile), 2);
                ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(sendRequest, super.classGuid);
                alertDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ChangeBioActivity$Q4bba_1YALoLViuYxQRzjsdV_6c(this, sendRequest));
                alertDialog.show();
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("UserBio", 2131560987));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChangeBioActivity.this.finishFragment();
                }
                else if (n == 1) {
                    ChangeBioActivity.this.saveName();
                }
            }
        });
        this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        super.fragmentView = (View)new LinearLayout(context);
        final LinearLayout linearLayout = (LinearLayout)super.fragmentView;
        linearLayout.setOrientation(1);
        super.fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChangeBioActivity$QHxoa1XUfskp3fsOABXGjgD9Sl4.INSTANCE);
        final FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 24.0f, 24.0f, 20.0f, 0.0f));
        (this.firstNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.firstNameField.setMaxLines(4);
        final EditTextBoldCursor firstNameField = this.firstNameField;
        final boolean isRTL = LocaleController.isRTL;
        final float n = 24.0f;
        float n2;
        if (isRTL) {
            n2 = 24.0f;
        }
        else {
            n2 = 0.0f;
        }
        final int dp = AndroidUtilities.dp(n2);
        float n3 = n;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        firstNameField.setPadding(dp, 0, AndroidUtilities.dp(n3), AndroidUtilities.dp(6.0f));
        final EditTextBoldCursor firstNameField2 = this.firstNameField;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        firstNameField2.setGravity(gravity);
        this.firstNameField.setImeOptions(268435456);
        this.firstNameField.setInputType(147457);
        this.firstNameField.setImeOptions(6);
        this.firstNameField.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(70) {
                public CharSequence filter(final CharSequence charSequence, final int n, final int n2, final Spanned spanned, final int n3, final int n4) {
                    if (charSequence != null && TextUtils.indexOf(charSequence, '\n') != -1) {
                        ChangeBioActivity.this.doneButton.performClick();
                        return "";
                    }
                    final CharSequence filter = super.filter(charSequence, n, n2, spanned, n3, n4);
                    if (filter != null && charSequence != null && filter.length() != charSequence.length()) {
                        final Vibrator vibrator = (Vibrator)ChangeBioActivity.this.getParentActivity().getSystemService("vibrator");
                        if (vibrator != null) {
                            vibrator.vibrate(200L);
                        }
                        AndroidUtilities.shakeView((View)ChangeBioActivity.this.checkTextView, 2.0f, 0);
                    }
                    return filter;
                }
            } });
        this.firstNameField.setMinHeight(AndroidUtilities.dp(36.0f));
        this.firstNameField.setHint((CharSequence)LocaleController.getString("UserBio", 2131560987));
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ChangeBioActivity$4xLMdC_iGI1lEMvcHXGPYTNQNU8(this));
        this.firstNameField.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                ChangeBioActivity.this.checkTextView.setText((CharSequence)String.format("%d", 70 - ChangeBioActivity.this.firstNameField.length()));
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        frameLayout.addView((View)this.firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 4.0f, 0.0f));
        (this.checkTextView = new TextView(context)).setTextSize(1, 15.0f);
        this.checkTextView.setText((CharSequence)String.format("%d", 70));
        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
        final TextView checkTextView = this.checkTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        frameLayout.addView((View)checkTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n4, 0.0f, 4.0f, 4.0f, 0.0f));
        (this.helpTextView = new TextView(context)).setTextSize(1, 15.0f);
        this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
        final TextView helpTextView = this.helpTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        helpTextView.setGravity(gravity2);
        this.helpTextView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("UserBioInfo", 2131560990)));
        final TextView helpTextView2 = this.helpTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        linearLayout.addView((View)helpTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n5, 24, 10, 24, 0));
        final TLRPC.UserFull userFull = MessagesController.getInstance(super.currentAccount).getUserFull(UserConfig.getInstance(super.currentAccount).getClientUserId());
        if (userFull != null) {
            final String about = userFull.about;
            if (about != null) {
                this.firstNameField.setText((CharSequence)about);
                final EditTextBoldCursor firstNameField3 = this.firstNameField;
                firstNameField3.setSelection(firstNameField3.length());
            }
        }
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText8"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText4") };
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
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard((View)this.firstNameField);
        }
    }
}
