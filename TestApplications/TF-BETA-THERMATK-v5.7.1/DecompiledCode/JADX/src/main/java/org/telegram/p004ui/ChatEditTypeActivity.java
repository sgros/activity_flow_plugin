package org.telegram.p004ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.AdminedChannelCell;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.LoadingCell;
import org.telegram.p004ui.Cells.RadioButtonCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Cells.TextBlockCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ExportedChatInvite;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_channels_checkUsername;
import org.telegram.tgnet.TLRPC.TL_channels_getAdminedPublicChannels;
import org.telegram.tgnet.TLRPC.TL_channels_updateUsername;
import org.telegram.tgnet.TLRPC.TL_chatInviteExported;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_chats;
import org.telegram.tgnet.TLRPC.TL_messages_exportChatInvite;

/* renamed from: org.telegram.ui.ChatEditTypeActivity */
public class ChatEditTypeActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int done_button = 1;
    private ArrayList<AdminedChannelCell> adminedChannelCells = new ArrayList();
    private ShadowSectionCell adminedInfoCell;
    private LinearLayout adminnedChannelsLayout;
    private boolean canCreatePublic = true;
    private int chatId;
    private int checkReqId;
    private Runnable checkRunnable;
    private TextInfoPrivacyCell checkTextView;
    private TextSettingsCell copyCell;
    private Chat currentChat;
    private EditText editText;
    private HeaderCell headerCell;
    private HeaderCell headerCell2;
    private ChatFull info;
    private TextInfoPrivacyCell infoCell;
    private ExportedChatInvite invite;
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

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$3 */
    class C24593 implements TextWatcher {
        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C24593() {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ChatEditTypeActivity chatEditTypeActivity = ChatEditTypeActivity.this;
            chatEditTypeActivity.checkUserName(chatEditTypeActivity.usernameTextView.getText().toString());
        }
    }

    /* renamed from: org.telegram.ui.ChatEditTypeActivity$1 */
    class C39961 extends ActionBarMenuOnItemClick {
        C39961() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ChatEditTypeActivity.this.finishFragment();
            } else if (i == 1) {
                ChatEditTypeActivity.this.processDone();
            }
        }
    }

    public ChatEditTypeActivity(int i) {
        this.chatId = i;
    }

    /* JADX WARNING: Missing block: B:17:0x005f, code skipped:
            if (r5.info == null) goto L_0x0061;
     */
    public boolean onFragmentCreate() {
        /*
        r5 = this;
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r5.chatId;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.getChat(r1);
        r5.currentChat = r0;
        r0 = r5.currentChat;
        r1 = 1;
        r2 = 0;
        if (r0 != 0) goto L_0x0062;
    L_0x0018:
        r0 = new java.util.concurrent.CountDownLatch;
        r0.<init>(r1);
        r3 = r5.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r3 = r3.getStorageQueue();
        r4 = new org.telegram.ui.-$$Lambda$ChatEditTypeActivity$_xE1oN7Z_mocgmUxwzzUt1LjqBE;
        r4.<init>(r5, r0);
        r3.postRunnable(r4);
        r0.await();	 Catch:{ Exception -> 0x0033 }
        goto L_0x0037;
    L_0x0033:
        r3 = move-exception;
        org.telegram.messenger.FileLog.m30e(r3);
    L_0x0037:
        r3 = r5.currentChat;
        if (r3 == 0) goto L_0x0061;
    L_0x003b:
        r3 = r5.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r4 = r5.currentChat;
        r3.putChat(r4, r1);
        r3 = r5.info;
        if (r3 != 0) goto L_0x0062;
    L_0x004a:
        r3 = r5.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r4 = r5.chatId;
        r3.loadChatInfo(r4, r0, r2, r2);
        r0.await();	 Catch:{ Exception -> 0x0059 }
        goto L_0x005d;
    L_0x0059:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x005d:
        r0 = r5.info;
        if (r0 != 0) goto L_0x0062;
    L_0x0061:
        return r2;
    L_0x0062:
        r0 = r5.currentChat;
        r0 = r0.username;
        r0 = android.text.TextUtils.isEmpty(r0);
        r5.isPrivate = r0;
        r0 = r5.currentChat;
        r0 = org.telegram.messenger.ChatObject.isChannel(r0);
        if (r0 == 0) goto L_0x007b;
    L_0x0074:
        r0 = r5.currentChat;
        r0 = r0.megagroup;
        if (r0 != 0) goto L_0x007b;
    L_0x007a:
        goto L_0x007c;
    L_0x007b:
        r1 = 0;
    L_0x007c:
        r5.isChannel = r1;
        r0 = r5.isPrivate;
        if (r0 == 0) goto L_0x00a6;
    L_0x0082:
        r0 = r5.currentChat;
        r0 = r0.creator;
        if (r0 == 0) goto L_0x00a6;
    L_0x0088:
        r0 = new org.telegram.tgnet.TLRPC$TL_channels_checkUsername;
        r0.<init>();
        r1 = "1";
        r0.username = r1;
        r1 = new org.telegram.tgnet.TLRPC$TL_inputChannelEmpty;
        r1.<init>();
        r0.channel = r1;
        r1 = r5.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r2 = new org.telegram.ui.-$$Lambda$ChatEditTypeActivity$cvbA8EN1qfRN8RDcsasuoHRxqE8;
        r2.<init>(r5);
        r1.sendRequest(r0, r2);
    L_0x00a6:
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r0);
        r1 = org.telegram.messenger.NotificationCenter.chatInfoDidLoad;
        r0.addObserver(r5, r1);
        r0 = super.onFragmentCreate();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChatEditTypeActivity.onFragmentCreate():boolean");
    }

    public /* synthetic */ void lambda$onFragmentCreate$0$ChatEditTypeActivity(CountDownLatch countDownLatch) {
        this.currentChat = MessagesStorage.getInstance(this.currentAccount).getChat(this.chatId);
        countDownLatch.countDown();
    }

    public /* synthetic */ void lambda$onFragmentCreate$2$ChatEditTypeActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1418-$$Lambda$ChatEditTypeActivity$YaiiLlvDLGglsjOvYPurrXC0wDs(this, tL_error));
    }

    public /* synthetic */ void lambda$null$1$ChatEditTypeActivity(TL_error tL_error) {
        boolean z = tL_error == null || !tL_error.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH");
        this.canCreatePublic = z;
        if (!this.canCreatePublic) {
            loadAdminedChannels();
        }
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        TextSettingsCell textSettingsCell = this.textCell2;
        if (textSettingsCell != null) {
            ChatFull chatFull = this.info;
            if (chatFull != null) {
                String str = "GroupStickers";
                if (chatFull.stickerset != null) {
                    textSettingsCell.setTextAndValue(LocaleController.getString(str, C1067R.string.GroupStickers), this.info.stickerset.title, false);
                } else {
                    textSettingsCell.setText(LocaleController.getString(str, C1067R.string.GroupStickers), false);
                }
            }
        }
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new C39961());
        this.actionBar.createMenu().addItemWithWidth(1, C1067R.C1065drawable.ic_done, AndroidUtilities.m26dp(56.0f));
        this.fragmentView = new ScrollView(context) {
            public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
                rect.bottom += AndroidUtilities.m26dp(60.0f);
                return super.requestChildRectangleOnScreen(view, rect, z);
            }
        };
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        ScrollView scrollView = (ScrollView) this.fragmentView;
        scrollView.setFillViewport(true);
        this.linearLayout = new LinearLayout(context);
        scrollView.addView(this.linearLayout, new LayoutParams(-1, -2));
        this.linearLayout.setOrientation(1);
        if (this.isChannel) {
            this.actionBar.setTitle(LocaleController.getString("ChannelSettingsTitle", C1067R.string.ChannelSettingsTitle));
        } else {
            this.actionBar.setTitle(LocaleController.getString("GroupSettingsTitle", C1067R.string.GroupSettingsTitle));
        }
        this.linearLayoutTypeContainer = new LinearLayout(context);
        this.linearLayoutTypeContainer.setOrientation(1);
        LinearLayout linearLayout = this.linearLayoutTypeContainer;
        String str = Theme.key_windowBackgroundWhite;
        linearLayout.setBackgroundColor(Theme.getColor(str));
        this.linearLayout.addView(this.linearLayoutTypeContainer, LayoutHelper.createLinear(-1, -2));
        this.headerCell2 = new HeaderCell(context, 23);
        this.headerCell2.setHeight(46);
        if (this.isChannel) {
            this.headerCell2.setText(LocaleController.getString("ChannelTypeHeader", C1067R.string.ChannelTypeHeader));
        } else {
            this.headerCell2.setText(LocaleController.getString("GroupTypeHeader", C1067R.string.GroupTypeHeader));
        }
        this.linearLayoutTypeContainer.addView(this.headerCell2);
        this.radioButtonCell2 = new RadioButtonCell(context);
        this.radioButtonCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (this.isChannel) {
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("ChannelPrivate", C1067R.string.ChannelPrivate), LocaleController.getString("ChannelPrivateInfo", C1067R.string.ChannelPrivateInfo), false, this.isPrivate);
        } else {
            this.radioButtonCell2.setTextAndValue(LocaleController.getString("MegaPrivate", C1067R.string.MegaPrivate), LocaleController.getString("MegaPrivateInfo", C1067R.string.MegaPrivateInfo), false, this.isPrivate);
        }
        this.linearLayoutTypeContainer.addView(this.radioButtonCell2, LayoutHelper.createLinear(-1, -2));
        this.radioButtonCell2.setOnClickListener(new C1421-$$Lambda$ChatEditTypeActivity$ocPS9yaO8oX3caNaqb3brf0F0tM(this));
        this.radioButtonCell1 = new RadioButtonCell(context);
        this.radioButtonCell1.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        if (this.isChannel) {
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("ChannelPublic", C1067R.string.ChannelPublic), LocaleController.getString("ChannelPublicInfo", C1067R.string.ChannelPublicInfo), false, this.isPrivate ^ 1);
        } else {
            this.radioButtonCell1.setTextAndValue(LocaleController.getString("MegaPublic", C1067R.string.MegaPublic), LocaleController.getString("MegaPublicInfo", C1067R.string.MegaPublicInfo), false, this.isPrivate ^ 1);
        }
        this.linearLayoutTypeContainer.addView(this.radioButtonCell1, LayoutHelper.createLinear(-1, -2));
        this.radioButtonCell1.setOnClickListener(new C1410-$$Lambda$ChatEditTypeActivity$IpgElbmOCajv_apzoxCqnnTdt5I(this));
        this.sectionCell2 = new ShadowSectionCell(context);
        this.linearLayout.addView(this.sectionCell2, LayoutHelper.createLinear(-1, -2));
        this.linkContainer = new LinearLayout(context);
        this.linkContainer.setOrientation(1);
        this.linkContainer.setBackgroundColor(Theme.getColor(str));
        this.linearLayout.addView(this.linkContainer, LayoutHelper.createLinear(-1, -2));
        this.headerCell = new HeaderCell(context, 23);
        this.linkContainer.addView(this.headerCell);
        this.publicContainer = new LinearLayout(context);
        this.publicContainer.setOrientation(0);
        this.linkContainer.addView(this.publicContainer, LayoutHelper.createLinear(-1, 36, 23.0f, 7.0f, 23.0f, 0.0f));
        this.editText = new EditText(context);
        EditText editText = this.editText;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
        stringBuilder.append("/");
        editText.setText(stringBuilder.toString());
        this.editText.setTextSize(1, 18.0f);
        editText = this.editText;
        String str2 = Theme.key_windowBackgroundWhiteHintText;
        editText.setHintTextColor(Theme.getColor(str2));
        editText = this.editText;
        String str3 = Theme.key_windowBackgroundWhiteBlackText;
        editText.setTextColor(Theme.getColor(str3));
        this.editText.setMaxLines(1);
        this.editText.setLines(1);
        this.editText.setEnabled(false);
        this.editText.setBackgroundDrawable(null);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setSingleLine(true);
        this.editText.setInputType(163840);
        this.editText.setImeOptions(6);
        this.publicContainer.addView(this.editText, LayoutHelper.createLinear(-2, 36));
        this.usernameTextView = new EditTextBoldCursor(context);
        this.usernameTextView.setTextSize(1, 18.0f);
        if (!this.isPrivate) {
            this.usernameTextView.setText(this.currentChat.username);
        }
        this.usernameTextView.setHintTextColor(Theme.getColor(str2));
        this.usernameTextView.setTextColor(Theme.getColor(str3));
        this.usernameTextView.setMaxLines(1);
        this.usernameTextView.setLines(1);
        this.usernameTextView.setBackgroundDrawable(null);
        this.usernameTextView.setPadding(0, 0, 0, 0);
        this.usernameTextView.setSingleLine(true);
        this.usernameTextView.setInputType(163872);
        this.usernameTextView.setImeOptions(6);
        this.usernameTextView.setHint(LocaleController.getString("ChannelUsernamePlaceholder", C1067R.string.ChannelUsernamePlaceholder));
        this.usernameTextView.setCursorColor(Theme.getColor(str3));
        this.usernameTextView.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.usernameTextView.setCursorWidth(1.5f);
        this.publicContainer.addView(this.usernameTextView, LayoutHelper.createLinear(-1, 36));
        this.usernameTextView.addTextChangedListener(new C24593());
        this.privateContainer = new LinearLayout(context);
        this.privateContainer.setOrientation(1);
        this.linkContainer.addView(this.privateContainer, LayoutHelper.createLinear(-1, -2));
        this.privateTextView = new TextBlockCell(context);
        this.privateTextView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.privateContainer.addView(this.privateTextView);
        this.privateTextView.setOnClickListener(new C1409-$$Lambda$ChatEditTypeActivity$7JS5wQ6nvw2zxnxic24e587d6Z8(this));
        this.copyCell = new TextSettingsCell(context);
        this.copyCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.copyCell.setText(LocaleController.getString("CopyLink", C1067R.string.CopyLink), true);
        this.privateContainer.addView(this.copyCell, LayoutHelper.createLinear(-1, -2));
        this.copyCell.setOnClickListener(new C1417-$$Lambda$ChatEditTypeActivity$TZcp0ymcWTiUlpFxGrOZVdeBPZM(this));
        this.revokeCell = new TextSettingsCell(context);
        this.revokeCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.revokeCell.setText(LocaleController.getString("RevokeLink", C1067R.string.RevokeLink), true);
        this.privateContainer.addView(this.revokeCell, LayoutHelper.createLinear(-1, -2));
        this.revokeCell.setOnClickListener(new C1412-$$Lambda$ChatEditTypeActivity$MZJTil1g1UVkSx0GMskvwQvMJ0k(this));
        this.shareCell = new TextSettingsCell(context);
        this.shareCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.shareCell.setText(LocaleController.getString("ShareLink", C1067R.string.ShareLink), false);
        this.privateContainer.addView(this.shareCell, LayoutHelper.createLinear(-1, -2));
        this.shareCell.setOnClickListener(new C1408-$$Lambda$ChatEditTypeActivity$3aw4OO93rb5NA_LBByFsperljoU(this));
        this.checkTextView = new TextInfoPrivacyCell(context);
        this.checkTextView.setBackgroundDrawable(Theme.getThemedDrawable(context, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.checkTextView.setBottomPadding(6);
        this.linearLayout.addView(this.checkTextView, LayoutHelper.createLinear(-2, -2));
        this.typeInfoCell = new TextInfoPrivacyCell(context);
        this.linearLayout.addView(this.typeInfoCell, LayoutHelper.createLinear(-1, -2));
        this.loadingAdminedCell = new LoadingCell(context);
        this.linearLayout.addView(this.loadingAdminedCell, LayoutHelper.createLinear(-1, -2));
        this.adminnedChannelsLayout = new LinearLayout(context);
        this.adminnedChannelsLayout.setBackgroundColor(Theme.getColor(str));
        this.adminnedChannelsLayout.setOrientation(1);
        this.linearLayout.addView(this.adminnedChannelsLayout, LayoutHelper.createLinear(-1, -2));
        this.adminedInfoCell = new ShadowSectionCell(context);
        this.linearLayout.addView(this.adminedInfoCell, LayoutHelper.createLinear(-1, -2));
        updatePrivatePublic();
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$3$ChatEditTypeActivity(View view) {
        if (!this.isPrivate) {
            this.isPrivate = true;
            updatePrivatePublic();
        }
    }

    public /* synthetic */ void lambda$createView$4$ChatEditTypeActivity(View view) {
        if (this.isPrivate) {
            this.isPrivate = false;
            updatePrivatePublic();
        }
    }

    public /* synthetic */ void lambda$createView$5$ChatEditTypeActivity(View view) {
        if (this.invite != null) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
                Toast.makeText(getParentActivity(), LocaleController.getString("LinkCopied", C1067R.string.LinkCopied), 0).show();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public /* synthetic */ void lambda$createView$6$ChatEditTypeActivity(View view) {
        if (this.invite != null) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.invite.link));
                Toast.makeText(getParentActivity(), LocaleController.getString("LinkCopied", C1067R.string.LinkCopied), 0).show();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public /* synthetic */ void lambda$createView$8$ChatEditTypeActivity(View view) {
        Builder builder = new Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("RevokeAlert", C1067R.string.RevokeAlert));
        builder.setTitle(LocaleController.getString("RevokeLink", C1067R.string.RevokeLink));
        builder.setPositiveButton(LocaleController.getString("RevokeButton", C1067R.string.RevokeButton), new C1413-$$Lambda$ChatEditTypeActivity$QEz_zw-4wfdKKBHW3Kn6oNi5pu8(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$null$7$ChatEditTypeActivity(DialogInterface dialogInterface, int i) {
        generateLink(true);
    }

    public /* synthetic */ void lambda$createView$9$ChatEditTypeActivity(View view) {
        if (this.invite != null) {
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", this.invite.link);
                getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString("InviteToGroupByLink", C1067R.string.InviteToGroupByLink)), 500);
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatInfoDidLoad) {
            ChatFull chatFull = (ChatFull) objArr[0];
            if (chatFull.f435id == this.chatId) {
                this.info = chatFull;
                this.invite = chatFull.exported_invite;
                updatePrivatePublic();
            }
        }
    }

    public void setInfo(ChatFull chatFull) {
        this.info = chatFull;
        if (chatFull != null) {
            ExportedChatInvite exportedChatInvite = chatFull.exported_invite;
            if (exportedChatInvite instanceof TL_chatInviteExported) {
                this.invite = exportedChatInvite;
            } else {
                generateLink(false);
            }
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0026, code skipped:
            if (r0.equalsIgnoreCase(r4.usernameTextView.getText().toString()) == false) goto L_0x0028;
     */
    private void processDone() {
        /*
        r4 = this;
        r0 = r4.isPrivate;
        if (r0 != 0) goto L_0x0050;
    L_0x0004:
        r0 = r4.currentChat;
        r0 = r0.username;
        if (r0 != 0) goto L_0x0012;
    L_0x000a:
        r0 = r4.usernameTextView;
        r0 = r0.length();
        if (r0 != 0) goto L_0x0028;
    L_0x0012:
        r0 = r4.currentChat;
        r0 = r0.username;
        if (r0 == 0) goto L_0x0050;
    L_0x0018:
        r1 = r4.usernameTextView;
        r1 = r1.getText();
        r1 = r1.toString();
        r0 = r0.equalsIgnoreCase(r1);
        if (r0 != 0) goto L_0x0050;
    L_0x0028:
        r0 = r4.usernameTextView;
        r0 = r0.length();
        if (r0 == 0) goto L_0x0050;
    L_0x0030:
        r0 = r4.lastNameAvailable;
        if (r0 != 0) goto L_0x0050;
    L_0x0034:
        r0 = r4.getParentActivity();
        r1 = "vibrator";
        r0 = r0.getSystemService(r1);
        r0 = (android.os.Vibrator) r0;
        if (r0 == 0) goto L_0x0047;
    L_0x0042:
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0.vibrate(r1);
    L_0x0047:
        r0 = r4.checkTextView;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r2 = 0;
        org.telegram.messenger.AndroidUtilities.shakeView(r0, r1, r2);
        return;
    L_0x0050:
        r0 = r4.currentChat;
        r0 = r0.username;
        r1 = "";
        if (r0 == 0) goto L_0x0059;
    L_0x0058:
        goto L_0x005a;
    L_0x0059:
        r0 = r1;
    L_0x005a:
        r2 = r4.isPrivate;
        if (r2 == 0) goto L_0x005f;
    L_0x005e:
        goto L_0x0069;
    L_0x005f:
        r1 = r4.usernameTextView;
        r1 = r1.getText();
        r1 = r1.toString();
    L_0x0069:
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x009b;
    L_0x006f:
        r0 = r4.currentChat;
        r0 = org.telegram.messenger.ChatObject.isChannel(r0);
        if (r0 != 0) goto L_0x008c;
    L_0x0077:
        r0 = r4.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r4.getParentActivity();
        r2 = r4.chatId;
        r3 = new org.telegram.ui.-$$Lambda$ChatEditTypeActivity$wzFYEdqnqyRZtP7rz7ySevzOMoo;
        r3.<init>(r4);
        r0.convertToMegaGroup(r1, r2, r3);
        return;
    L_0x008c:
        r0 = r4.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r2 = r4.chatId;
        r0.updateChannelUserName(r2, r1);
        r0 = r4.currentChat;
        r0.username = r1;
    L_0x009b:
        r4.finishFragment();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChatEditTypeActivity.processDone():void");
    }

    public /* synthetic */ void lambda$processDone$10$ChatEditTypeActivity(int i) {
        this.chatId = i;
        this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(i));
        processDone();
    }

    private void loadAdminedChannels() {
        if (!this.loadingAdminedChannels && this.adminnedChannelsLayout != null) {
            this.loadingAdminedChannels = true;
            updatePrivatePublic();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_channels_getAdminedPublicChannels(), new C3643-$$Lambda$ChatEditTypeActivity$kDy8MSXJZPphT1ROvE66SmVIgsc(this));
        }
    }

    public /* synthetic */ void lambda$loadAdminedChannels$16$ChatEditTypeActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1415-$$Lambda$ChatEditTypeActivity$QRTtp_Z_W9ScH4dQ0r3b172yIRQ(this, tLObject));
    }

    public /* synthetic */ void lambda$null$15$ChatEditTypeActivity(TLObject tLObject) {
        this.loadingAdminedChannels = false;
        if (tLObject != null && getParentActivity() != null) {
            int i;
            for (i = 0; i < this.adminedChannelCells.size(); i++) {
                this.linearLayout.removeView((View) this.adminedChannelCells.get(i));
            }
            this.adminedChannelCells.clear();
            TL_messages_chats tL_messages_chats = (TL_messages_chats) tLObject;
            for (i = 0; i < tL_messages_chats.chats.size(); i++) {
                AdminedChannelCell adminedChannelCell = new AdminedChannelCell(getParentActivity(), new C1407-$$Lambda$ChatEditTypeActivity$20mFPaWfDYXXR9Gd87amavGwrLI(this));
                Chat chat = (Chat) tL_messages_chats.chats.get(i);
                boolean z = true;
                if (i != tL_messages_chats.chats.size() - 1) {
                    z = false;
                }
                adminedChannelCell.setChannel(chat, z);
                this.adminedChannelCells.add(adminedChannelCell);
                this.adminnedChannelsLayout.addView(adminedChannelCell, LayoutHelper.createLinear(-1, 72));
            }
            updatePrivatePublic();
        }
    }

    public /* synthetic */ void lambda$null$14$ChatEditTypeActivity(View view) {
        Chat currentChannel = ((AdminedChannelCell) view.getParent()).getCurrentChannel();
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
        String str = "/";
        Object[] objArr;
        StringBuilder stringBuilder;
        if (this.isChannel) {
            objArr = new Object[2];
            stringBuilder = new StringBuilder();
            stringBuilder.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
            stringBuilder.append(str);
            stringBuilder.append(currentChannel.username);
            objArr[0] = stringBuilder.toString();
            objArr[1] = currentChannel.title;
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlertChannel", C1067R.string.RevokeLinkAlertChannel, objArr)));
        } else {
            objArr = new Object[2];
            stringBuilder = new StringBuilder();
            stringBuilder.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
            stringBuilder.append(str);
            stringBuilder.append(currentChannel.username);
            objArr[0] = stringBuilder.toString();
            objArr[1] = currentChannel.title;
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("RevokeLinkAlert", C1067R.string.RevokeLinkAlert, objArr)));
        }
        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
        builder.setPositiveButton(LocaleController.getString("RevokeButton", C1067R.string.RevokeButton), new C1422-$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg(this, currentChannel));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$null$13$ChatEditTypeActivity(Chat chat, DialogInterface dialogInterface, int i) {
        TL_channels_updateUsername tL_channels_updateUsername = new TL_channels_updateUsername();
        tL_channels_updateUsername.channel = MessagesController.getInputChannel(chat);
        tL_channels_updateUsername.username = "";
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_updateUsername, new C3639-$$Lambda$ChatEditTypeActivity$16BX7QJDGBSQnfTPIEFvTHPHhfQ(this), 64);
    }

    public /* synthetic */ void lambda$null$12$ChatEditTypeActivity(TLObject tLObject, TL_error tL_error) {
        if (tLObject instanceof TL_boolTrue) {
            AndroidUtilities.runOnUIThread(new C1416-$$Lambda$ChatEditTypeActivity$RD9_5i-WVZsRLoq50iXWE1Pepqg(this));
        }
    }

    public /* synthetic */ void lambda$null$11$ChatEditTypeActivity() {
        this.canCreatePublic = true;
        if (this.usernameTextView.length() > 0) {
            checkUserName(this.usernameTextView.getText().toString());
        }
        updatePrivatePublic();
    }

    private void updatePrivatePublic() {
        if (this.sectionCell2 != null) {
            boolean z = this.isPrivate;
            Drawable drawable = null;
            String str = Theme.key_windowBackgroundGrayShadow;
            int i = 8;
            TextInfoPrivacyCell textInfoPrivacyCell;
            String str2;
            if (z || this.canCreatePublic) {
                textInfoPrivacyCell = this.typeInfoCell;
                str2 = Theme.key_windowBackgroundWhiteGrayText4;
                textInfoPrivacyCell.setTag(str2);
                this.typeInfoCell.setTextColor(Theme.getColor(str2));
                this.sectionCell2.setVisibility(0);
                this.adminedInfoCell.setVisibility(8);
                textInfoPrivacyCell = this.typeInfoCell;
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell.getContext(), (int) C1067R.C1065drawable.greydivider_bottom, str));
                this.adminnedChannelsLayout.setVisibility(8);
                this.linkContainer.setVisibility(0);
                this.loadingAdminedCell.setVisibility(8);
                String str3 = "ChannelInviteLinkTitle";
                String str4 = "ChannelLinkTitle";
                int i2;
                String str5;
                if (this.isChannel) {
                    textInfoPrivacyCell = this.typeInfoCell;
                    if (this.isPrivate) {
                        i2 = C1067R.string.ChannelPrivateLinkHelp;
                        str5 = "ChannelPrivateLinkHelp";
                    } else {
                        i2 = C1067R.string.ChannelUsernameHelp;
                        str5 = "ChannelUsernameHelp";
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString(str5, i2));
                    this.headerCell.setText(this.isPrivate ? LocaleController.getString(str3, C1067R.string.ChannelInviteLinkTitle) : LocaleController.getString(str4, C1067R.string.ChannelLinkTitle));
                } else {
                    textInfoPrivacyCell = this.typeInfoCell;
                    if (this.isPrivate) {
                        i2 = C1067R.string.MegaPrivateLinkHelp;
                        str5 = "MegaPrivateLinkHelp";
                    } else {
                        i2 = C1067R.string.MegaUsernameHelp;
                        str5 = "MegaUsernameHelp";
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString(str5, i2));
                    this.headerCell.setText(this.isPrivate ? LocaleController.getString(str3, C1067R.string.ChannelInviteLinkTitle) : LocaleController.getString(str4, C1067R.string.ChannelLinkTitle));
                }
                this.publicContainer.setVisibility(this.isPrivate ? 8 : 0);
                this.privateContainer.setVisibility(this.isPrivate ? 0 : 8);
                this.linkContainer.setPadding(0, 0, 0, this.isPrivate ? 0 : AndroidUtilities.m26dp(7.0f));
                TextBlockCell textBlockCell = this.privateTextView;
                ExportedChatInvite exportedChatInvite = this.invite;
                textBlockCell.setText(exportedChatInvite != null ? exportedChatInvite.link : LocaleController.getString("Loading", C1067R.string.Loading), true);
                textInfoPrivacyCell = this.checkTextView;
                if (!(this.isPrivate || textInfoPrivacyCell.length() == 0)) {
                    i = 0;
                }
                textInfoPrivacyCell.setVisibility(i);
                textInfoPrivacyCell = this.typeInfoCell;
                if (this.checkTextView.getVisibility() != 0) {
                    drawable = Theme.getThemedDrawable(this.typeInfoCell.getContext(), (int) C1067R.C1065drawable.greydivider_bottom, str);
                }
                textInfoPrivacyCell.setBackgroundDrawable(drawable);
            } else {
                this.typeInfoCell.setText(LocaleController.getString("ChangePublicLimitReached", C1067R.string.ChangePublicLimitReached));
                textInfoPrivacyCell = this.typeInfoCell;
                str2 = Theme.key_windowBackgroundWhiteRedText4;
                textInfoPrivacyCell.setTag(str2);
                this.typeInfoCell.setTextColor(Theme.getColor(str2));
                this.linkContainer.setVisibility(8);
                this.sectionCell2.setVisibility(8);
                this.adminedInfoCell.setVisibility(0);
                if (this.loadingAdminedChannels) {
                    this.loadingAdminedCell.setVisibility(0);
                    this.adminnedChannelsLayout.setVisibility(8);
                    this.typeInfoCell.setBackgroundDrawable(this.checkTextView.getVisibility() == 0 ? null : Theme.getThemedDrawable(this.typeInfoCell.getContext(), (int) C1067R.C1065drawable.greydivider_bottom, str));
                    this.adminedInfoCell.setBackgroundDrawable(null);
                } else {
                    ShadowSectionCell shadowSectionCell = this.adminedInfoCell;
                    shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(shadowSectionCell.getContext(), (int) C1067R.C1065drawable.greydivider_bottom, str));
                    textInfoPrivacyCell = this.typeInfoCell;
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(textInfoPrivacyCell.getContext(), (int) C1067R.C1065drawable.greydivider_top, str));
                    this.loadingAdminedCell.setVisibility(8);
                    this.adminnedChannelsLayout.setVisibility(0);
                }
            }
            this.radioButtonCell1.setChecked(this.isPrivate ^ 1, true);
            this.radioButtonCell2.setChecked(this.isPrivate, true);
            this.usernameTextView.clearFocus();
        }
    }

    private boolean checkUserName(String str) {
        if (str == null || str.length() <= 0) {
            this.checkTextView.setVisibility(8);
        } else {
            this.checkTextView.setVisibility(0);
        }
        this.typeInfoCell.setBackgroundDrawable(this.checkTextView.getVisibility() == 0 ? null : Theme.getThemedDrawable(this.typeInfoCell.getContext(), (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        Runnable runnable = this.checkRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.checkRunnable = null;
            this.lastCheckName = null;
            if (this.checkReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkReqId, true);
            }
        }
        this.lastNameAvailable = false;
        String str2 = Theme.key_windowBackgroundWhiteRedText4;
        if (str != null) {
            String str3 = "_";
            String str4 = "LinkInvalid";
            if (str.startsWith(str3) || str.endsWith(str3)) {
                this.checkTextView.setText(LocaleController.getString(str4, C1067R.string.LinkInvalid));
                this.checkTextView.setTextColor(str2);
                return false;
            }
            int i = 0;
            while (i < str.length()) {
                char charAt = str.charAt(i);
                if (i == 0 && charAt >= '0' && charAt <= '9') {
                    if (this.isChannel) {
                        this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumber", C1067R.string.LinkInvalidStartNumber));
                    } else {
                        this.checkTextView.setText(LocaleController.getString("LinkInvalidStartNumberMega", C1067R.string.LinkInvalidStartNumberMega));
                    }
                    this.checkTextView.setTextColor(str2);
                    return false;
                } else if ((charAt < '0' || charAt > '9') && ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && charAt != '_'))) {
                    this.checkTextView.setText(LocaleController.getString(str4, C1067R.string.LinkInvalid));
                    this.checkTextView.setTextColor(str2);
                    return false;
                } else {
                    i++;
                }
            }
        }
        if (str == null || str.length() < 5) {
            if (this.isChannel) {
                this.checkTextView.setText(LocaleController.getString("LinkInvalidShort", C1067R.string.LinkInvalidShort));
            } else {
                this.checkTextView.setText(LocaleController.getString("LinkInvalidShortMega", C1067R.string.LinkInvalidShortMega));
            }
            this.checkTextView.setTextColor(str2);
            return false;
        } else if (str.length() > 32) {
            this.checkTextView.setText(LocaleController.getString("LinkInvalidLong", C1067R.string.LinkInvalidLong));
            this.checkTextView.setTextColor(str2);
            return false;
        } else {
            this.checkTextView.setText(LocaleController.getString("LinkChecking", C1067R.string.LinkChecking));
            this.checkTextView.setTextColor(Theme.key_windowBackgroundWhiteGrayText8);
            this.lastCheckName = str;
            this.checkRunnable = new C1420-$$Lambda$ChatEditTypeActivity$kFJ66e_ln7abTbKYBYoWFyd6BAI(this, str);
            AndroidUtilities.runOnUIThread(this.checkRunnable, 300);
            return true;
        }
    }

    public /* synthetic */ void lambda$checkUserName$19$ChatEditTypeActivity(String str) {
        TL_channels_checkUsername tL_channels_checkUsername = new TL_channels_checkUsername();
        tL_channels_checkUsername.username = str;
        tL_channels_checkUsername.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(this.chatId);
        this.checkReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_checkUsername, new C3644-$$Lambda$ChatEditTypeActivity$tnmJrzBKMOfOJQl31MjjEIQidqw(this, str), 2);
    }

    public /* synthetic */ void lambda$null$18$ChatEditTypeActivity(String str, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1411-$$Lambda$ChatEditTypeActivity$IqTkR4fY_wDnaSet6exFaZwpQVw(this, str, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$17$ChatEditTypeActivity(String str, TL_error tL_error, TLObject tLObject) {
        this.checkReqId = 0;
        String str2 = this.lastCheckName;
        if (str2 != null && str2.equals(str)) {
            if (tL_error == null && (tLObject instanceof TL_boolTrue)) {
                this.checkTextView.setText(LocaleController.formatString("LinkAvailable", C1067R.string.LinkAvailable, str));
                this.checkTextView.setTextColor(Theme.key_windowBackgroundWhiteGreenText);
                this.lastNameAvailable = true;
                return;
            }
            if (tL_error == null || !tL_error.text.equals("CHANNELS_ADMIN_PUBLIC_TOO_MUCH")) {
                this.checkTextView.setText(LocaleController.getString("LinkInUse", C1067R.string.LinkInUse));
            } else {
                this.canCreatePublic = false;
                loadAdminedChannels();
            }
            this.checkTextView.setTextColor(Theme.key_windowBackgroundWhiteRedText4);
            this.lastNameAvailable = false;
        }
    }

    private void generateLink(boolean z) {
        this.loadingInvite = true;
        TL_messages_exportChatInvite tL_messages_exportChatInvite = new TL_messages_exportChatInvite();
        tL_messages_exportChatInvite.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(-this.chatId);
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_exportChatInvite, new C3640-$$Lambda$ChatEditTypeActivity$NQoUsXIxZfHWCE1utCEcp_nY2As(this, z)), this.classGuid);
    }

    public /* synthetic */ void lambda$generateLink$21$ChatEditTypeActivity(boolean z, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1414-$$Lambda$ChatEditTypeActivity$QJca9ThplCpxIMzhhJYYsX0k5YM(this, tL_error, tLObject, z));
    }

    public /* synthetic */ void lambda$null$20$ChatEditTypeActivity(TL_error tL_error, TLObject tLObject, boolean z) {
        if (tL_error == null) {
            this.invite = (ExportedChatInvite) tLObject;
            ChatFull chatFull = this.info;
            if (chatFull != null) {
                chatFull.exported_invite = this.invite;
            }
            if (z) {
                if (getParentActivity() != null) {
                    Builder builder = new Builder(getParentActivity());
                    builder.setMessage(LocaleController.getString("RevokeAlertNewLink", C1067R.string.RevokeAlertNewLink));
                    builder.setTitle(LocaleController.getString("RevokeLink", C1067R.string.RevokeLink));
                    builder.setNegativeButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
                    showDialog(builder.create());
                } else {
                    return;
                }
            }
        }
        this.loadingInvite = false;
        TextBlockCell textBlockCell = this.privateTextView;
        if (textBlockCell != null) {
            ExportedChatInvite exportedChatInvite = this.invite;
            textBlockCell.setText(exportedChatInvite != null ? exportedChatInvite.link : LocaleController.getString("Loading", C1067R.string.Loading), true);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        C3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM = new C3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM(this);
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[59];
        themeDescriptionArr[0] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[1] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[5] = new ThemeDescription(this.sectionCell2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[6] = new ThemeDescription(this.infoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        View view = this.infoCell;
        Class[] clsArr = new Class[]{TextInfoPrivacyCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        themeDescriptionArr[7] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[8] = new ThemeDescription(this.textCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[9] = new ThemeDescription(this.textCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText5);
        themeDescriptionArr[10] = new ThemeDescription(this.textCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[11] = new ThemeDescription(this.textCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[12] = new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[13] = new ThemeDescription(this.usernameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        themeDescriptionArr[14] = new ThemeDescription(this.linearLayoutTypeContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[15] = new ThemeDescription(this.linkContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[16] = new ThemeDescription(this.headerCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[17] = new ThemeDescription(this.headerCell2, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[18] = new ThemeDescription(this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[19] = new ThemeDescription(this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        themeDescriptionArr[20] = new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText4);
        themeDescriptionArr[21] = new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText8);
        themeDescriptionArr[22] = new ThemeDescription(this.checkTextView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGreenText);
        themeDescriptionArr[23] = new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[24] = new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[25] = new ThemeDescription(this.typeInfoCell, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText4);
        themeDescriptionArr[26] = new ThemeDescription(this.adminedInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[27] = new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[28] = new ThemeDescription(this.privateTextView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[29] = new ThemeDescription(this.privateTextView, 0, new Class[]{TextBlockCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[30] = new ThemeDescription(this.loadingAdminedCell, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, null, null, null, Theme.key_progressCircle);
        themeDescriptionArr[31] = new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        view = this.radioButtonCell1;
        int i = ThemeDescription.FLAG_CHECKBOX;
        clsArr = new Class[]{RadioButtonCell.class};
        strArr = new String[1];
        strArr[0] = "radioButton";
        themeDescriptionArr[32] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_radioBackground);
        themeDescriptionArr[33] = new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackgroundChecked);
        themeDescriptionArr[34] = new ThemeDescription(this.radioButtonCell1, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.radioButtonCell1;
        i = ThemeDescription.FLAG_TEXTCOLOR;
        clsArr = new Class[]{RadioButtonCell.class};
        strArr = new String[1];
        strArr[0] = "valueTextView";
        themeDescriptionArr[35] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[36] = new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[37] = new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackground);
        themeDescriptionArr[38] = new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioButtonCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackgroundChecked);
        themeDescriptionArr[39] = new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[40] = new ThemeDescription(this.radioButtonCell2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{RadioButtonCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[41] = new ThemeDescription(this.copyCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[42] = new ThemeDescription(this.copyCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[43] = new ThemeDescription(this.revokeCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[44] = new ThemeDescription(this.revokeCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[45] = new ThemeDescription(this.shareCell, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[46] = new ThemeDescription(this.shareCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[47] = new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.adminnedChannelsLayout;
        i = ThemeDescription.FLAG_TEXTCOLOR;
        clsArr = new Class[]{AdminedChannelCell.class};
        strArr = new String[1];
        strArr[0] = "statusTextView";
        themeDescriptionArr[48] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayText);
        themeDescriptionArr[49] = new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_LINKCOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"statusTextView"}, null, null, null, Theme.key_windowBackgroundWhiteLinkText);
        themeDescriptionArr[50] = new ThemeDescription(this.adminnedChannelsLayout, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{AdminedChannelCell.class}, new String[]{"deleteButton"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText);
        C3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2 = c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM;
        themeDescriptionArr[51] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_text);
        themeDescriptionArr[52] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundRed);
        themeDescriptionArr[53] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundOrange);
        themeDescriptionArr[54] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundViolet);
        themeDescriptionArr[55] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundGreen);
        themeDescriptionArr[56] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundCyan);
        themeDescriptionArr[57] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundBlue);
        themeDescriptionArr[58] = new ThemeDescription(null, 0, null, null, null, c3642-$$Lambda$ChatEditTypeActivity$dkZMTKrtyLpToUIMwbJnMjxr8tM2, Theme.key_avatar_backgroundPink);
        return themeDescriptionArr;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$22$ChatEditTypeActivity() {
        LinearLayout linearLayout = this.adminnedChannelsLayout;
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.adminnedChannelsLayout.getChildAt(i);
                if (childAt instanceof AdminedChannelCell) {
                    ((AdminedChannelCell) childAt).update();
                }
            }
        }
    }
}