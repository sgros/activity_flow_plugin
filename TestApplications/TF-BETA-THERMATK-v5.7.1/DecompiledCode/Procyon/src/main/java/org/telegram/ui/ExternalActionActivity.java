// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.NotificationCenter;
import android.view.View$OnClickListener;
import android.view.View$OnTouchListener;
import android.widget.FrameLayout;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.drawable.Drawable;
import android.graphics.Shader$TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.widget.FrameLayout$LayoutParams;
import android.widget.RelativeLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.ApplicationLoader;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.ViewGroup$LayoutParams;
import android.os.Build$VERSION;
import android.widget.RelativeLayout$LayoutParams;
import android.view.MotionEvent;
import org.telegram.messenger.MessagesController;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import android.content.DialogInterface;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface$OnCancelListener;
import android.text.TextUtils;
import org.telegram.tgnet.TLRPC;
import android.content.DialogInterface$OnDismissListener;
import org.telegram.ui.Components.AlertsCreator;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.PasscodeView;
import android.content.Intent;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import android.view.View;
import org.telegram.ui.ActionBar.BaseFragment;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.app.Activity;

public class ExternalActionActivity extends Activity implements ActionBarLayoutDelegate
{
    private static ArrayList<BaseFragment> layerFragmentsStack;
    private static ArrayList<BaseFragment> mainFragmentsStack;
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private boolean finished;
    private ActionBarLayout layersActionBarLayout;
    private Runnable lockRunnable;
    private Intent passcodeSaveIntent;
    private int passcodeSaveIntentAccount;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private int passcodeSaveIntentState;
    private PasscodeView passcodeView;
    
    static {
        ExternalActionActivity.mainFragmentsStack = new ArrayList<BaseFragment>();
        ExternalActionActivity.layerFragmentsStack = new ArrayList<BaseFragment>();
    }
    
