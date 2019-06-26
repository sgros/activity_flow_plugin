// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.Intent;
import org.telegram.messenger.ImageLocation;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.FileLog;
import android.widget.ScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import android.view.ViewGroup$LayoutParams;
import android.view.View$OnTouchListener;
import android.view.View$MeasureSpec;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import android.content.DialogInterface$OnCancelListener;
import android.os.Vibrator;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.MotionEvent;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.os.Bundle;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.HeaderCell;
import android.widget.EditText;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.widget.TextView;
import org.telegram.ui.Components.RadialProgressView;
import android.view.View;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.animation.AnimatorSet;
import org.telegram.tgnet.TLRPC;
import android.widget.LinearLayout;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.AdminedChannelCell;
import java.util.ArrayList;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChannelCreateActivity extends BaseFragment implements NotificationCenterDelegate, ImageUpdaterDelegate
{
    private static final int done_button = 1;
    private ArrayList<AdminedChannelCell> adminedChannelCells;
    private TextInfoPrivacyCell adminedInfoCell;
    private LinearLayout adminnedChannelsLayout;
    private TLRPC.FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private TLRPC.FileLocation avatarBig;
    private AvatarDrawable avatarDrawable;
    private ImageView avatarEditor;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private boolean canCreatePublic;
    private int chatId;
    private int checkReqId;
    private Runnable checkRunnable;
    private TextView checkTextView;
    private boolean createAfterUpload;
    private int currentStep;
    private EditTextBoldCursor descriptionTextView;
    private View doneButton;
    private boolean donePressed;
    private EditText editText;
    private HeaderCell headerCell;
    private TextView helpTextView;
    private ImageUpdater imageUpdater;
    private TLRPC.ExportedChatInvite invite;
    private boolean isPrivate;
    private String lastCheckName;
    private boolean lastNameAvailable;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private LinearLayout linkContainer;
    private LoadingCell loadingAdminedCell;
    private boolean loadingAdminedChannels;
    private boolean loadingInvite;
    private EditTextEmoji nameTextView;
    private String nameToSet;
    private TextBlockCell privateContainer;
    private AlertDialog progressDialog;
    private LinearLayout publicContainer;
    private RadioButtonCell radioButtonCell1;
    private RadioButtonCell radioButtonCell2;
    private ShadowSectionCell sectionCell;
    private TextInfoPrivacyCell typeInfoCell;
    private TLRPC.InputFile uploadedAvatar;
    
    public ChannelCreateActivity(final Bundle bundle) {
        super(bundle);
        this.adminedChannelCells = new ArrayList<AdminedChannelCell>();
        this.canCreatePublic = true;
        this.currentStep = bundle.getInt("step", 0);
        final int currentStep = this.currentStep;
        if (currentStep == 0) {
            this.avatarDrawable = new AvatarDrawable();
            this.imageUpdater = new ImageUpdater();
            final TLRPC.TL_channels_checkUsername tl_channels_checkUsername = new TLRPC.TL_channels_checkUsername();
            tl_channels_checkUsername.username = "1";
            tl_channels_checkUsername.channel = new TLRPC.TL_inputChannelEmpty();
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_checkUsername, new _$$Lambda$ChannelCreateActivity$Dgmh8FVFPsTpRB5V7O64UrcVHdE(this));
        }
        else {
            if (currentStep == 1) {
                this.canCreatePublic = bundle.getBoolean("canCreatePublic", true);
                final boolean canCreatePublic = this.canCreatePublic;
                this.isPrivate = (canCreatePublic ^ true);
                if (!canCreatePublic) {
                    this.loadAdminedChannels();
                }
            }
            this.chatId = bundle.getInt("chat_id", 0);
        }
    }
    
    private boolean checkUserName(final String lastCheckName) {
        if (lastCheckName != null && lastCheckName.length() > 0) {
            this.checkTextView.setVisibility(0);
        }
        else {
            this.checkTextView.setVisibility(8);
        }
        final Runnable checkRunnable = this.checkRunnable;
        if (checkRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(checkRunnable);
            this.checkRunnable = null;
            this.lastCheckName = null;
            if (this.checkReqId != 0) {
                ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.checkReqId, true);
            }
        }
        this.lastNameAvailable = false;
        if (lastCheckName != null) {
            if (lastCheckName.startsWith("_") || lastCheckName.endsWith("_")) {
                this.checkTextView.setText((CharSequence)LocaleController.getString("LinkInvalid", 2131559755));
                this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                return false;
            }
            for (int i = 0; i < lastCheckName.length(); ++i) {
                final char char1 = lastCheckName.charAt(i);
                if (i == 0 && char1 >= '0' && char1 <= '9') {
                    this.checkTextView.setText((CharSequence)LocaleController.getString("LinkInvalidStartNumber", 2131559759));
                    this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    return false;
                }
                if ((char1 < '0' || char1 > '9') && (char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z') && char1 != '_') {
                    this.checkTextView.setText((CharSequence)LocaleController.getString("LinkInvalid", 2131559755));
                    this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
                    this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
                    return false;
                }
            }
        }
        if (lastCheckName == null || lastCheckName.length() < 5) {
            this.checkTextView.setText((CharSequence)LocaleController.getString("LinkInvalidShort", 2131559757));
            this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            return false;
        }
        if (lastCheckName.length() > 32) {
            this.checkTextView.setText((CharSequence)LocaleController.getString("LinkInvalidLong", 2131559756));
            this.checkTextView.setTag((Object)"windowBackgroundWhiteRedText4");
            this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            return false;
        }
        this.checkTextView.setText((CharSequence)LocaleController.getString("LinkChecking", 2131559750));
        this.checkTextView.setTag((Object)"windowBackgroundWhiteGrayText8");
        this.checkTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
        this.lastCheckName = lastCheckName;
        AndroidUtilities.runOnUIThread(this.checkRunnable = new _$$Lambda$ChannelCreateActivity$mDP7DqCeyyygG_tNsF0680xfQvY(this, lastCheckName), 300L);
        return true;
    }
    
    private void generateLink() {
        if (!this.loadingInvite) {
            if (this.invite == null) {
                this.loadingInvite = true;
                final TLRPC.TL_messages_exportChatInvite tl_messages_exportChatInvite = new TLRPC.TL_messages_exportChatInvite();
                tl_messages_exportChatInvite.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(-this.chatId);
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_exportChatInvite, new _$$Lambda$ChannelCreateActivity$B1f5C5wPqlSBUACduCE1syb6pss(this));
            }
        }
    }
    
    private void loadAdminedChannels() {
        if (this.loadingAdminedChannels) {
            return;
        }
        this.loadingAdminedChannels = true;
        this.updatePrivatePublic();
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_channels_getAdminedPublicChannels(), new _$$Lambda$ChannelCreateActivity$sX4Ap2SwpMlriw_rHlFC0Efiffk(this));
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
                    ChannelCreateActivity.this.avatarAnimation = null;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    if (ChannelCreateActivity.this.avatarAnimation != null) {
                        if (ChannelCreateActivity.this.avatarEditor != null) {
                            if (b) {
                                ChannelCreateActivity.this.avatarEditor.setVisibility(4);
                            }
                            else {
                                ChannelCreateActivity.this.avatarProgressView.setVisibility(4);
                            }
                            ChannelCreateActivity.this.avatarAnimation = null;
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
    
    private void showErrorAlert(final String s) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        int n = -1;
        final int hashCode = s.hashCode();
        if (hashCode != 288843630) {
            if (hashCode == 533175271) {
                if (s.equals("USERNAME_OCCUPIED")) {
                    n = 1;
                }
            }
        }
        else if (s.equals("USERNAME_INVALID")) {
            n = 0;
        }
        if (n != 0) {
            if (n != 1) {
                builder.setMessage(LocaleController.getString("ErrorOccurred", 2131559375));
            }
            else {
                builder.setMessage(LocaleController.getString("LinkInUse", 2131559753));
            }
        }
        else {
            builder.setMessage(LocaleController.getString("LinkInvalid", 2131559755));
        }
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        this.showDialog(builder.create());
    }
    
    private void updatePrivatePublic() {
        if (this.sectionCell == null) {
            return;
        }
        final boolean isPrivate = this.isPrivate;
        final int n = 8;
        if (!isPrivate && !this.canCreatePublic) {
            this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", 2131558916));
            this.typeInfoCell.setTag((Object)"windowBackgroundWhiteRedText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.linkContainer.setVisibility(8);
            this.sectionCell.setVisibility(8);
            if (this.loadingAdminedChannels) {
                this.loadingAdminedCell.setVisibility(0);
                this.adminnedChannelsLayout.setVisibility(8);
                final TextInfoPrivacyCell typeInfoCell = this.typeInfoCell;
                typeInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow"));
                this.adminedInfoCell.setVisibility(8);
            }
            else {
                final TextInfoPrivacyCell typeInfoCell2 = this.typeInfoCell;
                typeInfoCell2.setBackgroundDrawable(Theme.getThemedDrawable(typeInfoCell2.getContext(), 2131165394, "windowBackgroundGrayShadow"));
                this.loadingAdminedCell.setVisibility(8);
                this.adminnedChannelsLayout.setVisibility(0);
                this.adminedInfoCell.setVisibility(0);
            }
        }
        else {
            this.typeInfoCell.setTag((Object)"windowBackgroundWhiteGrayText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.sectionCell.setVisibility(0);
            this.adminedInfoCell.setVisibility(8);
            this.adminnedChannelsLayout.setVisibility(8);
            final TextInfoPrivacyCell typeInfoCell3 = this.typeInfoCell;
            typeInfoCell3.setBackgroundDrawable(Theme.getThemedDrawable(typeInfoCell3.getContext(), 2131165395, "windowBackgroundGrayShadow"));
            this.linkContainer.setVisibility(0);
            this.loadingAdminedCell.setVisibility(8);
            final TextInfoPrivacyCell typeInfoCell4 = this.typeInfoCell;
            int n2;
            String s;
            if (this.isPrivate) {
                n2 = 2131558990;
                s = "ChannelPrivateLinkHelp";
            }
            else {
                n2 = 2131559013;
                s = "ChannelUsernameHelp";
            }
            typeInfoCell4.setText(LocaleController.getString(s, n2));
            final HeaderCell headerCell = this.headerCell;
            int n3;
            String s2;
            if (this.isPrivate) {
                n3 = 2131558952;
                s2 = "ChannelInviteLinkTitle";
            }
            else {
                n3 = 2131558960;
                s2 = "ChannelLinkTitle";
            }
            headerCell.setText(LocaleController.getString(s2, n3));
            final LinearLayout publicContainer = this.publicContainer;
            int visibility;
            if (this.isPrivate) {
                visibility = 8;
            }
            else {
                visibility = 0;
            }
            publicContainer.setVisibility(visibility);
            final TextBlockCell privateContainer = this.privateContainer;
            int visibility2;
            if (this.isPrivate) {
                visibility2 = 0;
            }
            else {
                visibility2 = 8;
            }
            privateContainer.setVisibility(visibility2);
            final LinearLayout linkContainer = this.linkContainer;
            int dp;
            if (this.isPrivate) {
                dp = 0;
            }
            else {
                dp = AndroidUtilities.dp(7.0f);
            }
            linkContainer.setPadding(0, 0, 0, dp);
            final TextBlockCell privateContainer2 = this.privateContainer;
            final TLRPC.ExportedChatInvite invite = this.invite;
            String s3;
            if (invite != null) {
                s3 = invite.link;
            }
            else {
                s3 = LocaleController.getString("Loading", 2131559768);
            }
            privateContainer2.setText(s3, false);
            final TextView checkTextView = this.checkTextView;
            int visibility3 = n;
            if (!this.isPrivate) {
                visibility3 = n;
                if (checkTextView.length() != 0) {
                    visibility3 = 0;
                }
            }
            checkTextView.setVisibility(visibility3);
        }
        this.radioButtonCell1.setChecked(this.isPrivate ^ true, true);
        this.radioButtonCell2.setChecked(this.isPrivate, true);
        this.descriptionTextView.clearFocus();
        AndroidUtilities.hideKeyboard((View)this.descriptionTextView);
    }
    
    @Override
    public View createView(final Context context) {
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onDestroy();
        }
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int chat) {
                if (chat == -1) {
                    ChannelCreateActivity.this.finishFragment();
                }
                else if (chat == 1) {
                    if (ChannelCreateActivity.this.currentStep == 0) {
                        if (ChannelCreateActivity.this.donePressed || ChannelCreateActivity.this.getParentActivity() == null) {
                            return;
                        }
                        if (ChannelCreateActivity.this.nameTextView.length() == 0) {
                            final Vibrator vibrator = (Vibrator)ChannelCreateActivity.this.getParentActivity().getSystemService("vibrator");
                            if (vibrator != null) {
                                vibrator.vibrate(200L);
                            }
                            AndroidUtilities.shakeView((View)ChannelCreateActivity.this.nameTextView, 2.0f, 0);
                            return;
                        }
                        ChannelCreateActivity.this.donePressed = true;
                        if (ChannelCreateActivity.this.imageUpdater.uploadingImage != null) {
                            ChannelCreateActivity.this.createAfterUpload = true;
                            final ChannelCreateActivity this$0 = ChannelCreateActivity.this;
                            this$0.progressDialog = new AlertDialog((Context)this$0.getParentActivity(), 3);
                            ChannelCreateActivity.this.progressDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ChannelCreateActivity$1$b7xFRmQZDJGprfBGkWoRomCKvDQ(this));
                            ChannelCreateActivity.this.progressDialog.show();
                            return;
                        }
                        chat = MessagesController.getInstance(ChannelCreateActivity.this.currentAccount).createChat(ChannelCreateActivity.this.nameTextView.getText().toString(), new ArrayList<Integer>(), ChannelCreateActivity.this.descriptionTextView.getText().toString(), 2, ChannelCreateActivity.this);
                        final ChannelCreateActivity this$2 = ChannelCreateActivity.this;
                        this$2.progressDialog = new AlertDialog((Context)this$2.getParentActivity(), 3);
                        ChannelCreateActivity.this.progressDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ChannelCreateActivity$1$q1TLoAihH5rBozhaNhmW5QGac_s(this, chat));
                        ChannelCreateActivity.this.progressDialog.show();
                    }
                    else if (ChannelCreateActivity.this.currentStep == 1) {
                        if (!ChannelCreateActivity.this.isPrivate) {
                            if (ChannelCreateActivity.this.descriptionTextView.length() == 0) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ChannelCreateActivity.this.getParentActivity());
                                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                                builder.setMessage(LocaleController.getString("ChannelPublicEmptyUsername", 2131558992));
                                builder.setPositiveButton(LocaleController.getString("Close", 2131559117), null);
                                ChannelCreateActivity.this.showDialog(builder.create());
                                return;
                            }
                            if (!ChannelCreateActivity.this.lastNameAvailable) {
                                final Vibrator vibrator2 = (Vibrator)ChannelCreateActivity.this.getParentActivity().getSystemService("vibrator");
                                if (vibrator2 != null) {
                                    vibrator2.vibrate(200L);
                                }
                                AndroidUtilities.shakeView((View)ChannelCreateActivity.this.checkTextView, 2.0f, 0);
                                return;
                            }
                            MessagesController.getInstance(ChannelCreateActivity.this.currentAccount).updateChannelUserName(ChannelCreateActivity.this.chatId, ChannelCreateActivity.this.lastCheckName);
                        }
                        final Bundle bundle = new Bundle();
                        bundle.putInt("step", 2);
                        bundle.putInt("chatId", ChannelCreateActivity.this.chatId);
                        bundle.putInt("chatType", 2);
                        ChannelCreateActivity.this.presentFragment(new GroupCreateActivity(bundle), true);
                    }
                }
            }
        });
        this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        final int currentStep = this.currentStep;
        if (currentStep == 0) {
            super.actionBar.setTitle(LocaleController.getString("NewChannel", 2131559898));
            final SizeNotifierFrameLayout fragmentView = new SizeNotifierFrameLayout(context) {
                private boolean ignoreLayout;
                
                @Override
                protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                    final int childCount = this.getChildCount();
                    final int keyboardHeight = this.getKeyboardHeight();
                    final int dp = AndroidUtilities.dp(20.0f);
                    int i = 0;
                    int emojiPadding;
                    if (keyboardHeight <= dp && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                        emojiPadding = ChannelCreateActivity.this.nameTextView.getEmojiPadding();
                    }
                    else {
                        emojiPadding = 0;
                    }
                    this.setBottomClip(emojiPadding);
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
                            Label_0225: {
                                int n7;
                                int n8;
                                if (n6 != 1) {
                                    if (n6 != 5) {
                                        leftMargin = frameLayout$LayoutParams.leftMargin;
                                        break Label_0225;
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
                            int topMargin = 0;
                            Label_0327: {
                                int n9;
                                int n10;
                                if (n5 != 16) {
                                    if (n5 == 48) {
                                        topMargin = frameLayout$LayoutParams.topMargin + this.getPaddingTop();
                                        break Label_0327;
                                    }
                                    if (n5 != 80) {
                                        topMargin = frameLayout$LayoutParams.topMargin;
                                        break Label_0327;
                                    }
                                    n9 = n4 - emojiPadding - n2 - measuredHeight;
                                    n10 = frameLayout$LayoutParams.bottomMargin;
                                }
                                else {
                                    n9 = (n4 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                    n10 = frameLayout$LayoutParams.bottomMargin;
                                }
                                topMargin = n9 - n10;
                            }
                            int n11 = topMargin;
                            if (ChannelCreateActivity.this.nameTextView != null) {
                                n11 = topMargin;
                                if (ChannelCreateActivity.this.nameTextView.isPopupView(child)) {
                                    int measuredHeight2;
                                    int n12;
                                    if (AndroidUtilities.isTablet()) {
                                        measuredHeight2 = this.getMeasuredHeight();
                                        n12 = child.getMeasuredHeight();
                                    }
                                    else {
                                        measuredHeight2 = this.getMeasuredHeight() + this.getKeyboardHeight();
                                        n12 = child.getMeasuredHeight();
                                    }
                                    n11 = measuredHeight2 - n12;
                                }
                            }
                            child.layout(leftMargin, n11, measuredWidth + leftMargin, measuredHeight + n11);
                        }
                        ++i;
                    }
                    this.notifyHeightChanged();
                }
                
                protected void onMeasure(final int n, final int n2) {
                    final int size = View$MeasureSpec.getSize(n);
                    final int size2 = View$MeasureSpec.getSize(n2);
                    this.setMeasuredDimension(size, size2);
                    final int n3 = size2 - this.getPaddingTop();
                    this.measureChildWithMargins((View)ChannelCreateActivity.this.actionBar, n, 0, n2, 0);
                    final int keyboardHeight = this.getKeyboardHeight();
                    final int dp = AndroidUtilities.dp(20.0f);
                    int i = 0;
                    if (keyboardHeight > dp) {
                        this.ignoreLayout = true;
                        ChannelCreateActivity.this.nameTextView.hideEmojiView();
                        this.ignoreLayout = false;
                    }
                    while (i < this.getChildCount()) {
                        final View child = this.getChildAt(i);
                        if (child != null && child.getVisibility() != 8) {
                            if (child != ChannelCreateActivity.this.actionBar) {
                                if (ChannelCreateActivity.this.nameTextView != null && ChannelCreateActivity.this.nameTextView.isPopupView(child)) {
                                    if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                        child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                                    }
                                    else if (AndroidUtilities.isTablet()) {
                                        final int measureSpec = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                                        float n4;
                                        if (AndroidUtilities.isTablet()) {
                                            n4 = 200.0f;
                                        }
                                        else {
                                            n4 = 320.0f;
                                        }
                                        child.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(n4), n3 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                                    }
                                    else {
                                        child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n3 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                                    }
                                }
                                else {
                                    this.measureChildWithMargins(child, n, 0, n2, 0);
                                }
                            }
                        }
                        ++i;
                    }
                }
                
                public void requestLayout() {
                    if (this.ignoreLayout) {
                        return;
                    }
                    super.requestLayout();
                }
            };
            fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChannelCreateActivity$lIiXta2UxN2m1Vi_R2Lzg4Rdjg4.INSTANCE);
            (super.fragmentView = (View)fragmentView).setTag((Object)"windowBackgroundWhite");
            super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            (this.linearLayout = new LinearLayout(context)).setOrientation(1);
            fragmentView.addView((View)this.linearLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
            final FrameLayout frameLayout = new FrameLayout(context);
            this.linearLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.avatarImage = new BackupImageView(context) {
                public void invalidate() {
                    if (ChannelCreateActivity.this.avatarOverlay != null) {
                        ChannelCreateActivity.this.avatarOverlay.invalidate();
                    }
                    super.invalidate();
                }
                
                public void invalidate(final int n, final int n2, final int n3, final int n4) {
                    if (ChannelCreateActivity.this.avatarOverlay != null) {
                        ChannelCreateActivity.this.avatarOverlay.invalidate();
                    }
                    super.invalidate(n, n2, n3, n4);
                }
            }).setRoundRadius(AndroidUtilities.dp(32.0f));
            this.avatarDrawable.setInfo(5, null, null, false);
            this.avatarImage.setImageDrawable(this.avatarDrawable);
            final BackupImageView avatarImage = this.avatarImage;
            int n;
            if (LocaleController.isRTL) {
                n = 5;
            }
            else {
                n = 3;
            }
            float n2;
            if (LocaleController.isRTL) {
                n2 = 0.0f;
            }
            else {
                n2 = 16.0f;
            }
            float n3;
            if (LocaleController.isRTL) {
                n3 = 16.0f;
            }
            else {
                n3 = 0.0f;
            }
            frameLayout.addView((View)avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n | 0x30, n2, 12.0f, n3, 12.0f));
            final Paint paint = new Paint(1);
            paint.setColor(1426063360);
            this.avatarOverlay = new View(context) {
                protected void onDraw(final Canvas canvas) {
                    if (ChannelCreateActivity.this.avatarImage != null && ChannelCreateActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                        paint.setAlpha((int)(ChannelCreateActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                        canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0f), paint);
                    }
                }
            };
            final View avatarOverlay = this.avatarOverlay;
            int n4;
            if (LocaleController.isRTL) {
                n4 = 5;
            }
            else {
                n4 = 3;
            }
            float n5;
            if (LocaleController.isRTL) {
                n5 = 0.0f;
            }
            else {
                n5 = 16.0f;
            }
            float n6;
            if (LocaleController.isRTL) {
                n6 = 16.0f;
            }
            else {
                n6 = 0.0f;
            }
            frameLayout.addView(avatarOverlay, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n4 | 0x30, n5, 12.0f, n6, 12.0f));
            this.avatarOverlay.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelCreateActivity$s4ZwNFyULCtdH_y7TaKdhDy0a3c(this));
            (this.avatarEditor = new ImageView(context) {
                public void invalidate() {
                    super.invalidate();
                    ChannelCreateActivity.this.avatarOverlay.invalidate();
                }
                
                public void invalidate(final int n, final int n2, final int n3, final int n4) {
                    super.invalidate(n, n2, n3, n4);
                    ChannelCreateActivity.this.avatarOverlay.invalidate();
                }
            }).setScaleType(ImageView$ScaleType.CENTER);
            this.avatarEditor.setImageResource(2131165572);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            final ImageView avatarEditor = this.avatarEditor;
            int n7;
            if (LocaleController.isRTL) {
                n7 = 5;
            }
            else {
                n7 = 3;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 0.0f;
            }
            else {
                n8 = 16.0f;
            }
            float n9;
            if (LocaleController.isRTL) {
                n9 = 16.0f;
            }
            else {
                n9 = 0.0f;
            }
            frameLayout.addView((View)avatarEditor, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n7 | 0x30, n8, 12.0f, n9, 12.0f));
            (this.avatarProgressView = new RadialProgressView(context)).setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            final RadialProgressView avatarProgressView = this.avatarProgressView;
            int n10;
            if (LocaleController.isRTL) {
                n10 = 5;
            }
            else {
                n10 = 3;
            }
            float n11;
            if (LocaleController.isRTL) {
                n11 = 0.0f;
            }
            else {
                n11 = 16.0f;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 16.0f;
            }
            else {
                n12 = 0.0f;
            }
            frameLayout.addView((View)avatarProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n10 | 0x30, n11, 12.0f, n12, 12.0f));
            this.showAvatarProgress(false, false);
            (this.nameTextView = new EditTextEmoji(context, fragmentView, this, 0)).setHint(LocaleController.getString("EnterChannelName", 2131559367));
            final String nameToSet = this.nameToSet;
            if (nameToSet != null) {
                this.nameTextView.setText(nameToSet);
                this.nameToSet = null;
            }
            this.nameTextView.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(100) });
            final EditTextEmoji nameTextView2 = this.nameTextView;
            float n13;
            if (LocaleController.isRTL) {
                n13 = 5.0f;
            }
            else {
                n13 = 96.0f;
            }
            float n14;
            if (LocaleController.isRTL) {
                n14 = 96.0f;
            }
            else {
                n14 = 5.0f;
            }
            frameLayout.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 16, n13, 0.0f, n14, 0.0f));
            (this.descriptionTextView = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
            this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setBackgroundDrawable(Theme.createEditTextDrawable(context, false));
            this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
            final EditTextBoldCursor descriptionTextView = this.descriptionTextView;
            int gravity;
            if (LocaleController.isRTL) {
                gravity = 5;
            }
            else {
                gravity = 3;
            }
            descriptionTextView.setGravity(gravity);
            this.descriptionTextView.setInputType(180225);
            this.descriptionTextView.setImeOptions(6);
            this.descriptionTextView.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(120) });
            this.descriptionTextView.setHint((CharSequence)LocaleController.getString("DescriptionPlaceholder", 2131559265));
            this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
            this.descriptionTextView.setCursorWidth(1.5f);
            this.linearLayout.addView((View)this.descriptionTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 24.0f, 18.0f, 24.0f, 0.0f));
            this.descriptionTextView.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ChannelCreateActivity$MiQ4URkqTYXvUua86XByJz6HPDg(this));
            this.descriptionTextView.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            (this.helpTextView = new TextView(context)).setTextSize(1, 15.0f);
            this.helpTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText8"));
            final TextView helpTextView = this.helpTextView;
            int gravity2;
            if (LocaleController.isRTL) {
                gravity2 = 5;
            }
            else {
                gravity2 = 3;
            }
            helpTextView.setGravity(gravity2);
            this.helpTextView.setText((CharSequence)LocaleController.getString("DescriptionInfo", 2131559263));
            final LinearLayout linearLayout = this.linearLayout;
            final TextView helpTextView2 = this.helpTextView;
            int n15;
            if (LocaleController.isRTL) {
                n15 = 5;
            }
            else {
                n15 = 3;
            }
            linearLayout.addView((View)helpTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n15, 24, 10, 24, 20));
        }
        else if (currentStep == 1) {
            super.fragmentView = (View)new ScrollView(context);
            final ScrollView scrollView = (ScrollView)super.fragmentView;
            scrollView.setFillViewport(true);
            (this.linearLayout = new LinearLayout(context)).setOrientation(1);
            scrollView.addView((View)this.linearLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
            super.actionBar.setTitle(LocaleController.getString("ChannelSettings", 2131558998));
            super.fragmentView.setTag((Object)"windowBackgroundGray");
            super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            (this.linearLayout2 = new LinearLayout(context)).setOrientation(1);
            this.linearLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout.addView((View)this.linearLayout2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.radioButtonCell1 = new RadioButtonCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", 2131558991), LocaleController.getString("ChannelPublicInfo", 2131558993), false, this.isPrivate ^ true);
            this.linearLayout2.addView((View)this.radioButtonCell1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.radioButtonCell1.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelCreateActivity$ERDUGoRSlmYfCpWGH5A0thCtafY(this));
            (this.radioButtonCell2 = new RadioButtonCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", 2131558988), LocaleController.getString("ChannelPrivateInfo", 2131558989), false, this.isPrivate);
            this.linearLayout2.addView((View)this.radioButtonCell2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.radioButtonCell2.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelCreateActivity$4I7Y_V5uH9YemI181a2NrRWSAsc(this));
            this.sectionCell = new ShadowSectionCell(context);
            this.linearLayout.addView((View)this.sectionCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.linkContainer = new LinearLayout(context)).setOrientation(1);
            this.linkContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.linearLayout.addView((View)this.linkContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.headerCell = new HeaderCell(context);
            this.linkContainer.addView((View)this.headerCell);
            (this.publicContainer = new LinearLayout(context)).setOrientation(0);
            this.linkContainer.addView((View)this.publicContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 17.0f, 7.0f, 17.0f, 0.0f));
            this.editText = new EditText(context);
            final EditText editText = this.editText;
            final StringBuilder sb = new StringBuilder();
            sb.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
            sb.append("/");
            editText.setText((CharSequence)sb.toString());
            this.editText.setTextSize(1, 18.0f);
            this.editText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.editText.setMaxLines(1);
            this.editText.setLines(1);
            this.editText.setEnabled(false);
            this.editText.setBackgroundDrawable((Drawable)null);
            this.editText.setPadding(0, 0, 0, 0);
            this.editText.setSingleLine(true);
            this.editText.setInputType(163840);
            this.editText.setImeOptions(6);
            this.publicContainer.addView((View)this.editText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 36));
            (this.descriptionTextView = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
            this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setMaxLines(1);
            this.descriptionTextView.setLines(1);
            this.descriptionTextView.setBackgroundDrawable((Drawable)null);
            this.descriptionTextView.setPadding(0, 0, 0, 0);
            this.descriptionTextView.setSingleLine(true);
            this.descriptionTextView.setInputType(163872);
            this.descriptionTextView.setImeOptions(6);
            this.descriptionTextView.setHint((CharSequence)LocaleController.getString("ChannelUsernamePlaceholder", 2131559014));
            this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
            this.descriptionTextView.setCursorWidth(1.5f);
            this.publicContainer.addView((View)this.descriptionTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36));
            this.descriptionTextView.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    final ChannelCreateActivity this$0 = ChannelCreateActivity.this;
                    this$0.checkUserName(this$0.descriptionTextView.getText().toString());
                }
            });
            (this.privateContainer = new TextBlockCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.linkContainer.addView((View)this.privateContainer);
            this.privateContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelCreateActivity$9kLD_6LVZi1jry2jmUaAGbUgTCw(this));
            (this.checkTextView = new TextView(context)).setTextSize(1, 15.0f);
            final TextView checkTextView = this.checkTextView;
            int gravity3;
            if (LocaleController.isRTL) {
                gravity3 = 5;
            }
            else {
                gravity3 = 3;
            }
            checkTextView.setGravity(gravity3);
            this.checkTextView.setVisibility(8);
            final LinearLayout linkContainer = this.linkContainer;
            final TextView checkTextView2 = this.checkTextView;
            int n16;
            if (LocaleController.isRTL) {
                n16 = 5;
            }
            else {
                n16 = 3;
            }
            linkContainer.addView((View)checkTextView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n16, 17, 3, 17, 7));
            (this.typeInfoCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.linearLayout.addView((View)this.typeInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.loadingAdminedCell = new LoadingCell(context);
            this.linearLayout.addView((View)this.loadingAdminedCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.adminnedChannelsLayout = new LinearLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.adminnedChannelsLayout.setOrientation(1);
            this.linearLayout.addView((View)this.adminnedChannelsLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.adminedInfoCell = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            this.linearLayout.addView((View)this.adminedInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.updatePrivatePublic();
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int intValue, final int n, final Object... array) {
        if (intValue == NotificationCenter.chatDidFailCreate) {
            final AlertDialog progressDialog = this.progressDialog;
            if (progressDialog != null) {
                try {
                    progressDialog.dismiss();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            this.donePressed = false;
        }
        else if (intValue == NotificationCenter.chatDidCreated) {
            final AlertDialog progressDialog2 = this.progressDialog;
            if (progressDialog2 != null) {
                try {
                    progressDialog2.dismiss();
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            intValue = (int)array[0];
            final Bundle bundle = new Bundle();
            bundle.putInt("step", 1);
            bundle.putInt("chat_id", intValue);
            bundle.putBoolean("canCreatePublic", this.canCreatePublic);
            if (this.uploadedAvatar != null) {
                MessagesController.getInstance(super.currentAccount).changeChatAvatar(intValue, this.uploadedAvatar, this.avatar, this.avatarBig);
            }
            this.presentFragment(new ChannelCreateActivity(bundle), true);
        }
    }
    
    @Override
    public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ChannelCreateActivity$m0E_xD_NIIwbm9llzALGLpdnpvI(this, inputFile, photoSize2, photoSize));
    }
    
    @Override
    public String getInitialSearchString() {
        return this.nameTextView.getText().toString();
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c $$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c = new _$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.descriptionTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.descriptionTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.helpTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText8"), new ThemeDescription((View)this.linearLayout2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.linkContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(this.sectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.headerCell, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteRedText4"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGrayText8"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhiteGreenText"), new ThemeDescription((View)this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText4"), new ThemeDescription((View)this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.privateContainer, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.privateContainer, 0, new Class[] { TextBlockCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.loadingAdminedCell, 0, new Class[] { LoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteLinkText"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "deleteButton" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription(null, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChannelCreateActivity$ieYbFS24yeOuvItbU135YWldd4c, "avatar_backgroundPink") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        this.imageUpdater.onActivityResult(n, n2, intent);
    }
    
    @Override
    public boolean onBackPressed() {
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null && nameTextView.isPopupShowing()) {
            this.nameTextView.hidePopup(true);
            return false;
        }
        return true;
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidFailCreate);
        if (this.currentStep == 1) {
            this.generateLink();
        }
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.parentFragment = this;
            imageUpdater.delegate = (ImageUpdater.ImageUpdaterDelegate)this;
        }
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidFailCreate);
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onDestroy();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onPause();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onResume();
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    public void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && this.currentStep != 1) {
            this.nameTextView.openKeyboard();
        }
    }
    
    @Override
    public void restoreSelfArgs(final Bundle bundle) {
        if (this.currentStep == 0) {
            final ImageUpdater imageUpdater = this.imageUpdater;
            if (imageUpdater != null) {
                imageUpdater.currentPicturePath = bundle.getString("path");
            }
            final String string = bundle.getString("nameTextView");
            if (string != null) {
                final EditTextEmoji nameTextView = this.nameTextView;
                if (nameTextView != null) {
                    nameTextView.setText(string);
                }
                else {
                    this.nameToSet = string;
                }
            }
        }
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        if (this.currentStep == 0) {
            final ImageUpdater imageUpdater = this.imageUpdater;
            if (imageUpdater != null) {
                final String currentPicturePath = imageUpdater.currentPicturePath;
                if (currentPicturePath != null) {
                    bundle.putString("path", currentPicturePath);
                }
            }
            final EditTextEmoji nameTextView = this.nameTextView;
            if (nameTextView != null) {
                final String string = nameTextView.getText().toString();
                if (string != null && string.length() != 0) {
                    bundle.putString("nameTextView", string);
                }
            }
        }
    }
}
