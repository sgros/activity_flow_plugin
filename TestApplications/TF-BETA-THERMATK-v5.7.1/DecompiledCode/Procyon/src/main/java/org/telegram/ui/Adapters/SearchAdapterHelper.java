// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import java.util.Collection;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.AndroidUtilities;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.messenger.FileLog;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import java.util.HashMap;
import android.util.SparseArray;
import org.telegram.tgnet.TLObject;
import java.util.ArrayList;

public class SearchAdapterHelper
{
    private boolean allResultsAreGlobal;
    private int channelLastReqId;
    private int channelReqId;
    private int currentAccount;
    private SearchAdapterHelperDelegate delegate;
    private ArrayList<TLObject> globalSearch;
    private SparseArray<TLObject> globalSearchMap;
    private ArrayList<TLObject> groupSearch;
    private SparseArray<TLObject> groupSearchMap;
    private ArrayList<HashtagObject> hashtags;
    private HashMap<String, HashtagObject> hashtagsByText;
    private boolean hashtagsLoadedFromDb;
    private String lastFoundChannel;
    private String lastFoundUsername;
    private int lastReqId;
    private ArrayList<TLObject> localSearchResults;
    private ArrayList<TLObject> localServerSearch;
    private int reqId;
    
    public SearchAdapterHelper(final boolean allResultsAreGlobal) {
        this.reqId = 0;
        this.lastFoundUsername = null;
        this.localServerSearch = new ArrayList<TLObject>();
        this.globalSearch = new ArrayList<TLObject>();
        this.globalSearchMap = (SparseArray<TLObject>)new SparseArray();
        this.groupSearch = new ArrayList<TLObject>();
        this.groupSearchMap = (SparseArray<TLObject>)new SparseArray();
        this.currentAccount = UserConfig.selectedAccount;
        this.channelReqId = 0;
        this.hashtagsLoadedFromDb = false;
        this.allResultsAreGlobal = allResultsAreGlobal;
    }
    
