// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserObject;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.BotSwitchCell;
import android.widget.TextView;
import android.content.SharedPreferences$Editor;
import android.content.DialogInterface;
import org.telegram.ui.Cells.ContextLinkCell;
import java.util.Collection;
import androidx.recyclerview.widget.RecyclerView;
import java.io.Serializable;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.ConnectionsManager;
import android.text.TextUtils;
import org.telegram.messenger.AndroidUtilities;
import android.content.SharedPreferences;
import android.content.DialogInterface$OnDismissListener;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.ChatObject;
import android.os.Build$VERSION;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper.LocationProvider;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.DataQuery;
import org.telegram.ui.ChatActivity;
import org.telegram.messenger.MessageObject;
import java.util.ArrayList;
import android.content.Context;
import org.telegram.messenger.SendMessagesHelper;
import android.location.Location;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import org.telegram.ui.Components.RecyclerListView;

public class MentionsAdapter extends SelectionAdapter
{
    private static final String punctuationsChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n";
    private SparseArray<TLRPC.BotInfo> botInfo;
    private int botsCount;
    private Runnable cancelDelayRunnable;
    private int channelLastReqId;
    private int channelReqId;
    private boolean contextMedia;
    private int contextQueryReqid;
    private Runnable contextQueryRunnable;
    private int contextUsernameReqid;
    private int currentAccount;
    private MentionsAdapterDelegate delegate;
    private long dialog_id;
    private TLRPC.User foundContextBot;
    private TLRPC.ChatFull info;
    private boolean inlineMediaEnabled;
    private boolean isDarkTheme;
    private boolean isSearchingMentions;
    private Location lastKnownLocation;
    private int lastPosition;
    private String[] lastSearchKeyboardLanguage;
    private String lastText;
    private boolean lastUsernameOnly;
    private LocationProvider locationProvider;
    private Context mContext;
    private ArrayList<MessageObject> messages;
    private boolean needBotContext;
    private boolean needUsernames;
    private String nextQueryOffset;
    private boolean noUserName;
    private ChatActivity parentFragment;
    private int resultLength;
    private int resultStartPosition;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchGlobalRunnable;
    private ArrayList<TLRPC.BotInlineResult> searchResultBotContext;
    private TLRPC.TL_inlineBotSwitchPM searchResultBotContextSwitch;
    private ArrayList<String> searchResultCommands;
    private ArrayList<String> searchResultCommandsHelp;
    private ArrayList<TLRPC.User> searchResultCommandsUsers;
    private ArrayList<String> searchResultHashtags;
    private ArrayList<DataQuery.KeywordResult> searchResultSuggestions;
    private ArrayList<TLRPC.User> searchResultUsernames;
    private SparseArray<TLRPC.User> searchResultUsernamesMap;
    private String searchingContextQuery;
    private String searchingContextUsername;
    
    public MentionsAdapter(final Context mContext, final boolean isDarkTheme, final long dialog_id, final MentionsAdapterDelegate delegate) {
        this.currentAccount = UserConfig.selectedAccount;
        this.needUsernames = true;
        this.needBotContext = true;
        this.inlineMediaEnabled = true;
        this.locationProvider = new LocationProvider((LocationProvider.LocationProviderDelegate)new LocationProvider.LocationProviderDelegate() {
            @Override
            public void onLocationAcquired(final Location location) {
                if (MentionsAdapter.this.foundContextBot != null && MentionsAdapter.this.foundContextBot.bot_inline_geo) {
                    MentionsAdapter.this.lastKnownLocation = location;
                    final MentionsAdapter this$0 = MentionsAdapter.this;
                    this$0.searchForContextBotResults(true, this$0.foundContextBot, MentionsAdapter.this.searchingContextQuery, "");
                }
            }
            
            @Override
            public void onUnableLocationAcquire() {
                MentionsAdapter.this.onLocationUnavailable();
            }
        }) {
            @Override
            public void stop() {
                super.stop();
                MentionsAdapter.this.lastKnownLocation = null;
            }
        };
        this.mContext = mContext;
        this.delegate = delegate;
        this.isDarkTheme = isDarkTheme;
        this.dialog_id = dialog_id;
        (this.searchAdapterHelper = new SearchAdapterHelper(true)).setDelegate((SearchAdapterHelper.SearchAdapterHelperDelegate)new SearchAdapterHelper.SearchAdapterHelperDelegate() {
            @Override
            public void onDataSetChanged() {
                MentionsAdapter.this.notifyDataSetChanged();
            }
            
            @Override
            public void onSetHashtags(final ArrayList<HashtagObject> list, final HashMap<String, HashtagObject> hashMap) {
                if (MentionsAdapter.this.lastText != null) {
                    final MentionsAdapter this$0 = MentionsAdapter.this;
                    this$0.searchUsernameOrHashtag(this$0.lastText, MentionsAdapter.this.lastPosition, MentionsAdapter.this.messages, MentionsAdapter.this.lastUsernameOnly);
                }
            }
        });
    }
    
