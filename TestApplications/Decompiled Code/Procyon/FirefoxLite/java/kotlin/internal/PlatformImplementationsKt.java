// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.internal;

public final class PlatformImplementationsKt
{
    public static final PlatformImplementations IMPLEMENTATIONS;
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: istore_0       
        //     4: iload_0        
        //     5: ldc             65544
        //     7: if_icmplt       265
        //    10: ldc             "kotlin.internal.jdk8.JDK8PlatformImplementations"
        //    12: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //    15: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //    18: astore_1       
        //    19: aload_1        
        //    20: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //    22: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    25: aload_1        
        //    26: ifnull          37
        //    29: aload_1        
        //    30: checkcast       Lkotlin/internal/PlatformImplementations;
        //    33: astore_2       
        //    34: goto            532
        //    37: new             Lkotlin/TypeCastException;
        //    40: astore_2       
        //    41: aload_2        
        //    42: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //    44: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //    47: aload_2        
        //    48: athrow         
        //    49: astore_2       
        //    50: aload_1        
        //    51: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    54: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //    57: astore_1       
        //    58: ldc             Lkotlin/internal/PlatformImplementations;.class
        //    60: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //    63: astore_3       
        //    64: new             Ljava/lang/ClassCastException;
        //    67: astore          4
        //    69: new             Ljava/lang/StringBuilder;
        //    72: astore          5
        //    74: aload           5
        //    76: invokespecial   java/lang/StringBuilder.<init>:()V
        //    79: aload           5
        //    81: ldc             "Instance classloader: "
        //    83: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    86: pop            
        //    87: aload           5
        //    89: aload_1        
        //    90: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    93: pop            
        //    94: aload           5
        //    96: ldc             ", base type classloader: "
        //    98: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   101: pop            
        //   102: aload           5
        //   104: aload_3        
        //   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   108: pop            
        //   109: aload           4
        //   111: aload           5
        //   113: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   116: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   119: aload           4
        //   121: aload_2        
        //   122: checkcast       Ljava/lang/Throwable;
        //   125: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   128: astore_2       
        //   129: aload_2        
        //   130: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   132: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   135: aload_2        
        //   136: athrow         
        //   137: astore_2       
        //   138: ldc             "kotlin.internal.JRE8PlatformImplementations"
        //   140: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   143: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   146: astore_1       
        //   147: aload_1        
        //   148: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //   150: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   153: aload_1        
        //   154: ifnull          165
        //   157: aload_1        
        //   158: checkcast       Lkotlin/internal/PlatformImplementations;
        //   161: astore_2       
        //   162: goto            532
        //   165: new             Lkotlin/TypeCastException;
        //   168: astore_2       
        //   169: aload_2        
        //   170: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //   172: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   175: aload_2        
        //   176: athrow         
        //   177: astore_2       
        //   178: aload_1        
        //   179: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   182: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   185: astore_1       
        //   186: ldc             Lkotlin/internal/PlatformImplementations;.class
        //   188: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   191: astore_3       
        //   192: new             Ljava/lang/ClassCastException;
        //   195: astore          5
        //   197: new             Ljava/lang/StringBuilder;
        //   200: astore          4
        //   202: aload           4
        //   204: invokespecial   java/lang/StringBuilder.<init>:()V
        //   207: aload           4
        //   209: ldc             "Instance classloader: "
        //   211: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   214: pop            
        //   215: aload           4
        //   217: aload_1        
        //   218: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   221: pop            
        //   222: aload           4
        //   224: ldc             ", base type classloader: "
        //   226: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   229: pop            
        //   230: aload           4
        //   232: aload_3        
        //   233: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   236: pop            
        //   237: aload           5
        //   239: aload           4
        //   241: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   244: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   247: aload           5
        //   249: aload_2        
        //   250: checkcast       Ljava/lang/Throwable;
        //   253: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   256: astore_2       
        //   257: aload_2        
        //   258: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   260: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   263: aload_2        
        //   264: athrow         
        //   265: iload_0        
        //   266: ldc             65543
        //   268: if_icmplt       524
        //   271: ldc             "kotlin.internal.jdk7.JDK7PlatformImplementations"
        //   273: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   276: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   279: astore_1       
        //   280: aload_1        
        //   281: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //   283: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   286: aload_1        
        //   287: ifnull          298
        //   290: aload_1        
        //   291: checkcast       Lkotlin/internal/PlatformImplementations;
        //   294: astore_2       
        //   295: goto            532
        //   298: new             Lkotlin/TypeCastException;
        //   301: astore_2       
        //   302: aload_2        
        //   303: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //   305: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   308: aload_2        
        //   309: athrow         
        //   310: astore_2       
        //   311: aload_1        
        //   312: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   315: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   318: astore_3       
        //   319: ldc             Lkotlin/internal/PlatformImplementations;.class
        //   321: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   324: astore          4
        //   326: new             Ljava/lang/ClassCastException;
        //   329: astore_1       
        //   330: new             Ljava/lang/StringBuilder;
        //   333: astore          5
        //   335: aload           5
        //   337: invokespecial   java/lang/StringBuilder.<init>:()V
        //   340: aload           5
        //   342: ldc             "Instance classloader: "
        //   344: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   347: pop            
        //   348: aload           5
        //   350: aload_3        
        //   351: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   354: pop            
        //   355: aload           5
        //   357: ldc             ", base type classloader: "
        //   359: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   362: pop            
        //   363: aload           5
        //   365: aload           4
        //   367: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   370: pop            
        //   371: aload_1        
        //   372: aload           5
        //   374: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   377: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   380: aload_1        
        //   381: aload_2        
        //   382: checkcast       Ljava/lang/Throwable;
        //   385: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   388: astore_2       
        //   389: aload_2        
        //   390: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   392: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   395: aload_2        
        //   396: athrow         
        //   397: astore_2       
        //   398: ldc             "kotlin.internal.JRE7PlatformImplementations"
        //   400: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   403: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   406: astore_1       
        //   407: aload_1        
        //   408: ldc             "Class.forName(\"kotlin.in\u2026entations\").newInstance()"
        //   410: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   413: aload_1        
        //   414: ifnull          425
        //   417: aload_1        
        //   418: checkcast       Lkotlin/internal/PlatformImplementations;
        //   421: astore_2       
        //   422: goto            532
        //   425: new             Lkotlin/TypeCastException;
        //   428: astore_2       
        //   429: aload_2        
        //   430: ldc             "null cannot be cast to non-null type kotlin.internal.PlatformImplementations"
        //   432: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   435: aload_2        
        //   436: athrow         
        //   437: astore_2       
        //   438: aload_1        
        //   439: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   442: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   445: astore_1       
        //   446: ldc             Lkotlin/internal/PlatformImplementations;.class
        //   448: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   451: astore          5
        //   453: new             Ljava/lang/ClassCastException;
        //   456: astore_3       
        //   457: new             Ljava/lang/StringBuilder;
        //   460: astore          4
        //   462: aload           4
        //   464: invokespecial   java/lang/StringBuilder.<init>:()V
        //   467: aload           4
        //   469: ldc             "Instance classloader: "
        //   471: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   474: pop            
        //   475: aload           4
        //   477: aload_1        
        //   478: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   481: pop            
        //   482: aload           4
        //   484: ldc             ", base type classloader: "
        //   486: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   489: pop            
        //   490: aload           4
        //   492: aload           5
        //   494: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   497: pop            
        //   498: aload_3        
        //   499: aload           4
        //   501: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   504: invokespecial   java/lang/ClassCastException.<init>:(Ljava/lang/String;)V
        //   507: aload_3        
        //   508: aload_2        
        //   509: checkcast       Ljava/lang/Throwable;
        //   512: invokevirtual   java/lang/ClassCastException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   515: astore_2       
        //   516: aload_2        
        //   517: ldc             "ClassCastException(\"Inst\u2026baseTypeCL\").initCause(e)"
        //   519: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   522: aload_2        
        //   523: athrow         
        //   524: new             Lkotlin/internal/PlatformImplementations;
        //   527: dup            
        //   528: invokespecial   kotlin/internal/PlatformImplementations.<init>:()V
        //   531: astore_2       
        //   532: aload_2        
        //   533: putstatic       kotlin/internal/PlatformImplementationsKt.IMPLEMENTATIONS:Lkotlin/internal/PlatformImplementations;
        //   536: return         
        //   537: astore_2       
        //   538: goto            265
        //   541: astore_2       
        //   542: goto            524
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  10     25     137    265    Ljava/lang/ClassNotFoundException;
        //  29     34     49     137    Ljava/lang/ClassCastException;
        //  29     34     137    265    Ljava/lang/ClassNotFoundException;
        //  37     49     49     137    Ljava/lang/ClassCastException;
        //  37     49     137    265    Ljava/lang/ClassNotFoundException;
        //  50     137    137    265    Ljava/lang/ClassNotFoundException;
        //  138    153    537    541    Ljava/lang/ClassNotFoundException;
        //  157    162    177    265    Ljava/lang/ClassCastException;
        //  157    162    537    541    Ljava/lang/ClassNotFoundException;
        //  165    177    177    265    Ljava/lang/ClassCastException;
        //  165    177    537    541    Ljava/lang/ClassNotFoundException;
        //  178    265    537    541    Ljava/lang/ClassNotFoundException;
        //  271    286    397    524    Ljava/lang/ClassNotFoundException;
        //  290    295    310    397    Ljava/lang/ClassCastException;
        //  290    295    397    524    Ljava/lang/ClassNotFoundException;
        //  298    310    310    397    Ljava/lang/ClassCastException;
        //  298    310    397    524    Ljava/lang/ClassNotFoundException;
        //  311    397    397    524    Ljava/lang/ClassNotFoundException;
        //  398    413    541    545    Ljava/lang/ClassNotFoundException;
        //  417    422    437    524    Ljava/lang/ClassCastException;
        //  417    422    541    545    Ljava/lang/ClassNotFoundException;
        //  425    437    437    524    Ljava/lang/ClassCastException;
        //  425    437    541    545    Ljava/lang/ClassNotFoundException;
        //  438    524    541    545    Ljava/lang/ClassNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 277 out-of-bounds for length 277
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
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
    
