// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.network;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import okhttp3.CookieJar;
import javax.net.ssl.SSLSocketFactory;
import menion.android.whereyougo.utils.Logger;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Request;
import android.content.Context;
import okhttp3.OkHttpClient;
import android.os.AsyncTask;

public class DownloadCartridgeTask extends AsyncTask<String, Progress, Boolean>
{
    private static final String DOWNLOAD = "http://www.wherigo.com/cartridge/download.aspx";
    private static final String LOGIN = "https://www.wherigo.com/login/default.aspx";
    private static final String TAG = "DownloadCartridgeTask";
    private String errorMessage;
    private OkHttpClient httpClient;
    private final String password;
    private final String username;
    
    public DownloadCartridgeTask(final Context context, final String username, final String password) {
        this.username = username;
        this.password = password;
    }
    
    private boolean download(String str) {
        final Response handleRequest = this.handleRequest(new Request.Builder().url("http://www.wherigo.com/cartridge/download.aspx?CGUID=" + str).post(new FormBody.Builder().add("__EVENTTARGET", "").add("__EVENTARGUMENT", "").add("ctl00$ContentPlaceHolder1$uxDeviceList", "4").add("ctl00$ContentPlaceHolder1$btnDownload", "Download Now").build()).build());
        boolean download;
        if (handleRequest != null && "application/octet-stream".equals(handleRequest.body().contentType().toString())) {
            final String header = handleRequest.header("Content-Disposition", "");
            if (header.matches("(?i)^ *attachment *; *filename *= *(.*) *$")) {
                str = str + "_" + header.replaceFirst("(?i)^ *attachment *; *filename *= *(.*) *$", "$1");
            }
            else {
                str += ".gwc";
            }
            download = this.download(str, handleRequest.body().byteStream(), Long.parseLong(handleRequest.header("Content-Length", "0")));
        }
        else {
            download = false;
        }
        return download;
    }
    
