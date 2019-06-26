// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.util.AttributeSet;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.MediaController;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import android.os.PowerManager;
import android.text.TextUtils$TruncateAt;
import android.view.View$MeasureSpec;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.RoundStatusDrawable;
import org.telegram.ui.Components.PlayingGameDrawable;
import org.telegram.ui.Components.SendingFileDrawable;
import org.telegram.ui.Components.RecordStatusDrawable;
import org.telegram.ui.Components.TypingDotsDrawable;
import android.os.Bundle;
import android.content.res.Configuration;
import android.net.Uri;
import android.content.DialogInterface;
import org.telegram.messenger.LocaleController;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.SendMessagesHelper;
import android.view.MotionEvent;
import org.telegram.messenger.ApplicationLoader;
import android.app.KeyguardManager;
import java.util.Collection;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.UserConfig;
import android.content.Intent;
import android.widget.ScrollView;
import org.telegram.ui.Components.PopupAudioView;
import org.telegram.messenger.WebFile;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import android.widget.RelativeLayout$LayoutParams;
import android.view.View$OnClickListener;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View$OnTouchListener;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.LinearLayout;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MessagesController;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.os.PowerManager$WakeLock;
import android.view.VelocityTracker;
import org.telegram.ui.Components.StatusDrawable;
import android.widget.RelativeLayout;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import android.widget.TextView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.BackupImageView;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.NotificationCenter;
import android.app.Activity;

public class PopupNotificationActivity extends Activity implements NotificationCenterDelegate
{
    private static final int id_chat_compose_panel = 1000;
    private ActionBar actionBar;
    private boolean animationInProgress;
    private long animationStartTime;
    private ArrayList<ViewGroup> audioViews;
    private FrameLayout avatarContainer;
    private BackupImageView avatarImageView;
    private ViewGroup centerButtonsView;
    private ViewGroup centerView;
    private ChatActivityEnterView chatActivityEnterView;
    private int classGuid;
    private TextView countText;
    private TLRPC.Chat currentChat;
    private int currentMessageNum;
    private MessageObject currentMessageObject;
    private TLRPC.User currentUser;
    private boolean finished;
    private ArrayList<ViewGroup> imageViews;
    private boolean isReply;
    private CharSequence lastPrintString;
    private int lastResumedAccount;
    private ViewGroup leftButtonsView;
    private ViewGroup leftView;
    private ViewGroup messageContainer;
    private float moveStartX;
    private TextView nameTextView;
    private Runnable onAnimationEndRunnable;
    private TextView onlineTextView;
    private RelativeLayout popupContainer;
    private ArrayList<MessageObject> popupMessages;
    private ViewGroup rightButtonsView;
    private ViewGroup rightView;
    private boolean startedMoving;
    private StatusDrawable[] statusDrawables;
    private ArrayList<ViewGroup> textViews;
    private VelocityTracker velocityTracker;
    private PowerManager$WakeLock wakeLock;
    
    public PopupNotificationActivity() {
        this.textViews = new ArrayList<ViewGroup>();
        this.imageViews = new ArrayList<ViewGroup>();
        this.audioViews = new ArrayList<ViewGroup>();
        this.velocityTracker = null;
        this.statusDrawables = new StatusDrawable[5];
        this.lastResumedAccount = -1;
        this.finished = false;
        this.currentMessageObject = null;
        this.currentMessageNum = 0;
        this.wakeLock = null;
        this.animationInProgress = false;
        this.animationStartTime = 0L;
        this.moveStartX = -1.0f;
        this.startedMoving = false;
        this.onAnimationEndRunnable = null;
        this.popupMessages = new ArrayList<MessageObject>();
    }
    
