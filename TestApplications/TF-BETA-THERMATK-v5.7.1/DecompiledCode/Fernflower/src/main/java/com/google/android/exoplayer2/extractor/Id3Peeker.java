package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;

public final class Id3Peeker {
   private final ParsableByteArray scratch = new ParsableByteArray(10);

   public Metadata peekId3Data(ExtractorInput var1, Id3Decoder.FramePredicate var2) throws IOException, InterruptedException {
      Metadata var3 = null;
      int var4 = 0;

      while(true) {
         try {
            var1.peekFully(this.scratch.data, 0, 10);
         } catch (EOFException var7) {
            break;
         }

         this.scratch.setPosition(0);
         if (this.scratch.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
            break;
         }

         this.scratch.skipBytes(3);
         int var5 = this.scratch.readSynchSafeInt();
         int var6 = var5 + 10;
         if (var3 == null) {
            byte[] var8 = new byte[var6];
            System.arraycopy(this.scratch.data, 0, var8, 0, 10);
            var1.peekFully(var8, 10, var5);
            var3 = (new Id3Decoder(var2)).decode(var8, var6);
         } else {
            var1.advancePeekPosition(var5);
         }

         var4 += var6;
      }

      var1.resetPeekPosition();
      var1.advancePeekPosition(var4);
      return var3;
   }
}
