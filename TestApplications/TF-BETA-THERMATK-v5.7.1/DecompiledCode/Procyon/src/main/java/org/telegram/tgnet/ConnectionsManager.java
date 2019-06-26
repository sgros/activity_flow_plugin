// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

import java.util.ArrayList;
import org.telegram.messenger.ContactsController;
import android.content.SharedPreferences;
import android.annotation.SuppressLint;
import java.net.InetAddress;
import java.util.List;
import java.util.Enumeration;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import android.text.TextUtils;
import org.telegram.messenger.KeepAliveJob;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import android.os.SystemClock;
import android.content.pm.PackageInfo;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.UserConfig;
import android.os.Build$VERSION;
import android.os.Build;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import java.io.File;
import org.telegram.messenger.ApplicationLoader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import android.os.AsyncTask;

public class ConnectionsManager
{
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
    private static volatile ConnectionsManager[] Instance;
    public static final int RequestFlagCanCompress = 4;
    public static final int RequestFlagEnableUnauthorized = 1;
    public static final int RequestFlagFailOnServerErrors = 2;
    public static final int RequestFlagForceDownload = 32;
    public static final int RequestFlagInvokeAfter = 64;
    public static final int RequestFlagNeedQuickAck = 128;
    public static final int RequestFlagTryDifferentDc = 16;
    public static final int RequestFlagWithoutLogin = 8;
    private static AsyncTask currentTask;
    private static ConcurrentHashMap<String, ResolvedDomain> dnsCache;
    private static int lastClassGuid;
    private static long lastDnsRequestTime;
    private static HashMap<String, ResolveHostByNameTask> resolvingHostnameTasks;
    private boolean appPaused;
    private int appResumeCount;
    private int connectionState;
    private int currentAccount;
    private boolean isUpdating;
    private long lastPauseTime;
    private AtomicInteger lastRequestToken;
    
    static {
        ConnectionsManager.resolvingHostnameTasks = new HashMap<String, ResolveHostByNameTask>();
        ConnectionsManager.dnsCache = new ConcurrentHashMap<String, ResolvedDomain>();
        ConnectionsManager.lastClassGuid = 1;
        ConnectionsManager.Instance = new ConnectionsManager[3];
    }
    
