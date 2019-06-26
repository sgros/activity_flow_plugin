package org.telegram.tgnet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.io.File;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.KeepAliveJob;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;

public class ConnectionsManager {
   public static final int ConnectionStateConnected = 3;
   public static final int ConnectionStateConnecting = 1;
   public static final int ConnectionStateConnectingToProxy = 4;
   public static final int ConnectionStateUpdating = 5;
   public static final int ConnectionStateWaitingForNetwork = 2;
   public static final int ConnectionTypeDownload = 2;
   public static final int ConnectionTypeDownload2 = 65538;
   public static final int ConnectionTypeGeneric = 1;
   public static final int ConnectionTypePush = 8;
   public static final int ConnectionTypeUpload = 4;
   public static final int DEFAULT_DATACENTER_ID = Integer.MAX_VALUE;
   public static final int FileTypeAudio = 50331648;
   public static final int FileTypeFile = 67108864;
   public static final int FileTypePhoto = 16777216;
   public static final int FileTypeVideo = 33554432;
   private static volatile ConnectionsManager[] Instance = new ConnectionsManager[3];
   public static final int RequestFlagCanCompress = 4;
   public static final int RequestFlagEnableUnauthorized = 1;
   public static final int RequestFlagFailOnServerErrors = 2;
   public static final int RequestFlagForceDownload = 32;
   public static final int RequestFlagInvokeAfter = 64;
   public static final int RequestFlagNeedQuickAck = 128;
   public static final int RequestFlagTryDifferentDc = 16;
   public static final int RequestFlagWithoutLogin = 8;
   private static AsyncTask currentTask;
   private static ConcurrentHashMap dnsCache = new ConcurrentHashMap();
   private static int lastClassGuid = 1;
   private static long lastDnsRequestTime;
   private static HashMap resolvingHostnameTasks = new HashMap();
   private boolean appPaused = true;
   private int appResumeCount;
   private int connectionState;
   private int currentAccount;
   private boolean isUpdating;
   private long lastPauseTime = System.currentTimeMillis();
   private AtomicInteger lastRequestToken = new AtomicInteger(1);

   public ConnectionsManager(int var1) {
      this.currentAccount = var1;
      this.connectionState = native_getConnectionState(this.currentAccount);
      File var2 = ApplicationLoader.getFilesDirFixed();
      File var3 = var2;
      StringBuilder var12;
      if (var1 != 0) {
         var12 = new StringBuilder();
         var12.append("account");
         var12.append(var1);
         var3 = new File(var2, var12.toString());
         var3.mkdirs();
      }

      String var4 = var3.toString();
      boolean var5 = MessagesController.getGlobalNotificationsSettings().getBoolean("pushConnection", true);

      String var6;
      String var7;
      String var8;
      String var10;
      String var13;
      try {
         var6 = LocaleController.getSystemLocaleStringIso639().toLowerCase();
         var7 = LocaleController.getLocaleStringIso639().toLowerCase();
         var12 = new StringBuilder();
         var12.append(Build.MANUFACTURER);
         var12.append(Build.MODEL);
         var8 = var12.toString();
         PackageInfo var11 = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
         var12 = new StringBuilder();
         var12.append(var11.versionName);
         var12.append(" (");
         var12.append(var11.versionCode);
         var12.append(")");
         var10 = var12.toString();
         var12 = new StringBuilder();
         var12.append("SDK ");
         var12.append(VERSION.SDK_INT);
         var13 = var12.toString();
      } catch (Exception var9) {
         var12 = new StringBuilder();
         var12.append("SDK ");
         var12.append(VERSION.SDK_INT);
         var13 = var12.toString();
         var7 = "";
         var10 = "App version unknown";
         var8 = "Android unknown";
         var6 = "en";
      }

      if (var6.trim().length() == 0) {
         var6 = "en";
      }

      if (var8.trim().length() == 0) {
         var8 = "Android unknown";
      }

      if (var10.trim().length() == 0) {
         var10 = "App version unknown";
      }

      if (var13.trim().length() == 0) {
         var13 = "SDK Unknown";
      }

      UserConfig.getInstance(this.currentAccount).loadConfig();
      this.init(BuildVars.BUILD_VERSION, 100, BuildVars.APP_ID, var8, var13, var10, var7, var6, var4, FileLog.getNetworkLogPath(), UserConfig.getInstance(this.currentAccount).getClientUserId(), var5);
   }

