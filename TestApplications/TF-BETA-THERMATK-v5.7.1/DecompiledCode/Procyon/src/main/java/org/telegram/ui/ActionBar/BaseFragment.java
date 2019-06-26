// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.os.Build$VERSION;
import android.animation.AnimatorSet;
import android.content.res.Configuration;
import android.text.TextUtils;
import org.telegram.messenger.ApplicationLoader;
import android.view.accessibility.AccessibilityManager;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import android.app.Activity;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.MessagesController;
import java.util.ArrayList;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.AccountInstance;
import android.view.Menu;
import android.content.Context;
import org.telegram.messenger.FileLog;
import android.view.ViewGroup;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserConfig;
import android.app.Dialog;
import android.view.View;
import android.os.Bundle;

public class BaseFragment
{
    protected ActionBar actionBar;
    protected Bundle arguments;
    protected int classGuid;
    protected int currentAccount;
    private boolean finishing;
    protected View fragmentView;
    protected boolean hasOwnBackground;
    protected boolean inPreviewMode;
    private boolean isFinished;
    protected ActionBarLayout parentLayout;
    protected boolean swipeBackEnabled;
    protected Dialog visibleDialog;
    
    public BaseFragment() {
        this.currentAccount = UserConfig.selectedAccount;
        this.swipeBackEnabled = true;
        this.hasOwnBackground = false;
        this.classGuid = ConnectionsManager.generateClassGuid();
    }
    
    public BaseFragment(final Bundle arguments) {
        this.currentAccount = UserConfig.selectedAccount;
        this.swipeBackEnabled = true;
        this.hasOwnBackground = false;
        this.arguments = arguments;
        this.classGuid = ConnectionsManager.generateClassGuid();
    }
    
    public boolean canBeginSlide() {
        return true;
    }
    
