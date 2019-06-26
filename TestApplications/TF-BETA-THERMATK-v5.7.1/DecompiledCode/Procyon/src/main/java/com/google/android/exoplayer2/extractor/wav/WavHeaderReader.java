// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.audio.WavUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.extractor.ExtractorInput;

final class WavHeaderReader
{
    public static WavHeader peek(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        Assertions.checkNotNull(extractorInput);
        final ParsableByteArray parsableByteArray = new ParsableByteArray(16);
        if (ChunkHeader.peek(extractorInput, parsableByteArray).id != WavUtil.RIFF_FOURCC) {
            return null;
        }
        extractorInput.peekFully(parsableByteArray.data, 0, 4);
        parsableByteArray.setPosition(0);
        final int int1 = parsableByteArray.readInt();
        if (int1 != WavUtil.WAVE_FOURCC) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported RIFF format: ");
            sb.append(int1);
            Log.e("WavHeaderReader", sb.toString());
            return null;
        }
        ChunkHeader chunkHeader;
        for (chunkHeader = ChunkHeader.peek(extractorInput, parsableByteArray); chunkHeader.id != WavUtil.FMT_FOURCC; chunkHeader = ChunkHeader.peek(extractorInput, parsableByteArray)) {
            extractorInput.advancePeekPosition((int)chunkHeader.size);
        }
        Assertions.checkState(chunkHeader.size >= 16L);
        extractorInput.peekFully(parsableByteArray.data, 0, 16);
        parsableByteArray.setPosition(0);
        final int littleEndianUnsignedShort = parsableByteArray.readLittleEndianUnsignedShort();
        final int littleEndianUnsignedShort2 = parsableByteArray.readLittleEndianUnsignedShort();
        final int littleEndianUnsignedIntToInt = parsableByteArray.readLittleEndianUnsignedIntToInt();
        final int littleEndianUnsignedIntToInt2 = parsableByteArray.readLittleEndianUnsignedIntToInt();
        final int littleEndianUnsignedShort3 = parsableByteArray.readLittleEndianUnsignedShort();
        final int littleEndianUnsignedShort4 = parsableByteArray.readLittleEndianUnsignedShort();
        final int i = littleEndianUnsignedShort2 * littleEndianUnsignedShort4 / 8;
        if (littleEndianUnsignedShort3 != i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Expected block alignment: ");
            sb2.append(i);
            sb2.append("; got: ");
            sb2.append(littleEndianUnsignedShort3);
            throw new ParserException(sb2.toString());
        }
        final int encodingForType = WavUtil.getEncodingForType(littleEndianUnsignedShort, littleEndianUnsignedShort4);
        if (encodingForType == 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unsupported WAV format: ");
            sb3.append(littleEndianUnsignedShort4);
            sb3.append(" bit/sample, type ");
            sb3.append(littleEndianUnsignedShort);
            Log.e("WavHeaderReader", sb3.toString());
            return null;
        }
        extractorInput.advancePeekPosition((int)chunkHeader.size - 16);
        return new WavHeader(littleEndianUnsignedShort2, littleEndianUnsignedIntToInt, littleEndianUnsignedIntToInt2, littleEndianUnsignedShort3, littleEndianUnsignedShort4, encodingForType);
    }
    
    public static void skipToData(final ExtractorInput extractorInput, final WavHeader wavHeader) throws IOException, InterruptedException {
        Assertions.checkNotNull(extractorInput);
        Assertions.checkNotNull(wavHeader);
        extractorInput.resetPeekPosition();
        ParsableByteArray parsableByteArray;
        ChunkHeader chunkHeader;
        StringBuilder sb;
        long n;
        StringBuilder sb2;
        for (parsableByteArray = new ParsableByteArray(8), chunkHeader = ChunkHeader.peek(extractorInput, parsableByteArray); chunkHeader.id != Util.getIntegerCodeForString("data"); chunkHeader = ChunkHeader.peek(extractorInput, parsableByteArray)) {
            sb = new StringBuilder();
            sb.append("Ignoring unknown WAV chunk: ");
            sb.append(chunkHeader.id);
            Log.w("WavHeaderReader", sb.toString());
            n = chunkHeader.size + 8L;
            if (chunkHeader.id == Util.getIntegerCodeForString("RIFF")) {
                n = 12L;
            }
            if (n > 2147483647L) {
                sb2 = new StringBuilder();
                sb2.append("Chunk is too large (~2GB+) to skip; id: ");
                sb2.append(chunkHeader.id);
                throw new ParserException(sb2.toString());
            }
            extractorInput.skipFully((int)n);
        }
        extractorInput.skipFully(8);
        wavHeader.setDataBounds(extractorInput.getPosition(), chunkHeader.size);
    }
    
    private static final class ChunkHeader
    {
        public final int id;
        public final long size;
        
        private ChunkHeader(final int id, final long size) {
            this.id = id;
            this.size = size;
        }
        
        public static ChunkHeader peek(final ExtractorInput extractorInput, final ParsableByteArray parsableByteArray) throws IOException, InterruptedException {
            extractorInput.peekFully(parsableByteArray.data, 0, 8);
            parsableByteArray.setPosition(0);
            return new ChunkHeader(parsableByteArray.readInt(), parsableByteArray.readLittleEndianUnsignedInt());
        }
    }
}
