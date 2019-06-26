// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.PhoneFormat;

import org.telegram.messenger.FileLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.ByteBuffer;

public class PhoneFormat
{
    private static volatile PhoneFormat Instance;
    public ByteBuffer buffer;
    public HashMap<String, ArrayList<String>> callingCodeCountries;
    public HashMap<String, CallingCodeInfo> callingCodeData;
    public HashMap<String, Integer> callingCodeOffsets;
    public HashMap<String, String> countryCallingCode;
    public byte[] data;
    public String defaultCallingCode;
    public String defaultCountry;
    private boolean initialzed;
    
    public PhoneFormat() {
        this.initialzed = false;
        this.init(null);
    }
    
    public PhoneFormat(final String s) {
        this.initialzed = false;
        this.init(s);
    }
    
    public static PhoneFormat getInstance() {
        final PhoneFormat instance;
        if ((instance = PhoneFormat.Instance) == null) {
            synchronized (PhoneFormat.class) {
                if (PhoneFormat.Instance == null) {
                    PhoneFormat.Instance = new PhoneFormat();
                }
            }
        }
        return instance;
    }
    
    public static String strip(final String str) {
        final StringBuilder sb = new StringBuilder(str);
        for (int i = sb.length() - 1; i >= 0; --i) {
            if (!"0123456789+*#".contains(sb.substring(i, i + 1))) {
                sb.deleteCharAt(i);
            }
        }
        return sb.toString();
    }
    
    public static String stripExceptNumbers(final String s) {
        return stripExceptNumbers(s, false);
    }
    
    public static String stripExceptNumbers(String string, final boolean b) {
        if (string == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(string);
        string = "0123456789";
        if (b) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("0123456789");
            sb2.append("+");
            string = sb2.toString();
        }
        for (int i = sb.length() - 1; i >= 0; --i) {
            if (!string.contains(sb.substring(i, i + 1))) {
                sb.deleteCharAt(i);
            }
        }
        return sb.toString();
    }
    
    public String callingCodeForCountryCode(final String s) {
        return this.countryCallingCode.get(s.toLowerCase());
    }
    
    public CallingCodeInfo callingCodeInfo(String e) {
        CallingCodeInfo value;
        final CallingCodeInfo callingCodeInfo = value = this.callingCodeData.get(e);
        if (callingCodeInfo == null) {
            final Integer n = this.callingCodeOffsets.get(e);
            value = callingCodeInfo;
            if (n != null) {
                final byte[] data = this.data;
                final int intValue = n;
                value = new CallingCodeInfo();
                value.callingCode = e;
                value.countries = this.callingCodeCountries.get(e);
                this.callingCodeData.put(e, value);
                final short value2 = this.value16(intValue);
                int n2 = 2;
                final int n3 = intValue + 2 + 2;
                final short value3 = this.value16(n3);
                final int n4 = n3 + 2 + 2;
                final short value4 = this.value16(n4);
                int n5 = n4 + 2 + 2;
                final ArrayList<String> trunkPrefixes = new ArrayList<String>(5);
                while (true) {
                    e = this.valueString(n5);
                    if (e.length() == 0) {
                        break;
                    }
                    trunkPrefixes.add(e);
                    n5 += e.length() + 1;
                }
                value.trunkPrefixes = trunkPrefixes;
                ++n5;
                final ArrayList<String> intlPrefixes = new ArrayList<String>(5);
                while (true) {
                    e = this.valueString(n5);
                    if (e.length() == 0) {
                        break;
                    }
                    intlPrefixes.add(e);
                    n5 += e.length() + 1;
                }
                value.intlPrefixes = intlPrefixes;
                final ArrayList ruleSets = new ArrayList<RuleSet>(value4);
                int n7;
                final int n6 = n7 = intValue + value2;
                short n8 = 0;
                final byte[] array = data;
                while (n8 < value4) {
                    final RuleSet e2 = new RuleSet();
                    e2.matchLen = this.value16(n7);
                    final int n9 = n7 + n2;
                    final short value5 = this.value16(n9);
                    final ArrayList rules = new ArrayList<PhoneRule>(value5);
                    n7 = n9 + n2;
                    for (short n10 = 0; n10 < value5; ++n10) {
                        final PhoneRule e3 = new PhoneRule();
                        e3.minVal = this.value32(n7);
                        n7 += 4;
                        e3.maxVal = this.value32(n7);
                        final int n11 = n7 + 4;
                        final int n12 = n11 + 1;
                        e3.byte8 = array[n11];
                        final int n13 = n12 + 1;
                        e3.maxLen = array[n12];
                        final int n14 = n13 + 1;
                        e3.otherFlag = array[n13];
                        final int n15 = n14 + 1;
                        e3.prefixLen = array[n14];
                        final int n16 = n15 + 1;
                        e3.flag12 = array[n15];
                        final int n17 = n16 + 1;
                        e3.flag13 = array[n16];
                        final short value6 = this.value16(n17);
                        n7 = n17 + n2;
                        e3.format = this.valueString(n6 + value3 + value6);
                        final int index = e3.format.indexOf("[[");
                        if (index != -1) {
                            e3.format = String.format("%s%s", e3.format.substring(0, index), e3.format.substring(e3.format.indexOf("]]") + 2));
                        }
                        n2 = 2;
                        rules.add(e3);
                        if (e3.hasIntlPrefix) {
                            e2.hasRuleWithIntlPrefix = true;
                        }
                        if (e3.hasTrunkPrefix) {
                            e2.hasRuleWithTrunkPrefix = true;
                        }
                    }
                    e2.rules = (ArrayList<PhoneRule>)rules;
                    ruleSets.add(e2);
                    ++n8;
                }
                value.ruleSets = (ArrayList<RuleSet>)ruleSets;
            }
        }
        return value;
    }
    
