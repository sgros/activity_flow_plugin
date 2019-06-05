// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.text.util;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.Iterator;
import android.support.v4.util.PatternsCompat;
import java.util.ArrayList;
import android.text.style.URLSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify$TransformFilter;
import android.text.util.Linkify$MatchFilter;
import android.text.util.Linkify;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import java.util.regex.Pattern;
import android.text.method.MovementMethod;
import android.text.method.LinkMovementMethod;
import android.support.annotation.NonNull;
import android.widget.TextView;
import java.util.Comparator;

public final class LinkifyCompat
{
    private static final Comparator<LinkSpec> COMPARATOR;
    private static final String[] EMPTY_STRING;
    
    static {
        EMPTY_STRING = new String[0];
        COMPARATOR = new Comparator<LinkSpec>() {
            @Override
            public final int compare(final LinkSpec linkSpec, final LinkSpec linkSpec2) {
                if (linkSpec.start < linkSpec2.start) {
                    return -1;
                }
                if (linkSpec.start > linkSpec2.start) {
                    return 1;
                }
                if (linkSpec.end < linkSpec2.end) {
                    return 1;
                }
                if (linkSpec.end > linkSpec2.end) {
                    return -1;
                }
                return 0;
            }
        };
    }
    
    private LinkifyCompat() {
    }
    
