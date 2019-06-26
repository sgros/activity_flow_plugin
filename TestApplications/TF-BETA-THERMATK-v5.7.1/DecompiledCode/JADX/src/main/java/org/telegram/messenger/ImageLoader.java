package org.telegram.messenger;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;
import com.airbnb.lottie.LottieDrawable;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.messenger.FileLoader.FileLoaderDelegate;
import org.telegram.p004ui.Components.AnimatedFileDrawable;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_error;

public class ImageLoader {
    public static final String AUTOPLAY_FILTER = "g";
    private static volatile ImageLoader Instance = null;
    private static ThreadLocal<byte[]> bytesLocal = new ThreadLocal();
    private static ThreadLocal<byte[]> bytesThumbLocal = new ThreadLocal();
    private static byte[] header = new byte[12];
    private static byte[] headerThumb = new byte[12];
    private LinkedList<ArtworkLoadTask> artworkTasks = new LinkedList();
    private HashMap<String, Integer> bitmapUseCounts = new HashMap();
    private DispatchQueue cacheOutQueue = new DispatchQueue("cacheOutQueue");
    private DispatchQueue cacheThumbOutQueue = new DispatchQueue("cacheThumbOutQueue");
    private boolean canForce8888;
    private int currentArtworkTasksCount;
    private int currentHttpFileLoadTasksCount;
    private int currentHttpTasksCount;
    private ConcurrentHashMap<String, Float> fileProgresses = new ConcurrentHashMap();
    private HashMap<String, Integer> forceLoadingImages = new HashMap();
    private LinkedList<HttpFileTask> httpFileLoadTasks;
    private HashMap<String, HttpFileTask> httpFileLoadTasksByKeys;
    private LinkedList<HttpImageTask> httpTasks = new LinkedList();
    private String ignoreRemoval;
    private DispatchQueue imageLoadQueue = new DispatchQueue("imageLoadQueue");
    private HashMap<String, CacheImage> imageLoadingByKeys = new HashMap();
    private SparseArray<CacheImage> imageLoadingByTag = new SparseArray();
    private HashMap<String, CacheImage> imageLoadingByUrl = new HashMap();
    private volatile long lastCacheOutTime;
    private int lastImageNum;
    private long lastProgressUpdateTime;
    private LruCache<LottieDrawable> lottieMemCache;
    private LruCache<BitmapDrawable> memCache;
    private HashMap<String, String> replacedBitmaps = new HashMap();
    private HashMap<String, Runnable> retryHttpsTasks;
    private File telegramPath;
    private ConcurrentHashMap<String, WebFile> testWebFile;
    private HashMap<String, ThumbGenerateTask> thumbGenerateTasks = new HashMap();
    private DispatchQueue thumbGeneratingQueue = new DispatchQueue("thumbGeneratingQueue");
    private HashMap<String, ThumbGenerateInfo> waitingForQualityThumb = new HashMap();
    private SparseArray<String> waitingForQualityThumbByTag = new SparseArray();

    /* renamed from: org.telegram.messenger.ImageLoader$4 */
    class C10284 extends BroadcastReceiver {
        C10284() {
        }