    public ArrayList countriesForCallingCode(final String s) {
        String substring = s;
        if (s.startsWith("+")) {
            substring = s.substring(1);
        }
        return this.callingCodeCountries.get(substring);
    }
    
    public String defaultCallingCode() {
        return this.callingCodeForCountryCode(this.defaultCountry);
    }
    
    public CallingCodeInfo findCallingCodeInfo(final String s) {
        CallingCodeInfo callingCodeInfo = null;
        int endIndex = 0;
        CallingCodeInfo callingCodeInfo2;
        do {
            callingCodeInfo2 = callingCodeInfo;
            if (endIndex >= 3) {
                break;
            }
            callingCodeInfo2 = callingCodeInfo;
            if (endIndex >= s.length()) {
                break;
            }
            ++endIndex;
            callingCodeInfo2 = this.callingCodeInfo(s.substring(0, endIndex));
        } while ((callingCodeInfo = callingCodeInfo2) == null);
        return callingCodeInfo2;
    }
    
    public String format(final String s) {
        if (!this.initialzed) {
            return s;
        }
        try {
            final String strip = strip(s);
            if (strip.startsWith("+")) {
                final String substring = strip.substring(1);
                final CallingCodeInfo callingCodeInfo = this.findCallingCodeInfo(substring);
                String string = s;
                if (callingCodeInfo != null) {
                    final String format = callingCodeInfo.format(substring);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(format);
                    string = sb.toString();
                }
                return string;
            }
            final CallingCodeInfo callingCodeInfo2 = this.callingCodeInfo(this.defaultCallingCode);
            if (callingCodeInfo2 == null) {
                return s;
            }
            final String matchingAccessCode = callingCodeInfo2.matchingAccessCode(strip);
            if (matchingAccessCode == null) {
                return callingCodeInfo2.format(strip);
            }
            final String substring2 = strip.substring(matchingAccessCode.length());
            final CallingCodeInfo callingCodeInfo3 = this.findCallingCodeInfo(substring2);
            String format2 = substring2;
            if (callingCodeInfo3 != null) {
                format2 = callingCodeInfo3.format(substring2);
            }
            if (format2.length() == 0) {
                return matchingAccessCode;
            }
            return String.format("%s %s", matchingAccessCode, format2);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return s;
        }
    }
    
