// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView$OnEditorActionListener;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChangeNameActivity extends BaseFragment
{
    private static final int done_button = 1;
    private View doneButton;
    private EditTextBoldCursor firstNameField;
    private View headerLabelView;
    private EditTextBoldCursor lastNameField;
    
    private void saveName() {
        final TLRPC.User currentUser = UserConfig.getInstance(super.currentAccount).getCurrentUser();
        if (currentUser != null && this.lastNameField.getText() != null) {
            if (this.firstNameField.getText() != null) {
                final String string = this.firstNameField.getText().toString();
                final String string2 = this.lastNameField.getText().toString();
                final String first_name = currentUser.first_name;
                if (first_name != null && first_name.equals(string)) {
                    final String last_name = currentUser.last_name;
                    if (last_name != null && last_name.equals(string2)) {
                        return;
                    }
                }
                final TLRPC.TL_account_updateProfile tl_account_updateProfile = new TLRPC.TL_account_updateProfile();
                tl_account_updateProfile.flags = 3;
                tl_account_updateProfile.first_name = string;
                currentUser.first_name = string;
                tl_account_updateProfile.last_name = string2;
                currentUser.last_name = string2;
                final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
                if (user != null) {
                    user.first_name = tl_account_updateProfile.first_name;
                    user.last_name = tl_account_updateProfile.last_name;
                }
                UserConfig.getInstance(super.currentAccount).saveConfig(true);
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_updateProfile, new RequestDelegate() {
                    @Override
                    public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                    }
                });
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("EditName", 2131559326));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChangeNameActivity.this.finishFragment();
                }
                else if (n == 1 && ChangeNameActivity.this.firstNameField.getText().length() != 0) {
                    ChangeNameActivity.this.saveName();
                    ChangeNameActivity.this.finishFragment();
                }
            }
        });
        this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        TLRPC.User user;
        if ((user = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId())) == null) {
            user = UserConfig.getInstance(super.currentAccount).getCurrentUser();
        }
        final LinearLayout fragmentView = new LinearLayout(context);
        (super.fragmentView = (View)fragmentView).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        ((LinearLayout)super.fragmentView).setOrientation(1);
        super.fragmentView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return true;
            }
        });
        (this.firstNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setSingleLine(true);
        final EditTextBoldCursor firstNameField = this.firstNameField;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 3;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        firstNameField.setGravity(gravity);
        this.firstNameField.setInputType(49152);
        this.firstNameField.setImeOptions(5);
        this.firstNameField.setHint((CharSequence)LocaleController.getString("FirstName", 2131559494));
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        fragmentView.addView((View)this.firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 24.0f, 24.0f, 24.0f, 0.0f));
        this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                if (n == 5) {
                    ChangeNameActivity.this.lastNameField.requestFocus();
                    ChangeNameActivity.this.lastNameField.setSelection(ChangeNameActivity.this.lastNameField.length());
                    return true;
                }
                return false;
            }
        });
        (this.lastNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.lastNameField.setMaxLines(1);
        this.lastNameField.setLines(1);
        this.lastNameField.setSingleLine(true);
        final EditTextBoldCursor lastNameField = this.lastNameField;
        int gravity2 = n;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        lastNameField.setGravity(gravity2);
        this.lastNameField.setInputType(49152);
        this.lastNameField.setImeOptions(6);
        this.lastNameField.setHint((CharSequence)LocaleController.getString("LastName", 2131559728));
        this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.lastNameField.setCursorWidth(1.5f);
        fragmentView.addView((View)this.lastNameField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 24.0f, 16.0f, 24.0f, 0.0f));
        this.lastNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                if (n == 6) {
                    ChangeNameActivity.this.doneButton.performClick();
                    return true;
                }
                return false;
            }
        });
        if (user != null) {
            this.firstNameField.setText((CharSequence)user.first_name);
            final EditTextBoldCursor firstNameField2 = this.firstNameField;
            firstNameField2.setSelection(firstNameField2.length());
            this.lastNameField.setText((CharSequence)user.last_name);
        }
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated") };
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
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (ChangeNameActivity.this.firstNameField != null) {
                        ChangeNameActivity.this.firstNameField.requestFocus();
                        AndroidUtilities.showKeyboard((View)ChangeNameActivity.this.firstNameField);
                    }
                }
            }, 100L);
        }
    }
}
