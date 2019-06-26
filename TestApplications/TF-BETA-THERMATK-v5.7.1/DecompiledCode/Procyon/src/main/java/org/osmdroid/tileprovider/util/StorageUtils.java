// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.util;

import java.util.UUID;
import android.os.Build$VERSION;
import android.util.Log;
import java.io.FileOutputStream;
import java.util.List;
import android.os.Environment;
import java.io.File;
import java.util.Set;

public class StorageUtils
{
    private static Set<File> getAllStorageLocationsRevised() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/HashSet.<init>:()V
        //     7: astore_0       
        //     8: ldc             "EXTERNAL_STORAGE"
        //    10: invokestatic    java/lang/System.getenv:(Ljava/lang/String;)Ljava/lang/String;
        //    13: astore_1       
        //    14: aload_1        
        //    15: ifnull          67
        //    18: new             Ljava/lang/StringBuilder;
        //    21: dup            
        //    22: invokespecial   java/lang/StringBuilder.<init>:()V
        //    25: astore_2       
        //    26: aload_2        
        //    27: aload_1        
        //    28: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    31: pop            
        //    32: aload_2        
        //    33: getstatic       java/io/File.separator:Ljava/lang/String;
        //    36: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    39: pop            
        //    40: new             Ljava/io/File;
        //    43: dup            
        //    44: aload_2        
        //    45: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    48: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    51: astore_2       
        //    52: aload_2        
        //    53: invokestatic    org/osmdroid/tileprovider/util/StorageUtils.isWritable:(Ljava/io/File;)Z
        //    56: ifeq            67
        //    59: aload_0        
        //    60: aload_2        
        //    61: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //    66: pop            
        //    67: ldc             "SECONDARY_STORAGE"
        //    69: invokestatic    java/lang/System.getenv:(Ljava/lang/String;)Ljava/lang/String;
        //    72: astore_2       
        //    73: aload_2        
        //    74: ifnull          150
        //    77: aload_2        
        //    78: getstatic       java/io/File.pathSeparator:Ljava/lang/String;
        //    81: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    84: astore_2       
        //    85: iconst_0       
        //    86: istore_3       
        //    87: iload_3        
        //    88: aload_2        
        //    89: arraylength    
        //    90: if_icmpge       150
        //    93: new             Ljava/lang/StringBuilder;
        //    96: dup            
        //    97: invokespecial   java/lang/StringBuilder.<init>:()V
        //   100: astore_1       
        //   101: aload_1        
        //   102: aload_2        
        //   103: iload_3        
        //   104: aaload         
        //   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   108: pop            
        //   109: aload_1        
        //   110: getstatic       java/io/File.separator:Ljava/lang/String;
        //   113: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   116: pop            
        //   117: new             Ljava/io/File;
        //   120: dup            
        //   121: aload_1        
        //   122: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   125: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   128: astore_1       
        //   129: aload_1        
        //   130: invokestatic    org/osmdroid/tileprovider/util/StorageUtils.isWritable:(Ljava/io/File;)Z
        //   133: ifeq            144
        //   136: aload_0        
        //   137: aload_1        
        //   138: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   143: pop            
        //   144: iinc            3, 1
        //   147: goto            87
        //   150: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //   153: ifnull          175
        //   156: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //   159: astore_2       
        //   160: aload_2        
        //   161: invokestatic    org/osmdroid/tileprovider/util/StorageUtils.isWritable:(Ljava/io/File;)Z
        //   164: ifeq            175
        //   167: aload_0        
        //   168: aload_2        
        //   169: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   174: pop            
        //   175: new             Ljava/util/ArrayList;
        //   178: dup            
        //   179: bipush          10
        //   181: invokespecial   java/util/ArrayList.<init>:(I)V
        //   184: astore          4
        //   186: new             Ljava/util/ArrayList;
        //   189: dup            
        //   190: bipush          10
        //   192: invokespecial   java/util/ArrayList.<init>:(I)V
        //   195: astore          5
        //   197: aload           4
        //   199: ldc             "/mnt/sdcard"
        //   201: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   206: pop            
        //   207: aload           5
        //   209: ldc             "/mnt/sdcard"
        //   211: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   216: pop            
        //   217: aconst_null    
        //   218: astore          6
        //   220: aconst_null    
        //   221: astore          7
        //   223: aconst_null    
        //   224: astore          8
        //   226: new             Ljava/io/File;
        //   229: astore_1       
        //   230: aload_1        
        //   231: ldc             "/proc/mounts"
        //   233: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   236: aload_1        
        //   237: invokevirtual   java/io/File.exists:()Z
        //   240: ifeq            329
        //   243: new             Ljava/util/Scanner;
        //   246: astore_2       
        //   247: aload_2        
        //   248: aload_1        
        //   249: invokespecial   java/util/Scanner.<init>:(Ljava/io/File;)V
        //   252: aload_2        
        //   253: astore          9
        //   255: aload_2        
        //   256: astore_1       
        //   257: aload_2        
        //   258: invokevirtual   java/util/Scanner.hasNext:()Z
        //   261: ifeq            332
        //   264: aload_2        
        //   265: astore_1       
        //   266: aload_2        
        //   267: invokevirtual   java/util/Scanner.nextLine:()Ljava/lang/String;
        //   270: astore          9
        //   272: aload_2        
        //   273: astore_1       
        //   274: aload           9
        //   276: ldc             "/dev/block/vold/"
        //   278: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   281: ifeq            252
        //   284: aload_2        
        //   285: astore_1       
        //   286: aload           9
        //   288: ldc             " "
        //   290: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   293: iconst_1       
        //   294: aaload         
        //   295: astore          9
        //   297: aload_2        
        //   298: astore_1       
        //   299: aload           9
        //   301: ldc             "/mnt/sdcard"
        //   303: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   306: ifne            252
        //   309: aload_2        
        //   310: astore_1       
        //   311: aload           4
        //   313: aload           9
        //   315: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   320: pop            
        //   321: goto            252
        //   324: astore          9
        //   326: goto            357
        //   329: aconst_null    
        //   330: astore          9
        //   332: aload           9
        //   334: ifnull          371
        //   337: aload           9
        //   339: astore_2       
        //   340: aload_2        
        //   341: invokevirtual   java/util/Scanner.close:()V
        //   344: goto            371
        //   347: astore_2       
        //   348: aconst_null    
        //   349: astore_1       
        //   350: goto            932
        //   353: astore          9
        //   355: aconst_null    
        //   356: astore_2       
        //   357: aload_2        
        //   358: astore_1       
        //   359: aload           9
        //   361: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   364: aload_2        
        //   365: ifnull          371
        //   368: goto            340
        //   371: aload           6
        //   373: astore_1       
        //   374: new             Ljava/io/File;
        //   377: astore          9
        //   379: aload           6
        //   381: astore_1       
        //   382: aload           9
        //   384: ldc             "/system/etc/vold.fstab"
        //   386: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   389: aload           8
        //   391: astore_2       
        //   392: aload           6
        //   394: astore_1       
        //   395: aload           9
        //   397: invokevirtual   java/io/File.exists:()Z
        //   400: ifeq            510
        //   403: aload           6
        //   405: astore_1       
        //   406: new             Ljava/util/Scanner;
        //   409: astore_2       
        //   410: aload           6
        //   412: astore_1       
        //   413: aload_2        
        //   414: aload           9
        //   416: invokespecial   java/util/Scanner.<init>:(Ljava/io/File;)V
        //   419: aload_2        
        //   420: invokevirtual   java/util/Scanner.hasNext:()Z
        //   423: ifeq            498
        //   426: aload_2        
        //   427: invokevirtual   java/util/Scanner.nextLine:()Ljava/lang/String;
        //   430: astore_1       
        //   431: aload_1        
        //   432: ldc             "dev_mount"
        //   434: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   437: ifeq            419
        //   440: aload_1        
        //   441: ldc             " "
        //   443: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   446: iconst_2       
        //   447: aaload         
        //   448: astore          9
        //   450: aload           9
        //   452: astore_1       
        //   453: aload           9
        //   455: ldc             ":"
        //   457: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   460: ifeq            477
        //   463: aload           9
        //   465: iconst_0       
        //   466: aload           9
        //   468: ldc             ":"
        //   470: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   473: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   476: astore_1       
        //   477: aload_1        
        //   478: ldc             "/mnt/sdcard"
        //   480: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   483: ifne            419
        //   486: aload           5
        //   488: aload_1        
        //   489: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   494: pop            
        //   495: goto            419
        //   498: goto            510
        //   501: astore_1       
        //   502: goto            921
        //   505: astore          9
        //   507: goto            536
        //   510: aload_2        
        //   511: ifnull          550
        //   514: aload_2        
        //   515: invokevirtual   java/util/Scanner.close:()V
        //   518: goto            550
        //   521: astore          9
        //   523: aload_1        
        //   524: astore_2       
        //   525: aload           9
        //   527: astore_1       
        //   528: goto            921
        //   531: astore          9
        //   533: aload           7
        //   535: astore_2       
        //   536: aload_2        
        //   537: astore_1       
        //   538: aload           9
        //   540: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   543: aload_2        
        //   544: ifnull          550
        //   547: goto            514
        //   550: iconst_0       
        //   551: istore_3       
        //   552: iload_3        
        //   553: aload           4
        //   555: invokeinterface java/util/List.size:()I
        //   560: if_icmpge       609
        //   563: iload_3        
        //   564: istore          10
        //   566: aload           5
        //   568: aload           4
        //   570: iload_3        
        //   571: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   576: checkcast       Ljava/lang/String;
        //   579: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   584: ifne            601
        //   587: aload           4
        //   589: iload_3        
        //   590: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
        //   595: pop            
        //   596: iload_3        
        //   597: iconst_1       
        //   598: isub           
        //   599: istore          10
        //   601: iload           10
        //   603: iconst_1       
        //   604: iadd           
        //   605: istore_3       
        //   606: goto            552
        //   609: aload           5
        //   611: invokeinterface java/util/List.clear:()V
        //   616: new             Ljava/util/ArrayList;
        //   619: dup            
        //   620: bipush          10
        //   622: invokespecial   java/util/ArrayList.<init>:(I)V
        //   625: astore          9
        //   627: aload           4
        //   629: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   634: astore          7
        //   636: aload           7
        //   638: invokeinterface java/util/Iterator.hasNext:()Z
        //   643: ifeq            912
        //   646: new             Ljava/io/File;
        //   649: dup            
        //   650: aload           7
        //   652: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   657: checkcast       Ljava/lang/String;
        //   660: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   663: astore_1       
        //   664: aload_1        
        //   665: invokevirtual   java/io/File.exists:()Z
        //   668: ifeq            636
        //   671: aload_1        
        //   672: invokevirtual   java/io/File.isDirectory:()Z
        //   675: ifeq            636
        //   678: aload_1        
        //   679: invokevirtual   java/io/File.canWrite:()Z
        //   682: ifeq            636
        //   685: aload_1        
        //   686: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //   689: astore          6
        //   691: ldc             "["
        //   693: astore_2       
        //   694: aload           6
        //   696: ifnull          790
        //   699: aload           6
        //   701: arraylength    
        //   702: istore          10
        //   704: ldc             "["
        //   706: astore_2       
        //   707: iconst_0       
        //   708: istore_3       
        //   709: iload_3        
        //   710: iload           10
        //   712: if_icmpge       790
        //   715: aload           6
        //   717: iload_3        
        //   718: aaload         
        //   719: astore          5
        //   721: new             Ljava/lang/StringBuilder;
        //   724: dup            
        //   725: invokespecial   java/lang/StringBuilder.<init>:()V
        //   728: astore          8
        //   730: aload           8
        //   732: aload_2        
        //   733: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   736: pop            
        //   737: aload           8
        //   739: aload           5
        //   741: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   744: invokevirtual   java/lang/String.hashCode:()I
        //   747: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   750: pop            
        //   751: aload           8
        //   753: ldc             ":"
        //   755: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   758: pop            
        //   759: aload           8
        //   761: aload           5
        //   763: invokevirtual   java/io/File.length:()J
        //   766: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   769: pop            
        //   770: aload           8
        //   772: ldc             ", "
        //   774: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   777: pop            
        //   778: aload           8
        //   780: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   783: astore_2       
        //   784: iinc            3, 1
        //   787: goto            709
        //   790: new             Ljava/lang/StringBuilder;
        //   793: dup            
        //   794: invokespecial   java/lang/StringBuilder.<init>:()V
        //   797: astore          6
        //   799: aload           6
        //   801: aload_2        
        //   802: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   805: pop            
        //   806: aload           6
        //   808: ldc             "]"
        //   810: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   813: pop            
        //   814: aload           6
        //   816: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   819: astore_2       
        //   820: aload           9
        //   822: aload_2        
        //   823: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   828: ifne            636
        //   831: new             Ljava/lang/StringBuilder;
        //   834: dup            
        //   835: invokespecial   java/lang/StringBuilder.<init>:()V
        //   838: astore          6
        //   840: aload           6
        //   842: ldc             "sdCard_"
        //   844: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   847: pop            
        //   848: aload           6
        //   850: aload_0        
        //   851: invokeinterface java/util/Set.size:()I
        //   856: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   859: pop            
        //   860: aload           6
        //   862: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   865: pop            
        //   866: aload_0        
        //   867: invokeinterface java/util/Set.size:()I
        //   872: ifne            878
        //   875: goto            885
        //   878: aload_0        
        //   879: invokeinterface java/util/Set.size:()I
        //   884: pop            
        //   885: aload           9
        //   887: aload_2        
        //   888: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   893: pop            
        //   894: aload_1        
        //   895: invokestatic    org/osmdroid/tileprovider/util/StorageUtils.isWritable:(Ljava/io/File;)Z
        //   898: ifeq            636
        //   901: aload_0        
        //   902: aload_1        
        //   903: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   908: pop            
        //   909: goto            636
        //   912: aload           4
        //   914: invokeinterface java/util/List.clear:()V
        //   919: aload_0        
        //   920: areturn        
        //   921: aload_2        
        //   922: ifnull          929
        //   925: aload_2        
        //   926: invokevirtual   java/util/Scanner.close:()V
        //   929: aload_1        
        //   930: athrow         
        //   931: astore_2       
        //   932: aload_1        
        //   933: ifnull          940
        //   936: aload_1        
        //   937: invokevirtual   java/util/Scanner.close:()V
        //   940: aload_2        
        //   941: athrow         
        //   942: astore_2       
        //   943: goto            371
        //   946: astore_2       
        //   947: goto            550
        //   950: astore_2       
        //   951: goto            929
        //   954: astore_1       
        //   955: goto            940
        //    Signature:
        //  ()Ljava/util/Set<Ljava/io/File;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  226    252    353    357    Ljava/lang/Exception;
        //  226    252    347    353    Any
        //  257    264    324    329    Ljava/lang/Exception;
        //  257    264    931    932    Any
        //  266    272    324    329    Ljava/lang/Exception;
        //  266    272    931    932    Any
        //  274    284    324    329    Ljava/lang/Exception;
        //  274    284    931    932    Any
        //  286    297    324    329    Ljava/lang/Exception;
        //  286    297    931    932    Any
        //  299    309    324    329    Ljava/lang/Exception;
        //  299    309    931    932    Any
        //  311    321    324    329    Ljava/lang/Exception;
        //  311    321    931    932    Any
        //  340    344    942    946    Ljava/lang/Exception;
        //  359    364    931    932    Any
        //  374    379    531    536    Ljava/lang/Exception;
        //  374    379    521    531    Any
        //  382    389    531    536    Ljava/lang/Exception;
        //  382    389    521    531    Any
        //  395    403    531    536    Ljava/lang/Exception;
        //  395    403    521    531    Any
        //  406    410    531    536    Ljava/lang/Exception;
        //  406    410    521    531    Any
        //  413    419    531    536    Ljava/lang/Exception;
        //  413    419    521    531    Any
        //  419    450    505    510    Ljava/lang/Exception;
        //  419    450    501    505    Any
        //  453    477    505    510    Ljava/lang/Exception;
        //  453    477    501    505    Any
        //  477    495    505    510    Ljava/lang/Exception;
        //  477    495    501    505    Any
        //  514    518    946    950    Ljava/lang/Exception;
        //  538    543    521    531    Any
        //  925    929    950    954    Ljava/lang/Exception;
        //  936    940    954    958    Ljava/lang/Exception;
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
    
