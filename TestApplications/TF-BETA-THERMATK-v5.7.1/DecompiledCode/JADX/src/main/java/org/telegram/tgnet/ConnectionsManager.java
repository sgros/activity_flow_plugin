package org.telegram.tgnet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.SystemClock;
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
import org.telegram.messenger.FileLog;
import org.telegram.messenger.KeepAliveJob;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.StatsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC.TL_config;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.Updates;

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
    private static ConcurrentHashMap<String, ResolvedDomain> dnsCache = new ConcurrentHashMap();
    private static int lastClassGuid = 1;
    private static long lastDnsRequestTime;
    private static HashMap<String, ResolveHostByNameTask> resolvingHostnameTasks = new HashMap();
    private boolean appPaused = true;
    private int appResumeCount;
    private int connectionState;
    private int currentAccount;
    private boolean isUpdating;
    private long lastPauseTime = System.currentTimeMillis();
    private AtomicInteger lastRequestToken = new AtomicInteger(1);

    private static class AzureLoadTask extends AsyncTask<Void, Void, NativeByteBuffer> {
        private int currentAccount;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:68:0x00b8 in {4, 5, 14, 17, 18, 23, 25, 27, 28, 29, 31, 33, 35, 37, 39, 41, 47, 49, 52, 53, 54, 56, 60, 62, 65, 66, 67} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        protected org.telegram.tgnet.NativeByteBuffer doInBackground(java.lang.Void... r7) {
            /*
            r6 = this;
            r7 = 0;
            r0 = r6.currentAccount;	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0 = org.telegram.tgnet.ConnectionsManager.native_isTestBackend(r0);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            if (r0 == 0) goto L_0x0011;	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0 = new java.net.URL;	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r1 = "https://software-download.microsoft.com/testv2/config.txt";	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.<init>(r1);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            goto L_0x0018;	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0 = new java.net.URL;	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r1 = "https://software-download.microsoft.com/prodv2/config.txt";	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.<init>(r1);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0 = r0.openConnection();	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r1 = "User-Agent";	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r2 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.addRequestProperty(r1, r2);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r1 = "Host";	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r2 = "tcdnb.azureedge.net";	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.addRequestProperty(r1, r2);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r1 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.setConnectTimeout(r1);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.setReadTimeout(r1);	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0.connect();	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r0 = r0.getInputStream();	 Catch:{ Throwable -> 0x008f, all -> 0x008c }
            r1 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0086, all -> 0x0080 }
            r1.<init>();	 Catch:{ Throwable -> 0x0086, all -> 0x0080 }
            r2 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r2 = new byte[r2];	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r3 = r6.isCancelled();	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r4 = 0;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            if (r3 == 0) goto L_0x004b;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            goto L_0x0056;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r3 = r0.read(r2);	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            if (r3 <= 0) goto L_0x0055;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r1.write(r2, r4, r3);	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            goto L_0x0043;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r2 = -1;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r2 = r1.toByteArray();	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r2 = android.util.Base64.decode(r2, r4);	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r3 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r4 = r2.length;	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r3.<init>(r4);	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            r3.writeBytes(r2);	 Catch:{ Throwable -> 0x007a, all -> 0x0075 }
            if (r0 == 0) goto L_0x0071;
            r0.close();	 Catch:{ Throwable -> 0x006d }
            goto L_0x0071;
            r7 = move-exception;
            org.telegram.messenger.FileLog.m30e(r7);
            r1.close();	 Catch:{ Exception -> 0x0074 }
            return r3;
            r7 = move-exception;
            r5 = r0;
            r0 = r7;
            r7 = r5;
            goto L_0x00a8;
            r2 = move-exception;
            r5 = r1;
            r1 = r0;
            r0 = r2;
            r2 = r5;
            goto L_0x0092;
            r1 = move-exception;
            r5 = r1;
            r1 = r7;
            r7 = r0;
            r0 = r5;
            goto L_0x00a8;
            r1 = move-exception;
            r2 = r7;
            r5 = r1;
            r1 = r0;
            r0 = r5;
            goto L_0x0092;
            r0 = move-exception;
            r1 = r7;
            goto L_0x00a8;
            r0 = move-exception;
            r1 = r7;
            r2 = r1;
            org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ all -> 0x00a5 }
            if (r1 == 0) goto L_0x009f;
            r1.close();	 Catch:{ Throwable -> 0x009b }
            goto L_0x009f;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            if (r2 == 0) goto L_0x00a4;
            r2.close();	 Catch:{ Exception -> 0x00a4 }
            return r7;
            r0 = move-exception;
            r7 = r1;
            r1 = r2;
            if (r7 == 0) goto L_0x00b2;
            r7.close();	 Catch:{ Throwable -> 0x00ae }
            goto L_0x00b2;
            r7 = move-exception;
            org.telegram.messenger.FileLog.m30e(r7);
            if (r1 == 0) goto L_0x00b7;
            r1.close();	 Catch:{ Exception -> 0x00b7 }
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager$AzureLoadTask.doInBackground(java.lang.Void[]):org.telegram.tgnet.NativeByteBuffer");
        }

        public AzureLoadTask(int i) {
            this.currentAccount = i;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(NativeByteBuffer nativeByteBuffer) {
            Utilities.stageQueue.postRunnable(new C1143xeb362a86(this, nativeByteBuffer));
        }

        public /* synthetic */ void lambda$onPostExecute$0$ConnectionsManager$AzureLoadTask(NativeByteBuffer nativeByteBuffer) {
            if (nativeByteBuffer != null) {
                int i = this.currentAccount;
                ConnectionsManager.native_applyDnsConfig(i, nativeByteBuffer.address, UserConfig.getInstance(i).getClientPhone());
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("failed to get azure result");
            }
            ConnectionsManager.currentTask = null;
        }
    }

    private static class DnsTxtLoadTask extends AsyncTask<Void, Void, NativeByteBuffer> {
        private int currentAccount;

        public DnsTxtLoadTask(int i) {
            this.currentAccount = i;
        }

        /* Access modifiers changed, original: protected|varargs */
        /* JADX WARNING: Removed duplicated region for block: B:70:0x0155 A:{SYNTHETIC, Splitter:B:70:0x0155} */
        /* JADX WARNING: Removed duplicated region for block: B:75:0x015f A:{SYNTHETIC, Splitter:B:75:0x015f} */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x0142 A:{SYNTHETIC, Splitter:B:60:0x0142} */
        /* JADX WARNING: Removed duplicated region for block: B:82:0x014f A:{SYNTHETIC} */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x014c A:{SYNTHETIC, Splitter:B:65:0x014c} */
        public org.telegram.tgnet.NativeByteBuffer doInBackground(java.lang.Void... r14) {
            /*
            r13 = this;
            r14 = 0;
            r0 = 0;
            r2 = r14;
            r3 = r2;
            r1 = 0;
        L_0x0005:
            r4 = 3;
            if (r1 >= r4) goto L_0x0163;
        L_0x0008:
            if (r1 != 0) goto L_0x0013;
        L_0x000a:
            r4 = "www.google.com";
            goto L_0x001b;
        L_0x000d:
            r14 = move-exception;
            goto L_0x0153;
        L_0x0010:
            r4 = move-exception;
            goto L_0x013d;
        L_0x0013:
            r4 = 1;
            if (r1 != r4) goto L_0x0019;
        L_0x0016:
            r4 = "www.google.ru";
            goto L_0x001b;
        L_0x0019:
            r4 = "google.com";
        L_0x001b:
            r5 = r13.currentAccount;	 Catch:{ Throwable -> 0x0010 }
            r5 = org.telegram.tgnet.ConnectionsManager.native_isTestBackend(r5);	 Catch:{ Throwable -> 0x0010 }
            if (r5 == 0) goto L_0x0026;
        L_0x0023:
            r5 = "tapv2.stel.com";
            goto L_0x002e;
        L_0x0026:
            r5 = r13.currentAccount;	 Catch:{ Throwable -> 0x0010 }
            r5 = org.telegram.messenger.MessagesController.getInstance(r5);	 Catch:{ Throwable -> 0x0010 }
            r5 = r5.dcDomainName;	 Catch:{ Throwable -> 0x0010 }
        L_0x002e:
            r6 = org.telegram.messenger.Utilities.random;	 Catch:{ Throwable -> 0x0010 }
            r7 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
            r6 = r6.nextInt(r7);	 Catch:{ Throwable -> 0x0010 }
            r6 = r6 + 13;
            r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0010 }
            r7.<init>(r6);	 Catch:{ Throwable -> 0x0010 }
            r8 = 0;
        L_0x003e:
            if (r8 >= r6) goto L_0x0054;
        L_0x0040:
            r9 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            r10 = org.telegram.messenger.Utilities.random;	 Catch:{ Throwable -> 0x0010 }
            r11 = 62;
            r10 = r10.nextInt(r11);	 Catch:{ Throwable -> 0x0010 }
            r9 = r9.charAt(r10);	 Catch:{ Throwable -> 0x0010 }
            r7.append(r9);	 Catch:{ Throwable -> 0x0010 }
            r8 = r8 + 1;
            goto L_0x003e;
        L_0x0054:
            r6 = new java.net.URL;	 Catch:{ Throwable -> 0x0010 }
            r8 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0010 }
            r8.<init>();	 Catch:{ Throwable -> 0x0010 }
            r9 = "https://";
            r8.append(r9);	 Catch:{ Throwable -> 0x0010 }
            r8.append(r4);	 Catch:{ Throwable -> 0x0010 }
            r4 = "/resolve?name=";
            r8.append(r4);	 Catch:{ Throwable -> 0x0010 }
            r8.append(r5);	 Catch:{ Throwable -> 0x0010 }
            r4 = "&type=ANY&random_padding=";
            r8.append(r4);	 Catch:{ Throwable -> 0x0010 }
            r8.append(r7);	 Catch:{ Throwable -> 0x0010 }
            r4 = r8.toString();	 Catch:{ Throwable -> 0x0010 }
            r6.<init>(r4);	 Catch:{ Throwable -> 0x0010 }
            r4 = r6.openConnection();	 Catch:{ Throwable -> 0x0010 }
            r5 = "User-Agent";
            r6 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";
            r4.addRequestProperty(r5, r6);	 Catch:{ Throwable -> 0x0010 }
            r5 = "Host";
            r6 = "dns.google.com";
            r4.addRequestProperty(r5, r6);	 Catch:{ Throwable -> 0x0010 }
            r5 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r4.setConnectTimeout(r5);	 Catch:{ Throwable -> 0x0010 }
            r4.setReadTimeout(r5);	 Catch:{ Throwable -> 0x0010 }
            r4.connect();	 Catch:{ Throwable -> 0x0010 }
            r3 = r4.getInputStream();	 Catch:{ Throwable -> 0x0010 }
            r4 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0010 }
            r4.<init>();	 Catch:{ Throwable -> 0x0010 }
            r2 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r2 = new byte[r2];	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
        L_0x00a5:
            r5 = r13.isCancelled();	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            if (r5 == 0) goto L_0x00ac;
        L_0x00ab:
            goto L_0x00b7;
        L_0x00ac:
            r5 = r3.read(r2);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            if (r5 <= 0) goto L_0x00b6;
        L_0x00b2:
            r4.write(r2, r0, r5);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            goto L_0x00a5;
        L_0x00b6:
            r2 = -1;
        L_0x00b7:
            r2 = new org.json.JSONObject;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5 = new java.lang.String;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r6 = r4.toByteArray();	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5.<init>(r6);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r2.<init>(r5);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5 = "Answer";
            r2 = r2.getJSONArray(r5);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5 = r2.length();	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r6 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r6.<init>(r5);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r7 = 0;
        L_0x00d5:
            if (r7 >= r5) goto L_0x00f2;
        L_0x00d7:
            r8 = r2.getJSONObject(r7);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r9 = "type";
            r9 = r8.getInt(r9);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r10 = 16;
            if (r9 == r10) goto L_0x00e6;
        L_0x00e5:
            goto L_0x00ef;
        L_0x00e6:
            r9 = "data";
            r8 = r8.getString(r9);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r6.add(r8);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
        L_0x00ef:
            r7 = r7 + 1;
            goto L_0x00d5;
        L_0x00f2:
            r2 = org.telegram.tgnet.C1145x1cd5b821.INSTANCE;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            java.util.Collections.sort(r6, r2);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r2 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r2.<init>();	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5 = 0;
        L_0x00fd:
            r7 = r6.size();	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            if (r5 >= r7) goto L_0x0117;
        L_0x0103:
            r7 = r6.get(r5);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r7 = (java.lang.String) r7;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r8 = "\"";
            r9 = "";
            r7 = r7.replace(r8, r9);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r2.append(r7);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5 = r5 + 1;
            goto L_0x00fd;
        L_0x0117:
            r2 = r2.toString();	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r2 = android.util.Base64.decode(r2, r0);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r6 = r2.length;	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5.<init>(r6);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            r5.writeBytes(r2);	 Catch:{ Throwable -> 0x0139, all -> 0x0136 }
            if (r3 == 0) goto L_0x0132;
        L_0x012a:
            r3.close();	 Catch:{ Throwable -> 0x012e }
            goto L_0x0132;
        L_0x012e:
            r14 = move-exception;
            org.telegram.messenger.FileLog.m30e(r14);
        L_0x0132:
            r4.close();	 Catch:{ Exception -> 0x0135 }
        L_0x0135:
            return r5;
        L_0x0136:
            r14 = move-exception;
            r2 = r4;
            goto L_0x0153;
        L_0x0139:
            r2 = move-exception;
            r12 = r4;
            r4 = r2;
            r2 = r12;
        L_0x013d:
            org.telegram.messenger.FileLog.m30e(r4);	 Catch:{ all -> 0x000d }
            if (r3 == 0) goto L_0x014a;
        L_0x0142:
            r3.close();	 Catch:{ Throwable -> 0x0146 }
            goto L_0x014a;
        L_0x0146:
            r4 = move-exception;
            org.telegram.messenger.FileLog.m30e(r4);
        L_0x014a:
            if (r2 == 0) goto L_0x014f;
        L_0x014c:
            r2.close();	 Catch:{ Exception -> 0x014f }
        L_0x014f:
            r1 = r1 + 1;
            goto L_0x0005;
        L_0x0153:
            if (r3 == 0) goto L_0x015d;
        L_0x0155:
            r3.close();	 Catch:{ Throwable -> 0x0159 }
            goto L_0x015d;
        L_0x0159:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x015d:
            if (r2 == 0) goto L_0x0162;
        L_0x015f:
            r2.close();	 Catch:{ Exception -> 0x0162 }
        L_0x0162:
            throw r14;
        L_0x0163:
            return r14;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager$DnsTxtLoadTask.doInBackground(java.lang.Void[]):org.telegram.tgnet.NativeByteBuffer");
        }

        static /* synthetic */ int lambda$doInBackground$0(String str, String str2) {
            int length = str.length();
            int length2 = str2.length();
            if (length > length2) {
                return -1;
            }
            return length < length2 ? 1 : 0;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(NativeByteBuffer nativeByteBuffer) {
            Utilities.stageQueue.postRunnable(new C1146xa77535d2(this, nativeByteBuffer));
        }

        public /* synthetic */ void lambda$onPostExecute$1$ConnectionsManager$DnsTxtLoadTask(NativeByteBuffer nativeByteBuffer) {
            if (nativeByteBuffer != null) {
                ConnectionsManager.currentTask = null;
                int i = this.currentAccount;
                ConnectionsManager.native_applyDnsConfig(i, nativeByteBuffer.address, UserConfig.getInstance(i).getClientPhone());
                return;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("failed to get dns txt result");
                FileLog.m27d("start azure task");
            }
            AzureLoadTask azureLoadTask = new AzureLoadTask(this.currentAccount);
            azureLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            ConnectionsManager.currentTask = azureLoadTask;
        }
    }

    private static class FirebaseTask extends AsyncTask<Void, Void, NativeByteBuffer> {
        private int currentAccount;

        /* Access modifiers changed, original: protected */
        public void onPostExecute(NativeByteBuffer nativeByteBuffer) {
        }

        public FirebaseTask(int i) {
            this.currentAccount = i;
        }

        /* Access modifiers changed, original: protected|varargs */
        public NativeByteBuffer doInBackground(Void... voidArr) {
            Utilities.stageQueue.postRunnable(new C1147x47d6cb04(this));
            return null;
        }

        public /* synthetic */ void lambda$doInBackground$0$ConnectionsManager$FirebaseTask() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("failed to get firebase result");
                FileLog.m27d("start dns txt task");
            }
            DnsTxtLoadTask dnsTxtLoadTask = new DnsTxtLoadTask(this.currentAccount);
            dnsTxtLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            ConnectionsManager.currentTask = dnsTxtLoadTask;
        }
    }

    private static class ResolveHostByNameTask extends AsyncTask<Void, Void, String> {
        private ArrayList<Long> addresses = new ArrayList();
        private String currentHostName;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:85:0x010b in {10, 17, 22, 24, 26, 27, 28, 32, 34, 36, 37, 38, 40, 42, 44, 46, 47, 49, 51, 57, 59, 62, 64, 67, 69, 71, 73, 77, 79, 82, 83, 84} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        protected java.lang.String doInBackground(java.lang.Void... r9) {
            /*
            r8 = this;
            r9 = "Answer";
            r0 = 0;
            r1 = 0;
            r2 = new java.net.URL;	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3.<init>();	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r4 = "https://www.google.com/resolve?name=";	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3.append(r4);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r4 = r8.currentHostName;	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3.append(r4);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r4 = "&type=A";	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3.append(r4);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = r3.toString();	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2.<init>(r3);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2 = r2.openConnection();	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = "User-Agent";	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r4 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2.addRequestProperty(r3, r4);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = "Host";	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r4 = "dns.google.com";	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2.addRequestProperty(r3, r4);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2.setConnectTimeout(r3);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2.setReadTimeout(r3);	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2.connect();	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r2 = r2.getInputStream();	 Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r3 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x00c9, all -> 0x00c6 }
            r3.<init>();	 Catch:{ Throwable -> 0x00c9, all -> 0x00c6 }
            r0 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r0 = new byte[r0];	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4 = r2.read(r0);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r4 <= 0) goto L_0x0058;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r3.write(r0, r1, r4);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            goto L_0x004e;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r0 = -1;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r0 = new org.json.JSONObject;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4 = new java.lang.String;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5 = r3.toByteArray();	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4.<init>(r5);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r0.<init>(r4);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4 = r0.has(r9);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r4 == 0) goto L_0x00b3;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r9 = r0.getJSONArray(r9);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r0 = r9.length();	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r0 <= 0) goto L_0x00b3;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4.<init>(r0);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5 = 0;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r5 >= r0) goto L_0x008f;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r6 = r9.getJSONObject(r5);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r7 = "data";	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r6 = r6.getString(r7);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4.add(r6);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5 = r5 + 1;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            goto L_0x007d;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r9 = new org.telegram.tgnet.ConnectionsManager$ResolvedDomain;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r5 = android.os.SystemClock.elapsedRealtime();	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r9.<init>(r4, r5);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r0 = org.telegram.tgnet.ConnectionsManager.dnsCache;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r4 = r8.currentHostName;	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r0.put(r4, r9);	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            r9 = r9.getAddress();	 Catch:{ Throwable -> 0x00c4, all -> 0x00c2 }
            if (r2 == 0) goto L_0x00af;
            r2.close();	 Catch:{ Throwable -> 0x00ab }
            goto L_0x00af;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            r3.close();	 Catch:{ Exception -> 0x00b2 }
            return r9;
            if (r2 == 0) goto L_0x00bd;
            r2.close();	 Catch:{ Throwable -> 0x00b9 }
            goto L_0x00bd;
            r9 = move-exception;
            org.telegram.messenger.FileLog.m30e(r9);
            r3.close();	 Catch:{ Exception -> 0x00c0 }
            r1 = 1;
            goto L_0x00e5;
            r9 = move-exception;
            goto L_0x00fb;
            r9 = move-exception;
            goto L_0x00cb;
            r9 = move-exception;
            r3 = r0;
            goto L_0x00fb;
            r9 = move-exception;
            r3 = r0;
            r0 = r2;
            goto L_0x00d3;
            r9 = move-exception;
            r2 = r0;
            r3 = r2;
            goto L_0x00fb;
            r9 = move-exception;
            r3 = r0;
            org.telegram.messenger.FileLog.m30e(r9);	 Catch:{ all -> 0x00f9 }
            if (r0 == 0) goto L_0x00e0;
            r0.close();	 Catch:{ Throwable -> 0x00dc }
            goto L_0x00e0;
            r9 = move-exception;
            org.telegram.messenger.FileLog.m30e(r9);
            if (r3 == 0) goto L_0x00e5;
            r3.close();	 Catch:{ Exception -> 0x00e5 }
            if (r1 != 0) goto L_0x00f6;
            r9 = r8.currentHostName;	 Catch:{ Exception -> 0x00f2 }
            r9 = java.net.InetAddress.getByName(r9);	 Catch:{ Exception -> 0x00f2 }
            r9 = r9.getHostAddress();	 Catch:{ Exception -> 0x00f2 }
            return r9;
            r9 = move-exception;
            org.telegram.messenger.FileLog.m30e(r9);
            r9 = "";
            return r9;
            r9 = move-exception;
            r2 = r0;
            if (r2 == 0) goto L_0x0105;
            r2.close();	 Catch:{ Throwable -> 0x0101 }
            goto L_0x0105;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            if (r3 == 0) goto L_0x010a;
            r3.close();	 Catch:{ Exception -> 0x010a }
            throw r9;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.ConnectionsManager$ResolveHostByNameTask.doInBackground(java.lang.Void[]):java.lang.String");
        }

        public ResolveHostByNameTask(String str) {
            this.currentHostName = str;
        }

        public void addAddress(long j) {
            if (!this.addresses.contains(Long.valueOf(j))) {
                this.addresses.add(Long.valueOf(j));
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String str) {
            int size = this.addresses.size();
            for (int i = 0; i < size; i++) {
                ConnectionsManager.native_onHostNameResolved(this.currentHostName, ((Long) this.addresses.get(i)).longValue(), str);
            }
            ConnectionsManager.resolvingHostnameTasks.remove(this.currentHostName);
        }
    }

    private static class ResolvedDomain {
        public ArrayList<String> addresses;
        long ttl;

        public ResolvedDomain(ArrayList<String> arrayList, long j) {
            this.addresses = arrayList;
            this.ttl = j;
        }

        public String getAddress() {
            ArrayList arrayList = this.addresses;
            return (String) arrayList.get(Utilities.random.nextInt(arrayList.size()));
        }
    }

    public static int getInitFlags() {
        return 0;
    }

    public static native void native_applyDatacenterAddress(int i, int i2, String str, int i3);

    public static native void native_applyDnsConfig(int i, long j, String str);

    public static native void native_bindRequestToGuid(int i, int i2, int i3);

    public static native void native_cancelRequest(int i, int i2, boolean z);

    public static native void native_cancelRequestsForGuid(int i, int i2);

    public static native long native_checkProxy(int i, String str, int i2, String str2, String str3, String str4, RequestTimeDelegate requestTimeDelegate);

    public static native void native_cleanUp(int i, boolean z);

    public static native int native_getConnectionState(int i);

    public static native int native_getCurrentTime(int i);

    public static native long native_getCurrentTimeMillis(int i);

    public static native int native_getTimeDifference(int i);

    public static native void native_init(int i, int i2, int i3, int i4, String str, String str2, String str3, String str4, String str5, String str6, String str7, int i5, boolean z, boolean z2, int i6);

    public static native int native_isTestBackend(int i);

    public static native void native_onHostNameResolved(String str, long j, String str2);

    public static native void native_pauseNetwork(int i);

    public static native void native_resumeNetwork(int i, boolean z);

    public static native void native_seSystemLangCode(int i, String str);

    public static native void native_sendRequest(int i, long j, RequestDelegateInternal requestDelegateInternal, QuickAckDelegate quickAckDelegate, WriteToSocketDelegate writeToSocketDelegate, int i2, int i3, int i4, boolean z, int i5);

    public static native void native_setJava(boolean z);

    public static native void native_setLangCode(int i, String str);

    public static native void native_setNetworkAvailable(int i, boolean z, int i2, boolean z2);

    public static native void native_setProxySettings(int i, String str, int i2, String str2, String str3, String str4);

    public static native void native_setPushConnectionEnabled(int i, boolean z);

    public static native void native_setSystemLangCode(int i, String str);

    public static native void native_setUseIpv6(int i, boolean z);

    public static native void native_setUserId(int i, int i2);

    public static native void native_switchBackend(int i);

    public static native void native_updateDcSettings(int i);

    public static ConnectionsManager getInstance(int i) {
        ConnectionsManager connectionsManager = Instance[i];
        if (connectionsManager == null) {
            synchronized (ConnectionsManager.class) {
                connectionsManager = Instance[i];
                if (connectionsManager == null) {
                    ConnectionsManager[] connectionsManagerArr = Instance;
                    ConnectionsManager connectionsManager2 = new ConnectionsManager(i);
                    connectionsManagerArr[i] = connectionsManager2;
                    connectionsManager = connectionsManager2;
                }
            }
        }
        return connectionsManager;
    }

    public ConnectionsManager(int i) {
        StringBuilder stringBuilder;
        String toLowerCase;
        String toLowerCase2;
        String stringBuilder2;
        String stringBuilder3;
        String str;
        int i2 = i;
        String str2 = "SDK ";
        String str3 = "App version unknown";
        String str4 = "Android unknown";
        String str5 = "en";
        this.currentAccount = i2;
        this.connectionState = native_getConnectionState(this.currentAccount);
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        if (i2 != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("account");
            stringBuilder.append(i2);
            File file = new File(filesDirFixed, stringBuilder.toString());
            file.mkdirs();
            filesDirFixed = file;
        }
        String file2 = filesDirFixed.toString();
        boolean z = MessagesController.getGlobalNotificationsSettings().getBoolean("pushConnection", true);
        try {
            toLowerCase = LocaleController.getSystemLocaleStringIso639().toLowerCase();
            toLowerCase2 = LocaleController.getLocaleStringIso639().toLowerCase();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(Build.MANUFACTURER);
            stringBuilder4.append(Build.MODEL);
            stringBuilder2 = stringBuilder4.toString();
            PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            stringBuilder = new StringBuilder();
            stringBuilder.append(packageInfo.versionName);
            stringBuilder.append(" (");
            stringBuilder.append(packageInfo.versionCode);
            stringBuilder.append(")");
            stringBuilder3 = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(VERSION.SDK_INT);
            str2 = stringBuilder.toString();
            str = toLowerCase2;
        } catch (Exception unused) {
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append(str2);
            stringBuilder5.append(VERSION.SDK_INT);
            str2 = stringBuilder5.toString();
            str = "";
            stringBuilder3 = str3;
            stringBuilder2 = str4;
            toLowerCase = str5;
        }
        String str6 = toLowerCase.trim().length() == 0 ? str5 : toLowerCase;
        str5 = stringBuilder2.trim().length() == 0 ? str4 : stringBuilder2;
        stringBuilder2 = stringBuilder3.trim().length() == 0 ? str3 : stringBuilder3;
        toLowerCase2 = str2.trim().length() == 0 ? "SDK Unknown" : str2;
        UserConfig.getInstance(this.currentAccount).loadConfig();
        init(BuildVars.BUILD_VERSION, 100, BuildVars.APP_ID, str5, toLowerCase2, stringBuilder2, str, str6, file2, FileLog.getNetworkLogPath(), UserConfig.getInstance(this.currentAccount).getClientUserId(), z);
    }

    public long getCurrentTimeMillis() {
        return native_getCurrentTimeMillis(this.currentAccount);
    }

    public int getCurrentTime() {
        return native_getCurrentTime(this.currentAccount);
    }

    public int getTimeDifference() {
        return native_getTimeDifference(this.currentAccount);
    }

    public int sendRequest(TLObject tLObject, RequestDelegate requestDelegate) {
        return sendRequest(tLObject, requestDelegate, null, 0);
    }

    public int sendRequest(TLObject tLObject, RequestDelegate requestDelegate, int i) {
        return sendRequest(tLObject, requestDelegate, null, null, i, Integer.MAX_VALUE, 1, true);
    }

    public int sendRequest(TLObject tLObject, RequestDelegate requestDelegate, int i, int i2) {
        return sendRequest(tLObject, requestDelegate, null, null, i, Integer.MAX_VALUE, i2, true);
    }

    public int sendRequest(TLObject tLObject, RequestDelegate requestDelegate, QuickAckDelegate quickAckDelegate, int i) {
        return sendRequest(tLObject, requestDelegate, quickAckDelegate, null, i, Integer.MAX_VALUE, 1, true);
    }

    public int sendRequest(TLObject tLObject, RequestDelegate requestDelegate, QuickAckDelegate quickAckDelegate, WriteToSocketDelegate writeToSocketDelegate, int i, int i2, int i3, boolean z) {
        int andIncrement = this.lastRequestToken.getAndIncrement();
        Utilities.stageQueue.postRunnable(new C1154-$$Lambda$ConnectionsManager$csmhNL7gP4ZbIN5-kTApilG8kBQ(this, tLObject, andIncrement, requestDelegate, quickAckDelegate, writeToSocketDelegate, i, i2, i3, z));
        return andIncrement;
    }

    public /* synthetic */ void lambda$sendRequest$2$ConnectionsManager(TLObject tLObject, int i, RequestDelegate requestDelegate, QuickAckDelegate quickAckDelegate, WriteToSocketDelegate writeToSocketDelegate, int i2, int i3, int i4, boolean z) {
        Throwable e;
        TLObject tLObject2 = tLObject;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("send request ");
            stringBuilder.append(tLObject2);
            stringBuilder.append(" with token = ");
            stringBuilder.append(i);
            FileLog.m27d(stringBuilder.toString());
        } else {
            int i5 = i;
        }
        try {
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLObject.getObjectSize());
            tLObject2.serializeToStream(nativeByteBuffer);
            tLObject.freeResources();
            try {
                native_sendRequest(this.currentAccount, nativeByteBuffer.address, new C3570-$$Lambda$ConnectionsManager$qOa5d09BI1fBg0o1upbl6aXFnSY(tLObject2, requestDelegate), quickAckDelegate, writeToSocketDelegate, i2, i3, i4, z, i);
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            FileLog.m30e(e);
        }
    }

    static /* synthetic */ void lambda$null$1(TLObject tLObject, RequestDelegate requestDelegate, long j, int i, String str, int i2) {
        Object obj;
        TLObject tLObject2 = null;
        if (j != 0) {
            try {
                NativeByteBuffer wrap = NativeByteBuffer.wrap(j);
                wrap.reused = true;
                tLObject = tLObject.deserializeResponse(wrap, wrap.readInt32(true), true);
                obj = null;
                tLObject2 = tLObject;
            } catch (Exception e) {
                FileLog.m30e(e);
                return;
            }
        } else if (str != null) {
            obj = new TL_error();
            obj.code = i;
            obj.text = str;
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(tLObject);
                stringBuilder.append(" got error ");
                stringBuilder.append(obj.code);
                stringBuilder.append(" ");
                stringBuilder.append(obj.text);
                FileLog.m28e(stringBuilder.toString());
            }
        } else {
            obj = null;
        }
        if (tLObject2 != null) {
            tLObject2.networkType = i2;
        }
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("java received ");
            stringBuilder2.append(tLObject2);
            stringBuilder2.append(" error = ");
            stringBuilder2.append(obj);
            FileLog.m27d(stringBuilder2.toString());
        }
        Utilities.stageQueue.postRunnable(new C1149-$$Lambda$ConnectionsManager$N1Ud38cKJRQ_sqp20Fcot5ApM0Y(requestDelegate, tLObject2, obj));
    }

    static /* synthetic */ void lambda$null$0(RequestDelegate requestDelegate, TLObject tLObject, TL_error tL_error) {
        requestDelegate.run(tLObject, tL_error);
        if (tLObject != null) {
            tLObject.freeResources();
        }
    }

    public void cancelRequest(int i, boolean z) {
        native_cancelRequest(this.currentAccount, i, z);
    }

    public void cleanup(boolean z) {
        native_cleanUp(this.currentAccount, z);
    }

    public void cancelRequestsForGuid(int i) {
        native_cancelRequestsForGuid(this.currentAccount, i);
    }

    public void bindRequestToGuid(int i, int i2) {
        native_bindRequestToGuid(this.currentAccount, i, i2);
    }

    public void applyDatacenterAddress(int i, String str, int i2) {
        native_applyDatacenterAddress(this.currentAccount, i, str, i2);
    }

    public int getConnectionState() {
        if (this.connectionState == 3 && this.isUpdating) {
            return 5;
        }
        return this.connectionState;
    }

    public void setUserId(int i) {
        native_setUserId(this.currentAccount, i);
    }

    public void checkConnection() {
        native_setUseIpv6(this.currentAccount, useIpv6Address());
        native_setNetworkAvailable(this.currentAccount, ApplicationLoader.isNetworkOnline(), ApplicationLoader.getCurrentNetworkType(), ApplicationLoader.isConnectionSlow());
    }

    public void setPushConnectionEnabled(boolean z) {
        native_setPushConnectionEnabled(this.currentAccount, z);
    }

    public void init(int i, int i2, int i3, String str, String str2, String str3, String str4, String str5, String str6, String str7, int i4, boolean z) {
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        String str8 = "";
        String string = sharedPreferences.getString("proxy_ip", str8);
        String string2 = sharedPreferences.getString("proxy_user", str8);
        String string3 = sharedPreferences.getString("proxy_pass", str8);
        String string4 = sharedPreferences.getString("proxy_secret", str8);
        int i5 = sharedPreferences.getInt("proxy_port", 1080);
        if (sharedPreferences.getBoolean("proxy_enabled", false) && !TextUtils.isEmpty(string)) {
            native_setProxySettings(this.currentAccount, string, i5, string2, string3, string4);
        }
        native_init(this.currentAccount, i, i2, i3, str, str2, str3, str4, str5, str6, str7, i4, z, ApplicationLoader.isNetworkOnline(), ApplicationLoader.getCurrentNetworkType());
        checkConnection();
    }

    public static void setLangCode(String str) {
        str = str.replace('_', '-').toLowerCase();
        for (int i = 0; i < 3; i++) {
            native_setLangCode(i, str);
        }
    }

    public static void setSystemLangCode(String str) {
        str = str.replace('_', '-').toLowerCase();
        for (int i = 0; i < 3; i++) {
            native_setSystemLangCode(i, str);
        }
    }

    public void switchBackend() {
        MessagesController.getGlobalMainSettings().edit().remove("language_showed2").commit();
        native_switchBackend(this.currentAccount);
    }

    public void resumeNetworkMaybe() {
        native_resumeNetwork(this.currentAccount, true);
    }

    public void updateDcSettings() {
        native_updateDcSettings(this.currentAccount);
    }

    public long getPauseTime() {
        return this.lastPauseTime;
    }

    public long checkProxy(String str, int i, String str2, String str3, String str4, RequestTimeDelegate requestTimeDelegate) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        String str5 = "";
        return native_checkProxy(this.currentAccount, str == null ? str5 : str, i, str2 == null ? str5 : str2, str3 == null ? str5 : str3, str4 == null ? str5 : str4, requestTimeDelegate);
    }

    public void setAppPaused(boolean z, boolean z2) {
        if (!z2) {
            this.appPaused = z;
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("app paused = ");
                stringBuilder.append(z);
                FileLog.m27d(stringBuilder.toString());
            }
            if (z) {
                this.appResumeCount--;
            } else {
                this.appResumeCount++;
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("app resume count ");
                stringBuilder2.append(this.appResumeCount);
                FileLog.m27d(stringBuilder2.toString());
            }
            if (this.appResumeCount < 0) {
                this.appResumeCount = 0;
            }
        }
        if (this.appResumeCount == 0) {
            if (this.lastPauseTime == 0) {
                this.lastPauseTime = System.currentTimeMillis();
            }
            native_pauseNetwork(this.currentAccount);
        } else if (!this.appPaused) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("reset app pause time");
            }
            if (this.lastPauseTime != 0 && System.currentTimeMillis() - this.lastPauseTime > 5000) {
                ContactsController.getInstance(this.currentAccount).checkContacts();
            }
            this.lastPauseTime = 0;
            native_resumeNetwork(this.currentAccount, false);
        }
    }

    public static void onUnparsedMessageReceived(long j, int i) {
        try {
            NativeByteBuffer wrap = NativeByteBuffer.wrap(j);
            wrap.reused = true;
            TLObject TLdeserialize = TLClassStore.Instance().TLdeserialize(wrap, wrap.readInt32(true), true);
            if (TLdeserialize instanceof Updates) {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("java received ");
                    stringBuilder.append(TLdeserialize);
                    FileLog.m27d(stringBuilder.toString());
                }
                KeepAliveJob.finishJob();
                Utilities.stageQueue.postRunnable(new C1150-$$Lambda$ConnectionsManager$R5V1iXmwj8PWON-tb_jcTaBhzJo(i, TLdeserialize));
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d(String.format("java received unknown constructor 0x%x", new Object[]{Integer.valueOf(r0)}));
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public static void onUpdate(int i) {
        Utilities.stageQueue.postRunnable(new C1148-$$Lambda$ConnectionsManager$GiXNWNTneL61N5XH1sI4IVkif4k(i));
    }

    public static void onSessionCreated(int i) {
        Utilities.stageQueue.postRunnable(new C1153-$$Lambda$ConnectionsManager$c-kbk6lmCmsTzziA11WOyMsJtqY(i));
    }

    public static void onConnectionStateChanged(int i, int i2) {
        AndroidUtilities.runOnUIThread(new C1155-$$Lambda$ConnectionsManager$wMpd1-zDWgiLp6x8fjImjIX349A(i2, i));
    }

    static /* synthetic */ void lambda$onConnectionStateChanged$6(int i, int i2) {
        getInstance(i).connectionState = i2;
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.didUpdateConnectionState, new Object[0]);
    }

    public static void onLogout(int i) {
        AndroidUtilities.runOnUIThread(new C1152-$$Lambda$ConnectionsManager$WUMuAbrjc16kWFnK3JYXcq2eB4E(i));
    }

    static /* synthetic */ void lambda$onLogout$7(int i) {
        if (UserConfig.getInstance(i).getClientUserId() != 0) {
            UserConfig.getInstance(i).clearConfig();
            MessagesController.getInstance(i).performLogout(0);
        }
    }

    public static void onBytesSent(int i, int i2, int i3) {
        try {
            StatsController.getInstance(i3).incrementSentBytesCount(i2, 6, (long) i);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public static void onRequestNewServerIpAndPort(int i, int i2) {
        Utilities.stageQueue.postRunnable(new C1151-$$Lambda$ConnectionsManager$Vntp1UzbcZLxSUJY5_RFJvLOrY4(i, i2));
    }

    static /* synthetic */ void lambda$onRequestNewServerIpAndPort$8(int i, int i2) {
        if (currentTask != null || ((i == 0 && Math.abs(lastDnsRequestTime - System.currentTimeMillis()) < 10000) || !ApplicationLoader.isNetworkOnline())) {
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("don't start task, current task = ");
                stringBuilder.append(currentTask);
                stringBuilder.append(" next task = ");
                stringBuilder.append(i);
                stringBuilder.append(" time diff = ");
                stringBuilder.append(Math.abs(lastDnsRequestTime - System.currentTimeMillis()));
                stringBuilder.append(" network = ");
                stringBuilder.append(ApplicationLoader.isNetworkOnline());
                FileLog.m27d(stringBuilder.toString());
            }
            return;
        }
        lastDnsRequestTime = System.currentTimeMillis();
        if (i == 2) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("start azure dns task");
            }
            AzureLoadTask azureLoadTask = new AzureLoadTask(i2);
            azureLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            currentTask = azureLoadTask;
        } else if (i == 1) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("start dns txt task");
            }
            DnsTxtLoadTask dnsTxtLoadTask = new DnsTxtLoadTask(i2);
            dnsTxtLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            currentTask = dnsTxtLoadTask;
        } else {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("start firebase task");
            }
            FirebaseTask firebaseTask = new FirebaseTask(i2);
            firebaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            currentTask = firebaseTask;
        }
    }

    public static void onProxyError() {
        AndroidUtilities.runOnUIThread(C1142-$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU.INSTANCE);
    }

    public static void getHostByName(String str, long j) {
        ResolvedDomain resolvedDomain = (ResolvedDomain) dnsCache.get(str);
        if (resolvedDomain == null || SystemClock.elapsedRealtime() - resolvedDomain.ttl >= 300000) {
            ResolveHostByNameTask resolveHostByNameTask = (ResolveHostByNameTask) resolvingHostnameTasks.get(str);
            if (resolveHostByNameTask == null) {
                resolveHostByNameTask = new ResolveHostByNameTask(str);
                resolveHostByNameTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                resolvingHostnameTasks.put(str, resolveHostByNameTask);
            }
            resolveHostByNameTask.addAddress(j);
            return;
        }
        native_onHostNameResolved(str, j, resolvedDomain.getAddress());
    }

    public static void onBytesReceived(int i, int i2, int i3) {
        try {
            StatsController.getInstance(i3).incrementReceivedBytesCount(i2, 6, (long) i);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public static void onUpdateConfig(long j, int i) {
        try {
            NativeByteBuffer wrap = NativeByteBuffer.wrap(j);
            wrap.reused = true;
            TL_config TLdeserialize = TL_config.TLdeserialize(wrap, wrap.readInt32(true), true);
            if (TLdeserialize != null) {
                Utilities.stageQueue.postRunnable(new C1144-$$Lambda$ConnectionsManager$CYECFQVHelItYSO81Usv2-hJ1zU(i, TLdeserialize));
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public static void onInternalPushReceived(int i) {
        KeepAliveJob.startJob();
    }

    public static void setProxySettings(boolean z, String str, int i, String str2, String str3, String str4) {
        CharSequence str5;
        String str6 = "";
        if (str5 == null) {
            str5 = str6;
        }
        if (str2 == null) {
            str2 = str6;
        }
        if (str3 == null) {
            str3 = str6;
        }
        if (str4 == null) {
            str4 = str6;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            if (!z || TextUtils.isEmpty(str5)) {
                native_setProxySettings(i2, "", 1080, "", "", "");
            } else {
                native_setProxySettings(i2, str5, i, str2, str3, str4);
            }
            if (UserConfig.getInstance(i2).isClientActivated()) {
                MessagesController.getInstance(i2).checkProxyInfo(true);
            }
        }
    }

    public static int generateClassGuid() {
        int i = lastClassGuid;
        lastClassGuid = i + 1;
        return i;
    }

    public void setIsUpdating(boolean z) {
        AndroidUtilities.runOnUIThread(new C1141-$$Lambda$ConnectionsManager$-MqDe5Su-gCe6Qmn9_j7mUamUVo(this, z));
    }

    public /* synthetic */ void lambda$setIsUpdating$11$ConnectionsManager(boolean z) {
        if (this.isUpdating != z) {
            this.isUpdating = z;
            if (this.connectionState == 3) {
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didUpdateConnectionState, new Object[0]);
            }
        }
    }

    @SuppressLint({"NewApi"})
    protected static boolean useIpv6Address() {
        if (VERSION.SDK_INT < 19) {
            return false;
        }
        Enumeration networkInterfaces;
        if (BuildVars.LOGS_ENABLED) {
            try {
                networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                    if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                        if (!networkInterface.getInterfaceAddresses().isEmpty()) {
                            if (BuildVars.LOGS_ENABLED) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("valid interface: ");
                                stringBuilder.append(networkInterface);
                                FileLog.m27d(stringBuilder.toString());
                            }
                            List interfaceAddresses = networkInterface.getInterfaceAddresses();
                            for (int i = 0; i < interfaceAddresses.size(); i++) {
                                InetAddress address = ((InterfaceAddress) interfaceAddresses.get(i)).getAddress();
                                if (BuildVars.LOGS_ENABLED) {
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("address: ");
                                    stringBuilder2.append(address.getHostAddress());
                                    FileLog.m27d(stringBuilder2.toString());
                                }
                                if (!(address.isLinkLocalAddress() || address.isLoopbackAddress())) {
                                    if (!address.isMulticastAddress()) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.m27d("address is good");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                FileLog.m30e(th);
            }
        }
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Object obj = null;
            Object obj2 = null;
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface2 = (NetworkInterface) networkInterfaces.nextElement();
                if (networkInterface2.isUp()) {
                    if (!networkInterface2.isLoopback()) {
                        List interfaceAddresses2 = networkInterface2.getInterfaceAddresses();
                        Object obj3 = obj2;
                        obj2 = obj;
                        for (int i2 = 0; i2 < interfaceAddresses2.size(); i2++) {
                            InetAddress address2 = ((InterfaceAddress) interfaceAddresses2.get(i2)).getAddress();
                            if (!(address2.isLinkLocalAddress() || address2.isLoopbackAddress())) {
                                if (!address2.isMulticastAddress()) {
                                    if (address2 instanceof Inet6Address) {
                                        obj3 = 1;
                                    } else if ((address2 instanceof Inet4Address) && !address2.getHostAddress().startsWith("192.0.0.")) {
                                        obj2 = 1;
                                    }
                                }
                            }
                        }
                        obj = obj2;
                        obj2 = obj3;
                    }
                }
            }
            if (obj != null || obj2 == null) {
                return false;
            }
            return true;
        } catch (Throwable th2) {
            FileLog.m30e(th2);
        }
    }
}
