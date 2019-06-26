// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.app.PendingIntent;
import org.telegram.messenger.SmsReceiver;
import android.telephony.SmsManager;
import android.telephony.PhoneNumberUtils;
import org.telegram.messenger.BuildVars;
import android.widget.AdapterView;
import android.telephony.TelephonyManager;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.widget.Toast;
import android.animation.StateListAnimator;
import android.view.MotionEvent;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.Components.HintEditText;
import java.util.HashMap;
import org.telegram.ui.Cells.CheckBoxCell;
import android.widget.AdapterView$OnItemSelectedListener;
import android.content.SharedPreferences;
import android.view.View$OnKeyListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import org.telegram.ui.Components.AlertsCreator;
import java.util.TimerTask;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import java.util.Timer;
import android.widget.LinearLayout;
import org.telegram.PhoneFormat.PhoneFormat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;
import android.util.Base64;
import org.telegram.messenger.ImageLocation;
import org.telegram.ui.Components.ImageUpdater$ImageUpdaterDelegate$_CC;
import org.telegram.messenger.MessageObject;
import android.text.SpannableStringBuilder;
import android.text.method.MovementMethod;
import android.widget.ImageView$ScaleType;
import android.graphics.Canvas;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import android.view.KeyEvent;
import android.os.Vibrator;
import android.view.View$OnClickListener;
import android.widget.TextView$OnEditorActionListener;
import android.graphics.Typeface;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.widget.TextView;
import android.util.Property;
import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build$VERSION;
import android.content.Intent;
import java.util.Locale;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.widget.FrameLayout;
import android.graphics.Rect;
import android.view.View$MeasureSpec;
import android.widget.ScrollView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.DialogInterface;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.NotificationCenter;
import java.util.Iterator;
import org.telegram.messenger.FileLog;
import java.util.Map;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.ApplicationLoader;
import android.os.Bundle;
import org.telegram.ui.Components.SlideView;
import java.util.ArrayList;
import android.app.Dialog;
import org.telegram.ui.Components.ContextProgressView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.tgnet.TLRPC;
import android.annotation.SuppressLint;
import org.telegram.ui.ActionBar.BaseFragment;

@SuppressLint({ "HardwareIds" })
public class LoginActivity extends BaseFragment
{
    private static final int done_button = 1;
    private boolean checkPermissions;
    private boolean checkShowPermissions;
    private TLRPC.TL_help_termsOfService currentTermsOfService;
    private int currentViewNum;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private ContextProgressView doneProgressView;
    private boolean newAccount;
    private Dialog permissionsDialog;
    private ArrayList<String> permissionsItems;
    private Dialog permissionsShowDialog;
    private ArrayList<String> permissionsShowItems;
    private int progressRequestId;
    private int scrollHeight;
    private boolean syncContacts;
    private SlideView[] views;
    
    public LoginActivity() {
        this.views = new SlideView[9];
        this.permissionsItems = new ArrayList<String>();
        this.permissionsShowItems = new ArrayList<String>();
        this.checkPermissions = true;
        this.checkShowPermissions = true;
        this.syncContacts = true;
    }
    
    public LoginActivity(final int currentAccount) {
        this.views = new SlideView[9];
        this.permissionsItems = new ArrayList<String>();
        this.permissionsShowItems = new ArrayList<String>();
        this.checkPermissions = true;
        this.checkShowPermissions = true;
        this.syncContacts = true;
        super.currentAccount = currentAccount;
        this.newAccount = true;
    }
    
