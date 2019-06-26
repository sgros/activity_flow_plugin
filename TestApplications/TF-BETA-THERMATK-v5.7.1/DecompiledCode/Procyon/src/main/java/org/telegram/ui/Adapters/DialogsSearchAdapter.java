// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import org.telegram.messenger.UserObject;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.messenger.Utilities;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Cells.LoadingCell;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.animation.LayoutAnimationController;
import android.view.MotionEvent;
import android.view.ViewGroup;
import org.telegram.ui.Cells.ProfileSearchCell;
import android.view.View$OnClickListener;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.tgnet.NativeByteBuffer;
import android.text.style.ForegroundColorSpan;
import org.telegram.ui.ActionBar.Theme;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ChatObject;
import org.telegram.tgnet.AbstractSerializedData;
import java.util.Locale;
import org.telegram.messenger.LocaleController;
import org.telegram.SQLite.SQLitePreparedStatement;
import android.view.View;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.messenger.FileLog;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import android.text.TextUtils;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.DataQuery;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLObject;
import android.util.LongSparseArray;
import java.util.ArrayList;
import android.content.Context;
import org.telegram.ui.Components.RecyclerListView;

public class DialogsSearchAdapter extends SelectionAdapter
{
    private int currentAccount;
    private DialogsSearchAdapterDelegate delegate;
    private int dialogsType;
    private RecyclerListView innerListView;
    private String lastMessagesSearchString;
    private int lastReqId;
    private int lastSearchId;
    private String lastSearchText;
    private Context mContext;
    private boolean messagesSearchEndReached;
    private int needMessagesSearch;
    private int nextSearchRate;
    private ArrayList<RecentSearchObject> recentSearchObjects;
    private LongSparseArray<RecentSearchObject> recentSearchObjectsById;
    private int reqId;
    private SearchAdapterHelper searchAdapterHelper;
    private ArrayList<TLObject> searchResult;
    private ArrayList<String> searchResultHashtags;
    private ArrayList<MessageObject> searchResultMessages;
    private ArrayList<CharSequence> searchResultNames;
    private Runnable searchRunnable;
    private Runnable searchRunnable2;
    private boolean searchWas;
    private int selfUserId;
    
    public DialogsSearchAdapter(final Context mContext, final int needMessagesSearch, final int dialogsType) {
        this.searchResult = new ArrayList<TLObject>();
        this.searchResultNames = new ArrayList<CharSequence>();
        this.searchResultMessages = new ArrayList<MessageObject>();
        this.searchResultHashtags = new ArrayList<String>();
        this.reqId = 0;
        this.lastSearchId = 0;
        this.currentAccount = UserConfig.selectedAccount;
        this.recentSearchObjects = new ArrayList<RecentSearchObject>();
        this.recentSearchObjectsById = (LongSparseArray<RecentSearchObject>)new LongSparseArray();
        (this.searchAdapterHelper = new SearchAdapterHelper(false)).setDelegate((SearchAdapterHelper.SearchAdapterHelperDelegate)new SearchAdapterHelper.SearchAdapterHelperDelegate() {
            @Override
            public void onDataSetChanged() {
                DialogsSearchAdapter.this.searchWas = true;
                if (!DialogsSearchAdapter.this.searchAdapterHelper.isSearchInProgress() && DialogsSearchAdapter.this.delegate != null) {
                    DialogsSearchAdapter.this.delegate.searchStateChanged(false);
                }
                DialogsSearchAdapter.this.notifyDataSetChanged();
            }
            
            @Override
            public void onSetHashtags(final ArrayList<HashtagObject> list, final HashMap<String, HashtagObject> hashMap) {
                for (int i = 0; i < list.size(); ++i) {
                    DialogsSearchAdapter.this.searchResultHashtags.add(list.get(i).hashtag);
                }
                if (DialogsSearchAdapter.this.delegate != null) {
                    DialogsSearchAdapter.this.delegate.searchStateChanged(false);
                }
                DialogsSearchAdapter.this.notifyDataSetChanged();
            }
        });
        this.mContext = mContext;
        this.needMessagesSearch = needMessagesSearch;
        this.dialogsType = dialogsType;
        this.selfUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        this.loadRecentSearch();
        DataQuery.getInstance(this.currentAccount).loadHints(true);
    }
    
