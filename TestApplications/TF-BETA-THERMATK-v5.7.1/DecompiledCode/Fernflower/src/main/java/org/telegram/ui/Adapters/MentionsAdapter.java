package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Components.RecyclerListView;

public class MentionsAdapter extends RecyclerListView.SelectionAdapter {
   private static final String punctuationsChars = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n";
   private SparseArray botInfo;
   private int botsCount;
   private Runnable cancelDelayRunnable;
   private int channelLastReqId;
   private int channelReqId;
   private boolean contextMedia;
   private int contextQueryReqid;
   private Runnable contextQueryRunnable;
   private int contextUsernameReqid;
   private int currentAccount;
   private MentionsAdapter.MentionsAdapterDelegate delegate;
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
   private SendMessagesHelper.LocationProvider locationProvider;
   private Context mContext;
   private ArrayList messages;
   private boolean needBotContext;
   private boolean needUsernames;
   private String nextQueryOffset;
   private boolean noUserName;
   private ChatActivity parentFragment;
   private int resultLength;
   private int resultStartPosition;
   private SearchAdapterHelper searchAdapterHelper;
   private Runnable searchGlobalRunnable;
   private ArrayList searchResultBotContext;
   private TLRPC.TL_inlineBotSwitchPM searchResultBotContextSwitch;
   private ArrayList searchResultCommands;
   private ArrayList searchResultCommandsHelp;
   private ArrayList searchResultCommandsUsers;
   private ArrayList searchResultHashtags;
   private ArrayList searchResultSuggestions;
   private ArrayList searchResultUsernames;
   private SparseArray searchResultUsernamesMap;
   private String searchingContextQuery;
   private String searchingContextUsername;