    private void checkLocationPermissionsOrStart() {
        final ChatActivity parentFragment = this.parentFragment;
        if (parentFragment != null) {
            if (parentFragment.getParentActivity() != null) {
                if (Build$VERSION.SDK_INT >= 23 && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    this.parentFragment.getParentActivity().requestPermissions(new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" }, 2);
                    return;
                }
                final TLRPC.User foundContextBot = this.foundContextBot;
                if (foundContextBot != null && foundContextBot.bot_inline_geo) {
                    this.locationProvider.start();
                }
            }
        }
    }
    
    private void onLocationUnavailable() {
        final TLRPC.User foundContextBot = this.foundContextBot;
        if (foundContextBot != null && foundContextBot.bot_inline_geo) {
            (this.lastKnownLocation = new Location("network")).setLatitude(-1000.0);
            this.lastKnownLocation.setLongitude(-1000.0);
            this.searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
        }
    }
    
    private void processFoundUser(TLRPC.User foundContextBot) {
        this.contextUsernameReqid = 0;
        this.locationProvider.stop();
        Label_0299: {
            if (foundContextBot != null && foundContextBot.bot && foundContextBot.bot_inline_placeholder != null) {
                this.foundContextBot = foundContextBot;
                final ChatActivity parentFragment = this.parentFragment;
                if (parentFragment != null) {
                    final TLRPC.Chat currentChat = parentFragment.getCurrentChat();
                    if (currentChat != null && !(this.inlineMediaEnabled = ChatObject.canSendStickers(currentChat))) {
                        this.notifyDataSetChanged();
                        this.delegate.needChangePanelVisibility(true);
                        return;
                    }
                }
                if (this.foundContextBot.bot_inline_geo) {
                    final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("inlinegeo_");
                    sb.append(this.foundContextBot.id);
                    if (!notificationsSettings.getBoolean(sb.toString(), false)) {
                        final ChatActivity parentFragment2 = this.parentFragment;
                        if (parentFragment2 != null && parentFragment2.getParentActivity() != null) {
                            foundContextBot = this.foundContextBot;
                            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.parentFragment.getParentActivity());
                            builder.setTitle(LocaleController.getString("ShareYouLocationTitle", 2131560756));
                            builder.setMessage(LocaleController.getString("ShareYouLocationInline", 2131560755));
                            final boolean[] array = { false };
                            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$MentionsAdapter$3hBy3ePtH3hOR9uoZtzoQOcheTs(this, array, foundContextBot));
                            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (DialogInterface$OnClickListener)new _$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb_uXrprc(this, array));
                            this.parentFragment.showDialog(builder.create(), (DialogInterface$OnDismissListener)new _$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA(this, array));
                            break Label_0299;
                        }
                    }
                    this.checkLocationPermissionsOrStart();
                }
            }
            else {
                this.foundContextBot = null;
                this.inlineMediaEnabled = true;
            }
        }
        if (this.foundContextBot == null) {
            this.noUserName = true;
        }
        else {
            final MentionsAdapterDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onContextSearch(true);
            }
            this.searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
        }
    }
    
    private void searchForContextBot(final String s, final String s2) {
        final TLRPC.User foundContextBot = this.foundContextBot;
        if (foundContextBot != null) {
            final String username = foundContextBot.username;
            if (username != null && username.equals(s)) {
                final String searchingContextQuery = this.searchingContextQuery;
                if (searchingContextQuery != null && searchingContextQuery.equals(s2)) {
                    return;
                }
            }
        }
        this.searchResultBotContext = null;
        this.searchResultBotContextSwitch = null;
        this.notifyDataSetChanged();
        if (this.foundContextBot != null) {
            if (!this.inlineMediaEnabled && s != null && s2 != null) {
                return;
            }
            this.delegate.needChangePanelVisibility(false);
        }
        final Runnable contextQueryRunnable = this.contextQueryRunnable;
        if (contextQueryRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(contextQueryRunnable);
            this.contextQueryRunnable = null;
        }
        Label_0249: {
            if (!TextUtils.isEmpty((CharSequence)s)) {
                final String searchingContextUsername = this.searchingContextUsername;
                if (searchingContextUsername == null || searchingContextUsername.equals(s)) {
                    break Label_0249;
                }
            }
            if (this.contextUsernameReqid != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
                this.contextUsernameReqid = 0;
            }
            if (this.contextQueryReqid != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                this.contextQueryReqid = 0;
            }
            this.foundContextBot = null;
            this.inlineMediaEnabled = true;
            this.searchingContextUsername = null;
            this.searchingContextQuery = null;
            this.locationProvider.stop();
            this.noUserName = false;
            final MentionsAdapterDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onContextSearch(false);
            }
            if (s == null) {
                return;
            }
            if (s.length() == 0) {
                return;
            }
        }
        if (s2 == null) {
            if (this.contextQueryReqid != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                this.contextQueryReqid = 0;
            }
            this.searchingContextQuery = null;
            final MentionsAdapterDelegate delegate2 = this.delegate;
            if (delegate2 != null) {
                delegate2.onContextSearch(false);
            }
            return;
        }
        final MentionsAdapterDelegate delegate3 = this.delegate;
        if (delegate3 != null) {
            if (this.foundContextBot != null) {
                delegate3.onContextSearch(true);
            }
            else if (s.equals("gif")) {
                this.searchingContextUsername = "gif";
                this.delegate.onContextSearch(false);
            }
        }
        final MessagesController instance = MessagesController.getInstance(this.currentAccount);
        final MessagesStorage instance2 = MessagesStorage.getInstance(this.currentAccount);
        this.searchingContextQuery = s2;
        AndroidUtilities.runOnUIThread(this.contextQueryRunnable = new Runnable() {
            @Override
            public void run() {
                if (MentionsAdapter.this.contextQueryRunnable != this) {
                    return;
                }
                MentionsAdapter.this.contextQueryRunnable = null;
                if (MentionsAdapter.this.foundContextBot == null && !MentionsAdapter.this.noUserName) {
                    MentionsAdapter.this.searchingContextUsername = s;
                    final TLObject userOrChat = instance.getUserOrChat(MentionsAdapter.this.searchingContextUsername);
                    if (userOrChat instanceof TLRPC.User) {
                        MentionsAdapter.this.processFoundUser((TLRPC.User)userOrChat);
                    }
                    else {
                        final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
                        tl_contacts_resolveUsername.username = MentionsAdapter.this.searchingContextUsername;
                        final MentionsAdapter this$0 = MentionsAdapter.this;
                        this$0.contextUsernameReqid = ConnectionsManager.getInstance(this$0.currentAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7_SrPstmceOE6w(this, s, instance, instance2));
                    }
                }
                else {
                    if (MentionsAdapter.this.noUserName) {
                        return;
                    }
                    final MentionsAdapter this$2 = MentionsAdapter.this;
                    this$2.searchForContextBotResults(true, this$2.foundContextBot, s2, "");
                }
            }
        }, 400L);
    }
    
    private void searchForContextBotResults(final boolean b, final TLRPC.User user, final String s, final String s2) {
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        if (!this.inlineMediaEnabled) {
            final MentionsAdapterDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onContextSearch(false);
            }
            return;
        }
        if (s == null || user == null) {
            this.searchingContextQuery = null;
            return;
        }
        if (user.bot_inline_geo && this.lastKnownLocation == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.dialog_id);
        sb.append("_");
        sb.append(s);
        sb.append("_");
        sb.append(s2);
        sb.append("_");
        sb.append(this.dialog_id);
        sb.append("_");
        sb.append(user.id);
        sb.append("_");
        Serializable value = null;
        Label_0234: {
            if (user.bot_inline_geo) {
                final Location lastKnownLocation = this.lastKnownLocation;
                if (lastKnownLocation != null && lastKnownLocation.getLatitude() != -1000.0) {
                    value = this.lastKnownLocation.getLatitude() + this.lastKnownLocation.getLongitude();
                    break Label_0234;
                }
            }
            value = "";
        }
        sb.append(value);
        final String string = sb.toString();
        final MessagesStorage instance = MessagesStorage.getInstance(this.currentAccount);
        final _$$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U $$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U = new _$$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U(this, s, b, user, s2, instance, string);
        if (b) {
            instance.getBotCache(string, $$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U);
        }
        else {
            final TLRPC.TL_messages_getInlineBotResults tl_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
            tl_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(user);
            tl_messages_getInlineBotResults.query = s;
            tl_messages_getInlineBotResults.offset = s2;
            if (user.bot_inline_geo) {
                final Location lastKnownLocation2 = this.lastKnownLocation;
                if (lastKnownLocation2 != null && lastKnownLocation2.getLatitude() != -1000.0) {
                    tl_messages_getInlineBotResults.flags |= 0x1;
                    tl_messages_getInlineBotResults.geo_point = new TLRPC.TL_inputGeoPoint();
                    tl_messages_getInlineBotResults.geo_point.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
                    tl_messages_getInlineBotResults.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
                }
            }
            final int n = (int)this.dialog_id;
            if (n != 0) {
                tl_messages_getInlineBotResults.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n);
            }
            else {
                tl_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
            }
            this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getInlineBotResults, $$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U, 2);
        }
    }
    
    private void showUsersResult(final ArrayList<TLRPC.User> searchResultUsernames, final SparseArray<TLRPC.User> searchResultUsernamesMap, final boolean b) {
        this.searchResultUsernames = searchResultUsernames;
        this.searchResultUsernamesMap = searchResultUsernamesMap;
        final Runnable cancelDelayRunnable = this.cancelDelayRunnable;
        if (cancelDelayRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(cancelDelayRunnable);
            this.cancelDelayRunnable = null;
        }
        if (b) {
            this.notifyDataSetChanged();
            this.delegate.needChangePanelVisibility(this.searchResultUsernames.isEmpty() ^ true);
        }
    }
    
    public void addHashtagsFromMessage(final CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }
    
    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        this.notifyDataSetChanged();
        final MentionsAdapterDelegate delegate = this.delegate;
        if (delegate != null) {
            delegate.needChangePanelVisibility(false);
        }
    }
    
    public String getBotCaption() {
        final TLRPC.User foundContextBot = this.foundContextBot;
        if (foundContextBot != null) {
            return foundContextBot.bot_inline_placeholder;
        }
        final String searchingContextUsername = this.searchingContextUsername;
        if (searchingContextUsername != null && searchingContextUsername.equals("gif")) {
            return "Search GIFs";
        }
        return null;
    }
    
    public TLRPC.TL_inlineBotSwitchPM getBotContextSwitch() {
        return this.searchResultBotContextSwitch;
    }
    
    public int getContextBotId() {
        final TLRPC.User foundContextBot = this.foundContextBot;
        int id;
        if (foundContextBot != null) {
            id = foundContextBot.id;
        }
        else {
            id = 0;
        }
        return id;
    }
    
    public String getContextBotName() {
        final TLRPC.User foundContextBot = this.foundContextBot;
        String username;
        if (foundContextBot != null) {
            username = foundContextBot.username;
        }
        else {
            username = "";
        }
        return username;
    }
    
    public TLRPC.User getContextBotUser() {
        return this.foundContextBot;
    }
    
    public Object getItem(final int index) {
        if (this.searchResultBotContext != null) {
            final TLRPC.TL_inlineBotSwitchPM searchResultBotContextSwitch = this.searchResultBotContextSwitch;
            int index2 = index;
            if (searchResultBotContextSwitch != null) {
                if (index == 0) {
                    return searchResultBotContextSwitch;
                }
                index2 = index - 1;
            }
            if (index2 >= 0 && index2 < this.searchResultBotContext.size()) {
                return this.searchResultBotContext.get(index2);
            }
            return null;
        }
        else {
            final ArrayList<TLRPC.User> searchResultUsernames = this.searchResultUsernames;
            if (searchResultUsernames != null) {
                if (index >= 0 && index < searchResultUsernames.size()) {
                    return this.searchResultUsernames.get(index);
                }
                return null;
            }
            else {
                final ArrayList<String> searchResultHashtags = this.searchResultHashtags;
                if (searchResultHashtags != null) {
                    if (index >= 0 && index < searchResultHashtags.size()) {
                        return this.searchResultHashtags.get(index);
                    }
                    return null;
                }
                else {
                    final ArrayList<DataQuery.KeywordResult> searchResultSuggestions = this.searchResultSuggestions;
                    if (searchResultSuggestions != null) {
                        if (index >= 0 && index < searchResultSuggestions.size()) {
                            return this.searchResultSuggestions.get(index);
                        }
                        return null;
                    }
                    else {
                        final ArrayList<String> searchResultCommands = this.searchResultCommands;
                        if (searchResultCommands == null || index < 0 || index >= searchResultCommands.size()) {
                            return null;
                        }
                        if (this.searchResultCommandsUsers == null || (this.botsCount == 1 && !(this.info instanceof TLRPC.TL_channelFull))) {
                            return this.searchResultCommands.get(index);
                        }
                        if (this.searchResultCommandsUsers.get(index) != null) {
                            final String value = this.searchResultCommands.get(index);
                            String username;
                            if (this.searchResultCommandsUsers.get(index) != null) {
                                username = this.searchResultCommandsUsers.get(index).username;
                            }
                            else {
                                username = "";
                            }
                            return String.format("%s@%s", value, username);
                        }
                        return String.format("%s", this.searchResultCommands.get(index));
                    }
                }
            }
        }
    }
    
    @Override
    public int getItemCount() {
        final TLRPC.User foundContextBot = this.foundContextBot;
        int n = 1;
        if (foundContextBot != null && !this.inlineMediaEnabled) {
            return 1;
        }
        final ArrayList<TLRPC.BotInlineResult> searchResultBotContext = this.searchResultBotContext;
        if (searchResultBotContext != null) {
            final int size = searchResultBotContext.size();
            if (this.searchResultBotContextSwitch == null) {
                n = 0;
            }
            return size + n;
        }
        final ArrayList<TLRPC.User> searchResultUsernames = this.searchResultUsernames;
        if (searchResultUsernames != null) {
            return searchResultUsernames.size();
        }
        final ArrayList<String> searchResultHashtags = this.searchResultHashtags;
        if (searchResultHashtags != null) {
            return searchResultHashtags.size();
        }
        final ArrayList<String> searchResultCommands = this.searchResultCommands;
        if (searchResultCommands != null) {
            return searchResultCommands.size();
        }
        final ArrayList<DataQuery.KeywordResult> searchResultSuggestions = this.searchResultSuggestions;
        if (searchResultSuggestions != null) {
            return searchResultSuggestions.size();
        }
        return 0;
    }
    
    public int getItemPosition(final int n) {
        int n2 = n;
        if (this.searchResultBotContext != null) {
            n2 = n;
            if (this.searchResultBotContextSwitch != null) {
                n2 = n - 1;
            }
        }
        return n2;
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 3;
        }
        if (this.searchResultBotContext == null) {
            return 0;
        }
        if (n == 0 && this.searchResultBotContextSwitch != null) {
            return 2;
        }
        return 1;
    }
    
    public int getResultLength() {
        return this.resultLength;
    }
    
    public int getResultStartPosition() {
        return this.resultStartPosition;
    }
    
    public ArrayList<TLRPC.BotInlineResult> getSearchResultBotContext() {
        return this.searchResultBotContext;
    }
    
    public boolean isBannedInline() {
        return this.foundContextBot != null && !this.inlineMediaEnabled;
    }
    
    public boolean isBotCommands() {
        return this.searchResultCommands != null;
    }
    
    public boolean isBotContext() {
        return this.searchResultBotContext != null;
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        return this.foundContextBot == null || this.inlineMediaEnabled;
    }
    
    public boolean isLongClickEnabled() {
        return this.searchResultHashtags != null || this.searchResultCommands != null;
    }
    
    public boolean isMediaLayout() {
        return this.contextMedia;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        final int itemViewType = viewHolder.getItemViewType();
        final boolean b = false;
        if (itemViewType == 3) {
            final TextView textView = (TextView)viewHolder.itemView;
            final TLRPC.Chat currentChat = this.parentFragment.getCurrentChat();
            if (currentChat != null) {
                if (!ChatObject.hasAdminRights(currentChat)) {
                    final TLRPC.TL_chatBannedRights default_banned_rights = currentChat.default_banned_rights;
                    if (default_banned_rights != null && default_banned_rights.send_inline) {
                        textView.setText((CharSequence)LocaleController.getString("GlobalAttachInlineRestricted", 2131559591));
                        return;
                    }
                }
                if (AndroidUtilities.isBannedForever(currentChat.banned_rights)) {
                    textView.setText((CharSequence)LocaleController.getString("AttachInlineRestrictedForever", 2131558720));
                }
                else {
                    textView.setText((CharSequence)LocaleController.formatString("AttachInlineRestricted", 2131558719, LocaleController.formatDateForBan(currentChat.banned_rights.until_date)));
                }
            }
        }
        else if (this.searchResultBotContext != null) {
            final boolean b2 = this.searchResultBotContextSwitch != null;
            if (viewHolder.getItemViewType() == 2) {
                if (b2) {
                    ((BotSwitchCell)viewHolder.itemView).setText(this.searchResultBotContextSwitch.text);
                }
            }
            else {
                int index = n;
                if (b2) {
                    index = n - 1;
                }
                final ContextLinkCell contextLinkCell = (ContextLinkCell)viewHolder.itemView;
                final TLRPC.BotInlineResult botInlineResult = this.searchResultBotContext.get(index);
                final boolean contextMedia = this.contextMedia;
                final boolean b3 = index != this.searchResultBotContext.size() - 1;
                boolean b4 = b;
                if (b2) {
                    b4 = b;
                    if (index == 0) {
                        b4 = true;
                    }
                }
                contextLinkCell.setLink(botInlineResult, contextMedia, b3, b4);
            }
        }
        else {
            final ArrayList<TLRPC.User> searchResultUsernames = this.searchResultUsernames;
            if (searchResultUsernames != null) {
                ((MentionCell)viewHolder.itemView).setUser(searchResultUsernames.get(n));
            }
            else {
                final ArrayList<String> searchResultHashtags = this.searchResultHashtags;
                if (searchResultHashtags != null) {
                    ((MentionCell)viewHolder.itemView).setText(searchResultHashtags.get(n));
                }
                else {
                    final ArrayList<DataQuery.KeywordResult> searchResultSuggestions = this.searchResultSuggestions;
                    if (searchResultSuggestions != null) {
                        ((MentionCell)viewHolder.itemView).setEmojiSuggestion(searchResultSuggestions.get(n));
                    }
                    else {
                        final ArrayList<String> searchResultCommands = this.searchResultCommands;
                        if (searchResultCommands != null) {
                            final MentionCell mentionCell = (MentionCell)viewHolder.itemView;
                            final String s = searchResultCommands.get(n);
                            final String s2 = this.searchResultCommandsHelp.get(n);
                            final ArrayList<TLRPC.User> searchResultCommandsUsers = this.searchResultCommandsUsers;
                            TLRPC.User user;
                            if (searchResultCommandsUsers != null) {
                                user = searchResultCommandsUsers.get(n);
                            }
                            else {
                                user = null;
                            }
                            mentionCell.setBotCommand(s, s2, user);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Object o;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    o = new TextView(this.mContext);
                    ((TextView)o).setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                    ((TextView)o).setTextSize(1, 14.0f);
                    ((TextView)o).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
                }
                else {
                    o = new BotSwitchCell(this.mContext);
                }
            }
            else {
                o = new ContextLinkCell(this.mContext);
                ((ContextLinkCell)o).setDelegate((ContextLinkCell.ContextLinkCellDelegate)new _$$Lambda$MentionsAdapter$VoCNDYNNW_mhCQ_lScxh9_TCNkI(this));
            }
        }
        else {
            o = new MentionCell(this.mContext);
            ((MentionCell)o).setIsDarkTheme(this.isDarkTheme);
        }
        return new RecyclerListView.Holder((View)o);
    }
    
    public void onDestroy() {
        final LocationProvider locationProvider = this.locationProvider;
        if (locationProvider != null) {
            locationProvider.stop();
        }
        final Runnable contextQueryRunnable = this.contextQueryRunnable;
        if (contextQueryRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(contextQueryRunnable);
            this.contextQueryRunnable = null;
        }
        if (this.contextUsernameReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
            this.contextUsernameReqid = 0;
        }
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        this.foundContextBot = null;
        this.inlineMediaEnabled = true;
        this.searchingContextUsername = null;
        this.searchingContextQuery = null;
        this.noUserName = false;
    }
    
    public void onRequestPermissionsResultFragment(final int n, final String[] array, final int[] array2) {
        if (n == 2) {
            final TLRPC.User foundContextBot = this.foundContextBot;
            if (foundContextBot != null && foundContextBot.bot_inline_geo) {
                if (array2.length > 0 && array2[0] == 0) {
                    this.locationProvider.start();
                }
                else {
                    this.onLocationUnavailable();
                }
            }
        }
    }
    
    public void searchForContextBotForNextOffset() {
        if (this.contextQueryReqid == 0) {
            final String nextQueryOffset = this.nextQueryOffset;
            if (nextQueryOffset != null && nextQueryOffset.length() != 0) {
                final TLRPC.User foundContextBot = this.foundContextBot;
                if (foundContextBot != null) {
                    final String searchingContextQuery = this.searchingContextQuery;
                    if (searchingContextQuery != null) {
                        this.searchForContextBotResults(true, foundContextBot, searchingContextQuery, this.nextQueryOffset);
                    }
                }
            }
        }
    }
    
    public void searchUsernameOrHashtag(String username, int i, final ArrayList<MessageObject> list, final boolean lastUsernameOnly) {
        final Runnable cancelDelayRunnable = this.cancelDelayRunnable;
        if (cancelDelayRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(cancelDelayRunnable);
            this.cancelDelayRunnable = null;
        }
        if (this.channelReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.channelReqId, true);
            this.channelReqId = 0;
        }
        final Runnable searchGlobalRunnable = this.searchGlobalRunnable;
        if (searchGlobalRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(searchGlobalRunnable);
            this.searchGlobalRunnable = null;
        }
        if (TextUtils.isEmpty((CharSequence)username)) {
            this.searchForContextBot(null, null);
            this.delegate.needChangePanelVisibility(false);
            this.lastText = null;
            return;
        }
        int j;
        if (username.length() > 0) {
            j = i - 1;
        }
        else {
            j = i;
        }
        this.lastText = null;
        this.lastUsernameOnly = lastUsernameOnly;
        final StringBuilder sb = new StringBuilder();
        if (!lastUsernameOnly && this.needBotContext && username.charAt(0) == '@') {
            final int index = username.indexOf(32);
            final int length = username.length();
            final String s = "";
            String s2;
            String substring;
            if (index > 0) {
                s2 = username.substring(1, index);
                substring = username.substring(index + 1);
            }
            else if (username.charAt(length - 1) == 't' && username.charAt(length - 2) == 'o' && username.charAt(length - 3) == 'b') {
                s2 = username.substring(1);
                substring = "";
            }
            else {
                this.searchForContextBot(null, null);
                substring = (s2 = null);
            }
            String s3 = s;
            Label_0381: {
                if (s2 != null) {
                    s3 = s;
                    if (s2.length() >= 1) {
                        for (int k = 1; k < s2.length(); ++k) {
                            final char char1 = s2.charAt(k);
                            if ((char1 < '0' || char1 > '9') && (char1 < 'a' || char1 > 'z') && (char1 < 'A' || char1 > 'Z') && char1 != '_') {
                                s3 = s;
                                break Label_0381;
                            }
                        }
                        s3 = s2;
                    }
                }
            }
            this.searchForContextBot(s3, substring);
        }
        else {
            this.searchForContextBot(null, null);
        }
        if (this.foundContextBot != null) {
            return;
        }
        final MessagesController instance = MessagesController.getInstance(this.currentAccount);
        Label_0812: {
            Label_0447: {
                if (lastUsernameOnly) {
                    sb.append(username.substring(1));
                    this.resultStartPosition = 0;
                    this.resultLength = sb.length();
                    i = 0;
                }
                else {
                    while (j >= 0) {
                        if (j < username.length()) {
                            final char char2 = username.charAt(j);
                            Label_0792: {
                                if (j != 0) {
                                    final int n = j - 1;
                                    if (username.charAt(n) != ' ') {
                                        if (username.charAt(n) != '\n') {
                                            break Label_0792;
                                        }
                                    }
                                }
                                if (char2 == '@') {
                                    if (this.needUsernames || (this.needBotContext && j == 0)) {
                                        if (this.info == null && j != 0) {
                                            this.lastText = username;
                                            this.lastPosition = i;
                                            this.messages = list;
                                            this.delegate.needChangePanelVisibility(false);
                                            return;
                                        }
                                        this.resultStartPosition = j;
                                        this.resultLength = sb.length() + 1;
                                        i = 0;
                                        break Label_0812;
                                    }
                                }
                                else if (char2 == '#') {
                                    if (this.searchAdapterHelper.loadRecentHashtags()) {
                                        this.resultStartPosition = j;
                                        this.resultLength = sb.length() + 1;
                                        sb.insert(0, char2);
                                        i = 1;
                                        break Label_0447;
                                    }
                                    this.lastText = username;
                                    this.lastPosition = i;
                                    this.messages = list;
                                    this.delegate.needChangePanelVisibility(false);
                                    return;
                                }
                                else {
                                    if (j == 0 && this.botInfo != null && char2 == '/') {
                                        this.resultStartPosition = j;
                                        this.resultLength = sb.length() + 1;
                                        i = 2;
                                        break Label_0447;
                                    }
                                    if (char2 == ':' && sb.length() > 0 && (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(sb.charAt(0)) < 0 || sb.length() > 1)) {
                                        this.resultStartPosition = j;
                                        this.resultLength = sb.length() + 1;
                                        i = 3;
                                        break Label_0447;
                                    }
                                }
                            }
                            sb.insert(0, char2);
                        }
                        --j;
                    }
                    i = -1;
                }
            }
            j = -1;
        }
        if (i == -1) {
            this.delegate.needChangePanelVisibility(false);
            return;
        }
        if (i == 0) {
            final ArrayList<Integer> list2 = new ArrayList<Integer>();
            int from_id;
            for (i = 0; i < Math.min(100, list.size()); ++i) {
                from_id = list.get(i).messageOwner.from_id;
                if (!list2.contains(from_id)) {
                    list2.add(from_id);
                }
            }
            final String lowerCase = sb.toString().toLowerCase();
            final boolean b = lowerCase.indexOf(32) >= 0;
            final ArrayList list3 = new ArrayList<Object>();
            final SparseArray sparseArray = new SparseArray();
            final SparseArray sparseArray2 = new SparseArray();
            final ArrayList<TLRPC.TL_topPeer> inlineBots = DataQuery.getInstance(this.currentAccount).inlineBots;
            if (!lastUsernameOnly && this.needBotContext && j == 0 && !inlineBots.isEmpty()) {
                int l = 0;
                int n2 = 0;
                while (l < inlineBots.size()) {
                    final TLRPC.User user = instance.getUser(inlineBots.get(l).peer.user_id);
                    if (user != null) {
                        username = user.username;
                        i = n2;
                        Label_1127: {
                            if (username != null) {
                                i = n2;
                                if (username.length() > 0) {
                                    if (lowerCase.length() <= 0 || !user.username.toLowerCase().startsWith(lowerCase)) {
                                        i = n2;
                                        if (lowerCase.length() != 0) {
                                            break Label_1127;
                                        }
                                    }
                                    list3.add(user);
                                    sparseArray.put(user.id, (Object)user);
                                    i = n2 + 1;
                                }
                            }
                        }
                        if ((n2 = i) == 5) {
                            break;
                        }
                    }
                    ++l;
                }
            }
            final ChatActivity parentFragment = this.parentFragment;
            TLRPC.Chat chat;
            if (parentFragment != null) {
                chat = parentFragment.getCurrentChat();
            }
            else {
                final TLRPC.ChatFull info = this.info;
                if (info != null) {
                    chat = instance.getChat(info.id);
                }
                else {
                    chat = null;
                }
            }
            if (chat != null) {
                final TLRPC.ChatFull info2 = this.info;
                if (info2 != null && info2.participants != null && (!ChatObject.isChannel(chat) || chat.megagroup)) {
                    TLRPC.User user2;
                    String username2;
                    String first_name;
                    String last_name;
                    for (i = 0; i < this.info.participants.participants.size(); ++i) {
                        user2 = instance.getUser(this.info.participants.participants.get(i).user_id);
                        if (user2 != null && (lastUsernameOnly || !UserObject.isUserSelf(user2))) {
                            if (sparseArray.indexOfKey(user2.id) < 0) {
                                if (lowerCase.length() == 0) {
                                    if (!user2.deleted) {
                                        list3.add(user2);
                                    }
                                }
                                else {
                                    username2 = user2.username;
                                    if (username2 != null && username2.length() > 0 && user2.username.toLowerCase().startsWith(lowerCase)) {
                                        list3.add(user2);
                                        sparseArray2.put(user2.id, (Object)user2);
                                    }
                                    else {
                                        first_name = user2.first_name;
                                        if (first_name != null && first_name.length() > 0 && user2.first_name.toLowerCase().startsWith(lowerCase)) {
                                            list3.add(user2);
                                            sparseArray2.put(user2.id, (Object)user2);
                                        }
                                        else {
                                            last_name = user2.last_name;
                                            if (last_name != null && last_name.length() > 0 && user2.last_name.toLowerCase().startsWith(lowerCase)) {
                                                list3.add(user2);
                                                sparseArray2.put(user2.id, (Object)user2);
                                            }
                                            else if (b && ContactsController.formatName(user2.first_name, user2.last_name).toLowerCase().startsWith(lowerCase)) {
                                                list3.add(user2);
                                                sparseArray2.put(user2.id, (Object)user2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort((List<Object>)list3, new _$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY(sparseArray2, list2));
            this.searchResultHashtags = null;
            this.searchResultCommands = null;
            this.searchResultCommandsHelp = null;
            this.searchResultCommandsUsers = null;
            this.searchResultSuggestions = null;
            if (chat != null && chat.megagroup && lowerCase.length() > 0) {
                if (list3.size() < 5) {
                    AndroidUtilities.runOnUIThread(this.cancelDelayRunnable = new _$$Lambda$MentionsAdapter$_MTgLbVZU0vjX841sg9FU0IKm0g(this, list3, sparseArray2), 1000L);
                }
                else {
                    this.showUsersResult(list3, (SparseArray<TLRPC.User>)sparseArray2, true);
                }
                AndroidUtilities.runOnUIThread(this.searchGlobalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (MentionsAdapter.this.searchGlobalRunnable != this) {
                            return;
                        }
                        final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
                        tl_channels_getParticipants.channel = MessagesController.getInputChannel(chat);
                        tl_channels_getParticipants.limit = 20;
                        tl_channels_getParticipants.offset = 0;
                        final TLRPC.TL_channelParticipantsSearch filter = new TLRPC.TL_channelParticipantsSearch();
                        filter.q = lowerCase;
                        tl_channels_getParticipants.filter = filter;
                        final int access$1604 = ++MentionsAdapter.this.channelLastReqId;
                        final MentionsAdapter this$0 = MentionsAdapter.this;
                        this$0.channelReqId = ConnectionsManager.getInstance(this$0.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0(this, access$1604, list3, sparseArray2, instance));
                    }
                }, 200L);
            }
            else {
                this.showUsersResult(list3, (SparseArray<TLRPC.User>)sparseArray2, true);
            }
        }
        else if (i == 1) {
            final ArrayList<String> searchResultHashtags = new ArrayList<String>();
            final String lowerCase2 = sb.toString().toLowerCase();
            ArrayList<SearchAdapterHelper.HashtagObject> hashtags;
            SearchAdapterHelper.HashtagObject hashtagObject;
            String hashtag;
            for (hashtags = this.searchAdapterHelper.getHashtags(), i = 0; i < hashtags.size(); ++i) {
                hashtagObject = (SearchAdapterHelper.HashtagObject)hashtags.get(i);
                if (hashtagObject != null) {
                    hashtag = hashtagObject.hashtag;
                    if (hashtag != null && hashtag.startsWith(lowerCase2)) {
                        searchResultHashtags.add(hashtagObject.hashtag);
                    }
                }
            }
            this.searchResultHashtags = searchResultHashtags;
            this.searchResultUsernames = null;
            this.searchResultUsernamesMap = null;
            this.searchResultCommands = null;
            this.searchResultCommandsHelp = null;
            this.searchResultCommandsUsers = null;
            this.searchResultSuggestions = null;
            this.notifyDataSetChanged();
            this.delegate.needChangePanelVisibility(searchResultHashtags.isEmpty() ^ true);
        }
        else if (i == 2) {
            final ArrayList<String> searchResultCommands = new ArrayList<String>();
            final ArrayList<String> searchResultCommandsHelp = new ArrayList<String>();
            final ArrayList<TLRPC.User> searchResultCommandsUsers = new ArrayList<TLRPC.User>();
            final String lowerCase3 = sb.toString().toLowerCase();
            TLRPC.BotInfo botInfo;
            int index2;
            TLRPC.TL_botCommand tl_botCommand;
            String command;
            StringBuilder sb2;
            for (i = 0; i < this.botInfo.size(); ++i) {
                for (botInfo = (TLRPC.BotInfo)this.botInfo.valueAt(i), index2 = 0; index2 < botInfo.commands.size(); ++index2) {
                    tl_botCommand = botInfo.commands.get(index2);
                    if (tl_botCommand != null) {
                        command = tl_botCommand.command;
                        if (command != null && command.startsWith(lowerCase3)) {
                            sb2 = new StringBuilder();
                            sb2.append("/");
                            sb2.append(tl_botCommand.command);
                            searchResultCommands.add(sb2.toString());
                            searchResultCommandsHelp.add(tl_botCommand.description);
                            searchResultCommandsUsers.add(instance.getUser(botInfo.user_id));
                        }
                    }
                }
            }
            this.searchResultHashtags = null;
            this.searchResultUsernames = null;
            this.searchResultUsernamesMap = null;
            this.searchResultSuggestions = null;
            this.searchResultCommands = searchResultCommands;
            this.searchResultCommandsHelp = searchResultCommandsHelp;
            this.searchResultCommandsUsers = searchResultCommandsUsers;
            this.notifyDataSetChanged();
            this.delegate.needChangePanelVisibility(searchResultCommands.isEmpty() ^ true);
        }
        else if (i == 3) {
            final String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
            if (!Arrays.equals(currentKeyboardLanguage, this.lastSearchKeyboardLanguage)) {
                DataQuery.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
            }
            this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
            DataQuery.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, sb.toString(), false, (DataQuery.KeywordResultCallback)new _$$Lambda$MentionsAdapter$rScwFUSTmUPNK4byYGdy0eHPN2M(this));
        }
    }
    
    public void setBotInfo(final SparseArray<TLRPC.BotInfo> botInfo) {
        this.botInfo = botInfo;
    }
    
    public void setBotsCount(final int botsCount) {
        this.botsCount = botsCount;
    }
    
    public void setChatInfo(final TLRPC.ChatFull info) {
        this.currentAccount = UserConfig.selectedAccount;
        this.info = info;
        if (!this.inlineMediaEnabled && this.foundContextBot != null) {
            final ChatActivity parentFragment = this.parentFragment;
            if (parentFragment != null) {
                final TLRPC.Chat currentChat = parentFragment.getCurrentChat();
                if (currentChat != null) {
                    this.inlineMediaEnabled = ChatObject.canSendStickers(currentChat);
                    if (this.inlineMediaEnabled) {
                        this.searchResultUsernames = null;
                        this.notifyDataSetChanged();
                        this.delegate.needChangePanelVisibility(false);
                        this.processFoundUser(this.foundContextBot);
                    }
                }
            }
        }
        final String lastText = this.lastText;
        if (lastText != null) {
            this.searchUsernameOrHashtag(lastText, this.lastPosition, this.messages, this.lastUsernameOnly);
        }
    }
    
    public void setNeedBotContext(final boolean needBotContext) {
        this.needBotContext = needBotContext;
    }
    
    public void setNeedUsernames(final boolean needUsernames) {
        this.needUsernames = needUsernames;
    }
    
    public void setParentFragment(final ChatActivity parentFragment) {
        this.parentFragment = parentFragment;
    }
    
    public void setSearchingMentions(final boolean isSearchingMentions) {
        this.isSearchingMentions = isSearchingMentions;
    }
    
    public interface MentionsAdapterDelegate
    {
        void needChangePanelVisibility(final boolean p0);
        
        void onContextClick(final TLRPC.BotInlineResult p0);
        
        void onContextSearch(final boolean p0);
    }
}
