package org.telegram.messenger.voip;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;
import androidx.core.app.NotificationManagerCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.VoIPActivity;
import org.telegram.ui.VoIPFeedbackActivity;
import org.telegram.ui.Components.voip.VoIPHelper;

public class VoIPService extends VoIPBaseService {
   public static final int CALL_MAX_LAYER = VoIPController.getConnectionMaxLayer();
   public static final int CALL_MIN_LAYER = 65;
   public static final int STATE_BUSY = 17;
   public static final int STATE_EXCHANGING_KEYS = 12;
   public static final int STATE_HANGING_UP = 10;
   public static final int STATE_REQUESTING = 14;
   public static final int STATE_RINGING = 16;
   public static final int STATE_WAITING = 13;
   public static final int STATE_WAITING_INCOMING = 15;
   public static TLRPC.PhoneCall callIShouldHavePutIntoIntent;
   private byte[] a_or_b;
   private byte[] authKey;
   private TLRPC.PhoneCall call;
   private int callReqId;
   private String debugLog;
   private Runnable delayedStartOutgoingCall;
   private boolean endCallAfterRequest = false;
   private boolean forceRating;
   private byte[] g_a;
   private byte[] g_a_hash;
   private byte[] groupCallEncryptionKey;
   private long groupCallKeyFingerprint;
   private List groupUsersToAdd = new ArrayList();
   private boolean joiningGroupCall;
   private long keyFingerprint;
   private boolean needSendDebugLog = false;
   private int peerCapabilities;
   private ArrayList pendingUpdates = new ArrayList();
   private boolean startedRinging = false;
   private boolean upgrading;
   private TLRPC.User user;