    public static File getStorage() {
        final List<StorageInfo> storageList = getStorageList();
        int i = 0;
        StorageInfo storageInfo = null;
        while (i < storageList.size()) {
            final StorageInfo storageInfo2 = storageList.get(i);
            StorageInfo storageInfo3 = storageInfo;
            Label_0081: {
                if (!storageInfo2.readonly) {
                    storageInfo3 = storageInfo;
                    if (isWritable(new File(storageInfo2.path))) {
                        if (storageInfo != null) {
                            storageInfo3 = storageInfo;
                            if (storageInfo.freeSpace >= storageInfo2.freeSpace) {
                                break Label_0081;
                            }
                        }
                        storageInfo3 = storageInfo2;
                    }
                }
            }
            ++i;
            storageInfo = storageInfo3;
        }
        if (storageInfo != null) {
            return new File(storageInfo.path);
        }
        try {
            return Environment.getExternalStorageDirectory();
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static List<StorageInfo> getStorageList() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: astore_0       
        //     3: new             Ljava/util/ArrayList;
        //     6: dup            
        //     7: invokespecial   java/util/ArrayList.<init>:()V
        //    10: astore_1       
        //    11: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //    14: ifnull          32
        //    17: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //    20: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //    23: astore_2       
        //    24: goto            35
        //    27: astore_3       
        //    28: aload_3        
        //    29: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //    32: ldc             ""
        //    34: astore_2       
        //    35: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    38: bipush          9
        //    40: if_icmplt       64
        //    43: invokestatic    android/os/Environment.isExternalStorageRemovable:()Z
        //    46: istore          4
        //    48: iload           4
        //    50: ifne            64
        //    53: iconst_1       
        //    54: istore          4
        //    56: goto            67
        //    59: astore_3       
        //    60: aload_3        
        //    61: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //    64: iconst_0       
        //    65: istore          4
        //    67: invokestatic    android/os/Environment.getExternalStorageState:()Ljava/lang/String;
        //    70: astore_3       
        //    71: aload_3        
        //    72: astore_0       
        //    73: goto            81
        //    76: astore_3       
        //    77: aload_3        
        //    78: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //    81: aload_0        
        //    82: ldc             "mounted"
        //    84: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    87: ifne            103
        //    90: aload_0        
        //    91: ldc             "mounted_ro"
        //    93: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    96: istore          5
        //    98: iload           5
        //   100: ifeq            114
        //   103: iconst_1       
        //   104: istore          6
        //   106: goto            117
        //   109: astore_0       
        //   110: aload_0        
        //   111: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //   114: iconst_0       
        //   115: istore          6
        //   117: invokestatic    android/os/Environment.getExternalStorageState:()Ljava/lang/String;
        //   120: ldc             "mounted_ro"
        //   122: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   125: istore          5
        //   127: goto            138
        //   130: astore_0       
        //   131: aload_0        
        //   132: invokevirtual   java/lang/Throwable.printStackTrace:()V
        //   135: iconst_1       
        //   136: istore          5
        //   138: aconst_null    
        //   139: astore          7
        //   141: aconst_null    
        //   142: astore          8
        //   144: aconst_null    
        //   145: astore          9
        //   147: aload           9
        //   149: astore_0       
        //   150: new             Ljava/util/HashSet;
        //   153: astore          10
        //   155: aload           9
        //   157: astore_0       
        //   158: aload           10
        //   160: invokespecial   java/util/HashSet.<init>:()V
        //   163: aload           9
        //   165: astore_0       
        //   166: new             Ljava/io/BufferedReader;
        //   169: astore_3       
        //   170: aload           9
        //   172: astore_0       
        //   173: new             Ljava/io/FileReader;
        //   176: astore          11
        //   178: aload           9
        //   180: astore_0       
        //   181: aload           11
        //   183: ldc             "/proc/mounts"
        //   185: invokespecial   java/io/FileReader.<init>:(Ljava/lang/String;)V
        //   188: aload           9
        //   190: astore_0       
        //   191: aload_3        
        //   192: aload           11
        //   194: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   197: ldc             "StorageUtils"
        //   199: ldc             "/proc/mounts"
        //   201: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   204: pop            
        //   205: iconst_1       
        //   206: istore          12
        //   208: aload_3        
        //   209: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   212: astore          8
        //   214: aload           8
        //   216: ifnull          461
        //   219: ldc             "StorageUtils"
        //   221: aload           8
        //   223: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   226: pop            
        //   227: aload           8
        //   229: ldc_w           "vfat"
        //   232: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   235: ifne            249
        //   238: aload           8
        //   240: ldc_w           "/mnt"
        //   243: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   246: ifeq            208
        //   249: new             Ljava/util/StringTokenizer;
        //   252: astore          7
        //   254: aload           7
        //   256: aload           8
        //   258: ldc             " "
        //   260: invokespecial   java/util/StringTokenizer.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   263: aload           7
        //   265: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   268: pop            
        //   269: aload           7
        //   271: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   274: astore_0       
        //   275: aload           10
        //   277: aload_0        
        //   278: invokevirtual   java/util/HashSet.contains:(Ljava/lang/Object;)Z
        //   281: ifeq            287
        //   284: goto            208
        //   287: aload           7
        //   289: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   292: pop            
        //   293: aload           7
        //   295: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   298: ldc_w           ","
        //   301: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   304: invokestatic    java/util/Arrays.asList:([Ljava/lang/Object;)Ljava/util/List;
        //   307: ldc_w           "ro"
        //   310: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   315: istore          13
        //   317: aload_0        
        //   318: aload_2        
        //   319: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   322: ifeq            357
        //   325: aload           10
        //   327: aload_2        
        //   328: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
        //   331: pop            
        //   332: new             Lorg/osmdroid/tileprovider/util/StorageUtils$StorageInfo;
        //   335: astore_0       
        //   336: aload_0        
        //   337: aload_2        
        //   338: iload           4
        //   340: iload           13
        //   342: iconst_m1      
        //   343: invokespecial   org/osmdroid/tileprovider/util/StorageUtils$StorageInfo.<init>:(Ljava/lang/String;ZZI)V
        //   346: aload_1        
        //   347: iconst_0       
        //   348: aload_0        
        //   349: invokeinterface java/util/List.add:(ILjava/lang/Object;)V
        //   354: goto            208
        //   357: aload           8
        //   359: ldc_w           "/dev/block/vold"
        //   362: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   365: ifeq            208
        //   368: aload           8
        //   370: ldc_w           "/mnt/secure"
        //   373: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   376: ifne            208
        //   379: aload           8
        //   381: ldc_w           "/mnt/asec"
        //   384: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   387: ifne            208
        //   390: aload           8
        //   392: ldc_w           "/mnt/obb"
        //   395: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   398: ifne            208
        //   401: aload           8
        //   403: ldc_w           "/dev/mapper"
        //   406: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   409: ifne            208
        //   412: aload           8
        //   414: ldc_w           "tmpfs"
        //   417: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   420: ifne            208
        //   423: aload           10
        //   425: aload_0        
        //   426: invokevirtual   java/util/HashSet.add:(Ljava/lang/Object;)Z
        //   429: pop            
        //   430: new             Lorg/osmdroid/tileprovider/util/StorageUtils$StorageInfo;
        //   433: astore          7
        //   435: aload           7
        //   437: aload_0        
        //   438: iconst_0       
        //   439: iload           13
        //   441: iload           12
        //   443: invokespecial   org/osmdroid/tileprovider/util/StorageUtils$StorageInfo.<init>:(Ljava/lang/String;ZZI)V
        //   446: aload_1        
        //   447: aload           7
        //   449: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   454: pop            
        //   455: iinc            12, 1
        //   458: goto            208
        //   461: aload           10
        //   463: aload_2        
        //   464: invokevirtual   java/util/HashSet.contains:(Ljava/lang/Object;)Z
        //   467: ifne            504
        //   470: iload           6
        //   472: ifeq            504
        //   475: aload_2        
        //   476: invokevirtual   java/lang/String.length:()I
        //   479: ifle            504
        //   482: new             Lorg/osmdroid/tileprovider/util/StorageUtils$StorageInfo;
        //   485: astore_0       
        //   486: aload_0        
        //   487: aload_2        
        //   488: iload           4
        //   490: iload           5
        //   492: iconst_m1      
        //   493: invokespecial   org/osmdroid/tileprovider/util/StorageUtils$StorageInfo.<init>:(Ljava/lang/String;ZZI)V
        //   496: aload_1        
        //   497: iconst_0       
        //   498: aload_0        
        //   499: invokeinterface java/util/List.add:(ILjava/lang/Object;)V
        //   504: aload_3        
        //   505: invokevirtual   java/io/BufferedReader.close:()V
        //   508: goto            569
        //   511: astore_0       
        //   512: goto            681
        //   515: astore_2       
        //   516: goto            535
        //   519: astore_2       
        //   520: goto            556
        //   523: astore_2       
        //   524: aload_0        
        //   525: astore_3       
        //   526: aload_2        
        //   527: astore_0       
        //   528: goto            681
        //   531: astore_2       
        //   532: aload           7
        //   534: astore_3       
        //   535: aload_3        
        //   536: astore_0       
        //   537: aload_2        
        //   538: invokevirtual   java/io/IOException.printStackTrace:()V
        //   541: aload_3        
        //   542: ifnull          569
        //   545: aload_3        
        //   546: invokevirtual   java/io/BufferedReader.close:()V
        //   549: goto            569
        //   552: astore_2       
        //   553: aload           8
        //   555: astore_3       
        //   556: aload_3        
        //   557: astore_0       
        //   558: aload_2        
        //   559: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
        //   562: aload_3        
        //   563: ifnull          569
        //   566: goto            545
        //   569: invokestatic    org/osmdroid/tileprovider/util/StorageUtils.getAllStorageLocationsRevised:()Ljava/util/Set;
        //   572: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   577: astore_3       
        //   578: aload_3        
        //   579: invokeinterface java/util/Iterator.hasNext:()Z
        //   584: ifeq            679
        //   587: aload_3        
        //   588: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   593: checkcast       Ljava/io/File;
        //   596: astore_0       
        //   597: iconst_0       
        //   598: istore          6
        //   600: iload           6
        //   602: aload_1        
        //   603: invokeinterface java/util/List.size:()I
        //   608: if_icmpge       647
        //   611: aload_1        
        //   612: iload           6
        //   614: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   619: checkcast       Lorg/osmdroid/tileprovider/util/StorageUtils$StorageInfo;
        //   622: getfield        org/osmdroid/tileprovider/util/StorageUtils$StorageInfo.path:Ljava/lang/String;
        //   625: aload_0        
        //   626: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   629: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   632: ifeq            641
        //   635: iconst_1       
        //   636: istore          6
        //   638: goto            650
        //   641: iinc            6, 1
        //   644: goto            600
        //   647: iconst_0       
        //   648: istore          6
        //   650: iload           6
        //   652: ifne            578
        //   655: aload_1        
        //   656: new             Lorg/osmdroid/tileprovider/util/StorageUtils$StorageInfo;
        //   659: dup            
        //   660: aload_0        
        //   661: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   664: iconst_0       
        //   665: iconst_0       
        //   666: iconst_m1      
        //   667: invokespecial   org/osmdroid/tileprovider/util/StorageUtils$StorageInfo.<init>:(Ljava/lang/String;ZZI)V
        //   670: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   675: pop            
        //   676: goto            578
        //   679: aload_1        
        //   680: areturn        
        //   681: aload_3        
        //   682: ifnull          689
        //   685: aload_3        
        //   686: invokevirtual   java/io/BufferedReader.close:()V
        //   689: aload_0        
        //   690: athrow         
        //   691: astore_0       
        //   692: goto            569
        //   695: astore_3       
        //   696: goto            689
        //    Signature:
        //  ()Ljava/util/List<Lorg/osmdroid/tileprovider/util/StorageUtils$StorageInfo;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  11     24     27     32     Ljava/lang/Throwable;
        //  35     48     59     64     Ljava/lang/Throwable;
        //  67     71     76     81     Ljava/lang/Throwable;
        //  81     98     109    114    Ljava/lang/Throwable;
        //  117    127    130    138    Ljava/lang/Throwable;
        //  150    155    552    556    Ljava/io/FileNotFoundException;
        //  150    155    531    535    Ljava/io/IOException;
        //  150    155    523    531    Any
        //  158    163    552    556    Ljava/io/FileNotFoundException;
        //  158    163    531    535    Ljava/io/IOException;
        //  158    163    523    531    Any
        //  166    170    552    556    Ljava/io/FileNotFoundException;
        //  166    170    531    535    Ljava/io/IOException;
        //  166    170    523    531    Any
        //  173    178    552    556    Ljava/io/FileNotFoundException;
        //  173    178    531    535    Ljava/io/IOException;
        //  173    178    523    531    Any
        //  181    188    552    556    Ljava/io/FileNotFoundException;
        //  181    188    531    535    Ljava/io/IOException;
        //  181    188    523    531    Any
        //  191    197    552    556    Ljava/io/FileNotFoundException;
        //  191    197    531    535    Ljava/io/IOException;
        //  191    197    523    531    Any
        //  197    205    519    523    Ljava/io/FileNotFoundException;
        //  197    205    515    519    Ljava/io/IOException;
        //  197    205    511    515    Any
        //  208    214    519    523    Ljava/io/FileNotFoundException;
        //  208    214    515    519    Ljava/io/IOException;
        //  208    214    511    515    Any
        //  219    249    519    523    Ljava/io/FileNotFoundException;
        //  219    249    515    519    Ljava/io/IOException;
        //  219    249    511    515    Any
        //  249    284    519    523    Ljava/io/FileNotFoundException;
        //  249    284    515    519    Ljava/io/IOException;
        //  249    284    511    515    Any
        //  287    354    519    523    Ljava/io/FileNotFoundException;
        //  287    354    515    519    Ljava/io/IOException;
        //  287    354    511    515    Any
        //  357    455    519    523    Ljava/io/FileNotFoundException;
        //  357    455    515    519    Ljava/io/IOException;
        //  357    455    511    515    Any
        //  461    470    519    523    Ljava/io/FileNotFoundException;
        //  461    470    515    519    Ljava/io/IOException;
        //  461    470    511    515    Any
        //  475    504    519    523    Ljava/io/FileNotFoundException;
        //  475    504    515    519    Ljava/io/IOException;
        //  475    504    511    515    Any
        //  504    508    691    695    Ljava/io/IOException;
        //  537    541    523    531    Any
        //  545    549    691    695    Ljava/io/IOException;
        //  558    562    523    531    Any
        //  685    689    695    699    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0504:
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
    
    public static boolean isWritable(final File file) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(file.getAbsolutePath());
            sb.append(File.separator);
            sb.append("osm.tmp");
            final File file2 = new File(sb.toString());
            final FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write("hi".getBytes());
            fileOutputStream.close();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(file.getAbsolutePath());
            sb2.append(" is writable");
            Log.i("StorageUtils", sb2.toString());
            file2.delete();
            return true;
        }
        catch (Throwable t) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(file.getAbsolutePath());
            sb3.append(" is NOT writable");
            Log.i("StorageUtils", sb3.toString());
            return false;
        }
    }
    
    public static class StorageInfo
    {
        String displayName;
        public final int display_number;
        public long freeSpace;
        public final boolean internal;
        public final String path;
        public boolean readonly;
        
        public StorageInfo(final String str, final boolean internal, final boolean readonly, final int n) {
            this.freeSpace = 0L;
            this.displayName = "";
            this.path = str;
            this.internal = internal;
            this.display_number = n;
            if (Build$VERSION.SDK_INT >= 9) {
                this.freeSpace = new File(str).getFreeSpace();
            }
            if (!readonly) {
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(File.separator);
                sb.append(UUID.randomUUID().toString());
                final File file = new File(sb.toString());
                try {
                    file.createNewFile();
                    file.delete();
                    this.readonly = false;
                }
                catch (Throwable t) {
                    this.readonly = true;
                }
            }
            else {
                this.readonly = readonly;
            }
            final StringBuilder sb2 = new StringBuilder();
            if (internal) {
                sb2.append("Internal SD card");
            }
            else if (n > 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("SD card ");
                sb3.append(n);
                sb2.append(sb3.toString());
            }
            else {
                sb2.append("SD card");
            }
            if (readonly) {
                sb2.append(" (Read only)");
            }
            this.displayName = sb2.toString();
        }
    }
}
