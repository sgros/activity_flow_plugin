// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.media.ThumbnailUtils;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Map;
import java.net.URLConnection;
import java.util.List;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Collection;
import org.telegram.ui.Components.AnimatedFileDrawable;
import java.net.HttpURLConnection;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import java.io.InputStream;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.FileOutputStream;
import android.os.AsyncTask;
import java.util.ArrayList;
import android.text.TextUtils;
import android.net.Uri;
import org.telegram.tgnet.TLObject;
import java.io.RandomAccessFile;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import org.telegram.tgnet.TLRPC;
import android.graphics.Bitmap;
import android.app.ActivityManager;
import java.io.File;
import android.graphics.drawable.BitmapDrawable;
import com.airbnb.lottie.LottieDrawable;
import android.util.SparseArray;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.LinkedList;

public class ImageLoader
{
    public static final String AUTOPLAY_FILTER = "g";
    private static volatile ImageLoader Instance;
    private static ThreadLocal<byte[]> bytesLocal;
    private static ThreadLocal<byte[]> bytesThumbLocal;
    private static byte[] header;
    private static byte[] headerThumb;
    private LinkedList<ArtworkLoadTask> artworkTasks;
    private HashMap<String, Integer> bitmapUseCounts;
    private DispatchQueue cacheOutQueue;
    private DispatchQueue cacheThumbOutQueue;
    private boolean canForce8888;
    private int currentArtworkTasksCount;
    private int currentHttpFileLoadTasksCount;
    private int currentHttpTasksCount;
    private ConcurrentHashMap<String, Float> fileProgresses;
    private HashMap<String, Integer> forceLoadingImages;
    private LinkedList<HttpFileTask> httpFileLoadTasks;
    private HashMap<String, HttpFileTask> httpFileLoadTasksByKeys;
    private LinkedList<HttpImageTask> httpTasks;
    private String ignoreRemoval;
    private DispatchQueue imageLoadQueue;
    private HashMap<String, CacheImage> imageLoadingByKeys;
    private SparseArray<CacheImage> imageLoadingByTag;
    private HashMap<String, CacheImage> imageLoadingByUrl;
    private volatile long lastCacheOutTime;
    private int lastImageNum;
    private long lastProgressUpdateTime;
    private LruCache<LottieDrawable> lottieMemCache;
    private LruCache<BitmapDrawable> memCache;
    private HashMap<String, String> replacedBitmaps;
    private HashMap<String, Runnable> retryHttpsTasks;
    private File telegramPath;
    private ConcurrentHashMap<String, WebFile> testWebFile;
    private HashMap<String, ThumbGenerateTask> thumbGenerateTasks;
    private DispatchQueue thumbGeneratingQueue;
    private HashMap<String, ThumbGenerateInfo> waitingForQualityThumb;
    private SparseArray<String> waitingForQualityThumbByTag;
    
    static {
        ImageLoader.bytesLocal = new ThreadLocal<byte[]>();
        ImageLoader.bytesThumbLocal = new ThreadLocal<byte[]>();
        ImageLoader.header = new byte[12];
        ImageLoader.headerThumb = new byte[12];
        ImageLoader.Instance = null;
    }
    