   private void acknowledgeCall(final boolean var1) {
      if (this.call instanceof TLRPC.TL_phoneCallDiscarded) {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Call ");
            var5.append(this.call.id);
            var5.append(" was discarded before the service started, stopping");
            FileLog.w(var5.toString());
         }

         this.stopSelf();
      } else if (VERSION.SDK_INT >= 19 && XiaomiUtilities.isMIUI() && !XiaomiUtilities.isCustomPermissionGranted(10020) && ((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("MIUI: no permission to show when locked but the screen is locked. ¯\\_(ツ)_/¯");
         }

         this.stopSelf();
      } else {
         TLRPC.TL_phone_receivedCall var3 = new TLRPC.TL_phone_receivedCall();
         var3.peer = new TLRPC.TL_inputPhoneCall();
         TLRPC.TL_inputPhoneCall var4 = var3.peer;
         TLRPC.PhoneCall var2 = this.call;
         var4.id = var2.id;
         var4.access_hash = var2.access_hash;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new RequestDelegate() {
            public void run(final TLObject var1x, final TLRPC.TL_error var2) {
               AndroidUtilities.runOnUIThread(new Runnable() {
                  public void run() {
                     if (VoIPBaseService.sharedInstance != null) {
                        StringBuilder var1xx;
                        if (BuildVars.LOGS_ENABLED) {
                           var1xx = new StringBuilder();
                           var1xx.append("receivedCall response = ");
                           var1xx.append(var1x);
                           FileLog.w(var1xx.toString());
                        }

                        if (var2 != null) {
                           if (BuildVars.LOGS_ENABLED) {
                              var1xx = new StringBuilder();
                              var1xx.append("error on receivedCall: ");
                              var1xx.append(var2);
                              FileLog.e(var1xx.toString());
                           }

                           VoIPService.this.stopSelf();
                        } else {
                           if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                              ContactsController.getInstance(VoIPService.super.currentAccount).createOrUpdateConnectionServiceContact(VoIPService.this.user.id, VoIPService.this.user.first_name, VoIPService.this.user.last_name);
                              TelecomManager var3 = (TelecomManager)VoIPService.this.getSystemService("telecom");
                              Bundle var2x = new Bundle();
                              var2x.putInt("call_type", 1);
                              var3.addNewIncomingCall(VoIPService.this.addAccountToTelecomManager(), var2x);
                           }

                           <undefinedtype> var4 = <VAR_NAMELESS_ENCLOSURE>;
                           if (var1) {
                              VoIPService.this.startRinging();
                           }
                        }

                     }
                  }
               });
            }
         }, 2);
      }
   }

   private int convertDataSavingMode(int var1) {
      return var1 != 3 ? var1 : ApplicationLoader.isRoaming();
   }

   private void dumpCallObject() {
      // $FF: Couldn't be decompiled
   }

   private String[] getEmoji() {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      try {
         var1.write(this.authKey);
         var1.write(this.g_a);
      } catch (IOException var3) {
      }

      return EncryptionKeyEmojifier.emojifyForCall(Utilities.computeSHA256(var1.toByteArray(), 0, var1.size()));
   }

   public static VoIPService getSharedInstance() {
      VoIPBaseService var0 = VoIPBaseService.sharedInstance;
      VoIPService var1;
      if (var0 instanceof VoIPService) {
         var1 = (VoIPService)var0;
      } else {
         var1 = null;
      }

      return var1;
   }

   private void initiateActualEncryptedCall() {
      Runnable var1 = super.timeoutRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         super.timeoutRunnable = null;
      }

      Exception var10000;
      label136: {
         boolean var10001;
         StringBuilder var28;
         try {
            if (BuildVars.LOGS_ENABLED) {
               var28 = new StringBuilder();
               var28.append("InitCall: keyID=");
               var28.append(this.keyFingerprint);
               FileLog.d(var28.toString());
            }
         } catch (Exception var27) {
            var10000 = var27;
            var10001 = false;
            break label136;
         }

         SharedPreferences var2;
         HashSet var3;
         try {
            var2 = MessagesController.getNotificationsSettings(super.currentAccount);
            var3 = new HashSet(var2.getStringSet("calls_access_hashes", Collections.EMPTY_SET));
            var28 = new StringBuilder();
            var28.append(this.call.id);
            var28.append(" ");
            var28.append(this.call.access_hash);
            var28.append(" ");
            var28.append(System.currentTimeMillis());
            var3.add(var28.toString());
         } catch (Exception var23) {
            var10000 = var23;
            var10001 = false;
            break label136;
         }

         String var7;
         while(true) {
            Iterator var4;
            try {
               if (var3.size() <= 20) {
                  break;
               }

               var4 = var3.iterator();
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
               break label136;
            }

            long var5 = Long.MAX_VALUE;
            String var29 = null;

            label122:
            while(true) {
               String[] var8;
               while(true) {
                  try {
                     if (!var4.hasNext()) {
                        break label122;
                     }

                     var7 = (String)var4.next();
                     var8 = var7.split(" ");
                     if (var8.length >= 2) {
                        break;
                     }

                     var4.remove();
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label136;
                  }
               }

               long var9;
               try {
                  var9 = Long.parseLong(var8[2]);
               } catch (Exception var25) {
                  try {
                     var4.remove();
                     continue;
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label136;
                  }
               }

               if (var9 < var5) {
                  var29 = var7;
                  var5 = var9;
               }
            }

            if (var29 != null) {
               try {
                  var3.remove(var29);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label136;
               }
            }
         }

         int var11;
         VoIPController var31;
         SharedPreferences var34;
         try {
            var2.edit().putStringSet("calls_access_hashes", var3).commit();
            var34 = MessagesController.getGlobalMainSettings();
            var31 = super.controller;
            var11 = MessagesController.getInstance(super.currentAccount).callPacketTimeout;
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label136;
         }

         double var12 = (double)var11;
         Double.isNaN(var12);
         var12 /= 1000.0D;

         try {
            var11 = MessagesController.getInstance(super.currentAccount).callConnectTimeout;
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break label136;
         }

         double var14 = (double)var11;
         Double.isNaN(var14);
         var14 /= 1000.0D;

         SharedPreferences var32;
         try {
            var31.setConfig(var12, var14, this.convertDataSavingMode(var34.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault())), this.call.id);
            super.controller.setEncryptionKey(this.authKey, super.isOutgoing);
            TLRPC.TL_phoneConnection[] var35 = (TLRPC.TL_phoneConnection[])this.call.connections.toArray(new TLRPC.TL_phoneConnection[this.call.connections.size()]);
            var32 = MessagesController.getGlobalMainSettings();
            super.controller.setRemoteEndpoints(var35, this.call.p2p_allowed, var32.getBoolean("dbg_force_tcp_in_calls", false), this.call.protocol.max_layer);
            if (var32.getBoolean("dbg_force_tcp_in_calls", false)) {
               Runnable var36 = new Runnable() {
                  public void run() {
                     Toast.makeText(VoIPService.this, "This call uses TCP which will degrade its quality.", 0).show();
                  }
               };
               AndroidUtilities.runOnUIThread(var36);
            }
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label136;
         }

         try {
            if (var32.getBoolean("proxy_enabled", false) && var32.getBoolean("proxy_enabled_calls", false)) {
               var7 = var32.getString("proxy_ip", (String)null);
               String var30 = var32.getString("proxy_secret", (String)null);
               if (!TextUtils.isEmpty(var7) && TextUtils.isEmpty(var30)) {
                  super.controller.setProxy(var7, var32.getInt("proxy_port", 0), var32.getString("proxy_user", (String)null), var32.getString("proxy_pass", (String)null));
               }
            }
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label136;
         }

         try {
            super.controller.start();
            this.updateNetworkType();
            super.controller.connect();
            super.controllerStarted = true;
            var1 = new Runnable() {
               public void run() {
                  VoIPService var1 = VoIPService.this;
                  if (var1.controller != null) {
                     var1.updateStats();
                     AndroidUtilities.runOnUIThread(this, 5000L);
                  }
               }
            };
            AndroidUtilities.runOnUIThread(var1, 5000L);
            return;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
         }
      }

      Exception var33 = var10000;
      if (BuildVars.LOGS_ENABLED) {
         FileLog.e("error starting call", var33);
      }

      this.callFailed();
   }

   private void processAcceptedCall() {
      this.dispatchStateChanged(12);
      BigInteger var1 = new BigInteger(1, MessagesStorage.getInstance(super.currentAccount).getSecretPBytes());
      BigInteger var2 = new BigInteger(1, this.call.g_b);
      if (!Utilities.isGoodGaAndGb(var2, var1)) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.w("stopping VoIP service, bad Ga and Gb");
         }

         this.callFailed();
      } else {
         byte[] var8 = var2.modPow(new BigInteger(1, this.a_or_b), var1).toByteArray();
         byte[] var3;
         byte[] var7;
         if (var8.length > 256) {
            var7 = new byte[256];
            System.arraycopy(var8, var8.length - 256, var7, 0, 256);
         } else if (var8.length < 256) {
            var3 = new byte[256];
            System.arraycopy(var8, 0, var3, 256 - var8.length, var8.length);
            int var4 = 0;

            while(true) {
               var7 = var3;
               if (var4 >= 256 - var8.length) {
                  break;
               }

               var3[var4] = (byte)0;
               ++var4;
            }
         } else {
            var7 = var8;
         }

         var3 = Utilities.computeSHA1(var7);
         var8 = new byte[8];
         System.arraycopy(var3, var3.length - 8, var8, 0, 8);
         long var5 = Utilities.bytesToLong(var8);
         this.authKey = var7;
         this.keyFingerprint = var5;
         TLRPC.TL_phone_confirmCall var9 = new TLRPC.TL_phone_confirmCall();
         var9.g_a = this.g_a;
         var9.key_fingerprint = var5;
         var9.peer = new TLRPC.TL_inputPhoneCall();
         TLRPC.TL_inputPhoneCall var12 = var9.peer;
         TLRPC.PhoneCall var10 = this.call;
         var12.id = var10.id;
         var12.access_hash = var10.access_hash;
         var9.protocol = new TLRPC.TL_phoneCallProtocol();
         TLRPC.TL_phoneCallProtocol var11 = var9.protocol;
         var11.max_layer = CALL_MAX_LAYER;
         var11.min_layer = 65;
         var11.udp_reflector = true;
         var11.udp_p2p = true;
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var9, new RequestDelegate() {
            public void run(final TLObject var1, final TLRPC.TL_error var2) {
               AndroidUtilities.runOnUIThread(new Runnable() {
                  public void run() {
                     if (var2 != null) {
                        VoIPService.this.callFailed();
                     } else {
                        VoIPService.this.call = ((TLRPC.TL_phone_phoneCall)var1).phone_call;
                        VoIPService.this.initiateActualEncryptedCall();
                     }

                  }
               });
            }
         });
      }
   }

   private void startConnectingSound() {
      int var1 = super.spPlayID;
      if (var1 != 0) {
         super.soundPool.stop(var1);
      }

      super.spPlayID = super.soundPool.play(super.spConnectingId, 1.0F, 1.0F, 0, -1, 1.0F);
      if (super.spPlayID == 0) {
         Runnable var2 = new Runnable() {
            public void run() {
               if (VoIPBaseService.sharedInstance != null) {
                  VoIPService var1 = VoIPService.this;
                  if (var1.spPlayID == 0) {
                     var1.spPlayID = var1.soundPool.play(var1.spConnectingId, 1.0F, 1.0F, 0, -1, 1.0F);
                  }

                  var1 = VoIPService.this;
                  if (var1.spPlayID == 0) {
                     AndroidUtilities.runOnUIThread(this, 100L);
                  } else {
                     var1.connectingSoundRunnable = null;
                  }

               }
            }
         };
         super.connectingSoundRunnable = var2;
         AndroidUtilities.runOnUIThread(var2, 100L);
      }

   }

   private void startOutgoingCall() {
      if (VoIPBaseService.USE_CONNECTION_SERVICE) {
         VoIPBaseService.CallConnection var1 = super.systemCallConnection;
         if (var1 != null) {
            var1.setDialing();
         }
      }

      this.configureDeviceForCall();
      this.showNotification();
      this.startConnectingSound();
      this.dispatchStateChanged(14);
      AndroidUtilities.runOnUIThread(new Runnable() {
         public void run() {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall);
         }
      });
      byte[] var3 = new byte[256];
      Utilities.random.nextBytes(var3);
      TLRPC.TL_messages_getDhConfig var4 = new TLRPC.TL_messages_getDhConfig();
      var4.random_length = 256;
      final MessagesStorage var2 = MessagesStorage.getInstance(super.currentAccount);
      var4.version = var2.getLastSecretVersion();
      this.callReqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new RequestDelegate() {
         public void run(TLObject var1, TLRPC.TL_error var2x) {
            VoIPService.this.callReqId = 0;
            if (VoIPService.this.endCallAfterRequest) {
               VoIPService.this.callEnded();
            } else {
               if (var2x == null) {
                  TLRPC.messages_DhConfig var8 = (TLRPC.messages_DhConfig)var1;
                  if (var1 instanceof TLRPC.TL_messages_dhConfig) {
                     if (!Utilities.isGoodPrime(var8.p, var8.g)) {
                        VoIPService.this.callFailed();
                        return;
                     }

                     var2.setSecretPBytes(var8.p);
                     var2.setSecretG(var8.g);
                     var2.setLastSecretVersion(var8.version);
                     MessagesStorage var6 = var2;
                     var6.saveSecretParams(var6.getLastSecretVersion(), var2.getSecretG(), var2.getSecretPBytes());
                  }

                  final byte[] var3 = new byte[256];

                  for(int var4 = 0; var4 < 256; ++var4) {
                     var3[var4] = (byte)((byte)((byte)((int)(Utilities.random.nextDouble() * 256.0D)) ^ var8.random[var4]));
                  }

                  byte[] var10 = BigInteger.valueOf((long)var2.getSecretG()).modPow(new BigInteger(1, var3), new BigInteger(1, var2.getSecretPBytes())).toByteArray();
                  byte[] var7 = var10;
                  if (var10.length > 256) {
                     var7 = new byte[256];
                     System.arraycopy(var10, 1, var7, 0, 256);
                  }

                  TLRPC.TL_phone_requestCall var11 = new TLRPC.TL_phone_requestCall();
                  var11.user_id = MessagesController.getInstance(VoIPService.super.currentAccount).getInputUser(VoIPService.this.user);
                  var11.protocol = new TLRPC.TL_phoneCallProtocol();
                  TLRPC.TL_phoneCallProtocol var5 = var11.protocol;
                  var5.udp_p2p = true;
                  var5.udp_reflector = true;
                  var5.min_layer = 65;
                  var5.max_layer = VoIPService.CALL_MAX_LAYER;
                  VoIPService.this.g_a = var7;
                  var11.g_a_hash = Utilities.computeSHA256(var7, 0, var7.length);
                  var11.random_id = Utilities.random.nextInt();
                  ConnectionsManager.getInstance(VoIPService.super.currentAccount).sendRequest(var11, new RequestDelegate() {
                     public void run(final TLObject var1, final TLRPC.TL_error var2x) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                           public void run() {
                              TLRPC.TL_error var1x = var2x;
                              if (var1x == null) {
                                 VoIPService.this.call = ((TLRPC.TL_phone_phoneCall)var1).phone_call;
                                 <undefinedtype> var4 = <VAR_NAMELESS_ENCLOSURE>;
                                 VoIPService.this.a_or_b = var3;
                                 VoIPService.this.dispatchStateChanged(13);
                                 if (VoIPService.this.endCallAfterRequest) {
                                    VoIPService.this.hangUp();
                                    return;
                                 }

                                 if (VoIPService.this.pendingUpdates.size() > 0 && VoIPService.this.call != null) {
                                    Iterator var2xx = VoIPService.this.pendingUpdates.iterator();

                                    while(var2xx.hasNext()) {
                                       TLRPC.PhoneCall var5 = (TLRPC.PhoneCall)var2xx.next();
                                       VoIPService.this.onCallUpdated(var5);
                                    }

                                    VoIPService.this.pendingUpdates.clear();
                                 }

                                 VoIPService.super.timeoutRunnable = new Runnable() {
                                    public void run() {
                                       VoIPService.super.timeoutRunnable = null;
                                       TLRPC.TL_phone_discardCall var1x = new TLRPC.TL_phone_discardCall();
                                       var1x.peer = new TLRPC.TL_inputPhoneCall();
                                       var1x.peer.access_hash = VoIPService.this.call.access_hash;
                                       var1x.peer.id = VoIPService.this.call.id;
                                       var1x.reason = new TLRPC.TL_phoneCallDiscardReasonMissed();
                                       ConnectionsManager.getInstance(VoIPService.super.currentAccount).sendRequest(var1x, new RequestDelegate() {
                                          public void run(TLObject var1x, TLRPC.TL_error var2xx) {
                                             if (BuildVars.LOGS_ENABLED) {
                                                if (var2xx != null) {
                                                   StringBuilder var3x = new StringBuilder();
                                                   var3x.append("error on phone.discardCall: ");
                                                   var3x.append(var2xx);
                                                   FileLog.e(var3x.toString());
                                                } else {
                                                   StringBuilder var4 = new StringBuilder();
                                                   var4.append("phone.discardCall ");
                                                   var4.append(var1x);
                                                   FileLog.d(var4.toString());
                                                }
                                             }

                                             AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                   VoIPService.this.callFailed();
                                                }
                                             });
                                          }
                                       }, 2);
                                    }
                                 };
                                 VoIPService var6 = VoIPService.this;
                                 AndroidUtilities.runOnUIThread(var6.timeoutRunnable, (long)MessagesController.getInstance(var6.currentAccount).callReceiveTimeout);
                              } else if (var1x.code == 400 && "PARTICIPANT_VERSION_OUTDATED".equals(var1x.text)) {
                                 VoIPService.this.callFailed(-1);
                              } else {
                                 int var3x = var2x.code;
                                 if (var3x == 403) {
                                    VoIPService.this.callFailed(-2);
                                 } else if (var3x == 406) {
                                    VoIPService.this.callFailed(-3);
                                 } else {
                                    if (BuildVars.LOGS_ENABLED) {
                                       StringBuilder var7 = new StringBuilder();
                                       var7.append("Error on phone.requestCall: ");
                                       var7.append(var2x);
                                       FileLog.e(var7.toString());
                                    }

                                    VoIPService.this.callFailed();
                                 }
                              }

                           }
                        });
                     }
                  }, 2);
               } else {
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("Error on getDhConfig ");
                     var9.append(var2x);
                     FileLog.e(var9.toString());
                  }

                  VoIPService.this.callFailed();
               }

            }
         }
      }, 2);
   }

   private void startRatingActivity() {
      try {
         Intent var1 = new Intent(this, VoIPFeedbackActivity.class);
         PendingIntent.getActivity(this, 0, var1.putExtra("call_id", this.call.id).putExtra("call_access_hash", this.call.access_hash).putExtra("account", super.currentAccount).addFlags(805306368), 0).send();
      } catch (Exception var2) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Error starting incall activity", var2);
         }
      }

   }

   public void acceptIncomingCall() {
      this.stopRinging();
      this.showNotification();
      this.configureDeviceForCall();
      this.startConnectingSound();
      this.dispatchStateChanged(12);
      AndroidUtilities.runOnUIThread(new Runnable() {
         public void run() {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall);
         }
      });
      final MessagesStorage var1 = MessagesStorage.getInstance(super.currentAccount);
      TLRPC.TL_messages_getDhConfig var2 = new TLRPC.TL_messages_getDhConfig();
      var2.random_length = 256;
      var2.version = var1.getLastSecretVersion();
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new RequestDelegate() {
         public void run(TLObject var1x, TLRPC.TL_error var2) {
            if (var2 == null) {
               TLRPC.messages_DhConfig var7 = (TLRPC.messages_DhConfig)var1x;
               if (var1x instanceof TLRPC.TL_messages_dhConfig) {
                  if (!Utilities.isGoodPrime(var7.p, var7.g)) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("stopping VoIP service, bad prime");
                     }

                     VoIPService.this.callFailed();
                     return;
                  }

                  var1.setSecretPBytes(var7.p);
                  var1.setSecretG(var7.g);
                  var1.setLastSecretVersion(var7.version);
                  MessagesStorage.getInstance(VoIPService.super.currentAccount).saveSecretParams(var1.getLastSecretVersion(), var1.getSecretG(), var1.getSecretPBytes());
               }

               byte[] var5 = new byte[256];

               for(int var3 = 0; var3 < 256; ++var3) {
                  var5[var3] = (byte)((byte)((byte)((int)(Utilities.random.nextDouble() * 256.0D)) ^ var7.random[var3]));
               }

               if (VoIPService.this.call == null) {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("call is null");
                  }

                  VoIPService.this.callFailed();
                  return;
               }

               VoIPService.this.a_or_b = var5;
               BigInteger var4 = BigInteger.valueOf((long)var1.getSecretG());
               BigInteger var8 = new BigInteger(1, var1.getSecretPBytes());
               var8 = var4.modPow(new BigInteger(1, var5), var8);
               VoIPService var6 = VoIPService.this;
               var6.g_a_hash = var6.call.g_a_hash;
               byte[] var10 = var8.toByteArray();
               var5 = var10;
               if (var10.length > 256) {
                  var5 = new byte[256];
                  System.arraycopy(var10, 1, var5, 0, 256);
               }

               TLRPC.TL_phone_acceptCall var11 = new TLRPC.TL_phone_acceptCall();
               var11.g_b = var5;
               var11.peer = new TLRPC.TL_inputPhoneCall();
               var11.peer.id = VoIPService.this.call.id;
               var11.peer.access_hash = VoIPService.this.call.access_hash;
               var11.protocol = new TLRPC.TL_phoneCallProtocol();
               TLRPC.TL_phoneCallProtocol var9 = var11.protocol;
               var9.udp_reflector = true;
               var9.udp_p2p = true;
               var9.min_layer = 65;
               var9.max_layer = VoIPService.CALL_MAX_LAYER;
               ConnectionsManager.getInstance(VoIPService.super.currentAccount).sendRequest(var11, new RequestDelegate() {
                  public void run(final TLObject var1x, final TLRPC.TL_error var2) {
                     AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                           StringBuilder var1xx;
                           if (var2 == null) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var1xx = new StringBuilder();
                                 var1xx.append("accept call ok! ");
                                 var1xx.append(var1x);
                                 FileLog.w(var1xx.toString());
                              }

                              VoIPService.this.call = ((TLRPC.TL_phone_phoneCall)var1x).phone_call;
                              if (VoIPService.this.call instanceof TLRPC.TL_phoneCallDiscarded) {
                                 VoIPService var2x = VoIPService.this;
                                 var2x.onCallUpdated(var2x.call);
                              }
                           } else {
                              if (BuildVars.LOGS_ENABLED) {
                                 var1xx = new StringBuilder();
                                 var1xx.append("Error on phone.acceptCall: ");
                                 var1xx.append(var2);
                                 FileLog.e(var1xx.toString());
                              }

                              VoIPService.this.callFailed();
                           }

                        }
                     });
                  }
               }, 2);
            } else {
               VoIPService.this.callFailed();
            }

         }
      });
   }

   protected void callFailed(int var1) {
      if (this.call != null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Discarding failed call");
         }

         TLRPC.TL_phone_discardCall var2 = new TLRPC.TL_phone_discardCall();
         var2.peer = new TLRPC.TL_inputPhoneCall();
         TLRPC.TL_inputPhoneCall var3 = var2.peer;
         TLRPC.PhoneCall var4 = this.call;
         var3.access_hash = var4.access_hash;
         var3.id = var4.id;
         VoIPController var8 = super.controller;
         int var5;
         if (var8 != null && super.controllerStarted) {
            var5 = (int)(var8.getCallDuration() / 1000L);
         } else {
            var5 = 0;
         }

         var2.duration = var5;
         var8 = super.controller;
         long var6;
         if (var8 != null && super.controllerStarted) {
            var6 = var8.getPreferredRelayID();
         } else {
            var6 = 0L;
         }

         var2.connection_id = var6;
         var2.reason = new TLRPC.TL_phoneCallDiscardReasonDisconnect();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new RequestDelegate() {
            public void run(TLObject var1, TLRPC.TL_error var2) {
               if (var2 != null) {
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var3 = new StringBuilder();
                     var3.append("error on phone.discardCall: ");
                     var3.append(var2);
                     FileLog.e(var3.toString());
                  }
               } else if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("phone.discardCall ");
                  var4.append(var1);
                  FileLog.d(var4.toString());
               }

            }
         });
      }

      super.callFailed(var1);
   }

   public boolean canUpgrate() {
      int var1 = this.peerCapabilities;
      boolean var2 = true;
      if ((var1 & 1) != 1) {
         var2 = false;
      }

      return var2;
   }

   public void debugCtl(int var1, int var2) {
      VoIPController var3 = super.controller;
      if (var3 != null) {
         var3.debugCtl(var1, var2);
      }

   }

   public void declineIncomingCall() {
      this.declineIncomingCall(1, (Runnable)null);
   }

   public void declineIncomingCall(int var1, final Runnable var2) {
      this.stopRinging();
      super.callDiscardReason = var1;
      int var3 = super.currentState;
      final boolean var4 = true;
      if (var3 == 14) {
         var2 = this.delayedStartOutgoingCall;
         if (var2 != null) {
            AndroidUtilities.cancelRunOnUIThread(var2);
            this.callEnded();
         } else {
            this.dispatchStateChanged(10);
            this.endCallAfterRequest = true;
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  VoIPService var1 = VoIPService.this;
                  if (var1.currentState == 10) {
                     var1.callEnded();
                  }

               }
            }, 5000L);
         }

      } else {
         if (var3 != 10 && var3 != 11) {
            this.dispatchStateChanged(10);
            if (this.call == null) {
               if (var2 != null) {
                  var2.run();
               }

               this.callEnded();
               if (this.callReqId != 0) {
                  ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.callReqId, false);
                  this.callReqId = 0;
               }

               return;
            }

            TLRPC.TL_phone_discardCall var5 = new TLRPC.TL_phone_discardCall();
            var5.peer = new TLRPC.TL_inputPhoneCall();
            TLRPC.TL_inputPhoneCall var6 = var5.peer;
            TLRPC.PhoneCall var7 = this.call;
            var6.access_hash = var7.access_hash;
            var6.id = var7.id;
            VoIPController var10 = super.controller;
            if (var10 != null && super.controllerStarted) {
               var3 = (int)(var10.getCallDuration() / 1000L);
            } else {
               var3 = 0;
            }

            var5.duration = var3;
            var10 = super.controller;
            long var8;
            if (var10 != null && super.controllerStarted) {
               var8 = var10.getPreferredRelayID();
            } else {
               var8 = 0L;
            }

            var5.connection_id = var8;
            if (var1 != 2) {
               if (var1 != 3) {
                  if (var1 != 4) {
                     var5.reason = new TLRPC.TL_phoneCallDiscardReasonHangup();
                  } else {
                     var5.reason = new TLRPC.TL_phoneCallDiscardReasonBusy();
                  }
               } else {
                  var5.reason = new TLRPC.TL_phoneCallDiscardReasonMissed();
               }
            } else {
               var5.reason = new TLRPC.TL_phoneCallDiscardReasonDisconnect();
            }

            if (ConnectionsManager.getInstance(super.currentAccount).getConnectionState() == 3) {
               var4 = false;
            }

            final Runnable var11;
            if (var4) {
               if (var2 != null) {
                  var2.run();
               }

               this.callEnded();
               var11 = null;
            } else {
               var11 = new Runnable() {
                  private boolean done = false;

                  public void run() {
                     if (!this.done) {
                        this.done = true;
                        Runnable var1 = var2;
                        if (var1 != null) {
                           var1.run();
                        }

                        VoIPService.this.callEnded();
                     }
                  }
               };
               AndroidUtilities.runOnUIThread(var11, (long)((int)(VoIPServerConfig.getDouble("hangup_ui_timeout", 5.0D) * 1000.0D)));
            }

            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var5, new RequestDelegate() {
               public void run(TLObject var1, TLRPC.TL_error var2x) {
                  if (var2x != null) {
                     if (BuildVars.LOGS_ENABLED) {
                        StringBuilder var3 = new StringBuilder();
                        var3.append("error on phone.discardCall: ");
                        var3.append(var2x);
                        FileLog.e(var3.toString());
                     }
                  } else {
                     if (var1 instanceof TLRPC.TL_updates) {
                        TLRPC.TL_updates var5 = (TLRPC.TL_updates)var1;
                        MessagesController.getInstance(VoIPService.super.currentAccount).processUpdates(var5, false);
                     }

                     if (BuildVars.LOGS_ENABLED) {
                        StringBuilder var6 = new StringBuilder();
                        var6.append("phone.discardCall ");
                        var6.append(var1);
                        FileLog.d(var6.toString());
                     }
                  }

                  if (!var4) {
                     AndroidUtilities.cancelRunOnUIThread(var11);
                     Runnable var4x = var2;
                     if (var4x != null) {
                        var4x.run();
                     }
                  }

               }
            }, 2);
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.appDidLogout) {
         this.callEnded();
      }

   }

   public void forceRating() {
      this.forceRating = true;
   }

   public long getCallID() {
      TLRPC.PhoneCall var1 = this.call;
      long var2;
      if (var1 != null) {
         var2 = var1.id;
      } else {
         var2 = 0L;
      }

      return var2;
   }

   @TargetApi(26)
   public VoIPBaseService.CallConnection getConnectionAndStartCall() {
      if (super.systemCallConnection == null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("creating call connection");
         }

         super.systemCallConnection = new VoIPBaseService.CallConnection();
         super.systemCallConnection.setInitializing();
         if (super.isOutgoing) {
            this.delayedStartOutgoingCall = new Runnable() {
               public void run() {
                  VoIPService.this.delayedStartOutgoingCall = null;
                  VoIPService.this.startOutgoingCall();
               }
            };
            AndroidUtilities.runOnUIThread(this.delayedStartOutgoingCall, 2000L);
         }

         VoIPBaseService.CallConnection var1 = super.systemCallConnection;
         StringBuilder var2 = new StringBuilder();
         var2.append("+99084");
         var2.append(this.user.id);
         var1.setAddress(Uri.fromParts("tel", var2.toString(), (String)null), 1);
         var1 = super.systemCallConnection;
         TLRPC.User var3 = this.user;
         var1.setCallerDisplayName(ContactsController.formatName(var3.first_name, var3.last_name), 1);
      }

      return super.systemCallConnection;
   }

   public byte[] getEncryptionKey() {
      return this.authKey;
   }

   public byte[] getGA() {
      return this.g_a;
   }

   protected Class getUIActivityClass() {
      return VoIPActivity.class;
   }

   public TLRPC.User getUser() {
      return this.user;
   }

   public void hangUp() {
      int var1 = super.currentState;
      byte var2;
      if (var1 == 16 || var1 == 13 && super.isOutgoing) {
         var2 = 3;
      } else {
         var2 = 1;
      }

      this.declineIncomingCall(var2, (Runnable)null);
   }

   public void hangUp(Runnable var1) {
      int var2 = super.currentState;
      byte var3;
      if (var2 == 16 || var2 == 13 && super.isOutgoing) {
         var3 = 3;
      } else {
         var3 = 1;
      }

      this.declineIncomingCall(var3, var1);
   }

   protected boolean isRinging() {
      boolean var1;
      if (super.currentState == 15) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onCallUpdated(TLRPC.PhoneCall var1) {
      TLRPC.PhoneCall var2 = this.call;
      if (var2 == null) {
         this.pendingUpdates.add(var1);
      } else if (var1 != null) {
         StringBuilder var7;
         if (var1.id != var2.id) {
            if (BuildVars.LOGS_ENABLED) {
               var7 = new StringBuilder();
               var7.append("onCallUpdated called with wrong call id (got ");
               var7.append(var1.id);
               var7.append(", expected ");
               var7.append(this.call.id);
               var7.append(")");
               FileLog.w(var7.toString());
            }

         } else {
            if (var1.access_hash == 0L) {
               var1.access_hash = var2.access_hash;
            }

            if (BuildVars.LOGS_ENABLED) {
               var7 = new StringBuilder();
               var7.append("Call updated: ");
               var7.append(var1);
               FileLog.d(var7.toString());
               this.dumpCallObject();
            }

            this.call = var1;
            if (var1 instanceof TLRPC.TL_phoneCallDiscarded) {
               this.needSendDebugLog = var1.need_debug;
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("call discarded, stopping service");
               }

               if (var1.reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) {
                  this.dispatchStateChanged(17);
                  super.playingSound = true;
                  super.soundPool.play(super.spBusyId, 1.0F, 1.0F, 0, -1, 1.0F);
                  AndroidUtilities.runOnUIThread(super.afterSoundRunnable, 1500L);
                  this.endConnectionServiceCall(1500L);
                  this.stopSelf();
               } else {
                  this.callEnded();
               }

               if (var1.need_rating || this.forceRating || super.controller != null && VoIPServerConfig.getBoolean("bad_call_rating", true) && super.controller.needRate()) {
                  this.startRatingActivity();
               }

               if (this.debugLog == null) {
                  VoIPController var8 = super.controller;
                  if (var8 != null) {
                     this.debugLog = var8.getDebugLog();
                  }
               }

               if (this.needSendDebugLog && this.debugLog != null) {
                  TLRPC.TL_phone_saveCallDebug var3 = new TLRPC.TL_phone_saveCallDebug();
                  var3.debug = new TLRPC.TL_dataJSON();
                  var3.debug.data = this.debugLog;
                  var3.peer = new TLRPC.TL_inputPhoneCall();
                  TLRPC.TL_inputPhoneCall var9 = var3.peer;
                  var9.access_hash = var1.access_hash;
                  var9.id = var1.id;
                  ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new RequestDelegate() {
                     public void run(TLObject var1, TLRPC.TL_error var2) {
                        if (BuildVars.LOGS_ENABLED) {
                           StringBuilder var3 = new StringBuilder();
                           var3.append("Sent debug logs, response=");
                           var3.append(var1);
                           FileLog.d(var3.toString());
                        }

                     }
                  });
               }
            } else {
               int var5;
               if (var1 instanceof TLRPC.TL_phoneCall && this.authKey == null) {
                  byte[] var10 = var1.g_a_or_b;
                  if (var10 == null) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.w("stopping VoIP service, Ga == null");
                     }

                     this.callFailed();
                     return;
                  }

                  if (!Arrays.equals(this.g_a_hash, Utilities.computeSHA256(var10, 0, var10.length))) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.w("stopping VoIP service, Ga hash doesn't match");
                     }

                     this.callFailed();
                     return;
                  }

                  var10 = var1.g_a_or_b;
                  this.g_a = var10;
                  BigInteger var13 = new BigInteger(1, var10);
                  BigInteger var11 = new BigInteger(1, MessagesStorage.getInstance(super.currentAccount).getSecretPBytes());
                  if (!Utilities.isGoodGaAndGb(var13, var11)) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.w("stopping VoIP service, bad Ga and Gb (accepting)");
                     }

                     this.callFailed();
                     return;
                  }

                  byte[] var12 = var13.modPow(new BigInteger(1, this.a_or_b), var11).toByteArray();
                  byte[] var4;
                  if (var12.length > 256) {
                     var10 = new byte[256];
                     System.arraycopy(var12, var12.length - 256, var10, 0, 256);
                  } else if (var12.length < 256) {
                     var4 = new byte[256];
                     System.arraycopy(var12, 0, var4, 256 - var12.length, var12.length);
                     var5 = 0;

                     while(true) {
                        var10 = var4;
                        if (var5 >= 256 - var12.length) {
                           break;
                        }

                        var4[var5] = (byte)0;
                        ++var5;
                     }
                  } else {
                     var10 = var12;
                  }

                  var4 = Utilities.computeSHA1(var10);
                  var12 = new byte[8];
                  System.arraycopy(var4, var4.length - 8, var12, 0, 8);
                  this.authKey = var10;
                  this.keyFingerprint = Utilities.bytesToLong(var12);
                  if (this.keyFingerprint != var1.key_fingerprint) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.w("key fingerprints don't match");
                     }

                     this.callFailed();
                     return;
                  }

                  this.initiateActualEncryptedCall();
               } else if (var1 instanceof TLRPC.TL_phoneCallAccepted && this.authKey == null) {
                  this.processAcceptedCall();
               } else if (super.currentState == 13 && var1.receive_date != 0) {
                  this.dispatchStateChanged(16);
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("!!!!!! CALL RECEIVED");
                  }

                  Runnable var6 = super.connectingSoundRunnable;
                  if (var6 != null) {
                     AndroidUtilities.cancelRunOnUIThread(var6);
                     super.connectingSoundRunnable = null;
                  }

                  var5 = super.spPlayID;
                  if (var5 != 0) {
                     super.soundPool.stop(var5);
                  }

                  super.spPlayID = super.soundPool.play(super.spRingbackID, 1.0F, 1.0F, 0, -1, 1.0F);
                  var6 = super.timeoutRunnable;
                  if (var6 != null) {
                     AndroidUtilities.cancelRunOnUIThread(var6);
                     super.timeoutRunnable = null;
                  }

                  super.timeoutRunnable = new Runnable() {
                     public void run() {
                        VoIPService var1 = VoIPService.this;
                        var1.timeoutRunnable = null;
                        var1.declineIncomingCall(3, (Runnable)null);
                     }
                  };
                  AndroidUtilities.runOnUIThread(super.timeoutRunnable, (long)MessagesController.getInstance(super.currentAccount).callRingTimeout);
               }
            }

         }
      }
   }

   public void onCallUpgradeRequestReceived() {
      this.upgradeToGroupCall(new ArrayList());
   }

   public void onConnectionStateChanged(int var1) {
      if (var1 == 3) {
         this.peerCapabilities = super.controller.getPeerCapabilities();
      }

      super.onConnectionStateChanged(var1);
   }

   protected void onControllerPreRelease() {
      if (this.debugLog == null) {
         this.debugLog = super.controller.getDebugLog();
      }

   }

   public void onCreate() {
      super.onCreate();
      if (callIShouldHavePutIntoIntent != null && VERSION.SDK_INT >= 26) {
         NotificationsController.checkOtherNotificationsChannel();
         this.startForeground(201, (new Builder(this, NotificationsController.OTHER_NOTIFICATIONS_CHANNEL)).setSmallIcon(2131165698).setContentTitle(LocaleController.getString("VoipOutgoingCall", 2131561083)).setShowWhen(false).build());
      }

   }

   public void onGroupCallKeyReceived(byte[] var1) {
      this.joiningGroupCall = true;
      this.groupCallEncryptionKey = var1;
      byte[] var2 = Utilities.computeSHA1(this.groupCallEncryptionKey);
      var1 = new byte[8];
      System.arraycopy(var2, var2.length - 8, var1, 0, 8);
      this.groupCallKeyFingerprint = Utilities.bytesToLong(var1);
   }

   public void onGroupCallKeySent() {
      boolean var1 = super.isOutgoing;
   }

   void onMediaButtonEvent(KeyEvent var1) {
      if (var1.getKeyCode() == 79 || var1.getKeyCode() == 127 || var1.getKeyCode() == 85) {
         int var2 = var1.getAction();
         boolean var3 = true;
         if (var2 == 1) {
            if (super.currentState == 15) {
               this.acceptIncomingCall();
            } else {
               if (this.isMicMute()) {
                  var3 = false;
               }

               this.setMicMute(var3);
               Iterator var4 = super.stateListeners.iterator();

               while(var4.hasNext()) {
                  ((VoIPBaseService.StateListener)var4.next()).onAudioSettingsChanged();
               }
            }
         }
      }

   }

   @SuppressLint({"MissingPermission"})
   public int onStartCommand(Intent var1, int var2, int var3) {
      if (VoIPBaseService.sharedInstance != null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Tried to start the VoIP service when it's already started");
         }

         return 2;
      } else {
         super.currentAccount = var1.getIntExtra("account", -1);
         if (super.currentAccount != -1) {
            var2 = var1.getIntExtra("user_id", 0);
            super.isOutgoing = var1.getBooleanExtra("is_outgoing", false);
            this.user = MessagesController.getInstance(super.currentAccount).getUser(var2);
            if (this.user == null) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.w("VoIPService: user==null");
               }

               this.stopSelf();
               return 2;
            } else {
               VoIPBaseService.sharedInstance = this;
               if (super.isOutgoing) {
                  this.dispatchStateChanged(14);
                  if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                     TelecomManager var4 = (TelecomManager)this.getSystemService("telecom");
                     Bundle var5 = new Bundle();
                     Bundle var6 = new Bundle();
                     var5.putParcelable("android.telecom.extra.PHONE_ACCOUNT_HANDLE", this.addAccountToTelecomManager());
                     var6.putInt("call_type", 1);
                     var5.putBundle("android.telecom.extra.OUTGOING_CALL_EXTRAS", var6);
                     ContactsController var8 = ContactsController.getInstance(super.currentAccount);
                     TLRPC.User var7 = this.user;
                     var8.createOrUpdateConnectionServiceContact(var7.id, var7.first_name, var7.last_name);
                     StringBuilder var9 = new StringBuilder();
                     var9.append("+99084");
                     var9.append(this.user.id);
                     var4.placeCall(Uri.fromParts("tel", var9.toString(), (String)null), var5);
                  } else {
                     this.delayedStartOutgoingCall = new Runnable() {
                        public void run() {
                           VoIPService.this.delayedStartOutgoingCall = null;
                           VoIPService.this.startOutgoingCall();
                        }
                     };
                     AndroidUtilities.runOnUIThread(this.delayedStartOutgoingCall, 2000L);
                  }

                  if (var1.getBooleanExtra("start_incall_activity", false)) {
                     this.startActivity((new Intent(this, VoIPActivity.class)).addFlags(268435456));
                  }
               } else {
                  NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeInCallActivity);
                  this.call = callIShouldHavePutIntoIntent;
                  callIShouldHavePutIntoIntent = null;
                  if (VoIPBaseService.USE_CONNECTION_SERVICE) {
                     this.acknowledgeCall(false);
                     this.showNotification();
                  } else {
                     this.acknowledgeCall(true);
                  }
               }

               this.initializeAccountRelatedThings();
               return 2;
            }
         } else {
            throw new IllegalStateException("No account specified when starting VoIP service");
         }
      }
   }

   public void onUIForegroundStateChanged(boolean var1) {
      if (VERSION.SDK_INT < 21) {
         if (super.currentState == 15) {
            if (var1) {
               this.stopForeground(true);
            } else if (!((KeyguardManager)this.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
               if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                  TLRPC.User var2 = this.user;
                  this.showIncomingNotification(ContactsController.formatName(var2.first_name, var2.last_name), (CharSequence)null, this.user, (List)null, 0, VoIPActivity.class);
               } else {
                  this.declineIncomingCall(4, (Runnable)null);
               }
            } else {
               AndroidUtilities.runOnUIThread(new Runnable() {
                  public void run() {
                     Intent var1 = new Intent(VoIPService.this, VoIPActivity.class);
                     var1.addFlags(805306368);

                     try {
                        PendingIntent.getActivity(VoIPService.this, 0, var1, 0).send();
                     } catch (CanceledException var2) {
                        if (BuildVars.LOGS_ENABLED) {
                           FileLog.e("error restarting activity", var2);
                        }

                        VoIPService.this.declineIncomingCall(4, (Runnable)null);
                     }

                     if (VERSION.SDK_INT >= 26) {
                        VoIPService.this.showNotification();
                     }

                  }
               }, 500L);
            }
         }

      }
   }

   protected void showNotification() {
      TLRPC.User var1 = this.user;
      String var2 = ContactsController.formatName(var1.first_name, var1.last_name);
      TLRPC.UserProfilePhoto var3 = this.user.photo;
      TLRPC.FileLocation var4;
      if (var3 != null) {
         var4 = var3.photo_small;
      } else {
         var4 = null;
      }

      this.showNotification(var2, var4, VoIPActivity.class);
   }

   protected void startRinging() {
      if (super.currentState != 15) {
         if (VoIPBaseService.USE_CONNECTION_SERVICE) {
            VoIPBaseService.CallConnection var1 = super.systemCallConnection;
            if (var1 != null) {
               var1.setRinging();
            }
         }

         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var3 = new StringBuilder();
            var3.append("starting ringing for call ");
            var3.append(this.call.id);
            FileLog.d(var3.toString());
         }

         this.dispatchStateChanged(15);
         if (VERSION.SDK_INT >= 21) {
            TLRPC.User var4 = this.user;
            this.showIncomingNotification(ContactsController.formatName(var4.first_name, var4.last_name), (CharSequence)null, this.user, (List)null, 0, VoIPActivity.class);
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("Showing incoming call notification");
            }
         } else {
            this.startRingtoneAndVibration(this.user.id);
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("Starting incall activity for incoming call");
            }

            try {
               Intent var5 = new Intent(this, VoIPActivity.class);
               PendingIntent.getActivity(this, 12345, var5.addFlags(268435456), 0).send();
            } catch (Exception var2) {
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.e("Error starting incall activity", var2);
               }
            }
         }

      }
   }

   public void startRingtoneAndVibration() {
      if (!this.startedRinging) {
         this.startRingtoneAndVibration(this.user.id);
         this.startedRinging = true;
      }

   }

   protected void updateServerConfig() {
      final SharedPreferences var1 = MessagesController.getMainSettings(super.currentAccount);
      VoIPServerConfig.setConfig(var1.getString("voip_server_config", "{}"));
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_phone_getCallConfig(), new RequestDelegate() {
         public void run(TLObject var1x, TLRPC.TL_error var2) {
            if (var2 == null) {
               String var3 = ((TLRPC.TL_dataJSON)var1x).data;
               VoIPServerConfig.setConfig(var3);
               var1.edit().putString("voip_server_config", var3).commit();
            }

         }
      });
   }

   public void upgradeToGroupCall(List var1) {
      if (!this.upgrading) {
         this.groupUsersToAdd = var1;
         if (!super.isOutgoing) {
            super.controller.requestCallUpgrade();
         } else {
            this.upgrading = true;
            this.groupCallEncryptionKey = new byte[256];
            Utilities.random.nextBytes(this.groupCallEncryptionKey);
            byte[] var3 = this.groupCallEncryptionKey;
            var3[0] = (byte)((byte)(var3[0] & 127));
            var3 = Utilities.computeSHA1(var3);
            byte[] var2 = new byte[8];
            System.arraycopy(var3, var3.length - 8, var2, 0, 8);
            this.groupCallKeyFingerprint = Utilities.bytesToLong(var2);
            super.controller.sendGroupCallKey(this.groupCallEncryptionKey);
         }
      }
   }
}
