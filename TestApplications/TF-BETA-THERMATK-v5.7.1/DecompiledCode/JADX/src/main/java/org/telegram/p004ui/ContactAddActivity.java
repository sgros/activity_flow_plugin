package org.telegram.p004ui;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.telegram.PhoneFormat.C0278PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.ContactAddActivity */
public class ContactAddActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int done_button = 1;
    private boolean addContact;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private EditTextBoldCursor lastNameField;
    private TextView nameTextView;
    private TextView onlineTextView;
    private String phone = null;
    private int user_id;

    /* renamed from: org.telegram.ui.ContactAddActivity$1 */
    class C41551 extends ActionBarMenuOnItemClick {
        C41551() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ContactAddActivity.this.finishFragment();
            } else if (i == 1 && ContactAddActivity.this.firstNameField.getText().length() != 0) {
                User user = MessagesController.getInstance(ContactAddActivity.this.currentAccount).getUser(Integer.valueOf(ContactAddActivity.this.user_id));
                user.first_name = ContactAddActivity.this.firstNameField.getText().toString();
                user.last_name = ContactAddActivity.this.lastNameField.getText().toString();
                ContactsController.getInstance(ContactAddActivity.this.currentAccount).addContact(user);
                ContactAddActivity.this.finishFragment();
                Editor edit = MessagesController.getNotificationsSettings(ContactAddActivity.this.currentAccount).edit();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("spam3_");
                stringBuilder.append(ContactAddActivity.this.user_id);
                edit.putInt(stringBuilder.toString(), 1).commit();
                NotificationCenter.getInstance(ContactAddActivity.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(1));
                NotificationCenter.getInstance(ContactAddActivity.this.currentAccount).postNotificationName(NotificationCenter.peerSettingsDidLoad, Long.valueOf((long) ContactAddActivity.this.user_id));
            }
        }
    }

    public ContactAddActivity(Bundle bundle) {
        super(bundle);
    }

    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        this.user_id = getArguments().getInt("user_id", 0);
        this.phone = getArguments().getString("phone");
        this.addContact = getArguments().getBoolean("addContact", false);
        if (MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id)) == null || !super.onFragmentCreate()) {
            return false;
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
    }

    public View createView(Context context) {
        Context context2 = context;
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.addContact) {
            this.actionBar.setTitle(LocaleController.getString("AddContactTitle", C1067R.string.AddContactTitle));
        } else {
            this.actionBar.setTitle(LocaleController.getString("EditName", C1067R.string.EditName));
        }
        this.actionBar.setActionBarMenuOnItemClick(new C41551());
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, C1067R.C1065drawable.ic_done, AndroidUtilities.m26dp(56.0f));
        this.fragmentView = new ScrollView(context2);
        LinearLayout linearLayout = new LinearLayout(context2);
        linearLayout.setOrientation(1);
        ((ScrollView) this.fragmentView).addView(linearLayout, LayoutHelper.createScroll(-1, -2, 51));
        linearLayout.setOnTouchListener(C1463-$$Lambda$ContactAddActivity$A7kSn3Cfc-ajr4rigI3HkJXjVCE.INSTANCE);
        FrameLayout frameLayout = new FrameLayout(context2);
        linearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, -2, 24.0f, 24.0f, 24.0f, 0.0f));
        this.avatarImage = new BackupImageView(context2);
        this.avatarImage.setRoundRadius(AndroidUtilities.m26dp(30.0f));
        int i = 3;
        frameLayout.addView(this.avatarImage, LayoutHelper.createFrame(60, 60, (LocaleController.isRTL ? 5 : 3) | 48));
        this.nameTextView = new TextView(context2);
        TextView textView = this.nameTextView;
        String str = Theme.key_windowBackgroundWhiteBlackText;
        textView.setTextColor(Theme.getColor(str));
        this.nameTextView.setTextSize(1, 20.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TruncateAt.END);
        this.nameTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView(this.nameTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 80.0f, 3.0f, LocaleController.isRTL ? 80.0f : 0.0f, 0.0f));
        this.onlineTextView = new TextView(context2);
        this.onlineTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.onlineTextView.setTextSize(1, 14.0f);
        this.onlineTextView.setLines(1);
        this.onlineTextView.setMaxLines(1);
        this.onlineTextView.setSingleLine(true);
        this.onlineTextView.setEllipsize(TruncateAt.END);
        this.onlineTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        frameLayout.addView(this.onlineTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 80.0f, 32.0f, LocaleController.isRTL ? 80.0f : 0.0f, 0.0f));
        this.firstNameField = new EditTextBoldCursor(context2);
        this.firstNameField.setTextSize(1, 18.0f);
        EditTextBoldCursor editTextBoldCursor = this.firstNameField;
        String str2 = Theme.key_windowBackgroundWhiteHintText;
        editTextBoldCursor.setHintTextColor(Theme.getColor(str2));
        this.firstNameField.setTextColor(Theme.getColor(str));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setSingleLine(true);
        this.firstNameField.setGravity(LocaleController.isRTL ? 5 : 3);
        this.firstNameField.setInputType(49152);
        this.firstNameField.setImeOptions(5);
        this.firstNameField.setHint(LocaleController.getString("FirstName", C1067R.string.FirstName));
        this.firstNameField.setCursorColor(Theme.getColor(str));
        this.firstNameField.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        linearLayout.addView(this.firstNameField, LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        this.firstNameField.setOnEditorActionListener(new C1465-$$Lambda$ContactAddActivity$xQQmG-ikgUejGCdP82NOkH8zIao(this));
        this.lastNameField = new EditTextBoldCursor(context2);
        this.lastNameField.setTextSize(1, 18.0f);
        this.lastNameField.setHintTextColor(Theme.getColor(str2));
        this.lastNameField.setTextColor(Theme.getColor(str));
        this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context2, false));
        this.lastNameField.setMaxLines(1);
        this.lastNameField.setLines(1);
        this.lastNameField.setSingleLine(true);
        EditTextBoldCursor editTextBoldCursor2 = this.lastNameField;
        if (LocaleController.isRTL) {
            i = 5;
        }
        editTextBoldCursor2.setGravity(i);
        this.lastNameField.setInputType(49152);
        this.lastNameField.setImeOptions(6);
        this.lastNameField.setHint(LocaleController.getString("LastName", C1067R.string.LastName));
        this.lastNameField.setCursorColor(Theme.getColor(str));
        this.lastNameField.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.lastNameField.setCursorWidth(1.5f);
        linearLayout.addView(this.lastNameField, LayoutHelper.createLinear(-1, 36, 24.0f, 16.0f, 24.0f, 0.0f));
        this.lastNameField.setOnEditorActionListener(new C1464-$$Lambda$ContactAddActivity$LmoZEE36adLyzqPJDOoRrL7aQWs(this));
        User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
        if (user != null) {
            if (user.phone == null) {
                String str3 = this.phone;
                if (str3 != null) {
                    user.phone = C0278PhoneFormat.stripExceptNumbers(str3);
                }
            }
            this.firstNameField.setText(user.first_name);
            EditTextBoldCursor editTextBoldCursor3 = this.firstNameField;
            editTextBoldCursor3.setSelection(editTextBoldCursor3.length());
            this.lastNameField.setText(user.last_name);
        }
        return this.fragmentView;
    }

    public /* synthetic */ boolean lambda$createView$1$ContactAddActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        this.lastNameField.requestFocus();
        EditTextBoldCursor editTextBoldCursor = this.lastNameField;
        editTextBoldCursor.setSelection(editTextBoldCursor.length());
        return true;
    }

    public /* synthetic */ boolean lambda$createView$2$ContactAddActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        this.doneButton.performClick();
        return true;
    }

    private void updateAvatarLayout() {
        if (this.nameTextView != null) {
            Object user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
            if (user != null) {
                TextView textView = this.nameTextView;
                C0278PhoneFormat instance = C0278PhoneFormat.getInstance();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("+");
                stringBuilder.append(user.phone);
                textView.setText(instance.format(stringBuilder.toString()));
                this.onlineTextView.setText(LocaleController.formatUserStatus(this.currentAccount, user));
                BackupImageView backupImageView = this.avatarImage;
                ImageLocation forUser = ImageLocation.getForUser(user, false);
                Drawable avatarDrawable = new AvatarDrawable((User) user);
                this.avatarDrawable = avatarDrawable;
                backupImageView.setImage(forUser, "50_50", avatarDrawable, user);
            }
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.updateInterfaces) {
            i = ((Integer) objArr[0]).intValue();
            if ((i & 2) != 0 || (i & 4) != 0) {
                updateAvatarLayout();
            }
        }
    }

    public void onResume() {
        super.onResume();
        updateAvatarLayout();
        if (!MessagesController.getGlobalMainSettings().getBoolean("view_animations", true)) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (z) {
            this.firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(this.firstNameField);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        r10 = new ThemeDescription[23];
        C3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08 c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08 = new C3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08(this);
        r10[15] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_text);
        r10[16] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundRed);
        r10[17] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundOrange);
        r10[18] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundViolet);
        r10[19] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundGreen);
        r10[20] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundCyan);
        r10[21] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundBlue);
        r10[22] = new ThemeDescription(null, 0, null, null, null, c3671-$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, Theme.key_avatar_backgroundPink);
        return r10;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$3$ContactAddActivity() {
        if (this.avatarImage != null) {
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.user_id));
            if (user != null) {
                this.avatarDrawable.setInfo(user);
                this.avatarImage.invalidate();
            }
        }
    }
}
