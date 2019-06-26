// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.text.TextPaint;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import android.text.style.ClickableSpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.content.DialogInterface;
import org.telegram.messenger.MessagesStorage;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import android.view.KeyEvent;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.text.method.MovementMethod;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils;
import android.text.SpannableStringBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.view.View$OnTouchListener;
import android.widget.LinearLayout;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import android.view.MotionEvent;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.view.View;
import android.widget.TextView;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChangeUsernameActivity extends BaseFragment
{
    private static final int done_button = 1;
    private int checkReqId;
    private Runnable checkRunnable;
    private TextView checkTextView;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private TextView helpTextView;
    private boolean ignoreCheck;
    private CharSequence infoText;
    private String lastCheckName;
    private boolean lastNameAvailable;
    
    private boolean checkUserName(final String lastCheckName, final boolean b) {
        if (lastCheckName != null && lastCheckName.length() > 0) {
            this.checkTextView.setVisibility(0);
        }
        else {
            this.checkTextView.setVisibility(8);
        }
        if (b && lastCheckName.length() == 0) {
            return true;
        }
        final Runnable checkRunnable = this.checkRunnable;
        if (checkRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(checkRunnable);
            this.checkRunnable = null;
            this.lastCheckName = null;
            if (this.checkReqId != 0) {
                ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.checkReqId, true);
            }
        }
        this.lastNameAvailable = false;
        if (lastCheckName != null) {
            if (lastCheckName.startsWith("_") || lastCheckName.endsWith("_")) {
                this.checkTextView.setText((CharSequence)LocaleController.getString("UsernameInvalid", 2131561028));
                this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                return false;
            }
            for (int i = 0; i < lastCheckName.length(); ++i) {
                final char char1 = lastCheckName.charAt(i);
                if (i == 0 && char1 >= '0' && char1 <= '9') {
                    if (b) {
                        AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidStartNumber", 2131561031));
                    }
                    else {
                        this.checkTextView.setText((CharSequence)LocaleController.getString("UsernameInvalidStartNumber", 2131561031));
                        this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    }
                    return false;
                }
                if ((char1 < '0' || char1 > '9') && (char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z') && char1 != '_') {
                    if (b) {
                        AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalid", 2131561028));
                    }
                    else {
                        this.checkTextView.setText((CharSequence)LocaleController.getString("UsernameInvalid", 2131561028));
                        this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    }
                    return false;
                }
            }
        }
        if (lastCheckName == null || lastCheckName.length() < 5) {
            if (b) {
                AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidShort", 2131561030));
            }
            else {
                this.checkTextView.setText((CharSequence)LocaleController.getString("UsernameInvalidShort", 2131561030));
                this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            }
            return false;
        }
        if (lastCheckName.length() > 32) {
            if (b) {
                AlertsCreator.showSimpleAlert(this, LocaleController.getString("UsernameInvalidLong", 2131561029));
            }
            else {
                this.checkTextView.setText((CharSequence)LocaleController.getString("UsernameInvalidLong", 2131561029));
                this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            }
            return false;
        }
        if (!b) {
            String username;
            if ((username = UserConfig.getInstance(super.currentAccount).getCurrentUser().username) == null) {
                username = "";
            }
            if (lastCheckName.equals(username)) {
                this.checkTextView.setText((CharSequence)LocaleController.formatString("UsernameAvailable", 2131561022, lastCheckName));
                this.checkTextView.setTag((Object)"windowBackgroundWhiteGreenText");
                this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGreenText"));
                return true;
            }
            this.checkTextView.setText((CharSequence)LocaleController.getString("UsernameChecking", 2131561023));
            this.checkTextView.setTag((Object)"windowBackgroundWhiteGrayText8");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
            this.lastCheckName = lastCheckName;
            AndroidUtilities.runOnUIThread(this.checkRunnable = new _$$Lambda$ChangeUsernameActivity$a8ZrhEmIlukHAljdlSoaVTMTqDc(this, lastCheckName), 300L);
        }
        return true;
    }
    
    private void saveName() {
        if (!this.checkUserName(this.firstNameField.getText().toString(), true)) {
            return;
        }
        final TLRPC.User currentUser = UserConfig.getInstance(super.currentAccount).getCurrentUser();
        if (this.getParentActivity() != null) {
            if (currentUser != null) {
                String username;
                if ((username = currentUser.username) == null) {
                    username = "";
                }
                final String string = this.firstNameField.getText().toString();
                if (username.equals(string)) {
                    this.finishFragment();
                    return;
                }
                final AlertDialog alertDialog = new AlertDialog((Context)this.getParentActivity(), 3);
                final TLRPC.TL_account_updateUsername tl_account_updateUsername = new TLRPC.TL_account_updateUsername();
                tl_account_updateUsername.username = string;
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
                final int sendRequest = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_updateUsername, new _$$Lambda$ChangeUsernameActivity$v8ZSRwHtLRMnSL9d8TsxyGq5Q58(this, alertDialog, tl_account_updateUsername), 2);
                ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(sendRequest, super.classGuid);
                alertDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ChangeUsernameActivity$gFWNPcfawPg_VyQnhopqwvS2g8Q(this, sendRequest));
                alertDialog.show();
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("Username", 2131561021));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChangeUsernameActivity.this.finishFragment();
                }
                else if (n == 1) {
                    ChangeUsernameActivity.this.saveName();
                }
            }
        });
        this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        TLRPC.User user;
        if ((user = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId())) == null) {
            user = UserConfig.getInstance(super.currentAccount).getCurrentUser();
        }
        super.fragmentView = (View)new LinearLayout(context);
        final LinearLayout linearLayout = (LinearLayout)super.fragmentView;
        linearLayout.setOrientation(1);
        super.fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChangeUsernameActivity$OwHEXkVzOQRLF0pIctj_xlc9Dcc.INSTANCE);
        (this.firstNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setPadding(0, 0, 0, 0);
        this.firstNameField.setSingleLine(true);
        final EditTextBoldCursor firstNameField = this.firstNameField;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        firstNameField.setGravity(gravity);
        this.firstNameField.setInputType(180224);
        this.firstNameField.setImeOptions(6);
        this.firstNameField.setHint((CharSequence)LocaleController.getString("UsernamePlaceholder", 2131561032));
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ChangeUsernameActivity$I2mPiyaJW258mybxgiHBR_VofGA(this));
        this.firstNameField.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (ChangeUsernameActivity.this.firstNameField.length() > 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("https://");
                    sb.append(MessagesController.getInstance(ChangeUsernameActivity.this.currentAccount).linkPrefix);
                    sb.append("/");
                    sb.append(ChangeUsernameActivity.this.firstNameField.getText());
                    final String string = sb.toString();
                    final String formatString = LocaleController.formatString("UsernameHelpLink", 2131561026, string);
                    final int index = formatString.indexOf(string);
                    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)formatString);
                    if (index >= 0) {
                        spannableStringBuilder.setSpan((Object)new LinkSpan(string), index, string.length() + index, 33);
                    }
                    ChangeUsernameActivity.this.helpTextView.setText(TextUtils.concat(new CharSequence[] { ChangeUsernameActivity.this.infoText, "\n\n", (CharSequence)spannableStringBuilder }));
                }
                else {
                    ChangeUsernameActivity.this.helpTextView.setText(ChangeUsernameActivity.this.infoText);
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                if (ChangeUsernameActivity.this.ignoreCheck) {
                    return;
                }
                final ChangeUsernameActivity this$0 = ChangeUsernameActivity.this;
                this$0.checkUserName(this$0.firstNameField.getText().toString(), false);
            }
        });
        linearLayout.addView((View)this.firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        (this.checkTextView = new TextView(context)).setTextSize(1, 15.0f);
        final TextView checkTextView = this.checkTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        checkTextView.setGravity(gravity2);
        final TextView checkTextView2 = this.checkTextView;
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        linearLayout.addView((View)checkTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n, 24, 12, 24, 0));
        (this.helpTextView = new TextView(context)).setTextSize(1, 15.0f);
        this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
        final TextView helpTextView = this.helpTextView;
        int gravity3;
        if (LocaleController.isRTL) {
            gravity3 = 5;
        }
        else {
            gravity3 = 3;
        }
        helpTextView.setGravity(gravity3);
        this.helpTextView.setText(this.infoText = (CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("UsernameHelp", 2131561025)));
        this.helpTextView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
        this.helpTextView.setHighlightColor(Theme.getColor("windowBackgroundWhiteLinkSelection"));
        this.helpTextView.setMovementMethod((MovementMethod)new LinkMovementMethodMy());
        final TextView helpTextView2 = this.helpTextView;
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        linearLayout.addView((View)helpTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n2, 24, 10, 24, 0));
        this.checkTextView.setVisibility(8);
        if (user != null) {
            final String username = user.username;
            if (username != null && username.length() > 0) {
                this.ignoreCheck = true;
                this.firstNameField.setText((CharSequence)user.username);
                final EditTextBoldCursor firstNameField2 = this.firstNameField;
                firstNameField2.setSelection(firstNameField2.length());
                this.ignoreCheck = false;
            }
        }
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText8"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteRedText4"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGreenText"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGrayText8") };
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
    
    private static class LinkMovementMethodMy extends LinkMovementMethod
    {
        public boolean onTouchEvent(final TextView textView, final Spannable spannable, final MotionEvent motionEvent) {
            try {
                final boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    Selection.removeSelection(spannable);
                }
                return onTouchEvent;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return false;
            }
        }
    }
    
    public class LinkSpan extends ClickableSpan
    {
        private String url;
        
        public LinkSpan(final String url) {
            this.url = url;
        }
        
        public void onClick(final View view) {
            try {
                ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText((CharSequence)"label", (CharSequence)this.url));
                Toast.makeText((Context)ChangeUsernameActivity.this.getParentActivity(), (CharSequence)LocaleController.getString("LinkCopied", 2131559751), 0).show();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        public void updateDrawState(final TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }
}
