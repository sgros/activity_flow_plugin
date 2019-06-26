// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.os.Build;
import android.content.pm.ApplicationInfo;
import java.io.File;
import android.content.Context;

public class NativeLoader
{
    private static final String LIB_NAME = "tmessages.30";
    private static final String LIB_SO_NAME = "libtmessages.30.so";
    private static final int LIB_VERSION = 30;
    private static final String LOCALE_LIB_SO_NAME = "libtmessages.30loc.so";
    private static volatile boolean nativeLoaded = false;
    private String crashPath;
    
    public NativeLoader() {
        this.crashPath = "";
    }
    
    private static File getNativeLibraryDir(final Context context) {
        File file = null;
        Label_0039: {
            if (context != null) {
                try {
                    file = new File((String)ApplicationInfo.class.getField("nativeLibraryDir").get(context.getApplicationInfo()));
                    break Label_0039;
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            file = null;
        }
        File file2 = file;
        if (file == null) {
            file2 = new File(context.getApplicationInfo().dataDir, "lib");
        }
        if (file2.isDirectory()) {
            return file2;
        }
        return null;
    }
    
    private static native void init(final String p0, final boolean p1);
    
    @SuppressLint({ "UnsafeDynamicallyLoadedCode" })
    public static void initNativeLibs(final Context t) {
        synchronized (NativeLoader.class) {
            if (NativeLoader.nativeLoaded) {
                return;
            }
            Label_0378: {
                try {
                    try {
                        System.loadLibrary("tmessages.30");
                        NativeLoader.nativeLoaded = true;
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("loaded normal lib");
                        }
                        return;
                    }
                    catch (Throwable t) {}
                }
                catch (Error error) {
                    FileLog.e(error);
                    String s;
                    try {
                        final String cpu_ABI = Build.CPU_ABI;
                        if (Build.CPU_ABI.equalsIgnoreCase("x86_64")) {
                            s = "x86_64";
                        }
                        else if (Build.CPU_ABI.equalsIgnoreCase("arm64-v8a")) {
                            s = "arm64-v8a";
                        }
                        else if (Build.CPU_ABI.equalsIgnoreCase("armeabi-v7a")) {
                            s = "armeabi-v7a";
                        }
                        else if (Build.CPU_ABI.equalsIgnoreCase("armeabi")) {
                            s = "armeabi";
                        }
                        else if (Build.CPU_ABI.equalsIgnoreCase("x86")) {
                            s = "x86";
                        }
                        else if (Build.CPU_ABI.equalsIgnoreCase("mips")) {
                            s = "mips";
                        }
                        else {
                            final String s2 = s = "armeabi";
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unsupported arch: ");
                                sb.append(Build.CPU_ABI);
                                FileLog.e(sb.toString());
                                s = s2;
                            }
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                        s = "armeabi";
                    }
                    final String property = System.getProperty("os.arch");
                    String str = s;
                    if (property != null) {
                        str = s;
                        if (property.contains("686")) {
                            str = "x86";
                        }
                    }
                    final File parent = new File(((Context)t).getFilesDir(), "lib");
                    parent.mkdirs();
                    final File file = new File(parent, "libtmessages.30loc.so");
                    if (file.exists()) {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("Load local lib");
                            }
                            System.load(file.getAbsolutePath());
                            NativeLoader.nativeLoaded = true;
                            return;
                        }
                        catch (Error error2) {
                            FileLog.e(error2);
                            file.delete();
                        }
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Library not found, arch = ");
                        sb2.append(str);
                        FileLog.e(sb2.toString());
                    }
                    if (loadFromZip((Context)t, parent, file, str)) {
                        return;
                    }
                    break Label_0378;
                }
                t.printStackTrace();
                try {
                    System.loadLibrary("tmessages.30");
                    NativeLoader.nativeLoaded = true;
                }
                catch (Error error3) {
                    FileLog.e(error3);
                }
            }
        }
    }
    
    @SuppressLint({ "UnsafeDynamicallyLoadedCode", "SetWorldReadable" })
    private static boolean loadFromZip(final Context p0, final File p1, final File p2, final String p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //     4: astore_1       
        //     5: aload_1        
        //     6: arraylength    
        //     7: istore          4
        //     9: iconst_0       
        //    10: istore          5
        //    12: iload           5
        //    14: iload           4
        //    16: if_icmpge       38
        //    19: aload_1        
        //    20: iload           5
        //    22: aaload         
        //    23: invokevirtual   java/io/File.delete:()Z
        //    26: pop            
        //    27: iinc            5, 1
        //    30: goto            12
        //    33: astore_1       
        //    34: aload_1        
        //    35: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //    38: aconst_null    
        //    39: astore_1       
        //    40: aconst_null    
        //    41: astore          6
        //    43: aconst_null    
        //    44: astore          7
        //    46: aconst_null    
        //    47: astore          8
        //    49: new             Ljava/util/zip/ZipFile;
        //    52: astore          9
        //    54: aload           9
        //    56: aload_0        
        //    57: invokevirtual   android/content/Context.getApplicationInfo:()Landroid/content/pm/ApplicationInfo;
        //    60: getfield        android/content/pm/ApplicationInfo.sourceDir:Ljava/lang/String;
        //    63: invokespecial   java/util/zip/ZipFile.<init>:(Ljava/lang/String;)V
        //    66: aload           8
        //    68: astore          7
        //    70: aload           9
        //    72: astore_1       
        //    73: aload           6
        //    75: astore_0       
        //    76: new             Ljava/lang/StringBuilder;
        //    79: astore          10
        //    81: aload           8
        //    83: astore          7
        //    85: aload           9
        //    87: astore_1       
        //    88: aload           6
        //    90: astore_0       
        //    91: aload           10
        //    93: invokespecial   java/lang/StringBuilder.<init>:()V
        //    96: aload           8
        //    98: astore          7
        //   100: aload           9
        //   102: astore_1       
        //   103: aload           6
        //   105: astore_0       
        //   106: aload           10
        //   108: ldc             "lib/"
        //   110: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   113: pop            
        //   114: aload           8
        //   116: astore          7
        //   118: aload           9
        //   120: astore_1       
        //   121: aload           6
        //   123: astore_0       
        //   124: aload           10
        //   126: aload_3        
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: aload           8
        //   133: astore          7
        //   135: aload           9
        //   137: astore_1       
        //   138: aload           6
        //   140: astore_0       
        //   141: aload           10
        //   143: ldc             "/"
        //   145: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   148: pop            
        //   149: aload           8
        //   151: astore          7
        //   153: aload           9
        //   155: astore_1       
        //   156: aload           6
        //   158: astore_0       
        //   159: aload           10
        //   161: ldc             "libtmessages.30.so"
        //   163: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   166: pop            
        //   167: aload           8
        //   169: astore          7
        //   171: aload           9
        //   173: astore_1       
        //   174: aload           6
        //   176: astore_0       
        //   177: aload           9
        //   179: aload           10
        //   181: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   184: invokevirtual   java/util/zip/ZipFile.getEntry:(Ljava/lang/String;)Ljava/util/zip/ZipEntry;
        //   187: astore          10
        //   189: aload           10
        //   191: ifnull          438
        //   194: aload           8
        //   196: astore          7
        //   198: aload           9
        //   200: astore_1       
        //   201: aload           6
        //   203: astore_0       
        //   204: aload           9
        //   206: aload           10
        //   208: invokevirtual   java/util/zip/ZipFile.getInputStream:(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
        //   211: astore_3       
        //   212: aload_3        
        //   213: astore          7
        //   215: aload           9
        //   217: astore_1       
        //   218: aload_3        
        //   219: astore_0       
        //   220: new             Ljava/io/FileOutputStream;
        //   223: astore          6
        //   225: aload_3        
        //   226: astore          7
        //   228: aload           9
        //   230: astore_1       
        //   231: aload_3        
        //   232: astore_0       
        //   233: aload           6
        //   235: aload_2        
        //   236: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   239: aload_3        
        //   240: astore          7
        //   242: aload           9
        //   244: astore_1       
        //   245: aload_3        
        //   246: astore_0       
        //   247: sipush          4096
        //   250: newarray        B
        //   252: astore          8
        //   254: aload_3        
        //   255: astore          7
        //   257: aload           9
        //   259: astore_1       
        //   260: aload_3        
        //   261: astore_0       
        //   262: aload_3        
        //   263: aload           8
        //   265: invokevirtual   java/io/InputStream.read:([B)I
        //   268: istore          5
        //   270: iload           5
        //   272: ifle            307
        //   275: aload_3        
        //   276: astore          7
        //   278: aload           9
        //   280: astore_1       
        //   281: aload_3        
        //   282: astore_0       
        //   283: invokestatic    java/lang/Thread.yield:()V
        //   286: aload_3        
        //   287: astore          7
        //   289: aload           9
        //   291: astore_1       
        //   292: aload_3        
        //   293: astore_0       
        //   294: aload           6
        //   296: aload           8
        //   298: iconst_0       
        //   299: iload           5
        //   301: invokevirtual   java/io/FileOutputStream.write:([BII)V
        //   304: goto            254
        //   307: aload_3        
        //   308: astore          7
        //   310: aload           9
        //   312: astore_1       
        //   313: aload_3        
        //   314: astore_0       
        //   315: aload           6
        //   317: invokevirtual   java/io/FileOutputStream.close:()V
        //   320: aload_3        
        //   321: astore          7
        //   323: aload           9
        //   325: astore_1       
        //   326: aload_3        
        //   327: astore_0       
        //   328: aload_2        
        //   329: iconst_1       
        //   330: iconst_0       
        //   331: invokevirtual   java/io/File.setReadable:(ZZ)Z
        //   334: pop            
        //   335: aload_3        
        //   336: astore          7
        //   338: aload           9
        //   340: astore_1       
        //   341: aload_3        
        //   342: astore_0       
        //   343: aload_2        
        //   344: iconst_1       
        //   345: iconst_0       
        //   346: invokevirtual   java/io/File.setExecutable:(ZZ)Z
        //   349: pop            
        //   350: aload_3        
        //   351: astore          7
        //   353: aload           9
        //   355: astore_1       
        //   356: aload_3        
        //   357: astore_0       
        //   358: aload_2        
        //   359: iconst_1       
        //   360: invokevirtual   java/io/File.setWritable:(Z)Z
        //   363: pop            
        //   364: aload_3        
        //   365: astore          7
        //   367: aload           9
        //   369: astore_1       
        //   370: aload_3        
        //   371: astore_0       
        //   372: aload_2        
        //   373: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   376: invokestatic    java/lang/System.load:(Ljava/lang/String;)V
        //   379: aload_3        
        //   380: astore          7
        //   382: aload           9
        //   384: astore_1       
        //   385: aload_3        
        //   386: astore_0       
        //   387: iconst_1       
        //   388: putstatic       org/telegram/messenger/NativeLoader.nativeLoaded:Z
        //   391: goto            407
        //   394: astore_2       
        //   395: aload_3        
        //   396: astore          7
        //   398: aload           9
        //   400: astore_1       
        //   401: aload_3        
        //   402: astore_0       
        //   403: aload_2        
        //   404: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   407: aload_3        
        //   408: ifnull          423
        //   411: aload_3        
        //   412: invokevirtual   java/io/InputStream.close:()V
        //   415: goto            423
        //   418: astore_0       
        //   419: aload_0        
        //   420: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   423: aload           9
        //   425: invokevirtual   java/util/zip/ZipFile.close:()V
        //   428: goto            436
        //   431: astore_0       
        //   432: aload_0        
        //   433: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   436: iconst_1       
        //   437: ireturn        
        //   438: aload           8
        //   440: astore          7
        //   442: aload           9
        //   444: astore_1       
        //   445: aload           6
        //   447: astore_0       
        //   448: new             Ljava/lang/Exception;
        //   451: astore          10
        //   453: aload           8
        //   455: astore          7
        //   457: aload           9
        //   459: astore_1       
        //   460: aload           6
        //   462: astore_0       
        //   463: new             Ljava/lang/StringBuilder;
        //   466: astore_2       
        //   467: aload           8
        //   469: astore          7
        //   471: aload           9
        //   473: astore_1       
        //   474: aload           6
        //   476: astore_0       
        //   477: aload_2        
        //   478: invokespecial   java/lang/StringBuilder.<init>:()V
        //   481: aload           8
        //   483: astore          7
        //   485: aload           9
        //   487: astore_1       
        //   488: aload           6
        //   490: astore_0       
        //   491: aload_2        
        //   492: ldc             "Unable to find file in apk:lib/"
        //   494: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   497: pop            
        //   498: aload           8
        //   500: astore          7
        //   502: aload           9
        //   504: astore_1       
        //   505: aload           6
        //   507: astore_0       
        //   508: aload_2        
        //   509: aload_3        
        //   510: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   513: pop            
        //   514: aload           8
        //   516: astore          7
        //   518: aload           9
        //   520: astore_1       
        //   521: aload           6
        //   523: astore_0       
        //   524: aload_2        
        //   525: ldc             "/"
        //   527: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   530: pop            
        //   531: aload           8
        //   533: astore          7
        //   535: aload           9
        //   537: astore_1       
        //   538: aload           6
        //   540: astore_0       
        //   541: aload_2        
        //   542: ldc             "tmessages.30"
        //   544: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   547: pop            
        //   548: aload           8
        //   550: astore          7
        //   552: aload           9
        //   554: astore_1       
        //   555: aload           6
        //   557: astore_0       
        //   558: aload           10
        //   560: aload_2        
        //   561: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   564: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   567: aload           8
        //   569: astore          7
        //   571: aload           9
        //   573: astore_1       
        //   574: aload           6
        //   576: astore_0       
        //   577: aload           10
        //   579: athrow         
        //   580: astore_3       
        //   581: aload           9
        //   583: astore_2       
        //   584: goto            602
        //   587: astore_0       
        //   588: aconst_null    
        //   589: astore_1       
        //   590: aload           7
        //   592: astore_2       
        //   593: goto            652
        //   596: astore_3       
        //   597: aconst_null    
        //   598: astore_2       
        //   599: aload_1        
        //   600: astore          7
        //   602: aload_2        
        //   603: astore_1       
        //   604: aload           7
        //   606: astore_0       
        //   607: aload_3        
        //   608: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   611: aload           7
        //   613: ifnull          629
        //   616: aload           7
        //   618: invokevirtual   java/io/InputStream.close:()V
        //   621: goto            629
        //   624: astore_0       
        //   625: aload_0        
        //   626: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   629: aload_2        
        //   630: ifnull          645
        //   633: aload_2        
        //   634: invokevirtual   java/util/zip/ZipFile.close:()V
        //   637: goto            645
        //   640: astore_0       
        //   641: aload_0        
        //   642: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   645: iconst_0       
        //   646: ireturn        
        //   647: astore_3       
        //   648: aload_0        
        //   649: astore_2       
        //   650: aload_3        
        //   651: astore_0       
        //   652: aload_2        
        //   653: ifnull          668
        //   656: aload_2        
        //   657: invokevirtual   java/io/InputStream.close:()V
        //   660: goto            668
        //   663: astore_2       
        //   664: aload_2        
        //   665: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   668: aload_1        
        //   669: ifnull          684
        //   672: aload_1        
        //   673: invokevirtual   java/util/zip/ZipFile.close:()V
        //   676: goto            684
        //   679: astore_1       
        //   680: aload_1        
        //   681: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   684: aload_0        
        //   685: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      9      33     38     Ljava/lang/Exception;
        //  19     27     33     38     Ljava/lang/Exception;
        //  49     66     596    602    Ljava/lang/Exception;
        //  49     66     587    596    Any
        //  76     81     580    587    Ljava/lang/Exception;
        //  76     81     647    652    Any
        //  91     96     580    587    Ljava/lang/Exception;
        //  91     96     647    652    Any
        //  106    114    580    587    Ljava/lang/Exception;
        //  106    114    647    652    Any
        //  124    131    580    587    Ljava/lang/Exception;
        //  124    131    647    652    Any
        //  141    149    580    587    Ljava/lang/Exception;
        //  141    149    647    652    Any
        //  159    167    580    587    Ljava/lang/Exception;
        //  159    167    647    652    Any
        //  177    189    580    587    Ljava/lang/Exception;
        //  177    189    647    652    Any
        //  204    212    580    587    Ljava/lang/Exception;
        //  204    212    647    652    Any
        //  220    225    580    587    Ljava/lang/Exception;
        //  220    225    647    652    Any
        //  233    239    580    587    Ljava/lang/Exception;
        //  233    239    647    652    Any
        //  247    254    580    587    Ljava/lang/Exception;
        //  247    254    647    652    Any
        //  262    270    580    587    Ljava/lang/Exception;
        //  262    270    647    652    Any
        //  283    286    580    587    Ljava/lang/Exception;
        //  283    286    647    652    Any
        //  294    304    580    587    Ljava/lang/Exception;
        //  294    304    647    652    Any
        //  315    320    580    587    Ljava/lang/Exception;
        //  315    320    647    652    Any
        //  328    335    580    587    Ljava/lang/Exception;
        //  328    335    647    652    Any
        //  343    350    580    587    Ljava/lang/Exception;
        //  343    350    647    652    Any
        //  358    364    580    587    Ljava/lang/Exception;
        //  358    364    647    652    Any
        //  372    379    394    407    Ljava/lang/Error;
        //  372    379    580    587    Ljava/lang/Exception;
        //  372    379    647    652    Any
        //  387    391    394    407    Ljava/lang/Error;
        //  387    391    580    587    Ljava/lang/Exception;
        //  387    391    647    652    Any
        //  403    407    580    587    Ljava/lang/Exception;
        //  403    407    647    652    Any
        //  411    415    418    423    Ljava/lang/Exception;
        //  423    428    431    436    Ljava/lang/Exception;
        //  448    453    580    587    Ljava/lang/Exception;
        //  448    453    647    652    Any
        //  463    467    580    587    Ljava/lang/Exception;
        //  463    467    647    652    Any
        //  477    481    580    587    Ljava/lang/Exception;
        //  477    481    647    652    Any
        //  491    498    580    587    Ljava/lang/Exception;
        //  491    498    647    652    Any
        //  508    514    580    587    Ljava/lang/Exception;
        //  508    514    647    652    Any
        //  524    531    580    587    Ljava/lang/Exception;
        //  524    531    647    652    Any
        //  541    548    580    587    Ljava/lang/Exception;
        //  541    548    647    652    Any
        //  558    567    580    587    Ljava/lang/Exception;
        //  558    567    647    652    Any
        //  577    580    580    587    Ljava/lang/Exception;
        //  577    580    647    652    Any
        //  607    611    647    652    Any
        //  616    621    624    629    Ljava/lang/Exception;
        //  633    637    640    645    Ljava/lang/Exception;
        //  656    660    663    668    Ljava/lang/Exception;
        //  672    676    679    684    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0254:
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
}