    public void init(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore_3       
        //     4: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //     7: invokevirtual   android/content/Context.getAssets:()Landroid/content/res/AssetManager;
        //    10: ldc_w           "PhoneFormats.dat"
        //    13: invokevirtual   android/content/res/AssetManager.open:(Ljava/lang/String;)Ljava/io/InputStream;
        //    16: astore          4
        //    18: aload_2        
        //    19: astore          5
        //    21: aload           4
        //    23: astore          6
        //    25: new             Ljava/io/ByteArrayOutputStream;
        //    28: astore          7
        //    30: aload_2        
        //    31: astore          5
        //    33: aload           4
        //    35: astore          6
        //    37: aload           7
        //    39: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    42: sipush          1024
        //    45: newarray        B
        //    47: astore          6
        //    49: aload           4
        //    51: aload           6
        //    53: iconst_0       
        //    54: sipush          1024
        //    57: invokevirtual   java/io/InputStream.read:([BII)I
        //    60: istore          8
        //    62: iload           8
        //    64: iconst_m1      
        //    65: if_icmpeq       81
        //    68: aload           7
        //    70: aload           6
        //    72: iconst_0       
        //    73: iload           8
        //    75: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
        //    78: goto            49
        //    81: aload_0        
        //    82: aload           7
        //    84: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //    87: putfield        org/telegram/PhoneFormat/PhoneFormat.data:[B
        //    90: aload_0        
        //    91: aload_0        
        //    92: getfield        org/telegram/PhoneFormat/PhoneFormat.data:[B
        //    95: invokestatic    java/nio/ByteBuffer.wrap:([B)Ljava/nio/ByteBuffer;
        //    98: putfield        org/telegram/PhoneFormat/PhoneFormat.buffer:Ljava/nio/ByteBuffer;
        //   101: aload_0        
        //   102: getfield        org/telegram/PhoneFormat/PhoneFormat.buffer:Ljava/nio/ByteBuffer;
        //   105: getstatic       java/nio/ByteOrder.LITTLE_ENDIAN:Ljava/nio/ByteOrder;
        //   108: invokevirtual   java/nio/ByteBuffer.order:(Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
        //   111: pop            
        //   112: aload           7
        //   114: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //   117: goto            127
        //   120: astore          6
        //   122: aload           6
        //   124: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   127: aload           4
        //   129: ifnull          147
        //   132: aload           4
        //   134: invokevirtual   java/io/InputStream.close:()V
        //   137: goto            147
        //   140: astore          4
        //   142: aload           4
        //   144: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   147: aload_1        
        //   148: ifnull          166
        //   151: aload_1        
        //   152: invokevirtual   java/lang/String.length:()I
        //   155: ifeq            166
        //   158: aload_0        
        //   159: aload_1        
        //   160: putfield        org/telegram/PhoneFormat/PhoneFormat.defaultCountry:Ljava/lang/String;
        //   163: goto            179
        //   166: aload_0        
        //   167: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //   170: invokevirtual   java/util/Locale.getCountry:()Ljava/lang/String;
        //   173: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   176: putfield        org/telegram/PhoneFormat/PhoneFormat.defaultCountry:Ljava/lang/String;
        //   179: aload_0        
        //   180: new             Ljava/util/HashMap;
        //   183: dup            
        //   184: sipush          255
        //   187: invokespecial   java/util/HashMap.<init>:(I)V
        //   190: putfield        org/telegram/PhoneFormat/PhoneFormat.callingCodeOffsets:Ljava/util/HashMap;
        //   193: aload_0        
        //   194: new             Ljava/util/HashMap;
        //   197: dup            
        //   198: sipush          255
        //   201: invokespecial   java/util/HashMap.<init>:(I)V
        //   204: putfield        org/telegram/PhoneFormat/PhoneFormat.callingCodeCountries:Ljava/util/HashMap;
        //   207: aload_0        
        //   208: new             Ljava/util/HashMap;
        //   211: dup            
        //   212: bipush          10
        //   214: invokespecial   java/util/HashMap.<init>:(I)V
        //   217: putfield        org/telegram/PhoneFormat/PhoneFormat.callingCodeData:Ljava/util/HashMap;
        //   220: aload_0        
        //   221: new             Ljava/util/HashMap;
        //   224: dup            
        //   225: sipush          255
        //   228: invokespecial   java/util/HashMap.<init>:(I)V
        //   231: putfield        org/telegram/PhoneFormat/PhoneFormat.countryCallingCode:Ljava/util/HashMap;
        //   234: aload_0        
        //   235: invokevirtual   org/telegram/PhoneFormat/PhoneFormat.parseDataHeader:()V
        //   238: aload_0        
        //   239: iconst_1       
        //   240: putfield        org/telegram/PhoneFormat/PhoneFormat.initialzed:Z
        //   243: return         
        //   244: astore_1       
        //   245: aload           7
        //   247: astore          5
        //   249: goto            341
        //   252: astore          6
        //   254: aload           7
        //   256: astore_1       
        //   257: aload           6
        //   259: astore          7
        //   261: goto            289
        //   264: astore          7
        //   266: aload_3        
        //   267: astore_1       
        //   268: goto            289
        //   271: astore_1       
        //   272: aconst_null    
        //   273: astore          4
        //   275: aload           4
        //   277: astore          5
        //   279: goto            341
        //   282: astore          7
        //   284: aconst_null    
        //   285: astore          4
        //   287: aload_3        
        //   288: astore_1       
        //   289: aload_1        
        //   290: astore          5
        //   292: aload           4
        //   294: astore          6
        //   296: aload           7
        //   298: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   301: aload_1        
        //   302: ifnull          317
        //   305: aload_1        
        //   306: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //   309: goto            317
        //   312: astore_1       
        //   313: aload_1        
        //   314: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   317: aload           4
        //   319: ifnull          335
        //   322: aload           4
        //   324: invokevirtual   java/io/InputStream.close:()V
        //   327: goto            335
        //   330: astore_1       
        //   331: aload_1        
        //   332: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   335: return         
        //   336: astore_1       
        //   337: aload           6
        //   339: astore          4
        //   341: aload           5
        //   343: ifnull          361
        //   346: aload           5
        //   348: invokevirtual   java/io/ByteArrayOutputStream.close:()V
        //   351: goto            361
        //   354: astore          6
        //   356: aload           6
        //   358: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   361: aload           4
        //   363: ifnull          381
        //   366: aload           4
        //   368: invokevirtual   java/io/InputStream.close:()V
        //   371: goto            381
        //   374: astore          4
        //   376: aload           4
        //   378: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   381: aload_1        
        //   382: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      18     282    289    Ljava/lang/Exception;
        //  4      18     271    282    Any
        //  25     30     264    271    Ljava/lang/Exception;
        //  25     30     336    341    Any
        //  37     42     264    271    Ljava/lang/Exception;
        //  37     42     336    341    Any
        //  42     49     252    264    Ljava/lang/Exception;
        //  42     49     244    252    Any
        //  49     62     252    264    Ljava/lang/Exception;
        //  49     62     244    252    Any
        //  68     78     252    264    Ljava/lang/Exception;
        //  68     78     244    252    Any
        //  81     112    252    264    Ljava/lang/Exception;
        //  81     112    244    252    Any
        //  112    117    120    127    Ljava/lang/Exception;
        //  132    137    140    147    Ljava/lang/Exception;
        //  296    301    336    341    Any
        //  305    309    312    317    Ljava/lang/Exception;
        //  322    327    330    335    Ljava/lang/Exception;
        //  346    351    354    361    Ljava/lang/Exception;
        //  366    371    374    381    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0049:
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
    
    public boolean isPhoneNumberValid(String s) {
        final boolean initialzed = this.initialzed;
        final boolean b = true;
        boolean b2 = true;
        if (!initialzed) {
            return true;
        }
        final String strip = strip(s);
        if (strip.startsWith("+")) {
            s = strip.substring(1);
            final CallingCodeInfo callingCodeInfo = this.findCallingCodeInfo(s);
            if (callingCodeInfo == null || !callingCodeInfo.isValidPhoneNumber(s)) {
                b2 = false;
            }
            return b2;
        }
        final CallingCodeInfo callingCodeInfo2 = this.callingCodeInfo(this.defaultCallingCode);
        if (callingCodeInfo2 == null) {
            return false;
        }
        s = callingCodeInfo2.matchingAccessCode(strip);
        if (s == null) {
            return callingCodeInfo2.isValidPhoneNumber(strip);
        }
        s = strip.substring(s.length());
        if (s.length() != 0) {
            final CallingCodeInfo callingCodeInfo3 = this.findCallingCodeInfo(s);
            return callingCodeInfo3 != null && callingCodeInfo3.isValidPhoneNumber(s) && b;
        }
        return false;
    }
    
    public void parseDataHeader() {
        int i = 0;
        final int value32 = this.value32(0);
        int n = 4;
        while (i < value32) {
            final String valueString = this.valueString(n);
            n += 4;
            final String valueString2 = this.valueString(n);
            n += 4;
            final int value33 = this.value32(n);
            n += 4;
            if (valueString2.equals(this.defaultCountry)) {
                this.defaultCallingCode = valueString;
            }
            this.countryCallingCode.put(valueString2, valueString);
            this.callingCodeOffsets.put(valueString, value33 + (value32 * 12 + 4));
            ArrayList<String> value34;
            if ((value34 = this.callingCodeCountries.get(valueString)) == null) {
                value34 = new ArrayList<String>();
                this.callingCodeCountries.put(valueString, value34);
            }
            value34.add(valueString2);
            ++i;
        }
        final String defaultCallingCode = this.defaultCallingCode;
        if (defaultCallingCode != null) {
            this.callingCodeInfo(defaultCallingCode);
        }
    }
    
    short value16(final int n) {
        if (n + 2 <= this.data.length) {
            this.buffer.position(n);
            return this.buffer.getShort();
        }
        return 0;
    }
    
    int value32(final int n) {
        if (n + 4 <= this.data.length) {
            this.buffer.position(n);
            return this.buffer.getInt();
        }
        return 0;
    }
    
    public String valueString(final int offset) {
        int i = offset;
        try {
            while (i < this.data.length) {
                if (this.data[i] == 0) {
                    final int length = i - offset;
                    if (offset == length) {
                        return "";
                    }
                    return new String(this.data, offset, length);
                }
                else {
                    ++i;
                }
            }
            return "";
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