        public void onReceive(Context context, Intent intent) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("file system changed");
            }
            C0478-$$Lambda$ImageLoader$4$C7Gf_cAEPSage-rixfg5JW73rtw c0478-$$Lambda$ImageLoader$4$C7Gf_cAEPSage-rixfg5JW73rtw = new C0478-$$Lambda$ImageLoader$4$C7Gf_cAEPSage-rixfg5JW73rtw(this);
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                AndroidUtilities.runOnUIThread(c0478-$$Lambda$ImageLoader$4$C7Gf_cAEPSage-rixfg5JW73rtw, 1000);
            } else {
                c0478-$$Lambda$ImageLoader$4$C7Gf_cAEPSage-rixfg5JW73rtw.run();
            }
        }

        public /* synthetic */ void lambda$onReceive$0$ImageLoader$4() {
            ImageLoader.this.checkMediaPaths();
        }
    }

    private class ArtworkLoadTask extends AsyncTask<Void, Void, String> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private HttpURLConnection httpConnection;
        private boolean small;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:146:0x016d in {13, 16, 25, 28, 29, 37, 39, 42, 44, 46, 47, 48, 54, 56, 59, 61, 63, 64, 65, 69, 71, 74, 76, 79, 81, 83, 85, 86, 88, 90, 92, 98, 101, 108, 111, 116, 118, 121, 123, 125, 126, 127, 129, 133, 135, 138, 140, 143, 144, 145} preds:[]
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
        protected java.lang.String doInBackground(java.lang.Void... r8) {
            /*
            r7 = this;
            r8 = 0;
            r0 = 0;
            r1 = r7.cacheImage;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r1.imageLocation;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r1.path;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r2 = new java.net.URL;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r3 = "athumb://";	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r4 = "https://";	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r1.replace(r3, r4);	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r2.<init>(r1);	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r2.openConnection();	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = (java.net.HttpURLConnection) r1;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r7.httpConnection = r1;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r2 = "User-Agent";	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r3 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1.addRequestProperty(r2, r3);	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1.setConnectTimeout(r2);	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1.setReadTimeout(r2);	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1.connect();	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r7.httpConnection;	 Catch:{ Exception -> 0x0050 }
            if (r1 == 0) goto L_0x0054;	 Catch:{ Exception -> 0x0050 }
            r1 = r7.httpConnection;	 Catch:{ Exception -> 0x0050 }
            r1 = r1.getResponseCode();	 Catch:{ Exception -> 0x0050 }
            r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ Exception -> 0x0050 }
            if (r1 == r2) goto L_0x0054;	 Catch:{ Exception -> 0x0050 }
            r2 = 202; // 0xca float:2.83E-43 double:1.0E-321;	 Catch:{ Exception -> 0x0050 }
            if (r1 == r2) goto L_0x0054;	 Catch:{ Exception -> 0x0050 }
            r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;	 Catch:{ Exception -> 0x0050 }
            if (r1 == r2) goto L_0x0054;	 Catch:{ Exception -> 0x0050 }
            r7.canRetry = r0;	 Catch:{ Exception -> 0x0050 }
            goto L_0x0054;
            r1 = move-exception;
            org.telegram.messenger.FileLog.m30e(r1);	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r1 = r1.getInputStream();	 Catch:{ Throwable -> 0x0102, all -> 0x00ff }
            r2 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x00fa, all -> 0x00f5 }
            r2.<init>();	 Catch:{ Throwable -> 0x00fa, all -> 0x00f5 }
            r3 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r3 = new byte[r3];	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = r7.isCancelled();	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            if (r4 == 0) goto L_0x006b;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            goto L_0x0076;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = r1.read(r3);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            if (r4 <= 0) goto L_0x0075;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r2.write(r3, r0, r4);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            goto L_0x0064;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r3 = -1;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r7.canRetry = r0;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r3 = new org.json.JSONObject;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = new java.lang.String;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r5 = r2.toByteArray();	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4.<init>(r5);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r3.<init>(r4);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = "results";	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r3 = r3.getJSONArray(r4);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = r3.length();	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            if (r4 <= 0) goto L_0x00d6;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r3 = r3.getJSONObject(r0);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = "artworkUrl100";	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r3 = r3.getString(r4);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r4 = r7.small;	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            if (r4 == 0) goto L_0x00b7;
            r8 = r7.httpConnection;	 Catch:{ Throwable -> 0x00a9 }
            if (r8 == 0) goto L_0x00a9;	 Catch:{ Throwable -> 0x00a9 }
            r8 = r7.httpConnection;	 Catch:{ Throwable -> 0x00a9 }
            r8.disconnect();	 Catch:{ Throwable -> 0x00a9 }
            if (r1 == 0) goto L_0x00b3;
            r1.close();	 Catch:{ Throwable -> 0x00af }
            goto L_0x00b3;
            r8 = move-exception;
            org.telegram.messenger.FileLog.m30e(r8);
            r2.close();	 Catch:{ Exception -> 0x00b6 }
            return r3;
            r4 = "100x100";	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r5 = "600x600";	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r8 = r3.replace(r4, r5);	 Catch:{ Throwable -> 0x00f0, all -> 0x00ee }
            r0 = r7.httpConnection;	 Catch:{ Throwable -> 0x00c8 }
            if (r0 == 0) goto L_0x00c8;	 Catch:{ Throwable -> 0x00c8 }
            r0 = r7.httpConnection;	 Catch:{ Throwable -> 0x00c8 }
            r0.disconnect();	 Catch:{ Throwable -> 0x00c8 }
            if (r1 == 0) goto L_0x00d2;
            r1.close();	 Catch:{ Throwable -> 0x00ce }
            goto L_0x00d2;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            r2.close();	 Catch:{ Exception -> 0x00d5 }
            return r8;
            r0 = r7.httpConnection;	 Catch:{ Throwable -> 0x00df }
            if (r0 == 0) goto L_0x00df;	 Catch:{ Throwable -> 0x00df }
            r0 = r7.httpConnection;	 Catch:{ Throwable -> 0x00df }
            r0.disconnect();	 Catch:{ Throwable -> 0x00df }
            if (r1 == 0) goto L_0x00e9;
            r1.close();	 Catch:{ Throwable -> 0x00e5 }
            goto L_0x00e9;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            r2.close();	 Catch:{ Exception -> 0x0151 }
            goto L_0x0151;
            r0 = move-exception;
            goto L_0x00f7;
            r3 = move-exception;
            r6 = r3;
            r3 = r1;
            r1 = r6;
            goto L_0x0105;
            r0 = move-exception;
            r2 = r8;
            r8 = r1;
            goto L_0x0154;
            r2 = move-exception;
            r3 = r1;
            r1 = r2;
            r2 = r8;
            goto L_0x0105;
            r0 = move-exception;
            r2 = r8;
            goto L_0x0154;
            r1 = move-exception;
            r2 = r8;
            r3 = r2;
            r4 = r1 instanceof java.net.SocketTimeoutException;	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0112;	 Catch:{ all -> 0x0152 }
            r4 = org.telegram.messenger.ApplicationLoader.isNetworkOnline();	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r7.canRetry = r0;	 Catch:{ all -> 0x0152 }
            goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r4 = r1 instanceof java.net.UnknownHostException;	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0119;	 Catch:{ all -> 0x0152 }
            r7.canRetry = r0;	 Catch:{ all -> 0x0152 }
            goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r4 = r1 instanceof java.net.SocketException;	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0132;	 Catch:{ all -> 0x0152 }
            r4 = r1.getMessage();	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r4 = r1.getMessage();	 Catch:{ all -> 0x0152 }
            r5 = "ECONNRESET";	 Catch:{ all -> 0x0152 }
            r4 = r4.contains(r5);	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r7.canRetry = r0;	 Catch:{ all -> 0x0152 }
            goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r4 = r1 instanceof java.io.FileNotFoundException;	 Catch:{ all -> 0x0152 }
            if (r4 == 0) goto L_0x0138;	 Catch:{ all -> 0x0152 }
            r7.canRetry = r0;	 Catch:{ all -> 0x0152 }
            org.telegram.messenger.FileLog.m30e(r1);	 Catch:{ all -> 0x0152 }
            r0 = r7.httpConnection;	 Catch:{ Throwable -> 0x0144 }
            if (r0 == 0) goto L_0x0144;	 Catch:{ Throwable -> 0x0144 }
            r0 = r7.httpConnection;	 Catch:{ Throwable -> 0x0144 }
            r0.disconnect();	 Catch:{ Throwable -> 0x0144 }
            if (r3 == 0) goto L_0x014e;
            r3.close();	 Catch:{ Throwable -> 0x014a }
            goto L_0x014e;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            if (r2 == 0) goto L_0x0151;
            goto L_0x00e9;
            return r8;
            r0 = move-exception;
            r8 = r3;
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x015d }
            if (r1 == 0) goto L_0x015d;	 Catch:{ Throwable -> 0x015d }
            r1 = r7.httpConnection;	 Catch:{ Throwable -> 0x015d }
            r1.disconnect();	 Catch:{ Throwable -> 0x015d }
            if (r8 == 0) goto L_0x0167;
            r8.close();	 Catch:{ Throwable -> 0x0163 }
            goto L_0x0167;
            r8 = move-exception;
            org.telegram.messenger.FileLog.m30e(r8);
            if (r2 == 0) goto L_0x016c;
            r2.close();	 Catch:{ Exception -> 0x016c }
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader$ArtworkLoadTask.doInBackground(java.lang.Void[]):java.lang.String");
        }

        public ArtworkLoadTask(CacheImage cacheImage) {
            boolean z = true;
            this.cacheImage = cacheImage;
            if (Uri.parse(cacheImage.imageLocation.path).getQueryParameter("s") == null) {
                z = false;
            }
            this.small = z;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String str) {
            if (str != null) {
                CacheImage cacheImage = this.cacheImage;
                cacheImage.httpTask = new HttpImageTask(cacheImage, 0, str);
                ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
                ImageLoader.this.runHttpTasks(false);
            } else if (this.canRetry) {
                ImageLoader.this.artworkLoadError(this.cacheImage.url);
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new C0480xab18488c(this));
        }

        public /* synthetic */ void lambda$onPostExecute$0$ImageLoader$ArtworkLoadTask() {
            ImageLoader.this.runArtworkTasks(true);
        }

        public /* synthetic */ void lambda$onCancelled$1$ImageLoader$ArtworkLoadTask() {
            ImageLoader.this.runArtworkTasks(true);
        }

        /* Access modifiers changed, original: protected */
        public void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new C0481xd5198ea1(this));
        }
    }

    private class CacheImage {
        protected boolean animatedFile;
        protected ArtworkLoadTask artworkTask;
        protected CacheOutTask cacheTask;
        protected int currentAccount;
        protected File encryptionKeyPath;
        protected String ext;
        protected String filter;
        protected ArrayList<String> filters;
        protected File finalFilePath;
        protected HttpImageTask httpTask;
        protected ImageLocation imageLocation;
        protected ArrayList<ImageReceiver> imageReceiverArray;
        protected int imageType;
        protected ArrayList<Integer> imageTypes;
        protected String key;
        protected ArrayList<String> keys;
        protected boolean lottieFile;
        protected Object parentObject;
        protected SecureDocument secureDocument;
        protected int size;
        protected File tempFilePath;
        protected String url;

        private CacheImage() {
            this.imageReceiverArray = new ArrayList();
            this.keys = new ArrayList();
            this.filters = new ArrayList();
            this.imageTypes = new ArrayList();
        }

        /* synthetic */ CacheImage(ImageLoader imageLoader, C10291 c10291) {
            this();
        }

        public void addImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i) {
            if (!this.imageReceiverArray.contains(imageReceiver)) {
                this.imageReceiverArray.add(imageReceiver);
                this.keys.add(str);
                this.filters.add(str2);
                this.imageTypes.add(Integer.valueOf(i));
                ImageLoader.this.imageLoadingByTag.put(imageReceiver.getTag(i), this);
            }
        }

        public void replaceImageReceiver(ImageReceiver imageReceiver, String str, String str2, int i) {
            int indexOf = this.imageReceiverArray.indexOf(imageReceiver);
            if (indexOf != -1) {
                if (((Integer) this.imageTypes.get(indexOf)).intValue() != i) {
                    ArrayList arrayList = this.imageReceiverArray;
                    indexOf = arrayList.subList(indexOf + 1, arrayList.size()).indexOf(imageReceiver);
                    if (indexOf == -1) {
                        return;
                    }
                }
                this.keys.set(indexOf, str);
                this.filters.set(indexOf, str2);
            }
        }

        public void removeImageReceiver(ImageReceiver imageReceiver) {
            int i = this.imageType;
            int i2 = 0;
            while (i2 < this.imageReceiverArray.size()) {
                ImageReceiver imageReceiver2 = (ImageReceiver) this.imageReceiverArray.get(i2);
                if (imageReceiver2 == null || imageReceiver2 == imageReceiver) {
                    this.imageReceiverArray.remove(i2);
                    this.keys.remove(i2);
                    this.filters.remove(i2);
                    i = ((Integer) this.imageTypes.remove(i2)).intValue();
                    if (imageReceiver2 != null) {
                        ImageLoader.this.imageLoadingByTag.remove(imageReceiver2.getTag(i));
                    }
                    i2--;
                }
                i2++;
            }
            if (this.imageReceiverArray.size() == 0) {
                for (int i3 = 0; i3 < this.imageReceiverArray.size(); i3++) {
                    ImageLoader.this.imageLoadingByTag.remove(((ImageReceiver) this.imageReceiverArray.get(i3)).getTag(i));
                }
                this.imageReceiverArray.clear();
                if (!(this.imageLocation == null || ImageLoader.this.forceLoadingImages.containsKey(this.key))) {
                    ImageLocation imageLocation = this.imageLocation;
                    if (imageLocation.location != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.location, this.ext);
                    } else if (imageLocation.document != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.document);
                    } else if (imageLocation.secureDocument != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.secureDocument);
                    } else if (imageLocation.webFile != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.webFile);
                    }
                }
                if (this.cacheTask != null) {
                    if (i == 1) {
                        ImageLoader.this.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
                    } else {
                        ImageLoader.this.cacheOutQueue.cancelRunnable(this.cacheTask);
                    }
                    this.cacheTask.cancel();
                    this.cacheTask = null;
                }
                if (this.httpTask != null) {
                    ImageLoader.this.httpTasks.remove(this.httpTask);
                    this.httpTask.cancel(true);
                    this.httpTask = null;
                }
                if (this.artworkTask != null) {
                    ImageLoader.this.artworkTasks.remove(this.artworkTask);
                    this.artworkTask.cancel(true);
                    this.artworkTask = null;
                }
                if (this.url != null) {
                    ImageLoader.this.imageLoadingByUrl.remove(this.url);
                }
                if (this.key != null) {
                    ImageLoader.this.imageLoadingByKeys.remove(this.key);
                }
            }
        }

        public void setImageAndClear(Drawable drawable, String str) {
            if (drawable != null) {
                AndroidUtilities.runOnUIThread(new C0482-$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE(this, drawable, new ArrayList(this.imageReceiverArray), str));
            }
            for (int i = 0; i < this.imageReceiverArray.size(); i++) {
                ImageLoader.this.imageLoadingByTag.remove(((ImageReceiver) this.imageReceiverArray.get(i)).getTag(this.imageType));
            }
            this.imageReceiverArray.clear();
            if (this.url != null) {
                ImageLoader.this.imageLoadingByUrl.remove(this.url);
            }
            if (this.key != null) {
                ImageLoader.this.imageLoadingByKeys.remove(this.key);
            }
        }

        public /* synthetic */ void lambda$setImageAndClear$0$ImageLoader$CacheImage(Drawable drawable, ArrayList arrayList, String str) {
            int i;
            if (drawable instanceof AnimatedFileDrawable) {
                drawable = (AnimatedFileDrawable) drawable;
                Object obj = null;
                for (i = 0; i < arrayList.size(); i++) {
                    Drawable drawable2;
                    ImageReceiver imageReceiver = (ImageReceiver) arrayList.get(i);
                    if (i == 0) {
                        drawable2 = drawable;
                    } else {
                        drawable2 = drawable.makeCopy();
                    }
                    if (imageReceiver.setImageBitmapByKey(drawable2, this.key, this.imageType, false)) {
                        if (drawable2 == drawable) {
                            obj = 1;
                        }
                    } else if (drawable2 != drawable) {
                        drawable2.recycle();
                    }
                }
                if (obj == null) {
                    drawable.recycle();
                }
            } else {
                for (i = 0; i < arrayList.size(); i++) {
                    ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(drawable, this.key, ((Integer) this.imageTypes.get(i)).intValue(), false);
                }
            }
            if (str != null) {
                ImageLoader.this.decrementUseCount(str);
            }
        }
    }

    private class CacheOutTask implements Runnable {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync = new Object();

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:717:0x0977 in {6, 16, 22, 23, 25, 32, 34, 35, 36, 40, 49, 54, 56, 57, 61, 70, 78, 79, 80, 81, 82, 86, 94, 95, 96, 103, 104, 105, 106, 114, 115, 120, 121, 126, 128, 130, 132, 134, 136, 137, 143, 145, 149, 151, 152, 153, 160, 161, 162, 163, 164, 169, 170, 171, 174, 175, 178, 189, 191, 192, 196, 199, 202, 203, 209, 223, 225, 226, 227, 234, 235, 237, 242, 243, 244, 249, 250, 251, 254, 255, 256, 261, 262, 265, 267, 269, 270, 271, 273, 274, 276, 277, 279, 286, 287, 292, 297, 299, 301, 302, 303, 304, 306, 307, 308, 309, 310, 311, 321, 327, 328, 329, 333, 335, 336, 337, 342, 343, 345, 352, 353, 354, 355, 357, 358, 360, 365, 366, 367, 381, 382, 387, 388, 391, 392, 393, 395, 396, 403, 404, 405, 407, 414, 415, 416, 423, 424, 427, 428, 431, 432, 433, 438, 443, 445, 446, 447, 448, 451, 463, 464, 466, 467, 468, 469, 477, 486, 488, 490, 496, 498, 499, 510, 511, 517, 518, 520, 526, 530, 532, 543, 544, 545, 546, 547, 548, 551, 552, 554, 558, 565, 566, 568, 575, 576, 577, 578, 580, 581, 584, 585, 587, 588, 589, 590, 597, 598, 599, 617, 618, 621, 622, 623, 632, 633, 636, 637, 640, 641, 644, 645, 647, 648, 649, 659, 660, 661, 662, 663, 665, 666, 667, 668, 669, 675, 676, 678, 679, 680, 682, 683, 684, 690, 692, 693, 695, 696, 697, 698, 699, 703, 705, 706, 707, 709, 710, 711, 712, 716} preds:[]
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
        public void run() {
            /*
            r35 = this;
            r1 = r35;
            r2 = r1.sync;
            monitor-enter(r2);
            r0 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0974 }
            r1.runningThread = r0;	 Catch:{ all -> 0x0974 }
            java.lang.Thread.interrupted();	 Catch:{ all -> 0x0974 }
            r0 = r1.isCancelled;	 Catch:{ all -> 0x0974 }
            if (r0 == 0) goto L_0x0014;	 Catch:{ all -> 0x0974 }
            monitor-exit(r2);	 Catch:{ all -> 0x0974 }
            return;	 Catch:{ all -> 0x0974 }
            monitor-exit(r2);	 Catch:{ all -> 0x0974 }
            r0 = r1.cacheImage;
            r2 = r0.imageLocation;
            r2 = r2.photoSize;
            r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
            r3 = 2;
            r4 = 3;
            r6 = 1;
            r7 = 0;
            if (r2 == 0) goto L_0x00ca;
            r2 = r1.sync;
            monitor-enter(r2);
            r0 = r1.isCancelled;	 Catch:{ all -> 0x00c7 }
            if (r0 == 0) goto L_0x002c;	 Catch:{ all -> 0x00c7 }
            monitor-exit(r2);	 Catch:{ all -> 0x00c7 }
            return;	 Catch:{ all -> 0x00c7 }
            monitor-exit(r2);	 Catch:{ all -> 0x00c7 }
            r0 = r1.cacheImage;
            r0 = r0.imageLocation;
            r0 = r0.photoSize;
            r0 = (org.telegram.tgnet.TLRPC.TL_photoStrippedSize) r0;
            r2 = r0.bytes;
            r2 = r2.length;
            r2 = r2 - r4;
            r8 = org.telegram.messenger.Bitmaps.header;
            r8 = r8.length;
            r2 = r2 + r8;
            r8 = org.telegram.messenger.Bitmaps.footer;
            r8 = r8.length;
            r2 = r2 + r8;
            r8 = org.telegram.messenger.ImageLoader.bytesLocal;
            r8 = r8.get();
            r8 = (byte[]) r8;
            if (r8 == 0) goto L_0x0051;
            r9 = r8.length;
            if (r9 < r2) goto L_0x0051;
            goto L_0x0052;
            r8 = 0;
            if (r8 != 0) goto L_0x005d;
            r8 = new byte[r2];
            r9 = org.telegram.messenger.ImageLoader.bytesLocal;
            r9.set(r8);
            r9 = org.telegram.messenger.Bitmaps.header;
            r10 = r9.length;
            java.lang.System.arraycopy(r9, r7, r8, r7, r10);
            r9 = r0.bytes;
            r10 = org.telegram.messenger.Bitmaps.header;
            r10 = r10.length;
            r11 = r9.length;
            r11 = r11 - r4;
            java.lang.System.arraycopy(r9, r4, r8, r10, r11);
            r9 = org.telegram.messenger.Bitmaps.footer;
            r10 = org.telegram.messenger.Bitmaps.header;
            r10 = r10.length;
            r11 = r0.bytes;
            r11 = r11.length;
            r10 = r10 + r11;
            r10 = r10 - r4;
            r4 = org.telegram.messenger.Bitmaps.footer;
            r4 = r4.length;
            java.lang.System.arraycopy(r9, r7, r8, r10, r4);
            r4 = 164; // 0xa4 float:2.3E-43 double:8.1E-322;
            r0 = r0.bytes;
            r6 = r0[r6];
            r8[r4] = r6;
            r4 = 166; // 0xa6 float:2.33E-43 double:8.2E-322;
            r0 = r0[r3];
            r8[r4] = r0;
            r0 = android.graphics.BitmapFactory.decodeByteArray(r8, r7, r2);
            if (r0 == 0) goto L_0x00b9;
            r2 = r1.cacheImage;
            r2 = r2.filter;
            r2 = android.text.TextUtils.isEmpty(r2);
            if (r2 != 0) goto L_0x00b9;
            r2 = r1.cacheImage;
            r2 = r2.filter;
            r3 = "b";
            r2 = r2.contains(r3);
            if (r2 == 0) goto L_0x00b9;
            r10 = 3;
            r11 = 1;
            r12 = r0.getWidth();
            r13 = r0.getHeight();
            r14 = r0.getRowBytes();
            r9 = r0;
            org.telegram.messenger.Utilities.blurBitmap(r9, r10, r11, r12, r13, r14);
            if (r0 == 0) goto L_0x00c1;
            r5 = new android.graphics.drawable.BitmapDrawable;
            r5.<init>(r0);
            goto L_0x00c2;
            r5 = 0;
            r1.onPostExecute(r5);
            goto L_0x0973;
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x00c7 }
            throw r0;
            r2 = r0.lottieFile;
            if (r2 == 0) goto L_0x0118;
            r2 = r1.sync;
            monitor-enter(r2);
            r0 = r1.isCancelled;	 Catch:{ all -> 0x0115 }
            if (r0 == 0) goto L_0x00d7;	 Catch:{ all -> 0x0115 }
            monitor-exit(r2);	 Catch:{ all -> 0x0115 }
            return;	 Catch:{ all -> 0x0115 }
            monitor-exit(r2);	 Catch:{ all -> 0x0115 }
            r2 = new com.airbnb.lottie.LottieDrawable;
            r2.<init>();
            r0 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x00ff }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x00ff }
            r3 = r3.finalFilePath;	 Catch:{ Throwable -> 0x00ff }
            r0.<init>(r3);	 Catch:{ Throwable -> 0x00ff }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x00ff }
            r3 = r3.finalFilePath;	 Catch:{ Throwable -> 0x00ff }
            r3 = r3.toString();	 Catch:{ Throwable -> 0x00ff }
            r3 = com.airbnb.lottie.LottieCompositionFactory.fromJsonInputStreamSync(r0, r3);	 Catch:{ Throwable -> 0x00ff }
            r3 = r3.getValue();	 Catch:{ Throwable -> 0x00ff }
            r3 = (com.airbnb.lottie.LottieComposition) r3;	 Catch:{ Throwable -> 0x00ff }
            r2.setComposition(r3);	 Catch:{ Throwable -> 0x00ff }
            r0.close();	 Catch:{ Throwable -> 0x00ff }
            goto L_0x0103;
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            r2.setRepeatMode(r6);
            r0 = -1;
            r2.setRepeatCount(r0);
            r2.start();
            java.lang.Thread.interrupted();
            r1.onPostExecute(r2);
            goto L_0x0973;
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0115 }
            throw r0;
            r2 = r0.animatedFile;
            if (r2 == 0) goto L_0x0180;
            r2 = r1.sync;
            monitor-enter(r2);
            r0 = r1.isCancelled;	 Catch:{ all -> 0x017d }
            if (r0 == 0) goto L_0x0125;	 Catch:{ all -> 0x017d }
            monitor-exit(r2);	 Catch:{ all -> 0x017d }
            return;	 Catch:{ all -> 0x017d }
            monitor-exit(r2);	 Catch:{ all -> 0x017d }
            r0 = r1.cacheImage;
            r0 = r0.filter;
            r2 = "g";
            r0 = r2.equals(r0);
            if (r0 == 0) goto L_0x0157;
            r0 = r1.cacheImage;
            r2 = r0.imageLocation;
            r2 = r2.document;
            r3 = r2 instanceof org.telegram.tgnet.TLRPC.TL_documentEncrypted;
            if (r3 != 0) goto L_0x0157;
            r3 = new org.telegram.ui.Components.AnimatedFileDrawable;
            r7 = r0.finalFilePath;
            r8 = 0;
            r0 = r0.size;
            r9 = (long) r0;
            r0 = r2 instanceof org.telegram.tgnet.TLRPC.Document;
            if (r0 == 0) goto L_0x014a;
            r11 = r2;
            goto L_0x014b;
            r11 = 0;
            r0 = r1.cacheImage;
            r12 = r0.parentObject;
            r13 = r0.currentAccount;
            r6 = r3;
            r6.<init>(r7, r8, r9, r11, r12, r13);
            r0 = r3;
            goto L_0x0175;
            r0 = new org.telegram.ui.Components.AnimatedFileDrawable;
            r2 = r1.cacheImage;
            r15 = r2.finalFilePath;
            r2 = r2.filter;
            r3 = "d";
            r16 = r3.equals(r2);
            r17 = 0;
            r19 = 0;
            r20 = 0;
            r2 = r1.cacheImage;
            r2 = r2.currentAccount;
            r14 = r0;
            r21 = r2;
            r14.<init>(r15, r16, r17, r19, r20, r21);
            java.lang.Thread.interrupted();
            r1.onPostExecute(r0);
            goto L_0x0973;
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x017d }
            throw r0;
            r2 = r0.finalFilePath;
            r8 = r0.secureDocument;
            if (r8 != 0) goto L_0x019b;
            r0 = r0.encryptionKeyPath;
            if (r0 == 0) goto L_0x0199;
            if (r2 == 0) goto L_0x0199;
            r0 = r2.getAbsolutePath();
            r8 = ".enc";
            r0 = r0.endsWith(r8);
            if (r0 == 0) goto L_0x0199;
            goto L_0x019b;
            r8 = 0;
            goto L_0x019c;
            r8 = 1;
            r0 = r1.cacheImage;
            r0 = r0.secureDocument;
            if (r0 == 0) goto L_0x01b5;
            r9 = r0.secureDocumentKey;
            r0 = r0.secureFile;
            if (r0 == 0) goto L_0x01ad;
            r0 = r0.file_hash;
            if (r0 == 0) goto L_0x01ad;
            goto L_0x01b3;
            r0 = r1.cacheImage;
            r0 = r0.secureDocument;
            r0 = r0.fileHash;
            r10 = r0;
            goto L_0x01b7;
            r9 = 0;
            r10 = 0;
            r0 = android.os.Build.VERSION.SDK_INT;
            r11 = 19;
            if (r0 >= r11) goto L_0x0226;
            r11 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x020c, all -> 0x0208 }
            r0 = "r";	 Catch:{ Exception -> 0x020c, all -> 0x0208 }
            r11.<init>(r2, r0);	 Catch:{ Exception -> 0x020c, all -> 0x0208 }
            r0 = r1.cacheImage;	 Catch:{ Exception -> 0x0206 }
            r0 = r0.imageType;	 Catch:{ Exception -> 0x0206 }
            if (r0 != r6) goto L_0x01cf;	 Catch:{ Exception -> 0x0206 }
            r0 = org.telegram.messenger.ImageLoader.headerThumb;	 Catch:{ Exception -> 0x0206 }
            goto L_0x01d3;	 Catch:{ Exception -> 0x0206 }
            r0 = org.telegram.messenger.ImageLoader.header;	 Catch:{ Exception -> 0x0206 }
            r12 = r0.length;	 Catch:{ Exception -> 0x0206 }
            r11.readFully(r0, r7, r12);	 Catch:{ Exception -> 0x0206 }
            r12 = new java.lang.String;	 Catch:{ Exception -> 0x0206 }
            r12.<init>(r0);	 Catch:{ Exception -> 0x0206 }
            r0 = r12.toLowerCase();	 Catch:{ Exception -> 0x0206 }
            r0 = r0.toLowerCase();	 Catch:{ Exception -> 0x0206 }
            r12 = "riff";	 Catch:{ Exception -> 0x0206 }
            r12 = r0.startsWith(r12);	 Catch:{ Exception -> 0x0206 }
            if (r12 == 0) goto L_0x01f6;	 Catch:{ Exception -> 0x0206 }
            r12 = "webp";	 Catch:{ Exception -> 0x0206 }
            r0 = r0.endsWith(r12);	 Catch:{ Exception -> 0x0206 }
            if (r0 == 0) goto L_0x01f6;
            r12 = 1;
            goto L_0x01f7;
            r12 = 0;
            r11.close();	 Catch:{ Exception -> 0x0204 }
            r11.close();	 Catch:{ Exception -> 0x01fe }
            goto L_0x0227;
            r0 = move-exception;
            r11 = r0;
            org.telegram.messenger.FileLog.m30e(r11);
            goto L_0x0227;
            r0 = move-exception;
            goto L_0x020f;
            r0 = move-exception;
            goto L_0x020e;
            r0 = move-exception;
            r2 = r0;
            r11 = 0;
            goto L_0x021a;
            r0 = move-exception;
            r11 = 0;
            r12 = 0;
            org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ all -> 0x0218 }
            if (r11 == 0) goto L_0x0227;
            r11.close();	 Catch:{ Exception -> 0x01fe }
            goto L_0x0227;
            r0 = move-exception;
            r2 = r0;
            if (r11 == 0) goto L_0x0225;
            r11.close();	 Catch:{ Exception -> 0x0220 }
            goto L_0x0225;
            r0 = move-exception;
            r3 = r0;
            org.telegram.messenger.FileLog.m30e(r3);
            throw r2;
            r12 = 0;
            r0 = r1.cacheImage;
            r0 = r0.imageLocation;
            r0 = r0.path;
            r11 = 8;
            if (r0 == 0) goto L_0x028b;
            r13 = "thumb://";
            r13 = r0.startsWith(r13);
            if (r13 == 0) goto L_0x025a;
            r13 = ":";
            r13 = r0.indexOf(r13, r11);
            if (r13 < 0) goto L_0x0253;
            r14 = r0.substring(r11, r13);
            r14 = java.lang.Long.parseLong(r14);
            r14 = java.lang.Long.valueOf(r14);
            r13 = r13 + r6;
            r0 = r0.substring(r13);
            goto L_0x0255;
            r0 = 0;
            r14 = 0;
            r13 = r0;
            r15 = 0;
            r16 = 0;
            goto L_0x0290;
            r13 = "vthumb://";
            r13 = r0.startsWith(r13);
            if (r13 == 0) goto L_0x0280;
            r13 = 9;
            r14 = ":";
            r14 = r0.indexOf(r14, r13);
            if (r14 < 0) goto L_0x027a;
            r0 = r0.substring(r13, r14);
            r13 = java.lang.Long.parseLong(r0);
            r0 = java.lang.Long.valueOf(r13);
            r13 = 1;
            goto L_0x027c;
            r0 = 0;
            r13 = 0;
            r14 = r0;
            r15 = r13;
            r13 = 0;
            goto L_0x0257;
            r13 = "http";
            r0 = r0.startsWith(r13);
            if (r0 != 0) goto L_0x028b;
            r13 = 0;
            r14 = 0;
            goto L_0x0256;
            r13 = 0;
            r14 = 0;
            r15 = 0;
            r16 = 1;
            r11 = new android.graphics.BitmapFactory$Options;
            r11.<init>();
            r11.inSampleSize = r6;
            r0 = android.os.Build.VERSION.SDK_INT;
            r4 = 21;
            if (r0 >= r4) goto L_0x029f;
            r11.inPurgeable = r6;
            r0 = org.telegram.messenger.ImageLoader.this;
            r19 = r0.canForce8888;
            r20 = 0;
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0473 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0473 }
            r21 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x0473 }
            if (r0 == 0) goto L_0x0420;	 Catch:{ Throwable -> 0x0473 }
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0473 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0473 }
            r4 = "_";	 Catch:{ Throwable -> 0x0473 }
            r0 = r0.split(r4);	 Catch:{ Throwable -> 0x0473 }
            r4 = r0.length;	 Catch:{ Throwable -> 0x0473 }
            if (r4 < r3) goto L_0x02da;	 Catch:{ Throwable -> 0x0473 }
            r4 = r0[r7];	 Catch:{ Throwable -> 0x0473 }
            r4 = java.lang.Float.parseFloat(r4);	 Catch:{ Throwable -> 0x0473 }
            r22 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x0473 }
            r4 = r4 * r22;
            r0 = r0[r6];	 Catch:{ Throwable -> 0x02d3 }
            r0 = java.lang.Float.parseFloat(r0);	 Catch:{ Throwable -> 0x02d3 }
            r22 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x02d3 }
            r0 = r0 * r22;
            r22 = r0;
            goto L_0x02dd;
            r0 = move-exception;
            r7 = r12;
            r25 = r13;
            r3 = 0;
            goto L_0x0479;
            r4 = 0;
            r22 = 0;
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0419 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0419 }
            r3 = "b2";	 Catch:{ Throwable -> 0x0419 }
            r0 = r0.contains(r3);	 Catch:{ Throwable -> 0x0419 }
            if (r0 == 0) goto L_0x02eb;	 Catch:{ Throwable -> 0x0419 }
            r3 = 3;	 Catch:{ Throwable -> 0x0419 }
            goto L_0x0308;	 Catch:{ Throwable -> 0x0419 }
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0419 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0419 }
            r3 = "b1";	 Catch:{ Throwable -> 0x0419 }
            r0 = r0.contains(r3);	 Catch:{ Throwable -> 0x0419 }
            if (r0 == 0) goto L_0x02f9;	 Catch:{ Throwable -> 0x0419 }
            r3 = 2;	 Catch:{ Throwable -> 0x0419 }
            goto L_0x0308;	 Catch:{ Throwable -> 0x0419 }
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0419 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0419 }
            r3 = "b";	 Catch:{ Throwable -> 0x0419 }
            r0 = r0.contains(r3);	 Catch:{ Throwable -> 0x0419 }
            if (r0 == 0) goto L_0x0307;
            r3 = 1;
            goto L_0x0308;
            r3 = 0;
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x040f }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x040f }
            r5 = "i";	 Catch:{ Throwable -> 0x040f }
            r5 = r0.contains(r5);	 Catch:{ Throwable -> 0x040f }
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0407 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0407 }
            r7 = "f";	 Catch:{ Throwable -> 0x0407 }
            r0 = r0.contains(r7);	 Catch:{ Throwable -> 0x0407 }
            if (r0 == 0) goto L_0x0320;	 Catch:{ Throwable -> 0x0407 }
            r19 = 1;	 Catch:{ Throwable -> 0x0407 }
            if (r12 != 0) goto L_0x03fe;	 Catch:{ Throwable -> 0x0407 }
            r0 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));	 Catch:{ Throwable -> 0x0407 }
            if (r0 == 0) goto L_0x03fe;	 Catch:{ Throwable -> 0x0407 }
            r0 = (r22 > r20 ? 1 : (r22 == r20 ? 0 : -1));	 Catch:{ Throwable -> 0x0407 }
            if (r0 == 0) goto L_0x03fe;	 Catch:{ Throwable -> 0x0407 }
            r11.inJustDecodeBounds = r6;	 Catch:{ Throwable -> 0x0407 }
            if (r14 == 0) goto L_0x035d;
            if (r13 != 0) goto L_0x035d;
            if (r15 == 0) goto L_0x0349;
            r0 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0343 }
            r0 = r0.getContentResolver();	 Catch:{ Throwable -> 0x0343 }
            r7 = r12;
            r25 = r13;
            r12 = r14.longValue();	 Catch:{ Throwable -> 0x03b4 }
            android.provider.MediaStore.Video.Thumbnails.getThumbnail(r0, r12, r6, r11);	 Catch:{ Throwable -> 0x03b4 }
            goto L_0x0359;	 Catch:{ Throwable -> 0x03b4 }
            r0 = move-exception;	 Catch:{ Throwable -> 0x03b4 }
            r7 = r12;	 Catch:{ Throwable -> 0x03b4 }
            r25 = r13;	 Catch:{ Throwable -> 0x03b4 }
            goto L_0x03b5;	 Catch:{ Throwable -> 0x03b4 }
            r7 = r12;	 Catch:{ Throwable -> 0x03b4 }
            r25 = r13;	 Catch:{ Throwable -> 0x03b4 }
            r0 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x03b4 }
            r0 = r0.getContentResolver();	 Catch:{ Throwable -> 0x03b4 }
            r12 = r14.longValue();	 Catch:{ Throwable -> 0x03b4 }
            android.provider.MediaStore.Images.Thumbnails.getThumbnail(r0, r12, r6, r11);	 Catch:{ Throwable -> 0x03b4 }
            r26 = r3;	 Catch:{ Throwable -> 0x03b4 }
            goto L_0x03d2;	 Catch:{ Throwable -> 0x03b4 }
            r7 = r12;	 Catch:{ Throwable -> 0x03b4 }
            r25 = r13;	 Catch:{ Throwable -> 0x03b4 }
            if (r9 == 0) goto L_0x03b8;	 Catch:{ Throwable -> 0x03b4 }
            r0 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x03b4 }
            r12 = "r";	 Catch:{ Throwable -> 0x03b4 }
            r0.<init>(r2, r12);	 Catch:{ Throwable -> 0x03b4 }
            r12 = r0.length();	 Catch:{ Throwable -> 0x03b4 }
            r13 = (int) r12;	 Catch:{ Throwable -> 0x03b4 }
            r12 = org.telegram.messenger.ImageLoader.bytesLocal;	 Catch:{ Throwable -> 0x03b4 }
            r12 = r12.get();	 Catch:{ Throwable -> 0x03b4 }
            r12 = (byte[]) r12;	 Catch:{ Throwable -> 0x03b4 }
            if (r12 == 0) goto L_0x037e;	 Catch:{ Throwable -> 0x03b4 }
            r6 = r12.length;	 Catch:{ Throwable -> 0x03b4 }
            if (r6 < r13) goto L_0x037e;	 Catch:{ Throwable -> 0x03b4 }
            goto L_0x037f;	 Catch:{ Throwable -> 0x03b4 }
            r12 = 0;	 Catch:{ Throwable -> 0x03b4 }
            if (r12 != 0) goto L_0x038a;	 Catch:{ Throwable -> 0x03b4 }
            r12 = new byte[r13];	 Catch:{ Throwable -> 0x03b4 }
            r6 = org.telegram.messenger.ImageLoader.bytesLocal;	 Catch:{ Throwable -> 0x03b4 }
            r6.set(r12);	 Catch:{ Throwable -> 0x03b4 }
            r6 = 0;	 Catch:{ Throwable -> 0x03b4 }
            r0.readFully(r12, r6, r13);	 Catch:{ Throwable -> 0x03b4 }
            r0.close();	 Catch:{ Throwable -> 0x03b4 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r12, r6, r13, r9);	 Catch:{ Throwable -> 0x03b4 }
            r0 = org.telegram.messenger.Utilities.computeSHA256(r12, r6, r13);	 Catch:{ Throwable -> 0x03b4 }
            if (r10 == 0) goto L_0x03a5;	 Catch:{ Throwable -> 0x03b4 }
            r0 = java.util.Arrays.equals(r0, r10);	 Catch:{ Throwable -> 0x03b4 }
            if (r0 != 0) goto L_0x03a1;
            goto L_0x03a5;
            r26 = r3;
            r0 = 0;
            goto L_0x03a8;
            r26 = r3;
            r0 = 1;
            r6 = 0;
            r3 = r12[r6];	 Catch:{ Throwable -> 0x03fc }
            r3 = r3 & 255;	 Catch:{ Throwable -> 0x03fc }
            r13 = r13 - r3;	 Catch:{ Throwable -> 0x03fc }
            if (r0 != 0) goto L_0x03d2;	 Catch:{ Throwable -> 0x03fc }
            android.graphics.BitmapFactory.decodeByteArray(r12, r3, r13, r11);	 Catch:{ Throwable -> 0x03fc }
            goto L_0x03d2;	 Catch:{ Throwable -> 0x03fc }
            r0 = move-exception;	 Catch:{ Throwable -> 0x03fc }
            r26 = r3;	 Catch:{ Throwable -> 0x03fc }
            goto L_0x040d;	 Catch:{ Throwable -> 0x03fc }
            r26 = r3;	 Catch:{ Throwable -> 0x03fc }
            if (r8 == 0) goto L_0x03c6;	 Catch:{ Throwable -> 0x03fc }
            r0 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream;	 Catch:{ Throwable -> 0x03fc }
            r3 = r1.cacheImage;	 Catch:{ Throwable -> 0x03fc }
            r3 = r3.encryptionKeyPath;	 Catch:{ Throwable -> 0x03fc }
            r0.<init>(r2, r3);	 Catch:{ Throwable -> 0x03fc }
            goto L_0x03cb;	 Catch:{ Throwable -> 0x03fc }
            r0 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x03fc }
            r0.<init>(r2);	 Catch:{ Throwable -> 0x03fc }
            r3 = 0;	 Catch:{ Throwable -> 0x03fc }
            android.graphics.BitmapFactory.decodeStream(r0, r3, r11);	 Catch:{ Throwable -> 0x03fc }
            r0.close();	 Catch:{ Throwable -> 0x03fc }
            r0 = r11.outWidth;	 Catch:{ Throwable -> 0x03fc }
            r0 = (float) r0;	 Catch:{ Throwable -> 0x03fc }
            r3 = r11.outHeight;	 Catch:{ Throwable -> 0x03fc }
            r3 = (float) r3;	 Catch:{ Throwable -> 0x03fc }
            r6 = (r4 > r22 ? 1 : (r4 == r22 ? 0 : -1));	 Catch:{ Throwable -> 0x03fc }
            if (r6 <= 0) goto L_0x03e8;	 Catch:{ Throwable -> 0x03fc }
            r6 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));	 Catch:{ Throwable -> 0x03fc }
            if (r6 <= 0) goto L_0x03e8;	 Catch:{ Throwable -> 0x03fc }
            r0 = r0 / r4;	 Catch:{ Throwable -> 0x03fc }
            r3 = r3 / r22;	 Catch:{ Throwable -> 0x03fc }
            r0 = java.lang.Math.max(r0, r3);	 Catch:{ Throwable -> 0x03fc }
            goto L_0x03ef;	 Catch:{ Throwable -> 0x03fc }
            r0 = r0 / r4;	 Catch:{ Throwable -> 0x03fc }
            r3 = r3 / r22;	 Catch:{ Throwable -> 0x03fc }
            r0 = java.lang.Math.min(r0, r3);	 Catch:{ Throwable -> 0x03fc }
            r3 = (r0 > r21 ? 1 : (r0 == r21 ? 0 : -1));	 Catch:{ Throwable -> 0x03fc }
            if (r3 >= 0) goto L_0x03f5;	 Catch:{ Throwable -> 0x03fc }
            r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x03fc }
            r3 = 0;	 Catch:{ Throwable -> 0x03fc }
            r11.inJustDecodeBounds = r3;	 Catch:{ Throwable -> 0x03fc }
            r0 = (int) r0;	 Catch:{ Throwable -> 0x03fc }
            r11.inSampleSize = r0;	 Catch:{ Throwable -> 0x03fc }
            goto L_0x0403;
            r0 = move-exception;
            goto L_0x040d;
            r26 = r3;
            r7 = r12;
            r25 = r13;
            r0 = r5;
            r5 = 0;
            goto L_0x0471;
            r0 = move-exception;
            r26 = r3;
            r7 = r12;
            r25 = r13;
            r3 = r5;
            goto L_0x0416;
            r0 = move-exception;
            r26 = r3;
            r7 = r12;
            r25 = r13;
            r3 = 0;
            r5 = 0;
            goto L_0x047e;
            r0 = move-exception;
            r7 = r12;
            r25 = r13;
            r3 = 0;
            r5 = 0;
            goto L_0x047c;
            r7 = r12;
            r25 = r13;
            if (r25 == 0) goto L_0x046a;
            r3 = 1;
            r11.inJustDecodeBounds = r3;	 Catch:{ Throwable -> 0x0468 }
            if (r19 == 0) goto L_0x042d;	 Catch:{ Throwable -> 0x0468 }
            r0 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0468 }
            goto L_0x042f;	 Catch:{ Throwable -> 0x0468 }
            r0 = android.graphics.Bitmap.Config.RGB_565;	 Catch:{ Throwable -> 0x0468 }
            r11.inPreferredConfig = r0;	 Catch:{ Throwable -> 0x0468 }
            r0 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0468 }
            r0.<init>(r2);	 Catch:{ Throwable -> 0x0468 }
            r3 = 0;	 Catch:{ Throwable -> 0x0468 }
            r5 = android.graphics.BitmapFactory.decodeStream(r0, r3, r11);	 Catch:{ Throwable -> 0x0468 }
            r0.close();	 Catch:{ Throwable -> 0x0464 }
            r0 = r11.outWidth;	 Catch:{ Throwable -> 0x0464 }
            r3 = r11.outHeight;	 Catch:{ Throwable -> 0x0464 }
            r4 = 0;	 Catch:{ Throwable -> 0x0464 }
            r11.inJustDecodeBounds = r4;	 Catch:{ Throwable -> 0x0464 }
            r0 = r0 / 200;	 Catch:{ Throwable -> 0x0464 }
            r3 = r3 / 200;	 Catch:{ Throwable -> 0x0464 }
            r0 = java.lang.Math.max(r0, r3);	 Catch:{ Throwable -> 0x0464 }
            r0 = (float) r0;	 Catch:{ Throwable -> 0x0464 }
            r3 = (r0 > r21 ? 1 : (r0 == r21 ? 0 : -1));	 Catch:{ Throwable -> 0x0464 }
            if (r3 >= 0) goto L_0x0454;	 Catch:{ Throwable -> 0x0464 }
            r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x0464 }
            r3 = 1;	 Catch:{ Throwable -> 0x0464 }
            r4 = 2;	 Catch:{ Throwable -> 0x0464 }
            r3 = r3 * 2;	 Catch:{ Throwable -> 0x0464 }
            r4 = r3 * 2;	 Catch:{ Throwable -> 0x0464 }
            r4 = (float) r4;	 Catch:{ Throwable -> 0x0464 }
            r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));	 Catch:{ Throwable -> 0x0464 }
            if (r4 < 0) goto L_0x0455;	 Catch:{ Throwable -> 0x0464 }
            r11.inSampleSize = r3;	 Catch:{ Throwable -> 0x0464 }
            r0 = 0;
            r4 = 0;
            goto L_0x046d;
            r0 = move-exception;
            r3 = 0;
            r4 = 0;
            goto L_0x047a;
            r0 = move-exception;
            goto L_0x0477;
            r0 = 0;
            r4 = 0;
            r5 = 0;
            r22 = 0;
            r26 = 0;
            r3 = r0;
            goto L_0x0481;
            r0 = move-exception;
            r7 = r12;
            r25 = r13;
            r3 = 0;
            r4 = 0;
            r5 = 0;
            r22 = 0;
            r26 = 0;
            org.telegram.messenger.FileLog.m30e(r0);
            r6 = r22;
            r0 = r26;
            r12 = r1.cacheImage;
            r12 = r12.imageType;
            r21 = r14;
            r13 = 1;
            if (r12 != r13) goto L_0x068f;
            r6 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x0685 }
            r12 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x0685 }
            r6.lastCacheOutTime = r12;	 Catch:{ Throwable -> 0x0685 }
            r6 = r1.sync;	 Catch:{ Throwable -> 0x0685 }
            monitor-enter(r6);	 Catch:{ Throwable -> 0x0685 }
            r12 = r1.isCancelled;	 Catch:{ all -> 0x0682 }
            if (r12 == 0) goto L_0x04a0;	 Catch:{ all -> 0x0682 }
            monitor-exit(r6);	 Catch:{ all -> 0x0682 }
            return;	 Catch:{ all -> 0x0682 }
            monitor-exit(r6);	 Catch:{ all -> 0x0682 }
            if (r7 == 0) goto L_0x04e8;
            r6 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0685 }
            r7 = "r";	 Catch:{ Throwable -> 0x0685 }
            r6.<init>(r2, r7);	 Catch:{ Throwable -> 0x0685 }
            r12 = r6.getChannel();	 Catch:{ Throwable -> 0x0685 }
            r13 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x0685 }
            r14 = 0;	 Catch:{ Throwable -> 0x0685 }
            r16 = r2.length();	 Catch:{ Throwable -> 0x0685 }
            r7 = r12.map(r13, r14, r16);	 Catch:{ Throwable -> 0x0685 }
            r8 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x0685 }
            r8.<init>();	 Catch:{ Throwable -> 0x0685 }
            r9 = 1;	 Catch:{ Throwable -> 0x0685 }
            r8.inJustDecodeBounds = r9;	 Catch:{ Throwable -> 0x0685 }
            r10 = r7.limit();	 Catch:{ Throwable -> 0x0685 }
            r12 = 0;	 Catch:{ Throwable -> 0x0685 }
            org.telegram.messenger.Utilities.loadWebpImage(r12, r7, r10, r8, r9);	 Catch:{ Throwable -> 0x0685 }
            r9 = r8.outWidth;	 Catch:{ Throwable -> 0x0685 }
            r8 = r8.outHeight;	 Catch:{ Throwable -> 0x0685 }
            r10 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0685 }
            r5 = org.telegram.messenger.Bitmaps.createBitmap(r9, r8, r10);	 Catch:{ Throwable -> 0x0685 }
            r8 = r7.limit();	 Catch:{ Throwable -> 0x0685 }
            r9 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0685 }
            if (r9 != 0) goto L_0x04de;	 Catch:{ Throwable -> 0x0685 }
            r9 = 1;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x04df;	 Catch:{ Throwable -> 0x0685 }
            r9 = 0;	 Catch:{ Throwable -> 0x0685 }
            r10 = 0;	 Catch:{ Throwable -> 0x0685 }
            org.telegram.messenger.Utilities.loadWebpImage(r5, r7, r8, r10, r9);	 Catch:{ Throwable -> 0x0685 }
            r6.close();	 Catch:{ Throwable -> 0x0685 }
            goto L_0x0566;	 Catch:{ Throwable -> 0x0685 }
            r6 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0685 }
            if (r6 != 0) goto L_0x0509;	 Catch:{ Throwable -> 0x0685 }
            if (r9 == 0) goto L_0x04ef;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x0509;	 Catch:{ Throwable -> 0x0685 }
            if (r8 == 0) goto L_0x04fb;	 Catch:{ Throwable -> 0x0685 }
            r6 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream;	 Catch:{ Throwable -> 0x0685 }
            r7 = r1.cacheImage;	 Catch:{ Throwable -> 0x0685 }
            r7 = r7.encryptionKeyPath;	 Catch:{ Throwable -> 0x0685 }
            r6.<init>(r2, r7);	 Catch:{ Throwable -> 0x0685 }
            goto L_0x0500;	 Catch:{ Throwable -> 0x0685 }
            r6 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0685 }
            r6.<init>(r2);	 Catch:{ Throwable -> 0x0685 }
            r7 = 0;	 Catch:{ Throwable -> 0x0685 }
            r5 = android.graphics.BitmapFactory.decodeStream(r6, r7, r11);	 Catch:{ Throwable -> 0x0685 }
            r6.close();	 Catch:{ Throwable -> 0x0685 }
            goto L_0x0566;	 Catch:{ Throwable -> 0x0685 }
            r6 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0685 }
            r7 = "r";	 Catch:{ Throwable -> 0x0685 }
            r6.<init>(r2, r7);	 Catch:{ Throwable -> 0x0685 }
            r12 = r6.length();	 Catch:{ Throwable -> 0x0685 }
            r7 = (int) r12;	 Catch:{ Throwable -> 0x0685 }
            r12 = org.telegram.messenger.ImageLoader.bytesThumbLocal;	 Catch:{ Throwable -> 0x0685 }
            r12 = r12.get();	 Catch:{ Throwable -> 0x0685 }
            r12 = (byte[]) r12;	 Catch:{ Throwable -> 0x0685 }
            if (r12 == 0) goto L_0x0525;	 Catch:{ Throwable -> 0x0685 }
            r13 = r12.length;	 Catch:{ Throwable -> 0x0685 }
            if (r13 < r7) goto L_0x0525;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x0526;	 Catch:{ Throwable -> 0x0685 }
            r12 = 0;	 Catch:{ Throwable -> 0x0685 }
            if (r12 != 0) goto L_0x0531;	 Catch:{ Throwable -> 0x0685 }
            r12 = new byte[r7];	 Catch:{ Throwable -> 0x0685 }
            r13 = org.telegram.messenger.ImageLoader.bytesThumbLocal;	 Catch:{ Throwable -> 0x0685 }
            r13.set(r12);	 Catch:{ Throwable -> 0x0685 }
            r13 = 0;	 Catch:{ Throwable -> 0x0685 }
            r6.readFully(r12, r13, r7);	 Catch:{ Throwable -> 0x0685 }
            r6.close();	 Catch:{ Throwable -> 0x0685 }
            if (r9 == 0) goto L_0x0554;	 Catch:{ Throwable -> 0x0685 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r12, r13, r7, r9);	 Catch:{ Throwable -> 0x0685 }
            r6 = org.telegram.messenger.Utilities.computeSHA256(r12, r13, r7);	 Catch:{ Throwable -> 0x0685 }
            if (r10 == 0) goto L_0x054c;	 Catch:{ Throwable -> 0x0685 }
            r6 = java.util.Arrays.equals(r6, r10);	 Catch:{ Throwable -> 0x0685 }
            if (r6 != 0) goto L_0x054a;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x054c;	 Catch:{ Throwable -> 0x0685 }
            r6 = 0;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x054d;	 Catch:{ Throwable -> 0x0685 }
            r6 = 1;	 Catch:{ Throwable -> 0x0685 }
            r8 = 0;	 Catch:{ Throwable -> 0x0685 }
            r9 = r12[r8];	 Catch:{ Throwable -> 0x0685 }
            r8 = r9 & 255;	 Catch:{ Throwable -> 0x0685 }
            r7 = r7 - r8;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x0560;	 Catch:{ Throwable -> 0x0685 }
            if (r8 == 0) goto L_0x055e;	 Catch:{ Throwable -> 0x0685 }
            r6 = r1.cacheImage;	 Catch:{ Throwable -> 0x0685 }
            r6 = r6.encryptionKeyPath;	 Catch:{ Throwable -> 0x0685 }
            r8 = 0;	 Catch:{ Throwable -> 0x0685 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r12, r8, r7, r6);	 Catch:{ Throwable -> 0x0685 }
            r6 = 0;	 Catch:{ Throwable -> 0x0685 }
            r8 = 0;	 Catch:{ Throwable -> 0x0685 }
            if (r6 != 0) goto L_0x0566;	 Catch:{ Throwable -> 0x0685 }
            r5 = android.graphics.BitmapFactory.decodeByteArray(r12, r8, r7, r11);	 Catch:{ Throwable -> 0x0685 }
            if (r5 != 0) goto L_0x057f;	 Catch:{ Throwable -> 0x0685 }
            r3 = r2.length();	 Catch:{ Throwable -> 0x0685 }
            r6 = 0;	 Catch:{ Throwable -> 0x0685 }
            r0 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));	 Catch:{ Throwable -> 0x0685 }
            if (r0 == 0) goto L_0x0578;	 Catch:{ Throwable -> 0x0685 }
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x0685 }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x0685 }
            if (r0 != 0) goto L_0x057b;	 Catch:{ Throwable -> 0x0685 }
            r2.delete();	 Catch:{ Throwable -> 0x0685 }
            r2 = r5;	 Catch:{ Throwable -> 0x0685 }
            r7 = 0;	 Catch:{ Throwable -> 0x0685 }
            goto L_0x068b;	 Catch:{ Throwable -> 0x0685 }
            r2 = r1.cacheImage;	 Catch:{ Throwable -> 0x0685 }
            r2 = r2.filter;	 Catch:{ Throwable -> 0x0685 }
            if (r2 == 0) goto L_0x05b2;	 Catch:{ Throwable -> 0x0685 }
            r2 = r5.getWidth();	 Catch:{ Throwable -> 0x0685 }
            r2 = (float) r2;	 Catch:{ Throwable -> 0x0685 }
            r6 = r5.getHeight();	 Catch:{ Throwable -> 0x0685 }
            r6 = (float) r6;	 Catch:{ Throwable -> 0x0685 }
            r7 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0685 }
            if (r7 != 0) goto L_0x05b2;	 Catch:{ Throwable -> 0x0685 }
            r7 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));	 Catch:{ Throwable -> 0x0685 }
            if (r7 == 0) goto L_0x05b2;	 Catch:{ Throwable -> 0x0685 }
            r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));	 Catch:{ Throwable -> 0x0685 }
            if (r7 == 0) goto L_0x05b2;	 Catch:{ Throwable -> 0x0685 }
            r7 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;	 Catch:{ Throwable -> 0x0685 }
            r13 = r4 + r7;	 Catch:{ Throwable -> 0x0685 }
            r7 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1));	 Catch:{ Throwable -> 0x0685 }
            if (r7 <= 0) goto L_0x05b2;	 Catch:{ Throwable -> 0x0685 }
            r2 = r2 / r4;	 Catch:{ Throwable -> 0x0685 }
            r4 = (int) r4;	 Catch:{ Throwable -> 0x0685 }
            r6 = r6 / r2;	 Catch:{ Throwable -> 0x0685 }
            r2 = (int) r6;	 Catch:{ Throwable -> 0x0685 }
            r6 = 1;	 Catch:{ Throwable -> 0x0685 }
            r2 = org.telegram.messenger.Bitmaps.createScaledBitmap(r5, r4, r2, r6);	 Catch:{ Throwable -> 0x0685 }
            if (r5 == r2) goto L_0x05b2;	 Catch:{ Throwable -> 0x0685 }
            r5.recycle();	 Catch:{ Throwable -> 0x0685 }
            goto L_0x05b3;
            r2 = r5;
            if (r3 == 0) goto L_0x05d6;
            r3 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05d3 }
            if (r3 == 0) goto L_0x05bb;	 Catch:{ Throwable -> 0x05d3 }
            r3 = 0;	 Catch:{ Throwable -> 0x05d3 }
            goto L_0x05bc;	 Catch:{ Throwable -> 0x05d3 }
            r3 = 1;	 Catch:{ Throwable -> 0x05d3 }
            r4 = r2.getWidth();	 Catch:{ Throwable -> 0x05d3 }
            r5 = r2.getHeight();	 Catch:{ Throwable -> 0x05d3 }
            r6 = r2.getRowBytes();	 Catch:{ Throwable -> 0x05d3 }
            r3 = org.telegram.messenger.Utilities.needInvert(r2, r3, r4, r5, r6);	 Catch:{ Throwable -> 0x05d3 }
            if (r3 == 0) goto L_0x05d0;
            r7 = 1;
            goto L_0x05d1;
            r7 = 0;
            r3 = 1;
            goto L_0x05d8;
            r0 = move-exception;
            goto L_0x0687;
            r3 = 1;
            r7 = 0;
            if (r0 != r3) goto L_0x05ff;
            r0 = r2.getConfig();	 Catch:{ Throwable -> 0x05fc }
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x05fc }
            if (r0 != r3) goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            r13 = 3;	 Catch:{ Throwable -> 0x05fc }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05fc }
            if (r0 == 0) goto L_0x05e9;	 Catch:{ Throwable -> 0x05fc }
            r14 = 0;	 Catch:{ Throwable -> 0x05fc }
            goto L_0x05ea;	 Catch:{ Throwable -> 0x05fc }
            r14 = 1;	 Catch:{ Throwable -> 0x05fc }
            r15 = r2.getWidth();	 Catch:{ Throwable -> 0x05fc }
            r16 = r2.getHeight();	 Catch:{ Throwable -> 0x05fc }
            r17 = r2.getRowBytes();	 Catch:{ Throwable -> 0x05fc }
            r12 = r2;	 Catch:{ Throwable -> 0x05fc }
            org.telegram.messenger.Utilities.blurBitmap(r12, r13, r14, r15, r16, r17);	 Catch:{ Throwable -> 0x05fc }
            goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            r0 = move-exception;	 Catch:{ Throwable -> 0x05fc }
            goto L_0x0688;	 Catch:{ Throwable -> 0x05fc }
            r3 = 2;	 Catch:{ Throwable -> 0x05fc }
            if (r0 != r3) goto L_0x0624;	 Catch:{ Throwable -> 0x05fc }
            r0 = r2.getConfig();	 Catch:{ Throwable -> 0x05fc }
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x05fc }
            if (r0 != r3) goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            r13 = 1;	 Catch:{ Throwable -> 0x05fc }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05fc }
            if (r0 == 0) goto L_0x0611;	 Catch:{ Throwable -> 0x05fc }
            r14 = 0;	 Catch:{ Throwable -> 0x05fc }
            goto L_0x0612;	 Catch:{ Throwable -> 0x05fc }
            r14 = 1;	 Catch:{ Throwable -> 0x05fc }
            r15 = r2.getWidth();	 Catch:{ Throwable -> 0x05fc }
            r16 = r2.getHeight();	 Catch:{ Throwable -> 0x05fc }
            r17 = r2.getRowBytes();	 Catch:{ Throwable -> 0x05fc }
            r12 = r2;	 Catch:{ Throwable -> 0x05fc }
            org.telegram.messenger.Utilities.blurBitmap(r12, r13, r14, r15, r16, r17);	 Catch:{ Throwable -> 0x05fc }
            goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            r3 = 3;	 Catch:{ Throwable -> 0x05fc }
            if (r0 != r3) goto L_0x0678;	 Catch:{ Throwable -> 0x05fc }
            r0 = r2.getConfig();	 Catch:{ Throwable -> 0x05fc }
            r3 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x05fc }
            if (r0 != r3) goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            r13 = 7;	 Catch:{ Throwable -> 0x05fc }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05fc }
            if (r0 == 0) goto L_0x0636;	 Catch:{ Throwable -> 0x05fc }
            r14 = 0;	 Catch:{ Throwable -> 0x05fc }
            goto L_0x0637;	 Catch:{ Throwable -> 0x05fc }
            r14 = 1;	 Catch:{ Throwable -> 0x05fc }
            r15 = r2.getWidth();	 Catch:{ Throwable -> 0x05fc }
            r16 = r2.getHeight();	 Catch:{ Throwable -> 0x05fc }
            r17 = r2.getRowBytes();	 Catch:{ Throwable -> 0x05fc }
            r12 = r2;	 Catch:{ Throwable -> 0x05fc }
            org.telegram.messenger.Utilities.blurBitmap(r12, r13, r14, r15, r16, r17);	 Catch:{ Throwable -> 0x05fc }
            r13 = 7;	 Catch:{ Throwable -> 0x05fc }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05fc }
            if (r0 == 0) goto L_0x064e;	 Catch:{ Throwable -> 0x05fc }
            r14 = 0;	 Catch:{ Throwable -> 0x05fc }
            goto L_0x064f;	 Catch:{ Throwable -> 0x05fc }
            r14 = 1;	 Catch:{ Throwable -> 0x05fc }
            r15 = r2.getWidth();	 Catch:{ Throwable -> 0x05fc }
            r16 = r2.getHeight();	 Catch:{ Throwable -> 0x05fc }
            r17 = r2.getRowBytes();	 Catch:{ Throwable -> 0x05fc }
            r12 = r2;	 Catch:{ Throwable -> 0x05fc }
            org.telegram.messenger.Utilities.blurBitmap(r12, r13, r14, r15, r16, r17);	 Catch:{ Throwable -> 0x05fc }
            r13 = 7;	 Catch:{ Throwable -> 0x05fc }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05fc }
            if (r0 == 0) goto L_0x0666;	 Catch:{ Throwable -> 0x05fc }
            r14 = 0;	 Catch:{ Throwable -> 0x05fc }
            goto L_0x0667;	 Catch:{ Throwable -> 0x05fc }
            r14 = 1;	 Catch:{ Throwable -> 0x05fc }
            r15 = r2.getWidth();	 Catch:{ Throwable -> 0x05fc }
            r16 = r2.getHeight();	 Catch:{ Throwable -> 0x05fc }
            r17 = r2.getRowBytes();	 Catch:{ Throwable -> 0x05fc }
            r12 = r2;	 Catch:{ Throwable -> 0x05fc }
            org.telegram.messenger.Utilities.blurBitmap(r12, r13, r14, r15, r16, r17);	 Catch:{ Throwable -> 0x05fc }
            goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            if (r0 != 0) goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x05fc }
            if (r0 == 0) goto L_0x068b;	 Catch:{ Throwable -> 0x05fc }
            org.telegram.messenger.Utilities.pinBitmap(r2);	 Catch:{ Throwable -> 0x05fc }
            goto L_0x068b;
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0682 }
            throw r0;	 Catch:{ Throwable -> 0x0685 }
            r0 = move-exception;
            r2 = r5;
            r7 = 0;
            org.telegram.messenger.FileLog.m30e(r0);
            r0 = 0;
            r12 = 0;
            goto L_0x0952;
            r12 = 20;
            if (r21 == 0) goto L_0x0694;
            r12 = 0;
            if (r12 == 0) goto L_0x06c9;
            r13 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x06c1 }
            r13 = r13.lastCacheOutTime;	 Catch:{ Throwable -> 0x06c1 }
            r26 = 0;	 Catch:{ Throwable -> 0x06c1 }
            r23 = (r13 > r26 ? 1 : (r13 == r26 ? 0 : -1));	 Catch:{ Throwable -> 0x06c1 }
            if (r23 == 0) goto L_0x06c9;	 Catch:{ Throwable -> 0x06c1 }
            r13 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x06c1 }
            r13 = r13.lastCacheOutTime;	 Catch:{ Throwable -> 0x06c1 }
            r28 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x06c1 }
            r30 = r5;
            r23 = r6;
            r5 = (long) r12;
            r28 = r28 - r5;
            r12 = (r13 > r28 ? 1 : (r13 == r28 ? 0 : -1));
            if (r12 <= 0) goto L_0x06cd;
            r12 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x06c3 }
            r13 = 21;	 Catch:{ Throwable -> 0x06c3 }
            if (r12 >= r13) goto L_0x06cd;	 Catch:{ Throwable -> 0x06c3 }
            java.lang.Thread.sleep(r5);	 Catch:{ Throwable -> 0x06c3 }
            goto L_0x06cd;
            r30 = r5;
            r2 = r30;
            r7 = 0;
            r12 = 0;
            goto L_0x094e;
            r30 = r5;
            r23 = r6;
            r5 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x0949 }
            r12 = java.lang.System.currentTimeMillis();	 Catch:{ Throwable -> 0x0949 }
            r5.lastCacheOutTime = r12;	 Catch:{ Throwable -> 0x0949 }
            r5 = r1.sync;	 Catch:{ Throwable -> 0x0949 }
            monitor-enter(r5);	 Catch:{ Throwable -> 0x0949 }
            r6 = r1.isCancelled;	 Catch:{ all -> 0x0942 }
            if (r6 == 0) goto L_0x06df;	 Catch:{ all -> 0x0942 }
            monitor-exit(r5);	 Catch:{ all -> 0x0942 }
            return;	 Catch:{ all -> 0x0942 }
            monitor-exit(r5);	 Catch:{ all -> 0x0942 }
            if (r19 != 0) goto L_0x06f8;
            r5 = r1.cacheImage;	 Catch:{ Throwable -> 0x06c3 }
            r5 = r5.filter;	 Catch:{ Throwable -> 0x06c3 }
            if (r5 == 0) goto L_0x06f8;	 Catch:{ Throwable -> 0x06c3 }
            if (r0 != 0) goto L_0x06f8;	 Catch:{ Throwable -> 0x06c3 }
            r5 = r1.cacheImage;	 Catch:{ Throwable -> 0x06c3 }
            r5 = r5.imageLocation;	 Catch:{ Throwable -> 0x06c3 }
            r5 = r5.path;	 Catch:{ Throwable -> 0x06c3 }
            if (r5 == 0) goto L_0x06f3;	 Catch:{ Throwable -> 0x06c3 }
            goto L_0x06f8;	 Catch:{ Throwable -> 0x06c3 }
            r5 = android.graphics.Bitmap.Config.RGB_565;	 Catch:{ Throwable -> 0x06c3 }
            r11.inPreferredConfig = r5;	 Catch:{ Throwable -> 0x06c3 }
            goto L_0x06fc;
            r5 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0949 }
            r11.inPreferredConfig = r5;	 Catch:{ Throwable -> 0x0949 }
            r5 = 0;	 Catch:{ Throwable -> 0x0949 }
            r11.inDither = r5;	 Catch:{ Throwable -> 0x0949 }
            if (r21 == 0) goto L_0x0725;
            if (r25 != 0) goto L_0x0725;
            if (r15 == 0) goto L_0x0715;
            r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x06c3 }
            r5 = r5.getContentResolver();	 Catch:{ Throwable -> 0x06c3 }
            r12 = r21.longValue();	 Catch:{ Throwable -> 0x06c3 }
            r6 = 1;	 Catch:{ Throwable -> 0x06c3 }
            r5 = android.provider.MediaStore.Video.Thumbnails.getThumbnail(r5, r12, r6, r11);	 Catch:{ Throwable -> 0x06c3 }
            goto L_0x0727;	 Catch:{ Throwable -> 0x06c3 }
            r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x06c3 }
            r5 = r5.getContentResolver();	 Catch:{ Throwable -> 0x06c3 }
            r12 = r21.longValue();	 Catch:{ Throwable -> 0x06c3 }
            r6 = 1;	 Catch:{ Throwable -> 0x06c3 }
            r5 = android.provider.MediaStore.Images.Thumbnails.getThumbnail(r5, r12, r6, r11);	 Catch:{ Throwable -> 0x06c3 }
            goto L_0x0727;
            r5 = r30;
            if (r5 != 0) goto L_0x083a;
            if (r7 == 0) goto L_0x0779;
            r6 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0776 }
            r7 = "r";	 Catch:{ Throwable -> 0x0776 }
            r6.<init>(r2, r7);	 Catch:{ Throwable -> 0x0776 }
            r28 = r6.getChannel();	 Catch:{ Throwable -> 0x0776 }
            r29 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ Throwable -> 0x0776 }
            r30 = 0;	 Catch:{ Throwable -> 0x0776 }
            r32 = r2.length();	 Catch:{ Throwable -> 0x0776 }
            r7 = r28.map(r29, r30, r32);	 Catch:{ Throwable -> 0x0776 }
            r8 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x0776 }
            r8.<init>();	 Catch:{ Throwable -> 0x0776 }
            r9 = 1;	 Catch:{ Throwable -> 0x0776 }
            r8.inJustDecodeBounds = r9;	 Catch:{ Throwable -> 0x0776 }
            r10 = r7.limit();	 Catch:{ Throwable -> 0x0776 }
            r12 = 0;
            org.telegram.messenger.Utilities.loadWebpImage(r12, r7, r10, r8, r9);	 Catch:{ Throwable -> 0x0837 }
            r9 = r8.outWidth;	 Catch:{ Throwable -> 0x0776 }
            r8 = r8.outHeight;	 Catch:{ Throwable -> 0x0776 }
            r10 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0776 }
            r5 = org.telegram.messenger.Bitmaps.createBitmap(r9, r8, r10);	 Catch:{ Throwable -> 0x0776 }
            r8 = r7.limit();	 Catch:{ Throwable -> 0x0776 }
            r9 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0776 }
            if (r9 != 0) goto L_0x0766;
            r9 = 1;
            goto L_0x0767;
            r9 = 0;
            r10 = 0;
            org.telegram.messenger.Utilities.loadWebpImage(r5, r7, r8, r10, r9);	 Catch:{ Throwable -> 0x0772 }
            r6.close();	 Catch:{ Throwable -> 0x0776 }
            r7 = 0;
            r12 = 0;
            goto L_0x083c;
            r2 = r5;
            r12 = r10;
            goto L_0x094d;
            r2 = r5;
            goto L_0x06c5;
            r6 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0836 }
            if (r6 != 0) goto L_0x07d7;
            if (r9 == 0) goto L_0x0780;
            goto L_0x07d7;
            if (r8 == 0) goto L_0x078c;
            r6 = new org.telegram.messenger.secretmedia.EncryptedFileInputStream;	 Catch:{ Throwable -> 0x0776 }
            r7 = r1.cacheImage;	 Catch:{ Throwable -> 0x0776 }
            r7 = r7.encryptionKeyPath;	 Catch:{ Throwable -> 0x0776 }
            r6.<init>(r2, r7);	 Catch:{ Throwable -> 0x0776 }
            goto L_0x0791;
            r6 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0836 }
            r6.<init>(r2);	 Catch:{ Throwable -> 0x0836 }
            r7 = r1.cacheImage;	 Catch:{ Throwable -> 0x0836 }
            r7 = r7.imageLocation;	 Catch:{ Throwable -> 0x0836 }
            r7 = r7.document;	 Catch:{ Throwable -> 0x0836 }
            r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_document;	 Catch:{ Throwable -> 0x0836 }
            if (r7 == 0) goto L_0x07cd;
            r7 = new androidx.exifinterface.media.ExifInterface;	 Catch:{ Throwable -> 0x07bb }
            r7.<init>(r6);	 Catch:{ Throwable -> 0x07bb }
            r8 = "Orientation";	 Catch:{ Throwable -> 0x07bb }
            r9 = 1;	 Catch:{ Throwable -> 0x07bb }
            r7 = r7.getAttributeInt(r8, r9);	 Catch:{ Throwable -> 0x07bb }
            r8 = 3;
            if (r7 == r8) goto L_0x07b8;
            r8 = 6;
            if (r7 == r8) goto L_0x07b5;
            r8 = 8;
            if (r7 == r8) goto L_0x07b2;
            goto L_0x07bb;
            r7 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
            goto L_0x07bc;
            r7 = 90;
            goto L_0x07bc;
            r7 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            goto L_0x07bc;
            r7 = 0;
            r8 = r6.getChannel();	 Catch:{ Throwable -> 0x07c6 }
            r9 = 0;	 Catch:{ Throwable -> 0x07c6 }
            r8.position(r9);	 Catch:{ Throwable -> 0x07c6 }
            goto L_0x07ce;
            r2 = r5;
            r24 = r7;
            r7 = 0;
            r12 = 0;
            goto L_0x0950;
            r7 = 0;
            r12 = 0;
            r5 = android.graphics.BitmapFactory.decodeStream(r6, r12, r11);	 Catch:{ Throwable -> 0x093d }
            r6.close();	 Catch:{ Throwable -> 0x093d }
            goto L_0x083c;
            r12 = 0;
            r6 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0837 }
            r7 = "r";	 Catch:{ Throwable -> 0x0837 }
            r6.<init>(r2, r7);	 Catch:{ Throwable -> 0x0837 }
            r13 = r6.length();	 Catch:{ Throwable -> 0x0837 }
            r7 = (int) r13;	 Catch:{ Throwable -> 0x0837 }
            r13 = org.telegram.messenger.ImageLoader.bytesLocal;	 Catch:{ Throwable -> 0x0837 }
            r13 = r13.get();	 Catch:{ Throwable -> 0x0837 }
            r13 = (byte[]) r13;	 Catch:{ Throwable -> 0x0837 }
            if (r13 == 0) goto L_0x07f4;	 Catch:{ Throwable -> 0x0837 }
            r14 = r13.length;	 Catch:{ Throwable -> 0x0837 }
            if (r14 < r7) goto L_0x07f4;	 Catch:{ Throwable -> 0x0837 }
            goto L_0x07f5;	 Catch:{ Throwable -> 0x0837 }
            r13 = r12;	 Catch:{ Throwable -> 0x0837 }
            if (r13 != 0) goto L_0x0800;	 Catch:{ Throwable -> 0x0837 }
            r13 = new byte[r7];	 Catch:{ Throwable -> 0x0837 }
            r14 = org.telegram.messenger.ImageLoader.bytesLocal;	 Catch:{ Throwable -> 0x0837 }
            r14.set(r13);	 Catch:{ Throwable -> 0x0837 }
            r14 = 0;	 Catch:{ Throwable -> 0x0837 }
            r6.readFully(r13, r14, r7);	 Catch:{ Throwable -> 0x0837 }
            r6.close();	 Catch:{ Throwable -> 0x0837 }
            if (r9 == 0) goto L_0x0823;	 Catch:{ Throwable -> 0x0837 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r13, r14, r7, r9);	 Catch:{ Throwable -> 0x0837 }
            r6 = org.telegram.messenger.Utilities.computeSHA256(r13, r14, r7);	 Catch:{ Throwable -> 0x0837 }
            if (r10 == 0) goto L_0x081b;	 Catch:{ Throwable -> 0x0837 }
            r6 = java.util.Arrays.equals(r6, r10);	 Catch:{ Throwable -> 0x0837 }
            if (r6 != 0) goto L_0x0819;	 Catch:{ Throwable -> 0x0837 }
            goto L_0x081b;	 Catch:{ Throwable -> 0x0837 }
            r6 = 0;	 Catch:{ Throwable -> 0x0837 }
            goto L_0x081c;	 Catch:{ Throwable -> 0x0837 }
            r6 = 1;	 Catch:{ Throwable -> 0x0837 }
            r8 = 0;	 Catch:{ Throwable -> 0x0837 }
            r9 = r13[r8];	 Catch:{ Throwable -> 0x0837 }
            r8 = r9 & 255;	 Catch:{ Throwable -> 0x0837 }
            r7 = r7 - r8;	 Catch:{ Throwable -> 0x0837 }
            goto L_0x082f;	 Catch:{ Throwable -> 0x0837 }
            if (r8 == 0) goto L_0x082d;	 Catch:{ Throwable -> 0x0837 }
            r6 = r1.cacheImage;	 Catch:{ Throwable -> 0x0837 }
            r6 = r6.encryptionKeyPath;	 Catch:{ Throwable -> 0x0837 }
            r8 = 0;	 Catch:{ Throwable -> 0x0837 }
            org.telegram.messenger.secretmedia.EncryptedFileInputStream.decryptBytesWithKeyFile(r13, r8, r7, r6);	 Catch:{ Throwable -> 0x0837 }
            r6 = 0;	 Catch:{ Throwable -> 0x0837 }
            r8 = 0;	 Catch:{ Throwable -> 0x0837 }
            if (r6 != 0) goto L_0x083b;	 Catch:{ Throwable -> 0x0837 }
            r5 = android.graphics.BitmapFactory.decodeByteArray(r13, r8, r7, r11);	 Catch:{ Throwable -> 0x0837 }
            goto L_0x083b;
            r12 = 0;
            r2 = r5;
            goto L_0x094d;
            r12 = 0;
            r7 = 0;
            if (r5 != 0) goto L_0x0857;
            if (r16 == 0) goto L_0x0853;
            r3 = r2.length();	 Catch:{ Throwable -> 0x093d }
            r8 = 0;	 Catch:{ Throwable -> 0x093d }
            r0 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));	 Catch:{ Throwable -> 0x093d }
            if (r0 == 0) goto L_0x0850;	 Catch:{ Throwable -> 0x093d }
            r0 = r1.cacheImage;	 Catch:{ Throwable -> 0x093d }
            r0 = r0.filter;	 Catch:{ Throwable -> 0x093d }
            if (r0 != 0) goto L_0x0853;	 Catch:{ Throwable -> 0x093d }
            r2.delete();	 Catch:{ Throwable -> 0x093d }
            r2 = r5;
            r0 = 0;
            goto L_0x0936;
            r2 = r1.cacheImage;	 Catch:{ Throwable -> 0x093c }
            r2 = r2.filter;	 Catch:{ Throwable -> 0x093c }
            if (r2 == 0) goto L_0x0924;	 Catch:{ Throwable -> 0x093c }
            r2 = r5.getWidth();	 Catch:{ Throwable -> 0x093c }
            r2 = (float) r2;	 Catch:{ Throwable -> 0x093c }
            r6 = r5.getHeight();	 Catch:{ Throwable -> 0x093c }
            r6 = (float) r6;	 Catch:{ Throwable -> 0x093c }
            r8 = r11.inPurgeable;	 Catch:{ Throwable -> 0x093c }
            if (r8 != 0) goto L_0x08a2;
            r8 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));
            if (r8 == 0) goto L_0x08a2;
            r8 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r8 == 0) goto L_0x08a2;
            r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
            r13 = r4 + r8;
            r8 = (r2 > r13 ? 1 : (r2 == r13 ? 0 : -1));
            if (r8 <= 0) goto L_0x08a2;
            r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
            if (r8 <= 0) goto L_0x088f;
            r8 = (r4 > r23 ? 1 : (r4 == r23 ? 0 : -1));
            if (r8 <= 0) goto L_0x088f;
            r8 = r2 / r4;
            r4 = (int) r4;
            r8 = r6 / r8;
            r8 = (int) r8;
            r9 = 1;
            r4 = org.telegram.messenger.Bitmaps.createScaledBitmap(r5, r4, r8, r9);	 Catch:{ Throwable -> 0x093d }
            goto L_0x089c;	 Catch:{ Throwable -> 0x093d }
            r9 = 1;	 Catch:{ Throwable -> 0x093d }
            r4 = r6 / r23;	 Catch:{ Throwable -> 0x093d }
            r4 = r2 / r4;	 Catch:{ Throwable -> 0x093d }
            r4 = (int) r4;	 Catch:{ Throwable -> 0x093d }
            r8 = r23;	 Catch:{ Throwable -> 0x093d }
            r8 = (int) r8;	 Catch:{ Throwable -> 0x093d }
            r4 = org.telegram.messenger.Bitmaps.createScaledBitmap(r5, r4, r8, r9);	 Catch:{ Throwable -> 0x093d }
            if (r5 == r4) goto L_0x08a3;	 Catch:{ Throwable -> 0x093d }
            r5.recycle();	 Catch:{ Throwable -> 0x093d }
            goto L_0x08a4;
            r9 = 1;
            r4 = r5;
            if (r4 == 0) goto L_0x0921;
            if (r3 == 0) goto L_0x08e8;
            r3 = r4.getWidth();	 Catch:{ Throwable -> 0x08e4 }
            r5 = r4.getHeight();	 Catch:{ Throwable -> 0x08e4 }
            r3 = r3 * r5;
            r5 = 22500; // 0x57e4 float:3.1529E-41 double:1.11165E-319;
            if (r3 <= r5) goto L_0x08c0;
            r3 = 100;
            r5 = 100;
            r8 = 0;
            r3 = org.telegram.messenger.Bitmaps.createScaledBitmap(r4, r3, r5, r8);	 Catch:{ Throwable -> 0x08e5 }
            goto L_0x08c2;	 Catch:{ Throwable -> 0x08e5 }
            r8 = 0;	 Catch:{ Throwable -> 0x08e5 }
            r3 = r4;	 Catch:{ Throwable -> 0x08e5 }
            r5 = r11.inPurgeable;	 Catch:{ Throwable -> 0x08e5 }
            if (r5 == 0) goto L_0x08c8;	 Catch:{ Throwable -> 0x08e5 }
            r5 = 0;	 Catch:{ Throwable -> 0x08e5 }
            goto L_0x08c9;	 Catch:{ Throwable -> 0x08e5 }
            r5 = 1;	 Catch:{ Throwable -> 0x08e5 }
            r10 = r3.getWidth();	 Catch:{ Throwable -> 0x08e5 }
            r13 = r3.getHeight();	 Catch:{ Throwable -> 0x08e5 }
            r14 = r3.getRowBytes();	 Catch:{ Throwable -> 0x08e5 }
            r5 = org.telegram.messenger.Utilities.needInvert(r3, r5, r10, r13, r14);	 Catch:{ Throwable -> 0x08e5 }
            if (r5 == 0) goto L_0x08dd;
            r5 = 1;
            goto L_0x08de;
            r5 = 0;
            if (r3 == r4) goto L_0x08ea;
            r3.recycle();	 Catch:{ Throwable -> 0x0919 }
            goto L_0x08ea;	 Catch:{ Throwable -> 0x0919 }
            r8 = 0;	 Catch:{ Throwable -> 0x0919 }
            r2 = r4;	 Catch:{ Throwable -> 0x0919 }
            goto L_0x093e;	 Catch:{ Throwable -> 0x0919 }
            r8 = 0;	 Catch:{ Throwable -> 0x0919 }
            r5 = 0;	 Catch:{ Throwable -> 0x0919 }
            if (r0 == 0) goto L_0x091e;	 Catch:{ Throwable -> 0x0919 }
            r0 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;	 Catch:{ Throwable -> 0x0919 }
            r3 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1));	 Catch:{ Throwable -> 0x0919 }
            if (r3 >= 0) goto L_0x091e;	 Catch:{ Throwable -> 0x0919 }
            r0 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));	 Catch:{ Throwable -> 0x0919 }
            if (r0 >= 0) goto L_0x091e;	 Catch:{ Throwable -> 0x0919 }
            r0 = r4.getConfig();	 Catch:{ Throwable -> 0x0919 }
            r2 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ Throwable -> 0x0919 }
            if (r0 != r2) goto L_0x0916;	 Catch:{ Throwable -> 0x0919 }
            r14 = 3;	 Catch:{ Throwable -> 0x0919 }
            r0 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0919 }
            if (r0 == 0) goto L_0x0905;	 Catch:{ Throwable -> 0x0919 }
            r15 = 0;	 Catch:{ Throwable -> 0x0919 }
            goto L_0x0906;	 Catch:{ Throwable -> 0x0919 }
            r15 = 1;	 Catch:{ Throwable -> 0x0919 }
            r16 = r4.getWidth();	 Catch:{ Throwable -> 0x0919 }
            r17 = r4.getHeight();	 Catch:{ Throwable -> 0x0919 }
            r18 = r4.getRowBytes();	 Catch:{ Throwable -> 0x0919 }
            r13 = r4;	 Catch:{ Throwable -> 0x0919 }
            org.telegram.messenger.Utilities.blurBitmap(r13, r14, r15, r16, r17, r18);	 Catch:{ Throwable -> 0x0919 }
            r0 = r5;
            r8 = 1;
            goto L_0x091f;
            r2 = r4;
            r24 = r7;
            r7 = r5;
            goto L_0x0950;
            r0 = r5;
            r5 = r4;
            goto L_0x0926;
            r8 = 0;
            r5 = r4;
            goto L_0x0925;
            r8 = 0;
            r0 = 0;
            if (r8 != 0) goto L_0x0935;
            r2 = r11.inPurgeable;	 Catch:{ Throwable -> 0x0930 }
            if (r2 == 0) goto L_0x0935;	 Catch:{ Throwable -> 0x0930 }
            org.telegram.messenger.Utilities.pinBitmap(r5);	 Catch:{ Throwable -> 0x0930 }
            goto L_0x0935;
            r2 = r5;
            r24 = r7;
            r7 = r0;
            goto L_0x0950;
            r2 = r5;
            r34 = r7;
            r7 = r0;
            r0 = r34;
            goto L_0x0952;
            r8 = 0;
            r2 = r5;
            r24 = r7;
            r7 = 0;
            goto L_0x0950;
            r0 = move-exception;
            r8 = 0;
            r12 = 0;
            monitor-exit(r5);	 Catch:{ all -> 0x0947 }
            throw r0;	 Catch:{ Throwable -> 0x094b }
            r0 = move-exception;
            goto L_0x0945;
            r8 = 0;
            r12 = 0;
            r2 = r30;
            r7 = 0;
            r24 = 0;
            r0 = r24;
            java.lang.Thread.interrupted();
            if (r7 != 0) goto L_0x0967;
            if (r0 == 0) goto L_0x095a;
            goto L_0x0967;
            if (r2 == 0) goto L_0x0962;
            r5 = new android.graphics.drawable.BitmapDrawable;
            r5.<init>(r2);
            goto L_0x0963;
            r5 = r12;
            r1.onPostExecute(r5);
            goto L_0x0973;
            if (r2 == 0) goto L_0x096f;
            r5 = new org.telegram.messenger.ExtendedBitmapDrawable;
            r5.<init>(r2, r7, r0);
            goto L_0x0970;
            r5 = r12;
            r1.onPostExecute(r5);
            return;
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0974 }
            throw r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader$CacheOutTask.run():void");
        }

        public CacheOutTask(CacheImage cacheImage) {
            this.cacheImage = cacheImage;
        }

        private void onPostExecute(Drawable drawable) {
            AndroidUtilities.runOnUIThread(new C0484-$$Lambda$ImageLoader$CacheOutTask$P-Q-SglLFg8CKw46QDkF5nN_7Ko(this, drawable));
        }

        public /* synthetic */ void lambda$onPostExecute$1$ImageLoader$CacheOutTask(Drawable drawable) {
            String str = null;
            Drawable drawable2;
            if (drawable instanceof LottieDrawable) {
                drawable = (LottieDrawable) drawable;
                drawable2 = (Drawable) ImageLoader.this.lottieMemCache.get(this.cacheImage.key);
                if (drawable2 == null) {
                    ImageLoader.this.lottieMemCache.put(this.cacheImage.key, drawable);
                } else {
                    drawable = drawable2;
                }
            } else if (!(drawable instanceof AnimatedFileDrawable)) {
                if (drawable instanceof BitmapDrawable) {
                    drawable = (BitmapDrawable) drawable;
                    drawable2 = (Drawable) ImageLoader.this.memCache.get(this.cacheImage.key);
                    if (drawable2 == null) {
                        ImageLoader.this.memCache.put(this.cacheImage.key, drawable);
                    } else {
                        drawable.getBitmap().recycle();
                        drawable = drawable2;
                    }
                    if (drawable != null) {
                        ImageLoader.this.incrementUseCount(this.cacheImage.key);
                        str = this.cacheImage.key;
                    }
                } else {
                    drawable = null;
                }
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new C0483-$$Lambda$ImageLoader$CacheOutTask$L2FS7HdPO2NRh4SX6OgH248fhO4(this, drawable, str));
        }

        public /* synthetic */ void lambda$null$0$ImageLoader$CacheOutTask(Drawable drawable, String str) {
            this.cacheImage.setImageAndClear(drawable, str);
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0012 */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        public void cancel() {
            /*
            r2 = this;
            r0 = r2.sync;
            monitor-enter(r0);
            r1 = 1;
            r2.isCancelled = r1;	 Catch:{ Exception -> 0x0012 }
            r1 = r2.runningThread;	 Catch:{ Exception -> 0x0012 }
            if (r1 == 0) goto L_0x0012;
        L_0x000a:
            r1 = r2.runningThread;	 Catch:{ Exception -> 0x0012 }
            r1.interrupt();	 Catch:{ Exception -> 0x0012 }
            goto L_0x0012;
        L_0x0010:
            r1 = move-exception;
            goto L_0x0014;
        L_0x0012:
            monitor-exit(r0);	 Catch:{ all -> 0x0010 }
            return;
        L_0x0014:
            monitor-exit(r0);	 Catch:{ all -> 0x0010 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader$CacheOutTask.cancel():void");
        }
    }

    private class HttpFileTask extends AsyncTask<Void, Void, Boolean> {
        private boolean canRetry = true;
        private int currentAccount;
        private String ext;
        private RandomAccessFile fileOutputStream = null;
        private int fileSize;
        private long lastProgressTime;
        private File tempFile;
        private String url;

        public HttpFileTask(String str, File file, String str2, int i) {
            this.url = str;
            this.tempFile = file;
            this.ext = str2;
            this.currentAccount = i;
        }

        private void reportProgress(float f) {
            long currentTimeMillis = System.currentTimeMillis();
            if (f != 1.0f) {
                long j = this.lastProgressTime;
                if (j != 0 && j >= currentTimeMillis - 500) {
                    return;
                }
            }
            this.lastProgressTime = currentTimeMillis;
            Utilities.stageQueue.postRunnable(new C0485-$$Lambda$ImageLoader$HttpFileTask$CbdoQhu0HscXntXREbdZu5bUbuA(this, f));
        }

        public /* synthetic */ void lambda$reportProgress$1$ImageLoader$HttpFileTask(float f) {
            ImageLoader.this.fileProgresses.put(this.url, Float.valueOf(f));
            AndroidUtilities.runOnUIThread(new C0486-$$Lambda$ImageLoader$HttpFileTask$jxNOungyWTOpPFlVvFvzXDtQEx0(this, f));
        }

        public /* synthetic */ void lambda$null$0$ImageLoader$HttpFileTask(float f) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, this.url, Float.valueOf(f));
        }

        /* Access modifiers changed, original: protected|varargs */
        /* JADX WARNING: Removed duplicated region for block: B:103:0x0148 A:{Catch:{ Throwable -> 0x0150 }} */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x0156 A:{SYNTHETIC, Splitter:B:107:0x0156} */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x00ad A:{SYNTHETIC, Splitter:B:43:0x00ad} */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x007f  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0076  */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x00ad A:{SYNTHETIC, Splitter:B:43:0x00ad} */
        /* JADX WARNING: Removed duplicated region for block: B:103:0x0148 A:{Catch:{ Throwable -> 0x0150 }} */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x0156 A:{SYNTHETIC, Splitter:B:107:0x0156} */
        /* JADX WARNING: Removed duplicated region for block: B:103:0x0148 A:{Catch:{ Throwable -> 0x0150 }} */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x0156 A:{SYNTHETIC, Splitter:B:107:0x0156} */
        /* JADX WARNING: Removed duplicated region for block: B:103:0x0148 A:{Catch:{ Throwable -> 0x0150 }} */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x0156 A:{SYNTHETIC, Splitter:B:107:0x0156} */
        /* JADX WARNING: Removed duplicated region for block: B:103:0x0148 A:{Catch:{ Throwable -> 0x0150 }} */
        /* JADX WARNING: Removed duplicated region for block: B:107:0x0156 A:{SYNTHETIC, Splitter:B:107:0x0156} */
        public java.lang.Boolean doInBackground(java.lang.Void... r10) {
            /*
            r9 = this;
            r10 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";
            r0 = "User-Agent";
            r1 = 1;
            r2 = 0;
            r3 = 0;
            r4 = new java.net.URL;	 Catch:{ Throwable -> 0x006f }
            r5 = r9.url;	 Catch:{ Throwable -> 0x006f }
            r4.<init>(r5);	 Catch:{ Throwable -> 0x006f }
            r4 = r4.openConnection();	 Catch:{ Throwable -> 0x006f }
            r4.addRequestProperty(r0, r10);	 Catch:{ Throwable -> 0x006c }
            r5 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r4.setConnectTimeout(r5);	 Catch:{ Throwable -> 0x006c }
            r4.setReadTimeout(r5);	 Catch:{ Throwable -> 0x006c }
            r5 = r4 instanceof java.net.HttpURLConnection;	 Catch:{ Throwable -> 0x006c }
            if (r5 == 0) goto L_0x0054;
        L_0x0021:
            r5 = r4;
            r5 = (java.net.HttpURLConnection) r5;	 Catch:{ Throwable -> 0x006c }
            r5.setInstanceFollowRedirects(r1);	 Catch:{ Throwable -> 0x006c }
            r6 = r5.getResponseCode();	 Catch:{ Throwable -> 0x006c }
            r7 = 302; // 0x12e float:4.23E-43 double:1.49E-321;
            if (r6 == r7) goto L_0x0037;
        L_0x002f:
            r7 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
            if (r6 == r7) goto L_0x0037;
        L_0x0033:
            r7 = 303; // 0x12f float:4.25E-43 double:1.497E-321;
            if (r6 != r7) goto L_0x0054;
        L_0x0037:
            r6 = "Location";
            r6 = r5.getHeaderField(r6);	 Catch:{ Throwable -> 0x006c }
            r7 = "Set-Cookie";
            r5 = r5.getHeaderField(r7);	 Catch:{ Throwable -> 0x006c }
            r7 = new java.net.URL;	 Catch:{ Throwable -> 0x006c }
            r7.<init>(r6);	 Catch:{ Throwable -> 0x006c }
            r4 = r7.openConnection();	 Catch:{ Throwable -> 0x006c }
            r6 = "Cookie";
            r4.setRequestProperty(r6, r5);	 Catch:{ Throwable -> 0x006c }
            r4.addRequestProperty(r0, r10);	 Catch:{ Throwable -> 0x006c }
        L_0x0054:
            r4.connect();	 Catch:{ Throwable -> 0x006c }
            r10 = r4.getInputStream();	 Catch:{ Throwable -> 0x006c }
            r0 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x0067 }
            r5 = r9.tempFile;	 Catch:{ Throwable -> 0x0067 }
            r6 = "rws";
            r0.<init>(r5, r6);	 Catch:{ Throwable -> 0x0067 }
            r9.fileOutputStream = r0;	 Catch:{ Throwable -> 0x0067 }
            goto L_0x00a9;
        L_0x0067:
            r0 = move-exception;
            r8 = r0;
            r0 = r10;
            r10 = r8;
            goto L_0x0072;
        L_0x006c:
            r10 = move-exception;
            r0 = r2;
            goto L_0x0072;
        L_0x006f:
            r10 = move-exception;
            r0 = r2;
            r4 = r0;
        L_0x0072:
            r5 = r10 instanceof java.net.SocketTimeoutException;
            if (r5 == 0) goto L_0x007f;
        L_0x0076:
            r5 = org.telegram.messenger.ApplicationLoader.isNetworkOnline();
            if (r5 == 0) goto L_0x00a5;
        L_0x007c:
            r9.canRetry = r3;
            goto L_0x00a5;
        L_0x007f:
            r5 = r10 instanceof java.net.UnknownHostException;
            if (r5 == 0) goto L_0x0086;
        L_0x0083:
            r9.canRetry = r3;
            goto L_0x00a5;
        L_0x0086:
            r5 = r10 instanceof java.net.SocketException;
            if (r5 == 0) goto L_0x009f;
        L_0x008a:
            r5 = r10.getMessage();
            if (r5 == 0) goto L_0x00a5;
        L_0x0090:
            r5 = r10.getMessage();
            r6 = "ECONNRESET";
            r5 = r5.contains(r6);
            if (r5 == 0) goto L_0x00a5;
        L_0x009c:
            r9.canRetry = r3;
            goto L_0x00a5;
        L_0x009f:
            r5 = r10 instanceof java.io.FileNotFoundException;
            if (r5 == 0) goto L_0x00a5;
        L_0x00a3:
            r9.canRetry = r3;
        L_0x00a5:
            org.telegram.messenger.FileLog.m30e(r10);
            r10 = r0;
        L_0x00a9:
            r0 = r9.canRetry;
            if (r0 == 0) goto L_0x015e;
        L_0x00ad:
            r0 = r4 instanceof java.net.HttpURLConnection;	 Catch:{ Exception -> 0x00c7 }
            if (r0 == 0) goto L_0x00cb;
        L_0x00b1:
            r0 = r4;
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00c7 }
            r0 = r0.getResponseCode();	 Catch:{ Exception -> 0x00c7 }
            r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r0 == r5) goto L_0x00cb;
        L_0x00bc:
            r5 = 202; // 0xca float:2.83E-43 double:1.0E-321;
            if (r0 == r5) goto L_0x00cb;
        L_0x00c0:
            r5 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
            if (r0 == r5) goto L_0x00cb;
        L_0x00c4:
            r9.canRetry = r3;	 Catch:{ Exception -> 0x00c7 }
            goto L_0x00cb;
        L_0x00c7:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x00cb:
            if (r4 == 0) goto L_0x00fa;
        L_0x00cd:
            r0 = r4.getHeaderFields();	 Catch:{ Exception -> 0x00f6 }
            if (r0 == 0) goto L_0x00fa;
        L_0x00d3:
            r4 = "content-Length";
            r0 = r0.get(r4);	 Catch:{ Exception -> 0x00f6 }
            r0 = (java.util.List) r0;	 Catch:{ Exception -> 0x00f6 }
            if (r0 == 0) goto L_0x00fa;
        L_0x00dd:
            r4 = r0.isEmpty();	 Catch:{ Exception -> 0x00f6 }
            if (r4 != 0) goto L_0x00fa;
        L_0x00e3:
            r0 = r0.get(r3);	 Catch:{ Exception -> 0x00f6 }
            r0 = (java.lang.String) r0;	 Catch:{ Exception -> 0x00f6 }
            if (r0 == 0) goto L_0x00fa;
        L_0x00eb:
            r0 = org.telegram.messenger.Utilities.parseInt(r0);	 Catch:{ Exception -> 0x00f6 }
            r0 = r0.intValue();	 Catch:{ Exception -> 0x00f6 }
            r9.fileSize = r0;	 Catch:{ Exception -> 0x00f6 }
            goto L_0x00fa;
        L_0x00f6:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x00fa:
            if (r10 == 0) goto L_0x0144;
        L_0x00fc:
            r0 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r0 = new byte[r0];	 Catch:{ Throwable -> 0x0140 }
            r4 = 0;
        L_0x0102:
            r5 = r9.isCancelled();	 Catch:{ Throwable -> 0x0140 }
            if (r5 == 0) goto L_0x0109;
        L_0x0108:
            goto L_0x0134;
        L_0x0109:
            r5 = r10.read(r0);	 Catch:{ Exception -> 0x0136 }
            if (r5 <= 0) goto L_0x0122;
        L_0x010f:
            r6 = r9.fileOutputStream;	 Catch:{ Exception -> 0x0136 }
            r6.write(r0, r3, r5);	 Catch:{ Exception -> 0x0136 }
            r4 = r4 + r5;
            r5 = r9.fileSize;	 Catch:{ Exception -> 0x0136 }
            if (r5 <= 0) goto L_0x0102;
        L_0x0119:
            r5 = (float) r4;	 Catch:{ Exception -> 0x0136 }
            r6 = r9.fileSize;	 Catch:{ Exception -> 0x0136 }
            r6 = (float) r6;	 Catch:{ Exception -> 0x0136 }
            r5 = r5 / r6;
            r9.reportProgress(r5);	 Catch:{ Exception -> 0x0136 }
            goto L_0x0102;
        L_0x0122:
            r0 = -1;
            if (r5 != r0) goto L_0x0134;
        L_0x0125:
            r0 = r9.fileSize;	 Catch:{ Exception -> 0x0132, Throwable -> 0x012f }
            if (r0 == 0) goto L_0x013b;
        L_0x0129:
            r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r9.reportProgress(r0);	 Catch:{ Exception -> 0x0132, Throwable -> 0x012f }
            goto L_0x013b;
        L_0x012f:
            r0 = move-exception;
            r3 = 1;
            goto L_0x0141;
        L_0x0132:
            r0 = move-exception;
            goto L_0x0138;
        L_0x0134:
            r1 = 0;
            goto L_0x013b;
        L_0x0136:
            r0 = move-exception;
            r1 = 0;
        L_0x0138:
            org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ Throwable -> 0x013d }
        L_0x013b:
            r3 = r1;
            goto L_0x0144;
        L_0x013d:
            r0 = move-exception;
            r3 = r1;
            goto L_0x0141;
        L_0x0140:
            r0 = move-exception;
        L_0x0141:
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x0144:
            r0 = r9.fileOutputStream;	 Catch:{ Throwable -> 0x0150 }
            if (r0 == 0) goto L_0x0154;
        L_0x0148:
            r0 = r9.fileOutputStream;	 Catch:{ Throwable -> 0x0150 }
            r0.close();	 Catch:{ Throwable -> 0x0150 }
            r9.fileOutputStream = r2;	 Catch:{ Throwable -> 0x0150 }
            goto L_0x0154;
        L_0x0150:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x0154:
            if (r10 == 0) goto L_0x015e;
        L_0x0156:
            r10.close();	 Catch:{ Throwable -> 0x015a }
            goto L_0x015e;
        L_0x015a:
            r10 = move-exception;
            org.telegram.messenger.FileLog.m30e(r10);
        L_0x015e:
            r10 = java.lang.Boolean.valueOf(r3);
            return r10;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader$HttpFileTask.doInBackground(java.lang.Void[]):java.lang.Boolean");
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Boolean bool) {
            ImageLoader.this.runHttpFileLoadTasks(this, bool.booleanValue() ? 2 : 1);
        }

        /* Access modifiers changed, original: protected */
        public void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
    }

    private class HttpImageTask extends AsyncTask<Void, Void, Boolean> {
        private CacheImage cacheImage;
        private boolean canRetry = true;
        private RandomAccessFile fileOutputStream;
        private HttpURLConnection httpConnection;
        private int imageSize;
        private long lastProgressTime;
        private String overrideUrl;

        static /* synthetic */ void lambda$doInBackground$2(TLObject tLObject, TL_error tL_error) {
        }

        public HttpImageTask(CacheImage cacheImage, int i) {
            this.cacheImage = cacheImage;
            this.imageSize = i;
        }

        public HttpImageTask(CacheImage cacheImage, int i, String str) {
            this.cacheImage = cacheImage;
            this.imageSize = i;
            this.overrideUrl = str;
        }

        private void reportProgress(float f) {
            long currentTimeMillis = System.currentTimeMillis();
            if (f != 1.0f) {
                long j = this.lastProgressTime;
                if (j != 0 && j >= currentTimeMillis - 500) {
                    return;
                }
            }
            this.lastProgressTime = currentTimeMillis;
            Utilities.stageQueue.postRunnable(new C0489-$$Lambda$ImageLoader$HttpImageTask$WWTxHtUw7-WIiuq5bKLqtQ8BNBI(this, f));
        }

        public /* synthetic */ void lambda$reportProgress$1$ImageLoader$HttpImageTask(float f) {
            ImageLoader.this.fileProgresses.put(this.cacheImage.url, Float.valueOf(f));
            AndroidUtilities.runOnUIThread(new C0487-$$Lambda$ImageLoader$HttpImageTask$5HGicoisjrTisboRYDJXYCptNjk(this, f));
        }

        public /* synthetic */ void lambda$null$0$ImageLoader$HttpImageTask(float f) {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.FileLoadProgressChanged, this.cacheImage.url, Float.valueOf(f));
        }

        /* Access modifiers changed, original: protected|varargs */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00e9 A:{SYNTHETIC, Splitter:B:48:0x00e9} */
        /* JADX WARNING: Removed duplicated region for block: B:108:0x0185 A:{Catch:{ Throwable -> 0x018d }} */
        /* JADX WARNING: Removed duplicated region for block: B:114:0x0195 A:{Catch:{ Throwable -> 0x019a }} */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x019c A:{SYNTHETIC, Splitter:B:117:0x019c} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a6  */
        /* JADX WARNING: Removed duplicated region for block: B:108:0x0185 A:{Catch:{ Throwable -> 0x018d }} */
        /* JADX WARNING: Removed duplicated region for block: B:114:0x0195 A:{Catch:{ Throwable -> 0x019a }} */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x019c A:{SYNTHETIC, Splitter:B:117:0x019c} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a6  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00e9 A:{SYNTHETIC, Splitter:B:48:0x00e9} */
        /* JADX WARNING: Removed duplicated region for block: B:108:0x0185 A:{Catch:{ Throwable -> 0x018d }} */
        /* JADX WARNING: Removed duplicated region for block: B:114:0x0195 A:{Catch:{ Throwable -> 0x019a }} */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x019c A:{SYNTHETIC, Splitter:B:117:0x019c} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a6  */
        /* JADX WARNING: Removed duplicated region for block: B:108:0x0185 A:{Catch:{ Throwable -> 0x018d }} */
        /* JADX WARNING: Removed duplicated region for block: B:114:0x0195 A:{Catch:{ Throwable -> 0x019a }} */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x019c A:{SYNTHETIC, Splitter:B:117:0x019c} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a6  */
        /* JADX WARNING: Removed duplicated region for block: B:108:0x0185 A:{Catch:{ Throwable -> 0x018d }} */
        /* JADX WARNING: Removed duplicated region for block: B:114:0x0195 A:{Catch:{ Throwable -> 0x019a }} */
        /* JADX WARNING: Removed duplicated region for block: B:117:0x019c A:{SYNTHETIC, Splitter:B:117:0x019c} */
        /* JADX WARNING: Removed duplicated region for block: B:122:0x01a6  */
        public java.lang.Boolean doInBackground(java.lang.Void... r9) {
            /*
            r8 = this;
            r9 = r8.isCancelled();
            r0 = 1;
            r1 = 0;
            r2 = 0;
            if (r9 != 0) goto L_0x00e2;
        L_0x0009:
            r9 = r8.cacheImage;	 Catch:{ Throwable -> 0x00a8 }
            r9 = r9.imageLocation;	 Catch:{ Throwable -> 0x00a8 }
            r9 = r9.path;	 Catch:{ Throwable -> 0x00a8 }
            r3 = "https://static-maps";
            r3 = r9.startsWith(r3);	 Catch:{ Throwable -> 0x00a8 }
            if (r3 != 0) goto L_0x001f;
        L_0x0017:
            r3 = "https://maps.googleapis";
            r3 = r9.startsWith(r3);	 Catch:{ Throwable -> 0x00a8 }
            if (r3 == 0) goto L_0x0057;
        L_0x001f:
            r3 = r8.cacheImage;	 Catch:{ Throwable -> 0x00a8 }
            r3 = r3.currentAccount;	 Catch:{ Throwable -> 0x00a8 }
            r3 = org.telegram.messenger.MessagesController.getInstance(r3);	 Catch:{ Throwable -> 0x00a8 }
            r3 = r3.mapProvider;	 Catch:{ Throwable -> 0x00a8 }
            r4 = 3;
            if (r3 == r4) goto L_0x002f;
        L_0x002c:
            r4 = 4;
            if (r3 != r4) goto L_0x0057;
        L_0x002f:
            r3 = org.telegram.messenger.ImageLoader.this;	 Catch:{ Throwable -> 0x00a8 }
            r3 = r3.testWebFile;	 Catch:{ Throwable -> 0x00a8 }
            r3 = r3.get(r9);	 Catch:{ Throwable -> 0x00a8 }
            r3 = (org.telegram.messenger.WebFile) r3;	 Catch:{ Throwable -> 0x00a8 }
            if (r3 == 0) goto L_0x0057;
        L_0x003d:
            r4 = new org.telegram.tgnet.TLRPC$TL_upload_getWebFile;	 Catch:{ Throwable -> 0x00a8 }
            r4.<init>();	 Catch:{ Throwable -> 0x00a8 }
            r3 = r3.location;	 Catch:{ Throwable -> 0x00a8 }
            r4.location = r3;	 Catch:{ Throwable -> 0x00a8 }
            r4.offset = r2;	 Catch:{ Throwable -> 0x00a8 }
            r4.limit = r2;	 Catch:{ Throwable -> 0x00a8 }
            r3 = r8.cacheImage;	 Catch:{ Throwable -> 0x00a8 }
            r3 = r3.currentAccount;	 Catch:{ Throwable -> 0x00a8 }
            r3 = org.telegram.tgnet.ConnectionsManager.getInstance(r3);	 Catch:{ Throwable -> 0x00a8 }
            r5 = org.telegram.messenger.C3422-$$Lambda$ImageLoader$HttpImageTask$T115Ddi3sI3XyS3851ENmLig_I8.INSTANCE;	 Catch:{ Throwable -> 0x00a8 }
            r3.sendRequest(r4, r5);	 Catch:{ Throwable -> 0x00a8 }
        L_0x0057:
            r3 = new java.net.URL;	 Catch:{ Throwable -> 0x00a8 }
            r4 = r8.overrideUrl;	 Catch:{ Throwable -> 0x00a8 }
            if (r4 == 0) goto L_0x005f;
        L_0x005d:
            r9 = r8.overrideUrl;	 Catch:{ Throwable -> 0x00a8 }
        L_0x005f:
            r3.<init>(r9);	 Catch:{ Throwable -> 0x00a8 }
            r9 = r3.openConnection();	 Catch:{ Throwable -> 0x00a8 }
            r9 = (java.net.HttpURLConnection) r9;	 Catch:{ Throwable -> 0x00a8 }
            r8.httpConnection = r9;	 Catch:{ Throwable -> 0x00a8 }
            r9 = r8.httpConnection;	 Catch:{ Throwable -> 0x00a8 }
            r3 = "User-Agent";
            r4 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1";
            r9.addRequestProperty(r3, r4);	 Catch:{ Throwable -> 0x00a8 }
            r9 = r8.httpConnection;	 Catch:{ Throwable -> 0x00a8 }
            r3 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r9.setConnectTimeout(r3);	 Catch:{ Throwable -> 0x00a8 }
            r9 = r8.httpConnection;	 Catch:{ Throwable -> 0x00a8 }
            r9.setReadTimeout(r3);	 Catch:{ Throwable -> 0x00a8 }
            r9 = r8.httpConnection;	 Catch:{ Throwable -> 0x00a8 }
            r9.setInstanceFollowRedirects(r0);	 Catch:{ Throwable -> 0x00a8 }
            r9 = r8.isCancelled();	 Catch:{ Throwable -> 0x00a8 }
            if (r9 != 0) goto L_0x00e2;
        L_0x008a:
            r9 = r8.httpConnection;	 Catch:{ Throwable -> 0x00a8 }
            r9.connect();	 Catch:{ Throwable -> 0x00a8 }
            r9 = r8.httpConnection;	 Catch:{ Throwable -> 0x00a8 }
            r9 = r9.getInputStream();	 Catch:{ Throwable -> 0x00a8 }
            r3 = new java.io.RandomAccessFile;	 Catch:{ Throwable -> 0x00a3 }
            r4 = r8.cacheImage;	 Catch:{ Throwable -> 0x00a3 }
            r4 = r4.tempFilePath;	 Catch:{ Throwable -> 0x00a3 }
            r5 = "rws";
            r3.<init>(r4, r5);	 Catch:{ Throwable -> 0x00a3 }
            r8.fileOutputStream = r3;	 Catch:{ Throwable -> 0x00a3 }
            goto L_0x00e3;
        L_0x00a3:
            r3 = move-exception;
            r7 = r3;
            r3 = r9;
            r9 = r7;
            goto L_0x00aa;
        L_0x00a8:
            r9 = move-exception;
            r3 = r1;
        L_0x00aa:
            r4 = r9 instanceof java.net.SocketTimeoutException;
            if (r4 == 0) goto L_0x00b7;
        L_0x00ae:
            r4 = org.telegram.messenger.ApplicationLoader.isNetworkOnline();
            if (r4 == 0) goto L_0x00dd;
        L_0x00b4:
            r8.canRetry = r2;
            goto L_0x00dd;
        L_0x00b7:
            r4 = r9 instanceof java.net.UnknownHostException;
            if (r4 == 0) goto L_0x00be;
        L_0x00bb:
            r8.canRetry = r2;
            goto L_0x00dd;
        L_0x00be:
            r4 = r9 instanceof java.net.SocketException;
            if (r4 == 0) goto L_0x00d7;
        L_0x00c2:
            r4 = r9.getMessage();
            if (r4 == 0) goto L_0x00dd;
        L_0x00c8:
            r4 = r9.getMessage();
            r5 = "ECONNRESET";
            r4 = r4.contains(r5);
            if (r4 == 0) goto L_0x00dd;
        L_0x00d4:
            r8.canRetry = r2;
            goto L_0x00dd;
        L_0x00d7:
            r4 = r9 instanceof java.io.FileNotFoundException;
            if (r4 == 0) goto L_0x00dd;
        L_0x00db:
            r8.canRetry = r2;
        L_0x00dd:
            org.telegram.messenger.FileLog.m30e(r9);
            r9 = r3;
            goto L_0x00e3;
        L_0x00e2:
            r9 = r1;
        L_0x00e3:
            r3 = r8.isCancelled();
            if (r3 != 0) goto L_0x0181;
        L_0x00e9:
            r3 = r8.httpConnection;	 Catch:{ Exception -> 0x0102 }
            if (r3 == 0) goto L_0x0106;
        L_0x00ed:
            r3 = r8.httpConnection;	 Catch:{ Exception -> 0x0102 }
            r3 = r3.getResponseCode();	 Catch:{ Exception -> 0x0102 }
            r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r3 == r4) goto L_0x0106;
        L_0x00f7:
            r4 = 202; // 0xca float:2.83E-43 double:1.0E-321;
            if (r3 == r4) goto L_0x0106;
        L_0x00fb:
            r4 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
            if (r3 == r4) goto L_0x0106;
        L_0x00ff:
            r8.canRetry = r2;	 Catch:{ Exception -> 0x0102 }
            goto L_0x0106;
        L_0x0102:
            r3 = move-exception;
            org.telegram.messenger.FileLog.m30e(r3);
        L_0x0106:
            r3 = r8.imageSize;
            if (r3 != 0) goto L_0x013b;
        L_0x010a:
            r3 = r8.httpConnection;
            if (r3 == 0) goto L_0x013b;
        L_0x010e:
            r3 = r3.getHeaderFields();	 Catch:{ Exception -> 0x0137 }
            if (r3 == 0) goto L_0x013b;
        L_0x0114:
            r4 = "content-Length";
            r3 = r3.get(r4);	 Catch:{ Exception -> 0x0137 }
            r3 = (java.util.List) r3;	 Catch:{ Exception -> 0x0137 }
            if (r3 == 0) goto L_0x013b;
        L_0x011e:
            r4 = r3.isEmpty();	 Catch:{ Exception -> 0x0137 }
            if (r4 != 0) goto L_0x013b;
        L_0x0124:
            r3 = r3.get(r2);	 Catch:{ Exception -> 0x0137 }
            r3 = (java.lang.String) r3;	 Catch:{ Exception -> 0x0137 }
            if (r3 == 0) goto L_0x013b;
        L_0x012c:
            r3 = org.telegram.messenger.Utilities.parseInt(r3);	 Catch:{ Exception -> 0x0137 }
            r3 = r3.intValue();	 Catch:{ Exception -> 0x0137 }
            r8.imageSize = r3;	 Catch:{ Exception -> 0x0137 }
            goto L_0x013b;
        L_0x0137:
            r3 = move-exception;
            org.telegram.messenger.FileLog.m30e(r3);
        L_0x013b:
            if (r9 == 0) goto L_0x0181;
        L_0x013d:
            r3 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r3 = new byte[r3];	 Catch:{ Throwable -> 0x017d }
            r4 = 0;
        L_0x0142:
            r5 = r8.isCancelled();	 Catch:{ Throwable -> 0x017d }
            if (r5 == 0) goto L_0x0149;
        L_0x0148:
            goto L_0x0181;
        L_0x0149:
            r5 = r9.read(r3);	 Catch:{ Exception -> 0x0178 }
            if (r5 <= 0) goto L_0x0162;
        L_0x014f:
            r4 = r4 + r5;
            r6 = r8.fileOutputStream;	 Catch:{ Exception -> 0x0178 }
            r6.write(r3, r2, r5);	 Catch:{ Exception -> 0x0178 }
            r5 = r8.imageSize;	 Catch:{ Exception -> 0x0178 }
            if (r5 == 0) goto L_0x0142;
        L_0x0159:
            r5 = (float) r4;	 Catch:{ Exception -> 0x0178 }
            r6 = r8.imageSize;	 Catch:{ Exception -> 0x0178 }
            r6 = (float) r6;	 Catch:{ Exception -> 0x0178 }
            r5 = r5 / r6;
            r8.reportProgress(r5);	 Catch:{ Exception -> 0x0178 }
            goto L_0x0142;
        L_0x0162:
            r3 = -1;
            if (r5 != r3) goto L_0x0181;
        L_0x0165:
            r2 = r8.imageSize;	 Catch:{ Exception -> 0x0174, Throwable -> 0x0170 }
            if (r2 == 0) goto L_0x016e;
        L_0x0169:
            r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r8.reportProgress(r2);	 Catch:{ Exception -> 0x0174, Throwable -> 0x0170 }
        L_0x016e:
            r2 = 1;
            goto L_0x0181;
        L_0x0170:
            r2 = move-exception;
            r0 = r2;
            r2 = 1;
            goto L_0x017e;
        L_0x0174:
            r2 = move-exception;
            r0 = r2;
            r2 = 1;
            goto L_0x0179;
        L_0x0178:
            r0 = move-exception;
        L_0x0179:
            org.telegram.messenger.FileLog.m30e(r0);	 Catch:{ Throwable -> 0x017d }
            goto L_0x0181;
        L_0x017d:
            r0 = move-exception;
        L_0x017e:
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x0181:
            r0 = r8.fileOutputStream;	 Catch:{ Throwable -> 0x018d }
            if (r0 == 0) goto L_0x0191;
        L_0x0185:
            r0 = r8.fileOutputStream;	 Catch:{ Throwable -> 0x018d }
            r0.close();	 Catch:{ Throwable -> 0x018d }
            r8.fileOutputStream = r1;	 Catch:{ Throwable -> 0x018d }
            goto L_0x0191;
        L_0x018d:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x0191:
            r0 = r8.httpConnection;	 Catch:{ Throwable -> 0x019a }
            if (r0 == 0) goto L_0x019a;
        L_0x0195:
            r0 = r8.httpConnection;	 Catch:{ Throwable -> 0x019a }
            r0.disconnect();	 Catch:{ Throwable -> 0x019a }
        L_0x019a:
            if (r9 == 0) goto L_0x01a4;
        L_0x019c:
            r9.close();	 Catch:{ Throwable -> 0x01a0 }
            goto L_0x01a4;
        L_0x01a0:
            r9 = move-exception;
            org.telegram.messenger.FileLog.m30e(r9);
        L_0x01a4:
            if (r2 == 0) goto L_0x01ba;
        L_0x01a6:
            r9 = r8.cacheImage;
            r0 = r9.tempFilePath;
            if (r0 == 0) goto L_0x01ba;
        L_0x01ac:
            r9 = r9.finalFilePath;
            r9 = r0.renameTo(r9);
            if (r9 != 0) goto L_0x01ba;
        L_0x01b4:
            r9 = r8.cacheImage;
            r0 = r9.tempFilePath;
            r9.finalFilePath = r0;
        L_0x01ba:
            r9 = java.lang.Boolean.valueOf(r2);
            return r9;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader$HttpImageTask.doInBackground(java.lang.Void[]):java.lang.Boolean");
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Boolean bool) {
            if (bool.booleanValue() || !this.canRetry) {
                ImageLoader imageLoader = ImageLoader.this;
                CacheImage cacheImage = this.cacheImage;
                imageLoader.fileDidLoaded(cacheImage.url, cacheImage.finalFilePath, 0);
            } else {
                ImageLoader.this.httpFileLoadError(this.cacheImage.url);
            }
            Utilities.stageQueue.postRunnable(new C0488-$$Lambda$ImageLoader$HttpImageTask$SfPPeQgJq15qYgHfn-IXbR-DnQ0(this, bool));
            ImageLoader.this.imageLoadQueue.postRunnable(new C0490-$$Lambda$ImageLoader$HttpImageTask$Z2AJLo51Bz13ruMq5jl4ihFrKyc(this));
        }

        public /* synthetic */ void lambda$onPostExecute$4$ImageLoader$HttpImageTask(Boolean bool) {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new C0494-$$Lambda$ImageLoader$HttpImageTask$pG_0hY9R-vFFkVPjhMdZBfbtL24(this, bool));
        }

        public /* synthetic */ void lambda$null$3$ImageLoader$HttpImageTask(Boolean bool) {
            if (bool.booleanValue()) {
                NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileDidLoad, this.cacheImage.url);
                return;
            }
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileDidFailedLoad, this.cacheImage.url, Integer.valueOf(2));
        }

        public /* synthetic */ void lambda$onPostExecute$5$ImageLoader$HttpImageTask() {
            ImageLoader.this.runHttpTasks(true);
        }

        public /* synthetic */ void lambda$onCancelled$6$ImageLoader$HttpImageTask() {
            ImageLoader.this.runHttpTasks(true);
        }

        /* Access modifiers changed, original: protected */
        public void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new C0493-$$Lambda$ImageLoader$HttpImageTask$hp6Qb_emVVm8eUAVBNZgyYyMaoI(this));
            Utilities.stageQueue.postRunnable(new C0491-$$Lambda$ImageLoader$HttpImageTask$a-vnJl4U3DAZgDJvr8vdGGH5i2s(this));
        }

        public /* synthetic */ void lambda$onCancelled$8$ImageLoader$HttpImageTask() {
            ImageLoader.this.fileProgresses.remove(this.cacheImage.url);
            AndroidUtilities.runOnUIThread(new C0492-$$Lambda$ImageLoader$HttpImageTask$gXa55exgYzWejvB_mF-uS-Y2fOo(this));
        }

        public /* synthetic */ void lambda$null$7$ImageLoader$HttpImageTask() {
            NotificationCenter.getInstance(this.cacheImage.currentAccount).postNotificationName(NotificationCenter.fileDidFailedLoad, this.cacheImage.url, Integer.valueOf(1));
        }
    }

    private class ThumbGenerateInfo {
        private boolean big;
        private String filter;
        private ArrayList<ImageReceiver> imageReceiverArray;
        private Document parentDocument;

        private ThumbGenerateInfo() {
            this.imageReceiverArray = new ArrayList();
        }

        /* synthetic */ ThumbGenerateInfo(ImageLoader imageLoader, C10291 c10291) {
            this();
        }
    }

    private class ThumbGenerateTask implements Runnable {
        private ThumbGenerateInfo info;
        private int mediaType;
        private File originalPath;

        public ThumbGenerateTask(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
            this.mediaType = i;
            this.originalPath = file;
            this.info = thumbGenerateInfo;
        }

        private void removeTask() {
            ThumbGenerateInfo thumbGenerateInfo = this.info;
            if (thumbGenerateInfo != null) {
                ImageLoader.this.imageLoadQueue.postRunnable(new C0499xfd9184e6(this, FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument)));
            }
        }

        public /* synthetic */ void lambda$removeTask$0$ImageLoader$ThumbGenerateTask(String str) {
            ThumbGenerateTask thumbGenerateTask = (ThumbGenerateTask) ImageLoader.this.thumbGenerateTasks.remove(str);
        }

        /* JADX WARNING: Removed duplicated region for block: B:59:0x0145 A:{Catch:{ Exception -> 0x014e, Throwable -> 0x0173 }} */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x0142 A:{Catch:{ Exception -> 0x014e, Throwable -> 0x0173 }} */
        public void run() {
            /*
            r10 = this;
            r0 = ".jpg";
            r1 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            if (r1 != 0) goto L_0x000a;
        L_0x0006:
            r10.removeTask();	 Catch:{ Throwable -> 0x0173 }
            return;
        L_0x000a:
            r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0173 }
            r1.<init>();	 Catch:{ Throwable -> 0x0173 }
            r2 = "q_";
            r1.append(r2);	 Catch:{ Throwable -> 0x0173 }
            r2 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r2 = r2.parentDocument;	 Catch:{ Throwable -> 0x0173 }
            r2 = r2.dc_id;	 Catch:{ Throwable -> 0x0173 }
            r1.append(r2);	 Catch:{ Throwable -> 0x0173 }
            r2 = "_";
            r1.append(r2);	 Catch:{ Throwable -> 0x0173 }
            r2 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r2 = r2.parentDocument;	 Catch:{ Throwable -> 0x0173 }
            r2 = r2.f441id;	 Catch:{ Throwable -> 0x0173 }
            r1.append(r2);	 Catch:{ Throwable -> 0x0173 }
            r1 = r1.toString();	 Catch:{ Throwable -> 0x0173 }
            r2 = new java.io.File;	 Catch:{ Throwable -> 0x0173 }
            r3 = 4;
            r4 = org.telegram.messenger.FileLoader.getDirectory(r3);	 Catch:{ Throwable -> 0x0173 }
            r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0173 }
            r5.<init>();	 Catch:{ Throwable -> 0x0173 }
            r5.append(r1);	 Catch:{ Throwable -> 0x0173 }
            r5.append(r0);	 Catch:{ Throwable -> 0x0173 }
            r5 = r5.toString();	 Catch:{ Throwable -> 0x0173 }
            r2.<init>(r4, r5);	 Catch:{ Throwable -> 0x0173 }
            r4 = r2.exists();	 Catch:{ Throwable -> 0x0173 }
            if (r4 != 0) goto L_0x016f;
        L_0x0052:
            r4 = r10.originalPath;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.exists();	 Catch:{ Throwable -> 0x0173 }
            if (r4 != 0) goto L_0x005c;
        L_0x005a:
            goto L_0x016f;
        L_0x005c:
            r4 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.big;	 Catch:{ Throwable -> 0x0173 }
            if (r4 == 0) goto L_0x0071;
        L_0x0064:
            r3 = org.telegram.messenger.AndroidUtilities.displaySize;	 Catch:{ Throwable -> 0x0173 }
            r3 = r3.x;	 Catch:{ Throwable -> 0x0173 }
            r4 = org.telegram.messenger.AndroidUtilities.displaySize;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.y;	 Catch:{ Throwable -> 0x0173 }
            r3 = java.lang.Math.max(r3, r4);	 Catch:{ Throwable -> 0x0173 }
            goto L_0x0084;
        L_0x0071:
            r4 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
            r5 = org.telegram.messenger.AndroidUtilities.displaySize;	 Catch:{ Throwable -> 0x0173 }
            r5 = r5.x;	 Catch:{ Throwable -> 0x0173 }
            r6 = org.telegram.messenger.AndroidUtilities.displaySize;	 Catch:{ Throwable -> 0x0173 }
            r6 = r6.y;	 Catch:{ Throwable -> 0x0173 }
            r5 = java.lang.Math.min(r5, r6);	 Catch:{ Throwable -> 0x0173 }
            r5 = r5 / r3;
            r3 = java.lang.Math.min(r4, r5);	 Catch:{ Throwable -> 0x0173 }
        L_0x0084:
            r4 = r10.mediaType;	 Catch:{ Throwable -> 0x0173 }
            r5 = 0;
            r6 = 1;
            r7 = 0;
            if (r4 != 0) goto L_0x0097;
        L_0x008b:
            r0 = r10.originalPath;	 Catch:{ Throwable -> 0x0173 }
            r0 = r0.toString();	 Catch:{ Throwable -> 0x0173 }
            r4 = (float) r3;	 Catch:{ Throwable -> 0x0173 }
            r7 = org.telegram.messenger.ImageLoader.loadBitmap(r0, r7, r4, r4, r5);	 Catch:{ Throwable -> 0x0173 }
            goto L_0x0100;
        L_0x0097:
            r4 = r10.mediaType;	 Catch:{ Throwable -> 0x0173 }
            r8 = 2;
            if (r4 != r8) goto L_0x00b1;
        L_0x009c:
            r0 = r10.originalPath;	 Catch:{ Throwable -> 0x0173 }
            r0 = r0.toString();	 Catch:{ Throwable -> 0x0173 }
            r4 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.big;	 Catch:{ Throwable -> 0x0173 }
            if (r4 == 0) goto L_0x00ab;
        L_0x00aa:
            goto L_0x00ac;
        L_0x00ab:
            r8 = 1;
        L_0x00ac:
            r7 = android.media.ThumbnailUtils.createVideoThumbnail(r0, r8);	 Catch:{ Throwable -> 0x0173 }
            goto L_0x0100;
        L_0x00b1:
            r4 = r10.mediaType;	 Catch:{ Throwable -> 0x0173 }
            r9 = 3;
            if (r4 != r9) goto L_0x0100;
        L_0x00b6:
            r4 = r10.originalPath;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.toString();	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.toLowerCase();	 Catch:{ Throwable -> 0x0173 }
            r9 = "mp4";
            r9 = r4.endsWith(r9);	 Catch:{ Throwable -> 0x0173 }
            if (r9 == 0) goto L_0x00dd;
        L_0x00c8:
            r0 = r10.originalPath;	 Catch:{ Throwable -> 0x0173 }
            r0 = r0.toString();	 Catch:{ Throwable -> 0x0173 }
            r4 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.big;	 Catch:{ Throwable -> 0x0173 }
            if (r4 == 0) goto L_0x00d7;
        L_0x00d6:
            goto L_0x00d8;
        L_0x00d7:
            r8 = 1;
        L_0x00d8:
            r7 = android.media.ThumbnailUtils.createVideoThumbnail(r0, r8);	 Catch:{ Throwable -> 0x0173 }
            goto L_0x0100;
        L_0x00dd:
            r0 = r4.endsWith(r0);	 Catch:{ Throwable -> 0x0173 }
            if (r0 != 0) goto L_0x00fb;
        L_0x00e3:
            r0 = ".jpeg";
            r0 = r4.endsWith(r0);	 Catch:{ Throwable -> 0x0173 }
            if (r0 != 0) goto L_0x00fb;
        L_0x00eb:
            r0 = ".png";
            r0 = r4.endsWith(r0);	 Catch:{ Throwable -> 0x0173 }
            if (r0 != 0) goto L_0x00fb;
        L_0x00f3:
            r0 = ".gif";
            r0 = r4.endsWith(r0);	 Catch:{ Throwable -> 0x0173 }
            if (r0 == 0) goto L_0x0100;
        L_0x00fb:
            r0 = (float) r3;	 Catch:{ Throwable -> 0x0173 }
            r7 = org.telegram.messenger.ImageLoader.loadBitmap(r4, r7, r0, r0, r5);	 Catch:{ Throwable -> 0x0173 }
        L_0x0100:
            if (r7 != 0) goto L_0x0106;
        L_0x0102:
            r10.removeTask();	 Catch:{ Throwable -> 0x0173 }
            return;
        L_0x0106:
            r0 = r7.getWidth();	 Catch:{ Throwable -> 0x0173 }
            r4 = r7.getHeight();	 Catch:{ Throwable -> 0x0173 }
            if (r0 == 0) goto L_0x016b;
        L_0x0110:
            if (r4 != 0) goto L_0x0113;
        L_0x0112:
            goto L_0x016b;
        L_0x0113:
            r0 = (float) r0;	 Catch:{ Throwable -> 0x0173 }
            r3 = (float) r3;	 Catch:{ Throwable -> 0x0173 }
            r5 = r0 / r3;
            r4 = (float) r4;	 Catch:{ Throwable -> 0x0173 }
            r3 = r4 / r3;
            r3 = java.lang.Math.min(r5, r3);	 Catch:{ Throwable -> 0x0173 }
            r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r5 <= 0) goto L_0x0132;
        L_0x0124:
            r0 = r0 / r3;
            r0 = (int) r0;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4 / r3;
            r3 = (int) r4;	 Catch:{ Throwable -> 0x0173 }
            r0 = org.telegram.messenger.Bitmaps.createScaledBitmap(r7, r0, r3, r6);	 Catch:{ Throwable -> 0x0173 }
            if (r0 == r7) goto L_0x0132;
        L_0x012e:
            r7.recycle();	 Catch:{ Throwable -> 0x0173 }
            goto L_0x0133;
        L_0x0132:
            r0 = r7;
        L_0x0133:
            r3 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x0173 }
            r3.<init>(r2);	 Catch:{ Throwable -> 0x0173 }
            r2 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Throwable -> 0x0173 }
            r4 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r4 = r4.big;	 Catch:{ Throwable -> 0x0173 }
            if (r4 == 0) goto L_0x0145;
        L_0x0142:
            r4 = 83;
            goto L_0x0147;
        L_0x0145:
            r4 = 60;
        L_0x0147:
            r0.compress(r2, r4, r3);	 Catch:{ Throwable -> 0x0173 }
            r3.close();	 Catch:{ Exception -> 0x014e }
            goto L_0x0152;
        L_0x014e:
            r2 = move-exception;
            org.telegram.messenger.FileLog.m30e(r2);	 Catch:{ Throwable -> 0x0173 }
        L_0x0152:
            r2 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ Throwable -> 0x0173 }
            r2.<init>(r0);	 Catch:{ Throwable -> 0x0173 }
            r0 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x0173 }
            r3 = r10.info;	 Catch:{ Throwable -> 0x0173 }
            r3 = r3.imageReceiverArray;	 Catch:{ Throwable -> 0x0173 }
            r0.<init>(r3);	 Catch:{ Throwable -> 0x0173 }
            r3 = new org.telegram.messenger.-$$Lambda$ImageLoader$ThumbGenerateTask$93c40-AxUp0yfQKw5ZLfHAidaSg;	 Catch:{ Throwable -> 0x0173 }
            r3.<init>(r10, r1, r0, r2);	 Catch:{ Throwable -> 0x0173 }
            org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);	 Catch:{ Throwable -> 0x0173 }
            goto L_0x017a;
        L_0x016b:
            r10.removeTask();	 Catch:{ Throwable -> 0x0173 }
            return;
        L_0x016f:
            r10.removeTask();	 Catch:{ Throwable -> 0x0173 }
            return;
        L_0x0173:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
            r10.removeTask();
        L_0x017a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader$ThumbGenerateTask.run():void");
        }

        public /* synthetic */ void lambda$run$1$ImageLoader$ThumbGenerateTask(String str, ArrayList arrayList, BitmapDrawable bitmapDrawable) {
            removeTask();
            if (this.info.filter != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append("@");
                stringBuilder.append(this.info.filter);
                str = stringBuilder.toString();
            }
            for (int i = 0; i < arrayList.size(); i++) {
                ((ImageReceiver) arrayList.get(i)).setImageBitmapByKey(bitmapDrawable, str, 0, false);
            }
            ImageLoader.this.memCache.put(str, bitmapDrawable);
        }
    }

    public static ImageLoader getInstance() {
        ImageLoader imageLoader = Instance;
        if (imageLoader == null) {
            synchronized (ImageLoader.class) {
                imageLoader = Instance;
                if (imageLoader == null) {
                    imageLoader = new ImageLoader();
                    Instance = imageLoader;
                }
            }
        }
        return imageLoader;
    }

    public ImageLoader() {
        int i = 0;
        this.currentHttpTasksCount = 0;
        this.currentArtworkTasksCount = 0;
        this.testWebFile = new ConcurrentHashMap();
        this.httpFileLoadTasks = new LinkedList();
        this.httpFileLoadTasksByKeys = new HashMap();
        this.retryHttpsTasks = new HashMap();
        this.currentHttpFileLoadTasksCount = 0;
        this.ignoreRemoval = null;
        this.lastCacheOutTime = 0;
        this.lastImageNum = 0;
        this.lastProgressUpdateTime = 0;
        this.telegramPath = null;
        boolean z = true;
        this.thumbGeneratingQueue.setPriority(1);
        int memoryClass = ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
        if (memoryClass < 192) {
            z = false;
        }
        this.canForce8888 = z;
        this.memCache = new LruCache<BitmapDrawable>((Math.min(z ? 30 : 15, memoryClass / 7) * 1024) * 1024) {
            /* Access modifiers changed, original: protected */
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount();
            }

            /* Access modifiers changed, original: protected */
            public void entryRemoved(boolean z, String str, BitmapDrawable bitmapDrawable, BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval == null || !ImageLoader.this.ignoreRemoval.equals(str)) {
                    Integer num = (Integer) ImageLoader.this.bitmapUseCounts.get(str);
                    if (num == null || num.intValue() == 0) {
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                    }
                }
            }
        };
        this.lottieMemCache = new LruCache<LottieDrawable>(5) {
            /* Access modifiers changed, original: protected */
            public int sizeOf(String str, LottieDrawable lottieDrawable) {
                return 1;
            }
        };
        SparseArray sparseArray = new SparseArray();
        File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        try {
            new File(cacheDir, ".nomedia").createNewFile();
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
        sparseArray.put(4, cacheDir);
        while (i < 3) {
            FileLoader.getInstance(i).setDelegate(new FileLoaderDelegate() {
                public void fileUploadProgressChanged(String str, float f, boolean z) {
                    ImageLoader.this.fileProgresses.put(str, Float.valueOf(f));
                    long currentTimeMillis = System.currentTimeMillis();
                    if (ImageLoader.this.lastProgressUpdateTime == 0 || ImageLoader.this.lastProgressUpdateTime < currentTimeMillis - 500) {
                        ImageLoader.this.lastProgressUpdateTime = currentTimeMillis;
                        AndroidUtilities.runOnUIThread(new C0473-$$Lambda$ImageLoader$3$FqjDIba7eslh2iQtqmCAn4WCKjU(i, str, f, z));
                    }
                }

                public void fileDidUploaded(String str, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] bArr, byte[] bArr2, long j) {
                    Utilities.stageQueue.postRunnable(new C0475-$$Lambda$ImageLoader$3$_9uOaZVxDmXjnClpJdro4iTil8Q(this, i, str, inputFile, inputEncryptedFile, bArr, bArr2, j));
                }

                public /* synthetic */ void lambda$fileDidUploaded$2$ImageLoader$3(int i, String str, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] bArr, byte[] bArr2, long j) {
                    AndroidUtilities.runOnUIThread(new C0474-$$Lambda$ImageLoader$3$O9MuPZ9kD1RfxaVvbxm2MMOXv_E(i, str, inputFile, inputEncryptedFile, bArr, bArr2, j));
                    ImageLoader.this.fileProgresses.remove(str);
                }

                public void fileDidFailedUpload(String str, boolean z) {
                    Utilities.stageQueue.postRunnable(new C0470-$$Lambda$ImageLoader$3$3M_VSd8r5buZPqJNpilxe2zYwos(this, i, str, z));
                }

                public /* synthetic */ void lambda$fileDidFailedUpload$4$ImageLoader$3(int i, String str, boolean z) {
                    AndroidUtilities.runOnUIThread(new C0477-$$Lambda$ImageLoader$3$lo1j3M5K6zygAvdIFH_K82igfdE(i, str, z));
                    ImageLoader.this.fileProgresses.remove(str);
                }

                public void fileDidLoaded(String str, File file, int i) {
                    ImageLoader.this.fileProgresses.remove(str);
                    AndroidUtilities.runOnUIThread(new C0471-$$Lambda$ImageLoader$3$5Bs2fzd8mEyFObZ9x2J38wn4r-c(this, file, str, i, i));
                }

                public /* synthetic */ void lambda$fileDidLoaded$5$ImageLoader$3(File file, String str, int i, int i2) {
                    if (SharedConfig.saveToGallery && ImageLoader.this.telegramPath != null && file != null && ((str.endsWith(".mp4") || str.endsWith(".jpg")) && file.toString().startsWith(ImageLoader.this.telegramPath.toString()))) {
                        AndroidUtilities.addMediaToGallery(file.toString());
                    }
                    NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.fileDidLoad, str);
                    ImageLoader.this.fileDidLoaded(str, file, i2);
                }

                public void fileDidFailedLoad(String str, int i) {
                    ImageLoader.this.fileProgresses.remove(str);
                    AndroidUtilities.runOnUIThread(new C0472-$$Lambda$ImageLoader$3$9ENgl3dEFjSW9fmN2KOTidfJzZ0(this, str, i, i));
                }

                public /* synthetic */ void lambda$fileDidFailedLoad$6$ImageLoader$3(String str, int i, int i2) {
                    ImageLoader.this.fileDidFailedLoad(str, i);
                    NotificationCenter.getInstance(i2).postNotificationName(NotificationCenter.fileDidFailedLoad, str, Integer.valueOf(i));
                }

                public void fileLoadProgressChanged(String str, float f) {
                    ImageLoader.this.fileProgresses.put(str, Float.valueOf(f));
                    long currentTimeMillis = System.currentTimeMillis();
                    if (ImageLoader.this.lastProgressUpdateTime == 0 || ImageLoader.this.lastProgressUpdateTime < currentTimeMillis - 500) {
                        ImageLoader.this.lastProgressUpdateTime = currentTimeMillis;
                        AndroidUtilities.runOnUIThread(new C0476-$$Lambda$ImageLoader$3$iY0R0L0rfhonnXAaHAq36LTxLHA(i, str, f));
                    }
                }
            });
            i++;
        }
        FileLoader.setMediaDirs(sparseArray);
        C10284 c10284 = new C10284();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentFilter.addAction("android.intent.action.MEDIA_CHECKING");
        intentFilter.addAction("android.intent.action.MEDIA_EJECT");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_NOFS");
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        intentFilter.addAction("android.intent.action.MEDIA_SHARED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addDataScheme("file");
        try {
            ApplicationLoader.applicationContext.registerReceiver(c10284, intentFilter);
        } catch (Throwable unused) {
        }
        checkMediaPaths();
    }

    public void checkMediaPaths() {
        this.cacheOutQueue.postRunnable(new C0497-$$Lambda$ImageLoader$TEcsmbVkFIlJCFa-8B6JxwYMU3A(this));
    }

    public /* synthetic */ void lambda$checkMediaPaths$1$ImageLoader() {
        AndroidUtilities.runOnUIThread(new C0479-$$Lambda$ImageLoader$54eJN0C_gHRDy8W6-JUJzMnPHt0(createMediaPaths()));
    }

    public void addTestWebFile(String str, WebFile webFile) {
        if (str != null && webFile != null) {
            this.testWebFile.put(str, webFile);
        }
    }

    public void removeTestWebFile(String str) {
        if (str != null) {
            this.testWebFile.remove(str);
        }
    }

    public SparseArray<File> createMediaPaths() {
        String str = ".nomedia";
        SparseArray sparseArray = new SparseArray();
        File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        try {
            new File(cacheDir, str).createNewFile();
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
        sparseArray.put(4, cacheDir);
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cache path = ");
            stringBuilder.append(cacheDir);
            FileLog.m27d(stringBuilder.toString());
        }
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                this.telegramPath = new File(Environment.getExternalStorageDirectory(), "Telegram");
                this.telegramPath.mkdirs();
                if (this.telegramPath.isDirectory()) {
                    File file;
                    StringBuilder stringBuilder2;
                    try {
                        file = new File(this.telegramPath, "Telegram Images");
                        file.mkdir();
                        if (file.isDirectory() && canMoveFiles(cacheDir, file, 0)) {
                            sparseArray.put(0, file);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("image path = ");
                                stringBuilder2.append(file);
                                FileLog.m27d(stringBuilder2.toString());
                            }
                        }
                    } catch (Exception e22) {
                        FileLog.m30e(e22);
                    }
                    try {
                        file = new File(this.telegramPath, "Telegram Video");
                        file.mkdir();
                        if (file.isDirectory() && canMoveFiles(cacheDir, file, 2)) {
                            sparseArray.put(2, file);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("video path = ");
                                stringBuilder2.append(file);
                                FileLog.m27d(stringBuilder2.toString());
                            }
                        }
                    } catch (Exception e222) {
                        FileLog.m30e(e222);
                    }
                    try {
                        file = new File(this.telegramPath, "Telegram Audio");
                        file.mkdir();
                        if (file.isDirectory() && canMoveFiles(cacheDir, file, 1)) {
                            new File(file, str).createNewFile();
                            sparseArray.put(1, file);
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("audio path = ");
                                stringBuilder2.append(file);
                                FileLog.m27d(stringBuilder2.toString());
                            }
                        }
                    } catch (Exception e2222) {
                        FileLog.m30e(e2222);
                    }
                    try {
                        file = new File(this.telegramPath, "Telegram Documents");
                        file.mkdir();
                        if (file.isDirectory() && canMoveFiles(cacheDir, file, 3)) {
                            new File(file, str).createNewFile();
                            sparseArray.put(3, file);
                            if (BuildVars.LOGS_ENABLED) {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("documents path = ");
                                stringBuilder3.append(file);
                                FileLog.m27d(stringBuilder3.toString());
                            }
                        }
                    } catch (Exception e3) {
                        FileLog.m30e(e3);
                    }
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        } catch (Exception e32) {
            FileLog.m30e(e32);
        }
        return sparseArray;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0086 A:{SYNTHETIC, Splitter:B:38:0x0086} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007a A:{SYNTHETIC, Splitter:B:31:0x007a} */
    private boolean canMoveFiles(java.io.File r5, java.io.File r6, int r7) {
        /*
        r4 = this;
        r0 = 1;
        r1 = 0;
        if (r7 != 0) goto L_0x0018;
    L_0x0004:
        r7 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999_temp.jpg";
        r7.<init>(r5, r2);	 Catch:{ Exception -> 0x0016 }
        r5 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999.jpg";
        r5.<init>(r6, r2);	 Catch:{ Exception -> 0x0016 }
        goto L_0x004f;
    L_0x0013:
        r5 = move-exception;
        goto L_0x0084;
    L_0x0016:
        r5 = move-exception;
        goto L_0x0075;
    L_0x0018:
        r2 = 3;
        if (r7 != r2) goto L_0x002a;
    L_0x001b:
        r7 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999_temp.doc";
        r7.<init>(r5, r2);	 Catch:{ Exception -> 0x0016 }
        r5 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999.doc";
        r5.<init>(r6, r2);	 Catch:{ Exception -> 0x0016 }
        goto L_0x004f;
    L_0x002a:
        if (r7 != r0) goto L_0x003b;
    L_0x002c:
        r7 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999_temp.ogg";
        r7.<init>(r5, r2);	 Catch:{ Exception -> 0x0016 }
        r5 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999.ogg";
        r5.<init>(r6, r2);	 Catch:{ Exception -> 0x0016 }
        goto L_0x004f;
    L_0x003b:
        r2 = 2;
        if (r7 != r2) goto L_0x004d;
    L_0x003e:
        r7 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999_temp.mp4";
        r7.<init>(r5, r2);	 Catch:{ Exception -> 0x0016 }
        r5 = new java.io.File;	 Catch:{ Exception -> 0x0016 }
        r2 = "000000000_999999.mp4";
        r5.<init>(r6, r2);	 Catch:{ Exception -> 0x0016 }
        goto L_0x004f;
    L_0x004d:
        r5 = r1;
        r7 = r5;
    L_0x004f:
        r6 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r6 = new byte[r6];	 Catch:{ Exception -> 0x0016 }
        r7.createNewFile();	 Catch:{ Exception -> 0x0016 }
        r2 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x0016 }
        r3 = "rws";
        r2.<init>(r7, r3);	 Catch:{ Exception -> 0x0016 }
        r2.write(r6);	 Catch:{ Exception -> 0x0073, all -> 0x0070 }
        r2.close();	 Catch:{ Exception -> 0x0073, all -> 0x0070 }
        r6 = r7.renameTo(r5);	 Catch:{ Exception -> 0x0016 }
        r7.delete();	 Catch:{ Exception -> 0x0016 }
        r5.delete();	 Catch:{ Exception -> 0x0016 }
        if (r6 == 0) goto L_0x0082;
    L_0x006f:
        return r0;
    L_0x0070:
        r5 = move-exception;
        r1 = r2;
        goto L_0x0084;
    L_0x0073:
        r5 = move-exception;
        r1 = r2;
    L_0x0075:
        org.telegram.messenger.FileLog.m30e(r5);	 Catch:{ all -> 0x0013 }
        if (r1 == 0) goto L_0x0082;
    L_0x007a:
        r1.close();	 Catch:{ Exception -> 0x007e }
        goto L_0x0082;
    L_0x007e:
        r5 = move-exception;
        org.telegram.messenger.FileLog.m30e(r5);
    L_0x0082:
        r5 = 0;
        return r5;
    L_0x0084:
        if (r1 == 0) goto L_0x008e;
    L_0x0086:
        r1.close();	 Catch:{ Exception -> 0x008a }
        goto L_0x008e;
    L_0x008a:
        r6 = move-exception;
        org.telegram.messenger.FileLog.m30e(r6);
    L_0x008e:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.canMoveFiles(java.io.File, java.io.File, int):boolean");
    }

    public Float getFileProgress(String str) {
        return str == null ? null : (Float) this.fileProgresses.get(str);
    }

    public String getReplacedKey(String str) {
        return str == null ? null : (String) this.replacedBitmaps.get(str);
    }

    private void performReplace(String str, String str2) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.memCache.get(str);
        this.replacedBitmaps.put(str, str2);
        if (bitmapDrawable != null) {
            BitmapDrawable bitmapDrawable2 = (BitmapDrawable) this.memCache.get(str2);
            Object obj = null;
            if (!(bitmapDrawable2 == null || bitmapDrawable2.getBitmap() == null || bitmapDrawable.getBitmap() == null)) {
                Bitmap bitmap = bitmapDrawable2.getBitmap();
                Bitmap bitmap2 = bitmapDrawable.getBitmap();
                if (bitmap.getWidth() > bitmap2.getWidth() || bitmap.getHeight() > bitmap2.getHeight()) {
                    obj = 1;
                }
            }
            if (obj == null) {
                this.ignoreRemoval = str;
                this.memCache.remove(str);
                this.memCache.put(str2, bitmapDrawable);
                this.ignoreRemoval = null;
            } else {
                this.memCache.remove(str);
            }
        }
        Integer num = (Integer) this.bitmapUseCounts.get(str);
        if (num != null) {
            this.bitmapUseCounts.put(str2, num);
            this.bitmapUseCounts.remove(str);
        }
    }

    public void incrementUseCount(String str) {
        Integer num = (Integer) this.bitmapUseCounts.get(str);
        if (num == null) {
            this.bitmapUseCounts.put(str, Integer.valueOf(1));
        } else {
            this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() + 1));
        }
    }

    public boolean decrementUseCount(String str) {
        Integer num = (Integer) this.bitmapUseCounts.get(str);
        if (num == null) {
            return true;
        }
        if (num.intValue() == 1) {
            this.bitmapUseCounts.remove(str);
            return true;
        }
        this.bitmapUseCounts.put(str, Integer.valueOf(num.intValue() - 1));
        return false;
    }

    public void removeImage(String str) {
        this.bitmapUseCounts.remove(str);
        this.memCache.remove(str);
    }

    public boolean isInCache(String str) {
        return this.memCache.get(str) != null;
    }

    public void clearMemory() {
        this.memCache.evictAll();
        this.lottieMemCache.evictAll();
    }

    private void removeFromWaitingForThumb(int i, ImageReceiver imageReceiver) {
        String str = (String) this.waitingForQualityThumbByTag.get(i);
        if (str != null) {
            ThumbGenerateInfo thumbGenerateInfo = (ThumbGenerateInfo) this.waitingForQualityThumb.get(str);
            if (thumbGenerateInfo != null) {
                thumbGenerateInfo.imageReceiverArray.remove(imageReceiver);
                if (thumbGenerateInfo.imageReceiverArray.isEmpty()) {
                    this.waitingForQualityThumb.remove(str);
                }
            }
            this.waitingForQualityThumbByTag.remove(i);
        }
    }

    public void cancelLoadingForImageReceiver(ImageReceiver imageReceiver, boolean z) {
        if (imageReceiver != null) {
            this.imageLoadQueue.postRunnable(new C0496-$$Lambda$ImageLoader$QQrxxTOTOPgi4Ibzj2dcFh6tMmY(this, z, imageReceiver));
        }
    }

    public /* synthetic */ void lambda$cancelLoadingForImageReceiver$2$ImageLoader(boolean z, ImageReceiver imageReceiver) {
        int i = 0;
        while (true) {
            int i2 = 3;
            if (i >= 3) {
                return;
            }
            if (i <= 0 || z) {
                if (i == 0) {
                    i2 = 1;
                } else if (i == 1) {
                    i2 = 0;
                }
                i2 = imageReceiver.getTag(i2);
                if (i2 != 0) {
                    if (i == 0) {
                        removeFromWaitingForThumb(i2, imageReceiver);
                    }
                    CacheImage cacheImage = (CacheImage) this.imageLoadingByTag.get(i2);
                    if (cacheImage != null) {
                        cacheImage.removeImageReceiver(imageReceiver);
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    public BitmapDrawable getAnyImageFromMemory(String str) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.memCache.get(str);
        if (bitmapDrawable == null) {
            ArrayList filterKeys = this.memCache.getFilterKeys(str);
            if (!(filterKeys == null || filterKeys.isEmpty())) {
                LruCache lruCache = this.memCache;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append("@");
                stringBuilder.append((String) filterKeys.get(0));
                return (BitmapDrawable) lruCache.get(stringBuilder.toString());
            }
        }
        return bitmapDrawable;
    }

    public BitmapDrawable getImageFromMemory(TLObject tLObject, String str, String str2) {
        String str3 = null;
        if (tLObject == null && str == null) {
            return null;
        }
        if (str != null) {
            str3 = Utilities.MD5(str);
        } else {
            String str4 = "_";
            StringBuilder stringBuilder;
            if (tLObject instanceof FileLocation) {
                FileLocation fileLocation = (FileLocation) tLObject;
                stringBuilder = new StringBuilder();
                stringBuilder.append(fileLocation.volume_id);
                stringBuilder.append(str4);
                stringBuilder.append(fileLocation.local_id);
                str3 = stringBuilder.toString();
            } else if (tLObject instanceof Document) {
                Document document = (Document) tLObject;
                stringBuilder = new StringBuilder();
                stringBuilder.append(document.dc_id);
                stringBuilder.append(str4);
                stringBuilder.append(document.f441id);
                str3 = stringBuilder.toString();
            } else if (tLObject instanceof SecureDocument) {
                SecureDocument secureDocument = (SecureDocument) tLObject;
                stringBuilder = new StringBuilder();
                stringBuilder.append(secureDocument.secureFile.dc_id);
                stringBuilder.append(str4);
                stringBuilder.append(secureDocument.secureFile.f561id);
                str3 = stringBuilder.toString();
            } else if (tLObject instanceof WebFile) {
                str3 = Utilities.MD5(((WebFile) tLObject).url);
            }
        }
        if (str2 != null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str3);
            stringBuilder2.append("@");
            stringBuilder2.append(str2);
            str3 = stringBuilder2.toString();
        }
        return (BitmapDrawable) this.memCache.get(str3);
    }

    private void replaceImageInCacheInternal(String str, String str2, ImageLocation imageLocation) {
        ArrayList filterKeys = this.memCache.getFilterKeys(str);
        if (filterKeys != null) {
            for (int i = 0; i < filterKeys.size(); i++) {
                String str3 = (String) filterKeys.get(i);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                String str4 = "@";
                stringBuilder.append(str4);
                stringBuilder.append(str3);
                String stringBuilder2 = stringBuilder.toString();
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append(str4);
                stringBuilder3.append(str3);
                performReplace(stringBuilder2, stringBuilder3.toString());
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, stringBuilder2, str3, imageLocation);
            }
            return;
        }
        performReplace(str, str2);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, str, str2, imageLocation);
    }

    public /* synthetic */ void lambda$replaceImageInCache$3$ImageLoader(String str, String str2, ImageLocation imageLocation) {
        replaceImageInCacheInternal(str, str2, imageLocation);
    }

    public void replaceImageInCache(String str, String str2, ImageLocation imageLocation, boolean z) {
        if (z) {
            AndroidUtilities.runOnUIThread(new C0504-$$Lambda$ImageLoader$goqDHHQdnb5snOP60neGaS99rrI(this, str, str2, imageLocation));
        } else {
            replaceImageInCacheInternal(str, str2, imageLocation);
        }
    }

    public void putImageToCache(BitmapDrawable bitmapDrawable, String str) {
        this.memCache.put(str, bitmapDrawable);
    }

    private void generateThumb(int i, File file, ThumbGenerateInfo thumbGenerateInfo) {
        if ((i == 0 || i == 2 || i == 3) && file != null && thumbGenerateInfo != null) {
            if (((ThumbGenerateTask) this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument))) == null) {
                this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(i, file, thumbGenerateInfo));
            }
        }
    }

    public void cancelForceLoadingForImageReceiver(ImageReceiver imageReceiver) {
        if (imageReceiver != null) {
            String imageKey = imageReceiver.getImageKey();
            if (imageKey != null) {
                this.imageLoadQueue.postRunnable(new C0507-$$Lambda$ImageLoader$pOb77-ep1O4qdDkpbIPheznVQgg(this, imageKey));
            }
        }
    }

    public /* synthetic */ void lambda$cancelForceLoadingForImageReceiver$4$ImageLoader(String str) {
        Integer num = (Integer) this.forceLoadingImages.remove(str);
    }

    private void createLoadOperationForImageReceiver(ImageReceiver imageReceiver, String str, String str2, String str3, ImageLocation imageLocation, String str4, int i, int i2, int i3, int i4) {
        ImageReceiver imageReceiver2 = imageReceiver;
        int i5 = i3;
        if (imageReceiver2 != null && str2 != null && str != null && imageLocation != null) {
            int tag = imageReceiver2.getTag(i5);
            if (tag == 0) {
                tag = this.lastImageNum;
                imageReceiver2.setTag(tag, i5);
                this.lastImageNum++;
                if (this.lastImageNum == Integer.MAX_VALUE) {
                    this.lastImageNum = 0;
                }
            }
            int i6 = tag;
            boolean isNeedsQualityThumb = imageReceiver.isNeedsQualityThumb();
            Object parentObject = imageReceiver.getParentObject();
            Document qulityThumbDocument = imageReceiver.getQulityThumbDocument();
            boolean isShouldGenerateQualityThumb = imageReceiver.isShouldGenerateQualityThumb();
            int currentAccount = imageReceiver.getCurrentAccount();
            boolean z = i5 == 0 && imageReceiver.isCurrentKeyQuality();
            C0503-$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU c0503-$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU = r0;
            DispatchQueue dispatchQueue = this.imageLoadQueue;
            C0503-$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU c0503-$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU2 = new C0503-$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU(this, i4, str2, str, i6, imageReceiver, str4, i3, imageLocation, z, parentObject, qulityThumbDocument, isNeedsQualityThumb, isShouldGenerateQualityThumb, i2, i, str3, currentAccount);
            dispatchQueue.postRunnable(c0503-$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x014b  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0159  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01a1  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x015d  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x0373  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x048c  */
    /* JADX WARNING: Removed duplicated region for block: B:213:0x0484  */
    /* JADX WARNING: Missing block: B:61:0x0154, code skipped:
            if (r2.exists() == false) goto L_0x0156;
     */
    /* JADX WARNING: Missing block: B:122:0x025e, code skipped:
            if (r3 == false) goto L_0x02a9;
     */
    public /* synthetic */ void lambda$createLoadOperationForImageReceiver$5$ImageLoader(int r21, java.lang.String r22, java.lang.String r23, int r24, org.telegram.messenger.ImageReceiver r25, java.lang.String r26, int r27, org.telegram.messenger.ImageLocation r28, boolean r29, java.lang.Object r30, org.telegram.tgnet.TLRPC.Document r31, boolean r32, boolean r33, int r34, int r35, java.lang.String r36, int r37) {
        /*
        r20 = this;
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r23;
        r4 = r24;
        r5 = r25;
        r6 = r26;
        r7 = r27;
        r8 = r28;
        r9 = r30;
        r10 = r31;
        r11 = r34;
        r12 = r35;
        r13 = 2;
        if (r1 == r13) goto L_0x0058;
    L_0x001d:
        r14 = r0.imageLoadingByUrl;
        r14 = r14.get(r2);
        r14 = (org.telegram.messenger.ImageLoader.CacheImage) r14;
        r13 = r0.imageLoadingByKeys;
        r13 = r13.get(r3);
        r13 = (org.telegram.messenger.ImageLoader.CacheImage) r13;
        r15 = r0.imageLoadingByTag;
        r15 = r15.get(r4);
        r15 = (org.telegram.messenger.ImageLoader.CacheImage) r15;
        if (r15 == 0) goto L_0x0046;
    L_0x0037:
        if (r15 != r13) goto L_0x003b;
    L_0x0039:
        r15 = 1;
        goto L_0x0047;
    L_0x003b:
        if (r15 != r14) goto L_0x0043;
    L_0x003d:
        if (r13 != 0) goto L_0x0039;
    L_0x003f:
        r15.replaceImageReceiver(r5, r3, r6, r7);
        goto L_0x0039;
    L_0x0043:
        r15.removeImageReceiver(r5);
    L_0x0046:
        r15 = 0;
    L_0x0047:
        if (r15 != 0) goto L_0x004f;
    L_0x0049:
        if (r13 == 0) goto L_0x004f;
    L_0x004b:
        r13.addImageReceiver(r5, r3, r6, r7);
        r15 = 1;
    L_0x004f:
        if (r15 != 0) goto L_0x0059;
    L_0x0051:
        if (r14 == 0) goto L_0x0059;
    L_0x0053:
        r14.addImageReceiver(r5, r3, r6, r7);
        r15 = 1;
        goto L_0x0059;
    L_0x0058:
        r15 = 0;
    L_0x0059:
        if (r15 != 0) goto L_0x0493;
    L_0x005b:
        r13 = r8.path;
        r14 = "_";
        r15 = "athumb";
        r17 = 3;
        r18 = 4;
        if (r13 == 0) goto L_0x00c7;
    L_0x0067:
        r4 = "http";
        r4 = r13.startsWith(r4);
        if (r4 != 0) goto L_0x00bd;
    L_0x006f:
        r4 = r13.startsWith(r15);
        if (r4 != 0) goto L_0x00bd;
    L_0x0075:
        r4 = "thumb://";
        r4 = r13.startsWith(r4);
        r10 = ":";
        if (r4 == 0) goto L_0x0097;
    L_0x007f:
        r4 = 8;
        r4 = r13.indexOf(r10, r4);
        if (r4 < 0) goto L_0x0095;
    L_0x0087:
        r10 = new java.io.File;
        r16 = 1;
        r4 = r4 + 1;
        r4 = r13.substring(r4);
        r10.<init>(r4);
        goto L_0x00bb;
    L_0x0095:
        r10 = 0;
        goto L_0x00bb;
    L_0x0097:
        r4 = "vthumb://";
        r4 = r13.startsWith(r4);
        if (r4 == 0) goto L_0x00b5;
    L_0x009f:
        r4 = 9;
        r4 = r13.indexOf(r10, r4);
        if (r4 < 0) goto L_0x0095;
    L_0x00a7:
        r10 = new java.io.File;
        r16 = 1;
        r4 = r4 + 1;
        r4 = r13.substring(r4);
        r10.<init>(r4);
        goto L_0x00bb;
    L_0x00b5:
        r4 = new java.io.File;
        r4.<init>(r13);
        r10 = r4;
    L_0x00bb:
        r4 = 1;
        goto L_0x00bf;
    L_0x00bd:
        r4 = 0;
        r10 = 0;
    L_0x00bf:
        r9 = r10;
        r19 = r15;
        r2 = 0;
        r3 = 2;
        r15 = r4;
        goto L_0x01b6;
    L_0x00c7:
        if (r1 != 0) goto L_0x01ae;
    L_0x00c9:
        if (r29 == 0) goto L_0x01ae;
    L_0x00cb:
        r13 = r9 instanceof org.telegram.messenger.MessageObject;
        if (r13 == 0) goto L_0x00ed;
    L_0x00cf:
        r10 = r9;
        r10 = (org.telegram.messenger.MessageObject) r10;
        r13 = r10.getDocument();
        r3 = r10.messageOwner;
        r31 = r13;
        r13 = r3.attachPath;
        r3 = org.telegram.messenger.FileLoader.getPathToMessage(r3);
        r10 = r10.getFileType();
        r19 = r15;
        r15 = r10;
        r10 = r31;
        r31 = r3;
        r3 = 0;
        goto L_0x010d;
    L_0x00ed:
        if (r10 == 0) goto L_0x0105;
    L_0x00ef:
        r3 = 1;
        r13 = org.telegram.messenger.FileLoader.getPathToAttach(r10, r3);
        r3 = org.telegram.messenger.MessageObject.isVideoDocument(r31);
        if (r3 == 0) goto L_0x00fc;
    L_0x00fa:
        r3 = 2;
        goto L_0x00fd;
    L_0x00fc:
        r3 = 3;
    L_0x00fd:
        r31 = r13;
        r19 = r15;
        r13 = 0;
        r15 = r3;
        r3 = 1;
        goto L_0x010d;
    L_0x0105:
        r19 = r15;
        r31 = 0;
        r3 = 0;
        r10 = 0;
        r13 = 0;
        r15 = 0;
    L_0x010d:
        if (r10 == 0) goto L_0x01a7;
    L_0x010f:
        if (r32 == 0) goto L_0x0143;
    L_0x0111:
        r9 = new java.io.File;
        r7 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r11 = "q_";
        r12.append(r11);
        r11 = r10.dc_id;
        r12.append(r11);
        r12.append(r14);
        r1 = r10.f441id;
        r12.append(r1);
        r1 = ".jpg";
        r12.append(r1);
        r1 = r12.toString();
        r9.<init>(r7, r1);
        r1 = r9.exists();
        if (r1 != 0) goto L_0x0141;
    L_0x0140:
        goto L_0x0143;
    L_0x0141:
        r1 = 1;
        goto L_0x0145;
    L_0x0143:
        r1 = 0;
        r9 = 0;
    L_0x0145:
        r2 = android.text.TextUtils.isEmpty(r13);
        if (r2 != 0) goto L_0x0156;
    L_0x014b:
        r2 = new java.io.File;
        r2.<init>(r13);
        r7 = r2.exists();
        if (r7 != 0) goto L_0x0157;
    L_0x0156:
        r2 = 0;
    L_0x0157:
        if (r2 != 0) goto L_0x015b;
    L_0x0159:
        r2 = r31;
    L_0x015b:
        if (r9 != 0) goto L_0x01a1;
    L_0x015d:
        r1 = org.telegram.messenger.FileLoader.getAttachFileName(r10);
        r7 = r0.waitingForQualityThumb;
        r7 = r7.get(r1);
        r7 = (org.telegram.messenger.ImageLoader.ThumbGenerateInfo) r7;
        if (r7 != 0) goto L_0x017f;
    L_0x016b:
        r7 = new org.telegram.messenger.ImageLoader$ThumbGenerateInfo;
        r8 = 0;
        r7.<init>(r0, r8);
        r7.parentDocument = r10;
        r7.filter = r6;
        r7.big = r3;
        r3 = r0.waitingForQualityThumb;
        r3.put(r1, r7);
    L_0x017f:
        r3 = r7.imageReceiverArray;
        r3 = r3.contains(r5);
        if (r3 != 0) goto L_0x0190;
    L_0x0189:
        r3 = r7.imageReceiverArray;
        r3.add(r5);
    L_0x0190:
        r3 = r0.waitingForQualityThumbByTag;
        r3.put(r4, r1);
        r1 = r2.exists();
        if (r1 == 0) goto L_0x01a0;
    L_0x019b:
        if (r33 == 0) goto L_0x01a0;
    L_0x019d:
        r0.generateThumb(r15, r2, r7);
    L_0x01a0:
        return;
    L_0x01a1:
        r2 = r1;
        r3 = 2;
        r15 = 1;
        r1 = r21;
        goto L_0x01b6;
    L_0x01a7:
        r1 = r21;
        r2 = 0;
        r3 = 2;
        r9 = 0;
        r15 = 1;
        goto L_0x01b6;
    L_0x01ae:
        r19 = r15;
        r1 = r21;
        r2 = 0;
        r3 = 2;
        r9 = 0;
        r15 = 0;
    L_0x01b6:
        if (r1 == r3) goto L_0x0493;
    L_0x01b8:
        r3 = r28.isEncrypted();
        r4 = new org.telegram.messenger.ImageLoader$CacheImage;
        r7 = 0;
        r4.<init>(r0, r7);
        if (r29 != 0) goto L_0x020e;
    L_0x01c4:
        r7 = r8.webFile;
        r7 = org.telegram.messenger.MessageObject.isGifDocument(r7);
        if (r7 != 0) goto L_0x020b;
    L_0x01cc:
        r7 = r8.document;
        r7 = org.telegram.messenger.MessageObject.isGifDocument(r7);
        if (r7 != 0) goto L_0x020b;
    L_0x01d4:
        r7 = r8.document;
        r7 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r7);
        if (r7 == 0) goto L_0x01dd;
    L_0x01dc:
        goto L_0x020b;
    L_0x01dd:
        r7 = r8.path;
        if (r7 == 0) goto L_0x020e;
    L_0x01e1:
        r10 = "vthumb";
        r10 = r7.startsWith(r10);
        if (r10 != 0) goto L_0x020e;
    L_0x01e9:
        r10 = "thumb";
        r10 = r7.startsWith(r10);
        if (r10 != 0) goto L_0x020e;
    L_0x01f1:
        r10 = "jpg";
        r7 = getHttpUrlExtension(r7, r10);
        r10 = "mp4";
        r10 = r7.equals(r10);
        if (r10 != 0) goto L_0x0207;
    L_0x01ff:
        r10 = "gif";
        r7 = r7.equals(r10);
        if (r7 == 0) goto L_0x020e;
    L_0x0207:
        r7 = 1;
        r4.animatedFile = r7;
        goto L_0x020e;
    L_0x020b:
        r7 = 1;
        r4.animatedFile = r7;
    L_0x020e:
        if (r9 != 0) goto L_0x034d;
    L_0x0210:
        r7 = r8.photoSize;
        r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        r10 = "g";
        if (r7 == 0) goto L_0x0222;
    L_0x0218:
        r11 = r22;
        r7 = r34;
        r12 = r35;
        r14 = 0;
        r15 = 1;
        goto L_0x033e;
    L_0x0222:
        r7 = r8.secureDocument;
        if (r7 == 0) goto L_0x0248;
    L_0x0226:
        r4.secureDocument = r7;
        r3 = r4.secureDocument;
        r3 = r3.secureFile;
        r3 = r3.dc_id;
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r3 != r7) goto L_0x0234;
    L_0x0232:
        r15 = 1;
        goto L_0x0235;
    L_0x0234:
        r15 = 0;
    L_0x0235:
        r3 = new java.io.File;
        r7 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r11 = r22;
        r3.<init>(r7, r11);
        r7 = r34;
        r12 = r35;
    L_0x0244:
        r9 = r3;
    L_0x0245:
        r14 = 0;
        goto L_0x033e;
    L_0x0248:
        r11 = r22;
        r7 = r10.equals(r6);
        r9 = "application/x-tgsticker";
        if (r7 != 0) goto L_0x02a5;
    L_0x0252:
        r7 = r34;
        if (r7 != 0) goto L_0x0261;
    L_0x0256:
        r12 = r35;
        if (r12 <= 0) goto L_0x0263;
    L_0x025a:
        r13 = r8.path;
        if (r13 != 0) goto L_0x0263;
    L_0x025e:
        if (r3 == 0) goto L_0x02a9;
    L_0x0260:
        goto L_0x0263;
    L_0x0261:
        r12 = r35;
    L_0x0263:
        r3 = new java.io.File;
        r13 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r3.<init>(r13, r11);
        r13 = r3.exists();
        if (r13 == 0) goto L_0x0274;
    L_0x0272:
        r2 = 1;
        goto L_0x0298;
    L_0x0274:
        r13 = 2;
        if (r7 != r13) goto L_0x0296;
    L_0x0277:
        r3 = new java.io.File;
        r13 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r14.append(r11);
        r24 = r2;
        r2 = ".enc";
        r14.append(r2);
        r2 = r14.toString();
        r3.<init>(r13, r2);
        r2 = r24;
        goto L_0x0298;
    L_0x0296:
        r24 = r2;
    L_0x0298:
        r13 = r8.document;
        if (r13 == 0) goto L_0x0244;
    L_0x029c:
        r13 = r13.mime_type;
        r9 = r9.equals(r13);
        r4.lottieFile = r9;
        goto L_0x0244;
    L_0x02a5:
        r7 = r34;
        r12 = r35;
    L_0x02a9:
        r24 = r2;
        r2 = r8.document;
        if (r2 == 0) goto L_0x031d;
    L_0x02af:
        r3 = r2 instanceof org.telegram.tgnet.TLRPC.TL_documentEncrypted;
        if (r3 == 0) goto L_0x02bf;
    L_0x02b3:
        r3 = new java.io.File;
        r13 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r3.<init>(r13, r11);
        r31 = r15;
        goto L_0x02dd;
    L_0x02bf:
        r3 = org.telegram.messenger.MessageObject.isVideoDocument(r2);
        if (r3 == 0) goto L_0x02d2;
    L_0x02c5:
        r3 = new java.io.File;
        r31 = r15;
        r13 = 2;
        r15 = org.telegram.messenger.FileLoader.getDirectory(r13);
        r3.<init>(r15, r11);
        goto L_0x02dd;
    L_0x02d2:
        r31 = r15;
        r3 = new java.io.File;
        r13 = org.telegram.messenger.FileLoader.getDirectory(r17);
        r3.<init>(r13, r11);
    L_0x02dd:
        r13 = r10.equals(r6);
        if (r13 == 0) goto L_0x030d;
    L_0x02e3:
        r13 = r3.exists();
        if (r13 != 0) goto L_0x030d;
    L_0x02e9:
        r3 = new java.io.File;
        r13 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r15 = new java.lang.StringBuilder;
        r15.<init>();
        r1 = r2.dc_id;
        r15.append(r1);
        r15.append(r14);
        r0 = r2.f441id;
        r15.append(r0);
        r0 = ".temp";
        r15.append(r0);
        r0 = r15.toString();
        r3.<init>(r13, r0);
    L_0x030d:
        r0 = r2.mime_type;
        r0 = r9.equals(r0);
        r4.lottieFile = r0;
        r14 = r2.size;
        r2 = r24;
        r15 = r31;
        r9 = r3;
        goto L_0x033e;
    L_0x031d:
        r31 = r15;
        r0 = r8.webFile;
        if (r0 == 0) goto L_0x032d;
    L_0x0323:
        r0 = new java.io.File;
        r1 = org.telegram.messenger.FileLoader.getDirectory(r17);
        r0.<init>(r1, r11);
        goto L_0x0337;
    L_0x032d:
        r0 = new java.io.File;
        r1 = 0;
        r2 = org.telegram.messenger.FileLoader.getDirectory(r1);
        r0.<init>(r2, r11);
    L_0x0337:
        r2 = r24;
        r15 = r31;
        r9 = r0;
        goto L_0x0245;
    L_0x033e:
        r0 = r10.equals(r6);
        if (r0 == 0) goto L_0x0357;
    L_0x0344:
        r0 = 1;
        r4.animatedFile = r0;
        r4.size = r14;
        r1 = r27;
        r15 = 1;
        goto L_0x035a;
    L_0x034d:
        r11 = r22;
        r7 = r34;
        r12 = r35;
        r24 = r2;
        r31 = r15;
    L_0x0357:
        r0 = 1;
        r1 = r27;
    L_0x035a:
        r4.imageType = r1;
        r3 = r23;
        r4.key = r3;
        r4.filter = r6;
        r4.imageLocation = r8;
        r10 = r36;
        r4.ext = r10;
        r13 = r37;
        r4.currentAccount = r13;
        r14 = r30;
        r4.parentObject = r14;
        r0 = 2;
        if (r7 != r0) goto L_0x038f;
    L_0x0373:
        r0 = new java.io.File;
        r10 = org.telegram.messenger.FileLoader.getInternalCacheDir();
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r11);
        r14 = ".enc.key";
        r13.append(r14);
        r13 = r13.toString();
        r0.<init>(r10, r13);
        r4.encryptionKeyPath = r0;
    L_0x038f:
        r4.addImageReceiver(r5, r3, r6, r1);
        if (r15 != 0) goto L_0x0470;
    L_0x0394:
        if (r2 != 0) goto L_0x0470;
    L_0x0396:
        r0 = r9.exists();
        if (r0 == 0) goto L_0x039e;
    L_0x039c:
        goto L_0x0470;
    L_0x039e:
        r4.url = r11;
        r0 = r20;
        r1 = r0.imageLoadingByUrl;
        r1.put(r11, r4);
        r1 = r8.path;
        if (r1 == 0) goto L_0x03ff;
    L_0x03ab:
        r1 = org.telegram.messenger.Utilities.MD5(r1);
        r2 = org.telegram.messenger.FileLoader.getDirectory(r18);
        r3 = new java.io.File;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r1);
        r1 = "_temp.jpg";
        r5.append(r1);
        r1 = r5.toString();
        r3.<init>(r2, r1);
        r4.tempFilePath = r3;
        r4.finalFilePath = r9;
        r1 = r8.path;
        r2 = r19;
        r1 = r1.startsWith(r2);
        if (r1 == 0) goto L_0x03eb;
    L_0x03d7:
        r1 = new org.telegram.messenger.ImageLoader$ArtworkLoadTask;
        r1.<init>(r4);
        r4.artworkTask = r1;
        r1 = r0.artworkTasks;
        r2 = r4.artworkTask;
        r1.add(r2);
        r1 = 0;
        r0.runArtworkTasks(r1);
        goto L_0x0493;
    L_0x03eb:
        r1 = 0;
        r2 = new org.telegram.messenger.ImageLoader$HttpImageTask;
        r2.<init>(r4, r12);
        r4.httpTask = r2;
        r2 = r0.httpTasks;
        r3 = r4.httpTask;
        r2.add(r3);
        r0.runHttpTasks(r1);
        goto L_0x0493;
    L_0x03ff:
        r1 = r8.location;
        if (r1 == 0) goto L_0x0421;
    L_0x0403:
        if (r7 != 0) goto L_0x040d;
    L_0x0405:
        if (r12 <= 0) goto L_0x040b;
    L_0x0407:
        r1 = r8.key;
        if (r1 == 0) goto L_0x040d;
    L_0x040b:
        r11 = 1;
        goto L_0x040e;
    L_0x040d:
        r11 = r7;
    L_0x040e:
        r6 = org.telegram.messenger.FileLoader.getInstance(r37);
        if (r21 == 0) goto L_0x0416;
    L_0x0414:
        r10 = 2;
        goto L_0x0417;
    L_0x0416:
        r10 = 1;
    L_0x0417:
        r7 = r28;
        r8 = r30;
        r9 = r36;
        r6.loadFile(r7, r8, r9, r10, r11);
        goto L_0x045d;
    L_0x0421:
        r1 = r8.document;
        if (r1 == 0) goto L_0x0438;
    L_0x0425:
        r1 = org.telegram.messenger.FileLoader.getInstance(r37);
        r2 = r8.document;
        if (r21 == 0) goto L_0x0431;
    L_0x042d:
        r3 = r30;
        r6 = 2;
        goto L_0x0434;
    L_0x0431:
        r3 = r30;
        r6 = 1;
    L_0x0434:
        r1.loadFile(r2, r3, r6, r7);
        goto L_0x045d;
    L_0x0438:
        r1 = r8.secureDocument;
        if (r1 == 0) goto L_0x044b;
    L_0x043c:
        r1 = org.telegram.messenger.FileLoader.getInstance(r37);
        r2 = r8.secureDocument;
        if (r21 == 0) goto L_0x0446;
    L_0x0444:
        r3 = 2;
        goto L_0x0447;
    L_0x0446:
        r3 = 1;
    L_0x0447:
        r1.loadFile(r2, r3);
        goto L_0x045d;
    L_0x044b:
        r1 = r8.webFile;
        if (r1 == 0) goto L_0x045d;
    L_0x044f:
        r1 = org.telegram.messenger.FileLoader.getInstance(r37);
        r2 = r8.webFile;
        if (r21 == 0) goto L_0x0459;
    L_0x0457:
        r3 = 2;
        goto L_0x045a;
    L_0x0459:
        r3 = 1;
    L_0x045a:
        r1.loadFile(r2, r3, r7);
    L_0x045d:
        r1 = r25.isForceLoding();
        if (r1 == 0) goto L_0x0493;
    L_0x0463:
        r1 = r0.forceLoadingImages;
        r2 = r4.key;
        r3 = 0;
        r3 = java.lang.Integer.valueOf(r3);
        r1.put(r2, r3);
        goto L_0x0493;
    L_0x0470:
        r0 = r20;
        r4.finalFilePath = r9;
        r4.imageLocation = r8;
        r1 = new org.telegram.messenger.ImageLoader$CacheOutTask;
        r1.<init>(r4);
        r4.cacheTask = r1;
        r1 = r0.imageLoadingByKeys;
        r1.put(r3, r4);
        if (r21 == 0) goto L_0x048c;
    L_0x0484:
        r1 = r0.cacheThumbOutQueue;
        r2 = r4.cacheTask;
        r1.postRunnable(r2);
        goto L_0x0493;
    L_0x048c:
        r1 = r0.cacheOutQueue;
        r2 = r4.cacheTask;
        r1.postRunnable(r2);
    L_0x0493:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.lambda$createLoadOperationForImageReceiver$5$ImageLoader(int, java.lang.String, java.lang.String, int, org.telegram.messenger.ImageReceiver, java.lang.String, int, org.telegram.messenger.ImageLocation, boolean, java.lang.Object, org.telegram.tgnet.TLRPC$Document, boolean, boolean, int, int, java.lang.String, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:133:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0290 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0290 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x023c  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x021e  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0290 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0290 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x027c  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0290 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x029e A:{SYNTHETIC, EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0307  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x02a7  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03eb  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0394  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x029e A:{SYNTHETIC, EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x02a7  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0307  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x030e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x032c A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x035f  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0394  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03eb  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x029e A:{SYNTHETIC, EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0307  */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x02a7  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x030e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x032c A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x035f  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03eb  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0394  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x029e A:{SYNTHETIC, EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  , EDGE_INSN: B:211:0x029e->B:139:0x029e ?: BREAK  } */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x02a7  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0307  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x030e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:164:0x032c A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x035f  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0394  */
    /* JADX WARNING: Removed duplicated region for block: B:197:0x03eb  */
    public void loadImageForImageReceiver(org.telegram.messenger.ImageReceiver r30) {
        /*
        r29 = this;
        r11 = r29;
        r12 = r30;
        if (r12 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r30.getMediaKey();
        r1 = 0;
        r2 = 1;
        if (r0 == 0) goto L_0x003e;
    L_0x000f:
        r3 = r30.getMediaLocation();
        r3 = r3.document;
        r3 = org.telegram.messenger.MessageObject.isAnimatedStickerDocument(r3);
        if (r3 == 0) goto L_0x0024;
    L_0x001b:
        r3 = r11.lottieMemCache;
        r3 = r3.get(r0);
        r3 = (android.graphics.drawable.Drawable) r3;
        goto L_0x002c;
    L_0x0024:
        r3 = r11.memCache;
        r3 = r3.get(r0);
        r3 = (android.graphics.drawable.Drawable) r3;
    L_0x002c:
        if (r3 == 0) goto L_0x003e;
    L_0x002e:
        r11.cancelLoadingForImageReceiver(r12, r2);
        r4 = 3;
        r12.setImageBitmapByKey(r3, r0, r4, r2);
        r3 = r30.isForcePreview();
        if (r3 != 0) goto L_0x003c;
    L_0x003b:
        return;
    L_0x003c:
        r3 = 1;
        goto L_0x003f;
    L_0x003e:
        r3 = 0;
    L_0x003f:
        r4 = r30.getImageKey();
        if (r3 != 0) goto L_0x0079;
    L_0x0045:
        if (r4 == 0) goto L_0x0079;
    L_0x0047:
        r5 = r30.getImageLocation();
        if (r5 == 0) goto L_0x005e;
    L_0x004d:
        r5 = r5.document;
        r5 = org.telegram.messenger.MessageObject.isAnimatedStickerDocument(r5);
        if (r5 == 0) goto L_0x005e;
    L_0x0055:
        r5 = r11.lottieMemCache;
        r5 = r5.get(r4);
        r5 = (android.graphics.drawable.Drawable) r5;
        goto L_0x0066;
    L_0x005e:
        r5 = r11.memCache;
        r5 = r5.get(r4);
        r5 = (android.graphics.drawable.Drawable) r5;
    L_0x0066:
        if (r5 == 0) goto L_0x0079;
    L_0x0068:
        r11.cancelLoadingForImageReceiver(r12, r2);
        r12.setImageBitmapByKey(r5, r4, r1, r2);
        r3 = r30.isForcePreview();
        if (r3 != 0) goto L_0x0077;
    L_0x0074:
        if (r0 != 0) goto L_0x0077;
    L_0x0076:
        return;
    L_0x0077:
        r13 = 1;
        goto L_0x007a;
    L_0x0079:
        r13 = r3;
    L_0x007a:
        r0 = r30.getThumbKey();
        if (r0 == 0) goto L_0x009b;
    L_0x0080:
        r3 = r11.memCache;
        r3 = r3.get(r0);
        r3 = (android.graphics.drawable.BitmapDrawable) r3;
        if (r3 == 0) goto L_0x009b;
    L_0x008a:
        r12.setImageBitmapByKey(r3, r0, r2, r2);
        r11.cancelLoadingForImageReceiver(r12, r1);
        if (r13 == 0) goto L_0x0099;
    L_0x0092:
        r0 = r30.isForcePreview();
        if (r0 == 0) goto L_0x0099;
    L_0x0098:
        return;
    L_0x0099:
        r0 = 1;
        goto L_0x009c;
    L_0x009b:
        r0 = 0;
    L_0x009c:
        r3 = r30.getParentObject();
        r4 = r30.getQulityThumbDocument();
        r5 = r30.getThumbLocation();
        r6 = r30.getThumbFilter();
        r7 = r30.getMediaLocation();
        r14 = r30.getMediaFilter();
        r8 = r30.getImageLocation();
        r15 = r30.getImageFilter();
        if (r8 != 0) goto L_0x00e2;
    L_0x00be:
        r9 = r30.isNeedsQualityThumb();
        if (r9 == 0) goto L_0x00e2;
    L_0x00c4:
        r9 = r30.isCurrentKeyQuality();
        if (r9 == 0) goto L_0x00e2;
    L_0x00ca:
        r9 = r3 instanceof org.telegram.messenger.MessageObject;
        if (r9 == 0) goto L_0x00db;
    L_0x00ce:
        r4 = r3;
        r4 = (org.telegram.messenger.MessageObject) r4;
        r4 = r4.getDocument();
        r8 = org.telegram.messenger.ImageLocation.getForDocument(r4);
    L_0x00d9:
        r4 = 1;
        goto L_0x00e3;
    L_0x00db:
        if (r4 == 0) goto L_0x00e2;
    L_0x00dd:
        r8 = org.telegram.messenger.ImageLocation.getForDocument(r4);
        goto L_0x00d9;
    L_0x00e2:
        r4 = 0;
    L_0x00e3:
        r9 = r30.getExt();
        r10 = "jpg";
        if (r9 != 0) goto L_0x00ec;
    L_0x00eb:
        r9 = r10;
    L_0x00ec:
        r16 = 0;
        r17 = r7;
        r1 = r16;
        r19 = r1;
        r21 = r19;
        r22 = r21;
        r7 = 0;
        r20 = 0;
    L_0x00fb:
        r2 = 2;
        r11 = ".";
        if (r7 >= r2) goto L_0x029e;
    L_0x0100:
        if (r7 != 0) goto L_0x0104;
    L_0x0102:
        r2 = r8;
        goto L_0x0106;
    L_0x0104:
        r2 = r17;
    L_0x0106:
        if (r2 != 0) goto L_0x0109;
    L_0x0108:
        goto L_0x0115;
    L_0x0109:
        if (r17 == 0) goto L_0x010e;
    L_0x010b:
        r12 = r17;
        goto L_0x010f;
    L_0x010e:
        r12 = r8;
    L_0x010f:
        r12 = r2.getKey(r3, r12);
        if (r12 != 0) goto L_0x011e;
    L_0x0115:
        r23 = r13;
        r25 = r14;
        r26 = r15;
        r15 = 1;
        goto L_0x0290;
    L_0x011e:
        r23 = r13;
        r13 = r2.path;
        if (r13 == 0) goto L_0x0143;
    L_0x0124:
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r12);
        r13.append(r11);
        r11 = r2.path;
        r11 = getHttpUrlExtension(r11, r10);
        r13.append(r11);
        r11 = r13.toString();
    L_0x013c:
        r25 = r14;
        r26 = r15;
    L_0x0140:
        r15 = 1;
        goto L_0x0275;
    L_0x0143:
        r13 = r2.photoSize;
        r13 = r13 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r13 == 0) goto L_0x015c;
    L_0x0149:
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r12);
        r13.append(r11);
        r13.append(r9);
        r11 = r13.toString();
        goto L_0x013c;
    L_0x015c:
        r13 = r2.location;
        if (r13 == 0) goto L_0x01a2;
    L_0x0160:
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r12);
        r13.append(r11);
        r13.append(r9);
        r11 = r13.toString();
        r13 = r30.getExt();
        if (r13 != 0) goto L_0x0195;
    L_0x0178:
        r13 = r2.location;
        r24 = r11;
        r11 = r13.key;
        if (r11 != 0) goto L_0x0197;
    L_0x0180:
        r25 = r14;
        r26 = r15;
        r14 = r13.volume_id;
        r27 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r11 = (r14 > r27 ? 1 : (r14 == r27 ? 0 : -1));
        if (r11 != 0) goto L_0x0192;
    L_0x018d:
        r11 = r13.local_id;
        if (r11 >= 0) goto L_0x0192;
    L_0x0191:
        goto L_0x019b;
    L_0x0192:
        r11 = r24;
        goto L_0x0140;
    L_0x0195:
        r24 = r11;
    L_0x0197:
        r25 = r14;
        r26 = r15;
    L_0x019b:
        r11 = r24;
        r15 = 1;
        r20 = 1;
        goto L_0x0275;
    L_0x01a2:
        r25 = r14;
        r26 = r15;
        r13 = r2.webFile;
        if (r13 == 0) goto L_0x01cc;
    L_0x01aa:
        r13 = r13.mime_type;
        r13 = org.telegram.messenger.FileLoader.getMimeTypePart(r13);
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r14.append(r12);
        r14.append(r11);
        r11 = r2.webFile;
        r11 = r11.url;
        r11 = getHttpUrlExtension(r11, r13);
        r14.append(r11);
        r11 = r14.toString();
        goto L_0x0140;
    L_0x01cc:
        r13 = r2.secureDocument;
        if (r13 == 0) goto L_0x01e4;
    L_0x01d0:
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r13.append(r12);
        r13.append(r11);
        r13.append(r9);
        r11 = r13.toString();
        goto L_0x0140;
    L_0x01e4:
        r11 = r2.document;
        if (r11 == 0) goto L_0x0272;
    L_0x01e8:
        if (r7 != 0) goto L_0x01fd;
    L_0x01ea:
        if (r4 == 0) goto L_0x01fd;
    L_0x01ec:
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r13 = "q_";
        r11.append(r13);
        r11.append(r12);
        r12 = r11.toString();
    L_0x01fd:
        r11 = r2.document;
        r11 = org.telegram.messenger.FileLoader.getDocumentFileName(r11);
        r13 = "";
        if (r11 == 0) goto L_0x0216;
    L_0x0207:
        r14 = 46;
        r14 = r11.lastIndexOf(r14);
        r15 = -1;
        if (r14 != r15) goto L_0x0211;
    L_0x0210:
        goto L_0x0216;
    L_0x0211:
        r11 = r11.substring(r14);
        goto L_0x0217;
    L_0x0216:
        r11 = r13;
    L_0x0217:
        r14 = r11.length();
        r15 = 1;
        if (r14 > r15) goto L_0x023c;
    L_0x021e:
        r11 = r2.document;
        r11 = r11.mime_type;
        r14 = "video/mp4";
        r11 = r14.equals(r11);
        if (r11 == 0) goto L_0x022d;
    L_0x022a:
        r13 = ".mp4";
        goto L_0x023d;
    L_0x022d:
        r11 = r2.document;
        r11 = r11.mime_type;
        r14 = "video/x-matroska";
        r11 = r14.equals(r11);
        if (r11 == 0) goto L_0x023d;
    L_0x0239:
        r13 = ".mkv";
        goto L_0x023d;
    L_0x023c:
        r13 = r11;
    L_0x023d:
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r11.append(r12);
        r11.append(r13);
        r11 = r11.toString();
        r13 = r2.document;
        r13 = org.telegram.messenger.MessageObject.isVideoDocument(r13);
        if (r13 != 0) goto L_0x026e;
    L_0x0254:
        r13 = r2.document;
        r13 = org.telegram.messenger.MessageObject.isGifDocument(r13);
        if (r13 != 0) goto L_0x026e;
    L_0x025c:
        r13 = r2.document;
        r13 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r13);
        if (r13 != 0) goto L_0x026e;
    L_0x0264:
        r13 = r2.document;
        r13 = org.telegram.messenger.MessageObject.canPreviewDocument(r13);
        if (r13 != 0) goto L_0x026e;
    L_0x026c:
        r13 = 1;
        goto L_0x026f;
    L_0x026e:
        r13 = 0;
    L_0x026f:
        r20 = r13;
        goto L_0x0275;
    L_0x0272:
        r15 = 1;
        r11 = r16;
    L_0x0275:
        if (r7 != 0) goto L_0x027c;
    L_0x0277:
        r21 = r11;
        r19 = r12;
        goto L_0x027f;
    L_0x027c:
        r22 = r11;
        r1 = r12;
    L_0x027f:
        if (r2 != r5) goto L_0x0290;
    L_0x0281:
        if (r7 != 0) goto L_0x028a;
    L_0x0283:
        r8 = r16;
        r19 = r8;
        r21 = r19;
        goto L_0x0290;
    L_0x028a:
        r1 = r16;
        r17 = r1;
        r22 = r17;
    L_0x0290:
        r7 = r7 + 1;
        r11 = r29;
        r12 = r30;
        r13 = r23;
        r14 = r25;
        r15 = r26;
        goto L_0x00fb;
    L_0x029e:
        r23 = r13;
        r25 = r14;
        r26 = r15;
        r15 = 1;
        if (r5 == 0) goto L_0x0307;
    L_0x02a7:
        r4 = r30.getStrippedLocation();
        if (r4 != 0) goto L_0x02b3;
    L_0x02ad:
        if (r17 == 0) goto L_0x02b2;
    L_0x02af:
        r4 = r17;
        goto L_0x02b3;
    L_0x02b2:
        r4 = r8;
    L_0x02b3:
        r3 = r5.getKey(r3, r4);
        r4 = r5.path;
        if (r4 == 0) goto L_0x02d4;
    L_0x02bb:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r3);
        r4.append(r11);
        r7 = r5.path;
        r7 = getHttpUrlExtension(r7, r10);
        r4.append(r7);
        r4 = r4.toString();
        goto L_0x030a;
    L_0x02d4:
        r4 = r5.photoSize;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photoStrippedSize;
        if (r4 == 0) goto L_0x02ed;
    L_0x02da:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r3);
        r4.append(r11);
        r4.append(r9);
        r4 = r4.toString();
        goto L_0x030a;
    L_0x02ed:
        r4 = r5.location;
        if (r4 == 0) goto L_0x0304;
    L_0x02f1:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r3);
        r4.append(r11);
        r4.append(r9);
        r4 = r4.toString();
        goto L_0x030a;
    L_0x0304:
        r4 = r16;
        goto L_0x030a;
    L_0x0307:
        r3 = r16;
        r4 = r3;
    L_0x030a:
        r7 = "@";
        if (r1 == 0) goto L_0x0325;
    L_0x030e:
        if (r25 == 0) goto L_0x0325;
    L_0x0310:
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10.append(r1);
        r10.append(r7);
        r11 = r25;
        r10.append(r11);
        r1 = r10.toString();
        goto L_0x0327;
    L_0x0325:
        r11 = r25;
    L_0x0327:
        r12 = r1;
        r1 = r19;
        if (r1 == 0) goto L_0x0343;
    L_0x032c:
        if (r26 == 0) goto L_0x0343;
    L_0x032e:
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10.append(r1);
        r10.append(r7);
        r13 = r26;
        r10.append(r13);
        r1 = r10.toString();
        goto L_0x0345;
    L_0x0343:
        r13 = r26;
    L_0x0345:
        r14 = r1;
        if (r3 == 0) goto L_0x035d;
    L_0x0348:
        if (r6 == 0) goto L_0x035d;
    L_0x034a:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r3);
        r1.append(r7);
        r1.append(r6);
        r1 = r1.toString();
        r3 = r1;
    L_0x035d:
        if (r8 == 0) goto L_0x038e;
    L_0x035f:
        r1 = r8.path;
        if (r1 == 0) goto L_0x038e;
    L_0x0363:
        r7 = 0;
        r10 = 1;
        r11 = 1;
        if (r0 == 0) goto L_0x0369;
    L_0x0368:
        r15 = 2;
    L_0x0369:
        r0 = r29;
        r1 = r30;
        r2 = r3;
        r3 = r4;
        r4 = r9;
        r16 = r8;
        r8 = r10;
        r18 = r9;
        r9 = r11;
        r10 = r15;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        r7 = r30.getSize();
        r8 = 1;
        r9 = 0;
        r10 = 0;
        r2 = r14;
        r3 = r21;
        r4 = r18;
        r5 = r16;
        r6 = r13;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x041d;
    L_0x038e:
        r16 = r8;
        r18 = r9;
        if (r17 == 0) goto L_0x03eb;
    L_0x0394:
        r1 = r30.getCacheType();
        r19 = 1;
        if (r1 != 0) goto L_0x03a1;
    L_0x039c:
        if (r20 == 0) goto L_0x03a1;
    L_0x039e:
        r20 = 1;
        goto L_0x03a3;
    L_0x03a1:
        r20 = r1;
    L_0x03a3:
        if (r20 != 0) goto L_0x03a7;
    L_0x03a5:
        r8 = 1;
        goto L_0x03a9;
    L_0x03a7:
        r8 = r20;
    L_0x03a9:
        if (r0 != 0) goto L_0x03bd;
    L_0x03ab:
        r7 = 0;
        r9 = 1;
        if (r0 == 0) goto L_0x03b1;
    L_0x03af:
        r10 = 2;
        goto L_0x03b2;
    L_0x03b1:
        r10 = 1;
    L_0x03b2:
        r0 = r29;
        r1 = r30;
        r2 = r3;
        r3 = r4;
        r4 = r18;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x03bd:
        if (r23 != 0) goto L_0x03d3;
    L_0x03bf:
        r7 = 0;
        r9 = 0;
        r10 = 0;
        r0 = r29;
        r1 = r30;
        r2 = r14;
        r3 = r21;
        r4 = r18;
        r5 = r16;
        r6 = r13;
        r8 = r19;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x03d3:
        r7 = r30.getSize();
        r9 = 3;
        r10 = 0;
        r0 = r29;
        r1 = r30;
        r2 = r12;
        r3 = r22;
        r4 = r18;
        r5 = r17;
        r6 = r11;
        r8 = r20;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        goto L_0x041d;
    L_0x03eb:
        r1 = r30.getCacheType();
        if (r1 != 0) goto L_0x03f5;
    L_0x03f1:
        if (r20 == 0) goto L_0x03f5;
    L_0x03f3:
        r11 = 1;
        goto L_0x03f6;
    L_0x03f5:
        r11 = r1;
    L_0x03f6:
        if (r11 != 0) goto L_0x03fa;
    L_0x03f8:
        r8 = 1;
        goto L_0x03fb;
    L_0x03fa:
        r8 = r11;
    L_0x03fb:
        r7 = 0;
        r9 = 1;
        if (r0 == 0) goto L_0x0401;
    L_0x03ff:
        r10 = 2;
        goto L_0x0402;
    L_0x0401:
        r10 = 1;
    L_0x0402:
        r0 = r29;
        r1 = r30;
        r2 = r3;
        r3 = r4;
        r4 = r18;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        r7 = r30.getSize();
        r9 = 0;
        r10 = 0;
        r2 = r14;
        r3 = r21;
        r5 = r16;
        r6 = r13;
        r8 = r11;
        r0.createLoadOperationForImageReceiver(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x041d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.loadImageForImageReceiver(org.telegram.messenger.ImageReceiver):void");
    }

    private void httpFileLoadError(String str) {
        this.imageLoadQueue.postRunnable(new C0501-$$Lambda$ImageLoader$ZaOfz0BNqcCgsH2qEkswBKN9Vb0(this, str));
    }

    public /* synthetic */ void lambda$httpFileLoadError$6$ImageLoader(String str) {
        CacheImage cacheImage = (CacheImage) this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            HttpImageTask httpImageTask = cacheImage.httpTask;
            cacheImage.httpTask = new HttpImageTask(httpImageTask.cacheImage, httpImageTask.imageSize);
            this.httpTasks.add(cacheImage.httpTask);
            runHttpTasks(false);
        }
    }

    private void artworkLoadError(String str) {
        this.imageLoadQueue.postRunnable(new C0502-$$Lambda$ImageLoader$aEOKyJRqeuDWeruDQhba5rQv8xo(this, str));
    }

    public /* synthetic */ void lambda$artworkLoadError$7$ImageLoader(String str) {
        CacheImage cacheImage = (CacheImage) this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            cacheImage.artworkTask = new ArtworkLoadTask(cacheImage.artworkTask.cacheImage);
            this.artworkTasks.add(cacheImage.artworkTask);
            runArtworkTasks(false);
        }
    }

    private void fileDidLoaded(String str, File file, int i) {
        this.imageLoadQueue.postRunnable(new C0505-$$Lambda$ImageLoader$k74dlCvVooO9jw6g30Qy8tUSaQg(this, str, i, file));
    }

    public /* synthetic */ void lambda$fileDidLoaded$8$ImageLoader(String str, int i, File file) {
        ThumbGenerateInfo thumbGenerateInfo = (ThumbGenerateInfo) this.waitingForQualityThumb.get(str);
        if (!(thumbGenerateInfo == null || thumbGenerateInfo.parentDocument == null)) {
            generateThumb(i, file, thumbGenerateInfo);
            this.waitingForQualityThumb.remove(str);
        }
        CacheImage cacheImage = (CacheImage) this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            this.imageLoadingByUrl.remove(str);
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < cacheImage.imageReceiverArray.size(); i2++) {
                String str2 = (String) cacheImage.keys.get(i2);
                String str3 = (String) cacheImage.filters.get(i2);
                int intValue = ((Integer) cacheImage.imageTypes.get(i2)).intValue();
                ImageReceiver imageReceiver = (ImageReceiver) cacheImage.imageReceiverArray.get(i2);
                CacheImage cacheImage2 = (CacheImage) this.imageLoadingByKeys.get(str2);
                if (cacheImage2 == null) {
                    cacheImage2 = new CacheImage(this, null);
                    cacheImage2.secureDocument = cacheImage.secureDocument;
                    cacheImage2.currentAccount = cacheImage.currentAccount;
                    cacheImage2.finalFilePath = file;
                    cacheImage2.key = str2;
                    cacheImage2.imageLocation = cacheImage.imageLocation;
                    cacheImage2.imageType = intValue;
                    cacheImage2.ext = cacheImage.ext;
                    cacheImage2.encryptionKeyPath = cacheImage.encryptionKeyPath;
                    cacheImage2.cacheTask = new CacheOutTask(cacheImage2);
                    cacheImage2.filter = str3;
                    cacheImage2.animatedFile = cacheImage.animatedFile;
                    cacheImage2.lottieFile = cacheImage.lottieFile;
                    this.imageLoadingByKeys.put(str2, cacheImage2);
                    arrayList.add(cacheImage2.cacheTask);
                }
                cacheImage2.addImageReceiver(imageReceiver, str2, str3, intValue);
            }
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                CacheOutTask cacheOutTask = (CacheOutTask) arrayList.get(i3);
                if (cacheOutTask.cacheImage.imageType == 1) {
                    this.cacheThumbOutQueue.postRunnable(cacheOutTask);
                } else {
                    this.cacheOutQueue.postRunnable(cacheOutTask);
                }
            }
        }
    }

    private void fileDidFailedLoad(String str, int i) {
        if (i != 1) {
            this.imageLoadQueue.postRunnable(new C0506-$$Lambda$ImageLoader$oYsbNqws1vmTTbWlpv4MDvOVi0o(this, str));
        }
    }

    public /* synthetic */ void lambda$fileDidFailedLoad$9$ImageLoader(String str) {
        CacheImage cacheImage = (CacheImage) this.imageLoadingByUrl.get(str);
        if (cacheImage != null) {
            cacheImage.setImageAndClear(null, null);
        }
    }

    private void runHttpTasks(boolean z) {
        if (z) {
            this.currentHttpTasksCount--;
        }
        while (this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
            HttpImageTask httpImageTask = (HttpImageTask) this.httpTasks.poll();
            if (httpImageTask != null) {
                httpImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                this.currentHttpTasksCount++;
            }
        }
    }

    private void runArtworkTasks(boolean z) {
        if (z) {
            this.currentArtworkTasksCount--;
        }
        while (this.currentArtworkTasksCount < 4 && !this.artworkTasks.isEmpty()) {
            try {
                ((ArtworkLoadTask) this.artworkTasks.poll()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
                this.currentArtworkTasksCount++;
            } catch (Throwable unused) {
                runArtworkTasks(false);
            }
        }
    }

    public boolean isLoadingHttpFile(String str) {
        return this.httpFileLoadTasksByKeys.containsKey(str);
    }

    public static String getHttpFileName(String str) {
        return Utilities.MD5(str);
    }

    public static File getHttpFilePath(String str, String str2) {
        str2 = getHttpUrlExtension(str, str2);
        File directory = FileLoader.getDirectory(4);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utilities.MD5(str));
        stringBuilder.append(".");
        stringBuilder.append(str2);
        return new File(directory, stringBuilder.toString());
    }

    public void loadHttpFile(String str, String str2, int i) {
        if (str != null && str.length() != 0 && !this.httpFileLoadTasksByKeys.containsKey(str)) {
            String httpUrlExtension = getHttpUrlExtension(str, str2);
            File directory = FileLoader.getDirectory(4);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Utilities.MD5(str));
            stringBuilder.append("_temp.");
            stringBuilder.append(httpUrlExtension);
            File file = new File(directory, stringBuilder.toString());
            file.delete();
            HttpFileTask httpFileTask = new HttpFileTask(str, file, httpUrlExtension, i);
            this.httpFileLoadTasks.add(httpFileTask);
            this.httpFileLoadTasksByKeys.put(str, httpFileTask);
            runHttpFileLoadTasks(null, 0);
        }
    }

    public void cancelLoadHttpFile(String str) {
        HttpFileTask httpFileTask = (HttpFileTask) this.httpFileLoadTasksByKeys.get(str);
        if (httpFileTask != null) {
            httpFileTask.cancel(true);
            this.httpFileLoadTasksByKeys.remove(str);
            this.httpFileLoadTasks.remove(httpFileTask);
        }
        Runnable runnable = (Runnable) this.retryHttpsTasks.get(str);
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        runHttpFileLoadTasks(null, 0);
    }

    private void runHttpFileLoadTasks(HttpFileTask httpFileTask, int i) {
        AndroidUtilities.runOnUIThread(new C0500-$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g(this, httpFileTask, i));
    }

    public /* synthetic */ void lambda$runHttpFileLoadTasks$11$ImageLoader(HttpFileTask httpFileTask, int i) {
        if (httpFileTask != null) {
            this.currentHttpFileLoadTasksCount--;
        }
        if (httpFileTask != null) {
            if (i == 1) {
                if (httpFileTask.canRetry) {
                    C0495-$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig c0495-$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig = new C0495-$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig(this, new HttpFileTask(httpFileTask.url, httpFileTask.tempFile, httpFileTask.ext, httpFileTask.currentAccount));
                    this.retryHttpsTasks.put(httpFileTask.url, c0495-$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig);
                    AndroidUtilities.runOnUIThread(c0495-$$Lambda$ImageLoader$NIWBUvKKa0U_8_0TQJUIDKfrzig, 1000);
                } else {
                    this.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                    NotificationCenter.getInstance(httpFileTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidFailedLoad, httpFileTask.url, Integer.valueOf(0));
                }
            } else if (i == 2) {
                this.httpFileLoadTasksByKeys.remove(httpFileTask.url);
                File directory = FileLoader.getDirectory(4);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Utilities.MD5(httpFileTask.url));
                stringBuilder.append(".");
                stringBuilder.append(httpFileTask.ext);
                File file = new File(directory, stringBuilder.toString());
                if (!httpFileTask.tempFile.renameTo(file)) {
                    file = httpFileTask.tempFile;
                }
                String file2 = file.toString();
                NotificationCenter.getInstance(httpFileTask.currentAccount).postNotificationName(NotificationCenter.httpFileDidLoad, httpFileTask.url, file2);
            }
        }
        while (this.currentHttpFileLoadTasksCount < 2 && !this.httpFileLoadTasks.isEmpty()) {
            ((HttpFileTask) this.httpFileLoadTasks.poll()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentHttpFileLoadTasksCount++;
        }
    }

    public /* synthetic */ void lambda$null$10$ImageLoader(HttpFileTask httpFileTask) {
        this.httpFileLoadTasks.add(httpFileTask);
        runHttpFileLoadTasks(null, 0);
    }

    public static boolean shouldSendImageAsDocument(String str, Uri uri) {
        Options options = new Options();
        boolean z = true;
        options.inJustDecodeBounds = true;
        if (!(str != null || uri == null || uri.getScheme() == null)) {
            if (uri.getScheme().contains("file")) {
                str = uri.getPath();
            } else {
                try {
                    str = AndroidUtilities.getPath(uri);
                } catch (Throwable th) {
                    FileLog.m30e(th);
                }
            }
        }
        if (str != null) {
            BitmapFactory.decodeFile(str, options);
        } else if (uri != null) {
            try {
                InputStream openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(openInputStream, null, options);
                openInputStream.close();
            } catch (Throwable th2) {
                FileLog.m30e(th2);
                return false;
            }
        }
        float f = (float) options.outWidth;
        float f2 = (float) options.outHeight;
        if (f / f2 <= 10.0f && f2 / f <= 10.0f) {
            z = false;
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:95:0x013c  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00d3 A:{SYNTHETIC, Splitter:B:63:0x00d3} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a1 A:{SYNTHETIC, Splitter:B:46:0x00a1} */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00d3 A:{SYNTHETIC, Splitter:B:63:0x00d3} */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x013c  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x010c A:{SYNTHETIC, Splitter:B:80:0x010c} */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x011e A:{Catch:{ Throwable -> 0x011a }} */
    public static android.graphics.Bitmap loadBitmap(java.lang.String r11, android.net.Uri r12, float r13, float r14, boolean r15) {
        /*
        r0 = new android.graphics.BitmapFactory$Options;
        r0.<init>();
        r1 = 1;
        r0.inJustDecodeBounds = r1;
        if (r11 != 0) goto L_0x002c;
    L_0x000a:
        if (r12 == 0) goto L_0x002c;
    L_0x000c:
        r2 = r12.getScheme();
        if (r2 == 0) goto L_0x002c;
    L_0x0012:
        r2 = r12.getScheme();
        r3 = "file";
        r2 = r2.contains(r3);
        if (r2 == 0) goto L_0x0023;
    L_0x001e:
        r11 = r12.getPath();
        goto L_0x002c;
    L_0x0023:
        r11 = org.telegram.messenger.AndroidUtilities.getPath(r12);	 Catch:{ Throwable -> 0x0028 }
        goto L_0x002c;
    L_0x0028:
        r2 = move-exception;
        org.telegram.messenger.FileLog.m30e(r2);
    L_0x002c:
        r2 = 0;
        if (r11 == 0) goto L_0x0033;
    L_0x002f:
        android.graphics.BitmapFactory.decodeFile(r11, r0);
        goto L_0x0055;
    L_0x0033:
        if (r12 == 0) goto L_0x0055;
    L_0x0035:
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0050 }
        r3 = r3.getContentResolver();	 Catch:{ Throwable -> 0x0050 }
        r3 = r3.openInputStream(r12);	 Catch:{ Throwable -> 0x0050 }
        android.graphics.BitmapFactory.decodeStream(r3, r2, r0);	 Catch:{ Throwable -> 0x0050 }
        r3.close();	 Catch:{ Throwable -> 0x0050 }
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0050 }
        r3 = r3.getContentResolver();	 Catch:{ Throwable -> 0x0050 }
        r3 = r3.openInputStream(r12);	 Catch:{ Throwable -> 0x0050 }
        goto L_0x0056;
    L_0x0050:
        r11 = move-exception;
        org.telegram.messenger.FileLog.m30e(r11);
        return r2;
    L_0x0055:
        r3 = r2;
    L_0x0056:
        r4 = r0.outWidth;
        r4 = (float) r4;
        r5 = r0.outHeight;
        r5 = (float) r5;
        if (r15 == 0) goto L_0x0065;
    L_0x005e:
        r4 = r4 / r13;
        r5 = r5 / r14;
        r13 = java.lang.Math.max(r4, r5);
        goto L_0x006b;
    L_0x0065:
        r4 = r4 / r13;
        r5 = r5 / r14;
        r13 = java.lang.Math.min(r4, r5);
    L_0x006b:
        r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r15 = (r13 > r14 ? 1 : (r13 == r14 ? 0 : -1));
        if (r15 >= 0) goto L_0x0073;
    L_0x0071:
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0073:
        r14 = 0;
        r0.inJustDecodeBounds = r14;
        r13 = (int) r13;
        r0.inSampleSize = r13;
        r13 = r0.inSampleSize;
        r13 = r13 % 2;
        if (r13 == 0) goto L_0x008a;
    L_0x007f:
        r13 = 1;
    L_0x0080:
        r15 = r13 * 2;
        r4 = r0.inSampleSize;
        if (r15 >= r4) goto L_0x0088;
    L_0x0086:
        r13 = r15;
        goto L_0x0080;
    L_0x0088:
        r0.inSampleSize = r13;
    L_0x008a:
        r13 = android.os.Build.VERSION.SDK_INT;
        r15 = 21;
        if (r13 >= r15) goto L_0x0091;
    L_0x0090:
        r14 = 1;
    L_0x0091:
        r0.inPurgeable = r14;
        if (r11 == 0) goto L_0x0097;
    L_0x0095:
        r13 = r11;
        goto L_0x009f;
    L_0x0097:
        if (r12 == 0) goto L_0x009e;
    L_0x0099:
        r13 = org.telegram.messenger.AndroidUtilities.getPath(r12);
        goto L_0x009f;
    L_0x009e:
        r13 = r2;
    L_0x009f:
        if (r13 == 0) goto L_0x00d0;
    L_0x00a1:
        r14 = new androidx.exifinterface.media.ExifInterface;	 Catch:{ Throwable -> 0x00d0 }
        r14.<init>(r13);	 Catch:{ Throwable -> 0x00d0 }
        r13 = "Orientation";
        r13 = r14.getAttributeInt(r13, r1);	 Catch:{ Throwable -> 0x00d0 }
        r14 = new android.graphics.Matrix;	 Catch:{ Throwable -> 0x00d0 }
        r14.<init>();	 Catch:{ Throwable -> 0x00d0 }
        r15 = 3;
        if (r13 == r15) goto L_0x00c8;
    L_0x00b4:
        r15 = 6;
        if (r13 == r15) goto L_0x00c2;
    L_0x00b7:
        r15 = 8;
        if (r13 == r15) goto L_0x00bc;
    L_0x00bb:
        goto L_0x00d1;
    L_0x00bc:
        r13 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r14.postRotate(r13);	 Catch:{ Throwable -> 0x00ce }
        goto L_0x00d1;
    L_0x00c2:
        r13 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r14.postRotate(r13);	 Catch:{ Throwable -> 0x00ce }
        goto L_0x00d1;
    L_0x00c8:
        r13 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r14.postRotate(r13);	 Catch:{ Throwable -> 0x00ce }
        goto L_0x00d1;
        goto L_0x00d1;
    L_0x00d0:
        r14 = r2;
    L_0x00d1:
        if (r11 == 0) goto L_0x013c;
    L_0x00d3:
        r12 = android.graphics.BitmapFactory.decodeFile(r11, r0);	 Catch:{ Throwable -> 0x00fe }
        if (r12 == 0) goto L_0x00fb;
    L_0x00d9:
        r13 = r0.inPurgeable;	 Catch:{ Throwable -> 0x00f9 }
        if (r13 == 0) goto L_0x00e0;
    L_0x00dd:
        org.telegram.messenger.Utilities.pinBitmap(r12);	 Catch:{ Throwable -> 0x00f9 }
    L_0x00e0:
        r5 = 0;
        r6 = 0;
        r7 = r12.getWidth();	 Catch:{ Throwable -> 0x00f9 }
        r8 = r12.getHeight();	 Catch:{ Throwable -> 0x00f9 }
        r10 = 1;
        r4 = r12;
        r9 = r14;
        r13 = org.telegram.messenger.Bitmaps.createBitmap(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x00f9 }
        if (r13 == r12) goto L_0x00fb;
    L_0x00f3:
        r12.recycle();	 Catch:{ Throwable -> 0x00f9 }
        r2 = r13;
        goto L_0x0183;
    L_0x00f9:
        r13 = move-exception;
        goto L_0x0100;
    L_0x00fb:
        r2 = r12;
        goto L_0x0183;
    L_0x00fe:
        r13 = move-exception;
        r12 = r2;
    L_0x0100:
        org.telegram.messenger.FileLog.m30e(r13);
        r13 = getInstance();
        r13.clearMemory();
        if (r12 != 0) goto L_0x011c;
    L_0x010c:
        r12 = android.graphics.BitmapFactory.decodeFile(r11, r0);	 Catch:{ Throwable -> 0x011a }
        if (r12 == 0) goto L_0x011c;
    L_0x0112:
        r11 = r0.inPurgeable;	 Catch:{ Throwable -> 0x011a }
        if (r11 == 0) goto L_0x011c;
    L_0x0116:
        org.telegram.messenger.Utilities.pinBitmap(r12);	 Catch:{ Throwable -> 0x011a }
        goto L_0x011c;
    L_0x011a:
        r11 = move-exception;
        goto L_0x0135;
    L_0x011c:
        if (r12 == 0) goto L_0x0139;
    L_0x011e:
        r5 = 0;
        r6 = 0;
        r7 = r12.getWidth();	 Catch:{ Throwable -> 0x011a }
        r8 = r12.getHeight();	 Catch:{ Throwable -> 0x011a }
        r10 = 1;
        r4 = r12;
        r9 = r14;
        r11 = org.telegram.messenger.Bitmaps.createBitmap(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x011a }
        if (r11 == r12) goto L_0x0139;
    L_0x0131:
        r12.recycle();	 Catch:{ Throwable -> 0x011a }
        goto L_0x013a;
    L_0x0135:
        org.telegram.messenger.FileLog.m30e(r11);
        goto L_0x00fb;
    L_0x0139:
        r11 = r12;
    L_0x013a:
        r2 = r11;
        goto L_0x0183;
    L_0x013c:
        if (r12 == 0) goto L_0x0183;
    L_0x013e:
        r11 = android.graphics.BitmapFactory.decodeStream(r3, r2, r0);	 Catch:{ Throwable -> 0x0172 }
        if (r11 == 0) goto L_0x0166;
    L_0x0144:
        r12 = r0.inPurgeable;	 Catch:{ Throwable -> 0x0163 }
        if (r12 == 0) goto L_0x014b;
    L_0x0148:
        org.telegram.messenger.Utilities.pinBitmap(r11);	 Catch:{ Throwable -> 0x0163 }
    L_0x014b:
        r5 = 0;
        r6 = 0;
        r7 = r11.getWidth();	 Catch:{ Throwable -> 0x0163 }
        r8 = r11.getHeight();	 Catch:{ Throwable -> 0x0163 }
        r10 = 1;
        r4 = r11;
        r9 = r14;
        r12 = org.telegram.messenger.Bitmaps.createBitmap(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x0163 }
        if (r12 == r11) goto L_0x0166;
    L_0x015e:
        r11.recycle();	 Catch:{ Throwable -> 0x0163 }
        r2 = r12;
        goto L_0x0167;
    L_0x0163:
        r12 = move-exception;
        r2 = r11;
        goto L_0x0173;
    L_0x0166:
        r2 = r11;
    L_0x0167:
        r3.close();	 Catch:{ Throwable -> 0x016b }
        goto L_0x0183;
    L_0x016b:
        r11 = move-exception;
        org.telegram.messenger.FileLog.m30e(r11);
        goto L_0x0183;
    L_0x0170:
        r11 = move-exception;
        goto L_0x017a;
    L_0x0172:
        r12 = move-exception;
    L_0x0173:
        org.telegram.messenger.FileLog.m30e(r12);	 Catch:{ all -> 0x0170 }
        r3.close();	 Catch:{ Throwable -> 0x016b }
        goto L_0x0183;
    L_0x017a:
        r3.close();	 Catch:{ Throwable -> 0x017e }
        goto L_0x0182;
    L_0x017e:
        r12 = move-exception;
        org.telegram.messenger.FileLog.m30e(r12);
    L_0x0182:
        throw r11;
    L_0x0183:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.loadBitmap(java.lang.String, android.net.Uri, float, float, boolean):android.graphics.Bitmap");
    }

    public static void fillPhotoSizeWithBytes(PhotoSize photoSize) {
        if (photoSize != null) {
            byte[] bArr = photoSize.bytes;
            if (bArr == null || bArr.length == 0) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(FileLoader.getPathToAttach(photoSize, true), "r");
                    if (((int) randomAccessFile.length()) < 20000) {
                        photoSize.bytes = new byte[((int) randomAccessFile.length())];
                        randomAccessFile.readFully(photoSize.bytes, 0, photoSize.bytes.length);
                    }
                } catch (Throwable th) {
                    FileLog.m30e(th);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00eb  */
    private static org.telegram.tgnet.TLRPC.PhotoSize scaleAndSaveImageInternal(org.telegram.tgnet.TLRPC.PhotoSize r3, android.graphics.Bitmap r4, int r5, int r6, float r7, float r8, float r9, int r10, boolean r11, boolean r12) throws java.lang.Exception {
        /*
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r7 > 0) goto L_0x000b;
    L_0x0006:
        if (r12 == 0) goto L_0x0009;
    L_0x0008:
        goto L_0x000b;
    L_0x0009:
        r5 = r4;
        goto L_0x0010;
    L_0x000b:
        r7 = 1;
        r5 = org.telegram.messenger.Bitmaps.createScaledBitmap(r4, r5, r6, r7);
    L_0x0010:
        r6 = 0;
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r3 == 0) goto L_0x0020;
    L_0x0016:
        r9 = r3.location;
        r12 = r9 instanceof org.telegram.tgnet.TLRPC.TL_fileLocationToBeDeprecated;
        if (r12 != 0) goto L_0x001d;
    L_0x001c:
        goto L_0x0020;
    L_0x001d:
        r9 = (org.telegram.tgnet.TLRPC.TL_fileLocationToBeDeprecated) r9;
        goto L_0x0088;
    L_0x0020:
        r9 = new org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
        r9.<init>();
        r9.volume_id = r7;
        r3 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r9.dc_id = r3;
        r3 = org.telegram.messenger.SharedConfig.getLastLocalId();
        r9.local_id = r3;
        r3 = new byte[r6];
        r9.file_reference = r3;
        r3 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r3.<init>();
        r3.location = r9;
        r12 = r5.getWidth();
        r3.f465w = r12;
        r12 = r5.getHeight();
        r3.f464h = r12;
        r12 = r3.f465w;
        r0 = 100;
        if (r12 > r0) goto L_0x0057;
    L_0x004e:
        r12 = r3.f464h;
        if (r12 > r0) goto L_0x0057;
    L_0x0052:
        r12 = "s";
        r3.type = r12;
        goto L_0x0088;
    L_0x0057:
        r12 = r3.f465w;
        r0 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
        if (r12 > r0) goto L_0x0066;
    L_0x005d:
        r12 = r3.f464h;
        if (r12 > r0) goto L_0x0066;
    L_0x0061:
        r12 = "m";
        r3.type = r12;
        goto L_0x0088;
    L_0x0066:
        r12 = r3.f465w;
        r0 = 800; // 0x320 float:1.121E-42 double:3.953E-321;
        if (r12 > r0) goto L_0x0075;
    L_0x006c:
        r12 = r3.f464h;
        if (r12 > r0) goto L_0x0075;
    L_0x0070:
        r12 = "x";
        r3.type = r12;
        goto L_0x0088;
    L_0x0075:
        r12 = r3.f465w;
        r0 = 1280; // 0x500 float:1.794E-42 double:6.324E-321;
        if (r12 > r0) goto L_0x0084;
    L_0x007b:
        r12 = r3.f464h;
        if (r12 > r0) goto L_0x0084;
    L_0x007f:
        r12 = "y";
        r3.type = r12;
        goto L_0x0088;
    L_0x0084:
        r12 = "w";
        r3.type = r12;
    L_0x0088:
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r0 = r9.volume_id;
        r12.append(r0);
        r0 = "_";
        r12.append(r0);
        r0 = r9.local_id;
        r12.append(r0);
        r0 = ".jpg";
        r12.append(r0);
        r12 = r12.toString();
        r0 = new java.io.File;
        r1 = r9.volume_id;
        r9 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1));
        if (r9 == 0) goto L_0x00ae;
    L_0x00ad:
        goto L_0x00af;
    L_0x00ae:
        r6 = 4;
    L_0x00af:
        r6 = org.telegram.messenger.FileLoader.getDirectory(r6);
        r0.<init>(r6, r12);
        r6 = new java.io.FileOutputStream;
        r6.<init>(r0);
        r7 = android.graphics.Bitmap.CompressFormat.JPEG;
        r5.compress(r7, r10, r6);
        if (r11 == 0) goto L_0x00db;
    L_0x00c2:
        r7 = new java.io.ByteArrayOutputStream;
        r7.<init>();
        r8 = android.graphics.Bitmap.CompressFormat.JPEG;
        r5.compress(r8, r10, r7);
        r8 = r7.toByteArray();
        r3.bytes = r8;
        r8 = r3.bytes;
        r8 = r8.length;
        r3.size = r8;
        r7.close();
        goto L_0x00e6;
    L_0x00db:
        r7 = r6.getChannel();
        r7 = r7.size();
        r8 = (int) r7;
        r3.size = r8;
    L_0x00e6:
        r6.close();
        if (r5 == r4) goto L_0x00ee;
    L_0x00eb:
        r5.recycle();
    L_0x00ee:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.scaleAndSaveImageInternal(org.telegram.tgnet.TLRPC$PhotoSize, android.graphics.Bitmap, int, int, float, float, float, int, boolean, boolean):org.telegram.tgnet.TLRPC$PhotoSize");
    }

    public static PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z) {
        return scaleAndSaveImage(null, bitmap, f, f2, i, z, 0, 0);
    }

    public static PhotoSize scaleAndSaveImage(PhotoSize photoSize, Bitmap bitmap, float f, float f2, int i, boolean z) {
        return scaleAndSaveImage(photoSize, bitmap, f, f2, i, z, 0, 0);
    }

    public static PhotoSize scaleAndSaveImage(Bitmap bitmap, float f, float f2, int i, boolean z, int i2, int i3) {
        return scaleAndSaveImage(null, bitmap, f, f2, i, z, i2, i3);
    }

    public static PhotoSize scaleAndSaveImage(PhotoSize photoSize, Bitmap bitmap, float f, float f2, int i, boolean z, int i2, int i3) {
        int i4;
        int i5;
        int i6 = i2;
        int i7 = i3;
        if (bitmap == null) {
            return null;
        }
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        if (!(width == 0.0f || height == 0.0f)) {
            float f3;
            boolean z2;
            int i8;
            int i9;
            float max = Math.max(width / f, height / f2);
            if (!(i6 == 0 || i7 == 0)) {
                float f4 = (float) i6;
                if (width < f4 || height < ((float) i7)) {
                    if (width >= f4 || height <= ((float) i7)) {
                        if (width > f4) {
                            float f5 = (float) i7;
                            if (height < f5) {
                                f4 = height / f5;
                            }
                        }
                        f4 = Math.max(width / f4, height / ((float) i7));
                    } else {
                        f4 = width / f4;
                    }
                    f3 = f4;
                    z2 = true;
                    i8 = (int) (width / f3);
                    i9 = (int) (height / f3);
                    if (!(i9 == 0 || i8 == 0)) {
                        i4 = i9;
                        i5 = i8;
                        return scaleAndSaveImageInternal(photoSize, bitmap, i8, i9, width, height, f3, i, z, z2);
                    }
                }
            }
            f3 = max;
            z2 = false;
            i8 = (int) (width / f3);
            i9 = (int) (height / f3);
            i4 = i9;
            i5 = i8;
            try {
                return scaleAndSaveImageInternal(photoSize, bitmap, i8, i9, width, height, f3, i, z, z2);
            } catch (Throwable th) {
                FileLog.m30e(th);
            }
        }
        return null;
    }

    public static String getHttpUrlExtension(String str, String str2) {
        String lastPathSegment = Uri.parse(str).getLastPathSegment();
        if (!TextUtils.isEmpty(lastPathSegment) && lastPathSegment.length() > 1) {
            str = lastPathSegment;
        }
        int lastIndexOf = str.lastIndexOf(46);
        str = lastIndexOf != -1 ? str.substring(lastIndexOf + 1) : null;
        return (str == null || str.length() == 0 || str.length() > 4) ? str2 : str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:89:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:89:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:89:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0076  */
    public static void saveMessageThumbs(org.telegram.tgnet.TLRPC.Message r15) {
        /*
        r0 = r15.media;
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        r2 = 0;
        r3 = 0;
        if (r1 == 0) goto L_0x0027;
    L_0x0008:
        r0 = r0.photo;
        r0 = r0.sizes;
        r0 = r0.size();
        r1 = 0;
    L_0x0011:
        if (r1 >= r0) goto L_0x0074;
    L_0x0013:
        r4 = r15.media;
        r4 = r4.photo;
        r4 = r4.sizes;
        r4 = r4.get(r1);
        r4 = (org.telegram.tgnet.TLRPC.PhotoSize) r4;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r5 == 0) goto L_0x0024;
    L_0x0023:
        goto L_0x006f;
    L_0x0024:
        r1 = r1 + 1;
        goto L_0x0011;
    L_0x0027:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r1 == 0) goto L_0x004a;
    L_0x002b:
        r0 = r0.document;
        r0 = r0.thumbs;
        r0 = r0.size();
        r1 = 0;
    L_0x0034:
        if (r1 >= r0) goto L_0x0074;
    L_0x0036:
        r4 = r15.media;
        r4 = r4.document;
        r4 = r4.thumbs;
        r4 = r4.get(r1);
        r4 = (org.telegram.tgnet.TLRPC.PhotoSize) r4;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r5 == 0) goto L_0x0047;
    L_0x0046:
        goto L_0x006f;
    L_0x0047:
        r1 = r1 + 1;
        goto L_0x0034;
    L_0x004a:
        r1 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r1 == 0) goto L_0x0074;
    L_0x004e:
        r0 = r0.webpage;
        r0 = r0.photo;
        if (r0 == 0) goto L_0x0074;
    L_0x0054:
        r0 = r0.sizes;
        r0 = r0.size();
        r1 = 0;
    L_0x005b:
        if (r1 >= r0) goto L_0x0074;
    L_0x005d:
        r4 = r15.media;
        r4 = r4.webpage;
        r4 = r4.photo;
        r4 = r4.sizes;
        r4 = r4.get(r1);
        r4 = (org.telegram.tgnet.TLRPC.PhotoSize) r4;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r5 == 0) goto L_0x0071;
    L_0x006f:
        r3 = r4;
        goto L_0x0074;
    L_0x0071:
        r1 = r1 + 1;
        goto L_0x005b;
    L_0x0074:
        if (r3 == 0) goto L_0x01df;
    L_0x0076:
        r0 = r3.bytes;
        if (r0 == 0) goto L_0x01df;
    L_0x007a:
        r0 = r0.length;
        if (r0 == 0) goto L_0x01df;
    L_0x007d:
        r0 = r3.location;
        if (r0 == 0) goto L_0x0085;
    L_0x0081:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
        if (r0 == 0) goto L_0x0099;
    L_0x0085:
        r0 = new org.telegram.tgnet.TLRPC$TL_fileLocationToBeDeprecated;
        r0.<init>();
        r3.location = r0;
        r0 = r3.location;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0.volume_id = r4;
        r1 = org.telegram.messenger.SharedConfig.getLastLocalId();
        r0.local_id = r1;
    L_0x0099:
        r0 = 1;
        r1 = org.telegram.messenger.FileLoader.getPathToAttach(r3, r0);
        r4 = org.telegram.messenger.MessageObject.shouldEncryptPhotoOrVideo(r15);
        if (r4 == 0) goto L_0x00c0;
    L_0x00a4:
        r4 = new java.io.File;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r1 = r1.getAbsolutePath();
        r5.append(r1);
        r1 = ".enc";
        r5.append(r1);
        r1 = r5.toString();
        r4.<init>(r1);
        r1 = r4;
        goto L_0x00c1;
    L_0x00c0:
        r0 = 0;
    L_0x00c1:
        r4 = r1.exists();
        if (r4 != 0) goto L_0x013d;
    L_0x00c7:
        r4 = "rws";
        if (r0 == 0) goto L_0x012b;
    L_0x00cb:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x0139 }
        r5 = org.telegram.messenger.FileLoader.getInternalCacheDir();	 Catch:{ Exception -> 0x0139 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0139 }
        r6.<init>();	 Catch:{ Exception -> 0x0139 }
        r7 = r1.getName();	 Catch:{ Exception -> 0x0139 }
        r6.append(r7);	 Catch:{ Exception -> 0x0139 }
        r7 = ".key";
        r6.append(r7);	 Catch:{ Exception -> 0x0139 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0139 }
        r0.<init>(r5, r6);	 Catch:{ Exception -> 0x0139 }
        r5 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x0139 }
        r5.<init>(r0, r4);	 Catch:{ Exception -> 0x0139 }
        r6 = r5.length();	 Catch:{ Exception -> 0x0139 }
        r0 = 32;
        r9 = new byte[r0];	 Catch:{ Exception -> 0x0139 }
        r8 = 16;
        r10 = new byte[r8];	 Catch:{ Exception -> 0x0139 }
        r11 = 0;
        r13 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1));
        if (r13 <= 0) goto L_0x010e;
    L_0x0100:
        r13 = 48;
        r6 = r6 % r13;
        r13 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1));
        if (r13 != 0) goto L_0x010e;
    L_0x0107:
        r5.read(r9, r2, r0);	 Catch:{ Exception -> 0x0139 }
        r5.read(r10, r2, r8);	 Catch:{ Exception -> 0x0139 }
        goto L_0x011e;
    L_0x010e:
        r0 = org.telegram.messenger.Utilities.random;	 Catch:{ Exception -> 0x0139 }
        r0.nextBytes(r9);	 Catch:{ Exception -> 0x0139 }
        r0 = org.telegram.messenger.Utilities.random;	 Catch:{ Exception -> 0x0139 }
        r0.nextBytes(r10);	 Catch:{ Exception -> 0x0139 }
        r5.write(r9);	 Catch:{ Exception -> 0x0139 }
        r5.write(r10);	 Catch:{ Exception -> 0x0139 }
    L_0x011e:
        r5.close();	 Catch:{ Exception -> 0x0139 }
        r8 = r3.bytes;	 Catch:{ Exception -> 0x0139 }
        r11 = 0;
        r0 = r3.bytes;	 Catch:{ Exception -> 0x0139 }
        r12 = r0.length;	 Catch:{ Exception -> 0x0139 }
        r13 = 0;
        org.telegram.messenger.Utilities.aesCtrDecryptionByteArray(r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x0139 }
    L_0x012b:
        r0 = new java.io.RandomAccessFile;	 Catch:{ Exception -> 0x0139 }
        r0.<init>(r1, r4);	 Catch:{ Exception -> 0x0139 }
        r1 = r3.bytes;	 Catch:{ Exception -> 0x0139 }
        r0.write(r1);	 Catch:{ Exception -> 0x0139 }
        r0.close();	 Catch:{ Exception -> 0x0139 }
        goto L_0x013d;
    L_0x0139:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x013d:
        r0 = new org.telegram.tgnet.TLRPC$TL_photoSize;
        r0.<init>();
        r1 = r3.f465w;
        r0.f465w = r1;
        r1 = r3.f464h;
        r0.f464h = r1;
        r1 = r3.location;
        r0.location = r1;
        r1 = r3.size;
        r0.size = r1;
        r1 = r3.type;
        r0.type = r1;
        r1 = r15.media;
        r3 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r3 == 0) goto L_0x0183;
    L_0x015c:
        r1 = r1.photo;
        r1 = r1.sizes;
        r1 = r1.size();
    L_0x0164:
        if (r2 >= r1) goto L_0x01df;
    L_0x0166:
        r3 = r15.media;
        r3 = r3.photo;
        r3 = r3.sizes;
        r3 = r3.get(r2);
        r3 = (org.telegram.tgnet.TLRPC.PhotoSize) r3;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r3 == 0) goto L_0x0180;
    L_0x0176:
        r15 = r15.media;
        r15 = r15.photo;
        r15 = r15.sizes;
        r15.set(r2, r0);
        goto L_0x01df;
    L_0x0180:
        r2 = r2 + 1;
        goto L_0x0164;
    L_0x0183:
        r3 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r3 == 0) goto L_0x01ae;
    L_0x0187:
        r1 = r1.document;
        r1 = r1.thumbs;
        r1 = r1.size();
    L_0x018f:
        if (r2 >= r1) goto L_0x01df;
    L_0x0191:
        r3 = r15.media;
        r3 = r3.document;
        r3 = r3.thumbs;
        r3 = r3.get(r2);
        r3 = (org.telegram.tgnet.TLRPC.PhotoSize) r3;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r3 == 0) goto L_0x01ab;
    L_0x01a1:
        r15 = r15.media;
        r15 = r15.document;
        r15 = r15.thumbs;
        r15.set(r2, r0);
        goto L_0x01df;
    L_0x01ab:
        r2 = r2 + 1;
        goto L_0x018f;
    L_0x01ae:
        r3 = r1 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r3 == 0) goto L_0x01df;
    L_0x01b2:
        r1 = r1.webpage;
        r1 = r1.photo;
        r1 = r1.sizes;
        r1 = r1.size();
    L_0x01bc:
        if (r2 >= r1) goto L_0x01df;
    L_0x01be:
        r3 = r15.media;
        r3 = r3.webpage;
        r3 = r3.photo;
        r3 = r3.sizes;
        r3 = r3.get(r2);
        r3 = (org.telegram.tgnet.TLRPC.PhotoSize) r3;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_photoCachedSize;
        if (r3 == 0) goto L_0x01dc;
    L_0x01d0:
        r15 = r15.media;
        r15 = r15.webpage;
        r15 = r15.photo;
        r15 = r15.sizes;
        r15.set(r2, r0);
        goto L_0x01df;
    L_0x01dc:
        r2 = r2 + 1;
        goto L_0x01bc;
    L_0x01df:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.ImageLoader.saveMessageThumbs(org.telegram.tgnet.TLRPC$Message):void");
    }

    public static void saveMessagesThumbs(ArrayList<Message> arrayList) {
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                saveMessageThumbs((Message) arrayList.get(i));
            }
        }
    }
}