   // $FF: synthetic method
   static ConcurrentHashMap access$000() {
      return dnsCache;
   }

   public static int generateClassGuid() {
      int var0 = lastClassGuid++;
      return var0;
   }

   public static void getHostByName(String var0, long var1) {
      ConnectionsManager.ResolvedDomain var3 = (ConnectionsManager.ResolvedDomain)dnsCache.get(var0);
      if (var3 != null && SystemClock.elapsedRealtime() - var3.ttl < 300000L) {
         native_onHostNameResolved(var0, var1, var3.getAddress());
      } else {
         ConnectionsManager.ResolveHostByNameTask var4 = (ConnectionsManager.ResolveHostByNameTask)resolvingHostnameTasks.get(var0);
         ConnectionsManager.ResolveHostByNameTask var5 = var4;
         if (var4 == null) {
            var5 = new ConnectionsManager.ResolveHostByNameTask(var0);
            var5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            resolvingHostnameTasks.put(var0, var5);
         }

         var5.addAddress(var1);
      }

   }

   public static int getInitFlags() {
      return 0;
   }

   public static ConnectionsManager getInstance(int var0) {
      ConnectionsManager var1 = Instance[var0];
      ConnectionsManager var2 = var1;
      if (var1 == null) {
         synchronized(ConnectionsManager.class){}

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
               ConnectionsManager[] var23;
               try {
                  var23 = Instance;
                  var2 = new ConnectionsManager(var0);
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

   // $FF: synthetic method
   static void lambda$null$0(RequestDelegate var0, TLObject var1, TLRPC.TL_error var2) {
      var0.run(var1, var2);
      if (var1 != null) {
         var1.freeResources();
      }

   }

   // $FF: synthetic method
   static void lambda$null$1(TLObject var0, RequestDelegate var1, long var2, int var4, String var5, int var6) {
      Exception var10000;
      label71: {
         DispatchQueue var7 = null;
         TLObject var8;
         boolean var10001;
         TLRPC.TL_error var19;
         if (var2 != 0L) {
            try {
               NativeByteBuffer var21 = NativeByteBuffer.wrap(var2);
               var21.reused = true;
               var8 = var0.deserializeResponse(var21, var21.readInt32(true), true);
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label71;
            }

            var19 = null;
         } else if (var5 != null) {
            label69: {
               TLRPC.TL_error var9;
               try {
                  var9 = new TLRPC.TL_error();
                  var9.code = var4;
                  var9.text = var5;
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label71;
               }

               var8 = var7;
               var19 = var9;

               try {
                  if (!BuildVars.LOGS_ENABLED) {
                     break label69;
                  }

                  StringBuilder var20 = new StringBuilder();
                  var20.append(var0);
                  var20.append(" got error ");
                  var20.append(var9.code);
                  var20.append(" ");
                  var20.append(var9.text);
                  FileLog.e(var20.toString());
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label71;
               }

               var8 = var7;
               var19 = var9;
            }
         } else {
            var19 = null;
            var8 = var7;
         }

         if (var8 != null) {
            try {
               var8.networkType = var6;
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label71;
            }
         }

         try {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var16 = new StringBuilder();
               var16.append("java received ");
               var16.append(var8);
               var16.append(" error = ");
               var16.append(var19);
               FileLog.d(var16.toString());
            }
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label71;
         }

         try {
            var7 = Utilities.stageQueue;
            _$$Lambda$ConnectionsManager$N1Ud38cKJRQ_sqp20Fcot5ApM0Y var18 = new _$$Lambda$ConnectionsManager$N1Ud38cKJRQ_sqp20Fcot5ApM0Y(var1, var8, var19);
            var7.postRunnable(var18);
            return;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      }

      Exception var17 = var10000;
      FileLog.e((Throwable)var17);
   }

   // $FF: synthetic method
   static void lambda$onConnectionStateChanged$6(int var0, int var1) {
      getInstance(var0).connectionState = var1;
      NotificationCenter.getInstance(var0).postNotificationName(NotificationCenter.didUpdateConnectionState);
   }

   // $FF: synthetic method
   static void lambda$onLogout$7(int var0) {
      if (UserConfig.getInstance(var0).getClientUserId() != 0) {
         UserConfig.getInstance(var0).clearConfig();
         MessagesController.getInstance(var0).performLogout(0);
      }

   }

   // $FF: synthetic method
   static void lambda$onProxyError$9() {
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShowAlert, 3);
   }

   // $FF: synthetic method
   static void lambda$onRequestNewServerIpAndPort$8(int var0, int var1) {
      if (currentTask == null && (var0 != 0 || Math.abs(lastDnsRequestTime - System.currentTimeMillis()) >= 10000L) && ApplicationLoader.isNetworkOnline()) {
         lastDnsRequestTime = System.currentTimeMillis();
         if (var0 == 2) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("start azure dns task");
            }

            ConnectionsManager.AzureLoadTask var3 = new ConnectionsManager.AzureLoadTask(var1);
            var3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            currentTask = var3;
         } else if (var0 == 1) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("start dns txt task");
            }

            ConnectionsManager.DnsTxtLoadTask var4 = new ConnectionsManager.DnsTxtLoadTask(var1);
            var4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            currentTask = var4;
         } else {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("start firebase task");
            }