    protected void clearViews() {
        final View fragmentView = this.fragmentView;
        if (fragmentView != null) {
            final ViewGroup viewGroup = (ViewGroup)fragmentView.getParent();
            if (viewGroup != null) {
                try {
                    this.onRemoveFromParent();
                    viewGroup.removeView(this.fragmentView);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            this.fragmentView = null;
        }
        final ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            final ViewGroup viewGroup2 = (ViewGroup)actionBar.getParent();
            if (viewGroup2 != null) {
                try {
                    viewGroup2.removeView((View)this.actionBar);
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            this.actionBar = null;
        }
        this.parentLayout = null;
    }
    
    protected ActionBar createActionBar(final Context context) {
        final ActionBar actionBar = new ActionBar(context);
        actionBar.setBackgroundColor(Theme.getColor("actionBarDefault"));
        actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), false);
        actionBar.setItemsBackgroundColor(Theme.getColor("actionBarActionModeDefaultSelector"), true);
        actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon"), false);
        actionBar.setItemsColor(Theme.getColor("actionBarActionModeDefaultIcon"), true);
        if (this.inPreviewMode) {
            actionBar.setOccupyStatusBar(false);
        }
        return actionBar;
    }
    
    public View createView(final Context context) {
        return null;
    }
    
    public void dismissCurrentDialig() {
        final Dialog visibleDialog = this.visibleDialog;
        if (visibleDialog == null) {
            return;
        }
        try {
            visibleDialog.dismiss();
            this.visibleDialog = null;
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public boolean dismissDialogOnPause(final Dialog dialog) {
        return true;
    }
    
    public boolean extendActionMode(final Menu menu) {
        return false;
    }
    
    public void finishFragment() {
        this.finishFragment(true);
    }
    
    public void finishFragment(final boolean b) {
        if (!this.isFinished) {
            final ActionBarLayout parentLayout = this.parentLayout;
            if (parentLayout != null) {
                this.finishing = true;
                parentLayout.closeLastFragment(b);
            }
        }
    }
    
    public void finishPreviewFragment() {
        this.parentLayout.finishPreviewFragment();
    }
    
    protected AccountInstance getAccountInstance() {
        return AccountInstance.getInstance(this.currentAccount);
    }
    
    public ActionBar getActionBar() {
        return this.actionBar;
    }
    
    public Bundle getArguments() {
        return this.arguments;
    }
    
    protected ConnectionsManager getConnectionsManager() {
        return this.getAccountInstance().getConnectionsManager();
    }
    
    protected ContactsController getContactsController() {
        return this.getAccountInstance().getContactsController();
    }
    
    public int getCurrentAccount() {
        return this.currentAccount;
    }
    
    protected DataQuery getDataQuery() {
        return this.getAccountInstance().getDataQuery();
    }
    
    public BaseFragment getFragmentForAlert(final int n) {
        final ActionBarLayout parentLayout = this.parentLayout;
        if (parentLayout != null && parentLayout.fragmentsStack.size() > n + 1) {
            final ArrayList<BaseFragment> fragmentsStack = this.parentLayout.fragmentsStack;
            return fragmentsStack.get(fragmentsStack.size() - 2 - n);
        }
        return this;
    }
    
    public View getFragmentView() {
        return this.fragmentView;
    }
    
    protected MessagesController getMessagesController() {
        return this.getAccountInstance().getMessagesController();
    }
    
    public NotificationCenter getNotificationCenter() {
        return this.getAccountInstance().getNotificationCenter();
    }
    
    protected NotificationsController getNotificationsController() {
        return this.getAccountInstance().getNotificationsController();
    }
    
    public Activity getParentActivity() {
        final ActionBarLayout parentLayout = this.parentLayout;
        if (parentLayout != null) {
            return parentLayout.parentActivity;
        }
        return null;
    }
    
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[0];
    }
    
    public UserConfig getUserConfig() {
        return this.getAccountInstance().getUserConfig();
    }
    
    public Dialog getVisibleDialog() {
        return this.visibleDialog;
    }
    
    protected boolean isFinishing() {
        return this.finishing;
    }
    
    public void movePreviewFragment(final float n) {
        this.parentLayout.movePreviewFragment(n);
    }
    
    public boolean needDelayOpenAnimation() {
        return false;
    }
    
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
    }
    
    public boolean onBackPressed() {
        return true;
    }
    
    protected void onBecomeFullyHidden() {
    }
    
    protected void onBecomeFullyVisible() {
        if (((AccessibilityManager)ApplicationLoader.applicationContext.getSystemService("accessibility")).isEnabled()) {
            final ActionBar actionBar = this.getActionBar();
            if (actionBar != null) {
                final String title = actionBar.getTitle();
                if (!TextUtils.isEmpty((CharSequence)title)) {
                    this.setParentActivityTitle(title);
                }
            }
        }
    }
    
    public void onBeginSlide() {
        try {
            if (this.visibleDialog != null && this.visibleDialog.isShowing()) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        final ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.onPause();
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
    }
    
    protected AnimatorSet onCustomTransitionAnimation(final boolean b, final Runnable runnable) {
        return null;
    }
    
    protected void onDialogDismiss(final Dialog dialog) {
    }
    
    public boolean onFragmentCreate() {
        return true;
    }
    
    public void onFragmentDestroy() {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequestsForGuid(this.classGuid);
        this.isFinished = true;
        final ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.setEnabled(false);
        }
    }
    
    public void onLowMemory() {
    }
    
    public void onPause() {
        final ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.onPause();
        }
        try {
            if (this.visibleDialog != null && this.visibleDialog.isShowing() && this.dismissDialogOnPause(this.visibleDialog)) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    protected void onRemoveFromParent() {
    }
    
    public void onRequestPermissionsResultFragment(final int n, final String[] array, final int[] array2) {
    }
    
    public void onResume() {
    }
    
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
    }
    
    protected void onTransitionAnimationStart(final boolean b, final boolean b2) {
    }
    
    public boolean presentFragment(final BaseFragment baseFragment) {
        final ActionBarLayout parentLayout = this.parentLayout;
        return parentLayout != null && parentLayout.presentFragment(baseFragment);
    }
    
    public boolean presentFragment(final BaseFragment baseFragment, final boolean b) {
        final ActionBarLayout parentLayout = this.parentLayout;
        return parentLayout != null && parentLayout.presentFragment(baseFragment, b);
    }
    
    public boolean presentFragment(final BaseFragment baseFragment, final boolean b, final boolean b2) {
        final ActionBarLayout parentLayout = this.parentLayout;
        return parentLayout != null && parentLayout.presentFragment(baseFragment, b, b2, true, false);
    }
    
    public boolean presentFragmentAsPreview(final BaseFragment baseFragment) {
        final ActionBarLayout parentLayout = this.parentLayout;
        return parentLayout != null && parentLayout.presentFragmentAsPreview(baseFragment);
    }
    
    public void removeSelfFromStack() {
        if (!this.isFinished) {
            final ActionBarLayout parentLayout = this.parentLayout;
            if (parentLayout != null) {
                parentLayout.removeFragmentFromStack(this);
            }
        }
    }
    
    public void restoreSelfArgs(final Bundle bundle) {
    }
    
    public void saveSelfArgs(final Bundle bundle) {
    }
    
    public void setCurrentAccount(final int currentAccount) {
        if (this.fragmentView == null) {
            this.currentAccount = currentAccount;
            return;
        }
        throw new IllegalStateException("trying to set current account when fragment UI already created");
    }
    
    protected void setInPreviewMode(final boolean inPreviewMode) {
        this.inPreviewMode = inPreviewMode;
        final ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            final boolean inPreviewMode2 = this.inPreviewMode;
            boolean occupyStatusBar = false;
            if (inPreviewMode2) {
                actionBar.setOccupyStatusBar(false);
            }
            else {
                if (Build$VERSION.SDK_INT >= 21) {
                    occupyStatusBar = true;
                }
                actionBar.setOccupyStatusBar(occupyStatusBar);
            }
        }
    }
    