    private boolean handleIntent(final Intent intent, final boolean b, final boolean b2, final boolean b3, final int n, int intExtra) {
        if (!b3 && (AndroidUtilities.needShowPasscode(true) || SharedConfig.isWaitingForPasscodeEnter)) {
            this.showPasscodeActivity();
            this.passcodeSaveIntent = intent;
            this.passcodeSaveIntentIsNew = b;
            this.passcodeSaveIntentIsRestore = b2;
            this.passcodeSaveIntentAccount = n;
            this.passcodeSaveIntentState = intExtra;
            UserConfig.getInstance(n).saveConfig(false);
            return false;
        }
        if ("org.telegram.passport.AUTHORIZE".equals(intent.getAction())) {
            if (intExtra == 0) {
                final int activatedAccountsCount = UserConfig.getActivatedAccountsCount();
                if (activatedAccountsCount == 0) {
                    this.passcodeSaveIntent = intent;
                    this.passcodeSaveIntentIsNew = b;
                    this.passcodeSaveIntentIsRestore = b2;
                    this.passcodeSaveIntentAccount = n;
                    this.passcodeSaveIntentState = intExtra;
                    final LoginActivity loginActivity = new LoginActivity();
                    if (AndroidUtilities.isTablet()) {
                        this.layersActionBarLayout.addFragmentToStack(loginActivity);
                    }
                    else {
                        this.actionBarLayout.addFragmentToStack(loginActivity);
                    }
                    if (!AndroidUtilities.isTablet()) {
                        this.backgroundTablet.setVisibility(8);
                    }
                    this.actionBarLayout.showLastFragment();
                    if (AndroidUtilities.isTablet()) {
                        this.layersActionBarLayout.showLastFragment();
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("PleaseLoginPassport", 2131560458));
                    builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    builder.show();
                    return true;
                }
                if (activatedAccountsCount >= 2) {
                    final AlertDialog accountSelectDialog = AlertsCreator.createAccountSelectDialog(this, (AlertsCreator.AccountSelectDelegate)new _$$Lambda$ExternalActionActivity$7MenW_e7ZVDhaPYTW6k7H5_jjBA(this, n, intent, b, b2, b3));
                    accountSelectDialog.show();
                    accountSelectDialog.setCanceledOnTouchOutside(false);
                    accountSelectDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$ExternalActionActivity$HPinU7ZiWQ3pid8nvHDiLL5fIOA(this));
                    return true;
                }
            }
            intExtra = intent.getIntExtra("bot_id", 0);
            final String stringExtra = intent.getStringExtra("nonce");
            final String stringExtra2 = intent.getStringExtra("payload");
            final TLRPC.TL_account_getAuthorizationForm tl_account_getAuthorizationForm = new TLRPC.TL_account_getAuthorizationForm();
            tl_account_getAuthorizationForm.bot_id = intExtra;
            tl_account_getAuthorizationForm.scope = intent.getStringExtra("scope");
            tl_account_getAuthorizationForm.public_key = intent.getStringExtra("public_key");
            if (intExtra == 0 || (TextUtils.isEmpty((CharSequence)stringExtra2) && TextUtils.isEmpty((CharSequence)stringExtra)) || TextUtils.isEmpty((CharSequence)tl_account_getAuthorizationForm.scope) || TextUtils.isEmpty((CharSequence)tl_account_getAuthorizationForm.public_key)) {
                this.finish();
                return false;
            }
            final int[] array = { 0 };
            final AlertDialog alertDialog = new AlertDialog((Context)this, 3);
            alertDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ExternalActionActivity$JSGDZffGA_C8G3znC_C8fmn13dg(n, array));
            alertDialog.show();
            array[0] = ConnectionsManager.getInstance(n).sendRequest(tl_account_getAuthorizationForm, new _$$Lambda$ExternalActionActivity$7IhkCSknxlX6e3UV6VR8IaZyD4g(this, array, n, alertDialog, tl_account_getAuthorizationForm, stringExtra2, stringExtra), 10);
        }
        else {
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.addFragmentToStack(new CacheControlActivity());
                }
            }
            else if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                this.actionBarLayout.addFragmentToStack(new CacheControlActivity());
            }
            if (!AndroidUtilities.isTablet()) {
                this.backgroundTablet.setVisibility(8);
            }
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
            }
            intent.setAction((String)null);
        }
        return false;
    }
    
    private void onFinish() {
        if (this.finished) {
            return;
        }
        final Runnable lockRunnable = this.lockRunnable;
        if (lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(lockRunnable);
            this.lockRunnable = null;
        }
        this.finished = true;
    }
    
    private void onPasscodePause() {
        final Runnable lockRunnable = this.lockRunnable;
        if (lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(lockRunnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
            this.lockRunnable = new Runnable() {
                @Override
                public void run() {
                    if (ExternalActionActivity.this.lockRunnable == this) {
                        if (AndroidUtilities.needShowPasscode(true)) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("lock app");
                            }
                            ExternalActionActivity.this.showPasscodeActivity();
                        }
                        else if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("didn't pass lock check");
                        }
                        ExternalActionActivity.this.lockRunnable = null;
                    }
                }
            };
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000L);
            }
            else {
                final int autoLockIn = SharedConfig.autoLockIn;
                if (autoLockIn != 0) {
                    AndroidUtilities.runOnUIThread(this.lockRunnable, autoLockIn * 1000L + 1000L);
                }
            }
        }
        else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }
    
    private void onPasscodeResume() {
        final Runnable lockRunnable = this.lockRunnable;
        if (lockRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(lockRunnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            this.showPasscodeActivity();
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
        }
    }
    
    private void showPasscodeActivity() {
        if (this.passcodeView == null) {
            return;
        }
        SharedConfig.appLocked = true;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
        }
        else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
        }
        else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        this.passcodeView.onShow();
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.passcodeView.setDelegate((PasscodeView.PasscodeViewDelegate)new _$$Lambda$ExternalActionActivity$gW5CjfzBu63uWJgg9FkpT1P0Byc(this));
    }
    
    public void fixLayout() {
        if (!AndroidUtilities.isTablet()) {
            return;
        }
        final ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (actionBarLayout == null) {
            return;
        }
        actionBarLayout.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ExternalActionActivity.this.needLayout();
                if (ExternalActionActivity.this.actionBarLayout != null) {
                    ExternalActionActivity.this.actionBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                }
            }
        });
    }
    
    public boolean needAddFragmentToStack(final BaseFragment baseFragment, final ActionBarLayout actionBarLayout) {
        return true;
    }
    
    public boolean needCloseLastFragment(final ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            if (actionBarLayout == this.actionBarLayout && actionBarLayout.fragmentsStack.size() <= 1) {
                this.onFinish();
                this.finish();
                return false;
            }
            if (actionBarLayout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                this.onFinish();
                this.finish();
                return false;
            }
        }
        else if (actionBarLayout.fragmentsStack.size() <= 1) {
            this.onFinish();
            this.finish();
            return false;
        }
        return true;
    }
    
    public void needLayout() {
        if (AndroidUtilities.isTablet()) {
            final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.layersActionBarLayout.getLayoutParams();
            layoutParams.leftMargin = (AndroidUtilities.displaySize.x - layoutParams.width) / 2;
            int statusBarHeight;
            if (Build$VERSION.SDK_INT >= 21) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight = 0;
            }
            layoutParams.topMargin = statusBarHeight + (AndroidUtilities.displaySize.y - layoutParams.height - statusBarHeight) / 2;
            this.layersActionBarLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            if (AndroidUtilities.isSmallTablet() && this.getResources().getConfiguration().orientation != 2) {
                final RelativeLayout$LayoutParams layoutParams2 = (RelativeLayout$LayoutParams)this.actionBarLayout.getLayoutParams();
                layoutParams2.width = -1;
                layoutParams2.height = -1;
                this.actionBarLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            else {
                int dp;
                if ((dp = AndroidUtilities.displaySize.x / 100 * 35) < AndroidUtilities.dp(320.0f)) {
                    dp = AndroidUtilities.dp(320.0f);
                }
                final RelativeLayout$LayoutParams layoutParams3 = (RelativeLayout$LayoutParams)this.actionBarLayout.getLayoutParams();
                layoutParams3.width = dp;
                layoutParams3.height = -1;
                this.actionBarLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
                if (AndroidUtilities.isSmallTablet() && this.actionBarLayout.fragmentsStack.size() == 2) {
                    this.actionBarLayout.fragmentsStack.get(1).onPause();
                    this.actionBarLayout.fragmentsStack.remove(1);
                    this.actionBarLayout.showLastFragment();
                }
            }
        }
    }
    
    public boolean needPresentFragment(final BaseFragment baseFragment, final boolean b, final boolean b2, final ActionBarLayout actionBarLayout) {
        return true;
    }
    
    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            this.finish();
            return;
        }
        if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        }
        else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        }
        else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
                this.layersActionBarLayout.onBackPressed();
            }
            else {
                this.actionBarLayout.onBackPressed();
            }
        }
        else {
            this.actionBarLayout.onBackPressed();
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        AndroidUtilities.checkDisplaySize((Context)this, configuration);
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    protected void onCreate(final Bundle bundle) {
        ApplicationLoader.postInitApplication();
        this.requestWindowFeature(1);
        this.setTheme(2131624206);
        this.getWindow().setBackgroundDrawableResource(2131165891);
        if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
            try {
                this.getWindow().setFlags(8192, 8192);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        super.onCreate(bundle);
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
        }
        final int identifier = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            AndroidUtilities.statusBarHeight = this.getResources().getDimensionPixelSize(identifier);
        }
        Theme.createDialogsResources((Context)this);
        Theme.createChatResources((Context)this, false);
        this.actionBarLayout = new ActionBarLayout((Context)this);
        (this.drawerLayoutContainer = new DrawerLayoutContainer((Context)this)).setAllowOpenDrawer(false, false);
        this.setContentView((View)this.drawerLayoutContainer, new ViewGroup$LayoutParams(-1, -1));
        if (AndroidUtilities.isTablet()) {
            this.getWindow().setSoftInputMode(16);
            final RelativeLayout relativeLayout = new RelativeLayout((Context)this);
            this.drawerLayoutContainer.addView((View)relativeLayout);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)relativeLayout.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -1;
            relativeLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.backgroundTablet = new View((Context)this);
            final BitmapDrawable backgroundDrawable = (BitmapDrawable)this.getResources().getDrawable(2131165338);
            final Shader$TileMode repeat = Shader$TileMode.REPEAT;
            backgroundDrawable.setTileModeXY(repeat, repeat);
            this.backgroundTablet.setBackgroundDrawable((Drawable)backgroundDrawable);
            relativeLayout.addView(this.backgroundTablet, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -1));
            relativeLayout.addView((View)this.actionBarLayout, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -1));
            final FrameLayout backgroundView = new FrameLayout((Context)this);
            backgroundView.setBackgroundColor(2130706432);
            relativeLayout.addView((View)backgroundView, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -1));
            backgroundView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$ExternalActionActivity$uCjV_BmhLs5mtl2FVWuHvttAbEQ(this));
            backgroundView.setOnClickListener((View$OnClickListener)_$$Lambda$ExternalActionActivity$u8HNxR8AzNk3ipgQhpw5QlDqvFw.INSTANCE);
            (this.layersActionBarLayout = new ActionBarLayout((Context)this)).setRemoveActionBarExtraHeight(true);
            this.layersActionBarLayout.setBackgroundView((View)backgroundView);
            this.layersActionBarLayout.setUseAlphaAnimations(true);
            this.layersActionBarLayout.setBackgroundResource(2131165322);
            final ActionBarLayout layersActionBarLayout = this.layersActionBarLayout;
            int n;
            if (AndroidUtilities.isSmallTablet()) {
                n = 528;
            }
            else {
                n = 700;
            }
            relativeLayout.addView((View)layersActionBarLayout, (ViewGroup$LayoutParams)LayoutHelper.createRelative(530, n));
            this.layersActionBarLayout.init(ExternalActionActivity.layerFragmentsStack);
            this.layersActionBarLayout.setDelegate((ActionBarLayout.ActionBarLayoutDelegate)this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        }
        else {
            final RelativeLayout relativeLayout2 = new RelativeLayout((Context)this);
            this.drawerLayoutContainer.addView((View)relativeLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            this.backgroundTablet = new View((Context)this);
            final BitmapDrawable backgroundDrawable2 = (BitmapDrawable)this.getResources().getDrawable(2131165338);
            final Shader$TileMode repeat2 = Shader$TileMode.REPEAT;
            backgroundDrawable2.setTileModeXY(repeat2, repeat2);
            this.backgroundTablet.setBackgroundDrawable((Drawable)backgroundDrawable2);
            relativeLayout2.addView(this.backgroundTablet, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -1));
            relativeLayout2.addView((View)this.actionBarLayout, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -1));
        }
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.init(ExternalActionActivity.mainFragmentsStack);
        this.actionBarLayout.setDelegate((ActionBarLayout.ActionBarLayoutDelegate)this);
        this.passcodeView = new PasscodeView((Context)this);
        this.drawerLayoutContainer.addView((View)this.passcodeView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
        this.actionBarLayout.removeAllFragments();
        final ActionBarLayout layersActionBarLayout2 = this.layersActionBarLayout;
        if (layersActionBarLayout2 != null) {
            layersActionBarLayout2.removeAllFragments();
        }
        this.handleIntent(this.getIntent(), false, bundle != null, false, UserConfig.selectedAccount, 0);
        this.needLayout();
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.onFinish();
    }
    
    public void onFinishLogin() {
        this.handleIntent(this.passcodeSaveIntent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, this.passcodeSaveIntentAccount, this.passcodeSaveIntentState);
        this.actionBarLayout.removeAllFragments();
        final ActionBarLayout layersActionBarLayout = this.layersActionBarLayout;
        if (layersActionBarLayout != null) {
            layersActionBarLayout.removeAllFragments();
        }
        final View backgroundTablet = this.backgroundTablet;
        if (backgroundTablet != null) {
            backgroundTablet.setVisibility(0);
        }
    }
    
    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onLowMemory();
        }
    }
    
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.handleIntent(intent, true, false, false, UserConfig.selectedAccount, 0);
    }
    
    protected void onPause() {
        super.onPause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onPause();
        }
        ApplicationLoader.externalInterfacePaused = true;
        this.onPasscodePause();
        final PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null) {
            passcodeView.onPause();
        }
    }
    
    public boolean onPreIme() {
        return false;
    }
    
    public void onRebuildAllFragments(final ActionBarLayout actionBarLayout, final boolean b) {
        if (AndroidUtilities.isTablet() && actionBarLayout == this.layersActionBarLayout) {
            this.actionBarLayout.rebuildAllFragmentViews(b, b);
        }
    }
    
    protected void onResume() {
        super.onResume();
        this.actionBarLayout.onResume();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onResume();
        }
        ApplicationLoader.externalInterfacePaused = false;
        this.onPasscodeResume();
        if (this.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.onResume();
            }
        }
        else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.dismissDialogs();
            }
            this.passcodeView.onResume();
        }
    }
    
    public void presentFragment(final BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment);
    }
    
    public boolean presentFragment(final BaseFragment baseFragment, final boolean b, final boolean b2) {
        return this.actionBarLayout.presentFragment(baseFragment, b, b2, true, false);
    }
    
    public void switchToAccount(final int selectedAccount) {
        final int selectedAccount2 = UserConfig.selectedAccount;
        if (selectedAccount == selectedAccount2) {
            return;
        }
        ConnectionsManager.getInstance(selectedAccount2).setAppPaused(true, false);
        UserConfig.selectedAccount = selectedAccount;
        UserConfig.getInstance(0).saveConfig(false);
        if (!ApplicationLoader.mainInterfacePaused) {
            ConnectionsManager.getInstance(UserConfig.selectedAccount).setAppPaused(false, false);
        }
    }
}
