// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.widget.AdapterView;
import android.view.KeyEvent;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.ApplicationLoader;
import android.telephony.TelephonyManager;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.view.View$OnKeyListener;
import org.telegram.PhoneFormat.PhoneFormat;
import android.view.View$OnClickListener;
import android.text.TextUtils$TruncateAt;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.view.View$OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import android.os.Vibrator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.AlertsCreator;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ContactsController;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.MotionEvent;
import org.telegram.ui.Components.HintEditText;
import android.view.View;
import org.telegram.ui.Components.ContextProgressView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.AdapterView$OnItemSelectedListener;
import org.telegram.ui.ActionBar.BaseFragment;

public class NewContactActivity extends BaseFragment implements AdapterView$OnItemSelectedListener
{
    private static final int done_button = 1;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private EditTextBoldCursor codeField;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private TextView countryButton;
    private int countryState;
    private boolean donePressed;
    private ActionBarMenuItem editDoneItem;
    private AnimatorSet editDoneItemAnimation;
    private ContextProgressView editDoneItemProgress;
    private EditTextBoldCursor firstNameField;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private boolean ignoreSelection;
    private EditTextBoldCursor lastNameField;
    private View lineView;
    private HintEditText phoneField;
    private HashMap<String, String> phoneFormatMap;
    private TextView textView;
    
    public NewContactActivity() {
        this.countriesArray = new ArrayList<String>();
        this.countriesMap = new HashMap<String, String>();
        this.codesMap = new HashMap<String, String>();
        this.phoneFormatMap = new HashMap<String, String>();
    }
    
