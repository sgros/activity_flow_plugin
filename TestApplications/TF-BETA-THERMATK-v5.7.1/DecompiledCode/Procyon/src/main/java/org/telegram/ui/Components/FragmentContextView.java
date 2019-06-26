// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.annotation.Keep;
import android.content.DialogInterface;
import android.content.Intent;
import org.telegram.ui.VoIPActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.graphics.Typeface;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.LaunchActivity;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.MediaController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.MessagesController;
import java.util.Collection;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import org.telegram.ui.ChatActivity;
import org.telegram.messenger.LocationController;
import org.telegram.ui.DialogsActivity;
import android.widget.FrameLayout$LayoutParams;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import android.view.View$OnClickListener;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.ActionBar.BaseFragment;
import android.widget.ImageView;
import android.animation.AnimatorSet;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class FragmentContextView extends FrameLayout implements NotificationCenterDelegate
{
    private FragmentContextView additionalContextView;
    private AnimatorSet animatorSet;
    private Runnable checkLocationRunnable;
    private ImageView closeButton;
    private int currentStyle;
    private boolean firstLocationsLoaded;
    private BaseFragment fragment;
    private FrameLayout frameLayout;
    private boolean isLocation;
    private int lastLocationSharingCount;
    private MessageObject lastMessageObject;
    private String lastString;
    private boolean loadingSharingCount;
    private ImageView playButton;
    private ImageView playbackSpeedButton;
    private TextView titleTextView;
    private float topPadding;
    private boolean visible;
    private float yPosition;
    
    public FragmentContextView(final Context context, final BaseFragment fragment, final boolean isLocation) {
        super(context);
        this.currentStyle = -1;
        this.lastLocationSharingCount = -1;
        this.checkLocationRunnable = new Runnable() {
            @Override
            public void run() {
                FragmentContextView.this.checkLocationString();
                AndroidUtilities.runOnUIThread(FragmentContextView.this.checkLocationRunnable, 1000L);
            }
        };
        this.fragment = fragment;
        this.visible = true;
        this.isLocation = isLocation;
        ((ViewGroup)this.fragment.getFragmentView()).setClipToPadding(false);
        this.setTag((Object)1);
        (this.frameLayout = new FrameLayout(context)).setWillNotDraw(false);
        this.addView((View)this.frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
        final View view = new View(context);
        view.setBackgroundResource(2131165407);
        this.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 3.0f, 51, 0.0f, 36.0f, 0.0f, 0.0f));
        (this.playButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.playButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("inappPlayerPlayPause"), PorterDuff$Mode.MULTIPLY));
        this.addView((View)this.playButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36, 51));
        this.playButton.setOnClickListener((View$OnClickListener)new _$$Lambda$FragmentContextView$xaUr_8YxtF96bWuD8Iv6_gj2wjI(this));
        (this.titleTextView = new TextView(context)).setMaxLines(1);
        this.titleTextView.setLines(1);
        this.titleTextView.setSingleLine(true);
        this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.titleTextView.setTextSize(1, 15.0f);
        this.titleTextView.setGravity(19);
        this.addView((View)this.titleTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
        if (!isLocation) {
            (this.playbackSpeedButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.playbackSpeedButton.setImageResource(2131165910);
            this.playbackSpeedButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrPlayerSpeed", 2131558458));
            if (AndroidUtilities.density >= 3.0f) {
                this.playbackSpeedButton.setPadding(0, 1, 0, 0);
            }
            this.addView((View)this.playbackSpeedButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 53, 0.0f, 0.0f, 36.0f, 0.0f));
            this.playbackSpeedButton.setOnClickListener((View$OnClickListener)new _$$Lambda$FragmentContextView$mn4uoFbwvEVtbgf8Ptja5mtZJQM(this));
            this.updatePlaybackButton();
        }
        (this.closeButton = new ImageView(context)).setImageResource(2131165604);
        this.closeButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("inappPlayerClose"), PorterDuff$Mode.MULTIPLY));
        this.closeButton.setScaleType(ImageView$ScaleType.CENTER);
        this.addView((View)this.closeButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36, 53));
        this.closeButton.setOnClickListener((View$OnClickListener)new _$$Lambda$FragmentContextView$A00dqLRerQA_JpKS29_7k_ZhsDA(this));
        this.setOnClickListener((View$OnClickListener)new _$$Lambda$FragmentContextView$oHS8Qv6e4NDG6yqH2reqe1Dmqu0(this));
    }
    
    private void checkCall(final boolean b) {
        final View fragmentView = this.fragment.getFragmentView();
        boolean b2 = b;
        Label_0044: {
            if (!b) {
                b2 = b;
                if (fragmentView != null) {
                    if (fragmentView.getParent() != null) {
                        b2 = b;
                        if (((View)fragmentView.getParent()).getVisibility() == 0) {
                            break Label_0044;
                        }
                    }
                    b2 = true;
                }
            }
        }
        if (VoIPService.getSharedInstance() == null || VoIPService.getSharedInstance().getCallState() == 15) {
            if (this.visible) {
                this.visible = false;
                if (b2) {
                    if (this.getVisibility() != 8) {
                        this.setVisibility(8);
                    }
                    this.setTopPadding(0.0f);
                }
                else {
                    final AnimatorSet animatorSet = this.animatorSet;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        this.animatorSet = null;
                    }
                    (this.animatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "topPadding", new float[] { 0.0f }) });
                    this.animatorSet.setDuration(200L);
                    this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(obj)) {
                                FragmentContextView.this.setVisibility(8);
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
            }
        }
        else {
            this.updateStyle(1);
            if (b2 && this.topPadding == 0.0f) {
                this.setTopPadding((float)AndroidUtilities.dp2(36.0f));
                final FragmentContextView additionalContextView = this.additionalContextView;
                if (additionalContextView != null && additionalContextView.getVisibility() == 0) {
                    ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0f);
                }
                else {
                    ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f);
                }
                this.yPosition = 0.0f;
            }
            if (!this.visible) {
                if (!b2) {
                    final AnimatorSet animatorSet2 = this.animatorSet;
                    if (animatorSet2 != null) {
                        animatorSet2.cancel();
                        this.animatorSet = null;
                    }
                    this.animatorSet = new AnimatorSet();
                    final FragmentContextView additionalContextView2 = this.additionalContextView;
                    if (additionalContextView2 != null && additionalContextView2.getVisibility() == 0) {
                        ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0f);
                    }
                    else {
                        ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f);
                    }
                    this.animatorSet.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "topPadding", new float[] { (float)AndroidUtilities.dp2(36.0f) }) });
                    this.animatorSet.setDuration(200L);
                    this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(obj)) {
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
                this.visible = true;
                this.setVisibility(0);
            }
        }
    }
    
    private void checkLiveLocation(final boolean b) {
        final View fragmentView = this.fragment.getFragmentView();
        boolean b2 = b;
        Label_0044: {
            if (!b) {
                b2 = b;
                if (fragmentView != null) {
                    if (fragmentView.getParent() != null) {
                        b2 = b;
                        if (((View)fragmentView.getParent()).getVisibility() == 0) {
                            break Label_0044;
                        }
                    }
                    b2 = true;
                }
            }
        }
        final BaseFragment fragment = this.fragment;
        boolean sharingLocation;
        if (fragment instanceof DialogsActivity) {
            sharingLocation = (LocationController.getLocationsCount() != 0);
        }
        else {
            sharingLocation = LocationController.getInstance(fragment.getCurrentAccount()).isSharingLocation(((ChatActivity)this.fragment).getDialogId());
        }
        if (!sharingLocation) {
            this.lastLocationSharingCount = -1;
            AndroidUtilities.cancelRunOnUIThread(this.checkLocationRunnable);
            if (this.visible) {
                this.visible = false;
                if (b2) {
                    if (this.getVisibility() != 8) {
                        this.setVisibility(8);
                    }
                    this.setTopPadding(0.0f);
                }
                else {
                    final AnimatorSet animatorSet = this.animatorSet;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        this.animatorSet = null;
                    }
                    (this.animatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "topPadding", new float[] { 0.0f }) });
                    this.animatorSet.setDuration(200L);
                    this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(obj)) {
                                FragmentContextView.this.setVisibility(8);
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
            }
        }
        else {
            this.updateStyle(2);
            this.playButton.setImageDrawable((Drawable)new ShareLocationDrawable(this.getContext(), true));
            if (b2 && this.topPadding == 0.0f) {
                this.setTopPadding((float)AndroidUtilities.dp2(36.0f));
                this.yPosition = 0.0f;
            }
            if (!this.visible) {
                if (!b2) {
                    final AnimatorSet animatorSet2 = this.animatorSet;
                    if (animatorSet2 != null) {
                        animatorSet2.cancel();
                        this.animatorSet = null;
                    }
                    (this.animatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "topPadding", new float[] { (float)AndroidUtilities.dp2(36.0f) }) });
                    this.animatorSet.setDuration(200L);
                    this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(obj)) {
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
                this.visible = true;
                this.setVisibility(0);
            }
            if (this.fragment instanceof DialogsActivity) {
                final String string = LocaleController.getString("AttachLiveLocation", 2131558721);
                final ArrayList<LocationController.SharingLocationInfo> list = new ArrayList<LocationController.SharingLocationInfo>();
                for (int i = 0; i < 3; ++i) {
                    list.addAll((Collection<? extends LocationController.SharingLocationInfo>)LocationController.getInstance(i).sharingLocationsUI);
                }
                String s;
                if (list.size() == 1) {
                    final LocationController.SharingLocationInfo sharingLocationInfo = list.get(0);
                    final int j = (int)sharingLocationInfo.messageObject.getDialogId();
                    if (j > 0) {
                        s = UserObject.getFirstName(MessagesController.getInstance(sharingLocationInfo.messageObject.currentAccount).getUser(j));
                    }
                    else {
                        final TLRPC.Chat chat = MessagesController.getInstance(sharingLocationInfo.messageObject.currentAccount).getChat(-j);
                        if (chat != null) {
                            s = chat.title;
                        }
                        else {
                            s = "";
                        }
                    }
                }
                else {
                    s = LocaleController.formatPluralString("Chats", list.size());
                }
                final String format = String.format(LocaleController.getString("AttachLiveLocationIsSharing", 2131558722), string, s);
                final int index = format.indexOf(string);
                final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)format);
                this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
                text.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("inappPlayerPerformer")), index, string.length() + index, 18);
                this.titleTextView.setText((CharSequence)text);
            }
            else {
                this.checkLocationRunnable.run();
                this.checkLocationString();
            }
        }
    }
    
    private void checkLocationString() {
        final BaseFragment fragment = this.fragment;
        if (fragment instanceof ChatActivity) {
            if (this.titleTextView != null) {
                final ChatActivity chatActivity = (ChatActivity)fragment;
                final long dialogId = chatActivity.getDialogId();
                final int currentAccount = chatActivity.getCurrentAccount();
                final ArrayList list = (ArrayList)LocationController.getInstance(currentAccount).locationsCache.get(dialogId);
                if (!this.firstLocationsLoaded) {
                    LocationController.getInstance(currentAccount).loadLiveLocations(dialogId);
                    this.firstLocationsLoaded = true;
                }
                TLRPC.User user = null;
                int lastLocationSharingCount;
                if (list != null) {
                    final int clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
                    final int currentTime = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
                    user = null;
                    int i = 0;
                    lastLocationSharingCount = 0;
                    while (i < list.size()) {
                        final TLRPC.Message message = list.get(i);
                        final TLRPC.MessageMedia media = message.media;
                        int n;
                        TLRPC.User user2;
                        if (media == null) {
                            n = lastLocationSharingCount;
                            user2 = user;
                        }
                        else {
                            n = lastLocationSharingCount;
                            user2 = user;
                            if (message.date + media.period > currentTime) {
                                if ((user2 = user) == null) {
                                    user2 = user;
                                    if (message.from_id != clientUserId) {
                                        user2 = MessagesController.getInstance(currentAccount).getUser(message.from_id);
                                    }
                                }
                                n = lastLocationSharingCount + 1;
                            }
                        }
                        ++i;
                        lastLocationSharingCount = n;
                        user = user2;
                    }
                }
                else {
                    lastLocationSharingCount = 0;
                }
                if (this.lastLocationSharingCount == lastLocationSharingCount) {
                    return;
                }
                this.lastLocationSharingCount = lastLocationSharingCount;
                final String string = LocaleController.getString("AttachLiveLocation", 2131558721);
                String lastString;
                if (lastLocationSharingCount == 0) {
                    lastString = string;
                }
                else {
                    --lastLocationSharingCount;
                    if (LocationController.getInstance(currentAccount).isSharingLocation(dialogId)) {
                        if (lastLocationSharingCount != 0) {
                            if (lastLocationSharingCount == 1 && user != null) {
                                lastString = String.format("%1$s - %2$s", string, LocaleController.formatString("SharingYouAndOtherName", 2131560773, UserObject.getFirstName(user)));
                            }
                            else {
                                lastString = String.format("%1$s - %2$s %3$s", string, LocaleController.getString("ChatYourSelfName", 2131559050), LocaleController.formatPluralString("AndOther", lastLocationSharingCount));
                            }
                        }
                        else {
                            lastString = String.format("%1$s - %2$s", string, LocaleController.getString("ChatYourSelfName", 2131559050));
                        }
                    }
                    else if (lastLocationSharingCount != 0) {
                        lastString = String.format("%1$s - %2$s %3$s", string, UserObject.getFirstName(user), LocaleController.formatPluralString("AndOther", lastLocationSharingCount));
                    }
                    else {
                        lastString = String.format("%1$s - %2$s", string, UserObject.getFirstName(user));
                    }
                }
                final String lastString2 = this.lastString;
                if (lastString2 != null && lastString.equals(lastString2)) {
                    return;
                }
                this.lastString = lastString;
                final int index = lastString.indexOf(string);
                final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)lastString);
                this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
                if (index >= 0) {
                    text.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("inappPlayerPerformer")), index, string.length() + index, 18);
                }
                this.titleTextView.setText((CharSequence)text);
            }
        }
    }
    
    private void checkPlayer(final boolean b) {
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        final View fragmentView = this.fragment.getFragmentView();
        boolean b2 = b;
        Label_0055: {
            if (!b) {
                b2 = b;
                if (fragmentView != null) {
                    if (fragmentView.getParent() != null) {
                        b2 = b;
                        if (((View)fragmentView.getParent()).getVisibility() == 0) {
                            break Label_0055;
                        }
                    }
                    b2 = true;
                }
            }
        }
        if (playingMessageObject != null && playingMessageObject.getId() != 0 && !playingMessageObject.isVideo()) {
            final int currentStyle = this.currentStyle;
            this.updateStyle(0);
            if (b2 && this.topPadding == 0.0f) {
                this.setTopPadding((float)AndroidUtilities.dp2(36.0f));
                final FragmentContextView additionalContextView = this.additionalContextView;
                if (additionalContextView != null && additionalContextView.getVisibility() == 0) {
                    ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0f);
                }
                else {
                    ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f);
                }
                this.yPosition = 0.0f;
            }
            if (!this.visible) {
                if (!b2) {
                    final AnimatorSet animatorSet = this.animatorSet;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        this.animatorSet = null;
                    }
                    this.animatorSet = new AnimatorSet();
                    final FragmentContextView additionalContextView2 = this.additionalContextView;
                    if (additionalContextView2 != null && additionalContextView2.getVisibility() == 0) {
                        ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(72.0f);
                    }
                    else {
                        ((FrameLayout$LayoutParams)this.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f);
                    }
                    this.animatorSet.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "topPadding", new float[] { (float)AndroidUtilities.dp2(36.0f) }) });
                    this.animatorSet.setDuration(200L);
                    this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(obj)) {
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
                this.visible = true;
                this.setVisibility(0);
            }
            if (MediaController.getInstance().isMessagePaused()) {
                this.playButton.setImageResource(2131165606);
                this.playButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPlay", 2131558409));
            }
            else {
                this.playButton.setImageResource(2131165605);
                this.playButton.setContentDescription((CharSequence)LocaleController.getString("AccActionPause", 2131558408));
            }
            if (this.lastMessageObject != playingMessageObject || currentStyle != 0) {
                this.lastMessageObject = playingMessageObject;
                SpannableStringBuilder text;
                if (!this.lastMessageObject.isVoice() && !this.lastMessageObject.isRoundVideo()) {
                    final ImageView playbackSpeedButton = this.playbackSpeedButton;
                    if (playbackSpeedButton != null) {
                        playbackSpeedButton.setAlpha(0.0f);
                        this.playbackSpeedButton.setEnabled(false);
                    }
                    this.titleTextView.setPadding(0, 0, 0, 0);
                    text = new SpannableStringBuilder((CharSequence)String.format("%s - %s", playingMessageObject.getMusicAuthor(), playingMessageObject.getMusicTitle()));
                    this.titleTextView.setEllipsize(TextUtils$TruncateAt.END);
                }
                else {
                    final ImageView playbackSpeedButton2 = this.playbackSpeedButton;
                    if (playbackSpeedButton2 != null) {
                        playbackSpeedButton2.setAlpha(1.0f);
                        this.playbackSpeedButton.setEnabled(true);
                    }
                    this.titleTextView.setPadding(0, 0, AndroidUtilities.dp(44.0f), 0);
                    text = new SpannableStringBuilder((CharSequence)String.format("%s %s", playingMessageObject.getMusicAuthor(), playingMessageObject.getMusicTitle()));
                    this.titleTextView.setEllipsize(TextUtils$TruncateAt.MIDDLE);
                }
                text.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("inappPlayerPerformer")), 0, playingMessageObject.getMusicAuthor().length(), 18);
                this.titleTextView.setText((CharSequence)text);
            }
        }
        else {
            this.lastMessageObject = null;
            if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
                this.checkCall(false);
                return;
            }
            if (this.visible) {
                this.visible = false;
                if (b2) {
                    if (this.getVisibility() != 8) {
                        this.setVisibility(8);
                    }
                    this.setTopPadding(0.0f);
                }
                else {
                    final AnimatorSet animatorSet2 = this.animatorSet;
                    if (animatorSet2 != null) {
                        animatorSet2.cancel();
                        this.animatorSet = null;
                    }
                    (this.animatorSet = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "topPadding", new float[] { 0.0f }) });
                    this.animatorSet.setDuration(200L);
                    this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator obj) {
                            if (FragmentContextView.this.animatorSet != null && FragmentContextView.this.animatorSet.equals(obj)) {
                                FragmentContextView.this.setVisibility(8);
                                FragmentContextView.this.animatorSet = null;
                            }
                        }
                    });
                    this.animatorSet.start();
                }
            }
        }
    }
    
    private void checkVisibility() {
        final boolean isLocation = this.isLocation;
        boolean sharingLocation = true;
        int visibility = 0;
        Label_0107: {
            if (isLocation) {
                final BaseFragment fragment = this.fragment;
                if (!(fragment instanceof DialogsActivity)) {
                    sharingLocation = LocationController.getInstance(fragment.getCurrentAccount()).isSharingLocation(((ChatActivity)this.fragment).getDialogId());
                    break Label_0107;
                }
                if (LocationController.getLocationsCount() != 0) {
                    break Label_0107;
                }
            }
            else {
                if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
                    break Label_0107;
                }
                final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.getId() != 0) {
                    break Label_0107;
                }
            }
            sharingLocation = false;
        }
        if (!sharingLocation) {
            visibility = 8;
        }
        this.setVisibility(visibility);
    }
    
    private void openSharingLocation(final LocationController.SharingLocationInfo sharingLocationInfo) {
        if (sharingLocationInfo != null) {
            if (this.fragment.getParentActivity() != null) {
                final LaunchActivity launchActivity = (LaunchActivity)this.fragment.getParentActivity();
                launchActivity.switchToAccount(sharingLocationInfo.messageObject.currentAccount, true);
                final LocationActivity locationActivity = new LocationActivity(2);
                locationActivity.setMessageObject(sharingLocationInfo.messageObject);
                locationActivity.setDelegate((LocationActivity.LocationActivityDelegate)new _$$Lambda$FragmentContextView$qSdNXrOdRWHj2Hn2uLFHHQL36iI(sharingLocationInfo, sharingLocationInfo.messageObject.getDialogId()));
                launchActivity.presentFragment(locationActivity);
            }
        }
    }
    
    private void updatePlaybackButton() {
        if (MediaController.getInstance().getPlaybackSpeed() > 1.0f) {
            this.playbackSpeedButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("inappPlayerPlayPause"), PorterDuff$Mode.MULTIPLY));
        }
        else {
            this.playbackSpeedButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("inappPlayerClose"), PorterDuff$Mode.MULTIPLY));
        }
    }
    
    private void updateStyle(final int currentStyle) {
        if (this.currentStyle == currentStyle) {
            return;
        }
        this.currentStyle = currentStyle;
        if (currentStyle != 0 && currentStyle != 2) {
            if (currentStyle == 1) {
                this.titleTextView.setText((CharSequence)LocaleController.getString("ReturnToCall", 2131560615));
                this.frameLayout.setBackgroundColor(Theme.getColor("returnToCallBackground"));
                this.frameLayout.setTag((Object)"returnToCallBackground");
                this.titleTextView.setTextColor(Theme.getColor("returnToCallText"));
                this.titleTextView.setTag((Object)"returnToCallText");
                this.closeButton.setVisibility(8);
                this.playButton.setVisibility(8);
                this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.titleTextView.setTextSize(1, 14.0f);
                this.titleTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 2.0f));
                this.titleTextView.setPadding(0, 0, 0, 0);
                final ImageView playbackSpeedButton = this.playbackSpeedButton;
                if (playbackSpeedButton != null) {
                    playbackSpeedButton.setVisibility(8);
                }
            }
        }
        else {
            this.frameLayout.setBackgroundColor(Theme.getColor("inappPlayerBackground"));
            this.frameLayout.setTag((Object)"inappPlayerBackground");
            this.titleTextView.setTextColor(Theme.getColor("inappPlayerTitle"));
            this.titleTextView.setTag((Object)"inappPlayerTitle");
            this.closeButton.setVisibility(0);
            this.playButton.setVisibility(0);
            this.titleTextView.setTypeface(Typeface.DEFAULT);
            this.titleTextView.setTextSize(1, 15.0f);
            if (currentStyle == 0) {
                this.playButton.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                this.titleTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 35.0f, 0.0f, 36.0f, 0.0f));
                final ImageView playbackSpeedButton2 = this.playbackSpeedButton;
                if (playbackSpeedButton2 != null) {
                    playbackSpeedButton2.setVisibility(0);
                }
                this.closeButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrClosePlayer", 2131558427));
            }
            else if (currentStyle == 2) {
                this.playButton.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 8.0f, 0.0f, 0.0f, 0.0f));
                this.titleTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 36.0f, 51, 51.0f, 0.0f, 36.0f, 0.0f));
                this.closeButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrStopLiveLocation", 2131558477));
            }
        }
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.liveLocationsChanged) {
            this.checkLiveLocation(false);
        }
        else if (n == NotificationCenter.liveLocationsCacheChanged) {
            if (this.fragment instanceof ChatActivity && ((ChatActivity)this.fragment).getDialogId() == (long)array[0]) {
                this.checkLocationString();
            }
        }
        else if (n != NotificationCenter.messagePlayingDidStart && n != NotificationCenter.messagePlayingPlayStateChanged && n != NotificationCenter.messagePlayingDidReset && n != NotificationCenter.didEndedCall) {
            if (n == NotificationCenter.didStartedCall) {
                this.checkCall(false);
            }
            else {
                this.checkPlayer(false);
            }
        }
        else {
            this.checkPlayer(false);
        }
    }
    
    public float getTopPadding() {
        return this.topPadding;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.isLocation) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsCacheChanged);
            final FragmentContextView additionalContextView = this.additionalContextView;
            if (additionalContextView != null) {
                additionalContextView.checkVisibility();
            }
            this.checkLiveLocation(true);
        }
        else {
            for (int i = 0; i < 3; ++i) {
                NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
                NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidStart);
            }
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didStartedCall);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didEndedCall);
            final FragmentContextView additionalContextView2 = this.additionalContextView;
            if (additionalContextView2 != null) {
                additionalContextView2.checkVisibility();
            }
            if (VoIPService.getSharedInstance() != null && VoIPService.getSharedInstance().getCallState() != 15) {
                this.checkCall(true);
            }
            else {
                this.checkPlayer(true);
                this.updatePlaybackButton();
            }
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.topPadding = 0.0f;
        if (this.isLocation) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsCacheChanged);
        }
        else {
            for (int i = 0; i < 3; ++i) {
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidStart);
            }
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didStartedCall);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didEndedCall);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, AndroidUtilities.dp2(39.0f));
    }
    
    public void setAdditionalContextView(final FragmentContextView additionalContextView) {
        this.additionalContextView = additionalContextView;
    }
    
    @Keep
    public void setTopPadding(final float topPadding) {
        this.topPadding = topPadding;
        if (this.fragment != null && this.getParent() != null) {
            final View fragmentView = this.fragment.getFragmentView();
            this.fragment.getActionBar();
            final FragmentContextView additionalContextView = this.additionalContextView;
            int dp;
            if (additionalContextView != null && additionalContextView.getVisibility() == 0 && this.additionalContextView.getParent() != null) {
                dp = AndroidUtilities.dp(36.0f);
            }
            else {
                dp = 0;
            }
            if (fragmentView != null && this.getParent() != null) {
                fragmentView.setPadding(0, (int)this.topPadding + dp, 0, 0);
            }
            if (this.isLocation) {
                final FragmentContextView additionalContextView2 = this.additionalContextView;
                if (additionalContextView2 != null) {
                    ((FrameLayout$LayoutParams)additionalContextView2.getLayoutParams()).topMargin = -AndroidUtilities.dp(36.0f) - (int)this.topPadding;
                }
            }
        }
    }
}
