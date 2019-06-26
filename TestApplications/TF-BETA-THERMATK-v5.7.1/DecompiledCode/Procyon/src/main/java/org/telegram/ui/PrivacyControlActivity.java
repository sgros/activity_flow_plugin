// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Shader$TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Canvas;
import android.text.style.CharacterStyle;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessageObject;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Cells.ChatMessageCell;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.text.Spannable;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;
import org.telegram.messenger.FileLog;
import android.os.Bundle;
import android.content.DialogInterface;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.SharedPreferences;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.Components.RecyclerListView;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PrivacyControlActivity extends BaseFragment implements NotificationCenterDelegate
{
    public static final int PRIVACY_RULES_TYPE_CALLS = 2;
    public static final int PRIVACY_RULES_TYPE_FORWARDS = 5;
    public static final int PRIVACY_RULES_TYPE_INVITE = 1;
    public static final int PRIVACY_RULES_TYPE_LASTSEEN = 0;
    public static final int PRIVACY_RULES_TYPE_P2P = 3;
    public static final int PRIVACY_RULES_TYPE_PHONE = 6;
    public static final int PRIVACY_RULES_TYPE_PHOTO = 4;
    private static final int done_button = 1;
    private int alwaysShareRow;
    private ArrayList<Integer> currentMinus;
    private ArrayList<Integer> currentPlus;
    private int currentType;
    private int detailRow;
    private View doneButton;
    private boolean enableAnimation;
    private int everybodyRow;
    private ArrayList<Integer> initialMinus;
    private ArrayList<Integer> initialPlus;
    private int initialRulesType;
    private int lastCheckedType;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private MessageCell messageCell;
    private int messageRow;
    private int myContactsRow;
    private int neverShareRow;
    private int nobodyRow;
    private int p2pDetailRow;
    private int p2pRow;
    private int p2pSectionRow;
    private int rowCount;
    private int rulesType;
    private int sectionRow;
    private int shareDetailRow;
    private int shareSectionRow;
    
    public PrivacyControlActivity(final int n) {
        this(n, false);
    }
    
    public PrivacyControlActivity(final int rulesType, final boolean b) {
        this.initialPlus = new ArrayList<Integer>();
        this.initialMinus = new ArrayList<Integer>();
        this.lastCheckedType = -1;
        this.rulesType = rulesType;
        if (b) {
            ContactsController.getInstance(super.currentAccount).loadPrivacySettings();
        }
    }
    
    private void applyCurrentPrivacySettings() {
        final TLRPC.TL_account_setPrivacy tl_account_setPrivacy = new TLRPC.TL_account_setPrivacy();
        final int rulesType = this.rulesType;
        if (rulesType == 6) {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneNumber();
        }
        else if (rulesType == 5) {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyForwards();
        }
        else if (rulesType == 4) {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyProfilePhoto();
        }
        else if (rulesType == 3) {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneP2P();
        }
        else if (rulesType == 2) {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneCall();
        }
        else if (rulesType == 1) {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyChatInvite();
        }
        else {
            tl_account_setPrivacy.key = new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
        }
        if (this.currentType != 0 && this.currentPlus.size() > 0) {
            final TLRPC.TL_inputPrivacyValueAllowUsers e = new TLRPC.TL_inputPrivacyValueAllowUsers();
            final TLRPC.TL_inputPrivacyValueAllowChatParticipants e2 = new TLRPC.TL_inputPrivacyValueAllowChatParticipants();
            for (int i = 0; i < this.currentPlus.size(); ++i) {
                final int intValue = this.currentPlus.get(i);
                if (intValue > 0) {
                    final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(intValue);
                    if (user != null) {
                        final TLRPC.InputUser inputUser = MessagesController.getInstance(super.currentAccount).getInputUser(user);
                        if (inputUser != null) {
                            e.users.add(inputUser);
                        }
                    }
                }
                else {
                    e2.chats.add(-intValue);
                }
            }
            tl_account_setPrivacy.rules.add(e);
            tl_account_setPrivacy.rules.add(e2);
        }
        if (this.currentType != 1 && this.currentMinus.size() > 0) {
            final TLRPC.TL_inputPrivacyValueDisallowUsers e3 = new TLRPC.TL_inputPrivacyValueDisallowUsers();
            final TLRPC.TL_inputPrivacyValueDisallowChatParticipants e4 = new TLRPC.TL_inputPrivacyValueDisallowChatParticipants();
            for (int j = 0; j < this.currentMinus.size(); ++j) {
                final int intValue2 = this.currentMinus.get(j);
                if (intValue2 > 0) {
                    final TLRPC.User user2 = this.getMessagesController().getUser(intValue2);
                    if (user2 != null) {
                        final TLRPC.InputUser inputUser2 = this.getMessagesController().getInputUser(user2);
                        if (inputUser2 != null) {
                            e3.users.add(inputUser2);
                        }
                    }
                }
                else {
                    e4.chats.add(-intValue2);
                }
            }
            tl_account_setPrivacy.rules.add(e3);
            tl_account_setPrivacy.rules.add(e4);
        }
        final int currentType = this.currentType;
        if (currentType == 0) {
            tl_account_setPrivacy.rules.add(new TLRPC.TL_inputPrivacyValueAllowAll());
        }
        else if (currentType == 1) {
            tl_account_setPrivacy.rules.add(new TLRPC.TL_inputPrivacyValueDisallowAll());
        }
        else if (currentType == 2) {
            tl_account_setPrivacy.rules.add(new TLRPC.TL_inputPrivacyValueAllowContacts());
        }
        AlertDialog alertDialog = null;
        if (this.getParentActivity() != null) {
            alertDialog = new AlertDialog((Context)this.getParentActivity(), 3);
            alertDialog.setCanCacnel(false);
            alertDialog.show();
        }
        ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_account_setPrivacy, new _$$Lambda$PrivacyControlActivity$8u1Pr_pdaGnQbppD7jDw_8yxFb4(this, alertDialog), 2);
    }
    
    private boolean checkDiscard() {
        if (this.doneButton.getVisibility() == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
            builder.setMessage(LocaleController.getString("PrivacySettingsChangedAlert", 2131560510));
            builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), (DialogInterface$OnClickListener)new _$$Lambda$PrivacyControlActivity$5im2Vf3cKm0s4wzjCS8m_xwDFXs(this));
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), (DialogInterface$OnClickListener)new _$$Lambda$PrivacyControlActivity$jMiVlh9zo_Od8OHpA_uuLslJQzk(this));
            this.showDialog(builder.create());
            return false;
        }
        return true;
    }
    
    private void checkPrivacy() {
        this.currentPlus = new ArrayList<Integer>();
        this.currentMinus = new ArrayList<Integer>();
        final ArrayList<TLRPC.PrivacyRule> privacyRules = ContactsController.getInstance(super.currentAccount).getPrivacyRules(this.rulesType);
        if (privacyRules != null && privacyRules.size() != 0) {
            int i = 0;
            int n = -1;
            while (i < privacyRules.size()) {
                final TLRPC.PrivacyRule privacyRule = privacyRules.get(i);
                int n2;
                if (privacyRule instanceof TLRPC.TL_privacyValueAllowChatParticipants) {
                    final TLRPC.TL_privacyValueAllowChatParticipants tl_privacyValueAllowChatParticipants = (TLRPC.TL_privacyValueAllowChatParticipants)privacyRule;
                    final int size = tl_privacyValueAllowChatParticipants.chats.size();
                    int index = 0;
                    while (true) {
                        n2 = n;
                        if (index >= size) {
                            break;
                        }
                        this.currentPlus.add(-tl_privacyValueAllowChatParticipants.chats.get(index));
                        ++index;
                    }
                }
                else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowChatParticipants) {
                    final TLRPC.TL_privacyValueDisallowChatParticipants tl_privacyValueDisallowChatParticipants = (TLRPC.TL_privacyValueDisallowChatParticipants)privacyRule;
                    final int size2 = tl_privacyValueDisallowChatParticipants.chats.size();
                    int index2 = 0;
                    while (true) {
                        n2 = n;
                        if (index2 >= size2) {
                            break;
                        }
                        this.currentMinus.add(-tl_privacyValueDisallowChatParticipants.chats.get(index2));
                        ++index2;
                    }
                }
                else if (privacyRule instanceof TLRPC.TL_privacyValueAllowUsers) {
                    this.currentPlus.addAll(((TLRPC.TL_privacyValueAllowUsers)privacyRule).users);
                    n2 = n;
                }
                else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowUsers) {
                    this.currentMinus.addAll(((TLRPC.TL_privacyValueDisallowUsers)privacyRule).users);
                    n2 = n;
                }
                else if ((n2 = n) == -1) {
                    if (privacyRule instanceof TLRPC.TL_privacyValueAllowAll) {
                        n2 = 0;
                    }
                    else if (privacyRule instanceof TLRPC.TL_privacyValueDisallowAll) {
                        n2 = 1;
                    }
                    else {
                        n2 = 2;
                    }
                }
                ++i;
                n = n2;
            }
            if (n != 0 && (n != -1 || this.currentMinus.size() <= 0)) {
                if (n != 2 && (n != -1 || this.currentMinus.size() <= 0 || this.currentPlus.size() <= 0)) {
                    if (n == 1 || (n == -1 && this.currentPlus.size() > 0)) {
                        this.currentType = 1;
                    }
                }
                else {
                    this.currentType = 2;
                }
            }
            else {
                this.currentType = 0;
            }
            final View doneButton = this.doneButton;
            if (doneButton != null) {
                doneButton.setVisibility(8);
            }
        }
        else {
            this.currentType = 1;
        }
        this.initialPlus.clear();
        this.initialMinus.clear();
        this.initialRulesType = this.currentType;
        this.initialPlus.addAll(this.currentPlus);
        this.initialMinus.addAll(this.currentMinus);
        this.updateRows();
    }
    
    private boolean hasChanges() {
        if (this.initialRulesType != this.currentType) {
            return true;
        }
        if (this.initialMinus.size() != this.currentMinus.size()) {
            return true;
        }
        if (this.initialPlus.size() != this.currentPlus.size()) {
            return true;
        }
        Collections.sort(this.initialPlus);
        Collections.sort(this.currentPlus);
        if (!this.initialPlus.equals(this.currentPlus)) {
            return true;
        }
        Collections.sort(this.initialMinus);
        Collections.sort(this.currentMinus);
        return !this.initialMinus.equals(this.currentMinus);
    }
    
    private void processDone() {
        if (this.getParentActivity() == null) {
            return;
        }
        if (this.currentType != 0 && this.rulesType == 0) {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (!globalMainSettings.getBoolean("privacyAlertShowed", false)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                if (this.rulesType == 1) {
                    builder.setMessage(LocaleController.getString("WhoCanAddMeInfo", 2131561116));
                }
                else {
                    builder.setMessage(LocaleController.getString("CustomHelp", 2131559187));
                }
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw(this, globalMainSettings));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                this.showDialog(builder.create());
                return;
            }
        }
        this.applyCurrentPrivacySettings();
    }
    
    private void setMessageText() {
        final MessageCell messageCell = this.messageCell;
        if (messageCell != null) {
            final int currentType = this.currentType;
            if (currentType == 0) {
                messageCell.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsEverybody", 2131560486));
                this.messageCell.messageObject.messageOwner.fwd_from.from_id = 1;
            }
            else if (currentType == 1) {
                messageCell.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsNobody", 2131560490));
                this.messageCell.messageObject.messageOwner.fwd_from.from_id = 0;
            }
            else {
                messageCell.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsContacts", 2131560485));
                this.messageCell.messageObject.messageOwner.fwd_from.from_id = 1;
            }
            this.messageCell.cell.forceResetMessageObject();
        }
    }
    
    private void showErrorAlert() {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        builder.setMessage(LocaleController.getString("PrivacyFloodControlError", 2131560483));
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        this.showDialog(builder.create());
    }
    
    private void updateRows() {
        this.rowCount = 0;
        if (this.rulesType == 5) {
            this.messageRow = this.rowCount++;
        }
        else {
            this.messageRow = -1;
        }
        this.sectionRow = this.rowCount++;
        this.everybodyRow = this.rowCount++;
        this.myContactsRow = this.rowCount++;
        final int rulesType = this.rulesType;
        if (rulesType != 0 && rulesType != 2 && rulesType != 3 && rulesType != 5 && rulesType != 6) {
            this.nobodyRow = -1;
        }
        else {
            this.nobodyRow = this.rowCount++;
        }
        this.detailRow = this.rowCount++;
        this.shareSectionRow = this.rowCount++;
        final int currentType = this.currentType;
        if (currentType != 1 && currentType != 2) {
            this.alwaysShareRow = -1;
        }
        else {
            this.alwaysShareRow = this.rowCount++;
        }
        final int currentType2 = this.currentType;
        if (currentType2 != 0 && currentType2 != 2) {
            this.neverShareRow = -1;
        }
        else {
            this.neverShareRow = this.rowCount++;
        }
        this.shareDetailRow = this.rowCount++;
        if (this.rulesType == 2) {
            this.p2pSectionRow = this.rowCount++;
            this.p2pRow = this.rowCount++;
            this.p2pDetailRow = this.rowCount++;
        }
        else {
            this.p2pSectionRow = -1;
            this.p2pRow = -1;
            this.p2pDetailRow = -1;
        }
        this.setMessageText();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public boolean canBeginSlide() {
        return this.checkDiscard();
    }
    
    @Override
    public View createView(final Context context) {
        if (this.rulesType == 5) {
            this.messageCell = new MessageCell(context);
        }
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        final int rulesType = this.rulesType;
        if (rulesType == 6) {
            super.actionBar.setTitle(LocaleController.getString("PrivacyPhone", 2131560498));
        }
        else if (rulesType == 5) {
            super.actionBar.setTitle(LocaleController.getString("PrivacyForwards", 2131560484));
        }
        else if (rulesType == 4) {
            super.actionBar.setTitle(LocaleController.getString("PrivacyProfilePhoto", 2131560505));
        }
        else if (rulesType == 3) {
            super.actionBar.setTitle(LocaleController.getString("PrivacyP2P", 2131560493));
        }
        else if (rulesType == 2) {
            super.actionBar.setTitle(LocaleController.getString("Calls", 2131558888));
        }
        else if (rulesType == 1) {
            super.actionBar.setTitle(LocaleController.getString("GroupsAndChannels", 2131559624));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("PrivacyLastSeen", 2131560492));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (PrivacyControlActivity.this.checkDiscard()) {
                        PrivacyControlActivity.this.finishFragment();
                    }
                }
                else if (n == 1) {
                    PrivacyControlActivity.this.processDone();
                }
            }
        });
        final View doneButton = this.doneButton;
        int visibility;
        if (doneButton != null) {
            visibility = doneButton.getVisibility();
        }
        else {
            visibility = 8;
        }
        (this.doneButton = (View)super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f))).setVisibility(visibility);
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PrivacyControlActivity$wxF_vl2Ux3ukEJDYev7VlVBgIRk(this));
        this.setMessageText();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.privacyRulesUpdated) {
            this.checkPrivacy();
        }
        else if (n == NotificationCenter.emojiDidLoad) {
            this.listView.invalidateViews();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, HeaderCell.class, RadioCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { RadioCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable }, null, "chat_inBubble"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable }, null, "chat_inBubbleSelected"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable }, null, "chat_inBubbleShadow"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable }, null, "chat_outBubble"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable }, null, "chat_outBubbleSelected"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable }, null, "chat_outBubbleShadow"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_messageTextIn"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_messageTextOut"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable }, null, "chat_outSentCheck"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable }, null, "chat_outSentCheckSelected"), new ThemeDescription((View)this.listView, 0, null, null, new Drawable[] { Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable }, null, "chat_mediaSentCheck"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyLine"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyLine"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyNameText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyNameText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyMessageText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyMessageText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inReplyMediaMessageSelectedText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outReplyMediaMessageSelectedText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inTimeText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outTimeText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_inTimeSelectedText"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "chat_outTimeSelectedText") };
    }
    
    @Override
    public boolean onBackPressed() {
        return this.checkDiscard();
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.checkPrivacy();
        this.updateRows();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.lastCheckedType = -1;
        this.enableAnimation = false;
    }
    
    private static class LinkMovementMethodMy extends LinkMovementMethod
    {
        public boolean onTouchEvent(final TextView textView, final Spannable spannable, final MotionEvent motionEvent) {
            try {
                return super.onTouchEvent(textView, spannable, motionEvent);
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return false;
            }
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        private int getUsersCount(final ArrayList<Integer> list) {
            int i = 0;
            int n = 0;
            while (i < list.size()) {
                final int intValue = list.get(i);
                int n2;
                if (intValue > 0) {
                    n2 = n + 1;
                }
                else {
                    final TLRPC.Chat chat = BaseFragment.this.getMessagesController().getChat(-intValue);
                    n2 = n;
                    if (chat != null) {
                        n2 = n + chat.participants_count;
                    }
                }
                ++i;
                n = n2;
            }
            return n;
        }
        
        @Override
        public int getItemCount() {
            return PrivacyControlActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == PrivacyControlActivity.this.alwaysShareRow || n == PrivacyControlActivity.this.neverShareRow || n == PrivacyControlActivity.this.p2pRow) {
                return 0;
            }
            if (n == PrivacyControlActivity.this.shareDetailRow || n == PrivacyControlActivity.this.detailRow || n == PrivacyControlActivity.this.p2pDetailRow) {
                return 1;
            }
            if (n == PrivacyControlActivity.this.sectionRow || n == PrivacyControlActivity.this.shareSectionRow || n == PrivacyControlActivity.this.p2pSectionRow) {
                return 2;
            }
            if (n == PrivacyControlActivity.this.everybodyRow || n == PrivacyControlActivity.this.myContactsRow || n == PrivacyControlActivity.this.nobodyRow) {
                return 3;
            }
            if (n == PrivacyControlActivity.this.messageRow) {
                return 4;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PrivacyControlActivity.this.nobodyRow || adapterPosition == PrivacyControlActivity.this.everybodyRow || adapterPosition == PrivacyControlActivity.this.myContactsRow || adapterPosition == PrivacyControlActivity.this.neverShareRow || adapterPosition == PrivacyControlActivity.this.alwaysShareRow || (adapterPosition == PrivacyControlActivity.this.p2pRow && !ContactsController.getInstance(PrivacyControlActivity.this.currentAccount).getLoadingPrivicyInfo(3));
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            boolean b2 = false;
            if (itemViewType != 0) {
                final int n2 = 2;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            final RadioCell radioCell = (RadioCell)viewHolder.itemView;
                            Label_0406: {
                                if (n == PrivacyControlActivity.this.everybodyRow) {
                                    if (PrivacyControlActivity.this.rulesType == 3) {
                                        radioCell.setText(LocaleController.getString("P2PEverybody", 2131560139), PrivacyControlActivity.this.lastCheckedType == 0, true);
                                    }
                                    else {
                                        radioCell.setText(LocaleController.getString("LastSeenEverybody", 2131559736), PrivacyControlActivity.this.lastCheckedType == 0, true);
                                    }
                                }
                                else if (n == PrivacyControlActivity.this.myContactsRow) {
                                    if (PrivacyControlActivity.this.rulesType == 3) {
                                        radioCell.setText(LocaleController.getString("P2PContacts", 2131560134), PrivacyControlActivity.this.lastCheckedType == 2, PrivacyControlActivity.this.nobodyRow != -1);
                                        n = n2;
                                        break Label_0406;
                                    }
                                    radioCell.setText(LocaleController.getString("LastSeenContacts", 2131559730), PrivacyControlActivity.this.lastCheckedType == 2, PrivacyControlActivity.this.nobodyRow != -1);
                                    n = n2;
                                    break Label_0406;
                                }
                                else if (n == PrivacyControlActivity.this.nobodyRow) {
                                    if (PrivacyControlActivity.this.rulesType == 3) {
                                        radioCell.setText(LocaleController.getString("P2PNobody", 2131560141), PrivacyControlActivity.this.lastCheckedType == 1, false);
                                    }
                                    else {
                                        radioCell.setText(LocaleController.getString("LastSeenNobody", 2131559739), PrivacyControlActivity.this.lastCheckedType == 1, false);
                                    }
                                    n = 1;
                                    break Label_0406;
                                }
                                n = 0;
                            }
                            if (PrivacyControlActivity.this.lastCheckedType == n) {
                                radioCell.setChecked(false, PrivacyControlActivity.this.enableAnimation);
                            }
                            else if (PrivacyControlActivity.this.currentType == n) {
                                radioCell.setChecked(true, PrivacyControlActivity.this.enableAnimation);
                            }
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (n == PrivacyControlActivity.this.sectionRow) {
                            if (PrivacyControlActivity.this.rulesType == 6) {
                                headerCell.setText(LocaleController.getString("PrivacyPhoneTitle", 2131560501));
                            }
                            else if (PrivacyControlActivity.this.rulesType == 5) {
                                headerCell.setText(LocaleController.getString("PrivacyForwardsTitle", 2131560491));
                            }
                            else if (PrivacyControlActivity.this.rulesType == 4) {
                                headerCell.setText(LocaleController.getString("PrivacyProfilePhotoTitle", 2131560508));
                            }
                            else if (PrivacyControlActivity.this.rulesType == 3) {
                                headerCell.setText(LocaleController.getString("P2PEnabledWith", 2131560138));
                            }
                            else if (PrivacyControlActivity.this.rulesType == 2) {
                                headerCell.setText(LocaleController.getString("WhoCanCallMe", 2131561120));
                            }
                            else if (PrivacyControlActivity.this.rulesType == 1) {
                                headerCell.setText(LocaleController.getString("WhoCanAddMe", 2131561115));
                            }
                            else {
                                headerCell.setText(LocaleController.getString("LastSeenTitle", 2131559741));
                            }
                        }
                        else if (n == PrivacyControlActivity.this.shareSectionRow) {
                            headerCell.setText(LocaleController.getString("AddExceptions", 2131558570));
                        }
                        else if (n == PrivacyControlActivity.this.p2pSectionRow) {
                            headerCell.setText(LocaleController.getString("PrivacyP2PHeader", 2131560495));
                        }
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n == PrivacyControlActivity.this.detailRow) {
                        if (PrivacyControlActivity.this.rulesType == 6) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyPhoneInfo", 2131560499));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 5) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyForwardsInfo", 2131560487));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 4) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyProfilePhotoInfo", 2131560506));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 3) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyCallsP2PHelp", 2131560478));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 2) {
                            textInfoPrivacyCell.setText(LocaleController.getString("WhoCanCallMeInfo", 2131561121));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 1) {
                            textInfoPrivacyCell.setText(LocaleController.getString("WhoCanAddMeInfo", 2131561116));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("CustomHelp", 2131559187));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (n == PrivacyControlActivity.this.shareDetailRow) {
                        if (PrivacyControlActivity.this.rulesType == 6) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyPhoneInfo2", 2131560500));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 5) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyForwardsInfo2", 2131560488));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 4) {
                            textInfoPrivacyCell.setText(LocaleController.getString("PrivacyProfilePhotoInfo2", 2131560507));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 3) {
                            textInfoPrivacyCell.setText(LocaleController.getString("CustomP2PInfo", 2131559189));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 2) {
                            textInfoPrivacyCell.setText(LocaleController.getString("CustomCallInfo", 2131559186));
                        }
                        else if (PrivacyControlActivity.this.rulesType == 1) {
                            textInfoPrivacyCell.setText(LocaleController.getString("CustomShareInfo", 2131559190));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("CustomShareSettingsHelp", 2131559191));
                        }
                        if (PrivacyControlActivity.this.rulesType == 2) {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        }
                        else {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                    }
                    else if (n == PrivacyControlActivity.this.p2pDetailRow) {
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                }
            }
            else {
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                if (n == PrivacyControlActivity.this.alwaysShareRow) {
                    String s;
                    if (PrivacyControlActivity.this.currentPlus.size() != 0) {
                        s = LocaleController.formatPluralString("Users", this.getUsersCount(PrivacyControlActivity.this.currentPlus));
                    }
                    else {
                        s = LocaleController.getString("EmpryUsersPlaceholder", 2131559345);
                    }
                    if (PrivacyControlActivity.this.rulesType != 0) {
                        final String string = LocaleController.getString("AlwaysAllow", 2131558611);
                        if (PrivacyControlActivity.this.neverShareRow != -1) {
                            b2 = true;
                        }
                        textSettingsCell.setTextAndValue(string, s, b2);
                    }
                    else {
                        final String string2 = LocaleController.getString("AlwaysShareWith", 2131558612);
                        boolean b3 = b;
                        if (PrivacyControlActivity.this.neverShareRow != -1) {
                            b3 = true;
                        }
                        textSettingsCell.setTextAndValue(string2, s, b3);
                    }
                }
                else if (n == PrivacyControlActivity.this.neverShareRow) {
                    String s2;
                    if (PrivacyControlActivity.this.currentMinus.size() != 0) {
                        s2 = LocaleController.formatPluralString("Users", this.getUsersCount(PrivacyControlActivity.this.currentMinus));
                    }
                    else {
                        s2 = LocaleController.getString("EmpryUsersPlaceholder", 2131559345);
                    }
                    if (PrivacyControlActivity.this.rulesType != 0) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("NeverAllow", 2131559894), s2, false);
                    }
                    else {
                        textSettingsCell.setTextAndValue(LocaleController.getString("NeverShareWith", 2131559895), s2, false);
                    }
                }
                else if (n == PrivacyControlActivity.this.p2pRow) {
                    String s3;
                    if (ContactsController.getInstance(PrivacyControlActivity.this.currentAccount).getLoadingPrivicyInfo(3)) {
                        s3 = LocaleController.getString("Loading", 2131559768);
                    }
                    else {
                        s3 = PrivacySettingsActivity.formatRulesString(PrivacyControlActivity.this.currentAccount, 3);
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("PrivacyP2P2", 2131560494), s3, false);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout access$1700;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            access$1700 = PrivacyControlActivity.this.messageCell;
                        }
                        else {
                            access$1700 = new RadioCell(this.mContext);
                            ((View)access$1700).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        access$1700 = new HeaderCell(this.mContext);
                        ((View)access$1700).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    access$1700 = new TextInfoPrivacyCell(this.mContext);
                }
            }
            else {
                access$1700 = new TextSettingsCell(this.mContext);
                ((View)access$1700).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)access$1700);
        }
    }
    
    private class MessageCell extends FrameLayout
    {
        private Drawable backgroundDrawable;
        private ChatMessageCell cell;
        private HintView hintView;
        private MessageObject messageObject;
        private Drawable shadowDrawable;
        
        public MessageCell(final Context context) {
            super(context);
            this.setWillNotDraw(false);
            this.setClipToPadding(false);
            this.shadowDrawable = Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow");
            this.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f));
            final int n = (int)(System.currentTimeMillis() / 1000L);
            final TLRPC.User user = MessagesController.getInstance(PrivacyControlActivity.this.currentAccount).getUser(UserConfig.getInstance(PrivacyControlActivity.this.currentAccount).getClientUserId());
            final TLRPC.TL_message tl_message = new TLRPC.TL_message();
            tl_message.message = LocaleController.getString("PrivacyForwardsMessageLine", 2131560489);
            tl_message.date = n - 3600 + 60;
            tl_message.dialog_id = 1L;
            tl_message.flags = 261;
            tl_message.from_id = 0;
            tl_message.id = 1;
            tl_message.fwd_from = new TLRPC.TL_messageFwdHeader();
            tl_message.fwd_from.from_name = ContactsController.formatName(user.first_name, user.last_name);
            tl_message.media = new TLRPC.TL_messageMediaEmpty();
            tl_message.out = false;
            tl_message.to_id = new TLRPC.TL_peerUser();
            tl_message.to_id.user_id = UserConfig.getInstance(PrivacyControlActivity.this.currentAccount).getClientUserId();
            this.messageObject = new MessageObject(PrivacyControlActivity.this.currentAccount, tl_message, true);
            final MessageObject messageObject = this.messageObject;
            messageObject.eventId = 1L;
            messageObject.resetLayout();
            (this.cell = new ChatMessageCell(context)).setDelegate((ChatMessageCell.ChatMessageCellDelegate)new ChatMessageCell.ChatMessageCellDelegate() {});
            final ChatMessageCell cell = this.cell;
            cell.isChat = false;
            cell.setFullyDraw(true);
            this.cell.setMessageObject(this.messageObject, null, false, false);
            this.addView((View)this.cell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.addView((View)(this.hintView = new HintView(context, 1, true)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        }
        
        protected void dispatchDraw(final Canvas canvas) {
            super.dispatchDraw(canvas);
            this.hintView.showForMessageCell(this.cell, false);
        }
        
        protected void dispatchSetPressed(final boolean b) {
        }
        
        public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
            return false;
        }
        
        public void invalidate() {
            super.invalidate();
            this.cell.invalidate();
        }
        
        protected void onDraw(final Canvas canvas) {
            final Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
            if (cachedWallpaperNonBlocking != null) {
                this.backgroundDrawable = cachedWallpaperNonBlocking;
            }
            final Drawable backgroundDrawable = this.backgroundDrawable;
            if (backgroundDrawable instanceof ColorDrawable) {
                backgroundDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
                this.backgroundDrawable.draw(canvas);
            }
            else if (backgroundDrawable instanceof BitmapDrawable) {
                if (((BitmapDrawable)backgroundDrawable).getTileModeX() == Shader$TileMode.REPEAT) {
                    canvas.save();
                    final float n = 2.0f / AndroidUtilities.density;
                    canvas.scale(n, n);
                    this.backgroundDrawable.setBounds(0, 0, (int)Math.ceil(this.getMeasuredWidth() / n), (int)Math.ceil(this.getMeasuredHeight() / n));
                    this.backgroundDrawable.draw(canvas);
                    canvas.restore();
                }
                else {
                    final int measuredHeight = this.getMeasuredHeight();
                    final float n2 = this.getMeasuredWidth() / (float)this.backgroundDrawable.getIntrinsicWidth();
                    final float n3 = measuredHeight / (float)this.backgroundDrawable.getIntrinsicHeight();
                    float n4 = n2;
                    if (n2 < n3) {
                        n4 = n3;
                    }
                    final int n5 = (int)Math.ceil(this.backgroundDrawable.getIntrinsicWidth() * n4);
                    final int n6 = (int)Math.ceil(this.backgroundDrawable.getIntrinsicHeight() * n4);
                    final int n7 = (this.getMeasuredWidth() - n5) / 2;
                    final int n8 = (measuredHeight - n6) / 2;
                    canvas.save();
                    canvas.clipRect(0, 0, n5, this.getMeasuredHeight());
                    this.backgroundDrawable.setBounds(n7, n8, n5 + n7, n6 + n8);
                    this.backgroundDrawable.draw(canvas);
                    canvas.restore();
                }
            }
            else {
                super.onDraw(canvas);
            }
            this.shadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            this.shadowDrawable.draw(canvas);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            return false;
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return false;
        }
    }
}