    public ImageLoader() {
        this.bitmapUseCounts = new HashMap<String, Integer>();
        this.imageLoadingByUrl = new HashMap<String, CacheImage>();
        this.imageLoadingByKeys = new HashMap<String, CacheImage>();
        this.imageLoadingByTag = (SparseArray<CacheImage>)new SparseArray();
        this.waitingForQualityThumb = new HashMap<String, ThumbGenerateInfo>();
        this.waitingForQualityThumbByTag = (SparseArray<String>)new SparseArray();
        this.httpTasks = new LinkedList<HttpImageTask>();
        this.artworkTasks = new LinkedList<ArtworkLoadTask>();
        this.cacheOutQueue = new DispatchQueue("cacheOutQueue");
        this.cacheThumbOutQueue = new DispatchQueue("cacheThumbOutQueue");
        this.thumbGeneratingQueue = new DispatchQueue("thumbGeneratingQueue");
        this.imageLoadQueue = new DispatchQueue("imageLoadQueue");
        this.replacedBitmaps = new HashMap<String, String>();
        this.fileProgresses = new ConcurrentHashMap<String, Float>();
        this.thumbGenerateTasks = new HashMap<String, ThumbGenerateTask>();
        this.forceLoadingImages = new HashMap<String, Integer>();
        final int n = 0;
        this.currentHttpTasksCount = 0;
        this.currentArtworkTasksCount = 0;
        this.testWebFile = new ConcurrentHashMap<String, WebFile>();
        this.httpFileLoadTasks = new LinkedList<HttpFileTask>();
        this.httpFileLoadTasksByKeys = new HashMap<String, HttpFileTask>();
        this.retryHttpsTasks = new HashMap<String, Runnable>();
        this.currentHttpFileLoadTasksCount = 0;
        this.ignoreRemoval = null;
        this.lastCacheOutTime = 0L;
        this.lastImageNum = 0;
        this.lastProgressUpdateTime = 0L;
        this.telegramPath = null;
        final DispatchQueue thumbGeneratingQueue = this.thumbGeneratingQueue;
        boolean canForce8888 = true;
        thumbGeneratingQueue.setPriority(1);
        final int memoryClass = ((ActivityManager)ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass();
        if (memoryClass < 192) {
            canForce8888 = false;
        }
        this.canForce8888 = canForce8888;
        int a;
        if (canForce8888) {
            a = 30;
        }
        else {
            a = 15;
        }
        this.memCache = new LruCache<BitmapDrawable>(Math.min(a, memoryClass / 7) * 1024 * 1024) {
            @Override
            protected void entryRemoved(final boolean b, final String s, final BitmapDrawable bitmapDrawable, final BitmapDrawable bitmapDrawable2) {
                if (ImageLoader.this.ignoreRemoval != null && ImageLoader.this.ignoreRemoval.equals(s)) {
                    return;
                }
                final Integer n = ImageLoader.this.bitmapUseCounts.get(s);
                if (n == null || n == 0) {
                    final Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            }
            
            @Override
            protected int sizeOf(final String s, final BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount();
            }
        };
        this.lottieMemCache = new LruCache<LottieDrawable>(5) {
            @Override
            protected int sizeOf(final String s, final LottieDrawable lottieDrawable) {
                return 1;
            }
        };
        final SparseArray mediaDirs = new SparseArray();
        final File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        try {
            new File(cacheDir, ".nomedia").createNewFile();
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        mediaDirs.put(4, (Object)cacheDir);
        for (int i = n; i < 3; ++i) {
            FileLoader.getInstance(i).setDelegate((FileLoader.FileLoaderDelegate)new FileLoader.FileLoaderDelegate() {
                @Override
                public void fileDidFailedLoad(final String key, final int n) {
                    ImageLoader.this.fileProgresses.remove(key);
                    AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$9ENgl3dEFjSW9fmN2KOTidfJzZ0(this, key, n, i));
                }
                
                @Override
                public void fileDidFailedUpload(final String s, final boolean b) {
                    Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$3$3M_VSd8r5buZPqJNpilxe2zYwos(this, i, s, b));
                }
                
                @Override
                public void fileDidLoaded(final String key, final File file, final int n) {
                    ImageLoader.this.fileProgresses.remove(key);
                    AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$5Bs2fzd8mEyFObZ9x2J38wn4r_c(this, file, key, i, n));
                }
                
                @Override
                public void fileDidUploaded(final String s, final TLRPC.InputFile inputFile, final TLRPC.InputEncryptedFile inputEncryptedFile, final byte[] array, final byte[] array2, final long n) {
                    Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$3$_9uOaZVxDmXjnClpJdro4iTil8Q(this, i, s, inputFile, inputEncryptedFile, array, array2, n));
                }
                
                @Override
                public void fileLoadProgressChanged(final String key, final float f) {
                    ImageLoader.this.fileProgresses.put(key, f);
                    final long currentTimeMillis = System.currentTimeMillis();
                    if (ImageLoader.this.lastProgressUpdateTime == 0L || ImageLoader.this.lastProgressUpdateTime < currentTimeMillis - 500L) {
                        ImageLoader.this.lastProgressUpdateTime = currentTimeMillis;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$iY0R0L0rfhonnXAaHAq36LTxLHA(i, key, f));
                    }
                }
                
                @Override
                public void fileUploadProgressChanged(final String key, final float f, final boolean b) {
                    ImageLoader.this.fileProgresses.put(key, f);
                    final long currentTimeMillis = System.currentTimeMillis();
                    if (ImageLoader.this.lastProgressUpdateTime == 0L || ImageLoader.this.lastProgressUpdateTime < currentTimeMillis - 500L) {
                        ImageLoader.this.lastProgressUpdateTime = currentTimeMillis;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$3$FqjDIba7eslh2iQtqmCAn4WCKjU(i, key, f, b));
                    }
                }
            });
        }
        FileLoader.setMediaDirs((SparseArray<File>)mediaDirs);
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("file system changed");
                }
                final _$$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw $$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw = new _$$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw(this);
                if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent.getAction())) {
                    AndroidUtilities.runOnUIThread($$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw, 1000L);
                }
                else {
                    $$Lambda$ImageLoader$4$C7Gf_cAEPSage_rixfg5JW73rtw.run();
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
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
        while (true) {
            try {
                ApplicationLoader.applicationContext.registerReceiver((BroadcastReceiver)broadcastReceiver, intentFilter);
                this.checkMediaPaths();
            }
            catch (Throwable t) {
                continue;
            }
            break;
        }
    }
    
    private void artworkLoadError(final String s) {
        this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$aEOKyJRqeuDWeruDQhba5rQv8xo(this, s));
    }
    
    private boolean canMoveFiles(File dest, File file, final int n) {
        final RandomAccessFile randomAccessFile = null;
        final RandomAccessFile randomAccessFile2 = null;
        RandomAccessFile randomAccessFile3 = null;
        Label_0347: {
            RandomAccessFile randomAccessFile4 = null;
            Label_0322: {
                if (n == 0) {
                    randomAccessFile3 = randomAccessFile2;
                    try {
                        try {
                            randomAccessFile3 = randomAccessFile2;
                            final File file2 = new File(dest, "000000000_999999_temp.jpg");
                            randomAccessFile3 = randomAccessFile2;
                            randomAccessFile3 = randomAccessFile2;
                            final File file3 = new File(file, "000000000_999999.jpg");
                            file = file2;
                        }
                        finally {}
                    }
                    catch (Exception ex) {
                        randomAccessFile4 = randomAccessFile;
                        break Label_0322;
                    }
                }
                if (n == 3) {
                    final File file4 = new File(dest, "000000000_999999_temp.doc");
                    dest = new File(file, "000000000_999999.doc");
                    file = file4;
                }
                else if (n == 1) {
                    final File file5 = new File(dest, "000000000_999999_temp.ogg");
                    dest = new File(file, "000000000_999999.ogg");
                    file = file5;
                }
                else if (n == 2) {
                    final File file6 = new File(dest, "000000000_999999_temp.mp4");
                    dest = new File(file, "000000000_999999.mp4");
                    file = file6;
                }
                else {
                    dest = (file = null);
                }
                final byte[] b = new byte[1024];
                file.createNewFile();
                final RandomAccessFile randomAccessFile5 = new RandomAccessFile(file, "rws");
                try {
                    randomAccessFile5.write(b);
                    randomAccessFile5.close();
                    final boolean renameTo = file.renameTo(dest);
                    file.delete();
                    dest.delete();
                    if (renameTo) {
                        return true;
                    }
                    return false;
                }
                catch (Exception ex) {
                    randomAccessFile4 = randomAccessFile5;
                }
                finally {
                    randomAccessFile3 = randomAccessFile5;
                    break Label_0347;
                }
            }
            final Exception ex;
            FileLog.e(ex);
            if (randomAccessFile4 != null) {
                try {
                    randomAccessFile4.close();
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            return false;
        }
        if (randomAccessFile3 != null) {
            try {
                randomAccessFile3.close();
            }
            catch (Exception ex3) {
                FileLog.e(ex3);
            }
        }
    }
    
    private void createLoadOperationForImageReceiver(final ImageReceiver imageReceiver, final String s, final String s2, final String s3, final ImageLocation imageLocation, final String s4, final int n, final int n2, final int n3, final int n4) {
        if (imageReceiver != null && s2 != null && s != null) {
            if (imageLocation != null) {
                int tag;
                if ((tag = imageReceiver.getTag(n3)) == 0) {
                    final int lastImageNum = this.lastImageNum;
                    imageReceiver.setTag(lastImageNum, n3);
                    ++this.lastImageNum;
                    tag = lastImageNum;
                    if (this.lastImageNum == Integer.MAX_VALUE) {
                        this.lastImageNum = 0;
                        tag = lastImageNum;
                    }
                }
                this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$bsD7o_FB_o0LApsZQkahjvu_ZzU(this, n4, s2, s, tag, imageReceiver, s4, n3, imageLocation, n3 == 0 && imageReceiver.isCurrentKeyQuality(), imageReceiver.getParentObject(), imageReceiver.getQulityThumbDocument(), imageReceiver.isNeedsQualityThumb(), imageReceiver.isShouldGenerateQualityThumb(), n2, n, s3, imageReceiver.getCurrentAccount()));
            }
        }
    }
    
    private void fileDidFailedLoad(final String s, final int n) {
        if (n == 1) {
            return;
        }
        this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$oYsbNqws1vmTTbWlpv4MDvOVi0o(this, s));
    }
    
    private void fileDidLoaded(final String s, final File file, final int n) {
        this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$k74dlCvVooO9jw6g30Qy8tUSaQg(this, s, n, file));
    }
    
    public static void fillPhotoSizeWithBytes(final TLRPC.PhotoSize photoSize) {
        if (photoSize != null) {
            final byte[] bytes = photoSize.bytes;
            if (bytes == null || bytes.length == 0) {
                final File pathToAttach = FileLoader.getPathToAttach(photoSize, true);
                try {
                    final RandomAccessFile randomAccessFile = new RandomAccessFile(pathToAttach, "r");
                    if ((int)randomAccessFile.length() < 20000) {
                        randomAccessFile.readFully(photoSize.bytes = new byte[(int)randomAccessFile.length()], 0, photoSize.bytes.length);
                    }
                }
                catch (Throwable t) {
                    FileLog.e(t);
                }
            }
        }
    }
    
    private void generateThumb(final int n, final File file, final ThumbGenerateInfo thumbGenerateInfo) {
        if ((n == 0 || n == 2 || n == 3) && file != null) {
            if (thumbGenerateInfo != null) {
                if (this.thumbGenerateTasks.get(FileLoader.getAttachFileName(thumbGenerateInfo.parentDocument)) == null) {
                    this.thumbGeneratingQueue.postRunnable(new ThumbGenerateTask(n, file, thumbGenerateInfo));
                }
            }
        }
    }
    
    public static String getHttpFileName(final String s) {
        return Utilities.MD5(s);
    }
    
    public static File getHttpFilePath(final String s, String httpUrlExtension) {
        httpUrlExtension = getHttpUrlExtension(s, httpUrlExtension);
        final File directory = FileLoader.getDirectory(4);
        final StringBuilder sb = new StringBuilder();
        sb.append(Utilities.MD5(s));
        sb.append(".");
        sb.append(httpUrlExtension);
        return new File(directory, sb.toString());
    }
    
    public static String getHttpUrlExtension(String substring, final String s) {
        final String lastPathSegment = Uri.parse(substring).getLastPathSegment();
        String s2 = substring;
        if (!TextUtils.isEmpty((CharSequence)lastPathSegment)) {
            s2 = substring;
            if (lastPathSegment.length() > 1) {
                s2 = lastPathSegment;
            }
        }
        final int lastIndex = s2.lastIndexOf(46);
        if (lastIndex != -1) {
            substring = s2.substring(lastIndex + 1);
        }
        else {
            substring = null;
        }
        if (substring != null && substring.length() != 0) {
            final String s3 = substring;
            if (substring.length() <= 4) {
                return s3;
            }
        }
        return s;
    }
    
    public static ImageLoader getInstance() {
        final ImageLoader instance;
        if ((instance = ImageLoader.Instance) == null) {
            synchronized (ImageLoader.class) {
                if (ImageLoader.Instance == null) {
                    ImageLoader.Instance = new ImageLoader();
                }
            }
        }
        return instance;
    }
    
    private void httpFileLoadError(final String s) {
        this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ZaOfz0BNqcCgsH2qEkswBKN9Vb0(this, s));
    }
    
    public static Bitmap loadBitmap(final String p0, final Uri p1, final float p2, final float p3, final boolean p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
        //     7: astore          5
        //     9: aload           5
        //    11: iconst_1       
        //    12: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
        //    15: aload_0        
        //    16: astore          6
        //    18: aload_0        
        //    19: ifnonnull       80
        //    22: aload_0        
        //    23: astore          6
        //    25: aload_1        
        //    26: ifnull          80
        //    29: aload_0        
        //    30: astore          6
        //    32: aload_1        
        //    33: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //    36: ifnull          80
        //    39: aload_1        
        //    40: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //    43: ldc_w           "file"
        //    46: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //    49: ifeq            61
        //    52: aload_1        
        //    53: invokevirtual   android/net/Uri.getPath:()Ljava/lang/String;
        //    56: astore          6
        //    58: goto            80
        //    61: aload_1        
        //    62: invokestatic    org/telegram/messenger/AndroidUtilities.getPath:(Landroid/net/Uri;)Ljava/lang/String;
        //    65: astore          6
        //    67: goto            80
        //    70: astore          6
        //    72: aload           6
        //    74: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //    77: aload_0        
        //    78: astore          6
        //    80: aconst_null    
        //    81: astore          7
        //    83: aconst_null    
        //    84: astore          8
        //    86: aload           6
        //    88: ifnull          102
        //    91: aload           6
        //    93: aload           5
        //    95: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //    98: pop            
        //    99: goto            151
        //   102: aload_1        
        //   103: ifnull          151
        //   106: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   109: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   112: aload_1        
        //   113: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //   116: astore_0       
        //   117: aload_0        
        //   118: aconst_null    
        //   119: aload           5
        //   121: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   124: pop            
        //   125: aload_0        
        //   126: invokevirtual   java/io/InputStream.close:()V
        //   129: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   132: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   135: aload_1        
        //   136: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //   139: astore          9
        //   141: goto            154
        //   144: astore_0       
        //   145: aload_0        
        //   146: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   149: aconst_null    
        //   150: areturn        
        //   151: aconst_null    
        //   152: astore          9
        //   154: aload           5
        //   156: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //   159: i2f            
        //   160: fstore          10
        //   162: aload           5
        //   164: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //   167: i2f            
        //   168: fstore          11
        //   170: iload           4
        //   172: ifeq            190
        //   175: fload           10
        //   177: fload_2        
        //   178: fdiv           
        //   179: fload           11
        //   181: fload_3        
        //   182: fdiv           
        //   183: invokestatic    java/lang/Math.max:(FF)F
        //   186: fstore_2       
        //   187: goto            202
        //   190: fload           10
        //   192: fload_2        
        //   193: fdiv           
        //   194: fload           11
        //   196: fload_3        
        //   197: fdiv           
        //   198: invokestatic    java/lang/Math.min:(FF)F
        //   201: fstore_2       
        //   202: fload_2        
        //   203: fstore_3       
        //   204: fload_2        
        //   205: fconst_1       
        //   206: fcmpg          
        //   207: ifge            212
        //   210: fconst_1       
        //   211: fstore_3       
        //   212: iconst_0       
        //   213: istore          4
        //   215: aload           5
        //   217: iconst_0       
        //   218: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
        //   221: aload           5
        //   223: fload_3        
        //   224: f2i            
        //   225: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   228: aload           5
        //   230: getfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   233: iconst_2       
        //   234: irem           
        //   235: ifeq            271
        //   238: iconst_1       
        //   239: istore          12
        //   241: iload           12
        //   243: iconst_2       
        //   244: imul           
        //   245: istore          13
        //   247: iload           13
        //   249: aload           5
        //   251: getfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   254: if_icmpge       264
        //   257: iload           13
        //   259: istore          12
        //   261: goto            241
        //   264: aload           5
        //   266: iload           12
        //   268: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //   271: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   274: bipush          21
        //   276: if_icmpge       282
        //   279: iconst_1       
        //   280: istore          4
        //   282: aload           5
        //   284: iload           4
        //   286: putfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
        //   289: aload           6
        //   291: ifnull          300
        //   294: aload           6
        //   296: astore_0       
        //   297: goto            314
        //   300: aload_1        
        //   301: ifnull          312
        //   304: aload_1        
        //   305: invokestatic    org/telegram/messenger/AndroidUtilities.getPath:(Landroid/net/Uri;)Ljava/lang/String;
        //   308: astore_0       
        //   309: goto            314
        //   312: aconst_null    
        //   313: astore_0       
        //   314: aload_0        
        //   315: ifnull          413
        //   318: new             Landroidx/exifinterface/media/ExifInterface;
        //   321: astore          14
        //   323: aload           14
        //   325: aload_0        
        //   326: invokespecial   androidx/exifinterface/media/ExifInterface.<init>:(Ljava/lang/String;)V
        //   329: aload           14
        //   331: ldc_w           "Orientation"
        //   334: iconst_1       
        //   335: invokevirtual   androidx/exifinterface/media/ExifInterface.getAttributeInt:(Ljava/lang/String;I)I
        //   338: istore          12
        //   340: new             Landroid/graphics/Matrix;
        //   343: astore          14
        //   345: aload           14
        //   347: invokespecial   android/graphics/Matrix.<init>:()V
        //   350: iload           12
        //   352: iconst_3       
        //   353: if_icmpeq       397
        //   356: iload           12
        //   358: bipush          6
        //   360: if_icmpeq       385
        //   363: iload           12
        //   365: bipush          8
        //   367: if_icmpeq       373
        //   370: goto            416
        //   373: aload           14
        //   375: ldc_w           270.0
        //   378: invokevirtual   android/graphics/Matrix.postRotate:(F)Z
        //   381: pop            
        //   382: goto            416
        //   385: aload           14
        //   387: ldc_w           90.0
        //   390: invokevirtual   android/graphics/Matrix.postRotate:(F)Z
        //   393: pop            
        //   394: goto            416
        //   397: aload           14
        //   399: ldc_w           180.0
        //   402: invokevirtual   android/graphics/Matrix.postRotate:(F)Z
        //   405: pop            
        //   406: goto            416
        //   409: astore_0       
        //   410: goto            416
        //   413: aconst_null    
        //   414: astore          14
        //   416: aload           6
        //   418: ifnull          622
        //   421: aload           6
        //   423: aload           5
        //   425: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   428: astore_1       
        //   429: aload_1        
        //   430: astore_0       
        //   431: aload_1        
        //   432: ifnull          495
        //   435: aload           5
        //   437: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
        //   440: ifeq            448
        //   443: aload_1        
        //   444: invokestatic    org/telegram/messenger/Utilities.pinBitmap:(Landroid/graphics/Bitmap;)I
        //   447: pop            
        //   448: aload_1        
        //   449: iconst_0       
        //   450: iconst_0       
        //   451: aload_1        
        //   452: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //   455: aload_1        
        //   456: invokevirtual   android/graphics/Bitmap.getHeight:()I
        //   459: aload           14
        //   461: iconst_1       
        //   462: invokestatic    org/telegram/messenger/Bitmaps.createBitmap:(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
        //   465: astore          9
        //   467: aload_1        
        //   468: astore_0       
        //   469: aload           9
        //   471: aload_1        
        //   472: if_acmpeq       495
        //   475: aload_1        
        //   476: invokevirtual   android/graphics/Bitmap.recycle:()V
        //   479: aload           9
        //   481: astore_0       
        //   482: goto            748
        //   485: astore          9
        //   487: aload_1        
        //   488: astore_0       
        //   489: aload           9
        //   491: astore_1       
        //   492: goto            501
        //   495: goto            748
        //   498: astore_1       
        //   499: aconst_null    
        //   500: astore_0       
        //   501: aload_1        
        //   502: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   505: invokestatic    org/telegram/messenger/ImageLoader.getInstance:()Lorg/telegram/messenger/ImageLoader;
        //   508: invokevirtual   org/telegram/messenger/ImageLoader.clearMemory:()V
        //   511: aload_0        
        //   512: astore_1       
        //   513: aload_0        
        //   514: ifnonnull       567
        //   517: aload           6
        //   519: aload           5
        //   521: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   524: astore          6
        //   526: aload           6
        //   528: astore_1       
        //   529: aload           6
        //   531: ifnull          567
        //   534: aload           6
        //   536: astore_0       
        //   537: aload           6
        //   539: astore_1       
        //   540: aload           5
        //   542: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
        //   545: ifeq            567
        //   548: aload           6
        //   550: astore_0       
        //   551: aload           6
        //   553: invokestatic    org/telegram/messenger/Utilities.pinBitmap:(Landroid/graphics/Bitmap;)I
        //   556: pop            
        //   557: aload           6
        //   559: astore_1       
        //   560: goto            567
        //   563: astore_1       
        //   564: goto            610
        //   567: aload_1        
        //   568: ifnull          617
        //   571: aload_1        
        //   572: astore_0       
        //   573: aload_1        
        //   574: iconst_0       
        //   575: iconst_0       
        //   576: aload_1        
        //   577: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //   580: aload_1        
        //   581: invokevirtual   android/graphics/Bitmap.getHeight:()I
        //   584: aload           14
        //   586: iconst_1       
        //   587: invokestatic    org/telegram/messenger/Bitmaps.createBitmap:(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
        //   590: astore          6
        //   592: aload           6
        //   594: aload_1        
        //   595: if_acmpeq       617
        //   598: aload_1        
        //   599: astore_0       
        //   600: aload_1        
        //   601: invokevirtual   android/graphics/Bitmap.recycle:()V
        //   604: aload           6
        //   606: astore_0       
        //   607: goto            619
        //   610: aload_1        
        //   611: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   614: goto            495
        //   617: aload_1        
        //   618: astore_0       
        //   619: goto            748
        //   622: aload           7
        //   624: astore_0       
        //   625: aload_1        
        //   626: ifnull          748
        //   629: aload           9
        //   631: aconst_null    
        //   632: aload           5
        //   634: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   637: astore_0       
        //   638: aload_0        
        //   639: ifnull          691
        //   642: aload           5
        //   644: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
        //   647: ifeq            655
        //   650: aload_0        
        //   651: invokestatic    org/telegram/messenger/Utilities.pinBitmap:(Landroid/graphics/Bitmap;)I
        //   654: pop            
        //   655: aload_0        
        //   656: iconst_0       
        //   657: iconst_0       
        //   658: aload_0        
        //   659: invokevirtual   android/graphics/Bitmap.getWidth:()I
        //   662: aload_0        
        //   663: invokevirtual   android/graphics/Bitmap.getHeight:()I
        //   666: aload           14
        //   668: iconst_1       
        //   669: invokestatic    org/telegram/messenger/Bitmaps.createBitmap:(Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
        //   672: astore_1       
        //   673: aload_1        
        //   674: aload_0        
        //   675: if_acmpeq       691
        //   678: aload_0        
        //   679: invokevirtual   android/graphics/Bitmap.recycle:()V
        //   682: aload_1        
        //   683: astore_0       
        //   684: goto            691
        //   687: astore_1       
        //   688: goto            719
        //   691: aload_0        
        //   692: astore_1       
        //   693: aload           9
        //   695: invokevirtual   java/io/InputStream.close:()V
        //   698: goto            748
        //   701: astore_0       
        //   702: aload_0        
        //   703: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   706: aload_1        
        //   707: astore_0       
        //   708: goto            748
        //   711: astore_0       
        //   712: goto            733
        //   715: astore_1       
        //   716: aload           8
        //   718: astore_0       
        //   719: aload_1        
        //   720: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   723: aload_0        
        //   724: astore_1       
        //   725: aload           9
        //   727: invokevirtual   java/io/InputStream.close:()V
        //   730: goto            748
        //   733: aload           9
        //   735: invokevirtual   java/io/InputStream.close:()V
        //   738: goto            746
        //   741: astore_1       
        //   742: aload_1        
        //   743: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   746: aload_0        
        //   747: athrow         
        //   748: aload_0        
        //   749: areturn        
        //   750: astore_0       
        //   751: goto            413
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  61     67     70     80     Ljava/lang/Throwable;
        //  106    141    144    151    Ljava/lang/Throwable;
        //  318    350    750    754    Ljava/lang/Throwable;
        //  373    382    409    413    Ljava/lang/Throwable;
        //  385    394    409    413    Ljava/lang/Throwable;
        //  397    406    409    413    Ljava/lang/Throwable;
        //  421    429    498    501    Ljava/lang/Throwable;
        //  435    448    485    495    Ljava/lang/Throwable;
        //  448    467    485    495    Ljava/lang/Throwable;
        //  475    479    485    495    Ljava/lang/Throwable;
        //  517    526    563    567    Ljava/lang/Throwable;
        //  540    548    563    567    Ljava/lang/Throwable;
        //  551    557    563    567    Ljava/lang/Throwable;
        //  573    592    563    567    Ljava/lang/Throwable;
        //  600    604    563    567    Ljava/lang/Throwable;
        //  629    638    715    719    Ljava/lang/Throwable;
        //  629    638    711    748    Any
        //  642    655    687    691    Ljava/lang/Throwable;
        //  642    655    711    748    Any
        //  655    673    687    691    Ljava/lang/Throwable;
        //  655    673    711    748    Any
        //  678    682    687    691    Ljava/lang/Throwable;
        //  678    682    711    748    Any
        //  693    698    701    711    Ljava/lang/Throwable;
        //  719    723    711    748    Any
        //  725    730    701    711    Ljava/lang/Throwable;
        //  733    738    741    746    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0733:
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
    
    private void performReplace(final String s, final String s2) {
        final BitmapDrawable bitmapDrawable = this.memCache.get(s);
        this.replacedBitmaps.put(s, s2);
        if (bitmapDrawable != null) {
            final BitmapDrawable bitmapDrawable2 = this.memCache.get(s2);
            int n2;
            final int n = n2 = 0;
            Label_0120: {
                if (bitmapDrawable2 != null) {
                    n2 = n;
                    if (bitmapDrawable2.getBitmap() != null) {
                        n2 = n;
                        if (bitmapDrawable.getBitmap() != null) {
                            final Bitmap bitmap = bitmapDrawable2.getBitmap();
                            final Bitmap bitmap2 = bitmapDrawable.getBitmap();
                            if (bitmap.getWidth() <= bitmap2.getWidth()) {
                                n2 = n;
                                if (bitmap.getHeight() <= bitmap2.getHeight()) {
                                    break Label_0120;
                                }
                            }
                            n2 = 1;
                        }
                    }
                }
            }
            if (n2 == 0) {
                this.ignoreRemoval = s;
                this.memCache.remove(s);
                this.memCache.put(s2, bitmapDrawable);
                this.ignoreRemoval = null;
            }
            else {
                this.memCache.remove(s);
            }
        }
        final Integer value = this.bitmapUseCounts.get(s);
        if (value != null) {
            this.bitmapUseCounts.put(s2, value);
            this.bitmapUseCounts.remove(s);
        }
    }
    
    private void removeFromWaitingForThumb(final int n, final ImageReceiver o) {
        final String s = (String)this.waitingForQualityThumbByTag.get(n);
        if (s != null) {
            final ThumbGenerateInfo thumbGenerateInfo = this.waitingForQualityThumb.get(s);
            if (thumbGenerateInfo != null) {
                thumbGenerateInfo.imageReceiverArray.remove(o);
                if (thumbGenerateInfo.imageReceiverArray.isEmpty()) {
                    this.waitingForQualityThumb.remove(s);
                }
            }
            this.waitingForQualityThumbByTag.remove(n);
        }
    }
    
    private void replaceImageInCacheInternal(final String str, final String str2, final ImageLocation imageLocation) {
        final ArrayList<String> filterKeys = this.memCache.getFilterKeys(str);
        if (filterKeys != null) {
            for (int i = 0; i < filterKeys.size(); ++i) {
                final String s = filterKeys.get(i);
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("@");
                sb.append(s);
                final String string = sb.toString();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append("@");
                sb2.append(s);
                final String string2 = sb2.toString();
                this.performReplace(string, string2);
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, string, string2, imageLocation);
            }
        }
        else {
            this.performReplace(str, str2);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReplacedPhotoInMemCache, str, str2, imageLocation);
        }
    }
    
    private void runArtworkTasks(final boolean b) {
        if (b) {
            --this.currentArtworkTasksCount;
        }
        while (this.currentArtworkTasksCount < 4 && !this.artworkTasks.isEmpty()) {
            try {
                this.artworkTasks.poll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                ++this.currentArtworkTasksCount;
            }
            catch (Throwable t) {
                this.runArtworkTasks(false);
            }
        }
    }
    
    private void runHttpFileLoadTasks(final HttpFileTask httpFileTask, final int n) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$X9C99kpfS01SJNOymJYOCf_gN1g(this, httpFileTask, n));
    }
    
    private void runHttpTasks(final boolean b) {
        if (b) {
            --this.currentHttpTasksCount;
        }
        while (this.currentHttpTasksCount < 4 && !this.httpTasks.isEmpty()) {
            final HttpImageTask httpImageTask = this.httpTasks.poll();
            if (httpImageTask != null) {
                httpImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                ++this.currentHttpTasksCount;
            }
        }
    }
    
    public static void saveMessageThumbs(final TLRPC.Message message) {
        final TLRPC.MessageMedia media = message.media;
        final boolean b = media instanceof TLRPC.TL_messageMediaPhoto;
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        final TLRPC.PhotoSize photoSize = null;
        TLRPC.PhotoSize photoSize2;
        if (b) {
            final int size = media.photo.sizes.size();
            int index = 0;
            while (true) {
                photoSize2 = photoSize;
                if (index >= size) {
                    break;
                }
                photoSize2 = message.media.photo.sizes.get(index);
                if (photoSize2 instanceof TLRPC.TL_photoCachedSize) {
                    break;
                }
                ++index;
            }
        }
        else if (media instanceof TLRPC.TL_messageMediaDocument) {
            final int size2 = media.document.thumbs.size();
            int index2 = 0;
            while (true) {
                photoSize2 = photoSize;
                if (index2 >= size2) {
                    break;
                }
                photoSize2 = message.media.document.thumbs.get(index2);
                if (photoSize2 instanceof TLRPC.TL_photoCachedSize) {
                    break;
                }
                ++index2;
            }
        }
        else {
            photoSize2 = photoSize;
            if (media instanceof TLRPC.TL_messageMediaWebPage) {
                final TLRPC.Photo photo = media.webpage.photo;
                photoSize2 = photoSize;
                if (photo != null) {
                    final int size3 = photo.sizes.size();
                    int index3 = 0;
                    while (true) {
                        photoSize2 = photoSize;
                        if (index3 >= size3) {
                            break;
                        }
                        photoSize2 = message.media.webpage.photo.sizes.get(index3);
                        if (photoSize2 instanceof TLRPC.TL_photoCachedSize) {
                            break;
                        }
                        ++index3;
                    }
                }
            }
        }
        if (photoSize2 != null) {
            final byte[] bytes = photoSize2.bytes;
            if (bytes != null && bytes.length != 0) {
                final TLRPC.FileLocation location = photoSize2.location;
                if (location == null || location instanceof TLRPC.TL_fileLocationUnavailable) {
                    photoSize2.location = new TLRPC.TL_fileLocationToBeDeprecated();
                    final TLRPC.FileLocation location2 = photoSize2.location;
                    location2.volume_id = -2147483648L;
                    location2.local_id = SharedConfig.getLastLocalId();
                }
                boolean b2 = true;
                File pathToAttach = FileLoader.getPathToAttach(photoSize2, true);
                if (MessageObject.shouldEncryptPhotoOrVideo(message)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(pathToAttach.getAbsolutePath());
                    sb.append(".enc");
                    pathToAttach = new File(sb.toString());
                }
                else {
                    b2 = false;
                }
                if (!pathToAttach.exists()) {
                    Label_0578: {
                        if (!b2) {
                            break Label_0578;
                        }
                        try {
                            final File internalCacheDir = FileLoader.getInternalCacheDir();
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(pathToAttach.getName());
                            sb2.append(".key");
                            final RandomAccessFile randomAccessFile = new RandomAccessFile(new File(internalCacheDir, sb2.toString()), "rws");
                            final long length = randomAccessFile.length();
                            final byte[] b3 = new byte[32];
                            final byte[] b4 = new byte[16];
                            if (length > 0L && length % 48L == 0L) {
                                randomAccessFile.read(b3, 0, 32);
                                randomAccessFile.read(b4, 0, 16);
                            }
                            else {
                                Utilities.random.nextBytes(b3);
                                Utilities.random.nextBytes(b4);
                                randomAccessFile.write(b3);
                                randomAccessFile.write(b4);
                            }
                            randomAccessFile.close();
                            Utilities.aesCtrDecryptionByteArray(photoSize2.bytes, b3, b4, 0, photoSize2.bytes.length, 0);
                            final RandomAccessFile randomAccessFile2 = new RandomAccessFile(pathToAttach, "rws");
                            randomAccessFile2.write(photoSize2.bytes);
                            randomAccessFile2.close();
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                }
                final TLRPC.TL_photoSize element = new TLRPC.TL_photoSize();
                element.w = photoSize2.w;
                element.h = photoSize2.h;
                element.location = photoSize2.location;
                element.size = photoSize2.size;
                element.type = photoSize2.type;
                final TLRPC.MessageMedia media2 = message.media;
                if (media2 instanceof TLRPC.TL_messageMediaPhoto) {
                    for (int size4 = media2.photo.sizes.size(), i = n3; i < size4; ++i) {
                        if (message.media.photo.sizes.get(i) instanceof TLRPC.TL_photoCachedSize) {
                            message.media.photo.sizes.set(i, element);
                            break;
                        }
                    }
                }
                else if (media2 instanceof TLRPC.TL_messageMediaDocument) {
                    for (int size5 = media2.document.thumbs.size(), j = n; j < size5; ++j) {
                        if (message.media.document.thumbs.get(j) instanceof TLRPC.TL_photoCachedSize) {
                            message.media.document.thumbs.set(j, element);
                            break;
                        }
                    }
                }
                else if (media2 instanceof TLRPC.TL_messageMediaWebPage) {
                    for (int size6 = media2.webpage.photo.sizes.size(), k = n2; k < size6; ++k) {
                        if (message.media.webpage.photo.sizes.get(k) instanceof TLRPC.TL_photoCachedSize) {
                            message.media.webpage.photo.sizes.set(k, element);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public static void saveMessagesThumbs(final ArrayList<TLRPC.Message> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    saveMessageThumbs(list.get(i));
                }
            }
        }
    }
    
    public static TLRPC.PhotoSize scaleAndSaveImage(final Bitmap bitmap, final float n, final float n2, final int n3, final boolean b) {
        return scaleAndSaveImage(null, bitmap, n, n2, n3, b, 0, 0);
    }
    
    public static TLRPC.PhotoSize scaleAndSaveImage(final Bitmap bitmap, final float n, final float n2, final int n3, final boolean b, final int n4, final int n5) {
        return scaleAndSaveImage(null, bitmap, n, n2, n3, b, n4, n5);
    }
    
    public static TLRPC.PhotoSize scaleAndSaveImage(final TLRPC.PhotoSize photoSize, final Bitmap bitmap, final float n, final float n2, final int n3, final boolean b) {
        return scaleAndSaveImage(photoSize, bitmap, n, n2, n3, b, 0, 0);
    }
    
    public static TLRPC.PhotoSize scaleAndSaveImage(TLRPC.PhotoSize scaleAndSaveImageInternal, final Bitmap bitmap, float n, float n2, final int n3, final boolean b, int n4, int n5) {
        if (bitmap == null) {
            return null;
        }
        final float n6 = (float)bitmap.getWidth();
        final float n7 = (float)bitmap.getHeight();
        if (n6 != 0.0f) {
            if (n7 != 0.0f) {
                n = Math.max(n6 / n, n7 / n2);
                boolean b2 = false;
                Label_0152: {
                    if (n4 != 0 && n5 != 0) {
                        n2 = (float)n4;
                        if (n6 < n2 || n7 < n5) {
                            Label_0143: {
                                if (n6 < n2 && n7 > n5) {
                                    n = n6 / n2;
                                }
                                else {
                                    if (n6 > n2) {
                                        n = (float)n5;
                                        if (n7 < n) {
                                            n = n7 / n;
                                            break Label_0143;
                                        }
                                    }
                                    n = Math.max(n6 / n2, n7 / n5);
                                }
                            }
                            b2 = true;
                            break Label_0152;
                        }
                    }
                    b2 = false;
                }
                n5 = (int)(n6 / n);
                n4 = (int)(n7 / n);
                if (n4 != 0) {
                    if (n5 != 0) {
                        try {
                            return scaleAndSaveImageInternal(scaleAndSaveImageInternal, bitmap, n5, n4, n6, n7, n, n3, b, b2);
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                            getInstance().clearMemory();
                            System.gc();
                            try {
                                scaleAndSaveImageInternal = scaleAndSaveImageInternal(scaleAndSaveImageInternal, bitmap, n5, n4, n6, n7, n, n3, b, b2);
                                return scaleAndSaveImageInternal;
                            }
                            catch (Throwable t2) {
                                FileLog.e(t2);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private static TLRPC.PhotoSize scaleAndSaveImageInternal(TLRPC.PhotoSize photoSize, final Bitmap bitmap, int n, final int n2, final float n3, final float n4, final float n5, final int n6, final boolean b, final boolean b2) throws Exception {
        Bitmap scaledBitmap;
        if (n5 <= 1.0f && !b2) {
            scaledBitmap = bitmap;
        }
        else {
            scaledBitmap = Bitmaps.createScaledBitmap(bitmap, n, n2, true);
        }
        n = 0;
        TLRPC.TL_fileLocationToBeDeprecated location2 = null;
        Label_0261: {
            if (photoSize != null) {
                final TLRPC.FileLocation location = photoSize.location;
                if (location instanceof TLRPC.TL_fileLocationToBeDeprecated) {
                    location2 = (TLRPC.TL_fileLocationToBeDeprecated)location;
                    break Label_0261;
                }
            }
            location2 = new TLRPC.TL_fileLocationToBeDeprecated();
            location2.volume_id = -2147483648L;
            location2.dc_id = Integer.MIN_VALUE;
            location2.local_id = SharedConfig.getLastLocalId();
            location2.file_reference = new byte[0];
            photoSize = new TLRPC.TL_photoSize();
            photoSize.location = location2;
            photoSize.w = scaledBitmap.getWidth();
            photoSize.h = scaledBitmap.getHeight();
            if (photoSize.w <= 100 && photoSize.h <= 100) {
                photoSize.type = "s";
            }
            else if (photoSize.w <= 320 && photoSize.h <= 320) {
                photoSize.type = "m";
            }
            else if (photoSize.w <= 800 && photoSize.h <= 800) {
                photoSize.type = "x";
            }
            else if (photoSize.w <= 1280 && photoSize.h <= 1280) {
                photoSize.type = "y";
            }
            else {
                photoSize.type = "w";
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(location2.volume_id);
        sb.append("_");
        sb.append(location2.local_id);
        sb.append(".jpg");
        final String string = sb.toString();
        if (location2.volume_id == -2147483648L) {
            n = 4;
        }
        final FileOutputStream fileOutputStream = new FileOutputStream(new File(FileLoader.getDirectory(n), string));
        scaledBitmap.compress(Bitmap$CompressFormat.JPEG, n6, (OutputStream)fileOutputStream);
        if (b) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap$CompressFormat.JPEG, n6, (OutputStream)byteArrayOutputStream);
            photoSize.bytes = byteArrayOutputStream.toByteArray();
            photoSize.size = photoSize.bytes.length;
            byteArrayOutputStream.close();
        }
        else {
            photoSize.size = (int)fileOutputStream.getChannel().size();
        }
        fileOutputStream.close();
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle();
        }
        return photoSize;
    }
    
    public static boolean shouldSendImageAsDocument(final String s, final Uri uri) {
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        final boolean b = true;
        bitmapFactory$Options.inJustDecodeBounds = true;
        String s2 = s;
        if (s == null) {
            s2 = s;
            if (uri != null) {
                s2 = s;
                if (uri.getScheme() != null) {
                    if (uri.getScheme().contains("file")) {
                        s2 = uri.getPath();
                    }
                    else {
                        try {
                            s2 = AndroidUtilities.getPath(uri);
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                            s2 = s;
                        }
                    }
                }
            }
        }
        if (s2 != null) {
            BitmapFactory.decodeFile(s2, bitmapFactory$Options);
        }
        else if (uri != null) {
            try {
                final InputStream openInputStream = ApplicationLoader.applicationContext.getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(openInputStream, (Rect)null, bitmapFactory$Options);
                openInputStream.close();
            }
            catch (Throwable t2) {
                FileLog.e(t2);
                return false;
            }
        }
        final float n = (float)bitmapFactory$Options.outWidth;
        final float n2 = (float)bitmapFactory$Options.outHeight;
        boolean b2 = b;
        if (n / n2 <= 10.0f) {
            b2 = (n2 / n > 10.0f && b);
        }
        return b2;
    }
    
    public void addTestWebFile(final String key, final WebFile value) {
        if (key != null) {
            if (value != null) {
                this.testWebFile.put(key, value);
            }
        }
    }
    
    public void cancelForceLoadingForImageReceiver(final ImageReceiver imageReceiver) {
        if (imageReceiver == null) {
            return;
        }
        final String imageKey = imageReceiver.getImageKey();
        if (imageKey == null) {
            return;
        }
        this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$pOb77_ep1O4qdDkpbIPheznVQgg(this, imageKey));
    }
    
    public void cancelLoadHttpFile(final String key) {
        final HttpFileTask o = this.httpFileLoadTasksByKeys.get(key);
        if (o != null) {
            o.cancel(true);
            this.httpFileLoadTasksByKeys.remove(key);
            this.httpFileLoadTasks.remove(o);
        }
        final Runnable runnable = this.retryHttpsTasks.get(key);
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        this.runHttpFileLoadTasks(null, 0);
    }
    
    public void cancelLoadingForImageReceiver(final ImageReceiver imageReceiver, final boolean b) {
        if (imageReceiver == null) {
            return;
        }
        this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$QQrxxTOTOPgi4Ibzj2dcFh6tMmY(this, b, imageReceiver));
    }
    
    public void checkMediaPaths() {
        this.cacheOutQueue.postRunnable(new _$$Lambda$ImageLoader$TEcsmbVkFIlJCFa_8B6JxwYMU3A(this));
    }
    
    public void clearMemory() {
        this.memCache.evictAll();
        this.lottieMemCache.evictAll();
    }
    
    public SparseArray<File> createMediaPaths() {
        final SparseArray sparseArray = new SparseArray();
        final File cacheDir = AndroidUtilities.getCacheDir();
        if (!cacheDir.isDirectory()) {
            try {
                cacheDir.mkdirs();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        try {
            new File(cacheDir, ".nomedia").createNewFile();
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        sparseArray.put(4, (Object)cacheDir);
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cache path = ");
            sb.append(cacheDir);
            FileLog.d(sb.toString());
        }
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                (this.telegramPath = new File(Environment.getExternalStorageDirectory(), "Telegram")).mkdirs();
                if (this.telegramPath.isDirectory()) {
                    try {
                        final File obj = new File(this.telegramPath, "Telegram Images");
                        obj.mkdir();
                        if (obj.isDirectory() && this.canMoveFiles(cacheDir, obj, 0)) {
                            sparseArray.put(0, (Object)obj);
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("image path = ");
                                sb2.append(obj);
                                FileLog.d(sb2.toString());
                            }
                        }
                    }
                    catch (Exception ex3) {
                        FileLog.e(ex3);
                    }
                    try {
                        final File obj2 = new File(this.telegramPath, "Telegram Video");
                        obj2.mkdir();
                        if (obj2.isDirectory() && this.canMoveFiles(cacheDir, obj2, 2)) {
                            sparseArray.put(2, (Object)obj2);
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("video path = ");
                                sb3.append(obj2);
                                FileLog.d(sb3.toString());
                            }
                        }
                    }
                    catch (Exception ex4) {
                        FileLog.e(ex4);
                    }
                    try {
                        final File file = new File(this.telegramPath, "Telegram Audio");
                        file.mkdir();
                        if (file.isDirectory() && this.canMoveFiles(cacheDir, file, 1)) {
                            new File(file, ".nomedia").createNewFile();
                            sparseArray.put(1, (Object)file);
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("audio path = ");
                                sb4.append(file);
                                FileLog.d(sb4.toString());
                            }
                        }
                    }
                    catch (Exception ex5) {
                        FileLog.e(ex5);
                    }
                    try {
                        final File file2 = new File(this.telegramPath, "Telegram Documents");
                        file2.mkdir();
                        if (file2.isDirectory() && this.canMoveFiles(cacheDir, file2, 3)) {
                            new File(file2, ".nomedia").createNewFile();
                            sparseArray.put(3, (Object)file2);
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("documents path = ");
                                sb5.append(file2);
                                FileLog.d(sb5.toString());
                            }
                        }
                    }
                    catch (Exception ex6) {
                        FileLog.e(ex6);
                    }
                }
            }
            else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("this Android can't rename files");
            }
            SharedConfig.checkSaveToGalleryFiles();
        }
        catch (Exception ex7) {
            FileLog.e(ex7);
        }
        return (SparseArray<File>)sparseArray;
    }
    
    public boolean decrementUseCount(final String key) {
        final Integer n = this.bitmapUseCounts.get(key);
        if (n == null) {
            return true;
        }
        if (n == 1) {
            this.bitmapUseCounts.remove(key);
            return true;
        }
        this.bitmapUseCounts.put(key, n - 1);
        return false;
    }
    
    public BitmapDrawable getAnyImageFromMemory(final String str) {
        final BitmapDrawable bitmapDrawable = this.memCache.get(str);
        if (bitmapDrawable == null) {
            final ArrayList<String> filterKeys = this.memCache.getFilterKeys(str);
            if (filterKeys != null && !filterKeys.isEmpty()) {
                final LruCache<BitmapDrawable> memCache = this.memCache;
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("@");
                sb.append(filterKeys.get(0));
                return memCache.get(sb.toString());
            }
        }
        return bitmapDrawable;
    }
    
    public Float getFileProgress(final String key) {
        if (key == null) {
            return null;
        }
        return this.fileProgresses.get(key);
    }
    
    public BitmapDrawable getImageFromMemory(final TLObject tlObject, String str, final String str2) {
        final String s = null;
        if (tlObject == null && str == null) {
            return null;
        }
        if (str != null) {
            str = Utilities.MD5(str);
        }
        else if (tlObject instanceof TLRPC.FileLocation) {
            final TLRPC.FileLocation fileLocation = (TLRPC.FileLocation)tlObject;
            final StringBuilder sb = new StringBuilder();
            sb.append(fileLocation.volume_id);
            sb.append("_");
            sb.append(fileLocation.local_id);
            str = sb.toString();
        }
        else if (tlObject instanceof TLRPC.Document) {
            final TLRPC.Document document = (TLRPC.Document)tlObject;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(document.dc_id);
            sb2.append("_");
            sb2.append(document.id);
            str = sb2.toString();
        }
        else if (tlObject instanceof SecureDocument) {
            final SecureDocument secureDocument = (SecureDocument)tlObject;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(secureDocument.secureFile.dc_id);
            sb3.append("_");
            sb3.append(secureDocument.secureFile.id);
            str = sb3.toString();
        }
        else {
            str = s;
            if (tlObject instanceof WebFile) {
                str = Utilities.MD5(((WebFile)tlObject).url);
            }
        }
        String string = str;
        if (str2 != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append("@");
            sb4.append(str2);
            string = sb4.toString();
        }
        return this.memCache.get(string);
    }
    
    public String getReplacedKey(final String key) {
        if (key == null) {
            return null;
        }
        return this.replacedBitmaps.get(key);
    }
    
    public void incrementUseCount(final String key) {
        final Integer n = this.bitmapUseCounts.get(key);
        if (n == null) {
            this.bitmapUseCounts.put(key, 1);
        }
        else {
            this.bitmapUseCounts.put(key, n + 1);
        }
    }
    
    public boolean isInCache(final String s) {
        return this.memCache.get(s) != null;
    }
    
    public boolean isLoadingHttpFile(final String key) {
        return this.httpFileLoadTasksByKeys.containsKey(key);
    }
    
    public void loadHttpFile(final String s, String httpUrlExtension, final int n) {
        if (s != null && s.length() != 0) {
            if (!this.httpFileLoadTasksByKeys.containsKey(s)) {
                httpUrlExtension = getHttpUrlExtension(s, httpUrlExtension);
                final File directory = FileLoader.getDirectory(4);
                final StringBuilder sb = new StringBuilder();
                sb.append(Utilities.MD5(s));
                sb.append("_temp.");
                sb.append(httpUrlExtension);
                final File file = new File(directory, sb.toString());
                file.delete();
                final HttpFileTask httpFileTask = new HttpFileTask(s, file, httpUrlExtension, n);
                this.httpFileLoadTasks.add(httpFileTask);
                this.httpFileLoadTasksByKeys.put(s, httpFileTask);
                this.runHttpFileLoadTasks(null, 0);
            }
        }
    }
    
    public void loadImageForImageReceiver(final ImageReceiver imageReceiver) {
        if (imageReceiver == null) {
            return;
        }
        final String mediaKey = imageReceiver.getMediaKey();
        int n = 0;
        Label_0090: {
            if (mediaKey != null) {
                Drawable drawable;
                if (MessageObject.isAnimatedStickerDocument(imageReceiver.getMediaLocation().document)) {
                    drawable = this.lottieMemCache.get(mediaKey);
                }
                else {
                    drawable = (Drawable)this.memCache.get(mediaKey);
                }
                if (drawable != null) {
                    this.cancelLoadingForImageReceiver(imageReceiver, true);
                    imageReceiver.setImageBitmapByKey(drawable, mediaKey, 3, true);
                    if (!imageReceiver.isForcePreview()) {
                        return;
                    }
                    n = 1;
                    break Label_0090;
                }
            }
            n = 0;
        }
        final String imageKey = imageReceiver.getImageKey();
        if (n == 0 && imageKey != null) {
            final ImageLocation imageLocation = imageReceiver.getImageLocation();
            Drawable drawable2;
            if (imageLocation != null && MessageObject.isAnimatedStickerDocument(imageLocation.document)) {
                drawable2 = this.lottieMemCache.get(imageKey);
            }
            else {
                drawable2 = (Drawable)this.memCache.get(imageKey);
            }
            if (drawable2 != null) {
                this.cancelLoadingForImageReceiver(imageReceiver, true);
                imageReceiver.setImageBitmapByKey(drawable2, imageKey, 0, true);
                if (!imageReceiver.isForcePreview() && mediaKey == null) {
                    return;
                }
                n = 1;
            }
        }
        final String thumbKey = imageReceiver.getThumbKey();
        boolean b = false;
        Label_0254: {
            if (thumbKey != null) {
                final BitmapDrawable bitmapDrawable = this.memCache.get(thumbKey);
                if (bitmapDrawable != null) {
                    imageReceiver.setImageBitmapByKey((Drawable)bitmapDrawable, thumbKey, 1, true);
                    this.cancelLoadingForImageReceiver(imageReceiver, false);
                    if (n != 0 && imageReceiver.isForcePreview()) {
                        return;
                    }
                    b = true;
                    break Label_0254;
                }
            }
            b = false;
        }
        final Object parentObject = imageReceiver.getParentObject();
        final TLRPC.Document qulityThumbDocument = imageReceiver.getQulityThumbDocument();
        final ImageLocation thumbLocation = imageReceiver.getThumbLocation();
        final String thumbFilter = imageReceiver.getThumbFilter();
        ImageLocation mediaLocation = imageReceiver.getMediaLocation();
        final String mediaFilter = imageReceiver.getMediaFilter();
        Object o = imageReceiver.getImageLocation();
        final String imageFilter = imageReceiver.getImageFilter();
        boolean b2 = false;
        Label_0362: {
            Label_0359: {
                if (o == null && imageReceiver.isNeedsQualityThumb() && imageReceiver.isCurrentKeyQuality()) {
                    if (parentObject instanceof MessageObject) {
                        o = ImageLocation.getForDocument(((MessageObject)parentObject).getDocument());
                    }
                    else {
                        if (qulityThumbDocument == null) {
                            break Label_0359;
                        }
                        o = ImageLocation.getForDocument(qulityThumbDocument);
                    }
                    b2 = true;
                    break Label_0362;
                }
            }
            b2 = false;
        }
        String ext;
        if ((ext = imageReceiver.getExt()) == null) {
            ext = "jpg";
        }
        String string2;
        String string = string2 = null;
        String s2;
        String s = s2 = string2;
        int i = 0;
        int n2 = 0;
        final int n3 = n;
        while (i < 2) {
            Object o2;
            if (i == 0) {
                o2 = o;
            }
            else {
                o2 = mediaLocation;
            }
            Object o4 = null;
            ImageLocation imageLocation2 = null;
            Label_1190: {
                if (o2 != null) {
                    Object o3;
                    if (mediaLocation != null) {
                        o3 = mediaLocation;
                    }
                    else {
                        o3 = o;
                    }
                    final String key = ((ImageLocation)o2).getKey(parentObject, o3);
                    if (key != null) {
                        String s3 = null;
                        int n4 = 0;
                        String string3 = null;
                        Label_1056: {
                            Label_0522: {
                                if (((ImageLocation)o2).path != null) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(key);
                                    sb.append(".");
                                    sb.append(getHttpUrlExtension(((ImageLocation)o2).path, "jpg"));
                                    s3 = sb.toString();
                                }
                                else if (((ImageLocation)o2).photoSize instanceof TLRPC.TL_photoStrippedSize) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append(key);
                                    sb2.append(".");
                                    sb2.append(ext);
                                    s3 = sb2.toString();
                                }
                                else {
                                    if (((ImageLocation)o2).location != null) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append(key);
                                        sb3.append(".");
                                        sb3.append(ext);
                                        s3 = sb3.toString();
                                        if (imageReceiver.getExt() == null) {
                                            final TLRPC.TL_fileLocationToBeDeprecated location = ((ImageLocation)o2).location;
                                            if (location.key == null) {
                                                if (location.volume_id != -2147483648L || location.local_id >= 0) {
                                                    break Label_0522;
                                                }
                                            }
                                        }
                                        n4 = 1;
                                        string3 = key;
                                        break Label_1056;
                                    }
                                    final WebFile webFile = ((ImageLocation)o2).webFile;
                                    if (webFile != null) {
                                        final String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                                        final StringBuilder sb4 = new StringBuilder();
                                        sb4.append(key);
                                        sb4.append(".");
                                        sb4.append(getHttpUrlExtension(((ImageLocation)o2).webFile.url, mimeTypePart));
                                        s3 = sb4.toString();
                                    }
                                    else if (((ImageLocation)o2).secureDocument != null) {
                                        final StringBuilder sb5 = new StringBuilder();
                                        sb5.append(key);
                                        sb5.append(".");
                                        sb5.append(ext);
                                        s3 = sb5.toString();
                                    }
                                    else {
                                        if (((ImageLocation)o2).document != null) {
                                            string3 = key;
                                            if (i == 0) {
                                                string3 = key;
                                                if (b2) {
                                                    final StringBuilder sb6 = new StringBuilder();
                                                    sb6.append("q_");
                                                    sb6.append(key);
                                                    string3 = sb6.toString();
                                                }
                                            }
                                            final String documentFileName = FileLoader.getDocumentFileName(((ImageLocation)o2).document);
                                            final String s4 = "";
                                            String substring = null;
                                            Label_0901: {
                                                if (documentFileName != null) {
                                                    final int lastIndex = documentFileName.lastIndexOf(46);
                                                    if (lastIndex != -1) {
                                                        substring = documentFileName.substring(lastIndex);
                                                        break Label_0901;
                                                    }
                                                }
                                                substring = "";
                                            }
                                            if (substring.length() <= 1) {
                                                if ("video/mp4".equals(((ImageLocation)o2).document.mime_type)) {
                                                    substring = ".mp4";
                                                }
                                                else {
                                                    substring = s4;
                                                    if ("video/x-matroska".equals(((ImageLocation)o2).document.mime_type)) {
                                                        substring = ".mkv";
                                                    }
                                                }
                                            }
                                            final StringBuilder sb7 = new StringBuilder();
                                            sb7.append(string3);
                                            sb7.append(substring);
                                            s3 = sb7.toString();
                                            if (!MessageObject.isVideoDocument(((ImageLocation)o2).document) && !MessageObject.isGifDocument(((ImageLocation)o2).document) && !MessageObject.isRoundVideoDocument(((ImageLocation)o2).document) && !MessageObject.canPreviewDocument(((ImageLocation)o2).document)) {
                                                n4 = 1;
                                            }
                                            else {
                                                n4 = 0;
                                            }
                                            break Label_1056;
                                        }
                                        s3 = null;
                                        n4 = n2;
                                        string3 = key;
                                        break Label_1056;
                                    }
                                }
                            }
                            string3 = key;
                            n4 = n2;
                        }
                        String s5;
                        String s6;
                        String s7;
                        String s8;
                        if (i == 0) {
                            s5 = s3;
                            s6 = string;
                            s7 = string3;
                            s8 = s2;
                        }
                        else {
                            final String s9 = s3;
                            s6 = string3;
                            s8 = s9;
                            s5 = s;
                            s7 = string2;
                        }
                        string = s6;
                        o4 = o;
                        imageLocation2 = mediaLocation;
                        string2 = s7;
                        n2 = n4;
                        s = s5;
                        s2 = s8;
                        if (o2 != thumbLocation) {
                            break Label_1190;
                        }
                        if (i == 0) {
                            o4 = null;
                            string2 = (s = (String)o4);
                            string = s6;
                            imageLocation2 = mediaLocation;
                            n2 = n4;
                            s2 = s8;
                            break Label_1190;
                        }
                        string = null;
                        final String s10 = s2 = string;
                        s = s5;
                        n2 = n4;
                        string2 = s7;
                        imageLocation2 = (ImageLocation)s10;
                        o4 = o;
                        break Label_1190;
                    }
                }
                o4 = o;
                imageLocation2 = mediaLocation;
            }
            ++i;
            o = o4;
            mediaLocation = imageLocation2;
        }
        int n5 = 1;
        String key2;
        String s11;
        if (thumbLocation != null) {
            Object strippedLocation;
            if ((strippedLocation = imageReceiver.getStrippedLocation()) == null) {
                if (mediaLocation != null) {
                    strippedLocation = mediaLocation;
                }
                else {
                    strippedLocation = o;
                }
            }
            key2 = thumbLocation.getKey(parentObject, strippedLocation);
            if (thumbLocation.path != null) {
                final StringBuilder sb8 = new StringBuilder();
                sb8.append(key2);
                sb8.append(".");
                sb8.append(getHttpUrlExtension(thumbLocation.path, "jpg"));
                s11 = sb8.toString();
            }
            else if (thumbLocation.photoSize instanceof TLRPC.TL_photoStrippedSize) {
                final StringBuilder sb9 = new StringBuilder();
                sb9.append(key2);
                sb9.append(".");
                sb9.append(ext);
                s11 = sb9.toString();
            }
            else if (thumbLocation.location != null) {
                final StringBuilder sb10 = new StringBuilder();
                sb10.append(key2);
                sb10.append(".");
                sb10.append(ext);
                s11 = sb10.toString();
            }
            else {
                s11 = null;
            }
        }
        else {
            key2 = (s11 = null);
        }
        if (string != null && mediaFilter != null) {
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(string);
            sb11.append("@");
            sb11.append(mediaFilter);
            string = sb11.toString();
        }
        if (string2 != null && imageFilter != null) {
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(string2);
            sb12.append("@");
            sb12.append(imageFilter);
            string2 = sb12.toString();
        }
        final String s12 = imageFilter;
        String string4;
        if ((string4 = key2) != null) {
            string4 = key2;
            if (thumbFilter != null) {
                final StringBuilder sb13 = new StringBuilder();
                sb13.append(key2);
                sb13.append("@");
                sb13.append(thumbFilter);
                string4 = sb13.toString();
            }
        }
        if (o != null && ((ImageLocation)o).path != null) {
            if (b) {
                n5 = 2;
            }
            this.createLoadOperationForImageReceiver(imageReceiver, string4, s11, ext, thumbLocation, thumbFilter, 0, 1, 1, n5);
            this.createLoadOperationForImageReceiver(imageReceiver, string2, s, ext, (ImageLocation)o, s12, imageReceiver.getSize(), 1, 0, 0);
        }
        else if (mediaLocation != null) {
            int cacheType = imageReceiver.getCacheType();
            if (cacheType == 0 && n2 != 0) {
                cacheType = 1;
            }
            int n6;
            if (cacheType == 0) {
                n6 = 1;
            }
            else {
                n6 = cacheType;
            }
            if (!b) {
                int n7;
                if (b) {
                    n7 = 2;
                }
                else {
                    n7 = 1;
                }
                this.createLoadOperationForImageReceiver(imageReceiver, string4, s11, ext, thumbLocation, thumbFilter, 0, n6, 1, n7);
            }
            if (n3 == 0) {
                this.createLoadOperationForImageReceiver(imageReceiver, string2, s, ext, (ImageLocation)o, s12, 0, 1, 0, 0);
            }
            this.createLoadOperationForImageReceiver(imageReceiver, string, s2, ext, mediaLocation, mediaFilter, imageReceiver.getSize(), cacheType, 3, 0);
        }
        else {
            int cacheType2 = imageReceiver.getCacheType();
            if (cacheType2 == 0 && n2 != 0) {
                cacheType2 = 1;
            }
            int n8;
            if (cacheType2 == 0) {
                n8 = 1;
            }
            else {
                n8 = cacheType2;
            }
            int n9;
            if (b) {
                n9 = 2;
            }
            else {
                n9 = 1;
            }
            this.createLoadOperationForImageReceiver(imageReceiver, string4, s11, ext, thumbLocation, thumbFilter, 0, n8, 1, n9);
            this.createLoadOperationForImageReceiver(imageReceiver, string2, s, ext, (ImageLocation)o, s12, imageReceiver.getSize(), cacheType2, 0, 0);
        }
    }
    
    public void putImageToCache(final BitmapDrawable bitmapDrawable, final String s) {
        this.memCache.put(s, bitmapDrawable);
    }
    
    public void removeImage(final String key) {
        this.bitmapUseCounts.remove(key);
        this.memCache.remove(key);
    }
    
    public void removeTestWebFile(final String key) {
        if (key == null) {
            return;
        }
        this.testWebFile.remove(key);
    }
    
    public void replaceImageInCache(final String s, final String s2, final ImageLocation imageLocation, final boolean b) {
        if (b) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$goqDHHQdnb5snOP60neGaS99rrI(this, s, s2, imageLocation));
        }
        else {
            this.replaceImageInCacheInternal(s, s2, imageLocation);
        }
    }
    
    private class ArtworkLoadTask extends AsyncTask<Void, Void, String>
    {
        private CacheImage cacheImage;
        private boolean canRetry;
        private HttpURLConnection httpConnection;
        private boolean small;
        
        public ArtworkLoadTask(final CacheImage cacheImage) {
            boolean small = true;
            this.canRetry = true;
            this.cacheImage = cacheImage;
            if (Uri.parse(cacheImage.imageLocation.path).getQueryParameter("s") == null) {
                small = false;
            }
            this.small = small;
        }
        
        protected String doInBackground(final Void... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: astore_2       
            //     2: aload_0        
            //     3: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //     6: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //     9: getfield        org/telegram/messenger/ImageLocation.path:Ljava/lang/String;
            //    12: astore_1       
            //    13: new             Ljava/net/URL;
            //    16: astore_3       
            //    17: aload_3        
            //    18: aload_1        
            //    19: ldc             "athumb://"
            //    21: ldc             "https://"
            //    23: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
            //    26: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
            //    29: aload_0        
            //    30: aload_3        
            //    31: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
            //    34: checkcast       Ljava/net/HttpURLConnection;
            //    37: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    40: aload_0        
            //    41: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    44: ldc             "User-Agent"
            //    46: ldc             "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1"
            //    48: invokevirtual   java/net/HttpURLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
            //    51: aload_0        
            //    52: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    55: sipush          5000
            //    58: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
            //    61: aload_0        
            //    62: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    65: sipush          5000
            //    68: invokevirtual   java/net/HttpURLConnection.setReadTimeout:(I)V
            //    71: aload_0        
            //    72: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    75: invokevirtual   java/net/HttpURLConnection.connect:()V
            //    78: aload_0        
            //    79: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    82: ifnull          131
            //    85: aload_0        
            //    86: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //    89: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
            //    92: istore          4
            //    94: iload           4
            //    96: sipush          200
            //    99: if_icmpeq       131
            //   102: iload           4
            //   104: sipush          202
            //   107: if_icmpeq       131
            //   110: iload           4
            //   112: sipush          304
            //   115: if_icmpeq       131
            //   118: aload_0        
            //   119: iconst_0       
            //   120: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.canRetry:Z
            //   123: goto            131
            //   126: astore_1       
            //   127: aload_1        
            //   128: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   131: aload_0        
            //   132: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   135: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
            //   138: astore_1       
            //   139: new             Ljava/io/ByteArrayOutputStream;
            //   142: astore_3       
            //   143: aload_3        
            //   144: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
            //   147: ldc             32768
            //   149: newarray        B
            //   151: astore_2       
            //   152: aload_0        
            //   153: invokevirtual   android/os/AsyncTask.isCancelled:()Z
            //   156: ifeq            162
            //   159: goto            185
            //   162: aload_1        
            //   163: aload_2        
            //   164: invokevirtual   java/io/InputStream.read:([B)I
            //   167: istore          4
            //   169: iload           4
            //   171: ifle            185
            //   174: aload_3        
            //   175: aload_2        
            //   176: iconst_0       
            //   177: iload           4
            //   179: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
            //   182: goto            152
            //   185: aload_0        
            //   186: iconst_0       
            //   187: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.canRetry:Z
            //   190: new             Lorg/json/JSONObject;
            //   193: astore_2       
            //   194: new             Ljava/lang/String;
            //   197: astore          5
            //   199: aload           5
            //   201: aload_3        
            //   202: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
            //   205: invokespecial   java/lang/String.<init>:([B)V
            //   208: aload_2        
            //   209: aload           5
            //   211: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
            //   214: aload_2        
            //   215: ldc             "results"
            //   217: invokevirtual   org/json/JSONObject.getJSONArray:(Ljava/lang/String;)Lorg/json/JSONArray;
            //   220: astore_2       
            //   221: aload_2        
            //   222: invokevirtual   org/json/JSONArray.length:()I
            //   225: ifle            331
            //   228: aload_2        
            //   229: iconst_0       
            //   230: invokevirtual   org/json/JSONArray.getJSONObject:(I)Lorg/json/JSONObject;
            //   233: ldc             "artworkUrl100"
            //   235: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
            //   238: astore_2       
            //   239: aload_0        
            //   240: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.small:Z
            //   243: istore          6
            //   245: iload           6
            //   247: ifeq            286
            //   250: aload_0        
            //   251: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   254: ifnull          264
            //   257: aload_0        
            //   258: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   261: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   264: aload_1        
            //   265: ifnull          280
            //   268: aload_1        
            //   269: invokevirtual   java/io/InputStream.close:()V
            //   272: goto            280
            //   275: astore_1       
            //   276: aload_1        
            //   277: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   280: aload_3        
            //   281: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   284: aload_2        
            //   285: areturn        
            //   286: aload_2        
            //   287: ldc             "100x100"
            //   289: ldc             "600x600"
            //   291: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
            //   294: astore_2       
            //   295: aload_0        
            //   296: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   299: ifnull          309
            //   302: aload_0        
            //   303: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   306: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   309: aload_1        
            //   310: ifnull          325
            //   313: aload_1        
            //   314: invokevirtual   java/io/InputStream.close:()V
            //   317: goto            325
            //   320: astore_1       
            //   321: aload_1        
            //   322: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   325: aload_3        
            //   326: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   329: aload_2        
            //   330: areturn        
            //   331: aload_0        
            //   332: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   335: ifnull          345
            //   338: aload_0        
            //   339: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   342: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   345: aload_3        
            //   346: astore_2       
            //   347: aload_1        
            //   348: ifnull          367
            //   351: aload_1        
            //   352: invokevirtual   java/io/InputStream.close:()V
            //   355: aload_3        
            //   356: astore_2       
            //   357: goto            367
            //   360: astore_1       
            //   361: aload_1        
            //   362: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   365: aload_3        
            //   366: astore_2       
            //   367: aload_2        
            //   368: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   371: goto            548
            //   374: astore_2       
            //   375: goto            393
            //   378: astore_2       
            //   379: aload_1        
            //   380: astore          5
            //   382: aload_3        
            //   383: astore_1       
            //   384: aload           5
            //   386: astore_3       
            //   387: goto            423
            //   390: astore_2       
            //   391: aconst_null    
            //   392: astore_3       
            //   393: aload_1        
            //   394: astore          5
            //   396: aload_2        
            //   397: astore_1       
            //   398: aload           5
            //   400: astore_2       
            //   401: goto            559
            //   404: astore_2       
            //   405: aload_1        
            //   406: astore_3       
            //   407: aconst_null    
            //   408: astore_1       
            //   409: goto            423
            //   412: astore_1       
            //   413: aconst_null    
            //   414: astore_3       
            //   415: goto            559
            //   418: astore_2       
            //   419: aconst_null    
            //   420: astore_1       
            //   421: aload_1        
            //   422: astore_3       
            //   423: aload_2        
            //   424: instanceof      Ljava/net/SocketTimeoutException;
            //   427: ifeq            444
            //   430: invokestatic    org/telegram/messenger/ApplicationLoader.isNetworkOnline:()Z
            //   433: ifeq            505
            //   436: aload_0        
            //   437: iconst_0       
            //   438: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.canRetry:Z
            //   441: goto            505
            //   444: aload_2        
            //   445: instanceof      Ljava/net/UnknownHostException;
            //   448: ifeq            459
            //   451: aload_0        
            //   452: iconst_0       
            //   453: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.canRetry:Z
            //   456: goto            505
            //   459: aload_2        
            //   460: instanceof      Ljava/net/SocketException;
            //   463: ifeq            493
            //   466: aload_2        
            //   467: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
            //   470: ifnull          505
            //   473: aload_2        
            //   474: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
            //   477: ldc             "ECONNRESET"
            //   479: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //   482: ifeq            505
            //   485: aload_0        
            //   486: iconst_0       
            //   487: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.canRetry:Z
            //   490: goto            505
            //   493: aload_2        
            //   494: instanceof      Ljava/io/FileNotFoundException;
            //   497: ifeq            505
            //   500: aload_0        
            //   501: iconst_0       
            //   502: putfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.canRetry:Z
            //   505: aload_2        
            //   506: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   509: aload_0        
            //   510: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   513: ifnull          523
            //   516: aload_0        
            //   517: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   520: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   523: aload_3        
            //   524: ifnull          539
            //   527: aload_3        
            //   528: invokevirtual   java/io/InputStream.close:()V
            //   531: goto            539
            //   534: astore_3       
            //   535: aload_3        
            //   536: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   539: aload_1        
            //   540: ifnull          548
            //   543: aload_1        
            //   544: astore_2       
            //   545: goto            367
            //   548: aconst_null    
            //   549: areturn        
            //   550: astore          5
            //   552: aload_3        
            //   553: astore_2       
            //   554: aload_1        
            //   555: astore_3       
            //   556: aload           5
            //   558: astore_1       
            //   559: aload_0        
            //   560: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   563: ifnull          573
            //   566: aload_0        
            //   567: getfield        org/telegram/messenger/ImageLoader$ArtworkLoadTask.httpConnection:Ljava/net/HttpURLConnection;
            //   570: invokevirtual   java/net/HttpURLConnection.disconnect:()V
            //   573: aload_2        
            //   574: ifnull          589
            //   577: aload_2        
            //   578: invokevirtual   java/io/InputStream.close:()V
            //   581: goto            589
            //   584: astore_2       
            //   585: aload_2        
            //   586: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   589: aload_3        
            //   590: ifnull          597
            //   593: aload_3        
            //   594: invokevirtual   java/io/ByteArrayOutputStream.close:()V
            //   597: aload_1        
            //   598: athrow         
            //   599: astore          5
            //   601: goto            264
            //   604: astore_1       
            //   605: goto            284
            //   608: astore          5
            //   610: goto            309
            //   613: astore_1       
            //   614: goto            329
            //   617: astore_2       
            //   618: goto            345
            //   621: astore_1       
            //   622: goto            548
            //   625: astore_2       
            //   626: goto            523
            //   629: astore          5
            //   631: goto            573
            //   634: astore_3       
            //   635: goto            597
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  2      78     418    423    Ljava/lang/Throwable;
            //  2      78     412    418    Any
            //  78     94     126    131    Ljava/lang/Exception;
            //  78     94     418    423    Ljava/lang/Throwable;
            //  78     94     412    418    Any
            //  118    123    126    131    Ljava/lang/Exception;
            //  118    123    418    423    Ljava/lang/Throwable;
            //  118    123    412    418    Any
            //  127    131    418    423    Ljava/lang/Throwable;
            //  127    131    412    418    Any
            //  131    139    418    423    Ljava/lang/Throwable;
            //  131    139    412    418    Any
            //  139    147    404    412    Ljava/lang/Throwable;
            //  139    147    390    393    Any
            //  147    152    378    390    Ljava/lang/Throwable;
            //  147    152    374    378    Any
            //  152    159    378    390    Ljava/lang/Throwable;
            //  152    159    374    378    Any
            //  162    169    378    390    Ljava/lang/Throwable;
            //  162    169    374    378    Any
            //  174    182    378    390    Ljava/lang/Throwable;
            //  174    182    374    378    Any
            //  185    245    378    390    Ljava/lang/Throwable;
            //  185    245    374    378    Any
            //  250    264    599    604    Ljava/lang/Throwable;
            //  268    272    275    280    Ljava/lang/Throwable;
            //  280    284    604    608    Ljava/lang/Exception;
            //  286    295    378    390    Ljava/lang/Throwable;
            //  286    295    374    378    Any
            //  295    309    608    613    Ljava/lang/Throwable;
            //  313    317    320    325    Ljava/lang/Throwable;
            //  325    329    613    617    Ljava/lang/Exception;
            //  331    345    617    621    Ljava/lang/Throwable;
            //  351    355    360    367    Ljava/lang/Throwable;
            //  367    371    621    625    Ljava/lang/Exception;
            //  423    441    550    559    Any
            //  444    456    550    559    Any
            //  459    490    550    559    Any
            //  493    505    550    559    Any
            //  505    509    550    559    Any
            //  509    523    625    629    Ljava/lang/Throwable;
            //  527    531    534    539    Ljava/lang/Throwable;
            //  559    573    629    634    Ljava/lang/Throwable;
            //  577    581    584    589    Ljava/lang/Throwable;
            //  593    597    634    638    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 322 out-of-bounds for length 322
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
        
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ArtworkLoadTask$MXyHRkFD1Tybf8nHKTkuKr4tAsA(this));
        }
        
        protected void onPostExecute(final String s) {
            if (s != null) {
                final CacheImage cacheImage = this.cacheImage;
                cacheImage.httpTask = new HttpImageTask(cacheImage, 0, s);
                ImageLoader.this.httpTasks.add(this.cacheImage.httpTask);
                ImageLoader.this.runHttpTasks(false);
            }
            else if (this.canRetry) {
                ImageLoader.this.artworkLoadError(this.cacheImage.url);
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ArtworkLoadTask$AYFfY8_xR4BmCIfdHvNLIsGZyV0(this));
        }
    }
    
    private class CacheImage
    {
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
            this.imageReceiverArray = new ArrayList<ImageReceiver>();
            this.keys = new ArrayList<String>();
            this.filters = new ArrayList<String>();
            this.imageTypes = new ArrayList<Integer>();
        }
        
        public void addImageReceiver(final ImageReceiver imageReceiver, final String e, final String e2, final int i) {
            if (this.imageReceiverArray.contains(imageReceiver)) {
                return;
            }
            this.imageReceiverArray.add(imageReceiver);
            this.keys.add(e);
            this.filters.add(e2);
            this.imageTypes.add(i);
            ImageLoader.this.imageLoadingByTag.put(imageReceiver.getTag(i), (Object)this);
        }
        
        public void removeImageReceiver(final ImageReceiver imageReceiver) {
            int n = this.imageType;
            final int n2 = 0;
            int n3;
            for (int i = 0; i < this.imageReceiverArray.size(); i = n3 + 1) {
                final ImageReceiver imageReceiver2 = this.imageReceiverArray.get(i);
                if (imageReceiver2 != null) {
                    n3 = i;
                    if (imageReceiver2 != imageReceiver) {
                        continue;
                    }
                }
                this.imageReceiverArray.remove(i);
                this.keys.remove(i);
                this.filters.remove(i);
                n = this.imageTypes.remove(i);
                if (imageReceiver2 != null) {
                    ImageLoader.this.imageLoadingByTag.remove(imageReceiver2.getTag(n));
                }
                n3 = i - 1;
            }
            if (this.imageReceiverArray.size() == 0) {
                for (int j = n2; j < this.imageReceiverArray.size(); ++j) {
                    ImageLoader.this.imageLoadingByTag.remove(this.imageReceiverArray.get(j).getTag(n));
                }
                this.imageReceiverArray.clear();
                if (this.imageLocation != null && !ImageLoader.this.forceLoadingImages.containsKey(this.key)) {
                    final ImageLocation imageLocation = this.imageLocation;
                    if (imageLocation.location != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.location, this.ext);
                    }
                    else if (imageLocation.document != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.document);
                    }
                    else if (imageLocation.secureDocument != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.secureDocument);
                    }
                    else if (imageLocation.webFile != null) {
                        FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.imageLocation.webFile);
                    }
                }
                if (this.cacheTask != null) {
                    if (n == 1) {
                        ImageLoader.this.cacheThumbOutQueue.cancelRunnable(this.cacheTask);
                    }
                    else {
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
        
        public void replaceImageReceiver(final ImageReceiver o, final String element, final String element2, int index) {
            final int index2 = this.imageReceiverArray.indexOf(o);
            if (index2 == -1) {
                return;
            }
            int n = index2;
            if (this.imageTypes.get(index2) != index) {
                final ArrayList<ImageReceiver> imageReceiverArray = this.imageReceiverArray;
                index = imageReceiverArray.subList(index2 + 1, imageReceiverArray.size()).indexOf(o);
                if ((n = index) == -1) {
                    return;
                }
            }
            this.keys.set(n, element);
            this.filters.set(n, element2);
        }
        
        public void setImageAndClear(final Drawable drawable, final String s) {
            if (drawable != null) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE(this, drawable, new ArrayList((Collection<? extends E>)this.imageReceiverArray), s));
            }
            for (int i = 0; i < this.imageReceiverArray.size(); ++i) {
                ImageLoader.this.imageLoadingByTag.remove(this.imageReceiverArray.get(i).getTag(this.imageType));
            }
            this.imageReceiverArray.clear();
            if (this.url != null) {
                ImageLoader.this.imageLoadingByUrl.remove(this.url);
            }
            if (this.key != null) {
                ImageLoader.this.imageLoadingByKeys.remove(this.key);
            }
        }
    }
    
    private class CacheOutTask implements Runnable
    {
        private CacheImage cacheImage;
        private boolean isCancelled;
        private Thread runningThread;
        private final Object sync;
        
        public CacheOutTask(final CacheImage cacheImage) {
            this.sync = new Object();
            this.cacheImage = cacheImage;
        }
        
        private void onPostExecute(final Drawable drawable) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$CacheOutTask$P_Q_SglLFg8CKw46QDkF5nN_7Ko(this, drawable));
        }
        
        public void cancel() {
            final Object sync = this.sync;
            // monitorenter(sync)
            while (true) {
                try {
                    try {
                        this.isCancelled = true;
                        if (this.runningThread != null) {
                            this.runningThread.interrupt();
                        }
                        break Label_0033;
                    }
                    finally {
                    }
                    // monitorexit(sync)
                    // monitorexit(sync)
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
        
        @Override
        public void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.sync:Ljava/lang/Object;
            //     4: astore_1       
            //     5: aload_1        
            //     6: monitorenter   
            //     7: aload_0        
            //     8: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
            //    11: putfield        org/telegram/messenger/ImageLoader$CacheOutTask.runningThread:Ljava/lang/Thread;
            //    14: invokestatic    java/lang/Thread.interrupted:()Z
            //    17: pop            
            //    18: aload_0        
            //    19: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.isCancelled:Z
            //    22: ifeq            28
            //    25: aload_1        
            //    26: monitorexit    
            //    27: return         
            //    28: aload_1        
            //    29: monitorexit    
            //    30: aload_0        
            //    31: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //    34: astore_2       
            //    35: aload_2        
            //    36: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //    39: getfield        org/telegram/messenger/ImageLocation.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
            //    42: instanceof      Lorg/telegram/tgnet/TLRPC$TL_photoStrippedSize;
            //    45: ifeq            312
            //    48: aload_0        
            //    49: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.sync:Ljava/lang/Object;
            //    52: astore_1       
            //    53: aload_1        
            //    54: monitorenter   
            //    55: aload_0        
            //    56: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.isCancelled:Z
            //    59: ifeq            65
            //    62: aload_1        
            //    63: monitorexit    
            //    64: return         
            //    65: aload_1        
            //    66: monitorexit    
            //    67: aload_0        
            //    68: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //    71: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //    74: getfield        org/telegram/messenger/ImageLocation.photoSize:Lorg/telegram/tgnet/TLRPC$PhotoSize;
            //    77: checkcast       Lorg/telegram/tgnet/TLRPC$TL_photoStrippedSize;
            //    80: astore_3       
            //    81: aload_3        
            //    82: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
            //    85: arraylength    
            //    86: iconst_3       
            //    87: isub           
            //    88: getstatic       org/telegram/messenger/Bitmaps.header:[B
            //    91: arraylength    
            //    92: iadd           
            //    93: getstatic       org/telegram/messenger/Bitmaps.footer:[B
            //    96: arraylength    
            //    97: iadd           
            //    98: istore          4
            //   100: invokestatic    org/telegram/messenger/ImageLoader.access$1600:()Ljava/lang/ThreadLocal;
            //   103: invokevirtual   java/lang/ThreadLocal.get:()Ljava/lang/Object;
            //   106: checkcast       [B
            //   109: astore_2       
            //   110: aload_2        
            //   111: ifnull          124
            //   114: aload_2        
            //   115: arraylength    
            //   116: iload           4
            //   118: if_icmplt       124
            //   121: goto            126
            //   124: aconst_null    
            //   125: astore_2       
            //   126: aload_2        
            //   127: astore_1       
            //   128: aload_2        
            //   129: ifnonnull       144
            //   132: iload           4
            //   134: newarray        B
            //   136: astore_1       
            //   137: invokestatic    org/telegram/messenger/ImageLoader.access$1600:()Ljava/lang/ThreadLocal;
            //   140: aload_1        
            //   141: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
            //   144: getstatic       org/telegram/messenger/Bitmaps.header:[B
            //   147: astore_2       
            //   148: aload_2        
            //   149: iconst_0       
            //   150: aload_1        
            //   151: iconst_0       
            //   152: aload_2        
            //   153: arraylength    
            //   154: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
            //   157: aload_3        
            //   158: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
            //   161: astore_2       
            //   162: aload_2        
            //   163: iconst_3       
            //   164: aload_1        
            //   165: getstatic       org/telegram/messenger/Bitmaps.header:[B
            //   168: arraylength    
            //   169: aload_2        
            //   170: arraylength    
            //   171: iconst_3       
            //   172: isub           
            //   173: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
            //   176: getstatic       org/telegram/messenger/Bitmaps.footer:[B
            //   179: iconst_0       
            //   180: aload_1        
            //   181: getstatic       org/telegram/messenger/Bitmaps.header:[B
            //   184: arraylength    
            //   185: aload_3        
            //   186: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
            //   189: arraylength    
            //   190: iadd           
            //   191: iconst_3       
            //   192: isub           
            //   193: getstatic       org/telegram/messenger/Bitmaps.footer:[B
            //   196: arraylength    
            //   197: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
            //   200: aload_3        
            //   201: getfield        org/telegram/tgnet/TLRPC$PhotoSize.bytes:[B
            //   204: astore_2       
            //   205: aload_1        
            //   206: sipush          164
            //   209: aload_2        
            //   210: iconst_1       
            //   211: baload         
            //   212: i2b            
            //   213: bastore        
            //   214: aload_1        
            //   215: sipush          166
            //   218: aload_2        
            //   219: iconst_2       
            //   220: baload         
            //   221: i2b            
            //   222: bastore        
            //   223: aload_1        
            //   224: iconst_0       
            //   225: iload           4
            //   227: invokestatic    android/graphics/BitmapFactory.decodeByteArray:([BII)Landroid/graphics/Bitmap;
            //   230: astore_2       
            //   231: aload_2        
            //   232: ifnull          281
            //   235: aload_0        
            //   236: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   239: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //   242: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
            //   245: ifne            281
            //   248: aload_0        
            //   249: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   252: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //   255: ldc             "b"
            //   257: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //   260: ifeq            281
            //   263: aload_2        
            //   264: iconst_3       
            //   265: iconst_1       
            //   266: aload_2        
            //   267: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //   270: aload_2        
            //   271: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //   274: aload_2        
            //   275: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //   278: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //   281: aload_2        
            //   282: ifnull          297
            //   285: new             Landroid/graphics/drawable/BitmapDrawable;
            //   288: dup            
            //   289: aload_2        
            //   290: invokespecial   android/graphics/drawable/BitmapDrawable.<init>:(Landroid/graphics/Bitmap;)V
            //   293: astore_2       
            //   294: goto            299
            //   297: aconst_null    
            //   298: astore_2       
            //   299: aload_0        
            //   300: aload_2        
            //   301: invokespecial   org/telegram/messenger/ImageLoader$CacheOutTask.onPostExecute:(Landroid/graphics/drawable/Drawable;)V
            //   304: goto            4508
            //   307: astore_2       
            //   308: aload_1        
            //   309: monitorexit    
            //   310: aload_2        
            //   311: athrow         
            //   312: aload_2        
            //   313: getfield        org/telegram/messenger/ImageLoader$CacheImage.lottieFile:Z
            //   316: ifeq            429
            //   319: aload_0        
            //   320: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.sync:Ljava/lang/Object;
            //   323: astore_1       
            //   324: aload_1        
            //   325: monitorenter   
            //   326: aload_0        
            //   327: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.isCancelled:Z
            //   330: ifeq            336
            //   333: aload_1        
            //   334: monitorexit    
            //   335: return         
            //   336: aload_1        
            //   337: monitorexit    
            //   338: new             Lcom/airbnb/lottie/LottieDrawable;
            //   341: dup            
            //   342: invokespecial   com/airbnb/lottie/LottieDrawable.<init>:()V
            //   345: astore_2       
            //   346: new             Ljava/io/FileInputStream;
            //   349: astore_1       
            //   350: aload_1        
            //   351: aload_0        
            //   352: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   355: getfield        org/telegram/messenger/ImageLoader$CacheImage.finalFilePath:Ljava/io/File;
            //   358: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
            //   361: aload_2        
            //   362: aload_1        
            //   363: aload_0        
            //   364: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   367: getfield        org/telegram/messenger/ImageLoader$CacheImage.finalFilePath:Ljava/io/File;
            //   370: invokevirtual   java/io/File.toString:()Ljava/lang/String;
            //   373: invokestatic    com/airbnb/lottie/LottieCompositionFactory.fromJsonInputStreamSync:(Ljava/io/InputStream;Ljava/lang/String;)Lcom/airbnb/lottie/LottieResult;
            //   376: invokevirtual   com/airbnb/lottie/LottieResult.getValue:()Ljava/lang/Object;
            //   379: checkcast       Lcom/airbnb/lottie/LottieComposition;
            //   382: invokevirtual   com/airbnb/lottie/LottieDrawable.setComposition:(Lcom/airbnb/lottie/LottieComposition;)Z
            //   385: pop            
            //   386: aload_1        
            //   387: invokevirtual   java/io/FileInputStream.close:()V
            //   390: goto            398
            //   393: astore_1       
            //   394: aload_1        
            //   395: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   398: aload_2        
            //   399: iconst_1       
            //   400: invokevirtual   com/airbnb/lottie/LottieDrawable.setRepeatMode:(I)V
            //   403: aload_2        
            //   404: iconst_m1      
            //   405: invokevirtual   com/airbnb/lottie/LottieDrawable.setRepeatCount:(I)V
            //   408: aload_2        
            //   409: invokevirtual   com/airbnb/lottie/LottieDrawable.start:()V
            //   412: invokestatic    java/lang/Thread.interrupted:()Z
            //   415: pop            
            //   416: aload_0        
            //   417: aload_2        
            //   418: invokespecial   org/telegram/messenger/ImageLoader$CacheOutTask.onPostExecute:(Landroid/graphics/drawable/Drawable;)V
            //   421: goto            4508
            //   424: astore_2       
            //   425: aload_1        
            //   426: monitorexit    
            //   427: aload_2        
            //   428: athrow         
            //   429: aload_2        
            //   430: getfield        org/telegram/messenger/ImageLoader$CacheImage.animatedFile:Z
            //   433: ifeq            598
            //   436: aload_0        
            //   437: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.sync:Ljava/lang/Object;
            //   440: astore_1       
            //   441: aload_1        
            //   442: monitorenter   
            //   443: aload_0        
            //   444: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.isCancelled:Z
            //   447: ifeq            453
            //   450: aload_1        
            //   451: monitorexit    
            //   452: return         
            //   453: aload_1        
            //   454: monitorexit    
            //   455: ldc_w           "g"
            //   458: aload_0        
            //   459: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   462: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //   465: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //   468: ifeq            544
            //   471: aload_0        
            //   472: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   475: astore_3       
            //   476: aload_3        
            //   477: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //   480: getfield        org/telegram/messenger/ImageLocation.document:Lorg/telegram/tgnet/TLRPC$Document;
            //   483: astore_2       
            //   484: aload_2        
            //   485: instanceof      Lorg/telegram/tgnet/TLRPC$TL_documentEncrypted;
            //   488: ifne            544
            //   491: aload_3        
            //   492: getfield        org/telegram/messenger/ImageLoader$CacheImage.finalFilePath:Ljava/io/File;
            //   495: astore_1       
            //   496: aload_3        
            //   497: getfield        org/telegram/messenger/ImageLoader$CacheImage.size:I
            //   500: i2l            
            //   501: lstore          5
            //   503: aload_2        
            //   504: instanceof      Lorg/telegram/tgnet/TLRPC$Document;
            //   507: ifeq            513
            //   510: goto            515
            //   513: aconst_null    
            //   514: astore_2       
            //   515: aload_0        
            //   516: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   519: astore_3       
            //   520: new             Lorg/telegram/ui/Components/AnimatedFileDrawable;
            //   523: dup            
            //   524: aload_1        
            //   525: iconst_0       
            //   526: lload           5
            //   528: aload_2        
            //   529: aload_3        
            //   530: getfield        org/telegram/messenger/ImageLoader$CacheImage.parentObject:Ljava/lang/Object;
            //   533: aload_3        
            //   534: getfield        org/telegram/messenger/ImageLoader$CacheImage.currentAccount:I
            //   537: invokespecial   org/telegram/ui/Components/AnimatedFileDrawable.<init>:(Ljava/io/File;ZJLorg/telegram/tgnet/TLRPC$Document;Ljava/lang/Object;I)V
            //   540: astore_2       
            //   541: goto            581
            //   544: aload_0        
            //   545: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   548: astore_2       
            //   549: new             Lorg/telegram/ui/Components/AnimatedFileDrawable;
            //   552: dup            
            //   553: aload_2        
            //   554: getfield        org/telegram/messenger/ImageLoader$CacheImage.finalFilePath:Ljava/io/File;
            //   557: ldc_w           "d"
            //   560: aload_2        
            //   561: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //   564: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //   567: lconst_0       
            //   568: aconst_null    
            //   569: aconst_null    
            //   570: aload_0        
            //   571: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   574: getfield        org/telegram/messenger/ImageLoader$CacheImage.currentAccount:I
            //   577: invokespecial   org/telegram/ui/Components/AnimatedFileDrawable.<init>:(Ljava/io/File;ZJLorg/telegram/tgnet/TLRPC$Document;Ljava/lang/Object;I)V
            //   580: astore_2       
            //   581: invokestatic    java/lang/Thread.interrupted:()Z
            //   584: pop            
            //   585: aload_0        
            //   586: aload_2        
            //   587: invokespecial   org/telegram/messenger/ImageLoader$CacheOutTask.onPostExecute:(Landroid/graphics/drawable/Drawable;)V
            //   590: goto            4508
            //   593: astore_2       
            //   594: aload_1        
            //   595: monitorexit    
            //   596: aload_2        
            //   597: athrow         
            //   598: aload_2        
            //   599: getfield        org/telegram/messenger/ImageLoader$CacheImage.finalFilePath:Ljava/io/File;
            //   602: astore          7
            //   604: aload_2        
            //   605: getfield        org/telegram/messenger/ImageLoader$CacheImage.secureDocument:Lorg/telegram/messenger/SecureDocument;
            //   608: ifnonnull       646
            //   611: aload_2        
            //   612: getfield        org/telegram/messenger/ImageLoader$CacheImage.encryptionKeyPath:Ljava/io/File;
            //   615: ifnull          640
            //   618: aload           7
            //   620: ifnull          640
            //   623: aload           7
            //   625: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
            //   628: ldc_w           ".enc"
            //   631: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
            //   634: ifeq            640
            //   637: goto            646
            //   640: iconst_0       
            //   641: istore          8
            //   643: goto            649
            //   646: iconst_1       
            //   647: istore          8
            //   649: aload_0        
            //   650: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   653: getfield        org/telegram/messenger/ImageLoader$CacheImage.secureDocument:Lorg/telegram/messenger/SecureDocument;
            //   656: astore_2       
            //   657: aload_2        
            //   658: ifnull          705
            //   661: aload_2        
            //   662: getfield        org/telegram/messenger/SecureDocument.secureDocumentKey:Lorg/telegram/messenger/SecureDocumentKey;
            //   665: astore          9
            //   667: aload_2        
            //   668: getfield        org/telegram/messenger/SecureDocument.secureFile:Lorg/telegram/tgnet/TLRPC$TL_secureFile;
            //   671: astore_2       
            //   672: aload_2        
            //   673: ifnull          688
            //   676: aload_2        
            //   677: getfield        org/telegram/tgnet/TLRPC$TL_secureFile.file_hash:[B
            //   680: astore_2       
            //   681: aload_2        
            //   682: ifnull          688
            //   685: goto            699
            //   688: aload_0        
            //   689: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   692: getfield        org/telegram/messenger/ImageLoader$CacheImage.secureDocument:Lorg/telegram/messenger/SecureDocument;
            //   695: getfield        org/telegram/messenger/SecureDocument.fileHash:[B
            //   698: astore_2       
            //   699: aload_2        
            //   700: astore          10
            //   702: goto            711
            //   705: aconst_null    
            //   706: astore          9
            //   708: aconst_null    
            //   709: astore          10
            //   711: getstatic       android/os/Build$VERSION.SDK_INT:I
            //   714: bipush          19
            //   716: if_icmpge       936
            //   719: new             Ljava/io/RandomAccessFile;
            //   722: astore_2       
            //   723: aload_2        
            //   724: aload           7
            //   726: ldc_w           "r"
            //   729: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
            //   732: aload_2        
            //   733: astore_1       
            //   734: aload_0        
            //   735: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   738: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageType:I
            //   741: iconst_1       
            //   742: if_icmpne       754
            //   745: aload_2        
            //   746: astore_1       
            //   747: invokestatic    org/telegram/messenger/ImageLoader.access$1700:()[B
            //   750: astore_3       
            //   751: goto            760
            //   754: aload_2        
            //   755: astore_1       
            //   756: invokestatic    org/telegram/messenger/ImageLoader.access$1800:()[B
            //   759: astore_3       
            //   760: aload_2        
            //   761: astore_1       
            //   762: aload_2        
            //   763: aload_3        
            //   764: iconst_0       
            //   765: aload_3        
            //   766: arraylength    
            //   767: invokevirtual   java/io/RandomAccessFile.readFully:([BII)V
            //   770: aload_2        
            //   771: astore_1       
            //   772: new             Ljava/lang/String;
            //   775: astore          11
            //   777: aload_2        
            //   778: astore_1       
            //   779: aload           11
            //   781: aload_3        
            //   782: invokespecial   java/lang/String.<init>:([B)V
            //   785: aload_2        
            //   786: astore_1       
            //   787: aload           11
            //   789: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
            //   792: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
            //   795: astore_3       
            //   796: aload_2        
            //   797: astore_1       
            //   798: aload_3        
            //   799: ldc_w           "riff"
            //   802: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
            //   805: ifeq            830
            //   808: aload_2        
            //   809: astore_1       
            //   810: aload_3        
            //   811: ldc_w           "webp"
            //   814: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
            //   817: istore          12
            //   819: iload           12
            //   821: ifeq            830
            //   824: iconst_1       
            //   825: istore          13
            //   827: goto            833
            //   830: iconst_0       
            //   831: istore          13
            //   833: aload_2        
            //   834: astore_1       
            //   835: aload_2        
            //   836: invokevirtual   java/io/RandomAccessFile.close:()V
            //   839: iload           13
            //   841: istore          14
            //   843: aload_2        
            //   844: invokevirtual   java/io/RandomAccessFile.close:()V
            //   847: goto            939
            //   850: astore_2       
            //   851: aload_2        
            //   852: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   855: iload           14
            //   857: istore          13
            //   859: goto            939
            //   862: astore_3       
            //   863: iload           13
            //   865: istore          4
            //   867: goto            888
            //   870: astore_1       
            //   871: goto            883
            //   874: astore_2       
            //   875: aconst_null    
            //   876: astore_1       
            //   877: goto            918
            //   880: astore_1       
            //   881: aconst_null    
            //   882: astore_2       
            //   883: iconst_0       
            //   884: istore          4
            //   886: aload_1        
            //   887: astore_3       
            //   888: aload_2        
            //   889: astore_1       
            //   890: aload_3        
            //   891: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   894: iload           4
            //   896: istore          13
            //   898: aload_2        
            //   899: ifnull          939
            //   902: iload           4
            //   904: istore          14
            //   906: aload_2        
            //   907: invokevirtual   java/io/RandomAccessFile.close:()V
            //   910: iload           4
            //   912: istore          13
            //   914: goto            939
            //   917: astore_2       
            //   918: aload_1        
            //   919: ifnull          934
            //   922: aload_1        
            //   923: invokevirtual   java/io/RandomAccessFile.close:()V
            //   926: goto            934
            //   929: astore_1       
            //   930: aload_1        
            //   931: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //   934: aload_2        
            //   935: athrow         
            //   936: iconst_0       
            //   937: istore          13
            //   939: aload_0        
            //   940: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //   943: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //   946: getfield        org/telegram/messenger/ImageLocation.path:Ljava/lang/String;
            //   949: astore_1       
            //   950: aload_1        
            //   951: ifnull          1111
            //   954: aload_1        
            //   955: ldc_w           "thumb://"
            //   958: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
            //   961: ifeq            1035
            //   964: aload_1        
            //   965: ldc_w           ":"
            //   968: bipush          8
            //   970: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //   973: istore          4
            //   975: iload           4
            //   977: iflt            1007
            //   980: aload_1        
            //   981: bipush          8
            //   983: iload           4
            //   985: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   988: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
            //   991: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //   994: astore_2       
            //   995: aload_1        
            //   996: iload           4
            //   998: iconst_1       
            //   999: iadd           
            //  1000: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
            //  1003: astore_1       
            //  1004: goto            1011
            //  1007: aconst_null    
            //  1008: astore_1       
            //  1009: aconst_null    
            //  1010: astore_2       
            //  1011: aload_1        
            //  1012: astore_3       
            //  1013: aload_2        
            //  1014: astore_1       
            //  1015: aload_3        
            //  1016: astore_2       
            //  1017: iconst_0       
            //  1018: istore          4
            //  1020: iconst_0       
            //  1021: istore          15
            //  1023: aload_2        
            //  1024: astore          11
            //  1026: aload_1        
            //  1027: astore_3       
            //  1028: iload           4
            //  1030: istore          16
            //  1032: goto            1122
            //  1035: aload_1        
            //  1036: ldc_w           "vthumb://"
            //  1039: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
            //  1042: ifeq            1094
            //  1045: aload_1        
            //  1046: ldc_w           ":"
            //  1049: bipush          9
            //  1051: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;I)I
            //  1054: istore          4
            //  1056: iload           4
            //  1058: iflt            1082
            //  1061: aload_1        
            //  1062: bipush          9
            //  1064: iload           4
            //  1066: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //  1069: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
            //  1072: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
            //  1075: astore_2       
            //  1076: iconst_1       
            //  1077: istore          4
            //  1079: goto            1087
            //  1082: aconst_null    
            //  1083: astore_2       
            //  1084: iconst_0       
            //  1085: istore          4
            //  1087: aload_2        
            //  1088: astore_1       
            //  1089: aconst_null    
            //  1090: astore_2       
            //  1091: goto            1020
            //  1094: aload_1        
            //  1095: ldc_w           "http"
            //  1098: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
            //  1101: ifne            1111
            //  1104: aconst_null    
            //  1105: astore_2       
            //  1106: aconst_null    
            //  1107: astore_1       
            //  1108: goto            1017
            //  1111: aconst_null    
            //  1112: astore          11
            //  1114: aconst_null    
            //  1115: astore_3       
            //  1116: iconst_0       
            //  1117: istore          16
            //  1119: iconst_1       
            //  1120: istore          15
            //  1122: new             Landroid/graphics/BitmapFactory$Options;
            //  1125: dup            
            //  1126: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
            //  1129: astore          17
            //  1131: aload           17
            //  1133: iconst_1       
            //  1134: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
            //  1137: getstatic       android/os/Build$VERSION.SDK_INT:I
            //  1140: bipush          21
            //  1142: if_icmpge       1151
            //  1145: aload           17
            //  1147: iconst_1       
            //  1148: putfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  1151: aload_0        
            //  1152: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.this$0:Lorg/telegram/messenger/ImageLoader;
            //  1155: invokestatic    org/telegram/messenger/ImageLoader.access$1900:(Lorg/telegram/messenger/ImageLoader;)Z
            //  1158: istore          12
            //  1160: aload_0        
            //  1161: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1164: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1167: ifnull          1801
            //  1170: aload_0        
            //  1171: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1174: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1177: ldc_w           "_"
            //  1180: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
            //  1183: astore_2       
            //  1184: aload_2        
            //  1185: arraylength    
            //  1186: iconst_2       
            //  1187: if_icmplt       1237
            //  1190: aload_2        
            //  1191: iconst_0       
            //  1192: aaload         
            //  1193: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
            //  1196: fstore          18
            //  1198: getstatic       org/telegram/messenger/AndroidUtilities.density:F
            //  1201: fstore          19
            //  1203: fload           18
            //  1205: fload           19
            //  1207: fmul           
            //  1208: fstore          18
            //  1210: aload_2        
            //  1211: iconst_1       
            //  1212: aaload         
            //  1213: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
            //  1216: fstore          20
            //  1218: getstatic       org/telegram/messenger/AndroidUtilities.density:F
            //  1221: fstore          19
            //  1223: fload           20
            //  1225: fload           19
            //  1227: fmul           
            //  1228: fstore          19
            //  1230: goto            1243
            //  1233: astore_2       
            //  1234: goto            1990
            //  1237: fconst_0       
            //  1238: fstore          18
            //  1240: fconst_0       
            //  1241: fstore          19
            //  1243: aload_0        
            //  1244: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1247: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1250: ldc_w           "b2"
            //  1253: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //  1256: ifeq            1265
            //  1259: iconst_3       
            //  1260: istore          4
            //  1262: goto            1315
            //  1265: aload_0        
            //  1266: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1269: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1272: ldc_w           "b1"
            //  1275: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //  1278: ifeq            1287
            //  1281: iconst_2       
            //  1282: istore          4
            //  1284: goto            1315
            //  1287: aload_0        
            //  1288: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1291: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1294: ldc             "b"
            //  1296: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //  1299: istore          21
            //  1301: iload           21
            //  1303: ifeq            1312
            //  1306: iconst_1       
            //  1307: istore          4
            //  1309: goto            1315
            //  1312: iconst_0       
            //  1313: istore          4
            //  1315: aload_0        
            //  1316: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1319: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1322: ldc_w           "i"
            //  1325: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //  1328: istore          22
            //  1330: iload           12
            //  1332: istore          21
            //  1334: aload_0        
            //  1335: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1338: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  1341: ldc_w           "f"
            //  1344: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
            //  1347: ifeq            1353
            //  1350: iconst_1       
            //  1351: istore          21
            //  1353: iload           13
            //  1355: ifne            1757
            //  1358: fload           18
            //  1360: fconst_0       
            //  1361: fcmpl          
            //  1362: ifeq            1757
            //  1365: fload           19
            //  1367: fconst_0       
            //  1368: fcmpl          
            //  1369: ifeq            1757
            //  1372: iload           21
            //  1374: istore          12
            //  1376: aload           17
            //  1378: iconst_1       
            //  1379: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
            //  1382: aload_3        
            //  1383: ifnull          1442
            //  1386: aload           11
            //  1388: ifnonnull       1442
            //  1391: iload           16
            //  1393: ifeq            1422
            //  1396: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
            //  1399: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
            //  1402: astore_2       
            //  1403: aload_2        
            //  1404: aload_3        
            //  1405: invokevirtual   java/lang/Long.longValue:()J
            //  1408: iconst_1       
            //  1409: aload           17
            //  1411: invokestatic    android/provider/MediaStore$Video$Thumbnails.getThumbnail:(Landroid/content/ContentResolver;JILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  1414: pop            
            //  1415: goto            1439
            //  1418: astore_2       
            //  1419: goto            1607
            //  1422: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
            //  1425: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
            //  1428: aload_3        
            //  1429: invokevirtual   java/lang/Long.longValue:()J
            //  1432: iconst_1       
            //  1433: aload           17
            //  1435: invokestatic    android/provider/MediaStore$Images$Thumbnails.getThumbnail:(Landroid/content/ContentResolver;JILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  1438: pop            
            //  1439: goto            1657
            //  1442: aload           9
            //  1444: ifnull          1610
            //  1447: new             Ljava/io/RandomAccessFile;
            //  1450: astore          23
            //  1452: aload           23
            //  1454: aload           7
            //  1456: ldc_w           "r"
            //  1459: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
            //  1462: aload           23
            //  1464: invokevirtual   java/io/RandomAccessFile.length:()J
            //  1467: l2i            
            //  1468: istore          24
            //  1470: invokestatic    org/telegram/messenger/ImageLoader.access$1600:()Ljava/lang/ThreadLocal;
            //  1473: invokevirtual   java/lang/ThreadLocal.get:()Ljava/lang/Object;
            //  1476: checkcast       [B
            //  1479: astore_2       
            //  1480: aload_2        
            //  1481: ifnull          1494
            //  1484: aload_2        
            //  1485: arraylength    
            //  1486: iload           24
            //  1488: if_icmplt       1494
            //  1491: goto            1496
            //  1494: aconst_null    
            //  1495: astore_2       
            //  1496: aload_2        
            //  1497: astore_1       
            //  1498: aload_2        
            //  1499: ifnonnull       1514
            //  1502: iload           24
            //  1504: newarray        B
            //  1506: astore_1       
            //  1507: invokestatic    org/telegram/messenger/ImageLoader.access$1600:()Ljava/lang/ThreadLocal;
            //  1510: aload_1        
            //  1511: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
            //  1514: aload           23
            //  1516: aload_1        
            //  1517: iconst_0       
            //  1518: iload           24
            //  1520: invokevirtual   java/io/RandomAccessFile.readFully:([BII)V
            //  1523: aload           23
            //  1525: invokevirtual   java/io/RandomAccessFile.close:()V
            //  1528: aload_1        
            //  1529: iconst_0       
            //  1530: iload           24
            //  1532: aload           9
            //  1534: invokestatic    org/telegram/messenger/secretmedia/EncryptedFileInputStream.decryptBytesWithKeyFile:([BIILorg/telegram/messenger/SecureDocumentKey;)V
            //  1537: aload_1        
            //  1538: iconst_0       
            //  1539: iload           24
            //  1541: invokestatic    org/telegram/messenger/Utilities.computeSHA256:([BII)[B
            //  1544: astore_2       
            //  1545: aload           10
            //  1547: ifnull          1572
            //  1550: aload_2        
            //  1551: aload           10
            //  1553: invokestatic    java/util/Arrays.equals:([B[B)Z
            //  1556: istore          12
            //  1558: iload           12
            //  1560: ifne            1566
            //  1563: goto            1572
            //  1566: iconst_0       
            //  1567: istore          14
            //  1569: goto            1575
            //  1572: iconst_1       
            //  1573: istore          14
            //  1575: aload_1        
            //  1576: iconst_0       
            //  1577: baload         
            //  1578: sipush          255
            //  1581: iand           
            //  1582: istore          25
            //  1584: iload           14
            //  1586: ifne            1657
            //  1589: aload_1        
            //  1590: iload           25
            //  1592: iload           24
            //  1594: iload           25
            //  1596: isub           
            //  1597: aload           17
            //  1599: invokestatic    android/graphics/BitmapFactory.decodeByteArray:([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  1602: pop            
            //  1603: goto            1657
            //  1606: astore_2       
            //  1607: goto            1771
            //  1610: iload           8
            //  1612: ifeq            1635
            //  1615: new             Lorg/telegram/messenger/secretmedia/EncryptedFileInputStream;
            //  1618: astore_2       
            //  1619: aload_2        
            //  1620: aload           7
            //  1622: aload_0        
            //  1623: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  1626: getfield        org/telegram/messenger/ImageLoader$CacheImage.encryptionKeyPath:Ljava/io/File;
            //  1629: invokespecial   org/telegram/messenger/secretmedia/EncryptedFileInputStream.<init>:(Ljava/io/File;Ljava/io/File;)V
            //  1632: goto            1645
            //  1635: new             Ljava/io/FileInputStream;
            //  1638: dup            
            //  1639: aload           7
            //  1641: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
            //  1644: astore_2       
            //  1645: aload_2        
            //  1646: aconst_null    
            //  1647: aload           17
            //  1649: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  1652: pop            
            //  1653: aload_2        
            //  1654: invokevirtual   java/io/FileInputStream.close:()V
            //  1657: aload           17
            //  1659: getfield        android/graphics/BitmapFactory$Options.outWidth:I
            //  1662: i2f            
            //  1663: fstore          26
            //  1665: aload           17
            //  1667: getfield        android/graphics/BitmapFactory$Options.outHeight:I
            //  1670: i2f            
            //  1671: fstore          20
            //  1673: fload           18
            //  1675: fload           19
            //  1677: fcmpl          
            //  1678: ifle            1707
            //  1681: fload           26
            //  1683: fload           20
            //  1685: fcmpl          
            //  1686: ifle            1707
            //  1689: fload           26
            //  1691: fload           18
            //  1693: fdiv           
            //  1694: fload           20
            //  1696: fload           19
            //  1698: fdiv           
            //  1699: invokestatic    java/lang/Math.max:(FF)F
            //  1702: fstore          20
            //  1704: goto            1722
            //  1707: fload           26
            //  1709: fload           18
            //  1711: fdiv           
            //  1712: fload           20
            //  1714: fload           19
            //  1716: fdiv           
            //  1717: invokestatic    java/lang/Math.min:(FF)F
            //  1720: fstore          20
            //  1722: fload           20
            //  1724: fstore          26
            //  1726: fload           20
            //  1728: fconst_1       
            //  1729: fcmpg          
            //  1730: ifge            1736
            //  1733: fconst_1       
            //  1734: fstore          26
            //  1736: aload           17
            //  1738: iconst_0       
            //  1739: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
            //  1742: aload           17
            //  1744: fload           26
            //  1746: f2i            
            //  1747: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
            //  1750: goto            1757
            //  1753: astore_2       
            //  1754: goto            1771
            //  1757: aconst_null    
            //  1758: astore_2       
            //  1759: iload           21
            //  1761: istore          12
            //  1763: goto            1969
            //  1766: astore_2       
            //  1767: iload           12
            //  1769: istore          21
            //  1771: iload           22
            //  1773: istore          12
            //  1775: goto            1790
            //  1778: astore_2       
            //  1779: iconst_0       
            //  1780: istore          22
            //  1782: iload           12
            //  1784: istore          21
            //  1786: iload           22
            //  1788: istore          12
            //  1790: aconst_null    
            //  1791: astore_1       
            //  1792: goto            2009
            //  1795: astore_2       
            //  1796: aconst_null    
            //  1797: astore_1       
            //  1798: goto            1995
            //  1801: aload           11
            //  1803: ifnull          1955
            //  1806: aload           17
            //  1808: iconst_1       
            //  1809: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
            //  1812: iload           12
            //  1814: ifeq            1824
            //  1817: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  1820: astore_2       
            //  1821: goto            1828
            //  1824: getstatic       android/graphics/Bitmap$Config.RGB_565:Landroid/graphics/Bitmap$Config;
            //  1827: astore_2       
            //  1828: aload           17
            //  1830: aload_2        
            //  1831: putfield        android/graphics/BitmapFactory$Options.inPreferredConfig:Landroid/graphics/Bitmap$Config;
            //  1834: new             Ljava/io/FileInputStream;
            //  1837: astore_2       
            //  1838: aload_2        
            //  1839: aload           7
            //  1841: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
            //  1844: aload_2        
            //  1845: aconst_null    
            //  1846: aload           17
            //  1848: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  1851: astore_1       
            //  1852: aload_2        
            //  1853: invokevirtual   java/io/FileInputStream.close:()V
            //  1856: aload           17
            //  1858: getfield        android/graphics/BitmapFactory$Options.outWidth:I
            //  1861: istore          4
            //  1863: aload           17
            //  1865: getfield        android/graphics/BitmapFactory$Options.outHeight:I
            //  1868: istore          14
            //  1870: aload           17
            //  1872: iconst_0       
            //  1873: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
            //  1876: iload           4
            //  1878: sipush          200
            //  1881: idiv           
            //  1882: iload           14
            //  1884: sipush          200
            //  1887: idiv           
            //  1888: invokestatic    java/lang/Math.max:(II)I
            //  1891: i2f            
            //  1892: fstore          18
            //  1894: fload           18
            //  1896: fstore          19
            //  1898: fload           18
            //  1900: fconst_1       
            //  1901: fcmpg          
            //  1902: ifge            1908
            //  1905: fconst_1       
            //  1906: fstore          19
            //  1908: iconst_1       
            //  1909: istore          4
            //  1911: iload           4
            //  1913: iconst_2       
            //  1914: imul           
            //  1915: istore          14
            //  1917: iload           14
            //  1919: istore          4
            //  1921: iload           14
            //  1923: iconst_2       
            //  1924: imul           
            //  1925: i2f            
            //  1926: fload           19
            //  1928: fcmpg          
            //  1929: iflt            1911
            //  1932: aload           17
            //  1934: iload           14
            //  1936: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
            //  1939: aload_1        
            //  1940: astore_2       
            //  1941: goto            1957
            //  1944: astore_2       
            //  1945: fconst_0       
            //  1946: fstore          18
            //  1948: goto            1992
            //  1951: astore_2       
            //  1952: goto            1987
            //  1955: aconst_null    
            //  1956: astore_2       
            //  1957: fconst_0       
            //  1958: fstore          18
            //  1960: iconst_0       
            //  1961: istore          22
            //  1963: fconst_0       
            //  1964: fstore          19
            //  1966: iconst_0       
            //  1967: istore          4
            //  1969: iload           22
            //  1971: istore          21
            //  1973: aload_2        
            //  1974: astore_1       
            //  1975: iload           12
            //  1977: istore          22
            //  1979: iload           4
            //  1981: istore          14
            //  1983: goto            2025
            //  1986: astore_2       
            //  1987: fconst_0       
            //  1988: fstore          18
            //  1990: aconst_null    
            //  1991: astore_1       
            //  1992: fconst_0       
            //  1993: fstore          19
            //  1995: iconst_0       
            //  1996: istore          22
            //  1998: iconst_0       
            //  1999: istore          4
            //  2001: iload           12
            //  2003: istore          21
            //  2005: iload           22
            //  2007: istore          12
            //  2009: aload_2        
            //  2010: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //  2013: iload           4
            //  2015: istore          14
            //  2017: iload           21
            //  2019: istore          22
            //  2021: iload           12
            //  2023: istore          21
            //  2025: aload_0        
            //  2026: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  2029: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageType:I
            //  2032: iconst_1       
            //  2033: if_icmpne       3114
            //  2036: aload_1        
            //  2037: astore_3       
            //  2038: aload_0        
            //  2039: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.this$0:Lorg/telegram/messenger/ImageLoader;
            //  2042: invokestatic    java/lang/System.currentTimeMillis:()J
            //  2045: invokestatic    org/telegram/messenger/ImageLoader.access$2002:(Lorg/telegram/messenger/ImageLoader;J)J
            //  2048: pop2           
            //  2049: aload_1        
            //  2050: astore_3       
            //  2051: aload_0        
            //  2052: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.sync:Ljava/lang/Object;
            //  2055: astore_2       
            //  2056: aload_1        
            //  2057: astore_3       
            //  2058: aload_2        
            //  2059: monitorenter   
            //  2060: aload_0        
            //  2061: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.isCancelled:Z
            //  2064: ifeq            2070
            //  2067: aload_2        
            //  2068: monitorexit    
            //  2069: return         
            //  2070: aload_2        
            //  2071: monitorexit    
            //  2072: iload           13
            //  2074: ifeq            2221
            //  2077: aload_1        
            //  2078: astore_3       
            //  2079: new             Ljava/io/RandomAccessFile;
            //  2082: astore          10
            //  2084: aload_1        
            //  2085: astore_3       
            //  2086: aload           10
            //  2088: aload           7
            //  2090: ldc_w           "r"
            //  2093: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
            //  2096: aload_1        
            //  2097: astore_3       
            //  2098: aload           10
            //  2100: invokevirtual   java/io/RandomAccessFile.getChannel:()Ljava/nio/channels/FileChannel;
            //  2103: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
            //  2106: lconst_0       
            //  2107: aload           7
            //  2109: invokevirtual   java/io/File.length:()J
            //  2112: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
            //  2115: astore          9
            //  2117: aload_1        
            //  2118: astore_3       
            //  2119: new             Landroid/graphics/BitmapFactory$Options;
            //  2122: astore_2       
            //  2123: aload_1        
            //  2124: astore_3       
            //  2125: aload_2        
            //  2126: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
            //  2129: aload_1        
            //  2130: astore_3       
            //  2131: aload_2        
            //  2132: iconst_1       
            //  2133: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
            //  2136: aload_1        
            //  2137: astore_3       
            //  2138: aconst_null    
            //  2139: aload           9
            //  2141: aload           9
            //  2143: invokevirtual   java/nio/ByteBuffer.limit:()I
            //  2146: aload_2        
            //  2147: iconst_1       
            //  2148: invokestatic    org/telegram/messenger/Utilities.loadWebpImage:(Landroid/graphics/Bitmap;Ljava/nio/ByteBuffer;ILandroid/graphics/BitmapFactory$Options;Z)Z
            //  2151: pop            
            //  2152: aload_1        
            //  2153: astore_3       
            //  2154: aload_2        
            //  2155: getfield        android/graphics/BitmapFactory$Options.outWidth:I
            //  2158: aload_2        
            //  2159: getfield        android/graphics/BitmapFactory$Options.outHeight:I
            //  2162: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  2165: invokestatic    org/telegram/messenger/Bitmaps.createBitmap:(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
            //  2168: astore_2       
            //  2169: aload_2        
            //  2170: astore_3       
            //  2171: aload           9
            //  2173: invokevirtual   java/nio/ByteBuffer.limit:()I
            //  2176: istore          4
            //  2178: aload_2        
            //  2179: astore_3       
            //  2180: aload           17
            //  2182: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2185: ifne            2194
            //  2188: iconst_1       
            //  2189: istore          12
            //  2191: goto            2197
            //  2194: iconst_0       
            //  2195: istore          12
            //  2197: aload_2        
            //  2198: astore_3       
            //  2199: aload_2        
            //  2200: aload           9
            //  2202: iload           4
            //  2204: aconst_null    
            //  2205: iload           12
            //  2207: invokestatic    org/telegram/messenger/Utilities.loadWebpImage:(Landroid/graphics/Bitmap;Ljava/nio/ByteBuffer;ILandroid/graphics/BitmapFactory$Options;Z)Z
            //  2210: pop            
            //  2211: aload_2        
            //  2212: astore_3       
            //  2213: aload           10
            //  2215: invokevirtual   java/io/RandomAccessFile.close:()V
            //  2218: goto            2545
            //  2221: aload_1        
            //  2222: astore_3       
            //  2223: aload           17
            //  2225: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2228: ifne            2301
            //  2231: aload           9
            //  2233: ifnull          2239
            //  2236: goto            2301
            //  2239: iload           8
            //  2241: ifeq            2268
            //  2244: aload_1        
            //  2245: astore_3       
            //  2246: new             Lorg/telegram/messenger/secretmedia/EncryptedFileInputStream;
            //  2249: astore_2       
            //  2250: aload_1        
            //  2251: astore_3       
            //  2252: aload_2        
            //  2253: aload           7
            //  2255: aload_0        
            //  2256: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  2259: getfield        org/telegram/messenger/ImageLoader$CacheImage.encryptionKeyPath:Ljava/io/File;
            //  2262: invokespecial   org/telegram/messenger/secretmedia/EncryptedFileInputStream.<init>:(Ljava/io/File;Ljava/io/File;)V
            //  2265: goto            2280
            //  2268: aload_1        
            //  2269: astore_3       
            //  2270: new             Ljava/io/FileInputStream;
            //  2273: dup            
            //  2274: aload           7
            //  2276: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
            //  2279: astore_2       
            //  2280: aload_1        
            //  2281: astore_3       
            //  2282: aload_2        
            //  2283: aconst_null    
            //  2284: aload           17
            //  2286: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  2289: astore_1       
            //  2290: aload_1        
            //  2291: astore_3       
            //  2292: aload_2        
            //  2293: invokevirtual   java/io/FileInputStream.close:()V
            //  2296: aload_1        
            //  2297: astore_2       
            //  2298: goto            2545
            //  2301: aload_1        
            //  2302: astore_3       
            //  2303: new             Ljava/io/RandomAccessFile;
            //  2306: astore          23
            //  2308: aload_1        
            //  2309: astore_3       
            //  2310: aload           23
            //  2312: aload           7
            //  2314: ldc_w           "r"
            //  2317: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
            //  2320: aload_1        
            //  2321: astore_3       
            //  2322: aload           23
            //  2324: invokevirtual   java/io/RandomAccessFile.length:()J
            //  2327: l2i            
            //  2328: istore          15
            //  2330: aload_1        
            //  2331: astore_3       
            //  2332: invokestatic    org/telegram/messenger/ImageLoader.access$2100:()Ljava/lang/ThreadLocal;
            //  2335: invokevirtual   java/lang/ThreadLocal.get:()Ljava/lang/Object;
            //  2338: checkcast       [B
            //  2341: astore_2       
            //  2342: aload_2        
            //  2343: ifnull          2358
            //  2346: aload_1        
            //  2347: astore_3       
            //  2348: aload_2        
            //  2349: arraylength    
            //  2350: iload           15
            //  2352: if_icmplt       2358
            //  2355: goto            2360
            //  2358: aconst_null    
            //  2359: astore_2       
            //  2360: aload_2        
            //  2361: astore          11
            //  2363: aload_2        
            //  2364: ifnonnull       2385
            //  2367: aload_1        
            //  2368: astore_3       
            //  2369: iload           15
            //  2371: newarray        B
            //  2373: astore          11
            //  2375: aload_1        
            //  2376: astore_3       
            //  2377: invokestatic    org/telegram/messenger/ImageLoader.access$2100:()Ljava/lang/ThreadLocal;
            //  2380: aload           11
            //  2382: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
            //  2385: aload_1        
            //  2386: astore_3       
            //  2387: aload           23
            //  2389: aload           11
            //  2391: iconst_0       
            //  2392: iload           15
            //  2394: invokevirtual   java/io/RandomAccessFile.readFully:([BII)V
            //  2397: aload_1        
            //  2398: astore_3       
            //  2399: aload           23
            //  2401: invokevirtual   java/io/RandomAccessFile.close:()V
            //  2404: aload           9
            //  2406: ifnull          2488
            //  2409: aload_1        
            //  2410: astore_3       
            //  2411: aload           11
            //  2413: iconst_0       
            //  2414: iload           15
            //  2416: aload           9
            //  2418: invokestatic    org/telegram/messenger/secretmedia/EncryptedFileInputStream.decryptBytesWithKeyFile:([BIILorg/telegram/messenger/SecureDocumentKey;)V
            //  2421: aload_1        
            //  2422: astore_3       
            //  2423: aload           11
            //  2425: iconst_0       
            //  2426: iload           15
            //  2428: invokestatic    org/telegram/messenger/Utilities.computeSHA256:([BII)[B
            //  2431: astore_2       
            //  2432: aload           10
            //  2434: ifnull          2457
            //  2437: aload_1        
            //  2438: astore_3       
            //  2439: aload_2        
            //  2440: aload           10
            //  2442: invokestatic    java/util/Arrays.equals:([B[B)Z
            //  2445: ifne            2451
            //  2448: goto            2457
            //  2451: iconst_0       
            //  2452: istore          4
            //  2454: goto            2460
            //  2457: iconst_1       
            //  2458: istore          4
            //  2460: aload           11
            //  2462: iconst_0       
            //  2463: baload         
            //  2464: sipush          255
            //  2467: iand           
            //  2468: istore          13
            //  2470: iload           15
            //  2472: iload           13
            //  2474: isub           
            //  2475: istore          8
            //  2477: iload           4
            //  2479: istore          15
            //  2481: iload           8
            //  2483: istore          4
            //  2485: goto            2524
            //  2488: iload           8
            //  2490: ifeq            2510
            //  2493: aload_1        
            //  2494: astore_3       
            //  2495: aload           11
            //  2497: iconst_0       
            //  2498: iload           15
            //  2500: aload_0        
            //  2501: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  2504: getfield        org/telegram/messenger/ImageLoader$CacheImage.encryptionKeyPath:Ljava/io/File;
            //  2507: invokestatic    org/telegram/messenger/secretmedia/EncryptedFileInputStream.decryptBytesWithKeyFile:([BIILjava/io/File;)V
            //  2510: iconst_0       
            //  2511: istore          8
            //  2513: iconst_0       
            //  2514: istore          13
            //  2516: iload           15
            //  2518: istore          4
            //  2520: iload           8
            //  2522: istore          15
            //  2524: aload_1        
            //  2525: astore_2       
            //  2526: iload           15
            //  2528: ifne            2545
            //  2531: aload_1        
            //  2532: astore_3       
            //  2533: aload           11
            //  2535: iload           13
            //  2537: iload           4
            //  2539: aload           17
            //  2541: invokestatic    android/graphics/BitmapFactory.decodeByteArray:([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  2544: astore_2       
            //  2545: aload_2        
            //  2546: ifnonnull       2589
            //  2549: aload_2        
            //  2550: astore_3       
            //  2551: aload           7
            //  2553: invokevirtual   java/io/File.length:()J
            //  2556: lconst_0       
            //  2557: lcmp           
            //  2558: ifeq            2573
            //  2561: aload_2        
            //  2562: astore_3       
            //  2563: aload_0        
            //  2564: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  2567: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  2570: ifnonnull       2581
            //  2573: aload_2        
            //  2574: astore_3       
            //  2575: aload           7
            //  2577: invokevirtual   java/io/File.delete:()Z
            //  2580: pop            
            //  2581: iconst_0       
            //  2582: istore          12
            //  2584: aload_2        
            //  2585: astore_1       
            //  2586: goto            3106
            //  2589: aload_2        
            //  2590: astore_3       
            //  2591: aload_0        
            //  2592: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  2595: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  2598: ifnull          2696
            //  2601: aload_2        
            //  2602: astore_3       
            //  2603: aload_2        
            //  2604: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  2607: i2f            
            //  2608: fstore          20
            //  2610: aload_2        
            //  2611: astore_3       
            //  2612: aload_2        
            //  2613: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  2616: i2f            
            //  2617: fstore          19
            //  2619: aload_2        
            //  2620: astore_3       
            //  2621: aload           17
            //  2623: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2626: ifne            2696
            //  2629: fload           18
            //  2631: fconst_0       
            //  2632: fcmpl          
            //  2633: ifeq            2696
            //  2636: fload           20
            //  2638: fload           18
            //  2640: fcmpl          
            //  2641: ifeq            2696
            //  2644: fload           20
            //  2646: fload           18
            //  2648: ldc_w           20.0
            //  2651: fadd           
            //  2652: fcmpl          
            //  2653: ifle            2696
            //  2656: fload           20
            //  2658: fload           18
            //  2660: fdiv           
            //  2661: fstore          20
            //  2663: aload_2        
            //  2664: astore_3       
            //  2665: aload_2        
            //  2666: fload           18
            //  2668: f2i            
            //  2669: fload           19
            //  2671: fload           20
            //  2673: fdiv           
            //  2674: f2i            
            //  2675: iconst_1       
            //  2676: invokestatic    org/telegram/messenger/Bitmaps.createScaledBitmap:(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
            //  2679: astore_1       
            //  2680: aload_2        
            //  2681: aload_1        
            //  2682: if_acmpeq       2696
            //  2685: aload_2        
            //  2686: astore_3       
            //  2687: aload_2        
            //  2688: invokevirtual   android/graphics/Bitmap.recycle:()V
            //  2691: aload_1        
            //  2692: astore_2       
            //  2693: goto            2696
            //  2696: iload           21
            //  2698: ifeq            2759
            //  2701: aload           17
            //  2703: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2706: ifeq            2715
            //  2709: iconst_0       
            //  2710: istore          4
            //  2712: goto            2718
            //  2715: iconst_1       
            //  2716: istore          4
            //  2718: aload_2        
            //  2719: iload           4
            //  2721: aload_2        
            //  2722: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  2725: aload_2        
            //  2726: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  2729: aload_2        
            //  2730: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  2733: invokestatic    org/telegram/messenger/Utilities.needInvert:(Ljava/lang/Object;IIII)I
            //  2736: istore          4
            //  2738: iload           4
            //  2740: ifeq            2749
            //  2743: iconst_1       
            //  2744: istore          21
            //  2746: goto            2752
            //  2749: iconst_0       
            //  2750: istore          21
            //  2752: goto            2762
            //  2755: astore_1       
            //  2756: goto            3097
            //  2759: iconst_0       
            //  2760: istore          21
            //  2762: iload           14
            //  2764: iconst_1       
            //  2765: if_icmpne       2837
            //  2768: aload_2        
            //  2769: astore_1       
            //  2770: iload           21
            //  2772: istore          12
            //  2774: aload_2        
            //  2775: invokevirtual   android/graphics/Bitmap.getConfig:()Landroid/graphics/Bitmap$Config;
            //  2778: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  2781: if_acmpne       3106
            //  2784: aload           17
            //  2786: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2789: ifeq            2798
            //  2792: iconst_0       
            //  2793: istore          4
            //  2795: goto            2801
            //  2798: iconst_1       
            //  2799: istore          4
            //  2801: aload_2        
            //  2802: iconst_3       
            //  2803: iload           4
            //  2805: aload_2        
            //  2806: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  2809: aload_2        
            //  2810: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  2813: aload_2        
            //  2814: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  2817: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //  2820: aload_2        
            //  2821: astore_1       
            //  2822: iload           21
            //  2824: istore          12
            //  2826: goto            3106
            //  2829: astore_1       
            //  2830: iload           21
            //  2832: istore          12
            //  2834: goto            3100
            //  2837: iload           14
            //  2839: iconst_2       
            //  2840: if_icmpne       2904
            //  2843: aload_2        
            //  2844: astore_1       
            //  2845: iload           21
            //  2847: istore          12
            //  2849: aload_2        
            //  2850: invokevirtual   android/graphics/Bitmap.getConfig:()Landroid/graphics/Bitmap$Config;
            //  2853: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  2856: if_acmpne       3106
            //  2859: aload           17
            //  2861: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2864: ifeq            2873
            //  2867: iconst_0       
            //  2868: istore          4
            //  2870: goto            2876
            //  2873: iconst_1       
            //  2874: istore          4
            //  2876: aload_2        
            //  2877: iconst_1       
            //  2878: iload           4
            //  2880: aload_2        
            //  2881: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  2884: aload_2        
            //  2885: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  2888: aload_2        
            //  2889: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  2892: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //  2895: aload_2        
            //  2896: astore_1       
            //  2897: iload           21
            //  2899: istore          12
            //  2901: goto            3106
            //  2904: iload           14
            //  2906: iconst_3       
            //  2907: if_icmpne       3046
            //  2910: aload_2        
            //  2911: astore_1       
            //  2912: iload           21
            //  2914: istore          12
            //  2916: aload_2        
            //  2917: invokevirtual   android/graphics/Bitmap.getConfig:()Landroid/graphics/Bitmap$Config;
            //  2920: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  2923: if_acmpne       3106
            //  2926: aload           17
            //  2928: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2931: ifeq            2940
            //  2934: iconst_0       
            //  2935: istore          4
            //  2937: goto            2943
            //  2940: iconst_1       
            //  2941: istore          4
            //  2943: aload_2        
            //  2944: bipush          7
            //  2946: iload           4
            //  2948: aload_2        
            //  2949: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  2952: aload_2        
            //  2953: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  2956: aload_2        
            //  2957: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  2960: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //  2963: aload           17
            //  2965: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  2968: ifeq            2977
            //  2971: iconst_0       
            //  2972: istore          4
            //  2974: goto            2980
            //  2977: iconst_1       
            //  2978: istore          4
            //  2980: aload_2        
            //  2981: bipush          7
            //  2983: iload           4
            //  2985: aload_2        
            //  2986: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  2989: aload_2        
            //  2990: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  2993: aload_2        
            //  2994: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  2997: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //  3000: aload           17
            //  3002: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  3005: ifeq            3014
            //  3008: iconst_0       
            //  3009: istore          4
            //  3011: goto            3017
            //  3014: iconst_1       
            //  3015: istore          4
            //  3017: aload_2        
            //  3018: bipush          7
            //  3020: iload           4
            //  3022: aload_2        
            //  3023: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  3026: aload_2        
            //  3027: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  3030: aload_2        
            //  3031: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  3034: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //  3037: aload_2        
            //  3038: astore_1       
            //  3039: iload           21
            //  3041: istore          12
            //  3043: goto            3106
            //  3046: aload_2        
            //  3047: astore_1       
            //  3048: iload           21
            //  3050: istore          12
            //  3052: iload           14
            //  3054: ifne            3106
            //  3057: aload_2        
            //  3058: astore_1       
            //  3059: iload           21
            //  3061: istore          12
            //  3063: aload           17
            //  3065: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  3068: ifeq            3106
            //  3071: aload_2        
            //  3072: invokestatic    org/telegram/messenger/Utilities.pinBitmap:(Landroid/graphics/Bitmap;)I
            //  3075: pop            
            //  3076: aload_2        
            //  3077: astore_1       
            //  3078: iload           21
            //  3080: istore          12
            //  3082: goto            3106
            //  3085: astore          10
            //  3087: aload_2        
            //  3088: monitorexit    
            //  3089: aload_1        
            //  3090: astore_3       
            //  3091: aload           10
            //  3093: athrow         
            //  3094: astore_1       
            //  3095: aload_3        
            //  3096: astore_2       
            //  3097: iconst_0       
            //  3098: istore          12
            //  3100: aload_1        
            //  3101: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
            //  3104: aload_2        
            //  3105: astore_1       
            //  3106: iconst_0       
            //  3107: istore          4
            //  3109: aload_1        
            //  3110: astore_2       
            //  3111: goto            4438
            //  3114: bipush          20
            //  3116: istore          4
            //  3118: aload_3        
            //  3119: ifnull          3125
            //  3122: iconst_0       
            //  3123: istore          4
            //  3125: iload           4
            //  3127: ifeq            3194
            //  3130: aload_0        
            //  3131: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.this$0:Lorg/telegram/messenger/ImageLoader;
            //  3134: invokestatic    org/telegram/messenger/ImageLoader.access$2000:(Lorg/telegram/messenger/ImageLoader;)J
            //  3137: lconst_0       
            //  3138: lcmp           
            //  3139: ifeq            3194
            //  3142: aload_0        
            //  3143: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.this$0:Lorg/telegram/messenger/ImageLoader;
            //  3146: invokestatic    org/telegram/messenger/ImageLoader.access$2000:(Lorg/telegram/messenger/ImageLoader;)J
            //  3149: lstore          27
            //  3151: invokestatic    java/lang/System.currentTimeMillis:()J
            //  3154: lstore          5
            //  3156: iload           4
            //  3158: i2l            
            //  3159: lstore          29
            //  3161: lload           27
            //  3163: lload           5
            //  3165: lload           29
            //  3167: lsub           
            //  3168: lcmp           
            //  3169: ifle            3194
            //  3172: getstatic       android/os/Build$VERSION.SDK_INT:I
            //  3175: bipush          21
            //  3177: if_icmpge       3194
            //  3180: lload           29
            //  3182: invokestatic    java/lang/Thread.sleep:(J)V
            //  3185: goto            3194
            //  3188: astore_2       
            //  3189: aload_1        
            //  3190: astore_2       
            //  3191: goto            4432
            //  3194: aload_1        
            //  3195: astore_2       
            //  3196: aload_0        
            //  3197: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.this$0:Lorg/telegram/messenger/ImageLoader;
            //  3200: invokestatic    java/lang/System.currentTimeMillis:()J
            //  3203: invokestatic    org/telegram/messenger/ImageLoader.access$2002:(Lorg/telegram/messenger/ImageLoader;J)J
            //  3206: pop2           
            //  3207: aload_0        
            //  3208: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.sync:Ljava/lang/Object;
            //  3211: astore          23
            //  3213: aload           23
            //  3215: monitorenter   
            //  3216: aload_0        
            //  3217: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.isCancelled:Z
            //  3220: ifeq            3227
            //  3223: aload           23
            //  3225: monitorexit    
            //  3226: return         
            //  3227: aload           23
            //  3229: monitorexit    
            //  3230: iload           22
            //  3232: ifne            3277
            //  3235: aload_0        
            //  3236: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3239: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  3242: ifnull          3277
            //  3245: iload           14
            //  3247: ifne            3277
            //  3250: aload_0        
            //  3251: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3254: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //  3257: getfield        org/telegram/messenger/ImageLocation.path:Ljava/lang/String;
            //  3260: ifnull          3266
            //  3263: goto            3277
            //  3266: aload           17
            //  3268: getstatic       android/graphics/Bitmap$Config.RGB_565:Landroid/graphics/Bitmap$Config;
            //  3271: putfield        android/graphics/BitmapFactory$Options.inPreferredConfig:Landroid/graphics/Bitmap$Config;
            //  3274: goto            3285
            //  3277: aload           17
            //  3279: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  3282: putfield        android/graphics/BitmapFactory$Options.inPreferredConfig:Landroid/graphics/Bitmap$Config;
            //  3285: aload           17
            //  3287: iconst_0       
            //  3288: putfield        android/graphics/BitmapFactory$Options.inDither:Z
            //  3291: aload_3        
            //  3292: ifnull          3345
            //  3295: aload           11
            //  3297: ifnonnull       3345
            //  3300: iload           16
            //  3302: ifeq            3325
            //  3305: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
            //  3308: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
            //  3311: aload_3        
            //  3312: invokevirtual   java/lang/Long.longValue:()J
            //  3315: iconst_1       
            //  3316: aload           17
            //  3318: invokestatic    android/provider/MediaStore$Video$Thumbnails.getThumbnail:(Landroid/content/ContentResolver;JILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  3321: astore_2       
            //  3322: goto            3345
            //  3325: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
            //  3328: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
            //  3331: aload_3        
            //  3332: invokevirtual   java/lang/Long.longValue:()J
            //  3335: iconst_1       
            //  3336: aload           17
            //  3338: invokestatic    android/provider/MediaStore$Images$Thumbnails.getThumbnail:(Landroid/content/ContentResolver;JILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  3341: astore_2       
            //  3342: goto            3345
            //  3345: aload_2        
            //  3346: ifnonnull       3919
            //  3349: iload           13
            //  3351: ifeq            3519
            //  3354: aload_2        
            //  3355: astore_1       
            //  3356: new             Ljava/io/RandomAccessFile;
            //  3359: astore_3       
            //  3360: aload_2        
            //  3361: astore_1       
            //  3362: aload_3        
            //  3363: aload           7
            //  3365: ldc_w           "r"
            //  3368: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
            //  3371: aload_2        
            //  3372: astore_1       
            //  3373: aload_3        
            //  3374: invokevirtual   java/io/RandomAccessFile.getChannel:()Ljava/nio/channels/FileChannel;
            //  3377: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
            //  3380: lconst_0       
            //  3381: aload           7
            //  3383: invokevirtual   java/io/File.length:()J
            //  3386: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
            //  3389: astore          10
            //  3391: aload_2        
            //  3392: astore_1       
            //  3393: new             Landroid/graphics/BitmapFactory$Options;
            //  3396: astore          9
            //  3398: aload_2        
            //  3399: astore_1       
            //  3400: aload           9
            //  3402: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
            //  3405: aload_2        
            //  3406: astore_1       
            //  3407: aload           9
            //  3409: iconst_1       
            //  3410: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
            //  3413: aload_2        
            //  3414: astore_1       
            //  3415: aload           10
            //  3417: invokevirtual   java/nio/ByteBuffer.limit:()I
            //  3420: istore          4
            //  3422: aconst_null    
            //  3423: aload           10
            //  3425: iload           4
            //  3427: aload           9
            //  3429: iconst_1       
            //  3430: invokestatic    org/telegram/messenger/Utilities.loadWebpImage:(Landroid/graphics/Bitmap;Ljava/nio/ByteBuffer;ILandroid/graphics/BitmapFactory$Options;Z)Z
            //  3433: pop            
            //  3434: aload_2        
            //  3435: astore_1       
            //  3436: aload           9
            //  3438: getfield        android/graphics/BitmapFactory$Options.outWidth:I
            //  3441: aload           9
            //  3443: getfield        android/graphics/BitmapFactory$Options.outHeight:I
            //  3446: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  3449: invokestatic    org/telegram/messenger/Bitmaps.createBitmap:(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
            //  3452: astore_2       
            //  3453: aload_2        
            //  3454: astore_1       
            //  3455: aload           10
            //  3457: invokevirtual   java/nio/ByteBuffer.limit:()I
            //  3460: istore          4
            //  3462: aload_2        
            //  3463: astore_1       
            //  3464: aload           17
            //  3466: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  3469: istore          12
            //  3471: iload           12
            //  3473: ifne            3482
            //  3476: iconst_1       
            //  3477: istore          12
            //  3479: goto            3485
            //  3482: iconst_0       
            //  3483: istore          12
            //  3485: aload_2        
            //  3486: aload           10
            //  3488: iload           4
            //  3490: aconst_null    
            //  3491: iload           12
            //  3493: invokestatic    org/telegram/messenger/Utilities.loadWebpImage:(Landroid/graphics/Bitmap;Ljava/nio/ByteBuffer;ILandroid/graphics/BitmapFactory$Options;Z)Z
            //  3496: pop            
            //  3497: aload_2        
            //  3498: astore_1       
            //  3499: aload_3        
            //  3500: invokevirtual   java/io/RandomAccessFile.close:()V
            //  3503: iconst_0       
            //  3504: istore          4
            //  3506: goto            3926
            //  3509: astore_1       
            //  3510: goto            4432
            //  3513: astore_2       
            //  3514: aload_1        
            //  3515: astore_2       
            //  3516: goto            3191
            //  3519: aload           17
            //  3521: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  3524: istore          12
            //  3526: iload           12
            //  3528: ifne            3713
            //  3531: aload           9
            //  3533: ifnull          3539
            //  3536: goto            3713
            //  3539: iload           8
            //  3541: ifeq            3566
            //  3544: aload_2        
            //  3545: astore_1       
            //  3546: new             Lorg/telegram/messenger/secretmedia/EncryptedFileInputStream;
            //  3549: dup            
            //  3550: aload           7
            //  3552: aload_0        
            //  3553: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3556: getfield        org/telegram/messenger/ImageLoader$CacheImage.encryptionKeyPath:Ljava/io/File;
            //  3559: invokespecial   org/telegram/messenger/secretmedia/EncryptedFileInputStream.<init>:(Ljava/io/File;Ljava/io/File;)V
            //  3562: astore_3       
            //  3563: goto            3576
            //  3566: new             Ljava/io/FileInputStream;
            //  3569: dup            
            //  3570: aload           7
            //  3572: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
            //  3575: astore_3       
            //  3576: aload_0        
            //  3577: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3580: getfield        org/telegram/messenger/ImageLoader$CacheImage.imageLocation:Lorg/telegram/messenger/ImageLocation;
            //  3583: getfield        org/telegram/messenger/ImageLocation.document:Lorg/telegram/tgnet/TLRPC$Document;
            //  3586: instanceof      Lorg/telegram/tgnet/TLRPC$TL_document;
            //  3589: istore          12
            //  3591: iload           12
            //  3593: ifeq            3683
            //  3596: new             Landroidx/exifinterface/media/ExifInterface;
            //  3599: astore_1       
            //  3600: aload_1        
            //  3601: aload_3        
            //  3602: invokespecial   androidx/exifinterface/media/ExifInterface.<init>:(Ljava/io/InputStream;)V
            //  3605: aload_1        
            //  3606: ldc_w           "Orientation"
            //  3609: iconst_1       
            //  3610: invokevirtual   androidx/exifinterface/media/ExifInterface.getAttributeInt:(Ljava/lang/String;I)I
            //  3613: istore          4
            //  3615: iload           4
            //  3617: iconst_3       
            //  3618: if_icmpeq       3653
            //  3621: iload           4
            //  3623: bipush          6
            //  3625: if_icmpeq       3646
            //  3628: iload           4
            //  3630: bipush          8
            //  3632: if_icmpeq       3638
            //  3635: goto            3661
            //  3638: sipush          270
            //  3641: istore          4
            //  3643: goto            3664
            //  3646: bipush          90
            //  3648: istore          4
            //  3650: goto            3664
            //  3653: sipush          180
            //  3656: istore          4
            //  3658: goto            3664
            //  3661: iconst_0       
            //  3662: istore          4
            //  3664: aload_3        
            //  3665: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
            //  3668: lconst_0       
            //  3669: invokevirtual   java/nio/channels/FileChannel.position:(J)Ljava/nio/channels/FileChannel;
            //  3672: pop            
            //  3673: goto            3686
            //  3676: astore_1       
            //  3677: iconst_0       
            //  3678: istore          12
            //  3680: goto            4438
            //  3683: iconst_0       
            //  3684: istore          4
            //  3686: aload_2        
            //  3687: astore_1       
            //  3688: iload           4
            //  3690: istore          13
            //  3692: aload_3        
            //  3693: aconst_null    
            //  3694: aload           17
            //  3696: invokestatic    android/graphics/BitmapFactory.decodeStream:(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  3699: astore_2       
            //  3700: aload_2        
            //  3701: astore_1       
            //  3702: iload           4
            //  3704: istore          13
            //  3706: aload_3        
            //  3707: invokevirtual   java/io/FileInputStream.close:()V
            //  3710: goto            3926
            //  3713: new             Ljava/io/RandomAccessFile;
            //  3716: astore          11
            //  3718: aload           11
            //  3720: aload           7
            //  3722: ldc_w           "r"
            //  3725: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
            //  3728: aload           11
            //  3730: invokevirtual   java/io/RandomAccessFile.length:()J
            //  3733: l2i            
            //  3734: istore          13
            //  3736: invokestatic    org/telegram/messenger/ImageLoader.access$1600:()Ljava/lang/ThreadLocal;
            //  3739: invokevirtual   java/lang/ThreadLocal.get:()Ljava/lang/Object;
            //  3742: checkcast       [B
            //  3745: astore_1       
            //  3746: aload_1        
            //  3747: ifnull          3760
            //  3750: aload_1        
            //  3751: arraylength    
            //  3752: iload           13
            //  3754: if_icmplt       3760
            //  3757: goto            3762
            //  3760: aconst_null    
            //  3761: astore_1       
            //  3762: aload_1        
            //  3763: astore_3       
            //  3764: aload_1        
            //  3765: ifnonnull       3780
            //  3768: iload           13
            //  3770: newarray        B
            //  3772: astore_3       
            //  3773: invokestatic    org/telegram/messenger/ImageLoader.access$1600:()Ljava/lang/ThreadLocal;
            //  3776: aload_3        
            //  3777: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
            //  3780: aload           11
            //  3782: aload_3        
            //  3783: iconst_0       
            //  3784: iload           13
            //  3786: invokevirtual   java/io/RandomAccessFile.readFully:([BII)V
            //  3789: aload           11
            //  3791: invokevirtual   java/io/RandomAccessFile.close:()V
            //  3794: aload           9
            //  3796: ifnull          3869
            //  3799: aload_3        
            //  3800: iconst_0       
            //  3801: iload           13
            //  3803: aload           9
            //  3805: invokestatic    org/telegram/messenger/secretmedia/EncryptedFileInputStream.decryptBytesWithKeyFile:([BIILorg/telegram/messenger/SecureDocumentKey;)V
            //  3808: aload_3        
            //  3809: iconst_0       
            //  3810: iload           13
            //  3812: invokestatic    org/telegram/messenger/Utilities.computeSHA256:([BII)[B
            //  3815: astore_1       
            //  3816: aload           10
            //  3818: ifnull          3839
            //  3821: aload_1        
            //  3822: aload           10
            //  3824: invokestatic    java/util/Arrays.equals:([B[B)Z
            //  3827: ifne            3833
            //  3830: goto            3839
            //  3833: iconst_0       
            //  3834: istore          4
            //  3836: goto            3842
            //  3839: iconst_1       
            //  3840: istore          4
            //  3842: aload_3        
            //  3843: iconst_0       
            //  3844: baload         
            //  3845: sipush          255
            //  3848: iand           
            //  3849: istore          16
            //  3851: iload           13
            //  3853: iload           16
            //  3855: isub           
            //  3856: istore          13
            //  3858: iload           4
            //  3860: istore          8
            //  3862: iload           16
            //  3864: istore          4
            //  3866: goto            3894
            //  3869: iload           8
            //  3871: ifeq            3888
            //  3874: aload_3        
            //  3875: iconst_0       
            //  3876: iload           13
            //  3878: aload_0        
            //  3879: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3882: getfield        org/telegram/messenger/ImageLoader$CacheImage.encryptionKeyPath:Ljava/io/File;
            //  3885: invokestatic    org/telegram/messenger/secretmedia/EncryptedFileInputStream.decryptBytesWithKeyFile:([BIILjava/io/File;)V
            //  3888: iconst_0       
            //  3889: istore          8
            //  3891: iconst_0       
            //  3892: istore          4
            //  3894: aload_2        
            //  3895: astore_1       
            //  3896: iload           8
            //  3898: ifne            3921
            //  3901: aload_3        
            //  3902: iload           4
            //  3904: iload           13
            //  3906: aload           17
            //  3908: invokestatic    android/graphics/BitmapFactory.decodeByteArray:([BIILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //  3911: astore_1       
            //  3912: goto            3921
            //  3915: astore_1       
            //  3916: goto            4432
            //  3919: aload_2        
            //  3920: astore_1       
            //  3921: iconst_0       
            //  3922: istore          4
            //  3924: aload_1        
            //  3925: astore_2       
            //  3926: aload_2        
            //  3927: ifnonnull       3985
            //  3930: iload           15
            //  3932: ifeq            3979
            //  3935: aload_2        
            //  3936: astore_1       
            //  3937: iload           4
            //  3939: istore          13
            //  3941: aload           7
            //  3943: invokevirtual   java/io/File.length:()J
            //  3946: lconst_0       
            //  3947: lcmp           
            //  3948: ifeq            3967
            //  3951: aload_2        
            //  3952: astore_1       
            //  3953: iload           4
            //  3955: istore          13
            //  3957: aload_0        
            //  3958: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3961: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  3964: ifnonnull       3979
            //  3967: aload_2        
            //  3968: astore_1       
            //  3969: iload           4
            //  3971: istore          13
            //  3973: aload           7
            //  3975: invokevirtual   java/io/File.delete:()Z
            //  3978: pop            
            //  3979: iconst_0       
            //  3980: istore          12
            //  3982: goto            4411
            //  3985: aload_0        
            //  3986: getfield        org/telegram/messenger/ImageLoader$CacheOutTask.cacheImage:Lorg/telegram/messenger/ImageLoader$CacheImage;
            //  3989: getfield        org/telegram/messenger/ImageLoader$CacheImage.filter:Ljava/lang/String;
            //  3992: ifnull          4380
            //  3995: aload_2        
            //  3996: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  3999: i2f            
            //  4000: fstore          20
            //  4002: aload_2        
            //  4003: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  4006: i2f            
            //  4007: fstore          26
            //  4009: aload           17
            //  4011: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  4014: istore          12
            //  4016: iload           12
            //  4018: ifne            4147
            //  4021: fload           18
            //  4023: fconst_0       
            //  4024: fcmpl          
            //  4025: ifeq            4147
            //  4028: fload           20
            //  4030: fload           18
            //  4032: fcmpl          
            //  4033: ifeq            4147
            //  4036: fload           20
            //  4038: fload           18
            //  4040: ldc_w           20.0
            //  4043: fadd           
            //  4044: fcmpl          
            //  4045: ifle            4147
            //  4048: fload           20
            //  4050: fload           26
            //  4052: fcmpl          
            //  4053: ifle            4103
            //  4056: fload           18
            //  4058: fload           19
            //  4060: fcmpl          
            //  4061: ifle            4103
            //  4064: fload           20
            //  4066: fload           18
            //  4068: fdiv           
            //  4069: fstore          19
            //  4071: fload           18
            //  4073: f2i            
            //  4074: istore          15
            //  4076: fload           26
            //  4078: fload           19
            //  4080: fdiv           
            //  4081: f2i            
            //  4082: istore          8
            //  4084: aload_2        
            //  4085: astore_1       
            //  4086: iload           4
            //  4088: istore          13
            //  4090: aload_2        
            //  4091: iload           15
            //  4093: iload           8
            //  4095: iconst_1       
            //  4096: invokestatic    org/telegram/messenger/Bitmaps.createScaledBitmap:(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
            //  4099: astore_3       
            //  4100: goto            4127
            //  4103: aload_2        
            //  4104: astore_1       
            //  4105: iload           4
            //  4107: istore          13
            //  4109: aload_2        
            //  4110: fload           20
            //  4112: fload           26
            //  4114: fload           19
            //  4116: fdiv           
            //  4117: fdiv           
            //  4118: f2i            
            //  4119: fload           19
            //  4121: f2i            
            //  4122: iconst_1       
            //  4123: invokestatic    org/telegram/messenger/Bitmaps.createScaledBitmap:(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
            //  4126: astore_3       
            //  4127: aload_2        
            //  4128: aload_3        
            //  4129: if_acmpeq       4147
            //  4132: aload_2        
            //  4133: astore_1       
            //  4134: iload           4
            //  4136: istore          13
            //  4138: aload_2        
            //  4139: invokevirtual   android/graphics/Bitmap.recycle:()V
            //  4142: aload_3        
            //  4143: astore_2       
            //  4144: goto            4147
            //  4147: aload_2        
            //  4148: ifnull          4377
            //  4151: iload           21
            //  4153: ifeq            4273
            //  4156: aload_2        
            //  4157: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  4160: istore          15
            //  4162: aload_2        
            //  4163: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  4166: istore          13
            //  4168: iload           15
            //  4170: iload           13
            //  4172: imul           
            //  4173: sipush          22500
            //  4176: if_icmple       4192
            //  4179: aload_2        
            //  4180: bipush          100
            //  4182: bipush          100
            //  4184: iconst_0       
            //  4185: invokestatic    org/telegram/messenger/Bitmaps.createScaledBitmap:(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
            //  4188: astore_1       
            //  4189: goto            4194
            //  4192: aload_2        
            //  4193: astore_1       
            //  4194: aload           17
            //  4196: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  4199: ifeq            4208
            //  4202: iconst_0       
            //  4203: istore          13
            //  4205: goto            4211
            //  4208: iconst_1       
            //  4209: istore          13
            //  4211: aload_1        
            //  4212: iload           13
            //  4214: aload_1        
            //  4215: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  4218: aload_1        
            //  4219: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  4222: aload_1        
            //  4223: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  4226: invokestatic    org/telegram/messenger/Utilities.needInvert:(Ljava/lang/Object;IIII)I
            //  4229: istore          13
            //  4231: iload           13
            //  4233: ifeq            4242
            //  4236: iconst_1       
            //  4237: istore          21
            //  4239: goto            4245
            //  4242: iconst_0       
            //  4243: istore          21
            //  4245: iload           21
            //  4247: istore          12
            //  4249: aload_1        
            //  4250: aload_2        
            //  4251: if_acmpeq       4276
            //  4254: iload           21
            //  4256: istore          22
            //  4258: aload_1        
            //  4259: invokevirtual   android/graphics/Bitmap.recycle:()V
            //  4262: iload           21
            //  4264: istore          12
            //  4266: goto            4276
            //  4269: astore_1       
            //  4270: goto            4415
            //  4273: iconst_0       
            //  4274: istore          12
            //  4276: iconst_0       
            //  4277: istore          13
            //  4279: iload           14
            //  4281: ifeq            4374
            //  4284: fload           26
            //  4286: ldc_w           100.0
            //  4289: fcmpg          
            //  4290: ifge            4374
            //  4293: fload           20
            //  4295: ldc_w           100.0
            //  4298: fcmpg          
            //  4299: ifge            4374
            //  4302: iload           12
            //  4304: istore          22
            //  4306: aload_2        
            //  4307: invokevirtual   android/graphics/Bitmap.getConfig:()Landroid/graphics/Bitmap$Config;
            //  4310: getstatic       android/graphics/Bitmap$Config.ARGB_8888:Landroid/graphics/Bitmap$Config;
            //  4313: if_acmpne       4360
            //  4316: iload           12
            //  4318: istore          22
            //  4320: aload           17
            //  4322: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  4325: ifeq            4334
            //  4328: iconst_0       
            //  4329: istore          13
            //  4331: goto            4337
            //  4334: iconst_1       
            //  4335: istore          13
            //  4337: iload           12
            //  4339: istore          22
            //  4341: aload_2        
            //  4342: iconst_3       
            //  4343: iload           13
            //  4345: aload_2        
            //  4346: invokevirtual   android/graphics/Bitmap.getWidth:()I
            //  4349: aload_2        
            //  4350: invokevirtual   android/graphics/Bitmap.getHeight:()I
            //  4353: aload_2        
            //  4354: invokevirtual   android/graphics/Bitmap.getRowBytes:()I
            //  4357: invokestatic    org/telegram/messenger/Utilities.blurBitmap:(Ljava/lang/Object;IIIII)V
            //  4360: iconst_1       
            //  4361: istore          13
            //  4363: goto            4374
            //  4366: astore_1       
            //  4367: iload           22
            //  4369: istore          12
            //  4371: goto            4438
            //  4374: goto            4386
            //  4377: goto            4380
            //  4380: iconst_0       
            //  4381: istore          13
            //  4383: iconst_0       
            //  4384: istore          12
            //  4386: iload           13
            //  4388: ifne            4411
            //  4391: aload           17
            //  4393: getfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //  4396: ifeq            4411
            //  4399: aload_2        
            //  4400: invokestatic    org/telegram/messenger/Utilities.pinBitmap:(Landroid/graphics/Bitmap;)I
            //  4403: pop            
            //  4404: goto            4411
            //  4407: astore_1       
            //  4408: goto            4438
            //  4411: goto            4438
            //  4414: astore_1       
            //  4415: iconst_0       
            //  4416: istore          12
            //  4418: goto            4438
            //  4421: astore_1       
            //  4422: aload           23
            //  4424: monitorexit    
            //  4425: aload_1        
            //  4426: athrow         
            //  4427: astore_1       
            //  4428: goto            4422
            //  4431: astore_1       
            //  4432: iconst_0       
            //  4433: istore          12
            //  4435: iconst_0       
            //  4436: istore          4
            //  4438: invokestatic    java/lang/Thread.interrupted:()Z
            //  4441: pop            
            //  4442: iload           12
            //  4444: ifne            4481
            //  4447: iload           4
            //  4449: ifeq            4455
            //  4452: goto            4481
            //  4455: aload_2        
            //  4456: ifnull          4471
            //  4459: new             Landroid/graphics/drawable/BitmapDrawable;
            //  4462: dup            
            //  4463: aload_2        
            //  4464: invokespecial   android/graphics/drawable/BitmapDrawable.<init>:(Landroid/graphics/Bitmap;)V
            //  4467: astore_2       
            //  4468: goto            4473
            //  4471: aconst_null    
            //  4472: astore_2       
            //  4473: aload_0        
            //  4474: aload_2        
            //  4475: invokespecial   org/telegram/messenger/ImageLoader$CacheOutTask.onPostExecute:(Landroid/graphics/drawable/Drawable;)V
            //  4478: goto            4508
            //  4481: aload_2        
            //  4482: ifnull          4501
            //  4485: new             Lorg/telegram/messenger/ExtendedBitmapDrawable;
            //  4488: dup            
            //  4489: aload_2        
            //  4490: iload           12
            //  4492: iload           4
            //  4494: invokespecial   org/telegram/messenger/ExtendedBitmapDrawable.<init>:(Landroid/graphics/Bitmap;ZI)V
            //  4497: astore_2       
            //  4498: goto            4503
            //  4501: aconst_null    
            //  4502: astore_2       
            //  4503: aload_0        
            //  4504: aload_2        
            //  4505: invokespecial   org/telegram/messenger/ImageLoader$CacheOutTask.onPostExecute:(Landroid/graphics/drawable/Drawable;)V
            //  4508: return         
            //  4509: astore_2       
            //  4510: aload_1        
            //  4511: monitorexit    
            //  4512: aload_2        
            //  4513: athrow         
            //  4514: astore_2       
            //  4515: goto            3189
            //  4518: astore_1       
            //  4519: goto            3916
            //  4522: astore_1       
            //  4523: goto            3661
            //  4526: astore_2       
            //  4527: aload_1        
            //  4528: astore_2       
            //  4529: iload           13
            //  4531: istore          4
            //  4533: goto            4415
            //  4536: astore_1       
            //  4537: goto            4270
            //  4540: astore_1       
            //  4541: goto            4432
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  7      27     4509   4514   Any
            //  28     30     4509   4514   Any
            //  55     64     307    312    Any
            //  65     67     307    312    Any
            //  308    310    307    312    Any
            //  326    335    424    429    Any
            //  336    338    424    429    Any
            //  346    390    393    398    Ljava/lang/Throwable;
            //  425    427    424    429    Any
            //  443    452    593    598    Any
            //  453    455    593    598    Any
            //  594    596    593    598    Any
            //  719    732    880    883    Ljava/lang/Exception;
            //  719    732    874    880    Any
            //  734    745    870    874    Ljava/lang/Exception;
            //  734    745    917    918    Any
            //  747    751    870    874    Ljava/lang/Exception;
            //  747    751    917    918    Any
            //  756    760    870    874    Ljava/lang/Exception;
            //  756    760    917    918    Any
            //  762    770    870    874    Ljava/lang/Exception;
            //  762    770    917    918    Any
            //  772    777    870    874    Ljava/lang/Exception;
            //  772    777    917    918    Any
            //  779    785    870    874    Ljava/lang/Exception;
            //  779    785    917    918    Any
            //  787    796    870    874    Ljava/lang/Exception;
            //  787    796    917    918    Any
            //  798    808    870    874    Ljava/lang/Exception;
            //  798    808    917    918    Any
            //  810    819    870    874    Ljava/lang/Exception;
            //  810    819    917    918    Any
            //  835    839    862    870    Ljava/lang/Exception;
            //  835    839    917    918    Any
            //  843    847    850    862    Ljava/lang/Exception;
            //  890    894    917    918    Any
            //  906    910    850    862    Ljava/lang/Exception;
            //  922    926    929    934    Ljava/lang/Exception;
            //  1160   1203   1986   1987   Ljava/lang/Throwable;
            //  1210   1223   1233   1237   Ljava/lang/Throwable;
            //  1243   1259   1795   1801   Ljava/lang/Throwable;
            //  1265   1281   1795   1801   Ljava/lang/Throwable;
            //  1287   1301   1795   1801   Ljava/lang/Throwable;
            //  1315   1330   1778   1790   Ljava/lang/Throwable;
            //  1334   1350   1766   1771   Ljava/lang/Throwable;
            //  1376   1382   1766   1771   Ljava/lang/Throwable;
            //  1396   1403   1418   1422   Ljava/lang/Throwable;
            //  1403   1415   1606   1607   Ljava/lang/Throwable;
            //  1422   1439   1606   1607   Ljava/lang/Throwable;
            //  1447   1480   1606   1607   Ljava/lang/Throwable;
            //  1484   1491   1606   1607   Ljava/lang/Throwable;
            //  1502   1514   1606   1607   Ljava/lang/Throwable;
            //  1514   1545   1606   1607   Ljava/lang/Throwable;
            //  1550   1558   1606   1607   Ljava/lang/Throwable;
            //  1589   1603   1753   1757   Ljava/lang/Throwable;
            //  1615   1632   1753   1757   Ljava/lang/Throwable;
            //  1635   1645   1753   1757   Ljava/lang/Throwable;
            //  1645   1657   1753   1757   Ljava/lang/Throwable;
            //  1657   1673   1753   1757   Ljava/lang/Throwable;
            //  1689   1704   1753   1757   Ljava/lang/Throwable;
            //  1707   1722   1753   1757   Ljava/lang/Throwable;
            //  1736   1750   1753   1757   Ljava/lang/Throwable;
            //  1806   1812   1951   1955   Ljava/lang/Throwable;
            //  1817   1821   1951   1955   Ljava/lang/Throwable;
            //  1824   1828   1951   1955   Ljava/lang/Throwable;
            //  1828   1852   1951   1955   Ljava/lang/Throwable;
            //  1852   1894   1944   1951   Ljava/lang/Throwable;
            //  1932   1939   1944   1951   Ljava/lang/Throwable;
            //  2038   2049   3094   3097   Ljava/lang/Throwable;
            //  2051   2056   3094   3097   Ljava/lang/Throwable;
            //  2058   2060   3094   3097   Ljava/lang/Throwable;
            //  2060   2069   3085   3094   Any
            //  2070   2072   3085   3094   Any
            //  2079   2084   3094   3097   Ljava/lang/Throwable;
            //  2086   2096   3094   3097   Ljava/lang/Throwable;
            //  2098   2117   3094   3097   Ljava/lang/Throwable;
            //  2119   2123   3094   3097   Ljava/lang/Throwable;
            //  2125   2129   3094   3097   Ljava/lang/Throwable;
            //  2131   2136   3094   3097   Ljava/lang/Throwable;
            //  2138   2152   3094   3097   Ljava/lang/Throwable;
            //  2154   2169   3094   3097   Ljava/lang/Throwable;
            //  2171   2178   3094   3097   Ljava/lang/Throwable;
            //  2180   2188   3094   3097   Ljava/lang/Throwable;
            //  2199   2211   3094   3097   Ljava/lang/Throwable;
            //  2213   2218   3094   3097   Ljava/lang/Throwable;
            //  2223   2231   3094   3097   Ljava/lang/Throwable;
            //  2246   2250   3094   3097   Ljava/lang/Throwable;
            //  2252   2265   3094   3097   Ljava/lang/Throwable;
            //  2270   2280   3094   3097   Ljava/lang/Throwable;
            //  2282   2290   3094   3097   Ljava/lang/Throwable;
            //  2292   2296   3094   3097   Ljava/lang/Throwable;
            //  2303   2308   3094   3097   Ljava/lang/Throwable;
            //  2310   2320   3094   3097   Ljava/lang/Throwable;
            //  2322   2330   3094   3097   Ljava/lang/Throwable;
            //  2332   2342   3094   3097   Ljava/lang/Throwable;
            //  2348   2355   3094   3097   Ljava/lang/Throwable;
            //  2369   2375   3094   3097   Ljava/lang/Throwable;
            //  2377   2385   3094   3097   Ljava/lang/Throwable;
            //  2387   2397   3094   3097   Ljava/lang/Throwable;
            //  2399   2404   3094   3097   Ljava/lang/Throwable;
            //  2411   2421   3094   3097   Ljava/lang/Throwable;
            //  2423   2432   3094   3097   Ljava/lang/Throwable;
            //  2439   2448   3094   3097   Ljava/lang/Throwable;
            //  2495   2510   3094   3097   Ljava/lang/Throwable;
            //  2533   2545   3094   3097   Ljava/lang/Throwable;
            //  2551   2561   3094   3097   Ljava/lang/Throwable;
            //  2563   2573   3094   3097   Ljava/lang/Throwable;
            //  2575   2581   3094   3097   Ljava/lang/Throwable;
            //  2591   2601   3094   3097   Ljava/lang/Throwable;
            //  2603   2610   3094   3097   Ljava/lang/Throwable;
            //  2612   2619   3094   3097   Ljava/lang/Throwable;
            //  2621   2629   3094   3097   Ljava/lang/Throwable;
            //  2665   2680   3094   3097   Ljava/lang/Throwable;
            //  2687   2691   3094   3097   Ljava/lang/Throwable;
            //  2701   2709   2755   2759   Ljava/lang/Throwable;
            //  2718   2738   2755   2759   Ljava/lang/Throwable;
            //  2774   2792   2829   2837   Ljava/lang/Throwable;
            //  2801   2820   2829   2837   Ljava/lang/Throwable;
            //  2849   2867   2829   2837   Ljava/lang/Throwable;
            //  2876   2895   2829   2837   Ljava/lang/Throwable;
            //  2916   2934   2829   2837   Ljava/lang/Throwable;
            //  2943   2971   2829   2837   Ljava/lang/Throwable;
            //  2980   3008   2829   2837   Ljava/lang/Throwable;
            //  3017   3037   2829   2837   Ljava/lang/Throwable;
            //  3063   3076   2829   2837   Ljava/lang/Throwable;
            //  3087   3089   3085   3094   Any
            //  3091   3094   3094   3097   Ljava/lang/Throwable;
            //  3130   3156   3188   3189   Ljava/lang/Throwable;
            //  3172   3185   4514   4518   Ljava/lang/Throwable;
            //  3196   3216   4431   4432   Ljava/lang/Throwable;
            //  3216   3226   4421   4431   Any
            //  3227   3230   4421   4431   Any
            //  3235   3245   4514   4518   Ljava/lang/Throwable;
            //  3250   3263   4514   4518   Ljava/lang/Throwable;
            //  3266   3274   4514   4518   Ljava/lang/Throwable;
            //  3277   3285   4431   4432   Ljava/lang/Throwable;
            //  3285   3291   4431   4432   Ljava/lang/Throwable;
            //  3305   3322   4514   4518   Ljava/lang/Throwable;
            //  3325   3342   4514   4518   Ljava/lang/Throwable;
            //  3356   3360   3513   3519   Ljava/lang/Throwable;
            //  3362   3371   3513   3519   Ljava/lang/Throwable;
            //  3373   3391   3513   3519   Ljava/lang/Throwable;
            //  3393   3398   3513   3519   Ljava/lang/Throwable;
            //  3400   3405   3513   3519   Ljava/lang/Throwable;
            //  3407   3413   3513   3519   Ljava/lang/Throwable;
            //  3415   3422   3513   3519   Ljava/lang/Throwable;
            //  3422   3434   4518   4522   Ljava/lang/Throwable;
            //  3436   3453   3513   3519   Ljava/lang/Throwable;
            //  3455   3462   3513   3519   Ljava/lang/Throwable;
            //  3464   3471   3513   3519   Ljava/lang/Throwable;
            //  3485   3497   3509   3513   Ljava/lang/Throwable;
            //  3499   3503   3513   3519   Ljava/lang/Throwable;
            //  3519   3526   3915   3916   Ljava/lang/Throwable;
            //  3546   3563   3513   3519   Ljava/lang/Throwable;
            //  3566   3576   3915   3916   Ljava/lang/Throwable;
            //  3576   3591   3915   3916   Ljava/lang/Throwable;
            //  3596   3615   4522   4526   Ljava/lang/Throwable;
            //  3664   3673   3676   3683   Ljava/lang/Throwable;
            //  3692   3700   4526   4536   Ljava/lang/Throwable;
            //  3706   3710   4526   4536   Ljava/lang/Throwable;
            //  3713   3746   4518   4522   Ljava/lang/Throwable;
            //  3750   3757   4518   4522   Ljava/lang/Throwable;
            //  3768   3780   4518   4522   Ljava/lang/Throwable;
            //  3780   3794   4518   4522   Ljava/lang/Throwable;
            //  3799   3816   4518   4522   Ljava/lang/Throwable;
            //  3821   3830   4518   4522   Ljava/lang/Throwable;
            //  3874   3888   4518   4522   Ljava/lang/Throwable;
            //  3901   3912   4518   4522   Ljava/lang/Throwable;
            //  3941   3951   4526   4536   Ljava/lang/Throwable;
            //  3957   3967   4526   4536   Ljava/lang/Throwable;
            //  3973   3979   4526   4536   Ljava/lang/Throwable;
            //  3985   4016   4414   4415   Ljava/lang/Throwable;
            //  4090   4100   4526   4536   Ljava/lang/Throwable;
            //  4109   4127   4526   4536   Ljava/lang/Throwable;
            //  4138   4142   4526   4536   Ljava/lang/Throwable;
            //  4156   4168   4269   4270   Ljava/lang/Throwable;
            //  4179   4189   4536   4540   Ljava/lang/Throwable;
            //  4194   4202   4536   4540   Ljava/lang/Throwable;
            //  4211   4231   4536   4540   Ljava/lang/Throwable;
            //  4258   4262   4366   4374   Ljava/lang/Throwable;
            //  4306   4316   4366   4374   Ljava/lang/Throwable;
            //  4320   4328   4366   4374   Ljava/lang/Throwable;
            //  4341   4360   4366   4374   Ljava/lang/Throwable;
            //  4391   4404   4407   4411   Ljava/lang/Throwable;
            //  4422   4425   4427   4431   Any
            //  4425   4427   4540   4544   Ljava/lang/Throwable;
            //  4510   4512   4509   4514   Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 2396 out-of-bounds for length 2396
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
    }
    
    private class HttpFileTask extends AsyncTask<Void, Void, Boolean>
    {
        private boolean canRetry;
        private int currentAccount;
        private String ext;
        private RandomAccessFile fileOutputStream;
        private int fileSize;
        private long lastProgressTime;
        private File tempFile;
        private String url;
        
        public HttpFileTask(final String url, final File tempFile, final String ext, final int currentAccount) {
            this.fileOutputStream = null;
            this.canRetry = true;
            this.url = url;
            this.tempFile = tempFile;
            this.ext = ext;
            this.currentAccount = currentAccount;
        }
        
        private void reportProgress(final float n) {
            final long currentTimeMillis = System.currentTimeMillis();
            if (n != 1.0f) {
                final long lastProgressTime = this.lastProgressTime;
                if (lastProgressTime != 0L && lastProgressTime >= currentTimeMillis - 500L) {
                    return;
                }
            }
            this.lastProgressTime = currentTimeMillis;
            Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpFileTask$CbdoQhu0HscXntXREbdZu5bUbuA(this, n));
        }
        
        protected Boolean doInBackground(Void... array) {
            final boolean b = true;
            final boolean b2 = true;
            boolean b3 = false;
            boolean b4 = false;
            final boolean b5 = false;
            Object o = null;
            Object inputStream;
            try {
                final URLConnection urlConnection = (URLConnection)(o = new URL(this.url).openConnection());
                try {
                    urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                    o = urlConnection;
                    urlConnection.setConnectTimeout(5000);
                    o = urlConnection;
                    urlConnection.setReadTimeout(5000);
                    array = (Void[])(Object)urlConnection;
                    o = urlConnection;
                    Label_0218: {
                        if (urlConnection instanceof HttpURLConnection) {
                            o = urlConnection;
                            final HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
                            o = urlConnection;
                            httpURLConnection.setInstanceFollowRedirects(true);
                            o = urlConnection;
                            final int responseCode = httpURLConnection.getResponseCode();
                            if (responseCode != 302 && responseCode != 301) {
                                array = (Void[])(Object)urlConnection;
                                if (responseCode != 303) {
                                    break Label_0218;
                                }
                            }
                            o = urlConnection;
                            final String headerField = httpURLConnection.getHeaderField("Location");
                            o = urlConnection;
                            final String headerField2 = httpURLConnection.getHeaderField("Set-Cookie");
                            o = urlConnection;
                            o = urlConnection;
                            final URL url = new URL(headerField);
                            o = urlConnection;
                            array = (Void[])(o = url.openConnection());
                            ((URLConnection)(Object)array).setRequestProperty("Cookie", headerField2);
                            o = array;
                            ((URLConnection)(Object)array).addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                        }
                    }
                    o = array;
                    ((URLConnection)(Object)array).connect();
                    o = array;
                    inputStream = ((URLConnection)(Object)array).getInputStream();
                    try {
                        this.fileOutputStream = new RandomAccessFile(this.tempFile, "rws");
                        final Object o2 = inputStream;
                    }
                    catch (Throwable o) {}
                }
                catch (Throwable t) {
                    inputStream = null;
                    array = (Void[])o;
                    o = t;
                }
            }
            catch (Throwable o) {
                inputStream = (array = null);
            }
            if (o instanceof SocketTimeoutException) {
                if (ApplicationLoader.isNetworkOnline()) {
                    this.canRetry = false;
                }
            }
            else if (o instanceof UnknownHostException) {
                this.canRetry = false;
            }
            else if (o instanceof SocketException) {
                if (((Throwable)o).getMessage() != null && ((Throwable)o).getMessage().contains("ECONNRESET")) {
                    this.canRetry = false;
                }
            }
            else if (o instanceof FileNotFoundException) {
                this.canRetry = false;
            }
            FileLog.e((Throwable)o);
            final Object o2 = inputStream;
            if (this.canRetry) {
                try {
                    if (array instanceof HttpURLConnection) {
                        final int responseCode2 = ((HttpURLConnection)(Object)array).getResponseCode();
                        if (responseCode2 != 200 && responseCode2 != 202 && responseCode2 != 304) {
                            this.canRetry = false;
                        }
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                if (array != null) {
                    try {
                        final Map<String, List<String>> headerFields = ((URLConnection)(Object)array).getHeaderFields();
                        if (headerFields != null) {
                            final List<String> list = headerFields.get("content-Length");
                            if (list != null && !list.isEmpty()) {
                                final String s = list.get(0);
                                if (s != null) {
                                    this.fileSize = Utilities.parseInt(s);
                                }
                            }
                        }
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
                if (o2 != null) {
                    Label_0678: {
                        try {
                            final byte[] array2 = new byte[32768];
                            int n = 0;
                            Label_0666: {
                                while (true) {
                                    Label_0652: {
                                        if (this.isCancelled()) {
                                            break Label_0652;
                                        }
                                        try {
                                            final int read = ((InputStream)o2).read(array2);
                                            if (read <= 0) {
                                                if (read == -1) {
                                                    b3 = b;
                                                    try {
                                                        if (this.fileSize != 0) {
                                                            this.reportProgress(1.0f);
                                                            b3 = b;
                                                            break Label_0666;
                                                        }
                                                        break Label_0666;
                                                    }
                                                    catch (Throwable t2) {
                                                        b3 = true;
                                                        break Label_0678;
                                                    }
                                                    catch (Exception ex3) {
                                                        b3 = b2;
                                                        break;
                                                    }
                                                }
                                                b3 = false;
                                                break Label_0666;
                                            }
                                            this.fileOutputStream.write(array2, 0, read);
                                            final int n2 = n += read;
                                            if (this.fileSize > 0) {
                                                this.reportProgress(n2 / (float)this.fileSize);
                                                n = n2;
                                                continue;
                                            }
                                            continue;
                                        }
                                        catch (Exception ex3) {
                                            b3 = false;
                                        }
                                    }
                                    break;
                                }
                                try {
                                    final Exception ex3;
                                    FileLog.e(ex3);
                                }
                                catch (Throwable t2) {}
                            }
                        }
                        catch (Throwable t2) {
                            b3 = b5;
                        }
                    }
                    final Throwable t2;
                    FileLog.e(t2);
                }
                try {
                    if (this.fileOutputStream != null) {
                        this.fileOutputStream.close();
                        this.fileOutputStream = null;
                    }
                }
                catch (Throwable t3) {
                    FileLog.e(t3);
                }
                b4 = b3;
                if (o2 != null) {
                    try {
                        ((InputStream)o2).close();
                        b4 = b3;
                    }
                    catch (Throwable t4) {
                        FileLog.e(t4);
                        b4 = b3;
                    }
                }
            }
            return b4;
        }
        
        protected void onCancelled() {
            ImageLoader.this.runHttpFileLoadTasks(this, 2);
        }
        
        protected void onPostExecute(final Boolean b) {
            final ImageLoader this$0 = ImageLoader.this;
            int n;
            if (b) {
                n = 2;
            }
            else {
                n = 1;
            }
            this$0.runHttpFileLoadTasks(this, n);
        }
    }
    
    private class HttpImageTask extends AsyncTask<Void, Void, Boolean>
    {
        private CacheImage cacheImage;
        private boolean canRetry;
        private RandomAccessFile fileOutputStream;
        private HttpURLConnection httpConnection;
        private int imageSize;
        private long lastProgressTime;
        private String overrideUrl;
        
        public HttpImageTask(final CacheImage cacheImage, final int imageSize) {
            this.canRetry = true;
            this.cacheImage = cacheImage;
            this.imageSize = imageSize;
        }
        
        public HttpImageTask(final CacheImage cacheImage, final int imageSize, final String overrideUrl) {
            this.canRetry = true;
            this.cacheImage = cacheImage;
            this.imageSize = imageSize;
            this.overrideUrl = overrideUrl;
        }
        
        private void reportProgress(final float n) {
            final long currentTimeMillis = System.currentTimeMillis();
            if (n != 1.0f) {
                final long lastProgressTime = this.lastProgressTime;
                if (lastProgressTime != 0L && lastProgressTime >= currentTimeMillis - 500L) {
                    return;
                }
            }
            this.lastProgressTime = currentTimeMillis;
            Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$WWTxHtUw7_WIiuq5bKLqtQ8BNBI(this, n));
        }
        
        protected Boolean doInBackground(Void... t) {
            final boolean cancelled = this.isCancelled();
            final boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            Label_0376: {
                Label_0374: {
                    if (!cancelled) {
                        try {
                            String s = this.cacheImage.imageLocation.path;
                            if (s.startsWith("https://static-maps") || s.startsWith("https://maps.googleapis")) {
                                final int mapProvider = MessagesController.getInstance(this.cacheImage.currentAccount).mapProvider;
                                if (mapProvider == 3 || mapProvider == 4) {
                                    final WebFile webFile = ImageLoader.this.testWebFile.get(s);
                                    if (webFile != null) {
                                        final TLRPC.TL_upload_getWebFile tl_upload_getWebFile = new TLRPC.TL_upload_getWebFile();
                                        tl_upload_getWebFile.location = webFile.location;
                                        tl_upload_getWebFile.offset = 0;
                                        tl_upload_getWebFile.limit = 0;
                                        ConnectionsManager.getInstance(this.cacheImage.currentAccount).sendRequest(tl_upload_getWebFile, (RequestDelegate)_$$Lambda$ImageLoader$HttpImageTask$T115Ddi3sI3XyS3851ENmLig_I8.INSTANCE);
                                    }
                                }
                            }
                            if (this.overrideUrl != null) {
                                s = this.overrideUrl;
                            }
                            (this.httpConnection = (HttpURLConnection)new URL(s).openConnection()).addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1");
                            this.httpConnection.setConnectTimeout(5000);
                            this.httpConnection.setReadTimeout(5000);
                            this.httpConnection.setInstanceFollowRedirects(true);
                            if (this.isCancelled()) {
                                break Label_0374;
                            }
                            this.httpConnection.connect();
                            t = (Throwable)this.httpConnection.getInputStream();
                            try {
                                this.fileOutputStream = new RandomAccessFile(this.cacheImage.tempFilePath, "rws");
                            }
                            catch (Throwable t2) {}
                        }
                        catch (Throwable t2) {
                            t = null;
                        }
                        final Throwable t2;
                        if (t2 instanceof SocketTimeoutException) {
                            if (ApplicationLoader.isNetworkOnline()) {
                                this.canRetry = false;
                            }
                        }
                        else if (t2 instanceof UnknownHostException) {
                            this.canRetry = false;
                        }
                        else if (t2 instanceof SocketException) {
                            if (t2.getMessage() != null && t2.getMessage().contains("ECONNRESET")) {
                                this.canRetry = false;
                            }
                        }
                        else if (t2 instanceof FileNotFoundException) {
                            this.canRetry = false;
                        }
                        FileLog.e(t2);
                        break Label_0376;
                    }
                }
                t = null;
            }
            boolean b4 = b2;
            Label_0719: {
                if (!this.isCancelled()) {
                    try {
                        if (this.httpConnection != null) {
                            final int responseCode = this.httpConnection.getResponseCode();
                            if (responseCode != 200 && responseCode != 202 && responseCode != 304) {
                                this.canRetry = false;
                            }
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    if (this.imageSize == 0) {
                        final HttpURLConnection httpConnection = this.httpConnection;
                        if (httpConnection != null) {
                            try {
                                final Map<String, List<String>> headerFields = httpConnection.getHeaderFields();
                                if (headerFields != null) {
                                    final List<String> list = headerFields.get("content-Length");
                                    if (list != null && !list.isEmpty()) {
                                        final String s2 = list.get(0);
                                        if (s2 != null) {
                                            this.imageSize = Utilities.parseInt(s2);
                                        }
                                    }
                                }
                            }
                            catch (Exception ex2) {
                                FileLog.e(ex2);
                            }
                        }
                    }
                    b4 = b2;
                    if (t != null) {
                        boolean b5 = b;
                        try {
                            final byte[] array = new byte[8192];
                            int n = 0;
                            while (true) {
                                b5 = b;
                                if (this.isCancelled()) {
                                    b4 = b2;
                                    break Label_0719;
                                }
                                b5 = b;
                                try {
                                    final int read = ((InputStream)t).read(array);
                                    if (read > 0) {
                                        final int n2 = n + read;
                                        b5 = b;
                                        this.fileOutputStream.write(array, 0, read);
                                        n = n2;
                                        b5 = b;
                                        if (this.imageSize != 0) {
                                            b5 = b;
                                            this.reportProgress(n2 / (float)this.imageSize);
                                            n = n2;
                                            continue;
                                        }
                                        continue;
                                    }
                                    else {
                                        b4 = b2;
                                        if (read != -1) {
                                            break Label_0719;
                                        }
                                        try {
                                            if (this.imageSize != 0) {
                                                this.reportProgress(1.0f);
                                            }
                                            b4 = true;
                                        }
                                        catch (Throwable t3) {
                                            b4 = true;
                                        }
                                        catch (Exception ex3) {
                                            b4 = true;
                                        }
                                    }
                                }
                                catch (Exception ex3) {
                                    b4 = b3;
                                }
                                b5 = b4;
                                final Exception ex3;
                                FileLog.e(ex3);
                                break Label_0719;
                            }
                        }
                        catch (Throwable t3) {
                            b4 = b5;
                        }
                        final Throwable t3;
                        FileLog.e(t3);
                    }
                }
                try {
                    if (this.fileOutputStream != null) {
                        this.fileOutputStream.close();
                        this.fileOutputStream = null;
                    }
                }
                catch (Throwable t4) {
                    FileLog.e(t4);
                }
            }
            while (true) {
                try {
                    if (this.httpConnection != null) {
                        this.httpConnection.disconnect();
                    }
                    if (t != null) {
                        try {
                            ((InputStream)t).close();
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                        }
                    }
                    if (b4) {
                        final CacheImage cacheImage = this.cacheImage;
                        t = (Throwable)cacheImage.tempFilePath;
                        if (t != null && !((File)t).renameTo(cacheImage.finalFilePath)) {
                            t = (Throwable)this.cacheImage;
                            ((CacheImage)t).finalFilePath = ((CacheImage)t).tempFilePath;
                        }
                    }
                    return b4;
                }
                catch (Throwable t5) {
                    continue;
                }
                break;
            }
        }
        
        protected void onCancelled() {
            ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$hp6Qb_emVVm8eUAVBNZgyYyMaoI(this));
            Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$a_vnJl4U3DAZgDJvr8vdGGH5i2s(this));
        }
        
        protected void onPostExecute(final Boolean b) {
            if (!b && this.canRetry) {
                ImageLoader.this.httpFileLoadError(this.cacheImage.url);
            }
            else {
                final ImageLoader this$0 = ImageLoader.this;
                final CacheImage cacheImage = this.cacheImage;
                this$0.fileDidLoaded(cacheImage.url, cacheImage.finalFilePath, 0);
            }
            Utilities.stageQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$SfPPeQgJq15qYgHfn_IXbR_DnQ0(this, b));
            ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$HttpImageTask$Z2AJLo51Bz13ruMq5jl4ihFrKyc(this));
        }
    }
    
    private class ThumbGenerateInfo
    {
        private boolean big;
        private String filter;
        private ArrayList<ImageReceiver> imageReceiverArray;
        private TLRPC.Document parentDocument;
        
        private ThumbGenerateInfo() {
            this.imageReceiverArray = new ArrayList<ImageReceiver>();
        }
    }
    
    private class ThumbGenerateTask implements Runnable
    {
        private ThumbGenerateInfo info;
        private int mediaType;
        private File originalPath;
        
        public ThumbGenerateTask(final int mediaType, final File originalPath, final ThumbGenerateInfo info) {
            this.mediaType = mediaType;
            this.originalPath = originalPath;
            this.info = info;
        }
        
        private void removeTask() {
            final ThumbGenerateInfo info = this.info;
            if (info == null) {
                return;
            }
            ImageLoader.this.imageLoadQueue.postRunnable(new _$$Lambda$ImageLoader$ThumbGenerateTask$wNtiv0w_w5JLAy5lgehk_MfB6UY(this, FileLoader.getAttachFileName(info.parentDocument)));
        }
        
        @Override
        public void run() {
            try {
                if (this.info == null) {
                    this.removeTask();
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("q_");
                sb.append(this.info.parentDocument.dc_id);
                sb.append("_");
                sb.append(this.info.parentDocument.id);
                final String string = sb.toString();
                final File directory = FileLoader.getDirectory(4);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(".jpg");
                final File file = new File(directory, sb2.toString());
                if (file.exists() || !this.originalPath.exists()) {
                    this.removeTask();
                    return;
                }
                int n;
                if (this.info.big) {
                    n = Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y);
                }
                else {
                    n = Math.min(180, Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) / 4);
                }
                final int mediaType = this.mediaType;
                final Bitmap bitmap = null;
                Bitmap bitmap2 = null;
                Label_0402: {
                    if (mediaType == 0) {
                        final String string2 = this.originalPath.toString();
                        final float n2 = (float)n;
                        bitmap2 = ImageLoader.loadBitmap(string2, null, n2, n2, false);
                    }
                    else {
                        final int mediaType2 = this.mediaType;
                        int n3 = 2;
                        if (mediaType2 == 2) {
                            final String string3 = this.originalPath.toString();
                            if (!this.info.big) {
                                n3 = 1;
                            }
                            bitmap2 = ThumbnailUtils.createVideoThumbnail(string3, n3);
                        }
                        else {
                            bitmap2 = bitmap;
                            if (this.mediaType == 3) {
                                final String lowerCase = this.originalPath.toString().toLowerCase();
                                if (lowerCase.endsWith("mp4")) {
                                    final String string4 = this.originalPath.toString();
                                    if (!this.info.big) {
                                        n3 = 1;
                                    }
                                    bitmap2 = ThumbnailUtils.createVideoThumbnail(string4, n3);
                                }
                                else {
                                    if (!lowerCase.endsWith(".jpg") && !lowerCase.endsWith(".jpeg") && !lowerCase.endsWith(".png")) {
                                        bitmap2 = bitmap;
                                        if (!lowerCase.endsWith(".gif")) {
                                            break Label_0402;
                                        }
                                    }
                                    final float n4 = (float)n;
                                    bitmap2 = ImageLoader.loadBitmap(lowerCase, null, n4, n4, false);
                                }
                            }
                        }
                    }
                }
                if (bitmap2 == null) {
                    this.removeTask();
                    return;
                }
                final int width = bitmap2.getWidth();
                final int height = bitmap2.getHeight();
                if (width == 0 || height == 0) {
                    this.removeTask();
                    return;
                }
                final float n5 = (float)width;
                final float n6 = (float)n;
                final float a = n5 / n6;
                final float n7 = (float)height;
                final float min = Math.min(a, n7 / n6);
                if (min > 1.0f) {
                    final Bitmap scaledBitmap = Bitmaps.createScaledBitmap(bitmap2, (int)(n5 / min), (int)(n7 / min), true);
                    if (scaledBitmap != bitmap2) {
                        bitmap2.recycle();
                        bitmap2 = scaledBitmap;
                    }
                }
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                final Bitmap$CompressFormat jpeg = Bitmap$CompressFormat.JPEG;
                int n8;
                if (this.info.big) {
                    n8 = 83;
                }
                else {
                    n8 = 60;
                }
                bitmap2.compress(jpeg, n8, (OutputStream)fileOutputStream);
                try {
                    fileOutputStream.close();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                AndroidUtilities.runOnUIThread(new _$$Lambda$ImageLoader$ThumbGenerateTask$93c40_AxUp0yfQKw5ZLfHAidaSg(this, string, new ArrayList(this.info.imageReceiverArray), new BitmapDrawable(bitmap2)));
            }
            catch (Throwable t) {
                FileLog.e(t);
                this.removeTask();
            }
        }
    }
}