    private void applyViewsLayoutParams(final int n) {
        final int width = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
        final ViewGroup leftView = this.leftView;
        if (leftView != null) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)leftView.getLayoutParams();
            if (layoutParams.width != width) {
                layoutParams.width = width;
                this.leftView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            }
            this.leftView.setTranslationX((float)(-width + n));
        }
        final ViewGroup leftButtonsView = this.leftButtonsView;
        if (leftButtonsView != null) {
            leftButtonsView.setTranslationX((float)(-width + n));
        }
        final ViewGroup centerView = this.centerView;
        if (centerView != null) {
            final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)centerView.getLayoutParams();
            if (layoutParams2.width != width) {
                layoutParams2.width = width;
                this.centerView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            this.centerView.setTranslationX((float)n);
        }
        final ViewGroup centerButtonsView = this.centerButtonsView;
        if (centerButtonsView != null) {
            centerButtonsView.setTranslationX((float)n);
        }
        final ViewGroup rightView = this.rightView;
        if (rightView != null) {
            final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)rightView.getLayoutParams();
            if (layoutParams3.width != width) {
                layoutParams3.width = width;
                this.rightView.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
            }
            this.rightView.setTranslationX((float)(width + n));
        }
        final ViewGroup rightButtonsView = this.rightButtonsView;
        if (rightButtonsView != null) {
            rightButtonsView.setTranslationX((float)(width + n));
        }
        this.messageContainer.invalidate();
    }
    
    private void checkAndUpdateAvatar() {
        final MessageObject currentMessageObject = this.currentMessageObject;
        if (currentMessageObject == null) {
            return;
        }
        if (this.currentChat != null) {
            final TLRPC.Chat chat = MessagesController.getInstance(currentMessageObject.currentAccount).getChat(this.currentChat.id);
            if (chat == null) {
                return;
            }
            this.currentChat = chat;
            if (this.avatarImageView != null) {
                this.avatarImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", new AvatarDrawable(this.currentChat), chat);
            }
        }
        else if (this.currentUser != null) {
            final TLRPC.User user = MessagesController.getInstance(currentMessageObject.currentAccount).getUser(this.currentUser.id);
            if (user == null) {
                return;
            }
            this.currentUser = user;
            if (this.avatarImageView != null) {
                this.avatarImageView.setImage(ImageLocation.getForUser(user, false), "50_50", new AvatarDrawable(this.currentUser), user);
            }
        }
    }
    
    private void fixLayout() {
        final FrameLayout avatarContainer = this.avatarContainer;
        if (avatarContainer != null) {
            avatarContainer.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (PopupNotificationActivity.this.avatarContainer != null) {
                        PopupNotificationActivity.this.avatarContainer.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    }
                    final int n = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0f)) / 2;
                    PopupNotificationActivity.this.avatarContainer.setPadding(PopupNotificationActivity.this.avatarContainer.getPaddingLeft(), n, PopupNotificationActivity.this.avatarContainer.getPaddingRight(), n);
                    return true;
                }
            });
        }
        final ViewGroup messageContainer = this.messageContainer;
        if (messageContainer != null) {
            messageContainer.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    PopupNotificationActivity.this.messageContainer.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                    if (!PopupNotificationActivity.this.checkTransitionAnimation() && !PopupNotificationActivity.this.startedMoving) {
                        final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)PopupNotificationActivity.this.messageContainer.getLayoutParams();
                        layoutParams.topMargin = ActionBar.getCurrentActionBarHeight();
                        layoutParams.bottomMargin = AndroidUtilities.dp(48.0f);
                        layoutParams.width = -1;
                        layoutParams.height = -1;
                        PopupNotificationActivity.this.messageContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                        PopupNotificationActivity.this.applyViewsLayoutParams(0);
                    }
                    return true;
                }
            });
        }
    }
    
    private LinearLayout getButtonsViewForMessage(int i, final boolean b) {
        final int size = this.popupMessages.size();
        Object o = null;
        if (size == 1 && (i < 0 || i >= this.popupMessages.size())) {
            return null;
        }
        int index;
        if (i == -1) {
            index = this.popupMessages.size() - 1;
        }
        else if ((index = i) == this.popupMessages.size()) {
            index = 0;
        }
        final MessageObject messageObject = this.popupMessages.get(index);
        final TLRPC.ReplyMarkup reply_markup = messageObject.messageOwner.reply_markup;
        int n;
        if (messageObject.getDialogId() == 777000L && reply_markup != null) {
            final ArrayList<TLRPC.TL_keyboardButtonRow> rows = reply_markup.rows;
            final int size2 = rows.size();
            int index2 = 0;
            i = 0;
            while (true) {
                n = i;
                if (index2 >= size2) {
                    break;
                }
                final TLRPC.TL_keyboardButtonRow tl_keyboardButtonRow = rows.get(index2);
                int n2;
                for (int size3 = tl_keyboardButtonRow.buttons.size(), j = 0; j < size3; ++j, i = n2) {
                    n2 = i;
                    if (tl_keyboardButtonRow.buttons.get(j) instanceof TLRPC.TL_keyboardButtonCallback) {
                        n2 = i + 1;
                    }
                }
                ++index2;
            }
        }
        else {
            n = 0;
        }
        final int currentAccount = messageObject.currentAccount;
        if (n > 0) {
            final ArrayList<TLRPC.TL_keyboardButtonRow> rows2 = reply_markup.rows;
            final int size4 = rows2.size();
            o = null;
            TLRPC.TL_keyboardButtonRow tl_keyboardButtonRow2;
            int size5;
            int k;
            TLRPC.KeyboardButton tag;
            Object o2;
            TextView textView;
            for (i = 0; i < size4; ++i) {
                tl_keyboardButtonRow2 = rows2.get(i);
                for (size5 = tl_keyboardButtonRow2.buttons.size(), k = 0; k < size5; ++k, o = o2) {
                    tag = tl_keyboardButtonRow2.buttons.get(k);
                    o2 = o;
                    if (tag instanceof TLRPC.TL_keyboardButtonCallback) {
                        if ((o2 = o) == null) {
                            o2 = new LinearLayout((Context)this);
                            ((LinearLayout)o2).setOrientation(0);
                            ((LinearLayout)o2).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                            ((LinearLayout)o2).setWeightSum(100.0f);
                            ((LinearLayout)o2).setTag((Object)"b");
                            ((LinearLayout)o2).setOnTouchListener((View$OnTouchListener)_$$Lambda$PopupNotificationActivity$XvlaP2ODWCCStorSQi9nplxzY4s.INSTANCE);
                        }
                        textView = new TextView((Context)this);
                        textView.setTextSize(1, 16.0f);
                        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText"));
                        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textView.setText((CharSequence)tag.text.toUpperCase());
                        textView.setTag((Object)tag);
                        textView.setGravity(17);
                        textView.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        ((LinearLayout)o2).addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -1, 100.0f / n));
                        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$PopupNotificationActivity$ox3mIPlvmBDmNDp_7DLxqyRSnLI(currentAccount, messageObject));
                    }
                }
            }
        }
        if (o != null) {
            final int n3 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams(-1, -2);
            relativeLayout$LayoutParams.addRule(12);
            if (b) {
                i = this.currentMessageNum;
                if (index == i) {
                    ((LinearLayout)o).setTranslationX(0.0f);
                }
                else if (index == i - 1) {
                    ((LinearLayout)o).setTranslationX((float)(-n3));
                }
                else if (index == i + 1) {
                    ((LinearLayout)o).setTranslationX((float)n3);
                }
            }
            this.popupContainer.addView((View)o, (ViewGroup$LayoutParams)relativeLayout$LayoutParams);
        }
        return (LinearLayout)o;
    }
    
    private void getNewMessage() {
        if (this.popupMessages.isEmpty()) {
            this.onFinish();
            this.finish();
            return;
        }
        boolean b = false;
        Label_0138: {
            if ((this.currentMessageNum != 0 || this.chatActivityEnterView.hasText() || this.startedMoving) && this.currentMessageObject != null) {
                for (int size = this.popupMessages.size(), i = 0; i < size; ++i) {
                    final MessageObject messageObject = this.popupMessages.get(i);
                    if (messageObject.currentAccount == this.currentMessageObject.currentAccount && messageObject.getDialogId() == this.currentMessageObject.getDialogId() && messageObject.getId() == this.currentMessageObject.getId()) {
                        this.currentMessageNum = i;
                        b = true;
                        break Label_0138;
                    }
                }
            }
            b = false;
        }
        if (!b) {
            this.currentMessageNum = 0;
            this.currentMessageObject = this.popupMessages.get(0);
            this.updateInterfaceForCurrentMessage(0);
        }
        else if (this.startedMoving) {
            if (this.currentMessageNum == this.popupMessages.size() - 1) {
                this.prepareLayouts(3);
            }
            else if (this.currentMessageNum == 1) {
                this.prepareLayouts(4);
            }
        }
        this.countText.setText((CharSequence)String.format("%d/%d", this.currentMessageNum + 1, this.popupMessages.size()));
    }
    
    private ViewGroup getViewForMessage(int index, final boolean b) {
        final int n = index;
        if (this.popupMessages.size() == 1 && (n < 0 || n >= this.popupMessages.size())) {
            return null;
        }
        if (n == -1) {
            index = this.popupMessages.size() - 1;
        }
        else if ((index = n) == this.popupMessages.size()) {
            index = 0;
        }
        final MessageObject messageObject = this.popupMessages.get(index);
        final int type = messageObject.type;
        Object o2;
        if ((type == 1 || type == 4) && !messageObject.isSecretMedia()) {
            Object o;
            if (this.imageViews.size() > 0) {
                o = this.imageViews.get(0);
                this.imageViews.remove(0);
            }
            else {
                o = new FrameLayout((Context)this);
                final FrameLayout frameLayout = new FrameLayout((Context)this);
                frameLayout.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
                frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                ((ViewGroup)o).addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                final BackupImageView backupImageView = new BackupImageView((Context)this);
                backupImageView.setTag((Object)311);
                frameLayout.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                final TextView textView = new TextView((Context)this);
                textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                textView.setTextSize(1, 16.0f);
                textView.setGravity(17);
                textView.setTag((Object)312);
                frameLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 17));
                ((ViewGroup)o).setTag((Object)2);
                ((ViewGroup)o).setOnClickListener((View$OnClickListener)new _$$Lambda$PopupNotificationActivity$yXN7dQz6jZF2SRmRmEwBYh62Ap0(this));
            }
            final ViewGroup viewGroup = (ViewGroup)o;
            final TextView textView2 = (TextView)viewGroup.findViewWithTag((Object)312);
            final BackupImageView backupImageView2 = (BackupImageView)viewGroup.findViewWithTag((Object)311);
            backupImageView2.setAspectFit(true);
            final int type2 = messageObject.type;
            if (type2 == 1) {
                final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                final TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 100);
                boolean b3 = false;
                Label_0544: {
                    Label_0542: {
                        if (closestPhotoSizeWithSize != null) {
                            final boolean b2 = messageObject.type != 1 || FileLoader.getPathToMessage(messageObject.messageOwner).exists();
                            if (!messageObject.needDrawBluredPreview()) {
                                if (!b2 && !DownloadController.getInstance(messageObject.currentAccount).canDownloadMedia(messageObject)) {
                                    if (closestPhotoSizeWithSize2 == null) {
                                        break Label_0542;
                                    }
                                    backupImageView2.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject), "100_100_b", null, null, messageObject);
                                }
                                else {
                                    backupImageView2.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject), "100_100", ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject), "100_100_b", closestPhotoSizeWithSize.size, messageObject);
                                }
                                b3 = true;
                                break Label_0544;
                            }
                        }
                    }
                    b3 = false;
                }
                if (!b3) {
                    backupImageView2.setVisibility(8);
                    textView2.setVisibility(0);
                    textView2.setTextSize(2, (float)SharedConfig.fontSize);
                    textView2.setText(messageObject.messageText);
                    o2 = viewGroup;
                }
                else {
                    backupImageView2.setVisibility(0);
                    textView2.setVisibility(8);
                    o2 = viewGroup;
                }
            }
            else {
                o2 = viewGroup;
                if (type2 == 4) {
                    textView2.setVisibility(8);
                    textView2.setText(messageObject.messageText);
                    backupImageView2.setVisibility(0);
                    final TLRPC.GeoPoint geo = messageObject.messageOwner.media.geo;
                    final double lat = geo.lat;
                    final double long1 = geo._long;
                    if (MessagesController.getInstance(messageObject.currentAccount).mapProvider == 2) {
                        backupImageView2.setImage(ImageLocation.getForWebFile(WebFile.createWithGeoPoint(geo, 100, 100, 15, Math.min(2, (int)Math.ceil(AndroidUtilities.density)))), null, null, null, messageObject);
                        o2 = viewGroup;
                    }
                    else {
                        backupImageView2.setImage(AndroidUtilities.formapMapUrl(messageObject.currentAccount, lat, long1, 100, 100, true, 15), null, null);
                        o2 = viewGroup;
                    }
                }
            }
        }
        else if (messageObject.type == 2) {
            Object o3;
            PopupAudioView popupAudioView;
            if (this.audioViews.size() > 0) {
                o3 = this.audioViews.get(0);
                this.audioViews.remove(0);
                popupAudioView = (PopupAudioView)((ViewGroup)o3).findViewWithTag((Object)300);
            }
            else {
                o3 = new FrameLayout((Context)this);
                final FrameLayout frameLayout2 = new FrameLayout((Context)this);
                frameLayout2.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
                frameLayout2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                ((ViewGroup)o3).addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                final FrameLayout frameLayout3 = new FrameLayout((Context)this);
                frameLayout2.addView((View)frameLayout3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 17, 20.0f, 0.0f, 20.0f, 0.0f));
                popupAudioView = new PopupAudioView((Context)this);
                popupAudioView.setTag((Object)300);
                frameLayout3.addView((View)popupAudioView);
                ((ViewGroup)o3).setTag((Object)3);
                ((ViewGroup)o3).setOnClickListener((View$OnClickListener)new _$$Lambda$PopupNotificationActivity$VFWwjWrjLI64daw5erAQKADNXUs(this));
            }
            popupAudioView.setMessageObject(messageObject);
            o2 = o3;
            if (DownloadController.getInstance(messageObject.currentAccount).canDownloadMedia(messageObject)) {
                popupAudioView.downloadAudioIfNeed();
                o2 = o3;
            }
        }
        else {
            if (this.textViews.size() > 0) {
                o2 = this.textViews.get(0);
                this.textViews.remove(0);
            }
            else {
                o2 = new FrameLayout((Context)this);
                final ScrollView scrollView = new ScrollView((Context)this);
                scrollView.setFillViewport(true);
                ((ViewGroup)o2).addView((View)scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                final LinearLayout linearLayout = new LinearLayout((Context)this);
                linearLayout.setOrientation(0);
                linearLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                scrollView.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createScroll(-1, -2, 1));
                linearLayout.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
                linearLayout.setOnClickListener((View$OnClickListener)new _$$Lambda$PopupNotificationActivity$1_iHFPQDV_CYBmOOqbZFgFmuyU8(this));
                final TextView textView3 = new TextView((Context)this);
                textView3.setTextSize(1, 16.0f);
                textView3.setTag((Object)301);
                textView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                textView3.setLinkTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                textView3.setGravity(17);
                linearLayout.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 17));
                ((ViewGroup)o2).setTag((Object)1);
            }
            final TextView textView4 = (TextView)((ViewGroup)o2).findViewWithTag((Object)301);
            textView4.setTextSize(2, (float)SharedConfig.fontSize);
            textView4.setText(messageObject.messageText);
        }
        if (((ViewGroup)o2).getParent() == null) {
            this.messageContainer.addView((View)o2);
        }
        ((ViewGroup)o2).setVisibility(0);
        if (b) {
            final int width = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)((ViewGroup)o2).getLayoutParams();
            layoutParams.gravity = 51;
            layoutParams.height = -1;
            layoutParams.width = width;
            final int currentMessageNum = this.currentMessageNum;
            if (index == currentMessageNum) {
                ((ViewGroup)o2).setTranslationX(0.0f);
            }
            else if (index == currentMessageNum - 1) {
                ((ViewGroup)o2).setTranslationX((float)(-width));
            }
            else if (index == currentMessageNum + 1) {
                ((ViewGroup)o2).setTranslationX((float)width);
            }
            ((ViewGroup)o2).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            ((ViewGroup)o2).invalidate();
        }
        return (ViewGroup)o2;
    }
    
    private void handleIntent(final Intent intent) {
        this.isReply = (intent != null && intent.getBooleanExtra("force", false));
        this.popupMessages.clear();
        if (this.isReply) {
            int n;
            if (intent != null) {
                n = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
            }
            else {
                n = UserConfig.selectedAccount;
            }
            this.popupMessages.addAll(NotificationsController.getInstance(n).popupReplyMessages);
        }
        else {
            for (int i = 0; i < 3; ++i) {
                if (UserConfig.getInstance(i).isClientActivated()) {
                    this.popupMessages.addAll(NotificationsController.getInstance(i).popupMessages);
                }
            }
        }
        if (!((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode() && ApplicationLoader.isScreenOn) {
            this.getWindow().addFlags(2623488);
            this.getWindow().clearFlags(2);
        }
        else {
            this.getWindow().addFlags(2623490);
        }
        if (this.currentMessageObject == null) {
            this.currentMessageNum = 0;
        }
        this.getNewMessage();
    }
    
    private void openCurrentMessage() {
        if (this.currentMessageObject == null) {
            return;
        }
        final Intent intent = new Intent(ApplicationLoader.applicationContext, (Class)LaunchActivity.class);
        final long dialogId = this.currentMessageObject.getDialogId();
        final int n = (int)dialogId;
        if (n != 0) {
            if (n < 0) {
                intent.putExtra("chatId", -n);
            }
            else {
                intent.putExtra("userId", n);
            }
        }
        else {
            intent.putExtra("encId", (int)(dialogId >> 32));
        }
        intent.putExtra("currentAccount", this.currentMessageObject.currentAccount);
        final StringBuilder sb = new StringBuilder();
        sb.append("com.tmessages.openchat");
        sb.append(Math.random());
        sb.append(Integer.MAX_VALUE);
        intent.setAction(sb.toString());
        intent.setFlags(32768);
        this.startActivity(intent);
        this.onFinish();
        this.finish();
    }
    
    private void prepareLayouts(int n) {
        final int n2 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
        if (n == 0) {
            this.reuseView(this.centerView);
            this.reuseView(this.leftView);
            this.reuseView(this.rightView);
            this.reuseButtonsView(this.centerButtonsView);
            this.reuseButtonsView(this.leftButtonsView);
            this.reuseButtonsView(this.rightButtonsView);
            n = this.currentMessageNum - 1;
            while (true) {
                final int currentMessageNum = this.currentMessageNum;
                if (n >= currentMessageNum + 2) {
                    break;
                }
                if (n == currentMessageNum - 1) {
                    this.leftView = this.getViewForMessage(n, true);
                    this.leftButtonsView = (ViewGroup)this.getButtonsViewForMessage(n, true);
                }
                else if (n == currentMessageNum) {
                    this.centerView = this.getViewForMessage(n, true);
                    this.centerButtonsView = (ViewGroup)this.getButtonsViewForMessage(n, true);
                }
                else if (n == currentMessageNum + 1) {
                    this.rightView = this.getViewForMessage(n, true);
                    this.rightButtonsView = (ViewGroup)this.getButtonsViewForMessage(n, true);
                }
                ++n;
            }
        }
        else if (n == 1) {
            this.reuseView(this.rightView);
            this.reuseButtonsView(this.rightButtonsView);
            this.rightView = this.centerView;
            this.centerView = this.leftView;
            this.leftView = this.getViewForMessage(this.currentMessageNum - 1, true);
            this.rightButtonsView = this.centerButtonsView;
            this.centerButtonsView = this.leftButtonsView;
            this.leftButtonsView = (ViewGroup)this.getButtonsViewForMessage(this.currentMessageNum - 1, true);
        }
        else if (n == 2) {
            this.reuseView(this.leftView);
            this.reuseButtonsView(this.leftButtonsView);
            this.leftView = this.centerView;
            this.centerView = this.rightView;
            this.rightView = this.getViewForMessage(this.currentMessageNum + 1, true);
            this.leftButtonsView = this.centerButtonsView;
            this.centerButtonsView = this.rightButtonsView;
            this.rightButtonsView = (ViewGroup)this.getButtonsViewForMessage(this.currentMessageNum + 1, true);
        }
        else if (n == 3) {
            final ViewGroup rightView = this.rightView;
            if (rightView != null) {
                final float translationX = rightView.getTranslationX();
                this.reuseView(this.rightView);
                if ((this.rightView = this.getViewForMessage(this.currentMessageNum + 1, false)) != null) {
                    final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.rightView.getLayoutParams();
                    layoutParams.width = n2;
                    this.rightView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.rightView.setTranslationX(translationX);
                    this.rightView.invalidate();
                }
            }
            final ViewGroup rightButtonsView = this.rightButtonsView;
            if (rightButtonsView != null) {
                final float translationX2 = rightButtonsView.getTranslationX();
                this.reuseButtonsView(this.rightButtonsView);
                if ((this.rightButtonsView = (ViewGroup)this.getButtonsViewForMessage(this.currentMessageNum + 1, false)) != null) {
                    this.rightButtonsView.setTranslationX(translationX2);
                }
            }
        }
        else if (n == 4) {
            final ViewGroup leftView = this.leftView;
            if (leftView != null) {
                final float translationX3 = leftView.getTranslationX();
                this.reuseView(this.leftView);
                if ((this.leftView = this.getViewForMessage(0, false)) != null) {
                    final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.leftView.getLayoutParams();
                    layoutParams2.width = n2;
                    this.leftView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                    this.leftView.setTranslationX(translationX3);
                    this.leftView.invalidate();
                }
            }
            final ViewGroup leftButtonsView = this.leftButtonsView;
            if (leftButtonsView != null) {
                final float translationX4 = leftButtonsView.getTranslationX();
                this.reuseButtonsView(this.leftButtonsView);
                if ((this.leftButtonsView = (ViewGroup)this.getButtonsViewForMessage(0, false)) != null) {
                    this.leftButtonsView.setTranslationX(translationX4);
                }
            }
        }
    }
    
    private void reuseButtonsView(final ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        this.popupContainer.removeView((View)viewGroup);
    }
    
    private void reuseView(final ViewGroup e) {
        if (e == null) {
            return;
        }
        final int intValue = (int)e.getTag();
        e.setVisibility(8);
        if (intValue == 1) {
            this.textViews.add(e);
        }
        else if (intValue == 2) {
            this.imageViews.add(e);
        }
        else if (intValue == 3) {
            this.audioViews.add(e);
        }
    }
    
    private void setTypingAnimation(final boolean b) {
        if (this.actionBar == null) {
            return;
        }
        final int n = 0;
        int i = 0;
        if (b) {
            try {
                final Integer n2 = (Integer)MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStringsTypes.get(this.currentMessageObject.getDialogId());
                this.onlineTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)this.statusDrawables[n2], (Drawable)null, (Drawable)null, (Drawable)null);
                this.onlineTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                while (i < this.statusDrawables.length) {
                    if (i == n2) {
                        this.statusDrawables[i].start();
                    }
                    else {
                        this.statusDrawables[i].stop();
                    }
                    ++i;
                }
                return;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return;
            }
        }
        this.onlineTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
        this.onlineTextView.setCompoundDrawablePadding(0);
        int n3 = n;
        while (true) {
            final StatusDrawable[] statusDrawables = this.statusDrawables;
            if (n3 >= statusDrawables.length) {
                break;
            }
            statusDrawables[n3].stop();
            ++n3;
        }
    }
    
    private void switchToNextMessage() {
        if (this.popupMessages.size() > 1) {
            if (this.currentMessageNum < this.popupMessages.size() - 1) {
                ++this.currentMessageNum;
            }
            else {
                this.currentMessageNum = 0;
            }
            this.currentMessageObject = this.popupMessages.get(this.currentMessageNum);
            this.updateInterfaceForCurrentMessage(2);
            this.countText.setText((CharSequence)String.format("%d/%d", this.currentMessageNum + 1, this.popupMessages.size()));
        }
    }
    
    private void switchToPreviousMessage() {
        if (this.popupMessages.size() > 1) {
            final int currentMessageNum = this.currentMessageNum;
            if (currentMessageNum > 0) {
                this.currentMessageNum = currentMessageNum - 1;
            }
            else {
                this.currentMessageNum = this.popupMessages.size() - 1;
            }
            this.currentMessageObject = this.popupMessages.get(this.currentMessageNum);
            this.updateInterfaceForCurrentMessage(1);
            this.countText.setText((CharSequence)String.format("%d/%d", this.currentMessageNum + 1, this.popupMessages.size()));
        }
    }
    
    private void updateInterfaceForCurrentMessage(final int n) {
        if (this.actionBar == null) {
            return;
        }
        final int lastResumedAccount = this.lastResumedAccount;
        if (lastResumedAccount != this.currentMessageObject.currentAccount) {
            if (lastResumedAccount >= 0) {
                ConnectionsManager.getInstance(lastResumedAccount).setAppPaused(true, false);
            }
            this.lastResumedAccount = this.currentMessageObject.currentAccount;
            ConnectionsManager.getInstance(this.lastResumedAccount).setAppPaused(false, false);
        }
        this.currentChat = null;
        this.currentUser = null;
        final long dialogId = this.currentMessageObject.getDialogId();
        this.chatActivityEnterView.setDialogId(dialogId, this.currentMessageObject.currentAccount);
        final int i = (int)dialogId;
        if (i != 0) {
            if (i > 0) {
                this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(i);
            }
            else {
                this.currentChat = MessagesController.getInstance(this.currentMessageObject.currentAccount).getChat(-i);
                this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(this.currentMessageObject.messageOwner.from_id);
            }
        }
        else {
            this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(MessagesController.getInstance(this.currentMessageObject.currentAccount).getEncryptedChat((int)(dialogId >> 32)).user_id);
        }
        final TLRPC.Chat currentChat = this.currentChat;
        if (currentChat != null && this.currentUser != null) {
            this.nameTextView.setText((CharSequence)currentChat.title);
            this.onlineTextView.setText((CharSequence)UserObject.getUserName(this.currentUser));
            this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.nameTextView.setCompoundDrawablePadding(0);
        }
        else {
            final TLRPC.User currentUser = this.currentUser;
            if (currentUser != null) {
                this.nameTextView.setText((CharSequence)UserObject.getUserName(currentUser));
                if (i == 0) {
                    this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(2131165448, 0, 0, 0);
                    this.nameTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                }
                else {
                    this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    this.nameTextView.setCompoundDrawablePadding(0);
                }
            }
        }
        this.prepareLayouts(n);
        this.updateSubtitle();
        this.checkAndUpdateAvatar();
        this.applyViewsLayoutParams(0);
    }
    
    private void updateSubtitle() {
        if (this.actionBar != null) {
            final MessageObject currentMessageObject = this.currentMessageObject;
            if (currentMessageObject != null) {
                if (this.currentChat == null) {
                    final TLRPC.User currentUser = this.currentUser;
                    if (currentUser != null) {
                        final int id = currentUser.id;
                        if (id / 1000 != 777 && id / 1000 != 333 && ContactsController.getInstance(currentMessageObject.currentAccount).contactsDict.get(this.currentUser.id) == null && (ContactsController.getInstance(this.currentMessageObject.currentAccount).contactsDict.size() != 0 || !ContactsController.getInstance(this.currentMessageObject.currentAccount).isLoadingContacts())) {
                            final String phone = this.currentUser.phone;
                            if (phone != null && phone.length() != 0) {
                                final TextView nameTextView = this.nameTextView;
                                final PhoneFormat instance = PhoneFormat.getInstance();
                                final StringBuilder sb = new StringBuilder();
                                sb.append("+");
                                sb.append(this.currentUser.phone);
                                nameTextView.setText((CharSequence)instance.format(sb.toString()));
                            }
                            else {
                                this.nameTextView.setText((CharSequence)UserObject.getUserName(this.currentUser));
                            }
                        }
                        else {
                            this.nameTextView.setText((CharSequence)UserObject.getUserName(this.currentUser));
                        }
                        final TLRPC.User currentUser2 = this.currentUser;
                        if (currentUser2 != null && currentUser2.id == 777000) {
                            this.onlineTextView.setText((CharSequence)LocaleController.getString("ServiceNotifications", 2131560724));
                        }
                        else {
                            final CharSequence charSequence = (CharSequence)MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStrings.get(this.currentMessageObject.getDialogId());
                            if (charSequence != null && charSequence.length() != 0) {
                                this.lastPrintString = charSequence;
                                this.onlineTextView.setText(charSequence);
                                this.setTypingAnimation(true);
                            }
                            else {
                                this.lastPrintString = null;
                                this.setTypingAnimation(false);
                                final TLRPC.User user = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(this.currentUser.id);
                                if (user != null) {
                                    this.currentUser = user;
                                }
                                this.onlineTextView.setText((CharSequence)LocaleController.formatUserStatus(this.currentMessageObject.currentAccount, this.currentUser));
                            }
                        }
                    }
                }
            }
        }
    }
    
    public boolean checkTransitionAnimation() {
        if (this.animationInProgress && this.animationStartTime < System.currentTimeMillis() - 400L) {
            this.animationInProgress = false;
            final Runnable onAnimationEndRunnable = this.onAnimationEndRunnable;
            if (onAnimationEndRunnable != null) {
                onAnimationEndRunnable.run();
                this.onAnimationEndRunnable = null;
            }
        }
        return this.animationInProgress;
    }
    
    public void didReceivedNotification(int i, int childCount, final Object... array) {
        if (i == NotificationCenter.appDidLogout) {
            if (childCount == this.lastResumedAccount) {
                this.onFinish();
                this.finish();
            }
        }
        else {
            final int pushMessagesUpdated = NotificationCenter.pushMessagesUpdated;
            final int n = 0;
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            if (i == pushMessagesUpdated) {
                if (!this.isReply) {
                    this.popupMessages.clear();
                    for (i = n4; i < 3; ++i) {
                        if (UserConfig.getInstance(i).isClientActivated()) {
                            this.popupMessages.addAll(NotificationsController.getInstance(i).popupMessages);
                        }
                    }
                    this.getNewMessage();
                }
            }
            else if (i == NotificationCenter.updateInterfaces) {
                if (this.currentMessageObject == null || childCount != this.lastResumedAccount) {
                    return;
                }
                i = (int)array[0];
                if ((i & 0x1) != 0x0 || (i & 0x4) != 0x0 || (i & 0x10) != 0x0 || (i & 0x20) != 0x0) {
                    this.updateSubtitle();
                }
                if ((i & 0x2) != 0x0 || (i & 0x8) != 0x0) {
                    this.checkAndUpdateAvatar();
                }
                if ((i & 0x40) != 0x0) {
                    final CharSequence obj = (CharSequence)MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStrings.get(this.currentMessageObject.getDialogId());
                    if ((this.lastPrintString == null || obj != null) && (this.lastPrintString != null || obj == null)) {
                        final CharSequence lastPrintString = this.lastPrintString;
                        if (lastPrintString == null || obj == null || lastPrintString.equals(obj)) {
                            return;
                        }
                    }
                    this.updateSubtitle();
                }
            }
            else if (i == NotificationCenter.messagePlayingDidReset) {
                final Integer n5 = (Integer)array[0];
                final ViewGroup messageContainer = this.messageContainer;
                if (messageContainer != null) {
                    int childCount2;
                    View child;
                    PopupAudioView popupAudioView;
                    MessageObject messageObject;
                    for (childCount2 = messageContainer.getChildCount(), i = n; i < childCount2; ++i) {
                        child = this.messageContainer.getChildAt(i);
                        if ((int)child.getTag() == 3) {
                            popupAudioView = (PopupAudioView)child.findViewWithTag((Object)300);
                            messageObject = popupAudioView.getMessageObject();
                            if (messageObject != null && messageObject.currentAccount == childCount && messageObject.getId() == n5) {
                                popupAudioView.updateButtonState();
                                break;
                            }
                        }
                    }
                }
            }
            else if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                final Integer n6 = (Integer)array[0];
                final ViewGroup messageContainer2 = this.messageContainer;
                if (messageContainer2 != null) {
                    int childCount3;
                    View child2;
                    PopupAudioView popupAudioView2;
                    MessageObject messageObject2;
                    for (childCount3 = messageContainer2.getChildCount(), i = n2; i < childCount3; ++i) {
                        child2 = this.messageContainer.getChildAt(i);
                        if ((int)child2.getTag() == 3) {
                            popupAudioView2 = (PopupAudioView)child2.findViewWithTag((Object)300);
                            messageObject2 = popupAudioView2.getMessageObject();
                            if (messageObject2 != null && messageObject2.currentAccount == childCount && messageObject2.getId() == n6) {
                                popupAudioView2.updateProgress();
                                break;
                            }
                        }
                    }
                }
            }
            else if (i == NotificationCenter.emojiDidLoad) {
                final ViewGroup messageContainer3 = this.messageContainer;
                if (messageContainer3 != null) {
                    View child3;
                    TextView textView;
                    for (childCount = messageContainer3.getChildCount(), i = n3; i < childCount; ++i) {
                        child3 = this.messageContainer.getChildAt(i);
                        if ((int)child3.getTag() == 1) {
                            textView = (TextView)child3.findViewWithTag((Object)301);
                            if (textView != null) {
                                textView.invalidate();
                            }
                        }
                    }
                }
            }
            else if (i == NotificationCenter.contactsDidLoad && childCount == this.lastResumedAccount) {
                this.updateSubtitle();
            }
        }
    }
    
    public void onBackPressed() {
        if (this.chatActivityEnterView.isPopupShowing()) {
            this.chatActivityEnterView.hidePopup(true);
            return;
        }
        super.onBackPressed();
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AndroidUtilities.checkDisplaySize((Context)this, configuration);
        this.fixLayout();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        Theme.createChatResources((Context)this, false);
        final int identifier = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            AndroidUtilities.statusBarHeight = this.getResources().getDimensionPixelSize(identifier);
        }
        for (int i = 0; i < 3; ++i) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.contactsDidLoad);
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pushMessagesUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        this.classGuid = ConnectionsManager.generateClassGuid();
        this.statusDrawables[0] = new TypingDotsDrawable();
        this.statusDrawables[1] = new RecordStatusDrawable();
        this.statusDrawables[2] = new SendingFileDrawable();
        this.statusDrawables[3] = new PlayingGameDrawable();
        this.statusDrawables[4] = new RoundStatusDrawable();
        final SizeNotifierFrameLayout contentView = new SizeNotifierFrameLayout(this) {
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                final int childCount = this.getChildCount();
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                int i = 0;
                int emojiPadding;
                if (keyboardHeight <= dp) {
                    emojiPadding = PopupNotificationActivity.this.chatActivityEnterView.getEmojiPadding();
                }
                else {
                    emojiPadding = 0;
                }
                while (i < childCount) {
                    final View child = this.getChildAt(i);
                    if (child.getVisibility() != 8) {
                        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                        final int measuredWidth = child.getMeasuredWidth();
                        final int measuredHeight = child.getMeasuredHeight();
                        int gravity;
                        if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                            gravity = 51;
                        }
                        final int n5 = gravity & 0x70;
                        final int n6 = gravity & 0x7 & 0x7;
                        int leftMargin = 0;
                        Label_0207: {
                            int n7;
                            int n8;
                            if (n6 != 1) {
                                if (n6 != 5) {
                                    leftMargin = frameLayout$LayoutParams.leftMargin;
                                    break Label_0207;
                                }
                                n7 = n3 - measuredWidth;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            else {
                                n7 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            leftMargin = n7 - n8;
                        }
                        int n9 = 0;
                        Label_0304: {
                            int n10;
                            int n11;
                            if (n5 != 16) {
                                if (n5 == 48) {
                                    n9 = frameLayout$LayoutParams.topMargin;
                                    break Label_0304;
                                }
                                if (n5 != 80) {
                                    n9 = frameLayout$LayoutParams.topMargin;
                                    break Label_0304;
                                }
                                n10 = n4 - emojiPadding - n2 - measuredHeight;
                                n11 = frameLayout$LayoutParams.bottomMargin;
                            }
                            else {
                                n10 = (n4 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                n11 = frameLayout$LayoutParams.bottomMargin;
                            }
                            n9 = n10 - n11;
                        }
                        if (PopupNotificationActivity.this.chatActivityEnterView.isPopupView(child)) {
                            if (emojiPadding != 0) {
                                n9 = this.getMeasuredHeight() - emojiPadding;
                            }
                            else {
                                n9 = this.getMeasuredHeight();
                            }
                        }
                        else if (PopupNotificationActivity.this.chatActivityEnterView.isRecordCircle(child)) {
                            n9 = PopupNotificationActivity.this.popupContainer.getTop() + PopupNotificationActivity.this.popupContainer.getMeasuredHeight() - child.getMeasuredHeight() - frameLayout$LayoutParams.bottomMargin;
                            leftMargin = PopupNotificationActivity.this.popupContainer.getLeft() + PopupNotificationActivity.this.popupContainer.getMeasuredWidth() - child.getMeasuredWidth() - frameLayout$LayoutParams.rightMargin;
                        }
                        child.layout(leftMargin, n9, measuredWidth + leftMargin, measuredHeight + n9);
                    }
                    ++i;
                }
                this.notifyHeightChanged();
            }
            
            protected void onMeasure(final int n, final int n2) {
                View$MeasureSpec.getMode(n);
                View$MeasureSpec.getMode(n2);
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                int n3 = size2;
                if (this.getKeyboardHeight() <= AndroidUtilities.dp(20.0f)) {
                    n3 = size2 - PopupNotificationActivity.this.chatActivityEnterView.getEmojiPadding();
                }
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    if (child.getVisibility() != 8) {
                        if (PopupNotificationActivity.this.chatActivityEnterView.isPopupView(child)) {
                            child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                        }
                        else if (PopupNotificationActivity.this.chatActivityEnterView.isRecordCircle(child)) {
                            this.measureChildWithMargins(child, n, 0, n2, 0);
                        }
                        else {
                            child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.0f) + n3), 1073741824));
                        }
                    }
                }
            }
        };
        this.setContentView((View)contentView);
        contentView.setBackgroundColor(-1728053248);
        final RelativeLayout relativeLayout = new RelativeLayout((Context)this);
        contentView.addView((View)relativeLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.popupContainer = new RelativeLayout(this) {
            protected void onLayout(final boolean b, int i, final int n, final int n2, final int n3) {
                super.onLayout(b, i, n, n2, n3);
                View child;
                for (i = 0; i < this.getChildCount(); ++i) {
                    child = this.getChildAt(i);
                    if (child.getTag() instanceof String) {
                        child.layout(child.getLeft(), PopupNotificationActivity.this.chatActivityEnterView.getTop() + AndroidUtilities.dp(3.0f), child.getRight(), PopupNotificationActivity.this.chatActivityEnterView.getBottom());
                    }
                }
            }
            
            protected void onMeasure(int i, int measuredHeight) {
                super.onMeasure(i, measuredHeight);
                final int measuredWidth = PopupNotificationActivity.this.chatActivityEnterView.getMeasuredWidth();
                measuredHeight = PopupNotificationActivity.this.chatActivityEnterView.getMeasuredHeight();
                View child;
                for (i = 0; i < this.getChildCount(); ++i) {
                    child = this.getChildAt(i);
                    if (child.getTag() instanceof String) {
                        child.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View$MeasureSpec.makeMeasureSpec(measuredHeight - AndroidUtilities.dp(3.0f), 1073741824));
                    }
                }
            }
        }).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        relativeLayout.addView((View)this.popupContainer, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, 240, 12, 0, 12, 0, 13));
        final ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onDestroy();
        }
        (this.chatActivityEnterView = new ChatActivityEnterView(this, contentView, null, false)).setId(1000);
        this.popupContainer.addView((View)this.chatActivityEnterView, (ViewGroup$LayoutParams)LayoutHelper.createRelative(-1, -2, 12));
        this.chatActivityEnterView.setDelegate((ChatActivityEnterView.ChatActivityEnterViewDelegate)new ChatActivityEnterView.ChatActivityEnterViewDelegate() {
            @Override
            public void didPressedAttachButton() {
            }
            
            @Override
            public void needChangeVideoPreviewState(final int n, final float n2) {
            }
            
            @Override
            public void needSendTyping() {
                if (PopupNotificationActivity.this.currentMessageObject != null) {
                    MessagesController.getInstance(PopupNotificationActivity.this.currentMessageObject.currentAccount).sendTyping(PopupNotificationActivity.this.currentMessageObject.getDialogId(), 0, PopupNotificationActivity.this.classGuid);
                }
            }
            
            @Override
            public void needShowMediaBanHint() {
            }
            
            @Override
            public void needStartRecordAudio(final int n) {
            }
            
            @Override
            public void needStartRecordVideo(final int n) {
            }
            
            @Override
            public void onAttachButtonHidden() {
            }
            
            @Override
            public void onAttachButtonShow() {
            }
            
            @Override
            public void onMessageEditEnd(final boolean b) {
            }
            
            @Override
            public void onMessageSend(final CharSequence charSequence) {
                if (PopupNotificationActivity.this.currentMessageObject == null) {
                    return;
                }
                if (PopupNotificationActivity.this.currentMessageNum >= 0 && PopupNotificationActivity.this.currentMessageNum < PopupNotificationActivity.this.popupMessages.size()) {
                    PopupNotificationActivity.this.popupMessages.remove(PopupNotificationActivity.this.currentMessageNum);
                }
                MessagesController.getInstance(PopupNotificationActivity.this.currentMessageObject.currentAccount).markDialogAsRead(PopupNotificationActivity.this.currentMessageObject.getDialogId(), PopupNotificationActivity.this.currentMessageObject.getId(), Math.max(0, PopupNotificationActivity.this.currentMessageObject.getId()), PopupNotificationActivity.this.currentMessageObject.messageOwner.date, true, 0, true);
                PopupNotificationActivity.this.currentMessageObject = null;
                PopupNotificationActivity.this.getNewMessage();
            }
            
            @Override
            public void onPreAudioVideoRecord() {
            }
            
            @Override
            public void onStickersExpandedChange() {
            }
            
            @Override
            public void onStickersTab(final boolean b) {
            }
            
            @Override
            public void onSwitchRecordMode(final boolean b) {
            }
            
            @Override
            public void onTextChanged(final CharSequence charSequence, final boolean b) {
            }
            
            @Override
            public void onTextSelectionChanged(final int n, final int n2) {
            }
            
            @Override
            public void onTextSpansChanged(final CharSequence charSequence) {
            }
            
            @Override
            public void onWindowSizeChanged(final int n) {
            }
        });
        this.messageContainer = (ViewGroup)new FrameLayoutTouch((Context)this);
        this.popupContainer.addView((View)this.messageContainer, 0);
        (this.actionBar = new ActionBar((Context)this)).setOccupyStatusBar(false);
        this.actionBar.setBackButtonImage(2131165437);
        this.actionBar.setBackgroundColor(Theme.getColor("actionBarDefault"));
        this.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), false);
        this.popupContainer.addView((View)this.actionBar);
        final ViewGroup$LayoutParams layoutParams = this.actionBar.getLayoutParams();
        layoutParams.width = -1;
        this.actionBar.setLayoutParams(layoutParams);
        final ActionBarMenuItem addItemWithWidth = this.actionBar.createMenu().addItemWithWidth(2, 0, AndroidUtilities.dp(56.0f));
        (this.countText = new TextView((Context)this)).setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
        this.countText.setTextSize(1, 14.0f);
        this.countText.setGravity(17);
        addItemWithWidth.addView((View)this.countText, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, -1.0f));
        (this.avatarContainer = new FrameLayout((Context)this)).setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
        this.actionBar.addView((View)this.avatarContainer);
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.avatarContainer.getLayoutParams();
        layoutParams2.height = -1;
        layoutParams2.width = -2;
        layoutParams2.rightMargin = AndroidUtilities.dp(48.0f);
        layoutParams2.leftMargin = AndroidUtilities.dp(60.0f);
        layoutParams2.gravity = 51;
        this.avatarContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        (this.avatarImageView = new BackupImageView((Context)this)).setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarContainer.addView((View)this.avatarImageView);
        final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)this.avatarImageView.getLayoutParams();
        layoutParams3.width = AndroidUtilities.dp(42.0f);
        layoutParams3.height = AndroidUtilities.dp(42.0f);
        layoutParams3.topMargin = AndroidUtilities.dp(3.0f);
        this.avatarImageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
        (this.nameTextView = new TextView((Context)this)).setTextColor(Theme.getColor("actionBarDefaultTitle"));
        this.nameTextView.setTextSize(1, 18.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.nameTextView.setGravity(3);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.avatarContainer.addView((View)this.nameTextView);
        final FrameLayout$LayoutParams layoutParams4 = (FrameLayout$LayoutParams)this.nameTextView.getLayoutParams();
        layoutParams4.width = -2;
        layoutParams4.height = -2;
        layoutParams4.leftMargin = AndroidUtilities.dp(54.0f);
        layoutParams4.bottomMargin = AndroidUtilities.dp(22.0f);
        layoutParams4.gravity = 80;
        this.nameTextView.setLayoutParams((ViewGroup$LayoutParams)layoutParams4);
        (this.onlineTextView = new TextView((Context)this)).setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
        this.onlineTextView.setTextSize(1, 14.0f);
        this.onlineTextView.setLines(1);
        this.onlineTextView.setMaxLines(1);
        this.onlineTextView.setSingleLine(true);
        this.onlineTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.onlineTextView.setGravity(3);
        this.avatarContainer.addView((View)this.onlineTextView);
        final FrameLayout$LayoutParams layoutParams5 = (FrameLayout$LayoutParams)this.onlineTextView.getLayoutParams();
        layoutParams5.width = -2;
        layoutParams5.height = -2;
        layoutParams5.leftMargin = AndroidUtilities.dp(54.0f);
        layoutParams5.bottomMargin = AndroidUtilities.dp(4.0f);
        layoutParams5.gravity = 80;
        this.onlineTextView.setLayoutParams((ViewGroup$LayoutParams)layoutParams5);
        this.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PopupNotificationActivity.this.onFinish();
                    PopupNotificationActivity.this.finish();
                }
                else if (n == 1) {
                    PopupNotificationActivity.this.openCurrentMessage();
                }
                else if (n == 2) {
                    PopupNotificationActivity.this.switchToNextMessage();
                }
            }
        });
        (this.wakeLock = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(268435462, "screen")).setReferenceCounted(false);
        this.handleIntent(this.getIntent());
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.onFinish();
        MediaController.getInstance().setFeedbackView((View)this.chatActivityEnterView, false);
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        final BackupImageView avatarImageView = this.avatarImageView;
        if (avatarImageView != null) {
            avatarImageView.setImageDrawable(null);
        }
    }
    
    protected void onFinish() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        if (this.isReply) {
            this.popupMessages.clear();
        }
        for (int i = 0; i < 3; ++i) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.contactsDidLoad);
        }
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pushMessagesUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
        final ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onDestroy();
        }
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }
    
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.handleIntent(intent);
    }
    
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(0, 0);
        final ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.hidePopup(false);
            this.chatActivityEnterView.setFieldFocused(false);
        }
        final int lastResumedAccount = this.lastResumedAccount;
        if (lastResumedAccount >= 0) {
            ConnectionsManager.getInstance(lastResumedAccount).setAppPaused(true, false);
        }
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(n, array, array2);
        if (n == 3) {
            if (array2[0] == 0) {
                return;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
            builder.setTitle(LocaleController.getString("AppName", 2131558635));
            builder.setMessage(LocaleController.getString("PermissionNoAudio", 2131560414));
            builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new _$$Lambda$PopupNotificationActivity$4j1X9I2molTl8UnVVYY3k_eiVzk(this));
            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
            builder.show();
        }
    }
    
    protected void onResume() {
        super.onResume();
        MediaController.getInstance().setFeedbackView((View)this.chatActivityEnterView, true);
        final ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.setFieldFocused(true);
        }
        this.fixLayout();
        this.checkAndUpdateAvatar();
        this.wakeLock.acquire(7000L);
    }
    
    public boolean onTouchEventMy(final MotionEvent motionEvent) {
        final boolean checkTransitionAnimation = this.checkTransitionAnimation();
        final int n = 0;
        if (checkTransitionAnimation) {
            return false;
        }
        if (motionEvent != null && motionEvent.getAction() == 0) {
            this.moveStartX = motionEvent.getX();
        }
        else if (motionEvent != null && motionEvent.getAction() == 2) {
            final float x = motionEvent.getX();
            final float moveStartX = this.moveStartX;
            int n2;
            final int a = n2 = (int)(x - moveStartX);
            if (moveStartX != -1.0f) {
                n2 = a;
                if (!this.startedMoving) {
                    n2 = a;
                    if (Math.abs(a) > AndroidUtilities.dp(10.0f)) {
                        this.startedMoving = true;
                        this.moveStartX = x;
                        AndroidUtilities.lockOrientation(this);
                        final VelocityTracker velocityTracker = this.velocityTracker;
                        if (velocityTracker == null) {
                            this.velocityTracker = VelocityTracker.obtain();
                        }
                        else {
                            velocityTracker.clear();
                        }
                        n2 = 0;
                    }
                }
            }
            if (this.startedMoving) {
                int n3 = n2;
                if (this.leftView == null && (n3 = n2) > 0) {
                    n3 = 0;
                }
                if (this.rightView == null && n3 < 0) {
                    n3 = n;
                }
                final VelocityTracker velocityTracker2 = this.velocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.addMovement(motionEvent);
                }
                this.applyViewsLayoutParams(n3);
            }
        }
        else if (motionEvent == null || motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (motionEvent != null && this.startedMoving) {
                final int n4 = (int)(motionEvent.getX() - this.moveStartX);
                final int n5 = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
                final VelocityTracker velocityTracker3 = this.velocityTracker;
                int n6 = 0;
                Label_0341: {
                    if (velocityTracker3 != null) {
                        velocityTracker3.computeCurrentVelocity(1000);
                        if (this.velocityTracker.getXVelocity() >= 3500.0f) {
                            n6 = 1;
                            break Label_0341;
                        }
                        if (this.velocityTracker.getXVelocity() <= -3500.0f) {
                            n6 = 2;
                            break Label_0341;
                        }
                    }
                    n6 = 0;
                }
                float n7;
                ViewGroup viewGroup;
                ViewGroup viewGroup2;
                if ((n6 == 1 || n4 > n5 / 3) && this.leftView != null) {
                    n7 = n5 - this.centerView.getTranslationX();
                    viewGroup = this.leftView;
                    viewGroup2 = this.leftButtonsView;
                    this.onAnimationEndRunnable = new _$$Lambda$PopupNotificationActivity$EUnPo4xnwynM9tMiGtirwuisKAk(this);
                }
                else if ((n6 == 2 || n4 < -n5 / 3) && this.rightView != null) {
                    n7 = -n5 - this.centerView.getTranslationX();
                    viewGroup = this.rightView;
                    viewGroup2 = this.rightButtonsView;
                    this.onAnimationEndRunnable = new _$$Lambda$PopupNotificationActivity$w3pAWV1vrsUpHtwKKGENN1Nl00M(this);
                }
                else if (this.centerView.getTranslationX() != 0.0f) {
                    n7 = -this.centerView.getTranslationX();
                    if (n4 > 0) {
                        viewGroup = this.leftView;
                    }
                    else {
                        viewGroup = this.rightView;
                    }
                    if (n4 > 0) {
                        viewGroup2 = this.leftButtonsView;
                    }
                    else {
                        viewGroup2 = this.rightButtonsView;
                    }
                    this.onAnimationEndRunnable = new _$$Lambda$PopupNotificationActivity$EAJL169S6xhlVfXmCKr6JNKWWPk(this);
                }
                else {
                    viewGroup = (viewGroup2 = null);
                    n7 = 0.0f;
                }
                if (n7 != 0.0f) {
                    final int n8 = (int)(Math.abs(n7 / n5) * 200.0f);
                    final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                    final ViewGroup centerView = this.centerView;
                    list.add(ObjectAnimator.ofFloat((Object)centerView, "translationX", new float[] { centerView.getTranslationX() + n7 }));
                    final ViewGroup centerButtonsView = this.centerButtonsView;
                    if (centerButtonsView != null) {
                        list.add(ObjectAnimator.ofFloat((Object)centerButtonsView, "translationX", new float[] { centerButtonsView.getTranslationX() + n7 }));
                    }
                    if (viewGroup != null) {
                        list.add(ObjectAnimator.ofFloat((Object)viewGroup, "translationX", new float[] { ((View)viewGroup).getTranslationX() + n7 }));
                    }
                    if (viewGroup2 != null) {
                        list.add(ObjectAnimator.ofFloat((Object)viewGroup2, "translationX", new float[] { ((View)viewGroup2).getTranslationX() + n7 }));
                    }
                    final AnimatorSet set = new AnimatorSet();
                    set.playTogether((Collection)list);
                    set.setDuration((long)n8);
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            if (PopupNotificationActivity.this.onAnimationEndRunnable != null) {
                                PopupNotificationActivity.this.onAnimationEndRunnable.run();
                                PopupNotificationActivity.this.onAnimationEndRunnable = null;
                            }
                        }
                    });
                    set.start();
                    this.animationInProgress = true;
                    this.animationStartTime = System.currentTimeMillis();
                }
            }
            else {
                this.applyViewsLayoutParams(0);
            }
            final VelocityTracker velocityTracker4 = this.velocityTracker;
            if (velocityTracker4 != null) {
                velocityTracker4.recycle();
                this.velocityTracker = null;
            }
            this.startedMoving = false;
            this.moveStartX = -1.0f;
        }
        return this.startedMoving;
    }
    
    private class FrameLayoutTouch extends FrameLayout
    {
        public FrameLayoutTouch(final Context context) {
            super(context);
        }
        
        public FrameLayoutTouch(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public FrameLayoutTouch(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            return PopupNotificationActivity.this.checkTransitionAnimation() || ((PopupNotificationActivity)this.getContext()).onTouchEventMy(motionEvent);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return PopupNotificationActivity.this.checkTransitionAnimation() || ((PopupNotificationActivity)this.getContext()).onTouchEventMy(motionEvent);
        }
        
        public void requestDisallowInterceptTouchEvent(final boolean b) {
            ((PopupNotificationActivity)this.getContext()).onTouchEventMy(null);
            super.requestDisallowInterceptTouchEvent(b);
        }
    }
}
