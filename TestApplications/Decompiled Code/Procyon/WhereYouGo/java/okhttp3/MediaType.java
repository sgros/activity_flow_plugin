// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.regex.Pattern;

public final class MediaType
{
    private static final Pattern PARAMETER;
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final Pattern TYPE_SUBTYPE;
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;
    
    static {
        TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
        PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
    }
    
    private MediaType(final String mediaType, final String type, final String subtype, final String charset) {
        this.mediaType = mediaType;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }
    
    public static MediaType parse(final String str) {
        final MediaType mediaType = null;
        final Matcher matcher = MediaType.TYPE_SUBTYPE.matcher(str);
        MediaType mediaType2;
        if (!matcher.lookingAt()) {
            mediaType2 = mediaType;
        }
        else {
            final String lowerCase = matcher.group(1).toLowerCase(Locale.US);
            final String lowerCase2 = matcher.group(2).toLowerCase(Locale.US);
            String anotherString = null;
            final Matcher matcher2 = MediaType.PARAMETER.matcher(str);
            String s;
            for (int i = matcher.end(); i < str.length(); i = matcher2.end(), anotherString = s) {
                matcher2.region(i, str.length());
                mediaType2 = mediaType;
                if (!matcher2.lookingAt()) {
                    return mediaType2;
                }
                final String group = matcher2.group(1);
                s = anotherString;
                if (group != null) {
                    if (!group.equalsIgnoreCase("charset")) {
                        s = anotherString;
                    }
                    else {
                        s = matcher2.group(2);
                        if (s != null) {
                            if (s.startsWith("'") && s.endsWith("'") && s.length() > 2) {
                                s = s.substring(1, s.length() - 1);
                            }
                        }
                        else {
                            s = matcher2.group(3);
                        }
                        if (anotherString != null && !s.equalsIgnoreCase(anotherString)) {
                            throw new IllegalArgumentException("Multiple different charsets: " + str);
                        }
                    }
                }
            }
            mediaType2 = new MediaType(str, lowerCase, lowerCase2, anotherString);
        }
        return mediaType2;
    }
    
    public Charset charset() {
        Charset forName;
        if (this.charset != null) {
            forName = Charset.forName(this.charset);
        }
        else {
            forName = null;
        }
        return forName;
    }
    
    public Charset charset(Charset forName) {
        if (this.charset != null) {
            forName = Charset.forName(this.charset);
        }
        return forName;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof MediaType && ((MediaType)o).mediaType.equals(this.mediaType);
    }
    
    @Override
    public int hashCode() {
        return this.mediaType.hashCode();
    }
    
    public String subtype() {
        return this.subtype;
    }
    
    @Override
    public String toString() {
        return this.mediaType;
    }
    
    public String type() {
        return this.type;
    }
}
