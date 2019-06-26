// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.FileLog;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View$OnClickListener;
import org.telegram.ui.Components.LayoutHelper;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Rect;
import android.widget.ScrollView;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.MessagesStorage;
import android.content.Context;
import org.telegram.messenger.ChatObject;
import android.view.View;
import android.os.Vibrator;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.MessagesController;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.HeaderCell;
import android.widget.EditText;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.widget.LinearLayout;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.AdminedChannelCell;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChatEditTypeActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int done_button = 1;
    private ArrayList<AdminedChannelCell> adminedChannelCells;
    private ShadowSectionCell adminedInfoCell;
    private LinearLayout adminnedChannelsLayout;
    private boolean canCreatePublic;
    private int chatId;
    private int checkReqId;
    private Runnable checkRunnable;
    private TextInfoPrivacyCell checkTextView;
    private TextSettingsCell copyCell;
    private TLRPC.Chat currentChat;
    private EditText editText;
    private HeaderCell headerCell;
    private HeaderCell headerCell2;
    private TLRPC.ChatFull info;
    private TextInfoPrivacyCell infoCell;
    private TLRPC.ExportedChatInvite invite;
    private boolean isChannel;
    private boolean isPrivate;
    private String lastCheckName;
    private boolean lastNameAvailable;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutTypeContainer;
    private LinearLayout linkContainer;
    private LoadingCell loadingAdminedCell;
    private boolean loadingAdminedChannels;
    private boolean loadingInvite;
    private LinearLayout privateContainer;
    private TextBlockCell privateTextView;
    private LinearLayout publicContainer;
    private RadioButtonCell radioButtonCell1;
    private RadioButtonCell radioButtonCell2;
    private TextSettingsCell revokeCell;
    private ShadowSectionCell sectionCell2;
    private TextSettingsCell shareCell;
    private TextSettingsCell textCell;
    private TextSettingsCell textCell2;
    private TextInfoPrivacyCell typeInfoCell;
    private EditTextBoldCursor usernameTextView;
    
    public ChatEditTypeActivity(final int chatId) {
        this.canCreatePublic = true;
        this.adminedChannelCells = new ArrayList<AdminedChannelCell>();
        this.chatId = chatId;
    }
    
    private boolean checkUserName(final String lastCheckName) {
        if (lastCheckName != null && lastCheckName.length() > 0) {
            this.checkTextView.setVisibility(0);
        }
        else {
            this.checkTextView.setVisibility(8);
        }
        final TextInfoPrivacyCell typeInfoCell = this.typeInfoCell;
        Drawable themedDrawable;
        if (this.checkTextView.getVisibility() == 0) {
            themedDrawable = null;
        }
        else {
            themedDrawable = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow");
        }
        typeInfoCell.setBackgroundDrawable(themedDrawable);
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
                this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131559755));
                this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
                return false;
            }
            for (int i = 0; i < lastCheckName.length(); ++i) {
                final char char1 = lastCheckName.charAt(i);
                if (i == 0 && char1 >= '0' && char1 <= '9') {
                    if (this.isChannel) {
                        this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumber", 2131559759));
                    }
                    else {
                        this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumberMega", 2131559760));
                    }
                    this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
                    return false;
                }
                if ((char1 < '0' || char1 > '9') && (char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z') && char1 != '_') {
                    this.checkTextView.setText(LocaleController.getString("LinkInvalid", 2131559755));
                    this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
                    return false;
                }
            }
        }
        if (lastCheckName == null || lastCheckName.length() < 5) {
            if (this.isChannel) {
                this.checkTextView.setText(LocaleController.getString("LinkInvalidShort", 2131559757));
            }
            else {
                this.checkTextView.setText(LocaleController.getString("LinkInvalidShortMega", 2131559758));
            }
            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            return false;
        }
        if (lastCheckName.length() > 32) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidLong", 2131559756));
            this.checkTextView.setTextColor("windowBackgroundWhiteRedText4");
            return false;
        }
        this.checkTextView.setText(LocaleController.getString("LinkChecking", 2131559750));
        this.checkTextView.setTextColor("windowBackgroundWhiteGrayText8");
        this.lastCheckName = lastCheckName;
        AndroidUtilities.runOnUIThread(this.checkRunnable = new _$$Lambda$ChatEditTypeActivity$kFJ66e_ln7abTbKYBYoWFyd6BAI(this, lastCheckName), 300L);
        return true;
    }
    
    private void generateLink(final boolean b) {
        this.loadingInvite = true;
        final TLRPC.TL_messages_exportChatInvite tl_messages_exportChatInvite = new TLRPC.TL_messages_exportChatInvite();
        tl_messages_exportChatInvite.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(-this.chatId);
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_exportChatInvite, new _$$Lambda$ChatEditTypeActivity$NQoUsXIxZfHWCE1utCEcp_nY2As(this, b)), super.classGuid);
    }
    
    private void loadAdminedChannels() {
        if (!this.loadingAdminedChannels) {
            if (this.adminnedChannelsLayout != null) {
                this.loadingAdminedChannels = true;
                this.updatePrivatePublic();
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_channels_getAdminedPublicChannels(), new _$$Lambda$ChatEditTypeActivity$kDy8MSXJZPphT1ROvE66SmVIgsc(this));
            }
        }
    }
    
    private void processDone() {
        Label_0108: {
            if (!this.isPrivate) {
                if (this.currentChat.username != null || this.usernameTextView.length() == 0) {
                    final String username = this.currentChat.username;
                    if (username == null || username.equalsIgnoreCase(this.usernameTextView.getText().toString())) {
                        break Label_0108;
                    }
                }
                if (this.usernameTextView.length() != 0 && !this.lastNameAvailable) {
                    final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
                    if (vibrator != null) {
                        vibrator.vibrate(200L);
                    }
                    AndroidUtilities.shakeView((View)this.checkTextView, 2.0f, 0);
                    return;
                }
            }
        }
        String username2 = this.currentChat.username;
        String string = "";
        if (username2 == null) {
            username2 = "";
        }
        if (!this.isPrivate) {
            string = this.usernameTextView.getText().toString();
        }
        if (!username2.equals(string)) {
            if (!ChatObject.isChannel(this.currentChat)) {
                MessagesController.getInstance(super.currentAccount).convertToMegaGroup((Context)this.getParentActivity(), this.chatId, new _$$Lambda$ChatEditTypeActivity$wzFYEdqnqyRZtP7rz7ySevzOMoo(this));
                return;
            }
            MessagesController.getInstance(super.currentAccount).updateChannelUserName(this.chatId, string);
            this.currentChat.username = string;
        }
        this.finishFragment();
    }
    
    private void updatePrivatePublic() {
        if (this.sectionCell2 == null) {
            return;
        }
        final boolean isPrivate = this.isPrivate;
        final Drawable drawable = null;
        final int n = 8;
        if (!isPrivate && !this.canCreatePublic) {
            this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", 2131558916));
            this.typeInfoCell.setTag((Object)"windowBackgroundWhiteRedText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText4"));
            this.linkContainer.setVisibility(8);
            this.sectionCell2.setVisibility(8);
            this.adminedInfoCell.setVisibility(0);
            if (this.loadingAdminedChannels) {
                this.loadingAdminedCell.setVisibility(0);
                this.adminnedChannelsLayout.setVisibility(8);
                final TextInfoPrivacyCell typeInfoCell = this.typeInfoCell;
                Drawable themedDrawable;
                if (this.checkTextView.getVisibility() == 0) {
                    themedDrawable = null;
                }
                else {
                    themedDrawable = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow");
                }
                typeInfoCell.setBackgroundDrawable(themedDrawable);
                this.adminedInfoCell.setBackgroundDrawable((Drawable)null);
            }
            else {
                final ShadowSectionCell adminedInfoCell = this.adminedInfoCell;
                adminedInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(adminedInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow"));
                final TextInfoPrivacyCell typeInfoCell2 = this.typeInfoCell;
                typeInfoCell2.setBackgroundDrawable(Theme.getThemedDrawable(typeInfoCell2.getContext(), 2131165396, "windowBackgroundGrayShadow"));
                this.loadingAdminedCell.setVisibility(8);
                this.adminnedChannelsLayout.setVisibility(0);
            }
        }
        else {
            this.typeInfoCell.setTag((Object)"windowBackgroundWhiteGrayText4");
            this.typeInfoCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
            this.sectionCell2.setVisibility(0);
            this.adminedInfoCell.setVisibility(8);
            final TextInfoPrivacyCell typeInfoCell3 = this.typeInfoCell;
            typeInfoCell3.setBackgroundDrawable(Theme.getThemedDrawable(typeInfoCell3.getContext(), 2131165395, "windowBackgroundGrayShadow"));
            this.adminnedChannelsLayout.setVisibility(8);
            this.linkContainer.setVisibility(0);
            this.loadingAdminedCell.setVisibility(8);
            if (this.isChannel) {
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
                String text;
                if (this.isPrivate) {
                    text = LocaleController.getString("ChannelInviteLinkTitle", 2131558952);
                }
                else {
                    text = LocaleController.getString("ChannelLinkTitle", 2131558960);
                }
                headerCell.setText(text);
            }
            else {
                final TextInfoPrivacyCell typeInfoCell5 = this.typeInfoCell;
                int n3;
                String s2;
                if (this.isPrivate) {
                    n3 = 2131559833;
                    s2 = "MegaPrivateLinkHelp";
                }
                else {
                    n3 = 2131559836;
                    s2 = "MegaUsernameHelp";
                }
                typeInfoCell5.setText(LocaleController.getString(s2, n3));
                final HeaderCell headerCell2 = this.headerCell;
                String text2;
                if (this.isPrivate) {
                    text2 = LocaleController.getString("ChannelInviteLinkTitle", 2131558952);
                }
                else {
                    text2 = LocaleController.getString("ChannelLinkTitle", 2131558960);
                }
                headerCell2.setText(text2);
            }
            final LinearLayout publicContainer = this.publicContainer;
            int visibility;
            if (this.isPrivate) {
                visibility = 8;
            }
            else {
                visibility = 0;
            }
            publicContainer.setVisibility(visibility);
            final LinearLayout privateContainer = this.privateContainer;
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
            final TextBlockCell privateTextView = this.privateTextView;
            final TLRPC.ExportedChatInvite invite = this.invite;
            String s3;
            if (invite != null) {
                s3 = invite.link;
            }
            else {
                s3 = LocaleController.getString("Loading", 2131559768);
            }
            privateTextView.setText(s3, true);
            final TextInfoPrivacyCell checkTextView = this.checkTextView;
            int visibility3 = n;
            if (!this.isPrivate) {
                visibility3 = n;
                if (checkTextView.length() != 0) {
                    visibility3 = 0;
                }
            }
            checkTextView.setVisibility(visibility3);
            final TextInfoPrivacyCell typeInfoCell6 = this.typeInfoCell;
            Drawable themedDrawable2;
            if (this.checkTextView.getVisibility() == 0) {
                themedDrawable2 = drawable;
            }
            else {
                themedDrawable2 = Theme.getThemedDrawable(this.typeInfoCell.getContext(), 2131165395, "windowBackgroundGrayShadow");
            }
            typeInfoCell6.setBackgroundDrawable(themedDrawable2);
        }
        this.radioButtonCell1.setChecked(this.isPrivate ^ true, true);
        this.radioButtonCell2.setChecked(this.isPrivate, true);
        this.usernameTextView.clearFocus();
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChatEditTypeActivity.this.finishFragment();
                }
                else if (n == 1) {
                    ChatEditTypeActivity.this.processDone();
                }
            }
        });
        super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        (super.fragmentView = (View)new ScrollView(context) {
            public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                rect.bottom += AndroidUtilities.dp(60.0f);
                return super.requestChildRectangleOnScreen(view, rect, b);
            }
        }).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final ScrollView scrollView = (ScrollView)super.fragmentView;
        scrollView.setFillViewport(true);
        scrollView.addView((View)(this.linearLayout = new LinearLayout(context)), (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
        this.linearLayout.setOrientation(1);
        if (this.isChannel) {
            super.actionBar.setTitle(LocaleController.getString("ChannelSettingsTitle", 2131559000));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("GroupSettingsTitle", 2131559614));
        }
        (this.linearLayoutTypeContainer = new LinearLayout(context)).setOrientation(1);
        this.linearLayoutTypeContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout.addView((View)this.linearLayoutTypeContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.headerCell2 = new HeaderCell(context, 23)).setHeight(46);
        if (this.isChannel) {
            this.headerCell2.setText(LocaleController.getString("ChannelTypeHeader", 2131559006));
        }
        else {
            this.headerCell2.setText(LocaleController.getString("GroupTypeHeader", 2131559618));
        }
        this.linearLayoutTypeContainer.addView((View)this.headerCell2);
        (this.radioButtonCell2 = new RadioButtonCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (this.isChannel) {
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", 2131558988), LocaleController.getString("ChannelPrivateInfo", 2131558989), false, this.isPrivate);
        }
        else {
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("MegaPrivate", 2131559831), LocaleController.getString("MegaPrivateInfo", 2131559832), false, this.isPrivate);
        }
        this.linearLayoutTypeContainer.addView((View)this.radioButtonCell2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.radioButtonCell2.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditTypeActivity$ocPS9yaO8oX3caNaqb3brf0F0tM(this));
        (this.radioButtonCell1 = new RadioButtonCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (this.isChannel) {
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", 2131558991), LocaleController.getString("ChannelPublicInfo", 2131558993), false, this.isPrivate ^ true);
        }
        else {
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("MegaPublic", 2131559834), LocaleController.getString("MegaPublicInfo", 2131559835), false, this.isPrivate ^ true);
        }
        this.linearLayoutTypeContainer.addView((View)this.radioButtonCell1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.radioButtonCell1.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditTypeActivity$IpgElbmOCajv_apzoxCqnnTdt5I(this));
        this.sectionCell2 = new ShadowSectionCell(context);
        this.linearLayout.addView((View)this.sectionCell2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.linkContainer = new LinearLayout(context)).setOrientation(1);
        this.linkContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.linearLayout.addView((View)this.linkContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.headerCell = new HeaderCell(context, 23);
        this.linkContainer.addView((View)this.headerCell);
        (this.publicContainer = new LinearLayout(context)).setOrientation(0);
        this.linkContainer.addView((View)this.publicContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36, 23.0f, 7.0f, 23.0f, 0.0f));
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
        (this.usernameTextView = new EditTextBoldCursor(context)).setTextSize(1, 18.0f);
        if (!this.isPrivate) {
            this.usernameTextView.setText((CharSequence)this.currentChat.username);
        }
        this.usernameTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.usernameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.usernameTextView.setMaxLines(1);
        this.usernameTextView.setLines(1);
        this.usernameTextView.setBackgroundDrawable((Drawable)null);
        this.usernameTextView.setPadding(0, 0, 0, 0);
        this.usernameTextView.setSingleLine(true);
        this.usernameTextView.setInputType(163872);
        this.usernameTextView.setImeOptions(6);
        this.usernameTextView.setHint((CharSequence)LocaleController.getString("ChannelUsernamePlaceholder", 2131559014));
        this.usernameTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.usernameTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        this.usernameTextView.setCursorWidth(1.5f);
        this.publicContainer.addView((View)this.usernameTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 36));
        this.usernameTextView.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                final ChatEditTypeActivity this$0 = ChatEditTypeActivity.this;
                this$0.checkUserName(this$0.usernameTextView.getText().toString());
            }
        });
        (this.privateContainer = new LinearLayout(context)).setOrientation(1);
        this.linkContainer.addView((View)this.privateContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.privateTextView = new TextBlockCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.privateContainer.addView((View)this.privateTextView);
        this.privateTextView.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditTypeActivity$7JS5wQ6nvw2zxnxic24e587d6Z8(this));
        (this.copyCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.copyCell.setText(LocaleController.getString("CopyLink", 2131559164), true);
        this.privateContainer.addView((View)this.copyCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.copyCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditTypeActivity$TZcp0ymcWTiUlpFxGrOZVdeBPZM(this));
        (this.revokeCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.revokeCell.setText(LocaleController.getString("RevokeLink", 2131560620), true);
        this.privateContainer.addView((View)this.revokeCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.revokeCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditTypeActivity$MZJTil1g1UVkSx0GMskvwQvMJ0k(this));
        (this.shareCell = new TextSettingsCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.shareCell.setText(LocaleController.getString("ShareLink", 2131560749), false);
        this.privateContainer.addView((View)this.shareCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.shareCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditTypeActivity$3aw4OO93rb5NA_LBByFsperljoU(this));
        (this.checkTextView = new TextInfoPrivacyCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.checkTextView.setBottomPadding(6);
        this.linearLayout.addView((View)this.checkTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
        this.typeInfoCell = new TextInfoPrivacyCell(context);
        this.linearLayout.addView((View)this.typeInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.loadingAdminedCell = new LoadingCell(context);
        this.linearLayout.addView((View)this.loadingAdminedCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.adminnedChannelsLayout = new LinearLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.adminnedChannelsLayout.setOrientation(1);
        this.linearLayout.addView((View)this.adminnedChannelsLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.adminedInfoCell = new ShadowSectionCell(context);
        this.linearLayout.addView((View)this.adminedInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.updatePrivatePublic();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.chatInfoDidLoad) {
            final TLRPC.ChatFull info = (TLRPC.ChatFull)array[0];
            if (info.id == this.chatId) {
                this.info = info;
                this.invite = info.exported_invite;
                this.updatePrivatePublic();
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM $$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM = new _$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription(this.sectionCell2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.infoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.infoCell, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.textCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.textCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.textCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.textCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.usernameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.usernameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.linearLayoutTypeContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.linkContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.headerCell, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.headerCell2, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText4"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText8"), new ThemeDescription((View)this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGreenText"), new ThemeDescription((View)this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.privateTextView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.privateTextView, 0, new Class[] { TextBlockCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.loadingAdminedCell, 0, new Class[] { LoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioButtonCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { RadioButtonCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.copyCell, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.copyCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.revokeCell, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.revokeCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.shareCell, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.shareCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteLinkText"), new ThemeDescription((View)this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[] { AdminedChannelCell.class }, new String[] { "deleteButton" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription(null, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM, "avatar_backgroundPink") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
        final TLRPC.Chat currentChat = this.currentChat;
        boolean isChannel = true;
        Label_0142: {
            if (currentChat == null) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE(this, countDownLatch));
                try {
                    countDownLatch.await();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                if (this.currentChat != null) {
                    MessagesController.getInstance(super.currentAccount).putChat(this.currentChat, true);
                    if (this.info != null) {
                        break Label_0142;
                    }
                    MessagesStorage.getInstance(super.currentAccount).loadChatInfo(this.chatId, countDownLatch, false, false);
                    try {
                        countDownLatch.await();
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                    if (this.info != null) {
                        break Label_0142;
                    }
                }
                return false;
            }
        }
        this.isPrivate = TextUtils.isEmpty((CharSequence)this.currentChat.username);
        if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
            isChannel = false;
        }
        this.isChannel = isChannel;
        if (this.isPrivate && this.currentChat.creator) {
            final TLRPC.TL_channels_checkUsername tl_channels_checkUsername = new TLRPC.TL_channels_checkUsername();
            tl_channels_checkUsername.username = "1";
            tl_channels_checkUsername.channel = new TLRPC.TL_inputChannelEmpty();
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_checkUsername, new _$$Lambda$ChatEditTypeActivity$cvbA8EN1qfRN8RDcsasuoHRxqE8(this));
        }
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
        final TextSettingsCell textCell2 = this.textCell2;
        if (textCell2 != null) {
            final TLRPC.ChatFull info = this.info;
            if (info != null) {
                if (info.stickerset != null) {
                    textCell2.setTextAndValue(LocaleController.getString("GroupStickers", 2131559615), this.info.stickerset.title, false);
                }
                else {
                    textCell2.setText(LocaleController.getString("GroupStickers", 2131559615), false);
                }
            }
        }
    }
    
    public void setInfo(final TLRPC.ChatFull info) {
        this.info = info;
        if (info != null) {
            final TLRPC.ExportedChatInvite exported_invite = info.exported_invite;
            if (exported_invite instanceof TLRPC.TL_chatInviteExported) {
                this.invite = exported_invite;
            }
            else {
                this.generateLink(false);
            }
        }
    }
}
