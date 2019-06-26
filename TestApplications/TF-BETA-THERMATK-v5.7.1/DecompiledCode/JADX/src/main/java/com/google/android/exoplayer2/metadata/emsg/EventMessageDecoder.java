package com.google.android.exoplayer2.metadata.emsg;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class EventMessageDecoder implements MetadataDecoder {
    public Metadata decode(MetadataInputBuffer metadataInputBuffer) {
        ByteBuffer byteBuffer = metadataInputBuffer.data;
        byte[] array = byteBuffer.array();
        int limit = byteBuffer.limit();
        ParsableByteArray parsableByteArray = new ParsableByteArray(array, limit);
        String readNullTerminatedString = parsableByteArray.readNullTerminatedString();
        Assertions.checkNotNull(readNullTerminatedString);
        String str = readNullTerminatedString;
        readNullTerminatedString = parsableByteArray.readNullTerminatedString();
        Assertions.checkNotNull(readNullTerminatedString);
        String str2 = readNullTerminatedString;
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        long scaleLargeTimestamp = Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000000, readUnsignedInt);
        long scaleLargeTimestamp2 = Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000, readUnsignedInt);
        long readUnsignedInt2 = parsableByteArray.readUnsignedInt();
        byte[] copyOfRange = Arrays.copyOfRange(array, parsableByteArray.getPosition(), limit);
        return new Metadata(new EventMessage(str, str2, scaleLargeTimestamp2, readUnsignedInt2, copyOfRange, scaleLargeTimestamp));
    }
}