    private boolean download(final String p0, final InputStream p1, final long p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: new             Ljava/lang/StringBuilder;
        //     7: dup            
        //     8: invokespecial   java/lang/StringBuilder.<init>:()V
        //    11: getstatic       menion/android/whereyougo/utils/FileSystem.ROOT:Ljava/lang/String;
        //    14: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    17: aload_1        
        //    18: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    21: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    24: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    27: astore          5
        //    29: lconst_0       
        //    30: lstore          6
        //    32: sipush          1024
        //    35: newarray        B
        //    37: astore          8
        //    39: aconst_null    
        //    40: astore          9
        //    42: aconst_null    
        //    43: astore          10
        //    45: aconst_null    
        //    46: astore          11
        //    48: aconst_null    
        //    49: astore          12
        //    51: aconst_null    
        //    52: astore          13
        //    54: aload           9
        //    56: astore          14
        //    58: lload           6
        //    60: lstore          15
        //    62: aload           11
        //    64: astore          17
        //    66: new             Ljava/io/BufferedInputStream;
        //    69: astore          18
        //    71: aload           9
        //    73: astore          14
        //    75: lload           6
        //    77: lstore          15
        //    79: aload           11
        //    81: astore          17
        //    83: aload           18
        //    85: aload_2        
        //    86: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //    89: new             Ljava/io/BufferedOutputStream;
        //    92: astore_2       
        //    93: new             Ljava/io/FileOutputStream;
        //    96: astore          14
        //    98: aload           14
        //   100: aload           5
        //   102: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   105: aload_2        
        //   106: aload           14
        //   108: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   111: lload           6
        //   113: lstore          15
        //   115: lload           6
        //   117: lstore          19
        //   119: new             Lmenion/android/whereyougo/network/DownloadCartridgeTask$Progress;
        //   122: astore          14
        //   124: lload           6
        //   126: lstore          15
        //   128: lload           6
        //   130: lstore          19
        //   132: aload           14
        //   134: aload_0        
        //   135: getstatic       menion/android/whereyougo/network/DownloadCartridgeTask$Task.DOWNLOAD_SINGLE:Lmenion/android/whereyougo/network/DownloadCartridgeTask$Task;
        //   138: lconst_0       
        //   139: lload_3        
        //   140: invokespecial   menion/android/whereyougo/network/DownloadCartridgeTask$Progress.<init>:(Lmenion/android/whereyougo/network/DownloadCartridgeTask;Lmenion/android/whereyougo/network/DownloadCartridgeTask$Task;JJ)V
        //   143: lload           6
        //   145: lstore          15
        //   147: lload           6
        //   149: lstore          19
        //   151: aload_0        
        //   152: iconst_1       
        //   153: anewarray       Lmenion/android/whereyougo/network/DownloadCartridgeTask$Progress;
        //   156: dup            
        //   157: iconst_0       
        //   158: aload           14
        //   160: aastore        
        //   161: invokevirtual   menion/android/whereyougo/network/DownloadCartridgeTask.publishProgress:([Ljava/lang/Object;)V
        //   164: lload           6
        //   166: lstore          15
        //   168: lload           6
        //   170: lstore          19
        //   172: aload           18
        //   174: aload           8
        //   176: invokevirtual   java/io/BufferedInputStream.read:([B)I
        //   179: istore          21
        //   181: iload           21
        //   183: ifle            431
        //   186: lload           6
        //   188: lstore          15
        //   190: lload           6
        //   192: lstore          19
        //   194: aload_0        
        //   195: invokevirtual   menion/android/whereyougo/network/DownloadCartridgeTask.isCancelled:()Z
        //   198: ifne            431
        //   201: lload           6
        //   203: lstore          15
        //   205: lload           6
        //   207: lstore          19
        //   209: aload_2        
        //   210: aload           8
        //   212: iconst_0       
        //   213: iload           21
        //   215: invokevirtual   java/io/BufferedOutputStream.write:([BII)V
        //   218: lload           6
        //   220: iload           21
        //   222: i2l            
        //   223: ladd           
        //   224: lstore          6
        //   226: lload           6
        //   228: lstore          15
        //   230: lload           6
        //   232: lstore          19
        //   234: new             Lmenion/android/whereyougo/network/DownloadCartridgeTask$Progress;
        //   237: astore          14
        //   239: lload           6
        //   241: lstore          15
        //   243: lload           6
        //   245: lstore          19
        //   247: aload           14
        //   249: aload_0        
        //   250: getstatic       menion/android/whereyougo/network/DownloadCartridgeTask$Task.DOWNLOAD_SINGLE:Lmenion/android/whereyougo/network/DownloadCartridgeTask$Task;
        //   253: lload           6
        //   255: lload_3        
        //   256: invokespecial   menion/android/whereyougo/network/DownloadCartridgeTask$Progress.<init>:(Lmenion/android/whereyougo/network/DownloadCartridgeTask;Lmenion/android/whereyougo/network/DownloadCartridgeTask$Task;JJ)V
        //   259: lload           6
        //   261: lstore          15
        //   263: lload           6
        //   265: lstore          19
        //   267: aload_0        
        //   268: iconst_1       
        //   269: anewarray       Lmenion/android/whereyougo/network/DownloadCartridgeTask$Progress;
        //   272: dup            
        //   273: iconst_0       
        //   274: aload           14
        //   276: aastore        
        //   277: invokevirtual   menion/android/whereyougo/network/DownloadCartridgeTask.publishProgress:([Ljava/lang/Object;)V
        //   280: goto            164
        //   283: astore          12
        //   285: aload           18
        //   287: astore          14
        //   289: aload_2        
        //   290: astore          18
        //   292: lload           15
        //   294: lstore          6
        //   296: aload           14
        //   298: astore_2       
        //   299: aload_2        
        //   300: astore          14
        //   302: lload           6
        //   304: lstore          15
        //   306: aload           18
        //   308: astore          17
        //   310: new             Ljava/lang/StringBuilder;
        //   313: astore          13
        //   315: aload_2        
        //   316: astore          14
        //   318: lload           6
        //   320: lstore          15
        //   322: aload           18
        //   324: astore          17
        //   326: aload           13
        //   328: invokespecial   java/lang/StringBuilder.<init>:()V
        //   331: aload_2        
        //   332: astore          14
        //   334: lload           6
        //   336: lstore          15
        //   338: aload           18
        //   340: astore          17
        //   342: ldc             "DownloadCartridgeTask"
        //   344: aload           13
        //   346: ldc             "download("
        //   348: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   351: aload_1        
        //   352: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   355: ldc             ")"
        //   357: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   360: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   363: aload           12
        //   365: invokestatic    menion/android/whereyougo/utils/Logger.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Exception;)V
        //   368: aload_2        
        //   369: astore          14
        //   371: lload           6
        //   373: lstore          15
        //   375: aload           18
        //   377: astore          17
        //   379: aload_0        
        //   380: aload           12
        //   382: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   385: putfield        menion/android/whereyougo/network/DownloadCartridgeTask.errorMessage:Ljava/lang/String;
        //   388: aload_2        
        //   389: invokestatic    menion/android/whereyougo/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   392: aload           18
        //   394: invokestatic    menion/android/whereyougo/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   397: lload           6
        //   399: lstore          15
        //   401: lload           6
        //   403: lload_3        
        //   404: lcmp           
        //   405: ifeq            418
        //   408: aload           5
        //   410: invokevirtual   java/io/File.delete:()Z
        //   413: pop            
        //   414: lload           6
        //   416: lstore          15
        //   418: lload           15
        //   420: lload_3        
        //   421: lcmp           
        //   422: ifne            490
        //   425: iconst_1       
        //   426: istore          22
        //   428: iload           22
        //   430: ireturn        
        //   431: aload           18
        //   433: invokestatic    menion/android/whereyougo/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   436: aload_2        
        //   437: invokestatic    menion/android/whereyougo/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   440: lload           6
        //   442: lload_3        
        //   443: lcmp           
        //   444: ifeq            547
        //   447: aload           5
        //   449: invokevirtual   java/io/File.delete:()Z
        //   452: pop            
        //   453: lload           6
        //   455: lstore          15
        //   457: goto            418
        //   460: astore_1       
        //   461: lload           15
        //   463: lstore          6
        //   465: aload           14
        //   467: invokestatic    menion/android/whereyougo/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   470: aload           17
        //   472: invokestatic    menion/android/whereyougo/utils/Utils.closeStream:(Ljava/io/Closeable;)V
        //   475: lload           6
        //   477: lload_3        
        //   478: lcmp           
        //   479: ifeq            488
        //   482: aload           5
        //   484: invokevirtual   java/io/File.delete:()Z
        //   487: pop            
        //   488: aload_1        
        //   489: athrow         
        //   490: iconst_0       
        //   491: istore          22
        //   493: goto            428
        //   496: astore_1       
        //   497: aload           18
        //   499: astore          14
        //   501: aload           12
        //   503: astore          17
        //   505: goto            465
        //   508: astore_1       
        //   509: aload           18
        //   511: astore          14
        //   513: lload           19
        //   515: lstore          6
        //   517: aload_2        
        //   518: astore          17
        //   520: goto            465
        //   523: astore          12
        //   525: aload           10
        //   527: astore_2       
        //   528: aload           13
        //   530: astore          18
        //   532: goto            299
        //   535: astore          12
        //   537: aload           18
        //   539: astore_2       
        //   540: aload           13
        //   542: astore          18
        //   544: goto            299
        //   547: lload           6
        //   549: lstore          15
        //   551: goto            418
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  66     71     523    535    Ljava/io/IOException;
        //  66     71     460    465    Any
        //  83     89     523    535    Ljava/io/IOException;
        //  83     89     460    465    Any
        //  89     111    535    547    Ljava/io/IOException;
        //  89     111    496    508    Any
        //  119    124    283    299    Ljava/io/IOException;
        //  119    124    508    523    Any
        //  132    143    283    299    Ljava/io/IOException;
        //  132    143    508    523    Any
        //  151    164    283    299    Ljava/io/IOException;
        //  151    164    508    523    Any
        //  172    181    283    299    Ljava/io/IOException;
        //  172    181    508    523    Any
        //  194    201    283    299    Ljava/io/IOException;
        //  194    201    508    523    Any
        //  209    218    283    299    Ljava/io/IOException;
        //  209    218    508    523    Any
        //  234    239    283    299    Ljava/io/IOException;
        //  234    239    508    523    Any
        //  247    259    283    299    Ljava/io/IOException;
        //  247    259    508    523    Any
        //  267    280    283    299    Ljava/io/IOException;
        //  267    280    508    523    Any
        //  310    315    460    465    Any
        //  326    331    460    465    Any
        //  342    368    460    465    Any
        //  379    388    460    465    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0164:
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
    
    private boolean download(final String[] array) {
        this.publishProgress((Object[])new Progress[] { new Progress(Task.DOWNLOAD, State.WORKING) });
        for (int i = 0; i < array.length; ++i) {
            if (!this.download(array[i])) {
                this.publishProgress((Object[])new Progress[] { new Progress(Task.DOWNLOAD, State.FAIL, this.errorMessage) });
                return false;
            }
            this.publishProgress((Object[])new Progress[] { new Progress(Task.DOWNLOAD, i, array.length) });
        }
        return true;
    }
    
    private Response handleRequest(final Request request) {
        Response execute;
        if (this.isCancelled()) {
            execute = null;
        }
        else {
            try {
                final Response obj = execute = this.httpClient.newCall(request).execute();
                if (!obj.isSuccessful()) {
                    throw new IOException("Request " + request.toString() + " failed: " + obj);
                }
            }
            catch (Exception ex) {
                Logger.e("DownloadCartridgeTask", "handleRequest(" + request.toString() + ")", ex);
                this.errorMessage = ex.getMessage();
                execute = null;
            }
        }
        return execute;
    }
    
    private Response handleRequest(final Request request, final Task task) {
        this.publishProgress((Object[])new Progress[] { new Progress(task, State.WORKING) });
        final Response handleRequest = this.handleRequest(request);
        if (handleRequest != null) {
            this.publishProgress((Object[])new Progress[] { new Progress(task, State.SUCCESS) });
        }
        else {
            this.publishProgress((Object[])new Progress[] { new Progress(task, State.FAIL, this.errorMessage) });
        }
        return handleRequest;
    }
    
    private boolean init() {
        final boolean b = true;
        try {
            System.setProperty("http.keepAlive", "false");
            final OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Object sslSocketFactory = new TLSSocketFactory();
            sslSocketFactory = builder.sslSocketFactory((SSLSocketFactory)sslSocketFactory);
            this.httpClient = ((OkHttpClient.Builder)sslSocketFactory).cookieJar(new NonPersistentCookieJar()).connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).build();
            if (this.httpClient == null) {
                this.publishProgress((Object[])new Progress[] { new Progress(Task.INIT, State.FAIL, this.errorMessage) });
            }
            if (this.httpClient != null) {
                return b;
            }
            goto Label_0147;
        }
        catch (NoSuchAlgorithmException ex) {}
        catch (KeyManagementException sslSocketFactory) {
            goto Label_0127;
        }
    }
    
