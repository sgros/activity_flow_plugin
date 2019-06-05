// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import java.nio.charset.Charset;

public final class Charsets
{
    public static final Charsets INSTANCE;
    public static final Charset ISO_8859_1;
    public static final Charset US_ASCII;
    public static final Charset UTF_16;
    public static final Charset UTF_16BE;
    public static final Charset UTF_16LE;
    public static final Charset UTF_8;
    
    static {
        INSTANCE = new Charsets();
        final Charset forName = Charset.forName("UTF-8");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"UTF-8\")");
        UTF_8 = forName;
        final Charset forName2 = Charset.forName("UTF-16");
        Intrinsics.checkExpressionValueIsNotNull(forName2, "Charset.forName(\"UTF-16\")");
        UTF_16 = forName2;
        final Charset forName3 = Charset.forName("UTF-16BE");
        Intrinsics.checkExpressionValueIsNotNull(forName3, "Charset.forName(\"UTF-16BE\")");
        UTF_16BE = forName3;
        final Charset forName4 = Charset.forName("UTF-16LE");
        Intrinsics.checkExpressionValueIsNotNull(forName4, "Charset.forName(\"UTF-16LE\")");
        UTF_16LE = forName4;
        final Charset forName5 = Charset.forName("US-ASCII");
        Intrinsics.checkExpressionValueIsNotNull(forName5, "Charset.forName(\"US-ASCII\")");
        US_ASCII = forName5;
        final Charset forName6 = Charset.forName("ISO-8859-1");
        Intrinsics.checkExpressionValueIsNotNull(forName6, "Charset.forName(\"ISO-8859-1\")");
        ISO_8859_1 = forName6;
    }
    
    private Charsets() {
    }
}
