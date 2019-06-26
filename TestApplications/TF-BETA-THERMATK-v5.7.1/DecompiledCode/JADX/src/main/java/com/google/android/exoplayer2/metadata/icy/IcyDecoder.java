package com.google.android.exoplayer2.metadata.icy;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IcyDecoder implements MetadataDecoder {
    private static final Pattern METADATA_ELEMENT = Pattern.compile("(.+?)='(.+?)';");

    public Metadata decode(MetadataInputBuffer metadataInputBuffer) {
        ByteBuffer byteBuffer = metadataInputBuffer.data;
        return decode(Util.fromUtf8Bytes(byteBuffer.array(), 0, byteBuffer.limit()));
    }

    /* Access modifiers changed, original: 0000 */
    public Metadata decode(String str) {
        Matcher matcher = METADATA_ELEMENT.matcher(str);
        String str2 = null;
        String str3 = str2;
        for (int i = 0; matcher.find(i); i = matcher.end()) {
            String toLowerInvariant = Util.toLowerInvariant(matcher.group(1));
            String group = matcher.group(2);
            Object obj = -1;
            int hashCode = toLowerInvariant.hashCode();
            if (hashCode != -315603473) {
                if (hashCode == 1646559960 && toLowerInvariant.equals("streamtitle")) {
                    obj = null;
                }
            } else if (toLowerInvariant.equals("streamurl")) {
                obj = 1;
            }
            if (obj == null) {
                str2 = group;
            } else if (obj != 1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized ICY tag: ");
                stringBuilder.append(str2);
                Log.m18w("IcyDecoder", stringBuilder.toString());
            } else {
                str3 = group;
            }
        }
        if (str2 == null && str3 == null) {
            return null;
        }
        return new Metadata(new IcyInfo(str2, str3));
    }
}
