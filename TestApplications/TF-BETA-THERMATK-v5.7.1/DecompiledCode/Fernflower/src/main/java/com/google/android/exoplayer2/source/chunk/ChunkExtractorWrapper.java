package com.google.android.exoplayer2.source.chunk;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

public final class ChunkExtractorWrapper implements ExtractorOutput {
   private final SparseArray bindingTrackOutputs;
   private long endTimeUs;
   public final Extractor extractor;
   private boolean extractorInitialized;
   private final Format primaryTrackManifestFormat;
   private final int primaryTrackType;
   private Format[] sampleFormats;
   private SeekMap seekMap;
   private ChunkExtractorWrapper.TrackOutputProvider trackOutputProvider;

   public ChunkExtractorWrapper(Extractor var1, int var2, Format var3) {
      this.extractor = var1;
      this.primaryTrackType = var2;
      this.primaryTrackManifestFormat = var3;
      this.bindingTrackOutputs = new SparseArray();
   }

   public void endTracks() {
      Format[] var1 = new Format[this.bindingTrackOutputs.size()];

      for(int var2 = 0; var2 < this.bindingTrackOutputs.size(); ++var2) {
         var1[var2] = ((ChunkExtractorWrapper.BindingTrackOutput)this.bindingTrackOutputs.valueAt(var2)).sampleFormat;
      }

      this.sampleFormats = var1;
   }

   public Format[] getSampleFormats() {
      return this.sampleFormats;
   }

   public SeekMap getSeekMap() {
      return this.seekMap;
   }

   public void init(ChunkExtractorWrapper.TrackOutputProvider var1, long var2, long var4) {
      this.trackOutputProvider = var1;
      this.endTimeUs = var4;
      if (!this.extractorInitialized) {
         this.extractor.init(this);
         if (var2 != -9223372036854775807L) {
            this.extractor.seek(0L, var2);
         }

         this.extractorInitialized = true;
      } else {
         Extractor var6 = this.extractor;
         long var7 = var2;
         if (var2 == -9223372036854775807L) {
            var7 = 0L;
         }

         var6.seek(0L, var7);

         for(int var9 = 0; var9 < this.bindingTrackOutputs.size(); ++var9) {
            ((ChunkExtractorWrapper.BindingTrackOutput)this.bindingTrackOutputs.valueAt(var9)).bind(var1, var4);
         }
      }

   }

   public void seekMap(SeekMap var1) {
      this.seekMap = var1;
   }

   public TrackOutput track(int var1, int var2) {
      ChunkExtractorWrapper.BindingTrackOutput var3 = (ChunkExtractorWrapper.BindingTrackOutput)this.bindingTrackOutputs.get(var1);
      ChunkExtractorWrapper.BindingTrackOutput var4 = var3;
      if (var3 == null) {
         boolean var5;
         if (this.sampleFormats == null) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assertions.checkState(var5);
         Format var6;
         if (var2 == this.primaryTrackType) {
            var6 = this.primaryTrackManifestFormat;
         } else {
            var6 = null;
         }

         var4 = new ChunkExtractorWrapper.BindingTrackOutput(var1, var2, var6);
         var4.bind(this.trackOutputProvider, this.endTimeUs);
         this.bindingTrackOutputs.put(var1, var4);
      }

      return var4;
   }

   private static final class BindingTrackOutput implements TrackOutput {
      private final DummyTrackOutput dummyTrackOutput;
      private long endTimeUs;
      private final int id;
      private final Format manifestFormat;
      public Format sampleFormat;
      private TrackOutput trackOutput;
      private final int type;

      public BindingTrackOutput(int var1, int var2, Format var3) {
         this.id = var1;
         this.type = var2;
         this.manifestFormat = var3;
         this.dummyTrackOutput = new DummyTrackOutput();
      }

      public void bind(ChunkExtractorWrapper.TrackOutputProvider var1, long var2) {
         if (var1 == null) {
            this.trackOutput = this.dummyTrackOutput;
         } else {
            this.endTimeUs = var2;
            this.trackOutput = var1.track(this.id, this.type);
            Format var4 = this.sampleFormat;
            if (var4 != null) {
               this.trackOutput.format(var4);
            }

         }
      }

      public void format(Format var1) {
         Format var2 = this.manifestFormat;
         Format var3 = var1;
         if (var2 != null) {
            var3 = var1.copyWithManifestFormatInfo(var2);
         }

         this.sampleFormat = var3;
         this.trackOutput.format(this.sampleFormat);
      }

      public int sampleData(ExtractorInput var1, int var2, boolean var3) throws IOException, InterruptedException {
         return this.trackOutput.sampleData(var1, var2, var3);
      }

      public void sampleData(ParsableByteArray var1, int var2) {
         this.trackOutput.sampleData(var1, var2);
      }

      public void sampleMetadata(long var1, int var3, int var4, int var5, TrackOutput.CryptoData var6) {
         long var7 = this.endTimeUs;
         if (var7 != -9223372036854775807L && var1 >= var7) {
            this.trackOutput = this.dummyTrackOutput;
         }

         this.trackOutput.sampleMetadata(var1, var3, var4, var5, var6);
      }
   }

   public interface TrackOutputProvider {
      TrackOutput track(int var1, int var2);
   }
}
