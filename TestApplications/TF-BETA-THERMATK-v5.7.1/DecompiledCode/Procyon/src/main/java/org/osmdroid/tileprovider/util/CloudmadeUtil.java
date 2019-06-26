// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.util;

import android.content.SharedPreferences$Editor;

public class CloudmadeUtil
{
    public static boolean DEBUGMODE = false;
    private static String mAndroidId = "android_id";
    private static String mKey = "";
    private static SharedPreferences$Editor mPreferenceEditor;
    private static String mToken = "";
    
    public static String getCloudmadeKey() {
        return CloudmadeUtil.mKey;
    }
    
    public static String getCloudmadeToken() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokevirtual   java/lang/String.length:()I
        //     6: ifne            659
        //     9: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mToken:Ljava/lang/String;
        //    12: astore_0       
        //    13: aload_0        
        //    14: monitorenter   
        //    15: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mToken:Ljava/lang/String;
        //    18: invokevirtual   java/lang/String.length:()I
        //    21: ifne            649
        //    24: new             Ljava/lang/StringBuilder;
        //    27: astore_1       
        //    28: aload_1        
        //    29: invokespecial   java/lang/StringBuilder.<init>:()V
        //    32: aload_1        
        //    33: ldc             "http://auth.cloudmade.com/token/"
        //    35: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    38: pop            
        //    39: aload_1        
        //    40: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mKey:Ljava/lang/String;
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: pop            
        //    47: aload_1        
        //    48: ldc             "?userid="
        //    50: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    53: pop            
        //    54: aload_1        
        //    55: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mAndroidId:Ljava/lang/String;
        //    58: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    61: pop            
        //    62: aload_1        
        //    63: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    66: astore_1       
        //    67: aconst_null    
        //    68: astore_2       
        //    69: aconst_null    
        //    70: astore_3       
        //    71: aconst_null    
        //    72: astore          4
        //    74: new             Ljava/net/URL;
        //    77: astore          5
        //    79: aload           5
        //    81: aload_1        
        //    82: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    85: aload           5
        //    87: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    90: checkcast       Ljava/net/HttpURLConnection;
        //    93: astore          5
        //    95: aload           5
        //    97: iconst_1       
        //    98: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //   101: aload           5
        //   103: ldc             "POST"
        //   105: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //   108: aload           5
        //   110: ldc             "Content-Type"
        //   112: ldc             "application/x-www-form-urlencoded"
        //   114: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   117: aload           5
        //   119: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   122: invokeinterface org/osmdroid/config/IConfigurationProvider.getUserAgentHttpHeader:()Ljava/lang/String;
        //   127: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   130: invokeinterface org/osmdroid/config/IConfigurationProvider.getUserAgentValue:()Ljava/lang/String;
        //   135: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   138: invokestatic    org/osmdroid/config/Configuration.getInstance:()Lorg/osmdroid/config/IConfigurationProvider;
        //   141: invokeinterface org/osmdroid/config/IConfigurationProvider.getAdditionalHttpRequestProperties:()Ljava/util/Map;
        //   146: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   151: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   156: astore_2       
        //   157: aload_2        
        //   158: invokeinterface java/util/Iterator.hasNext:()Z
        //   163: ifeq            202
        //   166: aload_2        
        //   167: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   172: checkcast       Ljava/util/Map$Entry;
        //   175: astore_1       
        //   176: aload           5
        //   178: aload_1        
        //   179: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   184: checkcast       Ljava/lang/String;
        //   187: aload_1        
        //   188: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   193: checkcast       Ljava/lang/String;
        //   196: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   199: goto            157
        //   202: aload           5
        //   204: invokevirtual   java/net/HttpURLConnection.connect:()V
        //   207: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.DEBUGMODE:Z
        //   210: ifeq            248
        //   213: new             Ljava/lang/StringBuilder;
        //   216: astore_1       
        //   217: aload_1        
        //   218: invokespecial   java/lang/StringBuilder.<init>:()V
        //   221: aload_1        
        //   222: ldc             "Response from Cloudmade auth: "
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: pop            
        //   228: aload_1        
        //   229: aload           5
        //   231: invokevirtual   java/net/HttpURLConnection.getResponseMessage:()Ljava/lang/String;
        //   234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   237: pop            
        //   238: ldc             "OsmDroid"
        //   240: aload_1        
        //   241: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   244: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   247: pop            
        //   248: aload           5
        //   250: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   253: sipush          200
        //   256: if_icmpne       436
        //   259: new             Ljava/io/InputStreamReader;
        //   262: dup            
        //   263: aload           5
        //   265: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   268: ldc             "UTF-8"
        //   270: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/lang/String;)V
        //   273: astore_1       
        //   274: new             Ljava/io/BufferedReader;
        //   277: astore          4
        //   279: aload           4
        //   281: aload_1        
        //   282: sipush          8192
        //   285: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;I)V
        //   288: aload           4
        //   290: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   293: astore_3       
        //   294: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.DEBUGMODE:Z
        //   297: ifeq            331
        //   300: new             Ljava/lang/StringBuilder;
        //   303: astore_2       
        //   304: aload_2        
        //   305: invokespecial   java/lang/StringBuilder.<init>:()V
        //   308: aload_2        
        //   309: ldc             "First line from Cloudmade auth: "
        //   311: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   314: pop            
        //   315: aload_2        
        //   316: aload_3        
        //   317: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   320: pop            
        //   321: ldc             "OsmDroid"
        //   323: aload_2        
        //   324: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   327: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   330: pop            
        //   331: aload_3        
        //   332: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   335: putstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mToken:Ljava/lang/String;
        //   338: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mToken:Ljava/lang/String;
        //   341: invokevirtual   java/lang/String.length:()I
        //   344: ifle            377
        //   347: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mPreferenceEditor:Landroid/content/SharedPreferences$Editor;
        //   350: ldc             "CLOUDMADE_TOKEN"
        //   352: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mToken:Ljava/lang/String;
        //   355: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   360: pop            
        //   361: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mPreferenceEditor:Landroid/content/SharedPreferences$Editor;
        //   364: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //   369: pop            
        //   370: aconst_null    
        //   371: putstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mPreferenceEditor:Landroid/content/SharedPreferences$Editor;
        //   374: goto            385
        //   377: ldc             "OsmDroid"
        //   379: ldc             "No authorization token received from Cloudmade"
        //   381: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   384: pop            
        //   385: goto            438
        //   388: astore_2       
        //   389: aload           4
        //   391: astore_3       
        //   392: aload_1        
        //   393: astore          6
        //   395: aload           5
        //   397: astore          4
        //   399: aload_2        
        //   400: astore_1       
        //   401: aload           6
        //   403: astore          5
        //   405: goto            609
        //   408: astore_3       
        //   409: goto            522
        //   412: astore          4
        //   414: aload_1        
        //   415: astore_2       
        //   416: aload           4
        //   418: astore_1       
        //   419: aload           5
        //   421: astore          4
        //   423: aload_2        
        //   424: astore          5
        //   426: goto            609
        //   429: astore_3       
        //   430: aconst_null    
        //   431: astore          4
        //   433: goto            522
        //   436: aconst_null    
        //   437: astore_1       
        //   438: aload           5
        //   440: ifnull          453
        //   443: aload           5
        //   445: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   448: goto            453
        //   451: astore          5
        //   453: aload           4
        //   455: ifnull          468
        //   458: aload           4
        //   460: invokevirtual   java/io/BufferedReader.close:()V
        //   463: goto            468
        //   466: astore          5
        //   468: aload_1        
        //   469: ifnull          649
        //   472: aload_1        
        //   473: invokevirtual   java/io/InputStreamReader.close:()V
        //   476: goto            649
        //   479: astore_1       
        //   480: aconst_null    
        //   481: astore_2       
        //   482: aload           5
        //   484: astore          4
        //   486: aload_2        
        //   487: astore          5
        //   489: goto            609
        //   492: astore_3       
        //   493: aconst_null    
        //   494: astore          4
        //   496: aload           4
        //   498: astore_1       
        //   499: goto            522
        //   502: astore_1       
        //   503: aconst_null    
        //   504: astore          4
        //   506: aload           4
        //   508: astore          5
        //   510: goto            609
        //   513: astore_3       
        //   514: aconst_null    
        //   515: astore_1       
        //   516: aload_1        
        //   517: astore          4
        //   519: aload_2        
        //   520: astore          5
        //   522: new             Ljava/lang/StringBuilder;
        //   525: astore_2       
        //   526: aload_2        
        //   527: invokespecial   java/lang/StringBuilder.<init>:()V
        //   530: aload_2        
        //   531: ldc             "No authorization token received from Cloudmade: "
        //   533: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   536: pop            
        //   537: aload_2        
        //   538: aload_3        
        //   539: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   542: pop            
        //   543: ldc             "OsmDroid"
        //   545: aload_2        
        //   546: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   549: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   552: pop            
        //   553: aload           5
        //   555: ifnull          568
        //   558: aload           5
        //   560: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   563: goto            568
        //   566: astore          5
        //   568: aload           4
        //   570: ifnull          583
        //   573: aload           4
        //   575: invokevirtual   java/io/BufferedReader.close:()V
        //   578: goto            583
        //   581: astore          5
        //   583: aload_1        
        //   584: ifnull          649
        //   587: aload_1        
        //   588: invokevirtual   java/io/InputStreamReader.close:()V
        //   591: goto            649
        //   594: astore_3       
        //   595: aload           5
        //   597: astore_2       
        //   598: aload_1        
        //   599: astore          5
        //   601: aload_3        
        //   602: astore_1       
        //   603: aload           4
        //   605: astore_3       
        //   606: aload_2        
        //   607: astore          4
        //   609: aload           4
        //   611: ifnull          624
        //   614: aload           4
        //   616: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   619: goto            624
        //   622: astore          4
        //   624: aload_3        
        //   625: ifnull          637
        //   628: aload_3        
        //   629: invokevirtual   java/io/BufferedReader.close:()V
        //   632: goto            637
        //   635: astore          4
        //   637: aload           5
        //   639: ifnull          647
        //   642: aload           5
        //   644: invokevirtual   java/io/InputStreamReader.close:()V
        //   647: aload_1        
        //   648: athrow         
        //   649: aload_0        
        //   650: monitorexit    
        //   651: goto            659
        //   654: astore_1       
        //   655: aload_0        
        //   656: monitorexit    
        //   657: aload_1        
        //   658: athrow         
        //   659: getstatic       org/osmdroid/tileprovider/util/CloudmadeUtil.mToken:Ljava/lang/String;
        //   662: areturn        
        //   663: astore_1       
        //   664: goto            649
        //   667: astore          5
        //   669: goto            647
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  15     67     654    659    Any
        //  74     95     513    522    Ljava/io/IOException;
        //  74     95     502    513    Any
        //  95     157    492    502    Ljava/io/IOException;
        //  95     157    479    492    Any
        //  157    199    492    502    Ljava/io/IOException;
        //  157    199    479    492    Any
        //  202    248    492    502    Ljava/io/IOException;
        //  202    248    479    492    Any
        //  248    274    492    502    Ljava/io/IOException;
        //  248    274    479    492    Any
        //  274    288    429    436    Ljava/io/IOException;
        //  274    288    412    429    Any
        //  288    331    408    412    Ljava/io/IOException;
        //  288    331    388    408    Any
        //  331    374    408    412    Ljava/io/IOException;
        //  331    374    388    408    Any
        //  377    385    408    412    Ljava/io/IOException;
        //  377    385    388    408    Any
        //  443    448    451    453    Ljava/lang/Exception;
        //  443    448    654    659    Any
        //  458    463    466    468    Ljava/lang/Exception;
        //  458    463    654    659    Any
        //  472    476    663    667    Ljava/lang/Exception;
        //  472    476    654    659    Any
        //  522    553    594    609    Any
        //  558    563    566    568    Ljava/lang/Exception;
        //  558    563    654    659    Any
        //  573    578    581    583    Ljava/lang/Exception;
        //  573    578    654    659    Any
        //  587    591    663    667    Ljava/lang/Exception;
        //  587    591    654    659    Any
        //  614    619    622    624    Ljava/lang/Exception;
        //  614    619    654    659    Any
        //  628    632    635    637    Ljava/lang/Exception;
        //  628    632    654    659    Any
        //  642    647    667    672    Ljava/lang/Exception;
        //  642    647    654    659    Any
        //  647    649    654    659    Any
        //  649    651    654    659    Any
        //  655    657    654    659    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 319 out-of-bounds for length 319
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
}