    public ConnectionsManager(final int n) {
        this.lastPauseTime = System.currentTimeMillis();
        this.appPaused = true;
        this.lastRequestToken = new AtomicInteger(1);
        this.currentAccount = n;
        this.connectionState = native_getConnectionState(this.currentAccount);
        File filesDirFixed;
        final File parent = filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("account");
            sb.append(n);
            filesDirFixed = new File(parent, sb.toString());
            filesDirFixed.mkdirs();
        }
        final String string = filesDirFixed.toString();
        final boolean boolean1 = MessagesController.getGlobalNotificationsSettings().getBoolean("pushConnection", true);
        String lowerCase;
        String lowerCase2;
        String string2;
        String string3;
        String s;
        try {
            lowerCase = LocaleController.getSystemLocaleStringIso639().toLowerCase();
            lowerCase2 = LocaleController.getLocaleStringIso639().toLowerCase();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Build.MANUFACTURER);
            sb2.append(Build.MODEL);
            string2 = sb2.toString();
            final PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(packageInfo.versionName);
            sb3.append(" (");
            sb3.append(packageInfo.versionCode);
            sb3.append(")");
            string3 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("SDK ");
            sb4.append(Build$VERSION.SDK_INT);
            s = sb4.toString();
        }
        catch (Exception ex) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("SDK ");
            sb5.append(Build$VERSION.SDK_INT);
            s = sb5.toString();
            lowerCase2 = "";
            string3 = "App version unknown";
            string2 = "Android unknown";
            lowerCase = "en";
        }
        if (lowerCase.trim().length() == 0) {
            lowerCase = "en";
        }
        if (string2.trim().length() == 0) {
            string2 = "Android unknown";
        }
        if (string3.trim().length() == 0) {
            string3 = "App version unknown";
        }
        if (s.trim().length() == 0) {
            s = "SDK Unknown";
        }
        UserConfig.getInstance(this.currentAccount).loadConfig();
        this.init(BuildVars.BUILD_VERSION, 100, BuildVars.APP_ID, string2, s, string3, lowerCase2, lowerCase, string, FileLog.getNetworkLogPath(), UserConfig.getInstance(this.currentAccount).getClientUserId(), boolean1);
    }
    
    public static int generateClassGuid() {
        final int lastClassGuid = ConnectionsManager.lastClassGuid;
        ConnectionsManager.lastClassGuid = lastClassGuid + 1;
        return lastClassGuid;
    }
    
    public static void getHostByName(final String key, final long n) {
        final ResolvedDomain resolvedDomain = ConnectionsManager.dnsCache.get(key);
        if (resolvedDomain != null && SystemClock.elapsedRealtime() - resolvedDomain.ttl < 300000L) {
            native_onHostNameResolved(key, n, resolvedDomain.getAddress());
        }
        else {
            ResolveHostByNameTask value;
            if ((value = ConnectionsManager.resolvingHostnameTasks.get(key)) == null) {
                value = new ResolveHostByNameTask(key);
                value.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                ConnectionsManager.resolvingHostnameTasks.put(key, value);
            }
            value.addAddress(n);
        }
    }
    
    public static int getInitFlags() {
        return 0;
    }
    
    public static ConnectionsManager getInstance(final int n) {
        final ConnectionsManager connectionsManager;
        if ((connectionsManager = ConnectionsManager.Instance[n]) == null) {
            synchronized (ConnectionsManager.class) {
                if (ConnectionsManager.Instance[n] == null) {
                    ConnectionsManager.Instance[n] = new ConnectionsManager(n);
                }
            }
        }
        return connectionsManager;
    }
    
    public static native void native_applyDatacenterAddress(final int p0, final int p1, final String p2, final int p3);
    
    public static native void native_applyDnsConfig(final int p0, final long p1, final String p2);
    
    public static native void native_bindRequestToGuid(final int p0, final int p1, final int p2);
    
    public static native void native_cancelRequest(final int p0, final int p1, final boolean p2);
    
    public static native void native_cancelRequestsForGuid(final int p0, final int p1);
    
    public static native long native_checkProxy(final int p0, final String p1, final int p2, final String p3, final String p4, final String p5, final RequestTimeDelegate p6);
    
    public static native void native_cleanUp(final int p0, final boolean p1);
    
    public static native int native_getConnectionState(final int p0);
    
    public static native int native_getCurrentTime(final int p0);
    
    public static native long native_getCurrentTimeMillis(final int p0);
    
    public static native int native_getTimeDifference(final int p0);
    
    public static native void native_init(final int p0, final int p1, final int p2, final int p3, final String p4, final String p5, final String p6, final String p7, final String p8, final String p9, final String p10, final int p11, final boolean p12, final boolean p13, final int p14);
    
    public static native int native_isTestBackend(final int p0);
    
    public static native void native_onHostNameResolved(final String p0, final long p1, final String p2);
    
    public static native void native_pauseNetwork(final int p0);
    
    public static native void native_resumeNetwork(final int p0, final boolean p1);
    
    public static native void native_seSystemLangCode(final int p0, final String p1);
    
    public static native void native_sendRequest(final int p0, final long p1, final RequestDelegateInternal p2, final QuickAckDelegate p3, final WriteToSocketDelegate p4, final int p5, final int p6, final int p7, final boolean p8, final int p9);
    
    public static native void native_setJava(final boolean p0);
    
    public static native void native_setLangCode(final int p0, final String p1);
    
    public static native void native_setNetworkAvailable(final int p0, final boolean p1, final int p2, final boolean p3);
    
    public static native void native_setProxySettings(final int p0, final String p1, final int p2, final String p3, final String p4, final String p5);
    
    public static native void native_setPushConnectionEnabled(final int p0, final boolean p1);
    
    public static native void native_setSystemLangCode(final int p0, final String p1);
    
    public static native void native_setUseIpv6(final int p0, final boolean p1);
    
    public static native void native_setUserId(final int p0, final int p1);
    
    public static native void native_switchBackend(final int p0);
    
    public static native void native_updateDcSettings(final int p0);
    
    public static void onBytesReceived(final int n, final int n2, final int n3) {
        try {
            StatsController.getInstance(n3).incrementReceivedBytesCount(n2, 6, n);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void onBytesSent(final int n, final int n2, final int n3) {
        try {
            StatsController.getInstance(n3).incrementSentBytesCount(n2, 6, n);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void onConnectionStateChanged(final int n, final int n2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ConnectionsManager$wMpd1_zDWgiLp6x8fjImjIX349A(n2, n));
    }
    
    public static void onInternalPushReceived(final int n) {
        KeepAliveJob.startJob();
    }
    
    public static void onLogout(final int n) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ConnectionsManager$WUMuAbrjc16kWFnK3JYXcq2eB4E(n));
    }
    
    public static void onProxyError() {
        AndroidUtilities.runOnUIThread((Runnable)_$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU.INSTANCE);
    }
    
    public static void onRequestNewServerIpAndPort(final int n, final int n2) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$Vntp1UzbcZLxSUJY5_RFJvLOrY4(n, n2));
    }
    
    public static void onSessionCreated(final int n) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$c_kbk6lmCmsTzziA11WOyMsJtqY(n));
    }
    
    public static void onUnparsedMessageReceived(final long n, final int n2) {
        try {
            final NativeByteBuffer wrap = NativeByteBuffer.wrap(n);
            wrap.reused = true;
            final int int32 = wrap.readInt32(true);
            final TLObject tLdeserialize = TLClassStore.Instance().TLdeserialize(wrap, int32, true);
            if (tLdeserialize instanceof TLRPC.Updates) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("java received ");
                    sb.append(tLdeserialize);
                    FileLog.d(sb.toString());
                }
                KeepAliveJob.finishJob();
                Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$R5V1iXmwj8PWON_tb_jcTaBhzJo(n2, tLdeserialize));
            }
            else if (BuildVars.LOGS_ENABLED) {
                FileLog.d(String.format("java received unknown constructor 0x%x", int32));
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void onUpdate(final int n) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$GiXNWNTneL61N5XH1sI4IVkif4k(n));
    }
    
    public static void onUpdateConfig(final long n, final int n2) {
        try {
            final NativeByteBuffer wrap = NativeByteBuffer.wrap(n);
            wrap.reused = true;
            final TLRPC.TL_config tLdeserialize = TLRPC.TL_config.TLdeserialize(wrap, wrap.readInt32(true), true);
            if (tLdeserialize != null) {
                Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2_hJ1zU(n2, tLdeserialize));
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void setLangCode(String lowerCase) {
        lowerCase = lowerCase.replace('_', '-').toLowerCase();
        for (int i = 0; i < 3; ++i) {
            native_setLangCode(i, lowerCase);
        }
    }
    
    public static void setProxySettings(final boolean b, String s, final int n, String s2, String s3, final String s4) {
        String s5 = s;
        if (s == null) {
            s5 = "";
        }
        if ((s = s2) == null) {
            s = "";
        }
        if ((s2 = s3) == null) {
            s2 = "";
        }
        if ((s3 = s4) == null) {
            s3 = "";
        }
        for (int i = 0; i < 3; ++i) {
            if (b && !TextUtils.isEmpty((CharSequence)s5)) {
                native_setProxySettings(i, s5, n, s, s2, s3);
            }
            else {
                native_setProxySettings(i, "", 1080, "", "", "");
            }
            if (UserConfig.getInstance(i).isClientActivated()) {
                MessagesController.getInstance(i).checkProxyInfo(true);
            }
        }
    }
    
    public static void setSystemLangCode(String lowerCase) {
        lowerCase = lowerCase.replace('_', '-').toLowerCase();
        for (int i = 0; i < 3; ++i) {
            native_setSystemLangCode(i, lowerCase);
        }
    }
    
    @SuppressLint({ "NewApi" })
    protected static boolean useIpv6Address() {
        if (Build$VERSION.SDK_INT < 19) {
            return false;
        }
        if (BuildVars.LOGS_ENABLED) {
            try {
                final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    final NetworkInterface obj = networkInterfaces.nextElement();
                    if (obj.isUp() && !obj.isLoopback()) {
                        if (obj.getInterfaceAddresses().isEmpty()) {
                            continue;
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("valid interface: ");
                            sb.append(obj);
                            FileLog.d(sb.toString());
                        }
                        final List<InterfaceAddress> interfaceAddresses = obj.getInterfaceAddresses();
                        for (int i = 0; i < interfaceAddresses.size(); ++i) {
                            final InetAddress address = interfaceAddresses.get(i).getAddress();
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("address: ");
                                sb2.append(address.getHostAddress());
                                FileLog.d(sb2.toString());
                            }
                            if (!address.isLinkLocalAddress() && !address.isLoopbackAddress()) {
                                if (!address.isMulticastAddress()) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("address is good");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        try {
            final Enumeration<NetworkInterface> networkInterfaces2 = NetworkInterface.getNetworkInterfaces();
            int n = 0;
            int n2 = 0;
            while (networkInterfaces2.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces2.nextElement();
                if (networkInterface.isUp()) {
                    if (networkInterface.isLoopback()) {
                        continue;
                    }
                    final List<InterfaceAddress> interfaceAddresses2 = networkInterface.getInterfaceAddresses();
                    final int n3 = n;
                    final int n4 = 0;
                    int n5 = n2;
                    int n6 = n3;
                    int n7;
                    int n8;
                    for (int j = n4; j < interfaceAddresses2.size(); ++j, n6 = n7, n5 = n8) {
                        final InetAddress address2 = interfaceAddresses2.get(j).getAddress();
                        n7 = n6;
                        n8 = n5;
                        if (!address2.isLinkLocalAddress()) {
                            n7 = n6;
                            n8 = n5;
                            if (!address2.isLoopbackAddress()) {
                                if (address2.isMulticastAddress()) {
                                    n7 = n6;
                                    n8 = n5;
                                }
                                else if (address2 instanceof Inet6Address) {
                                    n8 = 1;
                                    n7 = n6;
                                }
                                else {
                                    n7 = n6;
                                    n8 = n5;
                                    if (address2 instanceof Inet4Address) {
                                        final boolean startsWith = address2.getHostAddress().startsWith("192.0.0.");
                                        n7 = n6;
                                        n8 = n5;
                                        if (!startsWith) {
                                            n7 = 1;
                                            n8 = n5;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    final int n9 = n5;
                    n = n6;
                    n2 = n9;
                }
            }
            if (n == 0 && n2 != 0) {
                return true;
            }
        }
        catch (Throwable t2) {
            FileLog.e(t2);
        }
        return false;
    }
    
    public void applyDatacenterAddress(final int n, final String s, final int n2) {
        native_applyDatacenterAddress(this.currentAccount, n, s, n2);
    }
    
    public void bindRequestToGuid(final int n, final int n2) {
        native_bindRequestToGuid(this.currentAccount, n, n2);
    }
    
    public void cancelRequest(final int n, final boolean b) {
        native_cancelRequest(this.currentAccount, n, b);
    }
    
    public void cancelRequestsForGuid(final int n) {
        native_cancelRequestsForGuid(this.currentAccount, n);
    }
    
    public void checkConnection() {
        native_setUseIpv6(this.currentAccount, useIpv6Address());
        native_setNetworkAvailable(this.currentAccount, ApplicationLoader.isNetworkOnline(), ApplicationLoader.getCurrentNetworkType(), ApplicationLoader.isConnectionSlow());
    }
    
    public long checkProxy(String s, final int n, String s2, String s3, String s4, final RequestTimeDelegate requestTimeDelegate) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return 0L;
        }
        if (s == null) {
            s = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        if (s3 == null) {
            s3 = "";
        }
        if (s4 == null) {
            s4 = "";
        }
        return native_checkProxy(this.currentAccount, s, n, s2, s3, s4, requestTimeDelegate);
    }
    
    public void cleanup(final boolean b) {
        native_cleanUp(this.currentAccount, b);
    }
    
    public int getConnectionState() {
        if (this.connectionState == 3 && this.isUpdating) {
            return 5;
        }
        return this.connectionState;
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
    
    public void init(final int n, final int n2, final int n3, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final int n4, final boolean b) {
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        final String string = sharedPreferences.getString("proxy_ip", "");
        final String string2 = sharedPreferences.getString("proxy_user", "");
        final String string3 = sharedPreferences.getString("proxy_pass", "");
        final String string4 = sharedPreferences.getString("proxy_secret", "");
        final int int1 = sharedPreferences.getInt("proxy_port", 1080);
        if (sharedPreferences.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty((CharSequence)string)) {
            native_setProxySettings(this.currentAccount, string, int1, string2, string3, string4);
        }
        native_init(this.currentAccount, n, n2, n3, s, s2, s3, s4, s5, s6, s7, n4, b, ApplicationLoader.isNetworkOnline(), ApplicationLoader.getCurrentNetworkType());
        this.checkConnection();
    }
    
    public void resumeNetworkMaybe() {
        native_resumeNetwork(this.currentAccount, true);
    }
    
    public int sendRequest(final TLObject tlObject, final RequestDelegate requestDelegate) {
        return this.sendRequest(tlObject, requestDelegate, null, 0);
    }
    
    public int sendRequest(final TLObject tlObject, final RequestDelegate requestDelegate, final int n) {
        return this.sendRequest(tlObject, requestDelegate, null, null, n, Integer.MAX_VALUE, 1, true);
    }
    
    public int sendRequest(final TLObject tlObject, final RequestDelegate requestDelegate, final int n, final int n2) {
        return this.sendRequest(tlObject, requestDelegate, null, null, n, Integer.MAX_VALUE, n2, true);
    }
    
    public int sendRequest(final TLObject tlObject, final RequestDelegate requestDelegate, final QuickAckDelegate quickAckDelegate, final int n) {
        return this.sendRequest(tlObject, requestDelegate, quickAckDelegate, null, n, Integer.MAX_VALUE, 1, true);
    }
    
    public int sendRequest(final TLObject tlObject, final RequestDelegate requestDelegate, final QuickAckDelegate quickAckDelegate, final WriteToSocketDelegate writeToSocketDelegate, final int n, final int n2, final int n3, final boolean b) {
        final int andIncrement = this.lastRequestToken.getAndIncrement();
        Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$csmhNL7gP4ZbIN5_kTApilG8kBQ(this, tlObject, andIncrement, requestDelegate, quickAckDelegate, writeToSocketDelegate, n, n2, n3, b));
        return andIncrement;
    }
    
    public void setAppPaused(final boolean b, final boolean b2) {
        if (!b2) {
            this.appPaused = b;
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("app paused = ");
                sb.append(b);
                FileLog.d(sb.toString());
            }
            if (b) {
                --this.appResumeCount;
            }
            else {
                ++this.appResumeCount;
            }
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("app resume count ");
                sb2.append(this.appResumeCount);
                FileLog.d(sb2.toString());
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
        }
        else {
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
    
    public void setIsUpdating(final boolean b) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ConnectionsManager$_MqDe5Su_gCe6Qmn9_j7mUamUVo(this, b));
    }
    
    public void setPushConnectionEnabled(final boolean b) {
        native_setPushConnectionEnabled(this.currentAccount, b);
    }
    
    public void setUserId(final int n) {
        native_setUserId(this.currentAccount, n);
    }
    
    public void switchBackend() {
        MessagesController.getGlobalMainSettings().edit().remove("language_showed2").commit();
        native_switchBackend(this.currentAccount);
    }
    
    public void updateDcSettings() {
        native_updateDcSettings(this.currentAccount);
    }
    
    private static class AzureLoadTask extends AsyncTask<Void, Void, NativeByteBuffer>
    {
        private int currentAccount;
        
        public AzureLoadTask(final int currentAccount) {
            this.currentAccount = currentAccount;
        }
        
        protected NativeByteBuffer doInBackground(final Void... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: astore_2       
            //     2: aload_0        
            //     3: getfield        org/telegram/tgnet/ConnectionsManager$AzureLoadTask.currentAccount:I
            //     6: invokestatic    org/telegram/tgnet/ConnectionsManager.native_isTestBackend:(I)I
            //     9: ifeq            25
            //    12: new             Ljava/net/URL;
            //    15: astore_1       
            //    16: aload_1        
            //    17: ldc             "https://software-download.microsoft.com/testv2/config.txt"
            //    19: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //    22: goto            35
            //    25: new             Ljava/net/URL;
            //    28: dup            
            //    29: ldc             "https://software-download.microsoft.com/prodv2/config.txt"
            //    31: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //    34: astore_1       
            //    35: aload_1        
            //    36: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //    39: astore_1       
            //    40: aload_1        
            //    41: ldc             "User-Agent"
            //    43: ldc             "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1"
            //    45: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //    48: aload_1        
            //    49: ldc             "Host"
            //    51: ldc             "tcdnb.azureedge.net"
            //    53: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //    56: aload_1        
            //    57: sipush          5000
            //    60: invokevirtual   java/net/URLConnection.setConnectTimeout:(I)V
            //    63: aload_1        
            //    64: sipush          5000
            //    67: invokevirtual   java/net/URLConnection.setReadTimeout:(I)V
            //    70: aload_1        
            //    71: invokevirtual   java/net/URLConnection.connect:()V
            //    74: aload_1        
            //    75: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
            //    78: astore_1       
            //    79: new             Ljava/io/ByteArrayOutputStream;
            //    82: astore_3       
            //    83: aload_3        
            //    84: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
            //    87: ldc             32768
            //    89: newarray        B
            //    91: astore_2       
            //    92: aload_0        
            //    93: invokevirtual   android/os/AsyncTask.isCancelled:()Z
            //    96: ifeq            102
            //    99: goto            125
            //   102: aload_1        
            //   103: aload_2        
            //   104: invokevirtual   java/io/InputStream.read:([B)I
            //   107: istore          4
            //   109: iload           4
            //   111: ifle            125
            //   114: aload_3        
            //   115: aload_2        
            //   116: iconst_0       
            //   117: iload           4
            //   119: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
            //   122: goto            92
            //   125: aload_3        
            //   126: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
            //   129: iconst_0       
            //   130: invokestatic    android/util/Base64.decode:([BI)[B
            //   133: astore          5
            //   135: new             Lorg/telegram/tgnet/NativeByteBuffer;
            //   138: astore_2       
            //   139: aload_2        
            //   140: aload           5
            //   142: arraylength    
            //   143: invokespecial   org/telegram/tgnet/NativeByteBuffer.<init>:(I)V
            //   146: aload_2        
            //   147: aload           5
            //   149: invokevirtual   org/telegram/tgnet/NativeByteBuffer.writeBytes:([B)V
            //   152: aload_1        
            //   153: ifnull          168
            //   156: aload_1        
            //   157: invokevirtual   java/io/InputStream.close:()V
            //   160: goto            168
            //   163: astore_1       
            //   164: aload_1        
            //   165: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   168: aload_3        
            //   169: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   172: aload_2        
            //   173: areturn        
            //   174: astore          5
            //   176: aload_1        
            //   177: astore_2       
            //   178: aload           5
            //   180: astore_1       
            //   181: goto            270
            //   184: astore_2       
            //   185: aload_3        
            //   186: astore          5
            //   188: aload_1        
            //   189: astore_3       
            //   190: aload           5
            //   192: astore_1       
            //   193: goto            231
            //   196: astore          5
            //   198: aconst_null    
            //   199: astore_3       
            //   200: aload_1        
            //   201: astore_2       
            //   202: aload           5
            //   204: astore_1       
            //   205: goto            270
            //   208: astore_2       
            //   209: aconst_null    
            //   210: astore          5
            //   212: aload_1        
            //   213: astore_3       
            //   214: aload           5
            //   216: astore_1       
            //   217: goto            231
            //   220: astore_1       
            //   221: aconst_null    
            //   222: astore_3       
            //   223: goto            270
            //   226: astore_2       
            //   227: aconst_null    
            //   228: astore_3       
            //   229: aload_3        
            //   230: astore_1       
            //   231: aload_2        
            //   232: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   235: aload_3        
            //   236: ifnull          251
            //   239: aload_3        
            //   240: invokevirtual   java/io/InputStream.close:()V
            //   243: goto            251
            //   246: astore_3       
            //   247: aload_3        
            //   248: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   251: aload_1        
            //   252: ifnull          259
            //   255: aload_1        
            //   256: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   259: aconst_null    
            //   260: areturn        
            //   261: astore          5
            //   263: aload_3        
            //   264: astore_2       
            //   265: aload_1        
            //   266: astore_3       
            //   267: aload           5
            //   269: astore_1       
            //   270: aload_2        
            //   271: ifnull          286
            //   274: aload_2        
            //   275: invokevirtual   java/io/InputStream.close:()V
            //   278: goto            286
            //   281: astore_2       
            //   282: aload_2        
            //   283: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   286: aload_3        
            //   287: ifnull          294
            //   290: aload_3        
            //   291: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   294: aload_1        
            //   295: athrow         
            //   296: astore_1       
            //   297: goto            172
            //   300: astore_1       
            //   301: goto            259
            //   304: astore_3       
            //   305: goto            294
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  2      22     226    231    Ljava/lang/Throwable;
            //  2      22     220    226    Any
            //  25     35     226    231    Ljava/lang/Throwable;
            //  25     35     220    226    Any
            //  35     79     226    231    Ljava/lang/Throwable;
            //  35     79     220    226    Any
            //  79     87     208    220    Ljava/lang/Throwable;
            //  79     87     196    208    Any
            //  87     92     184    196    Ljava/lang/Throwable;
            //  87     92     174    184    Any
            //  92     99     184    196    Ljava/lang/Throwable;
            //  92     99     174    184    Any
            //  102    109    184    196    Ljava/lang/Throwable;
            //  102    109    174    184    Any
            //  114    122    184    196    Ljava/lang/Throwable;
            //  114    122    174    184    Any
            //  125    152    184    196    Ljava/lang/Throwable;
            //  125    152    174    184    Any
            //  156    160    163    168    Ljava/lang/Throwable;
            //  168    172    296    300    Ljava/lang/Exception;
            //  231    235    261    270    Any
            //  239    243    246    251    Ljava/lang/Throwable;
            //  255    259    300    304    Ljava/lang/Exception;
            //  274    278    281    286    Ljava/lang/Throwable;
            //  290    294    304    308    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 170 out-of-bounds for length 170
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        protected void onPostExecute(final NativeByteBuffer nativeByteBuffer) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$AzureLoadTask$CCvFvz5lAUpDF3DaGVJItVYIMOk(this, nativeByteBuffer));
        }
    }
    
    private static class DnsTxtLoadTask extends AsyncTask<Void, Void, NativeByteBuffer>
    {
        private int currentAccount;
        
        public DnsTxtLoadTask(final int currentAccount) {
            this.currentAccount = currentAccount;
        }
        
        protected NativeByteBuffer doInBackground(final Void... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: astore_2       
            //     2: aload_2        
            //     3: astore_1       
            //     4: iconst_0       
            //     5: istore_3       
            //     6: iload_3        
            //     7: iconst_3       
            //     8: if_icmpge       837
            //    11: iload_3        
            //    12: ifne            44
            //    15: ldc             "www.google.com"
            //    17: astore          4
            //    19: goto            60
            //    22: astore_2       
            //    23: aload           5
            //    25: astore_1       
            //    26: aload_2        
            //    27: astore          5
            //    29: goto            808
            //    32: astore          5
            //    34: aload           6
            //    36: astore_1       
            //    37: aload           5
            //    39: astore          6
            //    41: goto            765
            //    44: iload_3        
            //    45: iconst_1       
            //    46: if_icmpne       56
            //    49: ldc             "www.google.ru"
            //    51: astore          4
            //    53: goto            60
            //    56: ldc             "google.com"
            //    58: astore          4
            //    60: aload_2        
            //    61: astore          7
            //    63: aload_1        
            //    64: astore          5
            //    66: aload_1        
            //    67: astore          6
            //    69: aload_0        
            //    70: getfield        org/telegram/tgnet/ConnectionsManager$DnsTxtLoadTask.currentAccount:I
            //    73: invokestatic    org/telegram/tgnet/ConnectionsManager.native_isTestBackend:(I)I
            //    76: ifeq            86
            //    79: ldc             "tapv2.stel.com"
            //    81: astore          8
            //    83: goto            107
            //    86: aload_2        
            //    87: astore          7
            //    89: aload_1        
            //    90: astore          5
            //    92: aload_1        
            //    93: astore          6
            //    95: aload_0        
            //    96: getfield        org/telegram/tgnet/ConnectionsManager$DnsTxtLoadTask.currentAccount:I
            //    99: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
            //   102: getfield        org/telegram/messenger/MessagesController.dcDomainName:Ljava/lang/String;
            //   105: astore          8
            //   107: aload_2        
            //   108: astore          7
            //   110: aload_1        
            //   111: astore          5
            //   113: aload_1        
            //   114: astore          6
            //   116: getstatic       org/telegram/messenger/Utilities.random:Ljava/security/SecureRandom;
            //   119: bipush          116
            //   121: invokevirtual   java/security/SecureRandom.nextInt:(I)I
            //   124: bipush          13
            //   126: iadd           
            //   127: istore          9
            //   129: aload_2        
            //   130: astore          7
            //   132: aload_1        
            //   133: astore          5
            //   135: aload_1        
            //   136: astore          6
            //   138: new             Ljava/lang/StringBuilder;
            //   141: astore          10
            //   143: aload_2        
            //   144: astore          7
            //   146: aload_1        
            //   147: astore          5
            //   149: aload_1        
            //   150: astore          6
            //   152: aload           10
            //   154: iload           9
            //   156: invokespecial   java/lang/StringBuilder.<init>:(I)V
            //   159: iconst_0       
            //   160: istore          11
            //   162: iload           11
            //   164: iload           9
            //   166: if_icmpge       203
            //   169: aload_2        
            //   170: astore          7
            //   172: aload_1        
            //   173: astore          5
            //   175: aload_1        
            //   176: astore          6
            //   178: aload           10
            //   180: ldc             "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            //   182: getstatic       org/telegram/messenger/Utilities.random:Ljava/security/SecureRandom;
            //   185: bipush          62
            //   187: invokevirtual   java/security/SecureRandom.nextInt:(I)I
            //   190: invokevirtual   java/lang/String.charAt:(I)C
            //   193: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
            //   196: pop            
            //   197: iinc            11, 1
            //   200: goto            162
            //   203: aload_2        
            //   204: astore          7
            //   206: aload_1        
            //   207: astore          5
            //   209: aload_1        
            //   210: astore          6
            //   212: new             Ljava/net/URL;
            //   215: astore          12
            //   217: aload_2        
            //   218: astore          7
            //   220: aload_1        
            //   221: astore          5
            //   223: aload_1        
            //   224: astore          6
            //   226: new             Ljava/lang/StringBuilder;
            //   229: astore          13
            //   231: aload_2        
            //   232: astore          7
            //   234: aload_1        
            //   235: astore          5
            //   237: aload_1        
            //   238: astore          6
            //   240: aload           13
            //   242: invokespecial   java/lang/StringBuilder.<init>:()V
            //   245: aload_2        
            //   246: astore          7
            //   248: aload_1        
            //   249: astore          5
            //   251: aload_1        
            //   252: astore          6
            //   254: aload           13
            //   256: ldc             "https://"
            //   258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   261: pop            
            //   262: aload_2        
            //   263: astore          7
            //   265: aload_1        
            //   266: astore          5
            //   268: aload_1        
            //   269: astore          6
            //   271: aload           13
            //   273: aload           4
            //   275: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   278: pop            
            //   279: aload_2        
            //   280: astore          7
            //   282: aload_1        
            //   283: astore          5
            //   285: aload_1        
            //   286: astore          6
            //   288: aload           13
            //   290: ldc             "/resolve?name="
            //   292: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   295: pop            
            //   296: aload_2        
            //   297: astore          7
            //   299: aload_1        
            //   300: astore          5
            //   302: aload_1        
            //   303: astore          6
            //   305: aload           13
            //   307: aload           8
            //   309: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   312: pop            
            //   313: aload_2        
            //   314: astore          7
            //   316: aload_1        
            //   317: astore          5
            //   319: aload_1        
            //   320: astore          6
            //   322: aload           13
            //   324: ldc             "&type=ANY&random_padding="
            //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   329: pop            
            //   330: aload_2        
            //   331: astore          7
            //   333: aload_1        
            //   334: astore          5
            //   336: aload_1        
            //   337: astore          6
            //   339: aload           13
            //   341: aload           10
            //   343: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   346: pop            
            //   347: aload_2        
            //   348: astore          7
            //   350: aload_1        
            //   351: astore          5
            //   353: aload_1        
            //   354: astore          6
            //   356: aload           12
            //   358: aload           13
            //   360: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   363: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //   366: aload_2        
            //   367: astore          7
            //   369: aload_1        
            //   370: astore          5
            //   372: aload_1        
            //   373: astore          6
            //   375: aload           12
            //   377: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //   380: astore          4
            //   382: aload_2        
            //   383: astore          7
            //   385: aload_1        
            //   386: astore          5
            //   388: aload_1        
            //   389: astore          6
            //   391: aload           4
            //   393: ldc             "User-Agent"
            //   395: ldc             "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1"
            //   397: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //   400: aload_2        
            //   401: astore          7
            //   403: aload_1        
            //   404: astore          5
            //   406: aload_1        
            //   407: astore          6
            //   409: aload           4
            //   411: ldc             "Host"
            //   413: ldc             "dns.google.com"
            //   415: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //   418: aload_2        
            //   419: astore          7
            //   421: aload_1        
            //   422: astore          5
            //   424: aload_1        
            //   425: astore          6
            //   427: aload           4
            //   429: sipush          5000
            //   432: invokevirtual   java/net/URLConnection.setConnectTimeout:(I)V
            //   435: aload_2        
            //   436: astore          7
            //   438: aload_1        
            //   439: astore          5
            //   441: aload_1        
            //   442: astore          6
            //   444: aload           4
            //   446: sipush          5000
            //   449: invokevirtual   java/net/URLConnection.setReadTimeout:(I)V
            //   452: aload_2        
            //   453: astore          7
            //   455: aload_1        
            //   456: astore          5
            //   458: aload_1        
            //   459: astore          6
            //   461: aload           4
            //   463: invokevirtual   java/net/URLConnection.connect:()V
            //   466: aload_2        
            //   467: astore          7
            //   469: aload_1        
            //   470: astore          5
            //   472: aload_1        
            //   473: astore          6
            //   475: aload           4
            //   477: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
            //   480: astore_1       
            //   481: aload_2        
            //   482: astore          7
            //   484: aload_1        
            //   485: astore          5
            //   487: aload_1        
            //   488: astore          6
            //   490: new             Ljava/io/ByteArrayOutputStream;
            //   493: dup            
            //   494: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
            //   497: astore          4
            //   499: ldc             32768
            //   501: newarray        B
            //   503: astore          5
            //   505: aload_0        
            //   506: invokevirtual   android/os/AsyncTask.isCancelled:()Z
            //   509: ifeq            515
            //   512: goto            541
            //   515: aload_1        
            //   516: aload           5
            //   518: invokevirtual   java/io/InputStream.read:([B)I
            //   521: istore          11
            //   523: iload           11
            //   525: ifle            541
            //   528: aload           4
            //   530: aload           5
            //   532: iconst_0       
            //   533: iload           11
            //   535: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
            //   538: goto            505
            //   541: new             Lorg/json/JSONObject;
            //   544: astore          5
            //   546: new             Ljava/lang/String;
            //   549: astore_2       
            //   550: aload_2        
            //   551: aload           4
            //   553: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
            //   556: invokespecial   java/lang/String.<init>:([B)V
            //   559: aload           5
            //   561: aload_2        
            //   562: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
            //   565: aload           5
            //   567: ldc             "Answer"
            //   569: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
            //   572: astore          7
            //   574: aload           7
            //   576: invokevirtual   org/json/JSONArray.length:()I
            //   579: istore          9
            //   581: new             Ljava/util/ArrayList;
            //   584: astore          5
            //   586: aload           5
            //   588: iload           9
            //   590: invokespecial   java/util/ArrayList.<init>:(I)V
            //   593: iconst_0       
            //   594: istore          11
            //   596: iload           11
            //   598: iload           9
            //   600: if_icmpge       643
            //   603: aload           7
            //   605: iload           11
            //   607: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
            //   610: astore_2       
            //   611: aload_2        
            //   612: ldc             "type"
            //   614: invokevirtual   org/json/JSONObject.getInt:(Ljava/lang/String;)I
            //   617: bipush          16
            //   619: if_icmpeq       625
            //   622: goto            637
            //   625: aload           5
            //   627: aload_2        
            //   628: ldc             "data"
            //   630: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
            //   633: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //   636: pop            
            //   637: iinc            11, 1
            //   640: goto            596
            //   643: aload           5
            //   645: getstatic       org/telegram/tgnet/_$$Lambda$ConnectionsManager$DnsTxtLoadTask$BEcjqZFmP4raPbtfXzTVfRUBAsw.INSTANCE:Lorg/telegram/tgnet/-$$Lambda$ConnectionsManager$DnsTxtLoadTask$BEcjqZFmP4raPbtfXzTVfRUBAsw;
            //   648: invokestatic    java/util/Collections.sort:(Ljava/util/List;Ljava/util/Comparator;)V
            //   651: new             Ljava/lang/StringBuilder;
            //   654: astore_2       
            //   655: aload_2        
            //   656: invokespecial   java/lang/StringBuilder.<init>:()V
            //   659: iconst_0       
            //   660: istore          11
            //   662: iload           11
            //   664: aload           5
            //   666: invokevirtual   java/util/ArrayList.size:()I
            //   669: if_icmpge       700
            //   672: aload_2        
            //   673: aload           5
            //   675: iload           11
            //   677: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
            //   680: checkcast       Ljava/lang/String;
            //   683: ldc             "\""
            //   685: ldc             ""
            //   687: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
            //   690: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   693: pop            
            //   694: iinc            11, 1
            //   697: goto            662
            //   700: aload_2        
            //   701: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   704: iconst_0       
            //   705: invokestatic    android/util/Base64.decode:(Ljava/lang/String;I)[B
            //   708: astore_2       
            //   709: new             Lorg/telegram/tgnet/NativeByteBuffer;
            //   712: astore          5
            //   714: aload           5
            //   716: aload_2        
            //   717: arraylength    
            //   718: invokespecial   org/telegram/tgnet/NativeByteBuffer.<init>:(I)V
            //   721: aload           5
            //   723: aload_2        
            //   724: invokevirtual   org/telegram/tgnet/NativeByteBuffer.writeBytes:([B)V
            //   727: aload_1        
            //   728: ifnull          743
            //   731: aload_1        
            //   732: invokevirtual   java/io/InputStream.close:()V
            //   735: goto            743
            //   738: astore_1       
            //   739: aload_1        
            //   740: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   743: aload           4
            //   745: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   748: aload           5
            //   750: areturn        
            //   751: astore          5
            //   753: aload           4
            //   755: astore          7
            //   757: goto            808
            //   760: astore          6
            //   762: aload           4
            //   764: astore_2       
            //   765: aload_2        
            //   766: astore          7
            //   768: aload_1        
            //   769: astore          5
            //   771: aload           6
            //   773: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   776: aload_1        
            //   777: ifnull          794
            //   780: aload_1        
            //   781: invokevirtual   java/io/InputStream.close:()V
            //   784: goto            794
            //   787: astore          5
            //   789: aload           5
            //   791: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   794: aload_2        
            //   795: ifnull          802
            //   798: aload_2        
            //   799: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   802: iinc            3, 1
            //   805: goto            6
            //   808: aload_1        
            //   809: ifnull          824
            //   812: aload_1        
            //   813: invokevirtual   java/io/InputStream.close:()V
            //   816: goto            824
            //   819: astore_1       
            //   820: aload_1        
            //   821: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   824: aload           7
            //   826: ifnull          834
            //   829: aload           7
            //   831: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   834: aload           5
            //   836: athrow         
            //   837: aconst_null    
            //   838: areturn        
            //   839: astore_1       
            //   840: goto            748
            //   843: astore          5
            //   845: goto            802
            //   848: astore_1       
            //   849: goto            834
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  69     79     32     44     Ljava/lang/Throwable;
            //  69     79     22     32     Any
            //  95     107    32     44     Ljava/lang/Throwable;
            //  95     107    22     32     Any
            //  116    129    32     44     Ljava/lang/Throwable;
            //  116    129    22     32     Any
            //  138    143    32     44     Ljava/lang/Throwable;
            //  138    143    22     32     Any
            //  152    159    32     44     Ljava/lang/Throwable;
            //  152    159    22     32     Any
            //  178    197    32     44     Ljava/lang/Throwable;
            //  178    197    22     32     Any
            //  212    217    32     44     Ljava/lang/Throwable;
            //  212    217    22     32     Any
            //  226    231    32     44     Ljava/lang/Throwable;
            //  226    231    22     32     Any
            //  240    245    32     44     Ljava/lang/Throwable;
            //  240    245    22     32     Any
            //  254    262    32     44     Ljava/lang/Throwable;
            //  254    262    22     32     Any
            //  271    279    32     44     Ljava/lang/Throwable;
            //  271    279    22     32     Any
            //  288    296    32     44     Ljava/lang/Throwable;
            //  288    296    22     32     Any
            //  305    313    32     44     Ljava/lang/Throwable;
            //  305    313    22     32     Any
            //  322    330    32     44     Ljava/lang/Throwable;
            //  322    330    22     32     Any
            //  339    347    32     44     Ljava/lang/Throwable;
            //  339    347    22     32     Any
            //  356    366    32     44     Ljava/lang/Throwable;
            //  356    366    22     32     Any
            //  375    382    32     44     Ljava/lang/Throwable;
            //  375    382    22     32     Any
            //  391    400    32     44     Ljava/lang/Throwable;
            //  391    400    22     32     Any
            //  409    418    32     44     Ljava/lang/Throwable;
            //  409    418    22     32     Any
            //  427    435    32     44     Ljava/lang/Throwable;
            //  427    435    22     32     Any
            //  444    452    32     44     Ljava/lang/Throwable;
            //  444    452    22     32     Any
            //  461    466    32     44     Ljava/lang/Throwable;
            //  461    466    22     32     Any
            //  475    481    32     44     Ljava/lang/Throwable;
            //  475    481    22     32     Any
            //  490    499    32     44     Ljava/lang/Throwable;
            //  490    499    22     32     Any
            //  499    505    760    765    Ljava/lang/Throwable;
            //  499    505    751    760    Any
            //  505    512    760    765    Ljava/lang/Throwable;
            //  505    512    751    760    Any
            //  515    523    760    765    Ljava/lang/Throwable;
            //  515    523    751    760    Any
            //  528    538    760    765    Ljava/lang/Throwable;
            //  528    538    751    760    Any
            //  541    593    760    765    Ljava/lang/Throwable;
            //  541    593    751    760    Any
            //  603    622    760    765    Ljava/lang/Throwable;
            //  603    622    751    760    Any
            //  625    637    760    765    Ljava/lang/Throwable;
            //  625    637    751    760    Any
            //  643    659    760    765    Ljava/lang/Throwable;
            //  643    659    751    760    Any
            //  662    694    760    765    Ljava/lang/Throwable;
            //  662    694    751    760    Any
            //  700    727    760    765    Ljava/lang/Throwable;
            //  700    727    751    760    Any
            //  731    735    738    743    Ljava/lang/Throwable;
            //  743    748    839    843    Ljava/lang/Exception;
            //  771    776    22     32     Any
            //  780    784    787    794    Ljava/lang/Throwable;
            //  798    802    843    848    Ljava/lang/Exception;
            //  812    816    819    824    Ljava/lang/Throwable;
            //  829    834    848    852    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.util.ConcurrentModificationException
            //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:937)
            //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:891)
            //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2863)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        protected void onPostExecute(final NativeByteBuffer nativeByteBuffer) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$DnsTxtLoadTask$Y_uiONB1DXfH_CyjIpbAd_79WxM(this, nativeByteBuffer));
        }
    }
    
    private static class FirebaseTask extends AsyncTask<Void, Void, NativeByteBuffer>
    {
        private int currentAccount;
        
        public FirebaseTask(final int currentAccount) {
            this.currentAccount = currentAccount;
        }
        
        protected NativeByteBuffer doInBackground(final Void... array) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$ConnectionsManager$FirebaseTask$SaZGsq57fSFWJm6Aju2tQS8ctbo(this));
            return null;
        }
        
        protected void onPostExecute(final NativeByteBuffer nativeByteBuffer) {
        }
    }
    
    private static class ResolveHostByNameTask extends AsyncTask<Void, Void, String>
    {
        private ArrayList<Long> addresses;
        private String currentHostName;
        
        public ResolveHostByNameTask(final String currentHostName) {
            this.addresses = new ArrayList<Long>();
            this.currentHostName = currentHostName;
        }
        
        public void addAddress(final long n) {
            if (this.addresses.contains(n)) {
                return;
            }
            this.addresses.add(n);
        }
        
        protected String doInBackground(final Void... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: astore_2       
            //     2: iconst_0       
            //     3: istore_3       
            //     4: new             Ljava/net/URL;
            //     7: astore_1       
            //     8: new             Ljava/lang/StringBuilder;
            //    11: astore          4
            //    13: aload           4
            //    15: invokespecial   java/lang/StringBuilder.<init>:()V
            //    18: aload           4
            //    20: ldc             "https://www.google.com/resolve?name="
            //    22: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    25: pop            
            //    26: aload           4
            //    28: aload_0        
            //    29: getfield        org/telegram/tgnet/ConnectionsManager$ResolveHostByNameTask.currentHostName:Ljava/lang/String;
            //    32: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    35: pop            
            //    36: aload           4
            //    38: ldc             "&type=A"
            //    40: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    43: pop            
            //    44: aload_1        
            //    45: aload           4
            //    47: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    50: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //    53: aload_1        
            //    54: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //    57: astore_1       
            //    58: aload_1        
            //    59: ldc             "User-Agent"
            //    61: ldc             "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1"
            //    63: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //    66: aload_1        
            //    67: ldc             "Host"
            //    69: ldc             "dns.google.com"
            //    71: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //    74: aload_1        
            //    75: sipush          1000
            //    78: invokevirtual   java/net/URLConnection.setConnectTimeout:(I)V
            //    81: aload_1        
            //    82: sipush          2000
            //    85: invokevirtual   java/net/URLConnection.setReadTimeout:(I)V
            //    88: aload_1        
            //    89: invokevirtual   java/net/URLConnection.connect:()V
            //    92: aload_1        
            //    93: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
            //    96: astore          5
            //    98: new             Ljava/io/ByteArrayOutputStream;
            //   101: astore_1       
            //   102: aload_1        
            //   103: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
            //   106: ldc             32768
            //   108: newarray        B
            //   110: astore          4
            //   112: aload           5
            //   114: aload           4
            //   116: invokevirtual   java/io/InputStream.read:([B)I
            //   119: istore          6
            //   121: iload           6
            //   123: ifle            138
            //   126: aload_1        
            //   127: aload           4
            //   129: iconst_0       
            //   130: iload           6
            //   132: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
            //   135: goto            112
            //   138: new             Lorg/json/JSONObject;
            //   141: astore_2       
            //   142: new             Ljava/lang/String;
            //   145: astore          4
            //   147: aload           4
            //   149: aload_1        
            //   150: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
            //   153: invokespecial   java/lang/String.<init>:([B)V
            //   156: aload_2        
            //   157: aload           4
            //   159: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
            //   162: aload_2        
            //   163: ldc             "Answer"
            //   165: invokevirtual   org/json/JSONObject.has:(Ljava/lang/String;)Z
            //   168: ifeq            292
            //   171: aload_2        
            //   172: ldc             "Answer"
            //   174: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
            //   177: astore_2       
            //   178: aload_2        
            //   179: invokevirtual   org/json/JSONArray.length:()I
            //   182: istore          7
            //   184: iload           7
            //   186: ifle            292
            //   189: new             Ljava/util/ArrayList;
            //   192: astore          4
            //   194: aload           4
            //   196: iload           7
            //   198: invokespecial   java/util/ArrayList.<init>:(I)V
            //   201: iconst_0       
            //   202: istore          6
            //   204: iload           6
            //   206: iload           7
            //   208: if_icmpge       234
            //   211: aload           4
            //   213: aload_2        
            //   214: iload           6
            //   216: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
            //   219: ldc             "data"
            //   221: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
            //   224: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
            //   227: pop            
            //   228: iinc            6, 1
            //   231: goto            204
            //   234: new             Lorg/telegram/tgnet/ConnectionsManager$ResolvedDomain;
            //   237: astore_2       
            //   238: aload_2        
            //   239: aload           4
            //   241: invokestatic    android/os/SystemClock.elapsedRealtime:()J
            //   244: invokespecial   org/telegram/tgnet/ConnectionsManager$ResolvedDomain.<init>:(Ljava/util/ArrayList;J)V
            //   247: invokestatic    org/telegram/tgnet/ConnectionsManager.access$000:()Ljava/util/concurrent/ConcurrentHashMap;
            //   250: aload_0        
            //   251: getfield        org/telegram/tgnet/ConnectionsManager$ResolveHostByNameTask.currentHostName:Ljava/lang/String;
            //   254: aload_2        
            //   255: invokevirtual   java/util/concurrent/ConcurrentHashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   258: pop            
            //   259: aload_2        
            //   260: invokevirtual   org/telegram/tgnet/ConnectionsManager$ResolvedDomain.getAddress:()Ljava/lang/String;
            //   263: astore          4
            //   265: aload           5
            //   267: ifnull          285
            //   270: aload           5
            //   272: invokevirtual   java/io/InputStream.close:()V
            //   275: goto            285
            //   278: astore          5
            //   280: aload           5
            //   282: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   285: aload_1        
            //   286: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   289: aload           4
            //   291: areturn        
            //   292: aload           5
            //   294: ifnull          312
            //   297: aload           5
            //   299: invokevirtual   java/io/InputStream.close:()V
            //   302: goto            312
            //   305: astore          4
            //   307: aload           4
            //   309: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   312: aload_1        
            //   313: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   316: iconst_1       
            //   317: istore          6
            //   319: goto            403
            //   322: astore          4
            //   324: goto            431
            //   327: astore          4
            //   329: goto            343
            //   332: astore          4
            //   334: aconst_null    
            //   335: astore_1       
            //   336: goto            431
            //   339: astore          4
            //   341: aconst_null    
            //   342: astore_1       
            //   343: goto            364
            //   346: astore          4
            //   348: aconst_null    
            //   349: astore          5
            //   351: aload           5
            //   353: astore_1       
            //   354: goto            431
            //   357: astore          4
            //   359: aconst_null    
            //   360: astore_1       
            //   361: aload_2        
            //   362: astore          5
            //   364: aload           4
            //   366: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   369: aload           5
            //   371: ifnull          389
            //   374: aload           5
            //   376: invokevirtual   java/io/InputStream.close:()V
            //   379: goto            389
            //   382: astore          4
            //   384: aload           4
            //   386: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   389: iload_3        
            //   390: istore          6
            //   392: aload_1        
            //   393: ifnull          403
            //   396: aload_1        
            //   397: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   400: iload_3        
            //   401: istore          6
            //   403: iload           6
            //   405: ifne            426
            //   408: aload_0        
            //   409: getfield        org/telegram/tgnet/ConnectionsManager$ResolveHostByNameTask.currentHostName:Ljava/lang/String;
            //   412: invokestatic    java/net/InetAddress.getByName:(Ljava/lang/String;)Ljava/net/InetAddress;
            //   415: invokevirtual   java/net/InetAddress.getHostAddress:()Ljava/lang/String;
            //   418: astore_1       
            //   419: aload_1        
            //   420: areturn        
            //   421: astore_1       
            //   422: aload_1        
            //   423: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   426: ldc             ""
            //   428: areturn        
            //   429: astore          4
            //   431: aload           5
            //   433: ifnull          451
            //   436: aload           5
            //   438: invokevirtual   java/io/InputStream.close:()V
            //   441: goto            451
            //   444: astore          5
            //   446: aload           5
            //   448: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   451: aload_1        
            //   452: ifnull          459
            //   455: aload_1        
            //   456: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   459: aload           4
            //   461: athrow         
            //   462: astore_1       
            //   463: goto            289
            //   466: astore_1       
            //   467: goto            316
            //   470: astore_1       
            //   471: iload_3        
            //   472: istore          6
            //   474: goto            403
            //   477: astore_1       
            //   478: goto            459
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  4      98     357    364    Ljava/lang/Throwable;
            //  4      98     346    357    Any
            //  98     106    339    343    Ljava/lang/Throwable;
            //  98     106    332    339    Any
            //  106    112    327    332    Ljava/lang/Throwable;
            //  106    112    322    327    Any
            //  112    121    327    332    Ljava/lang/Throwable;
            //  112    121    322    327    Any
            //  126    135    327    332    Ljava/lang/Throwable;
            //  126    135    322    327    Any
            //  138    184    327    332    Ljava/lang/Throwable;
            //  138    184    322    327    Any
            //  189    201    327    332    Ljava/lang/Throwable;
            //  189    201    322    327    Any
            //  211    228    327    332    Ljava/lang/Throwable;
            //  211    228    322    327    Any
            //  234    265    327    332    Ljava/lang/Throwable;
            //  234    265    322    327    Any
            //  270    275    278    285    Ljava/lang/Throwable;
            //  285    289    462    466    Ljava/lang/Exception;
            //  297    302    305    312    Ljava/lang/Throwable;
            //  312    316    466    470    Ljava/lang/Exception;
            //  364    369    429    431    Any
            //  374    379    382    389    Ljava/lang/Throwable;
            //  396    400    470    477    Ljava/lang/Exception;
            //  408    419    421    426    Ljava/lang/Exception;
            //  436    441    444    451    Ljava/lang/Throwable;
            //  455    459    477    481    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 232 out-of-bounds for length 232
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        protected void onPostExecute(final String s) {
            for (int size = this.addresses.size(), i = 0; i < size; ++i) {
                ConnectionsManager.native_onHostNameResolved(this.currentHostName, this.addresses.get(i), s);
            }
            ConnectionsManager.resolvingHostnameTasks.remove(this.currentHostName);
        }
    }
    
    private static class ResolvedDomain
    {
        public ArrayList<String> addresses;
        long ttl;
        
        public ResolvedDomain(final ArrayList<String> addresses, final long ttl) {
            this.addresses = addresses;
            this.ttl = ttl;
        }
        
        public String getAddress() {
            final ArrayList<String> addresses = this.addresses;
            return addresses.get(Utilities.random.nextInt(addresses.size()));
        }
    }
}
