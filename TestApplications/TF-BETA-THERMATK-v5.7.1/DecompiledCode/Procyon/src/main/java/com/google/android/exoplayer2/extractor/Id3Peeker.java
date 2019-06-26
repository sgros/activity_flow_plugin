// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.ParsableByteArray;

public final class Id3Peeker
{
    private final ParsableByteArray scratch;
    
    public Id3Peeker() {
        this.scratch = new ParsableByteArray(10);
    }
    
    public Metadata peekId3Data(final ExtractorInput extractorInput, final Id3Decoder.FramePredicate framePredicate) throws IOException, InterruptedException {
        Object decode = null;
        int n = 0;
        while (true) {
            try {
                while (true) {
                    extractorInput.peekFully(this.scratch.data, 0, 10);
                    this.scratch.setPosition(0);
                    if (this.scratch.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
                        break;
                    }
                    this.scratch.skipBytes(3);
                    final int synchSafeInt = this.scratch.readSynchSafeInt();
                    final int n2 = synchSafeInt + 10;
                    if (decode == null) {
                        decode = new byte[n2];
                        System.arraycopy(this.scratch.data, 0, decode, 0, 10);
                        extractorInput.peekFully((byte[])decode, 10, synchSafeInt);
                        decode = new Id3Decoder(framePredicate).decode((byte[])decode, n2);
                    }
                    else {
                        extractorInput.advancePeekPosition(synchSafeInt);
                    }
                    n += n2;
                }
                extractorInput.resetPeekPosition();
                extractorInput.advancePeekPosition(n);
                return (Metadata)decode;
            }
            catch (EOFException ex) {
                continue;
            }
            break;
        }
    }
}