    private boolean login() {
        boolean b = true;
        final Request build = new Request.Builder().url("https://www.wherigo.com/login/default.aspx").post(new FormBody.Builder().add("__EVENTTARGET", "").add("__EVENTARGUMENT", "").add("ctl00$ContentPlaceHolder1$Login1$Login1$UserName", this.username).add("ctl00$ContentPlaceHolder1$Login1$Login1$Password", this.password).add("ctl00$ContentPlaceHolder1$Login1$Login1$LoginButton", "Sign In").build()).build();
        this.publishProgress((Object[])new Progress[] { new Progress(Task.LOGIN, State.WORKING) });
        final Response handleRequest = this.handleRequest(build);
        if (handleRequest != null && !"https://www.wherigo.com/login/default.aspx".equals(handleRequest.request().url().toString())) {
            this.publishProgress((Object[])new Progress[] { new Progress(Task.LOGIN, State.SUCCESS) });
        }
        else {
            this.publishProgress((Object[])new Progress[] { new Progress(Task.LOGIN, State.FAIL, this.errorMessage) });
            b = false;
        }
        return b;
    }
    
    private boolean logout() {
        return this.handleRequest(new Request.Builder().url("https://www.wherigo.com/login/default.aspx").post(new FormBody.Builder().add("__EVENTTARGET", "ctl00$ProfileWidget$LoginStatus1$ctl00").add("__EVENTARGUMENT", "").build()).build(), Task.LOGOUT) != null;
    }
    