   public MentionsAdapter(Context var1, boolean var2, long var3, MentionsAdapter.MentionsAdapterDelegate var5) {
      this.currentAccount = UserConfig.selectedAccount;
      this.needUsernames = true;
      this.needBotContext = true;
      this.inlineMediaEnabled = true;
      this.locationProvider = new SendMessagesHelper.LocationProvider(new SendMessagesHelper.LocationProvider.LocationProviderDelegate() {
         public void onLocationAcquired(Location var1) {
            if (MentionsAdapter.this.foundContextBot != null && MentionsAdapter.this.foundContextBot.bot_inline_geo) {
               MentionsAdapter.this.lastKnownLocation = var1;
               MentionsAdapter var2 = MentionsAdapter.this;
               var2.searchForContextBotResults(true, var2.foundContextBot, MentionsAdapter.this.searchingContextQuery, "");
            }

         }

         public void onUnableLocationAcquire() {
            MentionsAdapter.this.onLocationUnavailable();
         }
      }) {
         public void stop() {
            super.stop();
            MentionsAdapter.this.lastKnownLocation = null;
         }
      };
      this.mContext = var1;
      this.delegate = var5;
      this.isDarkTheme = var2;
      this.dialog_id = var3;
      this.searchAdapterHelper = new SearchAdapterHelper(true);
      this.searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() {
         // $FF: synthetic method
         public SparseArray getExcludeUsers() {
            return SearchAdapterHelper$SearchAdapterHelperDelegate$_CC.$default$getExcludeUsers(this);
         }

         public void onDataSetChanged() {
            MentionsAdapter.this.notifyDataSetChanged();
         }

         public void onSetHashtags(ArrayList var1, HashMap var2) {
            if (MentionsAdapter.this.lastText != null) {
               MentionsAdapter var3 = MentionsAdapter.this;
               var3.searchUsernameOrHashtag(var3.lastText, MentionsAdapter.this.lastPosition, MentionsAdapter.this.messages, MentionsAdapter.this.lastUsernameOnly);
            }

         }
      });
   }

   // $FF: synthetic method
   static int access$1604(MentionsAdapter var0) {
      int var1 = var0.channelLastReqId + 1;
      var0.channelLastReqId = var1;
      return var1;
   }

   private void checkLocationPermissionsOrStart() {
      ChatActivity var1 = this.parentFragment;
      if (var1 != null && var1.getParentActivity() != null) {
         if (VERSION.SDK_INT >= 23 && this.parentFragment.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            return;
         }

         TLRPC.User var2 = this.foundContextBot;
         if (var2 != null && var2.bot_inline_geo) {
            this.locationProvider.start();
         }
      }

   }

   // $FF: synthetic method
   static int lambda$searchUsernameOrHashtag$5(SparseArray var0, ArrayList var1, TLRPC.User var2, TLRPC.User var3) {
      if (var0.indexOfKey(var2.id) >= 0 && var0.indexOfKey(var3.id) >= 0) {
         return 0;
      } else if (var0.indexOfKey(var2.id) >= 0) {
         return -1;
      } else {
         int var4 = var0.indexOfKey(var3.id);
         byte var5 = 1;
         if (var4 >= 0) {
            return 1;
         } else {
            var4 = var1.indexOf(var2.id);
            int var6 = var1.indexOf(var3.id);
            if (var4 != -1 && var6 != -1) {
               if (var4 < var6) {
                  var5 = -1;
               } else if (var4 == var6) {
                  var5 = 0;
               }

               return var5;
            } else if (var4 != -1 && var6 == -1) {
               return -1;
            } else {
               return var4 == -1 && var6 != -1 ? 1 : 0;
            }
         }
      }
   }

   private void onLocationUnavailable() {
      TLRPC.User var1 = this.foundContextBot;
      if (var1 != null && var1.bot_inline_geo) {
         this.lastKnownLocation = new Location("network");
         this.lastKnownLocation.setLatitude(-1000.0D);
         this.lastKnownLocation.setLongitude(-1000.0D);
         this.searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
      }

   }

   private void processFoundUser(TLRPC.User var1) {
      this.contextUsernameReqid = 0;
      this.locationProvider.stop();
      if (var1 != null && var1.bot && var1.bot_inline_placeholder != null) {
         this.foundContextBot = var1;
         ChatActivity var4 = this.parentFragment;
         if (var4 != null) {
            TLRPC.Chat var5 = var4.getCurrentChat();
            if (var5 != null) {
               this.inlineMediaEnabled = ChatObject.canSendStickers(var5);
               if (!this.inlineMediaEnabled) {
                  this.notifyDataSetChanged();
                  this.delegate.needChangePanelVisibility(true);
                  return;
               }
            }
         }

         if (this.foundContextBot.bot_inline_geo) {
            label35: {
               SharedPreferences var6 = MessagesController.getNotificationsSettings(this.currentAccount);
               StringBuilder var2 = new StringBuilder();
               var2.append("inlinegeo_");
               var2.append(this.foundContextBot.id);
               if (!var6.getBoolean(var2.toString(), false)) {
                  var4 = this.parentFragment;
                  if (var4 != null && var4.getParentActivity() != null) {
                     var1 = this.foundContextBot;
                     AlertDialog.Builder var7 = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                     var7.setTitle(LocaleController.getString("ShareYouLocationTitle", 2131560756));
                     var7.setMessage(LocaleController.getString("ShareYouLocationInline", 2131560755));
                     boolean[] var3 = new boolean[1];
                     var7.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$MentionsAdapter$3hBy3ePtH3hOR9uoZtzoQOcheTs(this, var3, var1));
                     var7.setNegativeButton(LocaleController.getString("Cancel", 2131558891), new _$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb_uXrprc(this, var3));
                     this.parentFragment.showDialog(var7.create(), new _$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA(this, var3));
                     break label35;
                  }
               }

               this.checkLocationPermissionsOrStart();
            }
         }
      } else {
         this.foundContextBot = null;
         this.inlineMediaEnabled = true;
      }

      if (this.foundContextBot == null) {
         this.noUserName = true;
      } else {
         MentionsAdapter.MentionsAdapterDelegate var8 = this.delegate;
         if (var8 != null) {
            var8.onContextSearch(true);
         }

         this.searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
      }

   }

   private void searchForContextBot(final String var1, final String var2) {
      TLRPC.User var3 = this.foundContextBot;
      String var6;
      if (var3 != null) {
         var6 = var3.username;
         if (var6 != null && var6.equals(var1)) {
            var6 = this.searchingContextQuery;
            if (var6 != null && var6.equals(var2)) {
               return;
            }
         }
      }

      this.searchResultBotContext = null;
      this.searchResultBotContextSwitch = null;
      this.notifyDataSetChanged();
      if (this.foundContextBot != null) {
         if (!this.inlineMediaEnabled && var1 != null && var2 != null) {
            return;
         }

         this.delegate.needChangePanelVisibility(false);
      }

      Runnable var7 = this.contextQueryRunnable;
      if (var7 != null) {
         AndroidUtilities.cancelRunOnUIThread(var7);
         this.contextQueryRunnable = null;
      }

      MentionsAdapter.MentionsAdapterDelegate var8;
      label87: {
         if (!TextUtils.isEmpty(var1)) {
            var6 = this.searchingContextUsername;
            if (var6 == null || var6.equals(var1)) {
               break label87;
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
         var8 = this.delegate;
         if (var8 != null) {
            var8.onContextSearch(false);
         }

         if (var1 == null || var1.length() == 0) {
            return;
         }
      }

      if (var2 == null) {
         if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
         }

         this.searchingContextQuery = null;
         MentionsAdapter.MentionsAdapterDelegate var5 = this.delegate;
         if (var5 != null) {
            var5.onContextSearch(false);
         }

      } else {
         var8 = this.delegate;
         if (var8 != null) {
            if (this.foundContextBot != null) {
               var8.onContextSearch(true);
            } else if (var1.equals("gif")) {
               this.searchingContextUsername = "gif";
               this.delegate.onContextSearch(false);
            }
         }

         final MessagesController var4 = MessagesController.getInstance(this.currentAccount);
         final MessagesStorage var9 = MessagesStorage.getInstance(this.currentAccount);
         this.searchingContextQuery = var2;
         this.contextQueryRunnable = new Runnable() {
            // $FF: synthetic method
            public void lambda$null$0$MentionsAdapter$4(String var1x, TLRPC.TL_error var2x, TLObject var3, MessagesController var4x, MessagesStorage var5) {
               if (MentionsAdapter.this.searchingContextUsername != null && MentionsAdapter.this.searchingContextUsername.equals(var1x)) {
                  Object var6 = null;
                  TLRPC.User var7 = (TLRPC.User)var6;
                  if (var2x == null) {
                     TLRPC.TL_contacts_resolvedPeer var8 = (TLRPC.TL_contacts_resolvedPeer)var3;
                     var7 = (TLRPC.User)var6;
                     if (!var8.users.isEmpty()) {
                        var7 = (TLRPC.User)var8.users.get(0);
                        var4x.putUser(var7, false);
                        var5.putUsersAndChats(var8.users, (ArrayList)null, true, true);
                     }
                  }

                  MentionsAdapter.this.processFoundUser(var7);
               }

            }

            // $FF: synthetic method
            public void lambda$run$1$MentionsAdapter$4(String var1x, MessagesController var2x, MessagesStorage var3, TLObject var4x, TLRPC.TL_error var5) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$MentionsAdapter$4$ie2Kd71f_OJP1CoMCJ5ZnpnLA2g(this, var1x, var5, var4x, var2x, var3));
            }

            public void run() {
               if (MentionsAdapter.this.contextQueryRunnable == this) {
                  MentionsAdapter.this.contextQueryRunnable = null;
                  MentionsAdapter var1x;
                  if (MentionsAdapter.this.foundContextBot == null && !MentionsAdapter.this.noUserName) {
                     MentionsAdapter.this.searchingContextUsername = var1;
                     TLObject var3 = var4.getUserOrChat(MentionsAdapter.this.searchingContextUsername);
                     if (var3 instanceof TLRPC.User) {
                        MentionsAdapter.this.processFoundUser((TLRPC.User)var3);
                     } else {
                        TLRPC.TL_contacts_resolveUsername var2x = new TLRPC.TL_contacts_resolveUsername();
                        var2x.username = MentionsAdapter.this.searchingContextUsername;
                        var1x = MentionsAdapter.this;
                        var1x.contextUsernameReqid = ConnectionsManager.getInstance(var1x.currentAccount).sendRequest(var2x, new _$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7_SrPstmceOE6w(this, var1, var4, var9));
                     }
                  } else {
                     if (MentionsAdapter.this.noUserName) {
                        return;
                     }

                     var1x = MentionsAdapter.this;
                     var1x.searchForContextBotResults(true, var1x.foundContextBot, var2, "");
                  }

               }
            }
         };
         AndroidUtilities.runOnUIThread(this.contextQueryRunnable, 400L);
      }
   }

   private void searchForContextBotResults(boolean var1, TLRPC.User var2, String var3, String var4) {
      if (this.contextQueryReqid != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
         this.contextQueryReqid = 0;
      }

      if (!this.inlineMediaEnabled) {
         MentionsAdapter.MentionsAdapterDelegate var10 = this.delegate;
         if (var10 != null) {
            var10.onContextSearch(false);
         }

      } else if (var3 != null && var2 != null) {
         if (!var2.bot_inline_geo || this.lastKnownLocation != null) {
            StringBuilder var5;
            Object var13;
            label50: {
               var5 = new StringBuilder();
               var5.append(this.dialog_id);
               var5.append("_");
               var5.append(var3);
               var5.append("_");
               var5.append(var4);
               var5.append("_");
               var5.append(this.dialog_id);
               var5.append("_");
               var5.append(var2.id);
               var5.append("_");
               if (var2.bot_inline_geo) {
                  Location var6 = this.lastKnownLocation;
                  if (var6 != null && var6.getLatitude() != -1000.0D) {
                     var13 = this.lastKnownLocation.getLatitude() + this.lastKnownLocation.getLongitude();
                     break label50;
                  }
               }

               var13 = "";
            }

            var5.append(var13);
            String var7 = var5.toString();
            MessagesStorage var11 = MessagesStorage.getInstance(this.currentAccount);
            _$$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U var14 = new _$$Lambda$MentionsAdapter$5habXq1r64920QdCkG9j6xMgM4U(this, var3, var1, var2, var4, var11, var7);
            if (var1) {
               var11.getBotCache(var7, var14);
            } else {
               TLRPC.TL_messages_getInlineBotResults var12 = new TLRPC.TL_messages_getInlineBotResults();
               var12.bot = MessagesController.getInstance(this.currentAccount).getInputUser(var2);
               var12.query = var3;
               var12.offset = var4;
               if (var2.bot_inline_geo) {
                  Location var9 = this.lastKnownLocation;
                  if (var9 != null && var9.getLatitude() != -1000.0D) {
                     var12.flags |= 1;
                     var12.geo_point = new TLRPC.TL_inputGeoPoint();
                     var12.geo_point.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
                     var12.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
                  }
               }

               int var8 = (int)this.dialog_id;
               if (var8 != 0) {
                  var12.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var8);
               } else {
                  var12.peer = new TLRPC.TL_inputPeerEmpty();
               }

               this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, var14, 2);
            }

         }
      } else {
         this.searchingContextQuery = null;
      }
   }

   private void showUsersResult(ArrayList var1, SparseArray var2, boolean var3) {
      this.searchResultUsernames = var1;
      this.searchResultUsernamesMap = var2;
      Runnable var4 = this.cancelDelayRunnable;
      if (var4 != null) {
         AndroidUtilities.cancelRunOnUIThread(var4);
         this.cancelDelayRunnable = null;
      }

      if (var3) {
         this.notifyDataSetChanged();
         this.delegate.needChangePanelVisibility(this.searchResultUsernames.isEmpty() ^ true);
      }

   }

   public void addHashtagsFromMessage(CharSequence var1) {
      this.searchAdapterHelper.addHashtagsFromMessage(var1);
   }

   public void clearRecentHashtags() {
      this.searchAdapterHelper.clearRecentHashtags();
      this.searchResultHashtags.clear();
      this.notifyDataSetChanged();
      MentionsAdapter.MentionsAdapterDelegate var1 = this.delegate;
      if (var1 != null) {
         var1.needChangePanelVisibility(false);
      }

   }

   public String getBotCaption() {
      TLRPC.User var1 = this.foundContextBot;
      if (var1 != null) {
         return var1.bot_inline_placeholder;
      } else {
         String var2 = this.searchingContextUsername;
         return var2 != null && var2.equals("gif") ? "Search GIFs" : null;
      }
   }

   public TLRPC.TL_inlineBotSwitchPM getBotContextSwitch() {
      return this.searchResultBotContextSwitch;
   }

   public int getContextBotId() {
      TLRPC.User var1 = this.foundContextBot;
      int var2;
      if (var1 != null) {
         var2 = var1.id;
      } else {
         var2 = 0;
      }

      return var2;
   }

   public String getContextBotName() {
      TLRPC.User var1 = this.foundContextBot;
      String var2;
      if (var1 != null) {
         var2 = var1.username;
      } else {
         var2 = "";
      }

      return var2;
   }

   public TLRPC.User getContextBotUser() {
      return this.foundContextBot;
   }

   public Object getItem(int var1) {
      if (this.searchResultBotContext != null) {
         TLRPC.TL_inlineBotSwitchPM var6 = this.searchResultBotContextSwitch;
         int var3 = var1;
         if (var6 != null) {
            if (var1 == 0) {
               return var6;
            }

            var3 = var1 - 1;
         }

         return var3 >= 0 && var3 < this.searchResultBotContext.size() ? this.searchResultBotContext.get(var3) : null;
      } else {
         ArrayList var2 = this.searchResultUsernames;
         if (var2 != null) {
            return var1 >= 0 && var1 < var2.size() ? this.searchResultUsernames.get(var1) : null;
         } else {
            var2 = this.searchResultHashtags;
            if (var2 != null) {
               return var1 >= 0 && var1 < var2.size() ? this.searchResultHashtags.get(var1) : null;
            } else {
               var2 = this.searchResultSuggestions;
               if (var2 != null) {
                  return var1 >= 0 && var1 < var2.size() ? this.searchResultSuggestions.get(var1) : null;
               } else {
                  var2 = this.searchResultCommands;
                  if (var2 != null && var1 >= 0 && var1 < var2.size()) {
                     if (this.searchResultCommandsUsers == null || this.botsCount == 1 && !(this.info instanceof TLRPC.TL_channelFull)) {
                        return this.searchResultCommands.get(var1);
                     } else if (this.searchResultCommandsUsers.get(var1) != null) {
                        Object var4 = this.searchResultCommands.get(var1);
                        String var5;
                        if (this.searchResultCommandsUsers.get(var1) != null) {
                           var5 = ((TLRPC.User)this.searchResultCommandsUsers.get(var1)).username;
                        } else {
                           var5 = "";
                        }

                        return String.format("%s@%s", var4, var5);
                     } else {
                        return String.format("%s", this.searchResultCommands.get(var1));
                     }
                  } else {
                     return null;
                  }
               }
            }
         }
      }
   }

   public int getItemCount() {
      TLRPC.User var1 = this.foundContextBot;
      byte var2 = 1;
      if (var1 != null && !this.inlineMediaEnabled) {
         return 1;
      } else {
         ArrayList var4 = this.searchResultBotContext;
         if (var4 != null) {
            int var3 = var4.size();
            if (this.searchResultBotContextSwitch == null) {
               var2 = 0;
            }

            return var3 + var2;
         } else {
            var4 = this.searchResultUsernames;
            if (var4 != null) {
               return var4.size();
            } else {
               var4 = this.searchResultHashtags;
               if (var4 != null) {
                  return var4.size();
               } else {
                  var4 = this.searchResultCommands;
                  if (var4 != null) {
                     return var4.size();
                  } else {
                     var4 = this.searchResultSuggestions;
                     return var4 != null ? var4.size() : 0;
                  }
               }
            }
         }
      }
   }

   public int getItemPosition(int var1) {
      int var2 = var1;
      if (this.searchResultBotContext != null) {
         var2 = var1;
         if (this.searchResultBotContextSwitch != null) {
            var2 = var1 - 1;
         }
      }

      return var2;
   }

   public int getItemViewType(int var1) {
      if (this.foundContextBot != null && !this.inlineMediaEnabled) {
         return 3;
      } else if (this.searchResultBotContext != null) {
         return var1 == 0 && this.searchResultBotContextSwitch != null ? 2 : 1;
      } else {
         return 0;
      }
   }

   public int getResultLength() {
      return this.resultLength;
   }

   public int getResultStartPosition() {
      return this.resultStartPosition;
   }

   public ArrayList getSearchResultBotContext() {
      return this.searchResultBotContext;
   }

   public boolean isBannedInline() {
      boolean var1;
      if (this.foundContextBot != null && !this.inlineMediaEnabled) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isBotCommands() {
      boolean var1;
      if (this.searchResultCommands != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isBotContext() {
      boolean var1;
      if (this.searchResultBotContext != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      boolean var2;
      if (this.foundContextBot != null && !this.inlineMediaEnabled) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isLongClickEnabled() {
      boolean var1;
      if (this.searchResultHashtags == null && this.searchResultCommands == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isMediaLayout() {
      return this.contextMedia;
   }

   // $FF: synthetic method
   public void lambda$null$3$MentionsAdapter(String var1, boolean var2, TLObject var3, TLRPC.User var4, String var5, MessagesStorage var6, String var7) {
      if (var1.equals(this.searchingContextQuery)) {
         this.contextQueryReqid = 0;
         MentionsAdapter.MentionsAdapterDelegate var11;
         if (var2 && var3 == null) {
            this.searchForContextBotResults(false, var4, var1, var5);
         } else {
            var11 = this.delegate;
            if (var11 != null) {
               var11.onContextSearch(false);
            }
         }

         if (var3 instanceof TLRPC.TL_messages_botResults) {
            TLRPC.TL_messages_botResults var12 = (TLRPC.TL_messages_botResults)var3;
            if (!var2 && var12.cache_time != 0) {
               var6.saveBotCache(var7, var12);
            }

            this.nextQueryOffset = var12.next_offset;
            if (this.searchResultBotContextSwitch == null) {
               this.searchResultBotContextSwitch = var12.switch_pm;
            }

            int var8 = 0;

            while(true) {
               int var9 = var12.results.size();
               boolean var10 = true;
               if (var8 >= var9) {
                  boolean var15;
                  if (this.searchResultBotContext != null && var5.length() != 0) {
                     this.searchResultBotContext.addAll(var12.results);
                     if (var12.results.isEmpty()) {
                        this.nextQueryOffset = "";
                     }

                     var15 = true;
                  } else {
                     this.searchResultBotContext = var12.results;
                     this.contextMedia = var12.gallery;
                     var15 = false;
                  }

                  Runnable var14 = this.cancelDelayRunnable;
                  if (var14 != null) {
                     AndroidUtilities.cancelRunOnUIThread(var14);
                     this.cancelDelayRunnable = null;
                  }

                  this.searchResultHashtags = null;
                  this.searchResultUsernames = null;
                  this.searchResultUsernamesMap = null;
                  this.searchResultCommands = null;
                  this.searchResultSuggestions = null;
                  this.searchResultCommandsHelp = null;
                  this.searchResultCommandsUsers = null;
                  if (var15) {
                     byte var16;
                     if (this.searchResultBotContextSwitch != null) {
                        var16 = 1;
                     } else {
                        var16 = 0;
                     }

                     this.notifyItemChanged(this.searchResultBotContext.size() - var12.results.size() + var16 - 1);
                     this.notifyItemRangeInserted(this.searchResultBotContext.size() - var12.results.size() + var16, var12.results.size());
                  } else {
                     this.notifyDataSetChanged();
                  }

                  var11 = this.delegate;
                  var2 = var10;
                  if (this.searchResultBotContext.isEmpty()) {
                     if (this.searchResultBotContextSwitch != null) {
                        var2 = var10;
                     } else {
                        var2 = false;
                     }
                  }

                  var11.needChangePanelVisibility(var2);
                  break;
               }

               TLRPC.BotInlineResult var13 = (TLRPC.BotInlineResult)var12.results.get(var8);
               var9 = var8;
               if (!(var13.document instanceof TLRPC.TL_document)) {
                  var9 = var8;
                  if (!(var13.photo instanceof TLRPC.TL_photo)) {
                     var9 = var8;
                     if (!"game".equals(var13.type)) {
                        var9 = var8;
                        if (var13.content == null) {
                           var9 = var8;
                           if (var13.send_message instanceof TLRPC.TL_botInlineMessageMediaAuto) {
                              var12.results.remove(var8);
                              var9 = var8 - 1;
                           }
                        }
                     }
                  }
               }

               var13.query_id = var12.query_id;
               var8 = var9 + 1;
            }
         }

      }
   }

   // $FF: synthetic method
   public void lambda$onCreateViewHolder$8$MentionsAdapter(ContextLinkCell var1) {
      this.delegate.onContextClick(var1.getResult());
   }

   // $FF: synthetic method
   public void lambda$processFoundUser$0$MentionsAdapter(boolean[] var1, TLRPC.User var2, DialogInterface var3, int var4) {
      var1[0] = true;
      if (var2 != null) {
         Editor var5 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
         StringBuilder var6 = new StringBuilder();
         var6.append("inlinegeo_");
         var6.append(var2.id);
         var5.putBoolean(var6.toString(), true).commit();
         this.checkLocationPermissionsOrStart();
      }

   }

   // $FF: synthetic method
   public void lambda$processFoundUser$1$MentionsAdapter(boolean[] var1, DialogInterface var2, int var3) {
      var1[0] = true;
      this.onLocationUnavailable();
   }

   // $FF: synthetic method
   public void lambda$processFoundUser$2$MentionsAdapter(boolean[] var1, DialogInterface var2) {
      if (!var1[0]) {
         this.onLocationUnavailable();
      }

   }

   // $FF: synthetic method
   public void lambda$searchForContextBotResults$4$MentionsAdapter(String var1, boolean var2, TLRPC.User var3, String var4, MessagesStorage var5, String var6, TLObject var7, TLRPC.TL_error var8) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MentionsAdapter$HVDPv79fAM1RehGsWWysAxv_W1Q(this, var1, var2, var7, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$searchUsernameOrHashtag$6$MentionsAdapter(ArrayList var1, SparseArray var2) {
      this.cancelDelayRunnable = null;
      this.showUsersResult(var1, var2, true);
   }

   // $FF: synthetic method
   public void lambda$searchUsernameOrHashtag$7$MentionsAdapter(ArrayList var1, String var2) {
      this.searchResultSuggestions = var1;
      this.searchResultHashtags = null;
      this.searchResultUsernames = null;
      this.searchResultUsernamesMap = null;
      this.searchResultCommands = null;
      this.searchResultCommandsHelp = null;
      this.searchResultCommandsUsers = null;
      this.notifyDataSetChanged();
      MentionsAdapter.MentionsAdapterDelegate var4 = this.delegate;
      ArrayList var5 = this.searchResultSuggestions;
      boolean var3;
      if (var5 != null && !var5.isEmpty()) {
         var3 = true;
      } else {
         var3 = false;
      }

      var4.needChangePanelVisibility(var3);
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      int var3 = var1.getItemViewType();
      boolean var4 = false;
      if (var3 == 3) {
         TextView var12 = (TextView)var1.itemView;
         TLRPC.Chat var5 = this.parentFragment.getCurrentChat();
         if (var5 != null) {
            if (!ChatObject.hasAdminRights(var5)) {
               TLRPC.TL_chatBannedRights var6 = var5.default_banned_rights;
               if (var6 != null && var6.send_inline) {
                  var12.setText(LocaleController.getString("GlobalAttachInlineRestricted", 2131559591));
                  return;
               }
            }

            if (AndroidUtilities.isBannedForever(var5.banned_rights)) {
               var12.setText(LocaleController.getString("AttachInlineRestrictedForever", 2131558720));
            } else {
               var12.setText(LocaleController.formatString("AttachInlineRestricted", 2131558719, LocaleController.formatDateForBan((long)var5.banned_rights.until_date)));
            }
         }
      } else if (this.searchResultBotContext != null) {
         boolean var16;
         if (this.searchResultBotContextSwitch != null) {
            var16 = true;
         } else {
            var16 = false;
         }

         if (var1.getItemViewType() == 2) {
            if (var16) {
               ((BotSwitchCell)var1.itemView).setText(this.searchResultBotContextSwitch.text);
            }
         } else {
            int var7 = var2;
            if (var16) {
               var7 = var2 - 1;
            }

            ContextLinkCell var13 = (ContextLinkCell)var1.itemView;
            TLRPC.BotInlineResult var17 = (TLRPC.BotInlineResult)this.searchResultBotContext.get(var7);
            boolean var8 = this.contextMedia;
            boolean var9;
            if (var7 != this.searchResultBotContext.size() - 1) {
               var9 = true;
            } else {
               var9 = false;
            }

            boolean var10 = var4;
            if (var16) {
               var10 = var4;
               if (var7 == 0) {
                  var10 = true;
               }
            }

            var13.setLink(var17, var8, var9, var10);
         }
      } else {
         ArrayList var18 = this.searchResultUsernames;
         if (var18 != null) {
            ((MentionCell)var1.itemView).setUser((TLRPC.User)var18.get(var2));
         } else {
            var18 = this.searchResultHashtags;
            if (var18 != null) {
               ((MentionCell)var1.itemView).setText((String)var18.get(var2));
            } else {
               var18 = this.searchResultSuggestions;
               if (var18 != null) {
                  ((MentionCell)var1.itemView).setEmojiSuggestion((DataQuery.KeywordResult)var18.get(var2));
               } else {
                  ArrayList var19 = this.searchResultCommands;
                  if (var19 != null) {
                     MentionCell var20 = (MentionCell)var1.itemView;
                     String var21 = (String)var19.get(var2);
                     String var11 = (String)this.searchResultCommandsHelp.get(var2);
                     ArrayList var14 = this.searchResultCommandsUsers;
                     TLRPC.User var15;
                     if (var14 != null) {
                        var15 = (TLRPC.User)var14.get(var2);
                     } else {
                        var15 = null;
                     }

                     var20.setBotCommand(var21, var11, var15);
                  }
               }
            }
         }
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var3;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               var3 = new TextView(this.mContext);
               ((TextView)var3).setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F));
               ((TextView)var3).setTextSize(1, 14.0F);
               ((TextView)var3).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            } else {
               var3 = new BotSwitchCell(this.mContext);
            }
         } else {
            var3 = new ContextLinkCell(this.mContext);
            ((ContextLinkCell)var3).setDelegate(new _$$Lambda$MentionsAdapter$VoCNDYNNW_mhCQ_lScxh9_TCNkI(this));
         }
      } else {
         var3 = new MentionCell(this.mContext);
         ((MentionCell)var3).setIsDarkTheme(this.isDarkTheme);
      }

      return new RecyclerListView.Holder((View)var3);
   }

   public void onDestroy() {
      SendMessagesHelper.LocationProvider var1 = this.locationProvider;
      if (var1 != null) {
         var1.stop();
      }

      Runnable var2 = this.contextQueryRunnable;
      if (var2 != null) {
         AndroidUtilities.cancelRunOnUIThread(var2);
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

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 2) {
         TLRPC.User var4 = this.foundContextBot;
         if (var4 != null && var4.bot_inline_geo) {
            if (var3.length > 0 && var3[0] == 0) {
               this.locationProvider.start();
            } else {
               this.onLocationUnavailable();
            }
         }
      }

   }

   public void searchForContextBotForNextOffset() {
      if (this.contextQueryReqid == 0) {
         String var1 = this.nextQueryOffset;
         if (var1 != null && var1.length() != 0) {
            TLRPC.User var2 = this.foundContextBot;
            if (var2 != null) {
               var1 = this.searchingContextQuery;
               if (var1 != null) {
                  this.searchForContextBotResults(true, var2, var1, this.nextQueryOffset);
               }
            }
         }
      }

   }

   public void searchUsernameOrHashtag(String var1, int var2, ArrayList var3, boolean var4) {
      Runnable var5 = this.cancelDelayRunnable;
      if (var5 != null) {
         AndroidUtilities.cancelRunOnUIThread(var5);
         this.cancelDelayRunnable = null;
      }

      if (this.channelReqId != 0) {
         ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.channelReqId, true);
         this.channelReqId = 0;
      }

      var5 = this.searchGlobalRunnable;
      if (var5 != null) {
         AndroidUtilities.cancelRunOnUIThread(var5);
         this.searchGlobalRunnable = null;
      }

      if (TextUtils.isEmpty(var1)) {
         this.searchForContextBot((String)null, (String)null);
         this.delegate.needChangePanelVisibility(false);
         this.lastText = null;
      } else {
         int var6;
         if (var1.length() > 0) {
            var6 = var2 - 1;
         } else {
            var6 = var2;
         }

         this.lastText = null;
         this.lastUsernameOnly = var4;
         StringBuilder var7 = new StringBuilder();
         int var8;
         int var9;
         final String var12;
         String var25;
         if (!var4 && this.needBotContext && var1.charAt(0) == '@') {
            var8 = var1.indexOf(32);
            var9 = var1.length();
            String var10 = "";
            String var11;
            if (var8 > 0) {
               var25 = var1.substring(1, var8);
               var11 = var1.substring(var8 + 1);
            } else if (var1.charAt(var9 - 1) == 't' && var1.charAt(var9 - 2) == 'o' && var1.charAt(var9 - 3) == 'b') {
               var25 = var1.substring(1);
               var11 = "";
            } else {
               this.searchForContextBot((String)null, (String)null);
               var11 = null;
               var25 = var11;
            }

            var12 = var10;
            if (var25 != null) {
               var12 = var10;
               if (var25.length() >= 1) {
                  var8 = 1;

                  while(true) {
                     if (var8 >= var25.length()) {
                        var12 = var25;
                        break;
                     }

                     char var29 = var25.charAt(var8);
                     if ((var29 < '0' || var29 > '9') && (var29 < 'a' || var29 > 'z') && (var29 < 'A' || var29 > 'Z') && var29 != '_') {
                        var12 = var10;
                        break;
                     }

                     ++var8;
                  }
               }
            }

            this.searchForContextBot(var12, var11);
         } else {
            this.searchForContextBot((String)null, (String)null);
         }

         if (this.foundContextBot == null) {
            byte var18;
            final MessagesController var26;
            boolean var30;
            label443: {
               var26 = MessagesController.getInstance(this.currentAccount);
               if (var4) {
                  var7.append(var1.substring(1));
                  this.resultStartPosition = 0;
                  this.resultLength = var7.length();
                  var18 = 0;
               } else {
                  while(true) {
                     if (var6 < 0) {
                        var18 = -1;
                        break;
                     }

                     if (var6 < var1.length()) {
                        char var13;
                        label346: {
                           var13 = var1.charAt(var6);
                           if (var6 != 0) {
                              var8 = var6 - 1;
                              if (var1.charAt(var8) != ' ' && var1.charAt(var8) != '\n') {
                                 break label346;
                              }
                           }

                           if (var13 == '@') {
                              if (this.needUsernames || this.needBotContext && var6 == 0) {
                                 if (this.info == null && var6 != 0) {
                                    this.lastText = var1;
                                    this.lastPosition = var2;
                                    this.messages = var3;
                                    this.delegate.needChangePanelVisibility(false);
                                    return;
                                 }

                                 this.resultStartPosition = var6;
                                 this.resultLength = var7.length() + 1;
                                 var18 = 0;
                                 break label443;
                              }
                           } else {
                              if (var13 == '#') {
                                 if (!this.searchAdapterHelper.loadRecentHashtags()) {
                                    this.lastText = var1;
                                    this.lastPosition = var2;
                                    this.messages = var3;
                                    this.delegate.needChangePanelVisibility(false);
                                    return;
                                 }

                                 this.resultStartPosition = var6;
                                 this.resultLength = var7.length() + 1;
                                 var7.insert(0, var13);
                                 var18 = 1;
                                 break;
                              }

                              if (var6 == 0 && this.botInfo != null && var13 == '/') {
                                 this.resultStartPosition = var6;
                                 this.resultLength = var7.length() + 1;
                                 var18 = 2;
                                 break;
                              }

                              if (var13 == ':' && var7.length() > 0) {
                                 if (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(var7.charAt(0)) >= 0) {
                                    var30 = true;
                                 } else {
                                    var30 = false;
                                 }

                                 if (!var30 || var7.length() > 1) {
                                    this.resultStartPosition = var6;
                                    this.resultLength = var7.length() + 1;
                                    var18 = 3;
                                    break;
                                 }
                              }
                           }
                        }

                        var7.insert(0, var13);
                     }

                     --var6;
                  }
               }

               var6 = -1;
            }

            if (var18 == -1) {
               this.delegate.needChangePanelVisibility(false);
            } else {
               ArrayList var33;
               if (var18 == 0) {
                  var33 = new ArrayList();

                  for(var2 = 0; var2 < Math.min(100, var3.size()); ++var2) {
                     var8 = ((MessageObject)var3.get(var2)).messageOwner.from_id;
                     if (!var33.contains(var8)) {
                        var33.add(var8);
                     }
                  }

                  var12 = var7.toString().toLowerCase();
                  if (var12.indexOf(32) >= 0) {
                     var30 = true;
                  } else {
                     var30 = false;
                  }

                  final ArrayList var31 = new ArrayList();
                  SparseArray var27 = new SparseArray();
                  final SparseArray var20 = new SparseArray();
                  ArrayList var14 = DataQuery.getInstance(this.currentAccount).inlineBots;
                  TLRPC.User var15;
                  if (!var4 && this.needBotContext && var6 == 0 && !var14.isEmpty()) {
                     var9 = 0;

                     for(var6 = 0; var9 < var14.size(); ++var9) {
                        var15 = var26.getUser(((TLRPC.TL_topPeer)var14.get(var9)).peer.user_id);
                        if (var15 != null) {
                           var1 = var15.username;
                           var2 = var6;
                           if (var1 != null) {
                              var2 = var6;
                              if (var1.length() > 0) {
                                 label455: {
                                    if (var12.length() <= 0 || !var15.username.toLowerCase().startsWith(var12)) {
                                       var2 = var6;
                                       if (var12.length() != 0) {
                                          break label455;
                                       }
                                    }

                                    var31.add(var15);
                                    var27.put(var15.id, var15);
                                    var2 = var6 + 1;
                                 }
                              }
                           }

                           var6 = var2;
                           if (var2 == 5) {
                              break;
                           }
                        }
                     }
                  }

                  ChatActivity var16 = this.parentFragment;
                  final TLRPC.Chat var17;
                  if (var16 != null) {
                     var17 = var16.getCurrentChat();
                  } else {
                     TLRPC.ChatFull var19 = this.info;
                     if (var19 != null) {
                        var17 = var26.getChat(var19.id);
                     } else {
                        var17 = null;
                     }
                  }

                  if (var17 != null) {
                     TLRPC.ChatFull var36 = this.info;
                     if (var36 != null && var36.participants != null && (!ChatObject.isChannel(var17) || var17.megagroup)) {
                        for(var2 = 0; var2 < this.info.participants.participants.size(); ++var2) {
                           var15 = var26.getUser(((TLRPC.ChatParticipant)this.info.participants.participants.get(var2)).user_id);
                           if (var15 != null && (var4 || !UserObject.isUserSelf(var15)) && var27.indexOfKey(var15.id) < 0) {
                              if (var12.length() == 0) {
                                 if (!var15.deleted) {
                                    var31.add(var15);
                                 }
                              } else {
                                 String var35 = var15.username;
                                 if (var35 != null && var35.length() > 0 && var15.username.toLowerCase().startsWith(var12)) {
                                    var31.add(var15);
                                    var20.put(var15.id, var15);
                                 } else {
                                    var35 = var15.first_name;
                                    if (var35 != null && var35.length() > 0 && var15.first_name.toLowerCase().startsWith(var12)) {
                                       var31.add(var15);
                                       var20.put(var15.id, var15);
                                    } else {
                                       var35 = var15.last_name;
                                       if (var35 != null && var35.length() > 0 && var15.last_name.toLowerCase().startsWith(var12)) {
                                          var31.add(var15);
                                          var20.put(var15.id, var15);
                                       } else if (var30 && ContactsController.formatName(var15.first_name, var15.last_name).toLowerCase().startsWith(var12)) {
                                          var31.add(var15);
                                          var20.put(var15.id, var15);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  Collections.sort(var31, new _$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY(var20, var33));
                  this.searchResultHashtags = null;
                  this.searchResultCommands = null;
                  this.searchResultCommandsHelp = null;
                  this.searchResultCommandsUsers = null;
                  this.searchResultSuggestions = null;
                  if (var17 != null && var17.megagroup && var12.length() > 0) {
                     if (var31.size() < 5) {
                        _$$Lambda$MentionsAdapter$_MTgLbVZU0vjX841sg9FU0IKm0g var34 = new _$$Lambda$MentionsAdapter$_MTgLbVZU0vjX841sg9FU0IKm0g(this, var31, var20);
                        this.cancelDelayRunnable = var34;
                        AndroidUtilities.runOnUIThread(var34, 1000L);
                     } else {
                        this.showUsersResult(var31, var20, true);
                     }

                     Runnable var22 = new Runnable() {
                        // $FF: synthetic method
                        public void lambda$null$0$MentionsAdapter$5(int var1, ArrayList var2, SparseArray var3, TLRPC.TL_error var4, TLObject var5, MessagesController var6) {
                           if (MentionsAdapter.this.channelReqId != 0 && var1 == MentionsAdapter.this.channelLastReqId && MentionsAdapter.this.searchResultUsernamesMap != null && MentionsAdapter.this.searchResultUsernames != null) {
                              MentionsAdapter.this.showUsersResult(var2, var3, false);
                              if (var4 == null) {
                                 TLRPC.TL_channels_channelParticipants var8 = (TLRPC.TL_channels_channelParticipants)var5;
                                 var6.putUsers(var8.users, false);
                                 MentionsAdapter.this.searchResultUsernames.isEmpty();
                                 if (!var8.participants.isEmpty()) {
                                    int var7 = UserConfig.getInstance(MentionsAdapter.this.currentAccount).getClientUserId();

                                    for(var1 = 0; var1 < var8.participants.size(); ++var1) {
                                       TLRPC.ChannelParticipant var9 = (TLRPC.ChannelParticipant)var8.participants.get(var1);
                                       if (MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(var9.user_id) < 0 && (MentionsAdapter.this.isSearchingMentions || var9.user_id != var7)) {
                                          TLRPC.User var10 = var6.getUser(var9.user_id);
                                          if (var10 == null) {
                                             return;
                                          }

                                          MentionsAdapter.this.searchResultUsernames.add(var10);
                                       }
                                    }
                                 }
                              }

                              MentionsAdapter.this.notifyDataSetChanged();
                              MentionsAdapter.this.delegate.needChangePanelVisibility(MentionsAdapter.this.searchResultUsernames.isEmpty() ^ true);
                           }

                           MentionsAdapter.this.channelReqId = 0;
                        }

                        // $FF: synthetic method
                        public void lambda$run$1$MentionsAdapter$5(int var1, ArrayList var2, SparseArray var3, MessagesController var4, TLObject var5, TLRPC.TL_error var6) {
                           AndroidUtilities.runOnUIThread(new _$$Lambda$MentionsAdapter$5$evj2cSH7pPT2A8KUwmVh6dcOrN4(this, var1, var2, var3, var6, var5, var4));
                        }

                        public void run() {
                           if (MentionsAdapter.this.searchGlobalRunnable == this) {
                              TLRPC.TL_channels_getParticipants var1 = new TLRPC.TL_channels_getParticipants();
                              var1.channel = MessagesController.getInputChannel(var17);
                              var1.limit = 20;
                              var1.offset = 0;
                              TLRPC.TL_channelParticipantsSearch var2 = new TLRPC.TL_channelParticipantsSearch();
                              var2.q = var12;
                              var1.filter = var2;
                              int var3 = MentionsAdapter.access$1604(MentionsAdapter.this);
                              MentionsAdapter var4 = MentionsAdapter.this;
                              var4.channelReqId = ConnectionsManager.getInstance(var4.currentAccount).sendRequest(var1, new _$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0(this, var3, var31, var20, var26));
                           }
                        }
                     };
                     this.searchGlobalRunnable = var22;
                     AndroidUtilities.runOnUIThread(var22, 200L);
                  } else {
                     this.showUsersResult(var31, var20, true);
                  }
               } else {
                  ArrayList var23;
                  if (var18 == 1) {
                     var23 = new ArrayList();
                     var25 = var7.toString().toLowerCase();
                     var33 = this.searchAdapterHelper.getHashtags();

                     for(var2 = 0; var2 < var33.size(); ++var2) {
                        SearchAdapterHelper.HashtagObject var21 = (SearchAdapterHelper.HashtagObject)var33.get(var2);
                        if (var21 != null) {
                           var12 = var21.hashtag;
                           if (var12 != null && var12.startsWith(var25)) {
                              var23.add(var21.hashtag);
                           }
                        }
                     }

                     this.searchResultHashtags = var23;
                     this.searchResultUsernames = null;
                     this.searchResultUsernamesMap = null;
                     this.searchResultCommands = null;
                     this.searchResultCommandsHelp = null;
                     this.searchResultCommandsUsers = null;
                     this.searchResultSuggestions = null;
                     this.notifyDataSetChanged();
                     this.delegate.needChangePanelVisibility(var23.isEmpty() ^ true);
                  } else if (var18 == 2) {
                     var23 = new ArrayList();
                     var3 = new ArrayList();
                     var33 = new ArrayList();
                     var12 = var7.toString().toLowerCase();

                     for(var2 = 0; var2 < this.botInfo.size(); ++var2) {
                        TLRPC.BotInfo var32 = (TLRPC.BotInfo)this.botInfo.valueAt(var2);

                        for(var6 = 0; var6 < var32.commands.size(); ++var6) {
                           TLRPC.TL_botCommand var28 = (TLRPC.TL_botCommand)var32.commands.get(var6);
                           if (var28 != null) {
                              String var37 = var28.command;
                              if (var37 != null && var37.startsWith(var12)) {
                                 StringBuilder var38 = new StringBuilder();
                                 var38.append("/");
                                 var38.append(var28.command);
                                 var23.add(var38.toString());
                                 var3.add(var28.description);
                                 var33.add(var26.getUser(var32.user_id));
                              }
                           }
                        }
                     }

                     this.searchResultHashtags = null;
                     this.searchResultUsernames = null;
                     this.searchResultUsernamesMap = null;
                     this.searchResultSuggestions = null;
                     this.searchResultCommands = var23;
                     this.searchResultCommandsHelp = var3;
                     this.searchResultCommandsUsers = var33;
                     this.notifyDataSetChanged();
                     this.delegate.needChangePanelVisibility(var23.isEmpty() ^ true);
                  } else if (var18 == 3) {
                     String[] var24 = AndroidUtilities.getCurrentKeyboardLanguage();
                     if (!Arrays.equals(var24, this.lastSearchKeyboardLanguage)) {
                        DataQuery.getInstance(this.currentAccount).fetchNewEmojiKeywords(var24);
                     }

                     this.lastSearchKeyboardLanguage = var24;
                     DataQuery.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, var7.toString(), false, new _$$Lambda$MentionsAdapter$rScwFUSTmUPNK4byYGdy0eHPN2M(this));
                  }
               }

            }
         }
      }
   }

   public void setBotInfo(SparseArray var1) {
      this.botInfo = var1;
   }

   public void setBotsCount(int var1) {
      this.botsCount = var1;
   }

   public void setChatInfo(TLRPC.ChatFull var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.info = var1;
      if (!this.inlineMediaEnabled && this.foundContextBot != null) {
         ChatActivity var2 = this.parentFragment;
         if (var2 != null) {
            TLRPC.Chat var3 = var2.getCurrentChat();
            if (var3 != null) {
               this.inlineMediaEnabled = ChatObject.canSendStickers(var3);
               if (this.inlineMediaEnabled) {
                  this.searchResultUsernames = null;
                  this.notifyDataSetChanged();
                  this.delegate.needChangePanelVisibility(false);
                  this.processFoundUser(this.foundContextBot);
               }
            }
         }
      }

      String var4 = this.lastText;
      if (var4 != null) {
         this.searchUsernameOrHashtag(var4, this.lastPosition, this.messages, this.lastUsernameOnly);
      }

   }

   public void setNeedBotContext(boolean var1) {
      this.needBotContext = var1;
   }

   public void setNeedUsernames(boolean var1) {
      this.needUsernames = var1;
   }

   public void setParentFragment(ChatActivity var1) {
      this.parentFragment = var1;
   }

   public void setSearchingMentions(boolean var1) {
      this.isSearchingMentions = var1;
   }

   public interface MentionsAdapterDelegate {
      void needChangePanelVisibility(boolean var1);

      void onContextClick(TLRPC.BotInlineResult var1);

      void onContextSearch(boolean var1);
   }
}