    private void clearCurrentState() {
        final SharedPreferences$Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).edit();
        edit.clear();
        edit.commit();
    }
    
    private void fillNextCodeParams(final Bundle bundle, final TLRPC.TL_auth_sentCode tl_auth_sentCode) {
        final TLRPC.TL_help_termsOfService terms_of_service = tl_auth_sentCode.terms_of_service;
        if (terms_of_service != null) {
            this.currentTermsOfService = terms_of_service;
        }
        bundle.putString("phoneHash", tl_auth_sentCode.phone_code_hash);
        final TLRPC.auth_CodeType next_type = tl_auth_sentCode.next_type;
        if (next_type instanceof TLRPC.TL_auth_codeTypeCall) {
            bundle.putInt("nextType", 4);
        }
        else if (next_type instanceof TLRPC.TL_auth_codeTypeFlashCall) {
            bundle.putInt("nextType", 3);
        }
        else if (next_type instanceof TLRPC.TL_auth_codeTypeSms) {
            bundle.putInt("nextType", 2);
        }
        if (tl_auth_sentCode.type instanceof TLRPC.TL_auth_sentCodeTypeApp) {
            bundle.putInt("type", 1);
            bundle.putInt("length", tl_auth_sentCode.type.length);
            this.setPage(1, true, bundle, false);
        }
        else {
            if (tl_auth_sentCode.timeout == 0) {
                tl_auth_sentCode.timeout = 60;
            }
            bundle.putInt("timeout", tl_auth_sentCode.timeout * 1000);
            final TLRPC.auth_SentCodeType type = tl_auth_sentCode.type;
            if (type instanceof TLRPC.TL_auth_sentCodeTypeCall) {
                bundle.putInt("type", 4);
                bundle.putInt("length", tl_auth_sentCode.type.length);
                this.setPage(4, true, bundle, false);
            }
            else if (type instanceof TLRPC.TL_auth_sentCodeTypeFlashCall) {
                bundle.putInt("type", 3);
                bundle.putString("pattern", tl_auth_sentCode.type.pattern);
                this.setPage(3, true, bundle, false);
            }
            else if (type instanceof TLRPC.TL_auth_sentCodeTypeSms) {
                bundle.putInt("type", 2);
                bundle.putInt("length", tl_auth_sentCode.type.length);
                this.setPage(2, true, bundle, false);
            }
        }
    }
    
    private Bundle loadCurrentState() {
        if (this.newAccount) {
            return null;
        }
        try {
            final Bundle bundle = new Bundle();
            for (final Map.Entry<String, V> entry : ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).getAll().entrySet()) {
                final String s = entry.getKey();
                final V value = entry.getValue();
                final String[] split = s.split("_\\|_");
                if (split.length == 1) {
                    if (value instanceof String) {
                        bundle.putString(s, (String)value);
                    }
                    else {
                        if (!(value instanceof Integer)) {
                            continue;
                        }
                        bundle.putInt(s, (int)value);
                    }
                }
                else {
                    if (split.length != 2) {
                        continue;
                    }
                    Bundle bundle2;
                    if ((bundle2 = bundle.getBundle(split[0])) == null) {
                        bundle2 = new Bundle();
                        bundle.putBundle(split[0], bundle2);
                    }
                    if (value instanceof String) {
                        bundle2.putString(split[1], (String)value);
                    }
                    else {
                        if (!(value instanceof Integer)) {
                            continue;
                        }
                        bundle2.putInt(split[1], (int)value);
                    }
                }
            }
            return bundle;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    private void needFinishActivity() {
        this.clearCurrentState();
        if (this.getParentActivity() instanceof LaunchActivity) {
            if (this.newAccount) {
                this.newAccount = false;
                ((LaunchActivity)this.getParentActivity()).switchToAccount(super.currentAccount, false);
                this.finishFragment();
            }
            else {
                this.presentFragment(new DialogsActivity(null), true);
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
            }
        }
        else if (this.getParentActivity() instanceof ExternalActionActivity) {
            ((ExternalActionActivity)this.getParentActivity()).onFinishLogin();
        }
    }
    
    private void needShowAlert(final String title, final String message) {
        if (message != null) {
            if (this.getParentActivity() != null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                this.showDialog(builder.create());
            }
        }
    }
    
    private void needShowInvalidAlert(final String s, final boolean b) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        if (b) {
            builder.setMessage(LocaleController.getString("BannedPhoneNumber", 2131558831));
        }
        else {
            builder.setMessage(LocaleController.getString("InvalidPhoneNumber", 2131559674));
        }
        builder.setNeutralButton(LocaleController.getString("BotHelp", 2131558850), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$ue6fWEokYAlkmz3p94N9QOm1_o4(this, b, s));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        this.showDialog(builder.create());
    }
    
    private void needShowProgress(final int progressRequestId) {
        this.progressRequestId = progressRequestId;
        this.showEditDoneProgress(true);
    }
    
    private void onAuthSuccess(final TLRPC.TL_auth_authorization tl_auth_authorization) {
        ConnectionsManager.getInstance(super.currentAccount).setUserId(tl_auth_authorization.user.id);
        UserConfig.getInstance(super.currentAccount).clearConfig();
        MessagesController.getInstance(super.currentAccount).cleanup();
        UserConfig.getInstance(super.currentAccount).syncContacts = this.syncContacts;
        UserConfig.getInstance(super.currentAccount).setCurrentUser(tl_auth_authorization.user);
        UserConfig.getInstance(super.currentAccount).saveConfig(true);
        MessagesStorage.getInstance(super.currentAccount).cleanup(true);
        final ArrayList<TLRPC.User> list = new ArrayList<TLRPC.User>();
        list.add(tl_auth_authorization.user);
        MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(list, null, true, true);
        MessagesController.getInstance(super.currentAccount).putUser(tl_auth_authorization.user, false);
        ContactsController.getInstance(super.currentAccount).checkAppAccount();
        MessagesController.getInstance(super.currentAccount).getBlockedUsers(true);
        MessagesController.getInstance(super.currentAccount).checkProxyInfo(true);
        ConnectionsManager.getInstance(super.currentAccount).updateDcSettings();
        this.needFinishActivity();
    }
    
    private void putBundleToEditor(final Bundle bundle, final SharedPreferences$Editor sharedPreferences$Editor, final String s) {
        for (final String s2 : bundle.keySet()) {
            final Object value = bundle.get(s2);
            if (value instanceof String) {
                if (s != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append("_|_");
                    sb.append(s2);
                    sharedPreferences$Editor.putString(sb.toString(), (String)value);
                }
                else {
                    sharedPreferences$Editor.putString(s2, (String)value);
                }
            }
            else if (value instanceof Integer) {
                if (s != null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s);
                    sb2.append("_|_");
                    sb2.append(s2);
                    sharedPreferences$Editor.putInt(sb2.toString(), (int)value);
                }
                else {
                    sharedPreferences$Editor.putInt(s2, (int)value);
                }
            }
            else {
                if (!(value instanceof Bundle)) {
                    continue;
                }
                this.putBundleToEditor((Bundle)value, sharedPreferences$Editor, s2);
            }
        }
    }
    
    private void showEditDoneProgress(final boolean b) {
        final AnimatorSet doneItemAnimation = this.doneItemAnimation;
        if (doneItemAnimation != null) {
            doneItemAnimation.cancel();
        }
        this.doneItemAnimation = new AnimatorSet();
        if (b) {
            this.doneProgressView.setTag((Object)1);
            this.doneProgressView.setVisibility(0);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneProgressView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneProgressView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneProgressView, "alpha", new float[] { 1.0f }) });
        }
        else {
            this.doneProgressView.setTag((Object)null);
            this.doneItem.getImageView().setVisibility(0);
            this.doneItemAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.doneProgressView, "scaleX", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneProgressView, "scaleY", new float[] { 0.1f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneProgressView, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.doneItem.getImageView(), "alpha", new float[] { 1.0f }) });
        }
        this.doneItemAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator obj) {
                if (LoginActivity.this.doneItemAnimation != null && LoginActivity.this.doneItemAnimation.equals(obj)) {
                    LoginActivity.this.doneItemAnimation = null;
                }
            }
            
            public void onAnimationEnd(final Animator obj) {
                if (LoginActivity.this.doneItemAnimation != null && LoginActivity.this.doneItemAnimation.equals(obj)) {
                    if (!b) {
                        LoginActivity.this.doneProgressView.setVisibility(4);
                    }
                    else {
                        LoginActivity.this.doneItem.getImageView().setVisibility(4);
                    }
                }
            }
        });
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setTitle(LocaleController.getString("AppName", 2131558635));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == 1) {
                    if (LoginActivity.this.doneProgressView.getTag() != null) {
                        if (LoginActivity.this.getParentActivity() == null) {
                            return;
                        }
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setMessage(LocaleController.getString("StopLoading", 2131560827));
                        builder.setPositiveButton(LocaleController.getString("WaitMore", 2131561101), null);
                        builder.setNegativeButton(LocaleController.getString("Stop", 2131560820), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$1$lZ8la8kpyrJX7A2ctFfMuS75IVo(this));
                        LoginActivity.this.showDialog(builder.create());
                    }
                    else {
                        LoginActivity.this.views[LoginActivity.this.currentViewNum].onNextPressed();
                    }
                }
                else if (n == -1 && LoginActivity.this.onBackPressed()) {
                    LoginActivity.this.finishFragment();
                }
            }
        });
        final ActionBarMenu menu = super.actionBar.createMenu();
        super.actionBar.setAllowOverlayTitle(true);
        this.doneItem = menu.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        (this.doneProgressView = new ContextProgressView(context, 1)).setAlpha(0.0f);
        this.doneProgressView.setScaleX(0.1f);
        this.doneProgressView.setScaleY(0.1f);
        this.doneProgressView.setVisibility(4);
        this.doneItem.addView((View)this.doneProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.doneItem.setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        final ScrollView fragmentView = new ScrollView(context) {
            protected void onMeasure(final int n, final int n2) {
                LoginActivity.this.scrollHeight = View$MeasureSpec.getSize(n2) - AndroidUtilities.dp(30.0f);
                super.onMeasure(n, n2);
            }
            
            public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                if (LoginActivity.this.currentViewNum == 1 || LoginActivity.this.currentViewNum == 2 || LoginActivity.this.currentViewNum == 4) {
                    rect.bottom += AndroidUtilities.dp(40.0f);
                }
                return super.requestChildRectangleOnScreen(view, rect, b);
            }
        };
        fragmentView.setFillViewport(true);
        super.fragmentView = (View)fragmentView;
        final FrameLayout frameLayout = new FrameLayout(context);
        fragmentView.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 51));
        this.views[0] = new PhoneView(context);
        this.views[1] = new LoginActivitySmsView(context, 1);
        this.views[2] = new LoginActivitySmsView(context, 2);
        this.views[3] = new LoginActivitySmsView(context, 3);
        this.views[4] = new LoginActivitySmsView(context, 4);
        this.views[5] = new LoginActivityRegisterView(context);
        this.views[6] = new LoginActivityPasswordView(context);
        this.views[7] = new LoginActivityRecoverView(context);
        this.views[8] = new LoginActivityResetWaitView(context);
        int n = 0;
        while (true) {
            final SlideView[] views = this.views;
            if (n >= views.length) {
                break;
            }
            final SlideView slideView = views[n];
            int visibility;
            if (n == 0) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            slideView.setVisibility(visibility);
            final SlideView slideView2 = this.views[n];
            final boolean tablet = AndroidUtilities.isTablet();
            float n2 = 18.0f;
            float n3;
            if (tablet) {
                n3 = 26.0f;
            }
            else {
                n3 = 18.0f;
            }
            if (AndroidUtilities.isTablet()) {
                n2 = 26.0f;
            }
            frameLayout.addView((View)slideView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, n3, 30.0f, n2, 0.0f));
            ++n;
        }
        final Bundle loadCurrentState = this.loadCurrentState();
        Bundle bundle = null;
        Label_0623: {
            if ((bundle = loadCurrentState) != null) {
                this.currentViewNum = loadCurrentState.getInt("currentViewNum", 0);
                this.syncContacts = (loadCurrentState.getInt("syncContacts", 1) == 1);
                final int currentViewNum = this.currentViewNum;
                if (currentViewNum >= 1 && currentViewNum <= 4) {
                    final int int1 = loadCurrentState.getInt("open");
                    bundle = loadCurrentState;
                    if (int1 == 0) {
                        break Label_0623;
                    }
                    bundle = loadCurrentState;
                    if (Math.abs(System.currentTimeMillis() / 1000L - int1) < 86400L) {
                        break Label_0623;
                    }
                    this.currentViewNum = 0;
                    this.clearCurrentState();
                }
                else {
                    bundle = loadCurrentState;
                    if (this.currentViewNum != 6) {
                        break Label_0623;
                    }
                    final LoginActivityPasswordView loginActivityPasswordView = (LoginActivityPasswordView)this.views[6];
                    if (loginActivityPasswordView.passwordType != 0 && loginActivityPasswordView.current_salt1 != null) {
                        bundle = loadCurrentState;
                        if (loginActivityPasswordView.current_salt2 != null) {
                            break Label_0623;
                        }
                    }
                    this.currentViewNum = 0;
                    this.clearCurrentState();
                }
                bundle = null;
            }
        }
        int n4 = 0;
        SlideView[] views2;
        while (true) {
            views2 = this.views;
            if (n4 >= views2.length) {
                break;
            }
            if (bundle != null) {
                if (n4 >= 1 && n4 <= 4) {
                    if (n4 == this.currentViewNum) {
                        views2[n4].restoreStateParams(bundle);
                    }
                }
                else {
                    this.views[n4].restoreStateParams(bundle);
                }
            }
            if (this.currentViewNum == n4) {
                final ActionBar actionBar = super.actionBar;
                int backButtonImage;
                if (!this.views[n4].needBackButton() && !this.newAccount) {
                    backButtonImage = 0;
                }
                else {
                    backButtonImage = 2131165409;
                }
                actionBar.setBackButtonImage(backButtonImage);
                this.views[n4].setVisibility(0);
                this.views[n4].onShow();
                if (n4 == 3 || n4 == 8) {
                    this.doneItem.setVisibility(8);
                }
            }
            else {
                this.views[n4].setVisibility(8);
            }
            ++n4;
        }
        super.actionBar.setTitle(views2[this.currentViewNum].getHeaderName());
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        int n = 0;
        while (true) {
            final SlideView[] views = this.views;
            if (n >= views.length) {
                final PhoneView phoneView = (PhoneView)views[0];
                final LoginActivitySmsView loginActivitySmsView = (LoginActivitySmsView)views[1];
                final LoginActivitySmsView loginActivitySmsView2 = (LoginActivitySmsView)views[2];
                final LoginActivitySmsView loginActivitySmsView3 = (LoginActivitySmsView)views[3];
                final LoginActivitySmsView loginActivitySmsView4 = (LoginActivitySmsView)views[4];
                final LoginActivityRegisterView loginActivityRegisterView = (LoginActivityRegisterView)views[5];
                final LoginActivityPasswordView loginActivityPasswordView = (LoginActivityPasswordView)views[6];
                final LoginActivityRecoverView loginActivityRecoverView = (LoginActivityRecoverView)views[7];
                final LoginActivityResetWaitView loginActivityResetWaitView = (LoginActivityResetWaitView)views[8];
                final ArrayList<ThemeDescription> list = new ArrayList<ThemeDescription>();
                list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
                list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
                list.add(new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
                list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
                list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
                list.add(new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
                list.add(new ThemeDescription((View)phoneView.countryButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription(phoneView.view, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhiteGrayLine"));
                list.add(new ThemeDescription((View)phoneView.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)phoneView.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)phoneView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)phoneView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)phoneView.phoneField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)phoneView.phoneField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)phoneView.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)phoneView.phoneField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)phoneView.textView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.codeField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.cancelButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.resetAccountButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteRedText6"));
                list.add(new ThemeDescription((View)loginActivityPasswordView.resetAccountText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.firstNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.firstNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.firstNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.lastNameField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.lastNameField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.lastNameField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.wrongNumber, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.privacyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityRegisterView.privacyView, ThemeDescription.FLAG_LINKCOLOR, null, null, null, null, "windowBackgroundWhiteLinkText"));
                list.add(new ThemeDescription((View)loginActivityRecoverView.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityRecoverView.codeField, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivityRecoverView.codeField, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
                list.add(new ThemeDescription((View)loginActivityRecoverView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
                list.add(new ThemeDescription((View)loginActivityRecoverView.codeField, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                list.add(new ThemeDescription((View)loginActivityRecoverView.cancelButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription((View)loginActivityResetWaitView.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityResetWaitView.resetAccountText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityResetWaitView.resetAccountTime, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityResetWaitView.resetAccountButton, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivityResetWaitView.resetAccountButton, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteRedText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                if (loginActivitySmsView.codeField != null) {
                    for (int i = 0; i < loginActivitySmsView.codeField.length; ++i) {
                        list.add(new ThemeDescription((View)loginActivitySmsView.codeField[i], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                        list.add(new ThemeDescription((View)loginActivitySmsView.codeField[i], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                    }
                }
                list.add(new ThemeDescription((View)loginActivitySmsView.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription(loginActivitySmsView.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
                list.add(new ThemeDescription(loginActivitySmsView.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
                list.add(new ThemeDescription((View)loginActivitySmsView.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
                list.add(new ThemeDescription((View)loginActivitySmsView2.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView2.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                if (loginActivitySmsView2.codeField != null) {
                    for (int j = 0; j < loginActivitySmsView2.codeField.length; ++j) {
                        list.add(new ThemeDescription((View)loginActivitySmsView2.codeField[j], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                        list.add(new ThemeDescription((View)loginActivitySmsView2.codeField[j], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                    }
                }
                list.add(new ThemeDescription((View)loginActivitySmsView2.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView2.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription(loginActivitySmsView2.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
                list.add(new ThemeDescription(loginActivitySmsView2.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
                list.add(new ThemeDescription((View)loginActivitySmsView2.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView2.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
                list.add(new ThemeDescription((View)loginActivitySmsView3.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView3.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                if (loginActivitySmsView3.codeField != null) {
                    for (int k = 0; k < loginActivitySmsView3.codeField.length; ++k) {
                        list.add(new ThemeDescription((View)loginActivitySmsView3.codeField[k], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                        list.add(new ThemeDescription((View)loginActivitySmsView3.codeField[k], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                    }
                }
                list.add(new ThemeDescription((View)loginActivitySmsView3.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView3.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription(loginActivitySmsView3.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
                list.add(new ThemeDescription(loginActivitySmsView3.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
                list.add(new ThemeDescription((View)loginActivitySmsView3.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView3.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
                list.add(new ThemeDescription((View)loginActivitySmsView4.confirmTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView4.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                if (loginActivitySmsView4.codeField != null) {
                    for (int l = 0; l < loginActivitySmsView4.codeField.length; ++l) {
                        list.add(new ThemeDescription((View)loginActivitySmsView4.codeField[l], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                        list.add(new ThemeDescription((View)loginActivitySmsView4.codeField[l], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
                    }
                }
                list.add(new ThemeDescription((View)loginActivitySmsView4.timeText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
                list.add(new ThemeDescription((View)loginActivitySmsView4.problemText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
                list.add(new ThemeDescription(loginActivitySmsView4.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressInner"));
                list.add(new ThemeDescription(loginActivitySmsView4.progressView, 0, new Class[] { ProgressView.class }, new String[] { "paint" }, null, null, null, "login_progressOuter"));
                list.add(new ThemeDescription((View)loginActivitySmsView4.blackImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
                list.add(new ThemeDescription((View)loginActivitySmsView4.blueImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionBackground"));
                return list.toArray(new ThemeDescription[0]);
            }
            if (views[n] == null) {
                return new ThemeDescription[0];
            }
            ++n;
        }
    }
    
    public void needHideProgress(final boolean b) {
        if (this.progressRequestId != 0) {
            if (b) {
                ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.progressRequestId, true);
            }
            this.progressRequestId = 0;
        }
        this.showEditDoneProgress(false);
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        final LoginActivityRegisterView loginActivityRegisterView = (LoginActivityRegisterView)this.views[5];
        if (loginActivityRegisterView != null) {
            loginActivityRegisterView.imageUpdater.onActivityResult(n, n2, intent);
        }
    }
    
    @Override
    public boolean onBackPressed() {
        final int currentViewNum = this.currentViewNum;
        int n = 0;
        if (currentViewNum == 0) {
            while (true) {
                final SlideView[] views = this.views;
                if (n >= views.length) {
                    break;
                }
                if (views[n] != null) {
                    views[n].onDestroyActivity();
                }
                ++n;
            }
            this.clearCurrentState();
            return true;
        }
        if (currentViewNum == 6) {
            this.views[currentViewNum].onBackPressed(true);
            this.setPage(0, true, null, true);
        }
        else if (currentViewNum != 7 && currentViewNum != 8) {
            if (currentViewNum >= 1 && currentViewNum <= 4) {
                if (this.views[currentViewNum].onBackPressed(false)) {
                    this.setPage(0, true, null, true);
                }
            }
            else {
                final int currentViewNum2 = this.currentViewNum;
                if (currentViewNum2 == 5) {
                    ((LoginActivityRegisterView)this.views[currentViewNum2]).wrongNumber.callOnClick();
                }
            }
        }
        else {
            this.views[this.currentViewNum].onBackPressed(true);
            this.setPage(6, true, null, true);
        }
        return false;
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        if (Build$VERSION.SDK_INT < 23) {
            return;
        }
        Label_0059: {
            if (dialog != this.permissionsDialog || this.permissionsItems.isEmpty() || this.getParentActivity() == null) {
                break Label_0059;
            }
            try {
                this.getParentActivity().requestPermissions((String[])this.permissionsItems.toArray(new String[0]), 6);
                Label_0107: {
                    return;
                }
                while (true) {
                    this.getParentActivity().requestPermissions((String[])this.permissionsShowItems.toArray(new String[0]), 7);
                    return;
                    continue;
                }
            }
            // iftrue(Label_0107:, dialog != this.permissionsShowDialog || this.permissionsShowItems.isEmpty() || this.getParentActivity() == null)
            catch (Exception ex) {}
        }
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        int n = 0;
        while (true) {
            final SlideView[] views = this.views;
            if (n >= views.length) {
                break;
            }
            if (views[n] != null) {
                views[n].onDestroyActivity();
            }
            ++n;
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
        if (this.newAccount) {
            ConnectionsManager.getInstance(super.currentAccount).setAppPaused(true, false);
        }
    }
    
    @Override
    public void onRequestPermissionsResultFragment(int n, final String[] array, final int[] array2) {
        if (n == 6) {
            this.checkPermissions = false;
            n = this.currentViewNum;
            if (n == 0) {
                this.views[n].onNextPressed();
            }
        }
        else if (n == 7) {
            this.checkShowPermissions = false;
            n = this.currentViewNum;
            if (n == 0) {
                ((PhoneView)this.views[n]).fillNumber();
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.newAccount) {
            ConnectionsManager.getInstance(super.currentAccount).setAppPaused(false, false);
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
        try {
            if (this.currentViewNum >= 1 && this.currentViewNum <= 4 && this.views[this.currentViewNum] instanceof LoginActivitySmsView) {
                final int access$700 = ((LoginActivitySmsView)this.views[this.currentViewNum]).openTime;
                if (access$700 != 0 && Math.abs(System.currentTimeMillis() / 1000L - access$700) >= 86400L) {
                    this.views[this.currentViewNum].onBackPressed(true);
                    this.setPage(0, false, null, true);
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        try {
            final Bundle bundle2 = new Bundle();
            bundle2.putInt("currentViewNum", this.currentViewNum);
            int n;
            if (this.syncContacts) {
                n = 1;
            }
            else {
                n = 0;
            }
            bundle2.putInt("syncContacts", n);
            for (int i = 0; i <= this.currentViewNum; ++i) {
                final SlideView slideView = this.views[i];
                if (slideView != null) {
                    slideView.saveStateParams(bundle2);
                }
            }
            final SharedPreferences$Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).edit();
            edit.clear();
            this.putBundleToEditor(bundle2, edit, null);
            edit.commit();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void setPage(int currentViewNum, final boolean b, final Bundle bundle, final boolean b2) {
        if (currentViewNum != 3 && currentViewNum != 8) {
            if (currentViewNum == 0) {
                this.checkPermissions = true;
                this.checkShowPermissions = true;
            }
            this.doneItem.setVisibility(0);
        }
        else {
            this.doneItem.setVisibility(8);
        }
        final int n = 2131165409;
        if (b) {
            final SlideView[] views = this.views;
            final SlideView slideView = views[this.currentViewNum];
            final SlideView slideView2 = views[currentViewNum];
            this.currentViewNum = currentViewNum;
            final ActionBar actionBar = super.actionBar;
            currentViewNum = n;
            if (!slideView2.needBackButton()) {
                if (this.newAccount) {
                    currentViewNum = n;
                }
                else {
                    currentViewNum = 0;
                }
            }
            actionBar.setBackButtonImage(currentViewNum);
            slideView2.setParams(bundle, false);
            super.actionBar.setTitle(slideView2.getHeaderName());
            this.setParentActivityTitle(slideView2.getHeaderName());
            slideView2.onShow();
            if (b2) {
                currentViewNum = -AndroidUtilities.displaySize.x;
            }
            else {
                currentViewNum = AndroidUtilities.displaySize.x;
            }
            slideView2.setX((float)currentViewNum);
            slideView2.setVisibility(0);
            final AnimatorSet set = new AnimatorSet();
            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    slideView.setVisibility(8);
                    slideView.setX(0.0f);
                }
            });
            final Property translation_X = View.TRANSLATION_X;
            if (b2) {
                currentViewNum = AndroidUtilities.displaySize.x;
            }
            else {
                currentViewNum = -AndroidUtilities.displaySize.x;
            }
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)slideView, translation_X, new float[] { (float)currentViewNum }), (Animator)ObjectAnimator.ofFloat((Object)slideView2, View.TRANSLATION_X, new float[] { 0.0f }) });
            set.setDuration(300L);
            set.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
            set.start();
        }
        else {
            final ActionBar actionBar2 = super.actionBar;
            int backButtonImage = n;
            if (!this.views[currentViewNum].needBackButton()) {
                if (this.newAccount) {
                    backButtonImage = n;
                }
                else {
                    backButtonImage = 0;
                }
            }
            actionBar2.setBackButtonImage(backButtonImage);
            this.views[this.currentViewNum].setVisibility(8);
            this.currentViewNum = currentViewNum;
            this.views[currentViewNum].setParams(bundle, false);
            this.views[currentViewNum].setVisibility(0);
            super.actionBar.setTitle(this.views[currentViewNum].getHeaderName());
            this.setParentActivityTitle(this.views[currentViewNum].getHeaderName());
            this.views[currentViewNum].onShow();
        }
    }
    
    public class LoginActivityPasswordView extends SlideView
    {
        private TextView cancelButton;
        private EditTextBoldCursor codeField;
        private TextView confirmTextView;
        private Bundle currentParams;
        private int current_g;
        private byte[] current_p;
        private byte[] current_salt1;
        private byte[] current_salt2;
        private byte[] current_srp_B;
        private long current_srp_id;
        private String email_unconfirmed_pattern;
        private boolean has_recovery;
        private String hint;
        private boolean nextPressed;
        private int passwordType;
        private String phoneCode;
        private String phoneHash;
        private String requestPhone;
        private TextView resetAccountButton;
        private TextView resetAccountText;
        
        public LoginActivityPasswordView(final Context context) {
            super(context);
            this.setOrientation(1);
            (this.confirmTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            final TextView confirmTextView = this.confirmTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int gravity;
            if (isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            confirmTextView.setGravity(gravity);
            this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.confirmTextView.setText((CharSequence)LocaleController.getString("LoginPasswordText", 2131559789));
            final TextView confirmTextView2 = this.confirmTextView;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            this.addView((View)confirmTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n2));
            (this.codeField = new EditTextBoldCursor(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.codeField.setCursorWidth(1.5f);
            this.codeField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.codeField.setHint((CharSequence)LocaleController.getString("LoginPassword", 2131559788));
            this.codeField.setImeOptions(268435461);
            this.codeField.setTextSize(1, 18.0f);
            this.codeField.setMaxLines(1);
            this.codeField.setPadding(0, 0, 0, 0);
            this.codeField.setInputType(129);
            this.codeField.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            this.codeField.setTypeface(Typeface.DEFAULT);
            final EditTextBoldCursor codeField = this.codeField;
            int gravity2;
            if (LocaleController.isRTL) {
                gravity2 = 5;
            }
            else {
                gravity2 = 3;
            }
            codeField.setGravity(gravity2);
            this.addView((View)this.codeField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 1, 0, 20, 0, 0));
            this.codeField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$LoginActivityPasswordView$TmxPaCI1XFUfKacPC6vKEsUBp8g(this));
            this.cancelButton = new TextView(context);
            final TextView cancelButton = this.cancelButton;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            cancelButton.setGravity(n3 | 0x30);
            this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.cancelButton.setText((CharSequence)LocaleController.getString("ForgotPassword", 2131559503));
            this.cancelButton.setTextSize(1, 14.0f);
            this.cancelButton.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.cancelButton.setPadding(0, AndroidUtilities.dp(14.0f), 0, 0);
            final TextView cancelButton2 = this.cancelButton;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            this.addView((View)cancelButton2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, n4 | 0x30));
            this.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityPasswordView$XuGku6jlA50Wtx4H_NfhHJUyWxg(this));
            this.resetAccountButton = new TextView(context);
            final TextView resetAccountButton = this.resetAccountButton;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            resetAccountButton.setGravity(n5 | 0x30);
            this.resetAccountButton.setTextColor(Theme.getColor("windowBackgroundWhiteRedText6"));
            this.resetAccountButton.setVisibility(8);
            this.resetAccountButton.setText((CharSequence)LocaleController.getString("ResetMyAccount", 2131560597));
            this.resetAccountButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.resetAccountButton.setTextSize(1, 14.0f);
            this.resetAccountButton.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.resetAccountButton.setPadding(0, AndroidUtilities.dp(14.0f), 0, 0);
            final TextView resetAccountButton2 = this.resetAccountButton;
            int n6;
            if (LocaleController.isRTL) {
                n6 = 5;
            }
            else {
                n6 = 3;
            }
            this.addView((View)resetAccountButton2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n6 | 0x30, 0, 34, 0, 0));
            this.resetAccountButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityPasswordView$K2ROoTSLPIpCLdI8XOROn58qsk4(this));
            this.resetAccountText = new TextView(context);
            final TextView resetAccountText = this.resetAccountText;
            int n7;
            if (LocaleController.isRTL) {
                n7 = 5;
            }
            else {
                n7 = 3;
            }
            resetAccountText.setGravity(n7 | 0x30);
            this.resetAccountText.setVisibility(8);
            this.resetAccountText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.resetAccountText.setText((CharSequence)LocaleController.getString("ResetMyAccountText", 2131560598));
            this.resetAccountText.setTextSize(1, 14.0f);
            this.resetAccountText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            final TextView resetAccountText2 = this.resetAccountText;
            int n8;
            if (LocaleController.isRTL) {
                n8 = n;
            }
            else {
                n8 = 3;
            }
            this.addView((View)resetAccountText2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n8 | 0x30, 0, 7, 0, 14));
        }
        
        private void onPasscodeError(final boolean b) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            final Vibrator vibrator = (Vibrator)LoginActivity.this.getParentActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            if (b) {
                this.codeField.setText((CharSequence)"");
            }
            AndroidUtilities.shakeView((View)this.confirmTextView, 2.0f, 0);
        }
        
        @Override
        public String getHeaderName() {
            return LocaleController.getString("LoginPassword", 2131559788);
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public boolean onBackPressed(final boolean b) {
            this.nextPressed = false;
            LoginActivity.this.needHideProgress(true);
            this.currentParams = null;
            return true;
        }
        
        @Override
        public void onCancelPressed() {
            this.nextPressed = false;
        }
        
        @Override
        public void onNextPressed() {
            if (this.nextPressed) {
                return;
            }
            final String string = this.codeField.getText().toString();
            if (string.length() == 0) {
                this.onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            LoginActivity.this.needShowProgress(0);
            Utilities.globalQueue.postRunnable(new _$$Lambda$LoginActivity$LoginActivityPasswordView$t8xAmO8Vg_agS_AjQ9_oOEqu8kM(this, string));
        }
        
        @Override
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityPasswordView$CMrfS4f7oV_czyRbH6gwt0E8MoQ(this), 100L);
        }
        
        @Override
        public void restoreStateParams(final Bundle bundle) {
            this.currentParams = bundle.getBundle("passview_params");
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                this.setParams(currentParams, true);
            }
            final String string = bundle.getString("passview_code");
            if (string != null) {
                this.codeField.setText((CharSequence)string);
            }
        }
        
        @Override
        public void saveStateParams(final Bundle bundle) {
            final String string = this.codeField.getText().toString();
            if (string.length() != 0) {
                bundle.putString("passview_code", string);
            }
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                bundle.putBundle("passview_params", currentParams);
            }
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean b) {
            if (currentParams == null) {
                return;
            }
            final boolean empty = currentParams.isEmpty();
            boolean has_recovery = false;
            if (empty) {
                this.resetAccountButton.setVisibility(0);
                this.resetAccountText.setVisibility(0);
                AndroidUtilities.hideKeyboard((View)this.codeField);
                return;
            }
            this.resetAccountButton.setVisibility(8);
            this.resetAccountText.setVisibility(8);
            this.codeField.setText((CharSequence)"");
            this.currentParams = currentParams;
            this.current_salt1 = Utilities.hexToBytes(this.currentParams.getString("current_salt1"));
            this.current_salt2 = Utilities.hexToBytes(this.currentParams.getString("current_salt2"));
            this.current_p = Utilities.hexToBytes(this.currentParams.getString("current_p"));
            this.current_g = this.currentParams.getInt("current_g");
            this.current_srp_B = Utilities.hexToBytes(this.currentParams.getString("current_srp_B"));
            this.current_srp_id = this.currentParams.getLong("current_srp_id");
            this.passwordType = this.currentParams.getInt("passwordType");
            this.hint = this.currentParams.getString("hint");
            if (this.currentParams.getInt("has_recovery") == 1) {
                has_recovery = true;
            }
            this.has_recovery = has_recovery;
            this.email_unconfirmed_pattern = this.currentParams.getString("email_unconfirmed_pattern");
            this.requestPhone = currentParams.getString("phoneFormated");
            this.phoneHash = currentParams.getString("phoneHash");
            this.phoneCode = currentParams.getString("code");
            final String hint = this.hint;
            if (hint != null && hint.length() > 0) {
                this.codeField.setHint((CharSequence)this.hint);
            }
            else {
                this.codeField.setHint((CharSequence)LocaleController.getString("LoginPassword", 2131559788));
            }
        }
    }
    
    public class LoginActivityRecoverView extends SlideView
    {
        private TextView cancelButton;
        private EditTextBoldCursor codeField;
        private TextView confirmTextView;
        private Bundle currentParams;
        private String email_unconfirmed_pattern;
        private boolean nextPressed;
        
        public LoginActivityRecoverView(final Context context) {
            super(context);
            this.setOrientation(1);
            (this.confirmTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            final TextView confirmTextView = this.confirmTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int gravity;
            if (isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            confirmTextView.setGravity(gravity);
            this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.confirmTextView.setText((CharSequence)LocaleController.getString("RestoreEmailSentInfo", 2131560608));
            final TextView confirmTextView2 = this.confirmTextView;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            this.addView((View)confirmTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n2));
            (this.codeField = new EditTextBoldCursor(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.codeField.setCursorWidth(1.5f);
            this.codeField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.codeField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.codeField.setHint((CharSequence)LocaleController.getString("PasswordCode", 2131560345));
            this.codeField.setImeOptions(268435461);
            this.codeField.setTextSize(1, 18.0f);
            this.codeField.setMaxLines(1);
            this.codeField.setPadding(0, 0, 0, 0);
            this.codeField.setInputType(3);
            this.codeField.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            this.codeField.setTypeface(Typeface.DEFAULT);
            final EditTextBoldCursor codeField = this.codeField;
            int gravity2;
            if (LocaleController.isRTL) {
                gravity2 = 5;
            }
            else {
                gravity2 = 3;
            }
            codeField.setGravity(gravity2);
            this.addView((View)this.codeField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 1, 0, 20, 0, 0));
            this.codeField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$LoginActivityRecoverView$qDtPayVjLCLCB__uwfYE1UHVJU0(this));
            this.cancelButton = new TextView(context);
            final TextView cancelButton = this.cancelButton;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            cancelButton.setGravity(n3 | 0x50);
            this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.cancelButton.setTextSize(1, 14.0f);
            this.cancelButton.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.cancelButton.setPadding(0, AndroidUtilities.dp(14.0f), 0, 0);
            final TextView cancelButton2 = this.cancelButton;
            int n4;
            if (LocaleController.isRTL) {
                n4 = n;
            }
            else {
                n4 = 3;
            }
            this.addView((View)cancelButton2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n4 | 0x50, 0, 0, 0, 14));
            this.cancelButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityRecoverView$T2kwdtaqLpflrB1CM7xmzXbnaj8(this));
        }
        
        private void onPasscodeError(final boolean b) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            final Vibrator vibrator = (Vibrator)LoginActivity.this.getParentActivity().getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(200L);
            }
            if (b) {
                this.codeField.setText((CharSequence)"");
            }
            AndroidUtilities.shakeView((View)this.confirmTextView, 2.0f, 0);
        }
        
        @Override
        public String getHeaderName() {
            return LocaleController.getString("LoginPassword", 2131559788);
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public boolean onBackPressed(final boolean b) {
            LoginActivity.this.needHideProgress(true);
            this.currentParams = null;
            this.nextPressed = false;
            return true;
        }
        
        @Override
        public void onCancelPressed() {
            this.nextPressed = false;
        }
        
        @Override
        public void onNextPressed() {
            if (this.nextPressed) {
                return;
            }
            if (this.codeField.getText().toString().length() == 0) {
                this.onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            final String string = this.codeField.getText().toString();
            if (string.length() == 0) {
                this.onPasscodeError(false);
                return;
            }
            LoginActivity.this.needShowProgress(0);
            final TLRPC.TL_auth_recoverPassword tl_auth_recoverPassword = new TLRPC.TL_auth_recoverPassword();
            tl_auth_recoverPassword.code = string;
            ConnectionsManager.getInstance(LoginActivity.this.currentAccount).sendRequest(tl_auth_recoverPassword, new _$$Lambda$LoginActivity$LoginActivityRecoverView$rgaN2I5iElJnNYqU1iHy0vwcr6w(this), 10);
        }
        
        @Override
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRecoverView$G4hHYij0gEl198Zvu2E28bVCEaY(this), 100L);
        }
        
        @Override
        public void restoreStateParams(final Bundle bundle) {
            this.currentParams = bundle.getBundle("recoveryview_params");
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                this.setParams(currentParams, true);
            }
            final String string = bundle.getString("recoveryview_code");
            if (string != null) {
                this.codeField.setText((CharSequence)string);
            }
        }
        
        @Override
        public void saveStateParams(final Bundle bundle) {
            final String string = this.codeField.getText().toString();
            if (string != null && string.length() != 0) {
                bundle.putString("recoveryview_code", string);
            }
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                bundle.putBundle("recoveryview_params", currentParams);
            }
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean b) {
            if (currentParams == null) {
                return;
            }
            this.codeField.setText((CharSequence)"");
            this.currentParams = currentParams;
            this.email_unconfirmed_pattern = this.currentParams.getString("email_unconfirmed_pattern");
            this.cancelButton.setText((CharSequence)LocaleController.formatString("RestoreEmailTrouble", 2131560609, this.email_unconfirmed_pattern));
            AndroidUtilities.showKeyboard((View)this.codeField);
            this.codeField.requestFocus();
        }
    }
    
    public class LoginActivityRegisterView extends SlideView implements ImageUpdaterDelegate
    {
        private TLRPC.FileLocation avatar;
        private AnimatorSet avatarAnimation;
        private TLRPC.FileLocation avatarBig;
        private AvatarDrawable avatarDrawable;
        private ImageView avatarEditor;
        private BackupImageView avatarImage;
        private View avatarOverlay;
        private RadialProgressView avatarProgressView;
        private boolean createAfterUpload;
        private Bundle currentParams;
        private EditTextBoldCursor firstNameField;
        private ImageUpdater imageUpdater;
        private EditTextBoldCursor lastNameField;
        private boolean nextPressed;
        private String phoneCode;
        private String phoneHash;
        private TextView privacyView;
        private String requestPhone;
        private TextView textView;
        private TLRPC.InputFile uploadedAvatar;
        private TextView wrongNumber;
        
        public LoginActivityRegisterView(final Context context) {
            super(context);
            this.nextPressed = false;
            this.setOrientation(1);
            (this.imageUpdater = new ImageUpdater()).setSearchAvailable(false);
            this.imageUpdater.setUploadAfterSelect(false);
            final ImageUpdater imageUpdater = this.imageUpdater;
            imageUpdater.parentFragment = LoginActivity.this;
            imageUpdater.delegate = (ImageUpdater.ImageUpdaterDelegate)this;
            (this.textView = new TextView(context)).setText((CharSequence)LocaleController.getString("RegisterText2", 2131560551));
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            final TextView textView = this.textView;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            textView.setGravity(gravity);
            this.textView.setTextSize(1, 14.0f);
            final TextView textView2 = this.textView;
            int n;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n, 0, 0, 0, 0));
            final FrameLayout frameLayout = new FrameLayout(context);
            this.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 21.0f, 0.0f, 0.0f));
            this.avatarDrawable = new AvatarDrawable();
            (this.avatarImage = new BackupImageView(context) {
                public void invalidate() {
                    if (LoginActivityRegisterView.this.avatarOverlay != null) {
                        LoginActivityRegisterView.this.avatarOverlay.invalidate();
                    }
                    super.invalidate();
                }
                
                public void invalidate(final int n, final int n2, final int n3, final int n4) {
                    if (LoginActivityRegisterView.this.avatarOverlay != null) {
                        LoginActivityRegisterView.this.avatarOverlay.invalidate();
                    }
                    super.invalidate(n, n2, n3, n4);
                }
            }).setRoundRadius(AndroidUtilities.dp(32.0f));
            this.avatarDrawable.setInfo(5, null, null, false);
            this.avatarImage.setImageDrawable(this.avatarDrawable);
            final BackupImageView avatarImage = this.avatarImage;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            frameLayout.addView((View)avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n2 | 0x30, 0.0f, 16.0f, 0.0f, 0.0f));
            final Paint paint = new Paint(1);
            paint.setColor(1426063360);
            this.avatarOverlay = new View(context) {
                protected void onDraw(final Canvas canvas) {
                    if (LoginActivityRegisterView.this.avatarImage != null && LoginActivityRegisterView.this.avatarProgressView.getVisibility() == 0) {
                        paint.setAlpha((int)(LoginActivityRegisterView.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f * LoginActivityRegisterView.this.avatarProgressView.getAlpha()));
                        canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0f), paint);
                    }
                }
            };
            final View avatarOverlay = this.avatarOverlay;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            frameLayout.addView(avatarOverlay, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n3 | 0x30, 0.0f, 16.0f, 0.0f, 0.0f));
            this.avatarOverlay.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$UrLK1f4_ouBTRVpCoDl3SfCgewQ(this));
            (this.avatarEditor = new ImageView(context) {
                public void invalidate() {
                    super.invalidate();
                    LoginActivityRegisterView.this.avatarOverlay.invalidate();
                }
                
                public void invalidate(final int n, final int n2, final int n3, final int n4) {
                    super.invalidate(n, n2, n3, n4);
                    LoginActivityRegisterView.this.avatarOverlay.invalidate();
                }
            }).setScaleType(ImageView$ScaleType.CENTER);
            this.avatarEditor.setImageResource(2131165276);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            this.avatarEditor.setPadding(AndroidUtilities.dp(2.0f), 0, 0, 0);
            final ImageView avatarEditor = this.avatarEditor;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            frameLayout.addView((View)avatarEditor, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n4 | 0x30, 0.0f, 16.0f, 0.0f, 0.0f));
            (this.avatarProgressView = new RadialProgressView(context) {
                @Override
                public void setAlpha(final float alpha) {
                    super.setAlpha(alpha);
                    LoginActivityRegisterView.this.avatarOverlay.invalidate();
                }
            }).setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            final RadialProgressView avatarProgressView = this.avatarProgressView;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            frameLayout.addView((View)avatarProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n5 | 0x30, 0.0f, 16.0f, 0.0f, 0.0f));
            this.showAvatarProgress(false, false);
            (this.firstNameField = new EditTextBoldCursor(context)).setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.firstNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.firstNameField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.firstNameField.setCursorWidth(1.5f);
            this.firstNameField.setHint((CharSequence)LocaleController.getString("FirstName", 2131559494));
            this.firstNameField.setImeOptions(268435461);
            this.firstNameField.setTextSize(1, 17.0f);
            this.firstNameField.setMaxLines(1);
            this.firstNameField.setInputType(8192);
            final EditTextBoldCursor firstNameField = this.firstNameField;
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
                n7 = 85.0f;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 85.0f;
            }
            else {
                n8 = 0.0f;
            }
            frameLayout.addView((View)firstNameField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, n6 | 0x30, n7, 0.0f, n8, 0.0f));
            this.firstNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$gWZuHFySSOyrQEK_aMCkRgvER6k(this));
            (this.lastNameField = new EditTextBoldCursor(context)).setHint((CharSequence)LocaleController.getString("LastName", 2131559728));
            this.lastNameField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.lastNameField.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.lastNameField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.lastNameField.setCursorWidth(1.5f);
            this.lastNameField.setImeOptions(268435462);
            this.lastNameField.setTextSize(1, 17.0f);
            this.lastNameField.setMaxLines(1);
            this.lastNameField.setInputType(8192);
            final EditTextBoldCursor lastNameField = this.lastNameField;
            int n9;
            if (LocaleController.isRTL) {
                n9 = 5;
            }
            else {
                n9 = 3;
            }
            float n10;
            if (LocaleController.isRTL) {
                n10 = 0.0f;
            }
            else {
                n10 = 85.0f;
            }
            float n11;
            if (LocaleController.isRTL) {
                n11 = 85.0f;
            }
            else {
                n11 = 0.0f;
            }
            frameLayout.addView((View)lastNameField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, n9 | 0x30, n10, 51.0f, n11, 0.0f));
            this.lastNameField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$EHan9TMOJ2z2Np_xyuMTnMKxl24(this));
            (this.wrongNumber = new TextView(context)).setText((CharSequence)LocaleController.getString("CancelRegistration", 2131558900));
            final TextView wrongNumber = this.wrongNumber;
            int n12;
            if (LocaleController.isRTL) {
                n12 = 5;
            }
            else {
                n12 = 3;
            }
            wrongNumber.setGravity(n12 | 0x1);
            this.wrongNumber.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.wrongNumber.setTextSize(1, 14.0f);
            this.wrongNumber.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.wrongNumber.setPadding(0, AndroidUtilities.dp(24.0f), 0, 0);
            this.wrongNumber.setVisibility(8);
            final TextView wrongNumber2 = this.wrongNumber;
            int n13;
            if (LocaleController.isRTL) {
                n13 = 5;
            }
            else {
                n13 = 3;
            }
            this.addView((View)wrongNumber2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n13 | 0x30, 0, 20, 0, 0));
            this.wrongNumber.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$9SHAtLUticbvQmKKGk0ALjL8TaM(this));
            (this.privacyView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.privacyView.setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
            this.privacyView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
            this.privacyView.setTextSize(1, 14.0f);
            this.privacyView.setGravity(81);
            this.privacyView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.addView((View)this.privacyView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -1, 81, 0, 28, 0, 16));
            final String string = LocaleController.getString("TermsOfServiceLogin", 2131560886);
            final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)string);
            final int index = string.indexOf(42);
            final int lastIndex = string.lastIndexOf(42);
            if (index != -1 && lastIndex != -1 && index != lastIndex) {
                text.replace(lastIndex, lastIndex + 1, (CharSequence)"");
                text.replace(index, index + 1, (CharSequence)"");
                text.setSpan((Object)new LinkSpan(), index, lastIndex - 1, 33);
            }
            this.privacyView.setText((CharSequence)text);
        }
        
        private void showAvatarProgress(final boolean b, final boolean b2) {
            if (this.avatarEditor == null) {
                return;
            }
            final AnimatorSet avatarAnimation = this.avatarAnimation;
            if (avatarAnimation != null) {
                avatarAnimation.cancel();
                this.avatarAnimation = null;
            }
            if (b2) {
                this.avatarAnimation = new AnimatorSet();
                if (b) {
                    this.avatarProgressView.setVisibility(0);
                    this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarEditor, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 1.0f }) });
                }
                else {
                    this.avatarEditor.setVisibility(0);
                    this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarEditor, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 0.0f }) });
                }
                this.avatarAnimation.setDuration(180L);
                this.avatarAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationCancel(final Animator animator) {
                        LoginActivityRegisterView.this.avatarAnimation = null;
                    }
                    
                    public void onAnimationEnd(final Animator animator) {
                        if (LoginActivityRegisterView.this.avatarAnimation != null) {
                            if (LoginActivityRegisterView.this.avatarEditor != null) {
                                if (b) {
                                    LoginActivityRegisterView.this.avatarEditor.setVisibility(4);
                                }
                                else {
                                    LoginActivityRegisterView.this.avatarProgressView.setVisibility(4);
                                }
                                LoginActivityRegisterView.this.avatarAnimation = null;
                            }
                        }
                    }
                });
                this.avatarAnimation.start();
            }
            else if (b) {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(4);
                this.avatarProgressView.setAlpha(1.0f);
                this.avatarProgressView.setVisibility(0);
            }
            else {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(0);
                this.avatarProgressView.setAlpha(0.0f);
                this.avatarProgressView.setVisibility(4);
            }
        }
        
        private void showTermsOfService(final boolean b) {
            if (LoginActivity.this.currentTermsOfService == null) {
                return;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("TermsOfService", 2131560885));
            if (b) {
                builder.setPositiveButton(LocaleController.getString("Accept", 2131558484), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$1zSGYizDeZnhefZSwKFasIIewOY(this));
                builder.setNegativeButton(LocaleController.getString("Decline", 2131559223), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$q2tVNOkdhWLHwjQp16kkC5ykcIE(this));
            }
            else {
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
            }
            final SpannableStringBuilder message = new SpannableStringBuilder((CharSequence)LoginActivity.this.currentTermsOfService.text);
            MessageObject.addEntitiesToText((CharSequence)message, LoginActivity.this.currentTermsOfService.entities, false, 0, false, false, false);
            builder.setMessage((CharSequence)message);
            LoginActivity.this.showDialog(builder.create());
        }
        
        @Override
        public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRegisterView$29x_JtuV_r7woPg6Q9kl8QOB7qw(this, photoSize2, photoSize));
        }
        
        @Override
        public String getHeaderName() {
            return LocaleController.getString("YourName", 2131561154);
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public boolean onBackPressed(final boolean b) {
            if (!b) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.getString("AreYouSureRegistration", 2131558695));
                builder.setNegativeButton(LocaleController.getString("Stop", 2131560820), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityRegisterView$tgsdp57qGOzyamGoBJL7DAG_UO0(this));
                builder.setPositiveButton(LocaleController.getString("Continue", 2131559153), null);
                LoginActivity.this.showDialog(builder.create());
                return false;
            }
            LoginActivity.this.needHideProgress(true);
            this.nextPressed = false;
            this.currentParams = null;
            return true;
        }
        
        @Override
        public void onCancelPressed() {
            this.nextPressed = false;
        }
        
        @Override
        public void onNextPressed() {
            if (this.nextPressed) {
                return;
            }
            if (LoginActivity.this.currentTermsOfService != null && LoginActivity.this.currentTermsOfService.popup) {
                this.showTermsOfService(true);
                return;
            }
            this.nextPressed = true;
            final TLRPC.TL_auth_signUp tl_auth_signUp = new TLRPC.TL_auth_signUp();
            tl_auth_signUp.phone_code = this.phoneCode;
            tl_auth_signUp.phone_code_hash = this.phoneHash;
            tl_auth_signUp.phone_number = this.requestPhone;
            tl_auth_signUp.first_name = this.firstNameField.getText().toString();
            tl_auth_signUp.last_name = this.lastNameField.getText().toString();
            LoginActivity.this.needShowProgress(0);
            ConnectionsManager.getInstance(LoginActivity.this.currentAccount).sendRequest(tl_auth_signUp, new _$$Lambda$LoginActivity$LoginActivityRegisterView$f0ZfM2Vj75AOg7vhpvjVYpg5d40(this), 10);
        }
        
        @Override
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivityRegisterView$mhiqEuaO3fJj3ukKBPnIzQ_c39Q(this), 100L);
        }
        
        @Override
        public void restoreStateParams(final Bundle bundle) {
            this.currentParams = bundle.getBundle("registerview_params");
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                this.setParams(currentParams, true);
            }
            try {
                final String string = bundle.getString("terms");
                if (string != null) {
                    final byte[] decode = Base64.decode(string, 0);
                    if (decode != null) {
                        final SerializedData serializedData = new SerializedData(decode);
                        LoginActivity.this.currentTermsOfService = TLRPC.TL_help_termsOfService.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        serializedData.cleanup();
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            final String string2 = bundle.getString("registerview_first");
            if (string2 != null) {
                this.firstNameField.setText((CharSequence)string2);
            }
            final String string3 = bundle.getString("registerview_last");
            if (string3 != null) {
                this.lastNameField.setText((CharSequence)string3);
            }
        }
        
        @Override
        public void saveStateParams(final Bundle bundle) {
            final String string = this.firstNameField.getText().toString();
            if (string.length() != 0) {
                bundle.putString("registerview_first", string);
            }
            final String string2 = this.lastNameField.getText().toString();
            if (string2.length() != 0) {
                bundle.putString("registerview_last", string2);
            }
            if (LoginActivity.this.currentTermsOfService != null) {
                final SerializedData serializedData = new SerializedData(LoginActivity.this.currentTermsOfService.getObjectSize());
                LoginActivity.this.currentTermsOfService.serializeToStream(serializedData);
                bundle.putString("terms", Base64.encodeToString(serializedData.toByteArray(), 0));
                serializedData.cleanup();
            }
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                bundle.putBundle("registerview_params", currentParams);
            }
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean b) {
            if (currentParams == null) {
                return;
            }
            this.firstNameField.setText((CharSequence)"");
            this.lastNameField.setText((CharSequence)"");
            this.requestPhone = currentParams.getString("phoneFormated");
            this.phoneHash = currentParams.getString("phoneHash");
            this.phoneCode = currentParams.getString("code");
            this.currentParams = currentParams;
        }
        
        public class LinkSpan extends ClickableSpan
        {
            public void onClick(final View view) {
                LoginActivityRegisterView.this.showTermsOfService(false);
            }
            
            public void updateDrawState(final TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
            }
        }
    }
    
    public class LoginActivityResetWaitView extends SlideView
    {
        private TextView confirmTextView;
        private Bundle currentParams;
        private String phoneCode;
        private String phoneHash;
        private String requestPhone;
        private TextView resetAccountButton;
        private TextView resetAccountText;
        private TextView resetAccountTime;
        private int startTime;
        private Runnable timeRunnable;
        private int waitTime;
        
        public LoginActivityResetWaitView(final Context context) {
            super(context);
            this.setOrientation(1);
            (this.confirmTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            final TextView confirmTextView = this.confirmTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 5;
            int gravity;
            if (isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            confirmTextView.setGravity(gravity);
            this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            final TextView confirmTextView2 = this.confirmTextView;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            this.addView((View)confirmTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n2));
            this.resetAccountText = new TextView(context);
            final TextView resetAccountText = this.resetAccountText;
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            resetAccountText.setGravity(n3 | 0x30);
            this.resetAccountText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.resetAccountText.setText((CharSequence)LocaleController.getString("ResetAccountStatus", 2131560588));
            this.resetAccountText.setTextSize(1, 14.0f);
            this.resetAccountText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            final TextView resetAccountText2 = this.resetAccountText;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            this.addView((View)resetAccountText2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n4 | 0x30, 0, 24, 0, 0));
            this.resetAccountTime = new TextView(context);
            final TextView resetAccountTime = this.resetAccountTime;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            resetAccountTime.setGravity(n5 | 0x30);
            this.resetAccountTime.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.resetAccountTime.setTextSize(1, 14.0f);
            this.resetAccountTime.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            final TextView resetAccountTime2 = this.resetAccountTime;
            int n6;
            if (LocaleController.isRTL) {
                n6 = 5;
            }
            else {
                n6 = 3;
            }
            this.addView((View)resetAccountTime2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n6 | 0x30, 0, 2, 0, 0));
            this.resetAccountButton = new TextView(context);
            final TextView resetAccountButton = this.resetAccountButton;
            int n7;
            if (LocaleController.isRTL) {
                n7 = 5;
            }
            else {
                n7 = 3;
            }
            resetAccountButton.setGravity(n7 | 0x30);
            this.resetAccountButton.setText((CharSequence)LocaleController.getString("ResetAccountButton", 2131560585));
            this.resetAccountButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.resetAccountButton.setTextSize(1, 14.0f);
            this.resetAccountButton.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.resetAccountButton.setPadding(0, AndroidUtilities.dp(14.0f), 0, 0);
            final TextView resetAccountButton2 = this.resetAccountButton;
            int n8;
            if (LocaleController.isRTL) {
                n8 = n;
            }
            else {
                n8 = 3;
            }
            this.addView((View)resetAccountButton2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n8 | 0x30, 0, 7, 0, 0));
            this.resetAccountButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivityResetWaitView$8W_HF39tcDgJINwVsj88SoCtEEI(this));
        }
        
        private void updateTimeText() {
            final int max = Math.max(0, this.waitTime - (ConnectionsManager.getInstance(LoginActivity.this.currentAccount).getCurrentTime() - this.startTime));
            final int n = max / 86400;
            final int n2 = max - 86400 * n;
            final int n3 = n2 / 3600;
            final int n4 = (n2 - n3 * 3600) / 60;
            if (n != 0) {
                final TextView resetAccountTime = this.resetAccountTime;
                final StringBuilder sb = new StringBuilder();
                sb.append(LocaleController.formatPluralString("DaysBold", n));
                sb.append(" ");
                sb.append(LocaleController.formatPluralString("HoursBold", n3));
                sb.append(" ");
                sb.append(LocaleController.formatPluralString("MinutesBold", n4));
                resetAccountTime.setText((CharSequence)AndroidUtilities.replaceTags(sb.toString()));
            }
            else {
                final TextView resetAccountTime2 = this.resetAccountTime;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(LocaleController.formatPluralString("HoursBold", n3));
                sb2.append(" ");
                sb2.append(LocaleController.formatPluralString("MinutesBold", n4));
                sb2.append(" ");
                sb2.append(LocaleController.formatPluralString("SecondsBold", max % 60));
                resetAccountTime2.setText((CharSequence)AndroidUtilities.replaceTags(sb2.toString()));
            }
            if (max > 0) {
                this.resetAccountButton.setTag((Object)"windowBackgroundWhiteGrayText6");
                this.resetAccountButton.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            }
            else {
                this.resetAccountButton.setTag((Object)"windowBackgroundWhiteRedText6");
                this.resetAccountButton.setTextColor(Theme.getColor("windowBackgroundWhiteRedText6"));
            }
        }
        
        @Override
        public String getHeaderName() {
            return LocaleController.getString("ResetAccount", 2131560584);
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public boolean onBackPressed(final boolean b) {
            LoginActivity.this.needHideProgress(true);
            AndroidUtilities.cancelRunOnUIThread(this.timeRunnable);
            this.timeRunnable = null;
            this.currentParams = null;
            return true;
        }
        
        @Override
        public void restoreStateParams(Bundle currentParams) {
            this.currentParams = currentParams.getBundle("resetview_params");
            currentParams = this.currentParams;
            if (currentParams != null) {
                this.setParams(currentParams, true);
            }
        }
        
        @Override
        public void saveStateParams(final Bundle bundle) {
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                bundle.putBundle("resetview_params", currentParams);
            }
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean b) {
            if (currentParams == null) {
                return;
            }
            this.currentParams = currentParams;
            this.requestPhone = currentParams.getString("phoneFormated");
            this.phoneHash = currentParams.getString("phoneHash");
            this.phoneCode = currentParams.getString("code");
            this.startTime = currentParams.getInt("startTime");
            this.waitTime = currentParams.getInt("waitTime");
            final TextView confirmTextView = this.confirmTextView;
            final PhoneFormat instance = PhoneFormat.getInstance();
            final StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(this.requestPhone);
            confirmTextView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("ResetAccountInfo", 2131560587, LocaleController.addNbsp(instance.format(sb.toString())))));
            this.updateTimeText();
            AndroidUtilities.runOnUIThread(this.timeRunnable = new Runnable() {
                @Override
                public void run() {
                    if (LoginActivityResetWaitView.this.timeRunnable != this) {
                        return;
                    }
                    LoginActivityResetWaitView.this.updateTimeText();
                    AndroidUtilities.runOnUIThread(LoginActivityResetWaitView.this.timeRunnable, 1000L);
                }
            }, 1000L);
        }
    }
    
    public class LoginActivitySmsView extends SlideView implements NotificationCenterDelegate
    {
        private ImageView blackImageView;
        private ImageView blueImageView;
        private String catchedPhone;
        private EditTextBoldCursor[] codeField;
        private LinearLayout codeFieldContainer;
        private int codeTime;
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private int currentType;
        private String emailPhone;
        private boolean ignoreOnTextChange;
        private boolean isRestored;
        private double lastCodeTime;
        private double lastCurrentTime;
        private String lastError;
        private int length;
        private boolean nextPressed;
        private int nextType;
        private int openTime;
        private String pattern;
        private String phone;
        private String phoneHash;
        private TextView problemText;
        private ProgressView progressView;
        private String requestPhone;
        private int time;
        private TextView timeText;
        private Timer timeTimer;
        private int timeout;
        private final Object timerSync;
        private TextView titleTextView;
        private boolean waitingForEvent;
        
        public LoginActivitySmsView(final Context context, int gravity) {
            super(context);
            this.timerSync = new Object();
            this.time = 60000;
            this.codeTime = 15000;
            this.lastError = "";
            this.pattern = "*";
            this.currentType = gravity;
            this.setOrientation(1);
            (this.confirmTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            (this.titleTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            final TextView titleTextView = this.titleTextView;
            final boolean isRTL = LocaleController.isRTL;
            final int n = 3;
            if (isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            titleTextView.setGravity(gravity);
            this.titleTextView.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            if (this.currentType == 3) {
                final TextView confirmTextView = this.confirmTextView;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                confirmTextView.setGravity(gravity | 0x30);
                final FrameLayout frameLayout = new FrameLayout(context);
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                this.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, gravity));
                final ImageView imageView = new ImageView(context);
                imageView.setImageResource(2131165739);
                final boolean isRTL2 = LocaleController.isRTL;
                if (isRTL2) {
                    frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 76.0f, 19, 2.0f, 2.0f, 0.0f, 0.0f));
                    final TextView confirmTextView2 = this.confirmTextView;
                    if (LocaleController.isRTL) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    frameLayout.addView((View)confirmTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity, 82.0f, 0.0f, 0.0f, 0.0f));
                }
                else {
                    final TextView confirmTextView3 = this.confirmTextView;
                    if (isRTL2) {
                        gravity = 5;
                    }
                    else {
                        gravity = 3;
                    }
                    frameLayout.addView((View)confirmTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity, 0.0f, 0.0f, 82.0f, 0.0f));
                    frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 76.0f, 21, 0.0f, 2.0f, 0.0f, 2.0f));
                }
            }
            else {
                this.confirmTextView.setGravity(49);
                final FrameLayout frameLayout2 = new FrameLayout(context);
                this.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
                if (this.currentType == 1) {
                    (this.blackImageView = new ImageView(context)).setImageResource(2131165856);
                    this.blackImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blackImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    (this.blueImageView = new ImageView(context)).setImageResource(2131165854);
                    this.blueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText((CharSequence)LocaleController.getString("SentAppCodeTitle", 2131560718));
                }
                else {
                    (this.blueImageView = new ImageView(context)).setImageResource(2131165855);
                    this.blueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionBackground"), PorterDuff$Mode.MULTIPLY));
                    frameLayout2.addView((View)this.blueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    this.titleTextView.setText((CharSequence)LocaleController.getString("SentSmsCodeTitle", 2131560722));
                }
                this.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                this.addView((View)this.confirmTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
            }
            (this.codeFieldContainer = new LinearLayout(context)).setOrientation(0);
            this.addView((View)this.codeFieldContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 36, 1));
            if (this.currentType == 3) {
                this.codeFieldContainer.setVisibility(8);
            }
            (this.timeText = new TextView(context) {
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            }).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            if (this.currentType == 3) {
                this.timeText.setTextSize(1, 14.0f);
                final TextView timeText = this.timeText;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                else {
                    gravity = 3;
                }
                this.addView((View)timeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, gravity));
                this.progressView = new ProgressView(context);
                final TextView timeText2 = this.timeText;
                gravity = n;
                if (LocaleController.isRTL) {
                    gravity = 5;
                }
                timeText2.setGravity(gravity);
                this.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 3, 0.0f, 12.0f, 0.0f, 0.0f));
            }
            else {
                this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
                this.timeText.setTextSize(1, 15.0f);
                this.timeText.setGravity(49);
                this.addView((View)this.timeText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
            }
            (this.problemText = new TextView(context) {
                protected void onMeasure(final int n, final int n2) {
                    super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            }).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.problemText.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            this.problemText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            if (this.currentType == 1) {
                this.problemText.setText((CharSequence)LocaleController.getString("DidNotGetTheCodeSms", 2131559267));
            }
            else {
                this.problemText.setText((CharSequence)LocaleController.getString("DidNotGetTheCode", 2131559266));
            }
            this.addView((View)this.problemText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 49));
            this.problemText.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$LoginActivitySmsView$WDlczSWG_Xmyhu8bi5YJvMI6JBs(this));
        }
        
        private void createCodeTimer() {
            if (this.codeTimer != null) {
                return;
            }
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = (double)System.currentTimeMillis();
            this.codeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$4$Fr5toso_Gx7wT_8YsmrVs3ysx4Y(this));
                }
            }, 0L, 1000L);
        }
        
        private void createTimer() {
            if (this.timeTimer != null) {
                return;
            }
            (this.timeTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    if (LoginActivitySmsView.this.timeTimer == null) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$5$yhZ0JlUwl5Kkxw_R7O3sYzK3tvc(this));
                }
            }, 0L, 1000L);
        }
        
        private void destroyCodeTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.codeTimer != null) {
                        this.codeTimer.cancel();
                        this.codeTimer = null;
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        private void destroyTimer() {
            try {
                synchronized (this.timerSync) {
                    if (this.timeTimer != null) {
                        this.timeTimer.cancel();
                        this.timeTimer = null;
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        private String getCode() {
            if (this.codeField == null) {
                return "";
            }
            final StringBuilder sb = new StringBuilder();
            int n = 0;
            while (true) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (n >= codeField.length) {
                    break;
                }
                sb.append(PhoneFormat.stripExceptNumbers(codeField[n].getText().toString()));
                ++n;
            }
            return sb.toString();
        }
        
        private void resendCode() {
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            this.nextPressed = true;
            final TLRPC.TL_auth_resendCode tl_auth_resendCode = new TLRPC.TL_auth_resendCode();
            tl_auth_resendCode.phone_number = this.requestPhone;
            tl_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(LoginActivity.this.currentAccount).sendRequest(tl_auth_resendCode, new _$$Lambda$LoginActivity$LoginActivitySmsView$7Coa87SRXNZ2UoSLGVeBv7VQ3Fs(this, bundle), 10);
            LoginActivity.this.needShowProgress(0);
        }
        
        @Override
        public void didReceivedNotification(final int n, final int n2, final Object... array) {
            if (this.waitingForEvent) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (codeField != null) {
                    if (n == NotificationCenter.didReceiveSmsCode) {
                        final EditTextBoldCursor editTextBoldCursor = codeField[0];
                        final StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(array[0]);
                        editTextBoldCursor.setText((CharSequence)sb.toString());
                        this.onNextPressed();
                    }
                    else if (n == NotificationCenter.didReceiveCall) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(array[0]);
                        final String string = sb2.toString();
                        if (!AndroidUtilities.checkPhonePattern(this.pattern, string)) {
                            return;
                        }
                        if (!this.pattern.equals("*")) {
                            this.catchedPhone = string;
                            AndroidUtilities.endIncomingCall();
                        }
                        this.ignoreOnTextChange = true;
                        this.codeField[0].setText((CharSequence)string);
                        this.ignoreOnTextChange = false;
                        this.onNextPressed();
                    }
                }
            }
        }
        
        @Override
        public String getHeaderName() {
            if (this.currentType == 1) {
                return this.phone;
            }
            return LocaleController.getString("YourCode", 2131561142);
        }
        
        @Override
        public boolean needBackButton() {
            return true;
        }
        
        @Override
        public boolean onBackPressed(final boolean b) {
            if (!b) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.getString("StopVerification", 2131560831));
                builder.setPositiveButton(LocaleController.getString("Continue", 2131559153), null);
                builder.setNegativeButton(LocaleController.getString("Stop", 2131560820), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$LoginActivitySmsView$hHDcDlk50TjqyjTury7_j6_VypU(this));
                LoginActivity.this.showDialog(builder.create());
                return false;
            }
            this.nextPressed = false;
            LoginActivity.this.needHideProgress(true);
            final TLRPC.TL_auth_cancelCode tl_auth_cancelCode = new TLRPC.TL_auth_cancelCode();
            tl_auth_cancelCode.phone_number = this.requestPhone;
            tl_auth_cancelCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(LoginActivity.this.currentAccount).sendRequest(tl_auth_cancelCode, (RequestDelegate)_$$Lambda$LoginActivity$LoginActivitySmsView$m2HPGKwCNffO48k34_Pfyo6t35w.INSTANCE, 10);
            this.destroyTimer();
            this.destroyCodeTimer();
            this.currentParams = null;
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            return true;
        }
        
        @Override
        public void onCancelPressed() {
            this.nextPressed = false;
        }
        
        @Override
        public void onDestroyActivity() {
            super.onDestroyActivity();
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            this.destroyTimer();
            this.destroyCodeTimer();
        }
        
        protected void onLayout(final boolean b, int n, int bottom, int n2, final int n3) {
            super.onLayout(b, n, bottom, n2, n3);
            if (this.currentType != 3 && this.blueImageView != null) {
                bottom = this.confirmTextView.getBottom();
                n = this.getMeasuredHeight() - bottom;
                if (this.problemText.getVisibility() == 0) {
                    n2 = this.problemText.getMeasuredHeight();
                    n = n + bottom - n2;
                    final TextView problemText = this.problemText;
                    problemText.layout(problemText.getLeft(), n, this.problemText.getRight(), n2 + n);
                }
                else if (this.timeText.getVisibility() == 0) {
                    n2 = this.timeText.getMeasuredHeight();
                    n = n + bottom - n2;
                    final TextView timeText = this.timeText;
                    timeText.layout(timeText.getLeft(), n, this.timeText.getRight(), n2 + n);
                }
                else {
                    n += bottom;
                }
                n2 = this.codeFieldContainer.getMeasuredHeight();
                n = (n - bottom - n2) / 2 + bottom;
                final LinearLayout codeFieldContainer = this.codeFieldContainer;
                codeFieldContainer.layout(codeFieldContainer.getLeft(), n, this.codeFieldContainer.getRight(), n2 + n);
            }
        }
        
        protected void onMeasure(int dp, int n) {
            super.onMeasure(dp, n);
            if (this.currentType != 3) {
                final ImageView blueImageView = this.blueImageView;
                if (blueImageView != null) {
                    n = blueImageView.getMeasuredHeight() + this.titleTextView.getMeasuredHeight() + this.confirmTextView.getMeasuredHeight() + AndroidUtilities.dp(35.0f);
                    final int dp2 = AndroidUtilities.dp(80.0f);
                    dp = AndroidUtilities.dp(291.0f);
                    if (LoginActivity.this.scrollHeight - n < dp2) {
                        this.setMeasuredDimension(this.getMeasuredWidth(), n + dp2);
                    }
                    else if (LoginActivity.this.scrollHeight > dp) {
                        this.setMeasuredDimension(this.getMeasuredWidth(), dp);
                    }
                    else {
                        this.setMeasuredDimension(this.getMeasuredWidth(), LoginActivity.this.scrollHeight);
                    }
                }
            }
        }
        
        @Override
        public void onNextPressed() {
            if (this.nextPressed) {
                return;
            }
            final String code = this.getCode();
            if (TextUtils.isEmpty((CharSequence)code)) {
                AndroidUtilities.shakeView((View)this.codeFieldContainer, 2.0f, 0);
                return;
            }
            this.nextPressed = true;
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            final TLRPC.TL_auth_signIn tl_auth_signIn = new TLRPC.TL_auth_signIn();
            tl_auth_signIn.phone_number = this.requestPhone;
            tl_auth_signIn.phone_code = code;
            tl_auth_signIn.phone_code_hash = this.phoneHash;
            this.destroyTimer();
            LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(LoginActivity.this.currentAccount).sendRequest(tl_auth_signIn, new _$$Lambda$LoginActivity$LoginActivitySmsView$QuZqrlgFwDiyZUcg7gbDXVsiiUk(this, tl_auth_signIn), 10));
        }
        
        @Override
        public void onShow() {
            super.onShow();
            if (this.currentType == 3) {
                return;
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$LoginActivitySmsView$Bj_lU3gGH1xepmEk_N_1k9TPJGE(this), 100L);
        }
        
        @Override
        public void restoreStateParams(final Bundle bundle) {
            final StringBuilder sb = new StringBuilder();
            sb.append("smsview_params_");
            sb.append(this.currentType);
            this.currentParams = bundle.getBundle(sb.toString());
            final Bundle currentParams = this.currentParams;
            if (currentParams != null) {
                this.setParams(currentParams, true);
            }
            final String string = bundle.getString("catchedPhone");
            if (string != null) {
                this.catchedPhone = string;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("smsview_code_");
            sb2.append(this.currentType);
            final String string2 = bundle.getString(sb2.toString());
            if (string2 != null) {
                final EditTextBoldCursor[] codeField = this.codeField;
                if (codeField != null) {
                    codeField[0].setText((CharSequence)string2);
                }
            }
            final int int1 = bundle.getInt("time");
            if (int1 != 0) {
                this.time = int1;
            }
            final int int2 = bundle.getInt("open");
            if (int2 != 0) {
                this.openTime = int2;
            }
        }
        
        @Override
        public void saveStateParams(final Bundle bundle) {
            final String code = this.getCode();
            if (code.length() != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("smsview_code_");
                sb.append(this.currentType);
                bundle.putString(sb.toString(), code);
            }
            final String catchedPhone = this.catchedPhone;
            if (catchedPhone != null) {
                bundle.putString("catchedPhone", catchedPhone);
            }
            if (this.currentParams != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("smsview_params_");
                sb2.append(this.currentType);
                bundle.putBundle(sb2.toString(), this.currentParams);
            }
            final int time = this.time;
            if (time != 0) {
                bundle.putInt("time", time);
            }
            final int openTime = this.openTime;
            if (openTime != 0) {
                bundle.putInt("open", openTime);
            }
        }
        
        @Override
        public void setParams(final Bundle currentParams, final boolean isRestored) {
            if (currentParams == null) {
                return;
            }
            this.isRestored = isRestored;
            this.waitingForEvent = true;
            final int currentType = this.currentType;
            if (currentType == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            }
            else if (currentType == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.currentParams = currentParams;
            this.phone = currentParams.getString("phone");
            this.emailPhone = currentParams.getString("ephone");
            this.requestPhone = currentParams.getString("phoneFormated");
            this.phoneHash = currentParams.getString("phoneHash");
            final int int1 = currentParams.getInt("timeout");
            this.time = int1;
            this.timeout = int1;
            this.openTime = (int)(System.currentTimeMillis() / 1000L);
            this.nextType = currentParams.getInt("nextType");
            this.pattern = currentParams.getString("pattern");
            this.length = currentParams.getInt("length");
            if (this.length == 0) {
                this.length = 5;
            }
            final EditTextBoldCursor[] codeField = this.codeField;
            Object text = "";
            int n = 8;
            if (codeField != null && codeField.length == this.length) {
                int n2 = 0;
                while (true) {
                    final EditTextBoldCursor[] codeField2 = this.codeField;
                    if (n2 >= codeField2.length) {
                        break;
                    }
                    codeField2[n2].setText((CharSequence)"");
                    ++n2;
                }
            }
            else {
                this.codeField = new EditTextBoldCursor[this.length];
                for (int i = 0; i < this.length; ++i) {
                    (this.codeField[i] = new EditTextBoldCursor(this.getContext())).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i].setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.codeField[i].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.codeField[i].setCursorWidth(1.5f);
                    final Drawable mutate = this.getResources().getDrawable(2131165811).mutate();
                    mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), PorterDuff$Mode.MULTIPLY));
                    this.codeField[i].setBackgroundDrawable(mutate);
                    this.codeField[i].setImeOptions(268435461);
                    this.codeField[i].setTextSize(1, 20.0f);
                    this.codeField[i].setMaxLines(1);
                    this.codeField[i].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.codeField[i].setPadding(0, 0, 0, 0);
                    this.codeField[i].setGravity(49);
                    if (this.currentType == 3) {
                        this.codeField[i].setEnabled(false);
                        this.codeField[i].setInputType(0);
                        this.codeField[i].setVisibility(8);
                    }
                    else {
                        this.codeField[i].setInputType(3);
                    }
                    final LinearLayout codeFieldContainer = this.codeFieldContainer;
                    final EditTextBoldCursor editTextBoldCursor = this.codeField[i];
                    int n3;
                    if (i != this.length - 1) {
                        n3 = 7;
                    }
                    else {
                        n3 = 0;
                    }
                    codeFieldContainer.addView((View)editTextBoldCursor, (ViewGroup$LayoutParams)LayoutHelper.createLinear(34, 36, 1, 0, 0, n3, 0));
                    this.codeField[i].addTextChangedListener((TextWatcher)new TextWatcher() {
                        public void afterTextChanged(final Editable editable) {
                            if (LoginActivitySmsView.this.ignoreOnTextChange) {
                                return;
                            }
                            final int length = editable.length();
                            if (length >= 1) {
                                if (length > 1) {
                                    final String string = editable.toString();
                                    LoginActivitySmsView.this.ignoreOnTextChange = true;
                                    for (int i = 0; i < Math.min(LoginActivitySmsView.this.length - i, length); ++i) {
                                        if (i == 0) {
                                            editable.replace(0, length, (CharSequence)string.substring(i, i + 1));
                                        }
                                        else {
                                            LoginActivitySmsView.this.codeField[i + i].setText((CharSequence)string.substring(i, i + 1));
                                        }
                                    }
                                    LoginActivitySmsView.this.ignoreOnTextChange = false;
                                }
                                if (i != LoginActivitySmsView.this.length - 1) {
                                    LoginActivitySmsView.this.codeField[i + 1].setSelection(LoginActivitySmsView.this.codeField[i + 1].length());
                                    LoginActivitySmsView.this.codeField[i + 1].requestFocus();
                                }
                                if ((i == LoginActivitySmsView.this.length - 1 || (i == LoginActivitySmsView.this.length - 2 && length >= 2)) && LoginActivitySmsView.this.getCode().length() == LoginActivitySmsView.this.length) {
                                    LoginActivitySmsView.this.onNextPressed();
                                }
                            }
                        }
                        
                        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                        
                        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                        }
                    });
                    this.codeField[i].setOnKeyListener((View$OnKeyListener)new _$$Lambda$LoginActivity$LoginActivitySmsView$W4f4bbr6ANrn17O_fi8CZ_C8uvk(this, i));
                    this.codeField[i].setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$LoginActivitySmsView$f1ikQgn9Rce7rxNwD6eW9NGEWc4(this));
                }
            }
            final ProgressView progressView = this.progressView;
            if (progressView != null) {
                int visibility;
                if (this.nextType != 0) {
                    visibility = 0;
                }
                else {
                    visibility = 8;
                }
                progressView.setVisibility(visibility);
            }
            if (this.phone == null) {
                return;
            }
            final String format = PhoneFormat.getInstance().format(this.phone);
            final int currentType2 = this.currentType;
            if (currentType2 == 1) {
                text = AndroidUtilities.replaceTags(LocaleController.getString("SentAppCode", 2131560717));
            }
            else if (currentType2 == 2) {
                text = AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131560721, LocaleController.addNbsp(format)));
            }
            else if (currentType2 == 3) {
                text = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131560719, LocaleController.addNbsp(format)));
            }
            else if (currentType2 == 4) {
                text = AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131560720, LocaleController.addNbsp(format)));
            }
            this.confirmTextView.setText((CharSequence)text);
            if (this.currentType != 3) {
                AndroidUtilities.showKeyboard((View)this.codeField[0]);
                this.codeField[0].requestFocus();
            }
            else {
                AndroidUtilities.hideKeyboard((View)this.codeField[0]);
            }
            this.destroyTimer();
            this.destroyCodeTimer();
            this.lastCurrentTime = (double)System.currentTimeMillis();
            final int currentType3 = this.currentType;
            if (currentType3 == 1) {
                this.problemText.setVisibility(0);
                this.timeText.setVisibility(8);
            }
            else {
                if (currentType3 == 3) {
                    final int nextType = this.nextType;
                    if (nextType == 4 || nextType == 2) {
                        this.problemText.setVisibility(8);
                        this.timeText.setVisibility(0);
                        final int nextType2 = this.nextType;
                        if (nextType2 == 4) {
                            this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, 1, 0));
                        }
                        else if (nextType2 == 2) {
                            this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, 1, 0));
                        }
                        final String catchedPhone = this.catchedPhone;
                        if (catchedPhone != null) {
                            this.ignoreOnTextChange = true;
                            this.codeField[0].setText((CharSequence)catchedPhone);
                            this.ignoreOnTextChange = false;
                            this.onNextPressed();
                            return;
                        }
                        this.createTimer();
                        return;
                    }
                }
                if (this.currentType == 2) {
                    final int nextType3 = this.nextType;
                    if (nextType3 == 4 || nextType3 == 3) {
                        this.timeText.setText((CharSequence)LocaleController.formatString("CallText", 2131558885, 2, 0));
                        final TextView problemText = this.problemText;
                        int visibility2;
                        if (this.time < 1000) {
                            visibility2 = 0;
                        }
                        else {
                            visibility2 = 8;
                        }
                        problemText.setVisibility(visibility2);
                        final TextView timeText = this.timeText;
                        if (this.time >= 1000) {
                            n = 0;
                        }
                        timeText.setVisibility(n);
                        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                        final CharSequence charSequence = null;
                        final String string = sharedPreferences.getString("sms_hash", (String)null);
                        CharSequence substring = charSequence;
                        if (!TextUtils.isEmpty((CharSequence)string)) {
                            final String string2 = sharedPreferences.getString("sms_hash_code", (String)null);
                            substring = charSequence;
                            if (string2 != null) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append(string);
                                sb.append("|");
                                substring = charSequence;
                                if (string2.contains(sb.toString())) {
                                    substring = string2.substring(string2.indexOf(124) + 1);
                                }
                            }
                        }
                        if (substring != null) {
                            this.codeField[0].setText(substring);
                            this.onNextPressed();
                            return;
                        }
                        this.createTimer();
                        return;
                    }
                }
                if (this.currentType == 4 && this.nextType == 2) {
                    this.timeText.setText((CharSequence)LocaleController.formatString("SmsText", 2131560793, 2, 0));
                    final TextView problemText2 = this.problemText;
                    int visibility3;
                    if (this.time < 1000) {
                        visibility3 = 0;
                    }
                    else {
                        visibility3 = 8;
                    }
                    problemText2.setVisibility(visibility3);
                    final TextView timeText2 = this.timeText;
                    if (this.time >= 1000) {
                        n = 0;
                    }
                    timeText2.setVisibility(n);
                    this.createTimer();
                }
                else {
                    this.timeText.setVisibility(8);
                    this.problemText.setVisibility(8);
                    this.createCodeTimer();
                }
            }
        }
    }
    
    public class PhoneView extends SlideView implements AdapterView$OnItemSelectedListener
    {
        private CheckBoxCell checkBoxCell;
        private EditTextBoldCursor codeField;
        private HashMap<String, String> codesMap;
        private ArrayList<String> countriesArray;
        private HashMap<String, String> countriesMap;
        private TextView countryButton;
        private int countryState;
        private boolean ignoreOnPhoneChange;
        private boolean ignoreOnTextChange;
        private boolean ignoreSelection;
        private boolean nextPressed;
        private HintEditText phoneField;
        private HashMap<String, String> phoneFormatMap;
        private TextView textView;
        private TextView textView2;
        private View view;
        
        public PhoneView(Context context) {
            super(context);
            this.countryState = 0;
            this.countriesArray = new ArrayList<String>();
            this.countriesMap = new HashMap<String, String>();
            this.codesMap = new HashMap<String, String>();
            this.phoneFormatMap = new HashMap<String, String>();
            this.ignoreSelection = false;
            this.ignoreOnTextChange = false;
            this.ignoreOnPhoneChange = false;
            this.nextPressed = false;
            this.setOrientation(1);
            (this.countryButton = new TextView(context)).setTextSize(1, 18.0f);
            this.countryButton.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(12.0f), 0);
            this.countryButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.countryButton.setMaxLines(1);
            this.countryButton.setSingleLine(true);
            this.countryButton.setEllipsize(TextUtils$TruncateAt.END);
            final TextView countryButton = this.countryButton;
            int n;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            countryButton.setGravity(n | 0x1);
            this.countryButton.setBackgroundResource(2131165857);
            this.addView((View)this.countryButton, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 0.0f, 0.0f, 0.0f, 14.0f));
            this.countryButton.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$PhoneView$YKPRtOo2JwvFERoOBV4IqiJYOOU(this));
            (this.view = new View(context)).setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
            this.view.setBackgroundColor(Theme.getColor("windowBackgroundWhiteGrayLine"));
            this.addView(this.view, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 1, 4.0f, -17.5f, 4.0f, 0.0f));
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            this.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 20.0f, 0.0f, 0.0f));
            (this.textView = new TextView(context)).setText((CharSequence)"+");
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTextSize(1, 18.0f);
            linearLayout.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
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
            this.codeField.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(5) });
            linearLayout.addView((View)this.codeField, (ViewGroup$LayoutParams)LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 16.0f, 0.0f));
            this.codeField.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    if (PhoneView.this.ignoreOnTextChange) {
                        return;
                    }
                    PhoneView.this.ignoreOnTextChange = true;
                    final String stripExceptNumbers = PhoneFormat.stripExceptNumbers(PhoneView.this.codeField.getText().toString());
                    PhoneView.this.codeField.setText((CharSequence)stripExceptNumbers);
                    final int length = stripExceptNumbers.length();
                    final String s = null;
                    if (length == 0) {
                        PhoneView.this.countryButton.setText((CharSequence)LocaleController.getString("ChooseCountry", 2131559086));
                        PhoneView.this.phoneField.setHintText(null);
                        PhoneView.this.countryState = 1;
                    }
                    else {
                        final int length2 = stripExceptNumbers.length();
                        int i = 4;
                        String text = null;
                        String substring2 = null;
                        boolean b = false;
                        Label_0311: {
                            if (length2 > 4) {
                                while (true) {
                                    while (i >= 1) {
                                        final String substring = stripExceptNumbers.substring(0, i);
                                        if (PhoneView.this.codesMap.get(substring) != null) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append(stripExceptNumbers.substring(i));
                                            sb.append(PhoneView.this.phoneField.getText().toString());
                                            text = sb.toString();
                                            PhoneView.this.codeField.setText((CharSequence)substring);
                                            final int n = 1;
                                            substring2 = substring;
                                            b = (n != 0);
                                            if (n == 0) {
                                                final StringBuilder sb2 = new StringBuilder();
                                                sb2.append(substring.substring(1));
                                                sb2.append(PhoneView.this.phoneField.getText().toString());
                                                text = sb2.toString();
                                                final EditTextBoldCursor access$1300 = PhoneView.this.codeField;
                                                substring2 = substring.substring(0, 1);
                                                access$1300.setText((CharSequence)substring2);
                                                b = (n != 0);
                                            }
                                            break Label_0311;
                                        }
                                        else {
                                            --i;
                                        }
                                    }
                                    final String substring = stripExceptNumbers;
                                    text = null;
                                    final int n = false ? 1 : 0;
                                    continue;
                                }
                            }
                            substring2 = stripExceptNumbers;
                            text = null;
                            b = false;
                        }
                        final String o = PhoneView.this.codesMap.get(substring2);
                        if (o != null) {
                            final int index = PhoneView.this.countriesArray.indexOf(o);
                            if (index != -1) {
                                PhoneView.this.ignoreSelection = true;
                                PhoneView.this.countryButton.setText((CharSequence)PhoneView.this.countriesArray.get(index));
                                final String s2 = PhoneView.this.phoneFormatMap.get(substring2);
                                final HintEditText access$1301 = PhoneView.this.phoneField;
                                String replace = s;
                                if (s2 != null) {
                                    replace = s2.replace('X', '\u2013');
                                }
                                access$1301.setHintText(replace);
                                PhoneView.this.countryState = 0;
                            }
                            else {
                                PhoneView.this.countryButton.setText((CharSequence)LocaleController.getString("WrongCountry", 2131561125));
                                PhoneView.this.phoneField.setHintText(null);
                                PhoneView.this.countryState = 2;
                            }
                        }
                        else {
                            PhoneView.this.countryButton.setText((CharSequence)LocaleController.getString("WrongCountry", 2131561125));
                            PhoneView.this.phoneField.setHintText(null);
                            PhoneView.this.countryState = 2;
                        }
                        if (!b) {
                            PhoneView.this.codeField.setSelection(PhoneView.this.codeField.getText().length());
                        }
                        if (text != null) {
                            PhoneView.this.phoneField.requestFocus();
                            PhoneView.this.phoneField.setText((CharSequence)text);
                            PhoneView.this.phoneField.setSelection(PhoneView.this.phoneField.length());
                        }
                    }
                    PhoneView.this.ignoreOnTextChange = false;
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            this.codeField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$PhoneView$s6X9t4lGGXuJqcRZlLSeul8QD9A(this));
            (this.phoneField = new HintEditText(context) {
                public boolean onTouchEvent(final MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard((View)this)) {
                        this.clearFocus();
                        this.requestFocus();
                    }
                    return super.onTouchEvent(motionEvent);
                }
            }).setInputType(3);
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
            this.phoneField.setImeOptions(268435461);
            linearLayout.addView((View)this.phoneField, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f));
            this.phoneField.addTextChangedListener((TextWatcher)new TextWatcher() {
                private int actionPosition;
                private int characterAction = -1;
                
                public void afterTextChanged(final Editable editable) {
                    if (PhoneView.this.ignoreOnPhoneChange) {
                        return;
                    }
                    final int selectionStart = PhoneView.this.phoneField.getSelectionStart();
                    final String string = PhoneView.this.phoneField.getText().toString();
                    int n = selectionStart;
                    String string2 = string;
                    if (this.characterAction == 3) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(string.substring(0, this.actionPosition));
                        sb.append(string.substring(this.actionPosition + 1));
                        string2 = sb.toString();
                        n = selectionStart - 1;
                    }
                    final StringBuilder sb2 = new StringBuilder(string2.length());
                    int endIndex;
                    for (int i = 0; i < string2.length(); i = endIndex) {
                        endIndex = i + 1;
                        final String substring = string2.substring(i, endIndex);
                        if ("0123456789".contains(substring)) {
                            sb2.append(substring);
                        }
                    }
                    PhoneView.this.ignoreOnPhoneChange = true;
                    final String hintText = PhoneView.this.phoneField.getHintText();
                    int length = n;
                    Label_0349: {
                        if (hintText != null) {
                            int j = 0;
                            while (j < sb2.length()) {
                                if (j < hintText.length()) {
                                    int n2 = j;
                                    int n3 = n;
                                    if (hintText.charAt(j) == ' ') {
                                        sb2.insert(j, ' ');
                                        n2 = ++j;
                                        if ((n3 = n) == j) {
                                            final int characterAction = this.characterAction;
                                            n2 = j;
                                            n3 = n;
                                            if (characterAction != 2) {
                                                n2 = j;
                                                n3 = n;
                                                if (characterAction != 3) {
                                                    n3 = n + 1;
                                                    n2 = j;
                                                }
                                            }
                                        }
                                    }
                                    j = n2 + 1;
                                    n = n3;
                                }
                                else {
                                    sb2.insert(j, ' ');
                                    if (n != j + 1) {
                                        break;
                                    }
                                    final int characterAction2 = this.characterAction;
                                    if (characterAction2 != 2 && characterAction2 != 3) {
                                        length = n + 1;
                                        break Label_0349;
                                    }
                                    break;
                                }
                            }
                            length = n;
                        }
                    }
                    editable.replace(0, editable.length(), (CharSequence)sb2);
                    if (length >= 0) {
                        final HintEditText access$1500 = PhoneView.this.phoneField;
                        if (length > PhoneView.this.phoneField.length()) {
                            length = PhoneView.this.phoneField.length();
                        }
                        access$1500.setSelection(length);
                    }
                    PhoneView.this.phoneField.onTextChange();
                    PhoneView.this.ignoreOnPhoneChange = false;
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
            this.phoneField.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$LoginActivity$PhoneView$TEi0WT_UJWWkmE9ZUo0lhioWQRI(this));
            this.phoneField.setOnKeyListener((View$OnKeyListener)new _$$Lambda$LoginActivity$PhoneView$Qv5VLHdHxMblEcKPlwyU2WrV61c(this));
            (this.textView2 = new TextView(context)).setText((CharSequence)LocaleController.getString("StartText", 2131560805));
            this.textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.textView2.setTextSize(1, 14.0f);
            final TextView textView2 = this.textView2;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            textView2.setGravity(gravity);
            this.textView2.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            final TextView textView3 = this.textView2;
            int n2;
            if (LocaleController.isRTL) {
                n2 = 5;
            }
            else {
                n2 = 3;
            }
            this.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n2, 0, 28, 0, 10));
            final TextView textView4 = new TextView(context);
            textView4.setText((CharSequence)"You may need to set up a proxy before you login, in case your country or ISP blocks Telegram");
            textView4.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            textView4.setTextSize(1, 14.0f);
            int gravity2;
            if (LocaleController.isRTL) {
                gravity2 = 5;
            }
            else {
                gravity2 = 3;
            }
            textView4.setGravity(gravity2);
            textView4.setLineSpacing((float)AndroidUtilities.dp(2.0f), 1.0f);
            int n3;
            if (LocaleController.isRTL) {
                n3 = 5;
            }
            else {
                n3 = 3;
            }
            this.addView((View)textView4, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n3, 0, 28, 0, 10));
            final TextView textView5 = new TextView(context);
            textView5.setText((CharSequence)"SET A PROXY");
            textView5.setGravity(17);
            textView5.setTextColor(-1);
            textView5.setTextSize(1, 16.0f);
            textView5.setBackgroundResource(2131165800);
            if (Build$VERSION.SDK_INT >= 21) {
                final StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)textView5, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
                stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)textView5, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
                textView5.setStateListAnimator(stateListAnimator);
            }
            textView5.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(10.0f));
            this.addView((View)textView5, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 81, 10.0f, 0.0f, 10.0f, 10.0f));
            textView5.setOnClickListener((View$OnClickListener)new _$$Lambda$LoginActivity$PhoneView$qJHy69D4Arc703i2l6zpx_M85PQ(this));
            if (LoginActivity.this.newAccount) {
                (this.checkBoxCell = new CheckBoxCell(context, 2)).setText(LocaleController.getString("SyncContacts", 2131560849), "", LoginActivity.this.syncContacts, false);
                this.addView((View)this.checkBoxCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -1, 51, 0, 0, 0, 0));
                this.checkBoxCell.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    private Toast visibleToast;
                    
                    public void onClick(final View view) {
                        if (LoginActivity.this.getParentActivity() == null) {
                            return;
                        }
                        final CheckBoxCell checkBoxCell = (CheckBoxCell)view;
                        final LoginActivity this$0 = LoginActivity.this;
                        this$0.syncContacts ^= true;
                        checkBoxCell.setChecked(LoginActivity.this.syncContacts, true);
                        try {
                            if (this.visibleToast != null) {
                                this.visibleToast.cancel();
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        if (LoginActivity.this.syncContacts) {
                            (this.visibleToast = Toast.makeText((Context)LoginActivity.this.getParentActivity(), (CharSequence)LocaleController.getString("SyncContactsOn", 2131560856), 0)).show();
                        }
                        else {
                            (this.visibleToast = Toast.makeText((Context)LoginActivity.this.getParentActivity(), (CharSequence)LocaleController.getString("SyncContactsOff", 2131560855), 0)).show();
                        }
                    }
                });
            }
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getResources().getAssets().open("countries.txt")));
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
            context = null;
            Object upperCase;
            try {
                final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                upperCase = context;
                if (telephonyManager != null) {
                    upperCase = telephonyManager.getSimCountryIso().toUpperCase();
                }
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
                upperCase = context;
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
            if (this.codeField.length() != 0) {
                this.phoneField.requestFocus();
                final HintEditText phoneField = this.phoneField;
                phoneField.setSelection(phoneField.length());
            }
            else {
                this.codeField.requestFocus();
            }
        }
        
        public void fillNumber() {
            try {
                final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                if (telephonyManager.getSimState() != 1 && telephonyManager.getPhoneType() != 0) {
                    final int sdk_INT = Build$VERSION.SDK_INT;
                    CharSequence text = null;
                    final CharSequence charSequence = null;
                    boolean b2;
                    if (sdk_INT >= 23) {
                        final boolean b = b2 = (LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0);
                        if (LoginActivity.this.checkShowPermissions && !(b2 = b)) {
                            LoginActivity.this.permissionsShowItems.clear();
                            if (!b) {
                                LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_STATE");
                            }
                            if (!LoginActivity.this.permissionsShowItems.isEmpty()) {
                                final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                                if (!globalMainSettings.getBoolean("firstloginshow", true) && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                                    LoginActivity.this.getParentActivity().requestPermissions((String[])LoginActivity.this.permissionsShowItems.toArray(new String[0]), 7);
                                }
                                else {
                                    globalMainSettings.edit().putBoolean("firstloginshow", false).commit();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
                                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                                    builder.setMessage(LocaleController.getString("AllowFillNumber", 2131558606));
                                    LoginActivity.this.permissionsShowDialog = LoginActivity.this.showDialog(builder.create());
                                }
                            }
                            return;
                        }
                    }
                    else {
                        b2 = true;
                    }
                    if (!LoginActivity.this.newAccount && b2) {
                        final String stripExceptNumbers = PhoneFormat.stripExceptNumbers(telephonyManager.getLine1Number());
                        if (!TextUtils.isEmpty((CharSequence)stripExceptNumbers)) {
                            final int length = stripExceptNumbers.length();
                            int i = 4;
                            Label_0423: {
                                if (length > 4) {
                                    while (true) {
                                        while (i >= 1) {
                                            final String substring = stripExceptNumbers.substring(0, i);
                                            if (this.codesMap.get(substring) != null) {
                                                text = stripExceptNumbers.substring(i);
                                                this.codeField.setText((CharSequence)substring);
                                                final boolean b3 = true;
                                                if (!b3) {
                                                    text = stripExceptNumbers.substring(1);
                                                    this.codeField.setText((CharSequence)stripExceptNumbers.substring(0, 1));
                                                }
                                                break Label_0423;
                                            }
                                            else {
                                                --i;
                                            }
                                        }
                                        final boolean b3 = false;
                                        text = charSequence;
                                        continue;
                                    }
                                }
                            }
                            if (text != null) {
                                this.phoneField.requestFocus();
                                this.phoneField.setText(text);
                                this.phoneField.setSelection(this.phoneField.length());
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        
        @Override
        public String getHeaderName() {
            return LocaleController.getString("YourPhone", 2131561158);
        }
        
        @Override
        public void onCancelPressed() {
            this.nextPressed = false;
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
        
        @Override
        public void onNextPressed() {
            if (LoginActivity.this.getParentActivity() != null) {
                if (!this.nextPressed) {
                    final TelephonyManager telephonyManager = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                    if (BuildVars.DEBUG_VERSION) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("sim status = ");
                        sb.append(telephonyManager.getSimState());
                        FileLog.d(sb.toString());
                    }
                    final int simState = telephonyManager.getSimState();
                    final boolean b = simState != 1 && simState != 0 && telephonyManager.getPhoneType() != 0 && !AndroidUtilities.isAirplaneModeOn();
                    boolean b4;
                    if (Build$VERSION.SDK_INT >= 23 && b) {
                        final boolean b2 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                        final boolean b3 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.CALL_PHONE") == 0;
                        b4 = b2;
                        if (LoginActivity.this.checkPermissions) {
                            LoginActivity.this.permissionsItems.clear();
                            if (!b2) {
                                LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                            }
                            if (!b3) {
                                LoginActivity.this.permissionsItems.add("android.permission.CALL_PHONE");
                            }
                            b4 = b2;
                            if (!LoginActivity.this.permissionsItems.isEmpty()) {
                                final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                                boolean b5 = false;
                                Label_0465: {
                                    Label_0462: {
                                        if (!b3 && b2) {
                                            LoginActivity.this.getParentActivity().requestPermissions((String[])LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                                        }
                                        else {
                                            if (!globalMainSettings.getBoolean("firstlogin", true)) {
                                                if (!LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                                                    try {
                                                        LoginActivity.this.getParentActivity().requestPermissions((String[])LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                                                        break Label_0462;
                                                    }
                                                    catch (Exception ex3) {
                                                        b5 = false;
                                                        break Label_0465;
                                                    }
                                                }
                                            }
                                            globalMainSettings.edit().putBoolean("firstlogin", false).commit();
                                            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
                                            builder.setTitle(LocaleController.getString("AppName", 2131558635));
                                            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                                            builder.setMessage(LocaleController.getString("AllowReadCall", 2131558607));
                                            final LoginActivity this$0 = LoginActivity.this;
                                            this$0.permissionsDialog = this$0.showDialog(builder.create());
                                        }
                                    }
                                    b5 = true;
                                }
                                b4 = b2;
                                if (b5) {
                                    return;
                                }
                            }
                        }
                    }
                    else {
                        b4 = true;
                    }
                    final int countryState = this.countryState;
                    if (countryState == 1) {
                        LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("ChooseCountry", 2131559086));
                        return;
                    }
                    if (countryState == 2 && !BuildVars.DEBUG_VERSION) {
                        LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("WrongCountry", 2131561125));
                        return;
                    }
                    if (this.codeField.length() == 0) {
                        LoginActivity.this.needShowAlert(LocaleController.getString("AppName", 2131558635), LocaleController.getString("InvalidPhoneNumber", 2131559674));
                        return;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    sb2.append(this.codeField.getText());
                    sb2.append(this.phoneField.getText());
                    final String stripExceptNumbers = PhoneFormat.stripExceptNumbers(sb2.toString());
                    if (LoginActivity.this.getParentActivity() instanceof LaunchActivity) {
                        for (int i = 0; i < 3; ++i) {
                            final UserConfig instance = UserConfig.getInstance(i);
                            if (instance.isClientActivated()) {
                                if (PhoneNumberUtils.compare(stripExceptNumbers, instance.getCurrentUser().phone)) {
                                    final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)LoginActivity.this.getParentActivity());
                                    builder2.setTitle(LocaleController.getString("AppName", 2131558635));
                                    builder2.setMessage(LocaleController.getString("AccountAlreadyLoggedIn", 2131558487));
                                    builder2.setPositiveButton(LocaleController.getString("AccountSwitch", 2131558489), (DialogInterface$OnClickListener)new _$$Lambda$LoginActivity$PhoneView$8svHsEYnVD6CBObp_k25RKsR8iQ(this, i));
                                    builder2.setNegativeButton(LocaleController.getString("OK", 2131560097), null);
                                    LoginActivity.this.showDialog(builder2.create());
                                    return;
                                }
                            }
                        }
                    }
                    ConnectionsManager.getInstance(LoginActivity.this.currentAccount).cleanup(false);
                    final TLRPC.TL_auth_sendCode tl_auth_sendCode = new TLRPC.TL_auth_sendCode();
                    tl_auth_sendCode.api_hash = BuildVars.APP_HASH;
                    tl_auth_sendCode.api_id = BuildVars.APP_ID;
                    tl_auth_sendCode.phone_number = stripExceptNumbers;
                    tl_auth_sendCode.settings = new TLRPC.TL_codeSettings();
                    tl_auth_sendCode.settings.allow_flashcall = (b && b4);
                    if (Build$VERSION.SDK_INT >= 26) {
                        try {
                            tl_auth_sendCode.settings.app_hash = SmsManager.getDefault().createAppSpecificSmsToken(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, (Class)SmsReceiver.class), 134217728));
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                        }
                    }
                    else {
                        final TLRPC.TL_codeSettings settings = tl_auth_sendCode.settings;
                        settings.app_hash = BuildVars.SMS_HASH;
                        settings.app_hash_persistent = true;
                    }
                    final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                    if (!TextUtils.isEmpty((CharSequence)tl_auth_sendCode.settings.app_hash)) {
                        final TLRPC.TL_codeSettings settings2 = tl_auth_sendCode.settings;
                        settings2.flags |= 0x8;
                        sharedPreferences.edit().putString("sms_hash", tl_auth_sendCode.settings.app_hash).commit();
                    }
                    else {
                        sharedPreferences.edit().remove("sms_hash").commit();
                    }
                    if (tl_auth_sendCode.settings.allow_flashcall) {
                        try {
                            final String line1Number = telephonyManager.getLine1Number();
                            if (!TextUtils.isEmpty((CharSequence)line1Number)) {
                                if (!(tl_auth_sendCode.settings.current_number = PhoneNumberUtils.compare(stripExceptNumbers, line1Number))) {
                                    tl_auth_sendCode.settings.allow_flashcall = false;
                                }
                            }
                            else if (UserConfig.getActivatedAccountsCount() > 0) {
                                tl_auth_sendCode.settings.allow_flashcall = false;
                            }
                            else {
                                tl_auth_sendCode.settings.current_number = false;
                            }
                        }
                        catch (Exception ex) {
                            tl_auth_sendCode.settings.allow_flashcall = false;
                            FileLog.e(ex);
                        }
                    }
                    final Bundle bundle = new Bundle();
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("+");
                    sb3.append(this.codeField.getText());
                    sb3.append(" ");
                    sb3.append(this.phoneField.getText());
                    bundle.putString("phone", sb3.toString());
                    try {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("+");
                        sb4.append(PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()));
                        sb4.append(" ");
                        sb4.append(PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
                        bundle.putString("ephone", sb4.toString());
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("+");
                        sb5.append(stripExceptNumbers);
                        bundle.putString("ephone", sb5.toString());
                    }
                    bundle.putString("phoneFormated", stripExceptNumbers);
                    this.nextPressed = true;
                    LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(LoginActivity.this.currentAccount).sendRequest(tl_auth_sendCode, new _$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX_vOjRSCgCsccc(this, bundle, tl_auth_sendCode), 27));
                }
            }
        }
        
        public void onNothingSelected(final AdapterView<?> adapterView) {
        }
        
        @Override
        public void onShow() {
            super.onShow();
            this.fillNumber();
            final CheckBoxCell checkBoxCell = this.checkBoxCell;
            if (checkBoxCell != null) {
                checkBoxCell.setChecked(LoginActivity.this.syncContacts, false);
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$LoginActivity$PhoneView$1Ok40Ao47pjEDI9trMBSQxPJ99s(this), 100L);
        }
        
        @Override
        public void restoreStateParams(final Bundle bundle) {
            final String string = bundle.getString("phoneview_code");
            if (string != null) {
                this.codeField.setText((CharSequence)string);
            }
            final String string2 = bundle.getString("phoneview_phone");
            if (string2 != null) {
                this.phoneField.setText((CharSequence)string2);
            }
        }
        
        @Override
        public void saveStateParams(final Bundle bundle) {
            final String string = this.codeField.getText().toString();
            if (string.length() != 0) {
                bundle.putString("phoneview_code", string);
            }
            final String string2 = this.phoneField.getText().toString();
            if (string2.length() != 0) {
                bundle.putString("phoneview_phone", string2);
            }
        }
        
        public void selectCountry(String replace, String s) {
            if (this.countriesArray.indexOf(replace) != -1) {
                this.ignoreOnTextChange = true;
                s = this.countriesMap.get(replace);
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
    
    private class ProgressView extends View
    {
        private Paint paint;
        private Paint paint2;
        private float progress;
        
        public ProgressView(final Context context) {
            super(context);
            this.paint = new Paint();
            this.paint2 = new Paint();
            this.paint.setColor(Theme.getColor("login_progressInner"));
            this.paint2.setColor(Theme.getColor("login_progressOuter"));
        }
        
        protected void onDraw(final Canvas canvas) {
            final float n = (float)(int)(this.getMeasuredWidth() * this.progress);
            canvas.drawRect(0.0f, 0.0f, n, (float)this.getMeasuredHeight(), this.paint2);
            canvas.drawRect(n, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), this.paint);
        }
        
        public void setProgress(final float progress) {
            this.progress = progress;
            this.invalidate();
        }
    }
}