    private void showEditDoneProgress(final boolean b, final boolean b2) {
        final AnimatorSet editDoneItemAnimation = this.editDoneItemAnimation;
        if (editDoneItemAnimation != null) {
            editDoneItemAnimation.cancel();
        }
        if (!b2) {
            if (b) {
                this.editDoneItem.getImageView().setScaleX(0.1f);
                this.editDoneItem.getImageView().setScaleY(0.1f);
                this.editDoneItem.getImageView().setAlpha(0.0f);
                this.editDoneItemProgress.setScaleX(1.0f);
                this.editDoneItemProgress.setScaleY(1.0f);
                this.editDoneItemProgress.setAlpha(1.0f);
                this.editDoneItem.getImageView().setVisibility(4);
                this.editDoneItemProgress.setVisibility(0);
                this.editDoneItem.setEnabled(false);
            }
            else {
                this.editDoneItemProgress.setScaleX(0.1f);
                this.editDoneItemProgress.setScaleY(0.1f);
                this.editDoneItemProgress.setAlpha(0.0f);
                this.editDoneItem.getImageView().setScaleX(1.0f);
                this.editDoneItem.getImageView().setScaleY(1.0f);
                this.editDoneItem.getImageView().setAlpha(1.0f);
                this.editDoneItem.getImageView().setVisibility(0);
                this.editDoneItemProgress.setVisibility(4);
                this.editDoneItem.setEnabled(true);
            }
        }
        else {
            this.editDoneItemAnimation = new AnimatorSet();
            if (b) {
                this.editDoneItemProgress.setVisibility(0);
                this.editDoneItem.setEnabled(false);
                this.editDoneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItem.getImageView(), "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItem.getImageView(), "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItem.getImageView(), "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItemProgress, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItemProgress, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItemProgress, "alpha", new float[] { 1.0f }) });
            }
            else {
                this.editDoneItem.getImageView().setVisibility(0);
                this.editDoneItem.setEnabled(true);
                this.editDoneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItemProgress, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItemProgress, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItemProgress, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItem.getImageView(), "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItem.getImageView(), "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.editDoneItem.getImageView(), "alpha", new float[] { 1.0f }) });
            }
            this.editDoneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator obj) {
                    if (NewContactActivity.this.editDoneItemAnimation != null && NewContactActivity.this.editDoneItemAnimation.equals(obj)) {
                        NewContactActivity.this.editDoneItemAnimation = null;
                    }
                }
                
                public void onAnimationEnd(final Animator obj) {
                    if (NewContactActivity.this.editDoneItemAnimation != null && NewContactActivity.this.editDoneItemAnimation.equals(obj)) {
                        if (!b) {
                            NewContactActivity.this.editDoneItemProgress.setVisibility(4);
                        }
                        else {
                            NewContactActivity.this.editDoneItem.getImageView().setVisibility(4);
                        }
                    }
                }
            });
            this.editDoneItemAnimation.setDuration(150L);
            this.editDoneItemAnimation.start();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("AddContactTitle", 2131558569));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int sendRequest) {
                if (sendRequest == -1) {
                    NewContactActivity.this.finishFragment();
                }
                else if (sendRequest == 1) {
                    if (NewContactActivity.this.donePressed) {
                        return;
                    }
                    if (NewContactActivity.this.firstNameField.length() == 0) {
                        final Vibrator vibrator = (Vibrator)NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                        if (vibrator != null) {
                            vibrator.vibrate(200L);
                        }
                        AndroidUtilities.shakeView((View)NewContactActivity.this.firstNameField, 2.0f, 0);
                        return;
                    }
                    if (NewContactActivity.this.codeField.length() == 0) {
                        final Vibrator vibrator2 = (Vibrator)NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                        if (vibrator2 != null) {
                            vibrator2.vibrate(200L);
                        }
                        AndroidUtilities.shakeView((View)NewContactActivity.this.codeField, 2.0f, 0);
                        return;
                    }
                    if (NewContactActivity.this.phoneField.length() == 0) {
                        final Vibrator vibrator3 = (Vibrator)NewContactActivity.this.getParentActivity().getSystemService("vibrator");
                        if (vibrator3 != null) {
                            vibrator3.vibrate(200L);
                        }
                        AndroidUtilities.shakeView((View)NewContactActivity.this.phoneField, 2.0f, 0);
                        return;
                    }
                    NewContactActivity.this.donePressed = true;
                    NewContactActivity.this.showEditDoneProgress(true, true);
                    final TLRPC.TL_contacts_importContacts tl_contacts_importContacts = new TLRPC.TL_contacts_importContacts();
                    final TLRPC.TL_inputPhoneContact e = new TLRPC.TL_inputPhoneContact();
                    e.first_name = NewContactActivity.this.firstNameField.getText().toString();
                    e.last_name = NewContactActivity.this.lastNameField.getText().toString();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(NewContactActivity.this.codeField.getText().toString());
                    sb.append(NewContactActivity.this.phoneField.getText().toString());
                    e.phone = sb.toString();
                    tl_contacts_importContacts.contacts.add(e);
                    sendRequest = ConnectionsManager.getInstance(NewContactActivity.this.currentAccount).sendRequest(tl_contacts_importContacts, new _$$Lambda$NewContactActivity$1$WRq0Ss_PBCngsAibqDEMoSm52R4(this, e, tl_contacts_importContacts), 2);
                    ConnectionsManager.getInstance(NewContactActivity.this.currentAccount).bindRequestToGuid(sendRequest, NewContactActivity.this.classGuid);
                }
            }
        });
        (this.avatarDrawable = new AvatarDrawable()).setInfo(5, "", "", false);
        (this.editDoneItem = super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f))).setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        this.editDoneItemProgress = new ContextProgressView(context, 1);
        this.editDoneItem.addView((View)this.editDoneItemProgress, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.editDoneItemProgress.setVisibility(4);
        super.fragmentView = (View)new ScrollView(context);
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
        linearLayout.setOrientation(1);
        ((ScrollView)super.fragmentView).addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        linearLayout.setOnTouchListener((View$OnTouchListener)_$$Lambda$NewContactActivity$dyt1ArQHbLSL06GO_wtQQZkRhQE.INSTANCE);
        final FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 24.0f, 0.0f, 0.0f));
        (this.avatarImage = new BackupImageView(context)).setImageDrawable(this.avatarDrawable);
        frameLayout.addView((View)this.avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(60, 60.0f, 51, 0.0f, 9.0f, 0.0f, 0.0f));
        (this.firstNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.firstNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setMaxLines(1);
        this.firstNameField.setLines(1);
        this.firstNameField.setSingleLine(true);
        this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.firstNameField.setGravity(3);
        this.firstNameField.setInputType(49152);
        this.firstNameField.setImeOptions(5);
        this.firstNameField.setHint((CharSequence)LocaleController.getString("FirstName", 2131559494));
        this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.firstNameField.setCursorWidth(1.5f);
        frameLayout.addView((View)this.firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 34.0f, 51, 84.0f, 0.0f, 0.0f, 0.0f));
        this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$NewContactActivity$OEffd5rsJU1asHgIO5gqt5wMyr4(this));
        this.firstNameField.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
                NewContactActivity.this.avatarImage.invalidate();
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        (this.lastNameField = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.lastNameField.setMaxLines(1);
        this.lastNameField.setLines(1);
        this.lastNameField.setSingleLine(true);
        this.lastNameField.setGravity(3);
        this.lastNameField.setInputType(49152);
        this.lastNameField.setImeOptions(5);
        this.lastNameField.setHint((CharSequence)LocaleController.getString("LastName", 2131559728));
        this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.lastNameField.setCursorWidth(1.5f);
        frameLayout.addView((View)this.lastNameField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 34.0f, 51, 84.0f, 44.0f, 0.0f, 0.0f));
        this.lastNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$NewContactActivity$caCQM7G1cFeQM5WDamEHC5G1jDk(this));
        this.lastNameField.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                NewContactActivity.this.avatarDrawable.setInfo(5, NewContactActivity.this.firstNameField.getText().toString(), NewContactActivity.this.lastNameField.getText().toString(), false);
                NewContactActivity.this.avatarImage.invalidate();
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        (this.countryButton = new TextView(context)).setTextSize(1, 18.0f);
        this.countryButton.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(6.0f), 0);
        this.countryButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.countryButton.setMaxLines(1);
        this.countryButton.setSingleLine(true);
        this.countryButton.setEllipsize(TextUtils$TruncateAt.END);
        this.countryButton.setGravity(3);
        this.countryButton.setBackgroundResource(2131165857);
        linearLayout.addView((View)this.countryButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 0.0f, 24.0f, 0.0f, 14.0f));
        this.countryButton.setOnClickListener((View$OnClickListener)new _$$Lambda$NewContactActivity$PQwWlWssBZjUKNZQJ0dIzeJV9OI(this));
        (this.lineView = new View(context)).setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
        this.lineView.setBackgroundColor(Theme.getColor("windowBackgroundWhiteGrayLine"));
        linearLayout.addView(this.lineView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 1, 0.0f, -17.5f, 0.0f, 0.0f));
        final LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        linearLayout.addView((View)linearLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 20.0f, 0.0f, 0.0f));
        (this.textView = new TextView(context)).setText((CharSequence)"+");
        this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 18.0f);
        this.textView.setImportantForAccessibility(2);
        linearLayout2.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
        (this.codeField = new EditTextBoldCursor(context)).setInputType(3);
        this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.codeField.setCursorWidth(1.5f);
        this.codeField.setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
        this.codeField.setTextSize(1, 18.0f);
        this.codeField.setMaxLines(1);
        this.codeField.setGravity(19);
        this.codeField.setImeOptions(268435461);
        linearLayout2.addView((View)this.codeField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 16.0f, 0.0f));
        this.codeField.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (NewContactActivity.this.ignoreOnTextChange) {
                    return;
                }
                NewContactActivity.this.ignoreOnTextChange = true;
                String s = PhoneFormat.stripExceptNumbers(NewContactActivity.this.codeField.getText().toString());
                NewContactActivity.this.codeField.setText((CharSequence)s);
                final int length = s.length();
                final String s2 = null;
                if (length == 0) {
                    NewContactActivity.this.countryButton.setText((CharSequence)LocaleController.getString("ChooseCountry", 2131559086));
                    NewContactActivity.this.phoneField.setHintText(null);
                    NewContactActivity.this.countryState = 1;
                }
                else {
                    final int length2 = s.length();
                    int i = 4;
                    String text = null;
                    boolean b = false;
                    Label_0329: {
                        if (length2 > 4) {
                            NewContactActivity.this.ignoreOnTextChange = true;
                            while (true) {
                                while (i >= 1) {
                                    final String substring = s.substring(0, i);
                                    if (NewContactActivity.this.codesMap.get(substring) != null) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append(s.substring(i, s.length()));
                                        sb.append(NewContactActivity.this.phoneField.getText().toString());
                                        text = sb.toString();
                                        NewContactActivity.this.codeField.setText((CharSequence)substring);
                                        final int n = 1;
                                        s = substring;
                                        b = (n != 0);
                                        if (n == 0) {
                                            NewContactActivity.this.ignoreOnTextChange = true;
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append(substring.substring(1, substring.length()));
                                            sb2.append(NewContactActivity.this.phoneField.getText().toString());
                                            text = sb2.toString();
                                            final EditTextBoldCursor access$200 = NewContactActivity.this.codeField;
                                            s = substring.substring(0, 1);
                                            access$200.setText((CharSequence)s);
                                            b = (n != 0);
                                        }
                                        break Label_0329;
                                    }
                                    else {
                                        --i;
                                    }
                                }
                                final String substring = s;
                                text = null;
                                final int n = false ? 1 : 0;
                                continue;
                            }
                        }
                        text = null;
                        b = false;
                    }
                    final String o = NewContactActivity.this.codesMap.get(s);
                    if (o != null) {
                        final int index = NewContactActivity.this.countriesArray.indexOf(o);
                        if (index != -1) {
                            NewContactActivity.this.ignoreSelection = true;
                            NewContactActivity.this.countryButton.setText((CharSequence)NewContactActivity.this.countriesArray.get(index));
                            final String s3 = NewContactActivity.this.phoneFormatMap.get(s);
                            final HintEditText access$201 = NewContactActivity.this.phoneField;
                            String replace = s2;
                            if (s3 != null) {
                                replace = s3.replace('X', '\u2013');
                            }
                            access$201.setHintText(replace);
                            NewContactActivity.this.countryState = 0;
                        }
                        else {
                            NewContactActivity.this.countryButton.setText((CharSequence)LocaleController.getString("WrongCountry", 2131561125));
                            NewContactActivity.this.phoneField.setHintText(null);
                            NewContactActivity.this.countryState = 2;
                        }
                    }
                    else {
                        NewContactActivity.this.countryButton.setText((CharSequence)LocaleController.getString("WrongCountry", 2131561125));
                        NewContactActivity.this.phoneField.setHintText(null);
                        NewContactActivity.this.countryState = 2;
                    }
                    if (!b) {
                        NewContactActivity.this.codeField.setSelection(NewContactActivity.this.codeField.getText().length());
                    }
                    if (text != null) {
                        NewContactActivity.this.phoneField.requestFocus();
                        NewContactActivity.this.phoneField.setText((CharSequence)text);
                        NewContactActivity.this.phoneField.setSelection(NewContactActivity.this.phoneField.length());
                    }
                }
                NewContactActivity.this.ignoreOnTextChange = false;
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.codeField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$NewContactActivity$sAQJMXy_aH9t_IBkPAm16jh4ito(this));
        (this.phoneField = new HintEditText(context)).setInputType(3);
        this.phoneField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.phoneField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.phoneField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
        this.phoneField.setPadding(0, 0, 0, 0);
        this.phoneField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.phoneField.setCursorSize(AndroidUtilities.dp(20.0f));
        this.phoneField.setCursorWidth(1.5f);
        this.phoneField.setTextSize(1, 18.0f);
        this.phoneField.setMaxLines(1);
        this.phoneField.setGravity(19);
        this.phoneField.setImeOptions(268435462);
        linearLayout2.addView((View)this.phoneField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f));
        this.phoneField.addTextChangedListener((TextWatcher)new TextWatcher() {
            private int actionPosition;
            private int characterAction = -1;
            
            public void afterTextChanged(final Editable editable) {
                if (NewContactActivity.this.ignoreOnPhoneChange) {
                    return;
                }
                final int selectionStart = NewContactActivity.this.phoneField.getSelectionStart();
                String s2;
                final String s = s2 = NewContactActivity.this.phoneField.getText().toString();
                int n = selectionStart;
                if (this.characterAction == 3) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s.substring(0, this.actionPosition));
                    sb.append(s.substring(this.actionPosition + 1, s.length()));
                    s2 = sb.toString();
                    n = selectionStart - 1;
                }
                final StringBuilder text = new StringBuilder(s2.length());
                int endIndex;
                for (int i = 0; i < s2.length(); i = endIndex) {
                    endIndex = i + 1;
                    final String substring = s2.substring(i, endIndex);
                    if ("0123456789".contains(substring)) {
                        text.append(substring);
                    }
                }
                NewContactActivity.this.ignoreOnPhoneChange = true;
                final String hintText = NewContactActivity.this.phoneField.getHintText();
                int length = n;
                Label_0340: {
                    if (hintText != null) {
                        int j = 0;
                        while (j < text.length()) {
                            if (j < hintText.length()) {
                                int n2 = n;
                                int n3 = j;
                                if (hintText.charAt(j) == ' ') {
                                    text.insert(j, ' ');
                                    ++j;
                                    if ((n2 = n) == (n3 = j)) {
                                        final int characterAction = this.characterAction;
                                        n2 = n;
                                        n3 = j;
                                        if (characterAction != 2) {
                                            n2 = n;
                                            n3 = j;
                                            if (characterAction != 3) {
                                                n2 = n + 1;
                                                n3 = j;
                                            }
                                        }
                                    }
                                }
                                j = n3 + 1;
                                n = n2;
                            }
                            else {
                                text.insert(j, ' ');
                                if (n != j + 1) {
                                    break;
                                }
                                final int characterAction2 = this.characterAction;
                                if (characterAction2 != 2 && characterAction2 != 3) {
                                    length = n + 1;
                                    break Label_0340;
                                }
                                break;
                            }
                        }
                        length = n;
                    }
                }
                NewContactActivity.this.phoneField.setText((CharSequence)text);
                if (length >= 0) {
                    final HintEditText access$300 = NewContactActivity.this.phoneField;
                    if (length > NewContactActivity.this.phoneField.length()) {
                        length = NewContactActivity.this.phoneField.length();
                    }
                    access$300.setSelection(length);
                }
                NewContactActivity.this.phoneField.onTextChange();
                NewContactActivity.this.ignoreOnPhoneChange = false;
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                if (n2 == 0 && n3 == 1) {
                    this.characterAction = 1;
                }
                else if (n2 == 1 && n3 == 0) {
                    if (charSequence.charAt(n) == ' ' && n > 0) {
                        this.characterAction = 3;
                        this.actionPosition = n - 1;
                    }
                    else {
                        this.characterAction = 2;
                    }
                }
                else {
                    this.characterAction = -1;
                }
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.phoneField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$NewContactActivity$6q60KF1tjXtvySPg1IX8F4PNNEY(this));
        this.phoneField.setOnKeyListener((View$OnKeyListener)new _$$Lambda$NewContactActivity$5Ca3pvZCNy2Se_fsqStW34es8nQ(this));
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                final String[] split = line.split(";");
                this.countriesArray.add(0, split[2]);
                this.countriesMap.put(split[2], split[0]);
                this.codesMap.put(split[0], split[2]);
                if (split.length > 3) {
                    this.phoneFormatMap.put(split[0], split[3]);
                }
                hashMap.put(split[1], split[2]);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        Collections.sort(this.countriesArray, (Comparator<? super String>)_$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
        final Object o = null;
        Object upperCase;
        try {
            final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
            upperCase = o;
            if (telephonyManager != null) {
                upperCase = telephonyManager.getSimCountryIso().toUpperCase();
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            upperCase = o;
        }
        if (upperCase != null) {
            final String s = hashMap.get(upperCase);
            if (s != null && this.countriesArray.indexOf(s) != -1) {
                this.codeField.setText((CharSequence)this.countriesMap.get(s));
                this.countryState = 0;
            }
        }
        if (this.codeField.length() == 0) {
            this.countryButton.setText((CharSequence)LocaleController.getString("ChooseCountry", 2131559086));
            this.phoneField.setHintText(null);
            this.countryState = 1;
        }
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk $$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk = new _$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.phoneField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.lineView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhiteGrayLine"), new ThemeDescription((View)this.countryButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.editDoneItemProgress, 0, null, null, null, null, "contextProgressInner2"), new ThemeDescription(this.editDoneItemProgress, 0, null, null, null, null, "contextProgressOuter2"), new ThemeDescription(null, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NewContactActivity$_TdFXabn_cMkV_y0uLVuNwOAoYk, "avatar_backgroundPink") };
    }
    
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int index, final long n) {
        if (this.ignoreSelection) {
            this.ignoreSelection = false;
            return;
        }
        this.ignoreOnTextChange = true;
        this.codeField.setText((CharSequence)this.countriesMap.get(this.countriesArray.get(index)));
        this.ignoreOnTextChange = false;
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
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
    
    public void selectCountry(String replace) {
        if (this.countriesArray.indexOf(replace) != -1) {
            this.ignoreOnTextChange = true;
            final String s = this.countriesMap.get(replace);
            this.codeField.setText((CharSequence)s);
            this.countryButton.setText((CharSequence)replace);
            replace = this.phoneFormatMap.get(s);
            final HintEditText phoneField = this.phoneField;
            if (replace != null) {
                replace = replace.replace('X', '\u2013');
            }
            else {
                replace = null;
            }
            phoneField.setHintText(replace);
            this.countryState = 0;
            this.ignoreOnTextChange = false;
        }
    }
}
