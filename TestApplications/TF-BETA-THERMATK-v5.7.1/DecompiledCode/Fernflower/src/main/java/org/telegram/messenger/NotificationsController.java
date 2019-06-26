package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.BitmapFactory.Options;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.Source;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.app.RemoteInput;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.IconCompat;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;

public class NotificationsController {
   public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
   private static volatile NotificationsController[] Instance;
   public static String OTHER_NOTIFICATIONS_CHANNEL;
   public static final int SETTING_MUTE_2_DAYS = 2;
   public static final int SETTING_MUTE_8_HOURS = 1;
   public static final int SETTING_MUTE_FOREVER = 3;
   public static final int SETTING_MUTE_HOUR = 0;
   public static final int SETTING_MUTE_UNMUTE = 4;
   public static final int TYPE_CHANNEL = 2;
   public static final int TYPE_GROUP = 0;
   public static final int TYPE_PRIVATE = 1;
   protected static AudioManager audioManager;
   public static long globalSecretChatId = -4294967296L;
   private static NotificationManagerCompat notificationManager = null;
   private static DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
   private static NotificationManager systemNotificationManager = null;
   private AlarmManager alarmManager;
   private int currentAccount;
   private ArrayList delayedPushMessages = new ArrayList();
   private LongSparseArray fcmRandomMessagesDict = new LongSparseArray();
   private boolean inChatSoundEnabled;
   private int lastBadgeCount = -1;
   private int lastButtonId = 5000;
   private int lastOnlineFromOtherDevice = 0;
   private long lastSoundOutPlay;
   private long lastSoundPlay;
   private LongSparseArray lastWearNotifiedMessageId = new LongSparseArray();
   private String launcherClassName;
   private Runnable notificationDelayRunnable;
   private WakeLock notificationDelayWakelock;
   private String notificationGroup;
   private int notificationId;
   private boolean notifyCheck = false;
   private long opened_dialog_id = 0L;
   private int personal_count = 0;
   public ArrayList popupMessages = new ArrayList();
   public ArrayList popupReplyMessages = new ArrayList();
   private LongSparseArray pushDialogs = new LongSparseArray();
   private LongSparseArray pushDialogsOverrideMention = new LongSparseArray();
   private ArrayList pushMessages = new ArrayList();
   private LongSparseArray pushMessagesDict = new LongSparseArray();
   public boolean showBadgeMessages;
   public boolean showBadgeMuted;
   public boolean showBadgeNumber;
   private LongSparseArray smartNotificationsDialogs = new LongSparseArray();
   private int soundIn;
   private boolean soundInLoaded;
   private int soundOut;
   private boolean soundOutLoaded;
   private SoundPool soundPool;
   private int soundRecord;
   private boolean soundRecordLoaded;
   private int total_unread_count = 0;
   private LongSparseArray wearNotificationsIds = new LongSparseArray();

   static {
      if (VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
         notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
         systemNotificationManager = (NotificationManager)ApplicationLoader.applicationContext.getSystemService("notification");
         checkOtherNotificationsChannel();
      }

      audioManager = (AudioManager)ApplicationLoader.applicationContext.getSystemService("audio");
      Instance = new NotificationsController[3];
   }

   public NotificationsController(int var1) {
      this.currentAccount = var1;
      this.notificationId = this.currentAccount + 1;
      StringBuilder var2 = new StringBuilder();
      var2.append("messages");
      var1 = this.currentAccount;
      Object var3;
      if (var1 == 0) {
         var3 = "";
      } else {
         var3 = var1;
      }

      var2.append(var3);
      this.notificationGroup = var2.toString();
      SharedPreferences var7 = MessagesController.getNotificationsSettings(this.currentAccount);
      this.inChatSoundEnabled = var7.getBoolean("EnableInChatSound", true);
      this.showBadgeNumber = var7.getBoolean("badgeNumber", true);
      this.showBadgeMuted = var7.getBoolean("badgeNumberMuted", false);
      this.showBadgeMessages = var7.getBoolean("badgeNumberMessages", true);
      notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
      systemNotificationManager = (NotificationManager)ApplicationLoader.applicationContext.getSystemService("notification");

      try {
         audioManager = (AudioManager)ApplicationLoader.applicationContext.getSystemService("audio");
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      try {
         this.alarmManager = (AlarmManager)ApplicationLoader.applicationContext.getSystemService("alarm");
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      try {
         this.notificationDelayWakelock = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(1, "lock");
         this.notificationDelayWakelock.setReferenceCounted(false);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      this.notificationDelayRunnable = new _$$Lambda$NotificationsController$u_XWL43v4eUkt0lAcsDPJJv0mZM(this);
   }

   // $FF: synthetic method
   static NotificationManagerCompat access$000() {
      return notificationManager;
   }

   private int addToPopupMessages(ArrayList var1, MessageObject var2, int var3, long var4, boolean var6, SharedPreferences var7) {
      int var9;
      label44: {
         if (var3 != 0) {
            StringBuilder var8 = new StringBuilder();
            var8.append("custom_");
            var8.append(var4);
            if (var7.getBoolean(var8.toString(), false)) {
               var8 = new StringBuilder();
               var8.append("popup_");
               var8.append(var4);
               var9 = var7.getInt(var8.toString(), 0);
            } else {
               var9 = 0;
            }

            if (var9 == 0) {
               if (var6) {
                  var3 = var7.getInt("popupChannel", 0);
               } else {
                  String var10;
                  if ((int)var4 < 0) {
                     var10 = "popupGroup";
                  } else {
                     var10 = "popupAll";
                  }

                  var3 = var7.getInt(var10, 0);
               }
               break label44;
            }

            if (var9 == 1) {
               var3 = 3;
               break label44;
            }

            var3 = var9;
            if (var9 != 2) {
               break label44;
            }
         }

         var3 = 0;
      }

      var9 = var3;
      if (var3 != 0) {
         var9 = var3;
         if (var2.messageOwner.to_id.channel_id != 0) {
            var9 = var3;
            if (!var2.isMegagroup()) {
               var9 = 0;
            }
         }
      }

      if (var9 != 0) {
         var1.add(0, var2);
      }

      return var9;
   }

   public static void checkOtherNotificationsChannel() {
      if (VERSION.SDK_INT >= 26) {
         SharedPreferences var0;
         if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            var0 = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            OTHER_NOTIFICATIONS_CHANNEL = var0.getString("OtherKey", "Other3");
         } else {
            var0 = null;
         }

         NotificationChannel var1 = systemNotificationManager.getNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
         NotificationChannel var2 = var1;
         if (var1 != null) {
            var2 = var1;
            if (var1.getImportance() == 0) {
               systemNotificationManager.deleteNotificationChannel(OTHER_NOTIFICATIONS_CHANNEL);
               OTHER_NOTIFICATIONS_CHANNEL = null;
               var2 = null;
            }
         }

         if (OTHER_NOTIFICATIONS_CHANNEL == null) {
            SharedPreferences var4 = var0;
            if (var0 == null) {
               var4 = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            }

            StringBuilder var3 = new StringBuilder();
            var3.append("Other");
            var3.append(Utilities.random.nextLong());
            OTHER_NOTIFICATIONS_CHANNEL = var3.toString();
            var4.edit().putString("OtherKey", OTHER_NOTIFICATIONS_CHANNEL).commit();
         }

         if (var2 == null) {
            NotificationChannel var5 = new NotificationChannel(OTHER_NOTIFICATIONS_CHANNEL, "Other", 3);
            var5.enableLights(false);
            var5.enableVibration(false);
            var5.setSound((Uri)null, (AudioAttributes)null);
            systemNotificationManager.createNotificationChannel(var5);
         }

      }
   }

   private void dismissNotification() {
      // $FF: Couldn't be decompiled
   }

   public static NotificationsController getInstance(int var0) {
      NotificationsController var1 = Instance[var0];
      NotificationsController var2 = var1;
      if (var1 == null) {
         synchronized(NotificationsController.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               NotificationsController[] var23;
               try {
                  var23 = Instance;
                  var2 = new NotificationsController(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   private int getNotifyOverride(SharedPreferences var1, long var2) {
      StringBuilder var4 = new StringBuilder();
      var4.append("notify2_");
      var4.append(var2);
      int var5 = var1.getInt(var4.toString(), -1);
      int var6 = var5;
      if (var5 == 3) {
         var4 = new StringBuilder();
         var4.append("notifyuntil_");
         var4.append(var2);
         var6 = var5;
         if (var1.getInt(var4.toString(), 0) >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
            var6 = 2;
         }
      }

      return var6;
   }

   private String getShortStringForMessage(MessageObject var1, String[] var2, boolean[] var3) {
      if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter) {
         TLRPC.Message var4 = var1.messageOwner;
         long var5 = var4.dialog_id;
         TLRPC.Peer var24 = var4.to_id;
         int var7 = var24.chat_id;
         if (var7 == 0) {
            var7 = var24.channel_id;
         }

         int var8 = var1.messageOwner.to_id.user_id;
         if (var3 != null) {
            var3[0] = true;
         }

         if (var1.isFcmMessage()) {
            if (var7 == 0 && var8 != 0) {
               if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                  if (var3 != null) {
                     var3[0] = false;
                  }

                  return LocaleController.formatString("NotificationMessageNoText", 2131560043, var1.localName);
               }

               if (VERSION.SDK_INT > 27) {
                  var2[0] = var1.localName;
               }
            } else if (var7 != 0) {
               SharedPreferences var32 = MessagesController.getNotificationsSettings(this.currentAccount);
               if (!var1.localChannel && !var32.getBoolean("EnablePreviewGroup", true) || var1.localChannel && !var32.getBoolean("EnablePreviewChannel", true)) {
                  if (var3 != null) {
                     var3[0] = false;
                  }

                  return !var1.isMegagroup() && var1.messageOwner.to_id.channel_id != 0 ? LocaleController.formatString("ChannelMessageNoText", 2131558973, var1.localName) : LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, var1.localUserName, var1.localName);
               }

               if (var1.messageOwner.to_id.channel_id != 0 && !var1.isMegagroup()) {
                  if (VERSION.SDK_INT > 27) {
                     var2[0] = var1.localName;
                  }
               } else {
                  var2[0] = var1.localUserName;
               }
            }

            return var1.messageOwner.message;
         } else {
            int var9;
            if (var8 == 0) {
               if (!var1.isFromUser() && var1.getId() >= 0) {
                  var9 = -var7;
               } else {
                  var9 = var1.messageOwner.from_id;
               }
            } else {
               var9 = var8;
               if (var8 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  var9 = var1.messageOwner.from_id;
               }
            }

            long var10 = var5;
            if (var5 == 0L) {
               if (var7 != 0) {
                  var10 = (long)(-var7);
               } else {
                  var10 = var5;
                  if (var9 != 0) {
                     var10 = (long)var9;
                  }
               }
            }

            String var29;
            label627: {
               if (var9 > 0) {
                  TLRPC.User var26 = MessagesController.getInstance(this.currentAccount).getUser(var9);
                  if (var26 != null) {
                     var29 = UserObject.getUserName(var26);
                     if (var7 != 0) {
                        var2[0] = var29;
                     } else if (VERSION.SDK_INT > 27) {
                        var2[0] = var29;
                     } else {
                        var2[0] = null;
                     }
                     break label627;
                  }
               } else {
                  TLRPC.Chat var30 = MessagesController.getInstance(this.currentAccount).getChat(-var9);
                  if (var30 != null) {
                     var29 = var30.title;
                     var2[0] = var29;
                     break label627;
                  }
               }

               var29 = null;
            }

            if (var29 == null) {
               return null;
            } else {
               TLRPC.Chat var13;
               if (var7 != 0) {
                  TLRPC.Chat var12 = MessagesController.getInstance(this.currentAccount).getChat(var7);
                  if (var12 == null) {
                     return null;
                  }

                  var13 = var12;
                  if (ChatObject.isChannel(var12)) {
                     var13 = var12;
                     if (!var12.megagroup) {
                        var13 = var12;
                        if (VERSION.SDK_INT <= 27) {
                           var2[0] = null;
                           var13 = var12;
                        }
                     }
                  }
               } else {
                  var13 = null;
               }

               if ((int)var10 == 0) {
                  var2[0] = null;
                  return LocaleController.getString("YouHaveNewMessage", 2131561139);
               } else {
                  SharedPreferences var38 = MessagesController.getNotificationsSettings(this.currentAccount);
                  boolean var33;
                  if (ChatObject.isChannel(var13) && !var13.megagroup) {
                     var33 = true;
                  } else {
                     var33 = false;
                  }

                  if (var7 == 0 && var9 != 0 && var38.getBoolean("EnablePreviewAll", true) || var7 != 0 && (!var33 && var38.getBoolean("EnablePreviewGroup", true) || var33 && var38.getBoolean("EnablePreviewChannel", true))) {
                     TLRPC.Message var20 = var1.messageOwner;
                     String var14;
                     TLRPC.Message var16;
                     TLRPC.MessageMedia var18;
                     StringBuilder var19;
                     if (var20 instanceof TLRPC.TL_messageService) {
                        var2[0] = null;
                        TLRPC.MessageAction var27 = var20.action;
                        if (var27 instanceof TLRPC.TL_messageActionUserJoined || var27 instanceof TLRPC.TL_messageActionContactSignUp) {
                           return LocaleController.formatString("NotificationContactJoined", 2131559997, var29);
                        }

                        if (var27 instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                           return LocaleController.formatString("NotificationContactNewPhoto", 2131559998, var29);
                        }

                        String var28;
                        if (var27 instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                           Object[] var10002 = new Object[2];
                           long var10006 = (long)var1.messageOwner.date;
                           var10002[0] = LocaleController.getInstance().formatterYear.format(var10006 * 1000L);
                           var10006 = (long)var1.messageOwner.date;
                           var10002[1] = LocaleController.getInstance().formatterDay.format(var10006 * 1000L);
                           var28 = LocaleController.formatString("formatDateAtTime", 2131561210, var10002);
                           String var37 = UserConfig.getInstance(this.currentAccount).getCurrentUser().first_name;
                           TLRPC.MessageAction var35 = var1.messageOwner.action;
                           return LocaleController.formatString("NotificationUnrecognizedDevice", 2131560054, var37, var28, var35.title, var35.address);
                        }

                        if (var27 instanceof TLRPC.TL_messageActionGameScore || var27 instanceof TLRPC.TL_messageActionPaymentSent) {
                           return var1.messageText.toString();
                        }

                        if (var27 instanceof TLRPC.TL_messageActionPhoneCall) {
                           TLRPC.PhoneCallDiscardReason var36 = var27.reason;
                           if (!var1.isOut() && var36 instanceof TLRPC.TL_phoneCallDiscardReasonMissed) {
                              return LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                           }
                        } else {
                           TLRPC.User var34;
                           if (var27 instanceof TLRPC.TL_messageActionChatAddUser) {
                              var8 = var27.user_id;
                              var7 = var8;
                              if (var8 == 0) {
                                 var7 = var8;
                                 if (var27.users.size() == 1) {
                                    var7 = (Integer)var1.messageOwner.action.users.get(0);
                                 }
                              }

                              if (var7 != 0) {
                                 if (var1.messageOwner.to_id.channel_id != 0 && !var13.megagroup) {
                                    return LocaleController.formatString("ChannelAddedByNotification", 2131558925, var29, var13.title);
                                 }

                                 if (var7 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    return LocaleController.formatString("NotificationInvitedToGroup", 2131560010, var29, var13.title);
                                 }

                                 var34 = MessagesController.getInstance(this.currentAccount).getUser(var7);
                                 if (var34 == null) {
                                    return null;
                                 }

                                 if (var9 == var34.id) {
                                    if (var13.megagroup) {
                                       return LocaleController.formatString("NotificationGroupAddSelfMega", 2131560003, var29, var13.title);
                                    }

                                    return LocaleController.formatString("NotificationGroupAddSelf", 2131560002, var29, var13.title);
                                 }

                                 return LocaleController.formatString("NotificationGroupAddMember", 2131560001, var29, var13.title, UserObject.getUserName(var34));
                              }

                              var19 = new StringBuilder();

                              for(var9 = 0; var9 < var1.messageOwner.action.users.size(); ++var9) {
                                 TLRPC.User var25 = MessagesController.getInstance(this.currentAccount).getUser((Integer)var1.messageOwner.action.users.get(var9));
                                 if (var25 != null) {
                                    var28 = UserObject.getUserName(var25);
                                    if (var19.length() != 0) {
                                       var19.append(", ");
                                    }

                                    var19.append(var28);
                                 }
                              }

                              return LocaleController.formatString("NotificationGroupAddMember", 2131560001, var29, var13.title, var19.toString());
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                              return LocaleController.formatString("NotificationInvitedToGroupByLink", 2131560011, var29, var13.title);
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChatEditTitle) {
                              return LocaleController.formatString("NotificationEditedGroupName", 2131559999, var29, var27.title);
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChatEditPhoto || var27 instanceof TLRPC.TL_messageActionChatDeletePhoto) {
                              if (var1.messageOwner.to_id.channel_id != 0 && !var13.megagroup) {
                                 return LocaleController.formatString("ChannelPhotoEditNotification", 2131558987, var13.title);
                              }

                              return LocaleController.formatString("NotificationEditedGroupPhoto", 2131560000, var29, var13.title);
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                              if (var27.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                 return LocaleController.formatString("NotificationGroupKickYou", 2131560008, var29, var13.title);
                              }

                              if (var1.messageOwner.action.user_id == var9) {
                                 return LocaleController.formatString("NotificationGroupLeftMember", 2131560009, var29, var13.title);
                              }

                              var34 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.action.user_id);
                              if (var34 == null) {
                                 return null;
                              }

                              return LocaleController.formatString("NotificationGroupKickMember", 2131560007, var29, var13.title, UserObject.getUserName(var34));
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChatCreate) {
                              return var1.messageText.toString();
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChannelCreate) {
                              return var1.messageText.toString();
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChatMigrateTo) {
                              return LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, var13.title);
                           }

                           if (var27 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                              return LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, var27.title);
                           }

                           if (var27 instanceof TLRPC.TL_messageActionScreenshotTaken) {
                              return var1.messageText.toString();
                           }

                           if (var27 instanceof TLRPC.TL_messageActionPinMessage) {
                              Object var15;
                              StringBuilder var17;
                              TLRPC.TL_messageMediaPoll var21;
                              TLRPC.TL_messageMediaContact var23;
                              CharSequence var31;
                              if (var13 == null || ChatObject.isChannel(var13) && !var13.megagroup) {
                                 var1 = var1.replyMessageObject;
                                 if (var1 == null) {
                                    return LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, var13.title);
                                 }

                                 if (var1.isMusic()) {
                                    return LocaleController.formatString("NotificationActionPinnedMusicChannel", 2131559978, var13.title);
                                 }

                                 if (var1.isVideo()) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                       var19 = new StringBuilder();
                                       var19.append("\ud83d\udcf9 ");
                                       var19.append(var1.messageOwner.message);
                                       var14 = var19.toString();
                                       return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var13.title, var14);
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedVideoChannel", 2131559994, var13.title);
                                 }

                                 if (var1.isGif()) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                       var19 = new StringBuilder();
                                       var19.append("\ud83c\udfac ");
                                       var19.append(var1.messageOwner.message);
                                       var14 = var19.toString();
                                       return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var13.title, var14);
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedGifChannel", 2131559974, var13.title);
                                 }

                                 if (var1.isVoice()) {
                                    return LocaleController.formatString("NotificationActionPinnedVoiceChannel", 2131559996, var13.title);
                                 }

                                 if (var1.isRoundVideo()) {
                                    return LocaleController.formatString("NotificationActionPinnedRoundChannel", 2131559986, var13.title);
                                 }

                                 if (var1.isSticker()) {
                                    var14 = var1.getStickerEmoji();
                                    if (var14 != null) {
                                       return LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", 2131559990, var13.title, var14);
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedStickerChannel", 2131559988, var13.title);
                                 }

                                 var20 = var1.messageOwner;
                                 var18 = var20.media;
                                 if (var18 instanceof TLRPC.TL_messageMediaDocument) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var20.message)) {
                                       var19 = new StringBuilder();
                                       var19.append("\ud83d\udcce ");
                                       var19.append(var1.messageOwner.message);
                                       var14 = var19.toString();
                                       return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var13.title, var14);
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedFileChannel", 2131559964, var13.title);
                                 }

                                 if (!(var18 instanceof TLRPC.TL_messageMediaGeo) && !(var18 instanceof TLRPC.TL_messageMediaVenue)) {
                                    if (var18 instanceof TLRPC.TL_messageMediaGeoLive) {
                                       return LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", 2131559972, var13.title);
                                    }

                                    if (var18 instanceof TLRPC.TL_messageMediaContact) {
                                       var23 = (TLRPC.TL_messageMediaContact)var18;
                                       return LocaleController.formatString("NotificationActionPinnedContactChannel2", 2131559962, var13.title, ContactsController.formatName(var23.first_name, var23.last_name));
                                    }

                                    if (var18 instanceof TLRPC.TL_messageMediaPoll) {
                                       var21 = (TLRPC.TL_messageMediaPoll)var18;
                                       return LocaleController.formatString("NotificationActionPinnedPollChannel2", 2131559984, var13.title, var21.poll.question);
                                    }

                                    if (var18 instanceof TLRPC.TL_messageMediaPhoto) {
                                       if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var20.message)) {
                                          var19 = new StringBuilder();
                                          var19.append("\ud83d\uddbc ");
                                          var19.append(var1.messageOwner.message);
                                          var14 = var19.toString();
                                          return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var13.title, var14);
                                       }

                                       return LocaleController.formatString("NotificationActionPinnedPhotoChannel", 2131559982, var13.title);
                                    }

                                    if (var18 instanceof TLRPC.TL_messageMediaGame) {
                                       return LocaleController.formatString("NotificationActionPinnedGameChannel", 2131559966, var13.title);
                                    }

                                    var31 = var1.messageText;
                                    if (var31 != null && var31.length() > 0) {
                                       var31 = var1.messageText;
                                       var15 = var31;
                                       if (var31.length() > 20) {
                                          var17 = new StringBuilder();
                                          var17.append(var31.subSequence(0, 20));
                                          var17.append("...");
                                          var15 = var17.toString();
                                       }

                                       return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var13.title, var15);
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, var13.title);
                                 }