    private static void addLinkMovementMethod(@NonNull final TextView textView) {
        final MovementMethod movementMethod = textView.getMovementMethod();
        if ((movementMethod == null || !(movementMethod instanceof LinkMovementMethod)) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            Linkify.addLinks(textView, pattern, s);
            return;
        }
        addLinks(textView, pattern, s, null, null, null);
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            Linkify.addLinks(textView, pattern, s, linkify$MatchFilter, linkify$TransformFilter);
            return;
        }
        addLinks(textView, pattern, s, null, linkify$MatchFilter, linkify$TransformFilter);
    }
    
    public static final void addLinks(@NonNull final TextView textView, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final String[] array, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            Linkify.addLinks(textView, pattern, s, array, linkify$MatchFilter, linkify$TransformFilter);
            return;
        }
        final SpannableString value = SpannableString.valueOf(textView.getText());
        if (addLinks((Spannable)value, pattern, s, array, linkify$MatchFilter, linkify$TransformFilter)) {
            textView.setText((CharSequence)value);
            addLinkMovementMethod(textView);
        }
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, n);
        }
        if (n == 0) {
            return false;
        }
        final URLSpan[] array = (URLSpan[])spannable.getSpans(0, spannable.length(), (Class)URLSpan.class);
        for (int i = array.length - 1; i >= 0; --i) {
            spannable.removeSpan((Object)array[i]);
        }
        if ((n & 0x4) != 0x0) {
            Linkify.addLinks(spannable, 4);
        }
        final ArrayList<LinkSpec> list = new ArrayList<LinkSpec>();
        if ((n & 0x1) != 0x0) {
            gatherLinks(list, spannable, PatternsCompat.AUTOLINK_WEB_URL, new String[] { "http://", "https://", "rtsp://" }, Linkify.sUrlMatchFilter, null);
        }
        if ((n & 0x2) != 0x0) {
            gatherLinks(list, spannable, PatternsCompat.AUTOLINK_EMAIL_ADDRESS, new String[] { "mailto:" }, null, null);
        }
        if ((n & 0x8) != 0x0) {
            gatherMapLinks(list, spannable);
        }
        pruneOverlaps(list, spannable);
        if (list.size() == 0) {
            return false;
        }
        for (final LinkSpec linkSpec : list) {
            if (linkSpec.frameworkAddedSpan == null) {
                applyLink(linkSpec.url, linkSpec.start, linkSpec.end, spannable);
            }
        }
        return true;
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, pattern, s);
        }
        return addLinks(spannable, pattern, s, null, null, null);
    }
    
    public static final boolean addLinks(@NonNull final Spannable spannable, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(spannable, pattern, s, linkify$MatchFilter, linkify$TransformFilter);
        }
        return addLinks(spannable, pattern, s, null, linkify$MatchFilter, linkify$TransformFilter);
    }
    
    public static final boolean addLinks(@NonNull final Spannable input, @NonNull final Pattern pattern, @Nullable final String s, @Nullable final String[] array, @Nullable final Linkify$MatchFilter linkify$MatchFilter, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(input, pattern, s, array, linkify$MatchFilter, linkify$TransformFilter);
        }
        String s2;
        if ((s2 = s) == null) {
            s2 = "";
        }
        String[] empty_STRING = null;
        Label_0047: {
            if (array != null) {
                empty_STRING = array;
                if (array.length >= 1) {
                    break Label_0047;
                }
            }
            empty_STRING = LinkifyCompat.EMPTY_STRING;
        }
        final String[] array2 = new String[empty_STRING.length + 1];
        array2[0] = s2.toLowerCase(Locale.ROOT);
        int i = 0;
        while (i < empty_STRING.length) {
            final String s3 = empty_STRING[i];
            ++i;
            String lowerCase;
            if (s3 == null) {
                lowerCase = "";
            }
            else {
                lowerCase = s3.toLowerCase(Locale.ROOT);
            }
            array2[i] = lowerCase;
        }
        final Matcher matcher = pattern.matcher((CharSequence)input);
        boolean b = false;
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            if (linkify$MatchFilter == null || linkify$MatchFilter.acceptMatch((CharSequence)input, start, end)) {
                applyLink(makeUrl(matcher.group(0), array2, matcher, linkify$TransformFilter), start, end, input);
                b = true;
            }
        }
        return b;
    }
    
    public static final boolean addLinks(@NonNull final TextView textView, final int n) {
        if (Build$VERSION.SDK_INT >= 26) {
            return Linkify.addLinks(textView, n);
        }
        if (n == 0) {
            return false;
        }
        final CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            if (addLinks((Spannable)text, n)) {
                addLinkMovementMethod(textView);
                return true;
            }
            return false;
        }
        else {
            final SpannableString value = SpannableString.valueOf(text);
            if (addLinks((Spannable)value, n)) {
                addLinkMovementMethod(textView);
                textView.setText((CharSequence)value);
                return true;
            }
            return false;
        }
    }
    
    private static void applyLink(final String s, final int n, final int n2, final Spannable spannable) {
        spannable.setSpan((Object)new URLSpan(s), n, n2, 33);
    }
    
    private static void gatherLinks(final ArrayList<LinkSpec> list, final Spannable input, final Pattern pattern, final String[] array, final Linkify$MatchFilter linkify$MatchFilter, final Linkify$TransformFilter linkify$TransformFilter) {
        final Matcher matcher = pattern.matcher((CharSequence)input);
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            if (linkify$MatchFilter == null || linkify$MatchFilter.acceptMatch((CharSequence)input, start, end)) {
                final LinkSpec e = new LinkSpec();
                e.url = makeUrl(matcher.group(0), array, matcher, linkify$TransformFilter);
                e.start = start;
                e.end = end;
                list.add(e);
            }
        }
    }
    
    private static final void gatherMapLinks(final ArrayList<LinkSpec> p0, final Spannable p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //     4: astore_1       
        //     5: iconst_0       
        //     6: istore_2       
        //     7: aload_1        
        //     8: invokestatic    android/webkit/WebView.findAddress:(Ljava/lang/String;)Ljava/lang/String;
        //    11: astore_3       
        //    12: aload_3        
        //    13: ifnull          128
        //    16: aload_1        
        //    17: aload_3        
        //    18: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //    21: istore          4
        //    23: iload           4
        //    25: ifge            31
        //    28: goto            128
        //    31: new             Landroid/support/v4/text/util/LinkifyCompat$LinkSpec;
        //    34: astore          5
        //    36: aload           5
        //    38: invokespecial   android/support/v4/text/util/LinkifyCompat$LinkSpec.<init>:()V
        //    41: aload_3        
        //    42: invokevirtual   java/lang/String.length:()I
        //    45: iload           4
        //    47: iadd           
        //    48: istore          6
        //    50: aload           5
        //    52: iload           4
        //    54: iload_2        
        //    55: iadd           
        //    56: putfield        android/support/v4/text/util/LinkifyCompat$LinkSpec.start:I
        //    59: iload_2        
        //    60: iload           6
        //    62: iadd           
        //    63: istore_2       
        //    64: aload           5
        //    66: iload_2        
        //    67: putfield        android/support/v4/text/util/LinkifyCompat$LinkSpec.end:I
        //    70: aload_1        
        //    71: iload           6
        //    73: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    76: astore_1       
        //    77: aload_3        
        //    78: ldc_w           "UTF-8"
        //    81: invokestatic    java/net/URLEncoder.encode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    84: astore          7
        //    86: new             Ljava/lang/StringBuilder;
        //    89: astore_3       
        //    90: aload_3        
        //    91: invokespecial   java/lang/StringBuilder.<init>:()V
        //    94: aload_3        
        //    95: ldc_w           "geo:0,0?q="
        //    98: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   101: pop            
        //   102: aload_3        
        //   103: aload           7
        //   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   108: pop            
        //   109: aload           5
        //   111: aload_3        
        //   112: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   115: putfield        android/support/v4/text/util/LinkifyCompat$LinkSpec.url:Ljava/lang/String;
        //   118: aload_0        
        //   119: aload           5
        //   121: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   124: pop            
        //   125: goto            7
        //   128: return         
        //   129: astore_0       
        //   130: return         
        //   131: astore          5
        //   133: goto            7
        //    Signature:
        //  (Ljava/util/ArrayList<Landroid/support/v4/text/util/LinkifyCompat$LinkSpec;>;Landroid/text/Spannable;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  7      12     129    131    Ljava/lang/UnsupportedOperationException;
        //  16     23     129    131    Ljava/lang/UnsupportedOperationException;
        //  31     59     129    131    Ljava/lang/UnsupportedOperationException;
        //  64     77     129    131    Ljava/lang/UnsupportedOperationException;
        //  77     86     131    136    Ljava/io/UnsupportedEncodingException;
        //  77     86     129    131    Ljava/lang/UnsupportedOperationException;
        //  86     125    129    131    Ljava/lang/UnsupportedOperationException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0128:
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
    
    private static String makeUrl(@NonNull String string, @NonNull final String[] array, final Matcher matcher, @Nullable final Linkify$TransformFilter linkify$TransformFilter) {
        String transformUrl = string;
        if (linkify$TransformFilter != null) {
            transformUrl = linkify$TransformFilter.transformUrl(matcher, string);
        }
        int n = 0;
        int n2;
        while (true) {
            final boolean b = true;
            if (n >= array.length) {
                n2 = 0;
                string = transformUrl;
                break;
            }
            if (transformUrl.regionMatches(true, 0, array[n], 0, array[n].length())) {
                n2 = (b ? 1 : 0);
                string = transformUrl;
                if (!transformUrl.regionMatches(false, 0, array[n], 0, array[n].length())) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(array[n]);
                    sb.append(transformUrl.substring(array[n].length()));
                    string = sb.toString();
                    n2 = (b ? 1 : 0);
                    break;
                }
                break;
            }
            else {
                ++n;
            }
        }
        String string2 = string;
        if (n2 == 0) {
            string2 = string;
            if (array.length > 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(array[0]);
                sb2.append(string);
                string2 = sb2.toString();
            }
        }
        return string2;
    }
    
    private static final void pruneOverlaps(final ArrayList<LinkSpec> list, final Spannable spannable) {
        final int length = spannable.length();
        final int n = 0;
        final URLSpan[] array = (URLSpan[])spannable.getSpans(0, length, (Class)URLSpan.class);
        for (int i = 0; i < array.length; ++i) {
            final LinkSpec e = new LinkSpec();
            e.frameworkAddedSpan = array[i];
            e.start = spannable.getSpanStart((Object)array[i]);
            e.end = spannable.getSpanEnd((Object)array[i]);
            list.add(e);
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)LinkifyCompat.COMPARATOR);
        int size = list.size();
        int j = n;
        while (j < size - 1) {
            final LinkSpec linkSpec = list.get(j);
            final int index = j + 1;
            final LinkSpec linkSpec2 = list.get(index);
            if (linkSpec.start <= linkSpec2.start && linkSpec.end > linkSpec2.start) {
                int n2;
                if (linkSpec2.end > linkSpec.end && linkSpec.end - linkSpec.start <= linkSpec2.end - linkSpec2.start) {
                    if (linkSpec.end - linkSpec.start < linkSpec2.end - linkSpec2.start) {
                        n2 = j;
                    }
                    else {
                        n2 = -1;
                    }
                }
                else {
                    n2 = index;
                }
                if (n2 != -1) {
                    final URLSpan frameworkAddedSpan = list.get(n2).frameworkAddedSpan;
                    if (frameworkAddedSpan != null) {
                        spannable.removeSpan((Object)frameworkAddedSpan);
                    }
                    list.remove(n2);
                    --size;
                    continue;
                }
            }
            j = index;
        }
    }
    
    private static class LinkSpec
    {
        int end;
        URLSpan frameworkAddedSpan;
        int start;
        String url;
        
        LinkSpec() {
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface LinkifyMask {
    }
}
