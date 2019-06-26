// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import java.util.TimerTask;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import java.util.Timer;
import org.telegram.tgnet.TLRPC;
import android.location.Location;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;

public abstract class BaseLocationAdapter extends SelectionAdapter
{
    private int currentAccount;
    private int currentRequestNum;
    private BaseLocationAdapterDelegate delegate;
    private long dialogId;
    protected ArrayList<String> iconUrls;
    private Location lastSearchLocation;
    private String lastSearchQuery;
    protected ArrayList<TLRPC.TL_messageMediaVenue> places;
    private Timer searchTimer;
    protected boolean searching;
    private boolean searchingUser;
    
    public BaseLocationAdapter() {
        this.places = new ArrayList<TLRPC.TL_messageMediaVenue>();
        this.iconUrls = new ArrayList<String>();
        this.currentAccount = UserConfig.selectedAccount;
    }
    
    private void searchBotUser() {
        if (this.searchingUser) {
            return;
        }
        this.searchingUser = true;
        final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
        tl_contacts_resolveUsername.username = MessagesController.getInstance(this.currentAccount).venueSearchBot;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$BaseLocationAdapter$OUu2tRVUfz8Mg0cIvwT_Hlk9dBk(this));
    }
    
    public void destroy() {
        if (this.currentRequestNum != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
            this.currentRequestNum = 0;
        }
    }
    
    public void searchDelayed(final String s, final Location location) {
        if (s != null && s.length() != 0) {
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            (this.searchTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        BaseLocationAdapter.this.searchTimer.cancel();
                        BaseLocationAdapter.this.searchTimer = null;
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    AndroidUtilities.runOnUIThread(new _$$Lambda$BaseLocationAdapter$1$kUOM7MJ1viSwJRv7kesEXXY9pCs(this, s, location));
                }
            }, 200L, 500L);
        }
        else {
            this.places.clear();
            this.notifyDataSetChanged();
        }
    }
    
    public void searchPlacesWithQuery(final String lastSearchQuery, final Location lastSearchLocation, final boolean b) {
        if (lastSearchLocation != null) {
            final Location lastSearchLocation2 = this.lastSearchLocation;
            if (lastSearchLocation2 == null || lastSearchLocation.distanceTo(lastSearchLocation2) >= 200.0f) {
                this.lastSearchLocation = lastSearchLocation;
                this.lastSearchQuery = lastSearchQuery;
                if (this.searching) {
                    this.searching = false;
                    if (this.currentRequestNum != 0) {
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
                        this.currentRequestNum = 0;
                    }
                }
                this.searching = true;
                final TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(MessagesController.getInstance(this.currentAccount).venueSearchBot);
                if (!(userOrChat instanceof TLRPC.User)) {
                    if (b) {
                        this.searchBotUser();
                    }
                    return;
                }
                final TLRPC.User user = (TLRPC.User)userOrChat;
                final TLRPC.TL_messages_getInlineBotResults tl_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
                String query;
                if ((query = lastSearchQuery) == null) {
                    query = "";
                }
                tl_messages_getInlineBotResults.query = query;
                tl_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                tl_messages_getInlineBotResults.offset = "";
                tl_messages_getInlineBotResults.geo_point = new TLRPC.TL_inputGeoPoint();
                tl_messages_getInlineBotResults.geo_point.lat = AndroidUtilities.fixLocationCoord(lastSearchLocation.getLatitude());
                tl_messages_getInlineBotResults.geo_point._long = AndroidUtilities.fixLocationCoord(lastSearchLocation.getLongitude());
                tl_messages_getInlineBotResults.flags |= 0x1;
                final int n = (int)this.dialogId;
                if (n != 0) {
                    tl_messages_getInlineBotResults.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n);
                }
                else {
                    tl_messages_getInlineBotResults.peer = new TLRPC.TL_inputPeerEmpty();
                }
                this.currentRequestNum = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getInlineBotResults, new _$$Lambda$BaseLocationAdapter$qLcpONoyStiIgQpEIiVXznU9o5o(this));
                this.notifyDataSetChanged();
            }
        }
    }
    
    public void setDelegate(final long dialogId, final BaseLocationAdapterDelegate delegate) {
        this.dialogId = dialogId;
        this.delegate = delegate;
    }
    
    public interface BaseLocationAdapterDelegate
    {
        void didLoadedSearchResult(final ArrayList<TLRPC.TL_messageMediaVenue> p0);
    }
}
