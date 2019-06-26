package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.MediaActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;

public class ChatAvatarContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
   private AvatarDrawable avatarDrawable = new AvatarDrawable();
   private BackupImageView avatarImageView;
   private int currentAccount;
   private int currentConnectionState;
   private boolean[] isOnline;
   private CharSequence lastSubtitle;
   private boolean occupyStatusBar;
   private int onlineCount;
   private ChatActivity parentFragment;
   private StatusDrawable[] statusDrawables = new StatusDrawable[5];
   private SimpleTextView subtitleTextView;
   private ImageView timeItem;
   private TimerDrawable timerDrawable;
   private SimpleTextView titleTextView;

   public ChatAvatarContainer(Context var1, ChatActivity var2, boolean var3) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.occupyStatusBar = true;
      this.isOnline = new boolean[1];
      this.onlineCount = -1;
      this.parentFragment = var2;
      this.avatarImageView = new BackupImageView(var1);
      this.avatarImageView.setRoundRadius(AndroidUtilities.dp(21.0F));
      this.addView(this.avatarImageView);
      this.titleTextView = new SimpleTextView(var1);
      this.titleTextView.setTextColor(Theme.getColor("actionBarDefaultTitle"));
      this.titleTextView.setTextSize(18);
      this.titleTextView.setGravity(3);
      this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.titleTextView.setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3F));
      this.addView(this.titleTextView);
      this.subtitleTextView = new SimpleTextView(var1);
      this.subtitleTextView.setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
      this.subtitleTextView.setTag("actionBarDefaultSubtitle");
      this.subtitleTextView.setTextSize(14);
      this.subtitleTextView.setGravity(3);
      this.addView(this.subtitleTextView);
      if (var3) {
         this.timeItem = new ImageView(var1);
         this.timeItem.setPadding(AndroidUtilities.dp(10.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(5.0F), AndroidUtilities.dp(5.0F));
         this.timeItem.setScaleType(ScaleType.CENTER);
         ImageView var7 = this.timeItem;
         TimerDrawable var5 = new TimerDrawable(var1);
         this.timerDrawable = var5;
         var7.setImageDrawable(var5);
         this.addView(this.timeItem);
         this.timeItem.setOnClickListener(new _$$Lambda$ChatAvatarContainer$AUPtAkLLMEGZaufiE0P7VnJ7Wsk(this));
         this.timeItem.setContentDescription(LocaleController.getString("SetTimer", 2131560737));
      }

      if (this.parentFragment != null) {
         this.setOnClickListener(new _$$Lambda$ChatAvatarContainer$F8krEgvfCBOEDgLZx9CeIOwCevs(this));
         TLRPC.Chat var6 = this.parentFragment.getCurrentChat();
         this.statusDrawables[0] = new TypingDotsDrawable();
         this.statusDrawables[1] = new RecordStatusDrawable();
         this.statusDrawables[2] = new SendingFileDrawable();
         this.statusDrawables[3] = new PlayingGameDrawable();
         this.statusDrawables[4] = new RoundStatusDrawable();
         int var4 = 0;

         while(true) {
            StatusDrawable[] var8 = this.statusDrawables;
            if (var4 >= var8.length) {
               break;
            }

            StatusDrawable var9 = var8[var4];
            if (var6 != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            var9.setIsChat(var3);
            ++var4;
         }
      }

   }

   private void setTypingAnimation(boolean var1) {
      byte var2 = 0;
      int var3 = 0;
      if (var1) {
         Exception var10000;
         label44: {
            boolean var10001;
            Integer var4;
            try {
               var4 = (Integer)MessagesController.getInstance(this.currentAccount).printingStringsTypes.get(this.parentFragment.getDialogId());
               this.subtitleTextView.setLeftDrawable(this.statusDrawables[var4]);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label44;
            }

            while(true) {
               label39: {
                  try {
                     if (var3 >= this.statusDrawables.length) {
                        return;
                     }

                     if (var3 == var4) {
                        this.statusDrawables[var3].start();
                        break label39;
                     }
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break;
                  }

                  try {
                     this.statusDrawables[var3].stop();
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break;
                  }
               }

               ++var3;
            }
         }

         Exception var8 = var10000;
         FileLog.e((Throwable)var8);
      } else {
         this.subtitleTextView.setLeftDrawable((Drawable)null);
         var3 = var2;

         while(true) {
            StatusDrawable[] var9 = this.statusDrawables;
            if (var3 >= var9.length) {
               break;
            }

            var9[var3].stop();
            ++var3;
         }
      }

   }

   private void updateCurrentConnectionState() {
      int var1 = this.currentConnectionState;
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

      if (var2 == null) {
         CharSequence var3 = this.lastSubtitle;
         if (var3 != null) {
            this.subtitleTextView.setText(var3);
            this.lastSubtitle = null;
         }
      } else {
         this.lastSubtitle = this.subtitleTextView.getText();
         this.subtitleTextView.setText(var2);
      }

   }

   public void checkAndUpdateAvatar() {
      ChatActivity var1 = this.parentFragment;
      if (var1 != null) {
         TLRPC.User var3 = var1.getCurrentUser();
         TLRPC.Chat var2 = this.parentFragment.getCurrentChat();
         if (var3 != null) {
            this.avatarDrawable.setInfo(var3);
            BackupImageView var5;
            if (UserObject.isUserSelf(var3)) {
               this.avatarDrawable.setAvatarType(2);
               var5 = this.avatarImageView;
               if (var5 != null) {
                  var5.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)var3);
               }
            } else {
               var5 = this.avatarImageView;
               if (var5 != null) {
                  var5.setImage((ImageLocation)ImageLocation.getForUser(var3, false), "50_50", (Drawable)this.avatarDrawable, (Object)var3);
               }
            }
         } else if (var2 != null) {
            this.avatarDrawable.setInfo(var2);
            BackupImageView var4 = this.avatarImageView;
            if (var4 != null) {
               var4.setImage((ImageLocation)ImageLocation.getForChat(var2, false), "50_50", (Drawable)this.avatarDrawable, (Object)var2);
            }
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.didUpdateConnectionState) {
         var1 = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
         if (this.currentConnectionState != var1) {
            this.currentConnectionState = var1;
            this.updateCurrentConnectionState();
         }
      }

   }

   public SimpleTextView getSubtitleTextView() {
      return this.subtitleTextView;
   }

   public ImageView getTimeItem() {
      return this.timeItem;
   }

   public SimpleTextView getTitleTextView() {
      return this.titleTextView;
   }

   public void hideTimeItem() {
      ImageView var1 = this.timeItem;
      if (var1 != null) {
         var1.setVisibility(8);
      }
   }

   // $FF: synthetic method
   public void lambda$new$0$ChatAvatarContainer(View var1) {
      this.parentFragment.showDialog(AlertsCreator.createTTLAlert(this.getContext(), this.parentFragment.getCurrentEncryptedChat()).create());
   }

   // $FF: synthetic method
   public void lambda$new$1$ChatAvatarContainer(View var1) {
      TLRPC.User var3 = this.parentFragment.getCurrentUser();
      TLRPC.Chat var2 = this.parentFragment.getCurrentChat();
      ProfileActivity var5;
      if (var3 != null) {
         Bundle var6 = new Bundle();
         if (UserObject.isUserSelf(var3)) {
            var6.putLong("dialog_id", this.parentFragment.getDialogId());
            MediaActivity var4 = new MediaActivity(var6, new int[]{-1, -1, -1, -1, -1});
            var4.setChatInfo(this.parentFragment.getCurrentChatInfo());
            this.parentFragment.presentFragment(var4);
         } else {
            var6.putInt("user_id", var3.id);
            if (this.timeItem != null) {
               var6.putLong("dialog_id", this.parentFragment.getDialogId());
            }

            var5 = new ProfileActivity(var6);
            var5.setUserInfo(this.parentFragment.getCurrentUserInfo());
            var5.setPlayProfileAnimation(true);
            this.parentFragment.presentFragment(var5);
         }
      } else if (var2 != null) {
         Bundle var7 = new Bundle();
         var7.putInt("chat_id", var2.id);
         var5 = new ProfileActivity(var7);
         var5.setChatInfo(this.parentFragment.getCurrentChatInfo());
         var5.setPlayProfileAnimation(true);
         this.parentFragment.presentFragment(var5);
      }

   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.parentFragment != null) {
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
         this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
         this.updateCurrentConnectionState();
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.parentFragment != null) {
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var3 = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(42.0F)) / 2;
      if (VERSION.SDK_INT >= 21 && this.occupyStatusBar) {
         var2 = AndroidUtilities.statusBarHeight;
      } else {
         var2 = 0;
      }

      var2 += var3;
      this.avatarImageView.layout(AndroidUtilities.dp(8.0F), var2, AndroidUtilities.dp(50.0F), AndroidUtilities.dp(42.0F) + var2);
      if (this.subtitleTextView.getVisibility() == 0) {
         this.titleTextView.layout(AndroidUtilities.dp(62.0F), AndroidUtilities.dp(1.3F) + var2, AndroidUtilities.dp(62.0F) + this.titleTextView.getMeasuredWidth(), this.titleTextView.getTextHeight() + var2 + AndroidUtilities.dp(1.3F));
      } else {
         this.titleTextView.layout(AndroidUtilities.dp(62.0F), AndroidUtilities.dp(11.0F) + var2, AndroidUtilities.dp(62.0F) + this.titleTextView.getMeasuredWidth(), this.titleTextView.getTextHeight() + var2 + AndroidUtilities.dp(11.0F));
      }

      ImageView var6 = this.timeItem;
      if (var6 != null) {
         var6.layout(AndroidUtilities.dp(24.0F), AndroidUtilities.dp(15.0F) + var2, AndroidUtilities.dp(58.0F), AndroidUtilities.dp(49.0F) + var2);
      }

      this.subtitleTextView.layout(AndroidUtilities.dp(62.0F), AndroidUtilities.dp(24.0F) + var2, AndroidUtilities.dp(62.0F) + this.subtitleTextView.getMeasuredWidth(), var2 + this.subtitleTextView.getTextHeight() + AndroidUtilities.dp(24.0F));
   }

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.getSize(var1);
      int var3 = var1 - AndroidUtilities.dp(70.0F);
      this.avatarImageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0F), 1073741824));
      this.titleTextView.measure(MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), Integer.MIN_VALUE));
      this.subtitleTextView.measure(MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0F), Integer.MIN_VALUE));
      ImageView var4 = this.timeItem;
      if (var4 != null) {
         var4.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0F), 1073741824));
      }

      this.setMeasuredDimension(var1, MeasureSpec.getSize(var2));
   }

   public void setChatAvatar(TLRPC.Chat var1) {
      this.avatarDrawable.setInfo(var1);
      BackupImageView var2 = this.avatarImageView;
      if (var2 != null) {
         var2.setImage((ImageLocation)ImageLocation.getForChat(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
      }

   }

   public void setOccupyStatusBar(boolean var1) {
      this.occupyStatusBar = var1;
   }

   public void setSubtitle(CharSequence var1) {
      if (this.lastSubtitle == null) {
         this.subtitleTextView.setText(var1);
      } else {
         this.lastSubtitle = var1;
      }

   }

   public void setTime(int var1) {
      TimerDrawable var2 = this.timerDrawable;
      if (var2 != null) {
         var2.setTime(var1);
      }
   }

   public void setTitle(CharSequence var1) {
      this.setTitle(var1, false);
   }

   public void setTitle(CharSequence var1, boolean var2) {
      this.titleTextView.setText(var1);
      if (var2) {
         if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
            ScamDrawable var3 = new ScamDrawable(11);
            var3.setColor(Theme.getColor("actionBarDefaultSubtitle"));
            this.titleTextView.setRightDrawable(var3);
         }
      } else if (this.titleTextView.getRightDrawable() instanceof ScamDrawable) {
         this.titleTextView.setRightDrawable((Drawable)null);
      }

   }

   public void setTitleColors(int var1, int var2) {
      this.titleTextView.setTextColor(var1);
      this.subtitleTextView.setTextColor(var2);
      this.subtitleTextView.setTag(var2);
   }

   public void setTitleIcons(Drawable var1, Drawable var2) {
      this.titleTextView.setLeftDrawable(var1);
      if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
         this.titleTextView.setRightDrawable(var2);
      }

   }

   public void setUserAvatar(TLRPC.User var1) {
      this.avatarDrawable.setInfo(var1);
      BackupImageView var2;
      if (UserObject.isUserSelf(var1)) {
         this.avatarDrawable.setAvatarType(2);
         var2 = this.avatarImageView;
         if (var2 != null) {
            var2.setImage((ImageLocation)null, (String)null, (Drawable)this.avatarDrawable, (Object)var1);
         }
      } else {
         var2 = this.avatarImageView;
         if (var2 != null) {
            var2.setImage((ImageLocation)ImageLocation.getForUser(var1, false), "50_50", (Drawable)this.avatarDrawable, (Object)var1);
         }
      }

   }

   public void showTimeItem() {
      ImageView var1 = this.timeItem;
      if (var1 != null) {
         var1.setVisibility(0);
      }
   }

   public void updateOnlineCount() {
      ChatActivity var1 = this.parentFragment;
      if (var1 != null) {
         byte var2 = 0;
         this.onlineCount = 0;
         TLRPC.ChatFull var8 = var1.getCurrentChatInfo();
         if (var8 != null) {
            int var3 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            int var4 = var2;
            if (!(var8 instanceof TLRPC.TL_chatFull)) {
               boolean var5 = var8 instanceof TLRPC.TL_channelFull;
               if (!var5 || var8.participants_count > 200 || var8.participants == null) {
                  if (var5 && var8.participants_count > 200) {
                     this.onlineCount = var8.online_count;
                  }

                  return;
               }

               var4 = var2;
            }

            for(; var4 < var8.participants.participants.size(); ++var4) {
               TLRPC.ChatParticipant var6 = (TLRPC.ChatParticipant)var8.participants.participants.get(var4);
               TLRPC.User var7 = MessagesController.getInstance(this.currentAccount).getUser(var6.user_id);
               if (var7 != null) {
                  TLRPC.UserStatus var9 = var7.status;
                  if (var9 != null && (var9.expires > var3 || var7.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) && var7.status.expires > 10000) {
                     ++this.onlineCount;
                  }
               }
            }

         }
      }
   }

   public void updateSubtitle() {
      ChatActivity var1 = this.parentFragment;
      if (var1 != null) {
         TLRPC.User var2 = var1.getCurrentUser();
         if (UserObject.isUserSelf(var2)) {
            if (this.subtitleTextView.getVisibility() != 8) {
               this.subtitleTextView.setVisibility(8);
            }

         } else {
            TLRPC.Chat var3 = this.parentFragment.getCurrentChat();
            CharSequence var4 = (CharSequence)MessagesController.getInstance(this.currentAccount).printingStrings.get(this.parentFragment.getDialogId());
            String var5 = "";
            boolean var6 = true;
            Object var9 = var4;
            if (var4 != null) {
               var9 = TextUtils.replace(var4, new String[]{"..."}, new String[]{""});
            }

            if (var9 == null || ((CharSequence)var9).length() == 0 || ChatObject.isChannel(var3) && !var3.megagroup) {
               label130: {
                  this.setTypingAnimation(false);
                  int var7;
                  if (var3 != null) {
                     TLRPC.ChatFull var10 = this.parentFragment.getCurrentChatInfo();
                     if (ChatObject.isChannel(var3)) {
                        label92: {
                           if (var10 != null) {
                              var7 = var10.participants_count;
                              if (var7 != 0) {
                                 if (var3.megagroup) {
                                    if (this.onlineCount > 1) {
                                       var9 = String.format("%s, %s", LocaleController.formatPluralString("Members", var7), LocaleController.formatPluralString("OnlineCount", Math.min(this.onlineCount, var10.participants_count)));
                                    } else {
                                       var9 = LocaleController.formatPluralString("Members", var7);
                                    }
                                 } else {
                                    int[] var13 = new int[1];
                                    String var11 = LocaleController.formatShortNumber(var7, var13);
                                    if (var3.megagroup) {
                                       var9 = LocaleController.formatPluralString("Members", var13[0]).replace(String.format("%d", var13[0]), var11);
                                    } else {
                                       var9 = LocaleController.formatPluralString("Subscribers", var13[0]).replace(String.format("%d", var13[0]), var11);
                                    }
                                 }
                                 break label92;
                              }
                           }

                           if (var3.megagroup) {
                              var9 = LocaleController.getString("Loading", 2131559768).toLowerCase();
                           } else if ((var3.flags & 64) != 0) {
                              var9 = LocaleController.getString("ChannelPublic", 2131558991).toLowerCase();
                           } else {
                              var9 = LocaleController.getString("ChannelPrivate", 2131558988).toLowerCase();
                           }
                        }
                     } else if (ChatObject.isKickedFromChat(var3)) {
                        var9 = LocaleController.getString("YouWereKicked", 2131561141);
                     } else if (ChatObject.isLeftFromChat(var3)) {
                        var9 = LocaleController.getString("YouLeft", 2131561140);
                     } else {
                        int var8 = var3.participants_count;
                        var7 = var8;
                        if (var10 != null) {
                           TLRPC.ChatParticipants var15 = var10.participants;
                           var7 = var8;
                           if (var15 != null) {
                              var7 = var15.participants.size();
                           }
                        }

                        if (this.onlineCount > 1 && var7 != 0) {
                           var9 = String.format("%s, %s", LocaleController.formatPluralString("Members", var7), LocaleController.formatPluralString("OnlineCount", this.onlineCount));
                        } else {
                           var9 = LocaleController.formatPluralString("Members", var7);
                        }
                     }
                  } else {
                     var9 = var5;
                     if (var2 != null) {
                        TLRPC.User var14 = MessagesController.getInstance(this.currentAccount).getUser(var2.id);
                        TLRPC.User var16 = var2;
                        if (var14 != null) {
                           var16 = var14;
                        }

                        if (var16.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                           var9 = LocaleController.getString("ChatYourSelf", 2131559045);
                        } else {
                           var7 = var16.id;
                           if (var7 != 333000 && var7 != 777000 && var7 != 42777) {
                              if (MessagesController.isSupportUser(var16)) {
                                 var9 = LocaleController.getString("SupportStatus", 2131560848);
                              } else {
                                 if (!var16.bot) {
                                    boolean[] var12 = this.isOnline;
                                    var12[0] = false;
                                    var9 = LocaleController.formatUserStatus(this.currentAccount, var16, var12);
                                    var6 = this.isOnline[0];
                                    break label130;
                                 }

                                 var9 = LocaleController.getString("Bot", 2131558848);
                              }
                           } else {
                              var9 = LocaleController.getString("ServiceNotifications", 2131560724);
                           }
                        }
                     }
                  }

                  var6 = false;
               }
            } else {
               this.setTypingAnimation(true);
            }

            if (this.lastSubtitle == null) {
               this.subtitleTextView.setText((CharSequence)var9);
               String var17;
               if (var6) {
                  var17 = "chat_status";
               } else {
                  var17 = "actionBarDefaultSubtitle";
               }

               this.subtitleTextView.setTextColor(Theme.getColor(var17));
               this.subtitleTextView.setTag(var17);
            } else {
               this.lastSubtitle = (CharSequence)var9;
            }

         }
      }
   }
}