                                 return LocaleController.formatString("NotificationActionPinnedGeoChannel", 2131559970, var13.title);
                              }

                              var1 = var1.replyMessageObject;
                              if (var1 == null) {
                                 return LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, var29, var13.title);
                              }

                              if (var1.isMusic()) {
                                 return LocaleController.formatString("NotificationActionPinnedMusic", 2131559977, var29, var13.title);
                              }

                              if (var1.isVideo()) {
                                 if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                    var19 = new StringBuilder();
                                    var19.append("\ud83d\udcf9 ");
                                    var19.append(var1.messageOwner.message);
                                    return LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var19.toString(), var13.title);
                                 }

                                 return LocaleController.formatString("NotificationActionPinnedVideo", 2131559993, var29, var13.title);
                              }

                              if (var1.isGif()) {
                                 if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                    var19 = new StringBuilder();
                                    var19.append("\ud83c\udfac ");
                                    var19.append(var1.messageOwner.message);
                                    return LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var19.toString(), var13.title);
                                 }

                                 return LocaleController.formatString("NotificationActionPinnedGif", 2131559973, var29, var13.title);
                              }

                              if (var1.isVoice()) {
                                 return LocaleController.formatString("NotificationActionPinnedVoice", 2131559995, var29, var13.title);
                              }

                              if (var1.isRoundVideo()) {
                                 return LocaleController.formatString("NotificationActionPinnedRound", 2131559985, var29, var13.title);
                              }

                              if (var1.isSticker()) {
                                 var14 = var1.getStickerEmoji();
                                 if (var14 != null) {
                                    return LocaleController.formatString("NotificationActionPinnedStickerEmoji", 2131559989, var29, var13.title, var14);
                                 }

                                 return LocaleController.formatString("NotificationActionPinnedSticker", 2131559987, var29, var13.title);
                              }

                              var16 = var1.messageOwner;
                              TLRPC.MessageMedia var22 = var16.media;
                              if (var22 instanceof TLRPC.TL_messageMediaDocument) {
                                 if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var16.message)) {
                                    var19 = new StringBuilder();
                                    var19.append("\ud83d\udcce ");
                                    var19.append(var1.messageOwner.message);
                                    return LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var19.toString(), var13.title);
                                 }

                                 return LocaleController.formatString("NotificationActionPinnedFile", 2131559963, var29, var13.title);
                              }

                              if (!(var22 instanceof TLRPC.TL_messageMediaGeo) && !(var22 instanceof TLRPC.TL_messageMediaVenue)) {
                                 if (var22 instanceof TLRPC.TL_messageMediaGeoLive) {
                                    return LocaleController.formatString("NotificationActionPinnedGeoLive", 2131559971, var29, var13.title);
                                 }

                                 if (var22 instanceof TLRPC.TL_messageMediaContact) {
                                    var23 = (TLRPC.TL_messageMediaContact)var22;
                                    return LocaleController.formatString("NotificationActionPinnedContact2", 2131559961, var29, var13.title, ContactsController.formatName(var23.first_name, var23.last_name));
                                 }

                                 if (var22 instanceof TLRPC.TL_messageMediaPoll) {
                                    var21 = (TLRPC.TL_messageMediaPoll)var22;
                                    return LocaleController.formatString("NotificationActionPinnedPoll2", 2131559983, var29, var13.title, var21.poll.question);
                                 }

                                 if (var22 instanceof TLRPC.TL_messageMediaPhoto) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var16.message)) {
                                       var19 = new StringBuilder();
                                       var19.append("\ud83d\uddbc ");
                                       var19.append(var1.messageOwner.message);
                                       return LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var19.toString(), var13.title);
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedPhoto", 2131559981, var29, var13.title);
                                 }

                                 if (var22 instanceof TLRPC.TL_messageMediaGame) {
                                    return LocaleController.formatString("NotificationActionPinnedGame", 2131559965, var29, var13.title);
                                 }

                                 var31 = var1.messageText;
                                 if (var31 != null && var31.length() > 0) {
                                    var31 = var1.messageText;
                                    var15 = var31;
                                    if (var31.length() > 20) {
                                       var17 = new StringBuilder();
                                       var17.append(var31.subSequence(0, 20));
                                       var17.append("...");
                                       var15 = var17.toString();
                                    }

                                    return LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var15, var13.title);
                                 }

                                 return LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, var29, var13.title);
                              }

                              return LocaleController.formatString("NotificationActionPinnedGeo", 2131559969, var29, var13.title);
                           }
                        }
                     } else {
                        if (var1.isMediaEmpty()) {
                           if (!TextUtils.isEmpty(var1.messageOwner.message)) {
                              return var1.messageOwner.message;
                           }

                           return LocaleController.getString("Message", 2131559845);
                        }

                        var16 = var1.messageOwner;
                        if (var16.media instanceof TLRPC.TL_messageMediaPhoto) {
                           if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var16.message)) {
                              var19 = new StringBuilder();
                              var19.append("\ud83d\uddbc ");
                              var19.append(var1.messageOwner.message);
                              return var19.toString();
                           }

                           if (var1.messageOwner.media.ttl_seconds != 0) {
                              return LocaleController.getString("AttachDestructingPhoto", 2131558712);
                           }

                           return LocaleController.getString("AttachPhoto", 2131558727);
                        }

                        if (var1.isVideo()) {
                           if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                              var19 = new StringBuilder();
                              var19.append("\ud83d\udcf9 ");
                              var19.append(var1.messageOwner.message);
                              return var19.toString();
                           }

                           if (var1.messageOwner.media.ttl_seconds != 0) {
                              return LocaleController.getString("AttachDestructingVideo", 2131558713);
                           }

                           return LocaleController.getString("AttachVideo", 2131558733);
                        }

                        if (var1.isGame()) {
                           return LocaleController.getString("AttachGame", 2131558715);
                        }

                        if (var1.isVoice()) {
                           return LocaleController.getString("AttachAudio", 2131558709);
                        }

                        if (var1.isRoundVideo()) {
                           return LocaleController.getString("AttachRound", 2131558729);
                        }

                        if (var1.isMusic()) {
                           return LocaleController.getString("AttachMusic", 2131558726);
                        }

                        var18 = var1.messageOwner.media;
                        if (var18 instanceof TLRPC.TL_messageMediaContact) {
                           return LocaleController.getString("AttachContact", 2131558711);
                        }

                        if (var18 instanceof TLRPC.TL_messageMediaPoll) {
                           return LocaleController.getString("Poll", 2131560467);
                        }

                        if (var18 instanceof TLRPC.TL_messageMediaGeo || var18 instanceof TLRPC.TL_messageMediaVenue) {
                           return LocaleController.getString("AttachLocation", 2131558723);
                        }

                        if (var18 instanceof TLRPC.TL_messageMediaGeoLive) {
                           return LocaleController.getString("AttachLiveLocation", 2131558721);
                        }

                        if (var18 instanceof TLRPC.TL_messageMediaDocument) {
                           if (var1.isSticker()) {
                              var14 = var1.getStickerEmoji();
                              if (var14 != null) {
                                 var19 = new StringBuilder();
                                 var19.append(var14);
                                 var19.append(" ");
                                 var19.append(LocaleController.getString("AttachSticker", 2131558730));
                                 return var19.toString();
                              }

                              return LocaleController.getString("AttachSticker", 2131558730);
                           }

                           if (var1.isGif()) {
                              if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                 var19 = new StringBuilder();
                                 var19.append("\ud83c\udfac ");
                                 var19.append(var1.messageOwner.message);
                                 return var19.toString();
                              }

                              return LocaleController.getString("AttachGif", 2131558716);
                           }

                           if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                              var19 = new StringBuilder();
                              var19.append("\ud83d\udcce ");
                              var19.append(var1.messageOwner.message);
                              return var19.toString();
                           }

                           return LocaleController.getString("AttachDocument", 2131558714);
                        }
                     }

                     return null;
                  } else {
                     if (var3 != null) {
                        var3[0] = false;
                     }

                     return LocaleController.getString("Message", 2131559845);
                  }
               }
            }
         }
      } else {
         return LocaleController.getString("YouHaveNewMessage", 2131561139);
      }
   }

   private String getStringForMessage(MessageObject var1, boolean var2, boolean[] var3, boolean[] var4) {
      if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter) {
         TLRPC.Message var5 = var1.messageOwner;
         long var6 = var5.dialog_id;
         TLRPC.Peer var25 = var5.to_id;
         int var8 = var25.chat_id;
         if (var8 == 0) {
            var8 = var25.channel_id;
         }

         int var9 = var1.messageOwner.to_id.user_id;
         if (var4 != null) {
            var4[0] = true;
         }

         if (var1.isFcmMessage()) {
            if (var8 == 0 && var9 != 0) {
               if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                  if (var4 != null) {
                     var4[0] = false;
                  }

                  return LocaleController.formatString("NotificationMessageNoText", 2131560043, var1.localName);
               }
            } else if (var8 != 0) {
               SharedPreferences var34 = MessagesController.getNotificationsSettings(this.currentAccount);
               if (!var1.localChannel && !var34.getBoolean("EnablePreviewGroup", true) || var1.localChannel && !var34.getBoolean("EnablePreviewChannel", true)) {
                  if (var4 != null) {
                     var4[0] = false;
                  }

                  if (!var1.isMegagroup() && var1.messageOwner.to_id.channel_id != 0) {
                     return LocaleController.formatString("ChannelMessageNoText", 2131558973, var1.localName);
                  }

                  return LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, var1.localUserName, var1.localName);
               }
            }

            var3[0] = true;
            return (String)var1.messageText;
         } else {
            int var10;
            if (var9 == 0) {
               if (!var1.isFromUser() && var1.getId() >= 0) {
                  var10 = -var8;
               } else {
                  var10 = var1.messageOwner.from_id;
               }
            } else {
               var10 = var9;
               if (var9 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  var10 = var1.messageOwner.from_id;
               }
            }

            long var11 = var6;
            if (var6 == 0L) {
               if (var8 != 0) {
                  var11 = (long)(-var8);
               } else {
                  var11 = var6;
                  if (var10 != 0) {
                     var11 = (long)var10;
                  }
               }
            }

            String var29;
            label836: {
               if (var10 > 0) {
                  TLRPC.User var26 = MessagesController.getInstance(this.currentAccount).getUser(var10);
                  if (var26 != null) {
                     var29 = UserObject.getUserName(var26);
                     break label836;
                  }
               } else {
                  TLRPC.Chat var30 = MessagesController.getInstance(this.currentAccount).getChat(-var10);
                  if (var30 != null) {
                     var29 = var30.title;
                     break label836;
                  }
               }

               var29 = null;
            }

            if (var29 == null) {
               return null;
            } else {
               TLRPC.Chat var14;
               if (var8 != 0) {
                  TLRPC.Chat var13 = MessagesController.getInstance(this.currentAccount).getChat(var8);
                  var14 = var13;
                  if (var13 == null) {
                     return null;
                  }
               } else {
                  var14 = null;
               }

               String var21;
               if ((int)var11 == 0) {
                  var21 = LocaleController.getString("YouHaveNewMessage", 2131561139);
               } else {
                  label1073: {
                     String var15;
                     TLRPC.TL_messageMediaContact var17;
                     String var20;
                     TLRPC.Message var23;
                     TLRPC.MessageAction var27;
                     StringBuilder var28;
                     TLRPC.MessageMedia var44;
                     if (var8 == 0 && var10 != 0) {
                        if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                           if (var4 != null) {
                              var4[0] = false;
                           }

                           var21 = LocaleController.formatString("NotificationMessageNoText", 2131560043, var29);
                           return var21;
                        }

                        var23 = var1.messageOwner;
                        if (var23 instanceof TLRPC.TL_messageService) {
                           var27 = var23.action;
                           if (!(var27 instanceof TLRPC.TL_messageActionUserJoined) && !(var27 instanceof TLRPC.TL_messageActionContactSignUp)) {
                              if (var27 instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                                 var21 = LocaleController.formatString("NotificationContactNewPhoto", 2131559998, var29);
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                                 Object[] var10002 = new Object[2];
                                 long var10006 = (long)var1.messageOwner.date;
                                 var10002[0] = LocaleController.getInstance().formatterYear.format(var10006 * 1000L);
                                 var10006 = (long)var1.messageOwner.date;
                                 var10002[1] = LocaleController.getInstance().formatterDay.format(var10006 * 1000L);
                                 var20 = LocaleController.formatString("formatDateAtTime", 2131561210, var10002);
                                 var21 = UserConfig.getInstance(this.currentAccount).getCurrentUser().first_name;
                                 TLRPC.MessageAction var45 = var1.messageOwner.action;
                                 var21 = LocaleController.formatString("NotificationUnrecognizedDevice", 2131560054, var21, var20, var45.title, var45.address);
                                 return var21;
                              }

                              if (!(var27 instanceof TLRPC.TL_messageActionGameScore) && !(var27 instanceof TLRPC.TL_messageActionPaymentSent)) {
                                 if (var27 instanceof TLRPC.TL_messageActionPhoneCall) {
                                    TLRPC.PhoneCallDiscardReason var35 = var27.reason;
                                    if (!var1.isOut() && var35 instanceof TLRPC.TL_phoneCallDiscardReasonMissed) {
                                       var21 = LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                                       return var21;
                                    }
                                 }
                                 break label1073;
                              }

                              var21 = var1.messageText.toString();
                              return var21;
                           }

                           var21 = LocaleController.formatString("NotificationContactJoined", 2131559997, var29);
                           return var21;
                        }

                        if (var1.isMediaEmpty()) {
                           if (!var2) {
                              if (!TextUtils.isEmpty(var1.messageOwner.message)) {
                                 var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var1.messageOwner.message);
                                 var3[0] = true;
                              } else {
                                 var21 = LocaleController.formatString("NotificationMessageNoText", 2131560043, var29);
                              }

                              return var21;
                           } else {
                              var21 = LocaleController.formatString("NotificationMessageNoText", 2131560043, var29);
                              return var21;
                           }
                        }

                        var23 = var1.messageOwner;
                        if (var23.media instanceof TLRPC.TL_messageMediaPhoto) {
                           if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var23.message)) {
                              var28 = new StringBuilder();
                              var28.append("\ud83d\uddbc ");
                              var28.append(var1.messageOwner.message);
                              var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                              var3[0] = true;
                              return var21;
                           } else {
                              if (var1.messageOwner.media.ttl_seconds != 0) {
                                 var21 = LocaleController.formatString("NotificationMessageSDPhoto", 2131560047, var29);
                              } else {
                                 var21 = LocaleController.formatString("NotificationMessagePhoto", 2131560044, var29);
                              }

                              return var21;
                           }
                        }

                        if (var1.isVideo()) {
                           if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                              var28 = new StringBuilder();
                              var28.append("\ud83d\udcf9 ");
                              var28.append(var1.messageOwner.message);
                              var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                              var3[0] = true;
                              return var21;
                           } else {
                              if (var1.messageOwner.media.ttl_seconds != 0) {
                                 var21 = LocaleController.formatString("NotificationMessageSDVideo", 2131560048, var29);
                              } else {
                                 var21 = LocaleController.formatString("NotificationMessageVideo", 2131560052, var29);
                              }

                              return var21;
                           }
                        }

                        if (var1.isGame()) {
                           var21 = LocaleController.formatString("NotificationMessageGame", 2131560018, var29, var1.messageOwner.media.game.title);
                           return var21;
                        }

                        if (var1.isVoice()) {
                           var21 = LocaleController.formatString("NotificationMessageAudio", 2131560013, var29);
                           return var21;
                        }

                        if (var1.isRoundVideo()) {
                           var21 = LocaleController.formatString("NotificationMessageRound", 2131560046, var29);
                           return var21;
                        }

                        if (var1.isMusic()) {
                           var21 = LocaleController.formatString("NotificationMessageMusic", 2131560042, var29);
                           return var21;
                        }

                        var44 = var1.messageOwner.media;
                        if (var44 instanceof TLRPC.TL_messageMediaContact) {
                           var17 = (TLRPC.TL_messageMediaContact)var44;
                           var21 = LocaleController.formatString("NotificationMessageContact2", 2131560014, var29, ContactsController.formatName(var17.first_name, var17.last_name));
                           return var21;
                        }

                        if (var44 instanceof TLRPC.TL_messageMediaPoll) {
                           var21 = LocaleController.formatString("NotificationMessagePoll2", 2131560045, var29, ((TLRPC.TL_messageMediaPoll)var44).poll.question);
                           return var21;
                        }

                        if (var44 instanceof TLRPC.TL_messageMediaGeo || var44 instanceof TLRPC.TL_messageMediaVenue) {
                           var21 = LocaleController.formatString("NotificationMessageMap", 2131560041, var29);
                           return var21;
                        }

                        if (var44 instanceof TLRPC.TL_messageMediaGeoLive) {
                           var21 = LocaleController.formatString("NotificationMessageLiveLocation", 2131560040, var29);
                           return var21;
                        }

                        if (!(var44 instanceof TLRPC.TL_messageMediaDocument)) {
                           break label1073;
                        }

                        if (!var1.isSticker()) {
                           if (var1.isGif()) {
                              if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                 var28 = new StringBuilder();
                                 var28.append("\ud83c\udfac ");
                                 var28.append(var1.messageOwner.message);
                                 var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                                 var3[0] = true;
                              } else {
                                 var21 = LocaleController.formatString("NotificationMessageGif", 2131560020, var29);
                              }

                              return var21;
                           } else {
                              if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                 var28 = new StringBuilder();
                                 var28.append("\ud83d\udcce ");
                                 var28.append(var1.messageOwner.message);
                                 var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                                 var3[0] = true;
                              } else {
                                 var21 = LocaleController.formatString("NotificationMessageDocument", 2131560015, var29);
                              }

                              return var21;
                           }
                        }

                        var15 = var1.getStickerEmoji();
                        if (var15 != null) {
                           var15 = LocaleController.formatString("NotificationMessageStickerEmoji", 2131560050, var29, var15);
                        } else {
                           var15 = LocaleController.formatString("NotificationMessageSticker", 2131560049, var29);
                        }
                     } else {
                        if (var8 == 0) {
                           break label1073;
                        }

                        SharedPreferences var37 = MessagesController.getNotificationsSettings(this.currentAccount);
                        boolean var36;
                        if (ChatObject.isChannel(var14) && !var14.megagroup) {
                           var36 = true;
                        } else {
                           var36 = false;
                        }

                        if ((var36 || !var37.getBoolean("EnablePreviewGroup", true)) && (!var36 || !var37.getBoolean("EnablePreviewChannel", true))) {
                           if (var4 != null) {
                              var4[0] = false;
                           }

                           if (ChatObject.isChannel(var14) && !var14.megagroup) {
                              var21 = LocaleController.formatString("ChannelMessageNoText", 2131558973, var29);
                              return var21;
                           }

                           var21 = LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, var29, var14.title);
                           return var21;
                        }

                        var23 = var1.messageOwner;
                        TLRPC.TL_messageMediaPoll var16;
                        StringBuilder var22;
                        if (var23 instanceof TLRPC.TL_messageService) {
                           var27 = var23.action;
                           TLRPC.User var24;
                           if (var27 instanceof TLRPC.TL_messageActionChatAddUser) {
                              var9 = var27.user_id;
                              var8 = var9;
                              if (var9 == 0) {
                                 var8 = var9;
                                 if (var27.users.size() == 1) {
                                    var8 = (Integer)var1.messageOwner.action.users.get(0);
                                 }
                              }

                              if (var8 != 0) {
                                 if (var1.messageOwner.to_id.channel_id != 0 && !var14.megagroup) {
                                    var15 = LocaleController.formatString("ChannelAddedByNotification", 2131558925, var29, var14.title);
                                 } else if (var8 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    var15 = LocaleController.formatString("NotificationInvitedToGroup", 2131560010, var29, var14.title);
                                 } else {
                                    var24 = MessagesController.getInstance(this.currentAccount).getUser(var8);
                                    if (var24 == null) {
                                       return null;
                                    }

                                    if (var10 == var24.id) {
                                       if (var14.megagroup) {
                                          var15 = LocaleController.formatString("NotificationGroupAddSelfMega", 2131560003, var29, var14.title);
                                       } else {
                                          var15 = LocaleController.formatString("NotificationGroupAddSelf", 2131560002, var29, var14.title);
                                       }
                                    } else {
                                       var15 = LocaleController.formatString("NotificationGroupAddMember", 2131560001, var29, var14.title, UserObject.getUserName(var24));
                                    }
                                 }
                              } else {
                                 var22 = new StringBuilder();

                                 for(var10 = 0; var10 < var1.messageOwner.action.users.size(); ++var10) {
                                    TLRPC.User var43 = MessagesController.getInstance(this.currentAccount).getUser((Integer)var1.messageOwner.action.users.get(var10));
                                    if (var43 != null) {
                                       var21 = UserObject.getUserName(var43);
                                       if (var22.length() != 0) {
                                          var22.append(", ");
                                       }

                                       var22.append(var21);
                                    }
                                 }

                                 var15 = LocaleController.formatString("NotificationGroupAddMember", 2131560001, var29, var14.title, var22.toString());
                              }
                           } else {
                              if (var27 instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                                 var21 = LocaleController.formatString("NotificationInvitedToGroupByLink", 2131560011, var29, var14.title);
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionChatEditTitle) {
                                 var21 = LocaleController.formatString("NotificationEditedGroupName", 2131559999, var29, var27.title);
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionChatEditPhoto || var27 instanceof TLRPC.TL_messageActionChatDeletePhoto) {
                                 if (var1.messageOwner.to_id.channel_id != 0 && !var14.megagroup) {
                                    var21 = LocaleController.formatString("ChannelPhotoEditNotification", 2131558987, var14.title);
                                    return var21;
                                 }

                                 var21 = LocaleController.formatString("NotificationEditedGroupPhoto", 2131560000, var29, var14.title);
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                                 if (var27.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    var21 = LocaleController.formatString("NotificationGroupKickYou", 2131560008, var29, var14.title);
                                    return var21;
                                 } else {
                                    if (var1.messageOwner.action.user_id == var10) {
                                       var21 = LocaleController.formatString("NotificationGroupLeftMember", 2131560009, var29, var14.title);
                                    } else {
                                       var24 = MessagesController.getInstance(this.currentAccount).getUser(var1.messageOwner.action.user_id);
                                       if (var24 == null) {
                                          return null;
                                       }

                                       var21 = LocaleController.formatString("NotificationGroupKickMember", 2131560007, var29, var14.title, UserObject.getUserName(var24));
                                    }

                                    return var21;
                                 }
                              }

                              var21 = null;
                              if (var27 instanceof TLRPC.TL_messageActionChatCreate) {
                                 var21 = var1.messageText.toString();
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionChannelCreate) {
                                 var21 = var1.messageText.toString();
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                 var21 = LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, var14.title);
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                 var21 = LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, var27.title);
                                 return var21;
                              }

                              if (var27 instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                 var21 = var1.messageText.toString();
                                 return var21;
                              }

                              if (!(var27 instanceof TLRPC.TL_messageActionPinMessage)) {
                                 if (var27 instanceof TLRPC.TL_messageActionGameScore) {
                                    var21 = var1.messageText.toString();
                                 }

                                 return var21;
                              }

                              MessageObject var31;
                              CharSequence var32;
                              StringBuilder var33;
                              CharSequence var41;
                              Object var42;
                              if (var14 != null && (!ChatObject.isChannel(var14) || var14.megagroup)) {
                                 var31 = var1.replyMessageObject;
                                 if (var31 == null) {
                                    var21 = LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, var29, var14.title);
                                    return var21;
                                 }

                                 if (var31.isMusic()) {
                                    var15 = LocaleController.formatString("NotificationActionPinnedMusic", 2131559977, var29, var14.title);
                                 } else if (var31.isVideo()) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var31.messageOwner.message)) {
                                       var33 = new StringBuilder();
                                       var33.append("\ud83d\udcf9 ");
                                       var33.append(var31.messageOwner.message);
                                       var15 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var33.toString(), var14.title);
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedVideo", 2131559993, var29, var14.title);
                                    }
                                 } else if (var31.isGif()) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var31.messageOwner.message)) {
                                       var33 = new StringBuilder();
                                       var33.append("\ud83c\udfac ");
                                       var33.append(var31.messageOwner.message);
                                       var15 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var33.toString(), var14.title);
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedGif", 2131559973, var29, var14.title);
                                    }
                                 } else if (var31.isVoice()) {
                                    var15 = LocaleController.formatString("NotificationActionPinnedVoice", 2131559995, var29, var14.title);
                                 } else if (var31.isRoundVideo()) {
                                    var15 = LocaleController.formatString("NotificationActionPinnedRound", 2131559985, var29, var14.title);
                                 } else if (var31.isSticker()) {
                                    var15 = var31.getStickerEmoji();
                                    if (var15 != null) {
                                       var15 = LocaleController.formatString("NotificationActionPinnedStickerEmoji", 2131559989, var29, var14.title, var15);
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedSticker", 2131559987, var29, var14.title);
                                    }
                                 } else {
                                    var23 = var31.messageOwner;
                                    TLRPC.MessageMedia var38 = var23.media;
                                    if (var38 instanceof TLRPC.TL_messageMediaDocument) {
                                       if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var23.message)) {
                                          var33 = new StringBuilder();
                                          var33.append("\ud83d\udcce ");
                                          var33.append(var31.messageOwner.message);
                                          var15 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var33.toString(), var14.title);
                                       } else {
                                          var15 = LocaleController.formatString("NotificationActionPinnedFile", 2131559963, var29, var14.title);
                                       }
                                    } else if (!(var38 instanceof TLRPC.TL_messageMediaGeo) && !(var38 instanceof TLRPC.TL_messageMediaVenue)) {
                                       if (var38 instanceof TLRPC.TL_messageMediaGeoLive) {
                                          var15 = LocaleController.formatString("NotificationActionPinnedGeoLive", 2131559971, var29, var14.title);
                                       } else if (var38 instanceof TLRPC.TL_messageMediaContact) {
                                          var17 = (TLRPC.TL_messageMediaContact)var1.messageOwner.media;
                                          var15 = LocaleController.formatString("NotificationActionPinnedContact2", 2131559961, var29, var14.title, ContactsController.formatName(var17.first_name, var17.last_name));
                                       } else if (var38 instanceof TLRPC.TL_messageMediaPoll) {
                                          var16 = (TLRPC.TL_messageMediaPoll)var38;
                                          var15 = LocaleController.formatString("NotificationActionPinnedPoll2", 2131559983, var29, var14.title, var16.poll.question);
                                       } else if (var38 instanceof TLRPC.TL_messageMediaPhoto) {
                                          if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var23.message)) {
                                             var33 = new StringBuilder();
                                             var33.append("\ud83d\uddbc ");
                                             var33.append(var31.messageOwner.message);
                                             var15 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var33.toString(), var14.title);
                                          } else {
                                             var15 = LocaleController.formatString("NotificationActionPinnedPhoto", 2131559981, var29, var14.title);
                                          }
                                       } else if (var38 instanceof TLRPC.TL_messageMediaGame) {
                                          var15 = LocaleController.formatString("NotificationActionPinnedGame", 2131559965, var29, var14.title);
                                       } else {
                                          var41 = var31.messageText;
                                          if (var41 != null && var41.length() > 0) {
                                             var32 = var31.messageText;
                                             var42 = var32;
                                             if (var32.length() > 20) {
                                                var33 = new StringBuilder();
                                                var33.append(var32.subSequence(0, 20));
                                                var33.append("...");
                                                var42 = var33.toString();
                                             }

                                             var15 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, var29, var42, var14.title);
                                          } else {
                                             var15 = LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, var29, var14.title);
                                          }
                                       }
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedGeo", 2131559969, var29, var14.title);
                                    }
                                 }
                              } else {
                                 var31 = var1.replyMessageObject;
                                 if (var31 == null) {
                                    var21 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, var14.title);
                                    return var21;
                                 }

                                 if (var31.isMusic()) {
                                    var15 = LocaleController.formatString("NotificationActionPinnedMusicChannel", 2131559978, var14.title);
                                 } else if (var31.isVideo()) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var31.messageOwner.message)) {
                                       var33 = new StringBuilder();
                                       var33.append("\ud83d\udcf9 ");
                                       var33.append(var31.messageOwner.message);
                                       var15 = var33.toString();
                                       var15 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var14.title, var15);
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedVideoChannel", 2131559994, var14.title);
                                    }
                                 } else if (var31.isGif()) {
                                    if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var31.messageOwner.message)) {
                                       var33 = new StringBuilder();
                                       var33.append("\ud83c\udfac ");
                                       var33.append(var31.messageOwner.message);
                                       var15 = var33.toString();
                                       var15 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var14.title, var15);
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedGifChannel", 2131559974, var14.title);
                                    }
                                 } else if (var31.isVoice()) {
                                    var15 = LocaleController.formatString("NotificationActionPinnedVoiceChannel", 2131559996, var14.title);
                                 } else if (var31.isRoundVideo()) {
                                    var15 = LocaleController.formatString("NotificationActionPinnedRoundChannel", 2131559986, var14.title);
                                 } else if (var31.isSticker()) {
                                    var15 = var31.getStickerEmoji();
                                    if (var15 != null) {
                                       var15 = LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", 2131559990, var14.title, var15);
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedStickerChannel", 2131559988, var14.title);
                                    }
                                 } else {
                                    var5 = var31.messageOwner;
                                    var44 = var5.media;
                                    if (var44 instanceof TLRPC.TL_messageMediaDocument) {
                                       if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var5.message)) {
                                          var33 = new StringBuilder();
                                          var33.append("\ud83d\udcce ");
                                          var33.append(var31.messageOwner.message);
                                          var15 = var33.toString();
                                          var15 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var14.title, var15);
                                       } else {
                                          var15 = LocaleController.formatString("NotificationActionPinnedFileChannel", 2131559964, var14.title);
                                       }
                                    } else if (!(var44 instanceof TLRPC.TL_messageMediaGeo) && !(var44 instanceof TLRPC.TL_messageMediaVenue)) {
                                       if (var44 instanceof TLRPC.TL_messageMediaGeoLive) {
                                          var15 = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", 2131559972, var14.title);
                                       } else if (var44 instanceof TLRPC.TL_messageMediaContact) {
                                          var17 = (TLRPC.TL_messageMediaContact)var1.messageOwner.media;
                                          var15 = LocaleController.formatString("NotificationActionPinnedContactChannel2", 2131559962, var14.title, ContactsController.formatName(var17.first_name, var17.last_name));
                                       } else if (var44 instanceof TLRPC.TL_messageMediaPoll) {
                                          var16 = (TLRPC.TL_messageMediaPoll)var44;
                                          var15 = LocaleController.formatString("NotificationActionPinnedPollChannel2", 2131559984, var14.title, var16.poll.question);
                                       } else if (var44 instanceof TLRPC.TL_messageMediaPhoto) {
                                          if (VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var5.message)) {
                                             var33 = new StringBuilder();
                                             var33.append("\ud83d\uddbc ");
                                             var33.append(var31.messageOwner.message);
                                             var15 = var33.toString();
                                             var15 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var14.title, var15);
                                          } else {
                                             var15 = LocaleController.formatString("NotificationActionPinnedPhotoChannel", 2131559982, var14.title);
                                          }
                                       } else if (var44 instanceof TLRPC.TL_messageMediaGame) {
                                          var15 = LocaleController.formatString("NotificationActionPinnedGameChannel", 2131559966, var14.title);
                                       } else {
                                          var41 = var31.messageText;
                                          if (var41 != null && var41.length() > 0) {
                                             var32 = var31.messageText;
                                             var42 = var32;
                                             if (var32.length() > 20) {
                                                var33 = new StringBuilder();
                                                var33.append(var32.subSequence(0, 20));
                                                var33.append("...");
                                                var42 = var33.toString();
                                             }

                                             var15 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, var14.title, var42);
                                          } else {
                                             var15 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, var14.title);
                                          }
                                       }
                                    } else {
                                       var15 = LocaleController.formatString("NotificationActionPinnedGeoChannel", 2131559970, var14.title);
                                    }
                                 }
                              }
                           }
                        } else {
                           var21 = null;
                           if (ChatObject.isChannel(var14) && !var14.megagroup) {
                              if (var1.isMediaEmpty()) {
                                 if (!var2) {
                                    var21 = var1.messageOwner.message;
                                    if (var21 != null && var21.length() != 0) {
                                       var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var1.messageOwner.message);
                                       var3[0] = true;
                                       return var21;
                                    }
                                 }

                                 var21 = LocaleController.formatString("ChannelMessageNoText", 2131558973, var29);
                                 return var21;
                              }

                              TLRPC.Message var39 = var1.messageOwner;
                              if (var39.media instanceof TLRPC.TL_messageMediaPhoto) {
                                 if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var39.message)) {
                                    var28 = new StringBuilder();
                                    var28.append("\ud83d\uddbc ");
                                    var28.append(var1.messageOwner.message);
                                    var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                                    var3[0] = true;
                                 } else {
                                    var21 = LocaleController.formatString("ChannelMessagePhoto", 2131558974, var29);
                                 }

                                 return var21;
                              }

                              if (var1.isVideo()) {
                                 if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                    var28 = new StringBuilder();
                                    var28.append("\ud83d\udcf9 ");
                                    var28.append(var1.messageOwner.message);
                                    var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                                    var3[0] = true;
                                 } else {
                                    var21 = LocaleController.formatString("ChannelMessageVideo", 2131558979, var29);
                                 }

                                 return var21;
                              }

                              if (var1.isVoice()) {
                                 var21 = LocaleController.formatString("ChannelMessageAudio", 2131558965, var29);
                                 return var21;
                              }

                              if (var1.isRoundVideo()) {
                                 var21 = LocaleController.formatString("ChannelMessageRound", 2131558976, var29);
                                 return var21;
                              }

                              if (var1.isMusic()) {
                                 var21 = LocaleController.formatString("ChannelMessageMusic", 2131558972, var29);
                                 return var21;
                              }

                              TLRPC.MessageMedia var40 = var1.messageOwner.media;
                              if (var40 instanceof TLRPC.TL_messageMediaContact) {
                                 var17 = (TLRPC.TL_messageMediaContact)var40;
                                 var21 = LocaleController.formatString("ChannelMessageContact2", 2131558966, var29, ContactsController.formatName(var17.first_name, var17.last_name));
                                 return var21;
                              }

                              if (var40 instanceof TLRPC.TL_messageMediaPoll) {
                                 var21 = LocaleController.formatString("ChannelMessagePoll2", 2131558975, var29, ((TLRPC.TL_messageMediaPoll)var40).poll.question);
                                 return var21;
                              }

                              if (var40 instanceof TLRPC.TL_messageMediaGeo || var40 instanceof TLRPC.TL_messageMediaVenue) {
                                 var21 = LocaleController.formatString("ChannelMessageMap", 2131558971, var29);
                                 return var21;
                              }

                              if (var40 instanceof TLRPC.TL_messageMediaGeoLive) {
                                 var21 = LocaleController.formatString("ChannelMessageLiveLocation", 2131558970, var29);
                                 return var21;
                              }

                              if (!(var40 instanceof TLRPC.TL_messageMediaDocument)) {
                                 return var21;
                              }

                              if (!var1.isSticker()) {
                                 if (var1.isGif()) {
                                    if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                       var28 = new StringBuilder();
                                       var28.append("\ud83c\udfac ");
                                       var28.append(var1.messageOwner.message);
                                       var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                                       var3[0] = true;
                                    } else {
                                       var21 = LocaleController.formatString("ChannelMessageGIF", 2131558969, var29);
                                    }

                                    return var21;
                                 } else {
                                    if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                       var28 = new StringBuilder();
                                       var28.append("\ud83d\udcce ");
                                       var28.append(var1.messageOwner.message);
                                       var21 = LocaleController.formatString("NotificationMessageText", 2131560051, var29, var28.toString());
                                       var3[0] = true;
                                    } else {
                                       var21 = LocaleController.formatString("ChannelMessageDocument", 2131558967, var29);
                                    }

                                    return var21;
                                 }
                              }

                              var15 = var1.getStickerEmoji();
                              if (var15 != null) {
                                 var15 = LocaleController.formatString("ChannelMessageStickerEmoji", 2131558978, var29, var15);
                              } else {
                                 var15 = LocaleController.formatString("ChannelMessageSticker", 2131558977, var29);
                              }
                           } else {
                              if (var1.isMediaEmpty()) {
                                 if (!var2) {
                                    var20 = var1.messageOwner.message;
                                    if (var20 != null && var20.length() != 0) {
                                       var21 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, var29, var14.title, var1.messageOwner.message);
                                       return var21;
                                    }
                                 }

                                 var21 = LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, var29, var14.title);
                                 return var21;
                              }

                              TLRPC.Message var18 = var1.messageOwner;
                              if (var18.media instanceof TLRPC.TL_messageMediaPhoto) {
                                 if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var18.message)) {
                                    var21 = var14.title;
                                    var22 = new StringBuilder();
                                    var22.append("\ud83d\uddbc ");
                                    var22.append(var1.messageOwner.message);
                                    var21 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, var29, var21, var22.toString());
                                 } else {
                                    var21 = LocaleController.formatString("NotificationMessageGroupPhoto", 2131560032, var29, var14.title);
                                 }

                                 return var21;
                              }

                              if (var1.isVideo()) {
                                 if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                    var21 = var14.title;
                                    var22 = new StringBuilder();
                                    var22.append("\ud83d\udcf9 ");
                                    var22.append(var1.messageOwner.message);
                                    var21 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, var29, var21, var22.toString());
                                 } else {
                                    var21 = LocaleController.formatString(" ", 2131560038, var29, var14.title);
                                 }

                                 return var21;
                              }

                              if (var1.isVoice()) {
                                 var21 = LocaleController.formatString("NotificationMessageGroupAudio", 2131560021, var29, var14.title);
                                 return var21;
                              }

                              if (var1.isRoundVideo()) {
                                 var21 = LocaleController.formatString("NotificationMessageGroupRound", 2131560034, var29, var14.title);
                                 return var21;
                              }

                              if (var1.isMusic()) {
                                 var21 = LocaleController.formatString("NotificationMessageGroupMusic", 2131560030, var29, var14.title);
                                 return var21;
                              }

                              TLRPC.MessageMedia var19 = var1.messageOwner.media;
                              if (var19 instanceof TLRPC.TL_messageMediaContact) {
                                 var17 = (TLRPC.TL_messageMediaContact)var19;
                                 var21 = LocaleController.formatString("NotificationMessageGroupContact2", 2131560022, var29, var14.title, ContactsController.formatName(var17.first_name, var17.last_name));
                                 return var21;
                              }

                              if (var19 instanceof TLRPC.TL_messageMediaPoll) {
                                 var16 = (TLRPC.TL_messageMediaPoll)var19;
                                 var21 = LocaleController.formatString("NotificationMessageGroupPoll2", 2131560033, var29, var14.title, var16.poll.question);
                                 return var21;
                              }

                              if (var19 instanceof TLRPC.TL_messageMediaGame) {
                                 var21 = LocaleController.formatString("NotificationMessageGroupGame", 2131560024, var29, var14.title, var19.game.title);
                                 return var21;
                              }

                              if (var19 instanceof TLRPC.TL_messageMediaGeo || var19 instanceof TLRPC.TL_messageMediaVenue) {
                                 var21 = LocaleController.formatString("NotificationMessageGroupMap", 2131560029, var29, var14.title);
                                 return var21;
                              }

                              if (var19 instanceof TLRPC.TL_messageMediaGeoLive) {
                                 var21 = LocaleController.formatString("NotificationMessageGroupLiveLocation", 2131560028, var29, var14.title);
                                 return var21;
                              }

                              if (!(var19 instanceof TLRPC.TL_messageMediaDocument)) {
                                 return var21;
                              }

                              if (!var1.isSticker()) {
                                 if (var1.isGif()) {
                                    if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                       var20 = var14.title;
                                       var28 = new StringBuilder();
                                       var28.append("\ud83c\udfac ");
                                       var28.append(var1.messageOwner.message);
                                       var21 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, var29, var20, var28.toString());
                                    } else {
                                       var21 = LocaleController.formatString("NotificationMessageGroupGif", 2131560026, var29, var14.title);
                                    }

                                    return var21;
                                 } else {
                                    if (!var2 && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(var1.messageOwner.message)) {
                                       var21 = var14.title;
                                       var22 = new StringBuilder();
                                       var22.append("\ud83d\udcce ");
                                       var22.append(var1.messageOwner.message);
                                       var21 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, var29, var21, var22.toString());
                                    } else {
                                       var21 = LocaleController.formatString("NotificationMessageGroupDocument", 2131560023, var29, var14.title);
                                    }

                                    return var21;
                                 }
                              }

                              var15 = var1.getStickerEmoji();
                              if (var15 != null) {
                                 var15 = LocaleController.formatString("NotificationMessageGroupStickerEmoji", 2131560036, var29, var14.title, var15);
                              } else {
                                 var15 = LocaleController.formatString("NotificationMessageGroupSticker", 2131560035, var29, var14.title);
                              }
                           }
                        }
                     }

                     var21 = var15;
                     return var21;
                  }

                  var21 = null;
               }

               return var21;
            }
         }
      } else {
         return LocaleController.getString("YouHaveNewMessage", 2131561139);
      }
   }

   private int getTotalAllUnreadCount() {
      int var1 = 0;

      int var2;
      int var3;
      for(var2 = 0; var1 < 3; var2 = var3) {
         var3 = var2;
         if (UserConfig.getInstance(var1).isClientActivated()) {
            NotificationsController var4 = getInstance(var1);
            var3 = var2;
            if (var4.showBadgeNumber) {
               label112: {
                  label97: {
                     int var5;
                     int var6;
                     Exception var13;
                     if (var4.showBadgeMessages) {
                        label104: {
                           if (!var4.showBadgeMuted) {
                              var3 = var4.total_unread_count;
                              break label97;
                           }

                           label105: {
                              try {
                                 var5 = MessagesController.getInstance(var1).allDialogs.size();
                              } catch (Exception var12) {
                                 var13 = var12;
                                 break label105;
                              }

                              var6 = 0;
                              var3 = var2;

                              while(true) {
                                 var2 = var3;
                                 if (var6 >= var5) {
                                    break label104;
                                 }

                                 label81: {
                                    label80: {
                                       Exception var10000;
                                       label107: {
                                          TLRPC.Dialog var14;
                                          boolean var10001;
                                          try {
                                             var14 = (TLRPC.Dialog)MessagesController.getInstance(var1).allDialogs.get(var6);
                                          } catch (Exception var11) {
                                             var10000 = var11;
                                             var10001 = false;
                                             break label107;
                                          }

                                          var2 = var3;

                                          try {
                                             if (var14.unread_count == 0) {
                                                break label81;
                                             }

                                             var2 = var14.unread_count;
                                             break label80;
                                          } catch (Exception var10) {
                                             var10000 = var10;
                                             var10001 = false;
                                          }
                                       }

                                       var13 = var10000;
                                       var2 = var3;
                                       break;
                                    }

                                    var2 += var3;
                                 }

                                 ++var6;
                                 var3 = var2;
                              }
                           }

                           FileLog.e((Throwable)var13);
                        }
                     } else {
                        label108: {
                           if (!var4.showBadgeMuted) {
                              var3 = var4.pushDialogs.size();
                              break label97;
                           }

                           label109: {
                              try {
                                 var5 = MessagesController.getInstance(var1).allDialogs.size();
                              } catch (Exception var9) {
                                 var13 = var9;
                                 break label109;
                              }

                              var6 = 0;
                              var3 = var2;

                              while(true) {
                                 var2 = var3;
                                 if (var6 >= var5) {
                                    break label108;
                                 }

                                 int var7;
                                 try {
                                    var7 = ((TLRPC.Dialog)MessagesController.getInstance(var1).allDialogs.get(var6)).unread_count;
                                 } catch (Exception var8) {
                                    var13 = var8;
                                    var2 = var3;
                                    break;
                                 }

                                 var2 = var3;
                                 if (var7 != 0) {
                                    var2 = var3 + 1;
                                 }

                                 ++var6;
                                 var3 = var2;
                              }
                           }

                           FileLog.e((Throwable)var13);
                        }
                     }

                     var3 = var2;
                     break label112;
                  }

                  var3 += var2;
               }
            }
         }

         ++var1;
      }

      return var2;
   }

   private boolean isEmptyVibration(long[] var1) {
      if (var1 != null && var1.length != 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2] != 0L) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isPersonalMessage(MessageObject var1) {
      TLRPC.Message var2 = var1.messageOwner;
      TLRPC.Peer var4 = var2.to_id;
      boolean var3;
      if (var4 != null && var4.chat_id == 0 && var4.channel_id == 0) {
         TLRPC.MessageAction var5 = var2.action;
         if (var5 == null || var5 instanceof TLRPC.TL_messageActionEmpty) {
            var3 = true;
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   // $FF: synthetic method
   static void lambda$dismissNotification$25() {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated);
   }

   // $FF: synthetic method
   static void lambda$loadRoundAvatar$33(ImageDecoder var0, ImageInfo var1, Source var2) {
      var0.setPostProcessor(_$$Lambda$NotificationsController$wGfyzcvvHxFIxbrke7bSnOwTfcM.INSTANCE);
   }

   // $FF: synthetic method
   static void lambda$null$26(SoundPool var0, int var1, int var2) {
      if (var2 == 0) {
         try {
            var0.play(var1, 1.0F, 1.0F, 1, 0, 1.0F);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }
      }

   }

   // $FF: synthetic method
   static int lambda$null$32(Canvas var0) {
      Path var1 = new Path();
      var1.setFillType(FillType.INVERSE_EVEN_ODD);
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      float var4 = (float)var2;
      float var5 = (float)var3;
      float var6 = (float)(var2 / 2);
      var1.addRoundRect(0.0F, 0.0F, var4, var5, var6, var6, Direction.CW);
      Paint var7 = new Paint();
      var7.setAntiAlias(true);
      var7.setColor(0);
      var7.setXfermode(new PorterDuffXfermode(Mode.SRC));
      var0.drawPath(var1, var7);
      return -3;
   }

   // $FF: synthetic method
   static void lambda$null$34(SoundPool var0, int var1, int var2) {
      if (var2 == 0) {
         try {
            var0.play(var1, 1.0F, 1.0F, 1, 0, 1.0F);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }
      }

   }

   // $FF: synthetic method
   static void lambda$showExtraNotifications$31(Uri var0) {
      ApplicationLoader.applicationContext.revokeUriPermission(var0, 1);
   }

   // $FF: synthetic method
   static void lambda$updateServerNotificationsSettings$36(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$updateServerNotificationsSettings$37(TLObject var0, TLRPC.TL_error var1) {
   }

   @TargetApi(28)
   private void loadRoundAvatar(File var1, Person.Builder var2) {
      if (var1 != null) {
         try {
            var2.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(var1), _$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8.INSTANCE)));
         } catch (Throwable var3) {
         }
      }

   }

   private void playInChatSound() {
      if (this.inChatSoundEnabled && !MediaController.getInstance().isRecordingAudio()) {
         label27: {
            int var1;
            try {
               var1 = audioManager.getRingerMode();
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
               break label27;
            }

            if (var1 == 0) {
               return;
            }
         }

         try {
            if (this.getNotifyOverride(MessagesController.getNotificationsSettings(this.currentAccount), this.opened_dialog_id) == 2) {
               return;
            }

            DispatchQueue var3 = notificationsQueue;
            _$$Lambda$NotificationsController$51wmHPlGOlC0_zQ9GY7w7j4BjsE var2 = new _$$Lambda$NotificationsController$51wmHPlGOlC0_zQ9GY7w7j4BjsE(this);
            var3.postRunnable(var2);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

   }

   private void scheduleNotificationDelay(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   private void scheduleNotificationRepeat() {
      Exception var10000;
      label33: {
         boolean var10001;
         int var2;
         PendingIntent var6;
         try {
            Intent var1 = new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class);
            var1.putExtra("currentAccount", this.currentAccount);
            var6 = PendingIntent.getService(ApplicationLoader.applicationContext, 0, var1, 0);
            var2 = MessagesController.getNotificationsSettings(this.currentAccount).getInt("repeat_messages", 60);
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label33;
         }

         if (var2 > 0) {
            try {
               if (this.personal_count > 0) {
                  this.alarmManager.set(2, SystemClock.elapsedRealtime() + (long)(var2 * 60 * 1000), var6);
                  return;
               }
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label33;
            }
         }

         try {
            this.alarmManager.cancel(var6);
            return;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      }

      Exception var7 = var10000;
      FileLog.e((Throwable)var7);
   }

   private void setBadge(int var1) {
      if (this.lastBadgeCount != var1) {
         this.lastBadgeCount = var1;
         NotificationBadge.applyCount(var1);
      }
   }

   @SuppressLint({"InlinedApi"})
   private void showExtraNotifications(NotificationCompat.Builder var1, boolean var2, String var3) {
      Notification var4 = var1.build();
      if (VERSION.SDK_INT < 18) {
         notificationManager.notify(this.notificationId, var4);
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("show summary notification by SDK check");
         }

      } else {
         ArrayList var5 = new ArrayList();
         LongSparseArray var6 = new LongSparseArray();

         int var7;
         long var9;
         ArrayList var11;
         ArrayList var12;
         for(var7 = 0; var7 < this.pushMessages.size(); ++var7) {
            MessageObject var8 = (MessageObject)this.pushMessages.get(var7);
            var9 = var8.getDialogId();
            var11 = (ArrayList)var6.get(var9);
            var12 = var11;
            if (var11 == null) {
               var12 = new ArrayList();
               var6.put(var9, var12);
               var5.add(0, var9);
            }

            var12.add(var8);
         }

         LongSparseArray var13 = this.wearNotificationsIds.clone();
         this.wearNotificationsIds.clear();
         var11 = new ArrayList();
         var7 = VERSION.SDK_INT;
         boolean var14;
         if (var7 <= 27 || var7 > 27 && var5.size() > 1) {
            var14 = true;
         } else {
            var14 = false;
         }

         if (var14 && VERSION.SDK_INT >= 26) {
            checkOtherNotificationsChannel();
         }

         int var15 = var5.size();

         int var16;
         StringBuilder var67;
         for(var16 = 0; var16 < var15; var11 = var12) {
            var9 = (Long)var5.get(var16);
            ArrayList var17 = (ArrayList)var6.get(var9);
            int var18 = ((MessageObject)var17.get(0)).getId();
            int var19 = (int)var9;
            int var20 = (int)(var9 >> 32);
            Integer var58 = (Integer)var13.get(var9);
            if (var58 == null) {
               if (var19 != 0) {
                  var58 = var19;
               } else {
                  var58 = var20;
               }
            } else {
               var13.remove(var9);
            }

            label532: {
               label589: {
                  LongSparseArray var22;
                  String var23;
                  TLRPC.User var24;
                  TLRPC.Chat var25;
                  boolean var28;
                  boolean var59;
                  String var65;
                  TLRPC.FileLocation var83;
                  String var87;
                  label530: {
                     MessageObject var21 = (MessageObject)var17.get(0);
                     var7 = var21.messageOwner.date;
                     var22 = new LongSparseArray();
                     TLRPC.User var64;
                     TLRPC.FileLocation var89;
                     if (var19 != 0) {
                        label557: {
                           if (var19 != 777000) {
                              var59 = true;
                           } else {
                              var59 = false;
                           }

                           if (var19 <= 0) {
                              TLRPC.FileLocation var72;
                              label515: {
                                 boolean var26;
                                 label514: {
                                    var25 = MessagesController.getInstance(this.currentAccount).getChat(-var19);
                                    boolean var27;
                                    if (var25 == null) {
                                       if (!var21.isFcmMessage()) {
                                          if (BuildVars.LOGS_ENABLED) {
                                             var67 = new StringBuilder();
                                             var67.append("not found chat to show dialog notification ");
                                             var67.append(var19);
                                             FileLog.w(var67.toString());
                                          }
                                          break label589;
                                       }

                                       var26 = var21.isMegagroup();
                                       var23 = var21.localName;
                                       var27 = var21.localChannel;
                                    } else {
                                       var2 = var25.megagroup;
                                       if (ChatObject.isChannel(var25) && !var25.megagroup) {
                                          var28 = true;
                                       } else {
                                          var28 = false;
                                       }

                                       var65 = var25.title;
                                       TLRPC.ChatPhoto var84 = var25.photo;
                                       var27 = var28;
                                       var23 = var65;
                                       var26 = var2;
                                       if (var84 != null) {
                                          var83 = var84.photo_small;
                                          var27 = var28;
                                          var23 = var65;
                                          var26 = var2;
                                          if (var83 != null) {
                                             var27 = var28;
                                             var23 = var65;
                                             var26 = var2;
                                             var28 = var28;
                                             if (var83.volume_id != 0L) {
                                                var23 = var65;
                                                var26 = var2;
                                                var28 = var27;
                                                if (var83.local_id != 0) {
                                                   var23 = var65;
                                                   var72 = var83;
                                                   var28 = var27;
                                                   break label515;
                                                }
                                             }
                                             break label514;
                                          }
                                       }
                                    }

                                    var28 = var27;
                                 }

                                 var72 = null;
                                 var2 = var26;
                              }

                              var24 = null;
                              var83 = var72;
                              break label530;
                           }

                           var64 = MessagesController.getInstance(this.currentAccount).getUser(var19);
                           if (var64 == null) {
                              if (!var21.isFcmMessage()) {
                                 if (BuildVars.LOGS_ENABLED) {
                                    var67 = new StringBuilder();
                                    var67.append("not found user to show dialog notification ");
                                    var67.append(var19);
                                    FileLog.w(var67.toString());
                                 }
                                 break label589;
                              }

                              var23 = var21.localName;
                              var24 = var64;
                              var65 = var23;
                           } else {
                              var23 = UserObject.getUserName(var64);
                              TLRPC.UserProfilePhoto var80 = var64.photo;
                              if (var80 != null) {
                                 var83 = var80.photo_small;
                                 if (var83 != null && var83.volume_id != 0L && var83.local_id != 0) {
                                    var24 = var64;
                                    var65 = var23;
                                    var89 = var83;
                                    break label557;
                                 }
                              }

                              var24 = var64;
                              var65 = var23;
                           }

                           var89 = null;
                        }
                     } else {
                        if (var9 != globalSecretChatId) {
                           TLRPC.EncryptedChat var86 = MessagesController.getInstance(this.currentAccount).getEncryptedChat(var20);
                           if (var86 == null) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var67 = new StringBuilder();
                                 var67.append("not found secret chat to show dialog notification ");
                                 var67.append(var20);
                                 FileLog.w(var67.toString());
                              }
                              break label589;
                           }

                           TLRPC.User var95 = MessagesController.getInstance(this.currentAccount).getUser(var86.user_id);
                           var64 = var95;
                           if (var95 == null) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var67 = new StringBuilder();
                                 var67.append("not found secret chat user to show dialog notification ");
                                 var67.append(var86.user_id);
                                 FileLog.w(var67.toString());
                              }
                              break label589;
                           }
                        } else {
                           var64 = null;
                        }

                        var87 = LocaleController.getString("SecretChatName", 2131560671);
                        var89 = null;
                        var59 = false;
                        var24 = var64;
                        var65 = var87;
                     }

                     var2 = false;
                     var25 = null;
                     var28 = false;
                     var83 = var89;
                     var23 = var65;
                  }

                  if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                     var23 = LocaleController.getString("AppName", 2131558635);
                     var83 = null;
                     var59 = false;
                  }

                  int var31;
                  Bitmap var75;
                  Object var93;
                  if (var83 != null) {
                     File var29 = FileLoader.getPathToAttach(var83, true);
                     BitmapDrawable var76 = ImageLoader.getInstance().getImageFromMemory(var83, (String)null, "50_50");
                     if (var76 != null) {
                        var75 = var76.getBitmap();
                        var93 = var29;
                     } else {
                        label569: {
                           if (VERSION.SDK_INT < 28) {
                              label559: {
                                 boolean var10001;
                                 float var30;
                                 Options var77;
                                 try {
                                    if (!var29.exists()) {
                                       break label559;
                                    }

                                    var30 = 160.0F / (float)AndroidUtilities.dp(50.0F);
                                    var77 = new Options();
                                 } catch (Throwable var54) {
                                    var10001 = false;
                                    break label559;
                                 }

                                 if (var30 < 1.0F) {
                                    var31 = 1;
                                 } else {
                                    var31 = (int)var30;
                                 }

                                 try {
                                    var77.inSampleSize = var31;
                                    var75 = BitmapFactory.decodeFile(var29.getAbsolutePath(), var77);
                                 } catch (Throwable var53) {
                                    var10001 = false;
                                    break label559;
                                 }

                                 var93 = var29;
                                 break label569;
                              }
                           }

                           var75 = null;
                           var93 = var29;
                        }
                     }
                  } else {
                     var75 = null;
                     var93 = var75;
                  }

                  String var32 = "dialog_id";
                  NotificationCompat.Action var98;
                  if ((!var28 || var2) && var59 && !SharedConfig.isWaitingForPasscodeEnter) {
                     Intent var99 = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                     var99.putExtra("dialog_id", var9);
                     var99.putExtra("max_id", var18);
                     var99.putExtra("currentAccount", this.currentAccount);
                     PendingIntent var33 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, var58, var99, 134217728);
                     RemoteInput.Builder var101 = new RemoteInput.Builder("extra_voice_reply");
                     var101.setLabel(LocaleController.getString("Reply", 2131560565));
                     RemoteInput var34 = var101.build();
                     String var102;
                     if (var19 < 0) {
                        var102 = LocaleController.formatString("ReplyToGroup", 2131560566, var23);
                     } else {
                        var102 = LocaleController.formatString("ReplyToUser", 2131560567, var23);
                     }

                     NotificationCompat.Action.Builder var103 = new NotificationCompat.Action.Builder(2131165467, var102, var33);
                     var103.setAllowGeneratedReplies(true);
                     var103.setSemanticAction(1);
                     var103.addRemoteInput(var34);
                     var103.setShowsUserInterface(false);
                     var98 = var103.build();
                  } else {
                     var98 = null;
                  }

                  Integer var35 = var58;
                  var58 = (Integer)this.pushDialogs.get(var9);
                  Integer var81 = var58;
                  if (var58 == null) {
                     var81 = 0;
                  }

                  var7 = Math.max(var81, var17.size());
                  if (var7 > 1 && VERSION.SDK_INT < 28) {
                     var65 = String.format("%1$s (%2$d)", var23, var7);
                  } else {
                     var65 = var23;
                  }

                  NotificationCompat.MessagingStyle var39 = new NotificationCompat.MessagingStyle("");
                  if (VERSION.SDK_INT < 28 || var19 < 0 && !var28) {
                     var39.setConversationTitle(var65);
                  }

                  if (VERSION.SDK_INT < 28 || !var28 && var19 < 0) {
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  var39.setGroupConversation(var2);
                  StringBuilder var60 = new StringBuilder();
                  String[] var109 = new String[1];
                  boolean[] var111 = new boolean[1];
                  var31 = var17.size() - 1;
                  ArrayList var94 = null;
                  var7 = 0;

                  int var42;
                  long var45;
                  StringBuilder var104;
                  for(var104 = var60; var31 >= 0; var7 = var42) {
                     MessageObject var40 = (MessageObject)var17.get(var31);
                     String var41 = this.getShortStringForMessage(var40, var109, var111);
                     ArrayList var74;
                     if (var41 == null) {
                        if (BuildVars.LOGS_ENABLED) {
                           var60 = new StringBuilder();
                           var60.append("message text is null for ");
                           var60.append(var40.getId());
                           var60.append(" did = ");
                           var60.append(var40.getDialogId());
                           FileLog.w(var60.toString());
                        }

                        var74 = var94;
                        var42 = var7;
                     } else {
                        if (var104.length() > 0) {
                           var104.append("\n\n");
                        }

                        if (var109[0] != null) {
                           var104.append(String.format("%1$s: %2$s", var109[0], var41));
                        } else {
                           var104.append(var41);
                        }

                        if (var19 > 0) {
                           var45 = (long)var19;
                        } else {
                           label424: {
                              if (var28) {
                                 var42 = -var19;
                              } else {
                                 if (var19 >= 0) {
                                    var45 = var9;
                                    break label424;
                                 }

                                 var42 = var40.getFromId();
                              }

                              var45 = (long)var42;
                           }
                        }

                        Person var47 = (Person)var22.get(var45);
                        if (var47 == null) {
                           Person.Builder var48 = new Person.Builder();
                           String var61;
                           if (var109[0] == null) {
                              var61 = "";
                           } else {
                              var61 = var109[0];
                           }

                           var48.setName(var61);
                           if (var111[0] && var19 != 0 && VERSION.SDK_INT >= 28) {
                              Object var62;
                              if (var19 <= 0 && !var28) {
                                 label409: {
                                    if (var19 < 0) {
                                       var42 = var40.getFromId();
                                       TLRPC.User var114 = MessagesController.getInstance(this.currentAccount).getUser(var42);
                                       TLRPC.User var63 = var114;
                                       if (var114 == null) {
                                          var114 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var42);
                                          var63 = var114;
                                          if (var114 != null) {
                                             MessagesController.getInstance(this.currentAccount).putUser(var114, true);
                                             var63 = var114;
                                          }
                                       }

                                       if (var63 != null) {
                                          TLRPC.UserProfilePhoto var66 = var63.photo;
                                          if (var66 != null) {
                                             TLRPC.FileLocation var68 = var66.photo_small;
                                             if (var68 != null && var68.volume_id != 0L && var68.local_id != 0) {
                                                var62 = FileLoader.getPathToAttach(var68, true);
                                                break label409;
                                             }
                                          }
                                       }
                                    }

                                    var62 = null;
                                 }
                              } else {
                                 var62 = var93;
                              }

                              this.loadRoundAvatar((File)var62, var48);
                           }

                           var47 = var48.build();
                           var22.put(var45, var47);
                        }

                        String var115 = var41;
                        if (var19 != 0) {
                           File var70;
                           Uri var73;
                           if (VERSION.SDK_INT >= 28 && !((ActivityManager)ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice()) {
                              if (var40.type != 1 && !var40.isSticker()) {
                                 var39.addMessage(var41, (long)var40.messageOwner.date * 1000L, var47);
                              } else {
                                 var70 = FileLoader.getPathToMessage(var40.messageOwner);
                                 NotificationCompat.MessagingStyle.Message var49 = new NotificationCompat.MessagingStyle.Message(var41, (long)var40.messageOwner.date * 1000L, var47);
                                 if (var40.isSticker()) {
                                    var41 = "image/webp";
                                 } else {
                                    var41 = "image/jpeg";
                                 }

                                 if (var70.exists()) {
                                    var73 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.provider", var70);
                                 } else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(var70.getName())) {
                                    Builder var50 = (new Builder()).scheme("content").authority("org.telegram.messenger.notification_image_provider").appendPath("msg_media_raw");
                                    StringBuilder var51 = new StringBuilder();
                                    var51.append(this.currentAccount);
                                    var51.append("");
                                    var73 = var50.appendPath(var51.toString()).appendPath(var70.getName()).appendQueryParameter("final_path", var70.getAbsolutePath()).build();
                                 } else {
                                    var73 = null;
                                 }

                                 if (var73 != null) {
                                    var49.setData(var41, var73);
                                    var39.addMessage(var49);
                                    ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", var73, 1);
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$hROO1aIM4eduzMv5uJ3U4yL97Bo(var73), 20000L);
                                    if (!TextUtils.isEmpty(var40.caption)) {
                                       var39.addMessage(var40.caption, (long)var40.messageOwner.date * 1000L, var47);
                                    }
                                 } else {
                                    var39.addMessage(var115, (long)var40.messageOwner.date * 1000L, var47);
                                 }
                              }
                           } else {
                              var39.addMessage(var41, (long)var40.messageOwner.date * 1000L, var47);
                           }

                           if (var40.isVoice()) {
                              List var116 = var39.getMessages();
                              if (!var116.isEmpty()) {
                                 var70 = FileLoader.getPathToMessage(var40.messageOwner);
                                 if (VERSION.SDK_INT >= 24) {
                                    try {
                                       var73 = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.provider", var70);
                                    } catch (Exception var52) {
                                       var73 = null;
                                    }
                                 } else {
                                    var73 = Uri.fromFile(var70);
                                 }

                                 if (var73 != null) {
                                    ((NotificationCompat.MessagingStyle.Message)var116.get(var116.size() - 1)).setData("audio/ogg", var73);
                                 }
                              }
                           }
                        } else {
                           var39.addMessage(var41, (long)var40.messageOwner.date * 1000L, var47);
                        }

                        var74 = var94;
                        var42 = var7;
                        if (var9 == 777000L) {
                           TLRPC.ReplyMarkup var117 = var40.messageOwner.reply_markup;
                           var74 = var94;
                           var42 = var7;
                           if (var117 != null) {
                              var74 = var117.rows;
                              var42 = var40.getId();
                           }
                        }
                     }

                     --var31;
                     var94 = var74;
                  }

                  Intent var96 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                  var60 = new StringBuilder();
                  var60.append("com.tmessages.openchat");
                  var60.append(Math.random());
                  var60.append(Integer.MAX_VALUE);
                  var96.setAction(var60.toString());
                  var96.setFlags(32768);
                  var96.addCategory("android.intent.category.LAUNCHER");
                  if (var19 != 0) {
                     if (var19 > 0) {
                        var96.putExtra("userId", var19);
                     } else {
                        var96.putExtra("chatId", -var19);
                     }
                  } else {
                     var96.putExtra("encId", var20);
                  }

                  var96.putExtra("currentAccount", this.currentAccount);
                  PendingIntent var85 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, var96, 1073741824);
                  NotificationCompat.WearableExtender var112 = new NotificationCompat.WearableExtender();
                  if (var98 != null) {
                     var112.addAction(var98);
                  }

                  Intent var90 = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                  var90.addFlags(32);
                  var90.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                  var90.putExtra(var32, var9);
                  var90.putExtra("max_id", var18);
                  var90.putExtra("currentAccount", this.currentAccount);
                  PendingIntent var91 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, var35, var90, 134217728);
                  var87 = "currentAccount";
                  NotificationCompat.Action.Builder var92 = new NotificationCompat.Action.Builder(2131165591, LocaleController.getString("MarkAsRead", 2131559807), var91);
                  var92.setSemanticAction(2);
                  var92.setShowsUserInterface(false);
                  NotificationCompat.Action var110 = var92.build();
                  if (var19 != 0) {
                     if (var19 > 0) {
                        var67 = new StringBuilder();
                        var67.append("tguser");
                        var67.append(var19);
                        var67.append("_");
                        var67.append(var18);
                        var65 = var67.toString();
                     } else {
                        var67 = new StringBuilder();
                        var67.append("tgchat");
                        var67.append(-var19);
                        var67.append("_");
                        var67.append(var18);
                        var65 = var67.toString();
                     }
                  } else if (var9 != globalSecretChatId) {
                     var67 = new StringBuilder();
                     var67.append("tgenc");
                     var67.append(var20);
                     var67.append("_");
                     var67.append(var18);
                     var65 = var67.toString();
                  } else {
                     var65 = null;
                  }

                  if (var65 != null) {
                     var112.setDismissalId(var65);
                     NotificationCompat.WearableExtender var105 = new NotificationCompat.WearableExtender();
                     StringBuilder var118 = new StringBuilder();
                     var118.append("summary_");
                     var118.append(var65);
                     var105.setDismissalId(var118.toString());
                     var1.extend(var105);
                  }

                  var67 = new StringBuilder();
                  var67.append("tgaccount");
                  var67.append(UserConfig.getInstance(this.currentAccount).getClientUserId());
                  var112.setBridgeTag(var67.toString());
                  var45 = (long)((MessageObject)var17.get(0)).messageOwner.date * 1000L;
                  NotificationCompat.Builder var106 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                  var106.setContentTitle(var23);
                  var106.setSmallIcon(2131165698);
                  var106.setContentText(var104.toString());
                  var106.setAutoCancel(true);
                  var106.setNumber(var17.size());
                  var106.setColor(-15618822);
                  var106.setGroupSummary(false);
                  var106.setWhen(var45);
                  var106.setShowWhen(true);
                  var67 = new StringBuilder();
                  var67.append("sdid_");
                  var67.append(var9);
                  var106.setShortcutId(var67.toString());
                  var106.setStyle(var39);
                  var106.setContentIntent(var85);
                  var106.extend(var112);
                  var67 = new StringBuilder();
                  var67.append("");
                  var67.append(Long.MAX_VALUE - var45);
                  var106.setSortKey(var67.toString());
                  var106.setCategory("msg");
                  if (var14) {
                     var106.setGroup(this.notificationGroup);
                     var106.setGroupAlertBehavior(1);
                  }

                  if (var98 != null) {
                     var106.addAction(var98);
                  }

                  var106.addAction(var110);
                  if (this.pushDialogs.size() == 1 && !TextUtils.isEmpty(var3)) {
                     var106.setSubText(var3);
                  }

                  if (var19 == 0) {
                     var106.setLocalOnly(true);
                  }

                  if (var75 != null && VERSION.SDK_INT < 28) {
                     var106.setLargeIcon(var75);
                  }

                  if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && var94 != null) {
                     var18 = var94.size();
                     byte var113 = 0;
                     var31 = var7;
                     var65 = var87;

                     for(var7 = var113; var7 < var18; ++var7) {
                        TLRPC.TL_keyboardButtonRow var79 = (TLRPC.TL_keyboardButtonRow)var94.get(var7);
                        var20 = var79.buttons.size();

                        for(var42 = 0; var42 < var20; ++var42) {
                           TLRPC.KeyboardButton var97 = (TLRPC.KeyboardButton)var79.buttons.get(var42);
                           if (var97 instanceof TLRPC.TL_keyboardButtonCallback) {
                              Intent var100 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                              var100.putExtra(var65, this.currentAccount);
                              var100.putExtra("did", var9);
                              byte[] var107 = var97.data;
                              if (var107 != null) {
                                 var100.putExtra("data", var107);
                              }

                              var100.putExtra("mid", var31);
                              var87 = var97.text;
                              Context var108 = ApplicationLoader.applicationContext;
                              var19 = this.lastButtonId++;
                              var106.addAction(0, var87, PendingIntent.getBroadcast(var108, var19, var100, 134217728));
                           }
                        }
                     }
                  }

                  if (var25 == null && var24 != null) {
                     var65 = var24.phone;
                     if (var65 != null && var65.length() > 0) {
                        var67 = new StringBuilder();
                        var67.append("tel:+");
                        var67.append(var24.phone);
                        var106.addPerson(var67.toString());
                     }
                  }

                  if (VERSION.SDK_INT >= 26) {
                     if (var14) {
                        var106.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                     } else {
                        var106.setChannelId(var4.getChannelId());
                     }
                  }

                  NotificationsController$1NotificationHolder var82 = new NotificationsController$1NotificationHolder(this, var35, var106.build());
                  var12 = var11;
                  var11.add(var82);
                  this.wearNotificationsIds.put(var9, var35);
                  break label532;
               }

               var12 = var11;
            }

            ++var16;
         }

         byte var71 = 0;
         if (var14) {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var55 = new StringBuilder();
               var55.append("show summary with id ");
               var55.append(this.notificationId);
               FileLog.d(var55.toString());
            }

            notificationManager.notify(this.notificationId, var4);
         } else {
            notificationManager.cancel(this.notificationId);
         }

         var16 = var11.size();
         var7 = 0;

         while(true) {
            int var69 = var71;
            LongSparseArray var56 = var13;
            if (var7 >= var16) {
               while(var69 < var56.size()) {
                  Integer var57 = (Integer)var56.valueAt(var69);
                  if (BuildVars.LOGS_ENABLED) {
                     var67 = new StringBuilder();
                     var67.append("cancel notification id ");
                     var67.append(var57);
                     FileLog.w(var67.toString());
                  }

                  notificationManager.cancel(var57);
                  ++var69;
               }

               return;
            }

            ((NotificationsController$1NotificationHolder)var11.get(var7)).call();
            ++var7;
         }
      }
   }

   private void showOrUpdateNotification(boolean var1) {
      if (!UserConfig.getInstance(this.currentAccount).isClientActivated() || this.pushMessages.isEmpty() || !SharedConfig.showNotificationsForAllAccounts && this.currentAccount != UserConfig.selectedAccount) {
         this.dismissNotification();
      } else {
         Exception var193;
         label1368: {
            Exception var10000;
            label1444: {
               MessageObject var2;
               SharedPreferences var3;
               int var4;
               boolean var10001;
               try {
                  ConnectionsManager.getInstance(this.currentAccount).resumeNetworkMaybe();
                  var2 = (MessageObject)this.pushMessages.get(0);
                  var3 = MessagesController.getNotificationsSettings(this.currentAccount);
                  var4 = var3.getInt("dismissDate", 0);
                  if (var2.messageOwner.date <= var4) {
                     this.dismissNotification();
                     return;
                  }
               } catch (Exception var104) {
                  var10000 = var104;
                  var10001 = false;
                  break label1444;
               }

               long var5;
               long var7;
               label1364: {
                  try {
                     var5 = var2.getDialogId();
                     if (var2.messageOwner.mentioned) {
                        var7 = (long)var2.messageOwner.from_id;
                        break label1364;
                     }
                  } catch (Exception var165) {
                     var10000 = var165;
                     var10001 = false;
                     break label1444;
                  }

                  var7 = var5;
               }

               int var9;
               label1450: {
                  try {
                     var2.getId();
                     if (var2.messageOwner.to_id.chat_id != 0) {
                        var9 = var2.messageOwner.to_id.chat_id;
                        break label1450;
                     }
                  } catch (Exception var164) {
                     var10000 = var164;
                     var10001 = false;
                     break label1444;
                  }

                  try {
                     var9 = var2.messageOwner.to_id.channel_id;
                  } catch (Exception var103) {
                     var10000 = var103;
                     var10001 = false;
                     break label1444;
                  }
               }

               int var10;
               try {
                  var10 = var2.messageOwner.to_id.user_id;
               } catch (Exception var102) {
                  var10000 = var102;
                  var10001 = false;
                  break label1444;
               }

               int var11;
               if (var10 == 0) {
                  try {
                     var11 = var2.messageOwner.from_id;
                  } catch (Exception var101) {
                     var10000 = var101;
                     var10001 = false;
                     break label1444;
                  }
               } else {
                  var11 = var10;

                  try {
                     if (var10 == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        var11 = var2.messageOwner.from_id;
                     }
                  } catch (Exception var100) {
                     var10000 = var100;
                     var10001 = false;
                     break label1444;
                  }
               }

               TLRPC.User var12;
               try {
                  var12 = MessagesController.getInstance(this.currentAccount).getUser(var11);
               } catch (Exception var99) {
                  var10000 = var99;
                  var10001 = false;
                  break label1444;
               }

               TLRPC.Chat var13;
               boolean var14;
               boolean var174;
               if (var9 != 0) {
                  label1345: {
                     label1344: {
                        try {
                           var13 = MessagesController.getInstance(this.currentAccount).getChat(var9);
                           if (ChatObject.isChannel(var13) && !var13.megagroup) {
                              break label1344;
                           }
                        } catch (Exception var163) {
                           var10000 = var163;
                           var10001 = false;
                           break label1444;
                        }

                        var174 = false;
                        break label1345;
                     }

                     var174 = true;
                  }

                  var14 = var174;
               } else {
                  var14 = false;
                  var13 = null;
               }

               try {
                  var10 = this.getNotifyOverride(var3, var7);
               } catch (Exception var98) {
                  var10000 = var98;
                  var10001 = false;
                  break label1444;
               }

               boolean var15;
               if (var10 == -1) {
                  try {
                     var15 = this.isGlobalNotificationsEnabled(var5);
                  } catch (Exception var97) {
                     var10000 = var97;
                     var10001 = false;
                     break label1444;
                  }
               } else if (var10 != 2) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               if (var1 && var15) {
                  var174 = false;
               } else {
                  var174 = true;
               }

               StringBuilder var16;
               int var17;
               int var18;
               if (!var174 && var5 == var7 && var13 != null) {
                  label1330: {
                     try {
                        var16 = new StringBuilder();
                        var16.append("custom_");
                        var16.append(var5);
                        if (var3.getBoolean(var16.toString(), false)) {
                           var16 = new StringBuilder();
                           var16.append("smart_max_count_");
                           var16.append(var5);
                           var17 = var3.getInt(var16.toString(), 2);
                           var16 = new StringBuilder();
                           var16.append("smart_delay_");
                           var16.append(var5);
                           var18 = var3.getInt(var16.toString(), 180);
                           break label1330;
                        }
                     } catch (Exception var162) {
                        var10000 = var162;
                        var10001 = false;
                        break label1444;
                     }

                     var18 = 180;
                     var17 = 2;
                  }

                  if (var17 != 0) {
                     Point var185;
                     try {
                        var185 = (Point)this.smartNotificationsDialogs.get(var5);
                     } catch (Exception var96) {
                        var10000 = var96;
                        var10001 = false;
                        break label1444;
                     }

                     if (var185 == null) {
                        try {
                           var185 = new Point(1, (int)(System.currentTimeMillis() / 1000L));
                           this.smartNotificationsDialogs.put(var5, var185);
                        } catch (Exception var95) {
                           var10000 = var95;
                           var10001 = false;
                           break label1444;
                        }
                     } else {
                        label1451: {
                           int var19;
                           try {
                              var19 = var185.y;
                           } catch (Exception var94) {
                              var10000 = var94;
                              var10001 = false;
                              break label1444;
                           }

                           try {
                              if ((long)(var19 + var18) < System.currentTimeMillis() / 1000L) {
                                 var185.set(1, (int)(System.currentTimeMillis() / 1000L));
                                 break label1451;
                              }
                           } catch (Exception var161) {
                              var10000 = var161;
                              var10001 = false;
                              break label1444;
                           }

                           try {
                              var10 = var185.x;
                           } catch (Exception var93) {
                              var10000 = var93;
                              var10001 = false;
                              break label1444;
                           }

                           if (var10 < var17) {
                              try {
                                 var185.set(var10 + 1, (int)(System.currentTimeMillis() / 1000L));
                              } catch (Exception var92) {
                                 var10000 = var92;
                                 var10001 = false;
                                 break label1444;
                              }

                              var174 = var174;
                           } else {
                              var174 = true;
                           }
                        }
                     }
                  }
               }

               String var21;
               boolean var22;
               boolean var23;
               boolean var24;
               boolean var25;
               try {
                  var21 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.getPath();
                  var22 = var3.getBoolean("EnableInAppSounds", true);
                  var23 = var3.getBoolean("EnableInAppVibrate", true);
                  var15 = var3.getBoolean("EnableInAppPreview", true);
                  var24 = var3.getBoolean("EnableInAppPriority", false);
                  var16 = new StringBuilder();
                  var16.append("custom_");
                  var16.append(var5);
                  var25 = var3.getBoolean(var16.toString(), false);
               } catch (Exception var91) {
                  var10000 = var91;
                  var10001 = false;
                  break label1444;
               }

               String var187;
               int var191;
               if (var25) {
                  try {
                     var16 = new StringBuilder();
                     var16.append("vibrate_");
                     var16.append(var5);
                     var191 = var3.getInt(var16.toString(), 0);
                     var16 = new StringBuilder();
                     var16.append("priority_");
                     var16.append(var5);
                     var18 = var3.getInt(var16.toString(), 3);
                     var16 = new StringBuilder();
                     var16.append("sound_path_");
                     var16.append(var5);
                     var187 = var3.getString(var16.toString(), (String)null);
                  } catch (Exception var90) {
                     var10000 = var90;
                     var10001 = false;
                     break label1444;
                  }
               } else {
                  var18 = 3;
                  var191 = 0;
                  var187 = null;
               }

               String var28;
               Intent var29;
               StringBuilder var167;
               byte var190;
               long[] var195;
               Uri var208;
               label1311: {
                  label1455: {
                     String var27;
                     int var182;
                     if (var9 != 0) {
                        if (var14) {
                           label1436: {
                              if (var187 != null) {
                                 label1425: {
                                    try {
                                       if (!var187.equals(var21)) {
                                          break label1425;
                                       }
                                    } catch (Exception var158) {
                                       var10000 = var158;
                                       var10001 = false;
                                       break label1455;
                                    }

                                    var27 = null;
                                    break label1436;
                                 }
                              }

                              var27 = var187;
                              if (var187 == null) {
                                 try {
                                    var27 = var3.getString("ChannelSoundPath", var21);
                                 } catch (Exception var157) {
                                    var10000 = var157;
                                    var10001 = false;
                                    break label1455;
                                 }
                              }
                           }

                           try {
                              var17 = var3.getInt("vibrate_channel", 0);
                              var182 = var3.getInt("priority_channel", 1);
                              var10 = var3.getInt("ChannelLed", -16776961);
                           } catch (Exception var156) {
                              var10000 = var156;
                              var10001 = false;
                              break label1455;
                           }

                           var187 = var27;
                        } else {
                           label1295: {
                              label1294: {
                                 if (var187 != null) {
                                    try {
                                       if (var187.equals(var21)) {
                                          break label1294;
                                       }
                                    } catch (Exception var159) {
                                       var10000 = var159;
                                       var10001 = false;
                                       break label1455;
                                    }
                                 }

                                 var27 = var187;
                                 if (var187 == null) {
                                    try {
                                       var27 = var3.getString("GroupSoundPath", var21);
                                    } catch (Exception var155) {
                                       var10000 = var155;
                                       var10001 = false;
                                       break label1455;
                                    }
                                 }
                                 break label1295;
                              }

                              var27 = null;
                           }

                           try {
                              var17 = var3.getInt("vibrate_group", 0);
                              var182 = var3.getInt("priority_group", 1);
                              var10 = var3.getInt("GroupLed", -16776961);
                           } catch (Exception var154) {
                              var10000 = var154;
                              var10001 = false;
                              break label1455;
                           }

                           var187 = var27;
                        }
                     } else if (var11 == 0) {
                        var10 = -16776961;
                        var17 = 0;
                        var182 = 0;
                     } else {
                        label1437: {
                           if (var187 != null) {
                              label1429: {
                                 try {
                                    if (!var187.equals(var21)) {
                                       break label1429;
                                    }
                                 } catch (Exception var160) {
                                    var10000 = var160;
                                    var10001 = false;
                                    break label1455;
                                 }

                                 var27 = null;
                                 break label1437;
                              }
                           }

                           var27 = var187;
                           if (var187 == null) {
                              try {
                                 var27 = var3.getString("GlobalSoundPath", var21);
                              } catch (Exception var153) {
                                 var10000 = var153;
                                 var10001 = false;
                                 break label1455;
                              }
                           }
                        }

                        try {
                           var17 = var3.getInt("vibrate_messages", 0);
                           var182 = var3.getInt("priority_messages", 1);
                           var10 = var3.getInt("MessagesLed", -16776961);
                        } catch (Exception var152) {
                           var10000 = var152;
                           var10001 = false;
                           break label1455;
                        }

                        var187 = var27;
                     }

                     if (var25) {
                        StringBuilder var205;
                        try {
                           var205 = new StringBuilder();
                        } catch (Exception var151) {
                           var10000 = var151;
                           var10001 = false;
                           break label1455;
                        }

                        try {
                           var205.append("color_");
                           var205.append(var5);
                        } catch (Exception var150) {
                           var10000 = var150;
                           var10001 = false;
                           break label1455;
                        }

                        try {
                           if (var3.contains(var205.toString())) {
                              var205 = new StringBuilder();
                              var205.append("color_");
                              var205.append(var5);
                              var10 = var3.getInt(var205.toString(), 0);
                           }
                        } catch (Exception var149) {
                           var10000 = var149;
                           var10001 = false;
                           break label1455;
                        }
                     }

                     if (var18 == 3) {
                        var18 = var182;
                     }

                     boolean var189;
                     if (var17 == 4) {
                        var17 = 0;
                        var189 = true;
                     } else {
                        var189 = false;
                     }

                     label1389: {
                        if (var17 == 2) {
                           var182 = var191;
                           if (var191 == 1) {
                              break label1389;
                           }

                           var182 = var191;
                           if (var191 == 3) {
                              break label1389;
                           }
                        }

                        if (var17 != 2) {
                           var182 = var191;
                           if (var191 == 2) {
                              break label1389;
                           }
                        }

                        if (var191 != 0 && var191 != 4) {
                           var182 = var191;
                        } else {
                           var182 = var17;
                        }
                     }

                     try {
                        var25 = ApplicationLoader.mainInterfacePaused;
                     } catch (Exception var148) {
                        var10000 = var148;
                        var10001 = false;
                        break label1455;
                     }

                     label722: {
                        if (!var25) {
                           if (!var22) {
                              var187 = null;
                           }

                           if (!var23) {
                              var182 = 2;
                           }

                           if (!var24) {
                              var17 = 0;
                              var28 = var187;
                              break label722;
                           }

                           var17 = var182;
                           var28 = var187;
                           if (var18 == 2) {
                              var17 = 1;
                              var28 = var187;
                              break label722;
                           }
                        } else {
                           var28 = var187;
                           var17 = var182;
                        }

                        var182 = var17;
                        var17 = var18;
                     }

                     var191 = var182;
                     if (var189) {
                        var191 = var182;
                        if (var182 != 2) {
                           label1246: {
                              try {
                                 var18 = audioManager.getRingerMode();
                              } catch (Exception var147) {
                                 var193 = var147;

                                 try {
                                    FileLog.e((Throwable)var193);
                                 } catch (Exception var146) {
                                    var10000 = var146;
                                    var10001 = false;
                                    break label1455;
                                 }

                                 var191 = var182;
                                 break label1246;
                              }

                              var191 = var182;
                              if (var18 != 0) {
                                 var191 = var182;
                                 if (var18 != 1) {
                                    var191 = 2;
                                 }
                              }
                           }
                        }
                     }

                     label1237: {
                        label1236: {
                           label1392: {
                              try {
                                 if (VERSION.SDK_INT < 26) {
                                    break label1392;
                                 }
                              } catch (Exception var145) {
                                 var10000 = var145;
                                 var10001 = false;
                                 break label1455;
                              }

                              if (var191 == 2) {
                                 try {
                                    var195 = new long[]{0L, 0L};
                                 } catch (Exception var143) {
                                    var10000 = var143;
                                    var10001 = false;
                                    break label1455;
                                 }
                              } else if (var191 == 1) {
                                 try {
                                    var195 = new long[4];
                                 } catch (Exception var142) {
                                    var10000 = var142;
                                    var10001 = false;
                                    break label1455;
                                 }

                                 var195[0] = 0L;
                                 var195[1] = 100L;
                                 var195[2] = 0L;
                                 var195[3] = 100L;
                              } else if (var191 != 0 && var191 != 4) {
                                 if (var191 == 3) {
                                    try {
                                       var195 = new long[2];
                                    } catch (Exception var141) {
                                       var10000 = var141;
                                       var10001 = false;
                                       break label1455;
                                    }

                                    var195[0] = 0L;
                                    var195[1] = 1000L;
                                 } else {
                                    var195 = null;
                                 }
                              } else {
                                 try {
                                    var195 = new long[0];
                                 } catch (Exception var140) {
                                    var10000 = var140;
                                    var10001 = false;
                                    break label1455;
                                 }
                              }

                              label1438: {
                                 if (var28 != null) {
                                    label1430: {
                                       try {
                                          if (var28.equals("NoSound")) {
                                             break label1430;
                                          }

                                          if (var28.equals(var21)) {
                                             var208 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
                                             break label1438;
                                          }
                                       } catch (Exception var144) {
                                          var10000 = var144;
                                          var10001 = false;
                                          break label1455;
                                       }

                                       try {
                                          var208 = Uri.parse(var28);
                                          break label1438;
                                       } catch (Exception var139) {
                                          var10000 = var139;
                                          var10001 = false;
                                          break label1455;
                                       }
                                    }
                                 }

                                 var208 = null;
                              }

                              if (var17 == 0) {
                                 var190 = 3;
                                 break label1237;
                              }

                              if (var17 == 1 || var17 == 2) {
                                 var190 = 4;
                                 break label1237;
                              }

                              if (var17 == 4) {
                                 var190 = 1;
                                 break label1237;
                              }

                              if (var17 == 5) {
                                 var190 = 2;
                                 break label1237;
                              }
                              break label1236;
                           }

                           var195 = null;
                           var208 = null;
                        }

                        var190 = 0;
                     }

                     if (var174) {
                        var17 = 0;
                        var10 = 0;
                        var191 = 0;
                        var28 = null;
                     }

                     try {
                        var29 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        var167 = new StringBuilder();
                        var167.append("com.tmessages.openchat");
                        var167.append(Math.random());
                        var167.append(Integer.MAX_VALUE);
                        var29.setAction(var167.toString());
                        var29.setFlags(32768);
                        break label1311;
                     } catch (Exception var138) {
                        var10000 = var138;
                        var10001 = false;
                     }
                  }

                  var193 = var10000;
                  break label1368;
               }

               TLRPC.FileLocation var170;
               label1183: {
                  var18 = (int)var5;
                  if (var18 != 0) {
                     label1395: {
                        NotificationsController var168 = this;

                        label1179: {
                           try {
                              if (var168.pushDialogs.size() != 1) {
                                 break label1179;
                              }
                           } catch (Exception var137) {
                              var10000 = var137;
                              var10001 = false;
                              break label1444;
                           }

                           if (var9 != 0) {
                              try {
                                 var29.putExtra("chatId", var9);
                              } catch (Exception var89) {
                                 var10000 = var89;
                                 var10001 = false;
                                 break label1444;
                              }
                           } else if (var11 != 0) {
                              try {
                                 var29.putExtra("userId", var11);
                              } catch (Exception var88) {
                                 var10000 = var88;
                                 var10001 = false;
                                 break label1444;
                              }
                           }
                        }

                        try {
                           if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                              break label1395;
                           }
                        } catch (Exception var136) {
                           var10000 = var136;
                           var10001 = false;
                           break label1444;
                        }

                        try {
                           if (var168.pushDialogs.size() != 1 || VERSION.SDK_INT >= 28) {
                              break label1395;
                           }
                        } catch (Exception var135) {
                           var10000 = var135;
                           var10001 = false;
                           break label1444;
                        }

                        if (var13 != null) {
                           TLRPC.Chat var169 = var13;

                           try {
                              if (var169.photo != null && var169.photo.photo_small != null && var169.photo.photo_small.volume_id != 0L && var169.photo.photo_small.local_id != 0) {
                                 var170 = var169.photo.photo_small;
                                 break label1183;
                              }
                           } catch (Exception var134) {
                              var10000 = var134;
                              var10001 = false;
                              break label1444;
                           }
                        } else if (var12 != null) {
                           TLRPC.User var171 = var12;

                           try {
                              if (var171.photo != null && var171.photo.photo_small != null && var171.photo.photo_small.volume_id != 0L && var171.photo.photo_small.local_id != 0) {
                                 var170 = var171.photo.photo_small;
                                 break label1183;
                              }
                           } catch (Exception var133) {
                              var10000 = var133;
                              var10001 = false;
                              break label1444;
                           }
                        }
                     }
                  } else {
                     try {
                        if (this.pushDialogs.size() == 1 && var5 != globalSecretChatId) {
                           var29.putExtra("encId", (int)(var5 >> 32));
                        }
                     } catch (Exception var87) {
                        var10000 = var87;
                        var10001 = false;
                        break label1444;
                     }
                  }

                  var170 = null;
               }

               NotificationsController var30 = this;
               TLRPC.User var31 = var12;
               TLRPC.Chat var32 = var13;
               var11 = var10;

               try {
                  var10 = var30.currentAccount;
               } catch (Exception var86) {
                  var10000 = var86;
                  var10001 = false;
                  break label1444;
               }

               String var33 = "currentAccount";

               Context var179;
               try {
                  var29.putExtra(var33, var10);
                  var179 = ApplicationLoader.applicationContext;
               } catch (Exception var85) {
                  var10000 = var85;
                  var10001 = false;
                  break label1444;
               }

               PendingIntent var34;
               try {
                  var34 = PendingIntent.getActivity(var179, 0, var29, 1073741824);
               } catch (Exception var84) {
                  var10000 = var84;
                  var10001 = false;
                  break label1444;
               }

               String var180;
               label1448: {
                  if (var9 != 0 && var13 == null || var12 == null) {
                     try {
                        if (var2.isFcmMessage()) {
                           var180 = var2.localName;
                           break label1448;
                        }
                     } catch (Exception var132) {
                        var10000 = var132;
                        var10001 = false;
                        break label1444;
                     }
                  }

                  if (var13 != null) {
                     try {
                        var180 = var32.title;
                     } catch (Exception var83) {
                        var10000 = var83;
                        var10001 = false;
                        break label1444;
                     }
                  } else {
                     try {
                        var180 = UserObject.getUserName(var31);
                     } catch (Exception var82) {
                        var10000 = var82;
                        var10001 = false;
                        break label1444;
                     }
                  }
               }

               boolean var20;
               String var35;
               label1439: {
                  if (var18 != 0) {
                     label1431: {
                        try {
                           if (var30.pushDialogs.size() > 1 || AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                              break label1431;
                           }
                        } catch (Exception var131) {
                           var10000 = var131;
                           var10001 = false;
                           break label1444;
                        }

                        var35 = var180;
                        var20 = true;
                        break label1439;
                     }
                  }

                  try {
                     var35 = LocaleController.getString("AppName", 2131558635);
                  } catch (Exception var81) {
                     var10000 = var81;
                     var10001 = false;
                     break label1444;
                  }

                  var20 = false;
               }

               try {
                  var10 = UserConfig.getActivatedAccountsCount();
               } catch (Exception var80) {
                  var10000 = var80;
                  var10001 = false;
                  break label1444;
               }

               StringBuilder var183;
               String var184;
               if (var10 > 1) {
                  label1399: {
                     try {
                        if (var30.pushDialogs.size() == 1) {
                           var184 = UserObject.getFirstName(UserConfig.getInstance(var30.currentAccount).getCurrentUser());
                           break label1399;
                        }
                     } catch (Exception var130) {
                        var10000 = var130;
                        var10001 = false;
                        break label1444;
                     }

                     try {
                        var183 = new StringBuilder();
                        var183.append(UserObject.getFirstName(UserConfig.getInstance(var30.currentAccount).getCurrentUser()));
                        var183.append("・");
                        var184 = var183.toString();
                     } catch (Exception var79) {
                        var10000 = var79;
                        var10001 = false;
                        break label1444;
                     }
                  }
               } else {
                  var184 = "";
               }

               String var202;
               label1107: {
                  StringBuilder var203;
                  label1106: {
                     label1105: {
                        label1400: {
                           try {
                              if (var30.pushDialogs.size() != 1) {
                                 break label1400;
                              }
                           } catch (Exception var129) {
                              var10000 = var129;
                              var10001 = false;
                              break label1444;
                           }

                           var202 = var184;

                           try {
                              if (VERSION.SDK_INT >= 23) {
                                 break label1105;
                              }
                           } catch (Exception var128) {
                              var10000 = var128;
                              var10001 = false;
                              break label1444;
                           }
                        }

                        try {
                           if (var30.pushDialogs.size() != 1) {
                              break label1106;
                           }

                           var203 = new StringBuilder();
                           var203.append(var184);
                           var203.append(LocaleController.formatPluralString("NewMessages", var30.total_unread_count));
                           var202 = var203.toString();
                        } catch (Exception var127) {
                           var10000 = var127;
                           var10001 = false;
                           break label1444;
                        }
                     }

                     var184 = var202;
                     break label1107;
                  }

                  try {
                     var203 = new StringBuilder();
                     var203.append(var184);
                     var203.append(LocaleController.formatString("NotificationMessagesPeopleDisplayOrder", 2131560053, LocaleController.formatPluralString("NewMessages", var30.total_unread_count), LocaleController.formatPluralString("FromChats", var30.pushDialogs.size())));
                     var184 = var203.toString();
                  } catch (Exception var78) {
                     var10000 = var78;
                     var10001 = false;
                     break label1444;
                  }
               }

               String var37 = var28;
               var28 = var184;

               NotificationCompat.Builder var38;
               try {
                  var38 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                  var38.setContentTitle(var35);
                  var38.setSmallIcon(2131165698);
                  var38.setAutoCancel(true);
                  var38.setNumber(var30.total_unread_count);
                  var38.setContentIntent(var34);
                  var38.setGroup(var30.notificationGroup);
                  var38.setGroupSummary(true);
                  var38.setShowWhen(true);
                  var38.setWhen((long)var2.messageOwner.date * 1000L);
                  var38.setColor(-15618822);
                  var38.setCategory("msg");
               } catch (Exception var77) {
                  var10000 = var77;
                  var10001 = false;
                  break label1444;
               }

               if (var13 == null && var12 != null) {
                  try {
                     if (var31.phone != null && var31.phone.length() > 0) {
                        var183 = new StringBuilder();
                        var183.append("tel:+");
                        var183.append(var31.phone);
                        var38.addPerson(var183.toString());
                     }
                  } catch (Exception var76) {
                     var10000 = var76;
                     var10001 = false;
                     break label1444;
                  }
               }

               String var166;
               MessageObject var172;
               String var173;
               byte var186;
               MessageObject var188;
               byte var196;
               TLRPC.FileLocation var214;
               label1401: {
                  boolean[] var209;
                  label1402: {
                     try {
                        if (var30.pushMessages.size() == 1) {
                           var188 = (MessageObject)var30.pushMessages.get(0);
                           var209 = new boolean[1];
                           var202 = var30.getStringForMessage(var188, false, var209, (boolean[])null);
                           var196 = var188.messageOwner.silent;
                           break label1402;
                        }
                     } catch (Exception var126) {
                        var10000 = var126;
                        var10001 = false;
                        break label1444;
                     }

                     boolean[] var39;
                     NotificationCompat.InboxStyle var215;
                     try {
                        var38.setContentText(var28);
                        var215 = new NotificationCompat.InboxStyle();
                        var215.setBigContentTitle(var35);
                        var9 = Math.min(10, var30.pushMessages.size());
                        var39 = new boolean[1];
                     } catch (Exception var71) {
                        var10000 = var71;
                        var10001 = false;
                        break label1444;
                     }

                     var18 = 0;
                     var186 = 2;

                     for(var184 = null; var18 < var9; ++var18) {
                        String var207;
                        try {
                           var172 = (MessageObject)var30.pushMessages.get(var18);
                           var207 = var30.getStringForMessage(var172, false, var39, (boolean[])null);
                        } catch (Exception var70) {
                           var10000 = var70;
                           var10001 = false;
                           break label1444;
                        }

                        if (var207 != null) {
                           try {
                              if (var172.messageOwner.date <= var4) {
                                 continue;
                              }
                           } catch (Exception var124) {
                              var10000 = var124;
                              var10001 = false;
                              break label1444;
                           }

                           var196 = var186;
                           if (var186 == 2) {
                              try {
                                 var196 = var172.messageOwner.silent;
                              } catch (Exception var69) {
                                 var10000 = var69;
                                 var10001 = false;
                                 break label1444;
                              }

                              var184 = var207;
                           }

                           var173 = var207;

                           label1071: {
                              try {
                                 if (var30.pushDialogs.size() != 1) {
                                    break label1071;
                                 }
                              } catch (Exception var125) {
                                 var10000 = var125;
                                 var10001 = false;
                                 break label1444;
                              }

                              var173 = var207;
                              if (var20) {
                                 if (var32 != null) {
                                    try {
                                       var167 = new StringBuilder();
                                       var167.append(" @ ");
                                       var167.append(var35);
                                       var173 = var207.replace(var167.toString(), "");
                                    } catch (Exception var68) {
                                       var10000 = var68;
                                       var10001 = false;
                                       break label1444;
                                    }
                                 } else if (var39[0]) {
                                    try {
                                       var167 = new StringBuilder();
                                       var167.append(var35);
                                       var167.append(": ");
                                       var173 = var207.replace(var167.toString(), "");
                                    } catch (Exception var67) {
                                       var10000 = var67;
                                       var10001 = false;
                                       break label1444;
                                    }
                                 } else {
                                    try {
                                       var167 = new StringBuilder();
                                       var167.append(var35);
                                       var167.append(" ");
                                       var173 = var207.replace(var167.toString(), "");
                                    } catch (Exception var66) {
                                       var10000 = var66;
                                       var10001 = false;
                                       break label1444;
                                    }
                                 }
                              }
                           }

                           try {
                              var215.addLine(var173);
                           } catch (Exception var65) {
                              var10000 = var65;
                              var10001 = false;
                              break label1444;
                           }

                           var186 = var196;
                        }
                     }

                     var166 = var33;

                     try {
                        var215.setSummaryText(var28);
                        var38.setStyle(var215);
                     } catch (Exception var64) {
                        var10000 = var64;
                        var10001 = false;
                        break label1444;
                     }

                     var188 = var2;
                     var214 = var170;
                     var173 = var184;
                     var196 = var186;
                     break label1401;
                  }

                  if (var202 == null) {
                     return;
                  }

                  if (var20) {
                     if (var13 != null) {
                        try {
                           var183 = new StringBuilder();
                           var183.append(" @ ");
                           var183.append(var35);
                           var184 = var202.replace(var183.toString(), "");
                        } catch (Exception var75) {
                           var10000 = var75;
                           var10001 = false;
                           break label1444;
                        }
                     } else if (var209[0]) {
                        try {
                           var183 = new StringBuilder();
                           var183.append(var35);
                           var183.append(": ");
                           var184 = var202.replace(var183.toString(), "");
                        } catch (Exception var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label1444;
                        }
                     } else {
                        try {
                           var183 = new StringBuilder();
                           var183.append(var35);
                           var183.append(" ");
                           var184 = var202.replace(var183.toString(), "");
                        } catch (Exception var73) {
                           var10000 = var73;
                           var10001 = false;
                           break label1444;
                        }
                     }
                  } else {
                     var184 = var202;
                  }

                  try {
                     var38.setContentText(var184);
                     NotificationCompat.BigTextStyle var218 = new NotificationCompat.BigTextStyle();
                     var218.bigText(var184);
                     var38.setStyle(var218);
                  } catch (Exception var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label1444;
                  }

                  var188 = var2;
                  var166 = var33;
                  var214 = var170;
                  var173 = var202;
               }

               Intent var219;
               try {
                  var219 = new Intent(ApplicationLoader.applicationContext, NotificationDismissReceiver.class);
               } catch (Exception var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label1444;
               }

               MessageObject var210 = var188;

               try {
                  var219.putExtra("messageDate", var210.messageOwner.date);
                  var219.putExtra(var166, var30.currentAccount);
                  var38.setDeleteIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 1, var219, 134217728));
               } catch (Exception var62) {
                  var10000 = var62;
                  var10001 = false;
                  break label1444;
               }

               if (var214 != null) {
                  BitmapDrawable var194;
                  try {
                     var194 = ImageLoader.getInstance().getImageFromMemory(var214, (String)null, "50_50");
                  } catch (Exception var61) {
                     var10000 = var61;
                     var10001 = false;
                     break label1444;
                  }

                  if (var194 != null) {
                     try {
                        var38.setLargeIcon(var194.getBitmap());
                     } catch (Exception var60) {
                        var10000 = var60;
                        var10001 = false;
                        break label1444;
                     }
                  } else {
                     label1404: {
                        float var40;
                        File var197;
                        Options var216;
                        try {
                           var197 = FileLoader.getPathToAttach(var214, true);
                           if (!var197.exists()) {
                              break label1404;
                           }

                           var40 = 160.0F / (float)AndroidUtilities.dp(50.0F);
                           var216 = new Options();
                        } catch (Throwable var123) {
                           var10001 = false;
                           break label1404;
                        }

                        if (var40 < 1.0F) {
                           var10 = 1;
                        } else {
                           var10 = (int)var40;
                        }

                        Bitmap var198;
                        try {
                           var216.inSampleSize = var10;
                           var198 = BitmapFactory.decodeFile(var197.getAbsolutePath(), var216);
                        } catch (Throwable var122) {
                           var10001 = false;
                           break label1404;
                        }

                        if (var198 != null) {
                           try {
                              var38.setLargeIcon(var198);
                           } catch (Throwable var121) {
                              var10001 = false;
                           }
                        }
                     }
                  }
               }

               label1037: {
                  label1036: {
                     label1035: {
                        label1446: {
                           var33 = null;
                           if (var1 && var196 != 1) {
                              if (var17 == 0) {
                                 label1025: {
                                    try {
                                       var38.setPriority(0);
                                       if (VERSION.SDK_INT < 26) {
                                          break label1025;
                                       }
                                    } catch (Exception var120) {
                                       var10000 = var120;
                                       var10001 = false;
                                       break label1444;
                                    }

                                    var186 = 3;
                                    break label1037;
                                 }
                              } else if (var17 != 1 && var17 != 2) {
                                 if (var17 == 4) {
                                    try {
                                       var38.setPriority(-2);
                                       if (VERSION.SDK_INT >= 26) {
                                          break label1036;
                                       }
                                    } catch (Exception var116) {
                                       var10000 = var116;
                                       var10001 = false;
                                       break label1444;
                                    }
                                 } else if (var17 == 5) {
                                    try {
                                       var38.setPriority(-1);
                                       if (VERSION.SDK_INT >= 26) {
                                          break label1446;
                                       }
                                    } catch (Exception var119) {
                                       var10000 = var119;
                                       var10001 = false;
                                       break label1444;
                                    }
                                 }
                              } else {
                                 try {
                                    var38.setPriority(1);
                                    if (VERSION.SDK_INT >= 26) {
                                       break label1035;
                                    }
                                 } catch (Exception var117) {
                                    var10000 = var117;
                                    var10001 = false;
                                    break label1444;
                                 }
                              }
                           } else {
                              try {
                                 var38.setPriority(-1);
                                 if (VERSION.SDK_INT >= 26) {
                                    break label1446;
                                 }
                              } catch (Exception var118) {
                                 var10000 = var118;
                                 var10001 = false;
                                 break label1444;
                              }
                           }

                           var186 = 0;
                           break label1037;
                        }

                        var186 = 2;
                        break label1037;
                     }

                     var186 = 4;
                     break label1037;
                  }

                  var186 = 1;
               }

               long[] var199;
               Uri var217;
               if (var196 != 1 && !var174) {
                  label1408: {
                     label1000: {
                        try {
                           if (ApplicationLoader.mainInterfacePaused) {
                              break label1000;
                           }
                        } catch (Exception var115) {
                           var10000 = var115;
                           var10001 = false;
                           break label1444;
                        }

                        if (!var15) {
                           break label1408;
                        }
                     }

                     var184 = var173;

                     try {
                        if (var173.length() > 100) {
                           var183 = new StringBuilder();
                           var183.append(var173.substring(0, 100).replace('\n', ' ').trim());
                           var183.append("...");
                           var184 = var183.toString();
                        }
                     } catch (Exception var114) {
                        var10000 = var114;
                        var10001 = false;
                        break label1444;
                     }

                     try {
                        var38.setTicker(var184);
                     } catch (Exception var59) {
                        var10000 = var59;
                        var10001 = false;
                        break label1444;
                     }
                  }

                  Uri var200;
                  label987: {
                     label1409: {
                        try {
                           if (MediaController.getInstance().isRecordingAudio()) {
                              break label1409;
                           }
                        } catch (Exception var112) {
                           var10000 = var112;
                           var10001 = false;
                           break label1444;
                        }

                        if (var37 != null) {
                           label1433: {
                              label984: {
                                 try {
                                    if (var37.equals("NoSound")) {
                                       break label1433;
                                    }

                                    if (VERSION.SDK_INT < 26) {
                                       break label984;
                                    }

                                    if (var37.equals(var21)) {
                                       var200 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
                                       break label987;
                                    }
                                 } catch (Exception var113) {
                                    var10000 = var113;
                                    var10001 = false;
                                    break label1444;
                                 }

                                 try {
                                    var200 = Uri.parse(var37);
                                    break label987;
                                 } catch (Exception var58) {
                                    var10000 = var58;
                                    var10001 = false;
                                    break label1444;
                                 }
                              }

                              try {
                                 if (var37.equals(var21)) {
                                    var38.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI, 5);
                                    break label1433;
                                 }
                              } catch (Exception var111) {
                                 var10000 = var111;
                                 var10001 = false;
                                 break label1444;
                              }

                              label961: {
                                 try {
                                    if (VERSION.SDK_INT < 24 || !var37.startsWith("file://")) {
                                       break label961;
                                    }

                                    var15 = AndroidUtilities.isInternalUri(Uri.parse(var37));
                                 } catch (Exception var110) {
                                    var10000 = var110;
                                    var10001 = false;
                                    break label1444;
                                 }

                                 if (!var15) {
                                    try {
                                       Context var201 = ApplicationLoader.applicationContext;
                                       File var176 = new File(var37.replace("file://", ""));
                                       Uri var177 = FileProvider.getUriForFile(var201, "org.telegram.messenger.provider", var176);
                                       ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", var177, 1);
                                       var38.setSound(var177, 5);
                                       break label1433;
                                    } catch (Exception var57) {
                                       try {
                                          var38.setSound(Uri.parse(var37), 5);
                                          break label1433;
                                       } catch (Exception var56) {
                                          var10000 = var56;
                                          var10001 = false;
                                          break label1444;
                                       }
                                    }
                                 }
                              }

                              try {
                                 var38.setSound(Uri.parse(var37), 5);
                              } catch (Exception var55) {
                                 var10000 = var55;
                                 var10001 = false;
                                 break label1444;
                              }
                           }
                        }
                     }

                     var200 = null;
                  }

                  if (var11 != 0) {
                     try {
                        var38.setLights(var11, 1000, 1000);
                     } catch (Exception var54) {
                        var10000 = var54;
                        var10001 = false;
                        break label1444;
                     }
                  }

                  long[] var178;
                  label1441: {
                     if (var191 != 2) {
                        label1452: {
                           label945:
                           try {
                              if (!MediaController.getInstance().isRecordingAudio()) {
                                 break label945;
                              }
                              break label1452;
                           } catch (Exception var109) {
                              var10000 = var109;
                              var10001 = false;
                              break label1444;
                           }

                           if (var191 == 1) {
                              try {
                                 var178 = new long[4];
                              } catch (Exception var53) {
                                 var10000 = var53;
                                 var10001 = false;
                                 break label1444;
                              }

                              var178[0] = 0L;
                              var178[1] = 100L;
                              var178[2] = 0L;
                              var178[3] = 100L;

                              try {
                                 var38.setVibrate(var178);
                                 break label1441;
                              } catch (Exception var52) {
                                 var10000 = var52;
                                 var10001 = false;
                                 break label1444;
                              }
                           }

                           if (var191 != 0 && var191 != 4) {
                              if (var191 == 3) {
                                 try {
                                    var178 = new long[2];
                                 } catch (Exception var51) {
                                    var10000 = var51;
                                    var10001 = false;
                                    break label1444;
                                 }

                                 var178[0] = 0L;
                                 var178[1] = 1000L;

                                 try {
                                    var38.setVibrate(var178);
                                    break label1441;
                                 } catch (Exception var50) {
                                    var10000 = var50;
                                    var10001 = false;
                                    break label1444;
                                 }
                              } else {
                                 var178 = (long[])var33;
                                 break label1441;
                              }
                           }

                           try {
                              var38.setDefaults(2);
                              var178 = new long[0];
                              break label1441;
                           } catch (Exception var49) {
                              var10000 = var49;
                              var10001 = false;
                              break label1444;
                           }
                        }
                     }

                     try {
                        var178 = new long[2];
                     } catch (Exception var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label1444;
                     }

                     var178[0] = 0L;
                     var178[1] = 0L;

                     try {
                        var38.setVibrate(var178);
                     } catch (Exception var47) {
                        var10000 = var47;
                        var10001 = false;
                        break label1444;
                     }
                  }

                  var217 = var200;
                  var199 = var178;
               } else {
                  try {
                     var199 = new long[2];
                  } catch (Exception var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label1444;
                  }

                  var199[0] = 0L;
                  var199[1] = 0L;

                  try {
                     var38.setVibrate(var199);
                  } catch (Exception var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label1444;
                  }

                  var217 = null;
               }

               var17 = var11;

               boolean var181;
               label1416: {
                  ArrayList var220;
                  label930: {
                     try {
                        if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && var210.getDialogId() == 777000L && var210.messageOwner.reply_markup != null) {
                           var220 = var210.messageOwner.reply_markup.rows;
                           var4 = var220.size();
                           break label930;
                        }
                     } catch (Exception var108) {
                        var10000 = var108;
                        var10001 = false;
                        break label1444;
                     }

                     var173 = var28;
                     var181 = false;
                     break label1416;
                  }

                  var191 = 0;
                  var181 = false;
                  var172 = var188;

                  boolean var175;
                  for(ArrayList var211 = var220; var191 < var4; var181 = var175) {
                     TLRPC.TL_keyboardButtonRow var221;
                     try {
                        var221 = (TLRPC.TL_keyboardButtonRow)var211.get(var191);
                        var18 = var221.buttons.size();
                     } catch (Exception var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label1444;
                     }

                     byte var192 = 0;
                     var175 = var181;
                     var11 = var4;

                     for(var4 = var192; var4 < var18; ++var4) {
                        Intent var212;
                        TLRPC.KeyboardButton var222;
                        try {
                           var222 = (TLRPC.KeyboardButton)var221.buttons.get(var4);
                           if (!(var222 instanceof TLRPC.TL_keyboardButtonCallback)) {
                              continue;
                           }

                           var212 = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                           var212.putExtra(var166, var30.currentAccount);
                           var212.putExtra("did", var5);
                           if (var222.data != null) {
                              var212.putExtra("data", var222.data);
                           }
                        } catch (Exception var107) {
                           var10000 = var107;
                           var10001 = false;
                           break label1444;
                        }

                        try {
                           var212.putExtra("mid", var172.getId());
                           var37 = var222.text;
                           Context var213 = ApplicationLoader.applicationContext;
                           var9 = var30.lastButtonId++;
                           var38.addAction(0, var37, PendingIntent.getBroadcast(var213, var9, var212, 134217728));
                        } catch (Exception var43) {
                           var10000 = var43;
                           var10001 = false;
                           break label1444;
                        }

                        var175 = true;
                     }

                     ++var191;
                     var4 = var11;
                  }

                  var173 = var28;
               }

               if (!var181) {
                  label895: {
                     Intent var204;
                     try {
                        if (VERSION.SDK_INT >= 24 || SharedConfig.passcodeHash.length() != 0 || !this.hasMessagesToReply()) {
                           break label895;
                        }

                        var204 = new Intent(ApplicationLoader.applicationContext, PopupReplyReceiver.class);
                        var204.putExtra(var166, var30.currentAccount);
                        if (VERSION.SDK_INT <= 19) {
                           var38.addAction(2131165418, LocaleController.getString("Reply", 2131560565), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, var204, 134217728));
                           break label895;
                        }
                     } catch (Exception var106) {
                        var10000 = var106;
                        var10001 = false;
                        break label1444;
                     }

                     try {
                        var38.addAction(2131165417, LocaleController.getString("Reply", 2131560565), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, var204, 134217728));
                     } catch (Exception var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label1444;
                     }
                  }
               }

               try {
                  if (VERSION.SDK_INT >= 26) {
                     var38.setChannelId(this.validateChannelId(var5, var180, var199, var17, var217, var186, var195, var208, var190));
                  }
               } catch (Exception var105) {
                  var10000 = var105;
                  var10001 = false;
                  break label1444;
               }

               try {
                  var30.showExtraNotifications(var38, var1, var173);
                  this.scheduleNotificationRepeat();
                  return;
               } catch (Exception var41) {
                  var10000 = var41;
                  var10001 = false;
               }
            }

            var193 = var10000;
         }

         FileLog.e((Throwable)var193);
      }
   }

   @TargetApi(26)
   private String validateChannelId(long var1, String var3, long[] var4, int var5, Uri var6, int var7, long[] var8, Uri var9, int var10) {
      SharedPreferences var11 = MessagesController.getNotificationsSettings(this.currentAccount);
      StringBuilder var19 = new StringBuilder();
      var19.append("org.telegram.key");
      var19.append(var1);
      String var20 = var19.toString();
      String var12 = var11.getString(var20, (String)null);
      var19 = new StringBuilder();
      var19.append(var20);
      var19.append("_s");
      String var13 = var11.getString(var19.toString(), (String)null);
      var19 = new StringBuilder();

      for(var10 = 0; var10 < var4.length; ++var10) {
         var19.append(var4[var10]);
      }

      var19.append(var5);
      if (var6 != null) {
         var19.append(var6.toString());
      }

      var19.append(var7);
      String var14 = Utilities.MD5(var19.toString());
      String var21 = var12;
      if (var12 != null) {
         var21 = var12;
         if (!var13.equals(var14)) {
            systemNotificationManager.deleteNotificationChannel(var12);
            var21 = null;
         }
      }

      var12 = var21;
      if (var21 == null) {
         var19 = new StringBuilder();
         var19.append(this.currentAccount);
         var19.append("channel");
         var19.append(var1);
         var19.append("_");
         var19.append(Utilities.random.nextLong());
         var12 = var19.toString();
         NotificationChannel var15 = new NotificationChannel(var12, var3, var7);
         if (var5 != 0) {
            var15.enableLights(true);
            var15.setLightColor(var5);
         }

         if (!this.isEmptyVibration(var4)) {
            var15.enableVibration(true);
            if (var4 != null && var4.length > 0) {
               var15.setVibrationPattern(var4);
            }
         } else {
            var15.enableVibration(false);
         }

         android.media.AudioAttributes.Builder var17 = new android.media.AudioAttributes.Builder();
         var17.setContentType(4);
         var17.setUsage(5);
         if (var6 != null) {
            var15.setSound(var6, var17.build());
         } else {
            var15.setSound((Uri)null, var17.build());
         }

         systemNotificationManager.createNotificationChannel(var15);
         Editor var16 = var11.edit().putString(var20, var12);
         StringBuilder var18 = new StringBuilder();
         var18.append(var20);
         var18.append("_s");
         var16.putString(var18.toString(), var14).commit();
      }

      return var12;
   }

   public void cleanup() {
      this.popupMessages.clear();
      this.popupReplyMessages.clear();
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$A9SCTrujp78_YxIRivW7UAoIEBo(this));
   }

   @TargetApi(26)
   public void deleteAllNotificationChannels() {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$iv6fUe9w_2c54mbdiQOLFrNptrg(this));
   }

   @TargetApi(26)
   public void deleteNotificationChannel(long var1) {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$eYqBa_GxEYzKlHSSB2VWl64XX2Q(this, var1));
   }

   protected void forceShowPopupForReply() {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$eQV_fs8YB0lhGMYS2TKm4CX_EZk(this));
   }

   public String getGlobalNotificationsKey(int var1) {
      if (var1 == 0) {
         return "EnableGroup2";
      } else {
         return var1 == 1 ? "EnableAll2" : "EnableChannel2";
      }
   }

   public int getTotalUnreadCount() {
      return this.total_unread_count;
   }

   public boolean hasMessagesToReply() {
      for(int var1 = 0; var1 < this.pushMessages.size(); ++var1) {
         MessageObject var2 = (MessageObject)this.pushMessages.get(var1);
         long var3 = var2.getDialogId();
         TLRPC.Message var5 = var2.messageOwner;
         if ((!var5.mentioned || !(var5.action instanceof TLRPC.TL_messageActionPinMessage)) && (int)var3 != 0 && (var2.messageOwner.to_id.channel_id == 0 || var2.isMegagroup())) {
            return true;
         }
      }

      return false;
   }

   public void hideNotifications() {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$_1VL5AJa2XU8eBaEZNLOYhMw8bE(this));
   }

   public boolean isGlobalNotificationsEnabled(int var1) {
      SharedPreferences var2 = MessagesController.getNotificationsSettings(this.currentAccount);
      String var3 = this.getGlobalNotificationsKey(var1);
      boolean var4 = false;
      if (var2.getInt(var3, 0) < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
         var4 = true;
      }

      return var4;
   }

   public boolean isGlobalNotificationsEnabled(long var1) {
      int var3 = (int)var1;
      byte var5;
      if (var3 < 0) {
         TLRPC.Chat var4 = MessagesController.getInstance(this.currentAccount).getChat(-var3);
         if (ChatObject.isChannel(var4) && !var4.megagroup) {
            var5 = 2;
         } else {
            var5 = 0;
         }
      } else {
         var5 = 1;
      }

      return this.isGlobalNotificationsEnabled(var5);
   }

   // $FF: synthetic method
   public void lambda$cleanup$1$NotificationsController() {
      this.opened_dialog_id = 0L;
      int var1 = 0;
      this.total_unread_count = 0;
      this.personal_count = 0;
      this.pushMessages.clear();
      this.pushMessagesDict.clear();
      this.fcmRandomMessagesDict.clear();
      this.pushDialogs.clear();
      this.wearNotificationsIds.clear();
      this.lastWearNotifiedMessageId.clear();
      this.delayedPushMessages.clear();
      this.notifyCheck = false;
      this.lastBadgeCount = 0;

      try {
         if (this.notificationDelayWakelock.isHeld()) {
            this.notificationDelayWakelock.release();
         }
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      this.dismissNotification();
      this.setBadge(this.getTotalAllUnreadCount());
      Editor var2 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
      var2.clear();
      var2.commit();
      if (VERSION.SDK_INT >= 26) {
         Throwable var10000;
         label38: {
            String var3;
            int var4;
            List var10;
            boolean var10001;
            try {
               StringBuilder var9 = new StringBuilder();
               var9.append(this.currentAccount);
               var9.append("channel");
               var3 = var9.toString();
               var10 = systemNotificationManager.getNotificationChannels();
               var4 = var10.size();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label38;
            }

            while(true) {
               if (var1 >= var4) {
                  return;
               }

               try {
                  String var5 = ((NotificationChannel)var10.get(var1)).getId();
                  if (var5.startsWith(var3)) {
                     systemNotificationManager.deleteNotificationChannel(var5);
                  }
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break;
               }

               ++var1;
            }
         }

         Throwable var11 = var10000;
         FileLog.e(var11);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteAllNotificationChannels$30$NotificationsController() {
      if (VERSION.SDK_INT >= 26) {
         Exception var10000;
         label48: {
            Iterator var3;
            Editor var9;
            boolean var10001;
            try {
               SharedPreferences var1 = MessagesController.getNotificationsSettings(this.currentAccount);
               Map var2 = var1.getAll();
               var9 = var1.edit();
               var3 = var2.entrySet().iterator();
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label48;
            }

            while(true) {
               String var4;
               label45: {
                  try {
                     while(var3.hasNext()) {
                        Entry var11 = (Entry)var3.next();
                        var4 = (String)var11.getKey();
                        if (var4.startsWith("org.telegram.key")) {
                           if (!var4.endsWith("_s")) {
                              systemNotificationManager.deleteNotificationChannel((String)var11.getValue());
                           }
                           break label45;
                        }
                     }
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  try {
                     var9.commit();
                     return;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var9.remove(var4);
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }
            }
         }

         Exception var10 = var10000;
         FileLog.e((Throwable)var10);
      }
   }

   // $FF: synthetic method
   public void lambda$deleteNotificationChannel$29$NotificationsController(long var1) {
      if (VERSION.SDK_INT >= 26) {
         Exception var10000;
         label30: {
            SharedPreferences var3;
            String var5;
            String var10;
            boolean var10001;
            try {
               var3 = MessagesController.getNotificationsSettings(this.currentAccount);
               StringBuilder var4 = new StringBuilder();
               var4.append("org.telegram.key");
               var4.append(var1);
               var10 = var4.toString();
               var5 = var3.getString(var10, (String)null);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label30;
            }

            if (var5 == null) {
               return;
            }

            try {
               Editor var6 = var3.edit().remove(var10);
               StringBuilder var9 = new StringBuilder();
               var9.append(var10);
               var9.append("_s");
               var6.remove(var9.toString()).commit();
               systemNotificationManager.deleteNotificationChannel(var5);
               return;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var11 = var10000;
         FileLog.e((Throwable)var11);
      }
   }

   // $FF: synthetic method
   public void lambda$forceShowPopupForReply$5$NotificationsController() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.pushMessages.size(); ++var2) {
         MessageObject var3 = (MessageObject)this.pushMessages.get(var2);
         long var4 = var3.getDialogId();
         TLRPC.Message var6 = var3.messageOwner;
         if ((!var6.mentioned || !(var6.action instanceof TLRPC.TL_messageActionPinMessage)) && (int)var4 != 0 && (var3.messageOwner.to_id.channel_id == 0 || var3.isMegagroup())) {
            var1.add(0, var3);
         }
      }

      if (!var1.isEmpty() && !AndroidUtilities.needShowPasscode(false)) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$SfVCz2vPoedKrTlwsJaPH9ngam4(this, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$hideNotifications$24$NotificationsController() {
      notificationManager.cancel(this.notificationId);
      this.lastWearNotifiedMessageId.clear();

      for(int var1 = 0; var1 < this.wearNotificationsIds.size(); ++var1) {
         notificationManager.cancel((Integer)this.wearNotificationsIds.valueAt(var1));
      }

      this.wearNotificationsIds.clear();
   }

   // $FF: synthetic method
   public void lambda$new$0$NotificationsController() {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("delay reached");
      }

      if (!this.delayedPushMessages.isEmpty()) {
         this.showOrUpdateNotification(true);
         this.delayedPushMessages.clear();
      }

      try {
         if (this.notificationDelayWakelock.isHeld()) {
            this.notificationDelayWakelock.release();
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   // $FF: synthetic method
   public void lambda$null$10$NotificationsController(int var1) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, var1);
   }

   // $FF: synthetic method
   public void lambda$null$12$NotificationsController(ArrayList var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.popupMessages.remove(var1.get(var3));
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated);
   }

   // $FF: synthetic method
   public void lambda$null$14$NotificationsController(ArrayList var1, int var2) {
      this.popupMessages.addAll(0, var1);
      if ((ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn && !SharedConfig.isWaitingForPasscodeEnter) && (var2 == 3 || var2 == 1 && ApplicationLoader.isScreenOn || var2 == 2 && !ApplicationLoader.isScreenOn)) {
         Intent var4 = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
         var4.setFlags(268763140);

         try {
            ApplicationLoader.applicationContext.startActivity(var4);
         } catch (Throwable var3) {
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$15$NotificationsController(int var1) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, var1);
   }

   // $FF: synthetic method
   public void lambda$null$17$NotificationsController(ArrayList var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.popupMessages.remove(var1.get(var3));
      }

   }

   // $FF: synthetic method
   public void lambda$null$18$NotificationsController(int var1) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, var1);
   }

   // $FF: synthetic method
   public void lambda$null$20$NotificationsController(int var1) {
      if (this.total_unread_count == 0) {
         this.popupMessages.clear();
         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated);
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, var1);
   }

   // $FF: synthetic method
   public void lambda$null$4$NotificationsController(ArrayList var1) {
      this.popupReplyMessages = var1;
      Intent var2 = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
      var2.putExtra("force", true);
      var2.putExtra("currentAccount", this.currentAccount);
      var2.setFlags(268763140);
      ApplicationLoader.applicationContext.startActivity(var2);
      var2 = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
      ApplicationLoader.applicationContext.sendBroadcast(var2);
   }

   // $FF: synthetic method
   public void lambda$null$6$NotificationsController(ArrayList var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.popupMessages.remove(var1.get(var3));
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$NotificationsController(int var1) {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsUnreadCounterChanged, var1);
   }

   // $FF: synthetic method
   public void lambda$null$9$NotificationsController(ArrayList var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.popupMessages.remove(var1.get(var3));
      }

   }

   // $FF: synthetic method
   public void lambda$playInChatSound$27$NotificationsController() {
      if (Math.abs(System.currentTimeMillis() - this.lastSoundPlay) > 500L) {
         Exception var10000;
         Exception var8;
         label57: {
            boolean var10001;
            try {
               if (this.soundPool == null) {
                  SoundPool var1 = new SoundPool(3, 1, 0);
                  this.soundPool = var1;
                  this.soundPool.setOnLoadCompleteListener(_$$Lambda$NotificationsController$NULIntVdHQSUoPd6L0mVTH6J8n0.INSTANCE);
               }
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
               break label57;
            }

            try {
               if (this.soundIn == 0 && !this.soundInLoaded) {
                  this.soundInLoaded = true;
                  this.soundIn = this.soundPool.load(ApplicationLoader.applicationContext, 2131492872, 1);
               }
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label57;
            }

            int var2;
            try {
               var2 = this.soundIn;
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label57;
            }

            if (var2 == 0) {
               return;
            }

            try {
               this.soundPool.play(this.soundIn, 1.0F, 1.0F, 1, 0, 1.0F);
               return;
            } catch (Exception var4) {
               var8 = var4;

               try {
                  FileLog.e((Throwable)var8);
                  return;
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
               }
            }
         }

         var8 = var10000;
         FileLog.e((Throwable)var8);
      }
   }

   // $FF: synthetic method
   public void lambda$playOutChatSound$35$NotificationsController() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   public void lambda$processDialogsUpdateRead$19$NotificationsController(LongSparseArray var1, ArrayList var2) {
      int var3 = this.total_unread_count;
      SharedPreferences var4 = MessagesController.getNotificationsSettings(this.currentAccount);
      int var5 = 0;

      while(true) {
         label108: {
            int var6 = var1.size();
            boolean var7 = true;
            if (var5 >= var6) {
               if (!var2.isEmpty()) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$ONJqyaSxnewsyizGxRK_V30P95A(this, var2));
               }

               if (var3 != this.total_unread_count) {
                  if (!this.notifyCheck) {
                     this.delayedPushMessages.clear();
                     this.showOrUpdateNotification(this.notifyCheck);
                  } else {
                     if (this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                        var7 = false;
                     }

                     this.scheduleNotificationDelay(var7);
                  }

                  AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$GAjtCMO1qmPedRnHLLIKT37DETU(this, this.pushDialogs.size()));
               }

               this.notifyCheck = false;
               if (this.showBadgeNumber) {
                  this.setBadge(this.getTotalAllUnreadCount());
               }

               return;
            }

            long var8 = var1.keyAt(var5);
            var6 = this.getNotifyOverride(var4, var8);
            if (var6 == -1) {
               var7 = this.isGlobalNotificationsEnabled(var8);
            } else if (var6 != 2) {
               var7 = true;
            } else {
               var7 = false;
            }

            Integer var10 = (Integer)this.pushDialogs.get(var8);
            Integer var11 = (Integer)var1.get(var8);
            Integer var12;
            if (this.notifyCheck && !var7) {
               var12 = (Integer)this.pushDialogsOverrideMention.get(var8);
               if (var12 != null && var12 != 0) {
                  var7 = true;
                  var11 = var12;
               }
            }

            if (var11 == 0) {
               this.smartNotificationsDialogs.remove(var8);
            }

            var12 = var11;
            if (var11 < 0) {
               if (var10 == null) {
                  break label108;
               }

               var12 = var10 + var11;
            }

            if ((var7 || var12 == 0) && var10 != null) {
               this.total_unread_count -= var10;
            }

            if (var12 == 0) {
               this.pushDialogs.remove(var8);
               this.pushDialogsOverrideMention.remove(var8);

               int var13;
               for(var6 = 0; var6 < this.pushMessages.size(); var6 = var13 + 1) {
                  MessageObject var18 = (MessageObject)this.pushMessages.get(var6);
                  var13 = var6;
                  if (var18.getDialogId() == var8) {
                     if (this.isPersonalMessage(var18)) {
                        --this.personal_count;
                     }

                     this.pushMessages.remove(var6);
                     var13 = var6 - 1;
                     this.delayedPushMessages.remove(var18);
                     long var14 = (long)var18.getId();
                     var6 = var18.messageOwner.to_id.channel_id;
                     long var16 = var14;
                     if (var6 != 0) {
                        var16 = var14 | (long)var6 << 32;
                     }

                     this.pushMessagesDict.remove(var16);
                     var2.add(var18);
                  }
               }
            } else if (var7) {
               this.total_unread_count += var12;
               this.pushDialogs.put(var8, var12);
            }
         }

         ++var5;
      }
   }

   // $FF: synthetic method
   public void lambda$processLoadedUnreadMessages$21$NotificationsController(ArrayList var1, LongSparseArray var2, ArrayList var3) {
      this.pushDialogs.clear();
      this.pushMessages.clear();
      this.pushMessagesDict.clear();
      this.total_unread_count = 0;
      this.personal_count = 0;
      SharedPreferences var4 = MessagesController.getNotificationsSettings(this.currentAccount);
      LongSparseArray var5 = new LongSparseArray();
      SharedPreferences var6 = var4;
      int var7;
      long var9;
      int var11;
      long var12;
      long var14;
      boolean var16;
      if (var1 != null) {
         var7 = 0;

         while(true) {
            var6 = var4;
            if (var7 >= var1.size()) {
               break;
            }

            label191: {
               TLRPC.Message var24 = (TLRPC.Message)var1.get(var7);
               if (var24 != null && var24.silent) {
                  TLRPC.MessageAction var8 = var24.action;
                  if (var8 instanceof TLRPC.TL_messageActionContactSignUp || var8 instanceof TLRPC.TL_messageActionUserJoined) {
                     break label191;
                  }
               }

               var9 = (long)var24.id;
               var11 = var24.to_id.channel_id;
               var12 = var9;
               if (var11 != 0) {
                  var12 = var9 | (long)var11 << 32;
               }

               if (this.pushMessagesDict.indexOfKey(var12) < 0) {
                  MessageObject var25 = new MessageObject(this.currentAccount, var24, false);
                  if (this.isPersonalMessage(var25)) {
                     ++this.personal_count;
                  }

                  var14 = var25.getDialogId();
                  TLRPC.Message var27 = var25.messageOwner;
                  if (var27.mentioned) {
                     var9 = (long)var27.from_id;
                  } else {
                     var9 = var14;
                  }

                  var11 = var5.indexOfKey(var9);
                  if (var11 >= 0) {
                     var16 = (Boolean)var5.valueAt(var11);
                  } else {
                     var11 = this.getNotifyOverride(var4, var9);
                     if (var11 == -1) {
                        var16 = this.isGlobalNotificationsEnabled(var9);
                     } else if (var11 != 2) {
                        var16 = true;
                     } else {
                        var16 = false;
                     }

                     var5.put(var9, var16);
                  }

                  if (var16 && (var9 != this.opened_dialog_id || !ApplicationLoader.isScreenOn)) {
                     this.pushMessagesDict.put(var12, var25);
                     this.pushMessages.add(0, var25);
                     if (var14 != var9) {
                        Integer var28 = (Integer)this.pushDialogsOverrideMention.get(var14);
                        LongSparseArray var26 = this.pushDialogsOverrideMention;
                        if (var28 == null) {
                           var11 = 1;
                        } else {
                           var11 = var28 + 1;
                        }

                        var26.put(var14, var11);
                     }
                  }
               }
            }

            ++var7;
         }
      }

      SharedPreferences var19 = var6;

      for(var7 = 0; var7 < var2.size(); ++var7) {
         var12 = var2.keyAt(var7);
         var11 = var5.indexOfKey(var12);
         if (var11 >= 0) {
            var16 = (Boolean)var5.valueAt(var11);
         } else {
            var11 = this.getNotifyOverride(var19, var12);
            if (var11 == -1) {
               var16 = this.isGlobalNotificationsEnabled(var12);
            } else if (var11 != 2) {
               var16 = true;
            } else {
               var16 = false;
            }

            var5.put(var12, var16);
         }

         if (var16) {
            var11 = (Integer)var2.valueAt(var7);
            this.pushDialogs.put(var12, var11);
            this.total_unread_count += var11;
         }
      }

      if (var3 != null) {
         for(var7 = 0; var7 < var3.size(); ++var7) {
            MessageObject var22 = (MessageObject)var3.get(var7);
            var12 = (long)var22.getId();
            var11 = var22.messageOwner.to_id.channel_id;
            if (var11 != 0) {
               var12 |= (long)var11 << 32;
            }

            if (this.pushMessagesDict.indexOfKey(var12) < 0) {
               if (this.isPersonalMessage(var22)) {
                  ++this.personal_count;
               }

               var9 = var22.getDialogId();
               TLRPC.Message var20 = var22.messageOwner;
               long var17 = var20.random_id;
               if (var20.mentioned) {
                  var14 = (long)var20.from_id;
               } else {
                  var14 = var9;
               }

               var11 = var5.indexOfKey(var14);
               if (var11 >= 0) {
                  var16 = (Boolean)var5.valueAt(var11);
               } else {
                  var11 = this.getNotifyOverride(var19, var14);
                  if (var11 == -1) {
                     var16 = this.isGlobalNotificationsEnabled(var14);
                  } else if (var11 != 2) {
                     var16 = true;
                  } else {
                     var16 = false;
                  }

                  var5.put(var14, var16);
               }

               if (var16 && (var14 != this.opened_dialog_id || !ApplicationLoader.isScreenOn)) {
                  if (var12 != 0L) {
                     this.pushMessagesDict.put(var12, var22);
                  } else if (var17 != 0L) {
                     this.fcmRandomMessagesDict.put(var17, var22);
                  }

                  this.pushMessages.add(0, var22);
                  Integer var23;
                  if (var9 != var14) {
                     var23 = (Integer)this.pushDialogsOverrideMention.get(var9);
                     var2 = this.pushDialogsOverrideMention;
                     if (var23 == null) {
                        var11 = 1;
                     } else {
                        var11 = var23 + 1;
                     }

                     var2.put(var9, var11);
                  }

                  var23 = (Integer)this.pushDialogs.get(var14);
                  if (var23 != null) {
                     var11 = var23 + 1;
                  } else {
                     var11 = 1;
                  }

                  Integer var21 = var11;
                  if (var23 != null) {
                     this.total_unread_count -= var23;
                  }

                  this.total_unread_count += var21;
                  this.pushDialogs.put(var14, var21);
               }
            }
         }
      }

      var16 = false;
      AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$CkSMdSXLZtMteSgS81186zoUJaI(this, this.pushDialogs.size()));
      if (SystemClock.elapsedRealtime() / 1000L < 60L) {
         var16 = true;
      }

      this.showOrUpdateNotification(var16);
      if (this.showBadgeNumber) {
         this.setBadge(this.getTotalAllUnreadCount());
      }

   }

   // $FF: synthetic method
   public void lambda$processNewMessages$16$NotificationsController(ArrayList var1, ArrayList var2, boolean var3, boolean var4, CountDownLatch var5) {
      LongSparseArray var6 = new LongSparseArray();
      SharedPreferences var7 = MessagesController.getNotificationsSettings(this.currentAccount);
      boolean var8 = var7.getBoolean("PinnedMessages", true);
      int var9 = 0;
      int var10 = 0;
      boolean var11 = false;

      boolean var12;
      int var16;
      long var17;
      Integer var30;
      for(var12 = false; var10 < var1.size(); ++var10) {
         MessageObject var13;
         boolean var15;
         long var19;
         long var21;
         int var23;
         boolean var24;
         long var25;
         label193: {
            label210: {
               var13 = (MessageObject)var1.get(var10);
               TLRPC.Message var14 = var13.messageOwner;
               if (var14 != null && var14.silent) {
                  TLRPC.MessageAction var31 = var14.action;
                  if (var31 instanceof TLRPC.TL_messageActionContactSignUp || var31 instanceof TLRPC.TL_messageActionUserJoined) {
                     var15 = var12;
                     var16 = var9;
                     break label210;
                  }
               }

               var17 = (long)var13.getId();
               if (var13.isFcmMessage()) {
                  var19 = var13.messageOwner.random_id;
               } else {
                  var19 = 0L;
               }

               var21 = var13.getDialogId();
               var23 = (int)var21;
               var16 = var13.messageOwner.to_id.channel_id;
               if (var16 != 0) {
                  var17 |= (long)var16 << 32;
                  var24 = true;
               } else {
                  var24 = false;
               }

               MessageObject var32 = (MessageObject)this.pushMessagesDict.get(var17);
               if (var32 == null) {
                  var25 = var13.messageOwner.random_id;
                  if (var25 != 0L) {
                     var32 = (MessageObject)this.fcmRandomMessagesDict.get(var25);
                     if (var32 != null) {
                        this.fcmRandomMessagesDict.remove(var13.messageOwner.random_id);
                     }
                  }
               }

               if (var32 != null) {
                  var15 = var12;
                  var16 = var9;
                  if (var32.isFcmMessage()) {
                     this.pushMessagesDict.put(var17, var13);
                     var16 = this.pushMessages.indexOf(var32);
                     if (var16 >= 0) {
                        this.pushMessages.set(var16, var13);
                        var9 = this.addToPopupMessages(var2, var13, var23, var21, var24, var7);
                     }

                     if (var3) {
                        var15 = var13.localEdit;
                        var12 = var15;
                        if (var15) {
                           MessagesStorage.getInstance(this.currentAccount).putPushMessage(var13);
                           var12 = var15;
                        }
                     }

                     var15 = var12;
                     var16 = var9;
                  }
               } else if (var12) {
                  var15 = var12;
                  var16 = var9;
               } else {
                  if (var3) {
                     MessagesStorage.getInstance(this.currentAccount).putPushMessage(var13);
                  }

                  if (var21 == this.opened_dialog_id && ApplicationLoader.isScreenOn) {
                     var15 = var12;
                     var16 = var9;
                     if (!var3) {
                        this.playInChatSound();
                        var15 = var12;
                        var16 = var9;
                     }
                  } else {
                     var14 = var13.messageOwner;
                     if (!var14.mentioned) {
                        var25 = var21;
                        break label193;
                     }

                     if (var8 || !(var14.action instanceof TLRPC.TL_messageActionPinMessage)) {
                        var25 = (long)var13.messageOwner.from_id;
                        break label193;
                     }

                     var16 = var9;
                     var15 = var12;
                  }
               }
            }

            var9 = var16;
            var12 = var15;
            continue;
         }

         if (this.isPersonalMessage(var13)) {
            ++this.personal_count;
         }

         var16 = var6.indexOfKey(var25);
         if (var16 >= 0) {
            var15 = (Boolean)var6.valueAt(var16);
         } else {
            var16 = this.getNotifyOverride(var7, var25);
            if (var16 == -1) {
               var15 = this.isGlobalNotificationsEnabled(var25);
            } else if (var16 != 2) {
               var15 = true;
            } else {
               var15 = false;
            }

            var6.put(var25, var15);
         }

         if (var15) {
            if (!var3) {
               var9 = this.addToPopupMessages(var2, var13, var23, var25, var24, var7);
            }

            this.delayedPushMessages.add(var13);
            this.pushMessages.add(0, var13);
            if (var17 != 0L) {
               this.pushMessagesDict.put(var17, var13);
            } else if (var19 != 0L) {
               this.fcmRandomMessagesDict.put(var19, var13);
            }

            var16 = var9;
            if (var21 != var25) {
               var30 = (Integer)this.pushDialogsOverrideMention.get(var21);
               LongSparseArray var33 = this.pushDialogsOverrideMention;
               if (var30 == null) {
                  var16 = 1;
               } else {
                  var16 = var30 + 1;
               }

               var33.put(var21, var16);
               var16 = var9;
            }
         } else {
            var16 = var9;
         }

         var9 = var16;
         var11 = true;
      }

      if (var11) {
         this.notifyCheck = var4;
      }

      if (!var2.isEmpty() && !AndroidUtilities.needShowPasscode(false)) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$vBhFCZdXUS15Ipx_fzqzTMIuA3o(this, var2, var9));
      }

      if (var3) {
         if (var12) {
            this.delayedPushMessages.clear();
            this.showOrUpdateNotification(this.notifyCheck);
         } else if (var11) {
            var17 = ((MessageObject)var1.get(0)).getDialogId();
            var16 = this.total_unread_count;
            var9 = this.getNotifyOverride(var7, var17);
            if (var9 == -1) {
               var3 = this.isGlobalNotificationsEnabled(var17);
            } else if (var9 != 2) {
               var3 = true;
            } else {
               var3 = false;
            }

            var4 = var3;
            var30 = (Integer)this.pushDialogs.get(var17);
            if (var30 != null) {
               var9 = var30 + 1;
            } else {
               var9 = 1;
            }

            Integer var29 = var9;
            Integer var27 = var29;
            var3 = var3;
            if (this.notifyCheck) {
               var27 = var29;
               var3 = var4;
               if (!var4) {
                  Integer var28 = (Integer)this.pushDialogsOverrideMention.get(var17);
                  var27 = var29;
                  var3 = var4;
                  if (var28 != null) {
                     var27 = var29;
                     var3 = var4;
                     if (var28 != 0) {
                        var27 = var28;
                        var3 = true;
                     }
                  }
               }
            }

            if (var3) {
               if (var30 != null) {
                  this.total_unread_count -= var30;
               }

               this.total_unread_count += var27;
               this.pushDialogs.put(var17, var27);
            }

            if (var16 != this.total_unread_count) {
               this.delayedPushMessages.clear();
               this.showOrUpdateNotification(this.notifyCheck);
               AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$R3R5Z37efc0XPsswynnBTmucwac(this, this.pushDialogs.size()));
            }

            this.notifyCheck = false;
            if (this.showBadgeNumber) {
               this.setBadge(this.getTotalAllUnreadCount());
            }
         }
      }

      if (var5 != null) {
         var5.countDown();
      }

   }

   // $FF: synthetic method
   public void lambda$processReadMessages$13$NotificationsController(SparseLongArray var1, ArrayList var2, long var3, int var5, int var6, boolean var7) {
      int var8;
      int var12;
      long var17;
      if (var1 != null) {
         for(var8 = 0; var8 < var1.size(); ++var8) {
            int var9 = var1.keyAt(var8);
            long var10 = var1.get(var9);

            int var14;
            for(var12 = 0; var12 < this.pushMessages.size(); var12 = var14 + 1) {
               MessageObject var13 = (MessageObject)this.pushMessages.get(var12);
               var14 = var12;
               if (var13.getDialogId() == (long)var9) {
                  var14 = var12;
                  if (var13.getId() <= (int)var10) {
                     if (this.isPersonalMessage(var13)) {
                        --this.personal_count;
                     }

                     var2.add(var13);
                     long var15 = (long)var13.getId();
                     var14 = var13.messageOwner.to_id.channel_id;
                     var17 = var15;
                     if (var14 != 0) {
                        var17 = var15 | (long)var14 << 32;
                     }

                     this.pushMessagesDict.remove(var17);
                     this.delayedPushMessages.remove(var13);
                     this.pushMessages.remove(var12);
                     var14 = var12 - 1;
                  }
               }
            }
         }
      }

      if (var3 != 0L && (var5 != 0 || var6 != 0)) {
         for(var12 = 0; var12 < this.pushMessages.size(); ++var12) {
            MessageObject var19 = (MessageObject)this.pushMessages.get(var12);
            if (var19.getDialogId() == var3) {
               boolean var20;
               label76: {
                  label75: {
                     if (var6 != 0) {
                        if (var19.messageOwner.date <= var6) {
                           break label75;
                        }
                     } else if (!var7) {
                        if (var19.getId() <= var5 || var5 < 0) {
                           break label75;
                        }
                     } else if (var19.getId() == var5 || var5 < 0) {
                        break label75;
                     }

                     var20 = false;
                     break label76;
                  }

                  var20 = true;
               }

               if (var20) {
                  if (this.isPersonalMessage(var19)) {
                     --this.personal_count;
                  }

                  this.pushMessages.remove(var12);
                  this.delayedPushMessages.remove(var19);
                  var2.add(var19);
                  var17 = (long)var19.getId();
                  var8 = var19.messageOwner.to_id.channel_id;
                  if (var8 != 0) {
                     var17 |= (long)var8 << 32;
                  }

                  this.pushMessagesDict.remove(var17);
                  --var12;
               }
            }
         }
      }

      if (!var2.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$uwXUA8kYkjmDHBUM6M6MDaJprzI(this, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$removeDeletedHisoryFromNotifications$11$NotificationsController(SparseIntArray var1, ArrayList var2) {
      int var3 = this.total_unread_count;
      MessagesController.getNotificationsSettings(this.currentAccount);
      Integer var4 = 0;
      int var5 = 0;

      while(true) {
         int var6 = var1.size();
         boolean var7 = true;
         if (var5 >= var6) {
            if (var2.isEmpty()) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$sZTwdrj4Q3g5O_k6lbH6PmmVEkI(this, var2));
            }

            if (var3 != this.total_unread_count) {
               if (!this.notifyCheck) {
                  this.delayedPushMessages.clear();
                  this.showOrUpdateNotification(this.notifyCheck);
               } else {
                  if (this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                     var7 = false;
                  }

                  this.scheduleNotificationDelay(var7);
               }

               AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$hEqV8j2COvHkVH0SA_DnqOAATPc(this, this.pushDialogs.size()));
            }

            this.notifyCheck = false;
            if (this.showBadgeNumber) {
               this.setBadge(this.getTotalAllUnreadCount());
            }

            return;
         }

         var6 = var1.keyAt(var5);
         long var8 = (long)(-var6);
         int var10 = var1.get(var6);
         Integer var11 = (Integer)this.pushDialogs.get(var8);
         Integer var12 = var11;
         if (var11 == null) {
            var12 = var4;
         }

         var11 = var12;

         for(var6 = 0; var6 < this.pushMessages.size(); ++var6) {
            MessageObject var13 = (MessageObject)this.pushMessages.get(var6);
            if (var13.getDialogId() == var8 && var13.getId() <= var10) {
               this.pushMessagesDict.remove(var13.getIdWithChannel());
               this.delayedPushMessages.remove(var13);
               this.pushMessages.remove(var13);
               --var6;
               if (this.isPersonalMessage(var13)) {
                  --this.personal_count;
               }

               var2.add(var13);
               var11 = var11 - 1;
            }
         }

         Integer var14 = var11;
         if (var11 <= 0) {
            this.smartNotificationsDialogs.remove(var8);
            var14 = var4;
         }

         if (!var14.equals(var12)) {
            this.total_unread_count -= var12;
            this.total_unread_count += var14;
            this.pushDialogs.put(var8, var14);
         }

         if (var14 == 0) {
            this.pushDialogs.remove(var8);
            this.pushDialogsOverrideMention.remove(var8);
         }

         ++var5;
      }
   }

   // $FF: synthetic method
   public void lambda$removeDeletedMessagesFromNotifications$8$NotificationsController(SparseArray var1, ArrayList var2) {
      int var3 = this.total_unread_count;
      MessagesController.getNotificationsSettings(this.currentAccount);
      Integer var4 = 0;

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         int var6 = var1.keyAt(var5);
         long var7 = (long)(-var6);
         ArrayList var9 = (ArrayList)var1.get(var6);
         Integer var10 = (Integer)this.pushDialogs.get(var7);
         Integer var11 = var10;
         if (var10 == null) {
            var11 = var4;
         }

         var10 = var11;

         for(int var12 = 0; var12 < var9.size(); ++var12) {
            long var13 = (long)(Integer)var9.get(var12) | (long)var6 << 32;
            MessageObject var15 = (MessageObject)this.pushMessagesDict.get(var13);
            if (var15 != null) {
               this.pushMessagesDict.remove(var13);
               this.delayedPushMessages.remove(var15);
               this.pushMessages.remove(var15);
               if (this.isPersonalMessage(var15)) {
                  --this.personal_count;
               }

               var2.add(var15);
               var10 = var10 - 1;
            }
         }

         if (var10 <= 0) {
            this.smartNotificationsDialogs.remove(var7);
            var10 = var4;
         }

         if (!var10.equals(var11)) {
            this.total_unread_count -= var11;
            this.total_unread_count += var10;
            this.pushDialogs.put(var7, var10);
         }

         if (var10 == 0) {
            this.pushDialogs.remove(var7);
            this.pushDialogsOverrideMention.remove(var7);
         }
      }

      boolean var16 = true;
      if (!var2.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$uUrKIQpuu_OHFjMyR7HGe660wQk(this, var2));
      }

      if (var3 != this.total_unread_count) {
         if (!this.notifyCheck) {
            this.delayedPushMessages.clear();
            this.showOrUpdateNotification(this.notifyCheck);
         } else {
            if (this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
               var16 = false;
            }

            this.scheduleNotificationDelay(var16);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$VcdDGTs8T17vFBc_zmAJ5lCdPBU(this, this.pushDialogs.size()));
      }

      this.notifyCheck = false;
      if (this.showBadgeNumber) {
         this.setBadge(this.getTotalAllUnreadCount());
      }

   }

   // $FF: synthetic method
   public void lambda$repeatNotificationMaybe$28$NotificationsController() {
      int var1 = Calendar.getInstance().get(11);
      if (var1 >= 11 && var1 <= 22) {
         notificationManager.cancel(this.notificationId);
         this.showOrUpdateNotification(true);
      } else {
         this.scheduleNotificationRepeat();
      }

   }

   // $FF: synthetic method
   public void lambda$setLastOnlineFromOtherDevice$3$NotificationsController(int var1) {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var2 = new StringBuilder();
         var2.append("set last online from other device = ");
         var2.append(var1);
         FileLog.d(var2.toString());
      }

      this.lastOnlineFromOtherDevice = var1;
   }

   // $FF: synthetic method
   public void lambda$setOpenedDialogId$2$NotificationsController(long var1) {
      this.opened_dialog_id = var1;
   }

   // $FF: synthetic method
   public void lambda$showNotifications$23$NotificationsController() {
      this.showOrUpdateNotification(false);
   }

   // $FF: synthetic method
   public void lambda$updateBadge$22$NotificationsController() {
      this.setBadge(this.getTotalAllUnreadCount());
   }

   public void playOutChatSound() {
      if (this.inChatSoundEnabled && !MediaController.getInstance().isRecordingAudio()) {
         label18: {
            int var1;
            try {
               var1 = audioManager.getRingerMode();
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
               break label18;
            }

            if (var1 == 0) {
               return;
            }
         }

         notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$9BWFjQml5zrAo3EV8FWEAyCpJLQ(this));
      }

   }

   public void processDialogsUpdateRead(LongSparseArray var1) {
      ArrayList var2 = new ArrayList();
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$bRv8AkmkiAwGyZ1dPg2TuCyHYS0(this, var1, var2));
   }

   public void processLoadedUnreadMessages(LongSparseArray var1, ArrayList var2, ArrayList var3, ArrayList var4, ArrayList var5, ArrayList var6) {
      MessagesController.getInstance(this.currentAccount).putUsers(var4, true);
      MessagesController.getInstance(this.currentAccount).putChats(var5, true);
      MessagesController.getInstance(this.currentAccount).putEncryptedChats(var6, true);
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$XEAogHRWLk5KuijEFvgR3DVl_Oc(this, var2, var1, var3));
   }

   public void processNewMessages(ArrayList var1, boolean var2, boolean var3, CountDownLatch var4) {
      if (var1.isEmpty()) {
         if (var4 != null) {
            var4.countDown();
         }

      } else {
         ArrayList var5 = new ArrayList(0);
         notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$blpPMIxTaKEgWkp2zDr1_y8eGUY(this, var1, var5, var3, var2, var4));
      }
   }

   public void processReadMessages(SparseLongArray var1, long var2, int var4, int var5, boolean var6) {
      ArrayList var7 = new ArrayList(0);
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$bn_qy54k0GHNymLhNYsBBa6g2mw(this, var1, var7, var2, var5, var4, var6));
   }

   public void removeDeletedHisoryFromNotifications(SparseIntArray var1) {
      ArrayList var2 = new ArrayList(0);
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$4ZPSiSXCXkKfxVPcPpmsFy8foEU(this, var1, var2));
   }

   public void removeDeletedMessagesFromNotifications(SparseArray var1) {
      ArrayList var2 = new ArrayList(0);
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$8lQbr5XMNBt__wC6arYqfGdfeMk(this, var1, var2));
   }

   public void removeNotificationsForDialog(long var1) {
      getInstance(this.currentAccount).processReadMessages((SparseLongArray)null, var1, 0, Integer.MAX_VALUE, false);
      LongSparseArray var3 = new LongSparseArray();
      var3.put(var1, 0);
      getInstance(this.currentAccount).processDialogsUpdateRead(var3);
   }

   protected void repeatNotificationMaybe() {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$kDrFFl__TRrIJW3mtxiKJeeK1vw(this));
   }

   public void setDialogNotificationsSettings(long var1, int var3) {
      Editor var4 = MessagesController.getNotificationsSettings(this.currentAccount).edit();
      TLRPC.Dialog var5 = (TLRPC.Dialog)MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(var1);
      StringBuilder var6;
      if (var3 == 4) {
         if (getInstance(this.currentAccount).isGlobalNotificationsEnabled(var1)) {
            var6 = new StringBuilder();
            var6.append("notify2_");
            var6.append(var1);
            var4.remove(var6.toString());
         } else {
            var6 = new StringBuilder();
            var6.append("notify2_");
            var6.append(var1);
            var4.putInt(var6.toString(), 0);
         }

         MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var1, 0L);
         if (var5 != null) {
            var5.notify_settings = new TLRPC.TL_peerNotifySettings();
         }
      } else {
         int var7 = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
         if (var3 == 0) {
            var7 += 3600;
         } else if (var3 == 1) {
            var7 += 28800;
         } else if (var3 == 2) {
            var7 += 172800;
         } else if (var3 == 3) {
            var7 = Integer.MAX_VALUE;
         }

         long var8 = 1L;
         if (var3 == 3) {
            var6 = new StringBuilder();
            var6.append("notify2_");
            var6.append(var1);
            var4.putInt(var6.toString(), 2);
         } else {
            var6 = new StringBuilder();
            var6.append("notify2_");
            var6.append(var1);
            var4.putInt(var6.toString(), 3);
            var6 = new StringBuilder();
            var6.append("notifyuntil_");
            var6.append(var1);
            var4.putInt(var6.toString(), var7);
            var8 = 1L | (long)var7 << 32;
         }

         getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(var1);
         MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(var1, var8);
         if (var5 != null) {
            var5.notify_settings = new TLRPC.TL_peerNotifySettings();
            var5.notify_settings.mute_until = var7;
         }
      }

      var4.commit();
      this.updateServerNotificationsSettings(var1);
   }

   public void setGlobalNotificationsEnabled(int var1, int var2) {
      MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt(this.getGlobalNotificationsKey(var1), var2).commit();
      getInstance(this.currentAccount).updateServerNotificationsSettings(var1);
   }

   public void setInChatSoundEnabled(boolean var1) {
      this.inChatSoundEnabled = var1;
   }

   public void setLastOnlineFromOtherDevice(int var1) {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$aMKmdt9uT4z6_2MONOs1umiLD6k(this, var1));
   }

   public void setOpenedDialogId(long var1) {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$XWu9HxcgJh0WGxxES9w4G4Lj_cA(this, var1));
   }

   public void showNotifications() {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$tVtEcXBSUtzhqixsWunEmHPHAAI(this));
   }

   public void updateBadge() {
      notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$z9M3KFS8OpgW1aPw2rnfQYb2xt0(this));
   }

   public void updateServerNotificationsSettings(int var1) {
      SharedPreferences var2 = MessagesController.getNotificationsSettings(this.currentAccount);
      TLRPC.TL_account_updateNotifySettings var3 = new TLRPC.TL_account_updateNotifySettings();
      var3.settings = new TLRPC.TL_inputPeerNotifySettings();
      var3.settings.flags = 5;
      if (var1 == 0) {
         var3.peer = new TLRPC.TL_inputNotifyChats();
         var3.settings.mute_until = var2.getInt("EnableGroup2", 0);
         var3.settings.show_previews = var2.getBoolean("EnablePreviewGroup", true);
      } else if (var1 == 1) {
         var3.peer = new TLRPC.TL_inputNotifyUsers();
         var3.settings.mute_until = var2.getInt("EnableAll2", 0);
         var3.settings.show_previews = var2.getBoolean("EnablePreviewAll", true);
      } else {
         var3.peer = new TLRPC.TL_inputNotifyBroadcasts();
         var3.settings.mute_until = var2.getInt("EnableChannel2", 0);
         var3.settings.show_previews = var2.getBoolean("EnablePreviewChannel", true);
      }

      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, _$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk.INSTANCE);
   }

   public void updateServerNotificationsSettings(long var1) {
      NotificationCenter var3 = NotificationCenter.getInstance(this.currentAccount);
      int var4 = NotificationCenter.notificationsSettingsUpdated;
      int var5 = 0;
      var3.postNotificationName(var4);
      var4 = (int)var1;
      if (var4 != 0) {
         SharedPreferences var10 = MessagesController.getNotificationsSettings(this.currentAccount);
         TLRPC.TL_account_updateNotifySettings var6 = new TLRPC.TL_account_updateNotifySettings();
         var6.settings = new TLRPC.TL_inputPeerNotifySettings();
         TLRPC.TL_inputPeerNotifySettings var7 = var6.settings;
         var7.flags |= 1;
         StringBuilder var8 = new StringBuilder();
         var8.append("preview_");
         var8.append(var1);
         var7.show_previews = var10.getBoolean(var8.toString(), true);
         var7 = var6.settings;
         var7.flags |= 2;
         var8 = new StringBuilder();
         var8.append("silent_");
         var8.append(var1);
         var7.silent = var10.getBoolean(var8.toString(), false);
         StringBuilder var11 = new StringBuilder();
         var11.append("notify2_");
         var11.append(var1);
         int var9 = var10.getInt(var11.toString(), -1);
         if (var9 != -1) {
            TLRPC.TL_inputPeerNotifySettings var12 = var6.settings;
            var12.flags |= 4;
            if (var9 == 3) {
               var11 = new StringBuilder();
               var11.append("notifyuntil_");
               var11.append(var1);
               var12.mute_until = var10.getInt(var11.toString(), 0);
            } else {
               if (var9 == 2) {
                  var5 = Integer.MAX_VALUE;
               }

               var12.mute_until = var5;
            }
         }

         var6.peer = new TLRPC.TL_inputNotifyPeer();
         ((TLRPC.TL_inputNotifyPeer)var6.peer).peer = MessagesController.getInstance(this.currentAccount).getInputPeer(var4);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, _$$Lambda$NotificationsController$KyQqllEdy_fdmMCr6frsin2S3Cs.INSTANCE);
      }
   }
}
