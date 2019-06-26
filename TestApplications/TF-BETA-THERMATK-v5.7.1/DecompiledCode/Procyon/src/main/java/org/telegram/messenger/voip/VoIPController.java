// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import org.telegram.tgnet.TLRPC;
import android.os.SystemClock;
import java.util.Locale;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.telegram.messenger.BuildVars;
import org.telegram.ui.Components.voip.VoIPHelper;
import java.io.File;
import org.telegram.messenger.ApplicationLoader;

public class VoIPController
{
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
    protected ConnectionStateListener listener;
    protected long nativeInst;
    
    public VoIPController() {
        this.nativeInst = 0L;
        this.nativeInst = this.nativeInit(new File(ApplicationLoader.applicationContext.getFilesDir(), "voip_persistent_state.json").getAbsolutePath());
    }
    
    private void callUpgradeRequestReceived() {
        final ConnectionStateListener listener = this.listener;
        if (listener != null) {
            listener.onCallUpgradeRequestReceived();
        }
    }
    
    public static native int getConnectionMaxLayer();
    
    private String getLogFilePath(final long lng) {
        final File logsDir = VoIPHelper.getLogsDir();
        if (!BuildVars.DEBUG_VERSION) {
            final File[] listFiles = logsDir.listFiles();
            final ArrayList<File> list = new ArrayList<File>();
            list.addAll(Arrays.asList(listFiles));
            while (list.size() > 20) {
                File o = list.get(0);
                for (final File file : list) {
                    if (file.getName().endsWith(".log") && file.lastModified() < o.lastModified()) {
                        o = file;
                    }
                }
                o.delete();
                list.remove(o);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(lng);
        sb.append(".log");
        return new File(logsDir, sb.toString()).getAbsolutePath();
    }
    
    private String getLogFilePath(final String s) {
        final Calendar instance = Calendar.getInstance();
        return new File(ApplicationLoader.applicationContext.getExternalFilesDir((String)null), String.format(Locale.US, "logs/%02d_%02d_%04d_%02d_%02d_%02d_%s.txt", instance.get(5), instance.get(2) + 1, instance.get(1), instance.get(11), instance.get(12), instance.get(13), s)).getAbsolutePath();
    }
    
    public static String getVersion() {
        return nativeGetVersion();
    }
    
    private void groupCallKeyReceived(final byte[] array) {
        final ConnectionStateListener listener = this.listener;
        if (listener != null) {
            listener.onGroupCallKeyReceived(array);
        }
    }
    
    private void groupCallKeySent() {
        final ConnectionStateListener listener = this.listener;
        if (listener != null) {
            listener.onGroupCallKeySent();
        }
    }
    
    private void handleSignalBarsChange(final int n) {
        final ConnectionStateListener listener = this.listener;
        if (listener != null) {
            listener.onSignalBarCountChanged(n);
        }
    }
    
    private void handleStateChange(final int n) {
        if (n == 3 && this.callStartTime == 0L) {
            this.callStartTime = SystemClock.elapsedRealtime();
        }
        final ConnectionStateListener listener = this.listener;
        if (listener != null) {
            listener.onConnectionStateChanged(n);
        }
    }
    
    private native void nativeConnect(final long p0);
    
    private native void nativeDebugCtl(final long p0, final int p1, final int p2);
    
    private native String nativeGetDebugLog(final long p0);
    
    private native String nativeGetDebugString(final long p0);
    
    private native int nativeGetLastError(final long p0);
    
    private native int nativeGetPeerCapabilities(final long p0);
    
    private native long nativeGetPreferredRelayID(final long p0);
    
    private native void nativeGetStats(final long p0, final Stats p1);
    
    private static native String nativeGetVersion();
    
    private native long nativeInit(final String p0);
    
    private static native boolean nativeNeedRate(final long p0);
    
    private native void nativeRelease(final long p0);
    
    private native void nativeRequestCallUpgrade(final long p0);
    
    private native void nativeSendGroupCallKey(final long p0, final byte[] p1);
    
    private native void nativeSetAudioOutputGainControlEnabled(final long p0, final boolean p1);
    
    private native void nativeSetConfig(final long p0, final double p1, final double p2, final int p3, final boolean p4, final boolean p5, final boolean p6, final String p7, final String p8, final boolean p9);
    
    private native void nativeSetEchoCancellationStrength(final long p0, final int p1);
    
    private native void nativeSetEncryptionKey(final long p0, final byte[] p1, final boolean p2);
    
    private native void nativeSetMicMute(final long p0, final boolean p1);
    
    private static native void nativeSetNativeBufferSize(final int p0);
    
    private native void nativeSetNetworkType(final long p0, final int p1);
    
    private native void nativeSetProxy(final long p0, final String p1, final int p2, final String p3, final String p4);
    
    private native void nativeSetRemoteEndpoints(final long p0, final TLRPC.TL_phoneConnection[] p1, final boolean p2, final boolean p3, final int p4);
    
    public static native void nativeSetVideoRenderer(final long p0, final long p1);
    
    public static native void nativeSetVideoSource(final long p0, final long p1);
    
    private native void nativeStart(final long p0);
    
    public static void setNativeBufferSize(final int n) {
        nativeSetNativeBufferSize(n);
    }
    
    public void connect() {
        this.ensureNativeInstance();
        this.nativeConnect(this.nativeInst);
    }
    
    public void debugCtl(final int n, final int n2) {
        this.ensureNativeInstance();
        this.nativeDebugCtl(this.nativeInst, n, n2);
    }
    
    protected void ensureNativeInstance() {
        if (this.nativeInst != 0L) {
            return;
        }
        throw new IllegalStateException("Native instance is not valid");
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
    
    public void getStats(final Stats stats) {
        this.ensureNativeInstance();
        if (stats != null) {
            this.nativeGetStats(this.nativeInst, stats);
            return;
        }
        throw new NullPointerException("You're not supposed to pass null here");
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
    
    public void sendGroupCallKey(final byte[] array) {
        if (array == null) {
            throw new NullPointerException("key can not be null");
        }
        if (array.length == 256) {
            this.ensureNativeInstance();
            this.nativeSendGroupCallKey(this.nativeInst, array);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("key must be 256 bytes long, got ");
        sb.append(array.length);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setAudioOutputGainControlEnabled(final boolean b) {
        this.ensureNativeInstance();
        this.nativeSetAudioOutputGainControlEnabled(this.nativeInst, b);
    }
    
    public void setConfig(final double p0, final double p1, final int p2, final long p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/telegram/messenger/voip/VoIPController.ensureNativeInstance:()V
        //     4: getstatic       android/os/Build$VERSION.SDK_INT:I
        //     7: bipush          16
        //     9: if_icmplt       25
        //    12: invokestatic    android/media/audiofx/AcousticEchoCanceler.isAvailable:()Z
        //    15: istore          8
        //    17: invokestatic    android/media/audiofx/NoiseSuppressor.isAvailable:()Z
        //    20: istore          9
        //    22: goto            31
        //    25: iconst_0       
        //    26: istore          8
        //    28: iconst_0       
        //    29: istore          9
        //    31: invokestatic    org/telegram/messenger/MessagesController.getGlobalMainSettings:()Landroid/content/SharedPreferences;
        //    34: ldc_w           "dbg_dump_call_stats"
        //    37: iconst_0       
        //    38: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //    43: istore          10
        //    45: aload_0        
        //    46: getfield        org/telegram/messenger/voip/VoIPController.nativeInst:J
        //    49: lstore          11
        //    51: iload           8
        //    53: ifeq            75
        //    56: ldc_w           "use_system_aec"
        //    59: iconst_1       
        //    60: invokestatic    org/telegram/messenger/voip/VoIPServerConfig.getBoolean:(Ljava/lang/String;Z)Z
        //    63: ifne            69
        //    66: goto            75
        //    69: iconst_0       
        //    70: istore          8
        //    72: goto            78
        //    75: iconst_1       
        //    76: istore          8
        //    78: iload           9
        //    80: ifeq            102
        //    83: ldc_w           "use_system_ns"
        //    86: iconst_1       
        //    87: invokestatic    org/telegram/messenger/voip/VoIPServerConfig.getBoolean:(Ljava/lang/String;Z)Z
        //    90: ifne            96
        //    93: goto            102
        //    96: iconst_0       
        //    97: istore          9
        //    99: goto            105
        //   102: iconst_1       
        //   103: istore          9
        //   105: getstatic       org/telegram/messenger/BuildVars.DEBUG_VERSION:Z
        //   108: ifeq            151
        //   111: new             Ljava/lang/StringBuilder;
        //   114: dup            
        //   115: invokespecial   java/lang/StringBuilder.<init>:()V
        //   118: astore          13
        //   120: aload           13
        //   122: ldc_w           "voip"
        //   125: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   128: pop            
        //   129: aload           13
        //   131: lload           6
        //   133: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   136: pop            
        //   137: aload_0        
        //   138: aload           13
        //   140: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   143: invokespecial   org/telegram/messenger/voip/VoIPController.getLogFilePath:(Ljava/lang/String;)Ljava/lang/String;
        //   146: astore          13
        //   148: goto            159
        //   151: aload_0        
        //   152: lload           6
        //   154: invokespecial   org/telegram/messenger/voip/VoIPController.getLogFilePath:(J)Ljava/lang/String;
        //   157: astore          13
        //   159: getstatic       org/telegram/messenger/BuildVars.DEBUG_VERSION:Z
        //   162: ifeq            182
        //   165: iload           10
        //   167: ifeq            182
        //   170: aload_0        
        //   171: ldc_w           "voipStats"
        //   174: invokespecial   org/telegram/messenger/voip/VoIPController.getLogFilePath:(Ljava/lang/String;)Ljava/lang/String;
        //   177: astore          14
        //   179: goto            185
        //   182: aconst_null    
        //   183: astore          14
        //   185: aload_0        
        //   186: lload           11
        //   188: dload_1        
        //   189: dload_3        
        //   190: iload           5
        //   192: iload           8
        //   194: iload           9
        //   196: iconst_1       
        //   197: aload           13
        //   199: aload           14
        //   201: getstatic       org/telegram/messenger/BuildVars.DEBUG_VERSION:Z
        //   204: invokespecial   org/telegram/messenger/voip/VoIPController.nativeSetConfig:(JDDIZZZLjava/lang/String;Ljava/lang/String;Z)V
        //   207: return         
        //   208: astore          13
        //   210: goto            25
        //   213: astore          13
        //   215: goto            28
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  12     17     208    213    Ljava/lang/Throwable;
        //  17     22     213    218    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0025:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void setConnectionStateListener(final ConnectionStateListener listener) {
        this.listener = listener;
    }
    
    public void setEchoCancellationStrength(final int n) {
        this.ensureNativeInstance();
        this.nativeSetEchoCancellationStrength(this.nativeInst, n);
    }
    
    public void setEncryptionKey(final byte[] array, final boolean b) {
        if (array.length == 256) {
            this.ensureNativeInstance();
            this.nativeSetEncryptionKey(this.nativeInst, array, b);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("key length must be exactly 256 bytes but is ");
        sb.append(array.length);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setMicMute(final boolean b) {
        this.ensureNativeInstance();
        this.nativeSetMicMute(this.nativeInst, b);
    }
    
    public void setNetworkType(final int n) {
        this.ensureNativeInstance();
        this.nativeSetNetworkType(this.nativeInst, n);
    }
    
    public void setProxy(final String s, final int n, final String s2, final String s3) {
        this.ensureNativeInstance();
        if (s != null) {
            this.nativeSetProxy(this.nativeInst, s, n, s2, s3);
            return;
        }
        throw new NullPointerException("address can't be null");
    }
    
    public void setRemoteEndpoints(final TLRPC.TL_phoneConnection[] array, final boolean b, final boolean b2, final int n) {
        if (array.length != 0) {
            for (int i = 0; i < array.length; ++i) {
                final TLRPC.TL_phoneConnection tl_phoneConnection = array[i];
                final String ip = tl_phoneConnection.ip;
                if (ip == null || ip.length() == 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("endpoint ");
                    sb.append(tl_phoneConnection);
                    sb.append(" has empty/null ipv4");
                    throw new IllegalArgumentException(sb.toString());
                }
                final byte[] peer_tag = tl_phoneConnection.peer_tag;
                if (peer_tag != null && peer_tag.length != 16) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("endpoint ");
                    sb2.append(tl_phoneConnection);
                    sb2.append(" has peer_tag of wrong length");
                    throw new IllegalArgumentException(sb2.toString());
                }
            }
            this.ensureNativeInstance();
            this.nativeSetRemoteEndpoints(this.nativeInst, array, b, b2, n);
            return;
        }
        throw new IllegalArgumentException("endpoints size is 0");
    }
    
    public void start() {
        this.ensureNativeInstance();
        this.nativeStart(this.nativeInst);
    }
    
    public interface ConnectionStateListener
    {
        void onCallUpgradeRequestReceived();
        
        void onConnectionStateChanged(final int p0);
        
        void onGroupCallKeyReceived(final byte[] p0);
        
        void onGroupCallKeySent();
        
        void onSignalBarCountChanged(final int p0);
    }
    
    public static class Stats
    {
        public long bytesRecvdMobile;
        public long bytesRecvdWifi;
        public long bytesSentMobile;
        public long bytesSentWifi;
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Stats{bytesRecvdMobile=");
            sb.append(this.bytesRecvdMobile);
            sb.append(", bytesSentWifi=");
            sb.append(this.bytesSentWifi);
            sb.append(", bytesRecvdWifi=");
            sb.append(this.bytesRecvdWifi);
            sb.append(", bytesSentMobile=");
            sb.append(this.bytesSentMobile);
            sb.append('}');
            return sb.toString();
        }
    }
}
