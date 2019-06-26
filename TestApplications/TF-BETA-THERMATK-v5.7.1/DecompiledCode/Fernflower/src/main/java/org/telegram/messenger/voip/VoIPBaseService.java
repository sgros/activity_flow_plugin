package org.telegram.messenger.voip;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.os.PowerManager.WakeLock;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.PhoneAccount.Builder;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.VoIPPermissionActivity;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.voip.VoIPHelper;

public abstract class VoIPBaseService extends Service implements SensorEventListener, OnAudioFocusChangeListener, VoIPController.ConnectionStateListener, NotificationCenter.NotificationCenterDelegate {
   public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
   public static final int AUDIO_ROUTE_BLUETOOTH = 2;
   public static final int AUDIO_ROUTE_EARPIECE = 0;
   public static final int AUDIO_ROUTE_SPEAKER = 1;
   public static final int DISCARD_REASON_DISCONNECT = 2;
   public static final int DISCARD_REASON_HANGUP = 1;
   public static final int DISCARD_REASON_LINE_BUSY = 4;
   public static final int DISCARD_REASON_MISSED = 3;
   protected static final int ID_INCOMING_CALL_NOTIFICATION = 202;
   protected static final int ID_ONGOING_CALL_NOTIFICATION = 201;
   protected static final int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
   public static final int STATE_ENDED = 11;
   public static final int STATE_ESTABLISHED = 3;
   public static final int STATE_FAILED = 4;
   public static final int STATE_RECONNECTING = 5;
   public static final int STATE_WAIT_INIT = 1;
   public static final int STATE_WAIT_INIT_ACK = 2;
   protected static final boolean USE_CONNECTION_SERVICE = isDeviceCompatibleWithConnectionServiceAPI();
   protected static VoIPBaseService sharedInstance;
   protected Runnable afterSoundRunnable = new Runnable() {
      public void run() {
         VoIPBaseService.this.soundPool.release();
         if (!VoIPBaseService.USE_CONNECTION_SERVICE) {
            if (VoIPBaseService.this.isBtHeadsetConnected) {
               ((AudioManager)ApplicationLoader.applicationContext.getSystemService("audio")).stopBluetoothSco();
            }

            ((AudioManager)ApplicationLoader.applicationContext.getSystemService("audio")).setSpeakerphoneOn(false);
         }
      }
   };
   protected boolean audioConfigured;
   protected int audioRouteToSet = 2;
   protected boolean bluetoothScoActive = false;
   protected BluetoothAdapter btAdapter;
   protected int callDiscardReason;
   protected Runnable connectingSoundRunnable;
   protected VoIPController controller;
   protected boolean controllerStarted;
   protected WakeLock cpuWakelock;
   protected int currentAccount = -1;
   protected int currentState = 0;
   protected boolean didDeleteConnectionServiceContact = false;
   protected boolean haveAudioFocus;
   protected boolean isBtHeadsetConnected;
   protected boolean isHeadsetPlugged;
   protected boolean isOutgoing;
   protected boolean isProximityNear;
   protected int lastError;
   protected long lastKnownDuration = 0L;
   protected NetworkInfo lastNetInfo;
   private Boolean mHasEarpiece = null;
   protected boolean micMute;
   protected boolean needPlayEndSound;
   protected boolean needSwitchToBluetoothAfterScoActivates = false;
   protected Notification ongoingCallNotification;
   protected boolean playingSound;
   protected VoIPController.Stats prevStats = new VoIPController.Stats();
   protected WakeLock proximityWakelock;
   protected BroadcastReceiver receiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         boolean var3 = "android.intent.action.HEADSET_PLUG".equals(var2.getAction());
         boolean var4 = true;
         boolean var5 = true;
         VoIPBaseService var9;
         if (var3) {
            var9 = VoIPBaseService.this;
            if (var2.getIntExtra("state", 0) != 1) {
               var5 = false;
            }

            var9.isHeadsetPlugged = var5;
            var9 = VoIPBaseService.this;
            if (var9.isHeadsetPlugged) {
               WakeLock var12 = var9.proximityWakelock;
               if (var12 != null && var12.isHeld()) {
                  VoIPBaseService.this.proximityWakelock.release();
               }
            }

            var9 = VoIPBaseService.this;
            var9.isProximityNear = false;
            var9.updateOutputGainControlState();
         } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(var2.getAction())) {
            VoIPBaseService.this.updateNetworkType();
         } else {
            StringBuilder var8;
            if ("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED".equals(var2.getAction())) {
               if (BuildVars.LOGS_ENABLED) {
                  var8 = new StringBuilder();
                  var8.append("bt headset state = ");
                  var8.append(var2.getIntExtra("android.bluetooth.profile.extra.STATE", 0));
                  FileLog.e(var8.toString());
               }

               var9 = VoIPBaseService.this;
               if (var2.getIntExtra("android.bluetooth.profile.extra.STATE", 0) == 2) {
                  var5 = var4;
               } else {
                  var5 = false;
               }

               var9.updateBluetoothHeadsetState(var5);
            } else if ("android.media.ACTION_SCO_AUDIO_STATE_UPDATED".equals(var2.getAction())) {
               int var6 = var2.getIntExtra("android.media.extra.SCO_AUDIO_STATE", 0);
               if (BuildVars.LOGS_ENABLED) {
                  var8 = new StringBuilder();
                  var8.append("Bluetooth SCO state updated: ");
                  var8.append(var6);
                  FileLog.e(var8.toString());
               }

               if (var6 == 0) {
                  var9 = VoIPBaseService.this;
                  if (var9.isBtHeadsetConnected && (!var9.btAdapter.isEnabled() || VoIPBaseService.this.btAdapter.getProfileConnectionState(1) != 2)) {
                     VoIPBaseService.this.updateBluetoothHeadsetState(false);
                     return;
                  }
               }

               var9 = VoIPBaseService.this;
               if (var6 == 1) {
                  var5 = true;
               } else {
                  var5 = false;
               }

               var9.bluetoothScoActive = var5;
               var9 = VoIPBaseService.this;
               if (var9.bluetoothScoActive && var9.needSwitchToBluetoothAfterScoActivates) {
                  var9.needSwitchToBluetoothAfterScoActivates = false;
                  AudioManager var10 = (AudioManager)var9.getSystemService("audio");
                  var10.setSpeakerphoneOn(false);
                  var10.setBluetoothScoOn(true);
               }

               Iterator var11 = VoIPBaseService.this.stateListeners.iterator();

               while(var11.hasNext()) {
                  ((VoIPBaseService.StateListener)var11.next()).onAudioSettingsChanged();
               }
            } else if ("android.intent.action.PHONE_STATE".equals(var2.getAction())) {
               String var7 = var2.getStringExtra("state");
               if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(var7)) {
                  VoIPBaseService.this.hangUp();
               }
            }
         }

      }
   };
   protected MediaPlayer ringtonePlayer;
   protected int signalBarCount;
   protected SoundPool soundPool;
   protected int spBusyId;
   protected int spConnectingId;
   protected int spEndId;
   protected int spFailedID;
   protected int spPlayID;
   protected int spRingbackID;
   protected boolean speakerphoneStateToSet;
   protected ArrayList stateListeners = new ArrayList();
   protected VoIPController.Stats stats = new VoIPController.Stats();
   protected VoIPBaseService.CallConnection systemCallConnection;
   protected Runnable timeoutRunnable;
   protected Vibrator vibrator;
   private boolean wasEstablished;

   private void acceptIncomingCallFromNotification() {
      this.showNotification();
      Intent var1;
      if (VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
         try {
            var1 = new Intent(this, VoIPPermissionActivity.class);
            PendingIntent.getActivity(this, 0, var1.addFlags(268435456), 0).send();
         } catch (Exception var2) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("Error starting permission activity", var2);
            }
         }

      } else {
         this.acceptIncomingCall();

         try {
            var1 = new Intent(this, this.getUIActivityClass());
            PendingIntent.getActivity(this, 0, var1.addFlags(805306368), 0).send();
         } catch (Exception var3) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("Error starting incall activity", var3);
            }
         }

      }
   }

   public static VoIPBaseService getSharedInstance() {
      return sharedInstance;
   }

   public static boolean isAnyKindOfCallActive() {
      VoIPService var0 = VoIPService.getSharedInstance();
      boolean var1 = false;
      boolean var2 = var1;
      if (var0 != null) {
         var2 = var1;
         if (VoIPService.getSharedInstance().getCallState() != 15) {
            var2 = true;
         }
      }

      return var2;
   }

   private static boolean isDeviceCompatibleWithConnectionServiceAPI() {
      int var0 = VERSION.SDK_INT;
      boolean var1 = false;
      if (var0 < 26) {
         return false;
      } else {
         if ("angler".equals(Build.PRODUCT) || "bullhead".equals(Build.PRODUCT) || "sailfish".equals(Build.PRODUCT) || "marlin".equals(Build.PRODUCT) || "walleye".equals(Build.PRODUCT) || "taimen".equals(Build.PRODUCT) || "blueline".equals(Build.PRODUCT) || "crosshatch".equals(Build.PRODUCT) || MessagesController.getGlobalMainSettings().getBoolean("dbg_force_connection_service", false)) {
            var1 = true;
         }

         return var1;
      }
   }

   public abstract void acceptIncomingCall();

   @TargetApi(26)
   protected PhoneAccountHandle addAccountToTelecomManager() {
      TelecomManager var1 = (TelecomManager)this.getSystemService("telecom");
      TLRPC.User var2 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
      ComponentName var3 = new ComponentName(this, TelegramConnectionService.class);
      StringBuilder var4 = new StringBuilder();
      var4.append("");
      var4.append(var2.id);
      PhoneAccountHandle var5 = new PhoneAccountHandle(var3, var4.toString());
      var1.registerPhoneAccount((new Builder(var5, ContactsController.formatName(var2.first_name, var2.last_name))).setCapabilities(2048).setIcon(Icon.createWithResource(this, 2131165446)).setHighlightColor(-13851168).addSupportedUriScheme("sip").build());
      return var5;
   }

   protected void callEnded() {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Call ");
         var1.append(this.getCallID());
         var1.append(" ended");
         FileLog.d(var1.toString());
      }

      this.dispatchStateChanged(11);
      boolean var2 = this.needPlayEndSound;
      long var3 = 700L;
      if (var2) {
         this.playingSound = true;
         this.soundPool.play(this.spEndId, 1.0F, 1.0F, 0, 0, 1.0F);
         AndroidUtilities.runOnUIThread(this.afterSoundRunnable, 700L);
      }

      Runnable var5 = this.timeoutRunnable;
      if (var5 != null) {
         AndroidUtilities.cancelRunOnUIThread(var5);
         this.timeoutRunnable = null;
      }

      if (!this.needPlayEndSound) {
         var3 = 0L;
      }

      this.endConnectionServiceCall(var3);
      this.stopSelf();
   }

   protected void callFailed() {
      VoIPController var1 = this.controller;
      int var2;
      if (var1 != null && this.controllerStarted) {
         var2 = var1.getLastError();
      } else {
         var2 = 0;
      }

      this.callFailed(var2);
   }

   protected void callFailed(int var1) {
      try {
         StringBuilder var6 = new StringBuilder();
         var6.append("Call ");
         var6.append(this.getCallID());
         var6.append(" failed with error code ");
         var6.append(var1);
         Exception var2 = new Exception(var6.toString());
         throw var2;
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
         this.lastError = var1;
         this.dispatchStateChanged(4);
         if (var1 != -3) {
            SoundPool var3 = this.soundPool;
            if (var3 != null) {
               this.playingSound = true;
               var3.play(this.spFailedID, 1.0F, 1.0F, 0, 0, 1.0F);
               AndroidUtilities.runOnUIThread(this.afterSoundRunnable, 1000L);
            }
         }

         if (USE_CONNECTION_SERVICE) {
            VoIPBaseService.CallConnection var5 = this.systemCallConnection;
            if (var5 != null) {
               var5.setDisconnected(new DisconnectCause(1));
               this.systemCallConnection.destroy();
               this.systemCallConnection = null;
            }
         }

         this.stopSelf();
      }
   }

   void callFailedFromConnectionService() {
      if (this.isOutgoing) {
         this.callFailed(-5);
      } else {
         this.hangUp();
      }

   }

   protected void configureDeviceForCall() {
      this.needPlayEndSound = true;
      AudioManager var1 = (AudioManager)this.getSystemService("audio");
      if (!USE_CONNECTION_SERVICE) {
         var1.setMode(3);
         var1.requestAudioFocus(this, 0, 1);
         if (this.isBluetoothHeadsetConnected() && this.hasEarpiece()) {
            int var2 = this.audioRouteToSet;
            if (var2 != 0) {
               if (var2 != 1) {
                  if (var2 == 2) {
                     if (!this.bluetoothScoActive) {
                        this.needSwitchToBluetoothAfterScoActivates = true;

                        try {
                           var1.startBluetoothSco();
                        } catch (Throwable var4) {
                        }
                     } else {
                        var1.setBluetoothScoOn(true);
                        var1.setSpeakerphoneOn(false);
                     }
                  }
               } else {
                  var1.setBluetoothScoOn(false);
                  var1.setSpeakerphoneOn(true);
               }
            } else {
               var1.setBluetoothScoOn(false);
               var1.setSpeakerphoneOn(false);
            }
         } else if (this.isBluetoothHeadsetConnected()) {
            var1.setBluetoothScoOn(this.speakerphoneStateToSet);
         } else {
            var1.setSpeakerphoneOn(this.speakerphoneStateToSet);
         }
      }

      this.updateOutputGainControlState();
      this.audioConfigured = true;
      SensorManager var3 = (SensorManager)this.getSystemService("sensor");
      Sensor var6 = var3.getDefaultSensor(8);
      if (var6 != null) {
         try {
            this.proximityWakelock = ((PowerManager)this.getSystemService("power")).newWakeLock(32, "telegram-voip-prx");
            var3.registerListener(this, var6, 3);
         } catch (Exception var5) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("Error initializing proximity sensor", var5);
            }
         }
      }

   }

   protected VoIPController createController() {
      return new VoIPController();
   }

   public abstract void declineIncomingCall();

   public abstract void declineIncomingCall(int var1, Runnable var2);

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.appDidLogout) {
         this.callEnded();
      }

   }

   protected void dispatchStateChanged(int var1) {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var2 = new StringBuilder();
         var2.append("== Call ");
         var2.append(this.getCallID());
         var2.append(" state changed to ");
         var2.append(var1);
         var2.append(" ==");
         FileLog.d(var2.toString());
      }

      this.currentState = var1;
      if (USE_CONNECTION_SERVICE && var1 == 3) {
         VoIPBaseService.CallConnection var4 = this.systemCallConnection;
         if (var4 != null) {
            var4.setActive();
         }
      }

      for(int var3 = 0; var3 < this.stateListeners.size(); ++var3) {
         ((VoIPBaseService.StateListener)this.stateListeners.get(var3)).onStateChanged(var1);
      }

   }

   protected void endConnectionServiceCall(long var1) {
      if (USE_CONNECTION_SERVICE) {
         Runnable var3 = new Runnable() {
            public void run() {
               VoIPBaseService var1 = VoIPBaseService.this;
               VoIPBaseService.CallConnection var2 = var1.systemCallConnection;
               if (var2 != null) {
                  int var3 = var1.callDiscardReason;
                  byte var4 = 2;
                  if (var3 != 1) {
                     if (var3 != 2) {
                        var4 = 4;
                        if (var3 != 3) {
                           if (var3 != 4) {
                              var2.setDisconnected(new DisconnectCause(3));
                           } else {
                              var2.setDisconnected(new DisconnectCause(7));
                           }
                        } else {
                           if (!var1.isOutgoing) {
                              var4 = 5;
                           }

                           var2.setDisconnected(new DisconnectCause(var4));
                        }
                     } else {
                        var2.setDisconnected(new DisconnectCause(1));
                     }
                  } else {
                     if (!var1.isOutgoing) {
                        var4 = 6;
                     }

                     var2.setDisconnected(new DisconnectCause(var4));
                  }

                  VoIPBaseService.this.systemCallConnection.destroy();
                  VoIPBaseService.this.systemCallConnection = null;
               }

            }
         };
         if (var1 > 0L) {
            AndroidUtilities.runOnUIThread(var3, var1);
         } else {
            var3.run();
         }
      }

   }

   public int getAccount() {
      return this.currentAccount;
   }

   public long getCallDuration() {
      if (this.controllerStarted) {
         VoIPController var1 = this.controller;
         if (var1 != null) {
            long var2 = var1.getCallDuration();
            this.lastKnownDuration = var2;
            return var2;
         }
      }

      return this.lastKnownDuration;
   }

   public abstract long getCallID();

   public int getCallState() {
      return this.currentState;
   }

   public abstract VoIPBaseService.CallConnection getConnectionAndStartCall();

   public int getCurrentAudioRoute() {
      if (!USE_CONNECTION_SERVICE) {
         if (this.audioConfigured) {
            AudioManager var3 = (AudioManager)this.getSystemService("audio");
            if (var3.isBluetoothScoOn()) {
               return 2;
            } else {
               return var3.isSpeakerphoneOn() ? 1 : 0;
            }
         } else {
            return this.audioRouteToSet;
         }
      } else {
         VoIPBaseService.CallConnection var1 = this.systemCallConnection;
         if (var1 != null && var1.getCallAudioState() != null) {
            int var2 = this.systemCallConnection.getCallAudioState().getRoute();
            if (var2 == 1) {
               return 0;
            }

            if (var2 == 2) {
               return 2;
            }

            if (var2 == 4) {
               return 0;
            }

            if (var2 == 8) {
               return 1;
            }
         }

         return this.audioRouteToSet;
      }
   }

   public String getDebugString() {
      return this.controller.getDebugString();
   }

   public int getLastError() {
      return this.lastError;
   }

   protected Bitmap getRoundAvatarBitmap(TLObject var1) {
      boolean var2 = var1 instanceof TLRPC.User;
      Bitmap var3 = null;
      Bitmap var6;
      BitmapDrawable var14;
      Options var15;
      if (var2) {
         TLRPC.User var4 = (TLRPC.User)var1;
         TLRPC.UserProfilePhoto var5 = var4.photo;
         var6 = var3;
         if (var5 != null) {
            var6 = var3;
            if (var5.photo_small != null) {
               var14 = ImageLoader.getInstance().getImageFromMemory(var4.photo.photo_small, (String)null, "50_50");
               if (var14 != null) {
                  var6 = var14.getBitmap().copy(Config.ARGB_8888, true);
               } else {
                  try {
                     var15 = new Options();
                     var15.inMutable = true;
                     var6 = BitmapFactory.decodeFile(FileLoader.getPathToAttach(var4.photo.photo_small, true).toString(), var15);
                  } catch (Throwable var8) {
                     FileLog.e(var8);
                     var6 = var3;
                  }
               }
            }
         }
      } else {
         TLRPC.Chat var11 = (TLRPC.Chat)var1;
         TLRPC.ChatPhoto var13 = var11.photo;
         var6 = var3;
         if (var13 != null) {
            var6 = var3;
            if (var13.photo_small != null) {
               var14 = ImageLoader.getInstance().getImageFromMemory(var11.photo.photo_small, (String)null, "50_50");
               if (var14 != null) {
                  var6 = var14.getBitmap().copy(Config.ARGB_8888, true);
               } else {
                  try {
                     var15 = new Options();
                     var15.inMutable = true;
                     var6 = BitmapFactory.decodeFile(FileLoader.getPathToAttach(var11.photo.photo_small, true).toString(), var15);
                  } catch (Throwable var7) {
                     FileLog.e(var7);
                     var6 = var3;
                  }
               }
            }
         }
      }

      var3 = var6;
      if (var6 == null) {
         Theme.createDialogsResources(this);
         AvatarDrawable var9;
         if (var2) {
            var9 = new AvatarDrawable((TLRPC.User)var1);
         } else {
            var9 = new AvatarDrawable((TLRPC.Chat)var1);
         }

         var3 = Bitmap.createBitmap(AndroidUtilities.dp(42.0F), AndroidUtilities.dp(42.0F), Config.ARGB_8888);
         var9.setBounds(0, 0, var3.getWidth(), var3.getHeight());
         var9.draw(new Canvas(var3));
      }

      Canvas var12 = new Canvas(var3);
      Path var10 = new Path();
      var10.addCircle((float)(var3.getWidth() / 2), (float)(var3.getHeight() / 2), (float)(var3.getWidth() / 2), Direction.CW);
      var10.toggleInverseFillType();
      Paint var16 = new Paint(1);
      var16.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
      var12.drawPath(var10, var16);
      return var3;
   }

   protected int getStatsNetworkType() {
      NetworkInfo var1 = this.lastNetInfo;
      byte var2;
      if (var1 != null && var1.getType() == 0) {
         if (this.lastNetInfo.isRoaming()) {
            var2 = 2;
         } else {
            var2 = 0;
         }
      } else {
         var2 = 1;
      }

      return var2;
   }

   protected abstract Class getUIActivityClass();

   public void handleNotificationAction(Intent var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getPackageName());
      var2.append(".END_CALL");
      if (var2.toString().equals(var1.getAction())) {
         this.stopForeground(true);
         this.hangUp();
      } else {
         var2 = new StringBuilder();
         var2.append(this.getPackageName());
         var2.append(".DECLINE_CALL");
         if (var2.toString().equals(var1.getAction())) {
            this.stopForeground(true);
            this.declineIncomingCall(4, (Runnable)null);
         } else {
            var2 = new StringBuilder();
            var2.append(this.getPackageName());
            var2.append(".ANSWER_CALL");
            if (var2.toString().equals(var1.getAction())) {
               this.acceptIncomingCallFromNotification();
            }
         }
      }

   }

   public abstract void hangUp();

   public abstract void hangUp(Runnable var1);

   public boolean hasEarpiece() {
      boolean var1 = USE_CONNECTION_SERVICE;
      boolean var2 = false;
      if (var1) {
         VoIPBaseService.CallConnection var3 = this.systemCallConnection;
         if (var3 != null && var3.getCallAudioState() != null) {
            if ((this.systemCallConnection.getCallAudioState().getSupportedRouteMask() & 5) != 0) {
               var2 = true;
            }

            return var2;
         }
      }

      if (((TelephonyManager)this.getSystemService("phone")).getPhoneType() != 0) {
         return true;
      } else {
         Boolean var7 = this.mHasEarpiece;
         if (var7 != null) {
            return var7;
         } else {
            try {
               AudioManager var8 = (AudioManager)this.getSystemService("audio");
               Method var4 = AudioManager.class.getMethod("getDevicesForStream", Integer.TYPE);
               int var5 = AudioManager.class.getField("DEVICE_OUT_EARPIECE").getInt((Object)null);
               if (((Integer)var4.invoke(var8, 0) & var5) == var5) {
                  this.mHasEarpiece = Boolean.TRUE;
               } else {
                  this.mHasEarpiece = Boolean.FALSE;
               }
            } catch (Throwable var6) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.e("Error while checking earpiece! ", var6);
               }

               this.mHasEarpiece = Boolean.TRUE;
            }

            return this.mHasEarpiece;
         }
      }
   }

   protected void initializeAccountRelatedThings() {
      this.updateServerConfig();
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
      ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
      this.controller = this.createController();
      this.controller.setConnectionStateListener(this);
   }

   public boolean isBluetoothHeadsetConnected() {
      if (USE_CONNECTION_SERVICE) {
         VoIPBaseService.CallConnection var1 = this.systemCallConnection;
         if (var1 != null && var1.getCallAudioState() != null) {
            boolean var2;
            if ((this.systemCallConnection.getCallAudioState().getSupportedRouteMask() & 2) != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      }

      return this.isBtHeadsetConnected;
   }

   protected boolean isFinished() {
      int var1 = this.currentState;
      boolean var2;
      if (var1 != 11 && var1 != 4) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isMicMute() {
      return this.micMute;
   }

   public boolean isOutgoing() {
      return this.isOutgoing;
   }

   protected boolean isRinging() {
      return false;
   }

   public boolean isSpeakerphoneOn() {
      boolean var4;
      if (USE_CONNECTION_SERVICE) {
         VoIPBaseService.CallConnection var1 = this.systemCallConnection;
         if (var1 != null && var1.getCallAudioState() != null) {
            int var2 = this.systemCallConnection.getCallAudioState().getRoute();
            boolean var3 = this.hasEarpiece();
            var4 = true;
            if (var3) {
               if (var2 == 8) {
                  return var4;
               }
            } else if (var2 == 2) {
               return var4;
            }

            var4 = false;
            return var4;
         }
      }

      if (this.audioConfigured && !USE_CONNECTION_SERVICE) {
         AudioManager var5 = (AudioManager)this.getSystemService("audio");
         if (this.hasEarpiece()) {
            var4 = var5.isSpeakerphoneOn();
         } else {
            var4 = var5.isBluetoothScoOn();
         }

         return var4;
      } else {
         return this.speakerphoneStateToSet;
      }
   }

   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   public void onAudioFocusChange(int var1) {
      if (var1 == 1) {
         this.haveAudioFocus = true;
      } else {
         this.haveAudioFocus = false;
      }

   }

   public void onConnectionStateChanged(int var1) {
      if (var1 == 4) {
         this.callFailed();
      } else {
         int var3;
         if (var1 == 3) {
            Runnable var2 = this.connectingSoundRunnable;
            if (var2 != null) {
               AndroidUtilities.cancelRunOnUIThread(var2);
               this.connectingSoundRunnable = null;
            }

            var3 = this.spPlayID;
            if (var3 != 0) {
               this.soundPool.stop(var3);
               this.spPlayID = 0;
            }

            if (!this.wasEstablished) {
               this.wasEstablished = true;
               if (!this.isProximityNear) {
                  Vibrator var4 = (Vibrator)this.getSystemService("vibrator");
                  if (var4.hasVibrator()) {
                     var4.vibrate(100L);
                  }
               }

               AndroidUtilities.runOnUIThread(new Runnable() {
                  public void run() {
                     VoIPBaseService var1 = VoIPBaseService.this;
                     if (var1.controller != null) {
                        int var2 = var1.getStatsNetworkType();
                        StatsController.getInstance(VoIPBaseService.this.currentAccount).incrementTotalCallsTime(var2, 5);
                        AndroidUtilities.runOnUIThread(this, 5000L);
                     }
                  }
               }, 5000L);
               if (this.isOutgoing) {
                  StatsController.getInstance(this.currentAccount).incrementSentItemsCount(this.getStatsNetworkType(), 0, 1);
               } else {
                  StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(this.getStatsNetworkType(), 0, 1);
               }
            }
         }

         if (var1 == 5) {
            var3 = this.spPlayID;
            if (var3 != 0) {
               this.soundPool.stop(var3);
            }

            this.spPlayID = this.soundPool.play(this.spConnectingId, 1.0F, 1.0F, 0, -1, 1.0F);
         }

         this.dispatchStateChanged(var1);
      }
   }

   protected void onControllerPreRelease() {
   }

   public void onCreate() {
      super.onCreate();
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("=============== VoIPService STARTING ===============");
      }

      AudioManager var1 = (AudioManager)this.getSystemService("audio");
      if (VERSION.SDK_INT >= 17 && var1.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER") != null) {
         VoIPController.setNativeBufferSize(Integer.parseInt(var1.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER")));
      } else {
         VoIPController.setNativeBufferSize(AudioTrack.getMinBufferSize(48000, 4, 2) / 2);
      }

      Exception var10000;
      label115: {
         BluetoothAdapter var2;
         boolean var10001;
         label99: {
            try {
               this.cpuWakelock = ((PowerManager)this.getSystemService("power")).newWakeLock(1, "telegram-voip");
               this.cpuWakelock.acquire();
               if (var1.isBluetoothScoAvailableOffCall()) {
                  var2 = BluetoothAdapter.getDefaultAdapter();
                  break label99;
               }
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
               break label115;
            }

            var2 = null;
         }

         IntentFilter var12;
         label90: {
            try {
               this.btAdapter = var2;
               var12 = new IntentFilter();
               var12.addAction("android.net.conn.CONNECTIVITY_CHANGE");
               if (USE_CONNECTION_SERVICE) {
                  break label90;
               }

               var12.addAction("android.intent.action.HEADSET_PLUG");
               if (this.btAdapter != null) {
                  var12.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                  var12.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
               }
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label115;
            }

            try {
               var12.addAction("android.intent.action.PHONE_STATE");
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label115;
            }
         }

         SoundPool var13;
         try {
            this.registerReceiver(this.receiver, var12);
            var13 = new SoundPool;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label115;
         }

         boolean var3 = false;

         try {
            var13.<init>(1, 0, 0);
            this.soundPool = var13;
            this.spConnectingId = this.soundPool.load(this, 2131492875, 1);
            this.spRingbackID = this.soundPool.load(this, 2131492878, 1);
            this.spFailedID = this.soundPool.load(this, 2131492877, 1);
            this.spEndId = this.soundPool.load(this, 2131492876, 1);
            this.spBusyId = this.soundPool.load(this, 2131492874, 1);
            ComponentName var14 = new ComponentName(this, VoIPMediaButtonReceiver.class);
            var1.registerMediaButtonEventReceiver(var14);
            if (USE_CONNECTION_SERVICE || this.btAdapter == null || !this.btAdapter.isEnabled()) {
               return;
            }
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label115;
         }

         label75: {
            try {
               if (this.btAdapter.getProfileConnectionState(1) != 2) {
                  break label75;
               }
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label115;
            }

            var3 = true;
         }

         Iterator var15;
         try {
            this.updateBluetoothHeadsetState(var3);
            var15 = this.stateListeners.iterator();
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label115;
         }

         while(true) {
            try {
               if (!var15.hasNext()) {
                  return;
               }

               ((VoIPBaseService.StateListener)var15.next()).onAudioSettingsChanged();
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break;
            }
         }
      }

      Exception var16 = var10000;
      if (BuildVars.LOGS_ENABLED) {
         FileLog.e("error initializing voip controller", var16);
      }

      this.callFailed();
   }

   public void onDestroy() {
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("=============== VoIPService STOPPING ===============");
      }

      this.stopForeground(true);
      this.stopRinging();
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.appDidLogout);
      SensorManager var1 = (SensorManager)this.getSystemService("sensor");
      if (var1.getDefaultSensor(8) != null) {
         var1.unregisterListener(this);
      }

      WakeLock var4 = this.proximityWakelock;
      if (var4 != null && var4.isHeld()) {
         this.proximityWakelock.release();
      }

      this.unregisterReceiver(this.receiver);
      Runnable var5 = this.timeoutRunnable;
      if (var5 != null) {
         AndroidUtilities.cancelRunOnUIThread(var5);
         this.timeoutRunnable = null;
      }

      super.onDestroy();
      sharedInstance = null;
      AndroidUtilities.runOnUIThread(new Runnable() {
         public void run() {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didEndedCall);
         }
      });
      VoIPController var6 = this.controller;
      if (var6 != null && this.controllerStarted) {
         this.lastKnownDuration = var6.getCallDuration();
         this.updateStats();
         StatsController.getInstance(this.currentAccount).incrementTotalCallsTime(this.getStatsNetworkType(), (int)(this.lastKnownDuration / 1000L) % 5);
         this.onControllerPreRelease();
         this.controller.release();
         this.controller = null;
      }

      this.cpuWakelock.release();
      AudioManager var7 = (AudioManager)this.getSystemService("audio");
      if (!USE_CONNECTION_SERVICE) {
         if (this.isBtHeadsetConnected && !this.playingSound) {
            var7.stopBluetoothSco();
            var7.setSpeakerphoneOn(false);
         }

         try {
            var7.setMode(0);
         } catch (SecurityException var3) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.e("Error setting audio more to normal", var3);
            }
         }

         var7.abandonAudioFocus(this);
      }

      var7.unregisterMediaButtonEventReceiver(new ComponentName(this, VoIPMediaButtonReceiver.class));
      if (this.haveAudioFocus) {
         var7.abandonAudioFocus(this);
      }

      if (!this.playingSound) {
         this.soundPool.release();
      }

      if (USE_CONNECTION_SERVICE) {
         if (!this.didDeleteConnectionServiceContact) {
            ContactsController.getInstance(this.currentAccount).deleteConnectionServiceContact();
         }

         VoIPBaseService.CallConnection var8 = this.systemCallConnection;
         if (var8 != null && !this.playingSound) {
            var8.destroy();
         }
      }

      ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
      VoIPHelper.lastCallTime = System.currentTimeMillis();
   }

   @SuppressLint({"NewApi"})
   public void onSensorChanged(SensorEvent var1) {
      if (var1.sensor.getType() == 8) {
         AudioManager var2 = (AudioManager)this.getSystemService("audio");
         if (!this.isHeadsetPlugged && !var2.isSpeakerphoneOn() && (!this.isBluetoothHeadsetConnected() || !var2.isBluetoothScoOn())) {
            float[] var6 = var1.values;
            boolean var3 = false;
            if (var6[0] < Math.min(var1.sensor.getMaximumRange(), 3.0F)) {
               var3 = true;
            }

            if (var3 != this.isProximityNear) {
               if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append("proximity ");
                  var5.append(var3);
                  FileLog.d(var5.toString());
               }

               this.isProximityNear = var3;

               try {
                  if (this.isProximityNear) {
                     this.proximityWakelock.acquire();
                  } else {
                     this.proximityWakelock.release(1);
                  }
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }
            }
         }
      }

   }

   public void onSignalBarCountChanged(int var1) {
      this.signalBarCount = var1;

      for(int var2 = 0; var2 < this.stateListeners.size(); ++var2) {
         ((VoIPBaseService.StateListener)this.stateListeners.get(var2)).onSignalBarsCountChanged(var1);
      }

   }

   public void registerStateListener(VoIPBaseService.StateListener var1) {
      this.stateListeners.add(var1);
      int var2 = this.currentState;
      if (var2 != 0) {
         var1.onStateChanged(var2);
      }

      var2 = this.signalBarCount;
      if (var2 != 0) {
         var1.onSignalBarsCountChanged(var2);
      }

   }

   public void setMicMute(boolean var1) {
      this.micMute = var1;
      VoIPController var2 = this.controller;
      if (var2 != null) {
         var2.setMicMute(var1);
      }

   }

   protected void showIncomingNotification(String var1, CharSequence var2, TLObject var3, List var4, int var5, Class var6) {
      Intent var7 = new Intent(this, var6);
      var7.addFlags(805306368);
      android.app.Notification.Builder var18 = (new android.app.Notification.Builder(this)).setContentTitle(LocaleController.getString("VoipInCallBranding", 2131561071)).setContentText(var1).setSmallIcon(2131165698).setSubText(var2).setContentIntent(PendingIntent.getActivity(this, 0, var7, 0));
      Uri var15 = Uri.parse("content://org.telegram.messenger.call_sound_provider/start_ringing");
      var5 = VERSION.SDK_INT;
      StringBuilder var16;
      if (var5 >= 26) {
         SharedPreferences var8 = MessagesController.getGlobalNotificationsSettings();
         int var9 = var8.getInt("calls_notification_channel", 0);
         NotificationManager var10 = (NotificationManager)this.getSystemService("notification");
         StringBuilder var11 = new StringBuilder();
         var11.append("incoming_calls");
         var11.append(var9);
         NotificationChannel var28 = var10.getNotificationChannel(var11.toString());
         if (var28 != null) {
            var10.deleteNotificationChannel(var28.getId());
         }

         boolean var12;
         label79: {
            var11 = new StringBuilder();
            var11.append("incoming_calls2");
            var11.append(var9);
            var28 = var10.getNotificationChannel(var11.toString());
            var5 = var9;
            if (var28 != null) {
               if (var28.getImportance() >= 4 && var15.equals(var28.getSound()) && var28.getVibrationPattern() == null && !var28.shouldVibrate()) {
                  var12 = false;
                  var5 = var9;
                  break label79;
               }

               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("User messed up the notification channel; deleting it and creating a proper one");
               }

               var11 = new StringBuilder();
               var11.append("incoming_calls2");
               var11.append(var9);
               var10.deleteNotificationChannel(var11.toString());
               var5 = var9 + 1;
               var8.edit().putInt("calls_notification_channel", var5).commit();
            }

            var12 = true;
         }

         if (var12) {
            AudioAttributes var23 = (new android.media.AudioAttributes.Builder()).setUsage(6).build();
            var11 = new StringBuilder();
            var11.append("incoming_calls2");
            var11.append(var5);
            var28 = new NotificationChannel(var11.toString(), LocaleController.getString("IncomingCalls", 2131559662), 4);
            var28.setSound(var15, var23);
            var28.enableVibration(false);
            var28.enableLights(false);
            var10.createNotificationChannel(var28);
         }

         var16 = new StringBuilder();
         var16.append("incoming_calls2");
         var16.append(var5);
         var18.setChannelId(var16.toString());
      } else if (var5 >= 21) {
         var18.setSound(var15, 2);
      }

      Intent var26 = new Intent(this, VoIPActionsReceiver.class);
      var16 = new StringBuilder();
      var16.append(this.getPackageName());
      var16.append(".DECLINE_CALL");
      var26.setAction(var16.toString());
      var26.putExtra("call_id", this.getCallID());
      Object var17 = LocaleController.getString("VoipDeclineCall", 2131561064);
      if (VERSION.SDK_INT >= 24) {
         var17 = new SpannableString((CharSequence)var17);
         ((SpannableString)var17).setSpan(new ForegroundColorSpan(-769226), 0, ((CharSequence)var17).length(), 0);
      }

      PendingIntent var27 = PendingIntent.getBroadcast(this, 0, var26, 268435456);
      var18.addAction(2131165430, (CharSequence)var17, var27);
      Intent var24 = new Intent(this, VoIPActionsReceiver.class);
      var16 = new StringBuilder();
      var16.append(this.getPackageName());
      var16.append(".ANSWER_CALL");
      var24.setAction(var16.toString());
      var24.putExtra("call_id", this.getCallID());
      var17 = LocaleController.getString("VoipAnswerCall", 2131561056);
      if (VERSION.SDK_INT >= 24) {
         var17 = new SpannableString((CharSequence)var17);
         ((SpannableString)var17).setSpan(new ForegroundColorSpan(-16733696), 0, ((CharSequence)var17).length(), 0);
      }

      PendingIntent var25 = PendingIntent.getBroadcast(this, 0, var24, 268435456);
      var18.addAction(2131165429, (CharSequence)var17, var25);
      var18.setPriority(2);
      if (VERSION.SDK_INT >= 17) {
         var18.setShowWhen(false);
      }

      if (VERSION.SDK_INT >= 21) {
         var18.setColor(-13851168);
         var18.setVibrate(new long[0]);
         var18.setCategory("call");
         var18.setFullScreenIntent(PendingIntent.getActivity(this, 0, var7, 0), true);
         if (var3 instanceof TLRPC.User) {
            TLRPC.User var19 = (TLRPC.User)var3;
            if (!TextUtils.isEmpty(var19.phone)) {
               var16 = new StringBuilder();
               var16.append("tel:");
               var16.append(var19.phone);
               var18.addPerson(var16.toString());
            }
         }
      }

      Notification var21 = var18.getNotification();
      if (VERSION.SDK_INT >= 21) {
         String var20 = this.getPackageName();
         if (LocaleController.isRTL) {
            var5 = 2131361822;
         } else {
            var5 = 2131361821;
         }

         RemoteViews var22 = new RemoteViews(var20, var5);
         var22.setTextViewText(2131230851, var1);
         TLRPC.User var13;
         if (TextUtils.isEmpty(var2)) {
            var22.setViewVisibility(2131230919, 8);
            if (UserConfig.getActivatedAccountsCount() > 1) {
               var13 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
               var22.setTextViewText(2131230935, LocaleController.formatString("VoipInCallBrandingWithName", 2131561072, ContactsController.formatName(var13.first_name, var13.last_name)));
            } else {
               var22.setTextViewText(2131230935, LocaleController.getString("VoipInCallBranding", 2131561071));
            }
         } else {
            if (UserConfig.getActivatedAccountsCount() > 1) {
               var13 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
               var22.setTextViewText(2131230919, LocaleController.formatString("VoipAnsweringAsAccount", 2131561057, ContactsController.formatName(var13.first_name, var13.last_name)));
            } else {
               var22.setViewVisibility(2131230919, 8);
            }

            var22.setTextViewText(2131230935, var2);
         }

         Bitmap var14 = this.getRoundAvatarBitmap(var3);
         var22.setTextViewText(2131230786, LocaleController.getString("VoipAnswerCall", 2131561056));
         var22.setTextViewText(2131230813, LocaleController.getString("VoipDeclineCall", 2131561064));
         var22.setImageViewBitmap(2131230872, var14);
         var22.setOnClickPendingIntent(2131230785, var25);
         var22.setOnClickPendingIntent(2131230812, var27);
         var18.setLargeIcon(var14);
         var21.bigContentView = var22;
         var21.headsUpContentView = var22;
      }

      this.startForeground(202, var21);
   }

   protected abstract void showNotification();

   protected void showNotification(String var1, TLRPC.FileLocation var2, Class var3) {
      Intent var13 = new Intent(this, var3);
      var13.addFlags(805306368);
      android.app.Notification.Builder var10 = (new android.app.Notification.Builder(this)).setContentTitle(LocaleController.getString("VoipOutgoingCall", 2131561083)).setContentText(var1).setSmallIcon(2131165698).setContentIntent(PendingIntent.getActivity(this, 0, var13, 0));
      if (VERSION.SDK_INT >= 16) {
         var13 = new Intent(this, VoIPActionsReceiver.class);
         StringBuilder var4 = new StringBuilder();
         var4.append(this.getPackageName());
         var4.append(".END_CALL");
         var13.setAction(var4.toString());
         var10.addAction(2131165430, LocaleController.getString("VoipEndCall", 2131561065), PendingIntent.getBroadcast(this, 0, var13, 134217728));
         var10.setPriority(2);
      }

      if (VERSION.SDK_INT >= 17) {
         var10.setShowWhen(false);
      }

      if (VERSION.SDK_INT >= 21) {
         var10.setColor(-13851168);
      }

      if (VERSION.SDK_INT >= 26) {
         NotificationsController.checkOtherNotificationsChannel();
         var10.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
      }

      if (var2 != null) {
         BitmapDrawable var14 = ImageLoader.getInstance().getImageFromMemory(var2, (String)null, "50_50");
         if (var14 != null) {
            var10.setLargeIcon(var14.getBitmap());
         } else {
            label47: {
               Throwable var10000;
               label58: {
                  float var5;
                  Options var15;
                  boolean var10001;
                  try {
                     var5 = 160.0F / (float)AndroidUtilities.dp(50.0F);
                     var15 = new Options();
                  } catch (Throwable var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label58;
                  }

                  int var6;
                  if (var5 < 1.0F) {
                     var6 = 1;
                  } else {
                     var6 = (int)var5;
                  }

                  Bitmap var11;
                  try {
                     var15.inSampleSize = var6;
                     var11 = BitmapFactory.decodeFile(FileLoader.getPathToAttach(var2, true).toString(), var15);
                  } catch (Throwable var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label58;
                  }

                  if (var11 == null) {
                     break label47;
                  }

                  try {
                     var10.setLargeIcon(var11);
                     break label47;
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                  }
               }

               Throwable var12 = var10000;
               FileLog.e(var12);
            }
         }
      }

      this.ongoingCallNotification = var10.getNotification();
      this.startForeground(201, this.ongoingCallNotification);
   }

   protected abstract void startRinging();

   public abstract void startRingtoneAndVibration();

   protected void startRingtoneAndVibration(int var1) {
      SharedPreferences var2 = MessagesController.getNotificationsSettings(this.currentAccount);
      AudioManager var3 = (AudioManager)this.getSystemService("audio");
      boolean var4;
      if (var3.getRingerMode() != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var4) {
         if (!USE_CONNECTION_SERVICE) {
            var3.requestAudioFocus(this, 2, 1);
         }

         this.ringtonePlayer = new MediaPlayer();
         this.ringtonePlayer.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer var1) {
               VoIPBaseService.this.ringtonePlayer.start();
            }
         });
         this.ringtonePlayer.setLooping(true);
         this.ringtonePlayer.setAudioStreamType(2);

         StringBuilder var5;
         label73: {
            Exception var10000;
            label72: {
               String var11;
               boolean var10001;
               label86: {
                  try {
                     var5 = new StringBuilder();
                     var5.append("custom_");
                     var5.append(var1);
                     if (var2.getBoolean(var5.toString(), false)) {
                        var5 = new StringBuilder();
                        var5.append("ringtone_path_");
                        var5.append(var1);
                        var11 = var2.getString(var5.toString(), RingtoneManager.getDefaultUri(1).toString());
                        break label86;
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label72;
                  }

                  try {
                     var11 = var2.getString("CallsRingtonePath", RingtoneManager.getDefaultUri(1).toString());
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label72;
                  }
               }

               try {
                  this.ringtonePlayer.setDataSource(this, Uri.parse(var11));
                  this.ringtonePlayer.prepareAsync();
                  break label73;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
               }
            }

            Exception var12 = var10000;
            FileLog.e((Throwable)var12);
            MediaPlayer var13 = this.ringtonePlayer;
            if (var13 != null) {
               var13.release();
               this.ringtonePlayer = null;
            }
         }

         var5 = new StringBuilder();
         var5.append("custom_");
         var5.append(var1);
         if (var2.getBoolean(var5.toString(), false)) {
            var5 = new StringBuilder();
            var5.append("calls_vibrate_");
            var5.append(var1);
            var1 = var2.getInt(var5.toString(), 0);
         } else {
            var1 = var2.getInt("vibrate_calls", 0);
         }

         if (var1 != 2 && var1 != 4 && (var3.getRingerMode() == 1 || var3.getRingerMode() == 2) || var1 == 4 && var3.getRingerMode() == 1) {
            this.vibrator = (Vibrator)this.getSystemService("vibrator");
            long var6 = 700L;
            if (var1 == 1) {
               var6 = 350L;
            } else if (var1 == 3) {
               var6 = 1400L;
            }

            this.vibrator.vibrate(new long[]{0L, var6, 500L}, 0);
         }
      }

   }

   public void stopRinging() {
      MediaPlayer var1 = this.ringtonePlayer;
      if (var1 != null) {
         var1.stop();
         this.ringtonePlayer.release();
         this.ringtonePlayer = null;
      }

      Vibrator var2 = this.vibrator;
      if (var2 != null) {
         var2.cancel();
         this.vibrator = null;
      }

   }

   public void toggleSpeakerphoneOrShowRouteSheet(Activity var1) {
      if (this.isBluetoothHeadsetConnected() && this.hasEarpiece()) {
         BottomSheet.Builder var2 = new BottomSheet.Builder(var1);
         String var3 = LocaleController.getString("VoipAudioRoutingBluetooth", 2131561058);
         int var14 = 0;
         String var5 = LocaleController.getString("VoipAudioRoutingEarpiece", 2131561059);
         String var11 = LocaleController.getString("VoipAudioRoutingSpeaker", 2131561060);
         OnClickListener var6 = new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               AudioManager var5 = (AudioManager)VoIPBaseService.this.getSystemService("audio");
               if (VoIPBaseService.getSharedInstance() != null) {
                  label82: {
                     if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                        VoIPBaseService.CallConnection var3 = VoIPBaseService.this.systemCallConnection;
                        if (var3 != null) {
                           if (var2 != 0) {
                              if (var2 != 1) {
                                 if (var2 == 2) {
                                    var3.setAudioRoute(8);
                                 }
                              } else {
                                 var3.setAudioRoute(5);
                              }
                           } else {
                              var3.setAudioRoute(2);
                           }
                           break label82;
                        }
                     }

                     VoIPBaseService var7 = VoIPBaseService.this;
                     if (var7.audioConfigured && !VoIPBaseService.USE_CONNECTION_SERVICE) {
                        if (var2 != 0) {
                           if (var2 != 1) {
                              if (var2 == 2) {
                                 if (var7.bluetoothScoActive) {
                                    var5.stopBluetoothSco();
                                 }

                                 var5.setBluetoothScoOn(false);
                                 var5.setSpeakerphoneOn(true);
                              }
                           } else {
                              if (var7.bluetoothScoActive) {
                                 var5.stopBluetoothSco();
                              }

                              var5.setSpeakerphoneOn(false);
                              var5.setBluetoothScoOn(false);
                           }
                        } else if (!var7.bluetoothScoActive) {
                           var7.needSwitchToBluetoothAfterScoActivates = true;

                           try {
                              var5.startBluetoothSco();
                           } catch (Throwable var4) {
                           }
                        } else {
                           var5.setBluetoothScoOn(true);
                           var5.setSpeakerphoneOn(false);
                        }

                        VoIPBaseService.this.updateOutputGainControlState();
                     } else if (var2 != 0) {
                        if (var2 != 1) {
                           if (var2 == 2) {
                              VoIPBaseService.this.audioRouteToSet = 1;
                           }
                        } else {
                           VoIPBaseService.this.audioRouteToSet = 0;
                        }
                     } else {
                        VoIPBaseService.this.audioRouteToSet = 2;
                     }
                  }

                  Iterator var6 = VoIPBaseService.this.stateListeners.iterator();

                  while(var6.hasNext()) {
                     ((VoIPBaseService.StateListener)var6.next()).onAudioSettingsChanged();
                  }

               }
            }
         };
         BottomSheet var12 = var2.setItems(new CharSequence[]{var3, var5, var11}, new int[]{2131165428, 2131165461, 2131165476}, var6).create();
         var12.setBackgroundColor(-13948117);
         var12.show();

         for(ViewGroup var13 = var12.getSheetContainer(); var14 < var13.getChildCount(); ++var14) {
            ((BottomSheet.BottomSheetCell)var13.getChildAt(var14)).setTextColor(-1);
         }

      } else {
         label67: {
            if (USE_CONNECTION_SERVICE) {
               VoIPBaseService.CallConnection var8 = this.systemCallConnection;
               if (var8 != null && var8.getCallAudioState() != null) {
                  boolean var7 = this.hasEarpiece();
                  byte var4 = 5;
                  if (var7) {
                     var8 = this.systemCallConnection;
                     if (var8.getCallAudioState().getRoute() != 8) {
                        var4 = 8;
                     }

                     var8.setAudioRoute(var4);
                  } else {
                     var8 = this.systemCallConnection;
                     if (var8.getCallAudioState().getRoute() != 2) {
                        var4 = 2;
                     }

                     var8.setAudioRoute(var4);
                  }
                  break label67;
               }
            }

            if (this.audioConfigured && !USE_CONNECTION_SERVICE) {
               AudioManager var9 = (AudioManager)this.getSystemService("audio");
               if (this.hasEarpiece()) {
                  var9.setSpeakerphoneOn(var9.isSpeakerphoneOn() ^ true);
               } else {
                  var9.setBluetoothScoOn(var9.isBluetoothScoOn() ^ true);
               }

               this.updateOutputGainControlState();
            } else {
               this.speakerphoneStateToSet ^= true;
            }
         }

         Iterator var10 = this.stateListeners.iterator();

         while(var10.hasNext()) {
            ((VoIPBaseService.StateListener)var10.next()).onAudioSettingsChanged();
         }

      }
   }

   public void unregisterStateListener(VoIPBaseService.StateListener var1) {
      this.stateListeners.remove(var1);
   }

   protected void updateBluetoothHeadsetState(boolean var1) {
      if (var1 != this.isBtHeadsetConnected) {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var2 = new StringBuilder();
            var2.append("updateBluetoothHeadsetState: ");
            var2.append(var1);
            FileLog.d(var2.toString());
         }

         this.isBtHeadsetConnected = var1;
         final AudioManager var3 = (AudioManager)this.getSystemService("audio");
         if (var1 && !this.isRinging() && this.currentState != 0) {
            if (this.bluetoothScoActive) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("SCO already active, setting audio routing");
               }

               var3.setSpeakerphoneOn(false);
               var3.setBluetoothScoOn(true);
            } else {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("startBluetoothSco");
               }

               this.needSwitchToBluetoothAfterScoActivates = true;
               AndroidUtilities.runOnUIThread(new Runnable() {
                  public void run() {
                     try {
                        var3.startBluetoothSco();
                     } catch (Throwable var2) {
                     }

                  }
               }, 500L);
            }
         } else {
            this.bluetoothScoActive = false;
         }

         Iterator var4 = this.stateListeners.iterator();

         while(var4.hasNext()) {
            ((VoIPBaseService.StateListener)var4.next()).onAudioSettingsChanged();
         }

      }
   }

   protected void updateNetworkType() {
      byte var2;
      label33: {
         NetworkInfo var1 = ((ConnectivityManager)this.getSystemService("connectivity")).getActiveNetworkInfo();
         this.lastNetInfo = var1;
         var2 = 1;
         if (var1 != null) {
            int var3 = var1.getType();
            if (var3 == 0) {
               switch(var1.getSubtype()) {
               case 1:
                  break label33;
               case 2:
               case 7:
                  var2 = 2;
                  break label33;
               case 3:
               case 5:
                  var2 = 3;
                  break label33;
               case 4:
               case 11:
               case 14:
               default:
                  var2 = 11;
                  break label33;
               case 6:
               case 8:
               case 9:
               case 10:
               case 12:
               case 15:
                  var2 = 4;
                  break label33;
               case 13:
                  var2 = 5;
                  break label33;
               }
            }

            if (var3 == 1) {
               var2 = 6;
               break label33;
            }

            if (var3 == 9) {
               var2 = 7;
               break label33;
            }
         }

         var2 = 0;
      }

      VoIPController var4 = this.controller;
      if (var4 != null) {
         var4.setNetworkType(var2);
      }

   }

   public void updateOutputGainControlState() {
      if (this.controller != null && this.controllerStarted) {
         boolean var1 = USE_CONNECTION_SERVICE;
         byte var2 = 0;
         byte var3 = 0;
         if (!var1) {
            AudioManager var4 = (AudioManager)this.getSystemService("audio");
            VoIPController var5 = this.controller;
            boolean var7;
            if (this.hasEarpiece() && !var4.isSpeakerphoneOn() && !var4.isBluetoothScoOn() && !this.isHeadsetPlugged) {
               var7 = true;
            } else {
               var7 = false;
            }

            var5.setAudioOutputGainControlEnabled(var7);
            var5 = this.controller;
            byte var6 = var3;
            if (!this.isHeadsetPlugged) {
               if (this.hasEarpiece() && !var4.isSpeakerphoneOn() && !var4.isBluetoothScoOn() && !this.isHeadsetPlugged) {
                  var6 = var3;
               } else {
                  var6 = 1;
               }
            }

            var5.setEchoCancellationStrength(var6);
         } else {
            if (this.systemCallConnection.getCallAudioState().getRoute() == 1) {
               var2 = 1;
            }

            this.controller.setAudioOutputGainControlEnabled((boolean)var2);
            this.controller.setEchoCancellationStrength(var2 ^ 1);
         }
      }

   }

   protected abstract void updateServerConfig();

   protected void updateStats() {
      this.controller.getStats(this.stats);
      VoIPController.Stats var1 = this.stats;
      long var2 = var1.bytesSentWifi;
      VoIPController.Stats var4 = this.prevStats;
      long var5 = var2 - var4.bytesSentWifi;
      var2 = var1.bytesRecvdWifi - var4.bytesRecvdWifi;
      long var7 = var1.bytesSentMobile - var4.bytesSentMobile;
      long var9 = var1.bytesRecvdMobile - var4.bytesRecvdMobile;
      this.stats = var4;
      this.prevStats = var1;
      if (var5 > 0L) {
         StatsController.getInstance(this.currentAccount).incrementSentBytesCount(1, 0, var5);
      }

      if (var2 > 0L) {
         StatsController.getInstance(this.currentAccount).incrementReceivedBytesCount(1, 0, var2);
      }

      byte var11 = 2;
      byte var12;
      if (var7 > 0L) {
         StatsController var15 = StatsController.getInstance(this.currentAccount);
         NetworkInfo var13 = this.lastNetInfo;
         if (var13 != null && var13.isRoaming()) {
            var12 = 2;
         } else {
            var12 = 0;
         }

         var15.incrementSentBytesCount(var12, 0, var7);
      }

      if (var9 > 0L) {
         StatsController var14 = StatsController.getInstance(this.currentAccount);
         NetworkInfo var16 = this.lastNetInfo;
         if (var16 != null && var16.isRoaming()) {
            var12 = var11;
         } else {
            var12 = 0;
         }

         var14.incrementReceivedBytesCount(var12, 0, var9);
      }

   }

   @TargetApi(26)
   public class CallConnection extends Connection {
      public CallConnection() {
         this.setConnectionProperties(128);
         this.setAudioModeIsVoip(true);
      }

      public void onAnswer() {
         VoIPBaseService.this.acceptIncomingCallFromNotification();
      }

      public void onCallAudioStateChanged(CallAudioState var1) {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var2 = new StringBuilder();
            var2.append("ConnectionService call audio state changed: ");
            var2.append(var1);
            FileLog.d(var2.toString());
         }

         Iterator var3 = VoIPBaseService.this.stateListeners.iterator();

         while(var3.hasNext()) {
            ((VoIPBaseService.StateListener)var3.next()).onAudioSettingsChanged();
         }

      }

      public void onCallEvent(String var1, Bundle var2) {
         super.onCallEvent(var1, var2);
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var3 = new StringBuilder();
            var3.append("ConnectionService onCallEvent ");
            var3.append(var1);
            FileLog.d(var3.toString());
         }

      }

      public void onDisconnect() {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("ConnectionService onDisconnect");
         }

         this.setDisconnected(new DisconnectCause(2));
         this.destroy();
         VoIPBaseService var1 = VoIPBaseService.this;
         var1.systemCallConnection = null;
         var1.hangUp();
      }

      public void onReject() {
         VoIPBaseService var1 = VoIPBaseService.this;
         var1.needPlayEndSound = false;
         var1.declineIncomingCall(1, (Runnable)null);
      }

      public void onShowIncomingCallUi() {
         VoIPBaseService.this.startRinging();
      }

      public void onSilence() {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("onSlience");
         }

         VoIPBaseService.this.stopRinging();
      }

      public void onStateChanged(int var1) {
         super.onStateChanged(var1);
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var2 = new StringBuilder();
            var2.append("ConnectionService onStateChanged ");
            var2.append(Connection.stateToString(var1));
            FileLog.d(var2.toString());
         }

         if (var1 == 4) {
            ContactsController.getInstance(VoIPBaseService.this.currentAccount).deleteConnectionServiceContact();
            VoIPBaseService.this.didDeleteConnectionServiceContact = true;
         }

      }
   }

   public interface StateListener {
      void onAudioSettingsChanged();

      void onSignalBarsCountChanged(int var1);

      void onStateChanged(int var1);
   }
}
