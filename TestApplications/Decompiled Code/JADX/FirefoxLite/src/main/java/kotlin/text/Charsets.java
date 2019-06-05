package kotlin.text;

import com.adjust.sdk.Constants;
import java.nio.charset.Charset;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Charsets.kt */
public final class Charsets {
    public static final Charsets INSTANCE = new Charsets();
    public static final Charset ISO_8859_1;
    public static final Charset US_ASCII;
    public static final Charset UTF_16;
    public static final Charset UTF_16BE;
    public static final Charset UTF_16LE;
    public static final Charset UTF_8;

    static {
        Charset forName = Charset.forName(Constants.ENCODING);
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"UTF-8\")");
        UTF_8 = forName;
        forName = Charset.forName("UTF-16");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"UTF-16\")");
        UTF_16 = forName;
        forName = Charset.forName("UTF-16BE");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"UTF-16BE\")");
        UTF_16BE = forName;
        forName = Charset.forName("UTF-16LE");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"UTF-16LE\")");
        UTF_16LE = forName;
        forName = Charset.forName("US-ASCII");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"US-ASCII\")");
        US_ASCII = forName;
        forName = Charset.forName("ISO-8859-1");
        Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(\"ISO-8859-1\")");
        ISO_8859_1 = forName;
    }

    private Charsets() {
    }
}
