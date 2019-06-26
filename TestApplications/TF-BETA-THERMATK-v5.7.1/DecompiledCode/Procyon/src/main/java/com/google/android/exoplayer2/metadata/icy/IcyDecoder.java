// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.icy;

import java.util.regex.Matcher;
import com.google.android.exoplayer2.util.Log;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import java.util.regex.Pattern;
import com.google.android.exoplayer2.metadata.MetadataDecoder;

public final class IcyDecoder implements MetadataDecoder
{
    private static final Pattern METADATA_ELEMENT;
    
    static {
        METADATA_ELEMENT = Pattern.compile("(.+?)='(.+?)';");
    }
    
    @Override
    public Metadata decode(final MetadataInputBuffer metadataInputBuffer) {
        final ByteBuffer data = metadataInputBuffer.data;
        return this.decode(Util.fromUtf8Bytes(data.array(), 0, data.limit()));
    }
    
    Metadata decode(String input) {
        final Matcher matcher = IcyDecoder.METADATA_ELEMENT.matcher(input);
        final Metadata metadata = null;
        String str = input = null;
        for (int end = 0; matcher.find(end); end = matcher.end()) {
            final String lowerInvariant = Util.toLowerInvariant(matcher.group(1));
            final String group = matcher.group(2);
            int n = -1;
            final int hashCode = lowerInvariant.hashCode();
            if (hashCode != -315603473) {
                if (hashCode == 1646559960) {
                    if (lowerInvariant.equals("streamtitle")) {
                        n = 0;
                    }
                }
            }
            else if (lowerInvariant.equals("streamurl")) {
                n = 1;
            }
            if (n != 0) {
                if (n != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized ICY tag: ");
                    sb.append(str);
                    Log.w("IcyDecoder", sb.toString());
                }
                else {
                    input = group;
                }
            }
            else {
                str = group;
            }
        }
        if (str == null) {
            final Metadata metadata2 = metadata;
            if (input == null) {
                return metadata2;
            }
        }
        return new Metadata(new Metadata.Entry[] { new IcyInfo(str, input) });
    }
}