    private static final int getJavaVersion() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: invokestatic    java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //     5: astore_0       
        //     6: ldc             65542
        //     8: istore_1       
        //     9: aload_0        
        //    10: ifnull          160
        //    13: aload_0        
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: astore_2       
        //    18: aload_2        
        //    19: bipush          46
        //    21: iconst_0       
        //    22: iconst_0       
        //    23: bipush          6
        //    25: aconst_null    
        //    26: invokestatic    kotlin/text/StringsKt.indexOf$default:(Ljava/lang/CharSequence;CIZILjava/lang/Object;)I
        //    29: istore_3       
        //    30: iload_3        
        //    31: ifge            48
        //    34: aload_0        
        //    35: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    38: istore          4
        //    40: iload           4
        //    42: ldc             65536
        //    44: imul           
        //    45: istore_1       
        //    46: iload_1        
        //    47: ireturn        
        //    48: iload_3        
        //    49: iconst_1       
        //    50: iadd           
        //    51: istore          5
        //    53: aload_2        
        //    54: bipush          46
        //    56: iload           5
        //    58: iconst_0       
        //    59: iconst_4       
        //    60: aconst_null    
        //    61: invokestatic    kotlin/text/StringsKt.indexOf$default:(Ljava/lang/CharSequence;CIZILjava/lang/Object;)I
        //    64: istore          6
        //    66: iload           6
        //    68: istore          4
        //    70: iload           6
        //    72: ifge            81
        //    75: aload_0        
        //    76: invokevirtual   java/lang/String.length:()I
        //    79: istore          4
        //    81: aload_0        
        //    82: ifnull          150
        //    85: aload_0        
        //    86: iconst_0       
        //    87: iload_3        
        //    88: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    91: astore_2       
        //    92: aload_2        
        //    93: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //    95: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    98: aload_0        
        //    99: ifnull          140
        //   102: aload_0        
        //   103: iload           5
        //   105: iload           4
        //   107: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   110: astore_0       
        //   111: aload_0        
        //   112: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   114: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   117: aload_2        
        //   118: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   121: istore          4
        //   123: aload_0        
        //   124: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   127: istore          6
        //   129: iload           4
        //   131: ldc             65536
        //   133: imul           
        //   134: iload           6
        //   136: iadd           
        //   137: istore_1       
        //   138: iload_1        
        //   139: ireturn        
        //   140: new             Lkotlin/TypeCastException;
        //   143: dup            
        //   144: ldc             "null cannot be cast to non-null type java.lang.String"
        //   146: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   149: athrow         
        //   150: new             Lkotlin/TypeCastException;
        //   153: dup            
        //   154: ldc             "null cannot be cast to non-null type java.lang.String"
        //   156: invokespecial   kotlin/TypeCastException.<init>:(Ljava/lang/String;)V
        //   159: athrow         
        //   160: ldc             65542
        //   162: ireturn        
        //   163: astore_2       
        //   164: goto            46
        //   167: astore_2       
        //   168: goto            138
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  34     40     163    167    Ljava/lang/NumberFormatException;
        //  117    129    167    171    Ljava/lang/NumberFormatException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0138:
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
