// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.database.Cursor;
import java.io.FileNotFoundException;
import android.os.ParcelFileDescriptor;
import android.content.ContentValues;
import android.net.Uri;
import java.util.HashSet;
import java.util.HashMap;
import android.content.UriMatcher;
import android.content.ContentProvider;

public class NotificationImageProvider extends ContentProvider implements NotificationCenterDelegate
{
    public static final String AUTHORITY = "org.telegram.messenger.notification_image_provider";
    private static final UriMatcher matcher;
    private HashMap<String, Long> fileStartTimes;
    private final Object sync;
    private HashSet<String> waitingForFiles;
    
    static {
        (matcher = new UriMatcher(-1)).addURI("org.telegram.messenger.notification_image_provider", "msg_media_raw/#/*", 1);
    }
    
    public NotificationImageProvider() {
        this.waitingForFiles = new HashSet<String>();
        this.sync = new Object();
        this.fileStartTimes = new HashMap<String, Long>();
    }
    
    public int delete(final Uri uri, final String s, final String[] array) {
        return 0;
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.fileDidLoad) {
            synchronized (this.sync) {
                final String s = (String)array[0];
                if (this.waitingForFiles.remove(s)) {
                    this.fileStartTimes.remove(s);
                    this.sync.notifyAll();
                }
            }
        }
    }
    
    public String[] getStreamTypes(final Uri uri, final String s) {
        if (!s.startsWith("*/") && !s.startsWith("image/")) {
            return null;
        }
        return new String[] { "image/jpeg", "image/png", "image/webp" };
    }
    
    public String getType(final Uri uri) {
        return null;
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        return null;
    }
    
    public boolean onCreate() {
        for (int i = 0; i < UserConfig.getActivatedAccountsCount(); ++i) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileDidLoad);
        }
        return true;
    }
    
    public ParcelFileDescriptor openFile(final Uri p0, final String p1) throws FileNotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: aload_2        
        //     3: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //     6: ifeq            316
        //     9: getstatic       org/telegram/messenger/NotificationImageProvider.matcher:Landroid/content/UriMatcher;
        //    12: aload_1        
        //    13: invokevirtual   android/content/UriMatcher.match:(Landroid/net/Uri;)I
        //    16: iconst_1       
        //    17: if_icmpne       306
        //    20: aload_1        
        //    21: invokevirtual   android/net/Uri.getPathSegments:()Ljava/util/List;
        //    24: astore_2       
        //    25: aload_2        
        //    26: iconst_1       
        //    27: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    32: checkcast       Ljava/lang/String;
        //    35: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    38: pop            
        //    39: aload_2        
        //    40: iconst_2       
        //    41: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //    46: checkcast       Ljava/lang/String;
        //    49: astore_2       
        //    50: aload_1        
        //    51: ldc             "final_path"
        //    53: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //    56: astore_3       
        //    57: aload_1        
        //    58: ldc             "fallback"
        //    60: invokevirtual   android/net/Uri.getQueryParameter:(Ljava/lang/String;)Ljava/lang/String;
        //    63: astore_1       
        //    64: new             Ljava/io/File;
        //    67: dup            
        //    68: aload_3        
        //    69: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    72: astore_3       
        //    73: aload_3        
        //    74: invokevirtual   java/io/File.exists:()Z
        //    77: ifeq            115
        //    80: new             Ljava/lang/StringBuilder;
        //    83: dup            
        //    84: invokespecial   java/lang/StringBuilder.<init>:()V
        //    87: astore_1       
        //    88: aload_1        
        //    89: aload_3        
        //    90: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    93: pop            
        //    94: aload_1        
        //    95: ldc             " already exists"
        //    97: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   100: pop            
        //   101: aload_1        
        //   102: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   105: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   108: aload_3        
        //   109: ldc             268435456
        //   111: invokestatic    android/os/ParcelFileDescriptor.open:(Ljava/io/File;I)Landroid/os/ParcelFileDescriptor;
        //   114: areturn        
        //   115: aload_0        
        //   116: getfield        org/telegram/messenger/NotificationImageProvider.fileStartTimes:Ljava/util/HashMap;
        //   119: aload_2        
        //   120: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   123: checkcast       Ljava/lang/Long;
        //   126: astore          4
        //   128: aload           4
        //   130: ifnull          143
        //   133: aload           4
        //   135: invokevirtual   java/lang/Long.longValue:()J
        //   138: lstore          5
        //   140: goto            148
        //   143: invokestatic    java/lang/System.currentTimeMillis:()J
        //   146: lstore          5
        //   148: aload           4
        //   150: ifnonnull       167
        //   153: aload_0        
        //   154: getfield        org/telegram/messenger/NotificationImageProvider.fileStartTimes:Ljava/util/HashMap;
        //   157: aload_2        
        //   158: lload           5
        //   160: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   163: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   166: pop            
        //   167: aload_3        
        //   168: invokevirtual   java/io/File.exists:()Z
        //   171: ifne            299
        //   174: invokestatic    java/lang/System.currentTimeMillis:()J
        //   177: lload           5
        //   179: lsub           
        //   180: ldc2_w          3000
        //   183: lcmp           
        //   184: iflt            259
        //   187: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   190: ifeq            228
        //   193: new             Ljava/lang/StringBuilder;
        //   196: dup            
        //   197: invokespecial   java/lang/StringBuilder.<init>:()V
        //   200: astore_3       
        //   201: aload_3        
        //   202: ldc             "Waiting for "
        //   204: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   207: pop            
        //   208: aload_3        
        //   209: aload_2        
        //   210: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   213: pop            
        //   214: aload_3        
        //   215: ldc             " to download timed out"
        //   217: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   220: pop            
        //   221: aload_3        
        //   222: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   225: invokestatic    org/telegram/messenger/FileLog.w:(Ljava/lang/String;)V
        //   228: aload_1        
        //   229: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   232: ifne            249
        //   235: new             Ljava/io/File;
        //   238: dup            
        //   239: aload_1        
        //   240: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   243: ldc             268435456
        //   245: invokestatic    android/os/ParcelFileDescriptor.open:(Ljava/io/File;I)Landroid/os/ParcelFileDescriptor;
        //   248: areturn        
        //   249: new             Ljava/io/FileNotFoundException;
        //   252: dup            
        //   253: ldc             "Download timed out"
        //   255: invokespecial   java/io/FileNotFoundException.<init>:(Ljava/lang/String;)V
        //   258: athrow         
        //   259: aload_0        
        //   260: getfield        org/telegram/messenger/NotificationImageProvider.sync:Ljava/lang/Object;
        //   263: astore          4
        //   265: aload           4
        //   267: monitorenter   
        //   268: aload_0        
        //   269: getfield        org/telegram/messenger/NotificationImageProvider.waitingForFiles:Ljava/util/HashSet;
        //   272: aload_2        
        //   273: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
        //   276: pop            
        //   277: aload_0        
        //   278: getfield        org/telegram/messenger/NotificationImageProvider.sync:Ljava/lang/Object;
        //   281: ldc2_w          1000
        //   284: invokevirtual   java/lang/Object.wait:(J)V
        //   287: aload           4
        //   289: monitorexit    
        //   290: goto            167
        //   293: astore_1       
        //   294: aload           4
        //   296: monitorexit    
        //   297: aload_1        
        //   298: athrow         
        //   299: aload_3        
        //   300: ldc             268435456
        //   302: invokestatic    android/os/ParcelFileDescriptor.open:(Ljava/io/File;I)Landroid/os/ParcelFileDescriptor;
        //   305: areturn        
        //   306: new             Ljava/io/FileNotFoundException;
        //   309: dup            
        //   310: ldc             "Invalid URI"
        //   312: invokespecial   java/io/FileNotFoundException.<init>:(Ljava/lang/String;)V
        //   315: athrow         
        //   316: new             Ljava/lang/SecurityException;
        //   319: dup            
        //   320: ldc             "Can only open files for read"
        //   322: invokespecial   java/lang/SecurityException.<init>:(Ljava/lang/String;)V
        //   325: athrow         
        //   326: astore          7
        //   328: goto            287
        //    Exceptions:
        //  throws java.io.FileNotFoundException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  268    277    293    299    Any
        //  277    287    326    331    Ljava/lang/InterruptedException;
        //  277    287    293    299    Any
        //  287    290    293    299    Any
        //  294    297    293    299    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0287:
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
    
    public Cursor query(final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        return null;
    }
    
    public void shutdown() {
        for (int i = 0; i < UserConfig.getActivatedAccountsCount(); ++i) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileDidLoad);
        }
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        return 0;
    }
}
