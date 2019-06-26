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
        //   124: iinc            5, 1
        //   127: iload           5
        //   129: iload           4
        //   131: if_icmpge       516
        //   134: aload_3        
        //   135: iload           5
        //   137: iload           4
        //   139: bipush          59
        //   141: invokestatic    okhttp3/internal/Util.delimiterOffset:(Ljava/lang/String;IIC)I
        //   144: istore          6
        //   146: aload_3        
        //   147: iload           5
        //   149: iload           6
        //   151: bipush          61
        //   153: invokestatic    okhttp3/internal/Util.delimiterOffset:(Ljava/lang/String;IIC)I
        //   156: istore          19
        //   158: aload_3        
        //   159: iload           5
        //   161: iload           19
        //   163: invokestatic    okhttp3/internal/Util.trimSubstring:(Ljava/lang/String;II)Ljava/lang/String;
        //   166: astore          20
        //   168: iload           19
        //   170: iload           6
        //   172: if_icmpge       270
        //   175: aload_3        
        //   176: iload           19
        //   178: iconst_1       
        //   179: iadd           
        //   180: iload           6
        //   182: invokestatic    okhttp3/internal/Util.trimSubstring:(Ljava/lang/String;II)Ljava/lang/String;
        //   185: astore          21
        //   187: aload           20
        //   189: ldc             "expires"
        //   191: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   194: ifeq            277
        //   197: aload           21
        //   199: iconst_0       
        //   200: aload           21
        //   202: invokevirtual   java/lang/String.length:()I
        //   205: invokestatic    okhttp3/Cookie.parseExpires:(Ljava/lang/String;II)J
        //   208: lstore          22
        //   210: iconst_1       
        //   211: istore          24
        //   213: lload           11
        //   215: lstore          25
        //   217: iload           17
        //   219: istore          27
        //   221: iload           15
        //   223: istore          28
        //   225: aload           14
        //   227: astore          29
        //   229: aload           13
        //   231: astore          21
        //   233: iload           6
        //   235: iconst_1       
        //   236: iadd           
        //   237: istore          5
        //   239: lload           22
        //   241: lstore          9
        //   243: aload           21
        //   245: astore          13
        //   247: aload           29
        //   249: astore          14
        //   251: iload           28
        //   253: istore          15
        //   255: iload           27
        //   257: istore          17
        //   259: iload           24
        //   261: istore          18
        //   263: lload           25
        //   265: lstore          11
        //   267: goto            127
        //   270: ldc             ""
        //   272: astore          21
        //   274: goto            187
        //   277: aload           20
        //   279: ldc             "max-age"
        //   281: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   284: ifeq            320
        //   287: aload           21
        //   289: invokestatic    okhttp3/Cookie.parseMaxAge:(Ljava/lang/String;)J
        //   292: lstore          25
        //   294: iconst_1       
        //   295: istore          24
        //   297: lload           9
        //   299: lstore          22
        //   301: aload           13
        //   303: astore          21
        //   305: aload           14
        //   307: astore          29
        //   309: iload           15
        //   311: istore          28
        //   313: iload           17
        //   315: istore          27
        //   317: goto            233
        //   320: aload           20
        //   322: ldc             "domain"
        //   324: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   327: ifeq            363
        //   330: aload           21
        //   332: invokestatic    okhttp3/Cookie.parseDomain:(Ljava/lang/String;)Ljava/lang/String;
        //   335: astore          21
        //   337: iconst_0       
        //   338: istore          27
        //   340: lload           9
        //   342: lstore          22
        //   344: aload           14
        //   346: astore          29
        //   348: iload           15
        //   350: istore          28
        //   352: iload           18
        //   354: istore          24
        //   356: lload           11
        //   358: lstore          25
        //   360: goto            233
        //   363: aload           20
        //   365: ldc             "path"
        //   367: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   370: ifeq            404
        //   373: aload           21
        //   375: astore          29
        //   377: lload           9
        //   379: lstore          22
        //   381: aload           13
        //   383: astore          21
        //   385: iload           15
        //   387: istore          28
        //   389: iload           17
        //   391: istore          27
        //   393: iload           18
        //   395: istore          24
        //   397: lload           11
        //   399: lstore          25
        //   401: goto            233
        //   404: aload           20
        //   406: ldc             "secure"
        //   408: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   411: ifeq            444
        //   414: iconst_1       
        //   415: istore          28
        //   417: lload           9
        //   419: lstore          22
        //   421: aload           13
        //   423: astore          21
        //   425: aload           14
        //   427: astore          29
        //   429: iload           17
        //   431: istore          27
        //   433: iload           18
        //   435: istore          24
        //   437: lload           11
        //   439: lstore          25
        //   441: goto            233
        //   444: lload           9
        //   446: lstore          22
        //   448: aload           13
        //   450: astore          21
        //   452: aload           14
        //   454: astore          29
        //   456: iload           15
        //   458: istore          28
        //   460: iload           17
        //   462: istore          27
        //   464: iload           18
        //   466: istore          24
        //   468: lload           11
        //   470: lstore          25
        //   472: aload           20
        //   474: ldc             "httponly"
        //   476: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   479: ifeq            233
        //   482: iconst_1       
        //   483: istore          16
        //   485: lload           9
        //   487: lstore          22
        //   489: aload           13
        //   491: astore          21
        //   493: aload           14
        //   495: astore          29
        //   497: iload           15
        //   499: istore          28
        //   501: iload           17
        //   503: istore          27
        //   505: iload           18
        //   507: istore          24
        //   509: lload           11
        //   511: lstore          25
        //   513: goto            233
        //   516: lload           11
        //   518: ldc2_w          -9223372036854775808
        //   521: lcmp           
        //   522: ifne            613
        //   525: ldc2_w          -9223372036854775808
        //   528: lstore          9
        //   530: aload           13
        //   532: ifnonnull       681
        //   535: aload_2        
        //   536: invokevirtual   okhttp3/HttpUrl.host:()Ljava/lang/String;
        //   539: astore          21
        //   541: aload           14
        //   543: ifnull          559
        //   546: aload           14
        //   548: astore_3       
        //   549: aload           14
        //   551: ldc             "/"
        //   553: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   556: ifne            585
        //   559: aload_2        
        //   560: invokevirtual   okhttp3/HttpUrl.encodedPath:()Ljava/lang/String;
        //   563: astore_2       
        //   564: aload_2        
        //   565: bipush          47
        //   567: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   570: istore          5
        //   572: iload           5
        //   574: ifeq            699
        //   577: aload_2        
        //   578: iconst_0       
        //   579: iload           5
        //   581: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   584: astore_3       
        //   585: new             Lokhttp3/Cookie;
        //   588: dup            
        //   589: aload           7
        //   591: aload           8
        //   593: lload           9
        //   595: aload           21
        //   597: aload_3        
        //   598: iload           15
        //   600: iload           16
        //   602: iload           17
        //   604: iload           18
        //   606: invokespecial   okhttp3/Cookie.<init>:(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V
        //   609: astore_2       
        //   610: goto            37
        //   613: lload           11
        //   615: ldc2_w          -1
        //   618: lcmp           
        //   619: ifeq            530
        //   622: lload           11
        //   624: ldc2_w          9223372036854775
        //   627: lcmp           
        //   628: ifgt            673
        //   631: lload           11
        //   633: ldc2_w          1000
        //   636: lmul           
        //   637: lstore          9
        //   639: lload_0        
        //   640: lload           9
        //   642: ladd           
        //   643: lstore          11
        //   645: lload           11
        //   647: lload_0        
        //   648: lcmp           
        //   649: iflt            665
        //   652: lload           11
        //   654: lstore          9
        //   656: lload           11
        //   658: ldc2_w          253402300799999
        //   661: lcmp           
        //   662: ifle            530
        //   665: ldc2_w          253402300799999
        //   668: lstore          9
        //   670: goto            530
        //   673: ldc2_w          9223372036854775807
        //   676: lstore          9
        //   678: goto            639
        //   681: aload           13
        //   683: astore          21
        //   685: aload_2        
        //   686: aload           13
        //   688: invokestatic    okhttp3/Cookie.domainMatch:(Lokhttp3/HttpUrl;Ljava/lang/String;)Z
        //   691: ifne            541
        //   694: aconst_null    
        //   695: astore_2       
        //   696: goto            37
        //   699: ldc             "/"
        //   701: astore_3       
        //   702: goto            585
        //   705: astore          21
        //   707: lload           9
        //   709: lstore          22
        //   711: aload           13
        //   713: astore          21
        //   715: aload           14
        //   717: astore          29
        //   719: iload           15
        //   721: istore          28
        //   723: iload           17
        //   725: istore          27
        //   727: iload           18
        //   729: istore          24
        //   731: lload           11
        //   733: lstore          25
        //   735: goto            233
        //   738: astore          21
        //   740: lload           9
        //   742: lstore          22
        //   744: aload           13
        //   746: astore          21
        //   748: aload           14
        //   750: astore          29
        //   752: iload           15
        //   754: istore          28
        //   756: iload           17
        //   758: istore          27
        //   760: iload           18
        //   762: istore          24
        //   764: lload           11
        //   766: lstore          25
        //   768: goto            233
        //   771: astore          21
        //   773: lload           9
        //   775: lstore          22
        //   777: aload           13
        //   779: astore          21
        //   781: aload           14
        //   783: astore          29
        //   785: iload           15
        //   787: istore          28
        //   789: iload           17
        //   791: istore          27
        //   793: iload           18
        //   795: istore          24
        //   797: lload           11
        //   799: lstore          25
        //   801: goto            233
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  197    210    771    804    Ljava/lang/IllegalArgumentException;
        //  287    294    705    738    Ljava/lang/NumberFormatException;
        //  330    337    738    771    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0363:
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