    private boolean ping() {
        return this.handleRequest(new Request.Builder().url("https://www.wherigo.com/login/default.aspx").build(), Task.PING) != null;
    }
    
    protected Boolean doInBackground(final String... array) {
        return this.init() && this.ping() && this.login() && this.download(array) && this.logout();
    }
    
    public class Progress
    {
        long completed;
        String message;
        final State state;
        final Task task;
        long total;
        
        public Progress(final Task task, final long completed, final long total) {
            this.state = State.WORKING;
            this.task = task;
            this.total = total;
            this.completed = completed;
        }
        
        public Progress(final Task task, final State state) {
            this.task = task;
            this.state = state;
        }
        
        public Progress(final Task task, final State state, final String message) {
            this.task = task;
            this.state = state;
            this.message = message;
        }
        
        public long getCompleted() {
            return this.completed;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public State getState() {
            return this.state;
        }
        
        public Task getTask() {
            return this.task;
        }
        
        public long getTotal() {
            return this.total;
        }
    }
    
    public enum State
    {
        FAIL, 
        SUCCESS, 
        WORKING;
    }
    
    public enum Task
    {
        DOWNLOAD, 
        DOWNLOAD_SINGLE, 
        INIT, 
        LOGIN, 
        LOGOUT, 
        PING;
    }
}
