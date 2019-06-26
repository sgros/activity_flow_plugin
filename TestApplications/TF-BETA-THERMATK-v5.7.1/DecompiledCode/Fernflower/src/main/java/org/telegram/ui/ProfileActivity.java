package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Property;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AboutLinkCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.IdenticonDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.voip.VoIPHelper;

public class ProfileActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
   private static final int add_contact = 1;
   private static final int add_member = 18;
   private static final int add_shortcut = 14;
   private static final int block_contact = 2;
   private static final int call_item = 15;
   private static final int delete_contact = 5;
   private static final int edit_channel = 12;
   private static final int edit_contact = 4;
   private static final int invite_to_group = 9;
   private static final int leave_group = 7;
   private static final int search_members = 17;
   private static final int share = 10;
   private static final int share_contact = 3;
   private static final int statistics = 19;
   private int addMemberRow;
   private int administratorsRow;
   private boolean allowProfileAnimation = true;
   private ActionBarMenuItem animatingItem;
   private float animationProgress;
   private int audioRow;
   private AvatarDrawable avatarDrawable;
   private BackupImageView avatarImage;
   private int banFromGroup;
   private int blockedUsersRow;
   private TLRPC.BotInfo botInfo;
   private ActionBarMenuItem callItem;
   private int channelInfoRow;
   private TLRPC.ChatFull chatInfo;
   private int chat_id;
   private boolean creatingChat;
   private TLRPC.ChannelParticipant currentChannelParticipant;
   private TLRPC.Chat currentChat;
   private TLRPC.EncryptedChat currentEncryptedChat;
   private long dialog_id;
   private ActionBarMenuItem editItem;
   private int emptyRow;
   private int extraHeight;
   private int filesRow;
   private int groupsInCommonRow;
   private int infoHeaderRow;
   private int infoSectionRow;
   private int initialAnimationExtraHeight;
   private boolean isBot;
   private boolean[] isOnline = new boolean[1];
   private int joinRow;
   private int[] lastMediaCount = new int[]{-1, -1, -1, -1, -1};
   private int lastSectionRow;
   private LinearLayoutManager layoutManager;
   private int leaveChannelRow;
   private int linksRow;
   private ProfileActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loadingUsers;
   private MediaActivity mediaActivity;
   private int[] mediaCount = new int[]{-1, -1, -1, -1, -1};
   private int[] mediaMergeCount = new int[]{-1, -1, -1, -1, -1};
   private int membersEndRow;
   private int membersHeaderRow;
   private int membersSectionRow;
   private int membersStartRow;
   private long mergeDialogId;
   private SimpleTextView[] nameTextView = new SimpleTextView[2];
   private int notificationsDividerRow;
   private int notificationsRow;
   private int onlineCount = -1;
   private SimpleTextView[] onlineTextView = new SimpleTextView[2];
   private boolean openAnimationInProgress;
   private SparseArray participantsMap = new SparseArray();
   private int phoneRow;
   private int photosRow;
   private boolean playProfileAnimation;
   private int[] prevMediaCount = new int[]{-1, -1, -1, -1, -1};
   private PhotoViewer.PhotoViewerProvider provider = new PhotoViewer.EmptyPhotoViewerProvider() {
      public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
         if (var2 == null) {
            return null;
         } else {
            TLRPC.FileLocation var8;
            label50: {
               if (ProfileActivity.this.user_id != 0) {
                  TLRPC.User var6 = MessagesController.getInstance(ProfileActivity.access$100(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                  if (var6 != null) {
                     TLRPC.UserProfilePhoto var7 = var6.photo;
                     if (var7 != null) {
                        var8 = var7.photo_big;
                        if (var8 != null) {
                           break label50;
                        }
                     }
                  }
               } else if (ProfileActivity.this.chat_id != 0) {
                  TLRPC.Chat var9 = MessagesController.getInstance(ProfileActivity.access$300(ProfileActivity.this)).getChat(ProfileActivity.this.chat_id);
                  if (var9 != null) {
                     TLRPC.ChatPhoto var11 = var9.photo;
                     if (var11 != null) {
                        var8 = var11.photo_big;
                        if (var8 != null) {
                           break label50;
                        }
                     }
                  }
               }

               var8 = null;
            }

            if (var8 != null && var8.local_id == var2.local_id && var8.volume_id == var2.volume_id && var8.dc_id == var2.dc_id) {
               int[] var10 = new int[2];
               ProfileActivity.this.avatarImage.getLocationInWindow(var10);
               PhotoViewer.PlaceProviderObject var12 = new PhotoViewer.PlaceProviderObject();
               var3 = 0;
               var12.viewX = var10[0];
               int var5 = var10[1];
               if (VERSION.SDK_INT < 21) {
                  var3 = AndroidUtilities.statusBarHeight;
               }

               var12.viewY = var5 - var3;
               var12.parentView = ProfileActivity.this.avatarImage;
               var12.imageReceiver = ProfileActivity.this.avatarImage.getImageReceiver();
               if (ProfileActivity.this.user_id != 0) {
                  var12.dialogId = ProfileActivity.this.user_id;
               } else if (ProfileActivity.this.chat_id != 0) {
                  var12.dialogId = -ProfileActivity.this.chat_id;
               }

               var12.thumb = var12.imageReceiver.getBitmapSafe();
               var12.size = -1;
               var12.radius = ProfileActivity.this.avatarImage.getImageReceiver().getRoundRadius();
               var12.scale = ProfileActivity.this.avatarImage.getScaleX();
               return var12;
            } else {
               return null;
            }
         }
      }

      public void willHidePhotoViewer() {
         ProfileActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
      }
   };
   private boolean recreateMenuAfterAnimation;
   private int rowCount;
   private ScamDrawable scamDrawable;
   private int selectedUser;
   private int settingsKeyRow;
   private int settingsSectionRow;
   private int settingsTimerRow;
   private int sharedHeaderRow;
   private MediaActivity.SharedMediaData[] sharedMediaData;
   private int sharedSectionRow;
   private ArrayList sortedUsers;
   private int startSecretChatRow;
   private int subscribersRow;
   private ProfileActivity.TopView topView;
   private int unblockRow;
   private boolean userBlocked;
   private TLRPC.UserFull userInfo;
   private int userInfoRow;
   private int user_id;
   private int usernameRow;
   private boolean usersEndReached;
   private int voiceRow;
   private ImageView writeButton;
   private AnimatorSet writeButtonAnimation;

   public ProfileActivity(Bundle var1) {
      super(var1);
   }

   // $FF: synthetic method
   static int access$100(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1200(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1300(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1400(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1500(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1600(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1700(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2000(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2100(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2800(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$300(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3000(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3100(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3500(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3600(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3700(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3800(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$500(ProfileActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static View access$5000(ProfileActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$5200(ProfileActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static ActionBar access$5500(ProfileActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$5600(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBarLayout access$5700(ProfileActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$5800(ProfileActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$5900(ProfileActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$6700(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$6900(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBarLayout access$700(ProfileActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$7000(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$7100(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBarLayout access$800(ProfileActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$8200(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$8600(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$9300(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$9400(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$9500(ProfileActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$9900(ProfileActivity var0) {
      return var0.currentAccount;
   }

   private void checkListViewScroll() {
      if (this.listView.getChildCount() > 0 && !this.openAnimationInProgress) {
         RecyclerListView var1 = this.listView;
         boolean var2 = false;
         View var3 = var1.getChildAt(0);
         RecyclerListView.Holder var5 = (RecyclerListView.Holder)this.listView.findContainingViewHolder(var3);
         int var4 = var3.getTop();
         if (var4 < 0 || var5 == null || var5.getAdapterPosition() != 0) {
            var4 = 0;
         }

         if (this.extraHeight != var4) {
            this.extraHeight = var4;
            this.topView.invalidate();
            if (this.playProfileAnimation) {
               if (this.extraHeight != 0) {
                  var2 = true;
               }

               this.allowProfileAnimation = var2;
            }

            this.needLayout();
         }
      }

   }

   private void createActionBarMenu() {
      ActionBarMenu var1 = super.actionBar.createMenu();
      var1.clearItems();
      TLRPC.UserFull var2 = null;
      ActionBarMenuItem var3 = null;
      this.animatingItem = null;
      int var6;
      ActionBarMenuItem var9;
      ActionBarMenuItem var10;
      if (this.user_id != 0) {
         if (UserConfig.getInstance(super.currentAccount).getClientUserId() != this.user_id) {
            TLRPC.User var4 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            if (var4 == null) {
               return;
            }

            var2 = this.userInfo;
            if (var2 != null && var2.phone_calls_available) {
               this.callItem = var1.addItem(15, 2131165429);
            }

            boolean var5;
            String var11;
            if (!this.isBot && ContactsController.getInstance(super.currentAccount).contactsDict.get(this.user_id) != null) {
               var9 = var1.addItem(10, 2131165416);
               var9.addSubItem(3, 2131165670, LocaleController.getString("ShareContact", 2131560747));
               var5 = this.userBlocked;
               if (!this.userBlocked) {
                  var11 = LocaleController.getString("BlockContact", 2131558833);
               } else {
                  var11 = LocaleController.getString("Unblock", 2131560932);
               }

               var9.addSubItem(2, 2131165614, var11);
               var9.addSubItem(4, 2131165625, LocaleController.getString("EditContact", 2131559322));
               var9.addSubItem(5, 2131165623, LocaleController.getString("DeleteContact", 2131559241));
               var10 = var9;
            } else {
               var9 = var1.addItem(10, 2131165416);
               if (MessagesController.isSupportUser(var4)) {
                  var10 = var9;
                  if (this.userBlocked) {
                     var9.addSubItem(2, 2131165614, LocaleController.getString("Unblock", 2131560932));
                     var10 = var9;
                  }
               } else {
                  if (this.isBot) {
                     if (!var4.bot_nochats) {
                        var9.addSubItem(9, 2131165611, LocaleController.getString("BotInvite", 2131558852));
                     }

                     var9.addSubItem(10, 2131165670, LocaleController.getString("BotShare", 2131558856));
                  }

                  var11 = var4.phone;
                  if (var11 != null && var11.length() != 0) {
                     var9.addSubItem(1, 2131165612, LocaleController.getString("AddContact", 2131558567));
                     var9.addSubItem(3, 2131165670, LocaleController.getString("ShareContact", 2131560747));
                     var5 = this.userBlocked;
                     if (!this.userBlocked) {
                        var11 = LocaleController.getString("BlockContact", 2131558833);
                     } else {
                        var11 = LocaleController.getString("Unblock", 2131560932);
                     }

                     var9.addSubItem(2, 2131165614, var11);
                     var10 = var9;
                  } else if (this.isBot) {
                     if (!this.userBlocked) {
                        var6 = 2131165614;
                     } else {
                        var6 = 2131165661;
                     }

                     int var7;
                     if (!this.userBlocked) {
                        var7 = 2131558860;
                        var11 = "BotStop";
                     } else {
                        var7 = 2131558854;
                        var11 = "BotRestart";
                     }

                     var9.addSubItem(2, var6, LocaleController.getString(var11, var7));
                     var10 = var9;
                  } else {
                     var5 = this.userBlocked;
                     if (!this.userBlocked) {
                        var11 = LocaleController.getString("BlockContact", 2131558833);
                     } else {
                        var11 = LocaleController.getString("Unblock", 2131560932);
                     }

                     var9.addSubItem(2, 2131165614, var11);
                     var10 = var9;
                  }
               }
            }
         } else {
            var10 = var1.addItem(10, 2131165416);
            var10.addSubItem(3, 2131165670, LocaleController.getString("ShareContact", 2131560747));
         }
      } else {
         var6 = this.chat_id;
         var10 = var2;
         if (var6 != 0) {
            var10 = var2;
            if (var6 > 0) {
               TLRPC.Chat var8 = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
               if (ChatObject.isChannel(var8)) {
                  if (ChatObject.hasAdminRights(var8) || var8.megagroup && ChatObject.canChangeChatInfo(var8)) {
                     this.editItem = var1.addItem(12, 2131165404);
                  }

                  var9 = var3;
                  if (!var8.megagroup) {
                     TLRPC.ChatFull var12 = this.chatInfo;
                     var9 = var3;
                     if (var12 != null) {
                        var9 = var3;
                        if (var12.can_view_stats) {
                           var9 = var1.addItem(10, 2131165416);
                           var9.addSubItem(19, 2131165672, LocaleController.getString("Statistics", 2131560806));
                        }
                     }
                  }

                  var10 = var9;
                  if (var8.megagroup) {
                     var3 = var9;
                     if (var9 == null) {
                        var3 = var1.addItem(10, 2131165416);
                     }

                     var3.addSubItem(17, 2131165669, LocaleController.getString("SearchMembers", 2131560654));
                     var10 = var3;
                     if (!var8.creator) {
                        var10 = var3;
                        if (!var8.left) {
                           var10 = var3;
                           if (!var8.kicked) {
                              var3.addSubItem(7, 2131165639, LocaleController.getString("LeaveMegaMenu", 2131559746));
                              var10 = var3;
                           }
                        }
                     }
                  }
               } else {
                  if (ChatObject.canChangeChatInfo(var8)) {
                     this.editItem = var1.addItem(12, 2131165404);
                  }

                  var10 = var1.addItem(10, 2131165416);
                  var10.addSubItem(17, 2131165669, LocaleController.getString("SearchMembers", 2131560654));
                  var10.addSubItem(7, 2131165639, LocaleController.getString("DeleteAndExit", 2131559234));
               }
            }
         }
      }

      var9 = var10;
      if (var10 == null) {
         var9 = var1.addItem(10, 2131165416);
      }

      var9.addSubItem(14, 2131165633, LocaleController.getString("AddShortcut", 2131558583));
      var9.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
      var10 = this.editItem;
      if (var10 != null) {
         var10.setContentDescription(LocaleController.getString("Edit", 2131559301));
      }

      var10 = this.callItem;
      if (var10 != null) {
         var10.setContentDescription(LocaleController.getString("Call", 2131558869));
      }

   }

   private void fetchUsersFromChannelInfo() {
      TLRPC.Chat var1 = this.currentChat;
      if (var1 != null && var1.megagroup) {
         TLRPC.ChatFull var3 = this.chatInfo;
         if (var3 instanceof TLRPC.TL_channelFull && var3.participants != null) {
            for(int var2 = 0; var2 < this.chatInfo.participants.participants.size(); ++var2) {
               TLRPC.ChatParticipant var4 = (TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var2);
               this.participantsMap.put(var4.user_id, var4);
            }
         }
      }

   }

   private void fixLayout() {
      View var1 = super.fragmentView;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               if (ProfileActivity.access$5000(ProfileActivity.this) != null) {
                  ProfileActivity.this.checkListViewScroll();
                  ProfileActivity.this.needLayout();
                  ProfileActivity.access$5200(ProfileActivity.this).getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }
   }

   private void getChannelParticipants(boolean var1) {
      if (!this.loadingUsers) {
         SparseArray var2 = this.participantsMap;
         if (var2 != null && this.chatInfo != null) {
            this.loadingUsers = true;
            int var3 = var2.size();
            int var4 = 0;
            short var6;
            if (var3 != 0 && var1) {
               var6 = 300;
            } else {
               var6 = 0;
            }

            TLRPC.TL_channels_getParticipants var5 = new TLRPC.TL_channels_getParticipants();
            var5.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chat_id);
            var5.filter = new TLRPC.TL_channelParticipantsRecent();
            if (!var1) {
               var4 = this.participantsMap.size();
            }

            var5.offset = var4;
            var5.limit = 200;
            var3 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new _$$Lambda$ProfileActivity$0ctq_HMwIlt8ZVK21U6qfPQkrgk(this, var5, var6));
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var3, super.classGuid);
         }
      }

   }

   private Drawable getScamDrawable() {
      if (this.scamDrawable == null) {
         this.scamDrawable = new ScamDrawable(11);
         ScamDrawable var1 = this.scamDrawable;
         int var2;
         if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
            var2 = 5;
         } else {
            var2 = this.chat_id;
         }

         var1.setColor(AvatarDrawable.getProfileTextColorForId(var2));
      }

      return this.scamDrawable;
   }

   private void kickUser(int var1) {
      if (var1 != 0) {
         MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chat_id, MessagesController.getInstance(super.currentAccount).getUser(var1), this.chatInfo);
      } else {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
         if (AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, -((long)this.chat_id));
         } else {
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
         }

         MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chat_id, MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId()), this.chatInfo);
         this.playProfileAnimation = false;
         this.finishFragment();
      }

   }

   private void leaveChatPressed() {
      AlertsCreator.createClearOrDeleteDialogAlert(this, false, this.currentChat, (TLRPC.User)null, false, new _$$Lambda$ProfileActivity$_PR1FK7y_zwmejCbxKdZpGR_CRE(this));
   }

   private void loadMediaCounts() {
      if (this.dialog_id != 0L) {
         DataQuery.getInstance(super.currentAccount).getMediaCounts(this.dialog_id, super.classGuid);
      } else if (this.user_id != 0) {
         DataQuery.getInstance(super.currentAccount).getMediaCounts((long)this.user_id, super.classGuid);
      } else if (this.chat_id > 0) {
         DataQuery.getInstance(super.currentAccount).getMediaCounts((long)(-this.chat_id), super.classGuid);
         if (this.mergeDialogId != 0L) {
            DataQuery.getInstance(super.currentAccount).getMediaCounts(this.mergeDialogId, super.classGuid);
         }
      }

   }

   private void needLayout() {
      int var1;
      if (super.actionBar.getOccupyStatusBar()) {
         var1 = AndroidUtilities.statusBarHeight;
      } else {
         var1 = 0;
      }

      var1 += ActionBar.getCurrentActionBarHeight();
      RecyclerListView var2 = this.listView;
      LayoutParams var13;
      if (var2 != null && !this.openAnimationInProgress) {
         var13 = (LayoutParams)var2.getLayoutParams();
         if (var13.topMargin != var1) {
            var13.topMargin = var1;
            this.listView.setLayoutParams(var13);
         }
      }

      if (this.avatarImage != null) {
         float var3 = (float)this.extraHeight / (float)AndroidUtilities.dp(88.0F);
         this.listView.setTopGlowOffset(this.extraHeight);
         ImageView var14 = this.writeButton;
         if (var14 != null) {
            if (super.actionBar.getOccupyStatusBar()) {
               var1 = AndroidUtilities.statusBarHeight;
            } else {
               var1 = 0;
            }

            var14.setTranslationY((float)(var1 + ActionBar.getCurrentActionBarHeight() + this.extraHeight - AndroidUtilities.dp(29.5F)));
            if (!this.openAnimationInProgress) {
               boolean var17;
               if (var3 > 0.2F) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               boolean var4;
               if (this.writeButton.getTag() == null) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var17 != var4) {
                  if (var17) {
                     this.writeButton.setTag((Object)null);
                  } else {
                     this.writeButton.setTag(0);
                  }

                  AnimatorSet var15 = this.writeButtonAnimation;
                  if (var15 != null) {
                     this.writeButtonAnimation = null;
                     var15.cancel();
                  }

                  this.writeButtonAnimation = new AnimatorSet();
                  if (var17) {
                     this.writeButtonAnimation.setInterpolator(new DecelerateInterpolator());
                     this.writeButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, new float[]{1.0F}), ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, new float[]{1.0F}), ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, new float[]{1.0F})});
                  } else {
                     this.writeButtonAnimation.setInterpolator(new AccelerateInterpolator());
                     this.writeButtonAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, new float[]{0.2F}), ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, new float[]{0.2F}), ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, new float[]{0.0F})});
                  }

                  this.writeButtonAnimation.setDuration(150L);
                  this.writeButtonAnimation.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        if (ProfileActivity.this.writeButtonAnimation != null && ProfileActivity.this.writeButtonAnimation.equals(var1)) {
                           ProfileActivity.this.writeButtonAnimation = null;
                        }

                     }
                  });
                  this.writeButtonAnimation.start();
               }
            }
         }

         if (super.actionBar.getOccupyStatusBar()) {
            var1 = AndroidUtilities.statusBarHeight;
         } else {
            var1 = 0;
         }

         float var5 = (float)var1;
         float var6 = (float)ActionBar.getCurrentActionBarHeight() / 2.0F;
         float var7 = AndroidUtilities.density;
         BackupImageView var16 = this.avatarImage;
         float var8 = (18.0F * var3 + 42.0F) / 42.0F;
         var16.setScaleX(var8);
         this.avatarImage.setScaleY(var8);
         this.avatarImage.setTranslationX((float)(-AndroidUtilities.dp(47.0F)) * var3);
         var16 = this.avatarImage;
         double var9 = (double)(var5 + var6 * (var3 + 1.0F) - 21.0F * var7 + var7 * 27.0F * var3);
         var16.setTranslationY((float)Math.ceil(var9));

         for(var1 = 0; var1 < 2; ++var1) {
            SimpleTextView[] var18 = this.nameTextView;
            if (var18[var1] != null) {
               var18[var1].setTranslationX(AndroidUtilities.density * -21.0F * var3);
               this.nameTextView[var1].setTranslationY((float)Math.floor(var9) + (float)AndroidUtilities.dp(1.3F) + (float)AndroidUtilities.dp(7.0F) * var3);
               this.onlineTextView[var1].setTranslationX(AndroidUtilities.density * -21.0F * var3);
               this.onlineTextView[var1].setTranslationY((float)Math.floor(var9) + (float)AndroidUtilities.dp(24.0F) + (float)Math.floor((double)(AndroidUtilities.density * 11.0F)) * var3);
               var7 = 0.12F * var3 + 1.0F;
               this.nameTextView[var1].setScaleX(var7);
               this.nameTextView[var1].setScaleY(var7);
               if (var1 == 1 && !this.openAnimationInProgress) {
                  int var19;
                  if (AndroidUtilities.isTablet()) {
                     var19 = AndroidUtilities.dp(490.0F);
                  } else {
                     var19 = AndroidUtilities.displaySize.x;
                  }

                  byte var11;
                  if (this.callItem == null && this.editItem == null) {
                     var11 = 0;
                  } else {
                     var11 = 48;
                  }

                  int var20 = AndroidUtilities.dp((float)(126 + 40 + var11));
                  var8 = (float)var19;
                  var5 = (float)var20;
                  if (var3 != 1.0F) {
                     var6 = 0.15F * var3 / (1.0F - var3);
                  } else {
                     var6 = 1.0F;
                  }

                  int var12 = (int)(var8 - var5 * Math.max(0.0F, 1.0F - var6) - this.nameTextView[var1].getTranslationX());
                  var5 = this.nameTextView[var1].getPaint().measureText(this.nameTextView[var1].getText().toString()) * var7 + (float)this.nameTextView[var1].getSideDrawablesSize();
                  var13 = (LayoutParams)this.nameTextView[var1].getLayoutParams();
                  var6 = (float)var12;
                  if (var6 < var5) {
                     var13.width = Math.max(var19 - var20, (int)Math.ceil((double)((float)(var12 - AndroidUtilities.dp(24.0F)) / ((1.12F - var7) * 7.0F + var7))));
                  } else {
                     var13.width = (int)Math.ceil((double)var5);
                  }

                  var13.width = (int)Math.min((var8 - this.nameTextView[var1].getX()) / var7 - (float)AndroidUtilities.dp(8.0F), (float)var13.width);
                  this.nameTextView[var1].setLayoutParams(var13);
                  var8 = this.onlineTextView[var1].getPaint().measureText(this.onlineTextView[var1].getText().toString());
                  var13 = (LayoutParams)this.onlineTextView[var1].getLayoutParams();
                  var13.rightMargin = (int)Math.ceil((double)(this.onlineTextView[var1].getTranslationX() + (float)AndroidUtilities.dp(8.0F) + (float)AndroidUtilities.dp(40.0F) * (1.0F - var3)));
                  if (var6 < var8) {
                     var13.width = (int)Math.ceil((double)var12);
                  } else {
                     var13.width = -2;
                  }

                  this.onlineTextView[var1].setLayoutParams(var13);
               }
            }
         }
      }

   }

   private void openAddMember() {
      Bundle var1 = new Bundle();
      var1.putBoolean("addToGroup", true);
      var1.putInt("chatId", this.currentChat.id);
      GroupCreateActivity var4 = new GroupCreateActivity(var1);
      TLRPC.ChatFull var2 = this.chatInfo;
      if (var2 != null && var2.participants != null) {
         SparseArray var5 = new SparseArray();

         for(int var3 = 0; var3 < this.chatInfo.participants.participants.size(); ++var3) {
            var5.put(((TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var3)).user_id, (Object)null);
         }

         var4.setIgnoreUsers(var5);
      }

      var4.setDelegate((GroupCreateActivity.ContactsAddActivityDelegate)(new _$$Lambda$ProfileActivity$00s_Q5Ms7AV0v3XsAsD6561decA(this)));
      this.presentFragment(var4);
   }

   private void openRightsEdit(int var1, int var2, TLRPC.ChatParticipant var3, TLRPC.TL_chatAdminRights var4, TLRPC.TL_chatBannedRights var5) {
      ChatRightsEditActivity var6 = new ChatRightsEditActivity(var2, this.chat_id, var4, this.currentChat.default_banned_rights, var5, var1, true, false);
      var6.setDelegate(new _$$Lambda$ProfileActivity$QONFZUnsCsoEQ085dztQiYr_zvw(this, var1, var3));
      this.presentFragment(var6);
   }

   private boolean processOnClickOrPress(int var1) {
      String var3;
      String var7;
      TLRPC.User var8;
      AlertDialog.Builder var9;
      if (var1 != this.usernameRow) {
         if (var1 == this.phoneRow) {
            var8 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            if (var8 != null) {
               var3 = var8.phone;
               if (var3 != null && var3.length() != 0 && this.getParentActivity() != null) {
                  AlertDialog.Builder var12 = new AlertDialog.Builder(this.getParentActivity());
                  ArrayList var13 = new ArrayList();
                  ArrayList var5 = new ArrayList();
                  TLRPC.UserFull var6 = this.userInfo;
                  if (var6 != null && var6.phone_calls_available) {
                     var13.add(LocaleController.getString("CallViaTelegram", 2131558886));
                     var5.add(2);
                  }

                  var13.add(LocaleController.getString("Call", 2131558869));
                  var5.add(0);
                  var13.add(LocaleController.getString("Copy", 2131559163));
                  var5.add(1);
                  var12.setItems((CharSequence[])var13.toArray(new CharSequence[0]), new _$$Lambda$ProfileActivity$5IM4wVI_k9G6IDitB8PxuFm_zIw(this, var5, var8));
                  this.showDialog(var12.create());
                  return true;
               }
            }

            return false;
         } else if (var1 != this.channelInfoRow && var1 != this.userInfoRow) {
            return false;
         } else {
            var9 = new AlertDialog.Builder(this.getParentActivity());
            var7 = LocaleController.getString("Copy", 2131559163);
            _$$Lambda$ProfileActivity$_RDBSC_Boyp_tFgUU9wFSccPLw8 var11 = new _$$Lambda$ProfileActivity$_RDBSC_Boyp_tFgUU9wFSccPLw8(this, var1);
            var9.setItems(new CharSequence[]{var7}, var11);
            this.showDialog(var9.create());
            return true;
         }
      } else {
         if (this.user_id != 0) {
            var8 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            if (var8 == null) {
               return false;
            }

            var3 = var8.username;
            var7 = var3;
            if (var3 == null) {
               return false;
            }
         } else {
            label65: {
               if (this.chat_id != 0) {
                  TLRPC.Chat var2 = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
                  if (var2 != null) {
                     var3 = var2.username;
                     var7 = var3;
                     if (var3 != null) {
                        break label65;
                     }
                  }
               }

               return false;
            }
         }

         var9 = new AlertDialog.Builder(this.getParentActivity());
         String var4 = LocaleController.getString("Copy", 2131559163);
         _$$Lambda$ProfileActivity$Bn6iwTJ0OB3g1V6Tv9pADNC222E var10 = new _$$Lambda$ProfileActivity$Bn6iwTJ0OB3g1V6Tv9pADNC222E(this, var7);
         var9.setItems(new CharSequence[]{var4}, var10);
         this.showDialog(var9.create());
         return true;
      }
   }

   private void updateOnlineCount() {
      byte var1 = 0;
      this.onlineCount = 0;
      int var2 = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime();
      this.sortedUsers.clear();
      TLRPC.ChatFull var3 = this.chatInfo;
      int var4 = var1;
      if (!(var3 instanceof TLRPC.TL_chatFull)) {
         if (!(var3 instanceof TLRPC.TL_channelFull) || var3.participants_count > 200 || var3.participants == null) {
            var3 = this.chatInfo;
            if (var3 instanceof TLRPC.TL_channelFull && var3.participants_count > 200) {
               this.onlineCount = var3.online_count;
            }

            return;
         }

         var4 = var1;
      }

      while(var4 < this.chatInfo.participants.participants.size()) {
         TLRPC.ChatParticipant var7 = (TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var4);
         TLRPC.User var8 = MessagesController.getInstance(super.currentAccount).getUser(var7.user_id);
         if (var8 != null) {
            TLRPC.UserStatus var5 = var8.status;
            if (var5 != null && (var5.expires > var2 || var8.id == UserConfig.getInstance(super.currentAccount).getClientUserId()) && var8.status.expires > 10000) {
               ++this.onlineCount;
            }
         }

         this.sortedUsers.add(var4);
         ++var4;
      }

      try {
         ArrayList var11 = this.sortedUsers;
         _$$Lambda$ProfileActivity$teGXnC_jUHvrMNZVWwzA3YIyd9o var9 = new _$$Lambda$ProfileActivity$teGXnC_jUHvrMNZVWwzA3YIyd9o(this, var2);
         Collections.sort(var11, var9);
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      ProfileActivity.ListAdapter var10 = this.listAdapter;
      if (var10 != null) {
         var4 = this.membersStartRow;
         if (var4 > 0) {
            var10.notifyItemRangeChanged(var4, this.sortedUsers.size());
         }
      }

   }

   private void updateProfileData() {
      if (this.avatarImage != null && this.nameTextView != null) {
         int var1 = ConnectionsManager.getInstance(super.currentAccount).getConnectionState();
         String var2;
         if (var1 == 2) {
            var2 = LocaleController.getString("WaitingForNetwork", 2131561102);
         } else if (var1 == 1) {
            var2 = LocaleController.getString("Connecting", 2131559137);
         } else if (var1 == 5) {
            var2 = LocaleController.getString("Updating", 2131560962);
         } else if (var1 == 4) {
            var2 = LocaleController.getString("ConnectingToProxy", 2131559139);
         } else {
            var2 = null;
         }

         String var7;
         String var14;
         if (this.user_id != 0) {
            TLRPC.User var3 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            TLRPC.UserProfilePhoto var4 = var3.photo;
            TLRPC.FileLocation var5;
            if (var4 != null) {
               var5 = var4.photo_big;
            } else {
               var5 = null;
            }

            this.avatarDrawable.setInfo(var3);
            this.avatarImage.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
            FileLoader.getInstance(super.currentAccount).loadFile(ImageLocation.getForUser(var3, true), var3, (String)null, 0, 1);
            String var6 = UserObject.getUserName(var3);
            if (var3.id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
               var14 = LocaleController.getString("ChatYourSelf", 2131559045);
               var7 = LocaleController.getString("ChatYourSelfName", 2131559050);
            } else {
               var1 = var3.id;
               if (var1 != 333000 && var1 != 777000 && var1 != 42777) {
                  if (MessagesController.isSupportUser(var3)) {
                     var14 = LocaleController.getString("SupportStatus", 2131560848);
                     var7 = var6;
                  } else if (this.isBot) {
                     var14 = LocaleController.getString("Bot", 2131558848);
                     var7 = var6;
                  } else {
                     boolean[] var15 = this.isOnline;
                     var15[0] = false;
                     String var8 = LocaleController.formatUserStatus(super.currentAccount, var3, var15);
                     var7 = var6;
                     var14 = var8;
                     if (this.onlineTextView[1] != null) {
                        if (this.isOnline[0]) {
                           var14 = "profile_status";
                        } else {
                           var14 = "avatar_subtitleInProfileBlue";
                        }

                        this.onlineTextView[1].setTag(var14);
                        this.onlineTextView[1].setTextColor(Theme.getColor(var14));
                        var7 = var6;
                        var14 = var8;
                     }
                  }
               } else {
                  var14 = LocaleController.getString("ServiceNotifications", 2131560724);
                  var7 = var6;
               }
            }

            for(var1 = 0; var1 < 2; ++var1) {
               if (this.nameTextView[var1] != null) {
                  label337: {
                     if (var1 == 0 && var3.id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                        int var9 = var3.id;
                        if (var9 / 1000 != 777 && var9 / 1000 != 333) {
                           var6 = var3.phone;
                           if (var6 != null && var6.length() != 0 && ContactsController.getInstance(super.currentAccount).contactsDict.get(var3.id) == null && (ContactsController.getInstance(super.currentAccount).contactsDict.size() != 0 || !ContactsController.getInstance(super.currentAccount).isLoadingContacts())) {
                              PhoneFormat var24 = PhoneFormat.getInstance();
                              StringBuilder var18 = new StringBuilder();
                              var18.append("+");
                              var18.append(var3.phone);
                              var6 = var24.format(var18.toString());
                              if (!this.nameTextView[var1].getText().equals(var6)) {
                                 this.nameTextView[var1].setText(var6);
                              }
                              break label337;
                           }
                        }
                     }

                     if (!this.nameTextView[var1].getText().equals(var7)) {
                        this.nameTextView[var1].setText(var7);
                     }
                  }

                  if (var1 == 0 && var2 != null) {
                     this.onlineTextView[var1].setText(var2);
                  } else if (!this.onlineTextView[var1].getText().equals(var14)) {
                     this.onlineTextView[var1].setText(var14);
                  }

                  Drawable var26;
                  if (this.currentEncryptedChat != null) {
                     var26 = Theme.chat_lockIconDrawable;
                  } else {
                     var26 = null;
                  }

                  Object var21;
                  label262: {
                     if (var1 == 0) {
                        if (var3.scam) {
                           var21 = this.getScamDrawable();
                           break label262;
                        }

                        MessagesController var20 = MessagesController.getInstance(super.currentAccount);
                        long var10 = this.dialog_id;
                        if (var10 == 0L) {
                           var10 = (long)this.user_id;
                        }

                        if (var20.isDialogMuted(var10)) {
                           var21 = Theme.chat_muteIconDrawable;
                           break label262;
                        }
                     } else {
                        if (var3.scam) {
                           var21 = this.getScamDrawable();
                           break label262;
                        }

                        if (var3.verified) {
                           var21 = new CombinedDrawable(Theme.profile_verifiedDrawable, Theme.profile_verifiedCheckDrawable);
                           break label262;
                        }
                     }

                     var21 = null;
                  }

                  this.nameTextView[var1].setLeftDrawable(var26);
                  this.nameTextView[var1].setRightDrawable((Drawable)var21);
               }
            }

            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(var5) ^ true, false);
         } else if (this.chat_id != 0) {
            TLRPC.Chat var30 = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
            if (var30 != null) {
               this.currentChat = var30;
            } else {
               var30 = this.currentChat;
            }

            TLRPC.ChatFull var16;
            if (!ChatObject.isChannel(var30)) {
               var1 = var30.participants_count;
               TLRPC.ChatFull var31 = this.chatInfo;
               if (var31 != null) {
                  var1 = var31.participants.participants.size();
               }

               if (var1 != 0 && this.onlineCount > 1) {
                  var14 = String.format("%s, %s", LocaleController.formatPluralString("Members", var1), LocaleController.formatPluralString("OnlineCount", this.onlineCount));
               } else {
                  var14 = LocaleController.formatPluralString("Members", var1);
               }
            } else {
               label345: {
                  var16 = this.chatInfo;
                  if (var16 != null) {
                     TLRPC.Chat var22 = this.currentChat;
                     if (var22.megagroup || var16.participants_count != 0 && !ChatObject.hasAdminRights(var22) && !this.chatInfo.can_view_participants) {
                        if (this.currentChat.megagroup) {
                           if (this.onlineCount > 1) {
                              var1 = this.chatInfo.participants_count;
                              if (var1 != 0) {
                                 var14 = String.format("%s, %s", LocaleController.formatPluralString("Members", var1), LocaleController.formatPluralString("OnlineCount", Math.min(this.onlineCount, this.chatInfo.participants_count)));
                                 break label345;
                              }
                           }

                           var14 = LocaleController.formatPluralString("Members", this.chatInfo.participants_count);
                        } else {
                           int[] var28 = new int[1];
                           LocaleController.formatShortNumber(this.chatInfo.participants_count, var28);
                           if (this.currentChat.megagroup) {
                              var14 = LocaleController.formatPluralString("Members", this.chatInfo.participants_count);
                           } else {
                              var14 = LocaleController.formatPluralString("Subscribers", this.chatInfo.participants_count);
                           }
                        }
                        break label345;
                     }
                  }

                  if (this.currentChat.megagroup) {
                     var14 = LocaleController.getString("Loading", 2131559768).toLowerCase();
                  } else if ((var30.flags & 64) != 0) {
                     var14 = LocaleController.getString("ChannelPublic", 2131558991).toLowerCase();
                  } else {
                     var14 = LocaleController.getString("ChannelPrivate", 2131558988).toLowerCase();
                  }
               }
            }

            int var12 = 0;

            boolean var27;
            for(var27 = false; var12 < 2; ++var12) {
               SimpleTextView[] var17 = this.nameTextView;
               if (var17[var12] != null) {
                  boolean var13 = var27;
                  if (var30.title != null) {
                     var13 = var27;
                     if (!var17[var12].getText().equals(var30.title)) {
                        var13 = var27;
                        if (this.nameTextView[var12].setText(var30.title)) {
                           var13 = true;
                        }
                     }
                  }

                  this.nameTextView[var12].setLeftDrawable((Drawable)null);
                  if (var12 != 0) {
                     if (var30.scam) {
                        this.nameTextView[var12].setRightDrawable(this.getScamDrawable());
                     } else if (var30.verified) {
                        this.nameTextView[var12].setRightDrawable(new CombinedDrawable(Theme.profile_verifiedDrawable, Theme.profile_verifiedCheckDrawable));
                     } else {
                        this.nameTextView[var12].setRightDrawable((Drawable)null);
                     }
                  } else if (var30.scam) {
                     this.nameTextView[var12].setRightDrawable(this.getScamDrawable());
                  } else {
                     SimpleTextView var29 = this.nameTextView[var12];
                     Drawable var19;
                     if (MessagesController.getInstance(super.currentAccount).isDialogMuted((long)(-this.chat_id))) {
                        var19 = Theme.chat_muteIconDrawable;
                     } else {
                        var19 = null;
                     }

                     var29.setRightDrawable(var19);
                  }

                  if (var12 == 0 && var2 != null) {
                     this.onlineTextView[var12].setText(var2);
                     var27 = var13;
                  } else if (this.currentChat.megagroup && this.chatInfo != null && this.onlineCount > 0) {
                     var27 = var13;
                     if (!this.onlineTextView[var12].getText().equals(var14)) {
                        this.onlineTextView[var12].setText(var14);
                        var27 = var13;
                     }
                  } else {
                     if (var12 == 0 && ChatObject.isChannel(this.currentChat)) {
                        var16 = this.chatInfo;
                        if (var16 != null && var16.participants_count != 0) {
                           TLRPC.Chat var23 = this.currentChat;
                           if (var23.megagroup || var23.broadcast) {
                              int[] var25 = new int[1];
                              var7 = LocaleController.formatShortNumber(this.chatInfo.participants_count, var25);
                              if (this.currentChat.megagroup) {
                                 this.onlineTextView[var12].setText(LocaleController.formatPluralString("Members", var25[0]).replace(String.format("%d", var25[0]), var7));
                                 var27 = var13;
                              } else {
                                 this.onlineTextView[var12].setText(LocaleController.formatPluralString("Subscribers", var25[0]).replace(String.format("%d", var25[0]), var7));
                                 var27 = var13;
                              }
                              continue;
                           }
                        }
                     }

                     var27 = var13;
                     if (!this.onlineTextView[var12].getText().equals(var14)) {
                        this.onlineTextView[var12].setText(var14);
                        var27 = var13;
                     }
                  }
               }
            }

            if (var27) {
               this.needLayout();
            }

            TLRPC.ChatPhoto var32 = var30.photo;
            TLRPC.FileLocation var33;
            if (var32 != null) {
               var33 = var32.photo_big;
            } else {
               var33 = null;
            }

            this.avatarDrawable.setInfo(var30);
            this.avatarImage.setImage((ImageLocation)ImageLocation.getForChat(var30, false), "50_50", (Drawable)this.avatarDrawable, (Object)var30);
            FileLoader.getInstance(super.currentAccount).loadFile(ImageLocation.getForChat(var30, true), var30, (String)null, 0, 1);
            this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(var33) ^ true, false);
         }
      }

   }

   private void updateRowsIds() {
      this.rowCount = 0;
      this.emptyRow = -1;
      this.infoHeaderRow = -1;
      this.phoneRow = -1;
      this.userInfoRow = -1;
      this.channelInfoRow = -1;
      this.usernameRow = -1;
      this.settingsTimerRow = -1;
      this.settingsKeyRow = -1;
      this.notificationsDividerRow = -1;
      this.notificationsRow = -1;
      this.infoSectionRow = -1;
      this.settingsSectionRow = -1;
      this.membersHeaderRow = -1;
      this.membersStartRow = -1;
      this.membersEndRow = -1;
      this.addMemberRow = -1;
      this.subscribersRow = -1;
      this.administratorsRow = -1;
      this.blockedUsersRow = -1;
      this.membersSectionRow = -1;
      this.sharedHeaderRow = -1;
      this.photosRow = -1;
      this.filesRow = -1;
      this.linksRow = -1;
      this.audioRow = -1;
      this.voiceRow = -1;
      this.groupsInCommonRow = -1;
      this.sharedSectionRow = -1;
      this.unblockRow = -1;
      this.startSecretChatRow = -1;
      this.leaveChannelRow = -1;
      this.joinRow = -1;
      this.lastSectionRow = -1;
      int var1 = 0;

      boolean var7;
      while(true) {
         int[] var2 = this.lastMediaCount;
         if (var1 >= var2.length) {
            var7 = false;
            break;
         }

         if (var2[var1] > 0) {
            var7 = true;
            break;
         }

         ++var1;
      }

      int var3;
      if (this.user_id != 0 && LocaleController.isRTL) {
         var3 = this.rowCount++;
         this.emptyRow = var3;
      }

      if (this.user_id != 0) {
         TLRPC.User var8 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         TLRPC.UserFull var4 = this.userInfo;
         boolean var10;
         if (var4 != null && !TextUtils.isEmpty(var4.about) || var8 != null && !TextUtils.isEmpty(var8.username)) {
            var10 = true;
         } else {
            var10 = false;
         }

         boolean var5;
         if (var8 != null && !TextUtils.isEmpty(var8.phone)) {
            var5 = true;
         } else {
            var5 = false;
         }

         int var6 = this.rowCount++;
         this.infoHeaderRow = var6;
         if (!this.isBot && (var5 || !var5 && !var10)) {
            var3 = this.rowCount++;
            this.phoneRow = var3;
         }

         var4 = this.userInfo;
         if (var4 != null && !TextUtils.isEmpty(var4.about)) {
            var3 = this.rowCount++;
            this.userInfoRow = var3;
         }

         if (var8 != null && !TextUtils.isEmpty(var8.username)) {
            var3 = this.rowCount++;
            this.usernameRow = var3;
         }

         if (this.phoneRow != -1 || this.userInfoRow != -1 || this.usernameRow != -1) {
            var3 = this.rowCount++;
            this.notificationsDividerRow = var3;
         }

         if (this.user_id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
            var3 = this.rowCount++;
            this.notificationsRow = var3;
         }

         var3 = this.rowCount++;
         this.infoSectionRow = var3;
         if (this.currentEncryptedChat instanceof TLRPC.TL_encryptedChat) {
            var3 = this.rowCount++;
            this.settingsTimerRow = var3;
            var3 = this.rowCount++;
            this.settingsKeyRow = var3;
            var3 = this.rowCount++;
            this.settingsSectionRow = var3;
         }

         label321: {
            if (!var7) {
               var4 = this.userInfo;
               if (var4 == null || var4.common_chats_count == 0) {
                  break label321;
               }
            }

            var1 = this.rowCount++;
            this.sharedHeaderRow = var1;
            if (this.lastMediaCount[0] > 0) {
               var1 = this.rowCount++;
               this.photosRow = var1;
            } else {
               this.photosRow = -1;
            }

            if (this.lastMediaCount[1] > 0) {
               var1 = this.rowCount++;
               this.filesRow = var1;
            } else {
               this.filesRow = -1;
            }

            if (this.lastMediaCount[3] > 0) {
               var1 = this.rowCount++;
               this.linksRow = var1;
            } else {
               this.linksRow = -1;
            }

            if (this.lastMediaCount[4] > 0) {
               var1 = this.rowCount++;
               this.audioRow = var1;
            } else {
               this.audioRow = -1;
            }

            if (this.lastMediaCount[2] > 0) {
               var1 = this.rowCount++;
               this.voiceRow = var1;
            } else {
               this.voiceRow = -1;
            }

            var4 = this.userInfo;
            if (var4 != null && var4.common_chats_count != 0) {
               var1 = this.rowCount++;
               this.groupsInCommonRow = var1;
            }

            var1 = this.rowCount++;
            this.sharedSectionRow = var1;
         }

         if (var8 != null && !this.isBot && this.currentEncryptedChat == null && var8.id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
            if (this.userBlocked) {
               var1 = this.rowCount++;
               this.unblockRow = var1;
            } else {
               var1 = this.rowCount++;
               this.startSecretChatRow = var1;
            }

            var1 = this.rowCount++;
            this.lastSectionRow = var1;
         }
      } else {
         var3 = this.chat_id;
         if (var3 != 0) {
            TLRPC.ChatFull var9;
            TLRPC.ChatParticipants var12;
            if (var3 > 0) {
               var9 = this.chatInfo;
               if (var9 != null && !TextUtils.isEmpty(var9.about) || !TextUtils.isEmpty(this.currentChat.username)) {
                  var3 = this.rowCount++;
                  this.infoHeaderRow = var3;
                  var9 = this.chatInfo;
                  if (var9 != null && !TextUtils.isEmpty(var9.about)) {
                     var3 = this.rowCount++;
                     this.channelInfoRow = var3;
                  }

                  if (!TextUtils.isEmpty(this.currentChat.username)) {
                     var3 = this.rowCount++;
                     this.usernameRow = var3;
                  }
               }

               if (this.channelInfoRow != -1 || this.usernameRow != -1) {
                  var3 = this.rowCount++;
                  this.notificationsDividerRow = var3;
               }

               var3 = this.rowCount++;
               this.notificationsRow = var3;
               var3 = this.rowCount++;
               this.infoSectionRow = var3;
               TLRPC.Chat var11;
               if (ChatObject.isChannel(this.currentChat)) {
                  var11 = this.currentChat;
                  if (!var11.megagroup) {
                     TLRPC.ChatFull var13 = this.chatInfo;
                     if (var13 != null && (var11.creator || var13.can_view_participants)) {
                        var3 = this.rowCount++;
                        this.membersHeaderRow = var3;
                        var3 = this.rowCount++;
                        this.subscribersRow = var3;
                        var3 = this.rowCount++;
                        this.administratorsRow = var3;
                        var9 = this.chatInfo;
                        if (var9.banned_count != 0 || var9.kicked_count != 0) {
                           var3 = this.rowCount++;
                           this.blockedUsersRow = var3;
                        }

                        var3 = this.rowCount++;
                        this.membersSectionRow = var3;
                     }
                  }
               }

               if (var7) {
                  var1 = this.rowCount++;
                  this.sharedHeaderRow = var1;
                  if (this.lastMediaCount[0] > 0) {
                     var1 = this.rowCount++;
                     this.photosRow = var1;
                  } else {
                     this.photosRow = -1;
                  }

                  if (this.lastMediaCount[1] > 0) {
                     var1 = this.rowCount++;
                     this.filesRow = var1;
                  } else {
                     this.filesRow = -1;
                  }

                  if (this.lastMediaCount[3] > 0) {
                     var1 = this.rowCount++;
                     this.linksRow = var1;
                  } else {
                     this.linksRow = -1;
                  }

                  if (this.lastMediaCount[4] > 0) {
                     var1 = this.rowCount++;
                     this.audioRow = var1;
                  } else {
                     this.audioRow = -1;
                  }

                  if (this.lastMediaCount[2] > 0) {
                     var1 = this.rowCount++;
                     this.voiceRow = var1;
                  } else {
                     this.voiceRow = -1;
                  }

                  var1 = this.rowCount++;
                  this.sharedSectionRow = var1;
               }

               if (ChatObject.isChannel(this.currentChat)) {
                  var11 = this.currentChat;
                  if (!var11.creator && !var11.left && !var11.kicked && !var11.megagroup) {
                     var1 = this.rowCount++;
                     this.leaveChannelRow = var1;
                     var1 = this.rowCount++;
                     this.lastSectionRow = var1;
                  }

                  var9 = this.chatInfo;
                  if (var9 != null && this.currentChat.megagroup) {
                     var12 = var9.participants;
                     if (var12 != null && !var12.participants.isEmpty()) {
                        label259: {
                           if (!ChatObject.isNotInChat(this.currentChat)) {
                              var11 = this.currentChat;
                              if (var11.megagroup && ChatObject.canAddUsers(var11)) {
                                 var9 = this.chatInfo;
                                 if (var9 == null || var9.participants_count < MessagesController.getInstance(super.currentAccount).maxMegagroupCount) {
                                    var1 = this.rowCount++;
                                    this.addMemberRow = var1;
                                    break label259;
                                 }
                              }
                           }

                           var1 = this.rowCount++;
                           this.membersHeaderRow = var1;
                        }

                        var1 = this.rowCount;
                        this.membersStartRow = var1;
                        this.rowCount = var1 + this.chatInfo.participants.participants.size();
                        var1 = this.rowCount;
                        this.membersEndRow = var1;
                        this.rowCount = var1 + 1;
                        this.membersSectionRow = var1;
                     }
                  }

                  if (this.lastSectionRow == -1) {
                     var11 = this.currentChat;
                     if (var11.left && !var11.kicked) {
                        var1 = this.rowCount++;
                        this.joinRow = var1;
                        var1 = this.rowCount++;
                        this.lastSectionRow = var1;
                     }
                  }
               } else {
                  var9 = this.chatInfo;
                  if (var9 != null && !(var9.participants instanceof TLRPC.TL_chatParticipantsForbidden)) {
                     label269: {
                        if (!ChatObject.canAddUsers(this.currentChat)) {
                           TLRPC.TL_chatBannedRights var14 = this.currentChat.default_banned_rights;
                           if (var14 != null && var14.invite_users) {
                              var1 = this.rowCount++;
                              this.membersHeaderRow = var1;
                              break label269;
                           }
                        }

                        var1 = this.rowCount++;
                        this.addMemberRow = var1;
                     }

                     var1 = this.rowCount;
                     this.membersStartRow = var1;
                     this.rowCount = var1 + this.chatInfo.participants.participants.size();
                     var1 = this.rowCount;
                     this.membersEndRow = var1;
                     this.rowCount = var1 + 1;
                     this.membersSectionRow = var1;
                  }
               }
            } else if (!ChatObject.isChannel(this.currentChat)) {
               var9 = this.chatInfo;
               if (var9 != null) {
                  var12 = var9.participants;
                  if (!(var12 instanceof TLRPC.TL_chatParticipantsForbidden)) {
                     var1 = this.rowCount++;
                     this.membersHeaderRow = var1;
                     var1 = this.rowCount;
                     this.membersStartRow = var1;
                     this.rowCount = var1 + var12.participants.size();
                     var1 = this.rowCount;
                     this.membersEndRow = var1;
                     this.rowCount = var1 + 1;
                     this.membersSectionRow = var1;
                     var1 = this.rowCount++;
                     this.addMemberRow = var1;
                  }
               }
            }
         }
      }

   }

   private void updateSharedMediaRows() {
      if (this.listAdapter != null) {
         int var1 = this.sharedHeaderRow;
         int var2 = this.photosRow;
         int var3 = this.filesRow;
         int var4 = this.linksRow;
         int var5 = this.audioRow;
         int var6 = this.voiceRow;
         int var7 = this.groupsInCommonRow;
         int var8 = this.sharedSectionRow;
         this.updateRowsIds();
         byte var9 = 3;
         if (var1 == -1 && this.sharedHeaderRow != -1) {
            if (this.photosRow == -1) {
               var9 = 2;
            }

            var7 = var9;
            if (this.filesRow != -1) {
               var7 = var9 + 1;
            }

            var8 = var7;
            if (this.linksRow != -1) {
               var8 = var7 + 1;
            }

            var7 = var8;
            if (this.audioRow != -1) {
               var7 = var8 + 1;
            }

            var8 = var7;
            if (this.voiceRow != -1) {
               var8 = var7 + 1;
            }

            var7 = var8;
            if (this.groupsInCommonRow != -1) {
               var7 = var8 + 1;
            }

            this.listAdapter.notifyItemRangeInserted(this.sharedHeaderRow, var7);
         } else if (var1 != -1 && this.sharedHeaderRow != -1) {
            if (var2 != -1) {
               var8 = this.photosRow;
               if (var8 != -1 && this.prevMediaCount[0] != this.lastMediaCount[0]) {
                  this.listAdapter.notifyItemChanged(var8);
               }
            }

            if (var3 != -1) {
               var8 = this.filesRow;
               if (var8 != -1 && this.prevMediaCount[1] != this.lastMediaCount[1]) {
                  this.listAdapter.notifyItemChanged(var8);
               }
            }

            if (var4 != -1) {
               var8 = this.linksRow;
               if (var8 != -1 && this.prevMediaCount[3] != this.lastMediaCount[3]) {
                  this.listAdapter.notifyItemChanged(var8);
               }
            }

            if (var5 != -1) {
               var8 = this.audioRow;
               if (var8 != -1 && this.prevMediaCount[4] != this.lastMediaCount[4]) {
                  this.listAdapter.notifyItemChanged(var8);
               }
            }

            if (var6 != -1) {
               var8 = this.voiceRow;
               if (var8 != -1 && this.prevMediaCount[2] != this.lastMediaCount[2]) {
                  this.listAdapter.notifyItemChanged(var8);
               }
            }

            label146: {
               if (var2 == -1) {
                  var8 = this.photosRow;
                  if (var8 != -1) {
                     this.listAdapter.notifyItemInserted(var8);
                     break label146;
                  }
               }

               if (var2 != -1 && this.photosRow == -1) {
                  this.listAdapter.notifyItemRemoved(var2);
               }
            }

            label141: {
               if (var3 == -1) {
                  var8 = this.filesRow;
                  if (var8 != -1) {
                     this.listAdapter.notifyItemInserted(var8);
                     break label141;
                  }
               }

               if (var3 != -1 && this.filesRow == -1) {
                  this.listAdapter.notifyItemRemoved(var3);
               }
            }

            label136: {
               if (var4 == -1) {
                  var8 = this.linksRow;
                  if (var8 != -1) {
                     this.listAdapter.notifyItemInserted(var8);
                     break label136;
                  }
               }

               if (var4 != -1 && this.linksRow == -1) {
                  this.listAdapter.notifyItemRemoved(var4);
               }
            }

            label131: {
               if (var5 == -1) {
                  var8 = this.audioRow;
                  if (var8 != -1) {
                     this.listAdapter.notifyItemInserted(var8);
                     break label131;
                  }
               }

               if (var5 != -1 && this.audioRow == -1) {
                  this.listAdapter.notifyItemRemoved(var5);
               }
            }

            label126: {
               if (var6 == -1) {
                  var8 = this.voiceRow;
                  if (var8 != -1) {
                     this.listAdapter.notifyItemInserted(var8);
                     break label126;
                  }
               }

               if (var6 != -1 && this.voiceRow == -1) {
                  this.listAdapter.notifyItemRemoved(var6);
               }
            }

            if (var7 == -1) {
               var8 = this.groupsInCommonRow;
               if (var8 != -1) {
                  this.listAdapter.notifyItemInserted(var8);
                  return;
               }
            }

            if (var7 != -1 && this.groupsInCommonRow == -1) {
               this.listAdapter.notifyItemRemoved(var7);
            }
         }

      }
   }

   protected ActionBar createActionBar(Context var1) {
      ActionBar var5 = new ActionBar(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            return super.onTouchEvent(var1);
         }
      };
      int var2;
      if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
         var2 = 5;
      } else {
         var2 = this.chat_id;
      }

      var2 = AvatarDrawable.getButtonColorForId(var2);
      boolean var3 = false;
      var5.setItemsBackgroundColor(var2, false);
      var5.setItemsColor(Theme.getColor("actionBarDefaultIcon"), false);
      var5.setItemsColor(Theme.getColor("actionBarActionModeDefaultIcon"), true);
      var5.setBackButtonDrawable(new BackDrawable(false));
      var5.setCastShadows(false);
      var5.setAddToContainer(false);
      boolean var4 = var3;
      if (VERSION.SDK_INT >= 21) {
         var4 = var3;
         if (!AndroidUtilities.isTablet()) {
            var4 = true;
         }
      }

      var5.setOccupyStatusBar(var4);
      return var5;
   }

   public View createView(Context var1) {
      Theme.createProfileResources(var1);
      super.hasOwnBackground = true;
      this.extraHeight = AndroidUtilities.dp(88.0F);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$null$3$ProfileActivity$3(AlertDialog[] var1, TLObject var2) {
            try {
               var1[0].dismiss();
            } catch (Throwable var4) {
            }

            var1[0] = null;
            if (var2 != null) {
               TLRPC.TL_statsURL var6 = (TLRPC.TL_statsURL)var2;
               ProfileActivity var5 = ProfileActivity.this;
               var5.presentFragment(new WebviewActivity(var6.url, (long)(-var5.chat_id)));
            }

         }

         // $FF: synthetic method
         public void lambda$onItemClick$0$ProfileActivity$3(DialogInterface var1, int var2) {
            MessagesController.getInstance(ProfileActivity.access$3800(ProfileActivity.this)).blockUser(ProfileActivity.this.user_id);
            AlertsCreator.showSimpleToast(ProfileActivity.this, LocaleController.getString("UserBlocked", 2131560991));
         }

         // $FF: synthetic method
         public void lambda$onItemClick$1$ProfileActivity$3(TLRPC.User var1, DialogInterface var2, int var3) {
            ArrayList var4 = new ArrayList();
            var4.add(var1);
            ContactsController.getInstance(ProfileActivity.access$3700(ProfileActivity.this)).deleteContact(var4);
         }

         // $FF: synthetic method
         public void lambda$onItemClick$2$ProfileActivity$3(TLRPC.User var1, DialogsActivity var2, ArrayList var3, CharSequence var4, boolean var5) {
            long var6 = (Long)var3.get(0);
            Bundle var9 = new Bundle();
            var9.putBoolean("scrollToTopOnResume", true);
            int var8 = -((int)var6);
            var9.putInt("chat_id", var8);
            if (MessagesController.getInstance(ProfileActivity.access$3300(ProfileActivity.this)).checkCanOpenChat(var9, var2)) {
               NotificationCenter.getInstance(ProfileActivity.access$3400(ProfileActivity.this)).removeObserver(ProfileActivity.this, NotificationCenter.closeChats);
               NotificationCenter.getInstance(ProfileActivity.access$3500(ProfileActivity.this)).postNotificationName(NotificationCenter.closeChats);
               MessagesController.getInstance(ProfileActivity.access$3600(ProfileActivity.this)).addUserToChat(var8, var1, (TLRPC.ChatFull)null, 0, (String)null, ProfileActivity.this, (Runnable)null);
               ProfileActivity.this.presentFragment(new ChatActivity(var9), true);
               ProfileActivity.this.removeSelfFromStack();
            }
         }

         // $FF: synthetic method
         public void lambda$onItemClick$4$ProfileActivity$3(AlertDialog[] var1, TLObject var2, TLRPC.TL_error var3) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ProfileActivity$3$Ez4H7d10euWTC8kOLsie4J_yGD0(this, var1, var2));
         }

         // $FF: synthetic method
         public void lambda$onItemClick$5$ProfileActivity$3(int var1, DialogInterface var2) {
            ConnectionsManager.getInstance(ProfileActivity.access$3200(ProfileActivity.this)).cancelRequest(var1, true);
         }

         public void onItemClick(int var1) {
            if (ProfileActivity.this.getParentActivity() != null) {
               if (var1 == -1) {
                  ProfileActivity.this.finishFragment();
               } else {
                  TLRPC.User var16;
                  AlertDialog.Builder var23;
                  AlertDialog var27;
                  TextView var28;
                  if (var1 == 2) {
                     var16 = MessagesController.getInstance(ProfileActivity.access$900(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                     if (var16 == null) {
                        return;
                     }

                     if (ProfileActivity.this.isBot && !MessagesController.isSupportUser(var16)) {
                        if (!ProfileActivity.this.userBlocked) {
                           MessagesController.getInstance(ProfileActivity.access$1300(ProfileActivity.this)).blockUser(ProfileActivity.this.user_id);
                        } else {
                           MessagesController.getInstance(ProfileActivity.access$1400(ProfileActivity.this)).unblockUser(ProfileActivity.this.user_id);
                           SendMessagesHelper.getInstance(ProfileActivity.access$1500(ProfileActivity.this)).sendMessage("/start", (long)ProfileActivity.this.user_id, (MessageObject)null, (TLRPC.WebPage)null, false, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
                           ProfileActivity.this.finishFragment();
                        }
                     } else if (ProfileActivity.this.userBlocked) {
                        MessagesController.getInstance(ProfileActivity.access$1200(ProfileActivity.this)).unblockUser(ProfileActivity.this.user_id);
                        AlertsCreator.showSimpleToast(ProfileActivity.this, LocaleController.getString("UserUnblocked", 2131561020));
                     } else {
                        var23 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity());
                        var23.setTitle(LocaleController.getString("BlockUser", 2131558834));
                        var23.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureBlockContact2", 2131558667, ContactsController.formatName(var16.first_name, var16.last_name))));
                        var23.setPositiveButton(LocaleController.getString("BlockContact", 2131558833), new _$$Lambda$ProfileActivity$3$RRKDv6pwZUkbtuOuDHn_j5rBSMY(this));
                        var23.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                        var27 = var23.create();
                        ProfileActivity.this.showDialog(var27);
                        var28 = (TextView)var27.getButton(-1);
                        if (var28 != null) {
                           var28.setTextColor(Theme.getColor("dialogTextRed2"));
                        }
                     }
                  } else {
                     Bundle var17;
                     if (var1 == 1) {
                        TLRPC.User var25 = MessagesController.getInstance(ProfileActivity.access$1600(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                        var17 = new Bundle();
                        var17.putInt("user_id", var25.id);
                        var17.putBoolean("addContact", true);
                        ProfileActivity.this.presentFragment(new ContactAddActivity(var17));
                     } else if (var1 == 3) {
                        var17 = new Bundle();
                        var17.putBoolean("onlySelect", true);
                        var17.putString("selectAlertString", LocaleController.getString("SendContactTo", 2131560689));
                        var17.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroup", 2131560690));
                        DialogsActivity var29 = new DialogsActivity(var17);
                        var29.setDelegate(ProfileActivity.this);
                        ProfileActivity.this.presentFragment(var29);
                     } else if (var1 == 4) {
                        var17 = new Bundle();
                        var17.putInt("user_id", ProfileActivity.this.user_id);
                        ProfileActivity.this.presentFragment(new ContactAddActivity(var17));
                     } else if (var1 == 5) {
                        var16 = MessagesController.getInstance(ProfileActivity.access$1700(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                        if (var16 == null || ProfileActivity.this.getParentActivity() == null) {
                           return;
                        }

                        var23 = new AlertDialog.Builder(ProfileActivity.this.getParentActivity());
                        var23.setTitle(LocaleController.getString("DeleteContact", 2131559241));
                        var23.setMessage(LocaleController.getString("AreYouSureDeleteContact", 2131558680));
                        var23.setPositiveButton(LocaleController.getString("Delete", 2131559227), new _$$Lambda$ProfileActivity$3$_wA0DEMpYVaVxb1SmJsBe0hOY_I(this, var16));
                        var23.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                        var27 = var23.create();
                        ProfileActivity.this.showDialog(var27);
                        var28 = (TextView)var27.getButton(-1);
                        if (var28 != null) {
                           var28.setTextColor(Theme.getColor("dialogTextRed2"));
                        }
                     } else if (var1 == 7) {
                        ProfileActivity.this.leaveChatPressed();
                     } else if (var1 == 12) {
                        var17 = new Bundle();
                        var17.putInt("chat_id", ProfileActivity.this.chat_id);
                        ChatEditActivity var26 = new ChatEditActivity(var17);
                        var26.setInfo(ProfileActivity.this.chatInfo);
                        ProfileActivity.this.presentFragment(var26);
                     } else if (var1 == 9) {
                        var16 = MessagesController.getInstance(ProfileActivity.access$2000(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                        if (var16 == null) {
                           return;
                        }

                        Bundle var21 = new Bundle();
                        var21.putBoolean("onlySelect", true);
                        var21.putInt("dialogsType", 2);
                        var21.putString("addToGroupAlertString", LocaleController.formatString("AddToTheGroupTitle", 2131558596, UserObject.getUserName(var16), "%1$s"));
                        DialogsActivity var22 = new DialogsActivity(var21);
                        var22.setDelegate(new _$$Lambda$ProfileActivity$3$P_pKjyT3akZRyLfSf9io1zoClmw(this, var16));
                        ProfileActivity.this.presentFragment(var22);
                     } else {
                        Exception var2;
                        Exception var10000;
                        boolean var10001;
                        if (var1 == 10) {
                           label208: {
                              try {
                                 var16 = MessagesController.getInstance(ProfileActivity.access$2100(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label208;
                              }

                              if (var16 == null) {
                                 return;
                              }

                              TLRPC.BotInfo var4;
                              Intent var19;
                              try {
                                 var19 = new Intent("android.intent.action.SEND");
                                 var19.setType("text/plain");
                                 var4 = ProfileActivity.this.botInfo;
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break label208;
                              }

                              label173: {
                                 StringBuilder var24;
                                 if (var4 != null) {
                                    try {
                                       if (ProfileActivity.this.userInfo != null && !TextUtils.isEmpty(ProfileActivity.this.userInfo.about)) {
                                          var24 = new StringBuilder();
                                          var24.append("%s https://");
                                          var24.append(MessagesController.getInstance(ProfileActivity.access$2400(ProfileActivity.this)).linkPrefix);
                                          var24.append("/%s");
                                          var19.putExtra("android.intent.extra.TEXT", String.format(var24.toString(), ProfileActivity.this.userInfo.about, var16.username));
                                          break label173;
                                       }
                                    } catch (Exception var13) {
                                       var10000 = var13;
                                       var10001 = false;
                                       break label208;
                                    }
                                 }

                                 try {
                                    var24 = new StringBuilder();
                                    var24.append("https://");
                                    var24.append(MessagesController.getInstance(ProfileActivity.access$2500(ProfileActivity.this)).linkPrefix);
                                    var24.append("/%s");
                                    var19.putExtra("android.intent.extra.TEXT", String.format(var24.toString(), var16.username));
                                 } catch (Exception var12) {
                                    var10000 = var12;
                                    var10001 = false;
                                    break label208;
                                 }
                              }

                              try {
                                 ProfileActivity.this.startActivityForResult(Intent.createChooser(var19, LocaleController.getString("BotShare", 2131558856)), 500);
                                 return;
                              } catch (Exception var11) {
                                 var10000 = var11;
                                 var10001 = false;
                              }
                           }

                           var2 = var10000;
                           FileLog.e((Throwable)var2);
                        } else if (var1 == 14) {
                           label209: {
                              long var5;
                              label206: {
                                 try {
                                    if (ProfileActivity.this.currentEncryptedChat != null) {
                                       var5 = (long)ProfileActivity.this.currentEncryptedChat.id << 32;
                                       break label206;
                                    }
                                 } catch (Exception var10) {
                                    var10000 = var10;
                                    var10001 = false;
                                    break label209;
                                 }

                                 label140: {
                                    try {
                                       if (ProfileActivity.this.user_id != 0) {
                                          var1 = ProfileActivity.this.user_id;
                                          break label140;
                                       }
                                    } catch (Exception var9) {
                                       var10000 = var9;
                                       var10001 = false;
                                       break label209;
                                    }

                                    try {
                                       if (ProfileActivity.this.chat_id == 0) {
                                          return;
                                       }

                                       var1 = -ProfileActivity.this.chat_id;
                                    } catch (Exception var8) {
                                       var10000 = var8;
                                       var10001 = false;
                                       break label209;
                                    }
                                 }

                                 var5 = (long)var1;
                              }

                              try {
                                 DataQuery.getInstance(ProfileActivity.access$2700(ProfileActivity.this)).installShortcut(var5);
                                 return;
                              } catch (Exception var7) {
                                 var10000 = var7;
                                 var10001 = false;
                              }
                           }

                           var2 = var10000;
                           FileLog.e((Throwable)var2);
                        } else if (var1 == 15) {
                           var16 = MessagesController.getInstance(ProfileActivity.access$2800(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                           if (var16 != null) {
                              VoIPHelper.startCall(var16, ProfileActivity.this.getParentActivity(), ProfileActivity.this.userInfo);
                           }
                        } else if (var1 == 17) {
                           var17 = new Bundle();
                           var17.putInt("chat_id", ProfileActivity.this.chat_id);
                           var17.putInt("type", 2);
                           var17.putBoolean("open_search", true);
                           ChatUsersActivity var18 = new ChatUsersActivity(var17);
                           var18.setInfo(ProfileActivity.this.chatInfo);
                           ProfileActivity.this.presentFragment(var18);
                        } else if (var1 == 18) {
                           ProfileActivity.this.openAddMember();
                        } else if (var1 == 19) {
                           if (ProfileActivity.this.user_id != 0) {
                              var1 = ProfileActivity.this.user_id;
                           } else {
                              var1 = -ProfileActivity.this.chat_id;
                           }

                           AlertDialog[] var20 = new AlertDialog[]{new AlertDialog(ProfileActivity.this.getParentActivity(), 3)};
                           TLRPC.TL_messages_getStatsURL var3 = new TLRPC.TL_messages_getStatsURL();
                           var3.peer = MessagesController.getInstance(ProfileActivity.access$3000(ProfileActivity.this)).getInputPeer(var1);
                           var3.dark = Theme.getCurrentTheme().isDark();
                           var3.params = "";
                           var1 = ConnectionsManager.getInstance(ProfileActivity.access$3100(ProfileActivity.this)).sendRequest(var3, new _$$Lambda$ProfileActivity$3$vS1Cpjl_0IaDsZcydzGAsiNHZyw(this, var20));
                           if (var20[0] == null) {
                              return;
                           }

                           var20[0].setOnCancelListener(new _$$Lambda$ProfileActivity$3$Y2V1RZ8yX7Q0L7hu0zkMaoHuQs4(this, var1));
                           ProfileActivity.this.showDialog(var20[0]);
                        }
                     }
                  }
               }

            }
         }
      });
      this.createActionBarMenu();
      this.listAdapter = new ProfileActivity.ListAdapter(var1);
      this.avatarDrawable = new AvatarDrawable();
      this.avatarDrawable.setProfile(true);
      super.fragmentView = new FrameLayout(var1) {
         public boolean hasOverlappingRendering() {
            return false;
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            ProfileActivity.this.checkListViewScroll();
         }
      };
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1) {
         private Paint paint = new Paint();

         public boolean hasOverlappingRendering() {
            return false;
         }

         public void onDraw(Canvas var1) {
            RecyclerView.ViewHolder var2;
            if (ProfileActivity.this.lastSectionRow != -1) {
               var2 = this.findViewHolderForAdapterPosition(ProfileActivity.this.lastSectionRow);
            } else if (ProfileActivity.this.sharedSectionRow != -1 && (ProfileActivity.this.membersSectionRow == -1 || ProfileActivity.this.membersSectionRow < ProfileActivity.this.sharedSectionRow)) {
               var2 = this.findViewHolderForAdapterPosition(ProfileActivity.this.sharedSectionRow);
            } else if (ProfileActivity.this.membersSectionRow != -1 && (ProfileActivity.this.sharedSectionRow == -1 || ProfileActivity.this.membersSectionRow > ProfileActivity.this.sharedSectionRow)) {
               var2 = this.findViewHolderForAdapterPosition(ProfileActivity.this.membersSectionRow);
            } else if (ProfileActivity.this.infoSectionRow != -1) {
               var2 = this.findViewHolderForAdapterPosition(ProfileActivity.this.infoSectionRow);
            } else {
               var2 = null;
            }

            int var3;
            int var4;
            label29: {
               var3 = this.getMeasuredHeight();
               if (var2 != null) {
                  var4 = var2.itemView.getBottom();
                  if (var2.itemView.getBottom() < var3) {
                     break label29;
                  }
               }

               var4 = var3;
            }

            this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
            float var5 = (float)this.getMeasuredWidth();
            float var6 = (float)var4;
            var1.drawRect(0.0F, 0.0F, var5, var6, this.paint);
            if (var4 != var3) {
               this.paint.setColor(Theme.getColor("windowBackgroundGray"));
               var1.drawRect(0.0F, var6, (float)this.getMeasuredWidth(), (float)var3, this.paint);
            }

         }
      };
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setClipToPadding(false);
      this.layoutManager = new LinearLayoutManager(var1) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager.setOrientation(1);
      this.listView.setLayoutManager(this.layoutManager);
      RecyclerListView var3 = this.listView;
      int var4;
      if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
         var4 = 5;
      } else {
         var4 = this.chat_id;
      }

      var3.setGlowColor(AvatarDrawable.getProfileBackColorForId(var4));
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$ProfileActivity$H08izesUB4mAkQt5BMshtXqt2Qs(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$ProfileActivity$Y_qIPa4kzyhg9puFuR2Vq4GocZU(this)));
      if (this.banFromGroup != 0) {
         TLRPC.Chat var5 = MessagesController.getInstance(super.currentAccount).getChat(this.banFromGroup);
         if (this.currentChannelParticipant == null) {
            TLRPC.TL_channels_getParticipant var11 = new TLRPC.TL_channels_getParticipant();
            var11.channel = MessagesController.getInputChannel(var5);
            var11.user_id = MessagesController.getInstance(super.currentAccount).getInputUser(this.user_id);
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var11, new _$$Lambda$ProfileActivity$UPmZCQ76huGf0mwqhk7A6RKfTkw(this));
         }

         FrameLayout var12 = new FrameLayout(var1) {
            protected void onDraw(Canvas var1) {
               int var2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
               Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), var2);
               Theme.chat_composeShadowDrawable.draw(var1);
               var1.drawRect(0.0F, (float)var2, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
         };
         var12.setWillNotDraw(false);
         var2.addView(var12, LayoutHelper.createFrame(-1, 51, 83));
         var12.setOnClickListener(new _$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA(this, var5));
         TextView var15 = new TextView(var1);
         var15.setTextColor(Theme.getColor("windowBackgroundWhiteRedText"));
         var15.setTextSize(1, 15.0F);
         var15.setGravity(17);
         var15.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var15.setText(LocaleController.getString("BanFromTheGroup", 2131558830));
         var12.addView(var15, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 1.0F, 0.0F, 0.0F));
         this.listView.setPadding(0, AndroidUtilities.dp(88.0F), 0, AndroidUtilities.dp(48.0F));
         this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0F));
      } else {
         this.listView.setPadding(0, AndroidUtilities.dp(88.0F), 0, 0);
      }

      this.topView = new ProfileActivity.TopView(var1);
      ProfileActivity.TopView var13 = this.topView;
      if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
         var4 = 5;
      } else {
         var4 = this.chat_id;
      }

      var13.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(var4));
      var2.addView(this.topView);
      var2.addView(super.actionBar);
      this.avatarImage = new BackupImageView(var1);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0F));
      this.avatarImage.setPivotX(0.0F);
      this.avatarImage.setPivotY(0.0F);
      var2.addView(this.avatarImage, LayoutHelper.createFrame(42, 42.0F, 51, 64.0F, 0.0F, 0.0F, 0.0F));
      this.avatarImage.setOnClickListener(new _$$Lambda$ProfileActivity$MnD_12oukmBdwxWh9wauwLpeZDg(this));
      this.avatarImage.setContentDescription(LocaleController.getString("AccDescrProfilePicture", 2131558460));

      float var6;
      for(var4 = 0; var4 < 2; ++var4) {
         if (this.playProfileAnimation || var4 != 0) {
            this.nameTextView[var4] = new SimpleTextView(var1);
            if (var4 == 1) {
               this.nameTextView[var4].setTextColor(Theme.getColor("profile_title"));
            } else {
               this.nameTextView[var4].setTextColor(Theme.getColor("actionBarDefaultTitle"));
            }

            this.nameTextView[var4].setTextSize(18);
            this.nameTextView[var4].setGravity(3);
            this.nameTextView[var4].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.nameTextView[var4].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3F));
            this.nameTextView[var4].setPivotX(0.0F);
            this.nameTextView[var4].setPivotY(0.0F);
            SimpleTextView var14 = this.nameTextView[var4];
            if (var4 == 0) {
               var6 = 0.0F;
            } else {
               var6 = 1.0F;
            }

            var14.setAlpha(var6);
            int var7;
            if (var4 == 1) {
               this.nameTextView[var4].setScrollNonFitText(true);
               var14 = this.nameTextView[var4];
               if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
                  var7 = 5;
               } else {
                  var7 = this.chat_id;
               }

               var14.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(var7));
            }

            var14 = this.nameTextView[var4];
            if (var4 == 0) {
               var6 = 48.0F;
            } else {
               var6 = 0.0F;
            }

            var2.addView(var14, LayoutHelper.createFrame(-2, -2.0F, 51, 118.0F, 0.0F, var6, 0.0F));
            this.onlineTextView[var4] = new SimpleTextView(var1);
            var14 = this.onlineTextView[var4];
            if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
               var7 = 5;
            } else {
               var7 = this.chat_id;
            }

            var14.setTextColor(AvatarDrawable.getProfileTextColorForId(var7));
            this.onlineTextView[var4].setTextSize(14);
            this.onlineTextView[var4].setGravity(3);
            var14 = this.onlineTextView[var4];
            if (var4 == 0) {
               var6 = 0.0F;
            } else {
               var6 = 1.0F;
            }

            var14.setAlpha(var6);
            var14 = this.onlineTextView[var4];
            if (var4 == 0) {
               var6 = 48.0F;
            } else {
               var6 = 8.0F;
            }

            var2.addView(var14, LayoutHelper.createFrame(-2, -2.0F, 51, 118.0F, 0.0F, var6, 0.0F));
         }
      }

      if (this.user_id != 0) {
         this.writeButton = new ImageView(var1);
         Drawable var16 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("profile_actionBackground"), Theme.getColor("profile_actionPressedBackground"));
         Object var17 = var16;
         if (VERSION.SDK_INT < 21) {
            Drawable var8 = var1.getResources().getDrawable(2131165388).mutate();
            var8.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
            var17 = new CombinedDrawable(var8, var16, 0, 0);
            ((CombinedDrawable)var17).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
         }

         this.writeButton.setBackgroundDrawable((Drawable)var17);
         this.writeButton.setImageResource(2131165790);
         this.writeButton.setContentDescription(LocaleController.getString("AccDescrOpenChat", 2131558450));
         this.writeButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), Mode.MULTIPLY));
         this.writeButton.setScaleType(ScaleType.CENTER);
         if (VERSION.SDK_INT >= 21) {
            StateListAnimator var9 = new StateListAnimator();
            ObjectAnimator var19 = ObjectAnimator.ofFloat(this.writeButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
            var9.addState(new int[]{16842919}, var19);
            var19 = ObjectAnimator.ofFloat(this.writeButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
            var9.addState(new int[0], var19);
            this.writeButton.setStateListAnimator(var9);
            this.writeButton.setOutlineProvider(new ViewOutlineProvider() {
               @SuppressLint({"NewApi"})
               public void getOutline(View var1, Outline var2) {
                  var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
               }
            });
         }

         ImageView var10 = this.writeButton;
         byte var18;
         if (VERSION.SDK_INT >= 21) {
            var18 = 56;
         } else {
            var18 = 60;
         }

         if (VERSION.SDK_INT >= 21) {
            var6 = 56.0F;
         } else {
            var6 = 60.0F;
         }

         var2.addView(var10, LayoutHelper.createFrame(var18, var6, 53, 0.0F, 0.0F, 16.0F, 0.0F));
         this.writeButton.setOnClickListener(new _$$Lambda$ProfileActivity$UMpMcjByin2Fxco4lDz2l5oi4sc(this));
      }

      this.needLayout();
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            ProfileActivity.this.checkListViewScroll();
            if (ProfileActivity.this.participantsMap != null && !ProfileActivity.this.usersEndReached && ProfileActivity.this.layoutManager.findLastVisibleItemPosition() > ProfileActivity.this.membersEndRow - 8) {
               ProfileActivity.this.getChannelParticipants(false);
            }

         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.updateInterfaces;
      boolean var5 = false;
      boolean var6 = false;
      byte var16 = 0;
      ProfileActivity.ListAdapter var20;
      int var24;
      if (var1 == var4) {
         var24 = (Integer)var3[0];
         RecyclerListView var17;
         if (this.user_id != 0) {
            if ((var24 & 2) != 0 || (var24 & 1) != 0 || (var24 & 4) != 0) {
               this.updateProfileData();
            }

            if ((var24 & 1024) != 0) {
               var17 = this.listView;
               if (var17 != null) {
                  RecyclerListView.Holder var18 = (RecyclerListView.Holder)var17.findViewHolderForPosition(this.phoneRow);
                  if (var18 != null) {
                     this.listAdapter.onBindViewHolder(var18, this.phoneRow);
                  }
               }
            }
         } else if (this.chat_id != 0) {
            var1 = var24 & 8192;
            if (var1 != 0 || (var24 & 8) != 0 || (var24 & 16) != 0 || (var24 & 32) != 0 || (var24 & 4) != 0) {
               this.updateOnlineCount();
               this.updateProfileData();
            }

            if (var1 != 0) {
               this.updateRowsIds();
               var20 = this.listAdapter;
               if (var20 != null) {
                  var20.notifyDataSetChanged();
               }
            }

            if ((var24 & 2) != 0 || (var24 & 1) != 0 || (var24 & 4) != 0) {
               var17 = this.listView;
               if (var17 != null) {
                  var4 = var17.getChildCount();

                  for(var1 = var16; var1 < var4; ++var1) {
                     View var21 = this.listView.getChildAt(var1);
                     if (var21 instanceof UserCell) {
                        ((UserCell)var21).update(var24);
                     }
                  }
               }
            }
         }
      } else {
         var4 = NotificationCenter.chatOnlineCountDidLoad;
         var16 = 1;
         if (var1 == var4) {
            Integer var7 = (Integer)var3[0];
            if (this.chatInfo == null) {
               return;
            }

            TLRPC.Chat var8 = this.currentChat;
            if (var8 == null || var8.id != var7) {
               return;
            }

            this.chatInfo.online_count = (Integer)var3[1];
            this.updateOnlineCount();
            this.updateProfileData();
         } else if (var1 == NotificationCenter.contactsDidLoad) {
            this.createActionBarMenu();
         } else {
            long var9;
            long var11;
            long var13;
            MessageObject var22;
            ArrayList var29;
            if (var1 == NotificationCenter.mediaDidLoad) {
               var9 = (Long)var3[0];
               if ((Integer)var3[3] == super.classGuid) {
                  var11 = this.dialog_id;
                  var13 = var11;
                  if (var11 == 0L) {
                     label418: {
                        var1 = this.user_id;
                        if (var1 == 0) {
                           var1 = this.chat_id;
                           var13 = var11;
                           if (var1 == 0) {
                              break label418;
                           }

                           var1 = -var1;
                        }

                        var13 = (long)var1;
                     }
                  }

                  var24 = (Integer)var3[4];
                  this.sharedMediaData[var24].setTotalCount((Integer)var3[1]);
                  var29 = (ArrayList)var3[2];
                  if ((int)var13 == 0) {
                     var5 = true;
                  } else {
                     var5 = false;
                  }

                  byte var19 = var16;
                  if (var9 == var13) {
                     var19 = 0;
                  }

                  if (!var29.isEmpty()) {
                     this.sharedMediaData[var24].setEndReached(var19, (Boolean)var3[5]);
                  }

                  for(var2 = 0; var2 < var29.size(); ++var2) {
                     var22 = (MessageObject)var29.get(var2);
                     this.sharedMediaData[var24].addMessage(var22, var19, false, var5);
                  }
               }
            } else {
               int[] var23;
               int[] var25;
               int[] var31;
               if (var1 == NotificationCenter.mediaCountsDidLoad) {
                  var9 = (Long)var3[0];
                  var11 = this.dialog_id;
                  var13 = var11;
                  if (var11 == 0L) {
                     var1 = this.user_id;
                     if (var1 != 0) {
                        var13 = (long)var1;
                     } else {
                        var1 = this.chat_id;
                        var13 = var11;
                        if (var1 != 0) {
                           var13 = (long)(-var1);
                        }
                     }
                  }

                  if (var9 == var13 || var9 == this.mergeDialogId) {
                     var23 = (int[])var3[1];
                     if (var9 == var13) {
                        this.mediaCount = var23;
                     } else {
                        this.mediaMergeCount = var23;
                     }

                     var23 = this.lastMediaCount;
                     var31 = this.prevMediaCount;
                     System.arraycopy(var23, 0, var31, 0, var31.length);
                     var1 = 0;

                     while(true) {
                        var25 = this.lastMediaCount;
                        if (var1 >= var25.length) {
                           this.updateSharedMediaRows();
                           break;
                        }

                        label342: {
                           var23 = this.mediaCount;
                           if (var23[var1] >= 0) {
                              var31 = this.mediaMergeCount;
                              if (var31[var1] >= 0) {
                                 var25[var1] = var23[var1] + var31[var1];
                                 break label342;
                              }
                           }

                           var23 = this.mediaCount;
                           if (var23[var1] >= 0) {
                              this.lastMediaCount[var1] = var23[var1];
                           } else {
                              var23 = this.mediaMergeCount;
                              if (var23[var1] >= 0) {
                                 this.lastMediaCount[var1] = var23[var1];
                              } else {
                                 this.lastMediaCount[var1] = 0;
                              }
                           }
                        }

                        if (var9 == var13 && this.lastMediaCount[var1] != 0) {
                           DataQuery.getInstance(super.currentAccount).loadMedia(var13, 50, 0, var1, 2, super.classGuid);
                        }

                        ++var1;
                     }
                  }
               } else if (var1 == NotificationCenter.mediaCountDidLoad) {
                  var9 = (Long)var3[0];
                  var11 = this.dialog_id;
                  var13 = var11;
                  if (var11 == 0L) {
                     label419: {
                        var1 = this.user_id;
                        if (var1 == 0) {
                           var1 = this.chat_id;
                           var13 = var11;
                           if (var1 == 0) {
                              break label419;
                           }

                           var1 = -var1;
                        }

                        var13 = (long)var1;
                     }
                  }

                  if (var9 == var13 || var9 == this.mergeDialogId) {
                     var2 = (Integer)var3[3];
                     var1 = (Integer)var3[1];
                     if (var9 == var13) {
                        this.mediaCount[var2] = var1;
                     } else {
                        this.mediaMergeCount[var2] = var1;
                     }

                     label321: {
                        var31 = this.prevMediaCount;
                        var23 = this.lastMediaCount;
                        var31[var2] = var23[var2];
                        var25 = this.mediaCount;
                        if (var25[var2] >= 0) {
                           var31 = this.mediaMergeCount;
                           if (var31[var2] >= 0) {
                              var23[var2] = var25[var2] + var31[var2];
                              break label321;
                           }
                        }

                        var23 = this.mediaCount;
                        if (var23[var2] >= 0) {
                           this.lastMediaCount[var2] = var23[var2];
                        } else {
                           var23 = this.mediaMergeCount;
                           if (var23[var2] >= 0) {
                              this.lastMediaCount[var2] = var23[var2];
                           } else {
                              this.lastMediaCount[var2] = 0;
                           }
                        }
                     }

                     this.updateSharedMediaRows();
                  }
               } else if (var1 == NotificationCenter.encryptedChatCreated) {
                  if (this.creatingChat) {
                     AndroidUtilities.runOnUIThread(new _$$Lambda$ProfileActivity$90QXGDxq7odO0Woawf3z2MTXklQ(this, var3));
                  }
               } else if (var1 == NotificationCenter.encryptedChatUpdated) {
                  TLRPC.EncryptedChat var26 = (TLRPC.EncryptedChat)var3[0];
                  TLRPC.EncryptedChat var37 = this.currentEncryptedChat;
                  if (var37 != null && var26.id == var37.id) {
                     this.currentEncryptedChat = var26;
                     this.updateRowsIds();
                     var20 = this.listAdapter;
                     if (var20 != null) {
                        var20.notifyDataSetChanged();
                     }
                  }
               } else if (var1 == NotificationCenter.blockedUsersDidLoad) {
                  boolean var15 = this.userBlocked;
                  if (MessagesController.getInstance(super.currentAccount).blockedUsers.indexOfKey(this.user_id) >= 0) {
                     var5 = true;
                  }

                  this.userBlocked = var5;
                  if (var15 != this.userBlocked) {
                     this.createActionBarMenu();
                     this.updateRowsIds();
                     this.listAdapter.notifyDataSetChanged();
                  }
               } else {
                  boolean var27;
                  if (var1 == NotificationCenter.chatInfoDidLoad) {
                     TLRPC.ChatFull var38 = (TLRPC.ChatFull)var3[0];
                     if (var38.id == this.chat_id) {
                        var5 = (Boolean)var3[2];
                        TLRPC.ChatFull var32 = this.chatInfo;
                        if (var32 instanceof TLRPC.TL_channelFull && var38.participants == null && var32 != null) {
                           var38.participants = var32.participants;
                        }

                        var27 = var6;
                        if (this.chatInfo == null) {
                           var27 = var6;
                           if (var38 instanceof TLRPC.TL_channelFull) {
                              var27 = true;
                           }
                        }

                        this.chatInfo = var38;
                        if (this.mergeDialogId == 0L) {
                           var2 = this.chatInfo.migrated_from_chat_id;
                           if (var2 != 0) {
                              this.mergeDialogId = (long)(-var2);
                              DataQuery.getInstance(super.currentAccount).getMediaCount(this.mergeDialogId, 0, super.classGuid, true);
                           }
                        }

                        this.fetchUsersFromChannelInfo();
                        this.updateOnlineCount();
                        this.updateRowsIds();
                        var20 = this.listAdapter;
                        if (var20 != null) {
                           var20.notifyDataSetChanged();
                        }

                        TLRPC.Chat var35 = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
                        if (var35 != null) {
                           this.currentChat = var35;
                           this.createActionBarMenu();
                        }

                        if (this.currentChat.megagroup && (var27 || !var5)) {
                           this.getChannelParticipants(true);
                        }
                     }
                  } else if (var1 == NotificationCenter.closeChats) {
                     this.removeSelfFromStack();
                  } else if (var1 == NotificationCenter.botInfoDidLoad) {
                     TLRPC.BotInfo var36 = (TLRPC.BotInfo)var3[0];
                     if (var36.user_id == this.user_id) {
                        this.botInfo = var36;
                        this.updateRowsIds();
                        var20 = this.listAdapter;
                        if (var20 != null) {
                           var20.notifyDataSetChanged();
                        }
                     }
                  } else if (var1 == NotificationCenter.userInfoDidLoad) {
                     if ((Integer)var3[0] == this.user_id) {
                        this.userInfo = (TLRPC.UserFull)var3[1];
                        if (!this.openAnimationInProgress && this.callItem == null) {
                           this.createActionBarMenu();
                        } else {
                           this.recreateMenuAfterAnimation = true;
                        }

                        this.updateRowsIds();
                        var20 = this.listAdapter;
                        if (var20 != null) {
                           var20.notifyDataSetChanged();
                        }
                     }
                  } else if (var1 == NotificationCenter.didReceiveNewMessages) {
                     var13 = this.dialog_id;
                     if (var13 == 0L) {
                        var1 = this.user_id;
                        if (var1 == 0) {
                           var1 = -this.chat_id;
                        }

                        var13 = (long)var1;
                     }

                     if (var13 == (Long)var3[0]) {
                        if ((int)var13 == 0) {
                           var5 = true;
                        } else {
                           var5 = false;
                        }

                        var29 = (ArrayList)var3[1];

                        for(var1 = 0; var1 < var29.size(); ++var1) {
                           var22 = (MessageObject)var29.get(var1);
                           if (this.currentEncryptedChat != null) {
                              TLRPC.MessageAction var28 = var22.messageOwner.action;
                              if (var28 instanceof TLRPC.TL_messageEncryptedAction) {
                                 TLRPC.DecryptedMessageAction var30 = var28.encryptedAction;
                                 if (var30 instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                                    TLRPC.TL_decryptedMessageActionSetMessageTTL var33 = (TLRPC.TL_decryptedMessageActionSetMessageTTL)var30;
                                    ProfileActivity.ListAdapter var34 = this.listAdapter;
                                    if (var34 != null) {
                                       var34.notifyDataSetChanged();
                                    }
                                 }
                              }
                           }

                           var2 = DataQuery.getMediaType(var22.messageOwner);
                           if (var2 == -1) {
                              return;
                           }

                           this.sharedMediaData[var2].addMessage(var22, 0, true, var5);
                        }

                        this.loadMediaCounts();
                     }
                  } else if (var1 == NotificationCenter.messagesDeleted) {
                     var1 = (Integer)var3[1];
                     if (ChatObject.isChannel(this.currentChat)) {
                        if ((var1 != 0 || this.mergeDialogId == 0L) && var1 != this.currentChat.id) {
                           return;
                        }
                     } else if (var1 != 0) {
                        return;
                     }

                     ArrayList var39 = (ArrayList)var3[0];
                     var4 = var39.size();
                     var2 = 0;

                     for(var27 = false; var2 < var4; ++var2) {
                        var24 = 0;

                        while(true) {
                           MediaActivity.SharedMediaData[] var40 = this.sharedMediaData;
                           if (var24 >= var40.length) {
                              break;
                           }

                           if (var40[var24].deleteMessage((Integer)var39.get(var2), 0)) {
                              var27 = true;
                           }

                           ++var24;
                        }
                     }

                     if (var27) {
                        MediaActivity var41 = this.mediaActivity;
                        if (var41 != null) {
                           var41.updateAdapters();
                        }
                     }

                     this.loadMediaCounts();
                  }
               }
            }
         }
      }

   }

   public void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      long var5 = (Long)var2.get(0);
      Bundle var9 = new Bundle();
      var9.putBoolean("scrollToTopOnResume", true);
      int var7 = (int)var5;
      if (var7 != 0) {
         if (var7 > 0) {
            var9.putInt("user_id", var7);
         } else if (var7 < 0) {
            var9.putInt("chat_id", -var7);
         }
      } else {
         var9.putInt("enc_id", (int)(var5 >> 32));
      }

      if (MessagesController.getInstance(super.currentAccount).checkCanOpenChat(var9, var1)) {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
         this.presentFragment(new ChatActivity(var9), true);
         this.removeSelfFromStack();
         TLRPC.User var8 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         SendMessagesHelper.getInstance(super.currentAccount).sendMessage((TLRPC.User)var8, var5, (MessageObject)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
      }
   }

   public float getAnimationProgress() {
      return this.animationProgress;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI var1 = new _$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(this.listView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var9 = new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var10 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_actionBarSelectorBlue");
      ThemeDescription var11 = new ThemeDescription(this.nameTextView[1], ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_title");
      ThemeDescription var12 = new ThemeDescription(this.nameTextView[1], ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundActionBarBlue");
      ThemeDescription var13 = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_status");
      ThemeDescription var14 = new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_subtitleInProfileBlue");
      ThemeDescription var15 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var16 = this.listView;
      Paint var17 = Theme.dividerPaint;
      ThemeDescription var18 = new ThemeDescription(var16, 0, new Class[]{View.class}, var17, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var19 = new ThemeDescription(this.avatarImage, 0, (Class[])null, (Paint)null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text");
      ThemeDescription var20 = new ThemeDescription(this.avatarImage, 0, (Class[])null, (Paint)null, new Drawable[]{this.avatarDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_backgroundInProfileBlue");
      ThemeDescription var21 = new ThemeDescription(this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionIcon");
      ThemeDescription var22 = new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionBackground");
      ThemeDescription var23 = new ThemeDescription(this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_actionPressedBackground");
      ThemeDescription var24 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var25 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGreenText2");
      ThemeDescription var26 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5");
      ThemeDescription var27 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText2");
      ThemeDescription var28 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueButton");
      ThemeDescription var29 = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText");
      ThemeDescription var30 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon");
      ThemeDescription var55 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueIcon");
      ThemeDescription var31 = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var32 = new ThemeDescription(this.listView, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var33 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var34 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var35 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var36 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack");
      ThemeDescription var37 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked");
      ThemeDescription var38 = new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{UserCell.class}, new String[]{"adminTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_creatorIcon");
      ThemeDescription var39 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon");
      ThemeDescription var40 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var56 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var41 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var42 = this.listView;
      Drawable var43 = Theme.avatar_broadcastDrawable;
      Drawable var44 = Theme.avatar_savedDrawable;
      ThemeDescription var58 = new ThemeDescription(var42, 0, new Class[]{UserCell.class}, (Paint)null, new Drawable[]{var43, var44}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text");
      ThemeDescription var45 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed");
      ThemeDescription var59 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange");
      ThemeDescription var46 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet");
      ThemeDescription var57 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen");
      ThemeDescription var47 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan");
      ThemeDescription var48 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue");
      ThemeDescription var54 = new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink");
      RecyclerListView var49 = this.listView;
      int var50 = ThemeDescription.FLAG_TEXTCOLOR;
      TextPaint var51 = Theme.profile_aboutTextPaint;
      ThemeDescription var61 = new ThemeDescription(var49, var50, new Class[]{AboutLinkCell.class}, var51, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      var49 = this.listView;
      var50 = ThemeDescription.FLAG_LINKCOLOR;
      TextPaint var52 = Theme.profile_aboutTextPaint;
      ThemeDescription var60 = new ThemeDescription(var49, var50, new Class[]{AboutLinkCell.class}, var52, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText");
      RecyclerListView var53 = this.listView;
      Paint var62 = Theme.linkSelectionPaint;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, var28, var29, var30, var55, var31, var32, var33, var34, var35, var36, var37, var38, var39, var40, var56, var41, var58, var45, var59, var46, var57, var47, var48, var54, var61, var60, new ThemeDescription(var53, 0, new Class[]{AboutLinkCell.class}, var62, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkSelection"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.nameTextView[1], 0, (Class[])null, (Paint)null, new Drawable[]{Theme.profile_verifiedCheckDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_verifiedCheck"), new ThemeDescription(this.nameTextView[1], 0, (Class[])null, (Paint)null, new Drawable[]{Theme.profile_verifiedDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "profile_verifiedBackground")};
   }

   public boolean isChat() {
      boolean var1;
      if (this.chat_id != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$createView$10$ProfileActivity(TLRPC.Chat var1, View var2) {
      int var3 = this.user_id;
      int var4 = this.banFromGroup;
      TLRPC.TL_chatBannedRights var7 = var1.default_banned_rights;
      TLRPC.ChannelParticipant var5 = this.currentChannelParticipant;
      TLRPC.TL_chatBannedRights var6;
      if (var5 != null) {
         var6 = var5.banned_rights;
      } else {
         var6 = null;
      }

      ChatRightsEditActivity var8 = new ChatRightsEditActivity(var3, var4, (TLRPC.TL_chatAdminRights)null, var7, var6, 1, true, false);
      var8.setDelegate(new _$$Lambda$ProfileActivity$FOe0dD4xjkU1Fka8Vb1wiDf0EaA(this));
      this.presentFragment(var8);
   }

   // $FF: synthetic method
   public void lambda$createView$11$ProfileActivity(View var1) {
      int var3;
      if (this.user_id != 0) {
         TLRPC.User var4 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         TLRPC.UserProfilePhoto var2 = var4.photo;
         if (var2 != null && var2.photo_big != null) {
            PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
            var2 = var4.photo;
            var3 = var2.dc_id;
            if (var3 != 0) {
               var2.photo_big.dc_id = var3;
            }

            PhotoViewer.getInstance().openPhoto(var4.photo.photo_big, this.provider);
         }
      } else if (this.chat_id != 0) {
         TLRPC.Chat var5 = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
         TLRPC.ChatPhoto var6 = var5.photo;
         if (var6 != null && var6.photo_big != null) {
            PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
            var6 = var5.photo;
            var3 = var6.dc_id;
            if (var3 != 0) {
               var6.photo_big.dc_id = var3;
            }

            PhotoViewer.getInstance().openPhoto(var5.photo.photo_big, this.provider);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$12$ProfileActivity(View var1) {
      if (this.playProfileAnimation) {
         ArrayList var2 = super.parentLayout.fragmentsStack;
         if (var2.get(var2.size() - 2) instanceof ChatActivity) {
            this.finishFragment();
            return;
         }
      }

      TLRPC.User var3 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
      if (var3 != null && !(var3 instanceof TLRPC.TL_userEmpty)) {
         Bundle var4 = new Bundle();
         var4.putInt("user_id", this.user_id);
         if (!MessagesController.getInstance(super.currentAccount).checkCanOpenChat(var4, this)) {
            return;
         }

         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
         this.presentFragment(new ChatActivity(var4), true);
      }

   }

   // $FF: synthetic method
   public void lambda$createView$3$ProfileActivity(View var1, int var2, float var3, float var4) {
      if (this.getParentActivity() != null) {
         int var5 = this.photosRow;
         byte var6 = 2;
         long var7 = 0L;
         long var9;
         if (var2 != var5 && var2 != this.filesRow && var2 != this.linksRow && var2 != this.audioRow && var2 != this.voiceRow) {
            if (var2 == this.groupsInCommonRow) {
               this.presentFragment(new CommonGroupsActivity(this.user_id));
            } else {
               Bundle var20;
               if (var2 == this.settingsKeyRow) {
                  var20 = new Bundle();
                  var20.putInt("chat_id", (int)(this.dialog_id >> 32));
                  this.presentFragment(new IdenticonActivity(var20));
               } else if (var2 == this.settingsTimerRow) {
                  this.showDialog(AlertsCreator.createTTLAlert(this.getParentActivity(), this.currentEncryptedChat).create());
               } else {
                  boolean var11;
                  if (var2 == this.notificationsRow) {
                     var9 = this.dialog_id;
                     if (var9 == 0L) {
                        var2 = this.user_id;
                        if (var2 == 0) {
                           var2 = -this.chat_id;
                        }

                        var9 = (long)var2;
                     }

                     if (LocaleController.isRTL && var3 <= (float)AndroidUtilities.dp(76.0F) || !LocaleController.isRTL && var3 >= (float)(var1.getMeasuredWidth() - AndroidUtilities.dp(76.0F))) {
                        NotificationsCheckCell var21 = (NotificationsCheckCell)var1;
                        var11 = var21.isChecked() ^ true;
                        boolean var12 = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(var9);
                        StringBuilder var14;
                        Editor var27;
                        TLRPC.Dialog var28;
                        if (var11) {
                           var27 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
                           if (var12) {
                              var14 = new StringBuilder();
                              var14.append("notify2_");
                              var14.append(var9);
                              var27.remove(var14.toString());
                           } else {
                              var14 = new StringBuilder();
                              var14.append("notify2_");
                              var14.append(var9);
                              var27.putInt(var14.toString(), 0);
                           }

                           MessagesStorage.getInstance(super.currentAccount).setDialogFlags(var9, 0L);
                           var27.commit();
                           var28 = (TLRPC.Dialog)MessagesController.getInstance(super.currentAccount).dialogs_dict.get(var9);
                           if (var28 != null) {
                              var28.notify_settings = new TLRPC.TL_peerNotifySettings();
                           }

                           NotificationsController.getInstance(super.currentAccount).updateServerNotificationsSettings(var9);
                        } else {
                           var27 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
                           if (!var12) {
                              var14 = new StringBuilder();
                              var14.append("notify2_");
                              var14.append(var9);
                              var27.remove(var14.toString());
                           } else {
                              var14 = new StringBuilder();
                              var14.append("notify2_");
                              var14.append(var9);
                              var27.putInt(var14.toString(), 2);
                              var7 = 1L;
                           }

                           NotificationsController.getInstance(super.currentAccount).removeNotificationsForDialog(var9);
                           MessagesStorage.getInstance(super.currentAccount).setDialogFlags(var9, var7);
                           var27.commit();
                           var28 = (TLRPC.Dialog)MessagesController.getInstance(super.currentAccount).dialogs_dict.get(var9);
                           if (var28 != null) {
                              var28.notify_settings = new TLRPC.TL_peerNotifySettings();
                              if (var12) {
                                 var28.notify_settings.mute_until = Integer.MAX_VALUE;
                              }
                           }

                           NotificationsController.getInstance(super.currentAccount).updateServerNotificationsSettings(var9);
                        }

                        var21.setChecked(var11);
                        RecyclerListView.Holder var22 = (RecyclerListView.Holder)this.listView.findViewHolderForPosition(this.notificationsRow);
                        if (var22 != null) {
                           this.listAdapter.onBindViewHolder(var22, this.notificationsRow);
                        }

                        return;
                     }

                     AlertsCreator.showCustomNotificationsDialog(this, var9, -1, (ArrayList)null, super.currentAccount, new _$$Lambda$ProfileActivity$KjKoxTLNU0USdgocPZA7qT_wkmY(this));
                  } else if (var2 == this.startSecretChatRow) {
                     AlertDialog.Builder var23 = new AlertDialog.Builder(this.getParentActivity());
                     var23.setTitle(LocaleController.getString("AreYouSureSecretChatTitle", 2131558697));
                     var23.setMessage(LocaleController.getString("AreYouSureSecretChat", 2131558696));
                     var23.setPositiveButton(LocaleController.getString("Start", 2131560802), new _$$Lambda$ProfileActivity$kx_V4hZeGc73ZkCyzsaLus8IpoA(this));
                     var23.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     this.showDialog(var23.create());
                  } else if (var2 == this.unblockRow) {
                     MessagesController.getInstance(super.currentAccount).unblockUser(this.user_id);
                     AlertsCreator.showSimpleToast(this, LocaleController.getString("UserUnblocked", 2131561020));
                  } else if (var2 >= this.membersStartRow && var2 < this.membersEndRow) {
                     if (!this.sortedUsers.isEmpty()) {
                        var2 = ((TLRPC.ChatParticipant)this.chatInfo.participants.participants.get((Integer)this.sortedUsers.get(var2 - this.membersStartRow))).user_id;
                     } else {
                        var2 = ((TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var2 - this.membersStartRow)).user_id;
                     }

                     if (var2 == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                        return;
                     }

                     var20 = new Bundle();
                     var20.putInt("user_id", var2);
                     this.presentFragment(new ProfileActivity(var20));
                  } else if (var2 == this.addMemberRow) {
                     this.openAddMember();
                  } else if (var2 == this.usernameRow) {
                     if (this.currentChat != null) {
                        Exception var10000;
                        label191: {
                           boolean var10001;
                           Intent var24;
                           try {
                              var24 = new Intent("android.intent.action.SEND");
                              var24.setType("text/plain");
                              var11 = TextUtils.isEmpty(this.chatInfo.about);
                           } catch (Exception var18) {
                              var10000 = var18;
                              var10001 = false;
                              break label191;
                           }

                           StringBuilder var29;
                           if (!var11) {
                              try {
                                 var29 = new StringBuilder();
                                 var29.append(this.currentChat.title);
                                 var29.append("\n");
                                 var29.append(this.chatInfo.about);
                                 var29.append("\nhttps://");
                                 var29.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
                                 var29.append("/");
                                 var29.append(this.currentChat.username);
                                 var24.putExtra("android.intent.extra.TEXT", var29.toString());
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label191;
                              }
                           } else {
                              try {
                                 var29 = new StringBuilder();
                                 var29.append(this.currentChat.title);
                                 var29.append("\nhttps://");
                                 var29.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
                                 var29.append("/");
                                 var29.append(this.currentChat.username);
                                 var24.putExtra("android.intent.extra.TEXT", var29.toString());
                              } catch (Exception var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label191;
                              }
                           }

                           try {
                              this.getParentActivity().startActivityForResult(Intent.createChooser(var24, LocaleController.getString("BotShare", 2131558856)), 500);
                              return;
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                           }
                        }

                        Exception var25 = var10000;
                        FileLog.e((Throwable)var25);
                     }
                  } else if (var2 == this.leaveChannelRow) {
                     this.leaveChatPressed();
                  } else if (var2 == this.joinRow) {
                     MessagesController.getInstance(super.currentAccount).addUserToChat(this.currentChat.id, UserConfig.getInstance(super.currentAccount).getCurrentUser(), (TLRPC.ChatFull)null, 0, (String)null, this, (Runnable)null);
                     NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeSearchByActiveAction);
                  } else {
                     ChatUsersActivity var26;
                     if (var2 == this.subscribersRow) {
                        var20 = new Bundle();
                        var20.putInt("chat_id", this.chat_id);
                        var20.putInt("type", 2);
                        var26 = new ChatUsersActivity(var20);
                        var26.setInfo(this.chatInfo);
                        this.presentFragment(var26);
                     } else if (var2 == this.administratorsRow) {
                        var20 = new Bundle();
                        var20.putInt("chat_id", this.chat_id);
                        var20.putInt("type", 1);
                        var26 = new ChatUsersActivity(var20);
                        var26.setInfo(this.chatInfo);
                        this.presentFragment(var26);
                     } else if (var2 == this.blockedUsersRow) {
                        var20 = new Bundle();
                        var20.putInt("chat_id", this.chat_id);
                        var20.putInt("type", 0);
                        var26 = new ChatUsersActivity(var20);
                        var26.setInfo(this.chatInfo);
                        this.presentFragment(var26);
                     } else {
                        this.processOnClickOrPress(var2);
                     }
                  }
               }
            }
         } else {
            if (var2 == this.photosRow) {
               var6 = 0;
            } else if (var2 == this.filesRow) {
               var6 = 1;
            } else if (var2 == this.linksRow) {
               var6 = 3;
            } else if (var2 == this.audioRow) {
               var6 = 4;
            }

            Bundle var13 = new Bundle();
            var2 = this.user_id;
            if (var2 != 0) {
               var9 = this.dialog_id;
               if (var9 == 0L) {
                  var9 = (long)var2;
               }

               var13.putLong("dialog_id", var9);
            } else {
               var13.putLong("dialog_id", (long)(-this.chat_id));
            }

            int[] var19 = new int[5];
            System.arraycopy(this.lastMediaCount, 0, var19, 0, var19.length);
            this.mediaActivity = new MediaActivity(var13, var19, this.sharedMediaData, var6);
            this.mediaActivity.setChatInfo(this.chatInfo);
            this.presentFragment(this.mediaActivity);
         }

      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$6$ProfileActivity(View var1, int var2) {
      if (var2 >= this.membersStartRow && var2 < this.membersEndRow) {
         if (this.getParentActivity() == null) {
            return false;
         } else {
            TLRPC.ChatParticipant var13;
            if (!this.sortedUsers.isEmpty()) {
               var13 = (TLRPC.ChatParticipant)this.chatInfo.participants.participants.get((Integer)this.sortedUsers.get(var2 - this.membersStartRow));
            } else {
               var13 = (TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var2 - this.membersStartRow);
            }

            TLRPC.User var3 = MessagesController.getInstance(super.currentAccount).getUser(var13.user_id);
            if (var3 != null && var13.user_id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
               this.selectedUser = var13.user_id;
               TLRPC.ChannelParticipant var4;
               boolean var5;
               boolean var6;
               boolean var7;
               boolean var8;
               if (ChatObject.isChannel(this.currentChat)) {
                  var4 = ((TLRPC.TL_chatChannelParticipant)var13).channelParticipant;
                  MessagesController.getInstance(super.currentAccount).getUser(var13.user_id);
                  var5 = ChatObject.canAddAdmins(this.currentChat);
                  if (!ChatObject.canBlockUsers(this.currentChat) || (var4 instanceof TLRPC.TL_channelParticipantAdmin || var4 instanceof TLRPC.TL_channelParticipantCreator) && !var4.can_edit) {
                     var6 = false;
                  } else {
                     var6 = true;
                  }

                  var7 = var4 instanceof TLRPC.TL_channelParticipantAdmin;
                  var8 = var6;
               } else {
                  TLRPC.Chat var16 = this.currentChat;
                  if (var16.creator || var13 instanceof TLRPC.TL_chatParticipant && (ChatObject.canBlockUsers(var16) || var13.inviter_id == UserConfig.getInstance(super.currentAccount).getClientUserId())) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var5 = this.currentChat.creator;
                  var7 = var13 instanceof TLRPC.TL_chatParticipantAdmin;
                  var4 = null;
                  var8 = var5;
               }

               ArrayList var9 = new ArrayList();
               ArrayList var10 = new ArrayList();
               ArrayList var11 = new ArrayList();
               if (var5) {
                  String var12;
                  if (var7) {
                     var2 = 2131559317;
                     var12 = "EditAdminRights";
                  } else {
                     var2 = 2131560731;
                     var12 = "SetAsAdmin";
                  }

                  var9.add(LocaleController.getString(var12, var2));
                  var10.add(2131165271);
                  var11.add(0);
               }

               if (var8) {
                  var9.add(LocaleController.getString("ChangePermissions", 2131558910));
                  var10.add(2131165273);
                  var11.add(1);
               }

               boolean var15;
               if (var6) {
                  var9.add(LocaleController.getString("KickFromGroup", 2131559714));
                  var10.add(2131165274);
                  var11.add(2);
                  var15 = true;
               } else {
                  var15 = false;
               }

               if (var9.isEmpty()) {
                  return false;
               } else {
                  AlertDialog.Builder var17 = new AlertDialog.Builder(this.getParentActivity());
                  var17.setItems((CharSequence[])var9.toArray(new CharSequence[0]), AndroidUtilities.toIntArray(var10), new _$$Lambda$ProfileActivity$iWlc9ep_wbwBIz7hlp_XPY9A0O0(this, var11, var4, var13, var3));
                  AlertDialog var14 = var17.create();
                  this.showDialog(var14);
                  if (var15) {
                     var14.setItemColor(var9.size() - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
                  }

                  return true;
               }
            } else {
               return false;
            }
         }
      } else {
         return this.processOnClickOrPress(var2);
      }
   }

   // $FF: synthetic method
   public void lambda$createView$8$ProfileActivity(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ProfileActivity$l9cktgJWqXuDhbbfkCq09NYKvPo(this, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$21$ProfileActivity(Object[] var1) {
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
      TLRPC.EncryptedChat var3 = (TLRPC.EncryptedChat)var1[0];
      Bundle var2 = new Bundle();
      var2.putInt("enc_id", var3.id);
      this.presentFragment(new ChatActivity(var2), true);
   }

   // $FF: synthetic method
   public void lambda$getChannelParticipants$19$ProfileActivity(TLRPC.TL_channels_getParticipants var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ProfileActivity$7uDNx35_ce6bIaHclXz2o2GdZ6I(this, var4, var3, var1), (long)var2);
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$23$ProfileActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof UserCell) {
               ((UserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$leaveChatPressed$17$ProfileActivity(boolean var1) {
      this.playProfileAnimation = false;
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats);
      this.finishFragment();
      NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.needDeleteDialog, (long)(-this.currentChat.id), null, this.currentChat, var1);
   }

   // $FF: synthetic method
   public void lambda$null$1$ProfileActivity(int var1) {
      this.listAdapter.notifyItemChanged(this.notificationsRow);
   }

   // $FF: synthetic method
   public void lambda$null$18$ProfileActivity(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_channels_getParticipants var3) {
      if (var1 == null) {
         TLRPC.TL_channels_channelParticipants var5 = (TLRPC.TL_channels_channelParticipants)var2;
         MessagesController.getInstance(super.currentAccount).putUsers(var5.users, false);
         if (var5.users.size() < 200) {
            this.usersEndReached = true;
         }

         if (var3.offset == 0) {
            this.participantsMap.clear();
            this.chatInfo.participants = new TLRPC.TL_chatParticipants();
            MessagesStorage.getInstance(super.currentAccount).putUsersAndChats(var5.users, (ArrayList)null, true, true);
            MessagesStorage.getInstance(super.currentAccount).updateChannelUsers(this.chat_id, var5.participants);
         }

         for(int var4 = 0; var4 < var5.participants.size(); ++var4) {
            TLRPC.TL_chatChannelParticipant var7 = new TLRPC.TL_chatChannelParticipant();
            var7.channelParticipant = (TLRPC.ChannelParticipant)var5.participants.get(var4);
            TLRPC.ChannelParticipant var8 = var7.channelParticipant;
            var7.inviter_id = var8.inviter_id;
            var7.user_id = var8.user_id;
            var7.date = var8.date;
            if (this.participantsMap.indexOfKey(var7.user_id) < 0) {
               this.chatInfo.participants.participants.add(var7);
               this.participantsMap.put(var7.user_id, var7);
            }
         }
      }

      this.updateOnlineCount();
      this.loadingUsers = false;
      this.updateRowsIds();
      ProfileActivity.ListAdapter var6 = this.listAdapter;
      if (var6 != null) {
         var6.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$ProfileActivity(DialogInterface var1, int var2) {
      this.creatingChat = true;
      SecretChatHelper.getInstance(super.currentAccount).startSecretChat(this.getParentActivity(), MessagesController.getInstance(super.currentAccount).getUser(this.user_id));
   }

   // $FF: synthetic method
   public void lambda$null$4$ProfileActivity(int var1, TLRPC.User var2, TLRPC.ChatParticipant var3, TLRPC.ChannelParticipant var4, DialogInterface var5, int var6) {
      var6 = var2.id;
      TLRPC.TL_chatBannedRights var7 = null;
      TLRPC.TL_chatAdminRights var8;
      if (var4 != null) {
         var8 = var4.admin_rights;
      } else {
         var8 = null;
      }

      if (var4 != null) {
         var7 = var4.banned_rights;
      }

      this.openRightsEdit(var1, var6, var3, var8, var7);
   }

   // $FF: synthetic method
   public void lambda$null$5$ProfileActivity(ArrayList var1, TLRPC.ChannelParticipant var2, TLRPC.ChatParticipant var3, TLRPC.User var4, DialogInterface var5, int var6) {
      if ((Integer)var1.get(var6) == 2) {
         this.kickUser(this.selectedUser);
      } else {
         var6 = (Integer)var1.get(var6);
         if (var6 == 1 && (var2 instanceof TLRPC.TL_channelParticipantAdmin || var3 instanceof TLRPC.TL_chatParticipantAdmin)) {
            AlertDialog.Builder var9 = new AlertDialog.Builder(this.getParentActivity());
            var9.setTitle(LocaleController.getString("AppName", 2131558635));
            var9.setMessage(LocaleController.formatString("AdminWillBeRemoved", 2131558598, ContactsController.formatName(var4.first_name, var4.last_name)));
            var9.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ProfileActivity$M0pE8LsEZwD1lKkfGrG8ZoBFh8o(this, var6, var4, var3, var2));
            var9.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            this.showDialog(var9.create());
         } else {
            int var7 = var4.id;
            TLRPC.TL_chatAdminRights var8;
            if (var2 != null) {
               var8 = var2.admin_rights;
            } else {
               var8 = null;
            }

            TLRPC.TL_chatBannedRights var10;
            if (var2 != null) {
               var10 = var2.banned_rights;
            } else {
               var10 = null;
            }

            this.openRightsEdit(var6, var7, var3, var8, var10);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$ProfileActivity(TLObject var1) {
      this.currentChannelParticipant = ((TLRPC.TL_channels_channelParticipant)var1).participant;
   }

   // $FF: synthetic method
   public void lambda$null$9$ProfileActivity(int var1, TLRPC.TL_chatAdminRights var2, TLRPC.TL_chatBannedRights var3) {
      this.removeSelfFromStack();
   }

   // $FF: synthetic method
   public void lambda$onFragmentCreate$0$ProfileActivity(CountDownLatch var1) {
      this.currentChat = MessagesStorage.getInstance(super.currentAccount).getChat(this.chat_id);
      var1.countDown();
   }

   // $FF: synthetic method
   public void lambda$openAddMember$20$ProfileActivity(ArrayList var1, int var2) {
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TLRPC.User var5 = (TLRPC.User)var1.get(var4);
         MessagesController.getInstance(super.currentAccount).addUserToChat(this.chat_id, var5, this.chatInfo, var2, (String)null, this, (Runnable)null);
      }

   }

   // $FF: synthetic method
   public void lambda$openRightsEdit$13$ProfileActivity(int var1, TLRPC.ChatParticipant var2, int var3, TLRPC.TL_chatAdminRights var4, TLRPC.TL_chatBannedRights var5) {
      if (var1 == 0) {
         if (var2 instanceof TLRPC.TL_chatChannelParticipant) {
            TLRPC.TL_chatChannelParticipant var6 = (TLRPC.TL_chatChannelParticipant)var2;
            if (var3 == 1) {
               var6.channelParticipant = new TLRPC.TL_channelParticipantAdmin();
            } else {
               var6.channelParticipant = new TLRPC.TL_channelParticipant();
            }

            var6.channelParticipant.inviter_id = UserConfig.getInstance(super.currentAccount).getClientUserId();
            TLRPC.ChannelParticipant var12 = var6.channelParticipant;
            var12.user_id = var2.user_id;
            var12.date = var2.date;
            var12.banned_rights = var5;
            var12.admin_rights = var4;
         } else if (var2 instanceof TLRPC.ChatParticipant) {
            Object var10;
            if (var3 == 1) {
               var10 = new TLRPC.TL_chatParticipantAdmin();
            } else {
               var10 = new TLRPC.TL_chatParticipant();
            }

            ((TLRPC.ChatParticipant)var10).user_id = var2.user_id;
            ((TLRPC.ChatParticipant)var10).date = var2.date;
            ((TLRPC.ChatParticipant)var10).inviter_id = var2.inviter_id;
            var1 = this.chatInfo.participants.participants.indexOf(var2);
            if (var1 >= 0) {
               this.chatInfo.participants.participants.set(var1, var10);
            }
         }
      } else if (var1 == 1 && var3 == 0 && this.currentChat.megagroup) {
         TLRPC.ChatFull var11 = this.chatInfo;
         if (var11 != null && var11.participants != null) {
            int var7 = 0;
            var1 = 0;

            boolean var8;
            while(true) {
               if (var1 >= this.chatInfo.participants.participants.size()) {
                  var8 = false;
                  break;
               }

               if (((TLRPC.TL_chatChannelParticipant)this.chatInfo.participants.participants.get(var1)).channelParticipant.user_id == var2.user_id) {
                  var11 = this.chatInfo;
                  if (var11 != null) {
                     --var11.participants_count;
                  }

                  this.chatInfo.participants.participants.remove(var1);
                  var8 = true;
                  break;
               }

               ++var1;
            }

            var11 = this.chatInfo;
            boolean var9 = var8;
            if (var11 != null) {
               var9 = var8;
               if (var11.participants != null) {
                  while(true) {
                     var9 = var8;
                     if (var7 >= this.chatInfo.participants.participants.size()) {
                        break;
                     }

                     if (((TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var7)).user_id == var2.user_id) {
                        this.chatInfo.participants.participants.remove(var7);
                        var9 = true;
                        break;
                     }

                     ++var7;
                  }
               }
            }

            if (var9) {
               this.updateOnlineCount();
               this.updateRowsIds();
               this.listAdapter.notifyDataSetChanged();
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$processOnClickOrPress$14$ProfileActivity(String var1, DialogInterface var2, int var3) {
      if (var3 == 0) {
         try {
            ClipboardManager var6 = (ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard");
            StringBuilder var4 = new StringBuilder();
            var4.append("@");
            var4.append(var1);
            var6.setPrimaryClip(ClipData.newPlainText("label", var4.toString()));
            Toast.makeText(this.getParentActivity(), LocaleController.getString("TextCopied", 2131560887), 0).show();
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$processOnClickOrPress$15$ProfileActivity(ArrayList var1, TLRPC.User var2, DialogInterface var3, int var4) {
      var4 = (Integer)var1.get(var4);
      if (var4 == 0) {
         try {
            StringBuilder var7 = new StringBuilder();
            var7.append("tel:+");
            var7.append(var2.phone);
            Intent var9 = new Intent("android.intent.action.DIAL", Uri.parse(var7.toString()));
            var9.addFlags(268435456);
            this.getParentActivity().startActivityForResult(var9, 500);
         } catch (Exception var6) {
            FileLog.e((Throwable)var6);
         }
      } else if (var4 == 1) {
         try {
            ClipboardManager var8 = (ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard");
            StringBuilder var10 = new StringBuilder();
            var10.append("+");
            var10.append(var2.phone);
            var8.setPrimaryClip(ClipData.newPlainText("label", var10.toString()));
            Toast.makeText(this.getParentActivity(), LocaleController.getString("PhoneCopied", 2131560423), 0).show();
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }
      } else if (var4 == 2) {
         VoIPHelper.startCall(var2, this.getParentActivity(), this.userInfo);
      }

   }

   // $FF: synthetic method
   public void lambda$processOnClickOrPress$16$ProfileActivity(int var1, DialogInterface var2, int var3) {
      Exception var10000;
      label51: {
         boolean var10001;
         try {
            var3 = this.channelInfoRow;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label51;
         }

         String var9 = null;
         if (var1 == var3) {
            try {
               if (this.chatInfo != null) {
                  var9 = this.chatInfo.about;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label51;
            }
         } else {
            try {
               if (this.userInfo != null) {
                  var9 = this.userInfo.about;
               }
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label51;
            }
         }

         try {
            if (TextUtils.isEmpty(var9)) {
               return;
            }
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label51;
         }

         try {
            AndroidUtilities.addToClipboard(var9);
            Toast.makeText(this.getParentActivity(), LocaleController.getString("TextCopied", 2131560887), 0).show();
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Exception var10 = var10000;
      FileLog.e((Throwable)var10);
   }

   // $FF: synthetic method
   public int lambda$updateOnlineCount$22$ProfileActivity(int var1, Integer var2, Integer var3) {
      byte var4;
      int var5;
      TLRPC.User var6;
      label95: {
         TLRPC.User var8 = MessagesController.getInstance(super.currentAccount).getUser(((TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var3)).user_id);
         var6 = MessagesController.getInstance(super.currentAccount).getUser(((TLRPC.ChatParticipant)this.chatInfo.participants.participants.get(var2)).user_id);
         var4 = -110;
         if (var8 != null) {
            if (var8.bot) {
               var5 = -110;
               break label95;
            }

            if (var8.self) {
               var5 = var1 + '썐';
               break label95;
            }

            TLRPC.UserStatus var9 = var8.status;
            if (var9 != null) {
               var5 = var9.expires;
               break label95;
            }
         }

         var5 = 0;
      }

      label86: {
         if (var6 != null) {
            if (var6.bot) {
               var1 = var4;
               break label86;
            }

            if (var6.self) {
               var1 += 50000;
               break label86;
            }

            TLRPC.UserStatus var7 = var6.status;
            if (var7 != null) {
               var1 = var7.expires;
               break label86;
            }
         }

         var1 = 0;
      }

      if (var5 > 0 && var1 > 0) {
         if (var5 > var1) {
            return 1;
         } else {
            return var5 < var1 ? -1 : 0;
         }
      } else if (var5 < 0 && var1 < 0) {
         if (var5 > var1) {
            return 1;
         } else {
            return var5 < var1 ? -1 : 0;
         }
      } else if (var5 < 0 && var1 > 0 || var5 == 0 && var1 != 0) {
         return -1;
      } else {
         return (var1 >= 0 || var5 <= 0) && (var1 != 0 || var5 == 0) ? 0 : 1;
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   protected AnimatorSet onCustomTransitionAnimation(boolean var1, final Runnable var2) {
      if (this.playProfileAnimation && this.allowProfileAnimation) {
         AnimatorSet var3 = new AnimatorSet();
         var3.setDuration(180L);
         this.listView.setLayerType(2, (Paint)null);
         ActionBarMenu var4 = super.actionBar.createMenu();
         if (var4.getItem(10) == null && this.animatingItem == null) {
            this.animatingItem = var4.addItem(10, 2131165416);
         }

         int var5;
         float var6;
         ImageView var9;
         SimpleTextView var10;
         ArrayList var12;
         SimpleTextView var13;
         Property var14;
         Property var15;
         ActionBarMenuItem var16;
         if (var1) {
            LayoutParams var11 = (LayoutParams)this.onlineTextView[1].getLayoutParams();
            var11.rightMargin = (int)(AndroidUtilities.density * -21.0F + (float)AndroidUtilities.dp(8.0F));
            this.onlineTextView[1].setLayoutParams(var11);
            var5 = (int)Math.ceil((double)((float)(AndroidUtilities.displaySize.x - AndroidUtilities.dp(126.0F)) + AndroidUtilities.density * 21.0F));
            var6 = this.nameTextView[1].getPaint().measureText(this.nameTextView[1].getText().toString());
            float var7 = (float)this.nameTextView[1].getSideDrawablesSize();
            var11 = (LayoutParams)this.nameTextView[1].getLayoutParams();
            float var8 = (float)var5;
            if (var8 < var6 * 1.12F + var7) {
               var11.width = (int)Math.ceil((double)(var8 / 1.12F));
            } else {
               var11.width = -2;
            }

            this.nameTextView[1].setLayoutParams(var11);
            this.initialAnimationExtraHeight = AndroidUtilities.dp(88.0F);
            super.fragmentView.setBackgroundColor(0);
            this.setAnimationProgress(0.0F);
            var12 = new ArrayList();
            var12.add(ObjectAnimator.ofFloat(this, "animationProgress", new float[]{0.0F, 1.0F}));
            var9 = this.writeButton;
            if (var9 != null) {
               var9.setScaleX(0.2F);
               this.writeButton.setScaleY(0.2F);
               this.writeButton.setAlpha(0.0F);
               var12.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_X, new float[]{1.0F}));
               var12.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, new float[]{1.0F}));
               var12.add(ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, new float[]{1.0F}));
            }

            for(var5 = 0; var5 < 2; ++var5) {
               var13 = this.onlineTextView[var5];
               if (var5 == 0) {
                  var6 = 1.0F;
               } else {
                  var6 = 0.0F;
               }

               var13.setAlpha(var6);
               var13 = this.nameTextView[var5];
               if (var5 == 0) {
                  var6 = 1.0F;
               } else {
                  var6 = 0.0F;
               }

               var13.setAlpha(var6);
               var10 = this.onlineTextView[var5];
               var14 = View.ALPHA;
               if (var5 == 0) {
                  var6 = 0.0F;
               } else {
                  var6 = 1.0F;
               }

               var12.add(ObjectAnimator.ofFloat(var10, var14, new float[]{var6}));
               var13 = this.nameTextView[var5];
               var15 = View.ALPHA;
               if (var5 == 0) {
                  var6 = 0.0F;
               } else {
                  var6 = 1.0F;
               }

               var12.add(ObjectAnimator.ofFloat(var13, var15, new float[]{var6}));
            }

            var16 = this.animatingItem;
            if (var16 != null) {
               var16.setAlpha(1.0F);
               var12.add(ObjectAnimator.ofFloat(this.animatingItem, View.ALPHA, new float[]{0.0F}));
            }

            var16 = this.callItem;
            if (var16 != null) {
               var16.setAlpha(0.0F);
               var12.add(ObjectAnimator.ofFloat(this.callItem, View.ALPHA, new float[]{1.0F}));
            }

            var16 = this.editItem;
            if (var16 != null) {
               var16.setAlpha(0.0F);
               var12.add(ObjectAnimator.ofFloat(this.editItem, View.ALPHA, new float[]{1.0F}));
            }

            var3.playTogether(var12);
         } else {
            this.initialAnimationExtraHeight = this.extraHeight;
            var12 = new ArrayList();
            var12.add(ObjectAnimator.ofFloat(this, "animationProgress", new float[]{1.0F, 0.0F}));
            var9 = this.writeButton;
            if (var9 != null) {
               var12.add(ObjectAnimator.ofFloat(var9, View.SCALE_X, new float[]{0.2F}));
               var12.add(ObjectAnimator.ofFloat(this.writeButton, View.SCALE_Y, new float[]{0.2F}));
               var12.add(ObjectAnimator.ofFloat(this.writeButton, View.ALPHA, new float[]{0.0F}));
            }

            for(var5 = 0; var5 < 2; ++var5) {
               var13 = this.onlineTextView[var5];
               var15 = View.ALPHA;
               if (var5 == 0) {
                  var6 = 1.0F;
               } else {
                  var6 = 0.0F;
               }

               var12.add(ObjectAnimator.ofFloat(var13, var15, new float[]{var6}));
               var10 = this.nameTextView[var5];
               var14 = View.ALPHA;
               if (var5 == 0) {
                  var6 = 1.0F;
               } else {
                  var6 = 0.0F;
               }

               var12.add(ObjectAnimator.ofFloat(var10, var14, new float[]{var6}));
            }

            var16 = this.animatingItem;
            if (var16 != null) {
               var16.setAlpha(0.0F);
               var12.add(ObjectAnimator.ofFloat(this.animatingItem, View.ALPHA, new float[]{1.0F}));
            }

            var16 = this.callItem;
            if (var16 != null) {
               var16.setAlpha(1.0F);
               var12.add(ObjectAnimator.ofFloat(this.callItem, View.ALPHA, new float[]{0.0F}));
            }

            var16 = this.editItem;
            if (var16 != null) {
               var16.setAlpha(1.0F);
               var12.add(ObjectAnimator.ofFloat(this.editItem, View.ALPHA, new float[]{0.0F}));
            }

            var3.playTogether(var12);
         }

         var3.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               ProfileActivity.this.listView.setLayerType(0, (Paint)null);
               if (ProfileActivity.this.animatingItem != null) {
                  ProfileActivity.access$5500(ProfileActivity.this).createMenu().clearItems();
                  ProfileActivity.this.animatingItem = null;
               }

               var2.run();
            }
         });
         var3.setInterpolator(new DecelerateInterpolator());
         var3.getClass();
         AndroidUtilities.runOnUIThread(new _$$Lambda$V1ckApGFHcFeYW_JU7RAm0ElIv8(var3), 50L);
         return var3;
      } else {
         return null;
      }
   }

   protected void onDialogDismiss(Dialog var1) {
      RecyclerListView var2 = this.listView;
      if (var2 != null) {
         var2.invalidateViews();
      }

   }

   public boolean onFragmentCreate() {
      this.user_id = super.arguments.getInt("user_id", 0);
      this.chat_id = super.arguments.getInt("chat_id", 0);
      this.banFromGroup = super.arguments.getInt("ban_chat_id", 0);
      if (this.user_id != 0) {
         this.dialog_id = super.arguments.getLong("dialog_id", 0L);
         if (this.dialog_id != 0L) {
            this.currentEncryptedChat = MessagesController.getInstance(super.currentAccount).getEncryptedChat((int)(this.dialog_id >> 32));
         }

         TLRPC.User var1 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         if (var1 == null) {
            return false;
         }

         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.encryptedChatUpdated);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoad);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.botInfoDidLoad);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoad);
         boolean var2;
         if (MessagesController.getInstance(super.currentAccount).blockedUsers.indexOfKey(this.user_id) >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.userBlocked = var2;
         if (var1.bot) {
            this.isBot = true;
            DataQuery.getInstance(super.currentAccount).loadBotInfo(var1.id, true, super.classGuid);
         }

         this.userInfo = MessagesController.getInstance(super.currentAccount).getUserFull(this.user_id);
         MessagesController.getInstance(super.currentAccount).loadFullUser(MessagesController.getInstance(super.currentAccount).getUser(this.user_id), super.classGuid, true);
         this.participantsMap = null;
      } else {
         if (this.chat_id == 0) {
            return false;
         }

         this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
         if (this.currentChat == null) {
            CountDownLatch var6 = new CountDownLatch(1);
            MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ProfileActivity$ELtZVLkMORk4xUSfHIm0LgPewE8(this, var6));

            try {
               var6.await();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }

            if (this.currentChat == null) {
               return false;
            }

            MessagesController.getInstance(super.currentAccount).putChat(this.currentChat, true);
         }

         if (this.currentChat.megagroup) {
            this.getChannelParticipants(true);
         } else {
            this.participantsMap = null;
         }

         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
         NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatOnlineCountDidLoad);
         this.sortedUsers = new ArrayList();
         this.updateOnlineCount();
         if (ChatObject.isChannel(this.currentChat)) {
            MessagesController.getInstance(super.currentAccount).loadFullChat(this.chat_id, super.classGuid, true);
         } else if (this.chatInfo == null) {
            MessagesController.getInstance(super.currentAccount).loadChatInfo(this.chat_id, (CountDownLatch)null, false);
         }

         if (this.chatInfo == null) {
            this.chatInfo = this.getMessagesController().getChatFull(this.chat_id);
         }
      }

      this.sharedMediaData = new MediaActivity.SharedMediaData[5];
      int var3 = 0;

      while(true) {
         MediaActivity.SharedMediaData[] var7 = this.sharedMediaData;
         if (var3 >= var7.length) {
            this.loadMediaCounts();
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaCountDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaCountsDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
            this.updateRowsIds();
            return true;
         }

         var7[var3] = new MediaActivity.SharedMediaData();
         MediaActivity.SharedMediaData var8 = this.sharedMediaData[var3];
         int var4;
         if (this.dialog_id != 0L) {
            var4 = Integer.MIN_VALUE;
         } else {
            var4 = Integer.MAX_VALUE;
         }

         var8.setMaxId(0, var4);
         ++var3;
      }
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaCountDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaCountsDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
      if (this.user_id != 0) {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.encryptedChatUpdated);
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoad);
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.botInfoDidLoad);
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoad);
         MessagesController.getInstance(super.currentAccount).cancelLoadFullUser(this.user_id);
      } else if (this.chat_id != 0) {
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
         NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatOnlineCountDidLoad);
      }

   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
      if (var1 == 101) {
         TLRPC.User var4 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
         if (var4 == null) {
            return;
         }

         if (var3.length > 0 && var3[0] == 0) {
            VoIPHelper.startCall(var4, this.getParentActivity(), this.userInfo);
         } else {
            VoIPHelper.permissionDenied(this.getParentActivity(), (Runnable)null);
         }
      }

   }

   public void onResume() {
      super.onResume();
      ProfileActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      this.updateProfileData();
      this.fixLayout();
      SimpleTextView[] var2 = this.nameTextView;
      if (var2[1] != null) {
         this.setParentActivityTitle(var2[1].getText());
      }

   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && !var2 && this.playProfileAnimation && this.allowProfileAnimation) {
         this.openAnimationInProgress = false;
         if (this.recreateMenuAfterAnimation) {
            this.createActionBarMenu();
         }
      }

      NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(false);
   }

   protected void onTransitionAnimationStart(boolean var1, boolean var2) {
      if (!var2 && this.playProfileAnimation && this.allowProfileAnimation) {
         this.openAnimationInProgress = true;
      }

      NotificationCenter.getInstance(super.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoad, NotificationCenter.mediaCountsDidLoad});
      NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(true);
   }

   @Keep
   public void setAnimationProgress(float var1) {
      this.animationProgress = var1;
      this.listView.setAlpha(var1);
      this.listView.setTranslationX((float)AndroidUtilities.dp(48.0F) - (float)AndroidUtilities.dp(48.0F) * var1);
      int var2;
      if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
         var2 = 5;
      } else {
         var2 = this.chat_id;
      }

      int var3 = AvatarDrawable.getProfileBackColorForId(var2);
      int var4 = Theme.getColor("actionBarDefault");
      int var5 = Color.red(var4);
      var2 = Color.green(var4);
      var4 = Color.blue(var4);
      int var6 = (int)((float)(Color.red(var3) - var5) * var1);
      int var7 = (int)((float)(Color.green(var3) - var2) * var1);
      var3 = (int)((float)(Color.blue(var3) - var4) * var1);
      this.topView.setBackgroundColor(Color.rgb(var5 + var6, var2 + var7, var4 + var3));
      if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
         var2 = 5;
      } else {
         var2 = this.chat_id;
      }

      var6 = AvatarDrawable.getIconColorForId(var2);
      var3 = Theme.getColor("actionBarDefaultIcon");
      var5 = Color.red(var3);
      var2 = Color.green(var3);
      var7 = Color.blue(var3);
      var4 = (int)((float)(Color.red(var6) - var5) * var1);
      var3 = (int)((float)(Color.green(var6) - var2) * var1);
      var6 = (int)((float)(Color.blue(var6) - var7) * var1);
      super.actionBar.setItemsColor(Color.rgb(var5 + var4, var2 + var3, var7 + var6), false);
      var2 = Theme.getColor("profile_title");
      var6 = Theme.getColor("actionBarDefaultTitle");
      var3 = Color.red(var6);
      var5 = Color.green(var6);
      var4 = Color.blue(var6);
      var6 = Color.alpha(var6);
      var7 = (int)((float)(Color.red(var2) - var3) * var1);
      int var8 = (int)((float)(Color.green(var2) - var5) * var1);
      int var9 = (int)((float)(Color.blue(var2) - var4) * var1);
      int var10 = (int)((float)(Color.alpha(var2) - var6) * var1);

      SimpleTextView[] var11;
      for(var2 = 0; var2 < 2; ++var2) {
         var11 = this.nameTextView;
         if (var11[var2] != null) {
            var11[var2].setTextColor(Color.argb(var6 + var10, var3 + var7, var5 + var8, var4 + var9));
         }
      }

      if (this.isOnline[0]) {
         var2 = Theme.getColor("profile_status");
      } else {
         if (this.user_id != 0 || ChatObject.isChannel(this.chat_id, super.currentAccount) && !this.currentChat.megagroup) {
            var2 = 5;
         } else {
            var2 = this.chat_id;
         }

         var2 = AvatarDrawable.getProfileTextColorForId(var2);
      }

      boolean[] var14 = this.isOnline;
      byte var13 = 0;
      String var15;
      if (var14[0]) {
         var15 = "chat_status";
      } else {
         var15 = "actionBarDefaultSubtitle";
      }

      var7 = Theme.getColor(var15);
      var6 = Color.red(var7);
      var4 = Color.green(var7);
      var3 = Color.blue(var7);
      var8 = Color.alpha(var7);
      var9 = (int)((float)(Color.red(var2) - var6) * var1);
      var7 = (int)((float)(Color.green(var2) - var4) * var1);
      var10 = (int)((float)(Color.blue(var2) - var3) * var1);
      int var12 = (int)((float)(Color.alpha(var2) - var8) * var1);

      for(var2 = var13; var2 < 2; ++var2) {
         var11 = this.onlineTextView;
         if (var11[var2] != null) {
            var11[var2].setTextColor(Color.argb(var8 + var12, var6 + var9, var4 + var7, var3 + var10));
         }
      }

      this.extraHeight = (int)((float)this.initialAnimationExtraHeight * var1);
      var2 = this.user_id;
      if (var2 == 0) {
         var2 = this.chat_id;
      }

      var5 = AvatarDrawable.getProfileColorForId(var2);
      var2 = this.user_id;
      if (var2 == 0) {
         var2 = this.chat_id;
      }

      var3 = AvatarDrawable.getColorForId(var2);
      if (var5 != var3) {
         var4 = (int)((float)(Color.red(var5) - Color.red(var3)) * var1);
         var2 = (int)((float)(Color.green(var5) - Color.green(var3)) * var1);
         var5 = (int)((float)(Color.blue(var5) - Color.blue(var3)) * var1);
         this.avatarDrawable.setColor(Color.rgb(Color.red(var3) + var4, Color.green(var3) + var2, Color.blue(var3) + var5));
         this.avatarImage.invalidate();
      }

      this.needLayout();
   }

   public void setChatInfo(TLRPC.ChatFull var1) {
      this.chatInfo = var1;
      var1 = this.chatInfo;
      if (var1 != null) {
         int var2 = var1.migrated_from_chat_id;
         if (var2 != 0 && this.mergeDialogId == 0L) {
            this.mergeDialogId = (long)(-var2);
            DataQuery.getInstance(super.currentAccount).getMediaCounts(this.mergeDialogId, super.classGuid);
         }
      }

      this.fetchUsersFromChannelInfo();
   }

   public void setPlayProfileAnimation(boolean var1) {
      SharedPreferences var2 = MessagesController.getGlobalMainSettings();
      if (!AndroidUtilities.isTablet() && var2.getBoolean("view_animations", true)) {
         this.playProfileAnimation = var1;
      }

   }

   public void setUserInfo(TLRPC.UserFull var1) {
      this.userInfo = var1;
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return ProfileActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != ProfileActivity.this.infoHeaderRow && var1 != ProfileActivity.this.sharedHeaderRow && var1 != ProfileActivity.this.membersHeaderRow) {
            if (var1 != ProfileActivity.this.phoneRow && var1 != ProfileActivity.this.usernameRow) {
               if (var1 != ProfileActivity.this.userInfoRow && var1 != ProfileActivity.this.channelInfoRow) {
                  if (var1 != ProfileActivity.this.settingsTimerRow && var1 != ProfileActivity.this.settingsKeyRow && var1 != ProfileActivity.this.photosRow && var1 != ProfileActivity.this.filesRow && var1 != ProfileActivity.this.linksRow && var1 != ProfileActivity.this.audioRow && var1 != ProfileActivity.this.voiceRow && var1 != ProfileActivity.this.groupsInCommonRow && var1 != ProfileActivity.this.startSecretChatRow && var1 != ProfileActivity.this.subscribersRow && var1 != ProfileActivity.this.administratorsRow && var1 != ProfileActivity.this.blockedUsersRow && var1 != ProfileActivity.this.leaveChannelRow && var1 != ProfileActivity.this.addMemberRow && var1 != ProfileActivity.this.joinRow && var1 != ProfileActivity.this.unblockRow) {
                     if (var1 == ProfileActivity.this.notificationsDividerRow) {
                        return 5;
                     } else if (var1 == ProfileActivity.this.notificationsRow) {
                        return 6;
                     } else if (var1 != ProfileActivity.this.infoSectionRow && var1 != ProfileActivity.this.sharedSectionRow && var1 != ProfileActivity.this.lastSectionRow && var1 != ProfileActivity.this.membersSectionRow && var1 != ProfileActivity.this.settingsSectionRow) {
                        if (var1 >= ProfileActivity.this.membersStartRow && var1 < ProfileActivity.this.membersEndRow) {
                           return 8;
                        } else {
                           return var1 == ProfileActivity.this.emptyRow ? 11 : 0;
                        }
                     } else {
                        return 7;
                     }
                  } else {
                     return 4;
                  }
               } else {
                  return 3;
               }
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3 = true;
         if (var2 == 1 || var2 == 5 || var2 == 7 || var2 == 9 || var2 == 10 || var2 == 11) {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         UserCell var4 = null;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;
         boolean var11 = false;
         boolean var12 = false;
         boolean var13 = false;
         boolean var14 = false;
         boolean var15 = false;
         boolean var16 = false;
         boolean var17 = false;
         boolean var18 = false;
         boolean var19 = true;
         boolean var20 = true;
         TLRPC.User var26;
         String var30;
         switch(var3) {
         case 1:
            HeaderCell var38 = (HeaderCell)var1.itemView;
            if (var2 == ProfileActivity.this.infoHeaderRow) {
               if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup && ProfileActivity.this.channelInfoRow != -1) {
                  var38.setText(LocaleController.getString("ReportChatDescription", 2131560570));
               } else {
                  var38.setText(LocaleController.getString("Info", 2131559665));
               }
            } else if (var2 == ProfileActivity.this.sharedHeaderRow) {
               var38.setText(LocaleController.getString("SharedContent", 2131560761));
            } else if (var2 == ProfileActivity.this.membersHeaderRow) {
               var38.setText(LocaleController.getString("ChannelMembers", 2131558962));
            }
            break;
         case 2:
            TextDetailCell var35 = (TextDetailCell)var1.itemView;
            if (var2 == ProfileActivity.this.phoneRow) {
               var26 = MessagesController.getInstance(ProfileActivity.access$6700(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
               if (!TextUtils.isEmpty(var26.phone)) {
                  PhoneFormat var42 = PhoneFormat.getInstance();
                  StringBuilder var41 = new StringBuilder();
                  var41.append("+");
                  var41.append(var26.phone);
                  var30 = var42.format(var41.toString());
               } else {
                  var30 = LocaleController.getString("PhoneHidden", 2131560424);
               }

               var35.setTextAndValue(var30, LocaleController.getString("PhoneMobile", 2131560427), false);
            } else if (var2 == ProfileActivity.this.usernameRow) {
               StringBuilder var37;
               if (ProfileActivity.this.user_id != 0) {
                  TLRPC.User var43 = MessagesController.getInstance(ProfileActivity.access$6900(ProfileActivity.this)).getUser(ProfileActivity.this.user_id);
                  if (var43 != null && !TextUtils.isEmpty(var43.username)) {
                     var37 = new StringBuilder();
                     var37.append("@");
                     var37.append(var43.username);
                     var30 = var37.toString();
                  } else {
                     var30 = "-";
                  }

                  var35.setTextAndValue(var30, LocaleController.getString("Username", 2131561021), false);
               } else if (ProfileActivity.this.currentChat != null) {
                  TLRPC.Chat var44 = MessagesController.getInstance(ProfileActivity.access$7000(ProfileActivity.this)).getChat(ProfileActivity.this.chat_id);
                  var37 = new StringBuilder();
                  var37.append(MessagesController.getInstance(ProfileActivity.access$7100(ProfileActivity.this)).linkPrefix);
                  var37.append("/");
                  var37.append(var44.username);
                  var35.setTextAndValue(var37.toString(), LocaleController.getString("InviteLink", 2131559679), false);
               }
            }
            break;
         case 3:
            AboutLinkCell var34 = (AboutLinkCell)var1.itemView;
            if (var2 == ProfileActivity.this.userInfoRow) {
               var34.setTextAndValue(ProfileActivity.this.userInfo.about, LocaleController.getString("UserBio", 2131560987), ProfileActivity.this.isBot);
            } else if (var2 == ProfileActivity.this.channelInfoRow) {
               for(var30 = ProfileActivity.this.chatInfo.about; var30.contains("\n\n\n"); var30 = var30.replace("\n\n\n", "\n\n")) {
               }

               var34.setText(var30, true);
            }
            break;
         case 4:
            TextCell var33 = (TextCell)var1.itemView;
            var33.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
            var33.setTag("windowBackgroundWhiteBlackText");
            String var40;
            if (var2 == ProfileActivity.this.photosRow) {
               var30 = LocaleController.getString("SharedPhotosAndVideos", 2131560769);
               var40 = String.format("%d", ProfileActivity.this.lastMediaCount[0]);
               var20 = var18;
               if (var2 != ProfileActivity.this.sharedSectionRow - 1) {
                  var20 = true;
               }

               var33.setTextAndValueAndIcon(var30, var40, 2131165792, var20);
            } else if (var2 == ProfileActivity.this.filesRow) {
               var30 = LocaleController.getString("FilesDataUsage", 2131559483);
               var40 = String.format("%d", ProfileActivity.this.lastMediaCount[1]);
               var20 = var5;
               if (var2 != ProfileActivity.this.sharedSectionRow - 1) {
                  var20 = true;
               }

               var33.setTextAndValueAndIcon(var30, var40, 2131165785, var20);
            } else if (var2 == ProfileActivity.this.linksRow) {
               var40 = LocaleController.getString("SharedLinks", 2131560764);
               var30 = String.format("%d", ProfileActivity.this.lastMediaCount[3]);
               var20 = var6;
               if (var2 != ProfileActivity.this.sharedSectionRow - 1) {
                  var20 = true;
               }

               var33.setTextAndValueAndIcon(var40, var30, 2131165787, var20);
            } else if (var2 == ProfileActivity.this.audioRow) {
               var40 = LocaleController.getString("SharedAudioFiles", 2131560760);
               var30 = String.format("%d", ProfileActivity.this.lastMediaCount[4]);
               var20 = var7;
               if (var2 != ProfileActivity.this.sharedSectionRow - 1) {
                  var20 = true;
               }

               var33.setTextAndValueAndIcon(var40, var30, 2131165783, var20);
            } else if (var2 == ProfileActivity.this.voiceRow) {
               var40 = LocaleController.getString("AudioAutodownload", 2131558735);
               var30 = String.format("%d", ProfileActivity.this.lastMediaCount[2]);
               var20 = var8;
               if (var2 != ProfileActivity.this.sharedSectionRow - 1) {
                  var20 = true;
               }

               var33.setTextAndValueAndIcon(var40, var30, 2131165793, var20);
            } else if (var2 == ProfileActivity.this.groupsInCommonRow) {
               var40 = LocaleController.getString("GroupsInCommonTitle", 2131559627);
               var30 = String.format("%d", ProfileActivity.this.userInfo.common_chats_count);
               var20 = var9;
               if (var2 != ProfileActivity.this.sharedSectionRow - 1) {
                  var20 = true;
               }

               var33.setTextAndValueAndIcon(var40, var30, 2131165277, var20);
            } else if (var2 == ProfileActivity.this.settingsTimerRow) {
               var2 = MessagesController.getInstance(ProfileActivity.access$8200(ProfileActivity.this)).getEncryptedChat((int)(ProfileActivity.this.dialog_id >> 32)).ttl;
               if (var2 == 0) {
                  var30 = LocaleController.getString("ShortMessageLifetimeForever", 2131560776);
               } else {
                  var30 = LocaleController.formatTTLString(var2);
               }

               var33.setTextAndValue(LocaleController.getString("MessageLifetime", 2131559846), var30, false);
            } else if (var2 == ProfileActivity.this.unblockRow) {
               var33.setText(LocaleController.getString("Unblock", 2131560932), false);
               var33.setColors((String)null, "windowBackgroundWhiteRedText5");
            } else if (var2 == ProfileActivity.this.startSecretChatRow) {
               var33.setText(LocaleController.getString("StartEncryptedChat", 2131560803), false);
               var33.setColors((String)null, "windowBackgroundWhiteGreenText2");
            } else if (var2 == ProfileActivity.this.settingsKeyRow) {
               IdenticonDrawable var36 = new IdenticonDrawable();
               var36.setEncryptedChat(MessagesController.getInstance(ProfileActivity.access$8600(ProfileActivity.this)).getEncryptedChat((int)(ProfileActivity.this.dialog_id >> 32)));
               var33.setTextAndValueDrawable(LocaleController.getString("EncryptionKey", 2131559360), var36, false);
            } else if (var2 == ProfileActivity.this.leaveChannelRow) {
               var33.setColors((String)null, "windowBackgroundWhiteRedText5");
               var33.setText(LocaleController.getString("LeaveChannel", 2131559744), false);
            } else if (var2 == ProfileActivity.this.joinRow) {
               var33.setColors((String)null, "windowBackgroundWhiteBlueText2");
               var33.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText2"));
               if (ProfileActivity.this.currentChat.megagroup) {
                  var33.setText(LocaleController.getString("ProfileJoinGroup", 2131560513), false);
               } else {
                  var33.setText(LocaleController.getString("ProfileJoinChannel", 2131560512), false);
               }
            } else if (var2 == ProfileActivity.this.subscribersRow) {
               if (ProfileActivity.this.chatInfo != null) {
                  if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup) {
                     var40 = LocaleController.getString("ChannelSubscribers", 2131559004);
                     var30 = String.format("%d", ProfileActivity.this.chatInfo.participants_count);
                     var20 = var10;
                     if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                        var20 = true;
                     }

                     var33.setTextAndValueAndIcon(var40, var30, 2131165277, var20);
                  } else {
                     var40 = LocaleController.getString("ChannelMembers", 2131558962);
                     var30 = String.format("%d", ProfileActivity.this.chatInfo.participants_count);
                     var20 = var11;
                     if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                        var20 = true;
                     }

                     var33.setTextAndValueAndIcon(var40, var30, 2131165277, var20);
                  }
               } else if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup) {
                  var30 = LocaleController.getString("ChannelSubscribers", 2131559004);
                  var20 = var12;
                  if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                     var20 = true;
                  }

                  var33.setTextAndIcon(var30, 2131165277, var20);
               } else {
                  var30 = LocaleController.getString("ChannelMembers", 2131558962);
                  var20 = var13;
                  if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                     var20 = true;
                  }

                  var33.setTextAndIcon(var30, 2131165277, var20);
               }
            } else if (var2 == ProfileActivity.this.administratorsRow) {
               if (ProfileActivity.this.chatInfo != null) {
                  var40 = LocaleController.getString("ChannelAdministrators", 2131558927);
                  var30 = String.format("%d", ProfileActivity.this.chatInfo.admins_count);
                  var20 = var14;
                  if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                     var20 = true;
                  }

                  var33.setTextAndValueAndIcon(var40, var30, 2131165271, var20);
               } else {
                  var30 = LocaleController.getString("ChannelAdministrators", 2131558927);
                  var20 = var15;
                  if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                     var20 = true;
                  }

                  var33.setTextAndIcon(var30, 2131165271, var20);
               }
            } else if (var2 == ProfileActivity.this.blockedUsersRow) {
               if (ProfileActivity.this.chatInfo != null) {
                  var40 = LocaleController.getString("ChannelBlacklist", 2131558932);
                  var30 = String.format("%d", Math.max(ProfileActivity.this.chatInfo.banned_count, ProfileActivity.this.chatInfo.kicked_count));
                  var20 = var16;
                  if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                     var20 = true;
                  }

                  var33.setTextAndValueAndIcon(var40, var30, 2131165275, var20);
               } else {
                  var30 = LocaleController.getString("ChannelBlacklist", 2131558932);
                  var20 = var17;
                  if (var2 != ProfileActivity.this.membersSectionRow - 1) {
                     var20 = true;
                  }

                  var33.setTextAndIcon(var30, 2131165275, var20);
               }
            } else if (var2 == ProfileActivity.this.addMemberRow) {
               var33.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
               if (ProfileActivity.this.chat_id > 0) {
                  var33.setTextAndIcon(LocaleController.getString("AddMember", 2131558573), 2131165272, true);
               } else {
                  var33.setTextAndIcon(LocaleController.getString("AddRecipient", 2131558582), 2131165272, true);
               }
            }
         case 5:
         default:
            break;
         case 6:
            NotificationsCheckCell var39 = (NotificationsCheckCell)var1.itemView;
            if (var2 == ProfileActivity.this.notificationsRow) {
               SharedPreferences var29 = MessagesController.getNotificationsSettings(ProfileActivity.access$9300(ProfileActivity.this));
               long var22;
               if (ProfileActivity.this.dialog_id != 0L) {
                  var22 = ProfileActivity.this.dialog_id;
               } else {
                  if (ProfileActivity.this.user_id != 0) {
                     var2 = ProfileActivity.this.user_id;
                  } else {
                     var2 = -ProfileActivity.this.chat_id;
                  }

                  var22 = (long)var2;
               }

               StringBuilder var24 = new StringBuilder();
               var24.append("custom_");
               var24.append(var22);
               var18 = var29.getBoolean(var24.toString(), false);
               var24 = new StringBuilder();
               var24.append("notify2_");
               var24.append(var22);
               var5 = var29.contains(var24.toString());
               var24 = new StringBuilder();
               var24.append("notify2_");
               var24.append(var22);
               var3 = var29.getInt(var24.toString(), 0);
               var24 = new StringBuilder();
               var24.append("notifyuntil_");
               var24.append(var22);
               var2 = var29.getInt(var24.toString(), 0);
               if (var3 == 3 && var2 != Integer.MAX_VALUE) {
                  var2 -= ConnectionsManager.getInstance(ProfileActivity.access$9400(ProfileActivity.this)).getCurrentTime();
                  if (var2 <= 0) {
                     if (var18) {
                        var30 = LocaleController.getString("NotificationsCustom", 2131560059);
                     } else {
                        var30 = LocaleController.getString("NotificationsOn", 2131560080);
                     }
                  } else {
                     if (var2 < 3600) {
                        var30 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Minutes", var2 / 60));
                     } else if (var2 < 86400) {
                        var30 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Hours", (int)Math.ceil((double)((float)var2 / 60.0F / 60.0F))));
                     } else {
                        var30 = var4;
                        if (var2 < 31536000) {
                           var30 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Days", (int)Math.ceil((double)((float)var2 / 60.0F / 60.0F / 24.0F))));
                        }
                     }

                     var20 = false;
                  }
               } else {
                  if (var3 == 0) {
                     if (var5) {
                        var20 = var19;
                     } else {
                        var20 = NotificationsController.getInstance(ProfileActivity.access$9500(ProfileActivity.this)).isGlobalNotificationsEnabled(var22);
                     }
                  } else if (var3 == 1) {
                     var20 = var19;
                  } else {
                     var20 = false;
                  }

                  if (var20 && var18) {
                     var30 = LocaleController.getString("NotificationsCustom", 2131560059);
                  } else {
                     if (var20) {
                        var2 = 2131560080;
                        var30 = "NotificationsOn";
                     } else {
                        var2 = 2131560078;
                        var30 = "NotificationsOff";
                     }

                     var30 = LocaleController.getString(var30, var2);
                  }
               }

               String var32 = var30;
               if (var30 == null) {
                  var32 = LocaleController.getString("NotificationsOff", 2131560078);
               }

               var39.setTextAndValueAndCheck(LocaleController.getString("Notifications", 2131560055), var32, var20, false);
            }
            break;
         case 7:
            View var31 = var1.itemView;
            var31.setTag(var2);
            Drawable var27;
            if (var2 == ProfileActivity.this.infoSectionRow && ProfileActivity.this.sharedSectionRow == -1 && ProfileActivity.this.lastSectionRow == -1 && ProfileActivity.this.settingsSectionRow == -1 || var2 == ProfileActivity.this.sharedSectionRow && ProfileActivity.this.lastSectionRow == -1 || var2 == ProfileActivity.this.lastSectionRow || var2 == ProfileActivity.this.membersSectionRow && ProfileActivity.this.lastSectionRow == -1 && (ProfileActivity.this.sharedSectionRow == -1 || ProfileActivity.this.membersSectionRow > ProfileActivity.this.sharedSectionRow)) {
               var27 = Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow");
            } else {
               var27 = Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow");
            }

            CombinedDrawable var28 = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), var27);
            var28.setFullsize(true);
            var31.setBackgroundDrawable(var28);
            break;
         case 8:
            var4 = (UserCell)var1.itemView;
            TLRPC.ChatParticipant var25;
            if (!ProfileActivity.this.sortedUsers.isEmpty()) {
               var25 = (TLRPC.ChatParticipant)ProfileActivity.this.chatInfo.participants.participants.get((Integer)ProfileActivity.this.sortedUsers.get(var2 - ProfileActivity.this.membersStartRow));
            } else {
               var25 = (TLRPC.ChatParticipant)ProfileActivity.this.chatInfo.participants.participants.get(var2 - ProfileActivity.this.membersStartRow);
            }

            if (var25 != null) {
               if (var25 instanceof TLRPC.TL_chatChannelParticipant) {
                  TLRPC.ChannelParticipant var21 = ((TLRPC.TL_chatChannelParticipant)var25).channelParticipant;
                  if (var21 instanceof TLRPC.TL_channelParticipantCreator) {
                     var4.setIsAdmin(1);
                  } else if (var21 instanceof TLRPC.TL_channelParticipantAdmin) {
                     var4.setIsAdmin(2);
                  } else {
                     var4.setIsAdmin(0);
                  }
               } else if (var25 instanceof TLRPC.TL_chatParticipantCreator) {
                  var4.setIsAdmin(1);
               } else if (var25 instanceof TLRPC.TL_chatParticipantAdmin) {
                  var4.setIsAdmin(2);
               } else {
                  var4.setIsAdmin(0);
               }

               var26 = MessagesController.getInstance(ProfileActivity.access$9900(ProfileActivity.this)).getUser(var25.user_id);
               if (var2 != ProfileActivity.this.membersEndRow - 1) {
                  var20 = true;
               } else {
                  var20 = false;
               }

               var4.setData(var26, (CharSequence)null, (CharSequence)null, 0, var20);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 11) {
            switch(var2) {
            case 1:
               var4 = new HeaderCell(this.mContext, 23);
               break;
            case 2:
               var4 = new TextDetailCell(this.mContext);
               break;
            case 3:
               var4 = new AboutLinkCell(this.mContext) {
                  protected void didPressUrl(String var1) {
                     if (var1.startsWith("@")) {
                        MessagesController.getInstance(ProfileActivity.access$5600(ProfileActivity.this)).openByUserName(var1.substring(1), ProfileActivity.this, 0);
                     } else if (var1.startsWith("#")) {
                        DialogsActivity var2 = new DialogsActivity((Bundle)null);
                        var2.setSearchString(var1);
                        ProfileActivity.this.presentFragment(var2);
                     } else if (var1.startsWith("/") && ProfileActivity.access$5700(ProfileActivity.this).fragmentsStack.size() > 1) {
                        BaseFragment var3 = (BaseFragment)ProfileActivity.access$5900(ProfileActivity.this).fragmentsStack.get(ProfileActivity.access$5800(ProfileActivity.this).fragmentsStack.size() - 2);
                        if (var3 instanceof ChatActivity) {
                           ProfileActivity.this.finishFragment();
                           ((ChatActivity)var3).chatActivityEnterView.setCommand((MessageObject)null, var1, false, false);
                        }
                     }

                  }
               };
               break;
            case 4:
               var4 = new TextCell(this.mContext);
               break;
            case 5:
               var4 = new DividerCell(this.mContext);
               ((View)var4).setPadding(AndroidUtilities.dp(20.0F), AndroidUtilities.dp(4.0F), 0, 0);
               break;
            case 6:
               var4 = new NotificationsCheckCell(this.mContext, 23, 70);
               break;
            case 7:
               var4 = new ShadowSectionCell(this.mContext);
               break;
            case 8:
               Context var3 = this.mContext;
               byte var5;
               if (ProfileActivity.this.addMemberRow == -1) {
                  var5 = 9;
               } else {
                  var5 = 6;
               }

               var4 = new UserCell(var3, var5, 0, true);
               break;
            default:
               var4 = null;
            }
         } else {
            var4 = new EmptyCell(this.mContext, 36);
         }

         ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var4);
      }
   }

   private class TopView extends View {
      private int currentColor;
      private Paint paint = new Paint();

      public TopView(Context var2) {
         super(var2);
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.getMeasuredHeight() - AndroidUtilities.dp(91.0F);
         var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)(ProfileActivity.this.extraHeight + var2), this.paint);
         if (ProfileActivity.access$700(ProfileActivity.this) != null) {
            ProfileActivity.access$800(ProfileActivity.this).drawHeaderShadow(var1, var2 + ProfileActivity.this.extraHeight);
         }

      }

      protected void onMeasure(int var1, int var2) {
         var2 = MeasureSpec.getSize(var1);
         int var3 = ActionBar.getCurrentActionBarHeight();
         if (ProfileActivity.access$500(ProfileActivity.this).getOccupyStatusBar()) {
            var1 = AndroidUtilities.statusBarHeight;
         } else {
            var1 = 0;
         }

         this.setMeasuredDimension(var2, var3 + var1 + AndroidUtilities.dp(91.0F));
      }

      public void setBackgroundColor(int var1) {
         if (var1 != this.currentColor) {
            this.paint.setColor(var1);
            this.invalidate();
         }

      }
   }
}
