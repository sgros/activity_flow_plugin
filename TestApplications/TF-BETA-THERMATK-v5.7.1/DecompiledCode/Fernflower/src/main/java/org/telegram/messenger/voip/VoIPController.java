package org.telegram.messenger.voip;

import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.SystemClock;
import android.os.Build.VERSION;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.voip.VoIPHelper;

public class VoIPController {
   public static final int DATA_SAVING_ALWAYS = 2;
   public static final int DATA_SAVING_MOBILE = 1;
   public static final int DATA_SAVING_NEVER = 0;
   public static final int DATA_SAVING_ROAMING = 3;
   public static final int ERROR_AUDIO_IO = 3;
   public static final int ERROR_CONNECTION_SERVICE = -5;
   public static final int ERROR_INCOMPATIBLE = 1;
   public static final int ERROR_INSECURE_UPGRADE = -4;
   public static final int ERROR_LOCALIZED = -3;
   public static final int ERROR_PEER_OUTDATED = -1;
   public static final int ERROR_PRIVACY = -2;
   public static final int ERROR_TIMEOUT = 2;
   public static final int ERROR_UNKNOWN = 0;
   public static final int NET_TYPE_3G = 3;
   public static final int NET_TYPE_DIALUP = 10;
   public static final int NET_TYPE_EDGE = 2;
   public static final int NET_TYPE_ETHERNET = 7;
   public static final int NET_TYPE_GPRS = 1;
   public static final int NET_TYPE_HSPA = 4;
   public static final int NET_TYPE_LTE = 5;
   public static final int NET_TYPE_OTHER_HIGH_SPEED = 8;
   public static final int NET_TYPE_OTHER_LOW_SPEED = 9;
   public static final int NET_TYPE_OTHER_MOBILE = 11;
   public static final int NET_TYPE_UNKNOWN = 0;
   public static final int NET_TYPE_WIFI = 6;
   public static final int PEER_CAP_GROUP_CALLS = 1;
   public static final int STATE_ESTABLISHED = 3;
   public static final int STATE_FAILED = 4;
   public static final int STATE_RECONNECTING = 5;
   public static final int STATE_WAIT_INIT = 1;
   public static final int STATE_WAIT_INIT_ACK = 2;
   protected long callStartTime;
   protected VoIPController.ConnectionStateListener listener;
   protected long nativeInst = 0L;

   public VoIPController() {
      this.nativeInst = this.nativeInit((new File(ApplicationLoader.applicationContext.getFilesDir(), "voip_persistent_state.json")).getAbsolutePath());
   }

   private void callUpgradeRequestReceived() {
      VoIPController.ConnectionStateListener var1 = this.listener;
      if (var1 != null) {
         var1.onCallUpgradeRequestReceived();
      }

   }

   public static native int getConnectionMaxLayer();

   private String getLogFilePath(long var1) {
      File var3 = VoIPHelper.getLogsDir();
      if (!BuildVars.DEBUG_VERSION) {
         File[] var4 = var3.listFiles();
         ArrayList var5 = new ArrayList();
         var5.addAll(Arrays.asList(var4));

         while(var5.size() > 20) {
            File var8 = (File)var5.get(0);
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               File var7 = (File)var6.next();
               if (var7.getName().endsWith(".log") && var7.lastModified() < var8.lastModified()) {
                  var8 = var7;
               }
            }

            var8.delete();
            var5.remove(var8);
         }
      }