    private void putRecentHashtags(final ArrayList<HashtagObject> list) {
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SearchAdapterHelper$GIT1_XH69tqWW5ny1WrNuu7bX8Q(this, list));
    }
    
    public void addHashtagsFromMessage(final CharSequence input) {
        if (input == null) {
            return;
        }
        final Matcher matcher = Pattern.compile("(^|\\s)#[\\w@.]+").matcher(input);
        boolean b = false;
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            int n = start;
            if (input.charAt(start) != '@') {
                n = start;
                if (input.charAt(start) != '#') {
                    n = start + 1;
                }
            }
            final String string = input.subSequence(n, end).toString();
            if (this.hashtagsByText == null) {
                this.hashtagsByText = new HashMap<String, HashtagObject>();
                this.hashtags = new ArrayList<HashtagObject>();
            }
            HashtagObject element = this.hashtagsByText.get(string);
            if (element == null) {
                element = new HashtagObject();
                element.hashtag = string;
                this.hashtagsByText.put(element.hashtag, element);
            }
            else {
                this.hashtags.remove(element);
            }
            element.date = (int)(System.currentTimeMillis() / 1000L);
            this.hashtags.add(0, element);
            b = true;
        }
        if (b) {
            this.putRecentHashtags(this.hashtags);
        }
    }
    
    public void clearRecentHashtags() {
        this.hashtags = new ArrayList<HashtagObject>();
        this.hashtagsByText = new HashMap<String, HashtagObject>();
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SearchAdapterHelper$rJWBOKrijR0k23tFEyFN_1lhPTE(this));
    }
    
    public ArrayList<TLObject> getGlobalSearch() {
        return this.globalSearch;
    }
    
    public ArrayList<TLObject> getGroupSearch() {
        return this.groupSearch;
    }
    
    public ArrayList<HashtagObject> getHashtags() {
        return this.hashtags;
    }
    
    public String getLastFoundChannel() {
        return this.lastFoundChannel;
    }
    
    public String getLastFoundUsername() {
        return this.lastFoundUsername;
    }
    
    public ArrayList<TLObject> getLocalServerSearch() {
        return this.localServerSearch;
    }
    
    public boolean isSearchInProgress() {
        return this.reqId != 0 || this.channelReqId != 0;
    }
    
    public boolean loadRecentHashtags() {
        if (this.hashtagsLoadedFromDb) {
            return true;
        }
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$SearchAdapterHelper$DPsjy4ay_Xo08TMx7YPJqUY2IzQ(this));
        return false;
    }
    
    public void mergeExcludeResults() {
        final SearchAdapterHelperDelegate delegate = this.delegate;
        if (delegate == null) {
            return;
        }
        final SparseArray<TLRPC.User> excludeUsers = delegate.getExcludeUsers();
        if (excludeUsers == null) {
            return;
        }
        for (int i = 0; i < excludeUsers.size(); ++i) {
            final TLRPC.User user = (TLRPC.User)this.globalSearchMap.get(excludeUsers.keyAt(i));
            if (user != null) {
                this.globalSearch.remove(user);
                this.localServerSearch.remove(user);
                this.globalSearchMap.remove(user.id);
            }
        }
    }
    
    public void mergeResults(final ArrayList<TLObject> localSearchResults) {
        this.localSearchResults = localSearchResults;
        if (this.globalSearchMap.size() != 0) {
            if (localSearchResults != null) {
                for (int size = localSearchResults.size(), i = 0; i < size; ++i) {
                    final TLObject tlObject = localSearchResults.get(i);
                    if (tlObject instanceof TLRPC.User) {
                        final TLRPC.User user = (TLRPC.User)tlObject;
                        final TLRPC.User user2 = (TLRPC.User)this.globalSearchMap.get(user.id);
                        if (user2 != null) {
                            this.globalSearch.remove(user2);
                            this.localServerSearch.remove(user2);
                            this.globalSearchMap.remove(user2.id);
                        }
                        final TLObject o = (TLObject)this.groupSearchMap.get(user.id);
                        if (o != null) {
                            this.groupSearch.remove(o);
                            this.groupSearchMap.remove(user.id);
                        }
                    }
                    else if (tlObject instanceof TLRPC.Chat) {
                        final TLRPC.Chat chat = (TLRPC.Chat)this.globalSearchMap.get(-((TLRPC.Chat)tlObject).id);
                        if (chat != null) {
                            this.globalSearch.remove(chat);
                            this.localServerSearch.remove(chat);
                            this.globalSearchMap.remove(-chat.id);
                        }
                    }
                }
            }
        }
    }
    
    public void queryServerSearch(final String s, final boolean b, final boolean b2, final boolean b3, final boolean b4, int n, final int n2) {
        if (this.reqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
        }
        if (this.channelReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.channelReqId, true);
            this.channelReqId = 0;
        }
        if (s == null) {
            this.groupSearch.clear();
            this.groupSearchMap.clear();
            this.globalSearch.clear();
            this.globalSearchMap.clear();
            this.localServerSearch.clear();
            this.lastReqId = 0;
            this.channelLastReqId = 0;
            this.delegate.onDataSetChanged();
            return;
        }
        if (s.length() > 0) {
            if (n != 0) {
                final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
                if (n2 == 1) {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsAdmins();
                }
                else if (n2 == 3) {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsBanned();
                }
                else if (n2 == 0) {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsKicked();
                }
                else {
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsSearch();
                }
                tl_channels_getParticipants.filter.q = s;
                tl_channels_getParticipants.limit = 50;
                tl_channels_getParticipants.offset = 0;
                tl_channels_getParticipants.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(n);
                n = this.channelLastReqId + 1;
                this.channelLastReqId = n;
                this.channelReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$SearchAdapterHelper$lQm_KegCMNFAimI1TKtkhyCcOJg(this, n, s, b4), 2);
            }
            else {
                this.lastFoundChannel = s.toLowerCase();
            }
        }
        else {
            this.groupSearch.clear();
            this.groupSearchMap.clear();
            this.channelLastReqId = 0;
            this.delegate.onDataSetChanged();
        }
        if (b) {
            if (s.length() > 0) {
                final TLRPC.TL_contacts_search tl_contacts_search = new TLRPC.TL_contacts_search();
                tl_contacts_search.q = s;
                tl_contacts_search.limit = 50;
                n = this.lastReqId + 1;
                this.lastReqId = n;
                this.reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_search, new _$$Lambda$SearchAdapterHelper$dscYso9YL4gEzQpoIdpN_Bu9BdA(this, n, b2, b3, b4, s), 2);
            }
            else {
                this.globalSearch.clear();
                this.globalSearchMap.clear();
                this.localServerSearch.clear();
                this.lastReqId = 0;
                this.delegate.onDataSetChanged();
            }
        }
    }
    
    public void setDelegate(final SearchAdapterHelperDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setHashtags(final ArrayList<HashtagObject> hashtags, final HashMap<String, HashtagObject> hashtagsByText) {
        this.hashtags = hashtags;
        this.hashtagsByText = hashtagsByText;
        this.hashtagsLoadedFromDb = true;
        this.delegate.onSetHashtags(hashtags, hashtagsByText);
    }
    
    public void unloadRecentHashtags() {
        this.hashtagsLoadedFromDb = false;
    }
    
    protected static final class DialogSearchResult
    {
        public int date;
        public CharSequence name;
        public TLObject object;
    }
    
    public static class HashtagObject
    {
        int date;
        String hashtag;
    }
    
    public interface SearchAdapterHelperDelegate
    {
        SparseArray<TLRPC.User> getExcludeUsers();
        
        void onDataSetChanged();
        
        void onSetHashtags(final ArrayList<HashtagObject> p0, final HashMap<String, HashtagObject> p1);
    }
}