    protected void setParentActivityTitle(final CharSequence title) {
        final Activity parentActivity = this.getParentActivity();
        if (parentActivity != null) {
            parentActivity.setTitle(title);
        }
    }
    
    protected void setParentLayout(ActionBarLayout parentLayout) {
        if (this.parentLayout != parentLayout) {
            this.parentLayout = parentLayout;
            final View fragmentView = this.fragmentView;
            if (fragmentView != null) {
                final ViewGroup viewGroup = (ViewGroup)fragmentView.getParent();
                if (viewGroup != null) {
                    try {
                        this.onRemoveFromParent();
                        viewGroup.removeView(this.fragmentView);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                parentLayout = this.parentLayout;
                if (parentLayout != null && parentLayout.getContext() != this.fragmentView.getContext()) {
                    this.fragmentView = null;
                }
            }
            if (this.actionBar != null) {
                parentLayout = this.parentLayout;
                final boolean b = parentLayout != null && parentLayout.getContext() != this.actionBar.getContext();
                if (this.actionBar.getAddToContainer() || b) {
                    final ViewGroup viewGroup2 = (ViewGroup)this.actionBar.getParent();
                    if (viewGroup2 != null) {
                        try {
                            viewGroup2.removeView((View)this.actionBar);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                    }
                }
                if (b) {
                    this.actionBar = null;
                }
            }
            parentLayout = this.parentLayout;
            if (parentLayout != null && this.actionBar == null) {
                this.actionBar = this.createActionBar(parentLayout.getContext());
                this.actionBar.parentFragment = this;
            }
        }
    }
    
    public void setVisibleDialog(final Dialog visibleDialog) {
        this.visibleDialog = visibleDialog;
    }
    
    public Dialog showDialog(final Dialog dialog) {
        return this.showDialog(dialog, false, null);
    }
    
    public Dialog showDialog(final Dialog dialog, final DialogInterface$OnDismissListener dialogInterface$OnDismissListener) {
        return this.showDialog(dialog, false, dialogInterface$OnDismissListener);
    }
    
    public Dialog showDialog(Dialog visibleDialog, final boolean b, final DialogInterface$OnDismissListener dialogInterface$OnDismissListener) {
        if (visibleDialog != null) {
            final ActionBarLayout parentLayout = this.parentLayout;
            if (parentLayout != null && !parentLayout.animationInProgress && !parentLayout.startedTracking) {
                if (b || !parentLayout.checkTransitionAnimation()) {
                    try {
                        if (this.visibleDialog != null) {
                            this.visibleDialog.dismiss();
                            this.visibleDialog = null;
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    try {
                        (this.visibleDialog = visibleDialog).setCanceledOnTouchOutside(true);
                        visibleDialog = this.visibleDialog;
                        visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM(this, dialogInterface$OnDismissListener));
                        this.visibleDialog.show();
                        visibleDialog = this.visibleDialog;
                        return visibleDialog;
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
            }
        }
        return null;
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        final ActionBarLayout parentLayout = this.parentLayout;
        if (parentLayout != null) {
            parentLayout.startActivityForResult(intent, n);
        }
    }
}