      StringBuilder var9 = new StringBuilder();
      var9.append(var1);
      var9.append(".log");
      return (new File(var3, var9.toString())).getAbsolutePath();
   }

   private String getLogFilePath(String var1) {
      Calendar var2 = Calendar.getInstance();
      return (new File(ApplicationLoader.applicationContext.getExternalFilesDir((String)null), String.format(Locale.US, "logs/%02d_%02d_%04d_%02d_%02d_%02d_%s.txt", var2.get(5), var2.get(2) + 1, var2.get(1), var2.get(11), var2.get(12), var2.get(13), var1))).getAbsolutePath();
   }

   public static String getVersion() {
      return nativeGetVersion();
   }

   private void groupCallKeyReceived(byte[] var1) {
      VoIPController.ConnectionStateListener var2 = this.listener;
      if (var2 != null) {
         var2.onGroupCallKeyReceived(var1);
      }

   }

   private void groupCallKeySent() {
      VoIPController.ConnectionStateListener var1 = this.listener;
      if (var1 != null) {
         var1.onGroupCallKeySent();
      }

   }

   private void handleSignalBarsChange(int var1) {
      VoIPController.ConnectionStateListener var2 = this.listener;
      if (var2 != null) {
         var2.onSignalBarCountChanged(var1);
      }

   }

   private void handleStateChange(int var1) {
      if (var1 == 3 && this.callStartTime == 0L) {
         this.callStartTime = SystemClock.elapsedRealtime();
      }

      VoIPController.ConnectionStateListener var2 = this.listener;
      if (var2 != null) {
         var2.onConnectionStateChanged(var1);
      }

   }

   private native void nativeConnect(long var1);

   private native void nativeDebugCtl(long var1, int var3, int var4);

   private native String nativeGetDebugLog(long var1);

   private native String nativeGetDebugString(long var1);

   private native int nativeGetLastError(long var1);

   private native int nativeGetPeerCapabilities(long var1);

   private native long nativeGetPreferredRelayID(long var1);

   private native void nativeGetStats(long var1, VoIPController.Stats var3);

   private static native String nativeGetVersion();

   private native long nativeInit(String var1);

   private static native boolean nativeNeedRate(long var0);

   private native void nativeRelease(long var1);

   private native void nativeRequestCallUpgrade(long var1);

   private native void nativeSendGroupCallKey(long var1, byte[] var3);

   private native void nativeSetAudioOutputGainControlEnabled(long var1, boolean var3);

   private native void nativeSetConfig(long var1, double var3, double var5, int var7, boolean var8, boolean var9, boolean var10, String var11, String var12, boolean var13);

   private native void nativeSetEchoCancellationStrength(long var1, int var3);

   private native void nativeSetEncryptionKey(long var1, byte[] var3, boolean var4);

   private native void nativeSetMicMute(long var1, boolean var3);

   private static native void nativeSetNativeBufferSize(int var0);

   private native void nativeSetNetworkType(long var1, int var3);

   private native void nativeSetProxy(long var1, String var3, int var4, String var5, String var6);

   private native void nativeSetRemoteEndpoints(long var1, TLRPC.TL_phoneConnection[] var3, boolean var4, boolean var5, int var6);

   public static native void nativeSetVideoRenderer(long var0, long var2);

   public static native void nativeSetVideoSource(long var0, long var2);

   private native void nativeStart(long var1);

   public static void setNativeBufferSize(int var0) {
      nativeSetNativeBufferSize(var0);
   }

   public void connect() {
      this.ensureNativeInstance();
      this.nativeConnect(this.nativeInst);
   }

   public void debugCtl(int var1, int var2) {
      this.ensureNativeInstance();
      this.nativeDebugCtl(this.nativeInst, var1, var2);
   }

   protected void ensureNativeInstance() {
      if (this.nativeInst == 0L) {
         throw new IllegalStateException("Native instance is not valid");
      }
   }

   public long getCallDuration() {
      return SystemClock.elapsedRealtime() - this.callStartTime;
   }

   public String getDebugLog() {
      this.ensureNativeInstance();
      return this.nativeGetDebugLog(this.nativeInst);
   }

   public String getDebugString() {
      this.ensureNativeInstance();
      return this.nativeGetDebugString(this.nativeInst);
   }

   public int getLastError() {
      this.ensureNativeInstance();
      return this.nativeGetLastError(this.nativeInst);
   }

   public int getPeerCapabilities() {
      this.ensureNativeInstance();
      return this.nativeGetPeerCapabilities(this.nativeInst);
   }

   public long getPreferredRelayID() {
      this.ensureNativeInstance();
      return this.nativeGetPreferredRelayID(this.nativeInst);
   }

   public void getStats(VoIPController.Stats var1) {
      this.ensureNativeInstance();
      if (var1 != null) {
         this.nativeGetStats(this.nativeInst, var1);
      } else {
         throw new NullPointerException("You're not supposed to pass null here");
      }
   }

   public boolean needRate() {
      this.ensureNativeInstance();
      return nativeNeedRate(this.nativeInst);
   }

   public void release() {
      this.ensureNativeInstance();
      this.nativeRelease(this.nativeInst);
      this.nativeInst = 0L;
   }

   public void requestCallUpgrade() {
      this.ensureNativeInstance();
      this.nativeRequestCallUpgrade(this.nativeInst);
   }

   public void sendGroupCallKey(byte[] var1) {
      if (var1 != null) {
         if (var1.length == 256) {
            this.ensureNativeInstance();
            this.nativeSendGroupCallKey(this.nativeInst, var1);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("key must be 256 bytes long, got ");
            var2.append(var1.length);
            throw new IllegalArgumentException(var2.toString());
         }
      } else {
         throw new NullPointerException("key can not be null");
      }
   }

   public void setAudioOutputGainControlEnabled(boolean var1) {
      this.ensureNativeInstance();
      this.nativeSetAudioOutputGainControlEnabled(this.nativeInst, var1);
   }

   public void setConfig(double var1, double var3, int var5, long var6) {
      boolean var8;
      boolean var9;
      label59: {
         label58: {
            this.ensureNativeInstance();
            if (VERSION.SDK_INT >= 16) {
               label56: {
                  try {
                     var8 = AcousticEchoCanceler.isAvailable();
                  } catch (Throwable var16) {
                     break label56;
                  }

                  try {
                     var9 = NoiseSuppressor.isAvailable();
                     break label59;
                  } catch (Throwable var15) {
                     break label58;
                  }
               }
            }

            var8 = false;
         }

         var9 = false;
      }

      boolean var10 = MessagesController.getGlobalMainSettings().getBoolean("dbg_dump_call_stats", false);
      long var11 = this.nativeInst;
      if (var8 && VoIPServerConfig.getBoolean("use_system_aec", true)) {
         var8 = false;
      } else {
         var8 = true;
      }

      if (var9 && VoIPServerConfig.getBoolean("use_system_ns", true)) {
         var9 = false;
      } else {
         var9 = true;
      }

      String var17;
      if (BuildVars.DEBUG_VERSION) {
         StringBuilder var13 = new StringBuilder();
         var13.append("voip");
         var13.append(var6);
         var17 = this.getLogFilePath(var13.toString());
      } else {
         var17 = this.getLogFilePath(var6);
      }

      String var14;
      if (BuildVars.DEBUG_VERSION && var10) {
         var14 = this.getLogFilePath("voipStats");
      } else {
         var14 = null;
      }

      this.nativeSetConfig(var11, var1, var3, var5, var8, var9, true, var17, var14, BuildVars.DEBUG_VERSION);
   }

   public void setConnectionStateListener(VoIPController.ConnectionStateListener var1) {
      this.listener = var1;
   }

   public void setEchoCancellationStrength(int var1) {
      this.ensureNativeInstance();
      this.nativeSetEchoCancellationStrength(this.nativeInst, var1);
   }

   public void setEncryptionKey(byte[] var1, boolean var2) {
      if (var1.length == 256) {
         this.ensureNativeInstance();
         this.nativeSetEncryptionKey(this.nativeInst, var1, var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("key length must be exactly 256 bytes but is ");
         var3.append(var1.length);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void setMicMute(boolean var1) {
      this.ensureNativeInstance();
      this.nativeSetMicMute(this.nativeInst, var1);
   }

   public void setNetworkType(int var1) {
      this.ensureNativeInstance();
      this.nativeSetNetworkType(this.nativeInst, var1);
   }

   public void setProxy(String var1, int var2, String var3, String var4) {
      this.ensureNativeInstance();
      if (var1 != null) {
         this.nativeSetProxy(this.nativeInst, var1, var2, var3, var4);
      } else {
         throw new NullPointerException("address can't be null");
      }
   }

   public void setRemoteEndpoints(TLRPC.TL_phoneConnection[] var1, boolean var2, boolean var3, int var4) {
      if (var1.length == 0) {
         throw new IllegalArgumentException("endpoints size is 0");
      } else {
         for(int var5 = 0; var5 < var1.length; ++var5) {
            TLRPC.TL_phoneConnection var6 = var1[var5];
            String var7 = var6.ip;
            StringBuilder var8;
            if (var7 == null || var7.length() == 0) {
               var8 = new StringBuilder();
               var8.append("endpoint ");
               var8.append(var6);
               var8.append(" has empty/null ipv4");
               throw new IllegalArgumentException(var8.toString());
            }

            byte[] var9 = var6.peer_tag;
            if (var9 != null && var9.length != 16) {
               var8 = new StringBuilder();
               var8.append("endpoint ");
               var8.append(var6);
               var8.append(" has peer_tag of wrong length");
               throw new IllegalArgumentException(var8.toString());
            }
         }

         this.ensureNativeInstance();
         this.nativeSetRemoteEndpoints(this.nativeInst, var1, var2, var3, var4);
      }
   }

   public void start() {
      this.ensureNativeInstance();
      this.nativeStart(this.nativeInst);
   }

   public interface ConnectionStateListener {
      void onCallUpgradeRequestReceived();

      void onConnectionStateChanged(int var1);

      void onGroupCallKeyReceived(byte[] var1);

      void onGroupCallKeySent();

      void onSignalBarCountChanged(int var1);
   }

   public static class Stats {
      public long bytesRecvdMobile;
      public long bytesRecvdWifi;
      public long bytesSentMobile;
      public long bytesSentWifi;

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Stats{bytesRecvdMobile=");
         var1.append(this.bytesRecvdMobile);
         var1.append(", bytesSentWifi=");
         var1.append(this.bytesSentWifi);
         var1.append(", bytesRecvdWifi=");
         var1.append(this.bytesRecvdWifi);
         var1.append(", bytesSentMobile=");
         var1.append(this.bytesSentMobile);
         var1.append('}');
         return var1.toString();
      }
   }
}
