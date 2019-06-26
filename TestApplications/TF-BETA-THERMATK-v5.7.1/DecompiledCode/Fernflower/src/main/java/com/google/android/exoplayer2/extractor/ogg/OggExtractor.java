package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

public class OggExtractor implements Extractor {
   public static final ExtractorsFactory FACTORY;
   private ExtractorOutput output;
   private StreamReader streamReader;
   private boolean streamReaderInitialized;

   static {
      FACTORY = _$$Lambda$OggExtractor$Ibu4KG2n586HVQ8R_UQJ8hUhsso.INSTANCE;
   }

   // $FF: synthetic method
   static Extractor[] lambda$static$0() {
      return new Extractor[]{new OggExtractor()};
   }

   private static ParsableByteArray resetPosition(ParsableByteArray var0) {
      var0.setPosition(0);
      return var0;
   }

   private boolean sniffInternal(ExtractorInput var1) throws IOException, InterruptedException {
      OggPageHeader var2 = new OggPageHeader();
      if (var2.populate(var1, true) && (var2.type & 2) == 2) {
         int var3 = Math.min(var2.bodySize, 8);
         ParsableByteArray var4 = new ParsableByteArray(var3);
         var1.peekFully(var4.data, 0, var3);
         resetPosition(var4);
         if (FlacReader.verifyBitstreamType(var4)) {
            this.streamReader = new FlacReader();
            return true;
         }

         resetPosition(var4);
         if (VorbisReader.verifyBitstreamType(var4)) {
            this.streamReader = new VorbisReader();
            return true;
         }

         resetPosition(var4);
         if (OpusReader.verifyBitstreamType(var4)) {
            this.streamReader = new OpusReader();
            return true;
         }
      }

      return false;
   }

   public void init(ExtractorOutput var1) {
      this.output = var1;
   }

   public int read(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      if (this.streamReader == null) {
         if (!this.sniffInternal(var1)) {
            throw new ParserException("Failed to determine bitstream type");
         }

         var1.resetPeekPosition();
      }

      if (!this.streamReaderInitialized) {
         TrackOutput var3 = this.output.track(0, 1);
         this.output.endTracks();
         this.streamReader.init(this.output, var3);
         this.streamReaderInitialized = true;
      }

      return this.streamReader.read(var1, var2);
   }

   public void release() {
   }

   public void seek(long var1, long var3) {
      StreamReader var5 = this.streamReader;
      if (var5 != null) {
         var5.seek(var1, var3);
      }

   }

   public boolean sniff(ExtractorInput var1) throws IOException, InterruptedException {
      try {
         boolean var2 = this.sniffInternal(var1);
         return var2;
      } catch (ParserException var3) {
         return false;
      }
   }
}