            ConnectionsManager.FirebaseTask var5 = new ConnectionsManager.FirebaseTask(var1);
            var5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            currentTask = var5;
         }

      } else {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var2 = new StringBuilder();
            var2.append("don't start task, current task = ");
            var2.append(currentTask);
            var2.append(" next task = ");
            var2.append(var0);
            var2.append(" time diff = ");
            var2.append(Math.abs(lastDnsRequestTime - System.currentTimeMillis()));
            var2.append(" network = ");
            var2.append(ApplicationLoader.isNetworkOnline());
            FileLog.d(var2.toString());
         }

      }
   }

   // $FF: synthetic method
   static void lambda$onSessionCreated$5(int var0) {
      MessagesController.getInstance(var0).getDifference();
   }

   // $FF: synthetic method
   static void lambda$onUnparsedMessageReceived$3(int var0, TLObject var1) {
      MessagesController.getInstance(var0).processUpdates((TLRPC.Updates)var1, false);
   }

   // $FF: synthetic method
   static void lambda$onUpdate$4(int var0) {
      MessagesController.getInstance(var0).updateTimerProc();
   }

   // $FF: synthetic method
   static void lambda$onUpdateConfig$10(int var0, TLRPC.TL_config var1) {
      MessagesController.getInstance(var0).updateConfig(var1);
   }

   public static native void native_applyDatacenterAddress(int var0, int var1, String var2, int var3);

   public static native void native_applyDnsConfig(int var0, long var1, String var3);

   public static native void native_bindRequestToGuid(int var0, int var1, int var2);

   public static native void native_cancelRequest(int var0, int var1, boolean var2);

   public static native void native_cancelRequestsForGuid(int var0, int var1);

   public static native long native_checkProxy(int var0, String var1, int var2, String var3, String var4, String var5, RequestTimeDelegate var6);

   public static native void native_cleanUp(int var0, boolean var1);

   public static native int native_getConnectionState(int var0);

   public static native int native_getCurrentTime(int var0);

   public static native long native_getCurrentTimeMillis(int var0);

   public static native int native_getTimeDifference(int var0);

   public static native void native_init(int var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, int var11, boolean var12, boolean var13, int var14);

   public static native int native_isTestBackend(int var0);

   public static native void native_onHostNameResolved(String var0, long var1, String var3);

   public static native void native_pauseNetwork(int var0);

   public static native void native_resumeNetwork(int var0, boolean var1);

   public static native void native_seSystemLangCode(int var0, String var1);

   public static native void native_sendRequest(int var0, long var1, RequestDelegateInternal var3, QuickAckDelegate var4, WriteToSocketDelegate var5, int var6, int var7, int var8, boolean var9, int var10);

   public static native void native_setJava(boolean var0);

   public static native void native_setLangCode(int var0, String var1);

   public static native void native_setNetworkAvailable(int var0, boolean var1, int var2, boolean var3);

   public static native void native_setProxySettings(int var0, String var1, int var2, String var3, String var4, String var5);

   public static native void native_setPushConnectionEnabled(int var0, boolean var1);

   public static native void native_setSystemLangCode(int var0, String var1);

   public static native void native_setUseIpv6(int var0, boolean var1);

   public static native void native_setUserId(int var0, int var1);

   public static native void native_switchBackend(int var0);

   public static native void native_updateDcSettings(int var0);

   public static void onBytesReceived(int var0, int var1, int var2) {
      try {
         StatsController.getInstance(var2).incrementReceivedBytesCount(var1, 6, (long)var0);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   public static void onBytesSent(int var0, int var1, int var2) {
      try {
         StatsController.getInstance(var2).incrementSentBytesCount(var1, 6, (long)var0);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   public static void onConnectionStateChanged(int var0, int var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ConnectionsManager$wMpd1_zDWgiLp6x8fjImjIX349A(var1, var0));
   }

   public static void onInternalPushReceived(int var0) {
      KeepAliveJob.startJob();
   }

   public static void onLogout(int var0) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ConnectionsManager$WUMuAbrjc16kWFnK3JYXcq2eB4E(var0));
   }

   public static void onProxyError() {
      AndroidUtilities.runOnUIThread(_$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU.INSTANCE);
   }

   public static void onRequestNewServerIpAndPort(int var0, int var1) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$Vntp1UzbcZLxSUJY5_RFJvLOrY4(var0, var1));
   }

   public static void onSessionCreated(int var0) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$c_kbk6lmCmsTzziA11WOyMsJtqY(var0));
   }

   public static void onUnparsedMessageReceived(long var0, int var2) {
      try {
         NativeByteBuffer var3 = NativeByteBuffer.wrap(var0);
         var3.reused = true;
         int var4 = var3.readInt32(true);
         TLObject var8 = TLClassStore.Instance().TLdeserialize(var3, var4, true);
         if (var8 instanceof TLRPC.Updates) {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var5 = new StringBuilder();
               var5.append("java received ");
               var5.append(var8);
               FileLog.d(var5.toString());
            }

            KeepAliveJob.finishJob();
            DispatchQueue var6 = Utilities.stageQueue;
            _$$Lambda$ConnectionsManager$R5V1iXmwj8PWON_tb_jcTaBhzJo var9 = new _$$Lambda$ConnectionsManager$R5V1iXmwj8PWON_tb_jcTaBhzJo(var2, var8);
            var6.postRunnable(var9);
         } else if (BuildVars.LOGS_ENABLED) {
            FileLog.d(String.format("java received unknown constructor 0x%x", var4));
         }
      } catch (Exception var7) {
         FileLog.e((Throwable)var7);
      }

   }

   public static void onUpdate(int var0) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$GiXNWNTneL61N5XH1sI4IVkif4k(var0));
   }

   public static void onUpdateConfig(long param0, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static void setLangCode(String var0) {
      var0 = var0.replace('_', '-').toLowerCase();

      for(int var1 = 0; var1 < 3; ++var1) {
         native_setLangCode(var1, var0);
      }

   }

   public static void setProxySettings(boolean var0, String var1, int var2, String var3, String var4, String var5) {
      String var6 = var1;
      if (var1 == null) {
         var6 = "";
      }

      var1 = var3;
      if (var3 == null) {
         var1 = "";
      }

      var3 = var4;
      if (var4 == null) {
         var3 = "";
      }

      var4 = var5;
      if (var5 == null) {
         var4 = "";
      }

      for(int var7 = 0; var7 < 3; ++var7) {
         if (var0 && !TextUtils.isEmpty(var6)) {
            native_setProxySettings(var7, var6, var2, var1, var3, var4);
         } else {
            native_setProxySettings(var7, "", 1080, "", "", "");
         }

         if (UserConfig.getInstance(var7).isClientActivated()) {
            MessagesController.getInstance(var7).checkProxyInfo(true);
         }
      }

   }

   public static void setSystemLangCode(String var0) {
      var0 = var0.replace('_', '-').toLowerCase();

      for(int var1 = 0; var1 < 3; ++var1) {
         native_setSystemLangCode(var1, var0);
      }

   }

   @SuppressLint({"NewApi"})
   protected static boolean useIpv6Address() {
      if (VERSION.SDK_INT < 19) {
         return false;
      } else {
         Enumeration var0;
         NetworkInterface var1;
         Throwable var10000;
         boolean var10001;
         Throwable var25;
         if (BuildVars.LOGS_ENABLED) {
            label200: {
               label199: {
                  try {
                     var0 = NetworkInterface.getNetworkInterfaces();
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label199;
                  }

                  label196:
                  while(true) {
                     while(true) {
                        try {
                           do {
                              do {
                                 if (!var0.hasMoreElements()) {
                                    break label200;
                                 }

                                 var1 = (NetworkInterface)var0.nextElement();
                              } while(!var1.isUp());
                           } while(var1.isLoopback());

                           if (var1.getInterfaceAddresses().isEmpty()) {
                              continue;
                           }
                        } catch (Throwable var23) {
                           var10000 = var23;
                           var10001 = false;
                           break label196;
                        }

                        try {
                           if (BuildVars.LOGS_ENABLED) {
                              StringBuilder var2 = new StringBuilder();
                              var2.append("valid interface: ");
                              var2.append(var1);
                              FileLog.d(var2.toString());
                           }
                        } catch (Throwable var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label196;
                        }

                        List var28;
                        try {
                           var28 = var1.getInterfaceAddresses();
                        } catch (Throwable var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label196;
                        }

                        int var3 = 0;

                        while(true) {
                           InetAddress var4;
                           try {
                              if (var3 >= var28.size()) {
                                 break;
                              }

                              var4 = ((InterfaceAddress)var28.get(var3)).getAddress();
                              if (BuildVars.LOGS_ENABLED) {
                                 StringBuilder var26 = new StringBuilder();
                                 var26.append("address: ");
                                 var26.append(var4.getHostAddress());
                                 FileLog.d(var26.toString());
                              }
                           } catch (Throwable var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label196;
                           }

                           label179: {
                              try {
                                 if (var4.isLinkLocalAddress() || var4.isLoopbackAddress() || var4.isMulticastAddress()) {
                                    break label179;
                                 }
                              } catch (Throwable var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 break label196;
                              }

                              try {
                                 if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("address is good");
                                 }
                              } catch (Throwable var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label196;
                              }
                           }

                           ++var3;
                        }
                     }
                  }
               }

               var25 = var10000;
               FileLog.e(var25);
            }
         }

         boolean var5;
         boolean var30;
         label222: {
            label208: {
               try {
                  var0 = NetworkInterface.getNetworkInterfaces();
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label208;
               }

               var30 = false;
               var5 = false;

               label147:
               while(true) {
                  while(true) {
                     try {
                        do {
                           if (!var0.hasMoreElements()) {
                              break label222;
                           }

                           var1 = (NetworkInterface)var0.nextElement();
                        } while(!var1.isUp());

                        if (!var1.isLoopback()) {
                           break;
                        }
                     } catch (Throwable var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label147;
                     }
                  }

                  List var27;
                  try {
                     var27 = var1.getInterfaceAddresses();
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break;
                  }

                  boolean var6 = var30;
                  byte var7 = 0;
                  var30 = var5;
                  var5 = var6;
                  int var31 = var7;

                  while(true) {
                     InetAddress var29;
                     try {
                        if (var31 >= var27.size()) {
                           break;
                        }

                        var29 = ((InterfaceAddress)var27.get(var31)).getAddress();
                     } catch (Throwable var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label147;
                     }

                     boolean var32 = var5;
                     boolean var8 = var30;

                     label211: {
                        try {
                           if (var29.isLinkLocalAddress()) {
                              break label211;
                           }
                        } catch (Throwable var15) {
                           var10000 = var15;
                           var10001 = false;
                           break label147;
                        }

                        var32 = var5;
                        var8 = var30;

                        label212: {
                           try {
                              if (var29.isLoopbackAddress()) {
                                 break label211;
                              }

                              if (var29.isMulticastAddress()) {
                                 break label212;
                              }
                           } catch (Throwable var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label147;
                           }

                           label120: {
                              try {
                                 if (!(var29 instanceof Inet6Address)) {
                                    break label120;
                                 }
                              } catch (Throwable var13) {
                                 var10000 = var13;
                                 var10001 = false;
                                 break label147;
                              }

                              var8 = true;
                              var32 = var5;
                              break label211;
                           }

                           var32 = var5;
                           var8 = var30;

                           boolean var9;
                           try {
                              if (!(var29 instanceof Inet4Address)) {
                                 break label211;
                              }

                              var9 = var29.getHostAddress().startsWith("192.0.0.");
                           } catch (Throwable var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label147;
                           }

                           var32 = var5;
                           var8 = var30;
                           if (!var9) {
                              var32 = true;
                              var8 = var30;
                           }
                           break label211;
                        }

                        var32 = var5;
                        var8 = var30;
                     }

                     ++var31;
                     var5 = var32;
                     var30 = var8;
                  }

                  var6 = var30;
                  var30 = var5;
                  var5 = var6;
               }
            }

            var25 = var10000;
            FileLog.e(var25);
            return false;
         }

         if (!var30 && var5) {
            return true;
         } else {
            return false;
         }
      }
   }

   public void applyDatacenterAddress(int var1, String var2, int var3) {
      native_applyDatacenterAddress(this.currentAccount, var1, var2, var3);
   }

   public void bindRequestToGuid(int var1, int var2) {
      native_bindRequestToGuid(this.currentAccount, var1, var2);
   }

   public void cancelRequest(int var1, boolean var2) {
      native_cancelRequest(this.currentAccount, var1, var2);
   }

   public void cancelRequestsForGuid(int var1) {
      native_cancelRequestsForGuid(this.currentAccount, var1);
   }

   public void checkConnection() {
      native_setUseIpv6(this.currentAccount, useIpv6Address());
      native_setNetworkAvailable(this.currentAccount, ApplicationLoader.isNetworkOnline(), ApplicationLoader.getCurrentNetworkType(), ApplicationLoader.isConnectionSlow());
   }

   public long checkProxy(String var1, int var2, String var3, String var4, String var5, RequestTimeDelegate var6) {
      if (TextUtils.isEmpty(var1)) {
         return 0L;
      } else {
         if (var1 == null) {
            var1 = "";
         }

         if (var3 == null) {
            var3 = "";
         }

         if (var4 == null) {
            var4 = "";
         }

         if (var5 == null) {
            var5 = "";
         }

         return native_checkProxy(this.currentAccount, var1, var2, var3, var4, var5, var6);
      }
   }

   public void cleanup(boolean var1) {
      native_cleanUp(this.currentAccount, var1);
   }

   public int getConnectionState() {
      return this.connectionState == 3 && this.isUpdating ? 5 : this.connectionState;
   }

   public int getCurrentTime() {
      return native_getCurrentTime(this.currentAccount);
   }

   public long getCurrentTimeMillis() {
      return native_getCurrentTimeMillis(this.currentAccount);
   }

   public long getPauseTime() {
      return this.lastPauseTime;
   }

   public int getTimeDifference() {
      return native_getTimeDifference(this.currentAccount);
   }

   public void init(int var1, int var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, int var11, boolean var12) {
      SharedPreferences var13 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
      String var14 = var13.getString("proxy_ip", "");
      String var15 = var13.getString("proxy_user", "");
      String var16 = var13.getString("proxy_pass", "");
      String var17 = var13.getString("proxy_secret", "");
      int var18 = var13.getInt("proxy_port", 1080);
      if (var13.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty(var14)) {
         native_setProxySettings(this.currentAccount, var14, var18, var15, var16, var17);
      }

      native_init(this.currentAccount, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, ApplicationLoader.isNetworkOnline(), ApplicationLoader.getCurrentNetworkType());
      this.checkConnection();
   }

   // $FF: synthetic method
   public void lambda$sendRequest$2$ConnectionsManager(TLObject var1, int var2, RequestDelegate var3, QuickAckDelegate var4, WriteToSocketDelegate var5, int var6, int var7, int var8, boolean var9) {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var10 = new StringBuilder();
         var10.append("send request ");
         var10.append(var1);
         var10.append(" with token = ");
         var10.append(var2);
         FileLog.d(var10.toString());
      }

      Exception var18;
      label21: {
         NativeByteBuffer var16;
         try {
            var16 = new NativeByteBuffer(var1.getObjectSize());
            var1.serializeToStream(var16);
            var1.freeResources();
         } catch (Exception var15) {
            var18 = var15;
            break label21;
         }

         try {
            int var11 = this.currentAccount;
            long var12 = var16.address;
            _$$Lambda$ConnectionsManager$qOa5d09BI1fBg0o1upbl6aXFnSY var17 = new _$$Lambda$ConnectionsManager$qOa5d09BI1fBg0o1upbl6aXFnSY(var1, var3);
            native_sendRequest(var11, var12, var17, var4, var5, var6, var7, var8, var9, var2);
            return;
         } catch (Exception var14) {
            var18 = var14;
         }
      }

      FileLog.e((Throwable)var18);
   }

   // $FF: synthetic method
   public void lambda$setIsUpdating$11$ConnectionsManager(boolean var1) {
      if (this.isUpdating != var1) {
         this.isUpdating = var1;
         if (this.connectionState == 3) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didUpdateConnectionState);
         }

      }
   }

   public void resumeNetworkMaybe() {
      native_resumeNetwork(this.currentAccount, true);
   }

   public int sendRequest(TLObject var1, RequestDelegate var2) {
      return this.sendRequest(var1, var2, (QuickAckDelegate)null, 0);
   }

   public int sendRequest(TLObject var1, RequestDelegate var2, int var3) {
      return this.sendRequest(var1, var2, (QuickAckDelegate)null, (WriteToSocketDelegate)null, var3, Integer.MAX_VALUE, 1, true);
   }

   public int sendRequest(TLObject var1, RequestDelegate var2, int var3, int var4) {
      return this.sendRequest(var1, var2, (QuickAckDelegate)null, (WriteToSocketDelegate)null, var3, Integer.MAX_VALUE, var4, true);
   }

   public int sendRequest(TLObject var1, RequestDelegate var2, QuickAckDelegate var3, int var4) {
      return this.sendRequest(var1, var2, var3, (WriteToSocketDelegate)null, var4, Integer.MAX_VALUE, 1, true);
   }

   public int sendRequest(TLObject var1, RequestDelegate var2, QuickAckDelegate var3, WriteToSocketDelegate var4, int var5, int var6, int var7, boolean var8) {
      int var9 = this.lastRequestToken.getAndIncrement();
      Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$csmhNL7gP4ZbIN5_kTApilG8kBQ(this, var1, var9, var2, var3, var4, var5, var6, var7, var8));
      return var9;
   }

   public void setAppPaused(boolean var1, boolean var2) {
      if (!var2) {
         this.appPaused = var1;
         StringBuilder var3;
         if (BuildVars.LOGS_ENABLED) {
            var3 = new StringBuilder();
            var3.append("app paused = ");
            var3.append(var1);
            FileLog.d(var3.toString());
         }

         if (var1) {
            --this.appResumeCount;
         } else {
            ++this.appResumeCount;
         }

         if (BuildVars.LOGS_ENABLED) {
            var3 = new StringBuilder();
            var3.append("app resume count ");
            var3.append(this.appResumeCount);
            FileLog.d(var3.toString());
         }

         if (this.appResumeCount < 0) {
            this.appResumeCount = 0;
         }
      }

      if (this.appResumeCount == 0) {
         if (this.lastPauseTime == 0L) {
            this.lastPauseTime = System.currentTimeMillis();
         }

         native_pauseNetwork(this.currentAccount);
      } else {
         if (this.appPaused) {
            return;
         }

         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("reset app pause time");
         }

         if (this.lastPauseTime != 0L && System.currentTimeMillis() - this.lastPauseTime > 5000L) {
            ContactsController.getInstance(this.currentAccount).checkContacts();
         }

         this.lastPauseTime = 0L;
         native_resumeNetwork(this.currentAccount, false);
      }

   }

   public void setIsUpdating(boolean var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ConnectionsManager$_MqDe5Su_gCe6Qmn9_j7mUamUVo(this, var1));
   }

   public void setPushConnectionEnabled(boolean var1) {
      native_setPushConnectionEnabled(this.currentAccount, var1);
   }

   public void setUserId(int var1) {
      native_setUserId(this.currentAccount, var1);
   }

   public void switchBackend() {
      MessagesController.getGlobalMainSettings().edit().remove("language_showed2").commit();
      native_switchBackend(this.currentAccount);
   }

   public void updateDcSettings() {
      native_updateDcSettings(this.currentAccount);
   }

   private static class AzureLoadTask extends AsyncTask {
      private int currentAccount;

      public AzureLoadTask(int var1) {
         this.currentAccount = var1;
      }

      protected NativeByteBuffer doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      public void lambda$onPostExecute$0$ConnectionsManager$AzureLoadTask(NativeByteBuffer var1) {
         if (var1 != null) {
            int var2 = this.currentAccount;
            ConnectionsManager.native_applyDnsConfig(var2, var1.address, UserConfig.getInstance(var2).getClientPhone());
         } else if (BuildVars.LOGS_ENABLED) {
            FileLog.d("failed to get azure result");
         }

         ConnectionsManager.currentTask = null;
      }

      protected void onPostExecute(NativeByteBuffer var1) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$AzureLoadTask$CCvFvz5lAUpDF3DaGVJItVYIMOk(this, var1));
      }
   }

   private static class DnsTxtLoadTask extends AsyncTask {
      private int currentAccount;

      public DnsTxtLoadTask(int var1) {
         this.currentAccount = var1;
      }

      // $FF: synthetic method
      static int lambda$doInBackground$0(String var0, String var1) {
         int var2 = var0.length();
         int var3 = var1.length();
         if (var2 > var3) {
            return -1;
         } else {
            return var2 < var3 ? 1 : 0;
         }
      }

      protected NativeByteBuffer doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      // $FF: synthetic method
      public void lambda$onPostExecute$1$ConnectionsManager$DnsTxtLoadTask(NativeByteBuffer var1) {
         if (var1 != null) {
            ConnectionsManager.currentTask = null;
            int var2 = this.currentAccount;
            ConnectionsManager.native_applyDnsConfig(var2, var1.address, UserConfig.getInstance(var2).getClientPhone());
         } else {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("failed to get dns txt result");
               FileLog.d("start azure task");
            }

            ConnectionsManager.AzureLoadTask var3 = new ConnectionsManager.AzureLoadTask(this.currentAccount);
            var3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            ConnectionsManager.currentTask = var3;
         }

      }

      protected void onPostExecute(NativeByteBuffer var1) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$DnsTxtLoadTask$Y_uiONB1DXfH_CyjIpbAd_79WxM(this, var1));
      }
   }

   private static class FirebaseTask extends AsyncTask {
      private int currentAccount;

      public FirebaseTask(int var1) {
         this.currentAccount = var1;
      }

      protected NativeByteBuffer doInBackground(Void... var1) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$FirebaseTask$SaZGsq57fSFWJm6Aju2tQS8ctbo(this));
         return null;
      }

      // $FF: synthetic method
      public void lambda$doInBackground$0$ConnectionsManager$FirebaseTask() {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("failed to get firebase result");
            FileLog.d("start dns txt task");
         }

         ConnectionsManager.DnsTxtLoadTask var1 = new ConnectionsManager.DnsTxtLoadTask(this.currentAccount);
         var1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
         ConnectionsManager.currentTask = var1;
      }

      protected void onPostExecute(NativeByteBuffer var1) {
      }
   }

   private static class ResolveHostByNameTask extends AsyncTask {
      private ArrayList addresses = new ArrayList();
      private String currentHostName;

      public ResolveHostByNameTask(String var1) {
         this.currentHostName = var1;
      }

      public void addAddress(long var1) {
         if (!this.addresses.contains(var1)) {
            this.addresses.add(var1);
         }
      }

      protected String doInBackground(Void... param1) {
         // $FF: Couldn't be decompiled
      }

      protected void onPostExecute(String var1) {
         int var2 = this.addresses.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            ConnectionsManager.native_onHostNameResolved(this.currentHostName, (Long)this.addresses.get(var3), var1);
         }

         ConnectionsManager.resolvingHostnameTasks.remove(this.currentHostName);
      }
   }

   private static class ResolvedDomain {
      public ArrayList addresses;
      long ttl;

      public ResolvedDomain(ArrayList var1, long var2) {
         this.addresses = var1;
         this.ttl = var2;
      }

      public String getAddress() {
         ArrayList var1 = this.addresses;
         return (String)var1.get(Utilities.random.nextInt(var1.size()));
      }
   }
}
