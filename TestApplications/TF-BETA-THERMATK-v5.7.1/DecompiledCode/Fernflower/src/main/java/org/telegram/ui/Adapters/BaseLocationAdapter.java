package org.telegram.ui.Adapters;

import android.location.Location;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.RecyclerListView;

public abstract class BaseLocationAdapter extends RecyclerListView.SelectionAdapter {
   private int currentAccount;
   private int currentRequestNum;
   private BaseLocationAdapter.BaseLocationAdapterDelegate delegate;
   private long dialogId;
   protected ArrayList iconUrls = new ArrayList();
   private Location lastSearchLocation;
   private String lastSearchQuery;
   protected ArrayList places = new ArrayList();
   private Timer searchTimer;
   protected boolean searching;
   private boolean searchingUser;

   public BaseLocationAdapter() {
      this.currentAccount = UserConfig.selectedAccount;
   }

   private void searchBotUser() {
      if (!this.searchingUser) {
         this.searchingUser = true;
         TLRPC.TL_contacts_resolveUsername var1 = new TLRPC.TL_contacts_resolveUsername();
         var1.username = MessagesController.getInstance(this.currentAccount).venueSearchBot;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$BaseLocationAdapter$OUu2tRVUfz8Mg0cIvwT_Hlk9dBk(this));
      }
   }

   public void destroy() {
      if (this.currentRequestNum != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
         this.currentRequestNum = 0;
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$BaseLocationAdapter(TLObject var1) {
      TLRPC.TL_contacts_resolvedPeer var2 = (TLRPC.TL_contacts_resolvedPeer)var1;
      MessagesController.getInstance(this.currentAccount).putUsers(var2.users, false);
      MessagesController.getInstance(this.currentAccount).putChats(var2.chats, false);
      MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var2.users, var2.chats, true, true);
      Location var3 = this.lastSearchLocation;
      this.lastSearchLocation = null;
      this.searchPlacesWithQuery(this.lastSearchQuery, var3, false);
   }

   // $FF: synthetic method
   public void lambda$null$2$BaseLocationAdapter(TLRPC.TL_error var1, TLObject var2) {
      int var3 = 0;
      this.currentRequestNum = 0;
      this.searching = false;
      this.places.clear();
      this.iconUrls.clear();
      if (var1 != null) {
         BaseLocationAdapter.BaseLocationAdapterDelegate var7 = this.delegate;
         if (var7 != null) {
            var7.didLoadedSearchResult(this.places);
         }
      } else {
         TLRPC.messages_BotResults var8 = (TLRPC.messages_BotResults)var2;

         for(int var4 = var8.results.size(); var3 < var4; ++var3) {
            TLRPC.BotInlineResult var9 = (TLRPC.BotInlineResult)var8.results.get(var3);
            if ("venue".equals(var9.type)) {
               TLRPC.BotInlineMessage var10 = var9.send_message;
               if (var10 instanceof TLRPC.TL_botInlineMessageMediaVenue) {
                  TLRPC.TL_botInlineMessageMediaVenue var11 = (TLRPC.TL_botInlineMessageMediaVenue)var10;
                  ArrayList var5 = this.iconUrls;
                  StringBuilder var6 = new StringBuilder();
                  var6.append("https://ss3.4sqi.net/img/categories_v2/");
                  var6.append(var11.venue_type);
                  var6.append("_64.png");
                  var5.add(var6.toString());
                  TLRPC.TL_messageMediaVenue var12 = new TLRPC.TL_messageMediaVenue();
                  var12.geo = var11.geo;
                  var12.address = var11.address;
                  var12.title = var11.title;
                  var12.venue_type = var11.venue_type;
                  var12.venue_id = var11.venue_id;
                  var12.provider = var11.provider;
                  this.places.add(var12);
               }
            }
         }
      }

      this.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public void lambda$searchBotUser$1$BaseLocationAdapter(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$BaseLocationAdapter$3QgE_sikMAhKXc3W4CzFrcyGhVs(this, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$searchPlacesWithQuery$3$BaseLocationAdapter(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$BaseLocationAdapter$cZ_97u5GiOnofWCJZtoLbhQZqEs(this, var2, var1));
   }

   public void searchDelayed(final String var1, final Location var2) {
      if (var1 != null && var1.length() != 0) {
         try {
            if (this.searchTimer != null) {
               this.searchTimer.cancel();
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         this.searchTimer = new Timer();
         this.searchTimer.schedule(new TimerTask() {
            // $FF: synthetic method
            public void lambda$run$0$BaseLocationAdapter$1(String var1x, Location var2x) {
               BaseLocationAdapter.this.lastSearchLocation = null;
               BaseLocationAdapter.this.searchPlacesWithQuery(var1x, var2x, true);
            }

            public void run() {
               try {
                  BaseLocationAdapter.this.searchTimer.cancel();
                  BaseLocationAdapter.this.searchTimer = null;
               } catch (Exception var2x) {
                  FileLog.e((Throwable)var2x);
               }

               AndroidUtilities.runOnUIThread(new _$$Lambda$BaseLocationAdapter$1$kUOM7MJ1viSwJRv7kesEXXY9pCs(this, var1, var2));
            }
         }, 200L, 500L);
      } else {
         this.places.clear();
         this.notifyDataSetChanged();
      }

   }

   public void searchPlacesWithQuery(String var1, Location var2, boolean var3) {
      if (var2 != null) {
         Location var4 = this.lastSearchLocation;
         if (var4 == null || var2.distanceTo(var4) >= 200.0F) {
            this.lastSearchLocation = var2;
            this.lastSearchQuery = var1;
            if (this.searching) {
               this.searching = false;
               if (this.currentRequestNum != 0) {
                  ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
                  this.currentRequestNum = 0;
               }
            }

            this.searching = true;
            TLObject var8 = MessagesController.getInstance(this.currentAccount).getUserOrChat(MessagesController.getInstance(this.currentAccount).venueSearchBot);
            if (!(var8 instanceof TLRPC.User)) {
               if (var3) {
                  this.searchBotUser();
               }

               return;
            }

            TLRPC.User var5 = (TLRPC.User)var8;
            TLRPC.TL_messages_getInlineBotResults var6 = new TLRPC.TL_messages_getInlineBotResults();
            String var9 = var1;
            if (var1 == null) {
               var9 = "";
            }

            var6.query = var9;
            var6.bot = MessagesController.getInstance(this.currentAccount).getInputUser(var5);
            var6.offset = "";
            var6.geo_point = new TLRPC.TL_inputGeoPoint();
            var6.geo_point.lat = AndroidUtilities.fixLocationCoord(var2.getLatitude());
            var6.geo_point._long = AndroidUtilities.fixLocationCoord(var2.getLongitude());
            var6.flags |= 1;
            int var7 = (int)this.dialogId;
            if (var7 != 0) {
               var6.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var7);
            } else {
               var6.peer = new TLRPC.TL_inputPeerEmpty();
            }

            this.currentRequestNum = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$BaseLocationAdapter$qLcpONoyStiIgQpEIiVXznU9o5o(this));
            this.notifyDataSetChanged();
         }
      }

   }

   public void setDelegate(long var1, BaseLocationAdapter.BaseLocationAdapterDelegate var3) {
      this.dialogId = var1;
      this.delegate = var3;
   }

   public interface BaseLocationAdapterDelegate {
      void didLoadedSearchResult(ArrayList var1);
   }
}
