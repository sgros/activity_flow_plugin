// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.network;

import java.io.FileInputStream;
import java.io.File;
import java.net.URL;
import java.net.HttpURLConnection;
import com.airbnb.lottie.utils.Logger;
import java.io.IOException;
import com.airbnb.lottie.LottieResult;
import androidx.core.util.Pair;
import com.airbnb.lottie.LottieCompositionFactory;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import com.airbnb.lottie.LottieComposition;
import android.content.Context;

public class NetworkFetcher
{
    private final Context appContext;
    private final NetworkCache networkCache;
    private final String url;
    
    private NetworkFetcher(final Context context, final String url) {
        this.appContext = context.getApplicationContext();
        this.url = url;
        this.networkCache = new NetworkCache(this.appContext, url);
    }
    
    private LottieComposition fetchFromCache() {
        final Pair<FileExtension, InputStream> fetch = this.networkCache.fetch();
        if (fetch == null) {
            return null;
        }
        final FileExtension fileExtension = fetch.first;
        final InputStream in = fetch.second;
        LottieResult<LottieComposition> lottieResult;
        if (fileExtension == FileExtension.ZIP) {
            lottieResult = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(in), this.url);
        }
        else {
            lottieResult = LottieCompositionFactory.fromJsonInputStreamSync(in, this.url);
        }
        if (lottieResult.getValue() != null) {
            return lottieResult.getValue();
        }
        return null;
    }
    
    private LottieResult<LottieComposition> fetchFromNetwork() {
        try {
            return (LottieResult<LottieComposition>)this.fetchFromNetworkInternal();
        }
        catch (IOException ex) {
            return new LottieResult<LottieComposition>(ex);
        }
    }
    
    private LottieResult fetchFromNetworkInternal() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Fetching ");
        sb.append(this.url);
        Logger.debug(sb.toString());
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        try {
            try {
                httpURLConnection.connect();
                if (httpURLConnection.getErrorStream() == null && httpURLConnection.getResponseCode() == 200) {
                    final LottieResult<LottieComposition> resultFromConnection = this.getResultFromConnection(httpURLConnection);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Completed fetch from network. Success: ");
                    sb2.append(resultFromConnection.getValue() != null);
                    Logger.debug(sb2.toString());
                    httpURLConnection.disconnect();
                    return resultFromConnection;
                }
                final String errorFromConnection = this.getErrorFromConnection(httpURLConnection);
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unable to fetch ");
                sb3.append(this.url);
                sb3.append(". Failed with ");
                sb3.append(httpURLConnection.getResponseCode());
                sb3.append("\n");
                sb3.append(errorFromConnection);
                final LottieResult lottieResult = new LottieResult(new IllegalArgumentException(sb3.toString()));
                httpURLConnection.disconnect();
                return lottieResult;
            }
            finally {}
        }
        catch (Exception ex) {
            final LottieResult lottieResult2 = new LottieResult(ex);
            httpURLConnection.disconnect();
            return lottieResult2;
        }
        httpURLConnection.disconnect();
    }
    
    public static LottieResult<LottieComposition> fetchSync(final Context context, final String s) {
        return new NetworkFetcher(context, s).fetchSync();
    }
    
    private String getErrorFromConnection(final HttpURLConnection p0) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //     4: pop            
        //     5: new             Ljava/io/BufferedReader;
        //     8: dup            
        //     9: new             Ljava/io/InputStreamReader;
        //    12: dup            
        //    13: aload_1        
        //    14: invokevirtual   java/net/HttpURLConnection.getErrorStream:()Ljava/io/InputStream;
        //    17: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    20: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    23: astore_1       
        //    24: new             Ljava/lang/StringBuilder;
        //    27: dup            
        //    28: invokespecial   java/lang/StringBuilder.<init>:()V
        //    31: astore_2       
        //    32: aload_1        
        //    33: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    36: astore_3       
        //    37: aload_3        
        //    38: ifnull          57
        //    41: aload_2        
        //    42: aload_3        
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: pop            
        //    47: aload_2        
        //    48: bipush          10
        //    50: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    53: pop            
        //    54: goto            32
        //    57: aload_1        
        //    58: invokevirtual   java/io/BufferedReader.close:()V
        //    61: aload_2        
        //    62: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    65: areturn        
        //    66: astore_2       
        //    67: goto            73
        //    70: astore_2       
        //    71: aload_2        
        //    72: athrow         
        //    73: aload_1        
        //    74: invokevirtual   java/io/BufferedReader.close:()V
        //    77: aload_2        
        //    78: athrow         
        //    79: astore_1       
        //    80: goto            61
        //    83: astore_1       
        //    84: goto            77
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  32     37     70     73     Ljava/lang/Exception;
        //  32     37     66     79     Any
        //  41     54     70     73     Ljava/lang/Exception;
        //  41     54     66     79     Any
        //  57     61     79     83     Ljava/lang/Exception;
        //  71     73     66     79     Any
        //  73     77     83     87     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 48 out-of-bounds for length 48
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
    
    private LottieResult<LottieComposition> getResultFromConnection(final HttpURLConnection httpURLConnection) throws IOException {
        String contentType;
        if ((contentType = httpURLConnection.getContentType()) == null) {
            contentType = "application/json";
        }
        int n = -1;
        final int hashCode = contentType.hashCode();
        if (hashCode != -1248325150) {
            if (hashCode == -43840953) {
                if (contentType.equals("application/json")) {
                    n = 1;
                }
            }
        }
        else if (contentType.equals("application/zip")) {
            n = 0;
        }
        FileExtension fileExtension;
        LottieResult<LottieComposition> fromZipStreamSync;
        if (n != 0) {
            Logger.debug("Received json response.");
            final FileExtension json = FileExtension.JSON;
            final LottieResult<LottieComposition> fromJsonInputStreamSync = LottieCompositionFactory.fromJsonInputStreamSync(new FileInputStream(new File(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), json).getAbsolutePath())), this.url);
            fileExtension = json;
            fromZipStreamSync = fromJsonInputStreamSync;
        }
        else {
            Logger.debug("Handling zip response.");
            final FileExtension zip = FileExtension.ZIP;
            fromZipStreamSync = LottieCompositionFactory.fromZipStreamSync(new ZipInputStream(new FileInputStream(this.networkCache.writeTempCacheFile(httpURLConnection.getInputStream(), zip))), this.url);
            fileExtension = zip;
        }
        if (fromZipStreamSync.getValue() != null) {
            this.networkCache.renameTempFile(fileExtension);
        }
        return fromZipStreamSync;
    }
    
    public LottieResult<LottieComposition> fetchSync() {
        final LottieComposition fetchFromCache = this.fetchFromCache();
        if (fetchFromCache != null) {
            return new LottieResult<LottieComposition>(fetchFromCache);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Animation for ");
        sb.append(this.url);
        sb.append(" not found in cache. Fetching from network.");
        Logger.debug(sb.toString());
        return this.fetchFromNetwork();
    }
}
