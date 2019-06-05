// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import okhttp3.internal.http.HttpDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import okhttp3.internal.Util;
import java.util.regex.Pattern;

public final class Cookie
{
    private static final Pattern DAY_OF_MONTH_PATTERN;
    private static final Pattern MONTH_PATTERN;
    private static final Pattern TIME_PATTERN;
    private static final Pattern YEAR_PATTERN;
    private final String domain;
    private final long expiresAt;
    private final boolean hostOnly;
    private final boolean httpOnly;
    private final String name;
    private final String path;
    private final boolean persistent;
    private final boolean secure;
    private final String value;
    
    static {
        YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
        MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
        DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
        TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
    }
    
    private Cookie(final String name, final String value, final long expiresAt, final String domain, final String path, final boolean secure, final boolean httpOnly, final boolean hostOnly, final boolean persistent) {
        this.name = name;
        this.value = value;
        this.expiresAt = expiresAt;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.hostOnly = hostOnly;
        this.persistent = persistent;
    }
    
    Cookie(final Builder builder) {
        if (builder.name == null) {
            throw new NullPointerException("builder.name == null");
        }
        if (builder.value == null) {
            throw new NullPointerException("builder.value == null");
        }
        if (builder.domain == null) {
            throw new NullPointerException("builder.domain == null");
        }
        this.name = builder.name;
        this.value = builder.value;
        this.expiresAt = builder.expiresAt;
        this.domain = builder.domain;
        this.path = builder.path;
        this.secure = builder.secure;
        this.httpOnly = builder.httpOnly;
        this.persistent = builder.persistent;
        this.hostOnly = builder.hostOnly;
    }
    
