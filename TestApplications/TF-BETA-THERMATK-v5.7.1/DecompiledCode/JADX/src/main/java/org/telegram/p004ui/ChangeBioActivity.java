package org.telegram.p004ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_updateProfile;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.UserFull;

/* renamed from: org.telegram.ui.ChangeBioActivity */
public class ChangeBioActivity extends BaseFragment {
    private static final int done_button = 1;
    private TextView checkTextView;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private TextView helpTextView;

    /* renamed from: org.telegram.ui.ChangeBioActivity$3 */
    class C23773 implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C23773() {
        }

        public void afterTextChanged(Editable editable) {
            ChangeBioActivity.this.checkTextView.setText(String.format("%d", new Object[]{Integer.valueOf(70 - ChangeBioActivity.this.firstNameField.length())}));
        }
    }

    /* renamed from: org.telegram.ui.ChangeBioActivity$1 */
    class C39421 extends ActionBarMenuOnItemClick {
        C39421() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ChangeBioActivity.this.finishFragment();
            } else if (i == 1) {
                ChangeBioActivity.this.saveName();
            }
        }
    }

    public View createView(Context context) {
        Context context2 = context;
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        String str = "UserBio";
        this.actionBar.setTitle(LocaleController.getString(str, C1067R.string.UserBio));
        this.actionBar.setActionBarMenuOnItemClick(new C39421());
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, C1067R.C1065drawable.ic_done, AndroidUtilities.m26dp(56.0f));
        this.fragmentView = new LinearLayout(context2);
        LinearLayout linearLayout = (LinearLayout) this.fragmentView;
        linearLayout.setOrientation(1);
        this.fragmentView.setOnTouchListener(C1235-$$Lambda$ChangeBioActivity$QHxoa1XUfskp3fsOABXGjgD9Sl4.INSTANCE);
        FrameLayout frameLayout = new FrameLayout(context2);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 24.0f, 24.0f, 20.0f, 0.0f));
        this.firstNameField = new EditTextBoldCursor(context2);
        this.firstNameField.setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor = this.firstNameField;
        String str2 = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor.setTextColor(Theme.getColor(str2));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.firstNameField.setMaxLines(4);
        editTextBoldCursor = this.firstNameField;
        float f = 24.0f;
        int dp = AndroidUtilities.m26dp(LocaleController.isRTL ? 24.0f : 0.0f);
        if (LocaleController.isRTL) {
            f = 0.0f;
        }
        editTextBoldCursor.setPadding(dp, 0, AndroidUtilities.m26dp(f), AndroidUtilities.m26dp(6.0f));
        this.firstNameField.setGravity(LocaleController.isRTL ? 5 : 3);
        this.firstNameField.setImeOptions(268435456);
        this.firstNameField.setInputType(147457);
        this.firstNameField.setImeOptions(6);
        this.firstNameField.setFilters(new InputFilter[]{new LengthFilter(70) {
            public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
                if (charSequence == null || TextUtils.indexOf(charSequence, 10) == -1) {
                    CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
                    if (!(filter == null || charSequence == null || filter.length() == charSequence.length())) {
                        Vibrator vibrator = (Vibrator) ChangeBioActivity.this.getParentActivity().getSystemService("vibrator");
                        if (vibrator != null) {
                            vibrator.vibrate(200);
                        }
                        AndroidUtilities.shakeView(ChangeBioActivity.this.checkTextView, 2.0f, 0);
                    }
                    return filter;
                }
                ChangeBioActivity.this.doneButton.performClick();
                return "";
            }
        }});
        this.firstNameField.setMinHeight(AndroidUtilities.m26dp(36.0f));
        this.firstNameField.setHint(LocaleController.getString(str, C1067R.string.UserBio));
        this.firstNameField.setCursorColor(Theme.getColor(str2));
        this.firstNameField.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        this.firstNameField.setOnEditorActionListener(new C1233-$$Lambda$ChangeBioActivity$4xLMdC_iGI1lEMvcHXGPYTNQNU8(this));
        this.firstNameField.addTextChangedListener(new C23773());
        frameLayout.addView(this.firstNameField, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 0.0f, 4.0f, 0.0f));
        this.checkTextView = new TextView(context2);
        this.checkTextView.setTextSize(1, 15.0f);
        this.checkTextView.setText(String.format("%d", new Object[]{Integer.valueOf(70)}));
        this.checkTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText4));
        frameLayout.addView(this.checkTextView, LayoutHelper.createFrame(-2, -2.0f, LocaleController.isRTL ? 3 : 5, 0.0f, 4.0f, 4.0f, 0.0f));
        this.helpTextView = new TextView(context2);
        this.helpTextView.setTextSize(1, 15.0f);
        this.helpTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText8));
        this.helpTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.helpTextView.setText(AndroidUtilities.replaceTags(LocaleController.getString("UserBioInfo", C1067R.string.UserBioInfo)));
        linearLayout.addView(this.helpTextView, LayoutHelper.createLinear(-2, -2, LocaleController.isRTL ? 5 : 3, 24, 10, 24, 0));
        UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(UserConfig.getInstance(this.currentAccount).getClientUserId());
        if (userFull != null) {
            String str3 = userFull.about;
            if (str3 != null) {
                this.firstNameField.setText(str3);
                EditTextBoldCursor editTextBoldCursor2 = this.firstNameField;
                editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
            }
        }
        return this.fragmentView;
    }

    public /* synthetic */ boolean lambda$createView$1$ChangeBioActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            View view = this.doneButton;
            if (view != null) {
                view.performClick();
                return true;
            }
        }
        return false;
    }

    public void onResume() {
        super.onResume();
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    private void saveName() {
        UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(UserConfig.getInstance(this.currentAccount).getClientUserId());
        if (!(getParentActivity() == null || userFull == null)) {
            String str = userFull.about;
            String str2 = "";
            if (str == null) {
                str = str2;
            }
            String replace = this.firstNameField.getText().toString().replace("\n", str2);
            if (str.equals(replace)) {
                finishFragment();
                return;
            }
            AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
            TL_account_updateProfile tL_account_updateProfile = new TL_account_updateProfile();
            tL_account_updateProfile.about = replace;
            tL_account_updateProfile.flags |= 4;
            int sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_updateProfile, new C3596-$$Lambda$ChangeBioActivity$JZsWl0x8uGEwh0dAVWe1jdDZvHs(this, alertDialog, userFull, replace, tL_account_updateProfile), 2);
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, this.classGuid);
            alertDialog.setOnCancelListener(new C1234-$$Lambda$ChangeBioActivity$Q4bba-1YALoLViuYxQRzjsdV-6c(this, sendRequest));
            alertDialog.show();
        }
    }

    public /* synthetic */ void lambda$saveName$4$ChangeBioActivity(AlertDialog alertDialog, UserFull userFull, String str, TL_account_updateProfile tL_account_updateProfile, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null) {
            AndroidUtilities.runOnUIThread(new C1237-$$Lambda$ChangeBioActivity$tNpkkFgAdE6xw7aDpwDOLm8jmpc(this, alertDialog, userFull, str, (User) tLObject));
            return;
        }
        AndroidUtilities.runOnUIThread(new C1236-$$Lambda$ChangeBioActivity$T2lBd_q-6K_Uw6G0hNAJdgSSX7I(this, alertDialog, tL_error, tL_account_updateProfile));
    }

    public /* synthetic */ void lambda$null$2$ChangeBioActivity(AlertDialog alertDialog, UserFull userFull, String str, User user) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        userFull.about = str;
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.userInfoDidLoad, Integer.valueOf(user.f534id), userFull, null);
        finishFragment();
    }

    public /* synthetic */ void lambda$null$3$ChangeBioActivity(AlertDialog alertDialog, TL_error tL_error, TL_account_updateProfile tL_account_updateProfile) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        AlertsCreator.processError(this.currentAccount, tL_error, this, tL_account_updateProfile, new Object[0]);
    }

    public /* synthetic */ void lambda$saveName$5$ChangeBioActivity(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[]{new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField), new ThemeDescription(this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated), new ThemeDescription(this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText8), new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText4)};
    }
}