    private void searchDialogsInternal(final String s, final int n) {
        if (this.needMessagesSearch == 2) {
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$jlz_Jg93hVH6t3KHP9A_P2d1MzU(this, s, n));
    }
    
    private void searchMessagesInternal(final String s) {
        if (this.needMessagesSearch != 0) {
            if (!TextUtils.isEmpty((CharSequence)this.lastMessagesSearchString) || !TextUtils.isEmpty((CharSequence)s)) {
                if (this.reqId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
                    this.reqId = 0;
                }
                if (TextUtils.isEmpty((CharSequence)s)) {
                    this.searchResultMessages.clear();
                    this.lastReqId = 0;
                    this.lastMessagesSearchString = null;
                    this.searchWas = false;
                    this.notifyDataSetChanged();
                    final DialogsSearchAdapterDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.searchStateChanged(false);
                    }
                    return;
                }
                final TLRPC.TL_messages_searchGlobal tl_messages_searchGlobal = new TLRPC.TL_messages_searchGlobal();
                tl_messages_searchGlobal.limit = 20;
                tl_messages_searchGlobal.q = s;
                if (s.equals(this.lastMessagesSearchString) && !this.searchResultMessages.isEmpty()) {
                    final ArrayList<MessageObject> searchResultMessages = this.searchResultMessages;
                    final MessageObject messageObject = searchResultMessages.get(searchResultMessages.size() - 1);
                    tl_messages_searchGlobal.offset_id = messageObject.getId();
                    tl_messages_searchGlobal.offset_rate = this.nextSearchRate;
                    final TLRPC.Peer to_id = messageObject.messageOwner.to_id;
                    int n = to_id.channel_id;
                    int user_id = 0;
                    Label_0226: {
                        if (n == 0) {
                            n = to_id.chat_id;
                            if (n == 0) {
                                user_id = to_id.user_id;
                                break Label_0226;
                            }
                        }
                        user_id = -n;
                    }
                    tl_messages_searchGlobal.offset_peer = MessagesController.getInstance(this.currentAccount).getInputPeer(user_id);
                }
                else {
                    tl_messages_searchGlobal.offset_rate = 0;
                    tl_messages_searchGlobal.offset_id = 0;
                    tl_messages_searchGlobal.offset_peer = new TLRPC.TL_inputPeerEmpty();
                }
                this.lastMessagesSearchString = s;
                final int lastReqId = this.lastReqId + 1;
                this.lastReqId = lastReqId;
                this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_searchGlobal, new _$$Lambda$DialogsSearchAdapter$jvBeESgA8AL2rHOVVbFgj80mlPg(this, lastReqId, tl_messages_searchGlobal), 2);
            }
        }
    }
    
    private void setRecentSearch(final ArrayList<RecentSearchObject> recentSearchObjects, final LongSparseArray<RecentSearchObject> recentSearchObjectsById) {
        this.recentSearchObjects = recentSearchObjects;
        this.recentSearchObjectsById = recentSearchObjectsById;
        for (int i = 0; i < this.recentSearchObjects.size(); ++i) {
            final RecentSearchObject recentSearchObject = this.recentSearchObjects.get(i);
            final TLObject object = recentSearchObject.object;
            if (object instanceof TLRPC.User) {
                MessagesController.getInstance(this.currentAccount).putUser((TLRPC.User)recentSearchObject.object, true);
            }
            else if (object instanceof TLRPC.Chat) {
                MessagesController.getInstance(this.currentAccount).putChat((TLRPC.Chat)recentSearchObject.object, true);
            }
            else if (object instanceof TLRPC.EncryptedChat) {
                MessagesController.getInstance(this.currentAccount).putEncryptedChat((TLRPC.EncryptedChat)recentSearchObject.object, true);
            }
        }
        this.notifyDataSetChanged();
    }
    
    private void updateSearchResults(final ArrayList<TLObject> list, final ArrayList<CharSequence> list2, final ArrayList<TLRPC.User> list3, final int n) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$DialogsSearchAdapter$_rG_J91Hu2opSK8sjEhFEpnZiuA(this, n, list, list3, list2));
    }
    
    public void addHashtagsFromMessage(final CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }
    
    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        this.notifyDataSetChanged();
    }
    
    public void clearRecentSearch() {
        this.recentSearchObjectsById = (LongSparseArray<RecentSearchObject>)new LongSparseArray();
        this.recentSearchObjects = new ArrayList<RecentSearchObject>();
        this.notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$It_IWmufaVpNeuW4BM4G8iqCxcU(this));
    }
    
    public RecyclerListView getInnerListView() {
        return this.innerListView;
    }
    
    public Object getItem(int n) {
        final boolean recentSearchDisplayed = this.isRecentSearchDisplayed();
        int n2 = 0;
        int n3 = 0;
        if (recentSearchDisplayed) {
            if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                n3 = 2;
            }
            if (n > n3) {
                n = n - 1 - n3;
                if (n < this.recentSearchObjects.size()) {
                    final TLObject object = this.recentSearchObjects.get(n).object;
                    TLObject tlObject;
                    if (object instanceof TLRPC.User) {
                        final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.User)object).id);
                        tlObject = object;
                        if (user != null) {
                            tlObject = user;
                        }
                    }
                    else {
                        tlObject = object;
                        if (object instanceof TLRPC.Chat) {
                            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(((TLRPC.Chat)object).id);
                            tlObject = object;
                            if (chat != null) {
                                tlObject = chat;
                            }
                        }
                    }
                    return tlObject;
                }
            }
            return null;
        }
        if (!this.searchResultHashtags.isEmpty()) {
            if (n > 0) {
                return this.searchResultHashtags.get(n - 1);
            }
            return null;
        }
        else {
            final ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            final ArrayList<TLObject> localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
            final int size = this.searchResult.size();
            final int size2 = localServerSearch.size();
            int n4;
            if (globalSearch.isEmpty()) {
                n4 = 0;
            }
            else {
                n4 = globalSearch.size() + 1;
            }
            if (!this.searchResultMessages.isEmpty()) {
                n2 = this.searchResultMessages.size() + 1;
            }
            if (n >= 0 && n < size) {
                return this.searchResult.get(n);
            }
            if (n >= size && n < size2 + size) {
                return localServerSearch.get(n - size);
            }
            if (n > size + size2 && n < n4 + size + size2) {
                return globalSearch.get(n - size - size2 - 1);
            }
            final int n5 = n4 + size;
            if (n > n5 + size2 && n < n5 + n2 + size2) {
                return this.searchResultMessages.get(n - size - n4 - size2 - 1);
            }
            return null;
        }
    }
    
    @Override
    public int getItemCount() {
        if (this.isRecentSearchDisplayed()) {
            final boolean empty = this.recentSearchObjects.isEmpty();
            int n = 0;
            int n2;
            if (!empty) {
                n2 = this.recentSearchObjects.size() + 1;
            }
            else {
                n2 = 0;
            }
            if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                n = 2;
            }
            return n2 + n;
        }
        if (!this.searchResultHashtags.isEmpty()) {
            return this.searchResultHashtags.size() + 1;
        }
        final int size = this.searchResult.size();
        final int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
        final int size3 = this.searchAdapterHelper.getGlobalSearch().size();
        final int size4 = this.searchResultMessages.size();
        int n4;
        final int n3 = n4 = size + size2;
        if (size3 != 0) {
            n4 = n3 + (size3 + 1);
        }
        int n5 = n4;
        if (size4 != 0) {
            n5 = n4 + (size4 + 1 + ((this.messagesSearchEndReached ^ true) ? 1 : 0));
        }
        return n5;
    }
    
    @Override
    public long getItemId(final int n) {
        return n;
    }
    
    @Override
    public int getItemViewType(int n) {
        final boolean recentSearchDisplayed = this.isRecentSearchDisplayed();
        final int n2 = 1;
        if (recentSearchDisplayed) {
            int n3;
            if (!DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                n3 = 2;
            }
            else {
                n3 = 0;
            }
            if (n > n3) {
                return 0;
            }
            if (n != n3 && n % 2 != 0) {
                return 5;
            }
            return 1;
        }
        else {
            if (!this.searchResultHashtags.isEmpty()) {
                if (n == 0) {
                    n = n2;
                }
                else {
                    n = 4;
                }
                return n;
            }
            final ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
            final int size = this.searchResult.size();
            final int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
            int n4;
            if (globalSearch.isEmpty()) {
                n4 = 0;
            }
            else {
                n4 = globalSearch.size() + 1;
            }
            int n5;
            if (this.searchResultMessages.isEmpty()) {
                n5 = 0;
            }
            else {
                n5 = this.searchResultMessages.size() + 1;
            }
            if ((n >= 0 && n < size + size2) || (n > size + size2 && n < n4 + size + size2)) {
                return 0;
            }
            final int n6 = n4 + size;
            if (n > n6 + size2 && n < n6 + n5 + size2) {
                return 2;
            }
            if (n5 != 0 && n == n6 + n5 + size2) {
                return 3;
            }
            return 1;
        }
    }
    
    public String getLastSearchString() {
        return this.lastMessagesSearchString;
    }
    
    public boolean hasRecentRearch() {
        return !this.recentSearchObjects.isEmpty() || !DataQuery.getInstance(this.currentAccount).hints.isEmpty();
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        final int itemViewType = viewHolder.getItemViewType();
        boolean b = true;
        if (itemViewType == 1 || itemViewType == 3) {
            b = false;
        }
        return b;
    }
    
    public boolean isGlobalSearch(final int n) {
        if (this.isRecentSearchDisplayed()) {
            return false;
        }
        if (!this.searchResultHashtags.isEmpty()) {
            return false;
        }
        final ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
        final ArrayList<TLObject> localServerSearch = this.searchAdapterHelper.getLocalServerSearch();
        final int size = this.searchResult.size();
        final int size2 = localServerSearch.size();
        int n2;
        if (globalSearch.isEmpty()) {
            n2 = 0;
        }
        else {
            n2 = globalSearch.size() + 1;
        }
        int n3;
        if (this.searchResultMessages.isEmpty()) {
            n3 = 0;
        }
        else {
            n3 = this.searchResultMessages.size() + 1;
        }
        if (n >= 0 && n < size) {
            return false;
        }
        if (n >= size && n < size2 + size) {
            return false;
        }
        if (n > size + size2 && n < n2 + size + size2) {
            return true;
        }
        final int n4 = n2 + size;
        if (n <= n4 + size2 || n < n4 + n3 + size2) {}
        return false;
    }
    
    public boolean isMessagesSearchEndReached() {
        return this.messagesSearchEndReached;
    }
    
    public boolean isRecentSearchDisplayed() {
        return this.needMessagesSearch != 2 && !this.searchWas && (!this.recentSearchObjects.isEmpty() || !DataQuery.getInstance(this.currentAccount).hints.isEmpty());
    }
    
    public void loadMoreSearchMessages() {
        this.searchMessagesInternal(this.lastMessagesSearchString);
    }
    
    public void loadRecentSearch() {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$EdS8aWM1r9L4_WkQYwpXcCyFRfM(this));
    }
    
    @Override
    public void onBindViewHolder(ViewHolder user, int index) {
        final int itemViewType = user.getItemViewType();
        int n = 2;
        final boolean b = false;
        boolean needDivider = false;
        if (itemViewType != 0) {
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        if (itemViewType != 4) {
                            if (itemViewType == 5) {
                                ((CategoryAdapterRecycler)((RecyclerListView)user.itemView).getAdapter()).setIndex(index / 2);
                            }
                        }
                        else {
                            final HashtagSearchCell hashtagSearchCell = (HashtagSearchCell)user.itemView;
                            hashtagSearchCell.setText((CharSequence)this.searchResultHashtags.get(index - 1));
                            if (index != this.searchResultHashtags.size()) {
                                needDivider = true;
                            }
                            hashtagSearchCell.setNeedDivider(needDivider);
                        }
                    }
                }
                else {
                    final DialogCell dialogCell = (DialogCell)user.itemView;
                    boolean useSeparator = b;
                    if (index != this.getItemCount() - 1) {
                        useSeparator = true;
                    }
                    dialogCell.useSeparator = useSeparator;
                    final MessageObject messageObject = (MessageObject)this.getItem(index);
                    dialogCell.setDialog(messageObject.getDialogId(), messageObject, messageObject.messageOwner.date);
                }
            }
            else {
                final GraySectionCell graySectionCell = (GraySectionCell)user.itemView;
                if (this.isRecentSearchDisplayed()) {
                    if (DataQuery.getInstance(this.currentAccount).hints.isEmpty()) {
                        n = 0;
                    }
                    if (index < n) {
                        graySectionCell.setText(LocaleController.getString("ChatHints", 2131559029));
                    }
                    else {
                        graySectionCell.setText(LocaleController.getString("Recent", 2131560537), LocaleController.getString("ClearButton", 2131559102), (View$OnClickListener)new _$$Lambda$DialogsSearchAdapter$941fnPDSgReuKOmz7WsSoxVuOTY(this));
                    }
                }
                else if (!this.searchResultHashtags.isEmpty()) {
                    graySectionCell.setText(LocaleController.getString("Hashtags", 2131559634), LocaleController.getString("ClearButton", 2131559102), (View$OnClickListener)new _$$Lambda$DialogsSearchAdapter$honrBco_zV9w0fwaI91SKdwfMI0(this));
                }
                else if (!this.searchAdapterHelper.getGlobalSearch().isEmpty() && index == this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size()) {
                    graySectionCell.setText(LocaleController.getString("GlobalSearch", 2131559594));
                }
                else {
                    graySectionCell.setText(LocaleController.getString("SearchMessages", 2131560655));
                }
            }
        }
        else {
            final ProfileSearchCell profileSearchCell = (ProfileSearchCell)user.itemView;
            final Object item = this.getItem(index);
            Object o = null;
            Object o2 = null;
            TLRPC.EncryptedChat encryptedChat = null;
            Label_0548: {
                if (item instanceof TLRPC.User) {
                    user = (ViewHolder)item;
                    o = ((TLRPC.User)user).username;
                    o2 = null;
                    encryptedChat = null;
                }
                else {
                    if (item instanceof TLRPC.Chat) {
                        final MessagesController instance = MessagesController.getInstance(this.currentAccount);
                        TLRPC.Chat chat = (TLRPC.Chat)item;
                        final TLRPC.Chat chat2 = instance.getChat(chat.id);
                        if (chat2 != null) {
                            chat = chat2;
                        }
                        o = chat.username;
                        o2 = chat;
                    }
                    else {
                        if (item instanceof TLRPC.EncryptedChat) {
                            encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(((TLRPC.EncryptedChat)item).id);
                            user = (ViewHolder)MessagesController.getInstance(this.currentAccount).getUser(encryptedChat.user_id);
                            o2 = null;
                            o = null;
                            break Label_0548;
                        }
                        o2 = null;
                        o = null;
                    }
                    user = null;
                    encryptedChat = null;
                }
            }
            Object o3;
            CharSequence charSequence;
            boolean b2;
            if (this.isRecentSearchDisplayed()) {
                profileSearchCell.useSeparator = (index != this.getItemCount() - 1);
                o3 = null;
                charSequence = null;
                b2 = true;
            }
            else {
                final ArrayList<TLObject> globalSearch = this.searchAdapterHelper.getGlobalSearch();
                final int size = this.searchResult.size();
                final int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
                int n2;
                if (globalSearch.isEmpty()) {
                    n2 = 0;
                }
                else {
                    n2 = globalSearch.size() + 1;
                }
                profileSearchCell.useSeparator = (index != this.getItemCount() - 1 && index != size + size2 - 1 && index != size + n2 + size2 - 1);
                CharSequence charSequence3 = null;
                Label_1110: {
                    Object o4 = null;
                    Label_1103: {
                        Label_0820: {
                            if (index >= this.searchResult.size()) {
                                final String lastFoundUsername = this.searchAdapterHelper.getLastFoundUsername();
                                if (!TextUtils.isEmpty((CharSequence)lastFoundUsername)) {
                                    String s;
                                    String s2;
                                    if (user != null) {
                                        s = ContactsController.formatName(((TLRPC.User)user).first_name, ((TLRPC.User)user).last_name);
                                        s2 = s.toLowerCase();
                                    }
                                    else if (o2 != null) {
                                        s = ((TLRPC.Chat)o2).title;
                                        s2 = s.toLowerCase();
                                    }
                                    else {
                                        s = null;
                                        s2 = null;
                                    }
                                    if (s != null) {
                                        index = s2.indexOf(lastFoundUsername);
                                        if (index != -1) {
                                            o4 = new SpannableStringBuilder((CharSequence)s);
                                            ((SpannableStringBuilder)o4).setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), index, lastFoundUsername.length() + index, 33);
                                            break Label_1103;
                                        }
                                    }
                                    if (o != null) {
                                        String substring = lastFoundUsername;
                                        if (lastFoundUsername.startsWith("@")) {
                                            substring = lastFoundUsername.substring(1);
                                        }
                                        try {
                                            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                            spannableStringBuilder.append((CharSequence)"@");
                                            spannableStringBuilder.append((CharSequence)o);
                                            index = ((String)o).toLowerCase().indexOf(substring);
                                            if (index != -1) {
                                                int length = substring.length();
                                                if (index == 0) {
                                                    ++length;
                                                }
                                                else {
                                                    ++index;
                                                }
                                                spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), index, length + index, 33);
                                            }
                                            o = spannableStringBuilder;
                                        }
                                        catch (Exception ex) {
                                            FileLog.e(ex);
                                        }
                                        break Label_0820;
                                    }
                                }
                                o4 = null;
                                break Label_1103;
                            }
                            final CharSequence charSequence2 = this.searchResultNames.get(index);
                            if ((o4 = charSequence2) == null) {
                                break Label_1103;
                            }
                            o4 = charSequence2;
                            if (user == null) {
                                break Label_1103;
                            }
                            final String username = ((TLRPC.User)user).username;
                            o4 = charSequence2;
                            if (username == null) {
                                break Label_1103;
                            }
                            o4 = charSequence2;
                            if (username.length() <= 0) {
                                break Label_1103;
                            }
                            final String string = charSequence2.toString();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("@");
                            sb.append(((TLRPC.User)user).username);
                            o4 = charSequence2;
                            if (!string.startsWith(sb.toString())) {
                                break Label_1103;
                            }
                            o = charSequence2;
                        }
                        o3 = null;
                        charSequence3 = (CharSequence)o;
                        break Label_1110;
                    }
                    charSequence3 = null;
                    o3 = o4;
                }
                b2 = false;
                charSequence = charSequence3;
            }
            Object string2;
            boolean b3;
            if (user != null && ((TLRPC.User)user).id == this.selfUserId) {
                string2 = LocaleController.getString("SavedMessages", 2131560633);
                charSequence = null;
                b3 = true;
            }
            else {
                b3 = false;
                string2 = o3;
            }
            CharSequence concat = null;
            Label_1289: {
                if (o2 != null && ((TLRPC.Chat)o2).participants_count != 0) {
                    String s3;
                    if (ChatObject.isChannel((TLRPC.Chat)o2) && !((TLRPC.Chat)o2).megagroup) {
                        s3 = LocaleController.formatPluralString("Subscribers", ((TLRPC.Chat)o2).participants_count);
                    }
                    else {
                        s3 = LocaleController.formatPluralString("Members", ((TLRPC.Chat)o2).participants_count);
                    }
                    if (charSequence instanceof SpannableStringBuilder) {
                        ((SpannableStringBuilder)charSequence).append((CharSequence)", ").append((CharSequence)s3);
                    }
                    else {
                        concat = s3;
                        if (!TextUtils.isEmpty(charSequence)) {
                            concat = TextUtils.concat(new CharSequence[] { charSequence, ", ", s3 });
                        }
                        break Label_1289;
                    }
                }
                concat = charSequence;
            }
            if (user == null) {
                user = (ViewHolder)o2;
            }
            profileSearchCell.setData((TLObject)user, encryptedChat, (CharSequence)string2, concat, b2, b3);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Object innerListView = null;
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n != 4) {
                            if (n == 5) {
                                innerListView = new RecyclerListView(this.mContext) {
                                    @Override
                                    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                                        if (this.getParent() != null && this.getParent().getParent() != null) {
                                            this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                                        }
                                        return super.onInterceptTouchEvent(motionEvent);
                                    }
                                };
                                ((ViewGroup)innerListView).setTag((Object)9);
                                ((RecyclerView)innerListView).setItemAnimator(null);
                                ((ViewGroup)innerListView).setLayoutAnimation((LayoutAnimationController)null);
                                final LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext) {
                                    @Override
                                    public boolean supportsPredictiveItemAnimations() {
                                        return false;
                                    }
                                };
                                layoutManager.setOrientation(0);
                                ((RecyclerView)innerListView).setLayoutManager((RecyclerView.LayoutManager)layoutManager);
                                ((RecyclerListView)innerListView).setAdapter(new CategoryAdapterRecycler());
                                ((RecyclerListView)innerListView).setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$DialogsSearchAdapter$DZkEHCwRy7JqjbUQmUNPYIVHu_I(this));
                                ((RecyclerListView)innerListView).setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$DialogsSearchAdapter$VmJg1wMYhOLJS8dwKIzHrQMjS0A(this));
                                this.innerListView = (RecyclerListView)innerListView;
                            }
                        }
                        else {
                            innerListView = new HashtagSearchCell(this.mContext);
                        }
                    }
                    else {
                        innerListView = new LoadingCell(this.mContext);
                    }
                }
                else {
                    innerListView = new DialogCell(this.mContext, false, true);
                }
            }
            else {
                innerListView = new GraySectionCell(this.mContext);
            }
        }
        else {
            innerListView = new ProfileSearchCell(this.mContext);
        }
        if (n == 5) {
            ((View)innerListView).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(86.0f)));
        }
        else {
            ((View)innerListView).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
        }
        return new RecyclerListView.Holder((View)innerListView);
    }
    
    public void putRecentSearch(final long did, final TLObject object) {
        RecentSearchObject recentSearchObject = (RecentSearchObject)this.recentSearchObjectsById.get(did);
        if (recentSearchObject == null) {
            recentSearchObject = new RecentSearchObject();
            this.recentSearchObjectsById.put(did, (Object)recentSearchObject);
        }
        else {
            this.recentSearchObjects.remove(recentSearchObject);
        }
        this.recentSearchObjects.add(0, recentSearchObject);
        recentSearchObject.did = did;
        recentSearchObject.object = object;
        recentSearchObject.date = (int)(System.currentTimeMillis() / 1000L);
        this.notifyDataSetChanged();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$DialogsSearchAdapter$EHNVdrp_nz_CUR77EsM44jqsFBg(this, did));
    }
    
    public void searchDialogs(String trim) {
        if (trim != null && trim.equals(this.lastSearchText)) {
            return;
        }
        this.lastSearchText = trim;
        if (this.searchRunnable != null) {
            Utilities.searchQueue.cancelRunnable(this.searchRunnable);
            this.searchRunnable = null;
        }
        final Runnable searchRunnable2 = this.searchRunnable2;
        if (searchRunnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(searchRunnable2);
            this.searchRunnable2 = null;
        }
        if (trim != null) {
            trim = trim.trim();
        }
        else {
            trim = null;
        }
        if (TextUtils.isEmpty((CharSequence)trim)) {
            this.searchAdapterHelper.unloadRecentHashtags();
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.searchResultHashtags.clear();
            this.searchAdapterHelper.mergeResults(null);
            if (this.needMessagesSearch != 2) {
                this.searchAdapterHelper.queryServerSearch(null, true, true, true, true, 0, 0);
            }
            this.searchWas = false;
            this.lastSearchId = -1;
            this.searchMessagesInternal(null);
            this.notifyDataSetChanged();
        }
        else {
            if (this.needMessagesSearch != 2 && trim.startsWith("#") && trim.length() == 1) {
                this.messagesSearchEndReached = true;
                if (this.searchAdapterHelper.loadRecentHashtags()) {
                    this.searchResultMessages.clear();
                    this.searchResultHashtags.clear();
                    final ArrayList<SearchAdapterHelper.HashtagObject> hashtags = this.searchAdapterHelper.getHashtags();
                    for (int i = 0; i < hashtags.size(); ++i) {
                        this.searchResultHashtags.add(((SearchAdapterHelper.HashtagObject)hashtags.get(i)).hashtag);
                    }
                    final DialogsSearchAdapterDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.searchStateChanged(false);
                    }
                }
                this.notifyDataSetChanged();
            }
            else {
                this.searchResultHashtags.clear();
                this.notifyDataSetChanged();
            }
            final int lastSearchId = this.lastSearchId + 1;
            this.lastSearchId = lastSearchId;
            Utilities.searchQueue.postRunnable(this.searchRunnable = new _$$Lambda$DialogsSearchAdapter$2OrdD5nR_nF_l2wgPqqTnpM3YF8(this, trim, lastSearchId), 300L);
        }
    }
    
    public void setDelegate(final DialogsSearchAdapterDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class CategoryAdapterRecycler extends SelectionAdapter
    {
        @Override
        public int getItemCount() {
            return DataQuery.getInstance(DialogsSearchAdapter.this.currentAccount).hints.size();
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return true;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final HintDialogCell hintDialogCell = (HintDialogCell)viewHolder.itemView;
            final TLRPC.TL_topPeer tl_topPeer = DataQuery.getInstance(DialogsSearchAdapter.this.currentAccount).hints.get(n);
            new TLRPC.TL_dialog();
            final TLRPC.Peer peer = tl_topPeer.peer;
            n = peer.user_id;
            TLRPC.User user = null;
            TLRPC.Chat chat;
            if (n != 0) {
                user = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getUser(tl_topPeer.peer.user_id);
                chat = null;
            }
            else {
                n = peer.channel_id;
                if (n != 0) {
                    n = -n;
                    chat = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getChat(tl_topPeer.peer.channel_id);
                }
                else {
                    n = peer.chat_id;
                    if (n != 0) {
                        n = -n;
                        chat = MessagesController.getInstance(DialogsSearchAdapter.this.currentAccount).getChat(tl_topPeer.peer.chat_id);
                    }
                    else {
                        n = 0;
                        chat = null;
                    }
                }
            }
            hintDialogCell.setTag((Object)n);
            String s;
            if (user != null) {
                s = UserObject.getFirstName(user);
            }
            else if (chat != null) {
                s = chat.title;
            }
            else {
                s = "";
            }
            hintDialogCell.setDialog(n, true, s);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            final HintDialogCell hintDialogCell = new HintDialogCell(DialogsSearchAdapter.this.mContext);
            ((View)hintDialogCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(AndroidUtilities.dp(80.0f), AndroidUtilities.dp(86.0f)));
            return new RecyclerListView.Holder((View)hintDialogCell);
        }
        
        public void setIndex(final int n) {
            this.notifyDataSetChanged();
        }
    }
    
    private class DialogSearchResult
    {
        public int date;
        public CharSequence name;
        public TLObject object;
    }
    
    public interface DialogsSearchAdapterDelegate
    {
        void didPressedOnSubDialog(final long p0);
        
        void needClearList();
        
        void needRemoveHint(final int p0);
        
        void searchStateChanged(final boolean p0);
    }
    
    protected static class RecentSearchObject
    {
        int date;
        long did;
        TLObject object;
    }
}
