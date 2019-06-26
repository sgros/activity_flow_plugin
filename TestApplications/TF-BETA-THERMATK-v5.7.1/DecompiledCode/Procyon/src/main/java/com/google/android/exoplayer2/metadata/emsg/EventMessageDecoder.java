// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.emsg;

import java.nio.ByteBuffer;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.metadata.MetadataDecoder;

public final class EventMessageDecoder implements MetadataDecoder
{
    @Override
    public Metadata decode(final MetadataInputBuffer metadataInputBuffer) {
        final ByteBuffer data = metadataInputBuffer.data;
        final byte[] array = data.array();
        final int limit = data.limit();
        final ParsableByteArray parsableByteArray = new ParsableByteArray(array, limit);
        final String nullTerminatedString = parsableByteArray.readNullTerminatedString();
        Assertions.checkNotNull(nullTerminatedString);
        final String s = nullTerminatedString;
        final String nullTerminatedString2 = parsableByteArray.readNullTerminatedString();
        Assertions.checkNotNull(nullTerminatedString2);
        final String s2 = nullTerminatedString2;
        final long unsignedInt = parsableByteArray.readUnsignedInt();
        return new Metadata(new Metadata.Entry[] { new EventMessage(s, s2, Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000L, unsignedInt), parsableByteArray.readUnsignedInt(), Arrays.copyOfRange(array, parsableByteArray.getPosition(), limit), Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000000L, unsignedInt)) });
    }
}