    private static int dateCharacterOffset(final String s, int i, final int n, final boolean b) {
        while (i < n) {
            final char char1 = s.charAt(i);
            int n2;
            if ((char1 < ' ' && char1 != '\t') || char1 >= '\u007f' || (char1 >= '0' && char1 <= '9') || (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || char1 == ':') {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            int n3;
            if (!b) {
                n3 = 1;
            }
            else {
                n3 = 0;
            }
            if (n2 == n3) {
                return i;
            }
            ++i;
        }
        i = n;
        return i;
    }
    
    private static boolean domainMatch(final HttpUrl httpUrl, final String s) {
        boolean b = true;
        final String host = httpUrl.host();
        if (!host.equals(s) && (!host.endsWith(s) || host.charAt(host.length() - s.length() - 1) != '.' || Util.verifyAsIpAddress(host))) {
            b = false;
        }
        return b;
    }
    
    static Cookie parse(final long p0, final HttpUrl p1, final String p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/String.length:()I
        //     4: istore          4
        //     6: aload_3        
        //     7: iconst_0       
        //     8: iload           4
        //    10: bipush          59
        //    12: invokestatic    okhttp3/internal/Util.delimiterOffset:(Ljava/lang/String;IIC)I
        //    15: istore          5
        //    17: aload_3        
        //    18: iconst_0       
        //    19: iload           5
        //    21: bipush          61
        //    23: invokestatic    okhttp3/internal/Util.delimiterOffset:(Ljava/lang/String;IIC)I
        //    26: istore          6
        //    28: iload           6
        //    30: iload           5
        //    32: if_icmpne       39
        //    35: aconst_null    
        //    36: astore_2       
        //    37: aload_2        
        //    38: areturn        
        //    39: aload_3        
        //    40: iconst_0       
        //    41: iload           6
        //    43: invokestatic    okhttp3/internal/Util.trimSubstring:(Ljava/lang/String;II)Ljava/lang/String;
        //    46: astore          7
        //    48: aload           7
        //    50: invokevirtual   java/lang/String.isEmpty:()Z
        //    53: ifne            65
        //    56: aload           7
        //    58: invokestatic    okhttp3/internal/Util.indexOfControlOrNonAscii:(Ljava/lang/String;)I
        //    61: iconst_m1      
        //    62: if_icmpeq       70
        //    65: aconst_null    
        //    66: astore_2       
        //    67: goto            37
        //    70: aload_3        
        //    71: iload           6
        //    73: iconst_1       
        //    74: iadd           
        //    75: iload           5
        //    77: invokestatic    okhttp3/internal/Util.trimSubstring:(Ljava/lang/String;II)Ljava/lang/String;
        //    80: astore          8
        //    82: aload           8
        //    84: invokestatic    okhttp3/internal/Util.indexOfControlOrNonAscii:(Ljava/lang/String;)I
        //    87: iconst_m1      
        //    88: if_icmpeq       96
        //    91: aconst_null    
        //    92: astore_2       
        //    93: goto            37
        //    96: ldc2_w          253402300799999
        //    99: lstore          9
        //   101: ldc2_w          -1
        //   104: lstore          11
        //   106: aconst_null    
        //   107: astore          13
        //   109: aconst_null    
        //   110: astore          14
        //   112: iconst_0       
        //   113: istore          15
        //   115: iconst_0       
        //   116: istore          16
        //   118: iconst_1       
        //   119: istore          17
        //   121: iconst_0       
        //   122: istore          18
        //   124: iload           5
        //   126: iconst_1       
        //   127: iadd           
        //   128: istore          6
        //   130: iload           6
        //   132: iload           4
        //   134: if_icmpge       519
        //   137: aload_3        
        //   138: iload           6
        //   140: iload           4
        //   142: bipush          59
        //   144: invokestatic    okhttp3/internal/Util.delimiterOffset:(Ljava/lang/String;IIC)I
        //   147: istore          19
        //   149: aload_3        
        //   150: iload           6
        //   152: iload           19
        //   154: bipush          61
        //   156: invokestatic    okhttp3/internal/Util.delimiterOffset:(Ljava/lang/String;IIC)I
        //   159: istore          5
        //   161: aload_3        
        //   162: iload           6
        //   164: iload           5
        //   166: invokestatic    okhttp3/internal/Util.trimSubstring:(Ljava/lang/String;II)Ljava/lang/String;
        //   169: astore          20
        //   171: iload           5
        //   173: iload           19
        //   175: if_icmpge       273
        //   178: aload_3        
        //   179: iload           5
        //   181: iconst_1       
        //   182: iadd           
        //   183: iload           19
        //   185: invokestatic    okhttp3/internal/Util.trimSubstring:(Ljava/lang/String;II)Ljava/lang/String;
        //   188: astore          21
        //   190: aload           20
        //   192: ldc             "expires"
        //   194: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   197: ifeq            280
        //   200: aload           21
        //   202: iconst_0       
        //   203: aload           21
        //   205: invokevirtual   java/lang/String.length:()I
        //   208: invokestatic    okhttp3/Cookie.parseExpires:(Ljava/lang/String;II)J
        //   211: lstore          22
        //   213: iconst_1       
        //   214: istore          24
        //   216: lload           11
        //   218: lstore          25
        //   220: iload           17
        //   222: istore          27
        //   224: iload           15
        //   226: istore          28
        //   228: aload           14
        //   230: astore          29
        //   232: aload           13
        //   234: astore          21
        //   236: iload           19
        //   238: iconst_1       
        //   239: iadd           
        //   240: istore          6
        //   242: lload           22
        //   244: lstore          9
        //   246: aload           21
        //   248: astore          13
        //   250: aload           29
        //   252: astore          14
        //   254: iload           28
        //   256: istore          15
        //   258: iload           27
        //   260: istore          17
        //   262: iload           24
        //   264: istore          18
        //   266: lload           25
        //   268: lstore          11
        //   270: goto            130
        //   273: ldc             ""
        //   275: astore          21
        //   277: goto            190
        //   280: aload           20
        //   282: ldc             "max-age"
        //   284: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   287: ifeq            323
        //   290: aload           21
        //   292: invokestatic    okhttp3/Cookie.parseMaxAge:(Ljava/lang/String;)J
        //   295: lstore          25
        //   297: iconst_1       
        //   298: istore          24
        //   300: lload           9
        //   302: lstore          22
        //   304: aload           13
        //   306: astore          21
        //   308: aload           14
        //   310: astore          29
        //   312: iload           15
        //   314: istore          28
        //   316: iload           17
        //   318: istore          27
        //   320: goto            236
        //   323: aload           20
        //   325: ldc             "domain"
        //   327: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   330: ifeq            366
        //   333: aload           21
        //   335: invokestatic    okhttp3/Cookie.parseDomain:(Ljava/lang/String;)Ljava/lang/String;
        //   338: astore          21
        //   340: iconst_0       
        //   341: istore          27
        //   343: lload           9
        //   345: lstore          22
        //   347: aload           14
        //   349: astore          29
        //   351: iload           15
        //   353: istore          28
        //   355: iload           18
        //   357: istore          24
        //   359: lload           11
        //   361: lstore          25
        //   363: goto            236
        //   366: aload           20
        //   368: ldc             "path"
        //   370: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   373: ifeq            407
        //   376: aload           21
        //   378: astore          29
        //   380: lload           9
        //   382: lstore          22
        //   384: aload           13
        //   386: astore          21
        //   388: iload           15
        //   390: istore          28
        //   392: iload           17
        //   394: istore          27
        //   396: iload           18
        //   398: istore          24
        //   400: lload           11
        //   402: lstore          25
        //   404: goto            236
        //   407: aload           20
        //   409: ldc             "secure"
        //   411: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   414: ifeq            447
        //   417: iconst_1       
        //   418: istore          28
        //   420: lload           9
        //   422: lstore          22
        //   424: aload           13
        //   426: astore          21
        //   428: aload           14
        //   430: astore          29
        //   432: iload           17
        //   434: istore          27
        //   436: iload           18
        //   438: istore          24
        //   440: lload           11
        //   442: lstore          25
        //   444: goto            236
        //   447: lload           9
        //   449: lstore          22
        //   451: aload           13
        //   453: astore          21
        //   455: aload           14
        //   457: astore          29
        //   459: iload           15
        //   461: istore          28
        //   463: iload           17
        //   465: istore          27
        //   467: iload           18
        //   469: istore          24
        //   471: lload           11
        //   473: lstore          25
        //   475: aload           20
        //   477: ldc             "httponly"
        //   479: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   482: ifeq            236
        //   485: iconst_1       
        //   486: istore          16
        //   488: lload           9
        //   490: lstore          22
        //   492: aload           13
        //   494: astore          21
        //   496: aload           14
        //   498: astore          29
        //   500: iload           15
        //   502: istore          28
        //   504: iload           17
        //   506: istore          27
        //   508: iload           18
        //   510: istore          24
        //   512: lload           11
        //   514: lstore          25
        //   516: goto            236
        //   519: lload           11
        //   521: ldc2_w          -9223372036854775808
        //   524: lcmp           
        //   525: ifne            616
        //   528: ldc2_w          -9223372036854775808
        //   531: lstore          9
        //   533: aload           13
        //   535: ifnonnull       684
        //   538: aload_2        
        //   539: invokevirtual   okhttp3/HttpUrl.host:()Ljava/lang/String;
        //   542: astore          21
        //   544: aload           14
        //   546: ifnull          562
        //   549: aload           14
        //   551: astore_3       
        //   552: aload           14
        //   554: ldc             "/"
        //   556: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   559: ifne            588
        //   562: aload_2        
        //   563: invokevirtual   okhttp3/HttpUrl.encodedPath:()Ljava/lang/String;
        //   566: astore_2       
        //   567: aload_2        
        //   568: bipush          47
        //   570: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   573: istore          6
        //   575: iload           6
        //   577: ifeq            702
        //   580: aload_2        
        //   581: iconst_0       
        //   582: iload           6
        //   584: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   587: astore_3       
        //   588: new             Lokhttp3/Cookie;
        //   591: dup            
        //   592: aload           7
        //   594: aload           8
        //   596: lload           9
        //   598: aload           21
        //   600: aload_3        
        //   601: iload           15
        //   603: iload           16
        //   605: iload           17
        //   607: iload           18
        //   609: invokespecial   okhttp3/Cookie.<init>:(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V
        //   612: astore_2       
        //   613: goto            37
        //   616: lload           11
        //   618: ldc2_w          -1
        //   621: lcmp           
        //   622: ifeq            533
        //   625: lload           11
        //   627: ldc2_w          9223372036854775
        //   630: lcmp           
        //   631: ifgt            676
        //   634: lload           11
        //   636: ldc2_w          1000
        //   639: lmul           
        //   640: lstore          9
        //   642: lload_0        
        //   643: lload           9
        //   645: ladd           
        //   646: lstore          11
        //   648: lload           11
        //   650: lload_0        
        //   651: lcmp           
        //   652: iflt            668
        //   655: lload           11
        //   657: lstore          9
        //   659: lload           11
        //   661: ldc2_w          253402300799999
        //   664: lcmp           
        //   665: ifle            533
        //   668: ldc2_w          253402300799999
        //   671: lstore          9
        //   673: goto            533
        //   676: ldc2_w          9223372036854775807
        //   679: lstore          9
        //   681: goto            642
        //   684: aload           13
        //   686: astore          21
        //   688: aload_2        
        //   689: aload           13
        //   691: invokestatic    okhttp3/Cookie.domainMatch:(Lokhttp3/HttpUrl;Ljava/lang/String;)Z
        //   694: ifne            544
        //   697: aconst_null    
        //   698: astore_2       
        //   699: goto            37
        //   702: ldc             "/"
        //   704: astore_3       
        //   705: goto            588
        //   708: astore          21
        //   710: lload           9
        //   712: lstore          22
        //   714: aload           13
        //   716: astore          21
        //   718: aload           14
        //   720: astore          29
        //   722: iload           15
        //   724: istore          28
        //   726: iload           17
        //   728: istore          27
        //   730: iload           18
        //   732: istore          24
        //   734: lload           11
        //   736: lstore          25
        //   738: goto            236
        //   741: astore          21
        //   743: lload           9
        //   745: lstore          22
        //   747: aload           13
        //   749: astore          21
        //   751: aload           14
        //   753: astore          29
        //   755: iload           15
        //   757: istore          28
        //   759: iload           17
        //   761: istore          27
        //   763: iload           18
        //   765: istore          24
        //   767: lload           11
        //   769: lstore          25
        //   771: goto            236
        //   774: astore          21
        //   776: lload           9
        //   778: lstore          22
        //   780: aload           13
        //   782: astore          21
        //   784: aload           14
        //   786: astore          29
        //   788: iload           15
        //   790: istore          28
        //   792: iload           17
        //   794: istore          27
        //   796: iload           18
        //   798: istore          24
        //   800: lload           11
        //   802: lstore          25
        //   804: goto            236
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  200    213    774    807    Ljava/lang/IllegalArgumentException;
        //  290    297    708    741    Ljava/lang/NumberFormatException;
        //  333    340    741    774    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0366:
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
    
    public static Cookie parse(final HttpUrl httpUrl, final String s) {
        return parse(System.currentTimeMillis(), httpUrl, s);
    }
    
    public static List<Cookie> parseAll(final HttpUrl httpUrl, final Headers headers) {
        final List<String> values = headers.values("Set-Cookie");
        List<Cookie> list = null;
        List<Cookie> list2;
        for (int i = 0; i < values.size(); ++i, list = list2) {
            final Cookie parse = parse(httpUrl, values.get(i));
            if (parse == null) {
                list2 = list;
            }
            else {
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Cookie>();
                }
                list2.add(parse);
            }
        }
        Object o;
        if (list != null) {
            o = Collections.unmodifiableList((List<?>)list);
        }
        else {
            o = Collections.emptyList();
        }
        return (List<Cookie>)o;
    }
    
    private static String parseDomain(String domainToAscii) {
        if (domainToAscii.endsWith(".")) {
            throw new IllegalArgumentException();
        }
        String substring = domainToAscii;
        if (domainToAscii.startsWith(".")) {
            substring = domainToAscii.substring(1);
        }
        domainToAscii = Util.domainToAscii(substring);
        if (domainToAscii == null) {
            throw new IllegalArgumentException();
        }
        return domainToAscii;
    }
    
    private static long parseExpires(final String input, int value, int n) {
        int i = dateCharacterOffset(input, value, n, false);
        int value2 = -1;
        int value3 = -1;
        int value4 = -1;
        int value5 = -1;
        int n2 = -1;
        value = -1;
        final Matcher matcher = Cookie.TIME_PATTERN.matcher(input);
        while (i < n) {
            final int dateCharacterOffset = dateCharacterOffset(input, i + 1, n, true);
            matcher.region(i, dateCharacterOffset);
            int int1;
            int int2;
            int int3;
            int int4;
            int n3;
            int int5;
            if (value2 == -1 && matcher.usePattern(Cookie.TIME_PATTERN).matches()) {
                int1 = Integer.parseInt(matcher.group(1));
                int2 = Integer.parseInt(matcher.group(2));
                int3 = Integer.parseInt(matcher.group(3));
                int4 = value;
                n3 = n2;
                int5 = value5;
            }
            else if (value5 == -1 && matcher.usePattern(Cookie.DAY_OF_MONTH_PATTERN).matches()) {
                int5 = Integer.parseInt(matcher.group(1));
                int1 = value2;
                int2 = value3;
                n3 = n2;
                int3 = value4;
                int4 = value;
            }
            else if (n2 == -1 && matcher.usePattern(Cookie.MONTH_PATTERN).matches()) {
                n3 = Cookie.MONTH_PATTERN.pattern().indexOf(matcher.group(1).toLowerCase(Locale.US)) / 4;
                int5 = value5;
                int1 = value2;
                int2 = value3;
                int3 = value4;
                int4 = value;
            }
            else {
                int5 = value5;
                int1 = value2;
                int2 = value3;
                n3 = n2;
                int3 = value4;
                if ((int4 = value) == -1) {
                    int5 = value5;
                    int1 = value2;
                    int2 = value3;
                    n3 = n2;
                    int3 = value4;
                    int4 = value;
                    if (matcher.usePattern(Cookie.YEAR_PATTERN).matches()) {
                        int4 = Integer.parseInt(matcher.group(1));
                        int5 = value5;
                        int1 = value2;
                        int2 = value3;
                        n3 = n2;
                        int3 = value4;
                    }
                }
            }
            final int dateCharacterOffset2 = dateCharacterOffset(input, dateCharacterOffset + 1, n, false);
            value5 = int5;
            value2 = int1;
            value3 = int2;
            n2 = n3;
            value4 = int3;
            value = int4;
            i = dateCharacterOffset2;
        }
        n = value;
        if (value >= 70 && (n = value) <= 99) {
            n = value + 1900;
        }
        if ((value = n) >= 0 && (value = n) <= 69) {
            value = n + 2000;
        }
        if (value < 1601) {
            throw new IllegalArgumentException();
        }
        if (n2 == -1) {
            throw new IllegalArgumentException();
        }
        if (value5 < 1 || value5 > 31) {
            throw new IllegalArgumentException();
        }
        if (value2 < 0 || value2 > 23) {
            throw new IllegalArgumentException();
        }
        if (value3 < 0 || value3 > 59) {
            throw new IllegalArgumentException();
        }
        if (value4 < 0 || value4 > 59) {
            throw new IllegalArgumentException();
        }
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
        gregorianCalendar.setLenient(false);
        gregorianCalendar.set(1, value);
        gregorianCalendar.set(2, n2 - 1);
        gregorianCalendar.set(5, value5);
        gregorianCalendar.set(11, value2);
        gregorianCalendar.set(12, value3);
        gregorianCalendar.set(13, value4);
        gregorianCalendar.set(14, 0);
        return gregorianCalendar.getTimeInMillis();
    }
    
    private static long parseMaxAge(final String s) {
        long long1 = Long.MIN_VALUE;
        try {
            if ((long1 = Long.parseLong(s)) <= 0L) {
                long1 = Long.MIN_VALUE;
            }
            return long1;
        }
        catch (NumberFormatException ex) {
            if (s.matches("-?\\d+")) {
                if (!s.startsWith("-")) {
                    long1 = Long.MAX_VALUE;
                }
                return long1;
            }
            throw ex;
        }
    }
    
    private static boolean pathMatch(final HttpUrl httpUrl, final String s) {
        final boolean b = true;
        final String encodedPath = httpUrl.encodedPath();
        boolean b2;
        if (encodedPath.equals(s)) {
            b2 = b;
        }
        else {
            if (encodedPath.startsWith(s)) {
                b2 = b;
                if (s.endsWith("/")) {
                    return b2;
                }
                b2 = b;
                if (encodedPath.charAt(s.length()) == '/') {
                    return b2;
                }
            }
            b2 = false;
        }
        return b2;
    }
    
    public String domain() {
        return this.domain;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (!(o instanceof Cookie)) {
            b2 = b;
        }
        else {
            final Cookie cookie = (Cookie)o;
            b2 = b;
            if (cookie.name.equals(this.name)) {
                b2 = b;
                if (cookie.value.equals(this.value)) {
                    b2 = b;
                    if (cookie.domain.equals(this.domain)) {
                        b2 = b;
                        if (cookie.path.equals(this.path)) {
                            b2 = b;
                            if (cookie.expiresAt == this.expiresAt) {
                                b2 = b;
                                if (cookie.secure == this.secure) {
                                    b2 = b;
                                    if (cookie.httpOnly == this.httpOnly) {
                                        b2 = b;
                                        if (cookie.persistent == this.persistent) {
                                            b2 = b;
                                            if (cookie.hostOnly == this.hostOnly) {
                                                b2 = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    public long expiresAt() {
        return this.expiresAt;
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        final int hashCode = this.name.hashCode();
        final int hashCode2 = this.value.hashCode();
        final int hashCode3 = this.domain.hashCode();
        final int hashCode4 = this.path.hashCode();
        final int n2 = (int)(this.expiresAt ^ this.expiresAt >>> 32);
        int n3;
        if (this.secure) {
            n3 = 0;
        }
        else {
            n3 = 1;
        }
        int n4;
        if (this.httpOnly) {
            n4 = 0;
        }
        else {
            n4 = 1;
        }
        int n5;
        if (this.persistent) {
            n5 = 0;
        }
        else {
            n5 = 1;
        }
        if (!this.hostOnly) {
            n = 1;
        }
        return ((((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + n2) * 31 + n3) * 31 + n4) * 31 + n5) * 31 + n;
    }
    
    public boolean hostOnly() {
        return this.hostOnly;
    }
    
    public boolean httpOnly() {
        return this.httpOnly;
    }
    
    public boolean matches(final HttpUrl httpUrl) {
        final boolean b = false;
        boolean b2;
        if (this.hostOnly) {
            b2 = httpUrl.host().equals(this.domain);
        }
        else {
            b2 = domainMatch(httpUrl, this.domain);
        }
        boolean b3;
        if (!b2) {
            b3 = b;
        }
        else {
            b3 = b;
            if (pathMatch(httpUrl, this.path)) {
                if (this.secure) {
                    b3 = b;
                    if (!httpUrl.isHttps()) {
                        return b3;
                    }
                }
                b3 = true;
            }
        }
        return b3;
    }
    
    public String name() {
        return this.name;
    }
    
    public String path() {
        return this.path;
    }
    
    public boolean persistent() {
        return this.persistent;
    }
    
    public boolean secure() {
        return this.secure;
    }
    
    @Override
    public String toString() {
        return this.toString(false);
    }
    
    String toString(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append('=');
        sb.append(this.value);
        if (this.persistent) {
            if (this.expiresAt == Long.MIN_VALUE) {
                sb.append("; max-age=0");
            }
            else {
                sb.append("; expires=").append(HttpDate.format(new Date(this.expiresAt)));
            }
        }
        if (!this.hostOnly) {
            sb.append("; domain=");
            if (b) {
                sb.append(".");
            }
            sb.append(this.domain);
        }
        sb.append("; path=").append(this.path);
        if (this.secure) {
            sb.append("; secure");
        }
        if (this.httpOnly) {
            sb.append("; httponly");
        }
        return sb.toString();
    }
    
    public String value() {
        return this.value;
    }
    
    public static final class Builder
    {
        String domain;
        long expiresAt;
        boolean hostOnly;
        boolean httpOnly;
        String name;
        String path;
        boolean persistent;
        boolean secure;
        String value;
        
        public Builder() {
            this.expiresAt = 253402300799999L;
            this.path = "/";
        }
        
        private Builder domain(final String str, final boolean hostOnly) {
            if (str == null) {
                throw new NullPointerException("domain == null");
            }
            final String domainToAscii = Util.domainToAscii(str);
            if (domainToAscii == null) {
                throw new IllegalArgumentException("unexpected domain: " + str);
            }
            this.domain = domainToAscii;
            this.hostOnly = hostOnly;
            return this;
        }
        
        public Cookie build() {
            return new Cookie(this);
        }
        
        public Builder domain(final String s) {
            return this.domain(s, false);
        }
        
        public Builder expiresAt(long expiresAt) {
            long n = expiresAt;
            if (expiresAt <= 0L) {
                n = Long.MIN_VALUE;
            }
            expiresAt = n;
            if (n > 253402300799999L) {
                expiresAt = 253402300799999L;
            }
            this.expiresAt = expiresAt;
            this.persistent = true;
            return this;
        }
        
        public Builder hostOnlyDomain(final String s) {
            return this.domain(s, true);
        }
        
        public Builder httpOnly() {
            this.httpOnly = true;
            return this;
        }
        
        public Builder name(final String s) {
            if (s == null) {
                throw new NullPointerException("name == null");
            }
            if (!s.trim().equals(s)) {
                throw new IllegalArgumentException("name is not trimmed");
            }
            this.name = s;
            return this;
        }
        
        public Builder path(final String path) {
            if (!path.startsWith("/")) {
                throw new IllegalArgumentException("path must start with '/'");
            }
            this.path = path;
            return this;
        }
        
        public Builder secure() {
            this.secure = true;
            return this;
        }
        
        public Builder value(final String s) {
            if (s == null) {
                throw new NullPointerException("value == null");
            }
            if (!s.trim().equals(s)) {
                throw new IllegalArgumentException("value is not trimmed");
            }
            this.value = s;
            return this;
        }
    }
}
