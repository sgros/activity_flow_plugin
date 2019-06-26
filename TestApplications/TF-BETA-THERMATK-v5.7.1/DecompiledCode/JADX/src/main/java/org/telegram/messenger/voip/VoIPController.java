package org.telegram.messenger.voip;

import android.os.SystemClock;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.p004ui.Components.voip.VoIPHelper;
import org.telegram.tgnet.TLRPC.TL_phoneConnection;

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
    protected ConnectionStateListener listener;
    protected long nativeInst;

    public interface ConnectionStateListener {
        void onCallUpgradeRequestReceived();

        void onConnectionStateChanged(int i);

        void onGroupCallKeyReceived(byte[] bArr);

        void onGroupCallKeySent();

        void onSignalBarCountChanged(int i);
    }

    public static class Stats {
        public long bytesRecvdMobile;
        public long bytesRecvdWifi;
        public long bytesSentMobile;
        public long bytesSentWifi;

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Stats{bytesRecvdMobile=");
            stringBuilder.append(this.bytesRecvdMobile);
            stringBuilder.append(", bytesSentWifi=");
            stringBuilder.append(this.bytesSentWifi);
            stringBuilder.append(", bytesRecvdWifi=");
            stringBuilder.append(this.bytesRecvdWifi);
            stringBuilder.append(", bytesSentMobile=");
            stringBuilder.append(this.bytesSentMobile);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    public static native int getConnectionMaxLayer();

    private native void nativeConnect(long j);

    private native void nativeDebugCtl(long j, int i, int i2);

    private native String nativeGetDebugLog(long j);

    private native String nativeGetDebugString(long j);

    private native int nativeGetLastError(long j);

    private native int nativeGetPeerCapabilities(long j);

    private native long nativeGetPreferredRelayID(long j);

    private native void nativeGetStats(long j, Stats stats);

    private static native String nativeGetVersion();

    private native long nativeInit(String str);

    private static native boolean nativeNeedRate(long j);

    private native void nativeRelease(long j);

    private native void nativeRequestCallUpgrade(long j);

    private native void nativeSendGroupCallKey(long j, byte[] bArr);

    private native void nativeSetAudioOutputGainControlEnabled(long j, boolean z);

    private native void nativeSetConfig(long j, double d, double d2, int i, boolean z, boolean z2, boolean z3, String str, String str2, boolean z4);

    private native void nativeSetEchoCancellationStrength(long j, int i);

    private native void nativeSetEncryptionKey(long j, byte[] bArr, boolean z);

    private native void nativeSetMicMute(long j, boolean z);

    private static native void nativeSetNativeBufferSize(int i);

    private native void nativeSetNetworkType(long j, int i);

    private native void nativeSetProxy(long j, String str, int i, String str2, String str3);

    private native void nativeSetRemoteEndpoints(long j, TL_phoneConnection[] tL_phoneConnectionArr, boolean z, boolean z2, int i);

    public static native void nativeSetVideoRenderer(long j, long j2);

    public static native void nativeSetVideoSource(long j, long j2);

    private native void nativeStart(long j);

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x006c in {13, 15, 16, 18, 20, 22} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public void setRemoteEndpoints(org.telegram.tgnet.TLRPC.TL_phoneConnection[] r8, boolean r9, boolean r10, int r11) {
        /*
        r7 = this;
        r0 = r8.length;
        if (r0 == 0) goto L_0x0064;
        r0 = 0;
        r1 = r8.length;
        if (r0 >= r1) goto L_0x0056;
        r1 = r8[r0];
        r2 = r1.f524ip;
        r3 = "endpoint ";
        if (r2 == 0) goto L_0x003c;
        r2 = r2.length();
        if (r2 == 0) goto L_0x003c;
        r2 = r1.peer_tag;
        if (r2 == 0) goto L_0x0039;
        r2 = r2.length;
        r4 = 16;
        if (r2 != r4) goto L_0x001f;
        goto L_0x0039;
        r8 = new java.lang.IllegalArgumentException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r9.append(r3);
        r9.append(r1);
        r10 = " has peer_tag of wrong length";
        r9.append(r10);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
        r0 = r0 + 1;
        goto L_0x0004;
        r8 = new java.lang.IllegalArgumentException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r9.append(r3);
        r9.append(r1);
        r10 = " has empty/null ipv4";
        r9.append(r10);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
        r7.ensureNativeInstance();
        r1 = r7.nativeInst;
        r0 = r7;
        r3 = r8;
        r4 = r9;
        r5 = r10;
        r6 = r11;
        r0.nativeSetRemoteEndpoints(r1, r3, r4, r5, r6);
        return;
        r8 = new java.lang.IllegalArgumentException;
        r9 = "endpoints size is 0";
        r8.<init>(r9);
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.voip.VoIPController.setRemoteEndpoints(org.telegram.tgnet.TLRPC$TL_phoneConnection[], boolean, boolean, int):void");
    }

    public VoIPController() {
        this.nativeInst = 0;
        this.nativeInst = nativeInit(new File(ApplicationLoader.applicationContext.getFilesDir(), "voip_persistent_state.json").getAbsolutePath());
    }

    public void start() {
        ensureNativeInstance();
        nativeStart(this.nativeInst);
    }

    public void connect() {
        ensureNativeInstance();
        nativeConnect(this.nativeInst);
    }

    public void setEncryptionKey(byte[] bArr, boolean z) {
        if (bArr.length == 256) {
            ensureNativeInstance();
            nativeSetEncryptionKey(this.nativeInst, bArr, z);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("key length must be exactly 256 bytes but is ");
        stringBuilder.append(bArr.length);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static void setNativeBufferSize(int i) {
        nativeSetNativeBufferSize(i);
    }

    public void release() {
        ensureNativeInstance();
        nativeRelease(this.nativeInst);
        this.nativeInst = 0;
    }

    public String getDebugString() {
        ensureNativeInstance();
        return nativeGetDebugString(this.nativeInst);
    }

    /* Access modifiers changed, original: protected */
    public void ensureNativeInstance() {
        if (this.nativeInst == 0) {
            throw new IllegalStateException("Native instance is not valid");
        }
    }

    public void setConnectionStateListener(ConnectionStateListener connectionStateListener) {
        this.listener = connectionStateListener;
    }

    private void handleStateChange(int i) {
        if (i == 3 && this.callStartTime == 0) {
            this.callStartTime = SystemClock.elapsedRealtime();
        }
        ConnectionStateListener connectionStateListener = this.listener;
        if (connectionStateListener != null) {
            connectionStateListener.onConnectionStateChanged(i);
        }
    }

    private void handleSignalBarsChange(int i) {
        ConnectionStateListener connectionStateListener = this.listener;
        if (connectionStateListener != null) {
            connectionStateListener.onSignalBarCountChanged(i);
        }
    }

    private void groupCallKeyReceived(byte[] bArr) {
        ConnectionStateListener connectionStateListener = this.listener;
        if (connectionStateListener != null) {
            connectionStateListener.onGroupCallKeyReceived(bArr);
        }
    }

    private void groupCallKeySent() {
        ConnectionStateListener connectionStateListener = this.listener;
        if (connectionStateListener != null) {
            connectionStateListener.onGroupCallKeySent();
        }
    }

    private void callUpgradeRequestReceived() {
        ConnectionStateListener connectionStateListener = this.listener;
        if (connectionStateListener != null) {
            connectionStateListener.onCallUpgradeRequestReceived();
        }
    }

    public void setNetworkType(int i) {
        ensureNativeInstance();
        nativeSetNetworkType(this.nativeInst, i);
    }

    public long getCallDuration() {
        return SystemClock.elapsedRealtime() - this.callStartTime;
    }

    public void setMicMute(boolean z) {
        ensureNativeInstance();
        nativeSetMicMute(this.nativeInst, z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0047  */
    public void setConfig(double r17, double r19, int r21, long r22) {
        /*
        r16 = this;
        r14 = r16;
        r0 = r22;
        r16.ensureNativeInstance();
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 0;
        r4 = 16;
        if (r2 < r4) goto L_0x0017;
    L_0x000e:
        r2 = android.media.audiofx.AcousticEchoCanceler.isAvailable();	 Catch:{ Throwable -> 0x0017 }
        r4 = android.media.audiofx.NoiseSuppressor.isAvailable();	 Catch:{ Throwable -> 0x0018 }
        goto L_0x0019;
    L_0x0017:
        r2 = 0;
    L_0x0018:
        r4 = 0;
    L_0x0019:
        r5 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r6 = "dbg_dump_call_stats";
        r5 = r5.getBoolean(r6, r3);
        r6 = r14.nativeInst;
        r8 = 1;
        if (r2 == 0) goto L_0x0033;
    L_0x0028:
        r2 = "use_system_aec";
        r2 = org.telegram.messenger.voip.VoIPServerConfig.getBoolean(r2, r8);
        if (r2 != 0) goto L_0x0031;
    L_0x0030:
        goto L_0x0033;
    L_0x0031:
        r9 = 0;
        goto L_0x0034;
    L_0x0033:
        r9 = 1;
    L_0x0034:
        if (r4 == 0) goto L_0x0041;
    L_0x0036:
        r2 = "use_system_ns";
        r2 = org.telegram.messenger.voip.VoIPServerConfig.getBoolean(r2, r8);
        if (r2 != 0) goto L_0x003f;
    L_0x003e:
        goto L_0x0041;
    L_0x003f:
        r10 = 0;
        goto L_0x0042;
    L_0x0041:
        r10 = 1;
    L_0x0042:
        r11 = 1;
        r2 = org.telegram.messenger.BuildVars.DEBUG_VERSION;
        if (r2 == 0) goto L_0x005d;
    L_0x0047:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "voip";
        r2.append(r3);
        r2.append(r0);
        r0 = r2.toString();
        r0 = r14.getLogFilePath(r0);
        goto L_0x0061;
    L_0x005d:
        r0 = r14.getLogFilePath(r0);
    L_0x0061:
        r12 = r0;
        r0 = org.telegram.messenger.BuildVars.DEBUG_VERSION;
        if (r0 == 0) goto L_0x006f;
    L_0x0066:
        if (r5 == 0) goto L_0x006f;
    L_0x0068:
        r0 = "voipStats";
        r0 = r14.getLogFilePath(r0);
        goto L_0x0070;
    L_0x006f:
        r0 = 0;
    L_0x0070:
        r13 = r0;
        r15 = org.telegram.messenger.BuildVars.DEBUG_VERSION;
        r0 = r16;
        r1 = r6;
        r3 = r17;
        r5 = r19;
        r7 = r21;
        r8 = r9;
        r9 = r10;
        r10 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r15;
        r0.nativeSetConfig(r1, r3, r5, r7, r8, r9, r10, r11, r12, r13);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.voip.VoIPController.setConfig(double, double, int, long):void");
    }

    public void debugCtl(int i, int i2) {
        ensureNativeInstance();
        nativeDebugCtl(this.nativeInst, i, i2);
    }

    public long getPreferredRelayID() {
        ensureNativeInstance();
        return nativeGetPreferredRelayID(this.nativeInst);
    }

    public int getLastError() {
        ensureNativeInstance();
        return nativeGetLastError(this.nativeInst);
    }

    public void getStats(Stats stats) {
        ensureNativeInstance();
        if (stats != null) {
            nativeGetStats(this.nativeInst, stats);
            return;
        }
        throw new NullPointerException("You're not supposed to pass null here");
    }

    public static String getVersion() {
        return nativeGetVersion();
    }

    private String getLogFilePath(String str) {
        Calendar instance = Calendar.getInstance();
        return new File(ApplicationLoader.applicationContext.getExternalFilesDir(null), String.format(Locale.US, "logs/%02d_%02d_%04d_%02d_%02d_%02d_%s.txt", new Object[]{Integer.valueOf(instance.get(5)), Integer.valueOf(instance.get(2) + 1), Integer.valueOf(instance.get(1)), Integer.valueOf(instance.get(11)), Integer.valueOf(instance.get(12)), Integer.valueOf(instance.get(13)), str})).getAbsolutePath();
    }

    private String getLogFilePath(long j) {
        File logsDir = VoIPHelper.getLogsDir();
        String str = ".log";
        if (!BuildVars.DEBUG_VERSION) {
            File[] listFiles = logsDir.listFiles();
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(listFiles));
            while (arrayList.size() > 20) {
                File file = (File) arrayList.get(0);
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    File file2 = (File) it.next();
                    if (file2.getName().endsWith(str) && file2.lastModified() < file.lastModified()) {
                        file = file2;
                    }
                }
                file.delete();
                arrayList.remove(file);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(j);
        stringBuilder.append(str);
        return new File(logsDir, stringBuilder.toString()).getAbsolutePath();
    }

    public String getDebugLog() {
        ensureNativeInstance();
        return nativeGetDebugLog(this.nativeInst);
    }

    public void setProxy(String str, int i, String str2, String str3) {
        ensureNativeInstance();
        if (str != null) {
            nativeSetProxy(this.nativeInst, str, i, str2, str3);
            return;
        }
        throw new NullPointerException("address can't be null");
    }

    public void setAudioOutputGainControlEnabled(boolean z) {
        ensureNativeInstance();
        nativeSetAudioOutputGainControlEnabled(this.nativeInst, z);
    }

    public int getPeerCapabilities() {
        ensureNativeInstance();
        return nativeGetPeerCapabilities(this.nativeInst);
    }

    public void sendGroupCallKey(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("key can not be null");
        } else if (bArr.length == 256) {
            ensureNativeInstance();
            nativeSendGroupCallKey(this.nativeInst, bArr);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("key must be 256 bytes long, got ");
            stringBuilder.append(bArr.length);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public void requestCallUpgrade() {
        ensureNativeInstance();
        nativeRequestCallUpgrade(this.nativeInst);
    }

    public void setEchoCancellationStrength(int i) {
        ensureNativeInstance();
        nativeSetEchoCancellationStrength(this.nativeInst, i);
    }

    public boolean needRate() {
        ensureNativeInstance();
        return nativeNeedRate(this.nativeInst);
    }
}