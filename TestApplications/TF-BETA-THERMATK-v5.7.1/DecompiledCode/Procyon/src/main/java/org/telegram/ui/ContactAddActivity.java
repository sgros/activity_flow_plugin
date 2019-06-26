// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.KeyEvent;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.widget.TextView$OnEditorActionListener;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import android.view.View$OnTouchListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import org.telegram.messenger.AndroidUtilities;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.MessagesController;
import android.view.MotionEvent;
import android.os.Bundle;
import android.widget.TextView;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.view.View;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ContactAddActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int done_button = 1;
    private boolean addContact;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private EditTextBoldCursor lastNameField;
    private TextView nameTextView;
    private TextView onlineTextView;
    private String phone;
    private int user_id;
    
    public ContactAddActivity(final Bundle bundle) {
        super(bundle);
        this.phone = null;
    }
    
    private void updateAvatarLayout() {
        if (this.nameTextView == null) {
            return;
        }
        final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
        if (user == null) {
            return;
        }
        final TextView nameTextView = this.nameTextView;
        final PhoneFormat instance = PhoneFormat.getInstance();
        final StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(user.phone);
        nameTextView.setText((CharSequence)instance.format(sb.toString()));
        this.onlineTextView.setText((CharSequence)LocaleController.formatUserStatus(super.currentAccount, user));
        this.avatarImage.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable = new AvatarDrawable(user), user);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (this.addContact) {
            super.actionBar.setTitle(LocaleController.getString("AddContactTitle", 2131558569));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("EditName", 2131559326));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ContactAddActivity.this.finishFragment();
                }
                else if (n == 1 && ContactAddActivity.this.firstNameField.getText().length() != 0) {
                    final TLRPC.User user = MessagesController.getInstance(ContactAddActivity.this.currentAccount).getUser(ContactAddActivity.this.user_id);
                    user.first_name = ContactAddActivity.this.firstNameField.getText().toString();
                    user.last_name = ContactAddActivity.this.lastNameField.getText().toString();
                    ContactsController.getInstance(ContactAddActivity.this.currentAccount).addContact(user);
                    ContactAddActivity.this.finishFragment();
                    final SharedPreferences$Editor edit = MessagesController.getNotificationsSettings(ContactAddActivity.this.currentAccount).edit();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("spam3_");
                    sb.append(ContactAddActivity.this.user_id);
                    edit.putInt(sb.toString(), 1).commit();
                    NotificationCenter.getInstance(ContactAddActivity.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
                    NotificationCenter.getInstance(ContactAddActivity.this.currentAccount).postNotificationName(NotificationCenter.peerSettingsDidLoad, ContactAddActivity.this.user_id);
                }
            }
        });
        this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        super.fragmentView = (View)new ScrollView(context);
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        ((ScrollView)super.fragmentView).addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        linearLayout.setOnTouchListener((View$OnTouchListener)_$$Lambda$ContactAddActivity$A7kSn3Cfc_ajr4rigI3HkJXjVCE.INSTANCE);
        final FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 24.0f, 24.0f, 24.0f, 0.0f));
        (this.avatarImage = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(30.0f));
        final BackupImageView avatarImage = this.avatarImage;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 3;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        frameLayout.addView((View)avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(60, 60, n2 | 0x30));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 20.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView nameTextView = this.nameTextView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        nameTextView.setGravity(gravity);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        final TextView nameTextView2 = this.nameTextView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 0.0f;
        }
        else {
            n4 = 80.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 80.0f;
        }
        else {
            n5 = 0.0f;
        }
        frameLayout.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n3 | 0x30, n4, 3.0f, n5, 0.0f));
        (this.onlineTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.onlineTextView.setTextSize(1, 14.0f);
        this.onlineTextView.setLines(1);
        this.onlineTextView.setMaxLines(1);
        this.onlineTextView.setSingleLine(true);
        this.onlineTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView onlineTextView = this.onlineTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        onlineTextView.setGravity(gravity2);
        final TextView onlineTextView2 = this.onlineTextView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 0.0f;
        }
        else {
            n7 = 80.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 80.0f;
        }
        else {
            n8 = 0.0f;
        }
        frameLayout.addView((View)onlineTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n6 | 0x30, n7, 32.0f, n8, 0.0f));
        (this.firstNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setSingleLine(true);
        final EditTextBoldCursor firstNameField = this.firstNameField;
        int gravity3;
        if (LocaleController.isRTL) {
            gravity3 = 5;
        }
        else {
            gravity3 = 3;
        }
        firstNameField.setGravity(gravity3);
        this.firstNameField.setInputType(49152);
        this.firstNameField.setImeOptions(5);
        this.firstNameField.setHint((CharSequence)LocaleController.getString("FirstName", 2131559494));
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        linearLayout.addView((View)this.firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ContactAddActivity$xQQmG_ikgUejGCdP82NOkH8zIao(this));
        (this.lastNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.lastNameField.setMaxLines(1);
        this.lastNameField.setLines(1);
        this.lastNameField.setSingleLine(true);
        final EditTextBoldCursor lastNameField = this.lastNameField;
        int gravity4 = n;
        if (LocaleController.isRTL) {
            gravity4 = 5;
        }
        lastNameField.setGravity(gravity4);
        this.lastNameField.setInputType(49152);
        this.lastNameField.setImeOptions(6);
        this.lastNameField.setHint((CharSequence)LocaleController.getString("LastName", 2131559728));
        this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.lastNameField.setCursorWidth(1.5f);
        linearLayout.addView((View)this.lastNameField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 24.0f, 16.0f, 24.0f, 0.0f));
        this.lastNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ContactAddActivity$LmoZEE36adLyzqPJDOoRrL7aQWs(this));
        final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
        if (user != null) {
            if (user.phone == null) {
                final String phone = this.phone;
                if (phone != null) {
                    user.phone = PhoneFormat.stripExceptNumbers(phone);
                }
            }
            this.firstNameField.setText((CharSequence)user.first_name);
            final EditTextBoldCursor firstNameField2 = this.firstNameField;
            firstNameField2.setSelection(firstNameField2.length());
            this.lastNameField.setText((CharSequence)user.last_name);
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int intValue, final int n, final Object... array) {
        if (intValue == NotificationCenter.updateInterfaces) {
            intValue = (int)array[0];
            if ((intValue & 0x2) != 0x0 || (intValue & 0x4) != 0x0) {
                this.updateAvatarLayout();
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08 $$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08 = new _$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.onlineTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription(null, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactAddActivity$SkTZ31Qqc_ITmfJvKZ4HlR_Vj08, "avatar_backgroundPink") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        final Bundle arguments = this.getArguments();
        final boolean b = false;
        this.user_id = arguments.getInt("user_id", 0);
        this.phone = this.getArguments().getString("phone");
        this.addContact = this.getArguments().getBoolean("addContact", false);
        boolean b2 = b;
        if (MessagesController.getInstance(super.currentAccount).getUser(this.user_id) != null) {
            b2 = b;
            if (super.onFragmentCreate()) {
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.updateAvatarLayout();
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
