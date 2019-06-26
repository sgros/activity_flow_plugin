// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.util.Log;
import android.graphics.drawable.Drawable;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.config.Configuration;

public class TileDownloader
{
    public long computeExpirationTime(final String s, final String s2, final long n) {
        final Long expirationOverrideDuration = Configuration.getInstance().getExpirationOverrideDuration();
        if (expirationOverrideDuration != null) {
            return n + expirationOverrideDuration;
        }
        final long expirationExtendedDuration = Configuration.getInstance().getExpirationExtendedDuration();
        final Long httpCacheControlDuration = this.getHttpCacheControlDuration(s2);
        if (httpCacheControlDuration != null) {
            return n + httpCacheControlDuration * 1000L + expirationExtendedDuration;
        }
        final Long httpExpiresTime = this.getHttpExpiresTime(s);
        if (httpExpiresTime != null) {
            return httpExpiresTime + expirationExtendedDuration;
        }
        return n + 604800000L + expirationExtendedDuration;
    }
    
    public Drawable downloadTile(final long p0, final int p1, final String p2, final IFilesystemCache p3, final OnlineTileSourceBase p4) throws CantContinueException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: astore          7
        //     4: aconst_null    
        //     5: astore          8
        //     7: aconst_null    
        //     8: astore          9
        //    10: iload_3        
        //    11: iconst_3       
        //    12: if_icmple       17
        //    15: aconst_null    
        //    16: areturn        
        //    17: aload           6
        //    19: invokevirtual   org/osmdroid/tileprovider/tilesource/OnlineTileSourceBase.getTileSourcePolicy:()Lorg/osmdroid/tileprovider/tilesource/TileSourcePolicy;
        //    22: invokevirtual   org/osmdroid/tileprovider/tilesource/TileSourcePolicy.normalizesUserAgent:()Z
        //    25: ifeq            41
        //    28: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //    31: invokeinterface org/osmdroid/config/IConfigurationProvider.getNormalizedUserAgent:()Ljava/lang/String;
        //    36: astore          10
        //    38: goto            44
        //    41: aconst_null    
        //    42: astore          10
        //    44: aload           10
        //    46: astore          11
        //    48: aload           10
        //    50: ifnonnull       63
        //    53: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //    56: invokeinterface org/osmdroid/config/IConfigurationProvider.getUserAgentValue:()Ljava/lang/String;
        //    61: astore          11
        //    63: aload           6
        //    65: invokevirtual   org/osmdroid/tileprovider/tilesource/OnlineTileSourceBase.getTileSourcePolicy:()Lorg/osmdroid/tileprovider/tilesource/TileSourcePolicy;
        //    68: aload           11
        //    70: invokevirtual   org/osmdroid/tileprovider/tilesource/TileSourcePolicy.acceptsUserAgent:(Ljava/lang/String;)Z
        //    73: ifne            114
        //    76: new             Ljava/lang/StringBuilder;
        //    79: dup            
        //    80: invokespecial   java/lang/StringBuilder.<init>:()V
        //    83: astore          4
        //    85: aload           4
        //    87: ldc             "Please configure a relevant user agent; current value is: "
        //    89: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    92: pop            
        //    93: aload           4
        //    95: aload           11
        //    97: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   100: pop            
        //   101: ldc             "OsmDroid"
        //   103: aload           4
        //   105: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   108: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   111: pop            
        //   112: aconst_null    
        //   113: areturn        
        //   114: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   117: invokeinterface org/osmdroid/config/IConfigurationProvider.isDebugMode:()Z
        //   122: ifeq            162
        //   125: new             Ljava/lang/StringBuilder;
        //   128: astore          10
        //   130: aload           10
        //   132: invokespecial   java/lang/StringBuilder.<init>:()V
        //   135: aload           10
        //   137: ldc             "Downloading Maptile from url: "
        //   139: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   142: pop            
        //   143: aload           10
        //   145: aload           4
        //   147: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   150: pop            
        //   151: ldc             "OsmDroid"
        //   153: aload           10
        //   155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   158: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   161: pop            
        //   162: aload           4
        //   164: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   167: istore          12
        //   169: iload           12
        //   171: ifeq            200
        //   174: aconst_null    
        //   175: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   178: aconst_null    
        //   179: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   182: aconst_null    
        //   183: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   186: aconst_null    
        //   187: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   190: new             Ljava/lang/NullPointerException;
        //   193: dup            
        //   194: invokespecial   java/lang/NullPointerException.<init>:()V
        //   197: athrow         
        //   198: aconst_null    
        //   199: areturn        
        //   200: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   203: invokeinterface org/osmdroid/config/IConfigurationProvider.getHttpProxy:()Ljava/net/Proxy;
        //   208: ifnull          244
        //   211: new             Ljava/net/URL;
        //   214: astore          10
        //   216: aload           10
        //   218: aload           4
        //   220: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   223: aload           10
        //   225: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   228: invokeinterface org/osmdroid/config/IConfigurationProvider.getHttpProxy:()Ljava/net/Proxy;
        //   233: invokevirtual   java/net/URL.openConnection:(Ljava/net/Proxy;)Ljava/net/URLConnection;
        //   236: checkcast       Ljava/net/HttpURLConnection;
        //   239: astore          10
        //   241: goto            266
        //   244: new             Ljava/net/URL;
        //   247: astore          10
        //   249: aload           10
        //   251: aload           4
        //   253: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   256: aload           10
        //   258: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //   261: checkcast       Ljava/net/HttpURLConnection;
        //   264: astore          10
        //   266: aload           10
        //   268: iconst_1       
        //   269: invokevirtual   java/net/HttpURLConnection.setUseCaches:(Z)V
        //   272: aload           10
        //   274: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   277: invokeinterface org/osmdroid/config/IConfigurationProvider.getUserAgentHttpHeader:()Ljava/lang/String;
        //   282: aload           11
        //   284: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   287: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   290: invokeinterface org/osmdroid/config/IConfigurationProvider.getAdditionalHttpRequestProperties:()Ljava/util/Map;
        //   295: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   300: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   305: astore          11
        //   307: aload           11
        //   309: invokeinterface java/util/Iterator.hasNext:()Z
        //   314: ifeq            357
        //   317: aload           11
        //   319: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   324: checkcast       Ljava/util/Map$Entry;
        //   327: astore          13
        //   329: aload           10
        //   331: aload           13
        //   333: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   338: checkcast       Ljava/lang/String;
        //   341: aload           13
        //   343: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   348: checkcast       Ljava/lang/String;
        //   351: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   354: goto            307
        //   357: aload           10
        //   359: invokevirtual   java/net/HttpURLConnection.connect:()V
        //   362: aload           10
        //   364: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   367: sipush          200
        //   370: if_icmpeq       826
        //   373: aload           10
        //   375: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   378: istore          14
        //   380: iload           14
        //   382: sipush          301
        //   385: if_icmpeq       415
        //   388: iload           14
        //   390: sipush          302
        //   393: if_icmpeq       415
        //   396: iload           14
        //   398: sipush          307
        //   401: if_icmpeq       415
        //   404: iload           14
        //   406: sipush          308
        //   409: if_icmpeq       415
        //   412: goto            710
        //   415: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   418: invokeinterface org/osmdroid/config/IConfigurationProvider.isMapTileDownloaderFollowRedirects:()Z
        //   423: ifeq            710
        //   426: aload           10
        //   428: ldc             "Location"
        //   430: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //   433: astore          13
        //   435: aload           13
        //   437: ifnull          826
        //   440: aload           13
        //   442: astore          11
        //   444: aload           13
        //   446: ldc             "/"
        //   448: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   451: ifeq            596
        //   454: new             Ljava/net/URL;
        //   457: astore          11
        //   459: aload           11
        //   461: aload           4
        //   463: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   466: aload           11
        //   468: invokevirtual   java/net/URL.getPort:()I
        //   471: istore          15
        //   473: aload           4
        //   475: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   478: ldc             "https://"
        //   480: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   483: istore          12
        //   485: iload           15
        //   487: istore          14
        //   489: iload           15
        //   491: iconst_m1      
        //   492: if_icmpne       520
        //   495: aload           4
        //   497: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   500: ldc             "http://"
        //   502: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   505: ifeq            515
        //   508: bipush          80
        //   510: istore          14
        //   512: goto            520
        //   515: sipush          443
        //   518: istore          14
        //   520: new             Ljava/lang/StringBuilder;
        //   523: astore          16
        //   525: aload           16
        //   527: invokespecial   java/lang/StringBuilder.<init>:()V
        //   530: iload           12
        //   532: ifeq            542
        //   535: aload           7
        //   537: astore          4
        //   539: goto            546
        //   542: ldc             "http"
        //   544: astore          4
        //   546: aload           16
        //   548: aload           4
        //   550: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   553: pop            
        //   554: aload           16
        //   556: aload           11
        //   558: invokevirtual   java/net/URL.getHost:()Ljava/lang/String;
        //   561: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   564: pop            
        //   565: aload           16
        //   567: ldc             ":"
        //   569: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   572: pop            
        //   573: aload           16
        //   575: iload           14
        //   577: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   580: pop            
        //   581: aload           16
        //   583: aload           13
        //   585: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   588: pop            
        //   589: aload           16
        //   591: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   594: astore          11
        //   596: new             Ljava/lang/StringBuilder;
        //   599: astore          4
        //   601: aload           4
        //   603: invokespecial   java/lang/StringBuilder.<init>:()V
        //   606: aload           4
        //   608: ldc             "Http redirect for MapTile: "
        //   610: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   613: pop            
        //   614: aload           4
        //   616: lload_1        
        //   617: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //   620: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   623: pop            
        //   624: aload           4
        //   626: ldc             " HTTP response: "
        //   628: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   631: pop            
        //   632: aload           4
        //   634: aload           10
        //   636: invokevirtual   java/net/HttpURLConnection.getResponseMessage:()Ljava/lang/String;
        //   639: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   642: pop            
        //   643: aload           4
        //   645: ldc             " to url "
        //   647: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   650: pop            
        //   651: aload           4
        //   653: aload           11
        //   655: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   658: pop            
        //   659: ldc             "OsmDroid"
        //   661: aload           4
        //   663: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   666: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   669: pop            
        //   670: aload_0        
        //   671: lload_1        
        //   672: iload_3        
        //   673: iconst_1       
        //   674: iadd           
        //   675: aload           11
        //   677: aload           5
        //   679: aload           6
        //   681: invokevirtual   org/osmdroid/tileprovider/modules/TileDownloader.downloadTile:(JILjava/lang/String;Lorg/osmdroid/tileprovider/modules/IFilesystemCache;Lorg/osmdroid/tileprovider/tilesource/OnlineTileSourceBase;)Landroid/graphics/drawable/Drawable;
        //   684: astore          4
        //   686: aconst_null    
        //   687: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   690: aconst_null    
        //   691: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   694: aconst_null    
        //   695: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   698: aconst_null    
        //   699: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   702: aload           10
        //   704: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   707: aload           4
        //   709: areturn        
        //   710: new             Ljava/lang/StringBuilder;
        //   713: astore          5
        //   715: aload           5
        //   717: invokespecial   java/lang/StringBuilder.<init>:()V
        //   720: aload           5
        //   722: ldc             "Problem downloading MapTile: "
        //   724: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   727: pop            
        //   728: aload           5
        //   730: lload_1        
        //   731: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //   734: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   737: pop            
        //   738: aload           5
        //   740: ldc             " HTTP response: "
        //   742: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   745: pop            
        //   746: aload           5
        //   748: aload           10
        //   750: invokevirtual   java/net/HttpURLConnection.getResponseMessage:()Ljava/lang/String;
        //   753: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   756: pop            
        //   757: ldc             "OsmDroid"
        //   759: aload           5
        //   761: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   764: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   767: pop            
        //   768: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   771: invokeinterface org/osmdroid/config/IConfigurationProvider.isDebugMapTileDownloader:()Z
        //   776: ifeq            787
        //   779: ldc             "OsmDroid"
        //   781: aload           4
        //   783: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   786: pop            
        //   787: getstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //   790: iconst_1       
        //   791: iadd           
        //   792: putstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //   795: aload           10
        //   797: invokevirtual   java/net/HttpURLConnection.getErrorStream:()Ljava/io/InputStream;
        //   800: astore          4
        //   802: aload           4
        //   804: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   807: aconst_null    
        //   808: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   811: aconst_null    
        //   812: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   815: aconst_null    
        //   816: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //   819: aload           10
        //   821: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   824: aconst_null    
        //   825: areturn        
        //   826: aload           10
        //   828: ldc_w           "Content-Type"
        //   831: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //   834: astore          11
        //   836: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   839: invokeinterface org/osmdroid/config/IConfigurationProvider.isDebugMapTileDownloader:()Z
        //   844: ifeq            893
        //   847: new             Ljava/lang/StringBuilder;
        //   850: astore          7
        //   852: aload           7
        //   854: invokespecial   java/lang/StringBuilder.<init>:()V
        //   857: aload           7
        //   859: aload           4
        //   861: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   864: pop            
        //   865: aload           7
        //   867: ldc_w           " success, mime is "
        //   870: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   873: pop            
        //   874: aload           7
        //   876: aload           11
        //   878: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   881: pop            
        //   882: ldc             "OsmDroid"
        //   884: aload           7
        //   886: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   889: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   892: pop            
        //   893: aload           11
        //   895: ifnull          958
        //   898: aload           11
        //   900: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   903: ldc_w           "image"
        //   906: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   909: ifne            958
        //   912: new             Ljava/lang/StringBuilder;
        //   915: astore          7
        //   917: aload           7
        //   919: invokespecial   java/lang/StringBuilder.<init>:()V
        //   922: aload           7
        //   924: aload           4
        //   926: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   929: pop            
        //   930: aload           7
        //   932: ldc_w           " success, however the mime type does not appear to be an image "
        //   935: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   938: pop            
        //   939: aload           7
        //   941: aload           11
        //   943: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   946: pop            
        //   947: ldc             "OsmDroid"
        //   949: aload           7
        //   951: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   954: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //   957: pop            
        //   958: aload           10
        //   960: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   963: astore          7
        //   965: new             Ljava/io/ByteArrayOutputStream;
        //   968: astore          4
        //   970: aload           4
        //   972: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //   975: new             Ljava/io/BufferedOutputStream;
        //   978: astore          11
        //   980: aload           11
        //   982: aload           4
        //   984: sipush          8192
        //   987: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;I)V
        //   990: aload           10
        //   992: ldc_w           "Expires"
        //   995: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //   998: astore          13
        //  1000: aload           10
        //  1002: ldc_w           "Cache-Control"
        //  1005: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //  1008: astore          9
        //  1010: invokestatic    java/lang/System.currentTimeMillis:()J
        //  1013: lstore          17
        //  1015: aload_0        
        //  1016: aload           13
        //  1018: aload           9
        //  1020: lload           17
        //  1022: invokevirtual   org/osmdroid/tileprovider/modules/TileDownloader.computeExpirationTime:(Ljava/lang/String;Ljava/lang/String;J)J
        //  1025: lstore          17
        //  1027: aload           7
        //  1029: aload           11
        //  1031: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.copy:(Ljava/io/InputStream;Ljava/io/OutputStream;)J
        //  1034: pop2           
        //  1035: aload           11
        //  1037: invokevirtual   java/io/BufferedOutputStream.flush:()V
        //  1040: aload           4
        //  1042: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //  1045: astore          9
        //  1047: new             Ljava/io/ByteArrayInputStream;
        //  1050: astore          13
        //  1052: aload           13
        //  1054: aload           9
        //  1056: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //  1059: aload           5
        //  1061: ifnull          1087
        //  1064: aload           5
        //  1066: aload           6
        //  1068: lload_1        
        //  1069: aload           13
        //  1071: lload           17
        //  1073: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1076: invokeinterface org/osmdroid/tileprovider/modules/IFilesystemCache.saveFile:(Lorg/osmdroid/tileprovider/tilesource/ITileSource;JLjava/io/InputStream;Ljava/lang/Long;)Z
        //  1081: pop            
        //  1082: aload           13
        //  1084: invokevirtual   java/io/ByteArrayInputStream.reset:()V
        //  1087: aload           6
        //  1089: aload           13
        //  1091: invokevirtual   org/osmdroid/tileprovider/tilesource/BitmapTileSourceBase.getDrawable:(Ljava/io/InputStream;)Landroid/graphics/drawable/Drawable;
        //  1094: astore          5
        //  1096: aload           7
        //  1098: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1101: aload           11
        //  1103: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1106: aload           13
        //  1108: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1111: aload           4
        //  1113: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1116: aload           10
        //  1118: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1121: aload           5
        //  1123: areturn        
        //  1124: astore          5
        //  1126: aload           4
        //  1128: astore          6
        //  1130: aload           5
        //  1132: astore          4
        //  1134: aload           11
        //  1136: astore          5
        //  1138: goto            1389
        //  1141: astore          5
        //  1143: goto            1285
        //  1146: astore          5
        //  1148: goto            1308
        //  1151: astore          5
        //  1153: aload           11
        //  1155: astore          6
        //  1157: goto            1331
        //  1160: astore          5
        //  1162: goto            1500
        //  1165: astore          5
        //  1167: goto            1355
        //  1170: astore          5
        //  1172: goto            1202
        //  1175: astore          5
        //  1177: goto            1219
        //  1180: astore          5
        //  1182: goto            1227
        //  1185: astore          5
        //  1187: goto            1235
        //  1190: astore          5
        //  1192: goto            1247
        //  1195: astore          5
        //  1197: goto            1255
        //  1200: astore          5
        //  1202: aload           4
        //  1204: astore          6
        //  1206: aload           5
        //  1208: astore          4
        //  1210: aload           6
        //  1212: astore          5
        //  1214: goto            1378
        //  1217: astore          5
        //  1219: aconst_null    
        //  1220: astore          13
        //  1222: goto            1285
        //  1225: astore          5
        //  1227: aconst_null    
        //  1228: astore          13
        //  1230: goto            1308
        //  1233: astore          5
        //  1235: aconst_null    
        //  1236: astore          13
        //  1238: aload           11
        //  1240: astore          6
        //  1242: goto            1331
        //  1245: astore          5
        //  1247: aconst_null    
        //  1248: astore          13
        //  1250: goto            1500
        //  1253: astore          5
        //  1255: aconst_null    
        //  1256: astore          13
        //  1258: goto            1355
        //  1261: astore          6
        //  1263: aload           4
        //  1265: astore          5
        //  1267: aconst_null    
        //  1268: astore          11
        //  1270: aload           6
        //  1272: astore          4
        //  1274: goto            1378
        //  1277: astore          5
        //  1279: aconst_null    
        //  1280: astore          11
        //  1282: aconst_null    
        //  1283: astore          13
        //  1285: aload           4
        //  1287: astore          6
        //  1289: aload           5
        //  1291: astore          4
        //  1293: aload           11
        //  1295: astore          5
        //  1297: goto            1416
        //  1300: astore          5
        //  1302: aconst_null    
        //  1303: astore          11
        //  1305: aconst_null    
        //  1306: astore          13
        //  1308: aload           4
        //  1310: astore          6
        //  1312: aload           5
        //  1314: astore          4
        //  1316: aload           11
        //  1318: astore          5
        //  1320: goto            1447
        //  1323: astore          5
        //  1325: aconst_null    
        //  1326: astore          6
        //  1328: aconst_null    
        //  1329: astore          13
        //  1331: aload           4
        //  1333: astore          11
        //  1335: aload           13
        //  1337: astore          4
        //  1339: goto            1478
        //  1342: astore          5
        //  1344: goto            1494
        //  1347: astore          5
        //  1349: aconst_null    
        //  1350: astore          11
        //  1352: aconst_null    
        //  1353: astore          13
        //  1355: aload           5
        //  1357: astore          6
        //  1359: aload           4
        //  1361: astore          5
        //  1363: aload           13
        //  1365: astore          4
        //  1367: goto            1535
        //  1370: astore          4
        //  1372: aconst_null    
        //  1373: astore          11
        //  1375: aconst_null    
        //  1376: astore          5
        //  1378: aconst_null    
        //  1379: astore          13
        //  1381: aload           5
        //  1383: astore          6
        //  1385: aload           11
        //  1387: astore          5
        //  1389: aload           7
        //  1391: astore          11
        //  1393: aload           10
        //  1395: astore          7
        //  1397: aload           13
        //  1399: astore          10
        //  1401: goto            2774
        //  1404: astore          4
        //  1406: aconst_null    
        //  1407: astore          5
        //  1409: aconst_null    
        //  1410: astore          6
        //  1412: aload           6
        //  1414: astore          13
        //  1416: aload           4
        //  1418: astore          11
        //  1420: aload           7
        //  1422: astore          4
        //  1424: aload           10
        //  1426: astore          7
        //  1428: aload           13
        //  1430: astore          10
        //  1432: goto            1735
        //  1435: astore          4
        //  1437: aconst_null    
        //  1438: astore          5
        //  1440: aconst_null    
        //  1441: astore          6
        //  1443: aload           6
        //  1445: astore          13
        //  1447: aload           4
        //  1449: astore          19
        //  1451: aload           7
        //  1453: astore          4
        //  1455: aload           10
        //  1457: astore          11
        //  1459: aload           13
        //  1461: astore          10
        //  1463: goto            1850
        //  1466: astore          5
        //  1468: aconst_null    
        //  1469: astore          6
        //  1471: aconst_null    
        //  1472: astore          11
        //  1474: aload           11
        //  1476: astore          4
        //  1478: aload           5
        //  1480: astore          19
        //  1482: aload           7
        //  1484: astore          5
        //  1486: goto            2138
        //  1489: astore          5
        //  1491: aconst_null    
        //  1492: astore          4
        //  1494: aconst_null    
        //  1495: astore          11
        //  1497: aconst_null    
        //  1498: astore          13
        //  1500: aload           5
        //  1502: astore          6
        //  1504: aload           11
        //  1506: astore          5
        //  1508: aload           7
        //  1510: astore          11
        //  1512: aload           10
        //  1514: astore          7
        //  1516: aload           13
        //  1518: astore          10
        //  1520: goto            2415
        //  1523: astore          6
        //  1525: aconst_null    
        //  1526: astore          11
        //  1528: aconst_null    
        //  1529: astore          5
        //  1531: aload           5
        //  1533: astore          4
        //  1535: aload           6
        //  1537: astore          19
        //  1539: aload           7
        //  1541: astore          6
        //  1543: goto            2528
        //  1546: astore          4
        //  1548: aconst_null    
        //  1549: astore          5
        //  1551: aconst_null    
        //  1552: astore          6
        //  1554: aload           6
        //  1556: astore          13
        //  1558: aload           8
        //  1560: astore          11
        //  1562: aload           10
        //  1564: astore          7
        //  1566: aload           13
        //  1568: astore          10
        //  1570: goto            2774
        //  1573: astore          11
        //  1575: aconst_null    
        //  1576: astore          4
        //  1578: aconst_null    
        //  1579: astore          5
        //  1581: aload           5
        //  1583: astore          6
        //  1585: aload           6
        //  1587: astore          13
        //  1589: aload           10
        //  1591: astore          7
        //  1593: aload           13
        //  1595: astore          10
        //  1597: goto            1735
        //  1600: astore          19
        //  1602: aconst_null    
        //  1603: astore          4
        //  1605: aconst_null    
        //  1606: astore          5
        //  1608: aload           5
        //  1610: astore          6
        //  1612: aload           6
        //  1614: astore          7
        //  1616: aload           10
        //  1618: astore          11
        //  1620: aload           7
        //  1622: astore          10
        //  1624: goto            1850
        //  1627: astore          19
        //  1629: aconst_null    
        //  1630: astore          5
        //  1632: aconst_null    
        //  1633: astore          6
        //  1635: aload           6
        //  1637: astore          11
        //  1639: aload           11
        //  1641: astore          4
        //  1643: goto            2138
        //  1646: astore          6
        //  1648: aconst_null    
        //  1649: astore          4
        //  1651: aconst_null    
        //  1652: astore          5
        //  1654: aload           5
        //  1656: astore          13
        //  1658: aload           9
        //  1660: astore          11
        //  1662: aload           10
        //  1664: astore          7
        //  1666: aload           13
        //  1668: astore          10
        //  1670: goto            2415
        //  1673: astore          19
        //  1675: aconst_null    
        //  1676: astore          6
        //  1678: aconst_null    
        //  1679: astore          11
        //  1681: aload           11
        //  1683: astore          5
        //  1685: aload           5
        //  1687: astore          4
        //  1689: goto            2528
        //  1692: astore          4
        //  1694: aconst_null    
        //  1695: astore          5
        //  1697: aconst_null    
        //  1698: astore          6
        //  1700: aload           6
        //  1702: astore          7
        //  1704: aload           7
        //  1706: astore          10
        //  1708: aload           8
        //  1710: astore          11
        //  1712: goto            2774
        //  1715: astore          11
        //  1717: aconst_null    
        //  1718: astore          4
        //  1720: aconst_null    
        //  1721: astore          5
        //  1723: aload           5
        //  1725: astore          6
        //  1727: aload           6
        //  1729: astore          7
        //  1731: aload           7
        //  1733: astore          10
        //  1735: getstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  1738: iconst_1       
        //  1739: iadd           
        //  1740: putstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  1743: new             Ljava/lang/StringBuilder;
        //  1746: astore          13
        //  1748: aload           13
        //  1750: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1753: aload           13
        //  1755: ldc_w           "Error downloading MapTile: "
        //  1758: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1761: pop            
        //  1762: aload           13
        //  1764: lload_1        
        //  1765: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //  1768: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1771: pop            
        //  1772: ldc             "OsmDroid"
        //  1774: aload           13
        //  1776: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1779: aload           11
        //  1781: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //  1784: pop            
        //  1785: aload           4
        //  1787: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1790: aload           5
        //  1792: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1795: aload           10
        //  1797: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1800: aload           6
        //  1802: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  1805: aload           7
        //  1807: astore          10
        //  1809: aload           10
        //  1811: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  1814: goto            2772
        //  1817: astore          13
        //  1819: aload           4
        //  1821: astore          11
        //  1823: aload           13
        //  1825: astore          4
        //  1827: goto            2774
        //  1830: astore          19
        //  1832: aconst_null    
        //  1833: astore          4
        //  1835: aconst_null    
        //  1836: astore          5
        //  1838: aload           5
        //  1840: astore          6
        //  1842: aload           6
        //  1844: astore          11
        //  1846: aload           11
        //  1848: astore          10
        //  1850: aload           4
        //  1852: astore          16
        //  1854: aload           5
        //  1856: astore          8
        //  1858: aload           6
        //  1860: astore          9
        //  1862: aload           11
        //  1864: astore          7
        //  1866: aload           10
        //  1868: astore          13
        //  1870: getstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  1873: iconst_1       
        //  1874: iadd           
        //  1875: putstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  1878: aload           4
        //  1880: astore          16
        //  1882: aload           5
        //  1884: astore          8
        //  1886: aload           6
        //  1888: astore          9
        //  1890: aload           11
        //  1892: astore          7
        //  1894: aload           10
        //  1896: astore          13
        //  1898: new             Ljava/lang/StringBuilder;
        //  1901: astore          20
        //  1903: aload           4
        //  1905: astore          16
        //  1907: aload           5
        //  1909: astore          8
        //  1911: aload           6
        //  1913: astore          9
        //  1915: aload           11
        //  1917: astore          7
        //  1919: aload           10
        //  1921: astore          13
        //  1923: aload           20
        //  1925: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1928: aload           4
        //  1930: astore          16
        //  1932: aload           5
        //  1934: astore          8
        //  1936: aload           6
        //  1938: astore          9
        //  1940: aload           11
        //  1942: astore          7
        //  1944: aload           10
        //  1946: astore          13
        //  1948: aload           20
        //  1950: ldc_w           "IOException downloading MapTile: "
        //  1953: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1956: pop            
        //  1957: aload           4
        //  1959: astore          16
        //  1961: aload           5
        //  1963: astore          8
        //  1965: aload           6
        //  1967: astore          9
        //  1969: aload           11
        //  1971: astore          7
        //  1973: aload           10
        //  1975: astore          13
        //  1977: aload           20
        //  1979: lload_1        
        //  1980: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //  1983: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1986: pop            
        //  1987: aload           4
        //  1989: astore          16
        //  1991: aload           5
        //  1993: astore          8
        //  1995: aload           6
        //  1997: astore          9
        //  1999: aload           11
        //  2001: astore          7
        //  2003: aload           10
        //  2005: astore          13
        //  2007: aload           20
        //  2009: ldc_w           " : "
        //  2012: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2015: pop            
        //  2016: aload           4
        //  2018: astore          16
        //  2020: aload           5
        //  2022: astore          8
        //  2024: aload           6
        //  2026: astore          9
        //  2028: aload           11
        //  2030: astore          7
        //  2032: aload           10
        //  2034: astore          13
        //  2036: aload           20
        //  2038: aload           19
        //  2040: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  2043: pop            
        //  2044: aload           4
        //  2046: astore          16
        //  2048: aload           5
        //  2050: astore          8
        //  2052: aload           6
        //  2054: astore          9
        //  2056: aload           11
        //  2058: astore          7
        //  2060: aload           10
        //  2062: astore          13
        //  2064: ldc             "OsmDroid"
        //  2066: aload           20
        //  2068: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2071: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //  2074: pop            
        //  2075: aload           10
        //  2077: astore          13
        //  2079: aload           11
        //  2081: astore          10
        //  2083: aload           6
        //  2085: astore          11
        //  2087: aload           5
        //  2089: astore          6
        //  2091: aload           4
        //  2093: astore          7
        //  2095: aload           7
        //  2097: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2100: aload           6
        //  2102: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2105: aload           13
        //  2107: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2110: aload           11
        //  2112: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2115: goto            1809
        //  2118: astore          19
        //  2120: aconst_null    
        //  2121: astore          5
        //  2123: aconst_null    
        //  2124: astore          6
        //  2126: aload           6
        //  2128: astore          11
        //  2130: aload           11
        //  2132: astore          10
        //  2134: aload           10
        //  2136: astore          4
        //  2138: aload           5
        //  2140: astore          16
        //  2142: aload           6
        //  2144: astore          8
        //  2146: aload           11
        //  2148: astore          9
        //  2150: aload           10
        //  2152: astore          7
        //  2154: aload           4
        //  2156: astore          13
        //  2158: getstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  2161: iconst_1       
        //  2162: iadd           
        //  2163: putstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  2166: aload           5
        //  2168: astore          16
        //  2170: aload           6
        //  2172: astore          8
        //  2174: aload           11
        //  2176: astore          9
        //  2178: aload           10
        //  2180: astore          7
        //  2182: aload           4
        //  2184: astore          13
        //  2186: new             Ljava/lang/StringBuilder;
        //  2189: astore          20
        //  2191: aload           5
        //  2193: astore          16
        //  2195: aload           6
        //  2197: astore          8
        //  2199: aload           11
        //  2201: astore          9
        //  2203: aload           10
        //  2205: astore          7
        //  2207: aload           4
        //  2209: astore          13
        //  2211: aload           20
        //  2213: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2216: aload           5
        //  2218: astore          16
        //  2220: aload           6
        //  2222: astore          8
        //  2224: aload           11
        //  2226: astore          9
        //  2228: aload           10
        //  2230: astore          7
        //  2232: aload           4
        //  2234: astore          13
        //  2236: aload           20
        //  2238: ldc_w           "Tile not found: "
        //  2241: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2244: pop            
        //  2245: aload           5
        //  2247: astore          16
        //  2249: aload           6
        //  2251: astore          8
        //  2253: aload           11
        //  2255: astore          9
        //  2257: aload           10
        //  2259: astore          7
        //  2261: aload           4
        //  2263: astore          13
        //  2265: aload           20
        //  2267: lload_1        
        //  2268: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //  2271: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2274: pop            
        //  2275: aload           5
        //  2277: astore          16
        //  2279: aload           6
        //  2281: astore          8
        //  2283: aload           11
        //  2285: astore          9
        //  2287: aload           10
        //  2289: astore          7
        //  2291: aload           4
        //  2293: astore          13
        //  2295: aload           20
        //  2297: ldc_w           " : "
        //  2300: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2303: pop            
        //  2304: aload           5
        //  2306: astore          16
        //  2308: aload           6
        //  2310: astore          8
        //  2312: aload           11
        //  2314: astore          9
        //  2316: aload           10
        //  2318: astore          7
        //  2320: aload           4
        //  2322: astore          13
        //  2324: aload           20
        //  2326: aload           19
        //  2328: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  2331: pop            
        //  2332: aload           5
        //  2334: astore          16
        //  2336: aload           6
        //  2338: astore          8
        //  2340: aload           11
        //  2342: astore          9
        //  2344: aload           10
        //  2346: astore          7
        //  2348: aload           4
        //  2350: astore          13
        //  2352: ldc             "OsmDroid"
        //  2354: aload           20
        //  2356: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2359: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //  2362: pop            
        //  2363: aload           5
        //  2365: astore          7
        //  2367: aload           4
        //  2369: astore          13
        //  2371: goto            2095
        //  2374: astore          4
        //  2376: aload           16
        //  2378: astore          11
        //  2380: aload           8
        //  2382: astore          5
        //  2384: aload           9
        //  2386: astore          6
        //  2388: aload           13
        //  2390: astore          10
        //  2392: goto            2774
        //  2395: astore          6
        //  2397: aconst_null    
        //  2398: astore          4
        //  2400: aconst_null    
        //  2401: astore          5
        //  2403: aload           5
        //  2405: astore          7
        //  2407: aload           7
        //  2409: astore          10
        //  2411: aload           9
        //  2413: astore          11
        //  2415: getstatic       org/osmdroid/tileprovider/util/Counters.countOOM:I
        //  2418: iconst_1       
        //  2419: iadd           
        //  2420: putstatic       org/osmdroid/tileprovider/util/Counters.countOOM:I
        //  2423: new             Ljava/lang/StringBuilder;
        //  2426: astore          13
        //  2428: aload           13
        //  2430: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2433: aload           13
        //  2435: ldc_w           "LowMemoryException downloading MapTile: "
        //  2438: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2441: pop            
        //  2442: aload           13
        //  2444: lload_1        
        //  2445: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //  2448: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2451: pop            
        //  2452: aload           13
        //  2454: ldc_w           " : "
        //  2457: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2460: pop            
        //  2461: aload           13
        //  2463: aload           6
        //  2465: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  2468: pop            
        //  2469: ldc             "OsmDroid"
        //  2471: aload           13
        //  2473: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2476: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //  2479: pop            
        //  2480: new             Lorg/osmdroid/tileprovider/modules/CantContinueException;
        //  2483: astore          13
        //  2485: aload           13
        //  2487: aload           6
        //  2489: invokespecial   org/osmdroid/tileprovider/modules/CantContinueException.<init>:(Ljava/lang/Throwable;)V
        //  2492: aload           13
        //  2494: athrow         
        //  2495: astore          13
        //  2497: aload           4
        //  2499: astore          6
        //  2501: aload           13
        //  2503: astore          4
        //  2505: goto            2774
        //  2508: astore          19
        //  2510: aconst_null    
        //  2511: astore          6
        //  2513: aconst_null    
        //  2514: astore          11
        //  2516: aload           11
        //  2518: astore          5
        //  2520: aload           5
        //  2522: astore          10
        //  2524: aload           10
        //  2526: astore          4
        //  2528: aload           6
        //  2530: astore          16
        //  2532: aload           11
        //  2534: astore          8
        //  2536: aload           5
        //  2538: astore          9
        //  2540: aload           10
        //  2542: astore          7
        //  2544: aload           4
        //  2546: astore          13
        //  2548: new             Ljava/lang/StringBuilder;
        //  2551: astore          20
        //  2553: aload           6
        //  2555: astore          16
        //  2557: aload           11
        //  2559: astore          8
        //  2561: aload           5
        //  2563: astore          9
        //  2565: aload           10
        //  2567: astore          7
        //  2569: aload           4
        //  2571: astore          13
        //  2573: aload           20
        //  2575: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2578: aload           6
        //  2580: astore          16
        //  2582: aload           11
        //  2584: astore          8
        //  2586: aload           5
        //  2588: astore          9
        //  2590: aload           10
        //  2592: astore          7
        //  2594: aload           4
        //  2596: astore          13
        //  2598: aload           20
        //  2600: ldc_w           "UnknownHostException downloading MapTile: "
        //  2603: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2606: pop            
        //  2607: aload           6
        //  2609: astore          16
        //  2611: aload           11
        //  2613: astore          8
        //  2615: aload           5
        //  2617: astore          9
        //  2619: aload           10
        //  2621: astore          7
        //  2623: aload           4
        //  2625: astore          13
        //  2627: aload           20
        //  2629: lload_1        
        //  2630: invokestatic    org/osmdroid/util/MapTileIndex.toString:(J)Ljava/lang/String;
        //  2633: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2636: pop            
        //  2637: aload           6
        //  2639: astore          16
        //  2641: aload           11
        //  2643: astore          8
        //  2645: aload           5
        //  2647: astore          9
        //  2649: aload           10
        //  2651: astore          7
        //  2653: aload           4
        //  2655: astore          13
        //  2657: aload           20
        //  2659: ldc_w           " : "
        //  2662: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2665: pop            
        //  2666: aload           6
        //  2668: astore          16
        //  2670: aload           11
        //  2672: astore          8
        //  2674: aload           5
        //  2676: astore          9
        //  2678: aload           10
        //  2680: astore          7
        //  2682: aload           4
        //  2684: astore          13
        //  2686: aload           20
        //  2688: aload           19
        //  2690: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  2693: pop            
        //  2694: aload           6
        //  2696: astore          16
        //  2698: aload           11
        //  2700: astore          8
        //  2702: aload           5
        //  2704: astore          9
        //  2706: aload           10
        //  2708: astore          7
        //  2710: aload           4
        //  2712: astore          13
        //  2714: ldc             "OsmDroid"
        //  2716: aload           20
        //  2718: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2721: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //  2724: pop            
        //  2725: aload           6
        //  2727: astore          16
        //  2729: aload           11
        //  2731: astore          8
        //  2733: aload           5
        //  2735: astore          9
        //  2737: aload           10
        //  2739: astore          7
        //  2741: aload           4
        //  2743: astore          13
        //  2745: getstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  2748: iconst_1       
        //  2749: iadd           
        //  2750: putstatic       org/osmdroid/tileprovider/util/Counters.tileDownloadErrors:I
        //  2753: aload           6
        //  2755: astore          7
        //  2757: aload           11
        //  2759: astore          6
        //  2761: aload           5
        //  2763: astore          11
        //  2765: aload           4
        //  2767: astore          13
        //  2769: goto            2095
        //  2772: aconst_null    
        //  2773: areturn        
        //  2774: aload           11
        //  2776: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2779: aload           5
        //  2781: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2784: aload           10
        //  2786: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2789: aload           6
        //  2791: invokestatic    org/osmdroid/tileprovider/util/StreamUtils.closeStream:(Ljava/io/Closeable;)V
        //  2794: aload           7
        //  2796: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //  2799: aload           4
        //  2801: athrow         
        //  2802: astore          4
        //  2804: goto            198
        //  2807: astore          5
        //  2809: goto            707
        //  2812: astore          4
        //  2814: goto            824
        //  2817: astore          4
        //  2819: goto            1121
        //  2822: astore          4
        //  2824: goto            2772
        //  2827: astore          5
        //  2829: goto            2799
        //    Exceptions:
        //  throws org.osmdroid.tileprovider.modules.CantContinueException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                                          
        //  -----  -----  -----  -----  ------------------------------------------------------------------------------
        //  114    162    2508   2528   Ljava/net/UnknownHostException;
        //  114    162    2395   2415   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  114    162    2118   2138   Ljava/io/FileNotFoundException;
        //  114    162    1830   1850   Ljava/io/IOException;
        //  114    162    1715   1735   Ljava/lang/Throwable;
        //  114    162    1692   1715   Any
        //  162    169    2508   2528   Ljava/net/UnknownHostException;
        //  162    169    2395   2415   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  162    169    2118   2138   Ljava/io/FileNotFoundException;
        //  162    169    1830   1850   Ljava/io/IOException;
        //  162    169    1715   1735   Ljava/lang/Throwable;
        //  162    169    1692   1715   Any
        //  190    198    2802   200    Ljava/lang/Exception;
        //  200    241    2508   2528   Ljava/net/UnknownHostException;
        //  200    241    2395   2415   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  200    241    2118   2138   Ljava/io/FileNotFoundException;
        //  200    241    1830   1850   Ljava/io/IOException;
        //  200    241    1715   1735   Ljava/lang/Throwable;
        //  200    241    1692   1715   Any
        //  244    266    2508   2528   Ljava/net/UnknownHostException;
        //  244    266    2395   2415   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  244    266    2118   2138   Ljava/io/FileNotFoundException;
        //  244    266    1830   1850   Ljava/io/IOException;
        //  244    266    1715   1735   Ljava/lang/Throwable;
        //  244    266    1692   1715   Any
        //  266    307    1673   1692   Ljava/net/UnknownHostException;
        //  266    307    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  266    307    1627   1646   Ljava/io/FileNotFoundException;
        //  266    307    1600   1627   Ljava/io/IOException;
        //  266    307    1573   1600   Ljava/lang/Throwable;
        //  266    307    1546   1573   Any
        //  307    354    1673   1692   Ljava/net/UnknownHostException;
        //  307    354    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  307    354    1627   1646   Ljava/io/FileNotFoundException;
        //  307    354    1600   1627   Ljava/io/IOException;
        //  307    354    1573   1600   Ljava/lang/Throwable;
        //  307    354    1546   1573   Any
        //  357    380    1673   1692   Ljava/net/UnknownHostException;
        //  357    380    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  357    380    1627   1646   Ljava/io/FileNotFoundException;
        //  357    380    1600   1627   Ljava/io/IOException;
        //  357    380    1573   1600   Ljava/lang/Throwable;
        //  357    380    1546   1573   Any
        //  415    435    1673   1692   Ljava/net/UnknownHostException;
        //  415    435    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  415    435    1627   1646   Ljava/io/FileNotFoundException;
        //  415    435    1600   1627   Ljava/io/IOException;
        //  415    435    1573   1600   Ljava/lang/Throwable;
        //  415    435    1546   1573   Any
        //  444    485    1673   1692   Ljava/net/UnknownHostException;
        //  444    485    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  444    485    1627   1646   Ljava/io/FileNotFoundException;
        //  444    485    1600   1627   Ljava/io/IOException;
        //  444    485    1573   1600   Ljava/lang/Throwable;
        //  444    485    1546   1573   Any
        //  495    508    1673   1692   Ljava/net/UnknownHostException;
        //  495    508    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  495    508    1627   1646   Ljava/io/FileNotFoundException;
        //  495    508    1600   1627   Ljava/io/IOException;
        //  495    508    1573   1600   Ljava/lang/Throwable;
        //  495    508    1546   1573   Any
        //  520    530    1673   1692   Ljava/net/UnknownHostException;
        //  520    530    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  520    530    1627   1646   Ljava/io/FileNotFoundException;
        //  520    530    1600   1627   Ljava/io/IOException;
        //  520    530    1573   1600   Ljava/lang/Throwable;
        //  520    530    1546   1573   Any
        //  546    596    1673   1692   Ljava/net/UnknownHostException;
        //  546    596    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  546    596    1627   1646   Ljava/io/FileNotFoundException;
        //  546    596    1600   1627   Ljava/io/IOException;
        //  546    596    1573   1600   Ljava/lang/Throwable;
        //  546    596    1546   1573   Any
        //  596    686    1673   1692   Ljava/net/UnknownHostException;
        //  596    686    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  596    686    1627   1646   Ljava/io/FileNotFoundException;
        //  596    686    1600   1627   Ljava/io/IOException;
        //  596    686    1573   1600   Ljava/lang/Throwable;
        //  596    686    1546   1573   Any
        //  702    707    2807   2812   Ljava/lang/Exception;
        //  710    787    1673   1692   Ljava/net/UnknownHostException;
        //  710    787    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  710    787    1627   1646   Ljava/io/FileNotFoundException;
        //  710    787    1600   1627   Ljava/io/IOException;
        //  710    787    1573   1600   Ljava/lang/Throwable;
        //  710    787    1546   1573   Any
        //  787    802    1673   1692   Ljava/net/UnknownHostException;
        //  787    802    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  787    802    1627   1646   Ljava/io/FileNotFoundException;
        //  787    802    1600   1627   Ljava/io/IOException;
        //  787    802    1573   1600   Ljava/lang/Throwable;
        //  787    802    1546   1573   Any
        //  819    824    2812   2817   Ljava/lang/Exception;
        //  826    893    1673   1692   Ljava/net/UnknownHostException;
        //  826    893    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  826    893    1627   1646   Ljava/io/FileNotFoundException;
        //  826    893    1600   1627   Ljava/io/IOException;
        //  826    893    1573   1600   Ljava/lang/Throwable;
        //  826    893    1546   1573   Any
        //  898    958    1673   1692   Ljava/net/UnknownHostException;
        //  898    958    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  898    958    1627   1646   Ljava/io/FileNotFoundException;
        //  898    958    1600   1627   Ljava/io/IOException;
        //  898    958    1573   1600   Ljava/lang/Throwable;
        //  898    958    1546   1573   Any
        //  958    965    1673   1692   Ljava/net/UnknownHostException;
        //  958    965    1646   1673   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  958    965    1627   1646   Ljava/io/FileNotFoundException;
        //  958    965    1600   1627   Ljava/io/IOException;
        //  958    965    1573   1600   Ljava/lang/Throwable;
        //  958    965    1546   1573   Any
        //  965    975    1523   1535   Ljava/net/UnknownHostException;
        //  965    975    1489   1494   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  965    975    1466   1478   Ljava/io/FileNotFoundException;
        //  965    975    1435   1447   Ljava/io/IOException;
        //  965    975    1404   1416   Ljava/lang/Throwable;
        //  965    975    1370   1378   Any
        //  975    990    1347   1355   Ljava/net/UnknownHostException;
        //  975    990    1342   1347   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  975    990    1323   1331   Ljava/io/FileNotFoundException;
        //  975    990    1300   1308   Ljava/io/IOException;
        //  975    990    1277   1285   Ljava/lang/Throwable;
        //  975    990    1261   1277   Any
        //  990    1015   1253   1255   Ljava/net/UnknownHostException;
        //  990    1015   1245   1247   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  990    1015   1233   1235   Ljava/io/FileNotFoundException;
        //  990    1015   1225   1227   Ljava/io/IOException;
        //  990    1015   1217   1219   Ljava/lang/Throwable;
        //  990    1015   1200   1202   Any
        //  1015   1059   1195   1200   Ljava/net/UnknownHostException;
        //  1015   1059   1190   1195   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  1015   1059   1185   1190   Ljava/io/FileNotFoundException;
        //  1015   1059   1180   1185   Ljava/io/IOException;
        //  1015   1059   1175   1180   Ljava/lang/Throwable;
        //  1015   1059   1170   1175   Any
        //  1064   1087   1165   1170   Ljava/net/UnknownHostException;
        //  1064   1087   1160   1165   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  1064   1087   1151   1160   Ljava/io/FileNotFoundException;
        //  1064   1087   1146   1151   Ljava/io/IOException;
        //  1064   1087   1141   1146   Ljava/lang/Throwable;
        //  1064   1087   1124   1141   Any
        //  1087   1096   1165   1170   Ljava/net/UnknownHostException;
        //  1087   1096   1160   1165   Lorg/osmdroid/tileprovider/tilesource/BitmapTileSourceBase$LowMemoryException;
        //  1087   1096   1151   1160   Ljava/io/FileNotFoundException;
        //  1087   1096   1146   1151   Ljava/io/IOException;
        //  1087   1096   1141   1146   Ljava/lang/Throwable;
        //  1087   1096   1124   1141   Any
        //  1116   1121   2817   2822   Ljava/lang/Exception;
        //  1735   1785   1817   1830   Any
        //  1809   1814   2822   2827   Ljava/lang/Exception;
        //  1870   1878   2374   2395   Any
        //  1898   1903   2374   2395   Any
        //  1923   1928   2374   2395   Any
        //  1948   1957   2374   2395   Any
        //  1977   1987   2374   2395   Any
        //  2007   2016   2374   2395   Any
        //  2036   2044   2374   2395   Any
        //  2064   2075   2374   2395   Any
        //  2158   2166   2374   2395   Any
        //  2186   2191   2374   2395   Any
        //  2211   2216   2374   2395   Any
        //  2236   2245   2374   2395   Any
        //  2265   2275   2374   2395   Any
        //  2295   2304   2374   2395   Any
        //  2324   2332   2374   2395   Any
        //  2352   2363   2374   2395   Any
        //  2415   2495   2495   2508   Any
        //  2548   2553   2374   2395   Any
        //  2573   2578   2374   2395   Any
        //  2598   2607   2374   2395   Any
        //  2627   2637   2374   2395   Any
        //  2657   2666   2374   2395   Any
        //  2686   2694   2374   2395   Any
        //  2714   2725   2374   2395   Any
        //  2745   2753   2374   2395   Any
        //  2794   2799   2827   2832   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 1318 out-of-bounds for length 1318
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
    
    public Long getHttpCacheControlDuration(final String str) {
        if (str != null && str.length() > 0) {
            try {
                for (final String s : str.split(", ")) {
                    if (s.indexOf("max-age=") == 0) {
                        return Long.valueOf(s.substring(8));
                    }
                }
                return null;
            }
            catch (Exception ex) {
                if (Configuration.getInstance().isDebugMapTileDownloader()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to parse cache control tag for tile, server returned ");
                    sb.append(str);
                    Log.d("OsmDroid", sb.toString(), (Throwable)ex);
                }
            }
        }
        return null;
    }
    
    public Long getHttpExpiresTime(final String s) {
        if (s != null && s.length() > 0) {
            try {
                return Configuration.getInstance().getHttpHeaderDateTimeFormat().parse(s).getTime();
            }
            catch (Exception ex) {
                if (Configuration.getInstance().isDebugMapTileDownloader()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to parse expiration tag for tile, server returned ");
                    sb.append(s);
                    Log.d("OsmDroid", sb.toString(), (Throwable)ex);
                }
            }
        }
        return null;
    }
}
