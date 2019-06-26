// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import java.util.HashSet;
import java.lang.reflect.Field;
import org.telegram.tgnet.SerializedData;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import java.util.Set;
import java.util.LinkedHashSet;
import android.content.pm.PackageInfo;
import java.util.Locale;
import android.os.Build;
import android.text.method.MovementMethod;
import android.view.ViewGroup;
import android.text.TextUtils;
import org.telegram.PhoneFormat.PhoneFormat;
import android.os.Bundle;
import org.telegram.messenger.DataQuery;
import android.content.res.Configuration;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.browser.Browser;
import android.os.Parcelable;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Cells.SettingsSearchCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.ImageUpdater$ImageUpdaterDelegate$_CC;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.StateListAnimator;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.text.TextUtils$TruncateAt;
import android.graphics.Canvas;
import android.graphics.Paint;
import org.telegram.messenger.FileLog;
import android.widget.Toast;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import android.content.DialogInterface;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.UserObject;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import android.app.Activity;
import android.app.Dialog;
import android.view.View$OnClickListener;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCell;
import org.telegram.messenger.BuildVars;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Cells.HeaderCell;
import android.widget.LinearLayout;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.messenger.Utilities;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.LocaleController;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.AccelerateInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.ViewTreeObserver$OnPreDrawListener;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessageObject;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.widget.TextView;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.RadialProgressView;
import android.view.View;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;
import android.animation.AnimatorSet;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class SettingsActivity extends BaseFragment implements NotificationCenterDelegate, ImageUpdaterDelegate
{
    private static final int edit_name = 1;
    private static final int logout = 2;
    private static final int search_button = 3;
    private TLRPC.FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private TLRPC.FileLocation avatarBig;
    private FrameLayout avatarContainer;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private int bioRow;
    private int chatRow;
    private int dataRow;
    private EmptyTextProgressView emptyView;
    private int extraHeight;
    private View extraHeightView;
    private int helpRow;
    private ImageUpdater imageUpdater;
    private int languageRow;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private TextView nameTextView;
    private int notificationRow;
    private int numberRow;
    private int numberSectionRow;
    private TextView onlineTextView;
    private ActionBarMenuItem otherItem;
    private int overscrollRow;
    private int privacyRow;
    private PhotoViewer.PhotoViewerProvider provider;
    private int rowCount;
    private SearchAdapter searchAdapter;
    private int settingsSectionRow;
    private int settingsSectionRow2;
    private View shadowView;
    private TLRPC.UserFull userInfo;
    private int usernameRow;
    private int versionRow;
    private ImageView writeButton;
    private AnimatorSet writeButtonAnimation;
    
    public SettingsActivity() {
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
            @Override
            public PlaceProviderObject getPlaceForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, int statusBarHeight, final boolean b) {
                if (fileLocation == null) {
                    return null;
                }
                final TLRPC.User user = MessagesController.getInstance(SettingsActivity.this.currentAccount).getUser(UserConfig.getInstance(SettingsActivity.this.currentAccount).getClientUserId());
                if (user != null) {
                    final TLRPC.UserProfilePhoto photo = user.photo;
                    if (photo != null) {
                        final TLRPC.FileLocation photo_big = photo.photo_big;
                        if (photo_big != null && photo_big.local_id == fileLocation.local_id && photo_big.volume_id == fileLocation.volume_id && photo_big.dc_id == fileLocation.dc_id) {
                            final int[] array = new int[2];
                            SettingsActivity.this.avatarImage.getLocationInWindow(array);
                            final PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                            statusBarHeight = 0;
                            placeProviderObject.viewX = array[0];
                            final int n = array[1];
                            if (Build$VERSION.SDK_INT < 21) {
                                statusBarHeight = AndroidUtilities.statusBarHeight;
                            }
                            placeProviderObject.viewY = n - statusBarHeight;
                            placeProviderObject.parentView = SettingsActivity.this.avatarImage;
                            placeProviderObject.imageReceiver = SettingsActivity.this.avatarImage.getImageReceiver();
                            placeProviderObject.dialogId = UserConfig.getInstance(SettingsActivity.this.currentAccount).getClientUserId();
                            placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                            placeProviderObject.size = -1;
                            placeProviderObject.radius = SettingsActivity.this.avatarImage.getImageReceiver().getRoundRadius();
                            placeProviderObject.scale = SettingsActivity.this.avatarContainer.getScaleX();
                            return placeProviderObject;
                        }
                    }
                }
                return null;
            }
            
            @Override
            public void willHidePhotoViewer() {
                SettingsActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
            }
        };
    }
    
    private void fixLayout() {
        final View fragmentView = super.fragmentView;
        if (fragmentView == null) {
            return;
        }
        fragmentView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
            public boolean onPreDraw() {
                if (SettingsActivity.this.fragmentView != null) {
                    SettingsActivity.this.needLayout();
                    SettingsActivity.this.fragmentView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                }
                return true;
            }
        });
    }
    
    private void needLayout() {
        final boolean occupyStatusBar = super.actionBar.getOccupyStatusBar();
        final int n = 0;
        int statusBarHeight;
        if (occupyStatusBar) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final int n2 = statusBarHeight + ActionBar.getCurrentActionBarHeight();
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)listView.getLayoutParams();
            if (layoutParams.topMargin != n2) {
                layoutParams.topMargin = n2;
                this.listView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                this.extraHeightView.setTranslationY((float)n2);
            }
            final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.emptyView.getLayoutParams();
            if (layoutParams2.topMargin != n2) {
                layoutParams2.topMargin = n2;
                this.emptyView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
        }
        final FrameLayout avatarContainer = this.avatarContainer;
        if (avatarContainer != null) {
            int extraHeight;
            if (avatarContainer.getVisibility() == 0) {
                extraHeight = this.extraHeight;
            }
            else {
                extraHeight = 0;
            }
            final float scaleY = extraHeight / (float)AndroidUtilities.dp(88.0f);
            this.extraHeightView.setScaleY(scaleY);
            this.shadowView.setTranslationY((float)(n2 + extraHeight));
            final ImageView writeButton = this.writeButton;
            int statusBarHeight2;
            if (super.actionBar.getOccupyStatusBar()) {
                statusBarHeight2 = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight2 = 0;
            }
            writeButton.setTranslationY((float)(statusBarHeight2 + ActionBar.getCurrentActionBarHeight() + extraHeight - AndroidUtilities.dp(29.5f)));
            final boolean b = scaleY > 0.2f;
            if (b != (this.writeButton.getTag() == null)) {
                if (b) {
                    this.writeButton.setTag((Object)null);
                    this.writeButton.setVisibility(0);
                }
                else {
                    this.writeButton.setTag((Object)0);
                }
                final AnimatorSet writeButtonAnimation = this.writeButtonAnimation;
                if (writeButtonAnimation != null) {
                    this.writeButtonAnimation = null;
                    writeButtonAnimation.cancel();
                }
                this.writeButtonAnimation = new AnimatorSet();
                if (b) {
                    this.writeButtonAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    this.writeButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "alpha", new float[] { 1.0f }) });
                }
                else {
                    this.writeButtonAnimation.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
                    this.writeButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "scaleX", new float[] { 0.2f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "scaleY", new float[] { 0.2f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "alpha", new float[] { 0.0f }) });
                }
                this.writeButtonAnimation.setDuration(150L);
                this.writeButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator obj) {
                        if (SettingsActivity.this.writeButtonAnimation != null && SettingsActivity.this.writeButtonAnimation.equals(obj)) {
                            final ImageView access$1000 = SettingsActivity.this.writeButton;
                            int visibility;
                            if (b) {
                                visibility = 0;
                            }
                            else {
                                visibility = 8;
                            }
                            access$1000.setVisibility(visibility);
                            SettingsActivity.this.writeButtonAnimation = null;
                        }
                    }
                });
                this.writeButtonAnimation.start();
            }
            final FrameLayout avatarContainer2 = this.avatarContainer;
            final float n3 = (18.0f * scaleY + 42.0f) / 42.0f;
            avatarContainer2.setScaleX(n3);
            this.avatarContainer.setScaleY(n3);
            this.avatarProgressView.setSize(AndroidUtilities.dp(26.0f / this.avatarContainer.getScaleX()));
            this.avatarProgressView.setStrokeWidth(3.0f / this.avatarContainer.getScaleX());
            int statusBarHeight3 = n;
            if (super.actionBar.getOccupyStatusBar()) {
                statusBarHeight3 = AndroidUtilities.statusBarHeight;
            }
            final float n4 = (float)statusBarHeight3;
            final float n5 = ActionBar.getCurrentActionBarHeight() / 2.0f;
            final float density = AndroidUtilities.density;
            final FrameLayout avatarContainer3 = this.avatarContainer;
            final double a = n4 + n5 * (scaleY + 1.0f) - 21.0f * density + density * 27.0f * scaleY;
            avatarContainer3.setTranslationY((float)Math.ceil(a));
            this.nameTextView.setTranslationY((float)Math.floor(a) - (float)Math.ceil(AndroidUtilities.density) + (float)Math.floor(AndroidUtilities.density * 7.0f * scaleY));
            this.onlineTextView.setTranslationY((float)Math.floor(a) + AndroidUtilities.dp(22.0f) + (float)Math.floor(AndroidUtilities.density * 11.0f) * scaleY);
            final TextView nameTextView = this.nameTextView;
            final float n6 = 0.12f * scaleY + 1.0f;
            nameTextView.setScaleX(n6);
            this.nameTextView.setScaleY(n6);
            if (LocaleController.isRTL) {
                this.avatarContainer.setTranslationX(AndroidUtilities.dp(95.0f) * scaleY);
                this.nameTextView.setTranslationX(AndroidUtilities.density * 69.0f * scaleY);
                this.onlineTextView.setTranslationX(AndroidUtilities.density * 69.0f * scaleY);
            }
            else {
                this.avatarContainer.setTranslationX(-AndroidUtilities.dp(47.0f) * scaleY);
                this.nameTextView.setTranslationX(AndroidUtilities.density * -21.0f * scaleY);
                this.onlineTextView.setTranslationX(AndroidUtilities.density * -21.0f * scaleY);
            }
        }
    }
    
    private void sendLogs() {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog alertDialog = new AlertDialog((Context)this.getParentActivity(), 3);
        alertDialog.setCanCacnel(false);
        alertDialog.show();
        Utilities.globalQueue.postRunnable(new _$$Lambda$SettingsActivity$fDRhZsppR_aQC0aOePrWJpUZ93Y(this, alertDialog));
    }
    
    private void showAvatarProgress(final boolean b, final boolean b2) {
        if (this.avatarProgressView == null) {
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
                this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 0.0f }) });
            }
            this.avatarAnimation.setDuration(180L);
            this.avatarAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    SettingsActivity.this.avatarAnimation = null;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    if (SettingsActivity.this.avatarAnimation != null) {
                        if (SettingsActivity.this.avatarProgressView != null) {
                            if (!b) {
                                SettingsActivity.this.avatarProgressView.setVisibility(4);
                            }
                            SettingsActivity.this.avatarAnimation = null;
                        }
                    }
                }
            });
            this.avatarAnimation.start();
        }
        else if (b) {
            this.avatarProgressView.setAlpha(1.0f);
            this.avatarProgressView.setVisibility(0);
        }
        else {
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
        }
    }
    
    private void showHelpAlert() {
        if (this.getParentActivity() == null) {
            return;
        }
        final Activity parentActivity = this.getParentActivity();
        final BottomSheet.Builder builder = new BottomSheet.Builder((Context)parentActivity);
        builder.setApplyTopPadding(false);
        final LinearLayout customView = new LinearLayout((Context)parentActivity);
        customView.setOrientation(1);
        final HeaderCell headerCell = new HeaderCell((Context)parentActivity, true, 23, 15, false);
        headerCell.setHeight(47);
        headerCell.setText(LocaleController.getString("SettingsHelp", 2131560740));
        customView.addView((View)headerCell);
        final LinearLayout linearLayout = new LinearLayout((Context)parentActivity);
        linearLayout.setOrientation(1);
        customView.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        for (int i = 0; i < 6; ++i) {
            if (i < 3 || i > 4 || BuildVars.LOGS_ENABLED) {
                if (i != 5 || BuildVars.DEBUG_VERSION) {
                    final TextCell textCell = new TextCell((Context)parentActivity);
                    String s;
                    if (i != 0) {
                        if (i != 1) {
                            if (i != 2) {
                                if (i != 3) {
                                    if (i != 4) {
                                        s = "Switch Backend";
                                    }
                                    else {
                                        s = LocaleController.getString("DebugClearLogs", 2131559208);
                                    }
                                }
                                else {
                                    s = LocaleController.getString("DebugSendLogs", 2131559221);
                                }
                            }
                            else {
                                s = LocaleController.getString("PrivacyPolicy", 2131560502);
                            }
                        }
                        else {
                            s = LocaleController.getString("TelegramFAQ", 2131560869);
                        }
                    }
                    else {
                        s = LocaleController.getString("AskAQuestion", 2131558706);
                    }
                    textCell.setText(s, (!BuildVars.LOGS_ENABLED && !BuildVars.DEBUG_VERSION) ? (i != 2) : (i != 5));
                    textCell.setTag((Object)i);
                    textCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    linearLayout.addView((View)textCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                    textCell.setOnClickListener((View$OnClickListener)new _$$Lambda$SettingsActivity$z3K3WuiCS2o_E03FhC5NesCF_7c(this, builder));
                }
            }
        }
        builder.setCustomView((View)customView);
        this.showDialog(builder.create());
    }
    
    private void updateUserData() {
        final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId());
        if (user == null) {
            return;
        }
        TLRPC.FileLocation photo_big = null;
        final TLRPC.UserProfilePhoto photo = user.photo;
        if (photo != null) {
            photo_big = photo.photo_big;
        }
        (this.avatarDrawable = new AvatarDrawable(user, true)).setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
        final BackupImageView avatarImage = this.avatarImage;
        if (avatarImage != null) {
            avatarImage.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(photo_big) ^ true, false);
            this.nameTextView.setText((CharSequence)UserObject.getUserName(user));
            this.onlineTextView.setText((CharSequence)LocaleController.getString("Online", 2131560100));
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(photo_big) ^ true, false);
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        super.actionBar.setItemsBackgroundColor(Theme.getColor("avatar_actionBarSelectorBlue"), false);
        super.actionBar.setItemsColor(Theme.getColor("avatar_actionBarIconBlue"), false);
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAddToContainer(false);
        this.extraHeight = 88;
        if (AndroidUtilities.isTablet()) {
            super.actionBar.setOccupyStatusBar(false);
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    SettingsActivity.this.finishFragment();
                }
                else if (n == 1) {
                    SettingsActivity.this.presentFragment(new ChangeNameActivity());
                }
                else if (n == 2) {
                    SettingsActivity.this.presentFragment(new LogoutActivity());
                }
            }
        });
        final ActionBarMenu menu = super.actionBar.createMenu();
        final ActionBarMenuItem setActionBarMenuItemSearchListener = menu.addItem(3, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                if (SettingsActivity.this.otherItem != null) {
                    SettingsActivity.this.otherItem.setVisibility(0);
                }
                SettingsActivity.this.listView.setAdapter(SettingsActivity.this.listAdapter);
                SettingsActivity.this.listView.setEmptyView(null);
                SettingsActivity.this.emptyView.setVisibility(8);
                SettingsActivity.this.avatarContainer.setVisibility(0);
                SettingsActivity.this.writeButton.setVisibility(0);
                SettingsActivity.this.nameTextView.setVisibility(0);
                SettingsActivity.this.onlineTextView.setVisibility(0);
                SettingsActivity.this.extraHeightView.setVisibility(0);
                SettingsActivity.this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                SettingsActivity.this.fragmentView.setTag((Object)"windowBackgroundGray");
                SettingsActivity.this.needLayout();
            }
            
            @Override
            public void onSearchExpand() {
                if (SettingsActivity.this.otherItem != null) {
                    SettingsActivity.this.otherItem.setVisibility(8);
                }
                SettingsActivity.this.searchAdapter.loadFaqWebPage();
                SettingsActivity.this.listView.setAdapter(SettingsActivity.this.searchAdapter);
                SettingsActivity.this.listView.setEmptyView((View)SettingsActivity.this.emptyView);
                SettingsActivity.this.avatarContainer.setVisibility(8);
                SettingsActivity.this.writeButton.setVisibility(8);
                SettingsActivity.this.nameTextView.setVisibility(8);
                SettingsActivity.this.onlineTextView.setVisibility(8);
                SettingsActivity.this.extraHeightView.setVisibility(8);
                SettingsActivity.this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                SettingsActivity.this.fragmentView.setTag((Object)"windowBackgroundWhite");
                SettingsActivity.this.needLayout();
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                SettingsActivity.this.searchAdapter.search(editText.getText().toString().toLowerCase());
            }
        });
        setActionBarMenuItemSearchListener.setContentDescription((CharSequence)LocaleController.getString("SearchInSettings", 2131560653));
        setActionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("SearchInSettings", 2131560653));
        (this.otherItem = menu.addItem(0, 2131165416)).setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        this.otherItem.addSubItem(1, 2131165625, LocaleController.getString("EditName", 2131559326));
        this.otherItem.addSubItem(2, 2131165639, LocaleController.getString("LogOut", 2131559783));
        int firstVisibleItemPosition;
        int top;
        Object tag;
        if (this.listView != null) {
            firstVisibleItemPosition = this.layoutManager.findFirstVisibleItemPosition();
            final View viewByPosition = this.layoutManager.findViewByPosition(firstVisibleItemPosition);
            if (viewByPosition != null) {
                top = viewByPosition.getTop();
            }
            else {
                firstVisibleItemPosition = -1;
                top = 0;
            }
            tag = this.writeButton.getTag();
        }
        else {
            tag = null;
            firstVisibleItemPosition = -1;
            top = 0;
        }
        this.listAdapter = new ListAdapter(context);
        this.searchAdapter = new SearchAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        super.fragmentView.setTag((Object)"windowBackgroundGray");
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context)).setVerticalScrollBarEnabled(false);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        this.listView.setGlowColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$SettingsActivity$iv7dvdiS8exd3Bpi9ZCm9_zKqgo(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new RecyclerListView.OnItemLongClickListener() {
            private int pressCount = 0;
            
            @Override
            public boolean onItemClick(final View view, int n) {
                if (SettingsActivity.this.listView.getAdapter() == SettingsActivity.this.searchAdapter) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)SettingsActivity.this.getParentActivity());
                    builder.setTitle(LocaleController.getString("AppName", 2131558635));
                    builder.setMessage(LocaleController.getString("ClearSearch", 2131559114));
                    builder.setPositiveButton(LocaleController.getString("ClearButton", 2131559102).toUpperCase(), (DialogInterface$OnClickListener)new _$$Lambda$SettingsActivity$5$ugZ8nxlV3bSe_GDAFB06httPzlQ(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    SettingsActivity.this.showDialog(builder.create());
                    return true;
                }
                if (n == SettingsActivity.this.versionRow) {
                    ++this.pressCount;
                    if (this.pressCount < 2 && !BuildVars.DEBUG_PRIVATE_VERSION) {
                        try {
                            Toast.makeText((Context)SettingsActivity.this.getParentActivity(), (CharSequence)"¯\\_(\u30c4)_/¯", 0).show();
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    else {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)SettingsActivity.this.getParentActivity());
                        builder2.setTitle(LocaleController.getString("DebugMenu", 2131559209));
                        final String string = LocaleController.getString("DebugMenuImportContacts", 2131559216);
                        final String string2 = LocaleController.getString("DebugMenuReloadContacts", 2131559218);
                        final String string3 = LocaleController.getString("DebugMenuResetContacts", 2131559219);
                        final String string4 = LocaleController.getString("DebugMenuResetDialogs", 2131559220);
                        String s;
                        if (BuildVars.LOGS_ENABLED) {
                            n = 2131559213;
                            s = "DebugMenuDisableLogs";
                        }
                        else {
                            n = 2131559215;
                            s = "DebugMenuEnableLogs";
                        }
                        final String string5 = LocaleController.getString(s, n);
                        String s2;
                        if (SharedConfig.inappCamera) {
                            n = 2131559212;
                            s2 = "DebugMenuDisableCamera";
                        }
                        else {
                            n = 2131559214;
                            s2 = "DebugMenuEnableCamera";
                        }
                        final String string6 = LocaleController.getString(s2, n);
                        final String string7 = LocaleController.getString("DebugMenuClearMediaCache", 2131559211);
                        final String string8 = LocaleController.getString("DebugMenuCallSettings", 2131559210);
                        String s3;
                        if (BuildVars.DEBUG_PRIVATE_VERSION) {
                            s3 = "Check for app updates";
                        }
                        else {
                            s3 = null;
                        }
                        builder2.setItems(new CharSequence[] { string, string2, string3, string4, string5, string6, string7, string8, null, s3, LocaleController.getString("DebugMenuReadAllDialogs", 2131559217) }, (DialogInterface$OnClickListener)new _$$Lambda$SettingsActivity$5$xAsEgwepb1pw1bhC6d7Q2D8mdcE(this));
                        builder2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                        SettingsActivity.this.showDialog(builder2.create());
                    }
                    return true;
                }
                return false;
            }
        });
        (this.emptyView = new EmptyTextProgressView(context)).showTextView();
        this.emptyView.setTextSize(18);
        this.emptyView.setVisibility(8);
        this.emptyView.setShowAtCenter(true);
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        frameLayout.addView((View)super.actionBar);
        (this.extraHeightView = new View(context)).setPivotY(0.0f);
        this.extraHeightView.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
        frameLayout.addView(this.extraHeightView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 88.0f));
        (this.shadowView = new View(context)).setBackgroundResource(2131165407);
        frameLayout.addView(this.shadowView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f));
        this.avatarContainer = new FrameLayout(context);
        final FrameLayout avatarContainer = this.avatarContainer;
        float pivotX;
        if (LocaleController.isRTL) {
            pivotX = (float)AndroidUtilities.dp(42.0f);
        }
        else {
            pivotX = 0.0f;
        }
        avatarContainer.setPivotX(pivotX);
        this.avatarContainer.setPivotY(0.0f);
        final FrameLayout avatarContainer2 = this.avatarContainer;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        int n3;
        if (LocaleController.isRTL) {
            n3 = 0;
        }
        else {
            n3 = 64;
        }
        final float n4 = (float)n3;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 112;
        }
        else {
            n5 = 0;
        }
        frameLayout.addView((View)avatarContainer2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f, n2 | 0x30, n4, 0.0f, (float)n5, 0.0f));
        this.avatarContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$SettingsActivity$tP6tYHrZMyudHveG_joX5MC_Y2o(this));
        (this.avatarImage = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarImage.setContentDescription((CharSequence)LocaleController.getString("AccDescrProfilePicture", 2131558460));
        this.avatarContainer.addView((View)this.avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f));
        final Paint paint = new Paint(1);
        paint.setColor(1426063360);
        (this.avatarProgressView = new RadialProgressView(context) {
            @Override
            protected void onDraw(final Canvas canvas) {
                if (SettingsActivity.this.avatarImage != null && SettingsActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                    paint.setAlpha((int)(SettingsActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                    canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(21.0f), paint);
                }
                super.onDraw(canvas);
            }
        }).setSize(AndroidUtilities.dp(26.0f));
        this.avatarProgressView.setProgressColor(-1);
        this.avatarContainer.addView((View)this.avatarProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f));
        this.showAvatarProgress(false, false);
        (this.nameTextView = new TextView(context) {
            protected void onMeasure(final int n, final int n2) {
                super.onMeasure(n, n2);
                float pivotX;
                if (LocaleController.isRTL) {
                    pivotX = (float)this.getMeasuredWidth();
                }
                else {
                    pivotX = 0.0f;
                }
                this.setPivotX(pivotX);
            }
        }).setTextColor(Theme.getColor("profile_title"));
        this.nameTextView.setTextSize(1, 18.0f);
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
        this.nameTextView.setPivotY(0.0f);
        final TextView nameTextView2 = this.nameTextView;
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 48.0f;
        }
        else {
            n7 = 118.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 166.0f;
        }
        else {
            n8 = 96.0f;
        }
        frameLayout.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n6 | 0x30, n7, 0.0f, n8, 0.0f));
        (this.onlineTextView = new TextView(context)).setTextColor(Theme.getColor("profile_status"));
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
        int n9;
        if (LocaleController.isRTL) {
            n9 = 5;
        }
        else {
            n9 = 3;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 48.0f;
        }
        else {
            n10 = 118.0f;
        }
        float n11;
        if (LocaleController.isRTL) {
            n11 = 166.0f;
        }
        else {
            n11 = 96.0f;
        }
        frameLayout.addView((View)onlineTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n9 | 0x30, n10, 0.0f, n11, 0.0f));
        this.writeButton = new ImageView(context);
        CombinedDrawable simpleSelectorCircleDrawable;
        final Drawable drawable = simpleSelectorCircleDrawable = (CombinedDrawable)Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("profile_actionBackground"), Theme.getColor("profile_actionPressedBackground"));
        if (Build$VERSION.SDK_INT < 21) {
            final Drawable mutate = context.getResources().getDrawable(2131165388).mutate();
            mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
            simpleSelectorCircleDrawable = new CombinedDrawable(mutate, drawable, 0, 0);
            simpleSelectorCircleDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
        this.writeButton.setBackgroundDrawable((Drawable)simpleSelectorCircleDrawable);
        this.writeButton.setImageResource(2131165572);
        this.writeButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), PorterDuff$Mode.MULTIPLY));
        this.writeButton.setScaleType(ImageView$ScaleType.CENTER);
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.writeButton.setStateListAnimator(stateListAnimator);
            this.writeButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        final ImageView writeButton = this.writeButton;
        int n12;
        if (Build$VERSION.SDK_INT >= 21) {
            n12 = 56;
        }
        else {
            n12 = 60;
        }
        float n13;
        if (Build$VERSION.SDK_INT >= 21) {
            n13 = 56.0f;
        }
        else {
            n13 = 60.0f;
        }
        int n14 = n;
        if (LocaleController.isRTL) {
            n14 = 3;
        }
        float n15;
        if (LocaleController.isRTL) {
            n15 = 16.0f;
        }
        else {
            n15 = 0.0f;
        }
        float n16;
        if (LocaleController.isRTL) {
            n16 = 0.0f;
        }
        else {
            n16 = 16.0f;
        }
        frameLayout.addView((View)writeButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n12, n13, n14 | 0x30, n15, 0.0f, n16, 0.0f));
        this.writeButton.setOnClickListener((View$OnClickListener)new _$$Lambda$SettingsActivity$r8NmpkmDLMDut28p1uEJ0PAPo_c(this));
        this.writeButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrChangeProfilePicture", 2131558425));
        if (firstVisibleItemPosition != -1) {
            this.layoutManager.scrollToPositionWithOffset(firstVisibleItemPosition, top);
            if (tag != null) {
                this.writeButton.setTag((Object)0);
                this.writeButton.setScaleX(0.2f);
                this.writeButton.setScaleY(0.2f);
                this.writeButton.setAlpha(0.0f);
                this.writeButton.setVisibility(8);
            }
        }
        this.needLayout();
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && SettingsActivity.this.listView.getAdapter() == SettingsActivity.this.searchAdapter) {
                    AndroidUtilities.hideKeyboard(SettingsActivity.this.getParentActivity().getCurrentFocus());
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, int top, int n) {
                if (((RecyclerView.LayoutManager)SettingsActivity.this.layoutManager).getItemCount() == 0) {
                    return;
                }
                top = 0;
                n = 0;
                final View child = recyclerView.getChildAt(0);
                if (child != null && SettingsActivity.this.avatarContainer.getVisibility() == 0) {
                    if (SettingsActivity.this.layoutManager.findFirstVisibleItemPosition() == 0) {
                        final int dp = AndroidUtilities.dp(88.0f);
                        top = n;
                        if (child.getTop() < 0) {
                            top = child.getTop();
                        }
                        top += dp;
                    }
                    if (SettingsActivity.this.extraHeight != top) {
                        SettingsActivity.this.extraHeight = top;
                        SettingsActivity.this.needLayout();
                    }
                }
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int intValue, final int n, final Object... array) {
        if (intValue == NotificationCenter.updateInterfaces) {
            intValue = (int)array[0];
            if ((intValue & 0x2) != 0x0 || (intValue & 0x1) != 0x0) {
                this.updateUserData();
            }
        }
        else if (intValue == NotificationCenter.userInfoDidLoad) {
            if ((int)array[0] == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                final ListAdapter listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    this.userInfo = (TLRPC.UserFull)array[1];
                    ((RecyclerView.Adapter)listAdapter).notifyItemChanged(this.bioRow);
                }
            }
        }
        else if (intValue == NotificationCenter.emojiDidLoad) {
            final RecyclerListView listView = this.listView;
            if (listView != null) {
                listView.invalidateViews();
            }
        }
    }
    
    @Override
    public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$SettingsActivity$NCmSP4zZF_AHhm4jiT4j3ELozqs(this, inputFile, photoSize2, photoSize));
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { EmptyCell.class, HeaderCell.class, TextDetailCell.class, TextCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundGray"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription(this.extraHeightView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "avatar_actionBarIconBlue"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "avatar_actionBarSelectorBlue"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "profile_title"), new ThemeDescription((View)this.onlineTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "profile_status"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[] { this.avatarDrawable }, null, "avatar_backgroundInProfileBlue"), new ThemeDescription((View)this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "profile_actionIcon"), new ThemeDescription((View)this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "profile_actionBackground"), new ThemeDescription((View)this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "profile_actionPressedBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.listView, 0, new Class[] { SettingsSearchCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { SettingsSearchCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { SettingsSearchCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        this.imageUpdater.onActivityResult(n, n2, intent);
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.imageUpdater = new ImageUpdater();
        final ImageUpdater imageUpdater = this.imageUpdater;
        imageUpdater.parentFragment = this;
        imageUpdater.delegate = (ImageUpdater.ImageUpdaterDelegate)this;
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoad);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.rowCount = 0;
        this.overscrollRow = this.rowCount++;
        this.numberSectionRow = this.rowCount++;
        this.numberRow = this.rowCount++;
        this.usernameRow = this.rowCount++;
        this.bioRow = this.rowCount++;
        this.settingsSectionRow = this.rowCount++;
        this.settingsSectionRow2 = this.rowCount++;
        this.notificationRow = this.rowCount++;
        this.privacyRow = this.rowCount++;
        this.dataRow = this.rowCount++;
        this.chatRow = this.rowCount++;
        this.languageRow = this.rowCount++;
        this.helpRow = this.rowCount++;
        this.versionRow = this.rowCount++;
        DataQuery.getInstance(super.currentAccount).checkFeaturedStickers();
        this.userInfo = MessagesController.getInstance(super.currentAccount).getUserFull(UserConfig.getInstance(super.currentAccount).getClientUserId());
        MessagesController.getInstance(super.currentAccount).loadUserInfo(UserConfig.getInstance(super.currentAccount).getCurrentUser(), true, super.classGuid);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        final BackupImageView avatarImage = this.avatarImage;
        if (avatarImage != null) {
            avatarImage.setImageDrawable(null);
        }
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoad);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        this.imageUpdater.clear();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        this.updateUserData();
        this.fixLayout();
        this.setParentActivityTitle(LocaleController.getString("Settings", 2131560738));
    }
    
    @Override
    public void restoreSelfArgs(final Bundle bundle) {
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.currentPicturePath = bundle.getString("path");
        }
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            final String currentPicturePath = imageUpdater.currentPicturePath;
            if (currentPicturePath != null) {
                bundle.putString("path", currentPicturePath);
            }
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return SettingsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == SettingsActivity.this.overscrollRow) {
                return 0;
            }
            if (n == SettingsActivity.this.settingsSectionRow) {
                return 1;
            }
            if (n == SettingsActivity.this.notificationRow || n == SettingsActivity.this.privacyRow || n == SettingsActivity.this.languageRow || n == SettingsActivity.this.dataRow || n == SettingsActivity.this.chatRow || n == SettingsActivity.this.helpRow) {
                return 2;
            }
            if (n == SettingsActivity.this.versionRow) {
                return 5;
            }
            if (n == SettingsActivity.this.numberRow || n == SettingsActivity.this.usernameRow || n == SettingsActivity.this.bioRow) {
                return 6;
            }
            if (n != SettingsActivity.this.settingsSectionRow2 && n != SettingsActivity.this.numberSectionRow) {
                return 2;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == SettingsActivity.this.notificationRow || adapterPosition == SettingsActivity.this.numberRow || adapterPosition == SettingsActivity.this.privacyRow || adapterPosition == SettingsActivity.this.languageRow || adapterPosition == SettingsActivity.this.usernameRow || adapterPosition == SettingsActivity.this.bioRow || adapterPosition == SettingsActivity.this.versionRow || adapterPosition == SettingsActivity.this.dataRow || adapterPosition == SettingsActivity.this.chatRow || adapterPosition == SettingsActivity.this.helpRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 2) {
                    if (itemViewType != 4) {
                        if (itemViewType == 6) {
                            final TextDetailCell textDetailCell = (TextDetailCell)viewHolder.itemView;
                            if (n == SettingsActivity.this.numberRow) {
                                final TLRPC.User currentUser = UserConfig.getInstance(SettingsActivity.this.currentAccount).getCurrentUser();
                                String s = null;
                                Label_0139: {
                                    if (currentUser != null) {
                                        final String phone = currentUser.phone;
                                        if (phone != null && phone.length() != 0) {
                                            final PhoneFormat instance = PhoneFormat.getInstance();
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("+");
                                            sb.append(currentUser.phone);
                                            s = instance.format(sb.toString());
                                            break Label_0139;
                                        }
                                    }
                                    s = LocaleController.getString("NumberUnknown", 2131560096);
                                }
                                textDetailCell.setTextAndValue(s, LocaleController.getString("TapToChangePhone", 2131560860), true);
                            }
                            else if (n == SettingsActivity.this.usernameRow) {
                                final TLRPC.User currentUser2 = UserConfig.getInstance(SettingsActivity.this.currentAccount).getCurrentUser();
                                String s2;
                                if (currentUser2 != null && !TextUtils.isEmpty((CharSequence)currentUser2.username)) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("@");
                                    sb2.append(currentUser2.username);
                                    s2 = sb2.toString();
                                }
                                else {
                                    s2 = LocaleController.getString("UsernameEmpty", 2131561024);
                                }
                                textDetailCell.setTextAndValue(s2, LocaleController.getString("Username", 2131561021), true);
                            }
                            else if (n == SettingsActivity.this.bioRow) {
                                if (SettingsActivity.this.userInfo != null && TextUtils.isEmpty((CharSequence)SettingsActivity.this.userInfo.about)) {
                                    textDetailCell.setTextAndValue(LocaleController.getString("UserBio", 2131560987), LocaleController.getString("UserBioDetail", 2131560988), false);
                                }
                                else {
                                    String s3;
                                    if (SettingsActivity.this.userInfo == null) {
                                        s3 = LocaleController.getString("Loading", 2131559768);
                                    }
                                    else {
                                        s3 = SettingsActivity.this.userInfo.about;
                                    }
                                    textDetailCell.setTextWithEmojiAndValue(s3, LocaleController.getString("UserBio", 2131560987), false);
                                }
                            }
                        }
                    }
                    else if (n == SettingsActivity.this.settingsSectionRow2) {
                        ((HeaderCell)viewHolder.itemView).setText(LocaleController.getString("SETTINGS", 2131560623));
                    }
                    else if (n == SettingsActivity.this.numberSectionRow) {
                        ((HeaderCell)viewHolder.itemView).setText(LocaleController.getString("Account", 2131558486));
                    }
                }
                else {
                    final TextCell textCell = (TextCell)viewHolder.itemView;
                    if (n == SettingsActivity.this.languageRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Language", 2131559715), 2131165586, true);
                    }
                    else if (n == SettingsActivity.this.notificationRow) {
                        textCell.setTextAndIcon(LocaleController.getString("NotificationsAndSounds", 2131560057), 2131165588, true);
                    }
                    else if (n == SettingsActivity.this.privacyRow) {
                        textCell.setTextAndIcon(LocaleController.getString("PrivacySettings", 2131560509), 2131165594, true);
                    }
                    else if (n == SettingsActivity.this.dataRow) {
                        textCell.setTextAndIcon(LocaleController.getString("DataSettings", 2131559193), 2131165579, true);
                    }
                    else if (n == SettingsActivity.this.chatRow) {
                        textCell.setTextAndIcon(LocaleController.getString("ChatSettings", 2131559043), 2131165574, true);
                    }
                    else if (n == SettingsActivity.this.helpRow) {
                        textCell.setTextAndIcon(LocaleController.getString("SettingsHelp", 2131560740), 2131165582, false);
                    }
                }
            }
            else if (n == SettingsActivity.this.overscrollRow) {
                ((EmptyCell)viewHolder.itemView).setHeight(AndroidUtilities.dp(88.0f));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
            Object o = null;
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 4) {
                            if (i != 5) {
                                if (i == 6) {
                                    o = new TextDetailCell(this.mContext);
                                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                }
                            }
                            else {
                                final TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext, 10);
                                textInfoPrivacyCell.getTextView().setGravity(1);
                                textInfoPrivacyCell.getTextView().setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
                                textInfoPrivacyCell.getTextView().setMovementMethod((MovementMethod)null);
                                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                                try {
                                    final PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                                    i = packageInfo.versionCode / 10;
                                    String string = "";
                                    switch (packageInfo.versionCode % 10) {
                                        case 6:
                                        case 8: {
                                            string = "x86_64";
                                            break;
                                        }
                                        case 5:
                                        case 7: {
                                            string = "arm64-v8a";
                                            break;
                                        }
                                        case 2:
                                        case 4: {
                                            string = "x86";
                                            break;
                                        }
                                        case 1:
                                        case 3: {
                                            string = "arm-v7a";
                                            break;
                                        }
                                        case 0:
                                        case 9: {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("universal ");
                                            sb.append(Build.CPU_ABI);
                                            sb.append(" ");
                                            sb.append(Build.CPU_ABI2);
                                            string = sb.toString();
                                            break;
                                        }
                                    }
                                    textInfoPrivacyCell.setText(String.format("Telegram-FOSS %1$s", String.format(Locale.US, "v%s (%d) %s", packageInfo.versionName, i, string)));
                                }
                                catch (Exception ex) {
                                    FileLog.e(ex);
                                }
                                textInfoPrivacyCell.getTextView().setPadding(0, AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f));
                                o = textInfoPrivacyCell;
                            }
                        }
                        else {
                            o = new HeaderCell(this.mContext, 23);
                            ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        o = new TextCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    o = new ShadowSectionCell(this.mContext);
                }
            }
            else {
                o = new EmptyCell(this.mContext);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class SearchAdapter extends SelectionAdapter
    {
        private ArrayList<FaqSearchResult> faqSearchArray;
        private ArrayList<FaqSearchResult> faqSearchResults;
        private TLRPC.WebPage faqWebPage;
        private String lastSearchString;
        private boolean loadingFaqPage;
        private Context mContext;
        private ArrayList<Object> recentSearches;
        private ArrayList<CharSequence> resultNames;
        private SearchResult[] searchArray;
        private ArrayList<SearchResult> searchResults;
        private Runnable searchRunnable;
        private boolean searchWas;
        final /* synthetic */ SettingsActivity this$0;
        
        public SearchAdapter(final SettingsActivity p0, final Context p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1        
            //     2: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.this$0:Lorg/telegram/ui/SettingsActivity;
            //     5: aload_0        
            //     6: invokespecial   org/telegram/ui/Components/RecyclerListView$SelectionAdapter.<init>:()V
            //     9: aload_0        
            //    10: bipush          81
            //    12: anewarray       Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //    15: dup            
            //    16: iconst_0       
            //    17: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //    20: dup            
            //    21: aload_0        
            //    22: sipush          500
            //    25: ldc             "EditName"
            //    27: ldc             2131559326
            //    29: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //    32: iconst_0       
            //    33: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$fYhNxPaKcCcAqdniDsRVcdOfAcw;
            //    36: dup            
            //    37: aload_0        
            //    38: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$fYhNxPaKcCcAqdniDsRVcdOfAcw.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //    41: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //    44: aastore        
            //    45: dup            
            //    46: iconst_1       
            //    47: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //    50: dup            
            //    51: aload_0        
            //    52: sipush          501
            //    55: ldc             "ChangePhoneNumber"
            //    57: ldc             2131558913
            //    59: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //    62: iconst_0       
            //    63: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$AQE0PybSOWsTppwbXC4ScpJt5cU;
            //    66: dup            
            //    67: aload_0        
            //    68: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$AQE0PybSOWsTppwbXC4ScpJt5cU.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //    71: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //    74: aastore        
            //    75: dup            
            //    76: iconst_2       
            //    77: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //    80: dup            
            //    81: aload_0        
            //    82: sipush          502
            //    85: ldc             "AddAnotherAccount"
            //    87: ldc             2131558562
            //    89: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //    92: iconst_0       
            //    93: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$qf5DMONPDpbbFVQlIYoeluu6fAg;
            //    96: dup            
            //    97: aload_0        
            //    98: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$qf5DMONPDpbbFVQlIYoeluu6fAg.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   101: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //   104: aastore        
            //   105: dup            
            //   106: iconst_3       
            //   107: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   110: dup            
            //   111: aload_0        
            //   112: sipush          503
            //   115: ldc             "UserBio"
            //   117: ldc             2131560987
            //   119: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   122: iconst_0       
            //   123: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$wEStD_IsL8y26JcLIYLGQZisGR0;
            //   126: dup            
            //   127: aload_0        
            //   128: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$wEStD_IsL8y26JcLIYLGQZisGR0.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   131: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //   134: aastore        
            //   135: dup            
            //   136: iconst_4       
            //   137: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   140: dup            
            //   141: aload_0        
            //   142: iconst_1       
            //   143: ldc             "NotificationsAndSounds"
            //   145: ldc             2131560057
            //   147: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   150: ldc             2131165588
            //   152: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$pgorJIrrbTNFMCB_ShcfXSVsFZo;
            //   155: dup            
            //   156: aload_0        
            //   157: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$pgorJIrrbTNFMCB_ShcfXSVsFZo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   160: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //   163: aastore        
            //   164: dup            
            //   165: iconst_5       
            //   166: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   169: dup            
            //   170: aload_0        
            //   171: iconst_2       
            //   172: ldc             "NotificationsPrivateChats"
            //   174: ldc             2131560087
            //   176: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   179: ldc             "NotificationsAndSounds"
            //   181: ldc             2131560057
            //   183: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   186: ldc             2131165588
            //   188: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$EaevAppnRfEWu4EsBvHSVnfoJO4;
            //   191: dup            
            //   192: aload_0        
            //   193: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$EaevAppnRfEWu4EsBvHSVnfoJO4.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   196: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   199: aastore        
            //   200: dup            
            //   201: bipush          6
            //   203: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   206: dup            
            //   207: aload_0        
            //   208: iconst_3       
            //   209: ldc             "NotificationsGroups"
            //   211: ldc             2131560071
            //   213: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   216: ldc             "NotificationsAndSounds"
            //   218: ldc             2131560057
            //   220: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   223: ldc             2131165588
            //   225: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$_Yb9Kq1MuLzQbkVBNuUX_K5eF_U;
            //   228: dup            
            //   229: aload_0        
            //   230: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$_Yb9Kq1MuLzQbkVBNuUX_K5eF_U.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   233: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   236: aastore        
            //   237: dup            
            //   238: bipush          7
            //   240: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   243: dup            
            //   244: aload_0        
            //   245: iconst_4       
            //   246: ldc             "NotificationsChannels"
            //   248: ldc             2131560058
            //   250: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   253: ldc             "NotificationsAndSounds"
            //   255: ldc             2131560057
            //   257: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   260: ldc             2131165588
            //   262: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$zEBaSkQhcY7piZudVwqn9mABazs;
            //   265: dup            
            //   266: aload_0        
            //   267: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$zEBaSkQhcY7piZudVwqn9mABazs.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   270: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   273: aastore        
            //   274: dup            
            //   275: bipush          8
            //   277: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   280: dup            
            //   281: aload_0        
            //   282: iconst_5       
            //   283: ldc             "VoipNotificationSettings"
            //   285: ldc             2131561075
            //   287: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   290: ldc             "callsSectionRow"
            //   292: ldc             "NotificationsAndSounds"
            //   294: ldc             2131560057
            //   296: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   299: ldc             2131165588
            //   301: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$aceyqZ4yZ6dj0j20qgJOi7MQ0NE;
            //   304: dup            
            //   305: aload_0        
            //   306: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$aceyqZ4yZ6dj0j20qgJOi7MQ0NE.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   309: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   312: aastore        
            //   313: dup            
            //   314: bipush          9
            //   316: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   319: dup            
            //   320: aload_0        
            //   321: bipush          6
            //   323: ldc             "BadgeNumber"
            //   325: ldc             2131558826
            //   327: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   330: ldc             "badgeNumberSection"
            //   332: ldc             "NotificationsAndSounds"
            //   334: ldc             2131560057
            //   336: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   339: ldc             2131165588
            //   341: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$t5wmPo8eU16Y_X8HwQqExxITKKQ;
            //   344: dup            
            //   345: aload_0        
            //   346: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$t5wmPo8eU16Y_X8HwQqExxITKKQ.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   349: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   352: aastore        
            //   353: dup            
            //   354: bipush          10
            //   356: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   359: dup            
            //   360: aload_0        
            //   361: bipush          7
            //   363: ldc             "InAppNotifications"
            //   365: ldc             2131559657
            //   367: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   370: ldc             "inappSectionRow"
            //   372: ldc             "NotificationsAndSounds"
            //   374: ldc             2131560057
            //   376: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   379: ldc             2131165588
            //   381: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$S2uiNLXT4KCsYO2kv5G38zxY_Ow;
            //   384: dup            
            //   385: aload_0        
            //   386: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$S2uiNLXT4KCsYO2kv5G38zxY_Ow.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   389: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   392: aastore        
            //   393: dup            
            //   394: bipush          11
            //   396: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   399: dup            
            //   400: aload_0        
            //   401: bipush          8
            //   403: ldc             "ContactJoined"
            //   405: ldc             2131559144
            //   407: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   410: ldc             "contactJoinedRow"
            //   412: ldc             "NotificationsAndSounds"
            //   414: ldc             2131560057
            //   416: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   419: ldc             2131165588
            //   421: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$Dee0X2OE_oxy67HDXUAjVnFzV7c;
            //   424: dup            
            //   425: aload_0        
            //   426: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$Dee0X2OE_oxy67HDXUAjVnFzV7c.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   429: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   432: aastore        
            //   433: dup            
            //   434: bipush          12
            //   436: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   439: dup            
            //   440: aload_0        
            //   441: bipush          9
            //   443: ldc             "PinnedMessages"
            //   445: ldc             2131560452
            //   447: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   450: ldc             "pinnedMessageRow"
            //   452: ldc             "NotificationsAndSounds"
            //   454: ldc             2131560057
            //   456: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   459: ldc             2131165588
            //   461: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$O36kxEh77vwepAjM1YBjwwIEdDY;
            //   464: dup            
            //   465: aload_0        
            //   466: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$O36kxEh77vwepAjM1YBjwwIEdDY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   469: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   472: aastore        
            //   473: dup            
            //   474: bipush          13
            //   476: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   479: dup            
            //   480: aload_0        
            //   481: bipush          10
            //   483: ldc             "ResetAllNotifications"
            //   485: ldc             2131560589
            //   487: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   490: ldc             "resetNotificationsRow"
            //   492: ldc             "NotificationsAndSounds"
            //   494: ldc             2131560057
            //   496: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   499: ldc             2131165588
            //   501: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$OmbyIf1DIC6b_M8uR5C3UfAIxWo;
            //   504: dup            
            //   505: aload_0        
            //   506: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$OmbyIf1DIC6b_M8uR5C3UfAIxWo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   509: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   512: aastore        
            //   513: dup            
            //   514: bipush          14
            //   516: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   519: dup            
            //   520: aload_0        
            //   521: bipush          100
            //   523: ldc             "PrivacySettings"
            //   525: ldc             2131560509
            //   527: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   530: ldc             2131165594
            //   532: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$fw82m1Z1AIBCwhHRMVab7sgzrow;
            //   535: dup            
            //   536: aload_0        
            //   537: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$fw82m1Z1AIBCwhHRMVab7sgzrow.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   540: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //   543: aastore        
            //   544: dup            
            //   545: bipush          15
            //   547: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   550: dup            
            //   551: aload_0        
            //   552: bipush          101
            //   554: ldc             "BlockedUsers"
            //   556: ldc             2131558835
            //   558: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   561: ldc             "PrivacySettings"
            //   563: ldc             2131560509
            //   565: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   568: ldc             2131165594
            //   570: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$z1aqQi8eq9En3J0XVwkCNdcd6mM;
            //   573: dup            
            //   574: aload_0        
            //   575: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$z1aqQi8eq9En3J0XVwkCNdcd6mM.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   578: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   581: aastore        
            //   582: dup            
            //   583: bipush          16
            //   585: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   588: dup            
            //   589: aload_0        
            //   590: bipush          105
            //   592: ldc             "PrivacyPhone"
            //   594: ldc             2131560498
            //   596: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   599: ldc             "PrivacySettings"
            //   601: ldc             2131560509
            //   603: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   606: ldc             2131165594
            //   608: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$f_3utWtHCzlSn6039S3bgof9vWg;
            //   611: dup            
            //   612: aload_0        
            //   613: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$f_3utWtHCzlSn6039S3bgof9vWg.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   616: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   619: aastore        
            //   620: dup            
            //   621: bipush          17
            //   623: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   626: dup            
            //   627: aload_0        
            //   628: bipush          102
            //   630: ldc             "PrivacyLastSeen"
            //   632: ldc             2131560492
            //   634: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   637: ldc             "PrivacySettings"
            //   639: ldc             2131560509
            //   641: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   644: ldc             2131165594
            //   646: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$xiSHHuIuMQVeQ_fdA3Wj_Bgq4iA;
            //   649: dup            
            //   650: aload_0        
            //   651: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$xiSHHuIuMQVeQ_fdA3Wj_Bgq4iA.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   654: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   657: aastore        
            //   658: dup            
            //   659: bipush          18
            //   661: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   664: dup            
            //   665: aload_0        
            //   666: bipush          103
            //   668: ldc             "PrivacyProfilePhoto"
            //   670: ldc             2131560505
            //   672: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   675: ldc             "PrivacySettings"
            //   677: ldc             2131560509
            //   679: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   682: ldc             2131165594
            //   684: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$RYPspq_lY6i8gNBxBdrIhxctFpo;
            //   687: dup            
            //   688: aload_0        
            //   689: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$RYPspq_lY6i8gNBxBdrIhxctFpo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   692: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   695: aastore        
            //   696: dup            
            //   697: bipush          19
            //   699: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   702: dup            
            //   703: aload_0        
            //   704: bipush          104
            //   706: ldc             "PrivacyForwards"
            //   708: ldc             2131560484
            //   710: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   713: ldc             "PrivacySettings"
            //   715: ldc             2131560509
            //   717: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   720: ldc             2131165594
            //   722: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$6LoBQomheYgP8glc54xFz2WzjyY;
            //   725: dup            
            //   726: aload_0        
            //   727: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$6LoBQomheYgP8glc54xFz2WzjyY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   730: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   733: aastore        
            //   734: dup            
            //   735: bipush          20
            //   737: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   740: dup            
            //   741: aload_0        
            //   742: bipush          105
            //   744: ldc             "PrivacyP2P"
            //   746: ldc             2131560493
            //   748: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   751: ldc             "PrivacySettings"
            //   753: ldc             2131560509
            //   755: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   758: ldc             2131165594
            //   760: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$VprFTsbEA5x2jnmQ8O1zs63h_9E;
            //   763: dup            
            //   764: aload_0        
            //   765: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$VprFTsbEA5x2jnmQ8O1zs63h_9E.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   768: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   771: aastore        
            //   772: dup            
            //   773: bipush          21
            //   775: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   778: dup            
            //   779: aload_0        
            //   780: bipush          106
            //   782: ldc             "Calls"
            //   784: ldc             2131558888
            //   786: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   789: ldc             "PrivacySettings"
            //   791: ldc             2131560509
            //   793: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   796: ldc             2131165594
            //   798: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$c5DPXMo95Hg5TAoapaOpYope9ao;
            //   801: dup            
            //   802: aload_0        
            //   803: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$c5DPXMo95Hg5TAoapaOpYope9ao.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   806: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   809: aastore        
            //   810: dup            
            //   811: bipush          22
            //   813: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   816: dup            
            //   817: aload_0        
            //   818: bipush          107
            //   820: ldc             "GroupsAndChannels"
            //   822: ldc             2131559624
            //   824: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   827: ldc             "PrivacySettings"
            //   829: ldc             2131560509
            //   831: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   834: ldc             2131165594
            //   836: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$YveYfMPgJtpzBc_Aye2IlUKRL_E;
            //   839: dup            
            //   840: aload_0        
            //   841: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$YveYfMPgJtpzBc_Aye2IlUKRL_E.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   844: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   847: aastore        
            //   848: dup            
            //   849: bipush          23
            //   851: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   854: dup            
            //   855: aload_0        
            //   856: bipush          108
            //   858: ldc             "Passcode"
            //   860: ldc             2131560160
            //   862: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   865: ldc             "PrivacySettings"
            //   867: ldc             2131560509
            //   869: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   872: ldc             2131165594
            //   874: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$OE5ko5HrJRcdo_04aDjTiMIRF4o;
            //   877: dup            
            //   878: aload_0        
            //   879: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$OE5ko5HrJRcdo_04aDjTiMIRF4o.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   882: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   885: aastore        
            //   886: dup            
            //   887: bipush          24
            //   889: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   892: dup            
            //   893: aload_0        
            //   894: bipush          109
            //   896: ldc             "TwoStepVerification"
            //   898: ldc             2131560919
            //   900: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   903: ldc             "PrivacySettings"
            //   905: ldc             2131560509
            //   907: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   910: ldc             2131165594
            //   912: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$_P9hp4zriAme7mOQTdmdfHidC8Y;
            //   915: dup            
            //   916: aload_0        
            //   917: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$_P9hp4zriAme7mOQTdmdfHidC8Y.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   920: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   923: aastore        
            //   924: dup            
            //   925: bipush          25
            //   927: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   930: dup            
            //   931: aload_0        
            //   932: bipush          110
            //   934: ldc             "SessionsTitle"
            //   936: ldc             2131560726
            //   938: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   941: ldc             "PrivacySettings"
            //   943: ldc             2131560509
            //   945: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   948: ldc             2131165594
            //   950: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$B_YXrprYoUDKN_R4RWC_vN8dQ3Y;
            //   953: dup            
            //   954: aload_0        
            //   955: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$B_YXrprYoUDKN_R4RWC_vN8dQ3Y.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   958: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //   961: aastore        
            //   962: dup            
            //   963: bipush          26
            //   965: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //   968: dup            
            //   969: aload_0        
            //   970: bipush          111
            //   972: ldc             "PrivacyDeleteCloudDrafts"
            //   974: ldc             2131560481
            //   976: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   979: ldc             "clearDraftsRow"
            //   981: ldc             "PrivacySettings"
            //   983: ldc             2131560509
            //   985: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //   988: ldc             2131165594
            //   990: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$5sT7VrARMFm8AXSv5eWPD8wnHec;
            //   993: dup            
            //   994: aload_0        
            //   995: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$5sT7VrARMFm8AXSv5eWPD8wnHec.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //   998: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1001: aastore        
            //  1002: dup            
            //  1003: bipush          27
            //  1005: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1008: dup            
            //  1009: aload_0        
            //  1010: bipush          112
            //  1012: ldc             "DeleteAccountIfAwayFor2"
            //  1014: ldc             2131559229
            //  1016: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1019: ldc             "deleteAccountRow"
            //  1021: ldc             "PrivacySettings"
            //  1023: ldc             2131560509
            //  1025: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1028: ldc             2131165594
            //  1030: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$lQlwXu3fj_1ozEGBWi7cV_Yrauo;
            //  1033: dup            
            //  1034: aload_0        
            //  1035: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$lQlwXu3fj_1ozEGBWi7cV_Yrauo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1038: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1041: aastore        
            //  1042: dup            
            //  1043: bipush          28
            //  1045: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1048: dup            
            //  1049: aload_0        
            //  1050: bipush          113
            //  1052: ldc             "PrivacyPaymentsClear"
            //  1054: ldc             2131560496
            //  1056: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1059: ldc             "paymentsClearRow"
            //  1061: ldc             "PrivacySettings"
            //  1063: ldc             2131560509
            //  1065: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1068: ldc             2131165594
            //  1070: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$VLzl5etbBP7OOe7IzDvNV3te2pQ;
            //  1073: dup            
            //  1074: aload_0        
            //  1075: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$VLzl5etbBP7OOe7IzDvNV3te2pQ.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1078: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1081: aastore        
            //  1082: dup            
            //  1083: bipush          29
            //  1085: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1088: dup            
            //  1089: aload_0        
            //  1090: bipush          114
            //  1092: ldc_w           "WebSessionsTitle"
            //  1095: ldc_w           2131561104
            //  1098: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1101: ldc             "PrivacySettings"
            //  1103: ldc             2131560509
            //  1105: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1108: ldc             2131165594
            //  1110: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$i8RSZV17ziFovJKq7XfIMHoM02o;
            //  1113: dup            
            //  1114: aload_0        
            //  1115: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$i8RSZV17ziFovJKq7XfIMHoM02o.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1118: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1121: aastore        
            //  1122: dup            
            //  1123: bipush          30
            //  1125: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1128: dup            
            //  1129: aload_0        
            //  1130: bipush          115
            //  1132: ldc_w           "SyncContactsDelete"
            //  1135: ldc_w           2131560851
            //  1138: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1141: ldc_w           "contactsDeleteRow"
            //  1144: ldc             "PrivacySettings"
            //  1146: ldc             2131560509
            //  1148: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1151: ldc             2131165594
            //  1153: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$ykgPCWhfDa0SAQRBusRsel30WAo;
            //  1156: dup            
            //  1157: aload_0        
            //  1158: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$ykgPCWhfDa0SAQRBusRsel30WAo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1161: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1164: aastore        
            //  1165: dup            
            //  1166: bipush          31
            //  1168: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1171: dup            
            //  1172: aload_0        
            //  1173: bipush          116
            //  1175: ldc_w           "SyncContacts"
            //  1178: ldc_w           2131560849
            //  1181: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1184: ldc_w           "contactsSyncRow"
            //  1187: ldc             "PrivacySettings"
            //  1189: ldc             2131560509
            //  1191: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1194: ldc             2131165594
            //  1196: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$ogTF__W6GjBUa6jCJUHIlUDUEH0;
            //  1199: dup            
            //  1200: aload_0        
            //  1201: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$ogTF__W6GjBUa6jCJUHIlUDUEH0.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1204: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1207: aastore        
            //  1208: dup            
            //  1209: bipush          32
            //  1211: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1214: dup            
            //  1215: aload_0        
            //  1216: bipush          117
            //  1218: ldc_w           "SuggestContacts"
            //  1221: ldc_w           2131560840
            //  1224: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1227: ldc_w           "contactsSuggestRow"
            //  1230: ldc             "PrivacySettings"
            //  1232: ldc             2131560509
            //  1234: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1237: ldc             2131165594
            //  1239: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$gXqnmEjhglRSWqeZPzgMYOpOhsk;
            //  1242: dup            
            //  1243: aload_0        
            //  1244: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$gXqnmEjhglRSWqeZPzgMYOpOhsk.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1247: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1250: aastore        
            //  1251: dup            
            //  1252: bipush          33
            //  1254: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1257: dup            
            //  1258: aload_0        
            //  1259: bipush          118
            //  1261: ldc_w           "MapPreviewProvider"
            //  1264: ldc_w           2131559801
            //  1267: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1270: ldc_w           "secretMapRow"
            //  1273: ldc             "PrivacySettings"
            //  1275: ldc             2131560509
            //  1277: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1280: ldc             2131165594
            //  1282: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$lb1RmyZ9zEsc2hcDKKU7dHdJeD8;
            //  1285: dup            
            //  1286: aload_0        
            //  1287: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$lb1RmyZ9zEsc2hcDKKU7dHdJeD8.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1290: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1293: aastore        
            //  1294: dup            
            //  1295: bipush          34
            //  1297: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1300: dup            
            //  1301: aload_0        
            //  1302: bipush          119
            //  1304: ldc_w           "SecretWebPage"
            //  1307: ldc_w           2131560673
            //  1310: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1313: ldc_w           "secretWebpageRow"
            //  1316: ldc             "PrivacySettings"
            //  1318: ldc             2131560509
            //  1320: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1323: ldc             2131165594
            //  1325: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$CPyFvh3ngQ1x3gECH51yA4WVVU8;
            //  1328: dup            
            //  1329: aload_0        
            //  1330: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$CPyFvh3ngQ1x3gECH51yA4WVVU8.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1333: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1336: aastore        
            //  1337: dup            
            //  1338: bipush          35
            //  1340: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1343: dup            
            //  1344: aload_0        
            //  1345: sipush          200
            //  1348: ldc_w           "DataSettings"
            //  1351: ldc_w           2131559193
            //  1354: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1357: ldc_w           2131165579
            //  1360: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$SR0QQ8fXitd2n2FmlZnOuv_PjCM;
            //  1363: dup            
            //  1364: aload_0        
            //  1365: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$SR0QQ8fXitd2n2FmlZnOuv_PjCM.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1368: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //  1371: aastore        
            //  1372: dup            
            //  1373: bipush          36
            //  1375: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1378: dup            
            //  1379: aload_0        
            //  1380: sipush          201
            //  1383: ldc_w           "DataUsage"
            //  1386: ldc_w           2131559194
            //  1389: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1392: ldc_w           "usageSectionRow"
            //  1395: ldc_w           "DataSettings"
            //  1398: ldc_w           2131559193
            //  1401: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1404: ldc_w           2131165579
            //  1407: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$jxZ1ug5oHHZLFprdCJ2sObOMdeY;
            //  1410: dup            
            //  1411: aload_0        
            //  1412: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$jxZ1ug5oHHZLFprdCJ2sObOMdeY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1415: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1418: aastore        
            //  1419: dup            
            //  1420: bipush          37
            //  1422: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1425: dup            
            //  1426: aload_0        
            //  1427: sipush          202
            //  1430: ldc_w           "StorageUsage"
            //  1433: ldc_w           2131560832
            //  1436: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1439: ldc_w           "DataSettings"
            //  1442: ldc_w           2131559193
            //  1445: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1448: ldc_w           2131165579
            //  1451: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$2a_PtgyUOPZ5daThh02W6OTXvjI;
            //  1454: dup            
            //  1455: aload_0        
            //  1456: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$2a_PtgyUOPZ5daThh02W6OTXvjI.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1459: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1462: aastore        
            //  1463: dup            
            //  1464: bipush          38
            //  1466: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1469: dup            
            //  1470: aload_0        
            //  1471: sipush          203
            //  1474: ldc_w           "KeepMedia"
            //  1477: ldc_w           2131559710
            //  1480: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1483: ldc_w           "keepMediaRow"
            //  1486: ldc_w           "DataSettings"
            //  1489: ldc_w           2131559193
            //  1492: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1495: ldc_w           "StorageUsage"
            //  1498: ldc_w           2131560832
            //  1501: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1504: ldc_w           2131165579
            //  1507: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$OfGBrtqXQbffkrMJs_wPG7G0fXA;
            //  1510: dup            
            //  1511: aload_0        
            //  1512: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$OfGBrtqXQbffkrMJs_wPG7G0fXA.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1515: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1518: aastore        
            //  1519: dup            
            //  1520: bipush          39
            //  1522: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1525: dup            
            //  1526: aload_0        
            //  1527: sipush          204
            //  1530: ldc_w           "ClearMediaCache"
            //  1533: ldc_w           2131559110
            //  1536: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1539: ldc_w           "cacheRow"
            //  1542: ldc_w           "DataSettings"
            //  1545: ldc_w           2131559193
            //  1548: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1551: ldc_w           "StorageUsage"
            //  1554: ldc_w           2131560832
            //  1557: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1560: ldc_w           2131165579
            //  1563: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$d_3fa2mVqtsd9YcCWBrdaK4AvwI;
            //  1566: dup            
            //  1567: aload_0        
            //  1568: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$d_3fa2mVqtsd9YcCWBrdaK4AvwI.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1571: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1574: aastore        
            //  1575: dup            
            //  1576: bipush          40
            //  1578: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1581: dup            
            //  1582: aload_0        
            //  1583: sipush          205
            //  1586: ldc_w           "LocalDatabase"
            //  1589: ldc_w           2131559772
            //  1592: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1595: ldc_w           "databaseRow"
            //  1598: ldc_w           "DataSettings"
            //  1601: ldc_w           2131559193
            //  1604: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1607: ldc_w           "StorageUsage"
            //  1610: ldc_w           2131560832
            //  1613: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1616: ldc_w           2131165579
            //  1619: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$TWYG1OVbu9uZ6lFX1vM2hYJxs7w;
            //  1622: dup            
            //  1623: aload_0        
            //  1624: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$TWYG1OVbu9uZ6lFX1vM2hYJxs7w.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1627: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1630: aastore        
            //  1631: dup            
            //  1632: bipush          41
            //  1634: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1637: dup            
            //  1638: aload_0        
            //  1639: sipush          206
            //  1642: ldc_w           "NetworkUsage"
            //  1645: ldc_w           2131559889
            //  1648: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1651: ldc_w           "DataSettings"
            //  1654: ldc_w           2131559193
            //  1657: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1660: ldc_w           2131165579
            //  1663: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$2CLVCiRkKjEbcz0jARW3ZP41JX8;
            //  1666: dup            
            //  1667: aload_0        
            //  1668: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$2CLVCiRkKjEbcz0jARW3ZP41JX8.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1671: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1674: aastore        
            //  1675: dup            
            //  1676: bipush          42
            //  1678: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1681: dup            
            //  1682: aload_0        
            //  1683: sipush          207
            //  1686: ldc_w           "AutomaticMediaDownload"
            //  1689: ldc_w           2131558802
            //  1692: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1695: ldc_w           "mediaDownloadSectionRow"
            //  1698: ldc_w           "DataSettings"
            //  1701: ldc_w           2131559193
            //  1704: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1707: ldc_w           2131165579
            //  1710: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$VUKBbuzi1KBkCnnX2JlWeTm2Cac;
            //  1713: dup            
            //  1714: aload_0        
            //  1715: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$VUKBbuzi1KBkCnnX2JlWeTm2Cac.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1718: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1721: aastore        
            //  1722: dup            
            //  1723: bipush          43
            //  1725: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1728: dup            
            //  1729: aload_0        
            //  1730: sipush          208
            //  1733: ldc_w           "WhenUsingMobileData"
            //  1736: ldc_w           2131561113
            //  1739: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1742: ldc_w           "DataSettings"
            //  1745: ldc_w           2131559193
            //  1748: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1751: ldc_w           2131165579
            //  1754: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$K3WxuNbtWdfw44GPZwu3Vih5_x4;
            //  1757: dup            
            //  1758: aload_0        
            //  1759: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$K3WxuNbtWdfw44GPZwu3Vih5_x4.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1762: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1765: aastore        
            //  1766: dup            
            //  1767: bipush          44
            //  1769: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1772: dup            
            //  1773: aload_0        
            //  1774: sipush          209
            //  1777: ldc_w           "WhenConnectedOnWiFi"
            //  1780: ldc_w           2131561111
            //  1783: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1786: ldc_w           "DataSettings"
            //  1789: ldc_w           2131559193
            //  1792: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1795: ldc_w           2131165579
            //  1798: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$BLHHvMFpEDhqBkzKQy4VYJV2Wzg;
            //  1801: dup            
            //  1802: aload_0        
            //  1803: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$BLHHvMFpEDhqBkzKQy4VYJV2Wzg.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1806: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1809: aastore        
            //  1810: dup            
            //  1811: bipush          45
            //  1813: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1816: dup            
            //  1817: aload_0        
            //  1818: sipush          210
            //  1821: ldc_w           "WhenRoaming"
            //  1824: ldc_w           2131561112
            //  1827: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1830: ldc_w           "DataSettings"
            //  1833: ldc_w           2131559193
            //  1836: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1839: ldc_w           2131165579
            //  1842: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$Pqx2xX3w9fZ7nlbzuimuQvpIHXo;
            //  1845: dup            
            //  1846: aload_0        
            //  1847: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$Pqx2xX3w9fZ7nlbzuimuQvpIHXo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1850: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1853: aastore        
            //  1854: dup            
            //  1855: bipush          46
            //  1857: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1860: dup            
            //  1861: aload_0        
            //  1862: sipush          211
            //  1865: ldc_w           "ResetAutomaticMediaDownload"
            //  1868: ldc_w           2131560590
            //  1871: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1874: ldc_w           "resetDownloadRow"
            //  1877: ldc_w           "DataSettings"
            //  1880: ldc_w           2131559193
            //  1883: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1886: ldc_w           2131165579
            //  1889: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$LMKnGgygm7Zy5y5yA0ZMoVKQ0rQ;
            //  1892: dup            
            //  1893: aload_0        
            //  1894: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$LMKnGgygm7Zy5y5yA0ZMoVKQ0rQ.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1897: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1900: aastore        
            //  1901: dup            
            //  1902: bipush          47
            //  1904: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1907: dup            
            //  1908: aload_0        
            //  1909: sipush          212
            //  1912: ldc_w           "AutoplayMedia"
            //  1915: ldc_w           2131558804
            //  1918: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1921: ldc_w           "autoplayHeaderRow"
            //  1924: ldc_w           "DataSettings"
            //  1927: ldc_w           2131559193
            //  1930: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1933: ldc_w           2131165579
            //  1936: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$lrHAN36teH_lCOFJ5PXNFKTwHTM;
            //  1939: dup            
            //  1940: aload_0        
            //  1941: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$lrHAN36teH_lCOFJ5PXNFKTwHTM.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1944: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1947: aastore        
            //  1948: dup            
            //  1949: bipush          48
            //  1951: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  1954: dup            
            //  1955: aload_0        
            //  1956: sipush          213
            //  1959: ldc_w           "AutoplayGIF"
            //  1962: ldc_w           2131558803
            //  1965: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1968: ldc_w           "autoplayGifsRow"
            //  1971: ldc_w           "DataSettings"
            //  1974: ldc_w           2131559193
            //  1977: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  1980: ldc_w           2131165579
            //  1983: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$raORBf9hVrFvO0t2rS9X8ZjwExo;
            //  1986: dup            
            //  1987: aload_0        
            //  1988: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$raORBf9hVrFvO0t2rS9X8ZjwExo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  1991: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  1994: aastore        
            //  1995: dup            
            //  1996: bipush          49
            //  1998: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2001: dup            
            //  2002: aload_0        
            //  2003: sipush          214
            //  2006: ldc_w           "AutoplayVideo"
            //  2009: ldc_w           2131558805
            //  2012: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2015: ldc_w           "autoplayVideoRow"
            //  2018: ldc_w           "DataSettings"
            //  2021: ldc_w           2131559193
            //  2024: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2027: ldc_w           2131165579
            //  2030: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$ZTzEE1deo_nmR_UL_VYD8uhoHoI;
            //  2033: dup            
            //  2034: aload_0        
            //  2035: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$ZTzEE1deo_nmR_UL_VYD8uhoHoI.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2038: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2041: aastore        
            //  2042: dup            
            //  2043: bipush          50
            //  2045: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2048: dup            
            //  2049: aload_0        
            //  2050: sipush          215
            //  2053: ldc_w           "Streaming"
            //  2056: ldc_w           2131560833
            //  2059: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2062: ldc_w           "streamSectionRow"
            //  2065: ldc_w           "DataSettings"
            //  2068: ldc_w           2131559193
            //  2071: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2074: ldc_w           2131165579
            //  2077: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$rOx4Eve_2YVGzrlUGIGIovv9JXE;
            //  2080: dup            
            //  2081: aload_0        
            //  2082: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$rOx4Eve_2YVGzrlUGIGIovv9JXE.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2085: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2088: aastore        
            //  2089: dup            
            //  2090: bipush          51
            //  2092: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2095: dup            
            //  2096: aload_0        
            //  2097: sipush          216
            //  2100: ldc_w           "EnableStreaming"
            //  2103: ldc_w           2131559349
            //  2106: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2109: ldc_w           "enableStreamRow"
            //  2112: ldc_w           "DataSettings"
            //  2115: ldc_w           2131559193
            //  2118: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2121: ldc_w           2131165579
            //  2124: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$rGwlYZXuIw_8MDI7I45SYbMs0G8;
            //  2127: dup            
            //  2128: aload_0        
            //  2129: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$rGwlYZXuIw_8MDI7I45SYbMs0G8.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2132: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2135: aastore        
            //  2136: dup            
            //  2137: bipush          52
            //  2139: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2142: dup            
            //  2143: aload_0        
            //  2144: sipush          217
            //  2147: ldc             "Calls"
            //  2149: ldc             2131558888
            //  2151: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2154: ldc             "callsSectionRow"
            //  2156: ldc_w           "DataSettings"
            //  2159: ldc_w           2131559193
            //  2162: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2165: ldc_w           2131165579
            //  2168: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$yQdZ2pbhXfP1NJfQiJKJVPctbyk;
            //  2171: dup            
            //  2172: aload_0        
            //  2173: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$yQdZ2pbhXfP1NJfQiJKJVPctbyk.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2176: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2179: aastore        
            //  2180: dup            
            //  2181: bipush          53
            //  2183: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2186: dup            
            //  2187: aload_0        
            //  2188: sipush          218
            //  2191: ldc_w           "VoipUseLessData"
            //  2194: ldc_w           2131561093
            //  2197: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2200: ldc_w           "useLessDataForCallsRow"
            //  2203: ldc_w           "DataSettings"
            //  2206: ldc_w           2131559193
            //  2209: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2212: ldc_w           2131165579
            //  2215: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$B4etMl_s5sjFGhMYip4XHa1DxEY;
            //  2218: dup            
            //  2219: aload_0        
            //  2220: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$B4etMl_s5sjFGhMYip4XHa1DxEY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2223: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2226: aastore        
            //  2227: dup            
            //  2228: bipush          54
            //  2230: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2233: dup            
            //  2234: aload_0        
            //  2235: sipush          219
            //  2238: ldc_w           "VoipQuickReplies"
            //  2241: ldc_w           2131561086
            //  2244: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2247: ldc_w           "quickRepliesRow"
            //  2250: ldc_w           "DataSettings"
            //  2253: ldc_w           2131559193
            //  2256: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2259: ldc_w           2131165579
            //  2262: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$i2kSSNMxTtfC03RmjV3ptURjCLA;
            //  2265: dup            
            //  2266: aload_0        
            //  2267: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$i2kSSNMxTtfC03RmjV3ptURjCLA.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2270: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2273: aastore        
            //  2274: dup            
            //  2275: bipush          55
            //  2277: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2280: dup            
            //  2281: aload_0        
            //  2282: sipush          220
            //  2285: ldc_w           "ProxySettings"
            //  2288: ldc_w           2131560519
            //  2291: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2294: ldc_w           "DataSettings"
            //  2297: ldc_w           2131559193
            //  2300: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2303: ldc_w           2131165579
            //  2306: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$XMZSE9GUh_0GP5dBwOjHByrjteo;
            //  2309: dup            
            //  2310: aload_0        
            //  2311: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$XMZSE9GUh_0GP5dBwOjHByrjteo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2314: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2317: aastore        
            //  2318: dup            
            //  2319: bipush          56
            //  2321: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2324: dup            
            //  2325: aload_0        
            //  2326: sipush          221
            //  2329: ldc_w           "UseProxyForCalls"
            //  2332: ldc_w           2131560972
            //  2335: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2338: ldc_w           "callsRow"
            //  2341: ldc_w           "DataSettings"
            //  2344: ldc_w           2131559193
            //  2347: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2350: ldc_w           "ProxySettings"
            //  2353: ldc_w           2131560519
            //  2356: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2359: ldc_w           2131165579
            //  2362: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$t63Mb0PWa1_oMCnIjcS62wWkWhI;
            //  2365: dup            
            //  2366: aload_0        
            //  2367: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$t63Mb0PWa1_oMCnIjcS62wWkWhI.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2370: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2373: aastore        
            //  2374: dup            
            //  2375: bipush          57
            //  2377: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2380: dup            
            //  2381: aload_0        
            //  2382: sipush          300
            //  2385: ldc_w           "ChatSettings"
            //  2388: ldc_w           2131559043
            //  2391: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2394: ldc_w           2131165574
            //  2397: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$oHMz_dP6H50aHqxTbdc3E7d4Oyo;
            //  2400: dup            
            //  2401: aload_0        
            //  2402: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$oHMz_dP6H50aHqxTbdc3E7d4Oyo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2405: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //  2408: aastore        
            //  2409: dup            
            //  2410: bipush          58
            //  2412: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2415: dup            
            //  2416: aload_0        
            //  2417: sipush          301
            //  2420: ldc_w           "TextSizeHeader"
            //  2423: ldc_w           2131560888
            //  2426: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2429: ldc_w           "textSizeHeaderRow"
            //  2432: ldc_w           "ChatSettings"
            //  2435: ldc_w           2131559043
            //  2438: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2441: ldc_w           2131165574
            //  2444: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$PQPKVfMuNVXvznSLWUi9d_FbGD8;
            //  2447: dup            
            //  2448: aload_0        
            //  2449: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$PQPKVfMuNVXvznSLWUi9d_FbGD8.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2452: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2455: aastore        
            //  2456: dup            
            //  2457: bipush          59
            //  2459: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2462: dup            
            //  2463: aload_0        
            //  2464: sipush          302
            //  2467: ldc_w           "ChatBackground"
            //  2470: ldc_w           2131559024
            //  2473: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2476: ldc_w           "ChatSettings"
            //  2479: ldc_w           2131559043
            //  2482: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2485: ldc_w           2131165574
            //  2488: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$hzumI77K9pwC4a7mUUTUW31jkYs;
            //  2491: dup            
            //  2492: aload_0        
            //  2493: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$hzumI77K9pwC4a7mUUTUW31jkYs.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2496: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2499: aastore        
            //  2500: dup            
            //  2501: bipush          60
            //  2503: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2506: dup            
            //  2507: aload_0        
            //  2508: sipush          303
            //  2511: ldc_w           "SetColor"
            //  2514: ldc_w           2131560733
            //  2517: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2520: aconst_null    
            //  2521: ldc_w           "ChatSettings"
            //  2524: ldc_w           2131559043
            //  2527: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2530: ldc_w           "ChatBackground"
            //  2533: ldc_w           2131559024
            //  2536: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2539: ldc_w           2131165574
            //  2542: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$Ov5OcCfPmPzmx4efoTSoB8_YMKk;
            //  2545: dup            
            //  2546: aload_0        
            //  2547: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$Ov5OcCfPmPzmx4efoTSoB8_YMKk.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2550: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2553: aastore        
            //  2554: dup            
            //  2555: bipush          61
            //  2557: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2560: dup            
            //  2561: aload_0        
            //  2562: sipush          304
            //  2565: ldc_w           "ResetChatBackgrounds"
            //  2568: ldc_w           2131560593
            //  2571: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2574: ldc_w           "resetRow"
            //  2577: ldc_w           "ChatSettings"
            //  2580: ldc_w           2131559043
            //  2583: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2586: ldc_w           "ChatBackground"
            //  2589: ldc_w           2131559024
            //  2592: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2595: ldc_w           2131165574
            //  2598: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$mEajmgrt04XurJBLXFyM6GNHru0;
            //  2601: dup            
            //  2602: aload_0        
            //  2603: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$mEajmgrt04XurJBLXFyM6GNHru0.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2606: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2609: aastore        
            //  2610: dup            
            //  2611: bipush          62
            //  2613: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2616: dup            
            //  2617: aload_0        
            //  2618: sipush          305
            //  2621: ldc_w           "AutoNightTheme"
            //  2624: ldc_w           2131558791
            //  2627: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2630: ldc_w           "ChatSettings"
            //  2633: ldc_w           2131559043
            //  2636: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2639: ldc_w           2131165574
            //  2642: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$vSOlOOSDVPitPhRnpx1o7rNw4zw;
            //  2645: dup            
            //  2646: aload_0        
            //  2647: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$vSOlOOSDVPitPhRnpx1o7rNw4zw.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2650: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2653: aastore        
            //  2654: dup            
            //  2655: bipush          63
            //  2657: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2660: dup            
            //  2661: aload_0        
            //  2662: sipush          306
            //  2665: ldc_w           "ColorTheme"
            //  2668: ldc_w           2131559129
            //  2671: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2674: ldc_w           "themeHeaderRow"
            //  2677: ldc_w           "ChatSettings"
            //  2680: ldc_w           2131559043
            //  2683: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2686: ldc_w           2131165574
            //  2689: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$46GIfyE4OhRvg3Gs4uaPMW_J3UM;
            //  2692: dup            
            //  2693: aload_0        
            //  2694: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$46GIfyE4OhRvg3Gs4uaPMW_J3UM.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2697: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2700: aastore        
            //  2701: dup            
            //  2702: bipush          64
            //  2704: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2707: dup            
            //  2708: aload_0        
            //  2709: sipush          307
            //  2712: ldc_w           "ChromeCustomTabs"
            //  2715: ldc_w           2131559100
            //  2718: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2721: ldc_w           "customTabsRow"
            //  2724: ldc_w           "ChatSettings"
            //  2727: ldc_w           2131559043
            //  2730: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2733: ldc_w           2131165574
            //  2736: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$s0h_sbhuVis6N_TS7j6bEyh7FfQ;
            //  2739: dup            
            //  2740: aload_0        
            //  2741: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$s0h_sbhuVis6N_TS7j6bEyh7FfQ.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2744: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2747: aastore        
            //  2748: dup            
            //  2749: bipush          65
            //  2751: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2754: dup            
            //  2755: aload_0        
            //  2756: sipush          308
            //  2759: ldc_w           "DirectShare"
            //  2762: ldc_w           2131559268
            //  2765: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2768: ldc_w           "directShareRow"
            //  2771: ldc_w           "ChatSettings"
            //  2774: ldc_w           2131559043
            //  2777: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2780: ldc_w           2131165574
            //  2783: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$5_PGzT67poE7m5IzEQLBofn8u_o;
            //  2786: dup            
            //  2787: aload_0        
            //  2788: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$5_PGzT67poE7m5IzEQLBofn8u_o.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2791: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2794: aastore        
            //  2795: dup            
            //  2796: bipush          66
            //  2798: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2801: dup            
            //  2802: aload_0        
            //  2803: sipush          309
            //  2806: ldc_w           "EnableAnimations"
            //  2809: ldc_w           2131559348
            //  2812: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2815: ldc_w           "enableAnimationsRow"
            //  2818: ldc_w           "ChatSettings"
            //  2821: ldc_w           2131559043
            //  2824: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2827: ldc_w           2131165574
            //  2830: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$djYxV0TLWfpCNMAt_hbj8er6rqo;
            //  2833: dup            
            //  2834: aload_0        
            //  2835: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$djYxV0TLWfpCNMAt_hbj8er6rqo.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2838: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2841: aastore        
            //  2842: dup            
            //  2843: bipush          67
            //  2845: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2848: dup            
            //  2849: aload_0        
            //  2850: sipush          310
            //  2853: ldc_w           "RaiseToSpeak"
            //  2856: ldc_w           2131560528
            //  2859: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2862: ldc_w           "raiseToSpeakRow"
            //  2865: ldc_w           "ChatSettings"
            //  2868: ldc_w           2131559043
            //  2871: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2874: ldc_w           2131165574
            //  2877: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$NM0akNEGYlYo_ZnAPY_boq0dLjY;
            //  2880: dup            
            //  2881: aload_0        
            //  2882: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$NM0akNEGYlYo_ZnAPY_boq0dLjY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2885: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2888: aastore        
            //  2889: dup            
            //  2890: bipush          68
            //  2892: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2895: dup            
            //  2896: aload_0        
            //  2897: sipush          311
            //  2900: ldc_w           "SendByEnter"
            //  2903: ldc_w           2131560688
            //  2906: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2909: ldc_w           "sendByEnterRow"
            //  2912: ldc_w           "ChatSettings"
            //  2915: ldc_w           2131559043
            //  2918: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2921: ldc_w           2131165574
            //  2924: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$WOvivT9gT9S9mbWbIsPI_YD8I38;
            //  2927: dup            
            //  2928: aload_0        
            //  2929: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$WOvivT9gT9S9mbWbIsPI_YD8I38.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2932: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2935: aastore        
            //  2936: dup            
            //  2937: bipush          69
            //  2939: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2942: dup            
            //  2943: aload_0        
            //  2944: sipush          312
            //  2947: ldc_w           "SaveToGallerySettings"
            //  2950: ldc_w           2131560631
            //  2953: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2956: ldc_w           "saveToGalleryRow"
            //  2959: ldc_w           "ChatSettings"
            //  2962: ldc_w           2131559043
            //  2965: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  2968: ldc_w           2131165574
            //  2971: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$LE1pN2xBggeckg4BXNGrk8dAfCA;
            //  2974: dup            
            //  2975: aload_0        
            //  2976: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$LE1pN2xBggeckg4BXNGrk8dAfCA.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  2979: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  2982: aastore        
            //  2983: dup            
            //  2984: bipush          70
            //  2986: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  2989: dup            
            //  2990: aload_0        
            //  2991: sipush          313
            //  2994: ldc_w           "StickersAndMasks"
            //  2997: ldc_w           2131560807
            //  3000: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3003: ldc_w           "ChatSettings"
            //  3006: ldc_w           2131559043
            //  3009: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3012: ldc_w           2131165574
            //  3015: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$guZCAugp_k8fJ3Gd84aSwGDA7co;
            //  3018: dup            
            //  3019: aload_0        
            //  3020: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$guZCAugp_k8fJ3Gd84aSwGDA7co.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3023: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3026: aastore        
            //  3027: dup            
            //  3028: bipush          71
            //  3030: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3033: dup            
            //  3034: aload_0        
            //  3035: sipush          314
            //  3038: ldc_w           "SuggestStickers"
            //  3041: ldc_w           2131560843
            //  3044: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3047: ldc_w           "suggestRow"
            //  3050: ldc_w           "ChatSettings"
            //  3053: ldc_w           2131559043
            //  3056: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3059: ldc_w           "StickersAndMasks"
            //  3062: ldc_w           2131560807
            //  3065: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3068: ldc_w           2131165574
            //  3071: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$a2LSsVzKR8HJrNBIEh5xSXFk_kY;
            //  3074: dup            
            //  3075: aload_0        
            //  3076: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$a2LSsVzKR8HJrNBIEh5xSXFk_kY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3079: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3082: aastore        
            //  3083: dup            
            //  3084: bipush          72
            //  3086: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3089: dup            
            //  3090: aload_0        
            //  3091: sipush          315
            //  3094: ldc_w           "FeaturedStickers"
            //  3097: ldc_w           2131559479
            //  3100: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3103: aconst_null    
            //  3104: ldc_w           "ChatSettings"
            //  3107: ldc_w           2131559043
            //  3110: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3113: ldc_w           "StickersAndMasks"
            //  3116: ldc_w           2131560807
            //  3119: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3122: ldc_w           2131165574
            //  3125: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$vTZmp1Lfwd9Jvf16tEr9C2amaYY;
            //  3128: dup            
            //  3129: aload_0        
            //  3130: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$vTZmp1Lfwd9Jvf16tEr9C2amaYY.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3133: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3136: aastore        
            //  3137: dup            
            //  3138: bipush          73
            //  3140: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3143: dup            
            //  3144: aload_0        
            //  3145: sipush          316
            //  3148: ldc_w           "Masks"
            //  3151: ldc_w           2131559809
            //  3154: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3157: aconst_null    
            //  3158: ldc_w           "ChatSettings"
            //  3161: ldc_w           2131559043
            //  3164: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3167: ldc_w           "StickersAndMasks"
            //  3170: ldc_w           2131560807
            //  3173: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3176: ldc_w           2131165574
            //  3179: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$O_sT8FF_ujolqkcXgAFR2EWMCOQ;
            //  3182: dup            
            //  3183: aload_0        
            //  3184: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$O_sT8FF_ujolqkcXgAFR2EWMCOQ.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3187: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3190: aastore        
            //  3191: dup            
            //  3192: bipush          74
            //  3194: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3197: dup            
            //  3198: aload_0        
            //  3199: sipush          317
            //  3202: ldc_w           "ArchivedStickers"
            //  3205: ldc_w           2131558659
            //  3208: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3211: aconst_null    
            //  3212: ldc_w           "ChatSettings"
            //  3215: ldc_w           2131559043
            //  3218: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3221: ldc_w           "StickersAndMasks"
            //  3224: ldc_w           2131560807
            //  3227: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3230: ldc_w           2131165574
            //  3233: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$dg765ZjLDUr492FSgTOKjOaKweE;
            //  3236: dup            
            //  3237: aload_0        
            //  3238: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$dg765ZjLDUr492FSgTOKjOaKweE.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3241: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3244: aastore        
            //  3245: dup            
            //  3246: bipush          75
            //  3248: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3251: dup            
            //  3252: aload_0        
            //  3253: sipush          317
            //  3256: ldc_w           "ArchivedMasks"
            //  3259: ldc_w           2131558654
            //  3262: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3265: aconst_null    
            //  3266: ldc_w           "ChatSettings"
            //  3269: ldc_w           2131559043
            //  3272: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3275: ldc_w           "StickersAndMasks"
            //  3278: ldc_w           2131560807
            //  3281: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3284: ldc_w           2131165574
            //  3287: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$h743V_ShaSJwbDnqAuxpCdWSljA;
            //  3290: dup            
            //  3291: aload_0        
            //  3292: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$h743V_ShaSJwbDnqAuxpCdWSljA.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3295: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3298: aastore        
            //  3299: dup            
            //  3300: bipush          76
            //  3302: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3305: dup            
            //  3306: aload_0        
            //  3307: sipush          400
            //  3310: ldc_w           "Language"
            //  3313: ldc_w           2131559715
            //  3316: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3319: ldc_w           2131165586
            //  3322: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$6umUiixsrbn0nLlRd8bqshWjNeU;
            //  3325: dup            
            //  3326: aload_0        
            //  3327: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$6umUiixsrbn0nLlRd8bqshWjNeU.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3330: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //  3333: aastore        
            //  3334: dup            
            //  3335: bipush          77
            //  3337: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3340: dup            
            //  3341: aload_0        
            //  3342: sipush          401
            //  3345: ldc_w           "SettingsHelp"
            //  3348: ldc_w           2131560740
            //  3351: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3354: ldc_w           2131165582
            //  3357: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$_pkjlu7nZwc3q0jMwngyqVOHNlk;
            //  3360: dup            
            //  3361: aload_0        
            //  3362: getfield        org/telegram/ui/SettingsActivity$SearchAdapter.this$0:Lorg/telegram/ui/SettingsActivity;
            //  3365: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$_pkjlu7nZwc3q0jMwngyqVOHNlk.<init>:(Lorg/telegram/ui/SettingsActivity;)V
            //  3368: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;ILjava/lang/Runnable;)V
            //  3371: aastore        
            //  3372: dup            
            //  3373: bipush          78
            //  3375: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3378: dup            
            //  3379: aload_0        
            //  3380: sipush          402
            //  3383: ldc_w           "AskAQuestion"
            //  3386: ldc_w           2131558706
            //  3389: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3392: ldc_w           "SettingsHelp"
            //  3395: ldc_w           2131560740
            //  3398: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3401: ldc_w           2131165582
            //  3404: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$UKpWGlt__8lNdI9VfZj9_2dBm0k;
            //  3407: dup            
            //  3408: aload_0        
            //  3409: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$UKpWGlt__8lNdI9VfZj9_2dBm0k.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3412: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3415: aastore        
            //  3416: dup            
            //  3417: bipush          79
            //  3419: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3422: dup            
            //  3423: aload_0        
            //  3424: sipush          403
            //  3427: ldc_w           "TelegramFAQ"
            //  3430: ldc_w           2131560869
            //  3433: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3436: ldc_w           "SettingsHelp"
            //  3439: ldc_w           2131560740
            //  3442: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3445: ldc_w           2131165582
            //  3448: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$dYFPJFhuxdXwNU2N5QyWTOm1hz8;
            //  3451: dup            
            //  3452: aload_0        
            //  3453: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$dYFPJFhuxdXwNU2N5QyWTOm1hz8.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3456: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3459: aastore        
            //  3460: dup            
            //  3461: bipush          80
            //  3463: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3466: dup            
            //  3467: aload_0        
            //  3468: sipush          404
            //  3471: ldc_w           "PrivacyPolicy"
            //  3474: ldc_w           2131560502
            //  3477: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3480: ldc_w           "SettingsHelp"
            //  3483: ldc_w           2131560740
            //  3486: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
            //  3489: ldc_w           2131165582
            //  3492: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$98mN5UJETCB_Tj6G_z_MUvkMLLk;
            //  3495: dup            
            //  3496: aload_0        
            //  3497: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$98mN5UJETCB_Tj6G_z_MUvkMLLk.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3500: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;ILjava/lang/String;Ljava/lang/String;ILjava/lang/Runnable;)V
            //  3503: aastore        
            //  3504: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.searchArray:[Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3507: aload_0        
            //  3508: new             Ljava/util/ArrayList;
            //  3511: dup            
            //  3512: invokespecial   java/util/ArrayList.<init>:()V
            //  3515: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.faqSearchArray:Ljava/util/ArrayList;
            //  3518: aload_0        
            //  3519: new             Ljava/util/ArrayList;
            //  3522: dup            
            //  3523: invokespecial   java/util/ArrayList.<init>:()V
            //  3526: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.resultNames:Ljava/util/ArrayList;
            //  3529: aload_0        
            //  3530: new             Ljava/util/ArrayList;
            //  3533: dup            
            //  3534: invokespecial   java/util/ArrayList.<init>:()V
            //  3537: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.searchResults:Ljava/util/ArrayList;
            //  3540: aload_0        
            //  3541: new             Ljava/util/ArrayList;
            //  3544: dup            
            //  3545: invokespecial   java/util/ArrayList.<init>:()V
            //  3548: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.faqSearchResults:Ljava/util/ArrayList;
            //  3551: aload_0        
            //  3552: new             Ljava/util/ArrayList;
            //  3555: dup            
            //  3556: invokespecial   java/util/ArrayList.<init>:()V
            //  3559: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.recentSearches:Ljava/util/ArrayList;
            //  3562: aload_0        
            //  3563: aload_2        
            //  3564: putfield        org/telegram/ui/SettingsActivity$SearchAdapter.mContext:Landroid/content/Context;
            //  3567: new             Ljava/util/HashMap;
            //  3570: dup            
            //  3571: invokespecial   java/util/HashMap.<init>:()V
            //  3574: astore_3       
            //  3575: iconst_0       
            //  3576: istore          4
            //  3578: aload_0        
            //  3579: getfield        org/telegram/ui/SettingsActivity$SearchAdapter.searchArray:[Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3582: astore_1       
            //  3583: iload           4
            //  3585: aload_1        
            //  3586: arraylength    
            //  3587: if_icmpge       3618
            //  3590: aload_3        
            //  3591: aload_1        
            //  3592: iload           4
            //  3594: aaload         
            //  3595: invokestatic    org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.access$3800:(Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;)I
            //  3598: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3601: aload_0        
            //  3602: getfield        org/telegram/ui/SettingsActivity$SearchAdapter.searchArray:[Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3605: iload           4
            //  3607: aaload         
            //  3608: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  3611: pop            
            //  3612: iinc            4, 1
            //  3615: goto            3578
            //  3618: invokestatic    org/telegram/messenger/MessagesController.getGlobalMainSettings:()Landroid/content/SharedPreferences;
            //  3621: ldc_w           "settingsSearchRecent2"
            //  3624: aconst_null    
            //  3625: invokeinterface android/content/SharedPreferences.getStringSet:(Ljava/lang/String;Ljava/util/Set;)Ljava/util/Set;
            //  3630: astore_1       
            //  3631: aload_1        
            //  3632: ifnull          3845
            //  3635: aload_1        
            //  3636: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
            //  3641: astore          5
            //  3643: aload           5
            //  3645: invokeinterface java/util/Iterator.hasNext:()Z
            //  3650: ifeq            3845
            //  3653: aload           5
            //  3655: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //  3660: checkcast       Ljava/lang/String;
            //  3663: astore_1       
            //  3664: new             Lorg/telegram/tgnet/SerializedData;
            //  3667: astore          6
            //  3669: aload           6
            //  3671: aload_1        
            //  3672: invokestatic    org/telegram/messenger/Utilities.hexToBytes:(Ljava/lang/String;)[B
            //  3675: invokespecial   org/telegram/tgnet/SerializedData.<init>:([B)V
            //  3678: aload           6
            //  3680: iconst_0       
            //  3681: invokevirtual   org/telegram/tgnet/SerializedData.readInt32:(Z)I
            //  3684: istore          7
            //  3686: aload           6
            //  3688: iconst_0       
            //  3689: invokevirtual   org/telegram/tgnet/SerializedData.readInt32:(Z)I
            //  3692: istore          4
            //  3694: iload           4
            //  3696: ifne            3799
            //  3699: aload           6
            //  3701: iconst_0       
            //  3702: invokevirtual   org/telegram/tgnet/SerializedData.readString:(Z)Ljava/lang/String;
            //  3705: astore          8
            //  3707: aload           6
            //  3709: iconst_0       
            //  3710: invokevirtual   org/telegram/tgnet/SerializedData.readInt32:(Z)I
            //  3713: istore          9
            //  3715: aconst_null    
            //  3716: astore_1       
            //  3717: iload           9
            //  3719: ifle            3756
            //  3722: iload           9
            //  3724: anewarray       Ljava/lang/String;
            //  3727: astore_2       
            //  3728: iconst_0       
            //  3729: istore          4
            //  3731: aload_2        
            //  3732: astore_1       
            //  3733: iload           4
            //  3735: iload           9
            //  3737: if_icmpge       3756
            //  3740: aload_2        
            //  3741: iload           4
            //  3743: aload           6
            //  3745: iconst_0       
            //  3746: invokevirtual   org/telegram/tgnet/SerializedData.readString:(Z)Ljava/lang/String;
            //  3749: aastore        
            //  3750: iinc            4, 1
            //  3753: goto            3731
            //  3756: aload           6
            //  3758: iconst_0       
            //  3759: invokevirtual   org/telegram/tgnet/SerializedData.readString:(Z)Ljava/lang/String;
            //  3762: astore_2       
            //  3763: new             Lorg/telegram/ui/SettingsActivity$SearchAdapter$FaqSearchResult;
            //  3766: astore          6
            //  3768: aload           6
            //  3770: aload_0        
            //  3771: aload           8
            //  3773: aload_1        
            //  3774: aload_2        
            //  3775: invokespecial   org/telegram/ui/SettingsActivity$SearchAdapter$FaqSearchResult.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)V
            //  3778: aload           6
            //  3780: iload           7
            //  3782: invokestatic    org/telegram/ui/SettingsActivity$SearchAdapter$FaqSearchResult.access$3902:(Lorg/telegram/ui/SettingsActivity$SearchAdapter$FaqSearchResult;I)I
            //  3785: pop            
            //  3786: aload_0        
            //  3787: getfield        org/telegram/ui/SettingsActivity$SearchAdapter.recentSearches:Ljava/util/ArrayList;
            //  3790: aload           6
            //  3792: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //  3795: pop            
            //  3796: goto            3643
            //  3799: iload           4
            //  3801: iconst_1       
            //  3802: if_icmpne       3643
            //  3805: aload_3        
            //  3806: aload           6
            //  3808: iconst_0       
            //  3809: invokevirtual   org/telegram/tgnet/SerializedData.readInt32:(Z)I
            //  3812: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3815: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
            //  3818: checkcast       Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;
            //  3821: astore_1       
            //  3822: aload_1        
            //  3823: ifnull          3643
            //  3826: aload_1        
            //  3827: iload           7
            //  3829: invokestatic    org/telegram/ui/SettingsActivity$SearchAdapter$SearchResult.access$4002:(Lorg/telegram/ui/SettingsActivity$SearchAdapter$SearchResult;I)I
            //  3832: pop            
            //  3833: aload_0        
            //  3834: getfield        org/telegram/ui/SettingsActivity$SearchAdapter.recentSearches:Ljava/util/ArrayList;
            //  3837: aload_1        
            //  3838: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //  3841: pop            
            //  3842: goto            3643
            //  3845: aload_0        
            //  3846: getfield        org/telegram/ui/SettingsActivity$SearchAdapter.recentSearches:Ljava/util/ArrayList;
            //  3849: new             Lorg/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$8ejWl7szf_KNcJH1StMby0vJtdc;
            //  3852: dup            
            //  3853: aload_0        
            //  3854: invokespecial   org/telegram/ui/_$$Lambda$SettingsActivity$SearchAdapter$8ejWl7szf_KNcJH1StMby0vJtdc.<init>:(Lorg/telegram/ui/SettingsActivity$SearchAdapter;)V
            //  3857: invokestatic    java/util/Collections.sort:(Ljava/util/List;Ljava/util/Comparator;)V
            //  3860: return         
            //  3861: astore_1       
            //  3862: goto            3796
            //  3865: astore_1       
            //  3866: goto            3643
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  3664   3694   3861   3865   Ljava/lang/Exception;
            //  3699   3715   3861   3865   Ljava/lang/Exception;
            //  3722   3728   3861   3865   Ljava/lang/Exception;
            //  3740   3750   3861   3865   Ljava/lang/Exception;
            //  3756   3796   3861   3865   Ljava/lang/Exception;
            //  3805   3822   3865   3869   Ljava/lang/Exception;
            //  3826   3842   3865   3869   Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_3845:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        private int getNum(final Object o) {
            if (o instanceof SearchResult) {
                return ((SearchResult)o).num;
            }
            if (o instanceof FaqSearchResult) {
                return ((FaqSearchResult)o).num;
            }
            return 0;
        }
        
        private void loadFaqWebPage() {
            if (this.faqWebPage == null) {
                if (!this.loadingFaqPage) {
                    this.loadingFaqPage = true;
                    final TLRPC.TL_messages_getWebPage tl_messages_getWebPage = new TLRPC.TL_messages_getWebPage();
                    tl_messages_getWebPage.url = LocaleController.getString("TelegramFaqUrl", 2131560871);
                    tl_messages_getWebPage.hash = 0;
                    ConnectionsManager.getInstance(this.this$0.currentAccount).sendRequest(tl_messages_getWebPage, new _$$Lambda$SettingsActivity$SearchAdapter$mPyLXiUd_cjxJI7NTOZqkYVtkUM(this));
                }
            }
        }
        
        public void addRecent(Object o) {
            final int index = this.recentSearches.indexOf(o);
            if (index >= 0) {
                this.recentSearches.remove(index);
            }
            final ArrayList<Object> recentSearches = this.recentSearches;
            int i = 0;
            recentSearches.add(0, o);
            if (!this.searchWas) {
                this.notifyDataSetChanged();
            }
            if (this.recentSearches.size() > 20) {
                final ArrayList<Object> recentSearches2 = this.recentSearches;
                recentSearches2.remove(recentSearches2.size() - 1);
            }
            o = new LinkedHashSet();
            while (i < this.recentSearches.size()) {
                final Object value = this.recentSearches.get(i);
                if (value instanceof SearchResult) {
                    ((SearchResult)value).num = i;
                }
                else if (value instanceof FaqSearchResult) {
                    ((FaqSearchResult)value).num = i;
                }
                ((HashSet<String>)o).add(value.toString());
                ++i;
            }
            MessagesController.getGlobalMainSettings().edit().putStringSet("settingsSearchRecent2", (Set)o).commit();
        }
        
        public void clearRecent() {
            this.recentSearches.clear();
            MessagesController.getGlobalMainSettings().edit().remove("settingsSearchRecent2").commit();
            this.notifyDataSetChanged();
        }
        
        @Override
        public int getItemCount() {
            final boolean searchWas = this.searchWas;
            final int n = 0;
            int n2 = 0;
            if (searchWas) {
                final int size = this.searchResults.size();
                if (!this.faqSearchResults.isEmpty()) {
                    n2 = this.faqSearchResults.size() + 1;
                }
                return size + n2;
            }
            int n3;
            if (this.recentSearches.isEmpty()) {
                n3 = n;
            }
            else {
                n3 = this.recentSearches.size() + 1;
            }
            return n3;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (this.searchWas) {
                if (n < this.searchResults.size()) {
                    return 0;
                }
                if (n == this.searchResults.size()) {
                    return 1;
                }
                return 0;
            }
            else {
                if (n == 0) {
                    return 2;
                }
                return 0;
            }
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = true;
            final boolean b2 = true;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        ((HeaderCell)viewHolder.itemView).setText(LocaleController.getString("SettingsRecent", 2131560743));
                    }
                }
                else {
                    ((GraySectionCell)viewHolder.itemView).setText(LocaleController.getString("SettingsFaqSearchTitle", 2131560739));
                }
            }
            else {
                final SettingsSearchCell settingsSearchCell = (SettingsSearchCell)viewHolder.itemView;
                final boolean searchWas = this.searchWas;
                final boolean b3 = false;
                boolean b4 = false;
                if (searchWas) {
                    if (n < this.searchResults.size()) {
                        final SearchResult searchResult = this.searchResults.get(n);
                        SearchResult searchResult2;
                        if (n > 0) {
                            searchResult2 = this.searchResults.get(n - 1);
                        }
                        else {
                            searchResult2 = null;
                        }
                        int access$4200;
                        if (searchResult2 != null && searchResult2.iconResId == searchResult.iconResId) {
                            access$4200 = 0;
                        }
                        else {
                            access$4200 = searchResult.iconResId;
                        }
                        settingsSearchCell.setTextAndValueAndIcon(this.resultNames.get(n), searchResult.path, access$4200, n < this.searchResults.size() - 1 && b2);
                    }
                    else {
                        n -= this.searchResults.size() + 1;
                        final FaqSearchResult faqSearchResult = this.faqSearchResults.get(n);
                        final CharSequence charSequence = this.resultNames.get(this.searchResults.size() + n);
                        final String[] access$4201 = faqSearchResult.path;
                        if (n < this.searchResults.size() - 1) {
                            b4 = true;
                        }
                        settingsSearchCell.setTextAndValue(charSequence, access$4201, true, b4);
                    }
                }
                else {
                    --n;
                    final FaqSearchResult value = this.recentSearches.get(n);
                    if (value instanceof SearchResult) {
                        final SearchResult searchResult3 = (SearchResult)value;
                        settingsSearchCell.setTextAndValue(searchResult3.searchTitle, searchResult3.path, false, n < this.recentSearches.size() - 1 && b);
                    }
                    else if (value instanceof FaqSearchResult) {
                        final FaqSearchResult faqSearchResult2 = value;
                        final String access$4202 = faqSearchResult2.title;
                        final String[] access$4203 = faqSearchResult2.path;
                        boolean b5 = b3;
                        if (n < this.recentSearches.size() - 1) {
                            b5 = true;
                        }
                        settingsSearchCell.setTextAndValue(access$4202, access$4203, true, b5);
                    }
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    frameLayout = new HeaderCell(this.mContext, 16);
                }
                else {
                    frameLayout = new GraySectionCell(this.mContext);
                }
            }
            else {
                frameLayout = new SettingsSearchCell(this.mContext);
            }
            ((View)frameLayout).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)frameLayout);
        }
        
        public void search(final String lastSearchString) {
            this.lastSearchString = lastSearchString;
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty((CharSequence)lastSearchString)) {
                this.searchWas = false;
                this.searchResults.clear();
                this.faqSearchResults.clear();
                this.resultNames.clear();
                this.this$0.emptyView.setTopImage(0);
                this.this$0.emptyView.setText(LocaleController.getString("SettingsNoRecent", 2131560741));
                this.notifyDataSetChanged();
                return;
            }
            Utilities.searchQueue.postRunnable(this.searchRunnable = new _$$Lambda$SettingsActivity$SearchAdapter$2i3P8i9DdS78PBZMuonLkeibTUg(this, lastSearchString), 300L);
        }
        
        private class FaqSearchResult
        {
            private int num;
            private String[] path;
            private String title;
            private String url;
            
            public FaqSearchResult(final String title, final String[] path, final String url) {
                this.title = title;
                this.path = path;
                this.url = url;
            }
            
            @Override
            public boolean equals(final Object o) {
                return o instanceof FaqSearchResult && this.title.equals(((FaqSearchResult)o).title);
            }
            
            @Override
            public String toString() {
                final SerializedData serializedData = new SerializedData();
                serializedData.writeInt32(this.num);
                final int n = 0;
                serializedData.writeInt32(0);
                serializedData.writeString(this.title);
                final String[] path = this.path;
                int length;
                if (path != null) {
                    length = path.length;
                }
                else {
                    length = 0;
                }
                serializedData.writeInt32(length);
                if (this.path != null) {
                    int n2 = n;
                    while (true) {
                        final String[] path2 = this.path;
                        if (n2 >= path2.length) {
                            break;
                        }
                        serializedData.writeString(path2[n2]);
                        ++n2;
                    }
                }
                serializedData.writeString(this.url);
                return Utilities.bytesToHex(serializedData.toByteArray());
            }
        }
        
        private class SearchResult
        {
            private int guid;
            private int iconResId;
            private int num;
            private Runnable openRunnable;
            private String[] path;
            private String rowName;
            private String searchTitle;
            
            public SearchResult(final SearchAdapter searchAdapter, final int n, final String s, final int n2, final Runnable runnable) {
                this(searchAdapter, n, s, null, null, null, n2, runnable);
            }
            
            public SearchResult(final SearchAdapter searchAdapter, final int n, final String s, final String s2, final int n2, final Runnable runnable) {
                this(searchAdapter, n, s, null, s2, null, n2, runnable);
            }
            
            public SearchResult(final SearchAdapter searchAdapter, final int n, final String s, final String s2, final String s3, final int n2, final Runnable runnable) {
                this(searchAdapter, n, s, s2, s3, null, n2, runnable);
            }
            
            public SearchResult(final int guid, final String searchTitle, final String rowName, final String s, final String s2, final int iconResId, final Runnable openRunnable) {
                this.guid = guid;
                this.searchTitle = searchTitle;
                this.rowName = rowName;
                this.openRunnable = openRunnable;
                this.iconResId = iconResId;
                if (s != null && s2 != null) {
                    this.path = new String[] { s, s2 };
                }
                else if (s != null) {
                    this.path = new String[] { s };
                }
            }
            
            private void open() {
                this.openRunnable.run();
                if (this.rowName == null) {
                    return;
                }
                final BaseFragment obj = SearchAdapter.this.this$0.parentLayout.fragmentsStack.get(SearchAdapter.this.this$0.parentLayout.fragmentsStack.size() - 1);
                try {
                    final Field declaredField = obj.getClass().getDeclaredField("listView");
                    declaredField.setAccessible(true);
                    ((RecyclerListView)declaredField.get(obj)).highlightRow((RecyclerListView.IntReturnCallback)new _$$Lambda$SettingsActivity$SearchAdapter$SearchResult$QTQolgPlTMmAnvlmOMDH38UI6H4(this, obj));
                    declaredField.setAccessible(false);
                }
                catch (Throwable t) {}
            }
            
            @Override
            public boolean equals(final Object o) {
                final boolean b = o instanceof SearchResult;
                boolean b2 = false;
                if (!b) {
                    return false;
                }
                if (this.guid == ((SearchResult)o).guid) {
                    b2 = true;
                }
                return b2;
            }
            
            @Override
            public String toString() {
                final SerializedData serializedData = new SerializedData();
                serializedData.writeInt32(this.num);
                serializedData.writeInt32(1);
                serializedData.writeInt32(this.guid);
                return Utilities.bytesToHex(serializedData.toByteArray());
            }
        }
    }
}
